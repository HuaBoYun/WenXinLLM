package com.huabo.fxgl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.fxgl.entity.TblMajorRiskCreate;
import com.huabo.fxgl.entity.TblRiskImplementEntity;
import com.huabo.fxgl.entity.TblRiskImplementGroupEntity;
import com.huabo.fxgl.entity.TblRiskReportingEntity;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TblMajorRiskCreationMapper extends BaseMapper<TblMajorRiskCreate> {
	
	 List<TblMajorRiskCreate> getMajorRiskCreateList(@Param("nd")String nd,@Param("jd")String jd,@Param("sql")String sql,@Param("staffid")BigDecimal staffid);
	
	 @Select("select i.*  from TBL_RISK_IMPLEMENTGROUP i  where majorid=#{id}")
	 List<TblRiskImplementGroupEntity> selectMajorRiskList(@Param("id")String id);

}
