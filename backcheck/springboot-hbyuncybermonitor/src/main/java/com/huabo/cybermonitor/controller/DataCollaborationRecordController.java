package com.huabo.cybermonitor.controller;

import java.util.HashMap;
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
import com.huabo.cybermonitor.entity.DataCollaborationRecord;
import com.huabo.cybermonitor.service.IDataCollaborationRecordService;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.util.R;
import com.huabo.cybermonitor.vo.DataCollaborationRecordQueryVO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;



/**
 * 数据协同记录管理 Controller
 *
 * @author system
 * @since 2024-01-01
 */
@Tag(name="数据协同记录管理",description="数据协同记录管理")
@RestController
@RequestMapping("/v1/data/collaboration")
public class DataCollaborationRecordController {

	private static final Logger log = LoggerFactory.getLogger(DataCollaborationRecordController.class);

    @Autowired
    private IDataCollaborationRecordService collaborationService;

    @Operation(summary = "分页查询数据协同记录列表")
    @PostMapping("/list")
    public R<PageResult<DataCollaborationRecord>> getCollaborationList(@RequestBody DataCollaborationRecordQueryVO queryVO) {
        try {
            IPage<DataCollaborationRecord> page = collaborationService.getCollaborationList(queryVO);
            PageResult<DataCollaborationRecord> pageResult = new PageResult<>();
            pageResult.setTlist(page.getRecords());
            pageResult.setTotalRecord((int) page.getTotal());
            pageResult.setPageNumber((int) page.getCurrent());
            pageResult.setPageSize((int) page.getSize());
            
            return R.success(pageResult);
        } catch (Exception e) {
            log.error("查询数据协同记录列表失败", e);
            return R.fail("查询数据协同记录列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取数据协同记录详情")
    @PostMapping("/detail")
    public R<DataCollaborationRecord> getCollaborationDetail(@RequestBody Map<String, String> params) {
        try {
            String recordId = params.get("recordId");
            if (recordId == null || recordId.trim().isEmpty()) {
                return R.fail("记录ID不能为空");
            }
            
            DataCollaborationRecord record = collaborationService.getCollaborationDetail(recordId);
            return R.success(record);
        } catch (Exception e) {
            log.error("获取数据协同记录详情失败", e);
            return R.fail("获取数据协同记录详情失败：" + e.getMessage());
        }
    }

    @Operation(summary = "新增数据协同记录")
    @PostMapping("/add")
    public R<String> addCollaborationRecord(@RequestBody DataCollaborationRecord record) {
        try {
            boolean success = collaborationService.addCollaborationRecord(record);
            if (success) {
                return R.success("新增数据协同记录成功");
            } else {
                return R.fail("新增数据协同记录失败");
            }
        } catch (Exception e) {
            log.error("新增数据协同记录失败", e);
            return R.fail("新增数据协同记录失败：" + e.getMessage());
        }
    }

    @Operation(summary = "更新数据协同记录")
    @PostMapping("/update")
    public R<String> updateCollaborationRecord(@RequestBody DataCollaborationRecord record) {
        try {
            boolean success = collaborationService.updateCollaborationRecord(record);
            if (success) {
                return R.success("更新数据协同记录成功");
            } else {
                return R.fail("更新数据协同记录失败");
            }
        } catch (Exception e) {
            log.error("更新数据协同记录失败", e);
            return R.fail("更新数据协同记录失败：" + e.getMessage());
        }
    }

    @Operation(summary = "删除数据协同记录")
    @PostMapping("/delete")
    public R<String> deleteCollaborationRecord(@RequestBody Map<String, String> params) {
        try {
            String recordId = params.get("recordId");
            if (recordId == null || recordId.trim().isEmpty()) {
                return R.fail("记录ID不能为空");
            }
            
            boolean success = collaborationService.deleteCollaborationRecord(recordId);
            if (success) {
                return R.success("删除数据协同记录成功");
            } else {
                return R.fail("删除数据协同记录失败");
            }
        } catch (Exception e) {
            log.error("删除数据协同记录失败", e);
            return R.fail("删除数据协同记录失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量删除数据协同记录")
    @PostMapping("/batch-delete")
    public R<String> batchDeleteCollaborationRecord(@RequestBody Map<String, List<String>> params) {
        try {
            List<String> recordIds = params.get("recordIds");
            if (recordIds == null || recordIds.isEmpty()) {
                return R.fail("记录ID列表不能为空");
            }
            
            boolean success = collaborationService.batchDeleteCollaborationRecord(recordIds);
            if (success) {
                return R.success("批量删除数据协同记录成功");
            } else {
                return R.fail("批量删除数据协同记录失败");
            }
        } catch (Exception e) {
            log.error("批量删除数据协同记录失败", e);
            return R.fail("批量删除数据协同记录失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据协同类型查询记录列表")
    @PostMapping("/list-by-type")
    public R<List<DataCollaborationRecord>> getRecordsByType(@RequestBody Map<String, String> params) {
        try {
            String collaborationType = params.get("collaborationType");
            if (collaborationType == null || collaborationType.trim().isEmpty()) {
                return R.fail("协同类型不能为空");
            }
            
            List<DataCollaborationRecord> records = collaborationService.getRecordsByType(collaborationType);
            return R.success(records);
        } catch (Exception e) {
            log.error("根据协同类型查询记录列表失败", e);
            return R.fail("根据协同类型查询记录列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据协同状态查询记录列表")
    @PostMapping("/list-by-status")
    public R<List<DataCollaborationRecord>> getRecordsByStatus(@RequestBody Map<String, String> params) {
        try {
            String collaborationStatus = params.get("collaborationStatus");
            if (collaborationStatus == null || collaborationStatus.trim().isEmpty()) {
                return R.fail("协同状态不能为空");
            }
            
            List<DataCollaborationRecord> records = collaborationService.getRecordsByStatus(collaborationStatus);
            return R.success(records);
        } catch (Exception e) {
            log.error("根据协同状态查询记录列表失败", e);
            return R.fail("根据协同状态查询记录列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据源系统查询记录列表")
    @PostMapping("/list-by-source-system")
    public R<List<DataCollaborationRecord>> getRecordsBySourceSystem(@RequestBody Map<String, String> params) {
        try {
            String sourceSystem = params.get("sourceSystem");
            if (sourceSystem == null || sourceSystem.trim().isEmpty()) {
                return R.fail("源系统不能为空");
            }
            
            List<DataCollaborationRecord> records = collaborationService.getRecordsBySourceSystem(sourceSystem);
            return R.success(records);
        } catch (Exception e) {
            log.error("根据源系统查询记录列表失败", e);
            return R.fail("根据源系统查询记录列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据目标系统查询记录列表")
    @PostMapping("/list-by-target-system")
    public R<List<DataCollaborationRecord>> getRecordsByTargetSystem(@RequestBody Map<String, String> params) {
        try {
            String targetSystem = params.get("targetSystem");
            if (targetSystem == null || targetSystem.trim().isEmpty()) {
                return R.fail("目标系统不能为空");
            }
            
            List<DataCollaborationRecord> records = collaborationService.getRecordsByTargetSystem(targetSystem);
            return R.success(records);
        } catch (Exception e) {
            log.error("根据目标系统查询记录列表失败", e);
            return R.fail("根据目标系统查询记录列表失败：" + e.getMessage());
        }
    }

    @Operation(summary = "查询正在处理的协同记录")
    @PostMapping("/processing-records")
    public R<List<DataCollaborationRecord>> getProcessingRecords() {
        try {
            List<DataCollaborationRecord> records = collaborationService.getProcessingRecords();
            return R.success(records);
        } catch (Exception e) {
            log.error("查询正在处理的协同记录失败", e);
            return R.fail("查询正在处理的协同记录失败：" + e.getMessage());
        }
    }

    @Operation(summary = "查询失败的协同记录")
    @PostMapping("/failed-records")
    public R<List<DataCollaborationRecord>> getFailedRecords() {
        try {
            List<DataCollaborationRecord> records = collaborationService.getFailedRecords();
            return R.success(records);
        } catch (Exception e) {
            log.error("查询失败的协同记录失败", e);
            return R.fail("查询失败的协同记录失败：" + e.getMessage());
        }
    }

    @Operation(summary = "重试失败的协同记录")
    @PostMapping("/retry")
    public R<String> retryCollaboration(@RequestBody Map<String, String> params) {
        try {
            String recordId = params.get("recordId");
            if (recordId == null || recordId.trim().isEmpty()) {
                return R.fail("记录ID不能为空");
            }
            
            boolean success = collaborationService.retryCollaboration(recordId);
            if (success) {
                return R.success("重试协同记录成功");
            } else {
                return R.fail("重试协同记录失败");
            }
        } catch (Exception e) {
            log.error("重试协同记录失败", e);
            return R.fail("重试协同记录失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量重试失败的协同记录")
    @PostMapping("/batch-retry")
    public R<String> batchRetryCollaboration(@RequestBody Map<String, List<String>> params) {
        try {
            List<String> recordIds = params.get("recordIds");
            if (recordIds == null || recordIds.isEmpty()) {
                return R.fail("记录ID列表不能为空");
            }
            
            boolean success = collaborationService.batchRetryCollaboration(recordIds);
            if (success) {
                return R.success("批量重试协同记录成功");
            } else {
                return R.fail("批量重试协同记录失败");
            }
        } catch (Exception e) {
            log.error("批量重试协同记录失败", e);
            return R.fail("批量重试协同记录失败：" + e.getMessage());
        }
    }

    @Operation(summary = "取消协同记录")
    @PostMapping("/cancel")
    public R<String> cancelCollaboration(@RequestBody Map<String, String> params) {
        try {
            String recordId = params.get("recordId");
            if (recordId == null || recordId.trim().isEmpty()) {
                return R.fail("记录ID不能为空");
            }
            
            boolean success = collaborationService.cancelCollaboration(recordId);
            if (success) {
                return R.success("取消协同记录成功");
            } else {
                return R.fail("取消协同记录失败");
            }
        } catch (Exception e) {
            log.error("取消协同记录失败", e);
            return R.fail("取消协同记录失败：" + e.getMessage());
        }
    }

    @Operation(summary = "更新协同状态")
    @PostMapping("/update-status")
    public R<String> updateCollaborationStatus(@RequestBody Map<String, String> params) {
        try {
            String recordId = params.get("recordId");
            String collaborationStatus = params.get("collaborationStatus");
            String errorMessage = params.get("errorMessage");
            
            if (recordId == null || recordId.trim().isEmpty()) {
                return R.fail("记录ID不能为空");
            }
            if (collaborationStatus == null || collaborationStatus.trim().isEmpty()) {
                return R.fail("协同状态不能为空");
            }
            
            boolean success = collaborationService.updateCollaborationStatus(recordId, collaborationStatus, errorMessage);
            if (success) {
                return R.success("更新协同状态成功");
            } else {
                return R.fail("更新协同状态失败");
            }
        } catch (Exception e) {
            log.error("更新协同状态失败", e);
            return R.fail("更新协同状态失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取协同记录统计信息")
    @PostMapping("/statistics/overview")
    public R<Map<String, Object>> getCollaborationStatistics() {
        try {
            Map<String, Object> statistics = collaborationService.getCollaborationStatistics();
            return R.success(statistics);
        } catch (Exception e) {
            log.error("获取协同记录统计信息失败", e);
            return R.fail("获取协同记录统计信息失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取协同类型分布统计")
    @PostMapping("/statistics/type-distribution")
    public R<List<Map<String, Object>>> getCollaborationTypeDistribution() {
        try {
            List<Map<String, Object>> distribution = collaborationService.getCollaborationTypeDistribution();
            return R.success(distribution);
        } catch (Exception e) {
            log.error("获取协同类型分布统计失败", e);
            return R.fail("获取协同类型分布统计失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取协同状态分布统计")
    @PostMapping("/statistics/status-distribution")
    public R<List<Map<String, Object>>> getCollaborationStatusDistribution() {
        try {
            List<Map<String, Object>> distribution = collaborationService.getCollaborationStatusDistribution();
            return R.success(distribution);
        } catch (Exception e) {
            log.error("获取协同状态分布统计失败", e);
            return R.fail("获取协同状态分布统计失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取系统协同统计")
    @PostMapping("/statistics/system-collaboration")
    public R<List<Map<String, Object>>> getSystemCollaborationStatistics() {
        try {
            List<Map<String, Object>> statistics = collaborationService.getSystemCollaborationStatistics();
            return R.success(statistics);
        } catch (Exception e) {
            log.error("获取系统协同统计失败", e);
            return R.fail("获取系统协同统计失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取协同性能统计")
    @PostMapping("/statistics/performance")
    public R<Map<String, Object>> getCollaborationPerformanceStatistics() {
        try {
            Map<String, Object> statistics = collaborationService.getCollaborationPerformanceStatistics();
            return R.success(statistics);
        } catch (Exception e) {
            log.error("获取协同性能统计失败", e);
            return R.fail("获取协同性能统计失败：" + e.getMessage());
        }
    }

    @Operation(summary = "根据时间范围获取协同趋势")
    @PostMapping("/statistics/trend")
    public R<List<Map<String, Object>>> getCollaborationTrend(@RequestBody Map<String, String> params) {
        try {
            String startDate = params.get("startDate");
            String endDate = params.get("endDate");
            
            if (startDate == null || startDate.trim().isEmpty()) {
                return R.fail("开始日期不能为空");
            }
            if (endDate == null || endDate.trim().isEmpty()) {
                return R.fail("结束日期不能为空");
            }
            
            List<Map<String, Object>> trend = collaborationService.getCollaborationTrend(startDate, endDate);
            return R.success(trend);
        } catch (Exception e) {
            log.error("获取协同趋势失败", e);
            return R.fail("获取协同趋势失败：" + e.getMessage());
        }
    }

    @Operation(summary = "验证协同配置")
    @PostMapping("/validate-config")
    public R<Map<String, Object>> validateCollaborationConfig(@RequestBody Map<String, String> params) {
        try {
            String sourceSystem = params.get("sourceSystem");
            String targetSystem = params.get("targetSystem");
            String dataType = params.get("dataType");
            
            Map<String, Object> result = collaborationService.validateCollaborationConfig(sourceSystem, targetSystem, dataType);
            return R.success(result);
        } catch (Exception e) {
            log.error("验证协同配置失败", e);
            return R.fail("验证协同配置失败：" + e.getMessage());
        }
    }

    @Operation(summary = "创建数据协同任务")
    @PostMapping("/create-task")
    public R<Map<String, Object>> createCollaborationTask(@RequestBody Map<String, String> params) {
        try {
            String collaborationType = params.get("collaborationType");
            String sourceSystem = params.get("sourceSystem");
            String targetSystem = params.get("targetSystem");
            String dataType = params.get("dataType");
            String triggerType = params.get("triggerType");
            String dataContent = params.get("dataContent");
            
            String recordId = collaborationService.createCollaborationTask(
                collaborationType, sourceSystem, targetSystem, dataType, triggerType, dataContent);

            Map<String, Object> result = new HashMap<>();
            result.put("recordId", recordId);
            result.put("message", "创建数据协同任务成功");

            return R.success(result);
        } catch (Exception e) {
            log.error("创建数据协同任务失败", e);
            return R.fail("创建数据协同任务失败：" + e.getMessage());
        }
    }

    @Operation(summary = "执行数据协同")
    @PostMapping("/execute")
    public R<Map<String, Object>> executeCollaboration(@RequestBody Map<String, String> params) {
        try {
            String recordId = params.get("recordId");
            if (recordId == null || recordId.trim().isEmpty()) {
                return R.fail("记录ID不能为空");
            }
            
            Map<String, Object> result = collaborationService.executeCollaboration(recordId);
            return R.success(result);
        } catch (Exception e) {
            log.error("执行数据协同失败", e);
            return R.fail("执行数据协同失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取协同进度")
    @PostMapping("/progress")
    public R<Map<String, Object>> getCollaborationProgress(@RequestBody Map<String, String> params) {
        try {
            String recordId = params.get("recordId");
            if (recordId == null || recordId.trim().isEmpty()) {
                return R.fail("记录ID不能为空");
            }
            
            Map<String, Object> progress = collaborationService.getCollaborationProgress(recordId);
            return R.success(progress);
        } catch (Exception e) {
            log.error("获取协同进度失败", e);
            return R.fail("获取协同进度失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取协同日志")
    @PostMapping("/logs")
    public R<List<Map<String, Object>>> getCollaborationLogs(@RequestBody Map<String, String> params) {
        try {
            String recordId = params.get("recordId");
            if (recordId == null || recordId.trim().isEmpty()) {
                return R.fail("记录ID不能为空");
            }
            
            List<Map<String, Object>> logs = collaborationService.getCollaborationLogs(recordId);
            return R.success(logs);
        } catch (Exception e) {
            log.error("获取协同日志失败", e);
            return R.fail("获取协同日志失败：" + e.getMessage());
        }
    }

    @Operation(summary = "清理过期的协同记录")
    @PostMapping("/clean-expired")
    public R<Map<String, Object>> cleanExpiredRecords(@RequestBody Map<String, Integer> params) {
        try {
            Integer days = params.get("days");
            if (days == null || days <= 0) {
                days = 30; // 默认30天
            }
            
            Integer cleanedCount = collaborationService.cleanExpiredRecords(days);

            Map<String, Object> result = new HashMap<>();
            result.put("cleanedCount", cleanedCount);
            result.put("days", days);
            result.put("message", "清理过期协同记录成功");
            
            return R.success(result);
        } catch (Exception e) {
            log.error("清理过期协同记录失败", e);
            return R.fail("清理过期协同记录失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取协同效率报告")
    @PostMapping("/efficiency-report")
    public R<Map<String, Object>> getCollaborationEfficiencyReport(@RequestBody Map<String, String> params) {
        try {
            String startDate = params.get("startDate");
            String endDate = params.get("endDate");
            
            if (startDate == null || startDate.trim().isEmpty()) {
                return R.fail("开始日期不能为空");
            }
            if (endDate == null || endDate.trim().isEmpty()) {
                return R.fail("结束日期不能为空");
            }
            
            Map<String, Object> report = collaborationService.getCollaborationEfficiencyReport(startDate, endDate);
            return R.success(report);
        } catch (Exception e) {
            log.error("获取协同效率报告失败", e);
            return R.fail("获取协同效率报告失败：" + e.getMessage());
        }
    }

    @Operation(summary = "导出协同记录列表")
    @PostMapping("/export")
    public void exportCollaborationList(@RequestBody DataCollaborationRecordQueryVO queryVO, HttpServletResponse response) {
        try {
            collaborationService.exportCollaborationList(queryVO, response);
        } catch (Exception e) {
            log.error("导出协同记录列表失败", e);
            throw new RuntimeException("导出协同记录列表失败：" + e.getMessage());
        }
    }
}
