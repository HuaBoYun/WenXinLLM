package com.financial.sharing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.entity.TblAuditRule;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 审批规则主表Mapper
 */
public interface TblAuditRuleMapper extends BaseMapper<TblAuditRule> {

    /**
     * 根据规则编码查询
     */
    TblAuditRule selectByRuleCode(@Param("ruleCode") String ruleCode);

    /**
     * 根据规则类型查询
     */
    List<TblAuditRule> selectByRuleType(@Param("ruleType") String ruleType);

    /**
     * 根据是否启用查询
     */
    List<TblAuditRule> selectByIsEnabled(@Param("isEnabled") Integer isEnabled);

    /**
     * 根据规则类型和是否启用查询
     */
    List<TblAuditRule> selectByRuleTypeAndEnabled(@Param("ruleType") String ruleType, @Param("isEnabled") Integer isEnabled);

    /**
     * 查询所有启用的规则，按优先级降序
     */
    List<TblAuditRule> selectAllEnabledRules();
}
