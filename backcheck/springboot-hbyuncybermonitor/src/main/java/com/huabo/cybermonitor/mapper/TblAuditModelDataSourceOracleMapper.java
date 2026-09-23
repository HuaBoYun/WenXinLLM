package com.huabo.cybermonitor.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.hbfk.util.PageInfo;
import com.huabo.cybermonitor.entity.StepResult;
import com.huabo.cybermonitor.entity.TblAuditModelDataSourceOracle;
import com.huabo.cybermonitor.vo.TblAuditModelDataSourceQueryParam;

import tk.mybatis.mapper.common.Mapper;

public interface TblAuditModelDataSourceOracleMapper extends Mapper<TblAuditModelDataSourceOracle> {

	

	@SelectProvider(method="selectByPageInfo",type=TblAuditModelDataSourceOracleMapperSqlConfig.class)
	List<TblAuditModelDataSourceOracle> selectByPageInfo(PageInfo<TblAuditModelDataSourceOracle> pageInfo, TblAuditModelDataSourceQueryParam param) throws Exception;
	
	
	 @SelectProvider(method="selectByPageInfocount",type=TblAuditModelDataSourceOracleMapperSqlConfig.class)
	 Integer selectByPageInfocount(TblAuditModelDataSourceQueryParam param) throws Exception;
	 
	 
	 @InsertProvider(method="insertEntity",type=TblAuditModelDataSourceOracleMapperSqlConfig.class)
	 @Options(useGeneratedKeys=true, keyProperty="resultid", keyColumn="RESULTID")
	 void insertEntity(StepResult result) throws Exception;
	 
	 
	 @Select("SELECT *  from TBL_NBSJ_SJMXRESULT    where STEPID=#{stepid} ")
	 List<StepResult> sjmxReulst(BigDecimal stepid);
	 
	 
	@Select("SELECT * from TBL_NBSJ_SJMXRESULT where RESULTID=#{resultid} ")
	StepResult onesjmxReulst(BigDecimal resultid);
	
	 @InsertProvider(method="insertEntityparem",type=TblAuditModelDataSourceOracleMapperSqlConfig.class)
	 @Options(useGeneratedKeys=true, keyProperty="id", keyColumn="ID")
	 void insertEntityparem(TblAuditModelDataSourceOracle param);
	
	

}