package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetReserveHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 预算储备操作历史Mapper
 */
@Mapper
public interface BudgetReserveHistoryMapper extends BaseMapper<BudgetReserveHistory> {

    @Select("SELECT HISTORY_ID, RESERVE_ID, OPERATION_TYPE, OPERATION_DESC, " +
            "AMOUNT, BEFORE_VALUE, AFTER_VALUE, OPERATOR, OPERATE_TIME, IP_ADDRESS, CREATE_TIME " +
            "FROM TBL_BUDGET_RESERVE_HISTORY " +
            "WHERE RESERVE_ID = #{reserveId} " +
            "ORDER BY OPERATE_TIME DESC")
    List<BudgetReserveHistory> selectByReserveId(@Param("reserveId") String reserveId);
}
