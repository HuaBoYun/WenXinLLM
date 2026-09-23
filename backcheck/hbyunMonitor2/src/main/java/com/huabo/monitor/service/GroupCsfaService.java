package com.huabo.monitor.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.PageInfo;
import com.hbfk.entity.TblStaffUtil;
import com.huabo.monitor.entity.TblGroupTestplan;
import com.huabo.monitor.entity.TblTesttempleVo;
import com.huabo.monitor.entity.Tree;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface GroupCsfaService {

     boolean isSJByOrgId(String s);
     PageInfo<TblGroupTestplan> findAllNew(TblGroupTestplan plan, Integer pageNumber, String starttime_min, String starttime_max, BigDecimal orgid,BigDecimal staffid,Integer authorityType,TblStaffUtil user)throws Exception;
     PageInfo<TblGroupTestplan> findAllnoSjNew(TblGroupTestplan plan, Integer pageNumber, String starttime_min, String starttime_max, String toString,Integer authorityType,TblStaffUtil user)throws Exception;
     PageInfo<TblGroupTestplan> findAll(TblGroupTestplan plan, Integer pageNumber, String starttime_min, String starttime_max, BigDecimal orgid,BigDecimal staffid,Integer authorityType,TblStaffUtil user)throws Exception;

     Map<String, Object>  queryOneTestPlan(BigDecimal selectProjectid);
     void deleteTestPlan(BigDecimal selectProjectid);
     void deleteAtt(BigDecimal attid);
}
