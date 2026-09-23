package com.huabo.cybermonitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hbfk.util.PageInfo;
import com.huabo.cybermonitor.entity.TblAttachment;
import io.lettuce.core.dynamic.annotation.Param;
import org.apache.ibatis.annotations.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Mapper
public interface TblAttachmentMapper extends BaseMapper<TblAttachment> {
	
	
	@Select("SELECT * FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_NBSJ_REFORM_ATT  WHERE REFORMID = #{reformid})")
	List<TblAttachment> selectAttListByRefromId(Integer reformid);
	

	@InsertProvider(type= TblAttachmentMapperSqlConfig.class,method="insertEntity")
	@Options(useGeneratedKeys=true, keyProperty="attid", keyColumn="ATTID")
	void insertEntity(TblAttachment tblAttachmentEntity) throws Exception;

	@Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER,ISPYTHONFLAG FROM TBL_ATTACHMENT WHERE ATTID = #{attId} ")
	@Results({
		@Result(column="ATTID",property="attid"),
		@Result(column="ATTNAME",property="attname"),
		@Result(column="ATTPATH",property="attpath"),
		@Result(column="ATTSIZE",property="attsize"),
		@Result(column="MEMO",property="memo"),
		@Result(column="UPLOADTIME",property="uploadtime"),
		@Result(column="UPLOADER",property="uploader"),
		@Result(column="ISPYTHONFLAG",property="ispythonflag"),
	})
	TblAttachment selectEntityById(@Param("attId") String attId) throws Exception;

	@Delete("DELETE FROM TBL_ATTACHMENT WHERE ATTID = #{attid}")
	void deleteEntity(@Param("attid")BigDecimal attid) throws Exception;

	@Select("SELECT * FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_YJPT_AUDITPLANATT  WHERE PLANID = #{planId})")
	List<TblAttachment> selectAttListByAuditPlanId(Integer planId);
	
	@Select("SELECT	* FROM	TBL_ATTACHMENT WHERE	ATTID IN (	SELECT	ATTID	FROM	TBL_NBSJ_OUTERRULE_ATT	WHERE	OUTRULID = #{outerId})")
	List<TblAttachment> findtTblAttachmentByOuter_SJ(BigDecimal outerId);
	
	@Select("SELECT	* FROM	TBL_ATTACHMENT WHERE	ATTID IN (	SELECT	ATTID	FROM	TBL_YJPT_INNERRULE_ATT	WHERE	INNRULID = #{innerId})")
	List<TblAttachment> findtTblAttachmentByInner_SJ(BigDecimal innerId);
	
	@Select("SELECT * FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_NBSJ_REFORM_SOLUTION_ATT  WHERE SOLUTIONID = #{solutionid})")
	List<TblAttachment> selectAttListBySolutionId(Integer solutionid);
	
	
	//==附件列表
	@SelectProvider(method="selectCountByPageInfo",type=TblAttachmentMapperSqlConfig.class)
   	Integer selectCountByPageInfo(PageInfo<TblAttachment> pageInfo, String attname, Integer orgid) throws Exception;

    @SelectProvider(method="selectListByPageInfo",type=TblAttachmentMapperSqlConfig.class)
    @Results({
    	@Result(column="ATTNAME",property="attname"),
    	@Result(column="ATTSIZE",property="attsize"),
    	@Result(column="UPLOADTIME",property="uploadtime"),
    	@Result(column="UPLOADER",property="uploader"),
    })
	List<TblAttachment> selectListByPageInfo(PageInfo<TblAttachment> pageInfo,String attname,Integer orgid) throws Exception;
    
    
    //==进场纪要附件============BEGIN
    @Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_LEGAL_ENTERMEETING_ATT WHERE ENTERID = #{enterid})")
    List<TblAttachment> findAttachmentListByPJEnterid(Integer enterid);
    
    @Insert("INSERT INTO TBL_LEGAL_ENTERMEETING_ATT(ENTERID,ATTID) VALUES(#{enterid},#{id})")
	void insertAttmentRelationEntermeeting(@Param("id")String id, @Param("enterid")Integer enterid);
    
    @Delete("DELETE FROM TBL_LEGAL_ENTERMEETING_ATT WHERE ENTERID=#{enterid}")
	void deleteAttmentRelationEntermeeting(@Param("enterid")Integer enterid);
    //==进场纪要附件============END
    
    //==离场纪要附件============BEGIN
    @Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_LEGAL_LEAVEMEETING_ATT WHERE LEAVEID = #{leaveid})")
    List<TblAttachment> findAttachmentListByPJLeaveid(Integer leaveid);
    
    @Insert("INSERT INTO TBL_LEGAL_LEAVEMEETING_ATT(LEAVEID,ATTID) VALUES(#{leaveid},#{id})")
	void insertAttmentRelationLeavemeeting(@Param("id")String id, @Param("leaveid")Integer leaveid);
    
    @Delete("DELETE FROM TBL_LEGAL_LEAVEMEETING_ATT WHERE LEAVEID=#{leaveid}")
	void deleteAttmentRelationLeavemeeting(@Param("leaveid")Integer leaveid);
    //==离场纪要附件============END
    
    //==我的底稿附件============BEGIN
    @Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_LEGAL_SHEET_ATT WHERE SHEETID = #{sheetid})")
    List<TblAttachment> findAttachmentListBySheetid(Integer sheetid);
    
    @Insert("INSERT INTO TBL_LEGAL_SHEET_ATT(SHEETID,ATTID) VALUES(#{sheetid},#{id})")
	void insertAttmentRelationSheet(@Param("id")String id, @Param("sheetid")Integer sheetid);
    
    @Delete("DELETE FROM TBL_LEGAL_SHEET_ATT WHERE SHEETID=#{sheetid}")
	void deleteAttmentRelationSheet(@Param("sheetid")Integer sheetid);
    //==我的底稿附件============END
    
    //==工作日志附件============BEGIN
    @Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_LEGAL_WORKREPORT_ATT WHERE REPORTID = #{reportid})")
    List<TblAttachment> findAttachmentListByReportid(Integer reportid);
    
    @Insert("INSERT INTO TBL_LEGAL_WORKREPORT_ATT(REPORTID,ATTID) VALUES(#{reportid},#{id})")
	void insertAttmentRelationWorkReport(@Param("id")String id, @Param("reportid")Integer reportid);
    
    @Delete("DELETE FROM TBL_LEGAL_WORKREPORT_ATT WHERE REPORTID=#{reportid}")
	void deleteAttmentRelationWorkReport(@Param("reportid")Integer reportid);
    //==工作日志附件============END
    
    //==疑点管理附件============BEGIN
    @Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_LEGAL_DOUBTFULPOINT_ATT WHERE DPOINTID = #{dpointid})")
    List<TblAttachment> findAttachmentListByDpointid(Integer dpointid);
    
    @Insert("INSERT INTO TBL_LEGAL_DOUBTFULPOINT_ATT(DPOINTID,ATTID) VALUES(#{dpointid},#{id})")
	void insertAttmentRelationDoubtfulpoint(@Param("id")String id, @Param("dpointid")Integer dpointid);
    
    @Delete("DELETE FROM TBL_LEGAL_DOUBTFULPOINT_ATT WHERE DPOINTID=#{dpointid}")
	void deleteAttmentRelationDoubtfulpoint(@Param("dpointid")Integer dpointid);
    //==疑点管理附件============END
    
    
    //==项目管理附件============BEGIN
    @Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_LEGAL_PROJECT_ATT WHERE PROJECTID = #{projectId})")
    List<TblAttachment> findAttachmentListByProjectid(Integer projectId);
    
    @Insert("INSERT INTO TBL_LEGAL_PROJECT_ATT(PROJECTID,ATTID) VALUES(#{projectId},#{id})")
	void insertAttmentRelationProject(@Param("id")String id, @Param("projectId")Integer projectId);
    
    @Delete("DELETE FROM TBL_LEGAL_PROJECT_ATT WHERE PROJECTID=#{projectId}")
	void deleteAttmentRelationProject(@Param("projectId")Integer projectId);
    //==项目管理附件============END
    
    
    //==项目资料准备-附件============BEGIN
    @Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_LEGAL_DATAPROJECT_ATT WHERE DATAID = #{dataId})")
    List<TblAttachment> findAttachmentListByDataid(Integer dataId);
    
    @Insert("INSERT INTO TBL_LEGAL_DATAPROJECT_ATT(DATAID,ATTID) VALUES(#{dataId},#{id})")
	void insertAttmentRelationDataPj(@Param("id")String id, @Param("dataId")Integer dataId);
    
    @Delete("DELETE FROM TBL_LEGAL_DATAPROJECT_ATT WHERE DATAID=#{dataId}")
	void deleteAttmentRelationDataPj(@Param("dataId")Integer dataId);
    //==项目资料准备-附件============END
    
    
    //==审计通知书-附件============BEGIN
    @Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_LEGAL_ADVICE_ATT WHERE ADVICEID = #{adviceid})")
    List<TblAttachment> findAttachmentListByAdviceid(Integer adviceid);
    
    @Insert("INSERT INTO TBL_LEGAL_ADVICE_ATT(ADVICEID,ATTID) VALUES(#{adviceid},#{id})")
	void insertAttmentRelationAdvice(@Param("id")String id, @Param("adviceid")Integer adviceid);
    
    @Delete("DELETE FROM TBL_LEGAL_ADVICE_ATT WHERE ADVICEID=#{adviceid}")
	void deleteAttmentRelationAdvice(@Param("adviceid")Integer adviceid);
    //==审计通知书-附件============END
    
    
    //==审计报告编制-附件============BEGIN
    @Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_LEGAL_REPORT_ATT WHERE REPORTID = #{reportid})")
    List<TblAttachment> findAttachmentListByReport(Integer reportid);
    //==审计报告编制-附件============END
    
    
    //==审计建议书-附件============BEGIN
    @Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_LEGAL_AUDITSUGGEST_ATT WHERE PROID = #{proid})")
    List<TblAttachment> findAttachmentListByAuditSuggest(Integer proid);
    
    @Insert("INSERT INTO TBL_LEGAL_AUDITSUGGEST_ATT(PROID,ATTID) VALUES(#{proid},#{id})")
	void insertAttmentRelationAuditSuggest(@Param("id")String id, @Param("proid")Integer proid);
    
    @Delete("DELETE FROM TBL_LEGAL_AUDITSUGGEST_ATT WHERE PROID=#{proid}")
	void deleteAttmentRelationAuditSuggest(@Param("proid")Integer proid);
    //==审计建议书-附件============END
    
    //==事实确认书-附件============BEGIN
    @Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_NBSJ_FACTBOOKATT WHERE FACTID = #{factid})")
    List<TblAttachment> findAttachmentListByFactbook(Integer factid);
    
    @Insert("INSERT INTO TBL_NBSJ_FACTBOOKATT(FACTID,ATTID) VALUES(#{factid},#{id})")
	void insertAttmentRelationFactbook(@Param("id")String id, @Param("factid")Integer factid);
    
    @Delete("DELETE FROM TBL_NBSJ_FACTBOOKATT WHERE FACTID=#{factid}")
	void deleteAttmentRelationFactbook(@Param("factid")Integer factid);
    //==事实确认书-附件============END
    
    
    //==法律规章-附件============BEGIN
    @Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_YJPT_OUTERRULE_ATT WHERE OUTRULID = #{outerId})")
    List<TblAttachment> findAttachmentListByOut(Integer outerId);
    //==法律规章-附件============END

    @UpdateProvider(method = "updateTblBugsInTblAttachment", type = TblAttachmentMapperSqlConfig.class)
    void updateTblBugsInTblAttachment(TblAttachment attachment);

    @Select("select ATTID from TBL_BUG_ATT where ATTID = #{bugid}")
    Set<BigDecimal> selectAttIdFromBugAtt(@org.apache.ibatis.annotations.Param("bugid") String bugid);
    
}
