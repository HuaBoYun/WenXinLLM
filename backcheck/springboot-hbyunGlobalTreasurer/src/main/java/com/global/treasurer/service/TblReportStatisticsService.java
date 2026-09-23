package com.global.treasurer.service;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblReportStatistics;

import java.util.List;
import java.util.Map;

/**
 * 报表统计Service接口
 * @author Claude
 * @date 2026-01-20
 */
public interface TblReportStatisticsService {

    /**
     * 分页查询报表统计列表
     */
    PageInfo<TblReportStatistics> getReportPage(Integer pageNum, Integer pageSize,
                                                String reportName, String reportType,
                                                String reportPeriod, String createTime);

    /**
     * 根据ID查询报表统计
     */
    TblReportStatistics getReportById(String reportId);

    /**
     * 保存报表统计
     */
    TblReportStatistics saveReport(TblReportStatistics report);

    /**
     * 更新报表统计
     */
    void updateReport(TblReportStatistics report);

    /**
     * 删除报表统计
     */
    void deleteReport(String reportId);

    /**
     * 生成报表
     */
    TblReportStatistics generateReport(String reportType, String reportPeriod, 
                                       String startDate, String endDate);

    /**
     * 发布报表
     */
    void publishReport(String reportId);

    /**
     * 获取报表数据分析
     */
    Map<String, Object> analyzeReport(String reportId);

    /**
     * 导出报表
     */
    byte[] exportReport(String reportId, String exportFormat);

    /**
     * 获取报表类型列表
     */
    List<Map<String, Object>> getReportTypes();
}

