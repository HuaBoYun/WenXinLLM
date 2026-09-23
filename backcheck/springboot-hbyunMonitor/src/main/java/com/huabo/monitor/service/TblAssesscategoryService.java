package com.huabo.monitor.service;

import com.huabo.monitor.oracle.entity.TblAssesscategory;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public interface TblAssesscategoryService {
    Serializable add(TblAssesscategory tblAssesscategory);

    void addList(List<TblAssesscategory> tblAssesscategory);

    void updateList(List<TblAssesscategory> tblAssesscategory);

    void update(TblAssesscategory tblAssesscategory);

    void delete(String id);

    void deleteListAndChildren(List<TblAssesscategory> tblAssesscategory);

    TblAssesscategory get(BigDecimal id);

    String GetTree(BigDecimal tmplId, String url);

    List<TblAssesscategory> findByTempleId(BigDecimal tmplId, BigDecimal nodeId);

    List<TblAssesscategory> findByTempleId(BigDecimal tmplId);

    void deleteByTempleId(BigDecimal tmplId);

    List<TblAssesscategory> getTreeRoot(BigDecimal tmplId);

    List<TblAssesscategory> getParentList(BigDecimal id);

    List<TblAssesscategory> getTreeByNodeId(BigDecimal parentId);

    List<Object[]> getHengXiang(BigDecimal id);

    String GetTree(BigDecimal tmplId, String url, String userid);
}