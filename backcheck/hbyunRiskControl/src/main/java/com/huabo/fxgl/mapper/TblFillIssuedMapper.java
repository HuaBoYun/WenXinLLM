package com.huabo.fxgl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.fxgl.entity.TblFillIssued;
import com.huabo.fxgl.entity.TblRiskImplementEntity;
import com.huabo.fxgl.entity.TblRiskImplementGroupEntity;
import com.huabo.fxgl.entity.TblRiskMonitoringFill;
import com.huabo.fxgl.entity.TblRiskReportingEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface TblFillIssuedMapper extends BaseMapper<TblFillIssued> {
	
     @Select("select * from Tbl_Fill_Issued where creatid=#{id}")
	List<TblFillIssued> selectByCreatid(@Param("id")String id);
     
     @Select("select * from Tbl_Fill_Issued where fillid=#{id}")
 	 TblFillIssued  selectByFillId(@Param("id")BigDecimal id);
}
