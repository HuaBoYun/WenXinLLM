package com.huabo.monitor.service;

import com.hbfk.util.JsonBean;
import com.huabo.monitor.entity.TblTestTemplate;

import java.math.BigDecimal;


public interface TblTestTemplateService {

    JsonBean add(TblTestTemplate template);

    JsonBean updateById(BigDecimal testtemid);

    JsonBean deleteById(String templeNumber);

    JsonBean selectList(String token, Integer pageNumber, Integer pageSize, String templeNumber, String templename) throws Exception;

    JsonBean save(String token,TblTestTemplate tblTestTemplate) throws Exception;

	JsonBean modify(String token, TblTestTemplate tblTestTemplate) throws Exception;

	JsonBean remove(String token, Integer templId) throws Exception;

	JsonBean saveissued(String token, String tempIds, String orgIds) throws Exception;
	
	JsonBean copyIssued(String token, String tempIds) throws Exception;
	
	JsonBean getInfo(String token, BigDecimal testtemid) throws Exception;
	
	JsonBean csmb_editcheck(Integer testtemid) throws Exception;
	
	JsonBean getDetail(BigDecimal testtemid) throws Exception;
}
