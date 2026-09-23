package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblFinancingCostAnalysis;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 融资成本分析Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-01-13
 */
@Mapper
public interface FinancingCostMapper extends BaseMapper<TblFinancingCostAnalysis> {

    /**
     * 分页查询融资成本列表
     *
     * @param params 查询参数
     * @return 融资成本列表
     */
    List<TblFinancingCostAnalysis> selectCostList(Map<String, Object> params);

    /**
     * 根据ID查询融资成本详情
     *
     * @param analysisId 分析ID
     * @return 融资成本
     */
    TblFinancingCostAnalysis selectCostById(@Param("analysisId") Long analysisId);

    /**
     * 统计融资成本数量
     *
     * @param params 查询参数
     * @return 数量
     */
    int countCostList(Map<String, Object> params);

    /**
     * 查询成本分析统计
     *
     * @param companyId 公司ID
     * @param periodType 周期类型
     * @param periodValue 周期值
     * @return 统计数据
     */
    Map<String, Object> selectCostStatistics(@Param("companyId") Long companyId,
                                             @Param("periodType") String periodType,
                                             @Param("periodValue") String periodValue);

    /**
     * 查询成本对比数据
     *
     * @param params 查询参数
     * @return 对比数据
     */
    List<Map<String, Object>> selectCostComparison(Map<String, Object> params);

    /**
     * 查询成本趋势数据
     *
     * @param companyId 公司ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 趋势数据
     */
    List<Map<String, Object>> selectCostTrend(@Param("companyId") Long companyId,
                                              @Param("startDate") String startDate,
                                              @Param("endDate") String endDate);
}
