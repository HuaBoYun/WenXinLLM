package com.financial.sharing.budgetControl.controller;

import com.financial.sharing.util.UserUtils;

import com.financial.sharing.budgetControl.dto.ControlAnalysisQueryParam;
import com.financial.sharing.budgetControl.service.ControlAnalysisService;
import com.financial.sharing.util.MyJsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 控制分析Controller (OpenAPI)
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Api(tags = "控制分析API")
@RestController
@RequestMapping("/financialSharing/controlAnalysis/api")
public class ControlAnalysisController {

    @Autowired
    private ControlAnalysisService controlAnalysisService;

    @ApiOperation(value = "获取控制效果分析")
    @PostMapping("/getControlEffectAnalysis")
    public MyJsonBean getControlEffectAnalysis(@RequestBody ControlAnalysisQueryParam param) {
        try {
            param.setOrgId(UserUtils.getOrgId());
            return controlAnalysisService.getControlEffectAnalysis(param);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "获取预算使用趋势分析")
    @PostMapping("/getBudgetUsageTrend")
    public MyJsonBean getBudgetUsageTrend(@RequestBody ControlAnalysisQueryParam param) {
        try {
            param.setOrgId(UserUtils.getOrgId());
            return controlAnalysisService.getBudgetUsageTrend(param);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "获取异常控制分析")
    @PostMapping("/getAbnormalControlAnalysis")
    public MyJsonBean getAbnormalControlAnalysis(@RequestBody ControlAnalysisQueryParam param) {
        try {
            param.setOrgId(UserUtils.getOrgId());
            return controlAnalysisService.getAbnormalControlAnalysis(param);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "获取控制结果分布")
    @PostMapping("/getControlResultDistribution")
    public MyJsonBean getControlResultDistribution(@RequestBody ControlAnalysisQueryParam param) {
        try {
            param.setOrgId(UserUtils.getOrgId());
            return controlAnalysisService.getControlResultDistribution(param);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "获取组织控制排名")
    @PostMapping("/getOrgControlRanking")
    public MyJsonBean getOrgControlRanking(@RequestBody ControlAnalysisQueryParam param) {
        try {
            param.setOrgId(UserUtils.getOrgId());
            return controlAnalysisService.getOrgControlRanking(param);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "获取科目控制排名")
    @PostMapping("/getSubjectControlRanking")
    public MyJsonBean getSubjectControlRanking(@RequestBody ControlAnalysisQueryParam param) {
        try {
            param.setOrgId(UserUtils.getOrgId());
            return controlAnalysisService.getSubjectControlRanking(param);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }
}

