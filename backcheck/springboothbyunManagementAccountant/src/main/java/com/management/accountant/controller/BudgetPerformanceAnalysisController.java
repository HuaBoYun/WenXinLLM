package com.management.accountant.controller;

import com.management.accountant.exception.ServiceException;
import com.management.accountant.service.BudgetPerformanceAnalysisService;
import com.management.accountant.util.MyJsonBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 预算绩效分析Controller
 * 
 * @description 预算绩效分析接口，支持绩效评价、KPI分析等
 * @author AI Assistant
 * @date 2025-01-04
 */
@RestController
@Api(tags = {"NCV65全面预算-绩效分析"})
@RequestMapping(value = "/accountant/budget/performance/analysis")
@Slf4j
public class BudgetPerformanceAnalysisController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetPerformanceAnalysisService performanceAnalysisService;

    /**
     * 执行绩效分析
     */
    @Operation(summary = "执行绩效分析")
    @ApiOperation("执行绩效分析")
    @PostMapping("/execute")
    public MyJsonBean<Map<String, Object>> executePerformanceAnalysis(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> analysisResult = performanceAnalysisService.executePerformanceAnalysis(params);
            result.setCode(1);
            result.setMsg("分析成功");
            result.setData(analysisResult);
        } catch (ServiceException ex) {
            log.error("执行绩效分析失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("执行绩效分析异常", e);
            result.setCode(0);
            result.setMsg("分析失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取KPI指标
     */
    @Operation(summary = "获取KPI指标")
    @ApiOperation("获取KPI指标")
    @PostMapping("/kpi")
    public MyJsonBean<Map<String, Object>> getKPIIndicators(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> kpiData = performanceAnalysisService.getKPIIndicators(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(kpiData);
        } catch (ServiceException ex) {
            log.error("获取KPI指标失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("获取KPI指标异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 生成绩效报告
     */
    @Operation(summary = "生成绩效报告")
    @ApiOperation("生成绩效报告")
    @PostMapping("/report")
    public MyJsonBean<Map<String, Object>> generatePerformanceReport(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> report = performanceAnalysisService.generatePerformanceReport(params);
            result.setCode(1);
            result.setMsg("生成成功");
            result.setData(report);
        } catch (ServiceException ex) {
            log.error("生成绩效报告失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("生成绩效报告异常", e);
            result.setCode(0);
            result.setMsg("生成失败：" + e.getMessage());
        }
        return result;
    }
}

