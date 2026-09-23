package com.management.accountant.oracle.service.budget;

import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.oracle.entity.budget.BudgetApproval;

import java.util.List;

/**
 * 预算审批Service接口
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
public interface BudgetApprovalService extends IService<BudgetApproval> {

    /**
     * 根据审批编码查询审批
     * 
     * @param approvalCode 审批编码
     * @return 预算审批
     */
    BudgetApproval getByApprovalCode(String approvalCode);

    /**
     * 查询待审批列表
     * 
     * @param approverId 审批人ID
     * @return 审批列表
     */
    List<BudgetApproval> getPendingApprovals(String approverId);

    /**
     * 查询我提交的审批列表
     * 
     * @param submitterId 提交人ID
     * @return 审批列表
     */
    List<BudgetApproval> getMySubmittedApprovals(String submitterId);

    /**
     * 提交审批
     * 
     * @param approval 审批信息
     * @return 是否成功
     */
    boolean submitApproval(BudgetApproval approval);

    /**
     * 审批通过
     * 
     * @param approvalId 审批ID
     * @param approvalComment 审批意见
     * @return 是否成功
     */
    boolean approve(String approvalId, String approvalComment);

    /**
     * 审批拒绝
     * 
     * @param approvalId 审批ID
     * @param rejectReason 拒绝原因
     * @return 是否成功
     */
    boolean reject(String approvalId, String rejectReason);

    /**
     * 撤回审批
     * 
     * @param approvalId 审批ID
     * @return 是否成功
     */
    boolean withdraw(String approvalId);

    /**
     * 根据预算ID查询审批记录
     * 
     * @param budgetId 预算ID
     * @return 审批列表
     */
    List<BudgetApproval> getByBudgetId(String budgetId);

    /**
     * 根据任务ID查询审批记录
     * 
     * @param taskId 任务ID
     * @return 审批列表
     */
    List<BudgetApproval> getByTaskId(String taskId);

    /**
     * 分页查询审批列表
     * 
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @param approval 查询条件
     * @return 审批列表
     */
    com.baomidou.mybatisplus.extension.plugins.pagination.Page<BudgetApproval> pageQuery(
        int pageNum, int pageSize, BudgetApproval approval);
}

