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

import com.huabo.cybermonitor.entity.AssetInfo;
import com.huabo.cybermonitor.service.IAssetInfoService;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.util.R;
import com.huabo.cybermonitor.vo.AssetInfoQueryVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;



/**
 * 资产信息控制器
 * 
 * @author 华博云AI助手
 * @since 2024-12-12
 */
@RestController
@RequestMapping("/v1/supervision/asset/info")
@Tag(name = "资产信息管理", description = "资产配置监管、资产质量评估、资产运营分析相关接口")
public class AssetInfoController {

	private static final Logger log = LoggerFactory.getLogger(AssetInfoController.class);

    @Autowired
    private IAssetInfoService assetInfoService;

    @Operation(summary = "分页查询资产信息列表", description = "支持多条件组合查询资产信息")
    @PostMapping("/list")
    public R<PageResult<AssetInfo>> getAssetInfoList(@RequestBody AssetInfoQueryVO queryVO) {
        try {
            PageResult<AssetInfo> result = assetInfoService.getAssetInfoList(queryVO);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询资产信息列表失败", e);
            return R.fail("查询资产信息列表失败: " + e.getMessage());
        }
    }

    @Operation(summary = "根据ID查询资产信息", description = "根据资产ID查询详细信息")
    @GetMapping("/{assetId}")
    public R<AssetInfo> getAssetInfoById(@Parameter(description = "资产ID") @PathVariable String assetId) {
        try {
            AssetInfo assetInfo = assetInfoService.getById(assetId);
            return R.success(assetInfo);
        } catch (Exception e) {
            log.error("根据ID查询资产信息失败: {}", assetId, e);
            return R.fail("查询资产信息失败: " + e.getMessage());
        }
    }

    @Operation(summary = "新增资产信息", description = "新增资产信息记录")
    @PostMapping("/add")
    public R<Boolean> addAssetInfo(@RequestBody AssetInfo assetInfo) {
        try {
            boolean result = assetInfoService.save(assetInfo);
            return R.success(result);
        } catch (Exception e) {
            log.error("新增资产信息失败", e);
            return R.fail("新增资产信息失败: " + e.getMessage());
        }
    }

    @Operation(summary = "修改资产信息", description = "修改资产信息记录")
    @PostMapping("/update")
    public R<Boolean> updateAssetInfo(@RequestBody AssetInfo assetInfo) {
        try {
            boolean result = assetInfoService.updateById(assetInfo);
            return R.success(result);
        } catch (Exception e) {
            log.error("修改资产信息失败", e);
            return R.fail("修改资产信息失败: " + e.getMessage());
        }
    }

    @Operation(summary = "删除资产信息", description = "根据ID删除资产信息")
    @DeleteMapping("/{assetId}")
    public R<Boolean> deleteAssetInfo(@Parameter(description = "资产ID") @PathVariable String assetId) {
        try {
            boolean result = assetInfoService.removeById(assetId);
            return R.success(result);
        } catch (Exception e) {
            log.error("删除资产信息失败: {}", assetId, e);
            return R.fail("删除资产信息失败: " + e.getMessage());
        }
    }

    @Operation(summary = "根据企业ID查询资产信息", description = "查询指定企业的所有资产信息")
    @GetMapping("/enterprise/{enterpriseId}")
    public R<List<AssetInfo>> getAssetInfoByEnterpriseId(@Parameter(description = "企业ID") @PathVariable String enterpriseId) {
        try {
            List<AssetInfo> result = assetInfoService.getAssetInfoByEnterpriseId(enterpriseId);
            return R.success(result);
        } catch (Exception e) {
            log.error("根据企业ID查询资产信息失败: {}", enterpriseId, e);
            return R.fail("查询资产信息失败: " + e.getMessage());
        }
    }

    @Operation(summary = "查询核心资产", description = "查询企业核心资产列表")
    @GetMapping("/core")
    public R<List<AssetInfo>> getCoreAssets(@Parameter(description = "是否核心资产") @RequestParam Boolean isCoreAsset) {
        try {
            List<AssetInfo> result = assetInfoService.getCoreAssets(isCoreAsset);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询核心资产失败", e);
            return R.fail("查询核心资产失败: " + e.getMessage());
        }
    }

    @Operation(summary = "查询战略资产", description = "查询企业战略资产列表")
    @GetMapping("/strategic")
    public R<List<AssetInfo>> getStrategicAssets(@Parameter(description = "是否战略资产") @RequestParam Boolean isStrategicAsset) {
        try {
            List<AssetInfo> result = assetInfoService.getStrategicAssets(isStrategicAsset);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询战略资产失败", e);
            return R.fail("查询战略资产失败: " + e.getMessage());
        }
    }

    @Operation(summary = "查询闲置资产", description = "查询企业闲置资产列表")
    @GetMapping("/idle")
    public R<List<AssetInfo>> getIdleAssets(@Parameter(description = "是否闲置资产") @RequestParam Boolean isIdleAsset) {
        try {
            List<AssetInfo> result = assetInfoService.getIdleAssets(isIdleAsset);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询闲置资产失败", e);
            return R.fail("查询闲置资产失败: " + e.getMessage());
        }
    }

    @Operation(summary = "查询抵押质押资产", description = "查询企业抵押质押资产列表")
    @GetMapping("/pledged")
    public R<List<AssetInfo>> getPledgedAssets(@Parameter(description = "是否抵押质押") @RequestParam Boolean isPledged) {
        try {
            List<AssetInfo> result = assetInfoService.getPledgedAssets(isPledged);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询抵押质押资产失败", e);
            return R.fail("查询抵押质押资产失败: " + e.getMessage());
        }
    }

    @Operation(summary = "查询需要监管关注的资产", description = "查询需要监管关注的资产列表")
    @GetMapping("/regulatory-attention")
    public R<List<AssetInfo>> getRegulatoryAttentionAssets(@Parameter(description = "是否需要监管关注") @RequestParam Boolean needRegulatoryAttention) {
        try {
            List<AssetInfo> result = assetInfoService.getRegulatoryAttentionAssets(needRegulatoryAttention);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询需要监管关注的资产失败", e);
            return R.fail("查询需要监管关注的资产失败: " + e.getMessage());
        }
    }

    @Operation(summary = "资产配置结构分析", description = "分析企业资产配置结构")
    @GetMapping("/analysis/allocation-structure/{enterpriseId}")
    public R<Map<String, Object>> analyzeAssetAllocationStructure(@Parameter(description = "企业ID") @PathVariable String enterpriseId) {
        try {
            Map<String, Object> result = assetInfoService.analyzeAssetAllocationStructure(enterpriseId);
            return R.success(result);
        } catch (Exception e) {
            log.error("资产配置结构分析失败: {}", enterpriseId, e);
            return R.fail("资产配置结构分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "资产配置合理性评估", description = "评估企业资产配置合理性")
    @GetMapping("/assessment/allocation-rationality/{enterpriseId}")
    public R<Map<String, Object>> assessAssetAllocationRationality(@Parameter(description = "企业ID") @PathVariable String enterpriseId) {
        try {
            Map<String, Object> result = assetInfoService.assessAssetAllocationRationality(enterpriseId);
            return R.success(result);
        } catch (Exception e) {
            log.error("资产配置合理性评估失败: {}", enterpriseId, e);
            return R.fail("资产配置合理性评估失败: " + e.getMessage());
        }
    }

    @Operation(summary = "资产配置优化建议", description = "生成资产配置优化建议")
    @GetMapping("/suggestions/allocation-optimization/{enterpriseId}")
    public R<Map<String, Object>> generateAssetAllocationOptimizationSuggestions(@Parameter(description = "企业ID") @PathVariable String enterpriseId) {
        try {
            Map<String, Object> result = assetInfoService.generateAssetAllocationOptimizationSuggestions(enterpriseId);
            return R.success(result);
        } catch (Exception e) {
            log.error("生成资产配置优化建议失败: {}", enterpriseId, e);
            return R.fail("生成资产配置优化建议失败: " + e.getMessage());
        }
    }

    @Operation(summary = "资产质量指标监控", description = "监控企业资产质量指标")
    @GetMapping("/monitor/quality-indicators/{enterpriseId}")
    public R<Map<String, Object>> monitorAssetQualityIndicators(@Parameter(description = "企业ID") @PathVariable String enterpriseId) {
        try {
            Map<String, Object> result = assetInfoService.monitorAssetQualityIndicators(enterpriseId);
            return R.success(result);
        } catch (Exception e) {
            log.error("资产质量指标监控失败: {}", enterpriseId, e);
            return R.fail("资产质量指标监控失败: " + e.getMessage());
        }
    }

    @Operation(summary = "资产减值风险识别", description = "识别企业资产减值风险")
    @GetMapping("/identify/impairment-risk/{enterpriseId}")
    public R<List<AssetInfo>> identifyAssetImpairmentRisk(@Parameter(description = "企业ID") @PathVariable String enterpriseId) {
        try {
            List<AssetInfo> result = assetInfoService.identifyAssetImpairmentRisk(enterpriseId);
            return R.success(result);
        } catch (Exception e) {
            log.error("资产减值风险识别失败: {}", enterpriseId, e);
            return R.fail("资产减值风险识别失败: " + e.getMessage());
        }
    }

    @Operation(summary = "资产质量趋势分析", description = "分析企业资产质量趋势")
    @GetMapping("/analysis/quality-trend/{enterpriseId}")
    public R<Map<String, Object>> analyzeAssetQualityTrend(
            @Parameter(description = "企业ID") @PathVariable String enterpriseId,
            @Parameter(description = "开始日期") @RequestParam String startDate,
            @Parameter(description = "结束日期") @RequestParam String endDate) {
        try {
            Map<String, Object> result = assetInfoService.analyzeAssetQualityTrend(enterpriseId, startDate, endDate);
            return R.success(result);
        } catch (Exception e) {
            log.error("资产质量趋势分析失败: {}", enterpriseId, e);
            return R.fail("资产质量趋势分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "资产运营效率分析", description = "分析企业资产运营效率")
    @GetMapping("/analysis/operational-efficiency/{enterpriseId}")
    public R<Map<String, Object>> analyzeAssetOperationalEfficiency(@Parameter(description = "企业ID") @PathVariable String enterpriseId) {
        try {
            Map<String, Object> result = assetInfoService.analyzeAssetOperationalEfficiency(enterpriseId);
            return R.success(result);
        } catch (Exception e) {
            log.error("资产运营效率分析失败: {}", enterpriseId, e);
            return R.fail("资产运营效率分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "资产收益率分析", description = "分析企业资产收益率")
    @GetMapping("/analysis/return-rate/{enterpriseId}")
    public R<Map<String, Object>> analyzeAssetReturnRate(@Parameter(description = "企业ID") @PathVariable String enterpriseId) {
        try {
            Map<String, Object> result = assetInfoService.analyzeAssetReturnRate(enterpriseId);
            return R.success(result);
        } catch (Exception e) {
            log.error("资产收益率分析失败: {}", enterpriseId, e);
            return R.fail("资产收益率分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "资产周转率分析", description = "分析企业资产周转率")
    @GetMapping("/analysis/turnover-rate/{enterpriseId}")
    public R<Map<String, Object>> analyzeAssetTurnoverRate(@Parameter(description = "企业ID") @PathVariable String enterpriseId) {
        try {
            Map<String, Object> result = assetInfoService.analyzeAssetTurnoverRate(enterpriseId);
            return R.success(result);
        } catch (Exception e) {
            log.error("资产周转率分析失败: {}", enterpriseId, e);
            return R.fail("资产周转率分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "资产利用率分析", description = "分析企业资产利用率")
    @GetMapping("/analysis/utilization-rate/{enterpriseId}")
    public R<Map<String, Object>> analyzeAssetUtilizationRate(@Parameter(description = "企业ID") @PathVariable String enterpriseId) {
        try {
            Map<String, Object> result = assetInfoService.analyzeAssetUtilizationRate(enterpriseId);
            return R.success(result);
        } catch (Exception e) {
            log.error("资产利用率分析失败: {}", enterpriseId, e);
            return R.fail("资产利用率分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "资产价值趋势分析", description = "分析企业资产价值趋势")
    @GetMapping("/analysis/value-trend/{enterpriseId}")
    public R<Map<String, Object>> analyzeAssetValueTrend(
            @Parameter(description = "企业ID") @PathVariable String enterpriseId,
            @Parameter(description = "开始日期") @RequestParam String startDate,
            @Parameter(description = "结束日期") @RequestParam String endDate) {
        try {
            Map<String, Object> result = assetInfoService.analyzeAssetValueTrend(enterpriseId, startDate, endDate);
            return R.success(result);
        } catch (Exception e) {
            log.error("资产价值趋势分析失败: {}", enterpriseId, e);
            return R.fail("资产价值趋势分析失败: " + e.getMessage());
        }
    }

    @Operation(summary = "资产质量评估", description = "评估指定资产的质量")
    @GetMapping("/assessment/quality/{assetId}")
    public R<Map<String, Object>> assessAssetQuality(@Parameter(description = "资产ID") @PathVariable String assetId) {
        try {
            Map<String, Object> result = assetInfoService.assessAssetQuality(assetId);
            return R.success(result);
        } catch (Exception e) {
            log.error("资产质量评估失败: {}", assetId, e);
            return R.fail("资产质量评估失败: " + e.getMessage());
        }
    }

    @Operation(summary = "资产价值评估", description = "评估指定资产的价值")
    @GetMapping("/assessment/value/{assetId}")
    public R<Map<String, Object>> assessAssetValue(@Parameter(description = "资产ID") @PathVariable String assetId) {
        try {
            Map<String, Object> result = assetInfoService.assessAssetValue(assetId);
            return R.success(result);
        } catch (Exception e) {
            log.error("资产价值评估失败: {}", assetId, e);
            return R.fail("资产价值评估失败: " + e.getMessage());
        }
    }

    @Operation(summary = "资产风险评估", description = "评估指定资产的风险")
    @GetMapping("/assessment/risk/{assetId}")
    public R<Map<String, Object>> assessAssetRisk(@Parameter(description = "资产ID") @PathVariable String assetId) {
        try {
            Map<String, Object> result = assetInfoService.assessAssetRisk(assetId);
            return R.success(result);
        } catch (Exception e) {
            log.error("资产风险评估失败: {}", assetId, e);
            return R.fail("资产风险评估失败: " + e.getMessage());
        }
    }

    @Operation(summary = "资产绩效评估", description = "评估指定资产的绩效")
    @GetMapping("/assessment/performance/{assetId}")
    public R<Map<String, Object>> assessAssetPerformance(@Parameter(description = "资产ID") @PathVariable String assetId) {
        try {
            Map<String, Object> result = assetInfoService.assessAssetPerformance(assetId);
            return R.success(result);
        } catch (Exception e) {
            log.error("资产绩效评估失败: {}", assetId, e);
            return R.fail("资产绩效评估失败: " + e.getMessage());
        }
    }

    @Operation(summary = "按资产类型统计分布", description = "统计资产按类型的分布情况")
    @GetMapping("/statistics/distribution/type")
    public R<List<Map<String, Object>>> getAssetDistributionByType() {
        try {
            List<Map<String, Object>> result = assetInfoService.getAssetDistributionByType();
            return R.success(result);
        } catch (Exception e) {
            log.error("按资产类型统计分布失败", e);
            return R.fail("按资产类型统计分布失败: " + e.getMessage());
        }
    }

    @Operation(summary = "按资产性质统计分布", description = "统计资产按性质的分布情况")
    @GetMapping("/statistics/distribution/nature")
    public R<List<Map<String, Object>>> getAssetDistributionByNature() {
        try {
            List<Map<String, Object>> result = assetInfoService.getAssetDistributionByNature();
            return R.success(result);
        } catch (Exception e) {
            log.error("按资产性质统计分布失败", e);
            return R.fail("按资产性质统计分布失败: " + e.getMessage());
        }
    }

    @Operation(summary = "按资产状态统计分布", description = "统计资产按状态的分布情况")
    @GetMapping("/statistics/distribution/status")
    public R<List<Map<String, Object>>> getAssetDistributionByStatus() {
        try {
            List<Map<String, Object>> result = assetInfoService.getAssetDistributionByStatus();
            return R.success(result);
        } catch (Exception e) {
            log.error("按资产状态统计分布失败", e);
            return R.fail("按资产状态统计分布失败: " + e.getMessage());
        }
    }

    @Operation(summary = "按资产风险等级统计分布", description = "统计资产按风险等级的分布情况")
    @GetMapping("/statistics/distribution/risk-level")
    public R<List<Map<String, Object>>> getAssetDistributionByRiskLevel() {
        try {
            List<Map<String, Object>> result = assetInfoService.getAssetDistributionByRiskLevel();
            return R.success(result);
        } catch (Exception e) {
            log.error("按资产风险等级统计分布失败", e);
            return R.fail("按资产风险等级统计分布失败: " + e.getMessage());
        }
    }

    @Operation(summary = "批量更新资产状态", description = "批量更新多个资产的状态")
    @PostMapping("/batch/update-status")
    public R<Boolean> batchUpdateAssetStatus(@RequestBody Map<String, Object> params) {
        try {
            @SuppressWarnings("unchecked")
            List<String> assetIds = (List<String>) params.get("assetIds");
            String assetStatus = (String) params.get("assetStatus");
            boolean result = assetInfoService.batchUpdateAssetStatus(assetIds, assetStatus);
            return R.success(result);
        } catch (Exception e) {
            log.error("批量更新资产状态失败", e);
            return R.fail("批量更新资产状态失败: " + e.getMessage());
        }
    }

    @Operation(summary = "获取资产统计概览", description = "获取资产管理统计概览信息")
    @GetMapping("/statistics/overview")
    public R<Map<String, Object>> getAssetStatisticsOverview() {
        try {
            Map<String, Object> result = assetInfoService.getAssetStatisticsOverview();
            return R.success(result);
        } catch (Exception e) {
            log.error("获取资产统计概览失败", e);
            return R.fail("获取资产统计概览失败: " + e.getMessage());
        }
    }

    @Operation(summary = "导出资产信息列表", description = "导出资产信息列表到Excel")
    @PostMapping("/export")
    public R<List<Map<String, Object>>> exportAssetInfoList(@RequestBody AssetInfoQueryVO queryVO) {
        try {
            List<Map<String, Object>> result = assetInfoService.exportAssetInfoList(queryVO);
            return R.success(result);
        } catch (Exception e) {
            log.error("导出资产信息列表失败", e);
            return R.fail("导出资产信息列表失败: " + e.getMessage());
        }
    }

}
