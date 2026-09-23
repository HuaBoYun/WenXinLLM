package com.huabo.system.service;

import java.util.List;
import java.util.Map;

import com.huabo.system.entity.TblFlowInnerRule;

public interface TblFlowInnerRuleService {
    void saveTblFlowInnerRule(TblFlowInnerRule var1);
    
    Map<String, Object> findByOrgidAndFlowidobj(String flowid, Integer pageNumber, Integer pageSize, String token) throws Exception;

    List<TblFlowInnerRule> isIfFlowInner(String flowid, String[] innerid);

    void deleteTblFlowInnerRule(TblFlowInnerRule inner);

    Map<String, Object> findOutRuleByOrgidAndFlowid(String name, String status, String flowid, Integer pageNumber, Integer pageSize, String token);
    
    void delteTblFlowInnerRule(TblFlowInnerRule inner);
}
