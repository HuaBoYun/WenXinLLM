package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetApprovalFlow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算审批流程Mapper
 * 
 * @author AI Agent
 * @date 2026-01-29
 */
@Mapper
public interface BudgetApprovalFlowMapper extends BaseMapper<BudgetApprovalFlow> {

    /**
     * 根据流程编码查询流程
     */
    BudgetApprovalFlow selectByFlowCode(@Param("flowCode") String flowCode);

    /**
     * 根据预算ID查询流程列表
     */
    List<BudgetApprovalFlow> selectByBudgetId(@Param("budgetId") String budgetId);

    /**
     * 根据发起人查询流程列表
     */
    List<BudgetApprovalFlow> selectByInitiator(@Param("initiator") String initiator);

    /**
     * 根据状态查询流程列表
     */
    List<BudgetApprovalFlow> selectByStatus(@Param("status") String status);

    /**
     * 根据流程类型查询流程列表
     */
    List<BudgetApprovalFlow> selectByFlowType(@Param("flowType") String flowType);

    /**
     * 批量更新流程状态
     */
    int batchUpdateStatus(@Param("flowIds") List<String> flowIds, @Param("status") String status);

    /**
     * 查询待审批流程列表
     */
    List<BudgetApprovalFlow> selectPendingFlows(@Param("companyId") String companyId);

    /**
     * 查询超时流程列表
     */
    List<BudgetApprovalFlow> selectTimeoutFlows();

    /**
     * 统计流程数量
     */
    int countByStatus(@Param("status") String status, @Param("companyId") String companyId);
}

