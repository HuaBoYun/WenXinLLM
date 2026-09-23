package com.financial.sharing.enterpriseReport.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.financial.sharing.enterpriseReport.entity.TblIndicatorInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 指标信息Mapper接口
 * 
 * @author system
 * @since 2026-01-30
 */
public interface IndicatorMapper extends BaseMapper<TblIndicatorInfo> {

    /**
     * 根据编码查询指标
     */
    @Select("SELECT * FROM TBL_INDICATOR_INFO WHERE INDICATOR_CODE = #{indicatorCode} AND TENANT_ID = #{tenantId}")
    TblIndicatorInfo selectByCode(@Param("indicatorCode") String indicatorCode, @Param("tenantId") String tenantId);

    /**
     * 检查编码是否存在
     */
    @Select("SELECT COUNT(*) FROM TBL_INDICATOR_INFO WHERE INDICATOR_CODE = #{indicatorCode} AND TENANT_ID = #{tenantId} AND INDICATOR_ID != #{excludeId}")
    int checkCodeExists(@Param("indicatorCode") String indicatorCode, @Param("tenantId") String tenantId, @Param("excludeId") String excludeId);
}

