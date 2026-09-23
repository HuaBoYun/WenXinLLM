package com.huabo.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.system.entity.TblSystemProjectOracle;
import com.huabo.system.mappersql.TblSystemProjectOracleMapperSqlConfig;
import com.huabo.system.vo.param.TblSystemProjectQueryParam;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;


import java.math.BigDecimal;
import java.util.List;

public interface TblSystemProjectOracleMapper extends BaseMapper<TblSystemProjectOracle> {

	/**
	 * 列表获取
	 * @param param
	 * @return
	 */
	@SelectProvider(method = "getList" , type = TblSystemProjectOracleMapperSqlConfig.class)
	List<TblSystemProjectOracle> getList(TblSystemProjectQueryParam project);

	@Select("SELECT TSP.PROJECTNAME,TSP.UNIQUEIDENTIFICATION FROM TBL_SYSTEM_PROJECT TSP LEFT JOIN TBL_SYSTEM_PROJECT_AUTH TSPA ON TSP.ID = TSPA.PROJECTID " + 
			"WHERE TSPA.BELONGGROUP = #{orgid} AND TSP.OTHERPROJECTROUTE IS NULL GROUP BY TSP.PROJECTNAME,TSP.UNIQUEIDENTIFICATION,SORT ORDER BY SORT ASC")
	List<TblSystemProjectOracle> selectModuleListByTheme(BigDecimal orgid) throws Exception;

	@Select("SELECT PROJECTNAME,UNIQUEIDENTIFICATION FROM TBL_SYSTEM_PROJECT WHERE UNIQUEIDENTIFICATION IN (SELECT MODULETYPE FROM TBL_SYSTEM_BIMODULE WHERE ORGID = #{orgid} AND PAGEID IN (${pageIdStrs}))")
	List<TblSystemProjectOracle> selectCancelListByTheme(@Param("orgid")BigDecimal orgid,@Param("pageIdStrs")String pageIdStrs) throws Exception;
}