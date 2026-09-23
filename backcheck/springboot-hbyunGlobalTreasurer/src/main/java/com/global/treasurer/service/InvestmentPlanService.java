package com.global.treasurer.service;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.InvestmentPlanDTO;
import com.global.treasurer.dto.InvestmentPlanQueryDTO;
import com.global.treasurer.entity.TblInvestmentPlan;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 投资计划服务接口
 *
 * @author 华博云开发团队
 * @since 2025-01-04
 */
public interface InvestmentPlanService {

    /**
     * 分页查询投资计划列表
     */
    PageInfo<TblInvestmentPlan> getPlanList(InvestmentPlanQueryDTO queryDTO);

    /**
     * 根据ID获取投资计划详情
     */
    TblInvestmentPlan getPlanById(Long planId);

    /**
     * 保存投资计划（新增或更新）
     */
    TblInvestmentPlan savePlan(InvestmentPlanDTO dto);

    /**
     * 删除投资计划
     */
    void deletePlan(Long planId);

    /**
     * 批量删除投资计划
     */
    void batchDeletePlans(List<Long> planIds);

    /**
     * 提交计划（草稿->已提交）
     */
    void submitPlan(Long planId);

    /**
     * 审批通过计划（已提交->已审批）
     */
    void approvePlan(Long planId, String approvalComments);

    /**
     * 驳回计划（已提交->草稿）
     */
    void rejectPlan(Long planId, String rejectionReason);

    /**
     * 开始执行计划（已审批->执行中）
     */
    void executePlan(Long planId);

    /**
     * 完成计划（执行中->已完成）
     */
    void completePlan(Long planId, String completionNotes);

    /**
     * 取消计划
     */
    void cancelPlan(Long planId, String cancelReason);

    /**
     * 更新已投资金额
     */
    void updateInvestedAmount(Long planId, BigDecimal investedAmount);

    /**
     * 更新实际收益率
     */
    void updateActualReturnRate(Long planId, BigDecimal actualReturnRate);

    /**
     * 获取计划统计信息
     */
    Map<String, Object> getPlanStatistics();

    /**
     * 获取即将到期的计划
     */
    List<TblInvestmentPlan> getNearExpiryPlans(Integer days);

    /**
     * 获取逾期计划
     */
    List<TblInvestmentPlan> getOverduePlans();

    /**
     * 导出计划数据
     */
    List<TblInvestmentPlan> exportPlans(InvestmentPlanQueryDTO queryDTO);
}

