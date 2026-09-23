package com.management.accountant.service.ts;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.management.accountant.entity.ts.TsInvoiceManagement;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 发票管理服务接口
 *
 * @author AI Assistant
 * @since 2025-01-27
 */
public interface TsInvoiceManagementService extends IService<TsInvoiceManagement> {

    // ==================== 基础CRUD操作 ====================

    /**
     * 创建发票
     */
    TsInvoiceManagement createInvoice(TsInvoiceManagement invoice);

    /**
     * 更新发票
     */
    TsInvoiceManagement updateInvoice(TsInvoiceManagement invoice);

    /**
     * 根据ID删除发票
     */
    boolean deleteInvoiceById(Long tenantId, Long invoiceId);

    /**
     * 根据ID查询发票详情
     */
    TsInvoiceManagement getInvoiceById(Long tenantId, Long invoiceId);

    /**
     * 分页查询发票列表
     */
    IPage<TsInvoiceManagement> getInvoicePage(Page<TsInvoiceManagement> page, 
                                             Long tenantId,
                                             String invoiceCode,
                                             String invoiceNumber,
                                             String invoiceType,
                                             String invoiceStatus,
                                             String sellerName,
                                             String buyerName,
                                             LocalDateTime startDate,
                                             LocalDateTime endDate,
                                             String riskLevel,
                                             String businessCategory);

    // ==================== 发票识别功能 ====================

    /**
     * 上传发票文件
     */
    TsInvoiceManagement uploadInvoiceFile(Long tenantId, String fileName, String filePath, Long fileSize, String fileType);

    /**
     * OCR识别发票
     */
    boolean recognizeInvoice(Long tenantId, Long invoiceId);

    /**
     * 批量OCR识别发票
     */
    Map<String, Object> batchRecognizeInvoices(Long tenantId, List<Long> invoiceIds);

    /**
     * 获取待识别发票列表
     */
    List<TsInvoiceManagement> getPendingOcrInvoices(Long tenantId, Integer limit);

    /**
     * 更新OCR识别结果
     */
    boolean updateOcrResult(Long tenantId, Long invoiceId, String ocrResult, BigDecimal confidence);

    // ==================== 发票验真功能 ====================

    /**
     * 验真发票
     */
    boolean verifyInvoice(Long tenantId, Long invoiceId);

    /**
     * 批量验真发票
     */
    Map<String, Object> batchVerifyInvoices(Long tenantId, List<Long> invoiceIds);

    /**
     * 获取待验真发票列表
     */
    List<TsInvoiceManagement> getPendingVerificationInvoices(Long tenantId, Integer limit);

    /**
     * 更新验真结果
     */
    boolean updateVerificationResult(Long tenantId, Long invoiceId, String verificationResult, String source);

    /**
     * 查询重复发票
     */
    List<TsInvoiceManagement> findDuplicateInvoices(Long tenantId, String invoiceCode, String invoiceNumber, Long excludeId);

    // ==================== 发票归档功能 ====================

    /**
     * 归档发票
     */
    boolean archiveInvoice(Long tenantId, Long invoiceId, String archivePath);

    /**
     * 批量归档发票
     */
    Map<String, Object> batchArchiveInvoices(Long tenantId, List<Long> invoiceIds, String archiveBasePath);

    /**
     * 获取待归档发票列表
     */
    List<TsInvoiceManagement> getPendingArchiveInvoices(Long tenantId, Integer limit);

    /**
     * 更新归档状态
     */
    boolean updateArchiveStatus(Long tenantId, Long invoiceId, String archiveStatus, String archivePath);

    // ==================== 风险识别功能 ====================

    /**
     * 风险评估
     */
    boolean assessInvoiceRisk(Long tenantId, Long invoiceId);

    /**
     * 批量风险评估
     */
    Map<String, Object> batchAssessInvoiceRisk(Long tenantId, List<Long> invoiceIds);

    /**
     * 获取高风险发票列表
     */
    List<TsInvoiceManagement> getHighRiskInvoices(Long tenantId, List<String> riskLevels);

    /**
     * 更新风险等级
     */
    boolean updateRiskLevel(Long tenantId, Long invoiceId, String riskLevel, String riskReason);

    // ==================== 发票审批功能 ====================

    /**
     * 提交审批
     */
    boolean submitForApproval(Long tenantId, Long invoiceId);

    /**
     * 审批发票
     */
    boolean approveInvoice(Long tenantId, Long invoiceId, Long approverId, String approverName, String comment);

    /**
     * 拒绝发票
     */
    boolean rejectInvoice(Long tenantId, Long invoiceId, Long approverId, String approverName, String comment);

    /**
     * 批量审批
     */
    Map<String, Object> batchApproveInvoices(Long tenantId, List<Long> invoiceIds, Long approverId, String approverName, String comment);

    // ==================== 统计分析功能 ====================

    /**
     * 获取发票统计概览
     */
    Map<String, Object> getInvoiceOverview(Long tenantId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 统计发票数量按状态分组
     */
    List<Map<String, Object>> countInvoicesByStatus(Long tenantId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 统计发票数量按类型分组
     */
    List<Map<String, Object>> countInvoicesByType(Long tenantId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 统计发票金额按月份分组
     */
    List<Map<String, Object>> sumAmountByMonth(Long tenantId, Integer year);

    /**
     * 统计发票金额按销售方分组
     */
    List<Map<String, Object>> sumAmountBySeller(Long tenantId, LocalDateTime startDate, LocalDateTime endDate, Integer limit);

    /**
     * 统计发票金额按购买方分组
     */
    List<Map<String, Object>> sumAmountByBuyer(Long tenantId, LocalDateTime startDate, LocalDateTime endDate, Integer limit);

    /**
     * 计算OCR识别成功率
     */
    Map<String, Object> calculateOcrSuccessRate(Long tenantId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 计算验真成功率
     */
    Map<String, Object> calculateVerificationSuccessRate(Long tenantId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 统计风险发票分布
     */
    List<Map<String, Object>> countInvoicesByRiskLevel(Long tenantId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 计算处理时长统计
     */
    Map<String, Object> calculateProcessingTimeStats(Long tenantId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 计算金额统计
     */
    Map<String, Object> calculateAmountStats(Long tenantId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 计算税额统计
     */
    Map<String, Object> calculateTaxAmountStats(Long tenantId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 获取发票数量趋势
     */
    List<Map<String, Object>> getInvoiceCountTrend(Long tenantId, LocalDateTime startDate, LocalDateTime endDate, String groupBy);

    /**
     * 获取发票金额趋势
     */
    List<Map<String, Object>> getInvoiceAmountTrend(Long tenantId, LocalDateTime startDate, LocalDateTime endDate, String groupBy);

    /**
     * 获取处理效率统计
     */
    Map<String, Object> getProcessingEfficiencyStats(Long tenantId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 获取发票质量统计
     */
    Map<String, Object> getInvoiceQualityStats(Long tenantId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 获取合规性统计
     */
    Map<String, Object> getComplianceStats(Long tenantId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 获取风险分析
     */
    Map<String, Object> getRiskAnalysis(Long tenantId, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 获取成本分析
     */
    Map<String, Object> getCostAnalysis(Long tenantId, LocalDateTime startDate, LocalDateTime endDate);

    // ==================== 批量操作功能 ====================

    /**
     * 批量创建发票
     */
    Map<String, Object> batchCreateInvoices(Long tenantId, List<TsInvoiceManagement> invoices);

    /**
     * 批量更新发票状态
     */
    boolean batchUpdateStatus(Long tenantId, List<Long> invoiceIds, String status);

    /**
     * 批量删除发票
     */
    boolean batchDeleteInvoices(Long tenantId, List<Long> invoiceIds);

    /**
     * 批量导入发票
     */
    Map<String, Object> batchImportInvoices(Long tenantId, List<Map<String, Object>> invoiceData);

    /**
     * 批量导出发票
     */
    List<Map<String, Object>> batchExportInvoices(Long tenantId, List<Long> invoiceIds);

    // ==================== 系统维护功能 ====================

    /**
     * 获取异常发票列表
     */
    List<TsInvoiceManagement> getAbnormalInvoices(Long tenantId, List<String> abnormalTypes);

    /**
     * 获取需要重试的发票列表
     */
    List<TsInvoiceManagement> getRetryInvoices(Long tenantId);

    /**
     * 重试处理发票
     */
    boolean retryProcessInvoice(Long tenantId, Long invoiceId);

    /**
     * 清理过期发票
     */
    int cleanExpiredInvoices(Long tenantId, LocalDateTime expiredDate);

    /**
     * 系统健康检查
     */
    Map<String, Object> healthCheck(Long tenantId);

    /**
     * 获取系统性能指标
     */
    Map<String, Object> getPerformanceMetrics(Long tenantId);

    // ==================== 查询功能 ====================

    /**
     * 根据发票代码和号码查询发票
     */
    TsInvoiceManagement getInvoiceByCodeAndNumber(Long tenantId, String invoiceCode, String invoiceNumber);

    /**
     * 根据销售方税号查询发票列表
     */
    List<TsInvoiceManagement> getInvoicesBySellerTaxNumber(Long tenantId, String sellerTaxNumber);

    /**
     * 根据购买方税号查询发票列表
     */
    List<TsInvoiceManagement> getInvoicesByBuyerTaxNumber(Long tenantId, String buyerTaxNumber);

    /**
     * 高级搜索发票
     */
    IPage<TsInvoiceManagement> advancedSearchInvoices(Page<TsInvoiceManagement> page, Long tenantId, Map<String, Object> searchParams);

    /**
     * 全文搜索发票
     */
    IPage<TsInvoiceManagement> fullTextSearchInvoices(Page<TsInvoiceManagement> page, Long tenantId, String keyword);
}
