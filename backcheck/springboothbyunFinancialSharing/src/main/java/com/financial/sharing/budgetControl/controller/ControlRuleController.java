package com.financial.sharing.budgetControl.controller;

import com.financial.sharing.util.UserUtils;

import com.financial.sharing.budgetControl.dto.ControlRuleQueryParam;
import com.financial.sharing.budgetControl.entity.TblControlRule;
import com.financial.sharing.budgetControl.service.ControlRuleService;
import com.financial.sharing.util.MyJsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 控制规则Controller
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Api(tags = "预算执行控制-控制规则管理")
@RestController
@RequestMapping("/financialSharing/budgetControl/controlRule")
public class ControlRuleController {

    @Autowired
    private ControlRuleService controlRuleService;

    @ApiOperation(value = "分页查询控制规则")
    @PostMapping("/queryPage")
    public MyJsonBean queryPage(@RequestBody ControlRuleQueryParam param) {
        try {
            param.setOrgId(UserUtils.getOrgId());
            return controlRuleService.queryPage(param);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "根据ID查询控制规则")
    @PostMapping("/queryById")
    public MyJsonBean queryById(@RequestParam String ruleId) {
        try {
            return controlRuleService.queryById(ruleId);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "新增控制规则")
    @PostMapping("/add")
    public MyJsonBean add(@RequestBody TblControlRule rule) {
        try {
            rule.setOrgId(UserUtils.getOrgId());
            return controlRuleService.add(rule);
        } catch (Exception e) {
            return MyJsonBean.errorData("新增失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "修改控制规则")
    @PostMapping("/modify")
    public MyJsonBean modify(@RequestBody TblControlRule rule) {
        try {
            return controlRuleService.modify(rule);
        } catch (Exception e) {
            return MyJsonBean.errorData("修改失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "删除控制规则")
    @PostMapping("/remove")
    public MyJsonBean remove(@RequestParam String ruleId) {
        try {
            return controlRuleService.remove(ruleId);
        } catch (Exception e) {
            return MyJsonBean.errorData("删除失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "启用/禁用控制规则")
    @PostMapping("/toggleStatus")
    public MyJsonBean toggleStatus(@RequestParam String ruleId, @RequestParam String isEnabled) {
        try {
            return controlRuleService.toggleStatus(ruleId, isEnabled);
        } catch (Exception e) {
            return MyJsonBean.errorData("操作失败：" + e.getMessage());
        }
    }
}

