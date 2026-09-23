package com.huabo.system.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.huabo.system.service.WenxinAgentClient;
import com.huabo.system.service.WenxinAgentStreamContext;
import com.huabo.system.utils.MyJsonBean;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 智能体平台 (WenxinAgent) 对接控制器。
 *
 * <p>提供 6 类能力，前端通过网关 /setting/agentPlatform/** 访问：</p>
 * <ul>
 *   <li>/agentPlatform/login          - 用华博云 token 换 WenxinAgent access_token (SSO)</li>
 *   <li>/agentPlatform/apps/create    - 创建 WenxinAgent 应用并自动生成专属 API Key</li>
 *   <li>/agentPlatform/apps/{id}/api-keys (GET)  - 列出已有 API Key</li>
 *   <li>/agentPlatform/chat/{appId}   - 流式对话 (SSE 透传，按 app.mode 自动分派到对应端点)</li>
 *   <li>/agentPlatform/apps/{id}/workflow-inputs (GET) - 拉取 workflow 草稿的开始节点变量定义</li>
 *   <li>/agentPlatform/apps/{id}/files (POST) - 文件上传透传到 WenxinAgent，用于 workflow 文件输入</li>
 * </ul>
 *
 * <p><b>WenxinAgent 5 种 App Mode 的端点分派（chat 方法关键逻辑）：</b></p>
 * <ul>
 *   <li>chat / agent-chat → /apps/{id}/chat-messages，payload 含 model_config</li>
 *   <li>advanced-chat → /apps/{id}/advanced-chat/workflows/draft/run，无 model_config</li>
 *   <li>workflow → /apps/{id}/workflows/draft/run，仅 inputs 必填，无 query 概念</li>
 *   <li>completion → /apps/{id}/completion-messages，payload 含 model_config</li>
 * </ul>
 *
 * <p>所有控制台 (console) 操作由前端从 SSO 拿到 access_token 后通过
 * Authorization: Bearer 头传入；对话调用用应用专属 app-xxx 由前端从
 * 智能体记录里解出后传入，不与用户 token 混用。</p>
 */
@Slf4j
@RestController
@Tag(name = "智能体平台对接", description = "SSO/应用/API Key/对话流式")
public class AgentPlatformController {

    @Resource
    private WenxinAgentClient wenxinAgentClient;

    /**
     * 使用当前华博云 token 完成 SSO 登录智能体平台。
     *
     * @param hbyunToken 华博云登录 token (header)
     * @return 含 access_token / refresh_token 的 WenxinAgent 凭证
     */
    @PostMapping("/agentPlatform/login")
    @Operation(summary = "智能体平台 SSO 登录")
    public MyJsonBean<Map<String, Object>> login(@RequestHeader("token") String hbyunToken) {
        MyJsonBean<Map<String, Object>> jb = new MyJsonBean<>();
        try {
            Map<String, Object> resp = wenxinAgentClient.ssoTokenExchange(hbyunToken);
            jb.setCode(1);
            jb.setMsg("登录成功");
            jb.setData(resp);
        } catch (Exception e) {
            log.error("智能体平台 SSO 登录失败", e);
            jb.setCode(0);
            jb.setMsg("智能体平台登录失败: " + e.getMessage());
        }
        return jb;
    }

    /**
     * 创建 WenxinAgent 应用并自动签发一把专属 API Key，返回给前端落库。
     *
     * <p>注意：WenxinAgent 访问令牌通过 <b>X-WenxinAgent-Authorization</b> 头传入，
     * 而不是标准 Authorization。原因是项目通用 axios 拦截器会用华博云
     * token 强制覆盖 Authorization 头，导致 WenxinAgent token 丢失。</p>
     */
    @PostMapping("/agentPlatform/apps/create")
    @Operation(summary = "创建智能体应用并签发 API Key")
    public MyJsonBean<Map<String, Object>> createApp(
            @RequestHeader(value = "X-WenxinAgent-Authorization", required = false) String wenxinAgentAuth,
            @RequestHeader(value = "Authorization", required = false) String fallbackAuth,
            @RequestBody Map<String, Object> body) {
        MyJsonBean<Map<String, Object>> jb = new MyJsonBean<>();
        try {
            String token = stripBearer(wenxinAgentAuth != null && !wenxinAgentAuth.isEmpty() ? wenxinAgentAuth : fallbackAuth);
            String name = (String) body.getOrDefault("name", "");
            String description = (String) body.getOrDefault("description", "");
            String mode = (String) body.getOrDefault("mode", "chat");
            Map<String, Object> appResp = wenxinAgentClient.createApp(token, name, description, mode);
            String appId = appResp == null ? null : (String) appResp.get("id");
            if (appId == null) {
                jb.setCode(0);
                jb.setMsg("创建应用失败: 未拿到 appId");
                return jb;
            }
            Map<String, Object> keyResp = wenxinAgentClient.createApiKey(token, appId);
            String apiKey = keyResp == null ? null : (String) keyResp.get("token");
            Map<String, Object> data = new java.util.HashMap<>();
            data.put("appId", appId);
            data.put("apiKey", apiKey);
            data.put("apiUrl", null);
            data.put("app", appResp);
            data.put("apiKeyRaw", keyResp);
            jb.setCode(1);
            jb.setMsg("创建成功");
            jb.setData(data);
        } catch (Exception e) {
            log.error("创建智能体应用失败", e);
            jb.setCode(0);
            jb.setMsg("创建智能体应用失败: " + e.getMessage());
        }
        return jb;
    }

    /** 列出指定应用已有的 API Key，供前端"重新获取"场景使用。 */
    @GetMapping("/agentPlatform/apps/{appId}/api-keys")
    @Operation(summary = "查询智能体 API Key 列表")
    public MyJsonBean<Map<String, Object>> listApiKeys(
            @PathVariable String appId,
            @RequestHeader(value = "X-WenxinAgent-Authorization", required = false) String wenxinAgentAuth,
            @RequestHeader(value = "Authorization", required = false) String fallbackAuth) {
        MyJsonBean<Map<String, Object>> jb = new MyJsonBean<>();
        try {
            String token = stripBearer(wenxinAgentAuth != null && !wenxinAgentAuth.isEmpty() ? wenxinAgentAuth : fallbackAuth);
            Map<String, Object> data = wenxinAgentClient.listApiKeys(token, appId);
            jb.setCode(1);
            jb.setMsg("查询成功");
            jb.setData(data);
        } catch (Exception e) {
            log.error("查询智能体 API Key 失败 appId={}", appId, e);
            jb.setCode(0);
            jb.setMsg("查询失败: " + e.getMessage());
        }
        return jb;
    }

    /**
     * 更新应用的模型配置（包括系统提示词）。
     *
     * <p>调用 WenxinAgent 的 POST /console/api/apps/{appId}/model-config 接口，
     * 用于在创建应用后设置系统提示词、模型参数等配置。</p>
     *
     * <p>注意：该接口只支持 chat / agent-chat / completion 三种模式。</p>
     */
    @PostMapping("/agentPlatform/apps/{appId}/model-config")
    @Operation(summary = "更新智能体模型配置")
    public MyJsonBean<Map<String, Object>> updateModelConfig(
            @PathVariable String appId,
            @RequestHeader(value = "X-WenxinAgent-Authorization", required = false) String wenxinAgentAuth,
            @RequestHeader(value = "Authorization", required = false) String fallbackAuth,
            @RequestBody Map<String, Object> modelConfig) {
        MyJsonBean<Map<String, Object>> jb = new MyJsonBean<>();
        try {
            String token = stripBearer(wenxinAgentAuth != null && !wenxinAgentAuth.isEmpty() ? wenxinAgentAuth : fallbackAuth);
            log.info("更新模型配置 appId={}, token前缀={}", appId, token != null ? token.substring(0, Math.min(10, token.length())) : "null");
            Map<String, Object> data = wenxinAgentClient.updateModelConfig(token, appId, modelConfig);
            jb.setCode(1);
            jb.setMsg("更新成功");
            jb.setData(data);
        } catch (Exception e) {
            log.error("更新模型配置失败 appId={}", appId, e);
            jb.setCode(0);
            jb.setMsg("更新失败: " + e.getMessage());
        }
        return jb;
    }

    /**
     * 获取应用详情（包含模型配置）。
     *
     * <p>调用 WenxinAgent 的 GET /console/api/apps/{appId} 接口，
     * 获取应用的完整信息，包括 model_config 等配置。</p>
     */
    @GetMapping("/agentPlatform/apps/{appId}")
    @Operation(summary = "获取智能体应用详情")
    public MyJsonBean<Map<String, Object>> getAppDetail(
            @PathVariable String appId,
            @RequestHeader(value = "X-WenxinAgent-Authorization", required = false) String wenxinAgentAuth,
            @RequestHeader(value = "Authorization", required = false) String fallbackAuth) {
        MyJsonBean<Map<String, Object>> jb = new MyJsonBean<>();
        try {
            String token = stripBearer(wenxinAgentAuth != null && !wenxinAgentAuth.isEmpty() ? wenxinAgentAuth : fallbackAuth);
            log.info("获取应用详情 appId={}", appId);
            Map<String, Object> data = wenxinAgentClient.getApp(token, appId);
            jb.setCode(1);
            jb.setMsg("查询成功");
            jb.setData(data);
        } catch (Exception e) {
            log.error("获取应用详情失败 appId={}", appId, e);
            jb.setCode(0);
            jb.setMsg("查询失败: " + e.getMessage());
        }
        return jb;
    }

    /**
     * 流式对话。前端 fetch + ReadableStream 读取响应。
     *
     * <p>调用流程（走 Console API，不走 Service API）：</p>
     * <ol>
     *   <li>前端 fetch 请求带 <code>X-WenxinAgent-Authorization: Bearer {wenxinAgentAccessToken}</code></li>
     *   <li>后端 <code>GET /console/api/apps/{appId}</code> 取应用 model_config</li>
     *   <li>后端 <code>POST /console/api/apps/{appId}/chat-messages</code> 走 DEBUGGER 模式</li>
     *   <li>SSE 帧逐行透传给前端</li>
     * </ol>
     *
     * <p>为什么用 Console API 而不是 Service API：当前 huabao.example.com 的 nginx
     * 没有暴露 <code>/v1/*</code>，直接调 service api 会被 nginx 挡成 405。
     * Console API 已经验证可用。</p>
     *
     * <p>请求体形如：
     * {@code {"query":"hi","conversationId":"","user":"u1","inputs":{}}}</p>
     */
    @PostMapping(value = "/agentPlatform/chat/{appId}", produces = "text/event-stream;charset=UTF-8")
    @Operation(summary = "智能体流式对话 (SSE 透传)")
    public void chat(@PathVariable String appId,
                     @RequestHeader(value = "X-WenxinAgent-Authorization", required = false) String wenxinAgentAuth,
                     @RequestHeader(value = "Authorization", required = false) String fallbackAuth,
                     @RequestBody Map<String, Object> body,
                     HttpServletResponse response) {
        String wenxinAgentToken = stripBearer(
                wenxinAgentAuth != null && !wenxinAgentAuth.isEmpty() ? wenxinAgentAuth : fallbackAuth);
        if (wenxinAgentToken == null || wenxinAgentToken.isEmpty()) {
            writeError(response, 401, "缺少 X-WenxinAgent-Authorization 头（WenxinAgent access_token）");
            return;
        }

        // 准备 SSE 响应头
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("text/event-stream;charset=UTF-8");
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("X-Accel-Buffering", "no");
        response.setCharacterEncoding("UTF-8");

        WenxinAgentStreamContext ctx = null;
        try {
            // 1. 先拿应用元数据，识别 mode（决定端点 + payload 形状）
            Map<String, Object> app = wenxinAgentClient.getApp(wenxinAgentToken, appId);
            if (app == null) {
                writeSseError(response, "应用不存在");
                return;
            }
            String mode = String.valueOf(app.getOrDefault("mode", "chat"));

            // 2. 按 mode 组装 payload + 选择端点
            Map<String, Object> payload;
            switch (mode) {
                case "advanced-chat":
                    payload = buildAdvancedChatPayload(body);
                    log.info("chat appId={} mode=advanced-chat conversationId={} parentMessageId={} query={} filesCount={} files={}",
                            appId, payload.get("conversation_id"), payload.get("parent_message_id"),
                            payload.get("query"),
                            payload.get("files") == null ? 0 : ((List<?>) payload.get("files")).size(),
                            payload.get("files"));
                    ctx = wenxinAgentClient.openConsoleAdvancedChatStream(wenxinAgentToken, appId, payload);
                    break;
                case "workflow":
                    payload = buildWorkflowPayload(body);
                    log.info("chat appId={} mode=workflow inputsKeys={} filesCount={}",
                            appId,
                            ((Map<?, ?>) payload.get("inputs")).keySet(),
                            payload.get("files") == null ? 0 : ((List<?>) payload.get("files")).size());
                    ctx = wenxinAgentClient.openConsoleWorkflowStream(wenxinAgentToken, appId, payload);
                    break;
                case "completion":
                    payload = buildCompletionPayload(body, app);
                    if (payload == null) {
                        writeSseError(response, "completion 应用缺少 model_config");
                        return;
                    }
                    log.info("chat appId={} mode=completion query={}", appId, payload.get("query"));
                    ctx = wenxinAgentClient.openConsoleCompletionStream(wenxinAgentToken, appId, payload);
                    break;
                case "chat":
                case "agent-chat":
                default:
                    payload = buildChatPayload(body, app);
                    if (payload == null) {
                        writeSseError(response, "应用不存在或缺少 model_config");
                        return;
                    }
                    log.info("chat appId={} mode={} conversationId={} parentMessageId={} query={} filesCount={} files={}",
                            appId, mode,
                            payload.get("conversation_id"),
                            payload.get("parent_message_id"),
                            payload.get("query"),
                            payload.get("files") == null ? 0 : ((List<?>) payload.get("files")).size(),
                            payload.get("files"));
                    ctx = wenxinAgentClient.openConsoleChatStream(wenxinAgentToken, appId, payload);
                    break;
            }

            // 3. SSE 透传给前端
            try (PrintWriter writer = response.getWriter();
                 InputStream in = ctx.getInputStream();
                 BufferedReader reader = new BufferedReader(
                         new InputStreamReader(in, StandardCharsets.UTF_8))) {
                if (ctx.getStatusCode() < 200 || ctx.getStatusCode() >= 300) {
                    StringBuilder errBody = new StringBuilder();
                    String l;
                    while ((l = reader.readLine()) != null) errBody.append(l);
                    String safe = errBody.toString().replace("\n", " ").replace("\r", " ");
                    writer.write("event: error\n");
                    writer.write("data: " + safe + "\n\n");
                    writer.flush();
                    return;
                }
                String line;
                while ((line = reader.readLine()) != null) {
                    writer.write(line);
                    writer.write("\n");
                    if (line.isEmpty()) writer.flush();
                }
                writer.flush();
            }
        } catch (Exception e) {
            log.error("智能体流式对话失败 appId={}", appId, e);
            try {
                response.getWriter().write("event: error\ndata: " + e.getMessage() + "\n\n");
                response.getWriter().flush();
            } catch (Exception ignored) { }
        } finally {
            if (ctx != null) ctx.close();
        }
    }

    /** 在 SSE 流上写出一条错误事件并 flush。 */
    private void writeSseError(HttpServletResponse response, String msg) {
        try {
            PrintWriter w = response.getWriter();
            w.write("event: error\n");
            w.write("data: " + (msg == null ? "" : msg.replace("\n", " ")) + "\n\n");
            w.flush();
        } catch (Exception ignored) { }
    }

    private String stripBearer(String authorization) {
        if (authorization == null) return "";
        String s = authorization.trim();
        return s.toLowerCase().startsWith("bearer ") ? s.substring(7).trim() : s;
    }

    private void writeError(HttpServletResponse response, int status, String msg) {
        try {
            response.setStatus(status);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":0,\"msg\":\"" + msg + "\"}");
        } catch (Exception ignored) { }
    }

    // ===================== payload builders =====================

    /**
     * 组装 chat / agent-chat 模式的 payload。
     *
     * <p>需要应用的 model_config（清掉派生字段避免 WenxinAgent 校验抱怨）。
     * parent_message_id 用于串起多轮对话线程（见 wenxinAgent
     * api/core/prompt/utils/extract_thread_messages.py），不传会被当作新对话起点。</p>
     *
     * @return 组装好的 payload；如果应用缺 model_config 返回 null
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> buildChatPayload(Map<String, Object> body, Map<String, Object> app) {
        Object modelConfig = app == null ? null : app.get("model_config");
        if (modelConfig == null) return null;
        if (modelConfig instanceof Map) {
            Map<String, Object> mc = (Map<String, Object>) modelConfig;
            mc.remove("created_by");
            mc.remove("created_at");
            mc.remove("updated_by");
            mc.remove("updated_at");
        }
        Map<String, Object> payload = new HashMap<>();
        payload.put("query", body.getOrDefault("query", ""));
        Object inputs = body.get("inputs");
        payload.put("inputs", inputs == null ? new HashMap<>() : inputs);
        payload.put("model_config", modelConfig);
        payload.put("response_mode", "streaming");
        payload.put("retriever_from", "dev");
        String convId = (String) body.getOrDefault("conversationId", "");
        payload.put("conversation_id", (convId != null && !convId.isEmpty()) ? convId : null);
        String parentMsgId = (String) body.getOrDefault("parentMessageId", "");
        if (parentMsgId != null && !parentMsgId.isEmpty()) {
            payload.put("parent_message_id", parentMsgId);
        }
        if (body.containsKey("files")) payload.put("files", body.get("files"));
        return payload;
    }

    /**
     * 组装 advanced-chat (Chatflow) 模式的 payload。
     *
     * <p>对应 WenxinAgent {@code AdvancedChatWorkflowRunPayload}：仅有
     * inputs / query / conversation_id / parent_message_id / files 五个字段，
     * 不需要 model_config（Chatflow 的模型配置在 graph 里）。</p>
     */
    private Map<String, Object> buildAdvancedChatPayload(Map<String, Object> body) {
        Map<String, Object> payload = new HashMap<>();
        Object inputs = body.get("inputs");
        payload.put("inputs", inputs == null ? new HashMap<>() : inputs);
        payload.put("query", body.getOrDefault("query", ""));
        String convId = (String) body.getOrDefault("conversationId", "");
        if (convId != null && !convId.isEmpty()) payload.put("conversation_id", convId);
        String parentMsgId = (String) body.getOrDefault("parentMessageId", "");
        if (parentMsgId != null && !parentMsgId.isEmpty()) {
            payload.put("parent_message_id", parentMsgId);
        }
        if (body.containsKey("files")) payload.put("files", body.get("files"));
        return payload;
    }

    /**
     * 组装 workflow 模式的 payload。
     *
     * <p>对应 WenxinAgent {@code DraftWorkflowRunPayload}：只有 inputs(必填) + files。
     * 无 query 概念，调用方必须保证 body.inputs 非空。</p>
     */
    private Map<String, Object> buildWorkflowPayload(Map<String, Object> body) {
        Map<String, Object> payload = new HashMap<>();
        Object inputs = body.get("inputs");
        payload.put("inputs", inputs == null ? new HashMap<>() : inputs);
        if (body.containsKey("files")) payload.put("files", body.get("files"));
        return payload;
    }

    /**
     * 组装 completion 模式的 payload。
     *
     * <p>对应 WenxinAgent {@code CompletionMessageApi}：与 chat 类似但不带 conversation_id
     * 和 parent_message_id（completion 是单轮）。</p>
     *
     * @return 组装好的 payload；如果应用缺 model_config 返回 null
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> buildCompletionPayload(Map<String, Object> body, Map<String, Object> app) {
        Object modelConfig = app == null ? null : app.get("model_config");
        if (modelConfig == null) return null;
        if (modelConfig instanceof Map) {
            Map<String, Object> mc = (Map<String, Object>) modelConfig;
            mc.remove("created_by");
            mc.remove("created_at");
            mc.remove("updated_by");
            mc.remove("updated_at");
        }
        Map<String, Object> payload = new HashMap<>();
        payload.put("query", body.getOrDefault("query", ""));
        Object inputs = body.get("inputs");
        payload.put("inputs", inputs == null ? new HashMap<>() : inputs);
        payload.put("model_config", modelConfig);
        payload.put("response_mode", "streaming");
        if (body.containsKey("files")) payload.put("files", body.get("files"));
        return payload;
    }

    // ===================== workflow inputs & files endpoints =====================

    /**
     * 拉取 workflow / advanced-chat 应用的"开始节点"变量定义，前端用以渲染输入表单。
     *
     * <p>从 {@code graph.nodes} 找到 {@code data.type='start'} 的节点，把
     * {@code data.variables} 数组扁平化返回。每个变量大致结构：</p>
     * <pre>{
     *   variable: "source_lang",
     *   label:    "源语言",
     *   type:     "text-input" | "paragraph" | "select" | "number" | "file" | "file-list",
     *   required: true,
     *   max_length: 256,
     *   options:  ["中文","英文",...],
     *   default:  "中文",
     *   allowed_file_types: ["document","image"],
     *   allowed_file_extensions: [".pdf",".doc",...]
     * }</pre>
     *
     * <p>非 workflow / advanced-chat 应用调此接口会返回 code=0 + 空数组。</p>
     */
    @GetMapping("/agentPlatform/apps/{appId}/workflow-inputs")
    @Operation(summary = "获取 workflow 草稿的开始节点变量定义")
    @SuppressWarnings("unchecked")
    public MyJsonBean<Map<String, Object>> getWorkflowInputs(
            @PathVariable String appId,
            @RequestHeader(value = "X-WenxinAgent-Authorization", required = false) String wenxinAgentAuth,
            @RequestHeader(value = "Authorization", required = false) String fallbackAuth) {
        MyJsonBean<Map<String, Object>> jb = new MyJsonBean<>();
        try {
            String token = stripBearer(wenxinAgentAuth != null && !wenxinAgentAuth.isEmpty() ? wenxinAgentAuth : fallbackAuth);

            // 先识别 mode，避免对非 workflow 应用调 draft 接口被 WenxinAgent 404
            Map<String, Object> app = wenxinAgentClient.getApp(token, appId);
            String mode = app == null ? "" : String.valueOf(app.getOrDefault("mode", ""));
            Map<String, Object> data = new HashMap<>();
            data.put("appMode", mode);
            data.put("variables", new ArrayList<>());
            if (!"workflow".equals(mode) && !"advanced-chat".equals(mode)) {
                jb.setCode(1);
                jb.setMsg("应用模式不支持工作流输入: " + mode);
                jb.setData(data);
                return jb;
            }

            Map<String, Object> draft = wenxinAgentClient.getWorkflowDraft(token, appId);
            List<Map<String, Object>> vars = extractStartNodeVariables(draft);
            data.put("variables", vars);
            jb.setCode(1);
            jb.setMsg(vars.isEmpty()
                    ? "工作流暂无输入变量，请先在 WenxinAgent 控制台为开始节点配置变量"
                    : "获取成功");
            jb.setData(data);
        } catch (Exception e) {
            log.error("获取 workflow 输入变量失败 appId={}", appId, e);
            jb.setCode(0);
            jb.setMsg("获取失败: " + e.getMessage());
        }
        return jb;
    }

    /**
     * 从 workflow draft 返回结构里抽出开始节点的 variables 列表。
     *
     * <p>结构假设：{@code draft.graph.nodes[*]} 是图节点数组；
     * 每个节点形如 {@code {id, data:{type:"start", variables:[...]}}}。</p>
     */
    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> extractStartNodeVariables(Map<String, Object> draft) {
        if (draft == null) return Collections.emptyList();
        Object graph = draft.get("graph");
        if (!(graph instanceof Map)) return Collections.emptyList();
        Object nodes = ((Map<String, Object>) graph).get("nodes");
        if (!(nodes instanceof List)) return Collections.emptyList();
        for (Object n : (List<Object>) nodes) {
            if (!(n instanceof Map)) continue;
            Map<String, Object> node = (Map<String, Object>) n;
            Object dataObj = node.get("data");
            if (!(dataObj instanceof Map)) continue;
            Map<String, Object> nodeData = (Map<String, Object>) dataObj;
            if (!"start".equals(String.valueOf(nodeData.get("type")))) continue;
            Object vars = nodeData.get("variables");
            if (vars instanceof List) {
                List<Map<String, Object>> out = new ArrayList<>();
                for (Object v : (List<Object>) vars) {
                    if (v instanceof Map) out.add((Map<String, Object>) v);
                }
                return out;
            }
        }
        return Collections.emptyList();
    }

    /**
     * 文件上传透传到 WenxinAgent Console。
     *
     * <p>前端用 FormData 发 multipart，字段名约定 {@code file}；后端把字节流原样转发到
     * {@code POST /console/api/files/upload}，返回 WenxinAgent 的 FileResponse（含 id）。
     * 前端拿到 id 后，作为 {@code upload_file_id} 拼到 workflow run 的 files 参数里。</p>
     */
    @PostMapping("/agentPlatform/apps/{appId}/files")
    @Operation(summary = "文件上传透传 (供 workflow 文件输入使用)")
    public MyJsonBean<Map<String, Object>> uploadFile(
            @PathVariable String appId,
            @RequestParam("file") MultipartFile file,
            @RequestHeader(value = "X-WenxinAgent-Authorization", required = false) String wenxinAgentAuth,
            @RequestHeader(value = "Authorization", required = false) String fallbackAuth) {
        MyJsonBean<Map<String, Object>> jb = new MyJsonBean<>();
        try {
            String token = stripBearer(wenxinAgentAuth != null && !wenxinAgentAuth.isEmpty() ? wenxinAgentAuth : fallbackAuth);
            if (file == null || file.isEmpty()) {
                jb.setCode(0);
                jb.setMsg("上传文件不能为空");
                return jb;
            }
            Map<String, Object> resp = wenxinAgentClient.uploadFile(
                    token, file.getBytes(),
                    file.getOriginalFilename(),
                    file.getContentType());
            jb.setCode(1);
            jb.setMsg("上传成功");
            jb.setData(resp);
            log.info("uploadFile appId={} filename={} size={} fileId={}",
                    appId, file.getOriginalFilename(), file.getSize(),
                    resp == null ? null : resp.get("id"));
        } catch (Exception e) {
            log.error("文件上传失败 appId={} filename={}", appId,
                    file == null ? "<null>" : file.getOriginalFilename(), e);
            jb.setCode(0);
            jb.setMsg("上传失败: " + e.getMessage());
        }
        return jb;
    }

    /**
     * 获取应用的文件上传配置（用于对话页判断是否显示上传按钮）。
     *
     * <p>按 mode 从不同位置读取：</p>
     * <ul>
     *   <li>chat / agent-chat / completion → app.model_config.file_upload</li>
     *   <li>advanced-chat → workflow draft features.file_upload</li>
     *   <li>workflow → 固定返回 enabled=false（用开始节点变量代替）</li>
     * </ul>
     *
     * @return {@code {enabled, mode, allowedFileTypes, allowedFileExtensions, numberLimits}}
     */
    @GetMapping("/agentPlatform/apps/{appId}/file-upload-config")
    @Operation(summary = "获取应用文件上传配置")
    @SuppressWarnings("unchecked")
    public MyJsonBean<Map<String, Object>> getFileUploadConfig(
            @PathVariable String appId,
            @RequestHeader(value = "X-WenxinAgent-Authorization", required = false) String wenxinAgentAuth,
            @RequestHeader(value = "Authorization", required = false) String fallbackAuth) {
        MyJsonBean<Map<String, Object>> jb = new MyJsonBean<>();
        try {
            String token = stripBearer(wenxinAgentAuth != null && !wenxinAgentAuth.isEmpty() ? wenxinAgentAuth : fallbackAuth);
            Map<String, Object> app = wenxinAgentClient.getApp(token, appId);
            String mode = app == null ? "" : String.valueOf(app.getOrDefault("mode", ""));

            Map<String, Object> fileUpload = null;
            if ("chat".equals(mode) || "agent-chat".equals(mode) || "completion".equals(mode)) {
                Object mcObj = app.get("model_config");
                if (mcObj instanceof Map) {
                    Object fu = ((Map<String, Object>) mcObj).get("file_upload");
                    if (fu instanceof Map) fileUpload = (Map<String, Object>) fu;
                }
            } else if ("advanced-chat".equals(mode)) {
                Map<String, Object> draft = wenxinAgentClient.getWorkflowDraft(token, appId);
                Object features = draft == null ? null : draft.get("features");
                if (features instanceof Map) {
                    Object fu = ((Map<String, Object>) features).get("file_upload");
                    if (fu instanceof Map) fileUpload = (Map<String, Object>) fu;
                }
            }

            // 归一化响应结构，兼容 fileUpload 为 null / 空 map / 未启用等情形
            Map<String, Object> data = new HashMap<>();
            data.put("mode", mode);
            boolean enabled = fileUpload != null
                    && Boolean.TRUE.equals(fileUpload.get("enabled"));
            data.put("enabled", enabled);
            if (enabled) {
                data.put("allowedFileTypes", fileUpload.getOrDefault(
                        "allowed_file_types", Arrays.asList("image", "document")));
                data.put("allowedFileExtensions", fileUpload.getOrDefault(
                        "allowed_file_extensions",
                        Arrays.asList(".pdf", ".docx", ".jpg", ".jpeg", ".png",
                                ".txt", ".csv", ".xlsx")));
                data.put("numberLimits", fileUpload.getOrDefault("number_limits", 3));
                data.put("allowedFileUploadMethods", fileUpload.getOrDefault(
                        "allowed_file_upload_methods", Arrays.asList("local_file")));
            } else {
                data.put("allowedFileTypes", Collections.emptyList());
                data.put("allowedFileExtensions", Collections.emptyList());
                data.put("numberLimits", 0);
                data.put("allowedFileUploadMethods", Collections.emptyList());
            }
            jb.setCode(1);
            jb.setMsg(enabled ? "已开启" : "未开启");
            jb.setData(data);
        } catch (Exception e) {
            log.error("getFileUploadConfig 失败 appId={}", appId, e);
            jb.setCode(0);
            jb.setMsg("查询失败: " + e.getMessage());
        }
        return jb;
    }

    /**
     * 开启/关闭 WenxinAgent 应用的文件上传功能。
     *
     * <p>仅支持 chat / agent-chat / completion 三种模式。
     * workflow / advanced-chat 的文件上传通过开始节点变量定义实现，无需此开关。</p>
     *
     * <p>实现原理：</p>
     * <ol>
     *   <li>GET /apps/{appId} 取 app 详情，识别 mode + 当前 model_config</li>
     *   <li>在 model_config 上合并/清除 file_upload 配置</li>
     *   <li>POST /apps/{appId}/model-config 整体回写（WenxinAgent 端点覆盖式更新）</li>
     * </ol>
     *
     * @param appId  WenxinAgent 应用 ID
     * @param body   请求体，含 {@code enabled} (boolean)
     */
    @PostMapping("/agentPlatform/apps/{appId}/file-upload")
    @Operation(summary = "开启/关闭应用文件上传功能")
    public MyJsonBean<Map<String, Object>> toggleFileUpload(
            @PathVariable String appId,
            @RequestBody Map<String, Object> body,
            @RequestHeader(value = "X-WenxinAgent-Authorization", required = false) String wenxinAgentAuth,
            @RequestHeader(value = "Authorization", required = false) String fallbackAuth) {
        MyJsonBean<Map<String, Object>> jb = new MyJsonBean<>();
        try {
            String token = stripBearer(wenxinAgentAuth != null && !wenxinAgentAuth.isEmpty() ? wenxinAgentAuth : fallbackAuth);
            boolean enabled = Boolean.TRUE.equals(body.get("enabled"));

            // 1) 拿到应用详情，识别 mode
            Map<String, Object> app = wenxinAgentClient.getApp(token, appId);
            String mode = app == null ? "" : String.valueOf(app.getOrDefault("mode", ""));
            if (!"chat".equals(mode) && !"agent-chat".equals(mode) && !"completion".equals(mode)) {
                jb.setCode(0);
                jb.setMsg("该应用模式(" + mode
                        + ")文件上传通过开始节点变量配置，无需此开关");
                return jb;
            }

            // 2) 从 GET 返回中取 model_config（WenxinAgent 序列化为 "model_config"）
            Map<String, Object> modelConfig = new HashMap<>();
            Object mcObj = app.get("model_config");
            if (mcObj instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, Object> original = (Map<String, Object>) mcObj;
                modelConfig.putAll(original);
            }

            // 3) 合并/清除 file_upload
            if (enabled) {
                Map<String, Object> fileUpload = new HashMap<>();
                fileUpload.put("enabled", true);
                fileUpload.put("allowed_file_types", Arrays.asList("image", "document"));
                fileUpload.put("allowed_file_extensions",
                        Arrays.asList(".pdf", ".docx", ".jpg", ".jpeg", ".png", ".txt", ".csv", ".xlsx"));
                fileUpload.put("allowed_file_upload_methods", Arrays.asList("local_file"));
                fileUpload.put("number_limits", 3);
                modelConfig.put("file_upload", fileUpload);
            } else {
                modelConfig.put("file_upload", Collections.emptyMap());
            }

            // 4) 回写 WenxinAgent model-config 端点
            wenxinAgentClient.updateModelConfig(token, appId, modelConfig);

            Map<String, Object> data = new HashMap<>();
            data.put("enabled", enabled);
            data.put("mode", mode);
            jb.setCode(1);
            jb.setMsg(enabled ? "已开启文件上传" : "已关闭文件上传");
            jb.setData(data);
            log.info("toggleFileUpload appId={} mode={} enabled={}", appId, mode, enabled);
        } catch (Exception e) {
            log.error("toggleFileUpload 失败 appId={}", appId, e);
            jb.setCode(0);
            jb.setMsg("操作失败: " + e.getMessage());
        }
        return jb;
    }
}
