package com.huabo.cybermonitor.controller;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.cybermonitor.entity.DataSubmissionTask;
import com.huabo.cybermonitor.service.IDataSubmissionTaskService;
import com.huabo.cybermonitor.util.R;
import com.huabo.cybermonitor.vo.DataSubmissionTaskQueryVo;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;



/**
 * 数据报送任务Controller
 *
 * @author huabo
 * @since 2024-12-12
 */
@RestController
@RequestMapping("/v1/enterprise/data-submission")
@Tag(name="数据报送任务管理",description="数据报送任务管理")
public class DataSubmissionTaskController {

	private static final Logger log = LoggerFactory.getLogger(DataSubmissionTaskController.class);

    @Autowired
    private IDataSubmissionTaskService dataSubmissionTaskService;

    /**
     * 分页查询数据报送任务列表
     */
    @Operation(summary = "list")
    @PostMapping("/list")
    public R<IPage<DataSubmissionTask>> list(@RequestBody DataSubmissionTaskQueryVo queryVo) {
        try {
            Page<DataSubmissionTask> page = new Page<>(queryVo.getPageNum(), queryVo.getPageSize());
            IPage<DataSubmissionTask> result = dataSubmissionTaskService.selectDataSubmissionTaskPage(page, queryVo);
            return R.success(result);
        } catch (Exception e) {
            log.error("分页查询数据报送任务列表失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    /**
     * 查询数据报送任务列表
     */
    @Operation(summary = "listAll")
    @PostMapping("/listAll")
    public R<List<DataSubmissionTask>> listAll(@RequestBody DataSubmissionTaskQueryVo queryVo) {
        try {
            List<DataSubmissionTask> result = dataSubmissionTaskService.selectDataSubmissionTaskList(queryVo);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询数据报送任务列表失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据ID查询数据报送任务详情
     */
    @Operation(summary = "根据ID查询")
    @GetMapping("/{taskId}")
    public R<DataSubmissionTask> getById(@Parameter(description="任务ID") @PathVariable String taskId) {
        try {
            DataSubmissionTask result = dataSubmissionTaskService.getById(taskId);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询数据报送任务详情失败，任务ID：{}", taskId, e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    /**
     * 新增数据报送任务
     */
    @Operation(summary = "新增")
    @PostMapping("/add")
    public R<Boolean> add(@RequestBody DataSubmissionTask dataSubmissionTask) {
        try {
            boolean result = dataSubmissionTaskService.addDataSubmissionTask(dataSubmissionTask);
            return result ? R.success(true, "新增成功") : R.fail("新增失败");
        } catch (Exception e) {
            log.error("新增数据报送任务失败", e);
            return R.fail("新增失败：" + e.getMessage());
        }
    }

    /**
     * 修改数据报送任务
     */
    @Operation(summary = "更新")
    @PutMapping("/update")
    public R<Boolean> update(@RequestBody DataSubmissionTask dataSubmissionTask) {
        try {
            boolean result = dataSubmissionTaskService.updateDataSubmissionTask(dataSubmissionTask);
            return result ? R.success(true, "修改成功") : R.fail("修改失败");
        } catch (Exception e) {
            log.error("修改数据报送任务失败", e);
            return R.fail("修改失败：" + e.getMessage());
        }
    }

    /**
     * 删除数据报送任务
     */
    @Operation(summary = "删除")
    @DeleteMapping("/{taskId}")
    public R<Boolean> delete(@Parameter(description="任务ID") @PathVariable String taskId) {
        try {
            boolean result = dataSubmissionTaskService.deleteDataSubmissionTask(taskId);
            return result ? R.success(true, "删除成功") : R.fail("删除失败");
        } catch (Exception e) {
            log.error("删除数据报送任务失败，任务ID：{}", taskId, e);
            return R.fail("删除失败：" + e.getMessage());
        }
    }

    /**
     * 批量删除数据报送任务
     */
    @Operation(summary = "批量操作")
    @DeleteMapping("/batchDelete")
    public R<Boolean> batchDelete(@RequestBody List<String> taskIds) {
        try {
            boolean result = dataSubmissionTaskService.batchDeleteDataSubmissionTasks(taskIds);
            return result ? R.success(true, "批量删除成功") : R.fail("批量删除失败");
        } catch (Exception e) {
            log.error("批量删除数据报送任务失败", e);
            return R.fail("批量删除失败：" + e.getMessage());
        }
    }

    /**
     * 根据企业ID查询报送任务统计
     */
    @Operation(summary = "")
    @GetMapping("/statistics/{enterpriseId}")
    public R<Map<String, Object>> getStatistics(@Parameter(description="企业ID") @PathVariable String enterpriseId) {
        try {
            Map<String, Object> result = dataSubmissionTaskService.getTaskStatisticsByEnterpriseId(enterpriseId);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询报送任务统计失败，企业ID：{}", enterpriseId, e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    /**
     * 查询即将到期的报送任务
     */
    @Operation(summary = "查询数据")
    @GetMapping("/upcoming/{enterpriseId}")
    public R<List<DataSubmissionTask>> getUpcomingTasks(
            @Parameter(description="企业ID") @PathVariable String enterpriseId,
            @Parameter(description="天数") @RequestParam(defaultValue = "7") Integer days) {
        try {
            List<DataSubmissionTask> result = dataSubmissionTaskService.getUpcomingTasks(enterpriseId, days);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询即将到期的报送任务失败，企业ID：{}", enterpriseId, e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    /**
     * 查询逾期的报送任务
     */
    @Operation(summary = "查询数据")
    @GetMapping("/overdue/{enterpriseId}")
    public R<List<DataSubmissionTask>> getOverdueTasks(@Parameter(description="企业ID") @PathVariable String enterpriseId) {
        try {
            List<DataSubmissionTask> result = dataSubmissionTaskService.getOverdueTasks(enterpriseId);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询逾期的报送任务失败，企业ID：{}", enterpriseId, e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据任务状态统计数量
     */
    @Operation(summary = "")
    @GetMapping("/countByStatus/{enterpriseId}")
    public R<List<Map<String, Object>>> getTaskCountByStatus(@Parameter(description="企业ID") @PathVariable String enterpriseId) {
        try {
            List<Map<String, Object>> result = dataSubmissionTaskService.getTaskCountByStatus(enterpriseId);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询任务状态统计失败，企业ID：{}", enterpriseId, e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据优先级统计数量
     */
    @Operation(summary = "")
    @GetMapping("/countByPriority/{enterpriseId}")
    public R<List<Map<String, Object>>> getTaskCountByPriority(@Parameter(description="企业ID") @PathVariable String enterpriseId) {
        try {
            List<Map<String, Object>> result = dataSubmissionTaskService.getTaskCountByPriority(enterpriseId);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询任务优先级统计失败，企业ID：{}", enterpriseId, e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据报送类型统计数量
     */
    @Operation(summary = "")
    @GetMapping("/countBySubmissionType/{enterpriseId}")
    public R<List<Map<String, Object>>> getTaskCountBySubmissionType(@Parameter(description="企业ID") @PathVariable String enterpriseId) {
        try {
            List<Map<String, Object>> result = dataSubmissionTaskService.getTaskCountBySubmissionType(enterpriseId);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询报送类型统计失败，企业ID：{}", enterpriseId, e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    /**
     * 查询任务完成率趋势
     */
    @Operation(summary = "")
    @GetMapping("/completionTrend/{enterpriseId}")
    public R<List<Map<String, Object>>> getTaskCompletionTrend(
            @Parameter(description="企业ID") @PathVariable String enterpriseId,
            @Parameter(description="月份数") @RequestParam(defaultValue = "12") Integer months) {
        try {
            List<Map<String, Object>> result = dataSubmissionTaskService.getTaskCompletionTrend(enterpriseId, months);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询任务完成率趋势失败，企业ID：{}", enterpriseId, e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    /**
     * 查询任务质量评分分布
     */
    @Operation(summary = "")
    @GetMapping("/qualityScoreDistribution/{enterpriseId}")
    public R<List<Map<String, Object>>> getQualityScoreDistribution(@Parameter(description="企业ID") @PathVariable String enterpriseId) {
        try {
            List<Map<String, Object>> result = dataSubmissionTaskService.getQualityScoreDistribution(enterpriseId);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询任务质量评分分布失败，企业ID：{}", enterpriseId, e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    /**
     * 批量更新任务状态
     */
    @Operation(summary = "批量操作")
    @PutMapping("/batchUpdateStatus")
    public R<Boolean> batchUpdateTaskStatus(@RequestBody Map<String, Object> params) {
        try {
            @SuppressWarnings("unchecked")
            List<String> taskIds = (List<String>) params.get("taskIds");
            String status = (String) params.get("status");
            String updateBy = (String) params.get("updateBy");
            
            boolean result = dataSubmissionTaskService.batchUpdateTaskStatus(taskIds, status, updateBy);
            return result ? R.success(true, "批量更新成功") : R.fail("批量更新失败");
        } catch (Exception e) {
            log.error("批量更新任务状态失败", e);
            return R.fail("批量更新失败：" + e.getMessage());
        }
    }

    /**
     * 启动任务
     */
    @Operation(summary = "启用")
    @PutMapping("/start/{taskId}")
    public R<Boolean> startTask(
            @Parameter(description="任务ID") @PathVariable String taskId,
            @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = dataSubmissionTaskService.startTask(taskId, updateBy);
            return result ? R.success(true, "启动成功") : R.fail("启动失败");
        } catch (Exception e) {
            log.error("启动任务失败，任务ID：{}", taskId, e);
            return R.fail("启动失败：" + e.getMessage());
        }
    }

    /**
     * 完成任务
     */
    @Operation(summary = "completeTask")
    @PutMapping("/complete/{taskId}")
    public R<Boolean> completeTask(@Parameter(description="任务ID") @PathVariable String taskId, @RequestBody Map<String, String> params) {
        try {
            String completionDescription = params.get("completionDescription");
            String qualityScore = params.get("qualityScore");
            String updateBy = params.get("updateBy");
            
            boolean result = dataSubmissionTaskService.completeTask(taskId, completionDescription, qualityScore, updateBy);
            return result ? R.success(true, "完成成功") : R.fail("完成失败");
        } catch (Exception e) {
            log.error("完成任务失败，任务ID：{}", taskId, e);
            return R.fail("完成失败：" + e.getMessage());
        }
    }

    /**
     * 暂停任务
     */
    @Operation(summary = "pauseTask")
    @PutMapping("/pause/{taskId}")
    public R<Boolean> pauseTask(
            @Parameter(description="任务ID") @PathVariable String taskId,
            @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = dataSubmissionTaskService.pauseTask(taskId, updateBy);
            return result ? R.success(true, "暂停成功") : R.fail("暂停失败");
        } catch (Exception e) {
            log.error("暂停任务失败，任务ID：{}", taskId, e);
            return R.fail("暂停失败：" + e.getMessage());
        }
    }

    /**
     * 取消任务
     */
    @Operation(summary = "撤销")
    @PutMapping("/cancel/{taskId}")
    public R<Boolean> cancelTask(
            @Parameter(description="任务ID") @PathVariable String taskId,
            @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = dataSubmissionTaskService.cancelTask(taskId, updateBy);
            return result ? R.success(true, "取消成功") : R.fail("取消失败");
        } catch (Exception e) {
            log.error("取消任务失败，任务ID：{}", taskId, e);
            return R.fail("取消失败：" + e.getMessage());
        }
    }

    /**
     * 重启任务
     */
    @Operation(summary = "restartTask")
    @PutMapping("/restart/{taskId}")
    public R<Boolean> restartTask(
            @Parameter(description="任务ID") @PathVariable String taskId,
            @Parameter(description="更新人") @RequestParam String updateBy) {
        try {
            boolean result = dataSubmissionTaskService.restartTask(taskId, updateBy);
            return result ? R.success(true, "重启成功") : R.fail("重启失败");
        } catch (Exception e) {
            log.error("重启任务失败，任务ID：{}", taskId, e);
            return R.fail("重启失败：" + e.getMessage());
        }
    }

    /**
     * 提交反馈
     */
    @Operation(summary = "提交")
    @PutMapping("/submitFeedback/{taskId}")
    public R<Boolean> submitFeedback(@Parameter(description="任务ID") @PathVariable String taskId, @RequestBody Map<String, String> params) {
        try {
            String feedbackInfo = params.get("feedbackInfo");
            String feedbackStatus = params.get("feedbackStatus");
            String updateBy = params.get("updateBy");
            
            boolean result = dataSubmissionTaskService.submitFeedback(taskId, feedbackInfo, feedbackStatus, updateBy);
            return result ? R.success(true, "提交反馈成功") : R.fail("提交反馈失败");
        } catch (Exception e) {
            log.error("提交反馈失败，任务ID：{}", taskId, e);
            return R.fail("提交反馈失败：" + e.getMessage());
        }
    }

    /**
     * 处理反馈
     */
    @Operation(summary = "processFeedback")
    @PutMapping("/processFeedback/{taskId}")
    public R<Boolean> processFeedback(@Parameter(description="任务ID") @PathVariable String taskId, @RequestBody Map<String, String> params) {
        try {
            String rectificationPlan = params.get("rectificationPlan");
            String updateBy = params.get("updateBy");
            
            boolean result = dataSubmissionTaskService.processFeedback(taskId, rectificationPlan, updateBy);
            return result ? R.success(true, "处理反馈成功") : R.fail("处理反馈失败");
        } catch (Exception e) {
            log.error("处理反馈失败，任务ID：{}", taskId, e);
            return R.fail("处理反馈失败：" + e.getMessage());
        }
    }

    /**
     * 导出任务列表
     */
    @Operation(summary = "导出")
    @PostMapping("/export")
    public R<List<DataSubmissionTask>> exportTaskList(@RequestBody DataSubmissionTaskQueryVo queryVo) {
        try {
            List<DataSubmissionTask> result = dataSubmissionTaskService.exportTaskList(queryVo);
            return R.success(result);
        } catch (Exception e) {
            log.error("导出任务列表失败", e);
            return R.fail("导出失败：" + e.getMessage());
        }
    }

    /**
     * 生成任务报告
     */
    @Operation(summary = "")
    @GetMapping("/generateReport/{enterpriseId}")
    public R<Map<String, Object>> generateTaskReport(
            @Parameter(description="企业ID") @PathVariable String enterpriseId,
            @Parameter(description="报告类型") @RequestParam String reportType) {
        try {
            Map<String, Object> result = dataSubmissionTaskService.generateTaskReport(enterpriseId, reportType);
            return R.success(result);
        } catch (Exception e) {
            log.error("生成任务报告失败，企业ID：{}", enterpriseId, e);
            return R.fail("生成报告失败：" + e.getMessage());
        }
    }
}
