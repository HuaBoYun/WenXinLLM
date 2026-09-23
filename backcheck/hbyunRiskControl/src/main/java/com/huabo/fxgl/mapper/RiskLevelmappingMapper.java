package com.huabo.fxgl.mapper;

import com.huabo.fxgl.entity.RiskLevelmapping;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;
import com.huabo.fxgl.entity.RiskPossibility;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author LiYe
 * @since 2022-08-01
 */
@Repository
public interface RiskLevelmappingMapper extends BaseMapper<RiskLevelmapping> {
    @Select("select * from TBL_RISK_LEVELMAPPING r where r.assstdid =#{mentId} and r.infludegree=#{DegreeId}")
    List<RiskLevelmapping> getRiskLevelMappingBymentIdAndDegreeId(@Param("mentId")BigDecimal mentId,@Param("DegreeId")String DegreeId);
  
    @Select("select R.*  from TBL_RISK_LEVELMAPPING R WHERE R.INFLUDEGREE = #{influid}")
    RiskLevelmapping getByInfluId(@Param("influid")String influid);
   
    @Select("select * from TBL_RISK_LEVELMAPPING WHERE assstdid=#{assstdid}")
    Set<RiskLevelmapping> selectListByAssstdid(@Param("assstdid")BigDecimal assstdid);
    
    Integer getFxpgRlt(@Param("orgid")String orgid,@Param("type") String type);
    
    Integer getFxpgRltGb(@Param("orgid")String orgid);
}
