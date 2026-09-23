package com.huabo.bigmodel.controller;

import com.huabo.bigmodel.common.Result;
import com.huabo.bigmodel.entity.Blueprint;
import com.huabo.bigmodel.entity.BlueprintDraft;
import com.huabo.bigmodel.service.BlueprintService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 业务蓝图控制器
 * 路径：/v1/ai/blueprint
 * 网关访问：/bigmodel/v1/ai/blueprint
 */
@Slf4j
@RestController
@RequestMapping("/v1/ai/blueprint")
@Tag(name = "业务蓝图管理", description = "AI拆分/草稿/结构化提交")
public class BlueprintController {

    @Autowired
    private BlueprintService blueprintService;

    // ============ AI 拆分 ============

    @PostMapping("/analyze")
    @Operation(summary = "AI拆分需求文档为功能点")
    public Map<String, Object> analyze(@RequestBody Map<String, String> body) {
        String title = body.getOrDefault("title", "");
        String content = body.getOrDefault("content", "");
        try {
            return blueprintService.analyze(title, content);
        } catch (Exception e) {
            log.error("AI拆分失败", e);
            Map<String, Object> err = new HashMap<>();
            err.put("success", false);
            err.put("requirements", java.util.Collections.emptyList());
            err.put("message", "请求失败: " + e.getMessage());
            return err;
        }
    }

    // ============ 草稿 ============

    @PostMapping("/draft/save")
    @Operation(summary = "保存蓝图草稿")
    public Result<BlueprintDraft> saveDraft(@RequestBody BlueprintDraft draft) {
        try {
            if (draft.getUserId() == null || draft.getUserId().isEmpty()) {
                return Result.error("userId 不能为空");
            }
            return Result.success(blueprintService.saveDraft(draft));
        } catch (Exception e) {
            log.error("保存蓝图草稿失败", e);
            return Result.error("保存失败: " + e.getMessage());
        }
    }

    @GetMapping("/draft/list")
    @Operation(summary = "蓝图草稿列表")
    public Result<List<BlueprintDraft>> listDrafts(@RequestParam("userId") String userId) {
        try {
            return Result.success(blueprintService.listDrafts(userId));
        } catch (Exception e) {
            log.error("获取蓝图草稿列表失败 userId={}", userId, e);
            return Result.error("获取失败: " + e.getMessage());
        }
    }

    @GetMapping("/draft/{id}")
    @Operation(summary = "蓝图草稿详情")
    public Result<BlueprintDraft> getDraft(@PathVariable("id") String id) {
        try {
            return Result.success(blueprintService.getDraft(id));
        } catch (Exception e) {
            log.error("获取蓝图草稿失败 id={}", id, e);
            return Result.error("获取失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/draft/{id}")
    @Operation(summary = "删除蓝图草稿")
    public Result<Void> deleteDraft(@PathVariable("id") String id) {
        try {
            blueprintService.deleteDraft(id);
            return Result.success();
        } catch (Exception e) {
            log.error("删除蓝图草稿失败 id={}", id, e);
            return Result.error("删除失败: " + e.getMessage());
        }
    }

    // ============ 结构化提交 ============

    @PostMapping("/save")
    @Operation(summary = "保存蓝图(主表+需求点+Bug)")
    public Result<Map<String, String>> save(@RequestBody Map<String, Object> payload) {
        try {
            String id = blueprintService.saveBlueprint(payload);
            Map<String, String> data = new HashMap<>();
            data.put("id", id);
            return Result.success(data);
        } catch (Exception e) {
            log.error("保存蓝图失败", e);
            return Result.error("保存失败: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "蓝图详情")
    public Result<Map<String, Object>> detail(@PathVariable("id") String id) {
        try {
            return Result.success(blueprintService.getBlueprintDetail(id));
        } catch (Exception e) {
            log.error("获取蓝图详情失败 id={}", id, e);
            return Result.error("获取失败: " + e.getMessage());
        }
    }

    @GetMapping("/list")
    @Operation(summary = "蓝图列表")
    public Result<List<Blueprint>> list(@RequestParam(value = "userId", required = false) String userId) {
        try {
            return Result.success(blueprintService.listBlueprints(userId));
        } catch (Exception e) {
            log.error("获取蓝图列表失败 userId={}", userId, e);
            return Result.error("获取失败: " + e.getMessage());
        }
    }
}
