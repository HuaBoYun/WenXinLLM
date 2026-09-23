package com.huabo.fxgl.mapper;

import com.huabo.fxgl.entity.Flowdes;
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
 * @since 2022-08-08
 */

@Repository
public interface FlowdesMapper extends BaseMapper<Flowdes> {
    @Select("select * from tbl_flowdes where flowid in (select flowid from tbl_risk_flow where riskid = #{param}) order by memo")
    List<Flowdes> selectFlowdesByRiskid(String riskid);
}
