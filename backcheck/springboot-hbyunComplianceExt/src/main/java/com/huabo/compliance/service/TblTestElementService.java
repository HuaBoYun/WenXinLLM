package com.huabo.compliance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.compliance.entity.TblTestelement;

import java.math.BigDecimal;
import java.util.List;

public interface TblTestElementService extends IService<TblTestelement> {

	JsonBean save(String token, TblTestelement newEle) throws Exception;

	JsonBean modify(String token, TblTestelement newEle) throws Exception;

	JsonBean remvoe(String token, BigDecimal elementid) throws Exception;

	JsonBean getPageInfo(String token, Integer pageNumber, Integer pageSize, BigDecimal typeid, BigDecimal templid, String elementcode, String businessdesc) throws Exception;

	JsonBean getInfo(String token, BigDecimal elementid) throws Exception;

	List<TblTestelement> findByPlanidAll(String planid)throws Exception;
	
}
