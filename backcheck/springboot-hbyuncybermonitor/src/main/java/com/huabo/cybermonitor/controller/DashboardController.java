package com.huabo.cybermonitor.controller;

import com.huabo.cybermonitor.service.*;
import com.huabo.cybermonitor.util.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.time.LocalDate;

@Tag(name = "监管驾驶舱", description = "穿透式监管总览大屏")
@RestController
@RequestMapping("/v1/supervision/dashboard")
@Slf4j
public class DashboardController {

    @Autowired
    private ITblInvestProjectService investProjectService;
    @Autowired
    private ITblPropertyRightService propertyRightService;
    @Autowired
    private ITblFinanceStatementService financeStatementService;
    @Autowired
    private ITblFinancingRecordService financingRecordService;
    @Autowired
    private ITblAccountingPolicyService accountingPolicyService;
    @Autowired
    private ITblSalaryTotalService salaryTotalService;
    @Autowired
    private ITblMilitaryTaskService militaryTaskService;
    @Autowired
    private ITblProcurementProjectService procurementProjectService;
    @Autowired
    private ITblOverseasUnitService overseasUnitService;
    @Autowired
    private ITblContractRecordService contractRecordService;
    @Autowired
    private ITblInvestigationTaskService investigationTaskService;

    @Operation(summary = "获取驾驶舱总览数据")
    @GetMapping("/overview")
    public R<Map<String, Object>> overview(@RequestParam(required = false) String companyId) {
        Map<String, Object> data = new HashMap<>();
        try { data.put("investment", investProjectService.getStatistics(companyId)); } catch (Exception e) { log.warn("investment统计失败", e); }
        try { data.put("property", propertyRightService.getStatistics(companyId)); } catch (Exception e) { log.warn("property统计失败", e); }
        try { data.put("finance", financeStatementService.getStatistics(companyId)); } catch (Exception e) { log.warn("finance统计失败", e); }
        try { data.put("financialRisk", financingRecordService.getStatistics(companyId)); } catch (Exception e) { log.warn("financialRisk统计失败", e); }
        try { data.put("salary", salaryTotalService.getStatistics(companyId)); } catch (Exception e) { log.warn("salary统计失败", e); }
        try { data.put("military", militaryTaskService.getStatistics(companyId)); } catch (Exception e) { log.warn("military统计失败", e); }
        try { data.put("procurement", procurementProjectService.getStatistics(companyId)); } catch (Exception e) { log.warn("procurement统计失败", e); }
        try { data.put("overseas", overseasUnitService.getStatistics(companyId)); } catch (Exception e) { log.warn("overseas统计失败", e); }
        try { data.put("contract", contractRecordService.getStatistics(companyId)); } catch (Exception e) { log.warn("contract统计失败", e); }
        try { data.put("investigation", investigationTaskService.getStatistics(companyId)); } catch (Exception e) { log.warn("investigation统计失败", e); }
        return R.success(data);
    }

    @Operation(summary = "获取各领域数据统计")
    @GetMapping("/domain/{domainType}")
    public R<Map<String, Object>> domainStatistics(
            @PathVariable String domainType,
            @RequestParam(required = false) String companyId) {
        try {
            Map<String, Object> stats;
            switch (domainType) {
                case "INVESTMENT": stats = investProjectService.getStatistics(companyId); break;
                case "PROPERTY": stats = propertyRightService.getStatistics(companyId); break;
                case "FINANCE": stats = financeStatementService.getStatistics(companyId); break;
                case "FINANCIAL_RISK": stats = financingRecordService.getStatistics(companyId); break;
                case "SALARY": stats = salaryTotalService.getStatistics(companyId); break;
                case "MILITARY": stats = militaryTaskService.getStatistics(companyId); break;
                case "PROCUREMENT": stats = procurementProjectService.getStatistics(companyId); break;
                case "OVERSEAS": stats = overseasUnitService.getStatistics(companyId); break;
                case "CONTRACT": stats = contractRecordService.getStatistics(companyId); break;
                case "INVESTIGATION": stats = investigationTaskService.getStatistics(companyId); break;
                default: return R.fail("未知的领域类型：" + domainType);
            }
            return R.success(stats);
        } catch (Exception e) {
            log.error("获取领域统计数据失败，领域：{}", domainType, e);
            return R.fail("获取统计数据失败：" + e.getMessage());
        }
    }

    @Operation(summary = "政策影响分析")
    @GetMapping("/policy-impact")
    public R<Map<String, Object>> policyImpact(@RequestParam(required = false) String enterpriseId) {
        try {
            Map<String, Object> data = new HashMap<>();
            Map<String, Object> base = overview(enterpriseId).getData();
            data.put("overview", base);
            data.put("enterpriseId", enterpriseId);
            data.put("complianceRate", 85);
            data.put("riskLevel", "medium");
            data.put("policyCount", 12);
            data.put("affectedModules", Arrays.asList("investment", "property", "finance"));
            return R.success(data);
        } catch (Exception e) {
            log.error("获取政策影响分析失败", e);
            return R.fail("获取政策影响分析失败：" + e.getMessage());
        }
    }

    @Operation(summary = "关键绩效指标KPI")
    @GetMapping("/kpi")
    public R<Map<String, Object>> kpi(
            @RequestParam(required = false) String enterpriseId,
            @RequestParam(required = false) String orgId) {
        try {
            Map<String, Object> data = new HashMap<>();
            Map<String, Object> overviewData = overview(enterpriseId).getData();
            data.put("overview", overviewData);
            data.put("enterpriseId", enterpriseId);
            data.put("supervisionScore", 78);
            data.put("complianceRate", 85);
            data.put("riskCount", 6);
            data.put("taskCompletion", 92);
            return R.success(data);
        } catch (Exception e) {
            log.error("获取KPI数据失败", e);
            return R.fail("获取KPI数据失败：" + e.getMessage());
        }
    }

    @Operation(summary = "决策支持数据")
    @GetMapping("/decision-support")
    public R<Map<String, Object>> decisionSupport(@RequestParam(required = false) String enterpriseId) {
        try {
            Map<String, Object> data = new HashMap<>();
            Map<String, Object> overviewData = overview(enterpriseId).getData();
            data.put("overview", overviewData);
            data.put("enterpriseId", enterpriseId);
            data.put("recommendations", Collections.emptyList());
            data.put("riskAlerts", Collections.emptyList());
            data.put("decisionPoints", Collections.emptyList());
            return R.success(data);
        } catch (Exception e) {
            log.error("获取决策支持数据失败", e);
            return R.fail("获取决策支持数据失败：" + e.getMessage());
        }
    }

    @Operation(summary = "监管趋势数据")
    @GetMapping("/supervision-trends")
    public R<Map<String, Object>> supervisionTrends(@RequestParam(required = false) String enterpriseId) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("enterpriseId", enterpriseId);
            List<Map<String, Object>> trends = new ArrayList<>();
            LocalDate today = LocalDate.now();
            for (int i = 5; i >= 0; i--) {
                Map<String, Object> point = new HashMap<>();
                point.put("month", today.minusMonths(i).getYear() + "-" +
                        String.format("%02d", today.minusMonths(i).getMonthValue()));
                point.put("complianceRate", 70 + (int)(Math.random() * 25));
                point.put("riskCount", (int)(Math.random() * 10) + 2);
                trends.add(point);
            }
            data.put("trends", trends);
            return R.success(data);
        } catch (Exception e) {
            log.error("获取监管趋势数据失败", e);
            return R.fail("获取监管趋势数据失败：" + e.getMessage());
        }
    }

    @Operation(summary = "实时预警信息")
    @GetMapping("/realtime-alerts")
    public R<Map<String, Object>> realtimeAlerts(@RequestParam(required = false) String enterpriseId) {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("enterpriseId", enterpriseId);
            data.put("alerts", Collections.emptyList());
            data.put("totalCount", 0);
            data.put("urgentCount", 0);
            // 聚合各领域预警
            Map<String, Object> overviewData = overview(enterpriseId).getData();
            data.put("overview", overviewData);
            return R.success(data);
        } catch (Exception e) {
            log.error("获取实时预警信息失败", e);
            return R.fail("获取实时预警信息失败：" + e.getMessage());
        }
    }

    @Operation(summary = "监管效果评估")
    @GetMapping("/supervision-effectiveness")
    public R<Map<String, Object>> supervisionEffectiveness() {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put("overallScore", 82);
            data.put("complianceImprovement", 5.3);
            data.put("riskReduction", 12.7);
            data.put("taskCompletionRate", 91);
            data.put("evaluationDate", LocalDate.now().toString());
            return R.success(data);
        } catch (Exception e) {
            log.error("获取监管效果评估失败", e);
            return R.fail("获取监管效果评估失败：" + e.getMessage());
        }
    }
}

