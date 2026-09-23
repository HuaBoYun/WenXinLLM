package com.financial.sharing.budgetPlanning.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.budgetPlanning.dto.BudgetRuleQueryParam;
import com.financial.sharing.budgetPlanning.entity.TblBudgetRule;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 业务规则Mapper接口
 * 
 * @author hbyun
 * @date 2026-01-30
 */
public interface BudgetRuleMapper extends BaseMapper<TblBudgetRule> {

    /**
     * 查询业务规则列表
     * 
     * @param param 查询参数
     * @return 业务规则列表
     */
    List<TblBudgetRule> selectRuleList(@Param("param") BudgetRuleQueryParam param);

    /**
     * 根据规则编码查询业务规则
     * 
     * @param ruleCode 规则编码
     * @return 业务规则
     */
    TblBudgetRule selectByRuleCode(@Param("ruleCode") String ruleCode);

    /**
     * 检查规则编码是否存在
     * 
     * @param ruleCode 规则编码
     * @param excludeId 排除的规则ID(用于编辑时排除自己)
     * @return 存在数量
     */
    int checkRuleCodeExists(@Param("ruleCode") String ruleCode, @Param("excludeId") String excludeId);
}

