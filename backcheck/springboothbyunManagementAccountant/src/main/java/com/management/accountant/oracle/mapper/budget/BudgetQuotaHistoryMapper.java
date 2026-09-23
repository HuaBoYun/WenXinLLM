package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetQuotaHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 预算配额操作历史Mapper
 */
@Mapper
public interface BudgetQuotaHistoryMapper extends BaseMapper<BudgetQuotaHistory> {

    @Select("SELECT HISTORY_ID, QUOTA_ID, OPERATION_TYPE, OPERATION_DESC, " +
            "AMOUNT, BEFORE_VALUE, AFTER_VALUE, OPERATOR, OPERATE_TIME, IP_ADDRESS, CREATE_TIME " +
            "FROM TBL_BUDGET_QUOTA_HISTORY " +
            "WHERE QUOTA_ID = #{quotaId} " +
            "ORDER BY OPERATE_TIME DESC")
    List<BudgetQuotaHistory> selectByQuotaId(@Param("quotaId") String quotaId);
}
