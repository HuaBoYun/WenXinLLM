package com.management.accountant.service;

import com.management.accountant.oracle.entity.budget.BudgetApproval;
import com.management.accountant.util.PageResult;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * 预算审批流程Service接口
 * 
 * @description 预算审批流程业务接口
 * @author AI Assistant
 * @date 2025-01-04
 */
public interface BudgetApprovalService {

    /**
     * 创建审批流程
     * 
     * @param approval 审批对象
     * @return 创建后的审批对象
     */
    BudgetApproval create(BudgetApproval approval);

    /**
     * 根据ID查询审批
     * 
     * @param approvalId 审批ID
     * @return 审批对象
     */
    BudgetApproval getById(String approvalId);

    /**
     * 分页查询审批列表
     * 
     * @param params 查询参数
     * @return 分页结果
     */
    PageResult<LinkedHashMap<String, Object>> getPage(Map<String, Object> params);

    /**
     * 审批通过
     * 
     * @param approvalId 审批ID
     * @param comment 审批意见
     */
    void approve(String approvalId, String comment);

    /**
     * 审批拒绝
     * 
     * @param approvalId 审批ID
     * @param reason 拒绝原因
     */
    void reject(String approvalId, String reason);

    /**
     * 撤回审批
     * 
     * @param approvalId 审批ID
     */
    void withdraw(String approvalId);

    /**
     * 批量审批通过
     * 
     * @param ids 审批ID列表
     * @param comment 审批意见
     */
    void batchApprove(List<String> ids, String comment);

    /**
     * 批量审批拒绝
     * 
     * @param ids 审批ID列表
     * @param reason 拒绝原因
     */
    void batchReject(List<String> ids, String reason);

    /**
     * 获取审批统计信息
     * 
     * @return 统计信息
     */
    Map<String, Object> getStatistics();

    /**
     * 获取我的待审批列表
     * 
     * @param userId 用户ID
     * @return 待审批列表
     */
    List<BudgetApproval> getMyPendingApprovals(String userId);

    /**
     * 获取我的已审批列表
     * 
     * @param userId 用户ID
     * @return 已审批列表
     */
    List<BudgetApproval> getMyApprovedList(String userId);

    /**
     * 更新审批流程
     *
     * @param approval 审批对象
     * @return 更新后的审批对象
     */
    BudgetApproval update(BudgetApproval approval);

    /**
     * 删除审批流程（逻辑删除）
     *
     * @param approvalId 审批ID
     */
    void delete(String approvalId);
}

