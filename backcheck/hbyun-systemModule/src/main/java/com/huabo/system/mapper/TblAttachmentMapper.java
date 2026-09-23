package com.huabo.system.mapper;

import io.lettuce.core.dynamic.annotation.Param;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.system.entity.TblAttachment;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2022-04-28
 */
public interface TblAttachmentMapper extends BaseMapper<TblAttachment> {

    @Select("SELECT * FROM TBL_ATTACHMENT TA LEFT JOIN TBL_NBSJ_AUDITPLANATT TNAT ON TA.ATTID = TNAT.ATTID  " +
            " LEFT JOIN TBL_NBSJ_AUDITPLAN TNA ON TNAT.PLANID = TNA.PLANID WHERE TA.ATTID = #{attid} ")
    com.hbfk.entity.TblAttachment selectAtt(BigDecimal attid);

    @Select("select * from TBL_ATTACHMENT TA LEFT JOIN TBL_NBSJ_SHEETATT TNSH ON TA.ATTID = TNSH.ATTID LEFT JOIN TBL_NBSJ_SHEET TNS ON TNSH.SHEETID = TNS.SHEETID WHERE TNSH.SHEETID = #{id} ")
    List<com.hbfk.entity.TblAttachment> findAllByTblNBSJSheet(String id);
    
   
	
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
	com.hbfk.entity.TblAttachment selectEntityById(@Param("attId") String attId) throws Exception;

	@Delete("DELETE FROM TBL_ATTACHMENT WHERE ATTID = #{attid}")
	void deleteEntity(@Param("attid")BigDecimal attid) throws Exception;
	
	 //培训资料信息附件列表
    @Select("SELECT * FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM tbl_training_att  WHERE trainid = #{trainid})")
	List<com.hbfk.entity.TblAttachment> selectTrainAttListByTrainid(String trainid);
    
    //人员信息附件列表
    @Select("SELECT * FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_TRAININGSTAFF_ATT  WHERE staffid = #{staffid})")
	List<com.hbfk.entity.TblAttachment> selectAttListByStaffId(String staffid);

    @Select("SELECT SINGINGPATH FROM TBL_CONTRACT_APPENDIXSIGNING WHERE SINGINGID = #{attId}")
	String selectSignFileNameById(BigDecimal attId) throws Exception;

    @Select("SELECT ATTPATH FROM TBL_ATTACHMENT WHERE ATTID = #{attId}")
	String selectUploadFileNameById(BigDecimal attId) throws Exception;
    
    @Select("SELECT CONTENTPDFPATH FROM TBL_CONTRACT_CONTENTPDF WHERE CONTENTPDFID = #{contentPdfId}")
    String findBycontentPdfId(BigDecimal attId);
    
    @Select("SELECT FILEPATH FROM TBL_CONTRACT_EXAMFILE WHERE ID = #{id}")
    String findBycontentExamId(BigDecimal attId);


	@Select("SELECT LEVELID FROM TBL_SECRECT_LEVEL WHERE LEVELTYPE = 4 AND LEVELNAME =#{attachmentLevel}")
	BigDecimal selectattachmentLevel(@io.lettuce.core.dynamic.annotation.Param("attachmentLevel")String attachmentLevel) throws Exception;


    
    
    //知识共享-文库附件
    @Select("SELECT * FROM TBL_ATTACHMENT WHERE ATTID IN (SELECT ATTID FROM TBL_ZSGX_LIBRARY_ATT  WHERE LIBRARYID = #{libraryid})")
	List<com.hbfk.entity.TblAttachment> selectTrainAttListBylibraryid(BigDecimal libraryid);
    
    
    @Select("SELECT * FROM TBL_ATTACHMENT WHERE ATTID = #{attId} ")
	@Results({
		@Result(column="ATTID",property="attid"),
		@Result(column="ATTNAME",property="attname"),
		@Result(column="ATTPATH",property="attpath"),
		@Result(column="ATTSIZE",property="attsize"),
		@Result(column="MEMO",property="memo"),
		@Result(column="JMURL",property="jmurl"),
		@Result(column="UPLOADTIME",property="uploadtime"),
		@Result(column="UPLOADER",property="uploader"),
		@Result(column="ISPYTHONFLAG",property="ispythonflag"),
	})
	TblAttachment selectEntityByIdalal( String attId) throws Exception;

	//搜索所有的临时文件
	@Select("SELECT * FROM TBL_ATTACHMENT WHERE MEMO = '0';")
	List<com.hbfk.entity.TblAttachment> selectMemoByL();
}
