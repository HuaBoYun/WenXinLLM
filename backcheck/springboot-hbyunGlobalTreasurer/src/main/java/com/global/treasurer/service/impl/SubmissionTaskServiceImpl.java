package com.global.treasurer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblSubmissionTask;
import com.global.treasurer.mapper.SubmissionTaskMapper;
import com.global.treasurer.service.SubmissionTaskService;
import com.global.treasurer.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 报送任务服务实现类
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
@Service
public class SubmissionTaskServiceImpl implements SubmissionTaskService {
    @Autowired
    private SubmissionTaskMapper taskMapper;

    @Override
    public PageInfo<TblSubmissionTask> getTaskList(Map<String, Object> params) {
        int pageNum = params.get("pageNum") != null ? (Integer) params.get("pageNum") : 1;
        int pageSize = params.get("pageSize") != null ? (Integer) params.get("pageSize") : 10;
        PageHelper.startPage(pageNum, pageSize);
        List<TblSubmissionTask> list = taskMapper.selectTaskList(params);
        return new PageInfo<>(list);
    }

    @Override
    public TblSubmissionTask getTaskById(String taskId) {
        TblSubmissionTask task = taskMapper.selectTaskById(taskId);
        if (task == null) {
            throw new ServiceException(404, "报送任务不存在");
        }
        return task;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblSubmissionTask saveTask(TblSubmissionTask task) {
        if (task.getTaskId() == null || task.getTaskId().isEmpty()) {
            task.setTaskCode(generateTaskCode());
            task.setTaskStatus("PENDING");
            task.setRetryCount(0);
            task.setMaxRetries(3);
            task.setDeleteFlag(0);
            task.setCreatedTime(new Date());
            taskMapper.insert(task);
        } else {
            task.setUpdatedTime(new Date());
            taskMapper.updateById(task);
        }
        return task;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteTask(String taskId) {
        TblSubmissionTask task = getTaskById(taskId);
        task.setDeleteFlag(1);
        task.setUpdatedTime(new Date());
        taskMapper.updateById(task);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteTasks(List<String> taskIds) {
        taskMapper.batchDeleteByIds(taskIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblSubmissionTask executeTask(String taskId) {
        TblSubmissionTask task = getTaskById(taskId);
        task.setTaskStatus("RUNNING");
        task.setExecutedTime(new Date());
        task.setUpdatedTime(new Date());
        taskMapper.updateById(task);
        return task;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblSubmissionTask pauseTask(String taskId) {
        TblSubmissionTask task = getTaskById(taskId);
        task.setTaskStatus("PAUSED");
        task.setUpdatedTime(new Date());
        taskMapper.updateById(task);
        return task;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblSubmissionTask resumeTask(String taskId) {
        TblSubmissionTask task = getTaskById(taskId);
        task.setTaskStatus("RUNNING");
        task.setUpdatedTime(new Date());
        taskMapper.updateById(task);
        return task;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblSubmissionTask cancelTask(String taskId, String cancelReason) {
        TblSubmissionTask task = getTaskById(taskId);
        task.setTaskStatus("CANCELLED");
        task.setUpdatedTime(new Date());
        taskMapper.updateById(task);
        return task;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblSubmissionTask retryTask(String taskId) {
        TblSubmissionTask task = getTaskById(taskId);
        task.setTaskStatus("PENDING");
        task.setRetryCount(task.getRetryCount() + 1);
        task.setUpdatedTime(new Date());
        taskMapper.updateById(task);
        return task;
    }

    @Override
    public List<TblSubmissionTask> getPendingTasks() {
        return taskMapper.selectList(
            new QueryWrapper<TblSubmissionTask>()
                .eq("TASK_STATUS", "PENDING")
                .orderByAsc("DUE_DATE")
        );
    }

    @Override
    public List<TblSubmissionTask> getOverdueTasks() {
        return taskMapper.selectList(
            new QueryWrapper<TblSubmissionTask>()
                .eq("TASK_STATUS", "PENDING")
                .lt("DUE_DATE", new Date())
                .orderByAsc("DUE_DATE")
        );
    }

    @Override
    public List<TblSubmissionTask> getDueSoonTasks(Integer days) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_MONTH, days);
        Date futureDate = cal.getTime();

        return taskMapper.selectList(
            new QueryWrapper<TblSubmissionTask>()
                .eq("TASK_STATUS", "PENDING")
                .ge("DUE_DATE", new Date())
                .le("DUE_DATE", futureDate)
                .orderByAsc("DUE_DATE")
        );
    }

    private String generateTaskCode() {
        return "TASK" + System.currentTimeMillis();
    }
}

