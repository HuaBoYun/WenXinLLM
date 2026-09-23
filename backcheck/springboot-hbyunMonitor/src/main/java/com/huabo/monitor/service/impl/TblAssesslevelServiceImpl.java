package com.huabo.monitor.service.impl;

import com.hbfk.util.JsonBean;
import com.huabo.monitor.oracle.entity.TblAssesslevel;
import com.huabo.monitor.service.TblAssesslevelService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service("TblAssesslevelService")
public class TblAssesslevelServiceImpl implements TblAssesslevelService {
    @Override
    public JsonBean findByPageBean(Integer pageNumber, Integer pageSize, TblAssesslevel tblAssesslevel) {
        return null;
    }

    @Override
    public JsonBean findById(BigDecimal id) {
        return null;
    }

    @Override
    public JsonBean add(TblAssesslevel tblAssesslevel) {
        return null;
    }

    @Override
    public JsonBean update(TblAssesslevel tblAssesslevel) {
        return null;
    }

    @Override
    public JsonBean delete(BigDecimal asslevid) {
        return null;
    }
}
