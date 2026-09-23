package com.huabo.monitor.oracle.mapper;

import com.huabo.monitor.oracle.entity.TblAssesscategory;
import tk.mybatis.mapper.common.BaseMapper;

import java.math.BigDecimal;
import java.util.List;

public interface TblAssesscategoryMapper extends BaseMapper<TblAssesscategory> {
    List<TblAssesscategory> getTreeByNodeId(BigDecimal pId);

    List<TblAssesscategory> findByTempleId(BigDecimal tmplId, BigDecimal nodeId);

    void deleteByTempleId(BigDecimal tmplId);

    List<TblAssesscategory> findByTempleId(BigDecimal tmplId);

    List<TblAssesscategory> getTreeRoot(BigDecimal tmplId);

    List<TblAssesscategory> getParentList(BigDecimal id);

    List<Object[]> getHengXiang(BigDecimal id);

    List<TblAssesscategory> listBySql(String sql);

    List<TblAssesscategory> findAllAssesscategory(String toString);

    BigDecimal save(TblAssesscategory tblAssesscategory);

    void update(TblAssesscategory tblAssesscategory2);

    TblAssesscategory findById(BigDecimal bigDecimal);

    TblAssesscategory get(Class<TblAssesscategory> tblAssesscategoryClass, BigDecimal id);
}
