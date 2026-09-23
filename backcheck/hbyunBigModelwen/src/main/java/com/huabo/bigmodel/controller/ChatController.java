package com.huabo.bigmodel.controller;

import com.huabo.bigmodel.dto.*;
import com.huabo.bigmodel.service.LlmService;
import com.huabo.bigmodel.service.LlmServiceFactory;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * OpenAI兼容API控制器
 * 提供符合OpenAI API规范的接口
 */
@Slf4j
@RestController
@RequestMapping("/v1")
@RequiredArgsConstructor
@Tag(name = "OpenAI兼容API", description = "符合OpenAI API规范的大模型接口")
public class ChatController {

    private final LlmServiceFactory llmServiceFactory;

    /**
     * Chat Completions API
     * 符合OpenAI /v1/chat/completions 规范
     */
    @PostMapping(value = "/chat/completions", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_EVENT_STREAM_VALUE})
    @Operation(summary = "对话补全", description = "创建一个对话补全请求，支持流式和非流式响应")
    public Object chatCompletions(
            @Validated @RequestBody ChatCompletionRequest request,
            @RequestHeader(value = "Authorization", required = false) String authorization) {
        
        log.info("收到对话请求, model: {}, stream: {}, messages: {}", 
                request.getModel(), request.getStream(), request.getMessages().size());
        
        try {
            // 根据模型名称智能选择服务
            LlmService llmService = llmServiceFactory.getServiceByModel(request.getModel());
            
            if (Boolean.TRUE.equals(request.getStream())) {
                // 流式响应
                return chatStream(request, llmService);
            } else {
                // 非流式响应
                ChatCompletionResponse response = llmService.chat(request);
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            log.error("对话请求处理失败", e);
            return ResponseEntity.internalServerError()
                    .body(ErrorResponse.serverError(e.getMessage()));
        }
    }

    /**
     * 流式响应处理
     */
    private Flux<String> chatStream(ChatCompletionRequest request, LlmService llmService) {
        return llmService.chatStream(request)
                .doOnError(e -> log.error("流式响应错误", e))
                .onErrorResume(e -> Flux.just("data: " + 
                        com.alibaba.fastjson.JSON.toJSONString(ErrorResponse.serverError(e.getMessage())) + "\n\n"));
    }

    /**
     * 获取模型列表
     * 符合OpenAI /v1/models 规范
     */
    @GetMapping("/models")
    @Operation(summary = "获取模型列表", description = "列出当前可用的模型")
    public ResponseEntity<ModelsResponse> listModels() {
        log.info("获取模型列表");
        
        long now = System.currentTimeMillis() / 1000;
        
        ModelsResponse response = ModelsResponse.builder()
                .data(Arrays.asList(
                        ModelsResponse.ModelData.builder()
                                .id("gpt-3.5-turbo")
                                .created(now)
                                .build(),
                        ModelsResponse.ModelData.builder()
                                .id("gpt-4")
                                .created(now)
                                .build(),
                        ModelsResponse.ModelData.builder()
                                .id("gpt-4-turbo")
                                .created(now)
                                .build(),
                        ModelsResponse.ModelData.builder()
                                .id("claude-3-sonnet-20240229")
                                .created(now)
                                .build(),
                        ModelsResponse.ModelData.builder()
                                .id("claude-3-opus-20240229")
                                .created(now)
                                .build(),
                        ModelsResponse.ModelData.builder()
                                .id("claude-3-haiku-20240307")
                                .created(now)
                                .build()
                ))
                .build();
        
        return ResponseEntity.ok(response);
    }

    /**
     * 获取单个模型信息
     */
    @GetMapping("/models/{model}")
    @Operation(summary = "获取模型信息", description = "获取指定模型的详细信息")
    public ResponseEntity<ModelsResponse.ModelData> getModel(@PathVariable String model) {
        log.info("获取模型信息: {}", model);
        
        ModelsResponse.ModelData modelData = ModelsResponse.ModelData.builder()
                .id(model)
                .created(System.currentTimeMillis() / 1000)
                .build();
        
        return ResponseEntity.ok(modelData);
    }

    /**
     * 健康检查接口
     */
    @GetMapping("/health")
    @Operation(summary = "健康检查", description = "检查服务是否正常运行")
    public ResponseEntity<String> health() {
        return ResponseEntity.ok("OK");
    }
}

