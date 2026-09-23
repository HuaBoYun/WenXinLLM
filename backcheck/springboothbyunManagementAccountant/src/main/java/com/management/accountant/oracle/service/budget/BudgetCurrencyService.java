package com.management.accountant.oracle.service.budget;

import java.util.Map;

/**
 * Currency Service接口
 * 
 * @description Currency业务逻辑接口
 * @author AI Assistant
 * @date 2026-02-04
 */
public interface BudgetCurrencyService {

    /**
     * createCurrencyConfig
     */
    Map<String, Object> createCurrencyConfig(Map<String, Object> params, String companyId, String userId);

    /**
     * getCurrencyConfig
     */
    Map<String, Object> getCurrencyConfig(Map<String, Object> params, String companyId, String userId);

    /**
     * createExchangeRate
     */
    Map<String, Object> createExchangeRate(Map<String, Object> params, String companyId, String userId);

    /**
     * getExchangeRatePage
     */
    Map<String, Object> getExchangeRatePage(Map<String, Object> params, String companyId, String userId);

    /**
     * getLatestExchangeRate
     */
    Map<String, Object> getLatestExchangeRate(String companyId, String userId);

    /**
     * convertCurrency
     */
    Map<String, Object> convertCurrency(Map<String, Object> params, String companyId, String userId);

}
