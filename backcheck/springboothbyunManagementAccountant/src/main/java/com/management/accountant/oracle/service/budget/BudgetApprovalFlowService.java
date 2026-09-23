package com.management.accountant.oracle.service.budget;

import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.oracle.entity.budget.BudgetApprovalFlow;
import com.management.accountant.vo.param.BudgetApprovalFlowDTO;

import java.util.List;
import java.util.Map;

/**
 * 预算审批流程Service
 * 
 * @author AI Agent
 * @date 2026-01-29
 */
public interface BudgetApprovalFlowService extends IService<BudgetApprovalFlow> {

    /**
     * 创建审批流程
     */
    boolean createFlow(BudgetApprovalFlowDTO dto);

    /**
     * 审批操作
     */
    boolean approve(String flowId, String action, String comment);

    /**
     * 拒绝操作
     */
    boolean reject(String flowId, String reason);

    /**
     * 批量审批
     */
    boolean batchApprove(List<String> flowIds, String action);

    /**
     * 获取审批详情
     */
    BudgetApprovalFlowDTO getFlowDetail(String flowId);

    /**
     * 根据预算ID查询流程列表
     */
    List<BudgetApprovalFlow> listByBudgetId(String budgetId);

    /**
     * 根据发起人查询流程列表
     */
    List<BudgetApprovalFlow> listByInitiator(String initiator);

    /**
     * 查询待审批流程
     */
    List<BudgetApprovalFlow> listPendingFlows(String companyId);

    /**
     * 查询超时流程
     */
    List<BudgetApprovalFlow> listTimeoutFlows();

    /**
     * 统计流程数量
     */
    Map<String, Integer> countFlowStats(String companyId);

    /**
     * 流转到下一节点
     */
    boolean moveToNextNode(String flowId);

    /**
     * 取消流程
     */
    boolean cancelFlow(String flowId, String reason);
}

