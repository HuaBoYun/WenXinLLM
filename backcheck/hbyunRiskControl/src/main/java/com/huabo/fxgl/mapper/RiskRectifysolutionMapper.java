package com.huabo.fxgl.mapper;

import com.huabo.fxgl.entity.Find;
import com.huabo.fxgl.entity.RiskRectifysolution;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.fxgl.entity.YyPrice;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

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
public interface RiskRectifysolutionMapper extends BaseMapper<RiskRectifysolution> {


    @Select("select * from TBL_RISK_RECTIFSOLUTION")
    List<RiskRectifysolution> findTblRiskRectifysolutionByRisEvent(String eventid, Find find);

    @Select("select count(*) from TBL_RISK_RECTIFYSOLUTION where SOLUTIONCODE = #{param}")
    Integer findSolutioncode(String solutioncode);

    @Select("select * from TBL_RISK_RECTIFYSOLUTION where RECTSOLID = #{param}")
    List<RiskRectifysolution> findBysql(String rectsolid);

}
