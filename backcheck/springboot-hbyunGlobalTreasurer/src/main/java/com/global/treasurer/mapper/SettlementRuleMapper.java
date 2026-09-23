package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.global.treasurer.entity.SettlementRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 结算规则Mapper接口
 * 
 * @author Global Treasurer System
 * @since 2025-09-22
 */
@Mapper
public interface SettlementRuleMapper extends BaseMapper<SettlementRule> {

    /**
     * 分页查询结算规则
     */
    IPage<SettlementRule> selectRulePage(Page<SettlementRule> page,
                                         @Param("ruleType") String ruleType,
                                         @Param("businessType") String businessType,
                                         @Param("isEnabled") Integer isEnabled,
                                         @Param("orgId") Long orgId);

    /**
     * 根据规则代码查询规则
     */
    SettlementRule selectByRuleCode(@Param("ruleCode") String ruleCode,
                                   @Param("orgId") Long orgId);

    /**
     * 查询启用的规则
     */
    List<SettlementRule> selectEnabledRules(@Param("orgId") Long orgId);

    /**
     * 查询指定类型的规则
     */
    List<SettlementRule> selectByRuleType(@Param("ruleType") String ruleType,
                                         @Param("orgId") Long orgId);

    /**
     * 查询指定业务类型的规则
     */
    List<SettlementRule> selectByBusinessType(@Param("businessType") String businessType,
                                             @Param("orgId") Long orgId);

    /**
     * 查询适用于指定条件的规则
     */
    List<SettlementRule> selectApplicableRules(@Param("businessType") String businessType,
                                              @Param("accountId") Long accountId,
                                              @Param("currencyCode") String currencyCode,
                                              @Param("orgId") Long orgId);

    /**
     * 查询高优先级规则
     */
    List<SettlementRule> selectHighPriorityRules(@Param("orgId") Long orgId);

    /**
     * 查询即将失效的规则
     */
    List<SettlementRule> selectExpiringSoonRules(@Param("days") Integer days,
                                                @Param("orgId") Long orgId);

    /**
     * 查询已失效的规则
     */
    List<SettlementRule> selectExpiredRules(@Param("currentDate") LocalDate currentDate,
                                           @Param("orgId") Long orgId);

    /**
     * 查询时间规则
     */
    List<SettlementRule> selectTimingRules(@Param("orgId") Long orgId);

    /**
     * 查询金额规则
     */
    List<SettlementRule> selectAmountRules(@Param("orgId") Long orgId);

    /**
     * 查询优先级规则
     */
    List<SettlementRule> selectPriorityRules(@Param("orgId") Long orgId);

    /**
     * 查询路由规则
     */
    List<SettlementRule> selectRoutingRules(@Param("orgId") Long orgId);

    /**
     * 批量启用规则
     */
    int batchEnableRules(@Param("ruleIds") List<Long> ruleIds,
                        @Param("updateUser") Long updateUser);

    /**
     * 批量禁用规则
     */
    int batchDisableRules(@Param("ruleIds") List<Long> ruleIds,
                         @Param("updateUser") Long updateUser);

    /**
     * 更新规则状态
     */
    int updateRuleStatus(@Param("ruleId") Long ruleId,
                        @Param("isEnabled") Integer isEnabled,
                        @Param("updateUser") Long updateUser);

    /**
     * 延长规则有效期
     */
    int extendRuleExpireDate(@Param("ruleId") Long ruleId,
                            @Param("expireDate") LocalDate expireDate,
                            @Param("updateUser") Long updateUser);

    /**
     * 统计规则概要
     */
    Map<String, Object> selectRuleSummary(@Param("orgId") Long orgId);

    /**
     * 按规则类型统计
     */
    List<Map<String, Object>> selectRulesByType(@Param("orgId") Long orgId);

    /**
     * 按业务类型统计规则
     */
    List<Map<String, Object>> selectRulesByBusinessType(@Param("orgId") Long orgId);

    /**
     * 按状态统计规则
     */
    List<Map<String, Object>> selectRulesByStatus(@Param("orgId") Long orgId);

    /**
     * 查询规则使用统计
     */
    List<Map<String, Object>> selectRuleUsageStats(@Param("startDate") LocalDate startDate,
                                                  @Param("endDate") LocalDate endDate,
                                                  @Param("orgId") Long orgId);

    /**
     * 查询规则执行效果分析
     */
    List<Map<String, Object>> selectRuleEffectivenessAnalysis(@Param("orgId") Long orgId);

    /**
     * 查询规则冲突检查
     */
    List<Map<String, Object>> selectRuleConflictCheck(@Param("orgId") Long orgId);

    /**
     * 查询规则覆盖率分析
     */
    Map<String, Object> selectRuleCoverageAnalysis(@Param("orgId") Long orgId);

    /**
     * 查询规则性能分析
     */
    List<Map<String, Object>> selectRulePerformanceAnalysis(@Param("orgId") Long orgId);

    /**
     * 查询规则维护建议
     */
    List<Map<String, Object>> selectRuleMaintenanceSuggestions(@Param("orgId") Long orgId);

    /**
     * 查询规则优化建议
     */
    List<Map<String, Object>> selectRuleOptimizationSuggestions(@Param("orgId") Long orgId);

    /**
     * 查询规则依赖关系
     */
    List<Map<String, Object>> selectRuleDependencies(@Param("ruleId") Long ruleId,
                                                    @Param("orgId") Long orgId);

    /**
     * 查询规则执行历史
     */
    List<Map<String, Object>> selectRuleExecutionHistory(@Param("ruleId") Long ruleId,
                                                        @Param("startDate") LocalDate startDate,
                                                        @Param("endDate") LocalDate endDate,
                                                        @Param("orgId") Long orgId);

    /**
     * 查询规则测试结果
     */
    List<Map<String, Object>> selectRuleTestResults(@Param("ruleId") Long ruleId,
                                                   @Param("orgId") Long orgId);

    /**
     * 查询规则版本历史
     */
    List<Map<String, Object>> selectRuleVersionHistory(@Param("ruleId") Long ruleId,
                                                      @Param("orgId") Long orgId);

    /**
     * 查询规则审计日志
     */
    List<Map<String, Object>> selectRuleAuditLog(@Param("ruleId") Long ruleId,
                                                @Param("orgId") Long orgId);

    /**
     * 查询规则配置完整性检查
     */
    List<SettlementRule> selectIncompleteRules(@Param("orgId") Long orgId);

    /**
     * 查询重复规则检查
     */
    List<Map<String, Object>> selectDuplicateRules(@Param("orgId") Long orgId);

    /**
     * 查询规则有效性验证
     */
    List<Map<String, Object>> selectRuleValidityCheck(@Param("orgId") Long orgId);

    /**
     * 查询规则影响范围分析
     */
    Map<String, Object> selectRuleImpactAnalysis(@Param("ruleId") Long ruleId,
                                                @Param("orgId") Long orgId);

    /**
     * 查询规则执行统计
     */
    Map<String, Object> selectRuleExecutionStats(@Param("ruleId") Long ruleId,
                                                @Param("startDate") LocalDate startDate,
                                                @Param("endDate") LocalDate endDate,
                                                @Param("orgId") Long orgId);

    /**
     * 查询规则匹配统计
     */
    List<Map<String, Object>> selectRuleMatchingStats(@Param("orgId") Long orgId);

    /**
     * 查询规则异常统计
     */
    List<Map<String, Object>> selectRuleExceptionStats(@Param("orgId") Long orgId);

    /**
     * 查询规则基准测试
     */
    Map<String, Object> selectRuleBenchmark(@Param("ruleId") Long ruleId,
                                           @Param("orgId") Long orgId);

    /**
     * 删除过期规则
     */
    int deleteExpiredRules(@Param("expireDate") LocalDate expireDate,
                          @Param("orgId") Long orgId);

    /**
     * 归档历史规则
     */
    int archiveHistoricalRules(@Param("archiveDate") LocalDate archiveDate,
                              @Param("orgId") Long orgId);

    /**
     * 清理规则数据
     */
    int cleanupRuleData(@Param("cleanupDate") LocalDate cleanupDate,
                       @Param("orgId") Long orgId);

    /**
     * 验证规则语法
     */
    Map<String, Object> validateRuleSyntax(@Param("ruleId") Long ruleId);

    /**
     * 测试规则执行
     */
    Map<String, Object> testRuleExecution(@Param("ruleId") Long ruleId,
                                         @Param("testData") String testData);
}
