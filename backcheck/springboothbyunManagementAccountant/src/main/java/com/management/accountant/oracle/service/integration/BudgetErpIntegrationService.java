package com.management.accountant.oracle.service.integration;

import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.oracle.entity.integration.BudgetErpIntegration;
import com.management.accountant.util.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 预算ERP系统集成Service
 *
 * @author AI Agent
 * @date 2025-12-31
 */
public interface BudgetErpIntegrationService extends IService<BudgetErpIntegration> {

    /**
     * 创建ERP集成
     *
     * @param erp ERP集成对象
     * @return 创建后的ERP集成对象
     */
    BudgetErpIntegration create(BudgetErpIntegration erp);

    /**
     * 删除ERP集成
     *
     * @param erpId ERP集成ID
     */
    void delete(String erpId);

    /**
     * 分页查询ERP集成
     *
     * @param params 查询参数
     * @return 分页结果
     */
    PageResult<BudgetErpIntegration> getPage(Map<String, Object> params);

    /**
     * 启用ERP集成
     *
     * @param erpId ERP集成ID
     */
    void enable(String erpId);

    /**
     * 禁用ERP集成
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
     * 根据ERP系统类型查询集成列表
     * 
     * @param erpType ERP系统类型
     * @return 集成列表
     */
    List<BudgetErpIntegration> listByErpType(String erpType);

    /**
     * 根据连接方式查询集成列表
     * 
     * @param connectionType 连接方式
     * @return 集成列表
     */
    List<BudgetErpIntegration> listByConnectionType(String connectionType);

    /**
     * 根据同步频率查询集成列表
     * 
     * @param syncFrequency 同步频率
     * @return 集成列表
     */
    List<BudgetErpIntegration> listBySyncFrequency(String syncFrequency);

    /**
     * 查询启用的ERP集成列表
     * 
     * @return 集成列表
     */
    List<BudgetErpIntegration> listEnabled();

    /**
     * 执行ERP数据同步
     * 
     * @param erpId ERP集成ID
     * @return 同步结果
     */
    boolean executeSync(String erpId);

    /**
     * 测试ERP连接
     * 
     * @param erpId ERP集成ID
     * @return 测试结果
     */
    boolean testConnection(String erpId);

    /**
     * 更新同步统计
     * 
     * @param erpId ERP集成ID
     * @param success 是否成功
     * @param recordCount 记录数
     * @return 是否更新成功
     */
    boolean updateSyncStatistics(String erpId, boolean success, Integer recordCount);

    /**
     * 批量启用/禁用ERP集成
     * 
     * @param erpIds ERP集成ID列表
     * @param enabled 是否启用
     * @return 是否操作成功
     */
    boolean batchUpdateEnabled(List<String> erpIds, boolean enabled);
}

