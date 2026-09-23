package com.financial.sharing.service;

import com.financial.sharing.dto.param.PayableAnalysisQueryParam;
import com.financial.sharing.vo.result.PayableAnalysisVO;
import com.financial.sharing.vo.result.PayableSummaryVO;

import java.util.List;
import java.util.Map;

/**
 * 应付分析Service接口
 * @author system
 * @since 2025-01-05
 */
public interface PayableAnalysisService {

    /**
     * 获取应付汇总
     * @return 汇总信息
     */
    PayableSummaryVO getSummary();

    /**
     * 获取账龄分析
     * @param param 查询参数
     * @return 账龄分析数据
     */
    List<PayableAnalysisVO.AgingAnalysisItem> getAgingAnalysis(PayableAnalysisQueryParam param);

    /**
     * 获取供应商分布
     * @param param 查询参数
     * @return 供应商分布数据
     */
    List<PayableAnalysisVO.SupplierDistributionItem> getSupplierDistribution(PayableAnalysisQueryParam param);

    /**
     * 获取趋势分析
     * @param param 查询参数
     * @return 趋势数据
     */
    List<PayableAnalysisVO.TrendItem> getTrendAnalysis(PayableAnalysisQueryParam param);

    /**
     * 获取结构分析数据(用于饼图展示)
     * @param param 查询参数
     * @return 结构分析数据列表
     */
    List<Map<String, Object>> getStructureAnalysis(PayableAnalysisQueryParam param);

    /**
     * 获取付款计划
     * @param param 查询参数
     * @return 付款计划数据
     */
    List<Map<String, Object>> getPaymentPlan(PayableAnalysisQueryParam param);

    /**
     * 获取逾期分析
     * @return 逾期分析数据
     */
    Map<String, Object> getOverdueAnalysis();

    /**
     * 获取供应商排名
     * @param param 查询参数
     * @return 供应商排名数据
     */
    List<Map<String, Object>> getSupplierRanking(PayableAnalysisQueryParam param);

    /**
     * 获取业务类型分布
     * @return 业务类型分布数据
     */
    List<Map<String, Object>> getBusinessTypeDistribution();

    /**
     * 获取票据统计
     * @return 票据统计数据
     */
    Map<String, Object> getBillStatistics();

    /**
     * 导出应付报表
     * @param param 查询参数
     * @return 导出文件路径
     */
    String exportReport(PayableAnalysisQueryParam param);

    /**
     * 获取现金流预测
     * @param param 查询参数
     * @return 现金流预测数据
     */
    List<Map<String, Object>> getCashFlowForecast(PayableAnalysisQueryParam param);
}

