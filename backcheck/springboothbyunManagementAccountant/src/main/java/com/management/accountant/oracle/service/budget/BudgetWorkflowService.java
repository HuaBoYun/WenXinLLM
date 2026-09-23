package com.management.accountant.oracle.service.budget;

import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.oracle.entity.budget.BudgetWorkflow;

import java.util.List;

/**
 * 预算工作流Service接口
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
public interface BudgetWorkflowService extends IService<BudgetWorkflow> {

    /**
     * 根据工作流编码查询工作流
     * 
     * @param workflowCode 工作流编码
     * @return 预算工作流
     */
    BudgetWorkflow getByWorkflowCode(String workflowCode);

    /**
     * 根据工作流类型查询工作流列表
     * 
     * @param workflowType 工作流类型
     * @return 工作流列表
     */
    List<BudgetWorkflow> getByWorkflowType(String workflowType);

    /**
     * 查询启用的工作流列表
     * 
     * @return 工作流列表
     */
    List<BudgetWorkflow> getEnabledWorkflows();

    /**
     * 创建预算工作流
     * 
     * @param workflow 预算工作流
     * @return 是否成功
     */
    boolean createWorkflow(BudgetWorkflow workflow);

    /**
     * 更新预算工作流
     * 
     * @param workflow 预算工作流
     * @return 是否成功
     */
    boolean updateWorkflow(BudgetWorkflow workflow);

    /**
     * 启用/禁用工作流
     * 
     * @param workflowId 工作流ID
     * @param enabled 是否启用
     * @return 是否成功
     */
    boolean toggleWorkflow(String workflowId, boolean enabled);

    /**
     * 批量启用/禁用工作流
     * 
     * @param workflowIds 工作流ID列表
     * @param enabled 是否启用
     * @return 是否成功
     */
    boolean batchToggleWorkflows(List<String> workflowIds, boolean enabled);

    /**
     * 批量删除工作流
     * 
     * @param workflowIds 工作流ID列表
     * @return 是否成功
     */
    boolean batchDeleteWorkflows(List<String> workflowIds);

    /**
     * 启动工作流实例
     * 
     * @param workflowId 工作流ID
     * @param businessId 业务ID
     * @return 工作流实例ID
     */
    String startWorkflowInstance(String workflowId, String businessId);

    /**
     * 分页查询工作流列表
     * 
     * @param pageNum 页码
     * @param pageSize 每页数量
     * @param workflow 查询条件
     * @return 工作流列表
     */
    com.baomidou.mybatisplus.extension.plugins.pagination.Page<BudgetWorkflow> pageQuery(
        int pageNum, int pageSize, BudgetWorkflow workflow);
}

