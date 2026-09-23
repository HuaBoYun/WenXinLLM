package com.financial.sharing.dataCollection.service;

import com.financial.sharing.util.MyJsonBean;

/**
 * 归集任务执行Service接口
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
public interface CollectionTaskExecutionService {

    /**
     * 执行归集任务（异步）
     *
     * @param taskId 任务ID
     * @param orgId 组织ID
     */
    void executeTaskAsync(String taskId, String orgId);

    /**
     * 停止归集任务
     *
     * @param taskId 任务ID
     * @param orgId 组织ID
     * @return 操作结果
     */
    MyJsonBean stopTaskExecution(String taskId, String orgId);

    /**
     * 获取任务执行进度
     *
     * @param taskId 任务ID
     * @param orgId 组织ID
     * @return 执行进度
     */
    MyJsonBean getTaskProgress(String taskId, String orgId);
}

