package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetWarningRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算预警规则Mapper接口
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Mapper
public interface BudgetWarningRuleMapper extends BaseMapper<BudgetWarningRule> {

    /**
     * 根据规则编码查询规则
     * 
     * @param ruleCode 规则编码
     * @return 预警规则
     */
    BudgetWarningRule selectByRuleCode(@Param("ruleCode") String ruleCode);

    /**
     * 查询启用的预警规则
     * 
     * @return 规则列表
     */
    List<BudgetWarningRule> selectEnabledRules();

    /**
     * 根据预警类型查询规则
     * 
     * @param warningType 预警类型
     * @return 规则列表
     */
    List<BudgetWarningRule> selectByWarningType(@Param("warningType") String warningType);

    /**
     * 批量启用/禁用规则
     * 
     * @param ruleIds 规则ID列表
     * @param isEnabled 是否启用
     * @return 更新数量
     */
    int batchUpdateEnabled(@Param("ruleIds") List<String> ruleIds, @Param("isEnabled") Boolean isEnabled);

    /**
     * 批量删除规则
     *
     * @param ruleIds 规则ID列表
     * @return 删除数量
     */
    int batchDeleteByIds(@Param("ruleIds") List<String> ruleIds);

    /**
     * 启用/禁用规则
     *
     * @param ruleId 规则ID
     * @param isEnabled 是否启用
     * @return 更新数量
     */
    int toggleRule(@Param("ruleId") String ruleId, @Param("isEnabled") int isEnabled);

    /**
     * 批量启用/禁用规则
     *
     * @param ruleIds 规则ID列表
     * @param isEnabled 是否启用
     * @return 更新数量
     */
    int batchToggleRules(@Param("ruleIds") List<String> ruleIds, @Param("isEnabled") int isEnabled);
}

