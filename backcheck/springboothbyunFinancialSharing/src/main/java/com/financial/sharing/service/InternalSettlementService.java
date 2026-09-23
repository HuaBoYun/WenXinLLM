package com.financial.sharing.service;

import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.InternalSettlementQueryParam;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import com.financial.sharing.oracle.entity.InternalSettlementEntity;

/**
 * 内部结算服务接口
 * 
 * @author system
 * @since 2024-12-19
 */
public interface InternalSettlementService {

    // ==================== 基础CRUD操作 ====================

    /**
     * 分页查询内部结算列表
     */
    MyJsonBean<PageResult<Map<String, Object>>> getInternalSettlementList(InternalSettlementQueryParam param);

    /**
     * 查询内部结算详情
     */
    MyJsonBean<Map<String, Object>> getInternalSettlementDetail(Long settlementId);

    /**
     * 创建内部结算记录
     */
    MyJsonBean<Map<String, Object>> createInternalSettlement(Map<String, Object> settlementData);

    /**
     * 更新内部结算记录
     */
    MyJsonBean<Map<String, Object>> updateInternalSettlement(Long settlementId, Map<String, Object> settlementData);

    /**
     * 删除内部结算记录
     */
    MyJsonBean<Boolean> deleteInternalSettlement(Long settlementId);

    /**
     * 批量删除内部结算记录
     */
    MyJsonBean<Boolean> batchDeleteInternalSettlement(List<Long> settlementIds);

    /**
     * 查询内部结算统计信息
     */
    MyJsonBean<Map<String, Object>> getInternalSettlementStats(InternalSettlementQueryParam param);

    /**
     * 批量插入内部结算记录
     */
    int batchInsertInternalSettlement(List<InternalSettlementEntity> settlementList);

    // ==================== 内部交易管理 ====================

    /**
     * 分页查询内部交易列表
     */
    MyJsonBean<PageResult<Map<String, Object>>> getInternalTransactionList(InternalSettlementQueryParam param);

    /**
     * 查询内部交易详情
     */
    MyJsonBean<Map<String, Object>> getInternalTransactionDetail(Long transactionId);

    /**
     * 创建内部交易
     */
    MyJsonBean<Map<String, Object>> createInternalTransaction(Map<String, Object> transactionData);

    /**
     * 更新内部交易
     */
    MyJsonBean<Map<String, Object>> updateInternalTransaction(Long transactionId, Map<String, Object> transactionData);

    /**
     * 删除内部交易
     */
    MyJsonBean<Boolean> deleteInternalTransaction(Long transactionId);

    /**
     * 审批内部交易
     */
    MyJsonBean<Boolean> approveInternalTransaction(Long transactionId, Map<String, Object> approvalData);

    /**
     * 批量审批内部交易
     */
    MyJsonBean<Boolean> batchApproveInternalTransaction(List<Long> transactionIds, Map<String, Object> approvalData);

    /**
     * 确认内部交易
     */
    MyJsonBean<Boolean> confirmInternalTransaction(Long transactionId, Map<String, Object> confirmData);

    /**
     * 结算内部交易
     */
    MyJsonBean<Boolean> settleInternalTransaction(Long transactionId, Map<String, Object> settlementData);

    /**
     * 查询内部交易统计
     */
    MyJsonBean<Map<String, Object>> getInternalTransactionStats(InternalSettlementQueryParam param);

    // ==================== 转移定价管理 ====================

    /**
     * 分页查询转移定价策略列表
     */
    MyJsonBean<PageResult<Map<String, Object>>> getTransferPricingList(InternalSettlementQueryParam param);

    /**
     * 查询转移定价策略详情
     */
    MyJsonBean<Map<String, Object>> getTransferPricingDetail(Long policyId);

    /**
     * 创建转移定价策略
     */
    MyJsonBean<Map<String, Object>> createTransferPricing(Map<String, Object> policyData);

    /**
     * 更新转移定价策略
     */
    MyJsonBean<Map<String, Object>> updateTransferPricing(Long policyId, Map<String, Object> policyData);

    /**
     * 删除转移定价策略
     */
    MyJsonBean<Boolean> deleteTransferPricing(Long policyId);

    /**
     * 审批转移定价策略
     */
    MyJsonBean<Boolean> approveTransferPricing(Long policyId, Map<String, Object> approvalData);

    /**
     * 批量审批转移定价策略
     */
    MyJsonBean<Boolean> batchApproveTransferPricing(List<Long> policyIds, Map<String, Object> approvalData);

    /**
     * 价格调整
     */
    MyJsonBean<Boolean> adjustTransferPrice(Long policyId, Map<String, Object> adjustmentData);

    /**
     * 查询有效的定价策略
     */
    MyJsonBean<List<Map<String, Object>>> getActivePricingPolicies(String productName, LocalDate effectiveDate);

    /**
     * 查询转移定价统计
     */
    MyJsonBean<Map<String, Object>> getTransferPricingStats(InternalSettlementQueryParam param);

    // ==================== 利润中心管理 ====================

    /**
     * 分页查询利润中心列表
     */
    MyJsonBean<PageResult<Map<String, Object>>> getProfitCenterList(InternalSettlementQueryParam param);

    /**
     * 查询利润中心详情
     */
    MyJsonBean<Map<String, Object>> getProfitCenterDetail(Long centerId);

    /**
     * 创建利润中心
     */
    MyJsonBean<Map<String, Object>> createProfitCenter(Map<String, Object> centerData);

    /**
     * 更新利润中心
     */
    MyJsonBean<Map<String, Object>> updateProfitCenter(Long centerId, Map<String, Object> centerData);

    /**
     * 删除利润中心
     */
    MyJsonBean<Boolean> deleteProfitCenter(Long centerId);

    /**
     * 查询利润中心层级结构
     */
    MyJsonBean<List<Map<String, Object>>> getProfitCenterTree(Long parentId);

    /**
     * 计算利润中心绩效
     */
    MyJsonBean<Map<String, Object>> calculateCenterPerformance(Long centerId, LocalDate startDate, LocalDate endDate);

    /**
     * 利润分析
     */
    MyJsonBean<Map<String, Object>> analyzeProfitCenter(Long centerId, InternalSettlementQueryParam param);

    /**
     * 绩效评价
     */
    MyJsonBean<Map<String, Object>> evaluateCenterPerformance(Long centerId, InternalSettlementQueryParam param);

    /**
     * 查询利润中心统计
     */
    MyJsonBean<Map<String, Object>> getProfitCenterStats(InternalSettlementQueryParam param);

    // ==================== 内部结算处理 ====================

    /**
     * 分页查询结算处理列表
     */
    MyJsonBean<PageResult<Map<String, Object>>> getSettlementProcessList(InternalSettlementQueryParam param);

    /**
     * 查询结算处理详情
     */
    MyJsonBean<Map<String, Object>> getSettlementProcessDetail(Long settlementId);

    /**
     * 创建结算规则
     */
    MyJsonBean<Map<String, Object>> createSettlementProcess(Map<String, Object> processData);

    /**
     * 更新结算规则
     */
    MyJsonBean<Map<String, Object>> updateSettlementProcess(Long settlementId, Map<String, Object> processData);

    /**
     * 删除结算规则
     */
    MyJsonBean<Boolean> deleteSettlementProcess(Long settlementId);

    /**
     * 执行结算
     */
    MyJsonBean<Map<String, Object>> executeSettlement(InternalSettlementQueryParam param);

    /**
     * 生成结算凭证
     */
    MyJsonBean<Map<String, Object>> generateSettlementVoucher(Long settlementId);

    /**
     * 确认结算结果
     */
    MyJsonBean<Boolean> confirmSettlementResult(Long settlementId, Map<String, Object> confirmData);

    /**
     * 查询结算处理统计
     */
    MyJsonBean<Map<String, Object>> getSettlementProcessStats(InternalSettlementQueryParam param);

    // ==================== 资金管理 ====================

    /**
     * 分页查询资金管理列表
     */
    MyJsonBean<PageResult<Map<String, Object>>> getFundManagementList(InternalSettlementQueryParam param);

    /**
     * 查询资金管理详情
     */
    MyJsonBean<Map<String, Object>> getFundManagementDetail(Long allocationId);

    /**
     * 创建资金调配
     */
    MyJsonBean<Map<String, Object>> createFundManagement(Map<String, Object> allocationData);

    /**
     * 更新资金调配
     */
    MyJsonBean<Map<String, Object>> updateFundManagement(Long allocationId, Map<String, Object> allocationData);

    /**
     * 删除资金调配
     */
    MyJsonBean<Boolean> deleteFundManagement(Long allocationId);

    /**
     * 审批资金调配
     */
    MyJsonBean<Boolean> approveFundAllocation(Long allocationId, Map<String, Object> approvalData);

    /**
     * 执行资金调配
     */
    MyJsonBean<Boolean> executeFundAllocation(Long allocationId, Map<String, Object> executionData);

    /**
     * 计算利息
     */
    MyJsonBean<BigDecimal> calculateInterest(Long allocationId, LocalDate calculateDate);

    /**
     * 查询资金池余额
     */
    MyJsonBean<Map<String, Object>> getFundPoolBalance(Long centerId);

    /**
     * 资金效率分析
     */
    MyJsonBean<Map<String, Object>> analyzeFundEfficiency(InternalSettlementQueryParam param);

    /**
     * 查询资金管理统计
     */
    MyJsonBean<Map<String, Object>> getFundManagementStats(InternalSettlementQueryParam param);

    // ==================== 结算分析 ====================

    /**
     * 查询结算分析数据
     */
    MyJsonBean<Map<String, Object>> getSettlementAnalysis(InternalSettlementQueryParam param);

    /**
     * 结算效率分析
     */
    MyJsonBean<List<Map<String, Object>>> analyzeSettlementEfficiency(InternalSettlementQueryParam param);

    /**
     * 交易结构分析
     */
    MyJsonBean<List<Map<String, Object>>> analyzeTransactionStructure(InternalSettlementQueryParam param);

    /**
     * 利润贡献分析
     */
    MyJsonBean<List<Map<String, Object>>> analyzeProfitContribution(InternalSettlementQueryParam param);

    /**
     * 成本效益分析
     */
    MyJsonBean<List<Map<String, Object>>> analyzeCostBenefit(InternalSettlementQueryParam param);

    /**
     * 生成优化建议
     */
    MyJsonBean<List<Map<String, Object>>> generateOptimizationSuggestions(InternalSettlementQueryParam param);

    /**
     * 导出分析报告
     */
    MyJsonBean<Map<String, Object>> exportAnalysisReport(InternalSettlementQueryParam param);

    // ==================== 报表功能 ====================

    /**
     * 查询内部结算报表数据
     */
    MyJsonBean<List<Map<String, Object>>> getSettlementReportData(InternalSettlementQueryParam param);

    /**
     * 查询内部交易报表数据
     */
    MyJsonBean<List<Map<String, Object>>> getTransactionReportData(InternalSettlementQueryParam param);

    /**
     * 查询转移定价报表数据
     */
    MyJsonBean<List<Map<String, Object>>> getPricingReportData(InternalSettlementQueryParam param);

    /**
     * 查询利润中心报表数据
     */
    MyJsonBean<List<Map<String, Object>>> getProfitCenterReportData(InternalSettlementQueryParam param);

    /**
     * 查询资金管理报表数据
     */
    MyJsonBean<List<Map<String, Object>>> getFundManagementReportData(InternalSettlementQueryParam param);

    // ==================== 审计功能 ====================

    /**
     * 查询审计日志
     */
    MyJsonBean<PageResult<Map<String, Object>>> getAuditLogList(InternalSettlementQueryParam param);

    /**
     * 记录操作日志
     */
    MyJsonBean<Boolean> recordOperationLog(Long settlementId, String operation, String content);

    /**
     * 查询变更历史
     */
    MyJsonBean<List<Map<String, Object>>> getChangeHistory(Long settlementId);

    // ==================== 维护功能 ====================

    /**
     * 数据清理
     */
    MyJsonBean<Boolean> cleanupExpiredData(LocalDate beforeDate);

    /**
     * 数据归档
     */
    MyJsonBean<Boolean> archiveHistoryData(LocalDate beforeDate);

    /**
     * 重新计算统计数据
     */
    MyJsonBean<Boolean> recalculateStatistics(InternalSettlementQueryParam param);

    /**
     * 数据一致性检查
     */
    MyJsonBean<List<Map<String, Object>>> checkDataConsistency(InternalSettlementQueryParam param);
}
