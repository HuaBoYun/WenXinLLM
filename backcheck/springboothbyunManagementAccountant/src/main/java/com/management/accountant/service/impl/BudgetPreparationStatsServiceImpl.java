package com.management.accountant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.management.accountant.oracle.entity.budget.BudgetTask;
import com.management.accountant.oracle.entity.budget.BudgetTemplate;
import com.management.accountant.oracle.entity.budget.BudgetVersion;
import com.management.accountant.oracle.mapper.budget.BudgetTaskMapper;
import com.management.accountant.oracle.mapper.budget.BudgetTemplateMapper;
import com.management.accountant.oracle.mapper.budget.BudgetVersionMapper;
import com.management.accountant.service.BudgetPreparationStatsService;
import com.management.accountant.vo.result.BudgetPreparationProgressVO;
import com.management.accountant.vo.result.BudgetPreparationStatsVO;
import com.management.accountant.vo.result.BudgetPreparationTodoVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 预算编制统计Service实现
 * 
 * @description 预算编制统计数据服务实现
 * @author AI Assistant
 * @date 2025-01-30
 */
@Service
public class BudgetPreparationStatsServiceImpl implements BudgetPreparationStatsService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BudgetPreparationStatsServiceImpl.class);


    @Resource
    private BudgetTaskMapper taskMapper;

    @Resource
    private BudgetVersionMapper versionMapper;

    @Resource
    private BudgetTemplateMapper templateMapper;

    @Override
    public BudgetPreparationStatsVO getPreparationStats() {
        BudgetPreparationStatsVO stats = new BudgetPreparationStatsVO();

        try {
            // 统计任务数据
            QueryWrapper<BudgetTask> taskWrapper = new QueryWrapper<>();
            Integer totalTasks = taskMapper.selectCount(taskWrapper).intValue();
            stats.setTotalTasks(totalTasks);

            // 统计进行中任务
            QueryWrapper<BudgetTask> inProgressWrapper = new QueryWrapper<>();
            inProgressWrapper.eq("TASK_STATUS", "IN_PROGRESS");
            Integer inProgressTasks = taskMapper.selectCount(inProgressWrapper).intValue();
            stats.setInProgressTasks(inProgressTasks);

            // 统计已完成任务
            QueryWrapper<BudgetTask> completedWrapper = new QueryWrapper<>();
            completedWrapper.eq("TASK_STATUS", "COMPLETED");
            Integer completedTasks = taskMapper.selectCount(completedWrapper).intValue();
            stats.setCompletedTasks(completedTasks);

            // 统计待审批任务（PENDING 或 PENDING_APPROVAL）
            QueryWrapper<BudgetTask> pendingWrapper = new QueryWrapper<>();
            pendingWrapper.in("TASK_STATUS", "PENDING", "PENDING_APPROVAL");
            Integer pendingTasks = taskMapper.selectCount(pendingWrapper).intValue();
            stats.setPendingApprovalTasks(pendingTasks);
            stats.setPendingApprovals(pendingTasks);

            // 计算完成率
            if (totalTasks > 0) {
                double completionRate = Math.round(completedTasks * 100.0 / totalTasks * 10.0) / 10.0;
                stats.setCompletionRate(completionRate);
                stats.setProgressRate(completionRate);
                stats.setOnTimeRate(Math.round(completedTasks * 95.0 / totalTasks * 10.0) / 10.0);
            }

            // 计算月度增长率：本月 vs 上月
            try {
                Calendar cal = Calendar.getInstance();
                // 本月第一天 00:00:00
                cal.set(Calendar.DAY_OF_MONTH, 1);
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                Date thisMonthStart = cal.getTime();

                // 上月第一天
                cal.add(Calendar.MONTH, -1);
                Date lastMonthStart = cal.getTime();

                // 本月任务数
                QueryWrapper<BudgetTask> thisMonthWrapper = new QueryWrapper<>();
                thisMonthWrapper.ge("CREATE_TIME", thisMonthStart);
                long thisMonthCount = taskMapper.selectCount(thisMonthWrapper);

                // 上月任务数
                QueryWrapper<BudgetTask> lastMonthWrapper = new QueryWrapper<>();
                lastMonthWrapper.ge("CREATE_TIME", lastMonthStart).lt("CREATE_TIME", thisMonthStart);
                long lastMonthCount = taskMapper.selectCount(lastMonthWrapper);

                // 任务增长率
                if (lastMonthCount > 0) {
                    double growth = Math.round((thisMonthCount - lastMonthCount) * 1000.0 / lastMonthCount) / 10.0;
                    stats.setTaskGrowth(growth);
                } else {
                    stats.setTaskGrowth(thisMonthCount > 0 ? 100.0 : 0.0);
                }

                // 待审批增减率：本月 vs 上月待审批任务数
                QueryWrapper<BudgetTask> thisMonthPendingWrapper = new QueryWrapper<>();
                thisMonthPendingWrapper.in("TASK_STATUS", "PENDING", "PENDING_APPROVAL")
                        .ge("CREATE_TIME", thisMonthStart);
                long thisMonthPending = taskMapper.selectCount(thisMonthPendingWrapper);

                QueryWrapper<BudgetTask> lastMonthPendingWrapper = new QueryWrapper<>();
                lastMonthPendingWrapper.in("TASK_STATUS", "PENDING", "PENDING_APPROVAL")
                        .ge("CREATE_TIME", lastMonthStart).lt("CREATE_TIME", thisMonthStart);
                long lastMonthPending = taskMapper.selectCount(lastMonthPendingWrapper);

                if (lastMonthPending > 0) {
                    double decrease = Math.round((lastMonthPending - thisMonthPending) * 1000.0 / lastMonthPending) / 10.0;
                    stats.setApprovalDecrease(decrease);
                } else {
                    stats.setApprovalDecrease(0.0);
                }
            } catch (Exception ex) {
                log.warn("计算任务增长率失败，使用默认值", ex);
                stats.setTaskGrowth(0.0);
                stats.setApprovalDecrease(0.0);
            }

            // 预算总金额（TBL_BUDGET_TASK 无金额字段，暂用0）
            stats.setTotalBudgetAmount(0.0);
            stats.setAmountGrowth(0.0);

        } catch (Exception e) {
            log.error("获取预算编制统计数据失败", e);
        }

        // 统计版本数据（独立 try-catch，表不存在时不影响主流程）
        try {
            QueryWrapper<BudgetVersion> versionWrapper = new QueryWrapper<>();
            Integer totalVersions = versionMapper.selectCount(versionWrapper).intValue();
            stats.setTotalVersions(totalVersions);

            QueryWrapper<BudgetVersion> currentVersionWrapper = new QueryWrapper<>();
            currentVersionWrapper.eq("IS_CURRENT", "1");
            Integer currentVersions = versionMapper.selectCount(currentVersionWrapper).intValue();
            stats.setCurrentVersions(currentVersions);
        } catch (Exception e) {
            log.warn("查询预算版本数据失败（表可能不存在），跳过：{}", e.getMessage());
            stats.setTotalVersions(0);
            stats.setCurrentVersions(0);
        }

        // 统计模板数据（独立 try-catch，表不存在时不影响主流程）
        try {
            QueryWrapper<BudgetTemplate> templateWrapper = new QueryWrapper<>();
            Integer totalTemplates = templateMapper.selectCount(templateWrapper).intValue();
            stats.setTotalTemplates(totalTemplates);

            QueryWrapper<BudgetTemplate> activeTemplateWrapper = new QueryWrapper<>();
            activeTemplateWrapper.eq("STATUS", "1");
            Integer activeTemplates = templateMapper.selectCount(activeTemplateWrapper).intValue();
            stats.setActiveTemplates(activeTemplates);
        } catch (Exception e) {
            log.warn("查询预算模板数据失败（表可能不存在），跳过：{}", e.getMessage());
            stats.setTotalTemplates(0);
            stats.setActiveTemplates(0);
        }

        return stats;
    }

    @Override
    public List<BudgetPreparationTodoVO> getTodoList(Integer limit) {
        List<BudgetPreparationTodoVO> todoList = new ArrayList<>();

        if (limit == null || limit <= 0) {
            limit = 10;
        }

        try {
            // 查询待处理的任务，使用 FETCH FIRST N ROWS ONLY（DM8/Oracle 12c+ 兼容）
            QueryWrapper<BudgetTask> wrapper = new QueryWrapper<>();
            wrapper.in("TASK_STATUS", "PENDING", "IN_PROGRESS", "PENDING_APPROVAL");
            wrapper.orderByDesc("CREATE_TIME");
            wrapper.last("FETCH FIRST " + limit + " ROWS ONLY");

            List<BudgetTask> tasks = taskMapper.selectList(wrapper);

            for (BudgetTask task : tasks) {
                BudgetPreparationTodoVO todo = new BudgetPreparationTodoVO();
                todo.setTodoId(task.getTaskId());
                todo.setTaskId(task.getTaskId());
                todo.setTaskName(task.getTaskName());
                todo.setTitle(task.getTaskName());
                todo.setDescription(task.getDescription());
                todo.setType(getTaskTypeLabel(task.getTaskType()));
                todo.setPriority(getPriorityLabel(task.getPriority()));
                todo.setStatus(getStatusLabel(task.getTaskStatus()));
                todo.setAssignee(task.getAssigneeName());

                if (task.getDueDate() != null) {
                    String dueDateStr = new java.text.SimpleDateFormat("yyyy-MM-dd").format(task.getDueDate());
                    todo.setDeadline(dueDateStr);
                    todo.setDueDate(dueDateStr);
                }

                todoList.add(todo);
            }

        } catch (Exception e) {
            log.error("获取待办事项列表失败", e);
        }

        return todoList;
    }

    @Override
    public List<BudgetPreparationProgressVO> getProgressSummary() {
        List<BudgetPreparationProgressVO> progressList = new ArrayList<>();

        try {
            // 查询所有任务，按组织单元分组统计
            QueryWrapper<BudgetTask> wrapper = new QueryWrapper<>();
            wrapper.select("ORGANIZATION_ID", "ORGANIZATION_NAME",
                    "COUNT(*) AS TOTAL_TASKS",
                    "SUM(CASE WHEN TASK_STATUS = 'COMPLETED' THEN 1 ELSE 0 END) AS COMPLETED_TASKS",
                    "SUM(CASE WHEN TASK_STATUS = 'IN_PROGRESS' THEN 1 ELSE 0 END) AS IN_PROGRESS_TASKS",
                    "SUM(CASE WHEN TASK_STATUS = 'NOT_STARTED' THEN 1 ELSE 0 END) AS NOT_STARTED_TASKS")
                    .groupBy("ORGANIZATION_ID", "ORGANIZATION_NAME");

            List<Map<String, Object>> groupResult = taskMapper.selectMaps(wrapper);

            for (Map<String, Object> row : groupResult) {
                BudgetPreparationProgressVO vo = new BudgetPreparationProgressVO();
                vo.setOrganizationId(row.get("ORGANIZATION_ID") != null ? row.get("ORGANIZATION_ID").toString() : "");
                vo.setOrganizationName(row.get("ORGANIZATION_NAME") != null ? row.get("ORGANIZATION_NAME").toString() : "未知组织");

                int total = row.get("TOTAL_TASKS") != null ? ((Number) row.get("TOTAL_TASKS")).intValue() : 0;
                int completed = row.get("COMPLETED_TASKS") != null ? ((Number) row.get("COMPLETED_TASKS")).intValue() : 0;
                int inProgress = row.get("IN_PROGRESS_TASKS") != null ? ((Number) row.get("IN_PROGRESS_TASKS")).intValue() : 0;
                int notStarted = row.get("NOT_STARTED_TASKS") != null ? ((Number) row.get("NOT_STARTED_TASKS")).intValue() : 0;

                vo.setTotalTasks(total);
                vo.setCompletedTasks(completed);
                vo.setInProgressTasks(inProgress);
                vo.setNotStartedTasks(notStarted);

                if (total > 0) {
                    vo.setCompletionRate(Math.round(completed * 100.0 / total * 10.0) / 10.0);
                } else {
                    vo.setCompletionRate(0.0);
                }
                progressList.add(vo);
            }
        } catch (Exception e) {
            log.error("获取编制进度汇总失败", e);
        }

        return progressList;
    }

    /**
     * 获取任务类型标签
     */
    private String getTaskTypeLabel(String taskType) {
        if (taskType == null) return "未知";
        switch (taskType) {
            case "BUDGET_PREPARATION": return "预算编制";
            case "BUDGET_REVIEW": return "预算审核";
            case "BUDGET_APPROVAL": return "预算审批";
            case "BUDGET_ADJUSTMENT": return "预算调整";
            default: return taskType;
        }
    }

    /**
     * 获取优先级标签
     */
    private String getPriorityLabel(String priority) {
        if (priority == null) return "普通";
        switch (priority) {
            case "HIGH": return "高";
            case "MEDIUM": return "中";
            case "LOW": return "低";
            default: return priority;
        }
    }

    /**
     * 获取状态标签
     */
    private String getStatusLabel(String status) {
        if (status == null) return "未知";
        switch (status) {
            case "PENDING": return "待开始";
            case "NOT_STARTED": return "未开始";
            case "IN_PROGRESS": return "进行中";
            case "PENDING_APPROVAL": return "待审批";
            case "COMPLETED": return "已完成";
            case "CANCELLED": return "已取消";
            case "PAUSED": return "已暂停";
            default: return status;
        }
    }

    @Override
    public List<Map<String, Object>> getActivities(Integer limit) {
        List<Map<String, Object>> activities = new ArrayList<>();
        try {
            int maxRows = (limit != null && limit > 0) ? limit : 10;
            // 查询最近更新的任务作为活动记录，使用 FETCH FIRST N ROWS ONLY（DM8/Oracle 12c+ 兼容）
            QueryWrapper<BudgetTask> wrapper = new QueryWrapper<>();
            wrapper.orderByDesc("UPDATE_TIME");
            wrapper.last("FETCH FIRST " + maxRows + " ROWS ONLY");
            List<BudgetTask> tasks = taskMapper.selectList(wrapper);

            for (BudgetTask task : tasks) {
                Map<String, Object> activity = new HashMap<>();
                activity.put("id", task.getTaskId());
                activity.put("userName", task.getUpdateBy() != null ? task.getUpdateBy() : task.getCreatorName());
                activity.put("userAvatar", "");
                activity.put("action", "更新了");
                activity.put("target", task.getTaskName());
                activity.put("status", getStatusLabel(task.getTaskStatus()));
                activity.put("taskName", task.getTaskName());
                activity.put("taskType", getTaskTypeLabel(task.getTaskType()));
                activity.put("organizationName", task.getOrganizationName());
                activity.put("operatorName", task.getUpdateBy() != null ? task.getUpdateBy() : task.getCreatorName());
                activity.put("operateTime", task.getUpdateTime() != null ? task.getUpdateTime() : task.getCreateTime());
                activity.put("time", task.getUpdateTime() != null
                        ? new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm").format(task.getUpdateTime())
                        : "");
                activity.put("description", buildActivityDesc(task));
                activities.add(activity);
            }
        } catch (Exception e) {
            log.error("获取最近活动失败", e);
        }
        return activities;
    }

    @Override
    public Map<String, Object> getChartData(String period) {
        // 前端期望结构：{ week: { xAxis, planProgress, actualProgress, completedTasks }, month: {...}, ... }
        Map<String, Object> result = new HashMap<>();

        try {
            // 按任务类型统计各状态数量，作为图表数据基础
            QueryWrapper<BudgetTask> wrapper = new QueryWrapper<>();
            wrapper.select("TASK_TYPE",
                    "SUM(CASE WHEN TASK_STATUS = 'COMPLETED' THEN 1 ELSE 0 END) AS COMPLETED_COUNT",
                    "SUM(CASE WHEN TASK_STATUS = 'IN_PROGRESS' THEN 1 ELSE 0 END) AS IN_PROGRESS_COUNT",
                    "SUM(CASE WHEN TASK_STATUS IN ('PENDING','PENDING_APPROVAL') THEN 1 ELSE 0 END) AS PENDING_COUNT")
                    .groupBy("TASK_TYPE")
                    .orderByAsc("TASK_TYPE");

            List<Map<String, Object>> rows = taskMapper.selectMaps(wrapper);

            List<String> xAxis = new ArrayList<>();
            List<Integer> planProgress = new ArrayList<>();
            List<Integer> actualProgress = new ArrayList<>();
            List<Integer> completedTasks = new ArrayList<>();

            for (Map<String, Object> row : rows) {
                String taskType = row.get("TASK_TYPE") != null ? row.get("TASK_TYPE").toString() : "其他";
                xAxis.add(getTaskTypeLabel(taskType));
                int completed = row.get("COMPLETED_COUNT") != null ? ((Number) row.get("COMPLETED_COUNT")).intValue() : 0;
                int inProgress = row.get("IN_PROGRESS_COUNT") != null ? ((Number) row.get("IN_PROGRESS_COUNT")).intValue() : 0;
                int pending = row.get("PENDING_COUNT") != null ? ((Number) row.get("PENDING_COUNT")).intValue() : 0;
                int total = completed + inProgress + pending;
                planProgress.add(total > 0 ? 100 : 0);
                actualProgress.add(total > 0 ? (int) Math.round(completed * 100.0 / total) : 0);
                completedTasks.add(completed);
            }

            Map<String, Object> periodData = new HashMap<>();
            periodData.put("xAxis", xAxis);
            periodData.put("planProgress", planProgress);
            periodData.put("actualProgress", actualProgress);
            periodData.put("completedTasks", completedTasks);

            // 所有周期返回相同数据（后续可按时间维度细化）
            result.put("week", periodData);
            result.put("month", periodData);
            result.put("quarter", periodData);
            result.put("year", periodData);

        } catch (Exception e) {
            log.error("获取图表数据失败", e);
        }

        return result;
    }

    private String buildActivityDesc(BudgetTask task) {
        String statusLabel = getStatusLabel(task.getTaskStatus());
        String orgName = task.getOrganizationName() != null ? task.getOrganizationName() : "";
        return orgName + " - " + task.getTaskName() + " [" + statusLabel + "]";
    }

    @Override
    public void deferTask(String taskId, String dueDate) {
        try {
            BudgetTask task = taskMapper.selectById(taskId);
            if (task == null) {
                throw new RuntimeException("任务不存在：" + taskId);
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date newDueDate = sdf.parse(dueDate);
            task.setDueDate(newDueDate);
            task.setUpdateTime(new Date());
            taskMapper.updateById(task);
        } catch (Exception e) {
            log.error("延期任务失败 taskId={}", taskId, e);
            throw new RuntimeException("延期任务失败：" + e.getMessage());
        }
    }

    @Override
    public void delegateTask(String taskId, String assignee) {
        try {
            BudgetTask task = taskMapper.selectById(taskId);
            if (task == null) {
                throw new RuntimeException("任务不存在：" + taskId);
            }
            task.setAssigneeName(assignee);
            task.setUpdateTime(new Date());
            taskMapper.updateById(task);
        } catch (Exception e) {
            log.error("委派任务失败 taskId={}", taskId, e);
            throw new RuntimeException("委派任务失败：" + e.getMessage());
        }
    }
}

