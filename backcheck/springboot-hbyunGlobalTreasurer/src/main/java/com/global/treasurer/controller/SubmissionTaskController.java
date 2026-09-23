package com.global.treasurer.controller;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblSubmissionTask;
import com.global.treasurer.service.SubmissionTaskService;
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
 * 报送任务Controller
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
@Controller
@RequestMapping({"/regulatory/task", "/globalTreasurer/regulatory/task"})
@Api(tags = "报送任务管理")
public class SubmissionTaskController {
    private static final Logger log = LoggerFactory.getLogger(SubmissionTaskController.class);

    @Resource
    private SubmissionTaskService taskService;

    @Resource
    private UserProvider userProvider;

    @GetMapping("/list")
    @ResponseBody
    @ApiOperation("分页查询报送任务列表")
    public String getTaskList(@RequestParam(required = false) String taskName,
                              @RequestParam(required = false) String taskStatus,
                              @RequestParam(required = false) String taskType,
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
            params.put("taskName", taskName);
            params.put("taskStatus", taskStatus);
            params.put("taskType", taskType);
            params.put("pageNum", pageNum);
            params.put("pageSize", pageSize);

            PageInfo<TblSubmissionTask> pageInfo = taskService.getTaskList(params);
            Map<String, Object> pageData = new HashMap<>();
            pageData.put("rows", pageInfo.getList());
            pageData.put("total", pageInfo.getTotal());
            return JsonBean.success(pageData);
        } catch (Exception e) {
            log.error("查询报送任务列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/{taskId}")
    @ResponseBody
    @ApiOperation("根据ID获取报送任务详情")
    public String getTaskById(@PathVariable String taskId,
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

            TblSubmissionTask task = taskService.getTaskById(taskId);
            return new JsonBean(1, "成功", task).toJson();
        } catch (Exception e) {
            log.error("获取报送任务详情失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("")
    @ResponseBody
    @ApiOperation("新增报送任务")
    public String addTask(@FlexibleRequestBody TblSubmissionTask task,
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

            task.setTaskId(null);
            TblSubmissionTask saved = taskService.saveTask(task);
            return new JsonBean(1, "新增成功", saved).toJson();
        } catch (Exception e) {
            log.error("新增报送任务失败", e);
            return new JsonBean(0, "新增失败: " + e.getMessage(), null).toJson();
        }
    }

    @PutMapping("")
    @ResponseBody
    @ApiOperation("修改报送任务")
    public String updateTask(@FlexibleRequestBody TblSubmissionTask task,
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

            if (task.getTaskId() == null || task.getTaskId().isEmpty()) {
                return new JsonBean(0, "任务ID不能为空", null).toJson();
            }

            TblSubmissionTask saved = taskService.saveTask(task);
            return new JsonBean(1, "修改成功", saved).toJson();
        } catch (Exception e) {
            log.error("修改报送任务失败", e);
            return new JsonBean(0, "修改失败: " + e.getMessage(), null).toJson();
        }
    }

    @DeleteMapping("/{taskIds}")
    @ResponseBody
    @ApiOperation("删除报送任务")
    public String deleteTask(@PathVariable String taskIds,
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

            String[] ids = taskIds.split(",");
            if (ids.length == 1) {
                taskService.deleteTask(ids[0]);
            } else {
                taskService.batchDeleteTasks(Arrays.asList(ids));
            }
            return new JsonBean(1, "删除成功", null).toJson();
        } catch (Exception e) {
            log.error("删除报送任务失败", e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/pending")
    @ResponseBody
    @ApiOperation("查询待执行的任务")
    public String getPendingTasks(@RequestHeader(value = "token", required = false) String token,
                                  HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            List<TblSubmissionTask> tasks = taskService.getPendingTasks();
            return new JsonBean(1, "成功", tasks).toJson();
        } catch (Exception e) {
            log.error("查询待执行的任务失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/overdue")
    @ResponseBody
    @ApiOperation("查询逾期任务")
    public String getOverdueTasks(@RequestHeader(value = "token", required = false) String token,
                                  HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            List<TblSubmissionTask> tasks = taskService.getOverdueTasks();
            return new JsonBean(1, "成功", tasks).toJson();
        } catch (Exception e) {
            log.error("查询逾期任务失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/due-soon/{days}")
    @ResponseBody
    @ApiOperation("查询即将到期的任务")
    public String getDueSoonTasks(@PathVariable Integer days,
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

            List<TblSubmissionTask> tasks = taskService.getDueSoonTasks(days);
            return new JsonBean(1, "成功", tasks).toJson();
        } catch (Exception e) {
            log.error("查询即将到期的任务失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/{taskId}/execute")
    @ResponseBody
    @ApiOperation("执行任务")
    public String executeTask(@PathVariable String taskId,
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

            taskService.executeTask(taskId);
            return new JsonBean(1, "执行成功", null).toJson();
        } catch (Exception e) {
            log.error("执行任务失败", e);
            return new JsonBean(0, "执行失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/{taskId}/cancel")
    @ResponseBody
    @ApiOperation("取消任务")
    public String cancelTask(@PathVariable String taskId,
                             @RequestParam(required = false) String cancelReason,
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

            taskService.cancelTask(taskId, cancelReason);
            return new JsonBean(1, "取消成功", null).toJson();
        } catch (Exception e) {
            log.error("取消任务失败", e);
            return new JsonBean(0, "取消失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/{taskId}/retry")
    @ResponseBody
    @ApiOperation("重试任务")
    public String retryTask(@PathVariable String taskId,
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

            taskService.retryTask(taskId);
            return new JsonBean(1, "重试成功", null).toJson();
        } catch (Exception e) {
            log.error("重试任务失败", e);
            return new JsonBean(0, "重试失败: " + e.getMessage(), null).toJson();
        }
    }
}
