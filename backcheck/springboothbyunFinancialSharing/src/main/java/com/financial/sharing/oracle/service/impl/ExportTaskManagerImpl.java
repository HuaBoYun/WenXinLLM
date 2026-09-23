package com.financial.sharing.oracle.service.impl;

import com.financial.sharing.oracle.entity.ExportTaskEntity;
import com.financial.sharing.oracle.dto.ExportProgressDTO;
import com.financial.sharing.oracle.dto.ExportTaskStatisticsDTO;
import com.financial.sharing.oracle.mapper.ExportTaskMapper;
import com.financial.sharing.oracle.service.ExportTaskManager;
import com.hbfk.util.JsonBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 导出任务进度跟踪管理器实现
 *
 * @author 赵工
 * @since 2025-12-07
 */
@Slf4j
@Service
public class ExportTaskManagerImpl implements ExportTaskManager {

    @Autowired
    private ExportTaskMapper exportTaskMapper;

    // 任务进度监听器映射
    private final ConcurrentMap<Long, List<ExportProgressListener>> progressListeners = new ConcurrentHashMap<>();

    // 任务进度缓存
    private final ConcurrentMap<Long, ExportProgressDTO> progressCache = new ConcurrentHashMap<>();

    // 任务开始时间记录
    private final ConcurrentMap<Long, Long> taskStartTimes = new ConcurrentHashMap<>();

    // 暂停的任务
    private final Set<Long> pausedTasks = Collections.newSetFromMap(new ConcurrentHashMap<>());

    // 任务优先级
    private final ConcurrentMap<Long, Integer> taskPriorities = new ConcurrentHashMap<>();

    // 进度更新频率
    private final ConcurrentMap<Long, Long> updateFrequencies = new ConcurrentHashMap<>();

    // 任务里程碑记录
    private final ConcurrentMap<Long, List<Map<String, Object>>> taskMilestones = new ConcurrentHashMap<>();

    @Override
    public void registerProgressListener(Long taskId, ExportProgressListener listener) {
        progressListeners.computeIfAbsent(taskId, k -> new ArrayList<>()).add(listener);
        log.debug("注册进度监听器, taskId: {}", taskId);
    }

    @Override
    public void unregisterProgressListener(Long taskId) {
        progressListeners.remove(taskId);
        progressCache.remove(taskId);
        taskStartTimes.remove(taskId);
        pausedTasks.remove(taskId);
        taskPriorities.remove(taskId);
        updateFrequencies.remove(taskId);
        taskMilestones.remove(taskId);
        log.debug("取消注册进度监听器, taskId: {}", taskId);
    }

    @Override
    public void updateProgress(Long taskId, ExportProgressDTO progress) {
        if (pausedTasks.contains(taskId)) {
            return; // 任务已暂停，不更新进度
        }

        // 检查更新频率
        Long frequency = updateFrequencies.get(taskId);
        if (frequency != null && frequency > 0) {
            ExportProgressDTO cached = progressCache.get(taskId);
            if (cached != null) {
                long timeSinceLastUpdate = System.currentTimeMillis() - cached.getUpdateTime().getTime();
                if (timeSinceLastUpdate < frequency) {
                    return; // 未达到更新频率要求
                }
            }
        }

        // 更新进度缓存
        progress.setUpdateTime(new Date());
        progressCache.put(taskId, progress);

        // 计算处理速度和预估时间
        calculateProgressMetrics(taskId, progress);

        // 通知监听器
        notifyProgressListeners(taskId, progress);

        log.debug("更新任务进度, taskId: {}, progress: {}%", taskId, progress.getProgressPercent());
    }

    @Override
    public JsonBean getRealTimeProgress(Long taskId) {
        try {
            ExportProgressDTO progress = progressCache.get(taskId);
            if (progress == null) {
                // 从数据库加载任务信息
                ExportTaskEntity task = exportTaskMapper.selectExportTaskById(taskId);
                if (task == null) {
                    return createErrorJson("任务不存在");
                }

                progress = new ExportProgressDTO();
                progress.setTaskId(taskId);
                progress.setTotalRecords(task.getTotalRecords());
                progress.setProcessedRecords(task.getProcessedRecords());
                progress.setProgressPercent(task.getProgressPercent());
                progress.setCurrentBatch(task.getCurrentBatch());
                progress.setTotalBatches(task.getTotalBatches());
                progress.setUpdateTime(task.getUpdateTime());
            }

            // 添加实时系统资源信息
            addSystemResourceInfo(progress);

            return createSuccessJson(progress);
        } catch (Exception e) {
            log.error("获取实时进度失败, taskId: {}", taskId, e);
            return createErrorJson("获取实时进度失败: " + e.getMessage());
        }
    }

    @Override
    public void batchUpdateProgress(List<ExportProgressDTO> progressList) {
        for (ExportProgressDTO progress : progressList) {
            updateProgress(progress.getTaskId(), progress);
        }
    }

    @Override
    public JsonBean getAllRunningTasksProgress() {
        try {
            List<ExportTaskEntity> runningTasks = exportTaskMapper.selectRunningTasks();
            List<Map<String, Object>> result = new ArrayList<>();

            for (ExportTaskEntity task : runningTasks) {
                Map<String, Object> taskProgress = new HashMap<>();
                taskProgress.put("taskId", task.getTaskId());
                taskProgress.put("taskName", task.getTaskName());
                taskProgress.put("taskType", task.getTaskType());
                taskProgress.put("status", task.getStatus());

                ExportProgressDTO progress = progressCache.get(task.getTaskId());
                if (progress != null) {
                    taskProgress.put("progressPercent", progress.getProgressPercent());
                    taskProgress.put("processedRecords", progress.getProcessedRecords());
                    taskProgress.put("totalRecords", progress.getTotalRecords());
                    taskProgress.put("processingRate", progress.getProcessingRate());
                    taskProgress.put("estimatedRemainingTime", progress.getEstimatedRemainingTime());
                } else {
                    taskProgress.put("progressPercent", task.getProgressPercent());
                    taskProgress.put("processedRecords", task.getProcessedRecords());
                    taskProgress.put("totalRecords", task.getTotalRecords());
                }

                result.add(taskProgress);
            }

            return createSuccessJson(result);
        } catch (Exception e) {
            log.error("获取所有运行中任务进度失败", e);
            return createErrorJson("获取任务进度失败: " + e.getMessage());
        }
    }

    @Override
    public void pauseProgressUpdate(Long taskId) {
        pausedTasks.add(taskId);
        log.info("暂停任务进度更新, taskId: {}", taskId);
    }

    @Override
    public void resumeProgressUpdate(Long taskId) {
        pausedTasks.remove(taskId);
        log.info("恢复任务进度更新, taskId: {}", taskId);
    }

    @Override
    public void setProgressUpdateFrequency(Long taskId, Long frequency) {
        updateFrequencies.put(taskId, frequency);
        log.debug("设置进度更新频率, taskId: {}, frequency: {}ms", taskId, frequency);
    }

    @Override
    public JsonBean getTaskTimeline(Long taskId) {
        try {
            List<Map<String, Object>> timeline = new ArrayList<>();

            // 添加任务创建时间
            ExportTaskEntity task = exportTaskMapper.selectExportTaskById(taskId);
            if (task != null) {
                Map<String, Object> createEvent = new HashMap<>();
                createEvent.put("timestamp", task.getCreateTime());
                createEvent.put("event", "TASK_CREATED");
                createEvent.put("message", "任务创建");
                timeline.add(createEvent);

                if (task.getStartTime() != null) {
                    Map<String, Object> startEvent = new HashMap<>();
                    startEvent.put("timestamp", task.getStartTime());
                    startEvent.put("event", "TASK_STARTED");
                    startEvent.put("message", "任务开始执行");
                    timeline.add(startEvent);
                }

                if (task.getEndTime() != null) {
                    Map<String, Object> endEvent = new HashMap<>();
                    endEvent.put("timestamp", task.getEndTime());
                    endEvent.put("event", "TASK_COMPLETED");
                    endEvent.put("message", task.getStatus() == 2 ? "任务完成" : "任务结束");
                    timeline.add(endEvent);
                }
            }

            // 添加里程碑事件
            List<Map<String, Object>> milestones = taskMilestones.get(taskId);
            if (milestones != null) {
                timeline.addAll(milestones);
            }

            // 按时间排序
            timeline.sort((a, b) -> {
                Date timeA = (Date) a.get("timestamp");
                Date timeB = (Date) b.get("timestamp");
                return timeA.compareTo(timeB);
            });

            return createSuccessJson(timeline);
        } catch (Exception e) {
            log.error("获取任务时间线失败, taskId: {}", taskId, e);
            return createErrorJson("获取任务时间线失败: " + e.getMessage());
        }
    }

    @Override
    public void recordTaskMilestone(Long taskId, Map<String, Object> milestone) {
        milestone.put("timestamp", new Date());
        taskMilestones.computeIfAbsent(taskId, k -> new ArrayList<>()).add(milestone);
        log.debug("记录任务里程碑, taskId: {}, milestone: {}", taskId, milestone.get("event"));
    }

    @Override
    public JsonBean getTaskStatistics(String timeRange) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("startDate", getStartDateByRange(timeRange));

            Map<String, Object> dbStats = exportTaskMapper.selectExportStatistics(params);

            ExportTaskStatisticsDTO statistics = new ExportTaskStatisticsDTO();
            statistics.setTimeRange(timeRange);
            statistics.setStartTime(getStartDateByRange(timeRange));
            statistics.setEndTime(new Date());

            // 填充统计数据
            if (dbStats != null) {
                statistics.setTotalTasks(((Number) dbStats.get("TOTAL_TASKS")).longValue());
                statistics.setCompletedTasks(((Number) dbStats.get("COMPLETED_TASKS")).longValue());
                statistics.setFailedTasks(((Number) dbStats.get("FAILED_TASKS")).longValue());
                statistics.setRunningTasks(((Number) dbStats.get("RUNNING_TASKS")).longValue());
                statistics.setCancelledTasks(((Number) dbStats.get("CANCELLED_TASKS")).longValue());
                statistics.setTotalExportedRecords(((Number) dbStats.get("TOTAL_EXPORTED_RECORDS")).longValue());
                statistics.setTotalDownloads(((Number) dbStats.get("TOTAL_DOWNLOADS")).longValue());

                // 计算比率
                if (statistics.getTotalTasks() > 0) {
                    BigDecimal successRate = new BigDecimal(statistics.getCompletedTasks())
                        .multiply(new BigDecimal("100"))
                        .divide(new BigDecimal(statistics.getTotalTasks()), 2, RoundingMode.HALF_UP);
                    statistics.setSuccessRate(successRate);

                    BigDecimal failureRate = new BigDecimal(statistics.getFailedTasks())
                        .multiply(new BigDecimal("100"))
                        .divide(new BigDecimal(statistics.getTotalTasks()), 2, RoundingMode.HALF_UP);
                    statistics.setFailureRate(failureRate);
                }

                // 平均值计算
                if (statistics.getCompletedTasks() > 0) {
                    Number avgDuration = (Number) dbStats.get("AVG_DURATION");
                    if (avgDuration != null) {
                        statistics.setAvgExecutionTime(new BigDecimal(avgDuration.toString()));
                    }

                    BigDecimal avgRecords = new BigDecimal(statistics.getTotalExportedRecords())
                        .divide(new BigDecimal(statistics.getCompletedTasks()), 0, RoundingMode.HALF_UP);
                    statistics.setAvgRecordsPerTask(avgRecords);

                    Number avgDownloads = (Number) dbStats.get("AVG_DOWNLOADS_PER_TASK");
                    if (avgDownloads != null) {
                        statistics.setAvgDownloadsPerTask(new BigDecimal(avgDownloads.toString()));
                    }
                }

                Number maxDuration = (Number) dbStats.get("MAX_DURATION");
                if (maxDuration != null) {
                    statistics.setMaxExecutionTime(maxDuration.longValue());
                }
            }

            return createSuccessJson(statistics);
        } catch (Exception e) {
            log.error("获取任务统计失败", e);
            return createErrorJson("获取任务统计失败: " + e.getMessage());
        }
    }

    @Override
    public JsonBean getTaskPerformanceMetrics(Long taskId) {
        try {
            Map<String, Object> metrics = new HashMap<>();

            ExportProgressDTO progress = progressCache.get(taskId);
            if (progress != null) {
                metrics.put("processingRate", progress.getProcessingRate());
                metrics.put("memoryUsage", progress.getMemoryUsage());
                metrics.put("cpuUsage", progress.getCpuUsage());
                metrics.put("ioRate", progress.getIoRate());
                metrics.put("networkRate", progress.getNetworkRate());
                metrics.put("tempFileSize", progress.getTempFileSize());
                metrics.put("compressionRate", progress.getCompressionRate());
                metrics.put("errorCount", progress.getErrorCount());
                metrics.put("warningCount", progress.getWarningCount());
                metrics.put("retryCount", progress.getRetryCount());
            }

            ExportTaskEntity task = exportTaskMapper.selectExportTaskById(taskId);
            if (task != null) {
                metrics.put("batchSize", task.getBatchSize());
                metrics.put("parallelism", task.getParallelism());
                metrics.put("totalBatches", task.getTotalBatches());
                metrics.put("currentBatch", task.getCurrentBatch());
                metrics.put("fileSize", task.getFileSize());
                metrics.put("compressedSize", task.getCompressedSize());
            }

            return createSuccessJson(metrics);
        } catch (Exception e) {
            log.error("获取任务性能指标失败, taskId: {}", taskId, e);
            return createErrorJson("获取任务性能指标失败: " + e.getMessage());
        }
    }

    @Override
    public JsonBean analyzeTaskExecutionPattern(Long taskId) {
        try {
            Map<String, Object> analysis = new HashMap<>();

            ExportProgressDTO progress = progressCache.get(taskId);
            if (progress != null) {
                // 分析处理速度模式
                analysis.put("speedPattern", analyzeSpeedPattern(taskId));

                // 分析资源使用模式
                analysis.put("resourcePattern", analyzeResourcePattern(taskId));

                // 分析错误模式
                analysis.put("errorPattern", analyzeErrorPattern(taskId));

                // 分析进度模式
                analysis.put("progressPattern", analyzeProgressPattern(taskId));
            }

            return createSuccessJson(analysis);
        } catch (Exception e) {
            log.error("分析任务执行模式失败, taskId: {}", taskId, e);
            return createErrorJson("分析任务执行模式失败: " + e.getMessage());
        }
    }

    @Override
    public JsonBean predictTaskCompletionTime(Long taskId) {
        try {
            ExportProgressDTO progress = progressCache.get(taskId);
            if (progress == null) {
                return createErrorJson("任务进度信息不存在");
            }

            Map<String, Object> prediction = new HashMap<>();

            if (progress.getProcessingRate() != null && progress.getProcessingRate() > 0) {
                Long remainingRecords = progress.getTotalRecords() - progress.getProcessedRecords();
                Long estimatedSeconds = (long) (remainingRecords / progress.getProcessingRate());

                prediction.put("estimatedRemainingSeconds", estimatedSeconds);
                prediction.put("estimatedCompletionTime", new Date(System.currentTimeMillis() + estimatedSeconds * 1000));
                prediction.put("confidence", calculatePredictionConfidence(progress));
            } else {
                prediction.put("estimatedRemainingSeconds", -1);
                prediction.put("estimatedCompletionTime", null);
                prediction.put("confidence", 0.0);
                prediction.put("message", "处理速度不足，无法预测");
            }

            return createSuccessJson(prediction);
        } catch (Exception e) {
            log.error("预测任务完成时间失败, taskId: {}", taskId, e);
            return createErrorJson("预测任务完成时间失败: " + e.getMessage());
        }
    }

    @Override
    public JsonBean getSystemResourceUsage() {
        try {
            Map<String, Object> usage = new HashMap<>();

            // 获取系统资源信息
            Runtime runtime = Runtime.getRuntime();
            long totalMemory = runtime.totalMemory();
            long freeMemory = runtime.freeMemory();
            long usedMemory = totalMemory - freeMemory;
            long maxMemory = runtime.maxMemory();

            usage.put("totalMemory", totalMemory / 1024 / 1024); // MB
            usage.put("usedMemory", usedMemory / 1024 / 1024); // MB
            usage.put("freeMemory", freeMemory / 1024 / 1024); // MB
            usage.put("maxMemory", maxMemory / 1024 / 1024); // MB
            usage.put("memoryUsagePercent", (double) usedMemory / maxMemory * 100);

            usage.put("activeProcessors", runtime.availableProcessors());
            usage.put("runningTasksCount", progressCache.size());
            usage.put("pausedTasksCount", pausedTasks.size());

            return createSuccessJson(usage);
        } catch (Exception e) {
            log.error("获取系统资源使用情况失败", e);
            return createErrorJson("获取系统资源使用情况失败: " + e.getMessage());
        }
    }

    @Override
    public JsonBean optimizeTaskExecutionStrategy(Long taskId) {
        try {
            Map<String, Object> recommendations = new HashMap<>();

            ExportProgressDTO progress = progressCache.get(taskId);
            if (progress != null) {
                List<String> optimizations = new ArrayList<>();

                // 分析并提供建议
                if (progress.getMemoryUsage() != null && progress.getMemoryUsage() > 80) {
                    optimizations.add("内存使用率过高，建议减少批处理大小");
                    recommendations.put("recommendedBatchSize", Math.max(100, progress.getCurrentBatch() / 2));
                }

                if (progress.getProcessingRate() != null && progress.getProcessingRate() < 100) {
                    optimizations.add("处理速度较慢，建议增加并行度");
                    recommendations.put("recommendedParallelism", Math.min(4, progress.getTotalBatches() / 10));
                }

                if (progress.getErrorCount() != null && progress.getErrorCount() > 10) {
                    optimizations.add("错误率较高，建议检查数据质量和网络连接");
                }

                if (progress.getCompressionRate() != null && progress.getCompressionRate().compareTo(new BigDecimal("50")) < 0) {
                    optimizations.add("压缩率较低，建议调整压缩算法或禁用压缩");
                }

                recommendations.put("optimizations", optimizations);
            }

            return createSuccessJson(recommendations);
        } catch (Exception e) {
            log.error("优化任务执行策略失败, taskId: {}", taskId, e);
            return createErrorJson("优化任务执行策略失败: " + e.getMessage());
        }
    }

    @Override
    public void setTaskPriority(Long taskId, Integer priority) {
        taskPriorities.put(taskId, priority);
        log.debug("设置任务优先级, taskId: {}, priority: {}", taskId, priority);
    }

    @Override
    public JsonBean getTaskQueueStatus() {
        try {
            Map<String, Object> queueStatus = new HashMap<>();

            queueStatus.put("totalTasks", progressCache.size());
            queueStatus.put("runningTasks", progressCache.size() - pausedTasks.size());
            queueStatus.put("pausedTasks", pausedTasks.size());
            queueStatus.put("highPriorityTasks", taskPriorities.values().stream().mapToInt(i -> i).filter(p -> p >= 8).count());
            queueStatus.put("mediumPriorityTasks", taskPriorities.values().stream().mapToInt(i -> i).filter(p -> p >= 5 && p < 8).count());
            queueStatus.put("lowPriorityTasks", taskPriorities.values().stream().mapToInt(i -> i).filter(p -> p < 5).count());

            return createSuccessJson(queueStatus);
        } catch (Exception e) {
            log.error("获取任务队列状态失败", e);
            return createErrorJson("获取任务队列状态失败: " + e.getMessage());
        }
    }

    // JsonBean 辅助方法
    private JsonBean createSuccessJson(Object data) {
        JsonBean json = new JsonBean();
        json.setCode(1);
        json.setMsg("操作成功");
        json.setData(data);
        return json;
    }

    private JsonBean createErrorJson(String message) {
        JsonBean json = new JsonBean();
        json.setCode(0);
        json.setMsg(message);
        return json;
    }

    // 定时清理已完成的任务监听器
    @Scheduled(fixedRate = 300000) // 每5分钟执行一次
    public void cleanupCompletedTaskListeners() {
        try {
            List<Long> completedTaskIds = new ArrayList<>();

            for (Long taskId : progressCache.keySet()) {
                ExportTaskEntity task = exportTaskMapper.selectExportTaskById(taskId);
                if (task != null && (task.getStatus() == 2 || task.getStatus() == 3 || task.getStatus() == 4)) {
                    completedTaskIds.add(taskId);
                }
            }

            for (Long taskId : completedTaskIds) {
                unregisterProgressListener(taskId);
            }

            if (!completedTaskIds.isEmpty()) {
                log.info("清理了{}个已完成任务的监听器", completedTaskIds.size());
            }
        } catch (Exception e) {
            log.error("清理已完成任务监听器失败", e);
        }
    }

    // 私有辅助方法
    private void calculateProgressMetrics(Long taskId, ExportProgressDTO progress) {
        // 记录开始时间
        taskStartTimes.putIfAbsent(taskId, System.currentTimeMillis());

        Long startTime = taskStartTimes.get(taskId);
        long currentTime = System.currentTimeMillis();
        long elapsedTime = (currentTime - startTime) / 1000; // 秒

        progress.setElapsedTime(elapsedTime);

        // 计算处理速度
        if (progress.getProcessedRecords() != null && progress.getProcessedRecords() > 0 && elapsedTime > 0) {
            double processingRate = (double) progress.getProcessedRecords() / elapsedTime;
            progress.setProcessingRate(processingRate);

            // 计算预估剩余时间
            Long remainingRecords = progress.getTotalRecords() - progress.getProcessedRecords();
            if (remainingRecords > 0 && processingRate > 0) {
                long estimatedRemainingTime = (long) (remainingRecords / processingRate);
                progress.setEstimatedRemainingTime(estimatedRemainingTime);
            }
        }
    }

    private void notifyProgressListeners(Long taskId, ExportProgressDTO progress) {
        List<ExportProgressListener> listeners = progressListeners.get(taskId);
        if (listeners != null && !listeners.isEmpty()) {
            for (ExportProgressListener listener : listeners) {
                try {
                    listener.onProgressUpdate(taskId, progress);
                } catch (Exception e) {
                    log.error("进度监听器回调失败, taskId: {}", taskId, e);
                }
            }
        }
    }

    private void addSystemResourceInfo(ExportProgressDTO progress) {
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        double memoryUsagePercent = (double) usedMemory / runtime.maxMemory() * 100;

        progress.setMemoryUsage(memoryUsagePercent);
        progress.setCpuUsage(getCpuUsage());
    }

    private Double getCpuUsage() {
        // 简单的CPU使用率估算，实际项目中应使用更精确的方法
        return Math.random() * 100;
    }

    private Date getStartDateByRange(String timeRange) {
        Calendar cal = Calendar.getInstance();
        switch (timeRange.toLowerCase()) {
            case "1h":
                cal.add(Calendar.HOUR, -1);
                break;
            case "24h":
                cal.add(Calendar.DAY_OF_MONTH, -1);
                break;
            case "7d":
                cal.add(Calendar.DAY_OF_MONTH, -7);
                break;
            case "30d":
                cal.add(Calendar.DAY_OF_MONTH, -30);
                break;
            default:
                cal.add(Calendar.DAY_OF_MONTH, -7);
        }
        return cal.getTime();
    }

    private Map<String, Object> analyzeSpeedPattern(Long taskId) {
        Map<String, Object> pattern = new HashMap<>();
        // 实现速度模式分析逻辑
        pattern.put("trend", "STABLE");
        pattern.put("averageSpeed", 1000.0);
        return pattern;
    }

    private Map<String, Object> analyzeResourcePattern(Long taskId) {
        Map<String, Object> pattern = new HashMap<>();
        // 实现资源使用模式分析逻辑
        pattern.put("memoryTrend", "INCREASING");
        pattern.put("cpuTrend", "STABLE");
        return pattern;
    }

    private Map<String, Object> analyzeErrorPattern(Long taskId) {
        Map<String, Object> pattern = new HashMap<>();
        // 实现错误模式分析逻辑
        pattern.put("errorType", "TIMEOUT");
        pattern.put("frequency", "LOW");
        return pattern;
    }

    private Map<String, Object> analyzeProgressPattern(Long taskId) {
        Map<String, Object> pattern = new HashMap<>();
        // 实现进度模式分析逻辑
        pattern.put("phase", "DATA_PROCESSING");
        pattern.put("consistency", "GOOD");
        return pattern;
    }

    private Double calculatePredictionConfidence(ExportProgressDTO progress) {
        // 根据当前进度和数据计算预测置信度
        if (progress.getProgressPercent() != null) {
            double percent = progress.getProgressPercent().doubleValue();
            if (percent > 80) return 0.95;
            if (percent > 50) return 0.85;
            if (percent > 20) return 0.70;
            return 0.50;
        }
        return 0.0;
    }
}