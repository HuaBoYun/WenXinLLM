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

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.PageInfo;
import com.huabo.contract.entity.TblContractAppendixsigning;
import com.huabo.contract.mappersql.TblContractAppendixsigningMapperSqlConifg;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2021-10-29
 */
public interface TblContractAppendixsigningMapper extends BaseMapper<TblContractAppendixsigning> {

	@Select("SELECT TAC.SINGINGID,TAC.SINGINGNAME,TAC.SINGINGPATH,TAC.SINGINGSIZE,TAC.PDFVIEWFILEPATH,TAC.SINGINGTYPE,TAC.SINGINGSTATUS,TAC.CONSTRACTID,TAC.UPLOADTIME,TAC.UPLOADER,TS.REALNAME,TAC.OAATTID FROM TBL_CONTRACT_APPENDIXSIGNING TAC LEFT JOIN TBL_STAFF TS ON TAC.UPLOADER = TS.STAFFID WHERE CONSTRACTID = #{contractid} order by TAC.SINGINGID ")
	@Results({
		@Result(column="SINGINGID",property="singingId"),
		@Result(column="SINGINGNAME",property="singingName"),
		@Result(column="PDFVIEWFILEPATH",property="pdfViewFilePath"),
		@Result(column="SINGINGPATH",property="singingPath"),
		@Result(column="SINGINGSIZE",property="singingSize"),
		@Result(column="SINGINGTYPE",property="singingType"),
		@Result(column="SINGINGSTATUS",property="singingStatus"),
		@Result(column="CONSTRACTID",property="constractId"),
		@Result(column="UPLOADTIME",property="uploadTime"),
		@Result(column="UPLOADER",property="uploader"),
		@Result(column="REALNAME",property="uploaderName"),
		@Result(column="OAATTID",property="oaattid")
	})
	List<TblContractAppendixsigning> findFileListByContractId(@Param("contractid") BigDecimal contractid) throws Exception;

	@Select("SELECT COUNT(0) FROM TBL_CONTRACT_APPENDIXSIGNING WHERE CONSTRACTID = #{contractId}")
	Integer findFileCountByContractId(String contractId);

	@Select("SELECT * FROM TBL_CONTRACT_APPENDIXSIGNING WHERE SINGINGID = #{singingId}")
    TblContractAppendixsigning findBysingingId(BigDecimal singingId);

	@Select("SELECT * FROM TBL_CONTRACT_APPENDIXSIGNING WHERE SINGINGID = #{singingId}")
    TblContractAppendixsigning findBysingingById(String singingId);
	
	@Delete("DELETE FROM TBL_CONTRACT_APPENDIXSIGNING WHERE SINGINGID = #{singingId}")
	void deleteBySingingId(BigDecimal singingId);
	
	@Insert("INSERT INTO TBL_CONTRACT_APPENDIXSIGNING(SINGINGID,SINGINGNAME,SINGINGPATH,SINGINGSIZE,SINGINGTYPE,SINGINGSTATUS,UPLOADTIME,UPLOADER,CONSTRACTID) VALUES(#{singingId},#{singingName},#{singingPath},#{singingSize},#{singingType},#{singingStatus},#{uploadTime},#{uploader},#{constractId})")
	@Options(useGeneratedKeys=true, keyProperty="singingId", keyColumn="SINGINGID")
	void saveEntity(TblContractAppendixsigning signing);

	@Select("SELECT COUNT(*) FROM TBL_CONTRACT_APPENDIXSIGNING WHERE CONSTRACTID = #{contractid} AND SINGINGTYPE = #{type}")
	Integer selectCountByContractId(BigDecimal contractid, int type) throws Exception;
	
	@Insert("UPDATE TBL_CONTRACT_APPENDIXSIGNING SET CONSTRACTID=#{contractid}  WHERE SINGINGID=#{singingId}")
	void updateEntity(BigDecimal singingId,Integer contractid);

	@SelectProvider(type=TblContractAppendixsigningMapperSqlConifg.class,method="findFileListByContractIdPageInfo")
	IPage<TblContractAppendixsigning> findFileListByContractIdPageInfo(IPage<TblContractAppendixsigning> page,TblContractAppendixsigning tca);

}
