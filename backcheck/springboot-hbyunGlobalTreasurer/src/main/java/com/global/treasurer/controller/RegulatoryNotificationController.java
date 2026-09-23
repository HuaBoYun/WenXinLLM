package com.global.treasurer.controller;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblRegulatoryNotification;
import com.global.treasurer.service.RegulatoryNotificationService;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 监管通知Controller
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
@Controller
@RequestMapping({"/regulatory/notification", "/globalTreasurer/regulatory/notification"})
@Api(tags = "监管通知管理")
public class RegulatoryNotificationController {
    private static final Logger log = LoggerFactory.getLogger(RegulatoryNotificationController.class);

    @Resource
    private RegulatoryNotificationService notificationService;

    @Resource
    private UserProvider userProvider;

    @GetMapping("/list")
    @ResponseBody
    @ApiOperation("分页查询监管通知列表")
    public String getNotificationList(@RequestParam(required = false) String notificationType,
                                      @RequestParam(required = false) String notificationStatus,
                                      @RequestParam(required = false) String priorityLevel,
                                      @RequestParam(defaultValue = "1") Integer pageNum,
                                      @RequestParam(defaultValue = "10") Integer pageSize,
                                      @RequestHeader(value = "token", required = false) String token,
                                      HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            Map<String, Object> params = new HashMap<>();
            params.put("notificationType", notificationType);
            params.put("notificationStatus", notificationStatus);
            params.put("priorityLevel", priorityLevel);
            params.put("pageNum", pageNum);
            params.put("pageSize", pageSize);

            PageInfo<TblRegulatoryNotification> pageInfo = notificationService.getNotificationList(params);
            Map<String, Object> pageData = new HashMap<>();
            pageData.put("rows", pageInfo.getList());
            pageData.put("total", pageInfo.getTotal());
            return JsonBean.success(pageData);
        } catch (Exception e) {
            log.error("查询监管通知列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/{notificationId}")
    @ResponseBody
    @ApiOperation("根据ID获取监管通知详情")
    public String getNotificationById(@PathVariable String notificationId,
                                      @RequestHeader(value = "token", required = false) String token,
                                      HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            TblRegulatoryNotification notification = notificationService.getNotificationById(notificationId);
            return new JsonBean(1, "成功", notification).toJson();
        } catch (Exception e) {
            log.error("获取监管通知详情失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("")
    @ResponseBody
    @ApiOperation("新增监管通知")
    public String addNotification(@FlexibleRequestBody TblRegulatoryNotification notification,
                                  @RequestHeader(value = "token", required = false) String token,
                                  HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            notification.setNotificationId(null);
            TblRegulatoryNotification saved = notificationService.saveNotification(notification);
            return new JsonBean(1, "新增成功", saved).toJson();
        } catch (Exception e) {
            log.error("新增监管通知失败", e);
            return new JsonBean(0, "新增失败: " + e.getMessage(), null).toJson();
        }
    }

    @PutMapping("")
    @ResponseBody
    @ApiOperation("修改监管通知")
    public String updateNotification(@FlexibleRequestBody TblRegulatoryNotification notification,
                                     @RequestHeader(value = "token", required = false) String token,
                                     HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            if (notification.getNotificationId() == null || notification.getNotificationId().isEmpty()) {
                return new JsonBean(0, "通知ID不能为空", null).toJson();
            }

            TblRegulatoryNotification saved = notificationService.saveNotification(notification);
            return new JsonBean(1, "修改成功", saved).toJson();
        } catch (Exception e) {
            log.error("修改监管通知失败", e);
            return new JsonBean(0, "修改失败: " + e.getMessage(), null).toJson();
        }
    }

    @DeleteMapping("/{notificationIds}")
    @ResponseBody
    @ApiOperation("删除监管通知")
    public String deleteNotification(@PathVariable String notificationIds,
                                     @RequestHeader(value = "token", required = false) String token,
                                     HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            String[] ids = notificationIds.split(",");
            if (ids.length == 1) {
                notificationService.deleteNotification(ids[0]);
            } else {
                notificationService.batchDeleteNotifications(Arrays.asList(ids));
            }
            return new JsonBean(1, "删除成功", null).toJson();
        } catch (Exception e) {
            log.error("删除监管通知失败", e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/unread")
    @ResponseBody
    @ApiOperation("查询未读通知")
    public String getUnreadNotifications(@RequestHeader(value = "token", required = false) String token,
                                         HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            List<TblRegulatoryNotification> notifications = notificationService.getUnreadNotifications();
            return new JsonBean(1, "成功", notifications).toJson();
        } catch (Exception e) {
            log.error("查询未读通知失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/urgent")
    @ResponseBody
    @ApiOperation("查询紧急通知")
    public String getUrgentNotifications(@RequestHeader(value = "token", required = false) String token,
                                         HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            List<TblRegulatoryNotification> notifications = notificationService.getUrgentNotifications();
            return new JsonBean(1, "成功", notifications).toJson();
        } catch (Exception e) {
            log.error("查询紧急通知失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/{notificationId}/read")
    @ResponseBody
    @ApiOperation("标记通知为已读")
    public String markAsRead(@PathVariable String notificationId,
                             @RequestHeader(value = "token", required = false) String token,
                             HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            notificationService.markAsRead(notificationId);
            return new JsonBean(1, "标记成功", null).toJson();
        } catch (Exception e) {
            log.error("标记通知为已读失败", e);
            return new JsonBean(0, "标记失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/batch-read")
    @ResponseBody
    @ApiOperation("批量标记通知为已读")
    public String batchMarkAsRead(@RequestParam(value = "notificationIds", required = false) List<String> notificationIds,
                                  @RequestHeader(value = "token", required = false) String token,
                                  HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            notificationService.batchMarkAsRead(notificationIds);
            return new JsonBean(1, "批量标记成功", null).toJson();
        } catch (Exception e) {
            log.error("批量标记通知为已读失败", e);
            return new JsonBean(0, "批量标记失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/{notificationId}/process")
    @ResponseBody
    @ApiOperation("处理通知")
    public String processNotification(@PathVariable String notificationId,
                                      @RequestParam(required = false) String processNote,
                                      @RequestHeader(value = "token", required = false) String token,
                                      HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            notificationService.processNotification(notificationId, processNote);
            return new JsonBean(1, "处理成功", null).toJson();
        } catch (Exception e) {
            log.error("处理通知失败", e);
            return new JsonBean(0, "处理失败: " + e.getMessage(), null).toJson();
        }
    }
}
