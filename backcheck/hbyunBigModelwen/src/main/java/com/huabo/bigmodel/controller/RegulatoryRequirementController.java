package com.huabo.bigmodel.controller;

import com.huabo.bigmodel.common.Result;
import com.huabo.bigmodel.entity.RegulatoryRequirement;
import com.huabo.bigmodel.service.RegulatoryRequirementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 监管模型需求控制器
 * 路径：/v1/ai/regulatory
 * 网关访问：/bigmodel/v1/ai/regulatory
 */
@Slf4j
@RestController
@RequestMapping("/v1/ai/regulatory")
@Tag(name = "监管模型需求管理", description = "监管模型需求的新增/查询/删除/提交审批")
public class RegulatoryRequirementController {

    @Autowired
    private RegulatoryRequirementService requirementService;

    @PostMapping("/save")
    @Operation(summary = "保存需求(主表 + 关键指标子表)")
    public Result<Map<String, String>> save(@RequestBody Map<String, Object> payload) {
        try {
            String id = requirementService.saveRequirement(payload);
            Map<String, String> data = new HashMap<>();
            data.put("id", id);
            return Result.success(data);
        } catch (Exception e) {
            log.error("保存监管需求失败", e);
            return Result.error("保存失败: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @Operation(summary = "需求详情")
    public Result<Map<String, Object>> detail(@PathVariable("id") String id) {
        try {
            return Result.success(requirementService.getRequirementDetail(id));
        } catch (Exception e) {
            log.error("获取监管需求详情失败 id={}", id, e);
            return Result.error("获取失败: " + e.getMessage());
        }
    }

    // 【2026-08-25 新增】需求管理页"需求评审"按业务梳理模板一对一加载关联需求；
    // 未建立关联时 data 为 null，前端打开空白评审表单
    @GetMapping("/by-template/{templateId}")
    @Operation(summary = "按关联的业务梳理模板ID查询需求详情(一对一)")
    public Result<Map<String, Object>> detailByTemplate(@PathVariable("templateId") String templateId) {
        try {
            return Result.success(requirementService.getRequirementByTemplateId(templateId));
        } catch (Exception e) {
            log.error("按模板获取监管需求失败 templateId={}", templateId, e);
            return Result.error("获取失败: " + e.getMessage());
        }
    }

    @GetMapping("/list")
    @Operation(summary = "需求列表")
    public Result<List<RegulatoryRequirement>> list(
            @RequestParam(value = "userId", required = false) String userId) {
        try {
            return Result.success(requirementService.listRequirements(userId));
        } catch (Exception e) {
            log.error("获取监管需求列表失败 userId={}", userId, e);
            return Result.error("获取失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "删除需求")
    public Result<Void> delete(@PathVariable("id") String id) {
        try {
            requirementService.deleteRequirement(id);
            return Result.success();
        } catch (Exception e) {
            log.error("删除监管需求失败 id={}", id, e);
            return Result.error("删除失败: " + e.getMessage());
        }
    }

    @PostMapping("/submit/{id}")
    @Operation(summary = "提交审批")
    public Result<Void> submit(@PathVariable("id") String id) {
        try {
            requirementService.submitApproval(id);
            return Result.success();
        } catch (Exception e) {
            log.error("提交审批失败 id={}", id, e);
            return Result.error("提交失败: " + e.getMessage());
        }
    }
}
