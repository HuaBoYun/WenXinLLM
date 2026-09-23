package com.financial.sharing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.entity.TblBudgetControlScope;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 规则适用范围Mapper
 */
public interface TblBudgetControlScopeMapper extends BaseMapper<TblBudgetControlScope> {

    /**
     * 根据规则ID查询
     */
    List<TblBudgetControlScope> selectByRuleId(@Param("ruleId") String ruleId);

    /**
     * 根据规则ID和范围类型查询
     */
    List<TblBudgetControlScope> selectByRuleIdAndType(@Param("ruleId") String ruleId, @Param("scopeType") String scopeType);

    /**
     * 根据范围类型和范围值查询
     */
    List<TblBudgetControlScope> selectByTypeAndValue(@Param("scopeType") String scopeType, @Param("scopeValue") String scopeValue);

    /**
     * 根据规则ID删除范围
     */
    int deleteByRuleId(@Param("ruleId") String ruleId);

    /**
     * 批量插入范围
     */
    int batchInsert(@Param("list") List<TblBudgetControlScope> list);
}
