package com.management.accountant.oracle.service.advanced;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.oracle.entity.advanced.AutomationWorkflow;

import java.util.List;
import java.util.Map;

public interface AutomationWorkflowService {
    List<AutomationWorkflow> selectList(Map<String, Object> params);
    Page<AutomationWorkflow> selectPage(Map<String, Object> params, Integer pageNum, Integer pageSize);
    AutomationWorkflow selectById(String workflowId);
    boolean insert(AutomationWorkflow workflow);
    boolean update(AutomationWorkflow workflow);
    boolean deleteById(String workflowId);
    boolean runWorkflow(String workflowId);
    boolean stopWorkflow(String workflowId);
    boolean copyWorkflow(String workflowId);
    /** 统计数据：totalWorkflows/activeWorkflows/totalExecutions/successRate */
    Map<String, Object> getStats();
    /** 按工作流类型分组统计数量 */
    Map<String, Long> countByType();
}
