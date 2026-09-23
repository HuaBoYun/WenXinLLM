package com.financial.sharing.controller;

import com.financial.sharing.dto.param.BatchOperationParam;
import com.financial.sharing.dto.param.PaymentTermsQueryParam;
import com.financial.sharing.dto.param.PaymentTermsSaveParam;
import com.financial.sharing.service.PaymentTermsServiceAp;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.result.PaymentTermsVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 账期Controller
 * @author system
 * @since 2025-01-05
 */
@Api(tags = "账期管理")
@RestController
@RequestMapping("/payables/payment-terms")
public class PaymentTermsController {

    @Resource
    private PaymentTermsServiceAp paymentTermsServiceAp;

    @ApiOperation("分页查询账期")
    @PostMapping("/getList")
    public MyJsonBean<PageResult<PaymentTermsVO>> queryPage(PaymentTermsQueryParam param) {
        PageResult<PaymentTermsVO> result = paymentTermsServiceAp.queryPage(param);
        return MyJsonBean.successData(result);
    }

    @ApiOperation("查询账期详情")
    @GetMapping("/{termsId}")
    public MyJsonBean<PaymentTermsVO> getDetail(
            @ApiParam("账期ID") @PathVariable String termsId) {
        PaymentTermsVO result = paymentTermsServiceAp.getDetail(termsId);
        return MyJsonBean.successData(result);
    }

    @ApiOperation("保存或更新账期")
    @PostMapping("/saveOrUpdate")
    public MyJsonBean<String> saveOrUpdate(@Validated PaymentTermsSaveParam param) {
        String termsId = paymentTermsServiceAp.save(param);
        return MyJsonBean.successData("保存成功", termsId);
    }

    @ApiOperation("删除账期")
    @DeleteMapping("/{termsId}")
    public MyJsonBean<Void> delete(
            @ApiParam("账期ID") @PathVariable String termsId) {
        paymentTermsServiceAp.delete(termsId);
        return MyJsonBean.successData();
    }

    @ApiOperation("批量更新账期状态")
    @PostMapping("/batch/status")
    public MyJsonBean<Void> batchUpdateStatus(BatchOperationParam param) {
        paymentTermsServiceAp.batchUpdateStatus(param.getTermsIds(), param.getStatus());
        return MyJsonBean.successData();
    }

    @ApiOperation("批量删除账期")
    @PostMapping("/batch/delete")
    public MyJsonBean<Void> batchDelete(BatchOperationParam param) {
        paymentTermsServiceAp.batchDelete(param.getTermsIds());
        return MyJsonBean.successData();
    }

    @ApiOperation("查询供应商账期")
    @GetMapping("/supplier/{supplierId}")
    public MyJsonBean<List<PaymentTermsVO>> queryBySupplier(
            @ApiParam("供应商ID") @PathVariable String supplierId) {
        List<PaymentTermsVO> result = paymentTermsServiceAp.queryBySupplier(supplierId);
        return MyJsonBean.successData(result);
    }

    @ApiOperation("查询所有启用的账期")
    @GetMapping("/enabled")
    public MyJsonBean<List<PaymentTermsVO>> queryAllEnabled() {
        List<PaymentTermsVO> result = paymentTermsServiceAp.queryAllEnabled();
        return MyJsonBean.successData(result);
    }

    @ApiOperation("更新账期状态")
    @PostMapping("/status/{termsId}")
    public MyJsonBean<Void> updateStatus(
            @ApiParam("账期ID") @PathVariable String termsId,
            @ApiParam("状态") @RequestParam Integer status) {
        paymentTermsServiceAp.updateStatus(termsId, status);
        return MyJsonBean.successData();
    }

    @ApiOperation("计算到期日期")
    @GetMapping("/calculate-due-date")
    public MyJsonBean<String> calculateDueDate(
            @ApiParam("账期ID") @RequestParam String termsId,
            @ApiParam("基准日期") @RequestParam String baseDate) {
        String dueDate = paymentTermsServiceAp.calculateDueDate(termsId, baseDate);
        return MyJsonBean.successData(dueDate);
    }
}

