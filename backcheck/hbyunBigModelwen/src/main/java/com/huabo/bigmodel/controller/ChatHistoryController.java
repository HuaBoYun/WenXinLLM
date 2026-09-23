package com.huabo.bigmodel.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huabo.bigmodel.common.Result;
import com.huabo.bigmodel.entity.ChatHistory;
import com.huabo.bigmodel.service.ChatHistoryService;
import com.huabo.bigmodel.vo.ChatHistoryPageVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * AI对话历史记录控制器
 */
@Slf4j
@RestController
@RequestMapping("/v1/ai/chat/history")
@Tag(name = "AI对话历史记录", description = "AI对话历史记录管理接口")
public class ChatHistoryController {

    @Autowired
    private ChatHistoryService chatHistoryService;

    /**
     * 保存对话历史记录
     */
    @PostMapping("/save")
    @Operation(summary = "保存对话历史记录")
    public Result<ChatHistory> saveHistory(@RequestBody ChatHistory chatHistory) {
        try {
            log.info("保存对话历史记录: userId={}, title={}", chatHistory.getUserId(), chatHistory.getTitle());
            ChatHistory saved = chatHistoryService.saveHistory(chatHistory);
            return Result.success(saved);
        } catch (Exception e) {
            log.error("保存对话历史记录失败", e);
            return Result.error("保存失败: " + e.getMessage());
        }
    }

    /**
     * 获取用户的对话历史列表（分页，支持滚动加载）
     * 当传入 pageNum 和 pageSize 时走分页逻辑，否则走旧的全量逻辑（向后兼容）
     */
    @GetMapping("/list/{userId}")
    @Operation(summary = "获取用户的对话历史列表（支持分页）")
    public Result<?> getHistoryList(
            @PathVariable String userId,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Integer pageNum,
            @RequestParam(required = false) Integer pageSize) {
        try {
            log.info("获取用户对话历史列表: userId={}, type={}, pageNum={}, pageSize={}", userId, type, pageNum, pageSize);

            // 如果传了分页参数，走分页逻辑
            if (pageNum != null || pageSize != null) {
                ChatHistoryPageVO pageVO = chatHistoryService.getHistoryListByPage(userId, type, pageNum, pageSize);
                return Result.success(pageVO);
            }

            // 否则走旧逻辑（向后兼容）
            List<ChatHistory> list = chatHistoryService.getHistoryListByUserId(userId, type);
            return Result.success(list);
        } catch (Exception e) {
            log.error("获取对话历史列表失败", e);
            return Result.error("获取失败: " + e.getMessage());
        }
    }

    /**
     * 获取单条对话历史详情
     */
    @GetMapping("/detail/{id}")
    @Operation(summary = "获取单条对话历史详情")
    public Result<ChatHistory> getHistoryDetail(@PathVariable String id) {
        try {
            log.info("获取对话历史详情: id={}", id);
            ChatHistory history = chatHistoryService.getHistoryById(id);
            return Result.success(history);
        } catch (Exception e) {
            log.error("获取对话历史详情失败", e);
            return Result.error("获取失败: " + e.getMessage());
        }
    }

    /**
     * 删除对话历史
     */
    @DeleteMapping("/delete/{id}")
    @Operation(summary = "删除对话历史")
    public Result<Void> deleteHistory(@PathVariable String id) {
        try {
            log.info("删除对话历史: id={}", id);
            chatHistoryService.deleteHistory(id);
            return Result.success();
        } catch (Exception e) {
            log.error("删除对话历史失败", e);
            return Result.error("删除失败: " + e.getMessage());
        }
    }

    /**
     * 更新对话历史
     */
    @PutMapping("/update")
    @Operation(summary = "更新对话历史")
    public Result<ChatHistory> updateHistory(@RequestBody ChatHistory chatHistory) {
        try {
            log.info("更新对话历史: id={}", chatHistory.getId());
            ChatHistory updated = chatHistoryService.updateHistory(chatHistory);
            return Result.success(updated);
        } catch (Exception e) {
            log.error("更新对话历史失败", e);
            return Result.error("更新失败: " + e.getMessage());
        }
    }
}
