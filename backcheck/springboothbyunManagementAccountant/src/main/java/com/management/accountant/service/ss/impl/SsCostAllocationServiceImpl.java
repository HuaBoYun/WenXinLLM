package com.management.accountant.service.ss.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.management.accountant.entity.ss.SsCostAllocation;
import com.management.accountant.mapper.ss.SsCostAllocationMapper;
import com.management.accountant.service.ss.SsCostAllocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 成本分摊服务实现类
 *
 * @author AI Assistant
 * @since 2024-01-15
 */
@Slf4j
@Service
public class SsCostAllocationServiceImpl extends ServiceImpl<SsCostAllocationMapper, SsCostAllocation> implements SsCostAllocationService {

    @Autowired
    private SsCostAllocationMapper costAllocationMapper;

    private static final Long DEFAULT_TENANT_ID = 1L;

    @Override
    public IPage<SsCostAllocation> getCostAllocationPage(Integer current, Integer size, Map<String, Object> params) {
        Page<SsCostAllocation> page = new Page<>(current, size);
        return costAllocationMapper.selectCostAllocationPage(page, params);
    }

    @Override
    public SsCostAllocation getCostAllocationById(Long allocationId) {
        if (allocationId == null) {
            return null;
        }
        return costAllocationMapper.selectById(allocationId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createCostAllocation(SsCostAllocation costAllocation) {
        if (costAllocation == null) {
            return false;
        }

        // 设置默认值
        if (costAllocation.getTenantId() == null) {
            costAllocation.setTenantId(DEFAULT_TENANT_ID);
        }
        if (!StringUtils.hasText(costAllocation.getAllocationStatus())) {
            costAllocation.setAllocationStatus("DRAFT");
        }
        if (!StringUtils.hasText(costAllocation.getCalculationStatus())) {
            costAllocation.setCalculationStatus("PENDING");
        }
        if (costAllocation.getPriority() == null) {
            costAllocation.setPriority(5);
        }
        if (costAllocation.getIsAutoAllocation() == null) {
            costAllocation.setIsAutoAllocation(false);
        }
        if (costAllocation.getIsRealtimeAllocation() == null) {
            costAllocation.setIsRealtimeAllocation(false);
        }
        if (costAllocation.getIsApprovalEnabled() == null) {
            costAllocation.setIsApprovalEnabled(false);
        }

        // 生成编码
        if (!StringUtils.hasText(costAllocation.getAllocationCode())) {
            costAllocation.setAllocationCode(generateAllocationCode());
        }

        // 验证数据
        if (!validateAllocationData(costAllocation)) {
            return false;
        }

        return costAllocationMapper.insert(costAllocation) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateCostAllocation(SsCostAllocation costAllocation) {
        if (costAllocation == null || costAllocation.getAllocationId() == null) {
            return false;
        }

        // 验证数据
        if (!validateAllocationData(costAllocation)) {
            return false;
        }

        return costAllocationMapper.updateById(costAllocation) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteCostAllocation(Long allocationId) {
        if (allocationId == null) {
            return false;
        }
        return costAllocationMapper.deleteById(allocationId) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteCostAllocation(List<Long> allocationIds) {
        if (allocationIds == null || allocationIds.isEmpty()) {
            return false;
        }
        return costAllocationMapper.batchDelete(allocationIds, DEFAULT_TENANT_ID) > 0;
    }

    @Override
    public List<SsCostAllocation> getCostAllocationByStatus(String status) {
        return costAllocationMapper.selectByStatus(status, DEFAULT_TENANT_ID);
    }

    @Override
    public List<SsCostAllocation> getCostAllocationByType(String type) {
        return costAllocationMapper.selectByType(type, DEFAULT_TENANT_ID);
    }

    @Override
    public List<SsCostAllocation> getCostAllocationByCostCenter(Long costCenterId) {
        return costAllocationMapper.selectByCostCenter(costCenterId, DEFAULT_TENANT_ID);
    }

    @Override
    public List<SsCostAllocation> getCostAllocationByMethod(String method) {
        return costAllocationMapper.selectByMethod(method, DEFAULT_TENANT_ID);
    }

    @Override
    public List<SsCostAllocation> getCostAllocationByPeriod(String period) {
        return costAllocationMapper.selectByPeriod(period, DEFAULT_TENANT_ID);
    }

    @Override
    public List<SsCostAllocation> getCostAllocationByTimeRange(LocalDateTime startTime, LocalDateTime endTime) {
        return costAllocationMapper.selectByTimeRange(startTime, endTime, DEFAULT_TENANT_ID);
    }

    @Override
    public List<SsCostAllocation> getCostAllocationByAmountRange(BigDecimal minAmount, BigDecimal maxAmount) {
        return costAllocationMapper.selectByAmountRange(minAmount, maxAmount, DEFAULT_TENANT_ID);
    }

    @Override
    public List<SsCostAllocation> getCostAllocationByPriority(Integer priority) {
        return costAllocationMapper.selectByPriority(priority, DEFAULT_TENANT_ID);
    }

    @Override
    public List<SsCostAllocation> getCostAllocationByApprovalStatus(String approvalStatus) {
        return costAllocationMapper.selectByApprovalStatus(approvalStatus, DEFAULT_TENANT_ID);
    }

    @Override
    public List<SsCostAllocation> getCostAllocationByCalculationStatus(String calculationStatus) {
        return costAllocationMapper.selectByCalculationStatus(calculationStatus, DEFAULT_TENANT_ID);
    }

    @Override
    public List<SsCostAllocation> getCostAllocationByCreator(Long createdBy) {
        return costAllocationMapper.selectByCreator(createdBy, DEFAULT_TENANT_ID);
    }

    @Override
    public List<SsCostAllocation> getCostAllocationByApprover(Long approverId) {
        return costAllocationMapper.selectByApprover(approverId, DEFAULT_TENANT_ID);
    }

    @Override
    public List<SsCostAllocation> getPendingAllocation() {
        return costAllocationMapper.selectPendingAllocation(DEFAULT_TENANT_ID);
    }

    @Override
    public List<SsCostAllocation> getActiveAllocation() {
        return costAllocationMapper.selectActiveAllocation(DEFAULT_TENANT_ID);
    }

    @Override
    public List<SsCostAllocation> getCompletedAllocation() {
        return costAllocationMapper.selectCompletedAllocation(DEFAULT_TENANT_ID);
    }

    @Override
    public List<SsCostAllocation> getFailedAllocation() {
        return costAllocationMapper.selectFailedAllocation(DEFAULT_TENANT_ID);
    }

    @Override
    public List<SsCostAllocation> getTimeoutAllocation(Integer timeoutMinutes) {
        return costAllocationMapper.selectTimeoutAllocation(timeoutMinutes, DEFAULT_TENANT_ID);
    }

    @Override
    public List<SsCostAllocation> getHighPriorityAllocation() {
        return costAllocationMapper.selectHighPriorityAllocation(DEFAULT_TENANT_ID);
    }

    @Override
    public List<SsCostAllocation> getAutoAllocation() {
        return costAllocationMapper.selectAutoAllocation(DEFAULT_TENANT_ID);
    }

    @Override
    public List<SsCostAllocation> getRealtimeAllocation() {
        return costAllocationMapper.selectRealtimeAllocation(DEFAULT_TENANT_ID);
    }

    @Override
    public List<SsCostAllocation> getPendingApproval() {
        return costAllocationMapper.selectPendingApproval(DEFAULT_TENANT_ID);
    }

    @Override
    public List<SsCostAllocation> getApprovedAllocation() {
        return costAllocationMapper.selectApprovedAllocation(DEFAULT_TENANT_ID);
    }

    @Override
    public List<SsCostAllocation> getRejectedAllocation() {
        return costAllocationMapper.selectRejectedAllocation(DEFAULT_TENANT_ID);
    }

    @Override
    public List<SsCostAllocation> getCostAllocationByParentId(Long parentAllocationId) {
        return costAllocationMapper.selectByParentId(parentAllocationId, DEFAULT_TENANT_ID);
    }

    @Override
    public List<SsCostAllocation> getCostAllocationByLevel(Integer level) {
        return costAllocationMapper.selectByLevel(level, DEFAULT_TENANT_ID);
    }

    @Override
    public List<SsCostAllocation> getCostAllocationByDepth(Integer depth) {
        return costAllocationMapper.selectByDepth(depth, DEFAULT_TENANT_ID);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean startAllocationCalculation(Long allocationId) {
        if (allocationId == null) {
            return false;
        }

        SsCostAllocation allocation = getCostAllocationById(allocationId);
        if (allocation == null) {
            return false;
        }

        // 更新状态为计算中
        allocation.setAllocationStatus("CALCULATING");
        allocation.setCalculationStatus("RUNNING");
        allocation.setCalculationStartTime(LocalDateTime.now());

        boolean updated = updateCostAllocation(allocation);
        if (updated) {
            // 执行分摊计算
            executeAllocationCalculation(allocation);
        }

        return updated;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean stopAllocationCalculation(Long allocationId) {
        if (allocationId == null) {
            return false;
        }

        return costAllocationMapper.updateCalculationStatus(allocationId, "CANCELLED", DEFAULT_TENANT_ID) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean pauseAllocationCalculation(Long allocationId) {
        if (allocationId == null) {
            return false;
        }

        return costAllocationMapper.updateCalculationStatus(allocationId, "PAUSED", DEFAULT_TENANT_ID) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean resumeAllocationCalculation(Long allocationId) {
        if (allocationId == null) {
            return false;
        }

        return costAllocationMapper.updateCalculationStatus(allocationId, "RUNNING", DEFAULT_TENANT_ID) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean recalculateAllocation(Long allocationId) {
        if (allocationId == null) {
            return false;
        }

        SsCostAllocation allocation = getCostAllocationById(allocationId);
        if (allocation == null) {
            return false;
        }

        // 重置计算状态
        allocation.setCalculationStatus("PENDING");
        allocation.setCalculationStartTime(null);
        allocation.setCalculationEndTime(null);
        allocation.setCalculationDuration(null);
        allocation.setCalculationResult(null);
        allocation.setCalculationError(null);

        return updateCostAllocation(allocation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchStartCalculation(List<Long> allocationIds) {
        if (allocationIds == null || allocationIds.isEmpty()) {
            return false;
        }

        for (Long allocationId : allocationIds) {
            startAllocationCalculation(allocationId);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchStopCalculation(List<Long> allocationIds) {
        if (allocationIds == null || allocationIds.isEmpty()) {
            return false;
        }

        return costAllocationMapper.batchUpdateStatus(allocationIds, "CANCELLED", DEFAULT_TENANT_ID) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchPauseCalculation(List<Long> allocationIds) {
        if (allocationIds == null || allocationIds.isEmpty()) {
            return false;
        }

        for (Long allocationId : allocationIds) {
            pauseAllocationCalculation(allocationId);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchResumeCalculation(List<Long> allocationIds) {
        if (allocationIds == null || allocationIds.isEmpty()) {
            return false;
        }

        for (Long allocationId : allocationIds) {
            resumeAllocationCalculation(allocationId);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean submitForApproval(Long allocationId) {
        if (allocationId == null) {
            return false;
        }

        return costAllocationMapper.updateApprovalStatus(allocationId, "PENDING", DEFAULT_TENANT_ID) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean approveAllocation(Long allocationId, String comments) {
        if (allocationId == null) {
            return false;
        }

        SsCostAllocation allocation = getCostAllocationById(allocationId);
        if (allocation == null) {
            return false;
        }

        allocation.setApprovalStatus("APPROVED");
        allocation.setApprovalTime(LocalDateTime.now());
        allocation.setApprovalComments(comments);

        return updateCostAllocation(allocation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean rejectAllocation(Long allocationId, String comments) {
        if (allocationId == null) {
            return false;
        }

        SsCostAllocation allocation = getCostAllocationById(allocationId);
        if (allocation == null) {
            return false;
        }

        allocation.setApprovalStatus("REJECTED");
        allocation.setApprovalTime(LocalDateTime.now());
        allocation.setApprovalComments(comments);

        return updateCostAllocation(allocation);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelApproval(Long allocationId) {
        if (allocationId == null) {
            return false;
        }

        return costAllocationMapper.updateApprovalStatus(allocationId, "CANCELLED", DEFAULT_TENANT_ID) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchApprove(List<Long> allocationIds, String comments) {
        if (allocationIds == null || allocationIds.isEmpty()) {
            return false;
        }

        for (Long allocationId : allocationIds) {
            approveAllocation(allocationId, comments);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchReject(List<Long> allocationIds, String comments) {
        if (allocationIds == null || allocationIds.isEmpty()) {
            return false;
        }

        for (Long allocationId : allocationIds) {
            rejectAllocation(allocationId, comments);
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateAllocationStatus(Long allocationId, String status) {
        if (allocationId == null || !StringUtils.hasText(status)) {
            return false;
        }

        return costAllocationMapper.updateAllocationStatus(allocationId, status, DEFAULT_TENANT_ID) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateCalculationStatus(Long allocationId, String status) {
        if (allocationId == null || !StringUtils.hasText(status)) {
            return false;
        }

        return costAllocationMapper.updateCalculationStatus(allocationId, status, DEFAULT_TENANT_ID) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateApprovalStatus(Long allocationId, String status) {
        if (allocationId == null || !StringUtils.hasText(status)) {
            return false;
        }

        return costAllocationMapper.updateApprovalStatus(allocationId, status, DEFAULT_TENANT_ID) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateAllocationResult(Long allocationId, String result) {
        if (allocationId == null) {
            return false;
        }

        return costAllocationMapper.updateAllocationResult(allocationId, result, DEFAULT_TENANT_ID) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateAllocationProgress(Long allocationId, Integer completedCount) {
        if (allocationId == null || completedCount == null) {
            return false;
        }

        return costAllocationMapper.updateAllocationProgress(allocationId, completedCount, DEFAULT_TENANT_ID) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateStatus(List<Long> allocationIds, String status) {
        if (allocationIds == null || allocationIds.isEmpty() || !StringUtils.hasText(status)) {
            return false;
        }

        return costAllocationMapper.batchUpdateStatus(allocationIds, status, DEFAULT_TENANT_ID) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdatePriority(List<Long> allocationIds, Integer priority) {
        if (allocationIds == null || allocationIds.isEmpty() || priority == null) {
            return false;
        }

        return costAllocationMapper.batchUpdatePriority(allocationIds, priority, DEFAULT_TENANT_ID) > 0;
    }

    @Override
    public SsCostAllocation copyCostAllocation(Long allocationId) {
        if (allocationId == null) {
            return null;
        }

        SsCostAllocation original = getCostAllocationById(allocationId);
        if (original == null) {
            return null;
        }

        SsCostAllocation copy = new SsCostAllocation();
        // 复制基本信息
        copy.setAllocationName(original.getAllocationName() + "_副本");
        copy.setAllocationDescription(original.getAllocationDescription());
        copy.setAllocationType(original.getAllocationType());
        copy.setAllocationBasis(original.getAllocationBasis());
        copy.setAllocationMethod(original.getAllocationMethod());
        copy.setAllocationRules(original.getAllocationRules());
        copy.setAllocationFormula(original.getAllocationFormula());
        copy.setAllocationPeriod(original.getAllocationPeriod());
        copy.setPriority(original.getPriority());
        copy.setCostCenterId(original.getCostCenterId());
        copy.setCostCenterCode(original.getCostCenterCode());
        copy.setCostCenterName(original.getCostCenterName());

        // 设置默认状态
        copy.setAllocationStatus("DRAFT");
        copy.setCalculationStatus("PENDING");
        copy.setTenantId(original.getTenantId());

        return copy;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean importCostAllocationData(List<SsCostAllocation> costAllocations) {
        if (costAllocations == null || costAllocations.isEmpty()) {
            return false;
        }

        return costAllocationMapper.batchInsert(costAllocations) > 0;
    }

    @Override
    public List<SsCostAllocation> exportCostAllocationData(Map<String, Object> params) {
        QueryWrapper<SsCostAllocation> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tenant_id", DEFAULT_TENANT_ID);

        if (params != null) {
            if (params.containsKey("status")) {
                queryWrapper.eq("allocation_status", params.get("status"));
            }
            if (params.containsKey("type")) {
                queryWrapper.eq("allocation_type", params.get("type"));
            }
            if (params.containsKey("startTime")) {
                queryWrapper.ge("created_time", params.get("startTime"));
            }
            if (params.containsKey("endTime")) {
                queryWrapper.le("created_time", params.get("endTime"));
            }
        }

        return costAllocationMapper.selectList(queryWrapper);
    }

    @Override
    public String generateAllocationReport(Long allocationId) {
        if (allocationId == null) {
            return null;
        }

        SsCostAllocation allocation = getCostAllocationById(allocationId);
        if (allocation == null) {
            return null;
        }

        // 生成分摊报告
        StringBuilder report = new StringBuilder();
        report.append("成本分摊报告\n");
        report.append("分摊编码: ").append(allocation.getAllocationCode()).append("\n");
        report.append("分摊名称: ").append(allocation.getAllocationName()).append("\n");
        report.append("分摊类型: ").append(allocation.getAllocationType()).append("\n");
        report.append("分摊状态: ").append(allocation.getAllocationStatus()).append("\n");
        report.append("总成本金额: ").append(allocation.getTotalCostAmount()).append("\n");
        report.append("已分摊金额: ").append(allocation.getAllocatedAmount()).append("\n");
        report.append("未分摊金额: ").append(allocation.getUnallocatedAmount()).append("\n");
        report.append("分摊比例: ").append(allocation.getAllocationPercentage()).append("%\n");
        report.append("成功率: ").append(allocation.getSuccessRate()).append("%\n");

        return report.toString();
    }

    @Override
    public String generateBatchAllocationReport(List<Long> allocationIds) {
        if (allocationIds == null || allocationIds.isEmpty()) {
            return null;
        }

        StringBuilder report = new StringBuilder();
        report.append("批量成本分摊报告\n");
        report.append("分摊数量: ").append(allocationIds.size()).append("\n\n");

        for (Long allocationId : allocationIds) {
            String singleReport = generateAllocationReport(allocationId);
            if (singleReport != null) {
                report.append(singleReport).append("\n");
            }
        }

        return report.toString();
    }

    @Override
    public boolean sendAllocationNotification(Long allocationId, String notificationType) {
        if (allocationId == null || !StringUtils.hasText(notificationType)) {
            return false;
        }

        // 发送通知逻辑
        log.info("发送成本分摊通知: allocationId={}, notificationType={}", allocationId, notificationType);
        return true;
    }

    @Override
    public boolean batchSendNotification(List<Long> allocationIds, String notificationType) {
        if (allocationIds == null || allocationIds.isEmpty() || !StringUtils.hasText(notificationType)) {
            return false;
        }

        for (Long allocationId : allocationIds) {
            sendAllocationNotification(allocationId, notificationType);
        }
        return true;
    }

    @Override
    public Map<String, Object> getAllocationStatistics() {
        return costAllocationMapper.selectAllocationStatistics(DEFAULT_TENANT_ID);
    }

    @Override
    public List<Map<String, Object>> getStatusDistribution() {
        return costAllocationMapper.selectStatusDistribution(DEFAULT_TENANT_ID);
    }

    @Override
    public List<Map<String, Object>> getTypeDistribution() {
        return costAllocationMapper.selectTypeDistribution(DEFAULT_TENANT_ID);
    }

    @Override
    public List<Map<String, Object>> getMethodDistribution() {
        return costAllocationMapper.selectMethodDistribution(DEFAULT_TENANT_ID);
    }

    @Override
    public List<Map<String, Object>> getPeriodDistribution() {
        return costAllocationMapper.selectPeriodDistribution(DEFAULT_TENANT_ID);
    }

    @Override
    public List<Map<String, Object>> getPriorityDistribution() {
        return costAllocationMapper.selectPriorityDistribution(DEFAULT_TENANT_ID);
    }

    @Override
    public Map<String, Object> getAmountStatistics() {
        return costAllocationMapper.selectAmountStatistics(DEFAULT_TENANT_ID);
    }

    @Override
    public Map<String, Object> getSuccessRateStatistics() {
        return costAllocationMapper.selectSuccessRateStatistics(DEFAULT_TENANT_ID);
    }

    @Override
    public Map<String, Object> getDurationStatistics() {
        return costAllocationMapper.selectDurationStatistics(DEFAULT_TENANT_ID);
    }

    @Override
    public List<Map<String, Object>> getAllocationTrend(String startDate, String endDate) {
        return costAllocationMapper.selectAllocationTrend(startDate, endDate, DEFAULT_TENANT_ID);
    }

    @Override
    public List<Map<String, Object>> getAllocationRanking(String rankType, Integer limit) {
        return costAllocationMapper.selectAllocationRanking(rankType, limit, DEFAULT_TENANT_ID);
    }

    @Override
    public List<Map<String, Object>> getEfficiencyAnalysis() {
        return costAllocationMapper.selectEfficiencyAnalysis(DEFAULT_TENANT_ID);
    }

    @Override
    public List<Map<String, Object>> getQualityAnalysis() {
        return costAllocationMapper.selectQualityAnalysis(DEFAULT_TENANT_ID);
    }

    @Override
    public List<Map<String, Object>> getCostAnalysis() {
        return costAllocationMapper.selectCostAnalysis(DEFAULT_TENANT_ID);
    }

    @Override
    public List<Map<String, Object>> getRiskAnalysis() {
        return costAllocationMapper.selectRiskAnalysis(DEFAULT_TENANT_ID);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cleanExpiredData(Integer expiredDays) {
        if (expiredDays == null || expiredDays <= 0) {
            expiredDays = 30; // 默认30天
        }

        return costAllocationMapper.cleanExpiredData(expiredDays, DEFAULT_TENANT_ID) >= 0;
    }

    @Override
    public boolean checkCodeExists(String allocationCode, Long allocationId) {
        if (!StringUtils.hasText(allocationCode)) {
            return false;
        }

        return costAllocationMapper.checkCodeExists(allocationCode, allocationId, DEFAULT_TENANT_ID) > 0;
    }

    @Override
    public boolean checkNameExists(String allocationName, Long allocationId) {
        if (!StringUtils.hasText(allocationName)) {
            return false;
        }

        return costAllocationMapper.checkNameExists(allocationName, allocationId, DEFAULT_TENANT_ID) > 0;
    }

    @Override
    public boolean validateAllocationData(SsCostAllocation costAllocation) {
        if (costAllocation == null) {
            return false;
        }

        // 验证必填字段
        if (!StringUtils.hasText(costAllocation.getAllocationName())) {
            log.error("分摊名称不能为空");
            return false;
        }

        if (!StringUtils.hasText(costAllocation.getAllocationType())) {
            log.error("分摊类型不能为空");
            return false;
        }

        if (!StringUtils.hasText(costAllocation.getAllocationMethod())) {
            log.error("分摊方法不能为空");
            return false;
        }

        // 验证编码唯一性
        if (StringUtils.hasText(costAllocation.getAllocationCode())) {
            if (checkCodeExists(costAllocation.getAllocationCode(), costAllocation.getAllocationId())) {
                log.error("分摊编码已存在: {}", costAllocation.getAllocationCode());
                return false;
            }
        }

        // 验证名称唯一性
        if (checkNameExists(costAllocation.getAllocationName(), costAllocation.getAllocationId())) {
            log.error("分摊名称已存在: {}", costAllocation.getAllocationName());
            return false;
        }

        // 验证金额
        if (costAllocation.getTotalCostAmount() != null && costAllocation.getTotalCostAmount().compareTo(BigDecimal.ZERO) < 0) {
            log.error("总成本金额不能为负数");
            return false;
        }

        return true;
    }

    @Override
    public BigDecimal calculateAllocationAmount(SsCostAllocation costAllocation) {
        if (costAllocation == null || costAllocation.getTotalCostAmount() == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal totalAmount = costAllocation.getTotalCostAmount();
        BigDecimal percentage = costAllocation.getAllocationPercentage();

        if (percentage != null) {
            return totalAmount.multiply(percentage).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);
        }

        return BigDecimal.ZERO;
    }

    @Override
    public BigDecimal calculateAllocationPercentage(SsCostAllocation costAllocation) {
        if (costAllocation == null || costAllocation.getTotalCostAmount() == null || 
            costAllocation.getAllocatedAmount() == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal totalAmount = costAllocation.getTotalCostAmount();
        BigDecimal allocatedAmount = costAllocation.getAllocatedAmount();

        if (totalAmount.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        return allocatedAmount.multiply(new BigDecimal("100")).divide(totalAmount, 2, RoundingMode.HALF_UP);
    }

    @Override
    public Map<String, Object> executeAllocationCalculation(SsCostAllocation costAllocation) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            LocalDateTime startTime = LocalDateTime.now();
            
            // 执行分摊计算逻辑
            BigDecimal allocatedAmount = calculateAllocationAmount(costAllocation);
            BigDecimal percentage = calculateAllocationPercentage(costAllocation);
            
            // 更新分摊结果
            costAllocation.setAllocatedAmount(allocatedAmount);
            costAllocation.setAllocationPercentage(percentage);
            costAllocation.setCalculationStartTime(startTime);
            costAllocation.setCalculationEndTime(LocalDateTime.now());
            costAllocation.setCalculationStatus("COMPLETED");
            costAllocation.setAllocationStatus("COMPLETED");
            
            // 计算耗时
            long duration = java.time.Duration.between(startTime, LocalDateTime.now()).getSeconds();
            costAllocation.setCalculationDuration(duration);
            
            // 计算成功率
            if (costAllocation.getTargetCount() != null && costAllocation.getTargetCount() > 0) {
                BigDecimal successRate = new BigDecimal(costAllocation.getCompletedCount() != null ? costAllocation.getCompletedCount() : 0)
                    .multiply(new BigDecimal("100"))
                    .divide(new BigDecimal(costAllocation.getTargetCount()), 2, RoundingMode.HALF_UP);
                costAllocation.setSuccessRate(successRate);
            }
            
            updateCostAllocation(costAllocation);
            
            result.put("success", true);
            result.put("allocatedAmount", allocatedAmount);
            result.put("percentage", percentage);
            result.put("duration", duration);
            
        } catch (Exception e) {
            log.error("分摊计算失败", e);
            costAllocation.setCalculationStatus("FAILED");
            costAllocation.setCalculationError(e.getMessage());
            updateCostAllocation(costAllocation);
            
            result.put("success", false);
            result.put("error", e.getMessage());
        }
        
        return result;
    }

    @Override
    public Map<String, Object> analyzeAllocationResult(Long allocationId) {
        if (allocationId == null) {
            return new HashMap<>();
        }

        SsCostAllocation allocation = getCostAllocationById(allocationId);
        if (allocation == null) {
            return new HashMap<>();
        }

        Map<String, Object> analysis = new HashMap<>();
        analysis.put("allocationId", allocationId);
        analysis.put("allocationCode", allocation.getAllocationCode());
        analysis.put("allocationName", allocation.getAllocationName());
        analysis.put("totalAmount", allocation.getTotalCostAmount());
        analysis.put("allocatedAmount", allocation.getAllocatedAmount());
        analysis.put("unallocatedAmount", allocation.getUnallocatedAmount());
        analysis.put("percentage", allocation.getAllocationPercentage());
        analysis.put("successRate", allocation.getSuccessRate());
        analysis.put("duration", allocation.getCalculationDuration());
        analysis.put("status", allocation.getAllocationStatus());

        return analysis;
    }

    /**
     * 生成分摊编码
     */
    private String generateAllocationCode() {
        return "CA" + System.currentTimeMillis();
    }
}
