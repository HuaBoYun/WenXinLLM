package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TcDataSourceConfig;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 财资公共模块 - 数据源配置管理Mapper
 * 使用MyBatis-Plus的BaseMapper
 *
 * @author HuaBo Cloud
 * @since 2024-01-01
 */
public interface TcDataSourceConfigMapper extends BaseMapper<TcDataSourceConfig> {

    /**
     * 根据数据源编码查询
     * 
     * @param sourceCode 数据源编码
     * @return 数据源配置信息
     */
    TcDataSourceConfig selectBySourceCode(@Param("sourceCode") String sourceCode);

    /**
     * 根据条件查询数据源配置列表
     *
     * @param sourceCode 数据源编码
     * @param sourceName 数据源名称
     * @param sourceType 数据源类型
     * @param dataType 数据类型
     * @param syncStatus 同步状态
     * @param isActive 是否激活
     * @param status 状态
     * @return 数据源配置列表
     */
    List<TcDataSourceConfig> selectByCondition(@Param("sourceCode") String sourceCode,
                                               @Param("sourceName") String sourceName,
                                               @Param("sourceType") String sourceType,
                                               @Param("dataType") String dataType,
                                               @Param("syncStatus") String syncStatus,
                                               @Param("isActive") String isActive,
                                               @Param("status") String status);

    /**
     * 根据完整条件查询数据源配置列表（包含连接状态）
     *
     * @param sourceCode 数据源编码
     * @param sourceName 数据源名称
     * @param sourceType 数据源类型
     * @param connectionStatus 连接状态
     * @param environment 环境类型
     * @param syncStatus 同步状态
     * @param isEnabled 是否启用
     * @param status 状态
     * @return 数据源配置列表
     */
    List<TcDataSourceConfig> selectByFullCondition(@Param("sourceCode") String sourceCode,
                                                   @Param("sourceName") String sourceName,
                                                   @Param("sourceType") String sourceType,
                                                   @Param("connectionStatus") String connectionStatus,
                                                   @Param("environment") String environment,
                                                   @Param("syncStatus") String syncStatus,
                                                   @Param("isEnabled") String isEnabled,
                                                   @Param("status") String status);

    /**
     * 根据数据源类型查询
     * 
     * @param sourceType 数据源类型
     * @return 数据源配置列表
     */
    List<TcDataSourceConfig> selectBySourceType(@Param("sourceType") String sourceType);

    /**
     * 根据数据类型查询
     * 
     * @param dataType 数据类型
     * @return 数据源配置列表
     */
    List<TcDataSourceConfig> selectByDataType(@Param("dataType") String dataType);

    /**
     * 根据同步状态查询
     * 
     * @param syncStatus 同步状态
     * @return 数据源配置列表
     */
    List<TcDataSourceConfig> selectBySyncStatus(@Param("syncStatus") String syncStatus);

    /**
     * 查询激活的数据源
     * 
     * @param sourceType 数据源类型
     * @param dataType 数据类型
     * @return 激活的数据源配置列表
     */
    List<TcDataSourceConfig> selectActiveDataSources(@Param("sourceType") String sourceType,
                                                     @Param("dataType") String dataType);

    /**
     * 查询需要同步的数据源
     * 
     * @param currentTime 当前时间
     * @return 需要同步的数据源配置列表
     */
    List<TcDataSourceConfig> selectDataSourcesForSync(@Param("currentTime") Date currentTime);

    /**
     * 查询同步异常的数据源
     * 
     * @return 同步异常的数据源配置列表
     */
    List<TcDataSourceConfig> selectErrorDataSources();

    /**
     * 查询所有数据源类型
     * 
     * @return 数据源类型列表
     */
    List<String> selectAllSourceTypes();

    /**
     * 查询所有数据类型
     * 
     * @return 数据类型列表
     */
    List<String> selectAllDataTypes();

    /**
     * 查询所有同步频率
     * 
     * @return 同步频率列表
     */
    List<String> selectAllSyncFrequencies();

    /**
     * 查询所有同步状态
     * 
     * @return 同步状态列表
     */
    List<String> selectAllSyncStatuses();

    /**
     * 批量插入数据源配置
     * 
     * @param dataSourceConfigs 数据源配置列表
     * @return 插入数量
     */
    int batchInsert(@Param("list") List<TcDataSourceConfig> dataSourceConfigs);

    /**
     * 批量更新数据源配置
     * 
     * @param dataSourceConfigs 数据源配置列表
     * @return 更新数量
     */
    int batchUpdate(@Param("list") List<TcDataSourceConfig> dataSourceConfigs);

    /**
     * 更新同步状态
     * 
     * @param id 数据源ID
     * @param syncStatus 同步状态
     * @param lastSyncTime 最后同步时间
     * @param errorCount 错误次数
     * @return 更新数量
     */
    int updateSyncStatus(@Param("id") String id,
                         @Param("syncStatus") String syncStatus,
                         @Param("lastSyncTime") Date lastSyncTime,
                         @Param("errorCount") Integer errorCount);

    /**
     * 更新激活状态
     * 
     * @param id 数据源ID
     * @param isActive 是否激活
     * @param updateUser 更新人
     * @return 更新数量
     */
    int updateActiveStatus(@Param("id") String id,
                           @Param("isActive") String isActive,
                           @Param("updateUser") String updateUser);

    /**
     * 更新状态
     * 
     * @param id 数据源ID
     * @param status 状态
     * @param updateUser 更新人
     * @return 更新数量
     */
    int updateStatus(@Param("id") String id,
                     @Param("status") String status,
                     @Param("updateUser") String updateUser);

    /**
     * 重置错误次数
     * 
     * @param id 数据源ID
     * @return 更新数量
     */
    int resetErrorCount(@Param("id") String id);

    /**
     * 统计数据源配置
     * 
     * @return 统计结果
     */
    List<Map<String, Object>> getStatistics();

    /**
     * 检查数据源编码是否存在
     * 
     * @param sourceCode 数据源编码
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    int checkSourceCodeExists(@Param("sourceCode") String sourceCode,
                              @Param("excludeId") String excludeId);

    // 添加缺少的select方法
    List<TcDataSourceConfig> selectEnabledSources(@Param("dataType") String dataType);
    List<TcDataSourceConfig> selectByConnectionStatus(@Param("connectionStatus") String connectionStatus);
    List<TcDataSourceConfig> selectSyncRequiredSources();
    List<TcDataSourceConfig> selectSyncFailedSources();
    List<String> selectAllConnectionStatuses();

    // 添加缺少的update方法
    int updateLastSyncTime(@Param("id") String id, @Param("lastSyncTime") Date lastSyncTime);

    /**
     * 根据数据源类型和数据类型统计数量
     * 
     * @param sourceType 数据源类型
     * @param dataType 数据类型
     * @return 数据源数量
     */
    int countByTypeAndDataType(@Param("sourceType") String sourceType,
                               @Param("dataType") String dataType);

    // 移除默认方法，改为在Service中实现具体业务逻辑
}
