package com.financial.sharing.enterpriseReport.service;

import com.financial.sharing.enterpriseReport.dto.ReportDataQueryDTO;

import java.util.List;
import java.util.Map;

/**
 * 报表数据查询分析Service接口
 * 
 * @author hbyun
 * @date 2026-01-30
 */
public interface ReportDataAnalysisService {

    /**
     * 按指标汇总数据
     * 
     * @param taskId 任务ID
     * @param period 期间
     * @return 汇总结果列表
     */
    List<ReportDataQueryDTO> aggregateByIndicator(String taskId, String period);

    /**
     * 按组织汇总数据
     * 
     * @param taskId 任务ID
     * @param period 期间
     * @return 汇总结果列表
     */
    List<ReportDataQueryDTO> aggregateByOrg(String taskId, String period);

    /**
     * 按期间汇总数据
     * 
     * @param taskId 任务ID
     * @param indicatorId 指标ID
     * @param orgId 组织ID
     * @return 汇总结果列表
     */
    List<ReportDataQueryDTO> aggregateByPeriod(String taskId, String indicatorId, String orgId);

    /**
     * 期间对比分析
     * 
     * @param taskId 任务ID
     * @param indicatorId 指标ID
     * @param orgId 组织ID
     * @param currentPeriod 当前期间
     * @param comparePeriod 对比期间
     * @return 对比结果
     */
    ReportDataQueryDTO comparePeriod(String taskId, String indicatorId, String orgId, 
                                     String currentPeriod, String comparePeriod);

    /**
     * 多维度数据查询
     * 
     * @param params 查询参数
     * @return 查询结果列表
     */
    List<ReportDataQueryDTO> multiDimensionQuery(Map<String, Object> params);

    /**
     * 数据趋势分析
     * 
     * @param taskId 任务ID
     * @param indicatorId 指标ID
     * @param orgId 组织ID
     * @param startPeriod 开始期间
     * @param endPeriod 结束期间
     * @return 趋势数据列表
     */
    List<ReportDataQueryDTO> trendAnalysis(String taskId, String indicatorId, String orgId,
                                           String startPeriod, String endPeriod);
}

