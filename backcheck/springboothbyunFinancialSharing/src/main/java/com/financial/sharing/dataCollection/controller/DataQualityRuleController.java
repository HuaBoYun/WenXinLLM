package com.financial.sharing.dataCollection.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.financial.sharing.dataCollection.dto.DataQualityRuleQueryParam;
import com.financial.sharing.dataCollection.entity.TblDataQualityRule;
import com.financial.sharing.dataCollection.service.DataQualityRuleService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.UserUtils;
import com.hbfk.util.user.UserProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据质量规则Controller
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Slf4j
@RestController
@RequestMapping("/financialSharing/dataCollection/qualityRule")
public class DataQualityRuleController {

    @Autowired
    private DataQualityRuleService dataQualityRuleService;

    @Autowired
    private UserProvider userProvider;

    /**
     * 分页查询质量规则
     */
    @PostMapping("/queryPage")
    public MyJsonBean queryPage(@RequestBody DataQualityRuleQueryParam param) {
        try {
            Long orgId = UserUtils.getOrgIdAsLong();
            IPage<TblDataQualityRule> page = dataQualityRuleService.queryPage(param, orgId);
            return MyJsonBean.successData("查询成功", page);
        } catch (Exception e) {
            log.error("分页查询质量规则失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 根据ID查询质量规则
     */
    @PostMapping("/queryById")
    public MyJsonBean queryById(@RequestParam Long ruleId) {
        try {
            Long orgId = UserUtils.getOrgIdAsLong();
            TblDataQualityRule rule = dataQualityRuleService.queryById(ruleId, orgId);
            return MyJsonBean.successData("查询成功", rule);
        } catch (Exception e) {
            log.error("查询质量规则失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 保存质量规则
     */
    @PostMapping("/saveRule")
    public MyJsonBean saveRule(@RequestBody TblDataQualityRule rule) {
        try {
            Long orgId = UserUtils.getOrgIdAsLong();
            String userId = UserUtils.getUserId();
            boolean success = dataQualityRuleService.saveRule(rule, orgId, userId);
            return success ? MyJsonBean.successData("保存成功") : MyJsonBean.errorData("保存失败");
        } catch (Exception e) {
            log.error("保存质量规则失败", e);
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    /**
     * 删除质量规则
     */
    @PostMapping("/deleteRule")
    public MyJsonBean deleteRule(@RequestParam Long ruleId) {
        try {
            Long orgId = UserUtils.getOrgIdAsLong();
            boolean success = dataQualityRuleService.deleteRule(ruleId, orgId);
            return success ? MyJsonBean.successData("删除成功") : MyJsonBean.errorData("删除失败");
        } catch (Exception e) {
            log.error("删除质量规则失败", e);
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    /**
     * 批量删除质量规则
     */
    @PostMapping("/batchDeleteRule")
    public MyJsonBean batchDeleteRule(@RequestBody List<Long> ruleIds) {
        try {
            Long orgId = UserUtils.getOrgIdAsLong();
            int count = dataQualityRuleService.batchDeleteRule(ruleIds, orgId);
            return MyJsonBean.successData("删除成功，共删除" + count + "条记录", count);
        } catch (Exception e) {
            log.error("批量删除质量规则失败", e);
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    /**
     * 启用/禁用质量规则
     */
    @PostMapping("/toggleEnabled")
    public MyJsonBean toggleEnabled(@RequestParam Long ruleId, @RequestParam String isEnabled) {
        try {
            Long orgId = UserUtils.getOrgIdAsLong();
            String userId = UserUtils.getUserId();
            boolean success = dataQualityRuleService.toggleEnabled(ruleId, isEnabled, orgId, userId);
            return success ? MyJsonBean.successData("操作成功") : MyJsonBean.errorData("操作失败");
        } catch (Exception e) {
            log.error("启用/禁用质量规则失败", e);
            return MyJsonBean.errorData("操作失败: " + e.getMessage());
        }
    }

    /**
     * 查询所有启用的规则
     */
    @PostMapping("/queryEnabledRules")
    public MyJsonBean queryEnabledRules() {
        try {
            Long orgId = UserUtils.getOrgIdAsLong();
            List<TblDataQualityRule> rules = dataQualityRuleService.queryEnabledRules(orgId);
            return MyJsonBean.successData("查询成功", rules);
        } catch (Exception e) {
            log.error("查询启用的规则失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }
}

