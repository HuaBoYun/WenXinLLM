package com.huabo.bigmodel.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
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
import java.util.concurrent.TimeUnit;

/**
 * OpenAI服务实现
 * 调用OpenAI官方API或兼容OpenAI协议的第三方服务
 */
@Slf4j
@Service("openaiService")
public class OpenAIServiceImpl implements LlmService {

    private final LlmProperties llmProperties;
    private OkHttpClient httpClient;

    public OpenAIServiceImpl(LlmProperties llmProperties) {
        this.llmProperties = llmProperties;
    }

    @PostConstruct
    public void init() {
        LlmProperties.OpenAIConfig config = llmProperties.getOpenai();
        this.httpClient = new OkHttpClient.Builder()
                .dns(Ipv4FirstDns.INSTANCE)
                .connectTimeout(config.getTimeout(), TimeUnit.SECONDS)
                .readTimeout(config.getTimeout(), TimeUnit.SECONDS)
                .writeTimeout(config.getTimeout(), TimeUnit.SECONDS)
                .build();
    }

    @Override
    public String getProviderName() {
        return "openai";
    }

    @Override
    public ChatCompletionResponse chat(ChatCompletionRequest request) {
        LlmProperties.OpenAIConfig config = llmProperties.getOpenai();
        
        // 设置默认模型
        if (StrUtil.isBlank(request.getModel())) {
            request.setModel(config.getModel());
        }
        request.setStream(false);

        String url = config.getBaseUrl() + "/chat/completions";
        String requestBody = JSON.toJSONString(request);
        
        log.debug("OpenAI请求URL: {}", url);
        log.debug("OpenAI请求体: {}", requestBody);

        Request httpRequest = new Request.Builder()
                .url(url)
                .addHeader("Authorization", "Bearer " + config.getApiKey())
                .addHeader("Content-Type", "application/json")
                .post(RequestBody.create(requestBody, MediaType.parse("application/json")))
                .build();

        try (Response response = httpClient.newCall(httpRequest).execute()) {
            String responseBody = response.body() != null ? response.body().string() : "";
            log.debug("OpenAI响应: {}", responseBody);
            
            if (!response.isSuccessful()) {
                log.error("OpenAI调用失败: {} - {}", response.code(), responseBody);
                throw new RuntimeException("OpenAI调用失败: " + responseBody);
            }
            
            return JSON.parseObject(responseBody, ChatCompletionResponse.class);
        } catch (IOException e) {
            log.error("OpenAI调用异常", e);
            throw new RuntimeException("OpenAI调用异常: " + e.getMessage(), e);
        }
    }

    @Override
    public Flux<String> chatStream(ChatCompletionRequest request) {
        LlmProperties.OpenAIConfig config = llmProperties.getOpenai();
        
        if (StrUtil.isBlank(request.getModel())) {
            request.setModel(config.getModel());
        }
        request.setStream(true);

        String url = config.getBaseUrl() + "/chat/completions";
        String requestBody = JSON.toJSONString(request);

        return Flux.create(emitter -> {
            Request httpRequest = new Request.Builder()
                    .url(url)
                    .addHeader("Authorization", "Bearer " + config.getApiKey())
                    .addHeader("Content-Type", "application/json")
                    .addHeader("Accept", "text/event-stream")
                    .post(RequestBody.create(requestBody, MediaType.parse("application/json")))
                    .build();

            EventSource.Factory factory = EventSources.createFactory(httpClient);
            factory.newEventSource(httpRequest, new EventSourceListener() {
                @Override
                public void onEvent(EventSource eventSource, String id, String type, String data) {
                    if ("[DONE]".equals(data)) {
                        emitter.complete();
                        return;
                    }
                    emitter.next("data: " + data + "\n\n");
                }

                @Override
                public void onFailure(EventSource eventSource, Throwable t, Response response) {
                    log.error("OpenAI流式调用失败", t);
                    emitter.error(t != null ? t : new RuntimeException("Stream failed"));
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
        return StrUtil.isNotBlank(llmProperties.getOpenai().getApiKey());
    }
}

