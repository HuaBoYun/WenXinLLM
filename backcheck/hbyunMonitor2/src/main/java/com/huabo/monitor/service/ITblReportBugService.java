package com.huabo.monitor.service;

import com.huabo.monitor.entity.TblReport;
import com.huabo.monitor.entity.TblReportBug;

import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.extension.service.IService;

public interface ITblReportBugService extends IService<TblReportBug> {


List<TblReportBug> getListByReportid(BigDecimal id)throws Exception;

 void saveEntity(TblReportBug entity) throws Exception;
 
 void deleteReportBug(BigDecimal bugId,BigDecimal reportId)throws Exception;
 
 void deleteByReportId(BigDecimal reportId)throws Exception;

 
}
