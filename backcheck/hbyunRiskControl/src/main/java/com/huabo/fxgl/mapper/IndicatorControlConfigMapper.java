package com.huabo.fxgl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.fxgl.entity.TblIndicatorControlConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface IndicatorControlConfigMapper extends BaseMapper<TblIndicatorControlConfig> {

    @Select("SELECT c.*, " +
            "r.NAME AS page_name, " +
            "pr.NAME AS module_name, " +
            "m.MODEL_NAME AS model_name, m.MODEL_CODE AS model_code, " +
            "r.MODULETYPE AS module_type " +
            "FROM TBL_INDICATOR_CONTROL_CONFIG c " +
            "LEFT JOIN TBL_SYSTEM_RIGHT r ON c.RIGHT_ID = r.ID " +
            "LEFT JOIN TBL_SYSTEM_RIGHT pr ON r.PARENT = pr.ID " +
            "LEFT JOIN TBL_DATA_MODEL m ON c.MODEL_ID = m.MODEL_ID " +
            "ORDER BY c.CREATE_TIME DESC")
    IPage<TblIndicatorControlConfig> selectConfigPage(IPage<?> page);

    @Select("SELECT c.*, m.MODEL_NAME AS model_name, m.MODEL_CODE AS model_code " +
            "FROM TBL_INDICATOR_CONTROL_CONFIG c " +
            "LEFT JOIN TBL_DATA_MODEL m ON c.MODEL_ID = m.MODEL_ID " +
            "WHERE c.RIGHT_ID = #{rightId} AND c.IS_ENABLED = 'Y' " +
            "AND c.SYSTEM_TYPE = 'INTERNAL' " +
            "AND m.IS_ENABLED = 'Y' AND m.STATUS = 'PUBLISHED'")
    List<TblIndicatorControlConfig> selectEnabledByRightId(@Param("rightId") String rightId);

    @Select("SELECT c.*, m.MODEL_NAME AS model_name, m.MODEL_CODE AS model_code " +
            "FROM TBL_INDICATOR_CONTROL_CONFIG c " +
            "LEFT JOIN TBL_DATA_MODEL m ON c.MODEL_ID = m.MODEL_ID " +
            "WHERE c.EXTERNAL_PAGE_KEY = #{externalPageKey} AND c.IS_ENABLED = 'Y' " +
            "AND c.SYSTEM_TYPE = 'EXTERNAL' " +
            "AND m.IS_ENABLED = 'Y' AND m.STATUS = 'PUBLISHED'")
    List<TblIndicatorControlConfig> selectEnabledByExternalKey(@Param("externalPageKey") String externalPageKey);
}
