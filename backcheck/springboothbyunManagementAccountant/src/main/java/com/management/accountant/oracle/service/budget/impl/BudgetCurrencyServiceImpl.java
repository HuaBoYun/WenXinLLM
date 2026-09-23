package com.management.accountant.oracle.service.budget.impl;

import com.management.accountant.oracle.entity.budget.BudgetMultiCurrency;
import com.management.accountant.oracle.service.budget.BudgetCurrencyService;
import com.management.accountant.service.BudgetMultiCurrencyService;
import com.management.accountant.util.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Currency Service实现类 - Oracle包
 *
 * @description Currency业务逻辑实现，委托给base包的Service
 * @author AI Assistant
 * @date 2026-02-04
 */
@Service("budgetCurrencyServiceOracle")
@Slf4j
public class BudgetCurrencyServiceImpl implements BudgetCurrencyService {

    @Resource
    private BudgetMultiCurrencyService multiCurrencyService;

    @Override
    public Map<String, Object> createCurrencyConfig(Map<String, Object> params, String companyId, String userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 转换Map为实体
            BudgetMultiCurrency currency = new BudgetMultiCurrency();
            if (params.get("currencyCode") != null) {
                currency.setCurrencyCode((String) params.get("currencyCode"));
            }
            if (params.get("currencyName") != null) {
                currency.setCurrencyName((String) params.get("currencyName"));
            }
            if (params.get("fromCurrency") != null) {
                currency.setFromCurrency((String) params.get("fromCurrency"));
            }
            if (params.get("toCurrency") != null) {
                currency.setToCurrency((String) params.get("toCurrency"));
            }
            if (params.get("exchangeRate") != null) {
                currency.setExchangeRate(Double.parseDouble(params.get("exchangeRate").toString()));
            }

            // 调用基础Service
            BudgetMultiCurrency created = multiCurrencyService.create(currency);
            result.put("success", true);
            result.put("message", "创建币种配置成功");
            result.put("data", created);
        } catch (Exception e) {
            log.error("创建币种配置失败", e);
            result.put("success", false);
            result.put("message", "创建币种配置失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public Map<String, Object> getCurrencyConfig(Map<String, Object> params, String companyId, String userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 调用基础Service
            PageResult<BudgetMultiCurrency> pageResult = multiCurrencyService.getPage(params);
            result.put("success", true);
            result.put("message", "查询币种配置成功");
            result.put("data", pageResult);
        } catch (Exception e) {
            log.error("查询币种配置失败", e);
            result.put("success", false);
            result.put("message", "查询币种配置失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public Map<String, Object> createExchangeRate(Map<String, Object> params, String companyId, String userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 调用基础Service更新汇率
            multiCurrencyService.updateExchangeRate(params);
            result.put("success", true);
            result.put("message", "创建汇率成功");
            result.put("data", params);
        } catch (Exception e) {
            log.error("创建汇率失败", e);
            result.put("success", false);
            result.put("message", "创建汇率失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public Map<String, Object> getExchangeRatePage(Map<String, Object> params, String companyId, String userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 调用基础Service获取汇率历史
            PageResult<Map<String, Object>> pageResult = multiCurrencyService.getExchangeRateHistory(params);
            result.put("success", true);
            result.put("message", "查询汇率历史成功");
            result.put("data", pageResult);
        } catch (Exception e) {
            log.error("查询汇率历史失败", e);
            result.put("success", false);
            result.put("message", "查询汇率历史失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public Map<String, Object> getLatestExchangeRate(String companyId, String userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("pageNum", 1);
            params.put("pageSize", 1);
            PageResult<Map<String, Object>> pageResult = multiCurrencyService.getExchangeRateHistory(params);
            result.put("success", true);
            result.put("message", "获取最新汇率成功");
            result.put("data", pageResult.getTlist().isEmpty() ? null : pageResult.getTlist().get(0));
        } catch (Exception e) {
            log.error("获取最新汇率失败", e);
            result.put("success", false);
            result.put("message", "获取最新汇率失败: " + e.getMessage());
        }
        return result;
    }

    @Override
    public Map<String, Object> convertCurrency(Map<String, Object> params, String companyId, String userId) {
        Map<String, Object> result = new HashMap<>();
        try {
            // 调用基础Service进行币种转换
            Map<String, Object> convertResult = multiCurrencyService.convertCurrency(params);
            result.put("success", true);
            result.put("message", "币种转换成功");
            result.put("data", convertResult);
        } catch (Exception e) {
            log.error("币种转换失败", e);
            result.put("success", false);
            result.put("message", "币种转换失败: " + e.getMessage());
        }
        return result;
    }
}
