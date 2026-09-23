package com.financial.sharing.controller;

import com.financial.sharing.dto.param.PaymentOrderQueryParam;
import com.financial.sharing.dto.param.PaymentOrderSaveParam;
import com.financial.sharing.dto.param.WriteOffParam;
import com.financial.sharing.service.PaymentOrderService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.result.PaymentOrderVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 付款单Controller
 * @author system
 * @since 2025-01-05
 */
@Api(tags = "付款单管理")
@RestController
@RequestMapping("/ap/payment/order")
public class PaymentOrderController {

    @Resource
    private PaymentOrderService paymentOrderService;

    @ApiOperation("分页查询付款单")
    @PostMapping("/page")
    public MyJsonBean<PageResult<PaymentOrderVO>> queryPage(@RequestBody PaymentOrderQueryParam param) {
        PageResult<PaymentOrderVO> result = paymentOrderService.queryPage(param);
        return MyJsonBean.successData(result);
    }

    @ApiOperation("查询付款单详情")
    @GetMapping("/detail/{paymentId}")
    public MyJsonBean<PaymentOrderVO> getDetail(
            @ApiParam("付款ID") @PathVariable String paymentId) {
        PaymentOrderVO result = paymentOrderService.getDetail(paymentId);
        return MyJsonBean.successData(result);
    }

    @ApiOperation("保存付款单")
    @PostMapping("/save")
    public MyJsonBean<String> save(@Validated @RequestBody PaymentOrderSaveParam param) {
        String paymentId = paymentOrderService.save(param);
        return MyJsonBean.successData("保存成功", paymentId);
    }

    @ApiOperation("删除付款单")
    @DeleteMapping("/delete/{paymentId}")
    public MyJsonBean<Void> delete(
            @ApiParam("付款ID") @PathVariable String paymentId) {
        paymentOrderService.delete(paymentId);
        return MyJsonBean.successData();
    }

    @ApiOperation("确认付款")
    @PostMapping("/confirm/{paymentId}")
    public MyJsonBean<Void> confirmPayment(
            @ApiParam("付款ID") @PathVariable String paymentId) {
        paymentOrderService.confirmPayment(paymentId);
        return MyJsonBean.successData();
    }

    @ApiOperation("核销付款")
    @PostMapping("/writeoff")
    public MyJsonBean<Void> writeOff(@Validated @RequestBody WriteOffParam param) {
        paymentOrderService.writeOff(param);
        return MyJsonBean.successData();
    }

    @ApiOperation("查询供应商付款单")
    @GetMapping("/supplier/{supplierId}")
    public MyJsonBean<List<PaymentOrderVO>> queryBySupplier(
            @ApiParam("供应商ID") @PathVariable String supplierId) {
        List<PaymentOrderVO> result = paymentOrderService.queryBySupplier(supplierId);
        return MyJsonBean.successData(result);
    }
}

