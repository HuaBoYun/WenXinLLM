package com.huabo.fxgl.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huabo.fxgl.entity.Innerrule;
import com.huabo.fxgl.entity.Outerrule;
import com.huabo.fxgl.entity.RiskOuterrule;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
 * @since 2022-08-05
 */
@Repository
public interface RiskOuterruleMapper extends BaseMapper<RiskOuterrule> {
    @Select("select * from tbl_outerrule where outrulid in (select outrulid from TBL_RISK_OUTERRULE where riskid = #{param1})")
    List<Outerrule> selectOuterRuleByRiskId(String riskid, QueryWrapper queryWrapper);
    @Select("select count(*) from TBL_RISK_OUTERRULE where RISKID = #{param1} and OUTRULID = #{param2}")
    Integer selectisexist(BigDecimal riskid, BigDecimal innrulid);
}
