package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetTemplateCategory;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 预算模板分类 Mapper 接口
 *
 * @author AI Agent
 * @date 2026-04-01
 */
@Mapper
public interface BudgetTemplateCategoryMapper extends BaseMapper<BudgetTemplateCategory> {

    /**
     * 查询分类树
     * @return 分类树
     */
    @Select("SELECT CATEGORY_ID, CATEGORY_CODE, CATEGORY_NAME, PARENT_ID, CATEGORY_LEVEL, " +
            "CATEGORY_PATH, SORT_ORDER, CATEGORY_DESCRIPTION, IS_ENABLED, TEMPLATE_COUNT " +
            "FROM TBL_BUDGET_TEMPLATE_CATEGORY " +
            "WHERE DEL_FLAG = 0 AND IS_ENABLED = 1 " +
            "ORDER BY CATEGORY_LEVEL, SORT_ORDER")
    List<BudgetTemplateCategory> selectCategoryTree();

    /**
     * 查询分类列表
     * @return 分类列表
     */
    @Select("SELECT CATEGORY_ID, CATEGORY_CODE, CATEGORY_NAME, PARENT_ID, CATEGORY_LEVEL, " +
            "CATEGORY_PATH, SORT_ORDER, CATEGORY_DESCRIPTION, IS_ENABLED, TEMPLATE_COUNT " +
            "FROM TBL_BUDGET_TEMPLATE_CATEGORY " +
            "WHERE DEL_FLAG = 0 AND IS_ENABLED = 1 " +
            "ORDER BY CATEGORY_LEVEL, SORT_ORDER")
    List<BudgetTemplateCategory> selectCategories();

    /**
     * 更新模板数量
     * @param categoryId 分类ID
     * @param count 模板数量
     */
    @Select("UPDATE TBL_BUDGET_TEMPLATE_CATEGORY SET TEMPLATE_COUNT = #{count}, UPDATE_TIME = SYSDATE " +
            "WHERE CATEGORY_ID = #{categoryId}")
    void updateTemplateCount(String categoryId, Integer count);
}
