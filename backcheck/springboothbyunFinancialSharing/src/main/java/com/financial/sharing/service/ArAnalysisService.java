package com.financial.sharing.service;

import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.vo.param.ArAnalysisQueryParam;

import java.time.LocalDate;

/**
 * 应收分析服务接口
 * @author system
 * @since 2026-01-04
 */
public interface ArAnalysisService {

    /**
     * 获取应收总览数据
     */
    MyJsonBean getReceivableOverview(Long tenantId);

    /**
     * 获取应收趋势分析
     */
    MyJsonBean getReceivableTrend(LocalDate startDate, LocalDate endDate, Long tenantId);

    /**
     * 获取客户应收排名
     */
    MyJsonBean getCustomerReceivableRanking(Long tenantId, Integer topN);

    /**
     * 获取收款趋势分析
     */
    MyJsonBean getReceiptTrend(LocalDate startDate, LocalDate endDate, Long tenantId);

    /**
     * 获取回款率分析
     */
    MyJsonBean getCollectionRateAnalysis(LocalDate startDate, LocalDate endDate, Long tenantId);

    /**
     * 获取逾期分析
     */
    MyJsonBean getOverdueAnalysis(Long tenantId);

    /**
     * 获取坏账分析
     */
    MyJsonBean getBadDebtAnalysis(Long tenantId);

    /**
     * 获取DSO（应收账款周转天数）分析
     */
    MyJsonBean getDsoAnalysis(LocalDate startDate, LocalDate endDate, Long tenantId);

    /**
     * 获取客户信用分析
     */
    MyJsonBean getCustomerCreditAnalysis(String customerId, Long tenantId);

    /**
     * 获取综合分析报表
     */
    MyJsonBean getComprehensiveReport(ArAnalysisQueryParam param);

    /**
     * 导出分析报表
     */
    MyJsonBean exportAnalysisReport(ArAnalysisQueryParam param);

    /**
     * 获取应收结构分析
     */
    MyJsonBean getStructureAnalysis(Long tenantId);

    /**
     * 获取收款效率分析
     */
    MyJsonBean getEfficiencyAnalysis(Long tenantId, LocalDate startDate, LocalDate endDate);

    /**
     * 获取客户排名数据
     */
    MyJsonBean getCustomerRankingData(Long tenantId, String rankingType, LocalDate startDate, LocalDate endDate, Integer topN);
}

