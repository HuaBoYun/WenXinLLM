package com.financial.sharing.controller;

import com.financial.sharing.dto.TblBudgetControlRuleQueryParam;
import com.financial.sharing.dto.TblBudgetControlRuleSaveParam;
import com.financial.sharing.service.TblBudgetControlRuleService;
import com.financial.sharing.util.MyJsonBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 预算管控规则管理控制器
 *
 * @author Financial Sharing System
 * @since 2024-12-19
 */
@Slf4j
@Api(tags = "预算管控规则管理")
@RestController
@RequestMapping("/budget-control-rules")
@CrossOrigin
public class BudgetControlRuleController {

    @Autowired
    private TblBudgetControlRuleService budgetControlRuleService;

    @ApiOperation("查询预算管控规则列表")
    @GetMapping
    public MyJsonBean getBudgetControlRuleList(@RequestParam(required = false) String ruleName,
                                             @RequestParam(required = false) String ruleCode,
                                             @RequestParam(required = false) String controlType,
                                             @RequestParam(required = false) Integer isEnabled,
                                             @RequestParam(defaultValue = "1") Integer pageNo,
                                             @RequestParam(defaultValue = "10") Integer pageSize) {
        try {
            TblBudgetControlRuleQueryParam param = new TblBudgetControlRuleQueryParam();
            param.setRuleName(ruleName);
            param.setRuleCode(ruleCode);
            param.setControlType(controlType);
            param.setIsEnabled(isEnabled);
            param.setPageNo(pageNo);
            param.setPageSize(pageSize);

            return budgetControlRuleService.getList(param);
        } catch (Exception e) {
            log.error("查询预算管控规则列表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("保存预算管控规则")
    @PostMapping
    public MyJsonBean saveBudgetControlRule(@RequestBody TblBudgetControlRuleSaveParam param) {
        try {
            return budgetControlRuleService.saveOrUpdate(param);
        } catch (Exception e) {
            log.error("保存预算管控规则失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @ApiOperation("删除预算管控规则")
    @DeleteMapping("/{ruleId}")
    public MyJsonBean deleteBudgetControlRule(@PathVariable String ruleId) {
        try {
            return budgetControlRuleService.delete(ruleId);
        } catch (Exception e) {
            log.error("删除预算管控规则失败", e);
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取预算管控规则详情")
    @GetMapping("/{ruleId}")
    public MyJsonBean getBudgetControlRuleDetail(@PathVariable String ruleId) {
        try {
            return budgetControlRuleService.getById(ruleId);
        } catch (Exception e) {
            log.error("获取预算管控规则详情失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取预算管控规则适用范围")
    @GetMapping("/{ruleId}/scope")
    public MyJsonBean getBudgetControlRuleScope(@PathVariable String ruleId) {
        try {
            return budgetControlRuleService.getControlScopes(ruleId);
        } catch (Exception e) {
            log.error("获取预算管控规则适用范围失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("保存预算管控规则适用范围")
    @PostMapping("/{ruleId}/scope")
    public MyJsonBean saveBudgetControlRuleScope(@PathVariable String ruleId,
                                                @RequestBody List<Map<String, String>> scopes) {
        try {
            return budgetControlRuleService.saveControlScopes(ruleId, scopes);
        } catch (Exception e) {
            log.error("保存预算管控规则适用范围失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @ApiOperation("预算控制检查")
    @PostMapping("/{ruleId}/check")
    public MyJsonBean checkBudgetControl(@PathVariable String ruleId,
                                       @RequestBody Map<String, Object> checkData) {
        try {
            // 从请求体中提取参数
            BigDecimal amount = null;
            if (checkData.containsKey("amount")) {
                Object amountObj = checkData.get("amount");
                if (amountObj instanceof Number) {
                    amount = new BigDecimal(amountObj.toString());
                }
            }

            if (amount == null) {
                return MyJsonBean.errorData("金额参数不能为空");
            }

            return budgetControlRuleService.checkBudget(ruleId, amount, checkData);
        } catch (Exception e) {
            log.error("预算控制检查失败", e);
            return MyJsonBean.errorData("预算检查失败: " + e.getMessage());
        }
    }

    @ApiOperation("启用/禁用预算管控规则")
    @PutMapping("/{ruleId}/status")
    public MyJsonBean updateBudgetControlRuleStatus(@PathVariable String ruleId,
                                                  @RequestParam Integer isEnabled) {
        try {
            return budgetControlRuleService.updateStatus(ruleId, isEnabled);
        } catch (Exception e) {
            log.error("更新预算管控规则状态失败", e);
            return MyJsonBean.errorData("状态更新失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取预算执行记录")
    @GetMapping("/{ruleId}/executions")
    public MyJsonBean getBudgetExecutions(@PathVariable String ruleId) {
        try {
            return budgetControlRuleService.getExecutionRecords(ruleId);
        } catch (Exception e) {
            log.error("获取预算执行记录失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }
}
