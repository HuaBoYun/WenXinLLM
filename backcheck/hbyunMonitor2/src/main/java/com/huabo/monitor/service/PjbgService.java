package com.huabo.monitor.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.github.pagehelper.PageInfo;
import com.hbfk.entity.TblStaffUtil;
import com.huabo.monitor.entity.TblAttachment;
import com.huabo.monitor.entity.TblNbsjBugEntity;
import com.huabo.monitor.entity.TblReport;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface PjbgService {
    IPage<TblReport> findAll(Integer pageNumber, String name, String startDate, String endDate, String type, BigDecimal orgid,BigDecimal staffid,Integer authorityType) throws Exception;

    void saveReport(TblReport report, String attids);

    List<TblAttachment> getRepAttByReportId(BigDecimal reportid);

    void saveRepAtt(TblAttachment a, String reportid,String isDecision);

    void delAttAndRepAtt(BigDecimal attid, String reportid);

    void delReport(String ids);
    
    
    PageInfo<TblReport> findAllNewPage(Integer pageNumber, String name, String startDate, String endDate, String type, BigDecimal orgid,BigDecimal staffid
    		,Integer authorityType,TblStaffUtil user,String year,String reporttype) throws Exception;

    List<TblNbsjBugEntity> selectNbsjBugList(BigDecimal reportid);

    
}
