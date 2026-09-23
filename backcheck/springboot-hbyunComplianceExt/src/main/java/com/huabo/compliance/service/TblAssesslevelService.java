package com.huabo.compliance.service;

import com.hbfk.util.JsonBean;
import com.huabo.compliance.entity.TblAssesslevel;

import java.math.BigDecimal;
import java.util.List;

public interface TblAssesslevelService {
    JsonBean findByPageBean(Integer pageNumber, Integer pageSize,String orgid)throws Exception;

    JsonBean findById(BigDecimal id);

    JsonBean add(TblAssesslevel tblAssesslevel);

    JsonBean update(TblAssesslevel tblAssesslevel);

    JsonBean delete(BigDecimal asslevid);

    List<TblAssesslevel> findAll(String tblCompany);
}
