package com.huabo.audit.oracle.service;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblYqnsOtherAudit;
import com.huabo.audit.oracle.entity.TblYqnsProjectAuditTemplateEntity;

public interface TblYqnsOtherAuditService extends IService<TblYqnsOtherAudit> {

	JsonBean getListForChoose(String token, BigDecimal draftPlanId, Integer chooseType, String auditIdStrs) throws Exception;
	
	JsonBean mengerEntity(String token, TblYqnsOtherAudit vo) throws Exception;

	JsonBean getDetailById(String token, BigDecimal auditId) throws Exception;

	JsonBean removeAttInfo(String token, BigDecimal attId) throws Exception;

	JsonBean removById(String token, BigDecimal auditId) throws Exception;

	JsonBean toPreAdd(String token) throws Exception;

	



}
