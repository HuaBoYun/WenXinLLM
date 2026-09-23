package com.huabo.audit.service;

import java.math.BigDecimal;
import java.util.List;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblNbsjStaffscore;
import com.huabo.audit.oracle.entity.TblNbsjStaffscoreDetails;

public interface TblNbsjStaffscoreService {
	
	public JsonBean saveOrUpdate(TblNbsjStaffscore re,String token,BigDecimal staffScoreid, List<TblNbsjStaffscoreDetails> tblNbsjStaffscoreDetails) throws Exception ;
	
	public JsonBean delete(BigDecimal staffScoreid,String token) throws Exception ;
	
	public JsonBean findAll(TblNbsjStaffscore re,Integer startIndex,Integer pageSize, String token) throws Exception ;
	
	public JsonBean getAttListByStaffScoreid(String token, BigDecimal staffScoreid) throws Exception;
	
	public JsonBean getscoreListnopage(TblNbsjStaffscore re,String token) throws Exception ;

	
}