package com.management.accountant.service.ncv65;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.entity.ncv65.BudgetAdjustment;

import java.util.List;
import java.util.Map;

/**
 * NCV65全面预算系统 - 预算调整服务接口
 * 
 * @description 预算调整业务逻辑接口，支持预算调整的申请、审批和执行
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
public interface IBudgetAdjustmentService extends IService<BudgetAdjustment> {

    // ==================== 基础CRUD操作 ====================

    /**
     * 创建预算调整
     * @param adjustment 调整信息
     * @return 是否创建成功
     */
    boolean createBudgetAdjustment(BudgetAdjustment adjustment);

    /**
     * 更新预算调整
     * @param adjustment 调整信息
     * @return 是否更新成功
     */
    boolean updateBudgetAdjustment(BudgetAdjustment adjustment);

    /**
     * 删除预算调整
     * @param id 调整ID
     * @return 是否删除成功
     */
    boolean deleteBudgetAdjustment(String id);

    /**
     * 批量删除预算调整
     * @param ids 调整ID列表
     * @return 是否删除成功
     */
    boolean batchDeleteBudgetAdjustments(List<String> ids);

    /**
     * 根据ID查询预算调整
     * @param id 调整ID
     * @return 调整信息
     */
    BudgetAdjustment getBudgetAdjustmentById(String id);

    /**
     * 根据编码查询预算调整
     * @param adjustmentCode 调整编码
     * @return 调整信息
     */
    BudgetAdjustment getBudgetAdjustmentByCode(String adjustmentCode);

    // ==================== 查询操作 ====================

    /**
     * 分页查询预算调整
     * @param current 当前页
     * @param size 页大小
     * @param params 查询参数
     * @return 分页结果
     */
    IPage<BudgetAdjustment> getBudgetAdjustmentPage(Integer current, Integer size, Map<String, Object> params);

    /**
     * 根据预算年度查询调整列表
     * @param fiscalYear 预算年度
     * @return 调整列表
     */
    List<BudgetAdjustment> getBudgetAdjustmentsByFiscalYear(Integer fiscalYear);

    /**
     * 根据调整类型查询调整列表
     * @param adjustmentType 调整类型
     * @return 调整列表
     */
    List<BudgetAdjustment> getBudgetAdjustmentsByType(String adjustmentType);

    /**
     * 根据调整分类查询调整列表
     * @param adjustmentCategory 调整分类
     * @return 调整列表
     */
    List<BudgetAdjustment> getBudgetAdjustmentsByCategory(String adjustmentCategory);

    /**
     * 根据审批状态查询调整列表
     * @param approvalStatus 审批状态
     * @return 调整列表
     */
    List<BudgetAdjustment> getBudgetAdjustmentsByApprovalStatus(String approvalStatus);

    /**
     * 根据执行状态查询调整列表
     * @param executionStatus 执行状态
     * @return 调整列表
     */
    List<BudgetAdjustment> getBudgetAdjustmentsByExecutionStatus(String executionStatus);

    /**
     * 查询我申请的调整列表
     * @param userId 用户ID
     * @return 我申请的调整列表
     */
    List<BudgetAdjustment> getMyApplications(String userId);

    /**
     * 查询待我审批的调整列表
     * @param userId 用户ID
     * @return 待我审批的调整列表
     */
    List<BudgetAdjustment> getPendingApprovals(String userId);

    /**
     * 查询我审批过的调整列表
     * @param userId 用户ID
     * @return 我审批过的调整列表
     */
    List<BudgetAdjustment> getMyApprovals(String userId);

    /**
     * 查询待执行的调整列表
     * @param userId 用户ID
     * @return 待执行的调整列表
     */
    List<BudgetAdjustment> getPendingExecutions(String userId);

    /**
     * 查询紧急调整列表
     * @return 紧急调整列表
     */
    List<BudgetAdjustment> getUrgentAdjustments();

    /**
     * 查询批量调整列表
     * @return 批量调整列表
     */
    List<BudgetAdjustment> getBatchAdjustments();

    /**
     * 查询自动执行的调整列表
     * @return 自动执行的调整列表
     */
    List<BudgetAdjustment> getAutoExecuteAdjustments();

    /**
     * 查询执行失败的调整列表
     * @return 执行失败的调整列表
     */
    List<BudgetAdjustment> getFailedAdjustments();

    /**
     * 查询需要回滚的调整列表
     * @return 需要回滚的调整列表
     */
    List<BudgetAdjustment> getRollbackRequiredAdjustments();

    // ==================== 业务操作 ====================

    /**
     * 提交审批
     * @param adjustmentId 调整ID
     * @param workflowId 审批流程ID
     * @return 是否提交成功
     */
    boolean submitForApproval(String adjustmentId, String workflowId);

    /**
     * 审批通过
     * @param adjustmentId 调整ID
     * @param approvalComments 审批意见
     * @return 是否审批成功
     */
    boolean approveAdjustment(String adjustmentId, String approvalComments);

    /**
     * 审批拒绝
     * @param adjustmentId 调整ID
     * @param approvalComments 审批意见
     * @return 是否拒绝成功
     */
    boolean rejectAdjustment(String adjustmentId, String approvalComments);

    /**
     * 开始执行
     * @param adjustmentId 调整ID
     * @return 是否开始执行成功
     */
    boolean startExecution(String adjustmentId);

    /**
     * 完成执行
     * @param adjustmentId 调整ID
     * @param executionResult 执行结果
     * @return 是否完成执行成功
     */
    boolean completeExecution(String adjustmentId, String executionResult);

    /**
     * 执行失败
     * @param adjustmentId 调整ID
     * @param executionError 执行错误信息
     * @return 是否标记失败成功
     */
    boolean failExecution(String adjustmentId, String executionError);

    /**
     * 取消调整
     * @param adjustmentId 调整ID
     * @return 是否取消成功
     */
    boolean cancelAdjustment(String adjustmentId);

    /**
     * 复制调整
     * @param sourceAdjustmentId 源调整ID
     * @param newAdjustmentCode 新调整编码
     * @param newAdjustmentName 新调整名称
     * @return 新调整ID
     */
    String copyAdjustment(String sourceAdjustmentId, String newAdjustmentCode, String newAdjustmentName);

    /**
     * 批量审批
     * @param adjustmentIds 调整ID列表
     * @param approvalComments 审批意见
     * @param isApproved 是否通过
     * @return 审批成功的调整数量
     */
    int batchApproveAdjustments(List<String> adjustmentIds, String approvalComments, boolean isApproved);

    /**
     * 批量执行
     * @param adjustmentIds 调整ID列表
     * @return 执行成功的调整数量
     */
    int batchExecuteAdjustments(List<String> adjustmentIds);

    /**
     * 批量取消
     * @param adjustmentIds 调整ID列表
     * @return 取消成功的调整数量
     */
    int batchCancelAdjustments(List<String> adjustmentIds);

    // ==================== 调整分析 ====================

    /**
     * 分析调整影响
     * @param adjustmentId 调整ID
     * @return 影响分析结果
     */
    Map<String, Object> analyzeAdjustmentImpact(String adjustmentId);

    /**
     * 评估调整风险
     * @param adjustmentId 调整ID
     * @return 风险评估结果
     */
    Map<String, Object> assessAdjustmentRisk(String adjustmentId);

    /**
     * 生成调整建议
     * @param organizationId 组织ID
     * @param indicatorId 指标ID
     * @param fiscalYear 预算年度
     * @return 调整建议
     */
    Map<String, Object> generateAdjustmentSuggestions(String organizationId, String indicatorId, Integer fiscalYear);

    /**
     * 预测调整效果
     * @param adjustmentId 调整ID
     * @return 效果预测结果
     */
    Map<String, Object> predictAdjustmentEffect(String adjustmentId);

    /**
     * 调整合规性检查
     * @param adjustmentId 调整ID
     * @return 合规性检查结果
     */
    Map<String, Object> checkAdjustmentCompliance(String adjustmentId);

    // ==================== 统计分析 ====================

    /**
     * 获取调整统计信息
     * @return 统计信息
     */
    Map<String, Object> getBudgetAdjustmentStatistics();

    /**
     * 按调整类型统计数量
     * @return 统计结果
     */
    List<Map<String, Object>> getBudgetAdjustmentCountByType();

    /**
     * 按调整分类统计数量
     * @return 统计结果
     */
    List<Map<String, Object>> getBudgetAdjustmentCountByCategory();

    /**
     * 按审批状态统计数量
     * @return 统计结果
     */
    List<Map<String, Object>> getBudgetAdjustmentCountByApprovalStatus();

    /**
     * 按执行状态统计数量
     * @return 统计结果
     */
    List<Map<String, Object>> getBudgetAdjustmentCountByExecutionStatus();

    /**
     * 按年度统计调整数量
     * @return 统计结果
     */
    List<Map<String, Object>> getBudgetAdjustmentCountByYear();

    /**
     * 按月份统计调整数量
     * @param fiscalYear 预算年度
     * @return 统计结果
     */
    List<Map<String, Object>> getBudgetAdjustmentCountByMonth(Integer fiscalYear);

    /**
     * 统计调整金额
     * @param fiscalYear 预算年度
     * @return 调整金额统计
     */
    Map<String, Object> getBudgetAdjustmentAmountSummary(Integer fiscalYear);

    /**
     * 获取用户调整统计信息
     * @param userId 用户ID
     * @return 用户调整统计信息
     */
    Map<String, Object> getUserAdjustmentStatistics(String userId);

    /**
     * 获取调整趋势分析
     * @param months 月份数
     * @return 趋势分析数据
     */
    List<Map<String, Object>> getAdjustmentTrend(Integer months);

    /**
     * 获取调整效率分析
     * @param fiscalYear 预算年度
     * @return 效率分析数据
     */
    Map<String, Object> getAdjustmentEfficiencyAnalysis(Integer fiscalYear);

    // ==================== 数据导入导出 ====================

    /**
     * 导出调整数据
     * @param adjustmentIds 调整ID列表
     * @return 导出文件路径
     */
    String exportAdjustments(List<String> adjustmentIds);

    /**
     * 导入调整数据
     * @param filePath 文件路径
     * @return 导入结果
     */
    Map<String, Object> importAdjustments(String filePath);

    /**
     * 导出调整报告
     * @param fiscalYear 预算年度
     * @param organizationId 组织ID
     * @return 导出文件路径
     */
    String exportAdjustmentReport(Integer fiscalYear, String organizationId);

    /**
     * 导出调整分析报告
     * @param adjustmentId 调整ID
     * @return 导出文件路径
     */
    String exportAdjustmentAnalysisReport(String adjustmentId);

    // ==================== 数据清理 ====================

    /**
     * 清理已取消的调整
     * @param days 取消天数
     * @return 清理数量
     */
    int cleanupCancelledAdjustments(Integer days);

    /**
     * 清理已完成的调整
     * @param days 完成天数
     * @return 清理数量
     */
    int cleanupCompletedAdjustments(Integer days);

    /**
     * 归档历史调整
     * @param fiscalYear 预算年度
     * @return 归档数量
     */
    int archiveHistoricalAdjustments(Integer fiscalYear);

    // ==================== 验证方法 ====================

    /**
     * 检查调整编码是否存在
     * @param adjustmentCode 调整编码
     * @param excludeId 排除的调整ID
     * @return 是否存在
     */
    boolean checkAdjustmentCodeExists(String adjustmentCode, String excludeId);

    /**
     * 检查调整名称是否存在
     * @param adjustmentName 调整名称
     * @param excludeId 排除的调整ID
     * @return 是否存在
     */
    boolean checkAdjustmentNameExists(String adjustmentName, String excludeId);

    /**
     * 检查用户是否有调整操作权限
     * @param adjustmentId 调整ID
     * @param userId 用户ID
     * @param operation 操作类型
     * @return 是否有权限
     */
    boolean hasAdjustmentPermission(String adjustmentId, String userId, String operation);

    /**
     * 验证调整状态转换是否有效
     * @param fromStatus 原状态
     * @param toStatus 目标状态
     * @return 是否有效
     */
    boolean isValidStatusTransition(String fromStatus, String toStatus);

    /**
     * 验证调整数据完整性
     * @param adjustmentId 调整ID
     * @return 验证结果
     */
    Map<String, Object> validateAdjustmentData(String adjustmentId);

    /**
     * 检查调整冲突
     * @param adjustment 调整信息
     * @return 冲突检查结果
     */
    Map<String, Object> checkAdjustmentConflict(BudgetAdjustment adjustment);
}
