package com.management.accountant.controller;

import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetTask;
import com.management.accountant.service.BudgetTaskService;
import com.management.accountant.util.MyJsonBean;
import com.management.accountant.util.PageResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 预算任务管理Controller
 * 
 * @description 预算任务管理接口，支持任务创建、分配、进度跟踪等功能
 * @author AI Assistant
 * @date 2025-01-04
 */
@RestController
@Api(tags = {"NCV65全面预算-任务管理"})
@RequestMapping(value = "/accountant/budget/task")
@Slf4j
public class BudgetTaskController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetTaskService taskService;

    /**
     * 创建任务
     * 前端: POST /accountant/budget/task
     */
    @Operation(summary = "创建任务")
    @ApiOperation("创建任务")
    @PostMapping("")
    public MyJsonBean<BudgetTask> create(@RequestBody @Validated BudgetTask task) {
        MyJsonBean<BudgetTask> result = new MyJsonBean<>();
        try {
            BudgetTask created = taskService.create(task);
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(created);
        } catch (ServiceException ex) {
            log.error("创建任务失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("创建任务异常", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 查询任务详情
     * 前端: GET /accountant/budget/task/{taskId}
     */
    @Operation(summary = "查询任务详情")
    @ApiOperation("查询任务详情")
    @GetMapping("/{taskId}")
    public MyJsonBean<BudgetTask> getDetail(
            @ApiParam(value = "任务ID", required = true) @PathVariable String taskId) {
        MyJsonBean<BudgetTask> result = new MyJsonBean<>();
        try {
            BudgetTask task = taskService.getById(taskId);
            if (task != null) {
                result.setCode(1);
                result.setMsg("查询成功");
                result.setData(task);
            } else {
                result.setCode(0);
                result.setMsg("任务不存在");
            }
        } catch (Exception e) {
            log.error("查询任务详情异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 更新任务
     * 前端: PUT /accountant/budget/task/{taskId}
     */
    @Operation(summary = "更新任务")
    @ApiOperation("更新任务")
    @PutMapping("/{taskId}")
    public MyJsonBean<BudgetTask> update(
            @ApiParam(value = "任务ID", required = true) @PathVariable String taskId,
            @RequestBody @Validated BudgetTask task) {
        MyJsonBean<BudgetTask> result = new MyJsonBean<>();
        try {
            task.setTaskId(taskId);
            BudgetTask updated = taskService.update(task);
            result.setCode(1);
            result.setMsg("更新成功");
            result.setData(updated);
        } catch (ServiceException ex) {
            log.error("更新任务失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("更新任务异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 删除任务
     * 前端: DELETE /accountant/budget/task/{taskId}
     */
    @Operation(summary = "删除任务")
    @ApiOperation("删除任务")
    @DeleteMapping("/{taskId}")
    public MyJsonBean<Void> delete(
            @ApiParam(value = "任务ID", required = true) @PathVariable String taskId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            taskService.delete(taskId);
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (ServiceException ex) {
            log.error("删除任务失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("删除任务异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 分页查询任务列表
     * 前端: POST /accountant/budget/task/page?current=1&size=20
     */
    @Operation(summary = "分页查询任务列表")
    @ApiOperation("分页查询任务列表")
    @PostMapping("/page")
    public MyJsonBean<PageResult<BudgetTask>> getPage(
            @RequestParam(value = "current", defaultValue = "1") Integer current,
            @RequestParam(value = "size", defaultValue = "20") Integer size,
            @RequestBody(required = false) Map<String, Object> params) {
        MyJsonBean<PageResult<BudgetTask>> result = new MyJsonBean<>();
        try {
            if (params == null) {
                params = new java.util.HashMap<>();
            }
            params.put("pageNum", current);
            params.put("pageSize", size);
            PageResult<BudgetTask> pageResult = taskService.getPage(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(pageResult);
        } catch (Exception e) {
            log.error("分页查询任务列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 批量删除任务
     * 前端: POST /accountant/budget/task/batch/delete，body 直接传 taskId 数组
     */
    @Operation(summary = "批量删除任务")
    @ApiOperation("批量删除任务")
    @PostMapping("/batch/delete")
    public MyJsonBean<Void> batchDelete(@RequestBody List<String> ids) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            if (ids == null || ids.isEmpty()) {
                result.setCode(0);
                result.setMsg("请选择要删除的数据");
                return result;
            }
            taskService.batchDelete(ids);
            result.setCode(1);
            result.setMsg("批量删除成功");
        } catch (ServiceException ex) {
            log.error("批量删除任务失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("批量删除任务异常", e);
            result.setCode(0);
            result.setMsg("批量删除失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 分配任务
     * 前端: POST /accountant/budget/task/{taskId}/assign，body: { assigneeId }
     */
    @Operation(summary = "分配任务")
    @ApiOperation("分配任务")
    @PostMapping("/{taskId}/assign")
    public MyJsonBean<Void> assign(
            @ApiParam(value = "任务ID", required = true) @PathVariable String taskId,
            @RequestBody Map<String, Object> params) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            String assigneeId = (String) params.get("assigneeId");
            String assigneeName = params.get("assigneeName") != null ? (String) params.get("assigneeName") : null;
            taskService.assign(taskId, assigneeId, assigneeName);
            result.setCode(1);
            result.setMsg("分配成功");
        } catch (ServiceException ex) {
            log.error("分配任务失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("分配任务异常", e);
            result.setCode(0);
            result.setMsg("分配失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 启动任务
     * 前端: POST /accountant/budget/task/{taskId}/start
     */
    @Operation(summary = "启动任务")
    @ApiOperation("启动任务")
    @PostMapping("/{taskId}/start")
    public MyJsonBean<Void> start(
            @ApiParam(value = "任务ID", required = true) @PathVariable String taskId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            taskService.start(taskId);
            result.setCode(1);
            result.setMsg("启动成功");
        } catch (ServiceException ex) {
            log.error("启动任务失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("启动任务异常", e);
            result.setCode(0);
            result.setMsg("启动失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 暂停任务
     * 前端: POST /accountant/budget/task/{taskId}/pause
     */
    @Operation(summary = "暂停任务")
    @ApiOperation("暂停任务")
    @PostMapping("/{taskId}/pause")
    public MyJsonBean<Void> pause(
            @ApiParam(value = "任务ID", required = true) @PathVariable String taskId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            taskService.pause(taskId);
            result.setCode(1);
            result.setMsg("暂停成功");
        } catch (ServiceException ex) {
            log.error("暂停任务失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("暂停任务异常", e);
            result.setCode(0);
            result.setMsg("暂停失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 完成任务
     * 前端: POST /accountant/budget/task/{taskId}/complete
     */
    @Operation(summary = "完成任务")
    @ApiOperation("完成任务")
    @PostMapping("/{taskId}/complete")
    public MyJsonBean<Void> complete(
            @ApiParam(value = "任务ID", required = true) @PathVariable String taskId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            taskService.complete(taskId);
            result.setCode(1);
            result.setMsg("完成成功");
        } catch (ServiceException ex) {
            log.error("完成任务失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("完成任务异常", e);
            result.setCode(0);
            result.setMsg("完成失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 取消任务
     */
    @Operation(summary = "取消任务")
    @ApiOperation("取消任务")
    @PostMapping("/{taskId}/cancel")
    public MyJsonBean<Void> cancel(
            @ApiParam(value = "任务ID", required = true) @PathVariable String taskId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            taskService.cancel(taskId);
            result.setCode(1);
            result.setMsg("取消成功");
        } catch (ServiceException ex) {
            log.error("取消任务失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("取消任务异常", e);
            result.setCode(0);
            result.setMsg("取消失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 提交任务审批
     */
    @Operation(summary = "提交任务审批")
    @ApiOperation("提交任务审批")
    @PostMapping("/submit/{taskId}")
    public MyJsonBean<Void> submit(
            @ApiParam(value = "任务ID", required = true) @PathVariable String taskId,
            @RequestBody(required = false) Map<String, Object> params) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            String comment = params != null ? (String) params.get("comment") : null;
            taskService.submit(taskId, comment);
            result.setCode(1);
            result.setMsg("提交成功");
        } catch (ServiceException ex) {
            log.error("提交任务审批失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("提交任务审批异常", e);
            result.setCode(0);
            result.setMsg("提交失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 审批任务通过
     */
    @Operation(summary = "审批任务通过")
    @ApiOperation("审批任务通过")
    @PostMapping("/approve/{taskId}")
    public MyJsonBean<Void> approve(
            @ApiParam(value = "任务ID", required = true) @PathVariable String taskId,
            @RequestBody(required = false) Map<String, Object> params) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            String comment = params != null ? (String) params.get("comment") : null;
            taskService.approve(taskId, comment);
            result.setCode(1);
            result.setMsg("审批通过");
        } catch (ServiceException ex) {
            log.error("审批任务失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("审批任务异常", e);
            result.setCode(0);
            result.setMsg("审批失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 审批任务拒绝
     */
    @Operation(summary = "审批任务拒绝")
    @ApiOperation("审批任务拒绝")
    @PostMapping("/reject/{taskId}")
    public MyJsonBean<Void> reject(
            @ApiParam(value = "任务ID", required = true) @PathVariable String taskId,
            @RequestBody(required = false) Map<String, Object> params) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            String comment = params != null ? (String) params.get("comment") : null;
            if (comment == null || comment.trim().isEmpty()) {
                result.setCode(0);
                result.setMsg("拒绝原因不能为空");
                return result;
            }
            taskService.reject(taskId, comment);
            result.setCode(1);
            result.setMsg("已拒绝");
        } catch (ServiceException ex) {
            log.error("拒绝任务失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("拒绝任务异常", e);
            result.setCode(0);
            result.setMsg("拒绝失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 复制任务
     * 前端: POST /accountant/budget/task/{taskId}/copy
     */
    @Operation(summary = "复制任务")
    @ApiOperation("复制任务")
    @PostMapping("/{taskId}/copy")
    public MyJsonBean<BudgetTask> copy(
            @ApiParam(value = "任务ID", required = true) @PathVariable String taskId) {
        MyJsonBean<BudgetTask> result = new MyJsonBean<>();
        try {
            BudgetTask copiedTask = taskService.copy(taskId);
            result.setCode(1);
            result.setMsg("复制成功");
            result.setData(copiedTask);
        } catch (ServiceException ex) {
            log.error("复制任务失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("复制任务异常", e);
            result.setCode(0);
            result.setMsg("复制失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 启用任务
     * 前端: POST /accountant/budget/task/{taskId}/enable
     */
    @Operation(summary = "启用任务")
    @ApiOperation("启用任务")
    @PostMapping("/{taskId}/enable")
    public MyJsonBean<Void> enable(
            @ApiParam(value = "任务ID", required = true) @PathVariable String taskId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            taskService.enable(taskId);
            result.setCode(1);
            result.setMsg("启用成功");
        } catch (ServiceException ex) {
            log.error("启用任务失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("启用任务异常", e);
            result.setCode(0);
            result.setMsg("启用失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 禁用任务
     * 前端: POST /accountant/budget/task/{taskId}/disable
     */
    @Operation(summary = "禁用任务")
    @ApiOperation("禁用任务")
    @PostMapping("/{taskId}/disable")
    public MyJsonBean<Void> disable(
            @ApiParam(value = "任务ID", required = true) @PathVariable String taskId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            taskService.disable(taskId);
            result.setCode(1);
            result.setMsg("禁用成功");
        } catch (ServiceException ex) {
            log.error("禁用任务失败: {}", ex.getMessage(), ex);
            result.setCode(0);
            result.setMsg(ex.getMessage());
        } catch (Exception e) {
            log.error("禁用任务异常", e);
            result.setCode(0);
            result.setMsg("禁用失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 检查任务编码是否存在
     * 前端: GET /accountant/budget/task/check/code?code=xxx
     */
    @Operation(summary = "检查任务编码是否存在")
    @ApiOperation("检查任务编码是否存在")
    @GetMapping("/check/code")
    public MyJsonBean<Boolean> checkCode(@RequestParam("code") String code) {
        MyJsonBean<Boolean> result = new MyJsonBean<>();
        try {
            boolean exists = taskService.checkCodeExists(code);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(exists);
        } catch (Exception e) {
            log.error("检查任务编码异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 按状态统计任务数量
     * 前端: GET /accountant/budget/task/count/status
     */
    @Operation(summary = "按状态统计任务数量")
    @ApiOperation("按状态统计任务数量")
    @GetMapping("/count/status")
    public MyJsonBean<Map<String, Object>> countByStatus() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> stats = taskService.getStatistics(null);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(stats);
        } catch (Exception e) {
            log.error("统计任务数量异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取我创建的任务
     * 前端: GET /accountant/budget/task/my/created
     */
    @Operation(summary = "获取我创建的任务")
    @ApiOperation("获取我创建的任务")
    @GetMapping("/my/created")
    public MyJsonBean<List<BudgetTask>> getMyCreatedTasks(@RequestParam Map<String, Object> params) {
        MyJsonBean<List<BudgetTask>> result = new MyJsonBean<>();
        try {
            List<BudgetTask> tasks = taskService.getMyCreatedTasks(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(tasks);
        } catch (Exception e) {
            log.error("查询我创建的任务异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    /**
     * 获取分配给我的任务
     * 前端: GET /accountant/budget/task/my/assigned
     */
    @Operation(summary = "获取分配给我的任务")
    @ApiOperation("获取分配给我的任务")
    @GetMapping("/my/assigned")
    public MyJsonBean<List<BudgetTask>> getMyAssignedTasks(@RequestParam Map<String, Object> params) {
        MyJsonBean<List<BudgetTask>> result = new MyJsonBean<>();
        try {
            String userId = params.get("assigneeId") != null ? (String) params.get("assigneeId") : null;
            List<BudgetTask> tasks = taskService.getMyTasks(userId, params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(tasks);
        } catch (Exception e) {
            log.error("查询分配给我的任务异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }
}

