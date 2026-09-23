package com.financial.sharing.controller;

import com.financial.sharing.dto.param.PayableDocumentQueryParam;
import com.financial.sharing.dto.param.PayableDocumentSaveParam;
import com.financial.sharing.service.PayableDocumentService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.result.PayableDocumentVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 应付单据Controller
 * @author system
 * @since 2025-01-05
 */
@Api(tags = "应付单据管理")
@RestController
@RequestMapping("/ap/payable/document")
public class PayableDocumentController {

    @Resource
    private PayableDocumentService payableDocumentService;

    @ApiOperation("分页查询应付单据")
    @PostMapping("/page")
    public MyJsonBean<PageResult<PayableDocumentVO>> queryPage(@RequestBody PayableDocumentQueryParam param) {
        PageResult<PayableDocumentVO> result = payableDocumentService.queryPage(param);
        return MyJsonBean.successData(result);
    }

    @ApiOperation("查询应付单据详情")
    @GetMapping("/detail/{documentId}")
    public MyJsonBean<PayableDocumentVO> getDetail(
            @ApiParam("单据ID") @PathVariable String documentId) {
        PayableDocumentVO result = payableDocumentService.getDetail(documentId);
        return MyJsonBean.successData(result);
    }

    @ApiOperation("保存应付单据")
    @PostMapping("/save")
    public MyJsonBean<String> save(@Validated @RequestBody PayableDocumentSaveParam param) {
        String documentId = payableDocumentService.save(param);
        return MyJsonBean.successData("保存成功", documentId);
    }

    @ApiOperation("删除应付单据")
    @DeleteMapping("/delete/{documentId}")
    public MyJsonBean<Void> delete(
            @ApiParam("单据ID") @PathVariable String documentId) {
        payableDocumentService.delete(documentId);
        return MyJsonBean.successData();
    }

    @ApiOperation("审核应付单据")
    @PostMapping("/audit/{documentId}")
    public MyJsonBean<Void> audit(
            @ApiParam("单据ID") @PathVariable String documentId,
            @ApiParam("是否通过") @RequestParam boolean approved,
            @ApiParam("审核意见") @RequestParam(required = false) String comments) {
        payableDocumentService.audit(documentId, approved, comments);
        return MyJsonBean.successData();
    }

    @ApiOperation("查询逾期单据")
    @GetMapping("/overdue")
    public MyJsonBean<List<PayableDocumentVO>> queryOverdueList() {
        List<PayableDocumentVO> result = payableDocumentService.queryOverdueList();
        return MyJsonBean.successData(result);
    }

    @ApiOperation("查询供应商应付单据")
    @GetMapping("/supplier/{supplierId}")
    public MyJsonBean<List<PayableDocumentVO>> queryBySupplier(
            @ApiParam("供应商ID") @PathVariable String supplierId) {
        List<PayableDocumentVO> result = payableDocumentService.queryBySupplier(supplierId);
        return MyJsonBean.successData(result);
    }
}

