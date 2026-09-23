package com.financial.sharing.controller;

import com.financial.sharing.service.ArWriteOffService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.ArWriteOffQueryParam;
import com.financial.sharing.vo.param.ArWriteOffSaveParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 核销管理控制器
 * @author system
 * @since 2026-01-04
 */
@RestController
@RequestMapping("/ar/writeOff")
@Api(tags = "应收管理-核销管理")
@CrossOrigin
public class ArWriteOffController extends BaseController {

    @Resource
    private ArWriteOffService arWriteOffService;

    @PostMapping("/page")
    @ApiOperation("分页查询核销记录列表")
    public MyJsonBean<PageResult> getWriteOffPage(@RequestBody ArWriteOffQueryParam param) {
        return arWriteOffService.getWriteOffList(param);
    }

    @GetMapping("/{writeOffId}")
    @ApiOperation("根据ID查询核销记录详情")
    public MyJsonBean getWriteOffById(@PathVariable @ApiParam("核销ID") String writeOffId) {
        return arWriteOffService.getWriteOffById(writeOffId);
    }

    @PostMapping("/execute")
    @ApiOperation("执行核销")
    public MyJsonBean executeWriteOff(@RequestBody ArWriteOffSaveParam param) {
        return arWriteOffService.executeWriteOff(param);
    }

    @PostMapping("/auto")
    @ApiOperation("自动核销")
    public MyJsonBean autoWriteOff(@RequestBody Map<String, Object> params) {
        String customerId = (String) params.get("customerId");
        Long tenantId = params.get("tenantId") != null ? Long.valueOf(params.get("tenantId").toString()) : null;
        return arWriteOffService.autoWriteOff(customerId, tenantId);
    }

    @PostMapping("/reverse/{receiptId}")
    @ApiOperation("反核销")
    public MyJsonBean reverseWriteOff(@PathVariable @ApiParam("收款单ID") String receiptId,
                                      @RequestBody Map<String, String> operatorInfo) {
        String operatorId = operatorInfo.get("operatorId");
        return arWriteOffService.reverseWriteOff(receiptId, operatorId);
    }

    @GetMapping("/receipt/{receiptId}")
    @ApiOperation("根据收款单ID查询核销记录")
    public MyJsonBean getWriteOffsByReceiptId(@PathVariable @ApiParam("收款单ID") String receiptId) {
        return arWriteOffService.getWriteOffsByReceiptId(receiptId);
    }

    @GetMapping("/receivable/{receivableId}")
    @ApiOperation("根据应收单ID查询核销记录")
    public MyJsonBean getWriteOffsByReceivableId(@PathVariable @ApiParam("应收单ID") String receivableId) {
        return arWriteOffService.getWriteOffsByReceivableId(receivableId);
    }

    @PostMapping("/statistics")
    @ApiOperation("查询核销统计")
    public MyJsonBean getWriteOffStatistics(@RequestBody ArWriteOffQueryParam param) {
        return arWriteOffService.getWriteOffStatistics(param);
    }

    @PostMapping("/export")
    @ApiOperation("导出核销记录")
    public MyJsonBean exportWriteOffs(@RequestBody ArWriteOffQueryParam param) {
        return arWriteOffService.exportWriteOffs(param);
    }
}

