package com.management.accountant.controller.ts;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.ts.TsInvoiceManagement;
import com.management.accountant.service.ts.TsInvoiceManagementService;
import com.management.accountant.util.MyJsonBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 发票管理控制器
 *
 * @author AI Assistant
 * @since 2025-01-27
 */
@Slf4j
@RestController
@RequestMapping("/accountant/ts/invoice-management")
@Api(tags = "发票管理")
public class TsInvoiceManagementController {

    @Autowired
    private TsInvoiceManagementService invoiceManagementService;

    // ==================== 基础CRUD操作 ====================

    @PostMapping("/create")
    @ApiOperation("创建发票")
    public MyJsonBean createInvoice(@RequestBody TsInvoiceManagement invoice) {
        try {
            TsInvoiceManagement result = invoiceManagementService.createInvoice(invoice);
            return MyJsonBean.success("发票创建成功", result);
        } catch (Exception e) {
            log.error("创建发票失败", e);
            return MyJsonBean.error("创建发票失败: " + e.getMessage());
        }
    }

    @PutMapping("/update")
    @ApiOperation("更新发票")
    public MyJsonBean updateInvoice(@RequestBody TsInvoiceManagement invoice) {
        try {
            TsInvoiceManagement result = invoiceManagementService.updateInvoice(invoice);
            return MyJsonBean.success("发票更新成功", result);
        } catch (Exception e) {
            log.error("更新发票失败", e);
            return MyJsonBean.error("更新发票失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/delete/{tenantId}/{invoiceId}")
    @ApiOperation("删除发票")
    public MyJsonBean deleteInvoice(@PathVariable Long tenantId, @PathVariable Long invoiceId) {
        try {
            boolean result = invoiceManagementService.deleteInvoiceById(tenantId, invoiceId);
            if (result) {
                return MyJsonBean.success("发票删除成功");
            } else {
                return MyJsonBean.error("发票删除失败");
            }
        } catch (Exception e) {
            log.error("删除发票失败", e);
            return MyJsonBean.error("删除发票失败: " + e.getMessage());
        }
    }

    @GetMapping("/detail/{tenantId}/{invoiceId}")
    @ApiOperation("获取发票详情")
    public MyJsonBean getInvoiceDetail(@PathVariable Long tenantId, @PathVariable Long invoiceId) {
        try {
            TsInvoiceManagement result = invoiceManagementService.getInvoiceById(tenantId, invoiceId);
            if (result != null) {
                return MyJsonBean.success("获取发票详情成功", result);
            } else {
                return MyJsonBean.error("发票不存在");
            }
        } catch (Exception e) {
            log.error("获取发票详情失败", e);
            return MyJsonBean.error("获取发票详情失败: " + e.getMessage());
        }
    }

    @GetMapping("/page")
    @ApiOperation("分页查询发票列表")
    public MyJsonBean getInvoicePage(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam Long tenantId,
            @RequestParam(required = false) String invoiceCode,
            @RequestParam(required = false) String invoiceNumber,
            @RequestParam(required = false) String invoiceType,
            @RequestParam(required = false) String invoiceStatus,
            @RequestParam(required = false) String sellerName,
            @RequestParam(required = false) String buyerName,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate,
            @RequestParam(required = false) String riskLevel,
            @RequestParam(required = false) String businessCategory) {
        try {
            Page<TsInvoiceManagement> page = new Page<>(current, size);
            IPage<TsInvoiceManagement> result = invoiceManagementService.getInvoicePage(
                    page, tenantId, invoiceCode, invoiceNumber, invoiceType, invoiceStatus,
                    sellerName, buyerName, startDate, endDate, riskLevel, businessCategory);
            return MyJsonBean.success("查询发票列表成功", result);
        } catch (Exception e) {
            log.error("查询发票列表失败", e);
            return MyJsonBean.error("查询发票列表失败: " + e.getMessage());
        }
    }

    // ==================== 发票识别功能 ====================

    @PostMapping("/upload")
    @ApiOperation("上传发票文件")
    public MyJsonBean uploadInvoiceFile(
            @RequestParam Long tenantId,
            @RequestParam String fileName,
            @RequestParam String filePath,
            @RequestParam Long fileSize,
            @RequestParam String fileType) {
        try {
            TsInvoiceManagement result = invoiceManagementService.uploadInvoiceFile(
                    tenantId, fileName, filePath, fileSize, fileType);
            return MyJsonBean.success("发票文件上传成功", result);
        } catch (Exception e) {
            log.error("上传发票文件失败", e);
            return MyJsonBean.error("上传发票文件失败: " + e.getMessage());
        }
    }

    @PostMapping("/recognize/{tenantId}/{invoiceId}")
    @ApiOperation("OCR识别发票")
    public MyJsonBean recognizeInvoice(@PathVariable Long tenantId, @PathVariable Long invoiceId) {
        try {
            boolean result = invoiceManagementService.recognizeInvoice(tenantId, invoiceId);
            if (result) {
                return MyJsonBean.success("发票识别成功");
            } else {
                return MyJsonBean.error("发票识别失败");
            }
        } catch (Exception e) {
            log.error("发票识别失败", e);
            return MyJsonBean.error("发票识别失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch-recognize")
    @ApiOperation("批量OCR识别发票")
    public MyJsonBean batchRecognizeInvoices(
            @RequestParam Long tenantId,
            @RequestBody List<Long> invoiceIds) {
        try {
            Map<String, Object> result = invoiceManagementService.batchRecognizeInvoices(tenantId, invoiceIds);
            return MyJsonBean.success("批量识别完成", result);
        } catch (Exception e) {
            log.error("批量识别发票失败", e);
            return MyJsonBean.error("批量识别发票失败: " + e.getMessage());
        }
    }

    @GetMapping("/pending-ocr/{tenantId}")
    @ApiOperation("获取待识别发票列表")
    public MyJsonBean getPendingOcrInvoices(
            @PathVariable Long tenantId,
            @RequestParam(defaultValue = "100") Integer limit) {
        try {
            List<TsInvoiceManagement> result = invoiceManagementService.getPendingOcrInvoices(tenantId, limit);
            return MyJsonBean.success("获取待识别发票列表成功", result);
        } catch (Exception e) {
            log.error("获取待识别发票列表失败", e);
            return MyJsonBean.error("获取待识别发票列表失败: " + e.getMessage());
        }
    }

    @PutMapping("/update-ocr-result")
    @ApiOperation("更新OCR识别结果")
    public MyJsonBean updateOcrResult(
            @RequestParam Long tenantId,
            @RequestParam Long invoiceId,
            @RequestParam String ocrResult,
            @RequestParam BigDecimal confidence) {
        try {
            boolean result = invoiceManagementService.updateOcrResult(tenantId, invoiceId, ocrResult, confidence);
            if (result) {
                return MyJsonBean.success("更新OCR结果成功");
            } else {
                return MyJsonBean.error("更新OCR结果失败");
            }
        } catch (Exception e) {
            log.error("更新OCR结果失败", e);
            return MyJsonBean.error("更新OCR结果失败: " + e.getMessage());
        }
    }

    // ==================== 发票验真功能 ====================

    @PostMapping("/verify/{tenantId}/{invoiceId}")
    @ApiOperation("验真发票")
    public MyJsonBean verifyInvoice(@PathVariable Long tenantId, @PathVariable Long invoiceId) {
        try {
            boolean result = invoiceManagementService.verifyInvoice(tenantId, invoiceId);
            if (result) {
                return MyJsonBean.success("发票验真成功");
            } else {
                return MyJsonBean.error("发票验真失败");
            }
        } catch (Exception e) {
            log.error("发票验真失败", e);
            return MyJsonBean.error("发票验真失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch-verify")
    @ApiOperation("批量验真发票")
    public MyJsonBean batchVerifyInvoices(
            @RequestParam Long tenantId,
            @RequestBody List<Long> invoiceIds) {
        try {
            Map<String, Object> result = invoiceManagementService.batchVerifyInvoices(tenantId, invoiceIds);
            return MyJsonBean.success("批量验真完成", result);
        } catch (Exception e) {
            log.error("批量验真发票失败", e);
            return MyJsonBean.error("批量验真发票失败: " + e.getMessage());
        }
    }

    @GetMapping("/pending-verification/{tenantId}")
    @ApiOperation("获取待验真发票列表")
    public MyJsonBean getPendingVerificationInvoices(
            @PathVariable Long tenantId,
            @RequestParam(defaultValue = "100") Integer limit) {
        try {
            List<TsInvoiceManagement> result = invoiceManagementService.getPendingVerificationInvoices(tenantId, limit);
            return MyJsonBean.success("获取待验真发票列表成功", result);
        } catch (Exception e) {
            log.error("获取待验真发票列表失败", e);
            return MyJsonBean.error("获取待验真发票列表失败: " + e.getMessage());
        }
    }

    @PutMapping("/update-verification-result")
    @ApiOperation("更新验真结果")
    public MyJsonBean updateVerificationResult(
            @RequestParam Long tenantId,
            @RequestParam Long invoiceId,
            @RequestParam String verificationResult,
            @RequestParam String source) {
        try {
            boolean result = invoiceManagementService.updateVerificationResult(
                    tenantId, invoiceId, verificationResult, source);
            if (result) {
                return MyJsonBean.success("更新验真结果成功");
            } else {
                return MyJsonBean.error("更新验真结果失败");
            }
        } catch (Exception e) {
            log.error("更新验真结果失败", e);
            return MyJsonBean.error("更新验真结果失败: " + e.getMessage());
        }
    }

    @GetMapping("/find-duplicates")
    @ApiOperation("查询重复发票")
    public MyJsonBean findDuplicateInvoices(
            @RequestParam Long tenantId,
            @RequestParam String invoiceCode,
            @RequestParam String invoiceNumber,
            @RequestParam(required = false) Long excludeId) {
        try {
            List<TsInvoiceManagement> result = invoiceManagementService.findDuplicateInvoices(
                    tenantId, invoiceCode, invoiceNumber, excludeId);
            return MyJsonBean.success("查询重复发票成功", result);
        } catch (Exception e) {
            log.error("查询重复发票失败", e);
            return MyJsonBean.error("查询重复发票失败: " + e.getMessage());
        }
    }

    // ==================== 发票归档功能 ====================

    @PostMapping("/archive/{tenantId}/{invoiceId}")
    @ApiOperation("归档发票")
    public MyJsonBean archiveInvoice(
            @PathVariable Long tenantId,
            @PathVariable Long invoiceId,
            @RequestParam String archivePath) {
        try {
            boolean result = invoiceManagementService.archiveInvoice(tenantId, invoiceId, archivePath);
            if (result) {
                return MyJsonBean.success("发票归档成功");
            } else {
                return MyJsonBean.error("发票归档失败");
            }
        } catch (Exception e) {
            log.error("发票归档失败", e);
            return MyJsonBean.error("发票归档失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch-archive")
    @ApiOperation("批量归档发票")
    public MyJsonBean batchArchiveInvoices(
            @RequestParam Long tenantId,
            @RequestBody List<Long> invoiceIds,
            @RequestParam String archiveBasePath) {
        try {
            Map<String, Object> result = invoiceManagementService.batchArchiveInvoices(
                    tenantId, invoiceIds, archiveBasePath);
            return MyJsonBean.success("批量归档完成", result);
        } catch (Exception e) {
            log.error("批量归档发票失败", e);
            return MyJsonBean.error("批量归档发票失败: " + e.getMessage());
        }
    }

    @GetMapping("/pending-archive/{tenantId}")
    @ApiOperation("获取待归档发票列表")
    public MyJsonBean getPendingArchiveInvoices(
            @PathVariable Long tenantId,
            @RequestParam(defaultValue = "100") Integer limit) {
        try {
            List<TsInvoiceManagement> result = invoiceManagementService.getPendingArchiveInvoices(tenantId, limit);
            return MyJsonBean.success("获取待归档发票列表成功", result);
        } catch (Exception e) {
            log.error("获取待归档发票列表失败", e);
            return MyJsonBean.error("获取待归档发票列表失败: " + e.getMessage());
        }
    }

    // ==================== 风险识别功能 ====================

    @PostMapping("/assess-risk/{tenantId}/{invoiceId}")
    @ApiOperation("风险评估")
    public MyJsonBean assessInvoiceRisk(@PathVariable Long tenantId, @PathVariable Long invoiceId) {
        try {
            boolean result = invoiceManagementService.assessInvoiceRisk(tenantId, invoiceId);
            if (result) {
                return MyJsonBean.success("风险评估成功");
            } else {
                return MyJsonBean.error("风险评估失败");
            }
        } catch (Exception e) {
            log.error("风险评估失败", e);
            return MyJsonBean.error("风险评估失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch-assess-risk")
    @ApiOperation("批量风险评估")
    public MyJsonBean batchAssessInvoiceRisk(
            @RequestParam Long tenantId,
            @RequestBody List<Long> invoiceIds) {
        try {
            Map<String, Object> result = invoiceManagementService.batchAssessInvoiceRisk(tenantId, invoiceIds);
            return MyJsonBean.success("批量风险评估完成", result);
        } catch (Exception e) {
            log.error("批量风险评估失败", e);
            return MyJsonBean.error("批量风险评估失败: " + e.getMessage());
        }
    }

    @GetMapping("/high-risk/{tenantId}")
    @ApiOperation("获取高风险发票列表")
    public MyJsonBean getHighRiskInvoices(
            @PathVariable Long tenantId,
            @RequestParam List<String> riskLevels) {
        try {
            List<TsInvoiceManagement> result = invoiceManagementService.getHighRiskInvoices(tenantId, riskLevels);
            return MyJsonBean.success("获取高风险发票列表成功", result);
        } catch (Exception e) {
            log.error("获取高风险发票列表失败", e);
            return MyJsonBean.error("获取高风险发票列表失败: " + e.getMessage());
        }
    }

    // ==================== 统计分析功能 ====================

    @GetMapping("/overview/{tenantId}")
    @ApiOperation("获取发票统计概览")
    public MyJsonBean getInvoiceOverview(
            @PathVariable Long tenantId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) {
        try {
            Map<String, Object> result = invoiceManagementService.getInvoiceOverview(tenantId, startDate, endDate);
            return MyJsonBean.success("获取发票统计概览成功", result);
        } catch (Exception e) {
            log.error("获取发票统计概览失败", e);
            return MyJsonBean.error("获取发票统计概览失败: " + e.getMessage());
        }
    }

    @GetMapping("/count-by-status/{tenantId}")
    @ApiOperation("统计发票数量按状态分组")
    public MyJsonBean countInvoicesByStatus(
            @PathVariable Long tenantId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) {
        try {
            List<Map<String, Object>> result = invoiceManagementService.countInvoicesByStatus(tenantId, startDate, endDate);
            return MyJsonBean.success("统计发票数量按状态分组成功", result);
        } catch (Exception e) {
            log.error("统计发票数量按状态分组失败", e);
            return MyJsonBean.error("统计发票数量按状态分组失败: " + e.getMessage());
        }
    }

    @GetMapping("/count-by-type/{tenantId}")
    @ApiOperation("统计发票数量按类型分组")
    public MyJsonBean countInvoicesByType(
            @PathVariable Long tenantId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) {
        try {
            List<Map<String, Object>> result = invoiceManagementService.countInvoicesByType(tenantId, startDate, endDate);
            return MyJsonBean.success("统计发票数量按类型分组成功", result);
        } catch (Exception e) {
            log.error("统计发票数量按类型分组失败", e);
            return MyJsonBean.error("统计发票数量按类型分组失败: " + e.getMessage());
        }
    }

    @GetMapping("/sum-amount-by-month/{tenantId}")
    @ApiOperation("统计发票金额按月份分组")
    public MyJsonBean sumAmountByMonth(
            @PathVariable Long tenantId,
            @RequestParam Integer year) {
        try {
            List<Map<String, Object>> result = invoiceManagementService.sumAmountByMonth(tenantId, year);
            return MyJsonBean.success("统计发票金额按月份分组成功", result);
        } catch (Exception e) {
            log.error("统计发票金额按月份分组失败", e);
            return MyJsonBean.error("统计发票金额按月份分组失败: " + e.getMessage());
        }
    }

    @GetMapping("/ocr-success-rate/{tenantId}")
    @ApiOperation("计算OCR识别成功率")
    public MyJsonBean calculateOcrSuccessRate(
            @PathVariable Long tenantId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) {
        try {
            Map<String, Object> result = invoiceManagementService.calculateOcrSuccessRate(tenantId, startDate, endDate);
            return MyJsonBean.success("计算OCR识别成功率成功", result);
        } catch (Exception e) {
            log.error("计算OCR识别成功率失败", e);
            return MyJsonBean.error("计算OCR识别成功率失败: " + e.getMessage());
        }
    }

    @GetMapping("/verification-success-rate/{tenantId}")
    @ApiOperation("计算验真成功率")
    public MyJsonBean calculateVerificationSuccessRate(
            @PathVariable Long tenantId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) {
        try {
            Map<String, Object> result = invoiceManagementService.calculateVerificationSuccessRate(tenantId, startDate, endDate);
            return MyJsonBean.success("计算验真成功率成功", result);
        } catch (Exception e) {
            log.error("计算验真成功率失败", e);
            return MyJsonBean.error("计算验真成功率失败: " + e.getMessage());
        }
    }

    // ==================== 批量操作功能 ====================

    @PostMapping("/batch-update-status")
    @ApiOperation("批量更新发票状态")
    public MyJsonBean batchUpdateStatus(
            @RequestParam Long tenantId,
            @RequestBody List<Long> invoiceIds,
            @RequestParam String status) {
        try {
            boolean result = invoiceManagementService.batchUpdateStatus(tenantId, invoiceIds, status);
            if (result) {
                return MyJsonBean.success("批量更新状态成功");
            } else {
                return MyJsonBean.error("批量更新状态失败");
            }
        } catch (Exception e) {
            log.error("批量更新状态失败", e);
            return MyJsonBean.error("批量更新状态失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/batch-delete")
    @ApiOperation("批量删除发票")
    public MyJsonBean batchDeleteInvoices(
            @RequestParam Long tenantId,
            @RequestBody List<Long> invoiceIds) {
        try {
            boolean result = invoiceManagementService.batchDeleteInvoices(tenantId, invoiceIds);
            if (result) {
                return MyJsonBean.success("批量删除成功");
            } else {
                return MyJsonBean.error("批量删除失败");
            }
        } catch (Exception e) {
            log.error("批量删除失败", e);
            return MyJsonBean.error("批量删除失败: " + e.getMessage());
        }
    }

    // ==================== 查询功能 ====================

    @GetMapping("/by-code-number/{tenantId}")
    @ApiOperation("根据发票代码和号码查询发票")
    public MyJsonBean getInvoiceByCodeAndNumber(
            @PathVariable Long tenantId,
            @RequestParam String invoiceCode,
            @RequestParam String invoiceNumber) {
        try {
            TsInvoiceManagement result = invoiceManagementService.getInvoiceByCodeAndNumber(
                    tenantId, invoiceCode, invoiceNumber);
            return MyJsonBean.success("查询发票成功", result);
        } catch (Exception e) {
            log.error("查询发票失败", e);
            return MyJsonBean.error("查询发票失败: " + e.getMessage());
        }
    }

    @GetMapping("/by-seller-tax-number/{tenantId}")
    @ApiOperation("根据销售方税号查询发票列表")
    public MyJsonBean getInvoicesBySellerTaxNumber(
            @PathVariable Long tenantId,
            @RequestParam String sellerTaxNumber) {
        try {
            List<TsInvoiceManagement> result = invoiceManagementService.getInvoicesBySellerTaxNumber(
                    tenantId, sellerTaxNumber);
            return MyJsonBean.success("查询发票列表成功", result);
        } catch (Exception e) {
            log.error("查询发票列表失败", e);
            return MyJsonBean.error("查询发票列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/by-buyer-tax-number/{tenantId}")
    @ApiOperation("根据购买方税号查询发票列表")
    public MyJsonBean getInvoicesByBuyerTaxNumber(
            @PathVariable Long tenantId,
            @RequestParam String buyerTaxNumber) {
        try {
            List<TsInvoiceManagement> result = invoiceManagementService.getInvoicesByBuyerTaxNumber(
                    tenantId, buyerTaxNumber);
            return MyJsonBean.success("查询发票列表成功", result);
        } catch (Exception e) {
            log.error("查询发票列表失败", e);
            return MyJsonBean.error("查询发票列表失败: " + e.getMessage());
        }
    }

    // ==================== 系统维护功能 ====================

    @GetMapping("/abnormal/{tenantId}")
    @ApiOperation("获取异常发票列表")
    public MyJsonBean getAbnormalInvoices(
            @PathVariable Long tenantId,
            @RequestParam List<String> abnormalTypes) {
        try {
            List<TsInvoiceManagement> result = invoiceManagementService.getAbnormalInvoices(tenantId, abnormalTypes);
            return MyJsonBean.success("获取异常发票列表成功", result);
        } catch (Exception e) {
            log.error("获取异常发票列表失败", e);
            return MyJsonBean.error("获取异常发票列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/retry/{tenantId}")
    @ApiOperation("获取需要重试的发票列表")
    public MyJsonBean getRetryInvoices(@PathVariable Long tenantId) {
        try {
            List<TsInvoiceManagement> result = invoiceManagementService.getRetryInvoices(tenantId);
            return MyJsonBean.success("获取需要重试的发票列表成功", result);
        } catch (Exception e) {
            log.error("获取需要重试的发票列表失败", e);
            return MyJsonBean.error("获取需要重试的发票列表失败: " + e.getMessage());
        }
    }

    @PostMapping("/health-check/{tenantId}")
    @ApiOperation("系统健康检查")
    public MyJsonBean healthCheck(@PathVariable Long tenantId) {
        try {
            Map<String, Object> result = invoiceManagementService.healthCheck(tenantId);
            return MyJsonBean.success("系统健康检查完成", result);
        } catch (Exception e) {
            log.error("系统健康检查失败", e);
            return MyJsonBean.error("系统健康检查失败: " + e.getMessage());
        }
    }

    @GetMapping("/performance-metrics/{tenantId}")
    @ApiOperation("获取系统性能指标")
    public MyJsonBean getPerformanceMetrics(@PathVariable Long tenantId) {
        try {
            Map<String, Object> result = invoiceManagementService.getPerformanceMetrics(tenantId);
            return MyJsonBean.success("获取系统性能指标成功", result);
        } catch (Exception e) {
            log.error("获取系统性能指标失败", e);
            return MyJsonBean.error("获取系统性能指标失败: " + e.getMessage());
        }
    }

    @GetMapping("/count-by-risk-level/{tenantId}")
    @ApiOperation("统计风险发票分布")
    public MyJsonBean countInvoicesByRiskLevel(
            @PathVariable Long tenantId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) {
        try {
            List<Map<String, Object>> result = invoiceManagementService.countInvoicesByRiskLevel(tenantId, startDate, endDate);
            return MyJsonBean.success("统计风险发票分布成功", result);
        } catch (Exception e) {
            log.error("统计风险发票分布失败", e);
            return MyJsonBean.error("统计风险发票分布失败: " + e.getMessage());
        }
    }

    @GetMapping("/processing-time-stats/{tenantId}")
    @ApiOperation("计算处理时长统计")
    public MyJsonBean calculateProcessingTimeStats(
            @PathVariable Long tenantId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) {
        try {
            Map<String, Object> result = invoiceManagementService.calculateProcessingTimeStats(tenantId, startDate, endDate);
            return MyJsonBean.success("计算处理时长统计成功", result);
        } catch (Exception e) {
            log.error("计算处理时长统计失败", e);
            return MyJsonBean.error("计算处理时长统计失败: " + e.getMessage());
        }
    }

    @GetMapping("/amount-stats/{tenantId}")
    @ApiOperation("计算金额统计")
    public MyJsonBean calculateAmountStats(
            @PathVariable Long tenantId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) {
        try {
            Map<String, Object> result = invoiceManagementService.calculateAmountStats(tenantId, startDate, endDate);
            return MyJsonBean.success("计算金额统计成功", result);
        } catch (Exception e) {
            log.error("计算金额统计失败", e);
            return MyJsonBean.error("计算金额统计失败: " + e.getMessage());
        }
    }

    @GetMapping("/tax-amount-stats/{tenantId}")
    @ApiOperation("计算税额统计")
    public MyJsonBean calculateTaxAmountStats(
            @PathVariable Long tenantId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) {
        try {
            Map<String, Object> result = invoiceManagementService.calculateTaxAmountStats(tenantId, startDate, endDate);
            return MyJsonBean.success("计算税额统计成功", result);
        } catch (Exception e) {
            log.error("计算税额统计失败", e);
            return MyJsonBean.error("计算税额统计失败: " + e.getMessage());
        }
    }

    @GetMapping("/count-trend/{tenantId}")
    @ApiOperation("获取发票数量趋势")
    public MyJsonBean getInvoiceCountTrend(
            @PathVariable Long tenantId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate,
            @RequestParam(defaultValue = "day") String groupBy) {
        try {
            List<Map<String, Object>> result = invoiceManagementService.getInvoiceCountTrend(tenantId, startDate, endDate, groupBy);
            return MyJsonBean.success("获取发票数量趋势成功", result);
        } catch (Exception e) {
            log.error("获取发票数量趋势失败", e);
            return MyJsonBean.error("获取发票数量趋势失败: " + e.getMessage());
        }
    }

    @GetMapping("/amount-trend/{tenantId}")
    @ApiOperation("获取发票金额趋势")
    public MyJsonBean getInvoiceAmountTrend(
            @PathVariable Long tenantId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate,
            @RequestParam(defaultValue = "day") String groupBy) {
        try {
            List<Map<String, Object>> result = invoiceManagementService.getInvoiceAmountTrend(tenantId, startDate, endDate, groupBy);
            return MyJsonBean.success("获取发票金额趋势成功", result);
        } catch (Exception e) {
            log.error("获取发票金额趋势失败", e);
            return MyJsonBean.error("获取发票金额趋势失败: " + e.getMessage());
        }
    }

    @GetMapping("/processing-efficiency-stats/{tenantId}")
    @ApiOperation("获取处理效率统计")
    public MyJsonBean getProcessingEfficiencyStats(
            @PathVariable Long tenantId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) {
        try {
            Map<String, Object> result = invoiceManagementService.getProcessingEfficiencyStats(tenantId, startDate, endDate);
            return MyJsonBean.success("获取处理效率统计成功", result);
        } catch (Exception e) {
            log.error("获取处理效率统计失败", e);
            return MyJsonBean.error("获取处理效率统计失败: " + e.getMessage());
        }
    }

    @GetMapping("/quality-stats/{tenantId}")
    @ApiOperation("获取发票质量统计")
    public MyJsonBean getInvoiceQualityStats(
            @PathVariable Long tenantId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) {
        try {
            Map<String, Object> result = invoiceManagementService.getInvoiceQualityStats(tenantId, startDate, endDate);
            return MyJsonBean.success("获取发票质量统计成功", result);
        } catch (Exception e) {
            log.error("获取发票质量统计失败", e);
            return MyJsonBean.error("获取发票质量统计失败: " + e.getMessage());
        }
    }

    @GetMapping("/compliance-stats/{tenantId}")
    @ApiOperation("获取合规性统计")
    public MyJsonBean getComplianceStats(
            @PathVariable Long tenantId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) {
        try {
            Map<String, Object> result = invoiceManagementService.getComplianceStats(tenantId, startDate, endDate);
            return MyJsonBean.success("获取合规性统计成功", result);
        } catch (Exception e) {
            log.error("获取合规性统计失败", e);
            return MyJsonBean.error("获取合规性统计失败: " + e.getMessage());
        }
    }

    @GetMapping("/risk-analysis/{tenantId}")
    @ApiOperation("获取风险分析")
    public MyJsonBean getRiskAnalysis(
            @PathVariable Long tenantId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) {
        try {
            Map<String, Object> result = invoiceManagementService.getRiskAnalysis(tenantId, startDate, endDate);
            return MyJsonBean.success("获取风险分析成功", result);
        } catch (Exception e) {
            log.error("获取风险分析失败", e);
            return MyJsonBean.error("获取风险分析失败: " + e.getMessage());
        }
    }

    @GetMapping("/cost-analysis/{tenantId}")
    @ApiOperation("获取成本分析")
    public MyJsonBean getCostAnalysis(
            @PathVariable Long tenantId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime endDate) {
        try {
            Map<String, Object> result = invoiceManagementService.getCostAnalysis(tenantId, startDate, endDate);
            return MyJsonBean.success("获取成本分析成功", result);
        } catch (Exception e) {
            log.error("获取成本分析失败", e);
            return MyJsonBean.error("获取成本分析失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch-import")
    @ApiOperation("批量导入发票")
    public MyJsonBean batchImportInvoices(
            @RequestParam Long tenantId,
            @RequestBody List<Map<String, Object>> invoiceData) {
        try {
            Map<String, Object> result = invoiceManagementService.batchImportInvoices(tenantId, invoiceData);
            return MyJsonBean.success("批量导入发票完成", result);
        } catch (Exception e) {
            log.error("批量导入发票失败", e);
            return MyJsonBean.error("批量导入发票失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch-export")
    @ApiOperation("批量导出发票")
    public MyJsonBean batchExportInvoices(
            @RequestParam Long tenantId,
            @RequestBody List<Long> invoiceIds) {
        try {
            List<Map<String, Object>> result = invoiceManagementService.batchExportInvoices(tenantId, invoiceIds);
            return MyJsonBean.success("批量导出发票完成", result);
        } catch (Exception e) {
            log.error("批量导出发票失败", e);
            return MyJsonBean.error("批量导出发票失败: " + e.getMessage());
        }
    }

    @PostMapping("/retry-process/{tenantId}/{invoiceId}")
    @ApiOperation("重试处理发票")
    public MyJsonBean retryProcessInvoice(@PathVariable Long tenantId, @PathVariable Long invoiceId) {
        try {
            boolean result = invoiceManagementService.retryProcessInvoice(tenantId, invoiceId);
            if (result) {
                return MyJsonBean.success("重试处理发票成功");
            } else {
                return MyJsonBean.error("重试处理发票失败");
            }
        } catch (Exception e) {
            log.error("重试处理发票失败", e);
            return MyJsonBean.error("重试处理发票失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/clean-expired/{tenantId}")
    @ApiOperation("清理过期发票")
    public MyJsonBean cleanExpiredInvoices(
            @PathVariable Long tenantId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime expiredDate) {
        try {
            int result = invoiceManagementService.cleanExpiredInvoices(tenantId, expiredDate);
            return MyJsonBean.success("清理过期发票完成，清理数量: " + result);
        } catch (Exception e) {
            log.error("清理过期发票失败", e);
            return MyJsonBean.error("清理过期发票失败: " + e.getMessage());
        }
    }
}
