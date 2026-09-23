package com.huabo.system.service;

import java.math.BigDecimal;
import java.util.Map;

import com.hbfk.util.PageInfo;
import com.huabo.system.entity.TblMonitorRule;

public interface TblMonitorRuleService {
	
    Map<String, Object> findTblMonitorRuleByUser(BigDecimal staffid, PageInfo<TblMonitorRule> pageInfo);

    TblMonitorRule findOne(BigDecimal ruleid);

    void findAll(String solutionid, PageInfo<TblMonitorRule> pageInfo);

}
