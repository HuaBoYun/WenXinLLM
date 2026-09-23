package com.financial.sharing.dataCollection.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.financial.sharing.dataCollection.dto.CollectionTaskQueryParam;
import com.financial.sharing.dataCollection.entity.TblCollectionTask;
import com.financial.sharing.dataCollection.mapper.CollectionTaskMapper;
import com.financial.sharing.dataCollection.service.CollectionTaskService;
import com.financial.sharing.dataCollection.service.CollectionTaskExecutionService;
import com.financial.sharing.dataCollection.service.CollectionTaskScheduleService;
import com.financial.sharing.util.MyJsonBean;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.support.CronExpression;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 归集任务Service实现类
 *
 * @author Augment Agent
 * @date 2026-02-02
 */
@Slf4j
@Service
public class CollectionTaskServiceImpl implements CollectionTaskService {

    @Autowired
    private CollectionTaskMapper collectionTaskMapper;

    @Autowired
    private CollectionTaskExecutionService collectionTaskExecutionService;

    @Autowired
    private CollectionTaskScheduleService collectionTaskScheduleService;

    @Override
    public MyJsonBean queryPage(CollectionTaskQueryParam param, String orgId) {
        try {
            // 使用 PageHelper 分页 (达梦数据库下 MyBatis-Plus 分页插件的 COUNT 子查询会保留 ORDER BY,
            // 触发达梦语法错误, 与项目其他模块保持一致, 改用 PageHelper)
            LambdaQueryWrapper<TblCollectionTask> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TblCollectionTask::getOrgId, orgId);

            if (StringUtils.isNotBlank(param.getTaskCode())) {
                wrapper.like(TblCollectionTask::getTaskCode, param.getTaskCode());
            }
            if (StringUtils.isNotBlank(param.getTaskName())) {
                wrapper.like(TblCollectionTask::getTaskName, param.getTaskName());
            }
            if (StringUtils.isNotBlank(param.getTaskType())) {
                wrapper.eq(TblCollectionTask::getTaskType, param.getTaskType());
            }
            if (StringUtils.isNotBlank(param.getSourceId())) {
                wrapper.eq(TblCollectionTask::getSourceId, param.getSourceId());
            }
            if (StringUtils.isNotBlank(param.getExecuteStatus())) {
                wrapper.eq(TblCollectionTask::getExecuteStatus, param.getExecuteStatus());
            }
            if (StringUtils.isNotBlank(param.getIsEnabled())) {
                wrapper.eq(TblCollectionTask::getIsEnabled, param.getIsEnabled());
            }
            if (StringUtils.isNotBlank(param.getScheduleType())) {
                wrapper.eq(TblCollectionTask::getScheduleType, param.getScheduleType());
            }

            wrapper.orderByDesc(TblCollectionTask::getCreateTime);

            int pageNumber = param.getPageNumber() != null ? param.getPageNumber() : 1;
            int pageSize = param.getPageSize() != null ? param.getPageSize() : 10;

            PageHelper.startPage(pageNumber, pageSize);
            List<TblCollectionTask> list = collectionTaskMapper.selectList(wrapper);
            PageInfo<TblCollectionTask> pageInfo = new PageInfo<>(list);

            // 前端期望读 data.records / data.total 字段
            Map<String, Object> result = new HashMap<>();
            result.put("records", pageInfo.getList());
            result.put("total", pageInfo.getTotal());
            result.put("pageNumber", pageInfo.getPageNum());
            result.put("pageSize", pageInfo.getPageSize());
            result.put("pages", pageInfo.getPages());

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("分页查询归集任务失败", e);
            return MyJsonBean.errorData("分页查询归集任务失败：" + e.getMessage());
        }
    }

    @Override
    public TblCollectionTask queryById(String taskId, String orgId) {
        LambdaQueryWrapper<TblCollectionTask> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TblCollectionTask::getTaskId, taskId);
        wrapper.eq(TblCollectionTask::getOrgId, orgId);

        return collectionTaskMapper.selectOne(wrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean saveCollectionTask(TblCollectionTask task, String orgId, String userId) {
        try {
            task.setOrgId(orgId);

            // 检查任务编码是否重复
            LambdaQueryWrapper<TblCollectionTask> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TblCollectionTask::getTaskCode, task.getTaskCode());
            wrapper.eq(TblCollectionTask::getOrgId, orgId);
            if (StringUtils.isNotBlank(task.getTaskId())) {
                wrapper.ne(TblCollectionTask::getTaskId, task.getTaskId());
            }

            Integer count = collectionTaskMapper.selectCount(wrapper);
            if (count > 0) {
                return MyJsonBean.errorData("任务编码已存在");
            }

            // 验证规则ID列表格式
            if (StringUtils.isNotBlank(task.getRuleIds())) {
                try {
                    JSONArray ruleIds = JSON.parseArray(task.getRuleIds());
                    if (ruleIds == null || ruleIds.isEmpty()) {
                        return MyJsonBean.errorData("规则ID列表不能为空");
                    }
                } catch (Exception e) {
                    return MyJsonBean.errorData("规则ID列表格式错误，必须是JSON数组");
                }
            }

            // 验证调度配置
            if ("SCHEDULED".equals(task.getTaskType())) {
                if (StringUtils.isBlank(task.getScheduleType())) {
                    return MyJsonBean.errorData("定时任务必须配置调度类型");
                }
                if ("CRON".equals(task.getScheduleType())) {
                    if (StringUtils.isBlank(task.getScheduleExpression())) {
                        return MyJsonBean.errorData("CRON调度必须配置调度表达式");
                    }
                    // 验证CRON表达式
                    if (!CronExpression.isValidExpression(task.getScheduleExpression())) {
                        return MyJsonBean.errorData("CRON表达式格式错误");
                    }
                }
            }

            Date now = new Date();
            if (StringUtils.isBlank(task.getTaskId())) {
                // 新增
                task.setCreateUser(userId);
                task.setCreateTime(now);
                task.setUpdateUser(userId);
                task.setUpdateTime(now);
                task.setExecuteStatus("PENDING");
                task.setTotalCount(0);
                task.setSuccessCount(0);
                task.setFailedCount(0);
                task.setSkipCount(0);

                // 计算下次执行时间
                if ("SCHEDULED".equals(task.getTaskType())) {
                    MyJsonBean nextTimeResult = calculateNextExecuteTime(task);
                    if (nextTimeResult.getCode() == 200) {
                        task.setNextExecuteTime((Date) nextTimeResult.getData());
                    }
                }

                collectionTaskMapper.insert(task);
            } else {
                // 更新
                task.setUpdateUser(userId);
                task.setUpdateTime(now);

                // 重新计算下次执行时间
                if ("SCHEDULED".equals(task.getTaskType())) {
                    MyJsonBean nextTimeResult = calculateNextExecuteTime(task);
                    if (nextTimeResult.getCode() == 200) {
                        task.setNextExecuteTime((Date) nextTimeResult.getData());
                    }
                }

                collectionTaskMapper.updateById(task);
            }

            // 处理任务调度
            if ("SCHEDULED".equals(task.getTaskType())) {
                if ("Y".equals(task.getIsEnabled())) {
                    // 启用的定时任务，启动调度
                    collectionTaskScheduleService.scheduleTask(task);
                } else {
                    // 禁用的定时任务，取消调度
                    collectionTaskScheduleService.cancelTask(task.getTaskId());
                }
            } else {
                // 非定时任务，取消调度（如果之前是定时任务）
                collectionTaskScheduleService.cancelTask(task.getTaskId());
            }

            return MyJsonBean.successData(task);
        } catch (Exception e) {
            log.error("保存归集任务失败", e);
            return MyJsonBean.errorData("保存归集任务失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean deleteCollectionTask(String taskId, String orgId) {
        try {
            // 检查任务是否存在
            TblCollectionTask task = queryById(taskId, orgId);
            if (task == null) {
                return MyJsonBean.errorData("归集任务不存在");
            }

            // 检查任务是否正在运行
            if ("RUNNING".equals(task.getExecuteStatus())) {
                return MyJsonBean.errorData("任务正在运行中，无法删除");
            }

            // 如果是定时任务，先取消调度
            if ("SCHEDULED".equals(task.getTaskType())) {
                collectionTaskScheduleService.cancelTask(taskId);
            }

            // 删除任务
            LambdaQueryWrapper<TblCollectionTask> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TblCollectionTask::getTaskId, taskId);
            wrapper.eq(TblCollectionTask::getOrgId, orgId);

            collectionTaskMapper.delete(wrapper);

            return MyJsonBean.successData("删除成功");
        } catch (Exception e) {
            log.error("删除归集任务失败", e);
            return MyJsonBean.errorData("删除归集任务失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public MyJsonBean toggleEnabled(String taskId, String isEnabled, String orgId, String userId) {
        try {
            TblCollectionTask task = queryById(taskId, orgId);
            if (task == null) {
                return MyJsonBean.errorData("归集任务不存在");
            }

            // 检查任务是否正在运行
            if ("RUNNING".equals(task.getExecuteStatus()) && "N".equals(isEnabled)) {
                return MyJsonBean.errorData("任务正在运行中，无法禁用");
            }

            task.setIsEnabled(isEnabled);
            task.setUpdateUser(userId);
            task.setUpdateTime(new Date());

            collectionTaskMapper.updateById(task);

            // 处理任务调度
            if ("SCHEDULED".equals(task.getTaskType())) {
                if ("Y".equals(isEnabled)) {
                    // 启用定时任务，启动调度
                    collectionTaskScheduleService.scheduleTask(task);
                } else {
                    // 禁用定时任务，取消调度
                    collectionTaskScheduleService.cancelTask(task.getTaskId());
                }
            }

            return MyJsonBean.successData("操作成功");
        } catch (Exception e) {
            log.error("启用/禁用归集任务失败", e);
            return MyJsonBean.errorData("启用/禁用归集任务失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean executeTask(String taskId, String orgId) {
        try {
            TblCollectionTask task = queryById(taskId, orgId);
            if (task == null) {
                return MyJsonBean.errorData("归集任务不存在");
            }

            if ("N".equals(task.getIsEnabled())) {
                return MyJsonBean.errorData("任务已禁用，无法执行");
            }

            if ("RUNNING".equals(task.getExecuteStatus())) {
                return MyJsonBean.errorData("任务正在运行中");
            }

            // 更新任务状态为运行中
            task.setExecuteStatus("RUNNING");
            task.setUpdateTime(new Date());
            collectionTaskMapper.updateById(task);

            // 调用执行服务异步执行任务
            collectionTaskExecutionService.executeTaskAsync(taskId, orgId);

            return MyJsonBean.successData("任务已提交执行");
        } catch (Exception e) {
            log.error("执行归集任务失败", e);
            return MyJsonBean.errorData("执行归集任务失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean stopTask(String taskId, String orgId) {
        try {
            TblCollectionTask task = queryById(taskId, orgId);
            if (task == null) {
                return MyJsonBean.errorData("归集任务不存在");
            }

            if (!"RUNNING".equals(task.getExecuteStatus())) {
                return MyJsonBean.errorData("任务未在运行中");
            }

            // 调用执行服务停止任务
            return collectionTaskExecutionService.stopTaskExecution(taskId, orgId);
        } catch (Exception e) {
            log.error("停止归集任务失败", e);
            return MyJsonBean.errorData("停止归集任务失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean calculateNextExecuteTime(TblCollectionTask task) {
        try {
            if (!"SCHEDULED".equals(task.getTaskType())) {
                return MyJsonBean.errorData("只有定时任务才需要计算下次执行时间");
            }

            String scheduleType = task.getScheduleType();
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime nextTime = null;

            switch (scheduleType) {
                case "ONCE":
                    // 一次性任务，下次执行时间为当前时间
                    nextTime = now;
                    break;

                case "DAILY":
                    // 每日任务，下次执行时间为明天的当前时间
                    nextTime = now.plusDays(1);
                    break;

                case "WEEKLY":
                    // 每周任务，下次执行时间为下周的当前时间
                    nextTime = now.plusWeeks(1);
                    break;

                case "MONTHLY":
                    // 每月任务，下次执行时间为下月的当前时间
                    nextTime = now.plusMonths(1);
                    break;

                case "CRON":
                    // CRON表达式任务
                    String cronExpression = task.getScheduleExpression();
                    if (StringUtils.isBlank(cronExpression)) {
                        return MyJsonBean.errorData("CRON表达式不能为空");
                    }

                    CronExpression cron = CronExpression.parse(cronExpression);
                    nextTime = cron.next(now);
                    if (nextTime == null) {
                        return MyJsonBean.errorData("无法计算下次执行时间");
                    }
                    break;

                default:
                    return MyJsonBean.errorData("不支持的调度类型：" + scheduleType);
            }

            Date nextExecuteTime = Date.from(nextTime.atZone(ZoneId.systemDefault()).toInstant());
            return MyJsonBean.successData(nextExecuteTime);
        } catch (Exception e) {
            log.error("计算下次执行时间失败", e);
            return MyJsonBean.errorData("计算下次执行时间失败：" + e.getMessage());
        }
    }
}

