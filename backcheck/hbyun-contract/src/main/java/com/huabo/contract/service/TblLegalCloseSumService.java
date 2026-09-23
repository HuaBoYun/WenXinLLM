package com.huabo.contract.service;

import java.math.BigDecimal;

import com.huabo.contract.entity.TblLegalCloseSum;

public interface TblLegalCloseSumService {
	
	void addLegalCloseSum(TblLegalCloseSum tla) throws Exception;
	
	TblLegalCloseSum getLegalCloseSumById(BigDecimal id);
	
	void deleteLegalCloseSumById(BigDecimal id);
	
	TblLegalCloseSum getLegalCloseSumBySSZCId(BigDecimal litigationid,BigDecimal arbitraid);
}
