package com.global.treasurer.controller;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblRegulatoryReport;
import com.global.treasurer.service.RegulatoryReportService;
import com.global.treasurer.dto.export.ExportRegulatoryReportDTO;
import com.global.treasurer.util.excel.ExcelExport;
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
import java.util.stream.Collectors;

/**
 * 监管报告Controller
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
@Controller
@RequestMapping({"/regulatory/report", "/globalTreasurer/regulatory/report"})
@Api(tags = "监管报告管理")
public class RegulatoryReportController {
    private static final Logger log = LoggerFactory.getLogger(RegulatoryReportController.class);

    @Resource
    private RegulatoryReportService reportService;

    @Resource
    private UserProvider userProvider;

    @GetMapping("/list")
    @ResponseBody
    @ApiOperation("分页查询监管报告列表")
    public String getReportList(@RequestParam(required = false) String reportName,
                                @RequestParam(required = false) String reportStatus,
                                @RequestParam(required = false) String authorityId,
                                @RequestParam(required = false) String reportPeriod,
                                @RequestParam(required = false) String reportPeriodStart,
                                @RequestParam(required = false) String reportPeriodEnd,
                                @RequestParam(required = false, defaultValue = "1") Integer current,
                                @RequestParam(required = false, defaultValue = "10") Integer size,
                                @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                @RequestParam(required = false, defaultValue = "10") Integer pageSize,
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

            // 兼容两种分页参数：current/size 和 pageNum/pageSize
            int actualPageNum = current != null && current > 0 ? current : pageNum;
            int actualPageSize = size != null && size > 0 ? size : pageSize;

            Map<String, Object> params = new HashMap<>();
            params.put("reportName", reportName);
            params.put("reportStatus", reportStatus);
            params.put("authorityId", authorityId);
            params.put("reportPeriod", reportPeriod);
            params.put("reportPeriodStart", reportPeriodStart);
            params.put("reportPeriodEnd", reportPeriodEnd);
            params.put("pageNum", actualPageNum);
            params.put("pageSize", actualPageSize);

            PageInfo<TblRegulatoryReport> pageInfo = reportService.getReportList(params);
            Map<String, Object> pageData = new HashMap<>();
            pageData.put("rows", pageInfo.getList());
            pageData.put("total", pageInfo.getTotal());
            return JsonBean.success(pageData);
        } catch (Exception e) {
            log.error("查询监管报告列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/{reportId}")
    @ResponseBody
    @ApiOperation("根据ID获取监管报告详情")
    public String getReportById(@PathVariable String reportId,
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

            TblRegulatoryReport report = reportService.getReportById(reportId);
            return new JsonBean(1, "成功", report).toJson();
        } catch (Exception e) {
            log.error("获取监管报告详情失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("")
    @ResponseBody
    @ApiOperation("新增监管报告")
    public String addReport(@FlexibleRequestBody TblRegulatoryReport report,
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

            report.setReportId(null);
            TblRegulatoryReport saved = reportService.saveReport(report);
            return new JsonBean(1, "新增成功", saved).toJson();
        } catch (Exception e) {
            log.error("新增监管报告失败", e);
            return new JsonBean(0, "新增失败: " + e.getMessage(), null).toJson();
        }
    }

    @PutMapping("")
    @ResponseBody
    @ApiOperation("修改监管报告")
    public String updateReport(@FlexibleRequestBody TblRegulatoryReport report,
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

            if (report.getReportId() == null || report.getReportId().isEmpty()) {
                return new JsonBean(0, "报告ID不能为空", null).toJson();
            }

            TblRegulatoryReport saved = reportService.saveReport(report);
            return new JsonBean(1, "修改成功", saved).toJson();
        } catch (Exception e) {
            log.error("修改监管报告失败", e);
            return new JsonBean(0, "修改失败: " + e.getMessage(), null).toJson();
        }
    }

    @DeleteMapping("/{reportIds}")
    @ResponseBody
    @ApiOperation("删除监管报告")
    public String deleteReport(@PathVariable String reportIds,
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

            String[] ids = reportIds.split(",");
            if (ids.length == 1) {
                reportService.deleteReport(ids[0]);
            } else {
                reportService.batchDeleteReports(Arrays.asList(ids));
            }
            return new JsonBean(1, "删除成功", null).toJson();
        } catch (Exception e) {
            log.error("删除监管报告失败", e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/overdue")
    @ResponseBody
    @ApiOperation("查询逾期报告")
    public String getOverdueReports(@RequestHeader(value = "token", required = false) String token,
                                    HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            List<TblRegulatoryReport> reports = reportService.getOverdueReports();
            return new JsonBean(1, "成功", reports).toJson();
        } catch (Exception e) {
            log.error("查询逾期报告失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/due-soon/{days}")
    @ResponseBody
    @ApiOperation("查询即将到期的报告")
    public String getDueSoonReports(@PathVariable Integer days,
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

            List<TblRegulatoryReport> reports = reportService.getDueSoonReports(days);
            return new JsonBean(1, "成功", reports).toJson();
        } catch (Exception e) {
            log.error("查询即将到期报告失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/attention")
    @ResponseBody
    @ApiOperation("查询需要关注的报告")
    public String getReportsNeedingAttention(@RequestHeader(value = "token", required = false) String token,
                                             HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            List<TblRegulatoryReport> reports = reportService.getReportsNeedingAttention();
            return new JsonBean(1, "成功", reports).toJson();
        } catch (Exception e) {
            log.error("查询需要关注的报告失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/{reportId}/generate")
    @ResponseBody
    @ApiOperation("生成报告")
    public String generateReport(@PathVariable String reportId,
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

            TblRegulatoryReport report = reportService.generateReport(reportId);
            return new JsonBean(1, "生成成功", report).toJson();
        } catch (Exception e) {
            log.error("生成报告失败", e);
            return new JsonBean(0, "生成失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/{reportId}/validate")
    @ResponseBody
    @ApiOperation("验证报告")
    public String validateReport(@PathVariable String reportId,
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

            TblRegulatoryReport report = reportService.validateReport(reportId);
            return new JsonBean(1, "验证成功", report).toJson();
        } catch (Exception e) {
            log.error("验证报告失败", e);
            return new JsonBean(0, "验证失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/{reportId}/submit")
    @ResponseBody
    @ApiOperation("提交报告")
    public String submitReport(@PathVariable String reportId,
                               @RequestParam(required = false) String submissionMethod,
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

            TblRegulatoryReport report = reportService.submitReport(reportId, submissionMethod);
            return new JsonBean(1, "提交成功", report).toJson();
        } catch (Exception e) {
            log.error("提交报告失败", e);
            return new JsonBean(0, "提交失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/{reportId}/accept")
    @ResponseBody
    @ApiOperation("接受报告")
    public String acceptReport(@PathVariable String reportId,
                               @RequestParam(required = false) String acknowledgmentNo,
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

            TblRegulatoryReport report = reportService.acceptReport(reportId, acknowledgmentNo);
            return new JsonBean(1, "接受成功", report).toJson();
        } catch (Exception e) {
            log.error("接受报告失败", e);
            return new JsonBean(0, "接受失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/{reportId}/reject")
    @ResponseBody
    @ApiOperation("拒绝报告")
    public String rejectReport(@PathVariable String reportId,
                               @RequestParam(required = false) String rejectReason,
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

            TblRegulatoryReport report = reportService.rejectReport(reportId, rejectReason);
            return new JsonBean(1, "拒绝成功", report).toJson();
        } catch (Exception e) {
            log.error("拒绝报告失败", e);
            return new JsonBean(0, "拒绝失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/statistics")
    @ResponseBody
    @ApiOperation("获取报告统计信息")
    public String getReportStatistics(@RequestParam(required = false) String companyId,
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

            Map<String, Object> statistics = reportService.getReportStatistics(companyId);
            return new JsonBean(1, "成功", statistics).toJson();
        } catch (Exception e) {
            log.error("获取报告统计信息失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/batch-generate")
    @ResponseBody
    @ApiOperation("批量生成报告")
    public String batchGenerateReports(@RequestBody(required = false) List<String> reportIds,
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

            reportService.batchGenerateReports(reportIds);
            return new JsonBean(1, "批量生成成功", null).toJson();
        } catch (Exception e) {
            log.error("批量生成报告失败", e);
            return new JsonBean(0, "批量生成失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/batch-submit")
    @ResponseBody
    @ApiOperation("批量提交报告")
    public String batchSubmitReports(@RequestBody(required = false) List<String> reportIds,
                                     @RequestParam(required = false) String submissionMethod,
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

            reportService.batchSubmitReports(reportIds, submissionMethod);
            return new JsonBean(1, "批量提交成功", null).toJson();
        } catch (Exception e) {
            log.error("批量提交报告失败", e);
            return new JsonBean(0, "批量提交失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/{sourceReportId}/copy")
    @ResponseBody
    @ApiOperation("复制报告")
    public String copyReport(@PathVariable String sourceReportId,
                             @RequestParam String newReportNo,
                             @RequestParam String newReportName,
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

            TblRegulatoryReport report = reportService.copyReport(sourceReportId, newReportNo, newReportName);
            return new JsonBean(1, "复制成功", report).toJson();
        } catch (Exception e) {
            log.error("复制报告失败", e);
            return new JsonBean(0, "复制失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/export")
    @ApiOperation("导出监管报告列表")
    public void exportReportList(@RequestParam(required = false) String reportName,
                                    @RequestParam(required = false) String reportStatus,
                                    @RequestParam(required = false) String authorityId,
                                    @RequestHeader(value = "token", required = false) String token,
                                    HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(new JsonBean(401, "用户已失效", null).toJson());
                return;
            }

            Map<String, Object> params = new HashMap<>();
            params.put("reportName", reportName);
            params.put("reportStatus", reportStatus);
            params.put("authorityId", authorityId);

            List<TblRegulatoryReport> list = reportService.exportReportList(params);
            List<ExportRegulatoryReportDTO> exportList = list.stream()
                    .map(ExportRegulatoryReportDTO::fromEntity)
                    .collect(Collectors.toList());

            String filename = "监管报告数据_" + System.currentTimeMillis() + ".xlsx";
            try (ExcelExport export = new ExcelExport("监管报告数据", ExportRegulatoryReportDTO.class)) {
                export.setDataList(exportList).write(response, filename);
            }
        } catch (Exception e) {
            log.error("导出监管报告列表失败", e);
            try {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(new JsonBean(0, "导出失败: " + e.getMessage(), null).toJson());
            } catch (Exception ex) {
                log.error("写入错误响应失败", ex);
            }
        }
    }

    @PostMapping("/{reportId}/recall")
    @ResponseBody
    @ApiOperation("撤回报告")
    public String recallReport(@PathVariable String reportId,
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

            TblRegulatoryReport report = reportService.recallReport(reportId);
            return new JsonBean(1, "撤回成功", report).toJson();
        } catch (Exception e) {
            log.error("撤回报告失败", e);
            return new JsonBean(0, "撤回失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/{reportId}/history")
    @ResponseBody
    @ApiOperation("获取报告操作历史")
    public String getReportHistory(@PathVariable String reportId,
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

            List<Map<String, Object>> history = reportService.getReportHistory(reportId);
            return new JsonBean(1, "成功", history).toJson();
        } catch (Exception e) {
            log.error("获取报告操作历史失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }
}
