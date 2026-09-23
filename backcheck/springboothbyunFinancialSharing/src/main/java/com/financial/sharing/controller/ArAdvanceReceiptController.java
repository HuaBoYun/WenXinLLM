package com.financial.sharing.controller;

import com.financial.sharing.dto.param.ArAdvanceReceiptQueryParam;
import com.financial.sharing.oracle.entity.ArAdvanceReceiptEntity;
import com.financial.sharing.service.ArAdvanceReceiptService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Map;

/**
 * 预收款管理控制器
 * @author system
 * @since 2026-01-04
 */
@RestController
@RequestMapping("/ar/advanceReceipt")
@Api(tags = "应收管理-预收款管理")
@CrossOrigin
public class ArAdvanceReceiptController extends BaseController {

    @Resource
    private ArAdvanceReceiptService arAdvanceReceiptService;

    @PostMapping("/page")
    @ApiOperation("分页查询预收款列表")
    public MyJsonBean<PageResult> getAdvanceReceiptPage(@RequestBody ArAdvanceReceiptQueryParam param) {
        return arAdvanceReceiptService.getAdvanceReceiptList(param);
    }

    @GetMapping("/{advanceId}")
    @ApiOperation("根据ID查询预收款详情")
    public MyJsonBean getAdvanceReceiptById(@PathVariable @ApiParam("预收款ID") String advanceId) {
        return arAdvanceReceiptService.getAdvanceReceiptById(advanceId);
    }

    @PostMapping("/saveOrUpdate")
    @ApiOperation("保存或更新预收款")
    public MyJsonBean saveOrUpdateAdvanceReceipt(@RequestBody ArAdvanceReceiptEntity entity) {
        return arAdvanceReceiptService.saveOrUpdateAdvanceReceipt(entity);
    }

    @DeleteMapping("/{advanceId}")
    @ApiOperation("删除预收款")
    public MyJsonBean deleteAdvanceReceipt(@PathVariable @ApiParam("预收款ID") String advanceId) {
        return arAdvanceReceiptService.deleteAdvanceReceipt(advanceId);
    }

    @GetMapping("/available")
    @ApiOperation("查询客户可用预收款")
    public MyJsonBean getAvailableAdvanceReceipts(
            @RequestParam @ApiParam("客户ID") String customerId,
            @RequestParam @ApiParam("租户ID") Long tenantId) {
        return arAdvanceReceiptService.getAvailableAdvanceReceipts(customerId, tenantId);
    }

    @PostMapping("/offset/{advanceId}")
    @ApiOperation("预收款冲销")
    public MyJsonBean offsetAdvanceReceipt(
            @PathVariable @ApiParam("预收款ID") String advanceId,
            @RequestBody Map<String, Object> offsetInfo) {
        BigDecimal offsetAmount = new BigDecimal(offsetInfo.get("offsetAmount").toString());
        String operatorId = (String) offsetInfo.get("operatorId");
        return arAdvanceReceiptService.offsetAdvanceReceipt(advanceId, offsetAmount, operatorId);
    }

    @PostMapping("/statistics")
    @ApiOperation("查询预收款统计")
    public MyJsonBean getAdvanceReceiptStatistics(@RequestBody ArAdvanceReceiptQueryParam param) {
        return arAdvanceReceiptService.getAdvanceReceiptStatistics(param);
    }
}

