package com.management.accountant.controller;

import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetAnalysisReport;
import com.management.accountant.oracle.entity.budget.BudgetReportShare;
import com.management.accountant.oracle.entity.budget.BudgetReportSchedule;
import com.management.accountant.oracle.entity.budget.BudgetReportTemplate;
import com.management.accountant.service.BudgetAnalysisReportService;
import com.management.accountant.util.MyJsonBean;
import com.management.accountant.util.PageResult;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.*;

@RestController
@Api(tags = {"NCV65全面预算-分析报告"})
@RequestMapping(value = "/accountant/budget/analysis/report")
@Slf4j
public class BudgetAnalysisReportController {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    @Resource
    private BudgetAnalysisReportService reportService;

    @Operation(summary = "创建分析报告")
    @ApiOperation("创建分析报告")
    @PostMapping("")
    public MyJsonBean<BudgetAnalysisReport> create(@RequestBody Map<String, Object> params) {
        MyJsonBean<BudgetAnalysisReport> result = new MyJsonBean<>();
        try {
            BudgetAnalysisReport report = new BudgetAnalysisReport();
            report.setReportName((String) params.get("reportName"));
            report.setReportType((String) params.get("reportType"));
            report.setTemplateId((String) params.get("templateId"));
            report.setDescription((String) params.get("description"));

            // 处理 analysisPeriod → periodStart/periodEnd
            @SuppressWarnings("unchecked")
            java.util.List<String> analysisPeriod = (java.util.List<String>) params.get("analysisPeriod");
            if (analysisPeriod != null && analysisPeriod.size() >= 2) {
                report.setPeriodStart(analysisPeriod.get(0));
                report.setPeriodEnd(analysisPeriod.get(1));
            }

            // 处理 outputFormats 数组 → JSON 字符串
            @SuppressWarnings("unchecked")
            java.util.List<String> outputFormats = (java.util.List<String>) params.get("outputFormats");
            if (outputFormats != null && !outputFormats.isEmpty()) {
                report.setOutputFormats(com.alibaba.fastjson.JSON.toJSONString(outputFormats));
            }

            // 处理 budgetYear 从 periodStart 提取
            Object budgetYearObj = params.get("budgetYear");
            if (budgetYearObj != null) {
                report.setBudgetYear(((Number) budgetYearObj).intValue());
            }

            BudgetAnalysisReport created = reportService.create(report);
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(created);
        } catch (Exception e) {
            log.error("创建分析报告异常", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "查询分析报告详情")
    @ApiOperation("查询分析报告详情")
    @GetMapping("/{reportId}")
    public MyJsonBean<BudgetAnalysisReport> getDetail(@PathVariable String reportId) {
        MyJsonBean<BudgetAnalysisReport> result = new MyJsonBean<>();
        try {
            BudgetAnalysisReport report = reportService.getById(reportId);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(report);
        } catch (Exception e) {
            log.error("查询分析报告详情异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "更新分析报告")
    @ApiOperation("更新分析报告")
    @PutMapping("/{reportId}")
    public MyJsonBean<Void> update(@PathVariable String reportId, @RequestBody BudgetAnalysisReport report) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            report.setId(reportId);
            reportService.update(report);
            result.setCode(1);
            result.setMsg("更新成功");
        } catch (Exception e) {
            log.error("更新分析报告异常", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "删除分析报告(RESTful)")
    @ApiOperation("删除分析报告(RESTful)")
    @DeleteMapping("/{reportId}")
    public MyJsonBean<Void> deleteById(@PathVariable String reportId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            reportService.delete(reportId);
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (Exception e) {
            log.error("删除分析报告异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "分页查询分析报告列表")
    @ApiOperation("分页查询分析报告列表")
    @PostMapping("/page")
    public MyJsonBean<PageResult<BudgetAnalysisReport>> getPage(@RequestBody Map<String, Object> params) {
        MyJsonBean<PageResult<BudgetAnalysisReport>> result = new MyJsonBean<>();
        try {
            PageResult<BudgetAnalysisReport> page = reportService.getPage(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(page);
        } catch (Exception e) {
            log.error("分页查询分析报告列表异常", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "导出分析报告")
    @ApiOperation("导出分析报告")
    @GetMapping("/{reportId}/export")
    public MyJsonBean<Map<String, Object>> exportReport(@PathVariable String reportId) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> exportData = new HashMap<>();
            exportData.put("fileName", "分析报告_" + reportId + ".xlsx");
            exportData.put("id", reportId);
            result.setCode(1);
            result.setMsg("导出成功");
            result.setData(exportData);
        } catch (Exception e) {
            log.error("导出分析报告异常", e);
            result.setCode(0);
            result.setMsg("导出失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "分享分析报告")
    @ApiOperation("分享分析报告")
    @PostMapping("/{reportId}/share")
    public MyJsonBean<BudgetReportShare> shareReport(@PathVariable String reportId, @RequestBody Map<String, Object> params) {
        MyJsonBean<BudgetReportShare> result = new MyJsonBean<>();
        try {
            params.put("reportId", reportId);
            BudgetReportShare share = reportService.shareReport(params);
            result.setCode(1);
            result.setMsg("分享成功");
            result.setData(share);
        } catch (Exception e) {
            log.error("分享分析报告异常", e);
            result.setCode(0);
            result.setMsg("分享失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取报告分享记录")
    @ApiOperation("获取报告分享记录")
    @GetMapping("/{reportId}/shares")
    public MyJsonBean<java.util.List<BudgetReportShare>> getShares(@PathVariable String reportId) {
        MyJsonBean<java.util.List<BudgetReportShare>> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(reportService.getSharesByReportId(reportId));
        } catch (Exception e) {
            log.error("获取分享记录失败", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "批量导出分析报告")
    @ApiOperation("批量导出分析报告")
    @PostMapping("/batch-export")
    public MyJsonBean<Map<String, Object>> batchExport(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            @SuppressWarnings("unchecked")
            java.util.List<String> ids = (java.util.List<String>) params.get("ids");
            Map<String, Object> exportData = reportService.batchExport(ids);
            result.setCode(1);
            result.setMsg("批量导出成功");
            result.setData(exportData);
        } catch (Exception e) {
            log.error("批量导出分析报告异常", e);
            result.setCode(0);
            result.setMsg("批量导出失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "快速生成报告")
    @ApiOperation("快速生成报告")
    @PostMapping("/quick-generate/{templateId}")
    public MyJsonBean<BudgetAnalysisReport> quickGenerate(@PathVariable String templateId) {
        MyJsonBean<BudgetAnalysisReport> result = new MyJsonBean<>();
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("templateId", templateId);
            BudgetAnalysisReport report = reportService.generate(params);
            result.setCode(1);
            result.setMsg("生成成功");
            result.setData(report);
        } catch (Exception e) {
            log.error("快速生成报告异常", e);
            result.setCode(0);
            result.setMsg("生成失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "导出仪表盘")
    @ApiOperation("导出仪表盘")
    @PostMapping("/export")
    public MyJsonBean<Map<String, Object>> exportDashboard(@RequestBody Map<String, Object> params) {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> exportData = new HashMap<>();
            exportData.put("fileName", "分析仪表盘.xlsx");
            result.setCode(1);
            result.setMsg("导出成功");
            result.setData(exportData);
        } catch (Exception e) {
            log.error("导出仪表盘异常", e);
            result.setCode(0);
            result.setMsg("导出失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "重新生成报告")
    @ApiOperation("重新生成报告")
    @PostMapping("/regenerate/{id}")
    public MyJsonBean<BudgetAnalysisReport> regenerate(@PathVariable String id) {
        MyJsonBean<BudgetAnalysisReport> result = new MyJsonBean<>();
        try {
            BudgetAnalysisReport report = reportService.regenerate(id);
            result.setCode(1);
            result.setMsg("重新生成成功");
            result.setData(report);
        } catch (Exception e) {
            log.error("重新生成报告异常", e);
            result.setCode(0);
            result.setMsg("重新生成失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "复制报告")
    @ApiOperation("复制报告")
    @PostMapping("/copy/{id}")
    public MyJsonBean<BudgetAnalysisReport> copyReport(@PathVariable String id) {
        MyJsonBean<BudgetAnalysisReport> result = new MyJsonBean<>();
        try {
            BudgetAnalysisReport copied = reportService.copy(id);
            result.setCode(1);
            result.setMsg("复制成功");
            result.setData(copied);
        } catch (Exception e) {
            log.error("复制报告异常", e);
            result.setCode(0);
            result.setMsg("复制失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "删除分析报告(action)")
    @ApiOperation("删除分析报告(action)")
    @DeleteMapping("/delete/{reportId}")
    public MyJsonBean<Void> deleteByAction(@PathVariable String reportId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            reportService.delete(reportId);
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (Exception e) {
            log.error("删除分析报告异常", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取报告统计数据")
    @ApiOperation("获取报告统计数据")
    @GetMapping("/stats")
    public MyJsonBean<Map<String, Object>> getStats() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> stats = reportService.getReportStats();
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(stats);
        } catch (Exception e) {
            log.error("获取报告统计数据失败", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取报告模板列表")
    @ApiOperation("获取报告模板列表")
    @GetMapping("/templates")
    public MyJsonBean<Map<String, Object>> getTemplates() {
        MyJsonBean<Map<String, Object>> result = new MyJsonBean<>();
        try {
            Map<String, Object> templates = reportService.getReportTemplates();
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(templates);
        } catch (Exception e) {
            log.error("获取报告模板列表失败", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    // ==================== 定时生成 ====================

    @Operation(summary = "创建定时生成任务")
    @ApiOperation("创建定时生成任务")
    @PostMapping("/schedule")
    public MyJsonBean<BudgetReportSchedule> createSchedule(@RequestBody Map<String, Object> params) {
        MyJsonBean<BudgetReportSchedule> result = new MyJsonBean<>();
        try {
            BudgetReportSchedule schedule = reportService.createSchedule(params);
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(schedule);
        } catch (Exception e) {
            log.error("创建定时任务失败", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "获取报告定时任务列表")
    @ApiOperation("获取报告定时任务列表")
    @GetMapping("/{reportId}/schedules")
    public MyJsonBean<java.util.List<BudgetReportSchedule>> getSchedules(@PathVariable String reportId) {
        MyJsonBean<java.util.List<BudgetReportSchedule>> result = new MyJsonBean<>();
        try {
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(reportService.getSchedulesByReportId(reportId));
        } catch (Exception e) {
            log.error("获取定时任务列表失败", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "更新定时任务状态")
    @ApiOperation("更新定时任务状态")
    @PutMapping("/schedule/{scheduleId}/status")
    public MyJsonBean<Void> updateScheduleStatus(@PathVariable String scheduleId, @RequestBody Map<String, Object> params) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            Integer status = Integer.parseInt(String.valueOf(params.get("status")));
            reportService.updateScheduleStatus(scheduleId, status);
            result.setCode(1);
            result.setMsg("更新成功");
        } catch (Exception e) {
            log.error("更新定时任务状态失败", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "删除定时任务")
    @ApiOperation("删除定时任务")
    @DeleteMapping("/schedule/{scheduleId}")
    public MyJsonBean<Void> deleteSchedule(@PathVariable String scheduleId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            reportService.deleteSchedule(scheduleId);
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (Exception e) {
            log.error("删除定时任务失败", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }

    // ==================== 模板管理 ====================

    @Operation(summary = "分页查询模板列表")
    @ApiOperation("分页查询模板列表")
    @PostMapping("/template/page")
    public MyJsonBean<PageResult<BudgetReportTemplate>> getTemplatePage(@RequestBody Map<String, Object> params) {
        MyJsonBean<PageResult<BudgetReportTemplate>> result = new MyJsonBean<>();
        try {
            PageResult<BudgetReportTemplate> page = reportService.getTemplatePage(params);
            result.setCode(1);
            result.setMsg("查询成功");
            result.setData(page);
        } catch (Exception e) {
            log.error("分页查询模板列表失败", e);
            result.setCode(0);
            result.setMsg("查询失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "创建模板")
    @ApiOperation("创建模板")
    @PostMapping("/template")
    public MyJsonBean<BudgetReportTemplate> createTemplate(@RequestBody BudgetReportTemplate template) {
        MyJsonBean<BudgetReportTemplate> result = new MyJsonBean<>();
        try {
            BudgetReportTemplate created = reportService.createTemplate(template);
            result.setCode(1);
            result.setMsg("创建成功");
            result.setData(created);
        } catch (Exception e) {
            log.error("创建模板失败", e);
            result.setCode(0);
            result.setMsg("创建失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "更新模板")
    @ApiOperation("更新模板")
    @PutMapping("/template/{templateId}")
    public MyJsonBean<Void> updateTemplate(@PathVariable String templateId, @RequestBody BudgetReportTemplate template) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            template.setId(templateId);
            reportService.updateTemplate(template);
            result.setCode(1);
            result.setMsg("更新成功");
        } catch (Exception e) {
            log.error("更新模板失败", e);
            result.setCode(0);
            result.setMsg("更新失败：" + e.getMessage());
        }
        return result;
    }

    @Operation(summary = "删除模板")
    @ApiOperation("删除模板")
    @DeleteMapping("/template/{templateId}")
    public MyJsonBean<Void> deleteTemplate(@PathVariable String templateId) {
        MyJsonBean<Void> result = new MyJsonBean<>();
        try {
            reportService.deleteTemplate(templateId);
            result.setCode(1);
            result.setMsg("删除成功");
        } catch (Exception e) {
            log.error("删除模板失败", e);
            result.setCode(0);
            result.setMsg("删除失败：" + e.getMessage());
        }
        return result;
    }
}
