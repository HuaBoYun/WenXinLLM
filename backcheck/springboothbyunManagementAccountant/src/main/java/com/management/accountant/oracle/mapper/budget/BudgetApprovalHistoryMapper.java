package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetApprovalHistory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算审批历史Mapper接口
 *
 * @author AI Agent
 * @date 2026-01-30
 */
@Mapper
public interface BudgetApprovalHistoryMapper extends BaseMapper<BudgetApprovalHistory> {

    /**
     * 根据流程ID查询审批历史
     *
     * @param flowId 流程ID
     * @return 审批历史列表
     */
    List<BudgetApprovalHistory> selectByFlowId(@Param("flowId") String flowId);

    /**
     * 根据预算ID查询审批历史
     *
     * @param budgetId 预算ID
     * @return 审批历史列表
     */
    List<BudgetApprovalHistory> selectByBudgetId(@Param("budgetId") String budgetId);

    /**
     * 根据审批人ID查询审批历史
     *
     * @param approverId 审批人ID
     * @return 审批历史列表
     */
    List<BudgetApprovalHistory> selectByApproverId(@Param("approverId") String approverId);

    /**
     * 根据审批动作查询审批历史
     *
     * @param action 审批动作
     * @return 审批历史列表
     */
    List<BudgetApprovalHistory> selectByAction(@Param("action") String action);

    /**
     * 查询超时的审批记录
     *
     * @return 超时审批记录列表
     */
    List<BudgetApprovalHistory> selectTimeoutRecords();

    /**
     * 统计审批历史数量
     *
     * @param companyId 公司ID
     * @return 审批历史数量
     */
    int countByCompanyId(@Param("companyId") String companyId);
}

