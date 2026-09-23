package com.financial.sharing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.entity.TblBillAuditRule;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 账单稽核规则关联Mapper
 */
public interface TblBillAuditRuleMapper extends BaseMapper<TblBillAuditRule> {

    /**
     * 根据配置ID查询
     */
    List<TblBillAuditRule> selectByConfigId(@Param("configId") String configId);

    /**
     * 根据配置ID查询，按优先级排序
     */
    List<TblBillAuditRule> selectByConfigIdOrderByPriority(@Param("configId") String configId);

    /**
     * 根据规则ID查询
     */
    List<TblBillAuditRule> selectByRuleId(@Param("ruleId") String ruleId);

    /**
     * 根据配置ID和规则ID查询
     */
    TblBillAuditRule selectByConfigIdAndRuleId(@Param("configId") String configId, @Param("ruleId") String ruleId);

    /**
     * 根据配置ID删除关联
     */
    int deleteByConfigId(@Param("configId") String configId);

    /**
     * 根据规则ID删除关联
     */
    int deleteByRuleId(@Param("ruleId") String ruleId);
}
