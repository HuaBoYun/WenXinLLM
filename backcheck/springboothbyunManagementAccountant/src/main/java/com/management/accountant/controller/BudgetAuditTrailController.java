package com.management.accountant.controller;

import com.management.accountant.oracle.entity.budget.BudgetAuditAlert;
import com.management.accountant.oracle.entity.budget.BudgetAuditSetting;
import com.management.accountant.oracle.entity.budget.BudgetAuditTrail;
import com.management.accountant.service.BudgetAuditAlertService;
import com.management.accountant.service.BudgetAuditSettingService;
import com.management.accountant.service.BudgetAuditTrailService;
import com.management.accountant.util.MyJsonBean;
import com.management.accountant.util.PageResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@Api(tags = {"NCV65全面预算-审计追踪"})
@RequestMapping(value = "/accountant/budget/audit-trail")
@Slf4j
public class BudgetAuditTrailController {

    @Resource
    private BudgetAuditTrailService auditTrailService;

    @Resource
    private BudgetAuditSettingService auditSettingService;

    @Resource
    private BudgetAuditAlertService auditAlertService;

    // ==================== 审计日志 ====================

    @Operation(summary = "获取审计日志列表")
    @ApiOperation("获取审计日志列表")
    @PostMapping("/list")
    public MyJsonBean<PageResult<BudgetAuditTrail>> getAuditList(@RequestBody Map<String, Object> params) {
        MyJsonBean<PageResult<BudgetAuditTrail>> result = new MyJsonBean<>();
        try {
            PageResult<BudgetAuditTrail> pageResult = auditTrailService.getPage(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(pageResult);
        } catch (Exception e) {
            log.error("获取审计日志列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取审计统计数据")
    @ApiOperation("获取审计统计数据")
    @GetMapping("/stats")
    public MyJsonBean<Map<String, Object>> getAuditStats() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> stats = auditTrailService.getStats();
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(stats);
        } catch (Exception e) {
            log.error("获取审计统计数据异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取审计详情")
    @ApiOperation("获取审计详情")
    @GetMapping("/{id}/detail")
    public MyJsonBean<BudgetAuditTrail> getAuditDetail(
            @ApiParam(value = "审计日志ID", required = true) @PathVariable String id) {
        MyJsonBean<BudgetAuditTrail> result = new MyJsonBean<>();
        try {
            BudgetAuditTrail audit = auditTrailService.getById(id);
            if (audit != null) {
                result.setCode(1);
                result.setMsg("查询成功");
                result.setData(audit);
            } else {
                result.setCode(0);
                result.setMsg("审计日志不存在");
            }
        } catch (Exception e) {
            log.error("获取审计详情异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "导出审计日志")
    @ApiOperation("导出审计日志")
    @PostMapping("/export")
    public void exportAuditLogs(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        try {
            List<BudgetAuditTrail> list = auditTrailService.exportData(params);
            response.setContentType("text/csv;charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=audit_logs.csv");
            StringBuilder sb = new StringBuilder();
            sb.append("审计ID,审计类型,模块名称,操作类型,目标表,目标ID,目标名称,操作人ID,操作人,操作时间,IP地址,风险等级,结果,执行时长(ms),状态,备注\n");
            for (BudgetAuditTrail item : list) {
                sb.append(csvField(item.getAuditId())).append(",")
                  .append(csvField(item.getAuditType())).append(",")
                  .append(csvField(item.getModuleName())).append(",")
                  .append(csvField(item.getOperationType())).append(",")
                  .append(csvField(item.getTargetTable())).append(",")
                  .append(csvField(item.getTargetId())).append(",")
                  .append(csvField(item.getTargetName())).append(",")
                  .append(csvField(item.getOperatorId())).append(",")
                  .append(csvField(item.getOperatorName())).append(",")
                  .append(item.getOperationTime() != null ? item.getOperationTime() : "").append(",")
                  .append(csvField(item.getIpAddress())).append(",")
                  .append(csvField(item.getRiskLevel())).append(",")
                  .append(csvField(item.getResult())).append(",")
                  .append(item.getExecutionTime() != null ? item.getExecutionTime() : "").append(",")
                  .append(csvField(item.getStatus())).append(",")
                  .append(csvField(item.getRemark())).append("\n");
            }
            byte[] bom = new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF};
            response.getOutputStream().write(bom);
            response.getOutputStream().write(sb.toString().getBytes("UTF-8"));
            response.getOutputStream().flush();
        } catch (Exception e) {
            log.error("导出审计日志异常", e);
        }
    }

    @Operation(summary = "清理历史审计日志")
    @ApiOperation("清理历史审计日志")
    @PostMapping("/cleanup")
    public MyJsonBean<Map<String, Object>> cleanupLogs(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            int retentionDays = 90;
            if (params.containsKey("retentionDays")) {
                retentionDays = Integer.parseInt(params.get("retentionDays").toString());
            }
            int count = auditTrailService.cleanupLogs(retentionDays);
            Map<String, Object> data = new HashMap<>();
            data.put("cleanedCount", count);
            data.put("retentionDays", retentionDays);
            result.setCode(1);
            result.setMsg("清理成功，共清理" + count + "条记录");
            result.setData(data);
        } catch (Exception e) {
            log.error("清理审计日志异常", e);
            result.setCode(0);
            result.setMsg("清理失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取日志分析统计")
    @ApiOperation("获取日志分析统计")
    @GetMapping("/analysis")
    public MyJsonBean<Map<String, Object>> getLogAnalysis() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> analysis = auditTrailService.getLogAnalysis(new HashMap<>());
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(analysis);
        } catch (Exception e) {
            log.error("获取日志分析统计异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    // ==================== 审计设置 ====================

    @Operation(summary = "获取所有审计设置")
    @ApiOperation("获取所有审计设置")
    @GetMapping("/settings")
    public MyJsonBean<List<BudgetAuditSetting>> getSettings() {
        MyJsonBean<List<BudgetAuditSetting>> result = new MyJsonBean<>();
        try {
            List<BudgetAuditSetting> settings = auditSettingService.getAll();
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(settings);
        } catch (Exception e) {
            log.error("获取审计设置异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "保存审计设置")
    @ApiOperation("保存审计设置")
    @PostMapping("/settings/save")
    public MyJsonBean<String> saveSetting(@RequestBody BudgetAuditSetting setting) {
        MyJsonBean<String> result = new MyJsonBean<>();
        try {
            auditSettingService.saveOrUpdate(setting);
            result.setCode(1);
            result.setMsg("保存成功");
        } catch (Exception e) {
            log.error("保存审计设置异常", e);
            result.setCode(0);
            result.setMsg("保存失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "删除审计设置")
    @ApiOperation("删除审计设置")
    @DeleteMapping("/settings/{settingId}")
    public MyJsonBean<String> deleteSetting(
            @ApiParam(value = "设置ID", required = true) @PathVariable String settingId) {
        MyJsonBean<String> result = new MyJsonBean<>();
        try {
            auditSettingService.deleteById(settingId);
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (Exception e) {
            log.error("删除审计设置异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    // ==================== 审计告警 ====================

    @Operation(summary = "获取告警列表")
    @ApiOperation("获取告警列表")
    @PostMapping("/alerts/list")
    public MyJsonBean<PageResult<BudgetAuditAlert>> getAlertList(@RequestBody Map<String, Object> params) {
        MyJsonBean<PageResult<BudgetAuditAlert>> result = new MyJsonBean<>();
        try {
            PageResult<BudgetAuditAlert> pageResult = auditAlertService.getPage(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(pageResult);
        } catch (Exception e) {
            log.error("获取告警列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取告警统计")
    @ApiOperation("获取告警统计")
    @GetMapping("/alerts/stats")
    public MyJsonBean<Map<String, Object>> getAlertStats() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> stats = auditAlertService.getAlertStats();
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(stats);
        } catch (Exception e) {
            log.error("获取告警统计异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取待处理告警")
    @ApiOperation("获取待处理告警")
    @GetMapping("/alerts/pending")
    public MyJsonBean<List<BudgetAuditAlert>> getPendingAlerts() {
        MyJsonBean<List<BudgetAuditAlert>> result = new MyJsonBean<>();
        try {
            List<BudgetAuditAlert> alerts = auditAlertService.getPendingAlerts();
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(alerts);
        } catch (Exception e) {
            log.error("获取待处理告警异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "处理告警")
    @ApiOperation("处理告警")
    @PostMapping("/alerts/{alertId}/handle")
    public MyJsonBean<String> handleAlert(
            @ApiParam(value = "告警ID", required = true) @PathVariable String alertId,
            @RequestBody Map<String, Object> params) {
        MyJsonBean<String> result = new MyJsonBean<>();
        try {
            String handlerId = params.get("handlerId") != null ? params.get("handlerId").toString() : "";
            String handlerName = params.get("handlerName") != null ? params.get("handlerName").toString() : "";
            String handleRemark = params.get("handleRemark") != null ? params.get("handleRemark").toString() : "";
            String alertStatus = params.get("alertStatus") != null ? params.get("alertStatus").toString() : "RESOLVED";
            auditAlertService.handleAlert(alertId, handlerId, handlerName, handleRemark, alertStatus);
            result.setCode(1);
            result.setMsg("处理成功");
        } catch (Exception e) {
            log.error("处理告警异常, alertId={}", alertId, e);
            result.setCode(0);
            result.setMsg("处理失败：" + e.getMessage());
        }
        return result;
    }

    // ==================== 工具方法 ====================

    private String csvField(String value) {
        if (value == null) {
            return "";
        }
        if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
            return "\"" + value.replace("\"", "\"\"") + "\"";
        }
        return value;
    }
}