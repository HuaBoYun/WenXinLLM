package com.huabo.audit.oracle.service;

import java.math.BigDecimal;

import com.hbfk.util.JsonBean;

public interface AuditControlAnalysisService {

	JsonBean auditControlAnalysisService(String token, Integer preXmnd, Integer endXmnd, Integer choiceType,
			BigDecimal staffId, String staffName, Integer pageNumber, Integer pageSize) throws Exception;

	JsonBean beginYearPlanRate(String token, Integer queryYear) throws Exception;

	JsonBean planCompletionRate(String token, Integer queryYear) throws Exception;

	JsonBean rectificationAmount(String token, Integer queryYear) throws Exception;

	JsonBean rectificationRate(String token, Integer queryYear) throws Exception;

	JsonBean auditAdoptionRate(String token, Integer queryYear) throws Exception;
	
	
	JsonBean selectPlancount(String token, Integer queryYear) throws Exception;
	
	
	JsonBean selectProjectcount(String token,Integer pageNumber, Integer pageSize, Integer queryYear) throws Exception ;
	
	
	JsonBean selectPlanyfcount(String token, Integer queryYear) throws Exception ;
	
	JsonBean selectPlanZtcount(String token, Integer queryYear) throws Exception ;

	JsonBean commandPlanData(String token, Integer queryYear) throws Exception;
	
	JsonBean selectProjectYxStageCount(String token, Integer queryYear) throws Exception ;
	
	JsonBean selectStaffStateCount(String token) throws Exception ;

}
