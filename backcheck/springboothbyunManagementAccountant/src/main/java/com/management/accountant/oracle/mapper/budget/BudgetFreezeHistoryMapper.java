package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetFreezeHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 预算冻结操作历史Mapper
 */
@Mapper
public interface BudgetFreezeHistoryMapper extends BaseMapper<BudgetFreezeHistory> {

    @Select("SELECT HISTORY_ID, FREEZE_ID, OPERATION_TYPE, OPERATION_DESC, " +
            "AMOUNT, BEFORE_VALUE, AFTER_VALUE, OPERATOR, OPERATE_TIME, IP_ADDRESS, CREATE_TIME " +
            "FROM TBL_BUDGET_FREEZE_HISTORY " +
            "WHERE FREEZE_ID = #{freezeId} " +
            "ORDER BY OPERATE_TIME DESC")
    List<BudgetFreezeHistory> selectByFreezeId(@Param("freezeId") String freezeId);
}
