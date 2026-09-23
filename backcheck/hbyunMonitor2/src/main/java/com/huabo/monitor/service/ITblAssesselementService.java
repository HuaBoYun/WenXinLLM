package com.huabo.monitor.service;

import com.huabo.monitor.entity.TblAssesselement;

import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.hbfk.entity.TblStaffUtil;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author yhr
 * @since 2022-08-26
 */
public interface ITblAssesselementService extends IService<TblAssesselement> {
	IPage<TblAssesselement> findByPageBean(String orgid, Integer pageNumber, TblAssesselement assesselement,Integer pagesize);
	
	IPage<TblAssesselement> findByPageBean(String orgid, Integer pageNumber, TblAssesselement assesselement, String notInStr);
	
	TblAssesselement getNumber(String number, String orgid);
	
	void add(TblAssesselement tblAssesselement);
	
	void Update(TblAssesselement tblAssesselement);
	
	TblAssesselement findById(BigDecimal id);
	
	TblAssesselement get(BigDecimal id);
	
	void delete(TblAssesselement tblAssesselement);
	
	List<TblAssesselement> getAssEssByIn(String assessIds);

    List<TblAssesselement> getComany(String orgid);
    
	PageInfo<TblAssesselement> getPageBean(String orgid, Integer pageNumber, TblAssesselement assesselement,Integer pagesize,TblStaffUtil userToken);

	PageInfo<TblAssesselement> findByPageBeanNew(String orgid, Integer pageNumber, TblAssesselement assesselement,List<String> notInStr);

}
