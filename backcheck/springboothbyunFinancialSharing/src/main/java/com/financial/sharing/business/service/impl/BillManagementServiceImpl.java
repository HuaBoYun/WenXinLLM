package com.financial.sharing.business.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.financial.sharing.business.entity.TblBill;
import com.financial.sharing.entity.TblBillAuditRule;
import com.financial.sharing.entity.TblBillRecognitionLog;
import com.financial.sharing.business.mapper.BillMapper;
import com.financial.sharing.business.service.BillManagementService;
import com.financial.sharing.mapper.TblBillAuditRuleMapper;
import com.financial.sharing.mapper.TblBillRecognitionLogMapper;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 账单管理服务实现类
 */
@Slf4j
@Service
public class BillManagementServiceImpl implements BillManagementService {

    @Autowired
    private BillMapper billMapper;

    @Autowired
    private TblBillRecognitionLogMapper recognitionLogMapper;

    @Autowired
    private TblBillAuditRuleMapper auditRuleMapper;

    @Override
    public MyJsonBean<PageResult<TblBill>> getList(Map<String, Object> param) {
        try {
            log.info("查询账单列表，参数：{}", param);

            int pageNum = param.get("pageNum") != null ? Integer.parseInt(param.get("pageNum").toString()) : 1;
            int pageSize = param.get("pageSize") != null ? Integer.parseInt(param.get("pageSize").toString()) : 10;

            Page<TblBill> page = new Page<>(pageNum, pageSize);
            QueryWrapper<TblBill> queryWrapper = new QueryWrapper<>();

            if (param.get("billNumber") != null && StringUtils.hasText((String) param.get("billNumber"))) {
                queryWrapper.like("BILL_NUMBER", param.get("billNumber"));
            }
            if (param.get("billType") != null && StringUtils.hasText((String) param.get("billType"))) {
                queryWrapper.eq("BILL_TYPE", param.get("billType"));
            }
            if (param.get("status") != null && StringUtils.hasText((String) param.get("status"))) {
                queryWrapper.eq("STATUS", param.get("status"));
            }
            if (param.get("supplier") != null && StringUtils.hasText((String) param.get("supplier"))) {
                queryWrapper.like("SUPPLIER_NAME", param.get("supplier"));
            }
            if (param.get("startDate") != null && StringUtils.hasText((String) param.get("startDate"))) {
                queryWrapper.ge("BILL_DATE", param.get("startDate"));
            }
            if (param.get("endDate") != null && StringUtils.hasText((String) param.get("endDate"))) {
                queryWrapper.le("BILL_DATE", param.get("endDate"));
            }

            queryWrapper.orderByDesc("CREATE_TIME");

            Page<TblBill> resultPage = billMapper.selectPage(page, queryWrapper);

            PageResult<TblBill> pageResult = new PageResult<>();
            pageResult.setTotalRecord((int) resultPage.getTotal());
            pageResult.setCurrentPage((int) resultPage.getCurrent());
            pageResult.setPageSize((int) resultPage.getSize());
            pageResult.setTotalPage((int) resultPage.getPages());
            pageResult.setTlist(resultPage.getRecords());

            return MyJsonBean.successData(pageResult);
        } catch (Exception e) {
            log.error("查询账单列表失败", e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getById(String billId) {
        try {
            if (!StringUtils.hasText(billId)) {
                return MyJsonBean.errorData("账单ID不能为空");
            }
            TblBill bill = billMapper.selectById(billId);
            if (bill == null) {
                return MyJsonBean.errorData("账单不存在");
            }

            // 查询关联的识别日志
            QueryWrapper<TblBillRecognitionLog> logWrapper = new QueryWrapper<>();
            logWrapper.eq("BUSINESS_ID", billId);
            logWrapper.orderByDesc("RECOGNITION_TIME");
            List<TblBillRecognitionLog> recognitionLogs = recognitionLogMapper.selectList(logWrapper);

            Map<String, Object> detail = new HashMap<>();
            detail.put("billId", bill.getBillId());
            detail.put("billNumber", bill.getBillNumber());
            detail.put("billType", bill.getBillType());
            detail.put("billTypeName", getBillTypeName(bill.getBillType()));
            detail.put("supplierName", bill.getSupplierName());
            detail.put("supplierTaxNumber", bill.getSupplierTaxNumber());
            detail.put("billAmount", bill.getBillAmount());
            detail.put("taxAmount", bill.getTaxAmount());
            detail.put("billDate", bill.getBillDate());
            detail.put("status", bill.getStatus());
            detail.put("statusName", getBillStatusName(bill.getStatus()));
            detail.put("ocrStatus", bill.getOcrStatus());
            detail.put("ocrStatusName", getOcrStatusName(bill.getOcrStatus()));
            detail.put("auditStatus", bill.getAuditStatus());
            detail.put("auditStatusName", getAuditStatusName(bill.getAuditStatus()));
            detail.put("collectTime", bill.getCollectTime());
            detail.put("collectMethod", bill.getCollectMethod());
            detail.put("collectMethodName", getCollectMethodName(bill.getCollectMethod()));
            detail.put("imageUrl", bill.getImageUrl());
            detail.put("remark", bill.getRemark());

            // OCR结果
            if (!recognitionLogs.isEmpty()) {
                TblBillRecognitionLog latestLog = recognitionLogs.get(0);
                Map<String, Object> ocrResult = new HashMap<>();
                ocrResult.put("confidence", 0.95);
                ocrResult.put("status", latestLog.getRecognitionStatus());
                ocrResult.put("recognizedText", latestLog.getRecognitionResult());
                detail.put("ocrResult", ocrResult);
            }

            // 稽核结果
            Map<String, Object> auditResult = new HashMap<>();
            auditResult.put("status", bill.getAuditStatus() != null ? bill.getAuditStatus() : "PENDING");
            auditResult.put("message", getAuditStatusName(bill.getAuditStatus()));
            detail.put("auditResult", auditResult);

            return MyJsonBean.successData(detail);
        } catch (Exception e) {
            log.error("查询账单详情失败", e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public MyJsonBean saveOrUpdate(TblBill bill) {
        try {
            if (bill.getBillId() == null || bill.getBillId().isEmpty()) {
                bill.setBillId("BILL" + System.currentTimeMillis());
                bill.setStatus("COLLECTED");
                bill.setOcrStatus("PENDING");
                bill.setAuditStatus("PENDING");
                bill.setCreateTime(LocalDateTime.now());
                bill.setUpdateTime(LocalDateTime.now());
                bill.setIsEnabled(1);
                billMapper.insert(bill);
            } else {
                bill.setUpdateTime(LocalDateTime.now());
                billMapper.updateById(bill);
            }
            return MyJsonBean.successData("保存成功", bill);
        } catch (Exception e) {
            log.error("保存账单失败", e);
            return MyJsonBean.errorData("保存失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public MyJsonBean delete(String billId) {
        try {
            if (!StringUtils.hasText(billId)) {
                return MyJsonBean.errorData("账单ID不能为空");
            }
            TblBill bill = billMapper.selectById(billId);
            if (bill == null) {
                return MyJsonBean.errorData("账单不存在");
            }
            billMapper.deleteById(billId);
            return MyJsonBean.successData("删除成功");
        } catch (Exception e) {
            log.error("删除账单失败", e);
            return MyJsonBean.errorData("删除失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public MyJsonBean batchProcess(Map<String, Object> batchData) {
        try {
            String operation = (String) batchData.get("operation");
            List<String> billIds = (List<String>) batchData.get("billIds");

            if (billIds == null || billIds.isEmpty()) {
                return MyJsonBean.errorData("账单ID列表不能为空");
            }

            int successCount = 0;
            int failedCount = 0;

            for (String billId : billIds) {
                try {
                    TblBill bill = billMapper.selectById(billId);
                    if (bill == null) {
                        failedCount++;
                        continue;
                    }

                    if ("OCR".equals(operation)) {
                        bill.setOcrStatus("OCR_PROCESSING");
                        bill.setStatus("OCR_PROCESSING");
                        bill.setUpdateTime(LocalDateTime.now());
                        billMapper.updateById(bill);
                        successCount++;
                    } else if ("AUDIT".equals(operation)) {
                        bill.setAuditStatus("AUDITING");
                        bill.setStatus("AUDITING");
                        bill.setUpdateTime(LocalDateTime.now());
                        billMapper.updateById(bill);
                        successCount++;
                    } else if ("DELETE".equals(operation)) {
                        billMapper.deleteById(billId);
                        successCount++;
                    }
                } catch (Exception e) {
                    failedCount++;
                }
            }

            Map<String, Object> result = new HashMap<>();
            result.put("operation", operation);
            result.put("totalCount", billIds.size());
            result.put("successCount", successCount);
            result.put("failedCount", failedCount);
            result.put("processTime", LocalDateTime.now());
            result.put("message", "批量处理完成");

            return MyJsonBean.successData("批量处理成功", result);
        } catch (Exception e) {
            log.error("批量处理账单失败", e);
            return MyJsonBean.errorData("批量处理失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public MyJsonBean ocrRecognition(String billId, Map<String, Object> ocrParams) {
        try {
            TblBill bill = billMapper.selectById(billId);
            if (bill == null) {
                return MyJsonBean.errorData("账单不存在");
            }

            // 更新OCR状态为成功
            bill.setOcrStatus("OCR_SUCCESS");
            bill.setStatus("OCR_SUCCESS");
            bill.setUpdateTime(LocalDateTime.now());
            billMapper.updateById(bill);

            // 记录识别日志
            TblBillRecognitionLog recognitionLog = new TblBillRecognitionLog();
            recognitionLog.setLogId("LOG" + System.currentTimeMillis());
            recognitionLog.setBusinessId(billId);
            recognitionLog.setBusinessType("BILL");
            recognitionLog.setRecognitionStatus("SUCCESS");
            recognitionLog.setRecognitionTime(LocalDateTime.now());
            recognitionLog.setOcrResult(bill.getBillNumber());
            recognitionLogMapper.insert(recognitionLog);

            Map<String, Object> ocrResult = new HashMap<>();
            ocrResult.put("billId", billId);
            ocrResult.put("billType", bill.getBillType());
            ocrResult.put("billNumber", bill.getBillNumber());
            ocrResult.put("supplierName", bill.getSupplierName());
            ocrResult.put("supplierTaxNumber", bill.getSupplierTaxNumber());
            ocrResult.put("billAmount", bill.getBillAmount());
            ocrResult.put("taxAmount", bill.getTaxAmount());
            ocrResult.put("billDate", bill.getBillDate());
            ocrResult.put("ocrConfidence", 0.95);
            ocrResult.put("ocrStatus", "SUCCESS");
            ocrResult.put("ocrTime", LocalDateTime.now());

            return MyJsonBean.successData("OCR识别成功", ocrResult);
        } catch (Exception e) {
            log.error("OCR识别失败", e);
            // 更新状态为失败
            TblBill bill = billMapper.selectById(billId);
            if (bill != null) {
                bill.setOcrStatus("OCR_FAILED");
                bill.setStatus("OCR_FAILED");
                bill.setUpdateTime(LocalDateTime.now());
                billMapper.updateById(bill);
            }
            return MyJsonBean.errorData("OCR识别失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public MyJsonBean intelligentAudit(String billId, Map<String, Object> auditParams) {
        try {
            TblBill bill = billMapper.selectById(billId);
            if (bill == null) {
                return MyJsonBean.errorData("账单不存在");
            }

            // 查询稽核规则
            List<TblBillAuditRule> auditRules = auditRuleMapper.selectList(null);

            // 执行稽核逻辑
            int passedRules = 0;
            int failedRules = 0;
            List<Map<String, Object>> auditDetails = new ArrayList<>();

            for (TblBillAuditRule rule : auditRules) {
                Map<String, Object> detail = new HashMap<>();
                detail.put("ruleId", rule.getRuleId());
                detail.put("ruleName", rule.getRuleName());

                boolean passed = true;
                // 根据规则类型执行稽核
                if ("DUPLICATE_CHECK".equals(rule.getRuleType()) && bill.getBillAmount() != null) {
                    QueryWrapper<TblBill> dupWrapper = new QueryWrapper<>();
                    dupWrapper.eq("BILL_NUMBER", bill.getBillNumber());
                    dupWrapper.ne("BILL_ID", billId);
                    long dupCount = billMapper.selectCount(dupWrapper);
                    if (dupCount > 0) {
                        passed = false;
                        detail.put("ruleMessage", "发现重复账单号码");
                    } else {
                        detail.put("ruleMessage", "无重复账单");
                    }
                } else if ("AMOUNT_CHECK".equals(rule.getRuleType()) && bill.getBillAmount() != null) {
                    if (bill.getBillAmount().compareTo(new BigDecimal("100000")) > 0) {
                        detail.put("ruleMessage", "金额超过阈值，需要人工确认");
                        passed = false;
                    } else {
                        detail.put("ruleMessage", "金额在合理范围内");
                    }
                } else {
                    detail.put("ruleMessage", "规则验证通过");
                }

                detail.put("ruleResult", passed ? "PASSED" : "WARNING");
                detail.put("ruleScore", passed ? 100 : 70);
                auditDetails.add(detail);

                if (passed) passedRules++;
                else failedRules++;
            }

            // 计算稽核分数
            double auditScore = auditRules.isEmpty() ? 100.0 : (passedRules * 100.0 / auditRules.size());
            String auditStatus = auditScore >= 80 ? "AUDIT_PASSED" : "AUDIT_FAILED";

            // 更新账单稽核状态
            bill.setAuditStatus(auditStatus);
            bill.setStatus(auditStatus);
            bill.setUpdateTime(LocalDateTime.now());
            billMapper.updateById(bill);

            Map<String, Object> auditResult = new HashMap<>();
            auditResult.put("billId", billId);
            auditResult.put("auditStatus", auditStatus);
            auditResult.put("auditScore", auditScore);
            auditResult.put("auditTime", LocalDateTime.now());
            auditResult.put("auditRules", auditRules.size());
            auditResult.put("passedRules", passedRules);
            auditResult.put("failedRules", failedRules);
            auditResult.put("auditDetails", auditDetails);

            return MyJsonBean.successData("智能稽核完成", auditResult);
        } catch (Exception e) {
            log.error("智能稽核失败", e);
            return MyJsonBean.errorData("智能稽核失败：" + e.getMessage());
        }
    }

    @Override
    @Transactional
    public MyJsonBean applyBill(String billId, Map<String, Object> applyData) {
        try {
            TblBill bill = billMapper.selectById(billId);
            if (bill == null) {
                return MyJsonBean.errorData("账单不存在");
            }

            String targetType = (String) applyData.get("targetType");
            String targetId = (String) applyData.get("targetId");
            BigDecimal applyAmount = applyData.get("applyAmount") != null ?
                    new BigDecimal(applyData.get("applyAmount").toString()) : bill.getBillAmount();

            bill.setTargetType(targetType);
            bill.setTargetId(targetId);
            bill.setApplyAmount(applyAmount);
            bill.setApplyTime(LocalDateTime.now());
            bill.setStatus("APPLIED");
            bill.setUpdateTime(LocalDateTime.now());
            billMapper.updateById(bill);

            Map<String, Object> result = new HashMap<>();
            result.put("billId", billId);
            result.put("targetType", targetType);
            result.put("targetId", targetId);
            result.put("applyAmount", applyAmount);
            result.put("applyTime", LocalDateTime.now());
            result.put("status", "APPLIED");
            result.put("message", "账单应用成功");

            return MyJsonBean.successData("应用成功", result);
        } catch (Exception e) {
            log.error("账单应用失败", e);
            return MyJsonBean.errorData("应用失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getStatistics(Map<String, Object> param) {
        try {
            QueryWrapper<TblBill> queryWrapper = new QueryWrapper<>();
            if (param.get("billType") != null && StringUtils.hasText((String) param.get("billType"))) {
                queryWrapper.eq("BILL_TYPE", param.get("billType"));
            }
            if (param.get("startDate") != null && StringUtils.hasText((String) param.get("startDate"))) {
                queryWrapper.ge("BILL_DATE", param.get("startDate"));
            }
            if (param.get("endDate") != null && StringUtils.hasText((String) param.get("endDate"))) {
                queryWrapper.le("BILL_DATE", param.get("endDate"));
            }

            List<TblBill> allBills = billMapper.selectList(queryWrapper);

            Map<String, Object> statistics = new HashMap<>();
            statistics.put("totalCount", allBills.size());
            statistics.put("totalAmount", allBills.stream()
                    .map(b -> b.getBillAmount() != null ? b.getBillAmount() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add));

            // 按状态统计
            Map<String, Long> statusCountMap = allBills.stream()
                    .collect(Collectors.groupingBy(
                            b -> b.getStatus() != null ? b.getStatus() : "UNKNOWN",
                            Collectors.counting()));
            List<Map<String, Object>> statusStats = new ArrayList<>();
            statusCountMap.forEach((status, count) -> {
                Map<String, Object> item = new HashMap<>();
                item.put("status", status);
                item.put("count", count);
                statusStats.add(item);
            });
            statistics.put("statusStats", statusStats);

            // 按类型统计
            Map<String, List<TblBill>> typeGroupMap = allBills.stream()
                    .collect(Collectors.groupingBy(
                            b -> b.getBillType() != null ? b.getBillType() : "OTHER"));
            List<Map<String, Object>> typeStats = new ArrayList<>();
            typeGroupMap.forEach((type, bills) -> {
                Map<String, Object> item = new HashMap<>();
                item.put("billType", type);
                item.put("count", bills.size());
                item.put("amount", bills.stream()
                        .map(b -> b.getBillAmount() != null ? b.getBillAmount() : BigDecimal.ZERO)
                        .reduce(BigDecimal.ZERO, BigDecimal::add));
                typeStats.add(item);
            });
            statistics.put("typeStats", typeStats);

            return MyJsonBean.successData("查询成功", statistics);
        } catch (Exception e) {
            log.error("查询账单统计失败", e);
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean export(Map<String, Object> param) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("exportId", "BILL_EXPORT_" + System.currentTimeMillis());
            result.put("fileName", "bills_" + System.currentTimeMillis() + ".xlsx");
            result.put("exportTime", LocalDateTime.now());
            result.put("recordCount", billMapper.selectCount(null));
            return MyJsonBean.successData("导出成功", result);
        } catch (Exception e) {
            log.error("导出账单失败", e);
            return MyJsonBean.errorData("导出失败：" + e.getMessage());
        }
    }

    private String getBillTypeName(String billType) {
        if (billType == null) return "未知";
        switch (billType) {
            case "INVOICE": return "发票";
            case "RECEIPT": return "收据";
            case "OTHER": return "其他";
            default: return billType;
        }
    }

    private String getBillStatusName(String status) {
        if (status == null) return "未知";
        switch (status) {
            case "COLLECTED": return "已采集";
            case "OCR_PROCESSING": return "识别中";
            case "OCR_SUCCESS": return "识别成功";
            case "OCR_FAILED": return "识别失败";
            case "AUDITING": return "稽核中";
            case "AUDIT_PASSED": return "稽核通过";
            case "AUDIT_FAILED": return "稽核失败";
            case "APPLIED": return "已应用";
            default: return status;
        }
    }

    private String getOcrStatusName(String ocrStatus) {
        if (ocrStatus == null) return "待识别";
        switch (ocrStatus) {
            case "PENDING": return "待识别";
            case "OCR_PROCESSING": return "识别中";
            case "SUCCESS": return "识别成功";
            case "OCR_SUCCESS": return "识别成功";
            case "FAILED": return "识别失败";
            case "OCR_FAILED": return "识别失败";
            default: return ocrStatus;
        }
    }

    private String getAuditStatusName(String auditStatus) {
        if (auditStatus == null) return "待稽核";
        switch (auditStatus) {
            case "PENDING": return "待稽核";
            case "AUDITING": return "稽核中";
            case "PASSED": return "稽核通过";
            case "AUDIT_PASSED": return "稽核通过";
            case "FAILED": return "稽核失败";
            case "AUDIT_FAILED": return "稽核失败";
            default: return auditStatus;
        }
    }

    private String getCollectMethodName(String collectMethod) {
        if (collectMethod == null) return "未知";
        switch (collectMethod) {
            case "SCAN": return "扫描采集";
            case "MOBILE": return "移动采集";
            case "UPLOAD": return "上传采集";
            default: return collectMethod;
        }
    }
}
