package com.financial.sharing.dataCollection.service;

import com.financial.sharing.dataCollection.entity.TblCollectionTask;

/**
 * 归集任务调度Service接口
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
public interface CollectionTaskScheduleService {

    /**
     * 启动任务调度
     *
     * @param task 归集任务
     */
    void scheduleTask(TblCollectionTask task);

    /**
     * 取消任务调度
     *
     * @param taskId 任务ID
     */
    void cancelTask(String taskId);

    /**
     * 重新调度任务
     *
     * @param task 归集任务
     */
    void rescheduleTask(TblCollectionTask task);

    /**
     * 检查任务是否正在调度中
     *
     * @param taskId 任务ID
     * @return 是否正在调度中
     */
    boolean isScheduled(String taskId);

    /**
     * 初始化所有启用的定时任务
     */
    void initScheduledTasks();
}

