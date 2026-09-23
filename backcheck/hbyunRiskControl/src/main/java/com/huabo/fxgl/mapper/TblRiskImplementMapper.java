package com.huabo.fxgl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.fxgl.entity.TblRiskImplementEntity;
import com.huabo.fxgl.entity.TblRiskImplementGroupEntity;
import com.huabo.fxgl.entity.TblRiskReportingEntity;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface TblRiskImplementMapper extends BaseMapper<TblRiskImplementEntity> {
	
	 List<TblRiskImplementEntity> selectAllList(@Param("queryParam")TblRiskImplementEntity queryParam,@Param("ids")String[] ids );
	
	 
	 List<TblRiskImplementEntity> selectAllSummaryList(@Param("queryParam")TblRiskImplementEntity queryParam,@Param("ids")String[] ids,@Param("sql")String sql);

	 List<TblRiskImplementEntity> selectAllSummaryListOrder(@Param("queryParam")TblRiskImplementEntity queryParam,@Param("ids")String[] ids,@Param("sql")String sql);

	 List<TblRiskImplementEntity> selectAllSummaryListOrderByName(@Param("impRiskName")String impRiskName);
	 
	@Select("select count(1) from TBL_RISK_IMPLEMENT where majorid=#{majorid} and majorRiskId=#{majorRiskId}")
	Integer getCountByMajorId(@Param("majorid")String majorid,@Param("majorRiskId")String majorRiskId);


@Select("select * from TBL_RISK_IMPLEMENT where  MAJORID=#{id}    and (TOREPORT IS NULL OR TOREPORT='') ")
List<TblRiskImplementEntity> getMajorIssList(@Param("id")BigDecimal  id);



@Select("select i.* from TBL_RISK_IMPLEMENT i  where majorid=#{id}")
 List<TblRiskImplementEntity> selectMajorRiskList(@Param("id")String id);

@Update("UPDATE TBL_RISK_IMPLEMENT SET TRANSFERSTAFFID=#{staffid} WHERE majorid=#{id}")
 void updateRiskImplementEntity(@Param("id")BigDecimal id,@Param("staffid")BigDecimal staffid);
}
