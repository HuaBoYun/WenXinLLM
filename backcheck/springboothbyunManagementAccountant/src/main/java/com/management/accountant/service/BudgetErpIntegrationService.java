package com.management.accountant.service;

import com.management.accountant.oracle.entity.integration.BudgetErpIntegration;
import com.management.accountant.util.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 预算ERP集成Service接口
 * 
 * @description 预算ERP集成业务接口
 * @author AI Assistant
 * @date 2025-01-04
 */
public interface BudgetErpIntegrationService {

    /**
     * 创建ERP集成
     * 
     * @param erp ERP集成对象
     * @return 创建后的ERP集成对象
     */
    BudgetErpIntegration create(BudgetErpIntegration erp);

    /**
     * 根据ID查询ERP集成
     * 
     * @param erpId ERP集成ID
     * @return ERP集成对象
     */
    BudgetErpIntegration getById(String erpId);

    /**
     * 更新ERP集成
     * 
     * @param erp ERP集成对象
     */
    void update(BudgetErpIntegration erp);

    /**
     * 删除ERP集成
     * 
     * @param erpId ERP集成ID
     */
    void delete(String erpId);

    /**
     * 分页查询ERP集成列表
     * 
     * @param params 查询参数
     * @return 分页结果
     */
    PageResult<BudgetErpIntegration> getPage(Map<String, Object> params);

    /**
     * 测试连接
     * 
     * @param erpId ERP集成ID
     * @return 测试结果
     */
    Map<String, Object> testConnection(String erpId);

    /**
     * 执行同步
     * 
     * @param erpId ERP集成ID
     * @return 同步结果
     */
    Map<String, Object> executeSync(String erpId);

    /**
     * 启用ERP集成
     * 
     * @param erpId ERP集成ID
     */
    void enable(String erpId);

    /**
     * 停用ERP集成
     * 
     * @param erpId ERP集成ID
     */
    void disable(String erpId);

    /**
     * 获取同步历史
     * 
     * @param erpId ERP集成ID
     * @return 同步历史列表
     */
    List<Map<String, Object>> getSyncHistory(String erpId);

    /**
     * 查询所有ERP集成列表
     *
     * @param erpType ERP类型（可选）
     * @return 集成列表
     */
    List<BudgetErpIntegration> getList(String erpType);

    /**
     * 获取ERP集成统计数据
     *
     * @return 统计数据
     */
    Map<String, Object> getStats();

    /**
     * 保存字段映射配置
     *
     * @param erpId ERP集成ID
     * @param mappings 映射配置JSON字符串
     */
    void saveFieldMappings(String erpId, String mappings);
}

