package com.financial.sharing.budgetPlanning.service;

import com.financial.sharing.budgetPlanning.dto.BudgetFormQueryParam;
import com.financial.sharing.budgetPlanning.entity.TblBudgetForm;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * 预算表单配置Service接口
 * 
 * @author hbyun
 * @date 2026-01-30
 */
public interface BudgetFormService {

    /**
     * 查询预算表单配置列表(分页)
     * 
     * @param param 查询参数
     * @return 分页结果
     */
    PageInfo<TblBudgetForm> getFormList(BudgetFormQueryParam param);

    /**
     * 查询预算表单配置列表(不分页)
     * 
     * @param param 查询参数
     * @return 预算表单配置列表
     */
    List<TblBudgetForm> getFormListNoPage(BudgetFormQueryParam param);

    /**
     * 根据ID查询预算表单配置
     * 
     * @param formId 表单ID
     * @return 预算表单配置
     */
    TblBudgetForm getFormById(String formId);

    /**
     * 新增预算表单配置
     * 
     * @param form 预算表单配置
     */
    void addForm(TblBudgetForm form);

    /**
     * 修改预算表单配置
     * 
     * @param form 预算表单配置
     */
    void updateForm(TblBudgetForm form);

    /**
     * 删除预算表单配置
     * 
     * @param formId 表单ID
     */
    void deleteForm(String formId);

    /**
     * 批量删除预算表单配置
     * 
     * @param formIds 表单ID列表
     */
    void batchDeleteForm(List<String> formIds);

    /**
     * 复制预算表单配置
     * 
     * @param formId 源表单ID
     * @param newFormCode 新表单编码
     * @param newFormName 新表单名称
     * @return 新表单ID
     */
    String copyForm(String formId, String newFormCode, String newFormName);

    /**
     * 启用预算表单配置
     * 
     * @param formId 表单ID
     */
    void enableForm(String formId);

    /**
     * 停用预算表单配置
     * 
     * @param formId 表单ID
     */
    void disableForm(String formId);
}

