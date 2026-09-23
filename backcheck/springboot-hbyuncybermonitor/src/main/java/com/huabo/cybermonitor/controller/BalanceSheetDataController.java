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

import com.huabo.cybermonitor.entity.BalanceSheetData;
import com.huabo.cybermonitor.service.IBalanceSheetDataService;
import com.huabo.cybermonitor.util.R;
import com.huabo.cybermonitor.vo.BalanceSheetDataQueryVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;



/**
 * 资产负债表数据控制器
 * 
 * @author 华博云AI助手
 * @since 2024-12-12
 */
@Tag(name="资产负债表数据管理",description="资产负债表数据管理")
@RestController
@RequestMapping("/v1/supervision/financial/balance-sheet")
public class BalanceSheetDataController {

	private static final Logger log = LoggerFactory.getLogger(BalanceSheetDataController.class);

    @Autowired
    private IBalanceSheetDataService balanceSheetDataService;

    // ==================== 基础CRUD操作 ====================

    @Operation(summary = "分页查询资产负债表数据列表")
    @PostMapping("/list")
    public R<Map<String, Object>> list(@RequestBody BalanceSheetDataQueryVO queryVO) {
        try {
            Map<String, Object> result = balanceSheetDataService.selectBalanceSheetDataList(queryVO);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询资产负债表数据列表失败", e);
            return R.fail("查询资产负债表数据列表失败: " + e.getMessage());
        }
    }

    @Operation(summary = "根据ID查询资产负债表数据详情")
    @GetMapping("/{balanceSheetId}")
    public R<BalanceSheetData> getById(@Parameter(description="资产负债表ID") @PathVariable String balanceSheetId) {
        try {
            BalanceSheetData data = balanceSheetDataService.selectBalanceSheetDataById(balanceSheetId);
            return R.success(data);
        } catch (Exception e) {
            log.error("查询资产负债表数据详情失败: {}", balanceSheetId, e);
            return R.fail("查询资产负债表数据详情失败: " + e.getMessage());
        }
    }

    @Operation(summary = "新增资产负债表数据")
    @PostMapping("/add")
    public R<Boolean> add(@RequestBody BalanceSheetData balanceSheetData) {
        try {
            boolean result = balanceSheetDataService.insertBalanceSheetData(balanceSheetData);
            return result ? R.success(true, "新增资产负债表数据成功") : R.fail("新增资产负债表数据失败");
        } catch (Exception e) {
            log.error("新增资产负债表数据失败", e);
            return R.fail("新增资产负债表数据失败: " + e.getMessage());
        }
    }

    @Operation(summary = "修改资产负债表数据")
    @PostMapping("/update")
    public R<Boolean> update(@RequestBody BalanceSheetData balanceSheetData) {
        try {
            boolean result = balanceSheetDataService.updateBalanceSheetData(balanceSheetData);
            return result ? R.success(true, "修改资产负债表数据成功") : R.fail("修改资产负债表数据失败");
        } catch (Exception e) {
            log.error("修改资产负债表数据失败", e);
            return R.fail("修改资产负债表数据失败: " + e.getMessage());
        }
    }

    @Operation(summary = "删除资产负债表数据")
    @DeleteMapping("/{balanceSheetId}")
    public R<Boolean> delete(@Parameter(description="资产负债表ID") @PathVariable String balanceSheetId) {
        try {
            boolean result = balanceSheetDataService.deleteBalanceSheetDataById(balanceSheetId);
            return result ? R.success(true, "删除资产负债表数据成功") : R.fail("删除资产负债表数据失败");
        } catch (Exception e) {
            log.error("删除资产负债表数据失败: {}", balanceSheetId, e);
            return R.fail("删除资产负债表数据失败: " + e.getMessage());
        }
    }

    @Operation(summary = "批量删除资产负债表数据")
    @DeleteMapping("/batch")
    public R<Boolean> deleteBatch(@RequestBody List<String> balanceSheetIds) {
        try {
            boolean result = balanceSheetDataService.deleteBalanceSheetDataByIds(balanceSheetIds);
            return result ? R.success(true, "批量删除资产负债表数据成功") : R.fail("批量删除资产负债表数据失败");
        } catch (Exception e) {
            log.error("批量删除资产负债表数据失败", e);
            return R.fail("批量删除资产负债表数据失败: " + e.getMessage());
        }
    }

    // ==================== 业务查询接口 ====================

    @Operation(summary = "根据企业ID查询资产负债表数据")
    @GetMapping("/enterprise/{enterpriseId}")
    public R<List<BalanceSheetData>> getByEnterpriseId(@Parameter(description="企业ID") @PathVariable String enterpriseId) {
        try {
            List<BalanceSheetData> dataList = balanceSheetDataService.selectByEnterpriseId(enterpriseId);
            return R.success(dataList);
        } catch (Exception e) {
            log.error("根据企业ID查询资产负债表数据失败: {}", enterpriseId, e);
            return R.fail("查询资产负债表数据失败: " + e.getMessage());
        }
    }

    @Operation(summary = "查询最新资产负债表数据")
    @GetMapping("/latest/{enterpriseId}")
    public R<List<BalanceSheetData>> getLatestData(
            @Parameter(description="企业ID") @PathVariable String enterpriseId,
            @Parameter(description="数量限制") @RequestParam(defaultValue = "5") Integer limit) {
        try {
            List<BalanceSheetData> dataList = balanceSheetDataService.selectLatestBalanceSheetData(enterpriseId, limit);
            return R.success(dataList);
        } catch (Exception e) {
            log.error("查询最新资产负债表数据失败: {}", enterpriseId, e);
            return R.fail("查询最新资产负债表数据失败: " + e.getMessage());
        }
    }

    // ==================== 资产结构分析接口 ====================

    @Operation(summary = "资产结构分析")
    @PostMapping("/analyze/asset-structure")
    public R<Map<String, Object>> analyzeAssetStructure(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = balanceSheetDataService.analyzeAssetStructure(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("资产结构分析失败: {}", enterpriseId, e);
            return R.fail("资产结构分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "流动资产分析")
    @PostMapping("/analyze/current-assets")
    public R<Map<String, Object>> analyzeCurrentAssets(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = balanceSheetDataService.analyzeCurrentAsset(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("流动资产分析失败: {}", enterpriseId, e);
            return R.fail("流动资产分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "非流动资产分析")
    @PostMapping("/analyze/non-current-assets")
    public R<Map<String, Object>> analyzeNonCurrentAssets(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = balanceSheetDataService.analyzeNonCurrentAsset(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("非流动资产分析失败: {}", enterpriseId, e);
            return R.fail("非流动资产分析失败: " + e.getMessage());
        }
    }

    // ==================== 负债结构分析接口 ====================

    @Operation(summary = "负债结构分析")
    @PostMapping("/analyze/liability-structure")
    public R<Map<String, Object>> analyzeLiabilityStructure(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = balanceSheetDataService.analyzeLiabilityStructure(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("负债结构分析失败: {}", enterpriseId, e);
            return R.fail("负债结构分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "流动负债分析")
    @PostMapping("/analyze/current-liabilities")
    public R<Map<String, Object>> analyzeCurrentLiabilities(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = balanceSheetDataService.analyzeCurrentLiability(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("流动负债分析失败: {}", enterpriseId, e);
            return R.fail("流动负债分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "非流动负债分析")
    @PostMapping("/analyze/non-current-liabilities")
    public R<Map<String, Object>> analyzeNonCurrentLiabilities(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = balanceSheetDataService.analyzeNonCurrentLiability(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("非流动负债分析失败: {}", enterpriseId, e);
            return R.fail("非流动负债分析失败: " + e.getMessage());
        }
    }

    // ==================== 所有者权益分析接口 ====================

    @Operation(summary = "所有者权益分析")
    @PostMapping("/analyze/owners-equity")
    public R<Map<String, Object>> analyzeOwnersEquity(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = balanceSheetDataService.analyzeOwnersEquityStructure(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("所有者权益分析失败: {}", enterpriseId, e);
            return R.fail("所有者权益分析失败: " + e.getMessage());
        }
    }

    // ==================== 偿债能力分析接口 ====================

    @Operation(summary = "偿债能力分析")
    @PostMapping("/analyze/solvency")
    public R<Map<String, Object>> analyzeSolvency(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = balanceSheetDataService.analyzeSolvency(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("偿债能力分析失败: {}", enterpriseId, e);
            return R.fail("偿债能力分析失败: " + e.getMessage());
        }
    }

    // ==================== 财务比率计算接口 ====================

    @Operation(summary = "计算财务比率")
    @PostMapping("/calculate/financial-ratios")
    public R<BalanceSheetData> calculateFinancialRatios(@Parameter(description="资产负债表ID") @RequestParam String balanceSheetId) {
        try {
            BalanceSheetData result = balanceSheetDataService.calculateFinancialRatios(balanceSheetId);
            return R.success(result);
        } catch (Exception e) {
            log.error("计算财务比率失败: {}", balanceSheetId, e);
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
            Map<String, Object> result = balanceSheetDataService.batchCalculateFinancialRatios(enterpriseIds, reportYear, reportPeriod);
            return R.success(result);
        } catch (Exception e) {
            log.error("批量计算财务比率失败", e);
            return R.fail("批量计算财务比率失败: " + e.getMessage());
        }
    }

    @Operation(summary = "重新计算财务比率")
    @PostMapping("/recalculate/financial-ratios")
    public R<Boolean> recalculateFinancialRatios(@Parameter(description="资产负债表ID") @RequestParam String balanceSheetId) {
        try {
            boolean result = balanceSheetDataService.recalculateFinancialRatios(balanceSheetId);
            return result ? R.success(true, "重新计算财务比率成功") : R.fail("重新计算财务比率失败");
        } catch (Exception e) {
            log.error("重新计算财务比率失败: {}", balanceSheetId, e);
            return R.fail("重新计算财务比率失败: " + e.getMessage());
        }
    }

    // ==================== 统计分析接口 ====================

    @Operation(summary = "按数据状态统计")
    @GetMapping("/statistics/data-status")
    public R<List<Map<String, Object>>> getDataStatusStatistics(
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            List<Map<String, Object>> result = balanceSheetDataService.getDataStatusStatistics(startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("按数据状态统计失败", e);
            return R.fail("按数据状态统计失败: " + e.getMessage());
        }
    }

    @Operation(summary = "资产规模分布统计")
    @GetMapping("/statistics/asset-scale")
    public R<List<Map<String, Object>>> getAssetScaleDistributionStatistics(
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            List<Map<String, Object>> result = balanceSheetDataService.getAssetScaleDistributionStatistics(startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("资产规模分布统计失败", e);
            return R.fail("资产规模分布统计失败: " + e.getMessage());
        }
    }

    @Operation(summary = "资产负债表统计概览")
    @GetMapping("/statistics/overview")
    public R<Map<String, Object>> getBalanceSheetStatisticsOverview() {
        try {
            Map<String, Object> result = balanceSheetDataService.getBalanceSheetStatisticsOverview();
            return R.success(result);
        } catch (Exception e) {
            log.error("获取资产负债表统计概览失败", e);
            return R.fail("获取资产负债表统计概览失败: " + e.getMessage());
        }
    }

    // ==================== 批量操作接口 ====================

    @Operation(summary = "批量更新数据状态")
    @PostMapping("/batch/update-status")
    public R<Boolean> batchUpdateDataStatus(
            @RequestBody List<String> balanceSheetIds,
            @Parameter(description="数据状态") @RequestParam String dataStatus) {
        try {
            boolean result = balanceSheetDataService.batchUpdateDataStatus(balanceSheetIds, dataStatus);
            return result ? R.success(true, "批量更新数据状态成功") : R.fail("批量更新数据状态失败");
        } catch (Exception e) {
            log.error("批量更新数据状态失败", e);
            return R.fail("批量更新数据状态失败: " + e.getMessage());
        }
    }

    @Operation(summary = "批量导入资产负债表数据")
    @PostMapping("/batch/import")
    public R<Map<String, Object>> batchImportBalanceSheetData(@RequestBody List<BalanceSheetData> dataList) {
        try {
            Map<String, Object> result = balanceSheetDataService.batchImportBalanceSheetData(dataList);
            return R.success(result);
        } catch (Exception e) {
            log.error("批量导入资产负债表数据失败", e);
            return R.fail("批量导入资产负债表数据失败: " + e.getMessage());
        }
    }

    // ==================== 导出功能接口 ====================

    @Operation(summary = "导出资产负债表数据列表")
    @PostMapping("/export/list")
    public R<List<Map<String, Object>>> exportBalanceSheetDataList(@RequestBody BalanceSheetDataQueryVO queryVO) {
        try {
            List<Map<String, Object>> result = balanceSheetDataService.exportBalanceSheetDataList(queryVO);
            return R.success(result);
        } catch (Exception e) {
            log.error("导出资产负债表数据列表失败", e);
            return R.fail("导出资产负债表数据列表失败: " + e.getMessage());
        }
    }

    @Operation(summary = "导出资产结构分析报告")
    @PostMapping("/export/asset-structure-report")
    public R<Map<String, Object>> exportAssetStructureAnalysisReport(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = balanceSheetDataService.exportAssetStructureAnalysisReport(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("导出资产结构分析报告失败: {}", enterpriseId, e);
            return R.fail("导出资产结构分析报告失败: " + e.getMessage());
        }
    }

    @Operation(summary = "导出偿债能力分析报告")
    @PostMapping("/export/solvency-report")
    public R<Map<String, Object>> exportSolvencyAnalysisReport(
            @Parameter(description="企业ID") @RequestParam String enterpriseId,
            @Parameter(description="开始年度") @RequestParam Integer startYear,
            @Parameter(description="结束年度") @RequestParam Integer endYear) {
        try {
            Map<String, Object> result = balanceSheetDataService.exportSolvencyAnalysisReport(enterpriseId, startYear, endYear);
            return R.success(result);
        } catch (Exception e) {
            log.error("导出偿债能力分析报告失败: {}", enterpriseId, e);
            return R.fail("导出偿债能力分析报告失败: " + e.getMessage());
        }
    }

}
