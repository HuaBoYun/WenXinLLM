package com.huabo.compliance.service;

import com.huabo.compliance.entity.TblAssesselement;

import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

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
}
