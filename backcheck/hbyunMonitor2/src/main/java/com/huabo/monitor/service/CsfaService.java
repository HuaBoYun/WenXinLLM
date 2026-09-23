package com.huabo.monitor.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.PageInfo;
import com.hbfk.entity.TblStaffUtil;
import com.huabo.monitor.entity.TblGroupTestplan;
import com.huabo.monitor.entity.TblTestplan;
import com.huabo.monitor.entity.TblTestplanVo;
import com.huabo.monitor.entity.TblTesttempleVo;
import com.huabo.monitor.entity.Tree;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface CsfaService {





    boolean isSJByOrgId(String s);

    IPage<TblTestplan> findAll(TblTestplan plan, Integer pageNumber, String starttime_min, String starttime_max, BigDecimal orgid,BigDecimal staffid,Integer authorityType);

    PageInfo<TblGroupTestplan> getGroupPlanList(Integer pageNumber, BigDecimal orgid,BigDecimal staffid);

    
    PageInfo<TblTestplan> findAllNew(TblTestplan plan, Integer pageNumber, String starttime_min, String starttime_max, BigDecimal orgid,BigDecimal staffid,Integer authorityType,TblStaffUtil user)throws Exception;

    IPage<TblTestplan> findAllnoSj(TblTestplan plan, Integer pageNumber, String starttime_min, String starttime_max, String toString,Integer authorityType);

    PageInfo<TblTestplan> findAllnoSjNew(TblTestplan plan, Integer pageNumber, String starttime_min, String starttime_max, String creatid,Integer authorityType,TblStaffUtil user)throws Exception;
    
    TblTestplanVo queryOneTestPlanVo(BigDecimal selectProjectid);

    PageInfo<TblTesttempleVo> getTestTemp(BigDecimal orgid, Integer pageNumber, String templeNumber, String templename, Integer pageSize,BigDecimal secrectLevelId,TblStaffUtil staff);

    void delTesttasksByPanId(BigDecimal testplanid);

     void deleteBytaskId(BigDecimal testtaskid);

    List<Tree> getTreeC(BigDecimal testtemid);

    PageInfo<Map<String,Object>> fingByTree(BigDecimal node, BigDecimal templId, BigDecimal planid, Integer pageNumber, Integer pageSize);

    void updateTesttsak(String task, String planid, String userid);

    Integer findByPlan(BigDecimal selectProjectid);
    
    Integer findByPlanJhn(BigDecimal selectProjectid);

    Integer findByPlanwStra(BigDecimal selectProjectid);

    void deleteTestPlan(BigDecimal selectProjectid);
    
    void confirmIssuance(BigDecimal id);
}
