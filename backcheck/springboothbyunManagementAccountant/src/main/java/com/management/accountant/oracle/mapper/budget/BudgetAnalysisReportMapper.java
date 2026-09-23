package com.management.accountant.oracle.mapper.budget;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.management.accountant.oracle.entity.budget.BudgetAnalysisReport;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预算分析报告Mapper接口
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
@Mapper
public interface BudgetAnalysisReportMapper extends BaseMapper<BudgetAnalysisReport> {

    /**
     * 根据报告类型查询报告列表
     * 
     * @param reportType 报告类型
     * @return 报告列表
     */
    List<BudgetAnalysisReport> selectByReportType(@Param("reportType") String reportType);

    /**
     * 根据预算年度查询报告列表
     * 
     * @param budgetYear 预算年度
     * @return 报告列表
     */
    List<BudgetAnalysisReport> selectByBudgetYear(@Param("budgetYear") Integer budgetYear);

    /**
     * 根据组织ID查询报告列表
     * 
     * @param organizationId 组织ID
     * @return 报告列表
     */
    List<BudgetAnalysisReport> selectByOrganizationId(@Param("organizationId") String organizationId);

    /**
     * 根据报告状态查询报告列表
     * 
     * @param reportStatus 报告状态
     * @return 报告列表
     */
    List<BudgetAnalysisReport> selectByReportStatus(@Param("reportStatus") String reportStatus);

    /**
     * 查询已发布的报告列表
     * 
     * @return 报告列表
     */
    List<BudgetAnalysisReport> selectPublishedReports();
}

