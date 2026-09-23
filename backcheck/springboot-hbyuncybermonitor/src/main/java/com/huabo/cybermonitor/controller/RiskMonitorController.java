package com.huabo.cybermonitor.controller;

import com.huabo.cybermonitor.util.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 国资风险穿透监控大屏数据接口
 * 对接前端 /home/screen/gzfxct 页面
 */
@Tag(name = "国资风险穿透监控", description = "国资风险穿透大屏数据聚合")
@RestController
@RequestMapping({"/risk/monitor", "/api/risk/monitor"})
public class RiskMonitorController {

    private static final Logger log = LoggerFactory.getLogger(RiskMonitorController.class);

    /**
     * 获取风险监控总览数据（一次性返回大屏所需全部数据）
     * 前端请求地址：/api/risk/monitor/all
     */
    @Operation(summary = "获取风险监控总览数据")
    @GetMapping("/all")
    public R<Map<String, Object>> getAllRiskMonitorData() {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("overview", buildOverview());
            result.put("investment", buildInvestmentRisk());
            result.put("groupControl", buildGroupControlRisk());
            result.put("financial", buildFinancialRisk());
            result.put("credit", buildCreditRisk());
            result.put("business", buildBusinessRisk());
            result.put("procurement", buildProcurementRisk());
            result.put("legal", buildLegalRisk());
            result.put("trade", buildTradeRisk());
            return R.success(result);
        } catch (Exception e) {
            log.error("获取风险监控总览数据失败", e);
            return R.success(buildFallbackData());
        }
    }

    // ================== 总览 ==================

    private Map<String, Object> buildOverview() {
        Map<String, Object> overview = new HashMap<>();

        // TODO: 后续可从 GZCT_ENTERPRISE_RISK_MONITORING 等表按 WARNING_LEVEL 统计
        overview.put("criticalCount", 5);
        overview.put("highCount", 12);
        overview.put("mediumCount", 23);
        overview.put("lowCount", 8);

        List<Map<String, Object>> categoryData = new ArrayList<>();
        categoryData.add(buildCategoryItem("投资风险", 8));
        categoryData.add(buildCategoryItem("集团管控", 6));
        categoryData.add(buildCategoryItem("财务风险", 10));
        categoryData.add(buildCategoryItem("信用风险", 7));
        categoryData.add(buildCategoryItem("经营风险", 9));
        categoryData.add(buildCategoryItem("采购风险", 5));
        categoryData.add(buildCategoryItem("法律风险", 3));
        categoryData.add(buildCategoryItem("贸易风险", 4));
        overview.put("categoryData", categoryData);

        Map<String, Object> trendData = new HashMap<>();
        trendData.put("months", Arrays.asList("7月", "8月", "9月", "10月", "11月", "12月"));
        trendData.put("critical", Arrays.asList(3, 4, 5, 4, 6, 5));
        trendData.put("high", Arrays.asList(8, 10, 12, 11, 13, 12));
        trendData.put("medium", Arrays.asList(15, 18, 20, 22, 24, 23));
        trendData.put("low", Arrays.asList(5, 6, 7, 8, 9, 8));
        overview.put("trendData", trendData);

        return overview;
    }

    private Map<String, Object> buildCategoryItem(String name, Integer value) {
        Map<String, Object> item = new HashMap<>();
        item.put("name", name);
        item.put("value", value);
        return item;
    }

    // ================== 投资风险 ==================

    private Map<String, Object> buildInvestmentRisk() {
        Map<String, Object> investment = new HashMap<>();
        investment.put("externalInvestmentRisk", 5);
        investment.put("diversificationRisk", 3);
        investment.put("overseasRisk", 2);
        investment.put("assetIdleRisk", 4);

        List<Map<String, Object>> details = new ArrayList<>();
        details.add(buildDetailItem("某新能源项目投资", "对外投资", "高", 5000, "待处理"));
        details.add(buildDetailItem("某房地产项目", "无关多元", "中", 3200, "处理中"));
        details.add(buildDetailItem("某境外矿产投资", "境外投资", "严重", 8000, "待处理"));
        details.add(buildDetailItem("某闲置厂房", "资产闲置", "中", 1500, "已处理"));
        investment.put("details", details);

        return investment;
    }

    // ================== 集团管控风险 ==================

    private Map<String, Object> buildGroupControlRisk() {
        Map<String, Object> groupControl = new HashMap<>();
        groupControl.put("controlRisk", 3);
        groupControl.put("hierarchyRisk", 5);
        groupControl.put("guaranteeRisk", 4);
        groupControl.put("affiliationRisk", 2);
        return groupControl;
    }

    // ================== 财务风险 ==================

    private Map<String, Object> buildFinancialRisk() {
        Map<String, Object> financial = new HashMap<>();
        financial.put("debtRisk", 6);
        financial.put("debtRatio", "78%");
        financial.put("financialRisk", 4);
        financial.put("financingCost", "6.5%");
        financial.put("payableRisk", 8);
        financial.put("overdueAmount", "2.3亿");
        financial.put("receivableRisk", 5);
        financial.put("ageingDays", "180天");
        return financial;
    }

    // ================== 信用风险 ==================

    private Map<String, Object> buildCreditRisk() {
        Map<String, Object> credit = new HashMap<>();
        credit.put("creditMonitoring", 7);
        credit.put("debtPrediction", 5);
        credit.put("loanDefault", 6);
        credit.put("externalLoan", 4);
        credit.put("guarantee", 3);
        return credit;
    }

    // ================== 经营风险 ==================

    private Map<String, Object> buildBusinessRisk() {
        Map<String, Object> business = new HashMap<>();
        business.put("operatingLoss", 8);
        business.put("contractRisk", 6);
        business.put("cashPosition", 5);
        business.put("salaryIssues", 3);
        return business;
    }

    // ================== 采购风险 ==================

    private Map<String, Object> buildProcurementRisk() {
        Map<String, Object> procurement = new HashMap<>();
        procurement.put("biddingRisk", 7);
        procurement.put("irregularBidding", 5);
        procurement.put("relatedParty", 4);
        procurement.put("overPayment", 3);
        procurement.put("publicConsumption", 2);
        return procurement;
    }

    // ================== 法律风险 ==================

    private Map<String, Object> buildLegalRisk() {
        Map<String, Object> legal = new HashMap<>();
        legal.put("complianceRisk", 6);
        legal.put("illegalBusiness", 4);
        legal.put("litigation", 3);
        return legal;
    }

    // ================== 贸易风险 ==================

    private Map<String, Object> buildTradeRisk() {
        Map<String, Object> trade = new HashMap<>();
        trade.put("fakeTradeFin", 8);
        trade.put("fakeTradeEmpty", 6);
        return trade;
    }

    // ================== 公共构造方法 ==================

    private Map<String, Object> buildDetailItem(String projectName, String riskType,
                                                String riskLevel, Integer amount, String status) {
        Map<String, Object> item = new HashMap<>();
        item.put("projectName", projectName);
        item.put("riskType", riskType);
        item.put("riskLevel", riskLevel);
        item.put("amount", amount);
        item.put("status", status);
        return item;
    }

    private Map<String, Object> buildFallbackData() {
        return buildAllData();
    }

    private Map<String, Object> buildAllData() {
        Map<String, Object> result = new HashMap<>();
        result.put("overview", buildOverview());
        result.put("investment", buildInvestmentRisk());
        result.put("groupControl", buildGroupControlRisk());
        result.put("financial", buildFinancialRisk());
        result.put("credit", buildCreditRisk());
        result.put("business", buildBusinessRisk());
        result.put("procurement", buildProcurementRisk());
        result.put("legal", buildLegalRisk());
        result.put("trade", buildTradeRisk());
        return result;
    }
}
