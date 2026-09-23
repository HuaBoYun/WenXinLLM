package com.global.treasurer.service;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.RepaymentPlanDTO;
import com.global.treasurer.entity.TblRepaymentPlan;

import java.util.List;

/**
 * 还款计划Service接口
 *
 * @author 华博云开发团队
 * @since 2026-02-09
 */
public interface RepaymentPlanService {

    /**
     * 分页查询还款计划列表
     */
    PageInfo<TblRepaymentPlan> getPlanList(RepaymentPlanDTO dto);

    /**
     * 根据贷款ID查询还款计划
     */
    List<TblRepaymentPlan> getPlansByLoanId(String loanId);

    /**
     * 根据ID查询还款计划详情
     */
    TblRepaymentPlan getPlanById(Long planId);

    /**
     * 保存还款计划（新增或更新）
     */
    TblRepaymentPlan savePlan(RepaymentPlanDTO dto);

    /**
     * 删除还款计划
     */
    void deletePlan(Long planId);

    /**
     * 自动生成还款计划
     * @param dto 包含贷款ID、还款方式、期数、贷款金额、利率等参数
     * @return 生成的还款计划列表
     */
    List<TblRepaymentPlan> generateRepaymentPlans(RepaymentPlanDTO dto);

    /**
     * 执行还款操作
     * @param planId 还款计划ID
     * @param payAmount 还款金额
     */
    void executeRepayment(Long planId, java.math.BigDecimal payAmount);

    /**
     * 查询待还款计划
     */
    List<TblRepaymentPlan> getPendingPlans(String loanId);

    /**
     * 查询逾期还款计划
     */
    List<TblRepaymentPlan> getOverduePlans(String loanId);

    /**
     * 更新逾期状态（定时任务调用）
     */
    void updateOverdueStatus();
}

