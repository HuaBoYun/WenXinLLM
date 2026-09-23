package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetApproval;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算审批Mapper接口
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Mapper
public interface BudgetApprovalMapper extends BaseMapper<BudgetApproval> {

    /**
     * 根据审批编码查询审批
     * 
     * @param approvalCode 审批编码
     * @return 预算审批
     */
    BudgetApproval selectByApprovalCode(@Param("approvalCode") String approvalCode);

    /**
     * 查询待审批列表
     * 
     * @param approverId 审批人ID
     * @return 审批列表
     */
    List<BudgetApproval> selectPendingApprovals(@Param("approverId") String approverId);

    /**
     * 查询我提交的审批列表
     * 
     * @param submitterId 提交人ID
     * @return 审批列表
     */
    List<BudgetApproval> selectMySubmittedApprovals(@Param("submitterId") String submitterId);

    /**
     * 更新审批状态
     * 
     * @param approvalId 审批ID
     * @param approvalStatus 审批状态
     * @return 更新数量
     */
    int updateApprovalStatus(@Param("approvalId") String approvalId, @Param("approvalStatus") String approvalStatus);

    /**
     * 根据预算ID查询审批记录
     * 
     * @param budgetId 预算ID
     * @return 审批列表
     */
    List<BudgetApproval> selectByBudgetId(@Param("budgetId") String budgetId);

    /**
     * 根据任务ID查询审批记录
     * 
     * @param taskId 任务ID
     * @return 审批列表
     */
    List<BudgetApproval> selectByTaskId(@Param("taskId") String taskId);
}

