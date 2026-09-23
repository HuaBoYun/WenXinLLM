package com.management.accountant.service.ts.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.management.accountant.entity.ts.TsInvoiceManagement;
import com.management.accountant.mapper.ts.TsInvoiceManagementMapper;
import com.management.accountant.service.ts.TsInvoiceManagementService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 发票管理服务实现类
 *
 * @author AI Assistant
 * @since 2025-01-27
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class TsInvoiceManagementServiceImpl extends ServiceImpl<TsInvoiceManagementMapper, TsInvoiceManagement> 
        implements TsInvoiceManagementService {

    // ==================== 基础CRUD操作 ====================

    @Override
    public TsInvoiceManagement createInvoice(TsInvoiceManagement invoice) {
        log.info("创建发票: {}", invoice.getInvoiceNumber());
        
        // 验证必填字段
        validateInvoiceData(invoice);
        
        // 检查重复发票
        if (StringUtils.hasText(invoice.getInvoiceCode()) && StringUtils.hasText(invoice.getInvoiceNumber())) {
            TsInvoiceManagement existing = getInvoiceByCodeAndNumber(
                invoice.getTenantId(), invoice.getInvoiceCode(), invoice.getInvoiceNumber());
            if (existing != null) {
                throw new RuntimeException("发票已存在: " + invoice.getInvoiceCode() + "-" + invoice.getInvoiceNumber());
            }
        }
        
        // 设置默认值
        if (invoice.getInvoiceStatus() == null) {
            invoice.setInvoiceStatus("DRAFT");
        }
        if (invoice.getOcrStatus() == null) {
            invoice.setOcrStatus("PENDING");
        }
        if (invoice.getVerificationStatus() == null) {
            invoice.setVerificationStatus("PENDING");
        }
        if (invoice.getArchiveStatus() == null) {
            invoice.setArchiveStatus("PENDING");
        }
        if (invoice.getProcessingStatus() == null) {
            invoice.setProcessingStatus("PENDING");
        }
        if (invoice.getRiskLevel() == null) {
            invoice.setRiskLevel("LOW");
        }
        if (invoice.getPriority() == null) {
            invoice.setPriority("NORMAL");
        }
        if (invoice.getRetryCount() == null) {
            invoice.setRetryCount(0);
        }
        if (invoice.getMaxRetryCount() == null) {
            invoice.setMaxRetryCount(3);
        }
        
        save(invoice);
        log.info("发票创建成功，ID: {}", invoice.getInvoiceId());
        return invoice;
    }

    @Override
    public TsInvoiceManagement updateInvoice(TsInvoiceManagement invoice) {
        log.info("更新发票: {}", invoice.getInvoiceId());
        
        // 验证发票是否存在
        TsInvoiceManagement existing = getInvoiceById(invoice.getTenantId(), invoice.getInvoiceId());
        if (existing == null) {
            throw new RuntimeException("发票不存在: " + invoice.getInvoiceId());
        }
        
        // 验证必填字段
        validateInvoiceData(invoice);
        
        updateById(invoice);
        log.info("发票更新成功: {}", invoice.getInvoiceId());
        return invoice;
    }

    @Override
    public boolean deleteInvoiceById(Long tenantId, Long invoiceId) {
        log.info("删除发票: {}", invoiceId);
        
        LambdaQueryWrapper<TsInvoiceManagement> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TsInvoiceManagement::getTenantId, tenantId)
               .eq(TsInvoiceManagement::getInvoiceId, invoiceId);
        
        boolean result = remove(wrapper);
        if (result) {
            log.info("发票删除成功: {}", invoiceId);
        } else {
            log.warn("发票删除失败: {}", invoiceId);
        }
        return result;
    }

    @Override
    public TsInvoiceManagement getInvoiceById(Long tenantId, Long invoiceId) {
        LambdaQueryWrapper<TsInvoiceManagement> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TsInvoiceManagement::getTenantId, tenantId)
               .eq(TsInvoiceManagement::getInvoiceId, invoiceId);
        return getOne(wrapper);
    }

    @Override
    public IPage<TsInvoiceManagement> getInvoicePage(Page<TsInvoiceManagement> page, 
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
                                                    String businessCategory) {
        return baseMapper.selectInvoiceManagementPage(page, tenantId, invoiceCode, invoiceNumber, 
                                                     invoiceType, invoiceStatus, sellerName, buyerName,
                                                     startDate, endDate, riskLevel, businessCategory);
    }

    // ==================== 发票识别功能 ====================

    @Override
    public TsInvoiceManagement uploadInvoiceFile(Long tenantId, String fileName, String filePath, Long fileSize, String fileType) {
        log.info("上传发票文件: {}", fileName);
        
        TsInvoiceManagement invoice = new TsInvoiceManagement();
        invoice.setTenantId(tenantId);
        invoice.setFileName(fileName);
        invoice.setFilePath(filePath);
        invoice.setFileSize(fileSize);
        invoice.setFileType(fileType);
        invoice.setInvoiceStatus("DRAFT");
        invoice.setOcrStatus("PENDING");
        invoice.setVerificationStatus("PENDING");
        invoice.setArchiveStatus("PENDING");
        invoice.setProcessingStatus("PENDING");
        invoice.setRiskLevel("LOW");
        invoice.setPriority("NORMAL");
        invoice.setRetryCount(0);
        invoice.setMaxRetryCount(3);
        
        save(invoice);
        log.info("发票文件上传成功，ID: {}", invoice.getInvoiceId());
        return invoice;
    }

    @Override
    public boolean recognizeInvoice(Long tenantId, Long invoiceId) {
        log.info("OCR识别发票: {}", invoiceId);
        
        TsInvoiceManagement invoice = getInvoiceById(tenantId, invoiceId);
        if (invoice == null) {
            throw new RuntimeException("发票不存在: " + invoiceId);
        }
        
        try {
            // 更新状态为识别中
            invoice.setOcrStatus("PROCESSING");
            invoice.setProcessingStatus("PROCESSING");
            updateById(invoice);
            
            // 模拟OCR识别过程
            Map<String, Object> ocrResult = performOcrRecognition(invoice);
            
            // 更新识别结果
            invoice.setOcrStatus("SUCCESS");
            invoice.setOcrResult(ocrResult.toString());
            invoice.setOcrConfidence(new BigDecimal("0.95"));
            invoice.setOcrTime(LocalDateTime.now());
            
            // 解析OCR结果并更新发票信息
            updateInvoiceFromOcrResult(invoice, ocrResult);
            
            updateById(invoice);
            log.info("发票OCR识别成功: {}", invoiceId);
            return true;
            
        } catch (Exception e) {
            log.error("发票OCR识别失败: {}", invoiceId, e);
            
            // 更新失败状态
            invoice.setOcrStatus("FAILED");
            invoice.setProcessingStatus("FAILED");
            invoice.setErrorMessage(e.getMessage());
            invoice.setRetryCount(invoice.getRetryCount() + 1);
            
            // 设置下次重试时间
            if (invoice.getRetryCount() < invoice.getMaxRetryCount()) {
                invoice.setNextRetryTime(LocalDateTime.now().plusMinutes(30));
            }
            
            updateById(invoice);
            return false;
        }
    }

    @Override
    public Map<String, Object> batchRecognizeInvoices(Long tenantId, List<Long> invoiceIds) {
        log.info("批量OCR识别发票，数量: {}", invoiceIds.size());
        
        Map<String, Object> result = new HashMap<>();
        int successCount = 0;
        int failCount = 0;
        List<String> errors = new ArrayList<>();
        
        for (Long invoiceId : invoiceIds) {
            try {
                if (recognizeInvoice(tenantId, invoiceId)) {
                    successCount++;
                } else {
                    failCount++;
                }
            } catch (Exception e) {
                failCount++;
                errors.add("发票ID " + invoiceId + ": " + e.getMessage());
            }
        }
        
        result.put("total", invoiceIds.size());
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("errors", errors);
        
        log.info("批量OCR识别完成，成功: {}, 失败: {}", successCount, failCount);
        return result;
    }

    @Override
    public List<TsInvoiceManagement> getPendingOcrInvoices(Long tenantId, Integer limit) {
        return baseMapper.selectPendingOcrInvoices(tenantId, limit);
    }

    @Override
    public boolean updateOcrResult(Long tenantId, Long invoiceId, String ocrResult, BigDecimal confidence) {
        TsInvoiceManagement invoice = getInvoiceById(tenantId, invoiceId);
        if (invoice == null) {
            return false;
        }
        
        invoice.setOcrResult(ocrResult);
        invoice.setOcrConfidence(confidence);
        invoice.setOcrTime(LocalDateTime.now());
        invoice.setOcrStatus("SUCCESS");
        
        return updateById(invoice);
    }

    // ==================== 发票验真功能 ====================

    @Override
    public boolean verifyInvoice(Long tenantId, Long invoiceId) {
        log.info("验真发票: {}", invoiceId);
        
        TsInvoiceManagement invoice = getInvoiceById(tenantId, invoiceId);
        if (invoice == null) {
            throw new RuntimeException("发票不存在: " + invoiceId);
        }
        
        try {
            // 更新状态为验真中
            invoice.setVerificationStatus("PROCESSING");
            updateById(invoice);
            
            // 模拟验真过程
            Map<String, Object> verificationResult = performInvoiceVerification(invoice);
            
            // 更新验真结果
            invoice.setVerificationStatus("SUCCESS");
            invoice.setVerificationResult(verificationResult.toString());
            invoice.setVerificationTime(LocalDateTime.now());
            invoice.setVerificationSource("TAX_BUREAU");
            
            updateById(invoice);
            log.info("发票验真成功: {}", invoiceId);
            return true;
            
        } catch (Exception e) {
            log.error("发票验真失败: {}", invoiceId, e);
            
            // 更新失败状态
            invoice.setVerificationStatus("FAILED");
            invoice.setErrorMessage(e.getMessage());
            invoice.setRetryCount(invoice.getRetryCount() + 1);
            
            // 设置下次重试时间
            if (invoice.getRetryCount() < invoice.getMaxRetryCount()) {
                invoice.setNextRetryTime(LocalDateTime.now().plusMinutes(60));
            }
            
            updateById(invoice);
            return false;
        }
    }

    @Override
    public Map<String, Object> batchVerifyInvoices(Long tenantId, List<Long> invoiceIds) {
        log.info("批量验真发票，数量: {}", invoiceIds.size());
        
        Map<String, Object> result = new HashMap<>();
        int successCount = 0;
        int failCount = 0;
        List<String> errors = new ArrayList<>();
        
        for (Long invoiceId : invoiceIds) {
            try {
                if (verifyInvoice(tenantId, invoiceId)) {
                    successCount++;
                } else {
                    failCount++;
                }
            } catch (Exception e) {
                failCount++;
                errors.add("发票ID " + invoiceId + ": " + e.getMessage());
            }
        }
        
        result.put("total", invoiceIds.size());
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("errors", errors);
        
        log.info("批量验真完成，成功: {}, 失败: {}", successCount, failCount);
        return result;
    }

    @Override
    public List<TsInvoiceManagement> getPendingVerificationInvoices(Long tenantId, Integer limit) {
        return baseMapper.selectPendingVerificationInvoices(tenantId, limit);
    }

    @Override
    public boolean updateVerificationResult(Long tenantId, Long invoiceId, String verificationResult, String source) {
        TsInvoiceManagement invoice = getInvoiceById(tenantId, invoiceId);
        if (invoice == null) {
            return false;
        }
        
        invoice.setVerificationResult(verificationResult);
        invoice.setVerificationSource(source);
        invoice.setVerificationTime(LocalDateTime.now());
        invoice.setVerificationStatus("SUCCESS");
        
        return updateById(invoice);
    }

    @Override
    public List<TsInvoiceManagement> findDuplicateInvoices(Long tenantId, String invoiceCode, String invoiceNumber, Long excludeId) {
        return baseMapper.selectDuplicateInvoices(tenantId, invoiceCode, invoiceNumber, excludeId);
    }

    // ==================== 私有辅助方法 ====================

    private void validateInvoiceData(TsInvoiceManagement invoice) {
        if (invoice.getTenantId() == null) {
            throw new RuntimeException("租户ID不能为空");
        }
    }

    private Map<String, Object> performOcrRecognition(TsInvoiceManagement invoice) {
        // 模拟OCR识别过程
        Map<String, Object> result = new HashMap<>();
        result.put("invoiceCode", "144001900111");
        result.put("invoiceNumber", "12345678");
        result.put("invoiceDate", LocalDateTime.now().minusDays(1));
        result.put("sellerName", "测试销售方");
        result.put("buyerName", "测试购买方");
        result.put("totalAmount", new BigDecimal("1000.00"));
        result.put("confidence", new BigDecimal("0.95"));
        return result;
    }

    private void updateInvoiceFromOcrResult(TsInvoiceManagement invoice, Map<String, Object> ocrResult) {
        // 从OCR结果更新发票信息
        if (ocrResult.containsKey("invoiceCode")) {
            invoice.setInvoiceCode((String) ocrResult.get("invoiceCode"));
        }
        if (ocrResult.containsKey("invoiceNumber")) {
            invoice.setInvoiceNumber((String) ocrResult.get("invoiceNumber"));
        }
        if (ocrResult.containsKey("sellerName")) {
            invoice.setSellerName((String) ocrResult.get("sellerName"));
        }
        if (ocrResult.containsKey("buyerName")) {
            invoice.setBuyerName((String) ocrResult.get("buyerName"));
        }
        if (ocrResult.containsKey("totalAmount")) {
            invoice.setTotalAmount((BigDecimal) ocrResult.get("totalAmount"));
        }
        
        // 更新状态
        invoice.setInvoiceStatus("RECOGNIZED");
    }

    private Map<String, Object> performInvoiceVerification(TsInvoiceManagement invoice) {
        // 模拟验真过程
        Map<String, Object> result = new HashMap<>();
        result.put("valid", true);
        result.put("status", "正常");
        result.put("verificationTime", LocalDateTime.now());
        return result;
    }

    // ==================== 其他接口方法的基础实现 ====================

    @Override
    public boolean archiveInvoice(Long tenantId, Long invoiceId, String archivePath) {
        // 基础实现，后续可扩展
        return false;
    }

    @Override
    public Map<String, Object> batchArchiveInvoices(Long tenantId, List<Long> invoiceIds, String archiveBasePath) {
        // 基础实现，后续可扩展
        return new HashMap<>();
    }

    @Override
    public List<TsInvoiceManagement> getPendingArchiveInvoices(Long tenantId, Integer limit) {
        return baseMapper.selectPendingArchiveInvoices(tenantId, limit);
    }

    @Override
    public boolean updateArchiveStatus(Long tenantId, Long invoiceId, String archiveStatus, String archivePath) {
        // 基础实现，后续可扩展
        return false;
    }

    @Override
    public boolean assessInvoiceRisk(Long tenantId, Long invoiceId) {
        // 基础实现，后续可扩展
        return false;
    }

    @Override
    public Map<String, Object> batchAssessInvoiceRisk(Long tenantId, List<Long> invoiceIds) {
        // 基础实现，后续可扩展
        return new HashMap<>();
    }

    @Override
    public List<TsInvoiceManagement> getHighRiskInvoices(Long tenantId, List<String> riskLevels) {
        return baseMapper.selectHighRiskInvoices(tenantId, riskLevels);
    }

    @Override
    public boolean updateRiskLevel(Long tenantId, Long invoiceId, String riskLevel, String riskReason) {
        // 基础实现，后续可扩展
        return false;
    }

    @Override
    public boolean submitForApproval(Long tenantId, Long invoiceId) {
        // 基础实现，后续可扩展
        return false;
    }

    @Override
    public boolean approveInvoice(Long tenantId, Long invoiceId, Long approverId, String approverName, String comment) {
        // 基础实现，后续可扩展
        return false;
    }

    @Override
    public boolean rejectInvoice(Long tenantId, Long invoiceId, Long approverId, String approverName, String comment) {
        // 基础实现，后续可扩展
        return false;
    }

    @Override
    public Map<String, Object> batchApproveInvoices(Long tenantId, List<Long> invoiceIds, Long approverId, String approverName, String comment) {
        // 基础实现，后续可扩展
        return new HashMap<>();
    }

    @Override
    public Map<String, Object> getInvoiceOverview(Long tenantId, LocalDateTime startDate, LocalDateTime endDate) {
        return baseMapper.getInvoiceOverview(tenantId, startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> countInvoicesByStatus(Long tenantId, LocalDateTime startDate, LocalDateTime endDate) {
        return baseMapper.countInvoicesByStatus(tenantId, startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> countInvoicesByType(Long tenantId, LocalDateTime startDate, LocalDateTime endDate) {
        return baseMapper.countInvoicesByType(tenantId, startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> sumAmountByMonth(Long tenantId, Integer year) {
        return baseMapper.sumAmountByMonth(tenantId, year);
    }

    @Override
    public List<Map<String, Object>> sumAmountBySeller(Long tenantId, LocalDateTime startDate, LocalDateTime endDate, Integer limit) {
        return baseMapper.sumAmountBySeller(tenantId, startDate, endDate, limit);
    }

    @Override
    public List<Map<String, Object>> sumAmountByBuyer(Long tenantId, LocalDateTime startDate, LocalDateTime endDate, Integer limit) {
        return baseMapper.sumAmountByBuyer(tenantId, startDate, endDate, limit);
    }

    @Override
    public Map<String, Object> calculateOcrSuccessRate(Long tenantId, LocalDateTime startDate, LocalDateTime endDate) {
        return baseMapper.calculateOcrSuccessRate(tenantId, startDate, endDate);
    }

    @Override
    public Map<String, Object> calculateVerificationSuccessRate(Long tenantId, LocalDateTime startDate, LocalDateTime endDate) {
        return baseMapper.calculateVerificationSuccessRate(tenantId, startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> countInvoicesByRiskLevel(Long tenantId, LocalDateTime startDate, LocalDateTime endDate) {
        return baseMapper.countInvoicesByRiskLevel(tenantId, startDate, endDate);
    }

    @Override
    public Map<String, Object> calculateProcessingTimeStats(Long tenantId, LocalDateTime startDate, LocalDateTime endDate) {
        return baseMapper.calculateProcessingTimeStats(tenantId, startDate, endDate);
    }

    @Override
    public Map<String, Object> calculateAmountStats(Long tenantId, LocalDateTime startDate, LocalDateTime endDate) {
        return baseMapper.calculateAmountStats(tenantId, startDate, endDate);
    }

    @Override
    public Map<String, Object> calculateTaxAmountStats(Long tenantId, LocalDateTime startDate, LocalDateTime endDate) {
        return baseMapper.calculateTaxAmountStats(tenantId, startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> getInvoiceCountTrend(Long tenantId, LocalDateTime startDate, LocalDateTime endDate, String groupBy) {
        return baseMapper.getInvoiceCountTrend(tenantId, startDate, endDate, groupBy);
    }

    @Override
    public List<Map<String, Object>> getInvoiceAmountTrend(Long tenantId, LocalDateTime startDate, LocalDateTime endDate, String groupBy) {
        return baseMapper.getInvoiceAmountTrend(tenantId, startDate, endDate, groupBy);
    }

    @Override
    public Map<String, Object> getProcessingEfficiencyStats(Long tenantId, LocalDateTime startDate, LocalDateTime endDate) {
        return baseMapper.getProcessingEfficiencyStats(tenantId, startDate, endDate);
    }

    @Override
    public Map<String, Object> getInvoiceQualityStats(Long tenantId, LocalDateTime startDate, LocalDateTime endDate) {
        return baseMapper.getInvoiceQualityStats(tenantId, startDate, endDate);
    }

    @Override
    public Map<String, Object> getComplianceStats(Long tenantId, LocalDateTime startDate, LocalDateTime endDate) {
        return baseMapper.getComplianceStats(tenantId, startDate, endDate);
    }

    @Override
    public Map<String, Object> getRiskAnalysis(Long tenantId, LocalDateTime startDate, LocalDateTime endDate) {
        return baseMapper.getRiskAnalysis(tenantId, startDate, endDate);
    }

    @Override
    public Map<String, Object> getCostAnalysis(Long tenantId, LocalDateTime startDate, LocalDateTime endDate) {
        return baseMapper.getCostAnalysis(tenantId, startDate, endDate);
    }

    @Override
    public Map<String, Object> batchCreateInvoices(Long tenantId, List<TsInvoiceManagement> invoices) {
        // 基础实现，后续可扩展
        return new HashMap<>();
    }

    @Override
    public boolean batchUpdateStatus(Long tenantId, List<Long> invoiceIds, String status) {
        return baseMapper.batchUpdateStatus(tenantId, invoiceIds, status, null, null) > 0;
    }

    @Override
    public boolean batchDeleteInvoices(Long tenantId, List<Long> invoiceIds) {
        return baseMapper.batchDeleteInvoices(tenantId, invoiceIds, null, null) > 0;
    }

    @Override
    public Map<String, Object> batchImportInvoices(Long tenantId, List<Map<String, Object>> invoiceData) {
        // 基础实现，后续可扩展
        return new HashMap<>();
    }

    @Override
    public List<Map<String, Object>> batchExportInvoices(Long tenantId, List<Long> invoiceIds) {
        // 基础实现，后续可扩展
        return new ArrayList<>();
    }

    @Override
    public List<TsInvoiceManagement> getAbnormalInvoices(Long tenantId, List<String> abnormalTypes) {
        return baseMapper.selectAbnormalInvoices(tenantId, abnormalTypes);
    }

    @Override
    public List<TsInvoiceManagement> getRetryInvoices(Long tenantId) {
        return baseMapper.selectRetryInvoices(tenantId, LocalDateTime.now());
    }

    @Override
    public boolean retryProcessInvoice(Long tenantId, Long invoiceId) {
        // 基础实现，后续可扩展
        return false;
    }

    @Override
    public int cleanExpiredInvoices(Long tenantId, LocalDateTime expiredDate) {
        return baseMapper.cleanExpiredInvoices(tenantId, expiredDate);
    }

    @Override
    public Map<String, Object> healthCheck(Long tenantId) {
        // 基础实现，后续可扩展
        return new HashMap<>();
    }

    @Override
    public Map<String, Object> getPerformanceMetrics(Long tenantId) {
        // 基础实现，后续可扩展
        return new HashMap<>();
    }

    @Override
    public TsInvoiceManagement getInvoiceByCodeAndNumber(Long tenantId, String invoiceCode, String invoiceNumber) {
        return baseMapper.selectByInvoiceCodeAndNumber(tenantId, invoiceCode, invoiceNumber);
    }

    @Override
    public List<TsInvoiceManagement> getInvoicesBySellerTaxNumber(Long tenantId, String sellerTaxNumber) {
        return baseMapper.selectBySellerTaxNumber(tenantId, sellerTaxNumber);
    }

    @Override
    public List<TsInvoiceManagement> getInvoicesByBuyerTaxNumber(Long tenantId, String buyerTaxNumber) {
        return baseMapper.selectByBuyerTaxNumber(tenantId, buyerTaxNumber);
    }

    @Override
    public IPage<TsInvoiceManagement> advancedSearchInvoices(Page<TsInvoiceManagement> page, Long tenantId, Map<String, Object> searchParams) {
        // 基础实现，后续可扩展
        return page;
    }

    @Override
    public IPage<TsInvoiceManagement> fullTextSearchInvoices(Page<TsInvoiceManagement> page, Long tenantId, String keyword) {
        // 基础实现，后续可扩展
        return page;
    }
}
