package com.management.accountant.controller.ncv65;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.management.accountant.common.Result;
import com.management.accountant.entity.ncv65.BudgetTask;
import com.management.accountant.service.ncv65.IBudgetTaskService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.Map;

/**
 * NCV65全面预算系统 - 预算任务控制器
 * 
 * @description 预算任务管理API接口，支持任务完整生命周期管理
 * @author AI Assistant
 * @date 2025-01-08
 * @version 1.0.0
 */
@Slf4j
@Api(tags = "NCV65-预算任务管理")
@RestController
@RequestMapping("/budget/task")
@Validated
public class BudgetTaskController {

    @Resource
    private IBudgetTaskService budgetTaskService;

    /**
     * 创建任务
     */
    @ApiOperation("创建任务")
    @PostMapping
    public Result<Boolean> createTask(@Valid @RequestBody BudgetTask task) {
        try {
            boolean result = budgetTaskService.createTask(task);
            return Result.success(result, "创建任务成功");
        } catch (Exception e) {
            log.error("创建任务失败：{}", e.getMessage(), e);
            return Result.error("创建任务失败：" + e.getMessage());
        }
    }

    /**
     * 更新任务
     */
    @ApiOperation("更新任务")
    @PutMapping("/{id}")
    public Result<Boolean> updateTask(
            @ApiParam("任务ID") @PathVariable @NotBlank String id,
            @Valid @RequestBody BudgetTask task) {
        try {
            task.setId(id);
            boolean result = budgetTaskService.updateTask(task);
            return Result.success(result, "更新任务成功");
        } catch (Exception e) {
            log.error("更新任务失败：{}", e.getMessage(), e);
            return Result.error("更新任务失败：" + e.getMessage());
        }
    }

    /**
     * 删除任务
     */
    @ApiOperation("删除任务")
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteTask(@ApiParam("任务ID") @PathVariable @NotBlank String id) {
        try {
            boolean result = budgetTaskService.deleteTask(id);
            return Result.success(result, "删除任务成功");
        } catch (Exception e) {
            log.error("删除任务失败：{}", e.getMessage(), e);
            return Result.error("删除任务失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除任务
     */
    @ApiOperation("批量删除任务")
    @DeleteMapping("/batch")
    public Result<Boolean> batchDeleteTasks(@RequestBody @NotEmpty List<String> ids) {
        try {
            boolean result = budgetTaskService.batchDeleteTasks(ids);
            return Result.success(result, "批量删除任务成功");
        } catch (Exception e) {
            log.error("批量删除任务失败：{}", e.getMessage(), e);
            return Result.error("批量删除任务失败：" + e.getMessage());
        }
    }

    /**
     * 查询任务详情
     */
    @ApiOperation("查询任务详情")
    @GetMapping("/{id}")
    public Result<BudgetTask> getTask(@ApiParam("任务ID") @PathVariable @NotBlank String id) {
        try {
            BudgetTask task = budgetTaskService.getTaskById(id);
            return Result.success(task, "查询任务详情成功");
        } catch (Exception e) {
            log.error("查询任务详情失败：{}", e.getMessage(), e);
            return Result.error("查询任务详情失败：" + e.getMessage());
        }
    }

    /**
     * 分页查询任务列表
     */
    @ApiOperation("分页查询任务列表")
    @PostMapping("/page")
    public Result<IPage<BudgetTask>> getTaskPage(
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Long current,
            @ApiParam("页大小") @RequestParam(defaultValue = "10") Long size,
            @RequestBody(required = false) Map<String, Object> params) {
        try {
            IPage<BudgetTask> page = budgetTaskService.getTaskPage(current, size, params);
            return Result.success(page, "查询任务列表成功");
        } catch (Exception e) {
            log.error("查询任务列表失败：{}", e.getMessage(), e);
            return Result.error("查询任务列表失败：" + e.getMessage());
        }
    }

    /**
     * 根据任务编码查询任务
     */
    @ApiOperation("根据任务编码查询任务")
    @GetMapping("/code/{taskCode}")
    public Result<BudgetTask> getTaskByCode(
            @ApiParam("任务编码") @PathVariable @NotBlank String taskCode) {
        try {
            BudgetTask task = budgetTaskService.getTaskByCode(taskCode);
            return Result.success(task, "查询任务成功");
        } catch (Exception e) {
            log.error("根据编码查询任务失败：{}", e.getMessage(), e);
            return Result.error("根据编码查询任务失败：" + e.getMessage());
        }
    }

    /**
     * 根据任务类型查询任务列表
     */
    @ApiOperation("根据任务类型查询任务列表")
    @GetMapping("/type/{taskType}")
    public Result<List<BudgetTask>> getTasksByType(
            @ApiParam("任务类型") @PathVariable @NotBlank String taskType) {
        try {
            List<BudgetTask> tasks = budgetTaskService.getTasksByType(taskType);
            return Result.success(tasks, "查询任务列表成功");
        } catch (Exception e) {
            log.error("根据类型查询任务列表失败：{}", e.getMessage(), e);
            return Result.error("根据类型查询任务列表失败：" + e.getMessage());
        }
    }

    /**
     * 根据任务状态查询任务列表
     */
    @ApiOperation("根据任务状态查询任务列表")
    @GetMapping("/status/{taskStatus}")
    public Result<List<BudgetTask>> getTasksByStatus(
            @ApiParam("任务状态") @PathVariable @NotBlank String taskStatus) {
        try {
            List<BudgetTask> tasks = budgetTaskService.getTasksByStatus(taskStatus);
            return Result.success(tasks, "查询任务列表成功");
        } catch (Exception e) {
            log.error("根据状态查询任务列表失败：{}", e.getMessage(), e);
            return Result.error("根据状态查询任务列表失败：" + e.getMessage());
        }
    }

    /**
     * 查询启用的任务列表
     */
    @ApiOperation("查询启用的任务列表")
    @GetMapping("/enabled")
    public Result<List<BudgetTask>> getEnabledTasks() {
        try {
            List<BudgetTask> tasks = budgetTaskService.getEnabledTasks();
            return Result.success(tasks, "查询启用的任务列表成功");
        } catch (Exception e) {
            log.error("查询启用的任务列表失败：{}", e.getMessage(), e);
            return Result.error("查询启用的任务列表失败：" + e.getMessage());
        }
    }

    /**
     * 查询待审批的任务列表
     */
    @ApiOperation("查询待审批的任务列表")
    @GetMapping("/pending-approval")
    public Result<List<BudgetTask>> getPendingApprovalTasks() {
        try {
            List<BudgetTask> tasks = budgetTaskService.getPendingApprovalTasks();
            return Result.success(tasks, "查询待审批的任务列表成功");
        } catch (Exception e) {
            log.error("查询待审批的任务列表失败：{}", e.getMessage(), e);
            return Result.error("查询待审批的任务列表失败：" + e.getMessage());
        }
    }

    /**
     * 查询已完成的任务列表
     */
    @ApiOperation("查询已完成的任务列表")
    @GetMapping("/completed")
    public Result<List<BudgetTask>> getCompletedTasks() {
        try {
            List<BudgetTask> tasks = budgetTaskService.getCompletedTasks();
            return Result.success(tasks, "查询已完成的任务列表成功");
        } catch (Exception e) {
            log.error("查询已完成的任务列表失败：{}", e.getMessage(), e);
            return Result.error("查询已完成的任务列表失败：" + e.getMessage());
        }
    }

    /**
     * 查询超期的任务列表
     */
    @ApiOperation("查询超期的任务列表")
    @GetMapping("/overdue")
    public Result<List<BudgetTask>> getOverdueTasks() {
        try {
            List<BudgetTask> tasks = budgetTaskService.getOverdueTasks();
            return Result.success(tasks, "查询超期的任务列表成功");
        } catch (Exception e) {
            log.error("查询超期的任务列表失败：{}", e.getMessage(), e);
            return Result.error("查询超期的任务列表失败：" + e.getMessage());
        }
    }

    /**
     * 启用任务
     */
    @ApiOperation("启用任务")
    @PostMapping("/{id}/enable")
    public Result<Boolean> enableTask(@ApiParam("任务ID") @PathVariable @NotBlank String id) {
        try {
            boolean result = budgetTaskService.enableTask(id);
            return Result.success(result, "启用任务成功");
        } catch (Exception e) {
            log.error("启用任务失败：{}", e.getMessage(), e);
            return Result.error("启用任务失败：" + e.getMessage());
        }
    }

    /**
     * 禁用任务
     */
    @ApiOperation("禁用任务")
    @PostMapping("/{id}/disable")
    public Result<Boolean> disableTask(@ApiParam("任务ID") @PathVariable @NotBlank String id) {
        try {
            boolean result = budgetTaskService.disableTask(id);
            return Result.success(result, "禁用任务成功");
        } catch (Exception e) {
            log.error("禁用任务失败：{}", e.getMessage(), e);
            return Result.error("禁用任务失败：" + e.getMessage());
        }
    }

    /**
     * 启动任务
     */
    @ApiOperation("启动任务")
    @PostMapping("/{id}/start")
    public Result<Boolean> startTask(@ApiParam("任务ID") @PathVariable @NotBlank String id) {
        try {
            boolean result = budgetTaskService.startTask(id);
            return Result.success(result, "启动任务成功");
        } catch (Exception e) {
            log.error("启动任务失败：{}", e.getMessage(), e);
            return Result.error("启动任务失败：" + e.getMessage());
        }
    }

    /**
     * 完成任务
     */
    @ApiOperation("完成任务")
    @PostMapping("/{id}/complete")
    public Result<Boolean> completeTask(@ApiParam("任务ID") @PathVariable @NotBlank String id) {
        try {
            boolean result = budgetTaskService.completeTask(id);
            return Result.success(result, "完成任务成功");
        } catch (Exception e) {
            log.error("完成任务失败：{}", e.getMessage(), e);
            return Result.error("完成任务失败：" + e.getMessage());
        }
    }

    /**
     * 取消任务
     */
    @ApiOperation("取消任务")
    @PostMapping("/{id}/cancel")
    public Result<Boolean> cancelTask(@ApiParam("任务ID") @PathVariable @NotBlank String id) {
        try {
            boolean result = budgetTaskService.cancelTask(id);
            return Result.success(result, "取消任务成功");
        } catch (Exception e) {
            log.error("取消任务失败：{}", e.getMessage(), e);
            return Result.error("取消任务失败：" + e.getMessage());
        }
    }

    /**
     * 重置任务
     */
    @ApiOperation("重置任务")
    @PostMapping("/{id}/reset")
    public Result<Boolean> resetTask(@ApiParam("任务ID") @PathVariable @NotBlank String id) {
        try {
            boolean result = budgetTaskService.resetTask(id);
            return Result.success(result, "重置任务成功");
        } catch (Exception e) {
            log.error("重置任务失败：{}", e.getMessage(), e);
            return Result.error("重置任务失败：" + e.getMessage());
        }
    }

    /**
     * 分配任务
     */
    @ApiOperation("分配任务")
    @PostMapping("/{id}/assign")
    public Result<Boolean> assignTask(
            @ApiParam("任务ID") @PathVariable @NotBlank String id,
            @RequestBody Map<String, String> params) {
        try {
            String assigneeId = params.get("assigneeId");
            String assigneeName = params.get("assigneeName");
            
            boolean result = budgetTaskService.assignTask(id, assigneeId, assigneeName);
            return Result.success(result, "分配任务成功");
        } catch (Exception e) {
            log.error("分配任务失败：{}", e.getMessage(), e);
            return Result.error("分配任务失败：" + e.getMessage());
        }
    }

    /**
     * 提交审批
     */
    @ApiOperation("提交审批")
    @PostMapping("/{id}/submit")
    public Result<Boolean> submitForApproval(@ApiParam("任务ID") @PathVariable @NotBlank String id) {
        try {
            boolean result = budgetTaskService.submitForApproval(id);
            return Result.success(result, "提交审批成功");
        } catch (Exception e) {
            log.error("提交审批失败：{}", e.getMessage(), e);
            return Result.error("提交审批失败：" + e.getMessage());
        }
    }

    /**
     * 审批通过
     */
    @ApiOperation("审批通过")
    @PostMapping("/{id}/approve")
    public Result<Boolean> approveTask(@ApiParam("任务ID") @PathVariable @NotBlank String id) {
        try {
            boolean result = budgetTaskService.approveTask(id);
            return Result.success(result, "审批通过成功");
        } catch (Exception e) {
            log.error("审批通过失败：{}", e.getMessage(), e);
            return Result.error("审批通过失败：" + e.getMessage());
        }
    }

    /**
     * 审批拒绝
     */
    @ApiOperation("审批拒绝")
    @PostMapping("/{id}/reject")
    public Result<Boolean> rejectTask(@ApiParam("任务ID") @PathVariable @NotBlank String id) {
        try {
            boolean result = budgetTaskService.rejectTask(id);
            return Result.success(result, "审批拒绝成功");
        } catch (Exception e) {
            log.error("审批拒绝失败：{}", e.getMessage(), e);
            return Result.error("审批拒绝失败：" + e.getMessage());
        }
    }

    /**
     * 更新任务进度
     */
    @ApiOperation("更新任务进度")
    @PostMapping("/{id}/progress")
    public Result<Boolean> updateTaskProgress(
            @ApiParam("任务ID") @PathVariable @NotBlank String id,
            @RequestBody Map<String, Integer> params) {
        try {
            Integer progress = params.get("progress");
            boolean result = budgetTaskService.updateTaskProgress(id, progress);
            return Result.success(result, "更新任务进度成功");
        } catch (Exception e) {
            log.error("更新任务进度失败：{}", e.getMessage(), e);
            return Result.error("更新任务进度失败：" + e.getMessage());
        }
    }

    /**
     * 复制任务
     */
    @ApiOperation("复制任务")
    @PostMapping("/{id}/copy")
    public Result<BudgetTask> copyTask(
            @ApiParam("源任务ID") @PathVariable @NotBlank String id,
            @RequestBody Map<String, String> params) {
        try {
            String targetName = params.get("targetName");
            String targetCode = params.get("targetCode");
            
            BudgetTask task = budgetTaskService.copyTask(id, targetName, targetCode);
            return Result.success(task, "复制任务成功");
        } catch (Exception e) {
            log.error("复制任务失败：{}", e.getMessage(), e);
            return Result.error("复制任务失败：" + e.getMessage());
        }
    }

    /**
     * 检查任务编码是否存在
     */
    @ApiOperation("检查任务编码是否存在")
    @GetMapping("/check/code")
    public Result<Boolean> checkTaskCodeExists(
            @ApiParam("任务编码") @RequestParam @NotBlank String taskCode,
            @ApiParam("排除的ID") @RequestParam(required = false) String excludeId) {
        try {
            boolean exists = budgetTaskService.checkTaskCodeExists(taskCode, excludeId);
            return Result.success(exists, "检查任务编码完成");
        } catch (Exception e) {
            log.error("检查任务编码失败：{}", e.getMessage(), e);
            return Result.error("检查任务编码失败：" + e.getMessage());
        }
    }

    /**
     * 查询我的任务（创建的任务）
     */
    @ApiOperation("查询我的任务（创建的任务）")
    @GetMapping("/my/created")
    public Result<List<BudgetTask>> getMyCreatedTasks(
            @ApiParam("用户ID") @RequestParam @NotBlank String userId) {
        try {
            List<BudgetTask> tasks = budgetTaskService.getMyCreatedTasks(userId);
            return Result.success(tasks, "查询我的任务成功");
        } catch (Exception e) {
            log.error("查询我的任务失败：{}", e.getMessage(), e);
            return Result.error("查询我的任务失败：" + e.getMessage());
        }
    }

    /**
     * 查询分配给我的任务
     */
    @ApiOperation("查询分配给我的任务")
    @GetMapping("/my/assigned")
    public Result<List<BudgetTask>> getMyAssignedTasks(
            @ApiParam("用户ID") @RequestParam @NotBlank String userId) {
        try {
            List<BudgetTask> tasks = budgetTaskService.getMyAssignedTasks(userId);
            return Result.success(tasks, "查询分配给我的任务成功");
        } catch (Exception e) {
            log.error("查询分配给我的任务失败：{}", e.getMessage(), e);
            return Result.error("查询分配给我的任务失败：" + e.getMessage());
        }
    }

    /**
     * 查询我参与的任务
     */
    @ApiOperation("查询我参与的任务")
    @GetMapping("/my/participated")
    public Result<List<BudgetTask>> getMyParticipatedTasks(
            @ApiParam("用户ID") @RequestParam @NotBlank String userId) {
        try {
            List<BudgetTask> tasks = budgetTaskService.getMyParticipatedTasks(userId);
            return Result.success(tasks, "查询我参与的任务成功");
        } catch (Exception e) {
            log.error("查询我参与的任务失败：{}", e.getMessage(), e);
            return Result.error("查询我参与的任务失败：" + e.getMessage());
        }
    }

    /**
     * 统计各任务类型的数量
     */
    @ApiOperation("统计各任务类型的数量")
    @GetMapping("/statistics/type")
    public Result<List<Map<String, Object>>> countByTaskType() {
        try {
            List<Map<String, Object>> statistics = budgetTaskService.countByTaskType();
            return Result.success(statistics, "统计任务类型数量成功");
        } catch (Exception e) {
            log.error("统计任务类型数量失败：{}", e.getMessage(), e);
            return Result.error("统计任务类型数量失败：" + e.getMessage());
        }
    }

    /**
     * 统计各任务状态的数量
     */
    @ApiOperation("统计各任务状态的数量")
    @GetMapping("/statistics/status")
    public Result<List<Map<String, Object>>> countByTaskStatus() {
        try {
            List<Map<String, Object>> statistics = budgetTaskService.countByTaskStatus();
            return Result.success(statistics, "统计任务状态数量成功");
        } catch (Exception e) {
            log.error("统计任务状态数量失败：{}", e.getMessage(), e);
            return Result.error("统计任务状态数量失败：" + e.getMessage());
        }
    }

    /**
     * 统计各预算年度的任务数量
     */
    @ApiOperation("统计各预算年度的任务数量")
    @GetMapping("/statistics/year")
    public Result<List<Map<String, Object>>> countByBudgetYear() {
        try {
            List<Map<String, Object>> statistics = budgetTaskService.countByBudgetYear();
            return Result.success(statistics, "统计预算年度任务数量成功");
        } catch (Exception e) {
            log.error("统计预算年度任务数量失败：{}", e.getMessage(), e);
            return Result.error("统计预算年度任务数量失败：" + e.getMessage());
        }
    }
}
