package com.financial.sharing.controller;

import com.financial.sharing.business.entity.TblLoanApplication;
import com.financial.sharing.business.service.LoanApplicationService;
import com.financial.sharing.util.MyJsonBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * 借款单管理控制器
 *
 * @author Financial Sharing System
 * @since 2026-03-02
 */
@Api(tags = "借款单管理")
@RestController
@RequestMapping("/expense/loans")
@CrossOrigin
public class LoanController {

    @Autowired
    private LoanApplicationService loanApplicationService;

    @ApiOperation("查询借款单列表")
    @GetMapping
    public MyJsonBean getLoanList(
            @RequestParam(required = false) String loanNumber,
            @RequestParam(required = false) String applicant,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String loanType,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String applicantDeptId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        try {
            Map<String, Object> param = new java.util.HashMap<>();
            param.put("loanCode", loanNumber);
            param.put("applicantName", applicant);
            param.put("loanStatus", status);
            param.put("loanType", loanType);
            param.put("startDate", startDate);
            param.put("endDate", endDate);
            param.put("applicantDeptId", applicantDeptId);
            param.put("pageNum", pageNum);
            param.put("pageSize", pageSize);

            return loanApplicationService.getList(param);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @ApiOperation("根据ID获取借款单详情")
    @GetMapping("/{loanId}")
    public MyJsonBean getLoanDetail(@PathVariable String loanId) {
        try {
            return loanApplicationService.getById(loanId);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @ApiOperation("根据借款单号查询")
    @GetMapping("/code/{loanCode}")
    public MyJsonBean getLoanByCode(@PathVariable String loanCode) {
        try {
            return loanApplicationService.getByLoanCode(loanCode);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @ApiOperation("保存借款单")
    @PostMapping
    public MyJsonBean saveLoan(@RequestBody TblLoanApplication loan) {
        try {
            return loanApplicationService.saveOrUpdate(loan);
        } catch (Exception e) {
            return MyJsonBean.errorData("保存失败：" + e.getMessage());
        }
    }

    @ApiOperation("删除借款单")
    @DeleteMapping("/{loanId}")
    public MyJsonBean deleteLoan(@PathVariable String loanId) {
        try {
            return loanApplicationService.delete(loanId);
        } catch (Exception e) {
            return MyJsonBean.errorData("删除失败：" + e.getMessage());
        }
    }

    @ApiOperation("批量删除借款单")
    @DeleteMapping("/batch")
    public MyJsonBean batchDeleteLoans(@RequestBody List<String> loanIds) {
        try {
            return loanApplicationService.batchDelete(loanIds);
        } catch (Exception e) {
            return MyJsonBean.errorData("批量删除失败：" + e.getMessage());
        }
    }

    @ApiOperation("提交借款单")
    @PostMapping("/{loanId}/submit")
    public MyJsonBean submitLoan(@PathVariable String loanId) {
        try {
            return loanApplicationService.submit(loanId);
        } catch (Exception e) {
            return MyJsonBean.errorData("提交失败：" + e.getMessage());
        }
    }

    @ApiOperation("审批借款单")
    @PostMapping("/{loanId}/approve")
    public MyJsonBean approveLoan(@PathVariable String loanId,
                                  @RequestBody Map<String, String> approvalData) {
        try {
            String action = approvalData.get("action");
            String opinion = approvalData.get("opinion");
            return loanApplicationService.approve(loanId, action, opinion);
        } catch (Exception e) {
            return MyJsonBean.errorData("审批失败：" + e.getMessage());
        }
    }

    @ApiOperation("放款")
    @PostMapping("/{loanId}/disburse")
    public MyJsonBean disburseLoan(@PathVariable String loanId,
                                   @RequestBody Map<String, Object> disburseData) {
        try {
            BigDecimal disburseAmount = disburseData.get("disburseAmount") != null ?
                new BigDecimal(disburseData.get("disburseAmount").toString()) : null;
            String disburseMethod = (String) disburseData.get("disburseMethod");
            String bankAccount = (String) disburseData.get("bankAccount");
            String disburseRemark = (String) disburseData.get("disburseRemark");
            return loanApplicationService.disburse(loanId, disburseAmount, disburseMethod, bankAccount, disburseRemark);
        } catch (Exception e) {
            return MyJsonBean.errorData("放款失败：" + e.getMessage());
        }
    }

    @ApiOperation("还款")
    @PostMapping("/{loanId}/repay")
    public MyJsonBean repayLoan(@PathVariable String loanId,
                               @RequestBody Map<String, Object> repayData) {
        try {
            BigDecimal repayAmount = repayData.get("repayAmount") != null ?
                new BigDecimal(repayData.get("repayAmount").toString()) : BigDecimal.ZERO;
            String repayMethod = (String) repayData.get("repayMethod");
            String repayVoucher = (String) repayData.get("repayVoucher");
            String repayRemark = (String) repayData.get("repayRemark");
            String expenseReportId = (String) repayData.get("expenseReportId");
            BigDecimal offsetAmount = repayData.get("offsetAmount") != null ?
                new BigDecimal(repayData.get("offsetAmount").toString()) : BigDecimal.ZERO;
            return loanApplicationService.repay(loanId, repayAmount, repayMethod, repayVoucher,
                repayRemark, expenseReportId, offsetAmount);
        } catch (Exception e) {
            return MyJsonBean.errorData("还款失败：" + e.getMessage());
        }
    }

    @ApiOperation("撤回借款单")
    @PostMapping("/{loanId}/withdraw")
    public MyJsonBean withdrawLoan(@PathVariable String loanId) {
        try {
            return loanApplicationService.withdraw(loanId);
        } catch (Exception e) {
            return MyJsonBean.errorData("撤回失败：" + e.getMessage());
        }
    }

    @ApiOperation("导出借款单")
    @GetMapping("/export")
    public MyJsonBean exportLoans(
            @RequestParam(required = false) String loanNumber,
            @RequestParam(required = false) String applicant,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String loanType,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("loanCode", loanNumber);
            param.put("applicantName", applicant);
            param.put("loanStatus", status);
            param.put("loanType", loanType);
            param.put("startDate", startDate);
            param.put("endDate", endDate);
            return loanApplicationService.export(param);
        } catch (Exception e) {
            return MyJsonBean.errorData("导出失败：" + e.getMessage());
        }
    }

    @ApiOperation("获取借款统计")
    @GetMapping("/statistics")
    public MyJsonBean getLoanStatistics(
            @RequestParam(required = false) String applicantDeptId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("applicantDeptId", applicantDeptId);
            param.put("startDate", startDate);
            param.put("endDate", endDate);
            return loanApplicationService.getStatistics(param);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }
}
