package com.huabo.bigmodel.controller;

import com.huabo.bigmodel.dto.AiChatRequest;
import com.huabo.bigmodel.dto.AiChatResponse;
import com.huabo.bigmodel.service.AiChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

/**
 * AI对话控制器 - 支持联网搜索和深度思考
 */
@Slf4j
@RestController
@RequestMapping("/v1/ai")
@Tag(name = "AI对话接口", description = "星光问心AI大模型对话接口，支持联网搜索和深度思考")
public class AiChatController {

    private final AiChatService aiChatService;

    public AiChatController(AiChatService aiChatService) {
        this.aiChatService = aiChatService;
    }

    /**
     * 流式对话接口
     */
    @PostMapping(value = "/chat/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "流式对话", description = "发送消息并获取流式响应，支持联网搜索和深度思考")
    public Flux<String> chatStream(@RequestBody AiChatRequest request) {
        log.info("收到流式对话请求: sessionId={}, enableThinking={}, enableWebSearch={}", 
                request.getSessionId(), request.getEnableThinking(), request.getEnableWebSearch());
        return aiChatService.chatStream(request);
    }

    /**
     * 普通对话接口（非流式）
     */
    @PostMapping("/chat")
    @Operation(summary = "普通对话", description = "发送消息并获取完整响应")
    public ResponseEntity<AiChatResponse> chat(@RequestBody AiChatRequest request) {
        log.info("收到对话请求: sessionId={}, enableThinking={}, enableWebSearch={}", 
                request.getSessionId(), request.getEnableThinking(), request.getEnableWebSearch());
        try {
            String content = aiChatService.chat(request);
            return ResponseEntity.ok(AiChatResponse.builder()
                    .success(true)
                    .content(content)
                    .sessionId(request.getSessionId())
                    .usedThinking(request.getEnableThinking())
                    .usedWebSearch(request.getEnableWebSearch())
                    .build());
        } catch (Exception e) {
            log.error("对话失败", e);
            return ResponseEntity.ok(AiChatResponse.error(e.getMessage()));
        }
    }

    /**
     * 清除会话历史
     */
    @DeleteMapping("/session/{sessionId}")
    @Operation(summary = "清除会话", description = "清除指定会话的对话历史")
    public ResponseEntity<AiChatResponse> clearSession(
            @Parameter(description = "会话ID") @PathVariable String sessionId) {
        log.info("清除会话: {}", sessionId);
        aiChatService.clearSession(sessionId);
        return ResponseEntity.ok(AiChatResponse.builder()
                .success(true)
                .message("会话已清除")
                .sessionId(sessionId)
                .build());
    }

    /**
     * 简单问答接口（GET方式，方便测试）
     */
    @GetMapping("/ask")
    @Operation(summary = "简单问答", description = "GET方式的简单问答接口，方便测试")
    public ResponseEntity<AiChatResponse> ask(
            @Parameter(description = "问题内容") @RequestParam String question,
            @Parameter(description = "是否开启深度思考") @RequestParam(defaultValue = "false") Boolean thinking,
            @Parameter(description = "是否开启联网搜索") @RequestParam(defaultValue = "true") Boolean search) {
        log.info("收到简单问答: question={}, thinking={}, search={}", question, thinking, search);
        
        AiChatRequest request = new AiChatRequest();
        request.setMessage(question);
        request.setEnableThinking(thinking);
        request.setEnableWebSearch(search);
        request.setStream(false);
        
        try {
            String content = aiChatService.chat(request);
            return ResponseEntity.ok(AiChatResponse.builder()
                    .success(true)
                    .content(content)
                    .usedThinking(thinking)
                    .usedWebSearch(search)
                    .build());
        } catch (Exception e) {
            log.error("问答失败", e);
            return ResponseEntity.ok(AiChatResponse.error(e.getMessage()));
        }
    }
}

