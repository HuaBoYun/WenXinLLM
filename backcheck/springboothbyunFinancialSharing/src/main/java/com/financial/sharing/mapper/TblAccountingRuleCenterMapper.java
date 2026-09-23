package com.financial.sharing.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.entity.TblAccountingRuleCenter;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 会计规则中心Mapper
 */
public interface TblAccountingRuleCenterMapper extends BaseMapper<TblAccountingRuleCenter> {

    /**
     * 根据规则编码查询
     */
    TblAccountingRuleCenter selectByRuleCode(@Param("ruleCode") String ruleCode);

    /**
     * 根据规则类型查询
     */
    List<TblAccountingRuleCenter> selectByRuleType(@Param("ruleType") String ruleType);

    /**
     * 根据优先级范围查询
     */
    List<TblAccountingRuleCenter> selectByPriorityRange(@Param("minPriority") Integer minPriority, @Param("maxPriority") Integer maxPriority);

    /**
     * 根据是否启用查询
     */
    List<TblAccountingRuleCenter> selectByIsEnabled(@Param("isEnabled") Integer isEnabled);

    /**
     * 根据借方科目查询
     */
    List<TblAccountingRuleCenter> selectByDebitAccount(@Param("debitAccount") String debitAccount);

    /**
     * 根据贷方科目查询
     */
    List<TblAccountingRuleCenter> selectByCreditAccount(@Param("creditAccount") String creditAccount);
}
