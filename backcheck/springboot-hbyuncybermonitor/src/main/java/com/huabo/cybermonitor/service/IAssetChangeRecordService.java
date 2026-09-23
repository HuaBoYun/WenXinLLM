package com.huabo.cybermonitor.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.AssetChangeRecord;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.vo.AssetChangeRecordQueryVO;

/**
 * 资产变动记录服务接口
 * 
 * @author 华博云AI助手
 * @since 2024-12-12
 */
public interface IAssetChangeRecordService extends IService<AssetChangeRecord> {

    /**
     * 分页查询资产变动记录列表
     */
    PageResult<AssetChangeRecord> getAssetChangeRecordList(AssetChangeRecordQueryVO queryVO);

    /**
     * 根据资产ID查询变动记录
     */
    List<AssetChangeRecord> getChangeRecordsByAssetId(String assetId);

    /**
     * 根据企业ID查询变动记录
     */
    List<AssetChangeRecord> getChangeRecordsByEnterpriseId(String enterpriseId);

    /**
     * 根据变动类型查询记录
     */
    List<AssetChangeRecord> getChangeRecordsByType(String changeType);

    /**
     * 根据变动原因查询记录
     */
    List<AssetChangeRecord> getChangeRecordsByReason(String changeReason);

    /**
     * 根据审批状态查询记录
     */
    List<AssetChangeRecord> getChangeRecordsByApprovalStatus(String approvalStatus);

    /**
     * 根据预警级别查询记录
     */
    List<AssetChangeRecord> getChangeRecordsByWarningLevel(String warningLevel);

    /**
     * 查询重大变动记录
     */
    List<AssetChangeRecord> getMajorChangeRecords(Boolean isMajorChange);

    /**
     * 查询需要预警的变动记录
     */
    List<AssetChangeRecord> getWarningChangeRecords(Boolean needWarning);

    /**
     * 根据转出方查询记录
     */
    List<AssetChangeRecord> getChangeRecordsByTransferor(String transferor);

    /**
     * 根据转入方查询记录
     */
    List<AssetChangeRecord> getChangeRecordsByTransferee(String transferee);

    /**
     * 根据变动经办人查询记录
     */
    List<AssetChangeRecord> getChangeRecordsByOperator(String changeOperator);

    /**
     * 查询最新变动记录
     */
    List<AssetChangeRecord> getLatestChangeRecords(String enterpriseId, Integer limit);

    /**
     * 查询变动频繁的资产
     */
    List<Map<String, Object>> getFrequentChangeAssets(Integer days, Integer minChangeCount);

    /**
     * 资产变动监控
     */
    Map<String, Object> monitorAssetChanges(String enterpriseId, String startDate, String endDate);

    /**
     * 资产流向分析
     */
    Map<String, Object> analyzeAssetFlow(String enterpriseId, String startDate, String endDate);

    /**
     * 资产运营效率分析
     */
    Map<String, Object> analyzeAssetOperationalEfficiency(String enterpriseId, String startDate, String endDate);

    /**
     * 资产变动影响分析
     */
    Map<String, Object> analyzeAssetChangeImpact(String changeId);

    /**
     * 资产变动合规性检查
     */
    Map<String, Object> checkAssetChangeCompliance(String enterpriseId, String startDate, String endDate);

    /**
     * 资产变动风险评估
     */
    Map<String, Object> assessAssetChangeRisk(String enterpriseId, String startDate, String endDate);

    /**
     * 资产处置效益分析
     */
    Map<String, Object> analyzeAssetDisposalBenefit(String enterpriseId, String startDate, String endDate);

    /**
     * 资产购置分析
     */
    Map<String, Object> analyzeAssetAcquisition(String enterpriseId, String startDate, String endDate);

    /**
     * 资产转移分析
     */
    Map<String, Object> analyzeAssetTransfer(String enterpriseId, String startDate, String endDate);

    /**
     * 资产重估分析
     */
    Map<String, Object> analyzeAssetRevaluation(String enterpriseId, String startDate, String endDate);

    /**
     * 资产减值分析
     */
    Map<String, Object> analyzeAssetImpairment(String enterpriseId, String startDate, String endDate);

    /**
     * 按变动类型统计
     */
    List<Map<String, Object>> getChangeTypeStatistics(String startDate, String endDate);

    /**
     * 按变动原因统计
     */
    List<Map<String, Object>> getChangeReasonStatistics(String startDate, String endDate);

    /**
     * 按审批状态统计
     */
    List<Map<String, Object>> getApprovalStatusStatistics(String startDate, String endDate);

    /**
     * 按预警级别统计
     */
    List<Map<String, Object>> getWarningLevelStatistics(String startDate, String endDate);

    /**
     * 查询变动趋势
     */
    List<Map<String, Object>> getChangeTrend(String startDate, String endDate);

    /**
     * 查询变动金额趋势
     */
    List<Map<String, Object>> getChangeAmountTrend(String startDate, String endDate);

    /**
     * 批量更新审批状态
     */
    boolean batchUpdateApprovalStatus(List<String> changeIds, String approvalStatus);

    /**
     * 批量更新预警状态
     */
    boolean batchUpdateWarningStatus(List<String> changeIds, Boolean needWarning, String warningLevel);

    /**
     * 删除过期变动记录
     */
    boolean deleteExpiredChangeRecords(Integer days);

    /**
     * 获取变动统计概览
     */
    Map<String, Object> getChangeStatisticsOverview();

    /**
     * 获取变动类型分布
     */
    List<Map<String, Object>> getChangeTypeDistribution();

    /**
     * 获取变动原因分布
     */
    List<Map<String, Object>> getChangeReasonDistribution();

    /**
     * 获取审批状态分布
     */
    List<Map<String, Object>> getApprovalStatusDistribution();

    /**
     * 获取预警级别分布
     */
    List<Map<String, Object>> getWarningLevelDistribution();

    /**
     * 导出资产变动记录列表
     */
    List<Map<String, Object>> exportAssetChangeRecordList(AssetChangeRecordQueryVO queryVO);

    /**
     * 变动类型标签转换
     */
    String convertChangeTypeLabel(String changeType);

    /**
     * 变动原因标签转换
     */
    String convertChangeReasonLabel(String changeReason);

    /**
     * 转让方式标签转换
     */
    String convertTransferMethodLabel(String transferMethod);

    /**
     * 处置方式标签转换
     */
    String convertDisposalMethodLabel(String disposalMethod);

    /**
     * 审批状态标签转换
     */
    String convertApprovalStatusLabel(String approvalStatus);

    /**
     * 预警级别标签转换
     */
    String convertWarningLevelLabel(String warningLevel);

}
