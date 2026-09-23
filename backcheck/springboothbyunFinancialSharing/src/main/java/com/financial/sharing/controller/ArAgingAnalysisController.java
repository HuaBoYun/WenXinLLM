package com.financial.sharing.controller;

import com.financial.sharing.service.ArAgingAnalysisService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.ArAgingAnalysisQueryParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.Map;

/**
 * 账龄分析控制器
 * @author system
 * @since 2026-01-04
 */
@RestController
@RequestMapping("/ar/aging")
@Api(tags = "应收管理-账龄分析")
@CrossOrigin
public class ArAgingAnalysisController extends BaseController {

    @Resource
    private ArAgingAnalysisService arAgingAnalysisService;

    @PostMapping("/details")
    @ApiOperation("查询账龄分析明细")
    public MyJsonBean<PageResult> getAgingDetails(@RequestBody ArAgingAnalysisQueryParam param) {
        return arAgingAnalysisService.getAgingDetails(param);
    }

    @GetMapping("/rangeSummary")
    @ApiOperation("按账龄区间统计")
    public MyJsonBean getAgingRangeSummary(
            @RequestParam @ApiParam("分析日期") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate analysisDate,
            @RequestParam @ApiParam("租户ID") Long tenantId) {
        return arAgingAnalysisService.getAgingRangeSummary(analysisDate, tenantId);
    }

    @GetMapping("/byCustomer")
    @ApiOperation("按客户统计账龄")
    public MyJsonBean getAgingByCustomer(
            @RequestParam @ApiParam("分析日期") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate analysisDate,
            @RequestParam @ApiParam("租户ID") Long tenantId) {
        return arAgingAnalysisService.getAgingByCustomer(analysisDate, tenantId);
    }

    @GetMapping("/byRiskLevel")
    @ApiOperation("按风险等级统计")
    public MyJsonBean getAgingByRiskLevel(
            @RequestParam @ApiParam("分析日期") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate analysisDate,
            @RequestParam @ApiParam("租户ID") Long tenantId) {
        return arAgingAnalysisService.getAgingByRiskLevel(analysisDate, tenantId);
    }

    @PostMapping("/generateSnapshot")
    @ApiOperation("生成账龄快照")
    public MyJsonBean generateAgingSnapshot(@RequestBody Map<String, Object> params) {
        String dateStr = (String) params.get("analysisDate");
        LocalDate analysisDate = LocalDate.parse(dateStr);
        Long tenantId = params.get("tenantId") != null ? Long.valueOf(params.get("tenantId").toString()) : null;
        return arAgingAnalysisService.generateAgingSnapshot(analysisDate, tenantId);
    }

    @GetMapping("/latestSnapshotDate")
    @ApiOperation("查询最新快照日期")
    public MyJsonBean getLatestSnapshotDate(@RequestParam @ApiParam("租户ID") Long tenantId) {
        return arAgingAnalysisService.getLatestSnapshotDate(tenantId);
    }

    @GetMapping("/trend")
    @ApiOperation("查询账龄趋势")
    public MyJsonBean getAgingTrend(
            @RequestParam @ApiParam("开始日期") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @ApiParam("结束日期") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam @ApiParam("租户ID") Long tenantId) {
        return arAgingAnalysisService.getAgingTrend(startDate, endDate, tenantId);
    }

    @GetMapping("/overdueAmount")
    @ApiOperation("查询逾期金额统计")
    public MyJsonBean getTotalOverdueAmount(
            @RequestParam @ApiParam("分析日期") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate analysisDate,
            @RequestParam @ApiParam("租户ID") Long tenantId) {
        return arAgingAnalysisService.getTotalOverdueAmount(analysisDate, tenantId);
    }

    @PostMapping("/export")
    @ApiOperation("导出账龄分析报表")
    public MyJsonBean exportAgingReport(@RequestBody ArAgingAnalysisQueryParam param) {
        return arAgingAnalysisService.exportAgingReport(param);
    }
}

