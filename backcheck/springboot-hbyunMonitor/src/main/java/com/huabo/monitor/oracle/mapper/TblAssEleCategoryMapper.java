package com.huabo.monitor.oracle.mapper;

import com.huabo.monitor.oracle.entity.TblAssEleCategory;
import tk.mybatis.mapper.common.BaseMapper;

import java.math.BigDecimal;
import java.util.List;

public interface TblAssEleCategoryMapper extends BaseMapper<TblAssEleCategory> {
    List<TblAssEleCategory> getAssesscategoryByNodeId(BigDecimal id);

    List<TblAssEleCategory> getAssesscategoryBytmplId(BigDecimal id);

    List<TblAssEleCategory> getAssesscategoryByMuBanId(BigDecimal id);

    List<TblAssEleCategory> getScore(BigDecimal tempId, BigDecimal cateId);

    void save(TblAssEleCategory assEleCategory);

    void update(TblAssEleCategory assEleCategory);

    TblAssEleCategory get(BigDecimal id);
}
