package com.huabo.cybermonitor.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.cybermonitor.entity.*;
import com.huabo.cybermonitor.mapper.*;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.util.R;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import io.swagger.v3.oas.annotations.Operation;

@Tag(name = "企业财务管理", description = "财务报表、预算、成本、预警、报告")
@RestController
@RequestMapping("/v1/enterprise/financial")
@Slf4j
public class EnterpriseFinancialController {

    @Autowired private GzctEnterpriseFinancialMapper financialMapper;
    @Autowired private GzctEnterpriseBudgetMapper budgetMapper;
    @Autowired private GzctEnterpriseCostMapper costMapper;
    @Autowired private GzctFinancialWarningMapper warningMapper;
    @Autowired private GzctFinancialReportMapper reportMapper;

    // ==================== 财务报表 ====================
    @Operation(summary = "statementList")
    @PostMapping("/statement/list")
    public R<PageResult<GzctEnterpriseFinancial>> statementList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctEnterpriseFinancial> w = new LambdaQueryWrapper<>();
            if (params.get("enterpriseId") != null && StringUtils.isNotBlank(params.get("enterpriseId").toString()) && !"ALL".equals(params.get("enterpriseId").toString())) w.eq(GzctEnterpriseFinancial::getEnterpriseId, params.get("enterpriseId").toString());
            if (params.get("reportType") != null && StringUtils.isNotBlank(params.get("reportType").toString())) w.eq(GzctEnterpriseFinancial::getReportType, params.get("reportType").toString());
            if (params.get("reportPeriod") != null && StringUtils.isNotBlank(params.get("reportPeriod").toString())) w.eq(GzctEnterpriseFinancial::getReportPeriod, params.get("reportPeriod").toString());
            if (params.get("reportYear") != null && StringUtils.isNotBlank(params.get("reportYear").toString())) w.eq(GzctEnterpriseFinancial::getReportYear, params.get("reportYear").toString());
            if (params.get("status") != null && StringUtils.isNotBlank(params.get("status").toString())) w.eq(GzctEnterpriseFinancial::getStatus, params.get("status").toString());
            w.orderByDesc(GzctEnterpriseFinancial::getCreateTime);
            Page<GzctEnterpriseFinancial> r = new GzctEnterpriseFinancial().selectPage(new Page<>(pn, ps), w);
            PageResult<GzctEnterpriseFinancial> pr = new PageResult<>();
            pr.setTotalRecord((int) r.getTotal()); pr.setCurrentPage((int) r.getCurrent()); pr.setPageNumber((int) r.getCurrent()); pr.setTotalPage((int) r.getPages()); pr.setPageSize((int) r.getSize()); pr.setTlist(r.getRecords());
            return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }
    @Operation(summary = "statementDetail")
    @GetMapping("/statement/{id}")
    public R<GzctEnterpriseFinancial> statementDetail(@PathVariable String id) { try { return R.success(financialMapper.selectById(id)); } catch (Exception e) { return R.fail("查询失败"); } }
    @Operation(summary = "新增")
    @PostMapping("/statement")
    public R<Boolean> addStatement(@RequestBody GzctEnterpriseFinancial record) { try { record.setCreateTime(LocalDateTime.now()); record.setStatus("DRAFT"); financialMapper.insert(record); return R.success(true); } catch (Exception e) { return R.fail("新增失败：" + e.getMessage()); } }
    @Operation(summary = "更新")
    @PutMapping("/statement")
    public R<Boolean> updateStatement(@RequestBody GzctEnterpriseFinancial record) { try { record.setUpdateTime(LocalDateTime.now()); financialMapper.updateById(record); return R.success(true); } catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); } }
    @Operation(summary = "删除")
    @DeleteMapping("/statement/{id}")
    public R<Boolean> deleteStatement(@PathVariable String id) { try { return R.success(financialMapper.deleteById(id) > 0); } catch (Exception e) { return R.fail("删除失败"); } }
    @Operation(summary = "提交")
    @PostMapping("/statement/submit")
    public R<Boolean> submitStatement(@RequestBody Map<String, Object> params) { try { String id = (String) params.get("id"); GzctEnterpriseFinancial r = financialMapper.selectById(id); if (r != null) { r.setStatus("SUBMITTED"); r.setSubmitTime(LocalDateTime.now()); r.setUpdateTime(LocalDateTime.now()); financialMapper.updateById(r); } return R.success(true); } catch (Exception e) { return R.fail("提交失败：" + e.getMessage()); } }
    @Operation(summary = "审批")
    @PostMapping("/statement/audit")
    public R<Boolean> auditStatement(@RequestBody Map<String, Object> params) { try { String id = (String) params.get("id"); GzctEnterpriseFinancial r = financialMapper.selectById(id); if (r != null) { r.setStatus("AUDITED"); r.setAuditTime(LocalDateTime.now()); r.setUpdateTime(LocalDateTime.now()); financialMapper.updateById(r); } return R.success(true); } catch (Exception e) { return R.fail("审核失败：" + e.getMessage()); } }
    @Operation(summary = "查询数据")
    @GetMapping("/statement/template/{templateType}")
    public R<String> getStatementTemplate(@PathVariable String templateType) { return R.success("template_" + templateType); }
    @Operation(summary = "导出")
    @PostMapping("/export-statement")
    public void exportStatement(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        try {
            LambdaQueryWrapper<GzctEnterpriseFinancial> w = new LambdaQueryWrapper<>();
            if (params.get("enterpriseId") != null && StringUtils.isNotBlank(params.get("enterpriseId").toString()) && !"ALL".equals(params.get("enterpriseId").toString())) {
                w.eq(GzctEnterpriseFinancial::getEnterpriseId, params.get("enterpriseId").toString());
            }
            if (params.get("id") != null && StringUtils.isNotBlank(params.get("id").toString())) {
                w.eq(GzctEnterpriseFinancial::getId, params.get("id").toString());
            }
            w.orderByDesc(GzctEnterpriseFinancial::getCreateTime);
            List<GzctEnterpriseFinancial> list = financialMapper.selectList(w);

            // 构建二维表数据
            List<List<Object>> rows = new ArrayList<>();
            rows.add(java.util.Arrays.asList("企业名称","报表类型","报告期间","报告年度","总资产","总负债","净资产","营业收入","净利润","现金流","状态"));
            for (GzctEnterpriseFinancial item : list) {
                rows.add(java.util.Arrays.asList(
                    item.getEnterpriseName() != null ? item.getEnterpriseName() : "",
                    item.getReportType() != null ? item.getReportType() : "",
                    item.getReportPeriod() != null ? item.getReportPeriod() : "",
                    item.getReportYear() != null ? item.getReportYear() : "",
                    item.getTotalAssets() != null ? item.getTotalAssets().toString() : "0",
                    item.getTotalLiabilities() != null ? item.getTotalLiabilities().toString() : "0",
                    item.getNetAssets() != null ? item.getNetAssets().toString() : "0",
                    item.getOperatingRevenue() != null ? item.getOperatingRevenue().toString() : "0",
                    item.getNetProfit() != null ? item.getNetProfit().toString() : "0",
                    item.getCashFlow() != null ? item.getCashFlow().toString() : "0",
                    item.getStatus() != null ? item.getStatus() : ""
                ));
            }

            byte[] xlsxBytes = com.huabo.cybermonitor.util.SimpleXlsxWriter.write("财务报表", rows);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=financial_" + System.currentTimeMillis() + ".xlsx");
            response.getOutputStream().write(xlsxBytes);
            response.getOutputStream().flush();
        } catch (Exception e) {
            log.error("导出失败", e);
            try {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":500,\"msg\":\"导出失败：" + e.getMessage() + "\"}");
            } catch (Exception ex) { log.error("写入错误响应失败", ex); }
        }
    }

    @Operation(summary = "报表版本历史")
    @GetMapping("/statement/history/{id}")
    public R<List<Map<String, Object>>> statementHistory(@PathVariable String id) {
        try {
            GzctEnterpriseFinancial record = financialMapper.selectById(id);
            if (record == null) {
                return R.fail("报表不存在");
            }
            List<Map<String, Object>> historyList = new ArrayList<>();
            // 创建记录
            if (record.getCreateTime() != null) {
                Map<String, Object> item = new HashMap<>();
                item.put("action", "CREATE");
                item.put("actionText", "创建报表");
                item.put("time", record.getCreateTime().toString());
                item.put("description", "创建" + (record.getReportType() != null ? record.getReportType() : "") + "报表，报告年度：" + (record.getReportYear() != null ? record.getReportYear() : ""));
                item.put("status", "DRAFT");
                historyList.add(item);
            }
            // 更新记录
            if (record.getUpdateTime() != null && !record.getUpdateTime().equals(record.getCreateTime())) {
                Map<String, Object> item = new HashMap<>();
                item.put("action", "UPDATE");
                item.put("actionText", "更新报表");
                item.put("time", record.getUpdateTime().toString());
                item.put("description", "报表数据已更新");
                item.put("status", record.getStatus());
                historyList.add(item);
            }
            // 提交记录
            if (record.getSubmitTime() != null) {
                Map<String, Object> item = new HashMap<>();
                item.put("action", "SUBMIT");
                item.put("actionText", "提交审核");
                item.put("time", record.getSubmitTime().toString());
                item.put("description", "报表已提交审核");
                item.put("status", "SUBMITTED");
                historyList.add(item);
            }
            // 审核记录
            if (record.getAuditTime() != null) {
                Map<String, Object> item = new HashMap<>();
                item.put("action", "AUDIT");
                item.put("actionText", "审核通过");
                item.put("time", record.getAuditTime().toString());
                item.put("description", "报表审核通过");
                item.put("status", "AUDITED");
                historyList.add(item);
            }
            // 按时间倒序排列
            historyList.sort((a, b) -> b.get("time").toString().compareTo(a.get("time").toString()));
            return R.success(historyList);
        } catch (Exception e) {
            log.error("查询版本历史失败", e);
            return R.fail("查询版本历史失败：" + e.getMessage());
        }
    }

    // ==================== 成本核算 ====================
    @Operation(summary = "costList")
    @PostMapping("/cost/list")
    public R<PageResult<GzctEnterpriseCost>> costList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctEnterpriseCost> w = new LambdaQueryWrapper<>();
            if (params.get("enterpriseId") != null && StringUtils.isNotBlank(params.get("enterpriseId").toString()) && !"ALL".equals(params.get("enterpriseId").toString())) {
                String eid = params.get("enterpriseId").toString();
                w.and(wrapper -> wrapper.eq(GzctEnterpriseCost::getEnterpriseId, eid).or().eq(GzctEnterpriseCost::getEnterpriseId, "ALL"));
            }
            // 成本类型筛选：兼容中英文值
            if (params.get("costType") != null && StringUtils.isNotBlank(params.get("costType").toString())) {
                String costType = params.get("costType").toString();
                java.util.List<String> costTypeValues = new java.util.ArrayList<>();
                costTypeValues.add(costType);
                switch (costType) {
                    case "direct": costTypeValues.add("直接成本"); break;
                    case "indirect": costTypeValues.add("间接成本"); break;
                    case "fixed": costTypeValues.add("固定成本"); break;
                    case "variable": costTypeValues.add("变动成本"); break;
                    case "直接成本": costTypeValues.add("direct"); break;
                    case "间接成本": costTypeValues.add("indirect"); break;
                    case "固定成本": costTypeValues.add("fixed"); break;
                    case "变动成本": costTypeValues.add("variable"); break;
                }
                w.in(GzctEnterpriseCost::getCostType, costTypeValues);
            }
            if (params.get("status") != null && StringUtils.isNotBlank(params.get("status").toString())) w.eq(GzctEnterpriseCost::getStatus, params.get("status").toString());
            if (params.get("reportPeriod") != null && StringUtils.isNotBlank(params.get("reportPeriod").toString())) w.eq(GzctEnterpriseCost::getReportPeriod, params.get("reportPeriod").toString());
            // 时间范围筛选
            if (params.get("createTimeBegin") != null && StringUtils.isNotBlank(params.get("createTimeBegin").toString())) {
                w.ge(GzctEnterpriseCost::getCreateTime, params.get("createTimeBegin").toString() + " 00:00:00");
            }
            if (params.get("createTimeEnd") != null && StringUtils.isNotBlank(params.get("createTimeEnd").toString())) {
                w.le(GzctEnterpriseCost::getCreateTime, params.get("createTimeEnd").toString() + " 23:59:59");
            }
            w.eq(GzctEnterpriseCost::getDelFlag, "0");
            w.orderByDesc(GzctEnterpriseCost::getCreateTime);
            Page<GzctEnterpriseCost> r = new GzctEnterpriseCost().selectPage(new Page<>(pn, ps), w);
            PageResult<GzctEnterpriseCost> pr = new PageResult<>();
            pr.setTotalRecord((int) r.getTotal()); pr.setCurrentPage((int) r.getCurrent()); pr.setPageNumber((int) r.getCurrent()); pr.setTotalPage((int) r.getPages()); pr.setPageSize((int) r.getSize()); pr.setTlist(r.getRecords());
            return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }
    @Operation(summary = "costDetail")
    @GetMapping("/cost/{id}")
    public R<GzctEnterpriseCost> costDetail(@PathVariable String id) { try { return R.success(costMapper.selectById(id)); } catch (Exception e) { return R.fail("查询失败"); } }
    @Operation(summary = "新增")
    @PostMapping("/cost")
    public R<Boolean> addCost(@RequestBody GzctEnterpriseCost record) { try { record.setCreateTime(LocalDateTime.now()); record.setDelFlag("0"); costMapper.insert(record); return R.success(true); } catch (Exception e) { return R.fail("新增失败：" + e.getMessage()); } }
    @Operation(summary = "更新")
    @PutMapping("/cost")
    public R<Boolean> updateCost(@RequestBody GzctEnterpriseCost record) { try { record.setUpdateTime(LocalDateTime.now()); costMapper.updateById(record); return R.success(true); } catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); } }
    @Operation(summary = "删除")
    @DeleteMapping("/cost/{id}")
    public R<Boolean> deleteCost(@PathVariable String id) { try { GzctEnterpriseCost c = costMapper.selectById(id); if (c != null) { c.setDelFlag("1"); c.setUpdateTime(LocalDateTime.now()); costMapper.updateById(c); } return R.success(true); } catch (Exception e) { return R.fail("删除失败"); } }

    @Operation(summary = "")
    @PostMapping("/cost/analyze")
    public R<Map<String, Object>> analyzeCost(@RequestBody Map<String, Object> params) {
        try {
            Map<String, Object> result = new HashMap<>();
            LambdaQueryWrapper<GzctEnterpriseCost> w = new LambdaQueryWrapper<>();
            w.eq(GzctEnterpriseCost::getDelFlag, "0");
            if (params.get("enterpriseId") != null && StringUtils.isNotBlank(params.get("enterpriseId").toString()) && !"ALL".equals(params.get("enterpriseId").toString())) {
                String eid = params.get("enterpriseId").toString();
                w.and(wrapper -> wrapper.eq(GzctEnterpriseCost::getEnterpriseId, eid).or().eq(GzctEnterpriseCost::getEnterpriseId, "ALL"));
            }
            List<GzctEnterpriseCost> all = costMapper.selectList(w);
            BigDecimal totalCost = all.stream().map(c -> c.getCostAmount() != null ? c.getCostAmount() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal totalBudget = all.stream().map(c -> c.getBudgetAmount() != null ? c.getBudgetAmount() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal directCost = all.stream().filter(c -> "direct".equals(c.getCostType())).map(c -> c.getCostAmount() != null ? c.getCostAmount() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal indirectCost = all.stream().filter(c -> "indirect".equals(c.getCostType())).map(c -> c.getCostAmount() != null ? c.getCostAmount() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add);
            result.put("totalCost", totalCost);
            result.put("totalBudget", totalBudget);
            result.put("directCost", directCost);
            result.put("indirectCost", indirectCost);
            result.put("count", all.size());
            return R.success(result);
        } catch (Exception e) { return R.fail("分析失败：" + e.getMessage()); }
    }

    @Operation(summary = "costTrend")
    @PostMapping("/cost/trend")
    public R<List<GzctEnterpriseCost>> costTrend(@RequestBody Map<String, Object> params) {
        try {
            LambdaQueryWrapper<GzctEnterpriseCost> w = new LambdaQueryWrapper<>();
            w.eq(GzctEnterpriseCost::getDelFlag, "0");
            if (params.get("enterpriseId") != null && StringUtils.isNotBlank(params.get("enterpriseId").toString()) && !"ALL".equals(params.get("enterpriseId").toString())) {
                String eid = params.get("enterpriseId").toString();
                w.and(wrapper -> wrapper.eq(GzctEnterpriseCost::getEnterpriseId, eid).or().eq(GzctEnterpriseCost::getEnterpriseId, "ALL"));
            }
            w.orderByAsc(GzctEnterpriseCost::getReportPeriod);
            return R.success(costMapper.selectList(w));
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "成本控制")
    @PostMapping("/cost/control")
    public R<Boolean> controlCost(@RequestBody Map<String, Object> params) {
        try {
            String id = params.get("id") != null ? params.get("id").toString() : null;
            if (id == null) return R.fail("成本记录ID不能为空");
            GzctEnterpriseCost cost = costMapper.selectById(id);
            if (cost == null) return R.fail("成本记录不存在");
            // 更新成本控制状态
            if (params.get("status") != null) {
                cost.setStatus(params.get("status").toString());
            }
            if (params.get("budgetAmount") != null) {
                BigDecimal newBudget = new BigDecimal(params.get("budgetAmount").toString());
                cost.setBudgetAmount(newBudget);
                // 重新计算差异
                if (cost.getCostAmount() != null) {
                    BigDecimal variance = cost.getCostAmount().subtract(newBudget);
                    cost.setVarianceAmount(variance);
                    if (newBudget.compareTo(BigDecimal.ZERO) > 0) {
                        cost.setVarianceRate(variance.multiply(new BigDecimal("100")).divide(newBudget, 2, RoundingMode.HALF_UP));
                    }
                }
            }
            cost.setUpdateTime(LocalDateTime.now());
            costMapper.updateById(cost);
            return R.success(true);
        } catch (Exception e) {
            log.error("成本控制失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    @Operation(summary = "导出成本数据")
    @PostMapping("/cost/export")
    public void exportCost(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        try {
            LambdaQueryWrapper<GzctEnterpriseCost> w = new LambdaQueryWrapper<>();
            w.eq(GzctEnterpriseCost::getDelFlag, "0");
            if (params.get("enterpriseId") != null && StringUtils.isNotBlank(params.get("enterpriseId").toString()) && !"ALL".equals(params.get("enterpriseId").toString())) {
                w.eq(GzctEnterpriseCost::getEnterpriseId, params.get("enterpriseId").toString());
            }
            w.orderByDesc(GzctEnterpriseCost::getCreateTime);
            List<GzctEnterpriseCost> list = costMapper.selectList(w);
            List<List<Object>> rows = new ArrayList<>();
            rows.add(java.util.Arrays.asList("成本项目", "成本类型", "成本金额", "预算金额", "差异金额", "差异率(%)", "归属部门", "报告期间", "状态"));
            for (GzctEnterpriseCost item : list) {
                rows.add(java.util.Arrays.asList(
                    item.getCostName() != null ? item.getCostName() : "",
                    item.getCostType() != null ? item.getCostType() : "",
                    item.getCostAmount() != null ? item.getCostAmount().toString() : "0",
                    item.getBudgetAmount() != null ? item.getBudgetAmount().toString() : "0",
                    item.getVarianceAmount() != null ? item.getVarianceAmount().toString() : "0",
                    item.getVarianceRate() != null ? item.getVarianceRate().toString() : "0",
                    item.getDepartment() != null ? item.getDepartment() : "",
                    item.getReportPeriod() != null ? item.getReportPeriod() : "",
                    item.getStatus() != null ? item.getStatus() : ""
                ));
            }
            byte[] xlsxBytes = com.huabo.cybermonitor.util.SimpleXlsxWriter.write("成本管理", rows);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=cost_" + System.currentTimeMillis() + ".xlsx");
            response.getOutputStream().write(xlsxBytes);
            response.getOutputStream().flush();
        } catch (Exception e) {
            log.error("导出成本数据失败", e);
            try {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":500,\"msg\":\"导出失败：" + e.getMessage() + "\"}");
            } catch (Exception ex) { log.error("写入错误响应失败", ex); }
        }
    }

    // ==================== 财务分析 ====================
    @Operation(summary = "获取财务指标分析(含上期对比)")
    @PostMapping("/analysis/indicators")
    public R<Map<String, Object>> getIndicators(@RequestBody Map<String, Object> params) {
        try {
            Map<String, Object> result = new HashMap<>();
            LambdaQueryWrapper<GzctEnterpriseFinancial> w = new LambdaQueryWrapper<>();
            if (params.get("enterpriseId") != null && StringUtils.isNotBlank(params.get("enterpriseId").toString()) && !"ALL".equals(params.get("enterpriseId").toString())) {
                w.eq(GzctEnterpriseFinancial::getEnterpriseId, params.get("enterpriseId").toString());
            }
            // 时间范围筛选
            if (params.get("startDate") != null && StringUtils.isNotBlank(params.get("startDate").toString())) {
                w.ge(GzctEnterpriseFinancial::getReportPeriod, params.get("startDate").toString());
            }
            if (params.get("endDate") != null && StringUtils.isNotBlank(params.get("endDate").toString())) {
                w.le(GzctEnterpriseFinancial::getReportPeriod, params.get("endDate").toString());
            }
            w.orderByDesc(GzctEnterpriseFinancial::getReportPeriod);
            List<GzctEnterpriseFinancial> all = financialMapper.selectList(w);

            if (all.isEmpty()) {
                result.put("totalAssets", BigDecimal.ZERO);
                result.put("totalRevenue", BigDecimal.ZERO);
                result.put("totalProfit", BigDecimal.ZERO);
                result.put("totalLiabilities", BigDecimal.ZERO);
                result.put("indicators", new ArrayList<>());
                return R.success(result);
            }

            // 当期数据(最新一条)
            GzctEnterpriseFinancial current = all.get(0);
            // 上期数据(第二条)
            GzctEnterpriseFinancial previous = all.size() > 1 ? all.get(1) : null;

            BigDecimal totalAssets = current.getTotalAssets() != null ? current.getTotalAssets() : BigDecimal.ZERO;
            BigDecimal totalLiabilities = current.getTotalLiabilities() != null ? current.getTotalLiabilities() : BigDecimal.ZERO;
            BigDecimal netAssets = current.getNetAssets() != null ? current.getNetAssets() : BigDecimal.ZERO;
            BigDecimal operatingRevenue = current.getOperatingRevenue() != null ? current.getOperatingRevenue() : BigDecimal.ZERO;
            BigDecimal netProfit = current.getNetProfit() != null ? current.getNetProfit() : BigDecimal.ZERO;
            BigDecimal cashFlow = current.getCashFlow() != null ? current.getCashFlow() : BigDecimal.ZERO;

            result.put("totalAssets", totalAssets);
            result.put("totalRevenue", operatingRevenue);
            result.put("totalProfit", netProfit);
            result.put("totalLiabilities", totalLiabilities);
            result.put("netAssets", netAssets);
            result.put("cashFlow", cashFlow);

            // 计算上期数据和变动率
            BigDecimal prevRevenue = BigDecimal.ZERO;
            BigDecimal prevProfit = BigDecimal.ZERO;
            BigDecimal prevAssets = BigDecimal.ZERO;
            BigDecimal prevLiabilities = BigDecimal.ZERO;
            BigDecimal prevNetAssets = BigDecimal.ZERO;
            BigDecimal prevCashFlow = BigDecimal.ZERO;

            if (previous != null) {
                prevRevenue = previous.getOperatingRevenue() != null ? previous.getOperatingRevenue() : BigDecimal.ZERO;
                prevProfit = previous.getNetProfit() != null ? previous.getNetProfit() : BigDecimal.ZERO;
                prevAssets = previous.getTotalAssets() != null ? previous.getTotalAssets() : BigDecimal.ZERO;
                prevLiabilities = previous.getTotalLiabilities() != null ? previous.getTotalLiabilities() : BigDecimal.ZERO;
                prevNetAssets = previous.getNetAssets() != null ? previous.getNetAssets() : BigDecimal.ZERO;
                prevCashFlow = previous.getCashFlow() != null ? previous.getCashFlow() : BigDecimal.ZERO;
            }

            result.put("prevRevenue", prevRevenue);
            result.put("prevProfit", prevProfit);
            result.put("prevAssets", prevAssets);
            result.put("prevLiabilities", prevLiabilities);
            result.put("prevNetAssets", prevNetAssets);
            result.put("prevCashFlow", prevCashFlow);

            // 计算变动率
            result.put("revenueGrowth", calcChangeRate(operatingRevenue, prevRevenue));
            result.put("profitGrowth", calcChangeRate(netProfit, prevProfit));
            result.put("assetGrowth", calcChangeRate(totalAssets, prevAssets));
            result.put("liabilityGrowth", calcChangeRate(totalLiabilities, prevLiabilities));
            result.put("netAssetGrowth", calcChangeRate(netAssets, prevNetAssets));
            result.put("cashFlowGrowth", calcChangeRate(cashFlow, prevCashFlow));

            // 计算关键比率
            BigDecimal debtRatio = totalAssets.compareTo(BigDecimal.ZERO) > 0
                ? totalLiabilities.multiply(new BigDecimal("100")).divide(totalAssets, 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
            BigDecimal roe = netAssets.compareTo(BigDecimal.ZERO) > 0
                ? netProfit.multiply(new BigDecimal("100")).divide(netAssets, 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
            BigDecimal roa = totalAssets.compareTo(BigDecimal.ZERO) > 0
                ? netProfit.multiply(new BigDecimal("100")).divide(totalAssets, 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
            BigDecimal profitMargin = operatingRevenue.compareTo(BigDecimal.ZERO) > 0
                ? netProfit.multiply(new BigDecimal("100")).divide(operatingRevenue, 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

            result.put("debtRatio", debtRatio);
            result.put("roe", roe);
            result.put("roa", roa);
            result.put("profitMargin", profitMargin);

            // 上期比率
            BigDecimal prevDebtRatio = prevAssets.compareTo(BigDecimal.ZERO) > 0
                ? prevLiabilities.multiply(new BigDecimal("100")).divide(prevAssets, 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
            BigDecimal prevRoe = prevNetAssets.compareTo(BigDecimal.ZERO) > 0
                ? prevProfit.multiply(new BigDecimal("100")).divide(prevNetAssets, 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
            BigDecimal prevRoa = prevAssets.compareTo(BigDecimal.ZERO) > 0
                ? prevProfit.multiply(new BigDecimal("100")).divide(prevAssets, 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

            result.put("prevDebtRatio", prevDebtRatio);
            result.put("prevRoe", prevRoe);
            result.put("prevRoa", prevRoa);

            result.put("count", all.size());
            result.put("currentPeriod", current.getReportPeriod());
            result.put("previousPeriod", previous != null ? previous.getReportPeriod() : null);

            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "")
    @PostMapping("/analysis/profitability")
    public R<Map<String, Object>> getProfitability(@RequestBody Map<String, Object> params) { return getIndicators(params); }
    @Operation(summary = "")
    @PostMapping("/analysis/solvency")
    public R<Map<String, Object>> getSolvency(@RequestBody Map<String, Object> params) { return getIndicators(params); }
    @Operation(summary = "")
    @PostMapping("/analysis/operating")
    public R<Map<String, Object>> getOperating(@RequestBody Map<String, Object> params) { return getIndicators(params); }
    @Operation(summary = "")
    @PostMapping("/analysis/development")
    public R<Map<String, Object>> getDevelopment(@RequestBody Map<String, Object> params) { return getIndicators(params); }
    @Operation(summary = "")
    @PostMapping("/analysis/comprehensive")
    public R<Map<String, Object>> getComprehensive(@RequestBody Map<String, Object> params) { return getIndicators(params); }
    @Operation(summary = "查询数据")
    @PostMapping("/analysis/industry-comparison")
    public R<Map<String, Object>> getIndustryComparison(@RequestBody Map<String, Object> params) { return getIndicators(params); }
    @Operation(summary = "查询数据")
    @PostMapping("/analysis/historical-trend")
    public R<List<GzctEnterpriseFinancial>> getHistoricalTrend(@RequestBody Map<String, Object> params) {
        try {
            LambdaQueryWrapper<GzctEnterpriseFinancial> w = new LambdaQueryWrapper<>();
            if (params.get("enterpriseId") != null && StringUtils.isNotBlank(params.get("enterpriseId").toString()) && !"ALL".equals(params.get("enterpriseId").toString())) {
                w.eq(GzctEnterpriseFinancial::getEnterpriseId, params.get("enterpriseId").toString());
            }
            if (params.get("startDate") != null && StringUtils.isNotBlank(params.get("startDate").toString())) {
                w.ge(GzctEnterpriseFinancial::getReportPeriod, params.get("startDate").toString());
            }
            if (params.get("endDate") != null && StringUtils.isNotBlank(params.get("endDate").toString())) {
                w.le(GzctEnterpriseFinancial::getReportPeriod, params.get("endDate").toString());
            }
            w.orderByAsc(GzctEnterpriseFinancial::getReportPeriod);
            return R.success(financialMapper.selectList(w));
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 财务预警 ====================
    @Operation(summary = "warningList")
    @PostMapping("/warning/list")
    public R<PageResult<GzctFinancialWarning>> warningList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctFinancialWarning> w = new LambdaQueryWrapper<>();
            if (params.get("enterpriseId") != null && StringUtils.isNotBlank(params.get("enterpriseId").toString()) && !"ALL".equals(params.get("enterpriseId").toString())) w.eq(GzctFinancialWarning::getEnterpriseId, params.get("enterpriseId").toString());
            // 预警类型筛选：兼容中英文
            if (params.get("warningType") != null && StringUtils.isNotBlank(params.get("warningType").toString())) {
                String val = params.get("warningType").toString();
                java.util.List<String> vals = new java.util.ArrayList<>();
                vals.add(val);
                switch (val) {
                    case "liquidity": vals.add("流动性风险"); break;
                    case "solvency": vals.add("偿债能力"); break;
                    case "profitability": vals.add("盈利能力"); break;
                    case "operational": vals.add("营运能力"); break;
                    case "流动性风险": vals.add("liquidity"); break;
                    case "偿债能力": vals.add("solvency"); break;
                    case "盈利能力": vals.add("profitability"); break;
                    case "营运能力": vals.add("operational"); break;
                }
                w.in(GzctFinancialWarning::getWarningType, vals);
            }
            // 风险等级筛选：兼容中英文
            if (params.get("riskLevel") != null && StringUtils.isNotBlank(params.get("riskLevel").toString())) {
                String val = params.get("riskLevel").toString();
                java.util.List<String> vals = new java.util.ArrayList<>();
                vals.add(val);
                switch (val) {
                    case "high": vals.add("高风险"); vals.add("高"); break;
                    case "medium": vals.add("中风险"); vals.add("中"); break;
                    case "low": vals.add("低风险"); vals.add("低"); break;
                    case "高风险": case "高": vals.add("high"); break;
                    case "中风险": case "中": vals.add("medium"); break;
                    case "低风险": case "低": vals.add("low"); break;
                }
                w.in(GzctFinancialWarning::getRiskLevel, vals);
            }
            // 处理状态筛选：兼容中英文
            if (params.get("status") != null && StringUtils.isNotBlank(params.get("status").toString())) {
                String val = params.get("status").toString();
                java.util.List<String> vals = new java.util.ArrayList<>();
                vals.add(val);
                switch (val) {
                    case "pending": vals.add("待处理"); break;
                    case "processing": vals.add("处理中"); break;
                    case "handled": vals.add("已处理"); break;
                    case "ignored": vals.add("已忽略"); break;
                    case "待处理": vals.add("pending"); break;
                    case "处理中": vals.add("processing"); break;
                    case "已处理": vals.add("handled"); break;
                    case "已忽略": vals.add("ignored"); break;
                }
                w.in(GzctFinancialWarning::getStatus, vals);
            }
            if (params.get("warningStatus") != null && StringUtils.isNotBlank(params.get("warningStatus").toString())) w.eq(GzctFinancialWarning::getStatus, params.get("warningStatus").toString());
            w.eq(GzctFinancialWarning::getDelFlag, "0");
            w.orderByDesc(GzctFinancialWarning::getTriggerTime);
            Page<GzctFinancialWarning> r = new GzctFinancialWarning().selectPage(new Page<>(pn, ps), w);
            PageResult<GzctFinancialWarning> pr = new PageResult<>();
            pr.setTotalRecord((int) r.getTotal()); pr.setCurrentPage((int) r.getCurrent()); pr.setPageNumber((int) r.getCurrent()); pr.setTotalPage((int) r.getPages()); pr.setPageSize((int) r.getSize()); pr.setTlist(r.getRecords());
            return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "handleWarning")
    @PostMapping("/warning/handle")
    public R<Boolean> handleWarning(@RequestBody Map<String, Object> params) {
        try {
            String warningId = params.get("warningId") != null ? params.get("warningId").toString() : null;
            if (warningId == null) return R.fail("预警ID不能为空");
            GzctFinancialWarning warning = warningMapper.selectById(warningId);
            if (warning != null) {
                if (params.get("status") != null) warning.setStatus(params.get("status").toString());
                if (params.get("handler") != null) warning.setHandler(params.get("handler").toString());
                if (params.get("handleRemark") != null) warning.setHandleRemark(params.get("handleRemark").toString());
                warning.setHandleTime(LocalDateTime.now());
                warning.setUpdateTime(LocalDateTime.now());
                warningMapper.updateById(warning);
            }
            return R.success(true);
        } catch (Exception e) { return R.fail("处理失败：" + e.getMessage()); }
    }

    @Operation(summary = "设置预警规则")
    @PostMapping("/warning/rules")
    public R<Boolean> setWarningRules(@RequestBody Map<String, Object> params) {
        try {
            // 根据规则参数创建预警记录
            String enterpriseId = params.get("enterpriseId") != null ? params.get("enterpriseId").toString() : null;
            String warningType = params.get("warningType") != null ? params.get("warningType").toString() : null;
            String riskLevel = params.get("riskLevel") != null ? params.get("riskLevel").toString() : "medium";
            BigDecimal threshold = params.get("threshold") != null ? new BigDecimal(params.get("threshold").toString()) : BigDecimal.ZERO;
            String warningName = params.get("warningName") != null ? params.get("warningName").toString() : "自定义预警规则";

            GzctFinancialWarning warning = new GzctFinancialWarning();
            warning.setEnterpriseId(enterpriseId);
            warning.setWarningName(warningName);
            warning.setWarningType(warningType);
            warning.setRiskLevel(riskLevel);
            warning.setThresholdValue(threshold);
            warning.setStatus("active");
            warning.setDelFlag("0");
            warning.setCreateTime(LocalDateTime.now());
            warningMapper.insert(warning);
            return R.success(true);
        } catch (Exception e) {
            log.error("设置预警规则失败", e);
            return R.fail("设置失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取预警规则")
    @GetMapping("/warning/rules")
    public R<List<Map<String, Object>>> getWarningRules() {
        try {
            LambdaQueryWrapper<GzctFinancialWarning> w = new LambdaQueryWrapper<>();
            w.eq(GzctFinancialWarning::getStatus, "active");
            w.eq(GzctFinancialWarning::getDelFlag, "0");
            w.orderByDesc(GzctFinancialWarning::getCreateTime);
            List<GzctFinancialWarning> rules = warningMapper.selectList(w);
            List<Map<String, Object>> result = new ArrayList<>();
            for (GzctFinancialWarning rule : rules) {
                Map<String, Object> item = new HashMap<>();
                item.put("warningId", rule.getWarningId());
                item.put("warningName", rule.getWarningName());
                item.put("warningType", rule.getWarningType());
                item.put("riskLevel", rule.getRiskLevel());
                item.put("threshold", rule.getThresholdValue());
                item.put("status", rule.getStatus());
                item.put("createTime", rule.getCreateTime());
                result.add(item);
            }
            return R.success(result);
        } catch (Exception e) {
            log.error("获取预警规则失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量处理预警")
    @PostMapping("/warning/batch-handle")
    public R<Boolean> batchHandleWarning(@RequestBody Map<String, Object> params) {
        try {
            List<String> warningIds = (List<String>) params.get("warningIds");
            String handler = params.get("handler") != null ? params.get("handler").toString() : "";
            String handleRemark = params.get("handleRemark") != null ? params.get("handleRemark").toString() : "";
            if (warningIds == null || warningIds.isEmpty()) return R.fail("请选择要处理的预警");
            for (String id : warningIds) {
                GzctFinancialWarning warning = warningMapper.selectById(id);
                if (warning != null) {
                    warning.setStatus("handled");
                    warning.setHandler(handler);
                    warning.setHandleRemark(handleRemark);
                    warning.setHandleTime(LocalDateTime.now());
                    warning.setUpdateTime(LocalDateTime.now());
                    warningMapper.updateById(warning);
                }
            }
            return R.success(true);
        } catch (Exception e) {
            log.error("批量处理预警失败", e);
            return R.fail("批量处理失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量忽略预警")
    @PostMapping("/warning/batch-ignore")
    public R<Boolean> batchIgnoreWarning(@RequestBody Map<String, Object> params) {
        try {
            List<String> warningIds = (List<String>) params.get("warningIds");
            String handleRemark = params.get("handleRemark") != null ? params.get("handleRemark").toString() : "批量忽略";
            if (warningIds == null || warningIds.isEmpty()) return R.fail("请选择要忽略的预警");
            for (String id : warningIds) {
                GzctFinancialWarning warning = warningMapper.selectById(id);
                if (warning != null) {
                    warning.setStatus("ignored");
                    warning.setHandleRemark(handleRemark);
                    warning.setHandleTime(LocalDateTime.now());
                    warning.setUpdateTime(LocalDateTime.now());
                    warningMapper.updateById(warning);
                }
            }
            return R.success(true);
        } catch (Exception e) {
            log.error("批量忽略预警失败", e);
            return R.fail("批量忽略失败：" + e.getMessage());
        }
    }

    @Operation(summary = "")
    @PostMapping("/warning/statistics")
    public R<Map<String, Object>> warningStatistics(@RequestBody Map<String, Object> params) {
        try {
            String enterpriseId = params.get("enterpriseId") != null ? params.get("enterpriseId").toString() : null;
            if (enterpriseId != null) {
                Map<String, Object> stats = warningMapper.selectStatisticsByEnterpriseId(enterpriseId);
                return R.success(stats != null ? stats : new HashMap<>());
            }
            return R.success(new HashMap<>());
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 统计概览 ====================
    @Operation(summary = "")
    @PostMapping("/statistics")
    public R<Map<String, Object>> statistics(@RequestBody Map<String, Object> params) {
        try {
            String enterpriseId = params.get("enterpriseId") != null ? params.get("enterpriseId").toString() : null;
            Map<String, Object> result = new HashMap<>();

            // 从财务报表获取核心指标
            LambdaQueryWrapper<GzctEnterpriseFinancial> w = new LambdaQueryWrapper<>();
            if (enterpriseId != null && StringUtils.isNotBlank(enterpriseId) && !"ALL".equals(enterpriseId)) {
                w.eq(GzctEnterpriseFinancial::getEnterpriseId, enterpriseId);
            }
            w.orderByDesc(GzctEnterpriseFinancial::getCreateTime);
            List<GzctEnterpriseFinancial> financials = financialMapper.selectList(w);

            if (!financials.isEmpty()) {
                // 取最新一条记录作为当期数据
                GzctEnterpriseFinancial latest = financials.get(0);
                result.put("totalRevenue", latest.getOperatingRevenue() != null ? latest.getOperatingRevenue() : BigDecimal.ZERO);
                result.put("netProfit", latest.getNetProfit() != null ? latest.getNetProfit() : BigDecimal.ZERO);
                result.put("totalAssets", latest.getTotalAssets() != null ? latest.getTotalAssets() : BigDecimal.ZERO);
                result.put("totalLiabilities", latest.getTotalLiabilities() != null ? latest.getTotalLiabilities() : BigDecimal.ZERO);
                result.put("netAssets", latest.getNetAssets() != null ? latest.getNetAssets() : BigDecimal.ZERO);

                // 计算负债率
                BigDecimal liabilityRatio = BigDecimal.ZERO;
                if (latest.getTotalAssets() != null && latest.getTotalAssets().compareTo(BigDecimal.ZERO) > 0 && latest.getTotalLiabilities() != null) {
                    liabilityRatio = latest.getTotalLiabilities().multiply(new BigDecimal("100")).divide(latest.getTotalAssets(), 2, RoundingMode.HALF_UP);
                }
                result.put("liabilityRatio", liabilityRatio);

                // 计算ROE
                BigDecimal roe = BigDecimal.ZERO;
                if (latest.getNetAssets() != null && latest.getNetAssets().compareTo(BigDecimal.ZERO) > 0 && latest.getNetProfit() != null) {
                    roe = latest.getNetProfit().multiply(new BigDecimal("100")).divide(latest.getNetAssets(), 2, RoundingMode.HALF_UP);
                }
                result.put("roe", roe);

                // 计算ROA
                BigDecimal roa = BigDecimal.ZERO;
                if (latest.getTotalAssets() != null && latest.getTotalAssets().compareTo(BigDecimal.ZERO) > 0 && latest.getNetProfit() != null) {
                    roa = latest.getNetProfit().multiply(new BigDecimal("100")).divide(latest.getTotalAssets(), 2, RoundingMode.HALF_UP);
                }
                result.put("roa", roa);

                // 计算增长率（如果有2条以上记录）
                if (financials.size() >= 2) {
                    GzctEnterpriseFinancial prev = financials.get(1);
                    BigDecimal revenueGrowth = BigDecimal.ZERO;
                    if (prev.getOperatingRevenue() != null && prev.getOperatingRevenue().compareTo(BigDecimal.ZERO) > 0 && latest.getOperatingRevenue() != null) {
                        revenueGrowth = latest.getOperatingRevenue().subtract(prev.getOperatingRevenue()).multiply(new BigDecimal("100")).divide(prev.getOperatingRevenue(), 2, RoundingMode.HALF_UP);
                    }
                    result.put("revenueGrowth", revenueGrowth);

                    BigDecimal profitGrowth = BigDecimal.ZERO;
                    if (prev.getNetProfit() != null && prev.getNetProfit().compareTo(BigDecimal.ZERO) > 0 && latest.getNetProfit() != null) {
                        profitGrowth = latest.getNetProfit().subtract(prev.getNetProfit()).multiply(new BigDecimal("100")).divide(prev.getNetProfit(), 2, RoundingMode.HALF_UP);
                    }
                    result.put("profitGrowth", profitGrowth);
                } else {
                    result.put("revenueGrowth", BigDecimal.ZERO);
                    result.put("profitGrowth", BigDecimal.ZERO);
                }
            } else {
                result.put("totalRevenue", BigDecimal.ZERO);
                result.put("netProfit", BigDecimal.ZERO);
                result.put("totalAssets", BigDecimal.ZERO);
                result.put("totalLiabilities", BigDecimal.ZERO);
                result.put("netAssets", BigDecimal.ZERO);
                result.put("liabilityRatio", BigDecimal.ZERO);
                result.put("roe", BigDecimal.ZERO);
                result.put("roa", BigDecimal.ZERO);
                result.put("revenueGrowth", BigDecimal.ZERO);
                result.put("profitGrowth", BigDecimal.ZERO);
            }

            return R.success(result);
        } catch (Exception e) { return R.fail("统计查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "")
    @PostMapping("/statistics/revenue")
    public R<Map<String, Object>> revenueStats(@RequestBody Map<String, Object> params) { return statistics(params); }
    @Operation(summary = "")
    @PostMapping("/statistics/profit")
    public R<Map<String, Object>> profitStats(@RequestBody Map<String, Object> params) { return statistics(params); }
    @Operation(summary = "")
    @PostMapping("/statistics/asset")
    public R<Map<String, Object>> assetStats(@RequestBody Map<String, Object> params) { return statistics(params); }
    @Operation(summary = "")
    @PostMapping("/statistics/liability")
    public R<Map<String, Object>> liabilityStats(@RequestBody Map<String, Object> params) { return statistics(params); }
    @Operation(summary = "")
    @PostMapping("/statistics/cashflow")
    public R<Map<String, Object>> cashflowStats(@RequestBody Map<String, Object> params) { return statistics(params); }

    // ==================== 财务报告 ====================
    @Operation(summary = "reportList")
    @PostMapping("/report/list")
    public R<PageResult<GzctFinancialReport>> reportList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctFinancialReport> w = new LambdaQueryWrapper<>();
            if (params.get("enterpriseId") != null && StringUtils.isNotBlank(params.get("enterpriseId").toString()) && !"ALL".equals(params.get("enterpriseId").toString())) w.eq(GzctFinancialReport::getEnterpriseId, params.get("enterpriseId").toString());
            // 报告类型筛选：兼容中英文
            if (params.get("reportType") != null && StringUtils.isNotBlank(params.get("reportType").toString())) {
                String val = params.get("reportType").toString();
                java.util.List<String> vals = new java.util.ArrayList<>();
                vals.add(val);
                switch (val) {
                    case "balance_sheet": vals.add("资产负债表"); break;
                    case "income_statement": vals.add("利润表"); break;
                    case "cash_flow": vals.add("现金流量表"); break;
                    case "financial_analysis": vals.add("财务分析报告"); break;
                    case "资产负债表": vals.add("balance_sheet"); break;
                    case "利润表": vals.add("income_statement"); break;
                    case "现金流量表": vals.add("cash_flow"); break;
                    case "财务分析报告": vals.add("financial_analysis"); break;
                }
                w.in(GzctFinancialReport::getReportType, vals);
            }
            // 状态筛选：兼容中英文
            if (params.get("status") != null && StringUtils.isNotBlank(params.get("status").toString())) {
                String val = params.get("status").toString();
                java.util.List<String> vals = new java.util.ArrayList<>();
                vals.add(val);
                switch (val) {
                    case "draft": vals.add("草稿"); break;
                    case "pending": vals.add("待审核"); break;
                    case "completed": vals.add("已完成"); break;
                    case "草稿": vals.add("draft"); break;
                    case "待审核": vals.add("pending"); break;
                    case "已完成": vals.add("completed"); break;
                }
                w.in(GzctFinancialReport::getStatus, vals);
            }
            w.eq(GzctFinancialReport::getDelFlag, "0");
            w.orderByDesc(GzctFinancialReport::getCreateTime);
            Page<GzctFinancialReport> r = new GzctFinancialReport().selectPage(new Page<>(pn, ps), w);
            PageResult<GzctFinancialReport> pr = new PageResult<>();
            pr.setTotalRecord((int) r.getTotal()); pr.setCurrentPage((int) r.getCurrent()); pr.setPageNumber((int) r.getCurrent()); pr.setTotalPage((int) r.getPages()); pr.setPageSize((int) r.getSize()); pr.setTlist(r.getRecords());
            return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "生成")
    @PostMapping("/report/generate")
    public R<Boolean> generateReport(@RequestBody Map<String, Object> params) {
        try {
            String reportId = params.get("reportId") != null ? params.get("reportId").toString() : null;
            if (reportId != null) {
                // 更新已有报告
                GzctFinancialReport report = reportMapper.selectById(reportId);
                if (report != null) {
                    if (params.get("status") != null) report.setStatus(params.get("status").toString());
                    if (params.get("delFlag") != null) report.setDelFlag(params.get("delFlag").toString());
                    report.setUpdateTime(LocalDateTime.now());
                    reportMapper.updateById(report);
                }
            } else {
                // 新建报告
                GzctFinancialReport report = new GzctFinancialReport();
                report.setEnterpriseId(params.get("enterpriseId") != null ? params.get("enterpriseId").toString() : null);
                report.setEnterpriseName(params.get("enterpriseName") != null ? params.get("enterpriseName").toString() : null);
                report.setReportName(params.get("reportName") != null ? params.get("reportName").toString() : null);
                report.setReportType(params.get("reportType") != null ? params.get("reportType").toString() : null);
                report.setPeriod(params.get("period") != null ? params.get("period").toString() : null);
                report.setCreator(params.get("creator") != null ? params.get("creator").toString() : null);
                report.setRemark(params.get("remark") != null ? params.get("remark").toString() : null);
                report.setStatus("draft");
                report.setProgress(BigDecimal.ZERO);
                report.setAuditStatus("未提交");
                report.setDelFlag("0");
                report.setCreateTime(LocalDateTime.now());
                reportMapper.insert(report);
            }
            return R.success(true);
        } catch (Exception e) { return R.fail("操作失败：" + e.getMessage()); }
    }

    @Operation(summary = "导出")
    @GetMapping("/report/download/{reportId}")
    public R<String> downloadReport(@PathVariable String reportId) { return R.success("report_" + reportId); }

    @Operation(summary = "更新报告")
    @PutMapping("/report/update")
    public R<Boolean> updateReport(@RequestBody GzctFinancialReport report) {
        try {
            if (report.getReportId() == null) return R.fail("报告ID不能为空");
            report.setUpdateTime(LocalDateTime.now());
            reportMapper.updateById(report);
            return R.success(true);
        } catch (Exception e) {
            log.error("更新报告失败", e);
            return R.fail("更新失败：" + e.getMessage());
        }
    }

    @Operation(summary = "删除报告")
    @DeleteMapping("/report/{reportId}")
    public R<Boolean> deleteReportById(@PathVariable String reportId) {
        try {
            GzctFinancialReport report = reportMapper.selectById(reportId);
            if (report != null) {
                report.setDelFlag("1");
                report.setUpdateTime(LocalDateTime.now());
                reportMapper.updateById(report);
            }
            return R.success(true);
        } catch (Exception e) {
            log.error("删除报告失败", e);
            return R.fail("删除失败：" + e.getMessage());
        }
    }

    @Operation(summary = "")
    @PostMapping("/report/statistics")
    public R<Map<String, Object>> reportStatistics(@RequestBody Map<String, Object> params) {
        try {
            String enterpriseId = params.get("enterpriseId") != null ? params.get("enterpriseId").toString() : null;
            if (enterpriseId != null) {
                Map<String, Object> stats = reportMapper.selectStatisticsByEnterpriseId(enterpriseId);
                return R.success(stats != null ? stats : new HashMap<>());
            }
            return R.success(new HashMap<>());
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 预算管理(GzctEnterpriseBudget) ====================
    // 注意: list/detail/delete 已迁移至 BudgetManagementController，避免路径冲突

    @Operation(summary = "新增")
    @PostMapping("/budget")
    public R<Boolean> addBudget(@RequestBody GzctEnterpriseBudget record) { try { record.setCreateTime(LocalDateTime.now()); budgetMapper.insert(record); return R.success(true); } catch (Exception e) { return R.fail("新增失败：" + e.getMessage()); } }

    @Operation(summary = "更新")
    @PutMapping("/budget")
    public R<Boolean> updateBudget(@RequestBody GzctEnterpriseBudget record) { try { record.setUpdateTime(LocalDateTime.now()); budgetMapper.updateById(record); return R.success(true); } catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); } }

    @Operation(summary = "")
    @PostMapping("/budget/statistics")
    public R<Map<String, Object>> budgetStatistics(@RequestBody Map<String, Object> params) {
        try {
            String enterpriseId = params.get("enterpriseId") != null ? params.get("enterpriseId").toString() : null;
            Map<String, Object> result = new HashMap<>();
            if (enterpriseId != null) {
                LambdaQueryWrapper<GzctEnterpriseBudget> w = new LambdaQueryWrapper<>();
                w.eq(GzctEnterpriseBudget::getEnterpriseId, enterpriseId);
                List<GzctEnterpriseBudget> all = budgetMapper.selectList(w);
                result.put("totalBudgets", all.size());
                result.put("compilationBudgets", all.stream().filter(b -> "编制中".equals(b.getStatus()) || "待编制".equals(b.getStatus())).count());
                result.put("executionBudgets", all.stream().filter(b -> "执行中".equals(b.getStatus())).count());
                result.put("completedBudgets", all.stream().filter(b -> "已完成".equals(b.getStatus())).count());
                BigDecimal totalAmount = all.stream().map(b -> b.getBudgetAmount() != null ? b.getBudgetAmount() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add);
                result.put("totalAmount", totalAmount);
                BigDecimal executedAmount = all.stream().map(b -> b.getActualAmount() != null ? b.getActualAmount() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add);
                result.put("executedAmount", executedAmount);
            }
            return R.success(result);
        } catch (Exception e) { return R.fail("统计查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "提交预算")
    @PostMapping("/budget/submit")
    public R<Boolean> submitBudget(@RequestBody Map<String, Object> params) {
        try {
            String id = params.get("id") != null ? params.get("id").toString() : null;
            if (id == null) return R.fail("预算ID不能为空");
            GzctEnterpriseBudget budget = budgetMapper.selectById(id);
            if (budget == null) return R.fail("预算记录不存在");
            budget.setStatus("待审批");
            budget.setUpdateTime(LocalDateTime.now());
            budgetMapper.updateById(budget);
            return R.success(true);
        } catch (Exception e) {
            log.error("提交预算失败", e);
            return R.fail("提交失败：" + e.getMessage());
        }
    }

    @Operation(summary = "审批预算")
    @PostMapping("/budget/approve")
    public R<Boolean> approveBudget(@RequestBody Map<String, Object> params) {
        try {
            String id = params.get("id") != null ? params.get("id").toString() : null;
            String approveResult = params.get("approveResult") != null ? params.get("approveResult").toString() : "通过";
            if (id == null) return R.fail("预算ID不能为空");
            GzctEnterpriseBudget budget = budgetMapper.selectById(id);
            if (budget == null) return R.fail("预算记录不存在");
            if ("通过".equals(approveResult)) {
                budget.setStatus("已审批");
            } else {
                budget.setStatus("已驳回");
            }
            budget.setApproveTime(LocalDateTime.now());
            budget.setUpdateTime(LocalDateTime.now());
            budgetMapper.updateById(budget);
            return R.success(true);
        } catch (Exception e) {
            log.error("审批预算失败", e);
            return R.fail("审批失败：" + e.getMessage());
        }
    }

    @Operation(summary = "预算执行情况")
    @PostMapping("/budget/execution")
    public R<Map<String, Object>> budgetExecution(@RequestBody Map<String, Object> params) {
        try {
            String enterpriseId = params.get("enterpriseId") != null ? params.get("enterpriseId").toString() : null;
            Map<String, Object> result = new HashMap<>();
            LambdaQueryWrapper<GzctEnterpriseBudget> w = new LambdaQueryWrapper<>();
            if (enterpriseId != null && StringUtils.isNotBlank(enterpriseId) && !"ALL".equals(enterpriseId)) {
                w.eq(GzctEnterpriseBudget::getEnterpriseId, enterpriseId);
            }
            List<GzctEnterpriseBudget> all = budgetMapper.selectList(w);
            BigDecimal totalBudget = all.stream().map(b -> b.getBudgetAmount() != null ? b.getBudgetAmount() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal totalActual = all.stream().map(b -> b.getActualAmount() != null ? b.getActualAmount() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal executionRate = totalBudget.compareTo(BigDecimal.ZERO) > 0 ? totalActual.multiply(new BigDecimal("100")).divide(totalBudget, 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
            result.put("totalBudget", totalBudget);
            result.put("totalActual", totalActual);
            result.put("executionRate", executionRate);
            result.put("remainingBudget", totalBudget.subtract(totalActual));
            result.put("budgetCount", all.size());
            result.put("executingCount", all.stream().filter(b -> "执行中".equals(b.getStatus())).count());
            result.put("completedCount", all.stream().filter(b -> "已完成".equals(b.getStatus())).count());
            return R.success(result);
        } catch (Exception e) {
            log.error("查询预算执行情况失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "预算调整")
    @PostMapping("/budget/adjust")
    public R<Boolean> adjustBudget(@RequestBody Map<String, Object> params) {
        try {
            String id = params.get("id") != null ? params.get("id").toString() : null;
            if (id == null) return R.fail("预算ID不能为空");
            GzctEnterpriseBudget budget = budgetMapper.selectById(id);
            if (budget == null) return R.fail("预算记录不存在");
            if (params.get("adjustAmount") != null) {
                BigDecimal adjustAmount = new BigDecimal(params.get("adjustAmount").toString());
                budget.setBudgetAmount(adjustAmount);
            }
            if (params.get("adjustReason") != null) {
                // 调整原因记录在category字段（复用）
            }
            budget.setUpdateTime(LocalDateTime.now());
            budgetMapper.updateById(budget);
            return R.success(true);
        } catch (Exception e) {
            log.error("预算调整失败", e);
            return R.fail("调整失败：" + e.getMessage());
        }
    }

    @Operation(summary = "预算分析")
    @PostMapping("/budget/analysis")
    public R<Map<String, Object>> budgetAnalysis(@RequestBody Map<String, Object> params) { return budgetStatistics(params); }

    @Operation(summary = "执行预算")
    @PostMapping("/execute-budget")
    public R<Boolean> executeBudget(@RequestBody Map<String, Object> params) {
        try {
            String id = params.get("id") != null ? params.get("id").toString() : null;
            if (id == null) return R.fail("预算ID不能为空");
            GzctEnterpriseBudget budget = budgetMapper.selectById(id);
            if (budget == null) return R.fail("预算记录不存在");
            budget.setStatus("执行中");
            budget.setUpdateTime(LocalDateTime.now());
            budgetMapper.updateById(budget);
            return R.success(true);
        } catch (Exception e) {
            log.error("执行预算失败", e);
            return R.fail("执行失败：" + e.getMessage());
        }
    }

    @Operation(summary = "报表对比分析")
    @PostMapping("/statement/compare")
    public R<Map<String, Object>> compareStatement(@RequestBody Map<String, Object> params) {
        try {
            String id1 = params.get("statementId1") != null ? params.get("statementId1").toString() : null;
            String id2 = params.get("statementId2") != null ? params.get("statementId2").toString() : null;
            Map<String, Object> result = new HashMap<>();
            if (id1 != null) {
                GzctEnterpriseFinancial s1 = financialMapper.selectById(id1);
                if (s1 != null) {
                    Map<String, Object> data1 = new HashMap<>();
                    data1.put("id", s1.getId());
                    data1.put("reportType", s1.getReportType());
                    data1.put("reportPeriod", s1.getReportPeriod());
                    data1.put("reportYear", s1.getReportYear());
                    data1.put("totalAssets", s1.getTotalAssets());
                    data1.put("totalLiabilities", s1.getTotalLiabilities());
                    data1.put("netAssets", s1.getNetAssets());
                    data1.put("operatingRevenue", s1.getOperatingRevenue());
                    data1.put("netProfit", s1.getNetProfit());
                    data1.put("cashFlow", s1.getCashFlow());
                    result.put("statement1", data1);
                }
            }
            if (id2 != null) {
                GzctEnterpriseFinancial s2 = financialMapper.selectById(id2);
                if (s2 != null) {
                    Map<String, Object> data2 = new HashMap<>();
                    data2.put("id", s2.getId());
                    data2.put("reportType", s2.getReportType());
                    data2.put("reportPeriod", s2.getReportPeriod());
                    data2.put("reportYear", s2.getReportYear());
                    data2.put("totalAssets", s2.getTotalAssets());
                    data2.put("totalLiabilities", s2.getTotalLiabilities());
                    data2.put("netAssets", s2.getNetAssets());
                    data2.put("operatingRevenue", s2.getOperatingRevenue());
                    data2.put("netProfit", s2.getNetProfit());
                    data2.put("cashFlow", s2.getCashFlow());
                    result.put("statement2", data2);
                }
            }
            return R.success(result);
        } catch (Exception e) {
            log.error("报表对比失败", e);
            return R.fail("对比失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量更新报表状态")
    @PostMapping("/statement/batch-update-status")
    public R<Boolean> batchUpdateStatementStatus(@RequestBody Map<String, Object> params) {
        try {
            List<String> ids = (List<String>) params.get("ids");
            String status = params.get("status") != null ? params.get("status").toString() : null;
            if (ids == null || ids.isEmpty()) return R.fail("请选择要操作的报表");
            if (status == null) return R.fail("状态不能为空");
            for (String id : ids) {
                GzctEnterpriseFinancial record = financialMapper.selectById(id);
                if (record != null) {
                    record.setStatus(status);
                    record.setUpdateTime(LocalDateTime.now());
                    financialMapper.updateById(record);
                }
            }
            return R.success(true);
        } catch (Exception e) {
            log.error("批量更新状态失败", e);
            return R.fail("批量更新失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量删除报表")
    @PostMapping("/statement/batch-delete")
    public R<Boolean> batchDeleteStatement(@RequestBody Map<String, Object> params) {
        try {
            List<String> ids = (List<String>) params.get("ids");
            if (ids == null || ids.isEmpty()) return R.fail("请选择要删除的报表");
            for (String id : ids) {
                financialMapper.deleteById(id);
            }
            return R.success(true);
        } catch (Exception e) {
            log.error("批量删除失败", e);
            return R.fail("批量删除失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量审核报表")
    @PostMapping("/statement/batch-audit")
    public R<Boolean> batchAuditStatement(@RequestBody Map<String, Object> params) {
        try {
            List<String> ids = (List<String>) params.get("ids");
            if (ids == null || ids.isEmpty()) return R.fail("请选择要审核的报表");
            for (String id : ids) {
                GzctEnterpriseFinancial record = financialMapper.selectById(id);
                if (record != null) {
                    record.setStatus("AUDITED");
                    record.setAuditTime(LocalDateTime.now());
                    record.setUpdateTime(LocalDateTime.now());
                    financialMapper.updateById(record);
                }
            }
            return R.success(true);
        } catch (Exception e) {
            log.error("批量审核失败", e);
            return R.fail("批量审核失败：" + e.getMessage());
        }
    }

    private BigDecimal calcChangeRate(BigDecimal current, BigDecimal previous) {
        if (previous == null || previous.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
        return current.subtract(previous).multiply(new BigDecimal("100")).divide(previous.abs(), 2, RoundingMode.HALF_UP);
    }
}
