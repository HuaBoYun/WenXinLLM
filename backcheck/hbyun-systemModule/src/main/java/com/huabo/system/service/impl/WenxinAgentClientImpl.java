package com.huabo.system.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huabo.system.service.WenxinAgentClient;
import com.huabo.system.service.WenxinAgentStreamContext;

import lombok.extern.slf4j.Slf4j;

/**
 * WenxinAgent 客户端实现。
 *
 * <p>控制台 API 使用 RestTemplate 同步调用；
 * 对话流式接口使用 {@link HttpURLConnection} 长连接读流，
 * 由 Controller 用 {@link org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody}
 * 透传 SSE 帧。</p>
 *
 * <p>所有外部 URL 通过 application.yml 的 {@code wenxinAgent.console-api-url} 与
 * {@code wenxinAgent.service-api-url} 注入，便于环境切换。</p>
 */
@Slf4j
@Service
public class WenxinAgentClientImpl implements WenxinAgentClient {

    @Resource
    private RestTemplate restTemplate;

    @Value("${wenxinAgent.console-api-url:https://www.huabao.example.com/console/api}")
    private String consoleApiUrl;

    @Value("${wenxinAgent.service-api-url:https://www.huabao.example.com/v1}")
    private String serviceApiUrl;

    @Value("${wenxinAgent.connect-timeout:10000}")
    private int connectTimeout;

    @Value("${wenxinAgent.read-timeout:600000}")
    private int readTimeout;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Map<String, Object> ssoTokenExchange(String hbyunToken) {
        String url = consoleApiUrl + "/sso/token-exchange";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> body = new HashMap<>();
        body.put("token", hbyunToken);
        ResponseEntity<Map> resp = restTemplate.exchange(
                url, HttpMethod.POST, new HttpEntity<>(body, headers), Map.class);
        return resp.getBody();
    }

    @Override
    public Map<String, Object> createApp(String wenxinAgentAccessToken, String name, String description, String mode) {
        String url = consoleApiUrl + "/apps";
        HttpHeaders headers = bearerHeaders(wenxinAgentAccessToken);
        Map<String, Object> body = new HashMap<>();
        body.put("name", name);
        body.put("description", description == null ? "" : description);
        body.put("mode", mode == null ? "chat" : mode);
        body.put("icon_type", "emoji");
        body.put("icon", "\uD83E\uDD16");
        body.put("icon_background", "#FFEAD5");
        ResponseEntity<Map> resp = restTemplate.exchange(
                url, HttpMethod.POST, new HttpEntity<>(body, headers), Map.class);
        return resp.getBody();
    }

    @Override
    public Map<String, Object> updateModelConfig(String wenxinAgentAccessToken, String appId,
                                                  Map<String, Object> modelConfig) {
        String url = consoleApiUrl + "/apps/" + appId + "/model-config";
        HttpHeaders headers = bearerHeaders(wenxinAgentAccessToken);
        ResponseEntity<Map> resp = restTemplate.exchange(
                url, HttpMethod.POST, new HttpEntity<>(modelConfig, headers), Map.class);
        return resp.getBody();
    }

    @Override
    public Map<String, Object> createApiKey(String wenxinAgentAccessToken, String appId) {
        String url = consoleApiUrl + "/apps/" + appId + "/api-keys";
        HttpHeaders headers = bearerHeaders(wenxinAgentAccessToken);
        ResponseEntity<Map> resp = restTemplate.exchange(
                url, HttpMethod.POST, new HttpEntity<>(Collections.emptyMap(), headers), Map.class);
        return resp.getBody();
    }

    @Override
    public Map<String, Object> listApiKeys(String wenxinAgentAccessToken, String appId) {
        String url = consoleApiUrl + "/apps/" + appId + "/api-keys";
        HttpHeaders headers = bearerHeaders(wenxinAgentAccessToken);
        ResponseEntity<Map> resp = restTemplate.exchange(
                url, HttpMethod.GET, new HttpEntity<>(headers), Map.class);
        return resp.getBody();
    }

    @Override
    public WenxinAgentStreamContext openChatStream(String appApiKey, Map<String, Object> payload) {
        try {
            URL url = new URL(serviceApiUrl + "/chat-messages");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setConnectTimeout(connectTimeout);
            conn.setReadTimeout(readTimeout);
            conn.setRequestProperty("Authorization", "Bearer " + appApiKey);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "text/event-stream");
            String bodyJson = objectMapper.writeValueAsString(payload);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(bodyJson.getBytes(StandardCharsets.UTF_8));
            }
            int code = conn.getResponseCode();
            String contentType = conn.getContentType();
            InputStream stream = (code >= 200 && code < 300) ? conn.getInputStream() : conn.getErrorStream();
            return new WenxinAgentStreamContextImpl(conn, stream, code, contentType);
        } catch (IOException e) {
            log.error("openChatStream failed", e);
            throw new RuntimeException("调用 WenxinAgent 对话接口失败: " + e.getMessage(), e);
        }
    }

    /**
     * 通过 WenxinAgent Console API 获取应用详情（含 model_config）。
     *
     * <p>背景：当前部署 (huabao.example.com) 的 nginx 未暴露 Service API (`/v1/*`)，
     * 所有 POST `/v1/chat-messages` 都被 nginx 直接挡成 405。
     * 因此对话调用必须改走 Console API，路径
     * `POST /console/api/apps/{appId}/chat-messages`。该端点是 DEBUGGER 模式，
     * 要求 payload 里必须带应用现有的 model_config，所以先 GET 拿配置。</p>
     */
    @Override
    public Map<String, Object> getApp(String wenxinAgentAccessToken, String appId) {
        String url = consoleApiUrl + "/apps/" + appId;
        HttpHeaders headers = bearerHeaders(wenxinAgentAccessToken);
        ResponseEntity<Map> resp = restTemplate.exchange(
                url, HttpMethod.GET, new HttpEntity<>(headers), Map.class);
        return resp.getBody();
    }

    /**
     * 打开 Console API 的对话流（DEBUGGER 模式），透传 SSE 给前端。
     *
     * @param wenxinAgentAccessToken WenxinAgent 用户 access_token（来自 SSO）
     * @param appId           WenxinAgent 应用 ID
     * @param payload         必须包含 query / model_config / response_mode=streaming /
     *                        inputs / conversation_id / retriever_from 等字段
     */
    @Override
    public WenxinAgentStreamContext openConsoleChatStream(String wenxinAgentAccessToken, String appId,
                                                    Map<String, Object> payload) {
        return openConsoleStream(wenxinAgentAccessToken, appId, "/chat-messages", payload, "chat");
    }

    @Override
    public WenxinAgentStreamContext openConsoleAdvancedChatStream(String wenxinAgentAccessToken, String appId,
                                                            Map<String, Object> payload) {
        return openConsoleStream(wenxinAgentAccessToken, appId,
                "/advanced-chat/workflows/draft/run", payload, "advanced-chat");
    }

    @Override
    public WenxinAgentStreamContext openConsoleWorkflowStream(String wenxinAgentAccessToken, String appId,
                                                        Map<String, Object> payload) {
        return openConsoleStream(wenxinAgentAccessToken, appId,
                "/workflows/draft/run", payload, "workflow");
    }

    @Override
    public WenxinAgentStreamContext openConsoleCompletionStream(String wenxinAgentAccessToken, String appId,
                                                          Map<String, Object> payload) {
        return openConsoleStream(wenxinAgentAccessToken, appId,
                "/completion-messages", payload, "completion");
    }

    /**
     * 通用 Console SSE 流打开。被 4 个 mode 的入口共用。
     *
     * <p>设计要点：</p>
     * <ul>
     *   <li>不在这里做 payload 字段校验——Controller 已按 mode 组装好，这里只负责传输。</li>
     *   <li>错误状态码也返回 ErrorStream 给上层透传给前端 SSE error 帧，便于前端定位。</li>
     * </ul>
     *
     * @param endpointSuffix 例如 "/chat-messages"、"/workflows/draft/run"。必须以 "/" 开头
     * @param modeForLog     仅用于日志，定位是哪种 mode 失败
     */
    private WenxinAgentStreamContext openConsoleStream(String wenxinAgentAccessToken, String appId,
                                                 String endpointSuffix,
                                                 Map<String, Object> payload,
                                                 String modeForLog) {
        try {
            URL url = new URL(consoleApiUrl + "/apps/" + appId + endpointSuffix);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setConnectTimeout(connectTimeout);
            conn.setReadTimeout(readTimeout);
            conn.setRequestProperty("Authorization", "Bearer " + wenxinAgentAccessToken);
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Accept", "text/event-stream");
            String bodyJson = objectMapper.writeValueAsString(payload);
            try (OutputStream os = conn.getOutputStream()) {
                os.write(bodyJson.getBytes(StandardCharsets.UTF_8));
            }
            int code = conn.getResponseCode();
            String contentType = conn.getContentType();
            InputStream stream = (code >= 200 && code < 300) ? conn.getInputStream() : conn.getErrorStream();
            return new WenxinAgentStreamContextImpl(conn, stream, code, contentType);
        } catch (IOException e) {
            log.error("openConsoleStream failed mode={} appId={} endpoint={}",
                    modeForLog, appId, endpointSuffix, e);
            throw new RuntimeException("调用 WenxinAgent Console " + modeForLog + " 流式接口失败: "
                    + e.getMessage(), e);
        }
    }

    @Override
    public Map<String, Object> getWorkflowDraft(String wenxinAgentAccessToken, String appId) {
        String url = consoleApiUrl + "/apps/" + appId + "/workflows/draft";
        HttpHeaders headers = bearerHeaders(wenxinAgentAccessToken);
        ResponseEntity<Map> resp = restTemplate.exchange(
                url, HttpMethod.GET, new HttpEntity<>(headers), Map.class);
        return resp.getBody();
    }

    /**
     * 上传文件到 WenxinAgent Console 的 /files/upload 端点（multipart/form-data）。
     *
     * <p>使用 {@link ByteArrayResource} 把字节数组包装成 multipart part，并强制
     * 覆盖 {@code getFilename()} 让 WenxinAgent 端能拿到原始文件名（否则 Spring 会用 null
     * 文件名，WenxinAgent 抛 "filename not exists" 错）。</p>
     */
    @Override
    public Map<String, Object> uploadFile(String wenxinAgentAccessToken, byte[] fileBytes,
                                          String filename, String mimeType) {
        String url = consoleApiUrl + "/files/upload";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("Authorization", "Bearer " + (wenxinAgentAccessToken == null ? "" : wenxinAgentAccessToken));

        ByteArrayResource fileResource = new ByteArrayResource(fileBytes) {
            @Override
            public String getFilename() {
                return filename;
            }
        };

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        // WenxinAgent FileApi.post 用 request.files["file"] 拿，所以 part 名必须是 "file"
        HttpHeaders partHeaders = new HttpHeaders();
        if (mimeType != null && !mimeType.isEmpty()) {
            try {
                partHeaders.setContentType(MediaType.parseMediaType(mimeType));
            } catch (Exception ignored) {
                partHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            }
        }
        body.add("file", new HttpEntity<>(fileResource, partHeaders));

        ResponseEntity<Map> resp = restTemplate.exchange(
                url, HttpMethod.POST, new HttpEntity<>(body, headers), Map.class);
        return resp.getBody();
    }

    private HttpHeaders bearerHeaders(String accessToken) {
        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_JSON);
        h.set("Authorization", "Bearer " + (accessToken == null ? "" : accessToken));
        return h;
    }

    /** 简单包装 HttpURLConnection 流读上下文。 */
    private static class WenxinAgentStreamContextImpl implements WenxinAgentStreamContext {
        private final HttpURLConnection conn;
        private final InputStream stream;
        private final int statusCode;
        private final String contentType;

        WenxinAgentStreamContextImpl(HttpURLConnection conn, InputStream stream, int statusCode, String contentType) {
            this.conn = conn;
            this.stream = stream;
            this.statusCode = statusCode;
            this.contentType = contentType;
        }

        @Override public int getStatusCode() { return statusCode; }
        @Override public String getContentType() { return contentType; }
        @Override public InputStream getInputStream() { return stream; }

        @Override
        public void close() {
            try { if (stream != null) stream.close(); } catch (IOException ignored) { }
            if (conn != null) conn.disconnect();
        }
    }
}
