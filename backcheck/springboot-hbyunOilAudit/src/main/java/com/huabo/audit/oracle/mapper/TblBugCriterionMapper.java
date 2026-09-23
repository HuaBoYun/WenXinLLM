package com.huabo.audit.oracle.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.audit.oracle.entity.TblBugCriterionEntity;

public interface TblBugCriterionMapper extends BaseMapper<TblBugCriterionEntity>{
	
	
	@Select("SELECT * FROM  TBL_BUG_CRITERION WHERE BUGID = #{bugid}")
	TblBugCriterionEntity findByTblBugCriterion(String bugid);
	
	@Select("SELECT * FROM  TBL_BUG_CRITERION WHERE ORGID = #{orgid}")
	List<TblBugCriterionEntity> findAll(String orgid);
	
	@Select("SELECT * FROM  TBL_BUG_CRITERION WHERE BUGCRIID = #{bugcriid}")
	TblBugCriterionEntity findByid(String bugcriid);
	
	@Select("SELECT * FROM  TBL_BUG_CRITERION WHERE ORGID = #{orgid} AND LEVEL = #{level}")
	List<TblBugCriterionEntity> fingByLevel(String orgid,String level);
	
	

}
