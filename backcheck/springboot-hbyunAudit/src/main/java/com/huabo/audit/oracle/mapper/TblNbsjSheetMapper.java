package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;
import java.util.List;

import com.hbfk.entity.TblStaffUtil;
import org.apache.ibatis.annotations.*;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.dto.FlowTaskInfo;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblNbsjSheetEntity;
import com.huabo.audit.oracle.entity.TblNbsjSheetReportEntity;
import com.huabo.audit.oracle.vo.TBlNbsjSheetVo;
import com.huabo.audit.util.PageInfo;

import cn.hutool.json.JSONObject;
import tk.mybatis.mapper.common.Mapper;

public interface TblNbsjSheetMapper extends BaseMapper<TblNbsjSheetEntity> {
	@Select("SELECT * from TBL_NBSJ_SHEET WHERE SHEETID= #{sheetid} ")
    TblNbsjSheetEntity getById(String sheetid);
    
    @SelectProvider(method="selectCountByPageInfo",type=TblNbsjSheetMapperSqlConfig.class)
   	Integer selectCountByPageInfo(PageInfo<TblNbsjSheetEntity> pageInfo,TBlNbsjSheetVo tBlNbsjSheetVo,Integer operateid) throws Exception;

    @SelectProvider(method="findByProjectIdAndPmUserId",type=TblNbsjSheetMapperSqlConfig.class)
    List<TblNbsjSheetEntity> findByProjectIdAndPmUserId(com.hbfk.util.PageInfo<TblNbsjSheetEntity> pageInfo,
			Integer projectId) throws Exception;

    @SelectProvider(method="selectCountByPageInfo",type=TblNbsjSheetMapperSqlConfig.class)
	Integer findByProjectIdAndPmUserIdCount(com.hbfk.util.PageInfo<TblNbsjSheetEntity> pageInfo, Integer projectId) throws Exception;
    
    @Select("SELECT TNA.*,PRINCIPAL.REALNAME,ORG.ORGNAME "
    		+ " FROM TBL_NBSJ_SHEET TNA "
    		+ "LEFT JOIN TBL_STAFF PRINCIPAL ON PRINCIPAL.STAFFID = TNA.CREATESTAFF "
			+ "LEFT JOIN TBL_ORGANIZATION ORG  ON ORG.ORGID = TNA.AUDITORG "
    		+ " WHERE TNA.SHEETID = #{sheetid}")
    @Results({
    	@Result(column="SHEETID",property="sheetId"),
    	@Result(column="SHEETCODE",property="sheetCode"), 
    	@Result(column="SHEETNAME",property="sheetName"),
    	@Result(column="SHEETTARGET",property="sheetTarget"),
    	@Result(column="AUDITORG",property="auditOrg"),
    	@Result(column="RISKATTRBUTION",property="riskAttrbution"),
    	@Result(column="BUSINESSAFFILIATION",property="businessAffiliation"),
    	@Result(column="APPROVER",property="approver"),
    	@Result(column="RISKLEVEL",property="riskLevel"),
    	@Result(column="QUESTITLE",property="quesTitle"),
    	@Result(column="TARGETNAME",property="targetName"),
    	@Result(column="BUSINESSTYPE",property="businessType"),
    	@Result(column="SUDITPROCESS",property="suditProcess"),
    	@Result(column="ORGNAME",property="orgname"),
    	@Result(column="REALNAME",property="realname"),
    	@Result(column="CREATESTAFF",property="createStaffObj.staffid"),
    	@Result(column="SHEETTYPE",property="sheettype"),
    	@Result(column="YJFH",property="yjfh"),
    	@Result(column="EJFH",property="ejfh"),
    	@Result(column="FIRSTSTAFFID",property="firststaffid"),
    	@Result(column="SECONDSTAFFID",property="secondstaffid"),
    	
    })
   	TblNbsjSheetEntity selectById(@Param("sheetid") BigDecimal sheetid) throws Exception;
    
    @SelectProvider(method="selectListByPageInfo",type=TblNbsjSheetMapperSqlConfig.class)
    @Results({
    	@Result(column="SHEETID",property="sheetId"),
    	@Result(column="SHEETCODE",property="sheetCode"),
    	@Result(column="SHEETNAME",property="sheetName"),
    	@Result(column="SHEETTARGET",property="sheetTarget"),
    	@Result(column="AUDITORG",property="auditOrg"),
    	@Result(column="RISKATTRBUTION",property="riskAttrbution"),
    	@Result(column="BUSINESSAFFILIATION",property="businessAffiliation"),
    	@Result(column="APPROVER",property="approver"),
    	@Result(column="RISKLEVEL",property="riskLevel"),
    	@Result(column="QUESTITLE",property="quesTitle"),
    	@Result(column="TARGETNAME",property="targetName"),
    	@Result(column="BUSINESSTYPE",property="businessType"),
    	@Result(column="SUDITPROCESS",property="suditProcess"),
    	@Result(column="STATE",property="state"),
    	@Result(column="CREATESTAFF",property="createStaffObj.staffid"),
    	@Result(column="REALNAME",property="createStaffObj.realname"),
    	@Result(column="auditrealname",property="auditusername"),
    	@Result(column="ORGNAME",property="orgname"),
    	@Result(column="REALNAME",property="realname"),
    	@Result(column="SHEETTYPE",property="sheettype"),
    	@Result(column="YJFH",property="yjfh"),
    	@Result(column="EJFH",property="ejfh"),
    	
    })
	List<TblNbsjSheetEntity> selectListByPageInfo(PageInfo<TblNbsjSheetEntity> pageInfo,TBlNbsjSheetVo tBlNbsjSheetVo,Integer operateid) throws Exception;

    @Delete("DELETE FROM TBL_NBSJ_SHEET WHERE SHEETID = #{sheetid}")
    void deleteById(BigDecimal sheetid) throws Exception;

    @SelectProvider(method="selectPlanCodeByOrgid",type=TblNbsjSheetMapperSqlConfig.class)
	Integer selectPlanCodeByOrgid(TblNbsjSheetEntity plan) throws Exception;
    
    @InsertProvider(method="insertEntity",type=TblNbsjSheetMapperSqlConfig.class)
    @Options(useGeneratedKeys=true, keyProperty="sheetId", keyColumn="SHEETID")
	void insertEntity(TblNbsjSheetEntity plan) throws Exception;

    @UpdateProvider(method="updateEntity",type=TblNbsjSheetMapperSqlConfig.class)
	void updateEntity(TblNbsjSheetEntity plan) throws Exception;

    @Update("update TBL_NBSJ_SHEET set firststaffid=#{firststaffid},set secondstaffid=#{secondstaffid} where sheetid=#{sheetid}")
	void saveSheetFh(Object object, Integer firststaffid, Integer secondstaffid,Integer sheetid);
    
    //
    @SelectProvider(method="getExportList",type=TblNbsjSheetMapperSqlConfig.class)
    @Results({
    	@Result(column="SHEETID",property="sheetId"),
    	@Result(column="SHEETCODE",property="sheetCode"),
    	@Result(column="SHEETNAME",property="sheetName"),
    	@Result(column="SHEETTARGET",property="sheetTarget"),
    	@Result(column="AUDITORG",property="auditOrg"),
    	@Result(column="RISKATTRBUTION",property="riskAttrbution"),
    	@Result(column="BUSINESSAFFILIATION",property="businessAffiliation"),
    	@Result(column="APPROVER",property="approver"),
    	@Result(column="RISKLEVEL",property="riskLevel"),
    	@Result(column="QUESTITLE",property="quesTitle"),
    	@Result(column="TARGETNAME",property="targetName"),
    	@Result(column="BUSINESSTYPE",property="businessType"),
    	@Result(column="SUDITPROCESS",property="suditProcess"),
    	@Result(column="REALNAME",property="realname"),
    	@Result(column="PRJOECTNAME",property="projectName"),
    	@Result(column="ORGIDNAMES",property="orgIdNames"),
    	
    })
	List<TblNbsjSheetEntity> getExportList(BigDecimal projectid ,TblNbsjSheetEntity tblSheet,TblStaffUtil staff) throws Exception;

    @Insert("INSERT INTO TBL_NBSJ_SHEETATT(SHEETID, ATTID) VALUES (#{workId}, #{attId})")
	void insetFileRelation(BigDecimal attId, BigDecimal workId) throws Exception;
    
    
    //==
    @Delete("DELETE FROM TBL_NBSJ_SHEET_REPORT WHERE SHEETID = #{sheetId}")
    void deleteSheetReportBySheetId(BigDecimal sheetId) throws Exception;
    
    @InsertProvider(method="insertSheetReport",type=TblNbsjSheetMapperSqlConfig.class)
    @Options(useGeneratedKeys=true, keyProperty="reportid", keyColumn="REPORTID")
	void insertSheetReport(TblNbsjSheetReportEntity sr) throws Exception;
    
    @Select("SELECT TNA.* "
    		+ " FROM TBL_NBSJ_SHEET_REPORT TNA "
    		+ " WHERE TNA.SHEETID = #{sheetid}")
    @Results({
    	@Result(column="REPORTID",property="reportid"),
    	@Result(column="REPORTCONTENT",property="reportConcent"),
    	@Result(column="REPORTORGIDS",property="sjdeptIds"),
    	@Result(column="SHEETID",property="sheetId"),
    })
   	List<TblNbsjSheetReportEntity> selectListSheetReport(@Param("sheetid") BigDecimal sheetid) throws Exception;
    
    @Delete("DELETE FROM TBL_NBSJ_SHEET_REPORT WHERE REPORTID = #{reportid}")
    void deleteSheetReportById(BigDecimal reportid) throws Exception;

    
    @Delete("DELETE FROM TBL_LEGAL_SHEET_ATT WHERE attid=#{attid}")
	void deleteFileInfoByAttId(BigDecimal attid);

    @Select("select * from TBL_NBSJ_SHEET where SHEETID in (select SHEETID from TBL_NBSJ_QUESTIONAFFIRM f left join TBL_NBSJ_QUESTION q on f.QUESTIONID = q.QUESTIONID where q.QUESTIONID is not null and f.FACTID = #{factid}) and PROJECTID = #{projectId}")
	List<TblNbsjSheetEntity> findNbsjSheetByFactBookIdSp(String factid, String projectId);
    
    
    @SelectProvider(method="selectifPmOrLeader",type=TblNbsjSheetMapperSqlConfig.class)
	int selectifPmOrLeader(BigDecimal staffid,BigDecimal projectid) throws Exception;
    
    
    @SelectProvider(method="selectSheetByprojectId",type=TblNbsjSheetMapperSqlConfig.class)
	Integer selectSheetByprojectId(BigDecimal projectid) throws Exception;
    
    @Select("select * from (select businessType,(select  count(businessType) from tbl_nbsj_sheet where businessType is not null and risklevel='是' and businessType=s.businessType and   projectid=#{projectid}  group by businessType) as tsize,quesTitle,auditDiscoverable,auditCourse from tbl_nbsj_sheet s where projectid=#{projectid}   order by businessType ) where tsize>0")
    List<TblNbsjSheetEntity> getReportSheet(String projectid) throws Exception;
    
    
    
    @SelectProvider(method="getExportListstaff",type=TblNbsjSheetMapperSqlConfig.class)
    @Results({
    	@Result(column="SHEETID",property="sheetId"),
    	@Result(column="SHEETCODE",property="sheetCode"),
    	@Result(column="SHEETNAME",property="sheetName"),
    	@Result(column="SHEETTARGET",property="sheetTarget"),
    	@Result(column="AUDITORG",property="auditOrg"),
    	@Result(column="RISKATTRBUTION",property="riskAttrbution"),
    	@Result(column="BUSINESSAFFILIATION",property="businessAffiliation"),
    	@Result(column="APPROVER",property="approver"),
    	@Result(column="RISKLEVEL",property="riskLevel"),
    	@Result(column="QUESTITLE",property="quesTitle"),
    	@Result(column="TARGETNAME",property="targetName"),
    	@Result(column="BUSINESSTYPE",property="businessType"),
    	@Result(column="SUDITPROCESS",property="suditProcess"),
    	@Result(column="REALNAME",property="realname"),
    	@Result(column="PRJOECTNAME",property="projectName"),
    	@Result(column="ORGIDNAMES",property="orgIdNames"),
    	
    })
	List<TblNbsjSheetEntity> getExportListstaff(BigDecimal projectid,TblStaffUtil staff,TblNbsjSheetEntity tblSheet) throws Exception;
    
    
	@Select("SELECT ts.* FROM TBL_NBSJ_SHEET TS WHERE  TS.RISKLEVEL='是' AND TS.PROJECTID=#{projectid}")
	List<TblNbsjSheetEntity> findwtzs(String projectid) throws Exception;

     
	@Select("SELECT ts.* FROM TBL_NBSJ_SHEET TS  WHERE TS.PROJECTID = #{projectid} AND (TS.IFSBG!=0 OR TS.IFSBG is NULL) AND TS.RISKLEVEL='是' ")
	List<TblNbsjSheetEntity> finddzgwts(String projectid) throws Exception;
	
	
	@Select("SELECT sh.* FROM TBL_NBSJ_SHEET sh LEFT JOIN TBL_NBSJ_QUESTION nq ON SH.SHEETID = nq.SHEETID   WHERE SH.PROJECTID = #{projectid} AND SH.RISKLEVEL='是' AND NQ.QUESTIONID in (SELECT DISTINCT TC.QUESTIONID from TBL_NBSJ_REFOPM TC  WHERE  TC.LASTREFORMSTATUS = 1 AND TC.STATUS = 3 ) ")
	List<TblNbsjSheetEntity> findyzgwts(String projectid) throws Exception;
	
	@Select("SELECT sh.* FROM TBL_NBSJ_SHEET sh LEFT JOIN TBL_NBSJ_QUESTION nq ON SH.SHEETID = nq.SHEETID   WHERE SH.PROJECTID = #{projectid} AND SH.RISKLEVEL='是' AND NQ.QUESTIONID in (SELECT DISTINCT TC.QUESTIONID from TBL_NBSJ_REFOPM TC  WHERE  TC.LASTREFORMSTATUS = 1  AND ( TC.STATUS < 3 OR TC.STATUS IS NULL) ) ")
	List<TblNbsjSheetEntity> findwzgwts(String projectid) throws Exception;
	
    
    @SelectProvider(method="selectListByhzPageInfo",type=TblNbsjSheetMapperSqlConfig.class)
    @Results({
    	@Result(column="SHEETID",property="sheetId"),
    	@Result(column="SHEETCODE",property="sheetCode"),
    	@Result(column="SHEETNAME",property="sheetName"),
    	@Result(column="SHEETTARGET",property="sheetTarget"),
    	@Result(column="AUDITORG",property="auditOrg"),
    	@Result(column="RISKATTRBUTION",property="riskAttrbution"),
    	@Result(column="BUSINESSAFFILIATION",property="businessAffiliation"),
    	@Result(column="APPROVER",property="approver"),
    	@Result(column="RISKLEVEL",property="riskLevel"),
    	@Result(column="QUESTITLE",property="quesTitle"),
    	@Result(column="TARGETNAME",property="targetName"),
    	@Result(column="BUSINESSTYPE",property="businessType"),
    	@Result(column="SUDITPROCESS",property="suditProcess"),
    	@Result(column="STATE",property="state"),
    	@Result(column="CREATESTAFF",property="createStaffObj.staffid"),
    	@Result(column="REALNAME",property="createStaffObj.realname"),
    	@Result(column="ORGNAME",property="orgname"),
    	@Result(column="REALNAME",property="realname"),
    	@Result(column="projectname",property="projectName"),
    	@Result(column="projectCode",property="projectCode"),
    	@Result(column="ISTYPE",property="istype"),
    	@Result(column="GLID",property="glid"),
    	@Result(column="YJFH",property="yjfh"),
    	@Result(column="EJFH",property="ejfh"),
    	@Result(column="SHEETTYPE",property="sheettype"),
    	@Result(column="projectid",property="projectId"),
    })
	List<TblNbsjSheetEntity> selectListByhzPageInfo(PageInfo<TblNbsjSheetEntity> pageInfo,TBlNbsjSheetVo tBlNbsjSheetVo,TblStaffUtil loginStaff) throws Exception;

    @SelectProvider(method="selectCountByhzPageInfo",type=TblNbsjSheetMapperSqlConfig.class)
   	Integer selectCountByhzPageInfo(PageInfo<TblNbsjSheetEntity> pageInfo,TBlNbsjSheetVo tBlNbsjSheetVo,TblStaffUtil loginStaff) throws Exception;
    
    @SelectProvider(method="selectPlanCodeByDGOrgid",type=TblNbsjSheetMapperSqlConfig.class)
   	Integer selectPlanCodeByDGOrgid(TblNbsjSheetEntity plan) throws Exception;

    @SelectProvider(method="selectPageInfoListByIssues",type=TblNbsjSheetMapperSqlConfig.class)
    @Results({
    	@Result(column="SHEETID",property="sheetId"),
    	@Result(column="SHEETCODE",property="sheetCode"),
    	@Result(column="SHEETNAME",property="sheetName"),
    	@Result(column="SHEETTARGET",property="sheetTarget"),
    	@Result(column="AUDITORG",property="auditOrg"),
    	@Result(column="RISKATTRBUTION",property="riskAttrbution"),
    	@Result(column="BUSINESSAFFILIATION",property="businessAffiliation"),
    	@Result(column="APPROVER",property="approver"),
    	@Result(column="RISKLEVEL",property="riskLevel"),
    	@Result(column="QUESTITLE",property="quesTitle"),
    	@Result(column="TARGETNAME",property="targetName"),
    	@Result(column="BUSINESSTYPE",property="businessType"),
    	@Result(column="SUDITPROCESS",property="suditProcess"),
    	@Result(column="STATE",property="state"),
    	@Result(column="CREATESTAFF",property="createStaffObj.staffid"),
    	@Result(column="REALNAME",property="createStaffObj.realname"),
    	@Result(column="ORGNAME",property="orgname"),
    	@Result(column="REALNAME",property="realname"),
    	@Result(column="projectname",property="projectName"),
    	@Result(column="projectCode",property="projectCode"),
    	@Result(column="ISTYPE",property="istype"),
    	@Result(column="GLID",property="glid"),
    	@Result(column="YJFH",property="yjfh"),
    	@Result(column="EJFH",property="ejfh"),
    	@Result(column="SHEETTYPE",property="sheettype"),
    	@Result(column="projectid",property="projectId"),
    })
	List<TblNbsjSheetEntity> selectPageInfoListByIssues(TBlNbsjSheetVo tBlNbsjSheetVo) ;

    @SelectProvider(method="selectListByPageInfoxml",type=TblNbsjSheetMapperSqlConfig.class)
	List<TblNbsjSheetEntity> selectListByPageInfoxml(TBlNbsjSheetVo vo, TblStaffUtil loginStaff);

	@SelectProvider(method="selectManageListByPageInfoxml",type=TblNbsjSheetMapperSqlConfig.class)
	List<TblNbsjSheetEntity> selectManageListByPageInfoxml(TBlNbsjSheetVo vo, TblStaffUtil loginStaff);

	List<TblNbsjSheetEntity> findByProjectIdAndPmUserIdXml(@Param("tBlNbsjSheetVo")TblNbsjSheetEntity tBlNbsjSheetVo);
	
	
    @Select("SELECT TNA.*,PRINCIPAL.REALNAME,ORG.ORGNAME,case when PRO.PRJOECTNAME is not NULL  then  PRO.PRJOECTNAME     when pl.PLANNAME is not NULL   then pl.PLANNAME  else wpro.projectname END projectname,"
    		+ "case when PRO.PROJECTCODE is not NULL  then  PRO.PROJECTCODE     when pl.PLANNUMBER is not NULL   then pl.PLANNUMBER  else wpro.PROJECTCODE END projectcode  "
    		+ " FROM TBL_NBSJ_SHEET TNA "
    		+ " LEFT JOIN  TBL_TESTPLAN pl on TNA.PROJECTID=pl.TESTPLANID  "
    		+ " LEFT JOIN  TBL_NBSJ_PROJECT pro on TNA.PROJECTID=PRO.PROJECTID "  
    		+ " LEFT JOIN  TBL_NBSJ_WBPROJECT wpro on TNA.PROJECTID=wpro.PROJECTID "
    		+ "LEFT JOIN TBL_STAFF PRINCIPAL ON PRINCIPAL.STAFFID = TNA.CREATESTAFF "
			+ "LEFT JOIN TBL_ORGANIZATION ORG  ON ORG.ORGID = TNA.AUDITORG "
    		+ " WHERE TNA.SHEETID = #{sheetid}")
    @Results({
    	@Result(column="SHEETID",property="sheetId"),
    	@Result(column="SHEETCODE",property="sheetCode"),
    	@Result(column="SHEETNAME",property="sheetName"),
    	@Result(column="SHEETTARGET",property="sheetTarget"),
    	@Result(column="AUDITORG",property="auditOrg"),
    	@Result(column="RISKATTRBUTION",property="riskAttrbution"),
    	@Result(column="BUSINESSAFFILIATION",property="businessAffiliation"),
    	@Result(column="APPROVER",property="approver"),
    	@Result(column="RISKLEVEL",property="riskLevel"),
    	@Result(column="QUESTITLE",property="quesTitle"),
    	@Result(column="TARGETNAME",property="targetName"),
    	@Result(column="BUSINESSTYPE",property="businessType"),
    	@Result(column="SUDITPROCESS",property="suditProcess"),
    	@Result(column="ORGNAME",property="orgname"),
    	@Result(column="REALNAME",property="realname"),
    	@Result(column="projectname",property="projectName"),
    	@Result(column="CREATESTAFF",property="createStaffObj.staffid"),
    	@Result(column="ISTYPE",property="istype"),
    	@Result(column="GLID",property="glid"),
    	@Result(column="projectid",property="projectId"),
    	@Result(column="projectcode",property="projectCode"),
    	@Result(column="YJFH",property="yjfh"),
    	@Result(column="EJFH",property="ejfh"),
    	@Result(column="SHEETTYPE",property="sheettype"),
    	@Result(column="FIRSTSTAFFID",property="firststaffid"),
    	@Result(column="SECONDSTAFFID",property="secondstaffid"),
    	
    })
   	TblNbsjSheetEntity selectByZgId(@Param("sheetid") BigDecimal sheetid) throws Exception;
    
    @SelectProvider(method="selectListByZgPageInfo",type=TblNbsjSheetMapperSqlConfig.class)
    @Results({
    	@Result(column="SHEETID",property="sheetId"),
    	@Result(column="SHEETCODE",property="sheetCode"),
    	@Result(column="SHEETNAME",property="sheetName"),
    	@Result(column="SHEETTARGET",property="sheetTarget"),
    	@Result(column="AUDITORG",property="auditOrg"),
    	@Result(column="RISKATTRBUTION",property="riskAttrbution"),
    	@Result(column="BUSINESSAFFILIATION",property="businessAffiliation"),
    	@Result(column="APPROVER",property="approver"),
    	@Result(column="RISKLEVEL",property="riskLevel"),
    	@Result(column="QUESTITLE",property="quesTitle"),
    	@Result(column="TARGETNAME",property="targetName"),
    	@Result(column="BUSINESSTYPE",property="businessType"),
    	@Result(column="SUDITPROCESS",property="suditProcess"),
    	@Result(column="STATE",property="state"),
    	@Result(column="CREATESTAFF",property="createStaffObj.staffid"),
    	@Result(column="REALNAME",property="createStaffObj.realname"),
    	@Result(column="ORGNAME",property="orgname"),
    	@Result(column="REALNAME",property="realname"),
    	@Result(column="projectname",property="projectName"),
    	@Result(column="ISTYPE",property="istype"),
    	@Result(column="GLID",property="glid"),
    	@Result(column="YJFH",property="yjfh"),
    	@Result(column="EJFH",property="ejfh"),
    	@Result(column="SHEETTYPE",property="sheettype"),
    	@Result(column="projectid",property="projectId"),
    })
	List<TblNbsjSheetEntity> selectListByZgPageInfo(PageInfo<TblNbsjSheetEntity> pageInfo,TBlNbsjSheetVo tBlNbsjSheetVo) throws Exception;

    @SelectProvider(method="selectCountByZgPageInfo",type=TblNbsjSheetMapperSqlConfig.class)
   	Integer selectCountByZgPageInfo(PageInfo<TblNbsjSheetEntity> pageInfo,TBlNbsjSheetVo tBlNbsjSheetVo) throws Exception;

    @Select("SELECT ORGIDS FROM TBL_NBSJ_SHEET WHERE SHEETID = #{sheetId}")
	String selectOrgIdsById(BigDecimal sheetId) throws Exception;

	@Select("SELECT ORGIDNAMES FROM TBL_NBSJ_SHEET WHERE SHEETID = #{sheetId}")
	String selectOrgIdsByName(BigDecimal sheetId) throws Exception;

	@Select("SELECT COUNT(0) FROM TBL_STAFF WHERE REALNAME =#{orgName}")
	Integer selectCountByName(String orgName) throws Exception;

    @Select("SELECT ORGIDS FROM TBL_NBSJ_PROJECT WHERE PROJECTID = #{sheetId}")
	String selectOrgIdsByProjectId(BigDecimal sheetId) throws Exception;


    
    @Select("SELECT TFT.CURRENTSTAFFID,TS.REALNAME AS CURRENTSTAFFNAME , TFT.CREATETIME,TFT.COMMONT FROM TBL_FLOW_TASKINFO TFT LEFT JOIN TBL_STAFF TS ON TFT.CURRENTSTAFFID = TS.STAFFID " + 
    		"WHERE TFT.TAKSID = ( SELECT MAX(TAKSID) FROM TBL_FLOW_TASKINFO WHERE FROMID = #{id} AND FLOWID IN (SELECT FLOWID FROM TBL_SYSTEM_FORMFLOW WHERE FORMID = #{id}) AND FLOWTASKID IN (SELECT YMFORMID FROM TBL_SYSTEM_FORMFLOW WHERE FORMID = #{id}) AND CURRENTROLE = #{stepId})")
	FlowTaskInfo selectApprovalInfo(@Param("id")BigDecimal id,@Param("sheetid") String sheetid,@Param("stepId") String stepId);
    
}
