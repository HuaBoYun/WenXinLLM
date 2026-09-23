package com.financial.sharing.budgetControl.controller;

import com.financial.sharing.util.UserUtils;

import com.financial.sharing.budgetControl.dto.ControlStrategyConfigDTO;
import com.financial.sharing.budgetControl.dto.ControlStrategyQueryParam;
import com.financial.sharing.budgetControl.service.ControlStrategyService;
import com.financial.sharing.util.MyJsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 控制策略配置Controller
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Api(tags = "预算执行控制-控制策略配置")
@RestController
@RequestMapping("/financialSharing/budgetControl/controlStrategy")
public class ControlStrategyController {

    @Autowired
    private ControlStrategyService controlStrategyService;

    @ApiOperation(value = "分页查询控制策略")
    @PostMapping("/queryPage")
    public MyJsonBean queryPage(@RequestBody ControlStrategyQueryParam param) {
        try {
            param.setOrgId(UserUtils.getOrgId());
            return controlStrategyService.queryPage(param);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "根据规则ID查询控制策略")
    @PostMapping("/queryByRuleId")
    public MyJsonBean queryByRuleId(@RequestParam String ruleId) {
        try {
            return controlStrategyService.queryByRuleId(ruleId);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "保存控制策略配置")
    @PostMapping("/saveStrategy")
    public MyJsonBean saveStrategy(@RequestBody ControlStrategyConfigDTO config) {
        try {
            if (config.getOrgId() == null) {
                config.setOrgId(UserUtils.getOrgId());
            }
            return controlStrategyService.saveStrategy(config);
        } catch (Exception e) {
            return MyJsonBean.errorData("保存失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "测试控制策略")
    @PostMapping("/testStrategy")
    public MyJsonBean testStrategy(@RequestBody ControlStrategyConfigDTO config,
                                   @RequestParam String testAmount,
                                   @RequestParam String testOrgId,
                                   @RequestParam String testSubjectCode,
                                   @RequestParam String testPeriod) {
        try {
            return controlStrategyService.testStrategy(config, testAmount, 
                testOrgId, testSubjectCode, testPeriod);
        } catch (Exception e) {
            return MyJsonBean.errorData("测试失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "复制控制策略")
    @PostMapping("/copyStrategy")
    public MyJsonBean copyStrategy(@RequestParam String ruleId,
                                   @RequestParam String newRuleCode,
                                   @RequestParam String newRuleName) {
        try {
            return controlStrategyService.copyStrategy(ruleId, newRuleCode, newRuleName);
        } catch (Exception e) {
            return MyJsonBean.errorData("复制失败：" + e.getMessage());
        }
    }
}

