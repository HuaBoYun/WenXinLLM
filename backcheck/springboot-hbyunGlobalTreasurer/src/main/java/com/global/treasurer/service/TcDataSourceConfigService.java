package com.global.treasurer.service;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TcDataSourceConfig;

import java.util.List;

/**
 * 数据源配置管理服务接口
 * 
 * @author system
 * @date 2024-09-20
 */
public interface TcDataSourceConfigService {

    /**
     * 分页查询数据源配置列表
     *
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @param sourceCode 数据源编码
     * @param sourceName 数据源名称
     * @param sourceType 数据源类型
     * @param connectionStatus 连接状态
     * @param environment 环境类型
     * @param syncStatus 同步状态
     * @param isEnabled 是否启用
     * @param status 状态
     * @return 分页结果
     */
    PageInfo<TcDataSourceConfig> getList(int pageNum, int pageSize, String sourceCode, String sourceName,
                                        String sourceType, String connectionStatus, String environment,
                                        String syncStatus, String isEnabled, String status);

    /**
     * 根据ID查询数据源配置
     * 
     * @param id 主键ID
     * @return 数据源配置信息
     */
    TcDataSourceConfig getById(String id);

    /**
     * 新增或更新数据源配置
     * 
     * @param dataSourceConfig 数据源配置信息
     * @param operator 操作人
     * @return 操作结果
     */
    int saveOrUpdate(TcDataSourceConfig dataSourceConfig, String operator);

    /**
     * 删除数据源配置
     * 
     * @param id 主键ID
     * @param operator 操作人
     * @return 操作结果
     */
    int delete(String id, String operator);

    /**
     * 根据数据源编码查询
     *
     * @param sourceCode 数据源编码
     * @return 数据源配置信息
     */
    TcDataSourceConfig getBySourceCode(String sourceCode);

    /**
     * 根据数据源类型查询
     *
     * @param sourceType 数据源类型
     * @return 数据源配置列表
     */
    List<TcDataSourceConfig> getBySourceType(String sourceType);

    /**
     * 获取启用的数据源列表
     *
     * @param sourceType 数据源类型(可选)
     * @return 数据源列表
     */
    List<TcDataSourceConfig> getEnabledSources(String sourceType);

    /**
     * 根据连接状态查询
     *
     * @param connectionStatus 连接状态
     * @return 数据源配置列表
     */
    List<TcDataSourceConfig> getByConnectionStatus(String connectionStatus);

    /**
     * 根据同步状态查询
     *
     * @param syncStatus 同步状态
     * @return 数据源配置列表
     */
    List<TcDataSourceConfig> getBySyncStatus(String syncStatus);

    /**
     * 获取同步失败的数据源列表
     *
     * @return 数据源列表
     */
    List<TcDataSourceConfig> getSyncFailedSources();

    /**
     * 测试数据源连接
     *
     * @param dataSourceConfig 数据源配置信息
     * @return 测试结果
     */
    java.util.Map<String, Object> testConnection(TcDataSourceConfig dataSourceConfig);

    /**
     * 更新连接状态
     *
     * @param id 主键ID
     * @param connectionStatus 连接状态
     * @param operator 操作人
     * @return 操作结果
     */
    int updateConnectionStatus(String id, String connectionStatus, String operator);

    /**
     * 更新同步状态
     *
     * @param id 主键ID
     * @param syncStatus 同步状态
     * @param operator 操作人
     * @return 操作结果
     */
    int updateSyncStatus(String id, String syncStatus, String operator);

    /**
     * 更新状态
     *
     * @param id 主键ID
     * @param status 状态
     * @param operator 操作人
     * @return 操作结果
     */
    int updateStatus(String id, String status, String operator);

    /**
     * 同步数据源数据
     *
     * @param id 数据源ID
     * @param syncType 同步类型(FULL-全量, INCREMENTAL-增量)
     * @param operator 操作人
     * @return 同步结果
     */
    boolean syncDataSource(String id, String syncType, String operator);

    /**
     * 批量同步数据源
     *
     * @param sourceIds 数据源ID列表
     * @param syncType 同步类型
     * @param operator 操作人
     * @return 同步结果
     */
    boolean batchSyncDataSources(List<String> sourceIds, String syncType, String operator);

    /**
     * 更新启用状态
     *
     * @param id 主键ID
     * @param isEnabled 是否启用(Y/N)
     * @param operator 操作人
     * @return 操作结果
     */
    void updateEnabledStatus(String id, String isEnabled, String operator);

    /**
     * 获取需要同步的数据源列表
     *
     * @return 数据源列表
     */
    List<TcDataSourceConfig> getSyncRequiredSources();

    /**
     * 获取所有数据源类型
     *
     * @return 数据源类型列表
     */
    List<String> getAllSourceTypes();

    /**
     * 获取所有连接状态
     *
     * @return 连接状态列表
     */
    List<String> getAllConnectionStatuses();

    /**
     * 获取所有同步状态
     *
     * @return 同步状态列表
     */
    List<String> getAllSyncStatuses();

    /**
     * 获取统计信息
     *
     * @return 统计信息
     */
    java.util.Map<String, Object> getStatistics();
}
