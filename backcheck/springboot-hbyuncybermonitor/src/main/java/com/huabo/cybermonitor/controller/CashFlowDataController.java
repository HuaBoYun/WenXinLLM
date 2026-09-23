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

import com.huabo.cybermonitor.entity.CashFlowData;
import com.huabo.cybermonitor.service.ICashFlowDataService;
import com.huabo.cybermonitor.util.R;
import com.huabo.cybermonitor.vo.CashFlowDataQueryVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;



/**
 * 现金流量表数据控制器
 * 
 * @author 华博云AI助手
 * @since 2024-12-12
 */
@Tag(name="现金流量表数据管理",description="现金流量表数据管理")
@RestController
@RequestMapping("/v1/supervision/financial/cash-flow")
public class CashFlowDataController {

	private static final Logger log = LoggerFactory.getLogger(CashFlowDataController.class);

    @Autowired
    private ICashFlowDataService cashFlowDataService;

    // ==================== 基础CRUD操作 ====================

    @Operation(summary = "分页查询现金流量表数据列表")
    @PostMapping("/list")
    public R<Map<String, Object>> list(@RequestBody CashFlowDataQueryVO queryVO) {
        try {
            Map<String, Object> result = cashFlowDataService.selectCashFlowDataList(queryVO);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询现金流量表数据列表失败", e);
            return R.fail("查询现金流量表数据列表失败: " + e.getMessage());
        }
    }

    @Operation(summary = "根据ID查询现金流量表数据详情")
    @GetMapping("/{cashFlowId}")
    public R<CashFlowData> getById(@Parameter(description="现金流量表ID") @PathVariable String cashFlowId) {
        try {
            CashFlowData data = cashFlowDataService.selectCashFlowDataById(cashFlowId);
            return R.success(data);
        } catch (Exception e) {
            log.error("查询现金流量表数据详情失败: {}", cashFlowId, e);
            return R.fail("查询现金流量表数据详情失败: " + e.getMessage());
        }
    }

    @Operation(summary = "新增现金流量表数据")
    @PostMapping("/add")
    public R<Boolean> add(@RequestBody CashFlowData cashFlowData) {
        try {
            boolean result = cashFlowDataService.insertCashFlowData(cashFlowData);
            return result ? R.success(true, "新增现金流量表数据成功") : R.fail("新增现金流量表数据失败");
        } catch (Exception e) {
            log.error("新增现金流量表数据失败", e);
            return R.fail("新增现金流量表数据失败: " + e.getMessage());
        }
    }

    @Operation(summary = "修改现金流量表数据")
    @PostMapping("/update")
    public R<Boolean> update(@RequestBody CashFlowData cashFlowData) {
        try {
            boolean result = cashFlowDataService.updateCashFlowData(cashFlowData);
            return result ? R.success(true, "修改现金流量表数据成功") : R.fail("修改现金流量表数据失败");
        } catch (Exception e) {
            log.error("修改现金流量表数据失败", e);
            return R.fail("修改现金流量表数据失败: " + e.getMessage());
        }
    }

    @Operation(summary = "删除现金流量表数据")
    @DeleteMapping("/{cashFlowId}")
    public R<Boolean> delete(@Parameter(description="现金流量表ID") @PathVariable String cashFlowId) {
        try {
            boolean result = cashFlowDataService.deleteCashFlowDataById(cashFlowId);
            return result ? R.success(true, "删除现金流量表数据成功") : R.fail("删除现金流量表数据失败");
        } catch (Exception e) {
            log.error("删除现金流量表数据失败: {}", cashFlowId, e);
            return R.fail("删除现金流量表数据失败: " + e.getMessage());
        }
    }

    // ==================== 经营活动现金流量分析接口 ====================

    @Operation(summary = "经营活动现金流量综合分析")
    @PostMapping("/analyze/operating-cash-flow")
    public R<Map<String, Object>> analyzeOperatingCashFlow(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = cashFlowDataService.analyzeOperatingCashFlow(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("经营活动现金流量分析失败: {}", enterpriseId, e);
            return R.fail("经营活动现金流量分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "经营活动现金流入分析")
    @PostMapping("/analyze/operating-cash-inflow")
    public R<Map<String, Object>> analyzeOperatingCashInflow(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = cashFlowDataService.analyzeOperatingCashInflow(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("经营活动现金流入分析失败: {}", enterpriseId, e);
            return R.fail("经营活动现金流入分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "经营活动现金流出分析")
    @PostMapping("/analyze/operating-cash-outflow")
    public R<Map<String, Object>> analyzeOperatingCashOutflow(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = cashFlowDataService.analyzeOperatingCashOutflow(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("经营活动现金流出分析失败: {}", enterpriseId, e);
            return R.fail("经营活动现金流出分析失败: " + e.getMessage());
        }
    }

    // ==================== 投资活动现金流量分析接口 ====================

    @Operation(summary = "投资活动现金流量综合分析")
    @PostMapping("/analyze/investing-cash-flow")
    public R<Map<String, Object>> analyzeInvestingCashFlow(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = cashFlowDataService.analyzeInvestingCashFlow(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("投资活动现金流量分析失败: {}", enterpriseId, e);
            return R.fail("投资活动现金流量分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "固定资产投资分析")
    @PostMapping("/analyze/fixed-asset-investment")
    public R<Map<String, Object>> analyzeFixedAssetInvestment(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = cashFlowDataService.analyzeFixedAssetInvestment(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("固定资产投资分析失败: {}", enterpriseId, e);
            return R.fail("固定资产投资分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "对外投资分析")
    @PostMapping("/analyze/external-investment")
    public R<Map<String, Object>> analyzeExternalInvestment(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = cashFlowDataService.analyzeExternalInvestment(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("对外投资分析失败: {}", enterpriseId, e);
            return R.fail("对外投资分析失败: " + e.getMessage());
        }
    }

    // ==================== 筹资活动现金流量分析接口 ====================

    @Operation(summary = "筹资活动现金流量综合分析")
    @PostMapping("/analyze/financing-cash-flow")
    public R<Map<String, Object>> analyzeFinancingCashFlow(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = cashFlowDataService.analyzeFinancingCashFlow(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("筹资活动现金流量分析失败: {}", enterpriseId, e);
            return R.fail("筹资活动现金流量分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "股权融资分析")
    @PostMapping("/analyze/equity-financing")
    public R<Map<String, Object>> analyzeEquityFinancing(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = cashFlowDataService.analyzeEquityFinancing(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("股权融资分析失败: {}", enterpriseId, e);
            return R.fail("股权融资分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "债务融资分析")
    @PostMapping("/analyze/debt-financing")
    public R<Map<String, Object>> analyzeDebtFinancing(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = cashFlowDataService.analyzeDebtFinancing(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("债务融资分析失败: {}", enterpriseId, e);
            return R.fail("债务融资分析失败: " + e.getMessage());
        }
    }

    // ==================== 现金流量质量分析接口 ====================

    @Operation(summary = "现金流量质量综合分析")
    @PostMapping("/analyze/cash-flow-quality")
    public R<Map<String, Object>> analyzeCashFlowQuality(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = cashFlowDataService.analyzeCashFlowQuality(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("现金流量质量分析失败: {}", enterpriseId, e);
            return R.fail("现金流量质量分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "现金流量结构分析")
    @PostMapping("/analyze/cash-flow-structure")
    public R<Map<String, Object>> analyzeCashFlowStructure(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = cashFlowDataService.analyzeCashFlowStructure(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("现金流量结构分析失败: {}", enterpriseId, e);
            return R.fail("现金流量结构分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "现金流量充足性分析")
    @PostMapping("/analyze/cash-flow-adequacy")
    public R<Map<String, Object>> analyzeCashFlowAdequacy(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = cashFlowDataService.analyzeCashFlowAdequacy(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("现金流量充足性分析失败: {}", enterpriseId, e);
            return R.fail("现金流量充足性分析失败: " + e.getMessage());
        }
    }

    // ==================== 现金管理分析接口 ====================

    @Operation(summary = "现金管理综合分析")
    @PostMapping("/analyze/cash-management")
    public R<Map<String, Object>> analyzeCashManagement(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = cashFlowDataService.analyzeCashManagement(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("现金管理分析失败: {}", enterpriseId, e);
            return R.fail("现金管理分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "现金周转分析")
    @PostMapping("/analyze/cash-turnover")
    public R<Map<String, Object>> analyzeCashTurnover(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = cashFlowDataService.analyzeCashTurnover(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("现金周转分析失败: {}", enterpriseId, e);
            return R.fail("现金周转分析失败: " + e.getMessage());
        }
    }

    // ==================== 现金流量比率分析接口 ====================

    @Operation(summary = "现金流量比率综合分析")
    @PostMapping("/analyze/cash-flow-ratio")
    public R<Map<String, Object>> analyzeCashFlowRatio(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = cashFlowDataService.analyzeCashFlowRatio(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("现金流量比率分析失败: {}", enterpriseId, e);
            return R.fail("现金流量比率分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "现金流量比率预警")
    @PostMapping("/warning/cash-flow-ratio")
    public R<Map<String, Object>> getCashFlowRatioWarning(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="报表年度") @RequestParam Integer reportYear) {
        try {
            Map<String, Object> result = cashFlowDataService.getCashFlowRatioWarning(enterpriseId, reportYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("现金流量比率预警失败: {}", enterpriseId, e);
            return R.fail("现金流量比率预警失败: " + e.getMessage());
        }
    }

    // ==================== 趋势分析接口 ====================

    @Operation(summary = "现金流量趋势综合分析")
    @PostMapping("/analyze/cash-flow-trend")
    public R<Map<String, Object>> analyzeCashFlowTrend(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = cashFlowDataService.analyzeCashFlowTrend(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("现金流量趋势分析失败: {}", enterpriseId, e);
            return R.fail("现金流量趋势分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "经营现金流趋势分析")
    @PostMapping("/analyze/operating-cash-flow-trend")
    public R<Map<String, Object>> analyzeOperatingCashFlowTrend(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = cashFlowDataService.analyzeOperatingCashFlowTrend(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("经营现金流趋势分析失败: {}", enterpriseId, e);
            return R.fail("经营现金流趋势分析失败: " + e.getMessage());
        }
    }

    // ==================== 现金流量比率计算接口 ====================

    @Operation(summary = "计算现金流量比率")
    @PostMapping("/calculate/cash-flow-ratios")
    public R<CashFlowData> calculateCashFlowRatios(@Parameter(description="现金流量表ID") @RequestParam String cashFlowId) {
        try {
            CashFlowData result = cashFlowDataService.calculateCashFlowRatios(cashFlowId);
            return R.success(result);
        } catch (Exception e) {
            log.error("计算现金流量比率失败: {}", cashFlowId, e);
            return R.fail("计算现金流量比率失败: " + e.getMessage());
        }
    }

    @Operation(summary = "批量计算现金流量比率")
    @PostMapping("/batch/calculate-ratios")
    public R<Map<String, Object>> batchCalculateCashFlowRatios(
            @RequestBody List<String> enterpriseIds,
            @Parameter(description="报表年度") @RequestParam Integer reportYear,
            @Parameter(description="报表期间") @RequestParam Integer reportPeriod) {
        try {
            Map<String, Object> result = cashFlowDataService.batchCalculateCashFlowRatios(enterpriseIds, reportYear, reportPeriod);
            return R.success(result);
        } catch (Exception e) {
            log.error("批量计算现金流量比率失败", e);
            return R.fail("批量计算现金流量比率失败: " + e.getMessage());
        }
    }

    // ==================== 统计分析接口 ====================

    @Operation(summary = "现金流量规模分布统计")
    @GetMapping("/statistics/cash-flow-scale")
    public R<List<Map<String, Object>>> getCashFlowScaleDistributionStatistics(
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            List<Map<String, Object>> result = cashFlowDataService.getCashFlowScaleDistributionStatistics(startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("现金流量规模分布统计失败", e);
            return R.fail("现金流量规模分布统计失败: " + e.getMessage());
        }
    }

    @Operation(summary = "现金流量表统计概览")
    @GetMapping("/statistics/overview")
    public R<Map<String, Object>> getCashFlowStatisticsOverview() {
        try {
            Map<String, Object> result = cashFlowDataService.getCashFlowStatisticsOverview();
            return R.success(result);
        } catch (Exception e) {
            log.error("获取现金流量表统计概览失败", e);
            return R.fail("获取现金流量表统计概览失败: " + e.getMessage());
        }
    }

    // ==================== 导出功能接口 ====================

    @Operation(summary = "导出现金流量表数据列表")
    @PostMapping("/export/list")
    public R<List<Map<String, Object>>> exportCashFlowDataList(@RequestBody CashFlowDataQueryVO queryVO) {
        try {
            List<Map<String, Object>> result = cashFlowDataService.exportCashFlowDataList(queryVO);
            return R.success(result);
        } catch (Exception e) {
            log.error("导出现金流量表数据列表失败", e);
            return R.fail("导出现金流量表数据列表失败: " + e.getMessage());
        }
    }

    @Operation(summary = "导出现金流量分析报告")
    @PostMapping("/export/cash-flow-report")
    public R<Map<String, Object>> exportCashFlowAnalysisReport(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = cashFlowDataService.exportCashFlowAnalysisReport(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("导出现金流量分析报告失败: {}", enterpriseId, e);
            return R.fail("导出现金流量分析报告失败: " + e.getMessage());
        }
    }

}
