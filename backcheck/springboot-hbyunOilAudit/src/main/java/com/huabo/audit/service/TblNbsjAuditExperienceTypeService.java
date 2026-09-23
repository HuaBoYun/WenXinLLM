package com.huabo.audit.service;

import java.math.BigDecimal;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblNbsjAuditExperienceTypeEntity;

public interface TblNbsjAuditExperienceTypeService{
	
	public JsonBean getRoot(String token,BigDecimal nodeId)  throws Exception ;
	
	
	public JsonBean saveOrupdate(TblNbsjAuditExperienceTypeEntity type,String token)  throws Exception;
	
	public JsonBean delete(String token,BigDecimal nodeId)  throws Exception ;
	
	public JsonBean findbyid(String token,BigDecimal nodeId)  throws Exception ;
}
