package com.management.accountant.oracle.service.integration;

import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.oracle.entity.integration.BudgetApiIntegration;

import java.util.List;

/**
 * 预算API接口集成Service
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
public interface BudgetApiIntegrationService extends IService<BudgetApiIntegration> {

    /**
     * 根据API类型查询API集成列表
     * 
     * @param apiType API类型
     * @return API集成列表
     */
    List<BudgetApiIntegration> listByApiType(String apiType);

    /**
     * 根据API状态查询API集成列表
     * 
     * @param apiStatus API状态
     * @return API集成列表
     */
    List<BudgetApiIntegration> listByApiStatus(String apiStatus);

    /**
     * 根据认证方式查询API集成列表
     * 
     * @param authType 认证方式
     * @return API集成列表
     */
    List<BudgetApiIntegration> listByAuthType(String authType);

    /**
     * 查询启用的API集成列表
     * 
     * @return API集成列表
     */
    List<BudgetApiIntegration> listEnabled();

    /**
     * 调用API接口
     * 
     * @param apiId API集成ID
     * @param params 请求参数
     * @return 调用结果
     */
    String callApi(String apiId, String params);

    /**
     * 测试API连接
     * 
     * @param apiId API集成ID
     * @return 测试结果
     */
    boolean testConnection(String apiId);

    /**
     * 更新API调用统计
     * 
     * @param apiId API集成ID
     * @param success 是否成功
     * @param responseTime 响应时间
     * @return 是否更新成功
     */
    boolean updateCallStatistics(String apiId, boolean success, Integer responseTime);

    /**
     * 批量启用/禁用API集成
     * 
     * @param apiIds API集成ID列表
     * @param enabled 是否启用
     * @return 是否操作成功
     */
    boolean batchUpdateEnabled(List<String> apiIds, boolean enabled);
}

