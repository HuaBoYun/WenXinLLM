package com.huabo.fxgl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.fxgl.entity.TblMonModelVersion;
import com.huabo.fxgl.entity.TblRiskImplementEntity;
import com.huabo.fxgl.entity.TblRiskImplementGroupEntity;
import com.huabo.fxgl.entity.TblRiskMonDictonary;
import com.huabo.fxgl.entity.TblRiskMonModel;
import com.huabo.fxgl.entity.TblRiskMonitoringFill;
import com.huabo.fxgl.entity.TblRiskReportingEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface TblMonModelVersionMapper extends BaseMapper<TblMonModelVersion> {
	
	@Select(" select max(version) from tbl_monmodel_version where orgid=#{orgid}")
	Integer getMaxVersion(@Param("orgid")BigDecimal orgid);
	
	
}
