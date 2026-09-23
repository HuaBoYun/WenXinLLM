package com.huabo.system.service;

import com.hbfk.util.PageInfo;
import com.huabo.system.entity.TblIndicator;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface TblIndicatorService {
    Map<String,Object> findIndicatorByUseridAndSlouid(PageInfo<TblIndicator> pageInfo, BigDecimal staffid);

    Map<String, Object> findIndicatorByJKZX(String solutionid, PageInfo<TblIndicator> pageInfo);

    TblIndicator findOne(String indicatorid);
}
