package com.financial.sharing.service;

import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.CurrencyRateQueryParam;
import com.financial.sharing.vo.param.CurrencyRateSaveParam;
import com.financial.sharing.vo.result.CurrencyRateVO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 币种汇率服务接口
 * 
 * @author system
 * @since 2024-12-19
 */
public interface CurrencyRateService {

    /**
     * 分页查询币种汇率
     * 
     * @param param 查询参数
     * @return 分页结果
     */
    PageResult<CurrencyRateVO> getCurrencyRatePage(CurrencyRateQueryParam param);

    /**
     * 保存或更新币种汇率
     * 
     * @param param 保存参数
     * @return 保存结果
     */
    CurrencyRateVO saveOrUpdateCurrencyRate(CurrencyRateSaveParam param);

    /**
     * 根据ID查询币种汇率详情
     * 
     * @param rateId 汇率ID
     * @return 币种汇率详情
     */
    CurrencyRateVO getCurrencyRateById(Long rateId);

    /**
     * 删除币种汇率
     * 
     * @param rateId 汇率ID
     * @return 是否成功
     */
    boolean deleteCurrencyRate(Long rateId);

    /**
     * 批量删除币种汇率
     * 
     * @param rateIds 汇率ID列表
     * @return 是否成功
     */
    boolean batchDeleteCurrencyRates(List<Long> rateIds);

    /**
     * 启用/禁用币种汇率
     * 
     * @param rateId 汇率ID
     * @param isEnabled 启用状态
     * @return 是否成功
     */
    boolean updateCurrencyRateStatus(Long rateId, Integer isEnabled);

    /**
     * 批量启用/禁用币种汇率
     * 
     * @param rateIds 汇率ID列表
     * @param isEnabled 启用状态
     * @return 是否成功
     */
    boolean batchUpdateCurrencyRateStatus(List<Long> rateIds, Integer isEnabled);

    /**
     * 检查币种和日期是否存在
     * 
     * @param currencyCode 币种编码
     * @param rateDate 汇率日期
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @param excludeId 排除的ID
     * @return 是否存在
     */
    boolean checkCurrencyAndDateExists(String currencyCode, LocalDate rateDate, Long bookId, Long tenantId, Long excludeId);

    /**
     * 获取最新汇率
     * 
     * @param currencyCode 币种编码
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 最新汇率
     */
    CurrencyRateVO getLatestRate(String currencyCode, Long bookId, Long tenantId);

    /**
     * 根据日期范围查询汇率
     * 
     * @param currencyCode 币种编码
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 汇率列表
     */
    List<CurrencyRateVO> getCurrencyRatesByDateRange(String currencyCode, LocalDate startDate, LocalDate endDate, Long bookId, Long tenantId);

    /**
     * 获取所有启用的币种列表
     * 
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 币种列表
     */
    List<CurrencyRateVO> getEnabledCurrencies(Long bookId, Long tenantId);

    /**
     * 获取本位币信息
     * 
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 本位币信息
     */
    CurrencyRateVO getBaseCurrency(Long bookId, Long tenantId);

    /**
     * 设置本位币
     * 
     * @param rateId 汇率ID
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 是否成功
     */
    boolean setBaseCurrency(Long rateId, Long bookId, Long tenantId);

    /**
     * 获取币种编码列表
     * 
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 币种编码列表
     */
    List<String> getCurrencyCodes(Long bookId, Long tenantId);

    /**
     * 计算汇率转换
     * 
     * @param amount 金额
     * @param fromCurrency 源币种
     * @param toCurrency 目标币种
     * @param bookId 账簿ID
     * @param tenantId 租户ID
     * @return 转换后金额
     */
    BigDecimal convertCurrency(BigDecimal amount, String fromCurrency, String toCurrency, Long bookId, Long tenantId);
}
