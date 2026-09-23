package com.management.accountant.service;

import com.management.accountant.oracle.entity.integration.BudgetDataMapping;
import com.management.accountant.util.PageResult;

import java.util.Map;

/**
 * 预算数据映射Service接口
 * 
 * @description 预算数据映射业务接口
 * @author AI Assistant
 * @date 2025-01-04
 */
public interface BudgetDataMappingService {

    /**
     * 创建数据映射
     * 
     * @param mapping 映射对象
     * @return 创建后的映射对象
     */
    BudgetDataMapping create(BudgetDataMapping mapping);

    /**
     * 根据ID查询映射
     * 
     * @param mappingId 映射ID
     * @return 映射对象
     */
    BudgetDataMapping getById(String mappingId);

    /**
     * 更新数据映射
     * 
     * @param mapping 映射对象
     */
    void update(BudgetDataMapping mapping);

    /**
     * 删除数据映射
     * 
     * @param mappingId 映射ID
     */
    void delete(String mappingId);

    /**
     * 分页查询映射列表
     * 
     * @param params 查询参数
     * @return 分页结果
     */
    PageResult<BudgetDataMapping> getPage(Map<String, Object> params);

    /**
     * 执行映射
     * 
     * @param params 执行参数
     * @return 执行结果
     */
    Map<String, Object> executeMapping(Map<String, Object> params);
}

