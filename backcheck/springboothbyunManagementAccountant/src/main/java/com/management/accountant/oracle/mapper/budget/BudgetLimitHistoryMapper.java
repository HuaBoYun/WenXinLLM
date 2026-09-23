package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetLimitHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 预算限额操作历史Mapper
 */
@Mapper
public interface BudgetLimitHistoryMapper extends BaseMapper<BudgetLimitHistory> {

    @Select("SELECT HISTORY_ID, LIMIT_ID, OPERATION_TYPE, OPERATION_DESC, " +
            "BEFORE_VALUE, AFTER_VALUE, OPERATOR, OPERATE_TIME, IP_ADDRESS, CREATE_TIME " +
            "FROM TBL_BUDGET_LIMIT_HISTORY " +
            "WHERE LIMIT_ID = #{limitId} " +
            "ORDER BY OPERATE_TIME DESC")
    List<BudgetLimitHistory> selectByLimitId(@Param("limitId") String limitId);
}
