package com.huabo.fxgl.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.fxgl.entity.TblWarningConfig;

import java.util.List;

/**
 * 预警配置服务接口
 *
 * @author 华博云
 * @date 2025-09-30
 */
public interface IWarningConfigService extends IService<TblWarningConfig> {

    /**
     * 分页查询预警配置列表
     *
     * @param pageNum 页码
     * @param pageSize 页大小
     * @param configType 配置类型
     * @param isEnabled 是否启用
     * @return 分页结果
     */
    JsonBean getWarningConfigList(Integer pageNum, Integer pageSize, String configType, String isEnabled);

    /**
     * 保存预警配置
     *
     * @param warningConfig 预警配置
     * @param currentUser 当前用户
     * @return 保存结果
     */
    JsonBean saveWarningConfig(TblWarningConfig warningConfig, String currentUser);

    /**
     * 更新预警配置
     *
     * @param warningConfig 预警配置
     * @param currentUser 当前用户
     * @return 更新结果
     */
    JsonBean updateWarningConfig(TblWarningConfig warningConfig, String currentUser);

    /**
     * 删除预警配置
     *
     * @param configId 配置ID
     * @return 删除结果
     */
    JsonBean deleteWarningConfig(String configId);

    /**
     * 根据配置类型获取配置
     *
     * @param configType 配置类型
     * @return 配置列表
     */
    List<TblWarningConfig> getConfigByType(String configType);

    /**
     * 根据配置名称获取配置
     *
     * @param configName 配置名称
     * @return 配置信息
     */
    TblWarningConfig getConfigByName(String configName);

    /**
     * 获取所有启用的配置
     *
     * @return 配置列表
     */
    List<TblWarningConfig> getAllEnabledConfigs();

    /**
     * 更新配置状态
     *
     * @param configId 配置ID
     * @param isEnabled 是否启用
     * @param currentUser 当前用户
     * @return 更新结果
     */
    JsonBean updateConfigStatus(String configId, String isEnabled, String currentUser);

    /**
     * 批量更新配置状态
     *
     * @param configIds 配置ID列表
     * @param isEnabled 是否启用
     * @param currentUser 当前用户
     * @return 更新结果
     */
    JsonBean batchUpdateConfigStatus(List<String> configIds, String isEnabled, String currentUser);

    /**
     * 验证配置值格式
     *
     * @param configType 配置类型
     * @param configValue 配置值
     * @return 验证结果
     */
    JsonBean validateConfigValue(String configType, String configValue);

    /**
     * 获取配置值模板
     *
     * @param configType 配置类型
     * @return 配置模板
     */
    JsonBean getConfigTemplate(String configType);

    /**
     * 导出配置
     *
     * @param configType 配置类型
     * @return 导出结果
     */
    JsonBean exportConfigs(String configType);

    /**
     * 导入配置
     *
     * @param configData 配置数据
     * @param currentUser 当前用户
     * @return 导入结果
     */
    JsonBean importConfigs(String configData, String currentUser);
}
