package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.PeriodHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 期间操作历史Mapper接口
 *
 * @author AI Agent
 * @date 2026-03-31
 */
@Mapper
public interface PeriodHistoryMapper extends BaseMapper<PeriodHistory> {

    @Select("SELECT HISTORY_ID, PERIOD_ID, OPERATION_TYPE, OPERATION_DESC, " +
            "BEFORE_VALUE, AFTER_VALUE, OPERATOR, OPERATE_TIME, IP_ADDRESS, CREATE_TIME " +
            "FROM TBL_PERIOD_HISTORY " +
            "WHERE PERIOD_ID = #{periodId} " +
            "ORDER BY OPERATE_TIME DESC")
    List<PeriodHistory> selectByPeriodId(@Param("periodId") String periodId);
}
