package com.financial.sharing.service.impl;

import com.financial.sharing.dto.CurrencyQueryParam;
import com.financial.sharing.dto.CurrencyRateQueryParam;
import com.financial.sharing.service.CommonService;
import com.financial.sharing.vo.result.CurrencyVO;
import com.financial.sharing.vo.result.CurrencyRateVO;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hbfk.entity.TblStaffUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * 通用设置服务实现类
 *
 * @author Financial Sharing System
 * @since 2024-12-08
 */
@Slf4j
@Service
@Transactional(readOnly = true)
public class CommonServiceImpl implements CommonService {

    // 临时模拟数据，实际应该从数据库获取
    private static final List<CurrencyVO> MOCK_CURRENCIES = Arrays.asList(
        createCurrencyVO(1L, "CNY", "人民币", "¥", true),
        createCurrencyVO(2L, "USD", "美元", "$", true),
        createCurrencyVO(3L, "EUR", "欧元", "€", true),
        createCurrencyVO(4L, "JPY", "日元", "¥", false)
    );

    private static final List<CurrencyRateVO> MOCK_RATES = Arrays.asList(
        createCurrencyRateVO(1L, "CNY", "USD", new BigDecimal("0.14"), true),
        createCurrencyRateVO(2L, "USD", "CNY", new BigDecimal("7.15"), true),
        createCurrencyRateVO(3L, "CNY", "EUR", new BigDecimal("0.13"), true)
    );

    private static final Map<String, String> MOCK_SYSTEM_CONFIGS = new HashMap<String, String>() {{
        put("system.name", "华博云财务共享系统");
        put("system.version", "1.0.0");
        put("default.currency", "CNY");
        put("decimal.places", "2");
        put("auto.voucher.number", "true");
    }};

    private static final Map<String, Map<String, String>> MOCK_DICTIONARIES = new HashMap<String, Map<String, String>>() {{
        put("voucher_type", new HashMap<String, String>() {{
            put("01", "记账凭证");
            put("02", "收款凭证");
            put("03", "付款凭证");
            put("04", "转账凭证");
        }});
        put("account_type", new HashMap<String, String>() {{
            put("1", "资产类");
            put("2", "负债类");
            put("3", "所有者权益类");
            put("4", "收入类");
            put("5", "费用类");
        }});
    }};

    // ==================== 币种管理 ====================

    @Override
    public PageInfo<CurrencyVO> getCurrencyPage(CurrencyQueryParam param, TblStaffUtil loginStaff) {
        log.info("查询币种列表，参数：{}，用户：{}", param, loginStaff.getStaffid());

        try {
            // 设置分页
            if (param.getPageNo() != null && param.getPageSize() != null) {
                PageHelper.startPage(param.getPageNo(), param.getPageSize());
            }

            // 模拟数据过滤
            List<CurrencyVO> filteredList = new ArrayList<>();
            for (CurrencyVO currency : MOCK_CURRENCIES) {
                boolean match = true;

                if (param.getCurrencyCode() != null && !param.getCurrencyCode().isEmpty()) {
                    match = currency.getCurrencyCode().contains(param.getCurrencyCode());
                }
                if (match && param.getCurrencyName() != null && !param.getCurrencyName().isEmpty()) {
                    match = currency.getCurrencyName().contains(param.getCurrencyName());
                }
                if (match && param.getStatus() != null) {
                    match = "ACTIVE".equals(currency.getStatus());
                }
                if (match) {
                    filteredList.add(currency);
                }
            }
            return new PageInfo<>(filteredList);
        } catch (Exception e) {
            log.error("查询币种列表失败", e);
            throw new RuntimeException("查询币种列表失败: " + e.getMessage(), e);
        }
    }

    @Override
    public List<CurrencyVO> getActiveCurrencyList(TblStaffUtil loginStaff) {
        log.info("获取启用币种列表，用户：{}", loginStaff.getStaffid());

        List<CurrencyVO> activeCurrencies = new ArrayList<>();
        for (CurrencyVO currency : MOCK_CURRENCIES) {
            if ("ACTIVE".equals(currency.getStatus())) {
                activeCurrencies.add(currency);
            }
        }
        return activeCurrencies;
    }

    @Override
    public CurrencyVO getCurrencyByCode(String currencyCode, TblStaffUtil loginStaff) {
        log.info("根据编码查询币种，编码：{}，用户：{}", currencyCode, loginStaff.getStaffid());

        for (CurrencyVO currency : MOCK_CURRENCIES) {
            if (currency.getCurrencyCode().equals(currencyCode)) {
                return currency;
            }
        }
        return null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CurrencyVO saveOrUpdateCurrency(CurrencyVO currencyVO, TblStaffUtil loginStaff) {
        log.info("保存或更新币种，币种：{}，用户：{}", currencyVO.getCurrencyCode(), loginStaff.getStaffid());

        // 模拟保存操作
        if (currencyVO.getCurrencyId() == null) {
            // 新增
            currencyVO.setCurrencyId(System.currentTimeMillis());
            currencyVO.setStatus("ACTIVE");
            currencyVO.setStatusName("启用");
        } else {
            // 更新
            // 实际应该更新数据库中的记录
        }
        return currencyVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteCurrency(Long currencyId, TblStaffUtil loginStaff) {
        log.info("删除币种，ID：{}，用户：{}", currencyId, loginStaff.getStaffid());

        // 模拟删除操作
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean toggleCurrencyStatus(Long currencyId, Boolean enabled, TblStaffUtil loginStaff) {
        log.info("切换币种状态，ID：{}，状态：{}，用户：{}", currencyId, enabled, loginStaff.getStaffid());

        // 模拟状态切换操作
        return true;
    }

    // ==================== 汇率管理 ====================

    @Override
    public PageInfo<CurrencyRateVO> getCurrencyRatePage(CurrencyRateQueryParam param, TblStaffUtil loginStaff) {
        log.info("查询汇率列表，参数：{}，用户：{}", param, loginStaff.getStaffid());

        try {
            // 设置分页
            if (param.getPageNo() != null && param.getPageSize() != null) {
                PageHelper.startPage(param.getPageNo(), param.getPageSize());
            }

            // 模拟数据过滤
            List<CurrencyRateVO> filteredList = new ArrayList<>();
            for (CurrencyRateVO rate : MOCK_RATES) {
                boolean match = true;

                // 假设currencyCode格式为 "CNY/USD"，从中解析fromCurrency和toCurrency
                if (rate.getCurrencyCode() != null) {
                    String[] currencies = rate.getCurrencyCode().split("/");
                    if (currencies.length == 2) {
                        String fromCurrency = currencies[0];
                        String toCurrency = currencies[1];

                        if (param.getFromCurrency() != null && !param.getFromCurrency().isEmpty()) {
                            match = fromCurrency.contains(param.getFromCurrency());
                        }
                        if (match && param.getToCurrency() != null && !param.getToCurrency().isEmpty()) {
                            match = toCurrency.contains(param.getToCurrency());
                        }
                    }
                }

                // 日期范围过滤
                if (match && (param.getStartDate() != null || param.getEndDate() != null)) {
                    if (param.getStartDate() != null && rate.getRateDate().isBefore(param.getStartDate())) {
                        match = false;
                    }
                    if (match && param.getEndDate() != null && rate.getRateDate().isAfter(param.getEndDate())) {
                        match = false;
                    }
                }
                if (match) {
                    filteredList.add(rate);
                }
            }
            return new PageInfo<>(filteredList);
        } catch (Exception e) {
            log.error("查询汇率列表失败", e);
            throw new RuntimeException("查询汇率列表失败: " + e.getMessage(), e);
        }
    }

    @Override
    public CurrencyRateVO getCurrencyRate(String fromCurrency, String toCurrency, String rateDate, TblStaffUtil loginStaff) {
        log.info("查询汇率，{} -> {}，日期：{}，用户：{}", fromCurrency, toCurrency, rateDate, loginStaff.getStaffid());

        for (CurrencyRateVO rate : MOCK_RATES) {
            // 解析currencyCode获取fromCurrency和toCurrency
            if (rate.getCurrencyCode() != null) {
                String[] currencies = rate.getCurrencyCode().split("/");
                if (currencies.length == 2 &&
                    currencies[0].equals(fromCurrency) &&
                    currencies[1].equals(toCurrency) &&
                    rate.getRateDate().toString().equals(rateDate)) {
                    return rate;
                }
            }
        }
        return null;
    }

    @Override
    public BigDecimal getLatestRate(String fromCurrency, String toCurrency, TblStaffUtil loginStaff) {
        log.info("查询最新汇率，{} -> {}，用户：{}", fromCurrency, toCurrency, loginStaff.getStaffid());

        for (CurrencyRateVO rate : MOCK_RATES) {
            // 解析currencyCode获取fromCurrency和toCurrency
            if (rate.getCurrencyCode() != null) {
                String[] currencies = rate.getCurrencyCode().split("/");
                if (currencies.length == 2 &&
                    currencies[0].equals(fromCurrency) &&
                    currencies[1].equals(toCurrency)) {
                    return rate.getExchangeRate(); // 使用exchangeRate字段
                }
            }
        }
        return BigDecimal.ONE; // 默认汇率
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CurrencyRateVO saveOrUpdateCurrencyRate(CurrencyRateVO currencyRateVO, TblStaffUtil loginStaff) {
        // 解析currencyCode获取fromCurrency和toCurrency用于日志
        String fromCurrency = "";
        String toCurrency = "";
        if (currencyRateVO.getCurrencyCode() != null) {
            String[] currencies = currencyRateVO.getCurrencyCode().split("/");
            if (currencies.length == 2) {
                fromCurrency = currencies[0];
                toCurrency = currencies[1];
            }
        }
        log.info("保存或更新汇率，{} -> {}，用户：{}", fromCurrency, toCurrency, loginStaff.getStaffid());

        // 模拟保存操作
        if (currencyRateVO.getRateId() == null) {
            // 新增
            currencyRateVO.setRateId(System.currentTimeMillis());
            currencyRateVO.setIsEnabled(1); // 1表示启用
        } else {
            // 更新
            // 实际应该更新数据库中的记录
        }
        return currencyRateVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> batchUpdateCurrencyRates(List<CurrencyRateVO> currencyRateList, TblStaffUtil loginStaff) {
        log.info("批量更新汇率，数量：{}，用户：{}", currencyRateList.size(), loginStaff.getStaffid());

        Map<String, Object> result = new HashMap<>();
        result.put("successCount", currencyRateList.size());
        result.put("failCount", 0);
        result.put("totalCount", currencyRateList.size());

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteCurrencyRate(Long rateId, TblStaffUtil loginStaff) {
        log.info("删除汇率，ID：{}，用户：{}", rateId, loginStaff.getStaffid());

        // 模拟删除操作
        return true;
    }

    // ==================== 系统配置 ====================

    @Override
    public String getSystemConfig(String configKey, TblStaffUtil loginStaff) {
        log.info("获取系统配置，键：{}，用户：{}", configKey, loginStaff.getStaffid());

        return MOCK_SYSTEM_CONFIGS.get(configKey);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveSystemConfig(String configKey, String configValue, TblStaffUtil loginStaff) {
        log.info("保存系统配置，键：{}，值：{}，用户：{}", configKey, configValue, loginStaff.getStaffid());

        MOCK_SYSTEM_CONFIGS.put(configKey, configValue);
        return true;
    }

    @Override
    public Map<String, String> getAllSystemConfigs(TblStaffUtil loginStaff) {
        log.info("获取所有系统配置，用户：{}", loginStaff.getStaffid());

        return new HashMap<>(MOCK_SYSTEM_CONFIGS);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchSaveSystemConfigs(Map<String, String> configs, TblStaffUtil loginStaff) {
        log.info("批量保存系统配置，数量：{}，用户：{}", configs.size(), loginStaff.getStaffid());

        MOCK_SYSTEM_CONFIGS.putAll(configs);
        return true;
    }

    // ==================== 数据字典 ====================

    @Override
    public List<Map<String, Object>> getDictionaryList(String dictType, TblStaffUtil loginStaff) {
        log.info("获取数据字典列表，类型：{}，用户：{}", dictType, loginStaff.getStaffid());

        List<Map<String, Object>> resultList = new ArrayList<>();
        Map<String, String> dict = MOCK_DICTIONARIES.get(dictType);

        if (dict != null) {
            for (Map.Entry<String, String> entry : dict.entrySet()) {
                Map<String, Object> item = new HashMap<>();
                item.put("dictKey", entry.getKey());
                item.put("dictValue", entry.getValue());
                item.put("dictType", dictType);
                resultList.add(item);
            }
        }
        return resultList;
    }

    @Override
    public String getDictionaryValue(String dictType, String dictKey, TblStaffUtil loginStaff) {
        log.info("获取数据字典值，类型：{}，键：{}，用户：{}", dictType, dictKey, loginStaff.getStaffid());

        Map<String, String> dict = MOCK_DICTIONARIES.get(dictType);
        return dict != null ? dict.get(dictKey) : null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean saveDictionary(String dictType, String dictKey, String dictValue, TblStaffUtil loginStaff) {
        log.info("保存数据字典，类型：{}，键：{}，值：{}，用户：{}", dictType, dictKey, dictValue, loginStaff.getStaffid());

        Map<String, String> dict = MOCK_DICTIONARIES.computeIfAbsent(dictType, k -> new HashMap<>());
        dict.put(dictKey, dictValue);
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteDictionary(String dictType, String dictKey, TblStaffUtil loginStaff) {
        log.info("删除数据字典，类型：{}，键：{}，用户：{}", dictType, dictKey, loginStaff.getStaffid());

        Map<String, String> dict = MOCK_DICTIONARIES.get(dictType);
        if (dict != null) {
            dict.remove(dictKey);
        }
        return true;
    }

    // ==================== 参数校验 ====================

    @Override
    public boolean validateCurrencyCodeUnique(String currencyCode, Long excludeId, TblStaffUtil loginStaff) {
        log.info("校验币种编码唯一性，编码：{}，排除ID：{}，用户：{}", currencyCode, excludeId, loginStaff.getStaffid());

        for (CurrencyVO currency : MOCK_CURRENCIES) {
            if (currency.getCurrencyCode().equals(currencyCode) &&
                !currency.getCurrencyId().equals(excludeId)) {
                return false; // 存在重复
            }
        }
        return true; // 唯一
    }

    @Override
    public boolean validateCurrencyRateDuplicate(String fromCurrency, String toCurrency, String rateDate, Long excludeId, TblStaffUtil loginStaff) {
        log.info("校验汇率重复，{} -> {}，日期：{}，排除ID：{}，用户：{}", fromCurrency, toCurrency, rateDate, excludeId, loginStaff.getStaffid());

        for (CurrencyRateVO rate : MOCK_RATES) {
            // 解析currencyCode获取fromCurrency和toCurrency
            if (rate.getCurrencyCode() != null) {
                String[] currencies = rate.getCurrencyCode().split("/");
                if (currencies.length == 2 &&
                    currencies[0].equals(fromCurrency) &&
                    currencies[1].equals(toCurrency) &&
                    rate.getRateDate().toString().equals(rateDate) &&
                    !rate.getRateId().equals(excludeId)) {
                    return true; // 存在重复
                }
            }
        }
        return false; // 不重复
    }

    /**
     * 创建币种VO的辅助方法
     */
    private static CurrencyVO createCurrencyVO(Long currencyId, String currencyCode, String currencyName, String currencySymbol, Boolean isEnabled) {
        CurrencyVO currency = new CurrencyVO();
        currency.setCurrencyId(currencyId);
        currency.setCurrencyCode(currencyCode);
        currency.setCurrencyName(currencyName);
        currency.setCurrencySymbol(currencySymbol);
        currency.setStatus(isEnabled ? "ACTIVE" : "INACTIVE");
        currency.setStatusName(isEnabled ? "启用" : "禁用");
        currency.setIsBaseCurrency(currencyId.equals(1L)); // 假设CNY是本位币
        currency.setDecimalPlaces(2);
        currency.setExchangeRate(new BigDecimal("1.0")); // 模拟汇率
        return currency;
    }

    /**
     * 创建汇率VO的辅助方法
     */
    private static CurrencyRateVO createCurrencyRateVO(Long rateId, String fromCurrency, String toCurrency, BigDecimal rate, Boolean enabled) {
        CurrencyRateVO rateVO = new CurrencyRateVO();
        rateVO.setRateId(rateId);
        rateVO.setCurrencyCode(fromCurrency + "/" + toCurrency);
        rateVO.setCurrencyName(fromCurrency + "到" + toCurrency + "汇率");
        rateVO.setRateType(1); // 即期汇率
        rateVO.setRateTypeName("即期汇率");
        rateVO.setExchangeRate(rate);
        rateVO.setRateDate(LocalDate.now());
        return rateVO;
    }
}