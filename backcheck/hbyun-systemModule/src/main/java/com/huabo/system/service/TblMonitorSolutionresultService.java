package com.huabo.system.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.hbfk.util.PageInfo;
import com.huabo.system.entity.TblMonitorSolutionresult;

public interface TblMonitorSolutionresultService {
	
    List<String> find(String table, BigDecimal SoultionId, String ruleid, String zt);

    Map<String, Object> findBySoultionIdZKZX(String table, BigDecimal SoultionId, String rulid, String zt, PageInfo<TblMonitorSolutionresult> pageInfo);

    Map<String, Object> findBySoultionIdZK(String table, BigDecimal SoultionId, String rulid, String zt, PageInfo<TblMonitorSolutionresult> pageIn);

}
