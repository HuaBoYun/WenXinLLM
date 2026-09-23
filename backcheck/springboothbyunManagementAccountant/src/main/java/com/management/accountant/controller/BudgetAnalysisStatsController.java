package com.management.accountant.controller;

import com.management.accountant.service.BudgetAnalysisStatsService;
import com.management.accountant.util.MyJsonBean;
import com.management.accountant.vo.result.BudgetAnalysisInsightVO;
import com.management.accountant.vo.result.BudgetAnalysisStatsVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * 预算分析统计Controller
 * 
 * @description 预算分析统计数据接口
 * @author AI Assistant
 * @date 2025-01-30
 */
@RestController
@Api(tags = {"NCV65全面预算-预算分析统计"})
@RequestMapping(value = "/accountant/budget/analysis")
@Slf4j
public class BudgetAnalysisStatsController {

    @Resource
    private BudgetAnalysisStatsService budgetAnalysisStatsService;

    /**
     * 获取预算分析统计数据
     */
    @Operation(summary = "获取预算分析统计数据")
    @ApiOperation("获取预算分析统计数据")
    @GetMapping("/stats")
    public MyJsonBean<BudgetAnalysisStatsVO> getAnalysisStats() {
        MyJsonBean<BudgetAnalysisStatsVO> result = new MyJsonBean<>();
        try {
            BudgetAnalysisStatsVO stats = budgetAnalysisStatsService.getAnalysisStats();
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(stats);
        } catch (Exception e) {
            log.error("获取预算分析统计数据失败", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取分析表格数据
     */
    @Operation(summary = "获取分析表格数据")
    @ApiOperation("获取分析表格数据")
    @GetMapping("/table-data")
    public MyJsonBean<List<Map<String, Object>>> getAnalysisTableData() {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            List<Map<String, Object>> tableData = budgetAnalysisStatsService.getAnalysisTableData();
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(tableData);
        } catch (Exception e) {
            log.error("获取分析表格数据失败", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取分析洞察
     */
    @Operation(summary = "获取分析洞察")
    @ApiOperation("获取分析洞察")
    @GetMapping("/insights")
    public MyJsonBean<List<BudgetAnalysisInsightVO>> getAnalysisInsights(
            @ApiParam(value = "限制数量", required = false) @RequestParam(required = false, defaultValue = "5") Integer limit) {
        MyJsonBean<List<BudgetAnalysisInsightVO>> result = new MyJsonBean<>();
        try {
            List<BudgetAnalysisInsightVO> insights = budgetAnalysisStatsService.getAnalysisInsights(limit);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(insights);
        } catch (Exception e) {
            log.error("获取分析洞察失败", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取组织列表
     */
    @Operation(summary = "获取组织列表")
    @ApiOperation("获取组织列表")
    @GetMapping("/organizations")
    public MyJsonBean<List<Map<String, Object>>> getOrganizations() {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            List<Map<String, Object>> organizations = budgetAnalysisStatsService.getOrganizations();
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(organizations);
        } catch (Exception e) {
            log.error("获取组织列表失败", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取预算科目列表
     */
    @Operation(summary = "获取预算科目列表")
    @ApiOperation("获取预算科目列表")
    @GetMapping("/budget-accounts")
    public MyJsonBean<List<Map<String, Object>>> getBudgetAccounts() {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            List<Map<String, Object>> accounts = budgetAnalysisStatsService.getBudgetAccounts();
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(accounts);
        } catch (Exception e) {
            log.error("获取预算科目列表失败", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取仪表盘统计数据")
    @ApiOperation("获取仪表盘统计数据")
    @GetMapping("/dashboard/stats")
    public MyJsonBean<Map<String, Object>> getDashboardStats() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> stats = budgetAnalysisStatsService.getDashboardStats();
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(stats);
        } catch (Exception e) {
            log.error("获取仪表盘统计数据失败", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取仪表盘数据")
    @ApiOperation("获取仪表盘数据")
    @GetMapping("/dashboard/data")
    public MyJsonBean<Map<String, Object>> getDashboardData() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> data = budgetAnalysisStatsService.getDashboardData();
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(data);
        } catch (Exception e) {
            log.error("获取仪表盘数据失败", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取仪表盘图表数据")
    @ApiOperation("获取仪表盘图表数据")
    @PostMapping("/dashboard/chart-data")
    public MyJsonBean<Map<String, Object>> getDashboardChartData(@RequestBody(required = false) Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> chartData = budgetAnalysisStatsService.getDashboardChartData(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(chartData);
        } catch (Exception e) {
            log.error("获取仪表盘图表数据失败", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "执行差异分析（仪表盘快捷操作）")
    @ApiOperation("执行差异分析（仪表盘快捷操作）")
    @PostMapping("/dashboard/variance-analysis")
    public MyJsonBean<List<Map<String, Object>>> runVarianceAnalysis(@RequestBody(required = false) Map<String, Object> params) {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            String timeRange = params != null ? (String) params.get("timeRange") : "MONTH";
            Object thresholdObj = params != null ? params.get("threshold") : null;
            double threshold = thresholdObj != null ? Double.parseDouble(thresholdObj.toString()) : 5.0;
            List<Map<String, Object>> data = budgetAnalysisStatsService.runVarianceAnalysis(timeRange, threshold);
            result.setCode(1);
            result.setMsg("分析成功");
            result.setData(data);
        } catch (Exception e) {
            log.error("执行差异分析失败", e);
            result.setCode(0);
            result.setMsg("分析失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "保存仪表盘预警设置")
    @ApiOperation("保存仪表盘预警设置")
    @PostMapping("/dashboard/alert-settings")
    public MyJsonBean<Void> saveAlertSettings(@RequestBody Map<String, Object> params) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            budgetAnalysisStatsService.saveDashboardAlertSettings(params);
            result.setCode(1);
            result.setMsg("保存成功");
        } catch (Exception e) {
            log.error("保存预警设置失败", e);
            result.setCode(0);
            result.setMsg("保存失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "保存仪表盘系统配置")
    @ApiOperation("保存仪表盘系统配置")
    @PostMapping("/dashboard/sys-settings")
    public MyJsonBean<Void> saveSysSettings(@RequestBody Map<String, Object> params) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            budgetAnalysisStatsService.saveDashboardSysSettings(params);
            result.setCode(1);
            result.setMsg("保存成功");
        } catch (Exception e) {
            log.error("保存系统配置失败", e);
            result.setCode(0);
            result.setMsg("保存失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "保存仪表盘布局配置")
    @ApiOperation("保存仪表盘布局配置")
    @PostMapping("/dashboard/layout")
    public MyJsonBean<Void> saveLayout(@RequestBody Map<String, Object> params) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            budgetAnalysisStatsService.saveDashboardLayout(params);
            result.setCode(1);
            result.setMsg("保存成功");
        } catch (Exception e) {
            log.error("保存布局配置失败", e);
            result.setCode(0);
            result.setMsg("保存失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取仪表盘布局配置")
    @ApiOperation("获取仪表盘布局配置")
    @GetMapping("/dashboard/layout")
    public MyJsonBean<Map<String, Object>> getLayout() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> layout = budgetAnalysisStatsService.getDashboardLayout();
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(layout);
        } catch (Exception e) {
            log.error("获取布局配置失败", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "快速创建分析")
    @ApiOperation("快速创建分析")
    @PostMapping("/dashboard/quick-analysis")
    public MyJsonBean<Map<String, Object>> quickAnalysis(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> data = budgetAnalysisStatsService.quickAnalysis(params);
            result.setCode(1);
            result.setMsg("分析成功");
            result.setData(data);
        } catch (Exception e) {
            log.error("快速分析失败", e);
            result.setCode(0);
            result.setMsg("分析失败：" + e.getMessage());
        }
        return result;
    }
}

