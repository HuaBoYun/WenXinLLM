package com.financial.sharing.budgetControl.service;

import com.financial.sharing.budgetControl.dto.WarningConfigQueryParam;
import com.financial.sharing.budgetControl.entity.TblWarningConfig;
import com.financial.sharing.util.MyJsonBean;

/**
 * 预警配置Service接口
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
public interface WarningConfigService {

    /**
     * 分页查询预警配置
     *
     * @param param 查询参数
     * @return 分页结果
     */
    MyJsonBean queryPage(WarningConfigQueryParam param);

    /**
     * 根据ID查询预警配置
     *
     * @param configId 配置ID
     * @param orgId 组织ID
     * @return 预警配置
     */
    MyJsonBean queryById(String configId, String orgId);

    /**
     * 保存预警配置
     *
     * @param config 预警配置
     * @return 操作结果
     */
    MyJsonBean saveConfig(TblWarningConfig config);

    /**
     * 删除预警配置
     *
     * @param configId 配置ID
     * @param orgId 组织ID
     * @return 操作结果
     */
    MyJsonBean deleteConfig(String configId, String orgId);

    /**
     * 启用/禁用预警配置
     *
     * @param configId 配置ID
     * @param isEnabled 是否启用
     * @param orgId 组织ID
     * @return 操作结果
     */
    MyJsonBean toggleEnabled(String configId, String isEnabled, String orgId);

    /**
     * 测试预警配置
     *
     * @param configId 配置ID
     * @param orgId 组织ID
     * @return 测试结果
     */
    MyJsonBean testConfig(String configId, String orgId);

    /**
     * 发送预警消息
     *
     * @param configId 配置ID
     * @param message 预警消息
     * @param orgId 组织ID
     * @return 发送结果
     */
    MyJsonBean sendWarningMessage(String configId, String message, String orgId);
}

