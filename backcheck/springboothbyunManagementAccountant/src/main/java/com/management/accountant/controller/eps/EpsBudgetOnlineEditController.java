package com.management.accountant.controller.eps;

import com.management.accountant.util.MyJsonBean;
import com.management.accountant.service.eps.EpsBudgetOnlineEditService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;

/**
 * 预算在线编辑控制器
 * 
 * @author 华博云
 * @version 3.0.0
 */
@Slf4j
@Api(tags = "预算在线编辑")
@RestController
@RequestMapping("/eps/budget-online-edit")
@Validated
public class EpsBudgetOnlineEditController {

    @Autowired
    private EpsBudgetOnlineEditService budgetOnlineEditService;

    /**
     * 获取预算编辑表格数据
     */
    @ApiOperation("获取预算编辑表格数据")
    @GetMapping("/table-data")
    public MyJsonBean<Map<String, Object>> getBudgetTableData(
            @ApiParam("预算体系ID") @RequestParam @NotNull Long systemId,
            @ApiParam("预算版本ID") @RequestParam @NotNull Long versionId,
            @ApiParam("模板ID") @RequestParam(required = false) Long templateId,
            @ApiParam("组织ID") @RequestParam(required = false) Long orgId,
            @ApiParam("科目ID列表") @RequestParam(required = false) List<Long> subjectIds,
            @ApiParam("期间") @RequestParam(required = false) String period) {
        try {
            Map<String, Object> result = budgetOnlineEditService.getBudgetTableData(
                    systemId, versionId, templateId, orgId, subjectIds, period);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取预算编辑表格数据失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 保存单元格数据
     */
    @ApiOperation("保存单元格数据")
    @PostMapping("/save-cell")
    public MyJsonBean<Boolean> saveCellData(
            @ApiParam("单元格数据") @RequestBody Map<String, Object> cellData) {
        try {
            boolean result = budgetOnlineEditService.saveCellData(cellData);
            if (result) {
                return MyJsonBean.success("保存成功", true);
            } else {
                return MyJsonBean.error("保存失败");
            }
        } catch (Exception e) {
            log.error("保存单元格数据失败", e);
            return MyJsonBean.error("保存失败：" + e.getMessage());
        }
    }

    /**
     * 批量保存表格数据
     */
    @ApiOperation("批量保存表格数据")
    @PostMapping("/batch-save")
    public MyJsonBean<Map<String, Object>> batchSaveTableData(
            @ApiParam("表格数据") @RequestBody Map<String, Object> tableData) {
        try {
            Map<String, Object> result = budgetOnlineEditService.batchSaveTableData(tableData);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("批量保存表格数据失败", e);
            return MyJsonBean.error("保存失败：" + e.getMessage());
        }
    }

    /**
     * 获取编辑锁状态
     */
    @ApiOperation("获取编辑锁状态")
    @GetMapping("/lock-status")
    public MyJsonBean<Map<String, Object>> getEditLockStatus(
            @ApiParam("预算体系ID") @RequestParam @NotNull Long systemId,
            @ApiParam("预算版本ID") @RequestParam @NotNull Long versionId,
            @ApiParam("组织ID") @RequestParam(required = false) Long orgId,
            @ApiParam("科目ID") @RequestParam(required = false) Long subjectId) {
        try {
            Map<String, Object> result = budgetOnlineEditService.getEditLockStatus(
                    systemId, versionId, orgId, subjectId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取编辑锁状态失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 申请编辑锁
     */
    @ApiOperation("申请编辑锁")
    @PostMapping("/acquire-lock")
    public MyJsonBean<Boolean> acquireEditLock(
            @ApiParam("预算体系ID") @RequestParam @NotNull Long systemId,
            @ApiParam("预算版本ID") @RequestParam @NotNull Long versionId,
            @ApiParam("组织ID") @RequestParam(required = false) Long orgId,
            @ApiParam("科目ID") @RequestParam(required = false) Long subjectId,
            @ApiParam("用户ID") @RequestParam @NotNull Long userId) {
        try {
            boolean result = budgetOnlineEditService.acquireEditLock(systemId, versionId, orgId, subjectId, userId);
            if (result) {
                return MyJsonBean.success("获取编辑锁成功", true);
            } else {
                return MyJsonBean.error("获取编辑锁失败，可能已被其他用户锁定");
            }
        } catch (Exception e) {
            log.error("申请编辑锁失败", e);
            return MyJsonBean.error("申请失败：" + e.getMessage());
        }
    }

    /**
     * 释放编辑锁
     */
    @ApiOperation("释放编辑锁")
    @PostMapping("/release-lock")
    public MyJsonBean<Boolean> releaseEditLock(
            @ApiParam("预算体系ID") @RequestParam @NotNull Long systemId,
            @ApiParam("预算版本ID") @RequestParam @NotNull Long versionId,
            @ApiParam("组织ID") @RequestParam(required = false) Long orgId,
            @ApiParam("科目ID") @RequestParam(required = false) Long subjectId,
            @ApiParam("用户ID") @RequestParam @NotNull Long userId) {
        try {
            boolean result = budgetOnlineEditService.releaseEditLock(systemId, versionId, orgId, subjectId, userId);
            if (result) {
                return MyJsonBean.success("释放编辑锁成功", true);
            } else {
                return MyJsonBean.error("释放编辑锁失败");
            }
        } catch (Exception e) {
            log.error("释放编辑锁失败", e);
            return MyJsonBean.error("释放失败：" + e.getMessage());
        }
    }

    /**
     * 强制释放编辑锁
     */
    @ApiOperation("强制释放编辑锁")
    @PostMapping("/force-release-lock")
    public MyJsonBean<Boolean> forceReleaseEditLock(
            @ApiParam("预算体系ID") @RequestParam @NotNull Long systemId,
            @ApiParam("预算版本ID") @RequestParam @NotNull Long versionId,
            @ApiParam("组织ID") @RequestParam(required = false) Long orgId,
            @ApiParam("科目ID") @RequestParam(required = false) Long subjectId,
            @ApiParam("操作用户ID") @RequestParam @NotNull Long operatorUserId) {
        try {
            boolean result = budgetOnlineEditService.forceReleaseEditLock(
                    systemId, versionId, orgId, subjectId, operatorUserId);
            if (result) {
                return MyJsonBean.success("强制释放编辑锁成功", true);
            } else {
                return MyJsonBean.error("强制释放编辑锁失败");
            }
        } catch (Exception e) {
            log.error("强制释放编辑锁失败", e);
            return MyJsonBean.error("强制释放失败：" + e.getMessage());
        }
    }

    /**
     * 获取编辑历史
     */
    @ApiOperation("获取编辑历史")
    @GetMapping("/edit-history")
    public MyJsonBean<List<Map<String, Object>>> getEditHistory(
            @ApiParam("预算体系ID") @RequestParam @NotNull Long systemId,
            @ApiParam("预算版本ID") @RequestParam @NotNull Long versionId,
            @ApiParam("组织ID") @RequestParam(required = false) Long orgId,
            @ApiParam("科目ID") @RequestParam(required = false) Long subjectId,
            @ApiParam("开始日期") @RequestParam(required = false) String startDate,
            @ApiParam("结束日期") @RequestParam(required = false) String endDate,
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Long current,
            @ApiParam("每页大小") @RequestParam(defaultValue = "10") Long size) {
        try {
            List<Map<String, Object>> result = budgetOnlineEditService.getEditHistory(
                    systemId, versionId, orgId, subjectId, startDate, endDate, current, size);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取编辑历史失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 撤销编辑操作
     */
    @ApiOperation("撤销编辑操作")
    @PostMapping("/undo")
    public MyJsonBean<Boolean> undoEdit(
            @ApiParam("编辑历史ID") @RequestParam @NotNull Long editHistoryId,
            @ApiParam("用户ID") @RequestParam @NotNull Long userId) {
        try {
            boolean result = budgetOnlineEditService.undoEdit(editHistoryId, userId);
            if (result) {
                return MyJsonBean.success("撤销成功", true);
            } else {
                return MyJsonBean.error("撤销失败");
            }
        } catch (Exception e) {
            log.error("撤销编辑操作失败", e);
            return MyJsonBean.error("撤销失败：" + e.getMessage());
        }
    }

    /**
     * 重做编辑操作
     */
    @ApiOperation("重做编辑操作")
    @PostMapping("/redo")
    public MyJsonBean<Boolean> redoEdit(
            @ApiParam("编辑历史ID") @RequestParam @NotNull Long editHistoryId,
            @ApiParam("用户ID") @RequestParam @NotNull Long userId) {
        try {
            boolean result = budgetOnlineEditService.redoEdit(editHistoryId, userId);
            if (result) {
                return MyJsonBean.success("重做成功", true);
            } else {
                return MyJsonBean.error("重做失败");
            }
        } catch (Exception e) {
            log.error("重做编辑操作失败", e);
            return MyJsonBean.error("重做失败：" + e.getMessage());
        }
    }

    /**
     * 获取在线用户列表
     */
    @ApiOperation("获取在线用户列表")
    @GetMapping("/online-users")
    public MyJsonBean<List<Map<String, Object>>> getOnlineUsers(
            @ApiParam("预算体系ID") @RequestParam @NotNull Long systemId,
            @ApiParam("预算版本ID") @RequestParam @NotNull Long versionId) {
        try {
            List<Map<String, Object>> result = budgetOnlineEditService.getOnlineUsers(systemId, versionId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取在线用户列表失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 发送协作消息
     */
    @ApiOperation("发送协作消息")
    @PostMapping("/send-message")
    public MyJsonBean<Boolean> sendCollaborationMessage(
            @ApiParam("消息数据") @RequestBody Map<String, Object> messageData) {
        try {
            boolean result = budgetOnlineEditService.sendCollaborationMessage(messageData);
            if (result) {
                return MyJsonBean.success("消息发送成功", true);
            } else {
                return MyJsonBean.error("消息发送失败");
            }
        } catch (Exception e) {
            log.error("发送协作消息失败", e);
            return MyJsonBean.error("发送失败：" + e.getMessage());
        }
    }

    /**
     * 获取协作消息
     */
    @ApiOperation("获取协作消息")
    @GetMapping("/messages")
    public MyJsonBean<List<Map<String, Object>>> getCollaborationMessages(
            @ApiParam("预算体系ID") @RequestParam @NotNull Long systemId,
            @ApiParam("预算版本ID") @RequestParam @NotNull Long versionId,
            @ApiParam("用户ID") @RequestParam @NotNull Long userId,
            @ApiParam("最后消息ID") @RequestParam(required = false) Long lastMessageId) {
        try {
            List<Map<String, Object>> result = budgetOnlineEditService.getCollaborationMessages(
                    systemId, versionId, userId, lastMessageId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取协作消息失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 验证数据有效性
     */
    @ApiOperation("验证数据有效性")
    @PostMapping("/validate-data")
    public MyJsonBean<Map<String, Object>> validateData(
            @ApiParam("验证数据") @RequestBody Map<String, Object> validationData) {
        try {
            Map<String, Object> result = budgetOnlineEditService.validateData(validationData);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("验证数据有效性失败", e);
            return MyJsonBean.error("验证失败：" + e.getMessage());
        }
    }

    /**
     * 自动保存设置
     */
    @ApiOperation("自动保存设置")
    @PostMapping("/auto-save-settings")
    public MyJsonBean<Boolean> setAutoSaveSettings(
            @ApiParam("用户ID") @RequestParam @NotNull Long userId,
            @ApiParam("自动保存间隔(秒)") @RequestParam Integer autoSaveInterval,
            @ApiParam("是否启用自动保存") @RequestParam Boolean enableAutoSave) {
        try {
            boolean result = budgetOnlineEditService.setAutoSaveSettings(userId, autoSaveInterval, enableAutoSave);
            if (result) {
                return MyJsonBean.success("设置成功", true);
            } else {
                return MyJsonBean.error("设置失败");
            }
        } catch (Exception e) {
            log.error("设置自动保存失败", e);
            return MyJsonBean.error("设置失败：" + e.getMessage());
        }
    }

    /**
     * 获取自动保存设置
     */
    @ApiOperation("获取自动保存设置")
    @GetMapping("/auto-save-settings")
    public MyJsonBean<Map<String, Object>> getAutoSaveSettings(
            @ApiParam("用户ID") @RequestParam @NotNull Long userId) {
        try {
            Map<String, Object> result = budgetOnlineEditService.getAutoSaveSettings(userId);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取自动保存设置失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }

    /**
     * 清理过期锁
     */
    @ApiOperation("清理过期锁")
    @PostMapping("/cleanup-expired-locks")
    public MyJsonBean<Integer> cleanupExpiredLocks(
            @ApiParam("锁过期时间(分钟)") @RequestParam(defaultValue = "30") Integer lockExpirationMinutes) {
        try {
            int cleanupCount = budgetOnlineEditService.cleanupExpiredLocks(lockExpirationMinutes);
            return MyJsonBean.success("清理完成，共清理" + cleanupCount + "个过期锁", cleanupCount);
        } catch (Exception e) {
            log.error("清理过期锁失败", e);
            return MyJsonBean.error("清理失败：" + e.getMessage());
        }
    }

    /**
     * 获取编辑统计信息
     */
    @ApiOperation("获取编辑统计信息")
    @GetMapping("/edit-statistics")
    public MyJsonBean<Map<String, Object>> getEditStatistics(
            @ApiParam("预算体系ID") @RequestParam @NotNull Long systemId,
            @ApiParam("预算版本ID") @RequestParam @NotNull Long versionId,
            @ApiParam("开始日期") @RequestParam(required = false) String startDate,
            @ApiParam("结束日期") @RequestParam(required = false) String endDate) {
        try {
            Map<String, Object> result = budgetOnlineEditService.getEditStatistics(
                    systemId, versionId, startDate, endDate);
            return MyJsonBean.success(result);
        } catch (Exception e) {
            log.error("获取编辑统计信息失败", e);
            return MyJsonBean.error("获取失败：" + e.getMessage());
        }
    }
}
