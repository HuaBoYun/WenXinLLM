package com.management.accountant.service.ncv65;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.entity.ncv65.BudgetAllocation;

import java.util.List;
import java.util.Map;

/**
 * NCV65全面预算系统 - 预算分配服务接口
 * 
 * @description 预算分配业务逻辑接口，支持预算资源的分配、调配和管理
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
public interface IBudgetAllocationService extends IService<BudgetAllocation> {

    // ==================== 基础CRUD操作 ====================

    /**
     * 创建预算分配
     * @param allocation 分配信息
     * @return 是否创建成功
     */
    boolean createBudgetAllocation(BudgetAllocation allocation);

    /**
     * 更新预算分配
     * @param allocation 分配信息
     * @return 是否更新成功
     */
    boolean updateBudgetAllocation(BudgetAllocation allocation);

    /**
     * 删除预算分配
     * @param id 分配ID
     * @return 是否删除成功
     */
    boolean deleteBudgetAllocation(String id);

    /**
     * 批量删除预算分配
     * @param ids 分配ID列表
     * @return 是否删除成功
     */
    boolean batchDeleteBudgetAllocations(List<String> ids);

    /**
     * 根据ID查询预算分配
     * @param id 分配ID
     * @return 分配信息
     */
    BudgetAllocation getBudgetAllocationById(String id);

    /**
     * 根据编码查询预算分配
     * @param allocationCode 分配编码
     * @return 分配信息
     */
    BudgetAllocation getBudgetAllocationByCode(String allocationCode);

    // ==================== 查询操作 ====================

    /**
     * 分页查询预算分配
     * @param current 当前页
     * @param size 页大小
     * @param params 查询参数
     * @return 分页结果
     */
    IPage<BudgetAllocation> getBudgetAllocationPage(Integer current, Integer size, Map<String, Object> params);

    /**
     * 根据预算年度查询分配列表
     * @param fiscalYear 预算年度
     * @return 分配列表
     */
    List<BudgetAllocation> getBudgetAllocationsByFiscalYear(Integer fiscalYear);

    /**
     * 根据分配类型查询分配列表
     * @param allocationType 分配类型
     * @return 分配列表
     */
    List<BudgetAllocation> getBudgetAllocationsByType(String allocationType);

    /**
     * 根据分配方式查询分配列表
     * @param allocationMethod 分配方式
     * @return 分配列表
     */
    List<BudgetAllocation> getBudgetAllocationsByMethod(String allocationMethod);

    /**
     * 根据源组织查询分配列表
     * @param sourceOrganizationId 源组织ID
     * @return 分配列表
     */
    List<BudgetAllocation> getBudgetAllocationsBySourceOrganization(String sourceOrganizationId);

    /**
     * 根据目标组织查询分配列表
     * @param targetOrganizationId 目标组织ID
     * @return 分配列表
     */
    List<BudgetAllocation> getBudgetAllocationsByTargetOrganization(String targetOrganizationId);

    /**
     * 根据指标ID查询分配列表
     * @param indicatorId 指标ID
     * @return 分配列表
     */
    List<BudgetAllocation> getBudgetAllocationsByIndicatorId(String indicatorId);

    /**
     * 根据审批状态查询分配列表
     * @param approvalStatus 审批状态
     * @return 分配列表
     */
    List<BudgetAllocation> getBudgetAllocationsByApprovalStatus(String approvalStatus);

    /**
     * 根据执行状态查询分配列表
     * @param executionStatus 执行状态
     * @return 分配列表
     */
    List<BudgetAllocation> getBudgetAllocationsByExecutionStatus(String executionStatus);

    /**
     * 查询我分配的预算列表
     * @param userId 用户ID
     * @return 我分配的预算列表
     */
    List<BudgetAllocation> getMyAllocations(String userId);

    /**
     * 查询待我审批的分配列表
     * @param userId 用户ID
     * @return 待我审批的分配列表
     */
    List<BudgetAllocation> getPendingApprovals(String userId);

    /**
     * 查询待执行的分配列表
     * @param userId 用户ID
     * @return 待执行的分配列表
     */
    List<BudgetAllocation> getPendingExecutions(String userId);

    /**
     * 查询自动分配列表
     * @return 自动分配列表
     */
    List<BudgetAllocation> getAutoAllocations();

    /**
     * 查询批量分配列表
     * @return 批量分配列表
     */
    List<BudgetAllocation> getBatchAllocations();

    /**
     * 查询可撤销的分配列表
     * @return 可撤销的分配列表
     */
    List<BudgetAllocation> getRevocableAllocations();

    /**
     * 查询执行失败的分配列表
     * @return 执行失败的分配列表
     */
    List<BudgetAllocation> getFailedAllocations();

    /**
     * 查询组织间的分配关系
     * @param sourceOrganizationId 源组织ID
     * @param targetOrganizationId 目标组织ID
     * @param fiscalYear 预算年度
     * @return 分配关系列表
     */
    List<BudgetAllocation> getAllocationsBetweenOrganizations(String sourceOrganizationId, String targetOrganizationId, Integer fiscalYear);

    /**
     * 查询指标的分配历史
     * @param indicatorId 指标ID
     * @param fiscalYear 预算年度
     * @return 分配历史列表
     */
    List<BudgetAllocation> getIndicatorAllocationHistory(String indicatorId, Integer fiscalYear);

    // ==================== 业务操作 ====================

    /**
     * 审批通过
     * @param allocationId 分配ID
     * @param approvalComments 审批意见
     * @return 是否审批成功
     */
    boolean approveAllocation(String allocationId, String approvalComments);

    /**
     * 审批拒绝
     * @param allocationId 分配ID
     * @param approvalComments 审批意见
     * @return 是否拒绝成功
     */
    boolean rejectAllocation(String allocationId, String approvalComments);

    /**
     * 开始执行
     * @param allocationId 分配ID
     * @return 是否开始执行成功
     */
    boolean startExecution(String allocationId);

    /**
     * 完成执行
     * @param allocationId 分配ID
     * @param executionResult 执行结果
     * @return 是否完成执行成功
     */
    boolean completeExecution(String allocationId, String executionResult);

    /**
     * 执行失败
     * @param allocationId 分配ID
     * @param executionError 执行错误信息
     * @return 是否标记失败成功
     */
    boolean failExecution(String allocationId, String executionError);

    /**
     * 撤销分配
     * @param allocationId 分配ID
     * @param revocationInfo 撤销信息
     * @return 是否撤销成功
     */
    boolean revokeAllocation(String allocationId, String revocationInfo);

    /**
     * 复制分配
     * @param sourceAllocationId 源分配ID
     * @param newAllocationCode 新分配编码
     * @param newAllocationName 新分配名称
     * @return 新分配ID
     */
    String copyAllocation(String sourceAllocationId, String newAllocationCode, String newAllocationName);

    /**
     * 批量审批
     * @param allocationIds 分配ID列表
     * @param approvalComments 审批意见
     * @param isApproved 是否通过
     * @return 审批成功的分配数量
     */
    int batchApproveAllocations(List<String> allocationIds, String approvalComments, boolean isApproved);

    /**
     * 批量执行
     * @param allocationIds 分配ID列表
     * @return 执行成功的分配数量
     */
    int batchExecuteAllocations(List<String> allocationIds);

    /**
     * 批量撤销
     * @param allocationIds 分配ID列表
     * @param revocationInfo 撤销信息
     * @return 撤销成功的分配数量
     */
    int batchRevokeAllocations(List<String> allocationIds, String revocationInfo);

    // ==================== 分配策略 ====================

    /**
     * 自动分配
     * @param organizationId 组织ID
     * @param indicatorId 指标ID
     * @param fiscalYear 预算年度
     * @param allocationStrategy 分配策略
     * @return 分配结果
     */
    Map<String, Object> autoAllocate(String organizationId, String indicatorId, Integer fiscalYear, String allocationStrategy);

    /**
     * 按比例分配
     * @param sourceOrganizationId 源组织ID
     * @param targetOrganizationIds 目标组织ID列表
     * @param indicatorId 指标ID
     * @param totalAmount 总金额
     * @param ratios 分配比例列表
     * @return 分配结果
     */
    Map<String, Object> allocateByRatio(String sourceOrganizationId, List<String> targetOrganizationIds, 
                                       String indicatorId, String totalAmount, List<String> ratios);

    /**
     * 按权重分配
     * @param sourceOrganizationId 源组织ID
     * @param targetOrganizationIds 目标组织ID列表
     * @param indicatorId 指标ID
     * @param totalAmount 总金额
     * @param weights 权重列表
     * @return 分配结果
     */
    Map<String, Object> allocateByWeight(String sourceOrganizationId, List<String> targetOrganizationIds, 
                                        String indicatorId, String totalAmount, List<String> weights);

    /**
     * 按公式分配
     * @param sourceOrganizationId 源组织ID
     * @param targetOrganizationIds 目标组织ID列表
     * @param indicatorId 指标ID
     * @param allocationFormula 分配公式
     * @return 分配结果
     */
    Map<String, Object> allocateByFormula(String sourceOrganizationId, List<String> targetOrganizationIds, 
                                         String indicatorId, String allocationFormula);

    /**
     * 按历史数据分配
     * @param sourceOrganizationId 源组织ID
     * @param targetOrganizationIds 目标组织ID列表
     * @param indicatorId 指标ID
     * @param totalAmount 总金额
     * @param referenceFiscalYear 参考年度
     * @return 分配结果
     */
    Map<String, Object> allocateByHistory(String sourceOrganizationId, List<String> targetOrganizationIds, 
                                         String indicatorId, String totalAmount, Integer referenceFiscalYear);

    /**
     * 按绩效分配
     * @param sourceOrganizationId 源组织ID
     * @param targetOrganizationIds 目标组织ID列表
     * @param indicatorId 指标ID
     * @param totalAmount 总金额
     * @param performanceMetric 绩效指标
     * @return 分配结果
     */
    Map<String, Object> allocateByPerformance(String sourceOrganizationId, List<String> targetOrganizationIds, 
                                             String indicatorId, String totalAmount, String performanceMetric);

    // ==================== 分配分析 ====================

    /**
     * 分析分配影响
     * @param allocationId 分配ID
     * @return 影响分析结果
     */
    Map<String, Object> analyzeAllocationImpact(String allocationId);

    /**
     * 评估分配合理性
     * @param allocationId 分配ID
     * @return 合理性评估结果
     */
    Map<String, Object> assessAllocationRationality(String allocationId);

    /**
     * 生成分配建议
     * @param organizationId 组织ID
     * @param indicatorId 指标ID
     * @param fiscalYear 预算年度
     * @return 分配建议
     */
    Map<String, Object> generateAllocationSuggestions(String organizationId, String indicatorId, Integer fiscalYear);

    /**
     * 预测分配效果
     * @param allocationId 分配ID
     * @return 效果预测结果
     */
    Map<String, Object> predictAllocationEffect(String allocationId);

    /**
     * 分配公平性分析
     * @param organizationId 组织ID
     * @param indicatorId 指标ID
     * @param fiscalYear 预算年度
     * @return 公平性分析结果
     */
    Map<String, Object> analyzeFairness(String organizationId, String indicatorId, Integer fiscalYear);

    // ==================== 统计分析 ====================

    /**
     * 获取分配统计信息
     * @return 统计信息
     */
    Map<String, Object> getBudgetAllocationStatistics();

    /**
     * 按分配类型统计数量
     * @return 统计结果
     */
    List<Map<String, Object>> getBudgetAllocationCountByType();

    /**
     * 按分配方式统计数量
     * @return 统计结果
     */
    List<Map<String, Object>> getBudgetAllocationCountByMethod();

    /**
     * 按审批状态统计数量
     * @return 统计结果
     */
    List<Map<String, Object>> getBudgetAllocationCountByApprovalStatus();

    /**
     * 按执行状态统计数量
     * @return 统计结果
     */
    List<Map<String, Object>> getBudgetAllocationCountByExecutionStatus();

    /**
     * 按年度统计分配数量
     * @return 统计结果
     */
    List<Map<String, Object>> getBudgetAllocationCountByYear();

    /**
     * 按月份统计分配数量
     * @param fiscalYear 预算年度
     * @return 统计结果
     */
    List<Map<String, Object>> getBudgetAllocationCountByMonth(Integer fiscalYear);

    /**
     * 统计分配金额
     * @param fiscalYear 预算年度
     * @return 分配金额统计
     */
    Map<String, Object> getBudgetAllocationAmountSummary(Integer fiscalYear);

    /**
     * 按组织统计分配金额
     * @param fiscalYear 预算年度
     * @return 各组织分配金额统计
     */
    List<Map<String, Object>> getBudgetAllocationAmountsByOrganization(Integer fiscalYear);

    /**
     * 按指标统计分配金额
     * @param fiscalYear 预算年度
     * @return 各指标分配金额统计
     */
    List<Map<String, Object>> getBudgetAllocationAmountsByIndicator(Integer fiscalYear);

    /**
     * 获取用户分配统计信息
     * @param userId 用户ID
     * @return 用户分配统计信息
     */
    Map<String, Object> getUserAllocationStatistics(String userId);

    /**
     * 获取分配趋势分析
     * @param months 月份数
     * @return 趋势分析数据
     */
    List<Map<String, Object>> getAllocationTrend(Integer months);

    /**
     * 获取分配效率分析
     * @param fiscalYear 预算年度
     * @return 效率分析数据
     */
    Map<String, Object> getAllocationEfficiencyAnalysis(Integer fiscalYear);

    // ==================== 数据导入导出 ====================

    /**
     * 导出分配数据
     * @param allocationIds 分配ID列表
     * @return 导出文件路径
     */
    String exportAllocations(List<String> allocationIds);

    /**
     * 导入分配数据
     * @param filePath 文件路径
     * @return 导入结果
     */
    Map<String, Object> importAllocations(String filePath);

    /**
     * 导出分配报告
     * @param fiscalYear 预算年度
     * @param organizationId 组织ID
     * @return 导出文件路径
     */
    String exportAllocationReport(Integer fiscalYear, String organizationId);

    /**
     * 导出分配分析报告
     * @param allocationId 分配ID
     * @return 导出文件路径
     */
    String exportAllocationAnalysisReport(String allocationId);

    // ==================== 数据清理 ====================

    /**
     * 清理已取消的分配
     * @param days 取消天数
     * @return 清理数量
     */
    int cleanupCancelledAllocations(Integer days);

    /**
     * 清理已完成的分配
     * @param days 完成天数
     * @return 清理数量
     */
    int cleanupCompletedAllocations(Integer days);

    /**
     * 归档历史分配
     * @param fiscalYear 预算年度
     * @return 归档数量
     */
    int archiveHistoricalAllocations(Integer fiscalYear);

    // ==================== 验证方法 ====================

    /**
     * 检查分配编码是否存在
     * @param allocationCode 分配编码
     * @param excludeId 排除的分配ID
     * @return 是否存在
     */
    boolean checkAllocationCodeExists(String allocationCode, String excludeId);

    /**
     * 检查分配名称是否存在
     * @param allocationName 分配名称
     * @param excludeId 排除的分配ID
     * @return 是否存在
     */
    boolean checkAllocationNameExists(String allocationName, String excludeId);

    /**
     * 检查用户是否有分配操作权限
     * @param allocationId 分配ID
     * @param userId 用户ID
     * @param operation 操作类型
     * @return 是否有权限
     */
    boolean hasAllocationPermission(String allocationId, String userId, String operation);

    /**
     * 验证分配状态转换是否有效
     * @param fromStatus 原状态
     * @param toStatus 目标状态
     * @return 是否有效
     */
    boolean isValidStatusTransition(String fromStatus, String toStatus);

    /**
     * 验证分配数据完整性
     * @param allocationId 分配ID
     * @return 验证结果
     */
    Map<String, Object> validateAllocationData(String allocationId);

    /**
     * 检查分配冲突
     * @param allocation 分配信息
     * @return 冲突检查结果
     */
    Map<String, Object> checkAllocationConflict(BudgetAllocation allocation);

    /**
     * 验证分配金额合理性
     * @param allocationId 分配ID
     * @return 验证结果
     */
    Map<String, Object> validateAllocationAmount(String allocationId);
}
