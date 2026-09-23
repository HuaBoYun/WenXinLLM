package com.huabo.cybermonitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.cybermonitor.entity.AssetInfo;
import com.huabo.cybermonitor.vo.AssetInfoQueryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 资产信息数据访问接口
 * 
 * @author 华博云AI助手
 * @since 2024-12-12
 */
@Mapper
public interface AssetInfoMapper extends BaseMapper<AssetInfo> {

    /**
     * 分页查询资产信息列表
     */
    List<AssetInfo> selectAssetInfoList(AssetInfoQueryVO queryVO);

    /**
     * 根据企业ID查询资产信息
     */
    List<AssetInfo> selectByEnterpriseId(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据资产类型查询资产信息
     */
    List<AssetInfo> selectByAssetType(@Param("assetType") String assetType);

    /**
     * 根据资产分类查询资产信息
     */
    List<AssetInfo> selectByAssetCategory(@Param("assetCategory") String assetCategory);

    /**
     * 根据资产性质查询资产信息
     */
    List<AssetInfo> selectByAssetNature(@Param("assetNature") String assetNature);

    /**
     * 根据资产状态查询资产信息
     */
    List<AssetInfo> selectByAssetStatus(@Param("assetStatus") String assetStatus);

    /**
     * 根据资产地区查询资产信息
     */
    List<AssetInfo> selectByAssetRegion(@Param("assetRegion") String assetRegion);

    /**
     * 根据资产行业查询资产信息
     */
    List<AssetInfo> selectByAssetIndustry(@Param("assetIndustry") String assetIndustry);

    /**
     * 根据资产质量等级查询资产信息
     */
    List<AssetInfo> selectByAssetQualityLevel(@Param("assetQualityLevel") String assetQualityLevel);

    /**
     * 根据资产风险等级查询资产信息
     */
    List<AssetInfo> selectByAssetRiskLevel(@Param("assetRiskLevel") String assetRiskLevel);

    /**
     * 查询核心资产
     */
    List<AssetInfo> selectCoreAssets(@Param("isCoreAsset") Boolean isCoreAsset);

    /**
     * 查询战略资产
     */
    List<AssetInfo> selectStrategicAssets(@Param("isStrategicAsset") Boolean isStrategicAsset);

    /**
     * 查询闲置资产
     */
    List<AssetInfo> selectIdleAssets(@Param("isIdleAsset") Boolean isIdleAsset);

    /**
     * 查询抵押质押资产
     */
    List<AssetInfo> selectPledgedAssets(@Param("isPledged") Boolean isPledged);

    /**
     * 查询需要监管关注的资产
     */
    List<AssetInfo> selectRegulatoryAttentionAssets(@Param("needRegulatoryAttention") Boolean needRegulatoryAttention);

    /**
     * 根据资产管理人查询资产
     */
    List<AssetInfo> selectByAssetManager(@Param("assetManager") String assetManager);

    /**
     * 根据管理部门查询资产
     */
    List<AssetInfo> selectByManagementDepartment(@Param("managementDepartment") String managementDepartment);

    /**
     * 按资产类型统计资产分布
     */
    List<Map<String, Object>> selectAssetDistributionByType();

    /**
     * 按资产分类统计资产分布
     */
    List<Map<String, Object>> selectAssetDistributionByCategory();

    /**
     * 按资产性质统计资产分布
     */
    List<Map<String, Object>> selectAssetDistributionByNature();

    /**
     * 按资产状态统计资产分布
     */
    List<Map<String, Object>> selectAssetDistributionByStatus();

    /**
     * 按资产地区统计资产分布
     */
    List<Map<String, Object>> selectAssetDistributionByRegion();

    /**
     * 按资产行业统计资产分布
     */
    List<Map<String, Object>> selectAssetDistributionByIndustry();

    /**
     * 按资产质量等级统计资产分布
     */
    List<Map<String, Object>> selectAssetDistributionByQualityLevel();

    /**
     * 按资产风险等级统计资产分布
     */
    List<Map<String, Object>> selectAssetDistributionByRiskLevel();

    /**
     * 查询资产配置效率分析
     */
    List<Map<String, Object>> selectAssetAllocationEfficiencyAnalysis(@Param("enterpriseId") String enterpriseId);

    /**
     * 查询资产质量评估分析
     */
    List<Map<String, Object>> selectAssetQualityAssessmentAnalysis(@Param("enterpriseId") String enterpriseId);

    /**
     * 查询资产运营效率分析
     */
    List<Map<String, Object>> selectAssetOperationalEfficiencyAnalysis(@Param("enterpriseId") String enterpriseId);

    /**
     * 查询资产收益率分析
     */
    List<Map<String, Object>> selectAssetReturnRateAnalysis(@Param("enterpriseId") String enterpriseId);

    /**
     * 查询资产周转率分析
     */
    List<Map<String, Object>> selectAssetTurnoverRateAnalysis(@Param("enterpriseId") String enterpriseId);

    /**
     * 查询资产利用率分析
     */
    List<Map<String, Object>> selectAssetUtilizationRateAnalysis(@Param("enterpriseId") String enterpriseId);

    /**
     * 查询资产价值趋势分析
     */
    List<Map<String, Object>> selectAssetValueTrendAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                           @Param("startDate") String startDate, 
                                                           @Param("endDate") String endDate);

    /**
     * 查询资产减值风险分析
     */
    List<Map<String, Object>> selectAssetImpairmentRiskAnalysis(@Param("enterpriseId") String enterpriseId);

    /**
     * 查询资产维护状况分析
     */
    List<Map<String, Object>> selectAssetMaintenanceAnalysis(@Param("enterpriseId") String enterpriseId);

    /**
     * 查询资产保险状况分析
     */
    List<Map<String, Object>> selectAssetInsuranceAnalysis(@Param("enterpriseId") String enterpriseId);

    /**
     * 查询资产技术状态分析
     */
    List<Map<String, Object>> selectAssetTechnicalConditionAnalysis(@Param("enterpriseId") String enterpriseId);

    /**
     * 查询资产折旧分析
     */
    List<Map<String, Object>> selectAssetDepreciationAnalysis(@Param("enterpriseId") String enterpriseId);

    /**
     * 查询资产集中度分析
     */
    List<Map<String, Object>> selectAssetConcentrationAnalysis(@Param("enterpriseId") String enterpriseId);

    /**
     * 查询资产风险预警
     */
    List<AssetInfo> selectAssetRiskWarning(@Param("riskLevel") String riskLevel);

    /**
     * 批量更新资产状态
     */
    int batchUpdateAssetStatus(@Param("assetIds") List<String> assetIds, @Param("assetStatus") String assetStatus);

    /**
     * 批量更新资产质量等级
     */
    int batchUpdateAssetQualityLevel(@Param("assetIds") List<String> assetIds, @Param("qualityLevel") String qualityLevel);

    /**
     * 批量更新资产风险等级
     */
    int batchUpdateAssetRiskLevel(@Param("assetIds") List<String> assetIds, @Param("riskLevel") String riskLevel);

    /**
     * 批量更新资产价值
     */
    int batchUpdateAssetValue(@Param("list") List<AssetInfo> assetList);

    /**
     * 删除过期资产记录
     */
    int deleteExpiredAssetRecords(@Param("days") Integer days);

    /**
     * 获取资产统计概览
     */
    Map<String, Object> selectAssetStatisticsOverview();

    /**
     * 获取资产类型分布
     */
    List<Map<String, Object>> selectAssetTypeDistribution();

    /**
     * 获取资产性质分布
     */
    List<Map<String, Object>> selectAssetNatureDistribution();

    /**
     * 获取资产状态分布
     */
    List<Map<String, Object>> selectAssetStatusDistribution();

    /**
     * 获取资产质量分布
     */
    List<Map<String, Object>> selectAssetQualityDistribution();

    /**
     * 获取资产风险分布
     */
    List<Map<String, Object>> selectAssetRiskDistribution();

    /**
     * 导出资产信息列表
     */
    List<Map<String, Object>> exportAssetInfoList(AssetInfoQueryVO queryVO);

}
