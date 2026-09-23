package com.management.accountant.service.ts.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.management.accountant.entity.ts.TsTaxDeclaration;
import com.management.accountant.mapper.ts.TsTaxDeclarationMapper;
import com.management.accountant.service.ts.TsTaxDeclarationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

/**
 * 税务申报服务实现类
 *
 * @author AI Assistant
 * @since 2025-01-27
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class TsTaxDeclarationServiceImpl extends ServiceImpl<TsTaxDeclarationMapper, TsTaxDeclaration> 
        implements TsTaxDeclarationService {

    // ==================== 基础CRUD操作 ====================

    @Override
    public TsTaxDeclaration createDeclaration(TsTaxDeclaration declaration) {
        log.info("创建税务申报: {}", declaration.getDeclarationName());
        
        // 生成申报编号
        if (!StringUtils.hasText(declaration.getDeclarationCode())) {
            declaration.setDeclarationCode(generateDeclarationCode(declaration.getTenantId(), declaration.getTaxType()));
        }
        
        // 设置默认值
        if (declaration.getDeclarationStatus() == null) {
            declaration.setDeclarationStatus("DRAFT");
        }
        if (declaration.getRetryCount() == null) {
            declaration.setRetryCount(0);
        }
        if (declaration.getMaxRetryCount() == null) {
            declaration.setMaxRetryCount(3);
        }
        if (declaration.getPriority() == null) {
            declaration.setPriority("NORMAL");
        }
        
        save(declaration);
        log.info("税务申报创建成功: {}", declaration.getDeclarationId());
        return declaration;
    }

    @Override
    public TsTaxDeclaration updateDeclaration(TsTaxDeclaration declaration) {
        log.info("更新税务申报: {}", declaration.getDeclarationId());
        
        TsTaxDeclaration existingDeclaration = getById(declaration.getDeclarationId());
        if (existingDeclaration == null) {
            throw new RuntimeException("税务申报不存在: " + declaration.getDeclarationId());
        }
        
        updateById(declaration);
        log.info("税务申报更新成功: {}", declaration.getDeclarationId());
        return declaration;
    }

    @Override
    public boolean deleteDeclaration(Long tenantId, Long declarationId) {
        log.info("删除税务申报: {}", declarationId);
        
        TsTaxDeclaration declaration = getDeclarationById(tenantId, declarationId);
        if (declaration == null) {
            throw new RuntimeException("税务申报不存在: " + declarationId);
        }
        
        boolean result = removeById(declarationId);
        log.info("税务申报删除{}: {}", result ? "成功" : "失败", declarationId);
        return result;
    }

    @Override
    public TsTaxDeclaration getDeclarationById(Long tenantId, Long declarationId) {
        QueryWrapper<TsTaxDeclaration> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("tenant_id", tenantId)
                   .eq("declaration_id", declarationId);
        return getOne(queryWrapper);
    }

    @Override
    public IPage<TsTaxDeclaration> getDeclarationPage(Page<TsTaxDeclaration> page, 
                                                     Long tenantId,
                                                     String declarationCode,
                                                     String declarationName,
                                                     String taxType,
                                                     String declarationType,
                                                     String declarationStatus,
                                                     String taxpayerName,
                                                     String declarationPeriod,
                                                     LocalDateTime startDate,
                                                     LocalDateTime endDate,
                                                     String businessCategory) {
        return baseMapper.selectTaxDeclarationPage(page, tenantId, declarationCode, declarationName, 
                                                  taxType, declarationType, declarationStatus, taxpayerName,
                                                  declarationPeriod, startDate, endDate, businessCategory);
    }

    // ==================== 申报管理功能 ====================

    @Override
    public List<TsTaxDeclaration> generateDeclarationPlan(Long tenantId, String taxType, String period, Integer year) {
        log.info("生成申报计划: 租户={}, 税种={}, 期间={}, 年度={}", tenantId, taxType, period, year);
        
        List<TsTaxDeclaration> declarations = new ArrayList<>();
        
        // 根据税种和期间生成申报计划
        if ("MONTHLY".equals(period)) {
            // 生成月度申报计划
            for (int month = 1; month <= 12; month++) {
                TsTaxDeclaration declaration = createMonthlyDeclaration(tenantId, taxType, year, month);
                declarations.add(declaration);
            }
        } else if ("QUARTERLY".equals(period)) {
            // 生成季度申报计划
            for (int quarter = 1; quarter <= 4; quarter++) {
                TsTaxDeclaration declaration = createQuarterlyDeclaration(tenantId, taxType, year, quarter);
                declarations.add(declaration);
            }
        } else if ("YEARLY".equals(period)) {
            // 生成年度申报计划
            TsTaxDeclaration declaration = createYearlyDeclaration(tenantId, taxType, year);
            declarations.add(declaration);
        }
        
        // 批量保存
        saveBatch(declarations);
        
        log.info("申报计划生成完成，共生成{}个申报任务", declarations.size());
        return declarations;
    }

    @Override
    public boolean autoFillDeclaration(Long tenantId, Long declarationId) {
        log.info("自动填报申报表: {}", declarationId);
        
        TsTaxDeclaration declaration = getDeclarationById(tenantId, declarationId);
        if (declaration == null) {
            throw new RuntimeException("税务申报不存在: " + declarationId);
        }
        
        try {
            // 更新状态为填报中
            declaration.setDeclarationStatus("FILLING");
            updateById(declaration);
            
            // 执行自动填报逻辑
            Map<String, Object> declarationData = performAutoFill(declaration);
            
            // 更新申报数据
            declaration.setDeclarationData(declarationData.toString());
            declaration.setDeclarationStatus("FILLED");
            
            updateById(declaration);
            log.info("申报表自动填报成功: {}", declarationId);
            return true;
            
        } catch (Exception e) {
            log.error("申报表自动填报失败: {}", declarationId, e);
            declaration.setDeclarationStatus("FILL_FAILED");
            declaration.setErrorMessage(e.getMessage());
            updateById(declaration);
            return false;
        }
    }

    @Override
    public boolean submitDeclaration(Long tenantId, Long declarationId) {
        log.info("提交申报: {}", declarationId);
        
        TsTaxDeclaration declaration = getDeclarationById(tenantId, declarationId);
        if (declaration == null) {
            throw new RuntimeException("税务申报不存在: " + declarationId);
        }
        
        try {
            // 验证申报数据
            Map<String, Object> validationResult = validateDeclarationData(tenantId, declarationId);
            if (!(Boolean) validationResult.get("valid")) {
                throw new RuntimeException("申报数据验证失败: " + validationResult.get("message"));
            }
            
            // 更新状态为提交中
            declaration.setDeclarationStatus("SUBMITTING");
            declaration.setSubmitTime(LocalDateTime.now());
            updateById(declaration);
            
            // 执行提交逻辑
            Map<String, Object> submitResult = performSubmit(declaration);
            
            // 更新提交结果
            declaration.setDeclarationStatus("SUBMITTED");
            declaration.setReceiptNumber((String) submitResult.get("receiptNumber"));
            declaration.setAcceptTime(LocalDateTime.now());
            
            updateById(declaration);
            log.info("申报提交成功: {}", declarationId);
            return true;
            
        } catch (Exception e) {
            log.error("申报提交失败: {}", declarationId, e);
            declaration.setDeclarationStatus("SUBMIT_FAILED");
            declaration.setErrorMessage(e.getMessage());
            updateById(declaration);
            return false;
        }
    }

    @Override
    public boolean withdrawDeclaration(Long tenantId, Long declarationId) {
        log.info("撤回申报: {}", declarationId);
        
        TsTaxDeclaration declaration = getDeclarationById(tenantId, declarationId);
        if (declaration == null) {
            throw new RuntimeException("税务申报不存在: " + declarationId);
        }
        
        // 检查是否可以撤回
        if (!"SUBMITTED".equals(declaration.getDeclarationStatus()) && 
            !"UNDER_REVIEW".equals(declaration.getDeclarationStatus())) {
            throw new RuntimeException("当前状态不允许撤回: " + declaration.getDeclarationStatus());
        }
        
        try {
            // 执行撤回逻辑
            performWithdraw(declaration);
            
            // 更新状态
            declaration.setDeclarationStatus("WITHDRAWN");
            updateById(declaration);
            
            log.info("申报撤回成功: {}", declarationId);
            return true;
            
        } catch (Exception e) {
            log.error("申报撤回失败: {}", declarationId, e);
            declaration.setErrorMessage(e.getMessage());
            updateById(declaration);
            return false;
        }
    }

    @Override
    public boolean reviewDeclaration(Long tenantId, Long declarationId, String reviewResult, String reviewComment) {
        log.info("审核申报: {}, 结果: {}", declarationId, reviewResult);
        
        TsTaxDeclaration declaration = getDeclarationById(tenantId, declarationId);
        if (declaration == null) {
            throw new RuntimeException("税务申报不存在: " + declarationId);
        }
        
        // 更新审核信息
        declaration.setDeclarationStatus("APPROVED".equals(reviewResult) ? "APPROVED" : "REJECTED");
        declaration.setReviewComment(reviewComment);
        declaration.setReviewTime(LocalDateTime.now());
        
        if ("APPROVED".equals(reviewResult)) {
            declaration.setCompleteTime(LocalDateTime.now());
        }
        
        updateById(declaration);
        log.info("申报审核完成: {}", declarationId);
        return true;
    }

    @Override
    public Map<String, Object> batchSubmitDeclarations(Long tenantId, List<Long> declarationIds) {
        log.info("批量提交申报: {}", declarationIds);
        
        int successCount = 0;
        int failCount = 0;
        List<String> errors = new ArrayList<>();
        
        for (Long declarationId : declarationIds) {
            try {
                if (submitDeclaration(tenantId, declarationId)) {
                    successCount++;
                } else {
                    failCount++;
                }
            } catch (Exception e) {
                failCount++;
                errors.add("申报ID " + declarationId + ": " + e.getMessage());
            }
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("errors", errors);
        
        log.info("批量提交申报完成，成功: {}, 失败: {}", successCount, failCount);
        return result;
    }

    @Override
    public Map<String, Object> batchReviewDeclarations(Long tenantId, List<Long> declarationIds, String reviewResult, String reviewComment) {
        log.info("批量审核申报: {}, 结果: {}", declarationIds, reviewResult);
        
        int successCount = 0;
        int failCount = 0;
        List<String> errors = new ArrayList<>();
        
        for (Long declarationId : declarationIds) {
            try {
                if (reviewDeclaration(tenantId, declarationId, reviewResult, reviewComment)) {
                    successCount++;
                } else {
                    failCount++;
                }
            } catch (Exception e) {
                failCount++;
                errors.add("申报ID " + declarationId + ": " + e.getMessage());
            }
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("errors", errors);
        
        log.info("批量审核申报完成，成功: {}, 失败: {}", successCount, failCount);
        return result;
    }

    // ==================== 申报计算功能 ====================

    @Override
    public Map<String, Object> calculateTaxAmount(Long tenantId, Long declarationId) {
        log.info("计算税额: {}", declarationId);
        
        TsTaxDeclaration declaration = getDeclarationById(tenantId, declarationId);
        if (declaration == null) {
            throw new RuntimeException("税务申报不存在: " + declarationId);
        }
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 获取计税依据
            BigDecimal taxBase = getTaxBaseAmount(declaration);
            
            // 获取税率
            BigDecimal taxRate = getTaxRateByType(declaration.getTaxType());
            
            // 计算应纳税额
            BigDecimal taxAmount = taxBase.multiply(taxRate);
            
            // 计算已缴税额
            BigDecimal paidAmount = getPaidTaxAmount(declaration);
            
            // 计算应补税额或应退税额
            BigDecimal difference = taxAmount.subtract(paidAmount);
            BigDecimal payableAmount = difference.compareTo(BigDecimal.ZERO) > 0 ? difference : BigDecimal.ZERO;
            BigDecimal refundableAmount = difference.compareTo(BigDecimal.ZERO) < 0 ? difference.abs() : BigDecimal.ZERO;
            
            // 更新申报记录
            declaration.setTaxBase(taxBase);
            declaration.setTaxRate(taxRate);
            declaration.setTaxAmount(taxAmount);
            declaration.setPaidAmount(paidAmount);
            declaration.setPayableAmount(payableAmount);
            declaration.setRefundableAmount(refundableAmount);
            
            updateById(declaration);
            
            result.put("success", true);
            result.put("taxBase", taxBase);
            result.put("taxRate", taxRate);
            result.put("taxAmount", taxAmount);
            result.put("paidAmount", paidAmount);
            result.put("payableAmount", payableAmount);
            result.put("refundableAmount", refundableAmount);
            
            log.info("税额计算完成: {}", declarationId);
            
        } catch (Exception e) {
            log.error("税额计算失败: {}", declarationId, e);
            result.put("success", false);
            result.put("message", e.getMessage());
        }
        
        return result;
    }

    @Override
    public boolean recalculateTaxAmount(Long tenantId, Long declarationId) {
        log.info("重新计算税额: {}", declarationId);
        
        Map<String, Object> result = calculateTaxAmount(tenantId, declarationId);
        return (Boolean) result.get("success");
    }

    @Override
    public Map<String, Object> validateDeclarationData(Long tenantId, Long declarationId) {
        log.info("验证申报数据: {}", declarationId);
        
        TsTaxDeclaration declaration = getDeclarationById(tenantId, declarationId);
        if (declaration == null) {
            throw new RuntimeException("税务申报不存在: " + declarationId);
        }
        
        Map<String, Object> result = new HashMap<>();
        List<String> errors = new ArrayList<>();
        
        // 验证必填字段
        if (!StringUtils.hasText(declaration.getTaxpayerId())) {
            errors.add("纳税人识别号不能为空");
        }
        if (!StringUtils.hasText(declaration.getTaxpayerName())) {
            errors.add("纳税人名称不能为空");
        }
        if (declaration.getTaxBase() == null || declaration.getTaxBase().compareTo(BigDecimal.ZERO) < 0) {
            errors.add("计税依据不能为空或负数");
        }
        if (declaration.getTaxRate() == null || declaration.getTaxRate().compareTo(BigDecimal.ZERO) < 0) {
            errors.add("税率不能为空或负数");
        }
        
        // 验证业务逻辑
        if (declaration.getDeadline() != null && declaration.getDeadline().isBefore(LocalDateTime.now())) {
            errors.add("申报截止日期已过期");
        }
        
        result.put("valid", errors.isEmpty());
        result.put("errors", errors);
        result.put("message", errors.isEmpty() ? "验证通过" : String.join(", ", errors));
        
        log.info("申报数据验证完成: {}, 结果: {}", declarationId, result.get("valid"));
        return result;
    }

    @Override
    public Map<String, Object> getTaxBase(Long tenantId, Long declarationId) {
        log.info("获取计税依据: {}", declarationId);
        
        TsTaxDeclaration declaration = getDeclarationById(tenantId, declarationId);
        if (declaration == null) {
            throw new RuntimeException("税务申报不存在: " + declarationId);
        }
        
        Map<String, Object> result = new HashMap<>();
        
        // 根据税种获取计税依据
        BigDecimal taxBase = getTaxBaseAmount(declaration);
        
        result.put("taxBase", taxBase);
        result.put("taxType", declaration.getTaxType());
        result.put("calculationMethod", getCalculationMethod(declaration.getTaxType()));
        result.put("calculationDetails", getCalculationDetails(declaration));
        
        return result;
    }

    // ==================== 私有辅助方法 ====================

    /**
     * 生成申报编号
     */
    private String generateDeclarationCode(Long tenantId, String taxType) {
        String prefix = "TD" + taxType.substring(0, Math.min(taxType.length(), 3)).toUpperCase();
        String timestamp = String.valueOf(System.currentTimeMillis());
        return prefix + timestamp.substring(timestamp.length() - 8);
    }

    /**
     * 创建月度申报
     */
    private TsTaxDeclaration createMonthlyDeclaration(Long tenantId, String taxType, Integer year, Integer month) {
        TsTaxDeclaration declaration = new TsTaxDeclaration();
        declaration.setTenantId(tenantId);
        declaration.setDeclarationName(taxType + year + "年" + month + "月申报");
        declaration.setTaxType(taxType);
        declaration.setDeclarationType("MONTHLY");
        declaration.setDeclarationStatus("DRAFT");
        declaration.setDeclarationPeriod(year + "-" + String.format("%02d", month));
        declaration.setDeclarationYear(year);
        declaration.setDeclarationMonth(month);
        declaration.setPriority("NORMAL");
        
        // 设置申报截止日期（次月15日）
        LocalDateTime deadline = LocalDateTime.of(year, month, 1, 0, 0)
                .plusMonths(1).withDayOfMonth(15).withHour(23).withMinute(59).withSecond(59);
        declaration.setDeadline(deadline);
        
        return declaration;
    }

    /**
     * 创建季度申报
     */
    private TsTaxDeclaration createQuarterlyDeclaration(Long tenantId, String taxType, Integer year, Integer quarter) {
        TsTaxDeclaration declaration = new TsTaxDeclaration();
        declaration.setTenantId(tenantId);
        declaration.setDeclarationName(taxType + year + "年第" + quarter + "季度申报");
        declaration.setTaxType(taxType);
        declaration.setDeclarationType("QUARTERLY");
        declaration.setDeclarationStatus("DRAFT");
        declaration.setDeclarationPeriod(year + "-Q" + quarter);
        declaration.setDeclarationYear(year);
        declaration.setDeclarationQuarter(quarter);
        declaration.setPriority("NORMAL");
        
        // 设置申报截止日期（季度后次月15日）
        int endMonth = quarter * 3;
        LocalDateTime deadline = LocalDateTime.of(year, endMonth, 1, 0, 0)
                .plusMonths(1).withDayOfMonth(15).withHour(23).withMinute(59).withSecond(59);
        declaration.setDeadline(deadline);
        
        return declaration;
    }

    /**
     * 创建年度申报
     */
    private TsTaxDeclaration createYearlyDeclaration(Long tenantId, String taxType, Integer year) {
        TsTaxDeclaration declaration = new TsTaxDeclaration();
        declaration.setTenantId(tenantId);
        declaration.setDeclarationName(taxType + year + "年度申报");
        declaration.setTaxType(taxType);
        declaration.setDeclarationType("YEARLY");
        declaration.setDeclarationStatus("DRAFT");
        declaration.setDeclarationPeriod(year.toString());
        declaration.setDeclarationYear(year);
        declaration.setPriority("HIGH");
        
        // 设置申报截止日期（次年5月31日）
        LocalDateTime deadline = LocalDateTime.of(year + 1, 5, 31, 23, 59, 59);
        declaration.setDeadline(deadline);
        
        return declaration;
    }

    /**
     * 执行自动填报
     */
    private Map<String, Object> performAutoFill(TsTaxDeclaration declaration) {
        Map<String, Object> data = new HashMap<>();
        
        // 模拟自动填报逻辑
        data.put("taxpayerId", declaration.getTaxpayerId());
        data.put("taxpayerName", declaration.getTaxpayerName());
        data.put("declarationPeriod", declaration.getDeclarationPeriod());
        data.put("taxType", declaration.getTaxType());
        
        // 根据税种填充不同的数据
        switch (declaration.getTaxType()) {
            case "VAT":
                data.put("salesAmount", new BigDecimal("1000000"));
                data.put("inputTax", new BigDecimal("130000"));
                data.put("outputTax", new BigDecimal("170000"));
                break;
            case "CIT":
                data.put("revenue", new BigDecimal("5000000"));
                data.put("cost", new BigDecimal("3000000"));
                data.put("profit", new BigDecimal("2000000"));
                break;
            default:
                data.put("amount", new BigDecimal("100000"));
                break;
        }
        
        return data;
    }

    /**
     * 执行提交
     */
    private Map<String, Object> performSubmit(TsTaxDeclaration declaration) {
        Map<String, Object> result = new HashMap<>();
        
        // 模拟提交到税务系统
        String receiptNumber = "RCP" + System.currentTimeMillis();
        result.put("receiptNumber", receiptNumber);
        result.put("submitTime", LocalDateTime.now());
        result.put("status", "SUCCESS");
        
        return result;
    }

    /**
     * 执行撤回
     */
    private void performWithdraw(TsTaxDeclaration declaration) {
        // 模拟撤回逻辑
        log.info("执行申报撤回: {}", declaration.getDeclarationId());
    }

    /**
     * 获取计税依据金额
     */
    private BigDecimal getTaxBaseAmount(TsTaxDeclaration declaration) {
        // 模拟获取计税依据
        switch (declaration.getTaxType()) {
            case "VAT":
                return new BigDecimal("1000000"); // 销售额
            case "CIT":
                return new BigDecimal("2000000"); // 应纳税所得额
            case "IIT":
                return new BigDecimal("50000");   // 应纳税所得额
            default:
                return new BigDecimal("100000");
        }
    }

    /**
     * 获取税率
     */
    private BigDecimal getTaxRateByType(String taxType) {
        switch (taxType) {
            case "VAT":
                return new BigDecimal("0.13");   // 13%
            case "CIT":
                return new BigDecimal("0.25");   // 25%
            case "IIT":
                return new BigDecimal("0.20");   // 20%
            default:
                return new BigDecimal("0.10");   // 10%
        }
    }

    /**
     * 获取已缴税额
     */
    private BigDecimal getPaidTaxAmount(TsTaxDeclaration declaration) {
        // 模拟获取已缴税额
        return new BigDecimal("100000");
    }

    /**
     * 获取计算方法
     */
    private String getCalculationMethod(String taxType) {
        switch (taxType) {
            case "VAT":
                return "销售额 × 税率";
            case "CIT":
                return "应纳税所得额 × 税率";
            case "IIT":
                return "应纳税所得额 × 税率";
            default:
                return "计税依据 × 税率";
        }
    }

    /**
     * 获取计算详情
     */
    private Map<String, Object> getCalculationDetails(TsTaxDeclaration declaration) {
        Map<String, Object> details = new HashMap<>();
        details.put("taxType", declaration.getTaxType());
        details.put("period", declaration.getDeclarationPeriod());
        details.put("calculationDate", LocalDateTime.now());
        return details;
    }

    // ==================== 申报跟踪功能 ====================

    @Override
    public Map<String, Object> getDeclarationProgress(Long tenantId, Long declarationId) {
        TsTaxDeclaration declaration = getDeclarationById(tenantId, declarationId);
        if (declaration == null) {
            throw new RuntimeException("税务申报不存在: " + declarationId);
        }
        
        Map<String, Object> progress = new HashMap<>();
        progress.put("declarationId", declarationId);
        progress.put("status", declaration.getDeclarationStatus());
        progress.put("progress", calculateProgress(declaration.getDeclarationStatus()));
        progress.put("currentStep", getCurrentStep(declaration.getDeclarationStatus()));
        progress.put("nextStep", getNextStep(declaration.getDeclarationStatus()));
        progress.put("deadline", declaration.getDeadline());
        progress.put("submitTime", declaration.getSubmitTime());
        progress.put("reviewTime", declaration.getReviewTime());
        progress.put("completeTime", declaration.getCompleteTime());
        
        return progress;
    }

    @Override
    public boolean updateDeclarationStatus(Long tenantId, Long declarationId, String status, String remark) {
        TsTaxDeclaration declaration = getDeclarationById(tenantId, declarationId);
        if (declaration == null) {
            throw new RuntimeException("税务申报不存在: " + declarationId);
        }
        
        declaration.setDeclarationStatus(status);
        if (StringUtils.hasText(remark)) {
            declaration.setRemark(remark);
        }
        
        return updateById(declaration);
    }

    @Override
    public List<Map<String, Object>> getDeclarationHistory(Long tenantId, Long declarationId) {
        // 模拟获取申报历史
        List<Map<String, Object>> history = new ArrayList<>();
        
        Map<String, Object> record1 = new HashMap<>();
        record1.put("action", "CREATE");
        record1.put("status", "DRAFT");
        record1.put("time", LocalDateTime.now().minusDays(5));
        record1.put("operator", "系统");
        record1.put("remark", "创建申报任务");
        history.add(record1);
        
        Map<String, Object> record2 = new HashMap<>();
        record2.put("action", "FILL");
        record2.put("status", "FILLED");
        record2.put("time", LocalDateTime.now().minusDays(3));
        record2.put("operator", "张三");
        record2.put("remark", "完成申报表填写");
        history.add(record2);
        
        Map<String, Object> record3 = new HashMap<>();
        record3.put("action", "SUBMIT");
        record3.put("status", "SUBMITTED");
        record3.put("time", LocalDateTime.now().minusDays(1));
        record3.put("operator", "李四");
        record3.put("remark", "提交申报");
        history.add(record3);
        
        return history;
    }

    @Override
    public List<Map<String, Object>> getDeclarationLogs(Long tenantId, Long declarationId) {
        // 模拟获取申报日志
        List<Map<String, Object>> logs = new ArrayList<>();
        
        Map<String, Object> log1 = new HashMap<>();
        log1.put("level", "INFO");
        log1.put("message", "申报任务创建成功");
        log1.put("time", LocalDateTime.now().minusDays(5));
        logs.add(log1);
        
        Map<String, Object> log2 = new HashMap<>();
        log2.put("level", "INFO");
        log2.put("message", "开始自动填报");
        log2.put("time", LocalDateTime.now().minusDays(3));
        logs.add(log2);
        
        Map<String, Object> log3 = new HashMap<>();
        log3.put("level", "INFO");
        log3.put("message", "申报提交成功，受理回执号：RCP123456789");
        log3.put("time", LocalDateTime.now().minusDays(1));
        logs.add(log3);
        
        return logs;
    }

    /**
     * 计算进度百分比
     */
    private int calculateProgress(String status) {
        switch (status) {
            case "DRAFT": return 10;
            case "FILLING": return 30;
            case "FILLED": return 50;
            case "SUBMITTING": return 70;
            case "SUBMITTED": return 80;
            case "UNDER_REVIEW": return 90;
            case "APPROVED": return 100;
            case "COMPLETED": return 100;
            default: return 0;
        }
    }

    /**
     * 获取当前步骤
     */
    private String getCurrentStep(String status) {
        switch (status) {
            case "DRAFT": return "草稿";
            case "FILLING": return "填报中";
            case "FILLED": return "已填报";
            case "SUBMITTING": return "提交中";
            case "SUBMITTED": return "已提交";
            case "UNDER_REVIEW": return "审核中";
            case "APPROVED": return "已审核";
            case "COMPLETED": return "已完成";
            default: return "未知";
        }
    }

    /**
     * 获取下一步骤
     */
    private String getNextStep(String status) {
        switch (status) {
            case "DRAFT": return "填报申报表";
            case "FILLING": return "完成填报";
            case "FILLED": return "提交申报";
            case "SUBMITTING": return "等待受理";
            case "SUBMITTED": return "等待审核";
            case "UNDER_REVIEW": return "等待审核结果";
            case "APPROVED": return "完成申报";
            case "COMPLETED": return "无";
            default: return "未知";
        }
    }

    // ==================== 申报提醒功能 ====================

    @Override
    public List<TsTaxDeclaration> getPendingDeclarations(Long tenantId, Integer limit) {
        return baseMapper.selectPendingDeclarations(tenantId, limit);
    }

    @Override
    public List<TsTaxDeclaration> getPendingReviewDeclarations(Long tenantId, Integer limit) {
        return baseMapper.selectPendingReviewDeclarations(tenantId, limit);
    }

    @Override
    public List<TsTaxDeclaration> getOverdueDeclarations(Long tenantId) {
        return baseMapper.selectOverdueDeclarations(tenantId, LocalDateTime.now());
    }

    @Override
    public List<TsTaxDeclaration> getUpcomingDeclarations(Long tenantId, Integer days) {
        return baseMapper.selectUpcomingDeclarations(tenantId, days);
    }

    @Override
    public boolean sendDeclarationReminder(Long tenantId, Long declarationId) {
        log.info("发送申报提醒: {}", declarationId);
        
        TsTaxDeclaration declaration = getDeclarationById(tenantId, declarationId);
        if (declaration == null) {
            throw new RuntimeException("税务申报不存在: " + declarationId);
        }
        
        // 模拟发送提醒
        log.info("提醒已发送: 申报名称={}, 截止日期={}", declaration.getDeclarationName(), declaration.getDeadline());
        return true;
    }

    @Override
    public Map<String, Object> batchSendDeclarationReminders(Long tenantId, List<Long> declarationIds) {
        log.info("批量发送申报提醒: {}", declarationIds);
        
        int successCount = 0;
        int failCount = 0;
        List<String> errors = new ArrayList<>();
        
        for (Long declarationId : declarationIds) {
            try {
                if (sendDeclarationReminder(tenantId, declarationId)) {
                    successCount++;
                } else {
                    failCount++;
                }
            } catch (Exception e) {
                failCount++;
                errors.add("申报ID " + declarationId + ": " + e.getMessage());
            }
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("errors", errors);
        
        log.info("批量发送申报提醒完成，成功: {}, 失败: {}", successCount, failCount);
        return result;
    }

    // ==================== 统计分析功能 ====================

    @Override
    public Map<String, Object> getDeclarationOverview(Long tenantId, LocalDateTime startDate, LocalDateTime endDate) {
        return baseMapper.getDeclarationOverview(tenantId, startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> countDeclarationsByStatus(Long tenantId, LocalDateTime startDate, LocalDateTime endDate) {
        return baseMapper.countDeclarationsByStatus(tenantId, startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> countDeclarationsByTaxType(Long tenantId, LocalDateTime startDate, LocalDateTime endDate) {
        return baseMapper.countDeclarationsByTaxType(tenantId, startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> countDeclarationsByType(Long tenantId, LocalDateTime startDate, LocalDateTime endDate) {
        return baseMapper.countDeclarationsByType(tenantId, startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> sumTaxAmountByMonth(Long tenantId, Integer year) {
        return baseMapper.sumTaxAmountByMonth(tenantId, year);
    }

    @Override
    public List<Map<String, Object>> sumTaxAmountByTaxType(Long tenantId, LocalDateTime startDate, LocalDateTime endDate) {
        return baseMapper.sumTaxAmountByTaxType(tenantId, startDate, endDate);
    }

    @Override
    public Map<String, Object> calculateTimelyRate(Long tenantId, LocalDateTime startDate, LocalDateTime endDate) {
        return baseMapper.calculateTimelyRate(tenantId, startDate, endDate);
    }

    @Override
    public Map<String, Object> calculateSuccessRate(Long tenantId, LocalDateTime startDate, LocalDateTime endDate) {
        return baseMapper.calculateSuccessRate(tenantId, startDate, endDate);
    }

    @Override
    public Map<String, Object> calculateProcessingTimeStats(Long tenantId, LocalDateTime startDate, LocalDateTime endDate) {
        return baseMapper.calculateProcessingTimeStats(tenantId, startDate, endDate);
    }

    @Override
    public List<Map<String, Object>> getDeclarationTrend(Long tenantId, LocalDateTime startDate, LocalDateTime endDate, String groupBy) {
        return baseMapper.getDeclarationTrend(tenantId, startDate, endDate, groupBy);
    }

    @Override
    public List<Map<String, Object>> getTaxAmountTrend(Long tenantId, LocalDateTime startDate, LocalDateTime endDate, String groupBy) {
        return baseMapper.getTaxAmountTrend(tenantId, startDate, endDate, groupBy);
    }

    @Override
    public Map<String, Object> getDeclarationEfficiencyStats(Long tenantId, LocalDateTime startDate, LocalDateTime endDate) {
        return baseMapper.getDeclarationEfficiencyStats(tenantId, startDate, endDate);
    }

    @Override
    public Map<String, Object> getDeclarationQualityStats(Long tenantId, LocalDateTime startDate, LocalDateTime endDate) {
        return baseMapper.getDeclarationQualityStats(tenantId, startDate, endDate);
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

    // ==================== 批量操作功能 ====================

    @Override
    public Map<String, Object> batchUpdateStatus(Long tenantId, List<Long> declarationIds, String status) {
        log.info("批量更新申报状态: {}, 状态: {}", declarationIds, status);
        
        int updateCount = baseMapper.batchUpdateStatus(tenantId, declarationIds, status, "系统", LocalDateTime.now());
        
        Map<String, Object> result = new HashMap<>();
        result.put("updateCount", updateCount);
        result.put("successCount", updateCount);
        result.put("failCount", declarationIds.size() - updateCount);
        
        log.info("批量更新申报状态完成，更新数量: {}", updateCount);
        return result;
    }

    @Override
    public Map<String, Object> batchDeleteDeclarations(Long tenantId, List<Long> declarationIds) {
        log.info("批量删除申报: {}", declarationIds);
        
        int deleteCount = baseMapper.batchDeleteDeclarations(tenantId, declarationIds, "系统", LocalDateTime.now());
        
        Map<String, Object> result = new HashMap<>();
        result.put("deleteCount", deleteCount);
        result.put("successCount", deleteCount);
        result.put("failCount", declarationIds.size() - deleteCount);
        
        log.info("批量删除申报完成，删除数量: {}", deleteCount);
        return result;
    }

    @Override
    public Map<String, Object> batchImportDeclarations(Long tenantId, List<Map<String, Object>> declarationData) {
        log.info("批量导入申报: {}", declarationData.size());
        
        int successCount = 0;
        int failCount = 0;
        List<String> errors = new ArrayList<>();
        
        for (Map<String, Object> data : declarationData) {
            try {
                TsTaxDeclaration declaration = convertMapToDeclaration(tenantId, data);
                createDeclaration(declaration);
                successCount++;
            } catch (Exception e) {
                failCount++;
                errors.add("导入失败: " + e.getMessage());
            }
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("errors", errors);
        
        log.info("批量导入申报完成，成功: {}, 失败: {}", successCount, failCount);
        return result;
    }

    @Override
    public List<Map<String, Object>> batchExportDeclarations(Long tenantId, List<Long> declarationIds) {
        log.info("批量导出申报: {}", declarationIds);
        
        List<Map<String, Object>> exportData = new ArrayList<>();
        
        for (Long declarationId : declarationIds) {
            TsTaxDeclaration declaration = getDeclarationById(tenantId, declarationId);
            if (declaration != null) {
                Map<String, Object> data = convertDeclarationToMap(declaration);
                exportData.add(data);
            }
        }
        
        log.info("批量导出申报完成，导出数量: {}", exportData.size());
        return exportData;
    }

    /**
     * 将Map转换为申报实体
     */
    private TsTaxDeclaration convertMapToDeclaration(Long tenantId, Map<String, Object> data) {
        TsTaxDeclaration declaration = new TsTaxDeclaration();
        declaration.setTenantId(tenantId);
        declaration.setDeclarationName((String) data.get("declarationName"));
        declaration.setTaxType((String) data.get("taxType"));
        declaration.setDeclarationType((String) data.get("declarationType"));
        declaration.setTaxpayerId((String) data.get("taxpayerId"));
        declaration.setTaxpayerName((String) data.get("taxpayerName"));
        declaration.setDeclarationPeriod((String) data.get("declarationPeriod"));
        
        if (data.get("taxBase") != null) {
            declaration.setTaxBase(new BigDecimal(data.get("taxBase").toString()));
        }
        if (data.get("taxRate") != null) {
            declaration.setTaxRate(new BigDecimal(data.get("taxRate").toString()));
        }
        
        return declaration;
    }

    /**
     * 将申报实体转换为Map
     */
    private Map<String, Object> convertDeclarationToMap(TsTaxDeclaration declaration) {
        Map<String, Object> data = new HashMap<>();
        data.put("declarationId", declaration.getDeclarationId());
        data.put("declarationCode", declaration.getDeclarationCode());
        data.put("declarationName", declaration.getDeclarationName());
        data.put("taxType", declaration.getTaxType());
        data.put("declarationType", declaration.getDeclarationType());
        data.put("declarationStatus", declaration.getDeclarationStatus());
        data.put("taxpayerId", declaration.getTaxpayerId());
        data.put("taxpayerName", declaration.getTaxpayerName());
        data.put("declarationPeriod", declaration.getDeclarationPeriod());
        data.put("taxBase", declaration.getTaxBase());
        data.put("taxRate", declaration.getTaxRate());
        data.put("taxAmount", declaration.getTaxAmount());
        data.put("deadline", declaration.getDeadline());
        data.put("submitTime", declaration.getSubmitTime());
        data.put("createdTime", declaration.getCreatedTime());
        return data;
    }

    // ==================== 查询功能 ====================

    @Override
    public TsTaxDeclaration getDeclarationByCode(Long tenantId, String declarationCode) {
        return baseMapper.selectByDeclarationCode(tenantId, declarationCode);
    }

    @Override
    public List<TsTaxDeclaration> getDeclarationsByTaxpayerId(Long tenantId, String taxpayerId) {
        return baseMapper.selectByTaxpayerId(tenantId, taxpayerId);
    }

    @Override
    public IPage<TsTaxDeclaration> advancedSearchDeclarations(Page<TsTaxDeclaration> page, Long tenantId, Map<String, Object> searchParams) {
        return baseMapper.advancedSearchDeclarations(page, tenantId, searchParams);
    }

    @Override
    public IPage<TsTaxDeclaration> fullTextSearchDeclarations(Page<TsTaxDeclaration> page, Long tenantId, String keyword) {
        return baseMapper.fullTextSearchDeclarations(page, tenantId, keyword);
    }

    // ==================== 系统维护功能 ====================

    @Override
    public List<TsTaxDeclaration> getAbnormalDeclarations(Long tenantId, List<String> errorTypes) {
        return baseMapper.selectAbnormalDeclarations(tenantId, errorTypes);
    }

    @Override
    public List<TsTaxDeclaration> getRetryDeclarations(Long tenantId) {
        return baseMapper.selectRetryDeclarations(tenantId);
    }

    @Override
    public boolean retryDeclaration(Long tenantId, Long declarationId) {
        log.info("重试申报: {}", declarationId);
        
        TsTaxDeclaration declaration = getDeclarationById(tenantId, declarationId);
        if (declaration == null) {
            throw new RuntimeException("税务申报不存在: " + declarationId);
        }
        
        // 检查重试次数
        if (declaration.getRetryCount() >= declaration.getMaxRetryCount()) {
            throw new RuntimeException("已达到最大重试次数: " + declaration.getMaxRetryCount());
        }
        
        try {
            // 增加重试次数
            declaration.setRetryCount(declaration.getRetryCount() + 1);
            declaration.setNextRetryTime(LocalDateTime.now().plusHours(1));
            
            // 重置状态
            declaration.setDeclarationStatus("DRAFT");
            declaration.setErrorMessage(null);
            declaration.setErrorCode(null);
            
            updateById(declaration);
            
            log.info("申报重试设置成功: {}", declarationId);
            return true;
            
        } catch (Exception e) {
            log.error("申报重试失败: {}", declarationId, e);
            return false;
        }
    }

    @Override
    public int cleanExpiredDeclarations(Long tenantId, LocalDateTime expiredDate) {
        log.info("清理过期申报: 租户={}, 过期日期={}", tenantId, expiredDate);
        
        int cleanCount = baseMapper.cleanExpiredDeclarations(tenantId, expiredDate);
        
        log.info("清理过期申报完成，清理数量: {}", cleanCount);
        return cleanCount;
    }

    @Override
    public Map<String, Object> healthCheck(Long tenantId) {
        log.info("系统健康检查: {}", tenantId);
        
        return baseMapper.healthCheck(tenantId);
    }

    @Override
    public Map<String, Object> getPerformanceMetrics(Long tenantId) {
        log.info("获取系统性能指标: {}", tenantId);
        
        return baseMapper.getPerformanceMetrics(tenantId);
    }
}
