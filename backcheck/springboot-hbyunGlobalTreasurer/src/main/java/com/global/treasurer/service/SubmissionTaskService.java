package com.global.treasurer.service;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblSubmissionTask;

import java.util.List;
import java.util.Map;

/**
 * 报送任务服务接口
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
public interface SubmissionTaskService {

    /**
     * 分页查询报送任务列表
     *
     * @param params 查询参数
     * @return 分页结果
     */
    PageInfo<TblSubmissionTask> getTaskList(Map<String, Object> params);

    /**
     * 根据ID查询报送任务详情
     *
     * @param taskId 任务ID
     * @return 报送任务
     */
    TblSubmissionTask getTaskById(String taskId);

    /**
     * 保存报送任务（新增或更新）
     *
     * @param task 报送任务
     * @return 保存后的报送任务
     */
    TblSubmissionTask saveTask(TblSubmissionTask task);

    /**
     * 删除报送任务
     *
     * @param taskId 任务ID
     */
    void deleteTask(String taskId);

    /**
     * 批量删除报送任务
     *
     * @param taskIds 任务ID列表
     */
    void batchDeleteTasks(List<String> taskIds);

    /**
     * 执行报送任务
     *
     * @param taskId 任务ID
     * @return 执行后的任务
     */
    TblSubmissionTask executeTask(String taskId);

    /**
     * 暂停报送任务
     *
     * @param taskId 任务ID
     * @return 暂停后的任务
     */
    TblSubmissionTask pauseTask(String taskId);

    /**
     * 恢复报送任务
     *
     * @param taskId 任务ID
     * @return 恢复后的任务
     */
    TblSubmissionTask resumeTask(String taskId);

    /**
     * 取消报送任务
     *
     * @param taskId 任务ID
     * @param cancelReason 取消原因
     * @return 取消后的任务
     */
    TblSubmissionTask cancelTask(String taskId, String cancelReason);

    /**
     * 重试报送任务
     *
     * @param taskId 任务ID
     * @return 重试后的任务
     */
    TblSubmissionTask retryTask(String taskId);

    /**
     * 获取待处理的任务列表
     *
     * @return 待处理任务列表
     */
    List<TblSubmissionTask> getPendingTasks();

    /**
     * 获取已逾期的任务列表
     *
     * @return 已逾期任务列表
     */
    List<TblSubmissionTask> getOverdueTasks();

    /**
     * 获取即将到期的任务列表
     *
     * @param days 天数
     * @return 即将到期任务列表
     */
    List<TblSubmissionTask> getDueSoonTasks(Integer days);
}

