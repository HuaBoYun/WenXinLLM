package com.huabo.finance.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.huabo.finance.entity.CollectionTransformConfig;

/**
 * 采集任务转化配置服务接口
 * 
 * @author 华博云开发团队
 * @since 2025-01-21
 */
public interface CollectionTransformConfigService extends IService<CollectionTransformConfig> {

    /**
     * 保存转化配置
     */
    JsonBean saveTransformConfig(TblStaffUtil staff, CollectionTransformConfig config) throws Exception;

    /**
     * 根据采集任务ID获取转化配置
     */
    JsonBean getConfigByCollectionTaskId(String collectionTaskId) throws Exception;

    /**
     * 执行自动转化(采集完成后调用)
     */
    JsonBean executeAutoTransform(String collectionTaskId) throws Exception;

    /**
     * 更新执行状态
     */
    JsonBean updateExecuteStatus(String configId, String status) throws Exception;
}

