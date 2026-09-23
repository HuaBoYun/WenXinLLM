package com.huabo.legal.oracle.mapper;


import com.hbfk.util.PageInfo;
import com.huabo.legal.oracle.entity.TblFwglLearrningOracle;
import org.apache.ibatis.annotations.*;
import tk.mybatis.mapper.common.Mapper;

import java.math.BigDecimal;
import java.util.List;


public interface TblFwglLearrningOracleMapper extends Mapper<TblFwglLearrningOracle> {

	@InsertProvider(method = "saveEnity", type = TblFwglLearrningOracleMapperSqlConfig.class)
	@Options(useGeneratedKeys = true, keyProperty = "lingid", keyColumn = "LINGID")
	void saveEnity(TblFwglLearrningOracle tct) throws Exception;


	@UpdateProvider(method = "updateEnity", type = TblFwglLearrningOracleMapperSqlConfig.class)
	void updateEnity(TblFwglLearrningOracle tcu);


	@SelectProvider(type = TblFwglLearrningOracleMapperSqlConfig.class, method = "findbyorgidall")
	List<TblFwglLearrningOracle> findbyorgidall(PageInfo<TblFwglLearrningOracle> pageInfo, BigDecimal pid, String code, String name);

	@SelectProvider(type = TblFwglLearrningOracleMapperSqlConfig.class, method = "findbyorgidallCpount")
	Integer findbyorgidallCpount(BigDecimal pid, String code, String name);


	@Select("SELECT * FROM TBL_FWGL_LEARRNING WHERE LINGID = #{lingid}")
	TblFwglLearrningOracle selectAllInfoById(Long lingid) throws Exception;


	@Delete("DELETE FROM TBL_FWGL_LEARRNING WHERE LINGID = #{lingid}")
	void deletePlannode(Long lingid);

	@Select("SELECT count(*) from TBL_FWGL_LEARRNING where to_char(CREATETIME,'yyyy')=#{year}  ORDER BY LINGID DESC")
	Integer findbycodecount(String year);


	//@Select("SELECT * from TBL_FWGL_LEARRNING where to_char(CREATETIME,'yyyy')=#{year} and ROWNUM<2 ORDER BY LINGID DESC")
	@Select("SELECT LINGCODE from TBL_FWGL_LEARRNING where to_char(CREATETIME,'yyyy')=#{year} and ROWNUM=1 ORDER BY LINGID DESC")
	String findbycode(String year);
}
