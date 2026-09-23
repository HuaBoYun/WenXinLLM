package com.financial.sharing.controller;

import com.financial.sharing.service.ReportsService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.util.PageableParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 报表分析模块控制器
 *
 * @author Financial Sharing System
 * @since 2024-01-01
 */
@Api(tags = "报表分析模块")
@RestController
@RequestMapping("/reports")
@CrossOrigin
public class ReportsController {

    @Resource
    private ReportsService reportsService;

    @ApiOperation("获取财务报表列表")
    @GetMapping("/financialReports")
    public MyJsonBean getFinancialReports(PageableParam pageableParam,
                                        @RequestParam(required = false) String reportType,
                                        @RequestParam(required = false) String period,
                                        @RequestParam(required = false) String status,
                                        @RequestParam(required = false) Long bookId,
                                        @RequestParam(required = false) Long tenantId) {
        return reportsService.getFinancialReports(pageableParam, reportType, period, status, bookId, tenantId);
    }

    @ApiOperation("获取业务分析数据")
    @GetMapping("/businessAnalysis")
    public MyJsonBean getBusinessAnalysis(@RequestParam String analysisType,
                                        @RequestParam String startPeriod,
                                        @RequestParam String endPeriod,
                                        @RequestParam(required = false) String dimension) {
        try {
            Map<String, Object> data = new HashMap<>();

            switch (analysisType) {
                case "cost":
                    data = generateCostAnalysisData();
                    break;
                case "revenue":
                    data = generateRevenueAnalysisData();
                    break;
                case "indicators":
                    data = generateFinancialIndicatorsData();
                    break;
                case "operational":
                    data = generateOperationalAnalysisData();
                    break;
                default:
                    data.put("message", "不支持的分析类型");
            }

            return MyJsonBean.successData("分析完成", data);
        } catch (Exception e) {
            return MyJsonBean.errorData("分析失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取成本分析数据")
    @GetMapping("/costAnalysis")
    public MyJsonBean getCostAnalysis(@RequestParam String startPeriod,
                                    @RequestParam String endPeriod,
                                    @RequestParam(required = false) String dimension) {
        try {
            Map<String, Object> data = generateCostAnalysisData();
            return MyJsonBean.successData("成本分析完成", data);
        } catch (Exception e) {
            return MyJsonBean.errorData("成本分析失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取收入分析数据")
    @GetMapping("/revenueAnalysis")
    public MyJsonBean getRevenueAnalysis(@RequestParam String startPeriod,
                                       @RequestParam String endPeriod,
                                       @RequestParam(required = false) String dimension) {
        try {
            Map<String, Object> data = generateRevenueAnalysisData();
            return MyJsonBean.successData("收入分析完成", data);
        } catch (Exception e) {
            return MyJsonBean.errorData("收入分析失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取财务指标数据")
    @GetMapping("/financialIndicators")
    public MyJsonBean getFinancialIndicators(@RequestParam String startPeriod,
                                           @RequestParam String endPeriod,
                                           @RequestParam(required = false) String dimension) {
        try {
            Map<String, Object> data = generateFinancialIndicatorsData();
            return MyJsonBean.successData("财务指标分析完成", data);
        } catch (Exception e) {
            return MyJsonBean.errorData("财务指标分析失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取经营分析数据")
    @GetMapping("/operationalAnalysis")
    public MyJsonBean getOperationalAnalysis(@RequestParam String startPeriod,
                                           @RequestParam String endPeriod,
                                           @RequestParam(required = false) String dimension) {
        try {
            Map<String, Object> data = generateOperationalAnalysisData();
            return MyJsonBean.successData("经营分析完成", data);
        } catch (Exception e) {
            return MyJsonBean.errorData("经营分析失败: " + e.getMessage());
        }
    }

    @ApiOperation("生成资产负债表")
    @PostMapping("/balanceSheet")
    public MyJsonBean generateBalanceSheet(@RequestBody Map<String, Object> reportData) {
        return reportsService.generateBalanceSheet(reportData);
    }

    @ApiOperation("生成利润表")
    @PostMapping("/incomeStatement")
    public MyJsonBean generateIncomeStatement(@RequestBody Map<String, Object> reportData) {
        return reportsService.generateIncomeStatement(reportData);
    }

    @ApiOperation("生成现金流量表")
    @PostMapping("/cashFlowStatement")
    public MyJsonBean generateCashFlowStatement(@RequestBody Map<String, Object> reportData) {
        return reportsService.generateCashFlowStatement(reportData);
    }

    // 私有方法：生成成本分析数据
    private Map<String, Object> generateCostAnalysisData() {
        Map<String, Object> data = new HashMap<>();
        data.put("totalCost", 62450000);
        data.put("totalCostChange", -2.3);
        data.put("directCost", 45230000);
        data.put("directCostChange", -1.8);
        data.put("indirectCost", 17220000);
        data.put("indirectCostChange", -3.5);
        data.put("costRatio", 72.9);
        data.put("costRatioChange", -0.8);

        // 成本明细数据
        java.util.List<Map<String, Object>> costDetails = new java.util.ArrayList<>();
        Map<String, Object> detail1 = new HashMap<>();
        detail1.put("category", "原材料成本");
        detail1.put("currentAmount", 28500000);
        detail1.put("previousAmount", 29200000);
        detail1.put("variance", -700000);
        detail1.put("varianceRate", -2.4);
        detail1.put("proportion", 45.6);
        detail1.put("remark", "原材料价格下降");
        costDetails.add(detail1);

        Map<String, Object> detail2 = new HashMap<>();
        detail2.put("category", "人工成本");
        detail2.put("currentAmount", 16730000);
        detail2.put("previousAmount", 16100000);
        detail2.put("variance", 630000);
        detail2.put("varianceRate", 3.9);
        detail2.put("proportion", 26.8);
        detail2.put("remark", "人员增加及薪资调整");
        costDetails.add(detail2);

        data.put("costDetails", costDetails);
        return data;
    }

    // 私有方法：生成收入分析数据
    private Map<String, Object> generateRevenueAnalysisData() {
        Map<String, Object> data = new HashMap<>();
        data.put("totalRevenue", 85680000);
        data.put("totalRevenueChange", 12.5);
        data.put("productRevenue", 68540000);
        data.put("productRevenueChange", 15.2);
        data.put("serviceRevenue", 17140000);
        data.put("serviceRevenueChange", 3.8);
        data.put("growthRate", 12.5);
        data.put("growthRateChange", 2.3);

        // 收入明细数据
        java.util.List<Map<String, Object>> revenueDetails = new java.util.ArrayList<>();
        Map<String, Object> detail1 = new HashMap<>();
        detail1.put("category", "云计算服务");
        detail1.put("currentAmount", 35600000);
        detail1.put("previousAmount", 30200000);
        detail1.put("variance", 5400000);
        detail1.put("varianceRate", 17.9);
        detail1.put("proportion", 41.5);
        detail1.put("remark", "云服务需求增长强劲");
        revenueDetails.add(detail1);

        data.put("revenueDetails", revenueDetails);
        return data;
    }

    // 私有方法：生成财务指标数据
    private Map<String, Object> generateFinancialIndicatorsData() {
        Map<String, Object> data = new HashMap<>();
        data.put("netProfitMargin", 18.3);
        data.put("roa", 12.5);
        data.put("roe", 19.5);
        data.put("grossProfitMargin", 27.1);
        data.put("currentRatio", 2.35);
        data.put("quickRatio", 1.85);
        data.put("debtToEquityRatio", 0.56);
        data.put("interestCoverage", 8.5);
        data.put("assetTurnover", 1.2);
        data.put("receivableTurnover", 6.8);
        data.put("inventoryTurnover", 4.5);
        data.put("workingCapitalTurnover", 3.2);

        // 指标详情数据
        java.util.List<Map<String, Object>> indicatorDetails = new java.util.ArrayList<>();
        Map<String, Object> detail1 = new HashMap<>();
        detail1.put("category", "盈利能力");
        detail1.put("indicator", "净利润率");
        detail1.put("currentValue", "18.3%");
        detail1.put("previousValue", "16.8%");
        detail1.put("industryAverage", "15.2%");
        detail1.put("benchmark", "20.0%");
        detail1.put("evaluation", "良好");
        detail1.put("analysis", "净利润率持续提升，盈利能力增强");
        indicatorDetails.add(detail1);

        data.put("indicatorDetails", indicatorDetails);
        return data;
    }

    // 私有方法：生成经营分析数据
    private Map<String, Object> generateOperationalAnalysisData() {
        Map<String, Object> data = new HashMap<>();
        data.put("marketShare", 15.8);
        data.put("marketShareChange", 2.3);
        data.put("customerSatisfaction", 92.5);
        data.put("customerSatisfactionChange", 1.8);
        data.put("operationalEfficiency", 88.2);
        data.put("operationalEfficiencyChange", 3.5);
        data.put("innovationIndex", 7.8);
        data.put("innovationIndexChange", 0.5);

        // 经营详情数据
        java.util.List<Map<String, Object>> operationalDetails = new java.util.ArrayList<>();
        Map<String, Object> detail1 = new HashMap<>();
        detail1.put("dimension", "市场表现");
        detail1.put("indicator", "市场份额");
        detail1.put("currentValue", "15.8%");
        detail1.put("targetValue", "18.0%");
        detail1.put("completionRate", 87.8);
        detail1.put("trend", "上升");
        detail1.put("analysis", "市场份额稳步提升，但仍需加强市场拓展");
        operationalDetails.add(detail1);

        data.put("operationalDetails", operationalDetails);
        return data;
    }

    @ApiOperation("导出报表")
    @GetMapping("/export")
    public MyJsonBean exportReport(@RequestParam String reportType,
                                 @RequestParam String period,
                                 @RequestParam(required = false) String format) {
        return reportsService.exportReport(reportType, period, format);
    }

    @ApiOperation("获取报表统计")
    @GetMapping("/statistics")
    public MyJsonBean getStatistics(@RequestParam(required = false) Long bookId,
                                   @RequestParam(required = false) Long tenantId) {
        return reportsService.getStatistics(bookId, tenantId);
    }
}
