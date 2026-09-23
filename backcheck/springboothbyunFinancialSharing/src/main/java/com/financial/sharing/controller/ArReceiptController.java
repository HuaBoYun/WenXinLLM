package com.financial.sharing.controller;

import com.financial.sharing.service.ArReceiptService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.ArReceiptSaveParam;
import com.financial.sharing.vo.param.ArReceiptQueryParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 收款单管理控制器
 * @author system
 * @since 2026-01-04
 */
@RestController
@RequestMapping("/ar/receipt")
@Api(tags = "应收管理-收款管理")
@CrossOrigin
public class ArReceiptController extends BaseController {

    @Resource
    private ArReceiptService arReceiptService;

    @PostMapping("/page")
    @ApiOperation("分页查询收款单列表")
    public MyJsonBean<PageResult> getReceiptPage(@RequestBody ArReceiptQueryParam param) {
        return arReceiptService.getReceiptList(param);
    }

    @GetMapping("/{receiptId}")
    @ApiOperation("根据ID查询收款单详情")
    public MyJsonBean getReceiptById(@PathVariable @ApiParam("收款单ID") String receiptId) {
        return arReceiptService.getReceiptById(receiptId);
    }

    @GetMapping("/receiptNo/{receiptNo}")
    @ApiOperation("根据收款单号查询收款单")
    public MyJsonBean getReceiptByNo(@PathVariable @ApiParam("收款单号") String receiptNo,
                                     @RequestParam @ApiParam("租户ID") Long tenantId) {
        return arReceiptService.getReceiptByNo(receiptNo, tenantId);
    }

    @PostMapping("/saveOrUpdate")
    @ApiOperation("保存或更新收款单")
    public MyJsonBean saveOrUpdateReceipt(@RequestBody ArReceiptSaveParam param) {
        return arReceiptService.saveOrUpdateReceipt(param);
    }

    @DeleteMapping("/{receiptId}")
    @ApiOperation("删除收款单")
    public MyJsonBean deleteReceipt(@PathVariable @ApiParam("收款单ID") String receiptId) {
        return arReceiptService.deleteReceipt(receiptId);
    }

    @PostMapping("/batchDelete")
    @ApiOperation("批量删除收款单")
    public MyJsonBean batchDeleteReceipt(@RequestBody List<String> receiptIds) {
        return arReceiptService.batchDeleteReceipt(receiptIds);
    }

    @PostMapping("/confirm/{receiptId}")
    @ApiOperation("确认收款")
    public MyJsonBean confirmReceipt(@PathVariable @ApiParam("收款单ID") String receiptId,
                                     @RequestBody Map<String, Object> confirmInfo) {
        BigDecimal confirmAmount = new BigDecimal(confirmInfo.get("confirmAmount").toString());
        String confirmBy = (String) confirmInfo.get("confirmBy");
        return arReceiptService.confirmReceipt(receiptId, confirmAmount, confirmBy);
    }

    @PostMapping("/cancel/{receiptId}")
    @ApiOperation("取消收款单")
    public MyJsonBean cancelReceipt(@PathVariable @ApiParam("收款单ID") String receiptId,
                                    @RequestBody Map<String, String> cancelInfo) {
        String cancelReason = cancelInfo.get("cancelReason");
        String cancelBy = cancelInfo.get("cancelBy");
        return arReceiptService.cancelReceipt(receiptId, cancelReason, cancelBy);
    }

    @GetMapping("/customer/{customerId}")
    @ApiOperation("查询客户的收款单")
    public MyJsonBean getReceiptsByCustomer(@PathVariable @ApiParam("客户ID") String customerId,
                                            @RequestParam @ApiParam("租户ID") Long tenantId) {
        return arReceiptService.getReceiptsByCustomer(customerId, tenantId);
    }

    @GetMapping("/pendingWriteOff")
    @ApiOperation("查询待核销的收款单")
    public MyJsonBean getPendingWriteOffReceipts(@RequestParam @ApiParam("客户ID") String customerId,
                                                 @RequestParam @ApiParam("租户ID") Long tenantId) {
        return arReceiptService.getPendingWriteOffReceipts(customerId, tenantId);
    }

    @PostMapping("/statistics")
    @ApiOperation("查询收款单统计")
    public MyJsonBean getReceiptStatistics(@RequestBody ArReceiptQueryParam param) {
        return arReceiptService.getReceiptStatistics(param);
    }

    @PostMapping("/export")
    @ApiOperation("导出收款单")
    public MyJsonBean exportReceipts(@RequestBody ArReceiptQueryParam param) {
        return arReceiptService.exportReceipts(param);
    }

    @PostMapping("/batchWriteOff")
    @ApiOperation("批量核销收款单")
    public MyJsonBean batchWriteOff(@RequestBody Map<String, Object> params) {
        @SuppressWarnings("unchecked")
        List<String> receiptIds = (List<String>) params.get("receiptIds");
        String operatorId = (String) params.get("operatorId");
        String remark = (String) params.get("remark");
        return arReceiptService.batchWriteOff(receiptIds, operatorId, remark);
    }
}

