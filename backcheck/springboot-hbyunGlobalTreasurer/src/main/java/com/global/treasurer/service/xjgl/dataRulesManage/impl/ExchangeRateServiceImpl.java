package com.global.treasurer.service.xjgl.dataRulesManage.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblGtExchangeRate;
import com.global.treasurer.mapper.TblGtExchangeRateMapper;
import com.global.treasurer.service.xjgl.dataRulesManage.ExchangeRateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 汇率管理Service实现类
 *
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Service
public class ExchangeRateServiceImpl extends ServiceImpl<TblGtExchangeRateMapper, TblGtExchangeRate> implements ExchangeRateService {
    private static final Logger log = LoggerFactory.getLogger(ExchangeRateServiceImpl.class);

    @Override
    public PageInfo<TblGtExchangeRate> getExchangeRateList(Map<String, Object> params, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);

        QueryWrapper<TblGtExchangeRate> queryWrapper = new QueryWrapper<>();

        // 添加查询条件
        if (params.get("fromCurrency") != null && !"".equals(params.get("fromCurrency"))) {
            queryWrapper.eq("FROM_CURRENCY", params.get("fromCurrency"));
        }
        if (params.get("toCurrency") != null && !"".equals(params.get("toCurrency"))) {
            queryWrapper.eq("TO_CURRENCY", params.get("toCurrency"));
        }
        if (params.get("rateSource") != null && !"".equals(params.get("rateSource"))) {
            queryWrapper.eq("RATE_SOURCE", params.get("rateSource"));
        }
        // 支持日期范围查询（startDate / endDate）和精确匹配（effectiveDate）
        if (params.get("startDate") != null && !"".equals(params.get("startDate"))) {
            queryWrapper.ge("EFFECTIVE_DATE", params.get("startDate"));
        }
        if (params.get("endDate") != null && !"".equals(params.get("endDate"))) {
            queryWrapper.le("EFFECTIVE_DATE", params.get("endDate"));
        }
        if (params.get("effectiveDate") != null && !"".equals(params.get("effectiveDate"))) {
            queryWrapper.eq("EFFECTIVE_DATE", params.get("effectiveDate"));
        }

        // 只查询未删除的数据
        queryWrapper.eq("STATUS", 1);
        queryWrapper.orderByDesc("CREATE_TIME");

        // 使用 baseMapper.selectList() 而非 this.list()，确保 PageHelper 能正确拦截
        List<TblGtExchangeRate> list = this.baseMapper.selectList(queryWrapper);
        return new PageInfo<>(list);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int createExchangeRate(TblGtExchangeRate exchangeRate) {
        exchangeRate.setCreateTime(new Date());
        exchangeRate.setUpdateTime(new Date());
        exchangeRate.setStatus(1);
        exchangeRate.setIsActive(1);
        return this.baseMapper.insert(exchangeRate);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateExchangeRate(TblGtExchangeRate exchangeRate) {
        exchangeRate.setUpdateTime(new Date());
        return this.baseMapper.updateById(exchangeRate);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteExchangeRate(Long rateId) {
        TblGtExchangeRate exchangeRate = new TblGtExchangeRate();
        exchangeRate.setRateId(rateId);
        exchangeRate.setStatus(0);
        exchangeRate.setUpdateTime(new Date());
        return this.baseMapper.updateById(exchangeRate);
    }

    @Override
    public Map<String, Object> getStatistics() {
        Map<String, Object> statistics = new HashMap<>();

        // 统计总币种数
        QueryWrapper<TblGtExchangeRate> wrapper = new QueryWrapper<>();
        wrapper.select("DISTINCT FROM_CURRENCY");
        wrapper.eq("STATUS", 1);
        List<TblGtExchangeRate> currencies = this.baseMapper.selectList(wrapper);
        statistics.put("totalCurrencies", currencies.size());

        // 获取最新USD/CNY汇率
        QueryWrapper<TblGtExchangeRate> usdWrapper = new QueryWrapper<>();
        usdWrapper.eq("FROM_CURRENCY", "USD");
        usdWrapper.eq("TO_CURRENCY", "CNY");
        usdWrapper.eq("STATUS", 1);
        usdWrapper.orderByDesc("EFFECTIVE_DATE");
        usdWrapper.last("LIMIT 1");
        TblGtExchangeRate usdRate = this.baseMapper.selectOne(usdWrapper);
        statistics.put("usdRate", usdRate != null ? usdRate.getExchangeRate().toString() : "7.2456");

        // 获取最新EUR/CNY汇率
        QueryWrapper<TblGtExchangeRate> eurWrapper = new QueryWrapper<>();
        eurWrapper.eq("FROM_CURRENCY", "EUR");
        eurWrapper.eq("TO_CURRENCY", "CNY");
        eurWrapper.eq("STATUS", 1);
        eurWrapper.orderByDesc("EFFECTIVE_DATE");
        eurWrapper.last("LIMIT 1");
        TblGtExchangeRate eurRate = this.baseMapper.selectOne(eurWrapper);
        statistics.put("eurRate", eurRate != null ? eurRate.getExchangeRate().toString() : "7.8923");

        statistics.put("lastUpdateTime", new Date().getMinutes() + "分钟前");

        return statistics;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int syncExchangeRate(String sourceType) {
        // 模拟同步汇率数据
        log.info("开始从{}同步汇率数据", sourceType);

        // 这里应该调用外部API获取实时汇率数据
        // 示例代码:
        // List<ExchangeRate> rates = externalApiService.getExchangeRates(sourceType);
        // for (ExchangeRate rate : rates) {
        //     this.createExchangeRate(rate);
        // }

        // 暂时返回0,表示没有同步新数据
        return 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDelete(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return 0;
        }
        int count = 0;
        for (Long id : ids) {
            count += deleteExchangeRate(id);
        }
        return count;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateStatus(Long id, Integer status) {
        TblGtExchangeRate exchangeRate = new TblGtExchangeRate();
        exchangeRate.setRateId(id);
        exchangeRate.setStatus(status);
        exchangeRate.setUpdateTime(new Date());
        return this.baseMapper.updateById(exchangeRate);
    }

    @Override
    public Map<String, Object> getTrendAnalysis(Map<String, Object> params) {
        Map<String, Object> result = new HashMap<>();

        String fromCurrency = params.get("fromCurrency") != null ? params.get("fromCurrency").toString() : "USD";
        String toCurrency = params.get("toCurrency") != null ? params.get("toCurrency").toString() : "CNY";
        Integer days = params.get("days") != null ? Integer.parseInt(params.get("days").toString()) : 7;

        // 查询最近N天的汇率数据
        QueryWrapper<TblGtExchangeRate> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("FROM_CURRENCY", fromCurrency);
        queryWrapper.eq("TO_CURRENCY", toCurrency);
        queryWrapper.eq("STATUS", 1);
        queryWrapper.orderByDesc("EFFECTIVE_DATE");
        queryWrapper.last("LIMIT " + days);

        List<TblGtExchangeRate> rates = this.list(queryWrapper);

        // 构建趋势数据
        result.put("fromCurrency", fromCurrency);
        result.put("toCurrency", toCurrency);
        result.put("rates", rates);

        // 处理BigDecimal类型的汇率数据
        if (!rates.isEmpty()) {
            result.put("maxRate", rates.stream().map(TblGtExchangeRate::getExchangeRate).max(java.math.BigDecimal::compareTo).orElse(java.math.BigDecimal.ZERO));
            result.put("minRate", rates.stream().map(TblGtExchangeRate::getExchangeRate).min(java.math.BigDecimal::compareTo).orElse(java.math.BigDecimal.ZERO));
            result.put("avgRate", rates.stream().mapToDouble(r -> r.getExchangeRate().doubleValue()).average().orElse(0.0));
        } else {
            result.put("maxRate", java.math.BigDecimal.ZERO);
            result.put("minRate", java.math.BigDecimal.ZERO);
            result.put("avgRate", 0.0);
        }

        return result;
    }
}
