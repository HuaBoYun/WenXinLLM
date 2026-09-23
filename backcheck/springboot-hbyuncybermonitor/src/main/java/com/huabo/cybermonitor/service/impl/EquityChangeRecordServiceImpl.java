package com.huabo.cybermonitor.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.huabo.cybermonitor.entity.EquityChangeRecord;
import com.huabo.cybermonitor.mapper.EquityChangeRecordMapper;
import com.huabo.cybermonitor.service.IEquityChangeRecordService;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.vo.EquityChangeRecordQueryVO;

import lombok.extern.slf4j.Slf4j;

/**
 * 股权变动记录服务实现类
 *
 * @author system
 * @since 2024-01-01
 */
@Slf4j
@Service
public class EquityChangeRecordServiceImpl extends ServiceImpl<EquityChangeRecordMapper, EquityChangeRecord> implements IEquityChangeRecordService {

    @Autowired
    private EquityChangeRecordMapper equityChangeRecordMapper;

    @Override
    public PageResult<EquityChangeRecord> getEquityChangeRecordList(EquityChangeRecordQueryVO queryVO) {
        try {
            List<EquityChangeRecord> list = equityChangeRecordMapper.selectEquityChangeRecordList(queryVO);

            QueryWrapper<EquityChangeRecord> countWrapper = new QueryWrapper<>();
            if (queryVO.getInvesteeEnterpriseId() != null) {
                countWrapper.eq("INVESTEE_ENTERPRISE_ID", queryVO.getInvesteeEnterpriseId());
            }
            long total = this.count(countWrapper);

            return null;
        } catch (Exception e) {
            log.error("查询股权变动记录列表失败", e);
            return null;
        }
    }

    @Override
    public EquityChangeRecord getEquityChangeRecordById(String changeId) {
        try {
            return this.getById(changeId);
        } catch (Exception e) {
            log.error("根据ID获取股权变动记录详情失败，changeId: {}", changeId, e);
            return null;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addEquityChangeRecord(EquityChangeRecord equityChangeRecord) {
        try {
            equityChangeRecord.setCreateTime(LocalDateTime.now());
            equityChangeRecord.setUpdateTime(LocalDateTime.now());
            
            // 进行变动影响分析
            this.analyzeChangeImpact(equityChangeRecord);
            
            // 进行合规性检查
            this.checkCompliance(equityChangeRecord);
            
            // 进行预警检查
            this.checkWarning(equityChangeRecord);
            
            return this.save(equityChangeRecord);
        } catch (Exception e) {
            log.error("新增股权变动记录失败", e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateEquityChangeRecord(EquityChangeRecord equityChangeRecord) {
        try {
            equityChangeRecord.setUpdateTime(LocalDateTime.now());
            return this.updateById(equityChangeRecord);
        } catch (Exception e) {
            log.error("更新股权变动记录失败", e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteEquityChangeRecord(String changeId) {
        try {
            return this.removeById(changeId);
        } catch (Exception e) {
            log.error("删除股权变动记录失败，changeId: {}", changeId, e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteEquityChangeRecord(List<String> changeIds) {
        try {
            return this.removeByIds(changeIds);
        } catch (Exception e) {
            log.error("批量删除股权变动记录失败", e);
            return false;
        }
    }

    @Override
    public List<EquityChangeRecord> getEquityChangeRecordByEquityId(String equityId) {
        try {
            return equityChangeRecordMapper.selectByEquityId(equityId);
        } catch (Exception e) {
            log.error("根据股权结构ID查询变动记录失败，equityId: {}", equityId, e);
            return null;
        }
    }

    @Override
    public List<EquityChangeRecord> getEquityChangeRecordByInvesteeEnterpriseId(String investeeEnterpriseId) {
        try {
            return equityChangeRecordMapper.selectByInvesteeEnterpriseId(investeeEnterpriseId);
        } catch (Exception e) {
            log.error("根据被投资企业ID查询变动记录失败，investeeEnterpriseId: {}", investeeEnterpriseId, e);
            return null;
        }
    }

    @Override
    public List<EquityChangeRecord> getEquityChangeRecordByInvestorEnterpriseId(String investorEnterpriseId) {
        try {
            return equityChangeRecordMapper.selectByInvestorEnterpriseId(investorEnterpriseId);
        } catch (Exception e) {
            log.error("根据投资方企业ID查询变动记录失败，investorEnterpriseId: {}", investorEnterpriseId, e);
            return null;
        }
    }

    @Override
    public List<EquityChangeRecord> getMajorChangeRecords(Boolean isMajorChange) {
        try {
            return equityChangeRecordMapper.selectMajorChangeRecords(isMajorChange);
        } catch (Exception e) {
            log.error("查询重大变动记录失败，isMajorChange: {}", isMajorChange, e);
            return null;
        }
    }

    @Override
    public List<EquityChangeRecord> getWarningChangeRecords(Boolean needWarning) {
        try {
            return equityChangeRecordMapper.selectWarningChangeRecords(needWarning);
        } catch (Exception e) {
            log.error("查询需要预警的变动记录失败，needWarning: {}", needWarning, e);
            return null;
        }
    }

    @Override
    public List<EquityChangeRecord> getEquityChangeRecordByChangeType(String changeType) {
        try {
            return equityChangeRecordMapper.selectByChangeType(changeType);
        } catch (Exception e) {
            log.error("按变动类型查询记录失败，changeType: {}", changeType, e);
            return null;
        }
    }

    @Override
    public List<EquityChangeRecord> getEquityChangeRecordByApprovalStatus(String approvalStatus) {
        try {
            return equityChangeRecordMapper.selectByApprovalStatus(approvalStatus);
        } catch (Exception e) {
            log.error("按审批状态查询记录失败，approvalStatus: {}", approvalStatus, e);
            return null;
        }
    }

    @Override
    public List<EquityChangeRecord> getEquityChangeRecordByWarningLevel(String warningLevel) {
        try {
            return equityChangeRecordMapper.selectByWarningLevel(warningLevel);
        } catch (Exception e) {
            log.error("按预警级别查询记录失败，warningLevel: {}", warningLevel, e);
            return null;
        }
    }

    @Override
    public List<EquityChangeRecord> getLatestChangeRecords(String investeeEnterpriseId, Integer limit) {
        try {
            if (limit == null || limit <= 0) {
                limit = 10;
            }
            return equityChangeRecordMapper.selectLatestChangeRecords(investeeEnterpriseId, limit);
        } catch (Exception e) {
            log.error("查询最新变动记录失败，investeeEnterpriseId: {}, limit: {}", investeeEnterpriseId, limit, e);
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> getFrequentChangeEnterprises(Integer days, Integer minChangeCount) {
        try {
            if (days == null || days <= 0) {
                days = 30;
            }
            if (minChangeCount == null || minChangeCount <= 0) {
                minChangeCount = 5;
            }
            return equityChangeRecordMapper.selectFrequentChangeEnterprises(days, minChangeCount);
        } catch (Exception e) {
            log.error("查询变动频繁的企业失败，days: {}, minChangeCount: {}", days, minChangeCount, e);
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> getChangeTypeStatistics(String startDate, String endDate) {
        try {
            return equityChangeRecordMapper.selectChangeTypeStatistics(startDate, endDate);
        } catch (Exception e) {
            log.error("按变动类型统计失败，startDate: {}, endDate: {}", startDate, endDate, e);
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> getChangeReasonStatistics(String startDate, String endDate) {
        try {
            return equityChangeRecordMapper.selectChangeReasonStatistics(startDate, endDate);
        } catch (Exception e) {
            log.error("按变动原因统计失败，startDate: {}, endDate: {}", startDate, endDate, e);
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> getApprovalStatusStatistics(String startDate, String endDate) {
        try {
            return equityChangeRecordMapper.selectApprovalStatusStatistics(startDate, endDate);
        } catch (Exception e) {
            log.error("按审批状态统计失败，startDate: {}, endDate: {}", startDate, endDate, e);
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> getWarningLevelStatistics(String startDate, String endDate) {
        try {
            return equityChangeRecordMapper.selectWarningLevelStatistics(startDate, endDate);
        } catch (Exception e) {
            log.error("按预警级别统计失败，startDate: {}, endDate: {}", startDate, endDate, e);
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> getChangeTrend(String startDate, String endDate) {
        try {
            return equityChangeRecordMapper.selectChangeTrend(startDate, endDate);
        } catch (Exception e) {
            log.error("查询变动趋势失败，startDate: {}, endDate: {}", startDate, endDate, e);
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> getChangeAmountTrend(String startDate, String endDate) {
        try {
            return equityChangeRecordMapper.selectChangeAmountTrend(startDate, endDate);
        } catch (Exception e) {
            log.error("查询变动金额趋势失败，startDate: {}, endDate: {}", startDate, endDate, e);
            return null;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateApprovalStatus(List<String> changeIds, String approvalStatus) {
        try {
            int result = equityChangeRecordMapper.batchUpdateApprovalStatus(changeIds, approvalStatus);
            return result > 0;
        } catch (Exception e) {
            log.error("批量更新审批状态失败", e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateWarningStatus(List<String> changeIds, Boolean needWarning, String warningLevel) {
        try {
            int result = equityChangeRecordMapper.batchUpdateWarningStatus(changeIds, needWarning, warningLevel);
            return result > 0;
        } catch (Exception e) {
            log.error("批量更新预警状态失败", e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteExpiredChangeRecords(Integer days) {
        try {
            if (days == null || days <= 0) {
                days = 365;
            }
            return equityChangeRecordMapper.deleteExpiredChangeRecords(days);
        } catch (Exception e) {
            log.error("删除过期变动记录失败，days: {}", days, e);
            return 0;
        }
    }

    @Override
    public Map<String, Object> getChangeStatisticsOverview() {
        try {
            return equityChangeRecordMapper.selectChangeStatisticsOverview();
        } catch (Exception e) {
            log.error("获取变动统计概览失败", e);
            return new HashMap<>();
        }
    }

    @Override
    public List<Map<String, Object>> getChangeTypeDistribution() {
        try {
            return equityChangeRecordMapper.selectChangeTypeDistribution();
        } catch (Exception e) {
            log.error("获取变动类型分布失败", e);
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> getChangeReasonDistribution() {
        try {
            return equityChangeRecordMapper.selectChangeReasonDistribution();
        } catch (Exception e) {
            log.error("获取变动原因分布失败", e);
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> getApprovalStatusDistribution() {
        try {
            return equityChangeRecordMapper.selectApprovalStatusDistribution();
        } catch (Exception e) {
            log.error("获取审批状态分布失败", e);
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> getWarningLevelDistribution() {
        try {
            return equityChangeRecordMapper.selectWarningLevelDistribution();
        } catch (Exception e) {
            log.error("获取预警级别分布失败", e);
            return null;
        }
    }

    @Override
    public List<Map<String, Object>> exportEquityChangeRecordList(EquityChangeRecordQueryVO queryVO) {
        try {
            return equityChangeRecordMapper.exportEquityChangeRecordList(queryVO);
        } catch (Exception e) {
            log.error("导出股权变动记录列表失败", e);
            return null;
        }
    }

    /**
     * 分析变动影响
     */
    private void analyzeChangeImpact(EquityChangeRecord record) {
        try {
            StringBuilder impact = new StringBuilder();
            
            // 分析控制权变化
            if (record.getBeforeIsControlling() != null && record.getAfterIsControlling() != null) {
                if (!record.getBeforeIsControlling().equals(record.getAfterIsControlling())) {
                    impact.append("控制权发生变化；");
                }
            }
            
            // 分析实际控制人变化
            if (record.getBeforeIsActualController() != null && record.getAfterIsActualController() != null) {
                if (!record.getBeforeIsActualController().equals(record.getAfterIsActualController())) {
                    impact.append("实际控制人发生变化；");
                }
            }
            
            // 分析持股比例变化
            if (record.getBeforeShareholdingRatio() != null && record.getAfterShareholdingRatio() != null) {
                BigDecimal change = record.getAfterShareholdingRatio().subtract(record.getBeforeShareholdingRatio());
                if (change.abs().compareTo(new BigDecimal("5")) > 0) {
                    impact.append("持股比例变化超过5%；");
                }
            }
            
            record.setImpactAnalysis(impact.toString());
        } catch (Exception e) {
            log.error("分析变动影响失败", e);
        }
    }

    /**
     * 合规性检查
     */
    private void checkCompliance(EquityChangeRecord record) {
        try {
            StringBuilder result = new StringBuilder("合规");
            
            // 检查是否需要审批
            if (record.getChangeAmount() != null && record.getChangeAmount().compareTo(new BigDecimal("1000")) > 0) {
                if (record.getApprovalStatus() == null || 
                    !EquityChangeRecord.APPROVAL_STATUS_APPROVED.equals(record.getApprovalStatus())) {
                    result.append("；大额变动需要审批");
                }
            }
            
            record.setComplianceCheckResult(result.toString());
        } catch (Exception e) {
            log.error("合规性检查失败", e);
        }
    }

    @Override
    public Map<String, Object> analyzeEquityChangeImpact(String changeId) {
        try {
            EquityChangeRecord record = this.getById(changeId);
            if (record == null) {
                return new HashMap<>();
            }

            Map<String, Object> result = new HashMap<>();
            result.put("changeRecord", record);
            result.put("impactAnalysis", record.getImpactAnalysis());

            // 添加详细的影响分析
            Map<String, Object> detailAnalysis = new HashMap<>();

            // 控制权影响
            if (record.getBeforeIsControlling() != null && record.getAfterIsControlling() != null) {
                detailAnalysis.put("controllingChange", !record.getBeforeIsControlling().equals(record.getAfterIsControlling()));
            }

            // 实际控制人影响
            if (record.getBeforeIsActualController() != null && record.getAfterIsActualController() != null) {
                detailAnalysis.put("actualControllerChange", !record.getBeforeIsActualController().equals(record.getAfterIsActualController()));
            }

            result.put("detailAnalysis", detailAnalysis);
            return result;
        } catch (Exception e) {
            log.error("股权变动影响分析失败，changeId: {}", changeId, e);
            return new HashMap<>();
        }
    }

    @Override
    public Map<String, Object> checkEquityChangeCompliance(EquityChangeRecord equityChangeRecord) {
        try {
            Map<String, Object> result = new HashMap<>();

            this.checkCompliance(equityChangeRecord);
            result.put("complianceResult", equityChangeRecord.getComplianceCheckResult());
            result.put("isCompliant", "合规".equals(equityChangeRecord.getComplianceCheckResult()));

            return result;
        } catch (Exception e) {
            log.error("股权变动合规性检查失败", e);
            return new HashMap<>();
        }
    }

    @Override
    public Map<String, Object> checkEquityChangeWarning(EquityChangeRecord equityChangeRecord) {
        try {
            Map<String, Object> result = new HashMap<>();

            this.checkWarning(equityChangeRecord);
            result.put("needWarning", equityChangeRecord.getNeedWarning());
            result.put("warningLevel", equityChangeRecord.getWarningLevel());
            result.put("warningReason", equityChangeRecord.getWarningReason());
            result.put("isMajorChange", equityChangeRecord.getIsMajorChange());

            return result;
        } catch (Exception e) {
            log.error("股权变动预警检查失败", e);
            return new HashMap<>();
        }
    }

    @Override
    public String getChangeTypeLabel(String changeType) {
        if (changeType == null) return "";
        switch (changeType) {
            case EquityChangeRecord.CHANGE_TYPE_INCREASE: return "增资";
            case EquityChangeRecord.CHANGE_TYPE_DECREASE: return "减资";
            case EquityChangeRecord.CHANGE_TYPE_TRANSFER: return "转让";
            case EquityChangeRecord.CHANGE_TYPE_PLEDGE: return "质押";
            case EquityChangeRecord.CHANGE_TYPE_UNPLEDGE: return "解押";
            case EquityChangeRecord.CHANGE_TYPE_FREEZE: return "冻结";
            case EquityChangeRecord.CHANGE_TYPE_UNFREEZE: return "解冻";
            case EquityChangeRecord.CHANGE_TYPE_MERGER: return "合并";
            case EquityChangeRecord.CHANGE_TYPE_SPLIT: return "分立";
            case EquityChangeRecord.CHANGE_TYPE_LIQUIDATION: return "清算";
            default: return changeType;
        }
    }

    @Override
    public String getChangeReasonLabel(String changeReason) {
        if (changeReason == null) return "";
        switch (changeReason) {
            case EquityChangeRecord.CHANGE_REASON_BUSINESS_EXPANSION: return "业务扩张";
            case EquityChangeRecord.CHANGE_REASON_CAPITAL_INCREASE: return "增资扩股";
            case EquityChangeRecord.CHANGE_REASON_DEBT_REPAYMENT: return "偿还债务";
            case EquityChangeRecord.CHANGE_REASON_STRATEGIC_ADJUSTMENT: return "战略调整";
            case EquityChangeRecord.CHANGE_REASON_ASSET_RESTRUCTURING: return "资产重组";
            case EquityChangeRecord.CHANGE_REASON_MARKET_TRANSACTION: return "市场交易";
            case EquityChangeRecord.CHANGE_REASON_POLICY_REQUIREMENT: return "政策要求";
            case EquityChangeRecord.CHANGE_REASON_OTHER: return "其他";
            default: return changeReason;
        }
    }

    @Override
    public String getTransferMethodLabel(String transferMethod) {
        if (transferMethod == null) return "";
        switch (transferMethod) {
            case EquityChangeRecord.TRANSFER_METHOD_AGREEMENT: return "协议转让";
            case EquityChangeRecord.TRANSFER_METHOD_AUCTION: return "拍卖";
            case EquityChangeRecord.TRANSFER_METHOD_TENDER: return "招标";
            case EquityChangeRecord.TRANSFER_METHOD_EXCHANGE: return "产权交易所";
            case EquityChangeRecord.TRANSFER_METHOD_INTERNAL: return "内部转让";
            case EquityChangeRecord.TRANSFER_METHOD_JUDICIAL: return "司法拍卖";
            default: return transferMethod;
        }
    }

    @Override
    public String getApprovalStatusLabel(String approvalStatus) {
        if (approvalStatus == null) return "";
        switch (approvalStatus) {
            case EquityChangeRecord.APPROVAL_STATUS_PENDING: return "待审批";
            case EquityChangeRecord.APPROVAL_STATUS_APPROVED: return "已审批";
            case EquityChangeRecord.APPROVAL_STATUS_REJECTED: return "已拒绝";
            case EquityChangeRecord.APPROVAL_STATUS_CANCELLED: return "已取消";
            case EquityChangeRecord.APPROVAL_STATUS_EXPIRED: return "已过期";
            default: return approvalStatus;
        }
    }

    @Override
    public String getWarningLevelLabel(String warningLevel) {
        if (warningLevel == null) return "";
        switch (warningLevel) {
            case EquityChangeRecord.WARNING_LEVEL_LOW: return "低风险";
            case EquityChangeRecord.WARNING_LEVEL_MEDIUM: return "中风险";
            case EquityChangeRecord.WARNING_LEVEL_HIGH: return "高风险";
            case EquityChangeRecord.WARNING_LEVEL_CRITICAL: return "严重风险";
            default: return warningLevel;
        }
    }

    /**
     * 预警检查
     */
    private void checkWarning(EquityChangeRecord record) {
        try {
            boolean needWarning = false;
            String warningLevel = EquityChangeRecord.WARNING_LEVEL_LOW;
            StringBuilder reason = new StringBuilder();

            // 检查是否为重大变动
            if (record.getChangeAmount() != null && record.getChangeAmount().compareTo(new BigDecimal("5000")) > 0) {
                needWarning = true;
                warningLevel = EquityChangeRecord.WARNING_LEVEL_HIGH;
                reason.append("大额变动；");
                record.setIsMajorChange(true);
            }

            // 检查控制权变化
            if (record.getBeforeIsControlling() != null && record.getAfterIsControlling() != null) {
                if (!record.getBeforeIsControlling().equals(record.getAfterIsControlling())) {
                    needWarning = true;
                    warningLevel = EquityChangeRecord.WARNING_LEVEL_CRITICAL;
                    reason.append("控制权变化；");
                    record.setIsMajorChange(true);
                }
            }

            record.setNeedWarning(needWarning);
            record.setWarningLevel(warningLevel);
            record.setWarningReason(reason.toString());
        } catch (Exception e) {
            log.error("预警检查失败", e);
        }
    }
}
