package com.huabo.system.mapper;

import java.math.BigDecimal;
import java.util.List;
import org.apache.ibatis.annotations.*;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.huabo.system.entity.TblFeeStandard;

public interface TblFeeStandardMapper extends BaseMapper<TblFeeStandard> {

    @Select("SELECT HBYUN_FEE_STD_SEQ.NEXTVAL FROM DUAL")
    BigDecimal getNextId();

    @Select("SELECT * FROM TBL_FEE_STANDARD WHERE RIGHT_ID = #{rightId}")
    TblFeeStandard findByRightId(@Param("rightId") BigDecimal rightId);

    @Select("SELECT * FROM TBL_FEE_STANDARD WHERE MODULE_TYPE = #{moduleType} AND STATUS = 1")
    List<TblFeeStandard> findByModuleType(@Param("moduleType") String moduleType);

    @Select("SELECT * FROM TBL_FEE_STANDARD WHERE STATUS = 1")
    List<TblFeeStandard> findAllEnabled();

    // Find fee standard by PERMS and PATH from TBL_SYSTEM_RIGHT
    @Select("SELECT fs.* FROM TBL_FEE_STANDARD fs INNER JOIN TBL_SYSTEM_RIGHT sr ON fs.RIGHT_ID = sr.ID WHERE sr.PERMS = #{perms} AND sr.PATH = #{path} AND fs.STATUS = 1")
    TblFeeStandard findByPermsAndPath(@Param("perms") String perms, @Param("path") String path);
}
