package com.financial.sharing.service;

import com.financial.sharing.util.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 调度服务接口
 */
public interface FinanceScheduledService {

    /**
     * 获取调度任务列表
     */
    List<Map<String, Object>> getScheduledList(Map<String, Object> params);

    /**
     * 获取调度任务详情
     */
    Map<String, Object> getScheduledDetail(String scheduledId);

    /**
     * 创建调度任务
     */
    int createScheduled(Map<String, Object> scheduled);

    /**
     * 更新调度任务
     */
    int updateScheduled(Map<String, Object> scheduled);

    /**
     * 删除调度任务
     */
    int deleteScheduled(String scheduledId);

    /**
     * 启动调度任务
     */
    int startScheduled(String scheduledId);

    /**
     * 停止调度任务
     */
    int stopScheduled(String scheduledId);

    /**
     * 获取调度任务执行历史
     */
    List<Map<String, Object>> getScheduledHistory(Map<String, Object> params);

    // ==================== Controller 需要的方法 ====================

    /**
     * 获取定时任务列表（分页）
     */
    PageResult<Map<String, Object>> getScheduledTaskList(Map<String, Object> params);

    /**
     * 删除定时任务
     */
    boolean deleteScheduledTask(Map<String, Object> params);

    /**
     * 修改定时任务状态
     */
    boolean changeScheduledTaskStatus(Map<String, Object> data);

    /**
     * 保存定时任务
     */
    Map<String, Object> saveScheduledTask(Map<String, Object> data);

    /**
     * 修改定时任务
     */
    Map<String, Object> updateScheduledTask(Map<String, Object> data);

    /**
     * 获取定时任务详情
     */
    Map<String, Object> getScheduledTaskDetail(Map<String, Object> params);
}