package com.huabo.monitor.service;


import com.huabo.monitor.entity.TblAssEleCategory;
import com.huabo.monitor.entity.TblAssesselement;

import java.math.BigDecimal;
import java.util.List;

public interface TblAssEleCategoryService {

    void save(TblAssEleCategory assEleCategory)throws Exception;

    void saveList(List<TblAssEleCategory> assEleCategorys)throws Exception;

    void update(TblAssEleCategory assEleCategory)throws Exception;

    void delete(TblAssEleCategory assEleCategory);

    void delete(List<TblAssEleCategory> assEleCategorys);

    TblAssEleCategory get(BigDecimal id);

    List<TblAssEleCategory> getAssesscategoryByNodeId(BigDecimal id);

    List<TblAssEleCategory> getAssesscategoryBytmplId(BigDecimal id);

    List<TblAssEleCategory> getAssesscategoryByMuBanId(BigDecimal id);

    List<TblAssEleCategory> getScore(BigDecimal tempId, BigDecimal cateId);

	TblAssesselement getAssesselementBycatid(BigDecimal asseleid);
}