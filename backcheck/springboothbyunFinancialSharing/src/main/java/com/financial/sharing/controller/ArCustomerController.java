package com.financial.sharing.controller;

import com.financial.sharing.service.ArCustomerService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.ArCustomerQueryParam;
import com.financial.sharing.vo.param.ArCustomerSaveParam;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * 客户档案管理控制器
 * @author system
 * @since 2026-01-04
 */
@RestController
@RequestMapping("/ar/customer")
@Api(tags = "应收管理-客户档案")
@CrossOrigin
public class ArCustomerController extends BaseController {

    @Resource
    private ArCustomerService arCustomerService;

    @PostMapping("/page")
    @ApiOperation("分页查询客户档案列表")
    public MyJsonBean<PageResult> getCustomerPage(@RequestBody ArCustomerQueryParam param) {
        return arCustomerService.getCustomerList(param);
    }

    @GetMapping("/{customerId}")
    @ApiOperation("根据ID查询客户详情")
    public MyJsonBean getCustomerById(@PathVariable @ApiParam("客户ID") String customerId) {
        return arCustomerService.getCustomerById(customerId);
    }

    @GetMapping("/code/{customerCode}")
    @ApiOperation("根据客户编码查询客户")
    public MyJsonBean getCustomerByCode(@PathVariable @ApiParam("客户编码") String customerCode,
                                        @RequestParam @ApiParam("租户ID") Long tenantId) {
        return arCustomerService.getCustomerByCode(customerCode, tenantId);
    }

    @PostMapping("/saveOrUpdate")
    @ApiOperation("保存或更新客户档案")
    public MyJsonBean saveOrUpdateCustomer(@RequestBody ArCustomerSaveParam param) {
        return arCustomerService.saveOrUpdateCustomer(param);
    }

    @DeleteMapping("/{customerId}")
    @ApiOperation("删除客户档案")
    public MyJsonBean deleteCustomer(@PathVariable @ApiParam("客户ID") String customerId) {
        return arCustomerService.deleteCustomer(customerId);
    }

    @PostMapping("/batchDelete")
    @ApiOperation("批量删除客户档案")
    public MyJsonBean batchDeleteCustomer(@RequestBody List<String> customerIds) {
        return arCustomerService.batchDeleteCustomer(customerIds);
    }

    @PostMapping("/batchUpdateStatus")
    @ApiOperation("批量更新客户状态")
    public MyJsonBean batchUpdateStatus(@RequestBody Map<String, Object> params) {
        @SuppressWarnings("unchecked")
        List<String> customerIds = (List<String>) params.get("customerIds");
        Integer status = (Integer) params.get("status");
        return arCustomerService.batchUpdateStatus(customerIds, status);
    }

    @GetMapping("/{customerId}/receivableStats")
    @ApiOperation("查询客户应收统计")
    public MyJsonBean getCustomerReceivableStats(@PathVariable @ApiParam("客户ID") String customerId) {
        return arCustomerService.getCustomerReceivableStats(customerId);
    }

    @GetMapping("/checkCode")
    @ApiOperation("检查客户编码是否存在")
    public MyJsonBean checkCustomerCodeExists(@RequestParam @ApiParam("客户编码") String customerCode,
                                              @RequestParam(required = false) @ApiParam("客户ID") String customerId,
                                              @RequestParam @ApiParam("租户ID") Long tenantId) {
        return arCustomerService.checkCustomerCodeExists(customerCode, customerId, tenantId);
    }

    @PostMapping("/export")
    @ApiOperation("导出客户档案")
    public MyJsonBean exportCustomers(@RequestBody ArCustomerQueryParam param) {
        return arCustomerService.exportCustomers(param);
    }
}

