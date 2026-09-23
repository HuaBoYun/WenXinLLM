package com.huabo.bigmodel.service;

import com.huabo.bigmodel.dto.ChatCompletionRequest;
import com.huabo.bigmodel.dto.ChatCompletionResponse;
import reactor.core.publisher.Flux;

/**
 * 大模型服务接口
 * 定义统一的LLM调用规范
 */
public interface LlmService {

    /**
     * 获取提供商名称
     */
    String getProviderName();

    /**
     * 同步调用大模型
     * 
     * @param request 请求参数
     * @return 完整响应
     */
    ChatCompletionResponse chat(ChatCompletionRequest request);

    /**
     * 流式调用大模型
     * 
     * @param request 请求参数
     * @return 流式响应
     */
    Flux<String> chatStream(ChatCompletionRequest request);

    /**
     * 检查服务是否可用
     */
    boolean isAvailable();
}

