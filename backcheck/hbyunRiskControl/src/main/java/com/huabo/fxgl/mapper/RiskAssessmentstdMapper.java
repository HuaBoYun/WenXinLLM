package com.huabo.fxgl.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.huabo.fxgl.entity.RiskAssessmentstd;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LiYe
 * @since 2022-08-01
 */
@Repository
public interface RiskAssessmentstdMapper extends BaseMapper<RiskAssessmentstd> {

    /**
     * 查询评估标准
     * @param page
     * @param wrapper
     * @return
     */
    List<RiskAssessmentstd> selectRiskAssessMents(@Param(Constants.WRAPPER) QueryWrapper wrapper,@Param("sql") String sql);

    
    @Select("SELECT DISTINCT TR.* from TBL_RISK_ASSESSMENTSTD TR  " +
            "LEFT JOIN TBL_RISK_LEVELMAPPING TL ON TR.ASSSTDID=TL.ASSSTDID  " +
            "WHERE TR.assstatus = 1 AND TL.RLEVELMAPID IS NOT NULL AND ${ew.sqlSegment}")
    IPage<RiskAssessmentstd> selectRiskAssessMentList(IPage page, @Param(Constants.WRAPPER) QueryWrapper wrapper);

    
    @Select("Select * from TBL_RISK_ASSESSMENTSTD Where ASSSTDID = #{id}")
    RiskAssessmentstd select(@Param("id") BigDecimal id);
    
    
    @Select("SELECT COUNT(*) FROM  TBL_RISK_ASSESSMENTSTD  where assNumber=#{assNumber} and COMPANYID IN (SELECT ORGID FROM TBL_ORGANIZATION WHERE  1=1 START WITH FATHERORGID=#{orgid} AND ORGTYPE=0  CONNECT BY PRIOR ORGID = FATHERORGID UNION ALL SELECT #{orgid}  FROM DUAL)")
    Integer selectRiskAssessMentsNumber(@Param("assNumber")String assNumber,@Param("orgid")BigDecimal orgid);

    
    @Select("SELECT * FROM  TBL_RISK_ASSESSMENTSTD  where assname=#{assname} and assdes=#{assdes} and COMPANYID IN (SELECT ORGID FROM TBL_ORGANIZATION WHERE  1=1 START WITH FATHERORGID=#{orgid} AND ORGTYPE=0  CONNECT BY PRIOR ORGID = FATHERORGID UNION ALL SELECT #{orgid}  FROM DUAL)")
    RiskAssessmentstd selectNumberByInfo(@Param("assname")String assname,@Param("assdes")String assdes,@Param("orgid")BigDecimal orgid);

    
    @Select("SELECT * from TBL_RISK_ASSESSMENTSTD r where 1=1  ${sql} AND ${ew.sqlSegment}")
    List<RiskAssessmentstd> getRiskAssessMentList( @Param(Constants.WRAPPER) QueryWrapper wrapper,@Param("sql")String sql);
    
   
}
