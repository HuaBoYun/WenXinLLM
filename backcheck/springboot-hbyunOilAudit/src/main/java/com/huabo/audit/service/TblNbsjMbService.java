package com.huabo.audit.service;


import java.math.BigDecimal;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblNbsjMb;

public interface TblNbsjMbService {
	
	public JsonBean saveOrUpdate(TblNbsjMb mb,String token,BigDecimal mbid,String attids) throws Exception ;
	
	public JsonBean delete(BigDecimal mbid,String token) throws Exception ;
	
	public JsonBean findByMbid(BigDecimal mbid, String token) throws Exception ;
	
	public JsonBean findAll(String code, String name,Integer startIndex,Integer pageSize, String token,String auditype) throws Exception ;
	
	public JsonBean getAttListByMbId(String token, BigDecimal mbid) throws Exception;
	
	public JsonBean delAttListByattId(String token, BigDecimal attid) throws Exception;
	
	public JsonBean findAllByDatapreID(String token,Integer dataperid) throws Exception ;
	
	public JsonBean delAttListBymbId(String token, BigDecimal mbid) throws Exception;
	
	
	
	
	

	

}
