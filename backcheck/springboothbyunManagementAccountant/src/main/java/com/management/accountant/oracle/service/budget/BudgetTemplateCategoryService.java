package com.management.accountant.oracle.service.budget;

import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.oracle.entity.budget.BudgetTemplateCategory;

import java.util.List;
import java.util.Map;

/**
 * 预算模板分类 Service 接口
 *
 * @author AI Agent
 * @date 2026-04-01
 */
public interface BudgetTemplateCategoryService extends IService<BudgetTemplateCategory> {

    /**
     * 查询分类树
     * @return 分类树
     */
    List<Map<String, Object>> getCategoryTree();

    /**
     * 查询分类列表
     * @return 分类列表
     */
    List<Map<String, Object>> getCategories();

    /**
     * 更新模板数量
     * @param categoryId 分类ID
     */
    void updateTemplateCount(String categoryId);
}
