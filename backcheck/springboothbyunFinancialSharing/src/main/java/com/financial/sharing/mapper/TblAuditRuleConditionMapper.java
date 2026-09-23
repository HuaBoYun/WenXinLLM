package com.financial.sharing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.entity.TblAuditRuleCondition;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 规则条件表Mapper
 */
public interface TblAuditRuleConditionMapper extends BaseMapper<TblAuditRuleCondition> {

    /**
     * 根据规则ID查询条件列表
     */
    List<TblAuditRuleCondition> selectByRuleId(@Param("ruleId") String ruleId);

    /**
     * 根据规则ID查询条件列表，按排序号升序
     */
    List<TblAuditRuleCondition> selectByRuleIdOrderBySort(@Param("ruleId") String ruleId);

    /**
     * 根据条件字段查询
     */
    List<TblAuditRuleCondition> selectByConditionField(@Param("conditionField") String conditionField);

    /**
     * 根据规则ID删除所有条件
     */
    int deleteByRuleId(@Param("ruleId") String ruleId);
}
