package com.global.treasurer.service;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.FinancingPlanDTO;
import com.global.treasurer.dto.FinancingPlanQueryDTO;
import com.global.treasurer.entity.TblFinancingPlan;

import java.util.List;
import java.util.Map;

/**
 * 融资计划服务接口
 *
 * @author 华博云开发团队
 * @since 2025-12-30
 */
public interface FinancingPlanService {

    /**
     * 分页查询融资计划列表
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    PageInfo<TblFinancingPlan> getPlanList(FinancingPlanQueryDTO queryDTO);

    /**
     * 根据ID查询融资计划详情
     *
     * @param planId 计划ID
     * @return 融资计划
     */
    TblFinancingPlan getPlanById(Long planId);

    /**
     * 保存融资计划（新增或更新）
     *
     * @param dto 融资计划DTO
     * @return 保存后的融资计划
     */
    TblFinancingPlan savePlan(FinancingPlanDTO dto);

    /**
     * 删除融资计划
     *
     * @param planId 计划ID
     */
    void deletePlan(Long planId);

    /**
     * 批量删除融资计划
     *
     * @param planIds 计划ID列表
     */
    void batchDeletePlans(List<Long> planIds);

    /**
     * 提交审批
     *
     * @param planId 计划ID
     */
    void submitForApproval(Long planId);

    /**
     * 审批通过
     *
     * @param planId 计划ID
     * @param comments 审批意见
     */
    void approve(Long planId, String comments);

    /**
     * 审批拒绝
     *
     * @param planId 计划ID
     * @param comments 审批意见
     */
    void reject(Long planId, String comments);

    /**
     * 查询年度融资计划汇总
     *
     * @param planYear 计划年度
     * @param companyId 公司ID
     * @return 汇总数据
     */
    Map<String, Object> getYearSummary(Integer planYear, Long companyId);
}

