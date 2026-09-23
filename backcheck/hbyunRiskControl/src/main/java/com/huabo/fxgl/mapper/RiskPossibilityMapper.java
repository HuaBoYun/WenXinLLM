package com.huabo.fxgl.mapper;

import com.huabo.fxgl.entity.RiskPossibility;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

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
public interface RiskPossibilityMapper extends BaseMapper<RiskPossibility> {
	
    @Select("select * from TBL_RISK_POSSIBILITY WHERE assstdid=#{assstdid}")
    Set<RiskPossibility> selectListByAssstdid(BigDecimal assstdid);
    
    
    @Select("select * from TBL_RISK_POSSIBILITY WHERE assstdid=#{assstdid} order by RPLEVEL")
    List<RiskPossibility>  findAllByAssId(BigDecimal assstdid);
    
    
}
