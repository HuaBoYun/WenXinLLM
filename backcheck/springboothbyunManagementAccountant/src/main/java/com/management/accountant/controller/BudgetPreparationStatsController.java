package com.management.accountant.controller;

import com.management.accountant.service.BudgetPreparationStatsService;
import com.management.accountant.util.MyJsonBean;
import com.management.accountant.vo.result.BudgetPreparationProgressVO;
import com.management.accountant.vo.result.BudgetPreparationStatsVO;
import com.management.accountant.vo.result.BudgetPreparationTodoVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 预算编制统计Controller
 *
 * @description 预算编制统计数据接口
 * @author AI Assistant
 * @date 2025-01-30
 */
@RestController
@Api(tags = {"NCV65全面预算-预算编制统计"})
@RequestMapping(value = "/accountant/budget/preparation")
public class BudgetPreparationStatsController {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(BudgetPreparationStatsController.class);


    @Resource
    private BudgetPreparationStatsService budgetPreparationStatsService;

    /**
     * 获取预算编制统计数据
     */
    @Operation(summary = "获取预算编制统计数据")
    @ApiOperation("获取预算编制统计数据")
    @GetMapping("/stats")
    public MyJsonBean<BudgetPreparationStatsVO> getPreparationStats() {
        MyJsonBean<BudgetPreparationStatsVO> result = new MyJsonBean<>();
        try {
            BudgetPreparationStatsVO stats = budgetPreparationStatsService.getPreparationStats();
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(stats);
        } catch (Exception e) {
            log.error("获取预算编制统计数据失败", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取待办事项列表
     */
    @Operation(summary = "获取待办事项列表")
    @ApiOperation("获取待办事项列表")
    @GetMapping("/todos")
    public MyJsonBean<List<BudgetPreparationTodoVO>> getTodoList(
            @ApiParam(value = "限制数量", required = false) @RequestParam(required = false, defaultValue = "10") Integer limit) {
        MyJsonBean<List<BudgetPreparationTodoVO>> result = new MyJsonBean<>();
        try {
            List<BudgetPreparationTodoVO> todoList = budgetPreparationStatsService.getTodoList(limit);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(todoList);
        } catch (Exception e) {
            log.error("获取待办事项列表失败", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取编制进度汇总
     * 返回前端期望的 { summary: { overall, dataEntry, approval, adjustment }, modules: { ... } } 结构
     */
    @Operation(summary = "获取编制进度汇总")
    @ApiOperation("获取编制进度汇总")
    @GetMapping("/progress")
    public MyJsonBean<Map<String, Object>> getProgressSummary() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            List<BudgetPreparationProgressVO> progressList = budgetPreparationStatsService.getProgressSummary();

            // 汇总 summary（取所有组织的平均完成率）
            Map<String, Object> summary = new java.util.HashMap<>();
            double overallRate = 0.0;
            if (!progressList.isEmpty()) {
                double sum = 0;
                for (BudgetPreparationProgressVO vo : progressList) {
                    sum += vo.getCompletionRate() != null ? vo.getCompletionRate() : 0;
                }
                overallRate = Math.round(sum / progressList.size() * 10.0) / 10.0;
            }
            summary.put("overall", overallRate);
            summary.put("dataEntry", overallRate);
            summary.put("approval", overallRate);
            summary.put("adjustment", overallRate);

            // modules：以组织名称为 key
            Map<String, Object> modules = new java.util.HashMap<>();
            for (BudgetPreparationProgressVO vo : progressList) {
                Map<String, Object> moduleData = new java.util.HashMap<>();
                moduleData.put("taskCount", vo.getTotalTasks());
                moduleData.put("completedTasks", vo.getCompletedTasks());
                moduleData.put("completionRate", vo.getCompletionRate());
                moduleData.put("lastUpdate", "-");
                String key = vo.getOrganizationId() != null && !vo.getOrganizationId().isEmpty()
                        ? vo.getOrganizationId() : vo.getOrganizationName();
                modules.put(key, moduleData);
            }

            Map<String, Object> data = new java.util.HashMap<>();
            data.put("summary", summary);
            data.put("modules", modules);
            data.put("list", progressList);

            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(data);
        } catch (Exception e) {
            log.error("获取编制进度汇总失败", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取最近活动
     */
    @Operation(summary = "获取最近活动")
    @ApiOperation("获取最近活动")
    @GetMapping("/activities")
    public MyJsonBean<List<Map<String, Object>>> getActivities(
            @ApiParam(value = "限制数量", required = false) @RequestParam(required = false, defaultValue = "10") Integer limit) {
        MyJsonBean<List<Map<String, Object>>> result = new MyJsonBean<>();
        try {
            List<Map<String, Object>> activities = budgetPreparationStatsService.getActivities(limit);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(activities);
        } catch (Exception e) {
            log.error("获取最近活动失败", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取图表数据
     */
    @Operation(summary = "获取图表数据")
    @ApiOperation("获取图表数据")
    @GetMapping("/chart")
    public MyJsonBean<Map<String, Object>> getChartData(
            @ApiParam(value = "周期类型", required = false) @RequestParam(required = false, defaultValue = "month") String period) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> chartData = budgetPreparationStatsService.getChartData(period);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(chartData);
        } catch (Exception e) {
            log.error("获取图表数据失败", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 延期任务
     */
    @Operation(summary = "延期任务")
    @ApiOperation("延期任务")
    @PostMapping("/todos/{taskId}/defer")
    public MyJsonBean<Void> deferTodo(
            @ApiParam(value = "任务ID") @PathVariable String taskId,
            @ApiParam(value = "新截止日期") @RequestParam String dueDate) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            budgetPreparationStatsService.deferTask(taskId, dueDate);
            result.setCode(1);
            result.setMsg("延期成功");
        } catch (Exception e) {
            log.error("延期任务失败 taskId={}", taskId, e);
            result.setCode(0);
            result.setMsg("延期失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 委派任务
     */
    @Operation(summary = "委派任务")
    @ApiOperation("委派任务")
    @PostMapping("/todos/{taskId}/delegate")
    public MyJsonBean<Void> delegateTodo(
            @ApiParam(value = "任务ID") @PathVariable String taskId,
            @ApiParam(value = "委派给的人员") @RequestParam String assignee) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            budgetPreparationStatsService.delegateTask(taskId, assignee);
            result.setCode(1);
            result.setMsg("委派成功");
        } catch (Exception e) {
            log.error("委派任务失败 taskId={}", taskId, e);
            result.setCode(0);
            result.setMsg("委派失败：" + e.getMessage());
        }
        return result;
    }
}

