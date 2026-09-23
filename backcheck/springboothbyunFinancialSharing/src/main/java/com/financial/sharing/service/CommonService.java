package com.financial.sharing.service;

import com.financial.sharing.dto.CurrencyQueryParam;
import com.financial.sharing.dto.CurrencyRateQueryParam;
import com.financial.sharing.vo.result.CurrencyVO;
import com.financial.sharing.vo.result.CurrencyRateVO;
import com.github.pagehelper.PageInfo;
import com.hbfk.entity.TblStaffUtil;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 通用设置服务接口
 *
 * @author Financial Sharing System
 * @since 2024-12-08
 */
public interface CommonService {

    // ==================== 币种管理 ====================

    /**
     * 分页查询币种列表
     *
     * @param param 查询参数
     * @param loginStaff 登录用户信息
     * @return 分页结果
     */
    PageInfo<CurrencyVO> getCurrencyPage(CurrencyQueryParam param, TblStaffUtil loginStaff);

    /**
     * 获取所有启用的币种
     *
     * @param loginStaff 登录用户信息
     * @return 币种列表
     */
    List<CurrencyVO> getActiveCurrencyList(TblStaffUtil loginStaff);

    /**
     * 根据币种编码查询币种信息
     *
     * @param currencyCode 币种编码
     * @param loginStaff 登录用户信息
     * @return 币种信息
     */
    CurrencyVO getCurrencyByCode(String currencyCode, TblStaffUtil loginStaff);

    /**
     * 保存或更新币种
     *
     * @param currencyVO 币种信息
     * @param loginStaff 登录用户信息
     * @return 保存结果
     */
    CurrencyVO saveOrUpdateCurrency(CurrencyVO currencyVO, TblStaffUtil loginStaff);

    /**
     * 删除币种
     *
     * @param currencyId 币种ID
     * @param loginStaff 登录用户信息
     * @return 删除结果
     */
    boolean deleteCurrency(Long currencyId, TblStaffUtil loginStaff);

    /**
     * 启用/禁用币种
     *
     * @param currencyId 币种ID
     * @param enabled 是否启用
     * @param loginStaff 登录用户信息
     * @return 操作结果
     */
    boolean toggleCurrencyStatus(Long currencyId, Boolean enabled, TblStaffUtil loginStaff);

    // ==================== 汇率管理 ====================

    /**
     * 分页查询汇率列表
     *
     * @param param 查询参数
     * @param loginStaff 登录用户信息
     * @return 分页结果
     */
    PageInfo<CurrencyRateVO> getCurrencyRatePage(CurrencyRateQueryParam param, TblStaffUtil loginStaff);

    /**
     * 获取指定日期的汇率
     *
     * @param fromCurrency 原币种
     * @param toCurrency 目标币种
     * @param rateDate 汇率日期
     * @param loginStaff 登录用户信息
     * @return 汇率信息
     */
    CurrencyRateVO getCurrencyRate(String fromCurrency, String toCurrency, String rateDate, TblStaffUtil loginStaff);

    /**
     * 获取最新汇率
     *
     * @param fromCurrency 原币种
     * @param toCurrency 目标币种
     * @param loginStaff 登录用户信息
     * @return 最新汇率
     */
    BigDecimal getLatestRate(String fromCurrency, String toCurrency, TblStaffUtil loginStaff);

    /**
     * 保存或更新汇率
     *
     * @param currencyRateVO 汇率信息
     * @param loginStaff 登录用户信息
     * @return 保存结果
     */
    CurrencyRateVO saveOrUpdateCurrencyRate(CurrencyRateVO currencyRateVO, TblStaffUtil loginStaff);

    /**
     * 批量更新汇率
     *
     * @param currencyRateList 汇率列表
     * @param loginStaff 登录用户信息
     * @return 更新结果
     */
    Map<String, Object> batchUpdateCurrencyRates(List<CurrencyRateVO> currencyRateList, TblStaffUtil loginStaff);

    /**
     * 删除汇率
     *
     * @param rateId 汇率ID
     * @param loginStaff 登录用户信息
     * @return 删除结果
     */
    boolean deleteCurrencyRate(Long rateId, TblStaffUtil loginStaff);

    // ==================== 系统配置 ====================

    /**
     * 获取系统配置
     *
     * @param configKey 配置键
     * @param loginStaff 登录用户信息
     * @return 配置值
     */
    String getSystemConfig(String configKey, TblStaffUtil loginStaff);

    /**
     * 保存系统配置
     *
     * @param configKey 配置键
     * @param configValue 配置值
     * @param loginStaff 登录用户信息
     * @return 保存结果
     */
    boolean saveSystemConfig(String configKey, String configValue, TblStaffUtil loginStaff);

    /**
     * 获取所有系统配置
     *
     * @param loginStaff 登录用户信息
     * @return 配置列表
     */
    Map<String, String> getAllSystemConfigs(TblStaffUtil loginStaff);

    /**
     * 批量保存系统配置
     *
     * @param configs 配置映射
     * @param loginStaff 登录用户信息
     * @return 保存结果
     */
    boolean batchSaveSystemConfigs(Map<String, String> configs, TblStaffUtil loginStaff);

    // ==================== 数据字典 ====================

    /**
     * 获取数据字典列表
     *
     * @param dictType 字典类型
     * @param loginStaff 登录用户信息
     * @return 字典列表
     */
    List<Map<String, Object>> getDictionaryList(String dictType, TblStaffUtil loginStaff);

    /**
     * 获取数据字典详情
     *
     * @param dictType 字典类型
     * @param dictKey 字典键
     * @param loginStaff 登录用户信息
     * @return 字典值
     */
    String getDictionaryValue(String dictType, String dictKey, TblStaffUtil loginStaff);

    /**
     * 保存数据字典
     *
     * @param dictType 字典类型
     * @param dictKey 字典键
     * @param dictValue 字典值
     * @param loginStaff 登录用户信息
     * @return 保存结果
     */
    boolean saveDictionary(String dictType, String dictKey, String dictValue, TblStaffUtil loginStaff);

    /**
     * 删除数据字典
     *
     * @param dictType 字典类型
     * @param dictKey 字典键
     * @param loginStaff 登录用户信息
     * @return 删除结果
     */
    boolean deleteDictionary(String dictType, String dictKey, TblStaffUtil loginStaff);

    // ==================== 参数校验 ====================

    /**
     * 校验币种编码唯一性
     *
     * @param currencyCode 币种编码
     * @param excludeId 排除的ID（用于更新时校验）
     * @param loginStaff 登录用户信息
     * @return 是否唯一
     */
    boolean validateCurrencyCodeUnique(String currencyCode, Long excludeId, TblStaffUtil loginStaff);

    /**
     * 校验汇率日期是否存在重复
     *
     * @param fromCurrency 原币种
     * @param toCurrency 目标币种
     * @param rateDate 汇率日期
     * @param excludeId 排除的ID（用于更新时校验）
     * @param loginStaff 登录用户信息
     * @return 是否存在重复
     */
    boolean validateCurrencyRateDuplicate(String fromCurrency, String toCurrency, String rateDate, Long excludeId, TblStaffUtil loginStaff);
}