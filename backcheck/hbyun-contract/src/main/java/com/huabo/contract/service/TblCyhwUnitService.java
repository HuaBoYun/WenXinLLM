package com.huabo.contract.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.PageInfo;
import com.huabo.contract.entity.TblContractTran;
import com.huabo.contract.entity.TblCyhwUnit;


public interface TblCyhwUnitService {

	Map<String, Object> findLedgerListPageInfo(String token, String staffId, String startdate, String enddate,
			Integer pageNumber, Integer pageSize,TblCyhwUnit unit) throws Exception;

	Map<String, Object> findLedgerListAllOrgPageInfo(String token, String staffId, String startdate, String enddate,
			Integer pageNumber, Integer pageSize, TblCyhwUnit unit) throws Exception;

	Integer findCountByUserSjb(BigDecimal staffid) throws Exception;

	List<TblCyhwUnit> findLedgerListForExport(TblCyhwUnit unit, String fatherOrgIds) throws Exception;

	Map<String, Object> findListByXdf(Integer pageSize, Integer pageNumber, String budgetid) throws Exception;

	Map<String, Object> findCollectionChoiceContract(TblCyhwUnit unit, Integer pageNumber, Integer pageSize,
			String contractno, String contractname) throws Exception;

	void findPaymentChoiceContract(PageInfo<TblCyhwUnit> pageInfo, TblCyhwUnit unit) throws Exception;

	Map<String, Object> getReportContractData(Integer year, Integer month) throws Exception;

	TblCyhwUnit findContractById(BigDecimal contractid) throws Exception;

	void modifyJiuFenContractStatus(BigDecimal historyStatus, Integer currentStatus, BigDecimal contractId) throws Exception;

	void modifyContractStatus(BigDecimal historyStatus, Integer currentStatus, BigDecimal contractId) throws Exception;

	void saveCyhwUnitTcu(TblCyhwUnit tcu) throws Exception;

	void findLegalContractListByPageInfo(PageInfo<TblCyhwUnit> pageInfo, TblCyhwUnit tcu) throws Exception;

	TblCyhwUnit getEntity(BigDecimal contractid) throws Exception;

	Map<String, Object> getContractAnalysis(Integer year) throws Exception;

	Map<String, Object> getContractCollection(Integer year) throws Exception;

	Map<String, Object> orgContractPlan(Integer year, Integer quarter) throws Exception;

	Map<String, Object> orgPayContract(Integer year, Integer quarter) throws Exception;

	Map<String, Object> contractLegalAprStat(Integer year, Integer quarter) throws Exception;

	Map<String, Object> legalLitigationCaseStat(Integer year, Integer quarter) throws Exception;

	JsonBean dynamicReport(String token, Integer indexYtype, Integer indexXtype, String contractTypeText,
			String contractDeptIds, String contractStaffIds, String contractOrgIds, String startDate, String endDate,
			Integer year, Integer quarte) throws Exception;

	JsonBean checkLendStatus(String token, String contractId) throws Exception;

	Map<String, Object> findListContractBiangengBypageInfo(String token, String flowId, String staffId,
			Integer pageNumber, Integer pageSize, TblCyhwUnit cyhwUnit) throws Exception;

	Map<String, Object> saveCyhwUnit(TblCyhwUnit tcu, String attids, String contractxdf, String token, String flowId,Integer goalStatus) throws Exception;

	Integer getStatueById(BigDecimal contractId);

	 Map<String, Object> removeCyhwUnit(BigDecimal contractId);

	 Map<String, Object> findListWindowOpenByPageInfo(Integer pageNumber, Integer pageSize, String recordType, String token, String staffId, String budgetname, String counterpartno);

	 Map<String, Object> checkStageInfo(BigDecimal contractId, String token) throws Exception;

	 Map<String, Object> findeContractInfo(BigDecimal flowId, String flowname, BigDecimal contractId, Integer cflag, String token, String staffId, Integer goalStatus);

	 List<TblCyhwUnit> findContractTemp(String token, String contractType) throws Exception;

	 Map<String, Object> findContractSealList(Integer pageNumber, Integer pageSize, String cateId, String flowid, String isFlowdb, String choose, String token, String staffId, TblCyhwUnit unit);

	Map<String, Object> findeFilContractList(Integer pageNumber, Integer pageSize, String flowid, String token, String staffId, TblCyhwUnit unit) throws Exception;

	JsonBean chooseLendContractList(Integer pageNumber, Integer pageSize, String token, TblCyhwUnit unit) throws Exception;

	Map<String, Object> updBasicUnitinspectionStatue(BigDecimal contractId, String flowname);

	Map<String, Object> updateContractStatus(String contractId, String status, String bindno);

	Map<String, Object> findContractTranList(Integer pageNumber, Integer pageSize, String token, TblContractTran tct) throws Exception;

	Map<String, Object> contractTranListSave(TblContractTran tct, String token) throws Exception;

	Map<String, Object> contractTranInfo(BigDecimal tranId, String token) throws Exception;

	Map<String, Object> sealedContractList(Integer pageNumber, Integer pageSize, String token, TblCyhwUnit unit) throws Exception;

	Map<String, Object> contractTranDelete(BigDecimal tranId, String token) throws Exception;

	Map<String, Object> cyhwUnitOAListSave(String token, String contractId, String oaList) throws Exception;

	Map<String, Object> getCyhwUnitOAList(String token, String contractId) throws Exception;

	Map<String, Object> removeOADocument(String token, String documentId) throws Exception;

	Map<String, Object> findeContractListByContractStaff(String flowId, Integer pageNumber, Integer pageSize, String token, String staffId, TblCyhwUnit unit, Integer isDept);

	Map<String, Object> getChangeContractStaffList(Integer pageNumber, Integer pageSize, String token, String username,
            String realname) throws Exception;

	Map<String, Object> changeContractStaff(String contractId, String staffId, String token) throws Exception;

	Map<String, Object> findeContractListPerformanceTracking(String flowId, Integer pageNumber, Integer pageSize, String token, String staffId, TblCyhwUnit unit);

	Map<String, Object> findFulfillmentContract(String flowId, Integer pageNumber, Integer pageSize, String token, String staffId, TblCyhwUnit unit);

	Map<String, Object> contractExcuteContractId(String contractId, String status);

	Map<String, Object> findContractDes(String budgetId);

	Map<String, Object> modifyContractStatus(String contractId, Integer goalStatus);

	//合同附件接口同步
    Map<String, Object> contractFjInfo(BigDecimal contractId) throws Exception;

    Map<String, Object> fjInfoGetList(String fjid,String userName) throws Exception;

  //相对方信息接口同步
    Map<String, Object> oppsiteInfogetList(TblStaffUtil staff) throws Exception;

	/**
     * 发展资产合同审批单表名处理映射
     *
     * @return
     */
    JsonBean saveFzzcSpCyhwUnit() throws Exception;

	JsonBean generateNo(String token) throws Exception;

	List<TblCyhwUnit> findLedgerOrgListForExport(TblCyhwUnit unit, String allCompanyIds) throws Exception;

	/**
	 * 查询符合合同列表的相对方信息
	 * @param unit
	 * @return
	 * @throws Exception
	 */
	Map<String, String> findOppsiteNamesByUnit(TblCyhwUnit unit, String allCompanyIds) throws Exception;

	Map<String, String> findOppsiteNamesByUnitTaiZhangExport(TblCyhwUnit unit, String fatherOrgIds) throws Exception;
}
