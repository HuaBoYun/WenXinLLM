package com.financial.sharing.budgetPlanning.service;

import com.financial.sharing.budgetPlanning.dto.BudgetModelQueryParam;
import com.financial.sharing.budgetPlanning.entity.TblBudgetModel;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 预算模型Service接口
 * 
 * @author hbyun
 * @date 2026-01-30
 */
public interface BudgetModelService {

    /**
     * 查询预算模型列表(分页)
     * 
     * @param param 查询参数
     * @return 分页结果
     */
    PageInfo<TblBudgetModel> getModelList(BudgetModelQueryParam param);

    /**
     * 查询预算模型列表(不分页)
     * 
     * @param param 查询参数
     * @return 预算模型列表
     */
    List<TblBudgetModel> getModelListNoPage(BudgetModelQueryParam param);

    /**
     * 根据ID查询预算模型
     * 
     * @param modelId 模型ID
     * @return 预算模型
     */
    TblBudgetModel getModelById(String modelId);

    /**
     * 新增预算模型
     * 
     * @param model 预算模型
     */
    void addModel(TblBudgetModel model);

    /**
     * 修改预算模型
     * 
     * @param model 预算模型
     */
    void updateModel(TblBudgetModel model);

    /**
     * 删除预算模型
     * 
     * @param modelId 模型ID
     */
    void deleteModel(String modelId);

    /**
     * 批量删除预算模型
     * 
     * @param modelIds 模型ID列表
     */
    void batchDeleteModel(List<String> modelIds);

    /**
     * 复制预算模型
     * 
     * @param modelId 源模型ID
     * @param newModelCode 新模型编码
     * @param newModelName 新模型名称
     * @return 新模型ID
     */
    String copyModel(String modelId, String newModelCode, String newModelName);

    /**
     * 启用预算模型
     * 
     * @param modelId 模型ID
     */
    void enableModel(String modelId);

    /**
     * 停用预算模型
     * 
     * @param modelId 模型ID
     */
    void disableModel(String modelId);
}

