package com.hbfk.controller;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hbfk.service.ModelCombinationExecutionService;
import com.hbfk.util.JsonBean;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * 模型组合执行结果管理控制器
 * 
 * @author AI Assistant
 * @date 2025-01-29
 */
@RestController
@RequestMapping("/api/model/combination/execution-results")
@Tag(name="模型组合执行结果管理",description="模型组合执行结果管理")
public class ModelCombinationExecutionController {

    private static final Logger logger = LoggerFactory.getLogger(ModelCombinationExecutionController.class);

    @Autowired
    private ModelCombinationExecutionService executionService;

    /**
     * 清理组合的执行结果数据
     * 用于解决删除指标时的外键约束问题
     */
    @PostMapping("/clear")
    @Operation(summary = "清理组合执行结果", description = "清理指定组合的所有执行结果数据，解决外键约束问题")
    public String clearCombinationExecutionResults(@RequestBody Map<String, Object> request) {
        try {
            String combinationId = (String) request.get("combinationId");
            
            if (combinationId == null || combinationId.trim().isEmpty()) {
                return JsonBean.error("组合ID不能为空");
            }
            
            logger.info("开始清理组合执行结果数据, combinationId: {}", combinationId);
            
            int deletedCount = executionService.clearExecutionResultsByCombinationId(combinationId);
            
            logger.info("清理组合执行结果完成, combinationId: {}, 删除记录数: {}", combinationId, deletedCount);
            
            return JsonBean.success("清理成功，共删除 " + deletedCount + " 条执行结果记录");
            
        } catch (Exception e) {
            logger.error("清理组合执行结果失败", e);
            return JsonBean.error("清理失败: " + e.getMessage());
        }
    }

    /**
     * 清理指定配置的执行结果数据
     */
    @PostMapping("/clear-by-config")
    @Operation(summary = "清理指定配置执行结果", description = "清理指定配置ID的执行结果数据")
    public String clearExecutionResultsByConfigId(@RequestBody Map<String, Object> request) {
        try {
            String configId = (String) request.get("configId");

            if (configId == null || configId.trim().isEmpty()) {
                return JsonBean.error("配置ID不能为空");
            }

            logger.info("开始清理配置执行结果数据, configId: {}", configId);

            int deletedCount = executionService.clearExecutionResultsByConfigId(configId);

            logger.info("清理配置执行结果完成, configId: {}, 删除记录数: {}", configId, deletedCount);

            return JsonBean.success("清理成功，共删除 " + deletedCount + " 条执行结果记录");

        } catch (Exception e) {
            logger.error("清理配置执行结果失败", e);
            return JsonBean.error("清理失败: " + e.getMessage());
        }
    }

    /**
     * 清理所有执行结果数据（危险操作，仅在必要时使用）
     */
    @PostMapping("/clear-all")
    @Operation(summary = "清理所有执行结果", description = "清理所有执行结果数据，危险操作")
    public String clearAllExecutionResults() {
        try {
            logger.warn("开始清理所有执行结果数据");

            int deletedCount = executionService.clearAllExecutionResults();

            logger.warn("清理所有执行结果完成, 删除记录数: {}", deletedCount);

            return JsonBean.success("清理成功，共删除 " + deletedCount + " 条执行结果记录");

        } catch (Exception e) {
            logger.error("清理所有执行结果失败", e);
            return JsonBean.error("清理失败: " + e.getMessage());
        }
    }

    /**
     * 获取执行结果统计信息
     */
    @PostMapping("/statistics")
    @Operation(summary = "获取执行结果统计", description = "获取执行结果的统计信息")
    public String getExecutionResultStatistics(@RequestBody(required = false) Map<String, Object> request) {
        try {
            String combinationId = null;
            if (request != null) {
                combinationId = (String) request.get("combinationId");
            }

            Map<String, Object> statistics = executionService.getExecutionResultStatistics(combinationId);

            return JsonBean.success("查询成功", statistics);

        } catch (Exception e) {
            logger.error("获取执行结果统计失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    /**
     * 删除单个执行记录
     */
    @PostMapping("/delete")
    @Operation(summary = "删除执行记录", description = "删除指定的执行记录及其相关结果数据")
    public String deleteExecutionRecord(@RequestBody Map<String, Object> request) {
        try {
            String executionId = (String) request.get("executionId");

            if (executionId == null || executionId.trim().isEmpty()) {
                return JsonBean.error("执行ID不能为空");
            }

            logger.info("开始删除执行记录, executionId: {}", executionId);

            int deletedCount = executionService.deleteExecutionRecord(executionId);

            logger.info("删除执行记录完成, executionId: {}, 删除记录数: {}", executionId, deletedCount);

            return JsonBean.success("删除成功");

        } catch (Exception e) {
            logger.error("删除执行记录失败", e);
            return JsonBean.error("删除失败: " + e.getMessage());
        }
    }
}
