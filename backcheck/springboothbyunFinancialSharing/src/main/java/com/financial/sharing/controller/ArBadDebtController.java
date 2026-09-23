package com.financial.sharing.controller;

import com.financial.sharing.service.ArBadDebtService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 坏账管理控制器
 * @author system
 * @since 2026-01-04
 */
@RestController
@RequestMapping("/ar/badDebt")
@Api(tags = "应收管理-坏账管理")
@CrossOrigin
public class ArBadDebtController extends BaseController {

    @Resource
    private ArBadDebtService arBadDebtService;

    // ==================== 坏账准备 ====================

    @PostMapping("/provision/page")
    @ApiOperation("分页查询坏账准备列表")
    public MyJsonBean<PageResult> getProvisionPage(@RequestBody ArBadDebtProvisionQueryParam param) {
        return arBadDebtService.getProvisionList(param);
    }

    @GetMapping("/provision/{provisionId}")
    @ApiOperation("根据ID查询坏账准备详情")
    public MyJsonBean getProvisionById(@PathVariable @ApiParam("坏账准备ID") String provisionId) {
        return arBadDebtService.getProvisionById(provisionId);
    }

    @PostMapping("/provision/create")
    @ApiOperation("计提坏账准备")
    public MyJsonBean createProvision(@RequestBody ArBadDebtProvisionSaveParam param) {
        return arBadDebtService.createProvision(param);
    }

    @PostMapping("/provision/batchCreate")
    @ApiOperation("批量计提坏账准备")
    public MyJsonBean batchCreateProvision(@RequestBody List<ArBadDebtProvisionSaveParam> params) {
        return arBadDebtService.batchCreateProvision(params);
    }

    @PostMapping("/provision/reverse/{provisionId}")
    @ApiOperation("冲回坏账准备")
    public MyJsonBean reverseProvision(@PathVariable @ApiParam("坏账准备ID") String provisionId,
                                       @RequestBody Map<String, String> operatorInfo) {
        String operatorId = operatorInfo.get("operatorId");
        return arBadDebtService.reverseProvision(provisionId, operatorId);
    }

    @GetMapping("/provision/balance")
    @ApiOperation("查询坏账准备余额")
    public MyJsonBean getProvisionBalance(@RequestParam @ApiParam("租户ID") Long tenantId,
                                          @RequestParam @ApiParam("截止日期") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate asOfDate) {
        return arBadDebtService.getProvisionBalance(tenantId, asOfDate);
    }

    @PostMapping("/provision/statistics")
    @ApiOperation("查询坏账准备统计")
    public MyJsonBean getProvisionStatistics(@RequestBody ArBadDebtProvisionQueryParam param) {
        return arBadDebtService.getProvisionStatistics(param);
    }

    // ==================== 坏账核销 ====================

    @PostMapping("/writeOff/page")
    @ApiOperation("分页查询坏账核销列表")
    public MyJsonBean<PageResult> getBadDebtWriteOffPage(@RequestBody ArBadDebtWriteOffQueryParam param) {
        return arBadDebtService.getBadDebtWriteOffList(param);
    }

    @GetMapping("/writeOff/{writeOffId}")
    @ApiOperation("根据ID查询坏账核销详情")
    public MyJsonBean getBadDebtWriteOffById(@PathVariable @ApiParam("坏账核销ID") String writeOffId) {
        return arBadDebtService.getBadDebtWriteOffById(writeOffId);
    }

    @PostMapping("/writeOff/apply")
    @ApiOperation("申请坏账核销")
    public MyJsonBean applyBadDebtWriteOff(@RequestBody ArBadDebtWriteOffSaveParam param) {
        return arBadDebtService.applyBadDebtWriteOff(param);
    }

    @PostMapping("/writeOff/audit/{writeOffId}")
    @ApiOperation("审核坏账核销")
    public MyJsonBean auditBadDebtWriteOff(@PathVariable @ApiParam("坏账核销ID") String writeOffId,
                                           @RequestBody Map<String, Object> auditInfo) {
        Integer status = (Integer) auditInfo.get("status");
        String auditorId = (String) auditInfo.get("auditorId");
        String auditComments = (String) auditInfo.get("auditComments");
        return arBadDebtService.auditBadDebtWriteOff(writeOffId, status, auditorId, auditComments);
    }

    @PostMapping("/writeOff/statistics")
    @ApiOperation("查询坏账核销统计")
    public MyJsonBean getBadDebtWriteOffStatistics(@RequestBody ArBadDebtWriteOffQueryParam param) {
        return arBadDebtService.getBadDebtWriteOffStatistics(param);
    }

    // ==================== 坏账回收 ====================

    @PostMapping("/recovery/page")
    @ApiOperation("分页查询坏账回收列表")
    public MyJsonBean<PageResult> getRecoveryPage(@RequestBody ArBadDebtRecoveryQueryParam param) {
        return arBadDebtService.getRecoveryList(param);
    }

    @GetMapping("/recovery/{recoveryId}")
    @ApiOperation("根据ID查询坏账回收详情")
    public MyJsonBean getRecoveryById(@PathVariable @ApiParam("坏账回收ID") String recoveryId) {
        return arBadDebtService.getRecoveryById(recoveryId);
    }

    @PostMapping("/recovery/create")
    @ApiOperation("登记坏账回收")
    public MyJsonBean createRecovery(@RequestBody ArBadDebtRecoverySaveParam param) {
        return arBadDebtService.createRecovery(param);
    }

    @PostMapping("/recovery/statistics")
    @ApiOperation("查询坏账回收统计")
    public MyJsonBean getRecoveryStatistics(@RequestBody ArBadDebtRecoveryQueryParam param) {
        return arBadDebtService.getRecoveryStatistics(param);
    }

    // ==================== 导出 ====================

    @PostMapping("/provision/export")
    @ApiOperation("导出坏账准备")
    public MyJsonBean exportProvisions(@RequestBody ArBadDebtProvisionQueryParam param) {
        return arBadDebtService.exportProvisions(param);
    }

    @PostMapping("/writeOff/export")
    @ApiOperation("导出坏账核销")
    public MyJsonBean exportBadDebtWriteOffs(@RequestBody ArBadDebtWriteOffQueryParam param) {
        return arBadDebtService.exportBadDebtWriteOffs(param);
    }

    @PostMapping("/recovery/export")
    @ApiOperation("导出坏账回收")
    public MyJsonBean exportRecoveries(@RequestBody ArBadDebtRecoveryQueryParam param) {
        return arBadDebtService.exportRecoveries(param);
    }
}

