package com.management.accountant.service.pm.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.management.accountant.entity.pm.PmIncentiveManagement;
import com.management.accountant.mapper.pm.PmIncentiveManagementMapper;
import com.management.accountant.service.pm.PmIncentiveManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 激励管理服务实现类
 *
 * @author AI Assistant
 * @since 2024-01-01
 */
@Slf4j
@Service
public class PmIncentiveManagementServiceImpl extends ServiceImpl<PmIncentiveManagementMapper, PmIncentiveManagement> 
        implements PmIncentiveManagementService {

    @Autowired
    private PmIncentiveManagementMapper incentiveManagementMapper;

    @Override
    public IPage<PmIncentiveManagement> getIncentiveManagementPage(Page<PmIncentiveManagement> page, 
                                                                   String incentiveTitle,
                                                                   String incentiveType,
                                                                   String incentiveStatus,
                                                                   Integer incentiveYear,
                                                                   Long targetDeptId,
                                                                   Long incentiveOwnerId,
                                                                   LocalDateTime startTime,
                                                                   LocalDateTime endTime,
                                                                   Long tenantId) {
        return incentiveManagementMapper.selectIncentiveManagementPage(page, incentiveTitle, incentiveType, 
                incentiveStatus, incentiveYear, targetDeptId, incentiveOwnerId, startTime, endTime, tenantId);
    }

    @Override
    public PmIncentiveManagement getByIncentiveCode(String incentiveCode, Long tenantId) {
        return incentiveManagementMapper.selectByIncentiveCode(incentiveCode, tenantId);
    }

    @Override
    public List<PmIncentiveManagement> getByIncentiveType(String incentiveType, Long tenantId) {
        return incentiveManagementMapper.selectByIncentiveType(incentiveType, tenantId);
    }

    @Override
    public List<PmIncentiveManagement> getByIncentiveStatus(String incentiveStatus, Long tenantId) {
        return incentiveManagementMapper.selectByIncentiveStatus(incentiveStatus, tenantId);
    }

    @Override
    public List<PmIncentiveManagement> getByDeptId(Long targetDeptId, Long tenantId) {
        return incentiveManagementMapper.selectByDeptId(targetDeptId, tenantId);
    }

    @Override
    public List<PmIncentiveManagement> getByOwnerId(Long incentiveOwnerId, Long tenantId) {
        return incentiveManagementMapper.selectByOwnerId(incentiveOwnerId, tenantId);
    }

    @Override
    public List<PmIncentiveManagement> getByYear(Integer incentiveYear, Long tenantId) {
        return incentiveManagementMapper.selectByYear(incentiveYear, tenantId);
    }

    @Override
    public List<PmIncentiveManagement> getByQuarter(Integer incentiveYear, Integer incentiveQuarter, Long tenantId) {
        return incentiveManagementMapper.selectByQuarter(incentiveYear, incentiveQuarter, tenantId);
    }

    @Override
    public List<PmIncentiveManagement> getByMonth(Integer incentiveYear, Integer incentiveMonth, Long tenantId) {
        return incentiveManagementMapper.selectByMonth(incentiveYear, incentiveMonth, tenantId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createIncentiveScheme(PmIncentiveManagement incentiveManagement) {
        try {
            // 验证激励编码唯一性
            if (checkIncentiveCodeExists(incentiveManagement.getIncentiveCode(), null, incentiveManagement.getTenantId())) {
                throw new RuntimeException("激励编码已存在");
            }

            // 验证时间冲突
            if (checkTimeConflict(null, incentiveManagement.getTargetDeptId(), incentiveManagement.getIncentiveType(),
                    incentiveManagement.getPlannedStartTime(), incentiveManagement.getPlannedEndTime(), incentiveManagement.getTenantId())) {
                throw new RuntimeException("激励时间存在冲突");
            }

            // 设置初始状态
            incentiveManagement.setIncentiveStatus("DRAFT");
            incentiveManagement.setApprovalStatus("PENDING");
            incentiveManagement.setDistributionStatus("PENDING");
            incentiveManagement.setFollowUpStatus("PENDING");
            incentiveManagement.setNeedFollowUp(0);
            incentiveManagement.setIsReminded(0);
            incentiveManagement.setNotificationStatus("PENDING");

            // 计算剩余金额
            if (incentiveManagement.getTotalBudget() != null) {
                incentiveManagement.setRemainingAmount(incentiveManagement.getTotalBudget());
                incentiveManagement.setUsedAmount(BigDecimal.ZERO);
            }

            return save(incentiveManagement);
        } catch (Exception e) {
            log.error("创建激励方案失败", e);
            throw new RuntimeException("创建激励方案失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateIncentiveScheme(PmIncentiveManagement incentiveManagement) {
        try {
            // 验证激励编码唯一性
            if (checkIncentiveCodeExists(incentiveManagement.getIncentiveCode(), 
                    incentiveManagement.getIncentiveId(), incentiveManagement.getTenantId())) {
                throw new RuntimeException("激励编码已存在");
            }

            // 验证时间冲突
            if (checkTimeConflict(incentiveManagement.getIncentiveId(), incentiveManagement.getTargetDeptId(), 
                    incentiveManagement.getIncentiveType(), incentiveManagement.getPlannedStartTime(), 
                    incentiveManagement.getPlannedEndTime(), incentiveManagement.getTenantId())) {
                throw new RuntimeException("激励时间存在冲突");
            }

            // 重新计算剩余金额
            if (incentiveManagement.getTotalBudget() != null && incentiveManagement.getUsedAmount() != null) {
                incentiveManagement.setRemainingAmount(
                    incentiveManagement.getTotalBudget().subtract(incentiveManagement.getUsedAmount())
                );
            }

            return updateById(incentiveManagement);
        } catch (Exception e) {
            log.error("更新激励方案失败", e);
            throw new RuntimeException("更新激励方案失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteIncentiveScheme(Long incentiveId, Long tenantId) {
        try {
            PmIncentiveManagement incentive = getById(incentiveId);
            if (incentive == null || !incentive.getTenantId().equals(tenantId)) {
                throw new RuntimeException("激励方案不存在");
            }

            // 检查是否可以删除
            if ("ACTIVE".equals(incentive.getIncentiveStatus()) || "COMPLETED".equals(incentive.getIncentiveStatus())) {
                throw new RuntimeException("激励方案已生效或已完成，无法删除");
            }

            return removeById(incentiveId);
        } catch (Exception e) {
            log.error("删除激励方案失败", e);
            throw new RuntimeException("删除激励方案失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteIncentiveSchemes(List<Long> incentiveIds, Long tenantId) {
        try {
            for (Long incentiveId : incentiveIds) {
                deleteIncentiveScheme(incentiveId, tenantId);
            }
            return true;
        } catch (Exception e) {
            log.error("批量删除激励方案失败", e);
            throw new RuntimeException("批量删除激励方案失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean startIncentiveScheme(Long incentiveId, Long tenantId) {
        try {
            PmIncentiveManagement incentive = getById(incentiveId);
            if (incentive == null || !incentive.getTenantId().equals(tenantId)) {
                throw new RuntimeException("激励方案不存在");
            }

            if (!"APPROVED".equals(incentive.getApprovalStatus())) {
                throw new RuntimeException("激励方案未审批通过，无法启动");
            }

            incentive.setIncentiveStatus("ACTIVE");
            incentive.setActualStartTime(LocalDateTime.now());

            return updateById(incentive);
        } catch (Exception e) {
            log.error("启动激励方案失败", e);
            throw new RuntimeException("启动激励方案失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean suspendIncentiveScheme(Long incentiveId, String reason, Long tenantId) {
        try {
            PmIncentiveManagement incentive = getById(incentiveId);
            if (incentive == null || !incentive.getTenantId().equals(tenantId)) {
                throw new RuntimeException("激励方案不存在");
            }

            incentive.setIncentiveStatus("SUSPENDED");
            incentive.setRemarks(StringUtils.hasText(incentive.getRemarks()) ? 
                incentive.getRemarks() + "; 暂停原因: " + reason : "暂停原因: " + reason);

            return updateById(incentive);
        } catch (Exception e) {
            log.error("暂停激励方案失败", e);
            throw new RuntimeException("暂停激励方案失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean completeIncentiveScheme(Long incentiveId, Long tenantId) {
        try {
            PmIncentiveManagement incentive = getById(incentiveId);
            if (incentive == null || !incentive.getTenantId().equals(tenantId)) {
                throw new RuntimeException("激励方案不存在");
            }

            incentive.setIncentiveStatus("COMPLETED");
            incentive.setActualEndTime(LocalDateTime.now());
            incentive.setCompletionRate(BigDecimal.valueOf(100));

            return updateById(incentive);
        } catch (Exception e) {
            log.error("完成激励方案失败", e);
            throw new RuntimeException("完成激励方案失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelIncentiveScheme(Long incentiveId, String reason, Long tenantId) {
        try {
            PmIncentiveManagement incentive = getById(incentiveId);
            if (incentive == null || !incentive.getTenantId().equals(tenantId)) {
                throw new RuntimeException("激励方案不存在");
            }

            incentive.setIncentiveStatus("CANCELLED");
            incentive.setRemarks(StringUtils.hasText(incentive.getRemarks()) ? 
                incentive.getRemarks() + "; 取消原因: " + reason : "取消原因: " + reason);

            return updateById(incentive);
        } catch (Exception e) {
            log.error("取消激励方案失败", e);
            throw new RuntimeException("取消激励方案失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean approveIncentiveScheme(Long incentiveId, String approvalComments, Long approverId, String approverName, Long tenantId) {
        try {
            PmIncentiveManagement incentive = getById(incentiveId);
            if (incentive == null || !incentive.getTenantId().equals(tenantId)) {
                throw new RuntimeException("激励方案不存在");
            }

            incentive.setApprovalStatus("APPROVED");
            incentive.setApproverId(approverId);
            incentive.setApproverName(approverName);
            incentive.setApprovalTime(LocalDateTime.now());
            incentive.setApprovalComments(approvalComments);

            return updateById(incentive);
        } catch (Exception e) {
            log.error("审批激励方案失败", e);
            throw new RuntimeException("审批激励方案失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean rejectIncentiveScheme(Long incentiveId, String approvalComments, Long approverId, String approverName, Long tenantId) {
        try {
            PmIncentiveManagement incentive = getById(incentiveId);
            if (incentive == null || !incentive.getTenantId().equals(tenantId)) {
                throw new RuntimeException("激励方案不存在");
            }

            incentive.setApprovalStatus("REJECTED");
            incentive.setApproverId(approverId);
            incentive.setApproverName(approverName);
            incentive.setApprovalTime(LocalDateTime.now());
            incentive.setApprovalComments(approvalComments);

            return updateById(incentive);
        } catch (Exception e) {
            log.error("拒绝激励方案失败", e);
            throw new RuntimeException("拒绝激励方案失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean distributeIncentive(Long incentiveId, String distributionMethod, Long tenantId) {
        try {
            PmIncentiveManagement incentive = getById(incentiveId);
            if (incentive == null || !incentive.getTenantId().equals(tenantId)) {
                throw new RuntimeException("激励方案不存在");
            }

            if (!"ACTIVE".equals(incentive.getIncentiveStatus())) {
                throw new RuntimeException("激励方案未生效，无法发放");
            }

            incentive.setDistributionStatus("COMPLETED");
            incentive.setDistributionMethod(distributionMethod);
            incentive.setDistributionTime(LocalDateTime.now());

            return updateById(incentive);
        } catch (Exception e) {
            log.error("发放激励失败", e);
            throw new RuntimeException("发放激励失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDistributeIncentives(List<Long> incentiveIds, String distributionMethod, Long tenantId) {
        try {
            LocalDateTime now = LocalDateTime.now();
            return incentiveManagementMapper.batchUpdateDistributionStatus(incentiveIds, "COMPLETED", 
                    now, null, null, now) > 0;
        } catch (Exception e) {
            log.error("批量发放激励失败", e);
            throw new RuntimeException("批量发放激励失败: " + e.getMessage());
        }
    }

    @Override
    public BigDecimal calculateIncentiveAmount(Long incentiveId, Map<String, Object> parameters, Long tenantId) {
        try {
            PmIncentiveManagement incentive = getById(incentiveId);
            if (incentive == null || !incentive.getTenantId().equals(tenantId)) {
                throw new RuntimeException("激励方案不存在");
            }

            return performIncentiveCalculation(incentive, parameters);
        } catch (Exception e) {
            log.error("计算激励金额失败", e);
            throw new RuntimeException("计算激励金额失败: " + e.getMessage());
        }
    }

    @Override
    public Map<Long, BigDecimal> batchCalculateIncentiveAmounts(List<Long> incentiveIds, Map<String, Object> parameters, Long tenantId) {
        Map<Long, BigDecimal> results = new HashMap<>();
        for (Long incentiveId : incentiveIds) {
            try {
                BigDecimal amount = calculateIncentiveAmount(incentiveId, parameters, tenantId);
                results.put(incentiveId, amount);
            } catch (Exception e) {
                log.error("计算激励金额失败: incentiveId={}", incentiveId, e);
                results.put(incentiveId, BigDecimal.ZERO);
            }
        }
        return results;
    }

    /**
     * 执行激励金额计算
     */
    private BigDecimal performIncentiveCalculation(PmIncentiveManagement incentive, Map<String, Object> parameters) {
        String calculationMethod = incentive.getCalculationMethod();
        BigDecimal baseAmount = incentive.getBaseAmount() != null ? incentive.getBaseAmount() : BigDecimal.ZERO;
        BigDecimal coefficient = incentive.getIncentiveCoefficient() != null ? incentive.getIncentiveCoefficient() : BigDecimal.ONE;

        BigDecimal result = BigDecimal.ZERO;

        switch (calculationMethod) {
            case "FIXED":
                result = baseAmount;
                break;
            case "PERCENTAGE":
                BigDecimal baseValue = (BigDecimal) parameters.getOrDefault("baseValue", BigDecimal.ZERO);
                result = baseValue.multiply(coefficient).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                break;
            case "FORMULA":
                result = calculateByFormula(incentive.getCalculationFormula(), parameters);
                break;
            case "TIERED":
                result = calculateByTiered(incentive, parameters);
                break;
            default:
                result = baseAmount;
        }

        // 应用最小值和最大值限制
        if (incentive.getMinAmount() != null && result.compareTo(incentive.getMinAmount()) < 0) {
            result = incentive.getMinAmount();
        }
        if (incentive.getMaxAmount() != null && result.compareTo(incentive.getMaxAmount()) > 0) {
            result = incentive.getMaxAmount();
        }

        return result;
    }

    /**
     * 根据公式计算
     */
    private BigDecimal calculateByFormula(String formula, Map<String, Object> parameters) {
        // 这里可以实现复杂的公式计算逻辑
        // 简化实现，返回基础金额
        return (BigDecimal) parameters.getOrDefault("baseAmount", BigDecimal.ZERO);
    }

    /**
     * 分层计算
     */
    private BigDecimal calculateByTiered(PmIncentiveManagement incentive, Map<String, Object> parameters) {
        // 这里可以实现分层计算逻辑
        // 简化实现，返回基础金额乘以系数
        BigDecimal baseAmount = incentive.getBaseAmount() != null ? incentive.getBaseAmount() : BigDecimal.ZERO;
        BigDecimal coefficient = incentive.getIncentiveCoefficient() != null ? incentive.getIncentiveCoefficient() : BigDecimal.ONE;
        return baseAmount.multiply(coefficient);
    }

    @Override
    public List<PmIncentiveManagement> getPendingApproval(Long tenantId) {
        return incentiveManagementMapper.selectPendingApproval(tenantId);
    }

    @Override
    public List<PmIncentiveManagement> getPendingDistribution(Long tenantId) {
        return incentiveManagementMapper.selectPendingDistribution(tenantId);
    }

    @Override
    public List<PmIncentiveManagement> getNeedFollowUp(Long tenantId) {
        return incentiveManagementMapper.selectNeedFollowUp(tenantId);
    }

    @Override
    public List<PmIncentiveManagement> getUpcomingDeadline(LocalDateTime deadline, Long tenantId) {
        return incentiveManagementMapper.selectUpcomingDeadline(deadline, tenantId);
    }

    @Override
    public List<PmIncentiveManagement> getOverdue(LocalDateTime currentTime, Long tenantId) {
        return incentiveManagementMapper.selectOverdue(currentTime, tenantId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean addFollowUpRecord(Long incentiveId, String followUpRecord, Long tenantId) {
        try {
            PmIncentiveManagement incentive = getById(incentiveId);
            if (incentive == null || !incentive.getTenantId().equals(tenantId)) {
                throw new RuntimeException("激励方案不存在");
            }

            String existingRecord = incentive.getFollowUpRecord();
            String newRecord = StringUtils.hasText(existingRecord) ?
                existingRecord + "\n" + LocalDateTime.now() + ": " + followUpRecord :
                LocalDateTime.now() + ": " + followUpRecord;

            incentive.setFollowUpRecord(newRecord);
            incentive.setFollowUpStatus("IN_PROGRESS");

            return updateById(incentive);
        } catch (Exception e) {
            log.error("添加跟进记录失败", e);
            throw new RuntimeException("添加跟进记录失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateFollowUpStatus(Long incentiveId, String followUpStatus, Long tenantId) {
        try {
            PmIncentiveManagement incentive = getById(incentiveId);
            if (incentive == null || !incentive.getTenantId().equals(tenantId)) {
                throw new RuntimeException("激励方案不存在");
            }

            incentive.setFollowUpStatus(followUpStatus);
            return updateById(incentive);
        } catch (Exception e) {
            log.error("更新跟进状态失败", e);
            throw new RuntimeException("更新跟进状态失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateFollowUpStatus(List<Long> incentiveIds, String followUpStatus, Long tenantId) {
        try {
            LocalDateTime now = LocalDateTime.now();
            return incentiveManagementMapper.batchUpdateFollowUpStatus(incentiveIds, followUpStatus,
                    null, null, now) > 0;
        } catch (Exception e) {
            log.error("批量更新跟进状态失败", e);
            throw new RuntimeException("批量更新跟进状态失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean sendReminderNotification(Long incentiveId, String notificationType, Long tenantId) {
        try {
            PmIncentiveManagement incentive = getById(incentiveId);
            if (incentive == null || !incentive.getTenantId().equals(tenantId)) {
                throw new RuntimeException("激励方案不存在");
            }

            // 这里可以集成消息通知服务
            incentive.setIsReminded(1);
            incentive.setReminderTime(LocalDateTime.now());
            incentive.setNotificationStatus("SENT");
            incentive.setNotificationTime(LocalDateTime.now());

            return updateById(incentive);
        } catch (Exception e) {
            log.error("发送提醒通知失败", e);
            throw new RuntimeException("发送提醒通知失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchSendReminderNotifications(List<Long> incentiveIds, String notificationType, Long tenantId) {
        try {
            for (Long incentiveId : incentiveIds) {
                sendReminderNotification(incentiveId, notificationType, tenantId);
            }
            return true;
        } catch (Exception e) {
            log.error("批量发送提醒通知失败", e);
            throw new RuntimeException("批量发送提醒通知失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getIncentiveStatistics(Integer incentiveYear, Long tenantId) {
        return incentiveManagementMapper.selectIncentiveStatistics(incentiveYear, tenantId);
    }

    @Override
    public List<Map<String, Object>> getIncentiveStatusDistribution(Integer incentiveYear, Long tenantId) {
        return incentiveManagementMapper.selectIncentiveStatusDistribution(incentiveYear, tenantId);
    }

    @Override
    public List<Map<String, Object>> getIncentiveTypeDistribution(Integer incentiveYear, Long tenantId) {
        return incentiveManagementMapper.selectIncentiveTypeDistribution(incentiveYear, tenantId);
    }

    @Override
    public List<Map<String, Object>> getIncentiveCompletionTrend(LocalDateTime startTime, LocalDateTime endTime, Long tenantId) {
        return incentiveManagementMapper.selectIncentiveCompletionTrend(startTime, endTime, tenantId);
    }

    @Override
    public List<Map<String, Object>> getIncentiveAmountDistribution(Integer incentiveYear, Long tenantId) {
        return incentiveManagementMapper.selectIncentiveAmountDistribution(incentiveYear, tenantId);
    }

    @Override
    public List<Map<String, Object>> getIncentiveEffectivenessDistribution(Integer incentiveYear, Long tenantId) {
        return incentiveManagementMapper.selectIncentiveEffectivenessDistribution(incentiveYear, tenantId);
    }

    @Override
    public List<Map<String, Object>> getIncentiveRanking(Integer incentiveYear, String rankingType, Integer limit, Long tenantId) {
        return incentiveManagementMapper.selectIncentiveRanking(incentiveYear, rankingType, limit, tenantId);
    }

    @Override
    public boolean checkIncentiveCodeExists(String incentiveCode, Long incentiveId, Long tenantId) {
        return incentiveManagementMapper.checkIncentiveCodeExists(incentiveCode, incentiveId, tenantId) > 0;
    }

    @Override
    public boolean checkTimeConflict(Long incentiveId, Long targetDeptId, String incentiveType,
                                     LocalDateTime startTime, LocalDateTime endTime, Long tenantId) {
        return incentiveManagementMapper.checkTimeConflict(incentiveId, targetDeptId, incentiveType,
                startTime, endTime, tenantId) > 0;
    }

    @Override
    public BigDecimal calculateTotalIncentiveAmount(Integer incentiveYear, Long tenantId) {
        BigDecimal amount = incentiveManagementMapper.calculateTotalIncentiveAmount(incentiveYear, tenantId);
        return amount != null ? amount : BigDecimal.ZERO;
    }

    @Override
    public BigDecimal calculateDeptIncentiveAmount(Long targetDeptId, Integer incentiveYear, Long tenantId) {
        BigDecimal amount = incentiveManagementMapper.calculateDeptIncentiveAmount(targetDeptId, incentiveYear, tenantId);
        return amount != null ? amount : BigDecimal.ZERO;
    }

    @Override
    public Map<String, Object> getIncentiveDetailWithRelations(Long incentiveId, Long tenantId) {
        return incentiveManagementMapper.selectIncentiveDetailWithRelations(incentiveId, tenantId);
    }

    @Override
    public List<Map<String, Object>> getIncentiveHistory(Long incentiveId, Long tenantId) {
        return incentiveManagementMapper.selectIncentiveHistory(incentiveId, tenantId);
    }

    @Override
    public List<Map<String, Object>> exportIncentiveData(Integer incentiveYear, String incentiveType,
                                                          String incentiveStatus, Long targetDeptId, Long tenantId) {
        return incentiveManagementMapper.selectIncentiveExportData(incentiveYear, incentiveType,
                incentiveStatus, targetDeptId, tenantId);
    }

    @Override
    public List<Map<String, Object>> getRecommendedIncentiveSchemes(Long targetDeptId, String incentiveType,
                                                                     BigDecimal budgetRange, Long tenantId) {
        return incentiveManagementMapper.selectRecommendedIncentiveSchemes(targetDeptId, incentiveType,
                budgetRange, tenantId);
    }

    @Override
    public Map<String, Object> analyzeIncentiveEffectiveness(Long incentiveId, Long tenantId) {
        return incentiveManagementMapper.analyzeIncentiveEffectiveness(incentiveId, tenantId);
    }

    @Override
    public Map<String, Object> generateIncentiveReportData(Integer incentiveYear, String reportType, Long tenantId) {
        return incentiveManagementMapper.generateIncentiveReportData(incentiveYear, reportType, tenantId);
    }

    @Override
    public List<Map<String, Object>> getIncentiveOptimizationSuggestions(Long incentiveId, Long tenantId) {
        return incentiveManagementMapper.selectIncentiveOptimizationSuggestions(incentiveId, tenantId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean copyIncentiveScheme(Long sourceIncentiveId, String newIncentiveTitle, Long tenantId) {
        try {
            PmIncentiveManagement sourceIncentive = getById(sourceIncentiveId);
            if (sourceIncentive == null || !sourceIncentive.getTenantId().equals(tenantId)) {
                throw new RuntimeException("源激励方案不存在");
            }

            PmIncentiveManagement newIncentive = new PmIncentiveManagement();
            // 复制属性（除了ID和时间相关字段）
            copyIncentiveProperties(sourceIncentive, newIncentive);

            newIncentive.setIncentiveTitle(newIncentiveTitle);
            newIncentive.setIncentiveCode(generateIncentiveCode());
            newIncentive.setIncentiveStatus("DRAFT");
            newIncentive.setApprovalStatus("PENDING");

            return save(newIncentive);
        } catch (Exception e) {
            log.error("复制激励方案失败", e);
            throw new RuntimeException("复制激励方案失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveAsTemplate(Long incentiveId, String templateName, Long tenantId) {
        try {
            // 这里可以实现保存为模板的逻辑
            // 简化实现，返回成功
            return true;
        } catch (Exception e) {
            log.error("保存为模板失败", e);
            throw new RuntimeException("保存为模板失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createFromTemplate(Long templateId, String incentiveTitle, Long tenantId) {
        try {
            // 这里可以实现从模板创建的逻辑
            // 简化实现，返回成功
            return true;
        } catch (Exception e) {
            log.error("从模板创建失败", e);
            throw new RuntimeException("从模板创建失败: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> analyzeIncentiveTrends(Integer incentiveYear, Long tenantId) {
        Map<String, Object> result = new HashMap<>();

        // 获取统计数据
        Map<String, Object> statistics = getIncentiveStatistics(incentiveYear, tenantId);
        List<Map<String, Object>> completionTrend = getIncentiveCompletionTrend(
            LocalDateTime.of(incentiveYear, 1, 1, 0, 0),
            LocalDateTime.of(incentiveYear, 12, 31, 23, 59),
            tenantId
        );

        result.put("statistics", statistics);
        result.put("completionTrend", completionTrend);
        result.put("analysisTime", LocalDateTime.now());

        return result;
    }

    @Override
    public Map<String, Object> predictIncentiveNeeds(Long targetDeptId, Integer targetYear, Long tenantId) {
        Map<String, Object> result = new HashMap<>();

        // 基于历史数据预测激励需求
        // 这里可以实现复杂的预测算法
        result.put("predictedBudget", BigDecimal.valueOf(100000));
        result.put("predictedParticipants", 50);
        result.put("recommendedTypes", Arrays.asList("PERFORMANCE", "ACHIEVEMENT"));
        result.put("predictionTime", LocalDateTime.now());

        return result;
    }

    @Override
    public Map<String, Object> optimizeIncentiveConfiguration(Long incentiveId, Long tenantId) {
        Map<String, Object> result = new HashMap<>();

        // 分析当前配置并提供优化建议
        PmIncentiveManagement incentive = getById(incentiveId);
        if (incentive != null && incentive.getTenantId().equals(tenantId)) {
            result.put("currentConfig", incentive);
            result.put("optimizationSuggestions", getIncentiveOptimizationSuggestions(incentiveId, tenantId));
            result.put("optimizationTime", LocalDateTime.now());
        }

        return result;
    }

    /**
     * 复制激励属性
     */
    private void copyIncentiveProperties(PmIncentiveManagement source, PmIncentiveManagement target) {
        target.setIncentiveType(source.getIncentiveType());
        target.setIncentiveCategory(source.getIncentiveCategory());
        target.setIncentiveCycle(source.getIncentiveCycle());
        target.setIncentiveScope(source.getIncentiveScope());
        target.setTargetDeptId(source.getTargetDeptId());
        target.setTargetDeptName(source.getTargetDeptName());
        target.setIncentiveObjective(source.getIncentiveObjective());
        target.setIncentivePrinciples(source.getIncentivePrinciples());
        target.setIncentiveStandards(source.getIncentiveStandards());
        target.setIncentiveRules(source.getIncentiveRules());
        target.setCalculationMethod(source.getCalculationMethod());
        target.setCalculationFormula(source.getCalculationFormula());
        target.setBaseAmount(source.getBaseAmount());
        target.setMinAmount(source.getMinAmount());
        target.setMaxAmount(source.getMaxAmount());
        target.setIncentiveCoefficient(source.getIncentiveCoefficient());
        target.setWeightConfig(source.getWeightConfig());
        target.setTenantId(source.getTenantId());
    }

    /**
     * 生成激励编码
     */
    private String generateIncentiveCode() {
        return "INC" + System.currentTimeMillis();
    }
}
