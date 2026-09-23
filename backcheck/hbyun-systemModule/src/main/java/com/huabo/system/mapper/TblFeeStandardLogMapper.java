package com.huabo.system.mapper;

import java.math.BigDecimal;
import java.util.List;
import org.apache.ibatis.annotations.*;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.system.entity.TblFeeStandardLog;

public interface TblFeeStandardLogMapper extends BaseMapper<TblFeeStandardLog> {

    @Select("SELECT HBYUN_FEE_STD_LOG_SEQ.NEXTVAL FROM DUAL")
    BigDecimal getNextId();

    @Select("SELECT * FROM TBL_FEE_STANDARD_LOG WHERE RIGHT_ID = #{rightId} ORDER BY CHANGE_TIME DESC")
    List<TblFeeStandardLog> findByRightId(@Param("rightId") BigDecimal rightId);

    // Get the latest log entry for a given RIGHT_ID (used as price snapshot)
    @Select("SELECT * FROM TBL_FEE_STANDARD_LOG WHERE RIGHT_ID = #{rightId} ORDER BY CHANGE_TIME DESC FETCH FIRST 1 ROWS ONLY")
    TblFeeStandardLog findLatestByRightId(@Param("rightId") BigDecimal rightId);
}
