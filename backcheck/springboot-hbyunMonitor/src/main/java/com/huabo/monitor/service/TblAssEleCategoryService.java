package com.huabo.monitor.service;

import com.huabo.monitor.oracle.entity.TblAssEleCategory;

import java.math.BigDecimal;
import java.util.List;

public interface TblAssEleCategoryService {

    void save(TblAssEleCategory assEleCategory);

    void saveList(List<TblAssEleCategory> assEleCategorys);

    void update(TblAssEleCategory assEleCategory);

    void delete(TblAssEleCategory assEleCategory);

    void delete(List<TblAssEleCategory> assEleCategorys);

    TblAssEleCategory get(BigDecimal id);

    List<TblAssEleCategory> getAssesscategoryByNodeId(BigDecimal id);

    List<TblAssEleCategory> getAssesscategoryBytmplId(BigDecimal id);

    List<TblAssEleCategory> getAssesscategoryByMuBanId(BigDecimal id);

    List<TblAssEleCategory> getScore(BigDecimal tempId, BigDecimal cateId);
}