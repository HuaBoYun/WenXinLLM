package com.huabo.bigmodel.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huabo.bigmodel.config.Ipv4FirstDns;
import com.huabo.bigmodel.config.LlmProperties;
import com.huabo.bigmodel.dto.AiChatRequest;
import com.huabo.bigmodel.service.AiChatService;
import com.huabo.bigmodel.service.CombinationDocLinkService;
import com.huabo.bigmodel.tool.ToolDefinitions;
import com.huabo.bigmodel.tool.ToolExecutor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.sse.EventSource;
import okhttp3.sse.EventSourceListener;
import okhttp3.sse.EventSources;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * AI聊天服务实现 - 支持联网搜索、深度思考和工具调用(SQL执行)
 */
@Slf4j
@Service
public class AiChatServiceImpl implements AiChatService {

    private final LlmProperties llmProperties;
    private final ToolExecutor toolExecutor;
    private final CombinationDocLinkService combinationDocLinkService;
    private OkHttpClient httpClient;

    // 会话历史存储 (生产环境建议使用Redis)
    private final Map<String, List<JSONObject>> sessionHistory = new ConcurrentHashMap<>();
    // 会话累计 token 消耗（输入+输出，含思考；仅用于计量日志，不做任何限制）
    private final Map<String, Long> sessionTokenUsage = new ConcurrentHashMap<>();
    /** AI 咨询页的请求标识（前端 AIConsult.vue 请求体带 scene=consult 才应用该页 token 限制） */
    private static final String SCENE_CONSULT = "consult";
    /** AI 数据治理页的请求标识（前端 AIGovernance.vue 请求体带 scene=governance 才应用该页 token 限制） */
    private static final String SCENE_GOVERNANCE = "governance";

    public AiChatServiceImpl(LlmProperties llmProperties, ToolExecutor toolExecutor,
            CombinationDocLinkService combinationDocLinkService) {
        this.llmProperties = llmProperties;
        this.toolExecutor = toolExecutor;
        this.combinationDocLinkService = combinationDocLinkService;
    }

    @PostConstruct
    public void init() {
        int timeout = llmProperties.getCurrentTimeout();
        this.httpClient = new OkHttpClient.Builder()
                .dns(Ipv4FirstDns.INSTANCE)
                .connectTimeout(timeout, TimeUnit.SECONDS)
                .readTimeout(timeout, TimeUnit.SECONDS)
                .writeTimeout(timeout, TimeUnit.SECONDS)
                .build();
        log.info("AI聊天服务初始化完成，当前提供商: {}, 超时时间: {}秒", llmProperties.getProvider(), timeout);
    }

    @Override
    public Flux<String> chatStream(AiChatRequest request) {
        return Flux.create(sink -> {
            // 在独立线程中处理，确保SSE事件能实时发送
            new Thread(() -> {
                try {
                    processChat(request, sink, true); // 流式场景，返回值由 sink 推送，忽略返回值
                } catch (Exception e) {
                    log.error("流式聊天失败", e);
                    sink.next("data: {\"error\": \"" + e.getMessage() + "\"}\n\n");
                    sink.complete();
                }
            }, "SSE-Chat-" + System.currentTimeMillis()).start();
        }, FluxSink.OverflowStrategy.BUFFER);
    }

    @Override
    public String chat(AiChatRequest request) {
        try {
            return processChat(request, null, false);
        } catch (Exception e) {
            log.error("聊天失败", e);
            return "请求失败: " + e.getMessage();
        }
    }

    @Override
    public void clearSession(String sessionId) {
        if (sessionId != null) {
            sessionHistory.remove(sessionId);
            sessionTokenUsage.remove(sessionId);
            log.info("已清除会话: {}", sessionId);
        }
    }

    /**
     * 处理聊天请求，返回最终文本内容（非流式时使用）
     */
    private String processChat(AiChatRequest request, FluxSink<String> sink, boolean isStream) throws Exception {
        String sessionId = request.getSessionId();
        if (sessionId == null || sessionId.isEmpty()) {
            sessionId = UUID.randomUUID().toString();
        }

        // 获取或创建会话历史
        List<JSONObject> history = sessionHistory.computeIfAbsent(sessionId, k -> new ArrayList<>());

        // 添加传入的历史记录
        if (request.getHistory() != null && !request.getHistory().isEmpty()) {
            for (AiChatRequest.Message msg : request.getHistory()) {
                JSONObject historyMsg = new JSONObject();
                historyMsg.put("role", msg.getRole());
                historyMsg.put("content", msg.getContent());
                history.add(historyMsg);
            }
        }

        // 添加当前用户消息
        JSONObject userMsg = new JSONObject();
        userMsg.put("role", "user");
        userMsg.put("content", request.getMessage());

        List<JSONObject> currentMessages = new ArrayList<>(history);
        currentMessages.add(userMsg);

        boolean enableWebSearch = request.getEnableWebSearch() != null ?
                request.getEnableWebSearch() : llmProperties.getSearch().getEnabled();
        boolean enableThinking = request.getEnableThinking() != null ?
                request.getEnableThinking() : false;

        // 循环处理工具调用
        int maxToolCalls = 50; // 增加到10次，支持更多工具调用
        int toolCallCount = 0;
        String finalResponse = "";

        // 页面级 token 限制（scene=consult 咨询页 / scene=governance 数据治理页）：仅带标识的请求生效，其他调用方不受影响。
        // 单轮输出上限传给 sendRequest；会话累计上限在工具循环顶部校验（首轮即入口检查，
        // 后续轮防止联网搜索/SQL 工具循环在单轮内把消耗刷过上限）
        boolean consultLimited = SCENE_CONSULT.equals(request.getScene());
        boolean governanceLimited = SCENE_GOVERNANCE.equals(request.getScene());
        Integer turnOutputLimit = consultLimited ? llmProperties.getConsultMaxTokens()
                : (governanceLimited ? llmProperties.getGovernanceMaxTokens() : null);

        while (toolCallCount < maxToolCalls) {
            String limitMsg = sessionLimitMessage(consultLimited, governanceLimited, sessionId);
            if (limitMsg != null) {
                log.warn("[token] 会话{}达到累计上限，拒绝继续生成", sessionId);
                if (sink != null) {
                    JSONObject err = new JSONObject();
                    err.put("error", limitMsg);
                    sink.next("data: " + err.toJSONString() + "\n\n");
                    sink.complete();
                }
                return limitMsg;
            }
            ChatResult result = sendRequest(currentMessages, enableWebSearch, enableThinking,
                    request.getSystemPrompt(), sink, isStream, sessionId, turnOutputLimit,
                    request.getEnableTools() == null || request.getEnableTools());

            // token 计量：按模型调用累计（工具循环每轮各是一次完整请求），
            // 只打日志观测消耗，不做任何限制
            logTokenUsage(sessionId, result, currentMessages);

            if (result.error != null) {
                if (sink != null) {
                    sink.next("data: {\"error\": \"" + result.error + "\"}\n\n");
                    sink.complete();
                }
                return finalResponse;
            }

            // 如果有工具调用
            if (result.toolUse != null) {
                toolCallCount++;
                String toolName = result.toolUse.getString("name");
                String toolId = result.toolUse.getString("id");
                JSONObject toolInput = result.toolUse.getJSONObject("input");

                log.info("调用工具: {}, 参数: {}", toolName, toolInput);

                // 记录组合配置INSERT，供对话历史保存时自动关联模型文档
                combinationDocLinkService.recordFromToolInput(sessionId, toolInput);

                // 发送详细的工具调用消息给前端
                if (sink != null) {
                    JSONObject toolCallEvent = new JSONObject();
                    toolCallEvent.put("type", "tool_call");
                    toolCallEvent.put("tool", toolName);

                    // 根据工具类型生成友好的提示消息
                    String toolMessage = generateToolCallMessage(toolName, toolInput);
                    toolCallEvent.put("message", toolMessage);
                    toolCallEvent.put("params", toolInput);

                    sink.next("data: " + toolCallEvent.toJSONString() + "\n\n");
                }

                // 执行工具
                String toolResult;
                if ("web_search".equals(toolName)) {
                    toolResult = executeWebSearch(toolInput.getString("query"));
                } else if ("execute_batch_sql".equals(toolName)) {
                    // 批量SQL执行,支持进度回调
                    toolResult = toolExecutor.executeTool(toolName, toolInput, (progressMsg) -> {
                        if (sink != null) {
                            // 发送进度到前端
                            JSONObject progressData = new JSONObject();
                            progressData.put("type", "progress");
                            progressData.put("message", progressMsg);
                            sink.next("data: " + progressData.toJSONString() + "\n\n");
                        }
                    });
                } else {
                    // 使用ToolExecutor执行SQL工具
                    toolResult = toolExecutor.executeTool(toolName, toolInput);
                }

                // 添加assistant的工具调用消息
                JSONObject assistantMsg = new JSONObject();
                assistantMsg.put("role", "assistant");
                JSONArray contentArray = new JSONArray();
                contentArray.add(result.toolUse);
                assistantMsg.put("content", contentArray);
                currentMessages.add(assistantMsg);

                // 添加工具结果消息
                JSONObject toolResultMsg = new JSONObject();
                toolResultMsg.put("role", "user");
                JSONArray toolResultContent = new JSONArray();
                JSONObject toolResultBlock = new JSONObject();
                toolResultBlock.put("type", "tool_result");
                toolResultBlock.put("tool_use_id", toolId);
                toolResultBlock.put("content", toolResult);
                toolResultContent.add(toolResultBlock);
                toolResultMsg.put("content", toolResultContent);
                currentMessages.add(toolResultMsg);

            } else {
                // 没有工具调用，返回最终结果
                finalResponse = result.textContent;

                // 保存到会话历史
                history.add(userMsg);
                JSONObject assistantMsg = new JSONObject();
                assistantMsg.put("role", "assistant");
                // 长产出（办公模式的整份 PPT JSON/文档可达数万字）截断入史：
                // 否则同会话下一轮 prompt 携带全部历史，输入 token 成倍膨胀，
                // 首 token 延迟急剧变长（表现为长时间无任何输出）
                String historyContent = finalResponse;
                if (historyContent != null && historyContent.length() > 2000) {
                    historyContent = historyContent.substring(0, 2000) + "\n…（内容过长，历史仅保留前2000字）";
                }
                assistantMsg.put("content", historyContent);
                history.add(assistantMsg);

                // 限制历史长度
                while (history.size() > 20) {
                    history.remove(0);
                }

                if (sink != null) {
                    sink.next("data: {\"type\": \"done\", \"session_id\": \"" + sessionId + "\"}\n\n");
                    sink.complete();
                }
                break;
            }
        }

        // 如果达到最大工具调用次数，仍然需要发送done消息
        if (toolCallCount >= maxToolCalls) {
            log.warn("达到最大工具调用次数: {}", maxToolCalls);
            if (sink != null) {
                sink.next("data: {\"type\": \"text\", \"content\": \"\\n\\n已达到最大工具调用次数，请继续对话。\"}\n\n");
                sink.next("data: {\"type\": \"done\", \"session_id\": \"" + sessionId + "\"}\n\n");
                sink.complete();
            }
        }
        return finalResponse;
    }

    /**
     * 发送请求到LLM提供商（根据配置动态选择）
     * enableTools：是否附加SQL执行工具；纯文本分析类调用方（如蓝图需求拆分）传false
     */
    private ChatResult sendRequest(List<JSONObject> messages, boolean enableWebSearch,
            boolean enableThinking, String customSystemPrompt, FluxSink<String> sink,
            boolean isStream, String sessionId, Integer maxTokensOverride,
            boolean enableTools) throws Exception {

        // 构建请求体
        JSONObject requestBody = new JSONObject();
        requestBody.put("model", llmProperties.getCurrentModel());
        requestBody.put("stream", true);

        // 设置系统提示词
        String systemPrompt = customSystemPrompt != null && !customSystemPrompt.isEmpty()
                ? customSystemPrompt : llmProperties.getSystemPrompt();
        if (systemPrompt != null && !systemPrompt.isEmpty()) {
            requestBody.put("system", systemPrompt);
        }

        // 单轮输出上限:页面级覆盖（AI 咨询页 scene=consult / 数据治理页 scene=governance），null=使用全局配置
        int maxTokens = maxTokensOverride != null ? maxTokensOverride
                : (llmProperties.getCurrentMaxTokens() != null ? llmProperties.getCurrentMaxTokens() : 131072);

        // 深度思考配置
        // thinking token 计入 max_tokens：budget 必须小于 max_tokens，且要为正文/工具调用预留空间，
        // 否则思考会在 max_tokens 处被截断，表现为"还没思考完对话就结束"
        if (enableThinking) {
            int budgetTokens = llmProperties.getThinking().getBudgetTokens() != null
                    ? llmProperties.getThinking().getBudgetTokens() : 10000;
            if (budgetTokens > maxTokens / 2) {
                budgetTokens = Math.max(1024, maxTokens / 2);
            }
            requestBody.put("max_tokens", maxTokens);
            JSONObject thinking = new JSONObject();
            thinking.put("type", "enabled");
            thinking.put("budget_tokens", budgetTokens);
            requestBody.put("thinking", thinking);
        } else {
            requestBody.put("max_tokens", maxTokens);
        }

        // 添加工具(web_search和SQL工具)
        JSONArray tools = new JSONArray();

        // 添加web_search工具
        if (enableWebSearch) {
            JSONObject webSearchTool = new JSONObject();
            webSearchTool.put("name", "web_search");
            webSearchTool.put("description", "搜索互联网获取实时信息，如新闻、天气、股票、最新事件等。");

            JSONObject inputSchema = new JSONObject();
            inputSchema.put("type", "object");
            JSONObject properties = new JSONObject();
            JSONObject queryProp = new JSONObject();
            queryProp.put("type", "string");
            queryProp.put("description", "搜索关键词");
            properties.put("query", queryProp);
            inputSchema.put("properties", properties);
            JSONArray required = new JSONArray();
            required.add("query");
            inputSchema.put("required", required);

            webSearchTool.put("input_schema", inputSchema);
            tools.add(webSearchTool);
        }

        // 添加SQL执行工具（execute_sql 已在 ToolExecutor 中禁用，注册批量执行工具替代）
        // enableTools=false 时不附加：纯文本分析场景（如蓝图需求拆分）用不到 SQL，
        // 附带反而诱导模型调用工具，拖慢响应并污染结构化输出
        if (enableTools) {
            tools.add(ToolDefinitions.getSqlBatchTool());
            tools.add(getSqlQueryTool());
            tools.add(getSqlDdlTool());
            tools.add(getSqlDmlTool());
        }

        if (!tools.isEmpty()) {
            requestBody.put("tools", tools);
        }

        // 构建消息列表
        JSONArray messagesArray = new JSONArray();
        for (JSONObject msg : messages) {
            messagesArray.add(msg);
        }
        requestBody.put("messages", messagesArray);

        String url = llmProperties.getCurrentBaseUrl() + "/v1/messages";

        Request request = new Request.Builder()
                .url(url)
                .addHeader("x-api-key", llmProperties.getCurrentApiKey())
                .addHeader("anthropic-version", "2023-06-01")
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "text/event-stream")
                .post(RequestBody.create(requestBody.toJSONString(), MediaType.parse("application/json")))
                .build();

        CountDownLatch latch = new CountDownLatch(1);
        ChatResult result = new ChatResult();
        StringBuilder fullResponse = new StringBuilder();
        StringBuilder thinkingContent = new StringBuilder();
        final boolean[] inThinking = {false};
        final JSONObject[] currentToolUse = {null};

        EventSource.Factory factory = EventSources.createFactory(httpClient);
        factory.newEventSource(request, new EventSourceListener() {
            @Override
            public void onEvent(EventSource eventSource, String id, String type, String data) {
                try {
                    // 跳过空行或无效数据
                    if (data == null || data.trim().isEmpty()) {
                        log.debug("跳过空事件数据");
                        return;
                    }

                    // 跳过非 JSON 格式的数据（如 [DONE] 标记）
                    if (data.trim().startsWith("[")) {
                        log.debug("跳过非JSON事件: {}", data);
                        return;
                    }

                    log.debug("收到SSE事件: type={}, data={}", type, data);
                    JSONObject event = JSON.parseObject(data);
                    String eventType = event.getString("type");

                    if ("message_start".equals(eventType)) {
                        // 计量：message_start 的 usage 携带本轮输入 token 数
                        JSONObject message = event.getJSONObject("message");
                        if (message != null) {
                            JSONObject usage = message.getJSONObject("usage");
                            if (usage != null && usage.getInteger("input_tokens") != null) {
                                result.inputTokens += usage.getInteger("input_tokens");
                            }
                        }
                    } else if ("content_block_start".equals(eventType)) {
                        JSONObject contentBlock = event.getJSONObject("content_block");
                        if (contentBlock != null) {
                            String blockType = contentBlock.getString("type");
                            if ("thinking".equals(blockType)) {
                                inThinking[0] = true;
                                if (sink != null) {
                                    sink.next("data: {\"type\": \"thinking_start\"}\n\n");
                                }
                            } else if ("text".equals(blockType)) {
                                if (inThinking[0]) {
                                    inThinking[0] = false;
                                    if (sink != null) {
                                        sink.next("data: {\"type\": \"thinking_end\"}\n\n");
                                    }
                                }
                            } else if ("tool_use".equals(blockType)) {
                                currentToolUse[0] = new JSONObject();
                                currentToolUse[0].put("type", "tool_use");
                                currentToolUse[0].put("id", contentBlock.getString("id"));
                                currentToolUse[0].put("name", contentBlock.getString("name"));
                                currentToolUse[0].put("input", new JSONObject());
                            }
                        }
                    } else if ("content_block_delta".equals(eventType)) {
                        JSONObject delta = event.getJSONObject("delta");
                        String deltaType = delta.getString("type");

                        if ("thinking_delta".equals(deltaType)) {
                            log.debug("进入thinking_delta处理");
                            String thinking = delta.getString("thinking");
                            if (thinking != null) {
                                thinkingContent.append(thinking);
                                if (sink != null) {
                                    sink.next("data: {\"type\": \"thinking\", \"content\": " +
                                            JSON.toJSONString(thinking) + "}\n\n");
                                }
                            }
                        } else if ("text_delta".equals(deltaType)) {
                            // 处理文本内容
                            String text = delta.getString("text");
                            log.debug("收到text_delta: {}", text);
                            if (text != null) {
                                fullResponse.append(text);
                                if (sink != null) {
                                    String textEvent = "data: {\"type\": \"text\", \"content\": " +
                                            JSON.toJSONString(text) + "}\n\n";
                                    log.debug("发送text事件到前端: {}", textEvent);
                                    sink.next(textEvent);
                                }
                            }
                        } else if ("input_json_delta".equals(deltaType)) {
                            String partialJson = delta.getString("partial_json");
                            if (partialJson != null && currentToolUse[0] != null) {
                                String existingInput = currentToolUse[0].getString("_partial_input");
                                if (existingInput == null) existingInput = "";
                                currentToolUse[0].put("_partial_input", existingInput + partialJson);
                            }
                        }
                    } else if ("content_block_stop".equals(eventType)) {
                        if (currentToolUse[0] != null) {
                            String partialInput = currentToolUse[0].getString("_partial_input");
                            if (partialInput != null) {
                                try {
                                    JSONObject input = JSON.parseObject(partialInput);
                                    currentToolUse[0].put("input", input);
                                } catch (Exception e) {
                                    log.warn("解析工具输入失败: {}", partialInput);
                                }
                                currentToolUse[0].remove("_partial_input");
                            }
                            result.toolUse = currentToolUse[0];
                        }
                    } else if ("message_stop".equals(eventType)) {
                        // 消息流结束，但不一定是整个会话结束（可能还有工具调用）
                        // 只释放 latch，让 processChat 决定是否发送 done 事件
                        log.debug("收到message_stop，释放latch等待");
                        latch.countDown();
                        // 注意：不在这里发送 done 事件和调用 sink.complete()
                        // 由 processChat 方法根据是否有工具调用来决定
                    } else if ("message_delta".equals(eventType)) {
                        // 计量：message_delta 的 usage 携带累计输出 token 数
                        JSONObject usage = event.getJSONObject("usage");
                        if (usage != null && usage.getInteger("output_tokens") != null) {
                            result.outputTokens = usage.getInteger("output_tokens");
                        }
                        // 处理 message_delta 事件（智谱AI的普通文本内容）
                        String text = event.getString("text");
                        log.debug("收到message_delta: {}", text);
                        if (text != null) {
                            fullResponse.append(text);
                            if (sink != null) {
                                String textEvent = "data: {\"type\": \"text\", \"content\": " +
                                            JSON.toJSONString(text) + "}\n\n";
                                log.debug("发送message_delta转text事件: {}", textEvent);
                                sink.next(textEvent);
                            } else {
                                log.warn("sink为null，无法发送message_delta事件");
                            }
                        }
                    }
                } catch (Exception e) {
                    log.warn("解析事件失败: {}", data, e);
                }
            }

            @Override
            public void onFailure(EventSource eventSource, Throwable t, Response response) {
                String errorMsg = "";
                if (response != null) {
                    try {
                        errorMsg = response.body() != null ? response.body().string() : "";
                    } catch (IOException ignored) {}
                }
                result.error = t != null ? t.getMessage() : errorMsg;
                log.error("请求失败: {}", result.error);
                latch.countDown();
            }

            @Override
            public void onClosed(EventSource eventSource) {
                latch.countDown();
            }
        });

        // 整轮耗时（深度思考+长脚本输出）远超单次网络读超时，按读超时的6倍作为整轮上限；
        // 超时必须显式报错——静默返回会被当作"无工具调用的最终回复"，前端表现为没干完活就结束
        boolean finished = latch.await(llmProperties.getCurrentTimeout() * 6L, TimeUnit.SECONDS);
        if (!finished) {
            result.error = "模型响应超时（" + (llmProperties.getCurrentTimeout() * 6L)
                    + "秒），本轮已终止，请重试或简化任务";
        }

        result.textContent = fullResponse.toString();
        result.thinkingContent = thinkingContent.toString();

        return result;
    }

    /**
     * 页面级会话累计上限检查（咨询页/数据治理页）：达到上限时返回拒绝提示文案（null=未触发/不限制）。
     * 超限后由前端提示开启新对话，新建会话（新 sessionId）即恢复
     */
    private String sessionLimitMessage(boolean consultLimited, boolean governanceLimited, String sessionId) {
        long cap = 0;
        if (consultLimited) {
            cap = llmProperties.getConsultSessionMaxTokens() != null
                    ? llmProperties.getConsultSessionMaxTokens() : 0;
        } else if (governanceLimited) {
            cap = llmProperties.getGovernanceSessionMaxTokens() != null
                    ? llmProperties.getGovernanceSessionMaxTokens() : 0;
        }
        if (cap <= 0) {
            return null;
        }
        long used = sessionTokenUsage.getOrDefault(sessionId, 0L);
        if (used < cap) {
            return null;
        }
        return "本会话累计消耗 token 已达上限（已用 " + used + "，上限 " + cap
                + "），请开启新对话继续使用";
    }

    /**
     * token 消耗计量日志：优先用接口返回的 usage，缺失时按字符数粗估
     * （中文约 1.5 字/token，日志标注"估算"）。仅观测，不做任何限制
     */
    private void logTokenUsage(String sessionId, ChatResult result, List<JSONObject> requestMessages) {
        long turnTokens = result.inputTokens + result.outputTokens;
        String source = "usage";
        if (turnTokens == 0 && result.error == null) {
            source = "估算";
            long chars = result.textContent.length() + result.thinkingContent.length();
            for (JSONObject msg : requestMessages) {
                Object content = msg.get("content");
                if (content != null) {
                    chars += content.toString().length();
                }
            }
            turnTokens = (long) (chars / 1.5);
        }
        if (turnTokens <= 0) {
            return;
        }
        long total = sessionTokenUsage.merge(sessionId, turnTokens, Long::sum);
        log.info("[token] session={} 本次模型调用({}) input={} output={} 合计={} 会话累计={}",
                sessionId, source, result.inputTokens, result.outputTokens, turnTokens, total);
    }

    /**
     * 生成工具调用的友好提示消息
     */
    private String generateToolCallMessage(String toolName, JSONObject toolInput) {
        switch (toolName) {
            case "web_search":
                String query = toolInput.getString("query");
                return "🔍 正在联网搜索：" + (query != null ? query : "");
            case "execute_sql":
                String sql = toolInput.getString("sql");
                return "📊 正在执行SQL查询...";
            case "execute_batch_sql":
                return "📊 正在批量执行SQL语句...";
            case "sql_query":
                return "📊 正在执行数据查询...";
            case "sql_ddl":
                return "🔧 正在执行DDL操作（创建/修改表结构）...";
            case "sql_dml":
                return "📝 正在执行DML操作（数据增删改）...";
            default:
                return "⚙️ 正在调用工具：" + toolName;
        }
    }

    /**
     * 执行网络搜索
     */
    private String executeWebSearch(String query) {
        log.info("执行网络搜索: {}", query);

        try {
            // 优先使用Tavily
            String tavilyKey = llmProperties.getSearch().getTavilyApiKey();
            if (tavilyKey != null && !tavilyKey.isEmpty()) {
                String searchResult = searchWithTavily(query, tavilyKey);
                if (searchResult != null && !searchResult.isEmpty()) {
                    return searchResult;
                }
            }

            // 备用: Serper
            String serperKey = llmProperties.getSearch().getSerperApiKey();
            if (serperKey != null && !serperKey.isEmpty()) {
                String searchResult = searchWithSerper(query, serperKey);
                if (searchResult != null && !searchResult.isEmpty()) {
                    return searchResult;
                }
            }

            return "搜索完成，但未找到直接相关的结果。";

        } catch (Exception e) {
            log.error("搜索失败", e);
            return "搜索出错: " + e.getMessage();
        }
    }

    /**
     * 使用Tavily搜索
     */
    private String searchWithTavily(String query, String apiKey) {
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("api_key", apiKey);
            requestBody.put("query", query);
            requestBody.put("search_depth", "basic");
            requestBody.put("include_answer", true);
            requestBody.put("include_raw_content", false);
            requestBody.put("max_results", 5);

            Request request = new Request.Builder()
                    .url("https://api.tavily.com/search")
                    .addHeader("Content-Type", "application/json")
                    .post(RequestBody.create(requestBody.toJSONString(), MediaType.parse("application/json")))
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String body = response.body().string();
                    JSONObject jsonResult = JSON.parseObject(body);

                    StringBuilder searchResult = new StringBuilder();
                    searchResult.append("【搜索结果】\n\n");

                    String answer = jsonResult.getString("answer");
                    if (answer != null && !answer.isEmpty()) {
                        searchResult.append("AI摘要: ").append(answer).append("\n\n");
                    }

                    JSONArray results = jsonResult.getJSONArray("results");
                    if (results != null && results.size() > 0) {
                        searchResult.append("相关来源:\n");
                        for (int i = 0; i < results.size(); i++) {
                            JSONObject item = results.getJSONObject(i);
                            String title = item.getString("title");
                            String content = item.getString("content");
                            String url = item.getString("url");

                            searchResult.append(i + 1).append(". ").append(title).append("\n");
                            if (content != null) {
                                String snippet = content.length() > 200 ? content.substring(0, 200) + "..." : content;
                                searchResult.append("   ").append(snippet).append("\n");
                            }
                            searchResult.append("   来源: ").append(url).append("\n\n");
                        }
                    }

                    if (searchResult.length() > 20) {
                        return searchResult.toString();
                    }
                }
            }
        } catch (Exception e) {
            log.warn("Tavily搜索失败: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 使用Serper搜索
     */
    private String searchWithSerper(String query, String apiKey) {
        try {
            JSONObject requestBody = new JSONObject();
            requestBody.put("q", query);
            requestBody.put("gl", "cn");
            requestBody.put("hl", "zh-cn");

            Request request = new Request.Builder()
                    .url("https://google.serper.dev/search")
                    .addHeader("X-API-KEY", apiKey)
                    .addHeader("Content-Type", "application/json")
                    .post(RequestBody.create(requestBody.toJSONString(), MediaType.parse("application/json")))
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (response.isSuccessful() && response.body() != null) {
                    String body = response.body().string();
                    JSONObject jsonResult = JSON.parseObject(body);

                    StringBuilder searchResult = new StringBuilder();
                    searchResult.append("【搜索结果】\n\n");

                    JSONObject answerBox = jsonResult.getJSONObject("answerBox");
                    if (answerBox != null) {
                        String answer = answerBox.getString("answer");
                        String snippet = answerBox.getString("snippet");
                        if (answer != null) {
                            searchResult.append("答案: ").append(answer).append("\n\n");
                        } else if (snippet != null) {
                            searchResult.append("摘要: ").append(snippet).append("\n\n");
                        }
                    }

                    JSONArray organic = jsonResult.getJSONArray("organic");
                    if (organic != null && organic.size() > 0) {
                        searchResult.append("相关结果:\n");
                        int count = Math.min(5, organic.size());
                        for (int i = 0; i < count; i++) {
                            JSONObject item = organic.getJSONObject(i);
                            String title = item.getString("title");
                            String snippet = item.getString("snippet");
                            String link = item.getString("link");

                            searchResult.append(i + 1).append(". ").append(title).append("\n");
                            if (snippet != null) {
                                searchResult.append("   ").append(snippet).append("\n");
                            }
                            searchResult.append("   来源: ").append(link).append("\n\n");
                        }
                    }

                    if (searchResult.length() > 20) {
                        return searchResult.toString();
                    }
                }
            }
        } catch (Exception e) {
            log.warn("Serper搜索失败: {}", e.getMessage());
        }
        return null;
    }

    /**
     * 聊天结果内部类
     */
    private static class ChatResult {
        String textContent = "";
        String thinkingContent = "";
        JSONObject toolUse = null;
        String error = null;
        // 本轮模型调用实际消耗（从 SSE usage 解析；接口未返回时为 0）
        long inputTokens = 0;
        long outputTokens = 0;
    }

    /**
     * SQL执行工具定义
     */
    private JSONObject getSqlExecuteTool() {
        JSONObject tool = new JSONObject();
        tool.put("name", "execute_sql");
        tool.put("description", "【仅在用户明确要求执行SQL时使用】执行SQL语句,自动识别SQL类型。重要提示:只有当用户明确说\"执行SQL\"、\"运行SQL\"、\"帮我执行\"等明确指令时才能调用此工具。不要主动建议或执行SQL操作。");

        JSONObject inputSchema = new JSONObject();
        inputSchema.put("type", "object");

        JSONObject properties = new JSONObject();

        JSONObject sqlParam = new JSONObject();
        sqlParam.put("type", "string");
        sqlParam.put("description", "要执行的SQL语句");
        properties.put("sql", sqlParam);

        JSONObject dbTypeParam = new JSONObject();
        dbTypeParam.put("type", "string");
        dbTypeParam.put("description", "数据库类型: dm或mysql,默认为dm");
        properties.put("dbType", dbTypeParam);

        inputSchema.put("properties", properties);
        JSONArray required = new JSONArray();
        required.add("sql");
        inputSchema.put("required", required);

        tool.put("input_schema", inputSchema);
        return tool;
    }

    /**
     * SQL查询工具定义
     */
    private JSONObject getSqlQueryTool() {
        JSONObject tool = new JSONObject();
        tool.put("name", "query_sql");
        tool.put("description", "【仅在用户明确要求查询数据库时使用】执行SQL查询语句,返回查询结果。重要提示:只有当用户明确说\"查询数据库\"、\"执行查询\"、\"帮我查一下数据库\"等明确指令时才能调用。不要主动查询数据库。");

        JSONObject inputSchema = new JSONObject();
        inputSchema.put("type", "object");

        JSONObject properties = new JSONObject();

        JSONObject sqlParam = new JSONObject();
        sqlParam.put("type", "string");
        sqlParam.put("description", "SELECT查询语句");
        properties.put("sql", sqlParam);

        JSONObject dbTypeParam = new JSONObject();
        dbTypeParam.put("type", "string");
        dbTypeParam.put("description", "数据库类型,默认为dm");
        properties.put("dbType", dbTypeParam);

        inputSchema.put("properties", properties);
        JSONArray required = new JSONArray();
        required.add("sql");
        inputSchema.put("required", required);

        tool.put("input_schema", inputSchema);
        return tool;
    }

    /**
     * SQL DDL工具定义
     */
    private JSONObject getSqlDdlTool() {
        JSONObject tool = new JSONObject();
        tool.put("name", "execute_ddl");
        tool.put("description", "【仅在用户明确要求建表或修改表结构时使用】执行DDL语句,用于创建表、修改表结构等操作。重要提示:只有当用户明确说\"帮我建表\"、\"创建表\"、\"执行建表语句\"、\"修改表结构\"等明确指令时才能调用。不要主动建议或执行建表操作。");

        JSONObject inputSchema = new JSONObject();
        inputSchema.put("type", "object");

        JSONObject properties = new JSONObject();

        JSONObject sqlParam = new JSONObject();
        sqlParam.put("type", "string");
        sqlParam.put("description", "DDL语句,例如CREATE TABLE");
        properties.put("sql", sqlParam);

        JSONObject dbTypeParam = new JSONObject();
        dbTypeParam.put("type", "string");
        dbTypeParam.put("description", "数据库类型,默认为dm");
        properties.put("dbType", dbTypeParam);

        inputSchema.put("properties", properties);
        JSONArray required = new JSONArray();
        required.add("sql");
        inputSchema.put("required", required);

        tool.put("input_schema", inputSchema);
        return tool;
    }

    /**
     * SQL DML工具定义
     */
    private JSONObject getSqlDmlTool() {
        JSONObject tool = new JSONObject();
        tool.put("name", "execute_dml");
        tool.put("description", "【仅在用户明确要求插入或更新数据时使用】执行DML语句,用于插入、更新数据。重要提示:只有当用户明确说\"插入数据\"、\"添加数据\"、\"执行插入\"、\"更新数据\"等明确指令时才能调用。不要主动插入或修改数据。注意:DELETE操作已被禁止。");

        JSONObject inputSchema = new JSONObject();
        inputSchema.put("type", "object");

        JSONObject properties = new JSONObject();

        JSONObject sqlParam = new JSONObject();
        sqlParam.put("type", "string");
        sqlParam.put("description", "DML语句,例如INSERT、UPDATE");
        properties.put("sql", sqlParam);

        JSONObject dbTypeParam = new JSONObject();
        dbTypeParam.put("type", "string");
        dbTypeParam.put("description", "数据库类型,默认为dm");
        properties.put("dbType", dbTypeParam);

        inputSchema.put("properties", properties);
        JSONArray required = new JSONArray();
        required.add("sql");
        inputSchema.put("required", required);

        tool.put("input_schema", inputSchema);
        return tool;
    }
}