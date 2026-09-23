package com.management.accountant.oracle.service.integration;

import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.oracle.entity.integration.BudgetWebServiceIntegration;

import java.util.List;

/**
 * 预算Web服务集成Service
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
public interface BudgetWebServiceIntegrationService extends IService<BudgetWebServiceIntegration> {

    /**
     * 根据Web服务类型查询集成列表
     * 
     * @param wsType Web服务类型
     * @return 集成列表
     */
    List<BudgetWebServiceIntegration> listByWsType(String wsType);

    /**
     * 根据认证方式查询集成列表
     * 
     * @param authType 认证方式
     * @return 集成列表
     */
    List<BudgetWebServiceIntegration> listByAuthType(String authType);

    /**
     * 根据调用频率查询集成列表
     * 
     * @param callFrequency 调用频率
     * @return 集成列表
     */
    List<BudgetWebServiceIntegration> listByCallFrequency(String callFrequency);

    /**
     * 查询启用的Web服务集成列表
     * 
     * @return 集成列表
     */
    List<BudgetWebServiceIntegration> listEnabled();

    /**
     * 调用Web服务
     * 
     * @param wsId Web服务集成ID
     * @param params 请求参数
     * @return 调用结果
     */
    String callWebService(String wsId, String params);

    /**
     * 测试Web服务连接
     * 
     * @param wsId Web服务集成ID
     * @return 测试结果
     */
    boolean testConnection(String wsId);

    /**
     * 更新调用统计
     * 
     * @param wsId Web服务集成ID
     * @param success 是否成功
     * @param responseTime 响应时间
     * @return 是否更新成功
     */
    boolean updateCallStatistics(String wsId, boolean success, Integer responseTime);

    /**
     * 批量启用/禁用Web服务集成
     * 
     * @param wsIds Web服务集成ID列表
     * @param enabled 是否启用
     * @return 是否操作成功
     */
    boolean batchUpdateEnabled(List<String> wsIds, boolean enabled);
}

