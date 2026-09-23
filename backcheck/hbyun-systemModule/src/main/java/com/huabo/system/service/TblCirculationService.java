package com.huabo.system.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.hbfk.util.PageInfo;
import com.huabo.system.entity.TblCirculation;

public interface TblCirculationService {
    Map<String, Object> findAll(BigDecimal staffid, TblCirculation tca, PageInfo<TblCirculation> pageInfo);

    TblCirculation get(String cyid);

    List<TblCirculation> findStaffid(String s);

    List<TblCirculation> findAllStaffid(String s);

    List<TblCirculation> findAllString(String s);

    public TblCirculation saveTblCirculation(String type, String number, String name, String url, BigDecimal loginUser);

    public void delTblCirculation(TblCirculation c);

    void update(TblCirculation c);
}
