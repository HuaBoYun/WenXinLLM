package com.huabo.bigmodel.controller;

import com.huabo.bigmodel.common.Result;
import com.huabo.bigmodel.entity.PromptConfig;
import com.huabo.bigmodel.service.PromptConfigService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * AI提示词配置控制器
 * <p>
 * 提供用户自定义系统提示词的查询与保存能力。
 * 当前主要用于 AI 咨询（promptType=consult），结构上支持其它类型扩展。
 */
@Slf4j
@RestController
@RequestMapping("/v1/ai/prompt")
@Tag(name = "AI提示词配置", description = "用户自定义系统提示词的查询与保存")
public class PromptConfigController {

    @Autowired
    private PromptConfigService promptConfigService;

    /**
     * 获取用户的提示词配置
     * 未配置时返回 data=null，由前端使用内置默认提示词兜底。
     */
    @GetMapping
    @Operation(summary = "获取提示词配置")
    public Result<PromptConfig> getPrompt(
            @RequestParam String userId,
            @RequestParam(defaultValue = "consult") String type) {
        try {
            log.info("获取提示词配置: userId={}, type={}", userId, type);
            PromptConfig config = promptConfigService.getPrompt(userId, type);
            return Result.success(config);
        } catch (Exception e) {
            log.error("获取提示词配置失败", e);
            return Result.error("获取失败: " + e.getMessage());
        }
    }

    /**
     * 保存（或更新）用户的提示词配置
     */
    @PostMapping("/save")
    @Operation(summary = "保存提示词配置")
    public Result<PromptConfig> savePrompt(@RequestBody PromptConfig request) {
        try {
            if (request.getUserId() == null || request.getUserId().isEmpty()) {
                return Result.error("用户ID不能为空");
            }
            String type = (request.getPromptType() == null || request.getPromptType().isEmpty())
                    ? "consult" : request.getPromptType();
            log.info("保存提示词配置: userId={}, type={}", request.getUserId(), type);
            PromptConfig saved = promptConfigService.savePrompt(
                    request.getUserId(), type, request.getPromptContent());
            return Result.success(saved);
        } catch (Exception e) {
            log.error("保存提示词配置失败", e);
            return Result.error("保存失败: " + e.getMessage());
        }
    }
}
