package com.management.accountant.controller.intg;

import com.management.accountant.common.J8;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.management.accountant.entity.intg.IntgSyncLog;
import com.management.accountant.service.intg.IntgSyncLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 同步日志控制器
 *
 * @author AI Assistant
 * @since 2024-01-20
 */
@Slf4j
@RestController
@RequestMapping("/api/intg/sync-log")
@Api(tags = "同步日志管理")
public class IntgSyncLogController {

    @Autowired
    private IntgSyncLogService syncLogService;

    // 基础CRUD操作

    @PostMapping
    @ApiOperation("创建同步日志")
    public ResponseEntity<Map<String, Object>> createSyncLog(
            @ApiParam("同步日志信息") @Valid @RequestBody IntgSyncLog syncLog) {
        try {
            IntgSyncLog result = syncLogService.createSyncLog(syncLog);
            return ResponseEntity.ok(J8.mapOf(
                "success", true,
                "message", "同步日志创建成功",
                "data", result
            ));
        } catch (Exception e) {
            log.error("创建同步日志失败", e);
            return ResponseEntity.ok(J8.mapOf(
                "success", false,
                "message", "创建同步日志失败: " + e.getMessage()
            ));
        }
    }

    @PutMapping("/{logId}")
    @ApiOperation("更新同步日志")
    public ResponseEntity<Map<String, Object>> updateSyncLog(
            @ApiParam("日志ID") @PathVariable String logId,
            @ApiParam("同步日志信息") @Valid @RequestBody IntgSyncLog syncLog) {
        try {
            syncLog.setLogId(logId);
            IntgSyncLog result = syncLogService.updateSyncLog(syncLog);
            return ResponseEntity.ok(J8.mapOf(
                "success", true,
                "message", "同步日志更新成功",
                "data", result
            ));
        } catch (Exception e) {
            log.error("更新同步日志失败", e);
            return ResponseEntity.ok(J8.mapOf(
                "success", false,
                "message", "更新同步日志失败: " + e.getMessage()
            ));
        }
    }

    @DeleteMapping("/{logId}")
    @ApiOperation("删除同步日志")
    public ResponseEntity<Map<String, Object>> deleteSyncLog(
            @ApiParam("日志ID") @PathVariable String logId) {
        try {
            boolean result = syncLogService.deleteSyncLog(logId);
            return ResponseEntity.ok(J8.mapOf(
                "success", result,
                "message", result ? "同步日志删除成功" : "同步日志删除失败"
            ));
        } catch (Exception e) {
            log.error("删除同步日志失败", e);
            return ResponseEntity.ok(J8.mapOf(
                "success", false,
                "message", "删除同步日志失败: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/{logId}")
    @ApiOperation("根据ID获取同步日志")
    public ResponseEntity<Map<String, Object>> getSyncLogById(
            @ApiParam("日志ID") @PathVariable String logId) {
        try {
            IntgSyncLog result = syncLogService.getSyncLogById(logId);
            return ResponseEntity.ok(J8.mapOf(
                "success", true,
                "message", "获取同步日志成功",
                "data", result
            ));
        } catch (Exception e) {
            log.error("获取同步日志失败", e);
            return ResponseEntity.ok(J8.mapOf(
                "success", false,
                "message", "获取同步日志失败: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/code/{logCode}")
    @ApiOperation("根据编码获取同步日志")
    public ResponseEntity<Map<String, Object>> getSyncLogByCode(
            @ApiParam("日志编码") @PathVariable String logCode) {
        try {
            IntgSyncLog result = syncLogService.getSyncLogByCode(logCode);
            return ResponseEntity.ok(J8.mapOf(
                "success", true,
                "message", "获取同步日志成功",
                "data", result
            ));
        } catch (Exception e) {
            log.error("获取同步日志失败", e);
            return ResponseEntity.ok(J8.mapOf(
                "success", false,
                "message", "获取同步日志失败: " + e.getMessage()
            ));
        }
    }

    // 查询操作

    @GetMapping("/page")
    @ApiOperation("分页查询同步日志")
    public ResponseEntity<Map<String, Object>> getSyncLogPage(
            @ApiParam("当前页") @RequestParam(defaultValue = "1") Integer current,
            @ApiParam("页大小") @RequestParam(defaultValue = "10") Integer size,
            @ApiParam("查询参数") @RequestParam Map<String, Object> params) {
        try {
            IPage<IntgSyncLog> result = syncLogService.getSyncLogPage(current, size, params);
            return ResponseEntity.ok(J8.mapOf(
                "success", true,
                "message", "查询同步日志成功",
                "data", result
            ));
        } catch (Exception e) {
            log.error("查询同步日志失败", e);
            return ResponseEntity.ok(J8.mapOf(
                "success", false,
                "message", "查询同步日志失败: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/config/{configId}")
    @ApiOperation("根据系统配置ID查询日志列表")
    public ResponseEntity<Map<String, Object>> getSyncLogsByConfigId(
            @ApiParam("配置ID") @PathVariable String configId) {
        try {
            List<IntgSyncLog> result = syncLogService.getSyncLogsByConfigId(configId);
            return ResponseEntity.ok(J8.mapOf(
                "success", true,
                "message", "查询同步日志成功",
                "data", result
            ));
        } catch (Exception e) {
            log.error("查询同步日志失败", e);
            return ResponseEntity.ok(J8.mapOf(
                "success", false,
                "message", "查询同步日志失败: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/mapping/{mappingId}")
    @ApiOperation("根据映射配置ID查询日志列表")
    public ResponseEntity<Map<String, Object>> getSyncLogsByMappingId(
            @ApiParam("映射ID") @PathVariable String mappingId) {
        try {
            List<IntgSyncLog> result = syncLogService.getSyncLogsByMappingId(mappingId);
            return ResponseEntity.ok(J8.mapOf(
                "success", true,
                "message", "查询同步日志成功",
                "data", result
            ));
        } catch (Exception e) {
            log.error("查询同步日志失败", e);
            return ResponseEntity.ok(J8.mapOf(
                "success", false,
                "message", "查询同步日志失败: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/task/{taskId}")
    @ApiOperation("根据任务ID查询日志列表")
    public ResponseEntity<Map<String, Object>> getSyncLogsByTaskId(
            @ApiParam("任务ID") @PathVariable String taskId) {
        try {
            List<IntgSyncLog> result = syncLogService.getSyncLogsByTaskId(taskId);
            return ResponseEntity.ok(J8.mapOf(
                "success", true,
                "message", "查询同步日志成功",
                "data", result
            ));
        } catch (Exception e) {
            log.error("查询同步日志失败", e);
            return ResponseEntity.ok(J8.mapOf(
                "success", false,
                "message", "查询同步日志失败: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/batch/{batchNumber}")
    @ApiOperation("根据批次号查询日志列表")
    public ResponseEntity<Map<String, Object>> getSyncLogsByBatchNumber(
            @ApiParam("批次号") @PathVariable String batchNumber) {
        try {
            List<IntgSyncLog> result = syncLogService.getSyncLogsByBatchNumber(batchNumber);
            return ResponseEntity.ok(J8.mapOf(
                "success", true,
                "message", "查询同步日志成功",
                "data", result
            ));
        } catch (Exception e) {
            log.error("查询同步日志失败", e);
            return ResponseEntity.ok(J8.mapOf(
                "success", false,
                "message", "查询同步日志失败: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/status/{syncStatus}")
    @ApiOperation("根据同步状态查询日志列表")
    public ResponseEntity<Map<String, Object>> getSyncLogsByStatus(
            @ApiParam("同步状态") @PathVariable String syncStatus) {
        try {
            List<IntgSyncLog> result = syncLogService.getSyncLogsByStatus(syncStatus);
            return ResponseEntity.ok(J8.mapOf(
                "success", true,
                "message", "查询同步日志成功",
                "data", result
            ));
        } catch (Exception e) {
            log.error("查询同步日志失败", e);
            return ResponseEntity.ok(J8.mapOf(
                "success", false,
                "message", "查询同步日志失败: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/result/{executionResult}")
    @ApiOperation("根据执行结果查询日志列表")
    public ResponseEntity<Map<String, Object>> getSyncLogsByResult(
            @ApiParam("执行结果") @PathVariable String executionResult) {
        try {
            List<IntgSyncLog> result = syncLogService.getSyncLogsByResult(executionResult);
            return ResponseEntity.ok(J8.mapOf(
                "success", true,
                "message", "查询同步日志成功",
                "data", result
            ));
        } catch (Exception e) {
            log.error("查询同步日志失败", e);
            return ResponseEntity.ok(J8.mapOf(
                "success", false,
                "message", "查询同步日志失败: " + e.getMessage()
            ));
        }
    }

    @GetMapping("/recent")
    @ApiOperation("查询最近的日志列表")
    public ResponseEntity<Map<String, Object>> getRecentSyncLogs(
            @ApiParam("限制数量") @RequestParam(defaultValue = "10") Integer limit) {
        try {
            List<IntgSyncLog> result = syncLogService.getRecentSyncLogs(limit);
            return ResponseEntity.ok(J8.mapOf(
                "success", true,
                "message", "查询最近日志成功",
                "data", result
            ));
        } catch (Exception e) {
            log.error("查询最近日志失败", e);
            return ResponseEntity.ok(J8.mapOf(
                "success", false,
                "message", "查询最近日志失败: " + e.getMessage()
            ));
        }
    }

    // 日志管理操作

    @PostMapping("/start")
    @ApiOperation("开始同步日志记录")
    public ResponseEntity<Map<String, Object>> startSyncLog(
            @ApiParam("配置ID") @RequestParam String configId,
            @ApiParam("映射ID") @RequestParam(required = false) String mappingId,
            @ApiParam("任务ID") @RequestParam String taskId,
            @ApiParam("同步类型") @RequestParam String syncType,
            @ApiParam("同步方向") @RequestParam String syncDirection) {
        try {
            IntgSyncLog result = syncLogService.startSyncLog(configId, mappingId, taskId, syncType, syncDirection);
            return ResponseEntity.ok(J8.mapOf(
                "success", true,
                "message", "开始同步日志记录成功",
                "data", result
            ));
        } catch (Exception e) {
            log.error("开始同步日志记录失败", e);
            return ResponseEntity.ok(J8.mapOf(
                "success", false,
                "message", "开始同步日志记录失败: " + e.getMessage()
            ));
        }
    }

    @PutMapping("/{logId}/complete")
    @ApiOperation("完成同步日志记录")
    public ResponseEntity<Map<String, Object>> completeSyncLog(
            @ApiParam("日志ID") @PathVariable String logId,
            @ApiParam("执行结果") @RequestParam String executionResult,
            @ApiParam("处理记录数") @RequestParam(required = false) Long processedRecords,
            @ApiParam("成功记录数") @RequestParam(required = false) Long successRecords,
            @ApiParam("失败记录数") @RequestParam(required = false) Long failedRecords) {
        try {
            boolean result = syncLogService.completeSyncLog(logId, executionResult, processedRecords, successRecords, failedRecords);
            return ResponseEntity.ok(J8.mapOf(
                "success", result,
                "message", result ? "完成同步日志记录成功" : "完成同步日志记录失败"
            ));
        } catch (Exception e) {
            log.error("完成同步日志记录失败", e);
            return ResponseEntity.ok(J8.mapOf(
                "success", false,
                "message", "完成同步日志记录失败: " + e.getMessage()
            ));
        }
    }

    // TODO: 继续实现其他API接口
    // 由于接口太多，这里只实现了部分核心接口
    // 其他接口的实现可以根据具体需求逐步完善
}
