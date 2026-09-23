package com.financial.sharing.controller;

import com.financial.sharing.business.entity.TblExpenseProvision;
import com.financial.sharing.business.service.ExpenseProvisionService;
import com.financial.sharing.util.MyJsonBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * 费用预提管理控制器
 *
 * @author Financial Sharing System
 * @since 2026-03-02
 */
@Api(tags = "费用预提管理")
@RestController
@RequestMapping("/expense/provisions")
@CrossOrigin
public class ProvisionController {

    @Autowired
    private ExpenseProvisionService expenseProvisionService;

    @ApiOperation("查询费用预提单列表")
    @GetMapping
    public MyJsonBean getProvisionList(
            @RequestParam(required = false) String provisionCode,
            @RequestParam(required = false) String applicantName,
            @RequestParam(required = false) String provisionStatus,
            @RequestParam(required = false) String provisionType,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String applicantDeptId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        try {
            Map<String, Object> param = new java.util.HashMap<>();
            param.put("provisionCode", provisionCode);
            param.put("applicantName", applicantName);
            param.put("provisionStatus", provisionStatus);
            param.put("provisionType", provisionType);
            param.put("startDate", startDate);
            param.put("endDate", endDate);
            param.put("applicantDeptId", applicantDeptId);
            param.put("pageNum", pageNum);
            param.put("pageSize", pageSize);

            return expenseProvisionService.getList(param);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @ApiOperation("根据ID获取费用预提单详情")
    @GetMapping("/{provisionId}")
    public MyJsonBean getProvisionDetail(@PathVariable String provisionId) {
        try {
            return expenseProvisionService.getById(provisionId);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @ApiOperation("根据预提单号查询")
    @GetMapping("/code/{provisionCode}")
    public MyJsonBean getProvisionByCode(@PathVariable String provisionCode) {
        try {
            return expenseProvisionService.getByProvisionCode(provisionCode);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @ApiOperation("保存费用预提单")
    @PostMapping
    public MyJsonBean saveProvision(@RequestBody TblExpenseProvision provision) {
        try {
            return expenseProvisionService.saveOrUpdate(provision);
        } catch (Exception e) {
            return MyJsonBean.errorData("保存失败：" + e.getMessage());
        }
    }

    @ApiOperation("删除费用预提单")
    @DeleteMapping("/{provisionId}")
    public MyJsonBean deleteProvision(@PathVariable String provisionId) {
        try {
            return expenseProvisionService.delete(provisionId);
        } catch (Exception e) {
            return MyJsonBean.errorData("删除失败：" + e.getMessage());
        }
    }

    @ApiOperation("批量删除费用预提单")
    @DeleteMapping("/batch")
    public MyJsonBean batchDeleteProvisions(@RequestBody List<String> provisionIds) {
        try {
            return expenseProvisionService.batchDelete(provisionIds);
        } catch (Exception e) {
            return MyJsonBean.errorData("批量删除失败：" + e.getMessage());
        }
    }

    @ApiOperation("提交费用预提单")
    @PostMapping("/{provisionId}/submit")
    public MyJsonBean submitProvision(@PathVariable String provisionId) {
        try {
            return expenseProvisionService.submit(provisionId);
        } catch (Exception e) {
            return MyJsonBean.errorData("提交失败：" + e.getMessage());
        }
    }

    @ApiOperation("审批费用预提单")
    @PostMapping("/{provisionId}/approve")
    public MyJsonBean approveProvision(@PathVariable String provisionId,
                                         @RequestBody Map<String, String> approvalData) {
        try {
            String action = approvalData.get("action");
            String opinion = approvalData.get("opinion");
            return expenseProvisionService.approve(provisionId, action, opinion);
        } catch (Exception e) {
            return MyJsonBean.errorData("审批失败：" + e.getMessage());
        }
    }

    @ApiOperation("撤回费用预提单")
    @PostMapping("/{provisionId}/withdraw")
    public MyJsonBean withdrawProvision(@PathVariable String provisionId) {
        try {
            return expenseProvisionService.withdraw(provisionId);
        } catch (Exception e) {
            return MyJsonBean.errorData("撤回失败：" + e.getMessage());
        }
    }

    @ApiOperation("冲销费用预提单")
    @PostMapping("/{provisionId}/reverse")
    public MyJsonBean reverseProvision(@PathVariable String provisionId,
                                      @RequestParam(required = false) String voucherNo) {
        try {
            return expenseProvisionService.reverse(provisionId, voucherNo);
        } catch (Exception e) {
            return MyJsonBean.errorData("冲销失败：" + e.getMessage());
        }
    }

    @ApiOperation("导出费用预提单")
    @GetMapping("/export")
    public MyJsonBean exportProvisions(
            @RequestParam(required = false) String provisionCode,
            @RequestParam(required = false) String applicantName,
            @RequestParam(required = false) String provisionStatus,
            @RequestParam(required = false) String provisionType,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("provisionCode", provisionCode);
            param.put("applicantName", applicantName);
            param.put("provisionStatus", provisionStatus);
            param.put("provisionType", provisionType);
            param.put("startDate", startDate);
            param.put("endDate", endDate);
            return expenseProvisionService.export(param);
        } catch (Exception e) {
            return MyJsonBean.errorData("导出失败：" + e.getMessage());
        }
    }

    @ApiOperation("获取费用预提统计")
    @GetMapping("/statistics")
    public MyJsonBean getProvisionStatistics(
            @RequestParam(required = false) String applicantDeptId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("applicantDeptId", applicantDeptId);
            param.put("startDate", startDate);
            param.put("endDate", endDate);
            return expenseProvisionService.getStatistics(param);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }
}
