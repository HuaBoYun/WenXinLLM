package com.huabo.compliance.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.compliance.entity.TblBugCriterionEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

public interface TblBugCriterionMapper extends BaseMapper<TblBugCriterionEntity>{
	
	
	@Select("SELECT * FROM  TBL_BUG_CRITERION WHERE BUGID = #{bugid}")
	TblBugCriterionEntity findByTblBugCriterion(String bugid);
	
	@Select("SELECT * FROM  TBL_BUG_CRITERION WHERE ORGID = #{orgid}")
	List<TblBugCriterionEntity> findAll(String orgid);
	
	@Select("SELECT * FROM  TBL_BUGCRITERION WHERE BUGCRIID = #{bugcriid}")
	TblBugCriterionEntity findByid(String bugcriid);
	
	@Select("SELECT * FROM  TBL_BUG_CRITERION WHERE ORGID = #{orgid} AND LEVEL = #{level}")
	List<TblBugCriterionEntity> fingByLevel(String orgid,String level);

	@Select("${sql}")
	<p extends IPage<TblBugCriterionEntity>>  p  getSqlPage(p page, @Param("sql") String sql);

	@Delete("delete from TBL_BUGCRITERION where  BUGCRIID = #{bugcriid}")
	int deleteByCriterionId( @Param("bugcriid") BigDecimal bugcriid);
}
