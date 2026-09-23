package com.huabo.fxgl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.fxgl.entity.Risk;
import com.huabo.fxgl.entity.TblRiskImplementEntity;
import com.huabo.fxgl.entity.TblRiskImprovementDetailsEntiry;
import com.huabo.fxgl.entity.TblRiskMonthlyEvaluationEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface TblRiskMonthlyEvaluationMapper extends BaseMapper<TblRiskMonthlyEvaluationEntity> {

	List<Risk> queryAssessedRiskList(@Param("queryParam")Risk queryParam,String[] ids,@Param("company")BigDecimal company,@Param("sql")String sql);
	 
	 List<TblRiskMonthlyEvaluationEntity> selectAllList(@Param("queryParam")TblRiskMonthlyEvaluationEntity queryParam,@Param("sql")String sql);

	List<TblRiskMonthlyEvaluationEntity> exportMonthlyEvaluationSummary(@Param("ids")String[] ids,@Param("queryParam")TblRiskMonthlyEvaluationEntity queryParam,@Param("sql")String sql);
	
	List<TblRiskMonthlyEvaluationEntity> exportMonthlyEvaluation(@Param("ids")String[] ids,@Param("riskid")String riskid,@Param("sql")String sql);

	 List<TblRiskMonthlyEvaluationEntity> queryMonthlyEvaluationList(@Param("queryParam")TblRiskMonthlyEvaluationEntity queryParam,@Param("sql")String sql);

	 @Select(" select * from  TBL_RISK_IMPROVEMENT_DETAILS where branch_id=#{company} and years=#{year} and month=#{month} and riskid=#{riskid}")
	 TblRiskImprovementDetailsEntiry selectRiskReport(@Param("company")String company,@Param("month")int month,@Param("riskid")BigDecimal riskid,@Param("year")int year);
	
	
    @Select("INSERT INTO TBL_RISK_MONTHLY_EVALUATION_ATT(EVALUATIONID,ATTID) VALUES (#{id},#{aid})")
    void insertAttInfoReporting(@Param("id") BigDecimal id, @Param("aid")String aid);

    @Select(" SELECT ATTID FROM TBL_RISK_MONTHLY_EVALUATION_ATT WHERE EVALUATIONID = #{id}")
    List<BigDecimal> findAttIdListByMonthlyEvaluation(@Param("id")String id);

    @Delete("DELETE FROM TBL_RISK_REPORTING_ATT WHERE ATTID = #{attid}")
    void deleteFileInfoByAttId(@Param("attid")BigDecimal attid);
    
    @Delete("DELETE FROM  TBL_RISK_IMPROVEMENT_DETAILS ")
    void sql1();
    
    @Delete("DELETE FROM TBL_RISK_IMPROVEMENT")
    void sql2();
    
  @Update("${sql}")
  void updatesql(@Param("sql")String sql);
  
  @Select("select max(month) from TBL_RISK_MONTHLY_EVALUATION where riskid=#{riskid} and YEAR(create_time)=#{year}  and status=6 ")
  BigDecimal getMaxMonthy(@Param("riskid")BigDecimal riskid,@Param("year")Integer year);
}
