package com.huabo.fxgl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.fxgl.entity.TblRiskImprovementDetailsEntiry;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface TblRiskImprovementDetailsMapper  extends BaseMapper<TblRiskImprovementDetailsEntiry> {

	List<TblRiskImprovementDetailsEntiry>  selectRiskDetail(@Param("queryParam")TblRiskImprovementDetailsEntiry queryParam);
	
	List<TblRiskImprovementDetailsEntiry>  exportRiskReport(@Param("ids")String[] ids,BigDecimal id,@Param("orgname") String orgname);
	
	TblRiskImprovementDetailsEntiry getScore(@Param("id")BigDecimal id,@Param("orgname")String orgname);
	
	List<TblRiskImprovementDetailsEntiry> getScoreHeadOffice(@Param("id")BigDecimal id,@Param("orgname")String orgname);
	
	
	@Select("select * from TBL_RISK_IMPROVEMENT_DETAILS where branch_id=#{id} and score_details=-1  order by id desc") 
	List<TblRiskImprovementDetailsEntiry> getDetailsEntiry(@Param("id")BigDecimal id);
	
    List<TblRiskImprovementDetailsEntiry> selectList(@Param("id") BigDecimal id,@Param("orgname") String orgname);
    
    
    @Select("select ls.*,nt.risknumber as risknumber  from TBL_RISK_IMPROVEMENT_DETAILS "
    		+ "ls left join tbl_risk   nt on ls.riskid = nt.riskid where ls.id=#{id} order by years desc,month desc")
   TblRiskImprovementDetailsEntiry  selectTblRiskImprovementDetails(@Param("id") BigDecimal id);
    
    @Select("select ls.* from TBL_RISK_IMPROVEMENT_DETAILS ls\n" +
            " left join TBL_RISK_MONTHLY_EVALUATION  nt on ls.IMPROVEMENT_ID = nt.id where ls.IMPROVEMENT_ID = #{riskImplementID}")
    List<TblRiskImprovementDetailsEntiry> selectListImprovement(@Param("riskImplementID") BigDecimal riskImplementID);

    @Select("select * from TBL_RISK_IMPROVEMENT_DETAILS  where id=#{id}")
    TblRiskImprovementDetailsEntiry getOne(@Param("id") BigDecimal id);
     
}
