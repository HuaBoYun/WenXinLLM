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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 薪酬穿透式监管 - 综合控制器
 * 覆盖：工资总额/效益联动/高管薪酬/激励计划/人工成本/合规检查/监控/预警/穿透分析/驾驶舱
 */
@Tag(name = "薪酬穿透式监管", description = "薪酬分配穿透式监管全接口")
@RestController
@RequestMapping("/v1/supervision/salary")
@Slf4j
public class SalarySupervisionController {

    @Autowired
    private GzctSalaryExecutiveMapper executiveMapper;
    @Autowired
    private GzctSalaryPerformanceLinkMapper performanceLinkMapper;
    @Autowired
    private GzctSalaryIncentivePlanMapper incentivePlanMapper;
    @Autowired
    private GzctSalaryLaborCostMapper laborCostMapper;
    @Autowired
    private GzctSalaryComplianceMapper complianceMapper;
    @Autowired
    private GzctSalaryWarningMapper warningMapper;
    @Autowired
    private TblSalaryTotalMapper salaryTotalMapper;
    @Autowired
    private com.huabo.cybermonitor.util.OrgQueryHelper orgQueryHelper;

    // ==================== 工资总额 ====================
    // 注意：工资总额CRUD接口（total/list, total/add, total/update, total/batch/delete）
    // 已由 SalaryController 提供，此处不再重复定义，避免URL映射冲突

    @Operation(summary = "工资总额预算vs实际")
    @PostMapping("/total/budget-vs-actual")
    public R<Map<String, Object>> budgetVsActual(@RequestBody Map<String, Object> params) {
        try {
            String companyId = (String) params.get("companyId");
            String year = (String) params.get("year");
            Map<String, Object> result = new HashMap<>();
            result.put("companyId", companyId);
            result.put("year", year);

            LambdaQueryWrapper<TblSalaryTotal> wrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(companyId)) {
                wrapper.eq(TblSalaryTotal::getCompanyId, companyId);
            }
            if (StringUtils.isNotEmpty(year)) {
                wrapper.eq(TblSalaryTotal::getReportYear, year);
            }
            List<TblSalaryTotal> records = salaryTotalMapper.selectList(wrapper);

            BigDecimal budgetTotal = BigDecimal.ZERO;
            BigDecimal actualTotal = BigDecimal.ZERO;
            for (TblSalaryTotal r : records) {
                if (r.getBudgetTotal() != null) budgetTotal = budgetTotal.add(r.getBudgetTotal());
                if (r.getActualTotal() != null) actualTotal = actualTotal.add(r.getActualTotal());
            }
            BigDecimal execRate = budgetTotal.compareTo(BigDecimal.ZERO) > 0
                    ? actualTotal.multiply(new BigDecimal("100")).divide(budgetTotal, 2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;

            result.put("budgetTotal", budgetTotal);
            result.put("actualTotal", actualTotal);
            result.put("execRate", execRate);
            return R.success(result);
        } catch (Exception e) {
            log.error("获取预算vs实际失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "工资总额趋势")
    @PostMapping("/total/trend")
    public R<List<Map<String, Object>>> salaryTotalTrend(@RequestBody Map<String, Object> params) {
        try {
            String companyId = (String) params.get("companyId");
            LambdaQueryWrapper<TblSalaryTotal> wrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(companyId)) {
                wrapper.eq(TblSalaryTotal::getCompanyId, companyId);
            }
            wrapper.orderByAsc(TblSalaryTotal::getReportYear);
            List<TblSalaryTotal> records = salaryTotalMapper.selectList(wrapper);

            // 按年度分组汇总
            Map<String, BigDecimal> yearBudget = new LinkedHashMap<>();
            Map<String, BigDecimal> yearActual = new LinkedHashMap<>();
            for (TblSalaryTotal r : records) {
                String year = r.getReportYear();
                if (year == null) continue;
                yearBudget.merge(year, r.getBudgetTotal() != null ? r.getBudgetTotal() : BigDecimal.ZERO, BigDecimal::add);
                yearActual.merge(year, r.getActualTotal() != null ? r.getActualTotal() : BigDecimal.ZERO, BigDecimal::add);
            }

            List<Map<String, Object>> trend = new ArrayList<>();
            for (Map.Entry<String, BigDecimal> entry : yearBudget.entrySet()) {
                Map<String, Object> item = new HashMap<>();
                item.put("period", entry.getKey());
                item.put("budgetTotal", entry.getValue());
                BigDecimal actual = yearActual.getOrDefault(entry.getKey(), BigDecimal.ZERO);
                item.put("actualTotal", actual);
                BigDecimal execRate = entry.getValue().compareTo(BigDecimal.ZERO) > 0
                        ? actual.multiply(new BigDecimal("100")).divide(entry.getValue(), 2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;
                item.put("execRate", execRate);
                trend.add(item);
            }
            return R.success(trend);
        } catch (Exception e) {
            log.error("获取工资总额趋势失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "保存工资总额")
    @PostMapping("/total/save")
    public R<Boolean> saveSalaryTotal(@RequestBody TblSalaryTotal salaryTotal) {
        try {
            // 自动计算人均薪酬
            if (salaryTotal.getActualTotal() != null && salaryTotal.getEmployeeCount() != null && salaryTotal.getEmployeeCount() > 0) {
                salaryTotal.setAvgSalary(salaryTotal.getActualTotal().divide(new BigDecimal(salaryTotal.getEmployeeCount()), 2, RoundingMode.HALF_UP));
            }
            // 自动计算工资增长率对应的联动系数（如果有利润增长率）
            if (salaryTotal.getWageGrowthRate() != null && salaryTotal.getProfitGrowthRate() != null
                    && salaryTotal.getProfitGrowthRate().compareTo(BigDecimal.ZERO) != 0) {
                salaryTotal.setLaborCostRate(salaryTotal.getWageGrowthRate().divide(salaryTotal.getProfitGrowthRate(), 2, RoundingMode.HALF_UP));
            }
            if (salaryTotal.getSalaryId() != null && !salaryTotal.getSalaryId().isEmpty()) {
                salaryTotal.setUpdateTime(LocalDateTime.now());
                salaryTotalMapper.updateById(salaryTotal);
            } else {
                salaryTotal.setCreateTime(LocalDateTime.now());
                salaryTotalMapper.insert(salaryTotal);
            }
            return R.success(true);
        } catch (Exception e) {
            log.error("保存工资总额失败", e);
            return R.fail("保存失败：" + e.getMessage());
        }
    }

    @Operation(summary = "删除工资总额")
    @PostMapping("/total/delete")
    public R<Boolean> deleteSalaryTotal(@RequestBody Map<String, Object> params) {
        try {
            String id = (String) params.get("id");
            if (StringUtils.isNotEmpty(id)) {
                salaryTotalMapper.deleteById(id);
            }
            return R.success(true);
        } catch (Exception e) {
            log.error("删除工资总额失败", e);
            return R.fail("删除失败：" + e.getMessage());
        }
    }

    // ==================== 效益联动 ====================

    @Operation(summary = "效益联动分析列表")
    @PostMapping("/performance-link/analysis")
    public R<PageResult<Map<String, Object>>> performanceLinkList(@RequestBody Map<String, Object> params) {
        try {
            int pageNum = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            String companyId = (String) params.get("companyId");
            String companyName = (String) params.get("companyName");
            String year = (String) params.get("year");

            LambdaQueryWrapper<GzctSalaryPerformanceLink> wrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(companyId)) {
                wrapper.eq(GzctSalaryPerformanceLink::getCompanyId, companyId);
            }
            if (StringUtils.isNotEmpty(companyName)) {
                wrapper.like(GzctSalaryPerformanceLink::getCompanyName, companyName);
            }
            if (StringUtils.isNotEmpty(year)) {
                wrapper.eq(GzctSalaryPerformanceLink::getReportYear, year);
            }
            wrapper.orderByDesc(GzctSalaryPerformanceLink::getCreateTime);

            Page<GzctSalaryPerformanceLink> page = new Page<>(pageNum, pageSize);
            Page<GzctSalaryPerformanceLink> result = new GzctSalaryPerformanceLink().selectPage(page, wrapper);

            // 转换为前端期望的字段名
            List<Map<String, Object>> mappedList = result.getRecords().stream().map(r -> {
                Map<String, Object> m = new HashMap<>();
                m.put("linkId", r.getLinkId());
                m.put("companyId", r.getCompanyId());
                m.put("companyName", r.getCompanyName());
                m.put("reportYear", r.getReportYear());
                m.put("wageGrowthRate", r.getWageGrowthRate());
                m.put("profitGrowthRate", r.getProfitGrowthRate());
                m.put("revenueGrowthRate", r.getRevenueGrowthRate());
                m.put("laborProductivityRate", r.getLaborProductivityRate());
                m.put("linkCoefficient", r.getLinkCoefficient());
                m.put("isReasonable", r.getIsReasonable());
                m.put("riskLevel", r.getRiskLevel());
                m.put("remark", r.getRemark());
                return m;
            }).collect(Collectors.toList());

            PageResult<Map<String, Object>> pageResult = new PageResult<>();
            pageResult.setTotalRecord((int) result.getTotal());
            pageResult.setCurrentPage((int) result.getCurrent());
            pageResult.setPageNumber((int) result.getCurrent());
            pageResult.setTotalPage((int) result.getPages());
            pageResult.setPageSize((int) result.getSize());
            pageResult.setTlist(mappedList);
            return R.success(pageResult);
        } catch (Exception e) {
            log.error("查询效益联动分析失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "计算联动系数")
    @PostMapping("/performance-link/coefficient")
    public R<Map<String, Object>> calcLinkCoefficient(@RequestBody Map<String, Object> params) {
        try {
            String companyId = (String) params.get("companyId");
            String year = (String) params.get("year");
            Map<String, Object> result = new HashMap<>();
            result.put("companyId", companyId);
            result.put("year", year);

            // 查询工资总额记录，用工资增长率/利润增长率计算联动系数
            LambdaQueryWrapper<TblSalaryTotal> wrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(companyId)) {
                wrapper.eq(TblSalaryTotal::getCompanyId, companyId);
            }
            if (StringUtils.isNotEmpty(year)) {
                wrapper.eq(TblSalaryTotal::getReportYear, year);
            }
            List<TblSalaryTotal> records = salaryTotalMapper.selectList(wrapper);

            BigDecimal wageGrowthRate = BigDecimal.ZERO;
            BigDecimal profitGrowthRate = BigDecimal.ZERO;
            BigDecimal linkCoefficient = BigDecimal.ZERO;
            for (TblSalaryTotal r : records) {
                if (r.getWageGrowthRate() != null) wageGrowthRate = wageGrowthRate.add(r.getWageGrowthRate());
                if (r.getProfitGrowthRate() != null) profitGrowthRate = profitGrowthRate.add(r.getProfitGrowthRate());
            }
            if (!records.isEmpty()) {
                wageGrowthRate = wageGrowthRate.divide(new BigDecimal(records.size()), 2, BigDecimal.ROUND_HALF_UP);
                profitGrowthRate = profitGrowthRate.divide(new BigDecimal(records.size()), 2, BigDecimal.ROUND_HALF_UP);
            }
            // 联动系数 = 工资增长率/利润增长率（利润增长率为0时取0）
            if (profitGrowthRate.compareTo(BigDecimal.ZERO) != 0) {
                linkCoefficient = wageGrowthRate.divide(profitGrowthRate, 2, BigDecimal.ROUND_HALF_UP);
            }

            result.put("wageGrowthRate", wageGrowthRate);
            result.put("profitGrowthRate", profitGrowthRate);
            result.put("linkCoefficient", linkCoefficient);
            return R.success(result);
        } catch (Exception e) {
            log.error("计算联动系数失败", e);
            return R.fail("计算失败：" + e.getMessage());
        }
    }

    @Operation(summary = "联动历史趋势")
    @PostMapping("/performance-link/trend")
    public R<List<Map<String, Object>>> linkHistoryTrend(@RequestBody Map<String, Object> params) {
        try {
            String companyId = (String) params.get("companyId");
            LambdaQueryWrapper<TblSalaryTotal> wrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(companyId)) {
                wrapper.eq(TblSalaryTotal::getCompanyId, companyId);
            }
            wrapper.orderByAsc(TblSalaryTotal::getReportYear);
            List<TblSalaryTotal> records = salaryTotalMapper.selectList(wrapper);

            // 按年度分组汇总
            Map<String, BigDecimal> yearWageGrowth = new LinkedHashMap<>();
            Map<String, BigDecimal> yearProfitGrowth = new LinkedHashMap<>();
            Map<String, Integer> yearCount = new LinkedHashMap<>();
            for (TblSalaryTotal r : records) {
                String y = r.getReportYear();
                if (y == null) continue;
                yearWageGrowth.merge(y, r.getWageGrowthRate() != null ? r.getWageGrowthRate() : BigDecimal.ZERO, BigDecimal::add);
                yearProfitGrowth.merge(y, r.getProfitGrowthRate() != null ? r.getProfitGrowthRate() : BigDecimal.ZERO, BigDecimal::add);
                yearCount.merge(y, 1, Integer::sum);
            }

            List<Map<String, Object>> trend = new ArrayList<>();
            for (Map.Entry<String, BigDecimal> entry : yearWageGrowth.entrySet()) {
                Map<String, Object> item = new HashMap<>();
                item.put("year", entry.getKey());
                int count = yearCount.getOrDefault(entry.getKey(), 1);
                BigDecimal wageGrowth = entry.getValue().divide(new BigDecimal(count), 2, BigDecimal.ROUND_HALF_UP);
                BigDecimal profitGrowth = yearProfitGrowth.getOrDefault(entry.getKey(), BigDecimal.ZERO)
                        .divide(new BigDecimal(count), 2, BigDecimal.ROUND_HALF_UP);
                BigDecimal linkCoeff = profitGrowth.compareTo(BigDecimal.ZERO) != 0
                        ? wageGrowth.divide(profitGrowth, 2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;
                item.put("wageGrowthRate", wageGrowth);
                item.put("profitGrowthRate", profitGrowth);
                item.put("linkCoefficient", linkCoeff);
                trend.add(item);
            }
            return R.success(trend);
        } catch (Exception e) {
            log.error("获取联动趋势失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    // ==================== 高管薪酬 ====================

    @Operation(summary = "高管薪酬列表")
    @PostMapping("/executive/list")
    public R<PageResult<Map<String, Object>>> executiveList(@RequestBody Map<String, Object> params) {
        try {
            int pageNum = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            String companyId = (String) params.get("companyId");
            String companyName = (String) params.get("companyName");

            LambdaQueryWrapper<GzctSalaryExecutive> wrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(companyId)) {
                wrapper.eq(GzctSalaryExecutive::getCompanyId, companyId);
            }
            if (StringUtils.isNotEmpty(companyName)) {
                wrapper.like(GzctSalaryExecutive::getCompanyName, companyName);
            }
            wrapper.orderByDesc(GzctSalaryExecutive::getCreateTime);

            Page<GzctSalaryExecutive> page = new Page<>(pageNum, pageSize);
            Page<GzctSalaryExecutive> result = new GzctSalaryExecutive().selectPage(page, wrapper);

            // 转换为前端期望的字段名
            List<Map<String, Object>> mappedList = result.getRecords().stream().map(r -> {
                Map<String, Object> m = new HashMap<>();
                m.put("execId", r.getExecId());
                m.put("companyId", r.getCompanyId());
                m.put("companyName", r.getCompanyName());
                m.put("execName", r.getExecName());
                m.put("execPosition", r.getExecPosition());
                m.put("baseSalary", r.getBaseSalary());
                m.put("perfSalary", r.getPerfSalary());
                m.put("termIncentive", r.getTermIncentive());
                m.put("otherIncome", r.getOtherIncome());
                m.put("totalComp", r.getTotalComp());
                m.put("empAvgSalary", r.getEmpAvgSalary());
                m.put("ratioToAvg", r.getRatioToAvg());
                m.put("reportYear", r.getReportYear());
                m.put("isCompliant", r.getIsCompliant());
                m.put("compliantIssue", r.getCompliantIssue());
                return m;
            }).collect(Collectors.toList());

            PageResult<Map<String, Object>> pageResult = new PageResult<>();
            pageResult.setTotalRecord((int) result.getTotal());
            pageResult.setCurrentPage((int) result.getCurrent());
            pageResult.setPageNumber((int) result.getCurrent());
            pageResult.setTotalPage((int) result.getPages());
            pageResult.setPageSize((int) result.getSize());
            pageResult.setTlist(mappedList);
            return R.success(pageResult);
        } catch (Exception e) {
            log.error("查询高管薪酬列表失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "高管薪酬详情")
    @PostMapping("/executive/detail")
    public R<GzctSalaryExecutive> executiveDetail(@RequestBody Map<String, Object> params) {
        try {
            String execId = (String) params.get("execId");
            return R.success(executiveMapper.selectById(execId));
        } catch (Exception e) {
            log.error("查询高管薪酬详情失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "高管薪酬对标")
    @PostMapping("/executive/benchmark")
    public R<Map<String, Object>> executiveBenchmark(@RequestBody Map<String, Object> params) {
        try {
            String companyId = (String) params.get("companyId");
            Map<String, Object> result = new HashMap<>();
            result.put("companyId", companyId);

            LambdaQueryWrapper<GzctSalaryExecutive> wrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(companyId)) {
                wrapper.eq(GzctSalaryExecutive::getCompanyId, companyId);
            }
            List<GzctSalaryExecutive> executives = executiveMapper.selectList(wrapper);

            BigDecimal avgTotalComp = BigDecimal.ZERO;
            BigDecimal maxTotalComp = BigDecimal.ZERO;
            BigDecimal avgRatio = BigDecimal.ZERO;
            int count = 0;
            for (GzctSalaryExecutive e : executives) {
                if (e.getTotalComp() != null) {
                    avgTotalComp = avgTotalComp.add(e.getTotalComp());
                    if (e.getTotalComp().compareTo(maxTotalComp) > 0) maxTotalComp = e.getTotalComp();
                    count++;
                }
                if (e.getRatioToAvg() != null) avgRatio = avgRatio.add(e.getRatioToAvg());
            }
            if (count > 0) {
                avgTotalComp = avgTotalComp.divide(new BigDecimal(count), 2, BigDecimal.ROUND_HALF_UP);
                avgRatio = avgRatio.divide(new BigDecimal(count), 2, BigDecimal.ROUND_HALF_UP);
            }

            result.put("avgTotalComp", avgTotalComp);
            result.put("maxTotalComp", maxTotalComp);
            result.put("avgRatio", avgRatio);
            return R.success(result);
        } catch (Exception e) {
            log.error("获取高管薪酬对标失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    // ==================== 激励计划 ====================

    @Operation(summary = "激励计划列表")
    @PostMapping("/incentive/list")
    public R<PageResult<Map<String, Object>>> incentiveList(@RequestBody Map<String, Object> params) {
        try {
            int pageNum = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;

            LambdaQueryWrapper<GzctSalaryIncentivePlan> wrapper = new LambdaQueryWrapper<>();
            wrapper.orderByDesc(GzctSalaryIncentivePlan::getCreateTime);

            Page<GzctSalaryIncentivePlan> page = new Page<>(pageNum, pageSize);
            Page<GzctSalaryIncentivePlan> result = new GzctSalaryIncentivePlan().selectPage(page, wrapper);

            // 转换为前端期望的字段名
            List<Map<String, Object>> mappedList = result.getRecords().stream().map(r -> {
                Map<String, Object> m = new HashMap<>();
                m.put("planId", r.getPlanId());
                m.put("planNo", r.getPlanId()); // 前端用planNo
                m.put("companyId", r.getCompanyId());
                m.put("companyName", r.getCompanyName());
                m.put("planName", r.getPlanName());
                m.put("incentiveType", r.getPlanType()); // 前端用incentiveType
                m.put("planType", r.getPlanType());
                m.put("scale", r.getTotalAmount()); // 前端用scale
                m.put("totalAmount", r.getTotalAmount());
                m.put("personCount", r.getParticipantCount()); // 前端用personCount
                m.put("participantCount", r.getParticipantCount());
                m.put("startDate", r.getStartDate() != null ? r.getStartDate().toString() : null);
                m.put("endDate", r.getEndDate() != null ? r.getEndDate().toString() : null);
                m.put("progressStatus", r.getProgressStatus());
                // STATUS优先用数据库值，若为空则从PROGRESS_STATUS映射
                String statusVal = r.getStatus();
                if (StringUtils.isEmpty(statusVal)) {
                    statusVal = r.getProgressStatus();
                    if ("DRAFT".equals(statusVal)) statusVal = "PENDING";
                    else if ("ACTIVE".equals(statusVal)) statusVal = "ACTIVE";
                    else if ("COMPLETED".equals(statusVal)) statusVal = "COMPLETED";
                    else if ("TERMINATED".equals(statusVal)) statusVal = "REJECTED";
                    else statusVal = "PENDING";
                }
                m.put("approvalStatus", statusVal);
                m.put("status", statusVal);
                // COMPLETION_RATE优先用数据库值，若为空则按日期计算
                BigDecimal completionRate = r.getCompletionRate();
                if (completionRate == null) {
                    completionRate = BigDecimal.ZERO;
                    if (r.getStartDate() != null && r.getEndDate() != null) {
                        long totalDays = r.getEndDate().toEpochDay() - r.getStartDate().toEpochDay();
                        long passedDays = java.time.LocalDate.now().toEpochDay() - r.getStartDate().toEpochDay();
                        if (totalDays > 0) {
                            completionRate = new BigDecimal(Math.max(0, Math.min(100, passedDays * 100 / totalDays)));
                        }
                    }
                    if ("COMPLETED".equals(statusVal)) completionRate = new BigDecimal("100");
                }
                m.put("progress", completionRate.intValue());
                m.put("completionRate", completionRate);
                m.put("isCompliant", r.getIsCompliant());
                m.put("compliantIssue", r.getCompliantIssue());
                return m;
            }).collect(Collectors.toList());

            PageResult<Map<String, Object>> pageResult = new PageResult<>();
            pageResult.setTotalRecord((int) result.getTotal());
            pageResult.setCurrentPage((int) result.getCurrent());
            pageResult.setPageNumber((int) result.getCurrent());
            pageResult.setTotalPage((int) result.getPages());
            pageResult.setPageSize((int) result.getSize());
            pageResult.setTlist(mappedList);
            return R.success(pageResult);
        } catch (Exception e) {
            log.error("查询激励计划列表失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "保存激励计划")
    @PostMapping("/incentive/save")
    public R<Boolean> saveIncentivePlan(@RequestBody Map<String, Object> params) {
        try {
            GzctSalaryIncentivePlan plan = new GzctSalaryIncentivePlan();
            // 兼容前端字段名映射
            plan.setPlanId((String) params.get("planId"));
            plan.setCompanyId((String) params.get("companyId"));
            plan.setCompanyName((String) params.get("companyName"));
            plan.setPlanName((String) params.get("planName"));
            // incentiveType → planType
            String planType = (String) params.get("planType");
            if (StringUtils.isEmpty(planType)) planType = (String) params.get("incentiveType");
            plan.setPlanType(planType);
            // scale → totalAmount
            Object scaleObj = params.get("scale");
            if (scaleObj == null) scaleObj = params.get("totalAmount");
            if (scaleObj != null) plan.setTotalAmount(new BigDecimal(scaleObj.toString()));
            // personCount → participantCount
            Object personObj = params.get("personCount");
            if (personObj == null) personObj = params.get("participantCount");
            if (personObj != null) plan.setParticipantCount(Integer.parseInt(personObj.toString()));
            // 日期
            String startDate = (String) params.get("startDate");
            String endDate = (String) params.get("endDate");
            if (StringUtils.isNotEmpty(startDate)) plan.setStartDate(java.time.LocalDate.parse(startDate));
            if (StringUtils.isNotEmpty(endDate)) plan.setEndDate(java.time.LocalDate.parse(endDate));

            // 自动计算实施进度（completionRate）
            if (plan.getStartDate() != null && plan.getEndDate() != null) {
                long totalDays = plan.getEndDate().toEpochDay() - plan.getStartDate().toEpochDay();
                long passedDays = java.time.LocalDate.now().toEpochDay() - plan.getStartDate().toEpochDay();
                if (totalDays > 0) {
                    int progress = (int) Math.max(0, Math.min(100, passedDays * 100 / totalDays));
                    plan.setCompletionRate(new BigDecimal(progress));
                }
            }

            // 状态
            plan.setIsCompliant((String) params.get("isCompliant"));

            if (StringUtils.isNotEmpty(plan.getPlanId())) {
                plan.setUpdateTime(LocalDateTime.now());
                // 编辑时保留原有状态，不覆盖
                String status = (String) params.get("status");
                if (StringUtils.isNotEmpty(status)) plan.setStatus(status);
                incentivePlanMapper.updateById(plan);
            } else {
                plan.setCreateTime(LocalDateTime.now());
                plan.setStatus("PENDING");
                plan.setProgressStatus("DRAFT");
                incentivePlanMapper.insert(plan);
            }
            return R.success(true);
        } catch (Exception e) {
            log.error("保存激励计划失败", e);
            return R.fail("保存失败：" + e.getMessage());
        }
    }

    @Operation(summary = "激励计划进度")
    @PostMapping("/incentive/progress")
    public R<Map<String, Object>> incentiveProgress(@RequestBody Map<String, Object> params) {
        try {
            String planId = (String) params.get("planId");
            Map<String, Object> result = new HashMap<>();
            result.put("planId", planId);

            if (StringUtils.isNotEmpty(planId)) {
                GzctSalaryIncentivePlan plan = incentivePlanMapper.selectById(planId);
                if (plan != null) {
                    result.put("progressStatus", plan.getStatus() != null ? plan.getStatus() : "ACTIVE");
                    result.put("completionRate", plan.getCompletionRate() != null ? plan.getCompletionRate() : BigDecimal.ZERO);
                    result.put("planName", plan.getPlanName());
                    result.put("startDate", plan.getStartDate());
                    result.put("endDate", plan.getEndDate());
                } else {
                    result.put("progressStatus", "NOT_FOUND");
                    result.put("completionRate", BigDecimal.ZERO);
                }
            } else {
                // 没有指定planId时，返回所有激励计划汇总进度
                List<GzctSalaryIncentivePlan> plans = incentivePlanMapper.selectList(null);
                BigDecimal avgCompletion = BigDecimal.ZERO;
                int activeCount = 0;
                for (GzctSalaryIncentivePlan p : plans) {
                    if (p.getCompletionRate() != null) avgCompletion = avgCompletion.add(p.getCompletionRate());
                    if ("ACTIVE".equals(p.getStatus())) activeCount++;
                }
                if (!plans.isEmpty()) avgCompletion = avgCompletion.divide(new BigDecimal(plans.size()), 2, BigDecimal.ROUND_HALF_UP);
                result.put("progressStatus", "AGGREGATED");
                result.put("completionRate", avgCompletion);
                result.put("totalPlans", plans.size());
                result.put("activePlans", activeCount);
            }
            return R.success(result);
        } catch (Exception e) {
            log.error("获取激励计划进度失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    // ==================== 人工成本 ====================

    @Operation(summary = "人工成本分析列表")
    @PostMapping("/labor-cost/analysis")
    public R<PageResult<Map<String, Object>>> laborCostList(@RequestBody Map<String, Object> params) {
        try {
            int pageNum = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            String companyId = (String) params.get("companyId");
            String companyName = (String) params.get("companyName");

            LambdaQueryWrapper<GzctSalaryLaborCost> wrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(companyId)) {
                wrapper.eq(GzctSalaryLaborCost::getCompanyId, companyId);
            }
            if (StringUtils.isNotEmpty(companyName)) {
                wrapper.like(GzctSalaryLaborCost::getCompanyName, companyName);
            }
            wrapper.orderByDesc(GzctSalaryLaborCost::getCreateTime);

            Page<GzctSalaryLaborCost> page = new Page<>(pageNum, pageSize);
            Page<GzctSalaryLaborCost> result = new GzctSalaryLaborCost().selectPage(page, wrapper);

            // 转换为前端期望的字段名
            List<Map<String, Object>> mappedList = result.getRecords().stream().map(r -> {
                Map<String, Object> m = new HashMap<>();
                m.put("costId", r.getCostId());
                m.put("companyId", r.getCompanyId());
                m.put("companyName", r.getCompanyName());
                m.put("reportYear", r.getReportYear());
                m.put("totalCost", r.getTotalLaborCost()); // 前端用totalCost
                m.put("totalLaborCost", r.getTotalLaborCost());
                m.put("wage", r.getSalaryCost()); // 前端用wage
                m.put("salaryCost", r.getSalaryCost());
                m.put("socialInsurance", r.getSocialCost()); // 前端用socialInsurance
                m.put("socialCost", r.getSocialCost());
                m.put("housingFund", r.getHousingFund() != null ? r.getHousingFund() : (r.getSalaryCost() != null ? r.getSalaryCost().multiply(new BigDecimal("0.12")).setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO));
                m.put("welfare", r.getWelfareCost()); // 前端用welfare
                m.put("welfareCost", r.getWelfareCost());
                m.put("trainingCost", r.getTrainingCost());
                m.put("otherCost", r.getOtherCost());
                m.put("laborCostRate", r.getLaborCostRate());
                m.put("laborProductivity", r.getLaborProductivity());
                m.put("yoyChange", r.getYoyChange() != null ? r.getYoyChange() : (r.getLaborCostRate() != null ? r.getLaborCostRate().multiply(new BigDecimal("0.6")).setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO));
                m.put("perCapitaCost", r.getPerCapitaCost() != null ? r.getPerCapitaCost() : (r.getTotalLaborCost() != null ? r.getTotalLaborCost().divide(new BigDecimal("10000"), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO));
                m.put("riskLevel", r.getRiskLevel());
                return m;
            }).collect(Collectors.toList());

            PageResult<Map<String, Object>> pageResult = new PageResult<>();
            pageResult.setTotalRecord((int) result.getTotal());
            pageResult.setCurrentPage((int) result.getCurrent());
            pageResult.setPageNumber((int) result.getCurrent());
            pageResult.setTotalPage((int) result.getPages());
            pageResult.setPageSize((int) result.getSize());
            pageResult.setTlist(mappedList);
            return R.success(pageResult);
        } catch (Exception e) {
            log.error("查询人工成本分析失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "人工成本趋势")
    @PostMapping("/labor-cost/trend")
    public R<List<Map<String, Object>>> laborCostTrend(@RequestBody Map<String, Object> params) {
        try {
            String companyId = (String) params.get("companyId");
            LambdaQueryWrapper<GzctSalaryLaborCost> wrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(companyId)) {
                wrapper.eq(GzctSalaryLaborCost::getCompanyId, companyId);
            }
            wrapper.orderByAsc(GzctSalaryLaborCost::getReportYear);
            List<GzctSalaryLaborCost> records = laborCostMapper.selectList(wrapper);

            // 按年度分组汇总
            Map<String, BigDecimal> yearCost = new LinkedHashMap<>();
            Map<String, BigDecimal> yearRate = new LinkedHashMap<>();
            Map<String, Integer> yearCount = new LinkedHashMap<>();
            for (GzctSalaryLaborCost r : records) {
                String y = r.getReportYear();
                if (y == null) continue;
                yearCost.merge(y, r.getTotalLaborCost() != null ? r.getTotalLaborCost() : BigDecimal.ZERO, BigDecimal::add);
                yearRate.merge(y, r.getLaborCostRate() != null ? r.getLaborCostRate() : BigDecimal.ZERO, BigDecimal::add);
                yearCount.merge(y, 1, Integer::sum);
            }

            List<Map<String, Object>> trend = new ArrayList<>();
            for (Map.Entry<String, BigDecimal> entry : yearCost.entrySet()) {
                Map<String, Object> item = new HashMap<>();
                String y = entry.getKey();
                int count = yearCount.getOrDefault(y, 1);
                item.put("year", y);
                item.put("totalLaborCost", entry.getValue());
                item.put("laborCostRate", yearRate.getOrDefault(y, BigDecimal.ZERO).divide(new BigDecimal(count), 2, BigDecimal.ROUND_HALF_UP));
                trend.add(item);
            }
            return R.success(trend);
        } catch (Exception e) {
            log.error("获取人工成本趋势失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "人工成本对标")
    @PostMapping("/labor-cost/benchmark")
    public R<Map<String, Object>> laborCostBenchmark(@RequestBody Map<String, Object> params) {
        try {
            String companyId = (String) params.get("companyId");
            Map<String, Object> result = new HashMap<>();
            result.put("companyId", companyId);

            // 查询该公司人工成本
            LambdaQueryWrapper<GzctSalaryLaborCost> compWrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(companyId)) {
                compWrapper.eq(GzctSalaryLaborCost::getCompanyId, companyId);
            }
            List<GzctSalaryLaborCost> companyRecords = laborCostMapper.selectList(compWrapper);
            BigDecimal companyRate = BigDecimal.ZERO;
            if (!companyRecords.isEmpty()) {
                BigDecimal sum = BigDecimal.ZERO;
                for (GzctSalaryLaborCost r : companyRecords) {
                    if (r.getLaborCostRate() != null) sum = sum.add(r.getLaborCostRate());
                }
                companyRate = sum.divide(new BigDecimal(companyRecords.size()), 2, BigDecimal.ROUND_HALF_UP);
            }

            // 查询行业平均
            List<GzctSalaryLaborCost> allRecords = laborCostMapper.selectList(null);
            BigDecimal industryAvg = BigDecimal.ZERO;
            if (!allRecords.isEmpty()) {
                BigDecimal sum = BigDecimal.ZERO;
                for (GzctSalaryLaborCost r : allRecords) {
                    if (r.getLaborCostRate() != null) sum = sum.add(r.getLaborCostRate());
                }
                industryAvg = sum.divide(new BigDecimal(allRecords.size()), 2, BigDecimal.ROUND_HALF_UP);
            }

            result.put("industryAvg", industryAvg);
            result.put("companyRate", companyRate);
            return R.success(result);
        } catch (Exception e) {
            log.error("获取人工成本对标失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    // ==================== 合规检查 ====================

    @Operation(summary = "执行合规检查")
    @PostMapping("/compliance/check")
    public R<Map<String, Object>> runComplianceCheck(@RequestBody Map<String, Object> params) {
        try {
            Map<String, Object> result = new HashMap<>();
            int newIssues = 0;
            String currentYear = String.valueOf(java.time.LocalDate.now().getYear());

            // 1. 检查工资总额超预算（不合规）
            List<TblSalaryTotal> salaryRecords = salaryTotalMapper.selectList(null);
            for (TblSalaryTotal r : salaryRecords) {
                if (r.getActualTotal() != null && r.getBudgetTotal() != null
                        && r.getBudgetTotal().compareTo(BigDecimal.ZERO) > 0
                        && r.getActualTotal().compareTo(r.getBudgetTotal()) > 0) {
                    // 检查是否已存在相同问题
                    LambdaQueryWrapper<GzctSalaryCompliance> existWrapper = new LambdaQueryWrapper<>();
                    existWrapper.eq(GzctSalaryCompliance::getCompanyName, r.getCompanyName())
                            .eq(GzctSalaryCompliance::getCheckYear, currentYear)
                            .like(GzctSalaryCompliance::getIssueDesc, "工资总额超预算");
                    if (complianceMapper.selectCount(existWrapper) == 0) {
                        GzctSalaryCompliance issue = new GzctSalaryCompliance();
                        issue.setCompanyId(r.getCompanyId());
                        issue.setCompanyName(r.getCompanyName());
                        issue.setCheckYear(currentYear);
                        issue.setCheckType("工资总额管控");
                        issue.setCheckResult("FAIL");
                        BigDecimal execRate = r.getActualTotal().multiply(new BigDecimal("100")).divide(r.getBudgetTotal(), 2, RoundingMode.HALF_UP);
                        issue.setIssueDesc("工资总额超预算，执行率" + execRate + "%，超出核准额度");
                        issue.setIsCompliant("0");
                        issue.setRiskLevel("HIGH");
                        issue.setRectStatus("PENDING");
                        issue.setCreateTime(LocalDateTime.now());
                        complianceMapper.insert(issue);
                        newIssues++;
                    }
                }
            }

            // 2. 检查高管薪酬超8倍上限（不合法）
            List<GzctSalaryExecutive> executives = executiveMapper.selectList(null);
            for (GzctSalaryExecutive e : executives) {
                if (e.getRatioToAvg() != null && e.getRatioToAvg().compareTo(new BigDecimal("8")) > 0) {
                    LambdaQueryWrapper<GzctSalaryCompliance> existWrapper = new LambdaQueryWrapper<>();
                    existWrapper.eq(GzctSalaryCompliance::getCompanyName, e.getCompanyName())
                            .eq(GzctSalaryCompliance::getCheckYear, currentYear)
                            .like(GzctSalaryCompliance::getIssueDesc, e.getExecName());
                    if (complianceMapper.selectCount(existWrapper) == 0) {
                        GzctSalaryCompliance issue = new GzctSalaryCompliance();
                        issue.setCompanyId(e.getCompanyId());
                        issue.setCompanyName(e.getCompanyName());
                        issue.setCheckYear(currentYear);
                        issue.setCheckType("高管薪酬限高");
                        issue.setCheckResult("FAIL");
                        issue.setIssueDesc(e.getExecName() + "薪酬为央企均值" + e.getRatioToAvg() + "倍，超过8倍上限");
                        issue.setIsCompliant("0");
                        issue.setRiskLevel("HIGH");
                        issue.setRectStatus("PENDING");
                        issue.setCreateTime(LocalDateTime.now());
                        complianceMapper.insert(issue);
                        newIssues++;
                    }
                }
            }

            // 3. 检查效益联动脱钩（不合理）
            List<GzctSalaryPerformanceLink> linkRecords = performanceLinkMapper.selectList(null);
            for (GzctSalaryPerformanceLink lr : linkRecords) {
                if (lr.getLinkCoefficient() != null && (lr.getLinkCoefficient().compareTo(new BigDecimal("0.6")) < 0
                        || lr.getLinkCoefficient().compareTo(new BigDecimal("1.5")) > 0)) {
                    LambdaQueryWrapper<GzctSalaryCompliance> existWrapper = new LambdaQueryWrapper<>();
                    existWrapper.eq(GzctSalaryCompliance::getCompanyName, lr.getCompanyName())
                            .eq(GzctSalaryCompliance::getCheckYear, currentYear)
                            .like(GzctSalaryCompliance::getIssueDesc, "效益联动");
                    if (complianceMapper.selectCount(existWrapper) == 0) {
                        GzctSalaryCompliance issue = new GzctSalaryCompliance();
                        issue.setCompanyId(lr.getCompanyId());
                        issue.setCompanyName(lr.getCompanyName());
                        issue.setCheckYear(currentYear);
                        issue.setCheckType("效益联动结构");
                        issue.setCheckResult("WARNING");
                        String desc = lr.getLinkCoefficient().compareTo(new BigDecimal("1.5")) > 0
                                ? "效益联动系数" + lr.getLinkCoefficient() + "，工资增长严重超过效益增长"
                                : "效益联动系数" + lr.getLinkCoefficient() + "，工资与效益严重脱钩";
                        issue.setIssueDesc(desc);
                        issue.setIsCompliant("2");
                        issue.setRiskLevel("MEDIUM");
                        issue.setRectStatus("PENDING");
                        issue.setCreateTime(LocalDateTime.now());
                        complianceMapper.insert(issue);
                        newIssues++;
                    }
                }
            }

            // 统计结果
            long failCount = complianceMapper.selectCount(new LambdaQueryWrapper<GzctSalaryCompliance>()
                    .eq(GzctSalaryCompliance::getCheckResult, "FAIL"));
            long warningCount = complianceMapper.selectCount(new LambdaQueryWrapper<GzctSalaryCompliance>()
                    .eq(GzctSalaryCompliance::getCheckResult, "WARNING"));

            result.put("newIssues", newIssues);
            result.put("failCount", failCount);
            result.put("warningCount", warningCount);
            result.put("totalChecks", failCount + warningCount);
            return R.success(result);
        } catch (Exception e) {
            log.error("执行合规检查失败", e);
            return R.fail("执行失败：" + e.getMessage());
        }
    }

    @Operation(summary = "合规问题列表")
    @PostMapping("/compliance/list")
    public R<PageResult<Map<String, Object>>> complianceList(@RequestBody Map<String, Object> params) {
        try {
            int pageNum = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;

            LambdaQueryWrapper<GzctSalaryCompliance> wrapper = new LambdaQueryWrapper<>();
            wrapper.orderByDesc(GzctSalaryCompliance::getCreateTime);

            Page<GzctSalaryCompliance> page = new Page<>(pageNum, pageSize);
            Page<GzctSalaryCompliance> result = new GzctSalaryCompliance().selectPage(page, wrapper);

            // 转换为前端期望的字段名
            List<Map<String, Object>> mappedList = result.getRecords().stream().map(r -> {
                Map<String, Object> m = new HashMap<>();
                m.put("checkId", r.getCheckId());
                m.put("issueNo", r.getCheckId()); // 前端用issueNo
                m.put("companyId", r.getCompanyId());
                m.put("companyName", r.getCompanyName());
                m.put("checkYear", r.getCheckYear());
                // 前端tab按"不合法/不合规/不合理"分类，需从checkResult映射
                String dimension;
                if ("FAIL".equals(r.getCheckResult())) {
                    dimension = "不合法";
                } else if ("WARNING".equals(r.getCheckResult())) {
                    if (r.getCheckType() != null && (r.getCheckType().contains("增幅") || r.getCheckType().contains("联动") || r.getCheckType().contains("结构"))) {
                        dimension = "不合理";
                    } else {
                        dimension = "不合规";
                    }
                } else {
                    dimension = "不合规";
                }
                m.put("dimension", dimension);
                m.put("checkType", r.getCheckType());
                m.put("checkResult", r.getCheckResult());
                m.put("issueDesc", r.getIssueDesc());
                m.put("rectification", r.getRectification());
                // 虚拟字段：isCompliant从checkResult推算
                String isCompliant = r.getIsCompliant();
                if (StringUtils.isEmpty(isCompliant) && r.getCheckResult() != null) {
                    switch (r.getCheckResult()) {
                        case "PASS": isCompliant = "1"; break;
                        case "FAIL": isCompliant = "0"; break;
                        case "WARNING": isCompliant = "2"; break;
                        default: isCompliant = "1"; break;
                    }
                }
                m.put("isCompliant", isCompliant);
                // 风险等级: 从riskLevel或从checkResult映射
                String riskLevel = r.getRiskLevel();
                if (StringUtils.isEmpty(riskLevel) && r.getCheckResult() != null) {
                    switch (r.getCheckResult()) {
                        case "FAIL": riskLevel = "HIGH"; break;
                        case "WARNING": riskLevel = "MEDIUM"; break;
                        default: riskLevel = "LOW"; break;
                    }
                }
                m.put("riskLevel", riskLevel);
                // 整改状态：数据库为空时设为PENDING（未整改即待整改）
                String rectStatus = r.getRectStatus();
                if (StringUtils.isEmpty(rectStatus)) {
                    rectStatus = "PENDING";
                }
                m.put("status", rectStatus);
                m.put("rectStatus", rectStatus);
                m.put("rectDeadline", r.getRectDeadline() != null ? r.getRectDeadline().toString() : null);
                return m;
            }).collect(Collectors.toList());

            PageResult<Map<String, Object>> pageResult = new PageResult<>();
            pageResult.setTotalRecord((int) result.getTotal());
            pageResult.setCurrentPage((int) result.getCurrent());
            pageResult.setPageNumber((int) result.getCurrent());
            pageResult.setTotalPage((int) result.getPages());
            pageResult.setPageSize((int) result.getSize());
            pageResult.setTlist(mappedList);
            return R.success(pageResult);
        } catch (Exception e) {
            log.error("查询合规问题列表失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "发送整改通知")
    @PostMapping("/compliance/rectification")
    public R<Boolean> sendRectification(@RequestBody Map<String, Object> params) {
        try {
            String issueNo = (String) params.get("issueNo");
            String complianceId = (String) params.get("complianceId");
            String requirement = (String) params.get("requirement");
            String deadline = (String) params.get("deadline");
            String responsible = (String) params.get("responsible");

            // 优先用issueNo查找，兼容complianceId
            String queryId = StringUtils.isNotEmpty(issueNo) ? issueNo : complianceId;
            if (StringUtils.isNotEmpty(queryId)) {
                GzctSalaryCompliance compliance = complianceMapper.selectById(queryId);
                if (compliance != null) {
                    compliance.setRectStatus("INVESTIGATING");
                    compliance.setRectification(requirement);
                    if (StringUtils.isNotEmpty(deadline)) {
                        compliance.setRectDeadline(java.time.LocalDate.parse(deadline));
                    }
                    compliance.setUpdateTime(LocalDateTime.now());
                    complianceMapper.updateById(compliance);
                }
            }
            return R.success(true);
        } catch (Exception e) {
            log.error("发送整改通知失败", e);
            return R.fail("发送失败：" + e.getMessage());
        }
    }

    // ==================== 监控接口 ====================

    @Operation(summary = "预算执行监控")
    @PostMapping("/monitor/budget-execution")
    public R<Map<String, Object>> monitorBudgetExecution(@RequestBody Map<String, Object> params) {
        try {
            String companyId = (String) params.get("companyId");
            Map<String, Object> result = new HashMap<>();
            result.put("companyId", companyId);

            LambdaQueryWrapper<TblSalaryTotal> wrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(companyId)) {
                wrapper.eq(TblSalaryTotal::getCompanyId, companyId);
            }
            wrapper.orderByDesc(TblSalaryTotal::getCreateTime);
            List<TblSalaryTotal> records = salaryTotalMapper.selectList(wrapper);

            BigDecimal budgetTotal = BigDecimal.ZERO;
            BigDecimal actualTotal = BigDecimal.ZERO;
            for (TblSalaryTotal r : records) {
                if (r.getBudgetTotal() != null) budgetTotal = budgetTotal.add(r.getBudgetTotal());
                if (r.getActualTotal() != null) actualTotal = actualTotal.add(r.getActualTotal());
            }
            BigDecimal execRate = budgetTotal.compareTo(BigDecimal.ZERO) > 0
                    ? actualTotal.multiply(new BigDecimal("100")).divide(budgetTotal, 2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;

            result.put("budgetTotal", budgetTotal);
            result.put("actualTotal", actualTotal);
            result.put("execRate", execRate);
            result.put("isOverBudget", execRate.compareTo(new BigDecimal("100")) > 0);
            return R.success(result);
        } catch (Exception e) {
            log.error("预算执行监控失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "效益联动监控")
    @PostMapping("/monitor/performance-link")
    public R<Map<String, Object>> monitorPerformanceLink(@RequestBody Map<String, Object> params) {
        try {
            String companyId = (String) params.get("companyId");
            Map<String, Object> result = new HashMap<>();
            result.put("companyId", companyId);

            LambdaQueryWrapper<GzctSalaryPerformanceLink> wrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(companyId)) {
                wrapper.eq(GzctSalaryPerformanceLink::getCompanyId, companyId);
            }
            List<GzctSalaryPerformanceLink> records = performanceLinkMapper.selectList(wrapper);

            BigDecimal avgCoefficient = BigDecimal.ZERO;
            boolean isReasonable = true;
            if (!records.isEmpty()) {
                BigDecimal sum = BigDecimal.ZERO;
                for (GzctSalaryPerformanceLink r : records) {
                    if (r.getLinkCoefficient() != null) sum = sum.add(r.getLinkCoefficient());
                }
                avgCoefficient = sum.divide(new BigDecimal(records.size()), 2, BigDecimal.ROUND_HALF_UP);
                // 联动系数在0.5~2.0之间认为合理
                isReasonable = avgCoefficient.compareTo(new BigDecimal("0.5")) >= 0 && avgCoefficient.compareTo(new BigDecimal("2.0")) <= 0;
            }

            result.put("linkCoefficient", avgCoefficient);
            result.put("isReasonable", isReasonable);
            return R.success(result);
        } catch (Exception e) {
            log.error("效益联动监控失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "高管薪酬监控")
    @PostMapping("/monitor/executive-pay")
    public R<Map<String, Object>> monitorExecutivePay(@RequestBody Map<String, Object> params) {
        try {
            String companyId = (String) params.get("companyId");
            Map<String, Object> result = new HashMap<>();
            result.put("companyId", companyId);

            LambdaQueryWrapper<GzctSalaryExecutive> wrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(companyId)) {
                wrapper.eq(GzctSalaryExecutive::getCompanyId, companyId);
            }
            List<GzctSalaryExecutive> executives = executiveMapper.selectList(wrapper);

            BigDecimal avgRatio = BigDecimal.ZERO;
            BigDecimal maxRatio = BigDecimal.ZERO;
            boolean isCompliant = true;
            for (GzctSalaryExecutive e : executives) {
                if (e.getRatioToAvg() != null) {
                    avgRatio = avgRatio.add(e.getRatioToAvg());
                    if (e.getRatioToAvg().compareTo(maxRatio) > 0) maxRatio = e.getRatioToAvg();
                }
                // 超过7倍认为不合规
                if (e.getRatioToAvg() != null && e.getRatioToAvg().compareTo(new BigDecimal("7")) > 0) {
                    isCompliant = false;
                }
            }
            if (!executives.isEmpty()) {
                avgRatio = avgRatio.divide(new BigDecimal(executives.size()), 2, BigDecimal.ROUND_HALF_UP);
            }

            result.put("avgRatio", avgRatio);
            result.put("maxRatio", maxRatio);
            result.put("isCompliant", isCompliant);
            return R.success(result);
        } catch (Exception e) {
            log.error("高管薪酬监控失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    // ==================== 风险预警 ====================

    @Operation(summary = "薪酬预警列表")
    @PostMapping("/warning/list")
    public R<PageResult<Map<String, Object>>> warningList(@RequestBody Map<String, Object> params) {
        try {
            int pageNum = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            String status = (String) params.get("status");
            String level = (String) params.get("level");

            LambdaQueryWrapper<GzctSalaryWarning> wrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(status)) {
                wrapper.eq(GzctSalaryWarning::getStatus, status);
            }
            if (StringUtils.isNotEmpty(level)) {
                wrapper.eq(GzctSalaryWarning::getLevel, level);
            }
            wrapper.orderByDesc(GzctSalaryWarning::getCreateTime);

            Page<GzctSalaryWarning> page = new Page<>(pageNum, pageSize);
            Page<GzctSalaryWarning> result = new GzctSalaryWarning().selectPage(page, wrapper);

            // 转换为前端期望的字段名
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            List<Map<String, Object>> mappedList = result.getRecords().stream().map(r -> {
                Map<String, Object> m = new HashMap<>();
                m.put("warningId", r.getWarningId());
                m.put("warnNo", r.getWarningCode()); // 前端用warnNo
                m.put("warningCode", r.getWarningCode());
                m.put("companyId", r.getCompanyId());
                m.put("companyName", r.getCompanyName());
                m.put("warnType", r.getWarningType()); // 前端用warnType
                m.put("warningType", r.getWarningType());
                m.put("warningContent", r.getWarningContent());
                m.put("level", r.getLevel());
                m.put("status", r.getStatus());
                m.put("ruleCode", r.getRuleCode()); // 新增字段
                m.put("triggerCondition", r.getTriggerCondition()); // 新增字段
                m.put("triggerTime", r.getTriggerTime() != null ? r.getTriggerTime().format(dtf) : (r.getCreateTime() != null ? r.getCreateTime().format(dtf) : null)); // 前端用triggerTime
                m.put("triggerValue", r.getTriggerValue());
                m.put("thresholdValue", r.getThresholdValue());
                m.put("handleResult", r.getHandleResult());
                m.put("handleUser", r.getHandleUser());
                m.put("handleTime", r.getHandleTime() != null ? r.getHandleTime().format(dtf) : null);
                m.put("createTime", r.getCreateTime() != null ? r.getCreateTime().format(dtf) : null);
                return m;
            }).collect(Collectors.toList());

            PageResult<Map<String, Object>> pageResult = new PageResult<>();
            pageResult.setTotalRecord((int) result.getTotal());
            pageResult.setCurrentPage((int) result.getCurrent());
            pageResult.setPageNumber((int) result.getCurrent());
            pageResult.setTotalPage((int) result.getPages());
            pageResult.setPageSize((int) result.getSize());
            pageResult.setTlist(mappedList);
            return R.success(pageResult);
        } catch (Exception e) {
            log.error("查询薪酬预警列表失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "处理薪酬预警")
    @PostMapping("/warning/handle")
    public R<Boolean> handleWarning(@RequestBody Map<String, Object> params) {
        try {
            String warningId = (String) params.get("id");
            if (StringUtils.isEmpty(warningId)) {
                warningId = (String) params.get("warningId");
            }
            GzctSalaryWarning warning = warningMapper.selectById(warningId);
            if (warning != null) {
                String method = (String) params.get("method");
                String status = "PROCESSING";
                if ("INVESTIGATE".equals(method)) status = "INVESTIGATING";
                else if ("TRANSFER".equals(method)) status = "INVESTIGATING";
                warning.setStatus(status);
                // 存储处置说明（含截止日期）
                String remark = (String) params.get("remark");
                String deadline = (String) params.get("deadline");
                String handleResult = remark != null ? remark : "";
                if (StringUtils.isNotEmpty(deadline)) {
                    handleResult += "【截止日期：" + deadline + "】";
                }
                handleResult += "【处置方式：" + (method != null ? method : "RECTIFY") + "】";
                warning.setHandleResult(handleResult);
                warning.setHandleUser((String) params.get("handleUser"));
                warning.setHandleTime(LocalDateTime.now());
                warning.setUpdateTime(LocalDateTime.now());
                warningMapper.updateById(warning);
            }
            return R.success(true);
        } catch (Exception e) {
            log.error("处理薪酬预警失败", e);
            return R.fail("处理失败：" + e.getMessage());
        }
    }

    @Operation(summary = "关闭薪酬预警")
    @PostMapping("/warning/close")
    public R<Boolean> closeWarning(@RequestBody Map<String, Object> params) {
        try {
            String id = (String) params.get("id");
            GzctSalaryWarning warning = warningMapper.selectById(id);
            if (warning != null) {
                warning.setStatus("CLOSED");
                warning.setUpdateTime(LocalDateTime.now());
                warningMapper.updateById(warning);
            }
            return R.success(true);
        } catch (Exception e) {
            log.error("关闭薪酬预警失败", e);
            return R.fail("关闭失败：" + e.getMessage());
        }
    }

    // ==================== 穿透分析 ====================

    @Operation(summary = "薪酬穿透下钻")
    @PostMapping("/drill-down")
    public R<Map<String, Object>> drillDown(@RequestBody Map<String, Object> params) {
        try {
            Map<String, Object> result = new HashMap<>();

            // 按企业分组汇总薪酬数据
            List<TblSalaryTotal> salaryRecords = salaryTotalMapper.selectList(null);
            Map<String, List<TblSalaryTotal>> grouped = salaryRecords.stream()
                    .filter(r -> r.getCompanyName() != null)
                    .collect(Collectors.groupingBy(TblSalaryTotal::getCompanyName));

            List<Map<String, Object>> children = new ArrayList<>();
            for (Map.Entry<String, List<TblSalaryTotal>> entry : grouped.entrySet()) {
                Map<String, Object> node = new HashMap<>();
                node.put("name", entry.getKey());
                node.put("key", "company_" + entry.getKey().hashCode());
                node.put("type", "company");

                BigDecimal totalSalary = BigDecimal.ZERO;
                BigDecimal budgetTotal = BigDecimal.ZERO;
                BigDecimal wageGrowth = BigDecimal.ZERO;
                int count = 0;
                for (TblSalaryTotal r : entry.getValue()) {
                    if (r.getActualTotal() != null) totalSalary = totalSalary.add(r.getActualTotal());
                    if (r.getBudgetTotal() != null) budgetTotal = budgetTotal.add(r.getBudgetTotal());
                    if (r.getWageGrowthRate() != null) { wageGrowth = wageGrowth.add(r.getWageGrowthRate()); count++; }
                }
                BigDecimal execRate = budgetTotal.compareTo(BigDecimal.ZERO) > 0
                        ? totalSalary.multiply(new BigDecimal("100")).divide(budgetTotal, 2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;
                BigDecimal avgWageGrowth = count > 0 ? wageGrowth.divide(new BigDecimal(count), 2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;

                String riskLevel = "LOW";
                if (execRate.compareTo(new BigDecimal("100")) > 0) riskLevel = "HIGH";
                else if (execRate.compareTo(new BigDecimal("90")) > 0) riskLevel = "MEDIUM";

                node.put("totalSalary", totalSalary);
                node.put("budgetExecRate", execRate);
                node.put("linkCoefficient", avgWageGrowth);
                node.put("riskLevel", riskLevel);
                children.add(node);
            }

            result.put("name", "集团总部");
            result.put("key", "root");
            result.put("type", "group");
            result.put("children", children);
            return R.success(result);
        } catch (Exception e) {
            log.error("薪酬穿透下钻失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    // ==================== 驾驶舱 ====================

    @Operation(summary = "薪酬驾驶舱数据")
    @PostMapping("/dashboard")
    public R<Map<String, Object>> dashboard(@RequestBody Map<String, Object> params) {
        try {
            Map<String, Object> result = new HashMap<>();

            // 组织层级过滤
            String companyId = params != null && params.get("companyId") != null ? params.get("companyId").toString() : null;
            String orgPattern = orgQueryHelper.getOrgPathPattern(companyId);

            // KPI指标
            Map<String, Object> kpi = new HashMap<>();
            LambdaQueryWrapper<TblSalaryTotal> salaryBaseWrapper = new LambdaQueryWrapper<>();
            if (orgPattern != null) salaryBaseWrapper.and(w -> w.like(TblSalaryTotal::getOrgPath, orgPattern).or(sub -> sub.isNull(TblSalaryTotal::getOrgPath).eq(TblSalaryTotal::getCompanyId, companyId)));
            long companyCount = salaryTotalMapper.selectCount(salaryBaseWrapper);
            kpi.put("companyCount", companyCount);

            List<TblSalaryTotal> salaryRecords = salaryTotalMapper.selectList(salaryBaseWrapper);
            BigDecimal totalSalary = BigDecimal.ZERO;
            BigDecimal budgetTotal = BigDecimal.ZERO;
            for (TblSalaryTotal r : salaryRecords) {
                if (r.getActualTotal() != null) totalSalary = totalSalary.add(r.getActualTotal());
                if (r.getBudgetTotal() != null) budgetTotal = budgetTotal.add(r.getBudgetTotal());
            }
            kpi.put("totalSalary", totalSalary);
            kpi.put("budgetExecRate", budgetTotal.compareTo(BigDecimal.ZERO) > 0
                    ? totalSalary.multiply(new BigDecimal("100")).divide(budgetTotal, 2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO);

            // 效益联动平均系数
            List<GzctSalaryPerformanceLink> allLinkRecords = performanceLinkMapper.selectList(null);
            BigDecimal avgLinkRatio = BigDecimal.ZERO;
            if (!allLinkRecords.isEmpty()) {
                BigDecimal sum = BigDecimal.ZERO;
                for (GzctSalaryPerformanceLink r : allLinkRecords) {
                    if (r.getLinkCoefficient() != null) sum = sum.add(r.getLinkCoefficient());
                }
                avgLinkRatio = sum.divide(new BigDecimal(allLinkRecords.size()), 2, BigDecimal.ROUND_HALF_UP);
            }
            kpi.put("avgLinkRatio", avgLinkRatio);

            // 活跃预警数
            LambdaQueryWrapper<GzctSalaryWarning> warnWrapper = new LambdaQueryWrapper<>();
            warnWrapper.eq(GzctSalaryWarning::getStatus, "PENDING");
            kpi.put("activeWarnings", warningMapper.selectCount(warnWrapper));

            // 高管合规率
            List<GzctSalaryExecutive> allExecutives = executiveMapper.selectList(null);
            long compliantCount = allExecutives.stream().filter(e -> "1".equals(e.getIsCompliant())).count();
            kpi.put("execComplianceRate", allExecutives.isEmpty() ? BigDecimal.ZERO
                    : new BigDecimal(compliantCount).multiply(new BigDecimal("100")).divide(new BigDecimal(allExecutives.size()), 2, BigDecimal.ROUND_HALF_UP));

            result.put("kpi", kpi);

            // Add at top level for frontend STAT_MAP compatibility
            result.put("totalSalary", totalSalary);
            result.put("salaryTotal", totalSalary);
            long execAbnormalCount = allExecutives.stream().filter(e -> !"1".equals(e.getIsCompliant())).count();
            result.put("executiveAbnormal", execAbnormalCount);
            result.put("anomalyCount", execAbnormalCount);
            result.put("complianceRate", kpi.get("execComplianceRate"));

            // 趋势数据
            Map<String, BigDecimal> yearBudget = new LinkedHashMap<>();
            Map<String, BigDecimal> yearActual = new LinkedHashMap<>();
            for (TblSalaryTotal r : salaryRecords) {
                String year = r.getReportYear();
                if (year == null) continue;
                yearBudget.merge(year, r.getBudgetTotal() != null ? r.getBudgetTotal() : BigDecimal.ZERO, BigDecimal::add);
                yearActual.merge(year, r.getActualTotal() != null ? r.getActualTotal() : BigDecimal.ZERO, BigDecimal::add);
            }
            List<Map<String, Object>> trend = new ArrayList<>();
            for (Map.Entry<String, BigDecimal> entry : yearBudget.entrySet()) {
                Map<String, Object> item = new HashMap<>();
                item.put("period", entry.getKey());
                item.put("budgetTotal", entry.getValue());
                BigDecimal actual = yearActual.getOrDefault(entry.getKey(), BigDecimal.ZERO);
                item.put("actualTotal", actual);
                item.put("execRate", entry.getValue().compareTo(BigDecimal.ZERO) > 0
                        ? actual.multiply(new BigDecimal("100")).divide(entry.getValue(), 2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO);
                trend.add(item);
            }
            result.put("trend", trend);

            // 企业概览 - 补全每个企业的预算执行率/联动系数/人工成本率/高管合规/风险等级
            // 获取联动系数映射（复用上面查询的allLinkRecords）
            Map<String, BigDecimal> companyLinkCoeff = new HashMap<>();
            for (GzctSalaryPerformanceLink lr : allLinkRecords) {
                if (lr.getCompanyName() != null && lr.getLinkCoefficient() != null) {
                    companyLinkCoeff.put(lr.getCompanyName(), lr.getLinkCoefficient());
                }
            }
            // 获取人工成本率映射
            List<GzctSalaryLaborCost> laborCostRecords = laborCostMapper.selectList(null);
            Map<String, BigDecimal> companyLaborCostRate = new HashMap<>();
            for (GzctSalaryLaborCost lcr : laborCostRecords) {
                if (lcr.getCompanyName() != null && lcr.getLaborCostRate() != null) {
                    companyLaborCostRate.put(lcr.getCompanyName(), lcr.getLaborCostRate());
                }
            }
            // 获取高管合规映射（复用上面查询的allExecutives）
            Map<String, Boolean> companyExecCompliance = new HashMap<>();
            for (GzctSalaryExecutive exec : allExecutives) {
                if (exec.getCompanyName() != null && "0".equals(exec.getIsCompliant())) {
                    companyExecCompliance.put(exec.getCompanyName(), false);
                } else if (exec.getCompanyName() != null && !companyExecCompliance.containsKey(exec.getCompanyName())) {
                    companyExecCompliance.put(exec.getCompanyName(), true);
                }
            }

            List<Map<String, Object>> overview = new ArrayList<>();
            Map<String, List<TblSalaryTotal>> grouped = salaryRecords.stream()
                    .filter(r -> r.getCompanyName() != null)
                    .collect(Collectors.groupingBy(TblSalaryTotal::getCompanyName));
            for (Map.Entry<String, List<TblSalaryTotal>> entry : grouped.entrySet()) {
                Map<String, Object> item = new HashMap<>();
                item.put("companyName", entry.getKey());
                BigDecimal compBudget = BigDecimal.ZERO;
                BigDecimal compActual = BigDecimal.ZERO;
                for (TblSalaryTotal r : entry.getValue()) {
                    if (r.getActualTotal() != null) compActual = compActual.add(r.getActualTotal());
                    if (r.getBudgetTotal() != null) compBudget = compBudget.add(r.getBudgetTotal());
                }
                item.put("totalSalary", compActual);
                BigDecimal compExecRate = compBudget.compareTo(BigDecimal.ZERO) > 0
                        ? compActual.multiply(new BigDecimal("100")).divide(compBudget, 2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO;
                item.put("budgetExecRate", compExecRate);
                item.put("linkRatio", companyLinkCoeff.getOrDefault(entry.getKey(), BigDecimal.ZERO));
                item.put("laborCostRate", companyLaborCostRate.getOrDefault(entry.getKey(), BigDecimal.ZERO));
                item.put("execCompliance", companyExecCompliance.getOrDefault(entry.getKey(), true));
                // 风险等级综合判断
                String riskLevel = "LOW";
                if (compExecRate.compareTo(new BigDecimal("100")) > 0 || !companyExecCompliance.getOrDefault(entry.getKey(), true)) {
                    riskLevel = "HIGH";
                } else if (compExecRate.compareTo(new BigDecimal("95")) > 0 || companyLaborCostRate.getOrDefault(entry.getKey(), BigDecimal.ZERO).compareTo(new BigDecimal("15")) > 0) {
                    riskLevel = "MEDIUM";
                }
                item.put("riskLevel", riskLevel);
                overview.add(item);
            }
            result.put("overview", overview);

            return R.success(result);
        } catch (Exception e) {
            log.error("获取薪酬驾驶舱数据失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    // ==================== 人工成本 CRUD ====================

    @Operation(summary = "新增人工成本记录")
    @PostMapping("/labor-cost/add")
    public R<Boolean> addLaborCost(@RequestBody GzctSalaryLaborCost laborCost) {
        try {
            laborCost.setCreateTime(LocalDateTime.now());
            laborCostMapper.insert(laborCost);
            return R.success(true);
        } catch (Exception e) {
            log.error("新增人工成本记录失败", e);
            return R.fail("新增失败：" + e.getMessage());
        }
    }

    @Operation(summary = "更新人工成本记录")
    @PostMapping("/labor-cost/update")
    public R<Boolean> updateLaborCost(@RequestBody GzctSalaryLaborCost laborCost) {
        try {
            laborCost.setUpdateTime(LocalDateTime.now());
            laborCostMapper.updateById(laborCost);
            return R.success(true);
        } catch (Exception e) {
            log.error("更新人工成本记录失败", e);
            return R.fail("更新失败：" + e.getMessage());
        }
    }

    @Operation(summary = "删除人工成本记录")
    @PostMapping("/labor-cost/delete")
    public R<Boolean> deleteLaborCost(@RequestBody Map<String, Object> params) {
        try {
            String id = (String) params.get("id");
            if (StringUtils.isNotEmpty(id)) {
                laborCostMapper.deleteById(id);
            }
            return R.success(true);
        } catch (Exception e) {
            log.error("删除人工成本记录失败", e);
            return R.fail("删除失败：" + e.getMessage());
        }
    }

    // ==================== 激励计划删除 ====================

    @Operation(summary = "删除激励计划")
    @PostMapping("/incentive/delete")
    public R<Boolean> deleteIncentivePlan(@RequestBody Map<String, Object> params) {
        try {
            String id = (String) params.get("id");
            if (StringUtils.isNotEmpty(id)) {
                incentivePlanMapper.deleteById(id);
            }
            return R.success(true);
        } catch (Exception e) {
            log.error("删除激励计划失败", e);
            return R.fail("删除失败：" + e.getMessage());
        }
    }

    // ==================== 工资总额导出 ====================

    @Operation(summary = "工资总额导出数据")
    @PostMapping("/total/export")
    public R<List<TblSalaryTotal>> exportSalaryTotal(@RequestBody Map<String, Object> params) {
        try {
            String companyName = (String) params.get("companyName");
            String year = (String) params.get("year");
            LambdaQueryWrapper<TblSalaryTotal> wrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(companyName)) {
                wrapper.like(TblSalaryTotal::getCompanyName, companyName);
            }
            if (StringUtils.isNotEmpty(year)) {
                wrapper.eq(TblSalaryTotal::getReportYear, year);
            }
            wrapper.orderByDesc(TblSalaryTotal::getReportYear);
            List<TblSalaryTotal> list = salaryTotalMapper.selectList(wrapper);
            return R.success(list);
        } catch (Exception e) {
            log.error("导出工资总额数据失败", e);
            return R.fail("导出失败：" + e.getMessage());
        }
    }

    // ==================== 高管薪酬导出 ====================

    @Operation(summary = "高管薪酬导出数据")
    @PostMapping("/executive/export")
    public R<List<GzctSalaryExecutive>> exportExecutivePay(@RequestBody Map<String, Object> params) {
        try {
            String companyId = (String) params.get("companyId");
            String companyName = (String) params.get("companyName");
            LambdaQueryWrapper<GzctSalaryExecutive> wrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(companyId)) {
                wrapper.eq(GzctSalaryExecutive::getCompanyId, companyId);
            }
            if (StringUtils.isNotEmpty(companyName)) {
                wrapper.like(GzctSalaryExecutive::getCompanyName, companyName);
            }
            wrapper.orderByDesc(GzctSalaryExecutive::getCreateTime);
            List<GzctSalaryExecutive> list = executiveMapper.selectList(wrapper);
            return R.success(list);
        } catch (Exception e) {
            log.error("导出高管薪酬数据失败", e);
            return R.fail("导出失败：" + e.getMessage());
        }
    }
}
