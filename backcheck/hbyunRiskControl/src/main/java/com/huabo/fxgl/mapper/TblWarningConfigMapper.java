package com.huabo.fxgl.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.fxgl.entity.TblWarningConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 预警配置表 Mapper 接口
 *
 * @author 华博云
 * @date 2025-09-30
 */
@Mapper
public interface TblWarningConfigMapper extends BaseMapper<TblWarningConfig> {

    /**
     * 分页查询预警配置列表
     *
     * @param page 分页参数
     * @param configType 配置类型
     * @param isEnabled 是否启用
     * @return 分页结果
     */
    @Select("<script>" +
            "SELECT * FROM TBL_WARNING_CONFIG " +
            "WHERE 1=1 " +
            "<if test='configType != null and configType != \"\"'>" +
            "AND CONFIG_TYPE = #{configType} " +
            "</if>" +
            "<if test='isEnabled != null and isEnabled != \"\"'>" +
            "AND IS_ENABLED = #{isEnabled} " +
            "</if>" +
            "ORDER BY SORT_ORDER ASC, CREATE_TIME DESC" +
            "</script>")
    IPage<TblWarningConfig> selectConfigPage(Page<TblWarningConfig> page, 
                                           @Param("configType") String configType,
                                           @Param("isEnabled") String isEnabled);

    /**
     * 根据配置类型查询配置列表
     *
     * @param configType 配置类型
     * @return 配置列表
     */
    @Select("SELECT * FROM TBL_WARNING_CONFIG " +
            "WHERE CONFIG_TYPE = #{configType} AND IS_ENABLED = 'Y' " +
            "ORDER BY SORT_ORDER ASC")
    List<TblWarningConfig> selectByConfigType(@Param("configType") String configType);

    /**
     * 根据配置名称查询配置
     *
     * @param configName 配置名称
     * @return 配置信息
     */
    @Select("SELECT * FROM TBL_WARNING_CONFIG " +
            "WHERE CONFIG_NAME = #{configName} AND IS_ENABLED = 'Y'")
    TblWarningConfig selectByConfigName(@Param("configName") String configName);

    /**
     * 查询所有启用的配置
     *
     * @return 配置列表
     */
    @Select("SELECT * FROM TBL_WARNING_CONFIG " +
            "WHERE IS_ENABLED = 'Y' " +
            "ORDER BY CONFIG_TYPE, SORT_ORDER ASC")
    List<TblWarningConfig> selectAllEnabled();

    /**
     * 更新配置状态
     *
     * @param configId 配置ID
     * @param isEnabled 是否启用
     * @param updateUser 更新人
     * @return 更新行数
     */
    @Select("UPDATE TBL_WARNING_CONFIG " +
            "SET IS_ENABLED = #{isEnabled}, UPDATE_USER = #{updateUser}, UPDATE_TIME = SYSDATE " +
            "WHERE CONFIG_ID = #{configId}")
    int updateConfigStatus(@Param("configId") String configId, 
                          @Param("isEnabled") String isEnabled,
                          @Param("updateUser") String updateUser);
}
