package com.global.treasurer.controller;

import com.github.pagehelper.PageInfo;
import com.hbfk.util.JsonBean;
import com.global.treasurer.entity.*;
import com.global.treasurer.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import com.global.treasurer.annotation.FlexibleRequestBody;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * @author Claude
 * @date 2026-01-20
 * @description 报表统计管理Controller
 */
@RestController
@RequestMapping("/report-statistics")
@Api(tags = "报表统计管理")
public class ReportStatisticsController {

    private static final Logger log = LoggerFactory.getLogger(ReportStatisticsController.class);

    @Resource
    private TblReportStatisticsService tblReportStatisticsService;

    /**
     * 分页查询报表统计
     */
    @GetMapping("/page")
    @ApiOperation("分页查询报表统计")
    public String getReportPage(
            @ApiParam("页码") @RequestParam(defaultValue = "1") Integer pageNo,
            @ApiParam("每页数量") @RequestParam(defaultValue = "20") Integer pageSize,
            @ApiParam("报表名称") @RequestParam(required = false) String reportName,
            @ApiParam("报表类型") @RequestParam(required = false) String reportType,
            @ApiParam("报表周期") @RequestParam(required = false) String reportPeriod,
            HttpServletResponse response) throws IOException {

        try {
            PageInfo<TblReportStatistics> pageInfo = tblReportStatisticsService.getReportPage(pageNo, pageSize, reportName, reportType, reportPeriod, null);
            Map<String, Object> result = new HashMap<>();
            result.put("tlist", pageInfo.getList());
            result.put("totalRecord", pageInfo.getTotal());
            result.put("pageNo", pageInfo.getPageNum());
            result.put("pageSize", pageInfo.getPageSize());

            return JsonBean.success(result);
        } catch (Exception e) {
            log.error("查询报表统计分页数据失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 根据ID查询报表统计
     */
    @GetMapping("/{reportId}")
    @ApiOperation("根据ID查询报表统计")
    public String getReportById(
            @ApiParam("报表ID") @PathVariable String reportId,
            HttpServletResponse response) throws IOException {

        try {
            TblReportStatistics report = tblReportStatisticsService.getReportById(reportId);
            return JsonBean.success(report);
        } catch (Exception e) {
            log.error("查询报表统计详情失败, reportId={}", reportId, e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 创建报表统计
     */
    @PostMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("创建报表统计")
    public String createReport(
            @ApiParam("报表统计信息") @FlexibleRequestBody TblReportStatistics report,
            HttpServletResponse response) throws IOException {

        try {
            TblReportStatistics saved = tblReportStatisticsService.saveReport(report);
            return JsonBean.success("创建成功", saved);
        } catch (Exception e) {
            log.error("创建报表统计失败", e);
            return new JsonBean(0, "创建失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 更新报表统计
     */
    @PutMapping(value = "", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation("更新报表统计")
    public String updateReport(
            @ApiParam("报表统计信息") @FlexibleRequestBody TblReportStatistics report,
            HttpServletResponse response) throws IOException {

        try {
            tblReportStatisticsService.updateReport(report);
            return JsonBean.success("更新成功");
        } catch (Exception e) {
            log.error("更新报表统计失败", e);
            return new JsonBean(0, "更新失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 删除报表统计
     */
    @DeleteMapping("/{reportId}")
    @ApiOperation("删除报表统计")
    public String deleteReport(
            @ApiParam("报表ID") @PathVariable String reportId,
            HttpServletResponse response) throws IOException {

        try {
            tblReportStatisticsService.deleteReport(reportId);
            return JsonBean.success("删除成功");
        } catch (Exception e) {
            log.error("删除报表统计失败, reportId={}", reportId, e);
            return new JsonBean(0, "删除失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 生成报表
     */
    @PostMapping("/generate")
    @ApiOperation("生成报表")
    public String generateReport(
            @ApiParam("报表类型") @RequestParam String reportType,
            @ApiParam("报表周期") @RequestParam String reportPeriod,
            @ApiParam("开始日期") @RequestParam(required = false) String startDate,
            @ApiParam("结束日期") @RequestParam(required = false) String endDate,
            HttpServletResponse response) throws IOException {

        try {
            TblReportStatistics report = tblReportStatisticsService.generateReport(reportType, reportPeriod, startDate, endDate);
            return JsonBean.success("生成成功", report);
        } catch (Exception e) {
            log.error("生成报表失败", e);
            return new JsonBean(0, "生成失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 分析报表
     */
    @GetMapping("/{reportId}/analyze")
    @ApiOperation("分析报表")
    public String analyzeReport(
            @ApiParam("报表ID") @PathVariable String reportId,
            HttpServletResponse response) throws IOException {

        try {
            Map<String, Object> analysis = tblReportStatisticsService.analyzeReport(reportId);
            return JsonBean.success(analysis);
        } catch (Exception e) {
            log.error("分析报表失败, reportId={}", reportId, e);
            return new JsonBean(0, "分析失败: " + e.getMessage(), null).toJson();
        }
    }

    /**
     * 获取报表类型列表
     */
    @GetMapping("/types")
    @ApiOperation("获取报表类型列表")
    public String getReportTypes(HttpServletResponse response) throws IOException {

        try {
            List<Map<String, Object>> types = tblReportStatisticsService.getReportTypes();
            return JsonBean.success(types);
        } catch (Exception e) {
            log.error("查询报表类型列表失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null).toJson();
        }
    }
}

