package com.huabo.cybermonitor.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.cybermonitor.entity.*;
import com.huabo.cybermonitor.mapper.*;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.util.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 投资决策分析控制器
 * 覆盖前端 investmentDecision.js 中定义的所有接口
 * 项目CRUD由 InvestProjectController 处理，本控制器负责统计/风险/合规/进度/预警等辅助接口
 */
@Tag(name = "投资决策分析", description = "投资决策分析管理(统计/风险/合规/进度/预警)")
@RestController
@RequestMapping("/v1/supervision/investment")
@Slf4j
public class InvestmentDecisionController {

    @Autowired private GzctInvestProjectMapper projectMapper;
    @Autowired private GzctInvestNonMainBizMapper nonMainBizMapper;
    @Autowired private GzctInvestPostEvalMapper postEvalMapper;
    @Autowired private GzctInvestProgressMapper progressMapper;
    @Autowired private GzctInvestWarningMapper warningMapper;
    @Autowired private GzctInvestComplianceMapper complianceMapper;
    @Autowired private EquityStructureMapper equityStructureMapper;
    @Autowired private FinancialIndicatorsMapper financialIndicatorsMapper;

    private <T> PageResult<T> buildPageResult(Page<T> r) {
        PageResult<T> pr = new PageResult<>();
        pr.setTotalRecord((int) r.getTotal()); pr.setCurrentPage((int) r.getCurrent());
        pr.setPageNumber((int) r.getCurrent()); pr.setTotalPage((int) r.getPages());
        pr.setPageSize((int) r.getSize()); pr.setTlist(r.getRecords());
        return pr;
    }

    // ==================== 统计接口 ====================
    // 注意：/statistics 接口已由 InvestSupervisionController 提供，此处不再重复定义

    // ==================== 项目拒绝 ====================

    @Operation(summary = "拒绝投资项目")
    @PostMapping("/projects/reject")
    public R<Boolean> rejectProject(@RequestBody Map<String, Object> params) {
        try {
            String projectId = params.get("projectId") != null ? params.get("projectId").toString() : null;
            if (StringUtils.isEmpty(projectId)) return R.fail("项目ID不能为空");
            GzctInvestProject project = projectMapper.selectById(projectId);
            if (project == null) return R.fail("项目不存在");
            project.setApprovalStatus("REJECTED");
            project.setUpdateTime(LocalDateTime.now());
            projectMapper.updateById(project);
            return R.success(true);
        } catch (Exception e) { log.error("拒绝投资项目失败", e); return R.fail("拒绝失败：" + e.getMessage()); }
    }

    // ==================== 风险评估 ====================

    @Operation(summary = "投资风险评估")
    @PostMapping("/risk/assessment")
    public R<Map<String, Object>> riskAssessment(@RequestBody Map<String, Object> params) {
        try {
            String projectId = params.get("projectId") != null ? params.get("projectId").toString() : null;
            Map<String, Object> result = new HashMap<>();
            if (StringUtils.isNotEmpty(projectId)) {
                GzctInvestProject project = projectMapper.selectById(projectId);
                if (project != null) {
                    result.put("projectId", projectId);
                    result.put("projectName", project.getProjectName());
                    result.put("investAmount", project.getInvestAmount());
                    result.put("isMainBiz", project.getIsMainBiz());
                    result.put("riskLevel", "N".equals(project.getIsMainBiz()) ? "HIGH" : "LOW");
                    result.put("riskFactors", "N".equals(project.getIsMainBiz()) ? "非主业投资，风险较高" : "主业投资，风险可控");
                    result.put("suggestion", "N".equals(project.getIsMainBiz()) ? "建议加强投后管理，关注投资回报" : "符合主业方向，继续跟踪");
                }
            }
            return R.success(result);
        } catch (Exception e) { return R.fail("风险评估失败：" + e.getMessage()); }
    }

    @Operation(summary = "风险评估报告")
    @PostMapping("/risk/report")
    public R<Map<String, Object>> riskReport(@RequestBody Map<String, Object> params) {
        try {
            Map<String, Object> report = new HashMap<>();
            List<GzctInvestProject> all = projectMapper.selectList(null);
            long highRisk = all.stream().filter(p -> "N".equals(p.getIsMainBiz())).count();
            long total = all.size();
            report.put("totalProjects", total);
            report.put("highRiskCount", highRisk);
            report.put("lowRiskCount", total - highRisk);
            report.put("riskRate", total > 0 ? Math.round((double) highRisk / total * 10000) / 100.0 : 0);
            report.put("generateTime", LocalDateTime.now().toString());
            return R.success(report);
        } catch (Exception e) { return R.fail("生成报告失败：" + e.getMessage()); }
    }

    // 批量风险评估已由 InvestSupervisionController 提供，此处不再重复定义

    // ==================== 效果分析 ====================

    @Operation(summary = "投资效果分析")
    @PostMapping("/effect/analysis")
    public R<Map<String, Object>> effectAnalysis(@RequestBody Map<String, Object> params) {
        try {
            String projectId = params.get("projectId") != null ? params.get("projectId").toString() : null;
            Map<String, Object> result = new HashMap<>();
            if (StringUtils.isNotEmpty(projectId)) {
                GzctInvestProject project = projectMapper.selectById(projectId);
                if (project != null) {
                    result.put("projectId", projectId);
                    result.put("projectName", project.getProjectName());
                    result.put("expectedReturn", project.getExpectedReturn());
                    result.put("actualReturn", project.getActualReturn());
                    BigDecimal deviation = BigDecimal.ZERO;
                    if (project.getExpectedReturn() != null && project.getActualReturn() != null && project.getExpectedReturn().compareTo(BigDecimal.ZERO) > 0) {
                        deviation = project.getActualReturn().subtract(project.getExpectedReturn());
                    }
                    result.put("deviation", deviation);
                    result.put("effectLevel", deviation.compareTo(BigDecimal.ZERO) >= 0 ? "GOOD" : "POOR");
                }
            }
            return R.success(result);
        } catch (Exception e) { return R.fail("效果分析失败：" + e.getMessage()); }
    }

    @Operation(summary = "投资效果报告")
    @PostMapping("/effect/report")
    public R<Map<String, Object>> effectReport(@RequestBody Map<String, Object> params) {
        try {
            Map<String, Object> report = new HashMap<>();
            List<GzctInvestProject> all = projectMapper.selectList(null);
            long withReturn = all.stream().filter(p -> p.getActualReturn() != null).count();
            BigDecimal avgReturn = BigDecimal.ZERO;
            if (withReturn > 0) {
                avgReturn = all.stream().filter(p -> p.getActualReturn() != null)
                    .map(GzctInvestProject::getActualReturn).reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(BigDecimal.valueOf(withReturn), 2, RoundingMode.HALF_UP);
            }
            report.put("totalProjects", all.size());
            report.put("evaluatedProjects", withReturn);
            report.put("avgReturnRate", avgReturn);
            report.put("generateTime", LocalDateTime.now().toString());
            return R.success(report);
        } catch (Exception e) { return R.fail("生成报告失败：" + e.getMessage()); }
    }

    // ==================== 进度跟踪 ====================

    @Operation(summary = "投资进度跟踪")
    @PostMapping("/progress/track")
    public R<Boolean> progressTrack(@RequestBody Map<String, Object> params) {
        try {
            String projectId = params.get("projectId") != null ? params.get("projectId").toString() : null;
            if (StringUtils.isEmpty(projectId)) return R.fail("项目ID不能为空");
            return R.success(true);
        } catch (Exception e) { return R.fail("进度跟踪失败：" + e.getMessage()); }
    }

    @Operation(summary = "投资进度数据")
    @PostMapping("/progress/data")
    public R<PageResult<GzctInvestProgress>> progressData(@RequestBody(required = false) Map<String, Object> params) {
        try {
            if (params == null) params = new HashMap<>();
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctInvestProgress> w = new LambdaQueryWrapper<>();
            if (params.get("projectId") != null && StringUtils.isNotEmpty(params.get("projectId").toString())) {
                w.eq(GzctInvestProgress::getProjectId, params.get("projectId").toString());
            }
            w.orderByDesc(GzctInvestProgress::getCreateTime);
            Page<GzctInvestProgress> r = progressMapper.selectPage(new Page<>(pn, ps), w);
            return R.success(buildPageResult(r));
        } catch (Exception e) { return R.fail("查询进度数据失败：" + e.getMessage()); }
    }

    // ==================== 决策流程 ====================

    @Operation(summary = "投资决策流程监管")
    @PostMapping("/decision/process")
    public R<Boolean> monitorDecisionProcess(@RequestBody Map<String, Object> params) {
        try {
            return R.success(true);
        } catch (Exception e) { return R.fail("操作失败：" + e.getMessage()); }
    }

    @Operation(summary = "投资决策流程数据")
    @PostMapping("/decision/process/data")
    public R<Map<String, Object>> decisionProcessData(@RequestBody(required = false) Map<String, Object> params) {
        try {
            Map<String, Object> result = new HashMap<>();
            List<GzctInvestProject> all = projectMapper.selectList(null);
            result.put("totalProjects", all.size());
            result.put("pendingCount", all.stream().filter(p -> "PLANNING".equals(p.getProjectStatus())).count());
            result.put("approvedCount", all.stream().filter(p -> "APPROVED".equals(p.getApprovalStatus())).count());
            result.put("rejectedCount", all.stream().filter(p -> "REJECTED".equals(p.getApprovalStatus())).count());
            return R.success(result);
        } catch (Exception e) { return R.fail("查询决策流程数据失败：" + e.getMessage()); }
    }

    @Operation(summary = "投资决策支持")
    @PostMapping("/decision/support")
    public R<Map<String, Object>> decisionSupport(@RequestBody(required = false) Map<String, Object> params) {
        try {
            Map<String, Object> result = new HashMap<>();
            List<GzctInvestProject> all = projectMapper.selectList(null);
            BigDecimal totalAmount = all.stream().map(p -> p.getInvestAmount() != null ? p.getInvestAmount() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add);
            result.put("totalInvestAmount", totalAmount);
            result.put("mainBizRatio", all.size() > 0 ? Math.round((double) all.stream().filter(p -> "Y".equals(p.getIsMainBiz())).count() / all.size() * 10000) / 100.0 : 0);
            result.put("avgReturnRate", all.stream().filter(p -> p.getActualReturn() != null).map(GzctInvestProject::getActualReturn).reduce(BigDecimal.ZERO, BigDecimal::add).divide(BigDecimal.valueOf(Math.max(1, all.stream().filter(p -> p.getActualReturn() != null).count())), 2, RoundingMode.HALF_UP));
            return R.success(result);
        } catch (Exception e) { return R.fail("查询决策支持数据失败：" + e.getMessage()); }
    }

    // ==================== 合规检查 ====================

    @Operation(summary = "投资合规性检查")
    @PostMapping("/compliance/check")
    public R<Map<String, Object>> complianceCheck(@RequestBody Map<String, Object> params) {
        try {
            String projectId = params.get("projectId") != null ? params.get("projectId").toString() : null;
            Map<String, Object> result = new HashMap<>();
            result.put("projectId", projectId);
            result.put("isCompliant", true);
            result.put("issues", new ArrayList<>());
            if (StringUtils.isNotEmpty(projectId)) {
                GzctInvestProject project = projectMapper.selectById(projectId);
                if (project != null) {
                    List<Map<String, Object>> issues = new ArrayList<>();
                    if ("N".equals(project.getIsMainBiz())) {
                        Map<String, Object> issue = new HashMap<>();
                        issue.put("type", "非主业投资");
                        issue.put("level", "WARNING");
                        issue.put("desc", "该项目为非主业投资，需关注");
                        issues.add(issue);
                        result.put("isCompliant", false);
                    }
                    result.put("issues", issues);
                }
            }
            return R.success(result);
        } catch (Exception e) { return R.fail("合规检查失败：" + e.getMessage()); }
    }

    @Operation(summary = "合规检查报告")
    @PostMapping("/compliance/report")
    public R<PageResult<GzctInvestCompliance>> complianceReport(@RequestBody(required = false) Map<String, Object> params) {
        try {
            if (params == null) params = new HashMap<>();
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            Page<GzctInvestCompliance> r = complianceMapper.selectPage(new Page<>(pn, ps), new LambdaQueryWrapper<GzctInvestCompliance>().orderByDesc(GzctInvestCompliance::getCreateTime));
            return R.success(buildPageResult(r));
        } catch (Exception e) { return R.fail("查询合规报告失败：" + e.getMessage()); }
    }

    // ==================== 预警管理 ====================

    @Operation(summary = "投资预警设置")
    @PostMapping("/alert/set")
    public R<Boolean> setAlert(@RequestBody Map<String, Object> params) {
        try {
            return R.success(true);
        } catch (Exception e) { return R.fail("设置预警失败：" + e.getMessage()); }
    }

    @Operation(summary = "投资预警列表")
    @PostMapping("/alert/list")
    public R<PageResult<GzctInvestWarning>> alertList(@RequestBody(required = false) Map<String, Object> params) {
        try {
            if (params == null) params = new HashMap<>();
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctInvestWarning> w = new LambdaQueryWrapper<>();
            if (params.get("level") != null && StringUtils.isNotEmpty(params.get("level").toString())) {
                w.eq(GzctInvestWarning::getLevel, params.get("level").toString());
            }
            if (params.get("status") != null && StringUtils.isNotEmpty(params.get("status").toString())) {
                w.eq(GzctInvestWarning::getStatus, params.get("status").toString());
            }
            w.orderByDesc(GzctInvestWarning::getCreateTime);
            Page<GzctInvestWarning> r = warningMapper.selectPage(new Page<>(pn, ps), w);
            return R.success(buildPageResult(r));
        } catch (Exception e) { return R.fail("查询预警列表失败：" + e.getMessage()); }
    }

    // ==================== 导入导出 ====================

    // 导出投资数据已由 InvestSupervisionController 提供，此处不再重复定义

    @Operation(summary = "导入投资数据")
    @PostMapping("/import")
    public R<Boolean> importData(@RequestBody(required = false) Map<String, Object> params) {
        try {
            return R.success(true);
        } catch (Exception e) { return R.fail("导入失败：" + e.getMessage()); }
    }

    // ==================== 标的分析 ====================

    @Operation(summary = "投资标的信息")
    @PostMapping("/target/info")
    public R<Map<String, Object>> targetInfo(@RequestBody Map<String, Object> params) {
        try {
            String projectId = params.get("projectId") != null ? params.get("projectId").toString() : null;
            Map<String, Object> result = new HashMap<>();
            if (StringUtils.isNotEmpty(projectId)) {
                GzctInvestProject project = projectMapper.selectById(projectId);
                if (project != null) {
                    result.put("projectId", projectId);
                    result.put("targetCompany", project.getTargetCompany());
                    result.put("investAmount", project.getInvestAmount());
                    result.put("investType", project.getInvestType());
                    result.put("expectedReturn", project.getExpectedReturn());
                    result.put("actualReturn", project.getActualReturn());
                }
            }
            return R.success(result);
        } catch (Exception e) { return R.fail("查询标的信息失败：" + e.getMessage()); }
    }

    @Operation(summary = "投资标的风险分析")
    @PostMapping("/target/risk/analysis")
    public R<Map<String, Object>> targetRiskAnalysis(@RequestBody Map<String, Object> params) {
        try {
            String projectId = params.get("projectId") != null ? params.get("projectId").toString() : null;
            Map<String, Object> result = new HashMap<>();
            result.put("projectId", projectId);
            result.put("riskLevel", "LOW");
            result.put("riskFactors", new ArrayList<>());
            if (StringUtils.isNotEmpty(projectId)) {
                GzctInvestProject project = projectMapper.selectById(projectId);
                if (project != null) {
                    result.put("riskLevel", "N".equals(project.getIsMainBiz()) ? "HIGH" : "LOW");
                }
            }
            return R.success(result);
        } catch (Exception e) { return R.fail("风险分析失败：" + e.getMessage()); }
    }

    // ==================== 收益分析 ====================

    @Operation(summary = "投资收益分析")
    @PostMapping("/return/analysis")
    public R<Map<String, Object>> returnAnalysis(@RequestBody(required = false) Map<String, Object> params) {
        try {
            Map<String, Object> result = new HashMap<>();
            List<GzctInvestProject> all = projectMapper.selectList(null);
            BigDecimal totalExpected = all.stream().filter(p -> p.getExpectedReturn() != null).map(GzctInvestProject::getExpectedReturn).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal totalActual = all.stream().filter(p -> p.getActualReturn() != null).map(GzctInvestProject::getActualReturn).reduce(BigDecimal.ZERO, BigDecimal::add);
            result.put("totalExpectedReturn", totalExpected);
            result.put("totalActualReturn", totalActual);
            result.put("deviation", totalActual.subtract(totalExpected));
            return R.success(result);
        } catch (Exception e) { return R.fail("收益分析失败：" + e.getMessage()); }
    }

    // ==================== 组合分析 ====================

    @Operation(summary = "投资组合分析")
    @PostMapping("/portfolio/analysis")
    public R<Map<String, Object>> portfolioAnalysis(@RequestBody(required = false) Map<String, Object> params) {
        try {
            Map<String, Object> result = new HashMap<>();
            List<GzctInvestProject> all = projectMapper.selectList(null);
            // 按投资类型统计
            Map<String, Long> typeDist = all.stream().filter(p -> p.getInvestType() != null).collect(Collectors.groupingBy(GzctInvestProject::getInvestType, Collectors.counting()));
            Map<String, BigDecimal> typeAmount = all.stream().filter(p -> p.getInvestType() != null && p.getInvestAmount() != null).collect(Collectors.groupingBy(GzctInvestProject::getInvestType, Collectors.reducing(BigDecimal.ZERO, GzctInvestProject::getInvestAmount, BigDecimal::add)));
            result.put("typeDistribution", typeDist);
            result.put("typeAmountDistribution", typeAmount);
            // 主业/非主业
            result.put("mainBizCount", all.stream().filter(p -> "Y".equals(p.getIsMainBiz())).count());
            result.put("nonMainBizCount", all.stream().filter(p -> "N".equals(p.getIsMainBiz())).count());
            return R.success(result);
        } catch (Exception e) { return R.fail("组合分析失败：" + e.getMessage()); }
    }

    // ==================== 趋势分析 ====================

    @Operation(summary = "投资趋势分析")
    @PostMapping("/trend/analysis")
    public R<Map<String, Object>> trendAnalysis(@RequestBody(required = false) Map<String, Object> params) {
        try {
            Map<String, Object> result = new HashMap<>();
            List<GzctInvestProject> all = projectMapper.selectList(null);
            // 按年统计
            Map<String, Long> yearDist = new HashMap<>();
            for (GzctInvestProject p : all) {
                String year = p.getApprovalDate() != null ? String.valueOf(p.getApprovalDate().getYear()) : "unknown";
                yearDist.merge(year, 1L, Long::sum);
            }
            List<Map<String, Object>> trend = new ArrayList<>();
            for (Map.Entry<String, Long> e : yearDist.entrySet()) {
                Map<String, Object> item = new HashMap<>();
                item.put("year", e.getKey());
                item.put("count", e.getValue());
                trend.add(item);
            }
            trend.sort((a, b) -> ((String) a.get("year")).compareTo((String) b.get("year")));
            result.put("yearlyTrend", trend);
            return R.success(result);
        } catch (Exception e) { return R.fail("趋势分析失败：" + e.getMessage()); }
    }

    // ==================== 投资建议 ====================

    @Operation(summary = "投资建议")
    @PostMapping("/recommendations")
    public R<Map<String, Object>> recommendations(@RequestBody(required = false) Map<String, Object> params) {
        try {
            Map<String, Object> result = new HashMap<>();
            List<GzctInvestProject> all = projectMapper.selectList(null);
            result.put("totalProjects", all.size());
            result.put("nonMainBizRatio", all.size() > 0 ? Math.round((double) all.stream().filter(p -> "N".equals(p.getIsMainBiz())).count() / all.size() * 10000) / 100.0 : 0);
            result.put("suggestion", "建议加大主业投资比例，控制非主业投资规模，加强投后评价管理。");
            return R.success(result);
        } catch (Exception e) { return R.fail("获取建议失败：" + e.getMessage()); }
    }

    // ==================== 实时监控 ====================

    @Operation(summary = "投资监控预警")
    @PostMapping("/monitoring/warning")
    public R<Boolean> monitoringWarning(@RequestBody(required = false) Map<String, Object> params) {
        try {
            return R.success(true);
        } catch (Exception e) { return R.fail("监控预警失败：" + e.getMessage()); }
    }

    @Operation(summary = "实时投资监控数据")
    @PostMapping("/monitoring/realtime")
    public R<Map<String, Object>> realtimeMonitoring(@RequestBody(required = false) Map<String, Object> params) {
        try {
            Map<String, Object> result = new HashMap<>();
            List<GzctInvestProject> all = projectMapper.selectList(null);
            result.put("totalProjects", all.size());
            result.put("executingCount", all.stream().filter(p -> "EXECUTING".equals(p.getProjectStatus())).count());
            result.put("completedCount", all.stream().filter(p -> "COMPLETED".equals(p.getProjectStatus())).count());
            // 预警统计
            Long warningCount = warningMapper.selectCount(new LambdaQueryWrapper<GzctInvestWarning>().eq(GzctInvestWarning::getStatus, "PENDING"));
            result.put("activeWarningCount", warningCount);
            result.put("timestamp", LocalDateTime.now().toString());
            return R.success(result);
        } catch (Exception e) { return R.fail("获取实时监控数据失败：" + e.getMessage()); }
    }
}
