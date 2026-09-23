package com.huabo.fxgl.mapper;

import com.huabo.fxgl.entity.RiskRiskAtt;
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
public interface RiskRiskAttMapper extends BaseMapper<RiskRiskAtt> {
    @Select("select * from tbl_risk_risk_att where riskid = #{param}")
    List<RiskRiskAtt> selectRiskAttByRiskid(String riskid);
}
