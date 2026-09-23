package com.financial.sharing.controller;

import com.financial.sharing.service.ArReceivableService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.ArReceivableSaveParam;
import com.financial.sharing.vo.param.ArReceivableQueryParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 应收单据管理控制器
 * @author system
 * @since 2026-01-04
 */
@RestController
@RequestMapping("/ar/receivable")
@Api(tags = "应收管理-应收登记")
@CrossOrigin
public class ArReceivableController extends BaseController {

    @Resource
    private ArReceivableService arReceivableService;

    @PostMapping("/page")
    @ApiOperation("分页查询应收单据列表")
    public MyJsonBean<PageResult> getReceivablePage(@RequestBody ArReceivableQueryParam param) {
        return arReceivableService.getReceivableList(param);
    }

    @GetMapping("/{receivableId}")
    @ApiOperation("根据ID查询应收单据详情")
    public MyJsonBean getReceivableById(@PathVariable @ApiParam("应收单ID") String receivableId) {
        return arReceivableService.getReceivableById(receivableId);
    }

    @GetMapping("/documentNo/{documentNo}")
    @ApiOperation("根据单据编号查询应收单据")
    public MyJsonBean getReceivableByDocumentNo(@PathVariable @ApiParam("单据编号") String documentNo,
                                                @RequestParam @ApiParam("租户ID") Long tenantId) {
        return arReceivableService.getReceivableByDocumentNo(documentNo, tenantId);
    }

    @PostMapping("/saveOrUpdate")
    @ApiOperation("保存或更新应收单据")
    public MyJsonBean saveOrUpdateReceivable(@RequestBody ArReceivableSaveParam param) {
        return arReceivableService.saveOrUpdateReceivable(param);
    }

    @DeleteMapping("/{receivableId}")
    @ApiOperation("删除应收单据")
    public MyJsonBean deleteReceivable(@PathVariable @ApiParam("应收单ID") String receivableId) {
        return arReceivableService.deleteReceivable(receivableId);
    }

    @PostMapping("/batchDelete")
    @ApiOperation("批量删除应收单据")
    public MyJsonBean batchDeleteReceivable(@RequestBody List<String> receivableIds) {
        return arReceivableService.batchDeleteReceivable(receivableIds);
    }

    @PostMapping("/submit/{receivableId}")
    @ApiOperation("提交审核")
    public MyJsonBean submitForAudit(@PathVariable @ApiParam("应收单ID") String receivableId) {
        return arReceivableService.submitForAudit(receivableId);
    }

    @PostMapping("/batchSubmit")
    @ApiOperation("批量提交审核")
    public MyJsonBean batchSubmitForAudit(@RequestBody List<String> receivableIds) {
        return arReceivableService.batchSubmitForAudit(receivableIds);
    }

    @PostMapping("/approve/{receivableId}")
    @ApiOperation("审核通过")
    public MyJsonBean approveReceivable(@PathVariable @ApiParam("应收单ID") String receivableId,
                                        @RequestBody Map<String, String> auditInfo) {
        String auditorId = auditInfo.get("auditorId");
        String auditComments = auditInfo.get("auditComments");
        return arReceivableService.approveReceivable(receivableId, auditorId, auditComments);
    }

    @PostMapping("/reject/{receivableId}")
    @ApiOperation("审核驳回")
    public MyJsonBean rejectReceivable(@PathVariable @ApiParam("应收单ID") String receivableId,
                                       @RequestBody Map<String, String> auditInfo) {
        String auditorId = auditInfo.get("auditorId");
        String auditComments = auditInfo.get("auditComments");
        return arReceivableService.rejectReceivable(receivableId, auditorId, auditComments);
    }

    @PostMapping("/batchAudit")
    @ApiOperation("批量审核")
    public MyJsonBean batchAudit(@RequestBody Map<String, Object> auditInfo) {
        @SuppressWarnings("unchecked")
        List<String> receivableIds = (List<String>) auditInfo.get("receivableIds");
        Integer status = (Integer) auditInfo.get("status");
        String auditorId = (String) auditInfo.get("auditorId");
        String auditComments = (String) auditInfo.get("auditComments");
        return arReceivableService.batchAudit(receivableIds, status, auditorId, auditComments);
    }

    @GetMapping("/customer/{customerId}")
    @ApiOperation("查询客户的应收单据")
    public MyJsonBean getReceivablesByCustomer(@PathVariable @ApiParam("客户ID") String customerId,
                                               @RequestParam @ApiParam("租户ID") Long tenantId) {
        return arReceivableService.getReceivablesByCustomer(customerId, tenantId);
    }

    @GetMapping("/pendingWriteOff")
    @ApiOperation("查询待核销的应收单据")
    public MyJsonBean getPendingWriteOffReceivables(@RequestParam @ApiParam("客户ID") String customerId,
                                                    @RequestParam @ApiParam("租户ID") Long tenantId) {
        return arReceivableService.getPendingWriteOffReceivables(customerId, tenantId);
    }

    @PostMapping("/statistics")
    @ApiOperation("查询应收单据统计")
    public MyJsonBean getReceivableStatistics(@RequestBody ArReceivableQueryParam param) {
        return arReceivableService.getReceivableStatistics(param);
    }

    @PostMapping("/export")
    @ApiOperation("导出应收单据")
    public MyJsonBean exportReceivables(@RequestBody ArReceivableQueryParam param) {
        return arReceivableService.exportReceivables(param);
    }
}

