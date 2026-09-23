package com.huabo.cybermonitor.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.huabo.cybermonitor.entity.*;
import com.huabo.cybermonitor.mapper.*;
import com.huabo.cybermonitor.service.IEnterpriseInfoService;
import com.huabo.cybermonitor.service.IMonitorModelService;
import com.huabo.cybermonitor.util.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 资金穿透监管首页 - 统计数据接口
 * 对应前端 /monitor/v1/fundHome/** 四个接口
 */
@Tag(name = "资金穿透监管首页", description = "FundHome首页数据接口")
@RestController
@RequestMapping("/v1/fundHome")
@Slf4j
public class FundHomeController {

    @Autowired private IEnterpriseInfoService enterpriseInfoService;
    @Autowired private IMonitorModelService monitorModelService;
    @Autowired private MonitorModelsolutionMapper modelsolutionMapper;
    @Autowired private TblBiDatasourceMapper biDatasourceMapper;
    @Autowired private RiskWarningMapper riskWarningMapper;
    @Autowired private TblFundFlowPenetrationMapper fundFlowMapper;
    @Autowired private TblFinanceStatementMapper financeStatementMapper;
    @Autowired private TblContractRecordMapper contractRecordMapper;
    @Autowired private GzctProcPurchaseRecordMapper purchaseRecordMapper;
    @Autowired private GzctEnterpriseHrEmployeeMapper hrEmployeeMapper;

    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Operation(summary = "KPI统计（监管企业数/模型数/数据源数/活跃预警数/问题汇总数/数据融合率）")
    @GetMapping("/kpi")
    public R<Map<String, Object>> kpi() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("monitorCount",  safeCount(() -> enterpriseInfoService.count()));
        data.put("modelCount",    safeCount(() -> monitorModelService.count()));
        data.put("dataSourceCount", safeCount(() -> biDatasourceMapper.selectCount(null)));
        data.put("activeWarnings", safeCount(() -> riskWarningMapper.selectCount(
                new LambdaQueryWrapper<RiskWarning>().ne(RiskWarning::getWarningStatus, "HANDLED"))));
        data.put("issueCount",    safeCount(() -> riskWarningMapper.selectCount(null)));
        long total = safeCount(() -> enterpriseInfoService.count());
        long synced = safeCount(() -> fundFlowMapper.selectCount(null));
        data.put("dataFusionRate", total > 0 ? Math.round(synced * 1000.0 / total) / 10.0 : 0.0);
        return R.success(data);
    }

    @Operation(summary = "各模块数据量（卡片count）")
    @GetMapping("/moduleCounts")
    public R<Map<String, Object>> moduleCounts() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("monitorCount",   safeCount(() -> enterpriseInfoService.count()));
        data.put("dataFusionCount",safeCount(() -> biDatasourceMapper.selectCount(null)));
        data.put("skCount",        safeCount(() -> fundFlowMapper.selectCount(null)));
        data.put("cwCount",        safeCount(() -> financeStatementMapper.selectCount(null)));
        data.put("htCount",        safeCount(() -> contractRecordMapper.selectCount(null)));
        data.put("fpCount",        safeCount(() -> purchaseRecordMapper.selectCount(null)));
        data.put("ryCount",        safeCount(() -> hrEmployeeMapper.selectCount(null)));
        data.put("telescopeCount", 0L);
        data.put("sjmxCount",      safeCount(() -> monitorModelService.count()));
        data.put("pgmxCount",      safeCount(() -> modelsolutionMapper.selectCount(null)));
        return R.success(data);
    }

    @Operation(summary = "近期资金风险预警列表")
    @GetMapping("/warningList")
    public R<List<Map<String, Object>>> warningList() {
        List<RiskWarning> list = Collections.emptyList();
        try {
            list = riskWarningMapper.selectList(
                    new LambdaQueryWrapper<RiskWarning>()
                            .ne(RiskWarning::getWarningStatus, "HANDLED")
                            .orderByDesc(RiskWarning::getWarningTime)
                            .last("FETCH FIRST 20 ROWS ONLY"));
        } catch (Exception e) { log.error("查询资金风险预警失败", e); }
        List<Map<String, Object>> result = new ArrayList<>();
        for (RiskWarning w : list) {
            Map<String, Object> item = new LinkedHashMap<>();
            item.put("companyName", w.getEnterpriseName());
            item.put("warnType",    w.getWarningType());
            item.put("level",       w.getWarningLevel());
            item.put("levelLabel",  levelLabel(w.getWarningLevel()));
            item.put("source",      w.getIndicatorName() != null ? w.getIndicatorName() : "系统检测");
            item.put("warnDate",    w.getWarningTime() != null ? DATE_FMT.format(w.getWarningTime()) : "");
            item.put("status",      w.getWarningStatus());
            item.put("statusLabel", statusLabel(w.getWarningStatus()));
            result.add(item);
        }
        return R.success(result);
    }

    @Operation(summary = "问题汇总统计（待处置/处置中/已处置）")
    @GetMapping("/issueSummary")
    public R<Map<String, Object>> issueSummary() {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("unhandled", safeCount(() -> riskWarningMapper.selectCount(
                new LambdaQueryWrapper<RiskWarning>().eq(RiskWarning::getWarningStatus, "PENDING"))));
        data.put("handling",  safeCount(() -> riskWarningMapper.selectCount(
                new LambdaQueryWrapper<RiskWarning>().eq(RiskWarning::getWarningStatus, "HANDLING"))));
        data.put("handled",   safeCount(() -> riskWarningMapper.selectCount(
                new LambdaQueryWrapper<RiskWarning>().eq(RiskWarning::getWarningStatus, "HANDLED"))));
        return R.success(data);
    }

    // ---- 工具 ----
    private long safeCount(CountSupplier s) {
        try { return s.get(); } catch (Exception e) { log.warn("计数失败: {}", e.getMessage()); return 0L; }
    }

    private String levelLabel(String level) {
        if (level == null) return "低危";
        switch (level.toUpperCase()) {
            case "HIGH":   return "高危";
            case "MEDIUM": return "中危";
            default:       return "低危";
        }
    }

    private String statusLabel(String status) {
        if (status == null) return "待处置";
        switch (status.toUpperCase()) {
            case "HANDLED":  return "已处置";
            case "HANDLING": return "处置中";
            default:         return "待处置";
        }
    }

    @FunctionalInterface
    interface CountSupplier { long get() throws Exception; }
}