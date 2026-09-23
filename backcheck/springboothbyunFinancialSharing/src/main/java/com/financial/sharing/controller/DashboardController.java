package com.financial.sharing.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.financial.sharing.business.entity.TblExpenseReport;
import com.financial.sharing.business.entity.TblLoanApplication;
import com.financial.sharing.business.entity.TblPrepayment;
import com.financial.sharing.business.mapper.ExpenseReportMapper;
import com.financial.sharing.business.mapper.LoanApplicationMapper;
import com.financial.sharing.business.mapper.PrepaymentMapper;
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
 * 费控看板控制器 - 数据来源数据库
 */
@Slf4j
@Api(tags = "费控看板")
@RestController
@RequestMapping("/dashboard")
@CrossOrigin
public class DashboardController {

    @Autowired
    private ExpenseReportMapper expenseReportMapper;

    @Autowired
    private LoanApplicationMapper loanApplicationMapper;

    @Autowired
    private PrepaymentMapper prepaymentMapper;

    @ApiOperation("获取总览数据")
    @GetMapping("/overview")
    public MyJsonBean getOverviewData(@RequestParam(required = false) String timeRange,
                                     @RequestParam(required = false) String departmentId) {
        try {
            Map<String, Object> overview = new HashMap<>();
            QueryWrapper<TblExpenseReport> wrapper = new QueryWrapper<>();
            if (departmentId != null && !departmentId.isEmpty()) {
                wrapper.eq("APPLICANT_DEPT_ID", departmentId);
            }

            List<TblExpenseReport> reports = expenseReportMapper.selectList(wrapper);
            BigDecimal totalExpense = reports.stream()
                    .map(r -> r.getTotalAmount() != null ? r.getTotalAmount() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // 核心指标
            long pendingApprovals = reports.stream().filter(r -> "PENDING".equals(r.getReportStatus())).count();

            // 本月费用
            LocalDateTime monthStart = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
            String monthStartStr = monthStart.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            BigDecimal monthlyExpense = expenseReportMapper.selectList(
                    new QueryWrapper<TblExpenseReport>().ge("CREATE_TIME", monthStartStr))
                    .stream().map(r -> r.getTotalAmount() != null ? r.getTotalAmount() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            Map<String, Object> coreMetrics = new HashMap<>();
            coreMetrics.put("totalExpense", totalExpense);
            coreMetrics.put("pendingApprovals", pendingApprovals);
            coreMetrics.put("monthlyExpense", monthlyExpense);
            coreMetrics.put("totalReports", reports.size());
            overview.put("coreMetrics", coreMetrics);

            // 趋势数据 - 按月聚合最近6个月
            List<Map<String, Object>> expenseTrend = new ArrayList<>();
            for (int i = 5; i >= 0; i--) {
                LocalDateTime mStart = LocalDateTime.now().minusMonths(i).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
                LocalDateTime mEnd = i == 0 ? LocalDateTime.now() : mStart.plusMonths(1);
                String msStr = mStart.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                String meStr = mEnd.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                BigDecimal mExpense = expenseReportMapper.selectList(
                        new QueryWrapper<TblExpenseReport>().ge("CREATE_TIME", msStr).lt("CREATE_TIME", meStr))
                        .stream().map(r -> r.getTotalAmount() != null ? r.getTotalAmount() : BigDecimal.ZERO)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                Map<String, Object> monthItem = new HashMap<>();
                monthItem.put("month", mStart.format(DateTimeFormatter.ofPattern("yyyy-MM")));
                monthItem.put("expense", mExpense);
                monthItem.put("reportCount", expenseReportMapper.selectCount(
                        new QueryWrapper<TblExpenseReport>().ge("CREATE_TIME", msStr).lt("CREATE_TIME", meStr)));
                expenseTrend.add(monthItem);
            }
            overview.put("expenseTrend", expenseTrend);

            // 部门费用排行 - 按部门分组聚合
            Map<String, List<TblExpenseReport>> deptGroup = reports.stream()
                    .filter(r -> r.getApplicantDeptName() != null)
                    .collect(Collectors.groupingBy(TblExpenseReport::getApplicantDeptName));
            List<Map<String, Object>> departmentRanking = new ArrayList<>();
            int rank = 1;
            for (Map.Entry<String, List<TblExpenseReport>> entry : deptGroup.entrySet()) {
                BigDecimal deptExpense = entry.getValue().stream()
                        .map(r -> r.getTotalAmount() != null ? r.getTotalAmount() : BigDecimal.ZERO)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                Map<String, Object> dept = new HashMap<>();
                dept.put("departmentName", entry.getKey());
                dept.put("expense", deptExpense);
                dept.put("reportCount", entry.getValue().size());
                dept.put("rank", rank++);
                departmentRanking.add(dept);
            }
            departmentRanking.sort((a, b) -> ((BigDecimal) b.get("expense")).compareTo((BigDecimal) a.get("expense")));
            overview.put("departmentRanking", departmentRanking);

            // 费用类型分布 - 按报销类型分组
            Map<String, List<TblExpenseReport>> typeGroup = reports.stream()
                    .filter(r -> r.getReportType() != null)
                    .collect(Collectors.groupingBy(TblExpenseReport::getReportType));
            List<Map<String, Object>> expenseTypeDistribution = new ArrayList<>();
            typeGroup.forEach((type, list) -> {
                BigDecimal typeAmount = list.stream()
                        .map(r -> r.getTotalAmount() != null ? r.getTotalAmount() : BigDecimal.ZERO)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                double percentage = totalExpense.compareTo(BigDecimal.ZERO) > 0 ? typeAmount.doubleValue() / totalExpense.doubleValue() * 100 : 0;
                Map<String, Object> item = new HashMap<>();
                item.put("expenseType", type);
                item.put("amount", typeAmount);
                item.put("percentage", percentage);
                expenseTypeDistribution.add(item);
            });
            overview.put("expenseTypeDistribution", expenseTypeDistribution);

            return MyJsonBean.successData("查询成功", overview);
        } catch (Exception e) {
            log.error("查询看板总览失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取实时数据")
    @GetMapping("/realtime")
    public MyJsonBean getRealtimeData() {
        try {
            Map<String, Object> realtime = new HashMap<>();

            // 今日费用统计
            LocalDateTime todayStart = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0);
            String todayStartStr = todayStart.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            List<TblExpenseReport> todayReports = expenseReportMapper.selectList(
                    new QueryWrapper<TblExpenseReport>().ge("CREATE_TIME", todayStartStr));
            BigDecimal todayExpense = todayReports.stream()
                    .map(r -> r.getTotalAmount() != null ? r.getTotalAmount() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            long pendingReports = expenseReportMapper.selectCount(
                    new QueryWrapper<TblExpenseReport>().eq("REPORT_STATUS", "PENDING"));

            Map<String, Object> realtimeMetrics = new HashMap<>();
            realtimeMetrics.put("todayExpense", todayExpense);
            realtimeMetrics.put("todayApprovals", todayReports.stream().filter(r -> "APPROVED".equals(r.getReportStatus())).count());
            realtimeMetrics.put("pendingReports", pendingReports);
            realtimeMetrics.put("lastUpdateTime", LocalDateTime.now());
            realtime.put("realtimeMetrics", realtimeMetrics);

            // 最新动态 - 从数据库查询最近5条
            List<Map<String, Object>> recentActivities = new ArrayList<>();
            List<TblExpenseReport> recent5 = expenseReportMapper.selectList(
                    new QueryWrapper<TblExpenseReport>().orderByDesc("UPDATE_TIME").last("LIMIT 5"));
            for (TblExpenseReport report : recent5) {
                Map<String, Object> activity = new HashMap<>();
                activity.put("type", "EXPENSE");
                activity.put("message", report.getApplicantName() + "提交了费用报销单");
                activity.put("time", report.getUpdateTime() != null ? report.getUpdateTime() : report.getCreateTime());
                activity.put("status", report.getReportStatus());
                recentActivities.add(activity);
            }
            realtime.put("recentActivities", recentActivities);

            // 预警信息 - 从数据库计算超预算
            List<Map<String, Object>> alerts = new ArrayList<>();
            long overdue = expenseReportMapper.selectCount(
                    new QueryWrapper<TblExpenseReport>().eq("REPORT_STATUS", "PENDING").le("CREATE_TIME", LocalDateTime.now().minusDays(3)));
            if (overdue > 0) {
                Map<String, Object> alert = new HashMap<>();
                alert.put("level", "HIGH");
                alert.put("message", "有" + overdue + "张报销单超过审批时限");
                alert.put("time", LocalDateTime.now());
                alerts.add(alert);
            }
            realtime.put("alerts", alerts);

            return MyJsonBean.successData("查询成功", realtime);
        } catch (Exception e) {
            log.error("查询实时数据失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取审批统计")
    @GetMapping("/approval-stats")
    public MyJsonBean getApprovalStats(@RequestParam(required = false) String timeRange,
                                      @RequestParam(required = false) String departmentId) {
        try {
            Map<String, Object> approvalStats = new HashMap<>();

            long totalReports = expenseReportMapper.selectCount(null);
            long pendingReports = expenseReportMapper.selectCount(
                    new QueryWrapper<TblExpenseReport>().eq("REPORT_STATUS", "PENDING"));
            long approvedReports = expenseReportMapper.selectCount(
                    new QueryWrapper<TblExpenseReport>().eq("REPORT_STATUS", "APPROVED"));
            long rejectedReports = expenseReportMapper.selectCount(
                    new QueryWrapper<TblExpenseReport>().eq("REPORT_STATUS", "REJECTED"));

            Map<String, Object> approvalOverview = new HashMap<>();
            approvalOverview.put("totalReports", totalReports);
            approvalOverview.put("pendingReports", pendingReports);
            approvalOverview.put("approvedReports", approvedReports);
            approvalOverview.put("rejectedReports", rejectedReports);
            approvalOverview.put("approvalRate", totalReports > 0 ? (double) approvedReports / (approvedReports + rejectedReports) * 100 : 0);
            approvalStats.put("approvalOverview", approvalOverview);

            // 审批趋势 - 按日聚合最近7天
            List<Map<String, Object>> approvalTrend = new ArrayList<>();
            for (int i = 6; i >= 0; i--) {
                LocalDateTime dayStart = LocalDateTime.now().minusDays(i).withHour(0).withMinute(0).withSecond(0);
                LocalDateTime dayEnd = dayStart.plusDays(1);
                String dsStr = dayStart.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                String deStr = dayEnd.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                long submitted = expenseReportMapper.selectCount(
                        new QueryWrapper<TblExpenseReport>().ge("CREATE_TIME", dsStr).lt("CREATE_TIME", deStr));
                long approved = expenseReportMapper.selectCount(
                        new QueryWrapper<TblExpenseReport>().ge("UPDATE_TIME", dsStr).lt("UPDATE_TIME", deStr).eq("REPORT_STATUS", "APPROVED"));
                long rejected = expenseReportMapper.selectCount(
                        new QueryWrapper<TblExpenseReport>().ge("UPDATE_TIME", dsStr).lt("UPDATE_TIME", deStr).eq("REPORT_STATUS", "REJECTED"));
                long pending = expenseReportMapper.selectCount(
                        new QueryWrapper<TblExpenseReport>().ge("CREATE_TIME", dsStr).lt("CREATE_TIME", deStr).eq("REPORT_STATUS", "PENDING"));

                Map<String, Object> dayItem = new HashMap<>();
                dayItem.put("date", dayStart.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                dayItem.put("submitted", submitted);
                dayItem.put("approved", approved);
                dayItem.put("rejected", rejected);
                dayItem.put("pending", pending);
                approvalTrend.add(dayItem);
            }
            approvalStats.put("approvalTrend", approvalTrend);

            return MyJsonBean.successData("查询成功", approvalStats);
        } catch (Exception e) {
            log.error("查询审批统计失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取预算分析")
    @GetMapping("/budget-analysis")
    public MyJsonBean getBudgetAnalysis(@RequestParam(required = false) String timeRange,
                                       @RequestParam(required = false) String departmentId) {
        try {
            Map<String, Object> budgetAnalysis = new HashMap<>();

            // 预算执行概况 - 从数据库聚合
            BigDecimal totalExpense = expenseReportMapper.selectList(null).stream()
                    .map(r -> r.getTotalAmount() != null ? r.getTotalAmount() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            Map<String, Object> budgetExecution = new HashMap<>();
            budgetExecution.put("usedAmount", totalExpense);
            budgetExecution.put("totalReports", expenseReportMapper.selectCount(null));
            budgetAnalysis.put("budgetExecution", budgetExecution);

            // 部门预算执行 - 按部门分组
            List<TblExpenseReport> allReports = expenseReportMapper.selectList(null);
            Map<String, List<TblExpenseReport>> deptGroup = allReports.stream()
                    .filter(r -> r.getApplicantDeptName() != null)
                    .collect(Collectors.groupingBy(TblExpenseReport::getApplicantDeptName));
            List<Map<String, Object>> departmentBudgetExecution = new ArrayList<>();
            deptGroup.forEach((dept, list) -> {
                BigDecimal deptUsed = list.stream()
                        .map(r -> r.getTotalAmount() != null ? r.getTotalAmount() : BigDecimal.ZERO)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                Map<String, Object> item = new HashMap<>();
                item.put("departmentName", dept);
                item.put("used", deptUsed);
                item.put("reportCount", list.size());
                departmentBudgetExecution.add(item);
            });
            budgetAnalysis.put("departmentBudgetExecution", departmentBudgetExecution);

            return MyJsonBean.successData("查询成功", budgetAnalysis);
        } catch (Exception e) {
            log.error("查询预算分析失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取费用分析")
    @GetMapping("/expense-analysis")
    public MyJsonBean getExpenseAnalysis(@RequestParam(required = false) String timeRange,
                                        @RequestParam(required = false) String analysisType) {
        try {
            Map<String, Object> expenseAnalysis = new HashMap<>();
            List<TblExpenseReport> allReports = expenseReportMapper.selectList(null);
            BigDecimal totalExpense = allReports.stream()
                    .map(r -> r.getTotalAmount() != null ? r.getTotalAmount() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // 费用结构分析 - 按类型分组
            Map<String, List<TblExpenseReport>> typeGroup = allReports.stream()
                    .filter(r -> r.getReportType() != null)
                    .collect(Collectors.groupingBy(TblExpenseReport::getReportType));
            List<Map<String, Object>> expenseStructure = new ArrayList<>();
            typeGroup.forEach((type, list) -> {
                BigDecimal typeAmount = list.stream()
                        .map(r -> r.getTotalAmount() != null ? r.getTotalAmount() : BigDecimal.ZERO)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                double percentage = totalExpense.compareTo(BigDecimal.ZERO) > 0 ? typeAmount.doubleValue() / totalExpense.doubleValue() * 100 : 0;
                Map<String, Object> item = new HashMap<>();
                item.put("category", type);
                item.put("amount", typeAmount);
                item.put("percentage", percentage);
                item.put("count", list.size());
                expenseStructure.add(item);
            });
            expenseAnalysis.put("expenseStructure", expenseStructure);

            // 异常费用分析 - 金额超过平均值3倍
            BigDecimal avg = allReports.size() > 0 ? totalExpense.divide(new BigDecimal(allReports.size()), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
            List<Map<String, Object>> abnormalExpenses = new ArrayList<>();
            allReports.stream()
                    .filter(r -> r.getTotalAmount() != null && r.getTotalAmount().compareTo(avg.multiply(new BigDecimal(3))) > 0)
                    .forEach(r -> {
                        Map<String, Object> item = new HashMap<>();
                        item.put("reportId", r.getReportId());
                        item.put("employeeName", r.getApplicantName());
                        item.put("amount", r.getTotalAmount());
                        item.put("category", r.getReportType());
                        item.put("reason", "单笔金额过高");
                        item.put("riskLevel", "HIGH");
                        abnormalExpenses.add(item);
                    });
            expenseAnalysis.put("abnormalExpenses", abnormalExpenses);

            return MyJsonBean.successData("查询成功", expenseAnalysis);
        } catch (Exception e) {
            log.error("查询费用分析失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取风险监控")
    @GetMapping("/risk-monitoring")
    public MyJsonBean getRiskMonitoring() {
        try {
            Map<String, Object> riskMonitoring = new HashMap<>();
            List<TblExpenseReport> allReports = expenseReportMapper.selectList(null);

            // 风险概况 - 计算超时和异常
            long overdueCount = expenseReportMapper.selectCount(
                    new QueryWrapper<TblExpenseReport>().eq("REPORT_STATUS", "PENDING").le("CREATE_TIME", LocalDateTime.now().minusDays(3)));
            BigDecimal avg = allReports.size() > 0 ?
                    allReports.stream().map(r -> r.getTotalAmount() != null ? r.getTotalAmount() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add)
                            .divide(new BigDecimal(allReports.size()), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
            long highAmountCount = allReports.stream()
                    .filter(r -> r.getTotalAmount() != null && r.getTotalAmount().compareTo(avg.multiply(new BigDecimal(3))) > 0).count();

            Map<String, Object> riskOverview = new HashMap<>();
            riskOverview.put("totalRisks", overdueCount + highAmountCount);
            riskOverview.put("highRisks", overdueCount);
            riskOverview.put("mediumRisks", highAmountCount);
            riskOverview.put("riskTrend", overdueCount + highAmountCount > 0 ? "INCREASING" : "STABLE");
            riskMonitoring.put("riskOverview", riskOverview);

            // 风险事件列表
            List<Map<String, Object>> riskEvents = new ArrayList<>();
            List<TblExpenseReport> overdueReports = expenseReportMapper.selectList(
                    new QueryWrapper<TblExpenseReport>().eq("REPORT_STATUS", "PENDING").le("CREATE_TIME", LocalDateTime.now().minusDays(3)).last("LIMIT 10"));
            for (TblExpenseReport report : overdueReports) {
                Map<String, Object> event = new HashMap<>();
                event.put("eventId", "OVERDUE_" + report.getReportId());
                event.put("eventType", "OVERDUE_APPROVAL");
                event.put("description", report.getApplicantName() + "的报销单超时未审批");
                event.put("riskLevel", "HIGH");
                event.put("amount", report.getTotalAmount());
                event.put("reportTime", report.getCreateTime());
                riskEvents.add(event);
            }
            riskMonitoring.put("riskEvents", riskEvents);

            return MyJsonBean.successData("查询成功", riskMonitoring);
        } catch (Exception e) {
            log.error("查询风险监控失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取系统性能")
    @GetMapping("/system-performance")
    public MyJsonBean getSystemPerformance() {
        try {
            Map<String, Object> systemPerformance = new HashMap<>();
            // 系统指标 - 运行时计算
            Runtime runtime = Runtime.getRuntime();
            long totalMemory = runtime.totalMemory();
            long freeMemory = runtime.freeMemory();
            long usedMemory = totalMemory - freeMemory;
            double memoryUsage = (double) usedMemory / totalMemory * 100;

            Map<String, Object> systemMetrics = new HashMap<>();
            systemMetrics.put("memoryUsage", memoryUsage);
            systemMetrics.put("totalMemoryMB", totalMemory / 1024 / 1024);
            systemMetrics.put("usedMemoryMB", usedMemory / 1024 / 1024);
            systemMetrics.put("activeReports", expenseReportMapper.selectCount(null));
            systemMetrics.put("pendingReports", expenseReportMapper.selectCount(
                    new QueryWrapper<TblExpenseReport>().eq("REPORT_STATUS", "PENDING")));
            systemPerformance.put("systemMetrics", systemMetrics);

            return MyJsonBean.successData("查询成功", systemPerformance);
        } catch (Exception e) {
            log.error("查询系统性能失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取自定义看板配置")
    @GetMapping("/custom-config")
    public MyJsonBean getCustomDashboardConfig(@RequestParam String userId) {
        try {
            Map<String, Object> customConfig = new HashMap<>();
            customConfig.put("userId", userId);
            customConfig.put("dashboardName", "我的费控看板");

            List<Map<String, Object>> widgets = new ArrayList<>();
            widgets.add(mapOf("widgetId", "overview", "widgetName", "总览", "visible", true));
            widgets.add(mapOf("widgetId", "expense-trend", "widgetName", "费用趋势", "visible", true));
            widgets.add(mapOf("widgetId", "budget-usage", "widgetName", "预算使用", "visible", true));
            widgets.add(mapOf("widgetId", "approval-stats", "widgetName", "审批统计", "visible", true));
            widgets.add(mapOf("widgetId", "risk-monitoring", "widgetName", "风险监控", "visible", true));
            customConfig.put("widgets", widgets);
            customConfig.put("refreshInterval", 300);
            customConfig.put("lastModified", LocalDateTime.now());

            return MyJsonBean.successData("查询成功", customConfig);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("保存自定义看板配置")
    @PostMapping("/custom-config")
    public MyJsonBean saveCustomDashboardConfig(@RequestBody Map<String, Object> configData) {
        try {
            configData.put("lastModified", LocalDateTime.now());
            configData.put("version", 1);
            return MyJsonBean.successData("保存成功", configData);
        } catch (Exception e) {
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    private Map<String, Object> mapOf(String k1, Object v1, String k2, Object v2, String k3, Object v3) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(k1, v1); map.put(k2, v2); map.put(k3, v3);
        return map;
    }
}
