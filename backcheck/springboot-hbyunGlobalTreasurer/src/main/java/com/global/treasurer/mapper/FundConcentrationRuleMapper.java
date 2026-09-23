package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.global.treasurer.entity.FundConcentrationRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 资金归集规则Mapper接口
 * 
 * @author Global Treasurer System
 * @since 2025-09-22
 */
@Mapper
public interface FundConcentrationRuleMapper extends BaseMapper<FundConcentrationRule> {

    // ==================== 基础查询方法 ====================

    /**
     * 根据规则编码查询
     */
    FundConcentrationRule selectByRuleCode(@Param("ruleCode") String ruleCode, @Param("orgId") Long orgId);

    /**
     * 根据资金池ID查询规则列表
     */
    List<FundConcentrationRule> selectByPoolId(@Param("poolId") Long poolId);

    /**
     * 根据规则类型查询
     */
    List<FundConcentrationRule> selectByRuleType(@Param("ruleType") String ruleType, @Param("orgId") Long orgId);

    /**
     * 根据触发类型查询
     */
    List<FundConcentrationRule> selectByTriggerType(@Param("triggerType") String triggerType, @Param("orgId") Long orgId);

    /**
     * 查询启用的规则
     */
    List<FundConcentrationRule> selectEnabledRules(@Param("orgId") Long orgId);

    /**
     * 查询有效的规则（在有效期内且启用）
     */
    List<FundConcentrationRule> selectEffectiveRules(@Param("orgId") Long orgId);

    /**
     * 查询定时规则
     */
    List<FundConcentrationRule> selectScheduledRules(@Param("orgId") Long orgId);

    /**
     * 查询阈值规则
     */
    List<FundConcentrationRule> selectThresholdRules(@Param("orgId") Long orgId);

    // ==================== 分页查询方法 ====================

    /**
     * 分页查询归集规则
     */
    IPage<FundConcentrationRule> selectRulePage(Page<FundConcentrationRule> page, @Param("ruleCode") String ruleCode,
                                               @Param("ruleName") String ruleName, @Param("poolId") Long poolId,
                                               @Param("ruleType") String ruleType, @Param("triggerType") String triggerType,
                                               @Param("isEnabled") Integer isEnabled, @Param("orgId") Long orgId);

    /**
     * 分页查询有效规则
     */
    IPage<FundConcentrationRule> selectEffectiveRulePage(Page<FundConcentrationRule> page, @Param("orgId") Long orgId);

    /**
     * 分页查询规则执行历史
     */
    IPage<Map<String, Object>> selectRuleExecutionHistoryPage(Page<Map<String, Object>> page,
                                                              @Param("ruleId") Long ruleId,
                                                              @Param("startDate") LocalDate startDate,
                                                              @Param("endDate") LocalDate endDate);

    // ==================== 统计查询方法 ====================

    /**
     * 统计归集规则数量
     */
    Map<String, Object> selectRuleStatistics(@Param("orgId") Long orgId);

    /**
     * 按规则类型统计数量
     */
    List<Map<String, Object>> selectRuleCountByType(@Param("orgId") Long orgId);

    /**
     * 按触发类型统计数量
     */
    List<Map<String, Object>> selectRuleCountByTriggerType(@Param("orgId") Long orgId);

    /**
     * 按状态统计规则数量
     */
    List<Map<String, Object>> selectRuleCountByStatus(@Param("orgId") Long orgId);

    /**
     * 统计规则执行次数
     */
    Map<String, Object> selectRuleExecutionStatistics(@Param("ruleId") Long ruleId,
                                                      @Param("startDate") LocalDate startDate,
                                                      @Param("endDate") LocalDate endDate);

    // ==================== 规则管理方法 ====================

    /**
     * 启用规则
     */
    int enableRule(@Param("ruleId") Long ruleId);

    /**
     * 禁用规则
     */
    int disableRule(@Param("ruleId") Long ruleId);

    /**
     * 批量启用规则
     */
    int batchEnableRules(@Param("ruleIds") List<Long> ruleIds);

    /**
     * 批量禁用规则
     */
    int batchDisableRules(@Param("ruleIds") List<Long> ruleIds);

    /**
     * 更新规则执行顺序
     */
    int updateExecutionOrder(@Param("ruleId") Long ruleId, @Param("executionOrder") Integer executionOrder);

    /**
     * 批量更新规则执行顺序
     */
    int batchUpdateExecutionOrder(@Param("ruleOrders") List<Map<String, Object>> ruleOrders);

    // ==================== 规则执行方法 ====================

    /**
     * 查询需要执行的定时规则
     */
    List<FundConcentrationRule> selectRulesForScheduledExecution(@Param("currentTime") String currentTime, @Param("orgId") Long orgId);

    /**
     * 查询需要执行的阈值规则
     */
    List<FundConcentrationRule> selectRulesForThresholdExecution(@Param("poolId") Long poolId, @Param("currentBalance") BigDecimal currentBalance);

    /**
     * 查询可手动执行的规则
     */
    List<FundConcentrationRule> selectRulesForManualExecution(@Param("poolId") Long poolId);

    /**
     * 更新规则最后执行时间
     */
    int updateLastExecutionTime(@Param("ruleId") Long ruleId, @Param("lastExecutionTime") LocalDate lastExecutionTime);

    /**
     * 记录规则执行结果
     */
    int recordRuleExecutionResult(@Param("ruleId") Long ruleId, @Param("executionResult") String executionResult,
                                 @Param("executionTime") LocalDate executionTime);

    // ==================== 监控预警方法 ====================

    /**
     * 查询即将过期的规则
     */
    List<FundConcentrationRule> selectRulesNearExpiry(@Param("days") Integer days, @Param("orgId") Long orgId);

    /**
     * 查询已过期的规则
     */
    List<FundConcentrationRule> selectExpiredRules(@Param("orgId") Long orgId);

    /**
     * 查询长期未执行的规则
     */
    List<FundConcentrationRule> selectLongUnexecutedRules(@Param("days") Integer days, @Param("orgId") Long orgId);

    /**
     * 查询执行异常的规则
     */
    List<FundConcentrationRule> selectRulesWithExecutionErrors(@Param("orgId") Long orgId);

    /**
     * 查询配置异常的规则
     */
    List<FundConcentrationRule> selectRulesWithConfigurationErrors(@Param("orgId") Long orgId);

    // ==================== 分析报表方法 ====================

    /**
     * 查询规则执行效率分析
     */
    List<Map<String, Object>> selectRuleExecutionEfficiencyAnalysis(@Param("orgId") Long orgId,
                                                                    @Param("startDate") LocalDate startDate,
                                                                    @Param("endDate") LocalDate endDate);

    /**
     * 查询规则成功率分析
     */
    List<Map<String, Object>> selectRuleSuccessRateAnalysis(@Param("orgId") Long orgId,
                                                            @Param("startDate") LocalDate startDate,
                                                            @Param("endDate") LocalDate endDate);

    /**
     * 查询规则影响分析
     */
    List<Map<String, Object>> selectRuleImpactAnalysis(@Param("ruleId") Long ruleId,
                                                       @Param("startDate") LocalDate startDate,
                                                       @Param("endDate") LocalDate endDate);

    /**
     * 查询规则使用频率分析
     */
    List<Map<String, Object>> selectRuleUsageFrequencyAnalysis(@Param("orgId") Long orgId,
                                                               @Param("startDate") LocalDate startDate,
                                                               @Param("endDate") LocalDate endDate);

    /**
     * 查询规则优化建议
     */
    List<Map<String, Object>> selectRuleOptimizationSuggestions(@Param("orgId") Long orgId);

    // ==================== 业务操作方法 ====================

    /**
     * 查询规则关联的资金池信息
     */
    Map<String, Object> selectRulePoolInfo(@Param("ruleId") Long ruleId);

    /**
     * 查询规则执行历史
     */
    List<Map<String, Object>> selectRuleExecutionHistory(@Param("ruleId") Long ruleId,
                                                         @Param("startDate") LocalDate startDate,
                                                         @Param("endDate") LocalDate endDate);

    /**
     * 查询规则冲突检测
     */
    List<Map<String, Object>> selectRuleConflictDetection(@Param("poolId") Long poolId);

    /**
     * 查询规则依赖关系
     */
    List<Map<String, Object>> selectRuleDependencies(@Param("ruleId") Long ruleId);

    // ==================== 配置管理方法 ====================

    /**
     * 查询规则配置模板
     */
    List<Map<String, Object>> selectRuleConfigurationTemplates(@Param("ruleType") String ruleType);

    /**
     * 复制规则配置
     */
    int copyRuleConfiguration(@Param("sourceRuleId") Long sourceRuleId, @Param("targetRuleId") Long targetRuleId);

    /**
     * 验证规则配置
     */
    Map<String, Object> validateRuleConfiguration(@Param("ruleId") Long ruleId);

    /**
     * 更新规则配置
     */
    int updateRuleConfiguration(@Param("ruleId") Long ruleId, @Param("configuration") Map<String, Object> configuration);

    // ==================== 数据导出方法 ====================

    /**
     * 查询规则导出数据
     */
    List<Map<String, Object>> selectRuleExportData(@Param("ruleIds") List<Long> ruleIds, @Param("orgId") Long orgId);

    /**
     * 查询规则执行报表数据
     */
    List<Map<String, Object>> selectRuleExecutionReportData(@Param("orgId") Long orgId,
                                                            @Param("startDate") LocalDate startDate,
                                                            @Param("endDate") LocalDate endDate);

    /**
     * 查询规则统计报表数据
     */
    List<Map<String, Object>> selectRuleStatisticsReportData(@Param("orgId") Long orgId,
                                                             @Param("startDate") LocalDate startDate,
                                                             @Param("endDate") LocalDate endDate);

    // ==================== 系统维护方法 ====================

    /**
     * 清理过期规则
     */
    int cleanupExpiredRules(@Param("beforeDate") LocalDate beforeDate, @Param("orgId") Long orgId);

    /**
     * 重置规则执行状态
     */
    int resetRuleExecutionStatus(@Param("ruleId") Long ruleId);

    /**
     * 批量重置规则执行状态
     */
    int batchResetRuleExecutionStatus(@Param("ruleIds") List<Long> ruleIds);

    /**
     * 验证规则数据一致性
     */
    List<Map<String, Object>> validateRuleDataConsistency(@Param("orgId") Long orgId);

    /**
     * 修复规则数据不一致问题
     */
    int fixRuleDataInconsistency(@Param("ruleId") Long ruleId);

    // ==================== 审计日志方法 ====================

    /**
     * 记录规则操作日志
     */
    int insertRuleOperationLog(@Param("ruleId") Long ruleId, @Param("operation") String operation,
                              @Param("operationData") String operationData, @Param("operatorId") Long operatorId);

    /**
     * 查询规则操作日志
     */
    List<Map<String, Object>> selectRuleOperationLogs(@Param("ruleId") Long ruleId,
                                                      @Param("startDate") LocalDate startDate,
                                                      @Param("endDate") LocalDate endDate);

    /**
     * 查询规则变更历史
     */
    List<Map<String, Object>> selectRuleChangeHistory(@Param("ruleId") Long ruleId);
}
