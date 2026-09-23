package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算规则Mapper
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Mapper
public interface BudgetRuleMapper extends BaseMapper<BudgetRule> {

    /**
     * 根据规则编码查询规则
     */
    BudgetRule selectByRuleCode(@Param("ruleCode") String ruleCode);

    /**
     * 根据规则类型查询规则列表
     */
    List<BudgetRule> selectByRuleType(@Param("ruleType") String ruleType);

    /**
     * 查询启用的规则列表
     */
    List<BudgetRule> selectEnabledRules();

    /**
     * 根据优先级查询规则列表
     */
    List<BudgetRule> selectByPriority(@Param("minPriority") Integer minPriority, @Param("maxPriority") Integer maxPriority);

    /**
     * 批量删除规则
     */
    int batchDeleteRules(@Param("ruleIds") List<String> ruleIds);
}

