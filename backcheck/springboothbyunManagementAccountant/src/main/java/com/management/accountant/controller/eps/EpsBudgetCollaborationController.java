package com.management.accountant.controller.eps;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.management.accountant.util.MyJsonBean;
import com.management.accountant.entity.eps.EpsBudgetCollaborationTask;
import com.management.accountant.service.eps.EpsBudgetCollaborationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 预算协同编制管理控制器
 * 
 * @author 华博云
 * @version 3.0.0
 */
@Slf4j
@Api(tags = "预算协同编制管理")
@RestController
@RequestMapping("/eps/budget-collaboration")
@Validated
public class EpsBudgetCollaborationController {

    @Autowired
    private EpsBudgetCollaborationService budgetCollaborationService;

    /**
     * 分页查询协同任务
     */
    @ApiOperation("分页查询协同任务")
    @GetMapping("/tasks")
    public MyJsonBean<IPage<EpsBudgetCollaborationTask>> queryCollaborationTasks(
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Long current,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Long size,
            @ApiParam("任务名称") @RequestParam(required = false) String taskName,
            @ApiParam("任务状态") @RequestParam(required = false) String taskStatus,
            @ApiParam("预算体系ID") @RequestParam(required = false) Long systemId,
            @ApiParam("负责人ID") @RequestParam(required = false) Long assigneeId) {
        try {
            IPage<EpsBudgetCollaborationTask> result = budgetCollaborationService.queryCollaborationTaskPage(
                    current, size, taskName, taskStatus, systemId, assigneeId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("查询协同任务失败", e);
            return MyJsonBean.error("查询失败：" + e.getMessage());
        }
    }

    /**
     * 创建协同任务
     */
    @ApiOperation("创建协同任务")
    @PostMapping("/tasks")
    public MyJsonBean<Boolean> createCollaborationTask(
            @ApiParam("协同任务") @RequestBody @Valid EpsBudgetCollaborationTask collaborationTask) {
        try {
            boolean result = budgetCollaborationService.createCollaborationTask(collaborationTask);
            if (result) {
                return MyJsonBean.success("创建成功", true);
            } else {
                return MyJsonBean.error("创建失败");
            }
        } catch (Exception e) {
            log.error("创建协同任务失败", e);
            return MyJsonBean.error("创建失败：" + e.getMessage());
        }
    }

    /**
     * 更新协同任务
     */
    @ApiOperation("更新协同任务")
    @PutMapping("/tasks/{taskId}")
    public MyJsonBean<Boolean> updateCollaborationTask(
            @ApiParam("任务ID") @PathVariable @NotNull Long taskId,
            @ApiParam("协同任务") @RequestBody @Valid EpsBudgetCollaborationTask collaborationTask) {
        try {
            collaborationTask.setTaskId(taskId);
            boolean result = budgetCollaborationService.updateCollaborationTask(collaborationTask);
            if (result) {
                return MyJsonBean.success("更新成功", true);
            } else {
                return MyJsonBean.error("更新失败");
            }
        } catch (Exception e) {
            log.error("更新协同任务失败", e);
            return MyJsonBean.error("更新失败：" + e.getMessage());
        }
    }

    /**
     * 删除协同任务
     */
    @ApiOperation("删除协同任务")
    @DeleteMapping("/tasks/{taskId}")
    public MyJsonBean<Boolean> deleteCollaborationTask(
            @ApiParam("任务ID") @PathVariable @NotNull Long taskId) {
        try {
            boolean result = budgetCollaborationService.deleteCollaborationTask(taskId);
            if (result) {
                return MyJsonBean.success("删除成功", true);
            } else {
                return MyJsonBean.error("删除失败");
            }
        } catch (Exception e) {
            log.error("删除协同任务失败", e);
            return MyJsonBean.error("删除失败：" + e.getMessage());
        }
    }

    /**
     * 获取协同任务详情
     */
    @ApiOperation("获取协同任务详情")
    @GetMapping("/tasks/{taskId}")
    public MyJsonBean<EpsBudgetCollaborationTask> getCollaborationTaskById(
            @ApiParam("任务ID") @PathVariable @NotNull Long taskId) {
        try {
            EpsBudgetCollaborationTask result = budgetCollaborationService.getCollaborationTaskById(taskId);
            if (result != null) {
                return MyJsonBean.success(result);
            } else {
                return MyJsonBean.error("任务不存在");
            }
        } catch (Exception e) {
            log.error("获取协同任务详情失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 分配协同任务
     */
    @ApiOperation("分配协同任务")
    @PostMapping("/tasks/{taskId}/assign")
    public MyJsonBean<Boolean> assignCollaborationTask(
            @ApiParam("任务ID") @PathVariable @NotNull Long taskId,
            @ApiParam("分配信息") @RequestBody Map<String, Object> assignmentData) {
        try {
            boolean result = budgetCollaborationService.assignCollaborationTask(taskId, assignmentData);
            if (result) {
                return MyJsonBean.success("分配成功", true);
            } else {
                return MyJsonBean.error("分配失败");
            }
        } catch (Exception e) {
            log.error("分配协同任务失败", e);
            return MyJsonBean.error("分配失败：" + e.getMessage());
        }
    }

    /**
     * 开始协同任务
     */
    @ApiOperation("开始协同任务")
    @PostMapping("/tasks/{taskId}/start")
    public MyJsonBean<Boolean> startCollaborationTask(
            @ApiParam("任务ID") @PathVariable @NotNull Long taskId,
            @ApiParam("用户ID") @RequestParam @NotNull Long userId) {
        try {
            boolean result = budgetCollaborationService.startCollaborationTask(taskId, userId);
            if (result) {
                return MyJsonBean.success("任务开始成功", true);
            } else {
                return MyJsonBean.error("任务开始失败");
            }
        } catch (Exception e) {
            log.error("开始协同任务失败", e);
            return MyJsonBean.error("开始失败：" + e.getMessage());
        }
    }

    /**
     * 完成协同任务
     */
    @ApiOperation("完成协同任务")
    @PostMapping("/tasks/{taskId}/complete")
    public MyJsonBean<Boolean> completeCollaborationTask(
            @ApiParam("任务ID") @PathVariable @NotNull Long taskId,
            @ApiParam("完成信息") @RequestBody Map<String, Object> completionData) {
        try {
            boolean result = budgetCollaborationService.completeCollaborationTask(taskId, completionData);
            if (result) {
                return MyJsonBean.success("任务完成成功", true);
            } else {
                return MyJsonBean.error("任务完成失败");
            }
        } catch (Exception e) {
            log.error("完成协同任务失败", e);
            return MyJsonBean.error("完成失败：" + e.getMessage());
        }
    }

    /**
     * 暂停协同任务
     */
    @ApiOperation("暂停协同任务")
    @PostMapping("/tasks/{taskId}/pause")
    public MyJsonBean<Boolean> pauseCollaborationTask(
            @ApiParam("任务ID") @PathVariable @NotNull Long taskId,
            @ApiParam("用户ID") @RequestParam @NotNull Long userId,
            @ApiParam("暂停原因") @RequestParam(required = false) String pauseReason) {
        try {
            boolean result = budgetCollaborationService.pauseCollaborationTask(taskId, userId, pauseReason);
            if (result) {
                return MyJsonBean.success("任务暂停成功", true);
            } else {
                return MyJsonBean.error("任务暂停失败");
            }
        } catch (Exception e) {
            log.error("暂停协同任务失败", e);
            return MyJsonBean.error("暂停失败：" + e.getMessage());
        }
    }

    /**
     * 恢复协同任务
     */
    @ApiOperation("恢复协同任务")
    @PostMapping("/tasks/{taskId}/resume")
    public MyJsonBean<Boolean> resumeCollaborationTask(
            @ApiParam("任务ID") @PathVariable @NotNull Long taskId,
            @ApiParam("用户ID") @RequestParam @NotNull Long userId) {
        try {
            boolean result = budgetCollaborationService.resumeCollaborationTask(taskId, userId);
            if (result) {
                return MyJsonBean.success("任务恢复成功", true);
            } else {
                return MyJsonBean.error("任务恢复失败");
            }
        } catch (Exception e) {
            log.error("恢复协同任务失败", e);
            return MyJsonBean.error("恢复失败：" + e.getMessage());
        }
    }

    /**
     * 获取任务进度
     */
    @ApiOperation("获取任务进度")
    @GetMapping("/tasks/{taskId}/progress")
    public MyJsonBean<Map<String, Object>> getTaskProgress(
            @ApiParam("任务ID") @PathVariable @NotNull Long taskId) {
        try {
            Map<String, Object> result = budgetCollaborationService.getTaskProgress(taskId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取任务进度失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 获取任务参与者
     */
    @ApiOperation("获取任务参与者")
    @GetMapping("/tasks/{taskId}/participants")
    public MyJsonBean<List<Map<String, Object>>> getTaskParticipants(
            @ApiParam("任务ID") @PathVariable @NotNull Long taskId) {
        try {
            List<Map<String, Object>> result = budgetCollaborationService.getTaskParticipants(taskId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取任务参与者失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 添加任务参与者
     */
    @ApiOperation("添加任务参与者")
    @PostMapping("/tasks/{taskId}/participants")
    public MyJsonBean<Boolean> addTaskParticipant(
            @ApiParam("任务ID") @PathVariable @NotNull Long taskId,
            @ApiParam("参与者信息") @RequestBody Map<String, Object> participantData) {
        try {
            boolean result = budgetCollaborationService.addTaskParticipant(taskId, participantData);
            if (result) {
                return MyJsonBean.success("添加参与者成功", true);
            } else {
                return MyJsonBean.error("添加参与者失败");
            }
        } catch (Exception e) {
            log.error("添加任务参与者失败", e);
            return MyJsonBean.error("添加失败：" + e.getMessage());
        }
    }

    /**
     * 移除任务参与者
     */
    @ApiOperation("移除任务参与者")
    @DeleteMapping("/tasks/{taskId}/participants/{participantId}")
    public MyJsonBean<Boolean> removeTaskParticipant(
            @ApiParam("任务ID") @PathVariable @NotNull Long taskId,
            @ApiParam("参与者ID") @PathVariable @NotNull Long participantId) {
        try {
            boolean result = budgetCollaborationService.removeTaskParticipant(taskId, participantId);
            if (result) {
                return MyJsonBean.success("移除参与者成功", true);
            } else {
                return MyJsonBean.error("移除参与者失败");
            }
        } catch (Exception e) {
            log.error("移除任务参与者失败", e);
            return MyJsonBean.error("移除失败：" + e.getMessage());
        }
    }

    /**
     * 获取任务评论
     */
    @ApiOperation("获取任务评论")
    @GetMapping("/tasks/{taskId}/comments")
    public MyJsonBean<List<Map<String, Object>>> getTaskComments(
            @ApiParam("任务ID") @PathVariable @NotNull Long taskId,
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Long current,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Long size) {
        try {
            List<Map<String, Object>> result = budgetCollaborationService.getTaskComments(taskId, current, size);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取任务评论失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 添加任务评论
     */
    @ApiOperation("添加任务评论")
    @PostMapping("/tasks/{taskId}/comments")
    public MyJsonBean<Boolean> addTaskComment(
            @ApiParam("任务ID") @PathVariable @NotNull Long taskId,
            @ApiParam("评论信息") @RequestBody Map<String, Object> commentData) {
        try {
            boolean result = budgetCollaborationService.addTaskComment(taskId, commentData);
            if (result) {
                return MyJsonBean.success("添加评论成功", true);
            } else {
                return MyJsonBean.error("添加评论失败");
            }
        } catch (Exception e) {
            log.error("添加任务评论失败", e);
            return MyJsonBean.error("添加失败：" + e.getMessage());
        }
    }

    /**
     * 获取任务附件
     */
    @ApiOperation("获取任务附件")
    @GetMapping("/tasks/{taskId}/attachments")
    public MyJsonBean<List<Map<String, Object>>> getTaskAttachments(
            @ApiParam("任务ID") @PathVariable @NotNull Long taskId) {
        try {
            List<Map<String, Object>> result = budgetCollaborationService.getTaskAttachments(taskId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取任务附件失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 上传任务附件
     */
    @ApiOperation("上传任务附件")
    @PostMapping("/tasks/{taskId}/attachments")
    public MyJsonBean<Boolean> uploadTaskAttachment(
            @ApiParam("任务ID") @PathVariable @NotNull Long taskId,
            @ApiParam("附件信息") @RequestBody Map<String, Object> attachmentData) {
        try {
            boolean result = budgetCollaborationService.uploadTaskAttachment(taskId, attachmentData);
            if (result) {
                return MyJsonBean.success("上传附件成功", true);
            } else {
                return MyJsonBean.error("上传附件失败");
            }
        } catch (Exception e) {
            log.error("上传任务附件失败", e);
            return MyJsonBean.error("上传失败：" + e.getMessage());
        }
    }

    /**
     * 删除任务附件
     */
    @ApiOperation("删除任务附件")
    @DeleteMapping("/tasks/{taskId}/attachments/{attachmentId}")
    public MyJsonBean<Boolean> deleteTaskAttachment(
            @ApiParam("任务ID") @PathVariable @NotNull Long taskId,
            @ApiParam("附件ID") @PathVariable @NotNull Long attachmentId) {
        try {
            boolean result = budgetCollaborationService.deleteTaskAttachment(taskId, attachmentId);
            if (result) {
                return MyJsonBean.success("删除附件成功", true);
            } else {
                return MyJsonBean.error("删除附件失败");
            }
        } catch (Exception e) {
            log.error("删除任务附件失败", e);
            return MyJsonBean.error("删除失败：" + e.getMessage());
        }
    }

    /**
     * 获取协同统计
     */
    @ApiOperation("获取协同统计")
    @GetMapping("/statistics")
    public MyJsonBean<Map<String, Object>> getCollaborationStatistics(
            @ApiParam("预算体系ID") @RequestParam(required = false) Long systemId,
            @ApiParam("开始日期") @RequestParam(required = false) String startDate,
            @ApiParam("结束日期") @RequestParam(required = false) String endDate) {
        try {
            Map<String, Object> result = budgetCollaborationService.getCollaborationStatistics(systemId, startDate, endDate);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取协同统计失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 获取我的任务
     */
    @ApiOperation("获取我的任务")
    @GetMapping("/my-tasks")
    public MyJsonBean<List<EpsBudgetCollaborationTask>> getMyTasks(
            @ApiParam("用户ID") @RequestParam @NotNull Long userId,
            @ApiParam("任务状态") @RequestParam(required = false) String taskStatus) {
        try {
            List<EpsBudgetCollaborationTask> result = budgetCollaborationService.getMyTasks(userId, taskStatus);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取我的任务失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 批量操作任务
     */
    @ApiOperation("批量操作任务")
    @PostMapping("/tasks/batch-operation")
    public MyJsonBean<Map<String, Object>> batchOperateTasks(
            @ApiParam("批量操作数据") @RequestBody Map<String, Object> batchData) {
        try {
            Map<String, Object> result = budgetCollaborationService.batchOperateTasks(batchData);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("批量操作任务失败", e);
            return MyJsonBean.error("操作失败：" + e.getMessage());
        }
    }
}
