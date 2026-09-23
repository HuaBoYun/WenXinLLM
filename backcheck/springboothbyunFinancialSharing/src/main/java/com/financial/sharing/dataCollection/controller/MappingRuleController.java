package com.financial.sharing.dataCollection.controller;

import com.financial.sharing.util.UserUtils;

import com.financial.sharing.dataCollection.dto.MappingRuleQueryParam;
import com.financial.sharing.dataCollection.entity.TblMappingRule;
import com.financial.sharing.dataCollection.service.MappingRuleService;
import com.financial.sharing.util.MyJsonBean;
import com.hbfk.util.user.UserProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 映射规则Controller (OpenAPI)
 *
 * @author Augment Agent
 * @date 2026-02-02
 */
@Api(tags = "映射规则管理API")
@RestController
@RequestMapping("/financialSharing/mappingRule/api")
public class MappingRuleController {

    @Autowired
    private MappingRuleService mappingRuleService;

    @ApiOperation(value = "分页查询映射规则")
    @PostMapping("/queryPage")
    public MyJsonBean queryPage(@RequestBody MappingRuleQueryParam param) {
        try {
            String orgId = UserUtils.getOrgId();
            return mappingRuleService.queryPage(param, orgId);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "根据ID查询映射规则")
    @PostMapping("/queryById")
    public MyJsonBean queryById(@RequestBody Map<String, String> params) {
        try {
            String ruleId = params.get("ruleId");
            String orgId = UserUtils.getOrgId();
            return mappingRuleService.queryById(ruleId, orgId);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "保存映射规则")
    @PostMapping("/saveMappingRule")
    public MyJsonBean saveMappingRule(@RequestBody TblMappingRule mappingRule) {
        try {
            String orgId = UserUtils.getOrgId();
            String userId = UserUtils.getUserId();
            return mappingRuleService.saveMappingRule(mappingRule, orgId, userId);
        } catch (Exception e) {
            return MyJsonBean.errorData("保存失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "删除映射规则")
    @PostMapping("/deleteMappingRule")
    public MyJsonBean deleteMappingRule(@RequestBody Map<String, String> params) {
        try {
            String ruleId = params.get("ruleId");
            String orgId = UserUtils.getOrgId();
            return mappingRuleService.deleteMappingRule(ruleId, orgId);
        } catch (Exception e) {
            return MyJsonBean.errorData("删除失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "启用/禁用映射规则")
    @PostMapping("/toggleEnabled")
    public MyJsonBean toggleEnabled(@RequestBody Map<String, String> params) {
        try {
            String ruleId = params.get("ruleId");
            String isEnabled = params.get("isEnabled");
            String orgId = UserUtils.getOrgId();
            String userId = UserUtils.getUserId();
            return mappingRuleService.toggleEnabled(ruleId, isEnabled, orgId, userId);
        } catch (Exception e) {
            return MyJsonBean.errorData("操作失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "复制映射规则")
    @PostMapping("/copyMappingRule")
    public MyJsonBean copyMappingRule(@RequestBody Map<String, String> params) {
        try {
            String ruleId = params.get("ruleId");
            String orgId = UserUtils.getOrgId();
            String userId = UserUtils.getUserId();
            return mappingRuleService.copyMappingRule(ruleId, orgId, userId);
        } catch (Exception e) {
            return MyJsonBean.errorData("复制失败：" + e.getMessage());
        }
    }

    @ApiOperation(value = "更新排序号")
    @PostMapping("/updateSortNo")
    public MyJsonBean updateSortNo(@RequestBody Map<String, Object> params) {
        try {
            String ruleId = (String) params.get("ruleId");
            Integer sortNo = (Integer) params.get("sortNo");
            String orgId = UserUtils.getOrgId();
            String userId = UserUtils.getUserId();
            return mappingRuleService.updateSortNo(ruleId, sortNo, orgId, userId);
        } catch (Exception e) {
            return MyJsonBean.errorData("更新失败：" + e.getMessage());
        }
    }
}

