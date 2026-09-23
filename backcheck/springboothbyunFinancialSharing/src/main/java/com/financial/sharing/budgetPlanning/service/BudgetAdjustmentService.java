package com.financial.sharing.budgetPlanning.service;

import com.financial.sharing.budgetPlanning.dto.BudgetAdjustmentQueryParam;
import com.financial.sharing.budgetPlanning.entity.TblBudgetAdjustment;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * 预算调整Service接口
 * 
 * @author hbyun
 * @date 2026-02-02
 */
public interface BudgetAdjustmentService {

    /**
     * 创建调整单
     * 
     * @param adjustment 调整单信息
     */
    void createAdjustment(TblBudgetAdjustment adjustment);

    /**
     * 修改调整单
     * 
     * @param adjustment 调整单信息
     */
    void updateAdjustment(TblBudgetAdjustment adjustment);

    /**
     * 查询调整单列表(分页)
     * 
     * @param param 查询参数
     * @return 分页结果
     */
    PageInfo<TblBudgetAdjustment> getAdjustmentList(BudgetAdjustmentQueryParam param);

    /**
     * 根据ID查询调整单
     * 
     * @param adjustmentId 调整单ID
     * @return 调整单
     */
    TblBudgetAdjustment getAdjustmentById(String adjustmentId);

    /**
     * 根据ID查询调整单(包含明细)
     * 
     * @param adjustmentId 调整单ID
     * @return 调整单
     */
    TblBudgetAdjustment getAdjustmentWithDetails(String adjustmentId);

    /**
     * 删除调整单
     * 
     * @param adjustmentId 调整单ID
     */
    void deleteAdjustment(String adjustmentId);

    /**
     * 批量删除调整单
     * 
     * @param adjustmentIds 调整单ID列表
     */
    void batchDeleteAdjustment(List<String> adjustmentIds);

    /**
     * 提交调整单
     * 
     * @param adjustmentId 调整单ID
     */
    void submitAdjustment(String adjustmentId);

    /**
     * 审批调整单
     * 
     * @param adjustmentId 调整单ID
     * @param approved 是否通过
     * @param opinion 审批意见
     */
    void approveAdjustment(String adjustmentId, boolean approved, String opinion);

    /**
     * 执行调整
     * 
     * @param adjustmentId 调整单ID
     */
    void executeAdjustment(String adjustmentId);

    /**
     * 撤销调整单
     * 
     * @param adjustmentId 调整单ID
     */
    void withdrawAdjustment(String adjustmentId);

    /**
     * 查询调整单统计信息
     * 
     * @return 统计信息
     */
    Map<String, Object> getAdjustmentStatistics();
}

