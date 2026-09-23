package com.financial.sharing.dataCollection.service;

import com.financial.sharing.dataCollection.dto.CollectionTaskQueryParam;
import com.financial.sharing.dataCollection.entity.TblCollectionTask;
import com.financial.sharing.util.MyJsonBean;

/**
 * 归集任务Service接口
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
public interface CollectionTaskService {

    /**
     * 分页查询归集任务
     *
     * @param param 查询参数
     * @param orgId 组织ID
     * @return 分页结果 (data.records / data.total)
     */
    MyJsonBean queryPage(CollectionTaskQueryParam param, String orgId);

    /**
     * 根据ID查询归集任务
     *
     * @param taskId 任务ID
     * @param orgId 组织ID
     * @return 归集任务
     */
    TblCollectionTask queryById(String taskId, String orgId);

    /**
     * 保存归集任务
     *
     * @param task 归集任务
     * @param orgId 组织ID
     * @param userId 用户ID
     * @return 操作结果
     */
    MyJsonBean saveCollectionTask(TblCollectionTask task, String orgId, String userId);

    /**
     * 删除归集任务
     *
     * @param taskId 任务ID
     * @param orgId 组织ID
     * @return 操作结果
     */
    MyJsonBean deleteCollectionTask(String taskId, String orgId);

    /**
     * 启用/禁用归集任务
     *
     * @param taskId 任务ID
     * @param isEnabled 是否启用
     * @param orgId 组织ID
     * @param userId 用户ID
     * @return 操作结果
     */
    MyJsonBean toggleEnabled(String taskId, String isEnabled, String orgId, String userId);

    /**
     * 立即执行归集任务
     *
     * @param taskId 任务ID
     * @param orgId 组织ID
     * @return 操作结果
     */
    MyJsonBean executeTask(String taskId, String orgId);

    /**
     * 停止归集任务
     *
     * @param taskId 任务ID
     * @param orgId 组织ID
     * @return 操作结果
     */
    MyJsonBean stopTask(String taskId, String orgId);

    /**
     * 计算下次执行时间
     *
     * @param task 归集任务
     * @return 下次执行时间
     */
    MyJsonBean calculateNextExecuteTime(TblCollectionTask task);
}

