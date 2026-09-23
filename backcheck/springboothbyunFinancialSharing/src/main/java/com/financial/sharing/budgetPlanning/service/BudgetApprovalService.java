package com.financial.sharing.budgetPlanning.service;

import com.financial.sharing.budgetPlanning.dto.BudgetApprovalQueryParam;
import com.financial.sharing.budgetPlanning.entity.TblBudgetApproval;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

/**
 * 预算数据审批Service接口
 * 
 * @author hbyun
 * @date 2026-02-02
 */
public interface BudgetApprovalService {

    /**
     * 提交审批
     * 
     * @param dataId 数据ID
     * @param approverIds 审批人ID列表(逗号分隔)
     * @param comment 提交说明
     */
    void submitApproval(String dataId, String approverIds, String comment);

    /**
     * 审批通过
     * 
     * @param approvalId 审批ID
     * @param comment 审批意见
     */
    void approveData(String approvalId, String comment);

    /**
     * 审批驳回
     * 
     * @param approvalId 审批ID
     * @param comment 驳回原因
     */
    void rejectData(String approvalId, String comment);

    /**
     * 撤销审批
     * 
     * @param dataId 数据ID
     * @param comment 撤销原因
     */
    void cancelApproval(String dataId, String comment);

    /**
     * 查询审批记录列表(分页)
     * 
     * @param param 查询参数
     * @return 分页结果
     */
    PageInfo<TblBudgetApproval> getApprovalList(BudgetApprovalQueryParam param);

    /**
     * 根据数据ID查询审批记录
     * 
     * @param dataId 数据ID
     * @return 审批记录列表
     */
    List<TblBudgetApproval> getApprovalHistory(String dataId);

    /**
     * 查询我的待审批列表(分页)
     * 
     * @param param 查询参数
     * @return 分页结果
     */
    PageInfo<TblBudgetApproval> getMyPendingList(BudgetApprovalQueryParam param);

    /**
     * 查询我的已审批列表(分页)
     * 
     * @param param 查询参数
     * @return 分页结果
     */
    PageInfo<TblBudgetApproval> getMyApprovedList(BudgetApprovalQueryParam param);

    /**
     * 批量审批通过
     * 
     * @param approvalIds 审批ID列表
     * @param comment 审批意见
     */
    void batchApprove(List<String> approvalIds, String comment);

    /**
     * 批量审批驳回
     * 
     * @param approvalIds 审批ID列表
     * @param comment 驳回原因
     */
    void batchReject(List<String> approvalIds, String comment);

    /**
     * 查询审批统计信息
     * 
     * @return 统计信息
     */
    Map<String, Object> getApprovalStatistics();
}

