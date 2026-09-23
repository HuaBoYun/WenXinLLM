package com.global.treasurer.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.global.treasurer.entity.FundDistributionRule;
import com.global.treasurer.util.PageResult;
import com.global.treasurer.util.PageableParam;

import java.util.List;
import java.util.Map;

/**
 * 资金下拨规则服务接口
 * 
 * @author Global Treasurer System
 * @since 2025-10-11
 */
public interface FundDistributionRuleService extends IService<FundDistributionRule> {

    /**
     * 分页查询资金下拨规则
     * 
     * @param pageableParam 分页参数
     * @param queryParam 查询参数
     * @return 分页结果
     */
    PageResult<FundDistributionRule> getRulesPage(PageableParam pageableParam, FundDistributionRule queryParam);

    /**
     * 创建资金下拨规则
     * 
     * @param rule 规则信息
     * @return 创建的规则
     */
    FundDistributionRule createRule(FundDistributionRule rule);

    /**
     * 更新资金下拨规则
     * 
     * @param rule 规则信息
     * @return 更新的规则
     */
    FundDistributionRule updateRule(FundDistributionRule rule);

    /**
     * 根据ID查询资金下拨规则
     * 
     * @param id 规则ID
     * @return 规则信息
     */
    FundDistributionRule getRuleById(Long id);

    /**
     * 删除资金下拨规则
     * 
     * @param id 规则ID
     */
    void deleteRule(Long id);

    /**
     * 启用资金下拨规则
     * 
     * @param id 规则ID
     */
    void enableRule(Long id);

    /**
     * 禁用资金下拨规则
     * 
     * @param id 规则ID
     */
    void disableRule(Long id);

    /**
     * 获取资金下拨规则列表
     * 
     * @param queryParam 查询参数
     * @return 规则列表
     */
    List<FundDistributionRule> getRulesList(FundDistributionRule queryParam);

    /**
     * 根据规则编码查询
     * 
     * @param ruleCode 规则编码
     * @param orgId 组织ID
     * @return 规则信息
     */
    FundDistributionRule getByRuleCode(String ruleCode, Long orgId);

    /**
     * 根据资金池ID查询规则列表
     * 
     * @param poolId 资金池ID
     * @return 规则列表
     */
    List<FundDistributionRule> getByPoolId(Long poolId);

    /**
     * 根据下拨类型查询
     * 
     * @param distributionType 下拨类型
     * @param orgId 组织ID
     * @return 规则列表
     */
    List<FundDistributionRule> getByDistributionType(String distributionType, Long orgId);

    /**
     * 根据触发类型查询
     * 
     * @param triggerType 触发类型
     * @param orgId 组织ID
     * @return 规则列表
     */
    List<FundDistributionRule> getByTriggerType(String triggerType, Long orgId);

    /**
     * 查询启用的规则
     * 
     * @param orgId 组织ID
     * @return 规则列表
     */
    List<FundDistributionRule> getEnabledRules(Long orgId);

    /**
     * 查询有效的规则（在有效期内且启用）
     * 
     * @param orgId 组织ID
     * @return 规则列表
     */
    List<FundDistributionRule> getEffectiveRules(Long orgId);

    /**
     * 查询定时规则
     * 
     * @param orgId 组织ID
     * @return 规则列表
     */
    List<FundDistributionRule> getScheduledRules(Long orgId);

    /**
     * 查询阈值规则
     * 
     * @param orgId 组织ID
     * @return 规则列表
     */
    List<FundDistributionRule> getThresholdRules(Long orgId);

    /**
     * 查询申请规则
     * 
     * @param orgId 组织ID
     * @return 规则列表
     */
    List<FundDistributionRule> getRequestRules(Long orgId);

    /**
     * 批量启用规则
     * 
     * @param ruleIds 规则ID列表
     * @return 操作结果
     */
    boolean batchEnableRules(List<Long> ruleIds);

    /**
     * 批量禁用规则
     * 
     * @param ruleIds 规则ID列表
     * @return 操作结果
     */
    boolean batchDisableRules(List<Long> ruleIds);

    /**
     * 复制规则
     * 
     * @param sourceRuleId 源规则ID
     * @param newRuleCode 新规则编码
     * @param newRuleName 新规则名称
     * @return 新规则
     */
    FundDistributionRule copyRule(Long sourceRuleId, String newRuleCode, String newRuleName);

    /**
     * 验证规则配置
     * 
     * @param rule 规则信息
     * @return 验证结果
     */
    Map<String, Object> validateRule(FundDistributionRule rule);

    /**
     * 获取规则统计信息
     * 
     * @param orgId 组织ID
     * @return 统计信息
     */
    Map<String, Object> getRuleStatistics(Long orgId);

    /**
     * 按下拨类型统计规则数量
     * 
     * @param orgId 组织ID
     * @return 统计结果
     */
    List<Map<String, Object>> getRuleCountByDistributionType(Long orgId);

    /**
     * 按触发类型统计规则数量
     * 
     * @param orgId 组织ID
     * @return 统计结果
     */
    List<Map<String, Object>> getRuleCountByTriggerType(Long orgId);

    /**
     * 按状态统计规则数量
     * 
     * @param orgId 组织ID
     * @return 统计结果
     */
    List<Map<String, Object>> getRuleCountByStatus(Long orgId);

    /**
     * 查询即将过期的规则
     * 
     * @param days 天数
     * @param orgId 组织ID
     * @return 规则列表
     */
    List<FundDistributionRule> getRulesNearExpiry(Integer days, Long orgId);

    /**
     * 查询已过期的规则
     * 
     * @param orgId 组织ID
     * @return 规则列表
     */
    List<FundDistributionRule> getExpiredRules(Long orgId);

    /**
     * 查询长期未执行的规则
     * 
     * @param days 天数
     * @param orgId 组织ID
     * @return 规则列表
     */
    List<FundDistributionRule> getLongUnexecutedRules(Integer days, Long orgId);

    /**
     * 检查规则冲突
     * 
     * @param poolId 资金池ID
     * @return 冲突检测结果
     */
    List<Map<String, Object>> checkRuleConflicts(Long poolId);

    /**
     * 获取规则执行历史
     * 
     * @param ruleId 规则ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 执行历史
     */
    List<Map<String, Object>> getRuleExecutionHistory(Long ruleId, String startDate, String endDate);

    /**
     * 模拟规则执行
     * 
     * @param ruleId 规则ID
     * @param simulationParams 模拟参数
     * @return 模拟结果
     */
    Map<String, Object> simulateRuleExecution(Long ruleId, Map<String, Object> simulationParams);

    /**
     * 导出规则数据
     * 
     * @param ruleIds 规则ID列表
     * @param orgId 组织ID
     * @return 导出数据
     */
    List<Map<String, Object>> exportRuleData(List<Long> ruleIds, Long orgId);

    /**
     * 清理过期规则
     * 
     * @param beforeDate 截止日期
     * @param orgId 组织ID
     * @return 清理数量
     */
    int cleanupExpiredRules(String beforeDate, Long orgId);
}
