package com.huabo.fxgl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.fxgl.entity.TblMajorRiskCreate;
import com.huabo.fxgl.entity.TblMajorRiskbranchCreate;
import com.huabo.fxgl.entity.TblRiskImplementEntity;
import com.huabo.fxgl.entity.TblRiskImplementGroupEntity;
import com.huabo.fxgl.entity.TblRiskReportingEntity;
import com.huabo.fxgl.entity.TblMajorTransfer;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TblMajorTransferMapper extends BaseMapper<TblMajorTransfer> {
	
	 @Select("select *from TBL_MAJOR_TRANSFER where majorid=#{id}  order by id desc")
	 List<TblMajorTransfer> getMajorTransferList(@Param("id")BigDecimal id);
	 
	 
}
