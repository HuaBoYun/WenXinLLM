package com.financial.sharing.oracle.service;

import com.financial.sharing.oracle.entity.ExportTaskEntity;
import com.financial.sharing.oracle.dto.ExportProgressDTO;
import com.financial.sharing.oracle.dto.ExportTaskStatisticsDTO;
import com.hbfk.util.JsonBean;

import java.util.List;
import java.util.Map;

/**
 * 导出任务进度跟踪管理器
 *
 * @author 赵工
 * @since 2025-12-07
 */
public interface ExportTaskManager {

    /**
     * 注册任务进度监听器
     *
     * @param taskId 任务ID
     * @param listener 进度监听器
     */
    void registerProgressListener(Long taskId, ExportProgressListener listener);

    /**
     * 取消注册进度监听器
     *
     * @param taskId 任务ID
     */
    void unregisterProgressListener(Long taskId);

    /**
     * 更新任务进度
     *
     * @param taskId 任务ID
     * @param progress 进度信息
     */
    void updateProgress(Long taskId, ExportProgressDTO progress);

    /**
     * 获取任务实时进度
     *
     * @param taskId 任务ID
     * @return 进度信息
     */
    JsonBean getRealTimeProgress(Long taskId);

    /**
     * 批量更新任务进度
     *
     * @param progressList 进度列表
     */
    void batchUpdateProgress(List<ExportProgressDTO> progressList);

    /**
     * 获取所有运行中任务的进度
     *
     * @return 进度列表
     */
    JsonBean getAllRunningTasksProgress();

    /**
     * 暂停任务进度更新
     *
     * @param taskId 任务ID
     */
    void pauseProgressUpdate(Long taskId);

    /**
     * 恢复任务进度更新
     *
     * @param taskId 任务ID
     */
    void resumeProgressUpdate(Long taskId);

    /**
     * 设置进度更新频率
     *
     * @param taskId 任务ID
     * @param frequency 更新频率(毫秒)
     */
    void setProgressUpdateFrequency(Long taskId, Long frequency);

    /**
     * 获取任务执行时间线
     *
     * @param taskId 任务ID
     * @return 时间线数据
     */
    JsonBean getTaskTimeline(Long taskId);

    /**
     * 记录任务里程碑
     *
     * @param taskId 任务ID
     * @param milestone 里程碑信息
     */
    void recordTaskMilestone(Long taskId, Map<String, Object> milestone);

    /**
     * 获取任务统计信息
     *
     * @param timeRange 时间范围
     * @return 统计信息
     */
    JsonBean getTaskStatistics(String timeRange);

    /**
     * 获取任务性能指标
     *
     * @param taskId 任务ID
     * @return 性能指标
     */
    JsonBean getTaskPerformanceMetrics(Long taskId);

    /**
     * 分析任务执行模式
     *
     * @param taskId 任务ID
     * @return 执行模式分析
     */
    JsonBean analyzeTaskExecutionPattern(Long taskId);

    /**
     * 预测任务完成时间
     *
     * @param taskId 任务ID
     * @return 预测结果
     */
    JsonBean predictTaskCompletionTime(Long taskId);

    /**
     * 获取系统资源使用情况
     *
     * @return 资源使用情况
     */
    JsonBean getSystemResourceUsage();

    /**
     * 优化任务执行策略
     *
     * @param taskId 任务ID
     * @return 优化建议
     */
    JsonBean optimizeTaskExecutionStrategy(Long taskId);

    /**
     * 设置任务优先级
     *
     * @param taskId 任务ID
     * @param priority 优先级
     */
    void setTaskPriority(Long taskId, Integer priority);

    /**
     * 获取任务队列状态
     *
     * @return 队列状态
     */
    JsonBean getTaskQueueStatus();

    /**
     * 清理已完成的任务监听器
     */
    void cleanupCompletedTaskListeners();

    /**
     * 导出进度监听器接口
     */
    interface ExportProgressListener {
        /**
         * 进度更新回调
         *
         * @param taskId 任务ID
         * @param progress 进度信息
         */
        void onProgressUpdate(Long taskId, ExportProgressDTO progress);

        /**
         * 任务开始回调
         *
         * @param taskId 任务ID
         */
        void onTaskStarted(Long taskId);

        /**
         * 任务完成回调
         *
         * @param taskId 任务ID
         * @param success 是否成功
         * @param message 完成消息
         */
        void onTaskCompleted(Long taskId, boolean success, String message);

        /**
         * 任务取消回调
         *
         * @param taskId 任务ID
         * @param reason 取消原因
         */
        void onTaskCancelled(Long taskId, String reason);

        /**
         * 任务错误回调
         *
         * @param taskId 任务ID
         * @param error 错误信息
         */
        void onTaskError(Long taskId, Exception error);
    }
}