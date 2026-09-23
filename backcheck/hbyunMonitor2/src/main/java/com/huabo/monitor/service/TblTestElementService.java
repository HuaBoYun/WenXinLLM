package com.huabo.monitor.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.monitor.entity.TblTestelement;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public interface TblTestElementService extends IService<TblTestelement> {

	JsonBean save(String token, TblTestelement newEle) throws Exception;

	JsonBean modify(String token, TblTestelement newEle) throws Exception;

	JsonBean remvoe(String token, BigDecimal elementid) throws Exception;

	JsonBean getPageInfo(String token, Integer pageNumber, Integer pageSize, BigDecimal typeid, BigDecimal templid, String elementcode, String businessdesc) throws Exception;

	JsonBean getInfo(String token, BigDecimal elementid) throws Exception;

	List<TblTestelement> findByPlanidAll(String planid)throws Exception;
	
	//计划内的模板要素查询
	List<TblTestelement> findByPlanidJhn(String planid)throws Exception;

}
