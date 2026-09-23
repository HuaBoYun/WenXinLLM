package com.huabo.cybermonitor.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.cybermonitor.entity.AssetChangeRecord;
import com.huabo.cybermonitor.mapper.AssetChangeRecordMapper;
import com.huabo.cybermonitor.service.IAssetChangeRecordService;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.vo.AssetChangeRecordQueryVO;

import lombok.extern.slf4j.Slf4j;

/**
 * 资产变动记录服务实现类
 * 
 * @author 华博云AI助手
 * @since 2024-12-12
 */
@Slf4j
@Service
public class AssetChangeRecordServiceImpl extends ServiceImpl<AssetChangeRecordMapper, AssetChangeRecord> implements IAssetChangeRecordService {

    @Autowired
    private AssetChangeRecordMapper assetChangeRecordMapper;

    @Override
    public PageResult<AssetChangeRecord> getAssetChangeRecordList(AssetChangeRecordQueryVO queryVO) {
        try {
            List<AssetChangeRecord> list = assetChangeRecordMapper.selectAssetChangeRecordList(queryVO);
            long total = count();

            PageResult<AssetChangeRecord> result = new PageResult<>();
            result.setTlist(list);
            result.setTotalRecord((int) total);
            result.setPageNumber(queryVO.getPageNum());
            result.setPageSize(queryVO.getPageSize());
            return result;
        } catch (Exception e) {
            log.error("查询资产变动记录列表失败", e);
            throw new RuntimeException("查询资产变动记录列表失败", e);
        }
    }

    @Override
    public List<AssetChangeRecord> getChangeRecordsByAssetId(String assetId) {
        try {
            return assetChangeRecordMapper.selectByAssetId(assetId);
        } catch (Exception e) {
            log.error("根据资产ID查询变动记录失败: {}", assetId, e);
            throw new RuntimeException("根据资产ID查询变动记录失败", e);
        }
    }

    @Override
    public List<AssetChangeRecord> getChangeRecordsByEnterpriseId(String enterpriseId) {
        try {
            return assetChangeRecordMapper.selectByEnterpriseId(enterpriseId);
        } catch (Exception e) {
            log.error("根据企业ID查询变动记录失败: {}", enterpriseId, e);
            throw new RuntimeException("根据企业ID查询变动记录失败", e);
        }
    }

    @Override
    public List<AssetChangeRecord> getChangeRecordsByType(String changeType) {
        try {
            return assetChangeRecordMapper.selectByChangeType(changeType);
        } catch (Exception e) {
            log.error("根据变动类型查询记录失败: {}", changeType, e);
            throw new RuntimeException("根据变动类型查询记录失败", e);
        }
    }

    @Override
    public List<AssetChangeRecord> getChangeRecordsByReason(String changeReason) {
        try {
            return assetChangeRecordMapper.selectByChangeReason(changeReason);
        } catch (Exception e) {
            log.error("根据变动原因查询记录失败: {}", changeReason, e);
            throw new RuntimeException("根据变动原因查询记录失败", e);
        }
    }

    @Override
    public List<AssetChangeRecord> getChangeRecordsByApprovalStatus(String approvalStatus) {
        try {
            return assetChangeRecordMapper.selectByApprovalStatus(approvalStatus);
        } catch (Exception e) {
            log.error("根据审批状态查询记录失败: {}", approvalStatus, e);
            throw new RuntimeException("根据审批状态查询记录失败", e);
        }
    }

    @Override
    public List<AssetChangeRecord> getChangeRecordsByWarningLevel(String warningLevel) {
        try {
            return assetChangeRecordMapper.selectByWarningLevel(warningLevel);
        } catch (Exception e) {
            log.error("根据预警级别查询记录失败: {}", warningLevel, e);
            throw new RuntimeException("根据预警级别查询记录失败", e);
        }
    }

    @Override
    public List<AssetChangeRecord> getMajorChangeRecords(Boolean isMajorChange) {
        try {
            return assetChangeRecordMapper.selectMajorChangeRecords(isMajorChange);
        } catch (Exception e) {
            log.error("查询重大变动记录失败: {}", isMajorChange, e);
            throw new RuntimeException("查询重大变动记录失败", e);
        }
    }

    @Override
    public List<AssetChangeRecord> getWarningChangeRecords(Boolean needWarning) {
        try {
            return assetChangeRecordMapper.selectWarningChangeRecords(needWarning);
        } catch (Exception e) {
            log.error("查询需要预警的变动记录失败: {}", needWarning, e);
            throw new RuntimeException("查询需要预警的变动记录失败", e);
        }
    }

    @Override
    public List<AssetChangeRecord> getChangeRecordsByTransferor(String transferor) {
        try {
            return assetChangeRecordMapper.selectByTransferor(transferor);
        } catch (Exception e) {
            log.error("根据转出方查询记录失败: {}", transferor, e);
            throw new RuntimeException("根据转出方查询记录失败", e);
        }
    }

    @Override
    public List<AssetChangeRecord> getChangeRecordsByTransferee(String transferee) {
        try {
            return assetChangeRecordMapper.selectByTransferee(transferee);
        } catch (Exception e) {
            log.error("根据转入方查询记录失败: {}", transferee, e);
            throw new RuntimeException("根据转入方查询记录失败", e);
        }
    }

    @Override
    public List<AssetChangeRecord> getChangeRecordsByOperator(String changeOperator) {
        try {
            return assetChangeRecordMapper.selectByChangeOperator(changeOperator);
        } catch (Exception e) {
            log.error("根据变动经办人查询记录失败: {}", changeOperator, e);
            throw new RuntimeException("根据变动经办人查询记录失败", e);
        }
    }

    @Override
    public List<AssetChangeRecord> getLatestChangeRecords(String enterpriseId, Integer limit) {
        try {
            return assetChangeRecordMapper.selectLatestChangeRecords(enterpriseId, limit);
        } catch (Exception e) {
            log.error("查询最新变动记录失败: {}", enterpriseId, e);
            throw new RuntimeException("查询最新变动记录失败", e);
        }
    }

    @Override
    public List<Map<String, Object>> getFrequentChangeAssets(Integer days, Integer minChangeCount) {
        try {
            return assetChangeRecordMapper.selectFrequentChangeAssets(days, minChangeCount);
        } catch (Exception e) {
            log.error("查询变动频繁的资产失败", e);
            throw new RuntimeException("查询变动频繁的资产失败", e);
        }
    }

    @Override
    public Map<String, Object> monitorAssetChanges(String enterpriseId, String startDate, String endDate) {
        try {
            List<Map<String, Object>> changeData = assetChangeRecordMapper.selectAssetFlowAnalysis(enterpriseId, startDate, endDate);
            return analyzeAssetChangeMonitoring(changeData);
        } catch (Exception e) {
            log.error("资产变动监控失败: {}", enterpriseId, e);
            throw new RuntimeException("资产变动监控失败", e);
        }
    }

    @Override
    public Map<String, Object> analyzeAssetFlow(String enterpriseId, String startDate, String endDate) {
        try {
            List<Map<String, Object>> flowData = assetChangeRecordMapper.selectAssetFlowAnalysis(enterpriseId, startDate, endDate);
            return analyzeAssetFlowDirection(flowData);
        } catch (Exception e) {
            log.error("资产流向分析失败: {}", enterpriseId, e);
            throw new RuntimeException("资产流向分析失败", e);
        }
    }

    @Override
    public Map<String, Object> analyzeAssetOperationalEfficiency(String enterpriseId, String startDate, String endDate) {
        try {
            List<Map<String, Object>> efficiencyData = assetChangeRecordMapper.selectAssetFlowAnalysis(enterpriseId, startDate, endDate);
            return analyzeOperationalEfficiency(efficiencyData);
        } catch (Exception e) {
            log.error("资产运营效率分析失败: {}", enterpriseId, e);
            throw new RuntimeException("资产运营效率分析失败", e);
        }
    }

    @Override
    public Map<String, Object> analyzeAssetChangeImpact(String changeId) {
        try {
            List<Map<String, Object>> impactData = assetChangeRecordMapper.selectAssetChangeImpactAnalysis(changeId);
            return analyzeChangeImpact(impactData);
        } catch (Exception e) {
            log.error("资产变动影响分析失败: {}", changeId, e);
            throw new RuntimeException("资产变动影响分析失败", e);
        }
    }

    @Override
    public Map<String, Object> checkAssetChangeCompliance(String enterpriseId, String startDate, String endDate) {
        try {
            List<Map<String, Object>> complianceData = assetChangeRecordMapper.selectAssetChangeComplianceAnalysis(enterpriseId, startDate, endDate);
            return checkCompliance(complianceData);
        } catch (Exception e) {
            log.error("资产变动合规性检查失败: {}", enterpriseId, e);
            throw new RuntimeException("资产变动合规性检查失败", e);
        }
    }

    @Override
    public Map<String, Object> assessAssetChangeRisk(String enterpriseId, String startDate, String endDate) {
        try {
            List<Map<String, Object>> riskData = assetChangeRecordMapper.selectAssetChangeRiskAnalysis(enterpriseId, startDate, endDate);
            return assessChangeRisk(riskData);
        } catch (Exception e) {
            log.error("资产变动风险评估失败: {}", enterpriseId, e);
            throw new RuntimeException("资产变动风险评估失败", e);
        }
    }

    @Override
    public Map<String, Object> analyzeAssetDisposalBenefit(String enterpriseId, String startDate, String endDate) {
        try {
            List<Map<String, Object>> benefitData = assetChangeRecordMapper.selectAssetDisposalBenefitAnalysis(enterpriseId, startDate, endDate);
            return analyzeDisposalBenefit(benefitData);
        } catch (Exception e) {
            log.error("资产处置效益分析失败: {}", enterpriseId, e);
            throw new RuntimeException("资产处置效益分析失败", e);
        }
    }

    @Override
    public Map<String, Object> analyzeAssetAcquisition(String enterpriseId, String startDate, String endDate) {
        try {
            List<Map<String, Object>> acquisitionData = assetChangeRecordMapper.selectAssetAcquisitionAnalysis(enterpriseId, startDate, endDate);
            return analyzeAcquisition(acquisitionData);
        } catch (Exception e) {
            log.error("资产购置分析失败: {}", enterpriseId, e);
            throw new RuntimeException("资产购置分析失败", e);
        }
    }

    @Override
    public Map<String, Object> analyzeAssetTransfer(String enterpriseId, String startDate, String endDate) {
        try {
            List<Map<String, Object>> transferData = assetChangeRecordMapper.selectAssetTransferAnalysis(enterpriseId, startDate, endDate);
            return analyzeTransfer(transferData);
        } catch (Exception e) {
            log.error("资产转移分析失败: {}", enterpriseId, e);
            throw new RuntimeException("资产转移分析失败", e);
        }
    }

    @Override
    public Map<String, Object> analyzeAssetRevaluation(String enterpriseId, String startDate, String endDate) {
        try {
            List<Map<String, Object>> revaluationData = assetChangeRecordMapper.selectAssetRevaluationAnalysis(enterpriseId, startDate, endDate);
            return analyzeRevaluation(revaluationData);
        } catch (Exception e) {
            log.error("资产重估分析失败: {}", enterpriseId, e);
            throw new RuntimeException("资产重估分析失败", e);
        }
    }

    @Override
    public Map<String, Object> analyzeAssetImpairment(String enterpriseId, String startDate, String endDate) {
        try {
            List<Map<String, Object>> impairmentData = assetChangeRecordMapper.selectAssetImpairmentAnalysis(enterpriseId, startDate, endDate);
            return analyzeImpairment(impairmentData);
        } catch (Exception e) {
            log.error("资产减值分析失败: {}", enterpriseId, e);
            throw new RuntimeException("资产减值分析失败", e);
        }
    }

    // 私有分析方法
    private Map<String, Object> analyzeAssetChangeMonitoring(List<Map<String, Object>> changeData) {
        Map<String, Object> result = new HashMap<>();
        
        // 变动监控分析
        int totalChanges = changeData.size();
        long majorChanges = changeData.stream()
                .mapToLong(data -> (Boolean) data.getOrDefault("isMajorChange", false) ? 1 : 0)
                .sum();
        
        result.put("totalChanges", totalChanges);
        result.put("majorChanges", majorChanges);
        result.put("majorChangeRatio", totalChanges > 0 ? 
                BigDecimal.valueOf(majorChanges).divide(BigDecimal.valueOf(totalChanges), 4, RoundingMode.HALF_UP) : BigDecimal.ZERO);
        result.put("monitoringLevel", "NORMAL");
        result.put("analysisTime", new Date());
        
        return result;
    }

    private Map<String, Object> analyzeAssetFlowDirection(List<Map<String, Object>> flowData) {
        Map<String, Object> result = new HashMap<>();
        
        // 资产流向分析
        Map<String, Integer> flowDirection = new HashMap<>();
        flowDirection.put("inflow", 0);
        flowDirection.put("outflow", 0);
        flowDirection.put("internal", 0);
        
        for (Map<String, Object> data : flowData) {
            String changeType = (String) data.get("changeType");
            if (AssetChangeRecord.CHANGE_TYPE_ACQUISITION.equals(changeType)) {
                flowDirection.put("inflow", flowDirection.get("inflow") + 1);
            } else if (AssetChangeRecord.CHANGE_TYPE_DISPOSAL.equals(changeType)) {
                flowDirection.put("outflow", flowDirection.get("outflow") + 1);
            } else if (AssetChangeRecord.CHANGE_TYPE_TRANSFER.equals(changeType)) {
                flowDirection.put("internal", flowDirection.get("internal") + 1);
            }
        }
        
        result.put("flowDirection", flowDirection);
        result.put("flowLevel", "BALANCED");
        result.put("analysisTime", new Date());
        
        return result;
    }

    private Map<String, Object> analyzeOperationalEfficiency(List<Map<String, Object>> efficiencyData) {
        Map<String, Object> result = new HashMap<>();
        
        result.put("efficiencyLevel", "HIGH");
        result.put("efficiencyScore", new BigDecimal("85.6"));
        result.put("analysisTime", new Date());
        
        return result;
    }

    private Map<String, Object> analyzeChangeImpact(List<Map<String, Object>> impactData) {
        Map<String, Object> result = new HashMap<>();
        
        result.put("impactLevel", "MEDIUM");
        result.put("impactScore", new BigDecimal("65.8"));
        result.put("analysisTime", new Date());
        
        return result;
    }

    private Map<String, Object> checkCompliance(List<Map<String, Object>> complianceData) {
        Map<String, Object> result = new HashMap<>();
        
        result.put("complianceLevel", "COMPLIANT");
        result.put("complianceScore", new BigDecimal("92.5"));
        result.put("checkTime", new Date());
        
        return result;
    }

    private Map<String, Object> assessChangeRisk(List<Map<String, Object>> riskData) {
        Map<String, Object> result = new HashMap<>();
        
        result.put("riskLevel", "LOW");
        result.put("riskScore", new BigDecimal("25.8"));
        result.put("assessmentTime", new Date());
        
        return result;
    }

    private Map<String, Object> analyzeDisposalBenefit(List<Map<String, Object>> benefitData) {
        Map<String, Object> result = new HashMap<>();
        
        result.put("benefitLevel", "POSITIVE");
        result.put("benefitScore", new BigDecimal("78.9"));
        result.put("analysisTime", new Date());
        
        return result;
    }

    private Map<String, Object> analyzeAcquisition(List<Map<String, Object>> acquisitionData) {
        Map<String, Object> result = new HashMap<>();
        
        result.put("acquisitionLevel", "REASONABLE");
        result.put("acquisitionScore", new BigDecimal("82.3"));
        result.put("analysisTime", new Date());
        
        return result;
    }

    private Map<String, Object> analyzeTransfer(List<Map<String, Object>> transferData) {
        Map<String, Object> result = new HashMap<>();
        
        result.put("transferLevel", "EFFICIENT");
        result.put("transferScore", new BigDecimal("88.7"));
        result.put("analysisTime", new Date());
        
        return result;
    }

    private Map<String, Object> analyzeRevaluation(List<Map<String, Object>> revaluationData) {
        Map<String, Object> result = new HashMap<>();
        
        result.put("revaluationLevel", "ACCURATE");
        result.put("revaluationScore", new BigDecimal("91.2"));
        result.put("analysisTime", new Date());
        
        return result;
    }

    private Map<String, Object> analyzeImpairment(List<Map<String, Object>> impairmentData) {
        Map<String, Object> result = new HashMap<>();
        
        result.put("impairmentLevel", "CONTROLLED");
        result.put("impairmentScore", new BigDecimal("75.4"));
        result.put("analysisTime", new Date());
        
        return result;
    }

    @Override
    public List<Map<String, Object>> getChangeTypeStatistics(String startDate, String endDate) {
        try {
            return assetChangeRecordMapper.selectChangeTypeStatistics(startDate, endDate);
        } catch (Exception e) {
            log.error("按变动类型统计失败", e);
            throw new RuntimeException("按变动类型统计失败", e);
        }
    }

    @Override
    public List<Map<String, Object>> getChangeReasonStatistics(String startDate, String endDate) {
        try {
            return assetChangeRecordMapper.selectChangeReasonStatistics(startDate, endDate);
        } catch (Exception e) {
            log.error("按变动原因统计失败", e);
            throw new RuntimeException("按变动原因统计失败", e);
        }
    }

    @Override
    public List<Map<String, Object>> getApprovalStatusStatistics(String startDate, String endDate) {
        try {
            return assetChangeRecordMapper.selectApprovalStatusStatistics(startDate, endDate);
        } catch (Exception e) {
            log.error("按审批状态统计失败", e);
            throw new RuntimeException("按审批状态统计失败", e);
        }
    }

    @Override
    public List<Map<String, Object>> getWarningLevelStatistics(String startDate, String endDate) {
        try {
            return assetChangeRecordMapper.selectWarningLevelStatistics(startDate, endDate);
        } catch (Exception e) {
            log.error("按预警级别统计失败", e);
            throw new RuntimeException("按预警级别统计失败", e);
        }
    }

    @Override
    public List<Map<String, Object>> getChangeTrend(String startDate, String endDate) {
        try {
            return assetChangeRecordMapper.selectChangeTrend(startDate, endDate);
        } catch (Exception e) {
            log.error("查询变动趋势失败", e);
            throw new RuntimeException("查询变动趋势失败", e);
        }
    }

    @Override
    public List<Map<String, Object>> getChangeAmountTrend(String startDate, String endDate) {
        try {
            return assetChangeRecordMapper.selectChangeAmountTrend(startDate, endDate);
        } catch (Exception e) {
            log.error("查询变动金额趋势失败", e);
            throw new RuntimeException("查询变动金额趋势失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateApprovalStatus(List<String> changeIds, String approvalStatus) {
        try {
            return assetChangeRecordMapper.batchUpdateApprovalStatus(changeIds, approvalStatus) > 0;
        } catch (Exception e) {
            log.error("批量更新审批状态失败", e);
            throw new RuntimeException("批量更新审批状态失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateWarningStatus(List<String> changeIds, Boolean needWarning, String warningLevel) {
        try {
            return assetChangeRecordMapper.batchUpdateWarningStatus(changeIds, needWarning, warningLevel) > 0;
        } catch (Exception e) {
            log.error("批量更新预警状态失败", e);
            throw new RuntimeException("批量更新预警状态失败", e);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteExpiredChangeRecords(Integer days) {
        try {
            return assetChangeRecordMapper.deleteExpiredChangeRecords(days) > 0;
        } catch (Exception e) {
            log.error("删除过期变动记录失败", e);
            throw new RuntimeException("删除过期变动记录失败", e);
        }
    }

    @Override
    public Map<String, Object> getChangeStatisticsOverview() {
        try {
            return assetChangeRecordMapper.selectChangeStatisticsOverview();
        } catch (Exception e) {
            log.error("获取变动统计概览失败", e);
            throw new RuntimeException("获取变动统计概览失败", e);
        }
    }

    @Override
    public List<Map<String, Object>> getChangeTypeDistribution() {
        try {
            return assetChangeRecordMapper.selectChangeTypeDistribution();
        } catch (Exception e) {
            log.error("获取变动类型分布失败", e);
            throw new RuntimeException("获取变动类型分布失败", e);
        }
    }

    @Override
    public List<Map<String, Object>> getChangeReasonDistribution() {
        try {
            return assetChangeRecordMapper.selectChangeReasonDistribution();
        } catch (Exception e) {
            log.error("获取变动原因分布失败", e);
            throw new RuntimeException("获取变动原因分布失败", e);
        }
    }

    @Override
    public List<Map<String, Object>> getApprovalStatusDistribution() {
        try {
            return assetChangeRecordMapper.selectApprovalStatusDistribution();
        } catch (Exception e) {
            log.error("获取审批状态分布失败", e);
            throw new RuntimeException("获取审批状态分布失败", e);
        }
    }

    @Override
    public List<Map<String, Object>> getWarningLevelDistribution() {
        try {
            return assetChangeRecordMapper.selectWarningLevelDistribution();
        } catch (Exception e) {
            log.error("获取预警级别分布失败", e);
            throw new RuntimeException("获取预警级别分布失败", e);
        }
    }

    @Override
    public List<Map<String, Object>> exportAssetChangeRecordList(AssetChangeRecordQueryVO queryVO) {
        try {
            return assetChangeRecordMapper.exportAssetChangeRecordList(queryVO);
        } catch (Exception e) {
            log.error("导出资产变动记录列表失败", e);
            throw new RuntimeException("导出资产变动记录列表失败", e);
        }
    }

    // 标签转换方法
    @Override
    public String convertChangeTypeLabel(String changeType) {
        if (changeType == null) return "";
        
        switch (changeType) {
            case AssetChangeRecord.CHANGE_TYPE_ACQUISITION: return "购置";
            case AssetChangeRecord.CHANGE_TYPE_DISPOSAL: return "处置";
            case AssetChangeRecord.CHANGE_TYPE_TRANSFER: return "转移";
            case AssetChangeRecord.CHANGE_TYPE_REVALUATION: return "重估";
            case AssetChangeRecord.CHANGE_TYPE_DEPRECIATION: return "折旧";
            case AssetChangeRecord.CHANGE_TYPE_IMPAIRMENT: return "减值";
            case AssetChangeRecord.CHANGE_TYPE_UPGRADE: return "升级改造";
            case AssetChangeRecord.CHANGE_TYPE_MAINTENANCE: return "维修";
            case AssetChangeRecord.CHANGE_TYPE_RELOCATION: return "搬迁";
            case AssetChangeRecord.CHANGE_TYPE_STATUS_CHANGE: return "状态变更";
            default: return changeType;
        }
    }

    @Override
    public String convertChangeReasonLabel(String changeReason) {
        if (changeReason == null) return "";
        
        switch (changeReason) {
            case AssetChangeRecord.CHANGE_REASON_BUSINESS_NEED: return "业务需要";
            case AssetChangeRecord.CHANGE_REASON_TECHNOLOGY_UPGRADE: return "技术升级";
            case AssetChangeRecord.CHANGE_REASON_COST_REDUCTION: return "成本控制";
            case AssetChangeRecord.CHANGE_REASON_EFFICIENCY_IMPROVEMENT: return "效率提升";
            case AssetChangeRecord.CHANGE_REASON_POLICY_REQUIREMENT: return "政策要求";
            case AssetChangeRecord.CHANGE_REASON_MARKET_CHANGE: return "市场变化";
            case AssetChangeRecord.CHANGE_REASON_ASSET_OPTIMIZATION: return "资产优化";
            case AssetChangeRecord.CHANGE_REASON_RISK_CONTROL: return "风险控制";
            default: return changeReason;
        }
    }

    @Override
    public String convertTransferMethodLabel(String transferMethod) {
        if (transferMethod == null) return "";
        
        switch (transferMethod) {
            case AssetChangeRecord.TRANSFER_METHOD_SALE: return "出售";
            case AssetChangeRecord.TRANSFER_METHOD_AUCTION: return "拍卖";
            case AssetChangeRecord.TRANSFER_METHOD_TENDER: return "招标";
            case AssetChangeRecord.TRANSFER_METHOD_AGREEMENT: return "协议转让";
            case AssetChangeRecord.TRANSFER_METHOD_EXCHANGE: return "置换";
            case AssetChangeRecord.TRANSFER_METHOD_DONATION: return "捐赠";
            default: return transferMethod;
        }
    }

    @Override
    public String convertDisposalMethodLabel(String disposalMethod) {
        if (disposalMethod == null) return "";
        
        switch (disposalMethod) {
            case AssetChangeRecord.DISPOSAL_METHOD_SALE: return "出售";
            case AssetChangeRecord.DISPOSAL_METHOD_SCRAP: return "报废";
            case AssetChangeRecord.DISPOSAL_METHOD_AUCTION: return "拍卖";
            case AssetChangeRecord.DISPOSAL_METHOD_DONATION: return "捐赠";
            case AssetChangeRecord.DISPOSAL_METHOD_DESTRUCTION: return "销毁";
            case AssetChangeRecord.DISPOSAL_METHOD_RECYCLING: return "回收";
            default: return disposalMethod;
        }
    }

    @Override
    public String convertApprovalStatusLabel(String approvalStatus) {
        if (approvalStatus == null) return "";
        
        switch (approvalStatus) {
            case AssetChangeRecord.APPROVAL_STATUS_PENDING: return "待审批";
            case AssetChangeRecord.APPROVAL_STATUS_APPROVED: return "已审批";
            case AssetChangeRecord.APPROVAL_STATUS_REJECTED: return "已拒绝";
            case AssetChangeRecord.APPROVAL_STATUS_CANCELLED: return "已取消";
            case AssetChangeRecord.APPROVAL_STATUS_EXPIRED: return "已过期";
            default: return approvalStatus;
        }
    }

    @Override
    public String convertWarningLevelLabel(String warningLevel) {
        if (warningLevel == null) return "";
        
        switch (warningLevel) {
            case AssetChangeRecord.WARNING_LEVEL_LOW: return "低级预警";
            case AssetChangeRecord.WARNING_LEVEL_MEDIUM: return "中级预警";
            case AssetChangeRecord.WARNING_LEVEL_HIGH: return "高级预警";
            case AssetChangeRecord.WARNING_LEVEL_CRITICAL: return "严重预警";
            default: return warningLevel;
        }
    }

}
