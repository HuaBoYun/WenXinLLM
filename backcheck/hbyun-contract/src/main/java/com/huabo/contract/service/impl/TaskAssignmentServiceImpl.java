package com.huabo.contract.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.huabo.contract.entity.TaskAssignment;
import com.huabo.contract.mapper.TaskAssignmentMapper;
import com.huabo.contract.service.TaskAssignmentService;
import com.huabo.contract.vo.TaskAssignmentQueryParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 任务书表 服务实现类
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Slf4j
@Service
public class TaskAssignmentServiceImpl extends ServiceImpl<TaskAssignmentMapper, TaskAssignment> implements TaskAssignmentService {

    @Autowired
    private TaskAssignmentMapper taskAssignmentMapper;

    @Override
    public PageInfo<TaskAssignment> getTaskAssignmentList(TaskAssignmentQueryParam param) {
        try {
            log.info("分页查询任务书列表，参数：{}", param);
            
            // 设置分页参数
            PageHelper.startPage(param.getPageNumber(), param.getPageSize());
            
            // 查询数据
            List<TaskAssignment> list = taskAssignmentMapper.selectTaskAssignmentList(param);
            
            return new PageInfo<>(list);
        } catch (Exception e) {
            log.error("分页查询任务书列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveTaskAssignment(TaskAssignment taskAssignment) {
        try {
            log.info("保存任务书，任务：{}", taskAssignment);
            
            Date now = new Date();
            
            if (taskAssignment.getId() == null) {
                // 新增
                if (!StringUtils.hasText(taskAssignment.getTaskNo())) {
                    taskAssignment.setTaskNo(generateTaskNo());
                }

                // 设置默认值
                if (taskAssignment.getTaskType() == null) {
                    taskAssignment.setTaskType(1); // 默认为勘察任务书
                }
                if (taskAssignment.getTaskStatus() == null) {
                    taskAssignment.setTaskStatus(1); // 默认为草稿状态
                }
                if (taskAssignment.getIssueDate() == null) {
                    taskAssignment.setIssueDate(now); // 默认为当前日期
                }
                if (taskAssignment.getProgressPercentage() == null) {
                    taskAssignment.setProgressPercentage(new BigDecimal("0.00")); // 默认进度为0
                }

                // 设置创建人和更新人（暂时使用默认值，实际应该从当前登录用户获取）
                if (taskAssignment.getCreateBy() == null) {
                    taskAssignment.setCreateBy(1L); // 默认创建人ID
                }
                if (taskAssignment.getUpdateBy() == null) {
                    taskAssignment.setUpdateBy(1L); // 默认更新人ID
                }

                taskAssignment.setCreateTime(now);
                taskAssignment.setUpdateTime(now);
                return this.save(taskAssignment);
            } else {
                // 修改
                if (taskAssignment.getUpdateBy() == null) {
                    taskAssignment.setUpdateBy(1L); // 默认更新人ID
                }
                taskAssignment.setUpdateTime(now);
                return this.updateById(taskAssignment);
            }
        } catch (Exception e) {
            log.error("保存任务书失败", e);
            throw new RuntimeException("保存失败：" + e.getMessage());
        }
    }

    @Override
    public TaskAssignment getTaskAssignmentById(Long id) {
        try {
            log.info("根据ID获取任务书详情，ID：{}", id);
            return this.getById(id);
        } catch (Exception e) {
            log.error("获取任务书详情失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public TaskAssignment getTaskAssignmentByTaskNo(String taskNo) {
        try {
            log.info("根据任务书编号获取任务书，任务书编号：{}", taskNo);
            return taskAssignmentMapper.selectByTaskNo(taskNo);
        } catch (Exception e) {
            log.error("根据任务书编号获取任务书失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteTaskAssignment(Long id) {
        try {
            log.info("删除任务书，ID：{}", id);
            return this.removeById(id);
        } catch (Exception e) {
            log.error("删除任务书失败", e);
            throw new RuntimeException("删除失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteTaskAssignment(List<Long> ids) {
        try {
            log.info("批量删除任务书，ID列表：{}", ids);
            if (CollectionUtils.isEmpty(ids)) {
                return false;
            }
            return this.removeByIds(ids);
        } catch (Exception e) {
            log.error("批量删除任务书失败", e);
            throw new RuntimeException("批量删除失败：" + e.getMessage());
        }
    }

    @Override
    public String generateTaskNo() {
        try {
            // 生成任务书编号：TASK + 年月日 + 4位序号
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String dateStr = sdf.format(new Date());
            String prefix = "TASK" + dateStr;

            // 查询当天最大序号
            int maxSeq = taskAssignmentMapper.getMaxSeqByDate(dateStr);
            log.info("当天最大序号：{}", maxSeq);

            String seq = String.format("%04d", maxSeq + 1);
            String taskNo = prefix + seq;
            log.info("生成任务书编号：{}", taskNo);

            return taskNo;
        } catch (Exception e) {
            log.error("生成任务书编号失败", e);
            throw new RuntimeException("生成任务书编号失败：" + e.getMessage());
        }
    }

    @Override
    public boolean existsTaskNo(String taskNo, Long excludeId) {
        try {
            log.info("检查任务书编号是否存在，任务书编号：{}，排除ID：{}", taskNo, excludeId);
            return taskAssignmentMapper.existsTaskNo(taskNo, excludeId);
        } catch (Exception e) {
            log.error("检查任务书编号是否存在失败", e);
            throw new RuntimeException("检查失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateTaskStatus(Long id, Integer taskStatus) {
        try {
            log.info("更新任务状态，ID：{}，任务状态：{}", id, taskStatus);
            
            TaskAssignment task = new TaskAssignment();
            task.setId(id);
            task.setTaskStatus(taskStatus);
            task.setUpdateTime(new Date());
            
            return this.updateById(task);
        } catch (Exception e) {
            log.error("更新任务状态失败", e);
            throw new RuntimeException("更新失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateTaskStatus(List<Long> ids, Integer taskStatus) {
        try {
            log.info("批量更新任务状态，ID列表：{}，任务状态：{}", ids, taskStatus);
            if (CollectionUtils.isEmpty(ids)) {
                return false;
            }
            
            int count = taskAssignmentMapper.batchUpdateTaskStatus(ids, taskStatus, null);
            return count > 0;
        } catch (Exception e) {
            log.error("批量更新任务状态失败", e);
            throw new RuntimeException("批量更新失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateCompletionRate(Long id, BigDecimal completionRate) {
        try {
            log.info("更新完成度，ID：{}，完成度：{}", id, completionRate);
            
            int count = taskAssignmentMapper.updateCompletionRate(id, completionRate);
            return count > 0;
        } catch (Exception e) {
            log.error("更新完成度失败", e);
            throw new RuntimeException("更新失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateCompletionRate(List<Long> ids, BigDecimal completionRate) {
        try {
            log.info("批量更新完成度，ID列表：{}，完成度：{}", ids, completionRate);
            if (CollectionUtils.isEmpty(ids)) {
                return false;
            }
            
            int count = taskAssignmentMapper.batchUpdateCompletionRate(ids, completionRate, null);
            return count > 0;
        } catch (Exception e) {
            log.error("批量更新完成度失败", e);
            throw new RuntimeException("批量更新失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean reviewTask(Long id, Long reviewerId, String reviewerName, String reviewComments, boolean approved) {
        try {
            log.info("审核任务书，ID：{}，审核人：{}，是否通过：{}", id, reviewerName, approved);
            
            TaskAssignment task = new TaskAssignment();
            task.setId(id);
            task.setReviewerId(reviewerId);
            task.setReviewDate(new Date());
            task.setReviewComments(reviewComments);
            task.setTaskStatus(approved ? 3 : 1); // 通过：已审核，不通过：草稿
            task.setUpdateTime(new Date());
            
            return this.updateById(task);
        } catch (Exception e) {
            log.error("审核任务书失败", e);
            throw new RuntimeException("审核失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean issueTask(Long id, Long issuerId, String issuerName, Long receiverId, String receiverName) {
        try {
            log.info("下达任务书，ID：{}，下达人：{}，接收人：{}", id, issuerName, receiverName);
            
            TaskAssignment task = new TaskAssignment();
            task.setId(id);
            task.setIssuerId(issuerId);
            task.setIssueDate(new Date());
            task.setTaskStatus(4); // 已下达
            task.setUpdateTime(new Date());
            
            return this.updateById(task);
        } catch (Exception e) {
            log.error("下达任务书失败", e);
            throw new RuntimeException("下达失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean receiveTask(Long id, Long receiverId, String receiverName) {
        try {
            log.info("接收任务书，ID：{}，接收人：{}", id, receiverName);
            
            TaskAssignment task = new TaskAssignment();
            task.setId(id);
            task.setAssignedPersonId(receiverId);
            task.setUpdateTime(new Date());
            
            return this.updateById(task);
        } catch (Exception e) {
            log.error("接收任务书失败", e);
            throw new RuntimeException("接收失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean startTask(Long id) {
        try {
            log.info("开始执行任务，ID：{}", id);
            
            TaskAssignment task = new TaskAssignment();
            task.setId(id);
            task.setTaskStatus(5); // 执行中
            task.setActualStartDate(new Date());
            task.setUpdateTime(new Date());
            
            return this.updateById(task);
        } catch (Exception e) {
            log.error("开始执行任务失败", e);
            throw new RuntimeException("开始执行失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean completeTask(Long id) {
        try {
            log.info("完成任务，ID：{}", id);
            
            TaskAssignment task = new TaskAssignment();
            task.setId(id);
            task.setTaskStatus(6); // 已完成
            task.setActualEndDate(new Date());
            task.setProgressPercentage(new BigDecimal("100"));
            task.setUpdateTime(new Date());
            
            return this.updateById(task);
        } catch (Exception e) {
            log.error("完成任务失败", e);
            throw new RuntimeException("完成任务失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean pauseTask(Long id) {
        try {
            log.info("暂停任务，ID：{}", id);
            return updateTaskStatus(id, 7); // 已暂停
        } catch (Exception e) {
            log.error("暂停任务失败", e);
            throw new RuntimeException("暂停任务失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelTask(Long id) {
        try {
            log.info("取消任务，ID：{}", id);
            return updateTaskStatus(id, 8); // 已取消
        } catch (Exception e) {
            log.error("取消任务失败", e);
            throw new RuntimeException("取消任务失败：" + e.getMessage());
        }
    }

    @Override
    public List<TaskAssignment> getPendingReviewTasks() {
        try {
            log.info("获取待审核的任务列表");
            return taskAssignmentMapper.selectPendingReviewTasks();
        } catch (Exception e) {
            log.error("获取待审核的任务列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<TaskAssignment> getIssuedTasks() {
        try {
            log.info("获取已下达的任务列表");
            return taskAssignmentMapper.selectIssuedTasks();
        } catch (Exception e) {
            log.error("获取已下达的任务列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<TaskAssignment> getInProgressTasks() {
        try {
            log.info("获取执行中的任务列表");
            return taskAssignmentMapper.selectInProgressTasks();
        } catch (Exception e) {
            log.error("获取执行中的任务列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<TaskAssignment> getCompletedTasks() {
        try {
            log.info("获取已完成的任务列表");
            return taskAssignmentMapper.selectCompletedTasks();
        } catch (Exception e) {
            log.error("获取已完成的任务列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<TaskAssignment> getDelayedTasks() {
        try {
            log.info("获取延期的任务列表");
            return taskAssignmentMapper.selectDelayedTasks();
        } catch (Exception e) {
            log.error("获取延期的任务列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<TaskAssignment> getExpiringSoonTasks(Integer days) {
        try {
            log.info("获取即将到期的任务列表，天数：{}", days);
            if (days == null || days <= 0) {
                days = 3; // 默认3天
            }
            return taskAssignmentMapper.selectExpiringSoonTasks(days);
        } catch (Exception e) {
            log.error("获取即将到期的任务列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<TaskAssignment> getUrgentTasks() {
        try {
            log.info("获取紧急任务列表");
            return taskAssignmentMapper.selectUrgentTasks();
        } catch (Exception e) {
            log.error("获取紧急任务列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<TaskAssignment> getImportantTasks() {
        try {
            log.info("获取重要任务列表");
            return taskAssignmentMapper.selectImportantTasks();
        } catch (Exception e) {
            log.error("获取重要任务列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<TaskAssignment> getLargeTasks(BigDecimal minAmount) {
        try {
            log.info("获取大额任务列表，最小金额：{}", minAmount);
            if (minAmount == null) {
                minAmount = new BigDecimal("1000000"); // 默认100万
            }
            return taskAssignmentMapper.selectLargeTasks(minAmount);
        } catch (Exception e) {
            log.error("获取大额任务列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<TaskAssignment> getMyResponsibleTasks(Long userId) {
        try {
            log.info("获取我负责的任务列表，用户ID：{}", userId);
            return taskAssignmentMapper.selectMyResponsibleTasks(userId);
        } catch (Exception e) {
            log.error("获取我负责的任务列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<TaskAssignment> getMyReceivedTasks(Long userId) {
        try {
            log.info("获取我接收的任务列表，用户ID：{}", userId);
            return taskAssignmentMapper.selectMyReceivedTasks(userId);
        } catch (Exception e) {
            log.error("获取我接收的任务列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<TaskAssignment> getMyIssuedTasks(Long userId) {
        try {
            log.info("获取我下达的任务列表，用户ID：{}", userId);
            return taskAssignmentMapper.selectMyIssuedTasks(userId);
        } catch (Exception e) {
            log.error("获取我下达的任务列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<TaskAssignment> getTaskAssignmentByProjectId(Long projectId) {
        try {
            log.info("根据项目ID查询任务列表，项目ID：{}", projectId);
            return taskAssignmentMapper.selectByProjectId(projectId);
        } catch (Exception e) {
            log.error("根据项目ID查询任务列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> getTaskAssignmentStatistics(TaskAssignmentQueryParam param) {
        try {
            log.info("统计任务书数据，参数：{}", param);
            return taskAssignmentMapper.selectTaskAssignmentStatistics(param);
        } catch (Exception e) {
            log.error("统计任务书数据失败", e);
            throw new RuntimeException("统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getTaskTypeDistribution(TaskAssignmentQueryParam param) {
        try {
            log.info("获取任务类型分布统计，参数：{}", param);
            return taskAssignmentMapper.selectTaskTypeDistribution(param);
        } catch (Exception e) {
            log.error("获取任务类型分布统计失败", e);
            throw new RuntimeException("统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getTaskStatusDistribution(TaskAssignmentQueryParam param) {
        try {
            log.info("获取任务状态分布统计，参数：{}", param);
            return taskAssignmentMapper.selectTaskStatusDistribution(param);
        } catch (Exception e) {
            log.error("获取任务状态分布统计失败", e);
            throw new RuntimeException("统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getTaskLevelDistribution(TaskAssignmentQueryParam param) {
        try {
            log.info("获取任务等级分布统计，参数：{}", param);
            return taskAssignmentMapper.selectTaskLevelDistribution(param);
        } catch (Exception e) {
            log.error("获取任务等级分布统计失败", e);
            throw new RuntimeException("统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getPriorityDistribution(TaskAssignmentQueryParam param) {
        try {
            log.info("获取优先级分布统计，参数：{}", param);
            return taskAssignmentMapper.selectPriorityDistribution(param);
        } catch (Exception e) {
            log.error("获取优先级分布统计失败", e);
            throw new RuntimeException("统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<Map<String, Object>> getMonthlyTaskTrend(TaskAssignmentQueryParam param) {
        try {
            log.info("获取月度任务趋势，参数：{}", param);
            return taskAssignmentMapper.selectMonthlyTaskTrend(param);
        } catch (Exception e) {
            log.error("获取月度任务趋势失败", e);
            throw new RuntimeException("统计失败：" + e.getMessage());
        }
    }

    @Override
    public List<TaskAssignment> searchTaskAssignments(String keyword, Integer limit) {
        try {
            log.info("模糊搜索任务书，关键词：{}，限制数量：{}", keyword, limit);
            if (limit == null || limit <= 0) {
                limit = 10;
            }
            return taskAssignmentMapper.searchTaskAssignments(keyword, limit);
        } catch (Exception e) {
            log.error("模糊搜索任务书失败", e);
            throw new RuntimeException("搜索失败：" + e.getMessage());
        }
    }

    @Override
    public List<TaskAssignment> getTodayExpiringTasks() {
        try {
            log.info("获取今日到期的任务列表");
            return taskAssignmentMapper.selectTodayExpiringTasks();
        } catch (Exception e) {
            log.error("获取今日到期的任务列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<TaskAssignment> getThisWeekExpiringTasks() {
        try {
            log.info("获取本周到期的任务列表");
            return taskAssignmentMapper.selectThisWeekExpiringTasks();
        } catch (Exception e) {
            log.error("获取本周到期的任务列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    public List<TaskAssignment> getThisMonthExpiringTasks() {
        try {
            log.info("获取本月到期的任务列表");
            return taskAssignmentMapper.selectThisMonthExpiringTasks();
        } catch (Exception e) {
            log.error("获取本月到期的任务列表失败", e);
            throw new RuntimeException("查询失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> importTaskAssignments(List<TaskAssignment> taskList) {
        try {
            log.info("导入任务书，数量：{}", taskList.size());

            Map<String, Object> result = new HashMap<>();
            int successCount = 0;
            int failCount = 0;
            List<String> errorMessages = new ArrayList<>();

            for (TaskAssignment task : taskList) {
                try {
                    // 设置默认值
                    if (!StringUtils.hasText(task.getTaskNo())) {
                        task.setTaskNo(generateTaskNo());
                    }

                    // 检查任务书编号是否重复
                    if (existsTaskNo(task.getTaskNo(), null)) {
                        failCount++;
                        errorMessages.add("任务书编号 " + task.getTaskNo() + " 已存在");
                        continue;
                    }

                    task.setCreateTime(new Date());
                    task.setUpdateTime(new Date());

                    if (this.save(task)) {
                        successCount++;
                    } else {
                        failCount++;
                        errorMessages.add("保存任务书 " + task.getTaskName() + " 失败");
                    }
                } catch (Exception e) {
                    failCount++;
                    errorMessages.add("导入任务书 " + task.getTaskName() + " 异常：" + e.getMessage());
                }
            }

            result.put("total", taskList.size());
            result.put("successCount", successCount);
            result.put("failCount", failCount);
            result.put("errorMessages", errorMessages);

            return result;
        } catch (Exception e) {
            log.error("导入任务书失败", e);
            throw new RuntimeException("导入失败：" + e.getMessage());
        }
    }

    @Override
    public List<TaskAssignment> exportTaskAssignments(TaskAssignmentQueryParam param) {
        try {
            log.info("导出任务书，参数：{}", param);
            return taskAssignmentMapper.selectTaskAssignmentList(param);
        } catch (Exception e) {
            log.error("导出任务书失败", e);
            throw new RuntimeException("导出失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int autoUpdateExpiredTasks() {
        try {
            log.info("自动更新过期任务状态");

            // 获取延期但状态未更新的任务
            List<TaskAssignment> delayedTasks = getDelayedTasks();
            if (CollectionUtils.isEmpty(delayedTasks)) {
                return 0;
            }

            List<Long> ids = new ArrayList<>();
            for (TaskAssignment task : delayedTasks) {
                if (task.getTaskStatus() != null && task.getTaskStatus() == 5) {
                    ids.add(task.getId());
                }
            }

            if (CollectionUtils.isEmpty(ids)) {
                return 0;
            }

            // 批量更新为暂停状态
            batchUpdateTaskStatus(ids, 7);
            return ids.size();
        } catch (Exception e) {
            log.error("自动更新过期任务状态失败", e);
            throw new RuntimeException("更新失败：" + e.getMessage());
        }
    }

    @Override
    public int sendTaskReminders(Integer days) {
        try {
            log.info("发送任务提醒通知，提前天数：{}", days);

            List<TaskAssignment> tasks = getExpiringSoonTasks(days);
            if (CollectionUtils.isEmpty(tasks)) {
                return 0;
            }

            // 这里可以添加发送提醒的逻辑，比如发送邮件、短信等
            // 暂时只记录日志
            for (TaskAssignment task : tasks) {
                log.info("任务 {} 即将到期，计划结束时间：{}", task.getTaskName(), task.getPlannedEndDate());
            }

            return tasks.size();
        } catch (Exception e) {
            log.error("发送任务提醒通知失败", e);
            throw new RuntimeException("发送提醒失败：" + e.getMessage());
        }
    }
}
