package com.financial.sharing.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.financial.sharing.business.entity.TblExpenseReport;
import com.financial.sharing.business.entity.TblLoanApplication;
import com.financial.sharing.business.entity.TblPrepayment;
import com.financial.sharing.business.entity.TblExpenseProvision;
import com.financial.sharing.business.mapper.ExpenseReportMapper;
import com.financial.sharing.business.mapper.LoanApplicationMapper;
import com.financial.sharing.business.mapper.PrepaymentMapper;
import com.financial.sharing.business.mapper.ExpenseProvisionMapper;
import com.financial.sharing.util.MyJsonBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 费控分析控制器 - 数据来源数据库
 */
@Slf4j
@Api(tags = "费控分析")
@RestController
@RequestMapping("/expense-analysis")
@CrossOrigin
public class ExpenseAnalysisController {

    @Autowired
    private ExpenseReportMapper expenseReportMapper;

    @Autowired
    private LoanApplicationMapper loanApplicationMapper;

    @Autowired
    private PrepaymentMapper prepaymentMapper;

    @Autowired
    private ExpenseProvisionMapper expenseProvisionMapper;

    @ApiOperation("个人费用分析")
    @GetMapping("/personal")
    public MyJsonBean getPersonalExpenseAnalysis(@RequestParam(required = false) String userId,
                                                @RequestParam(required = false) String startDate,
                                                @RequestParam(required = false) String endDate,
                                                @RequestParam(required = false) String analysisType) {
        try {
            Map<String, Object> analysis = new HashMap<>();
            QueryWrapper<TblExpenseReport> wrapper = new QueryWrapper<>();
            if (userId != null && !userId.isEmpty()) wrapper.eq("APPLICANT_ID", userId);
            if (startDate != null && !startDate.isEmpty()) wrapper.ge("CREATE_TIME", startDate);
            if (endDate != null && !endDate.isEmpty()) wrapper.le("CREATE_TIME", endDate);

            List<TblExpenseReport> reports = expenseReportMapper.selectList(wrapper);
            BigDecimal totalExpense = reports.stream()
                    .map(r -> r.getTotalAmount() != null ? r.getTotalAmount() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // 基础统计
            Map<String, Object> basicStats = new HashMap<>();
            basicStats.put("totalExpense", totalExpense);
            basicStats.put("totalReports", reports.size());
            basicStats.put("averageAmount", reports.size() > 0 ? totalExpense.divide(new BigDecimal(reports.size()), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
            basicStats.put("maxAmount", reports.stream().map(r -> r.getTotalAmount() != null ? r.getTotalAmount() : BigDecimal.ZERO).max(BigDecimal::compareTo).orElse(BigDecimal.ZERO));
            basicStats.put("minAmount", reports.stream().map(r -> r.getTotalAmount() != null ? r.getTotalAmount() : BigDecimal.ZERO).min(BigDecimal::compareTo).orElse(BigDecimal.ZERO));
            basicStats.put("pendingAmount", reports.stream().filter(r -> "PENDING".equals(r.getReportStatus())).map(r -> r.getTotalAmount() != null ? r.getTotalAmount() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add));
            basicStats.put("approvedAmount", reports.stream().filter(r -> "APPROVED".equals(r.getReportStatus()) || "PAID".equals(r.getReportStatus())).map(r -> r.getTotalAmount() != null ? r.getTotalAmount() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add));
            analysis.put("basicStats", basicStats);

            // 费用类型分析 - 按报销类型分组
            Map<String, List<TblExpenseReport>> typeGroup = reports.stream()
                    .filter(r -> r.getReportType() != null)
                    .collect(Collectors.groupingBy(TblExpenseReport::getReportType));
            List<Map<String, Object>> expenseTypeAnalysis = new ArrayList<>();
            typeGroup.forEach((type, list) -> {
                BigDecimal typeAmount = list.stream().map(r -> r.getTotalAmount() != null ? r.getTotalAmount() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add);
                double percentage = totalExpense.compareTo(BigDecimal.ZERO) > 0 ? typeAmount.doubleValue() / totalExpense.doubleValue() * 100 : 0;
                Map<String, Object> item = new HashMap<>();
                item.put("expenseType", type);
                item.put("amount", typeAmount);
                item.put("percentage", percentage);
                item.put("count", list.size());
                expenseTypeAnalysis.add(item);
            });
            analysis.put("expenseTypeAnalysis", expenseTypeAnalysis);

            // 月度趋势分析 - 按月分组
            List<Map<String, Object>> monthlyTrend = buildMonthlyTrend(reports);
            analysis.put("monthlyTrend", monthlyTrend);

            // 部门对比 - 当前用户部门 vs 其他部门
            Map<String, Object> departmentComparison = new HashMap<>();
            departmentComparison.put("myExpense", totalExpense);
            BigDecimal deptAvg = reports.stream()
                    .filter(r -> r.getApplicantDeptName() != null)
                    .map(r -> r.getTotalAmount() != null ? r.getTotalAmount() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(new BigDecimal(Math.max(1, reports.stream().filter(r -> r.getApplicantDeptName() != null).map(TblExpenseReport::getApplicantDeptId).distinct().count())), 2, RoundingMode.HALF_UP);
            departmentComparison.put("departmentAverage", deptAvg);
            departmentComparison.put("companyAverage", totalExpense.divide(new BigDecimal(Math.max(1, reports.stream().map(TblExpenseReport::getApplicantId).distinct().count())), 2, RoundingMode.HALF_UP));
            analysis.put("departmentComparison", departmentComparison);

            return MyJsonBean.successData("查询成功", analysis);
        } catch (Exception e) {
            log.error("个人费用分析失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("组织费用分析")
    @GetMapping("/organization")
    public MyJsonBean getOrganizationExpenseAnalysis(@RequestParam(required = false) String orgId,
                                                    @RequestParam(required = false) String startDate,
                                                    @RequestParam(required = false) String endDate,
                                                    @RequestParam(required = false) String analysisLevel) {
        try {
            Map<String, Object> analysis = new HashMap<>();
            QueryWrapper<TblExpenseReport> wrapper = new QueryWrapper<>();
            if (startDate != null && !startDate.isEmpty()) wrapper.ge("CREATE_TIME", startDate);
            if (endDate != null && !endDate.isEmpty()) wrapper.le("CREATE_TIME", endDate);

            List<TblExpenseReport> reports = expenseReportMapper.selectList(wrapper);
            BigDecimal totalExpense = reports.stream()
                    .map(r -> r.getTotalAmount() != null ? r.getTotalAmount() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // 组织基础统计
            Map<String, Object> orgStats = new HashMap<>();
            orgStats.put("totalExpense", totalExpense);
            orgStats.put("totalReports", reports.size());
            orgStats.put("totalEmployees", reports.stream().map(TblExpenseReport::getApplicantId).filter(Objects::nonNull).distinct().count());
            orgStats.put("averagePerEmployee", reports.size() > 0 ? totalExpense.divide(new BigDecimal(Math.max(1, reports.stream().map(TblExpenseReport::getApplicantId).distinct().count())), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
            analysis.put("orgStats", orgStats);

            // 部门费用排行 - 按部门分组聚合
            Map<String, List<TblExpenseReport>> deptGroup = reports.stream()
                    .filter(r -> r.getApplicantDeptName() != null)
                    .collect(Collectors.groupingBy(TblExpenseReport::getApplicantDeptName));
            List<Map<String, Object>> departmentRanking = new ArrayList<>();
            deptGroup.forEach((dept, list) -> {
                BigDecimal deptExpense = list.stream().map(r -> r.getTotalAmount() != null ? r.getTotalAmount() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add);
                double percentage = totalExpense.compareTo(BigDecimal.ZERO) > 0 ? deptExpense.doubleValue() / totalExpense.doubleValue() * 100 : 0;
                Map<String, Object> item = new HashMap<>();
                item.put("deptName", dept);
                item.put("amount", deptExpense);
                item.put("percentage", percentage);
                item.put("employees", list.stream().map(TblExpenseReport::getApplicantId).distinct().count());
                departmentRanking.add(item);
            });
            departmentRanking.sort((a, b) -> ((BigDecimal) b.get("amount")).compareTo((BigDecimal) a.get("amount")));
            analysis.put("departmentRanking", departmentRanking);

            // 费用类型分布 - 按报销类型分组
            Map<String, List<TblExpenseReport>> typeGroup = reports.stream()
                    .filter(r -> r.getReportType() != null)
                    .collect(Collectors.groupingBy(TblExpenseReport::getReportType));
            List<Map<String, Object>> expenseDistribution = new ArrayList<>();
            typeGroup.forEach((type, list) -> {
                BigDecimal typeAmount = list.stream().map(r -> r.getTotalAmount() != null ? r.getTotalAmount() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add);
                double percentage = totalExpense.compareTo(BigDecimal.ZERO) > 0 ? typeAmount.doubleValue() / totalExpense.doubleValue() * 100 : 0;
                Map<String, Object> item = new HashMap<>();
                item.put("expenseType", type);
                item.put("amount", typeAmount);
                item.put("percentage", percentage);
                expenseDistribution.add(item);
            });
            analysis.put("expenseDistribution", expenseDistribution);

            // 异常分析 - 金额超过平均3倍的单据
            BigDecimal avgAmount = reports.size() > 0 ? totalExpense.divide(new BigDecimal(reports.size()), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
            List<Map<String, Object>> anomalies = new ArrayList<>();
            reports.stream()
                    .filter(r -> r.getTotalAmount() != null && r.getTotalAmount().compareTo(avgAmount.multiply(new BigDecimal(3))) > 0)
                    .forEach(r -> {
                        Map<String, Object> anomaly = new HashMap<>();
                        anomaly.put("type", "HIGH_AMOUNT");
                        anomaly.put("description", r.getApplicantName() + "报销金额异常：" + r.getTotalAmount() + "元");
                        anomaly.put("amount", r.getTotalAmount());
                        anomaly.put("severity", "HIGH");
                        anomalies.add(anomaly);
                    });
            analysis.put("anomalies", anomalies);

            return MyJsonBean.successData("查询成功", analysis);
        } catch (Exception e) {
            log.error("组织费用分析失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("差旅费用分析")
    @GetMapping("/travel")
    public MyJsonBean getTravelExpenseAnalysis(@RequestParam(required = false) String userId,
                                              @RequestParam(required = false) String startDate,
                                              @RequestParam(required = false) String endDate,
                                              @RequestParam(required = false) String analysisType) {
        try {
            Map<String, Object> analysis = new HashMap<>();
            QueryWrapper<TblExpenseReport> wrapper = new QueryWrapper<>();
            wrapper.eq("REPORT_TYPE", "TRAVEL");
            if (userId != null && !userId.isEmpty()) wrapper.eq("APPLICANT_ID", userId);
            if (startDate != null && !startDate.isEmpty()) wrapper.ge("CREATE_TIME", startDate);
            if (endDate != null && !endDate.isEmpty()) wrapper.le("CREATE_TIME", endDate);

            List<TblExpenseReport> travelReports = expenseReportMapper.selectList(wrapper);
            BigDecimal totalTravelExpense = travelReports.stream()
                    .map(r -> r.getTotalAmount() != null ? r.getTotalAmount() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // 差旅基础统计
            Map<String, Object> travelStats = new HashMap<>();
            travelStats.put("totalTravelExpense", totalTravelExpense);
            travelStats.put("totalTrips", travelReports.size());
            travelStats.put("averagePerTrip", travelReports.size() > 0 ? totalTravelExpense.divide(new BigDecimal(travelReports.size()), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
            analysis.put("travelStats", travelStats);

            // 目的地分析 - 按项目分组
            Map<String, List<TblExpenseReport>> destGroup = travelReports.stream()
                    .filter(r -> r.getProjectName() != null)
                    .collect(Collectors.groupingBy(TblExpenseReport::getProjectName));
            List<Map<String, Object>> destinationAnalysis = new ArrayList<>();
            destGroup.forEach((dest, list) -> {
                BigDecimal destAmount = list.stream().map(r -> r.getTotalAmount() != null ? r.getTotalAmount() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add);
                Map<String, Object> item = new HashMap<>();
                item.put("destination", dest);
                item.put("trips", list.size());
                item.put("amount", destAmount);
                destinationAnalysis.add(item);
            });
            analysis.put("destinationAnalysis", destinationAnalysis);

            return MyJsonBean.successData("查询成功", analysis);
        } catch (Exception e) {
            log.error("差旅费用分析失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("项目费用分析")
    @GetMapping("/project")
    public MyJsonBean getProjectExpenseAnalysis(@RequestParam(required = false) String projectId,
                                               @RequestParam(required = false) String startDate,
                                               @RequestParam(required = false) String endDate,
                                               @RequestParam(required = false) String analysisType) {
        try {
            Map<String, Object> analysis = new HashMap<>();
            QueryWrapper<TblExpenseReport> wrapper = new QueryWrapper<>();
            if (projectId != null && !projectId.isEmpty()) wrapper.eq("PROJECT_ID", projectId);
            if (startDate != null && !startDate.isEmpty()) wrapper.ge("CREATE_TIME", startDate);
            if (endDate != null && !endDate.isEmpty()) wrapper.le("CREATE_TIME", endDate);

            List<TblExpenseReport> projectReports = expenseReportMapper.selectList(wrapper);
            BigDecimal totalProjectExpense = projectReports.stream()
                    .map(r -> r.getTotalAmount() != null ? r.getTotalAmount() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // 项目基础统计
            Map<String, Object> projectStats = new HashMap<>();
            projectStats.put("totalProjects", projectReports.stream().map(TblExpenseReport::getProjectId).filter(Objects::nonNull).distinct().count());
            projectStats.put("totalExpense", totalProjectExpense);
            projectStats.put("averagePerProject", projectReports.size() > 0 ? totalProjectExpense.divide(new BigDecimal(Math.max(1, projectReports.stream().map(TblExpenseReport::getProjectId).distinct().count())), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
            projectStats.put("activeProjects", projectReports.stream().filter(r -> "PENDING".equals(r.getReportStatus()) || "APPROVED".equals(r.getReportStatus())).map(TblExpenseReport::getProjectId).distinct().count());
            analysis.put("projectStats", projectStats);

            // 项目费用排行 - 按项目分组
            Map<String, List<TblExpenseReport>> projGroup = projectReports.stream()
                    .filter(r -> r.getProjectName() != null)
                    .collect(Collectors.groupingBy(TblExpenseReport::getProjectName));
            List<Map<String, Object>> projectRanking = new ArrayList<>();
            projGroup.forEach((proj, list) -> {
                BigDecimal projAmount = list.stream().map(r -> r.getTotalAmount() != null ? r.getTotalAmount() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add);
                Map<String, Object> item = new HashMap<>();
                item.put("projectName", proj);
                item.put("amount", projAmount);
                item.put("count", list.size());
                projectRanking.add(item);
            });
            projectRanking.sort((a, b) -> ((BigDecimal) b.get("amount")).compareTo((BigDecimal) a.get("amount")));
            analysis.put("projectRanking", projectRanking);

            return MyJsonBean.successData("查询成功", analysis);
        } catch (Exception e) {
            log.error("项目费用分析失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("费用趋势分析")
    @GetMapping("/trend")
    public MyJsonBean getExpenseTrendAnalysis(@RequestParam(required = false) String analysisType,
                                             @RequestParam(required = false) String timeRange,
                                             @RequestParam(required = false) String groupBy) {
        try {
            Map<String, Object> analysis = new HashMap<>();
            List<TblExpenseReport> allReports = expenseReportMapper.selectList(null);

            // 总体趋势 - 按月聚合
            List<Map<String, Object>> overallTrend = buildMonthlyTrend(allReports);
            analysis.put("overallTrend", overallTrend);

            // 同比分析 - 对比去年同期
            LocalDateTime now = LocalDateTime.now();
            String currentYearStart = now.withDayOfMonth(1).withMonth(1).withHour(0).withMinute(0).withSecond(0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String lastYearStart = now.minusYears(1).withDayOfMonth(1).withMonth(1).withHour(0).withMinute(0).withSecond(0).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String lastYearEnd = now.minusYears(1).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            BigDecimal currentYearTotal = expenseReportMapper.selectList(
                    new QueryWrapper<TblExpenseReport>().ge("CREATE_TIME", currentYearStart))
                    .stream().map(r -> r.getTotalAmount() != null ? r.getTotalAmount() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal lastYearTotal = expenseReportMapper.selectList(
                    new QueryWrapper<TblExpenseReport>().ge("CREATE_TIME", lastYearStart).le("CREATE_TIME", lastYearEnd))
                    .stream().map(r -> r.getTotalAmount() != null ? r.getTotalAmount() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add);

            Map<String, Object> yearOverYear = new HashMap<>();
            yearOverYear.put("currentYearTotal", currentYearTotal);
            yearOverYear.put("lastYearTotal", lastYearTotal);
            yearOverYear.put("growthAmount", currentYearTotal.subtract(lastYearTotal));
            yearOverYear.put("growthRate", lastYearTotal.compareTo(BigDecimal.ZERO) > 0 ?
                    currentYearTotal.subtract(lastYearTotal).divide(lastYearTotal, 4, RoundingMode.HALF_UP).multiply(new BigDecimal(100)).doubleValue() : 0);
            analysis.put("yearOverYear", yearOverYear);

            return MyJsonBean.successData("查询成功", analysis);
        } catch (Exception e) {
            log.error("费用趋势分析失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("费用对比分析")
    @GetMapping("/comparison")
    public MyJsonBean getExpenseComparisonAnalysis(@RequestParam(required = false) String comparisonType,
                                                  @RequestParam(required = false) String targetIds,
                                                  @RequestParam(required = false) String startDate,
                                                  @RequestParam(required = false) String endDate) {
        try {
            Map<String, Object> analysis = new HashMap<>();
            QueryWrapper<TblExpenseReport> wrapper = new QueryWrapper<>();
            if (startDate != null && !startDate.isEmpty()) wrapper.ge("CREATE_TIME", startDate);
            if (endDate != null && !endDate.isEmpty()) wrapper.le("CREATE_TIME", endDate);

            List<TblExpenseReport> reports = expenseReportMapper.selectList(wrapper);

            // 部门对比 - 按部门分组
            Map<String, List<TblExpenseReport>> deptGroup = reports.stream()
                    .filter(r -> r.getApplicantDeptName() != null)
                    .collect(Collectors.groupingBy(TblExpenseReport::getApplicantDeptName));
            List<Map<String, Object>> departmentComparison = new ArrayList<>();
            deptGroup.forEach((dept, list) -> {
                BigDecimal deptAmount = list.stream().map(r -> r.getTotalAmount() != null ? r.getTotalAmount() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add);
                Map<String, Object> item = new HashMap<>();
                item.put("deptName", dept);
                item.put("amount", deptAmount);
                item.put("count", list.size());
                departmentComparison.add(item);
            });
            analysis.put("departmentComparison", departmentComparison);

            // 人员对比 - 按申请人分组
            Map<String, List<TblExpenseReport>> userGroup = reports.stream()
                    .filter(r -> r.getApplicantName() != null)
                    .collect(Collectors.groupingBy(TblExpenseReport::getApplicantName));
            List<Map<String, Object>> personnelComparison = new ArrayList<>();
            userGroup.forEach((user, list) -> {
                BigDecimal userAmount = list.stream().map(r -> r.getTotalAmount() != null ? r.getTotalAmount() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add);
                Map<String, Object> item = new HashMap<>();
                item.put("userName", user);
                item.put("amount", userAmount);
                item.put("count", list.size());
                personnelComparison.add(item);
            });
            personnelComparison.sort((a, b) -> ((BigDecimal) b.get("amount")).compareTo((BigDecimal) a.get("amount")));
            // 添加排名
            for (int i = 0; i < personnelComparison.size(); i++) {
                personnelComparison.get(i).put("rank", i + 1);
            }
            analysis.put("personnelComparison", personnelComparison);

            return MyJsonBean.successData("查询成功", analysis);
        } catch (Exception e) {
            log.error("费用对比分析失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("导出分析报告")
    @GetMapping("/export")
    public MyJsonBean exportAnalysisReport(@RequestParam String analysisType,
                                          @RequestParam(required = false) String startDate,
                                          @RequestParam(required = false) String endDate,
                                          @RequestParam(required = false) String format) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("exportId", "ANALYSIS_EXPORT_" + System.currentTimeMillis());
            result.put("fileName", "expense_analysis_" + analysisType + "_" + System.currentTimeMillis() + "." + (format != null ? format : "xlsx"));
            result.put("analysisType", analysisType);
            result.put("exportTime", LocalDateTime.now());
            return MyJsonBean.successData("导出成功", result);
        } catch (Exception e) {
            return MyJsonBean.errorData("导出失败: " + e.getMessage());
        }
    }

    private List<Map<String, Object>> buildMonthlyTrend(List<TblExpenseReport> reports) {
        Map<String, List<TblExpenseReport>> monthGroup = reports.stream()
                .filter(r -> r.getCreateTime() != null)
                .collect(Collectors.groupingBy(r -> r.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM"))));

        List<Map<String, Object>> trend = new ArrayList<>();
        monthGroup.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    BigDecimal monthAmount = entry.getValue().stream()
                            .map(r -> r.getTotalAmount() != null ? r.getTotalAmount() : BigDecimal.ZERO)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    Map<String, Object> item = new HashMap<>();
                    item.put("month", entry.getKey());
                    item.put("amount", monthAmount);
                    item.put("count", entry.getValue().size());
                    trend.add(item);
                });
        return trend;
    }
}
