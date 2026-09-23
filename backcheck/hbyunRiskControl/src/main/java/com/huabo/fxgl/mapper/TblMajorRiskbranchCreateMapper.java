package com.huabo.fxgl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.fxgl.dto.TblMajorRiskbranchCreateDto;
import com.huabo.fxgl.entity.TblMajorRiskCreate;
import com.huabo.fxgl.entity.TblMajorRiskbranchCreate;
import com.huabo.fxgl.entity.TblRiskImplementEntity;
import com.huabo.fxgl.entity.TblRiskImplementGroupEntity;
import com.huabo.fxgl.entity.TblRiskReportingEntity;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TblMajorRiskbranchCreateMapper extends BaseMapper<TblMajorRiskbranchCreate> {
	
	 List<TblMajorRiskbranchCreate> getMajorRiskCreateList(@Param("param")TblMajorRiskbranchCreateDto param,@Param("sql")String sql,@Param("staffid")BigDecimal staffid);
	
	 
	 List<TblMajorRiskbranchCreate>  getMajorRiskTrack(@Param("queryParam")TblMajorRiskbranchCreate queryParam,@Param("sql")String sql);
//	 @Select("select i.*,s.realname as impWayStaffName,o.orgname as impWayDeptName,ou.orgname "
//	 		+ "as impDutyUnitName from TBL_RISK_IMPLEMENTGROUP i "
//	 		+ "left join tbl_staff  s on i.IMP_WAY_STAFF=s.staffid "
//	 		+ "left join tbl_organization o on o.orgid=i.IMP_WAY_DEPT "
//	 		+ "left join tbl_organization ou on ou.orgid=i.IMP_DUTY_UNIT where majorid=#{id}")
//	 List<TblRiskImplementGroupEntity> selectMajorRiskList(@Param("id")String id);

	 
	 @Select("select count(1) from TBL_MAJORRISK_BRANCHCREATE where majorid=#{id} and CREATESTAFFID=#{staffid}")
	 Integer getCountByMajorId(@Param("id")String id,@Param("staffid")String staffid);
	 
	 
	 @Select("select *from TBL_MAJORRISK_BRANCHCREATE where majorid=#{id} and CREATESTAFFID=#{staffid}")
	 TblMajorRiskbranchCreate getBranchByMajorId(@Param("id")String id,@Param("staffid")String staffid);
	 
	 
	 
}
