package com.financial.sharing.controller;

import com.financial.sharing.dto.param.SupplierQueryParam;
import com.financial.sharing.dto.param.SupplierSaveParam;
import com.financial.sharing.service.SupplierService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.result.SupplierVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 供应商Controller(应付模块)
 * @author system
 * @since 2025-01-05
 */
@Api(tags = "供应商管理(应付)")
@RestController
@RequestMapping("/ap/supplier")
public class ApSupplierController {

    @Resource
    private SupplierService supplierService;

    @ApiOperation("分页查询供应商")
    @PostMapping("/page")
    public MyJsonBean<PageResult<SupplierVO>> queryPage(@RequestBody SupplierQueryParam param) {
        PageResult<SupplierVO> result = supplierService.queryPage(param);
        return MyJsonBean.successData(result);
    }

    @ApiOperation("查询供应商详情")
    @GetMapping("/detail/{supplierId}")
    public MyJsonBean<SupplierVO> getDetail(
            @ApiParam("供应商ID") @PathVariable String supplierId) {
        SupplierVO result = supplierService.getDetail(supplierId);
        return MyJsonBean.successData(result);
    }

    @ApiOperation("保存供应商")
    @PostMapping("/save")
    public MyJsonBean<String> save(@Validated @RequestBody SupplierSaveParam param) {
        String supplierId = supplierService.save(param);
        return MyJsonBean.successData("保存成功", supplierId);
    }

    @ApiOperation("删除供应商")
    @DeleteMapping("/delete/{supplierId}")
    public MyJsonBean<Void> delete(
            @ApiParam("供应商ID") @PathVariable String supplierId) {
        supplierService.delete(supplierId);
        return MyJsonBean.successData();
    }

    @ApiOperation("查询所有启用的供应商")
    @GetMapping("/enabled")
    public MyJsonBean<List<SupplierVO>> queryAllEnabled() {
        List<SupplierVO> result = supplierService.queryAllEnabled();
        return MyJsonBean.successData(result);
    }

    @ApiOperation("查询供应商下拉列表")
    @GetMapping("/dropdown")
    public MyJsonBean<List<SupplierVO>> queryDropdownList(
            @ApiParam("关键字") @RequestParam(required = false) String keyword) {
        List<SupplierVO> result = supplierService.queryDropdownList(keyword);
        return MyJsonBean.successData(result);
    }

    @ApiOperation("更新供应商状态")
    @PostMapping("/status/{supplierId}")
    public MyJsonBean<Void> updateStatus(
            @ApiParam("供应商ID") @PathVariable String supplierId,
            @ApiParam("状态") @RequestParam Integer status) {
        supplierService.updateStatus(supplierId, status);
        return MyJsonBean.successData();
    }
}

