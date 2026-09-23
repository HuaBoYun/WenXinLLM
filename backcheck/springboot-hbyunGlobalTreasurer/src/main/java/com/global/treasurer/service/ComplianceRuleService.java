package com.global.treasurer.service;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblComplianceRule;

import java.util.List;
import java.util.Map;

/**
 * 合规检查规则服务接口
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
public interface ComplianceRuleService {

    /**
     * 分页查询合规检查规则列表
     *
     * @param params 查询参数
     * @return 分页结果
     */
    PageInfo<TblComplianceRule> getRuleList(Map<String, Object> params);

    /**
     * 根据ID查询合规检查规则详情
     *
     * @param ruleId 规则ID
     * @return 合规检查规则
     */
    TblComplianceRule getRuleById(String ruleId);

    /**
     * 保存合规检查规则（新增或更新）
     *
     * @param rule 合规检查规则
     * @return 保存后的合规检查规则
     */
    TblComplianceRule saveRule(TblComplianceRule rule);

    /**
     * 删除合规检查规则
     *
     * @param ruleId 规则ID
     */
    void deleteRule(String ruleId);

    /**
     * 批量删除合规检查规则
     *
     * @param ruleIds 规则ID列表
     */
    void batchDeleteRules(List<String> ruleIds);

    /**
     * 查询可执行的规则
     *
     * @return 合规检查规则列表
     */
    List<TblComplianceRule> getExecutableRules();

    /**
     * 启用/停用合规检查规则
     *
     * @param ruleId 规则ID
     * @param isEnabled 是否启用
     */
    void toggleRuleStatus(String ruleId, Integer isEnabled);

    /**
     * 执行合规检查
     *
     * @param ruleId 规则ID
     * @param checkData 检查数据
     * @return 检查结果
     */
    Map<String, Object> executeComplianceCheck(String ruleId, Map<String, Object> checkData);

    /**
     * 执行合规检查
     *
     * @param ruleId 规则ID
     * @param targetType 检查对象类型
     * @return 检查结果
     */
    Map<String, Object> executeCheck(String ruleId, String targetType);

    /**
     * 批量执行合规检查
     *
     * @param ruleIds 规则ID列表
     */
    void batchExecuteRules(List<String> ruleIds);

    /**
     * 复制合规检查规则
     *
     * @param ruleId 源规则ID
     * @return 新规则
     */
    TblComplianceRule copyRule(String ruleId);

    /**
     * 导出合规检查规则数据
     *
     * @param params 查询参数
     * @return 规则列表
     */
    List<TblComplianceRule> exportRules(Map<String, Object> params);
}

