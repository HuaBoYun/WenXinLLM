package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetWorkflow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算工作流Mapper接口
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Mapper
public interface BudgetWorkflowMapper extends BaseMapper<BudgetWorkflow> {

    /**
     * 根据工作流编码查询工作流
     * 
     * @param workflowCode 工作流编码
     * @return 预算工作流
     */
    BudgetWorkflow selectByWorkflowCode(@Param("workflowCode") String workflowCode);

    /**
     * 根据工作流类型查询工作流列表
     * 
     * @param workflowType 工作流类型
     * @return 工作流列表
     */
    List<BudgetWorkflow> selectByWorkflowType(@Param("workflowType") String workflowType);

    /**
     * 查询启用的工作流列表
     * 
     * @return 工作流列表
     */
    List<BudgetWorkflow> selectEnabledWorkflows();

    /**
     * 批量启用/禁用工作流
     *
     * @param workflowIds 工作流ID列表
     * @param isEnabled 是否启用
     * @return 更新数量
     */
    int batchUpdateEnabled(@Param("workflowIds") List<String> workflowIds, @Param("isEnabled") Boolean isEnabled);

    /**
     * 切换工作流启用状态
     *
     * @param workflowId 工作流ID
     * @param status 状态
     * @return 更新数量
     */
    int toggleWorkflow(@Param("workflowId") String workflowId, @Param("status") int status);

    /**
     * 批量切换工作流启用状态
     *
     * @param workflowIds 工作流ID列表
     * @param status 状态
     * @return 更新数量
     */
    int batchToggleWorkflows(@Param("workflowIds") List<String> workflowIds, @Param("status") int status);

    /**
     * 批量删除工作流
     *
     * @param workflowIds 工作流ID列表
     * @return 删除数量
     */
    int batchDeleteByIds(@Param("workflowIds") List<String> workflowIds);
}

