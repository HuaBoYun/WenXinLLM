package com.financial.sharing.oracle.mapper;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 财务配置Mapper接口
 */
public interface FinanceConfigMapper {
    
    /**
     * 查询配置列表
     */
    List<Map<String, Object>> selectConfigList(@Param("params") Map<String, Object> params);
    
    /**
     * 根据ID查询配置详情
     */
    Map<String, Object> selectConfigById(@Param("configId") String configId);
    
    /**
     * 插入配置
     */
    int insertConfig(@Param("config") Map<String, Object> config);
    
    /**
     * 更新配置
     */
    int updateConfig(@Param("config") Map<String, Object> config);
    
    /**
     * 删除配置
     */
    int deleteConfig(@Param("configId") String configId);

    // ==================== 财务版本信息管理相关方法 ====================

    /**
     * 查询财务版本信息分类列表
     */
    List<Map<String, Object>> selectCwbbxxTypeList();

    /**
     * 查询财务版本信息列表
     */
    List<Map<String, Object>> selectCwbbxxList(@Param("params") Map<String, Object> params);

    /**
     * 统计财务版本信息总数
     */
    int countCwbbxxList(@Param("params") Map<String, Object> params);

    /**
     * 插入财务版本信息
     */
    int insertCwbbxx(@Param("data") Map<String, Object> data);

    /**
     * 删除财务版本信息
     */
    int deleteCwbbxx(@Param("params") Map<String, Object> params);

    /**
     * 查询财务版本信息详情
     */
    Map<String, Object> selectCwbbxxDetail(@Param("params") Map<String, Object> params);

    // ==================== 数据源管理相关方法 ====================

    /**
     * 查询数据源列表
     */
    List<Map<String, Object>> selectDataSourceList(@Param("params") Map<String, Object> params);

    /**
     * 统计数据源总数
     */
    int countDataSourceList(@Param("params") Map<String, Object> params);

    /**
     * 查询数据源详情
     */
    Map<String, Object> selectDataSourceDetail(@Param("params") Map<String, Object> params);

    /**
     * 插入数据源
     */
    int insertDataSource(@Param("data") Map<String, Object> data);

    /**
     * 删除数据源
     */
    int deleteDataSource(@Param("params") Map<String, Object> params);

    /**
     * 测试数据源连接
     */
    int testDataSourceConnection(@Param("data") Map<String, Object> data);

    // ==================== 采集方案管理相关方法 ====================

    /**
     * 查询采集方案列表
     */
    List<Map<String, Object>> selectCjfaList(@Param("params") Map<String, Object> params);

    /**
     * 统计采集方案总数
     */
    int countCjfaList(@Param("params") Map<String, Object> params);

    /**
     * 查询采集方案详情
     */
    Map<String, Object> selectCjfaDetail(@Param("params") Map<String, Object> params);

    /**
     * 插入采集方案
     */
    int insertCjfa(@Param("data") Map<String, Object> data);

    /**
     * 删除采集方案
     */
    int deleteCjfa(@Param("params") Map<String, Object> params);

    // ==================== 采集SQL管理相关方法 ====================

    /**
     * 查询采集SQL新增详情
     */
    Map<String, Object> selectCjsqlAddDetail(@Param("params") Map<String, Object> params);

    /**
     * 查询采集SQL详情
     */
    Map<String, Object> selectCjsqlDetail(@Param("params") Map<String, Object> params);

    /**
     * 查询采集SQL列表
     */
    List<Map<String, Object>> selectCjsqlList(@Param("params") Map<String, Object> params);

    /**
     * 统计采集SQL总数
     */
    int countCjsqlList(@Param("params") Map<String, Object> params);

    /**
     * 插入采集SQL
     */
    int insertCjsql(@Param("data") Map<String, Object> data);

    /**
     * 删除采集SQL
     */
    int deleteCjsql(@Param("data") Map<String, Object> data);

    /**
     * 测试采集SQL执行
     */
    List<Map<String, Object>> testCjsqlExecution(@Param("data") Map<String, Object> data);

    /**
     * 执行采集SQL任务
     */
    int executeCjsqlTask(@Param("data") Map<String, Object> data);

    // ==================== 离线工具下载相关方法 ====================

    /**
     * 查询离线工具信息
     */
    Map<String, Object> selectOfflineToolInfo();
}