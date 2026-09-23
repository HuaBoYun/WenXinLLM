package com.huabo.system.service;

import com.hbfk.util.PageInfo;
import com.huabo.system.entity.TblMonitorIndicatorresult;

import java.math.BigDecimal;
import java.util.Map;

public interface TblMonitorIndicatorresultService {
    Map<String, Object> getResultListJKZX(PageInfo<TblMonitorIndicatorresult> pageInfo, BigDecimal indicatorid, BigDecimal solutionresultid);}
