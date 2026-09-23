package com.huabo.bigmodel.wenxinclaw;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

/**
 * WenxinClaw 网关直连客户端（后端定时任务发送通道）
 *
 * 协议与前端 src/api/ai/wenxinclaw.js 完全对齐：
 * - WebSocket 连接网关（wss://...?token=...）
 * - connect.challenge 挑战 → Ed25519 签名（v2|deviceId|clientId|mode|role|scopes|signedAtMs|token|nonce）→ connect 请求
 * - chat.send 发送消息，等待 chat 事件 state=final 拿最终回复
 * - 设备身份（Ed25519 密钥对）持久化在 ai_wenxinclaw_device_identity 表，首次自动生成
 *
 * 注意：新设备需要在 XingGuang 终端一次性审批（xingguang devices approve --latest），
 * 与前端浏览器首次配对流程相同；未审批时发送会报 "pairing required"。
 */
@Slf4j
@Component
public class WenxinClawGatewayClient {

    static {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    @Value("${wenxinclaw.gateway-url:wss://xg.example.com}")
    private String gatewayUrl;

    @Value("${wenxinclaw.token:}")
    private String token;

    @Value("${wenxinclaw.session-key:agent:main:main}")
    private String sessionKey;

    @Value("${wenxinclaw.client-id:gateway-client}")
    private String clientId;

    /** client.mode 必须用网关白名单值；backend=后端服务（不触发浏览器 Origin 校验），webchat/webchat-ui 会被 Origin 校验拒绝 */
    @Value("${wenxinclaw.client-mode:backend}")
    private String clientMode;

    @Value("${wenxinclaw.connect-timeout-seconds:20}")
    private int connectTimeoutSeconds;

    @Value("${wenxinclaw.response-timeout-seconds:180}")
    private int responseTimeoutSeconds;

    private final JdbcTemplate dmJdbcTemplate;

    private OkHttpClient httpClient;

    /** 设备身份（raw 公钥 Base64Url + PKCS8 私钥 Base64Url + SHA256 指纹） */
    private static class DeviceIdentity {
        String deviceId;
        String publicKeyBase64Url;
        String privateKeyPkcs8Base64Url;
    }

    /** 发送结果 */
    public static class SendResult {
        public boolean success;
        public String responseText;
        public String error;
    }

    public WenxinClawGatewayClient(@Qualifier("dmJdbcTemplate") JdbcTemplate dmJdbcTemplate) {
        this.dmJdbcTemplate = dmJdbcTemplate;
    }

    @PostConstruct
    public void init() {
        this.httpClient = new OkHttpClient.Builder()
                .dns(com.huabo.bigmodel.config.Ipv4FirstDns.INSTANCE)
                .connectTimeout(connectTimeoutSeconds, TimeUnit.SECONDS)
                .readTimeout(0, TimeUnit.MILLISECONDS) // WebSocket 长读不超时，由 Future 超时控制
                // 不设 pingInterval：网关侧（或其代理）不回 pong，OkHttp 心跳看门狗会在约 30~60 秒
                // 判死连接导致长回复中途失败（"WebSocket 连接失败/中断"）；前端浏览器无 ping 也能
                // 维持长连接，证明网关容忍无心跳长连接
                .build();
        log.info("WenxinClaw网关客户端初始化: url={}, clientId={}", gatewayUrl, clientId);
    }

    /**
     * 发送一条消息到 wenxinclaw 会话，等待 AI 最终回复后返回。
     * 每次发送独立建连（挑战握手→chat.send→等 final→关闭），无状态、简单可靠。
     */
    public SendResult sendMessage(String content) {
        SendResult result = new SendResult();
        WebSocket[] wsHolder = new WebSocket[1];
        try {
            DeviceIdentity identity = loadOrCreateIdentity();

            CompletableFuture<Void> connected = new CompletableFuture<>();
            CompletableFuture<String> finalResponse = new CompletableFuture<>();
            // 连接失败时联动失败两个 Future，避免悬挂等待
            Runnable failAll = () -> {
                connected.completeExceptionally(new IllegalStateException("WebSocket 连接失败/中断"));
                finalResponse.completeExceptionally(new IllegalStateException("WebSocket 连接失败/中断"));
            };

            String url = gatewayUrl + (token != null && !token.isEmpty() ? "?token=" + token : "");
            Request request = new Request.Builder().url(url).build();

            httpClient.newWebSocket(request, new WebSocketListener() {
                @Override
                public void onOpen(WebSocket webSocket, Response response) {
                    wsHolder[0] = webSocket;
                    log.info("[WenxinClaw-Java] WebSocket 已打开，等待连接挑战...");
                }

                @Override
                public void onMessage(WebSocket webSocket, String text) {
                    handleServerMessage(webSocket, text, identity, connected, finalResponse);
                }

                @Override
                public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                    log.warn("[WenxinClaw-Java] WebSocket 失败: {}", t.getMessage());
                    failAll.run();
                }

                @Override
                public void onClosed(WebSocket webSocket, int code, String reason) {
                    log.info("[WenxinClaw-Java] WebSocket 关闭: {} {}", code, reason);
                    finalResponse.completeExceptionally(
                            new IllegalStateException("连接已关闭(code=" + code + ")"));
                }
            });

            // 1. 等待握手完成
            try {
                connected.get(connectTimeoutSeconds, TimeUnit.SECONDS);
            } catch (Exception e) {
                result.error = "网关握手失败: " + rootMessage(e);
                closeQuietly(wsHolder[0]);
                return result;
            }

            // 2. 发送消息
            JSONObject chatReq = new JSONObject();
            chatReq.put("type", "req");
            chatReq.put("id", nextRequestId());
            chatReq.put("method", "chat.send");
            JSONObject params = new JSONObject();
            params.put("sessionKey", sessionKey);
            params.put("message", content);
            params.put("idempotencyKey",
                    "msg_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8));
            chatReq.put("params", params);
            wsHolder[0].send(chatReq.toJSONString());

            // 3. 等待 AI 最终回复
            try {
                result.responseText = finalResponse.get(responseTimeoutSeconds, TimeUnit.SECONDS);
                result.success = true;
            } catch (Exception e) {
                result.error = "等待回复失败: " + rootMessage(e);
            }
            closeQuietly(wsHolder[0]);
            return result;
        } catch (Exception e) {
            log.error("[WenxinClaw-Java] 发送异常", e);
            result.error = e.getMessage();
            closeQuietly(wsHolder[0]);
            return result;
        }
    }

    /**
     * 通用网关 RPC（握手后发送单个请求并等待对应响应），如 sessions.reset。
     * 返回 null 表示失败（错误已记录日志）。
     */
    public JSONObject rpcRequest(String method, JSONObject params) {
        WebSocket[] wsHolder = new WebSocket[1];
        String[] rpcId = new String[1];
        try {
            DeviceIdentity identity = loadOrCreateIdentity();
            CompletableFuture<Void> connected = new CompletableFuture<>();
            CompletableFuture<JSONObject> rpcResult = new CompletableFuture<>();

            String url = gatewayUrl + (token != null && !token.isEmpty() ? "?token=" + token : "");
            Request request = new Request.Builder().url(url).build();
            rpcId[0] = nextRequestId();

            httpClient.newWebSocket(request, new WebSocketListener() {
                @Override
                public void onOpen(WebSocket webSocket, Response response) {
                    wsHolder[0] = webSocket;
                }

                @Override
                public void onMessage(WebSocket webSocket, String text) {
                    try {
                        JSONObject msg = JSON.parseObject(text);
                        if ("event".equals(msg.getString("type"))) {
                            JSONObject payload = msg.getJSONObject("payload");
                            if ("connect.challenge".equals(msg.getString("event"))) {
                                String nonce = payload != null ? payload.getString("nonce") : null;
                                if (nonce != null && !nonce.isEmpty()) {
                                    sendConnect(webSocket, identity, nonce, connected);
                                }
                            }
                        } else if ("res".equals(msg.getString("type"))) {
                            boolean ok = Boolean.TRUE.equals(msg.getBoolean("ok"));
                            JSONObject payload = msg.getJSONObject("payload");
                            if (payload != null && (payload.containsKey("protocol") || payload.containsKey("server"))) {
                                connected.complete(null);
                                return;
                            }
                            if (rpcId[0].equals(msg.getString("id"))) {
                                if (ok) {
                                    rpcResult.complete(payload);
                                } else {
                                    Object errObj = msg.get("error");
                                    rpcResult.completeExceptionally(new IllegalStateException(
                                            errObj != null ? String.valueOf(errObj) : "网关返回失败"));
                                }
                            } else if (!ok) {
                                // 握手阶段的错误响应
                                connected.completeExceptionally(new IllegalStateException(String.valueOf(msg.get("error"))));
                                rpcResult.completeExceptionally(new IllegalStateException(String.valueOf(msg.get("error"))));
                            }
                        }
                    } catch (Exception e) {
                        log.warn("[WenxinClaw-Java] RPC消息处理失败: {}", e.getMessage());
                    }
                }

                @Override
                public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                    connected.completeExceptionally(new IllegalStateException("WebSocket 连接失败: " + t.getMessage()));
                    rpcResult.completeExceptionally(new IllegalStateException("WebSocket 连接失败: " + t.getMessage()));
                }

                @Override
                public void onClosed(WebSocket webSocket, int code, String reason) {
                    rpcResult.completeExceptionally(new IllegalStateException("连接已关闭(code=" + code + ")"));
                }
            });

            connected.get(connectTimeoutSeconds, TimeUnit.SECONDS);

            JSONObject req = new JSONObject();
            req.put("type", "req");
            req.put("id", rpcId[0]);
            req.put("method", method);
            req.put("params", params);
            wsHolder[0].send(req.toJSONString());

            JSONObject result = rpcResult.get(connectTimeoutSeconds, TimeUnit.SECONDS);
            closeQuietly(wsHolder[0]);
            return result;
        } catch (Exception e) {
            log.error("[WenxinClaw-Java] RPC {} 失败: {}", method, rootMessage(e));
            closeQuietly(wsHolder[0]);
            return null;
        }
    }

    /** 处理服务端消息：挑战→签名握手；res→连接确认/错误；chat 事件→捕获最终回复 */
    private void handleServerMessage(WebSocket ws, String text, DeviceIdentity identity,
            CompletableFuture<Void> connected, CompletableFuture<String> finalResponse) {
        try {
            JSONObject msg = JSON.parseObject(text);
            String type = msg.getString("type");
            if ("event".equals(type)) {
                String event = msg.getString("event");
                JSONObject payload = msg.getJSONObject("payload");
                if ("connect.challenge".equals(event)) {
                    String nonce = payload != null ? payload.getString("nonce") : null;
                    if (nonce == null || nonce.isEmpty()) {
                        connected.completeExceptionally(new IllegalStateException("挑战缺少 nonce"));
                        return;
                    }
                    sendConnect(ws, identity, nonce, connected);
                } else if ("chat".equals(event)) {
                    String state = payload != null ? payload.getString("state") : null;
                    if ("final".equals(state)) {
                        finalResponse.complete(extractReplyText(payload));
                    } else if ("error".equals(state)) {
                        finalResponse.completeExceptionally(new IllegalStateException(
                                "服务端返回错误: " + payload.toJSONString()));
                    }
                }
                // delta / aborted / tick 等事件忽略
            } else if ("res".equals(type)) {
                boolean ok = Boolean.TRUE.equals(msg.getBoolean("ok"));
                JSONObject payload = msg.getJSONObject("payload");
                if (!ok) {
                    Object errObj = msg.get("error");
                    String errMsg = errObj instanceof String ? (String) errObj
                            : (errObj != null ? JSON.toJSONString(errObj) : "未知错误");
                    if (errMsg.contains("pairing required")) {
                        errMsg += "（新设备需在 XingGuang 终端执行一次: xingguang devices approve --latest）";
                    }
                    log.warn("[WenxinClaw-Java] 请求被拒绝: {}", errMsg);
                    connected.completeExceptionally(new IllegalStateException(errMsg));
                    finalResponse.completeExceptionally(new IllegalStateException(errMsg));
                    return;
                }
                if (payload != null && (payload.containsKey("protocol") || payload.containsKey("server"))) {
                    log.info("[WenxinClaw-Java] 网关握手成功");
                    connected.complete(null);
                }
            }
        } catch (Exception e) {
            log.warn("[WenxinClaw-Java] 消息处理失败: {}", e.getMessage());
        }
    }

    /** 构造并签名 connect 请求（对齐前端 sendConnect/buildDeviceAuthPayload） */
    private void sendConnect(WebSocket ws, DeviceIdentity identity, String nonce,
            CompletableFuture<Void> connected) {
        try {
            long signedAtMs = System.currentTimeMillis();
            List<String> scopes = Arrays.asList("operator.read", "operator.write");
            String payloadStr = String.join("|",
                    "v2",
                    identity.deviceId,
                    clientId,
                    clientMode,
                    "operator",
                    String.join(",", scopes),
                    String.valueOf(signedAtMs),
                    token != null ? token : "",
                    nonce);
            String signature = sign(identity.privateKeyPkcs8Base64Url, payloadStr.getBytes("UTF-8"));

            JSONObject params = new JSONObject();
            params.put("minProtocol", 3);
            params.put("maxProtocol", 3);
            JSONObject client = new JSONObject();
            client.put("id", clientId);
            client.put("version", "1.0.0");
            client.put("platform", "java");
            client.put("mode", clientMode);
            params.put("client", client);
            params.put("role", "operator");
            params.put("scopes", scopes);
            params.put("caps", new String[]{"tool-events"});
            params.put("userAgent", "hbyun-scheduler");
            params.put("locale", "zh-CN");
            JSONObject device = new JSONObject();
            device.put("id", identity.deviceId);
            device.put("publicKey", identity.publicKeyBase64Url);
            device.put("signature", signature);
            device.put("signedAt", signedAtMs);
            device.put("nonce", nonce);
            params.put("device", device);
            if (token != null && !token.isEmpty()) {
                JSONObject auth = new JSONObject();
                auth.put("token", token);
                params.put("auth", auth);
            }

            JSONObject request = new JSONObject();
            request.put("type", "req");
            request.put("id", nextRequestId());
            request.put("method", "connect");
            request.put("params", params);
            ws.send(request.toJSONString());
            log.info("[WenxinClaw-Java] 已发送 connect 请求, deviceId={}",
                    identity.deviceId.substring(0, Math.min(16, identity.deviceId.length())) + "...");
        } catch (Exception e) {
            connected.completeExceptionally(new IllegalStateException("签名/发送 connect 失败: " + e.getMessage()));
        }
    }

    /** 从 chat final 事件中提取 AI 回复文本（content 为数组或字符串） */
    private String extractReplyText(JSONObject payload) {
        try {
            JSONObject message = payload.getJSONObject("message");
            if (message == null) return "";
            Object content = message.get("content");
            if (content instanceof JSONArray) {
                JSONArray arr = (JSONArray) content;
                for (int i = 0; i < arr.size(); i++) {
                    JSONObject block = arr.getJSONObject(i);
                    if (block != null && "text".equals(block.getString("type"))) {
                        return block.getString("text");
                    }
                }
                return "";
            }
            if (content instanceof String) return (String) content;
        } catch (Exception e) {
            log.warn("[WenxinClaw-Java] 提取回复文本失败: {}", e.getMessage());
        }
        return "";
    }

    // ===== 设备身份（ai_wenxinclaw_device_identity 单行持久化）=====

    private DeviceIdentity loadOrCreateIdentity() throws Exception {
        List<DeviceIdentity> existing = dmJdbcTemplate.query(
                "SELECT DEVICE_ID, PUBLIC_KEY, PRIVATE_KEY FROM AI_WENXINCLAW_DEVICE_IDENTITY WHERE ID = ?",
                (rs, i) -> {
                    DeviceIdentity d = new DeviceIdentity();
                    d.deviceId = rs.getString("DEVICE_ID");
                    d.publicKeyBase64Url = rs.getString("PUBLIC_KEY");
                    d.privateKeyPkcs8Base64Url = rs.getString("PRIVATE_KEY");
                    return d;
                }, "1");
        if (!existing.isEmpty() && existing.get(0).deviceId != null) {
            return existing.get(0);
        }

        // 首次生成 Ed25519 身份并落库
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("Ed25519", BouncyCastleProvider.PROVIDER_NAME);
        KeyPair keyPair = kpg.generateKeyPair();
        byte[] pubSpki = keyPair.getPublic().getEncoded();
        byte[] pubRaw = Arrays.copyOfRange(pubSpki, pubSpki.length - 32, pubSpki.length);
        byte[] privPkcs8 = keyPair.getPrivate().getEncoded();

        DeviceIdentity identity = new DeviceIdentity();
        identity.publicKeyBase64Url = base64UrlEncode(pubRaw);
        identity.privateKeyPkcs8Base64Url = base64UrlEncode(privPkcs8);
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] fp = digest.digest(pubRaw);
        StringBuilder sb = new StringBuilder();
        for (byte b : fp) sb.append(String.format("%02x", b));
        identity.deviceId = sb.toString();

        dmJdbcTemplate.update(
                "INSERT INTO AI_WENXINCLAW_DEVICE_IDENTITY (ID, DEVICE_ID, PUBLIC_KEY, PRIVATE_KEY, CREATE_TIME) "
                        + "VALUES (?, ?, ?, ?, ?)",
                "1", identity.deviceId, identity.publicKeyBase64Url,
                identity.privateKeyPkcs8Base64Url, new Date());
        log.info("[WenxinClaw-Java] 已生成后端设备身份 deviceId={}（首次使用需在 XingGuang 终端审批配对）",
                identity.deviceId);
        return identity;
    }

    private String sign(String privateKeyPkcs8Base64Url, byte[] data) throws Exception {
        byte[] pkcs8 = base64UrlDecode(privateKeyPkcs8Base64Url);
        PrivateKey privateKey = KeyFactory.getInstance("Ed25519", BouncyCastleProvider.PROVIDER_NAME)
                .generatePrivate(new PKCS8EncodedKeySpec(pkcs8));
        Signature signature = Signature.getInstance("Ed25519", BouncyCastleProvider.PROVIDER_NAME);
        signature.initSign(privateKey, new SecureRandom());
        signature.update(data);
        return base64UrlEncode(signature.sign());
    }

    // ===== 工具 =====

    private String base64UrlEncode(byte[] bytes) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    private byte[] base64UrlDecode(String input) {
        String normalized = input.replace('-', '+').replace('_', '/');
        int pad = (4 - normalized.length() % 4) % 4;
        StringBuilder padded = new StringBuilder(normalized);
        for (int i = 0; i < pad; i++) padded.append('=');
        return Base64.getDecoder().decode(padded.toString());
    }

    private String nextRequestId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16)
                + "_" + System.currentTimeMillis();
    }

    private void closeQuietly(WebSocket ws) {
        try {
            if (ws != null) ws.close(1000, "bye");
        } catch (Exception ignored) {
        }
    }

    private String rootMessage(Throwable e) {
        Throwable t = e;
        while (t.getCause() != null) t = t.getCause();
        return t.getMessage() != null ? t.getMessage() : t.toString();
    }
}
