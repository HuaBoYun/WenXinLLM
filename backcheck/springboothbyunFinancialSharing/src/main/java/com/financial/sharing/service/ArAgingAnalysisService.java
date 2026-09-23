package com.financial.sharing.service;

import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.ArAgingAnalysisQueryParam;

import java.time.LocalDate;

/**
 * 账龄分析服务接口
 * @author system
 * @since 2026-01-04
 */
public interface ArAgingAnalysisService {

    /**
     * 查询账龄分析明细
     */
    MyJsonBean<PageResult> getAgingDetails(ArAgingAnalysisQueryParam param);

    /**
     * 按账龄区间统计
     */
    MyJsonBean getAgingRangeSummary(LocalDate analysisDate, Long tenantId);

    /**
     * 按客户统计账龄
     */
    MyJsonBean getAgingByCustomer(LocalDate analysisDate, Long tenantId);

    /**
     * 按风险等级统计
     */
    MyJsonBean getAgingByRiskLevel(LocalDate analysisDate, Long tenantId);

    /**
     * 生成账龄快照
     */
    MyJsonBean generateAgingSnapshot(LocalDate analysisDate, Long tenantId);

    /**
     * 查询最新快照日期
     */
    MyJsonBean getLatestSnapshotDate(Long tenantId);

    /**
     * 查询账龄趋势
     */
    MyJsonBean getAgingTrend(LocalDate startDate, LocalDate endDate, Long tenantId);

    /**
     * 查询逾期金额统计
     */
    MyJsonBean getTotalOverdueAmount(LocalDate analysisDate, Long tenantId);

    /**
     * 导出账龄分析报表
     */
    MyJsonBean exportAgingReport(ArAgingAnalysisQueryParam param);
}

