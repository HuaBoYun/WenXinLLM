package com.huabo.cybermonitor.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.cybermonitor.entity.DataSubmitTask;
import com.huabo.cybermonitor.service.IDataSubmitTaskService;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.util.R;
import com.huabo.cybermonitor.vo.DataSubmitTaskQueryVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;



/**
 * 数据报送任务管理 Controller
 *
 * @author system
 * @since 2024-01-01
 */
@Tag(name="数据报送任务管理",description="数据报送任务管理")
@RestController
@RequestMapping("/v1/data/submit/task")
public class DataSubmitTaskController {

	private static final Logger log = LoggerFactory.getLogger(DataSubmitTaskController.class);

    @Autowired
    private IDataSubmitTaskService taskService;

    @Operation(summary = "分页查询数据报送任务列表")
    @PostMapping("/list")
    public R<PageResult<DataSubmitTask>> getTaskList(@RequestBody DataSubmitTaskQueryVO queryVO) {
        try {
            IPage<DataSubmitTask> page = taskService.getTaskList(queryVO);
            PageResult<DataSubmitTask> pageResult = new PageResult<>();
            pageResult.setTlist(page.getRecords());
            pageResult.setTotalRecord((int) page.getTotal());
            pageResult.setPageNumber((int) page.getCurrent());
            pageResult.setPageSize((int) page.getSize());
            
            return R.success(pageResult);
        } catch (Exception e) {
            log.error("查询数据报送任务列表失败", e);
            return R.fail("查询数据报送任务列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取数据报送任务详情")
    @PostMapping("/detail")
    public R<DataSubmitTask> getTaskDetail(@RequestBody Map<String, String> params) {
        try {
            String taskId = params.get("taskId");
            if (taskId == null || taskId.trim().isEmpty()) {
                return R.fail("任务ID不能为空");
            }
            
            DataSubmitTask task = taskService.getTaskDetail(taskId);
            return R.success(task);
        } catch (Exception e) {
            log.error("获取数据报送任务详情失败", e);
            return R.fail("获取数据报送任务详情失败：" + e.getMessage());
        }
    }

    @Operation(summary = "新增数据报送任务")
    @PostMapping("/add")
    public R<String> addTask(@RequestBody DataSubmitTask task) {
        try {
            boolean success = taskService.addTask(task);
            if (success) {
                return R.success("新增数据报送任务成功");
            } else {
                return R.fail("新增数据报送任务失败");
            }
        } catch (Exception e) {
            log.error("新增数据报送任务失败", e);
            return R.fail("新增数据报送任务失败：" + e.getMessage());
        }
    }

    @Operation(summary = "更新数据报送任务")
    @PostMapping("/update")
    public R<String> updateTask(@RequestBody DataSubmitTask task) {
        try {
            if (task.getTaskId() == null || task.getTaskId().trim().isEmpty()) {
                return R.fail("任务ID不能为空");
            }
            
            boolean success = taskService.updateTask(task);
            if (success) {
                return R.success("更新数据报送任务成功");
            } else {
                return R.fail("更新数据报送任务失败");
            }
        } catch (Exception e) {
            log.error("更新数据报送任务失败", e);
            return R.fail("更新数据报送任务失败：" + e.getMessage());
        }
    }

    @Operation(summary = "删除数据报送任务")
    @PostMapping("/delete")
    public R<String> deleteTask(@RequestBody Map<String, String> params) {
        try {
            String taskId = params.get("taskId");
            if (taskId == null || taskId.trim().isEmpty()) {
                return R.fail("任务ID不能为空");
            }
            
            boolean success = taskService.deleteTask(taskId);
            if (success) {
                return R.success("删除数据报送任务成功");
            } else {
                return R.fail("删除数据报送任务失败");
            }
        } catch (Exception e) {
            log.error("删除数据报送任务失败", e);
            return R.fail("删除数据报送任务失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量删除数据报送任务")
    @PostMapping("/batch-delete")
    public R<String> batchDeleteTask(@RequestBody Map<String, List<String>> params) {
        try {
            List<String> taskIds = params.get("taskIds");
            if (taskIds == null || taskIds.isEmpty()) {
                return R.fail("任务ID列表不能为空");
            }
            
            boolean success = taskService.batchDeleteTask(taskIds);
            if (success) {
                return R.success("批量删除数据报送任务成功");
            } else {
                return R.fail("批量删除数据报送任务失败");
            }
        } catch (Exception e) {
            log.error("批量删除数据报送任务失败", e);
            return R.fail("批量删除数据报送任务失败：" + e.getMessage());
        }
    }

    @Operation(summary = "发布任务")
    @PostMapping("/publish")
    public R<String> publishTask(@RequestBody Map<String, String> params) {
        try {
            String taskId = params.get("taskId");
            if (taskId == null || taskId.trim().isEmpty()) {
                return R.fail("任务ID不能为空");
            }
            
            boolean success = taskService.publishTask(taskId);
            if (success) {
                return R.success("发布任务成功");
            } else {
                return R.fail("发布任务失败");
            }
        } catch (Exception e) {
            log.error("发布任务失败", e);
            return R.fail("发布任务失败：" + e.getMessage());
        }
    }

    @Operation(summary = "取消任务")
    @PostMapping("/cancel")
    public R<String> cancelTask(@RequestBody Map<String, String> params) {
        try {
            String taskId = params.get("taskId");
            if (taskId == null || taskId.trim().isEmpty()) {
                return R.fail("任务ID不能为空");
            }
            
            boolean success = taskService.cancelTask(taskId);
            if (success) {
                return R.success("取消任务成功");
            } else {
                return R.fail("取消任务失败");
            }
        } catch (Exception e) {
            log.error("取消任务失败", e);
            return R.fail("取消任务失败：" + e.getMessage());
        }
    }

    @Operation(summary = "完成任务")
    @PostMapping("/complete")
    public R<String> completeTask(@RequestBody Map<String, String> params) {
        try {
            String taskId = params.get("taskId");
            if (taskId == null || taskId.trim().isEmpty()) {
                return R.fail("任务ID不能为空");
            }
            
            boolean success = taskService.completeTask(taskId);
            if (success) {
                return R.success("完成任务成功");
            } else {
                return R.fail("完成任务失败");
            }
        } catch (Exception e) {
            log.error("完成任务失败", e);
            return R.fail("完成任务失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据任务状态查询任务列表")
    @PostMapping("/list-by-status")
    public R<List<DataSubmitTask>> getTasksByStatus(@RequestBody Map<String, String> params) {
        try {
            String taskStatus = params.get("taskStatus");
            if (taskStatus == null || taskStatus.trim().isEmpty()) {
                return R.fail("任务状态不能为空");
            }
            
            List<DataSubmitTask> tasks = taskService.getTasksByStatus(taskStatus);
            return R.success(tasks);
        } catch (Exception e) {
            log.error("根据任务状态查询任务列表失败", e);
            return R.fail("根据任务状态查询任务列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据任务类型查询任务列表")
    @PostMapping("/list-by-type")
    public R<List<DataSubmitTask>> getTasksByType(@RequestBody Map<String, String> params) {
        try {
            String taskType = params.get("taskType");
            if (taskType == null || taskType.trim().isEmpty()) {
                return R.fail("任务类型不能为空");
            }
            
            List<DataSubmitTask> tasks = taskService.getTasksByType(taskType);
            return R.success(tasks);
        } catch (Exception e) {
            log.error("根据任务类型查询任务列表失败", e);
            return R.fail("根据任务类型查询任务列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据报送周期查询任务列表")
    @PostMapping("/list-by-cycle")
    public R<List<DataSubmitTask>> getTasksByCycle(@RequestBody Map<String, String> params) {
        try {
            String submitCycle = params.get("submitCycle");
            if (submitCycle == null || submitCycle.trim().isEmpty()) {
                return R.fail("报送周期不能为空");
            }
            
            List<DataSubmitTask> tasks = taskService.getTasksByCycle(submitCycle);
            return R.success(tasks);
        } catch (Exception e) {
            log.error("根据报送周期查询任务列表失败", e);
            return R.fail("根据报送周期查询任务列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "查询活跃的任务列表")
    @PostMapping("/active-tasks")
    public R<List<DataSubmitTask>> getActiveTasks() {
        try {
            List<DataSubmitTask> tasks = taskService.getActiveTasks();
            return R.success(tasks);
        } catch (Exception e) {
            log.error("查询活跃的任务列表失败", e);
            return R.fail("查询活跃的任务列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "查询即将到期的任务列表")
    @PostMapping("/expiring-tasks")
    public R<List<DataSubmitTask>> getExpiringTasks(@RequestBody Map<String, Integer> params) {
        try {
            Integer days = params.get("days");
            if (days == null || days <= 0) {
                days = 7; // 默认7天
            }
            
            List<DataSubmitTask> tasks = taskService.getExpiringTasks(days);
            return R.success(tasks);
        } catch (Exception e) {
            log.error("查询即将到期的任务列表失败", e);
            return R.fail("查询即将到期的任务列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取任务统计信息")
    @PostMapping("/statistics/overview")
    public R<Map<String, Object>> getTaskStatistics() {
        try {
            Map<String, Object> statistics = taskService.getTaskStatistics();
            return R.success(statistics);
        } catch (Exception e) {
            log.error("获取任务统计信息失败", e);
            return R.fail("获取任务统计信息失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取任务类型分布统计")
    @PostMapping("/statistics/type-distribution")
    public R<List<Map<String, Object>>> getTaskTypeDistribution() {
        try {
            List<Map<String, Object>> distribution = taskService.getTaskTypeDistribution();
            return R.success(distribution);
        } catch (Exception e) {
            log.error("获取任务类型分布统计失败", e);
            return R.fail("获取任务类型分布统计失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取任务分类分布统计")
    @PostMapping("/statistics/category-distribution")
    public R<List<Map<String, Object>>> getTaskCategoryDistribution() {
        try {
            List<Map<String, Object>> distribution = taskService.getTaskCategoryDistribution();
            return R.success(distribution);
        } catch (Exception e) {
            log.error("获取任务分类分布统计失败", e);
            return R.fail("获取任务分类分布统计失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取任务周期分布统计")
    @PostMapping("/statistics/cycle-distribution")
    public R<List<Map<String, Object>>> getTaskCycleDistribution() {
        try {
            List<Map<String, Object>> distribution = taskService.getTaskCycleDistribution();
            return R.success(distribution);
        } catch (Exception e) {
            log.error("获取任务周期分布统计失败", e);
            return R.fail("获取任务周期分布统计失败：" + e.getMessage());
        }
    }

    @Operation(summary = "验证任务名称")
    @PostMapping("/validate-name")
    public R<Map<String, Object>> validateTaskName(@RequestBody Map<String, String> params) {
        try {
            String taskName = params.get("taskName");
            String excludeId = params.get("excludeId");
            
            if (taskName == null || taskName.trim().isEmpty()) {
                return R.fail("任务名称不能为空");
            }
            
            boolean isDuplicate = taskService.validateTaskName(taskName, excludeId);

            Map<String, Object> result = new HashMap<>();
            result.put("isDuplicate", isDuplicate);
            result.put("message", isDuplicate ? "任务名称已存在" : "任务名称可用");

            return R.success(result);
        } catch (Exception e) {
            log.error("验证任务名称失败", e);
            return R.fail("验证任务名称失败：" + e.getMessage());
        }
    }

    @Operation(summary = "复制任务")
    @PostMapping("/copy")
    public R<Map<String, Object>> copyTask(@RequestBody Map<String, String> params) {
        try {
            String taskId = params.get("taskId");
            if (taskId == null || taskId.trim().isEmpty()) {
                return R.fail("任务ID不能为空");
            }
            
            String newTaskId = taskService.copyTask(taskId);

            Map<String, Object> result = new HashMap<>();
            result.put("newTaskId", newTaskId);
            result.put("message", "复制任务成功");

            return R.success(result);
        } catch (Exception e) {
            log.error("复制任务失败", e);
            return R.fail("复制任务失败：" + e.getMessage());
        }
    }

    @Operation(summary = "导出任务列表")
    @PostMapping("/export")
    public void exportTaskList(@RequestBody DataSubmitTaskQueryVO queryVO, HttpServletResponse response) {
        try {
            taskService.exportTaskList(queryVO, response);
        } catch (Exception e) {
            log.error("导出任务列表失败", e);
            throw new RuntimeException("导出任务列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "下载任务导入模板")
    @GetMapping("/template")
    public void downloadTaskTemplate(HttpServletResponse response) {
        try {
            taskService.downloadTaskTemplate(response);
        } catch (Exception e) {
            log.error("下载任务导入模板失败", e);
            throw new RuntimeException("下载任务导入模板失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量导入任务")
    @PostMapping("/import")
    public R<Map<String, Object>> importTaskList(
            @Parameter(description = "导入文件", required = true) @RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return R.fail("导入文件不能为空");
            }
            
            Map<String, Object> result = taskService.importTaskList(file);
            return R.success(result);
        } catch (Exception e) {
            log.error("批量导入任务失败", e);
            return R.fail("批量导入任务失败：" + e.getMessage());
        }
    }
}
