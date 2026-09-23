package com.huabo.cybermonitor.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.huabo.cybermonitor.entity.IncomeStatementData;
import com.huabo.cybermonitor.service.IIncomeStatementDataService;
import com.huabo.cybermonitor.util.R;
import com.huabo.cybermonitor.vo.IncomeStatementDataQueryVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;



/**
 * 利润表数据控制器
 * 
 * @author 华博云AI助手
 * @since 2024-12-12
 */
@Tag(name="利润表数据管理",description="利润表数据管理")
@RestController
@RequestMapping("/v1/supervision/financial/income-statement")
public class IncomeStatementDataController {

	private static final Logger log = LoggerFactory.getLogger(IncomeStatementDataController.class);

    @Autowired
    private IIncomeStatementDataService incomeStatementDataService;

    // ==================== 基础CRUD操作 ====================

    @Operation(summary = "分页查询利润表数据列表")
    @PostMapping("/list")
    public R<Map<String, Object>> list(@RequestBody IncomeStatementDataQueryVO queryVO) {
        try {
            Map<String, Object> result = incomeStatementDataService.selectIncomeStatementDataList(queryVO);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询利润表数据列表失败", e);
            return R.fail("查询利润表数据列表失败: " + e.getMessage());
        }
    }

    @Operation(summary = "根据ID查询利润表数据详情")
    @GetMapping("/{incomeStatementId}")
    public R<IncomeStatementData> getById(@Parameter(description="利润表ID") @PathVariable String incomeStatementId) {
        try {
            IncomeStatementData data = incomeStatementDataService.selectIncomeStatementDataById(incomeStatementId);
            return R.success(data);
        } catch (Exception e) {
            log.error("查询利润表数据详情失败: {}", incomeStatementId, e);
            return R.fail("查询利润表数据详情失败: " + e.getMessage());
        }
    }

    @Operation(summary = "新增利润表数据")
    @PostMapping("/add")
    public R<Boolean> add(@RequestBody IncomeStatementData incomeStatementData) {
        try {
            boolean result = incomeStatementDataService.insertIncomeStatementData(incomeStatementData);
            return result ? R.success(true, "新增利润表数据成功") : R.fail("新增利润表数据失败");
        } catch (Exception e) {
            log.error("新增利润表数据失败", e);
            return R.fail("新增利润表数据失败: " + e.getMessage());
        }
    }

    @Operation(summary = "修改利润表数据")
    @PostMapping("/update")
    public R<Boolean> update(@RequestBody IncomeStatementData incomeStatementData) {
        try {
            boolean result = incomeStatementDataService.updateIncomeStatementData(incomeStatementData);
            return result ? R.success(true, "修改利润表数据成功") : R.fail("修改利润表数据失败");
        } catch (Exception e) {
            log.error("修改利润表数据失败", e);
            return R.fail("修改利润表数据失败: " + e.getMessage());
        }
    }

    @Operation(summary = "删除利润表数据")
    @DeleteMapping("/{incomeStatementId}")
    public R<Boolean> delete(@Parameter(description="利润表ID") @PathVariable String incomeStatementId) {
        try {
            boolean result = incomeStatementDataService.deleteIncomeStatementDataById(incomeStatementId);
            return result ? R.success(true, "删除利润表数据成功") : R.fail("删除利润表数据失败");
        } catch (Exception e) {
            log.error("删除利润表数据失败: {}", incomeStatementId, e);
            return R.fail("删除利润表数据失败: " + e.getMessage());
        }
    }

    // ==================== 盈利能力分析接口 ====================

    @Operation(summary = "盈利能力综合分析")
    @PostMapping("/analyze/profitability")
    public R<Map<String, Object>> analyzeProfitability(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = incomeStatementDataService.analyzeProfitability(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("盈利能力分析失败: {}", enterpriseId, e);
            return R.fail("盈利能力分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "收入结构分析")
    @PostMapping("/analyze/revenue-structure")
    public R<Map<String, Object>> analyzeRevenueStructure(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = incomeStatementDataService.analyzeRevenueStructure(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("收入结构分析失败: {}", enterpriseId, e);
            return R.fail("收入结构分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "成本结构分析")
    @PostMapping("/analyze/cost-structure")
    public R<Map<String, Object>> analyzeCostStructure(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = incomeStatementDataService.analyzeCostStructure(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("成本结构分析失败: {}", enterpriseId, e);
            return R.fail("成本结构分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "费用结构分析")
    @PostMapping("/analyze/expense-structure")
    public R<Map<String, Object>> analyzeExpenseStructure(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = incomeStatementDataService.analyzeExpenseStructure(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("费用结构分析失败: {}", enterpriseId, e);
            return R.fail("费用结构分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "利润结构分析")
    @PostMapping("/analyze/profit-structure")
    public R<Map<String, Object>> analyzeProfitStructure(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = incomeStatementDataService.analyzeProfitStructure(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("利润结构分析失败: {}", enterpriseId, e);
            return R.fail("利润结构分析失败: " + e.getMessage());
        }
    }

    // ==================== 收入分析接口 ====================

    @Operation(summary = "营业收入综合分析")
    @PostMapping("/analyze/operating-revenue")
    public R<Map<String, Object>> analyzeOperatingRevenue(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = incomeStatementDataService.analyzeOperatingRevenue(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("营业收入分析失败: {}", enterpriseId, e);
            return R.fail("营业收入分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "收入增长分析")
    @PostMapping("/analyze/revenue-growth")
    public R<Map<String, Object>> analyzeRevenueGrowth(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = incomeStatementDataService.analyzeRevenueGrowth(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("收入增长分析失败: {}", enterpriseId, e);
            return R.fail("收入增长分析失败: " + e.getMessage());
        }
    }

    // ==================== 成本费用分析接口 ====================

    @Operation(summary = "营业成本分析")
    @PostMapping("/analyze/operating-cost")
    public R<Map<String, Object>> analyzeOperatingCost(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = incomeStatementDataService.analyzeOperatingCost(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("营业成本分析失败: {}", enterpriseId, e);
            return R.fail("营业成本分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "期间费用分析")
    @PostMapping("/analyze/period-expense")
    public R<Map<String, Object>> analyzePeriodExpense(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = incomeStatementDataService.analyzePeriodExpense(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("期间费用分析失败: {}", enterpriseId, e);
            return R.fail("期间费用分析失败: " + e.getMessage());
        }
    }

    // ==================== 利润分析接口 ====================

    @Operation(summary = "营业利润分析")
    @PostMapping("/analyze/operating-profit")
    public R<Map<String, Object>> analyzeOperatingProfit(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = incomeStatementDataService.analyzeOperatingProfit(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("营业利润分析失败: {}", enterpriseId, e);
            return R.fail("营业利润分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "净利润分析")
    @PostMapping("/analyze/net-profit")
    public R<Map<String, Object>> analyzeNetProfit(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = incomeStatementDataService.analyzeNetProfit(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("净利润分析失败: {}", enterpriseId, e);
            return R.fail("净利润分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "利润质量分析")
    @PostMapping("/analyze/profit-quality")
    public R<Map<String, Object>> analyzeProfitQuality(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = incomeStatementDataService.analyzeProfitQuality(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("利润质量分析失败: {}", enterpriseId, e);
            return R.fail("利润质量分析失败: " + e.getMessage());
        }
    }

    // ==================== 财务比率计算接口 ====================

    @Operation(summary = "计算财务比率")
    @PostMapping("/calculate/financial-ratios")
    public R<IncomeStatementData> calculateFinancialRatios(@Parameter(description="利润表ID") @RequestParam String incomeStatementId) {
        try {
            IncomeStatementData result = incomeStatementDataService.calculateFinancialRatios(incomeStatementId);
            return R.success(result);
        } catch (Exception e) {
            log.error("计算财务比率失败: {}", incomeStatementId, e);
            return R.fail("计算财务比率失败: " + e.getMessage());
        }
    }

    @Operation(summary = "批量计算财务比率")
    @PostMapping("/batch/calculate-ratios")
    public R<Map<String, Object>> batchCalculateFinancialRatios(
            @RequestBody List<String> enterpriseIds,
            @Parameter(description="报表年度") @RequestParam Integer reportYear,
            @Parameter(description="报表期间") @RequestParam Integer reportPeriod) {
        try {
            Map<String, Object> result = incomeStatementDataService.batchCalculateFinancialRatios(enterpriseIds, reportYear, reportPeriod);
            return R.success(result);
        } catch (Exception e) {
            log.error("批量计算财务比率失败", e);
            return R.fail("批量计算财务比率失败: " + e.getMessage());
        }
    }

    // ==================== 趋势分析接口 ====================

    @Operation(summary = "收入趋势分析")
    @PostMapping("/analyze/revenue-trend")
    public R<Map<String, Object>> analyzeRevenueTrend(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = incomeStatementDataService.analyzeRevenueTrend(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("收入趋势分析失败: {}", enterpriseId, e);
            return R.fail("收入趋势分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "利润趋势分析")
    @PostMapping("/analyze/profit-trend")
    public R<Map<String, Object>> analyzeProfitTrend(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = incomeStatementDataService.analyzeProfitTrend(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("利润趋势分析失败: {}", enterpriseId, e);
            return R.fail("利润趋势分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "盈利能力趋势分析")
    @PostMapping("/analyze/profitability-trend")
    public R<Map<String, Object>> analyzeProfitabilityTrend(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = incomeStatementDataService.analyzeProfitabilityTrend(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("盈利能力趋势分析失败: {}", enterpriseId, e);
            return R.fail("盈利能力趋势分析失败: " + e.getMessage());
        }
    }

    // ==================== 统计分析接口 ====================

    @Operation(summary = "收入规模分布统计")
    @GetMapping("/statistics/revenue-scale")
    public R<List<Map<String, Object>>> getRevenueScaleDistributionStatistics(
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            List<Map<String, Object>> result = incomeStatementDataService.getRevenueScaleDistributionStatistics(startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("收入规模分布统计失败", e);
            return R.fail("收入规模分布统计失败: " + e.getMessage());
        }
    }

    @Operation(summary = "利润水平分布统计")
    @GetMapping("/statistics/profit-level")
    public R<List<Map<String, Object>>> getProfitLevelDistributionStatistics(
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            List<Map<String, Object>> result = incomeStatementDataService.getProfitLevelDistributionStatistics(startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("利润水平分布统计失败", e);
            return R.fail("利润水平分布统计失败: " + e.getMessage());
        }
    }

    @Operation(summary = "利润表统计概览")
    @GetMapping("/statistics/overview")
    public R<Map<String, Object>> getIncomeStatementStatisticsOverview() {
        try {
            Map<String, Object> result = incomeStatementDataService.getIncomeStatementStatisticsOverview();
            return R.success(result);
        } catch (Exception e) {
            log.error("获取利润表统计概览失败", e);
            return R.fail("获取利润表统计概览失败: " + e.getMessage());
        }
    }

    // ==================== 导出功能接口 ====================

    @Operation(summary = "导出利润表数据列表")
    @PostMapping("/export/list")
    public R<List<Map<String, Object>>> exportIncomeStatementDataList(@RequestBody IncomeStatementDataQueryVO queryVO) {
        try {
            List<Map<String, Object>> result = incomeStatementDataService.exportIncomeStatementDataList(queryVO);
            return R.success(result);
        } catch (Exception e) {
            log.error("导出利润表数据列表失败", e);
            return R.fail("导出利润表数据列表失败: " + e.getMessage());
        }
    }

    @Operation(summary = "导出盈利能力分析报告")
    @PostMapping("/export/profitability-report")
    public R<Map<String, Object>> exportProfitabilityAnalysisReport(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = incomeStatementDataService.exportProfitabilityAnalysisReport(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("导出盈利能力分析报告失败: {}", enterpriseId, e);
            return R.fail("导出盈利能力分析报告失败: " + e.getMessage());
        }
    }

}
