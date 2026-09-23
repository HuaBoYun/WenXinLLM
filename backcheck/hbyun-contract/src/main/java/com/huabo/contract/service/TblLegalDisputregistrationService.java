package com.huabo.contract.service;

import java.math.BigDecimal;
import java.util.Map;

import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.huabo.contract.entity.TblLegalDisputregistration;

public interface TblLegalDisputregistrationService {

	void saveBidType(Integer type, BigDecimal bid, BigDecimal aid) throws Exception;

	Map<String, Object> findAttacheMentByBid(Integer type, BigDecimal bid) throws Exception;

	Map<String, Object> deleteAttacheMentByBid(Integer type, BigDecimal aid) throws Exception;

	JsonBean getDisputeNo() throws Exception;

	TblLegalDisputregistration findBydisputeId(BigDecimal disputeId) throws Exception;

	void findListByPageInfo(String companyId, PageInfo<TblLegalDisputregistration> pageInfo,
			TblLegalDisputregistration dispute, BigDecimal pid) throws Exception;

	void findListLSByPageInfo(String companyId, PageInfo<TblLegalDisputregistration> pageInfo,
			TblLegalDisputregistration dispute, BigDecimal pid) throws Exception;

	void addDiputregistration(TblLegalDisputregistration dispute) throws Exception;

	TblLegalDisputregistration findByDisputeId(BigDecimal disputeid) throws Exception;

	void modifyDiputregistration(TblLegalDisputregistration oldDispute) throws Exception;

	void deleteRelation(String attid) throws Exception;

	TblLegalDisputregistration findByDidputeid(BigDecimal disputeId) throws Exception;

	void deleteAttacheMents(Integer type, BigDecimal bid) throws Exception;

	void removecaseInformation(BigDecimal disputeId) throws Exception;

	TblLegalDisputregistration findById(BigDecimal disputeId) throws Exception;

	void findListByPageInfoDispute(PageInfo<TblLegalDisputregistration> pageInfo, TblLegalDisputregistration dispute,BigDecimal pid,Integer choiceType,BigDecimal oid) throws Exception;

	TblLegalDisputregistration findDisputeId(BigDecimal disputeId) throws Exception;

	TblLegalDisputregistration findByOrgid(BigDecimal disputeId, BigDecimal orgid) throws Exception;

	void delClassicCase(BigDecimal disputeId) throws Exception;

	void addClassicCase(BigDecimal disputeid) throws Exception;

	void findClassicCaseListByPageInfo(String companyId, PageInfo<TblLegalDisputregistration> pageInfo,
			TblLegalDisputregistration dispute, BigDecimal orgid) throws Exception;

	Map<String, Object> setDisputeStatistics(Integer year) throws Exception;

	Map<String, Object> setDisputeMoneyList(Integer year) throws Exception;


}
