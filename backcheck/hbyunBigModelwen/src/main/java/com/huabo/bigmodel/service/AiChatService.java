package com.huabo.bigmodel.service;

import com.huabo.bigmodel.dto.AiChatRequest;
import reactor.core.publisher.Flux;

/**
 * AI聊天服务接口
 */
public interface AiChatService {

    /**
     * 发送消息并获取流式响应
     *
     * @param request 聊天请求
     * @return 流式响应
     */
    Flux<String> chatStream(AiChatRequest request);

    /**
     * 发送消息并获取完整响应
     *
     * @param request 聊天请求
     * @return 完整响应内容
     */
    String chat(AiChatRequest request);

    /**
     * 清除会话历史
     *
     * @param sessionId 会话ID
     */
    void clearSession(String sessionId);
}

