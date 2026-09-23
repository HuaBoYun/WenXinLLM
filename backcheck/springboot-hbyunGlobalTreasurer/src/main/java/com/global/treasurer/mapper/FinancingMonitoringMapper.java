package com.global.treasurer.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.global.treasurer.dto.FinancingMonitoringQueryDTO;
import com.global.treasurer.entity.TblFinancingMonitoring;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 融资监控Mapper接口
 *
 * @author 华博云开发团队
 * @since 2026-01-13
 */
@Mapper
public interface FinancingMonitoringMapper extends BaseMapper<TblFinancingMonitoring> {

    /**
     * 根据查询条件查询融资监控列表
     */
    List<TblFinancingMonitoring> selectByQueryDTO(FinancingMonitoringQueryDTO queryDTO);

    /**
     * 获取监控概览统计
     */
    Map<String, Object> selectMonitoringOverview();

    /**
     * 获取仪表盘数据
     */
    Map<String, Object> selectDashboardData();

    /**
     * 获取趋势分析数据
     */
    List<Map<String, Object>> selectTrendsData(@Param("period") String period);

    /**
     * 获取统计信息
     */
    Map<String, Object> selectMonitoringStatistics();

    /**
     * 根据预警级别查询预警列表
     */
    List<TblFinancingMonitoring> selectByAlertLevel(@Param("alertLevel") String alertLevel);

    /**
     * 查询需要刷新的监控记录
     */
    List<TblFinancingMonitoring> selectNeedRefresh();

    /**
     * 获取融资类型分布数据
     */
    List<Map<String, Object>> selectFinancingTypeDistribution();

    /**
     * 获取风险等级分布数据
     */
    List<Map<String, Object>> selectRiskLevelDistribution();

    /**
     * 获取合规状态分布数据
     */
    List<Map<String, Object>> selectComplianceDistribution();

    /**
     * 获取融资规模趋势数据（按月统计）
     * @param months 查询月数
     * @return 趋势数据列表，包含 month(月份) 和 amount(金额)
     */
    List<Map<String, Object>> selectFinancingScaleTrend(@Param("months") Integer months);

    /**
     * 获取融资结构分析数据（按融资类型统计）
     * @return 结构数据列表，包含 name(类型名称) 和 value(金额或占比)
     */
    List<Map<String, Object>> selectFinancingStructure();
}
