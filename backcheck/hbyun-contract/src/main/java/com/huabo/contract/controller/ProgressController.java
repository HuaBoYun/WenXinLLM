package com.huabo.contract.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

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
import com.huabo.contract.entity.Progress;
import com.huabo.contract.service.ProgressService;
import com.huabo.contract.vo.ProgressQueryParam;
import com.huabo.contract.vo.Result;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

/**
 * 进度管理控制器
 * 
 * @author 华博云开发团队
 * @since 2025-01-06
 */
@Tag(name="进度管理",description="进度管理")
@RestController
@RequestMapping("/progress")
@RequiredArgsConstructor
public class ProgressController {

    private final ProgressService progressService;

    @Operation(summary = "分页查询进度记录列表")
    @PostMapping("/page")
    public Result<IPage<Progress>> getProgressPage(@RequestBody ProgressQueryParam queryParam) {
        IPage<Progress> page = progressService.getProgressPage(queryParam);
        return Result.success(page);
    }

    @Operation(summary = "根据ID查询进度记录详情")
    @GetMapping("/{id}")
    public Result<Progress> getProgressById(@Parameter(description="进度记录ID") @PathVariable Long id) {
        Progress progress = progressService.getById(id);
        return Result.success(progress);
    }

    @Operation(summary = "新增进度记录")
    @PostMapping("/create")
    public Result<Boolean> saveProgress(@RequestBody Progress progress) {
        // 验证进度记录信息
        if (!progressService.validateProgressInfo(progress)) {
            return Result.fail("进度记录信息验证失败");
        }

        // 设置项目ID（如果未设置）
        if (progress.getProjectId() == null) {
            progress.setProjectId(1001L); // 设置默认项目ID
        }

        // 设置默认值
        if (progress.getReportDate() == null) {
            progress.setReportDate(new Date());
        }
        if (progress.getReporterId() == null) {
            progress.setReporterId(1001L); // 设置默认汇报人ID
        }

        boolean result = progressService.save(progress);
        return Result.success(result);
    }

    @Operation(summary = "修改进度记录")
    @PutMapping("/update/{id}")
    public Result<Boolean> updateProgress(@Parameter(description="进度记录ID") @PathVariable Long id,
                                        @RequestBody Progress progress) {
        // 验证进度记录信息
        if (!progressService.validateProgressInfo(progress)) {
            return Result.fail("进度记录信息验证失败");
        }
        
        progress.setId(id);
        boolean result = progressService.updateById(progress);
        return Result.success(result);
    }

    @Operation(summary = "删除进度记录")
    @DeleteMapping("/{id}")
    public Result<Boolean> deleteProgress(@Parameter(description="进度记录ID") @PathVariable Long id) {
        boolean result = progressService.removeById(id);
        return Result.success(result);
    }

    @Operation(summary = "批量删除进度记录")
    @DeleteMapping("/batch")
    public Result<Boolean> batchDeleteProgress(@RequestBody List<Long> ids) {
        boolean result = progressService.removeByIds(ids);
        return Result.success(result);
    }

    @Operation(summary = "根据项目ID查询进度记录列表")
    @GetMapping("/project/{projectId}")
    public Result<List<Progress>> getByProjectId(@Parameter(description="项目ID") @PathVariable Long projectId) {
        List<Progress> list = progressService.getByProjectId(projectId);
        return Result.success(list);
    }

    @Operation(summary = "根据进度类型查询进度记录列表")
    @GetMapping("/type/{progressType}")
    public Result<List<Progress>> getByProgressType(@Parameter(description="进度类型") @PathVariable Integer progressType) {
        List<Progress> list = progressService.getByProgressType(progressType);
        return Result.success(list);
    }

    @Operation(summary = "根据进度状态查询进度记录列表")
    @GetMapping("/status/{progressStatus}")
    public Result<List<Progress>> getByProgressStatus(@Parameter(description="进度状态") @PathVariable Integer progressStatus) {
        List<Progress> list = progressService.getByProgressStatus(progressStatus);
        return Result.success(list);
    }

    @Operation(summary = "根据优先级查询进度记录列表")
    @GetMapping("/priority/{priority}")
    public Result<List<Progress>> getByPriority(@Parameter(description="优先级") @PathVariable Integer priority) {
        List<Progress> list = progressService.getByPriority(priority);
        return Result.success(list);
    }

    @Operation(summary = "根据负责人ID查询进度记录列表")
    @GetMapping("/manager/{managerId}")
    public Result<List<Progress>> getByManagerId(@Parameter(description="负责人ID") @PathVariable Long managerId) {
        List<Progress> list = progressService.getByManagerId(managerId);
        return Result.success(list);
    }

    @Operation(summary = "查询里程碑进度记录列表")
    @GetMapping("/milestones/{projectId}")
    public Result<List<Progress>> getMilestones(@Parameter(description="项目ID") @PathVariable Long projectId) {
        List<Progress> list = progressService.getMilestones(projectId);
        return Result.success(list);
    }

    @Operation(summary = "查询关键路径进度记录列表")
    @GetMapping("/critical-path/{projectId}")
    public Result<List<Progress>> getCriticalPath(@Parameter(description="项目ID") @PathVariable Long projectId) {
        List<Progress> list = progressService.getCriticalPath(projectId);
        return Result.success(list);
    }

    @Operation(summary = "根据项目ID统计总体进度")
    @GetMapping("/overall-progress/{projectId}")
    public Result<BigDecimal> calculateOverallProgress(@Parameter(description="项目ID") @PathVariable Long projectId) {
        BigDecimal progress = progressService.calculateOverallProgress(projectId);
        return Result.success(progress);
    }

    @Operation(summary = "查询延期的进度记录列表")
    @GetMapping("/delayed")
    public Result<List<Progress>> getDelayedProgress() {
        List<Progress> list = progressService.getDelayedProgress();
        return Result.success(list);
    }

    @Operation(summary = "查询提前完成的进度记录列表")
    @GetMapping("/early-completed")
    public Result<List<Progress>> getEarlyCompletedProgress() {
        List<Progress> list = progressService.getEarlyCompletedProgress();
        return Result.success(list);
    }

    @Operation(summary = "查询高优先级进度记录列表")
    @GetMapping("/high-priority")
    public Result<List<Progress>> getHighPriorityProgress() {
        List<Progress> list = progressService.getHighPriorityProgress();
        return Result.success(list);
    }

    @Operation(summary = "查询即将到期的进度记录列表")
    @GetMapping("/upcoming-deadlines")
    public Result<List<Progress>> getUpcomingDeadlines(@Parameter(description="天数") @RequestParam(defaultValue = "7") Integer days) {
        List<Progress> list = progressService.getUpcomingDeadlines(days);
        return Result.success(list);
    }

    @Operation(summary = "批量更新进度状态")
    @PutMapping("/batch-status")
    public Result<Integer> batchUpdateProgressStatus(@RequestBody List<Long> ids, 
                                                   @Parameter(description="新状态") @RequestParam Integer progressStatus,
                                                   @Parameter(description="更新人") @RequestParam Long updateBy) {
        Integer count = progressService.batchUpdateProgressStatus(ids, progressStatus, updateBy);
        return Result.success(count);
    }

    @Operation(summary = "批量更新实际进度")
    @PutMapping("/batch-actual-progress")
    public Result<Integer> batchUpdateActualProgress(@RequestBody List<Long> ids, 
                                                   @Parameter(description="实际进度") @RequestParam BigDecimal actualProgress,
                                                   @Parameter(description="更新人") @RequestParam Long updateBy) {
        Integer count = progressService.batchUpdateActualProgress(ids, actualProgress, updateBy);
        return Result.success(count);
    }

    @Operation(summary = "查询项目进度树形结构")
    @GetMapping("/tree/{projectId}")
    public Result<List<Progress>> getProgressTree(@Parameter(description="项目ID") @PathVariable Long projectId) {
        List<Progress> tree = progressService.getProgressTree(projectId);
        return Result.success(tree);
    }

    @Operation(summary = "更新进度")
    @PutMapping("/update-progress/{id}")
    public Result<Boolean> updateProgress(@Parameter(description="进度记录ID") @PathVariable Long id,
                                        @Parameter(description="实际进度") @RequestParam BigDecimal actualProgress,
                                        @Parameter(description="完成情况说明") @RequestParam(required = false) String completionDescription,
                                        @Parameter(description="更新人") @RequestParam Long updateBy) {
        Boolean result = progressService.updateProgress(id, actualProgress, completionDescription, updateBy);
        return Result.success(result);
    }

    @Operation(summary = "开始进度")
    @PostMapping("/start/{id}")
    public Result<Boolean> startProgress(@Parameter(description="进度记录ID") @PathVariable Long id,
                                       @Parameter(description="实际开始时间") @RequestParam(required = false) Date actualStartTime,
                                       @Parameter(description="更新人") @RequestParam Long updateBy) {
        Boolean result = progressService.startProgress(id, actualStartTime, updateBy);
        return Result.success(result);
    }

    @Operation(summary = "完成进度")
    @PostMapping("/complete/{id}")
    public Result<Boolean> completeProgress(@Parameter(description="进度记录ID") @PathVariable Long id,
                                          @Parameter(description="实际结束时间") @RequestParam(required = false) Date actualEndTime,
                                          @Parameter(description="完成情况说明") @RequestParam(required = false) String completionDescription,
                                          @Parameter(description="更新人") @RequestParam Long updateBy) {
        Boolean result = progressService.completeProgress(id, actualEndTime, completionDescription, updateBy);
        return Result.success(result);
    }

    @Operation(summary = "暂停进度")
    @PostMapping("/pause/{id}")
    public Result<Boolean> pauseProgress(@Parameter(description="进度记录ID") @PathVariable Long id,
                                       @Parameter(description="暂停原因") @RequestParam String reason,
                                       @Parameter(description="更新人") @RequestParam Long updateBy) {
        Boolean result = progressService.pauseProgress(id, reason, updateBy);
        return Result.success(result);
    }

    @Operation(summary = "恢复进度")
    @PostMapping("/resume/{id}")
    public Result<Boolean> resumeProgress(@Parameter(description="进度记录ID") @PathVariable Long id,
                                        @Parameter(description="更新人") @RequestParam Long updateBy) {
        Boolean result = progressService.resumeProgress(id, updateBy);
        return Result.success(result);
    }

    @Operation(summary = "取消进度")
    @PostMapping("/cancel/{id}")
    public Result<Boolean> cancelProgress(@Parameter(description="进度记录ID") @PathVariable Long id,
                                        @Parameter(description="取消原因") @RequestParam String reason,
                                        @Parameter(description="更新人") @RequestParam Long updateBy) {
        Boolean result = progressService.cancelProgress(id, reason, updateBy);
        return Result.success(result);
    }

    @Operation(summary = "根据关键词搜索进度记录")
    @GetMapping("/search")
    public Result<List<Progress>> searchByKeyword(@Parameter(description="关键词") @RequestParam String keyword,
                                                @Parameter(description="限制数量") @RequestParam(defaultValue = "10") Integer limit) {
        List<Progress> list = progressService.searchByKeyword(keyword, limit);
        return Result.success(list);
    }

    @Operation(summary = "查询进度统计信息")
    @GetMapping("/statistics")
    public Result<List<Progress>> getProgressStatistics() {
        List<Progress> statistics = progressService.getProgressStatistics();
        return Result.success(statistics);
    }

    @Operation(summary = "获取项目进度报告")
    @GetMapping("/project-report/{projectId}")
    public Result<List<String>> getProjectProgressReport(@Parameter(description="项目ID") @PathVariable Long projectId) {
        List<String> report = progressService.getProjectProgressReport(projectId);
        return Result.success(report);
    }

    @Operation(summary = "生成进度编号")
    @GetMapping("/generate-progress-no")
    public Result<String> generateProgressNo() {
        String progressNo = progressService.generateProgressNo();
        return Result.success(progressNo);
    }
}
