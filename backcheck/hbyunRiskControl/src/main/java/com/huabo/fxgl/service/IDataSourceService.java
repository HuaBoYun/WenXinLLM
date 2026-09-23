package com.huabo.fxgl.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.fxgl.dto.DataSourceQueryDTO;
import com.huabo.fxgl.dto.DataSourceSaveDTO;
import com.huabo.fxgl.entity.TblDataSource;
import com.huabo.fxgl.vo.DataSourceVO;

import java.util.List;

/**
 * 数据源管理服务接口
 * 
 * @author 华博云
 * @since 2025-01-21
 */
public interface IDataSourceService extends IService<TblDataSource> {

    /**
     * 分页查询数据源列表
     * 
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    JsonBean getDataSourceList(DataSourceQueryDTO queryDTO);

    /**
     * 保存数据源配置
     *
     * @param saveDTO 保存参数
     * @param currentUser 当前用户
     * @return 保存结果
     */
    JsonBean saveDataSource(DataSourceSaveDTO saveDTO, String currentUser);

    /**
     * 删除数据源
     *
     * @param sourceId 数据源ID
     * @return 删除结果
     */
    JsonBean deleteDataSource(String sourceId);

    /**
     * 批量删除数据源
     *
     * @param sourceIds 数据源ID列表
     * @return 删除结果
     */
    JsonBean batchDeleteDataSource(List<String> sourceIds);

    /**
     * 获取数据源详情
     *
     * @param sourceId 数据源ID
     * @return 数据源详情
     */
    JsonBean getDataSourceDetail(String sourceId);

    /**
     * 测试数据源连接
     *
     * @param sourceId 数据源ID
     * @return 测试结果
     */
    JsonBean testDataSourceConnection(String sourceId);

    /**
     * 同步表结构
     *
     * @param sourceId 数据源ID
     * @return 同步结果
     */
    JsonBean syncTableStructure(String sourceId);

    /**
     * 获取活跃数据源列表
     *
     * @return 活跃数据源列表
     */
    JsonBean getActiveDataSources();

    /**
     * 获取数据源统计信息
     *
     * @return 统计信息
     */
    JsonBean getDataSourceStatistics();

    /**
     * 启用数据源
     *
     * @param sourceId 数据源ID
     * @return 操作结果
     */
    JsonBean enableDataSource(String sourceId);

    /**
     * 禁用数据源
     *
     * @param sourceId 数据源ID
     * @return 操作结果
     */
    JsonBean disableDataSource(String sourceId);

    /**
     * 验证数据源名称是否唯一
     * 
     * @param sourceName 数据源名称
     * @param sourceId 数据源ID(更新时传入)
     * @return 是否唯一
     */
    boolean isSourceNameUnique(String sourceName, String sourceId);

    /**
     * 构建连接URL
     * 
     * @param sourceType 数据源类型
     * @param hostIp 主机IP
     * @param port 端口
     * @param databaseName 数据库名
     * @return 连接URL
     */
    String buildConnectionUrl(String sourceType, String hostIp, Integer port, String databaseName);
}
