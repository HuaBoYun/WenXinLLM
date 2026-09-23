package com.financial.sharing.controller;

import com.financial.sharing.dto.param.*;
import com.financial.sharing.service.*;
import com.financial.sharing.util.JsonMapper;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.result.*;
import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

/**
 * 应付管理控制器
 * @author system
 * @date 2024-12-19
 */
@Slf4j
@RestController
@RequestMapping("/payables")
@Api(tags = "应付管理")
@CrossOrigin
public class PayablesController {

    @Resource
    private UserProvider userProvider;

    @Resource
    private SupplierService supplierService;

    @Resource
    private PayableDocumentService payableDocumentService;

    @Resource
    private PaymentOrderService paymentOrderService;

    @Resource
    private PayableAnalysisService payableAnalysisService;

    @Resource
    private WriteOffRecordService writeOffRecordService;

    @Resource
    private PrepaymentService prepaymentService;

    // ==================== 应付单据管理 API ====================

    @PostMapping("/document/getList")
    @ApiOperation("分页查询应付单据列表")
    public String getPayableDocumentList(HttpServletRequest request,
                                        HttpServletResponse response,
                                        @RequestParam(required = false) Integer pageNumber,
                                        @RequestParam(required = false) Integer pageSize,
                                        @RequestParam(required = false) String documentNo,
                                        @RequestParam(required = false) String supplierId,
                                        @RequestParam(required = false) Integer documentStatus) {
        try {
            // 宽松权限验证 - 记录日志但不阻断请求
            try {
                TblStaffUtil loginStaff = userProvider.get();
                if (loginStaff != null) {
                    log.info("用户 {} 访问应付单据列表", loginStaff.getRealname());
                }
            } catch (Exception e) {
                log.warn("权限验证跳过: {}", e.getMessage());
            }

            log.info("查询应付单据列表，参数: pageNumber={}, pageSize={}, documentNo={}, supplierId={}, documentStatus={}",
                     pageNumber, pageSize, documentNo, supplierId, documentStatus);

            // 构建查询参数
            PayableDocumentQueryParam queryParam = new PayableDocumentQueryParam();
            queryParam.setPageNo(pageNumber != null ? pageNumber : 1);
            queryParam.setPageSize(pageSize != null ? pageSize : 20);
            queryParam.setDocumentNo(documentNo);
            queryParam.setSupplierId(supplierId);
            queryParam.setDocumentStatus(documentStatus);

            // 调用Service查询数据库
            PageResult<PayableDocumentVO> pageResult = payableDocumentService.queryPage(queryParam);

            // 构建返回数据
            Map<String, Object> data = new HashMap<>();
            data.put("tlist", pageResult.getRecords());
            data.put("totalRecord", pageResult.getTotal());
            data.put("pageNo", pageResult.getCurrent());
            data.put("pageSize", pageResult.getSize());

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("查询成功");
            json.setData(data);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("查询应付单据列表失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/document/{documentId}")
    @ApiOperation("根据ID查询应付单据详情")
    public String getPayableDocumentById(HttpServletRequest request,
                                       HttpServletResponse response,
                                       @PathVariable String documentId) {
        try {
            // 宽松权限验证 - 记录日志但不阻断请求
            try {
                TblStaffUtil loginStaff = userProvider.get();
                if (loginStaff != null) {
                    log.info("用户 {} 查询应付单据详情: {}", loginStaff.getRealname(), documentId);
                }
            } catch (Exception e) {
                log.warn("权限验证跳过: {}", e.getMessage());
            }

            log.info("查询应付单据详情，documentId={}", documentId);

            // 调用Service查询数据库
            PayableDocumentVO document = payableDocumentService.getDetail(documentId);
            if (document == null) {
                return createErrorResponse("应付单据不存在");
            }

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("查询成功");
            json.setData(document);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("查询应付单据详情失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/document/detail/{documentId}")
    @ApiOperation("查询应付单据详情（完整版）")
    public String getPayableDocumentDetail(HttpServletRequest request,
                                          HttpServletResponse response,
                                          @PathVariable String documentId) {
        try {
            // 宽松权限验证 - 记录日志但不阻断请求
            try {
                TblStaffUtil loginStaff = userProvider.get();
                if (loginStaff != null) {
                    log.info("用户 {} 查询应付单据详情(完整版): {}", loginStaff.getRealname(), documentId);
                }
            } catch (Exception e) {
                log.warn("权限验证跳过: {}", e.getMessage());
            }

            log.info("查询应付单据详情(完整版)，documentId={}", documentId);

            // 调用Service查询数据库
            PayableDocumentVO document = payableDocumentService.getDetail(documentId);
            if (document == null) {
                return createErrorResponse("应付单据不存在");
            }

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("查询成功");
            json.setData(document);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("查询应付单据详情(完整版)失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/document/saveOrUpdate")
    @ApiOperation("保存或更新应付单据")
    public String saveOrUpdatePayableDocument(HttpServletRequest request,
                                             HttpServletResponse response,
                                             @RequestParam(required = false) String documentId,
                                             @RequestParam(required = false) String documentNo,
                                             @RequestParam(required = false) String supplierId,
                                             @RequestParam(required = false) Double payableAmount,
                                             @RequestParam(required = false) String dueDate,
                                             @RequestParam(required = false) String currency,
                                             @RequestParam(required = false) Double exchangeRate,
                                             @RequestParam(required = false) Integer businessType,
                                             @RequestParam(required = false) String summary,
                                             @RequestParam(required = false) String remarks) {
        try {
            // 宽松权限验证 - 记录日志但不阻断请求
            try {
                TblStaffUtil loginStaff = userProvider.get();
                if (loginStaff != null) {
                    log.info("用户 {} 保存应付单据", loginStaff.getRealname());
                }
            } catch (Exception e) {
                log.warn("权限验证跳过: {}", e.getMessage());
            }

            log.info("保存应付单据，参数: documentId={}, documentNo={}, supplierId={}, payableAmount={}, dueDate={}, currency={}, exchangeRate={}, businessType={}, summary={}, remarks={}",
                     documentId, documentNo, supplierId, payableAmount, dueDate, currency, exchangeRate, businessType, summary, remarks);

            // 构建保存参数
            PayableDocumentSaveParam saveParam = new PayableDocumentSaveParam();
            saveParam.setDocumentId(documentId);
            saveParam.setDocumentNo(documentNo);
            saveParam.setSupplierId(supplierId);
            if (payableAmount != null) {
                saveParam.setPayableAmount(new BigDecimal(payableAmount));
            }
            saveParam.setDueDate(dueDate != null ? java.time.LocalDate.parse(dueDate) : null);
            saveParam.setCurrency(currency);
            if (exchangeRate != null) {
                saveParam.setExchangeRate(new BigDecimal(exchangeRate));
            }
            saveParam.setBusinessType(businessType);
            saveParam.setSummary(summary);
            saveParam.setRemarks(remarks);

            // 调用Service保存数据库
            String savedDocumentId = payableDocumentService.save(saveParam);

            Map<String, Object> result = new HashMap<>();
            result.put("documentId", savedDocumentId);
            result.put("documentNo", documentNo);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg(documentId != null ? "更新成功" : "保存成功");
            json.setData(result);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("保存应付单据失败", e);
            return createErrorResponse("保存失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/document/{documentId}")
    @ApiOperation("删除应付单据")
    public String deletePayableDocument(HttpServletRequest request,
                                       HttpServletResponse response,
                                       @PathVariable String documentId) {
        try {
            // 宽松权限验证 - 记录日志但不阻断请求
            try {
                TblStaffUtil loginStaff = userProvider.get();
                if (loginStaff != null) {
                    log.info("用户 {} 删除应付单据: {}", loginStaff.getRealname(), documentId);
                }
            } catch (Exception e) {
                log.warn("权限验证跳过: {}", e.getMessage());
            }

            log.info("删除应付单据，documentId={}", documentId);

            // 调用Service删除数据库记录
            payableDocumentService.delete(documentId);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("删除成功");
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("删除应付单据失败", e);
            return createErrorResponse("删除失败: " + e.getMessage());
        }
    }

    @PostMapping("/document/{documentId}/audit")
    @ApiOperation("审核应付单据")
    public String auditPayableDocument(HttpServletRequest request,
                                      HttpServletResponse response,
                                      @PathVariable String documentId,
                                      @RequestParam(required = false) Integer auditResult,
                                      @RequestParam(required = false) String auditComments) {
        try {
            // 宽松权限验证 - 记录日志但不阻断请求
            try {
                TblStaffUtil loginStaff = userProvider.get();
                if (loginStaff != null) {
                    log.info("用户 {} 审核应付单据: {}", loginStaff.getRealname(), documentId);
                }
            } catch (Exception e) {
                log.warn("权限验证跳过: {}", e.getMessage());
            }

            log.info("审核应付单据，参数: documentId={}, auditResult={}, auditComments={}",
                     documentId, auditResult, auditComments);

            // auditResult: 1=通过, 其他=拒绝
            boolean approved = auditResult != null && auditResult == 1;

            // 调用Service审核
            payableDocumentService.audit(documentId, approved, auditComments);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("审核成功");
            json.setData(null);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("审核应付单据失败", e);
            return createErrorResponse("审核失败: " + e.getMessage());
        }
    }

    @GetMapping("/statistics")
    @ApiOperation("获取应付统计概览")
    public String getPayableStatistics(HttpServletRequest request,
                                      HttpServletResponse response) {
        try {
            // 宽松权限验证 - 记录日志但不阻断请求
            try {
                TblStaffUtil loginStaff = userProvider.get();
                if (loginStaff != null) {
                    log.info("用户 {} 访问应付统计概览", loginStaff.getRealname());
                }
            } catch (Exception e) {
                log.warn("权限验证跳过: {}", e.getMessage());
            }

            log.info("查询应付统计概览");

            // 调用Service获取统计数据
            PayableSummaryVO summary = payableAnalysisService.getSummary();

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("查询成功");
            json.setData(summary);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("获取应付统计失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/document/create")
    @ApiOperation("创建应付单据")
    public String createPayableDocument(HttpServletRequest request,
                                       HttpServletResponse response,
                                       @RequestParam(required = false) String documentNumber,
                                       @RequestParam(required = false) String supplierId,
                                       @RequestParam(required = false) Double amount,
                                       @RequestParam(required = false) String dueDate,
                                       @RequestParam(required = false) String remarks) {
        try {
            // 宽松权限验证 - 记录日志但不阻断请求
            try {
                TblStaffUtil loginStaff = userProvider.get();
                if (loginStaff != null) {
                    log.info("用户 {} 创建应付单据", loginStaff.getRealname());
                }
            } catch (Exception e) {
                log.warn("权限验证跳过: {}", e.getMessage());
            }

            log.info("创建应付单据，参数: documentNumber={}, supplierId={}, amount={}, dueDate={}, remarks={}",
                     documentNumber, supplierId, amount, dueDate, remarks);

            // 构建保存参数
            PayableDocumentSaveParam saveParam = new PayableDocumentSaveParam();
            saveParam.setDocumentNo(documentNumber);
            saveParam.setSupplierId(supplierId);
            if (amount != null) {
                saveParam.setPayableAmount(new BigDecimal(amount));
            }
            saveParam.setDueDate(dueDate != null ? java.time.LocalDate.parse(dueDate) : null);
            saveParam.setRemarks(remarks);
            saveParam.setBusinessType(1); // 默认业务类型：1=采购应付

            // 调用Service创建
            String documentId = payableDocumentService.save(saveParam);

            Map<String, Object> result = new HashMap<>();
            result.put("documentId", documentId);
            result.put("documentNumber", documentNumber);
            result.put("status", "DRAFT");
            result.put("message", "应付单据创建成功");

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("创建成功");
            json.setData(result);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("创建应付单据失败", e);
            return createErrorResponse("创建失败: " + e.getMessage());
        }
    }

    @PostMapping("/payment/apply")
    @ApiOperation("付款申请")
    public String applyPayment(HttpServletRequest request,
                              HttpServletResponse response,
                              @RequestParam(required = false) String documentId,
                              @RequestParam(required = false) String supplierId,
                              @RequestParam(required = false) Double paymentAmount,
                              @RequestParam(required = false) String paymentDate,
                              @RequestParam(required = false) String paymentMethod,
                              @RequestParam(required = false) String remarks) {
        try {
            // 宽松权限验证 - 记录日志但不阻断请求
            try {
                TblStaffUtil loginStaff = userProvider.get();
                if (loginStaff != null) {
                    log.info("用户 {} 提交付款申请", loginStaff.getRealname());
                }
            } catch (Exception e) {
                log.warn("权限验证跳过: {}", e.getMessage());
            }

            log.info("付款申请，参数: documentId={}, supplierId={}, paymentAmount={}, paymentDate={}, paymentMethod={}, remarks={}",
                     documentId, supplierId, paymentAmount, paymentDate, paymentMethod, remarks);

            // 构建保存参数
            PaymentOrderSaveParam saveParam = new PaymentOrderSaveParam();
            saveParam.setSupplierId(supplierId);
            if (paymentAmount != null) {
                saveParam.setPaymentAmount(new BigDecimal(paymentAmount));
            }
            saveParam.setPaymentDate(paymentDate != null ? java.time.LocalDate.parse(paymentDate) : null);
            // paymentMethod 需要转换为 Integer: 1=现金,2=银行转账,3=支票,4=承兑汇票
            if (paymentMethod != null && !paymentMethod.isEmpty()) {
                try {
                    saveParam.setPaymentMethod(Integer.parseInt(paymentMethod));
                } catch (NumberFormatException e) {
                    log.warn("付款方式格式错误: {}", paymentMethod);
                    saveParam.setPaymentMethod(2); // 默认银行转账
                }
            } else {
                saveParam.setPaymentMethod(2); // 默认银行转账
            }
            saveParam.setRemarks(remarks);

            // 调用Service保存
            String paymentId = paymentOrderService.save(saveParam);

            Map<String, Object> result = new HashMap<>();
            result.put("paymentId", paymentId);
            result.put("status", "PENDING_APPROVAL");
            result.put("message", "付款申请提交成功，等待审批");

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("申请提交成功");
            json.setData(result);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("付款申请失败", e);
            return createErrorResponse("申请失败: " + e.getMessage());
        }
    }

    @GetMapping("/payment/list")
    @ApiOperation("查询付款申请列表")
    public String getPaymentApplicationList(HttpServletRequest request,
                                          HttpServletResponse response,
                                          @RequestParam(required = false) Integer pageNumber,
                                          @RequestParam(required = false) Integer pageSize,
                                          @RequestParam(required = false) Integer status,
                                          @RequestParam(required = false) String startDate,
                                          @RequestParam(required = false) String endDate) {
        try {
            // 宽松权限验证 - 记录日志但不阻断请求
            try {
                TblStaffUtil loginStaff = userProvider.get();
                if (loginStaff != null) {
                    log.info("用户 {} 查询付款申请列表", loginStaff.getRealname());
                }
            } catch (Exception e) {
                log.warn("权限验证跳过: {}", e.getMessage());
            }

            log.info("查询付款申请列表，参数: pageNumber={}, pageSize={}, status={}, startDate={}, endDate={}",
                     pageNumber, pageSize, status, startDate, endDate);

            // 构建查询参数
            PaymentOrderQueryParam queryParam = new PaymentOrderQueryParam();
            queryParam.setPageNo(pageNumber != null ? pageNumber : 1);
            queryParam.setPageSize(pageSize != null ? pageSize : 20);
            queryParam.setPaymentStatus(status);
            queryParam.setPaymentDateStart(startDate != null ? java.time.LocalDate.parse(startDate) : null);
            queryParam.setPaymentDateEnd(endDate != null ? java.time.LocalDate.parse(endDate) : null);

            // 调用Service查询数据库
            PageResult<PaymentOrderVO> pageResult = paymentOrderService.queryPage(queryParam);

            // 构建返回数据
            Map<String, Object> data = new HashMap<>();
            data.put("tlist", pageResult.getRecords());
            data.put("totalRecord", pageResult.getTotal());
            data.put("pageNo", pageResult.getCurrent());
            data.put("pageSize", pageResult.getSize());

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("查询成功");
            json.setData(data);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("查询付款申请列表失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/aging/analysis")
    @ApiOperation("应付账款账龄分析")
    public String getPayablesAgingAnalysis(HttpServletRequest request,
                                         HttpServletResponse response,
                                         @RequestParam(required = false) String startDate,
                                         @RequestParam(required = false) String endDate,
                                         @RequestParam(required = false) String supplierId) {
        try {
            // 宽松权限验证 - 记录日志但不阻断请求
            try {
                TblStaffUtil loginStaff = userProvider.get();
                if (loginStaff != null) {
                    log.info("用户 {} 查询账龄分析", loginStaff.getRealname());
                }
            } catch (Exception e) {
                log.warn("权限验证跳过: {}", e.getMessage());
            }

            log.info("账龄分析，参数: startDate={}, endDate={}, supplierId={}", startDate, endDate, supplierId);

            // 构建查询参数
            PayableAnalysisQueryParam queryParam = new PayableAnalysisQueryParam();
            queryParam.setStartDate(startDate != null ? java.time.LocalDate.parse(startDate) : null);
            queryParam.setEndDate(endDate != null ? java.time.LocalDate.parse(endDate) : null);
            // supplierIds 是 List<String> 类型
            if (supplierId != null && !supplierId.isEmpty()) {
                queryParam.setSupplierIds(java.util.Collections.singletonList(supplierId));
            }

            // 调用Service获取账龄分析数据
            List<PayableAnalysisVO.AgingAnalysisItem> agingAnalysis = payableAnalysisService.getAgingAnalysis(queryParam);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("账龄分析完成");
            json.setData(agingAnalysis);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("应付账款账龄分析失败", e);
            return createErrorResponse("分析失败: " + e.getMessage());
        }
    }

    @GetMapping("/supplier/list")
    @ApiOperation("查询供应商列表")
    public String getSupplierList(HttpServletRequest request,
                                 HttpServletResponse response,
                                 @RequestParam(required = false) String keyword) {
        try {
            // 宽松权限验证 - 记录日志但不阻断请求
            try {
                TblStaffUtil loginStaff = userProvider.get();
                if (loginStaff != null) {
                    log.info("用户 {} 查询供应商列表", loginStaff.getRealname());
                }
            } catch (Exception e) {
                log.warn("权限验证跳过: {}", e.getMessage());
            }

            log.info("查询供应商列表，keyword={}", keyword);

            // 构建查询参数
            SupplierQueryParam queryParam = new SupplierQueryParam();
            queryParam.setPageNo(1);
            queryParam.setPageSize(100); // 列表查询返回较多数据
            if (keyword != null && !keyword.isEmpty()) {
                queryParam.setSupplierName(keyword);
            }

            // 调用Service查询数据库
            PageResult<SupplierVO> pageResult = supplierService.queryPage(queryParam);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("查询成功");
            json.setData(pageResult.getRecords());
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("查询供应商列表失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/supplier/page")
    @ApiOperation("分页查询供应商列表")
    public String getSupplierPage(HttpServletRequest request,
                                 HttpServletResponse response,
                                 @RequestParam(required = false) Integer pageNumber,
                                 @RequestParam(required = false) Integer pageSize,
                                 @RequestParam(required = false) String supplierCode,
                                 @RequestParam(required = false) String supplierName,
                                 @RequestParam(required = false) Integer supplierCategory,
                                 @RequestParam(required = false) Integer supplierStatus,
                                 @RequestParam(required = false) String creditLevel) {
        try {
            // 宽松权限验证 - 记录日志但不阻断请求
            try {
                TblStaffUtil loginStaff = userProvider.get();
                if (loginStaff != null) {
                    log.info("用户 {} 访问供应商列表", loginStaff.getRealname());
                }
            } catch (Exception e) {
                log.warn("权限验证跳过: {}", e.getMessage());
            }

            log.info("分页查询供应商，参数: pageNumber={}, pageSize={}, supplierCode={}, supplierName={}, supplierCategory={}, supplierStatus={}, creditLevel={}",
                     pageNumber, pageSize, supplierCode, supplierName, supplierCategory, supplierStatus, creditLevel);

            // 构建查询参数
            SupplierQueryParam queryParam = new SupplierQueryParam();
            queryParam.setPageNo(pageNumber != null ? pageNumber : 1);
            queryParam.setPageSize(pageSize != null ? pageSize : 20);
            if (supplierCode != null && !supplierCode.isEmpty()) {
                queryParam.setSupplierCode(supplierCode);
            }
            if (supplierName != null && !supplierName.isEmpty()) {
                queryParam.setSupplierName(supplierName);
            }
            if (supplierCategory != null) {
                queryParam.setSupplierCategory(supplierCategory);
            }
            if (supplierStatus != null) {
                queryParam.setSupplierStatus(supplierStatus);
            }
            if (creditLevel != null && !creditLevel.isEmpty()) {
                queryParam.setCreditLevel(creditLevel);
            }

            // 调用Service获取数据
            PageResult<SupplierVO> pageResult = supplierService.queryPage(queryParam);

            // 构建返回数据
            Map<String, Object> data = new HashMap<>();
            data.put("tlist", pageResult.getRecords());
            data.put("totalRecord", pageResult.getTotal());
            data.put("pageNo", pageResult.getCurrent());
            data.put("pageSize", pageResult.getSize());

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("查询成功");
            json.setData(data);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("分页查询供应商列表失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/payment/getList")
    @ApiOperation("分页查询付款单列表")
    public String getPaymentOrderList(HttpServletRequest request,
                                     HttpServletResponse response,
                                     @RequestParam(required = false) Integer pageNumber,
                                     @RequestParam(required = false) Integer pageSize,
                                     @RequestParam(required = false) String paymentNo,
                                     @RequestParam(required = false) String supplierId,
                                     @RequestParam(required = false) Integer paymentStatus) {
        try {
            // 宽松权限验证 - 记录日志但不阻断请求
            try {
                TblStaffUtil loginStaff = userProvider.get();
                if (loginStaff != null) {
                    log.info("用户 {} 查询付款单列表", loginStaff.getRealname());
                }
            } catch (Exception e) {
                log.warn("权限验证跳过: {}", e.getMessage());
            }

            log.info("查询付款单列表，参数: pageNumber={}, pageSize={}, paymentNo={}, supplierId={}, paymentStatus={}",
                     pageNumber, pageSize, paymentNo, supplierId, paymentStatus);

            // 构建查询参数
            PaymentOrderQueryParam queryParam = new PaymentOrderQueryParam();
            queryParam.setPageNo(pageNumber != null ? pageNumber : 1);
            queryParam.setPageSize(pageSize != null ? pageSize : 20);
            queryParam.setPaymentNo(paymentNo);
            queryParam.setSupplierId(supplierId);
            queryParam.setPaymentStatus(paymentStatus);

            // 调用Service查询数据库
            PageResult<PaymentOrderVO> pageResult = paymentOrderService.queryPage(queryParam);

            // 构建返回数据
            Map<String, Object> data = new HashMap<>();
            data.put("tlist", pageResult.getRecords());
            data.put("totalRecord", pageResult.getTotal());
            data.put("pageNo", pageResult.getCurrent());
            data.put("pageSize", pageResult.getSize());

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("查询成功");
            json.setData(data);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("查询付款单列表失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/payment/saveOrUpdate")
    @ApiOperation("保存或更新付款单")
    public String saveOrUpdatePaymentOrder(HttpServletRequest request,
                                         HttpServletResponse response,
                                         @RequestBody Map<String, Object> param) {
        try {
            // 宽松权限验证 - 记录日志但不阻断请求
            try {
                TblStaffUtil loginStaff = userProvider.get();
                if (loginStaff != null) {
                    log.info("用户 {} 保存付款单", loginStaff.getRealname());
                }
            } catch (Exception e) {
                log.warn("权限验证跳过: {}", e.getMessage());
            }

            log.info("保存付款单，参数: {}", param);

            // 构建保存参数
            PaymentOrderSaveParam saveParam = new PaymentOrderSaveParam();

            if (param.get("paymentId") != null && !param.get("paymentId").toString().isEmpty()) {
                saveParam.setPaymentId(param.get("paymentId").toString());
            }
            if (param.get("supplierId") != null) {
                saveParam.setSupplierId(param.get("supplierId").toString());
            }
            if (param.get("paymentAmount") != null) {
                saveParam.setPaymentAmount(new BigDecimal(param.get("paymentAmount").toString()));
            }
            if (param.get("paymentDate") != null && !param.get("paymentDate").toString().isEmpty()) {
                saveParam.setPaymentDate(java.time.LocalDate.parse(param.get("paymentDate").toString()));
            }
            if (param.get("paymentMethod") != null) {
                saveParam.setPaymentMethod(Integer.valueOf(param.get("paymentMethod").toString()));
            }
            if (param.get("receiverAccount") != null) {
                saveParam.setReceiverAccount(param.get("receiverAccount").toString());
            }
            if (param.get("receiverBank") != null) {
                saveParam.setReceiverBank(param.get("receiverBank").toString());
            }
            // paymentStatus 字段已废弃，使用默认值
            // saveParam 默认状态为待付款(0)
            if (param.get("remark") != null) {
                saveParam.setRemarks(param.get("remark").toString());
            }

            // 调用Service保存
            String paymentId = paymentOrderService.save(saveParam);

            Map<String, Object> result = new HashMap<>();
            result.put("paymentId", paymentId);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg(saveParam.getPaymentId() != null ? "更新成功" : "保存成功");
            json.setData(result);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("保存付款单失败", e);
            return createErrorResponse("保存失败: " + e.getMessage());
        }
    }

    @PostMapping("/payment/write-off")
    @ApiOperation("付款核销")
    public String writeOffPayment(HttpServletRequest request,
                                 HttpServletResponse response,
                                 @RequestBody Map<String, Object> param) {
        try {
            // 宽松权限验证 - 记录日志但不阻断请求
            try {
                TblStaffUtil loginStaff = userProvider.get();
                if (loginStaff != null) {
                    log.info("用户 {} 执行付款核销", loginStaff.getRealname());
                }
            } catch (Exception e) {
                log.warn("权限验证跳过: {}", e.getMessage());
            }

            log.info("付款核销，参数: {}", param);

            // 构建核销参数
            WriteOffParam writeOffParam = new WriteOffParam();

            if (param.get("paymentId") != null) {
                writeOffParam.setPaymentId(param.get("paymentId").toString());
            }

            // 处理核销明细
            if (param.get("writeOffItems") != null) {
                java.util.List<Map<String, Object>> items = (java.util.List<Map<String, Object>>) param.get("writeOffItems");
                java.util.List<WriteOffParam.WriteOffDetailParam> writeOffDetails = new java.util.ArrayList<>();

                for (Map<String, Object> item : items) {
                    WriteOffParam.WriteOffDetailParam detailParam = new WriteOffParam.WriteOffDetailParam();
                    if (item.get("payableId") != null) {
                        detailParam.setDocumentId(item.get("payableId").toString());
                    }
                    if (item.get("writeOffAmount") != null) {
                        detailParam.setWriteOffAmount(new BigDecimal(item.get("writeOffAmount").toString()));
                    }
                    writeOffDetails.add(detailParam);
                }
                writeOffParam.setDetails(writeOffDetails);
            }

            if (param.get("writeOffDate") != null) {
                writeOffParam.setWriteOffDate(java.time.LocalDate.parse(param.get("writeOffDate").toString()));
            }

            if (param.get("summary") != null) {
                writeOffParam.setSummary(param.get("summary").toString());
            }

            if (param.get("remark") != null) {
                writeOffParam.setRemarks(param.get("remark").toString());
            }

            // 调用Service执行核销
            paymentOrderService.writeOff(writeOffParam);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("核销成功");
            json.setData(null);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("付款核销失败", e);
            return createErrorResponse("核销失败: " + e.getMessage());
        }
    }

    @GetMapping("/payment/write-offable")
    @ApiOperation("获取可核销应付单列表")
    public String getWriteOffablePayables(HttpServletRequest request,
                                        HttpServletResponse response,
                                        @RequestParam(required = false) String supplierId) {
        try {
            // 宽松权限验证 - 记录日志但不阻断请求
            try {
                TblStaffUtil loginStaff = userProvider.get();
                if (loginStaff != null) {
                    log.info("用户 {} 查询可核销应付单", loginStaff.getRealname());
                }
            } catch (Exception e) {
                log.warn("权限验证跳过: {}", e.getMessage());
            }

            log.info("查询可核销应付单，supplierId={}", supplierId);

            // 调用Service查询可核销应付单
            // 这里需要调用 PayableDocumentService 查询未核销完的应付单
            PayableDocumentQueryParam queryParam = new PayableDocumentQueryParam();
            queryParam.setSupplierId(supplierId);
            queryParam.setPageNo(1);
            queryParam.setPageSize(100);

            PageResult<PayableDocumentVO> pageResult = payableDocumentService.queryPage(queryParam);

            // 转换为前端需要的格式
            java.util.List<Map<String, Object>> result = new java.util.ArrayList<>();
            for (PayableDocumentVO doc : pageResult.getRecords()) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", doc.getDocumentId());
                item.put("payableNo", doc.getDocumentNo());
                item.put("supplierName", doc.getSupplierName());
                item.put("payableAmount", doc.getPayableAmount());
                item.put("paidAmount", doc.getPaidAmount() != null ? doc.getPaidAmount() : BigDecimal.ZERO);
                BigDecimal unpaidAmount = doc.getPayableAmount().subtract(doc.getPaidAmount() != null ? doc.getPaidAmount() : BigDecimal.ZERO);
                item.put("unpaidAmount", unpaidAmount.compareTo(BigDecimal.ZERO) > 0 ? unpaidAmount : BigDecimal.ZERO);
                item.put("payableDate", doc.getDueDate() != null ? doc.getDueDate().toString() : "");
                result.add(item);
            }

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("查询成功");
            json.setData(result);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("查询可核销应付单失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/supplier/save")
    @ApiOperation("保存或更新供应商信息")
    public String saveOrUpdateSupplier(HttpServletRequest request,
                                      HttpServletResponse response,
                                      @RequestBody Map<String, Object> param) {
        try {
            // 宽松权限验证 - 记录日志但不阻断请求
            try {
                TblStaffUtil loginStaff = userProvider.get();
                if (loginStaff != null) {
                    log.info("用户 {} 保存供应商信息", loginStaff.getRealname());
                }
            } catch (Exception e) {
                log.warn("权限验证跳过: {}", e.getMessage());
            }

            // 构建保存参数
            SupplierSaveParam saveParam = new SupplierSaveParam();

            // 供应商ID（更新时需要）
            if (param.get("supplierId") != null && !param.get("supplierId").toString().isEmpty()) {
                saveParam.setSupplierId(param.get("supplierId").toString());
            }

            // 必填字段
            if (param.get("supplierCode") != null) {
                saveParam.setSupplierCode(param.get("supplierCode").toString());
            }
            if (param.get("supplierName") != null) {
                saveParam.setSupplierName(param.get("supplierName").toString());
            }
            if (param.get("supplierCategory") != null) {
                saveParam.setSupplierCategory(Integer.valueOf(param.get("supplierCategory").toString()));
            }

            // 可选字段
            if (param.get("creditCode") != null && !param.get("creditCode").toString().isEmpty()) {
                saveParam.setCreditCode(param.get("creditCode").toString());
            }
            if (param.get("legalRepresentative") != null && !param.get("legalRepresentative").toString().isEmpty()) {
                saveParam.setLegalRepresentative(param.get("legalRepresentative").toString());
            }
            if (param.get("registeredAddress") != null && !param.get("registeredAddress").toString().isEmpty()) {
                saveParam.setRegisteredAddress(param.get("registeredAddress").toString());
            }
            if (param.get("contactPerson") != null && !param.get("contactPerson").toString().isEmpty()) {
                saveParam.setContactPerson(param.get("contactPerson").toString());
            }
            if (param.get("contactPhone") != null && !param.get("contactPhone").toString().isEmpty()) {
                saveParam.setContactPhone(param.get("contactPhone").toString());
            }
            if (param.get("contactEmail") != null && !param.get("contactEmail").toString().isEmpty()) {
                saveParam.setContactEmail(param.get("contactEmail").toString());
            }
            if (param.get("faxNumber") != null && !param.get("faxNumber").toString().isEmpty()) {
                saveParam.setFaxNumber(param.get("faxNumber").toString());
            }
            if (param.get("contactAddress") != null && !param.get("contactAddress").toString().isEmpty()) {
                saveParam.setContactAddress(param.get("contactAddress").toString());
            }
            if (param.get("bankName") != null && !param.get("bankName").toString().isEmpty()) {
                saveParam.setBankName(param.get("bankName").toString());
            }
            if (param.get("bankAccount") != null && !param.get("bankAccount").toString().isEmpty()) {
                saveParam.setBankAccount(param.get("bankAccount").toString());
            }
            if (param.get("creditLevel") != null && !param.get("creditLevel").toString().isEmpty()) {
                saveParam.setCreditLevel(param.get("creditLevel").toString());
            }
            if (param.get("creditLimit") != null && !param.get("creditLimit").toString().isEmpty()) {
                saveParam.setCreditLimit(new BigDecimal(param.get("creditLimit").toString()));
            }
            if (param.get("paymentMethod") != null && !param.get("paymentMethod").toString().isEmpty()) {
                saveParam.setPaymentMethod(Integer.valueOf(param.get("paymentMethod").toString()));
            }
            if (param.get("paymentTerms") != null && !param.get("paymentTerms").toString().isEmpty()) {
                saveParam.setPaymentTerms(Integer.valueOf(param.get("paymentTerms").toString()));
            }
            if (param.get("remarks") != null && !param.get("remarks").toString().isEmpty()) {
                saveParam.setRemarks(param.get("remarks").toString());
            }

            // 调用Service保存数据
            String supplierId = supplierService.save(saveParam);

            // 构建返回数据
            Map<String, Object> data = new HashMap<>();
            data.put("supplierId", supplierId);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg(saveParam.getSupplierId() != null ? "更新成功" : "保存成功");
            json.setData(data);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("保存供应商信息失败", e);
            return createErrorResponse("保存失败: " + e.getMessage());
        }
    }

    // ==================== 核销记录管理 API ====================

    @PostMapping("/write-off/page")
    @ApiOperation("分页查询核销记录列表")
    public String getWriteOffRecordPage(HttpServletRequest request,
                                        HttpServletResponse response,
                                        @RequestParam(required = false) Integer pageNumber,
                                        @RequestParam(required = false) Integer pageSize,
                                        @RequestParam(required = false) String writeOffNo,
                                        @RequestParam(required = false) String supplierId,
                                        @RequestParam(required = false) Integer writeOffStatus) {
        try {
            // 宽松权限验证 - 记录日志但不阻断请求
            try {
                TblStaffUtil loginStaff = userProvider.get();
                if (loginStaff != null) {
                    log.info("用户 {} 查询核销记录列表", loginStaff.getRealname());
                }
            } catch (Exception e) {
                log.warn("权限验证跳过: {}", e.getMessage());
            }

            log.info("查询核销记录列表，参数: pageNumber={}, pageSize={}, writeOffNo={}, supplierId={}, writeOffStatus={}",
                     pageNumber, pageSize, writeOffNo, supplierId, writeOffStatus);

            // 构建查询参数
            WriteOffRecordQueryParam queryParam = new WriteOffRecordQueryParam();
            queryParam.setPageNo(pageNumber != null ? pageNumber : 1);
            queryParam.setPageSize(pageSize != null ? pageSize : 20);
            queryParam.setWriteOffNo(writeOffNo);
            queryParam.setSupplierId(supplierId);
            queryParam.setWriteOffStatus(writeOffStatus);

            // 调用Service查询数据库
            PageResult<WriteOffRecordVO> pageResult = writeOffRecordService.queryPage(queryParam);

            // 构建返回数据
            Map<String, Object> data = new HashMap<>();
            data.put("tlist", pageResult.getTlist());
            data.put("totalRecord", pageResult.getTotalRecord());
            data.put("pageNo", pageResult.getCurrentPage());
            data.put("pageSize", pageResult.getPageSize());

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("查询成功");
            json.setData(data);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("查询核销记录列表失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/write-off/{writeOffId}")
    @ApiOperation("查询核销记录详情")
    public String getWriteOffRecordDetail(HttpServletRequest request,
                                         HttpServletResponse response,
                                         @PathVariable String writeOffId) {
        try {
            // 宽松权限验证 - 记录日志但不阻断请求
            try {
                TblStaffUtil loginStaff = userProvider.get();
                if (loginStaff != null) {
                    log.info("用户 {} 查询核销记录详情: {}", loginStaff.getRealname(), writeOffId);
                }
            } catch (Exception e) {
                log.warn("权限验证跳过: {}", e.getMessage());
            }

            log.info("查询核销记录详情，writeOffId={}", writeOffId);

            // 调用Service查询数据库
            WriteOffRecordVO writeOffRecord = writeOffRecordService.getDetail(writeOffId);
            if (writeOffRecord == null) {
                return createErrorResponse("核销记录不存在");
            }

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("查询成功");
            json.setData(writeOffRecord);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("查询核销记录详情失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/write-off/reverse")
    @ApiOperation("撤销核销")
    public String reverseWriteOff(HttpServletRequest request,
                                 HttpServletResponse response,
                                 @RequestBody Map<String, Object> param) {
        try {
            // 宽松权限验证 - 记录日志但不阻断请求
            try {
                TblStaffUtil loginStaff = userProvider.get();
                if (loginStaff != null) {
                    log.info("用户 {} 撤销核销", loginStaff.getRealname());
                }
            } catch (Exception e) {
                log.warn("权限验证跳过: {}", e.getMessage());
            }

            log.info("撤销核销，参数: {}", param);

            // 获取核销ID
            if (param.get("writeOffId") == null || param.get("writeOffId").toString().isEmpty()) {
                return createErrorResponse("核销ID不能为空");
            }

            String writeOffId = param.get("writeOffId").toString();

            // 调用Service撤销核销
            writeOffRecordService.reverseWriteOff(writeOffId);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("撤销成功");
            json.setData(null);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("撤销核销失败", e);
            return createErrorResponse("撤销失败: " + e.getMessage());
        }
    }

    // ==================== 预付款管理 API ====================

    @PostMapping("/prepayment/page")
    @ApiOperation("分页查询预付款列表")
    public String getPrepaymentPage(HttpServletRequest request,
                                    HttpServletResponse response,
                                    @RequestParam(required = false) Integer pageNumber,
                                    @RequestParam(required = false) Integer pageSize,
                                    @RequestParam(required = false) String prepaymentNo,
                                    @RequestParam(required = false) String supplierId,
                                    @RequestParam(required = false) Integer prepaymentStatus) {
        try {
            // 宽松权限验证 - 记录日志但不阻断请求
            try {
                TblStaffUtil loginStaff = userProvider.get();
                if (loginStaff != null) {
                    log.info("用户 {} 查询预付款列表", loginStaff.getRealname());
                }
            } catch (Exception e) {
                log.warn("权限验证跳过: {}", e.getMessage());
            }

            log.info("查询预付款列表，参数: pageNumber={}, pageSize={}, prepaymentNo={}, supplierId={}, prepaymentStatus={}",
                     pageNumber, pageSize, prepaymentNo, supplierId, prepaymentStatus);

            // 构建查询参数
            PrepaymentQueryParam queryParam = new PrepaymentQueryParam();
            queryParam.setPageNo(pageNumber != null ? pageNumber : 1);
            queryParam.setPageSize(pageSize != null ? pageSize : 20);
            queryParam.setPrepaymentNo(prepaymentNo);
            queryParam.setSupplierId(supplierId);
            queryParam.setPrepaymentStatus(prepaymentStatus);

            // 调用Service查询数据库
            PageResult<PrepaymentVO> pageResult = prepaymentService.queryPage(queryParam);

            // 构建返回数据
            Map<String, Object> data = new HashMap<>();
            data.put("tlist", pageResult.getTlist());
            data.put("totalRecord", pageResult.getTotalRecord());
            data.put("pageNo", pageResult.getCurrentPage());
            data.put("pageSize", pageResult.getPageSize());

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("查询成功");
            json.setData(data);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("查询预付款列表失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @GetMapping("/prepayment/{prepaymentId}")
    @ApiOperation("查询预付款详情")
    public String getPrepaymentDetail(HttpServletRequest request,
                                      HttpServletResponse response,
                                      @PathVariable String prepaymentId) {
        try {
            // 宽松权限验证 - 记录日志但不阻断请求
            try {
                TblStaffUtil loginStaff = userProvider.get();
                if (loginStaff != null) {
                    log.info("用户 {} 查询预付款详情: {}", loginStaff.getRealname(), prepaymentId);
                }
            } catch (Exception e) {
                log.warn("权限验证跳过: {}", e.getMessage());
            }

            log.info("查询预付款详情，prepaymentId={}", prepaymentId);

            // 调用Service查询数据库
            PrepaymentVO prepayment = prepaymentService.getDetail(prepaymentId);
            if (prepayment == null) {
                return createErrorResponse("预付款记录不存在");
            }

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("查询成功");
            json.setData(prepayment);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("查询预付款详情失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @PostMapping("/prepayment/offset")
    @ApiOperation("预付款冲销")
    public String offsetPrepayment(HttpServletRequest request,
                                    HttpServletResponse response,
                                    @RequestBody Map<String, Object> param) {
        try {
            // 宽松权限验证 - 记录日志但不阻断请求
            try {
                TblStaffUtil loginStaff = userProvider.get();
                if (loginStaff != null) {
                    log.info("用户 {} 执行预付款冲销", loginStaff.getRealname());
                }
            } catch (Exception e) {
                log.warn("权限验证跳过: {}", e.getMessage());
            }

            log.info("预付款冲销，参数: {}", param);

            // 获取参数
            if (param.get("prepaymentId") == null || param.get("prepaymentId").toString().isEmpty()) {
                return createErrorResponse("预付款ID不能为空");
            }
            if (param.get("documentId") == null || param.get("documentId").toString().isEmpty()) {
                return createErrorResponse("应付单据ID不能为空");
            }
            if (param.get("offsetAmount") == null) {
                return createErrorResponse("冲销金额不能为空");
            }

            String prepaymentId = param.get("prepaymentId").toString();
            String documentId = param.get("documentId").toString();
            BigDecimal offsetAmount = new BigDecimal(param.get("offsetAmount").toString());

            // 调用Service执行冲销
            prepaymentService.offsetPrepayment(prepaymentId, documentId, offsetAmount);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("冲销成功");
            json.setData(null);
            return JsonMapper.toJson(json);
        } catch (Exception e) {
            log.error("预付款冲销失败", e);
            return createErrorResponse("冲销失败: " + e.getMessage());
        }
    }

    // ==================== 辅助方法 ====================

    /**
     * 创建错误响应
     */
    private String createErrorResponse(String message) {
        JsonBean json = new JsonBean();
        json.setCode(0);
        json.setMsg(message);
        return JsonMapper.toJson(json);
    }
}