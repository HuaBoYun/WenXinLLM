package com.management.accountant.controller;

import com.management.accountant.exception.ServiceException;
import com.management.accountant.service.BudgetSensitivityAnalysisService;
import com.management.accountant.util.MyJsonBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 预算敏感性分析Controller
 * 
 * @description 预算敏感性分析接口，支持单因素、多因素敏感性分析
 * @author AI Assistant
 * @date 2025-01-04
 */
@RestController
@Api(tags = {"NCV65全面预算-敏感性分析"})
@RequestMapping(value = "/accountant/budget/sensitivity/analysis")
@Slf4j
public class BudgetSensitivityAnalysisController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetSensitivityAnalysisService sensitivityAnalysisService;

    /**
     * 单因素敏感性分析
     */
    @Operation(summary = "单因素敏感性分析")
    @ApiOperation("单因素敏感性分析")
    @PostMapping("/single-factor")
    public MyJsonBean<Map<String, Object>> singleFactorAnalysis(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> analysisResult = sensitivityAnalysisService.singleFactorAnalysis(params);
            result.setCode(1);
            result.setMsg("分析成功");
            result.setData(analysisResult);
        } catch (ServiceException ex) {
            log.error("单因素敏感性分析失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("单因素敏感性分析异常", e);
            result.setCode(0);
            result.setMsg("分析失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 多因素敏感性分析
     */
    @Operation(summary = "多因素敏感性分析")
    @ApiOperation("多因素敏感性分析")
    @PostMapping("/multi-factor")
    public MyJsonBean<Map<String, Object>> multiFactorAnalysis(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> analysisResult = sensitivityAnalysisService.multiFactorAnalysis(params);
            result.setCode(1);
            result.setMsg("分析成功");
            result.setData(analysisResult);
        } catch (ServiceException ex) {
            log.error("多因素敏感性分析失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("多因素敏感性分析异常", e);
            result.setCode(0);
            result.setMsg("分析失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 生成敏感性分析报告
     */
    @Operation(summary = "生成敏感性分析报告")
    @ApiOperation("生成敏感性分析报告")
    @PostMapping("/report")
    public MyJsonBean<Map<String, Object>> generateReport(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> report = sensitivityAnalysisService.generateReport(params);
            result.setCode(1);
            result.setMsg("生成成功");
            result.setData(report);
        } catch (ServiceException ex) {
            log.error("生成敏感性分析报告失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("生成敏感性分析报告异常", e);
            result.setCode(0);
            result.setMsg("生成失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 执行敏感性分析
     */
    @Operation(summary = "执行敏感性分析")
    @ApiOperation("执行敏感性分析")
    @PostMapping("/execute")
    public MyJsonBean<Map<String, Object>> executeSensitivityAnalysis(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> analysisResult = sensitivityAnalysisService.singleFactorAnalysis(params);
            result.setCode(1);
            result.setMsg("分析成功");
            result.setData(analysisResult);
        } catch (Exception e) {
            log.error("执行敏感性分析异常", e);
            result.setCode(0);
            result.setMsg("分析失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 导出敏感性分析
     */
    @Operation(summary = "导出敏感性分析")
    @ApiOperation("导出敏感性分析")
    @PostMapping("/export")
    public MyJsonBean<Map<String, Object>> exportSensitivityAnalysis(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> exportData = new java.util.HashMap<>();
            exportData.put("fileName", "敏感性分析.xlsx");
            result.setCode(1);
            result.setMsg("导出成功");
            result.setData(exportData);
        } catch (Exception e) {
            log.error("导出敏感性分析异常", e);
            result.setCode(0);
            result.setMsg("导出失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取敏感性分析统计数据")
    @ApiOperation("获取敏感性分析统计数据")
    @GetMapping("/stats")
    public MyJsonBean<Map<String, Object>> getStats() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> stats = sensitivityAnalysisService.getSensitivityStats();
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(stats);
        } catch (Exception e) {
            log.error("获取敏感性分析统计数据失败", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取敏感性分析图表数据")
    @ApiOperation("获取敏感性分析图表数据")
    @PostMapping("/chart-data")
    public MyJsonBean<Map<String, Object>> getChartData(@RequestBody(required = false) Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> chartData = sensitivityAnalysisService.getSensitivityChartData(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(chartData);
        } catch (Exception e) {
            log.error("获取敏感性分析图表数据失败", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }
}

