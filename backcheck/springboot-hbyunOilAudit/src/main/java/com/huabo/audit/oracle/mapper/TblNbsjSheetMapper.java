package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;
import java.util.List;

import com.huabo.audit.oracle.entity.TblYqnsAuditMyManuscriptEntity;
import org.apache.ibatis.annotations.*;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.oracle.entity.TblNbsjSheetEntity;
import com.huabo.audit.oracle.entity.TblNbsjSheetReportEntity;
import com.huabo.audit.oracle.vo.TBlNbsjSheetVo;
import com.huabo.audit.util.PageInfo;

import cn.hutool.json.JSONObject;

public interface TblNbsjSheetMapper extends BaseMapper<TblNbsjSheetEntity> {
	@Select("SELECT * from TBL_NBSJ_SHEET WHERE SHEETID= #{sheetid} ")
    TblNbsjSheetEntity getById(String sheetid);
    
    @SelectProvider(method="selectCountByPageInfo",type=TblNbsjSheetMapperSqlConfig.class)
   	Integer selectCountByPageInfo(PageInfo<TblYqnsAuditMyManuscriptEntity> pageInfo,TBlNbsjSheetVo tBlNbsjSheetVo,Integer operateid) throws Exception;

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
    	@Result(column="CREATESTAFF",property="createStaff.staffid"),
    	
    	@Result(column="YJFH",property="yjfh"),
    	@Result(column="EJFH",property="ejfh"),
    	@Result(column="FIRSTSTAFFID",property="firststaffid"),
    	@Result(column="SECONDSTAFFID",property="secondstaffid"),
    	
    })
   	TblNbsjSheetEntity selectById(@Param("sheetid") Integer sheetid) throws Exception;
    
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
    	@Result(column="CREATESTAFF",property="createStaff.staffid"),
    	@Result(column="REALNAME",property="createStaff.realname"),
    	@Result(column="auditrealname",property="auditusername"),
    	@Result(column="ORGNAME",property="orgname"),
    	@Result(column="REALNAME",property="realname"),
    	@Result(column="YJFH",property="yjfh"),
    	@Result(column="EJFH",property="ejfh"),
    	
    })
	List<TblYqnsAuditMyManuscriptEntity> selectListByPageInfo(PageInfo<TblYqnsAuditMyManuscriptEntity> pageInfo, TBlNbsjSheetVo tBlNbsjSheetVo, Integer operateid) throws Exception;

    @Delete("DELETE FROM TBL_NBSJ_SHEET WHERE SHEETID = #{sheetid}")
    void deleteById(Integer sheetid) throws Exception;

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
	List<TblNbsjSheetEntity> getExportList(Integer projectid) throws Exception;

    @Insert("INSERT INTO TBL_NBSJ_SHEETATT(SHEETID, ATTID) VALUES (#{workId}, #{attId})")
	void insetFileRelation(BigDecimal attId, Integer workId) throws Exception;
    
    
    //==
    @Delete("DELETE FROM TBL_NBSJ_SHEET_REPORT WHERE SHEETID = #{sheetId}")
    void deleteSheetReportBySheetId(Integer sheetId) throws Exception;
    
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
   	List<TblNbsjSheetReportEntity> selectListSheetReport(@Param("sheetid") Integer sheetid) throws Exception;
    
    @Delete("DELETE FROM TBL_NBSJ_SHEET_REPORT WHERE REPORTID = #{reportid}")
    void deleteSheetReportById(Integer reportid) throws Exception;

    
    @Delete("DELETE FROM TBL_LEGAL_SHEET_ATT WHERE attid=#{attid}")
	void deleteFileInfoByAttId(Integer attid);

    @Select("select * from TBL_NBSJ_SHEET where SHEETID in (select SHEETID from TBL_NBSJ_QUESTIONAFFIRM f left join TBL_NBSJ_QUESTION q on f.QUESTIONID = q.QUESTIONID where q.QUESTIONID is not null and f.FACTID = #{factid}) and PROJECTID = #{projectId}")
	List<TblNbsjSheetEntity> findNbsjSheetByFactBookIdSp(String factid, String projectId);
    
    
    @SelectProvider(method="selectifPmOrLeader",type=TblNbsjSheetMapperSqlConfig.class)
	int selectifPmOrLeader(BigDecimal staffid,Integer projectid) throws Exception;
    
    
    @SelectProvider(method="selectSheetByprojectId",type=TblNbsjSheetMapperSqlConfig.class)
	Integer selectSheetByprojectId(Integer projectid) throws Exception;
    
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
	List<TblNbsjSheetEntity> getExportListstaff(Integer projectid,BigDecimal staffid) throws Exception;
    
    
	@Select("SELECT ts.* FROM TBL_NBSJ_SHEET TS WHERE  TS.RISKLEVEL='是' AND TS.PROJECTID=#{projectid}")
	List<TblNbsjSheetEntity> findwtzs(String projectid) throws Exception;

     
	@Select("SELECT ts.* FROM TBL_NBSJ_SHEET TS LEFT JOIN TBL_NBSJ_QUESTION nq ON TS.SHEETID = nq.SHEETID WHERE TS.PROJECTID = #{projectid} AND NQ.RECSTATUS = 1 AND TS.RISKLEVEL='是' ")
	List<TblNbsjSheetEntity> finddzgwts(String projectid) throws Exception;
	
	
	@Select("SELECT sh.* FROM TBL_NBSJ_SHEET sh LEFT JOIN TBL_NBSJ_QUESTION nq ON SH.SHEETID = nq.SHEETID   WHERE SH.PROJECTID = #{projectid} AND SH.RISKLEVEL='是' AND NQ.QUESTIONID in (SELECT DISTINCT TC.QUESTIONID from TBL_NBSJ_REFOPM TC  WHERE  TC.LASTREFORMSTATUS = 1 AND TC.STATUS = 3 ) ")
	List<TblNbsjSheetEntity> findyzgwts(String projectid) throws Exception;
	
	@Select("SELECT sh.* FROM TBL_NBSJ_SHEET sh LEFT JOIN TBL_NBSJ_QUESTION nq ON SH.SHEETID = nq.SHEETID   WHERE SH.PROJECTID = #{projectid} AND SH.RISKLEVEL='是' AND NQ.QUESTIONID in (SELECT DISTINCT TC.QUESTIONID from TBL_NBSJ_REFOPM TC  WHERE  TC.LASTREFORMSTATUS = 1  AND ( TC.STATUS < 3 OR TC.STATUS IS NULL) ) ")
	List<TblNbsjSheetEntity> findwzgwts(String projectid) throws Exception;
    
}
