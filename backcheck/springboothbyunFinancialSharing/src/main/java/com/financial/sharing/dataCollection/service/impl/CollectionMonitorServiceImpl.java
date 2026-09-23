package com.financial.sharing.dataCollection.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.financial.sharing.dataCollection.dto.*;
import com.financial.sharing.dataCollection.entity.TblCollectionLog;
import com.financial.sharing.dataCollection.entity.TblCollectionTask;
import com.financial.sharing.dataCollection.mapper.CollectionLogMapper;
import com.financial.sharing.dataCollection.mapper.CollectionTaskMapper;
import com.financial.sharing.dataCollection.service.CollectionMonitorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 归集监控服务实现类
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Slf4j
@Service
public class CollectionMonitorServiceImpl implements CollectionMonitorService {

    @Autowired
    private CollectionTaskMapper collectionTaskMapper;

    @Autowired
    private CollectionLogMapper collectionLogMapper;

    @Override
    @Transactional(readOnly = true)
    public CollectionMonitorStatistics getOverallStatistics(Long orgId) {
        log.info("获取整体监控统计, orgId={}", orgId);

        CollectionMonitorStatistics statistics = new CollectionMonitorStatistics();

        // 1. 查询任务统计
        QueryWrapper<TblCollectionTask> taskWrapper = new QueryWrapper<>();
        taskWrapper.eq("ORG_ID", orgId);
        Integer totalTasks = collectionTaskMapper.selectCount(taskWrapper);
        statistics.setTotalTasks(totalTasks);

        // 启用任务数
        QueryWrapper<TblCollectionTask> enabledWrapper = new QueryWrapper<>();
        enabledWrapper.eq("ORG_ID", orgId);
        enabledWrapper.eq("IS_ENABLED", "Y");
        Integer enabledTasks = collectionTaskMapper.selectCount(enabledWrapper);
        statistics.setEnabledTasks(enabledTasks);

        // 定时任务数
        QueryWrapper<TblCollectionTask> scheduledWrapper = new QueryWrapper<>();
        scheduledWrapper.eq("ORG_ID", orgId);
        scheduledWrapper.eq("TASK_TYPE", "SCHEDULED");
        Integer scheduledTasks = collectionTaskMapper.selectCount(scheduledWrapper);
        statistics.setScheduledTasks(scheduledTasks);

        // 2. 查询今日执行统计
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date todayStart = calendar.getTime();

        QueryWrapper<TblCollectionLog> todayWrapper = new QueryWrapper<>();
        todayWrapper.eq("ORG_ID", orgId);
        todayWrapper.ge("START_TIME", todayStart);
        List<TblCollectionLog> todayLogs = collectionLogMapper.selectList(todayWrapper);

        statistics.setTodayExecutions(todayLogs.size());
        long todaySuccess = todayLogs.stream().filter(log -> "SUCCESS".equals(log.getExecuteStatus())).count();
        long todayFailed = todayLogs.stream().filter(log -> "FAILED".equals(log.getExecuteStatus())).count();
        statistics.setTodaySuccessCount((int) todaySuccess);
        statistics.setTodayFailedCount((int) todayFailed);

        // 今日成功率
        if (todayLogs.size() > 0) {
            double successRate = (double) todaySuccess / todayLogs.size() * 100;
            statistics.setTodaySuccessRate(Math.round(successRate * 100.0) / 100.0);
        } else {
            statistics.setTodaySuccessRate(0.0);
        }

        // 3. 查询本周执行统计
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Date weekStart = calendar.getTime();

        QueryWrapper<TblCollectionLog> weekWrapper = new QueryWrapper<>();
        weekWrapper.eq("ORG_ID", orgId);
        weekWrapper.ge("START_TIME", weekStart);
        Integer weekExecutions = collectionLogMapper.selectCount(weekWrapper);
        statistics.setWeekExecutions(weekExecutions);

        // 4. 查询本月执行统计
        calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date monthStart = calendar.getTime();

        QueryWrapper<TblCollectionLog> monthWrapper = new QueryWrapper<>();
        monthWrapper.eq("ORG_ID", orgId);
        monthWrapper.ge("START_TIME", monthStart);
        Integer monthExecutions = collectionLogMapper.selectCount(monthWrapper);
        statistics.setMonthExecutions(monthExecutions);

        // 5. 查询累计记录统计
        QueryWrapper<TblCollectionLog> allWrapper = new QueryWrapper<>();
        allWrapper.eq("ORG_ID", orgId);
        allWrapper.eq("EXECUTE_STATUS", "SUCCESS");
        List<TblCollectionLog> allLogs = collectionLogMapper.selectList(allWrapper);

        long totalRecords = allLogs.stream()
                .mapToLong(log -> log.getTotalCount() != null ? log.getTotalCount() : 0)
                .sum();
        long totalSuccessRecords = allLogs.stream()
                .mapToLong(log -> log.getSuccessCount() != null ? log.getSuccessCount() : 0)
                .sum();
        long totalFailedRecords = allLogs.stream()
                .mapToLong(log -> log.getFailedCount() != null ? log.getFailedCount() : 0)
                .sum();

        statistics.setTotalRecords(totalRecords);
        statistics.setTotalSuccessRecords(totalSuccessRecords);
        statistics.setTotalFailedRecords(totalFailedRecords);

        // 6. 计算平均执行时长
        QueryWrapper<TblCollectionLog> durationWrapper = new QueryWrapper<>();
        durationWrapper.eq("ORG_ID", orgId);
        durationWrapper.isNotNull("EXECUTE_DURATION");
        List<TblCollectionLog> durationLogs = collectionLogMapper.selectList(durationWrapper);

        if (!durationLogs.isEmpty()) {
            long avgDuration = (long) durationLogs.stream()
                    .mapToLong(TblCollectionLog::getExecuteDuration)
                    .average()
                    .orElse(0);
            statistics.setAvgExecutionDuration(avgDuration);
        } else {
            statistics.setAvgExecutionDuration(0L);
        }

        return statistics;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskExecutionTrend> getTaskExecutionTrend(Long orgId, Integer days) {
        log.info("获取任务执行趋势, orgId={}, days={}", orgId, days);

        if (days == null || days <= 0) {
            days = 7; // 默认7天
        }

        // 计算开始日期
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -days + 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date startDate = calendar.getTime();

        // 查询日志
        QueryWrapper<TblCollectionLog> wrapper = new QueryWrapper<>();
        wrapper.eq("ORG_ID", orgId);
        wrapper.ge("START_TIME", startDate);
        wrapper.orderByAsc("START_TIME");
        List<TblCollectionLog> logs = collectionLogMapper.selectList(wrapper);

        // 按日期分组统计
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, List<TblCollectionLog>> logsByDate = logs.stream()
                .collect(Collectors.groupingBy(log -> sdf.format(log.getStartTime())));

        // 生成趋势数据
        List<TaskExecutionTrend> trendList = new ArrayList<>();
        calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -days + 1);

        for (int i = 0; i < days; i++) {
            String date = sdf.format(calendar.getTime());
            TaskExecutionTrend trend = new TaskExecutionTrend();
            trend.setDate(date);

            List<TblCollectionLog> dayLogs = logsByDate.getOrDefault(date, new ArrayList<>());
            trend.setExecutionCount(dayLogs.size());

            long successCount = dayLogs.stream().filter(log -> "SUCCESS".equals(log.getExecuteStatus())).count();
            long failedCount = dayLogs.stream().filter(log -> "FAILED".equals(log.getExecuteStatus())).count();

            trend.setSuccessCount((int) successCount);
            trend.setFailedCount((int) failedCount);

            if (dayLogs.size() > 0) {
                double successRate = (double) successCount / dayLogs.size() * 100;
                trend.setSuccessRate(Math.round(successRate * 100.0) / 100.0);
            } else {
                trend.setSuccessRate(0.0);
            }

            trendList.add(trend);
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        return trendList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskStatusDistribution> getTaskStatusDistribution(Long orgId) {
        log.info("获取任务状态分布, orgId={}", orgId);

        // 查询所有任务
        QueryWrapper<TblCollectionTask> wrapper = new QueryWrapper<>();
        wrapper.eq("ORG_ID", orgId);
        List<TblCollectionTask> tasks = collectionTaskMapper.selectList(wrapper);

        int totalTasks = tasks.size();
        if (totalTasks == 0) {
            return new ArrayList<>();
        }

        // 按状态分组统计
        Map<String, Long> statusCounts = tasks.stream()
                .collect(Collectors.groupingBy(TblCollectionTask::getExecuteStatus, Collectors.counting()));

        // 生成分布数据
        List<TaskStatusDistribution> distributionList = new ArrayList<>();

        // 状态映射
        Map<String, String> statusNames = new HashMap<>();
        statusNames.put("PENDING", "待执行");
        statusNames.put("RUNNING", "执行中");
        statusNames.put("SUCCESS", "执行成功");
        statusNames.put("FAILED", "执行失败");
        statusNames.put("STOPPED", "已停止");

        for (Map.Entry<String, Long> entry : statusCounts.entrySet()) {
            TaskStatusDistribution distribution = new TaskStatusDistribution();
            distribution.setStatusName(statusNames.getOrDefault(entry.getKey(), entry.getKey()));
            distribution.setTaskCount(entry.getValue().intValue());
            double percentage = (double) entry.getValue() / totalTasks * 100;
            distribution.setPercentage(Math.round(percentage * 100.0) / 100.0);
            distributionList.add(distribution);
        }

        return distributionList;
    }

    @Override
    @Transactional(readOnly = true)
    public List<FailedTaskStatistics> getTopFailedTasks(Long orgId, Integer days) {
        log.info("获取失败任务TOP10, orgId={}, days={}", orgId, days);

        if (days == null || days <= 0) {
            days = 30; // 默认30天
        }

        // 计算开始日期
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -days);
        Date startDate = calendar.getTime();

        // 查询失败日志
        QueryWrapper<TblCollectionLog> wrapper = new QueryWrapper<>();
        wrapper.eq("ORG_ID", orgId);
        wrapper.eq("EXECUTE_STATUS", "FAILED");
        wrapper.ge("START_TIME", startDate);
        wrapper.orderByDesc("START_TIME");
        List<TblCollectionLog> failedLogs = collectionLogMapper.selectList(wrapper);

        // 按任务ID分组统计
        Map<String, List<TblCollectionLog>> logsByTask = failedLogs.stream()
                .collect(Collectors.groupingBy(TblCollectionLog::getTaskId));

        // 生成失败任务统计
        List<FailedTaskStatistics> statisticsList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (Map.Entry<String, List<TblCollectionLog>> entry : logsByTask.entrySet()) {
            List<TblCollectionLog> taskLogs = entry.getValue();
            if (taskLogs.isEmpty()) {
                continue;
            }

            TblCollectionLog latestLog = taskLogs.get(0); // 已按时间倒序排列

            FailedTaskStatistics statistics = new FailedTaskStatistics();
            statistics.setTaskId(entry.getKey());
            statistics.setTaskCode(latestLog.getTaskCode());
            statistics.setTaskName(latestLog.getTaskName());
            statistics.setFailedCount(taskLogs.size());
            statistics.setLastFailedTime(sdf.format(latestLog.getStartTime()));
            statistics.setLastErrorMessage(latestLog.getErrorMessage());

            statisticsList.add(statistics);
        }

        // 按失败次数倒序排列，取TOP10
        return statisticsList.stream()
                .sorted(Comparator.comparing(FailedTaskStatistics::getFailedCount).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskExecutionTrend> getExecutionDurationStatistics(Long orgId) {
        log.info("获取执行时长统计, orgId={}", orgId);

        // 查询所有任务
        QueryWrapper<TblCollectionTask> taskWrapper = new QueryWrapper<>();
        taskWrapper.eq("ORG_ID", orgId);
        List<TblCollectionTask> tasks = collectionTaskMapper.selectList(taskWrapper);

        // 查询最近的成功日志（每个任务最近一次）
        List<TaskExecutionTrend> durationList = new ArrayList<>();

        for (TblCollectionTask task : tasks) {
            QueryWrapper<TblCollectionLog> logWrapper = new QueryWrapper<>();
            logWrapper.eq("ORG_ID", orgId);
            logWrapper.eq("TASK_ID", task.getTaskId());
            logWrapper.eq("EXECUTE_STATUS", "SUCCESS");
            logWrapper.orderByDesc("START_TIME");
            logWrapper.last("LIMIT 1");

            List<TblCollectionLog> logs = collectionLogMapper.selectList(logWrapper);
            if (!logs.isEmpty()) {
                TblCollectionLog log = logs.get(0);
                TaskExecutionTrend trend = new TaskExecutionTrend();
                trend.setDate(task.getTaskName()); // 使用任务名称作为标签
                trend.setExecutionCount(log.getExecuteDuration().intValue()); // 使用执行时长
                durationList.add(trend);
            }
        }

        // 按执行时长倒序排列，取TOP10
        return durationList.stream()
                .sorted(Comparator.comparing(TaskExecutionTrend::getExecutionCount).reversed())
                .limit(10)
                .collect(Collectors.toList());
    }
}

