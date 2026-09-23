package com.huabo.finance.mapper;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.finance.entity.TblConfigColumnInfo;
import com.huabo.finance.entity.TblConfigTableInfo;
import com.huabo.finance.mappersql.TblConfigTableInfoMapperSqlConfig;
import com.huabo.finance.vo.BusinessDataVo;
import com.huabo.finance.vo.TblConfigTableInfoVo;
import com.huabo.finance.vr.TblConfigTableInfoVr;

import afu.org.checkerframework.checker.regex.qual.ClassRegexParam;
import io.lettuce.core.dynamic.annotation.Param;

public interface TblConfigTableInfoMapper extends BaseMapper<TblConfigTableInfo> {

	@SelectProvider(type = TblConfigTableInfoMapperSqlConfig.class,method = "selectPageInfo")
	@Results({
		@Result(column = "RECORDID" , property = "brv.recordid",id = true),
		@Result(column = "RECORDNAME" , property = "brv.recordname"),
		@Result(column = "SQLID" , property = "brv.sqlid"),
		@Result(column = "STARTDATE" , property = "brv.startdate"),
		@Result(column = "ENDDATE" , property = "brv.enddate"),
		@Result(column = "RECORDTYPE" , property = "brv.recordtype"),
		@Result(column = "ISCOMPLETED" , property = "brv.iscompleted"),
		@Result(column = "ISRESULT" , property = "brv.isresult")
	})
	IPage<TblConfigTableInfoVr> selectPageInfo(Page<TblConfigTableInfoVr> page, TblConfigTableInfoVo vo) throws Exception;

	@Select("${sql}")
	Integer selectTableCount(@Param("sql")String sql) throws Exception;

	@Update("${sql}")
	void executeSql(@Param("sql")String sql) throws Exception;

	@SelectProvider(type = TblConfigTableInfoMapperSqlConfig.class,method = "selectEntityById")
	TblConfigTableInfoVr selectEntityById(String fid) throws Exception;

	@Insert("${sql}")
	void executeInsertSql(@Param("sql")String sql) throws Exception;

	@Select("SELECT * FROM TBL_CONFIG_TABLEINFO WHERE PLANID = #{planId} AND FSTATUS = 1")
	List<TblConfigTableInfo> selectListByPlanId(@Param("planId") String planId) throws Exception;
	
	@Select("SELECT FID,FNAME,FINANCETYPE FROM TBL_CONFIG_TABLEINFO WHERE PLANID = #{planId}")
	List<TblConfigTableInfo> selectTreeListByPlanId(@Param("planId") String planId) throws Exception;

	@SelectProvider(type = TblConfigTableInfoMapperSqlConfig.class,method = "selectBusinessDatePage")
	IPage<Map<String, Object>> selectBusinessDatePage(Page<Map<String, Object>> page, BusinessDataVo vo, TblConfigTableInfoVr info, List<TblConfigColumnInfo> pageColList, TblConfigColumnInfo primaryCol) throws Exception;

	@SelectProvider(type = TblConfigTableInfoMapperSqlConfig.class,method = "selectBusinessDateEntity")
	Map<String, Object> selectBusinessDateEntity(BusinessDataVo vo, TblConfigTableInfoVr info, List<TblConfigColumnInfo> colList) throws Exception;

	@Update("UPDATE TBL_CONFIG_TABLEINFO SET FSTATUS = #{fstatus} WHERE FID = #{fid}")
	void updateTableStatus(@Param("fid")String fid,@Param("fstatus") Integer fstatus) throws Exception;

	@Select("SELECT * FROM TBL_CONFIG_TABLEINFO WHERE PLANID = #{planId} AND FSTATUS = 1 AND FINANCETYPE != 2 ORDER BY CREATETIME ASC")
	List<TblConfigTableInfo> selectFinanceDataListByPlanId(@Param("planId") String planId) throws Exception;

	@Delete("${sql}")
	void executeDeleteSql(@Param("sql")String sql) throws Exception;


}
