package com.global.treasurer.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.global.treasurer.entity.FundConcentrationRule;
import com.global.treasurer.util.PageResult;
import com.global.treasurer.util.PageableParam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 资金归集规则服务接口
 *
 * @author Global Treasurer System
 * @since 2025-09-22
 */
public interface FundConcentrationRuleService extends IService<FundConcentrationRule> {

    // ==================== 新增的基础方法 ====================

    /**
     * 分页查询资金归集规则
     *
     * @param pageableParam 分页参数
     * @param queryParam 查询参数
     * @return 分页结果
     */
    PageResult<FundConcentrationRule> getRulesPage(PageableParam pageableParam, FundConcentrationRule queryParam);

    /**
     * 创建资金归集规则
     *
     * @param rule 规则信息
     * @return 创建的规则
     */
    FundConcentrationRule createRule(FundConcentrationRule rule);

    /**
     * 更新资金归集规则
     *
     * @param rule 规则信息
     * @return 更新的规则
     */
    FundConcentrationRule updateRule(FundConcentrationRule rule);

    /**
     * 根据ID查询资金归集规则
     *
     * @param id 规则ID
     * @return 规则信息
     */
    FundConcentrationRule getRuleById(Long id);

    /**
     * 删除资金归集规则
     *
     * @param id 规则ID
     */
    void deleteRule(Long id);

    /**
     * 启用资金归集规则
     *
     * @param id 规则ID
     */
    void enableRule(Long id);

    /**
     * 禁用资金归集规则
     *
     * @param id 规则ID
     */
    void disableRule(Long id);

    /**
     * 获取资金归集规则列表
     *
     * @param queryParam 查询参数
     * @return 规则列表
     */
    List<FundConcentrationRule> getRulesList(FundConcentrationRule queryParam);

    // ==================== 基础业务方法 ====================

    /**
     * 根据规则编码查询
     */
    FundConcentrationRule getByRuleCode(String ruleCode, Long orgId);

    /**
     * 根据资金池ID查询规则列表
     */
    List<FundConcentrationRule> getByPoolId(Long poolId);

    /**
     * 根据规则类型查询
     */
    List<FundConcentrationRule> getByRuleType(String ruleType, Long orgId);

    /**
     * 根据触发类型查询
     */
    List<FundConcentrationRule> getByTriggerType(String triggerType, Long orgId);

    /**
     * 查询启用的规则
     */
    List<FundConcentrationRule> getEnabledRules(Long orgId);

    /**
     * 查询有效的规则（在有效期内且启用）
     */
    List<FundConcentrationRule> getEffectiveRules(Long orgId);

    /**
     * 查询定时规则
     */
    List<FundConcentrationRule> getScheduledRules(Long orgId);

    /**
     * 查询阈值规则
     */
    List<FundConcentrationRule> getThresholdRules(Long orgId);

    /**
     * 创建归集规则
     */
    boolean createConcentrationRule(FundConcentrationRule rule);

    /**
     * 更新归集规则
     */
    boolean updateConcentrationRule(FundConcentrationRule rule);

    /**
     * 删除归集规则
     */
    boolean deleteConcentrationRule(Long ruleId);

    // ==================== 分页查询方法 ====================

    /**
     * 分页查询归集规则
     */
    IPage<FundConcentrationRule> getRulePage(Page<FundConcentrationRule> page, String ruleCode, String ruleName,
                                            Long poolId, String ruleType, String triggerType, Integer isEnabled, Long orgId);

    /**
     * 分页查询有效规则
     */
    IPage<FundConcentrationRule> getEffectiveRulePage(Page<FundConcentrationRule> page, Long orgId);

    /**
     * 分页查询规则执行历史
     */
    IPage<Map<String, Object>> getRuleExecutionHistoryPage(Page<Map<String, Object>> page, Long ruleId,
                                                           LocalDate startDate, LocalDate endDate);

    // ==================== 规则管理方法 ====================

    /**
     * 批量启用规则
     */
    boolean batchEnableRules(List<Long> ruleIds);

    /**
     * 批量禁用规则
     */
    boolean batchDisableRules(List<Long> ruleIds);

    /**
     * 更新规则执行顺序
     */
    boolean updateExecutionOrder(Long ruleId, Integer executionOrder);

    /**
     * 批量更新规则执行顺序
     */
    boolean batchUpdateExecutionOrder(List<Map<String, Object>> ruleOrders);

    /**
     * 复制规则
     */
    FundConcentrationRule copyRule(Long sourceRuleId, String newRuleCode, String newRuleName);

    // ==================== 规则执行方法 ====================

    /**
     * 查询需要执行的定时规则
     */
    List<FundConcentrationRule> getRulesForScheduledExecution(String currentTime, Long orgId);

    /**
     * 查询需要执行的阈值规则
     */
    List<FundConcentrationRule> getRulesForThresholdExecution(Long poolId, BigDecimal currentBalance);

    /**
     * 查询可手动执行的规则
     */
    List<FundConcentrationRule> getRulesForManualExecution(Long poolId);

    /**
     * 更新规则最后执行时间
     */
    boolean updateLastExecutionTime(Long ruleId, LocalDate lastExecutionTime);

    /**
     * 记录规则执行结果
     */
    boolean recordRuleExecutionResult(Long ruleId, String executionResult, LocalDate executionTime);

    /**
     * 执行归集规则
     */
    Map<String, Object> executeConcentrationRule(Long ruleId, Map<String, Object> executionParams);

    /**
     * 批量执行归集规则
     */
    Map<String, Object> batchExecuteConcentrationRules(List<Long> ruleIds, Map<String, Object> executionParams);

    // ==================== 统计分析方法 ====================

    /**
     * 统计归集规则数量
     */
    Map<String, Object> getRuleStatistics(Long orgId);

    /**
     * 按规则类型统计数量
     */
    List<Map<String, Object>> getRuleCountByType(Long orgId);

    /**
     * 按触发类型统计数量
     */
    List<Map<String, Object>> getRuleCountByTriggerType(Long orgId);

    /**
     * 按状态统计规则数量
     */
    List<Map<String, Object>> getRuleCountByStatus(Long orgId);

    /**
     * 统计规则执行次数
     */
    Map<String, Object> getRuleExecutionStatistics(Long ruleId, LocalDate startDate, LocalDate endDate);

    // ==================== 监控预警方法 ====================

    /**
     * 查询即将过期的规则
     */
    List<FundConcentrationRule> getRulesNearExpiry(Integer days, Long orgId);

    /**
     * 查询已过期的规则
     */
    List<FundConcentrationRule> getExpiredRules(Long orgId);

    /**
     * 查询长期未执行的规则
     */
    List<FundConcentrationRule> getLongUnexecutedRules(Integer days, Long orgId);

    /**
     * 查询执行异常的规则
     */
    List<FundConcentrationRule> getRulesWithExecutionErrors(Long orgId);

    /**
     * 查询配置异常的规则
     */
    List<FundConcentrationRule> getRulesWithConfigurationErrors(Long orgId);

    /**
     * 生成规则预警报告
     */
    Map<String, Object> generateRuleAlertReport(Long orgId);

    // ==================== 分析报表方法 ====================

    /**
     * 查询规则执行效率分析
     */
    List<Map<String, Object>> getRuleExecutionEfficiencyAnalysis(Long orgId, LocalDate startDate, LocalDate endDate);

    /**
     * 查询规则成功率分析
     */
    List<Map<String, Object>> getRuleSuccessRateAnalysis(Long orgId, LocalDate startDate, LocalDate endDate);

    /**
     * 查询规则影响分析
     */
    List<Map<String, Object>> getRuleImpactAnalysis(Long ruleId, LocalDate startDate, LocalDate endDate);

    /**
     * 查询规则使用频率分析
     */
    List<Map<String, Object>> getRuleUsageFrequencyAnalysis(Long orgId, LocalDate startDate, LocalDate endDate);

    /**
     * 查询规则优化建议
     */
    List<Map<String, Object>> getRuleOptimizationSuggestions(Long orgId);

    /**
     * 生成规则综合分析报告
     */
    Map<String, Object> generateRuleComprehensiveReport(Long orgId, LocalDate startDate, LocalDate endDate);

    // ==================== 业务操作方法 ====================

    /**
     * 查询规则关联的资金池信息
     */
    Map<String, Object> getRulePoolInfo(Long ruleId);

    /**
     * 查询规则执行历史
     */
    List<Map<String, Object>> getRuleExecutionHistory(Long ruleId, LocalDate startDate, LocalDate endDate);

    /**
     * 查询规则冲突检测
     */
    List<Map<String, Object>> getRuleConflictDetection(Long poolId);

    /**
     * 查询规则依赖关系
     */
    List<Map<String, Object>> getRuleDependencies(Long ruleId);

    /**
     * 规则模拟执行
     */
    Map<String, Object> simulateRuleExecution(Long ruleId, Map<String, Object> simulationParams);

    // ==================== 配置管理方法 ====================

    /**
     * 查询规则配置模板
     */
    List<Map<String, Object>> getRuleConfigurationTemplates(String ruleType);

    /**
     * 复制规则配置
     */
    boolean copyRuleConfiguration(Long sourceRuleId, Long targetRuleId);

    /**
     * 验证规则配置
     */
    Map<String, Object> validateRuleConfiguration(Long ruleId);

    /**
     * 更新规则配置
     */
    boolean updateRuleConfiguration(Long ruleId, Map<String, Object> configuration);

    /**
     * 重置规则配置
     */
    boolean resetRuleConfiguration(Long ruleId);

    // ==================== 数据导出方法 ====================

    /**
     * 导出规则数据
     */
    List<Map<String, Object>> exportRuleData(List<Long> ruleIds, Long orgId);

    /**
     * 导出规则执行报表
     */
    List<Map<String, Object>> exportRuleExecutionReport(Long orgId, LocalDate startDate, LocalDate endDate);

    /**
     * 导出规则统计报表
     */
    List<Map<String, Object>> exportRuleStatisticsReport(Long orgId, LocalDate startDate, LocalDate endDate);

    /**
     * 生成规则Excel报表
     */
    byte[] generateRuleExcelReport(Long orgId, LocalDate startDate, LocalDate endDate);

    /**
     * 生成规则PDF报表
     */
    byte[] generateRulePdfReport(Long orgId, LocalDate startDate, LocalDate endDate);

    // ==================== 系统维护方法 ====================

    /**
     * 清理过期规则
     */
    int cleanupExpiredRules(LocalDate beforeDate, Long orgId);

    /**
     * 重置规则执行状态
     */
    boolean resetRuleExecutionStatus(Long ruleId);

    /**
     * 批量重置规则执行状态
     */
    boolean batchResetRuleExecutionStatus(List<Long> ruleIds);

    /**
     * 验证规则数据一致性
     */
    List<Map<String, Object>> validateRuleDataConsistency(Long orgId);

    /**
     * 修复规则数据不一致问题
     */
    boolean fixRuleDataInconsistency(Long ruleId);

    /**
     * 规则数据备份
     */
    boolean backupRuleData(Long orgId, String backupPath);

    /**
     * 规则数据恢复
     */
    boolean restoreRuleData(Long orgId, String backupPath);

    // ==================== 审计日志方法 ====================

    /**
     * 记录规则操作日志
     */
    boolean recordRuleOperationLog(Long ruleId, String operation, String operationData, Long operatorId);

    /**
     * 查询规则操作日志
     */
    List<Map<String, Object>> getRuleOperationLogs(Long ruleId, LocalDate startDate, LocalDate endDate);

    /**
     * 查询规则变更历史
     */
    List<Map<String, Object>> getRuleChangeHistory(Long ruleId);

    /**
     * 生成规则审计报告
     */
    Map<String, Object> generateRuleAuditReport(Long ruleId, LocalDate startDate, LocalDate endDate);

    // ==================== 高级功能方法 ====================

    /**
     * 规则智能优化建议
     */
    List<Map<String, Object>> getRuleIntelligentOptimizationSuggestions(Long orgId);

    /**
     * 规则风险评估
     */
    Map<String, Object> assessRuleRisk(Long ruleId);

    /**
     * 规则性能评估
     */
    Map<String, Object> assessRulePerformance(Long ruleId, LocalDate startDate, LocalDate endDate);

    /**
     * 规则预测分析
     */
    Map<String, Object> predictRuleTrend(Long ruleId, Integer days);

    /**
     * 规则自动调优
     */
    Map<String, Object> autoOptimizeRule(Long ruleId, Map<String, Object> optimizationParams);

    /**
     * 规则智能推荐
     */
    List<Map<String, Object>> intelligentRuleRecommendation(Long poolId, Map<String, Object> recommendationCriteria);
}
