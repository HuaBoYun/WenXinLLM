package com.huabo.bigmodel.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huabo.bigmodel.config.Ipv4FirstDns;
import com.huabo.bigmodel.config.LlmProperties;
import com.huabo.bigmodel.dto.ChatCompletionChunk;
import com.huabo.bigmodel.dto.ChatCompletionRequest;
import com.huabo.bigmodel.dto.ChatCompletionResponse;
import com.huabo.bigmodel.service.LlmService;
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
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 智谱AI服务实现 (使用Anthropic兼容接口)
 * 文档: https://docs.bigmodel.cn/cn/coding-plan/tool/claude
 */
@Slf4j
@Service("zhipuService")
public class ZhipuServiceImpl implements LlmService {

    private final LlmProperties llmProperties;
    private OkHttpClient httpClient;

    public ZhipuServiceImpl(LlmProperties llmProperties) {
        this.llmProperties = llmProperties;
    }

    @PostConstruct
    public void init() {
        LlmProperties.ZhipuConfig config = llmProperties.getZhipu();
        this.httpClient = new OkHttpClient.Builder()
                .dns(Ipv4FirstDns.INSTANCE)
                .connectTimeout(config.getTimeout(), TimeUnit.SECONDS)
                .readTimeout(config.getTimeout(), TimeUnit.SECONDS)
                .writeTimeout(config.getTimeout(), TimeUnit.SECONDS)
                .build();
    }

    @Override
    public String getProviderName() {
        return "zhipu";
    }

    @Override
    public ChatCompletionResponse chat(ChatCompletionRequest request) {
        LlmProperties.ZhipuConfig config = llmProperties.getZhipu();
        
        // 转换为Anthropic格式
        JSONObject anthropicRequest = convertToAnthropicFormat(request, config);
        
        String url = config.getBaseUrl() + "/v1/messages";
        String requestBody = anthropicRequest.toJSONString();
        
        log.debug("智谱AI请求URL: {}", url);
        log.debug("智谱AI请求体: {}", requestBody);

        Request httpRequest = new Request.Builder()
                .url(url)
                .addHeader("x-api-key", config.getApiKey())
                .addHeader("anthropic-version", "2023-06-01")
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(requestBody, MediaType.parse("application/json")))
                .build();

        try (Response response = httpClient.newCall(httpRequest).execute()) {
            String responseBody = response.body() != null ? response.body().string() : "";
            log.debug("智谱AI响应: {}", responseBody);
            
            if (!response.isSuccessful()) {
                log.error("智谱AI调用失败: {} - {}", response.code(), responseBody);
                throw new RuntimeException("智谱AI调用失败: " + responseBody);
            }
            
            // 转换为OpenAI格式
            return convertToOpenAIFormat(responseBody, request.getModel());
        } catch (IOException e) {
            log.error("智谱AI调用异常", e);
            throw new RuntimeException("智谱AI调用异常: " + e.getMessage(), e);
        }
    }

    @Override
    public Flux<String> chatStream(ChatCompletionRequest request) {
        LlmProperties.ZhipuConfig config = llmProperties.getZhipu();
        
        JSONObject anthropicRequest = convertToAnthropicFormat(request, config);
        anthropicRequest.put("stream", true);
        
        String url = config.getBaseUrl() + "/v1/messages";
        String requestBody = anthropicRequest.toJSONString();

        log.debug("智谱AI流式请求URL: {}", url);
        log.debug("智谱AI流式请求体: {}", requestBody);

        return Flux.create(emitter -> {
            Request httpRequest = new Request.Builder()
                    .url(url)
                    .addHeader("x-api-key", config.getApiKey())
                    .addHeader("anthropic-version", "2023-06-01")
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "text/event-stream")
                    .post(RequestBody.create(requestBody, MediaType.parse("application/json")))
                    .build();

            String responseId = "chatcmpl-" + IdUtil.fastSimpleUUID();
            long created = System.currentTimeMillis() / 1000;

            EventSource.Factory factory = EventSources.createFactory(httpClient);
            factory.newEventSource(httpRequest, new EventSourceListener() {
                @Override
                public void onEvent(EventSource eventSource, String id, String type, String data) {
                    try {
                        log.trace("智谱AI SSE事件: type={}, data={}", type, data);
                        JSONObject event = JSON.parseObject(data);
                        String eventType = event.getString("type");
                        
                        if ("content_block_delta".equals(eventType)) {
                            JSONObject delta = event.getJSONObject("delta");
                            String text = delta.getString("text");
                            if (text != null) {
                                ChatCompletionChunk chunk = ChatCompletionChunk.builder()
                                        .id(responseId)
                                        .object("chat.completion.chunk")
                                        .created(created)
                                        .model(request.getModel())
                                        .choices(Collections.singletonList(
                                                ChatCompletionChunk.Choice.builder()
                                                        .index(0)
                                                        .delta(ChatCompletionChunk.Delta.builder()
                                                                .content(text)
                                                                .build())
                                                        .build()
                                        ))
                                        .build();
                                emitter.next("data: " + JSON.toJSONString(chunk) + "\n\n");
                            }
                        } else if ("message_stop".equals(eventType)) {
                            ChatCompletionChunk endChunk = ChatCompletionChunk.builder()
                                    .id(responseId)
                                    .object("chat.completion.chunk")
                                    .created(created)
                                    .model(request.getModel())
                                    .choices(Collections.singletonList(
                                            ChatCompletionChunk.Choice.builder()
                                                    .index(0)
                                                    .delta(ChatCompletionChunk.Delta.builder().build())
                                                    .finishReason("stop")
                                                    .build()
                                    ))
                                    .build();
                            emitter.next("data: " + JSON.toJSONString(endChunk) + "\n\n");
                            emitter.next("data: [DONE]\n\n");
                            emitter.complete();
                        }
                    } catch (Exception e) {
                        log.error("解析智谱AI流式响应失败", e);
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
                    log.error("智谱AI流式调用失败: {}", errorMsg, t);
                    emitter.error(t != null ? t : new RuntimeException("Stream failed: " + errorMsg));
                }

                @Override
                public void onClosed(EventSource eventSource) {
                    emitter.complete();
                }
            });
        }, FluxSink.OverflowStrategy.BUFFER);
    }

    @Override
    public boolean isAvailable() {
        return StrUtil.isNotBlank(llmProperties.getZhipu().getApiKey());
    }

    /**
     * 将OpenAI格式请求转换为Anthropic格式
     */
    private JSONObject convertToAnthropicFormat(ChatCompletionRequest request, LlmProperties.ZhipuConfig config) {
        JSONObject anthropicRequest = new JSONObject();
        
        String model = StrUtil.isNotBlank(request.getModel()) ? request.getModel() : config.getModel();
        anthropicRequest.put("model", model);
        anthropicRequest.put("max_tokens", request.getMaxTokens() != null ? request.getMaxTokens() : config.getMaxTokens());
        
        if (request.getTemperature() != null) {
            anthropicRequest.put("temperature", request.getTemperature());
        }
        if (request.getTopP() != null) {
            anthropicRequest.put("top_p", request.getTopP());
        }
        
        // 转换消息格式
        JSONArray messages = new JSONArray();
        String systemPrompt = null;
        
        for (ChatCompletionRequest.Message msg : request.getMessages()) {
            if ("system".equals(msg.getRole())) {
                systemPrompt = getContentAsString(msg.getContent());
            } else {
                JSONObject message = new JSONObject();
                message.put("role", msg.getRole());
                message.put("content", getContentAsString(msg.getContent()));
                messages.add(message);
            }
        }
        
        if (systemPrompt != null) {
            anthropicRequest.put("system", systemPrompt);
        }
        anthropicRequest.put("messages", messages);
        
        return anthropicRequest;
    }

    /**
     * 将Anthropic响应转换为OpenAI格式
     */
    private ChatCompletionResponse convertToOpenAIFormat(String responseBody, String model) {
        JSONObject anthropicResponse = JSON.parseObject(responseBody);
        
        String content = "";
        JSONArray contentArray = anthropicResponse.getJSONArray("content");
        if (contentArray != null && !contentArray.isEmpty()) {
            content = contentArray.getJSONObject(0).getString("text");
        }
        
        JSONObject usage = anthropicResponse.getJSONObject("usage");
        
        return ChatCompletionResponse.builder()
                .id("chatcmpl-" + IdUtil.fastSimpleUUID())
                .object("chat.completion")
                .created(System.currentTimeMillis() / 1000)
                .model(model)
                .choices(Collections.singletonList(
                        ChatCompletionResponse.Choice.builder()
                                .index(0)
                                .message(ChatCompletionRequest.Message.builder()
                                        .role("assistant")
                                        .content(content)
                                        .build())
                                .finishReason(anthropicResponse.getString("stop_reason"))
                                .build()
                ))
                .usage(ChatCompletionResponse.Usage.builder()
                        .promptTokens(usage != null ? usage.getInteger("input_tokens") : 0)
                        .completionTokens(usage != null ? usage.getInteger("output_tokens") : 0)
                        .totalTokens(usage != null ? 
                                usage.getInteger("input_tokens") + usage.getInteger("output_tokens") : 0)
                        .build())
                .build();
    }

    private String getContentAsString(Object content) {
        if (content instanceof String) {
            return (String) content;
        } else if (content instanceof List) {
            StringBuilder sb = new StringBuilder();
            for (Object item : (List<?>) content) {
                if (item instanceof JSONObject) {
                    JSONObject obj = (JSONObject) item;
                    if ("text".equals(obj.getString("type"))) {
                        sb.append(obj.getString("text"));
                    }
                }
            }
            return sb.toString();
        }
        return content != null ? content.toString() : "";
    }
}

