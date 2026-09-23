package com.management.accountant.controller.eps;

import com.management.accountant.util.MyJsonBean;
import com.management.accountant.service.eps.EpsBudgetAnalysisService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 预算分析控制器
 * 
 * @author 华博云
 * @version 3.0.0
 */
@Slf4j
@Api(tags = "预算分析管理")
@RestController
@RequestMapping("/eps/budget-analysis")
@Validated
public class EpsBudgetAnalysisController {

    @Autowired
    private EpsBudgetAnalysisService budgetAnalysisService;

    /**
     * 获取预算执行分析
     */
    @ApiOperation("获取预算执行分析")
    @GetMapping("/execution")
    public MyJsonBean<Map<String, Object>> getBudgetExecutionAnalysis(
            @ApiParam("版本ID") @RequestParam @NotNull Long versionId,
            @ApiParam("组织ID") @RequestParam(required = false) Long organizationId,
            @ApiParam("科目ID") @RequestParam(required = false) Long subjectId,
            @ApiParam("分析期间") @RequestParam(required = false) String analysisPeriod,
            @ApiParam("分析维度") @RequestParam(defaultValue = "MONTH") String analysisDimension) {
        try {
            Map<String, Object> result = budgetAnalysisService.getBudgetExecutionAnalysis(
                    versionId, organizationId, subjectId, analysisPeriod, analysisDimension);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取预算执行分析失败", e);
            return MyJsonBean.error("分析失败：" + e.getMessage());
        }
    }

    /**
     * 获取预算差异分析
     */
    @ApiOperation("获取预算差异分析")
    @GetMapping("/variance")
    public MyJsonBean<Map<String, Object>> getBudgetVarianceAnalysis(
            @ApiParam("版本ID") @RequestParam @NotNull Long versionId,
            @ApiParam("对比版本ID") @RequestParam(required = false) Long compareVersionId,
            @ApiParam("组织ID") @RequestParam(required = false) Long organizationId,
            @ApiParam("科目ID") @RequestParam(required = false) Long subjectId,
            @ApiParam("差异类型") @RequestParam(defaultValue = "AMOUNT") String varianceType) {
        try {
            Map<String, Object> result = budgetAnalysisService.getBudgetVarianceAnalysis(
                    versionId, compareVersionId, organizationId, subjectId, varianceType);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取预算差异分析失败", e);
            return MyJsonBean.error("分析失败：" + e.getMessage());
        }
    }

    /**
     * 获取预算趋势分析
     */
    @ApiOperation("获取预算趋势分析")
    @GetMapping("/trend")
    public MyJsonBean<Map<String, Object>> getBudgetTrendAnalysis(
            @ApiParam("版本ID") @RequestParam @NotNull Long versionId,
            @ApiParam("组织ID") @RequestParam(required = false) Long organizationId,
            @ApiParam("科目ID") @RequestParam(required = false) Long subjectId,
            @ApiParam("趋势周期") @RequestParam(defaultValue = "12") Integer trendPeriods,
            @ApiParam("预测周期") @RequestParam(defaultValue = "3") Integer forecastPeriods) {
        try {
            Map<String, Object> result = budgetAnalysisService.getBudgetTrendAnalysis(
                    versionId, organizationId, subjectId, trendPeriods, forecastPeriods);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取预算趋势分析失败", e);
            return MyJsonBean.error("分析失败：" + e.getMessage());
        }
    }

    /**
     * 获取预算结构分析
     */
    @ApiOperation("获取预算结构分析")
    @GetMapping("/structure")
    public MyJsonBean<Map<String, Object>> getBudgetStructureAnalysis(
            @ApiParam("版本ID") @RequestParam @NotNull Long versionId,
            @ApiParam("分析维度") @RequestParam(defaultValue = "SUBJECT") String analysisDimension,
            @ApiParam("组织ID") @RequestParam(required = false) Long organizationId,
            @ApiParam("层级深度") @RequestParam(defaultValue = "3") Integer hierarchyDepth) {
        try {
            Map<String, Object> result = budgetAnalysisService.getBudgetStructureAnalysis(
                    versionId, analysisDimension, organizationId, hierarchyDepth);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取预算结构分析失败", e);
            return MyJsonBean.error("分析失败：" + e.getMessage());
        }
    }

    /**
     * 获取预算对比分析
     */
    @ApiOperation("获取预算对比分析")
    @GetMapping("/comparison")
    public MyJsonBean<Map<String, Object>> getBudgetComparisonAnalysis(
            @ApiParam("源版本ID") @RequestParam @NotNull Long sourceVersionId,
            @ApiParam("目标版本ID") @RequestParam @NotNull Long targetVersionId,
            @ApiParam("对比维度") @RequestParam(defaultValue = "AMOUNT") String comparisonDimension,
            @ApiParam("组织ID") @RequestParam(required = false) Long organizationId,
            @ApiParam("科目ID") @RequestParam(required = false) Long subjectId) {
        try {
            Map<String, Object> result = budgetAnalysisService.getBudgetComparisonAnalysis(
                    sourceVersionId, targetVersionId, comparisonDimension, organizationId, subjectId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取预算对比分析失败", e);
            return MyJsonBean.error("分析失败：" + e.getMessage());
        }
    }

    /**
     * 获取预算完成率分析
     */
    @ApiOperation("获取预算完成率分析")
    @GetMapping("/completion-rate")
    public MyJsonBean<Map<String, Object>> getBudgetCompletionRateAnalysis(
            @ApiParam("版本ID") @RequestParam @NotNull Long versionId,
            @ApiParam("组织ID") @RequestParam(required = false) Long organizationId,
            @ApiParam("科目ID") @RequestParam(required = false) Long subjectId,
            @ApiParam("统计周期") @RequestParam(defaultValue = "MONTH") String statisticsPeriod) {
        try {
            Map<String, Object> result = budgetAnalysisService.getBudgetCompletionRateAnalysis(
                    versionId, organizationId, subjectId, statisticsPeriod);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取预算完成率分析失败", e);
            return MyJsonBean.error("分析失败：" + e.getMessage());
        }
    }

    /**
     * 获取预算排名分析
     */
    @ApiOperation("获取预算排名分析")
    @GetMapping("/ranking")
    @SuppressWarnings("unchecked")
    public MyJsonBean<Map<String, Object>> getBudgetRankingAnalysis(
            @ApiParam("版本ID") @RequestParam @NotNull Long versionId,
            @ApiParam("排名维度") @RequestParam(defaultValue = "ORGANIZATION") String rankingDimension,
            @ApiParam("排名指标") @RequestParam(defaultValue = "BUDGET_AMOUNT") String rankingMetric,
            @ApiParam("排名数量") @RequestParam(defaultValue = "10") Integer rankingLimit) {
        try {
            Map<String, Object> result = budgetAnalysisService.getBudgetRankingAnalysis(
                    versionId, rankingDimension, rankingMetric, rankingLimit);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取预算排名分析失败", e);
            return MyJsonBean.error("分析失败：" + e.getMessage());
        }
    }

    /**
     * 获取预算异常分析
     */
    @ApiOperation("获取预算异常分析")
    @GetMapping("/anomaly")
    public MyJsonBean<Map<String, Object>> getBudgetAnomalyAnalysis(
            @ApiParam("版本ID") @RequestParam @NotNull Long versionId,
            @ApiParam("异常类型") @RequestParam(defaultValue = "ALL") String anomalyType,
            @ApiParam("异常阈值") @RequestParam(defaultValue = "0.2") Double anomalyThreshold,
            @ApiParam("组织ID") @RequestParam(required = false) Long organizationId) {
        try {
            Map<String, Object> result = budgetAnalysisService.getBudgetAnomalyAnalysis(
                    versionId, anomalyType, anomalyThreshold, organizationId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取预算异常分析失败", e);
            return MyJsonBean.error("分析失败：" + e.getMessage());
        }
    }

    /**
     * 生成预算分析报表
     */
    @ApiOperation("生成预算分析报表")
    @PostMapping("/report")
    public MyJsonBean<Map<String, Object>> generateBudgetAnalysisReport(
            @ApiParam("报表参数") @RequestBody Map<String, Object> reportParams) {
        try {
            Map<String, Object> result = budgetAnalysisService.generateBudgetAnalysisReport(reportParams);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("生成预算分析报表失败", e);
            return MyJsonBean.error("生成失败：" + e.getMessage());
        }
    }

    /**
     * 导出预算分析数据
     */
    @ApiOperation("导出预算分析数据")
    @PostMapping("/export")
    public MyJsonBean<Map<String, Object>> exportBudgetAnalysisData(
            @ApiParam("导出参数") @RequestBody Map<String, Object> exportParams) {
        try {
            Map<String, Object> result = budgetAnalysisService.exportBudgetAnalysisData(exportParams);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("导出预算分析数据失败", e);
            return MyJsonBean.error("导出失败：" + e.getMessage());
        }
    }

    /**
     * 获取预算分析仪表板
     */
    @ApiOperation("获取预算分析仪表板")
    @GetMapping("/dashboard")
    public MyJsonBean<Map<String, Object>> getBudgetAnalysisDashboard(
            @ApiParam("版本ID") @RequestParam @NotNull Long versionId,
            @ApiParam("组织ID") @RequestParam(required = false) Long organizationId,
            @ApiParam("仪表板类型") @RequestParam(defaultValue = "OVERVIEW") String dashboardType) {
        try {
            Map<String, Object> result = budgetAnalysisService.getBudgetAnalysisDashboard(
                    versionId, organizationId, dashboardType);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取预算分析仪表板失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 获取预算分析图表数据
     */
    @ApiOperation("获取预算分析图表数据")
    @GetMapping("/chart")
    public MyJsonBean<Map<String, Object>> getBudgetAnalysisChartData(
            @ApiParam("版本ID") @RequestParam @NotNull Long versionId,
            @ApiParam("图表类型") @RequestParam @NotNull String chartType,
            @ApiParam("图表参数") @RequestParam(required = false) String chartParams) {
        try {
            Map<String, Object> result = budgetAnalysisService.getBudgetAnalysisChartData(
                    versionId, chartType, chartParams);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取预算分析图表数据失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 保存预算分析配置
     */
    @ApiOperation("保存预算分析配置")
    @PostMapping("/config")
    public MyJsonBean<Boolean> saveBudgetAnalysisConfig(
            @ApiParam("分析配置") @RequestBody Map<String, Object> analysisConfig) {
        try {
            boolean result = budgetAnalysisService.saveBudgetAnalysisConfig(analysisConfig);
            if (result) {
                return MyJsonBean.success("保存成功", true);
            } else {
                return MyJsonBean.error("保存失败");
            }
        } catch (Exception e) {
            log.error("保存预算分析配置失败", e);
            return MyJsonBean.error("保存失败：" + e.getMessage());
        }
    }

    /**
     * 获取预算分析配置
     */
    @ApiOperation("获取预算分析配置")
    @GetMapping("/config")
    public MyJsonBean<Map<String, Object>> getBudgetAnalysisConfig(
            @ApiParam("版本ID") @RequestParam @NotNull Long versionId,
            @ApiParam("配置类型") @RequestParam(defaultValue = "DEFAULT") String configType) {
        try {
            Map<String, Object> result = budgetAnalysisService.getBudgetAnalysisConfig(versionId, configType);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取预算分析配置失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 获取预算分析模板
     */
    @ApiOperation("获取预算分析模板")
    @GetMapping("/template")
    public MyJsonBean<List<Map<String, Object>>> getBudgetAnalysisTemplates(
            @ApiParam("模板类型") @RequestParam(defaultValue = "ALL") String templateType) {
        try {
            List<Map<String, Object>> result = budgetAnalysisService.getBudgetAnalysisTemplates(templateType);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取预算分析模板失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 应用预算分析模板
     */
    @ApiOperation("应用预算分析模板")
    @PostMapping("/template/apply")
    public MyJsonBean<Map<String, Object>> applyBudgetAnalysisTemplate(
            @ApiParam("模板应用参数") @RequestBody Map<String, Object> templateParams) {
        try {
            Map<String, Object> result = budgetAnalysisService.applyBudgetAnalysisTemplate(templateParams);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("应用预算分析模板失败", e);
            return MyJsonBean.error("应用失败：" + e.getMessage());
        }
    }

    /**
     * 获取预算分析洞察
     */
    @ApiOperation("获取预算分析洞察")
    @GetMapping("/insights")
    public MyJsonBean<Map<String, Object>> getBudgetAnalysisInsights(
            @ApiParam("版本ID") @RequestParam @NotNull Long versionId,
            @ApiParam("洞察类型") @RequestParam(defaultValue = "AUTO") String insightType,
            @ApiParam("组织ID") @RequestParam(required = false) Long organizationId) {
        try {
            Map<String, Object> result = budgetAnalysisService.getBudgetAnalysisInsights(
                    versionId, insightType, organizationId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取预算分析洞察失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 获取预算分析建议
     */
    @ApiOperation("获取预算分析建议")
    @GetMapping("/recommendations")
    public MyJsonBean<List<Map<String, Object>>> getBudgetAnalysisRecommendations(
            @ApiParam("版本ID") @RequestParam @NotNull Long versionId,
            @ApiParam("建议类型") @RequestParam(defaultValue = "OPTIMIZATION") String recommendationType,
            @ApiParam("组织ID") @RequestParam(required = false) Long organizationId) {
        try {
            List<Map<String, Object>> result = budgetAnalysisService.getBudgetAnalysisRecommendations(
                    versionId, recommendationType, organizationId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取预算分析建议失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 执行自定义分析
     */
    @ApiOperation("执行自定义分析")
    @PostMapping("/custom")
    public MyJsonBean<Map<String, Object>> executeCustomAnalysis(
            @ApiParam("自定义分析参数") @RequestBody Map<String, Object> customParams) {
        try {
            Map<String, Object> result = budgetAnalysisService.executeCustomAnalysis(customParams);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("执行自定义分析失败", e);
            return MyJsonBean.error("执行失败：" + e.getMessage());
        }
    }

    /**
     * 获取预算分析历史
     */
    @ApiOperation("获取预算分析历史")
    @GetMapping("/history")
    public MyJsonBean<List<Map<String, Object>>> getBudgetAnalysisHistory(
            @ApiParam("版本ID") @RequestParam @NotNull Long versionId,
            @ApiParam("分析类型") @RequestParam(required = false) String analysisType,
            @ApiParam("开始日期") @RequestParam(required = false) String startDate,
            @ApiParam("结束日期") @RequestParam(required = false) String endDate) {
        try {
            List<Map<String, Object>> result = budgetAnalysisService.getBudgetAnalysisHistory(
                    versionId, analysisType, startDate, endDate);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取预算分析历史失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 刷新预算分析缓存
     */
    @ApiOperation("刷新预算分析缓存")
    @PostMapping("/refresh-cache")
    public MyJsonBean<Boolean> refreshBudgetAnalysisCache(
            @ApiParam("版本ID") @RequestParam @NotNull Long versionId,
            @ApiParam("缓存类型") @RequestParam(defaultValue = "ALL") String cacheType) {
        try {
            boolean result = budgetAnalysisService.refreshBudgetAnalysisCache(versionId, cacheType);
            if (result) {
                return MyJsonBean.success("刷新成功", true);
            } else {
                return MyJsonBean.error("刷新失败");
            }
        } catch (Exception e) {
            log.error("刷新预算分析缓存失败", e);
            return MyJsonBean.error("刷新失败：" + e.getMessage());
        }
    }
}
