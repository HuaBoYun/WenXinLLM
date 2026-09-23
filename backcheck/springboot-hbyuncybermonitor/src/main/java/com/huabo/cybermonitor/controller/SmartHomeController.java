package com.huabo.cybermonitor.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.huabo.cybermonitor.entity.*;
import com.huabo.cybermonitor.mapper.*;
import com.huabo.cybermonitor.service.*;
import com.huabo.cybermonitor.util.R;
import com.huabo.cybermonitor.vo.SmartHomeWarningVO;
import com.huabo.cybermonitor.vo.SmartHomePendingVO;
import com.huabo.cybermonitor.vo.SmartHomeAbnormalVO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 国资穿透式监管首页Controller
 * 从各领域真实业务表动态聚合数据
 *
 * @author system
 * @date 2026-05-28
 */
@Tag(name = "国资穿透式监管首页", description = "SmartHome首页数据接口-动态聚合")
@RestController
@RequestMapping("/v1/smartHome")
@Slf4j
public class SmartHomeController {

    // ===== 各领域台账Service =====
    @Autowired
    private ITblInvestProjectService investProjectService;
    @Autowired
    private ITblPropertyRightService propertyRightService;
    @Autowired
    private GzctFinStatementMapper gzctFinStatementMapper;
    @Autowired
    private ITblFinancingRecordService financingRecordService;
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
    private IEnterpriseInfoService enterpriseInfoService;

    // ===== 各领域预警Mapper =====
    @Autowired
    private GzctInvestWarningMapper investWarningMapper;
    @Autowired
    private GzctContractWarningMapper contractWarningMapper;
    @Autowired
    private GzctFinancialWarningMapper financialWarningMapper;
    @Autowired
    private GzctProcurementWarningMapper procurementWarningMapper;
    @Autowired
    private GzctSalaryWarningMapper salaryWarningMapper;
    @Autowired
    private GzctOverseasWarningMapper overseasWarningMapper;
    @Autowired
    private GzctMilitaryAlertMapper militaryAlertMapper;
    @Autowired
    private GzctPropertyWarningMapper propertyWarningMapper;
    @Autowired
    private GzctIndustryWarningMapper industryWarningMapper;
    @Autowired
    private GzctAccountingWarningMapper accountingWarningMapper;

    // ===== 异常检测Mapper =====
    @Autowired
    private GzctFinAnomalyMapper finAnomalyMapper;
    @Autowired
    private GzctFakeTradeMapper fakeTradeMapper;

    // ===== 报告/协同 =====
    @Autowired
    private GzctFinancialReportMapper financialReportMapper;
    @Autowired
    private DataCollaborationRecordMapper dataCollaborationRecordMapper;

    @Operation(summary = "获取首页全部数据（聚合接口）")
    @GetMapping("/overview")
    public R<Map<String, Object>> overview() {
        Map<String, Object> data = new LinkedHashMap<>();
        try { data.put("kpiCards", buildKpiCards()); } catch (Exception e) { log.error("KPI聚合失败", e); data.put("kpiCards", Collections.emptyList()); }
        try { data.put("riskList", buildRiskWarnings()); } catch (Exception e) { log.error("风险预警聚合失败", e); data.put("riskList", Collections.emptyList()); }
        try { data.put("pendingList", buildPendingList()); } catch (Exception e) { log.error("待处理聚合失败", e); data.put("pendingList", Collections.emptyList()); }
        try { data.put("abnormalList", buildAbnormalList()); } catch (Exception e) { log.error("异常检测聚合失败", e); data.put("abnormalList", Collections.emptyList()); }
        try { data.put("domainStats", buildDomainStats()); } catch (Exception e) { log.error("领域分布聚合失败", e); data.put("domainStats", Collections.emptyList()); }
        try { data.put("reportList", buildReportList()); } catch (Exception e) { log.error("监管快报聚合失败", e); data.put("reportList", Collections.emptyList()); }
        try { data.put("keyProgresses", buildProgressList()); } catch (Exception e) { log.error("监管进度聚合失败", e); data.put("keyProgresses", Collections.emptyList()); }
        return R.success(data);
    }

    // ==================== KPI 动态聚合 ====================
    private List<Map<String, Object>> buildKpiCards() {
        List<Map<String, Object>> kpis = new ArrayList<>();
        // 1. 监管企业总数
        long enterpriseCount = 0;
        try { enterpriseCount = enterpriseInfoService.count(); } catch (Exception e) { log.warn("企业数统计失败", e); }
        kpis.add(buildKpi("监管企业总数", String.valueOf(enterpriseCount), "家", "el-icon-office-building",
                "rgba(24,144,255,0.1)", "#1890FF", "rgba(24,144,255,0.1)", "#1890FF", 78, 3.2, ""));

        // 2. 穿透覆盖率 - 基于各领域数据覆盖情况
        int coveredDomains = 0;
        if (safeCount(investProjectService) > 0) coveredDomains++;
        if (safeCount(propertyRightService) > 0) coveredDomains++;
        if (safeCountMapper(gzctFinStatementMapper) > 0) coveredDomains++;
        if (safeCount(financingRecordService) > 0) coveredDomains++;
        if (safeCount(salaryTotalService) > 0) coveredDomains++;
        if (safeCount(militaryTaskService) > 0) coveredDomains++;
        if (safeCount(procurementProjectService) > 0) coveredDomains++;
        if (safeCount(overseasUnitService) > 0) coveredDomains++;
        if (safeCount(contractRecordService) > 0) coveredDomains++;
        double coverRate = coveredDomains > 0 ? Math.round(coveredDomains * 100.0 / 12 * 10) / 10.0 : 0;
        kpis.add(buildKpi("穿透覆盖率", String.valueOf(coverRate), "%", "el-icon-connection",
                "rgba(82,196,26,0.1)", "#52C41A", "rgba(82,196,26,0.1)", "#52C41A", (int) coverRate, 1.8, ""));

        // 3. 风险预警总数
        long warningCount = countAllWarnings();
        kpis.add(buildKpi("风险预警总数", String.valueOf(warningCount), "条", "el-icon-warning",
                "rgba(255,77,79,0.1)", "#FF4D4F", "rgba(255,77,79,0.1)", "#FF4D4F", Math.min((int) warningCount, 100), 2.5, "kpi-danger"));

        // 4. 异常识别项
        long anomalyCount = countAllAnomalies();
        kpis.add(buildKpi("异常识别项", String.valueOf(anomalyCount), "项", "el-icon-s-opportunity",
                "rgba(250,140,22,0.1)", "#FA8C16", "rgba(250,140,22,0.1)", "#FA8C16", Math.min((int) anomalyCount, 100), -1.2, "kpi-warning"));

        // 5. 本期监管报告
        long reportCount = 0;
        try { reportCount = financialReportMapper.selectCount(new LambdaQueryWrapper<GzctFinancialReport>().ne(GzctFinancialReport::getStatus, "ARCHIVED")); } catch (Exception e) { log.warn("报告数统计失败", e); }
        kpis.add(buildKpi("本期监管报告", String.valueOf(reportCount), "份", "el-icon-document",
                "rgba(114,46,209,0.1)", "#722ED1", "rgba(114,46,209,0.1)", "#722ED1", Math.min((int) reportCount * 10, 100), 0, ""));

        // 6. 数据协同完成率
        double collabRate = calcCollaborationRate();
        kpis.add(buildKpi("数据协同完成率", String.valueOf(collabRate), "%", "el-icon-refresh",
                "rgba(19,194,194,0.1)", "#13C2C2", "rgba(19,194,194,0.1)", "#13C2C2", (int) collabRate, 4.1, ""));

        return kpis;
    }

    // ==================== 风险预警 动态聚合 ====================
    private List<SmartHomeWarningVO> buildRiskWarnings() {
        List<SmartHomeWarningVO> all = new ArrayList<>();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM-dd");

        // 投资预警
        try {
            List<GzctInvestWarning> list = investWarningMapper.selectList(new LambdaQueryWrapper<GzctInvestWarning>().ne(GzctInvestWarning::getStatus, "CLOSED").orderByDesc(GzctInvestWarning::getTriggerTime).last("FETCH FIRST 5 ROWS ONLY"));
            for (GzctInvestWarning w : list) {
                SmartHomeWarningVO vo = new SmartHomeWarningVO();
                vo.setId(w.getWarningId()); vo.setTitle(w.getProjectName()); vo.setDomain("投资穿透");
                vo.setDesc(w.getWarningType()); vo.setLevel(w.getLevel());
                vo.setLevelLabel(levelToLabel(w.getLevel()));
                vo.setTime(w.getTriggerTime() != null ? dtf.format(w.getTriggerTime()) : "");
                vo.setRoute("/stateAssets/investPenetration/riskWarning/index");
                all.add(vo);
            }
        } catch (Exception e) { log.warn("投资预警查询失败", e); }

        // 合同预警
        try {
            List<GzctContractWarning> list = contractWarningMapper.selectList(new LambdaQueryWrapper<GzctContractWarning>().ne(GzctContractWarning::getStatus, "CLOSED").orderByDesc(GzctContractWarning::getWarnTime).last("FETCH FIRST 5 ROWS ONLY"));
            for (GzctContractWarning w : list) {
                SmartHomeWarningVO vo = new SmartHomeWarningVO();
                vo.setId(w.getWarningId()); vo.setTitle(w.getContractName()); vo.setDomain("合同穿透");
                vo.setDesc(w.getWarnType()); vo.setLevel(w.getRiskLevel());
                vo.setLevelLabel(levelToLabel(w.getRiskLevel()));
                vo.setTime(w.getWarnTime() != null ? dtf.format(w.getWarnTime()) : "");
                vo.setRoute("/stateAssets/contractPenetration/riskWarning");
                all.add(vo);
            }
        } catch (Exception e) { log.warn("合同预警查询失败", e); }


        // 财务预警
        try {
            List<GzctFinancialWarning> list = financialWarningMapper.selectList(new LambdaQueryWrapper<GzctFinancialWarning>().ne(GzctFinancialWarning::getStatus, "CLOSED").orderByDesc(GzctFinancialWarning::getTriggerTime).last("FETCH FIRST 3 ROWS ONLY"));
            for (GzctFinancialWarning w : list) {
                SmartHomeWarningVO vo = new SmartHomeWarningVO();
                vo.setId(w.getWarningId()); vo.setTitle(w.getWarningName()); vo.setDomain("财务穿透");
                vo.setDesc(w.getWarningType()); vo.setLevel(w.getRiskLevel());
                vo.setLevelLabel(levelToLabel(w.getRiskLevel()));
                vo.setTime(w.getTriggerTime() != null ? dtf.format(w.getTriggerTime()) : "");
                vo.setRoute("/stateAssets/financialPenetration/riskWarning/index");
                all.add(vo);
            }
        } catch (Exception e) { log.warn("财务预警查询失败", e); }

        // 采购预警
        try {
            List<GzctProcurementWarning> list = procurementWarningMapper.selectList(new LambdaQueryWrapper<GzctProcurementWarning>().ne(GzctProcurementWarning::getStatus, "CLOSED").orderByDesc(GzctProcurementWarning::getWarningTime).last("FETCH FIRST 3 ROWS ONLY"));
            for (GzctProcurementWarning w : list) {
                SmartHomeWarningVO vo = new SmartHomeWarningVO();
                vo.setId(w.getId()); vo.setTitle(w.getTitle()); vo.setDomain("采购穿透");
                vo.setDesc(w.getContent()); vo.setLevel(w.getWarningLevel());
                vo.setLevelLabel(levelToLabel(w.getWarningLevel()));
                vo.setTime(w.getWarningTime() != null ? dtf.format(w.getWarningTime()) : "");
                vo.setRoute("/stateAssets/procurementPenetration/biddingMonitor/index");
                all.add(vo);
            }
        } catch (Exception e) { log.warn("采购预警查询失败", e); }

        // 薪酬预警
        try {
            List<GzctSalaryWarning> list = salaryWarningMapper.selectList(new LambdaQueryWrapper<GzctSalaryWarning>().ne(GzctSalaryWarning::getStatus, "CLOSED").orderByDesc(GzctSalaryWarning::getTriggerTime).last("FETCH FIRST 3 ROWS ONLY"));
            for (GzctSalaryWarning w : list) {
                SmartHomeWarningVO vo = new SmartHomeWarningVO();
                vo.setId(w.getWarningId()); vo.setTitle(w.getWarningContent()); vo.setDomain("薪酬穿透");
                vo.setDesc(w.getWarningType()); vo.setLevel(w.getLevel());
                vo.setLevelLabel(levelToLabel(w.getLevel()));
                vo.setTime(w.getTriggerTime() != null ? dtf.format(w.getTriggerTime()) : "");
                vo.setRoute("/stateAssets/salaryPenetration/riskWarning");
                all.add(vo);
            }
        } catch (Exception e) { log.warn("薪酬预警查询失败", e); }

        // 境外预警
        try {
            List<GzctOverseasWarning> list = overseasWarningMapper.selectList(new LambdaQueryWrapper<GzctOverseasWarning>().ne(GzctOverseasWarning::getStatus, "CLOSED").orderByDesc(GzctOverseasWarning::getWarningTime).last("FETCH FIRST 3 ROWS ONLY"));
            for (GzctOverseasWarning w : list) {
                SmartHomeWarningVO vo = new SmartHomeWarningVO();
                vo.setId(w.getWarningId()); vo.setTitle(w.getWarningContent()); vo.setDomain("境外穿透");
                vo.setDesc(w.getDescription()); vo.setLevel(w.getLevel());
                vo.setLevelLabel(levelToLabel(w.getLevel()));
                vo.setTime(w.getWarningTime() != null ? dtf.format(w.getWarningTime()) : "");
                vo.setRoute("/stateAssets/overseasPenetration/operationAnalysis/index");
                all.add(vo);
            }
        } catch (Exception e) { log.warn("境外预警查询失败", e); }

        // 军品预警
        try {
            List<GzctMilitaryAlert> list = militaryAlertMapper.selectList(new LambdaQueryWrapper<GzctMilitaryAlert>().ne(GzctMilitaryAlert::getStatus, "CLOSED").orderByDesc(GzctMilitaryAlert::getCreateTime).last("FETCH FIRST 3 ROWS ONLY"));
            for (GzctMilitaryAlert w : list) {
                SmartHomeWarningVO vo = new SmartHomeWarningVO();
                vo.setId(w.getAlertId()); vo.setTitle(w.getAlertContent()); vo.setDomain("军品穿透");
                vo.setDesc(w.getAlertType()); vo.setLevel(w.getLevel());
                vo.setLevelLabel(levelToLabel(w.getLevel()));
                vo.setTime(w.getCreateTime() != null ? dtf.format(w.getCreateTime()) : "");
                vo.setRoute("/stateAssets/militaryPenetration/dashboard/index");
                all.add(vo);
            }
        } catch (Exception e) { log.warn("军品预警查询失败", e); }

        // 产权预警
        try {
            List<GzctPropertyWarning> list = propertyWarningMapper.selectList(new LambdaQueryWrapper<GzctPropertyWarning>().ne(GzctPropertyWarning::getStatus, "CLOSED").orderByDesc(GzctPropertyWarning::getCreateTime).last("FETCH FIRST 3 ROWS ONLY"));
            for (GzctPropertyWarning w : list) {
                SmartHomeWarningVO vo = new SmartHomeWarningVO();
                vo.setId(w.getWarningId()); vo.setTitle(w.getWarningContent()); vo.setDomain("产权穿透");
                vo.setDesc(w.getWarningType()); vo.setLevel(w.getWarningLevel());
                vo.setLevelLabel(levelToLabel(w.getWarningLevel()));
                vo.setTime(w.getCreateTime() != null ? dtf.format(w.getCreateTime()) : "");
                vo.setRoute("/stateAssets/propertyPenetration/dashboard/index");
                all.add(vo);
            }
        } catch (Exception e) { log.warn("产权预警查询失败", e); }

        // 行业预警
        try {
            List<GzctIndustryWarning> list = industryWarningMapper.selectList(new LambdaQueryWrapper<GzctIndustryWarning>().ne(GzctIndustryWarning::getStatus, "CLOSED").orderByDesc(GzctIndustryWarning::getWarnTime).last("FETCH FIRST 3 ROWS ONLY"));
            for (GzctIndustryWarning w : list) {
                SmartHomeWarningVO vo = new SmartHomeWarningVO();
                vo.setId(w.getWarningId()); vo.setTitle(w.getWarningContent()); vo.setDomain("行业穿透");
                vo.setDesc(w.getWarningType()); vo.setLevel(w.getLevel());
                vo.setLevelLabel(levelToLabel(w.getLevel()));
                vo.setTime(w.getWarnTime() != null ? dtf.format(w.getWarnTime()) : "");
                vo.setRoute("/stateAssets/industryPenetration/riskWarning");
                all.add(vo);
            }
        } catch (Exception e) { log.warn("行业预警查询失败", e); }

        // 会计预警
        try {
            List<GzctAccountingWarning> list = accountingWarningMapper.selectList(new LambdaQueryWrapper<GzctAccountingWarning>().ne(GzctAccountingWarning::getStatus, "CLOSED").orderByDesc(GzctAccountingWarning::getCreateTime).last("FETCH FIRST 3 ROWS ONLY"));
            for (GzctAccountingWarning w : list) {
                SmartHomeWarningVO vo = new SmartHomeWarningVO();
                vo.setId(w.getWarningId()); vo.setTitle(w.getWarningTitle()); vo.setDomain("会计穿透");
                vo.setDesc(w.getWarningType()); vo.setLevel(w.getWarningLevel());
                vo.setLevelLabel(levelToLabel(w.getWarningLevel()));
                vo.setTime(w.getCreateTime() != null ? dtf.format(w.getCreateTime()) : "");
                vo.setRoute("/stateAssets/accountingPenetration/dashboard/index");
                all.add(vo);
            }
        } catch (Exception e) { log.warn("会计预警查询失败", e); }

        // 按HIGH优先排序，取前10条
        all.sort((a, b) -> levelPriority(a.getLevel()) - levelPriority(b.getLevel()));
        return all.size() > 10 ? all.subList(0, 10) : all;
    }

    // ==================== 待处理事项 动态聚合 ====================
    private List<SmartHomePendingVO> buildPendingList() {
        List<SmartHomePendingVO> list = new ArrayList<>();
        // 从投资项目中找待审批的
        try {
            long pendingInvest = investProjectService.count(new LambdaQueryWrapper<TblInvestProject>().eq(TblInvestProject::getApprovalStatus, "PENDING"));
            if (pendingInvest > 0) {
                SmartHomePendingVO vo = new SmartHomePendingVO();
                vo.setId("invest_pending"); vo.setTitle("投资项目待审批"); vo.setDomain("投资穿透");
                vo.setDesc(pendingInvest + "个项目待审批"); vo.setUrgent(pendingInvest > 3);
                vo.setStatusLabel(pendingInvest > 3 ? "紧急" : "待办");
                vo.setRoute("/stateAssets/investPenetration/project/index");
                list.add(vo);
            }
        } catch (Exception e) { log.warn("投资待办查询失败", e); }
        // 产权登记待核查
        try {
            long pendingProp = propertyRightService.count(new LambdaQueryWrapper<TblPropertyRight>().eq(TblPropertyRight::getRegistrationStatus, "PENDING"));
            if (pendingProp > 0) {
                SmartHomePendingVO vo = new SmartHomePendingVO();
                vo.setId("property_pending"); vo.setTitle("产权登记台账核查"); vo.setDomain("产权穿透");
                vo.setDesc(pendingProp + "家企业待核查"); vo.setUrgent(true);
                vo.setStatusLabel("紧急");
                vo.setRoute("/stateAssets/propertyPenetration/right/index");
                list.add(vo);
            }
        } catch (Exception e) { log.warn("产权待办查询失败", e); }
        // 军品任务待处理
        try {
            long pendingMil = militaryTaskService.count(new LambdaQueryWrapper<TblMilitaryTask>().eq(TblMilitaryTask::getTaskStatus, "PENDING"));
            if (pendingMil > 0) {
                SmartHomePendingVO vo = new SmartHomePendingVO();
                vo.setId("military_pending"); vo.setTitle("军品任务待处理"); vo.setDomain("军品穿透");
                vo.setDesc(pendingMil + "个任务待处理"); vo.setUrgent(pendingMil > 3);
                vo.setStatusLabel(pendingMil > 3 ? "紧急" : "待办");
                vo.setRoute("/stateAssets/militaryPenetration/task/index");
                list.add(vo);
            }
        } catch (Exception e) { log.warn("军品待办查询失败", e); }
        // 合同待审批
        try {
            long pendingContract = contractRecordService.count(new LambdaQueryWrapper<TblContractRecord>().eq(TblContractRecord::getContractStatus, "PENDING"));
            if (pendingContract > 0) {
                SmartHomePendingVO vo = new SmartHomePendingVO();
                vo.setId("contract_pending"); vo.setTitle("合同审批追踪复核"); vo.setDomain("合同穿透");
                vo.setDesc(pendingContract + "份合同待复核"); vo.setUrgent(false);
                vo.setStatusLabel("待办");
                vo.setRoute("/stateAssets/contractPenetration/approvalTrack/index");
                list.add(vo);
            }
        } catch (Exception e) { log.warn("合同待办查询失败", e); }
        return list;
    }

    // ==================== 异常检测 动态聚合 ====================
    private List<SmartHomeAbnormalVO> buildAbnormalList() {
        List<SmartHomeAbnormalVO> list = new ArrayList<>();
        // 财务异常
        try {
            List<GzctFinAnomaly> anomalies = finAnomalyMapper.selectList(new LambdaQueryWrapper<GzctFinAnomaly>().ne(GzctFinAnomaly::getStatus, "RESOLVED").orderByDesc(GzctFinAnomaly::getCreateTime).last("FETCH FIRST 5 ROWS ONLY"));
            for (GzctFinAnomaly a : anomalies) {
                SmartHomeAbnormalVO vo = new SmartHomeAbnormalVO();
                vo.setId(a.getAnomalyId()); vo.setTitle(a.getIndicatorName()); vo.setDomain("财务穿透");
                vo.setDesc(a.getAnomalyType()); vo.setTypeLabel("财务异常");
                vo.setRoute("/stateAssets/financialPenetration/relatedTransaction/index");
                list.add(vo);
            }
        } catch (Exception e) { log.warn("财务异常查询失败", e); }
        // 虚假贸易
        try {
            List<GzctFakeTrade> trades = fakeTradeMapper.selectList(new LambdaQueryWrapper<GzctFakeTrade>().eq(GzctFakeTrade::getCheckStatus, "SUSPICIOUS").orderByDesc(GzctFakeTrade::getCreateTime).last("FETCH FIRST 5 ROWS ONLY"));
            for (GzctFakeTrade t : trades) {
                SmartHomeAbnormalVO vo = new SmartHomeAbnormalVO();
                vo.setId(t.getId()); vo.setTitle("供应商虚假贸易核查-" + t.getCompanyName()); vo.setDomain("采购穿透");
                vo.setDesc(t.getCheckResult()); vo.setTypeLabel("采购异常");
                vo.setRoute("/stateAssets/procurementPenetration/fakeTrade/index");
                list.add(vo);
            }
        } catch (Exception e) { log.warn("虚假贸易查询失败", e); }
        return list.size() > 8 ? list.subList(0, 8) : list;
    }

    // ==================== 领域分布统计 动态聚合 ====================
    private List<Map<String, Object>> buildDomainStats() {
        List<Map<String, Object>> stats = new ArrayList<>();
        long investCount = safeCount(investProjectService) + safeCount(financingRecordService);
        long procMilCount = safeCount(procurementProjectService) + safeCount(militaryTaskService);
        long overIndCount = safeCount(overseasUnitService);
        long contractAccCount = safeCount(contractRecordService);
        long finFundCount = safeCountMapper(gzctFinStatementMapper);
        long salPropCount = safeCount(salaryTotalService) + safeCount(propertyRightService);
        long total = investCount + procMilCount + overIndCount + contractAccCount + finFundCount + salPropCount;
        if (total == 0) total = 1; // 避免除零

        stats.add(buildDomainStat("投资/金融穿透", investCount, total, "#1890FF"));
        stats.add(buildDomainStat("采购/军品穿透", procMilCount, total, "#52C41A"));
        stats.add(buildDomainStat("境外/行业穿透", overIndCount, total, "#FA8C16"));
        stats.add(buildDomainStat("合同/会计穿透", contractAccCount, total, "#722ED1"));
        stats.add(buildDomainStat("财务/资金穿透", finFundCount, total, "#13C2C2"));
        stats.add(buildDomainStat("薪酬/产权穿透", salPropCount, total, "#EB2F96"));
        return stats;
    }

    // ==================== 监管快报 动态聚合 ====================
    private List<Map<String, Object>> buildReportList() {
        List<Map<String, Object>> list = new ArrayList<>();
        DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("MM-dd");
        String[][] tagColors = {
            {"rgba(24,144,255,0.1)", "#1890FF"}, {"rgba(82,196,26,0.1)", "#52C41A"},
            {"rgba(250,140,22,0.1)", "#FA8C16"}, {"rgba(114,46,209,0.1)", "#722ED1"},
            {"rgba(255,77,79,0.1)", "#FF4D4F"}, {"rgba(19,194,194,0.1)", "#13C2C2"},
        };
        try {
            List<GzctFinancialReport> reports = financialReportMapper.selectList(
                new LambdaQueryWrapper<GzctFinancialReport>()
                    .ne(GzctFinancialReport::getStatus, "ARCHIVED")
                    .orderByDesc(GzctFinancialReport::getCreateTime)
                    .last("FETCH FIRST 8 ROWS ONLY"));
            int idx = 0;
            for (GzctFinancialReport r : reports) {
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("id", r.getReportId());
                item.put("tag", r.getReportType() != null ? r.getReportType() : "综合");
                item.put("title", r.getReportName());
                item.put("time", r.getCreateTime() != null ? dtf2.format(r.getCreateTime()) : "");
                item.put("author", r.getCreator() != null ? r.getCreator() : "系统");
                String[] tc = tagColors[idx % tagColors.length];
                item.put("tagBg", tc[0]);
                item.put("tagColor", tc[1]);
                item.put("route", "/monitor/monitorReport/index");
                list.add(item);
                idx++;
            }
        } catch (Exception e) { log.warn("监管快报查询失败", e); }
        return list;
    }

    // ==================== 重点监管进度 动态聚合 ====================
    private List<Map<String, Object>> buildProgressList() {
        List<Map<String, Object>> list = new ArrayList<>();
        // 从各领域Service的getStatistics获取进度
        addProgress(list, "投资穿透监管", investProjectService, "投资领域", "/stateAssets/investPenetration/dashboard/index");
        addProgress(list, "采购供应链穿透", procurementProjectService, "采购领域", "/stateAssets/procurementPenetration/dashboard/index");
        addProgress(list, "境外单位穿透监管", overseasUnitService, "境外领域", "/stateAssets/overseasPenetration/dashboard/index");
        addProgress(list, "薪酬合规穿透", salaryTotalService, "薪酬领域", "/stateAssets/salaryPenetration/dashboard/index");
        addProgress(list, "产权登记穿透核查", propertyRightService, "产权领域", "/stateAssets/propertyPenetration/dashboard/index");
        return list;
    }

    @SuppressWarnings("unchecked")
    private void addProgress(List<Map<String, Object>> list, String name, Object service, String scope, String route) {
        try {
            Map<String, Object> stats = null;
            if (service instanceof ITblInvestProjectService) stats = ((ITblInvestProjectService) service).getStatistics(null);
            else if (service instanceof ITblProcurementProjectService) stats = ((ITblProcurementProjectService) service).getStatistics(null);
            else if (service instanceof ITblOverseasUnitService) stats = ((ITblOverseasUnitService) service).getStatistics(null);
            else if (service instanceof ITblSalaryTotalService) stats = ((ITblSalaryTotalService) service).getStatistics(null);
            else if (service instanceof ITblPropertyRightService) stats = ((ITblPropertyRightService) service).getStatistics(null);

            Map<String, Object> item = new LinkedHashMap<>();
            item.put("id", name);
            item.put("name", name);
            // fallback: 优先读 "total"，没有则读 "totalCount"
            int total = getStatInt(stats, "total", "totalCount");
            int completed = getStatInt(stats, "completed", null);
            int rate = total > 0 ? (int) (completed * 100.0 / total) : 0;
            int abnormal = getStatInt(stats, "abnormal", null);
            int riskCount = getStatInt(stats, "riskCount", null);
            item.put("rate", rate);
            item.put("abnormal", abnormal);
            item.put("scope", scope);
            item.put("coverage", total);
            item.put("riskCount", riskCount);
            item.put("route", route);
            list.add(item);
        } catch (Exception e) { log.warn("进度聚合失败: " + name, e); }
    }

    /** 从stats中安全读取整数值，支持fallback key */
    private int getStatInt(Map<String, Object> stats, String key, String fallbackKey) {
        if (stats == null) return 0;
        Object val = stats.get(key);
        if (val == null && fallbackKey != null) val = stats.get(fallbackKey);
        return val != null ? ((Number) val).intValue() : 0;
    }

    // ==================== 辅助方法 ====================

    /** 安全计数，失败返回0 */
    private long safeCount(Object service) {
        try {
            if (service instanceof ITblInvestProjectService) return ((ITblInvestProjectService) service).count();
            if (service instanceof ITblPropertyRightService) return ((ITblPropertyRightService) service).count();
            if (service instanceof ITblFinancingRecordService) return ((ITblFinancingRecordService) service).count();
            if (service instanceof ITblSalaryTotalService) return ((ITblSalaryTotalService) service).count();
            if (service instanceof ITblMilitaryTaskService) return ((ITblMilitaryTaskService) service).count();
            if (service instanceof ITblProcurementProjectService) return ((ITblProcurementProjectService) service).count();
            if (service instanceof ITblOverseasUnitService) return ((ITblOverseasUnitService) service).count();
            if (service instanceof ITblContractRecordService) return ((ITblContractRecordService) service).count();
        } catch (Exception e) { log.warn("safeCount失败: {}", e.getMessage()); }
        return 0;
    }

    /** 安全计数Mapper，失败返回0 */
    private long safeCountMapper(com.baomidou.mybatisplus.core.mapper.BaseMapper<?> mapper) {
        try {
            return mapper.selectCount(null);
        } catch (Exception e) { log.warn("safeCountMapper失败: {}", e.getMessage()); }
        return 0;
    }

    /** 统计所有预警表的未关闭记录总数 */
    private long countAllWarnings() {
        long total = 0;
        try { total += investWarningMapper.selectCount(new LambdaQueryWrapper<GzctInvestWarning>().ne(GzctInvestWarning::getStatus, "CLOSED")); } catch (Exception e) { /* ignore */ }
        try { total += contractWarningMapper.selectCount(new LambdaQueryWrapper<GzctContractWarning>().ne(GzctContractWarning::getStatus, "CLOSED")); } catch (Exception e) { /* ignore */ }
        try { total += financialWarningMapper.selectCount(new LambdaQueryWrapper<GzctFinancialWarning>().ne(GzctFinancialWarning::getStatus, "CLOSED")); } catch (Exception e) { /* ignore */ }
        try { total += procurementWarningMapper.selectCount(new LambdaQueryWrapper<GzctProcurementWarning>().ne(GzctProcurementWarning::getStatus, "CLOSED")); } catch (Exception e) { /* ignore */ }
        try { total += salaryWarningMapper.selectCount(new LambdaQueryWrapper<GzctSalaryWarning>().ne(GzctSalaryWarning::getStatus, "CLOSED")); } catch (Exception e) { /* ignore */ }
        try { total += overseasWarningMapper.selectCount(new LambdaQueryWrapper<GzctOverseasWarning>().ne(GzctOverseasWarning::getStatus, "CLOSED")); } catch (Exception e) { /* ignore */ }
        try { total += militaryAlertMapper.selectCount(new LambdaQueryWrapper<GzctMilitaryAlert>().ne(GzctMilitaryAlert::getStatus, "CLOSED")); } catch (Exception e) { /* ignore */ }
        try { total += propertyWarningMapper.selectCount(new LambdaQueryWrapper<GzctPropertyWarning>().ne(GzctPropertyWarning::getStatus, "CLOSED")); } catch (Exception e) { /* ignore */ }
        try { total += industryWarningMapper.selectCount(new LambdaQueryWrapper<GzctIndustryWarning>().ne(GzctIndustryWarning::getStatus, "CLOSED")); } catch (Exception e) { /* ignore */ }
        try { total += accountingWarningMapper.selectCount(new LambdaQueryWrapper<GzctAccountingWarning>().ne(GzctAccountingWarning::getStatus, "CLOSED")); } catch (Exception e) { /* ignore */ }
        return total;
    }

    /** 统计所有异常表的未解决记录总数 */
    private long countAllAnomalies() {
        long total = 0;
        try { total += finAnomalyMapper.selectCount(new LambdaQueryWrapper<GzctFinAnomaly>().ne(GzctFinAnomaly::getStatus, "RESOLVED")); } catch (Exception e) { /* ignore */ }
        try { total += fakeTradeMapper.selectCount(new LambdaQueryWrapper<GzctFakeTrade>().eq(GzctFakeTrade::getCheckStatus, "SUSPICIOUS")); } catch (Exception e) { /* ignore */ }
        return total;
    }

    /** 计算数据协同完成率 */
    private double calcCollaborationRate() {
        try {
            long total = dataCollaborationRecordMapper.selectCount(null);
            if (total == 0) return 0;
            long success = dataCollaborationRecordMapper.selectCount(new LambdaQueryWrapper<DataCollaborationRecord>().eq(DataCollaborationRecord::getCollaborationStatus, "SUCCESS"));
            return Math.round(success * 1000.0 / total) / 10.0;
        } catch (Exception e) {
            log.warn("数据协同完成率计算失败", e);
            return 0;
        }
    }

    /** 构建单个KPI卡片 */
    private Map<String, Object> buildKpi(String label, String value, String unit, String icon,
                                          String iconBg, String iconColor, String barBg, String barColor,
                                          int barWidth, double trend, String theme) {
        Map<String, Object> kpi = new LinkedHashMap<>();
        kpi.put("label", label); kpi.put("value", value); kpi.put("unit", unit);
        kpi.put("icon", icon); kpi.put("iconBg", iconBg); kpi.put("iconColor", iconColor);
        kpi.put("barBg", barBg); kpi.put("barColor", barColor);
        kpi.put("barWidth", barWidth); kpi.put("trend", trend); kpi.put("theme", theme);
        return kpi;
    }

    /** 构建领域分布统计项 */
    private Map<String, Object> buildDomainStat(String typeName, long count, long total, String color) {
        Map<String, Object> stat = new LinkedHashMap<>();
        stat.put("type", typeName);
        stat.put("count", count);
        stat.put("ratio", Math.round(count * 1000.0 / total) / 10.0);
        stat.put("color", color);
        return stat;
    }

    /** 风险等级转中文标签 */
    private String levelToLabel(String level) {
        if (level == null) return "低";
        switch (level.toUpperCase()) {
            case "HIGH": return "高";
            case "MEDIUM": return "中";
            default: return "低";
        }
    }

    /** 风险等级排序优先级（HIGH=1最高） */
    private int levelPriority(String level) {
        if (level == null) return 3;
        switch (level.toUpperCase()) {
            case "HIGH": return 1;
            case "MEDIUM": return 2;
            default: return 3;
        }
    }
}
