package com.huabo.fxgl.service;

import com.hbfk.util.JsonBean;
import com.huabo.fxgl.dto.TblMajorRiskbranchCreateDto;
import com.huabo.fxgl.entity.TblRiskImplementEntity;
import com.huabo.fxgl.entity.TblRiskMonitoringFill;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;

import org.apache.poi.ss.formula.functions.T;

public interface TblRiskMonitoringFillService {
	
  /**
  * 填报新增/修改
  * @param token
  * @param tblRiskImplementEntity
  * @return
  * @throws Exception
  */
 JsonBean saveOrUpdate(TblRiskMonitoringFill tblRiskImplementEntity, String token) throws Exception;
 
 void delete(String id) throws Exception;
	
 JsonBean getList(String token, Integer pageNumber, Integer pageSize,TblRiskMonitoringFill tblRiskImplementEntity) throws Exception;

 JsonBean details(String id, String token) throws Exception;
 
 JsonBean reportToLeader(String token,String id)throws Exception;
 
 JsonBean getHzList(String token,String orgid,Integer year,String jd)throws Exception;
 
 
 Map<String, Object>   getReportHzList(String token,String orgid,Integer year,String jd)throws Exception;
 

 JsonBean monitoringCancel(String token,String id)throws Exception;

 public   void exportByTemplate(String templatePath,  Map<String, Object> dataList, ServletOutputStream outputStream,String jd,String orgid,Integer year) throws IOException;
 
 
 JsonBean getRiskMonDeptList(String token,String id)throws Exception;
 
 //重大经营类接口信息
 JsonBean getZdHzList(String token,String orgid,Integer year,String jd)throws Exception;

 Map<String, Object>   getReportZdHzList(String token,String orgid,Integer year,String jd)throws Exception;
 
 public   void exportByZdTemplate(String templatePath,  Map<String, Object> dataList, ServletOutputStream outputStream,String jd,String orgid,Integer year) throws IOException;
 
 //重大经营类接口信息New
 JsonBean getZdHzListNew(String token,String orgid,Integer year,String jd)throws Exception;
 
 Map<String, Object>   getReportZdHzListNew(String token,String orgid,Integer year,String jd)throws Exception;
 
 public   void exportByZdTemplateNew(String templatePath,  Map<String, Object> dataList, ServletOutputStream outputStream,String jd,String orgid,Integer year) throws IOException;

}
