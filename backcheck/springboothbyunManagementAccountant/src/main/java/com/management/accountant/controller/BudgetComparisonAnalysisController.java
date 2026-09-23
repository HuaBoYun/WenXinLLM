package com.management.accountant.controller;

import com.management.accountant.exception.ServiceException;
import com.management.accountant.service.BudgetComparisonAnalysisService;
import com.management.accountant.util.MyJsonBean;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 预算对比分析Controller
 * 
 * @description 预算对比分析接口，支持同比、环比、预实对比等
 * @author AI Assistant
 * @date 2025-01-04
 */
@RestController
@Api(tags = {"NCV65全面预算-对比分析"})
@RequestMapping(value = "/accountant/budget/analysis/comparison")
@Slf4j
public class BudgetComparisonAnalysisController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetComparisonAnalysisService comparisonAnalysisService;

    /**
     * 执行对比分析
     */
    @Operation(summary = "执行对比分析")
    @ApiOperation("执行对比分析")
    @PostMapping("/execute")
    public MyJsonBean<Map<String, Object>> executeComparisonAnalysis(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> analysisResult = comparisonAnalysisService.executeComparisonAnalysis(params);
            result.setCode(1);
            result.setMsg("分析成功");
            result.setData(analysisResult);
        } catch (ServiceException ex) {
            log.error("执行对比分析失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("执行对比分析异常", e);
            result.setCode(0);
            result.setMsg("分析失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取对比图表数据
     */
    @Operation(summary = "获取对比图表数据")
    @ApiOperation("获取对比图表数据")
    @PostMapping("/chart")
    public MyJsonBean<Map<String, Object>> getComparisonChart(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> chartData = comparisonAnalysisService.getComparisonChart(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(chartData);
        } catch (Exception e) {
            log.error("获取对比图表数据异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 导出对比分析报告
     */
    @Operation(summary = "导出对比分析报告")
    @ApiOperation("导出对比分析报告")
    @PostMapping("/export")
    public MyJsonBean<Map<String, Object>> exportComparisonReport(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> exportResult = comparisonAnalysisService.exportComparisonReport(params);
            result.setCode(1);
            result.setMsg("导出成功");
            result.setData(exportResult);
        } catch (ServiceException ex) {
            log.error("导出对比分析报告失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("导出对比分析报告异常", e);
            result.setCode(0);
            result.setMsg("导出失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取对比分析统计数据
     */
    @Operation(summary = "获取对比分析统计数据")
    @ApiOperation("获取对比分析统计数据")
    @GetMapping("/stats")
    public MyJsonBean<Map<String, Object>> getStats() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> stats = comparisonAnalysisService.getComparisonStats();
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(stats);
        } catch (Exception e) {
            log.error("获取对比分析统计数据失败", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取对比分析图表数据
     */
    @Operation(summary = "获取对比分析图表数据")
    @ApiOperation("获取对比分析图表数据")
    @PostMapping("/chart-data")
    public MyJsonBean<Map<String, Object>> getChartData(@RequestBody(required = false) Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> chartData = comparisonAnalysisService.getComparisonChartData(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(chartData);
        } catch (Exception e) {
            log.error("获取对比分析图表数据失败", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取组织单元选项
     */
    @Operation(summary = "获取组织单元选项")
    @ApiOperation("获取组织单元选项")
    @GetMapping("/organizations")
    public MyJsonBean<List<Map<String, Object>>> getOrganizations() {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            List<Map<String, Object>> organizations = comparisonAnalysisService.getOrganizations();
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(organizations);
        } catch (Exception e) {
            log.error("获取组织单元选项失败", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取预算科目选项
     */
    @Operation(summary = "获取预算科目选项")
    @ApiOperation("获取预算科目选项")
    @GetMapping("/budget-accounts")
    public MyJsonBean<List<Map<String, Object>>> getBudgetAccounts() {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            List<Map<String, Object>> accounts = comparisonAnalysisService.getBudgetAccounts();
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(accounts);
        } catch (Exception e) {
            log.error("获取预算科目选项失败", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 更新对比分析
     */
    @Operation(summary = "更新对比分析")
    @ApiOperation("更新对比分析")
    @PostMapping("/update")
    public MyJsonBean<Map<String, Object>> updateComparison(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> updateResult = comparisonAnalysisService.updateComparison(params);
            result.setCode(1);
            result.setMsg("更新成功");
            result.setData(updateResult);
        } catch (ServiceException ex) {
            log.error("更新对比分析失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("更新对比分析异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 批量对比分析
     */
    @Operation(summary = "批量对比分析")
    @ApiOperation("批量对比分析")
    @PostMapping("/batch")
    public MyJsonBean<Map<String, Object>> batchComparison(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> batchResult = comparisonAnalysisService.batchComparison(params);
            result.setCode(1);
            result.setMsg("批量对比成功");
            result.setData(batchResult);
        } catch (ServiceException ex) {
            log.error("批量对比分析失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("批量对比分析异常", e);
            result.setCode(0);
            result.setMsg("批量对比失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 删除对比分析
     */
    @Operation(summary = "删除对比分析")
    @ApiOperation("删除对比分析")
    @PostMapping("/delete")
    public MyJsonBean<Map<String, Object>> deleteComparison(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> deleteResult = comparisonAnalysisService.deleteComparison(params);
            result.setCode(1);
            result.setMsg("删除成功");
            result.setData(deleteResult);
        } catch (ServiceException ex) {
            log.error("删除对比分析失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("删除对比分析异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }
}

