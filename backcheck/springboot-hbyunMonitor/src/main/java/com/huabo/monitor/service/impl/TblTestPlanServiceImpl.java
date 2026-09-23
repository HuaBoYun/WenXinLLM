package com.huabo.monitor.service.impl;

import com.hbfk.util.JsonBean;
import com.huabo.monitor.oracle.entity.TblTestplan;
import com.huabo.monitor.service.TblTestPlanService;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TblTestPlanServiceImpl implements TblTestPlanService {
    @Override
    public JsonBean save(TblTestplan tblTestplan) {
        return null;
    }

    @Override
    public JsonBean add(TblTestplan tblTestplan) {
        return null;
    }

    @Override
    public JsonBean deleteById(String plannumber) {
        return null;
    }

    @Override
    public JsonBean selectList(String plannumber, String planname, String planstatus, Date starttime_min, Date starttime_max) {
        return null;
    }
}
