package com.huabo.fxgl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.fxgl.dto.ZbVo;
import com.huabo.fxgl.entity.TblNbsjAuditStepEntity;
import com.huabo.fxgl.vo.HomeRiskRltVo;
import com.huabo.fxgl.vo.RiskPgVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface TblNbsjAuditStepEntityMapper extends BaseMapper<TblNbsjAuditStepEntity> {

    @Select("SELECT * FROM TBL_NBSJ_AUDITSTEP WHERE STEPID = #{stepId}")
    TblNbsjAuditStepEntity selectByStepId(String stepId);

    @Select("SELECT * FROM (${sql})")
    List<ZbVo> selectBySQL(String sql);

//    @Select("SELECT BOOKID,STEPTITLE,SQLSTR\n" +
//            "FROM TBL_NBSJ_AUDITSTEP\n" +
//            "WHERE MPDELTYPE = 'FXCT'")
    @Select("SELECT BOOKID,STEPTITLE,SQLSTR " +
            "from TBL_NBSJ_AUDITSTEP " +
            "WHERE STEPID in " +
            "(SELECT STEPID from TBL_NBSJ_AUDITSTEP_XFRY where STAFFID= #{staffid}) and QYSTATUS=0 " +
            "AND MPDELTYPE = 'FXCT'")
    List<TblNbsjAuditStepEntity> selectAllList(@Param("staffid") BigDecimal staffid);

    @Select("SELECT " +
            "    o.ORGNAME AS orgname," +
            "    r.UNIT AS unit," +
            "    SUM(CASE WHEN a.FREQUENCY = 1 THEN 1 ELSE 0 END) AS frequency1," +
            "    SUM(CASE WHEN a.FREQUENCY = 2 THEN 1 ELSE 0 END) AS frequency2," +
            "    SUM(CASE WHEN a.FREQUENCY = 3 THEN 1 ELSE 0 END) AS frequency3," +
            "    SUM(CASE WHEN a.FREQUENCY = 4 THEN 1 ELSE 0 END) AS frequency4," +
            "    SUM(CASE WHEN a.FREQUENCY = 5 THEN 1 ELSE 0 END) AS frequency5," +
            "    SUM(CASE WHEN a.SEVERITY = 1 THEN 1 ELSE 0 END) AS severity1," +
            "    SUM(CASE WHEN a.SEVERITY = 2 THEN 1 ELSE 0 END) AS severity2," +
            "    SUM(CASE WHEN a.SEVERITY = 3 THEN 1 ELSE 0 END) AS severity3," +
            "    SUM(CASE WHEN a.SEVERITY = 4 THEN 1 ELSE 0 END) AS severity4," +
            "    SUM(CASE WHEN a.SEVERITY = 5 THEN 1 ELSE 0 END) AS severity5" +
            " FROM TBL_RISK_ASSPLAN_RISK a" +
            "    LEFT JOIN TBL_RISK r ON a.RISKID = r.RISKID" +
            "    LEFT JOIN TBL_ORGANIZATION o ON o.ORGID = r.UNIT " +
            " WHERE " +
            "    a.FREQUENCY IS NOT NULL " +
            "    AND a.SEVERITY IS NOT NULL " +
            "    AND r.UNIT = #{commpanyId} " +
            " GROUP BY " +
            "    o.ORGNAME,r.UNIT " +
            " ORDER BY " +
            "    o.ORGNAME")
    HomeRiskRltVo selectHomeRiskRltVoByUnit(@Param("commpanyId") BigDecimal commpanyId);

    @Select("SELECT " +
            "    SUM(CASE WHEN a.FREQUENCY = 1 THEN 1 ELSE 0 END) AS frequency1," +
            "    SUM(CASE WHEN a.FREQUENCY = 2 THEN 1 ELSE 0 END) AS frequency2," +
            "    SUM(CASE WHEN a.FREQUENCY = 3 THEN 1 ELSE 0 END) AS frequency3," +
            "    SUM(CASE WHEN a.FREQUENCY = 4 THEN 1 ELSE 0 END) AS frequency4," +
            "    SUM(CASE WHEN a.FREQUENCY = 5 THEN 1 ELSE 0 END) AS frequency5," +
            "    SUM(CASE WHEN a.SEVERITY = 1 THEN 1 ELSE 0 END) AS severity1," +
            "    SUM(CASE WHEN a.SEVERITY = 2 THEN 1 ELSE 0 END) AS severity2," +
            "    SUM(CASE WHEN a.SEVERITY = 3 THEN 1 ELSE 0 END) AS severity3," +
            "    SUM(CASE WHEN a.SEVERITY = 4 THEN 1 ELSE 0 END) AS severity4," +
            "    SUM(CASE WHEN a.SEVERITY = 5 THEN 1 ELSE 0 END) AS severity5" +
            " FROM TBL_RISK_ASSPLAN_RISK a" +
            "    LEFT JOIN TBL_RISK r ON a.RISKID = r.RISKID" +
            "    LEFT JOIN TBL_ORGANIZATION o ON o.ORGID = r.UNIT " +
            " WHERE " +
            "    a.FREQUENCY IS NOT NULL " +
            "    AND a.SEVERITY IS NOT NULL")
    HomeRiskRltVo selectHomeRiskRltVo();

    @Select("SELECT DISTINCT(a.ASSRISKID),r.RISKNUMBER,R.RISKNAME,R.RISKDES,a.FREQUENCY,a.SEVERITY,a.RISKLEVEL,a.ASSDATE,s.REALNAME " +
            " FROM TBL_RISK_ASSPLAN_RISK a " +
            " LEFT JOIN TBL_RISK r ON a.RISKID = r.RISKID " +
            " LEFT JOIN TBL_RISK_RISKMARKING m ON a.ASSRISKID = m.ASSRISKID " +
            " LEFT JOIN TBL_STAFF s ON m.STAFFID = s.STAFFID " +
            " WHERE a.FREQUENCY = #{FREQUENCY} AND a.SEVERITY = #{SEVERITY} AND r.UNIT = #{UNIT} ")
    List<RiskPgVo> selectASSRISKIDByUnit(@Param("FREQUENCY") BigDecimal FREQUENCY, @Param("SEVERITY") BigDecimal SEVERITY, @Param("UNIT") BigDecimal UNIT);


}
