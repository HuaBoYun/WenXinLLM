package com.huabo.system.service;

import java.math.BigDecimal;
import java.util.Map;

import com.hbfk.util.PageInfo;
import com.huabo.system.entity.TblMonitorSolution;

public interface TblMonitorSolutionService {
	
    Map<String, Object> tblMonitorSolutionService(BigDecimal staffid, PageInfo<TblMonitorSolution> pageInfo, String type);

    TblMonitorSolution findOne(String solutionid);

}
