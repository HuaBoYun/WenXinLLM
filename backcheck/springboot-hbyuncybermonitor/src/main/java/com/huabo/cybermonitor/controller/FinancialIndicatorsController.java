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

import com.huabo.cybermonitor.entity.FinancialIndicators;
import com.huabo.cybermonitor.service.IFinancialIndicatorsService;
import com.huabo.cybermonitor.util.R;
import com.huabo.cybermonitor.vo.FinancialIndicatorsQueryVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;



/**
 * 财务指标控制器
 * 
 * @author 华博云AI助手
 * @since 2024-12-12
 */
@Tag(name="财务指标管理",description="财务指标管理") @RestController
@RequestMapping("/v1/supervision/financial/indicators")
public class FinancialIndicatorsController {

	private static final Logger log = LoggerFactory.getLogger(FinancialIndicatorsController.class);

    @Autowired
    private IFinancialIndicatorsService financialIndicatorsService;

    // ==================== 基础CRUD操作 ====================

    @Operation(summary = "分页查询财务指标列表")
    @PostMapping("/list")
    public R<Map<String, Object>> list(@RequestBody FinancialIndicatorsQueryVO queryVO) {
        try {
            Map<String, Object> result = financialIndicatorsService.selectFinancialIndicatorsList(queryVO);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询财务指标列表失败", e);
            return R.fail("查询财务指标列表失败: " + e.getMessage());
        }
    }

    @Operation(summary = "根据ID查询财务指标详情")
    @GetMapping("/{indicatorId}")
    public R<FinancialIndicators> getById(@Parameter(description="财务指标ID") @PathVariable String indicatorId) {
        try {
            FinancialIndicators indicators = financialIndicatorsService.selectFinancialIndicatorsById(indicatorId);
            return R.success(indicators);
        } catch (Exception e) {
            log.error("查询财务指标详情失败: {}", indicatorId, e);
            return R.fail("查询财务指标详情失败: " + e.getMessage());
        }
    }

    @Operation(summary = "新增财务指标")
    @PostMapping("/add")
    public R<Boolean> add(@RequestBody FinancialIndicators financialIndicators) {
        try {
            boolean result = financialIndicatorsService.insertFinancialIndicators(financialIndicators);
            return result ? R.success(true, "新增财务指标成功") : R.fail("新增财务指标失败");
        } catch (Exception e) {
            log.error("新增财务指标失败", e);
            return R.fail("新增财务指标失败: " + e.getMessage());
        }
    }

    @Operation(summary = "修改财务指标")
    @PostMapping("/update")
    public R<Boolean> update(@RequestBody FinancialIndicators financialIndicators) {
        try {
            boolean result = financialIndicatorsService.updateFinancialIndicators(financialIndicators);
            return result ? R.success(true, "修改财务指标成功") : R.fail("修改财务指标失败");
        } catch (Exception e) {
            log.error("修改财务指标失败", e);
            return R.fail("修改财务指标失败: " + e.getMessage());
        }
    }

    @Operation(summary = "删除财务指标")
    @DeleteMapping("/{indicatorId}")
    public R<Boolean> delete(@Parameter(description="财务指标ID") @PathVariable String indicatorId) {
        try {
            boolean result = financialIndicatorsService.deleteFinancialIndicatorsById(indicatorId);
            return result ? R.success(true, "删除财务指标成功") : R.fail("删除财务指标失败");
        } catch (Exception e) {
            log.error("删除财务指标失败: {}", indicatorId, e);
            return R.fail("删除财务指标失败: " + e.getMessage());
        }
    }

    // ==================== 财务指标计算接口 ====================

    @Operation(summary = "自动计算财务指标")
    @PostMapping("/calculate/auto")
    public R<FinancialIndicators> autoCalculateFinancialIndicators(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="报表年度") @RequestParam Integer reportYear,
            @Parameter(description="报表期间") @RequestParam Integer reportPeriod) {
        try {
            FinancialIndicators result = financialIndicatorsService.calculateFinancialIndicators(enterpriseId, reportYear, reportPeriod);
            return R.success(result);
        } catch (Exception e) {
            log.error("自动计算财务指标失败: {}", enterpriseId, e);
            return R.fail("自动计算财务指标失败: " + e.getMessage());
        }
    }

    @Operation(summary = "批量计算财务指标")
    @PostMapping("/calculate/batch")
    public R<Map<String, Object>> batchCalculateFinancialIndicators(
            @RequestBody List<String> enterpriseIds,
            @Parameter(description="报表年度") @RequestParam Integer reportYear,
            @Parameter(description="报表期间") @RequestParam Integer reportPeriod) {
        try {
            Map<String, Object> result = financialIndicatorsService.batchCalculateFinancialIndicators(enterpriseIds, reportYear, reportPeriod);
            return R.success(result);
        } catch (Exception e) {
            log.error("批量计算财务指标失败", e);
            return R.fail("批量计算财务指标失败: " + e.getMessage());
        }
    }

    @Operation(summary = "重新计算财务指标")
    @PostMapping("/recalculate")
    public R<Boolean> recalculateFinancialIndicators(@Parameter(description="财务指标ID") @RequestParam String indicatorId) {
        try {
            boolean result = financialIndicatorsService.recalculateFinancialIndicators(indicatorId);
            return result ? R.success(true, "重新计算财务指标成功") : R.fail("重新计算财务指标失败");
        } catch (Exception e) {
            log.error("重新计算财务指标失败: {}", indicatorId, e);
            return R.fail("重新计算财务指标失败: " + e.getMessage());
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
            Map<String, Object> result = financialIndicatorsService.analyzeProfitability(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("盈利能力分析失败: {}", enterpriseId, e);
            return R.fail("盈利能力分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "ROE分析")
    @PostMapping("/analyze/roe")
    public R<Map<String, Object>> analyzeROE(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = financialIndicatorsService.analyzeROE(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("ROE分析失败: {}", enterpriseId, e);
            return R.fail("ROE分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "ROA分析")
    @PostMapping("/analyze/roa")
    public R<Map<String, Object>> analyzeROA(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = financialIndicatorsService.analyzeROA(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("ROA分析失败: {}", enterpriseId, e);
            return R.fail("ROA分析失败: " + e.getMessage());
        }
    }

    // ==================== 偿债能力分析接口 ====================

    @Operation(summary = "偿债能力综合分析")
    @PostMapping("/analyze/solvency")
    public R<Map<String, Object>> analyzeSolvency(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = financialIndicatorsService.analyzeSolvency(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("偿债能力分析失败: {}", enterpriseId, e);
            return R.fail("偿债能力分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "短期偿债能力分析")
    @PostMapping("/analyze/short-term-solvency")
    public R<Map<String, Object>> analyzeShortTermSolvency(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = financialIndicatorsService.analyzeShortTermSolvency(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("短期偿债能力分析失败: {}", enterpriseId, e);
            return R.fail("短期偿债能力分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "长期偿债能力分析")
    @PostMapping("/analyze/long-term-solvency")
    public R<Map<String, Object>> analyzeLongTermSolvency(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = financialIndicatorsService.analyzeLongTermSolvency(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("长期偿债能力分析失败: {}", enterpriseId, e);
            return R.fail("长期偿债能力分析失败: " + e.getMessage());
        }
    }

    // ==================== 营运能力分析接口 ====================

    @Operation(summary = "营运能力综合分析")
    @PostMapping("/analyze/operating-ability")
    public R<Map<String, Object>> analyzeOperatingAbility(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = financialIndicatorsService.analyzeOperatingAbility(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("营运能力分析失败: {}", enterpriseId, e);
            return R.fail("营运能力分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "资产周转能力分析")
    @PostMapping("/analyze/asset-turnover")
    public R<Map<String, Object>> analyzeAssetTurnover(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = financialIndicatorsService.analyzeAssetTurnover(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("资产周转能力分析失败: {}", enterpriseId, e);
            return R.fail("资产周转能力分析失败: " + e.getMessage());
        }
    }

    // ==================== 发展能力分析接口 ====================

    @Operation(summary = "发展能力综合分析")
    @PostMapping("/analyze/development-ability")
    public R<Map<String, Object>> analyzeDevelopmentAbility(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = financialIndicatorsService.analyzeDevelopmentAbility(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("发展能力分析失败: {}", enterpriseId, e);
            return R.fail("发展能力分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "增长率分析")
    @PostMapping("/analyze/growth-rate")
    public R<Map<String, Object>> analyzeGrowthRate(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = financialIndicatorsService.analyzeGrowthRate(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("增长率分析失败: {}", enterpriseId, e);
            return R.fail("增长率分析失败: " + e.getMessage());
        }
    }

    // ==================== 现金流量分析接口 ====================

    @Operation(summary = "现金流量指标分析")
    @PostMapping("/analyze/cash-flow")
    public R<Map<String, Object>> analyzeCashFlow(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = financialIndicatorsService.analyzeCashFlow(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("现金流量指标分析失败: {}", enterpriseId, e);
            return R.fail("现金流量指标分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "现金流量质量分析")
    @PostMapping("/analyze/cash-flow-quality")
    public R<Map<String, Object>> analyzeCashFlowQuality(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = financialIndicatorsService.analyzeCashFlowQuality(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("现金流量质量分析失败: {}", enterpriseId, e);
            return R.fail("现金流量质量分析失败: " + e.getMessage());
        }
    }

    // ==================== 综合评价接口 ====================

    @Operation(summary = "财务综合评价")
    @PostMapping("/evaluate/comprehensive")
    public R<Map<String, Object>> comprehensiveFinancialEvaluation(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = financialIndicatorsService.evaluateComprehensiveFinancial(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("财务综合评价失败: {}", enterpriseId, e);
            return R.fail("财务综合评价失败: " + e.getMessage());
        }
    }

    @Operation(summary = "同业对比分析")
    @PostMapping("/compare/industry")
    public R<Map<String, Object>> industryComparison(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="行业代码") @RequestParam String industryCode,
            @Parameter(description="报表年度") @RequestParam Integer reportYear) {
        try {
            Map<String, Object> result = financialIndicatorsService.analyzeIndustryComparison(enterpriseId, industryCode, reportYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("同业对比分析失败: {}", enterpriseId, e);
            return R.fail("同业对比分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "历史趋势分析")
    @PostMapping("/analyze/historical-trend")
    public R<Map<String, Object>> historicalTrendAnalysis(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = financialIndicatorsService.analyzeHistoricalTrend(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("历史趋势分析失败: {}", enterpriseId, e);
            return R.fail("历史趋势分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "财务预警分析")
    @PostMapping("/warning/financial")
    public R<Map<String, Object>> financialWarningAnalysis(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="报表年度") @RequestParam Integer reportYear) {
        try {
            Map<String, Object> result = financialIndicatorsService.analyzeFinancialWarning(enterpriseId, reportYear, reportYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("财务预警分析失败: {}", enterpriseId, e);
            return R.fail("财务预警分析失败: " + e.getMessage());
        }
    }

    // ==================== 统计分析接口 ====================

    @Operation(summary = "财务指标分布统计")
    @GetMapping("/statistics/distribution")
    public R<List<Map<String, Object>>> getFinancialIndicatorsDistributionStatistics(
            @Parameter(description="指标类型") @RequestParam String indicatorType,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            List<Map<String, Object>> result = financialIndicatorsService.getIndicatorDistributionStatistics(indicatorType, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("财务指标分布统计失败", e);
            return R.fail("财务指标分布统计失败: " + e.getMessage());
        }
    }

    @Operation(summary = "财务指标排名统计")
    @GetMapping("/statistics/ranking")
    public R<List<Map<String, Object>>> getFinancialIndicatorsRankingStatistics(
            @Parameter(description="指标名称") @RequestParam String indicatorName,
            @Parameter(description="报表年度") @RequestParam Integer reportYear,
            @Parameter(description="排名数量") @RequestParam(defaultValue = "20") Integer topN) {
        try {
            List<Map<String, Object>> result = financialIndicatorsService.getIndustryRankingStatistics(indicatorName, reportYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("财务指标排名统计失败", e);
            return R.fail("财务指标排名统计失败: " + e.getMessage());
        }
    }

    @Operation(summary = "财务指标统计概览")
    @GetMapping("/statistics/overview")
    public R<Map<String, Object>> getFinancialIndicatorsStatisticsOverview() {
        try {
            Map<String, Object> result = financialIndicatorsService.getFinancialIndicatorsStatisticsOverview();
            return R.success(result);
        } catch (Exception e) {
            log.error("获取财务指标统计概览失败", e);
            return R.fail("获取财务指标统计概览失败: " + e.getMessage());
        }
    }

    // ==================== 导出功能接口 ====================

    @Operation(summary = "导出财务指标列表")
    @PostMapping("/export/list")
    public R<List<Map<String, Object>>> exportFinancialIndicatorsList(@RequestBody FinancialIndicatorsQueryVO queryVO) {
        try {
            List<Map<String, Object>> result = financialIndicatorsService.exportFinancialIndicatorsList(queryVO);
            return R.success(result);
        } catch (Exception e) {
            log.error("导出财务指标列表失败", e);
            return R.fail("导出财务指标列表失败: " + e.getMessage());
        }
    }

    @Operation(summary = "导出财务综合分析报告")
    @PostMapping("/export/comprehensive-report")
    public R<Map<String, Object>> exportComprehensiveAnalysisReport(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = financialIndicatorsService.exportFinancialAnalysisReport(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("导出财务综合分析报告失败: {}", enterpriseId, e);
            return R.fail("导出财务综合分析报告失败: " + e.getMessage());
        }
    }

    @Operation(summary = "导出同业对比报告")
    @PostMapping("/export/industry-comparison-report")
    public R<Map<String, Object>> exportIndustryComparisonReport(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="行业代码") @RequestParam String industryCode,
            @Parameter(description="报表年度") @RequestParam Integer reportYear) {
        try {
            Map<String, Object> result = financialIndicatorsService.exportIndustryComparisonReport(enterpriseId, industryCode, reportYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("导出同业对比报告失败: {}", enterpriseId, e);
            return R.fail("导出同业对比报告失败: " + e.getMessage());
        }
    }

}
