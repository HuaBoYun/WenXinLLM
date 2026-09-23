package com.financial.sharing.service;

import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.util.PageableParam;

import java.util.Map;

/**
 * 报表分析服务接口
 * 
 * @author Financial Sharing System
 * @since 2024-01-01
 */
public interface ReportsService {

    /**
     * 获取财务报表列表
     * 
     * @param pageableParam 分页参数
     * @param reportType 报表类型
     * @param period 报表期间
     * @param status 报表状态
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 报表列表
     */
    MyJsonBean<PageResult<Map<String, Object>>> getFinancialReports(
            PageableParam pageableParam,
            String reportType,
            String period,
            String status,
            Long bookId,
            Long tenantId);

    /**
     * 获取报表统计信息
     * 
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 统计信息
     */
    MyJsonBean<Map<String, Object>> getStatistics(Long bookId, Long tenantId);

    /**
     * 获取财务报表详情
     * 
     * @param reportId 报表ID
     * @return 报表详情
     */
    MyJsonBean<Map<String, Object>> getFinancialReportDetail(Long reportId);

    /**
     * 保存或更新财务报表
     * 
     * @param reportData 报表数据
     * @return 操作结果
     */
    MyJsonBean<Map<String, Object>> saveOrUpdateFinancialReport(Map<String, Object> reportData);

    /**
     * 删除财务报表
     * 
     * @param reportId 报表ID
     * @return 操作结果
     */
    MyJsonBean<String> deleteFinancialReport(Long reportId);

    /**
     * 生成资产负债表
     * 
     * @param reportData 报表数据
     * @return 操作结果
     */
    MyJsonBean<Map<String, Object>> generateBalanceSheet(Map<String, Object> reportData);

    /**
     * 生成利润表
     * 
     * @param reportData 报表数据
     * @return 操作结果
     */
    MyJsonBean<Map<String, Object>> generateIncomeStatement(Map<String, Object> reportData);

    /**
     * 生成现金流量表
     * 
     * @param reportData 报表数据
     * @return 操作结果
     */
    MyJsonBean<Map<String, Object>> generateCashFlowStatement(Map<String, Object> reportData);

    /**
     * 导出报表
     * 
     * @param reportType 报表类型
     * @param period 报表期间
     * @param format 导出格式
     * @return 导出结果
     */
    MyJsonBean<Map<String, Object>> exportReport(String reportType, String period, String format);
}

