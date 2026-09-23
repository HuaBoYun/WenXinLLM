package com.huabo.fxgl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.fxgl.entity.TblRiskImplementEntity;
import com.huabo.fxgl.entity.TblRiskImplementGroupEntity;
import com.huabo.fxgl.entity.TblRiskReportingEntity;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TblRiskImplementGroupMapper extends BaseMapper<TblRiskImplementGroupEntity> {
	
	
	TblRiskImplementGroupEntity getOneById(@Param("id")String id);
	
	
	@Select(" select * from TBL_RISK_IMPLEMENTGROUP where majorid=#{majorid} and (imp_lssued_staffid is null or  imp_lssued_staffid ='')")
	List<TblRiskImplementGroupEntity> selectAllIssStaff(@Param("majorid")String majorid);
	
	@Select(" select * from TBL_RISK_IMPLEMENTGROUP where majorid=#{majorid} ")
	List<TblRiskImplementGroupEntity> selectAllEntityByMajrId(@Param("majorid")String majorid);
	
}
