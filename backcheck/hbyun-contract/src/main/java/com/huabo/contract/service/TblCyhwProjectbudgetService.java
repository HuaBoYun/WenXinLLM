package com.huabo.contract.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.huabo.contract.entity.TblCyhwProjectbudget;

public interface TblCyhwProjectbudgetService {

	Map<String, Object> findBudgetListByStaffOrg(String token, String flowId, String staffId,
			TblCyhwProjectbudget budget, Integer pageNumber, Integer pageSize) throws Exception;

	Map<String, Object> SaveOppositeParty(TblCyhwProjectbudget tcpb, String attids, String staffId) throws Exception;

	void removeOppsiteFile(String attid) throws Exception;

	Integer findStatueById(BigDecimal budgetId) throws Exception;

	Map<String, Object> findOppsiteAllInfoById(BigDecimal budgetId) throws Exception;

	Map<String, Object> mengerOppsitePartyById(TblCyhwProjectbudget tcpb, String attids) throws Exception;

	Map<String, Object> removeOppsitePartyInfo(String budgetId) throws Exception;

	Map<String, Object> removeOppsitePartyInfoNoLc(String budgetId) throws Exception;

	Map<String, Object> saveOppsitePartyBlack(BigDecimal budgetId, Integer blackType, String datetext, String backreason, String brid) throws Exception;

	Map<String, Object> finOppsiteWarningList(Integer pageNumber, TblCyhwProjectbudget budget, Integer isFlowdb, String flowNumber, Integer view, Integer pageSize) throws Exception;

	String projectrBudgetDetail(BigDecimal budgetid) throws Exception;

	Map<String, Object> findOppsiteBlackList(Integer pageNumber, TblCyhwProjectbudget tcbp, String flowNumber,
			Integer pageSize, String staffId, String choose) throws Exception;

	Map<String, Object> addOppsitePartyBlackList(BigDecimal budgetid) throws Exception;

	JsonBean removeOppsitePartyBlack(BigDecimal budgetid, String brid, String rmid, String remreason, String token) throws Exception;

	List<TblCyhwProjectbudget> blacklistExport(BigDecimal orgid, TblCyhwProjectbudget tcbp, TblStaffUtil staff) throws Exception;

	void invoiceCounterpartInfoListByPageInfo(PageInfo<TblCyhwProjectbudget> pageInfo, TblCyhwProjectbudget tcpb) throws Exception;

	List<TblCyhwProjectbudget> getContractBudgetList(BigDecimal contractId) throws Exception;

	Map<String, Object> projectrBudgetToModify(BigDecimal flowId, BigDecimal budgetId) ;

	Map<String, Object> projectrBudgettoAdd(BigDecimal flowId, BigDecimal contractId);

	Map<String, Object> insertOrUpdateBybudget(TblCyhwProjectbudget tcpb, String token, String attids,String flowId,BigDecimal sealorgid,BigDecimal singingId);

	Map<String, Object> findbudgteInfoById(BigDecimal budgetId, String token) throws Exception;

	Map<String, Object> loadAddOppositePartyInfo(String flowId, Integer contractId, String choiceSearch, String token,
			String staffId);

	JsonBean getOppoRelaInfo(String removeid, String blackid) throws Exception;


	
}
