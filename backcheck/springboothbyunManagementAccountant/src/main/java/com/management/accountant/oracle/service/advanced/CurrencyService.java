package com.management.accountant.oracle.service.advanced;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.oracle.entity.advanced.CurrencyConfig;
import com.management.accountant.oracle.entity.advanced.ExchangeRate;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 币种管理Service接口
 * 
 * @author AI Agent
 * @date 2025-12-31
 */
public interface CurrencyService {

    // ========== 币种配置相关 ==========
    
    /**
     * 查询币种配置列表
     */
    List<CurrencyConfig> selectConfigList(Map<String, Object> params);

    /**
     * 分页查询币种配置列表
     */
    Page<CurrencyConfig> selectConfigPage(Map<String, Object> params, Integer pageNum, Integer pageSize);

    /**
     * 根据ID查询币种配置
     */
    CurrencyConfig selectConfigById(String configId);

    /**
     * 新增币种配置
     */
    boolean insertConfig(CurrencyConfig config);

    /**
     * 修改币种配置
     */
    boolean updateConfig(CurrencyConfig config);

    /**
     * 删除币种配置
     */
    boolean deleteConfigById(String configId);

    /**
     * 根据币种代码查询配置
     */
    CurrencyConfig selectConfigByCode(String currencyCode);

    // ========== 汇率相关 ==========
    
    /**
     * 查询汇率列表
     */
    List<ExchangeRate> selectRateList(Map<String, Object> params);

    /**
     * 分页查询汇率列表
     */
    Page<ExchangeRate> selectRatePage(Map<String, Object> params, Integer pageNum, Integer pageSize);

    /**
     * 根据ID查询汇率
     */
    ExchangeRate selectRateById(String rateId);

    /**
     * 新增汇率
     */
    boolean insertRate(ExchangeRate rate);

    /**
     * 修改汇率
     */
    boolean updateRate(ExchangeRate rate);

    /**
     * 删除汇率
     */
    boolean deleteRateById(String rateId);

    /**
     * 获取最新汇率
     */
    ExchangeRate getLatestRate(String fromCurrency, String toCurrency);

    /**
     * 币种转换
     */
    Map<String, Object> convertCurrency(BigDecimal amount, String fromCurrency, String toCurrency, String conversionDate);
}

