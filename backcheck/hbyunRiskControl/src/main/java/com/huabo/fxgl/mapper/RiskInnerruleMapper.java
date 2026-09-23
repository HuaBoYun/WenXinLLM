package com.huabo.fxgl.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.huabo.fxgl.entity.Innerrule;
import com.huabo.fxgl.entity.RiskInnerrule;
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
public interface RiskInnerruleMapper extends BaseMapper<RiskInnerrule> {
    @Select("select * from TBL_INNERRULE where innrulid in (select innrulid from TBL_RISK_INNERRULE where riskid = #{param1})")
    List<Innerrule> selectInnerRuleByRiskId(String riskid,QueryWrapper queryWrapper);
    @Select("select count(*) from TBL_RISK_INNERRULE where RISKID = #{param1} and INNRULID = #{param2}")
    Integer selectisexist(BigDecimal riskid,BigDecimal innrulid);
}
