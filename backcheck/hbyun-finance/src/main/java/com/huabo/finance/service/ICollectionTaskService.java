package com.huabo.finance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.finance.entity.CollectionTask;

/**
 * 采集任务Service接口
 *
 * @author 华博云开发团队
 * @since 2025-10-23
 */
public interface ICollectionTaskService extends IService<CollectionTask> {

    /**
     * 启动采集任务
     *
     * @param taskName 任务名称
     * @param planId 采集方案ID
     * @param dataSourceId 数据源ID
     * @param collectionType 采集类型
     * @param remark 备注
     * @param createUser 创建人
     * @return 任务ID
     */
    JsonBean startCollection(String taskName, String planId, String dataSourceId, 
                            String collectionType, String remark, String createUser);

    /**
     * 暂停采集任务
     *
     * @param taskId 任务ID
     * @return 操作结果
     */
    JsonBean pauseCollection(String taskId);

    /**
     * 恢复采集任务
     *
     * @param taskId 任务ID
     * @return 操作结果
     */
    JsonBean resumeCollection(String taskId);

    /**
     * 取消采集任务
     *
     * @param taskId 任务ID
     * @return 操作结果
     */
    JsonBean cancelCollection(String taskId);

    /**
     * 删除采集任务
     *
     * @param taskId 任务ID
     * @return 操作结果
     */
    JsonBean deleteCollection(String taskId);

    /**
     * 获取任务状态
     *
     * @param taskId 任务ID
     * @return 任务信息
     */
    JsonBean getTaskStatus(String taskId);

    /**
     * 执行采集任务
     *
     * @param taskId 任务ID
     */
    void executeCollection(String taskId);
}

