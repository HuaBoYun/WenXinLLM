package com.global.treasurer.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.global.treasurer.entity.KpiIndicator;

import java.math.BigDecimal;
import java.util.Map;

/**
 * KPI指标Service接口
 *
 * @author 华博云开发团队
 * @since 2025-01-12
 */
public interface IKpiIndicatorService extends IService<KpiIndicator> {

    /**
     * 分页查询KPI指标
     * @param page 分页对象
     * @param params 查询参数
     * @return 分页结果
     */
    IPage<KpiIndicator> selectPage(IPage<KpiIndicator> page, Map<String, Object> params);

    /**
     * 计算KPI指标
     * @param kpiId KPI ID
     * @return 是否成功
     */
    boolean calculateKpi(Long kpiId);

    /**
     * 更新KPI当前值
     * @param kpiId KPI ID
     * @param currentValue 当前值
     * @return 是否成功
     */
    boolean updateCurrentValue(Long kpiId, BigDecimal currentValue);

    /**
     * 更新KPI状态
     * @param kpiId KPI ID
     * @param kpiStatus KPI状态
     * @return 是否成功
     */
    boolean updateKpiStatus(Long kpiId, String kpiStatus);

    /**
     * 批量计算KPI指标
     * @param kpiIds KPI ID列表
     * @return 计算结果,包含成功数、失败数、每个KPI的计算状态
     */
    Map<String, Object> batchCalculateKpi(java.util.List<Long> kpiIds);
}
