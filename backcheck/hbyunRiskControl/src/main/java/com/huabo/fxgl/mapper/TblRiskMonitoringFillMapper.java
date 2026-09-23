package com.huabo.fxgl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
public interface TblRiskMonitoringFillMapper extends BaseMapper<TblRiskMonitoringFill> {
	
	@Select("select m.*,s.realname as createname,o.ORGNAME as linkOrgName,org.orgname as linkDeptName,i.deptnotes from TBL_RISK_MONITORINGFILL m  left join tbl_staff s on s.staffid=m.CREATESTAFFID left join tbl_organization o on o.orgid=m.LINKORGID left join tbl_organization org on org.orgid=m.LINKDEPTID left join TBL_FILL_ISSUED i on i.fillid=m.id where m.id=#{id}")
	TblRiskMonitoringFill getEntityById(@Param("id") String id);

	List<TblRiskMonitoringFill> getList(@Param("queryParam") TblRiskMonitoringFill queryParam,
			@Param("sql") String sql);

	@Select("select count(1) from TBL_RISK_MONITORINGFILL where monitorid=#{id} and createstaffid=#{staffid}")
	int checkDouble(@Param("id") BigDecimal id, @Param("staffid") String staffid);

	@Select("select count(1) from TBL_RISK_MONITORINGFILL where monitorid=#{id}  ")
	int checkDouble2(@Param("id") BigDecimal id);

	@Select("select * from TBL_RISK_MONITORINGFILL where riskyear=#{year} and linkorgid=#{orgid} and quartername=#{jd} and  order by createtime desc ")
	List<Map<String, Object>> selectByYear(@Param("year") Integer year, @Param("orgid") String orgid,
			@Param("jd") String jd);
	
	
	List<TblRiskMonitoringFill> getListByYear(@Param("year") Integer year, @Param("jd") String jd,@Param("orgid")String orgid);
	
	List<TblRiskMonitoringFill> getDesList(@Param("cols") String cols,@Param("year") Integer year, @Param("jd") String jd,@Param("orgid")String orgid);

}
