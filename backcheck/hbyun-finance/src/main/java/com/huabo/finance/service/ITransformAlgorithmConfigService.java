package com.huabo.finance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.util.JsonBean;
import com.huabo.finance.entity.TransformAlgorithmConfig;

import java.util.List;

/**
 * 算法配置Service接口
 * 
 * @author 华博云开发团队
 * @since 2025-01-21
 */
public interface ITransformAlgorithmConfigService extends IService<TransformAlgorithmConfig> {

    /**
     * 保存算法配置
     *
     * @param configs 算法配置列表
     * @param transformTaskId 转化任务ID
     * @param createUser 创建人
     * @return 操作结果
     */
    JsonBean saveConfigs(List<TransformAlgorithmConfig> configs, String transformTaskId, String createUser);

    /**
     * 查询算法配置列表
     *
     * @param transformTaskId 转化任务ID
     * @return 算法配置列表
     */
    JsonBean getConfigList(String transformTaskId);

    /**
     * 获取所有可用算法列表
     *
     * @return 算法列表
     */
    JsonBean getAvailableAlgorithms();

    /**
     * 删除算法配置
     *
     * @param configId 配置ID
     * @return 操作结果
     */
    JsonBean deleteConfig(String configId);
}

