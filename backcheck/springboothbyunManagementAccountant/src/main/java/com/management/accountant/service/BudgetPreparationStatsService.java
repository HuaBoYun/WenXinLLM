package com.management.accountant.service;

import com.management.accountant.vo.result.BudgetPreparationProgressVO;
import com.management.accountant.vo.result.BudgetPreparationStatsVO;
import com.management.accountant.vo.result.BudgetPreparationTodoVO;

import java.util.List;
import java.util.Map;

/**
 * 预算编制统计Service接口
 * 
 * @description 预算编制统计数据服务
 * @author AI Assistant
 * @date 2025-01-30
 */
public interface BudgetPreparationStatsService {

    /**
     * 获取预算编制统计数据
     * 
     * @return 统计数据
     */
    BudgetPreparationStatsVO getPreparationStats();

    /**
     * 获取待办事项列表
     * 
     * @param limit 限制数量
     * @return 待办列表
     */
    List<BudgetPreparationTodoVO> getTodoList(Integer limit);

    /**
     * 获取编制进度汇总
     */
    List<BudgetPreparationProgressVO> getProgressSummary();

    /**
     * 获取最近活动列表
     */
    List<Map<String, Object>> getActivities(Integer limit);

    /**
     * 获取图表数据
     */
    Map<String, Object> getChartData(String period);

    /**
     * 延期任务
     *
     * @param taskId  任务ID
     * @param dueDate 新截止日期（yyyy-MM-dd）
     */
    void deferTask(String taskId, String dueDate);

    /**
     * 委派任务
     *
     * @param taskId   任务ID
     * @param assignee 委派给的人员名称
     */
    void delegateTask(String taskId, String assignee);
}

