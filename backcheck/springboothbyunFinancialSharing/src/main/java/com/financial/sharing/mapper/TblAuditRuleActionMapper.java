package com.financial.sharing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.entity.TblAuditRuleAction;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 规则动作表Mapper
 */
public interface TblAuditRuleActionMapper extends BaseMapper<TblAuditRuleAction> {

    /**
     * 根据规则ID查询动作列表
     */
    List<TblAuditRuleAction> selectByRuleId(@Param("ruleId") String ruleId);

    /**
     * 根据规则ID查询动作列表，按排序号升序
     */
    List<TblAuditRuleAction> selectByRuleIdOrderBySort(@Param("ruleId") String ruleId);

    /**
     * 根据动作类型查询
     */
    List<TblAuditRuleAction> selectByActionType(@Param("actionType") String actionType);

    /**
     * 根据规则ID删除所有动作
     */
    int deleteByRuleId(@Param("ruleId") String ruleId);
}
