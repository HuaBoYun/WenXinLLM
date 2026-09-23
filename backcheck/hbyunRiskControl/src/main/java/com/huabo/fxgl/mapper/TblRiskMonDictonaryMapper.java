package com.huabo.fxgl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.fxgl.entity.TblRiskImplementEntity;
import com.huabo.fxgl.entity.TblRiskImplementGroupEntity;
import com.huabo.fxgl.entity.TblRiskMonDictonary;
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
public interface TblRiskMonDictonaryMapper extends BaseMapper<TblRiskMonDictonary> {
	
	@Select("select * from TBL_RISK_MONDICTONARY where orgid=#{orgid} and status=1 order by sort")
	List<TblRiskMonDictonary>  getList(@Param("orgid")BigDecimal orgid);
	
	List<TblRiskMonDictonary> getDicByDept(@Param("deptid")BigDecimal deptid,@Param("orgid")BigDecimal orgid,@Param("versionId")BigDecimal versionId);
	
	List<TblRiskMonDictonary> getMonDicByDept(@Param("orgid")BigDecimal orgid);

	 List<TblRiskMonDictonary> getMonDicCol(@Param("orgid")BigDecimal orgid,@Param("versionId")BigDecimal versionId);

	 List<TblRiskMonDictonary> getMonDicColZd(@Param("orgid")BigDecimal orgid,@Param("versionId")BigDecimal versionId);

}
