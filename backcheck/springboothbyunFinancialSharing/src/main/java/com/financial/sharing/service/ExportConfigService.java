package com.financial.sharing.service;

import com.financial.sharing.enums.ExportFormat;

import java.util.List;
import java.util.Map;

/**
 * 导出配置管理服务接口
 */
public interface ExportConfigService {

    /**
     * 获取支持的导出格式列表
     */
    List<ExportFormat> getSupportedFormats();

    /**
     * 获取模块的默认导出格式
     */
    ExportFormat getDefaultFormat(String module);

    /**
     * 设置模块的默认导出格式
     */
    void setDefaultFormat(String module, ExportFormat format);

    /**
     * 获取导出格式配置
     */
    Map<String, Object> getFormatConfig(ExportFormat format);

    /**
     * 设置导出格式配置
     */
    void setFormatConfig(ExportFormat format, Map<String, Object> config);

    /**
     * 获取用户的导出偏好设置
     */
    Map<String, Object> getUserExportPreferences(Long userId);

    /**
     * 设置用户的导出偏好
     */
    void setUserExportPreferences(Long userId, Map<String, Object> preferences);

    /**
     * 获取模块导出模板配置
     */
    Map<String, Object> getExportTemplate(String module, ExportFormat format);

    /**
     * 保存模块导出模板配置
     */
    void saveExportTemplate(String module, ExportFormat format, Map<String, Object> template);

    /**
     * 检查模块是否支持指定格式
     */
    boolean isFormatSupported(String module, ExportFormat format);

    /**
     * 获取导出权限配置
     */
    Map<String, Object> getExportPermissionConfig(Long userId);

    /**
     * 获取导出队列配置
     */
    Map<String, Object> getExportQueueConfig();

    /**
     * 获取导出缓存配置
     */
    Map<String, Object> getExportCacheConfig();
}