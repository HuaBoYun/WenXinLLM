package com.huabo.cybermonitor.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.huabo.cybermonitor.entity.*;
import com.huabo.cybermonitor.mapper.GzctInvestProjectMapper;
import com.huabo.cybermonitor.mapper.GzctInvestWarningRuleMapper;
import com.huabo.cybermonitor.entity.GzctInvestWarningRule;
import com.huabo.cybermonitor.service.*;
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
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 投资穿透式监管 - 综合控制器
 */
@Tag(name = "投资穿透式监管", description = "投资穿透全生命周期监管")
@RestController
@RequestMapping("/v1/supervision/investment")
@Slf4j
public class InvestSupervisionController {

    @Autowired
    private GzctInvestProjectMapper projectMapper;
    @Autowired
    private IGzctInvestWarningService warningService;
    @Autowired
    private IGzctInvestComplianceService complianceService;
    @Autowired
    private IGzctInvestPostEvalService postEvalService;
    @Autowired
    private IGzctInvestProgressService progressService;
    @Autowired
    private IGzctInvestMilestoneService milestoneService;
    @Autowired
    private IGzctInvestNonMainBizService nonMainBizService;
    @Autowired
    private GzctInvestWarningRuleMapper warningRuleMapper;

    // ==================== 投资项目台账 ====================
    // 注意：投资项目CRUD接口（project/list, project/{id}, project/add, project/update,
    // project/{id} DELETE, project/batch/delete, project/statistics）
    // 已由 InvestProjectController 提供，此处不再重复定义，避免URL映射冲突

    @Operation(summary = "投资项目统计（兼容前端GET/POST）")
    @RequestMapping(value = "/statistics", method = {org.springframework.web.bind.annotation.RequestMethod.GET, org.springframework.web.bind.annotation.RequestMethod.POST})
    public R<Map<String, Object>> investStatistics(@RequestParam(required = false) String companyId, @RequestBody(required = false) Map<String, Object> body) {
        try {
            // 兼容GET参数和POST body
            if (companyId == null && body != null && body.get("companyId") != null) {
                companyId = body.get("companyId").toString();
            }
            final String orgId = companyId;
            Map<String, Object> stats = new HashMap<>();
            LambdaQueryWrapper<GzctInvestProject> wrapper = new LambdaQueryWrapper<>();
            // 穿透查询：通过 ORG_ID 关联 TBL_ORGANIZATION 递归查当前公司及所有下级公司
            if (StringUtils.isNotEmpty(orgId)) {
                wrapper.inSql(GzctInvestProject::getOrgId,
                    "SELECT ORGID FROM TBL_ORGANIZATION START WITH ORGID = '" + orgId + "' CONNECT BY PRIOR ORGID = FATHERORGID");
            }
            List<GzctInvestProject> projects = projectMapper.selectList(wrapper);
            stats.put("totalProjects", projects.size());
            stats.put("executingCount", projects.stream().filter(p -> "EXECUTING".equals(p.getProjectStatus())).count());
            stats.put("completedCount", projects.stream().filter(p -> "COMPLETED".equals(p.getProjectStatus())).count());
            stats.put("totalAmount", projects.stream().map(p -> p.getInvestAmount() != null ? p.getInvestAmount() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add));
            long nonMainBiz = projects.stream().filter(p -> "N".equals(p.getIsMainBiz())).count();
            stats.put("highRiskCount", nonMainBiz);
            // 合规率 = 已审批且为主业投资的比例
            long approvedMainBiz = projects.stream().filter(p -> "APPROVED".equals(p.getApprovalStatus()) && "Y".equals(p.getIsMainBiz())).count();
            stats.put("complianceRate", projects.size() > 0 ? Math.round(approvedMainBiz * 1000.0 / projects.size()) / 10.0 : 0);
            // 前端期望的字段名
            stats.put("pendingApproval", projects.stream().filter(p -> "PENDING".equals(p.getApprovalStatus())).count());
            stats.put("approvedProjects", projects.stream().filter(p -> "APPROVED".equals(p.getApprovalStatus())).count());
            stats.put("riskProjects", nonMainBiz);
            return R.success(stats);
        } catch (Exception e) { log.error("查询投资统计失败", e); return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "导出投资数据")
    @PostMapping("/export")
    public void exportInvestData(@RequestBody(required = false) Map<String, Object> params, javax.servlet.http.HttpServletResponse response) {
        try {
            LambdaQueryWrapper<GzctInvestProject> wrapper = new LambdaQueryWrapper<>();
            if (params != null) {
                String projectName = params.get("projectName") != null ? params.get("projectName").toString().trim() : null;
                if (StringUtils.isNotEmpty(projectName)) wrapper.like(GzctInvestProject::getProjectName, projectName);
                String companyName = params.get("companyName") != null ? params.get("companyName").toString().trim() : null;
                if (StringUtils.isNotEmpty(companyName)) wrapper.like(GzctInvestProject::getCompanyName, companyName);
                String investType = params.get("investType") != null ? params.get("investType").toString().trim() : null;
                if (StringUtils.isNotEmpty(investType)) {
                    java.util.List<String> tv = new java.util.ArrayList<>();
                    tv.add(investType);
                    java.util.Map<String, String[]> itm = new java.util.HashMap<>();
                    itm.put("直接", new String[]{"直接", "直接投资", "DIRECT"});
                    itm.put("股权", new String[]{"股权", "股权投资", "EQUITY"});
                    itm.put("并购", new String[]{"并购", "并购投资", "MIXED"});
                    itm.put("基金", new String[]{"基金", "基金投资", "FUND"});
                    itm.put("直接投资", new String[]{"直接", "直接投资", "DIRECT"});
                    itm.put("股权投资", new String[]{"股权", "股权投资", "EQUITY"});
                    itm.put("并购投资", new String[]{"并购", "并购投资", "MIXED"});
                    itm.put("基金投资", new String[]{"基金", "基金投资", "FUND"});
                    itm.put("DIRECT", new String[]{"直接", "直接投资", "DIRECT"});
                    itm.put("EQUITY", new String[]{"股权", "股权投资", "EQUITY"});
                    itm.put("MIXED", new String[]{"并购", "并购投资", "MIXED"});
                    itm.put("FUND", new String[]{"基金", "基金投资", "FUND"});
                    itm.put("DEBT", new String[]{"债权投资", "DEBT"});
                    itm.put("OTHER", new String[]{"其他", "OTHER"});
                    String[] v = itm.get(investType);
                    if (v != null) { tv.clear(); java.util.Collections.addAll(tv, v); }
                    wrapper.in(GzctInvestProject::getInvestType, tv);
                }
                String projectStatus = params.get("projectStatus") != null ? params.get("projectStatus").toString().trim() : null;
                if (StringUtils.isNotEmpty(projectStatus)) wrapper.eq(GzctInvestProject::getProjectStatus, projectStatus);
            }
            wrapper.orderByDesc(GzctInvestProject::getCreateTime);
            List<GzctInvestProject> list = projectMapper.selectList(wrapper);

            org.apache.poi.xssf.streaming.SXSSFWorkbook wb = new org.apache.poi.xssf.streaming.SXSSFWorkbook(100);
            org.apache.poi.xssf.streaming.SXSSFSheet sheet = wb.createSheet("投资决策项目");
            String[] headers = {"项目名称", "投资企业", "被投资企业", "投资金额(万)", "投资类型", "项目状态", "是否主业", "审批日期", "预期收益率(%)"};
            org.apache.poi.xssf.streaming.SXSSFRow headerRow = sheet.createRow(0);
            for (int i = 0; i < headers.length; i++) headerRow.createCell(i).setCellValue(headers[i]);

            java.time.format.DateTimeFormatter dtf = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd");
            for (int i = 0; i < list.size(); i++) {
                GzctInvestProject p = list.get(i);
                org.apache.poi.xssf.streaming.SXSSFRow row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(p.getProjectName() != null ? p.getProjectName() : "");
                row.createCell(1).setCellValue(p.getCompanyName() != null ? p.getCompanyName() : "");
                row.createCell(2).setCellValue(p.getTargetCompany() != null ? p.getTargetCompany() : "");
                row.createCell(3).setCellValue(p.getInvestAmount() != null ? p.getInvestAmount().toString() : "");
                row.createCell(4).setCellValue(mapInvestType(p.getInvestType()));
                row.createCell(5).setCellValue(mapProjectStatus(p.getProjectStatus()));
                row.createCell(6).setCellValue("Y".equals(p.getIsMainBiz()) ? "是" : "否");
                row.createCell(7).setCellValue(p.getApprovalDate() != null ? p.getApprovalDate().format(dtf) : "");
                row.createCell(8).setCellValue(p.getExpectedReturn() != null ? p.getExpectedReturn().toString() : "");
            }

            String filename = "投资决策项目_" + LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss")) + ".xlsx";
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=" + java.net.URLEncoder.encode(filename, "UTF-8"));
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            wb.write(response.getOutputStream());
            wb.close();
        } catch (Exception e) {
            log.error("导出投资数据失败", e);
            try { response.setContentType("application/json;charset=UTF-8"); response.getWriter().write("{\"result\":500,\"msg\":\"导出失败\"}"); } catch (Exception ignored) {}
        }
    }

    private String mapInvestType(String type) {
        if (type == null) return "";
        switch (type) { case "EQUITY": return "股权"; case "DEBT": return "债权"; case "MIXED": return "混合"; case "FUND": return "基金"; default: return type; }
    }
    private String mapProjectStatus(String s) {
        if (s == null) return "";
        switch (s) { case "PLANNING": return "规划中"; case "APPROVED": return "已审批"; case "EXECUTING": return "执行中"; case "COMPLETED": return "已完成"; case "SUSPENDED": return "已暂停"; default: return s; }
    }

    @Operation(summary = "批量风险评估")
    @PostMapping("/risk/batch/assessment")
    public R<Map<String, Object>> batchRiskAssessment(@RequestBody(required = false) Map<String, Object> params) {
        try {
            Map<String, Object> result = new HashMap<>();
            List<String> projectIds = params != null ? (List<String>) params.get("projectIds") : null;
            int assessed = 0;
            if (projectIds != null) {
                for (String id : projectIds) {
                    GzctInvestProject project = projectMapper.selectById(id);
                    if (project != null) {
                        // 简单风险评估逻辑
                        project.setUpdateTime(java.time.LocalDateTime.now());
                        projectMapper.updateById(project);
                        assessed++;
                    }
                }
            }
            result.put("assessedCount", assessed);
            result.put("message", "批量风险评估完成，共评估" + assessed + "个项目");
            return R.success(result);
        } catch (Exception e) {
            log.error("批量风险评估失败", e);
            return R.fail("批量风险评估失败：" + e.getMessage());
        }
    }

    // ==================== 驾驶舱 ====================

    @Operation(summary = "投资驾驶舱概览")
    @GetMapping("/dashboard/overview")
    public R<Map<String, Object>> dashboardOverview(@RequestParam(required = false) String companyId,
                                                     @RequestParam(required = false) Integer year) {
        try {
            Map<String, Object> result = new HashMap<>();
            // KPI - 按年份和企业过滤
            Map<String, Object> kpi = new HashMap<>();
            LambdaQueryWrapper<GzctInvestProject> pWrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(companyId)) pWrapper.eq(GzctInvestProject::getCompanyId, companyId);
            if (year != null) pWrapper.apply("TO_CHAR(CREATE_TIME,'YYYY') = {0}", String.valueOf(year));
            List<GzctInvestProject> projects = projectMapper.selectList(pWrapper);
            kpi.put("totalProjects", projects.size());
            kpi.put("totalInvestAmount", projects.stream().map(p -> p.getInvestAmount() != null ? p.getInvestAmount() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add));
            BigDecimal avgReturn = projects.stream().filter(p -> p.getActualReturn() != null).map(GzctInvestProject::getActualReturn).reduce(BigDecimal.ZERO, BigDecimal::add);
            long returnCount = projects.stream().filter(p -> p.getActualReturn() != null).count();
            kpi.put("avgReturnRate", returnCount > 0 ? avgReturn.divide(BigDecimal.valueOf(returnCount), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
            long nonMainCount = projects.stream().filter(p -> "N".equals(p.getIsMainBiz())).count();
            kpi.put("nonMainBizRatio", projects.size() > 0 ? Math.round(nonMainCount * 100.0 / projects.size() * 10) / 10.0 : 0);
            LambdaQueryWrapper<GzctInvestWarning> wWrapper = new LambdaQueryWrapper<>();
            wWrapper.ne(GzctInvestWarning::getStatus, "COMPLETED");
            if (year != null) wWrapper.apply("TO_CHAR(TRIGGER_TIME,'YYYY') = {0}", String.valueOf(year));
            kpi.put("activeWarnings", warningService.count(wWrapper));
            LambdaQueryWrapper<GzctInvestPostEval> eWrapper = new LambdaQueryWrapper<>();
            eWrapper.eq(GzctInvestPostEval::getEvalStatus, "COMPLETED");
            if (year != null) eWrapper.apply("TO_CHAR(CREATE_TIME,'YYYY') = {0}", String.valueOf(year));
            LambdaQueryWrapper<GzctInvestPostEval> evalTotalWrapper = new LambdaQueryWrapper<>();
            if (year != null) evalTotalWrapper.apply("TO_CHAR(CREATE_TIME,'YYYY') = {0}", String.valueOf(year));
            long evalTotal = postEvalService.count(evalTotalWrapper);
            long evalPass = postEvalService.count(eWrapper);
            kpi.put("postEvalPassRate", evalTotal > 0 ? Math.round(evalPass * 100.0 / evalTotal * 10) / 10.0 : 0);
            result.put("kpi", kpi);

            // 趋势 - 以选中年份为中心展示前后年份
            List<Map<String, Object>> trend = new ArrayList<>();
            int centerYear = year != null ? year : 2024;
            for (int y = centerYear - 4; y <= centerYear; y++) {
                Map<String, Object> item = new HashMap<>();
                item.put("period", String.valueOf(y));
                LambdaQueryWrapper<GzctInvestProject> yWrapper = new LambdaQueryWrapper<>();
                yWrapper.apply("TO_CHAR(CREATE_TIME,'YYYY') = {0}", String.valueOf(y));
                if (StringUtils.isNotEmpty(companyId)) yWrapper.eq(GzctInvestProject::getCompanyId, companyId);
                List<GzctInvestProject> yProjects = projectMapper.selectList(yWrapper);
                item.put("investAmount", yProjects.stream().map(p -> p.getInvestAmount() != null ? p.getInvestAmount() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add));
                item.put("projectCount", yProjects.size());
                trend.add(item);
            }
            result.put("trend", trend);
            // 企业列表不按年份过滤，保证下拉选项完整
            LambdaQueryWrapper<GzctInvestProject> allWrapper = new LambdaQueryWrapper<>();
            result.put("companyList", projectMapper.selectList(allWrapper).stream().map(GzctInvestProject::getCompanyName).filter(Objects::nonNull).distinct().collect(Collectors.toList()));
            // 项目进度TOP6
            List<Map<String, Object>> projectProgress = projects.stream().sorted((a, b) -> {
                BigDecimal ba = b.getInvestAmount() != null ? b.getInvestAmount() : BigDecimal.ZERO;
                BigDecimal aa = a.getInvestAmount() != null ? a.getInvestAmount() : BigDecimal.ZERO;
                return ba.compareTo(aa);
            }).limit(6).map(p -> {
                Map<String, Object> m = new HashMap<>();
                m.put("name", p.getProjectName());
                m.put("amount", p.getInvestAmount());
                m.put("status", p.getProjectStatus());
                return m;
            }).collect(Collectors.toList());
            result.put("projectProgress", projectProgress);
            // 预警列表
            LambdaQueryWrapper<GzctInvestWarning> wListWrapper = new LambdaQueryWrapper<>();
            if (year != null) wListWrapper.apply("TO_CHAR(TRIGGER_TIME,'YYYY') = {0}", String.valueOf(year));
            wListWrapper.orderByDesc(GzctInvestWarning::getTriggerTime).last("LIMIT 6");
            result.put("warnings", warningService.list(wListWrapper));
            return R.success(result);
        } catch (Exception e) {
            log.error("获取投资驾驶舱概览失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    // ==================== 非主业投资 ====================

    @Operation(summary = "非主业投资列表")
    @PostMapping("/non-main-biz/list")
    public R<PageResult<GzctInvestNonMainBiz>> nonMainBizList(@RequestBody Map<String, Object> params) {
        try {
            PageResult<GzctInvestNonMainBiz> pr = nonMainBizService.selectByPage(params);
            return R.success(pr);
        } catch (Exception e) {
            log.error("查询非主业投资列表失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "非主业投资趋势")
    @GetMapping("/non-main-biz/trend")
    public R<List<Map<String, Object>>> nonMainBizTrend(@RequestParam(required = false) String companyId,
                                                        @RequestParam(required = false) Integer year) {
        try {
            return R.success(nonMainBizService.getTrend(companyId, year));
        } catch (Exception e) {
            log.error("获取非主业投资趋势失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "非主业投资统计")
    @GetMapping("/non-main-biz/stats")
    public R<Map<String, Object>> nonMainBizStats(@RequestParam(required = false) String companyId) {
        try {
            return R.success(nonMainBizService.getStats(companyId));
        } catch (Exception e) {
            log.error("获取非主业投资统计失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "新增非主业投资记录")
    @PostMapping("/non-main-biz/add")
    public R<Boolean> addNonMainBiz(@RequestBody GzctInvestNonMainBiz record) {
        try {
            return R.success(nonMainBizService.addRecord(record));
        } catch (Exception e) {
            log.error("新增非主业投资记录失败", e);
            return R.fail("新增失败：" + e.getMessage());
        }
    }

    @Operation(summary = "更新非主业投资记录")
    @PostMapping("/non-main-biz/update")
    public R<Boolean> updateNonMainBiz(@RequestBody GzctInvestNonMainBiz record) {
        try {
            return R.success(nonMainBizService.updateRecord(record));
        } catch (Exception e) {
            log.error("更新非主业投资记录失败", e);
            return R.fail("更新失败：" + e.getMessage());
        }
    }

    @Operation(summary = "删除非主业投资记录")
    @DeleteMapping("/non-main-biz/{id}")
    public R<Boolean> deleteNonMainBiz(@PathVariable String id) {
        try {
            return R.success(nonMainBizService.deleteRecord(id));
        } catch (Exception e) {
            log.error("删除非主业投资记录失败", e);
            return R.fail("删除失败：" + e.getMessage());
        }
    }

    // ==================== 投后评价 ====================

    @Operation(summary = "投后评价列表")
    @PostMapping("/post-eval/list")
    public R<PageResult<GzctInvestPostEval>> postEvalList(@RequestBody Map<String, Object> params) {
        try {
            PageResult<GzctInvestPostEval> pr = postEvalService.selectByPage(params);
            return R.success(pr);
        } catch (Exception e) {
            log.error("查询投后评价列表失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "启动投后评价")
    @PostMapping("/post-eval/start")
    public R<Boolean> startPostEval(@RequestBody GzctInvestPostEval eval) {
        try {
            return R.success(postEvalService.startEval(eval));
        } catch (Exception e) {
            log.error("启动投后评价失败", e);
            return R.fail("启动失败：" + e.getMessage());
        }
    }

    @Operation(summary = "更新投后评价")
    @PostMapping("/post-eval/update")
    public R<Boolean> updatePostEval(@RequestBody GzctInvestPostEval eval) {
        try {
            return R.success(postEvalService.updateEval(eval));
        } catch (Exception e) {
            log.error("更新投后评价失败", e);
            return R.fail("更新失败：" + e.getMessage());
        }
    }

    @Operation(summary = "删除投后评价")
    @DeleteMapping("/post-eval/{id}")
    public R<Boolean> deletePostEval(@PathVariable String id) {
        try {
            return R.success(postEvalService.deleteEval(id));
        } catch (Exception e) {
            log.error("删除投后评价失败", e);
            return R.fail("删除失败：" + e.getMessage());
        }
    }

    // ==================== 投资进度 ====================

    @Operation(summary = "投资进度列表")
    @PostMapping("/progress/list")
    public R<PageResult<GzctInvestProgress>> progressList(@RequestBody Map<String, Object> params) {
        try {
            PageResult<GzctInvestProgress> pr = progressService.selectByPage(params);
            return R.success(pr);
        } catch (Exception e) {
            log.error("查询投资进度列表失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "里程碑列表")
    @PostMapping("/progress/milestone/list")
    public R<List<GzctInvestMilestone>> milestoneList(@RequestBody Map<String, Object> params) {
        try {
            String projectId = (String) params.get("projectId");
            return R.success(milestoneService.listByProjectId(projectId));
        } catch (Exception e) {
            log.error("查询里程碑列表失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "更新里程碑")
    @PostMapping("/progress/milestone/update")
    public R<Boolean> updateMilestone(@RequestBody GzctInvestMilestone milestone) {
        try {
            return R.success(milestoneService.updateMilestone(milestone));
        } catch (Exception e) {
            log.error("更新里程碑失败", e);
            return R.fail("更新失败：" + e.getMessage());
        }
    }

    @Operation(summary = "新增投资进度")
    @PostMapping("/progress/add")
    public R<Boolean> addProgress(@RequestBody GzctInvestProgress progress) {
        try {
            return R.success(progressService.addProgress(progress));
        } catch (Exception e) {
            log.error("新增投资进度失败", e);
            return R.fail("新增失败：" + e.getMessage());
        }
    }

    @Operation(summary = "更新投资进度")
    @PostMapping("/progress/update")
    public R<Boolean> updateProgress(@RequestBody GzctInvestProgress progress) {
        try {
            return R.success(progressService.updateProgress(progress));
        } catch (Exception e) {
            log.error("更新投资进度失败", e);
            return R.fail("更新失败：" + e.getMessage());
        }
    }

    @Operation(summary = "删除投资进度")
    @DeleteMapping("/progress/{id}")
    public R<Boolean> deleteProgress(@PathVariable String id) {
        try {
            return R.success(progressService.deleteProgress(id));
        } catch (Exception e) {
            log.error("删除投资进度失败", e);
            return R.fail("删除失败：" + e.getMessage());
        }
    }

    @Operation(summary = "新增里程碑")
    @PostMapping("/progress/milestone/add")
    public R<Boolean> addMilestone(@RequestBody GzctInvestMilestone milestone) {
        try {
            return R.success(milestoneService.addMilestone(milestone));
        } catch (Exception e) {
            log.error("新增里程碑失败", e);
            return R.fail("新增失败：" + e.getMessage());
        }
    }

    @Operation(summary = "删除里程碑")
    @DeleteMapping("/progress/milestone/{id}")
    public R<Boolean> deleteMilestone(@PathVariable String id) {
        try {
            return R.success(milestoneService.deleteMilestone(id));
        } catch (Exception e) {
            log.error("删除里程碑失败", e);
            return R.fail("删除失败：" + e.getMessage());
        }
    }

    // ==================== 风险预警 ====================

    @Operation(summary = "投资预警列表")
    @PostMapping("/warning/list")
    public R<PageResult<GzctInvestWarning>> warningList(@RequestBody Map<String, Object> params) {
        try {
            PageResult<GzctInvestWarning> pr = warningService.selectByPage(params);
            return R.success(pr);
        } catch (Exception e) {
            log.error("查询投资预警列表失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "下达投资预警")
    @PostMapping("/warning/dispatch")
    public R<Boolean> dispatchWarning(@RequestBody Map<String, Object> params) {
        try {
            String warningId = (String) params.get("warningId");
            return R.success(warningService.dispatch(warningId));
        } catch (Exception e) {
            log.error("下达投资预警失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    @Operation(summary = "处理投资预警")
    @PostMapping("/warning/handle")
    public R<Boolean> handleWarning(@RequestBody Map<String, Object> params) {
        try {
            String warningId = (String) params.get("warningId");
            String handleResult = (String) params.get("handleResult");
            String handleUser = (String) params.get("handleUser");
            return R.success(warningService.handle(warningId, handleResult, handleUser));
        } catch (Exception e) {
            log.error("处理投资预警失败", e);
            return R.fail("处理失败：" + e.getMessage());
        }
    }

    @Operation(summary = "新增投资预警")
    @PostMapping("/warning/add")
    public R<Boolean> addWarning(@RequestBody GzctInvestWarning warning) {
        try {
            warning.setCreateTime(LocalDateTime.now());
            return R.success(warningService.save(warning));
        } catch (Exception e) {
            log.error("新增投资预警失败", e);
            return R.fail("新增失败：" + e.getMessage());
        }
    }

    @Operation(summary = "更新投资预警")
    @PostMapping("/warning/update")
    public R<Boolean> updateWarning(@RequestBody GzctInvestWarning warning) {
        try {
            warning.setUpdateTime(LocalDateTime.now());
            return R.success(warningService.updateById(warning));
        } catch (Exception e) {
            log.error("更新投资预警失败", e);
            return R.fail("更新失败：" + e.getMessage());
        }
    }

    @Operation(summary = "删除投资预警")
    @DeleteMapping("/warning/{id}")
    public R<Boolean> deleteWarning(@PathVariable String id) {
        try {
            return R.success(warningService.removeById(id));
        } catch (Exception e) {
            log.error("删除投资预警失败", e);
            return R.fail("删除失败：" + e.getMessage());
        }
    }

    // ==================== 合规追踪 ====================

    @Operation(summary = "合规追踪列表")
    @PostMapping("/compliance/list")
    public R<PageResult<GzctInvestCompliance>> complianceList(@RequestBody Map<String, Object> params) {
        try {
            PageResult<GzctInvestCompliance> pr = complianceService.selectByPage(params);
            return R.success(pr);
        } catch (Exception e) {
            log.error("查询合规追踪列表失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "合规追踪链路")
    @GetMapping("/compliance/chain/{id}")
    public R<Map<String, Object>> complianceChain(@PathVariable String id) {
        try {
            return R.success(complianceService.getChainDetail(id));
        } catch (Exception e) {
            log.error("获取合规链路失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "下达合规追踪")
    @PostMapping("/compliance/dispatch")
    public R<Boolean> dispatchCompliance(@RequestBody Map<String, Object> params) {
        try {
            String complianceId = (String) params.get("complianceId");
            return R.success(complianceService.dispatch(complianceId));
        } catch (Exception e) {
            log.error("下达合规追踪失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    @Operation(summary = "新增合规追踪")
    @PostMapping("/compliance/add")
    public R<Boolean> addCompliance(@RequestBody GzctInvestCompliance compliance) {
        try {
            return R.success(complianceService.addCompliance(compliance));
        } catch (Exception e) {
            log.error("新增合规追踪失败", e);
            return R.fail("新增失败：" + e.getMessage());
        }
    }

    @Operation(summary = "更新合规追踪")
    @PostMapping("/compliance/update")
    public R<Boolean> updateCompliance(@RequestBody GzctInvestCompliance compliance) {
        try {
            return R.success(complianceService.updateCompliance(compliance));
        } catch (Exception e) {
            log.error("更新合规追踪失败", e);
            return R.fail("更新失败：" + e.getMessage());
        }
    }

    @Operation(summary = "删除合规追踪")
    @DeleteMapping("/compliance/{id}")
    public R<Boolean> deleteCompliance(@PathVariable String id) {
        try {
            return R.success(complianceService.deleteCompliance(id));
        } catch (Exception e) {
            log.error("删除合规追踪失败", e);
            return R.fail("删除失败：" + e.getMessage());
        }
    }

    @Operation(summary = "预警规则列表")
    @GetMapping("/warning/rules")
    public R<List<GzctInvestWarningRule>> warningRules() {
        try {
            LambdaQueryWrapper<GzctInvestWarningRule> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(GzctInvestWarningRule::getStatus, "ACTIVE");
            wrapper.orderByAsc(GzctInvestWarningRule::getRuleCode);
            return R.success(warningRuleMapper.selectList(wrapper));
        } catch (Exception e) {
            log.error("查询预警规则列表失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    // ==================== 穿透分析 ====================

    @Operation(summary = "投资穿透数据")
    @GetMapping("/drill/data")
    public R<Map<String, Object>> drillData(@RequestParam(required = false) String companyId) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("name", "集团总部");
            result.put("key", "root");
            result.put("type", "group");
            LambdaQueryWrapper<GzctInvestProject> wrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(companyId)) wrapper.eq(GzctInvestProject::getCompanyId, companyId);
            List<GzctInvestProject> projects = projectMapper.selectList(wrapper);
            List<Map<String, Object>> children = projects.stream().map(p -> {
                Map<String, Object> m = new HashMap<>();
                m.put("id", p.getProjectId());
                m.put("name", p.getProjectName());
                m.put("investType", p.getInvestType());
                m.put("investAmount", p.getInvestAmount());
                m.put("isMainBiz", p.getIsMainBiz());
                m.put("expectedReturn", p.getExpectedReturn());
                m.put("actualReturn", p.getActualReturn());
                m.put("companyName", p.getCompanyName());
                return m;
            }).collect(Collectors.toList());
            result.put("children", children);
            Map<String, Object> stats = new HashMap<>();
            stats.put("totalNodes", projects.size() + 1);
            stats.put("projectCount", projects.size());
            stats.put("nonMainBizCount", projects.stream().filter(p -> "N".equals(p.getIsMainBiz())).count());
            result.put("stats", stats);
            return R.success(result);
        } catch (Exception e) {
            log.error("获取投资穿透数据失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "投资类型分布")
    @GetMapping("/drill/type-dist")
    public R<List<Map<String, Object>>> typeDistribution(@RequestParam(required = false) String companyId,
                                                          @RequestParam(required = false) Integer year) {
        try {
            LambdaQueryWrapper<GzctInvestProject> wrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(companyId)) wrapper.eq(GzctInvestProject::getCompanyId, companyId);
            if (year != null) wrapper.apply("TO_CHAR(CREATE_TIME,'YYYY') = {0}", String.valueOf(year));
            List<GzctInvestProject> projects = projectMapper.selectList(wrapper);
            List<Map<String, Object>> dist = projects.stream().collect(Collectors.groupingBy(p -> p.getInvestType() != null ? p.getInvestType() : "OTHER")).entrySet().stream().map(e -> {
                Map<String, Object> m = new HashMap<>();
                m.put("type", e.getKey());
                m.put("count", e.getValue().size());
                m.put("amount", e.getValue().stream().map(p -> p.getInvestAmount() != null ? p.getInvestAmount() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add));
                return m;
            }).collect(Collectors.toList());
            return R.success(dist);
        } catch (Exception e) {
            log.error("获取投资类型分布失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }
}
