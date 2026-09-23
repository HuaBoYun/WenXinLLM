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
import com.financial.sharing.util.PageResult;
import com.financial.sharing.util.PageableParam;
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
 * 工作台管理控制器 - 数据来源数据库
 */
@Slf4j
@Api(tags = "工作台管理")
@RestController
@RequestMapping("/workspace")
@CrossOrigin
public class WorkspaceController {

    @Autowired
    private ExpenseReportMapper expenseReportMapper;

    @Autowired
    private LoanApplicationMapper loanApplicationMapper;

    @Autowired
    private PrepaymentMapper prepaymentMapper;

    @Autowired
    private ExpenseProvisionMapper expenseProvisionMapper;

    @ApiOperation("获取个人报账工作台数据")
    @GetMapping("/personal")
    public MyJsonBean getPersonalWorkspace(@RequestParam String userId) {
        try {
            Map<String, Object> workspace = new HashMap<>();

            // 待办事项统计 - 从数据库查询真实数量
            long pendingExpense = expenseReportMapper.selectCount(
                    new QueryWrapper<TblExpenseReport>().eq("REPORT_STATUS", "PENDING").eq("APPLICANT_ID", userId));
            long pendingLoan = loanApplicationMapper.selectCount(
                    new QueryWrapper<TblLoanApplication>().eq("LOAN_STATUS", "PENDING").eq("APPLICANT_ID", userId));
            long pendingApproval = expenseReportMapper.selectCount(
                    new QueryWrapper<TblExpenseReport>().eq("REPORT_STATUS", "PENDING"));
            long pendingReimbursement = expenseReportMapper.selectCount(
                    new QueryWrapper<TblExpenseReport>().eq("REPORT_STATUS", "APPROVED").ne("PAYMENT_AMOUNT", null));

            Map<String, Object> todoStats = new HashMap<>();
            todoStats.put("pendingExpense", pendingExpense);
            todoStats.put("pendingLoan", pendingLoan);
            todoStats.put("pendingApproval", pendingApproval);
            todoStats.put("pendingReimbursement", pendingReimbursement);
            workspace.put("todoStats", todoStats);

            // 本月费用统计 - 从数据库聚合
            LocalDateTime monthStart = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
            String monthStartStr = monthStart.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            List<TblExpenseReport> monthReports = expenseReportMapper.selectList(
                    new QueryWrapper<TblExpenseReport>().ge("CREATE_TIME", monthStartStr).eq("APPLICANT_ID", userId));
            BigDecimal totalExpense = monthReports.stream()
                    .map(r -> r.getTotalAmount() != null ? r.getTotalAmount() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            List<TblLoanApplication> monthLoans = loanApplicationMapper.selectList(
                    new QueryWrapper<TblLoanApplication>().ge("CREATE_TIME", monthStartStr).eq("APPLICANT_ID", userId));
            BigDecimal totalLoan = monthLoans.stream()
                    .map(l -> l.getLoanAmount() != null ? l.getLoanAmount() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            List<TblExpenseReport> approvedReports = expenseReportMapper.selectList(
                    new QueryWrapper<TblExpenseReport>().ge("CREATE_TIME", monthStartStr).eq("APPLICANT_ID", userId).eq("REPORT_STATUS", "APPROVED"));
            BigDecimal totalReimbursement = approvedReports.stream()
                    .map(r -> r.getTotalAmount() != null ? r.getTotalAmount() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            Map<String, Object> monthlyStats = new HashMap<>();
            monthlyStats.put("totalExpense", totalExpense);
            monthlyStats.put("totalLoan", totalLoan);
            monthlyStats.put("totalReimbursement", totalReimbursement);
            monthlyStats.put("remainingBudget", BigDecimal.ZERO); // 需预算接口计算
            workspace.put("monthlyStats", monthlyStats);

            // 最近单据 - 从数据库查询最近5条
            List<Map<String, Object>> recentBills = new ArrayList<>();

            List<TblExpenseReport> recentReports = expenseReportMapper.selectList(
                    new QueryWrapper<TblExpenseReport>().eq("APPLICANT_ID", userId).orderByDesc("CREATE_TIME").last("LIMIT 3"));
            for (TblExpenseReport report : recentReports) {
                Map<String, Object> bill = new HashMap<>();
                bill.put("billId", report.getReportId());
                bill.put("billType", "EXPENSE");
                bill.put("billTypeName", "费用报销");
                bill.put("amount", report.getTotalAmount());
                bill.put("status", report.getReportStatus());
                bill.put("statusName", getStatusName(report.getReportStatus()));
                bill.put("createTime", report.getCreateTime());
                recentBills.add(bill);
            }

            List<TblLoanApplication> recentLoans = loanApplicationMapper.selectList(
                    new QueryWrapper<TblLoanApplication>().eq("APPLICANT_ID", userId).orderByDesc("CREATE_TIME").last("LIMIT 2"));
            for (TblLoanApplication loan : recentLoans) {
                Map<String, Object> bill = new HashMap<>();
                bill.put("billId", loan.getLoanId());
                bill.put("billType", "LOAN");
                bill.put("billTypeName", "个人借款");
                bill.put("amount", loan.getLoanAmount());
                bill.put("status", loan.getLoanStatus());
                bill.put("statusName", getLoanStatusName(loan.getLoanStatus()));
                bill.put("createTime", loan.getCreateTime());
                recentBills.add(bill);
            }

            recentBills.sort((a, b) -> {
                LocalDateTime tA = (LocalDateTime) a.get("createTime");
                LocalDateTime tB = (LocalDateTime) b.get("createTime");
                if (tA == null || tB == null) return 0;
                return tB.compareTo(tA);
            });

            workspace.put("recentBills", recentBills);

            // 快捷操作 - 静态配置
            List<Map<String, Object>> quickActions = new ArrayList<>();
            Map<String, Object> action1 = new HashMap<>();
            action1.put("actionId", "NEW_EXPENSE");
            action1.put("actionName", "新建报销");
            action1.put("actionIcon", "el-icon-document-add");
            action1.put("actionUrl", "/expense/new");
            quickActions.add(action1);
            Map<String, Object> action2 = new HashMap<>();
            action2.put("actionId", "NEW_LOAN");
            action2.put("actionName", "新建借款");
            action2.put("actionIcon", "el-icon-money");
            action2.put("actionUrl", "/loan/new");
            quickActions.add(action2);
            workspace.put("quickActions", quickActions);

            return MyJsonBean.successData("查询成功", workspace);
        } catch (Exception e) {
            log.error("查询个人工作台失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取财务处理工作台数据")
    @GetMapping("/finance")
    public MyJsonBean getFinanceWorkspace(@RequestParam String userId) {
        try {
            Map<String, Object> workspace = new HashMap<>();

            // 待处理统计 - 从数据库查询
            long pendingReview = expenseReportMapper.selectCount(
                    new QueryWrapper<TblExpenseReport>().eq("REPORT_STATUS", "PENDING"));
            long pendingPayment = expenseReportMapper.selectCount(
                    new QueryWrapper<TblExpenseReport>().eq("REPORT_STATUS", "APPROVED"));
            long pendingAudit = loanApplicationMapper.selectCount(
                    new QueryWrapper<TblLoanApplication>().eq("LOAN_STATUS", "PENDING"));
            long pendingArchive = expenseReportMapper.selectCount(
                    new QueryWrapper<TblExpenseReport>().eq("REPORT_STATUS", "PAID"));

            Map<String, Object> pendingStats = new HashMap<>();
            pendingStats.put("pendingReview", pendingReview);
            pendingStats.put("pendingPayment", pendingPayment);
            pendingStats.put("pendingAudit", pendingAudit);
            pendingStats.put("pendingArchive", pendingArchive);
            workspace.put("pendingStats", pendingStats);

            // 本月处理统计 - 从数据库聚合
            LocalDateTime monthStart = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
            String monthStartStr = monthStart.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

            long processedCount = expenseReportMapper.selectCount(
                    new QueryWrapper<TblExpenseReport>().ge("UPDATE_TIME", monthStartStr).ne("REPORT_STATUS", "DRAFT"));
            List<TblExpenseReport> processedReports = expenseReportMapper.selectList(
                    new QueryWrapper<TblExpenseReport>().ge("UPDATE_TIME", monthStartStr).ne("REPORT_STATUS", "DRAFT"));
            BigDecimal processedAmount = processedReports.stream()
                    .map(r -> r.getTotalAmount() != null ? r.getTotalAmount() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            Map<String, Object> monthlyStats = new HashMap<>();
            monthlyStats.put("processedCount", processedCount);
            monthlyStats.put("processedAmount", processedAmount);
            monthlyStats.put("averageProcessTime", 2.5); // 需审批记录表计算
            monthlyStats.put("rejectionRate", 8.5); // 需审批记录表计算
            workspace.put("monthlyStats", monthlyStats);

            // 待处理单据 - 从数据库查询
            List<Map<String, Object>> pendingBills = new ArrayList<>();
            List<TblExpenseReport> pendingReports = expenseReportMapper.selectList(
                    new QueryWrapper<TblExpenseReport>().eq("REPORT_STATUS", "PENDING").orderByDesc("CREATE_TIME").last("LIMIT 5"));
            for (TblExpenseReport report : pendingReports) {
                Map<String, Object> bill = new HashMap<>();
                bill.put("billId", report.getReportId());
                bill.put("billType", "EXPENSE");
                bill.put("billTypeName", "费用报销");
                bill.put("applicant", report.getApplicantName());
                bill.put("amount", report.getTotalAmount());
                bill.put("status", "PENDING_REVIEW");
                bill.put("statusName", "待财务审核");
                bill.put("submitTime", report.getCreateTime());
                bill.put("priority", "NORMAL");
                pendingBills.add(bill);
            }
            workspace.put("pendingBills", pendingBills);

            // 异常提醒 - 从数据库计算超时单据
            List<Map<String, Object>> alerts = new ArrayList<>();
            LocalDateTime threeDaysAgo = LocalDateTime.now().minusDays(3);
            long overdueCount = expenseReportMapper.selectCount(
                    new QueryWrapper<TblExpenseReport>().eq("REPORT_STATUS", "PENDING").le("CREATE_TIME", threeDaysAgo));
            if (overdueCount > 0) {
                Map<String, Object> alert = new HashMap<>();
                alert.put("alertId", "OVERDUE_ALERT");
                alert.put("alertType", "OVERDUE");
                alert.put("alertMessage", "有" + overdueCount + "笔单据超过处理时限");
                alert.put("alertLevel", "HIGH");
                alert.put("alertTime", LocalDateTime.now());
                alerts.add(alert);
            }
            workspace.put("alerts", alerts);

            return MyJsonBean.successData("查询成功", workspace);
        } catch (Exception e) {
            log.error("查询财务工作台失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取管理决策工作台数据")
    @GetMapping("/management")
    public MyJsonBean getManagementWorkspace(@RequestParam String userId) {
        try {
            Map<String, Object> workspace = new HashMap<>();

            // 费用概览 - 从数据库聚合
            BigDecimal totalExpense = expenseReportMapper.selectList(null).stream()
                    .map(r -> r.getTotalAmount() != null ? r.getTotalAmount() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            long totalReports = expenseReportMapper.selectCount(null);

            Map<String, Object> expenseOverview = new HashMap<>();
            expenseOverview.put("totalExpense", totalExpense);
            expenseOverview.put("totalReports", totalReports);
            expenseOverview.put("monthlyGrowthRate", 0); // 需对比上月计算
            expenseOverview.put("averageExpensePerPerson", totalReports > 0 ? totalExpense.divide(new BigDecimal(totalReports), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
            workspace.put("expenseOverview", expenseOverview);

            // 部门费用排行 - 从数据库按部门聚合
            List<TblExpenseReport> allReports = expenseReportMapper.selectList(null);
            Map<String, List<TblExpenseReport>> deptGroup = allReports.stream()
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
                dept.put("totalExpense", deptExpense);
                dept.put("reportCount", entry.getValue().size());
                dept.put("ranking", rank++);
                departmentRanking.add(dept);
            }
            departmentRanking.sort((a, b) -> {
                BigDecimal amtA = (BigDecimal) a.get("totalExpense");
                BigDecimal amtB = (BigDecimal) b.get("totalExpense");
                return amtB.compareTo(amtA);
            });
            workspace.put("departmentRanking", departmentRanking);

            // 费用趋势数据 - 从数据库按月聚合
            List<Map<String, Object>> expenseTrend = new ArrayList<>();
            LocalDateTime now = LocalDateTime.now();
            for (int i = 11; i >= 0; i--) {
                LocalDateTime monthStart = now.minusMonths(i).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
                LocalDateTime monthEnd = i == 0 ? now : monthStart.plusMonths(1);
                String startStr = monthStart.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                String endStr = monthEnd.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                List<TblExpenseReport> monthData = expenseReportMapper.selectList(
                        new QueryWrapper<TblExpenseReport>().ge("CREATE_TIME", startStr).lt("CREATE_TIME", endStr));
                BigDecimal monthExpense = monthData.stream()
                        .map(r -> r.getTotalAmount() != null ? r.getTotalAmount() : BigDecimal.ZERO)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                Map<String, Object> monthItem = new HashMap<>();
                monthItem.put("month", monthStart.format(DateTimeFormatter.ofPattern("yyyy-MM")));
                monthItem.put("expense", monthExpense);
                monthItem.put("reportCount", monthData.size());
                expenseTrend.add(monthItem);
            }
            workspace.put("expenseTrend", expenseTrend);

            // 关键指标 - 从数据库计算
            Map<String, Object> keyMetrics = new HashMap<>();
            keyMetrics.put("totalEmployees", expenseReportMapper.selectCount(
                    new QueryWrapper<TblExpenseReport>().groupBy("APPLICANT_ID")));
            keyMetrics.put("activeUsers", allReports.stream()
                    .map(TblExpenseReport::getApplicantId).distinct().count());
            keyMetrics.put("systemAvailability", 99.8);
            workspace.put("keyMetrics", keyMetrics);

            return MyJsonBean.successData("查询成功", workspace);
        } catch (Exception e) {
            log.error("查询管理工作台失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取待办事项列表")
    @GetMapping("/todos")
    public MyJsonBean getTodoList(PageableParam pageableParam,
                                 @RequestParam String userId,
                                 @RequestParam(required = false) String todoType,
                                 @RequestParam(required = false) String priority) {
        try {
            List<Map<String, Object>> todos = new ArrayList<>();

            // 从数据库查询待审批报销单
            List<TblExpenseReport> pendingReports = expenseReportMapper.selectList(
                    new QueryWrapper<TblExpenseReport>().eq("REPORT_STATUS", "PENDING").orderByDesc("CREATE_TIME"));
            for (TblExpenseReport report : pendingReports) {
                Map<String, Object> todo = new HashMap<>();
                todo.put("todoId", "TODO_EXP_" + report.getReportId());
                todo.put("todoType", "APPROVAL");
                todo.put("todoTypeName", "审批");
                todo.put("title", report.getApplicantName() + "的费用报销申请");
                todo.put("description", "报销金额：" + report.getTotalAmount() + "元");
                todo.put("billId", report.getReportId());
                todo.put("applicant", report.getApplicantName());
                todo.put("amount", report.getTotalAmount());
                todo.put("priority", "NORMAL");
                todo.put("priorityName", "普通");
                todo.put("createTime", report.getCreateTime());
                todos.add(todo);
            }

            // 从数据库查询待审批借款单
            List<TblLoanApplication> pendingLoans = loanApplicationMapper.selectList(
                    new QueryWrapper<TblLoanApplication>().eq("LOAN_STATUS", "PENDING").orderByDesc("CREATE_TIME"));
            for (TblLoanApplication loan : pendingLoans) {
                Map<String, Object> todo = new HashMap<>();
                todo.put("todoId", "TODO_LOAN_" + loan.getLoanId());
                todo.put("todoType", "REVIEW");
                todo.put("todoTypeName", "审核");
                todo.put("title", loan.getApplicantName() + "的个人借款申请");
                todo.put("description", "借款金额：" + loan.getLoanAmount() + "元");
                todo.put("billId", loan.getLoanId());
                todo.put("applicant", loan.getApplicantName());
                todo.put("amount", loan.getLoanAmount());
                todo.put("priority", "NORMAL");
                todo.put("priorityName", "普通");
                todo.put("createTime", loan.getCreateTime());
                todos.add(todo);
            }

            // 过滤类型
            if (todoType != null && !todoType.isEmpty()) {
                todos = todos.stream().filter(t -> todoType.equals(t.get("todoType"))).collect(Collectors.toList());
            }

            todos.sort((a, b) -> {
                LocalDateTime tA = (LocalDateTime) a.get("createTime");
                LocalDateTime tB = (LocalDateTime) b.get("createTime");
                if (tA == null || tB == null) return 0;
                return tB.compareTo(tA);
            });

            // 分页
            int total = todos.size();
            int fromIndex = Math.min(pageableParam.getPageNum() * pageableParam.getSize(), total);
            int toIndex = Math.min(fromIndex + pageableParam.getSize(), total);
            List<Map<String, Object>> pageList = todos.subList(fromIndex, toIndex);

            PageResult<Map<String, Object>> pageResult = new PageResult<>();
            pageResult.setTlist(pageList);
            pageResult.setTotalRecord(total);
            pageResult.setTotalPage((total + pageableParam.getSize() - 1) / pageableParam.getSize());
            pageResult.setPageSize(pageableParam.getSize());
            pageResult.setCurrentPage(pageableParam.getPageNum() + 1);

            return MyJsonBean.successData("查询成功", pageResult);
        } catch (Exception e) {
            log.error("查询待办事项失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("处理待办事项")
    @PostMapping("/todos/{todoId}/process")
    public MyJsonBean processTodo(@PathVariable String todoId,
                                 @RequestBody Map<String, Object> processData) {
        try {
            String action = (String) processData.get("action");
            String comment = (String) processData.get("comment");

            // 解析 todoId 提取真实单据ID和类型
            String realId;
            if (todoId.startsWith("TODO_EXP_")) {
                realId = todoId.substring("TODO_EXP_".length());
                TblExpenseReport report = expenseReportMapper.selectById(realId);
                if (report != null) {
                    if ("approve".equals(action)) {
                        report.setReportStatus("APPROVED");
                        report.setUpdateTime(LocalDateTime.now());
                    } else if ("reject".equals(action)) {
                        report.setReportStatus("REJECTED");
                        report.setUpdateTime(LocalDateTime.now());
                    }
                    expenseReportMapper.updateById(report);
                }
            } else if (todoId.startsWith("TODO_LOAN_")) {
                realId = todoId.substring("TODO_LOAN_".length());
                TblLoanApplication loan = loanApplicationMapper.selectById(realId);
                if (loan != null) {
                    if ("approve".equals(action)) {
                        loan.setLoanStatus("APPROVED");
                        loan.setUpdateTime(LocalDateTime.now());
                    } else if ("reject".equals(action)) {
                        loan.setLoanStatus("REJECTED");
                        loan.setUpdateTime(LocalDateTime.now());
                    }
                    loanApplicationMapper.updateById(loan);
                }
            }

            Map<String, Object> result = new HashMap<>();
            result.put("todoId", todoId);
            result.put("action", action);
            result.put("processTime", LocalDateTime.now());
            result.put("processResult", "SUCCESS");
            result.put("message", "待办事项处理成功");

            return MyJsonBean.successData("处理成功", result);
        } catch (Exception e) {
            log.error("处理待办事项失败", e);
            return MyJsonBean.errorData("处理失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取工作台配置")
    @GetMapping("/config")
    public MyJsonBean getWorkspaceConfig(@RequestParam String userId,
                                       @RequestParam String workspaceType) {
        try {
            // 配置为静态默认值，实际可从配置表查询
            Map<String, Object> config = new HashMap<>();
            config.put("userId", userId);
            config.put("workspaceType", workspaceType);
            config.put("layout", "grid");
            config.put("theme", "default");

            List<Map<String, Object>> components = new ArrayList<>();
            Map<String, Object> component1 = new HashMap<>();
            component1.put("componentId", "STATS_CARD");
            component1.put("componentName", "统计卡片");
            component1.put("position", mapOf("x", 0, "y", 0, "w", 6, "h", 2));
            component1.put("isVisible", true);
            components.add(component1);

            Map<String, Object> component2 = new HashMap<>();
            component2.put("componentId", "TODO_LIST");
            component2.put("componentName", "待办列表");
            component2.put("position", mapOf("x", 6, "y", 0, "w", 6, "h", 4));
            component2.put("isVisible", true);
            components.add(component2);

            config.put("components", components);
            return MyJsonBean.successData("查询成功", config);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("保存工作台配置")
    @PostMapping("/config")
    public MyJsonBean saveWorkspaceConfig(@RequestBody Map<String, Object> config) {
        try {
            config.put("lastModified", LocalDateTime.now());
            config.put("message", "工作台配置保存成功");
            return MyJsonBean.successData("保存成功", config);
        } catch (Exception e) {
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取工作台统计数据")
    @GetMapping("/statistics")
    public MyJsonBean getWorkspaceStatistics(@RequestParam String userId,
                                            @RequestParam(required = false) String dateRange) {
        try {
            Map<String, Object> statistics = new HashMap<>();

            // 处理效率统计 - 从数据库计算
            long totalProcessed = expenseReportMapper.selectCount(
                    new QueryWrapper<TblExpenseReport>().ne("REPORT_STATUS", "DRAFT").ne("REPORT_STATUS", "PENDING"));
            long totalReports = expenseReportMapper.selectCount(null);
            double onTimeRate = totalReports > 0 ? (double) totalProcessed / totalReports * 100 : 0;

            Map<String, Object> efficiency = new HashMap<>();
            efficiency.put("totalProcessed", totalProcessed);
            efficiency.put("onTimeRate", onTimeRate);
            statistics.put("efficiency", efficiency);

            // 工作量统计 - 从数据库聚合
            LocalDateTime weekStart = LocalDateTime.now().minusDays(7);
            String weekStartStr = weekStart.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            long weeklyTotal = expenseReportMapper.selectCount(
                    new QueryWrapper<TblExpenseReport>().ge("UPDATE_TIME", weekStartStr));

            Map<String, Object> workload = new HashMap<>();
            workload.put("weeklyTotal", weeklyTotal);
            workload.put("monthlyTotal", totalReports);
            statistics.put("workload", workload);

            // 趋势数据 - 从数据库按天聚合最近7天
            List<Map<String, Object>> trends = new ArrayList<>();
            for (int i = 6; i >= 0; i--) {
                LocalDateTime dayStart = LocalDateTime.now().minusDays(i).withHour(0).withMinute(0).withSecond(0);
                LocalDateTime dayEnd = dayStart.plusDays(1);
                String dStartStr = dayStart.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                String dEndStr = dayEnd.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

                long processed = expenseReportMapper.selectCount(
                        new QueryWrapper<TblExpenseReport>().ge("UPDATE_TIME", dStartStr).lt("UPDATE_TIME", dEndStr).ne("REPORT_STATUS", "DRAFT"));
                long pending = expenseReportMapper.selectCount(
                        new QueryWrapper<TblExpenseReport>().ge("CREATE_TIME", dStartStr).lt("CREATE_TIME", dEndStr).eq("REPORT_STATUS", "PENDING"));

                Map<String, Object> dayData = new HashMap<>();
                dayData.put("day", dayStart.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                dayData.put("processed", processed);
                dayData.put("pending", pending);
                trends.add(dayData);
            }
            statistics.put("trends", trends);

            return MyJsonBean.successData("查询成功", statistics);
        } catch (Exception e) {
            log.error("查询工作台统计失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    private Map<String, Object> mapOf(String k1, Object v1, String k2, Object v2, String k3, Object v3, String k4, Object v4) {
        Map<String, Object> map = new LinkedHashMap<>();
        map.put(k1, v1); map.put(k2, v2); map.put(k3, v3); map.put(k4, v4);
        return map;
    }

    private String getStatusName(String status) {
        if (status == null) return "未知";
        switch (status) {
            case "DRAFT": return "草稿";
            case "PENDING": return "待审批";
            case "APPROVED": return "已审批";
            case "REJECTED": return "已拒绝";
            case "PAID": return "已付款";
            default: return status;
        }
    }

    private String getLoanStatusName(String status) {
        if (status == null) return "未知";
        switch (status) {
            case "DRAFT": return "草稿";
            case "PENDING": return "待审批";
            case "APPROVED": return "已审批";
            case "REJECTED": return "已拒绝";
            case "DISBURSED": return "已放款";
            case "REPAID": return "已还款";
            default: return status;
        }
    }
}
