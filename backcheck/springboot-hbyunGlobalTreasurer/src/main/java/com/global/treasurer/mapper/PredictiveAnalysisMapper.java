package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.entity.PredictiveAnalysis;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 预测分析Mapper接口
 *
 * @author 华博云开发团队
 * @since 2025-01-12
 */
@Mapper
public interface PredictiveAnalysisMapper extends BaseMapper<PredictiveAnalysis> {

    /**
     * 根据条件查询预测分析列表(支持动态条件)
     *
     * @param analysisName 分析名称(模糊查询)
     * @param analysisType 分析类型(精确查询)
     * @param analysisStatus 分析状态(精确查询)
     * @param orgId 组织ID(精确查询)
     * @return 预测分析列表
     */
    List<PredictiveAnalysis> selectByCondition(@Param("analysisName") String analysisName,
                                               @Param("analysisType") String analysisType,
                                               @Param("analysisStatus") String analysisStatus,
                                               @Param("orgId") Long orgId);
}
