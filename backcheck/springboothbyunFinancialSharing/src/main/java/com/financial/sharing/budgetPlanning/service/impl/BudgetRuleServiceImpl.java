package com.financial.sharing.budgetPlanning.service.impl;

import com.financial.sharing.util.UserUtils;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.financial.sharing.budgetPlanning.dto.BudgetRuleQueryParam;
import com.financial.sharing.budgetPlanning.entity.TblBudgetRule;
import com.financial.sharing.budgetPlanning.mapper.BudgetRuleMapper;
import com.financial.sharing.budgetPlanning.service.BudgetRuleService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hbfk.util.user.UserProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 业务规则Service实现类
 * 
 * @author hbyun
 * @date 2026-01-30
 */
@Service
public class BudgetRuleServiceImpl implements BudgetRuleService {

    @Autowired
    private BudgetRuleMapper ruleMapper;

    @Override
    public PageInfo<TblBudgetRule> getRuleList(BudgetRuleQueryParam param) {
        PageHelper.startPage(param.getPageNum(), param.getPageSize());
        List<TblBudgetRule> list = ruleMapper.selectRuleList(param);
        return new PageInfo<>(list);
    }

    @Override
    public List<TblBudgetRule> getRuleListNoPage(BudgetRuleQueryParam param) {
        return ruleMapper.selectRuleList(param);
    }

    @Override
    public TblBudgetRule getRuleById(String ruleId) {
        return ruleMapper.selectById(ruleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void addRule(TblBudgetRule rule) {
        // 检查规则编码是否存在
        int count = ruleMapper.checkRuleCodeExists(rule.getRuleCode(), null);
        if (count > 0) {
            throw new RuntimeException("规则编码已存在");
        }

        String orgId = UserUtils.requireOrgId();
        String userId = UserUtils.getUser().getStaffid().toString();
        Date now = new Date();

        rule.setRuleId(UUID.randomUUID().toString().replace("-", ""));
        rule.setStatus("DRAFT");
        rule.setOrgId(orgId);
        rule.setCreateUser(userId);
        rule.setCreateTime(now);
        rule.setUpdateUser(userId);
        rule.setUpdateTime(now);

        ruleMapper.insert(rule);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRule(TblBudgetRule rule) {
        // 检查规则编码是否存在
        int count = ruleMapper.checkRuleCodeExists(rule.getRuleCode(), rule.getRuleId());
        if (count > 0) {
            throw new RuntimeException("规则编码已存在");
        }

        String userId = UserUtils.getUser().getStaffid().toString();
        Date now = new Date();

        rule.setUpdateUser(userId);
        rule.setUpdateTime(now);

        ruleMapper.updateById(rule);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRule(String ruleId) {
        // TODO: 检查是否有关联数据
        ruleMapper.deleteById(ruleId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteRule(List<String> ruleIds) {
        for (String ruleId : ruleIds) {
            deleteRule(ruleId);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String copyRule(String ruleId, String newRuleCode, String newRuleName) {
        // 查询源规则
        TblBudgetRule sourceRule = ruleMapper.selectById(ruleId);
        if (sourceRule == null) {
            throw new RuntimeException("源规则不存在");
        }

        // 检查新规则编码是否存在
        int count = ruleMapper.checkRuleCodeExists(newRuleCode, null);
        if (count > 0) {
            throw new RuntimeException("新规则编码已存在");
        }

        String orgId = UserUtils.requireOrgId();
        String userId = UserUtils.getUser().getStaffid().toString();
        Date now = new Date();

        // 创建新规则
        TblBudgetRule newRule = new TblBudgetRule();
        newRule.setRuleId(UUID.randomUUID().toString().replace("-", ""));
        newRule.setModelId(sourceRule.getModelId());
        newRule.setRuleCode(newRuleCode);
        newRule.setRuleName(newRuleName);
        newRule.setRuleType(sourceRule.getRuleType());
        newRule.setRuleExpression(sourceRule.getRuleExpression());
        newRule.setPriority(sourceRule.getPriority());
        newRule.setStatus("DRAFT");
        newRule.setDescription("复制自: " + sourceRule.getRuleName());
        newRule.setSortOrder(sourceRule.getSortOrder());
        newRule.setOrgId(orgId);
        newRule.setCreateUser(userId);
        newRule.setCreateTime(now);
        newRule.setUpdateUser(userId);
        newRule.setUpdateTime(now);

        ruleMapper.insert(newRule);

        return newRule.getRuleId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void enableRule(String ruleId) {
        TblBudgetRule rule = ruleMapper.selectById(ruleId);
        if (rule == null) {
            throw new RuntimeException("规则不存在");
        }

        rule.setStatus("ACTIVE");
        rule.setUpdateUser(UserUtils.getUser().getStaffid().toString());
        rule.setUpdateTime(new Date());

        ruleMapper.updateById(rule);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disableRule(String ruleId) {
        TblBudgetRule rule = ruleMapper.selectById(ruleId);
        if (rule == null) {
            throw new RuntimeException("规则不存在");
        }

        rule.setStatus("INACTIVE");
        rule.setUpdateUser(UserUtils.getUser().getStaffid().toString());
        rule.setUpdateTime(new Date());

        ruleMapper.updateById(rule);
    }
}

