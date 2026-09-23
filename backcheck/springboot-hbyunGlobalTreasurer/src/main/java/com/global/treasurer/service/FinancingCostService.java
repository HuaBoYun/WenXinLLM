package com.global.treasurer.service;

import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.FinancingCostDTO;
import com.global.treasurer.dto.FinancingCostQueryDTO;
import com.global.treasurer.entity.TblFinancingCostAnalysis;

import java.util.List;
import java.util.Map;

/**
 * 融资成本分析服务接口
 *
 * @author 华博云开发团队
 * @since 2025-01-13
 */
public interface FinancingCostService {

    /**
     * 分页查询融资成本列表
     *
     * @param queryDTO 查询条件
     * @return 分页结果
     */
    PageInfo<TblFinancingCostAnalysis> getCostList(FinancingCostQueryDTO queryDTO);

    /**
     * 根据ID查询融资成本详情
     *
     * @param analysisId 分析ID
     * @return 融资成本
     */
    TblFinancingCostAnalysis getCostById(Long analysisId);

    /**
     * 保存融资成本分析（新增或更新）
     *
     * @param dto 融资成本DTO
     * @return 保存后的融资成本
     */
    TblFinancingCostAnalysis saveCost(FinancingCostDTO dto);

    /**
     * 删除融资成本
     *
     * @param analysisId 分析ID
     */
    void deleteCost(Long analysisId);

    /**
     * 批量删除融资成本
     *
     * @param analysisIds 分析ID列表
     */
    void batchDeleteCosts(List<Long> analysisIds);

    /**
     * 执行成本分析
     *
     * @param financingId 融资ID
     * @param analysisDate 分析日期
     * @param periodType 周期类型
     * @return 分析结果
     */
    TblFinancingCostAnalysis performAnalysis(Long financingId,
                                            String analysisDate,
                                            String periodType);

    /**
     * 查询成本统计
     *
     * @param companyId 公司ID
     * @param periodType 周期类型
     * @param periodValue 周期值
     * @return 统计数据
     */
    Map<String, Object> getCostStatistics(Long companyId,
                                         String periodType,
                                         String periodValue);

    /**
     * 查询成本对比
     *
     * @param params 查询参数
     * @return 对比数据
     */
    List<Map<String, Object>> getCostComparison(Map<String, Object> params);

    /**
     * 查询成本趋势
     *
     * @param companyId 公司ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 趋势数据
     */
    List<Map<String, Object>> getCostTrend(Long companyId,
                                          String startDate,
                                          String endDate);

    /**
     * 导出成本报表
     *
     * @param params 查询参数
     * @return 导出数据
     */
    List<TblFinancingCostAnalysis> exportCostReport(Map<String, Object> params);
}

