package com.global.treasurer.service;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblRegulatoryAnalysis;

import java.util.List;
import java.util.Map;

/**
 * 监管分析Service接口
 * @author Claude
 * @date 2026-01-20
 */
public interface TblRegulatoryAnalysisService {

    /**
     * 分页查询监管分析列表
     */
    PageInfo<TblRegulatoryAnalysis> getAnalysisPage(Integer pageNum, Integer pageSize,
                                                    String analysisName, String analysisType,
                                                    String complianceStatus, String startDate, String endDate);

    /**
     * 根据ID查询监管分析
     */
    TblRegulatoryAnalysis getAnalysisById(String analysisId);

    /**
     * 保存监管分析
     */
    TblRegulatoryAnalysis saveAnalysis(TblRegulatoryAnalysis analysis);

    /**
     * 更新监管分析
     */
    TblRegulatoryAnalysis updateAnalysis(TblRegulatoryAnalysis analysis);

    /**
     * 删除监管分析
     */
    void deleteAnalysis(String analysisId);

    /**
     * 执行监管分析
     */
    TblRegulatoryAnalysis executeAnalysis(String analysisType, String startDate, String endDate);

    /**
     * 审核监管分析
     */
    void reviewAnalysis(String analysisId, String reviewResult, String reviewRemark);

    /**
     * 生成监管报告
     */
    byte[] generateReport(String analysisId, String reportFormat);

    /**
     * 获取合规状态统计
     */
    Map<String, Object> getComplianceStatistics();

    /**
     * 获取风险等级分布
     */
    Map<String, Object> getRiskLevelDistribution();

    /**
     * 获取分析类型列表
     */
    List<Map<String, Object>> getAnalysisTypes();

    /**
     * 获取概览统计数据
     */
    Map<String, Object> getOverviewStatistics();
}

