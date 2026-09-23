package com.financial.sharing.controller;

import com.financial.sharing.service.ArAnalysisService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.vo.param.ArAnalysisQueryParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;

/**
 * 应收分析控制器
 * @author system
 * @since 2026-01-04
 */
@RestController
@RequestMapping("/ar/analysis")
@Api(tags = "应收管理-应收分析")
@CrossOrigin
public class ArAnalysisController extends BaseController {

    @Resource
    private ArAnalysisService arAnalysisService;

    @GetMapping("/overview")
    @ApiOperation("获取应收总览数据")
    public MyJsonBean getReceivableOverview(@RequestParam @ApiParam("租户ID") Long tenantId) {
        return arAnalysisService.getReceivableOverview(tenantId);
    }

    @GetMapping("/receivableTrend")
    @ApiOperation("获取应收趋势分析")
    public MyJsonBean getReceivableTrend(
            @RequestParam @ApiParam("开始日期") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @ApiParam("结束日期") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam @ApiParam("租户ID") Long tenantId) {
        return arAnalysisService.getReceivableTrend(startDate, endDate, tenantId);
    }

    @GetMapping("/customerRanking")
    @ApiOperation("获取客户应收排名")
    public MyJsonBean getCustomerReceivableRanking(
            @RequestParam @ApiParam("租户ID") Long tenantId,
            @RequestParam(defaultValue = "10") @ApiParam("排名数量") Integer topN) {
        return arAnalysisService.getCustomerReceivableRanking(tenantId, topN);
    }

    @GetMapping("/receiptTrend")
    @ApiOperation("获取收款趋势分析")
    public MyJsonBean getReceiptTrend(
            @RequestParam @ApiParam("开始日期") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @ApiParam("结束日期") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam @ApiParam("租户ID") Long tenantId) {
        return arAnalysisService.getReceiptTrend(startDate, endDate, tenantId);
    }

    @GetMapping("/collectionRate")
    @ApiOperation("获取回款率分析")
    public MyJsonBean getCollectionRateAnalysis(
            @RequestParam @ApiParam("开始日期") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @ApiParam("结束日期") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam @ApiParam("租户ID") Long tenantId) {
        return arAnalysisService.getCollectionRateAnalysis(startDate, endDate, tenantId);
    }

    @GetMapping("/overdue")
    @ApiOperation("获取逾期分析")
    public MyJsonBean getOverdueAnalysis(@RequestParam @ApiParam("租户ID") Long tenantId) {
        return arAnalysisService.getOverdueAnalysis(tenantId);
    }

    @GetMapping("/badDebt")
    @ApiOperation("获取坏账分析")
    public MyJsonBean getBadDebtAnalysis(@RequestParam @ApiParam("租户ID") Long tenantId) {
        return arAnalysisService.getBadDebtAnalysis(tenantId);
    }

    @GetMapping("/dso")
    @ApiOperation("获取DSO（应收账款周转天数）分析")
    public MyJsonBean getDsoAnalysis(
            @RequestParam @ApiParam("开始日期") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @ApiParam("结束日期") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam @ApiParam("租户ID") Long tenantId) {
        return arAnalysisService.getDsoAnalysis(startDate, endDate, tenantId);
    }

    @GetMapping("/customerCredit/{customerId}")
    @ApiOperation("获取客户信用分析")
    public MyJsonBean getCustomerCreditAnalysis(
            @PathVariable @ApiParam("客户ID") String customerId,
            @RequestParam @ApiParam("租户ID") Long tenantId) {
        return arAnalysisService.getCustomerCreditAnalysis(customerId, tenantId);
    }

    @PostMapping("/comprehensive")
    @ApiOperation("获取综合分析报表")
    public MyJsonBean getComprehensiveReport(@RequestBody ArAnalysisQueryParam param) {
        return arAnalysisService.getComprehensiveReport(param);
    }

    @PostMapping("/export")
    @ApiOperation("导出分析报表")
    public MyJsonBean exportAnalysisReport(@RequestBody ArAnalysisQueryParam param) {
        return arAnalysisService.exportAnalysisReport(param);
    }

    @GetMapping("/structure")
    @ApiOperation("获取应收结构分析")
    public MyJsonBean getStructureAnalysis(@RequestParam @ApiParam("租户ID") Long tenantId) {
        return arAnalysisService.getStructureAnalysis(tenantId);
    }

    @GetMapping("/efficiency")
    @ApiOperation("获取收款效率分析")
    public MyJsonBean getEfficiencyAnalysis(
            @RequestParam @ApiParam("租户ID") Long tenantId,
            @RequestParam(required = false) @ApiParam("开始日期") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @ApiParam("结束日期") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        return arAnalysisService.getEfficiencyAnalysis(tenantId, startDate, endDate);
    }

    @GetMapping("/ranking")
    @ApiOperation("获取客户排名数据")
    public MyJsonBean getCustomerRankingData(
            @RequestParam @ApiParam("租户ID") Long tenantId,
            @RequestParam(defaultValue = "balance") @ApiParam("排名类型") String rankingType,
            @RequestParam(required = false) @ApiParam("开始日期") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam(required = false) @ApiParam("结束日期") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(defaultValue = "20") @ApiParam("排名数量") Integer topN) {
        return arAnalysisService.getCustomerRankingData(tenantId, rankingType, startDate, endDate, topN);
    }
}

