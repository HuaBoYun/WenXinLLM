package com.management.accountant.service;

import com.management.accountant.oracle.entity.budget.BudgetControlRule;
import com.management.accountant.util.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 预算控制规则Service接口
 * 
 * @description 预算控制规则业务接口
 * @author AI Assistant
 * @date 2025-01-04
 */
public interface BudgetControlRuleService {

    /**
     * 创建控制规则
     * 
     * @param rule 规则对象
     * @return 创建后的规则对象
     */
    BudgetControlRule create(BudgetControlRule rule);

    /**
     * 根据ID查询规则
     * 
     * @param ruleId 规则ID
     * @return 规则对象
     */
    BudgetControlRule getById(String ruleId);

    /**
     * 更新控制规则
     * 
     * @param rule 规则对象
     */
    void update(BudgetControlRule rule);

    /**
     * 删除控制规则
     * 
     * @param ruleId 规则ID
     */
    void delete(String ruleId);

    /**
     * 分页查询规则列表
     * 
     * @param params 查询参数
     * @return 分页结果
     */
    PageResult<BudgetControlRule> getPage(Map<String, Object> params);

    /**
     * 启用规则
     * 
     * @param ruleId 规则ID
     */
    void enable(String ruleId);

    /**
     * 禁用规则
     * 
     * @param ruleId 规则ID
     */
    void disable(String ruleId);

    /**
     * 批量删除
     * 
     * @param ids 规则ID列表
     */
    void batchDelete(List<String> ids);

    /**
     * 校验规则
     * 
     * @param params 校验参数
     * @return 校验结果
     */
    Map<String, Object> validateRule(Map<String, Object> params);

    /**
     * 获取规则统计信息
     * 
     * @return 统计信息
     */
    Map<String, Object> getStatistics();

    /**
     * 获取生效的规则列表
     *
     * @param controlType 控制类型
     * @return 规则列表
     */
    List<BudgetControlRule> getActiveRules(String controlType);

    /**
     * 复制规则
     *
     * @param ruleId 规则ID
     * @return 新规则对象
     */
    BudgetControlRule copyRule(String ruleId);

    /**
     * 导出控制规则数据
     *
     * @param params 查询参数
     * @return 规则数据列表
     */
    List<BudgetControlRule> exportData(Map<String, Object> params);
}

