package com.huabo.contract.mapper;


import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.ResultType;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.PageInfo;
import com.huabo.contract.entity.TblAttachment;
import com.huabo.contract.entity.TblCyhwProjectbudget;
import com.huabo.contract.entity.TblCyhwUnit;
import com.huabo.contract.mappersql.TblCyhwProjectbudgetMapperSqlConifg;
import com.huabo.contract.mappersql.TblCyhwUnitMapperSqlConfig;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2021-10-23
 */
public interface TblCyhwProjectbudgetMapper extends BaseMapper<TblCyhwProjectbudget> {

	@SelectProvider(method="selectListByPageInfo",type=TblCyhwProjectbudgetMapperSqlConifg.class)
	@Results(id = "setBlackRecord" ,value = {
		@Result(column = "BRID" , property = "blackRecord.brid" , id = true),
		@Result(column = "OPPOID" , property = "blackRecord.oppoid"),
		@Result(column = "VERSION" , property = "blackRecord.version"),
		@Result(column = "OBRTYPE" , property = "blackRecord.obrtype"),
		@Result(column = "BLACKDEADTIME" , property = "blackRecord.blackdeadtime"),
		@Result(column = "APRSTATUS" , property = "blackRecord.aprstatus"),
		@Result(column = "BACKREASON" , property = "blackRecord.backreason"),
		@Result(column = "TORCT" , property = "blackRecord.createtime"),
		@Result(column = "TORCR" , property = "blackRecord.createstaff")
	})
	IPage<TblCyhwProjectbudget> selectListByPageInfo(IPage<TblCyhwProjectbudget> page, TblStaffUtil staff,Integer taskCount,
			TblCyhwProjectbudget budget) throws Exception;

	@SelectProvider(method="selectRepearBudgetName",type=TblCyhwProjectbudgetMapperSqlConifg.class)
	Integer selectRepearBudgetName(String budgetname, BigDecimal orgid, BigDecimal budgetId) throws Exception;

	@Insert("INSERT INTO TBL_CYHW_PROJECTBUDGET_ATT(BUDGETID,ATTID) VALUES(#{buegetId},#{attId})")
	void insertAttmentRelation(@Param("attId")String id, @Param("buegetId")BigDecimal budgetid) throws Exception;

	@Select("SELECT INSPECTIONSTATUS FROM TBL_CYHW_PROJECTBUDGET WHERE BUDGETID = #{budgetId}")
	Integer selectStatusByBudgetId(@Param("budgetId") BigDecimal budgetId) throws Exception;

	@Select("SELECT TCP.*,TFL.FLOWID,TFL.FLOWNUMBER,TFL.FLOWNAME,TFL.SETTINGID,TFL.EDITMODULE,TFL.FLOWMAPPINGURL,TCU.CONTRACTNAME CONTRACTNAME,TCU.CONTRACTID CONTRACTID,CTS.STAFFID CREATESTAFFID,CTS.REALNAME CREATEUSERNAME,LINKORG.ORGID LINKORGID,LINKORG.ORGNAME LINKORGNAME,RELADEPT.ORGID RELADEPTID,RELADEPT.ORGNAME RELADEPTNAME ,REPORTDEPT.ORGID REPORTDEPTID,REPORTDEPT.ORGNAME REPORTDEPTNAME"
			+ " FROM TBL_CYHW_PROJECTBUDGET TCP LEFT JOIN TBL_FLOW TFL ON TCP.FLOWID = TFL.FLOWID LEFT JOIN TBL_STAFF CTS ON TCP.CREATEUSER = CTS.STAFFID LEFT JOIN TBL_ORGANIZATION LINKORG ON LINKORG.ORGID = TCP.ORGID LEFT JOIN TBL_ORGANIZATION RELADEPT ON RELADEPT.ORGID = TCP.LINKDEPR LEFT JOIN TBL_ORGANIZATION REPORTDEPT ON REPORTDEPT.ORGID = TCP.REPORTTODEPT LEFT JOIN TBL_CYHW_UNIT TCU ON TCP.RECORDPARENT = TCU.CONTRACTID " +
			" WHERE TCP.BUDGETID = #{budgetId}")
	@Results({
			@Result(column="CREATESTAFFID",property="createStaff.staffid",id=true),
			@Result(column="CREATEUSERNAME",property="createStaff.username"),
			@Result(column="LINKORGID",property="linkOrg.orgid",id=true),
			@Result(column="LINKORGNAME",property="linkOrg.orgname"),
			@Result(column="RELADEPTID",property="relaDept.orgid",id=true),
			@Result(column="RELADEPTNAME",property="relaDept.orgname"),
			@Result(column="REPORTDEPTID",property="reportDept.orgid",id=true),
			@Result(column="REPORTDEPTNAME",property="reportDept.orgname"),
			@Result(column="CONTRACTID",property="recordUnit.contractid",id=true),
			@Result(column="CONTRACTNAME",property="recordUnit.contractname"),
			@Result(column="FLOWID",property="flowInfo.flowid",id=true),
			@Result(column="FLOWNUMBER",property="flowInfo.flownumber"),
			@Result(column="FLOWNAME",property="flowInfo.flowname"),
			@Result(column="SETTINGID",property="flowInfo.settingid"),
			@Result(column="EDITMODULE",property="flowInfo.editmodule"),
			@Result(column="FLOWMAPPINGURL",property="flowInfo.flowmappingurl"),
	})
	TblCyhwProjectbudget selectAllInfoById(@Param("budgetId") BigDecimal budgetId) throws Exception;

	@Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_CYHW_PROJECTBUDGET_ATT WHERE BUDGETID = #{budgetId})")
	List<TblAttachment> selectAllATT(BigDecimal budgetId);

	@Delete("DELETE FROM TBL_CYHW_PROJECTBUDGET_ATT WHERE BUDGETID = #{budgetId}")
	void deleteRelaFileInfo(@Param("budgetId") BigDecimal budgetId) throws Exception;

	@Delete("DELETE FROM TBL_CYHW_PROJECTBUDGET WHERE BUDGETID = #{budgetId}")
	void deleteOppsiteInfoByBudtetId(@Param("budgetId") BigDecimal budgetId) throws Exception;

	@Select("select count(*) from TBL_CYHW_PROJECTBUDGET where recordtype='HTGL001' AND  budgetid in (select CONTRACTXDFXINFO from tbl_cyhw_unit where  recordtype IN ('HTGL002','HTGL005') and CONTRACTXDFXINFO=#{budgetId})")
	Integer checkOppositeDelete(@Param("budgetId")String budgetId) throws Exception;

	@Select("SELECT * FROM TBL_CYHW_PROJECTBUDGET WHERE BUDGETID = #{recordparent}")
	TblCyhwProjectbudget getEntity(BigDecimal recordparent) throws Exception;

	@SelectProvider(method="selectListByPageInfoFlowid",type=TblCyhwProjectbudgetMapperSqlConifg.class)
	@ResultMap("setBlackRecord")
	IPage<TblCyhwProjectbudget> selectListByPageInfoFlowid(IPage<TblCyhwProjectbudget> page, TblCyhwProjectbudget budget,
			TblStaffUtil staff, Integer taskCount, String flowid) throws Exception;

	@SelectProvider(method="selectOppsiteBlackList",type=TblCyhwProjectbudgetMapperSqlConifg.class)
	@Results({
		@Result(column = "RBID" , property = "removeRecord.rbid" , id = true),
		@Result(column = "REMREASON" , property = "removeRecord.remreason"),
		@Result(column = "RMSTATUS" , property = "removeRecord.rmstatus"),
		@Result(column = "BRID" , property = "blackRecord.brid" , id = true),
		@Result(column = "OPPOID" , property = "blackRecord.oppoid"),
		@Result(column = "VERSION" , property = "blackRecord.version"),
		@Result(column = "OBRTYPE" , property = "blackRecord.obrtype"),
		@Result(column = "BLACKDEADTIME" , property = "blackRecord.blackdeadtime"),
		@Result(column = "APRSTATUS" , property = "blackRecord.aprstatus"),
		@Result(column = "BACKREASON" , property = "blackRecord.backreason"),
		@Result(column = "TORCT" , property = "blackRecord.createtime"),
		@Result(column = "TORCR" , property = "blackRecord.createstaff")
	})
	IPage<TblCyhwProjectbudget> selectOppsiteBlackList(IPage<TblCyhwProjectbudget> page, TblCyhwProjectbudget tcb,
			TblStaffUtil staff, boolean taskCount) throws Exception ;

	@Select("SELECT * FROM TBL_CYHW_PROJECTBUDGET WHERE BUDGETID = #{budgetid}")
	String excuteFunReturnUnique(BigDecimal budgetid) throws Exception;

	@Update("UPDATE TBL_CYHW_PROJECTBUDGET SET ISBLACK = 2,BLACKTYPE = '',EFFECTDATE='',BLACKAPRSTATUS = 0 WHERE BUDGETID = #{budgetid}")
	void executeSql(BigDecimal budgetid, Object o) throws Exception;

	@Delete("DELETE FROM TBL_SYSTEM_FORMFLOW WHERE FLOWID IN (SELECT YMWORKFROM FROM TBL_SYSTEM_YMWORK WHERE TABLEID = 3) AND FORMID = #{budgetid}")
	void deleteBalckFlowInf(BigDecimal budgetid) throws Exception;

	
	@SelectProvider(method="selectBlackList",type=TblCyhwProjectbudgetMapperSqlConifg.class)
	@Results({
			@Result(column="COUNTERPARTNO",property="counterpartno"),
			@Result(column="BUDGETNAME",property="budgetname"),
			@Result(column="PROJECTSTAGEGOAL",property="projectstagegoal"),
			@Result(column="SERVICETYPE",property="servicetype"),
			@Result(column="TOTALTMONEY",property="totaltmoney"),
			@Result(column="ISBLACK",property="financemoney"),
			@Result(column="BLACKTYPE",property="othermoney"),
	})	
	List<TblCyhwProjectbudget> selectBlackList(BigDecimal orgid, TblStaffUtil staff, boolean taskCount, TblCyhwProjectbudget tcb) throws Exception;

	@SelectProvider(method="findCollectionChoiceContractPid",type=TblCyhwProjectbudgetMapperSqlConifg.class)
	IPage<TblCyhwProjectbudget> findCollectionChoiceContractPid(IPage<TblCyhwProjectbudget> page,TblCyhwProjectbudget tcpb) throws Exception;

	@Select("SELECT " +
			"tcp.* " +
			"FROM " +
			"TBL_CONTRACT_BUDGET tcb " +
			"LEFT JOIN TBL_CYHW_PROJECTBUDGET tcp ON " +
			"tcb.BUDGETID = tcp.BUDGETID " +
			"WHERE " +
			"tcb.CONTRACTID = #{congractId} " +
			"ORDER BY " +
			"TCP.CREATETIME ASC")
	List<TblCyhwProjectbudget> getContractBudgetList(BigDecimal contractId) throws Exception;

	@SelectProvider(method="findListByPageInfo",type=TblCyhwProjectbudgetMapperSqlConifg.class)
	IPage<TblCyhwProjectbudget> findListByPageInfo(IPage<TblCyhwProjectbudget> page, String orgId,TblCyhwProjectbudget tcpb);

	@Select("SELECT BUDGETNAME,BUDGETID FROM TBL_CYHW_PROJECTBUDGET TP   "
			+ "WHERE TP.BUDGETID IN(${budgetId})")
	List<TblCyhwProjectbudget> getEntityBudget(@Param("budgetId")String budgetId,@Param("nodeId") BigDecimal nodeId);

	@Select("SELECT TP.BUDGETID,BUDGETNAME,COUNTERPARTNO,TOTALTMONEY,PROJECTSTAGEGOAL,TC.BUDGETTYPE FROM TBL_CYHW_PROJECTBUDGET TP  "
			+ "RIGHT JOIN TBL_CONTRACT_BUDGET TC ON TP.BUDGETID = TC.BUDGETID  "
			+ "WHERE TC.CONTRACTID = #{congractId} ORDER BY TC.ORDERBY")
	List<TblCyhwProjectbudget> seleCyhwProjectbudgets(BigDecimal congractId);

	@Select("SELECT TCP.*,TS.*,SEALORG.ORGNAME SEALORGNAME FROM TBL_CYHW_PROJECTBUDGET TCP " +
			"LEFT JOIN TBL_STAFF TS ON TCP.CREATEUSER = TS.STAFFID " +
			"LEFT JOIN TBL_ORGANIZATION SEALORG ON SEALORG.ORGID = TCP.SEALORGID " +
			"WHERE TCP.BUDGETID = #{budgetId}")
	TblCyhwProjectbudget findBybudgetId(BigDecimal budgetId);

	@UpdateProvider(method="updateOppositeInfoById",type=TblCyhwProjectbudgetMapperSqlConifg.class)
	void updateOppositeInfoById(TblCyhwProjectbudget tcpb,BigDecimal sealorgid) throws Exception;

	@InsertProvider(method="insertOppositeParty",type=TblCyhwProjectbudgetMapperSqlConifg.class)
	void insertOppositeParty(TblCyhwProjectbudget tcpb,BigDecimal sealorgid) throws Exception;

	@Select("SELECT * FROM  TBL_CYHW_PROJECTBUDGET tcp  WHERE counterpartno=#{bmbh}")
	TblCyhwProjectbudget findByBmbh(String bmbh);

	@Update("UPDATE TBL_CYHW_PROJECTBUDGET SET counterparttype = #{counterparttype},counterparttype = #{counterparttype}"
			+ "counterpartaddress = #{counterpartaddress},servicetype = #{servicetype},projectgoal = #{projectgoal}"
			+ "contactsadress = #{contactsadress},Oppositenature = #{Oppositenature},counterparttype = #{counterparttype}"
			+ "recordtype = #{recordtype},counterpartphone = #{counterpartphone} WHERE counterpartno = #{counterpartno}")
	void updateBybmbh(TblCyhwProjectbudget entity) throws Exception;

	@Select("SELECT * FROM TBL_CYHW_PROJECTBUDGET WHERE BUDGETNAME = #{budgetname}")
	List<TblCyhwProjectbudget> selectCountByOutSideIdCompany(String budgetname) throws Exception;

	@InsertProvider(method="insertOppositePartyOld",type=TblCyhwProjectbudgetMapperSqlConifg.class)
	@Options(useGeneratedKeys=true, keyProperty="budgetid", keyColumn="BUDGETID")
	void insertOppositePartyOld(TblCyhwProjectbudget tcpb) throws Exception;

	@SelectProvider(method="findAutoNumber",type=TblCyhwProjectbudgetMapperSqlConifg.class)
	Integer findAutoNumber(String counterpartno, BigDecimal orgid);

	@SelectProvider(type = TblCyhwProjectbudgetMapperSqlConifg.class, method = "selectListByUnitExport")
	@Results({
		@Result(column="CONTRACTID",property="recordparent"),
		@Result(column="BUDGETID",property="budgetid"),
		@Result(column="BUDGETNAME",property="budgetname"),
	})	
	List<TblCyhwProjectbudget> selectListByUnitExport(TblCyhwUnit unit, String allCompanyIds) throws Exception;

	@SelectProvider(type = TblCyhwProjectbudgetMapperSqlConifg.class, method = "selectOppsiteNamesByUnitTaiZhangExport")
	@Results({
		@Result(column="CONTRACTID",property="recordparent"),
		@Result(column="BUDGETID",property="budgetid"),
		@Result(column="BUDGETNAME",property="budgetname"),
	})
	List<TblCyhwProjectbudget> selectOppsiteNamesByUnitTaiZhangExport(TblCyhwUnit unit, String fatherOrgIds) throws Exception;

	@SelectProvider(type = TblCyhwProjectbudgetMapperSqlConifg.class, method = "selectOppsiteNamesByUnitTaiZhang")
	@Results({
		@Result(column="CONTRACTID",property="recordparent"),
		@Result(column="BUDGETID",property="budgetid"),
		@Result(column="BUDGETNAME",property="budgetname"),
	})
	List<TblCyhwProjectbudget> selectOppsiteNamesByUnitTaiZhang(String idStrs) throws Exception;

}
