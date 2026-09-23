package com.management.accountant.service.ncv65;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.entity.ncv65.BudgetExecution;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * NCV65全面预算系统 - 预算执行监控服务接口
 * 
 * @description 预算执行监控业务逻辑接口，支持预算执行情况的实时监控和分析
 * @author AI Assistant
 * @date 2025-01-09
 * @version 1.0.0
 */
public interface IBudgetExecutionService extends IService<BudgetExecution> {

    // ==================== 基础CRUD操作 ====================

    /**
     * 创建预算执行监控
     * @param execution 执行监控信息
     * @return 是否创建成功
     */
    boolean createBudgetExecution(BudgetExecution execution);

    /**
     * 更新预算执行监控
     * @param execution 执行监控信息
     * @return 是否更新成功
     */
    boolean updateBudgetExecution(BudgetExecution execution);

    /**
     * 删除预算执行监控
     * @param id 执行监控ID
     * @return 是否删除成功
     */
    boolean deleteBudgetExecution(String id);

    /**
     * 批量删除预算执行监控
     * @param ids 执行监控ID列表
     * @return 是否删除成功
     */
    boolean batchDeleteBudgetExecutions(List<String> ids);

    /**
     * 根据ID查询预算执行监控
     * @param id 执行监控ID
     * @return 执行监控信息
     */
    BudgetExecution getBudgetExecutionById(String id);

    /**
     * 根据编码查询预算执行监控
     * @param executionCode 执行编码
     * @return 执行监控信息
     */
    BudgetExecution getBudgetExecutionByCode(String executionCode);

    // ==================== 查询操作 ====================

    /**
     * 分页查询预算执行监控
     * @param current 当前页
     * @param size 页大小
     * @param params 查询参数
     * @return 分页结果
     */
    IPage<BudgetExecution> getBudgetExecutionPage(Integer current, Integer size, Map<String, Object> params);

    /**
     * 根据预算年度查询执行列表
     * @param fiscalYear 预算年度
     * @return 执行列表
     */
    List<BudgetExecution> getBudgetExecutionsByFiscalYear(Integer fiscalYear);

    /**
     * 根据组织ID查询执行列表
     * @param organizationId 组织ID
     * @return 执行列表
     */
    List<BudgetExecution> getBudgetExecutionsByOrganizationId(String organizationId);

    /**
     * 根据指标ID查询执行列表
     * @param indicatorId 指标ID
     * @return 执行列表
     */
    List<BudgetExecution> getBudgetExecutionsByIndicatorId(String indicatorId);

    /**
     * 根据执行状态查询执行列表
     * @param executionStatus 执行状态
     * @return 执行列表
     */
    List<BudgetExecution> getBudgetExecutionsByExecutionStatus(String executionStatus);

    /**
     * 根据控制状态查询执行列表
     * @param controlStatus 控制状态
     * @return 执行列表
     */
    List<BudgetExecution> getBudgetExecutionsByControlStatus(String controlStatus);

    /**
     * 根据预警级别查询执行列表
     * @param warningLevel 预警级别
     * @return 执行列表
     */
    List<BudgetExecution> getBudgetExecutionsByWarningLevel(String warningLevel);

    /**
     * 根据执行期间查询执行列表
     * @param executionPeriod 执行期间
     * @return 执行列表
     */
    List<BudgetExecution> getBudgetExecutionsByExecutionPeriod(String executionPeriod);

    /**
     * 查询我负责的执行监控列表
     * @param userId 用户ID
     * @return 我负责的执行监控列表
     */
    List<BudgetExecution> getMyExecutions(String userId);

    /**
     * 查询我监控的执行列表
     * @param userId 用户ID
     * @return 我监控的执行列表
     */
    List<BudgetExecution> getMyMonitorings(String userId);

    /**
     * 查询预警执行列表
     * @return 预警执行列表
     */
    List<BudgetExecution> getWarningExecutions();

    /**
     * 查询超支执行列表
     * @return 超支执行列表
     */
    List<BudgetExecution> getOverBudgetExecutions();

    /**
     * 查询执行不足列表
     * @return 执行不足列表
     */
    List<BudgetExecution> getUnderExecutions();

    /**
     * 查询失控执行列表
     * @return 失控执行列表
     */
    List<BudgetExecution> getUncontrolledExecutions();

    /**
     * 查询自动监控的执行列表
     * @return 自动监控的执行列表
     */
    List<BudgetExecution> getAutoMonitoringExecutions();

    /**
     * 查询需要更新的执行列表
     * @return 需要更新的执行列表
     */
    List<BudgetExecution> getExecutionsNeedUpdate();

    // ==================== 执行监控操作 ====================

    /**
     * 更新执行数据
     * @param executionId 执行ID
     * @param actualAmount 实际金额
     * @param usedAmount 已使用金额
     * @return 是否更新成功
     */
    boolean updateExecutionData(String executionId, BigDecimal actualAmount, BigDecimal usedAmount);

    /**
     * 更新执行状态
     * @param executionId 执行ID
     * @param executionStatus 执行状态
     * @param controlStatus 控制状态
     * @param warningLevel 预警级别
     * @param warningReason 预警原因
     * @return 是否更新成功
     */
    boolean updateExecutionStatus(String executionId, String executionStatus, String controlStatus, 
                                 String warningLevel, String warningReason);

    /**
     * 启用自动监控
     * @param executionId 执行ID
     * @return 是否启用成功
     */
    boolean enableAutoMonitoring(String executionId);

    /**
     * 停用自动监控
     * @param executionId 执行ID
     * @return 是否停用成功
     */
    boolean disableAutoMonitoring(String executionId);

    /**
     * 手动刷新执行数据
     * @param executionId 执行ID
     * @return 是否刷新成功
     */
    boolean refreshExecutionData(String executionId);

    /**
     * 批量更新执行状态
     * @param executionIds 执行ID列表
     * @param status 状态
     * @return 更新成功的执行数量
     */
    int batchUpdateExecutionStatus(List<String> executionIds, String status);

    /**
     * 批量启用/停用自动监控
     * @param executionIds 执行ID列表
     * @param isAutoMonitoring 是否自动监控
     * @return 更新成功的执行数量
     */
    int batchUpdateAutoMonitoring(List<String> executionIds, boolean isAutoMonitoring);

    // ==================== 执行分析 ====================

    /**
     * 分析执行趋势
     * @param organizationId 组织ID
     * @param indicatorId 指标ID
     * @param startPeriod 开始期间
     * @param endPeriod 结束期间
     * @return 执行趋势分析结果
     */
    Map<String, Object> analyzeExecutionTrend(String organizationId, String indicatorId, String startPeriod, String endPeriod);

    /**
     * 执行对比分析
     * @param organizationIds 组织ID列表
     * @param indicatorId 指标ID
     * @param executionPeriod 执行期间
     * @return 执行对比分析结果
     */
    Map<String, Object> compareExecutions(List<String> organizationIds, String indicatorId, String executionPeriod);

    /**
     * 执行偏差分析
     * @param executionId 执行ID
     * @return 偏差分析结果
     */
    Map<String, Object> analyzeExecutionDeviation(String executionId);

    /**
     * 执行效率分析
     * @param organizationId 组织ID
     * @param fiscalYear 预算年度
     * @return 效率分析结果
     */
    Map<String, Object> analyzeExecutionEfficiency(String organizationId, Integer fiscalYear);

    /**
     * 执行风险评估
     * @param executionId 执行ID
     * @return 风险评估结果
     */
    Map<String, Object> assessExecutionRisk(String executionId);

    /**
     * 生成执行改进建议
     * @param executionId 执行ID
     * @return 改进建议
     */
    Map<String, Object> generateImprovementSuggestions(String executionId);

    // ==================== 预警管理 ====================

    /**
     * 设置预警规则
     * @param executionId 执行ID
     * @param warningRules 预警规则
     * @return 是否设置成功
     */
    boolean setWarningRules(String executionId, String warningRules);

    /**
     * 触发预警
     * @param executionId 执行ID
     * @param warningLevel 预警级别
     * @param warningReason 预警原因
     * @return 是否触发成功
     */
    boolean triggerWarning(String executionId, String warningLevel, String warningReason);

    /**
     * 解除预警
     * @param executionId 执行ID
     * @return 是否解除成功
     */
    boolean clearWarning(String executionId);

    /**
     * 发送预警通知
     * @param executionId 执行ID
     * @param recipients 接收人列表
     * @return 是否发送成功
     */
    boolean sendWarningNotification(String executionId, List<String> recipients);

    /**
     * 获取预警统计
     * @param fiscalYear 预算年度
     * @return 预警统计信息
     */
    Map<String, Object> getWarningStatistics(Integer fiscalYear);

    // ==================== 控制管理 ====================

    /**
     * 设置控制策略
     * @param executionId 执行ID
     * @param controlStrategy 控制策略
     * @return 是否设置成功
     */
    boolean setControlStrategy(String executionId, String controlStrategy);

    /**
     * 执行控制动作
     * @param executionId 执行ID
     * @param controlAction 控制动作
     * @return 是否执行成功
     */
    boolean executeControlAction(String executionId, String controlAction);

    /**
     * 冻结预算
     * @param executionId 执行ID
     * @param freezeReason 冻结原因
     * @return 是否冻结成功
     */
    boolean freezeBudget(String executionId, String freezeReason);

    /**
     * 解冻预算
     * @param executionId 执行ID
     * @return 是否解冻成功
     */
    boolean unfreezeBudget(String executionId);

    /**
     * 限制支出
     * @param executionId 执行ID
     * @param limitAmount 限制金额
     * @param limitReason 限制原因
     * @return 是否限制成功
     */
    boolean limitExpenditure(String executionId, BigDecimal limitAmount, String limitReason);

    /**
     * 取消限制
     * @param executionId 执行ID
     * @return 是否取消成功
     */
    boolean removeLimitation(String executionId);

    // ==================== 统计分析 ====================

    /**
     * 获取执行统计信息
     * @return 统计信息
     */
    Map<String, Object> getBudgetExecutionStatistics();

    /**
     * 按执行状态统计数量
     * @return 统计结果
     */
    List<Map<String, Object>> getBudgetExecutionCountByStatus();

    /**
     * 按控制状态统计数量
     * @return 统计结果
     */
    List<Map<String, Object>> getBudgetExecutionCountByControlStatus();

    /**
     * 按预警级别统计数量
     * @return 统计结果
     */
    List<Map<String, Object>> getBudgetExecutionCountByWarningLevel();

    /**
     * 按年度统计执行数量
     * @return 统计结果
     */
    List<Map<String, Object>> getBudgetExecutionCountByYear();

    /**
     * 按月份统计执行数量
     * @param fiscalYear 预算年度
     * @return 统计结果
     */
    List<Map<String, Object>> getBudgetExecutionCountByMonth(Integer fiscalYear);

    /**
     * 统计执行金额
     * @param fiscalYear 预算年度
     * @return 执行金额统计
     */
    Map<String, Object> getBudgetExecutionAmountSummary(Integer fiscalYear);

    /**
     * 按组织统计执行金额
     * @param fiscalYear 预算年度
     * @return 各组织执行金额统计
     */
    List<Map<String, Object>> getBudgetExecutionAmountsByOrganization(Integer fiscalYear);

    /**
     * 按指标统计执行金额
     * @param fiscalYear 预算年度
     * @return 各指标执行金额统计
     */
    List<Map<String, Object>> getBudgetExecutionAmountsByIndicator(Integer fiscalYear);

    /**
     * 统计执行率分布
     * @param fiscalYear 预算年度
     * @return 执行率分布统计
     */
    List<Map<String, Object>> getExecutionRateDistribution(Integer fiscalYear);

    /**
     * 统计差异率分布
     * @param fiscalYear 预算年度
     * @return 差异率分布统计
     */
    List<Map<String, Object>> getVarianceRateDistribution(Integer fiscalYear);

    /**
     * 获取用户执行统计信息
     * @param userId 用户ID
     * @return 用户执行统计信息
     */
    Map<String, Object> getUserExecutionStatistics(String userId);

    /**
     * 获取执行趋势分析
     * @param months 月份数
     * @return 趋势分析数据
     */
    List<Map<String, Object>> getExecutionTrend(Integer months);

    /**
     * 获取执行绩效分析
     * @param fiscalYear 预算年度
     * @return 绩效分析数据
     */
    Map<String, Object> getExecutionPerformanceAnalysis(Integer fiscalYear);

    // ==================== 数据导入导出 ====================

    /**
     * 导出执行数据
     * @param executionIds 执行ID列表
     * @return 导出文件路径
     */
    String exportExecutions(List<String> executionIds);

    /**
     * 导入执行数据
     * @param filePath 文件路径
     * @return 导入结果
     */
    Map<String, Object> importExecutions(String filePath);

    /**
     * 导出执行报告
     * @param fiscalYear 预算年度
     * @param organizationId 组织ID
     * @return 导出文件路径
     */
    String exportExecutionReport(Integer fiscalYear, String organizationId);

    /**
     * 导出执行分析报告
     * @param executionId 执行ID
     * @return 导出文件路径
     */
    String exportExecutionAnalysisReport(String executionId);

    /**
     * 导出预警报告
     * @param fiscalYear 预算年度
     * @return 导出文件路径
     */
    String exportWarningReport(Integer fiscalYear);

    // ==================== 数据清理 ====================

    /**
     * 清理历史执行数据
     * @param days 保留天数
     * @return 清理数量
     */
    int cleanupHistoricalExecutions(Integer days);

    /**
     * 清理停用的执行监控
     * @param days 停用天数
     * @return 清理数量
     */
    int cleanupInactiveExecutions(Integer days);

    /**
     * 归档历史执行数据
     * @param fiscalYear 预算年度
     * @return 归档数量
     */
    int archiveHistoricalExecutions(Integer fiscalYear);

    // ==================== 验证方法 ====================

    /**
     * 检查执行编码是否存在
     * @param executionCode 执行编码
     * @param excludeId 排除的执行ID
     * @return 是否存在
     */
    boolean checkExecutionCodeExists(String executionCode, String excludeId);

    /**
     * 检查执行监控是否存在
     * @param organizationId 组织ID
     * @param indicatorId 指标ID
     * @param executionPeriod 执行期间
     * @param excludeId 排除的执行ID
     * @return 是否存在
     */
    boolean checkExecutionExists(String organizationId, String indicatorId, String executionPeriod, String excludeId);

    /**
     * 检查用户是否有执行操作权限
     * @param executionId 执行ID
     * @param userId 用户ID
     * @param operation 操作类型
     * @return 是否有权限
     */
    boolean hasExecutionPermission(String executionId, String userId, String operation);

    /**
     * 验证执行数据完整性
     * @param executionId 执行ID
     * @return 验证结果
     */
    Map<String, Object> validateExecutionData(String executionId);

    /**
     * 验证执行状态转换是否有效
     * @param fromStatus 原状态
     * @param toStatus 目标状态
     * @return 是否有效
     */
    boolean isValidStatusTransition(String fromStatus, String toStatus);

    /**
     * 检查执行异常
     * @param executionId 执行ID
     * @return 异常检查结果
     */
    Map<String, Object> checkExecutionAnomalies(String executionId);
}
