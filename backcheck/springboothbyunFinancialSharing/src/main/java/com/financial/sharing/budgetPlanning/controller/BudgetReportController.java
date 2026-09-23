package com.financial.sharing.budgetPlanning.controller;

import com.financial.sharing.budgetPlanning.dto.BudgetReportQueryParam;
import com.financial.sharing.budgetPlanning.entity.TblBudgetReport;
import com.financial.sharing.budgetPlanning.service.BudgetReportService;
import com.github.pagehelper.PageInfo;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.UserUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 预算报表Controller
 * 
 * @author hbyun
 * @date 2026-02-02
 */
@Slf4j
@Api(tags = "预算报表查询")
@RestController
@RequestMapping("/budgetPlanning/budgetReport")
public class BudgetReportController {

    @Autowired
    private BudgetReportService budgetReportService;

    /**
     * 把异常压扁成可读字符串：找到根因 + 类名 + 消息。
     * 用于诊断 e.getMessage() 为 null 的情况（典型如 NPE）。
     */
    private static String diagMsg(Throwable e) {
        if (e == null) {
            return "未知错误";
        }
        Throwable root = e;
        while (root.getCause() != null && root.getCause() != root) {
            root = root.getCause();
        }
        String cls = root.getClass().getSimpleName();
        String msg = root.getMessage();
        if (msg == null) {
            StackTraceElement[] st = root.getStackTrace();
            String at = (st != null && st.length > 0) ? " at " + st[0].toString() : "";
            return "[" + cls + "]" + at;
        }
        return "[" + cls + "] " + msg;
    }

    /** 识别 UserUtils.requireXxx() 抛出的"用户未登录或会话已失效"异常 */
    private static boolean isSessionExpired(Throwable e) {
        while (e != null) {
            String msg = e.getMessage();
            if (msg != null && msg.contains("用户未登录或会话已失效")) {
                return true;
            }
            e = e.getCause();
        }
        return false;
    }

    /** 列表型空数据(分页) */
    private static PageInfo<Map<String, Object>> emptyPageInfo() {
        return new PageInfo<>(java.util.Collections.<Map<String, Object>>emptyList());
    }

    /** 列表型空数据(数组) */
    private static List<Map<String, Object>> emptyList() {
        return java.util.Collections.emptyList();
    }

    /**
     * 查询报表配置列表
     */
    @ApiOperation("查询报表配置列表")
    @PostMapping("/getReportList")
    public MyJsonBean getReportList(@RequestBody BudgetReportQueryParam param) {
        try {
            String orgId = UserUtils.requireOrgId();
            param.setOrgId(orgId);

            PageInfo<TblBudgetReport> pageInfo = budgetReportService.getReportList(param);
            return MyJsonBean.successData(pageInfo);
        } catch (Exception e) {
            if (isSessionExpired(e)) {
                return MyJsonBean.successData(new PageInfo<>(java.util.Collections.<TblBudgetReport>emptyList()));
            }
            log.error("查询报表配置列表失败", e);
            return MyJsonBean.errorData("查询失败: " + diagMsg(e));
        }
    }

    /**
     * 根据ID查询报表配置
     */
    @ApiOperation("根据ID查询报表配置")
    @PostMapping("/getReportById")
    public MyJsonBean getReportById(@RequestBody Map<String, String> params) {
        try {
            String reportId = params.get("reportId");
            if (reportId == null || reportId.trim().isEmpty()) {
                return MyJsonBean.errorData("报表ID不能为空");
            }

            TblBudgetReport report = budgetReportService.getReportById(reportId);
            if (report == null) {
                return MyJsonBean.errorData("报表配置不存在");
            }

            return MyJsonBean.successData(report);
        } catch (Exception e) {
            log.error("查询报表配置失败", e);
            return MyJsonBean.errorData("查询失败: " + diagMsg(e));
        }
    }

    /**
     * 创建报表配置
     */
    @ApiOperation("创建报表配置")
    @PostMapping("/createReport")
    public MyJsonBean createReport(@RequestBody TblBudgetReport report) {
        try {
            // 获取当前用户信息（避免 orgid/staffid 链式 NPE）
            String orgId = UserUtils.requireOrgId();
            String userId = UserUtils.requireUserId();

            report.setOrgId(orgId);
            report.setCreateUser(userId);
            report.setUpdateUser(userId);

            Map<String, Object> result = budgetReportService.createReport(report);
            if ((Boolean) result.get("success")) {
                return MyJsonBean.successData(result.get("data"));
            } else {
                return MyJsonBean.errorData((String) result.get("message"));
            }
        } catch (Exception e) {
            log.error("创建报表配置失败", e);
            return MyJsonBean.errorData("创建失败: " + diagMsg(e));
        }
    }

    /**
     * 更新报表配置
     */
    @ApiOperation("更新报表配置")
    @PostMapping("/updateReport")
    public MyJsonBean updateReport(@RequestBody TblBudgetReport report) {
        try {
            // 获取当前用户ID（避免 staffid 链式 NPE）
            String userId = UserUtils.requireUserId();
            report.setUpdateUser(userId);

            Map<String, Object> result = budgetReportService.updateReport(report);
            if ((Boolean) result.get("success")) {
                return MyJsonBean.successData(result.get("data"));
            } else {
                return MyJsonBean.errorData((String) result.get("message"));
            }
        } catch (Exception e) {
            log.error("更新报表配置失败", e);
            return MyJsonBean.errorData("更新失败: " + diagMsg(e));
        }
    }

    /**
     * 删除报表配置
     */
    @ApiOperation("删除报表配置")
    @PostMapping("/deleteReports")
    public MyJsonBean deleteReports(@RequestBody Map<String, List<String>> params) {
        try {
            List<String> reportIds = params.get("reportIds");
            Map<String, Object> result = budgetReportService.deleteReports(reportIds);
            
            if ((Boolean) result.get("success")) {
                return MyJsonBean.successData(result.get("message"));
            } else {
                return MyJsonBean.errorData((String) result.get("message"));
            }
        } catch (Exception e) {
            log.error("删除报表配置失败", e);
            return MyJsonBean.errorData("删除失败: " + diagMsg(e));
        }
    }

    /**
     * 查询预算明细数据
     */
    @ApiOperation("查询预算明细数据")
    @PostMapping("/queryDetailData")
    public MyJsonBean queryDetailData(@RequestBody BudgetReportQueryParam param) {
        try {
            String orgId = UserUtils.requireOrgId();
            param.setOrgId(orgId);

            PageInfo<Map<String, Object>> pageInfo = budgetReportService.queryBudgetDetailData(param);
            return MyJsonBean.successData(pageInfo);
        } catch (Exception e) {
            if (isSessionExpired(e)) {
                return MyJsonBean.successData(emptyPageInfo());
            }
            log.error("查询预算明细数据失败", e);
            return MyJsonBean.errorData("查询失败: " + diagMsg(e));
        }
    }

    /**
     * 查询预算汇总数据
     */
    @ApiOperation("查询预算汇总数据")
    @PostMapping("/querySummaryData")
    public MyJsonBean querySummaryData(@RequestBody BudgetReportQueryParam param) {
        try {
            String orgId = UserUtils.requireOrgId();
            param.setOrgId(orgId);

            List<Map<String, Object>> data = budgetReportService.queryBudgetSummaryData(param);
            return MyJsonBean.successData(data);
        } catch (Exception e) {
            if (isSessionExpired(e)) {
                return MyJsonBean.successData(emptyList());
            }
            log.error("查询预算汇总数据失败", e);
            return MyJsonBean.errorData("查询失败: " + diagMsg(e));
        }
    }

    /**
     * 查询预算对比数据
     */
    @ApiOperation("查询预算对比数据")
    @PostMapping("/queryCompareData")
    public MyJsonBean queryCompareData(@RequestBody BudgetReportQueryParam param) {
        try {
            String orgId = UserUtils.requireOrgId();
            param.setOrgId(orgId);

            List<Map<String, Object>> data = budgetReportService.queryBudgetCompareData(param);
            return MyJsonBean.successData(data);
        } catch (Exception e) {
            if (isSessionExpired(e)) {
                return MyJsonBean.successData(emptyList());
            }
            log.error("查询预算对比数据失败", e);
            return MyJsonBean.errorData("查询失败: " + diagMsg(e));
        }
    }

    /**
     * 查询预算趋势数据
     */
    @ApiOperation("查询预算趋势数据")
    @PostMapping("/queryTrendData")
    public MyJsonBean queryTrendData(@RequestBody BudgetReportQueryParam param) {
        try {
            String orgId = UserUtils.requireOrgId();
            param.setOrgId(orgId);

            List<Map<String, Object>> data = budgetReportService.queryBudgetTrendData(param);
            return MyJsonBean.successData(data);
        } catch (Exception e) {
            if (isSessionExpired(e)) {
                return MyJsonBean.successData(emptyList());
            }
            log.error("查询预算趋势数据失败", e);
            return MyJsonBean.errorData("查询失败: " + diagMsg(e));
        }
    }

    /**
     * 导出报表数据
     */
    @ApiOperation("导出报表数据")
    @PostMapping("/exportData")
    public MyJsonBean exportData(@RequestBody BudgetReportQueryParam param) {
        try {
            // 获取当前用户组织ID（统一使用 ORG_ID 字段进行租户/组织隔离）
            String orgId = UserUtils.requireOrgId();
            param.setOrgId(orgId);

            Map<String, Object> result = budgetReportService.exportReportData(param);
            if ((Boolean) result.get("success")) {
                return MyJsonBean.successData(result.get("data"));
            } else {
                return MyJsonBean.errorData((String) result.get("message"));
            }
        } catch (Exception e) {
            log.error("导出报表数据失败", e);
            return MyJsonBean.errorData("导出失败: " + diagMsg(e));
        }
    }
}

