package com.huabo.cybermonitor.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.AssetInfo;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.vo.AssetInfoQueryVO;

/**
 * 资产信息服务接口
 * 
 * @author 华博云AI助手
 * @since 2024-12-12
 */
public interface IAssetInfoService extends IService<AssetInfo> {

    /**
     * 分页查询资产信息列表
     */
    PageResult<AssetInfo> getAssetInfoList(AssetInfoQueryVO queryVO);

    /**
     * 根据企业ID查询资产信息
     */
    List<AssetInfo> getAssetInfoByEnterpriseId(String enterpriseId);

    /**
     * 根据资产类型查询资产信息
     */
    List<AssetInfo> getAssetInfoByType(String assetType);

    /**
     * 根据资产分类查询资产信息
     */
    List<AssetInfo> getAssetInfoByCategory(String assetCategory);

    /**
     * 根据资产性质查询资产信息
     */
    List<AssetInfo> getAssetInfoByNature(String assetNature);

    /**
     * 根据资产状态查询资产信息
     */
    List<AssetInfo> getAssetInfoByStatus(String assetStatus);

    /**
     * 根据资产地区查询资产信息
     */
    List<AssetInfo> getAssetInfoByRegion(String assetRegion);

    /**
     * 根据资产行业查询资产信息
     */
    List<AssetInfo> getAssetInfoByIndustry(String assetIndustry);

    /**
     * 根据资产质量等级查询资产信息
     */
    List<AssetInfo> getAssetInfoByQualityLevel(String assetQualityLevel);

    /**
     * 根据资产风险等级查询资产信息
     */
    List<AssetInfo> getAssetInfoByRiskLevel(String assetRiskLevel);

    /**
     * 查询核心资产
     */
    List<AssetInfo> getCoreAssets(Boolean isCoreAsset);

    /**
     * 查询战略资产
     */
    List<AssetInfo> getStrategicAssets(Boolean isStrategicAsset);

    /**
     * 查询闲置资产
     */
    List<AssetInfo> getIdleAssets(Boolean isIdleAsset);

    /**
     * 查询抵押质押资产
     */
    List<AssetInfo> getPledgedAssets(Boolean isPledged);

    /**
     * 查询需要监管关注的资产
     */
    List<AssetInfo> getRegulatoryAttentionAssets(Boolean needRegulatoryAttention);

    /**
     * 根据资产管理人查询资产
     */
    List<AssetInfo> getAssetInfoByManager(String assetManager);

    /**
     * 根据管理部门查询资产
     */
    List<AssetInfo> getAssetInfoByDepartment(String managementDepartment);

    /**
     * 资产配置结构分析
     */
    Map<String, Object> analyzeAssetAllocationStructure(String enterpriseId);

    /**
     * 资产配置合理性评估
     */
    Map<String, Object> assessAssetAllocationRationality(String enterpriseId);

    /**
     * 资产配置优化建议
     */
    Map<String, Object> generateAssetAllocationOptimizationSuggestions(String enterpriseId);

    /**
     * 资产质量指标监控
     */
    Map<String, Object> monitorAssetQualityIndicators(String enterpriseId);

    /**
     * 资产减值风险识别
     */
    List<AssetInfo> identifyAssetImpairmentRisk(String enterpriseId);

    /**
     * 资产质量趋势分析
     */
    Map<String, Object> analyzeAssetQualityTrend(String enterpriseId, String startDate, String endDate);

    /**
     * 资产运营效率分析
     */
    Map<String, Object> analyzeAssetOperationalEfficiency(String enterpriseId);

    /**
     * 资产收益率分析
     */
    Map<String, Object> analyzeAssetReturnRate(String enterpriseId);

    /**
     * 资产周转率分析
     */
    Map<String, Object> analyzeAssetTurnoverRate(String enterpriseId);

    /**
     * 资产利用率分析
     */
    Map<String, Object> analyzeAssetUtilizationRate(String enterpriseId);

    /**
     * 资产价值趋势分析
     */
    Map<String, Object> analyzeAssetValueTrend(String enterpriseId, String startDate, String endDate);

    /**
     * 资产维护状况分析
     */
    Map<String, Object> analyzeAssetMaintenanceCondition(String enterpriseId);

    /**
     * 资产保险状况分析
     */
    Map<String, Object> analyzeAssetInsuranceStatus(String enterpriseId);

    /**
     * 资产技术状态分析
     */
    Map<String, Object> analyzeAssetTechnicalCondition(String enterpriseId);

    /**
     * 资产折旧分析
     */
    Map<String, Object> analyzeAssetDepreciation(String enterpriseId);

    /**
     * 资产集中度分析
     */
    Map<String, Object> analyzeAssetConcentration(String enterpriseId);

    /**
     * 资产风险预警
     */
    List<AssetInfo> getAssetRiskWarning(String riskLevel);

    /**
     * 资产质量评估
     */
    Map<String, Object> assessAssetQuality(String assetId);

    /**
     * 资产价值评估
     */
    Map<String, Object> assessAssetValue(String assetId);

    /**
     * 资产风险评估
     */
    Map<String, Object> assessAssetRisk(String assetId);

    /**
     * 资产绩效评估
     */
    Map<String, Object> assessAssetPerformance(String assetId);

    /**
     * 按资产类型统计资产分布
     */
    List<Map<String, Object>> getAssetDistributionByType();

    /**
     * 按资产分类统计资产分布
     */
    List<Map<String, Object>> getAssetDistributionByCategory();

    /**
     * 按资产性质统计资产分布
     */
    List<Map<String, Object>> getAssetDistributionByNature();

    /**
     * 按资产状态统计资产分布
     */
    List<Map<String, Object>> getAssetDistributionByStatus();

    /**
     * 按资产地区统计资产分布
     */
    List<Map<String, Object>> getAssetDistributionByRegion();

    /**
     * 按资产行业统计资产分布
     */
    List<Map<String, Object>> getAssetDistributionByIndustry();

    /**
     * 按资产质量等级统计资产分布
     */
    List<Map<String, Object>> getAssetDistributionByQualityLevel();

    /**
     * 按资产风险等级统计资产分布
     */
    List<Map<String, Object>> getAssetDistributionByRiskLevel();

    /**
     * 批量更新资产状态
     */
    boolean batchUpdateAssetStatus(List<String> assetIds, String assetStatus);

    /**
     * 批量更新资产质量等级
     */
    boolean batchUpdateAssetQualityLevel(List<String> assetIds, String qualityLevel);

    /**
     * 批量更新资产风险等级
     */
    boolean batchUpdateAssetRiskLevel(List<String> assetIds, String riskLevel);

    /**
     * 批量更新资产价值
     */
    boolean batchUpdateAssetValue(List<AssetInfo> assetList);

    /**
     * 删除过期资产记录
     */
    boolean deleteExpiredAssetRecords(Integer days);

    /**
     * 获取资产统计概览
     */
    Map<String, Object> getAssetStatisticsOverview();

    /**
     * 获取资产类型分布
     */
    List<Map<String, Object>> getAssetTypeDistribution();

    /**
     * 获取资产性质分布
     */
    List<Map<String, Object>> getAssetNatureDistribution();

    /**
     * 获取资产状态分布
     */
    List<Map<String, Object>> getAssetStatusDistribution();

    /**
     * 获取资产质量分布
     */
    List<Map<String, Object>> getAssetQualityDistribution();

    /**
     * 获取资产风险分布
     */
    List<Map<String, Object>> getAssetRiskDistribution();

    /**
     * 导出资产信息列表
     */
    List<Map<String, Object>> exportAssetInfoList(AssetInfoQueryVO queryVO);

    /**
     * 资产类型标签转换
     */
    String convertAssetTypeLabel(String assetType);

    /**
     * 资产分类标签转换
     */
    String convertAssetCategoryLabel(String assetCategory);

    /**
     * 资产性质标签转换
     */
    String convertAssetNatureLabel(String assetNature);

    /**
     * 资产状态标签转换
     */
    String convertAssetStatusLabel(String assetStatus);

    /**
     * 资产质量等级标签转换
     */
    String convertAssetQualityLevelLabel(String qualityLevel);

    /**
     * 资产风险等级标签转换
     */
    String convertAssetRiskLevelLabel(String riskLevel);

}
