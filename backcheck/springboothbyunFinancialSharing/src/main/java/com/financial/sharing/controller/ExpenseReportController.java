package com.financial.sharing.controller;

import com.financial.sharing.business.entity.TblExpenseReport;
import com.financial.sharing.business.service.ExpenseReportService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 报销单管理控制器
 * 
 * @author Financial Sharing System
 * @since 2024-12-19
 */
@Api(tags = "报销单管理")
@RestController
@RequestMapping("/expense/reports")
@CrossOrigin
public class ExpenseReportController {

    @Autowired
    private ExpenseReportService expenseReportService;

    @ApiOperation("查询报销单列表")
    @GetMapping
    public MyJsonBean getExpenseReportList(
            @RequestParam(required = false) String reportCode,
            @RequestParam(required = false) String reportTitle,
            @RequestParam(required = false) String applicantName,
            @RequestParam(required = false) String reportStatus,
            @RequestParam(required = false) String reportType,
            @RequestParam(required = false) String applicantDeptId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("reportCode", reportCode);
            param.put("reportTitle", reportTitle);
            param.put("applicantName", applicantName);
            param.put("reportStatus", reportStatus);
            param.put("reportType", reportType);
            param.put("applicantDeptId", applicantDeptId);
            param.put("startDate", startDate);
            param.put("endDate", endDate);
            param.put("pageNum", pageNum);
            param.put("pageSize", pageSize);

            return expenseReportService.getList(param);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @ApiOperation("根据 ID 获取报销单详情")
    @GetMapping("/{reportId}")
    public MyJsonBean getExpenseReportDetail(@PathVariable String reportId) {
        try {
            return expenseReportService.getById(reportId);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @ApiOperation("根据报销单号查询")
    @GetMapping("/code/{reportCode}")
    public MyJsonBean getExpenseReportByCode(@PathVariable String reportCode) {
        try {
            return expenseReportService.getByReportCode(reportCode);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @ApiOperation("保存报销单")
    @PostMapping
    public MyJsonBean saveExpenseReport(@RequestBody TblExpenseReport expenseReport) {
        try {
            return expenseReportService.saveOrUpdate(expenseReport);
        } catch (Exception e) {
            return MyJsonBean.errorData("保存失败：" + e.getMessage());
        }
    }

    @ApiOperation("删除报销单")
    @DeleteMapping("/{reportId}")
    public MyJsonBean deleteExpenseReport(@PathVariable String reportId) {
        try {
            return expenseReportService.delete(reportId);
        } catch (Exception e) {
            return MyJsonBean.errorData("删除失败：" + e.getMessage());
        }
    }

    @ApiOperation("批量删除报销单")
    @DeleteMapping("/batch")
    public MyJsonBean batchDeleteExpenseReports(@RequestBody List<String> reportIds) {
        try {
            return expenseReportService.batchDelete(reportIds);
        } catch (Exception e) {
            return MyJsonBean.errorData("批量删除失败：" + e.getMessage());
        }
    }

    @ApiOperation("提交报销单")
    @PostMapping("/{reportId}/submit")
    public MyJsonBean submitExpenseReport(@PathVariable String reportId) {
        try {
            return expenseReportService.submit(reportId);
        } catch (Exception e) {
            return MyJsonBean.errorData("提交失败：" + e.getMessage());
        }
    }

    @ApiOperation("审批报销单")
    @PostMapping("/{reportId}/approve")
    public MyJsonBean approveExpenseReport(@PathVariable String reportId,
                                          @RequestBody Map<String, String> approvalData) {
        try {
            String action = approvalData.get("action");
            String opinion = approvalData.get("opinion");
            return expenseReportService.approve(reportId, action, opinion);
        } catch (Exception e) {
            return MyJsonBean.errorData("审批失败：" + e.getMessage());
        }
    }

    @ApiOperation("撤回报销单")
    @PostMapping("/{reportId}/withdraw")
    public MyJsonBean withdrawExpenseReport(@PathVariable String reportId) {
        try {
            return expenseReportService.withdraw(reportId);
        } catch (Exception e) {
            return MyJsonBean.errorData("撤回失败：" + e.getMessage());
        }
    }

    @ApiOperation("更新报销单状态")
    @PutMapping("/{reportId}/status")
    public MyJsonBean updateExpenseReportStatus(@PathVariable String reportId,
                                                @RequestParam String reportStatus) {
        try {
            return expenseReportService.updateStatus(reportId, reportStatus);
        } catch (Exception e) {
            return MyJsonBean.errorData("状态更新失败：" + e.getMessage());
        }
    }

    @ApiOperation("确认付款")
    @PostMapping("/{reportId}/payment")
    public MyJsonBean confirmPayment(@PathVariable String reportId,
                                     @RequestParam(required = false) String paymentVoucherNo) {
        try {
            return expenseReportService.confirmPayment(reportId, paymentVoucherNo);
        } catch (Exception e) {
            return MyJsonBean.errorData("付款确认失败：" + e.getMessage());
        }
    }

    @ApiOperation("获取报销单统计")
    @GetMapping("/statistics")
    public MyJsonBean getExpenseReportStatistics(
            @RequestParam(required = false) String applicantId,
            @RequestParam(required = false) String applicantDeptId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("applicantId", applicantId);
            param.put("applicantDeptId", applicantDeptId);
            param.put("startDate", startDate);
            param.put("endDate", endDate);

            return expenseReportService.getStatistics(param);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @ApiOperation("导出报销单")
    @GetMapping("/export")
    public MyJsonBean exportExpenseReports(
            @RequestParam(required = false) String reportCode,
            @RequestParam(required = false) String applicantName,
            @RequestParam(required = false) String reportStatus,
            @RequestParam(required = false) String reportType,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("reportCode", reportCode);
            param.put("applicantName", applicantName);
            param.put("reportStatus", reportStatus);
            param.put("reportType", reportType);
            param.put("startDate", startDate);
            param.put("endDate", endDate);
            return expenseReportService.export(param);
        } catch (Exception e) {
            return MyJsonBean.errorData("导出失败：" + e.getMessage());
        }
    }

    @ApiOperation("获取报销单明细")
    @GetMapping("/{reportId}/details")
    public MyJsonBean getExpenseReportDetails(@PathVariable String reportId) {
        try {
            return expenseReportService.getDetails(reportId);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @ApiOperation("保存报销单明细")
    @PostMapping("/{reportId}/details")
    public MyJsonBean saveExpenseReportDetails(@PathVariable String reportId,
                                               @RequestBody List<Map<String, Object>> details) {
        try {
            return expenseReportService.saveDetails(reportId, details);
        } catch (Exception e) {
            return MyJsonBean.errorData("保存失败：" + e.getMessage());
        }
    }

    @ApiOperation("费用分摊")
    @PostMapping("/{reportId}/allocate")
    public MyJsonBean allocateExpense(@PathVariable String reportId,
                                      @RequestBody Map<String, Object> allocationData) {
        try {
            return expenseReportService.allocate(reportId, allocationData);
        } catch (Exception e) {
            return MyJsonBean.errorData("分摊失败：" + e.getMessage());
        }
    }

    @ApiOperation("打印报销单")
    @GetMapping("/{reportId}/print")
    public MyJsonBean printExpenseReport(@PathVariable String reportId) {
        try {
            return expenseReportService.print(reportId);
        } catch (Exception e) {
            return MyJsonBean.errorData("打印失败：" + e.getMessage());
        }
    }
}
