package com.huabo.fxgl.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hbfk.entity.TblStaffUtil;
import com.huabo.fxgl.entity.Find;
import com.huabo.fxgl.entity.RiskAssplan;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xujiajun
 * @since 2022-08-12
 */
@Repository
public interface RiskAssplanMapper extends BaseMapper<RiskAssplan> {
	
    List<Map<String, Object>> getRiskPointTask(@Param("company")String company);
    
    List<Map<String, Object>> getRiskPointTaskNew(@Param("company")String company);

    List<Map<String, Object>> riskCatnameRisks(@Param("company")String company);

    List<RiskAssplan> riplanTrackList(@Param("ew") Wrapper queryWrapper,@Param("sql")String sql);

    List<RiskAssplan> selectAllList(@Param("ew") Wrapper queryWrapper,@Param("sql")String sql);

    @Select( "SELECT DISTINCT 	r.assplanid,r.plancode,r.planName,r.startDate,r.endDate,r.planStatus" 
    		+ ",r.aprstatus,r.assstdid "
    		+ " FROM TBL_RISK_ASSPLAN r  INNER  JOIN " +
            "( SELECT DISTINCT rb.ASSPLANID,rr.ASSSATUS from TBL_RISK_ASSPLAN_RISK  rb  inner join " +
            " TBL_RISK_RISKMarking rr ON RB.assriskid=RR.ASSRISKID WHERE RR.STAFFID=#{param1})"+
            "  rbs ON rbs.ASSPLANID=r.ASSPLANID WHERE	r.planStatus != 1 ${sql} and ${ew.sqlSegment}")
    @ResultMap("RM_RISK_ASSPLAN_NO_DETAIL")
    List<RiskAssplan> findRiskAssplanRiskfxgl( @Param("staffid")BigDecimal staffid,@Param("sql") String sql,  @Param("ew")QueryWrapper queryWrapper);

    
    @Select( "SELECT DISTINCT 	r.assplanid,r.plancode,r.planName,r.startDate,r.endDate,r.planStatus"
    		+ ",r.aprstatus "
    		+ " FROM TBL_RISK_ASSPLAN r  INNER  JOIN " +
            "( SELECT DISTINCT rb.ASSPLANID,rr.ASSSATUS from TBL_RISK_ASSPLAN_RISK  rb  inner join " +
            " TBL_RISK_RISKMarking rr ON RB.assriskid=RR.ASSRISKID WHERE RR.STAFFID=#{param1})"+
            "  rbs ON rbs.ASSPLANID=r.ASSPLANID WHERE	r.planStatus != 1 and ${ew.sqlSegment}")
    @ResultMap("RM_RISK_ASSPLAN_NO_DETAIL")
    List<RiskAssplan>  findRiskAssplanRiskfxgl(BigDecimal staffid, @Param("ew") QueryWrapper queryWrapper);

 
    Integer findBysqlObj(@Param("plancode") String plancode,@Param("orgid") String orgid, @Param("idsStr") String idsStr);


    @Select("SELECT COUNT(*) from TBL_RISK_ASSPLAN  where plancode = #{plancode}" +
            " AND UNIT IN (SELECT ORGID FROM TBL_ORGANIZATION WHERE  1=1 START WITH FATHERORGID= ${orgid}  " +
            " AND ORGTYPE=0  CONNECT BY PRIOR ORGID = FATHERORGID UNION ALL SELECT ${orgid}  FROM DUAL)")
    Integer findBysqlObj(@Param("plancode") String plancode, @Param("orgid") String orgid);

    @Select("SELECT * from TBL_RISK_ASSPLAN  where ASSSTDID = #{assstdid}")
     List<RiskAssplan> getRiskAssplanByAssessMentId(@Param("assstdid")BigDecimal assstdid);
    
    
    @Select("SELECT MAX(TO_NUMBER(SUBSTR(plancode,INSTR(plancode,'-',-1)+1)))  "
			+ " FROM TBL_RISK_ASSPLAN "
			+ " WHERE plancode LIKE ${plancode} ")
	Integer get_riskpgplan_no(String plancode) throws Exception;
    
    @Select("SELECT MAX(TO_NUMBER(SUBSTR(assnumber,INSTR(assnumber,'-',-1)+1)))  "
			+ " FROM TBL_RISK_ASSESSMENTSTD "
			+ " WHERE assnumber LIKE ${assnumber} ")
	Integer get_riskpgbz_no(String assnumber) throws Exception;
    
    
    @Select("SELECT MAX(TO_NUMBER(SUBSTR(assnumber,INSTR(assnumber,'-',-1)+1)))  "
 			+ " FROM TBL_RISK_ASSESSMENTSTD "
 			+ " WHERE assnumber LIKE ${assnumber}  and companyid=#{company} ")
 	Integer get_NewCode(String assnumber,BigDecimal company) throws Exception;
    
    @Select("select count(1) from TBL_RISK_ASSPLAN where groupID=#{id} and CREATESTAFFID=#{staffid}")
    Integer getCountById(BigDecimal id,String staffid);

    @SelectProvider(type = RiskAssplanMapperSqlConfig.class , method = "selectListByPageInfo")
    List<RiskAssplan> selectListByPageInfo(TblStaffUtil staffUtil, Find find,Integer authorityType);

}
