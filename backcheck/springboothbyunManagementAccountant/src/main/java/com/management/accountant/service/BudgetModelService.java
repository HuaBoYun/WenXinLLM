package com.management.accountant.service;

import com.management.accountant.oracle.entity.budget.BudgetModel;
import com.management.accountant.util.PageResult;

import java.util.List;
import java.util.Map;

/**
 * 预算模型Service接口
 * 
 * @description 预算模型业务接口
 * @author AI Assistant
 * @date 2025-01-04
 */
public interface BudgetModelService {

    /**
     * 创建预算模型
     * 
     * @param model 模型对象
     * @return 创建后的模型对象
     */
    BudgetModel create(BudgetModel model);

    /**
     * 根据ID查询模型
     * 
     * @param modelId 模型ID
     * @return 模型对象
     */
    BudgetModel getById(String modelId);

    /**
     * 更新预算模型
     * 
     * @param model 模型对象
     */
    void update(BudgetModel model);

    /**
     * 删除预算模型
     * 
     * @param modelId 模型ID
     */
    void delete(String modelId);

    /**
     * 分页查询模型列表
     * 
     * @param params 查询参数
     * @return 分页结果
     */
    PageResult<BudgetModel> getPage(Map<String, Object> params);

    /**
     * 激活模型
     * 
     * @param modelId 模型ID
     */
    void activate(String modelId);

    /**
     * 停用模型
     * 
     * @param modelId 模型ID
     */
    void deactivate(String modelId);

    /**
     * 复制模型
     * 
     * @param modelId 模型ID
     * @param newModelName 新模型名称
     * @return 复制后的模型对象
     */
    BudgetModel copy(String modelId, String newModelName);

    /**
     * 归档模型
     * 
     * @param modelId 模型ID
     */
    void archive(String modelId);

    /**
     * 批量删除模型
     * 
     * @param ids 模型ID列表
     */
    void batchDelete(List<String> ids);

    /**
     * 获取模型统计信息
     * 
     * @return 统计信息
     */
    Map<String, Object> getStatistics();

    /**
     * 获取各模型类型的统计数量
     * @return 类型统计信息
     */
    Map<String, Object> getTypeStatistics();

    /**
     * 根据类型查询模型列表
     * 
     * @param modelType 模型类型
     * @return 模型列表
     */
    List<BudgetModel> getByType(String modelType);

    /**
     * 获取生效的模型列表
     * 
     * @return 模型列表
     */
    List<BudgetModel> getActiveModels();

    /**
     * 校验模型配置
     * 
     * @param modelId 模型ID
     * @return 校验结果
     */
    Map<String, Object> validateConfig(String modelId);

    /**
     * 应用模型到预算任务
     * 
     * @param modelId 模型ID
     * @param taskId 任务ID
     * @return 应用结果
     */
    Map<String, Object> applyToTask(String modelId, String taskId);
}

