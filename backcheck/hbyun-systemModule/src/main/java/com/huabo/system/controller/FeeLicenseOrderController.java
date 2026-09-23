package com.huabo.system.controller;

import java.math.BigDecimal;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.hbfk.util.JsonBean;
import com.huabo.system.service.FeeLicenseOrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

/**
 * 密钥购买管理控制器
 * <p>提供许可证密钥的购买订单管理接口</p>
 *
 * @author hbyun
 */
@Slf4j
@Tag(name = "密钥购买管理")
@RestController
@RequestMapping(value = "/system/fee/order")
public class FeeLicenseOrderController {

    @Resource
    private FeeLicenseOrderService orderService;

    @PostMapping(value = "/submit", produces = "application/json; charset=utf-8")
    @Operation(summary = "提交购买申请")
    public JsonBean submit(HttpServletRequest request,
            @RequestHeader("token") String token,
            @RequestBody java.util.Map<String, Object> body) {
        try {
            BigDecimal amount = new BigDecimal(body.get("purchaseAmount").toString());
            return orderService.submitOrder(token, amount);
        } catch (Exception e) {
            log.error("提交购买申请失败", e);
            return new JsonBean(0, "提交失败: " + e.getMessage(), null);
        }
    }

    @GetMapping(value = "/list", produces = "application/json; charset=utf-8")
    @Operation(summary = "购买记录列表")
    public JsonBean list(HttpServletRequest request,
            @RequestHeader("token") String token,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "startTime", required = false) String startTime,
            @RequestParam(value = "endTime", required = false) String endTime,
            @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize) {
        try {
            return orderService.getOrderList(token, status, startTime, endTime, pageNum, pageSize);
        } catch (Exception e) {
            log.error("查询购买记录失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null);
        }
    }

    @GetMapping(value = "/detail", produces = "application/json; charset=utf-8")
    @Operation(summary = "订单详情")
    public JsonBean detail(HttpServletRequest request,
            @RequestHeader("token") String token,
            @RequestParam("orderId") BigDecimal orderId) {
        try {
            return orderService.getOrderDetail(token, orderId);
        } catch (Exception e) {
            log.error("查询订单详情失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null);
        }
    }

    @PostMapping(value = "/approve", produces = "application/json; charset=utf-8")
    @Operation(summary = "审批订单")
    public JsonBean approve(HttpServletRequest request,
            @RequestHeader("token") String token,
            @RequestBody java.util.Map<String, Object> body) {
        try {
            BigDecimal orderId = new BigDecimal(body.get("orderId").toString());
            boolean approved = Boolean.parseBoolean(body.get("approved").toString());
            String remark = body.get("remark") != null ? body.get("remark").toString() : "";
            return orderService.approveOrder(token, orderId, approved, remark);
        } catch (Exception e) {
            log.error("审批失败", e);
            return new JsonBean(0, "审批失败: " + e.getMessage(), null);
        }
    }

    @PostMapping(value = "/generateKey", produces = "application/json; charset=utf-8")
    @Operation(summary = "生成密钥")
    public JsonBean generateKey(HttpServletRequest request,
            @RequestHeader("token") String token,
            @RequestBody java.util.Map<String, Object> body) {
        try {
            BigDecimal orderId = new BigDecimal(body.get("orderId").toString());
            return orderService.generateKey(token, orderId);
        } catch (Exception e) {
            log.error("生成密钥失败", e);
            return new JsonBean(0, "生成失败: " + e.getMessage(), null);
        }
    }

    @GetMapping(value = "/export", produces = "application/octet-stream")
    @Operation(summary = "导出购买记录")
    public void export(HttpServletRequest request, HttpServletResponse response,
            @RequestHeader("token") String token,
            @RequestParam(value = "status", required = false) Integer status,
            @RequestParam(value = "startTime", required = false) String startTime,
            @RequestParam(value = "endTime", required = false) String endTime) {
        try {
            orderService.exportOrders(token, status, startTime, endTime, response);
        } catch (Exception e) {
            log.error("导出失败", e);
        }
    }
}
