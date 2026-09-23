package com.huabo.monitor.service;

import com.hbfk.util.JsonBean;
import com.huabo.monitor.oracle.entity.TblTestplan;

import java.util.Date;

public interface TblTestPlanService {
    JsonBean save(TblTestplan tblTestplan);

    JsonBean add(TblTestplan tblTestplan);

    JsonBean deleteById(String plannumber);

    JsonBean selectList(String plannumber, String planname, String planstatus, Date starttime_min, Date starttime_max);
}
