package com.huabo.finance.mapper;

import com.huabo.finance.entity.BdPlanSqlconfig;
import com.huabo.finance.mappersql.BdPlanSqlconfigMapperSqlConfig;
import com.huabo.finance.vo.BdPlanSqlconfigVo;
import com.huabo.finance.vr.BdPlanSqlconfigVr;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;

/**
 * <p>
 * 财务采方案配置sql语句 Mapper 接口
 * </p>
 *
 * @author L
 * @since 2025-03-13
 */
public interface BdPlanSqlconfigMapper extends BaseMapper<BdPlanSqlconfig> {
	
	@SelectProvider(type = BdPlanSqlconfigMapperSqlConfig.class , method = "selectPageInfo")
	IPage<BdPlanSqlconfigVr> selectPageInfo(IPage<BdPlanSqlconfigVr> page, BdPlanSqlconfigVo vo) throws Exception;

	@Select("SELECT BIS.FID AS SQLCONFIGID,BIS.INCREMENTCOL AS FINITINCREMENTCOL ,BIS.FNAME AS SQLCONFIGNAME,BIS.FVERSIONID AS CONFIGVERSIONID,BIS.FTABLE AS CONFIGTABLENAME,BIS.PRIMARYCOL AS INITPRIMARYCOL,BIS.FINSPECIFICITYCOL AS CONFIGSPECIFICITYCOL,#{fid} AS FINITPLANID,BIS.FINITSQL AS INITSQL,BPS.* FROM BD_INIT_SQLCONFIG BIS LEFT JOIN BD_PLAN_SQLCONFIG BPS ON BPS.FPLANID = #{fid} AND BIS.FID = BPS.FINITSQLID  WHERE BIS.FVERSIONID = #{fversionid}  ORDER BY BIS.FID ASC ")
	List<BdPlanSqlconfigVr> selectAllListByPlan(@Param("fid") String fid,@Param("fversionid") String fversionid) throws Exception;

	@Delete("DELETE FROM BD_PLAN_SQLCONFIG WHERE FPLANID = #{planid}")
	void deleteByPlanId(@Param("planid") String planid) throws Exception;
}
