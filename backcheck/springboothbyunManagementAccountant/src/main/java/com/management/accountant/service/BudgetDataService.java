package com.management.accountant.service;

import com.management.accountant.oracle.entity.budget.BudgetData;
import com.management.accountant.util.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 预算数据录入Service接口
 * 
 * @description 预算数据录入业务接口
 * @author AI Assistant
 * @date 2025-01-04
 */
public interface BudgetDataService {

    /**
     * 创建预算数据
     * 
     * @param data 预算数据对象
     * @return 创建后的预算数据
     */
    BudgetData create(BudgetData data);

    /**
     * 根据ID查询预算数据
     * 
     * @param dataId 数据ID
     * @return 预算数据对象
     */
    BudgetData getById(String dataId);

    /**
     * 更新预算数据
     * 
     * @param data 预算数据对象
     * @return 更新后的预算数据
     */
    BudgetData update(BudgetData data);

    /**
     * 删除预算数据
     * 
     * @param dataId 数据ID
     */
    void delete(String dataId);

    /**
     * 分页查询预算数据列表
     * 
     * @param params 查询参数
     * @return 分页结果
     */
    PageResult<BudgetData> getPage(Map<String, Object> params);

    /**
     * 批量保存预算数据
     * 
     * @param dataList 预算数据列表
     */
    void batchSave(List<BudgetData> dataList);

    /**
     * 批量删除预算数据
     * 
     * @param ids ID列表
     */
    void batchDelete(List<String> ids);

    /**
     * 提交预算数据
     * 
     * @param dataId 数据ID
     */
    void submit(String dataId);

    /**
     * 批量提交预算数据
     * 
     * @param ids ID列表
     */
    void batchSubmit(List<String> ids);

    /**
     * 导入预算数据
     * 
     * @param dataList 数据列表
     * @return 导入结果
     */
    Map<String, Object> importData(List<Map<String, Object>> dataList);

    /**
     * 校验预算数据
     * 
     * @param data 预算数据
     * @return 校验结果
     */
    Map<String, Object> validate(BudgetData data);

    /**
     * 计算差异
     *
     * @param dataId 数据ID
     */
    void calculateVariance(String dataId);

    /**
     * 获取数据变更历史
     * @param dataId 数据ID
     */
    List<Map<String, Object>> getHistory(String dataId);

    /**
     * 获取审计日志
     * @param dataId 数据ID
     */
    List<Map<String, Object>> getAuditLog(String dataId);
}

