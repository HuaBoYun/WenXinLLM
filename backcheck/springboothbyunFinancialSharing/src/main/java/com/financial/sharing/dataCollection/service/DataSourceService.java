package com.financial.sharing.dataCollection.service;

import com.financial.sharing.dataCollection.dto.DataSourceQueryParam;
import com.financial.sharing.dataCollection.entity.TblDataSource;
import com.financial.sharing.util.MyJsonBean;

/**
 * 数据源配置Service接口
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
public interface DataSourceService {

    /**
     * 分页查询数据源配置
     *
     * @param param 查询参数
     * @return 分页结果
     */
    MyJsonBean queryPage(DataSourceQueryParam param);

    /**
     * 根据ID查询数据源配置
     *
     * @param sourceId 数据源ID
     * @param orgId 组织ID
     * @return 数据源配置
     */
    MyJsonBean queryById(String sourceId, String orgId);

    /**
     * 保存数据源配置
     *
     * @param dataSource 数据源配置
     * @return 操作结果
     */
    MyJsonBean saveDataSource(TblDataSource dataSource);

    /**
     * 删除数据源配置
     *
     * @param sourceId 数据源ID
     * @param orgId 组织ID
     * @return 操作结果
     */
    MyJsonBean deleteDataSource(String sourceId, String orgId);

    /**
     * 启用/禁用数据源
     *
     * @param sourceId 数据源ID
     * @param isEnabled 是否启用
     * @param orgId 组织ID
     * @return 操作结果
     */
    MyJsonBean toggleEnabled(String sourceId, String isEnabled, String orgId);

    /**
     * 测试数据源连接
     *
     * @param sourceId 数据源ID
     * @param orgId 组织ID
     * @return 测试结果
     */
    MyJsonBean testConnection(String sourceId, String orgId);
}

