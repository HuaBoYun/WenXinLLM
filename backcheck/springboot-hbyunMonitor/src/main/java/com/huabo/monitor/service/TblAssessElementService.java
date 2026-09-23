package com.huabo.monitor.service;

import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.huabo.monitor.oracle.entity.TblAssesselement;

import java.math.BigDecimal;
import java.util.List;

public interface TblAssessElementService {
    JsonBean add(TblAssesselement tblAssesselement);

    /**
     * 查询模板里面不存在的要素
     *
     * @param pageNumber
     * @param pageSize
     * @param assesselement
     * @return
     */
    List<String> findByPageBean(Integer pageNumber, int pageSize, TblAssesselement assesselement);

    JsonBean deleteByIds(List deleteIds);

    JsonBean update(TblAssesselement tblAssesselement);


    List<TblAssesselement> getAssEssByIn(String assessIds);

    TblAssesselement get(BigDecimal id);

    List<TblAssesselement> getComany(String orgid);


}