package com.financial.sharing.mysql.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.mysql.entity.TblPaymentPlan;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * 付款计划Mapper
 * @author system
 * @since 2025-01-05
 */
public interface PaymentPlanMapper extends BaseMapper<TblPaymentPlan> {

    /**
     * 查询日期范围内的付款计划
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 付款计划列表
     */
    List<TblPaymentPlan> selectByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    /**
     * 查询待执行的付款计划
     * @return 付款计划列表
     */
    List<TblPaymentPlan> selectPendingPlans();
}

