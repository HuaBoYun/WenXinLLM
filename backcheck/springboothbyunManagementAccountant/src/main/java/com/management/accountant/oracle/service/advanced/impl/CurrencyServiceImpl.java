package com.management.accountant.oracle.service.advanced.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.oracle.entity.advanced.CurrencyConfig;
import com.management.accountant.oracle.entity.advanced.ExchangeRate;
import com.management.accountant.oracle.mapper.advanced.CurrencyConfigMapper;
import com.management.accountant.oracle.mapper.advanced.ExchangeRateMapper;
import com.management.accountant.oracle.service.advanced.CurrencyService;
import com.management.accountant.util.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;

@Slf4j
@Primary
@Service("currencyServiceOracle")
public class CurrencyServiceImpl implements CurrencyService {

    @Resource
    private CurrencyConfigMapper configMapper;
    @Resource
    private ExchangeRateMapper rateMapper;
    private static final SnowflakeIdWorker idWorker = new SnowflakeIdWorker(1, 1);

    @Override
    public List<CurrencyConfig> selectConfigList(Map<String, Object> params) {
        QueryWrapper<CurrencyConfig> w = new QueryWrapper<>();
        w.orderByDesc("CREATE_TIME");
        return configMapper.selectList(w);
    }

    @Override
    public Page<CurrencyConfig> selectConfigPage(Map<String, Object> params, Integer pageNum, Integer pageSize) {
        return configMapper.selectPage(new Page<>(pageNum, pageSize), new QueryWrapper<CurrencyConfig>().orderByDesc("CREATE_TIME"));
    }

    @Override
    public CurrencyConfig selectConfigById(String configId) { return configMapper.selectById(configId); }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertConfig(CurrencyConfig config) {
        config.setCreateTime(new Date());
        return configMapper.insert(config) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateConfig(CurrencyConfig config) {
        config.setUpdateTime(new Date());
        return configMapper.updateById(config) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteConfigById(String configId) { return configMapper.deleteById(configId) > 0; }

    @Override
    public CurrencyConfig selectConfigByCode(String currencyCode) {
        QueryWrapper<CurrencyConfig> w = new QueryWrapper<>();
        w.eq("CURRENCY_CODE", currencyCode);
        return configMapper.selectOne(w);
    }

    @Override
    public List<ExchangeRate> selectRateList(Map<String, Object> params) {
        return rateMapper.selectList(new QueryWrapper<ExchangeRate>().orderByDesc("CREATE_TIME"));
    }

    @Override
    public Page<ExchangeRate> selectRatePage(Map<String, Object> params, Integer pageNum, Integer pageSize) {
        return rateMapper.selectPage(new Page<>(pageNum, pageSize), new QueryWrapper<ExchangeRate>().orderByDesc("CREATE_TIME"));
    }

    @Override
    public ExchangeRate selectRateById(String rateId) { return rateMapper.selectById(rateId); }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insertRate(ExchangeRate rate) {
        rate.setCreateTime(new Date());
        return rateMapper.insert(rate) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateRate(ExchangeRate rate) {
        rate.setUpdateTime(new Date());
        return rateMapper.updateById(rate) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteRateById(String rateId) { return rateMapper.deleteById(rateId) > 0; }

    @Override
    public ExchangeRate getLatestRate(String fromCurrency, String toCurrency) {
        QueryWrapper<ExchangeRate> w = new QueryWrapper<>();
        w.eq("FROM_CURRENCY", fromCurrency).eq("TO_CURRENCY", toCurrency).orderByDesc("CREATE_TIME").last("FETCH FIRST 1 ROWS ONLY");
        return rateMapper.selectOne(w);
    }

    @Override
    public Map<String, Object> convertCurrency(BigDecimal amount, String fromCurrency, String toCurrency, String conversionDate) {
        Map<String, Object> result = new HashMap<>();
        ExchangeRate rate = getLatestRate(fromCurrency, toCurrency);
        if (rate != null) {
            result.put("convertedAmount", amount.multiply(rate.getExchangeRate()));
            result.put("exchangeRate", rate.getExchangeRate());
        } else {
            result.put("convertedAmount", amount);
            result.put("exchangeRate", BigDecimal.ONE);
        }
        result.put("fromCurrency", fromCurrency);
        result.put("toCurrency", toCurrency);
        return result;
    }
}
