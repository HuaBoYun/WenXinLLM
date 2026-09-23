package com.financial.sharing.service;

import com.financial.sharing.dto.LedgerAnalysisQueryParam;
import com.hbfk.entity.TblStaffUtil;

import java.util.List;
import java.util.Map;

/**
 * 账本分析服务接口
 *
 * @author Financial Sharing System
 * @since 2024-12-08
 */
public interface LedgerAnalysisService {

    // ==================== 科目余额分析 ====================

    /**
     * 科目余额分析
     *
     * @param param 查询参数
     * @param loginStaff 登录用户信息
     * @return 分析结果
     */
    Map<String, Object> getSubjectBalanceAnalysis(LedgerAnalysisQueryParam param, TblStaffUtil loginStaff);

    /**
     * 科目余额趋势分析
     *
     * @param param 查询参数
     * @param loginStaff 登录用户信息
     * @return 趋势分析结果
     */
    List<Map<String, Object>> getSubjectBalanceTrendAnalysis(LedgerAnalysisQueryParam param, TblStaffUtil loginStaff);

    /**
     * 科目余额结构分析
     *
     * @param param 查询参数
     * @param loginStaff 登录用户信息
     * @return 结构分析结果
     */
    Map<String, Object> getSubjectBalanceStructureAnalysis(LedgerAnalysisQueryParam param, TblStaffUtil loginStaff);

    /**
     * 科目余额异常分析
     *
     * @param param 查询参数
     * @param loginStaff 登录用户信息
     * @return 异常分析结果
     */
    List<Map<String, Object>> getSubjectBalanceAbnormalAnalysis(LedgerAnalysisQueryParam param, TblStaffUtil loginStaff);

    // ==================== 发生额分析 ====================

    /**
     * 科目发生额分析
     *
     * @param param 查询参数
     * @param loginStaff 登录用户信息
     * @return 分析结果
     */
    Map<String, Object> getSubjectOccurrenceAnalysis(LedgerAnalysisQueryParam param, TblStaffUtil loginStaff);

    /**
     * 借贷发生额对比分析
     *
     * @param param 查询参数
     * @param loginStaff 登录用户信息
     * @return 对比分析结果
     */
    List<Map<String, Object>> getDebitCreditOccurrenceAnalysis(LedgerAnalysisQueryParam param, TblStaffUtil loginStaff);

    /**
     * 发生额波动分析
     *
     * @param param 查询参数
     * @param loginStaff 登录用户信息
     * @return 波动分析结果
     */
    Map<String, Object> getOccurrenceFluctuationAnalysis(LedgerAnalysisQueryParam param, TblStaffUtil loginStaff);

    // ==================== 账龄分析 ====================

    /**
     * 应收账款账龄分析
     *
     * @param param 查询参数
     * @param loginStaff 登录用户信息
     * @return 账龄分析结果
     */
    List<Map<String, Object>> getReceivableAgeAnalysis(LedgerAnalysisQueryParam param, TblStaffUtil loginStaff);

    /**
     * 应付账款账龄分析
     *
     * @param param 查询参数
     * @param loginStaff 登录用户信息
     * @return 账龄分析结果
     */
    List<Map<String, Object>> getPayableAgeAnalysis(LedgerAnalysisQueryParam param, TblStaffUtil loginStaff);

    /**
     * 坏账风险分析
     *
     * @param param 查询参数
     * @param loginStaff 登录用户信息
     * @return 风险分析结果
     */
    Map<String, Object> getBadDebtRiskAnalysis(LedgerAnalysisQueryParam param, TblStaffUtil loginStaff);

    // ==================== 趋势分析 ====================

    /**
     * 收入趋势分析
     *
     * @param param 查询参数
     * @param loginStaff 登录用户信息
     * @return 趋势分析结果
     */
    List<Map<String, Object>> getRevenueTrendAnalysis(LedgerAnalysisQueryParam param, TblStaffUtil loginStaff);

    /**
     * 费用趋势分析
     *
     * @param param 查询参数
     * @param loginStaff 登录用户信息
     * @return 趋势分析结果
     */
    List<Map<String, Object>> getExpenseTrendAnalysis(LedgerAnalysisQueryParam param, TblStaffUtil loginStaff);

    /**
     * 利润趋势分析
     *
     * @param param 查询参数
     * @param loginStaff 登录用户信息
     * @return 趋势分析结果
     */
    List<Map<String, Object>> getProfitTrendAnalysis(LedgerAnalysisQueryParam param, TblStaffUtil loginStaff);

    /**
     * 现金流趋势分析
     *
     * @param param 查询参数
     * @param loginStaff 登录用户信息
     * @return 趋势分析结果
     */
    List<Map<String, Object>> getCashFlowTrendAnalysis(LedgerAnalysisQueryParam param, TblStaffUtil loginStaff);

    // ==================== 对比分析 ====================

    /**
     * 同期对比分析
     *
     * @param param 查询参数
     * @param loginStaff 登录用户信息
     * @return 对比分析结果
     */
    List<Map<String, Object>> getPeriodComparisonAnalysis(LedgerAnalysisQueryParam param, TblStaffUtil loginStaff);

    /**
     * 环比分析
     *
     * @param param 查询参数
     * @param loginStaff 登录用户信息
     * @return 环比分析结果
     */
    List<Map<String, Object>> getMonthOnMonthAnalysis(LedgerAnalysisQueryParam param, TblStaffUtil loginStaff);

    /**
     * 同比分析
     *
     * @param param 查询参数
     * @param loginStaff 登录用户信息
     * @return 同比分析结果
     */
    List<Map<String, Object>> getYearOnYearAnalysis(LedgerAnalysisQueryParam param, TblStaffUtil loginStaff);

    /**
     * 预算执行对比分析
     *
     * @param param 查询参数
     * @param loginStaff 登录用户信息
     * @return 预算执行分析结果
     */
    Map<String, Object> getBudgetExecutionAnalysis(LedgerAnalysisQueryParam param, TblStaffUtil loginStaff);

    // ==================== 结构分析 ====================

    /**
     * 资产结构分析
     *
     * @param param 查询参数
     * @param loginStaff 登录用户信息
     * @return 结构分析结果
     */
    Map<String, Object> getAssetStructureAnalysis(LedgerAnalysisQueryParam param, TblStaffUtil loginStaff);

    /**
     * 负债结构分析
     *
     * @param param 查询参数
     * @param loginStaff 登录用户信息
     * @return 结构分析结果
     */
    Map<String, Object> getLiabilityStructureAnalysis(LedgerAnalysisQueryParam param, TblStaffUtil loginStaff);

    /**
     * 所有者权益结构分析
     *
     * @param param 查询参数
     * @param loginStaff 登录用户信息
     * @return 结构分析结果
     */
    Map<String, Object> getEquityStructureAnalysis(LedgerAnalysisQueryParam param, TblStaffUtil loginStaff);

    /**
     * 收入成本结构分析
     *
     * @param param 查询参数
     * @param loginStaff 登录用户信息
     * @return 结构分析结果
     */
    Map<String, Object> getRevenueCostStructureAnalysis(LedgerAnalysisQueryParam param, TblStaffUtil loginStaff);

    // ==================== 财务比率分析 ====================

    /**
     * 偿债能力比率分析
     *
     * @param param 查询参数
     * @param loginStaff 登录用户信息
     * @return 比率分析结果
     */
    Map<String, Object> getSolvencyRatioAnalysis(LedgerAnalysisQueryParam param, TblStaffUtil loginStaff);

    /**
     * 盈利能力比率分析
     *
     * @param param 查询参数
     * @param loginStaff 登录用户信息
     * @return 比率分析结果
     */
    Map<String, Object> getProfitabilityRatioAnalysis(LedgerAnalysisQueryParam param, TblStaffUtil loginStaff);

    /**
     * 运营能力比率分析
     *
     * @param param 查询参数
     * @param loginStaff 登录用户信息
     * @return 比率分析结果
     */
    Map<String, Object> getOperatingRatioAnalysis(LedgerAnalysisQueryParam param, TblStaffUtil loginStaff);

    /**
     * 发展能力比率分析
     *
     * @param param 查询参数
     * @param loginStaff 登录用户信息
     * @return 比率分析结果
     */
    Map<String, Object> getGrowthRatioAnalysis(LedgerAnalysisQueryParam param, TblStaffUtil loginStaff);

    // ==================== 异常数据分析 ====================

    /**
     * 异常交易分析
     *
     * @param param 查询参数
     * @param loginStaff 登录用户信息
     * @return 异常分析结果
     */
    List<Map<String, Object>> getAbnormalTransactionAnalysis(LedgerAnalysisQueryParam param, TblStaffUtil loginStaff);

    /**
     * 科目余额异常分析
     *
     * @param param 查询参数
     * @param loginStaff 登录用户信息
     * @return 异常分析结果
     */
    List<Map<String, Object>> getAbnormalBalanceAnalysis(LedgerAnalysisQueryParam param, TblStaffUtil loginStaff);

    /**
     * 大额交易分析
     *
     * @param param 查询参数
     * @param loginStaff 登录用户信息
     * @return 大额交易分析结果
     */
    List<Map<String, Object>> getLargeAmountTransactionAnalysis(LedgerAnalysisQueryParam param, TblStaffUtil loginStaff);

    /**
     * 频繁交易分析
     *
     * @param param 查询参数
     * @param loginStaff 登录用户信息
     * @return 频繁交易分析结果
     */
    List<Map<String, Object>> getFrequentTransactionAnalysis(LedgerAnalysisQueryParam param, TblStaffUtil loginStaff);
}