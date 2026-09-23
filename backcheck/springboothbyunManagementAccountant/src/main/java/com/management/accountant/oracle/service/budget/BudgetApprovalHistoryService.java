package com.management.accountant.oracle.service.budget;

import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.oracle.entity.budget.BudgetApprovalHistory;

import java.util.List;
import java.util.Map;

/**
 * 预算审批历史Service接口
 *
 * @author AI Agent
 * @date 2026-01-30
 */
public interface BudgetApprovalHistoryService extends IService<BudgetApprovalHistory> {

    /**
     * 根据流程ID查询审批历史
     *
     * @param flowId 流程ID
     * @return 审批历史列表
     */
    List<BudgetApprovalHistory> getByFlowId(String flowId);

    /**
     * 根据预算ID查询审批历史
     *
     * @param budgetId 预算ID
     * @return 审批历史列表
     */
    List<BudgetApprovalHistory> getByBudgetId(String budgetId);

    /**
     * 根据审批人ID查询审批历史
     *
     * @param approverId 审批人ID
     * @return 审批历史列表
     */
    List<BudgetApprovalHistory> getByApproverId(String approverId);

    /**
     * 创建审批历史记录
     *
     * @param history 审批历史
     * @return 是否成功
     */
    boolean createHistory(BudgetApprovalHistory history);

    /**
     * 查询超时的审批记录
     *
     * @return 超时审批记录列表
     */
    List<BudgetApprovalHistory> getTimeoutRecords();

    /**
     * 统计审批历史
     *
     * @param companyId 公司ID
     * @return 统计结果
     */
    Map<String, Object> countHistoryStats(String companyId);
}

