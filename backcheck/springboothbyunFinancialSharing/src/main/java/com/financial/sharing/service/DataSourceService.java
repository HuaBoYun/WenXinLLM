package com.financial.sharing.service;

import com.financial.sharing.dto.*;
import com.financial.sharing.vo.param.DataSourceBatchParam;
import com.financial.sharing.vo.param.DataSourceImportParam;
import com.financial.sharing.vo.param.DataSourceExportParam;
import com.hbfk.util.JsonBean;

import java.util.List;
import java.util.Map;

/**
 * 数据源服务接口
 */
public interface DataSourceService {

    /**
     * 保存数据源配置
     */
    Long saveDataSourceConfig(DataSourceConfigParam param);

    /**
     * 更新数据源配置
     */
    void updateDataSourceConfig(Long configId, DataSourceConfigParam param);

    /**
     * 获取数据源配置列表
     */
    Map<String, Object> getDataSourceConfigList(DataSourceQueryParam param);

    /**
     * 获取数据源配置详情
     */
    JsonBean getDataSourceConfig(Long configId);

    /**
     * 删除数据源配置
     */
    void deleteDataSourceConfig(Long configId);

    /**
     * 测试数据源连接
     */
    Map<String, Object> testDataSource(DataSourceTestParam param);

    /**
     * 预览数据源数据
     */
    Map<String, Object> previewDataSource(DataSourcePreviewParam param);

    /**
     * 批量操作数据源配置
     */
    Map<String, Object> batchOperationDataSource(DataSourceBatchParam param);

    /**
     * 获取支持的数据源类型
     */
    List<Map<String, Object>> getDataSourceTypes();

    /**
     * 导入数据源配置
     */
    Map<String, Object> importDataSourceConfig(DataSourceImportParam param);

    /**
     * 导出数据源配置
     */
    byte[] exportDataSourceConfig(DataSourceExportParam param);

    /**
     * 验证数据源配置
     */
    Map<String, String> validateDataSourceConfig(DataSourceConfigParam param);

    /**
     * 获取数据源字段信息
     */
    List<Map<String, Object>> getDataSourceFields(Long configId);

    /**
     * 同步数据源数据
     */
    String syncDataSourceData(Long configId, Map<String, Object> syncOptions);

    /**
     * 获取数据源统计信息
     */
    Map<String, Object> getDataSourceStatistics(Long configId);
}