package com.financial.sharing.controller;

import com.hbfk.util.JsonBean;
import com.hbfk.util.user.UserProvider;
import com.hbfk.entity.TblStaffUtil;
import com.vip.vjtools.vjkit.mapper.JsonMapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

/**
 * 多币种管理控制器
 *
 * @author Financial Sharing System
 * @since 2024-12-19
 */
@Slf4j
@Api(tags = "多币种管理")
@RestController
@RequestMapping("/currency")
@CrossOrigin
public class CurrencyController {

    @Resource
    private UserProvider userProvider;

    @ApiOperation("查询币种列表")
    @GetMapping
    public String getCurrencyList(HttpServletRequest request,
                                 HttpServletResponse response,
                                 @RequestParam(required = false) String currencyCode,
                                 @RequestParam(required = false) String currencyName,
                                 @RequestParam(required = false) String status) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean();
                json.setCode(401);
                json.setMsg("用户已失效");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.nonNullMapper().toJson(json));
                return null;
            }

            // 模拟数据
            List<Map<String, Object>> currencies = new ArrayList<>();

            Map<String, Object> currency1 = new HashMap<>();
            currency1.put("currencyId", "CURRENCY001");
            currency1.put("currencyCode", "CNY");
            currency1.put("currencyName", "人民币");
            currency1.put("currencySymbol", "¥");
            currency1.put("exchangeRate", 1.0000);
            currency1.put("isBaseCurrency", true);
            currency1.put("decimalPlaces", 2);
            currency1.put("status", "ACTIVE");
            currency1.put("statusName", "启用");
            currency1.put("lastUpdateTime", "2024-12-19T10:00:00.000Z");

            Map<String, Object> currency2 = new HashMap<>();
            currency2.put("currencyId", "CURRENCY002");
            currency2.put("currencyCode", "USD");
            currency2.put("currencyName", "美元");
            currency2.put("currencySymbol", "$");
            currency2.put("exchangeRate", 7.2500);
            currency2.put("isBaseCurrency", false);
            currency2.put("decimalPlaces", 2);
            currency2.put("status", "ACTIVE");
            currency2.put("statusName", "启用");
            currency2.put("lastUpdateTime", "2024-12-19T10:05:00.000Z");

            Map<String, Object> currency3 = new HashMap<>();
            currency3.put("currencyId", "CURRENCY003");
            currency3.put("currencyCode", "EUR");
            currency3.put("currencyName", "欧元");
            currency3.put("currencySymbol", "€");
            currency3.put("exchangeRate", 7.8500);
            currency3.put("isBaseCurrency", false);
            currency3.put("decimalPlaces", 2);
            currency3.put("status", "INACTIVE");
            currency3.put("statusName", "禁用");
            currency3.put("lastUpdateTime", "2024-12-19T10:03:00.000Z");

            currencies.add(currency1);
            currencies.add(currency2);
            currencies.add(currency3);

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("查询成功");
            json.setData(currencies);
            return JsonMapper.nonNullMapper().toJson(json);
        } catch (Exception e) {
            log.error("查询币种列表失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("根据ID查询币种详情")
    @GetMapping("/{currencyId}")
    public String getCurrencyById(HttpServletRequest request,
                                 HttpServletResponse response,
                                 @PathVariable String currencyId) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean();
                json.setCode(401);
                json.setMsg("用户已失效");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.nonNullMapper().toJson(json));
                return null;
            }

            Map<String, Object> currency = new HashMap<>();
            currency.put("currencyId", currencyId);
            currency.put("currencyCode", "CNY");
            currency.put("currencyName", "人民币");
            currency.put("currencySymbol", "¥");
            currency.put("exchangeRate", 1.0000);
            currency.put("isBaseCurrency", true);
            currency.put("decimalPlaces", 2);
            currency.put("status", "ACTIVE");
            currency.put("statusName", "启用");
            currency.put("description", "中华人民共和国法定货币，为本系统的基本核算货币");
            currency.put("country", "中国");
            currency.put("createTime", "2024-01-01T00:00:00.000Z");
            currency.put("updateTime", "2024-12-19T10:00:00.000Z");

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("查询成功");
            json.setData(currency);
            return JsonMapper.nonNullMapper().toJson(json);
        } catch (Exception e) {
            log.error("查询币种详情失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    @ApiOperation("更新汇率")
    @PostMapping("/update-rate")
    public String updateExchangeRate(HttpServletRequest request,
                                   HttpServletResponse response,
                                   @RequestBody Map<String, Object> rateData) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean();
                json.setCode(401);
                json.setMsg("用户已失效");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.nonNullMapper().toJson(json));
                return null;
            }

            String currencyId = (String) rateData.get("currencyId");
            BigDecimal newRate = new BigDecimal(rateData.get("exchangeRate").toString());

            rateData.put("updateTime", new Date());
            rateData.put("updateBy", loginStaff.getUsername());

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("汇率更新成功");
            json.setData(rateData);
            return JsonMapper.nonNullMapper().toJson(json);
        } catch (Exception e) {
            log.error("更新汇率失败", e);
            return createErrorResponse("更新失败: " + e.getMessage());
        }
    }

    @ApiOperation("获取汇率历史")
    @GetMapping("/{currencyId}/rate-history")
    public String getRateHistory(HttpServletRequest request,
                                HttpServletResponse response,
                                @PathVariable String currencyId) {
        try {
            // 权限验证
            TblStaffUtil loginStaff = userProvider.get();
            if (loginStaff == null || loginStaff.getLinkDetp() == null || loginStaff.getCurrentOrg() == null) {
                JsonBean json = new JsonBean();
                json.setCode(401);
                json.setMsg("用户已失效");
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-Type", "application/json;charset=UTF-8");
                response.getWriter().write(JsonMapper.nonNullMapper().toJson(json));
                return null;
            }

            List<Map<String, Object>> history = new ArrayList<>();

            // 模拟历史数据
            for (int i = 0; i < 10; i++) {
                Map<String, Object> rate = new HashMap<>();
                rate.put("date", "2024-12-" + String.format("%02d", 19 - i));
                rate.put("exchangeRate", 7.2500 + (i * 0.01));
                rate.put("change", i % 3 == 0 ? "↑" : "↓");
                rate.put("changePercent", 0.1 + (i * 0.05));
                history.add(rate);
            }

            JsonBean json = new JsonBean();
            json.setCode(1);
            json.setMsg("查询成功");
            json.setData(history);
            return JsonMapper.nonNullMapper().toJson(json);
        } catch (Exception e) {
            log.error("查询汇率历史失败", e);
            return createErrorResponse("查询失败: " + e.getMessage());
        }
    }

    /**
     * 创建错误响应
     */
    private String createErrorResponse(String message) {
        JsonBean json = new JsonBean();
        json.setCode(0);
        json.setMsg(message);
        return JsonMapper.nonNullMapper().toJson(json);
    }
}