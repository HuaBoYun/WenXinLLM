package com.global.treasurer.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.DataAnalysisTask;
import com.global.treasurer.mapper.DataAnalysisTaskMapper;
import com.global.treasurer.service.IDataAnalysisTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 数据分析任务Service实现类
 *
 * @author 华博云开发团队
 * @since 2025-01-12
 */
@Service
public class DataAnalysisTaskServiceImpl extends ServiceImpl<DataAnalysisTaskMapper, DataAnalysisTask>
        implements IDataAnalysisTaskService {
    private static final Logger log = LoggerFactory.getLogger(DataAnalysisTaskServiceImpl.class);

    @Override
    public IPage<DataAnalysisTask> selectPage(IPage<DataAnalysisTask> page, Map<String, Object> params) {
        log.info("========== 数据分析任务查询Service ==========");
        log.info("接收到的分页参数 - 当前页: {}, 每页大小: {}", page.getCurrent(), page.getSize());
        log.info("接收到的查询参数: {}", params);

        // 提取查询参数
        String taskNo = params.get("taskNo") != null ? params.get("taskNo").toString() : null;
        String taskName = params.get("taskName") != null ? params.get("taskName").toString() : null;
        String taskType = params.get("taskType") != null ? params.get("taskType").toString() : null;
        String taskStatus = params.get("taskStatus") != null ? params.get("taskStatus").toString() : null;
        Long orgId = params.get("orgId") != null ? Long.parseLong(params.get("orgId").toString()) : null;

        // 使用 PageHelper 进行分页（项目已禁用 MyBatis-Plus 分页插件）
        PageHelper.startPage((int)page.getCurrent(), (int)page.getSize());

        // 调用自定义的条件查询方法
        List<DataAnalysisTask> list = baseMapper.selectByCondition(
                taskNo, taskName, taskType, taskStatus, orgId);

        log.info("查询结果记录数: {}", list.size());

        // 使用 PageInfo 获取分页信息
        PageInfo<DataAnalysisTask> pageInfo = new PageInfo<>(list);

        // 构建返回的 IPage 对象
        Page<DataAnalysisTask> resultPage = new Page<>(page.getCurrent(), page.getSize(), pageInfo.getTotal());
        resultPage.setRecords(list);
        resultPage.setPages(pageInfo.getPages());

        log.info("返回的分页信息 - 当前页: {}, 每页大小: {}, 总记录数: {}, 总页数: {}",
                resultPage.getCurrent(), resultPage.getSize(), resultPage.getTotal(), resultPage.getPages());

        return resultPage;
    }

    @Override
    public boolean executeTask(Long taskId, Long executeUser) {
        log.info("执行数据分析任务 - taskId:{}, executeUser:{}", taskId, executeUser);

        try {
            // 检查任务是否存在
            DataAnalysisTask task = getById(taskId);
            if (task == null) {
                log.error("任务不存在 - taskId:{}", taskId);
                return false;
            }

            // 检查任务状态
            if (!"PENDING".equals(task.getTaskStatus())) {
                log.error("任务状态不允许执行 - taskId:{}, taskStatus:{}", taskId, task.getTaskStatus());
                return false;
            }

            // 更新任务状态为运行中
            task.setTaskStatus("RUNNING");
            task.setStartTime(LocalDateTime.now());
            task.setExecuteUser(executeUser);
            task.setUpdateTime(LocalDateTime.now());

            boolean success = updateById(task);
            if (success) {
                log.info("任务开始执行成功 - taskId:{}", taskId);

                // TODO: 这里应该调用实际的数据分析引擎执行任务
                // 由于当前只修复编译问题,暂时只更新状态

                return true;
            } else {
                log.error("更新任务状态失败 - taskId:{}", taskId);
                return false;
            }

        } catch (Exception e) {
            log.error("执行数据分析任务失败 - taskId:{}", taskId, e);

            // 更新任务状态为失败
            DataAnalysisTask task = getById(taskId);
            if (task != null) {
                task.setTaskStatus("FAILED");
                task.setEndTime(LocalDateTime.now());
                task.setErrorMessage("执行任务失败: " + e.getMessage());
                updateById(task);
            }

            return false;
        }
    }

    @Override
    public boolean cancelTask(Long taskId, Long updateUser) {
        log.info("取消数据分析任务 - taskId:{}, updateUser:{}", taskId, updateUser);

        try {
            // 检查任务是否存在
            DataAnalysisTask task = getById(taskId);
            if (task == null) {
                log.error("任务不存在 - taskId:{}", taskId);
                return false;
            }

            // 检查任务状态
            if ("COMPLETED".equals(task.getTaskStatus()) || "FAILED".equals(task.getTaskStatus())) {
                log.error("任务已结束,无法取消 - taskId:{}, taskStatus:{}", taskId, task.getTaskStatus());
                return false;
            }

            // 更新任务状态为已取消
            task.setTaskStatus("CANCELLED");
            task.setEndTime(LocalDateTime.now());
            task.setUpdateTime(LocalDateTime.now());
            task.setUpdateBy(updateUser);

            boolean success = updateById(task);
            if (success) {
                log.info("任务取消成功 - taskId:{}", taskId);
            } else {
                log.error("更新任务状态失败 - taskId:{}", taskId);
            }

            return success;

        } catch (Exception e) {
            log.error("取消数据分析任务失败 - taskId:{}", taskId, e);
            return false;
        }
    }

    @Override
    public boolean retryTask(Long taskId, Long executeUser) {
        log.info("重试失败任务 - taskId:{}, executeUser:{}", taskId, executeUser);

        try {
            // 检查任务是否存在
            DataAnalysisTask task = getById(taskId);
            if (task == null) {
                log.error("任务不存在 - taskId:{}", taskId);
                return false;
            }

            // 检查任务状态
            if (!"FAILED".equals(task.getTaskStatus()) && !"CANCELLED".equals(task.getTaskStatus())) {
                log.error("任务状态不允许重试 - taskId:{}, taskStatus:{}", taskId, task.getTaskStatus());
                return false;
            }

            // 重置任务状态为待执行
            task.setTaskStatus("PENDING");
            task.setStartTime(null);
            task.setEndTime(null);
            task.setExecutionTime(null);
            task.setExecutionResult(null);
            task.setErrorMessage(null);
            task.setUpdateTime(LocalDateTime.now());
            task.setUpdateBy(executeUser);

            boolean success = updateById(task);
            if (success) {
                log.info("任务重置成功,可以重新执行 - taskId:{}", taskId);
            } else {
                log.error("更新任务状态失败 - taskId:{}", taskId);
            }

            return success;

        } catch (Exception e) {
            log.error("重试数据分析任务失败 - taskId:{}", taskId, e);
            return false;
        }
    }
}
