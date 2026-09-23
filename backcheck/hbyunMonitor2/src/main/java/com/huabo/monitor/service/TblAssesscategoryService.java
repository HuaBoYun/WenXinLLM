package com.huabo.monitor.service;


import com.huabo.monitor.entity.TblAssesscategory;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

public interface TblAssesscategoryService {
    Serializable add(TblAssesscategory tblAssesscategory);

    void addList(List<TblAssesscategory> tblAssesscategory)throws Exception;

    void updateList(List<TblAssesscategory> tblAssesscategory)throws Exception;

    void insertEntity(TblAssesscategory tblAssesscategory)throws Exception;
    
    void update(TblAssesscategory tblAssesscategory)throws Exception;

    void delete(String id);

    void deleteListAndChildren(List<TblAssesscategory> tblAssesscategory);

    TblAssesscategory get(BigDecimal id);

    String GetTree(BigDecimal tmplId, String url);

    List<TblAssesscategory> findByTempleId(BigDecimal tmplId);

    void deleteByTempleId(BigDecimal tmplId);

    List<TblAssesscategory> getTreeRoot(BigDecimal tmplId);

    List<TblAssesscategory> getParentList(BigDecimal id);

    List<TblAssesscategory> getTreeByNodeId(BigDecimal parentId);

    List<Object[]> getHengXiang(BigDecimal id);

    String GetTree(BigDecimal tmplId, String url, String userid);
}