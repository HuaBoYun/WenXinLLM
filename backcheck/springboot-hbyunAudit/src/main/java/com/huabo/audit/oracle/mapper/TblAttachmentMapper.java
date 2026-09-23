package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;
import java.util.List;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblNbsjDoubtfulpointEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.huabo.audit.oracle.entity.TblAttachment;
import com.huabo.audit.util.PageInfo;

import io.lettuce.core.dynamic.annotation.Param;

public interface TblAttachmentMapper extends BaseMapper<TblAttachment> {
	
	@Select("SELECT * FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_NBSJ_SJJYK_ATT  WHERE JYKID = #{jykid})")
	List<TblAttachment> selectAttListByJykId(BigDecimal jykid);
	
	@Select("SELECT * FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_NBSJ_MB_ATT  WHERE MBID = #{mbid})")
	List<TblAttachment> selectAttListByMbId(BigDecimal mbid);
	
	@Select("SELECT * FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_NBSJ_REFORM_ATT  WHERE REFORMID = #{reformid})")
	List<TblAttachment> selectAttListByRefromId(BigDecimal reformid);
	

	@InsertProvider(type=TblAttachmentMapperSqlConfig.class,method="insertEntity")
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
	TblAttachment selectEntityById(@Param("attId") BigDecimal attId) throws Exception;

	@Delete("DELETE FROM TBL_ATTACHMENT WHERE ATTID = #{attid}")
	void deleteEntity(@Param("attid")BigDecimal attid) throws Exception;

	@Select("SELECT * FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_NBSJ_AUDITPLANATT  WHERE PLANID = #{planId})")
	List<TblAttachment> selectAttListByAuditPlanId(BigDecimal planId);
	
	@Select("SELECT	* FROM	TBL_ATTACHMENT WHERE	ATTID IN (	SELECT	ATTID	FROM	TBL_NBSJ_OUTERRULE_ATT	WHERE	OUTRULID = #{outerId})")
	List<TblAttachment> findtTblAttachmentByOuter_SJ(BigDecimal outerId);
	
	@Select("SELECT	* FROM	TBL_ATTACHMENT WHERE	ATTID IN (	SELECT	ATTID	FROM	TBL_NBSJ_INNERRULE_ATT	WHERE	INNRULID = #{innerId})")
	List<TblAttachment> findtTblAttachmentByInner_SJ(BigDecimal innerId);
	
	@Select("SELECT * FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_NBSJ_REFORM_SOLUTION_ATT  WHERE SOLUTIONID = #{solutionid})")
	List<TblAttachment> selectAttListBySolutionId(BigDecimal solutionid);
	
	
	//==附件列表
	@SelectProvider(method="selectCountByPageInfo",type=TblAttachmentMapperSqlConfig.class)
   	Integer selectCountByPageInfo(PageInfo<TblAttachment> pageInfo,String attname,BigDecimal orgid) throws Exception;

    @SelectProvider(method="selectListByPageInfo",type=TblAttachmentMapperSqlConfig.class)
    @Results({
    	@Result(column="ATTNAME",property="attname"),
    	@Result(column="ATTSIZE",property="attsize"),
    	@Result(column="UPLOADTIME",property="uploadtime"),
    	@Result(column="UPLOADER",property="uploader"),
    })
	List<TblAttachment> selectListByPageInfo(PageInfo<TblAttachment> pageInfo,String attname,BigDecimal orgid) throws Exception;
    
    
    //==进场纪要附件============BEGIN
    @Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_LEGAL_ENTERMEETING_ATT WHERE ENTERID = #{enterid})")
    List<TblAttachment> findAttachmentListByPJEnterid(BigDecimal enterid);
    
    @Insert("INSERT INTO TBL_LEGAL_ENTERMEETING_ATT(ENTERID,ATTID) VALUES(#{enterid},#{id})")
	void insertAttmentRelationEntermeeting(@Param("id")String id, @Param("enterid")BigDecimal enterid);
    
    @Delete("DELETE FROM TBL_LEGAL_ENTERMEETING_ATT WHERE ENTERID=#{enterid}")
	void deleteAttmentRelationEntermeeting(@Param("enterid")BigDecimal enterid);
    //==进场纪要附件============END
    
    //==离场纪要附件============BEGIN
    @Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_LEGAL_LEAVEMEETING_ATT WHERE LEAVEID = #{leaveid})")
    List<TblAttachment> findAttachmentListByPJLeaveid(BigDecimal leaveid);
    
    @Insert("INSERT INTO TBL_LEGAL_LEAVEMEETING_ATT(LEAVEID,ATTID) VALUES(#{leaveid},#{id})")
	void insertAttmentRelationLeavemeeting(@Param("id")String id, @Param("leaveid")BigDecimal leaveid);
    
    @Delete("DELETE FROM TBL_LEGAL_LEAVEMEETING_ATT WHERE LEAVEID=#{leaveid}")
	void deleteAttmentRelationLeavemeeting(@Param("leaveid")BigDecimal leaveid);
    //==离场纪要附件============END
    
    //==我的底稿附件============BEGIN
    @Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER,JMURL FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_LEGAL_SHEET_ATT WHERE SHEETID = #{sheetid})")
    List<TblAttachment> findAttachmentListBySheetid(BigDecimal sheetid);
    
    @Insert("INSERT INTO TBL_LEGAL_SHEET_ATT(SHEETID,ATTID) VALUES(#{sheetid},#{id})")
	void insertAttmentRelationSheet(@Param("id")String id, @Param("sheetid")BigDecimal sheetid);
    
    @Delete("DELETE FROM TBL_LEGAL_SHEET_ATT WHERE SHEETID=#{sheetid}")
	void deleteAttmentRelationSheet(@Param("sheetid")BigDecimal sheetid);
    //==我的底稿附件============END
    
    //==工作日志附件============BEGIN
    @Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_LEGAL_WORKREPORT_ATT WHERE REPORTID = #{reportid})")
    List<TblAttachment> findAttachmentListByReportid(BigDecimal reportid);
    
    @Insert("INSERT INTO TBL_LEGAL_WORKREPORT_ATT(REPORTID,ATTID) VALUES(#{reportid},#{id})")
	void insertAttmentRelationWorkReport(@Param("id")String id, @Param("reportid")BigDecimal reportid);
    
    @Delete("DELETE FROM TBL_LEGAL_WORKREPORT_ATT WHERE REPORTID=#{reportid}")
	void deleteAttmentRelationWorkReport(@Param("reportid")BigDecimal reportid);
    //==工作日志附件============END
    
    //==疑点管理附件============BEGIN
    @Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_LEGAL_DOUBTFULPOINT_ATT WHERE DPOINTID = #{dpointid})")
    List<TblAttachment> findAttachmentListByDpointid(BigDecimal dpointid);
    
    @Insert("INSERT INTO TBL_LEGAL_DOUBTFULPOINT_ATT(DPOINTID,ATTID) VALUES(#{dpointid},#{id})")
	void insertAttmentRelationDoubtfulpoint(@Param("id")String id, @Param("dpointid")BigDecimal dpointid);
    
    @Delete("DELETE FROM TBL_LEGAL_DOUBTFULPOINT_ATT WHERE DPOINTID=#{dpointid}")
	void deleteAttmentRelationDoubtfulpoint(@Param("dpointid")BigDecimal dpointid);
    //==疑点管理附件============END
    
    
    //==项目管理附件============BEGIN
    @Select("SELECT * FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_LEGAL_PROJECT_ATT WHERE PROJECTID = #{projectId})")
    List<TblAttachment> findAttachmentListByProjectid(BigDecimal projectId);
    
    
    @Insert("INSERT INTO TBL_LEGAL_PROJECT_ATT(PROJECTID,ATTID) VALUES(#{projectId},#{id})")
	void insertAttmentRelationProject(@Param("id")String id, @Param("projectId")BigDecimal projectId);
    
    @Delete("DELETE FROM TBL_LEGAL_PROJECT_ATT WHERE PROJECTID=#{projectId}")
	void deleteAttmentRelationProject(@Param("projectId")BigDecimal projectId);
    //==项目管理附件============END
    
    
    //==项目资料准备-附件============BEGIN
    @Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_LEGAL_DATAPROJECT_ATT WHERE DATAID = #{dataId})")
    List<TblAttachment> findAttachmentListByDataid(BigDecimal dataId);
    //==项目资料准备-附件============BEGIN
	/*
	 * @Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_LEGAL_DATAPROJECT_ATT WHERE DATAID = #{dataId}) and STAFFIDS like '%${staffid}%'"
	 * )
	 */
    @SelectProvider(method="findAttachmentListByData",type=TblAttachmentMapperSqlConfig.class)
    List<TblAttachment> findAttachmentListByDataids(BigDecimal dataId,String staffid);
    
    @Insert("INSERT INTO TBL_LEGAL_DATAPROJECT_ATT(DATAID,ATTID) VALUES(#{dataId},#{id})")
	void insertAttmentRelationDataPj(@Param("id")String id, @Param("dataId")BigDecimal dataId);
    
    @Delete("DELETE FROM TBL_LEGAL_DATAPROJECT_ATT WHERE DATAID=#{dataId}")
	void deleteAttmentRelationDataPj(@Param("dataId")BigDecimal dataId);
    //==项目资料准备-附件============END
    
    
    //==审计通知书-附件============BEGIN
    @Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_LEGAL_ADVICE_ATT WHERE ADVICEID = #{adviceid})")
    List<TblAttachment> findAttachmentListByAdviceid(BigDecimal adviceid);
    
    @Insert("INSERT INTO TBL_LEGAL_ADVICE_ATT(ADVICEID,ATTID) VALUES(#{adviceid},#{id})")
	void insertAttmentRelationAdvice(@Param("id")String id, @Param("adviceid")BigDecimal adviceid);
    
    @Delete("DELETE FROM TBL_LEGAL_ADVICE_ATT WHERE ADVICEID=#{adviceid}")
	void deleteAttmentRelationAdvice(@Param("adviceid")BigDecimal adviceid);
    //==审计通知书-附件============END
    
    
    //==审计报告编制-附件============BEGIN
    @Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_LEGAL_REPORT_ATT WHERE REPORTID = #{reportid} AND FILETYPE IS NULL)")
    List<TblAttachment> findAttachmentListByReport(BigDecimal reportid);
    //==审计报告编制-附件============END
    
    //==审计报告编制-复核意见附件============BEGIN
    @Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_LEGAL_REPORT_ATT WHERE REPORTID = #{reportid} AND FILETYPE = 2)")
    List<TblAttachment> findAttListByOpinionReport(BigDecimal reportid);
    //==审计报告编制-复核意见附件============END
    
    //==审计报告编制-终稿附件============BEGIN
    @Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_LEGAL_REPORT_ATT WHERE REPORTID = #{reportid} AND FILETYPE = 3)")
    List<TblAttachment> findAttListByFinalReport(BigDecimal reportid);
    //==审计报告编制-终稿附件============END
    
    
    //==审计建议书-附件============BEGIN
    @Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_LEGAL_AUDITSUGGEST_ATT WHERE PROID = #{proid})")
    List<TblAttachment> findAttachmentListByAuditSuggest(BigDecimal proid);
    
    @Insert("INSERT INTO TBL_LEGAL_AUDITSUGGEST_ATT(PROID,ATTID) VALUES(#{proid},#{id})")
	void insertAttmentRelationAuditSuggest(@Param("id")String id, @Param("proid")BigDecimal proid);
    
    @Delete("DELETE FROM TBL_LEGAL_AUDITSUGGEST_ATT WHERE PROID=#{proid}")
	void deleteAttmentRelationAuditSuggest(@Param("proid")BigDecimal proid);
    //==审计建议书-附件============END
    
    //==事实确认书-附件============BEGIN
    @Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_NBSJ_FACTBOOKATT WHERE FACTID = #{factid})")
    List<TblAttachment> findAttachmentListByFactbook(BigDecimal factid);
    
    @Insert("INSERT INTO TBL_NBSJ_FACTBOOKATT(FACTID,ATTID) VALUES(#{factid},#{id})")
	void insertAttmentRelationFactbook(@Param("id")String id, @Param("factid")BigDecimal factid);
    
    @Delete("DELETE FROM TBL_NBSJ_FACTBOOKATT WHERE FACTID=#{factid}")
	void deleteAttmentRelationFactbook(@Param("factid")BigDecimal factid);
    //==事实确认书-附件============END
    
    
    //==法律规章-附件============BEGIN
    @Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_NBSJ_OUTERRULE_ATT WHERE OUTRULID = #{outerId})")
    List<TblAttachment> findAttachmentListByOut(BigDecimal outerId);
    //==法律规章-附件============END
    
    
    //==审计取证单-附件============BEGIN
    @Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_NBSJ_CERTIFICATE_ATT WHERE certificateId = #{certificateId})")
    List<TblAttachment> findAttachmentListByCertificate(BigDecimal certificateId);
    
    @Insert("INSERT INTO TBL_NBSJ_CERTIFICATE_ATT(certificateId,ATTID) VALUES(#{certificateId},#{id})")
	void insertAttmentRelationCertificate(@Param("id")String id, @Param("certificateId")BigDecimal certificateId);
    
    @Delete("DELETE FROM TBL_NBSJ_CERTIFICATE_ATT WHERE certificateId=#{certificateId}")
	void deleteAttmentRelationCertificate(@Param("certificateId")BigDecimal certificateId);
    //==审计取证单-附件============END
    
	/* @Select("SELECT * FROM TBL_ATTACHMENT WHERE ATTID IN (#{attids})") */
    @SelectProvider(method="findAttachmentListByattids",type=TblAttachmentMapperSqlConfig.class)
    List<TblAttachment> findAttachmentListByattids(String attids);
    
	/*
	 * @Insert("UPDATE TBL_ATTACHMENT SET  STAFFIDS=#{staffids} WHERE ATTID=#{attid}"
	 * )
	 */
    @SelectProvider(method="updatexfry",type=TblAttachmentMapperSqlConfig.class)
   	void updatexfry(String staffids, BigDecimal attid);
    
    /**
     * 	整改追责附件列表
     * @param issuesId 整改追责主键
     * @return
     */
    @Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_ISSUES_ATT WHERE ISSUESID = #{issuesId}) ORDER BY ATTID ASC")
	List<TblAttachment> selectAttListbyIssuesId(@Param("issuesId")String issuesId);
    
    /**
     * 	整改落实 查找落实人上传的附件
     * @param implId 整改落实主键
     * @return
     */
    @Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_RECTIFICATIONIMPL_ATT WHERE IMPLID = #{implId}) ORDER BY ATTID ASC ")
	List<TblAttachment> selectAttListbyRectificationImpl(String implId) throws Exception;

    /**
     * 	整改评价 查找附件信息
     * @param evalId  整改评价主键
     * @return
     * @throws Exception
     */
    @Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_RECTVAL_ATT WHERE EVALID = #{evalId}) ORDER BY ATTID ASC")
	List<TblAttachment> selectAttListByZgzzEvaluation(String evalId) throws Exception;
    
    /**
     * 	整改追责整改方案附件列表
     * @param planId 整改方案主键
     * @return
     */
    @Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_RECTIFICATION_ATT WHERE PLANID = #{planId}) ORDER BY ATTID ASC ")
	List<TblAttachment> selectAttListbyRectification(String planId);

    /**
	 * 整改方案清单中间表 查询所有附件信息
	 * @param reportid
	 * @return
	 * @throws Exception
	 */
    @Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_PLANISSUES_ATT WHERE RELAID = #{relaId}) ORDER BY ATTID ASC")
	List<TblAttachment> selectAttListbyRelaId(@Param("relaId") String relaId) throws Exception;

    /**
	 * 整改报告查询所有附件信息
	 * @param reportid
	 * @return
	 * @throws Exception
	 */
    @Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_ZGZZREPORT_ATT WHERE REPORTID = #{reportid}) ORDER BY ATTID ASC")
	List<TblAttachment> selectAttListbyZgzzReportId(@Param("reportid") String reportid) throws Exception;

	@SelectProvider(method="findAttachmentListInIds",type=TblAttachmentMapperSqlConfig.class)
    List<TblAttachment> findAttachmentListInIds(String attids);


    @Select("SELECT LEVELID FROM TBL_SECRECT_LEVEL WHERE LEVELTYPE = 4 AND LEVELNAME =#{attachmentLevel}")
    BigDecimal selectattachmentLevel(@Param("attachmentLevel")String attachmentLevel) throws Exception;

    @Select("SELECT COUNT(*) FROM TBL_SECRECT_LEVEL WHERE LEVELTYPE = 2 AND LEVELNAME = #{formlevel} AND  SECRECYMENUSCOPE LIKE concat('%',#{attachmentLevelId},'%')")
    Integer selectattachmentList(@Param("formlevel")String formlevel,@Param("attachmentLevelId")String attachmentLevelId) throws Exception;

    @Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_NBSJ_CERTIFICATE_STAMPFILE WHERE CERTIFICATEID = #{certificateId}) ORDER BY ATTID ASC")
	List<TblAttachment> selectAttStampedFileListByQzd(@Param("certificateId")BigDecimal certificateId) throws Exception;

    @Select("SELECT DPNUMBER,DPNAME,DPDESCRIBE,TESTRESULT,EDITOR,edittime,memo FROM TBL_NBSJ_DOUBTFULPOINT where DPOINTID in (${selectIds}) order by DPOINTID DESC")
    List<TblNbsjDoubtfulpointEntity> exclTblDoubtfulpointByids(@Param("selectIds")String selectIds) throws Exception;

}
