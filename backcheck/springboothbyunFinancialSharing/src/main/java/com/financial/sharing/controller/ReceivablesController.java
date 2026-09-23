package com.financial.sharing.controller;

import com.financial.sharing.service.ArReceivableService;
import com.financial.sharing.service.ArCustomerService;
import com.financial.sharing.service.ArReceiptService;
import com.financial.sharing.service.ArAgingAnalysisService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.util.PageableParam;
import com.financial.sharing.vo.param.ArReceivableQueryParam;
import com.financial.sharing.vo.param.ArReceivableSaveParam;
import com.financial.sharing.vo.param.ArCustomerQueryParam;
import com.financial.sharing.vo.param.ArCustomerSaveParam;
import com.financial.sharing.vo.param.ArReceiptQueryParam;
import com.financial.sharing.vo.param.ArReceiptSaveParam;
import com.financial.sharing.vo.param.ArAgingAnalysisQueryParam;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 应收管理控制器
 * @author system
 * @date 2024-12-19
 */
@RestController
@RequestMapping("/receivables")
@Api(tags = "应收管理")
@CrossOrigin
public class ReceivablesController {

    @Resource
    private ArReceivableService arReceivableService;

    @Resource
    private ArCustomerService arCustomerService;

    @Resource
    private ArReceiptService arReceiptService;

    @Resource
    private ArAgingAnalysisService arAgingAnalysisService;

    // ==================== 应收单据管理 API ====================

    @PostMapping("/document/getList")
    @ApiOperation("分页查询应收单据列表")
    public MyJsonBean getReceivableDocumentList(PageableParam param) {
        try {
            // 构建查询参数
            ArReceivableQueryParam queryParam = new ArReceivableQueryParam();
            queryParam.setPageNumber(param.getPageNumber() != null ? param.getPageNumber() : 1);
            queryParam.setPageSize(param.getPageSize() != null ? param.getPageSize() : 20);
            queryParam.setTenantId(1L); // 默认租户ID

            // 从数据库查询
            return arReceivableService.getReceivableList(queryParam);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/receivableDocument/page")
    @ApiOperation("分页查询应收登记单据")
    public MyJsonBean getReceivableDocumentPage(@RequestBody PageableParam param) {
        try {
            // 模拟应收登记数据
            List<Map<String, Object>> documentList = Arrays.asList(
                createReceivableRegisterDocument("AR001", "AR202412190001", "客户A", 1, 500000, 0, 500000, 1, "2024-12-19", "2024-12-19 10:00:00"),
                createReceivableRegisterDocument("AR002", "AR202412190002", "客户B", 2, 800000, 200000, 600000, 2, "2024-12-18", "2024-12-18 14:30:00"),
                createReceivableRegisterDocument("AR003", "AR202412190003", "客户C", 1, 300000, 300000, 0, 2, "2024-12-17", "2024-12-17 09:15:00")
            );

            PageResult result = new PageResult();
            result.setTlist(documentList);
            result.setTotalRecord(100);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询应收登记单据失败: " + e.getMessage());
        }
    }

    @PostMapping("/receivableDocument/saveOrUpdateNew")
    @ApiOperation("保存或更新应收登记单据")
    public MyJsonBean saveOrUpdateReceivableDocumentNew(@RequestBody Map<String, Object> receivableDocument) {
        try {
            // 模拟保存逻辑
            return MyJsonBean.successData("应收单据保存成功");
        } catch (Exception e) {
            return MyJsonBean.errorData("保存应收单据失败: " + e.getMessage());
        }
    }

    @PostMapping("/receivableDocument/auditNew/{receivableId}")
    @ApiOperation("审核应收单据")
    public MyJsonBean auditReceivableDocumentNew(
            @ApiParam("应收ID") @PathVariable String receivableId,
            @RequestBody Map<String, Object> auditInfo) {
        try {
            // 模拟审核逻辑
            return MyJsonBean.successData("应收单据审核成功");
        } catch (Exception e) {
            return MyJsonBean.errorData("审核应收单据失败: " + e.getMessage());
        }
    }

    // ==================== 收款管理 API ====================

    @PostMapping("/paymentReceipt/page")
    @ApiOperation("分页查询收款单据")
    public MyJsonBean getPaymentReceiptPage(@RequestBody PageableParam param) {
        try {
            // 模拟收款单据数据
            List<Map<String, Object>> receiptList = Arrays.asList(
                createPaymentReceipt("RC001", "RC202412190001", "客户A", 300000, 300000, 0, 2, "2024-12-19", "2024-12-19 11:00:00"),
                createPaymentReceipt("RC002", "RC202412190002", "客户B", 500000, 200000, 300000, 1, "2024-12-18", "2024-12-18 15:30:00"),
                createPaymentReceipt("RC003", "RC202412190003", "客户C", 200000, 0, 200000, 0, "2024-12-17", "2024-12-17 16:45:00")
            );

            PageResult result = new PageResult();
            result.setTlist(receiptList);
            result.setTotalRecord(50);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询收款单据失败: " + e.getMessage());
        }
    }

    @PostMapping("/paymentReceipt/saveOrUpdate")
    @ApiOperation("保存或更新收款单据")
    public MyJsonBean saveOrUpdatePaymentReceiptDocument(@RequestBody Map<String, Object> paymentReceipt) {
        try {
            // 模拟保存逻辑
            return MyJsonBean.successData("收款单据保存成功");
        } catch (Exception e) {
            return MyJsonBean.errorData("保存收款单据失败: " + e.getMessage());
        }
    }

    @PostMapping("/paymentReceipt/writeOff/{receiptId}")
    @ApiOperation("核销收款")
    public MyJsonBean writeOffPayment(
            @ApiParam("收款ID") @PathVariable String receiptId,
            @RequestBody Map<String, Object> writeOffInfo) {
        try {
            // 模拟核销逻辑
            return MyJsonBean.successData("收款核销成功");
        } catch (Exception e) {
            return MyJsonBean.errorData("收款核销失败: " + e.getMessage());
        }
    }

    // ==================== 客户管理 API ====================

    @PostMapping("/customer/page")
    @ApiOperation("分页查询客户信息")
    public MyJsonBean getCustomerPage(@RequestBody PageableParam param) {
        try {
            // 模拟客户数据
            List<Map<String, Object>> customerList = Arrays.asList(
                createCustomer("C001", "CUST001", "华为技术有限公司", 1, "张经理", "13800138000", "AAA", 5000000, 2800000, 1, "2024-01-15 09:00:00"),
                createCustomer("C002", "CUST002", "腾讯科技有限公司", 1, "李总监", "13800138000", "AA", 3000000, 2350000, 1, "2024-02-20 10:30:00"),
                createCustomer("C003", "CUST003", "阿里巴巴集团", 1, "王主管", "13800138000", "AAA", 4000000, 1980000, 1, "2024-03-10 14:20:00")
            );

            PageResult result = new PageResult();
            result.setTlist(customerList);
            result.setTotalRecord(200);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询客户信息失败: " + e.getMessage());
        }
    }

    @PostMapping("/customer/saveOrUpdate")
    @ApiOperation("保存或更新客户信息")
    public MyJsonBean saveOrUpdateCustomer(@RequestBody ArCustomerSaveParam param) {
        try {
            // 设置默认租户ID
            if (param.getTenantId() == null) {
                param.setTenantId(1L);
            }
            return arCustomerService.saveOrUpdateCustomer(param);
        } catch (Exception e) {
            return MyJsonBean.errorData("保存客户信息失败: " + e.getMessage());
        }
    }

    @GetMapping("/customer/detail/{customerId}")
    @ApiOperation("获取客户详情")
    public MyJsonBean getCustomerDetail(@PathVariable @ApiParam("客户ID") String customerId) {
        try {
            return arCustomerService.getCustomerById(customerId);
        } catch (Exception e) {
            return MyJsonBean.errorData("获取客户详情失败: " + e.getMessage());
        }
    }

    @GetMapping("/customer/receivableSummary/{customerId}")
    @ApiOperation("获取客户应收汇总")
    public MyJsonBean getCustomerReceivableSummary(@ApiParam("客户ID") @PathVariable String customerId) {
        try {
            Map<String, Object> summary = new HashMap<>();
            summary.put("customerId", customerId);
            summary.put("totalReceivable", 2800000);
            summary.put("overdueAmount", 180000);
            summary.put("averageAgingDays", 35);
            summary.put("creditLimit", 5000000);
            summary.put("creditUsed", 2800000);
            summary.put("creditAvailable", 2200000);

            return MyJsonBean.successData(summary);
        } catch (Exception e) {
            return MyJsonBean.errorData("获取客户应收汇总失败: " + e.getMessage());
        }
    }

    @GetMapping("/customer/creditInfo/{customerId}")
    @ApiOperation("获取客户信用信息")
    public MyJsonBean getCustomerCreditInfo(@ApiParam("客户ID") @PathVariable String customerId) {
        try {
            Map<String, Object> creditInfo = new HashMap<>();
            creditInfo.put("customerId", customerId);
            creditInfo.put("creditLevel", "AAA");
            creditInfo.put("creditLimit", 5000000);
            creditInfo.put("creditScore", 95);
            creditInfo.put("paymentHistory", "良好");
            creditInfo.put("riskAssessment", "低风险");

            return MyJsonBean.successData(creditInfo);
        } catch (Exception e) {
            return MyJsonBean.errorData("获取客户信用信息失败: " + e.getMessage());
        }
    }

    @PostMapping("/document/saveOrUpdate")
    @ApiOperation("保存或更新应收单据")
    public MyJsonBean saveOrUpdateReceivableDocument(@RequestBody Map<String, Object> data) {
        try {
            // 构建保存参数
            ArReceivableSaveParam saveParam = new ArReceivableSaveParam();
            saveParam.setReceivableId((String) data.get("receivableId"));
            saveParam.setDocumentNo((String) data.get("documentNo"));
            saveParam.setCustomerId((String) data.get("customerId"));

            // 处理业务类型
            Object businessTypeObj = data.get("businessType");
            if (businessTypeObj != null) {
                saveParam.setBusinessType(Integer.valueOf(businessTypeObj.toString()));
            }

            // 处理应收金额
            Object amountObj = data.get("receivableAmount");
            if (amountObj != null) {
                saveParam.setReceivableAmount(new BigDecimal(amountObj.toString()));
            }

            // 处理到期日期
            Object dueDateObj = data.get("dueDate");
            if (dueDateObj != null) {
                saveParam.setDueDate(LocalDate.parse(dueDateObj.toString(), DateTimeFormatter.ofPattern("yyyy-MM-dd")));
            }

            saveParam.setCurrency((String) data.get("currency"));
            saveParam.setRemarks((String) data.get("remarks"));
            saveParam.setTenantId(1L); // 默认租户ID
            saveParam.setOperatorId("system"); // 默认操作人

            // 调用Service保存
            return arReceivableService.saveOrUpdateReceivable(saveParam);
        } catch (Exception e) {
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @GetMapping("/document/{documentId}")
    @ApiOperation("获取应收单据详情")
    public MyJsonBean getReceivableDocumentDetail(@PathVariable @ApiParam("单据ID") String documentId) {
        try {
            // 模拟应收单据详情数据
            Map<String, Object> documentData = createReceivableDocument(documentId, "华博云科技有限公司", "INVOICE", "发票", 150000.00, 120000.00, "PARTIAL_PAID", "部分收款");
            
            // 添加详细信息
            documentData.put("customerCode", "CUST001");
            documentData.put("customerContact", "张经理");
            documentData.put("customerPhone", "13800138000");
            documentData.put("contractNo", "CONTRACT2024001");
            documentData.put("invoiceNo", "INV2024001");
            documentData.put("dueDate", "2024-12-31");
            documentData.put("paymentTerms", "30天");
            documentData.put("currency", "CNY");
            documentData.put("exchangeRate", 1.0);
            
            // 添加收款记录
            documentData.put("paymentRecords", Arrays.asList(
                createPaymentRecord("PAY001", "2024-12-01", 50000.00, "银行转账", "已确认"),
                createPaymentRecord("PAY002", "2024-12-10", 70000.00, "银行转账", "已确认")
            ));
            
            return MyJsonBean.successData(documentData);
        } catch (Exception e) {
            return MyJsonBean.errorData("获取失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/document/{documentId}")
    @ApiOperation("删除应收单据")
    public MyJsonBean deleteReceivableDocument(@PathVariable @ApiParam("单据ID") String documentId) {
        try {
            // TODO: 检查单据是否可以删除
            // 检查是否有收款记录
            // 检查是否已审核
            return MyJsonBean.successData("应收单据删除成功");
        } catch (Exception e) {
            return MyJsonBean.errorData("删除失败: " + e.getMessage());
        }
    }

    @PostMapping("/document/{documentId}/audit")
    @ApiOperation("审核应收单据")
    public MyJsonBean auditReceivableDocument(@PathVariable @ApiParam("单据ID") String documentId,
                                             @RequestBody Map<String, Object> auditData) {
        try {
            String auditResult = (String) auditData.get("auditResult"); // PASS, REJECT
            String auditComment = (String) auditData.get("auditComment");
            
            // TODO: 实现应收单据审核逻辑
            // 验证单据状态
            // 更新审核状态
            // 记录审核历史
            
            return MyJsonBean.successData("应收单据审核完成");
        } catch (Exception e) {
            return MyJsonBean.errorData("审核失败: " + e.getMessage());
        }
    }

    // ==================== 收款管理 API ====================

    @PostMapping("/payment/getList")
    @ApiOperation("分页查询收款单列表")
    public MyJsonBean getPaymentReceiptList(@RequestBody ArReceiptQueryParam param) {
        try {
            // 设置默认租户ID（实际应从登录用户获取）
            if (param.getTenantId() == null) {
                param.setTenantId(1L);
            }
            // 调用Service查询数据库
            return arReceiptService.getReceiptList(param);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/payment/saveOrUpdate")
    @ApiOperation("保存或更新收款单")
    public MyJsonBean saveOrUpdatePaymentReceipt(@RequestBody ArReceiptSaveParam param) {
        try {
            // 设置默认租户ID（实际应从登录用户获取）
            if (param.getTenantId() == null) {
                param.setTenantId(1L);
            }
            // 调用Service保存到数据库
            return arReceiptService.saveOrUpdateReceipt(param);
        } catch (Exception e) {
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @PostMapping("/payment/write-off")
    @ApiOperation("收款核销")
    public MyJsonBean writeOffPayment(@RequestBody Map<String, Object> data) {
        try {
            String paymentId = (String) data.get("paymentId");
            @SuppressWarnings("unchecked")
            List<Map<String, Object>> writeOffItems = (List<Map<String, Object>>) data.get("writeOffItems");
            
            // TODO: 实现收款核销逻辑
            // 验证核销金额
            // 更新应收余额
            // 生成核销凭证
            
            Map<String, Object> result = new HashMap<>();
            result.put("paymentId", paymentId);
            result.put("writeOffAmount", 50000.00);
            result.put("writeOffCount", writeOffItems.size());
            result.put("writeOffDate", new Date());
            
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            return MyJsonBean.errorData("核销失败: " + e.getMessage());
        }
    }

    @GetMapping("/payment/write-offable")
    @ApiOperation("获取可核销应收列表")
    public MyJsonBean getWriteOffableReceivables(@RequestParam @ApiParam("客户ID") String customerId) {
        try {
            // 模拟可核销应收数据
            List<Map<String, Object>> writeOffableList = Arrays.asList(
                createWriteOffableItem("AR202412190001", "发票001", 30000.00, "2024-11-01"),
                createWriteOffableItem("AR202412190002", "合同款", 80000.00, "2024-11-15"),
                createWriteOffableItem("AR202412190003", "服务费", 25000.00, "2024-11-20")
            );
            
            return MyJsonBean.successData(writeOffableList);
        } catch (Exception e) {
            return MyJsonBean.errorData("获取失败: " + e.getMessage());
        }
    }

    // ==================== 客户管理 API ====================

    @PostMapping("/customer/getList")
    @ApiOperation("分页查询客户列表")
    public MyJsonBean getCustomerList(@RequestBody(required = false) ArCustomerQueryParam param) {
        try {
            // 构建查询参数
            ArCustomerQueryParam queryParam = param != null ? param : new ArCustomerQueryParam();
            if (queryParam.getPageNumber() == null || queryParam.getPageNumber() < 1) {
                queryParam.setPageNumber(1);
            }
            if (queryParam.getPageSize() == null || queryParam.getPageSize() < 1) {
                queryParam.setPageSize(20);
            }
            if (queryParam.getTenantId() == null) {
                queryParam.setTenantId(1L); // 默认租户ID
            }

            // 从数据库查询
            return arCustomerService.getCustomerList(queryParam);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/customer/saveOrUpdateNew")
    @ApiOperation("保存或更新客户信息")
    public MyJsonBean saveOrUpdateCustomerNew(@RequestBody Map<String, Object> data) {
        try {
            // TODO: 实现保存或更新客户逻辑
            // 验证客户信息
            // 检查客户编码唯一性
            // 保存客户档案
            return MyJsonBean.successData("客户信息保存成功");
        } catch (Exception e) {
            return MyJsonBean.errorData("保存失败: " + e.getMessage());
        }
    }

    @GetMapping("/customer/{customerId}/summaryNew")
    @ApiOperation("获取客户应收汇总")
    public MyJsonBean getCustomerReceivableSummaryNew(@PathVariable @ApiParam("客户ID") String customerId) {
        try {
            Map<String, Object> summary = new HashMap<>();
            summary.put("customerId", customerId);
            summary.put("customerName", "华博云科技有限公司");
            summary.put("totalReceivable", 500000.00);
            summary.put("paidAmount", 350000.00);
            summary.put("unpaidAmount", 150000.00);
            summary.put("overdueAmount", 30000.00);
            summary.put("creditLimit", 1000000.00);
            summary.put("availableCredit", 500000.00);
            summary.put("paymentDays", 25); // 平均回款天数
            
            // 账龄分析
            summary.put("agingAnalysis", Arrays.asList(
                createAgingItem("0-30天", 80000.00, 53.33),
                createAgingItem("31-60天", 40000.00, 26.67),
                createAgingItem("61-90天", 20000.00, 13.33),
                createAgingItem("90天以上", 10000.00, 6.67)
            ));
            
            return MyJsonBean.successData(summary);
        } catch (Exception e) {
            return MyJsonBean.errorData("获取失败: " + e.getMessage());
        }
    }

    @GetMapping("/customer/{customerId}/creditNew")
    @ApiOperation("获取客户信用信息")
    public MyJsonBean getCustomerCreditInfoNew(@PathVariable @ApiParam("客户ID") String customerId) {
        try {
            Map<String, Object> creditInfo = new HashMap<>();
            creditInfo.put("customerId", customerId);
            creditInfo.put("customerName", "华博云科技有限公司");
            creditInfo.put("creditRating", "AAA");
            creditInfo.put("creditLimit", 1000000.00);
            creditInfo.put("usedCredit", 500000.00);
            creditInfo.put("availableCredit", 500000.00);
            creditInfo.put("creditUtilization", 50.0); // 信用使用率
            creditInfo.put("paymentHistory", "良好");
            creditInfo.put("riskLevel", "低风险");
            creditInfo.put("lastCreditReview", "2024-06-01");
            creditInfo.put("nextCreditReview", "2024-12-01");
            
            return MyJsonBean.successData(creditInfo);
        } catch (Exception e) {
            return MyJsonBean.errorData("获取失败: " + e.getMessage());
        }
    }

    // ==================== 辅助方法 ====================

    private Map<String, Object> createReceivableDocument(String documentId, String customerName,
                                                         String documentType, String documentTypeName,
                                                         Double totalAmount, Double paidAmount,
                                                         String status, String statusName) {
        Map<String, Object> document = new HashMap<>();
        document.put("documentId", documentId);
        document.put("customerName", customerName);
        document.put("documentType", documentType);
        document.put("documentTypeName", documentTypeName);
        document.put("totalAmount", totalAmount);
        document.put("paidAmount", paidAmount);
        document.put("unpaidAmount", totalAmount - paidAmount);
        document.put("status", status);
        document.put("statusName", statusName);
        document.put("documentDate", new Date());
        document.put("dueDate", new Date(System.currentTimeMillis() + 30L * 24 * 60 * 60 * 1000)); // 30天后
        document.put("creator", "张三");
        document.put("createTime", new Date());
        return document;
    }

    private Map<String, Object> createPaymentRecord(String paymentId, String paymentDate, 
                                                   Double amount, String paymentMethod, String status) {
        Map<String, Object> record = new HashMap<>();
        record.put("paymentId", paymentId);
        record.put("paymentDate", paymentDate);
        record.put("amount", amount);
        record.put("paymentMethod", paymentMethod);
        record.put("status", status);
        return record;
    }

    private Map<String, Object> createPaymentReceipt(String paymentId, String customerName, 
                                                     Double amount, String paymentMethod, 
                                                     String paymentDate, String status, String statusName) {
        Map<String, Object> receipt = new HashMap<>();
        receipt.put("paymentId", paymentId);
        receipt.put("customerName", customerName);
        receipt.put("amount", amount);
        receipt.put("paymentMethod", paymentMethod);
        receipt.put("paymentDate", paymentDate);
        receipt.put("status", status);
        receipt.put("statusName", statusName);
        receipt.put("operator", "李四");
        receipt.put("createTime", new Date());
        return receipt;
    }

    private Map<String, Object> createWriteOffableItem(String documentId, String documentName, 
                                                       Double unpaidAmount, String dueDate) {
        Map<String, Object> item = new HashMap<>();
        item.put("documentId", documentId);
        item.put("documentName", documentName);
        item.put("unpaidAmount", unpaidAmount);
        item.put("dueDate", dueDate);
        return item;
    }

    private Map<String, Object> createCustomer(String customerId, String customerName, 
                                              String creditRating, Double creditLimit, 
                                              Double totalReceivable, Double availableCredit) {
        Map<String, Object> customer = new HashMap<>();
        customer.put("customerId", customerId);
        customer.put("customerName", customerName);
        customer.put("creditRating", creditRating);
        customer.put("creditLimit", creditLimit);
        customer.put("totalReceivable", totalReceivable);
        customer.put("availableCredit", availableCredit);
        customer.put("paymentTerms", "30天");
        customer.put("status", "ACTIVE");
        customer.put("statusName", "正常");
        customer.put("createTime", new Date());
        return customer;
    }

    private Map<String, Object> createAgingItem(String agingRange, Double amount, Double percentage) {
        Map<String, Object> item = new HashMap<>();
        item.put("agingRange", agingRange);
        item.put("amount", amount);
        item.put("percentage", percentage);
        return item;
    }

    // ==================== 坏账管理 API ====================

    @PostMapping("/bad-debt/getList")
    @ApiOperation("分页查询坏账列表")
    public MyJsonBean getBadDebtList(@RequestBody PageableParam param) {
        try {
            // 模拟坏账数据
            List<Map<String, Object>> badDebtList = Arrays.asList(
                createBadDebt("BD202412190001", "某倒闭公司", 50000.00, 50000.00, "FULL_PROVISION", "全额计提"),
                createBadDebt("BD202412190002", "某困难企业", 80000.00, 40000.00, "PARTIAL_PROVISION", "部分计提"),
                createBadDebt("BD202412190003", "某失联客户", 30000.00, 30000.00, "WRITTEN_OFF", "已核销"),
                createBadDebt("BD202412190004", "某纠纷客户", 60000.00, 30000.00, "PARTIAL_PROVISION", "部分计提")
            );

            PageResult result = new PageResult();
            result.setTlist(badDebtList);
            result.setTotalRecord(badDebtList.size());
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/bad-debt/provide")
    @ApiOperation("计提坏账准备")
    public MyJsonBean provideBadDebt(@RequestBody Map<String, Object> data) {
        try {
            String documentId = (String) data.get("documentId");
            Double provisionAmount = (Double) data.get("provisionAmount");
            String provisionReason = (String) data.get("provisionReason");

            // TODO: 实现坏账计提逻辑
            // 验证计提金额
            // 生成坏账准备凭证
            // 更新应收状态

            Map<String, Object> result = new HashMap<>();
            result.put("documentId", documentId);
            result.put("provisionAmount", provisionAmount);
            result.put("provisionDate", new Date());
            result.put("status", "PROVIDED");

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            return MyJsonBean.errorData("计提失败: " + e.getMessage());
        }
    }

    @PostMapping("/bad-debt/write-off")
    @ApiOperation("坏账核销")
    public MyJsonBean writeOffBadDebt(@RequestBody Map<String, Object> data) {
        try {
            String documentId = (String) data.get("documentId");
            Double writeOffAmount = (Double) data.get("writeOffAmount");
            String writeOffReason = (String) data.get("writeOffReason");

            // TODO: 实现坏账核销逻辑
            // 验证核销金额
            // 生成核销凭证
            // 更新应收状态

            Map<String, Object> result = new HashMap<>();
            result.put("documentId", documentId);
            result.put("writeOffAmount", writeOffAmount);
            result.put("writeOffDate", new Date());
            result.put("status", "WRITTEN_OFF");

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            return MyJsonBean.errorData("核销失败: " + e.getMessage());
        }
    }

    // ==================== 账龄分析 API ====================

    @GetMapping("/aging/analysis")
    @ApiOperation("获取应收账龄分析")
    public MyJsonBean getReceivableAgingAnalysis(
            @RequestParam @ApiParam("分析日期") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate analysisDate,
            @RequestParam(required = false, defaultValue = "1") @ApiParam("租户ID") Long tenantId) {
        return arAgingAnalysisService.getAgingRangeSummary(analysisDate, tenantId);
    }

    @PostMapping("/aging/details")
    @ApiOperation("查询账龄分析明细")
    public MyJsonBean<PageResult> getAgingDetails(ArAgingAnalysisQueryParam param) {
        return arAgingAnalysisService.getAgingDetails(param);
    }

    @GetMapping("/aging/overdueStatistics")
    @ApiOperation("获取逾期统计")
    public MyJsonBean getOverdueStatistics(
            @RequestParam @ApiParam("分析日期") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate analysisDate,
            @RequestParam(required = false, defaultValue = "1") @ApiParam("租户ID") Long tenantId) {
        return arAgingAnalysisService.getTotalOverdueAmount(analysisDate, tenantId);
    }

    @GetMapping("/aging/riskAssessment")
    @ApiOperation("获取风险评估")
    public MyJsonBean getRiskAssessment(
            @RequestParam @ApiParam("分析日期") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate analysisDate,
            @RequestParam(required = false, defaultValue = "1") @ApiParam("租户ID") Long tenantId) {
        return arAgingAnalysisService.getAgingByRiskLevel(analysisDate, tenantId);
    }

    @GetMapping("/aging/byCustomer")
    @ApiOperation("按客户统计账龄")
    public MyJsonBean getAgingByCustomer(
            @RequestParam @ApiParam("分析日期") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate analysisDate,
            @RequestParam(required = false, defaultValue = "1") @ApiParam("租户ID") Long tenantId) {
        return arAgingAnalysisService.getAgingByCustomer(analysisDate, tenantId);
    }

    @GetMapping("/aging/trend")
    @ApiOperation("查询账龄趋势")
    public MyJsonBean getAgingTrend(
            @RequestParam @ApiParam("开始日期") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @ApiParam("结束日期") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam(required = false, defaultValue = "1") @ApiParam("租户ID") Long tenantId) {
        return arAgingAnalysisService.getAgingTrend(startDate, endDate, tenantId);
    }

    @PostMapping("/aging/generateSnapshot")
    @ApiOperation("生成账龄快照")
    public MyJsonBean generateAgingSnapshot(@RequestBody Map<String, Object> params) {
        String dateStr = (String) params.get("analysisDate");
        LocalDate analysisDate = LocalDate.parse(dateStr);
        Long tenantId = params.get("tenantId") != null ? Long.valueOf(params.get("tenantId").toString()) : 1L;
        return arAgingAnalysisService.generateAgingSnapshot(analysisDate, tenantId);
    }

    @GetMapping("/aging/latestSnapshotDate")
    @ApiOperation("查询最新快照日期")
    public MyJsonBean getLatestSnapshotDate(
            @RequestParam(required = false, defaultValue = "1") @ApiParam("租户ID") Long tenantId) {
        return arAgingAnalysisService.getLatestSnapshotDate(tenantId);
    }

    @PostMapping("/aging/export")
    @ApiOperation("导出账龄分析报表")
    public MyJsonBean exportAgingReport(@RequestBody ArAgingAnalysisQueryParam param) {
        return arAgingAnalysisService.exportAgingReport(param);
    }

    // ==================== 应收分析 API ====================

    @PostMapping("/analysis/structure")
    @ApiOperation("获取应收结构分析")
    public MyJsonBean getReceivablesStructureAnalysis(@RequestBody Map<String, Object> param) {
        try {
            // 模拟结构分析数据
            Map<String, Object> analysis = new HashMap<>();
            analysis.put("top5CustomerRatio", 68.5);
            analysis.put("concentrationIndex", 0.72);
            analysis.put("within30DaysRatio", 45.8);
            analysis.put("overdueRatio", 23.6);
            analysis.put("salesRatio", 72.3);
            analysis.put("serviceRatio", 18.9);

            return MyJsonBean.successData(analysis);
        } catch (Exception e) {
            return MyJsonBean.errorData("获取结构分析失败: " + e.getMessage());
        }
    }

    @PostMapping("/analysis/customerRanking")
    @ApiOperation("获取客户排名")
    public MyJsonBean getCustomerRanking(@RequestBody Map<String, Object> param) {
        try {
            // 模拟客户排名数据
            List<Map<String, Object>> ranking = Arrays.asList(
                createCustomerRanking("华为技术有限公司", 2800000, 0, 25, 15, 95.8, "AAA", "2024-12-15"),
                createCustomerRanking("腾讯科技有限公司", 2350000, 180000, 38, 25, 92.3, "AA", "2024-12-10"),
                createCustomerRanking("阿里巴巴集团", 1980000, 0, 22, 12, 97.1, "AAA", "2024-12-18")
            );

            return MyJsonBean.successData(ranking);
        } catch (Exception e) {
            return MyJsonBean.errorData("获取客户排名失败: " + e.getMessage());
        }
    }

    @PostMapping("/analysis/collectionEfficiency")
    @ApiOperation("获取收款效率分析")
    public MyJsonBean getCollectionEfficiencyAnalysis(@RequestBody Map<String, Object> param) {
        try {
            // 模拟效率分析数据
            Map<String, Object> efficiency = new HashMap<>();
            efficiency.put("averageCollectionCycle", 42);
            efficiency.put("receivableTurnover", 8.6);
            efficiency.put("badDebtRate", 2.8);
            efficiency.put("collectionRate", 85.6);

            return MyJsonBean.successData(efficiency);
        } catch (Exception e) {
            return MyJsonBean.errorData("获取收款效率分析失败: " + e.getMessage());
        }
    }

    @PostMapping("/analysis/forecast")
    @ApiOperation("获取应收预测")
    public MyJsonBean getReceivablesForecast(@RequestBody Map<String, Object> param) {
        try {
            // 模拟预测数据
            Map<String, Object> forecast = new HashMap<>();
            forecast.put("nextMonthForecast", 16800000);
            forecast.put("nextQuarterForecast", 48500000);
            forecast.put("riskWarning", "客户B存在逾期风险");

            return MyJsonBean.successData(forecast);
        } catch (Exception e) {
            return MyJsonBean.errorData("获取应收预测失败: " + e.getMessage());
        }
    }

    // ==================== 统计分析 API ====================

    @GetMapping("/statistics")
    @ApiOperation("获取应收统计概览")
    public MyJsonBean getReceivableStatistics() {
        try {
            Map<String, Object> statistics = new HashMap<>();
            statistics.put("totalReceivable", 1025000.00);
            statistics.put("paidAmount", 650000.00);
            statistics.put("unpaidAmount", 375000.00);
            statistics.put("overdueAmount", 115000.00);
            statistics.put("badDebtAmount", 50000.00);
            statistics.put("collectionRate", 63.41); // 回款率
            statistics.put("overdueRate", 11.22); // 逾期率
            statistics.put("badDebtRate", 4.88); // 坏账率
            statistics.put("averageCollectionDays", 32); // 平均回款天数

            // 月度趋势
            statistics.put("monthlyTrend", Arrays.asList(
                createMonthlyTrend("2024-07", 850000.00, 520000.00, 330000.00),
                createMonthlyTrend("2024-08", 920000.00, 580000.00, 340000.00),
                createMonthlyTrend("2024-09", 980000.00, 620000.00, 360000.00),
                createMonthlyTrend("2024-10", 1050000.00, 680000.00, 370000.00),
                createMonthlyTrend("2024-11", 1100000.00, 720000.00, 380000.00),
                createMonthlyTrend("2024-12", 1025000.00, 650000.00, 375000.00)
            ));

            return MyJsonBean.successData(statistics);
        } catch (Exception e) {
            return MyJsonBean.errorData("获取失败: " + e.getMessage());
        }
    }

    @GetMapping("/statistics/customer-ranking")
    @ApiOperation("获取客户应收排名")
    public MyJsonBean getCustomerReceivableRanking(@RequestParam(defaultValue = "10") @ApiParam("排名数量") Integer topN) {
        try {
            // 模拟客户排名数据
            List<Map<String, Object>> ranking = Arrays.asList(
                createCustomerRanking(1, "深圳科技集团", 320000.00, 31.22),
                createCustomerRanking(2, "北京软件公司", 280000.00, 27.32),
                createCustomerRanking(3, "广州数据公司", 180000.00, 17.56),
                createCustomerRanking(4, "华博云科技有限公司", 150000.00, 14.63),
                createCustomerRanking(5, "上海信息技术公司", 95000.00, 9.27)
            );

            return MyJsonBean.successData(ranking);
        } catch (Exception e) {
            return MyJsonBean.errorData("获取失败: " + e.getMessage());
        }
    }

    // ==================== 新增辅助方法 ====================

    private Map<String, Object> createAgingDistribution(String agingRange, Double amount,
                                                        Double percentage, String riskLevel) {
        Map<String, Object> distribution = new HashMap<>();
        distribution.put("agingRange", agingRange);
        distribution.put("amount", amount);
        distribution.put("percentage", percentage);
        distribution.put("riskLevel", riskLevel);
        return distribution;
    }

    private Map<String, Object> createCustomerAging(String customerName, Double amount,
                                                    Integer avgDays, String riskLevel) {
        Map<String, Object> aging = new HashMap<>();
        aging.put("customerName", customerName);
        aging.put("amount", amount);
        aging.put("avgDays", avgDays);
        aging.put("riskLevel", riskLevel);
        return aging;
    }

    private Map<String, Object> createAgingDetail(String documentId, String customerName,
                                                 String documentName, Double amount,
                                                 Integer agingDays, String riskLevel) {
        Map<String, Object> detail = new HashMap<>();
        detail.put("documentId", documentId);
        detail.put("customerName", customerName);
        detail.put("documentName", documentName);
        detail.put("amount", amount);
        detail.put("agingDays", agingDays);
        detail.put("riskLevel", riskLevel);
        detail.put("dueDate", new Date(System.currentTimeMillis() - agingDays * 24L * 60 * 60 * 1000));
        return detail;
    }

    private Map<String, Object> createBadDebt(String badDebtId, String customerName,
                                              Double originalAmount, Double provisionAmount,
                                              String status, String statusName) {
        Map<String, Object> badDebt = new HashMap<>();
        badDebt.put("badDebtId", badDebtId);
        badDebt.put("customerName", customerName);
        badDebt.put("originalAmount", originalAmount);
        badDebt.put("provisionAmount", provisionAmount);
        badDebt.put("status", status);
        badDebt.put("statusName", statusName);
        badDebt.put("provisionDate", new Date());
        badDebt.put("provisionReason", "长期逾期");
        return badDebt;
    }

    private Map<String, Object> createMonthlyTrend(String month, Double totalReceivable,
                                                  Double paidAmount, Double unpaidAmount) {
        Map<String, Object> trend = new HashMap<>();
        trend.put("month", month);
        trend.put("totalReceivable", totalReceivable);
        trend.put("paidAmount", paidAmount);
        trend.put("unpaidAmount", unpaidAmount);
        trend.put("collectionRate", (paidAmount / totalReceivable) * 100);
        return trend;
    }

    private Map<String, Object> createCustomerRanking(Integer rank, String customerName,
                                                      Double amount, Double percentage) {
        Map<String, Object> ranking = new HashMap<>();
        ranking.put("rank", rank);
        ranking.put("customerName", customerName);
        ranking.put("amount", amount);
        ranking.put("percentage", percentage);
        return ranking;
    }

    // ==================== 新增辅助方法 ====================

    private Map<String, Object> createReceivableRegisterDocument(String receivableId, String documentNo, String customerName,
            int businessType, long receivableAmount, long receivedAmount, long remainingAmount, int documentStatus,
            String dueDate, String createTime) {
        Map<String, Object> doc = new HashMap<>();
        doc.put("receivableId", receivableId);
        doc.put("documentNo", documentNo);
        doc.put("customerName", customerName);
        doc.put("businessTypeName", businessType == 1 ? "销售收入" : "服务收入");
        doc.put("receivableAmount", receivableAmount);
        doc.put("receivedAmount", receivedAmount);
        doc.put("remainingAmount", remainingAmount);
        doc.put("documentStatus", documentStatus);
        doc.put("dueDate", dueDate);
        doc.put("createTime", createTime);
        return doc;
    }

    private Map<String, Object> createPaymentReceipt(String receiptId, String receiptNo, String customerName,
            long receiptAmount, long writeOffAmount, long remainingAmount, int receiptStatus,
            String receiptDate, String createTime) {
        Map<String, Object> receipt = new HashMap<>();
        receipt.put("receiptId", receiptId);
        receipt.put("receiptNo", receiptNo);
        receipt.put("customerName", customerName);
        receipt.put("receiptAmount", receiptAmount);
        receipt.put("writeOffAmount", writeOffAmount);
        receipt.put("remainingAmount", remainingAmount);
        receipt.put("receiptStatus", receiptStatus);
        receipt.put("receiptDate", receiptDate);
        receipt.put("createTime", createTime);
        return receipt;
    }

    private Map<String, Object> createCustomer(String customerId, String customerCode, String customerName,
            int customerCategory, String contactPerson, String contactPhone, String creditLevel,
            long creditLimit, long receivableAmount, int customerStatus, String createTime) {
        Map<String, Object> customer = new HashMap<>();
        customer.put("customerId", customerId);
        customer.put("customerCode", customerCode);
        customer.put("customerName", customerName);
        customer.put("customerCategoryName", customerCategory == 1 ? "重要客户" : "一般客户");
        customer.put("contactPerson", contactPerson);
        customer.put("contactPhone", contactPhone);
        customer.put("creditLevel", creditLevel);
        customer.put("creditLimit", creditLimit);
        customer.put("receivableAmount", receivableAmount);
        customer.put("customerStatus", customerStatus);
        customer.put("createTime", createTime);
        return customer;
    }

    private Map<String, Object> createOverdueStats(long amount, int count, double percentage) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("amount", amount);
        stats.put("count", count);
        stats.put("percentage", percentage);
        return stats;
    }

    private Map<String, Object> createRiskStats(long amount, int count) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("amount", amount);
        stats.put("count", count);
        return stats;
    }

    private Map<String, Object> createBadDebtProvision(String provisionId, String customerName,
            long receivableAmount, int provisionRate, long provisionAmount, String provisionMethodName,
            String provisionDate, int provisionStatus, String createTime) {
        Map<String, Object> provision = new HashMap<>();
        provision.put("provisionId", provisionId);
        provision.put("customerName", customerName);
        provision.put("receivableAmount", receivableAmount);
        provision.put("provisionRate", provisionRate);
        provision.put("provisionAmount", provisionAmount);
        provision.put("provisionMethodName", provisionMethodName);
        provision.put("provisionDate", provisionDate);
        provision.put("provisionStatus", provisionStatus);
        provision.put("createTime", createTime);
        return provision;
    }

    private Map<String, Object> createBadDebtWriteOff(String writeOffId, String writeOffNo, String customerName,
            String receivableNo, long writeOffAmount, String writeOffReason, int writeOffStatus,
            String writeOffDate, String createTime) {
        Map<String, Object> writeOff = new HashMap<>();
        writeOff.put("writeOffId", writeOffId);
        writeOff.put("writeOffNo", writeOffNo);
        writeOff.put("customerName", customerName);
        writeOff.put("receivableNo", receivableNo);
        writeOff.put("writeOffAmount", writeOffAmount);
        writeOff.put("writeOffReason", writeOffReason);
        writeOff.put("writeOffStatus", writeOffStatus);
        writeOff.put("writeOffDate", writeOffDate);
        writeOff.put("createTime", createTime);
        return writeOff;
    }

    private Map<String, Object> createBadDebtRecovery(String recoveryId, String recoveryNo, String customerName,
            long originalWriteOffAmount, long recoveryAmount, int recoveryRate, String recoveryMethodName,
            String recoveryDate, String createTime) {
        Map<String, Object> recovery = new HashMap<>();
        recovery.put("recoveryId", recoveryId);
        recovery.put("recoveryNo", recoveryNo);
        recovery.put("customerName", customerName);
        recovery.put("originalWriteOffAmount", originalWriteOffAmount);
        recovery.put("recoveryAmount", recoveryAmount);
        recovery.put("recoveryRate", recoveryRate);
        recovery.put("recoveryMethodName", recoveryMethodName);
        recovery.put("recoveryDate", recoveryDate);
        recovery.put("createTime", createTime);
        return recovery;
    }

    private Map<String, Object> createCustomerRanking(String customerName, long receivableBalance,
            long overdueAmount, int averageAgingDays, int riskScore, double collectionRate,
            String creditLevel, String lastPaymentDate) {
        Map<String, Object> ranking = new HashMap<>();
        ranking.put("customerName", customerName);
        ranking.put("receivableBalance", receivableBalance);
        ranking.put("overdueAmount", overdueAmount);
        ranking.put("averageAgingDays", averageAgingDays);
        ranking.put("riskScore", riskScore);
        ranking.put("collectionRate", collectionRate);
        ranking.put("creditLevel", creditLevel);
        ranking.put("lastPaymentDate", lastPaymentDate);
        return ranking;
    }
}
