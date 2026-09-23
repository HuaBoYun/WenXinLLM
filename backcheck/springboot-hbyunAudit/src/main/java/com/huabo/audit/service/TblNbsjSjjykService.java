package com.huabo.audit.service;


import java.math.BigDecimal;

import com.hbfk.util.JsonBean;
import com.huabo.audit.oracle.entity.TblNbsjSjjyk;

public interface TblNbsjSjjykService {
	
	public JsonBean saveOrUpdate(TblNbsjSjjyk jyk,String token,BigDecimal jykid,String attids) throws Exception ;
	
	public JsonBean delete(BigDecimal jykid,String token) throws Exception ;
	
	public JsonBean findByjykid(BigDecimal jykid, String token) throws Exception ;
	
	public JsonBean findAll(String code, String tatle,Integer startIndex,Integer pageSize, String token,String experiencetype,String overview ) throws Exception ;
	
	public JsonBean getAttListByjykid(String token, BigDecimal jykid) throws Exception;
	
	public JsonBean delAttListByattId(String token, BigDecimal attid) throws Exception;
	
	public JsonBean findAllByDatapreID(String token,BigDecimal dataperid) throws Exception ;
	
	public JsonBean delAttListByjykId(String token, BigDecimal  jykid) throws Exception;
	
	
	

	

}
