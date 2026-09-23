package com.financial.sharing.controller;

import com.financial.sharing.business.entity.TblBudget;
import com.financial.sharing.business.service.BudgetService;
import com.financial.sharing.util.MyJsonBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 费用预算控制器
 *
 * @author Financial Sharing System
 * @since 2026-03-02
 */
@Api(tags = "费用预算管理")
@RestController
@RequestMapping("/budget")
@CrossOrigin
public class BudgetController {

    @Autowired
    private BudgetService budgetService;

    @ApiOperation("查询预算列表")
    @GetMapping
    public MyJsonBean getBudgetList(
            @RequestParam(required = false) String budgetName,
            @RequestParam(required = false) String budgetType,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String departmentId,
            @RequestParam(required = false) String budgetYear,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        try {
            Map<String, Object> param = new java.util.HashMap<>();
            param.put("budgetName", budgetName);
            param.put("budgetType", budgetType);
            param.put("status", status);
            param.put("departmentId", departmentId);
            param.put("budgetYear", budgetYear);
            param.put("startDate", startDate);
            param.put("endDate", endDate);
            param.put("pageNum", pageNum);
            param.put("pageSize", pageSize);

            return budgetService.getList(param);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @ApiOperation("根据ID获取预算详情")
    @GetMapping("/{budgetId}")
    public MyJsonBean getBudgetDetail(@PathVariable String budgetId) {
        try {
            return budgetService.getById(budgetId);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @ApiOperation("根据预算编号查询")
    @GetMapping("/code/{budgetCode}")
    public MyJsonBean getBudgetByCode(@PathVariable String budgetCode) {
        try {
            return budgetService.getByBudgetCode(budgetCode);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @ApiOperation("保存预算")
    @PostMapping
    public MyJsonBean saveBudget(@RequestBody TblBudget budget) {
        try {
            return budgetService.saveOrUpdate(budget);
        } catch (Exception e) {
            return MyJsonBean.errorData("保存失败：" + e.getMessage());
        }
    }

    @ApiOperation("删除预算")
    @DeleteMapping("/{budgetId}")
    public MyJsonBean deleteBudget(@PathVariable String budgetId) {
        try {
            return budgetService.delete(budgetId);
        } catch (Exception e) {
            return MyJsonBean.errorData("删除失败：" + e.getMessage());
        }
    }

    @ApiOperation("批量删除预算")
    @DeleteMapping("/batch")
    public MyJsonBean batchDeleteBudgets(@RequestBody List<String> budgetIds) {
        try {
            return budgetService.batchDelete(budgetIds);
        } catch (Exception e) {
            return MyJsonBean.errorData("批量删除失败：" + e.getMessage());
        }
    }

    @ApiOperation("提交预算")
    @PostMapping("/{budgetId}/submit")
    public MyJsonBean submitBudget(@PathVariable String budgetId) {
        try {
            return budgetService.submit(budgetId);
        } catch (Exception e) {
            return MyJsonBean.errorData("提交失败：" + e.getMessage());
        }
    }

    @ApiOperation("审批预算")
    @PostMapping("/{budgetId}/approve")
    public MyJsonBean approveBudget(@PathVariable String budgetId,
                                   @RequestBody Map<String, String> approvalData) {
        try {
            String action = approvalData.get("action");
            String opinion = approvalData.get("opinion");
            return budgetService.approve(budgetId, action, opinion);
        } catch (Exception e) {
            return MyJsonBean.errorData("审批失败：" + e.getMessage());
        }
    }

    @ApiOperation("预算控制检查")
    @PostMapping("/control-check")
    public MyJsonBean checkBudgetControl(@RequestBody Map<String, Object> controlData) {
        try {
            String businessType = (String) controlData.get("businessType");
            String businessId = (String) controlData.get("businessId");
            String departmentId = (String) controlData.get("departmentId");
            String projectId = (String) controlData.get("projectId");
            BigDecimal amount = controlData.get("amount") != null ?
                new BigDecimal(controlData.get("amount").toString()) : BigDecimal.ZERO;
            String currency = (String) controlData.get("currency");

            Map<String, Object> result = budgetService.checkBudgetControl(
                businessType, businessId, departmentId, projectId, amount, currency);
            return MyJsonBean.successData("预算控制检查完成", result);
        } catch (Exception e) {
            return MyJsonBean.errorData("预算控制检查失败：" + e.getMessage());
        }
    }

    @ApiOperation("预算占用")
    @PostMapping("/occupy")
    public MyJsonBean occupyBudget(@RequestBody Map<String, Object> occupyData) {
        try {
            String businessType = (String) occupyData.get("businessType");
            String businessId = (String) occupyData.get("businessId");
            String departmentId = (String) occupyData.get("departmentId");
            String projectId = (String) occupyData.get("projectId");
            BigDecimal amount = occupyData.get("amount") != null ?
                new BigDecimal(occupyData.get("amount").toString()) : BigDecimal.ZERO;
            String currency = (String) occupyData.get("currency");

            return budgetService.occupy(businessType, businessId, departmentId, projectId, amount, currency);
        } catch (Exception e) {
            return MyJsonBean.errorData("预算占用失败：" + e.getMessage());
        }
    }

    @ApiOperation("预算释放")
    @PostMapping("/release")
    public MyJsonBean releaseBudget(@RequestBody Map<String, Object> releaseData) {
        try {
            String businessType = (String) releaseData.get("businessType");
            String businessId = (String) releaseData.get("businessId");

            return budgetService.release(businessType, businessId);
        } catch (Exception e) {
            return MyJsonBean.errorData("预算释放失败：" + e.getMessage());
        }
    }

    @ApiOperation("预算统计")
    @GetMapping("/statistics")
    public MyJsonBean getBudgetStatistics(
            @RequestParam(required = false) String departmentId,
            @RequestParam(required = false) String budgetYear) {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("departmentId", departmentId);
            param.put("budgetYear", budgetYear);
            return budgetService.getStatistics(param);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @ApiOperation("导出预算")
    @GetMapping("/export")
    public MyJsonBean exportBudget(
            @RequestParam(required = false) String budgetName,
            @RequestParam(required = false) String budgetType,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String departmentId,
            @RequestParam(required = false) String budgetYear) {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("budgetName", budgetName);
            param.put("budgetType", budgetType);
            param.put("status", status);
            param.put("departmentId", departmentId);
            param.put("budgetYear", budgetYear);
            return budgetService.export(param);
        } catch (Exception e) {
            return MyJsonBean.errorData("导出失败：" + e.getMessage());
        }
    }
}
