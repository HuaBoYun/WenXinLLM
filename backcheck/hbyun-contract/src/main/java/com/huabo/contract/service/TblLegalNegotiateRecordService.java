package com.huabo.contract.service;

import java.math.BigDecimal;
import java.util.List;

import com.hbfk.util.PageInfo;
import com.huabo.contract.entity.TblLegalNegotiaterecord;

public interface TblLegalNegotiateRecordService {

	List<TblLegalNegotiaterecord> findListBynegotiaId(BigDecimal negotiaId) throws Exception;

	void removeNegitiateRecord(BigDecimal recordid);

	void saveNegotiateRecord(TblLegalNegotiaterecord record) throws Exception;

	TblLegalNegotiaterecord findById(BigDecimal recordId) throws Exception;

	void findByNegotiaId(PageInfo<TblLegalNegotiaterecord> pageInfo, BigDecimal negotiaId) throws Exception;

	void modifyNegotiateRecord(TblLegalNegotiaterecord oldRecord) throws Exception;
	
}
