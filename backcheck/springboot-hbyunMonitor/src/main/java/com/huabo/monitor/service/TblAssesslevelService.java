package com.huabo.monitor.service;

import com.hbfk.util.JsonBean;
import com.huabo.monitor.oracle.entity.TblAssesslevel;

import java.math.BigDecimal;

public interface TblAssesslevelService {
    JsonBean findByPageBean(Integer pageNumber, Integer pageSize, TblAssesslevel tblAssesslevel);

    JsonBean findById(BigDecimal id);

    JsonBean add(TblAssesslevel tblAssesslevel);

    JsonBean update(TblAssesslevel tblAssesslevel);

    JsonBean delete(BigDecimal asslevid);
}
