package com.huabo.contract.service;

import java.math.BigDecimal;

import javax.transaction.xa.XAException;

import com.hbfk.util.PageInfo;
import com.huabo.contract.entity.TblLegalNegotiatedsettlemen;

public interface TblLegalNegotiatedsettlemenService {

	void findListByPage(PageInfo<TblLegalNegotiatedsettlemen> pageInfo, TblLegalNegotiatedsettlemen negotia,
			BigDecimal pid, BigDecimal disputeid) throws Exception;

	TblLegalNegotiatedsettlemen findById(BigDecimal negotiaId) throws Exception;

	void addDiputregistration(TblLegalNegotiatedsettlemen negotiated, String attids) throws Exception;

	void modifyNegotiatedSettlement(TblLegalNegotiatedsettlemen oldNegotiated) throws Exception;

	void removeLegalNegotiatedSettlemen(BigDecimal negotiaId) throws Exception;

	void findListForLitiationList(PageInfo<TblLegalNegotiatedsettlemen> pageInfo,
			TblLegalNegotiatedsettlemen negotiate) throws Exception;
}
