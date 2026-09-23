package com.huabo.fxgl.mapper;

import com.huabo.fxgl.entity.RiskInfludegree;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import com.huabo.fxgl.entity.RiskLevelmapping;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.Set;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author xujiajun
 * @since 2022-08-12
 */
@Repository
public interface RiskInfludegreeMapper extends BaseMapper<RiskInfludegree> {
    @Select("select * from TBL_RISK_INFLUDEGREE WHERE assstdid=#{assstdid}")
    Set<RiskInfludegree> selectListByAssstdid(BigDecimal assstdid);
    
    
    @Select("select TRI.DEGREEID,TRI.ASSSTDID,TRI.RILEVEL,TRI.INFLUDEGREEDES,TRI.MEMO,TRL.RLEVELMAPID,TRL.POSS1,TRL.POSS2,TRL.POSS3,TRL.POSS4,TRL.POSS5,TRL.MEMO AS TRLMEMO,TRL.INFLUDEGREE from TBL_RISK_INFLUDEGREE TRI LEFT JOIN TBL_RISK_LEVELMAPPING TRL ON TRI.DEGREEID = TRL.INFLUDEGREE AND TRI.ASSSTDID = TRL.ASSSTDID where TRI.assstdid = #{assstdid} ORDER BY rilevel ASC")
    @Results({
    	@Result(column="DEGREEID",property="degreeid"),
    	@Result(column="ASSSTDID",property="assstdid"),
    	@Result(column="RILEVEL",property="rilevel"),
    	@Result(column="INFLUDEGREEDES",property="infludegreedes"),
    	@Result(column="MEMO",property="memo"),
    	@Result(column="RLEVELMAPID",property="riskLevelMapping.rlevelmapid",id=true),
    	@Result(column="POSS1",property="riskLevelMapping.poss1"),
    	@Result(column="POSS2",property="riskLevelMapping.poss2"),
    	@Result(column="POSS3",property="riskLevelMapping.poss3"),
    	@Result(column="POSS4",property="riskLevelMapping.poss4"),
    	@Result(column="POSS5",property="riskLevelMapping.poss5"),
    	@Result(column="TRLMEMO",property="riskLevelMapping.memo"),
    	@Result(column="INFLUDEGREE",property="riskLevelMapping.infludegree"),
    	@Result(column="ASSSTDID",property="riskLevelMapping.assstdid"),
    })
    List<RiskInfludegree> findAllByAssId(BigDecimal assstdid);

}
