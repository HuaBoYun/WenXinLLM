package com.financial.sharing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.entity.TblBudgetControlRule;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 预算控制规则Mapper
 */
public interface TblBudgetControlRuleMapper extends BaseMapper<TblBudgetControlRule> {

    /**
     * 根据规则编码查询
     */
    TblBudgetControlRule selectByRuleCode(@Param("ruleCode") String ruleCode);

    /**
     * 根据控制类型查询
     */
    List<TblBudgetControlRule> selectByControlType(@Param("controlType") String controlType);

    /**
     * 根据是否启用查询
     */
    List<TblBudgetControlRule> selectByIsEnabled(@Param("isEnabled") Integer isEnabled);

    /**
     * 查询有效期内的规则
     */
    List<TblBudgetControlRule> selectEffectiveRules(@Param("currentDate") String currentDate);
}
