package com.management.accountant.service.ss;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.entity.ss.SsCostAllocation;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 成本分摊服务接口
 *
 * @author AI Assistant
 * @since 2024-01-15
 */
public interface SsCostAllocationService extends IService<SsCostAllocation> {

    /**
     * 分页查询成本分摊列表
     */
    IPage<SsCostAllocation> getCostAllocationPage(Integer current, Integer size, Map<String, Object> params);

    /**
     * 根据ID获取成本分摊详情
     */
    SsCostAllocation getCostAllocationById(Long allocationId);

    /**
     * 创建成本分摊
     */
    boolean createCostAllocation(SsCostAllocation costAllocation);

    /**
     * 更新成本分摊
     */
    boolean updateCostAllocation(SsCostAllocation costAllocation);

    /**
     * 删除成本分摊
     */
    boolean deleteCostAllocation(Long allocationId);

    /**
     * 批量删除成本分摊
     */
    boolean batchDeleteCostAllocation(List<Long> allocationIds);

    /**
     * 根据状态查询成本分摊列表
     */
    List<SsCostAllocation> getCostAllocationByStatus(String status);

    /**
     * 根据类型查询成本分摊列表
     */
    List<SsCostAllocation> getCostAllocationByType(String type);

    /**
     * 根据成本中心查询成本分摊列表
     */
    List<SsCostAllocation> getCostAllocationByCostCenter(Long costCenterId);

    /**
     * 根据分摊方法查询成本分摊列表
     */
    List<SsCostAllocation> getCostAllocationByMethod(String method);

    /**
     * 根据分摊周期查询成本分摊列表
     */
    List<SsCostAllocation> getCostAllocationByPeriod(String period);

    /**
     * 根据时间范围查询成本分摊列表
     */
    List<SsCostAllocation> getCostAllocationByTimeRange(LocalDateTime startTime, LocalDateTime endTime);

    /**
     * 根据金额范围查询成本分摊列表
     */
    List<SsCostAllocation> getCostAllocationByAmountRange(BigDecimal minAmount, BigDecimal maxAmount);

    /**
     * 根据优先级查询成本分摊列表
     */
    List<SsCostAllocation> getCostAllocationByPriority(Integer priority);

    /**
     * 根据审批状态查询成本分摊列表
     */
    List<SsCostAllocation> getCostAllocationByApprovalStatus(String approvalStatus);

    /**
     * 根据计算状态查询成本分摊列表
     */
    List<SsCostAllocation> getCostAllocationByCalculationStatus(String calculationStatus);

    /**
     * 根据创建人查询成本分摊列表
     */
    List<SsCostAllocation> getCostAllocationByCreator(Long createdBy);

    /**
     * 根据审批人查询成本分摊列表
     */
    List<SsCostAllocation> getCostAllocationByApprover(Long approverId);

    /**
     * 查询待分摊的成本分摊列表
     */
    List<SsCostAllocation> getPendingAllocation();

    /**
     * 查询进行中的成本分摊列表
     */
    List<SsCostAllocation> getActiveAllocation();

    /**
     * 查询已完成的成本分摊列表
     */
    List<SsCostAllocation> getCompletedAllocation();

    /**
     * 查询失败的成本分摊列表
     */
    List<SsCostAllocation> getFailedAllocation();

    /**
     * 查询超时的成本分摊列表
     */
    List<SsCostAllocation> getTimeoutAllocation(Integer timeoutMinutes);

    /**
     * 查询高优先级的成本分摊列表
     */
    List<SsCostAllocation> getHighPriorityAllocation();

    /**
     * 查询自动分摊的成本分摊列表
     */
    List<SsCostAllocation> getAutoAllocation();

    /**
     * 查询实时分摊的成本分摊列表
     */
    List<SsCostAllocation> getRealtimeAllocation();

    /**
     * 查询需要审批的成本分摊列表
     */
    List<SsCostAllocation> getPendingApproval();

    /**
     * 查询已审批的成本分摊列表
     */
    List<SsCostAllocation> getApprovedAllocation();

    /**
     * 查询被拒绝的成本分摊列表
     */
    List<SsCostAllocation> getRejectedAllocation();

    /**
     * 根据父分摊ID查询子分摊列表
     */
    List<SsCostAllocation> getCostAllocationByParentId(Long parentAllocationId);

    /**
     * 根据分摊层级查询成本分摊列表
     */
    List<SsCostAllocation> getCostAllocationByLevel(Integer level);

    /**
     * 根据分摊深度查询成本分摊列表
     */
    List<SsCostAllocation> getCostAllocationByDepth(Integer depth);

    /**
     * 开始分摊计算
     */
    boolean startAllocationCalculation(Long allocationId);

    /**
     * 停止分摊计算
     */
    boolean stopAllocationCalculation(Long allocationId);

    /**
     * 暂停分摊计算
     */
    boolean pauseAllocationCalculation(Long allocationId);

    /**
     * 恢复分摊计算
     */
    boolean resumeAllocationCalculation(Long allocationId);

    /**
     * 重新计算分摊
     */
    boolean recalculateAllocation(Long allocationId);

    /**
     * 批量开始分摊计算
     */
    boolean batchStartCalculation(List<Long> allocationIds);

    /**
     * 批量停止分摊计算
     */
    boolean batchStopCalculation(List<Long> allocationIds);

    /**
     * 批量暂停分摊计算
     */
    boolean batchPauseCalculation(List<Long> allocationIds);

    /**
     * 批量恢复分摊计算
     */
    boolean batchResumeCalculation(List<Long> allocationIds);

    /**
     * 提交审批
     */
    boolean submitForApproval(Long allocationId);

    /**
     * 审批通过
     */
    boolean approveAllocation(Long allocationId, String comments);

    /**
     * 审批拒绝
     */
    boolean rejectAllocation(Long allocationId, String comments);

    /**
     * 取消审批
     */
    boolean cancelApproval(Long allocationId);

    /**
     * 批量审批通过
     */
    boolean batchApprove(List<Long> allocationIds, String comments);

    /**
     * 批量审批拒绝
     */
    boolean batchReject(List<Long> allocationIds, String comments);

    /**
     * 更新分摊状态
     */
    boolean updateAllocationStatus(Long allocationId, String status);

    /**
     * 更新计算状态
     */
    boolean updateCalculationStatus(Long allocationId, String status);

    /**
     * 更新审批状态
     */
    boolean updateApprovalStatus(Long allocationId, String status);

    /**
     * 更新分摊结果
     */
    boolean updateAllocationResult(Long allocationId, String result);

    /**
     * 更新分摊进度
     */
    boolean updateAllocationProgress(Long allocationId, Integer completedCount);

    /**
     * 批量更新状态
     */
    boolean batchUpdateStatus(List<Long> allocationIds, String status);

    /**
     * 批量更新优先级
     */
    boolean batchUpdatePriority(List<Long> allocationIds, Integer priority);

    /**
     * 复制成本分摊
     */
    SsCostAllocation copyCostAllocation(Long allocationId);

    /**
     * 导入成本分摊数据
     */
    boolean importCostAllocationData(List<SsCostAllocation> costAllocations);

    /**
     * 导出成本分摊数据
     */
    List<SsCostAllocation> exportCostAllocationData(Map<String, Object> params);

    /**
     * 生成分摊报告
     */
    String generateAllocationReport(Long allocationId);

    /**
     * 生成批量分摊报告
     */
    String generateBatchAllocationReport(List<Long> allocationIds);

    /**
     * 发送分摊通知
     */
    boolean sendAllocationNotification(Long allocationId, String notificationType);

    /**
     * 批量发送分摊通知
     */
    boolean batchSendNotification(List<Long> allocationIds, String notificationType);

    /**
     * 查询成本分摊统计信息
     */
    Map<String, Object> getAllocationStatistics();

    /**
     * 查询成本分摊状态分布
     */
    List<Map<String, Object>> getStatusDistribution();

    /**
     * 查询成本分摊类型分布
     */
    List<Map<String, Object>> getTypeDistribution();

    /**
     * 查询成本分摊方法分布
     */
    List<Map<String, Object>> getMethodDistribution();

    /**
     * 查询成本分摊周期分布
     */
    List<Map<String, Object>> getPeriodDistribution();

    /**
     * 查询成本分摊优先级分布
     */
    List<Map<String, Object>> getPriorityDistribution();

    /**
     * 查询成本分摊金额统计
     */
    Map<String, Object> getAmountStatistics();

    /**
     * 查询成本分摊成功率统计
     */
    Map<String, Object> getSuccessRateStatistics();

    /**
     * 查询成本分摊耗时统计
     */
    Map<String, Object> getDurationStatistics();

    /**
     * 查询成本分摊趋势数据
     */
    List<Map<String, Object>> getAllocationTrend(String startDate, String endDate);

    /**
     * 查询成本分摊排行榜
     */
    List<Map<String, Object>> getAllocationRanking(String rankType, Integer limit);

    /**
     * 查询成本分摊效率分析
     */
    List<Map<String, Object>> getEfficiencyAnalysis();

    /**
     * 查询成本分摊质量分析
     */
    List<Map<String, Object>> getQualityAnalysis();

    /**
     * 查询成本分摊成本分析
     */
    List<Map<String, Object>> getCostAnalysis();

    /**
     * 查询成本分摊风险分析
     */
    List<Map<String, Object>> getRiskAnalysis();

    /**
     * 清理过期数据
     */
    boolean cleanExpiredData(Integer expiredDays);

    /**
     * 检查编码是否存在
     */
    boolean checkCodeExists(String allocationCode, Long allocationId);

    /**
     * 检查名称是否存在
     */
    boolean checkNameExists(String allocationName, Long allocationId);

    /**
     * 验证分摊数据
     */
    boolean validateAllocationData(SsCostAllocation costAllocation);

    /**
     * 计算分摊金额
     */
    BigDecimal calculateAllocationAmount(SsCostAllocation costAllocation);

    /**
     * 计算分摊比例
     */
    BigDecimal calculateAllocationPercentage(SsCostAllocation costAllocation);

    /**
     * 执行分摊计算
     */
    Map<String, Object> executeAllocationCalculation(SsCostAllocation costAllocation);

    /**
     * 分摊结果分析
     */
    Map<String, Object> analyzeAllocationResult(Long allocationId);
}
