package com.huabo.fxgl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
public interface TblRiskMonModelMapper extends BaseMapper<TblRiskMonModel> {
	
	List<TblRiskMonModel> getOrgList(@Param("orgid")BigDecimal orgid);
	
	
	@Select("select m.* from TBL_RISK_MONMODEL  m left join tbl_organization o on o.orgid=m.deptid "
			+ "where m.linkorgid=#{orgid} group by m.deptid,o.orgname")
	List<TblRiskMonModel> getList(@Param("orgid")BigDecimal orgid);
	
	
	@Delete(" delete from TBL_RISK_MONMODEL where linkorgid=#{orgid}")
	void removeByOrgid(@Param("orgid")BigDecimal orgid);
}
