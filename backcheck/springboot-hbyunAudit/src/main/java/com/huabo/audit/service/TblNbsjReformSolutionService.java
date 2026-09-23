package com.huabo.audit.service;


import java.math.BigDecimal;
import java.util.List;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblNbsjReformSolution;

import cn.hutool.json.JSONObject;

public interface TblNbsjReformSolutionService {
	
	public JsonBean saveOrUpdate(TblNbsjReformSolution solution,String token,String reformid,String attids,String projectid,String endDate) throws Exception ;
	
	public JsonBean delete(String solutionid,String token) throws Exception ;
	
	public JsonBean findBySolutionid(String solutionid) throws Exception ;
	
	public JsonBean findAllcxs(String code, String name,Integer startIndex,Integer pageSize, String token,String type,BigDecimal createstaffid,Integer runstatus) throws Exception ;
	
	public JsonBean getAttListBySolutionId(String token, BigDecimal solutionid) throws Exception;
	
	public JsonBean delAttListBySolutionId(String token, BigDecimal attid) throws Exception;
	
	
	public JsonBean xfry(TblNbsjReformSolution solution,String token) throws Exception ;
	
	public List<JSONObject> findAlls2(BigDecimal solution);
	

}
