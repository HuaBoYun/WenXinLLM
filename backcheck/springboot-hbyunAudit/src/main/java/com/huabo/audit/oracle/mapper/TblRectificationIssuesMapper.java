package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.hbfk.util.PageInfo;
import com.huabo.audit.oracle.entity.TblRectificationIssues;
import com.huabo.audit.oracle.vo.TblRectificationIssuesVo;
import com.huabo.audit.oracle.vo.TblZgzzIssuesilistVo;
import com.huabo.audit.vo.param.MaxNumberParam;

import tk.mybatis.mapper.common.Mapper;

/**
 * <p>
 * 业务表单多对多关联组织或用户的中间关系表 Mapper 接口
 * </p>
 *
 * @author LHP
 * @since 2023-11-24
 */
public interface TblRectificationIssuesMapper extends Mapper<TblRectificationIssues> {

	/**
	 * 根据整改方案主键查询获取 该放下的所有整改清单列表，保留最大版本
	 * @param issues
	 * @return
	 * @throws Exception
	 */
	@SelectProvider(method = "selectListByExample" , type = TblRectificationIssuesMapperSqlConfig.class)
	@Results({
		  @Result(column="RELAID",property="relaId"),
		  @Result(column="RECTIFICATIONPLAN",property="rectificationPlan"),
		  @Result(column="DEADLINE",property="deadline"),
		  @Result(column="PLANID",property="planId"),
		  @Result(column="RESPONSIBLEPERSON",property="responsiblePerson"),
		  @Result(column="RESPONSIBLEPERSONNAME",property="responsiblePersonName"),
		  @Result(column="RESPONSIBLEDEPT",property="responsibleDept"),
		  @Result(column="RESPONSIBLEDEPTNAME",property="responsibleDetpName"),
		  @Result(column="IMPLEMENTER",property="implementer"),
		  @Result(column="IMPLEMENTERNAME",property="implementerName"),
		  @Result(column="ISSUESID",property="issuesId"),
		  @Result(column="ISSUESID",property="issues.issuesId",id=true),
		  @Result(column="ISSUESCODE",property="issues.issuesCode"),
		  @Result(column="ISSUESNAME",property="issues.issuesName"),
		  @Result(column="CREATESTAFF",property="issues.createStaff"),
		  @Result(column="CREATESTAFFNAME",property="issues.createStaffName"),
		  @Result(column="ISSUESITEM",property="issues.issuesItem"),
		  @Result(column="ISSUESTITLE",property="issues.issuesTitle"),
		  @Result(column="QUESTIONMEMO",property="issues.questionMemo"),
		  @Result(column="CREATETIME",property="issues.createTime"),
		  
	})
	List<TblRectificationIssuesVo> selectListByExample(TblRectificationIssuesVo issues) throws Exception;

	@Select("SELECT RELAID FROM TBL_RECTIFICATION_ISSUES WHERE PLANID = #{planId} AND ISSUESID = #{issuesId}")
	List<String> selectIdByIssuesIdPlanId(@Param("planId") String planId,@Param("issuesId") String issuesId) throws Exception;

	@Select("SELECT TRI.*,HST.REALNAME AS HANDLERNAME,IST.REALNAME AS IMPLEMENTERNAME,RST.REALNAME AS RESPONSIBLEPERSONNAME,RORG.ORGNAME AS RESPONSIBLEDEPTNAME FROM TBL_RECTIFICATION_ISSUES TRI"
			+ " LEFT JOIN TBL_STAFF HST ON TRI.HANDLERID = HST.STAFFID LEFT JOIN TBL_STAFF IST ON TRI.IMPLEMENTER = IST.STAFFID LEFT JOIN TBL_STAFF RST ON TRI.RESPONSIBLEPERSON = RST.STAFFID"
			+ " LEFT JOIN TBL_ORGANIZATION RORG ON RORG.ORGID = TRI.RESPONSIBLEDEPT WHERE RELAID = #{relaId}")
	TblRectificationIssuesVo selectEntityById(@Param("relaId")String relaId);

	@SelectProvider(method = "selectPageInfoListByRectificaionAllocation" , type = TblRectificationIssuesMapperSqlConfig.class)
	@Results({
		  @Result(column="RELAID",property="relaId"),
		  @Result(column="RECTIFICATIONPLAN",property="rectificationPlan"),
		  @Result(column="DEADLINE",property="deadline"),
		  @Result(column="TRISTATUS",property="status"),
		  @Result(column="RESPONSIBLEPERSON",property="responsiblePerson"),
		  @Result(column="RESPONSIBLEPERSONNAME",property="responsiblePersonName"),
		  @Result(column="RESPONSIBLEDEPT",property="responsibleDept"),
		  @Result(column="RESPONSIBLEDEPTNAME",property="responsibleDetpName"),
		  @Result(column="IMPLEMENTER",property="implementer"),
		  @Result(column="IMPLEMENTERNAME",property="implementerName"),
		  @Result(column="ISSUESID",property="issues.issuesId",id=true),
		  @Result(column="ISSUESCODE",property="issues.issuesCode"),
		  @Result(column="ISSUESNAME",property="issues.issuesName"),
		  @Result(column="CREATESTAFF",property="issues.createStaff"),
		  @Result(column="CREATESTAFFNAME",property="issues.createStaffName"),
		  @Result(column="ISSUESITEM",property="issues.issuesItem"),
		  @Result(column="ISSUESTITLE",property="issues.issuesTitle"),
		  @Result(column="QUESTIONMEMO",property="issues.questionMemo"),
		  @Result(column="EVALID",property="valua.evalId",id=true),
		  @Result(column="STATUS",property="valua.status"),
		  @Result(column="RESULTSTATUS",property="valua.resultStatus"),
		  @Result(column="IMPLID",property="valua.implId"),
		  @Result(column="PLANID",property="plan.planId",id=true),
		  @Result(column="PLANCODE",property="plan.planCode"),
		  @Result(column="PLANNAME",property="plan.planName"),
		  @Result(column="TZRISTATUS",property="reimpl.status"),
	})
	List<TblRectificationIssuesVo> selectPageInfoListByRectificaionAllocation(TblRectificationIssuesVo issuesVo);

	@Select("SELECT COUNT(0) FROM TBL_RECTIFICATION_ISSUES TRI "
			+ " INNER JOIN ( SELECT ISSUESID,PLANID,MAX(VERSION) AS VERSION FROM TBL_RECTIFICATION_ISSUES GROUP BY ISSUESID,PLANID ) T1 ON TRI.ISSUESID = T1.ISSUESID AND TRI.PLANID = T1.PLANID AND TRI.VERSION = T1.VERSION "
			+ " WHERE TRI.PLANID = #{planId} AND TRI.IMPLEMENTER IS NULL")
	Integer selectUnassignedRecordCount(@Param("planId") String planId) throws Exception;

	@SelectProvider(method = "selectMyRectificationPageInfoList" , type = TblRectificationIssuesMapperSqlConfig.class)
	@Results({
		 @Result(column="AUDITOBJECTNAME",property="issues.auditObjectName"),
		 @Result(column="PROJECTNO",property="issues.projectNo"),
		 @Result(column="PROJECTNAME",property="issues.projectName"),
		 @Result(column="ISSUESCODE",property="issues.issuesCode"),
		 @Result(column="ISSUESNAME",property="issues.issuesName"),
		 @Result(column="PLANCODE",property="plan.planCode"),
		 @Result(column="PLANNAME",property="plan.planName"),
		 @Result(column="TZRVEVALID",property="valua.evalId"),
		 @Result(column="TZRVSTATUS",property="valua.status"),
		 @Result(column="TZRVRSTATUS",property="valua.resultStatus"),
		 @Result(column="TZRISTATUS",property="reimpl.status"),
	})
	List<TblRectificationIssuesVo> selectMyRectificationPageInfoList(TblRectificationIssuesVo condition);

	/**
	 * 根据整改落实主键 获取 整改方案与整改清单关系表的主键
	 * @param implId 整改落实信息主键
	 * @return
	 * @throws Exception
	 */
	@Select("SELECT RELAID FROM TBL_ZGZZ_RECTIFICATIONIMPL WHERE IMPLID = #{implId}")
	String selectIdByImplId(@Param("implId")String implId) throws Exception;

	@SelectProvider(method = "selectRectificationIssuesLedgetPageInfoList" , type = TblRectificationIssuesMapperSqlConfig.class)
	@Results({
		 @Result(column="ISSUESID",property="issues.issuesId"),
		 @Result(column="ISSUESCODE",property="issues.issuesCode"),
		 @Result(column="ISSUESNAME",property="issues.issuesName"),
		 @Result(column="CREATESTAFF",property="issues.createStaff"),
		 @Result(column="ISSUESITEM",property="issues.issuesItem"),
		 @Result(column="ISSUESTITLE",property="issues.issuesTitle"),
		 @Result(column="STATUS",property="issues.status"),
		 @Result(column="CREATESTAFFNAME",property="issues.createStaffName"),
		 @Result(column="CREATETIME",property="issues.createTime"),
		 @Result(column="PLANCODE",property="plan.planCode"),
		 @Result(column="PLANNAME",property="plan.planName"),
	})
	List<TblRectificationIssuesVo> selectRectificationIssuesLedgetPageInfoList(PageInfo<TblRectificationIssuesVo> pageInfo, Integer isAll) throws Exception;
	
	@SelectProvider(method = "selectRectificationIssuesLedgetPageInfoCount" , type = TblRectificationIssuesMapperSqlConfig.class)
	Integer selectRectificationIssuesLedgetPageInfoCount(PageInfo<TblRectificationIssuesVo> pageInfo, Integer isAll) throws Exception;

	@SelectProvider(method = "selectListByIssuesId" , type = TblRectificationIssuesMapperSqlConfig.class)
	@Results({
		  @Result(column="RELAID",property="relaId"),
		  @Result(column="RECTIFICATIONPLAN",property="rectificationPlan"),
		  @Result(column="RECTIFICATIONMEASURES",property="rectificationMeasures"),
		  @Result(column="RESULTMEMO",property="resultMemo"),
		  @Result(column="DEADLINE",property="deadline"),
		  @Result(column="TRISTATUS",property="status"),
		  @Result(column="RESPONSIBLEPERSON",property="responsiblePerson"),
		  @Result(column="RESPONSIBLEPERSONNAME",property="responsiblePersonName"),
		  @Result(column="RESPONSIBLEDEPT",property="responsibleDept"),
		  @Result(column="RESPONSIBLEDEPTNAME",property="responsibleDetpName"),
		  @Result(column="IMPLEMENTER",property="implementer"),
		  @Result(column="IMPLEMENTERNAME",property="implementerName"),
		  @Result(column="ISSUESID",property="issues.issuesId",id=true),
		  @Result(column="ISSUESCODE",property="issues.issuesCode"),
		  @Result(column="ISSUESNAME",property="issues.issuesName"),
		  @Result(column="CREATESTAFF",property="issues.createStaff"),
		  @Result(column="CREATESTAFFNAME",property="issues.createStaffName"),
		  @Result(column="ISSUESITEM",property="issues.issuesItem"),
		  @Result(column="ISSUESTITLE",property="issues.issuesTitle"),
		  @Result(column="QUESTIONMEMO",property="issues.questionMemo"),
		  @Result(column="EVALID",property="valua.evalId",id=true),
		  @Result(column="STATUS",property="valua.status"),
		  @Result(column="RESULTSTATUS",property="valua.resultStatus"),
		  @Result(column="IMPLID",property="valua.implId"),
		  @Result(column="PLANID",property="plan.planId",id=true),
		  @Result(column="PLANCODE",property="plan.planCode"),
		  @Result(column="PLANNAME",property="plan.planName"),
		  @Result(column="IMPLID",property="reimpl.implId",id=true),
		  @Result(column="TZRIRECTIFICATIONMEASURES",property="reimpl.rectificationMeasures"),
		  @Result(column="TZRISITUATIONOVERVIEW",property="reimpl.situationoverView"),
		  @Result(column="TZRIACHIVEMENT",property="reimpl.achivement"),
	})
	List<TblRectificationIssuesVo> selectListByIssuesId(String issuesId, String relaId) throws Exception;

	@SelectProvider(method = "selectListByReport" , type = TblRectificationIssuesMapperSqlConfig.class)
	@Results({
		  @Result(column="RELAID",property="relaId"),
		  @Result(column="RECTIFICATIONPLAN",property="rectificationPlan"),
		  @Result(column="DEADLINE",property="deadline"),
		  @Result(column="PLANID",property="planId"),
		  @Result(column="RESPONSIBLEPERSON",property="responsiblePerson"),
		  @Result(column="RESPONSIBLEPERSONNAME",property="responsiblePersonName"),
		  @Result(column="RESPONSIBLEDEPT",property="responsibleDept"),
		  @Result(column="RESPONSIBLEDEPTNAME",property="responsibleDetpName"),
		  @Result(column="IMPLEMENTER",property="implementer"),
		  @Result(column="IMPLEMENTERNAME",property="implementerName"),
		  @Result(column="ISSUESID",property="issues.issuesId",id=true),
		  @Result(column="ISSUESCODE",property="issues.issuesCode"),
		  @Result(column="ISSUESNAME",property="issues.issuesName"),
		  @Result(column="CREATESTAFF",property="issues.createStaff"),
		  @Result(column="CREATESTAFFNAME",property="issues.createStaffName"),
		  @Result(column="ISSUESITEM",property="issues.issuesItem"),
		  @Result(column="TZICREATETIME",property="issues.createTime"),
		  @Result(column="ISSUESTITLE",property="issues.issuesTitle"),
		  @Result(column="QUESTIONMEMO",property="issues.questionMemo"),
		  @Result(column="SITUATIONOVERVIEW",property="situationoverView"),
	})
	List<TblRectificationIssuesVo> selectListByReport(TblRectificationIssuesVo rela) throws Exception;

	@Delete("DELETE FROM TBL_PLANISSUES_ATT WHERE relaId = #{relaId} AND ATTID = #{attId}")
	void deleteFileRelationByAttId(@Param("relaId") String relaId,@Param("attId") String attId) throws Exception;

	@Insert("INSERT INTO TBL_PLANISSUES_ATT(ATTID,RELAID) VALUES (#{attid},#{relaId})")
	void saveFileRelation(@Param("attid")String attid,@Param("relaId") String relaId) throws Exception;

	@Select("SELECT RELAID FROM TBL_RECTIFICATION_ISSUES TRI " + 
			" INNER JOIN ( SELECT ISSUESID,PLANID,MAX(VERSION) AS VERSION FROM TBL_RECTIFICATION_ISSUES GROUP BY ISSUESID,PLANID ) T1 ON TRI.ISSUESID = T1.ISSUESID " + 
			" AND TRI.PLANID = T1.PLANID AND TRI.VERSION = T1.VERSION " + 
			"	WHERE TRI.PLANID = #{planId} AND TRI.ISSUESID = #{issuesId}")
	String selectRelaIdByPlanIdIssuesId(@Param("planId") String planId,@Param("issuesId") String issuesId) throws Exception;

	@SelectProvider(method = "selectMyRectificationExportList" , type = TblRectificationIssuesMapperSqlConfig.class)
	@Results({
		 @Result(column="AUDITOBJECTNAME",property="issues.auditObjectName"),
		 @Result(column="PROJECTNO",property="issues.projectNo"),
		 @Result(column="PROJECTNAME",property="issues.projectName"),
		 @Result(column="ISSUESCODE",property="issues.issuesCode"),
		 @Result(column="ISSUESNAME",property="issues.issuesName"),
		 @Result(column="PLANCODE",property="plan.planCode"),
		 @Result(column="PLANNAME",property="plan.planName"),
		 @Result(column="TZRVEVALID",property="valua.evalId"),
		 @Result(column="TZRVSTATUS",property="valua.status"),
		 @Result(column="TZRVRSTATUS",property="valua.resultStatus"),
		 @Result(column="TZRISTATUS",property="reimpl.status"),
		 @Result(column="RECTIFICATIONMEASURES",property="reimpl.rectificationMeasures"),
		 @Result(column="SITUATIONOVERVIEW",property="reimpl.situationoverView"),
		 @Result(column="ACHIVEMENT",property="reimpl.achivement"),
		 @Result(column="TZRIDEADLINE",property="reimpl.deadline"),
		 @Result(column="CONCLUSION",property="reimpl.conclusion"),
		 @Result(column="NEXTMEASURES",property="reimpl.nextMeasures"),
		 @Result(column="FINISHTIME",property="reimpl.finishTime"),
		 @Result(column="ONEORGNAME",property="reimpl.oneorgname"),
		 @Result(column="PROBLEMSRC",property="reimpl.problemsrc"),
		 @Result(column="REPORTYEAR",property="reimpl.reportyear"),
		 @Result(column="PROBLEMTYPE",property="reimpl.problemtype"),
		 @Result(column="ONETITLE",property="reimpl.onetitle"),
		 @Result(column="TWOTITLE",property="reimpl.twotitle"),
		 @Result(column="THREETITLE",property="reimpl.threetitle"),
		 @Result(column="REPORTEXPRESSION",property="reimpl.reportexpression"),
		 @Result(column="QUEEXPRESSION",property="reimpl.queexpression"),
		 @Result(column="QUEMONEY",property="reimpl.quemoney"),
		 @Result(column="SUPERVISION",property="reimpl.supervision"),
		 @Result(column="RECTTYPE",property="reimpl.recttype"),
		 @Result(column="LEGALBASIS",property="reimpl.legalbasis"),
		 @Result(column="RECTDEMAND",property="reimpl.rectdemand"),
		 @Result(column="RECTTIMELIMIT",property="reimpl.recttimelimit"),
		 @Result(column="FIRSTRESPONSTAFFNAME",property="reimpl.firstresponstaffname"),
		 @Result(column="ASSISTLEADER",property="reimpl.assistleader"),
		 @Result(column="MAINDEPTHEADTEL",property="reimpl.maindeptheadtel"),
		 @Result(column="ASSISTDEPTHEADTEL",property="reimpl.assistdeptheadtel"),
		 @Result(column="AUDITDEPTHEADTEL",property="reimpl.auditdeptheadtel"),
		 @Result(column="PJCNT",property="reimpl.pjcnt"),
		 @Result(column="RECTMONEY",property="reimpl.rectmoney"),
		 @Result(column="RECOVERYMONEY",property="reimpl.recoverymoney"),
		 @Result(column="BACKMONEY",property="reimpl.backmoney"),
		 @Result(column="OVERALLMONEY",property="reimpl.overallmoney"),
		 @Result(column="ACCELERATEMONEY",property="reimpl.acceleratemoney"),
		 @Result(column="RETRIEVEMONEY",property="reimpl.retrievemoney"),
		 @Result(column="ADJUSTMONEY",property="reimpl.adjustmoney"),
		 @Result(column="STOPMONEY",property="reimpl.stopmoney"),
		 @Result(column="REISSUEMONEY",property="reimpl.reissuemoney"),
		 @Result(column="OTHERMONEY",property="reimpl.othermoney"),
		 @Result(column="OTHERWAY",property="reimpl.otherway"),
		 @Result(column="LANDAREA",property="reimpl.landarea"),
		 @Result(column="MINERALS",property="reimpl.minerals"),
		 @Result(column="ORGCNT",property="reimpl.orgcnt"),
		 @Result(column="FAMILYCNT",property="reimpl.familycnt"),
		 @Result(column="PERSONCNT",property="reimpl.personcnt"),
		 @Result(column="HOUSECNT",property="reimpl.housecnt"),
		 @Result(column="ACCOUNTABILITYINFO",property="reimpl.accountabilityinfo"),
		 @Result(column="ACCOUNTABILITYCNT",property="reimpl.accountabilitycnt"),
		 @Result(column="INSTITUTIONCNT",property="reimpl.institutioncnt"),
		 @Result(column="INSTITUTIONINFO",property="reimpl.institutioninfo"),
		 @Result(column="ISXH",property="reimpl.isxh"),
	})
	List<TblRectificationIssuesVo> selectMyRectificationExportList(TblRectificationIssuesVo condition);

	@Select("SELECT CASE WHEN TON.ISUSEDEFAULT = 0 THEN TAI.NODEFAULTCODE ELSE TON.NOCODE END AS CODE , CASE WHEN TON.ISUSEDEFAULT = 0 THEN TAI.NODEFAULTSEPARTOR ELSE"+
		" TON.NOSEPARTOR END AS SEP, CASE WHEN TON.ISUSEDEFAULT = 0 THEN TAI.NODEFAULTNUMBER ELSE TON.NONUMBER END AS NUMBER1"+
		" FROM TBL_AUTONO_INFO TAI LEFT JOIN TBL_ORG_NO TON ON TAI.NOID = TON.NOID WHERE TON.ORGID =#{orgid} AND TON.NOID =#{NoId}")
	Map<String, Object>  getcodeRule(@Param("orgid")BigDecimal  orgid,@Param("NoId")Integer NoId);
	
	@SelectProvider(method = "getMaxNumberFor" , type = TblRectificationIssuesMapperSqlConfig.class)
	String getMaxNumberFor(MaxNumberParam queryParam)throws Exception;
	
	
	
	@SelectProvider(method = "selectHxzgPageInfoList" , type = TblRectificationIssuesMapperSqlConfig.class)
	@Results({
		 @Result(column="AUDITOBJECTNAME",property="issues.auditObjectName"),
		 @Result(column="PROJECTNO",property="issues.projectNo"),
		 @Result(column="PROJECTNAME",property="issues.projectName"),
		 @Result(column="ISSUESCODE",property="issues.issuesCode"),
		 @Result(column="ISSUESNAME",property="issues.issuesName"),
		 @Result(column="PLANCODE",property="plan.planCode"),
		 @Result(column="PLANNAME",property="plan.planName"),
		 @Result(column="TZRVEVALID",property="valua.evalId"),
		 @Result(column="TZRVSTATUS",property="valua.status"),
		 @Result(column="TZRVRSTATUS",property="valua.resultStatus"),
		 @Result(column="TZRISTATUS",property="reimpl.status"),
	})
	List<TblRectificationIssuesVo> selectHxzgPageInfoList(TblRectificationIssuesVo condition) ;


}
