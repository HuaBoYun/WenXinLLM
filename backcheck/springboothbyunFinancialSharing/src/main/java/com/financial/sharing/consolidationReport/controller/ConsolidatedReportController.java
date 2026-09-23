package com.financial.sharing.consolidationReport.controller;

import com.financial.sharing.util.UserUtils;

import com.financial.sharing.consolidationReport.dto.ConsolidatedReportQueryParam;
import com.financial.sharing.consolidationReport.dto.ReportCompareResult;
import com.financial.sharing.consolidationReport.entity.TblConsolidatedReport;
import com.financial.sharing.consolidationReport.service.ConsolidatedReportService;
import com.github.pagehelper.PageInfo;
import com.financial.sharing.util.MyJsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 合并报表Controller
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Api(tags = "合并报表-合并报表生成")
@RestController
@RequestMapping("/consolidationReport/consolidatedReport")
public class ConsolidatedReportController {

    @Autowired
    private ConsolidatedReportService reportService;

    /**
     * 查询合并报表列表(分页)
     */
    @ApiOperation("查询合并报表列表(分页)")
    @PostMapping("/getReportList")
    public MyJsonBean getReportList(@RequestBody ConsolidatedReportQueryParam param) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            PageInfo<TblConsolidatedReport> pageInfo = reportService.getReportList(param);
            return MyJsonBean.ok("查询成功", pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 查询合并报表列表(不分页)
     */
    @ApiOperation("查询合并报表列表(不分页)")
    @PostMapping("/getReportListNoPage")
    public MyJsonBean getReportListNoPage(@RequestBody ConsolidatedReportQueryParam param) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            List<TblConsolidatedReport> list = reportService.getReportListNoPage(param);
            return MyJsonBean.ok("查询成功", list);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID查询合并报表
     */
    @ApiOperation("根据ID查询合并报表")
    @PostMapping("/getReportById")
    public MyJsonBean getReportById(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            String reportId = params.get("reportId");
            if (reportId == null || reportId.isEmpty()) {
                return MyJsonBean.errorData("合并报表ID不能为空");
            }
            
            TblConsolidatedReport report = reportService.getReportById(reportId);
            return MyJsonBean.ok("查询成功", report);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 生成合并报表
     */
    @ApiOperation("生成合并报表")
    @PostMapping("/generateReport")
    public MyJsonBean generateReport(@RequestBody Map<String, Object> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            String modelId = (String) params.get("modelId");
            String period = (String) params.get("period");
            String reportType = (String) params.get("reportType");
            Boolean regenerate = params.get("regenerate") != null 
                ? (Boolean) params.get("regenerate") 
                : false;
            
            if (modelId == null || modelId.isEmpty()) {
                return MyJsonBean.errorData("模型ID不能为空");
            }
            if (period == null || period.isEmpty()) {
                return MyJsonBean.errorData("期间不能为空");
            }
            
            Map<String, Object> result = reportService.generateReport(modelId, period, reportType, regenerate);
            return MyJsonBean.ok(result.get("message").toString(), result);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("生成失败: " + e.getMessage());
        }
    }

    /**
     * 删除合并报表
     */
    @ApiOperation("删除合并报表")
    @PostMapping("/deleteReport")
    public MyJsonBean deleteReport(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            String modelId = params.get("modelId");
            String period = params.get("period");
            String reportType = params.get("reportType");
            
            if (modelId == null || modelId.isEmpty()) {
                return MyJsonBean.errorData("模型ID不能为空");
            }
            if (period == null || period.isEmpty()) {
                return MyJsonBean.errorData("期间不能为空");
            }
            
            reportService.deleteReport(modelId, period, reportType);
            return MyJsonBean.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    /**
     * 确认合并报表
     */
    @ApiOperation("确认合并报表")
    @PostMapping("/confirmReport")
    public MyJsonBean confirmReport(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        
        try {
            String modelId = params.get("modelId");
            String period = params.get("period");
            String reportType = params.get("reportType");
            
            if (modelId == null || modelId.isEmpty()) {
                return MyJsonBean.errorData("模型ID不能为空");
            }
            if (period == null || period.isEmpty()) {
                return MyJsonBean.errorData("期间不能为空");
            }
            
            reportService.confirmReport(modelId, period, reportType);
            return MyJsonBean.ok("确认成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("确认失败: " + e.getMessage());
        }
    }

    /**
     * 根据模型ID和期间查询报表类型列表
     */
    @ApiOperation("根据模型ID和期间查询报表类型列表")
    @PostMapping("/getReportTypeList")
    public MyJsonBean getReportTypeList(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }

        try {
            String modelId = params.get("modelId");
            String period = params.get("period");

            if (modelId == null || modelId.isEmpty()) {
                return MyJsonBean.errorData("模型ID不能为空");
            }
            if (period == null || period.isEmpty()) {
                return MyJsonBean.errorData("期间不能为空");
            }

            List<String> reportTypeList = reportService.getReportTypeList(modelId, period);
            return MyJsonBean.ok("查询成功", reportTypeList);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 导出合并报表到Excel
     */
    @ApiOperation("导出合并报表到Excel")
    @PostMapping("/exportReport")
    public void exportReport(@RequestBody Map<String, String> params, HttpServletResponse response) {
        try {
            String modelId = params.get("modelId");
            String period = params.get("period");
            String reportType = params.get("reportType");

            if (modelId == null || modelId.isEmpty()) {
                throw new RuntimeException("模型ID不能为空");
            }
            if (period == null || period.isEmpty()) {
                throw new RuntimeException("期间不能为空");
            }
            if (reportType == null || reportType.isEmpty()) {
                throw new RuntimeException("报表类型不能为空");
            }

            reportService.exportReportToExcel(modelId, period, reportType, response);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("导出失败: " + e.getMessage());
        }
    }

    /**
     * 对比两个期间的合并报表
     */
    @ApiOperation("对比两个期间的合并报表")
    @PostMapping("/compareReports")
    public MyJsonBean compareReports(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }

        try {
            String modelId = params.get("modelId");
            String period1 = params.get("period1");
            String period2 = params.get("period2");
            String reportType = params.get("reportType");

            if (modelId == null || modelId.isEmpty()) {
                return MyJsonBean.errorData("模型ID不能为空");
            }
            if (period1 == null || period1.isEmpty()) {
                return MyJsonBean.errorData("期间1不能为空");
            }
            if (period2 == null || period2.isEmpty()) {
                return MyJsonBean.errorData("期间2不能为空");
            }
            if (reportType == null || reportType.isEmpty()) {
                return MyJsonBean.errorData("报表类型不能为空");
            }

            List<ReportCompareResult> compareResults = reportService.compareReports(modelId, period1, period2, reportType);
            return MyJsonBean.ok("对比成功", compareResults);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("对比失败: " + e.getMessage());
        }
    }
}

