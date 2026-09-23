package com.management.accountant.service;

import com.management.accountant.oracle.entity.budget.BudgetFormula;


import java.util.List;
import java.util.Map;

/**
 * 预算公式Service接口
 * 
 * @description 预算公式业务接口
 * @author AI Assistant
 * @date 2025-01-04
 */
public interface BudgetFormulaService {

    /**
     * 创建公式
     * 
     * @param formula 公式对象
     * @return 创建后的公式对象
     */
    BudgetFormula create(BudgetFormula formula);

    /**
     * 根据ID查询公式
     * 
     * @param formulaId 公式ID
     * @return 公式对象
     */
    BudgetFormula getById(String formulaId);

    /**
     * 更新公式
     * 
     * @param formula 公式对象
     */
    void update(BudgetFormula formula);

    /**
     * 删除公式
     * 
     * @param formulaId 公式ID
     */
    void delete(String formulaId);

    /**
     * 分页查询公式列表
     * 
     * @param params 查询参数
     * @return 分页结果
     */
    Map<String, Object> getPage(Map<String, Object> params);

    /**
     * 验证公式
     * 
     * @param params 验证参数
     * @return 验证结果
     */
    Map<String, Object> validateFormula(Map<String, Object> params);

    /**
     * 计算公式
     * 
     * @param params 计算参数
     * @return 计算结果
     */
    Map<String, Object> calculateFormula(Map<String, Object> params);

    /**
     * 批量删除公式
     * 
     * @param ids 公式ID列表
     */
    void batchDelete(List<String> ids);

    /**
     * 根据类型查询公式列表
     * 
     * @param formulaType 公式类型
     * @return 公式列表
     */
    List<BudgetFormula> getByType(String formulaType);

    /**
     * 获取启用的公式列表
     *
     * @return 公式列表
     */
    List<BudgetFormula> getEnabledFormulas();

    /**
     * 获取公式分类树
     */
    List<Map<String, Object>> getCategoryTree();

    /**
     * 获取公式分类列表
     */
    List<Map<String, Object>> getCategories();

    /**
     * 验证单个公式
     */
    Map<String, Object> validateById(String formulaId);

    /**
     * 批量验证公式
     */
    Map<String, Object> batchValidate(List<String> ids);

    /**
     * 导出公式
     */
    Map<String, Object> exportFormulas(Map<String, Object> params);

    /**
     * 导出单个公式
     */
    Map<String, Object> exportById(String formulaId);
    Map<String, Object> getStats();

    /**
     * 导入公式
     */
    Map<String, Object> importFormulas(List<BudgetFormula> formulas);
}

