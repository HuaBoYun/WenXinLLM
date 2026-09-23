package com.huabo.monitor.service;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.huabo.monitor.entity.TblAssesslevel;

import java.math.BigDecimal;
import java.util.List;

public interface TblAssesslevelService {
    JsonBean findByPageBean(Integer pageNumber, Integer pageSize,String orgid,TblStaffUtil user )throws Exception;

    JsonBean findById(BigDecimal id);

    JsonBean add(TblAssesslevel tblAssesslevel);

    JsonBean update(TblAssesslevel tblAssesslevel);

    JsonBean delete(BigDecimal asslevid);

    List<TblAssesslevel> findAll(String tblCompany);
}
