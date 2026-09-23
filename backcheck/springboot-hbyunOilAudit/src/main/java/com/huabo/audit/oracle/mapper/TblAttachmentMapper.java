package com.huabo.audit.oracle.mapper;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

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

public interface TblAttachmentMapper{
	
	@Select("SELECT * FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_NBSJ_SJJYK_ATT  WHERE JYKID = #{jykid})")
	List<TblAttachment> selectAttListByJykId(BigDecimal jykid);
	
	@Select("SELECT * FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_NBSJ_MB_ATT  WHERE MBID = #{mbid})")
	List<TblAttachment> selectAttListByMbId(BigDecimal mbid);
	
	@Select("SELECT * FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_NBSJ_REFORM_ATT  WHERE REFORMID = #{reformid})")
	List<TblAttachment> selectAttListByRefromId(Integer reformid);




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
	TblAttachment selectEntityById(@Param("attId") String attId) throws Exception;

	@Delete("DELETE FROM TBL_ATTACHMENT WHERE ATTID = #{attid}")
	void deleteEntity(@Param("attid")BigDecimal attid) throws Exception;

	@Select("SELECT * FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_NBSJ_AUDITPLANATT  WHERE PLANID = #{planId})")
	List<TblAttachment> selectAttListByAuditPlanId(Integer planId);
	
	@Select("SELECT	* FROM	TBL_ATTACHMENT WHERE	ATTID IN (	SELECT	ATTID	FROM	TBL_NBSJ_OUTERRULE_ATT	WHERE	OUTRULID = #{outerId})")
	List<TblAttachment> findtTblAttachmentByOuter_SJ(BigDecimal outerId);
	
	@Select("SELECT	* FROM	TBL_ATTACHMENT WHERE	ATTID IN (	SELECT	ATTID	FROM	TBL_NBSJ_INNERRULE_ATT	WHERE	INNRULID = #{innerId})")
	List<TblAttachment> findtTblAttachmentByInner_SJ(BigDecimal innerId);
	
	@Select("SELECT * FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_NBSJ_REFORM_SOLUTION_ATT  WHERE SOLUTIONID = #{solutionid})")
	List<TblAttachment> selectAttListBySolutionId(Integer solutionid);
	
	
	//==附件列表
	@SelectProvider(method="selectCountByPageInfo",type=TblAttachmentMapperSqlConfig.class)
   	Integer selectCountByPageInfo(PageInfo<TblAttachment> pageInfo,String attname,Integer orgid) throws Exception;

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
    //==项目资料准备-附件============BEGIN
	/*
	 * @Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_LEGAL_DATAPROJECT_ATT WHERE DATAID = #{dataId}) and STAFFIDS like '%${staffid}%'"
	 * )
	 */
    @SelectProvider(method="findAttachmentListByData",type=TblAttachmentMapperSqlConfig.class)
    List<TblAttachment> findAttachmentListByDataids(Integer dataId,String staffid); 
    
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
    @Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_NBSJ_OUTERRULE_ATT WHERE OUTRULID = #{outerId})")
    List<TblAttachment> findAttachmentListByOut(Integer outerId);
    //==法律规章-附件============END
    
    
    //==审计取证单-附件============BEGIN
    @Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_NBSJ_CERTIFICATE_ATT WHERE certificateId = #{certificateId})")
    List<TblAttachment> findAttachmentListByCertificate(Integer certificateId);
    
    @Insert("INSERT INTO TBL_NBSJ_CERTIFICATE_ATT(certificateId,ATTID) VALUES(#{certificateId},#{id})")
	void insertAttmentRelationCertificate(@Param("id")String id, @Param("certificateId")Integer certificateId);
    
    @Delete("DELETE FROM TBL_NBSJ_CERTIFICATE_ATT WHERE certificateId=#{certificateId}")
	void deleteAttmentRelationCertificate(@Param("certificateId")Integer certificateId);
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


    @Select("SELECT * FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_NBSJ_REFORM_RECTIFY  WHERE ISSUEID = #{issueId})")
    List<TblAttachment> selectAttListByIssueId(BigDecimal issueId);

    @Delete("DELETE FROM TBL_NBSJ_REFORM_RECTIFY WHERE ATTID=#{attId} AND ISSUEID=#{issueId}")
    void deleteAttachmentByIssueIdAndAttId(String attId,BigDecimal issueId);

    @Insert("INSERT INTO TBL_NBSJ_REFORM_RECTIFY(ATTID,ISSUEID) VALUES (#{attId},#{issueId})")
    void insertAttachmentByIssueId(String attId,BigDecimal issueId);

    @Select("SELECT * FROM TBL_NBSJ_REFORM_RECTIFY WHERE ATTID=#{attId} AND ISSUEID=#{issueId}")
    Object selectAttachmentByIssueId(String attId, Long issueId);



    @Insert("INSERT INTO TBL_NBSJ_REFORM_RECTIFY(ATTID,REPORTID) VALUES (#{attId},#{reportId})")
    void insertAttachmentByReportId(String attId,Long reportId);

    @Delete("DELETE FROM TBL_NBSJ_REFORM_RECTIFY WHERE ATTID=#{attId} AND REPORTID=#{reportId}")
    void deleteAttachmentByReportIdAndAttId(String attId,Integer reportId);
    
    
    @Select("SELECT * FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_YQNS_SHEET_ATT  WHERE SHEETID = #{sheetid})")
	List<TblAttachment> selectAttListBysheetId(BigDecimal sheetid);
    
    
    @Select("SELECT * FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_YQNS_LETTER_ATT  WHERE LETTERID = #{letterid})")
  	List<TblAttachment> selectAttListByletterid(BigDecimal letterid);
    
    @Select("SELECT * FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_YQNS_RESULT_ATT  WHERE RESULTID = #{resultid})")
  	List<TblAttachment> selectAttListByresultid(BigDecimal sheetid);
    
    
    @Insert("INSERT INTO TBL_PROJECT_EVALUATION_ATT(ATTID,ID) VALUES (#{atid},#{idKey})")
    void insertAttachmentByProjectEvaluationAttId(@org.apache.ibatis.annotations.Param("atid")BigDecimal atid,@org.apache.ibatis.annotations.Param("idKey")BigDecimal idKey);

    @Delete("DELETE FROM TBL_PROJECT_EVALUATION_ATT WHERE ATTID=#{attid} AND ID=#{id}")
    void deleteAttachmentByProjectEvaluationAttId(@org.apache.ibatis.annotations.Param("attid")BigDecimal attid,@org.apache.ibatis.annotations.Param("id")BigDecimal id);
    
    
    @Delete("DELETE FROM TBL_PROJECT_EVALUATION_ATT WHERE ID=#{id}")
    void deleteAttachmentByProjectEvaluationAtt(@org.apache.ibatis.annotations.Param("id")BigDecimal id);
    
    @Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_PROJECT_EVALUATION_ATT WHERE ID = #{id})")
    List<TblAttachment> findAttachmentListByProjectEvaluationAtt(@org.apache.ibatis.annotations.Param("id")BigDecimal id);
    
    
    //项目启动附件列表
    @Select("SELECT * FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_YQNS_XMQD_ATT  WHERE XMDQID = #{xmdqid})")
  	List<TblAttachment> selectAttListByxmdqid(BigDecimal xmdqid);
    
     
  //项目延期申请附件列表
    @Select("SELECT * FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_YQNS_XMYQSQ_ATT  WHERE XMDQID = #{xmdqid})")
  	List<TblAttachment> selectAttListByxmyqid(BigDecimal xmdqid);

    //其他审计附件列表
    @Select("SELECT * FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_YQNS_OTHERAUDIT_ATT  WHERE AUDITID = #{auditId})")
	List<TblAttachment> selectOtherAuditAttInfoList(BigDecimal auditId) throws Exception;
    
    //审计督导记录附件列表
    @Select("SELECT * FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_YQNS_OVERSEE_RECORDS_ATT  WHERE DUDAOID = #{id})")
	List<TblAttachment> selectDUDAOAttInfoList(Long id) throws Exception;
    
    
    //理论研讨通知附件列表
    @Select("SELECT * FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_YQNS_THEORY_ATT  WHERE RYID = #{ryid})")
  	List<TblAttachment> selectAttListByryid(BigDecimal ryid);
    
    
  //论文上报附件列表
    @Select("SELECT * FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_YQNS_PAPER_ATT  WHERE PERID = #{perid})")
  	List<TblAttachment> selectAttListBylwid(BigDecimal perid);

    @Delete("DELETE FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_YQNS_SJZG_WTZG_ATTACH WHERE WTZGID = #{wtzgid}) ")
	void deleteAttachmentByWtzzgId(@Param("wtzgid")BigDecimal wtzgid) throws Exception;
    
    
  //审计意见书附件列表
    @Select("SELECT * FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_YQNS_PROPOSALBG_ATT  WHERE BGID = #{bgid})")
  	List<TblAttachment> selectAttListBybgid(BigDecimal bgid);
 
    //财务项目安排填报表附件
    @Select("SELECT * FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_YQNS_FUNDTB_ATT  WHERE TBID = #{tbid})")
  	List<TblAttachment> selectAttListBytbid(String tbid);
    
  //工程项目安排填报表附件
    @Select("SELECT * FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_YQNS_ENGINTB_ATT  WHERE TBID = #{tbid})")
  	List<TblAttachment> selectAttListBygctbid(String tbid);
    
    
    //获奖通知附件列表
    @Select("SELECT * FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_YQNS_HJTZ_ATT  WHERE RYID = #{ryid})")
  	List<TblAttachment> selectAttListByyjtzid(BigDecimal ryid);
    
    //项目评优-获奖通知附件列表
    @Select("SELECT * FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_YQNS_XMHJTZ_ATT  WHERE RYID = #{ryid})")
  	List<TblAttachment> selectAttListBXmtzid(BigDecimal ryid);
}
