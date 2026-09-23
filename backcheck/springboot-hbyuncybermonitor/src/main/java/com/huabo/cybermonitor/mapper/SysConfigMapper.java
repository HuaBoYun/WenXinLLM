package com.huabo.cybermonitor.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.huabo.cybermonitor.entity.SysConfig;
import com.huabo.cybermonitor.vo.SysConfigQueryVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 系统配置 Mapper 接口
 *
 * @author system
 * @since 2024-01-01
 */
@Mapper
public interface SysConfigMapper extends BaseMapper<SysConfig> {

    /**
     * 分页查询系统配置列表
     *
     * @param page  分页参数
     * @param query 查询条件
     * @return 分页结果
     */
    IPage<SysConfig> selectConfigList(IPage<SysConfig> page, @Param("query") SysConfigQueryVO query);

    /**
     * 根据配置键查询配置
     *
     * @param configKey 配置键
     * @return 配置信息
     */
    @Select("SELECT * FROM SYS_CONFIG WHERE CONFIG_KEY = #{configKey} AND IS_ENABLED = '1'")
    SysConfig selectByConfigKey(@Param("configKey") String configKey);

    /**
     * 根据配置键获取配置值
     *
     * @param configKey 配置键
     * @return 配置值
     */
    @Select("SELECT CONFIG_VALUE FROM SYS_CONFIG WHERE CONFIG_KEY = #{configKey} AND IS_ENABLED = '1'")
    String selectConfigValue(@Param("configKey") String configKey);

    /**
     * 根据配置分组查询配置列表
     *
     * @param configGroup 配置分组
     * @return 配置列表
     */
    @Select("SELECT * FROM SYS_CONFIG WHERE CONFIG_GROUP = #{configGroup} AND IS_ENABLED = '1' ORDER BY SORT_ORDER ASC, CREATE_TIME ASC")
    List<SysConfig> selectConfigsByGroup(@Param("configGroup") String configGroup);

    /**
     * 根据配置类型查询配置列表
     *
     * @param configType 配置类型
     * @return 配置列表
     */
    @Select("SELECT * FROM SYS_CONFIG WHERE CONFIG_TYPE = #{configType} AND IS_ENABLED = '1' ORDER BY SORT_ORDER ASC, CREATE_TIME ASC")
    List<SysConfig> selectConfigsByType(@Param("configType") String configType);

    /**
     * 查询系统配置列表
     *
     * @return 系统配置列表
     */
    @Select("SELECT * FROM SYS_CONFIG WHERE IS_SYSTEM = '1' AND IS_ENABLED = '1' ORDER BY SORT_ORDER ASC, CREATE_TIME ASC")
    List<SysConfig> selectSystemConfigs();

    /**
     * 查询业务配置列表
     *
     * @return 业务配置列表
     */
    @Select("SELECT * FROM SYS_CONFIG WHERE IS_SYSTEM = '0' AND IS_ENABLED = '1' ORDER BY SORT_ORDER ASC, CREATE_TIME ASC")
    List<SysConfig> selectBusinessConfigs();

    /**
     * 查询启用的配置列表
     *
     * @return 启用的配置列表
     */
    @Select("SELECT * FROM SYS_CONFIG WHERE IS_ENABLED = '1' ORDER BY CONFIG_GROUP ASC, SORT_ORDER ASC, CREATE_TIME ASC")
    List<SysConfig> selectEnabledConfigs();

    /**
     * 获取配置统计信息
     *
     * @return 统计信息
     */
    @Select("SELECT " +
            "COUNT(*) as totalCount, " +
            "SUM(CASE WHEN IS_ENABLED = '1' THEN 1 ELSE 0 END) as enabledCount, " +
            "SUM(CASE WHEN IS_SYSTEM = '1' THEN 1 ELSE 0 END) as systemCount, " +
            "COUNT(DISTINCT CONFIG_GROUP) as groupCount " +
            "FROM SYS_CONFIG")
    Map<String, Object> selectConfigStatistics();

    /**
     * 根据配置分组获取统计分布
     *
     * @return 分组分布统计
     */
    @Select("SELECT CONFIG_GROUP as configGroup, COUNT(*) as count FROM SYS_CONFIG GROUP BY CONFIG_GROUP ORDER BY count DESC")
    List<Map<String, Object>> selectGroupDistribution();

    /**
     * 根据配置类型获取统计分布
     *
     * @return 类型分布统计
     */
    @Select("SELECT CONFIG_TYPE as configType, COUNT(*) as count FROM SYS_CONFIG GROUP BY CONFIG_TYPE ORDER BY count DESC")
    List<Map<String, Object>> selectTypeDistribution();

    /**
     * 批量插入配置
     *
     * @param configList 配置列表
     * @return 插入数量
     */
    int batchInsert(@Param("list") List<SysConfig> configList);

    /**
     * 根据条件导出配置列表
     *
     * @param query 查询条件
     * @return 配置列表
     */
    List<SysConfig> selectConfigListForExport(@Param("query") SysConfigQueryVO query);

    /**
     * 根据配置键检查是否存在
     *
     * @param configKey 配置键
     * @param excludeId 排除的配置ID
     * @return 配置信息
     */
    @Select("SELECT * FROM SYS_CONFIG WHERE CONFIG_KEY = #{configKey} " +
            "AND (#{excludeId} IS NULL OR CONFIG_ID != #{excludeId}) LIMIT 1")
    SysConfig selectByConfigKeyExclude(@Param("configKey") String configKey, @Param("excludeId") String excludeId);

    /**
     * 更新配置值
     *
     * @param configKey   配置键
     * @param configValue 配置值
     * @return 更新数量
     */
    @Select("UPDATE SYS_CONFIG SET CONFIG_VALUE = #{configValue}, UPDATE_TIME = NOW() WHERE CONFIG_KEY = #{configKey}")
    int updateConfigValue(@Param("configKey") String configKey, @Param("configValue") String configValue);

    /**
     * 更新配置状态
     *
     * @param configId  配置ID
     * @param isEnabled 是否启用
     * @return 更新数量
     */
    @Select("UPDATE SYS_CONFIG SET IS_ENABLED = #{isEnabled}, UPDATE_TIME = NOW() WHERE CONFIG_ID = #{configId}")
    int updateConfigStatus(@Param("configId") String configId, @Param("isEnabled") String isEnabled);

    /**
     * 批量更新配置状态
     *
     * @param configIds 配置ID列表
     * @param isEnabled 是否启用
     * @return 更新数量
     */
    int batchUpdateStatus(@Param("configIds") List<String> configIds, @Param("isEnabled") String isEnabled);

    /**
     * 获取配置的最大排序号
     *
     * @param configGroup 配置分组
     * @return 最大排序号
     */
    @Select("SELECT COALESCE(MAX(SORT_ORDER), 0) FROM SYS_CONFIG WHERE CONFIG_GROUP = #{configGroup}")
    Integer selectMaxSortOrder(@Param("configGroup") String configGroup);

    /**
     * 根据分组和键值对查询配置
     *
     * @param configGroup 配置分组
     * @param configKeys  配置键列表
     * @return 配置列表
     */
    List<SysConfig> selectConfigsByGroupAndKeys(@Param("configGroup") String configGroup, @Param("configKeys") List<String> configKeys);

    /**
     * 获取数据采集相关配置
     *
     * @return 数据采集配置列表
     */
    @Select("SELECT * FROM SYS_CONFIG WHERE CONFIG_GROUP = 'DATA_COLLECTION' AND IS_ENABLED = '1' ORDER BY SORT_ORDER ASC")
    List<SysConfig> selectDataCollectionConfigs();

    /**
     * 获取系统通知相关配置
     *
     * @return 通知配置列表
     */
    @Select("SELECT * FROM SYS_CONFIG WHERE CONFIG_GROUP = 'NOTIFICATION' AND IS_ENABLED = '1' ORDER BY SORT_ORDER ASC")
    List<SysConfig> selectNotificationConfigs();

    /**
     * 获取报表相关配置
     *
     * @return 报表配置列表
     */
    @Select("SELECT * FROM SYS_CONFIG WHERE CONFIG_GROUP = 'REPORT' AND IS_ENABLED = '1' ORDER BY SORT_ORDER ASC")
    List<SysConfig> selectReportConfigs();
}
