package com.huabo.contract.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.PageInfo;
import com.huabo.contract.entity.TblAttachment;
import com.huabo.contract.entity.TblContractBudget;
import com.huabo.contract.entity.TblContractTran;
import com.huabo.contract.entity.TblCyhwProjectbudget;
import com.huabo.contract.entity.TblCyhwUnit;
import com.huabo.contract.entity.TblOrganization;
import com.huabo.contract.mappersql.TblCyhwUnitMapperSqlConfig;
import com.huabo.contract.vo.ContractTypeVO;

import net.sf.json.JSONObject;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2021-10-23
 */
public interface TblCyhwUnitMapper extends BaseMapper<TblCyhwUnit> {

	@Select("SELECT COUNT(0) FROM TBL_ORGANIZATION WHERE ORGNAME LIKE '%审计%' AND ORGID = (SELECT ORGID FROM TBL_STAFF WHERE STAFFID = #{staffid})")
	Integer findCountByUserSjb(@Param("staffid") BigDecimal staffid) throws Exception;

	@SelectProvider(type = TblCyhwUnitMapperSqlConfig.class, method = "selectLedgerListPageInfo")
	IPage<TblCyhwUnit> selectLedgerListPageInfo(IPage<TblCyhwUnit> page, TblCyhwUnit condition, String fatherOrgIds) throws Exception;

	@SelectProvider(type = TblCyhwUnitMapperSqlConfig.class, method = "selectLedgerOrgListPageInfo")
	IPage<TblCyhwUnit> selectLedgerOrgListPageInfo(IPage<TblCyhwUnit> page, TblCyhwUnit unit, String allCompanyIds) throws Exception;

	@SelectProvider(type = TblCyhwUnitMapperSqlConfig.class, method = "findLedgerListForExport")
	List<TblCyhwUnit> findLedgerListForExport(TblCyhwUnit unit, String fatherOrgIds) throws Exception;

	@Select("SELECT COUNT(0) FROM TBL_CYHW_UNIT WHERE RECORDTYPE IN ('HTGL002','HTGL005') AND CONTRACTID IN (SELECT CONTRACTID FROM TBL_CONTRACT_BUDGET WHERE BUDGETID = #{budgetid})")
	// 不包含变跟钱的加入下面sql语句
	// AND CONTRACTID NOT IN (SELECT RECORDPARENT FROM TBL_CYHW_UNIT WHERE RECORDTYPE = 'HTGL005' AND RECORDPARENT IS NOT NULL)
	Integer selectCyhwUnitCountByBudgetId(String budgetid) throws Exception;

	@Select("SELECT COUNT(0) FROM TBL_CYHW_UNIT WHERE CONTRACTID IN (SELECT CONTRACTID FROM TBL_CONTRACT_SPNODE WHERE ISWY='是' AND NODEPOST IS NOT NULL AND CONTRACTID IN (SELECT CONTRACTID FROM TBL_CONTRACT_BUDGET WHERE BUDGETID=#{budgetid}))")
	Integer selectContractCountWyByBudgetId(String budgetid) throws Exception;
	
	@SelectProvider(type = TblCyhwUnitMapperSqlConfig.class, method = "selectNowyearcount1")
	Integer selectNowyearcount1(String budgetid,String dateStr)  throws Exception;

	@Select("SELECT * FROM TBL_CYHW_UNIT TCU WHERE CONTRACTID = #{contractid}")
	TblCyhwUnit selectChangeContractInfo(BigDecimal contractid)  throws Exception;

	@SelectProvider(type = TblCyhwUnitMapperSqlConfig.class, method = "findListByXdf")
	IPage<TblCyhwUnit> findListByXdf(IPage<TblCyhwUnit> page, String budgetid) throws Exception;

	@SelectProvider(type = TblCyhwUnitMapperSqlConfig.class, method = "findCollectionChoiceContractPid")
	IPage<TblCyhwUnit> findCollectionChoiceContractPid(IPage<TblCyhwUnit> page, BigDecimal pid, String contractno,
			String contractname) throws Exception;

	@SelectProvider(type = TblCyhwUnitMapperSqlConfig.class, method = "selectListByPageInfo")
	IPage<TblCyhwUnit> selectListByPageInfo(IPage<TblCyhwUnit> page, TblCyhwUnit unit) throws Exception;

	@SelectProvider(method = "getYearMoney", type = TblCyhwUnitMapperSqlConfig.class)
	BigDecimal getYearMoney(String orgid, String startDate, String endDate) throws Exception;

	// 本年签订合同数量
	@SelectProvider(method = "getContractCount", type = TblCyhwUnitMapperSqlConfig.class)
	BigDecimal getContractCount(String orgid, String startDate, String endDate) throws Exception;

	@SelectProvider(method = "getBreachCount", type = TblCyhwUnitMapperSqlConfig.class)
	BigDecimal getBreachCount(String orgid, String startDate, String endDate) throws Exception;

	@SelectProvider(method = "getCounterpartCount", type = TblCyhwUnitMapperSqlConfig.class)
	BigDecimal getCounterpartCount(String orgid, String startDate, String endDate) throws Exception;

	@SelectProvider(method = "getReceivables", type = TblCyhwUnitMapperSqlConfig.class)
	BigDecimal getReceivables(String orgid, String startDate, String endDate) throws Exception;

	@SelectProvider(method = "getActualCollection", type = TblCyhwUnitMapperSqlConfig.class)
	BigDecimal getActualCollection(String orgid, String startDate, String endDate) throws Exception;

	@SelectProvider(method = "getPayable", type = TblCyhwUnitMapperSqlConfig.class)
	BigDecimal getPayable(String orgid, String startDate, String endDate) throws Exception;

	@SelectProvider(method = "getActualPayment", type = TblCyhwUnitMapperSqlConfig.class)
	BigDecimal getActualPayment(String orgid, String startDate, String endDate) throws Exception;

	@SelectProvider(method = "getContractType", type = TblCyhwUnitMapperSqlConfig.class)
	@Results({ @Result(column = "value", property = "value"), @Result(column = "name", property = "name") })
	List<ContractTypeVO> getContractType(String orgid, String startDate, String endDate) throws Exception;

	@SelectProvider(method = "getContractMonthYear", type = TblCyhwUnitMapperSqlConfig.class)
	List<JSONObject> getContractMonthYear(String orgid, String startDate, String endDate) throws Exception;

	@SelectProvider(method = "getOrgCount", type = TblCyhwUnitMapperSqlConfig.class)
	List<Integer> getOrgCount(String orgid, String startDate, String endDate) throws Exception;

	@Select("SELECT ORGNAME FROM TBL_ORGANIZATION WHERE  FATHERORGID = #{orgid} and   icode='1' and orgtype=0 and status=0 ")
	List<String> getOrgs(@Param("orgid")String orgid) throws Exception;

	@Select("select orgname from tbl_organization where fatherorgid!=1 and orgtype>0 and fatherorgid=#{orgid} and status=0")
	List<String> getCompany(@Param("orgid")String orgid) throws Exception;

	@Select("select * from tbl_organization where (fatherorgid IN (${companyIds}) OR ORGID = #{orgid} ) and status=0")
	List<TblOrganization> getCompanyOrgid(@Param("orgid")String orgid,@Param("companyIds") String companyIds) throws Exception;

	@SelectProvider(method = "getCompanyCount2", type = TblCyhwUnitMapperSqlConfig.class)
	Integer getCompanyCount2(String orgid, String startDate, String endDate) throws Exception;

	@SelectProvider(method = "getCompanyAmount2", type = TblCyhwUnitMapperSqlConfig.class)
	BigDecimal getCompanyAmount2(String orgid, String startDate, String endDate) throws Exception;

	@Select("SELECT T.CONTRACTID,T.FLOWID,T.UNITNAME,T.TYPEFL,T.CONTRACTNAME,T.CONTRACTITEM,T.CONTRACTNO,T.CONTRACTDEPT,T.CONTRACTLINK,T.ISMANY,T.AGREEMENTCOUNT,T.CONTRACTMONEY,T.CONTRACTSTATUS,T.LINKDEPT,T.ORGID,T.CREATEUSER,T.CREATETIME,T.MOMOCONCAT,T.RISKCONTROL,T.DESCRIBE,T.CONTRACTSTAFF,T.SCRILEVEL,T.STARTDATE,T.ENDDATE,TCPJ.PROJECTNAME AS TOPICNAME,T.CONTRACTTYPE,T.CONTRACTDATETYPE,T.CONTRACTXZ,T.CONTRACTXDFXINFO,T.RECORDTYPE,T.RECORDPARENT,T.HZSUMOWING,T.JIJIATYPE,T.MONEYTYPE,T.DCTYPE,T.CONTRACTBD,T.CONTRACTZD,T.JBSTAFF,T.JBDEPT,T.JBUNIT,T.ZXUNIT,T.CONTRACTCHILDREN,T.CONTRACTPLAN,T.TOPICID,T.TOPICNAME,T.HISCONTRACTSTATUS,T.CHANGETYPE,T.CHANGEDATE,T.CHANGEDESC,T.ISDG,T.COUNTERPARTBANK,T.CONTRACTQUFEN,T.REWORKFLOW,T.ISMOREMILLION,T.FUNDPOOLNAME,T.HISTORYSTATUS,T.PRECONTRACTID FROM TBL_CYHW_UNIT  T LEFT JOIN TBL_CONTRACT_PROJECT TCPJ ON T.TOPICID = TCPJ.PROJECTID where CONTRACTID = #{contractid}")
	TblCyhwUnit findContractById(BigDecimal contractid) throws Exception;

	@Update("UPDATE TBL_CYHW_UNIT SET HISCONTRACTSTATUS = #{historyStatus},HISTORYSTATUS = #{historyStatus},CONTRACTSTATUS = #{currentStatus} WHERE CONTRACTID = #{contractId} ")
	void updateJiuFenContractStatus(BigDecimal historyStatus, Integer currentStatus, BigDecimal contractId) throws Exception;

	@Update("UPDATE TBL_CYHW_UNIT SET HISCONTRACTSTATUS = #{historyStatus},CONTRACTSTATUS = #{currentStatus} WHERE CONTRACTID = #{contractId} ")
	void updateModifyContractStatus(BigDecimal historyStatus, Integer currentStatus, BigDecimal contractId) throws Exception;

	@SelectProvider(method = "findLegalContractListByPageInfo", type = TblCyhwUnitMapperSqlConfig.class)
	IPage<TblCyhwUnit> findLegalContractListByPageInfo(IPage<TblCyhwUnit> page, TblCyhwUnit tcu) throws Exception;

	@SelectProvider(method = "getContractMonth", type = TblCyhwUnitMapperSqlConfig.class)
	List<JSONObject> getContractMonth(String orgid, Integer year) throws Exception;

	@SelectProvider(method = "getAmountMonth", type = TblCyhwUnitMapperSqlConfig.class)
	List<JSONObject> getAmountMonth(String orgid, Integer year);

	// 历月合同付款金额
	@SelectProvider(method = "getPaymentAmount", type = TblCyhwUnitMapperSqlConfig.class)
	List<JSONObject> getPaymentAmount(String orgid, Integer year);

	@SelectProvider(method = "getActualPaymentAmount", type = TblCyhwUnitMapperSqlConfig.class)
	List<JSONObject> getActualPaymentAmount(String orgid, Integer year) throws Exception;

	@SelectProvider(method = "getCollectionAmount", type = TblCyhwUnitMapperSqlConfig.class)
	List<JSONObject> getCollectionAmount(String orgid, Integer year);

	// 历月合同实际收款金额
	@SelectProvider(method = "getActualCollectionAmount", type = TblCyhwUnitMapperSqlConfig.class)
	List<JSONObject> getActualCollectionAmount(String orgid, Integer year);

	// 历月合同实际收款金额
	@SelectProvider(method = "getOrgCollectionAmount", type = TblCyhwUnitMapperSqlConfig.class)
	List<JSONObject> getOrgCollectionAmount(String orgid, Integer year);

	@SelectProvider(method = "getOrgAmount", type = TblCyhwUnitMapperSqlConfig.class)
	List<JSONObject> getOrgAmount(String orgid, Integer year, Integer quarter);

	@SelectProvider(method = "getOrgFactualAmount", type = TblCyhwUnitMapperSqlConfig.class)
	List<JSONObject> getOrgFactualAmount(String orgid, Integer year, Integer quarter);

	@SelectProvider(method = "getPlannedProject", type = TblCyhwUnitMapperSqlConfig.class)
	List<JSONObject> getPlannedProject(String orgid, Integer year, Integer quarter);

	@SelectProvider(method = "getFactualProject", type = TblCyhwUnitMapperSqlConfig.class)
	List<JSONObject> getFactualProject(String orgid, Integer year, Integer quarter);

	@SelectProvider(method = "contractLegalAprStat", type = TblCyhwUnitMapperSqlConfig.class)
	List<JSONObject> contractLegalAprStat(String orgid, Integer year, Integer quarter);

	@SelectProvider(method = "selectCountByContractNo", type = TblCyhwUnitMapperSqlConfig.class)
	Integer selectCountByContractNo(String contractno, String contractnolike);

	@Select("${sql}")
	List<Map<String, Object>> selectDynamicReportData(@Param("sql")String sql) throws Exception;

	@SelectProvider(type = TblCyhwUnitMapperSqlConfig.class, method = "selectUnitListInfo")
	IPage<TblCyhwUnit> selectUnitListInfo(IPage<TblCyhwUnit> page, String orgid, String staffid, Integer paCount,TblCyhwUnit unit,String secrectScopeIds);
	
	@SelectProvider(type = TblCyhwUnitMapperSqlConfig.class, method = "selectUnitListByPageInfo")
	IPage<TblCyhwUnit> selectUnitListByPageInfo(IPage<TblCyhwUnit> page, String staffid, Integer paCount,TblCyhwUnit unit) throws Exception;

	@Delete("DELETE FROM TBL_CONTRACT_BUDGET WHERE CONTRACTID = #{contractid}")
	void deleteContractXDF(BigDecimal contractId);

	@Select("SELECT * FROM  TBL_CONTRACT_BUDGET  WHERE CONTRACTID = #{contractid} AND BUDGETID = #{xdfxId}")
	List<TblContractBudget> selectContractBudget(@Param("contractid")BigDecimal contractid,@Param("xdfxId") String xdfxId);

	@InsertProvider(method = "insertContractBudget", type = TblCyhwUnitMapperSqlConfig.class)
	void insertContractBudget(BigDecimal contractid, String xdfxId, String bugetType, String contractname) throws Exception;

	@InsertProvider(method = "insertContractBudgetOrder", type = TblCyhwUnitMapperSqlConfig.class)
	void insertContractBudgetOrder(BigDecimal contractid, String xdfxId, String bugetType, String contractname,Integer orderby) throws Exception;

	@Delete("DELETE FROM TBL_CONTRACT_BUDGET WHERE CONTRACTID = #{contractid}")
	void deleteContractBudget(BigDecimal contractId);
	
	@Update("UPDATE TBL_CONTRACT_BUDGET SET CONTRACTNAME = #{contractname},BUDGETTYPE = #{bugetType} WHERE CONTRACTID = #{contractid} AND BUDGETID = #{bugetId}")
	void updateContractBudget(@Param("contractid")BigDecimal contractid,@Param("bugetId") String bugetId,@Param("bugetType") String bugetType,@Param("contractname") String contractname) throws Exception;

	@InsertProvider(method = "updateCyhwUnit", type = TblCyhwUnitMapperSqlConfig.class)
	void updateCyhwUnit(TblCyhwUnit tcu) throws Exception;

	@Select("SELECT T.CONTRACTID,T.MATTERORG,T.ISBIGMATTER,T.FLOWID,T.UNITNAME,T.CONTRACTNAME,T.CONTRACTITEM,T.ISMANY,T.AGREEMENTCOUNT,T.CONTRACTNO,T.CONTRACTDEPT,T.TYPEFL,T.CONTRACTLINK,T.CONTRACTMONEY,T.CONTRACTSTATUS,T.LINKDEPT,T.ORGID,T.CREATEUSER,T.CREATETIME,T.MOMOCONCAT,T.RISKCONTROL,T.DESCRIBE,T.CONTRACTSTAFF,T.SCRILEVEL,T.STARTDATE,T.ENDDATE,T.CONTRACTTYPE,T.CONTRACTDATETYPE,T.CONTRACTXZ,T.CONTRACTXDFXINFO,T.RECORDTYPE,T.RECORDPARENT,T.HZSUMOWING,T.JIJIATYPE,T.MONEYTYPE,T.DCTYPE,T.CONTRACTBD,T.CONTRACTZD,T.JBSTAFF,T.JBDEPT,T.JBUNIT,T.ZXUNIT,T.CONTRACTCHILDREN,T.CONTRACTPLAN,T.TOPICID,TCPJ.PROJECTNAME,T.HISCONTRACTSTATUS,T.CHANGETYPE,T.CHANGEDATE,T.CHANGEDESC,T.ISDG,T.COUNTERPARTBANK,T.CONTRACTQUFEN,T.REWORKFLOW,T.ISMOREMILLION,T.FUNDPOOLNAME,o.ORGNAME AS ORGMENO,s.REALNAME,"
			+ "T.PRECONTRACTID,T.SECRECTLEVELID,TSL.LEVELNAME AS SECRECTLEVELNAME,T.STAFFSCOPEIDS,T.STAFFSCOPENAMES,c.COUNTERPARTNO,c.BUDGETNAME,c.TOTALTMONEY,c.COUNTERPARTTYPE,c.PROJECTSTAGEGOAL,c.COUNTERPARTADDRESS,c.COUNTERPARTPHONE,c.COUNTERPARTCODE,"
			+ "oz.ORGNAME,TORG.ORGNAME AS JBORGNAME,cb.BANKID,cb.BANKACCOUNT,cb.BANKKHYH,ENTRUST.REALNAME ENTRUSTSTAFFNAME,ENTRUST.STAFFID ENTRUSTSTAFFID "
			+ " FROM TBL_CYHW_UNIT T left join TBL_ORGANIZATION o on t.CONTRACTDEPT = o.ORGID "
			+ " left join TBL_STAFF s on t.CONTRACTSTAFF = s.STAFFID "
			+ " left join TBL_CYHW_PROJECTBUDGET c on t.CONTRACTXDFXINFO = c.BUDGETID "
			+ " left join TBL_ORGANIZATION oz on t.ZXUNIT = oz.ORGID "
			+ " LEFT JOIN TBL_ORGANIZATION TORG ON t.JBUNIT = TORG.ORGID"
			+ " left join TBL_COUNTERPART_BANKINFO cb on t.COUNTERPARTBANK = cb.BANKID "
			+ " LEFT JOIN TBL_CONTRACT_PROJECT TCPJ ON T.TOPICID = TCPJ.PROJECTID "
			+ " LEFT JOIN TBL_STAFF ENTRUST ON ENTRUST.STAFFID=T.ENTRUSTSTAFFID"
			+ " LEFT JOIN TBL_SECRECT_LEVEL TSL ON T.SECRECTLEVELID = TSL.LEVELID "
			+ " WHERE t.CONTRACTID = #{contractId}")
	@Results({ @Result(column = "BANKKHYH", property = "bankinfo.bankkhyh"),
			@Result(column = "BANKID", property = "bankinfo.bankid"),
			@Result(column = "BANKACCOUNT", property = "bankinfo.bankaccount"),
			@Result(column = "BANKKHYH", property = "bankinfo.bankkhyh"),
			@Result(column = "PROJECTNAME", property = "topicname"),
			@Result(column = "ENTRUSTSTAFFNAME", property = "entrustStaffName"),
			@Result(column = "SECRECTLEVELID", property = "secrectLevelId"),
			@Result(column = "SECRECTLEVELNAME", property = "secrectLevelName"),
			@Result(column = "STAFFSCOPEIDS", property = "staffScopeIds"),
			@Result(column = "STAFFSCOPENAMES", property = "staffScopeNames"),
			@Result(column = "JBORGNAME", property = "jborgName"),})  
	TblCyhwUnit getCyhwUnitEntity(BigDecimal contractId);

	@Update("UPDATE TBL_CYHW_UNIT SET HISCONTRACTSTATUS=CONTRACTSTATUS,CONTRACTSTATUS = #{goalStatus} WHERE CONTRACTID =  #{contractId}")
	void updaContractStatus(String contractId, int goalStatus);
	
	@Update("UPDATE TBL_CYHW_UNIT SET HISCONTRACTSTATUS='',CONTRACTSTATUS = #{goalStatus} WHERE CONTRACTID =  #{contractId}")
	void repaContractStatus(String contractId, int goalStatus);

	@Insert("INSERT INTO TBL_CYHW_UNIT_ATT(CONTRACTID,ATTID) VALUES(#{contractid},#{attId})")
	void insertAttmentRelation(@Param("attId") String id, @Param("contractid") BigDecimal contractid);

	@Select("SELECT CONTRACTSTATUS FROM TBL_CYHW_UNIT WHERE CONTRACTID = #{contractId}")
	Integer selectStatueById(BigDecimal contractId);

	@Delete("DELETE FROM TBL_CONTRACT_PLANNODE WHERE PROJECTID = #{contractId}")
	void deletePlannode(BigDecimal contractId);

	@Delete("DELETE FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_CYHW_UNIT_ATT WHERE CONTRACTID = #{contractId}) ")
	void deleteattachment(BigDecimal contractId);

	@Delete("DELETE FROM TBL_CYHW_UNIT_ATT WHERE CONTRACTID = #{contractId}")
	void deleteUnitaTT(BigDecimal contractId);

	@Delete("DELETE FROM TBL_CONTRACT_INFORMATION WHERE PROJECTID = #{contractId}")
	void deleteInformation(BigDecimal contractId);

	@Delete("DELETE FROM TBL_CYHW_UNIT WHERE CONTRACTID = #{contractId}")
	void deleteunit(BigDecimal contractId);

	@SelectProvider(type = TblCyhwUnitMapperSqlConfig.class, method = "findListByPageInfo")
	IPage<TblCyhwUnit> findListByPageInfo(IPage<TblCyhwUnit> page, TblCyhwUnit tcu, String orgId);

	@Select("SELECT CONTRACTMONEY FROM TBL_CYHW_UNIT WHERE CONTRACTID =#{contractId}")
	BigDecimal findContractMoneyByContractId(BigDecimal contractId);

	@Select("SELECT * FROM TBL_CYHW_UNIT WHERE  CONTRACTID = #{contractId}")
	TblCyhwUnit selectByContractId(BigDecimal contractId);

	@Select("SELECT CONTRACTID,FLOWID,CONTRACTNAME,CONTRACTNO,CONTRACTMONEY,CONTRACTTYPE,CREATETIME,RECORDTYPE,RECORDPARENT,PRECONTRACTID FROM TBL_CYHW_UNIT WHERE CONTRACTID = #{contractid}")
	TblCyhwUnit findAllParentList(BigDecimal contractid);

	@Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_CYHW_UNIT_ATT WHERE contractId = #{contractId})")
	List<TblAttachment> findContractSealFileList(@Param("orgid") BigDecimal orgid,
			@Param("contractId") BigDecimal contractId);

	@Select("SELECT contractId,MOMOCONCAT,describe,contractName  FROM TBL_CYHW_UNIT WHERE recordType = 'HTGL007' AND contractType = #{contractType} AND orgid = #{orgid} AND contractStatus = 6")
	List<TblCyhwUnit> selectContractTemp(BigDecimal orgid, String contractType);

	@SelectProvider(type = TblCyhwUnitMapperSqlConfig.class, method = "findContractSealList")
	IPage<TblCyhwUnit> findContractSealList(IPage<TblCyhwUnit> page, TblCyhwUnit unit) throws Exception;

	@Select("SELECT T.*,TCPJ.PROJECTNAME AS TOPICNAME,oz.ORGNAME FROM TBL_CYHW_UNIT T "
			+ " left join TBL_ORGANIZATION oz on T.ZXUNIT = oz.ORGID "
			+ " LEFT JOIN TBL_CONTRACT_PROJECT TCPJ ON T.TOPICID = TCPJ.PROJECTID "
			+ " WHERE CONTRACTID = #{recordparent}")
	@Results({ @Result(column = "ORGNAME", property = "orgname"), })
	TblCyhwUnit getEntity(BigDecimal recordparent);

	@SelectProvider(type = TblCyhwUnitMapperSqlConfig.class, method = "findeFilContractList")
	IPage<TblCyhwUnit> findeFilContractList(IPage<TblCyhwUnit> page, TblCyhwUnit unit);

	@SelectProvider(type = TblCyhwUnitMapperSqlConfig.class, method = "findChooseLendContractList")
	IPage<TblCyhwUnit> findChooseLendContractList(IPage<TblCyhwUnit> page, TblCyhwUnit unit) throws Exception;

	@Update("UPDATE TBL_CYHW_UNIT SET CONTRACTSTATUS = #{status},BINDNO = #{bindno} WHERE CONTRACTID =  #{contractid} ")
	void modifyContractStatus(@Param("contractid")BigDecimal contractid,@Param("status") String status,@Param("bindno")String bindno);

	@SelectProvider(type = TblCyhwUnitMapperSqlConfig.class, method = "findContractTranList")
	IPage<TblContractTran> findContractTranList(IPage<TblContractTran> page, TblContractTran tct);

	@Select("SELECT * FROM TBL_CYHW_UNIT WHERE CONTRACTID = #{contractid}")
	TblCyhwUnit selectAllInfoById(BigDecimal contractid) throws Exception;

	@UpdateProvider(method = "updateTblContractTran", type = TblCyhwUnitMapperSqlConfig.class)
	void updateTblContractTran(TblContractTran tct) throws Exception;

	@InsertProvider(method = "insertTblContractTran", type = TblCyhwUnitMapperSqlConfig.class)
	@Options(useGeneratedKeys = true, keyProperty = "tranId", keyColumn = "TRANID")
	void insertTblContractTran(TblContractTran tct) throws Exception;

	@Select("SELECT TCT.*,TC.FLOWID,TC.RECORDTYPE FROM  TBL_CONTRACT_TRAN tct LEFT JOIN TBL_CYHW_UNIT tc on TCT.CONTRACTID=TC.CONTRACTID  WHERE tct.tranId =#{tranId}")
	TblContractTran findTblContractTran(BigDecimal tranId);

	@SelectProvider(type = TblCyhwUnitMapperSqlConfig.class, method = "findSealedContractList")
	IPage<TblCyhwUnit> findSealedContractList(IPage<TblCyhwUnit> page, TblCyhwUnit unit) throws Exception;

	@Delete("DELETE FROM TBL_CONTRACT_TRAN WHERE tranId = #{tranId}")
	void delTblContractTran(BigDecimal tranId);

	@SelectProvider(type = TblCyhwUnitMapperSqlConfig.class, method = "findeContractListByContractStaff")
	IPage<TblCyhwUnit> findeContractListByContractStaff(IPage<TblCyhwUnit> page, TblCyhwUnit unit);

	@Update("UPDATE TBL_CYHW_UNIT SET CONTRACTSTAFF = #{staffId} WHERE CONTRACTID = #{contractId}")
	void updateContractStaff(String staffId, String contractId) throws Exception;

	@SelectProvider(type = TblCyhwUnitMapperSqlConfig.class, method = "findeContractListPerformanceTracking")
	IPage<TblCyhwUnit> findeContractListPerformanceTracking(IPage<TblCyhwUnit> page, TblCyhwUnit unit);

	@SelectProvider(type = TblCyhwUnitMapperSqlConfig.class, method = "findFulfillmentContract")
	IPage<TblCyhwUnit> findFulfillmentContract(IPage<TblCyhwUnit> page, TblCyhwUnit unit);

	@Select("SELECT DESCRIBE FROM TBL_CYHW_UNIT WHERE CONTRACTID = #{budgetId}")
	String findContractDes(String budgetId);

	@Select("SELECT DESCRIBE FROM TBL_CYHW_UNIT WHERE CONTRACTID = #{contractId}")
	String getCyhwUnitDescribe(String contractId);

	@Update("UPDATE TBL_CYHW_UNIT SET DESCRIBE = #{describe} WHERE CONTRACTID = #{contractid}")
	void updateContractDescribe(TblCyhwUnit entity);

	@Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE from TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_CYHW_UNIT_ATT WHERE CONTRACTID =#{contractid} )")
	List<TblAttachment> findeFileInfo(BigDecimal contractid);

	@SelectProvider(method = "getList", type = TblCyhwUnitMapperSqlConfig.class)
	List<JSONObject> getList(String staffid, String startDate,String orgid);

	@Select("SELECT * FROM  TBL_CYHW_UNIT tcu  WHERE tcu.CONTRACTNO =#{contractno}")
	TblCyhwUnit findContractByContractno(String contractno);

	@InsertProvider(type = TblCyhwUnitMapperSqlConfig.class, method = "insertCyhwUnit")
	@Options(useGeneratedKeys = true, keyProperty = "contractid", keyColumn = "CONTRACTID")
	void insertCyhwUnit(TblCyhwUnit tcu);

	@Select("SELECT COUNT(0) FROM TBL_CYHW_UNIT WHERE CONTRACTNO LIKE '${contractNo}%'")
	Integer selectContractNoCountByNo(@Param("contractNo")String contractNo) throws Exception;

	@Select("SELECT COUNT(0) FROM TBL_CYHW_UNIT WHERE BINDNO LIKE '${no}%'")
	Integer selectCountByArchiveCount(String no);
	
	@SelectProvider(type = TblCyhwUnitMapperSqlConfig.class, method = "findLedgerListOrgForExport")
	@Results({ 
		@Result(property = "contractid", column = "CONTRACTID"),
		@Result(property = "contractno", column = "CONTRACTNO"),
			@Result(property = "contractname", column = "CONTRACTNAME"),
			@Result(property = "recordConcatname", column = "recordConcatname"),
			@Result(property = "contracttype", column = "CONTRACTTYPE"),
			@Result(property = "contractitem", column = "CONTRACTITEM"),
			@Result(property = "dctype", column = "DCTYPE"),
			@Result(property = "contractmoney", column = "CONTRACTMONEY"),
			@Result(property = "createtime", column = "CREATETIME"),
			@Result(property = "startdate", column = "STARTDATE"), @Result(property = "enddate", column = "ENDDATE"),
			@Result(property = "choicejbunitid", column = "choicejbunitid"),
			@Result(property = "choicecontractDeptId", column = "choicecontractDeptId"),
			@Result(property = "topic", column = "topic"), @Result(property = "nodeCount", column = "nodeCount"),
			@Result(property = "yongyintime", column = "yongyintime"),
			@Result(property = "contractstatus", column = "CONTRACTSTATUS"),
			@Result(property = "topicname", column = "TOPICNAME"), @Result(property = "orgname", column = "ORGNAME"),
			@Result(property = "isWy", column = "ISWY"), })
	List<TblCyhwUnit> findLedgerListOrgForExport(TblCyhwUnit unit, String allCompanyIds);

}
