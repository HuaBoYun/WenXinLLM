package com.huabo.fxgl.mapper;

import com.huabo.fxgl.entity.Risktolerability;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

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
public interface RisktolerabilityMapper extends BaseMapper<Risktolerability> {
    @Select("select * from tbl_risktolerability where riskid = #{param} order by RTCODE")
    List<Risktolerability> selectRiskTolerByRiskid(String riskid);
}
