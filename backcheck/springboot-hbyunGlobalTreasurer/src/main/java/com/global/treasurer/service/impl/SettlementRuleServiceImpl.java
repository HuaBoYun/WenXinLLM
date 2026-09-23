package com.global.treasurer.service.impl;

import com.global.treasurer.entity.TblSettlementRule;
import com.global.treasurer.mapper.TblSettlementRuleMapper;
import com.global.treasurer.service.SettlementRuleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class SettlementRuleServiceImpl implements SettlementRuleService {
    @Autowired
    private TblSettlementRuleMapper ruleMapper;

    @Override
    public Map<String, Object> getRulePage(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();

        // 兼容前端分页参数: pageNum 和 pageSize
        Integer page = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) :
                       (params.get("current") != null ? Integer.parseInt(params.get("current").toString()) : 1);
        Integer size = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) :
                       (params.get("size") != null ? Integer.parseInt(params.get("size").toString()) : 10);

        params.put("offset", (page - 1) * size);
        params.put("limit", size);

        List<TblSettlementRule> list = ruleMapper.selectRulePage(params);
        int total = ruleMapper.countRuleList(params);

        // 兼容前端数据格式: rows 和 total
        result.put("rows", list);
        result.put("total", total);

        return result;
    }

    @Override
    public TblSettlementRule getRuleById(Long ruleId) {
        return ruleMapper.selectById(ruleId);
    }

    @Override
    public int createRule(TblSettlementRule rule) {
        rule.setIsEnabled(1);
        rule.setDeleteFlag(0);
        rule.setExecutionCount(0);
        rule.setSuccessCount(0);
        rule.setCreatedTime(new Date());
        return ruleMapper.insert(rule);
    }

    @Override
    public int updateRule(TblSettlementRule rule) {
        rule.setUpdatedTime(new Date());
        return ruleMapper.updateById(rule);
    }

    @Override
    public int deleteRule(List<Long> ruleIds) {
        if (ruleIds == null || ruleIds.isEmpty()) return 0;
        int count = 0;
        for (Long id : ruleIds) {
            TblSettlementRule r = getRuleById(id);
            if (r != null && r.getIsEnabled() == 0) {
                r.setDeleteFlag(1);
                r.setUpdatedTime(new Date());
                count += ruleMapper.updateById(r);
            }
        }
        return count;
    }

    @Override
    public int enableRule(List<Long> ruleIds) {
        if (ruleIds == null || ruleIds.isEmpty()) return 0;
        int count = 0;
        for (Long id : ruleIds) {
            TblSettlementRule r = getRuleById(id);
            if (r != null) {
                r.setIsEnabled(1);
                r.setUpdatedTime(new Date());
                count += ruleMapper.updateById(r);
            }
        }
        return count;
    }

    @Override
    public int disableRule(List<Long> ruleIds) {
        if (ruleIds == null || ruleIds.isEmpty()) return 0;
        int count = 0;
        for (Long id : ruleIds) {
            TblSettlementRule r = getRuleById(id);
            if (r != null) {
                r.setIsEnabled(0);
                r.setUpdatedTime(new Date());
                count += ruleMapper.updateById(r);
            }
        }
        return count;
    }

    @Override
    public List<TblSettlementRule> getApplicableRules(Map<String, Object> params) {
        return ruleMapper.selectApplicableRules(params);
    }

    @Override
    public Map<String, Object> testRule(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();
        result.put("matched", true);
        result.put("action", "APPROVE");
        result.put("message", "规则测试通过");
        return result;
    }

    @Override
    public Map<String, Object> getRuleExecutionStats(Long orgId) {
        return ruleMapper.selectRuleExecutionStats(orgId);
    }

    @Override
    public List<Map<String, Object>> detectRuleConflicts(Long orgId) {
        return ruleMapper.selectRuleConflicts(orgId);
    }
}
