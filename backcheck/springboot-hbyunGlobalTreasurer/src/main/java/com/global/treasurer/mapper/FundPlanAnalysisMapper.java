package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.TblFundPlanAnalysis;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 资金计划分析Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-01-19
 */
@Mapper
public interface FundPlanAnalysisMapper extends BaseMapper<TblFundPlanAnalysis> {

    /**
     * 分页查询资金计划分析列表
     *
     * @param params 查询参数
     * @return 资金计划分析列表
     */
    List<TblFundPlanAnalysis> selectAnalysisPage(Map<String, Object> params);

    /**
     * 统计分析数量
     *
     * @param params 查询参数
     * @return 数量
     */
    int countAnalysisList(Map<String, Object> params);

    /**
     * 查询分析汇总信息
     *
     * @param params 查询参数
     * @return 汇总数据
     */
    Map<String, Object> selectAnalysisSummary(Map<String, Object> params);

    /**
     * 新增资金计划分析（手写SQL，避免达梦列名大小写问题）
     */
    int insertAnalysis(TblFundPlanAnalysis analysis);

    /**
     * 更新资金计划分析（手写SQL，避免达梦列名大小写问题）
     */
    int updateAnalysis(TblFundPlanAnalysis analysis);
}
