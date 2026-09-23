package com.management.accountant.controller;

import com.management.accountant.exception.ServiceException;
import com.management.accountant.service.BudgetBatchCalculationService;
import com.management.accountant.util.MyJsonBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 预算批量计算Controller
 * 
 * @description 预算批量计算接口，支持批量重算、批量汇总等
 * @author AI Assistant
 * @date 2025-01-04
 */
@RestController
@Api(tags = {"NCV65全面预算-批量计算"})
@RequestMapping(value = "/accountant/advanced/batch/calculation")
@Slf4j
public class BudgetBatchCalculationController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetBatchCalculationService batchCalculationService;

    /**
     * 批量计算预算
     */
    @Operation(summary = "批量计算预算")
    @ApiOperation("批量计算预算")
    @PostMapping("/execute")
    public MyJsonBean<Map<String, Object>> executeBatchCalculation(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> calculationResult = batchCalculationService.executeBatchCalculation(params);
            result.setCode(1);
            result.setMsg("计算成功");
            result.setData(calculationResult);
        } catch (ServiceException ex) {
            log.error("批量计算预算失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("批量计算预算异常", e);
            result.setCode(0);
            result.setMsg("计算失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 批量汇总预算
     */
    @Operation(summary = "批量汇总预算")
    @ApiOperation("批量汇总预算")
    @PostMapping("/summarize")
    public MyJsonBean<Map<String, Object>> batchSummarize(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> summarizeResult = batchCalculationService.batchSummarize(params);
            result.setCode(1);
            result.setMsg("汇总成功");
            result.setData(summarizeResult);
        } catch (ServiceException ex) {
            log.error("批量汇总预算失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("批量汇总预算异常", e);
            result.setCode(0);
            result.setMsg("汇总失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 批量重算预算
     */
    @Operation(summary = "批量重算预算")
    @ApiOperation("批量重算预算")
    @PostMapping("/recalculate")
    public MyJsonBean<Map<String, Object>> batchRecalculate(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> recalculateResult = batchCalculationService.batchRecalculate(params);
            result.setCode(1);
            result.setMsg("重算成功");
            result.setData(recalculateResult);
        } catch (ServiceException ex) {
            log.error("批量重算预算失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("批量重算预算异常", e);
            result.setCode(0);
            result.setMsg("重算失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取计算进度
     */
    @Operation(summary = "获取计算进度")
    @ApiOperation("获取计算进度")
    @GetMapping("/{taskId}/progress")
    public MyJsonBean<Map<String, Object>> getCalculationProgress(@PathVariable String taskId) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> progress = batchCalculationService.getCalculationProgress(taskId);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(progress);
        } catch (Exception e) {
            log.error("获取计算进度异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 取消批量计算
     */
    @Operation(summary = "取消批量计算")
    @ApiOperation("取消批量计算")
    @PostMapping("/{taskId}/cancel")
    public MyJsonBean<Void> cancelCalculation(@PathVariable String taskId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            batchCalculationService.cancelCalculation(taskId);
            result.setCode(1);
            result.setMsg("取消成功");
        } catch (ServiceException ex) {
            log.error("取消批量计算失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("取消批量计算异常", e);
            result.setCode(0);
            result.setMsg("取消失败：" + e.getMessage());
        }
        return result;
    }

    // list/stats/create/update/delete 端点已在 BudgetAdvancedFeaturesController 中实现，此处不再重复注册

    /**
     * 执行批量计算任务
     */
    @Operation(summary = "执行批量计算任务")
    @ApiOperation("执行批量计算任务")
    @PostMapping("/{taskId}/run")
    public MyJsonBean<Map<String, Object>> runTask(@PathVariable String taskId) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> runResult = batchCalculationService.runTask(taskId);
            result.setCode(1);
            result.setMsg("执行成功");
            result.setData(runResult);
        } catch (Exception e) {
            log.error("执行批量计算任务异常", e);
            result.setCode(0);
            result.setMsg("执行失败：" + e.getMessage());
        }
        return result;
    }

    // stop/retry/copy/export/results/logs 端点已在 BudgetAdvancedFeaturesController 中实现，此处不再重复注册
}

