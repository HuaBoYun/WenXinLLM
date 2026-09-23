package com.huabo.legal.oracle.mapper;


import com.hbfk.util.PageInfo;
import com.huabo.legal.oracle.entity.TblFwglNoticeOracle;
import org.apache.ibatis.annotations.*;
import tk.mybatis.mapper.common.Mapper;

import java.math.BigDecimal;
import java.util.List;


public interface TblFwglNoticeOracleMapper extends Mapper<TblFwglNoticeOracle> {

	@InsertProvider(method = "saveEnity", type = TblFwglNoticeOracleMapperSqlConfig.class)
	@Options(useGeneratedKeys = true, keyProperty = "noticeid", keyColumn = "NOTICEID")
	void saveEnity(TblFwglNoticeOracle tct) throws Exception;


	@UpdateProvider(method = "updateEnity", type = TblFwglNoticeOracleMapperSqlConfig.class)
	void updateEnity(TblFwglNoticeOracle tcu);


	@SelectProvider(type = TblFwglNoticeOracleMapperSqlConfig.class, method = "findbyorgidall")
	List<TblFwglNoticeOracle> findbyorgidall(PageInfo<TblFwglNoticeOracle> pageInfo, BigDecimal pid, String code, String name);

	@SelectProvider(type = TblFwglNoticeOracleMapperSqlConfig.class, method = "findbyorgidallCpount")
	Integer findbyorgidallCpount(BigDecimal pid, String code, String name);


	@Select("SELECT * FROM TBL_FWGL_NOTICE WHERE NOTICEID = #{noticeid}")
	TblFwglNoticeOracle selectAllInfoById(Long noticeid) throws Exception;


	@Delete("DELETE FROM TBL_FWGL_NOTICE WHERE NOTICEID = #{noticeid}")
	void deletePlannode(Long noticeid);

	@Select("SELECT count(*) from TBL_FWGL_NOTICE where to_char(CREATETIME,'yyyy')=#{year}  ORDER BY NOTICEID DESC")
	Integer findbycodecount(String year);


	@Select("SELECT NOTICECODE from TBL_FWGL_NOTICE where to_char(CREATETIME,'yyyy')=#{year} and ROWNUM=1 ORDER BY NOTICEID DESC")
	String findbycode(String year);

}
