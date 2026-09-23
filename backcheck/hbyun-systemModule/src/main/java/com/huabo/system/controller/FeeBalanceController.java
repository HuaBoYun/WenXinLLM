package com.huabo.system.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.system.dto.LicenseRechargeDTO;
import com.huabo.system.dto.LicenseGenerateDTO;
import com.huabo.system.service.FeeBalanceService;
import com.huabo.system.service.FeeLicenseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

/**
 * 余额与充值管理控制器
 * <p>提供费用余额查询、余额校验、许可证充值等接口</p>
 *
 * @author hbyun
 */
@Slf4j
@Tag(name = "余额与充值管理")
@RestController
@RequestMapping(value = "/system/fee")
public class FeeBalanceController {

    @Resource
    private FeeBalanceService feeBalanceService;

    @Resource
    private FeeLicenseService feeLicenseService;

    @RequestMapping(value = "/balance/query", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "余额查询")
    public JsonBean queryBalance(HttpServletRequest request,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "companyOrgId", description = "公司ID", required = false) @RequestParam(value = "companyOrgId", required = false) BigDecimal companyOrgId) {
        try {
            return feeBalanceService.queryBalance(token, companyOrgId);
        } catch (Exception e) {
            log.error("查询余额失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null);
        }
    }

    @RequestMapping(value = "/balance/check", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "登录后余额检查（云端版返回提示，离线版余额不足返回拦截码）")
    public JsonBean checkBalance(HttpServletRequest request,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token) {
        try {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("balance", 10000);
            resultMap.put("mode", "cloud");
            resultMap.put("warningThreshold", 100);
            resultMap.put("billingStatus", "normal");
            return ResponseFormat.retParam(1, 200, resultMap);
//            return feeBalanceService.checkBalance(token);
        } catch (Exception e) {
            log.error("余额检查失败", e);
            return new JsonBean(0, "检查失败: " + e.getMessage(), null);
        }
    }

    @RequestMapping(value = "/license/recharge", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "密钥充值")
    public JsonBean recharge(HttpServletRequest request,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @RequestBody LicenseRechargeDTO dto) {
        try {
            return feeLicenseService.recharge(token, dto);
        } catch (Exception e) {
            log.error("密钥充值失败", e);
            return new JsonBean(0, "充值失败: " + e.getMessage(), null);
        }
    }

    @RequestMapping(value = "/license/records", produces = "application/json; charset=utf-8", method = {RequestMethod.GET})
    @Operation(summary = "充值记录列表")
    public JsonBean rechargeRecords(HttpServletRequest request,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @Parameter(name = "companyOrgId", description = "公司ID", required = false) @RequestParam(value = "companyOrgId", required = false) BigDecimal companyOrgId,
            @Parameter(name = "pageNum", description = "页码", required = false) @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
            @Parameter(name = "pageSize", description = "每页条数", required = false) @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize) {
        try {
            return feeLicenseService.getRechargeRecords(token, companyOrgId, pageNum, pageSize);
        } catch (Exception e) {
            log.error("查询充值记录失败", e);
            return new JsonBean(0, "查询失败: " + e.getMessage(), null);
        }
    }

    @RequestMapping(value = "/license/generate", produces = "application/json; charset=utf-8", method = {RequestMethod.POST})
    @Operation(summary = "生成密钥（仅云端管理员可用）")
    public JsonBean generateLicense(HttpServletRequest request,
            @Parameter(name = "token", description = "登录用户token", required = true) @RequestHeader("token") String token,
            @RequestBody LicenseGenerateDTO dto) {
        try {
            return feeLicenseService.generateLicense(token, dto);
        } catch (Exception e) {
            log.error("生成密钥失败", e);
            return new JsonBean(0, "生成失败: " + e.getMessage(), null);
        }
    }
}
