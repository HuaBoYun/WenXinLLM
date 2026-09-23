package com.financial.sharing.budgetPlanning.controller;

import com.financial.sharing.util.UserUtils;

import com.financial.sharing.budgetPlanning.dto.BudgetRuleQueryParam;
import com.financial.sharing.budgetPlanning.entity.TblBudgetRule;
import com.financial.sharing.budgetPlanning.service.BudgetRuleService;
import com.github.pagehelper.PageInfo;
import com.financial.sharing.util.MyJsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 业务规则Controller
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Api(tags = "计划预算-业务规则管理")
@RestController
@RequestMapping("/budgetPlanning/budgetRule")
public class BudgetRuleController {

    @Autowired
    private BudgetRuleService ruleService;

    @ApiOperation("查询业务规则列表(分页)")
    @PostMapping("/getRuleList")
    public MyJsonBean getRuleList(@RequestBody BudgetRuleQueryParam param) {
        // 未登录时返回空分页结果，避免前端列表渲染异常
        if (UserUtils.getUser() == null) {
            return MyJsonBean.ok("查询成功", new PageInfo<>(java.util.Collections.emptyList()));
        }
        try {
            PageInfo<TblBudgetRule> pageInfo = ruleService.getRuleList(param);
            return MyJsonBean.ok("查询成功", pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("查询业务规则列表(不分页)")
    @PostMapping("/getRuleListNoPage")
    public MyJsonBean getRuleListNoPage(@RequestBody BudgetRuleQueryParam param) {
        // 未登录时返回空列表，避免前端下拉/表格渲染异常
        if (UserUtils.getUser() == null) {
            return MyJsonBean.ok("查询成功", java.util.Collections.emptyList());
        }
        try {
            List<TblBudgetRule> list = ruleService.getRuleListNoPage(param);
            return MyJsonBean.ok("查询成功", list);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("根据ID查询业务规则")
    @PostMapping("/getRuleById")
    public MyJsonBean getRuleById(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            String ruleId = params.get("ruleId");
            if (ruleId == null || ruleId.isEmpty()) {
                return MyJsonBean.errorData("规则ID不能为空");
            }
            TblBudgetRule rule = ruleService.getRuleById(ruleId);
            return MyJsonBean.ok("查询成功", rule);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("新增业务规则")
    @PostMapping("/addRule")
    public MyJsonBean addRule(@RequestBody TblBudgetRule rule) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            ruleService.addRule(rule);
            return MyJsonBean.ok("新增成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("新增失败: " + e.getMessage());
        }
    }

    @ApiOperation("修改业务规则")
    @PostMapping("/updateRule")
    public MyJsonBean updateRule(@RequestBody TblBudgetRule rule) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            ruleService.updateRule(rule);
            return MyJsonBean.ok("修改成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("修改失败: " + e.getMessage());
        }
    }

    @ApiOperation("删除业务规则")
    @PostMapping("/deleteRule")
    public MyJsonBean deleteRule(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            String ruleId = params.get("ruleId");
            if (ruleId == null || ruleId.isEmpty()) {
                return MyJsonBean.errorData("规则ID不能为空");
            }
            ruleService.deleteRule(ruleId);
            return MyJsonBean.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    @ApiOperation("批量删除业务规则")
    @PostMapping("/batchDeleteRule")
    public MyJsonBean batchDeleteRule(@RequestBody Map<String, List<String>> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            List<String> ruleIds = params.get("ruleIds");
            if (ruleIds == null || ruleIds.isEmpty()) {
                return MyJsonBean.errorData("规则ID列表不能为空");
            }
            ruleService.batchDeleteRule(ruleIds);
            return MyJsonBean.ok("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    @ApiOperation("复制业务规则")
    @PostMapping("/copyRule")
    public MyJsonBean copyRule(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            String ruleId = params.get("ruleId");
            String newRuleCode = params.get("newRuleCode");
            String newRuleName = params.get("newRuleName");
            
            if (ruleId == null || ruleId.isEmpty()) {
                return MyJsonBean.errorData("源规则ID不能为空");
            }
            if (newRuleCode == null || newRuleCode.isEmpty()) {
                return MyJsonBean.errorData("新规则编码不能为空");
            }
            if (newRuleName == null || newRuleName.isEmpty()) {
                return MyJsonBean.errorData("新规则名称不能为空");
            }
            
            String newRuleId = ruleService.copyRule(ruleId, newRuleCode, newRuleName);
            return MyJsonBean.ok("复制成功", newRuleId);
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("复制失败: " + e.getMessage());
        }
    }

    @ApiOperation("启用业务规则")
    @PostMapping("/enableRule")
    public MyJsonBean enableRule(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            String ruleId = params.get("ruleId");
            if (ruleId == null || ruleId.isEmpty()) {
                return MyJsonBean.errorData("规则ID不能为空");
            }
            ruleService.enableRule(ruleId);
            return MyJsonBean.ok("启用成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("启用失败: " + e.getMessage());
        }
    }

    @ApiOperation("停用业务规则")
    @PostMapping("/disableRule")
    public MyJsonBean disableRule(@RequestBody Map<String, String> params) {
        if (UserUtils.getUser() == null) {
            return MyJsonBean.errorData("用户未登录");
        }
        try {
            String ruleId = params.get("ruleId");
            if (ruleId == null || ruleId.isEmpty()) {
                return MyJsonBean.errorData("规则ID不能为空");
            }
            ruleService.disableRule(ruleId);
            return MyJsonBean.ok("停用成功");
        } catch (Exception e) {
            e.printStackTrace();
            return MyJsonBean.errorData("停用失败: " + e.getMessage());
        }
    }
}

