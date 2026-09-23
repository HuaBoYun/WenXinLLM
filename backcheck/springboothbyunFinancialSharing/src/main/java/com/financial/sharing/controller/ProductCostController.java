package com.financial.sharing.controller;

import com.financial.sharing.service.ProductCostService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.ProductCostQueryParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * 产品成本管理控制器
 *
 * @author AI Agent
 * @date 2025-10-21
 */
@Api(tags = "产品成本管理")
@RestController
@RequestMapping("/ma/productcost")
public class ProductCostController {

    @Autowired
    private ProductCostService productCostService;

    // ==================== 基础CRUD操作 ====================

    @ApiOperation("分页查询产品成本列表")
    @PostMapping("/getList")
    public MyJsonBean<PageResult> getList(@Valid @RequestBody ProductCostQueryParam param) {
        return productCostService.getProductCostList(param);
    }

    @ApiOperation("根据ID查询产品成本详情")
    @GetMapping("/getById")
    public MyJsonBean<Map<String, Object>> getById(
            @ApiParam("产品成本ID") @RequestParam Long costingId) {
        return productCostService.getProductCostById(costingId);
    }

    @ApiOperation("新增产品成本")
    @PostMapping("/create")
    public MyJsonBean<String> create(@Valid @RequestBody Map<String, Object> data) {
        return productCostService.createProductCost(data);
    }

    @ApiOperation("修改产品成本")
    @PostMapping("/update")
    public MyJsonBean<String> update(@Valid @RequestBody Map<String, Object> data) {
        return productCostService.updateProductCost(data);
    }

    @ApiOperation("删除产品成本")
    @PostMapping("/delete")
    public MyJsonBean<String> delete(
            @ApiParam("产品成本ID") @RequestParam Long costingId) {
        return productCostService.deleteProductCost(costingId);
    }

    @ApiOperation("批量删除产品成本")
    @PostMapping("/batchDelete")
    public MyJsonBean<String> batchDelete(@RequestBody List<Long> costingIds) {
        return productCostService.batchDeleteProductCost(costingIds);
    }

    // ==================== 产品信息管理 ====================

    @ApiOperation("查询产品信息列表")
    @PostMapping("/product/getList")
    public MyJsonBean<PageResult> getProductList(@Valid @RequestBody ProductCostQueryParam param) {
        return productCostService.getProductInfoList(param);
    }

    @ApiOperation("新增产品信息")
    @PostMapping("/product/create")
    public MyJsonBean<String> createProduct(@Valid @RequestBody Map<String, Object> data) {
        return productCostService.createProductInfo(data);
    }

    @ApiOperation("修改产品信息")
    @PostMapping("/product/update")
    public MyJsonBean<String> updateProduct(@Valid @RequestBody Map<String, Object> data) {
        return productCostService.updateProductInfo(data);
    }

    @ApiOperation("删除产品信息")
    @PostMapping("/product/delete")
    public MyJsonBean<String> deleteProduct(@RequestParam Long productId) {
        return productCostService.deleteProductInfo(productId);
    }

    // ==================== 成本核算 ====================

    @ApiOperation("执行成本核算")
    @PostMapping("/accounting/execute")
    public MyJsonBean<String> executeCostAccounting(
            @Valid @RequestBody ProductCostQueryParam param) {
        return productCostService.executeCostAccounting(param);
    }

    @ApiOperation("查询成本核算结果")
    @PostMapping("/accounting/getResults")
    public MyJsonBean<PageResult> getCostAccountingResults(
            @Valid @RequestBody ProductCostQueryParam param) {
        return productCostService.getCostAccountingResults(param);
    }

    @ApiOperation("重新计算成本")
    @PostMapping("/accounting/recalculate")
    public MyJsonBean<String> recalculateCost(@RequestParam Long costingId) {
        return productCostService.recalculateCost(costingId);
    }

    @ApiOperation("批量成本核算")
    @PostMapping("/accounting/batchExecute")
    public MyJsonBean<String> batchCostAccounting(
            @RequestParam List<Long> productIds,
            @RequestParam String costingPeriod) {
        return productCostService.batchCostAccounting(productIds, costingPeriod);
    }

    // ==================== 成本分析 ====================

    @ApiOperation("成本构成分析")
    @PostMapping("/analysis/composition")
    public MyJsonBean<List<Map<String, Object>>> getCostCompositionAnalysis(
            @Valid @RequestBody ProductCostQueryParam param) {
        return productCostService.getCostCompositionAnalysis(param);
    }

    @ApiOperation("成本趋势分析")
    @PostMapping("/analysis/trend")
    public MyJsonBean<List<Map<String, Object>>> getCostTrendAnalysis(
            @Valid @RequestBody ProductCostQueryParam param) {
        return productCostService.getCostTrendAnalysis(param);
    }

    @ApiOperation("成本对比分析")
    @PostMapping("/analysis/comparison")
    public MyJsonBean<List<Map<String, Object>>> getCostComparisonAnalysis(
            @Valid @RequestBody ProductCostQueryParam param) {
        return productCostService.getCostComparisonAnalysis(param);
    }

    @ApiOperation("成本差异分析")
    @PostMapping("/analysis/variance")
    public MyJsonBean<List<Map<String, Object>>> getCostVarianceAnalysis(
            @Valid @RequestBody ProductCostQueryParam param) {
        return productCostService.getCostVarianceAnalysis(param);
    }

    // ==================== 成本控制 ====================

    @ApiOperation("查询预算执行情况")
    @PostMapping("/control/getBudgetExecution")
    public MyJsonBean<List<Map<String, Object>>> getBudgetExecution(
            @Valid @RequestBody ProductCostQueryParam param) {
        return productCostService.getBudgetExecution(param);
    }

    @ApiOperation("查询成本预警")
    @PostMapping("/control/getAlerts")
    public MyJsonBean<Map<String, Object>> getCostAlerts(
            @Valid @RequestBody ProductCostQueryParam param) {
        return productCostService.getCostAlerts(param);
    }

    @ApiOperation("查询成本异常数据")
    @PostMapping("/control/getAnomalies")
    public MyJsonBean<List<Map<String, Object>>> getCostAnomalies(
            @Valid @RequestBody ProductCostQueryParam param) {
        return productCostService.getCostAnomalies(param);
    }

    @ApiOperation("设置成本预警规则")
    @PostMapping("/control/setAlertRules")
    public MyJsonBean<String> setCostAlertRules(
            @Valid @RequestBody Map<String, Object> data) {
        return productCostService.setCostAlertRules(data);
    }

    @ApiOperation("执行成本控制措施")
    @PostMapping("/control/executeMeasures")
    public MyJsonBean<String> executeCostControlMeasures(
            @Valid @RequestBody Map<String, Object> param) {
        return productCostService.executeCostControlMeasures(param);
    }

    @ApiOperation("查询成本控制效果")
    @PostMapping("/control/getEffectiveness")
    public MyJsonBean<Map<String, Object>> getCostControlEffectiveness(
            @Valid @RequestBody ProductCostQueryParam param) {
        return productCostService.getCostControlEffectiveness(param);
    }

    // ==================== 成本报告 ====================

    @ApiOperation("查询成本报告列表")
    @PostMapping("/report/getList")
    public MyJsonBean<PageResult> getCostReportList(
            @Valid @RequestBody ProductCostQueryParam param) {
        return productCostService.getCostReportList(param);
    }

    @ApiOperation("生成成本报告")
    @PostMapping("/report/generate")
    public MyJsonBean<String> generateCostReport(
            @Valid @RequestBody Map<String, Object> param) {
        return productCostService.generateCostReport(param);
    }

    @ApiOperation("下载成本报告")
    @GetMapping("/report/download/{reportId}")
    public MyJsonBean<Map<String, Object>> downloadCostReport(
            @ApiParam("报告ID") @PathVariable Long reportId) {
        return productCostService.downloadCostReport(reportId);
    }

    // ==================== 数据导入导出 ====================

    @ApiOperation("导出产品成本数据")
    @PostMapping("/export")
    public MyJsonBean<Map<String, Object>> exportProductCostData(
            @Valid @RequestBody ProductCostQueryParam param) {
        return productCostService.exportProductCostData(param);
    }

    @ApiOperation("导入产品成本数据")
    @PostMapping("/import")
    public MyJsonBean<String> importProductCostData(
            @Valid @RequestBody List<Map<String, Object>> data) {
        return productCostService.importProductCostData(data);
    }

    // ==================== 统计分析 ====================

    @ApiOperation("成本统计概览")
    @PostMapping("/statistics/overview")
    public MyJsonBean<Map<String, Object>> getCostStatisticsOverview(
            @Valid @RequestBody ProductCostQueryParam param) {
        return productCostService.getCostStatisticsOverview(param);
    }

    @ApiOperation("成本分布统计")
    @PostMapping("/statistics/distribution")
    public MyJsonBean<List<Map<String, Object>>> getCostDistributionStatistics(
            @Valid @RequestBody ProductCostQueryParam param) {
        return productCostService.getCostDistributionStatistics(param);
    }

    @ApiOperation("成本变化趋势")
    @PostMapping("/statistics/trend")
    public MyJsonBean<List<Map<String, Object>>> getCostChangeTrend(
            @Valid @RequestBody ProductCostQueryParam param) {
        return productCostService.getCostChangeTrend(param);
    }
}

