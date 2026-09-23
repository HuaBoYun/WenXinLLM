package com.management.accountant.service;

import com.management.accountant.oracle.entity.budget.BudgetMultiCurrency;
import com.management.accountant.util.PageResult;

import java.util.Map;

/**
 * 预算多币种管理Service接口
 *
 * @description 预算多币种管理业务接口
 * @author AI Assistant
 * @date 2025-01-04
 */
public interface BudgetMultiCurrencyService {

    /**
     * 创建币种配置
     *
     * @param currency 币种对象
     * @return 创建后的币种对象
     */
    BudgetMultiCurrency create(BudgetMultiCurrency currency);

    /**
     * 更新币种配置
     *
     * @param currency 币种对象
     */
    void update(BudgetMultiCurrency currency);

    /**
     * 删除币种配置
     *
     * @param currencyId 币种ID
     */
    void delete(String currencyId);

    /**
     * 分页查询币种配置
     *
     * @param params 查询参数
     * @return 分页结果
     */
    PageResult<BudgetMultiCurrency> getPage(Map<String, Object> params);

    /**
     * 更新汇率
     *
     * @param params 汇率参数
     */
    void updateExchangeRate(Map<String, Object> params);

    /**
     * 币种转换
     *
     * @param params 转换参数
     * @return 转换结果
     */
    Map<String, Object> convertCurrency(Map<String, Object> params);

    /**
     * 获取汇率历史
     *
     * @param params 查询参数
     * @return 汇率历史
     */
    PageResult<Map<String, Object>> getExchangeRateHistory(Map<String, Object> params);

    /**
     * 批量币种转换
     *
     * @param params 批量参数
     * @return 批量结果
     */
    Map<String, Object> batchConvert(Map<String, Object> params);

    /**
     * 获取币种列表
     *
     * @param params 查询参数
     * @return 币种列表
     */
    Map<String, Object> getCurrencyList(Map<String, Object> params);

    /**
     * 获取币种统计数据
     *
     * @param params 查询参数
     * @return 统计数据
     */
    Map<String, Object> getCurrencyStats(Map<String, Object> params);

    /**
     * 获取汇率历史（简化版）
     *
     * @param params 查询参数
     * @return 汇率历史
     */
    Map<String, Object> getRateHistory(Map<String, Object> params);

    /**
     * 启用币种
     *
     * @param currencyId 币种ID
     */
    void enableCurrency(String currencyId);

    /**
     * 禁用币种
     *
     * @param currencyId 币种ID
     */
    void disableCurrency(String currencyId);

    /**
     * 设置基准币种
     *
     * @param currencyId 币种ID
     */
    void setBaseCurrency(String currencyId);

    /**
     * 获取币种详情
     *
     * @param currencyId 币种ID
     * @return 币种详情
     */
    BudgetMultiCurrency getDetail(String currencyId);
}

