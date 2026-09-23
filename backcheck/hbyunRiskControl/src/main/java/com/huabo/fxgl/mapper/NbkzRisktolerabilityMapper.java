package com.huabo.fxgl.mapper;

import com.huabo.fxgl.entity.NbkzRisktolerability;
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
 * @since 2022-08-09
 */
@Repository
public interface NbkzRisktolerabilityMapper extends BaseMapper<NbkzRisktolerability> {
    @Select("select * from TBL_NBKZ_RISKTOLERABILITY where riskid = #{riskid}")
    List<NbkzRisktolerability> findRiskTolerByRiskid(String riskid);
}
