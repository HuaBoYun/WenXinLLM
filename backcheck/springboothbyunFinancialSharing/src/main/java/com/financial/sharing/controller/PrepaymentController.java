package com.financial.sharing.controller;

import com.financial.sharing.business.entity.TblPrepayment;
import com.financial.sharing.business.service.PrepaymentServiceNew;
import com.financial.sharing.util.MyJsonBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/**
 * 预付款管理控制器
 *
 * @author Financial Sharing System
 * @since 2026-03-02
 */
@Api(tags = "预付款管理")
@RestController
@RequestMapping("/prepayments")
@CrossOrigin
public class PrepaymentController {

    @Autowired
    private PrepaymentServiceNew prepaymentService;

    @ApiOperation("查询预付款列表")
    @GetMapping
    public MyJsonBean getPrepaymentList(
            @RequestParam(required = false) String prepaymentNumber,
            @RequestParam(required = false) String applicant,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String prepaymentType,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate,
            @RequestParam(required = false) String applicantDeptId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        try {
            Map<String, Object> param = new java.util.HashMap<>();
            param.put("prepaymentNumber", prepaymentNumber);
            param.put("applicant", applicant);
            param.put("status", status);
            param.put("prepaymentType", prepaymentType);
            param.put("startDate", startDate);
            param.put("endDate", endDate);
            param.put("applicantDeptId", applicantDeptId);
            param.put("pageNum", pageNum);
            param.put("pageSize", pageSize);

            return prepaymentService.getList(param);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @ApiOperation("根据ID获取预付款详情")
    @GetMapping("/{prepaymentId}")
    public MyJsonBean getPrepaymentDetail(@PathVariable String prepaymentId) {
        try {
            return prepaymentService.getById(prepaymentId);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @ApiOperation("根据预付款单号查询")
    @GetMapping("/code/{prepaymentCode}")
    public MyJsonBean getPrepaymentByCode(@PathVariable String prepaymentCode) {
        try {
            return prepaymentService.getByPrepaymentCode(prepaymentCode);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }

    @ApiOperation("保存预付款")
    @PostMapping
    public MyJsonBean savePrepayment(@RequestBody TblPrepayment prepayment) {
        try {
            return prepaymentService.saveOrUpdate(prepayment);
        } catch (Exception e) {
            return MyJsonBean.errorData("保存失败：" + e.getMessage());
        }
    }

    @ApiOperation("删除预付款")
    @DeleteMapping("/{prepaymentId}")
    public MyJsonBean deletePrepayment(@PathVariable String prepaymentId) {
        try {
            return prepaymentService.delete(prepaymentId);
        } catch (Exception e) {
            return MyJsonBean.errorData("删除失败：" + e.getMessage());
        }
    }

    @ApiOperation("批量删除预付款")
    @DeleteMapping("/batch")
    public MyJsonBean batchDeletePrepayments(@RequestBody List<String> prepaymentIds) {
        try {
            return prepaymentService.batchDelete(prepaymentIds);
        } catch (Exception e) {
            return MyJsonBean.errorData("批量删除失败：" + e.getMessage());
        }
    }

    @ApiOperation("提交预付款申请")
    @PostMapping("/{prepaymentId}/submit")
    public MyJsonBean submitPrepayment(@PathVariable String prepaymentId) {
        try {
            return prepaymentService.submit(prepaymentId);
        } catch (Exception e) {
            return MyJsonBean.errorData("提交失败：" + e.getMessage());
        }
    }

    @ApiOperation("审批预付款")
    @PostMapping("/{prepaymentId}/approve")
    public MyJsonBean approvePrepayment(@PathVariable String prepaymentId,
                                         @RequestBody Map<String, Object> approvalData) {
        try {
            // 兼容前端两种格式: {action, opinion} 或 {approved, rejectReason}
            String action;
            String opinion;
            if (approvalData.containsKey("action")) {
                action = (String) approvalData.get("action");
                opinion = (String) approvalData.get("opinion");
            } else {
                Boolean approved = (Boolean) approvalData.get("approved");
                action = approved ? "APPROVE" : "REJECT";
                opinion = approved ?
                    (String) approvalData.getOrDefault("opinion", "审批通过") :
                    (String) approvalData.getOrDefault("rejectReason", "审批驳回");
            }
            return prepaymentService.approve(prepaymentId, action, opinion);
        } catch (Exception e) {
            return MyJsonBean.errorData("审批失败：" + e.getMessage());
        }
    }

    @ApiOperation("预付款付款")
    @PostMapping("/{prepaymentId}/pay")
    public MyJsonBean payPrepayment(@PathVariable String prepaymentId,
                                   @RequestParam(required = false) String voucherNo) {
        try {
            return prepaymentService.pay(prepaymentId, voucherNo);
        } catch (Exception e) {
            return MyJsonBean.errorData("付款失败：" + e.getMessage());
        }
    }

    @ApiOperation("预付款核销")
    @PostMapping("/{prepaymentId}/writeoff")
    public MyJsonBean writeoffPrepayment(@PathVariable String prepaymentId,
                                        @RequestBody Map<String, Object> writeoffData) {
        try {
            // 兼容前端字段名 writeOffAmount 或 amount
            BigDecimal amount = writeoffData.get("writeOffAmount") != null ?
                new BigDecimal(writeoffData.get("writeOffAmount").toString()) :
                (writeoffData.get("amount") != null ?
                    new BigDecimal(writeoffData.get("amount").toString()) : BigDecimal.ZERO);
            String relatedDocType = (String) writeoffData.getOrDefault("relatedDocType", "");
            String relatedDocNumber = (String) writeoffData.getOrDefault("relatedDocNumber", "");
            String relatedBillId = (String) writeoffData.getOrDefault("relatedBillId", relatedDocNumber);
            String contractId = (String) writeoffData.getOrDefault("contractId", "");
            return prepaymentService.writeoff(prepaymentId, amount, relatedBillId, contractId);
        } catch (Exception e) {
            return MyJsonBean.errorData("核销失败：" + e.getMessage());
        }
    }

    @ApiOperation("预付款退款")
    @PostMapping("/{prepaymentId}/refund")
    public MyJsonBean refundPrepayment(@PathVariable String prepaymentId,
                                       @RequestBody Map<String, Object> refundData) {
        try {
            BigDecimal amount = new BigDecimal(refundData.get("amount").toString());
            String reason = (String) refundData.get("reason");
            return prepaymentService.refund(prepaymentId, amount, reason);
        } catch (Exception e) {
            return MyJsonBean.errorData("退款失败：" + e.getMessage());
        }
    }

    @ApiOperation("导出预付款")
    @GetMapping("/export")
    public MyJsonBean exportPrepayments(
            @RequestParam(required = false) String prepaymentNumber,
            @RequestParam(required = false) String applicant,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String prepaymentType,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("prepaymentNumber", prepaymentNumber);
            param.put("applicant", applicant);
            param.put("status", status);
            param.put("prepaymentType", prepaymentType);
            param.put("startDate", startDate);
            param.put("endDate", endDate);
            return prepaymentService.export(param);
        } catch (Exception e) {
            return MyJsonBean.errorData("导出失败：" + e.getMessage());
        }
    }

    @ApiOperation("获取预付款统计")
    @GetMapping("/statistics")
    public MyJsonBean getPrepaymentStatistics(
            @RequestParam(required = false) String applicantDeptId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        try {
            Map<String, Object> param = new HashMap<>();
            param.put("applicantDeptId", applicantDeptId);
            param.put("startDate", startDate);
            param.put("endDate", endDate);
            return prepaymentService.getStatistics(param);
        } catch (Exception e) {
            return MyJsonBean.errorData("查询失败：" + e.getMessage());
        }
    }
}
