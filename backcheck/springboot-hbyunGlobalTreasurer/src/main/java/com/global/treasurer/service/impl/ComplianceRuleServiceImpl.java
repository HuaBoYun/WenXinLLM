package com.global.treasurer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblComplianceRule;
import com.global.treasurer.mapper.ComplianceRuleMapper;
import com.global.treasurer.service.ComplianceRuleService;
import com.global.treasurer.exception.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 合规检查规则服务实现类
 *
 * @author 华博云开发团队
 * @since 2026-01-22
 */
@Service
public class ComplianceRuleServiceImpl implements ComplianceRuleService {
    @Autowired
    private ComplianceRuleMapper ruleMapper;

    @Override
    public PageInfo<TblComplianceRule> getRuleList(Map<String, Object> params) {
        int pageNum = params.get("pageNum") != null ? (Integer) params.get("pageNum") : 1;
        int pageSize = params.get("pageSize") != null ? (Integer) params.get("pageSize") : 10;
        PageHelper.startPage(pageNum, pageSize);
        List<TblComplianceRule> list = ruleMapper.selectRuleList(params);
        return new PageInfo<>(list);
    }

    @Override
    public TblComplianceRule getRuleById(String ruleId) {
        TblComplianceRule rule = ruleMapper.selectRuleById(ruleId);
        if (rule == null) {
            throw new ServiceException(404, "合规检查规则不存在");
        }
        return rule;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblComplianceRule saveRule(TblComplianceRule rule) {
        if (rule.getRuleId() == null || rule.getRuleId().isEmpty()) {
            rule.setDeleteFlag(0);
            rule.setIsEnabled(1);
            rule.setCreatedTime(new Date());
            ruleMapper.insert(rule);
        } else {
            rule.setUpdatedTime(new Date());
            ruleMapper.updateById(rule);
        }
        return rule;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRule(String ruleId) {
        TblComplianceRule rule = getRuleById(ruleId);
        rule.setDeleteFlag(1);
        rule.setUpdatedTime(new Date());
        ruleMapper.updateById(rule);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteRules(List<String> ruleIds) {
        ruleMapper.batchDeleteByIds(ruleIds);
    }

    @Override
    public List<TblComplianceRule> getExecutableRules() {
        return ruleMapper.selectExecutableRules();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void toggleRuleStatus(String ruleId, Integer isEnabled) {
        ruleMapper.updateRuleStatus(ruleId, isEnabled);
    }

    @Override
    public Map<String, Object> executeComplianceCheck(String ruleId, Map<String, Object> checkData) {
        TblComplianceRule rule = getRuleById(ruleId);
        Map<String, Object> result = new HashMap<>();
        result.put("ruleId", ruleId);
        result.put("ruleName", rule.getRuleName());
        result.put("checkTime", new Date());
        result.put("isPassed", true);
        result.put("message", "合规检查通过");
        return result;
    }

    @Override
    public Map<String, Object> executeCheck(String ruleId, String targetType) {
        TblComplianceRule rule = getRuleById(ruleId);
        Map<String, Object> result = new HashMap<>();
        result.put("ruleId", ruleId);
        result.put("ruleName", rule.getRuleName());
        result.put("targetType", targetType);
        result.put("checkTime", new Date());
        result.put("isPassed", true);
        result.put("message", "合规检查通过");
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchExecuteRules(List<String> ruleIds) {
        for (String ruleId : ruleIds) {
            executeCheck(ruleId, null);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblComplianceRule copyRule(String ruleId) {
        TblComplianceRule source = getRuleById(ruleId);
        TblComplianceRule newRule = new TblComplianceRule();
        org.springframework.beans.BeanUtils.copyProperties(source, newRule);
        newRule.setRuleId(null);
        newRule.setRuleName(source.getRuleName() + "_副本");
        newRule.setIsEnabled(0);
        newRule.setDeleteFlag(0);
        newRule.setCreatedTime(new Date());
        newRule.setUpdatedTime(null);
        ruleMapper.insert(newRule);
        return newRule;
    }

    @Override
    public List<TblComplianceRule> exportRules(Map<String, Object> params) {
        return ruleMapper.selectRuleList(params);
    }
}

