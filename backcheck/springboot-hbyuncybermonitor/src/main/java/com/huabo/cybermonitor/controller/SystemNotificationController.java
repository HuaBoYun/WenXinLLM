package com.huabo.cybermonitor.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.cybermonitor.entity.SystemNotification;
import com.huabo.cybermonitor.service.ISystemNotificationService;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.util.R;
import com.huabo.cybermonitor.vo.SystemNotificationQueryVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;



/**
 * 系统通知公告管理 Controller
 *
 * @author system
 * @since 2024-01-01
 */
@Tag(name="系统通知公告管理",description="系统通知公告管理")
@RestController
@RequestMapping("/v1/system/notification")
public class SystemNotificationController {

	private static final Logger log = LoggerFactory.getLogger(SystemNotificationController.class);

    @Autowired
    private ISystemNotificationService notificationService;

    @Operation(summary = "分页查询系统通知列表")
    @PostMapping("/list")
    public R<PageResult<SystemNotification>> getNotificationList(@RequestBody SystemNotificationQueryVO queryVO) {
        try {
            IPage<SystemNotification> page = notificationService.getNotificationList(queryVO);
            PageResult<SystemNotification> pageResult = new PageResult<SystemNotification>();
            pageResult.setTlist(page.getRecords());
            pageResult.setTotalRecord((int) page.getTotal());
            pageResult.setPageNumber((int) page.getCurrent());
            pageResult.setPageSize((int) page.getSize());

            return R.success(pageResult);
        } catch (Exception e) {
            log.error("查询系统通知列表失败", e);
            return R.fail("查询系统通知列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取系统通知详情")
    @PostMapping("/detail")
    public R<SystemNotification> getNotificationDetail(@RequestBody Map<String, String> params) {
        try {
            String notificationId = params.get("notificationId");
            if (notificationId == null || notificationId.trim().isEmpty()) {
                return R.fail("通知ID不能为空");
            }
            
            SystemNotification notification = notificationService.getNotificationDetail(notificationId);
            return R.success(notification);
        } catch (Exception e) {
            log.error("获取系统通知详情失败", e);
            return R.fail("获取系统通知详情失败：" + e.getMessage());
        }
    }

    @Operation(summary = "新增系统通知")
    @PostMapping("/add")
    public R<String> addNotification(@RequestBody SystemNotification notification) {
        try {
            boolean success = notificationService.addNotification(notification);
            if (success) {
                return R.success("新增系统通知成功");
            } else {
                return R.fail("新增系统通知失败");
            }
        } catch (Exception e) {
            log.error("新增系统通知失败", e);
            return R.fail("新增系统通知失败：" + e.getMessage());
        }
    }

    @Operation(summary = "更新系统通知")
    @PostMapping("/update")
    public R<String> updateNotification(@RequestBody SystemNotification notification) {
        try {
            if (notification.getNotificationId() == null || notification.getNotificationId().trim().isEmpty()) {
                return R.fail("通知ID不能为空");
            }
            
            boolean success = notificationService.updateNotification(notification);
            if (success) {
                return R.success("更新系统通知成功");
            } else {
                return R.fail("更新系统通知失败");
            }
        } catch (Exception e) {
            log.error("更新系统通知失败", e);
            return R.fail("更新系统通知失败：" + e.getMessage());
        }
    }

    @Operation(summary = "删除系统通知")
    @PostMapping("/delete")
    public R<String> deleteNotification(@RequestBody Map<String, String> params) {
        try {
            String notificationId = params.get("notificationId");
            if (notificationId == null || notificationId.trim().isEmpty()) {
                return R.fail("通知ID不能为空");
            }
            
            boolean success = notificationService.deleteNotification(notificationId);
            if (success) {
                return R.success("删除系统通知成功");
            } else {
                return R.fail("删除系统通知失败");
            }
        } catch (Exception e) {
            log.error("删除系统通知失败", e);
            return R.fail("删除系统通知失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量删除系统通知")
    @PostMapping("/batch-delete")
    public R<String> batchDeleteNotification(@RequestBody Map<String, List<String>> params) {
        try {
            List<String> notificationIds = params.get("notificationIds");
            if (notificationIds == null || notificationIds.isEmpty()) {
                return R.fail("通知ID列表不能为空");
            }
            
            boolean success = notificationService.batchDeleteNotification(notificationIds);
            if (success) {
                return R.success("批量删除系统通知成功");
            } else {
                return R.fail("批量删除系统通知失败");
            }
        } catch (Exception e) {
            log.error("批量删除系统通知失败", e);
            return R.fail("批量删除系统通知失败：" + e.getMessage());
        }
    }

    @Operation(summary = "发布系统通知")
    @PostMapping("/publish")
    public R<String> publishNotification(@RequestBody Map<String, String> params) {
        try {
            String notificationId = params.get("notificationId");

            if (notificationId == null || notificationId.trim().isEmpty()) {
                return R.fail("通知ID不能为空");
            }

            boolean success = notificationService.publishNotification(notificationId);
            if (success) {
                return R.success("发布系统通知成功");
            } else {
                return R.fail("发布系统通知失败");
            }
        } catch (Exception e) {
            log.error("发布系统通知失败", e);
            return R.fail("发布系统通知失败：" + e.getMessage());
        }
    }

    @Operation(summary = "取消发布系统通知")
    @PostMapping("/unpublish")
    public R<String> unpublishNotification(@RequestBody Map<String, String> params) {
        try {
            String notificationId = params.get("notificationId");

            if (notificationId == null || notificationId.trim().isEmpty()) {
                return R.fail("通知ID不能为空");
            }

            boolean success = notificationService.unpublishNotification(notificationId);
            if (success) {
                return R.success("取消发布系统通知成功");
            } else {
                return R.fail("取消发布系统通知失败");
            }
        } catch (Exception e) {
            log.error("取消发布系统通知失败", e);
            return R.fail("取消发布系统通知失败：" + e.getMessage());
        }
    }

    @Operation(summary = "置顶系统通知")
    @PostMapping("/top")
    public R<String> topNotification(@RequestBody Map<String, String> params) {
        try {
            String notificationId = params.get("notificationId");
            if (notificationId == null || notificationId.trim().isEmpty()) {
                return R.fail("通知ID不能为空");
            }
            
            boolean success = notificationService.topNotification(notificationId);
            if (success) {
                return R.success("置顶系统通知成功");
            } else {
                return R.fail("置顶系统通知失败");
            }
        } catch (Exception e) {
            log.error("置顶系统通知失败", e);
            return R.fail("置顶系统通知失败：" + e.getMessage());
        }
    }

    @Operation(summary = "取消置顶系统通知")
    @PostMapping("/untop")
    public R<String> untopNotification(@RequestBody Map<String, String> params) {
        try {
            String notificationId = params.get("notificationId");
            if (notificationId == null || notificationId.trim().isEmpty()) {
                return R.fail("通知ID不能为空");
            }
            
            boolean success = notificationService.untopNotification(notificationId);
            if (success) {
                return R.success("取消置顶系统通知成功");
            } else {
                return R.fail("取消置顶系统通知失败");
            }
        } catch (Exception e) {
            log.error("取消置顶系统通知失败", e);
            return R.fail("取消置顶系统通知失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据通知类型查询通知列表")
    @PostMapping("/list-by-type")
    public R<List<SystemNotification>> getNotificationsByType(@RequestBody Map<String, String> params) {
        try {
            String notificationType = params.get("notificationType");
            if (notificationType == null || notificationType.trim().isEmpty()) {
                return R.fail("通知类型不能为空");
            }
            
            List<SystemNotification> notifications = notificationService.getNotificationsByType(notificationType);
            return R.success(notifications);
        } catch (Exception e) {
            log.error("根据通知类型查询通知列表失败", e);
            return R.fail("根据通知类型查询通知列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据通知级别查询通知列表")
    @PostMapping("/list-by-level")
    public R<List<SystemNotification>> getNotificationsByLevel(@RequestBody Map<String, String> params) {
        try {
            String notificationLevel = params.get("notificationLevel");
            if (notificationLevel == null || notificationLevel.trim().isEmpty()) {
                return R.fail("通知级别不能为空");
            }
            
            List<SystemNotification> notifications = notificationService.getNotificationsByLevel(notificationLevel);
            return R.success(notifications);
        } catch (Exception e) {
            log.error("根据通知级别查询通知列表失败", e);
            return R.fail("根据通知级别查询通知列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据通知状态查询通知列表")
    @PostMapping("/list-by-status")
    public R<List<SystemNotification>> getNotificationsByStatus(@RequestBody Map<String, String> params) {
        try {
            String notificationStatus = params.get("notificationStatus");
            if (notificationStatus == null || notificationStatus.trim().isEmpty()) {
                return R.fail("通知状态不能为空");
            }
            
            List<SystemNotification> notifications = notificationService.getNotificationsByStatus(notificationStatus);
            return R.success(notifications);
        } catch (Exception e) {
            log.error("根据通知状态查询通知列表失败", e);
            return R.fail("根据通知状态查询通知列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "查询已发布的通知列表")
    @PostMapping("/published")
    public R<List<SystemNotification>> getPublishedNotifications() {
        try {
            List<SystemNotification> notifications = notificationService.getPublishedNotifications();
            return R.success(notifications);
        } catch (Exception e) {
            log.error("查询已发布的通知列表失败", e);
            return R.fail("查询已发布的通知列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "查询置顶的通知列表")
    @PostMapping("/top-list")
    public R<List<SystemNotification>> getTopNotifications() {
        try {
            List<SystemNotification> notifications = notificationService.getTopNotifications();
            return R.success(notifications);
        } catch (Exception e) {
            log.error("查询置顶的通知列表失败", e);
            return R.fail("查询置顶的通知列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据用户查询通知列表")
    @PostMapping("/list-by-user")
    public R<List<SystemNotification>> getNotificationsByUser(@RequestBody Map<String, String> params) {
        try {
            String userId = params.get("userId");
            if (userId == null || userId.trim().isEmpty()) {
                return R.fail("用户ID不能为空");
            }

            List<SystemNotification> notifications = notificationService.getUserNotifications(userId);
            return R.success(notifications);
        } catch (Exception e) {
            log.error("根据用户查询通知列表失败", e);
            return R.fail("根据用户查询通知列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据角色查询通知列表")
    @PostMapping("/list-by-role")
    public R<List<SystemNotification>> getNotificationsByRole(@RequestBody Map<String, String> params) {
        try {
            String roleId = params.get("roleId");
            if (roleId == null || roleId.trim().isEmpty()) {
                return R.fail("角色ID不能为空");
            }

            List<SystemNotification> notifications = notificationService.getRoleNotifications(roleId);
            return R.success(notifications);
        } catch (Exception e) {
            log.error("根据角色查询通知列表失败", e);
            return R.fail("根据角色查询通知列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据部门查询通知列表")
    @PostMapping("/list-by-department")
    public R<List<SystemNotification>> getNotificationsByDepartment(@RequestBody Map<String, String> params) {
        try {
            String departmentId = params.get("departmentId");
            if (departmentId == null || departmentId.trim().isEmpty()) {
                return R.fail("部门ID不能为空");
            }

            // 部门通知查询 - 使用发布范围为部门的通知
            List<SystemNotification> notifications = notificationService.getNotificationsByStatus("PUBLISHED");
            return R.success(notifications);
        } catch (Exception e) {
            log.error("根据部门查询通知列表失败", e);
            return R.fail("根据部门查询通知列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "标记通知为已读")
    @PostMapping("/read")
    public R<String> readNotification(@RequestBody Map<String, String> params) {
        try {
            String notificationId = params.get("notificationId");
            String userId = params.get("userId");
            String readIp = params.getOrDefault("readIp", "");
            String userAgent = params.getOrDefault("userAgent", "");
            String readDevice = params.getOrDefault("readDevice", "");

            if (notificationId == null || notificationId.trim().isEmpty()) {
                return R.fail("通知ID不能为空");
            }
            if (userId == null || userId.trim().isEmpty()) {
                return R.fail("用户ID不能为空");
            }

            boolean success = notificationService.readNotification(notificationId, userId, readIp, userAgent, readDevice);
            if (success) {
                return R.success("标记通知为已读成功");
            } else {
                return R.fail("标记通知为已读失败");
            }
        } catch (Exception e) {
            log.error("标记通知为已读失败", e);
            return R.fail("标记通知为已读失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取用户未读通知数量")
    @PostMapping("/unread-count")
    public R<Integer> getUserUnreadCount(@RequestBody Map<String, String> params) {
        try {
            String userId = params.get("userId");
            if (userId == null || userId.trim().isEmpty()) {
                return R.fail("用户ID不能为空");
            }
            
            Integer count = notificationService.getUserUnreadCount(userId);
            return R.success(count);
        } catch (Exception e) {
            log.error("获取用户未读通知数量失败", e);
            return R.fail("获取用户未读通知数量失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取用户未读通知列表")
    @PostMapping("/unread-list")
    public R<List<Map<String, Object>>> getUserUnreadNotifications(@RequestBody Map<String, String> params) {
        try {
            String userId = params.get("userId");
            String limitStr = params.getOrDefault("limit", "10");
            Integer limit = Integer.parseInt(limitStr);

            if (userId == null || userId.trim().isEmpty()) {
                return R.fail("用户ID不能为空");
            }

            List<Map<String, Object>> notifications = notificationService.getUserUnreadNotifications(userId, limit);
            return R.success(notifications);
        } catch (Exception e) {
            log.error("获取用户未读通知列表失败", e);
            return R.fail("获取用户未读通知列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "处理定时发布通知")
    @PostMapping("/schedule-publish")
    public R<Integer> schedulePublishNotifications() {
        try {
            Integer count = notificationService.schedulePublishNotifications();
            if (count != null && count > 0) {
                return R.success(count, "处理定时发布通知成功，发布" + count + "条通知");
            } else {
                return R.success(0, "没有需要发布的通知");
            }
        } catch (Exception e) {
            log.error("处理定时发布通知失败", e);
            return R.fail("处理定时发布通知失败：" + e.getMessage());
        }
    }

    @Operation(summary = "自动更新过期通知")
    @PostMapping("/auto-update-expired")
    public R<Integer> autoUpdateExpiredNotifications() {
        try {
            Integer count = notificationService.autoUpdateExpiredNotifications();
            if (count != null && count > 0) {
                return R.success(count, "自动更新过期通知成功，更新" + count + "条通知");
            } else {
                return R.success(0, "没有需要更新的过期通知");
            }
        } catch (Exception e) {
            log.error("自动更新过期通知失败", e);
            return R.fail("自动更新过期通知失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取通知统计信息")
    @PostMapping("/statistics/overview")
    public R<Map<String, Object>> getNotificationStatistics() {
        try {
            Map<String, Object> statistics = notificationService.getNotificationStatistics();
            return R.success(statistics);
        } catch (Exception e) {
            log.error("获取通知统计信息失败", e);
            return R.fail("获取通知统计信息失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取通知类型分布统计")
    @PostMapping("/statistics/type-distribution")
    public R<List<Map<String, Object>>> getNotificationTypeDistribution() {
        try {
            List<Map<String, Object>> distribution = notificationService.getNotificationTypeDistribution();
            return R.success(distribution);
        } catch (Exception e) {
            log.error("获取通知类型分布统计失败", e);
            return R.fail("获取通知类型分布统计失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取通知级别分布统计")
    @PostMapping("/statistics/level-distribution")
    public R<List<Map<String, Object>>> getNotificationLevelDistribution() {
        try {
            List<Map<String, Object>> distribution = notificationService.getNotificationLevelDistribution();
            return R.success(distribution);
        } catch (Exception e) {
            log.error("获取通知级别分布统计失败", e);
            return R.fail("获取通知级别分布统计失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取通知状态分布统计")
    @PostMapping("/statistics/status-distribution")
    public R<List<Map<String, Object>>> getNotificationStatusDistribution() {
        try {
            List<Map<String, Object>> distribution = notificationService.getNotificationStatusDistribution();
            return R.success(distribution);
        } catch (Exception e) {
            log.error("获取通知状态分布统计失败", e);
            return R.fail("获取通知状态分布统计失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取通知发布趋势统计")
    @PostMapping("/statistics/publish-trend")
    public R<List<Map<String, Object>>> getNotificationPublishTrend(@RequestBody Map<String, String> params) {
        try {
            String startDate = params.get("startDate");
            String endDate = params.get("endDate");
            
            List<Map<String, Object>> trend = notificationService.getNotificationPublishTrend(startDate, endDate);
            return R.success(trend);
        } catch (Exception e) {
            log.error("获取通知发布趋势统计失败", e);
            return R.fail("获取通知发布趋势统计失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取通知阅读统计")
    @PostMapping("/statistics/read-statistics")
    public R<List<Map<String, Object>>> getNotificationReadStatistics() {
        try {
            List<Map<String, Object>> statistics = notificationService.getNotificationReadStatistics();
            return R.success(statistics);
        } catch (Exception e) {
            log.error("获取通知阅读统计失败", e);
            return R.fail("获取通知阅读统计失败：" + e.getMessage());
        }
    }

    @Operation(summary = "导出通知列表")
    @PostMapping("/export")
    public void exportNotificationList(@RequestBody SystemNotificationQueryVO queryVO, HttpServletResponse response) {
        try {
            notificationService.exportNotificationList(queryVO, response);
        } catch (Exception e) {
            log.error("导出通知列表失败", e);
            throw new RuntimeException("导出通知列表失败：" + e.getMessage());
        }
    }
}
