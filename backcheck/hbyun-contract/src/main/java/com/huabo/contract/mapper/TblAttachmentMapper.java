package com.huabo.contract.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.contract.entity.TblAttachment;
import com.huabo.contract.mappersql.TblAttachmentMapperSqlConfig;

public interface TblAttachmentMapper extends BaseMapper<TblAttachment> {

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
	TblAttachment findById(@Param("attid") String attid);

	@Delete("DELETE FROM TBL_ATTACHMENT WHERE ATTID = #{attid}")
	void deleteAtt(String attid) throws Exception;

	@Delete("DELETE FROM TBL_CYHW_UNIT_ATT WHERE ATTID = #{attid}")
    void removeUnitAttByattid(BigDecimal attid);

	@Delete("DELETE FROM TBL_ATTACHMENT where ATTID= #{attid}")
	void deleteAttachmentByattId(BigDecimal attid);

	@Delete("DELETE FROM TBL_ATTACHMENT WHERE ATTID = #{attid}")
	void deleteAttid(BigDecimal attid) throws Exception;

	@Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_CYHW_UNIT_ATT WHERE contractId = #{contractId})")
    List<TblAttachment> findAttachmentListById(BigDecimal contractId);

	@Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_CYHW_PROJECTBUDGET_ATT WHERE BUDGETID = #{budgetId})")
	List<TblAttachment> findAttachmentListByBudgetId(BigDecimal budgetId);
	
	
	@Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_LEGAL_CONTRACT_PROJECT_ATT WHERE PROJECTID = #{projectid})")
    List<TblAttachment> findAttachmentListByPJId(BigDecimal projectid);
	
	@Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT EQBFLOWID from TBL_CYHW_PROJECTBUDGET where recordparent=#{contractId} AND RECORDTYPE='HTGL003' );")
    List<TblAttachment> findAttachmentListByIds(BigDecimal contractId);

	@Delete(" DELETE FROM TBL_CONTRACTNODE_ATT where ATTID= #{attid}")
	void deleteContractNode(BigDecimal attid) throws Exception;
	
	@Select("SELECT	* FROM	TBL_ATTACHMENT WHERE	ATTID IN (	SELECT	ATTID	FROM	TBL_CONTRACT_INNERRULE_ATT	WHERE	INNRULID = #{innerId})")
	List<TblAttachment> findtTblAttachmentByInner_SJ(BigDecimal innerId);
	
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
	
	//==诉讼过程-附件============BEGIN
    @Select("SELECT ATTID,ATTNAME,ATTPATH,ATTSIZE,MEMO,UPLOADTIME,UPLOADER FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_LEGAL_PRECORD_ATT WHERE PROCEEDID = #{proceedId})")
    List<TblAttachment> findAttachmentproceed(BigDecimal proceedId);

    @Select("SELECT SINGINGPATH FROM TBL_CONTRACT_APPENDIXSIGNING WHERE SINGINGID = #{attId}")
 	String selectSignFileNameById(Integer attId) throws Exception;

    @Select("SELECT SINGINGPATH FROM TBL_CONTRACT_APPENDIXSIGNING WHERE SINGINGID = #{attId}")
 	String selectSignFileById(String attId) throws Exception;
    
     @Select("SELECT ATTPATH FROM TBL_ATTACHMENT WHERE ATTID = #{attId}")
 	String selectUploadFileNameById(Integer attId) throws Exception;

     @InsertProvider(method="add",type=TblAttachmentMapperSqlConfig.class)
 	@Options(useGeneratedKeys=true, keyProperty="attid", keyColumn="ATTID")
 	void add(TblAttachment att);

    @InsertProvider(type=TblAttachmentMapperSqlConfig.class,method="insertEntity")
 	@Options(useGeneratedKeys=true, keyProperty="attid", keyColumn="ATTID")
	void insertEntity(TblAttachment tblAttachmentEntity);


}
