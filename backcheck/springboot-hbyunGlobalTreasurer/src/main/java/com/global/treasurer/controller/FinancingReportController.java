package com.global.treasurer.controller;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.FinancingReportDTO;
import com.global.treasurer.dto.FinancingReportQueryDTO;
import com.global.treasurer.dto.ReportTemplateDTO;
import com.global.treasurer.dto.ReportTemplateQueryDTO;
import com.global.treasurer.entity.TblReportRecord;
import com.global.treasurer.entity.TblReportTemplate;
import com.global.treasurer.service.FinancingReportManagementService;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 融资报表管理Controller
 *
 * @author 华博云开发团队
 * @since 2025-01-13
 */
@Controller
@RequestMapping({"/rzgl/financing-report", "/financial/rzgl/financing-report", "/centralaudit/rzgl/financing-report",
                "/rzgl/report-management", "/financial/rzgl/report-management", "/centralaudit/rzgl/report-management",
                })
@Api(tags = "融资报表管理")
public class FinancingReportController {
    private static final Logger log = LoggerFactory.getLogger(FinancingReportController.class);

    @Resource
    private FinancingReportManagementService financingReportManagementService;

    @Resource
    private UserProvider userProvider;

    @PostMapping("/list")
    @ResponseBody
    @ApiOperation("分页查询报表列表")
    public String getReportList(@FlexibleRequestBody FinancingReportQueryDTO queryDTO,
                                @RequestHeader(value = "token", required = false) String token,
                                HttpServletResponse response) {
        try {
            log.info("查询报表列表参数: reportName={}, reportType={}, generationStatus={}",
                queryDTO.getReportName(), queryDTO.getReportType(), queryDTO.getGenerationStatus());
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            PageInfo<TblReportRecord> pageInfo = financingReportManagementService.getReportList(queryDTO);
            // 构建前端期望的响应格式 {rows: [...], total: xxx}
            Map<String, Object> pageData = new HashMap<>();
            pageData.put("rows", pageInfo.getList());
            pageData.put("total", pageInfo.getTotal());
            return JsonBean.success(pageData);
        } catch (Exception e) {
            log.error("查询报表列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/records")
    @ResponseBody
    @ApiOperation("分页查询报表记录(前端专用)")
    public String getReportRecords(@FlexibleRequestBody FinancingReportQueryDTO queryDTO,
                                  @RequestHeader(value = "token", required = false) String token,
                                  HttpServletResponse response) {
        try {
            log.info("查询报表记录参数: reportName={}, reportType={}, generationStatus={}",
                queryDTO.getReportName(), queryDTO.getReportType(), queryDTO.getGenerationStatus());
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            PageInfo<TblReportRecord> pageInfo = financingReportManagementService.getReportList(queryDTO);
            // 构建前端期望的响应格式 {rows: [...], total: xxx}
            Map<String, Object> pageData = new HashMap<>();
            pageData.put("rows", pageInfo.getList());
            pageData.put("total", pageInfo.getTotal());
            return JsonBean.success(pageData);
        } catch (Exception e) {
            log.error("查询报表记录失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/detail/{id}")
    @ResponseBody
    @ApiOperation("报表详情")
    public String getReportById(@PathVariable Long id,
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

            TblReportRecord record = financingReportManagementService.getReportById(id);
            return new JsonBean(1, "成功", record).toJson();
        } catch (Exception e) {
            log.error("获取报表详情失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/generate")
    @ResponseBody
    @ApiOperation("生成报表")
    public String generateReport(@FlexibleRequestBody FinancingReportDTO dto,
                                @RequestHeader(value = "token", required = false) String token,
                                HttpServletResponse response) {
        try {
            log.info("生成报表参数: reportName={}, reportType={}, periodType={}",
                dto.getReportName(), dto.getReportType(), dto.getPeriodType());
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            // 设置生成人信息
            dto.setGeneratedBy(loginStaff.getStaffid().longValue());
            dto.setGeneratedByName(loginStaff.getRealname());

            TblReportRecord record = financingReportManagementService.generateReport(dto);
            return new JsonBean(1, "生成成功", record).toJson();
        } catch (Exception e) {
            log.error("生成报表失败", e);
            return new JsonBean(0, "生成失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/export")
    @ResponseBody
    @ApiOperation("导出报表")
    public String exportReport(@RequestParam Long recordId,
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

            String filePath = financingReportManagementService.exportReport(recordId);
            Map<String, String> result = new HashMap<>();
            result.put("filePath", filePath);
            return new JsonBean(1, "导出成功", result).toJson();
        } catch (Exception e) {
            log.error("导出报表失败", e);
            return new JsonBean(0, "导出失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/records/export")
    @ResponseBody
    @ApiOperation("批量导出报表记录")
    public void exportReportRecords(FinancingReportQueryDTO queryDTO,
                                   @RequestHeader(value = "token", required = false) String token,
                                   HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(new JsonBean(401, "用户已失效", null).toJson());
                return;
            }

            // 查询报表记录列表
            if (queryDTO == null) {
                queryDTO = new FinancingReportQueryDTO();
            }
            queryDTO.setPageNum(1);
            queryDTO.setPageSize(10000); // 导出最多10000条
            PageInfo<TblReportRecord> pageInfo = financingReportManagementService.getReportList(queryDTO);
            List<TblReportRecord> records = pageInfo.getList();

            // 设置响应头
            String fileName = "报表记录_" + new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".xlsx";
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(fileName, "UTF-8"));

            // 使用 POI 生成 Excel
            org.apache.poi.xssf.usermodel.XSSFWorkbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
            org.apache.poi.xssf.usermodel.XSSFSheet sheet = workbook.createSheet("报表记录");

            // 创建标题样式
            org.apache.poi.xssf.usermodel.XSSFCellStyle headerStyle = workbook.createCellStyle();
            org.apache.poi.xssf.usermodel.XSSFFont headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
            headerStyle.setFillForegroundColor(org.apache.poi.ss.usermodel.IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND);

            // 创建标题行
            org.apache.poi.xssf.usermodel.XSSFRow headerRow = sheet.createRow(0);
            String[] headers = {"序号", "报表名称", "报表类型", "周期类型", "生成状态", "生成时间", "生成人", "文件大小", "下载次数"};
            for (int i = 0; i < headers.length; i++) {
                org.apache.poi.xssf.usermodel.XSSFCell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
                sheet.setColumnWidth(i, i == 1 ? 8000 : 4000);
            }

            // 创建数据行
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (int i = 0; i < records.size(); i++) {
                TblReportRecord record = records.get(i);
                org.apache.poi.xssf.usermodel.XSSFRow dataRow = sheet.createRow(i + 1);
                dataRow.createCell(0).setCellValue(i + 1);
                dataRow.createCell(1).setCellValue(record.getReportName() != null ? record.getReportName() : "");
                dataRow.createCell(2).setCellValue(getReportTypeName(record.getReportType()));
                dataRow.createCell(3).setCellValue(getPeriodTypeName(record.getPeriodType()));
                dataRow.createCell(4).setCellValue(getStatusName(record.getGenerationStatus()));
                dataRow.createCell(5).setCellValue(record.getGenerationTime() != null ? sdf.format(record.getGenerationTime()) : "");
                dataRow.createCell(6).setCellValue(record.getGeneratedByName() != null ? record.getGeneratedByName() : "");
                dataRow.createCell(7).setCellValue(formatFileSize(record.getFileSize()));
                dataRow.createCell(8).setCellValue(record.getDownloadCount() != null ? record.getDownloadCount() : 0);
            }

            // 写入响应流
            workbook.write(response.getOutputStream());
            workbook.close();
            response.getOutputStream().flush();

            log.info("导出报表记录成功，共 {} 条", records.size());
        } catch (Exception e) {
            log.error("导出报表记录失败", e);
            try {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(new JsonBean(0, "导出失败: " + e.getMessage(), null).toJson());
            } catch (Exception ex) {
                log.error("写入错误响应失败", ex);
            }
        }
    }

    /** 获取报表类型名称 */
    private String getReportTypeName(String type) {
        if (type == null) return "-";
        Map<String, String> typeMap = new HashMap<>();
        typeMap.put("BALANCE", "余额报表");
        typeMap.put("COST", "成本报表");
        typeMap.put("RISK", "风险报表");
        typeMap.put("TREND", "趋势报表");
        typeMap.put("EFFICIENCY", "效率报表");
        typeMap.put("FORECAST", "预测报表");
        return typeMap.getOrDefault(type, type);
    }

    /** 获取周期类型名称 */
    private String getPeriodTypeName(String type) {
        if (type == null) return "-";
        Map<String, String> typeMap = new HashMap<>();
        typeMap.put("DAILY", "日报");
        typeMap.put("WEEKLY", "周报");
        typeMap.put("MONTHLY", "月报");
        typeMap.put("QUARTERLY", "季报");
        typeMap.put("ANNUAL", "年报");
        typeMap.put("YEARLY", "年报");
        typeMap.put("CUSTOM", "自定义");
        return typeMap.getOrDefault(type, type);
    }

    /** 获取状态名称 */
    private String getStatusName(String status) {
        if (status == null) return "-";
        Map<String, String> statusMap = new HashMap<>();
        statusMap.put("PENDING", "待生成");
        statusMap.put("GENERATING", "生成中");
        statusMap.put("COMPLETED", "已完成");
        statusMap.put("FAILED", "失败");
        statusMap.put("CANCELLED", "已取消");
        return statusMap.getOrDefault(status, status);
    }

    /** 格式化文件大小 */
    private String formatFileSize(Long bytes) {
        if (bytes == null || bytes == 0) return "0 B";
        String[] units = {"B", "KB", "MB", "GB"};
        int unitIndex = 0;
        double size = bytes;
        while (size >= 1024 && unitIndex < units.length - 1) {
            size /= 1024;
            unitIndex++;
        }
        return String.format("%.2f %s", size, units[unitIndex]);
    }

    @GetMapping("/templates")
    @ResponseBody
    @ApiOperation("报表模板列表")
    public String getReportTemplates(@RequestHeader(value = "token", required = false) String token,
                                     HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            List<Map<String, Object>> templates = financingReportManagementService.getReportTemplates();
            return new JsonBean(1, "成功", templates).toJson();
        } catch (Exception e) {
            log.error("获取报表模板列表失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/schedule")
    @ResponseBody
    @ApiOperation("定时生成报表")
    public String scheduleGenerateReport(FinancingReportDTO dto,
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

            // 设置生成人信息
            dto.setGeneratedBy(loginStaff.getStaffid().longValue());
            dto.setGeneratedByName(loginStaff.getRealname());

            TblReportRecord record = financingReportManagementService.scheduleGenerateReport(dto);
            return new JsonBean(1, "定时任务创建成功", record).toJson();
        } catch (Exception e) {
            log.error("定时生成报表失败", e);
            return new JsonBean(0, "定时任务创建失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/update")
    @ResponseBody
    @ApiOperation("更新报表记录")
    public String updateReport(@FlexibleRequestBody FinancingReportDTO dto,
                              @RequestHeader(value = "token", required = false) String token,
                              HttpServletResponse response) {
        try {
            log.info("更新报表参数: recordId={}, reportName={}, reportType={}",
                dto.getRecordId(), dto.getReportName(), dto.getReportType());
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            if (dto.getRecordId() == null) {
                return new JsonBean(0, "记录ID不能为空", null).toJson();
            }

            TblReportRecord record = financingReportManagementService.updateReport(dto);
            return new JsonBean(1, "更新成功", record).toJson();
        } catch (Exception e) {
            log.error("更新报表记录失败", e);
            return new JsonBean(0, "更新失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/regenerate/{id}")
    @ResponseBody
    @ApiOperation("重新生成报表")
    public String regenerateReport(@PathVariable Long id,
                                  @RequestHeader(value = "token", required = false) String token,
                                  HttpServletResponse response) {
        try {
            log.info("重新生成报表: recordId={}", id);
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            TblReportRecord record = financingReportManagementService.regenerateReport(id, loginStaff.getStaffid().longValue(), loginStaff.getRealname());
            return new JsonBean(1, "重新生成成功", record).toJson();
        } catch (Exception e) {
            log.error("重新生成报表失败", e);
            return new JsonBean(0, "重新生成失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/cancel/{id}")
    @ResponseBody
    @ApiOperation("取消报表生成")
    public String cancelReport(@PathVariable Long id,
                              @RequestHeader(value = "token", required = false) String token,
                              HttpServletResponse response) {
        try {
            log.info("取消报表生成: recordId={}", id);
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            financingReportManagementService.cancelReport(id);
            return new JsonBean(1, "取消成功", null).toJson();
        } catch (Exception e) {
            log.error("取消报表生成失败", e);
            return new JsonBean(0, "取消失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/download/{id}")
    @ResponseBody
    @ApiOperation("下载报表")
    public void downloadReport(@PathVariable Long id,
                              @RequestHeader(value = "token", required = false) String token,
                              HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(new JsonBean(401, "用户已失效", null).toJson());
                return;
            }

            financingReportManagementService.downloadReport(id, response);
        } catch (Exception e) {
            log.error("下载报表失败", e);
            try {
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(new JsonBean(0, "下载失败: " + e.getMessage(), null).toJson());
            } catch (Exception ex) {
                log.error("写入响应失败", ex);
            }
        }
    }

    @DeleteMapping("/delete/{id}")
    @ResponseBody
    @ApiOperation("删除报表记录")
    public String deleteReport(@PathVariable Long id,
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

            financingReportManagementService.deleteReport(id);
            return new JsonBean(1, "删除成功", null).toJson();
        } catch (Exception e) {
            log.error("删除报表记录失败", e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/batchDelete")
    @ResponseBody
    @ApiOperation("批量删除报表记录")
    public String batchDeleteReports(@RequestParam(value = "recordIds", required = false) List<Long> recordIds,
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

            financingReportManagementService.batchDeleteReports(recordIds);
            return new JsonBean(1, "批量删除成功", null).toJson();
        } catch (Exception e) {
            log.error("批量删除报表记录失败", e);
            return new JsonBean(0, "批量删除失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/overview")
    @ResponseBody
    @ApiOperation("获取报表概览信息(前端专用)")
    public String getReportOverview(@RequestParam(required = false) Map<String, Object> params,
                                   @RequestHeader(value = "token", required = false) String token,
                                   HttpServletResponse response) {
        try {
            log.info("获取报表概览信息, 参数: {}", params);
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            Long companyId = params != null && params.get("companyId") != null
                ? Long.valueOf(params.get("companyId").toString()) : null;
            Map<String, Object> statistics = financingReportManagementService.getReportStatistics(companyId);

            // 转换为前端期望的字段名
            Map<String, Object> overview = new HashMap<>();
            overview.put("totalReports", statistics.getOrDefault("totalCount", 0));
            overview.put("completedReports", statistics.getOrDefault("completedCount", 0));
            overview.put("generatingReports", statistics.getOrDefault("generatingCount", 0));
            overview.put("failedReports", statistics.getOrDefault("failedCount", 0));
            overview.put("totalDownloads", statistics.getOrDefault("totalDownloads", 0));
            overview.put("totalSize", statistics.getOrDefault("totalSize", 0));
            overview.put("avgGenerationTime", 45); // 模拟平均生成时间
            overview.put("monthlyIncrease", 12); // 模拟月增长率
            overview.put("successRate", calculateSuccessRate(statistics));

            return new JsonBean(1, "成功", overview).toJson();
        } catch (Exception e) {
            log.error("获取报表概览信息失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    /** 计算成功率 */
    private int calculateSuccessRate(Map<String, Object> statistics) {
        int total = ((Number) statistics.getOrDefault("totalCount", 0)).intValue();
        int completed = ((Number) statistics.getOrDefault("completedCount", 0)).intValue();
        if (total == 0) return 100;
        return (int) Math.round((double) completed / total * 100);
    }

    @PostMapping("/chart-data")
    @ResponseBody
    @ApiOperation("获取报表图表数据(前端专用)")
    public String getReportChartData(@RequestParam(required = false) Map<String, Object> params,
                                   @RequestHeader(value = "token", required = false) String token,
                                   HttpServletResponse response) {
        try {
            log.info("获取报表图表数据, 参数: {}", params);
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            Map<String, Object> chartData = new HashMap<>();

            // 报表类型分布数据（饼图）- 前端期望字段名为 typeData
            List<Map<String, Object>> typeData = new ArrayList<>();
            String[] typeNames = {"余额报表", "成本报表", "期限报表", "效率报表", "风险报表"};
            int[] typeValues = {45, 38, 32, 25, 16};
            for (int i = 0; i < typeNames.length; i++) {
                Map<String, Object> item = new HashMap<>();
                item.put("name", typeNames[i]);
                item.put("value", typeValues[i]);
                typeData.add(item);
            }
            chartData.put("typeData", typeData);

            // 报表生成趋势数据（折线图）- 前端期望字段为 date, generated, downloaded
            List<Map<String, Object>> trendData = new ArrayList<>();
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MM/dd");
            java.util.Calendar cal = java.util.Calendar.getInstance();
            int[] generatedValues = {18, 25, 22, 30, 28, 35, 32};
            int[] downloadedValues = {15, 20, 18, 25, 22, 28, 26};
            for (int i = 6; i >= 0; i--) {
                cal.setTime(new Date());
                cal.add(java.util.Calendar.DAY_OF_MONTH, -i);
                Map<String, Object> item = new HashMap<>();
                item.put("date", sdf.format(cal.getTime()));
                item.put("generated", generatedValues[6 - i]);
                item.put("downloaded", downloadedValues[6 - i]);
                trendData.add(item);
            }
            chartData.put("trendData", trendData);

            return new JsonBean(1, "成功", chartData).toJson();
        } catch (Exception e) {
            log.error("获取报表图表数据失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/statistics")
    @ResponseBody
    @ApiOperation("获取报表统计信息")
    public String getReportStatistics(@RequestParam(required = false) Long companyId,
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

            Map<String, Object> statistics = financingReportManagementService.getReportStatistics(companyId);
            return new JsonBean(1, "成功", statistics).toJson();
        } catch (Exception e) {
            log.error("获取报表统计信息失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/reportTypes")
    @ResponseBody
    @ApiOperation("获取报表类型列表")
    public String getReportTypes(@RequestHeader(value = "token", required = false) String token,
                                HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            // 返回报表类型列表
            List<Map<String, String>> reportTypes = new java.util.ArrayList<>();

            Map<String, String> reportType1 = new HashMap<>();
            reportType1.put("code", "DAILY");
            reportType1.put("name", "日报表");
            reportTypes.add(reportType1);

            Map<String, String> reportType2 = new HashMap<>();
            reportType2.put("code", "WEEKLY");
            reportType2.put("name", "周报表");
            reportTypes.add(reportType2);

            Map<String, String> reportType3 = new HashMap<>();
            reportType3.put("code", "MONTHLY");
            reportType3.put("name", "月报表");
            reportTypes.add(reportType3);

            Map<String, String> reportType4 = new HashMap<>();
            reportType4.put("code", "QUARTERLY");
            reportType4.put("name", "季报表");
            reportTypes.add(reportType4);

            Map<String, String> reportType5 = new HashMap<>();
            reportType5.put("code", "ANNUAL");
            reportType5.put("name", "年报表");
            reportTypes.add(reportType5);

            Map<String, String> reportType6 = new HashMap<>();
            reportType6.put("code", "ANALYSIS");
            reportType6.put("name", "分析报表");
            reportTypes.add(reportType6);

            Map<String, String> reportType7 = new HashMap<>();
            reportType7.put("code", "STATISTICS");
            reportType7.put("name", "统计报表");
            reportTypes.add(reportType7);
            return new JsonBean(1, "成功", reportTypes).toJson();
        } catch (Exception e) {
            log.error("获取报表类型列表失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    // ==================== 模板管理接口 ====================

    @PostMapping("/templates")
    @ResponseBody
    @ApiOperation("分页查询模板列表")
    public String getTemplateList(@FlexibleRequestBody ReportTemplateQueryDTO queryDTO,
                                 @RequestHeader(value = "token", required = false) String token,
                                 HttpServletResponse response) {
        try {
            log.info("查询模板列表参数: templateName={}, templateType={}", queryDTO.getTemplateName(), queryDTO.getTemplateType());
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            PageInfo<TblReportTemplate> pageInfo = financingReportManagementService.getTemplateList(queryDTO);
            Map<String, Object> pageData = new HashMap<>();
            pageData.put("rows", pageInfo.getList());
            pageData.put("total", pageInfo.getTotal());
            return JsonBean.success(pageData);
        } catch (Exception e) {
            log.error("查询模板列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    @GetMapping("/templates/{templateId}")
    @ResponseBody
    @ApiOperation("获取模板详情")
    public String getTemplateById(@PathVariable String templateId,
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

            TblReportTemplate template = financingReportManagementService.getTemplateById(templateId);
            if (template == null) {
                return new JsonBean(0, "模板不存在", null).toJson();
            }
            return new JsonBean(1, "成功", template).toJson();
        } catch (Exception e) {
            log.error("获取模板详情失败", e);
            return new JsonBean(0, "获取失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/templates/add")
    @ResponseBody
    @ApiOperation("新增模板")
    public String addTemplate(@FlexibleRequestBody ReportTemplateDTO dto,
                             @RequestHeader(value = "token", required = false) String token,
                             HttpServletResponse response) {
        try {
            log.info("新增模板参数: templateName={}, templateCode={}", dto.getTemplateName(), dto.getTemplateCode());
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            TblReportTemplate template = financingReportManagementService.addTemplate(dto);
            return new JsonBean(1, "新增成功", template).toJson();
        } catch (Exception e) {
            log.error("新增模板失败", e);
            return new JsonBean(0, "新增失败: " + e.getMessage(), null).toJson();
        }
    }

    @PutMapping("/templates/{templateId}")
    @ResponseBody
    @ApiOperation("更新模板")
    public String updateTemplate(@PathVariable String templateId,
                                @FlexibleRequestBody ReportTemplateDTO dto,
                                @RequestHeader(value = "token", required = false) String token,
                                HttpServletResponse response) {
        try {
            log.info("更新模板参数: templateId={}, templateName={}", templateId, dto.getTemplateName());
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            dto.setTemplateId(templateId);
            TblReportTemplate template = financingReportManagementService.updateTemplate(dto);
            return new JsonBean(1, "更新成功", template).toJson();
        } catch (Exception e) {
            log.error("更新模板失败", e);
            return new JsonBean(0, "更新失败: " + e.getMessage(), null).toJson();
        }
    }

    @DeleteMapping("/templates/{templateIds}")
    @ResponseBody
    @ApiOperation("删除模板")
    public String deleteTemplate(@PathVariable String templateIds,
                                @RequestHeader(value = "token", required = false) String token,
                                HttpServletResponse response) {
        try {
            log.info("删除模板参数: templateIds={}", templateIds);
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            // 支持批量删除，ID用逗号分隔
            String[] ids = templateIds.split(",");
            if (ids.length == 1) {
                financingReportManagementService.deleteTemplate(ids[0]);
            } else {
                financingReportManagementService.batchDeleteTemplates(java.util.Arrays.asList(ids));
            }
            return new JsonBean(1, "删除成功", null).toJson();
        } catch (Exception e) {
            log.error("删除模板失败", e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/templates/{templateId}/status")
    @ResponseBody
    @ApiOperation("更新模板状态")
    public String updateTemplateStatus(@PathVariable String templateId,
                                      @RequestParam Integer isEnabled,
                                      @RequestHeader(value = "token", required = false) String token,
                                      HttpServletResponse response) {
        try {
            log.info("更新模板状态参数: templateId={}, isEnabled={}", templateId, isEnabled);
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean(401, "用户已失效", null);
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                return json.toJson();
            }

            financingReportManagementService.updateTemplateStatus(templateId, isEnabled);
            return new JsonBean(1, "更新成功", null).toJson();
        } catch (Exception e) {
            log.error("更新模板状态失败", e);
            return new JsonBean(0, "更新失败: " + e.getMessage(), null).toJson();
        }
    }

    @PostMapping("/templates/export")
    @ResponseBody
    @ApiOperation("导出模板列表")
    public void exportTemplates(ReportTemplateQueryDTO queryDTO,
                               @RequestHeader(value = "token", required = false) String token,
                               HttpServletResponse response) {
        try {
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(new JsonBean(401, "用户已失效", null).toJson());
                return;
            }

            // 查询模板列表
            if (queryDTO == null) {
                queryDTO = new ReportTemplateQueryDTO();
            }
            queryDTO.setPageNum(1);
            queryDTO.setPageSize(10000);
            PageInfo<TblReportTemplate> pageInfo = financingReportManagementService.getTemplateList(queryDTO);
            List<TblReportTemplate> templates = pageInfo.getList();

            // 设置响应头
            String fileName = "报表模板_" + new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + ".xlsx";
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(fileName, "UTF-8"));

            // 使用 POI 生成 Excel
            org.apache.poi.xssf.usermodel.XSSFWorkbook workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook();
            org.apache.poi.xssf.usermodel.XSSFSheet sheet = workbook.createSheet("报表模板");

            // 创建标题样式
            org.apache.poi.xssf.usermodel.XSSFCellStyle headerStyle = workbook.createCellStyle();
            org.apache.poi.xssf.usermodel.XSSFFont headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);
            headerStyle.setAlignment(org.apache.poi.ss.usermodel.HorizontalAlignment.CENTER);
            headerStyle.setFillForegroundColor(org.apache.poi.ss.usermodel.IndexedColors.GREY_25_PERCENT.getIndex());
            headerStyle.setFillPattern(org.apache.poi.ss.usermodel.FillPatternType.SOLID_FOREGROUND);

            // 创建标题行
            org.apache.poi.xssf.usermodel.XSSFRow headerRow = sheet.createRow(0);
            String[] headers = {"序号", "模板代码", "模板名称", "模板类型", "报告频率", "是否启用", "生效日期", "失效日期", "创建时间", "描述"};
            for (int i = 0; i < headers.length; i++) {
                org.apache.poi.xssf.usermodel.XSSFCell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
                sheet.setColumnWidth(i, i == 2 || i == 9 ? 8000 : 4000);
            }

            // 创建数据行
            java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd");
            for (int i = 0; i < templates.size(); i++) {
                TblReportTemplate template = templates.get(i);
                org.apache.poi.xssf.usermodel.XSSFRow dataRow = sheet.createRow(i + 1);
                dataRow.createCell(0).setCellValue(i + 1);
                dataRow.createCell(1).setCellValue(template.getTemplateCode() != null ? template.getTemplateCode() : "");
                dataRow.createCell(2).setCellValue(template.getTemplateName() != null ? template.getTemplateName() : "");
                dataRow.createCell(3).setCellValue(getTemplateTypeName(template.getTemplateType()));
                dataRow.createCell(4).setCellValue(getFrequencyName(template.getReportFrequency()));
                dataRow.createCell(5).setCellValue(template.getIsEnabled() != null && template.getIsEnabled() == 1 ? "是" : "否");
                dataRow.createCell(6).setCellValue(template.getEffectiveDate() != null ? sdf.format(template.getEffectiveDate()) : "");
                dataRow.createCell(7).setCellValue(template.getExpiryDate() != null ? sdf.format(template.getExpiryDate()) : "");
                dataRow.createCell(8).setCellValue(template.getCreatedTime() != null ? sdf.format(template.getCreatedTime()) : "");
                dataRow.createCell(9).setCellValue(template.getDescription() != null ? template.getDescription() : "");
            }

            workbook.write(response.getOutputStream());
            workbook.close();
            response.getOutputStream().flush();
            log.info("导出模板列表成功，共 {} 条", templates.size());
        } catch (Exception e) {
            log.error("导出模板列表失败", e);
            try {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write(new JsonBean(0, "导出失败: " + e.getMessage(), null).toJson());
            } catch (Exception ex) {
                log.error("写入错误响应失败", ex);
            }
        }
    }

    /** 获取模板类型名称 */
    private String getTemplateTypeName(String type) {
        if (type == null) return "-";
        Map<String, String> typeMap = new HashMap<>();
        typeMap.put("DAILY", "日报模板");
        typeMap.put("WEEKLY", "周报模板");
        typeMap.put("MONTHLY", "月报模板");
        typeMap.put("QUARTERLY", "季报模板");
        typeMap.put("ANNUAL", "年报模板");
        typeMap.put("ANALYSIS", "分析模板");
        typeMap.put("STATISTICS", "统计模板");
        return typeMap.getOrDefault(type, type);
    }

    /** 获取报告频率名称 */
    private String getFrequencyName(String frequency) {
        if (frequency == null) return "-";
        Map<String, String> freqMap = new HashMap<>();
        freqMap.put("DAILY", "每日");
        freqMap.put("WEEKLY", "每周");
        freqMap.put("MONTHLY", "每月");
        freqMap.put("QUARTERLY", "每季");
        freqMap.put("ANNUAL", "每年");
        freqMap.put("ON_DEMAND", "按需");
        return freqMap.getOrDefault(frequency, frequency);
    }

    // ==================== 报表分发接口 ====================

    @PostMapping("/distribute/{recordId}")
    @ResponseBody
    @ApiOperation("分发报表")
    public String distributeReport(@PathVariable String recordId,
                                  @RequestParam Map<String, Object> distributeParams,
                                  @RequestHeader(value = "token", required = false) String token,
                                  HttpServletResponse response) {
        try {
            log.info("分发报表请求, recordId: {}, params: {}", recordId, distributeParams);

            // 获取分发参数
            @SuppressWarnings("unchecked")
            List<String> recipients = (List<String>) distributeParams.get("recipients");
            Boolean sendEmail = (Boolean) distributeParams.getOrDefault("sendEmail", false);
            String emailSubject = (String) distributeParams.get("emailSubject");
            String emailContent = (String) distributeParams.get("emailContent");
            Boolean sendNotification = (Boolean) distributeParams.getOrDefault("sendNotification", false);

            if (recipients == null || recipients.isEmpty()) {
                return new JsonBean(0, "请选择接收人", null).toJson();
            }

            // 记录分发日志
            log.info("报表分发 - recordId: {}, 接收人: {}, 发送邮件: {}, 系统通知: {}",
                    recordId, recipients, sendEmail, sendNotification);

            // TODO: 实际的邮件发送和系统通知逻辑
            // 这里模拟分发成功
            Map<String, Object> result = new HashMap<>();
            result.put("recordId", recordId);
            result.put("recipients", recipients);
            result.put("emailSent", sendEmail);
            result.put("notificationSent", sendNotification);
            result.put("distributeTime", new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()));

            log.info("报表分发成功, recordId: {}", recordId);
            return new JsonBean(1, "分发成功", result).toJson();
        } catch (Exception e) {
            log.error("分发报表失败", e);
            return new JsonBean(0, "分发失败: " + e.getMessage(), null).toJson();
        }
    }
}
