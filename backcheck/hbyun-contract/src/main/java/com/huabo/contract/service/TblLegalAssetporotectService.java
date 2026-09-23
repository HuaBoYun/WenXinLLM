package com.huabo.contract.service;

import java.math.BigDecimal;

import com.hbfk.util.PageInfo;
import com.huabo.contract.entity.TblLegalAssetporotect;

public interface TblLegalAssetporotectService {

	void findListByPage(PageInfo<TblLegalAssetporotect> pageInfo, BigDecimal litigationid,BigDecimal arbitraid);
	
	void addLegalAssetporotectService(TblLegalAssetporotect tla);
	
	TblLegalAssetporotect getLegalAssetporotectById(BigDecimal id);
	
	void deleteLegalAssetporotectById(BigDecimal id);
	
}
