package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetControlRule;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算控制规则Mapper接口
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Mapper
public interface BudgetControlRuleMapper extends BaseMapper<BudgetControlRule> {

    /**
     * 根据规则编码查询规则
     * 
     * @param ruleCode 规则编码
     * @return 控制规则
     */
    BudgetControlRule selectByRuleCode(@Param("ruleCode") String ruleCode);

    /**
     * 查询启用的控制规则
     * 
     * @return 规则列表
     */
    List<BudgetControlRule> selectEnabledRules();

    /**
     * 根据控制类型查询规则
     * 
     * @param controlType 控制类型
     * @return 规则列表
     */
    List<BudgetControlRule> selectByControlType(@Param("controlType") String controlType);

    /**
     * 批量启用/禁用规则
     */
    int batchUpdateEnabled(@Param("ruleIds") List<String> ruleIds, @Param("isEnabled") Boolean isEnabled);

    /**
     * 批量删除规则（物理删除）
     */
    int batchDeleteByIds(@Param("ruleIds") List<String> ruleIds);
}

