package com.global.treasurer.service;

import com.global.treasurer.entity.TblSettlementRule;
import java.util.List;
import java.util.Map;

public interface SettlementRuleService {
    Map<String, Object> getRulePage(Map<String, Object> params);
    TblSettlementRule getRuleById(Long ruleId);
    int createRule(TblSettlementRule rule);
    int updateRule(TblSettlementRule rule);
    int deleteRule(List<Long> ruleIds);
    int enableRule(List<Long> ruleIds);
    int disableRule(List<Long> ruleIds);
    List<TblSettlementRule> getApplicableRules(Map<String, Object> params);
    Map<String, Object> testRule(Map<String, Object> params);
    Map<String, Object> getRuleExecutionStats(Long orgId);
    List<Map<String, Object>> detectRuleConflicts(Long orgId);
}
