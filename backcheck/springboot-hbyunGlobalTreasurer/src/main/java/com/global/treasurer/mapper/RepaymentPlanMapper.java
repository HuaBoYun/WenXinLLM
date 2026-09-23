package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblRepaymentPlan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 还款计划Mapper接口
 *
 * @author 华博云开发团队
 * @since 2026-02-09
 */
@Mapper
public interface RepaymentPlanMapper extends BaseMapper<TblRepaymentPlan> {

    /**
     * 查询还款计划列表
     */
    List<TblRepaymentPlan> selectPlanList(@Param("params") Map<String, Object> params);

    /**
     * 根据贷款ID查询还款计划
     */
    List<TblRepaymentPlan> selectByLoanId(@Param("loanId") String loanId);

    /**
     * 根据合同ID查询还款计划
     */
    List<TblRepaymentPlan> selectByContractId(@Param("contractId") String contractId);

    /**
     * 查询待还款计划（按到期日期排序）
     */
    List<TblRepaymentPlan> selectPendingPlans(@Param("loanId") String loanId);

    /**
     * 查询逾期还款计划
     */
    List<TblRepaymentPlan> selectOverduePlans(@Param("loanId") String loanId);

    /**
     * 统计贷款的还款计划数量
     */
    int countByLoanId(@Param("loanId") String loanId);

    /**
     * 删除贷款的所有还款计划
     */
    int deleteByLoanId(@Param("loanId") String loanId);
}

