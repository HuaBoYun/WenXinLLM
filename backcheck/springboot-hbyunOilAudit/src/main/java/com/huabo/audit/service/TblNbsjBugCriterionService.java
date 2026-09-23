package com.huabo.audit.service;

import java.util.Map;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblNbsjBugCriterion;

public interface TblNbsjBugCriterionService{
	
	//public JsonBean getNbsjBugCriterionListForMerge(String token) throws Exception;

	public JsonBean saveNbsjBugCriterion(TblNbsjBugCriterion bug,String token) throws Exception;
	
	JsonBean SelectNbsjBugCriterion(String bugid,String token) throws Exception;
	
	JsonBean delNbsjBugCriterion(String bugcriid,String token) throws Exception;
	
	/**
	 * 缺陷标准列表页
	 * @param templeteName
	 * @param templeteCode
	 * @param status
	 * @param tempType
	 * @param pageNumber
	 * @param pageSize
	 * @param orgId
	 * @return
	 */
    JsonBean selectNbsjBugCriterionByPageInfo(String token, Integer pageNumber, Integer pageSize) throws Exception;
	
	
}
