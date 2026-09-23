package com.financial.sharing.controller;

import com.financial.sharing.dto.param.PayableAnalysisQueryParam;
import com.financial.sharing.service.PayableAnalysisService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.vo.result.PayableAnalysisVO;
import com.financial.sharing.vo.result.PayableSummaryVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 应付分析Controller
 * @author system
 * @since 2025-01-05
 */
@Api(tags = "应付分析")
@RestController
@RequestMapping({"/ap/analysis", "/payables/statistics"})
public class PayableAnalysisController {

    @Resource
    private PayableAnalysisService payableAnalysisService;

    @ApiOperation("获取应付汇总")
    @GetMapping("/summary")
    public MyJsonBean<PayableSummaryVO> getSummary() {
        PayableSummaryVO result = payableAnalysisService.getSummary();
        return MyJsonBean.successData(result);
    }

    @ApiOperation("获取账龄分析")
    @PostMapping("/aging")
    public MyJsonBean<List<PayableAnalysisVO.AgingAnalysisItem>> getAgingAnalysis(
            @RequestBody(required = false) PayableAnalysisQueryParam param) {
        List<PayableAnalysisVO.AgingAnalysisItem> result = payableAnalysisService.getAgingAnalysis(param);
        return MyJsonBean.successData(result);
    }

    @ApiOperation("获取供应商分布")
    @PostMapping("/supplier-distribution")
    public MyJsonBean<List<PayableAnalysisVO.SupplierDistributionItem>> getSupplierDistribution(
            @RequestBody(required = false) PayableAnalysisQueryParam param) {
        List<PayableAnalysisVO.SupplierDistributionItem> result = payableAnalysisService.getSupplierDistribution(param);
        return MyJsonBean.successData(result);
    }

    @ApiOperation("获取趋势分析")
    @PostMapping({"/trend", "/trend-analysis"})
    public MyJsonBean<List<PayableAnalysisVO.TrendItem>> getTrendAnalysisPost(
            @RequestBody(required = false) PayableAnalysisQueryParam param) {
        List<PayableAnalysisVO.TrendItem> result = payableAnalysisService.getTrendAnalysis(param);
        return MyJsonBean.successData(result);
    }

    @ApiOperation("获取趋势分析(GET)")
    @GetMapping("/trend")
    public MyJsonBean<List<PayableAnalysisVO.TrendItem>> getTrendAnalysisGet(
            @RequestParam(required = false) Integer dimension) {
        PayableAnalysisQueryParam param = new PayableAnalysisQueryParam();
        param.setDimension(dimension);
        List<PayableAnalysisVO.TrendItem> result = payableAnalysisService.getTrendAnalysis(param);
        return MyJsonBean.successData(result);
    }

    @ApiOperation("获取结构分析数据(用于饼图)")
    @GetMapping("/trend-analysis")
    public MyJsonBean<Map<String, Object>> getStructureAnalysis(
            @RequestParam(required = false) Integer dimension) {
        PayableAnalysisQueryParam param = new PayableAnalysisQueryParam();
        param.setDimension(dimension);
        List<Map<String, Object>> structureData = payableAnalysisService.getStructureAnalysis(param);

        // 返回前端期望的格式
        Map<String, Object> response = new HashMap<>();
        response.put("structureData", structureData);
        return MyJsonBean.successData(response);
    }

    @ApiOperation("获取付款计划")
    @PostMapping("/payment-plan")
    public MyJsonBean<List<Map<String, Object>>> getPaymentPlan(
            @RequestBody(required = false) PayableAnalysisQueryParam param) {
        List<Map<String, Object>> result = payableAnalysisService.getPaymentPlan(param);
        return MyJsonBean.successData(result);
    }

    @ApiOperation("获取逾期分析")
    @GetMapping("/overdue")
    public MyJsonBean<Map<String, Object>> getOverdueAnalysis() {
        Map<String, Object> result = payableAnalysisService.getOverdueAnalysis();
        return MyJsonBean.successData(result);
    }

    @ApiOperation("获取供应商排名")
    @PostMapping("/supplier-ranking")
    public MyJsonBean<List<Map<String, Object>>> getSupplierRankingPost(
            @RequestBody(required = false) PayableAnalysisQueryParam param) {
        List<Map<String, Object>> result = payableAnalysisService.getSupplierRanking(param);
        return MyJsonBean.successData(result);
    }

    @ApiOperation("获取供应商排名(GET)")
    @GetMapping("/supplier-ranking")
    public MyJsonBean<List<Map<String, Object>>> getSupplierRankingGet() {
        List<Map<String, Object>> result = payableAnalysisService.getSupplierRanking(new PayableAnalysisQueryParam());
        return MyJsonBean.successData(result);
    }

    @ApiOperation("获取业务类型分布")
    @GetMapping("/business-type-distribution")
    public MyJsonBean<List<Map<String, Object>>> getBusinessTypeDistribution() {
        List<Map<String, Object>> result = payableAnalysisService.getBusinessTypeDistribution();
        return MyJsonBean.successData(result);
    }

    @ApiOperation("获取票据统计")
    @GetMapping("/bill-statistics")
    public MyJsonBean<Map<String, Object>> getBillStatistics() {
        Map<String, Object> result = payableAnalysisService.getBillStatistics();
        return MyJsonBean.successData(result);
    }

    @ApiOperation("导出应付报表")
    @PostMapping("/export")
    public MyJsonBean<String> exportReport(
            @RequestBody(required = false) PayableAnalysisQueryParam param) {
        String filePath = payableAnalysisService.exportReport(param);
        return MyJsonBean.successData(filePath);
    }

    @ApiOperation("获取现金流预测")
    @PostMapping("/cash-flow-forecast")
    public MyJsonBean<List<Map<String, Object>>> getCashFlowForecast(
            @RequestBody(required = false) PayableAnalysisQueryParam param) {
        List<Map<String, Object>> result = payableAnalysisService.getCashFlowForecast(param);
        return MyJsonBean.successData(result);
    }
}

