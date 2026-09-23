package com.huabo.fxgl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.fxgl.entity.TblRiskImprovementEntiry;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TblRiskImprovementMapper  extends BaseMapper<TblRiskImprovementEntiry> {
	
	List<Map<BigDecimal, Object>> getOrgGroupList(@Param("name")String name);
	
	@Select("select * from TBL_RISK_IMPROVEMENT where BRANCH_ID=#{id}")
	List<TblRiskImprovementEntiry> getOrgGroupListByName(@Param("id")String id);
}
