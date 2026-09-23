package com.financial.sharing.controller;

import com.financial.sharing.dto.param.BillEndorseParam;
import com.financial.sharing.dto.param.BillPaymentParam;
import com.financial.sharing.dto.param.PayableBillQueryParam;
import com.financial.sharing.dto.param.PayableBillSaveParam;
import com.financial.sharing.service.PayableBillService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.result.BillEndorseVO;
import com.financial.sharing.vo.result.BillPaymentVO;
import com.financial.sharing.vo.result.PayableBillVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 应付票据Controller
 * @author system
 * @since 2025-01-05
 */
@Api(tags = "应付票据管理")
@RestController
@RequestMapping("/financial/payables/bills")
public class PayableBillController {

    @Resource
    private PayableBillService payableBillService;

    @ApiOperation("分页查询应付票据列表")
    @PostMapping("/getList")
    public MyJsonBean<PageResult<PayableBillVO>> getPayableBillList(PayableBillQueryParam param) {
        PageResult<PayableBillVO> result = payableBillService.getPayableBillList(param);
        return MyJsonBean.successData(result);
    }

    @ApiOperation("保存或更新应付票据")
    @PostMapping("/saveOrUpdate")
    public MyJsonBean<String> saveOrUpdatePayableBill(@Validated PayableBillSaveParam param) {
        String billId = payableBillService.saveOrUpdate(param);
        return MyJsonBean.successData("保存成功", billId);
    }

    @ApiOperation("获取应付票据详情")
    @GetMapping("/{billId}")
    public MyJsonBean<PayableBillVO> getPayableBillDetail(
            @ApiParam("票据ID") @PathVariable String billId) {
        PayableBillVO result = payableBillService.getDetail(billId);
        return MyJsonBean.successData(result);
    }

    @ApiOperation("删除应付票据")
    @DeleteMapping("/{billId}")
    public MyJsonBean<Void> deletePayableBill(
            @ApiParam("票据ID") @PathVariable String billId) {
        payableBillService.delete(billId);
        return MyJsonBean.successData();
    }

    @ApiOperation("作废应付票据")
    @PostMapping("/{billId}/cancel")
    public MyJsonBean<Void> cancelPayableBill(
            @ApiParam("票据ID") @PathVariable String billId,
            @RequestParam(required = false) String remarks) {
        payableBillService.cancelBill(billId, remarks);
        return MyJsonBean.successData();
    }

    @ApiOperation("票据审核")
    @PostMapping("/{billId}/audit")
    public MyJsonBean<Void> auditPayableBill(
            @ApiParam("票据ID") @PathVariable String billId,
            @RequestParam(required = false, defaultValue = "true") Boolean approved,
            @RequestParam(required = false) String comments) {
        payableBillService.audit(billId, approved, comments);
        return MyJsonBean.successData();
    }

    @ApiOperation("票据背书转让")
    @PostMapping("/{billId}/endorse")
    public MyJsonBean<Void> endorseBill(
            @ApiParam("票据ID") @PathVariable String billId,
            BillEndorseParam param) {
        payableBillService.endorse(billId, param);
        return MyJsonBean.successData();
    }

    @ApiOperation("票据兑付处理")
    @PostMapping("/{billId}/pay")
    public MyJsonBean<Void> payBill(
            @ApiParam("票据ID") @PathVariable String billId,
            BillPaymentParam param) {
        payableBillService.payment(billId, param);
        return MyJsonBean.successData();
    }

    @ApiOperation("获取票据到期提醒")
    @GetMapping("/due-reminders")
    public MyJsonBean<List<PayableBillVO>> getBillDueReminders(
            @ApiParam("到期范围") @RequestParam(required = false, defaultValue = "7") String range,
            @ApiParam("票据类型") @RequestParam(required = false) String billType) {
        List<PayableBillVO> result = payableBillService.getBillDueReminders(range, billType);
        return MyJsonBean.successData(result);
    }

    @ApiOperation("获取票据统计信息")
    @GetMapping("/statistics")
    public MyJsonBean<Map<String, Object>> getBillStatistics() {
        Map<String, Object> result = payableBillService.getBillStatistics();
        return MyJsonBean.successData(result);
    }

    @ApiOperation("获取背书列表")
    @GetMapping("/endorse/list")
    public MyJsonBean<List<BillEndorseVO>> getEndorseList() {
        List<BillEndorseVO> result = payableBillService.getEndorseList();
        return MyJsonBean.successData(result);
    }

    @ApiOperation("审批背书")
    @PostMapping("/endorse/{endorseId}/approve")
    public MyJsonBean<Void> approveEndorse(
            @ApiParam("背书ID") @PathVariable String endorseId,
            @RequestParam(required = false, defaultValue = "true") Boolean approved,
            @RequestParam(required = false) String comments) {
        payableBillService.approveEndorse(endorseId, approved, comments);
        return MyJsonBean.successData();
    }

    @ApiOperation("撤销背书")
    @PostMapping("/endorse/{endorseId}/cancel")
    public MyJsonBean<Void> cancelEndorse(
            @ApiParam("背书ID") @PathVariable String endorseId) {
        payableBillService.cancelEndorse(endorseId);
        return MyJsonBean.successData();
    }

    @ApiOperation("获取兑付列表")
    @GetMapping("/payment/list")
    public MyJsonBean<List<BillPaymentVO>> getPaymentList() {
        List<BillPaymentVO> result = payableBillService.getPaymentList();
        return MyJsonBean.successData(result);
    }

    @ApiOperation("确认兑付")
    @PostMapping("/payment/{paymentId}/confirm")
    public MyJsonBean<Void> confirmPayment(
            @ApiParam("兑付ID") @PathVariable String paymentId,
            @RequestParam(required = false) Map<String, Object> confirmData) {
        payableBillService.confirmPayment(paymentId, confirmData);
        return MyJsonBean.successData();
    }

    @ApiOperation("批量操作")
    @PostMapping("/batch/{operation}")
    public MyJsonBean<Void> batchOperation(
            @ApiParam("操作类型") @PathVariable String operation,
            @RequestParam List<String> billIds,
            @RequestParam(required = false) Map<String, Object> data) {
        payableBillService.batchOperation(operation, billIds, data);
        return MyJsonBean.successData();
    }

    @ApiOperation("导出票据")
    @PostMapping("/export")
    public void exportBills(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        payableBillService.exportBills(params, response);
    }
}
