package com.global.treasurer.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.global.treasurer.entity.DataAnalysisTask;

import java.util.Map;

/**
 * 数据分析任务Service接口
 *
 * @author 华博云开发团队
 * @since 2025-01-12
 */
public interface IDataAnalysisTaskService extends IService<DataAnalysisTask> {

    /**
     * 分页查询数据分析任务
     * @param page 分页对象
     * @param params 查询参数
     * @return 分页结果
     */
    IPage<DataAnalysisTask> selectPage(IPage<DataAnalysisTask> page, Map<String, Object> params);

    /**
     * 执行数据分析任务
     * @param taskId 任务ID
     * @param executeUser 执行人ID
     * @return 是否成功
     */
    boolean executeTask(Long taskId, Long executeUser);

    /**
     * 取消数据分析任务
     * @param taskId 任务ID
     * @param updateUser 更新人ID
     * @return 是否成功
     */
    boolean cancelTask(Long taskId, Long updateUser);

    /**
     * 重试失败任务
     * @param taskId 任务ID
     * @param executeUser 执行人ID
     * @return 是否成功
     */
    boolean retryTask(Long taskId, Long executeUser);
}
