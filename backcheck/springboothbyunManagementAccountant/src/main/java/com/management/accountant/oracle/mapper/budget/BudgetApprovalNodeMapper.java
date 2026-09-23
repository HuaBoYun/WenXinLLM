package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetApprovalNode;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算审批节点Mapper
 * 
 * @author AI Agent
 * @date 2026-01-29
 */
@Mapper
public interface BudgetApprovalNodeMapper extends BaseMapper<BudgetApprovalNode> {

    /**
     * 根据流程ID查询节点列表
     */
    List<BudgetApprovalNode> selectByFlowId(@Param("flowId") String flowId);

    /**
     * 根据节点编码查询节点
     */
    BudgetApprovalNode selectByNodeCode(@Param("nodeCode") String nodeCode);

    /**
     * 根据状态查询节点列表
     */
    List<BudgetApprovalNode> selectByStatus(@Param("status") String status);

    /**
     * 查询流程的下一个节点
     */
    BudgetApprovalNode selectNextNode(@Param("flowId") String flowId, @Param("currentOrderNum") Integer currentOrderNum);

    /**
     * 批量插入节点
     */
    int batchInsert(@Param("nodes") List<BudgetApprovalNode> nodes);

    /**
     * 批量更新节点状态
     */
    int batchUpdateStatus(@Param("nodeIds") List<String> nodeIds, @Param("status") String status);

    /**
     * 删除流程的所有节点
     */
    int deleteByFlowId(@Param("flowId") String flowId);
}

