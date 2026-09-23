package com.global.treasurer.controller;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.DataAnalysisTask;
import com.global.treasurer.service.IDataAnalysisTaskService;
import com.hbfk.util.JsonBean;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * 数据分析任务控制器
 *
 * @author 华博云开发团队
 * @since 2025-01-12
 */
@RestController
@RequestMapping({"/financial/decision-support/tasks", "/xjgl/decision-support/tasks", "/centralaudit/decision-support/tasks", "/decision-support/tasks",
                  "/financial/decision/analysis/task", "/xjgl/decision/analysis/task", "/centralaudit/decision/analysis/task",
                  "/decision/analysis/task", "/dataAnalysisTask"})
@Api(tags = "数据分析任务管理")
public class DataAnalysisTaskController {
    private static final Logger log = LoggerFactory.getLogger(DataAnalysisTaskController.class);

    @Resource
    private IDataAnalysisTaskService dataAnalysisTaskService;

    @Resource
    private UserProvider userProvider;

    @GetMapping({"/list", "/page"})
    @ApiOperation("获取数据分析任务列表(GET)")
    public String getTaskListGet(
            @ApiParam(value = "页码", example = "1") @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNum,
            @ApiParam(value = "每页数量", example = "20") @RequestParam(defaultValue = "20") Integer pageSize,
            @ApiParam(value = "任务编号") @RequestParam(required = false) String taskNo,
            @ApiParam(value = "任务名称") @RequestParam(required = false) String taskName,
            @ApiParam(value = "任务类型") @RequestParam(required = false) String taskType,
            @ApiParam(value = "任务状态") @RequestParam(required = false) String taskStatus,
            HttpServletResponse response) {

        try {
            log.info("========== 数据分析任务查询接口(GET) ==========");
            log.info("接收参数 - pageNo: {}, pageSize: {}", pageNum, pageSize);
            log.info("接收参数 - taskNo: {}, taskName: {}, taskType: {}, taskStatus: {}",
                    taskNo, taskName, taskType, taskStatus);

            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 构建查询参数
            Map<String, Object> params = new HashMap<>();
            if (taskNo != null && !taskNo.trim().isEmpty()) {
                params.put("taskNo", taskNo);
            }
            if (taskName != null && !taskName.trim().isEmpty()) {
                params.put("taskName", taskName);
            }
            if (taskType != null && !taskType.trim().isEmpty()) {
                params.put("taskType", taskType);
            }
            if (taskStatus != null && !taskStatus.trim().isEmpty()) {
                params.put("taskStatus", taskStatus);
            }

            log.info("构建的params: {}", params);

            // 分页查询
            com.baomidou.mybatisplus.extension.plugins.pagination.Page<DataAnalysisTask> page =
                    new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageNum, pageSize);
            com.baomidou.mybatisplus.core.metadata.IPage<DataAnalysisTask> result =
                    dataAnalysisTaskService.selectPage(page, params);

            // 构建返回数据
            Map<String, Object> data = new HashMap<>();
            data.put("tlist", result.getRecords());
            data.put("totalRecord", result.getTotal());
            data.put("pageNo", result.getCurrent());
            data.put("pageSize", result.getSize());

            log.info("查询结果 - 当前页: {}, 每页大小: {}, 总记录数: {}, 当前页记录数: {}",
                    result.getCurrent(), result.getSize(), result.getTotal(), result.getRecords().size());

            return new JsonBean(1, "查询成功", data).toString();

        } catch (Exception e) {
            log.error("获取数据分析任务列表失败", e);
            return JsonBean.error("获取数据分析任务列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/{taskId}")
    @ApiOperation("获取数据分析任务详情")
    public String getTaskDetail(
            @ApiParam(value = "任务ID", required = true) @PathVariable Long taskId,
            HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            DataAnalysisTask task = dataAnalysisTaskService.getById(taskId);
            if (task == null) {
                return JsonBean.error("数据分析任务不存在");
            }

            return JsonBean.success(task);

        } catch (Exception e) {
            log.error("获取数据分析任务详情失败，taskId: {}", taskId, e);
            return JsonBean.error("获取详情失败: " + e.getMessage());
        }
    }

    @PostMapping
    @ApiOperation("创建数据分析任务")
    public String createTask(@FlexibleRequestBody DataAnalysisTask task, HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 设置创建信息
            TblStaffUtil user = userProvider.get();
            if (user != null) {
                task.setCreateBy(getStaffId(user));
                task.setUpdateBy(getStaffId(user));
                if (user.getCurrentOrg() != null) {
                    task.setOrgId(getOrgId(user.getCurrentOrg()));
                }
            }

            task.setCreateTime(java.time.LocalDateTime.now());
            task.setUpdateTime(java.time.LocalDateTime.now());
            task.setDelFlag("0");

            boolean success = dataAnalysisTaskService.save(task);
            if (success) {
                return JsonBean.success("创建成功");
            } else {
                return JsonBean.error("创建失败");
            }

        } catch (Exception e) {
            log.error("创建数据分析任务失败", e);
            return JsonBean.error("创建失败: " + e.getMessage());
        }
    }

    /**
     * 验证用户权限
     */
    private boolean validateUser() {
        try {
            TblStaffUtil user = userProvider.get();
            return user != null && user.getLinkDetp() != null && user.getCurrentOrg() != null;
        } catch (Exception e) {
            log.error("验证用户权限失败", e);
            return false;
        }
    }

    /**
     * 获取员工ID
     */
    private Long getStaffId(TblStaffUtil user) {
        return user.getStaffid() != null ? user.getStaffid().longValue() : null;
    }

    /**
     * 获取组织ID
     */
    private Long getOrgId(Object org) {
        // 根据实际情况获取组织ID
        return 1L;
    }

    @PutMapping
    @ApiOperation("更新数据分析任务(RESTful)")
    public String updateTaskRestful(@FlexibleRequestBody DataAnalysisTask task, HttpServletResponse response) {
        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            if (task.getTaskId() == null) {
                return JsonBean.error("任务ID不能为空");
            }

            // 检查任务是否存在
            DataAnalysisTask existingTask = dataAnalysisTaskService.getById(task.getTaskId());
            if (existingTask == null) {
                return JsonBean.error("数据分析任务不存在");
            }

            // 设置更新信息
            TblStaffUtil user = userProvider.get();
            if (user != null) {
                task.setUpdateBy(getStaffId(user));
            }
            task.setUpdateTime(java.time.LocalDateTime.now());

            boolean success = dataAnalysisTaskService.updateById(task);
            if (success) {
                return JsonBean.success("更新成功");
            } else {
                return JsonBean.error("更新失败");
            }

        } catch (Exception e) {
            log.error("更新数据分析任务失败", e);
            return JsonBean.error("更新失败: " + e.getMessage());
        }
    }

    @PostMapping("/update")
    @ApiOperation("更新数据分析任务")
    public String updateTask(@FlexibleRequestBody DataAnalysisTask task, HttpServletResponse response) {
        return updateTaskRestful(task, response);
    }

    @DeleteMapping("/{taskId}")
    @ApiOperation("删除数据分析任务")
    public String deleteTask(
            @ApiParam(value = "任务ID", required = true) @PathVariable Long taskId,
            HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 检查任务是否存在
            DataAnalysisTask task = dataAnalysisTaskService.getById(taskId);
            if (task == null) {
                return JsonBean.error("数据分析任务不存在");
            }

            // 检查任务状态
            if ("RUNNING".equals(task.getTaskStatus())) {
                return JsonBean.error("运行中的任务不能删除");
            }

            boolean success = dataAnalysisTaskService.removeById(taskId);
            if (success) {
                return JsonBean.success("删除成功");
            } else {
                return JsonBean.error("删除失败");
            }

        } catch (Exception e) {
            log.error("删除数据分析任务失败，taskId: {}", taskId, e);
            return JsonBean.error("删除失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch-delete")
    @ApiOperation("批量删除数据分析任务")
    public String batchDeleteTasks(
            @ApiParam(value = "任务ID列表", required = true) @RequestParam(value = "taskIds", required = false) List<Long> taskIds,
            HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            if (taskIds == null || taskIds.isEmpty()) {
                return JsonBean.error("任务ID列表不能为空");
            }

            // 检查是否有运行中的任务
            for (Long taskId : taskIds) {
                DataAnalysisTask task = dataAnalysisTaskService.getById(taskId);
                if (task != null && "RUNNING".equals(task.getTaskStatus())) {
                    return JsonBean.error("存在运行中的任务,不能删除");
                }
            }

            boolean success = dataAnalysisTaskService.removeByIds(taskIds);
            if (success) {
                return JsonBean.success("批量删除成功");
            } else {
                return JsonBean.error("批量删除失败");
            }

        } catch (Exception e) {
            log.error("批量删除数据分析任务失败", e);
            return JsonBean.error("批量删除失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/batch")
    @ApiOperation("批量删除数据分析任务(RESTful)")
    public String batchDeleteTasksRestful(
            @ApiParam(value = "任务ID列表", required = true) @RequestParam(value = "taskIds", required = false) List<Long> taskIds,
            HttpServletResponse response) {
        return batchDeleteTasks(taskIds, response);
    }

    @PostMapping("/{taskId}/execute")
    @ApiOperation("执行数据分析任务")
    public String executeTask(
            @ApiParam(value = "任务ID", required = true) @PathVariable Long taskId,
            @ApiParam(value = "执行用户ID") @RequestParam(required = false) Long executeUser,
            HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 如果没有传入executeUser,则使用当前登录用户
            if (executeUser == null) {
                TblStaffUtil user = userProvider.get();
                executeUser = user != null ? getStaffId(user) : null;
            }

            boolean success = dataAnalysisTaskService.executeTask(taskId, executeUser);
            if (success) {
                return JsonBean.success("任务执行成功");
            } else {
                return JsonBean.error("任务执行失败");
            }

        } catch (Exception e) {
            log.error("执行数据分析任务失败，taskId: {}", taskId, e);
            return JsonBean.error("执行失败: " + e.getMessage());
        }
    }

    @PostMapping("/{taskId}/cancel")
    @ApiOperation("取消数据分析任务")
    public String cancelTask(
            @ApiParam(value = "任务ID", required = true) @PathVariable Long taskId,
            @ApiParam(value = "更新用户ID") @RequestParam(required = false) Long updateUser,
            HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 如果没有传入updateUser,则使用当前登录用户
            if (updateUser == null) {
                TblStaffUtil user = userProvider.get();
                updateUser = user != null ? getStaffId(user) : null;
            }

            boolean success = dataAnalysisTaskService.cancelTask(taskId, updateUser);
            if (success) {
                return JsonBean.success("任务取消成功");
            } else {
                return JsonBean.error("任务取消失败");
            }

        } catch (Exception e) {
            log.error("取消数据分析任务失败，taskId: {}", taskId, e);
            return JsonBean.error("取消失败: " + e.getMessage());
        }
    }

    @PostMapping("/{taskId}/retry")
    @ApiOperation("重试数据分析任务")
    public String retryTask(
            @ApiParam(value = "任务ID", required = true) @PathVariable Long taskId,
            @ApiParam(value = "执行用户ID") @RequestParam(required = false) Long executeUser,
            HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 如果没有传入executeUser,则使用当前登录用户
            if (executeUser == null) {
                TblStaffUtil user = userProvider.get();
                executeUser = user != null ? getStaffId(user) : null;
            }

            boolean success = dataAnalysisTaskService.retryTask(taskId, executeUser);
            if (success) {
                return JsonBean.success("任务重试成功");
            } else {
                return JsonBean.error("任务重试失败");
            }

        } catch (Exception e) {
            log.error("重试数据分析任务失败，taskId: {}", taskId, e);
            return JsonBean.error("重试失败: " + e.getMessage());
        }
    }

    @GetMapping("/pending")
    @ApiOperation("查询待处理任务")
    public String getPendingTasks(HttpServletResponse response) {
        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            Map<String, Object> params = new HashMap<>();
            params.put("taskStatus", "PENDING");

            com.baomidou.mybatisplus.core.metadata.IPage<DataAnalysisTask> iPage = dataAnalysisTaskService.selectPage(
                    new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(1, 100),
                    params
            );

            return JsonBean.success(iPage.getRecords());

        } catch (Exception e) {
            log.error("查询待处理任务失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/running")
    @ApiOperation("查询运行中任务")
    public String getRunningTasks(HttpServletResponse response) {
        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            Map<String, Object> params = new HashMap<>();
            params.put("taskStatus", "RUNNING");

            com.baomidou.mybatisplus.core.metadata.IPage<DataAnalysisTask> iPage = dataAnalysisTaskService.selectPage(
                    new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(1, 100),
                    params
            );

            return JsonBean.success(iPage.getRecords());

        } catch (Exception e) {
            log.error("查询运行中任务失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/failed")
    @ApiOperation("查询失败任务")
    public String getFailedTasks(HttpServletResponse response) {
        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            Map<String, Object> params = new HashMap<>();
            params.put("taskStatus", "FAILED");

            com.baomidou.mybatisplus.core.metadata.IPage<DataAnalysisTask> iPage = dataAnalysisTaskService.selectPage(
                    new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(1, 100),
                    params
            );

            return JsonBean.success(iPage.getRecords());

        } catch (Exception e) {
            log.error("查询失败任务失败", e);
            return JsonBean.error("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/statistics")
    @ApiOperation("任务统计")
    public String getTaskStatistics(HttpServletResponse response) {
        try {
            // 权限验证
            if (!validateUser()) {
                return JsonBean.error("用户已失效");
            }

            // 构建统计数据
            Map<String, Object> statistics = new HashMap<>();

            // 查询各状态任务数量
            Map<String, Object> pendingParams = new HashMap<>();
            pendingParams.put("taskStatus", "PENDING");
            long pendingCount = dataAnalysisTaskService.selectPage(
                    new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(1, 1),
                    pendingParams
            ).getTotal();

            Map<String, Object> runningParams = new HashMap<>();
            runningParams.put("taskStatus", "RUNNING");
            long runningCount = dataAnalysisTaskService.selectPage(
                    new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(1, 1),
                    runningParams
            ).getTotal();

            Map<String, Object> completedParams = new HashMap<>();
            completedParams.put("taskStatus", "COMPLETED");
            long completedCount = dataAnalysisTaskService.selectPage(
                    new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(1, 1),
                    completedParams
            ).getTotal();

            Map<String, Object> failedParams = new HashMap<>();
            failedParams.put("taskStatus", "FAILED");
            long failedCount = dataAnalysisTaskService.selectPage(
                    new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(1, 1),
                    failedParams
            ).getTotal();

            statistics.put("pendingCount", pendingCount);
            statistics.put("runningCount", runningCount);
            statistics.put("completedCount", completedCount);
            statistics.put("failedCount", failedCount);
            statistics.put("totalCount", pendingCount + runningCount + completedCount + failedCount);

            return JsonBean.success(statistics);

        } catch (Exception e) {
            log.error("获取任务统计失败", e);
            return JsonBean.error("获取统计失败: " + e.getMessage());
        }
    }

    @GetMapping("/export")
    @ApiOperation("导出数据分析任务")
    public void exportTasks(
            @ApiParam(value = "任务编号") @RequestParam(required = false) String taskNo,
            @ApiParam(value = "任务名称") @RequestParam(required = false) String taskName,
            @ApiParam(value = "任务类型") @RequestParam(required = false) String taskType,
            @ApiParam(value = "任务状态") @RequestParam(required = false) String taskStatus,
            HttpServletResponse response) {

        try {
            // 权限验证
            if (!validateUser()) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }

            // 构建查询参数
            Map<String, Object> params = new HashMap<>();
            params.put("taskNo", taskNo);
            params.put("taskName", taskName);
            params.put("taskType", taskType);
            params.put("taskStatus", taskStatus);

            // 查询所有数据(不分页)
            com.baomidou.mybatisplus.core.metadata.IPage<DataAnalysisTask> iPage = dataAnalysisTaskService.selectPage(
                    new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(1, 10000),
                    params
            );

            java.util.List<DataAnalysisTask> taskList = iPage.getRecords();

            // 设置响应头
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setCharacterEncoding("utf-8");
            String fileName = java.net.URLEncoder.encode("数据分析任务_" + System.currentTimeMillis(), "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");

            // 创建Excel工作簿
            org.apache.poi.ss.usermodel.Workbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("数据分析任务");

            // 创建标题行
            org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0);
            String[] headers = {"任务编号", "任务名称", "任务类型", "任务状态", "数据源", "分析类型",
                               "开始时间", "结束时间", "执行时长(秒)", "创建时间"};
            for (int i = 0; i < headers.length; i++) {
                org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
            }

            // 填充数据
            int rowNum = 1;
            for (DataAnalysisTask task : taskList) {
                org.apache.poi.ss.usermodel.Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(task.getTaskNo() != null ? task.getTaskNo() : "");
                row.createCell(1).setCellValue(task.getTaskName() != null ? task.getTaskName() : "");
                row.createCell(2).setCellValue(task.getTaskType() != null ? task.getTaskType() : "");
                row.createCell(3).setCellValue(task.getTaskStatus() != null ? task.getTaskStatus() : "");
                row.createCell(4).setCellValue(task.getDataSource() != null ? task.getDataSource() : "");
                row.createCell(5).setCellValue(task.getAnalysisType() != null ? task.getAnalysisType() : "");
                row.createCell(6).setCellValue(task.getStartTime() != null ? task.getStartTime().toString() : "");
                row.createCell(7).setCellValue(task.getEndTime() != null ? task.getEndTime().toString() : "");
                row.createCell(8).setCellValue(task.getExecutionTime() != null ? task.getExecutionTime().toString() : "");
                row.createCell(9).setCellValue(task.getCreateTime() != null ? task.getCreateTime().toString() : "");
            }

            // 自动调整列宽
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            // 写入响应流
            workbook.write(response.getOutputStream());
            workbook.close();

            log.info("导出数据分析任务成功，共{}条记录", taskList.size());

        } catch (Exception e) {
            log.error("导出数据分析任务失败", e);
            try {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("导出失败: " + e.getMessage());
            } catch (java.io.IOException ioException) {
                log.error("写入错误响应失败", ioException);
            }
        }
    }
}

