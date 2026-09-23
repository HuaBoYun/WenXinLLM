package com.huabo.system.mapper;

import java.math.BigDecimal;
import java.util.List;
import org.apache.ibatis.annotations.*;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.system.entity.TblFeeStandardTier;

/**
 * 阶梯计费规则Mapper
 */
public interface TblFeeStandardTierMapper extends BaseMapper<TblFeeStandardTier> {

    @Select("SELECT HBYUN_FEE_TIER_SEQ.NEXTVAL FROM DUAL")
    BigDecimal getNextId();

    @Select("SELECT * FROM TBL_FEE_STANDARD_TIER WHERE STANDARD_ID = #{standardId} ORDER BY SORT ASC, MIN_COUNT ASC")
    List<TblFeeStandardTier> findByStandardId(@Param("standardId") BigDecimal standardId);

    @Delete("DELETE FROM TBL_FEE_STANDARD_TIER WHERE STANDARD_ID = #{standardId}")
    int deleteByStandardId(@Param("standardId") BigDecimal standardId);
}
