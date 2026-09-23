package com.management.accountant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.management.accountant.oracle.entity.budget.BudgetTemplate;
import com.management.accountant.oracle.entity.budget.BudgetTemplateCategory;
import com.management.accountant.oracle.mapper.budget.BudgetTemplateCategoryMapper;
import com.management.accountant.oracle.mapper.budget.BudgetTemplateMapper;
import com.management.accountant.oracle.service.budget.BudgetTemplateCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 预算模板分类 ServiceImpl
 *
 * @author AI Agent
 * @date 2026-04-01
 */
@Service
@Slf4j
public class BudgetTemplateCategoryServiceImpl extends ServiceImpl<BudgetTemplateCategoryMapper, BudgetTemplateCategory>
        implements BudgetTemplateCategoryService {

    @Resource
    private BudgetTemplateMapper budgetTemplateMapper;

    /**
     * 统计某个 categoryId 前缀下的有效模板数（CATEGORY_ID LIKE 'xxx%'）
     * 用于一级分类的汇总计数
     */
    private int countByPrefix(String prefix) {
        QueryWrapper<BudgetTemplate> w = new QueryWrapper<>();
        w.likeRight("CATEGORY_ID", prefix)
         .eq("DEL_FLAG", 0)
         .and(qw -> qw.isNull("TEMPLATE_STATUS").or().ne("TEMPLATE_STATUS", "DRAFT"));
        return budgetTemplateMapper.selectCount(w).intValue();
    }

    /**
     * 精确统计某个 categoryId 下的有效模板数
     * 用于二级分类的精确计数
     */
    private int countByExact(String categoryId) {
        QueryWrapper<BudgetTemplate> w = new QueryWrapper<>();
        w.eq("CATEGORY_ID", categoryId)
         .eq("DEL_FLAG", 0)
         .and(qw -> qw.isNull("TEMPLATE_STATUS").or().ne("TEMPLATE_STATUS", "DRAFT"));
        return budgetTemplateMapper.selectCount(w).intValue();
    }

    @Override
    public List<Map<String, Object>> getCategoryTree() {
        log.info("构建分类树，templateCount 从数据库实时统计");

        List<Map<String, Object>> tree = new ArrayList<>();

        // 一级分类：收入预算
        Map<String, Object> income = new HashMap<>();
        income.put("id", "CAT001");
        income.put("code", "INCOME");
        income.put("name", "收入预算");
        income.put("parentId", null);
        income.put("level", 1);
        income.put("path", "/CAT001");
        income.put("sortOrder", 1);
        income.put("description", "包含各类收入预算模板");
        income.put("enabled", true);
        income.put("templateCount", countByPrefix("CAT001"));

        // 收入预算的子分类
        List<Map<String, Object>> incomeChildren = new ArrayList<>();

        Map<String, Object> mainIncome = new HashMap<>();
        mainIncome.put("id", "CAT001-001");
        mainIncome.put("code", "MAIN_INCOME");
        mainIncome.put("name", "营业收入");
        mainIncome.put("parentId", "CAT001");
        mainIncome.put("level", 2);
        mainIncome.put("path", "/CAT001/CAT001-001");
        mainIncome.put("sortOrder", 1);
        mainIncome.put("description", "主营业务收入预算");
        mainIncome.put("enabled", true);
        mainIncome.put("templateCount", countByExact("CAT001-001"));

        Map<String, Object> otherIncome = new HashMap<>();
        otherIncome.put("id", "CAT001-002");
        otherIncome.put("code", "OTHER_INCOME");
        otherIncome.put("name", "其他收入");
        otherIncome.put("parentId", "CAT001");
        otherIncome.put("level", 2);
        otherIncome.put("path", "/CAT001/CAT001-002");
        otherIncome.put("sortOrder", 2);
        otherIncome.put("description", "其他业务收入预算");
        otherIncome.put("enabled", true);
        otherIncome.put("templateCount", countByExact("CAT001-002"));

        Map<String, Object> reportTemplates = new HashMap<>();
        reportTemplates.put("id", "CAT001-003");
        reportTemplates.put("code", "REPORT_TEMPLATE");
        reportTemplates.put("name", "报表模板");
        reportTemplates.put("parentId", "CAT001");
        reportTemplates.put("level", 2);
        reportTemplates.put("path", "/CAT001/CAT001-003");
        reportTemplates.put("sortOrder", 3);
        reportTemplates.put("description", "各类报表模板");
        reportTemplates.put("enabled", true);
        reportTemplates.put("templateCount", countByExact("CAT001-003"));

        incomeChildren.add(mainIncome);
        incomeChildren.add(otherIncome);
        incomeChildren.add(reportTemplates);
        income.put("children", incomeChildren);

        // 一级分类：支出预算
        Map<String, Object> expense = new HashMap<>();
        expense.put("id", "CAT002");
        expense.put("code", "EXPENSE");
        expense.put("name", "支出预算");
        expense.put("parentId", null);
        expense.put("level", 1);
        expense.put("path", "/CAT002");
        expense.put("sortOrder", 2);
        expense.put("description", "包含各类支出预算模板");
        expense.put("enabled", true);
        expense.put("templateCount", countByPrefix("CAT002"));

        // 支出预算的子分类
        List<Map<String, Object>> expenseChildren = new ArrayList<>();

        Map<String, Object> operationExpense = new HashMap<>();
        operationExpense.put("id", "CAT002-001");
        operationExpense.put("code", "OPERATION_EXPENSE");
        operationExpense.put("name", "运营支出");
        operationExpense.put("parentId", "CAT002");
        operationExpense.put("level", 2);
        operationExpense.put("path", "/CAT002/CAT002-001");
        operationExpense.put("sortOrder", 1);
        operationExpense.put("description", "日常运营支出预算");
        operationExpense.put("enabled", true);
        operationExpense.put("templateCount", countByExact("CAT002-001"));

        Map<String, Object> investmentExpense = new HashMap<>();
        investmentExpense.put("id", "CAT002-002");
        investmentExpense.put("code", "INVESTMENT_EXPENSE");
        investmentExpense.put("name", "投资支出");
        investmentExpense.put("parentId", "CAT002");
        investmentExpense.put("level", 2);
        investmentExpense.put("path", "/CAT002/CAT002-002");
        investmentExpense.put("sortOrder", 2);
        investmentExpense.put("description", "投资性支出预算");
        investmentExpense.put("enabled", true);
        investmentExpense.put("templateCount", countByExact("CAT002-002"));

        expenseChildren.add(operationExpense);
        expenseChildren.add(investmentExpense);
        expense.put("children", expenseChildren);

        // 一级分类：资产预算
        Map<String, Object> asset = new HashMap<>();
        asset.put("id", "CAT003");
        asset.put("code", "ASSET");
        asset.put("name", "资产预算");
        asset.put("parentId", null);
        asset.put("level", 1);
        asset.put("path", "/CAT003");
        asset.put("sortOrder", 3);
        asset.put("description", "包含各类资产预算模板");
        asset.put("enabled", true);
        asset.put("templateCount", countByPrefix("CAT003"));

        // 一级分类：负债预算
        Map<String, Object> debt = new HashMap<>();
        debt.put("id", "CAT004");
        debt.put("code", "DEBT");
        debt.put("name", "负债预算");
        debt.put("parentId", null);
        debt.put("level", 1);
        debt.put("path", "/CAT004");
        debt.put("sortOrder", 4);
        debt.put("description", "包含各类负债预算模板");
        debt.put("enabled", true);
        debt.put("templateCount", countByPrefix("CAT004"));

        tree.add(income);
        tree.add(expense);
        tree.add(asset);
        tree.add(debt);

        return tree;
    }

    @Override
    public List<Map<String, Object>> getCategories() {
        log.info("构建分类扁平列表，templateCount 从数据库实时统计");

        List<Map<String, Object>> categories = new ArrayList<>();

        // 一级分类
        categories.add(createCategory("CAT001", "INCOME", "收入预算", null, 1, "/CAT001", 1, "包含各类收入预算模板", true, countByPrefix("CAT001")));
        categories.add(createCategory("CAT002", "EXPENSE", "支出预算", null, 1, "/CAT002", 2, "包含各类支出预算模板", true, countByPrefix("CAT002")));
        categories.add(createCategory("CAT003", "ASSET", "资产预算", null, 1, "/CAT003", 3, "包含各类资产预算模板", true, countByPrefix("CAT003")));
        categories.add(createCategory("CAT004", "DEBT", "负债预算", null, 1, "/CAT004", 4, "包含各类负债预算模板", true, countByPrefix("CAT004")));

        // 二级分类
        categories.add(createCategory("CAT001-001", "MAIN_INCOME", "营业收入", "CAT001", 2, "/CAT001/CAT001-001", 1, "主营业务收入预算", true, countByExact("CAT001-001")));
        categories.add(createCategory("CAT001-002", "OTHER_INCOME", "其他收入", "CAT001", 2, "/CAT001/CAT001-002", 2, "其他业务收入预算", true, countByExact("CAT001-002")));
        categories.add(createCategory("CAT001-003", "REPORT_TEMPLATE", "报表模板", "CAT001", 2, "/CAT001/CAT001-003", 3, "各类报表模板", true, countByExact("CAT001-003")));
        categories.add(createCategory("CAT002-001", "OPERATION_EXPENSE", "运营支出", "CAT002", 2, "/CAT002/CAT002-001", 1, "日常运营支出预算", true, countByExact("CAT002-001")));
        categories.add(createCategory("CAT002-002", "INVESTMENT_EXPENSE", "投资支出", "CAT002", 2, "/CAT002/CAT002-002", 2, "投资性支出预算", true, countByExact("CAT002-002")));

        return categories;
    }

    /**
     * 创建分类对象
     */
    private Map<String, Object> createCategory(String id, String code, String name, String parentId,
                                                Integer level, String path, Integer sortOrder,
                                                String description, Boolean enabled, Integer templateCount) {
        Map<String, Object> category = new HashMap<>();
        category.put("id", id);
        category.put("code", code);
        category.put("name", name);
        category.put("parentId", parentId);
        category.put("level", level);
        category.put("path", path);
        category.put("sortOrder", sortOrder);
        category.put("description", description);
        category.put("enabled", enabled);
        category.put("templateCount", templateCount);
        return category;
    }

    @Override
    public void updateTemplateCount(String categoryId) {
        // 分类表不存在，不做任何操作
        log.info("分类表 TBL_BUDGET_TEMPLATE_CATEGORY 不存在，跳过更新模板数量");
    }
}
