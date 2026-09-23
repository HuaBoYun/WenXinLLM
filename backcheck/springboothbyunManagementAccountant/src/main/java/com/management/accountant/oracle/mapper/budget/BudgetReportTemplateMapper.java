package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetReportTemplate;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算报告模板 Mapper 接口
 */
@Mapper
public interface BudgetReportTemplateMapper extends BaseMapper<BudgetReportTemplate> {

    /**
     * 查询快速模板列表（is_quick=1）
     */
    List<BudgetReportTemplate> selectQuickTemplates();

    /**
     * 按报告类型查询模板
     */
    List<BudgetReportTemplate> selectByReportType(@Param("reportType") String reportType);
}
