package com.huabo.contract.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.util.PageInfo;
import com.huabo.contract.entity.TblContractExamFile;
import com.huabo.contract.mappersql.TblContractExamFileMapperSqlConifg;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author huabo
 * @since 2021-10-29
 */
public interface TblContractExamFileMapper extends BaseMapper<TblContractExamFile> {

	@Select("SELECT COUNT(0) FROM TBL_CONTRACT_EXAMFILE WHERE CONSTRACTID = #{contractId}")
	Integer findFileCountByContractId(String contractId);

	@SelectProvider(type=TblContractExamFileMapperSqlConifg.class,method="findFileListByContractIdPageInfo")
	IPage<TblContractExamFile> findFileListByContractIdPageInfo(IPage<TblContractExamFile> page,TblContractExamFile tca);

	@Select("SELECT * FROM TBL_CONTRACT_EXAMFILE WHERE ID = #{id}")
    TblContractExamFile findBycontentId(BigDecimal contentId);
	
	@Select("SELECT * FROM TBL_CONTRACT_EXAMFILE WHERE CONSTRACTID = #{contractId}")
	List<TblContractExamFile> findBycontractId(BigDecimal contractId);

	@Delete("DELETE FROM TBL_CONTRACT_EXAMFILE WHERE ID= #{id}")
	void deleteById(Integer id);
	
	@Insert("INSERT INTO TBL_CONTRACT_EXAMFILE(ID,FILENAME,FILEPATH,FILESIZE,FILETYPE,FILESTATUS,UPLOADTIME,UPLOADER,UPLOADERNAME,CONSTRACTID) "
			+ "VALUES(#{id},#{fileName},#{filePath},#{fileSize},#{fileType},#{fileStatus},#{uploadTime},#{uploader},#{uploaderName},#{constractId})")
	@Options(useGeneratedKeys=true, keyProperty="id", keyColumn="ID")
	void saveEntity(TblContractExamFile file);

//	@Select("SELECT COUNT(*) FROM TBL_CONTRACT_EXAMFILE WHERE CONSTRACTID = #{contractid} AND CONTENTTYPE = #{type}")
//	Integer selectCountByContractId(BigDecimal contractid, int type) throws Exception;
//	
	@Insert("UPDATE TBL_CONTRACT_EXAMFILE SET CONSTRACTID=#{contractid}  WHERE ID=#{ID}")
	void updateEntity(BigDecimal ID,Integer contractid);

	
}
