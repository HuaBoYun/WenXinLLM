package com.financial.sharing.controller;

import com.financial.sharing.service.InternalSettlementService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.InternalSettlementQueryParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 内部结算控制器
 *
 * @author system
 * @since 2024-12-19
 */
@Slf4j
@RestController
@RequestMapping("/internal-settlement")
@Api(tags = "内部结算管理")
public class InternalSettlementController {

    @Autowired
    private InternalSettlementService internalSettlementService;

    // ==================== 基础CRUD操作 ====================

    @GetMapping("/list")
    @ApiOperation("分页查询内部结算列表")
    public MyJsonBean<PageResult<Map<String, Object>>> getInternalSettlementList(InternalSettlementQueryParam param) {
        return internalSettlementService.getInternalSettlementList(param);
    }

    @GetMapping("/{settlementId}")
    @ApiOperation("查询内部结算详情")
    public MyJsonBean<Map<String, Object>> getInternalSettlementDetail(
            @ApiParam("结算ID") @PathVariable Long settlementId) {
        return internalSettlementService.getInternalSettlementDetail(settlementId);
    }

    @PostMapping
    @ApiOperation("创建内部结算记录")
    public MyJsonBean<Map<String, Object>> createInternalSettlement(
            @ApiParam("结算数据") @RequestBody Map<String, Object> settlementData) {
        return internalSettlementService.createInternalSettlement(settlementData);
    }

    @PutMapping("/{settlementId}")
    @ApiOperation("更新内部结算记录")
    public MyJsonBean<Map<String, Object>> updateInternalSettlement(
            @ApiParam("结算ID") @PathVariable Long settlementId,
            @ApiParam("结算数据") @RequestBody Map<String, Object> settlementData) {
        return internalSettlementService.updateInternalSettlement(settlementId, settlementData);
    }

    @DeleteMapping("/{settlementId}")
    @ApiOperation("删除内部结算记录")
    public MyJsonBean<Boolean> deleteInternalSettlement(
            @ApiParam("结算ID") @PathVariable Long settlementId) {
        return internalSettlementService.deleteInternalSettlement(settlementId);
    }

    @DeleteMapping("/batch")
    @ApiOperation("批量删除内部结算记录")
    public MyJsonBean<Boolean> batchDeleteInternalSettlement(
            @ApiParam("结算ID列表") @RequestBody List<Long> settlementIds) {
        return internalSettlementService.batchDeleteInternalSettlement(settlementIds);
    }

    @GetMapping("/stats")
    @ApiOperation("查询内部结算统计信息")
    public MyJsonBean<Map<String, Object>> getInternalSettlementStats(InternalSettlementQueryParam param) {
        return internalSettlementService.getInternalSettlementStats(param);
    }

    // ==================== 内部交易管理 ====================

    @GetMapping("/transaction/list")
    @ApiOperation("分页查询内部交易列表")
    public MyJsonBean<PageResult<Map<String, Object>>> getInternalTransactionList(InternalSettlementQueryParam param) {
        return internalSettlementService.getInternalTransactionList(param);
    }

    @GetMapping("/transaction/{transactionId}")
    @ApiOperation("查询内部交易详情")
    public MyJsonBean<Map<String, Object>> getInternalTransactionDetail(
            @ApiParam("交易ID") @PathVariable Long transactionId) {
        return internalSettlementService.getInternalTransactionDetail(transactionId);
    }

    @PostMapping("/transaction")
    @ApiOperation("创建内部交易")
    public MyJsonBean<Map<String, Object>> createInternalTransaction(
            @ApiParam("交易数据") @RequestBody Map<String, Object> transactionData) {
        return internalSettlementService.createInternalTransaction(transactionData);
    }

    @PutMapping("/transaction/{transactionId}")
    @ApiOperation("更新内部交易")
    public MyJsonBean<Map<String, Object>> updateInternalTransaction(
            @ApiParam("交易ID") @PathVariable Long transactionId,
            @ApiParam("交易数据") @RequestBody Map<String, Object> transactionData) {
        return internalSettlementService.updateInternalTransaction(transactionId, transactionData);
    }

    @DeleteMapping("/transaction/{transactionId}")
    @ApiOperation("删除内部交易")
    public MyJsonBean<Boolean> deleteInternalTransaction(
            @ApiParam("交易ID") @PathVariable Long transactionId) {
        return internalSettlementService.deleteInternalTransaction(transactionId);
    }

    @PostMapping("/transaction/{transactionId}/approve")
    @ApiOperation("审批内部交易")
    public MyJsonBean<Boolean> approveInternalTransaction(
            @ApiParam("交易ID") @PathVariable Long transactionId,
            @ApiParam("审批数据") @RequestBody Map<String, Object> approvalData) {
        return internalSettlementService.approveInternalTransaction(transactionId, approvalData);
    }

    @PostMapping("/transaction/batch-approve")
    @ApiOperation("批量审批内部交易")
    public MyJsonBean<Boolean> batchApproveInternalTransaction(
            @ApiParam("交易ID列表") @RequestParam List<Long> transactionIds,
            @ApiParam("审批数据") @RequestBody Map<String, Object> approvalData) {
        return internalSettlementService.batchApproveInternalTransaction(transactionIds, approvalData);
    }

    @PostMapping("/transaction/{transactionId}/confirm")
    @ApiOperation("确认内部交易")
    public MyJsonBean<Boolean> confirmInternalTransaction(
            @ApiParam("交易ID") @PathVariable Long transactionId,
            @ApiParam("确认数据") @RequestBody Map<String, Object> confirmData) {
        return internalSettlementService.confirmInternalTransaction(transactionId, confirmData);
    }

    @PostMapping("/transaction/{transactionId}/settle")
    @ApiOperation("结算内部交易")
    public MyJsonBean<Boolean> settleInternalTransaction(
            @ApiParam("交易ID") @PathVariable Long transactionId,
            @ApiParam("结算数据") @RequestBody Map<String, Object> settlementData) {
        return internalSettlementService.settleInternalTransaction(transactionId, settlementData);
    }

    @GetMapping("/transaction/stats")
    @ApiOperation("查询内部交易统计")
    public MyJsonBean<Map<String, Object>> getInternalTransactionStats(InternalSettlementQueryParam param) {
        return internalSettlementService.getInternalTransactionStats(param);
    }

    // ==================== 转移定价管理 ====================

    @GetMapping("/pricing/list")
    @ApiOperation("分页查询转移定价策略列表")
    public MyJsonBean<PageResult<Map<String, Object>>> getTransferPricingList(InternalSettlementQueryParam param) {
        return internalSettlementService.getTransferPricingList(param);
    }

    @GetMapping("/pricing/{policyId}")
    @ApiOperation("查询转移定价策略详情")
    public MyJsonBean<Map<String, Object>> getTransferPricingDetail(
            @ApiParam("策略ID") @PathVariable Long policyId) {
        return internalSettlementService.getTransferPricingDetail(policyId);
    }

    @PostMapping("/pricing")
    @ApiOperation("创建转移定价策略")
    public MyJsonBean<Map<String, Object>> createTransferPricing(
            @ApiParam("策略数据") @RequestBody Map<String, Object> policyData) {
        return internalSettlementService.createTransferPricing(policyData);
    }

    @PutMapping("/pricing/{policyId}")
    @ApiOperation("更新转移定价策略")
    public MyJsonBean<Map<String, Object>> updateTransferPricing(
            @ApiParam("策略ID") @PathVariable Long policyId,
            @ApiParam("策略数据") @RequestBody Map<String, Object> policyData) {
        return internalSettlementService.updateTransferPricing(policyId, policyData);
    }

    @DeleteMapping("/pricing/{policyId}")
    @ApiOperation("删除转移定价策略")
    public MyJsonBean<Boolean> deleteTransferPricing(
            @ApiParam("策略ID") @PathVariable Long policyId) {
        return internalSettlementService.deleteTransferPricing(policyId);
    }

    @PostMapping("/pricing/{policyId}/approve")
    @ApiOperation("审批转移定价策略")
    public MyJsonBean<Boolean> approveTransferPricing(
            @ApiParam("策略ID") @PathVariable Long policyId,
            @ApiParam("审批数据") @RequestBody Map<String, Object> approvalData) {
        return internalSettlementService.approveTransferPricing(policyId, approvalData);
    }

    @PostMapping("/pricing/batch-approve")
    @ApiOperation("批量审批转移定价策略")
    public MyJsonBean<Boolean> batchApproveTransferPricing(
            @ApiParam("策略ID列表") @RequestParam List<Long> policyIds,
            @ApiParam("审批数据") @RequestBody Map<String, Object> approvalData) {
        return internalSettlementService.batchApproveTransferPricing(policyIds, approvalData);
    }

    @PostMapping("/pricing/{policyId}/adjust")
    @ApiOperation("价格调整")
    public MyJsonBean<Boolean> adjustTransferPrice(
            @ApiParam("策略ID") @PathVariable Long policyId,
            @ApiParam("调整数据") @RequestBody Map<String, Object> adjustmentData) {
        return internalSettlementService.adjustTransferPrice(policyId, adjustmentData);
    }

    @GetMapping("/pricing/active")
    @ApiOperation("查询有效的定价策略")
    public MyJsonBean<List<Map<String, Object>>> getActivePricingPolicies(
            @ApiParam("产品名称") @RequestParam(required = false) String productName,
            @ApiParam("生效日期") @RequestParam(required = false) LocalDate effectiveDate) {
        return internalSettlementService.getActivePricingPolicies(productName, effectiveDate);
    }

    @GetMapping("/pricing/stats")
    @ApiOperation("查询转移定价统计")
    public MyJsonBean<Map<String, Object>> getTransferPricingStats(InternalSettlementQueryParam param) {
        return internalSettlementService.getTransferPricingStats(param);
    }

    // ==================== 利润中心管理 ====================

    @GetMapping("/profit-center/list")
    @ApiOperation("分页查询利润中心列表")
    public MyJsonBean<PageResult<Map<String, Object>>> getProfitCenterList(InternalSettlementQueryParam param) {
        return internalSettlementService.getProfitCenterList(param);
    }

    @GetMapping("/profit-center/{centerId}")
    @ApiOperation("查询利润中心详情")
    public MyJsonBean<Map<String, Object>> getProfitCenterDetail(
            @ApiParam("中心ID") @PathVariable Long centerId) {
        return internalSettlementService.getProfitCenterDetail(centerId);
    }

    @PostMapping("/profit-center")
    @ApiOperation("创建利润中心")
    public MyJsonBean<Map<String, Object>> createProfitCenter(
            @ApiParam("中心数据") @RequestBody Map<String, Object> centerData) {
        return internalSettlementService.createProfitCenter(centerData);
    }

    @PutMapping("/profit-center/{centerId}")
    @ApiOperation("更新利润中心")
    public MyJsonBean<Map<String, Object>> updateProfitCenter(
            @ApiParam("中心ID") @PathVariable Long centerId,
            @ApiParam("中心数据") @RequestBody Map<String, Object> centerData) {
        return internalSettlementService.updateProfitCenter(centerId, centerData);
    }

    @DeleteMapping("/profit-center/{centerId}")
    @ApiOperation("删除利润中心")
    public MyJsonBean<Boolean> deleteProfitCenter(
            @ApiParam("中心ID") @PathVariable Long centerId) {
        return internalSettlementService.deleteProfitCenter(centerId);
    }

    @GetMapping("/profit-center/tree")
    @ApiOperation("查询利润中心层级结构")
    public MyJsonBean<List<Map<String, Object>>> getProfitCenterTree(
            @ApiParam("父级ID") @RequestParam(required = false) Long parentId) {
        return internalSettlementService.getProfitCenterTree(parentId);
    }

    @GetMapping("/profit-center/{centerId}/performance")
    @ApiOperation("计算利润中心绩效")
    public MyJsonBean<Map<String, Object>> calculateCenterPerformance(
            @ApiParam("中心ID") @PathVariable Long centerId,
            @ApiParam("开始日期") @RequestParam LocalDate startDate,
            @ApiParam("结束日期") @RequestParam LocalDate endDate) {
        return internalSettlementService.calculateCenterPerformance(centerId, startDate, endDate);
    }

    @PostMapping("/profit-center/{centerId}/analyze")
    @ApiOperation("利润分析")
    public MyJsonBean<Map<String, Object>> analyzeProfitCenter(
            @ApiParam("中心ID") @PathVariable Long centerId,
            @ApiParam("分析参数") @RequestBody InternalSettlementQueryParam param) {
        return internalSettlementService.analyzeProfitCenter(centerId, param);
    }

    @PostMapping("/profit-center/{centerId}/evaluate")
    @ApiOperation("绩效评价")
    public MyJsonBean<Map<String, Object>> evaluateCenterPerformance(
            @ApiParam("中心ID") @PathVariable Long centerId,
            @ApiParam("评价参数") @RequestBody InternalSettlementQueryParam param) {
        return internalSettlementService.evaluateCenterPerformance(centerId, param);
    }

    @GetMapping("/profit-center/stats")
    @ApiOperation("查询利润中心统计")
    public MyJsonBean<Map<String, Object>> getProfitCenterStats(InternalSettlementQueryParam param) {
        return internalSettlementService.getProfitCenterStats(param);
    }

    // ==================== 内部结算处理 ====================

    @GetMapping("/process/list")
    @ApiOperation("分页查询结算处理列表")
    public MyJsonBean<PageResult<Map<String, Object>>> getSettlementProcessList(InternalSettlementQueryParam param) {
        return internalSettlementService.getSettlementProcessList(param);
    }

    @GetMapping("/process/{settlementId}")
    @ApiOperation("查询结算处理详情")
    public MyJsonBean<Map<String, Object>> getSettlementProcessDetail(
            @ApiParam("结算ID") @PathVariable Long settlementId) {
        return internalSettlementService.getSettlementProcessDetail(settlementId);
    }

    @PostMapping("/process")
    @ApiOperation("创建结算规则")
    public MyJsonBean<Map<String, Object>> createSettlementProcess(
            @ApiParam("处理数据") @RequestBody Map<String, Object> processData) {
        return internalSettlementService.createSettlementProcess(processData);
    }

    @PutMapping("/process/{settlementId}")
    @ApiOperation("更新结算规则")
    public MyJsonBean<Map<String, Object>> updateSettlementProcess(
            @ApiParam("结算ID") @PathVariable Long settlementId,
            @ApiParam("处理数据") @RequestBody Map<String, Object> processData) {
        return internalSettlementService.updateSettlementProcess(settlementId, processData);
    }

    @DeleteMapping("/process/{settlementId}")
    @ApiOperation("删除结算规则")
    public MyJsonBean<Boolean> deleteSettlementProcess(
            @ApiParam("结算ID") @PathVariable Long settlementId) {
        return internalSettlementService.deleteSettlementProcess(settlementId);
    }

    @PostMapping("/process/execute")
    @ApiOperation("执行结算")
    public MyJsonBean<Map<String, Object>> executeSettlement(
            @ApiParam("执行参数") @RequestBody InternalSettlementQueryParam param) {
        return internalSettlementService.executeSettlement(param);
    }

    @PostMapping("/process/{settlementId}/voucher")
    @ApiOperation("生成结算凭证")
    public MyJsonBean<Map<String, Object>> generateSettlementVoucher(
            @ApiParam("结算ID") @PathVariable Long settlementId) {
        return internalSettlementService.generateSettlementVoucher(settlementId);
    }

    @PostMapping("/process/{settlementId}/confirm")
    @ApiOperation("确认结算结果")
    public MyJsonBean<Boolean> confirmSettlementResult(
            @ApiParam("结算ID") @PathVariable Long settlementId,
            @ApiParam("确认数据") @RequestBody Map<String, Object> confirmData) {
        return internalSettlementService.confirmSettlementResult(settlementId, confirmData);
    }

    @GetMapping("/process/stats")
    @ApiOperation("查询结算处理统计")
    public MyJsonBean<Map<String, Object>> getSettlementProcessStats(InternalSettlementQueryParam param) {
        return internalSettlementService.getSettlementProcessStats(param);
    }

    // ==================== 资金管理 ====================

    @GetMapping("/fund/list")
    @ApiOperation("分页查询资金管理列表")
    public MyJsonBean<PageResult<Map<String, Object>>> getFundManagementList(InternalSettlementQueryParam param) {
        return internalSettlementService.getFundManagementList(param);
    }

    @GetMapping("/fund/{allocationId}")
    @ApiOperation("查询资金管理详情")
    public MyJsonBean<Map<String, Object>> getFundManagementDetail(
            @ApiParam("调配ID") @PathVariable Long allocationId) {
        return internalSettlementService.getFundManagementDetail(allocationId);
    }

    @PostMapping("/fund")
    @ApiOperation("创建资金调配")
    public MyJsonBean<Map<String, Object>> createFundManagement(
            @ApiParam("调配数据") @RequestBody Map<String, Object> allocationData) {
        return internalSettlementService.createFundManagement(allocationData);
    }

    @PutMapping("/fund/{allocationId}")
    @ApiOperation("更新资金调配")
    public MyJsonBean<Map<String, Object>> updateFundManagement(
            @ApiParam("调配ID") @PathVariable Long allocationId,
            @ApiParam("调配数据") @RequestBody Map<String, Object> allocationData) {
        return internalSettlementService.updateFundManagement(allocationId, allocationData);
    }

    @DeleteMapping("/fund/{allocationId}")
    @ApiOperation("删除资金调配")
    public MyJsonBean<Boolean> deleteFundManagement(
            @ApiParam("调配ID") @PathVariable Long allocationId) {
        return internalSettlementService.deleteFundManagement(allocationId);
    }

    @PostMapping("/fund/{allocationId}/approve")
    @ApiOperation("审批资金调配")
    public MyJsonBean<Boolean> approveFundAllocation(
            @ApiParam("调配ID") @PathVariable Long allocationId,
            @ApiParam("审批数据") @RequestBody Map<String, Object> approvalData) {
        return internalSettlementService.approveFundAllocation(allocationId, approvalData);
    }

    @PostMapping("/fund/{allocationId}/execute")
    @ApiOperation("执行资金调配")
    public MyJsonBean<Boolean> executeFundAllocation(
            @ApiParam("调配ID") @PathVariable Long allocationId,
            @ApiParam("执行数据") @RequestBody Map<String, Object> executionData) {
        return internalSettlementService.executeFundAllocation(allocationId, executionData);
    }

    @GetMapping("/fund/{allocationId}/interest")
    @ApiOperation("计算利息")
    public MyJsonBean<BigDecimal> calculateInterest(
            @ApiParam("调配ID") @PathVariable Long allocationId,
            @ApiParam("计算日期") @RequestParam LocalDate calculateDate) {
        return internalSettlementService.calculateInterest(allocationId, calculateDate);
    }

    @GetMapping("/fund/pool/{centerId}/balance")
    @ApiOperation("查询资金池余额")
    public MyJsonBean<Map<String, Object>> getFundPoolBalance(
            @ApiParam("中心ID") @PathVariable Long centerId) {
        return internalSettlementService.getFundPoolBalance(centerId);
    }

    @PostMapping("/fund/analyze")
    @ApiOperation("资金效率分析")
    public MyJsonBean<Map<String, Object>> analyzeFundEfficiency(
            @ApiParam("分析参数") @RequestBody InternalSettlementQueryParam param) {
        return internalSettlementService.analyzeFundEfficiency(param);
    }

    @GetMapping("/fund/stats")
    @ApiOperation("查询资金管理统计")
    public MyJsonBean<Map<String, Object>> getFundManagementStats(InternalSettlementQueryParam param) {
        return internalSettlementService.getFundManagementStats(param);
    }

    // ==================== 结算分析 ====================

    @PostMapping("/analysis")
    @ApiOperation("查询结算分析数据")
    public MyJsonBean<Map<String, Object>> getSettlementAnalysis(
            @ApiParam("分析参数") @RequestBody InternalSettlementQueryParam param) {
        return internalSettlementService.getSettlementAnalysis(param);
    }

    @PostMapping("/analysis/efficiency")
    @ApiOperation("结算效率分析")
    public MyJsonBean<List<Map<String, Object>>> analyzeSettlementEfficiency(
            @ApiParam("分析参数") @RequestBody InternalSettlementQueryParam param) {
        return internalSettlementService.analyzeSettlementEfficiency(param);
    }

    @PostMapping("/analysis/transaction-structure")
    @ApiOperation("交易结构分析")
    public MyJsonBean<List<Map<String, Object>>> analyzeTransactionStructure(
            @ApiParam("分析参数") @RequestBody InternalSettlementQueryParam param) {
        return internalSettlementService.analyzeTransactionStructure(param);
    }

    @PostMapping("/analysis/profit-contribution")
    @ApiOperation("利润贡献分析")
    public MyJsonBean<List<Map<String, Object>>> analyzeProfitContribution(
            @ApiParam("分析参数") @RequestBody InternalSettlementQueryParam param) {
        return internalSettlementService.analyzeProfitContribution(param);
    }

    @PostMapping("/analysis/cost-benefit")
    @ApiOperation("成本效益分析")
    public MyJsonBean<List<Map<String, Object>>> analyzeCostBenefit(
            @ApiParam("分析参数") @RequestBody InternalSettlementQueryParam param) {
        return internalSettlementService.analyzeCostBenefit(param);
    }

    @PostMapping("/analysis/optimization")
    @ApiOperation("生成优化建议")
    public MyJsonBean<List<Map<String, Object>>> generateOptimizationSuggestions(
            @ApiParam("分析参数") @RequestBody InternalSettlementQueryParam param) {
        return internalSettlementService.generateOptimizationSuggestions(param);
    }

    @PostMapping("/analysis/export")
    @ApiOperation("导出分析报告")
    public MyJsonBean<Map<String, Object>> exportAnalysisReport(
            @ApiParam("导出参数") @RequestBody InternalSettlementQueryParam param) {
        return internalSettlementService.exportAnalysisReport(param);
    }

    // ==================== 报表功能 ====================

    @PostMapping("/report/settlement")
    @ApiOperation("查询内部结算报表数据")
    public MyJsonBean<List<Map<String, Object>>> getSettlementReportData(
            @ApiParam("报表参数") @RequestBody InternalSettlementQueryParam param) {
        return internalSettlementService.getSettlementReportData(param);
    }

    @PostMapping("/report/transaction")
    @ApiOperation("查询内部交易报表数据")
    public MyJsonBean<List<Map<String, Object>>> getTransactionReportData(
            @ApiParam("报表参数") @RequestBody InternalSettlementQueryParam param) {
        return internalSettlementService.getTransactionReportData(param);
    }

    @PostMapping("/report/pricing")
    @ApiOperation("查询转移定价报表数据")
    public MyJsonBean<List<Map<String, Object>>> getPricingReportData(
            @ApiParam("报表参数") @RequestBody InternalSettlementQueryParam param) {
        return internalSettlementService.getPricingReportData(param);
    }

    @PostMapping("/report/profit-center")
    @ApiOperation("查询利润中心报表数据")
    public MyJsonBean<List<Map<String, Object>>> getProfitCenterReportData(
            @ApiParam("报表参数") @RequestBody InternalSettlementQueryParam param) {
        return internalSettlementService.getProfitCenterReportData(param);
    }

    @PostMapping("/report/fund")
    @ApiOperation("查询资金管理报表数据")
    public MyJsonBean<List<Map<String, Object>>> getFundManagementReportData(
            @ApiParam("报表参数") @RequestBody InternalSettlementQueryParam param) {
        return internalSettlementService.getFundManagementReportData(param);
    }

    // ==================== 审计功能 ====================

    @GetMapping("/audit/log")
    @ApiOperation("查询审计日志")
    public MyJsonBean<PageResult<Map<String, Object>>> getAuditLogList(InternalSettlementQueryParam param) {
        return internalSettlementService.getAuditLogList(param);
    }

    @PostMapping("/audit/log")
    @ApiOperation("记录操作日志")
    public MyJsonBean<Boolean> recordOperationLog(
            @ApiParam("结算ID") @RequestParam Long settlementId,
            @ApiParam("操作类型") @RequestParam String operation,
            @ApiParam("操作内容") @RequestParam String content) {
        return internalSettlementService.recordOperationLog(settlementId, operation, content);
    }

    @GetMapping("/audit/{settlementId}/history")
    @ApiOperation("查询变更历史")
    public MyJsonBean<List<Map<String, Object>>> getChangeHistory(
            @ApiParam("结算ID") @PathVariable Long settlementId) {
        return internalSettlementService.getChangeHistory(settlementId);
    }

    // ==================== 维护功能 ====================

    @PostMapping("/maintenance/cleanup")
    @ApiOperation("数据清理")
    public MyJsonBean<Boolean> cleanupExpiredData(
            @ApiParam("截止日期") @RequestParam LocalDate beforeDate) {
        return internalSettlementService.cleanupExpiredData(beforeDate);
    }

    @PostMapping("/maintenance/archive")
    @ApiOperation("数据归档")
    public MyJsonBean<Boolean> archiveHistoryData(
            @ApiParam("截止日期") @RequestParam LocalDate beforeDate) {
        return internalSettlementService.archiveHistoryData(beforeDate);
    }

    @PostMapping("/maintenance/recalculate")
    @ApiOperation("重新计算统计数据")
    public MyJsonBean<Boolean> recalculateStatistics(
            @ApiParam("计算参数") @RequestBody InternalSettlementQueryParam param) {
        return internalSettlementService.recalculateStatistics(param);
    }

    @PostMapping("/maintenance/check")
    @ApiOperation("数据一致性检查")
    public MyJsonBean<List<Map<String, Object>>> checkDataConsistency(
            @ApiParam("检查参数") @RequestBody InternalSettlementQueryParam param) {
        return internalSettlementService.checkDataConsistency(param);
    }
}
