package com.huabo.contract.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.pagehelper.PageInfo;
import com.hbfk.util.JsonBean;
import com.huabo.contract.entity.TaskAssignment;
import com.huabo.contract.service.TaskAssignmentService;
import com.huabo.contract.vo.TaskAssignmentQueryParam;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

/**
 * 任务书管理控制器
 *
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Slf4j
@RestController
@RequestMapping("/task")
@Tag(name="任务书管理",description="任务书管理")
@Validated
public class TaskAssignmentController {

    @Autowired
    private TaskAssignmentService taskAssignmentService;

    /**
     * 分页查询任务书列表
     *
     * @param param 查询参数
     * @return 分页结果
     */
    @PostMapping("/list")
    @Operation(summary = "分页查询任务书列表", description = "支持多条件查询和分页")
    public String getTaskAssignmentList(@RequestBody TaskAssignmentQueryParam param) {
        try {
            log.info("分页查询任务书列表，参数：{}", param);

            // 设置默认分页参数
            if (param.getPageNumber() == null || param.getPageNumber() <= 0) {
                param.setPageNumber(1);
            }
            if (param.getPageSize() == null || param.getPageSize() <= 0) {
                param.setPageSize(20);
            }

            PageInfo<TaskAssignment> pageInfo = taskAssignmentService.getTaskAssignmentList(param);

            return JsonBean.success("查询成功", pageInfo);
        } catch (Exception e) {
            log.error("查询任务书列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID获取任务书详情
     *
     * @param id 主键ID
     * @return 任务书详情
     */
    @GetMapping("/{id}")
    @Operation(summary = "获取任务书详情", description = "根据ID获取详细信息")
    public String getTaskAssignmentById(@PathVariable Long id) {
        try {
            log.info("获取任务书详情，ID：{}", id);

            if (id == null) {
                return JsonBean.error("ID不能为空");
            }

            TaskAssignment taskAssignment = taskAssignmentService.getTaskAssignmentById(id);
            if (taskAssignment == null) {
                return JsonBean.error("任务书不存在");
            }

            return JsonBean.success("查询成功", taskAssignment);
        } catch (Exception e) {
            log.error("获取任务书详情失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 保存任务书（新增或修改）
     *
     * @param taskAssignment 任务书
     * @return 保存结果
     */
    @PostMapping("/save")
    @Operation(summary = "保存任务书", description = "新增或修改任务书")
    public String saveTaskAssignment(@RequestBody TaskAssignment taskAssignment) {
        try {
            log.info("保存任务书，任务：{}", taskAssignment);

            // 校验必填字段
            if (!StringUtils.hasText(taskAssignment.getTaskName())) {
                return JsonBean.error("任务书名称不能为空");
            }
            if (taskAssignment.getAssignedPersonId() == null) {
                return JsonBean.error("任务负责人不能为空");
            }
            if (taskAssignment.getIssuerId() == null) {
                return JsonBean.error("任务发布人不能为空");
            }
            if (taskAssignment.getIssueDate() == null) {
                return JsonBean.error("发布日期不能为空");
            }

            // 校验任务书编号唯一性
            if (StringUtils.hasText(taskAssignment.getTaskNo())) {
                boolean exists = taskAssignmentService.existsTaskNo(
                    taskAssignment.getTaskNo(), taskAssignment.getId());
                if (exists) {
                    return JsonBean.error("任务书编号已存在，请重新输入");
                }
            }

            boolean result = taskAssignmentService.saveTaskAssignment(taskAssignment);
            if (result) {
                return JsonBean.success("保存成功");
            } else {
                return JsonBean.error("保存失败");
            }
        } catch (Exception e) {
            log.error("保存任务书失败", e);
            return JsonBean.error("保存失败：" + e.getMessage());
        }
    }

    /**
     * 更新任务书
     *
     * @param id 任务书ID
     * @param taskAssignment 任务书
     * @return 更新结果
     */
    @PutMapping("/{id}")
    @Operation(summary = "更新任务书", description = "根据ID更新任务书")
    public String updateTaskAssignment(@PathVariable Long id, @RequestBody TaskAssignment taskAssignment) {
        try {
            log.info("更新任务书，ID：{}，任务：{}", id, taskAssignment);

            // 设置ID确保更新正确的记录
            taskAssignment.setId(id);

            // 校验必填字段
            if (!StringUtils.hasText(taskAssignment.getTaskName())) {
                return JsonBean.error("任务书名称不能为空");
            }
            if (taskAssignment.getAssignedPersonId() == null) {
                return JsonBean.error("任务负责人不能为空");
            }
            if (taskAssignment.getIssuerId() == null) {
                return JsonBean.error("任务发布人不能为空");
            }
            if (taskAssignment.getIssueDate() == null) {
                return JsonBean.error("发布日期不能为空");
            }

            boolean result = taskAssignmentService.saveTaskAssignment(taskAssignment);
            if (result) {
                return JsonBean.success("更新成功");
            } else {
                return JsonBean.error("更新失败");
            }
        } catch (Exception e) {
            log.error("更新任务书失败", e);
            return JsonBean.error("更新失败：" + e.getMessage());
        }
    }

    /**
     * 删除任务书
     *
     * @param id 主键ID
     * @return 删除结果
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "删除任务书", description = "根据ID删除任务书")
    public String deleteTaskAssignment(@PathVariable Long id) {
        try {
            log.info("删除任务书，ID：{}", id);

            if (id == null) {
                return JsonBean.error("ID不能为空");
            }

            boolean result = taskAssignmentService.deleteTaskAssignment(id);
            if (result) {
                return JsonBean.success("删除成功");
            } else {
                return JsonBean.error("删除失败");
            }
        } catch (Exception e) {
            log.error("删除任务书失败", e);
            return JsonBean.error("删除失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除任务书
     *
     * @param ids 主键ID列表
     * @return 删除结果
     */
    @PostMapping("/batchDelete")
    @Operation(summary = "批量删除任务书", description = "根据ID列表批量删除任务书")
    public String batchDeleteTaskAssignment(@RequestBody List<Long> ids) {
        try {
            log.info("批量删除任务书，ID列表：{}", ids);

            if (ids == null || ids.isEmpty()) {
                return JsonBean.error("ID列表不能为空");
            }

            boolean result = taskAssignmentService.batchDeleteTaskAssignment(ids);
            if (result) {
                return JsonBean.success("批量删除成功");
            } else {
                return JsonBean.error("批量删除失败");
            }
        } catch (Exception e) {
            log.error("批量删除任务书失败", e);
            return JsonBean.error("批量删除失败：" + e.getMessage());
        }
    }

    /**
     * 审核任务书
     *
     * @param request 审核请求
     * @return 审核结果
     */
    @PostMapping("/review")
    @Operation(summary = "审核任务书", description = "审核任务书")
    public String reviewTask(@RequestBody Map<String, Object> request) {
        try {
            Long id = Long.valueOf(request.get("id").toString());
            Long reviewerId = Long.valueOf(request.get("reviewerId").toString());
            String reviewerName = (String) request.get("reviewerName");
            String reviewComments = (String) request.get("reviewComments");
            Boolean approved = (Boolean) request.get("approved");

            log.info("审核任务书，ID：{}，审核人：{}，是否通过：{}", id, reviewerName, approved);

            if (id == null) {
                return JsonBean.error("任务ID不能为空");
            }
            if (reviewerId == null) {
                return JsonBean.error("审核人ID不能为空");
            }
            if (!StringUtils.hasText(reviewerName)) {
                return JsonBean.error("审核人姓名不能为空");
            }
            if (approved == null) {
                return JsonBean.error("审核结果不能为空");
            }

            boolean result = taskAssignmentService.reviewTask(id, reviewerId, reviewerName, reviewComments, approved);
            if (result) {
                return JsonBean.success("审核成功");
            } else {
                return JsonBean.error("审核失败");
            }
        } catch (Exception e) {
            log.error("审核任务书失败", e);
            return JsonBean.error("审核失败：" + e.getMessage());
        }
    }

    /**
     * 下达任务书
     *
     * @param request 下达请求
     * @return 下达结果
     */
    @PostMapping("/issue")
    @Operation(summary = "下达任务书", description = "下达任务书")
    public String issueTask(@RequestBody Map<String, Object> request) {
        try {
            Long id = Long.valueOf(request.get("id").toString());
            Long issuerId = Long.valueOf(request.get("issuerId").toString());
            String issuerName = (String) request.get("issuerName");
            Long receiverId = Long.valueOf(request.get("receiverId").toString());
            String receiverName = (String) request.get("receiverName");

            log.info("下达任务书，ID：{}，下达人：{}，接收人：{}", id, issuerName, receiverName);

            if (id == null) {
                return JsonBean.error("任务ID不能为空");
            }
            if (issuerId == null) {
                return JsonBean.error("下达人ID不能为空");
            }
            if (!StringUtils.hasText(issuerName)) {
                return JsonBean.error("下达人姓名不能为空");
            }
            if (receiverId == null) {
                return JsonBean.error("接收人ID不能为空");
            }
            if (!StringUtils.hasText(receiverName)) {
                return JsonBean.error("接收人姓名不能为空");
            }

            boolean result = taskAssignmentService.issueTask(id, issuerId, issuerName, receiverId, receiverName);
            if (result) {
                return JsonBean.success("下达成功");
            } else {
                return JsonBean.error("下达失败");
            }
        } catch (Exception e) {
            log.error("下达任务书失败", e);
            return JsonBean.error("下达失败：" + e.getMessage());
        }
    }

    /**
     * 接收任务书
     *
     * @param request 接收请求
     * @return 接收结果
     */
    @PostMapping("/receive")
    @Operation(summary = "接收任务书", description = "接收任务书")
    public String receiveTask(@RequestBody Map<String, Object> request) {
        try {
            Long id = Long.valueOf(request.get("id").toString());
            Long receiverId = Long.valueOf(request.get("receiverId").toString());
            String receiverName = (String) request.get("receiverName");

            log.info("接收任务书，ID：{}，接收人：{}", id, receiverName);

            if (id == null) {
                return JsonBean.error("任务ID不能为空");
            }
            if (receiverId == null) {
                return JsonBean.error("接收人ID不能为空");
            }
            if (!StringUtils.hasText(receiverName)) {
                return JsonBean.error("接收人姓名不能为空");
            }

            boolean result = taskAssignmentService.receiveTask(id, receiverId, receiverName);
            if (result) {
                return JsonBean.success("接收成功");
            } else {
                return JsonBean.error("接收失败");
            }
        } catch (Exception e) {
            log.error("接收任务书失败", e);
            return JsonBean.error("接收失败：" + e.getMessage());
        }
    }

    /**
     * 开始执行任务
     *
     * @param id 任务ID
     * @return 执行结果
     */
    @PostMapping("/start/{id}")
    @Operation(summary = "开始执行任务", description = "开始执行任务")
    public String startTask(@PathVariable Long id) {
        try {
            log.info("开始执行任务，ID：{}", id);

            if (id == null) {
                return JsonBean.error("任务ID不能为空");
            }

            boolean result = taskAssignmentService.startTask(id);
            if (result) {
                return JsonBean.success("开始执行成功");
            } else {
                return JsonBean.error("开始执行失败");
            }
        } catch (Exception e) {
            log.error("开始执行任务失败", e);
            return JsonBean.error("开始执行失败：" + e.getMessage());
        }
    }

    /**
     * 完成任务
     *
     * @param id 任务ID
     * @return 完成结果
     */
    @PostMapping("/complete/{id}")
    @Operation(summary = "完成任务", description = "完成任务")
    public String completeTask(@PathVariable Long id) {
        try {
            log.info("完成任务，ID：{}", id);

            if (id == null) {
                return JsonBean.error("任务ID不能为空");
            }

            boolean result = taskAssignmentService.completeTask(id);
            if (result) {
                return JsonBean.success("完成任务成功");
            } else {
                return JsonBean.error("完成任务失败");
            }
        } catch (Exception e) {
            log.error("完成任务失败", e);
            return JsonBean.error("完成任务失败：" + e.getMessage());
        }
    }

    /**
     * 暂停任务
     *
     * @param id 任务ID
     * @return 暂停结果
     */
    @PostMapping("/pause/{id}")
    @Operation(summary = "暂停任务", description = "暂停任务")
    public String pauseTask(@PathVariable Long id) {
        try {
            log.info("暂停任务，ID：{}", id);

            if (id == null) {
                return JsonBean.error("任务ID不能为空");
            }

            boolean result = taskAssignmentService.pauseTask(id);
            if (result) {
                return JsonBean.success("暂停任务成功");
            } else {
                return JsonBean.error("暂停任务失败");
            }
        } catch (Exception e) {
            log.error("暂停任务失败", e);
            return JsonBean.error("暂停任务失败：" + e.getMessage());
        }
    }

    /**
     * 取消任务
     *
     * @param id 任务ID
     * @return 取消结果
     */
    @PostMapping("/cancel/{id}")
    @Operation(summary = "取消任务", description = "取消任务")
    public String cancelTask(@PathVariable Long id) {
        try {
            log.info("取消任务，ID：{}", id);

            if (id == null) {
                return JsonBean.error("任务ID不能为空");
            }

            boolean result = taskAssignmentService.cancelTask(id);
            if (result) {
                return JsonBean.success("取消任务成功");
            } else {
                return JsonBean.error("取消任务失败");
            }
        } catch (Exception e) {
            log.error("取消任务失败", e);
            return JsonBean.error("取消任务失败：" + e.getMessage());
        }
    }

    /**
     * 更新完成度
     *
     * @param request 更新请求
     * @return 更新结果
     */
    @PostMapping("/updateCompletionRate")
    @Operation(summary = "更新完成度", description = "更新任务完成度")
    public String updateCompletionRate(@RequestBody Map<String, Object> request) {
        try {
            Long id = Long.valueOf(request.get("id").toString());
            BigDecimal completionRate = new BigDecimal(request.get("completionRate").toString());

            log.info("更新完成度，ID：{}，完成度：{}", id, completionRate);

            if (id == null) {
                return JsonBean.error("任务ID不能为空");
            }
            if (completionRate == null) {
                return JsonBean.error("完成度不能为空");
            }
            if (completionRate.compareTo(BigDecimal.ZERO) < 0 || completionRate.compareTo(new BigDecimal("100")) > 0) {
                return JsonBean.error("完成度必须在0-100之间");
            }

            boolean result = taskAssignmentService.updateCompletionRate(id, completionRate);
            if (result) {
                return JsonBean.success("更新完成度成功");
            } else {
                return JsonBean.error("更新完成度失败");
            }
        } catch (Exception e) {
            log.error("更新完成度失败", e);
            return JsonBean.error("更新完成度失败：" + e.getMessage());
        }
    }

    /**
     * 批量更新任务状态
     *
     * @param request 更新请求
     * @return 更新结果
     */
    @PostMapping("/batchUpdateStatus")
    @Operation(summary = "批量更新任务状态", description = "批量更新任务状态")
    public String batchUpdateTaskStatus(@RequestBody Map<String, Object> request) {
        try {
            @SuppressWarnings("unchecked")
            List<Long> ids = (List<Long>) request.get("ids");
            Integer taskStatus = Integer.valueOf(request.get("taskStatus").toString());

            log.info("批量更新任务状态，ID列表：{}，任务状态：{}", ids, taskStatus);

            if (ids == null || ids.isEmpty()) {
                return JsonBean.error("任务ID列表不能为空");
            }
            if (taskStatus == null) {
                return JsonBean.error("任务状态不能为空");
            }

            boolean result = taskAssignmentService.batchUpdateTaskStatus(ids, taskStatus);
            if (result) {
                return JsonBean.success("批量更新状态成功");
            } else {
                return JsonBean.error("批量更新状态失败");
            }
        } catch (Exception e) {
            log.error("批量更新任务状态失败", e);
            return JsonBean.error("批量更新状态失败：" + e.getMessage());
        }
    }

    /**
     * 获取待审核的任务列表
     *
     * @return 待审核的任务列表
     */
    @GetMapping("/pendingReview")
    @Operation(summary = "获取待审核的任务列表", description = "获取待审核的任务列表")
    public String getPendingReviewTasks() {
        try {
            log.info("获取待审核的任务列表");

            List<TaskAssignment> tasks = taskAssignmentService.getPendingReviewTasks();
            return JsonBean.success("查询成功", tasks);
        } catch (Exception e) {
            log.error("获取待审核的任务列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取已下达的任务列表
     *
     * @return 已下达的任务列表
     */
    @GetMapping("/issued")
    @Operation(summary = "获取已下达的任务列表", description = "获取已下达的任务列表")
    public String getIssuedTasks() {
        try {
            log.info("获取已下达的任务列表");

            List<TaskAssignment> tasks = taskAssignmentService.getIssuedTasks();
            return JsonBean.success("查询成功", tasks);
        } catch (Exception e) {
            log.error("获取已下达的任务列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取执行中的任务列表
     *
     * @return 执行中的任务列表
     */
    @GetMapping("/inProgress")
    @Operation(summary = "获取执行中的任务列表", description = "获取执行中的任务列表")
    public String getInProgressTasks() {
        try {
            log.info("获取执行中的任务列表");

            List<TaskAssignment> tasks = taskAssignmentService.getInProgressTasks();
            return JsonBean.success("查询成功", tasks);
        } catch (Exception e) {
            log.error("获取执行中的任务列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取已完成的任务列表
     *
     * @return 已完成的任务列表
     */
    @GetMapping("/completed")
    @Operation(summary = "获取已完成的任务列表", description = "获取已完成的任务列表")
    public String getCompletedTasks() {
        try {
            log.info("获取已完成的任务列表");

            List<TaskAssignment> tasks = taskAssignmentService.getCompletedTasks();
            return JsonBean.success("查询成功", tasks);
        } catch (Exception e) {
            log.error("获取已完成的任务列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取延期的任务列表
     *
     * @return 延期的任务列表
     */
    @GetMapping("/delayed")
    @Operation(summary = "获取延期的任务列表", description = "获取延期的任务列表")
    public String getDelayedTasks() {
        try {
            log.info("获取延期的任务列表");

            List<TaskAssignment> tasks = taskAssignmentService.getDelayedTasks();
            return JsonBean.success("查询成功", tasks);
        } catch (Exception e) {
            log.error("获取延期的任务列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取即将到期的任务列表
     *
     * @param days 天数
     * @return 即将到期的任务列表
     */
    @GetMapping("/expiringSoon")
    @Operation(summary = "获取即将到期的任务列表", description = "获取即将到期的任务列表")
    public String getExpiringSoonTasks(@RequestParam(defaultValue = "3") Integer days) {
        try {
            log.info("获取即将到期的任务列表，天数：{}", days);

            List<TaskAssignment> tasks = taskAssignmentService.getExpiringSoonTasks(days);
            return JsonBean.success("查询成功", tasks);
        } catch (Exception e) {
            log.error("获取即将到期的任务列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取紧急任务列表
     *
     * @return 紧急任务列表
     */
    @GetMapping("/urgent")
    @Operation(summary = "获取紧急任务列表", description = "获取紧急任务列表")
    public String getUrgentTasks() {
        try {
            log.info("获取紧急任务列表");

            List<TaskAssignment> tasks = taskAssignmentService.getUrgentTasks();
            return JsonBean.success("查询成功", tasks);
        } catch (Exception e) {
            log.error("获取紧急任务列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取重要任务列表
     *
     * @return 重要任务列表
     */
    @GetMapping("/important")
    @Operation(summary = "获取重要任务列表", description = "获取重要任务列表")
    public String getImportantTasks() {
        try {
            log.info("获取重要任务列表");

            List<TaskAssignment> tasks = taskAssignmentService.getImportantTasks();
            return JsonBean.success("查询成功", tasks);
        } catch (Exception e) {
            log.error("获取重要任务列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取大额任务列表
     *
     * @param minAmount 最小金额
     * @return 大额任务列表
     */
    @GetMapping("/large")
    @Operation(summary = "获取大额任务列表", description = "获取大额任务列表")
    public String getLargeTasks(@RequestParam(defaultValue = "1000000") BigDecimal minAmount) {
        try {
            log.info("获取大额任务列表，最小金额：{}", minAmount);

            List<TaskAssignment> tasks = taskAssignmentService.getLargeTasks(minAmount);
            return JsonBean.success("查询成功", tasks);
        } catch (Exception e) {
            log.error("获取大额任务列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取我负责的任务列表
     *
     * @param userId 用户ID
     * @return 我负责的任务列表
     */
    @GetMapping("/myResponsible/{userId}")
    @Operation(summary = "获取我负责的任务列表", description = "获取我负责的任务列表")
    public String getMyResponsibleTasks(@PathVariable Long userId) {
        try {
            log.info("获取我负责的任务列表，用户ID：{}", userId);

            if (userId == null) {
                return JsonBean.error("用户ID不能为空");
            }

            List<TaskAssignment> tasks = taskAssignmentService.getMyResponsibleTasks(userId);
            return JsonBean.success("查询成功", tasks);
        } catch (Exception e) {
            log.error("获取我负责的任务列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取我接收的任务列表
     *
     * @param userId 用户ID
     * @return 我接收的任务列表
     */
    @GetMapping("/myReceived/{userId}")
    @Operation(summary = "获取我接收的任务列表", description = "获取我接收的任务列表")
    public String getMyReceivedTasks(@PathVariable Long userId) {
        try {
            log.info("获取我接收的任务列表，用户ID：{}", userId);

            if (userId == null) {
                return JsonBean.error("用户ID不能为空");
            }

            List<TaskAssignment> tasks = taskAssignmentService.getMyReceivedTasks(userId);
            return JsonBean.success("查询成功", tasks);
        } catch (Exception e) {
            log.error("获取我接收的任务列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取我下达的任务列表
     *
     * @param userId 用户ID
     * @return 我下达的任务列表
     */
    @GetMapping("/myIssued/{userId}")
    @Operation(summary = "获取我下达的任务列表", description = "获取我下达的任务列表")
    public String getMyIssuedTasks(@PathVariable Long userId) {
        try {
            log.info("获取我下达的任务列表，用户ID：{}", userId);

            if (userId == null) {
                return JsonBean.error("用户ID不能为空");
            }

            List<TaskAssignment> tasks = taskAssignmentService.getMyIssuedTasks(userId);
            return JsonBean.success("查询成功", tasks);
        } catch (Exception e) {
            log.error("获取我下达的任务列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据项目ID查询任务列表
     *
     * @param projectId 项目ID
     * @return 任务列表
     */
    @GetMapping("/project/{projectId}")
    @Operation(summary = "根据项目ID查询任务列表", description = "根据项目ID查询任务列表")
    public String getTaskAssignmentByProjectId(@PathVariable Long projectId) {
        try {
            log.info("根据项目ID查询任务列表，项目ID：{}", projectId);

            if (projectId == null) {
                return JsonBean.error("项目ID不能为空");
            }

            List<TaskAssignment> tasks = taskAssignmentService.getTaskAssignmentByProjectId(projectId);
            return JsonBean.success("查询成功", tasks);
        } catch (Exception e) {
            log.error("根据项目ID查询任务列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 统计任务书数据
     *
     * @param param 查询参数
     * @return 统计数据
     */
    @PostMapping("/statistics")
    @Operation(summary = "统计任务书数据", description = "统计任务书数据")
    public String getTaskAssignmentStatistics(@RequestBody TaskAssignmentQueryParam param) {
        try {
            log.info("统计任务书数据，参数：{}", param);

            Map<String, Object> statistics = taskAssignmentService.getTaskAssignmentStatistics(param);
            return JsonBean.success("统计成功", statistics);
        } catch (Exception e) {
            log.error("统计任务书数据失败", e);
            return JsonBean.error("统计失败：" + e.getMessage());
        }
    }

    /**
     * 获取任务类型分布统计
     *
     * @param param 查询参数
     * @return 任务类型分布
     */
    @PostMapping("/statistics/taskType")
    @Operation(summary = "获取任务类型分布统计", description = "获取任务类型分布统计")
    public String getTaskTypeDistribution(@RequestBody TaskAssignmentQueryParam param) {
        try {
            log.info("获取任务类型分布统计，参数：{}", param);

            List<Map<String, Object>> distribution = taskAssignmentService.getTaskTypeDistribution(param);
            return JsonBean.success("统计成功", distribution);
        } catch (Exception e) {
            log.error("获取任务类型分布统计失败", e);
            return JsonBean.error("统计失败：" + e.getMessage());
        }
    }

    /**
     * 获取任务状态分布统计
     *
     * @param param 查询参数
     * @return 任务状态分布
     */
    @PostMapping("/statistics/taskStatus")
    @Operation(summary = "获取任务状态分布统计", description = "获取任务状态分布统计")
    public String getTaskStatusDistribution(@RequestBody TaskAssignmentQueryParam param) {
        try {
            log.info("获取任务状态分布统计，参数：{}", param);

            List<Map<String, Object>> distribution = taskAssignmentService.getTaskStatusDistribution(param);
            return JsonBean.success("统计成功", distribution);
        } catch (Exception e) {
            log.error("获取任务状态分布统计失败", e);
            return JsonBean.error("统计失败：" + e.getMessage());
        }
    }

    /**
     * 获取月度任务趋势
     *
     * @param param 查询参数
     * @return 月度任务趋势
     */
    @PostMapping("/statistics/monthlyTrend")
    @Operation(summary = "获取月度任务趋势", description = "获取月度任务趋势")
    public String getMonthlyTaskTrend(@RequestBody TaskAssignmentQueryParam param) {
        try {
            log.info("获取月度任务趋势，参数：{}", param);

            List<Map<String, Object>> trend = taskAssignmentService.getMonthlyTaskTrend(param);
            return JsonBean.success("统计成功", trend);
        } catch (Exception e) {
            log.error("获取月度任务趋势失败", e);
            return JsonBean.error("统计失败：" + e.getMessage());
        }
    }

    /**
     * 模糊搜索任务书
     *
     * @param keyword 关键词
     * @param limit 限制数量
     * @return 任务书列表
     */
    @GetMapping("/search")
    @Operation(summary = "模糊搜索任务书", description = "模糊搜索任务书")
    public String searchTaskAssignments(@RequestParam String keyword,
                                       @RequestParam(defaultValue = "10") Integer limit) {
        try {
            log.info("模糊搜索任务书，关键词：{}，限制数量：{}", keyword, limit);

            if (!StringUtils.hasText(keyword)) {
                return JsonBean.error("搜索关键词不能为空");
            }

            List<TaskAssignment> tasks = taskAssignmentService.searchTaskAssignments(keyword, limit);
            return JsonBean.success("搜索成功", tasks);
        } catch (Exception e) {
            log.error("模糊搜索任务书失败", e);
            return JsonBean.error("搜索失败：" + e.getMessage());
        }
    }

    /**
     * 获取今日到期的任务列表
     *
     * @return 今日到期的任务列表
     */
    @GetMapping("/todayExpiring")
    @Operation(summary = "获取今日到期的任务列表", description = "获取今日到期的任务列表")
    public String getTodayExpiringTasks() {
        try {
            log.info("获取今日到期的任务列表");

            List<TaskAssignment> tasks = taskAssignmentService.getTodayExpiringTasks();
            return JsonBean.success("查询成功", tasks);
        } catch (Exception e) {
            log.error("获取今日到期的任务列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取本周到期的任务列表
     *
     * @return 本周到期的任务列表
     */
    @GetMapping("/thisWeekExpiring")
    @Operation(summary = "获取本周到期的任务列表", description = "获取本周到期的任务列表")
    public String getThisWeekExpiringTasks() {
        try {
            log.info("获取本周到期的任务列表");

            List<TaskAssignment> tasks = taskAssignmentService.getThisWeekExpiringTasks();
            return JsonBean.success("查询成功", tasks);
        } catch (Exception e) {
            log.error("获取本周到期的任务列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 获取本月到期的任务列表
     *
     * @return 本月到期的任务列表
     */
    @GetMapping("/thisMonthExpiring")
    @Operation(summary = "获取本月到期的任务列表", description = "获取本月到期的任务列表")
    public String getThisMonthExpiringTasks() {
        try {
            log.info("获取本月到期的任务列表");

            List<TaskAssignment> tasks = taskAssignmentService.getThisMonthExpiringTasks();
            return JsonBean.success("查询成功", tasks);
        } catch (Exception e) {
            log.error("获取本月到期的任务列表失败", e);
            return JsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 导出任务书
     *
     * @param param 查询参数
     * @return 任务列表
     */
    @PostMapping("/export")
    @Operation(summary = "导出任务书", description = "导出任务书")
    public String exportTaskAssignments(@RequestBody TaskAssignmentQueryParam param) {
        try {
            log.info("导出任务书，参数：{}", param);

            List<TaskAssignment> tasks = taskAssignmentService.exportTaskAssignments(param);
            return JsonBean.success("导出成功", tasks);
        } catch (Exception e) {
            log.error("导出任务书失败", e);
            return JsonBean.error("导出失败：" + e.getMessage());
        }
    }

    /**
     * 自动更新过期任务状态
     *
     * @return 更新数量
     */
    @PostMapping("/autoUpdateExpired")
    @Operation(summary = "自动更新过期任务状态", description = "自动更新过期任务状态")
    public String autoUpdateExpiredTasks() {
        try {
            log.info("自动更新过期任务状态");

            int count = taskAssignmentService.autoUpdateExpiredTasks();
            return JsonBean.success("更新成功，共更新 " + count + " 个任务");
        } catch (Exception e) {
            log.error("自动更新过期任务状态失败", e);
            return JsonBean.error("更新失败：" + e.getMessage());
        }
    }

    /**
     * 发送任务提醒通知
     *
     * @param days 提前天数
     * @return 发送数量
     */
    @PostMapping("/sendReminders")
    @Operation(summary = "发送任务提醒通知", description = "发送任务提醒通知")
    public String sendTaskReminders(@RequestParam(defaultValue = "3") Integer days) {
        try {
            log.info("发送任务提醒通知，提前天数：{}", days);

            int count = taskAssignmentService.sendTaskReminders(days);
            return JsonBean.success("发送成功，共发送 " + count + " 个提醒");
        } catch (Exception e) {
            log.error("发送任务提醒通知失败", e);
            return JsonBean.error("发送失败：" + e.getMessage());
        }
    }
}
