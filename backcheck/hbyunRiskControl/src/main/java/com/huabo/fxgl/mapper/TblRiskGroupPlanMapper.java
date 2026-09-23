package com.huabo.fxgl.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.fxgl.entity.TblRiskGroupplan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

 
@Repository
public interface TblRiskGroupPlanMapper extends BaseMapper<TblRiskGroupplan> {
	
    //列表分页查询
    List<TblRiskGroupplan> findAll(@Param("sql")String sql,@Param("ew") QueryWrapper queryWrapper);
    
    /*@Select( "SELECT * from TBL_RISK_GROUPPLAN WHERE ID=#{ID}")
    @ResultMap("RM_RISK_GROUPPLAN")*/
   TblRiskGroupplan getOneDetail(@Param("ID")String ID);

    
    
     Integer findBysqlObj(@Param("plancode") String plancode,@Param("orgid") String orgid, @Param("idsStr") String idsStr);
 
    @Select("SELECT MAX(TO_NUMBER(SUBSTR(plancode,INSTR(plancode,'-',-1)+1)))  "
			+ " FROM TBL_RISK_GROUPPLAN "
			+ " WHERE plancode LIKE ${plancode} ")
	Integer get_plan_no(String plancode) throws Exception;
 
   
}
