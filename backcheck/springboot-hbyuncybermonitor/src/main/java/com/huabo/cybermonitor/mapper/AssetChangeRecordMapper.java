package com.huabo.cybermonitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.cybermonitor.entity.AssetChangeRecord;
import com.huabo.cybermonitor.vo.AssetChangeRecordQueryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 资产变动记录数据访问接口
 * 
 * @author 华博云AI助手
 * @since 2024-12-12
 */
@Mapper
public interface AssetChangeRecordMapper extends BaseMapper<AssetChangeRecord> {

    /**
     * 分页查询资产变动记录列表
     */
    List<AssetChangeRecord> selectAssetChangeRecordList(AssetChangeRecordQueryVO queryVO);

    /**
     * 根据资产ID查询变动记录
     */
    List<AssetChangeRecord> selectByAssetId(@Param("assetId") String assetId);

    /**
     * 根据企业ID查询变动记录
     */
    List<AssetChangeRecord> selectByEnterpriseId(@Param("enterpriseId") String enterpriseId);

    /**
     * 根据变动类型查询记录
     */
    List<AssetChangeRecord> selectByChangeType(@Param("changeType") String changeType);

    /**
     * 根据变动原因查询记录
     */
    List<AssetChangeRecord> selectByChangeReason(@Param("changeReason") String changeReason);

    /**
     * 根据审批状态查询记录
     */
    List<AssetChangeRecord> selectByApprovalStatus(@Param("approvalStatus") String approvalStatus);

    /**
     * 根据预警级别查询记录
     */
    List<AssetChangeRecord> selectByWarningLevel(@Param("warningLevel") String warningLevel);

    /**
     * 查询重大变动记录
     */
    List<AssetChangeRecord> selectMajorChangeRecords(@Param("isMajorChange") Boolean isMajorChange);

    /**
     * 查询需要预警的变动记录
     */
    List<AssetChangeRecord> selectWarningChangeRecords(@Param("needWarning") Boolean needWarning);

    /**
     * 根据转出方查询记录
     */
    List<AssetChangeRecord> selectByTransferor(@Param("transferor") String transferor);

    /**
     * 根据转入方查询记录
     */
    List<AssetChangeRecord> selectByTransferee(@Param("transferee") String transferee);

    /**
     * 根据变动经办人查询记录
     */
    List<AssetChangeRecord> selectByChangeOperator(@Param("changeOperator") String changeOperator);

    /**
     * 查询最新变动记录
     */
    List<AssetChangeRecord> selectLatestChangeRecords(@Param("enterpriseId") String enterpriseId, @Param("limit") Integer limit);

    /**
     * 查询变动频繁的资产
     */
    List<Map<String, Object>> selectFrequentChangeAssets(@Param("days") Integer days, @Param("minChangeCount") Integer minChangeCount);

    /**
     * 按变动类型统计
     */
    List<Map<String, Object>> selectChangeTypeStatistics(@Param("startDate") String startDate, @Param("endDate") String endDate);

    /**
     * 按变动原因统计
     */
    List<Map<String, Object>> selectChangeReasonStatistics(@Param("startDate") String startDate, @Param("endDate") String endDate);

    /**
     * 按审批状态统计
     */
    List<Map<String, Object>> selectApprovalStatusStatistics(@Param("startDate") String startDate, @Param("endDate") String endDate);

    /**
     * 按预警级别统计
     */
    List<Map<String, Object>> selectWarningLevelStatistics(@Param("startDate") String startDate, @Param("endDate") String endDate);

    /**
     * 查询变动趋势
     */
    List<Map<String, Object>> selectChangeTrend(@Param("startDate") String startDate, @Param("endDate") String endDate);

    /**
     * 查询变动金额趋势
     */
    List<Map<String, Object>> selectChangeAmountTrend(@Param("startDate") String startDate, @Param("endDate") String endDate);

    /**
     * 查询资产流向分析
     */
    List<Map<String, Object>> selectAssetFlowAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                     @Param("startDate") String startDate, 
                                                     @Param("endDate") String endDate);

    /**
     * 查询资产处置效益分析
     */
    List<Map<String, Object>> selectAssetDisposalBenefitAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                                @Param("startDate") String startDate, 
                                                                @Param("endDate") String endDate);

    /**
     * 查询资产购置分析
     */
    List<Map<String, Object>> selectAssetAcquisitionAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                            @Param("startDate") String startDate, 
                                                            @Param("endDate") String endDate);

    /**
     * 查询资产转移分析
     */
    List<Map<String, Object>> selectAssetTransferAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                         @Param("startDate") String startDate, 
                                                         @Param("endDate") String endDate);

    /**
     * 查询资产重估分析
     */
    List<Map<String, Object>> selectAssetRevaluationAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                            @Param("startDate") String startDate, 
                                                            @Param("endDate") String endDate);

    /**
     * 查询资产减值分析
     */
    List<Map<String, Object>> selectAssetImpairmentAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                           @Param("startDate") String startDate, 
                                                           @Param("endDate") String endDate);

    /**
     * 查询资产变动影响分析
     */
    List<Map<String, Object>> selectAssetChangeImpactAnalysis(@Param("changeId") String changeId);

    /**
     * 查询资产变动合规性分析
     */
    List<Map<String, Object>> selectAssetChangeComplianceAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                                 @Param("startDate") String startDate, 
                                                                 @Param("endDate") String endDate);

    /**
     * 查询资产变动风险分析
     */
    List<Map<String, Object>> selectAssetChangeRiskAnalysis(@Param("enterpriseId") String enterpriseId, 
                                                           @Param("startDate") String startDate, 
                                                           @Param("endDate") String endDate);

    /**
     * 批量更新审批状态
     */
    int batchUpdateApprovalStatus(@Param("changeIds") List<String> changeIds, @Param("approvalStatus") String approvalStatus);

    /**
     * 批量更新预警状态
     */
    int batchUpdateWarningStatus(@Param("changeIds") List<String> changeIds, 
                                @Param("needWarning") Boolean needWarning, 
                                @Param("warningLevel") String warningLevel);

    /**
     * 删除过期变动记录
     */
    int deleteExpiredChangeRecords(@Param("days") Integer days);

    /**
     * 获取变动统计概览
     */
    Map<String, Object> selectChangeStatisticsOverview();

    /**
     * 获取变动类型分布
     */
    List<Map<String, Object>> selectChangeTypeDistribution();

    /**
     * 获取变动原因分布
     */
    List<Map<String, Object>> selectChangeReasonDistribution();

    /**
     * 获取审批状态分布
     */
    List<Map<String, Object>> selectApprovalStatusDistribution();

    /**
     * 获取预警级别分布
     */
    List<Map<String, Object>> selectWarningLevelDistribution();

    /**
     * 导出资产变动记录列表
     */
    List<Map<String, Object>> exportAssetChangeRecordList(AssetChangeRecordQueryVO queryVO);

}
