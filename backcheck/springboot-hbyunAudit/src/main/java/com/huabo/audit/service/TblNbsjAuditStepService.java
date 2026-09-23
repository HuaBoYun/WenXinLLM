package com.huabo.audit.service;

import java.math.BigDecimal;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblNbsjAuditStepEntity;

public interface TblNbsjAuditStepService {
	
	public JsonBean findByExper(String token,BigDecimal typeId,TblNbsjAuditStepEntity setp,Integer pageNumber,Integer pageSize) throws Exception;
	public JsonBean deleteByExperId(String token,BigDecimal stepId) throws Exception;
	public JsonBean saveOrupdate(String token,TblNbsjAuditStepEntity auditStep) throws Exception;
	
	public JsonBean getone(String token,BigDecimal stepId) throws Exception;
	
	public JsonBean getList(String token,Integer pageNumber,Integer pageSize,String sql) throws Exception;
	
	public JsonBean zxsql(String token, BigDecimal stepId) throws Exception;
	
	public JsonBean getxjjgList(String token, BigDecimal stepId) throws Exception;
	
	public JsonBean getDatelistt(String token, BigDecimal resultid,Integer pageNumber,Integer pageSize) throws Exception;
	
	public JsonBean findBycode(String token)throws Exception;
	
	JsonBean xgStatus(String token, BigDecimal stepId,Integer xgstatus) throws Exception;
	
	JsonBean saveXfry(String token, String stepIds,String staffids) throws Exception;
	
	JsonBean findByxfUser(String token, BigDecimal stepId,String realname) throws Exception;
	
	JsonBean deleteXfry(String token, BigDecimal stepId,String staffids) throws Exception;
	
	JsonBean findByxfStep(String token,TblNbsjAuditStepEntity setp,Integer pageNumber,Integer pageSize) throws Exception;
}
