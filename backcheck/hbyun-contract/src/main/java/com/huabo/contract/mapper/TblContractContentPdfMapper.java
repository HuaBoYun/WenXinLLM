package com.huabo.contract.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.contract.entity.TblContractContentPdf;
import com.huabo.contract.mappersql.TblContractContentPdfMapperSqlConifg;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2021-10-29
 */
public interface TblContractContentPdfMapper extends BaseMapper<TblContractContentPdf> {

	@Select("SELECT TAC.CONTENTPDFID,TAC.CONTENTPDFNAME,TAC.CONTENTPDFPATH,TAC.CONTENTPDFSIZE,TAC.PDFVIEWFILEPATH,TAC.CONTENTPDFTYPE,TAC.CONTENTPDFSTATUS,TAC.CONSTRACTID,TAC.UPLOADTIME,TAC.UPLOADER,TS.REALNAME "
			+ "FROM TBL_CONTRACT_CONTENTPDF TAC LEFT JOIN TBL_STAFF TS ON TAC.UPLOADER = TS.STAFFID WHERE CONSTRACTID = #{contractid}")
	@Results({
		@Result(column="CONTENTPDFID",property="contentPdfId"),
		@Result(column="CONTENTPDFNAME",property="contentPdfName"),
		@Result(column="PDFVIEWFILEPATH",property="pdfViewFilePath"),
		@Result(column="CONTENTPDFPATH",property="contentPdfPath"),
		@Result(column="CONTENTPDFSIZE",property="contentPdfSize"),
		@Result(column="CONTENTPDFTYPE",property="contentPdfType"),
		@Result(column="CONTENTPDFSTATUS",property="contentPdfStatus"),
		@Result(column="CONSTRACTID",property="constractId"),
		@Result(column="UPLOADTIME",property="uploadTime"),
		@Result(column="UPLOADER",property="uploader"),
		@Result(column="REALNAME",property="uploaderName"),
	})
	List<TblContractContentPdf> findFileListByContractId(@Param("contractid") BigDecimal contractid) throws Exception;

	@Select("SELECT COUNT(0) FROM TBL_CONTRACT_CONTENTPDF WHERE CONSTRACTID = #{contractId}")
	Integer findFileCountByContractId(String contractId);

	@SelectProvider(type=TblContractContentPdfMapperSqlConifg.class,method="findFileListByContractIdPageInfo")
	IPage<TblContractContentPdf> findFileListByContractIdPageInfo(IPage<TblContractContentPdf> page,TblContractContentPdf tca);

	@Select("SELECT * FROM TBL_CONTRACT_CONTENTPDF WHERE CONTENTPDFID = #{contentPdfId}")
    TblContractContentPdf findBycontentPdfId(BigDecimal contentPdfId);
	
	@Select("SELECT * FROM TBL_CONTRACT_CONTENTPDF WHERE CONSTRACTID = #{contractId}")
	List<TblContractContentPdf> findBycontractId(BigDecimal contractId);

	@Delete("DELETE FROM TBL_CONTRACT_CONTENTPDF WHERE CONTENTPDFID = #{contentPdfId}")
	void deleteBySingingId(BigDecimal contentPdfId);
	
	@Insert("INSERT INTO TBL_CONTRACT_CONTENTPDF(CONTENTPDFID,CONTENTPDFNAME,CONTENTPDFPATH,CONTENTPDFSIZE,CONTENTPDFTYPE,CONTENTPDFSTATUS,UPLOADTIME,UPLOADER,CONSTRACTID) "
			+ "VALUES(#{contentPdfId},#{contentPdfName},#{contentPdfPath},#{contentPdfSize},#{contentPdfType},#{contentPdfStatus},#{uploadTime},#{uploader},#{constractId})")
	@Options(useGeneratedKeys=true, keyProperty="contentPdfId", keyColumn="CONTENTPDFID")
	void saveEntity(TblContractContentPdf signing);

	@Select("SELECT COUNT(*) FROM TBL_CONTRACT_CONTENTPDF WHERE CONSTRACTID = #{contractid} AND CONTENTPDFTYPE = #{type}")
	Integer selectCountByContractId(BigDecimal contractid, int type) throws Exception;
	
	@Update("UPDATE TBL_CONTRACT_CONTENTPDF SET CONSTRACTID=#{contractid}  WHERE CONTENTPDFID=#{contentPdfId}")
	void updateEntity(BigDecimal contentPdfId,Integer contractid);

}
