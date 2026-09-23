package com.management.accountant.controller;

import com.management.accountant.exception.ServiceException;
import com.management.accountant.service.BudgetOptimizationService;
import com.management.accountant.util.MyJsonBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 预算优化Controller
 * 
 * @description 预算优化接口，支持线性规划、目标规划、多目标优化等
 * @author AI Assistant
 * @date 2025-01-04
 */
@RestController
@Api(tags = {"NCV65全面预算-预算优化"})
@RequestMapping(value = "/accountant/advanced/optimization")
@Slf4j
public class BudgetOptimizationController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetOptimizationService optimizationService;

    /**
     * 线性规划优化
     */
    @Operation(summary = "线性规划优化")
    @ApiOperation("线性规划优化")
    @PostMapping("/linear-programming")
    public MyJsonBean<Map<String, Object>> linearProgramming(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> optimization = optimizationService.linearProgramming(params);
            result.setCode(1);
            result.setMsg("优化成功");
            result.setData(optimization);
        } catch (ServiceException ex) {
            log.error("线性规划优化失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("线性规划优化异常", e);
            result.setCode(0);
            result.setMsg("优化失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 目标规划
     */
    @Operation(summary = "目标规划")
    @ApiOperation("目标规划")
    @PostMapping("/goal-programming")
    public MyJsonBean<Map<String, Object>> goalProgramming(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> optimization = optimizationService.goalProgramming(params);
            result.setCode(1);
            result.setMsg("规划成功");
            result.setData(optimization);
        } catch (ServiceException ex) {
            log.error("目标规划失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("目标规划异常", e);
            result.setCode(0);
            result.setMsg("规划失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 多目标优化
     */
    @Operation(summary = "多目标优化")
    @ApiOperation("多目标优化")
    @PostMapping("/multi-objective")
    public MyJsonBean<Map<String, Object>> multiObjectiveOptimization(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> optimization = optimizationService.multiObjectiveOptimization(params);
            result.setCode(1);
            result.setMsg("优化成功");
            result.setData(optimization);
        } catch (ServiceException ex) {
            log.error("多目标优化失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("多目标优化异常", e);
            result.setCode(0);
            result.setMsg("优化失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 约束优化
     */
    @Operation(summary = "约束优化")
    @ApiOperation("约束优化")
    @PostMapping("/constrained")
    public MyJsonBean<Map<String, Object>> constrainedOptimization(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> optimization = optimizationService.constrainedOptimization(params);
            result.setCode(1);
            result.setMsg("优化成功");
            result.setData(optimization);
        } catch (ServiceException ex) {
            log.error("约束优化失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("约束优化异常", e);
            result.setCode(0);
            result.setMsg("优化失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 生成优化报告
     */
    @Operation(summary = "生成优化报告")
    @ApiOperation("生成优化报告")
    @PostMapping("/report")
    public MyJsonBean<Map<String, Object>> generateOptimizationReport(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> report = optimizationService.generateOptimizationReport(params);
            result.setCode(1);
            result.setMsg("生成成功");
            result.setData(report);
        } catch (ServiceException ex) {
            log.error("生成优化报告失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("生成优化报告异常", e);
            result.setCode(0);
            result.setMsg("生成失败：" + e.getMessage());
        }
        return result;
    }

    // list/stats/create/update/delete/run/stop/apply/export/copy/results/logs 端点已在 BudgetAdvancedFeaturesController 中实现
}

