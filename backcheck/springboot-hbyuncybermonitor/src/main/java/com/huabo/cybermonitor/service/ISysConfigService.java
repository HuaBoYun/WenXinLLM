package com.huabo.cybermonitor.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.huabo.cybermonitor.entity.SysConfig;
import com.huabo.cybermonitor.vo.SysConfigQueryVO;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * 系统配置服务接口
 *
 * @author system
 * @since 2024-01-01
 */
public interface ISysConfigService extends IService<SysConfig> {

    /**
     * 分页查询系统配置列表
     *
     * @param queryVO 查询条件
     * @return 分页结果
     */
    IPage<SysConfig> getConfigList(SysConfigQueryVO queryVO);

    /**
     * 根据配置ID获取配置详情
     *
     * @param configId 配置ID
     * @return 配置详情
     */
    SysConfig getConfigDetail(String configId);

    /**
     * 根据配置键获取配置值
     *
     * @param configKey 配置键
     * @return 配置值
     */
    String getConfigValue(String configKey);

    /**
     * 根据配置键获取配置值（带默认值）
     *
     * @param configKey    配置键
     * @param defaultValue 默认值
     * @return 配置值
     */
    String getConfigValue(String configKey, String defaultValue);

    /**
     * 根据配置键获取整数配置值
     *
     * @param configKey 配置键
     * @return 配置值
     */
    Integer getIntConfigValue(String configKey);

    /**
     * 根据配置键获取整数配置值（带默认值）
     *
     * @param configKey    配置键
     * @param defaultValue 默认值
     * @return 配置值
     */
    Integer getIntConfigValue(String configKey, Integer defaultValue);

    /**
     * 根据配置键获取布尔配置值
     *
     * @param configKey 配置键
     * @return 配置值
     */
    Boolean getBooleanConfigValue(String configKey);

    /**
     * 根据配置键获取布尔配置值（带默认值）
     *
     * @param configKey    配置键
     * @param defaultValue 默认值
     * @return 配置值
     */
    Boolean getBooleanConfigValue(String configKey, Boolean defaultValue);

    /**
     * 根据配置分组查询配置列表
     *
     * @param configGroup 配置分组
     * @return 配置列表
     */
    List<SysConfig> getConfigsByGroup(String configGroup);

    /**
     * 根据配置类型查询配置列表
     *
     * @param configType 配置类型
     * @return 配置列表
     */
    List<SysConfig> getConfigsByType(String configType);

    /**
     * 查询启用的配置列表
     *
     * @return 启用的配置列表
     */
    List<SysConfig> getEnabledConfigs();

    /**
     * 查询系统配置列表
     *
     * @return 系统配置列表
     */
    List<SysConfig> getSystemConfigs();

    /**
     * 新增系统配置
     *
     * @param config 配置信息
     * @return 是否成功
     */
    boolean addConfig(SysConfig config);

    /**
     * 更新系统配置
     *
     * @param config 配置信息
     * @return 是否成功
     */
    boolean updateConfig(SysConfig config);

    /**
     * 更新配置值
     *
     * @param configKey   配置键
     * @param configValue 配置值
     * @return 是否成功
     */
    boolean updateConfigValue(String configKey, String configValue);

    /**
     * 批量更新配置值
     *
     * @param configMap 配置键值对
     * @return 是否成功
     */
    boolean batchUpdateConfigValue(Map<String, String> configMap);

    /**
     * 删除系统配置
     *
     * @param configId 配置ID
     * @return 是否成功
     */
    boolean deleteConfig(String configId);

    /**
     * 批量删除系统配置
     *
     * @param configIds 配置ID列表
     * @return 是否成功
     */
    boolean batchDeleteConfig(List<String> configIds);

    /**
     * 启用配置
     *
     * @param configId 配置ID
     * @return 是否成功
     */
    boolean enableConfig(String configId);

    /**
     * 禁用配置
     *
     * @param configId 配置ID
     * @return 是否成功
     */
    boolean disableConfig(String configId);

    /**
     * 批量启用配置
     *
     * @param configIds 配置ID列表
     * @return 是否成功
     */
    boolean batchEnableConfig(List<String> configIds);

    /**
     * 批量禁用配置
     *
     * @param configIds 配置ID列表
     * @return 是否成功
     */
    boolean batchDisableConfig(List<String> configIds);

    /**
     * 验证配置键是否重复
     *
     * @param configKey 配置键
     * @param excludeId 排除的配置ID
     * @return 是否重复
     */
    boolean validateConfigKey(String configKey, String excludeId);

    /**
     * 验证配置值格式
     *
     * @param configType  配置类型
     * @param configValue 配置值
     * @return 验证结果
     */
    Map<String, Object> validateConfigValue(String configType, String configValue);

    /**
     * 获取数据采集相关配置
     *
     * @return 数据采集配置列表
     */
    List<SysConfig> getDataCollectionConfigs();

    /**
     * 获取通知相关配置
     *
     * @return 通知配置列表
     */
    List<SysConfig> getNotificationConfigs();

    /**
     * 获取报表相关配置
     *
     * @return 报表配置列表
     */
    List<SysConfig> getReportConfigs();

    /**
     * 获取集成相关配置
     *
     * @return 集成配置列表
     */
    List<SysConfig> getIntegrationConfigs();

    /**
     * 重置配置为默认值
     *
     * @param configId 配置ID
     * @return 是否成功
     */
    boolean resetConfigToDefault(String configId);

    /**
     * 批量重置配置为默认值
     *
     * @param configIds 配置ID列表
     * @return 是否成功
     */
    boolean batchResetConfigToDefault(List<String> configIds);

    /**
     * 导出配置列表
     *
     * @param queryVO  查询条件
     * @param response HTTP响应
     */
    void exportConfigList(SysConfigQueryVO queryVO, HttpServletResponse response);

    /**
     * 获取配置类型标签
     *
     * @param configType 配置类型
     * @return 类型标签
     */
    String getConfigTypeLabel(String configType);

    /**
     * 获取配置分组标签
     *
     * @param configGroup 配置分组
     * @return 分组标签
     */
    String getConfigGroupLabel(String configGroup);

    /**
     * 刷新配置缓存
     *
     * @return 是否成功
     */
    boolean refreshConfigCache();

    /**
     * 获取配置缓存信息
     *
     * @return 缓存信息
     */
    Map<String, Object> getConfigCacheInfo();

    /**
     * 清空配置缓存
     *
     * @return 是否成功
     */
    boolean clearConfigCache();

    /**
     * 获取配置变更历史
     *
     * @param configKey 配置键
     * @return 变更历史列表
     */
    List<Map<String, Object>> getConfigChangeHistory(String configKey);

    /**
     * 备份配置
     *
     * @return 备份结果
     */
    Map<String, Object> backupConfigs();

    /**
     * 恢复配置
     *
     * @param backupData 备份数据
     * @return 恢复结果
     */
    Map<String, Object> restoreConfigs(String backupData);

    /**
     * 获取配置统计信息
     *
     * @return 统计信息
     */
    Map<String, Object> getConfigStatistics();

    /**
     * 获取配置分组分布统计
     *
     * @return 分组分布统计
     */
    List<Map<String, Object>> getConfigGroupDistribution();

    /**
     * 获取配置类型分布统计
     *
     * @return 类型分布统计
     */
    List<Map<String, Object>> getConfigTypeDistribution();

    /**
     * 检查配置健康状态
     *
     * @return 健康状态报告
     */
    Map<String, Object> checkConfigHealth();
}
