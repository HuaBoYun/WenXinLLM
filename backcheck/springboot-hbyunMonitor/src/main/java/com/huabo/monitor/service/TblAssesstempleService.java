package com.huabo.monitor.service;

import com.hbfk.util.JsonBean;
import com.huabo.monitor.oracle.entity.TblAssesstemple;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface TblAssesstempleService {
    List<TblAssesstemple> getTmplByNumber(String templeNumber, BigDecimal orgid);

    Map<String, Object> findAll(String toString, Integer pageNumber, Integer pageSize, TblAssesstemple assesstemple);

    TblAssesstemple findByid(JsonBean tmplId);

    JsonBean add(TblAssesstemple tblAssesstemple);

    void modify(TblAssesstemple tblAssesstemple);

    JsonBean findByPageBean(Integer pageNumber, Integer pageSize, TblAssesstemple tblAssesstemple);
}
