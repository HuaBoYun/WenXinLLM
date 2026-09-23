package com.huabo.fxgl.mapper;

import org.apache.ibatis.annotations.*;

import com.huabo.fxgl.entity.TblAttachment;

import java.math.BigDecimal;
import java.util.List;

public interface TblAttachmentMapper {

	//@Insert("INSERT INTO TBL_ATTACHMENT (ATTID, ATTNAME, ATTPATH, ATTSIZE, MEMO, UPLOADTIME, UPLOADER, ISPYTHONFLAG) VALUES (ATTCHMENT_SEQUENCE.nextval, #{attname}, #{attpath}, #{attsize},  #{memo}, TO_DATE(#{uploadtime}, 'SYYYY-MM-DD HH24:MI:SS'), #{uploader},  #{ispythonflag})")
	/*@InsertProvider(method="add",type=TblAttachmentMapperSqlConfig.class)
	@Options(useGeneratedKeys=true, keyProperty="attid", keyColumn="ATTID")
	void add(TblAttachment att);

	@Insert("INSERT INTO TBL_ATTACHMENT (ATTID, ATTNAME, ATTPATH, ATTSIZE, UPLOADER,UPLOADTIME) VALUES (ATTCHMENT_SEQUENCE.nextval, #{attname}, #{attpath}, #{attsize}, #{uploader},#{uploadtime})")
	@Options(useGeneratedKeys=true, keyProperty="attid", keyColumn="ATTID")
	void insertAttachment(TblAttachment att);

	@Delete("DELETE FROM TBL_CYHW_PROJECTBUDGET_ATT WHERE ATTID = #{attid}")
	void deleteOppsiteFileRelatioin(@Param("attid") String attid) throws Exception;
	
	@Delete("DELETE FROM TBL_ATTACHMENT WHERE ATTID = #{attid}")
	void deleteFileInfoById(@Param("attid")String attid) throws Exception;


	@Select("SELECT * FROM TBL_ATTACHMENT WHERE ATTID = #{id}")
    TblAttachment getId(BigDecimal id);

	@Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,ISPYTHONFLAG FROM TBL_ATTACHMENT WHERE ATTID = #{attid}")
	@Results({
		@Result(column="ATTID",property="attid"),
		@Result(column="ATTNAME",property="attname"),
		@Result(column="ATTPATH",property="attpath"),
		@Result(column="ATTSIZE",property="attsize"),
		@Result(column="MEMO",property="memo"),
		@Result(column="ISPYTHONFLAG",property="ispythonflag"),
	})
	TblAttachment findById(String attid);

	@Delete("DELETE FROM TBL_ATTACHMENT WHERE ATTID = #{attid}")
	void deleteAtt(String attid);

	@Delete("DELETE FROM TBL_CYHW_UNIT_ATT WHERE ATTID = #{attid}")
    void removeUnitAttByattid(Integer attid);

	@Delete("DELETE FROM TBL_ATTACHMENT where ATTID= #{attid}")
	void deleteAttachmentByattId(Integer attid);

	@Delete("DELETE FROM TBL_ATTACHMENT WHERE ATTID = #{attid}")
	void deleteAttid(BigDecimal attid);

	@Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_CYHW_UNIT_ATT WHERE contractId = #{contractId})")
    List<TblAttachment> findAttachmentListById(Integer contractId);

	@Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_CYHW_PROJECTBUDGET_ATT WHERE BUDGETID = #{budgetId})")
	List<TblAttachment> findAttachmentListByBudgetId(Integer budgetId);
	
	
	@Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_LEGAL_CONTRACT_PROJECT_ATT WHERE PROJECTID = #{projectid})")
    List<TblAttachment> findAttachmentListByPJId(Integer projectid);
	
	@Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT EQBFLOWID from TBL_CYHW_PROJECTBUDGET where recordparent=#{contractId} AND RECORDTYPE='HTGL003' );")
    List<TblAttachment> findAttachmentListByIds(Integer contractId);*/

	@InsertProvider(type=TblAttachmentMapperSqlConfig.class,method="insertEntity")
	@Options(useGeneratedKeys=true, keyProperty="attid", keyColumn="ATTID")
	void insertEntity(TblAttachment tblAttachmentEntity) throws Exception;

	/*@Delete(" DELETE FROM TBL_CONTRACTNODE_ATT where ATTID= #{attid}")
	void deleteContractNode(Integer attid) throws Exception;*/
	
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
	
	
	@Insert("INSERT INTO TBL_LEGAL_BUG_ATT (ATTID, BUGID) VALUES (#{attId},#{moduleId})")
	void insertQxglFile(@Param("moduleId")BigDecimal moduleId,@Param("attId")BigDecimal attId);
	
	
	@Insert("INSERT INTO TBL_RISK_COPINGATT (ATTID, RISKCOPINGID) VALUES (#{attId},#{riskCopingId})")
	void insertFxydFile(@Param("riskCopingId")BigDecimal riskCopingId,@Param("attId")BigDecimal attId);
	

}
