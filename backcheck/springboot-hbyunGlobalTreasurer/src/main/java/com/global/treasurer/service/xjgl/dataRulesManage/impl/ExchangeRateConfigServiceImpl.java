package com.global.treasurer.service.xjgl.dataRulesManage.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.entity.TblGtExchangeRateConfig;
import com.global.treasurer.mapper.TblGtExchangeRateConfigMapper;
import com.global.treasurer.service.xjgl.dataRulesManage.ExchangeRateConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * 汇率配置Service实现类
 *
 * @author 华博云开发团队
 * @since 2026-01-28
 */
@Service
public class ExchangeRateConfigServiceImpl extends ServiceImpl<TblGtExchangeRateConfigMapper, TblGtExchangeRateConfig>
        implements ExchangeRateConfigService {
    private static final Logger log = LoggerFactory.getLogger(ExchangeRateConfigServiceImpl.class);

    @Override
    public PageInfo<TblGtExchangeRateConfig> getExchangeRateConfigList(Map<String, Object> params, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        
        QueryWrapper<TblGtExchangeRateConfig> queryWrapper = new QueryWrapper<>();
        
        // 基准币种
        if (params.get("baseCurrency") != null && StringUtils.hasText(params.get("baseCurrency").toString())) {
            queryWrapper.eq("BASE_CURRENCY", params.get("baseCurrency").toString());
        }
        // 目标币种
        if (params.get("targetCurrency") != null && StringUtils.hasText(params.get("targetCurrency").toString())) {
            queryWrapper.eq("TARGET_CURRENCY", params.get("targetCurrency").toString());
        }
        // 汇率类型
        if (params.get("rateType") != null && StringUtils.hasText(params.get("rateType").toString())) {
            queryWrapper.eq("RATE_TYPE", params.get("rateType").toString());
        }
        // 生效日期
        if (params.get("effectiveDate") != null && StringUtils.hasText(params.get("effectiveDate").toString())) {
            queryWrapper.eq("EFFECTIVE_DATE", params.get("effectiveDate").toString());
        }
        // 状态
        if (params.get("status") != null) {
            queryWrapper.eq("STATUS", params.get("status"));
        }
        
        queryWrapper.orderByDesc("CREATE_TIME");

        // 使用 baseMapper.selectList() 而非 this.list()，确保 PageHelper 能正确拦截
        List<TblGtExchangeRateConfig> list = this.baseMapper.selectList(queryWrapper);
        return new PageInfo<>(list);
    }

    @Override
    public int createExchangeRateConfig(TblGtExchangeRateConfig config) {
        config.setCreateTime(new Date());
        config.setUpdateTime(new Date());
        if (config.getStatus() == null) {
            config.setStatus(1);
        }
        return this.save(config) ? 1 : 0;
    }

    @Override
    public int updateExchangeRateConfig(TblGtExchangeRateConfig config) {
        config.setUpdateTime(new Date());
        return this.updateById(config) ? 1 : 0;
    }

    @Override
    public int deleteExchangeRateConfig(Long id) {
        return this.removeById(id) ? 1 : 0;
    }

    @Override
    public Map<String, Object> getStatistics() {
        Map<String, Object> statistics = new HashMap<>();

        // 总数
        long totalCount = this.count();
        statistics.put("totalCount", totalCount);

        // 启用数
        QueryWrapper<TblGtExchangeRateConfig> enabledWrapper = new QueryWrapper<>();
        enabledWrapper.eq("STATUS", 1);
        long enabledCount = this.count(enabledWrapper);
        statistics.put("enabledCount", enabledCount);

        // 禁用数
        statistics.put("disabledCount", totalCount - enabledCount);

        // 按汇率类型统计
        Map<String, Long> typeStats = new HashMap<>();
        typeStats.put("SPOT", countByRateType("SPOT"));
        typeStats.put("CASH", countByRateType("CASH"));
        typeStats.put("MIDDLE", countByRateType("MIDDLE"));
        statistics.put("typeStats", typeStats);

        // 汇率概览：查询 USD/CNY、EUR/CNY、JPY/CNY 最新汇率
        List<Map<String, Object>> rateOverview = new ArrayList<>();
        String[][] currencyPairs = {{"USD", "CNY"}, {"EUR", "CNY"}, {"JPY", "CNY"}};
        for (String[] pair : currencyPairs) {
            Map<String, Object> rateInfo = getLatestRate(pair[0], pair[1]);
            rateOverview.add(rateInfo);
        }
        statistics.put("rateOverview", rateOverview);

        // 最后更新时间：取所有启用记录中最新的 UPDATE_TIME
        QueryWrapper<TblGtExchangeRateConfig> latestWrapper = new QueryWrapper<>();
        latestWrapper.eq("STATUS", 1);
        latestWrapper.orderByDesc("UPDATE_TIME");
        latestWrapper.last("LIMIT 1");
        TblGtExchangeRateConfig latestRecord = this.getOne(latestWrapper, false);
        if (latestRecord != null && latestRecord.getUpdateTime() != null) {
            statistics.put("lastUpdateTime", latestRecord.getUpdateTime());
        } else {
            statistics.put("lastUpdateTime", new Date());
        }

        return statistics;
    }

    /**
     * 查询指定货币对的最新汇率（取启用状态、按生效日期倒序的第一条）
     */
    private Map<String, Object> getLatestRate(String baseCurrency, String targetCurrency) {
        Map<String, Object> rateInfo = new HashMap<>();
        rateInfo.put("baseCurrency", baseCurrency);
        rateInfo.put("targetCurrency", targetCurrency);

        // 查询最新的一条启用记录
        QueryWrapper<TblGtExchangeRateConfig> wrapper = new QueryWrapper<>();
        wrapper.eq("BASE_CURRENCY", baseCurrency);
        wrapper.eq("TARGET_CURRENCY", targetCurrency);
        wrapper.eq("STATUS", 1);
        wrapper.orderByDesc("EFFECTIVE_DATE");
        wrapper.last("LIMIT 1");
        TblGtExchangeRateConfig latest = this.getOne(wrapper, false);

        if (latest != null && latest.getExchangeRate() != null) {
            rateInfo.put("rate", latest.getExchangeRate());
            rateInfo.put("updateTime", latest.getUpdateTime());

            // 查询前一条记录计算涨跌幅
            QueryWrapper<TblGtExchangeRateConfig> prevWrapper = new QueryWrapper<>();
            prevWrapper.eq("BASE_CURRENCY", baseCurrency);
            prevWrapper.eq("TARGET_CURRENCY", targetCurrency);
            prevWrapper.eq("STATUS", 1);
            prevWrapper.orderByDesc("EFFECTIVE_DATE");
            prevWrapper.last("LIMIT 1 OFFSET 1");
            TblGtExchangeRateConfig prev = this.getOne(prevWrapper, false);

            if (prev != null && prev.getExchangeRate() != null
                    && prev.getExchangeRate().compareTo(java.math.BigDecimal.ZERO) != 0) {
                java.math.BigDecimal change = latest.getExchangeRate().subtract(prev.getExchangeRate());
                java.math.BigDecimal changePercent = change.divide(prev.getExchangeRate(), 6, java.math.RoundingMode.HALF_UP)
                        .multiply(new java.math.BigDecimal("100"));
                rateInfo.put("change", change);
                rateInfo.put("changePercent", changePercent);
            } else {
                rateInfo.put("change", java.math.BigDecimal.ZERO);
                rateInfo.put("changePercent", java.math.BigDecimal.ZERO);
            }
        } else {
            rateInfo.put("rate", null);
            rateInfo.put("change", java.math.BigDecimal.ZERO);
            rateInfo.put("changePercent", java.math.BigDecimal.ZERO);
            rateInfo.put("updateTime", null);
        }

        return rateInfo;
    }
    
    private long countByRateType(String rateType) {
        QueryWrapper<TblGtExchangeRateConfig> wrapper = new QueryWrapper<>();
        wrapper.eq("RATE_TYPE", rateType);
        return this.count(wrapper);
    }

    @Override
    public int syncExchangeRateConfig(String sourceType) {
        log.info("同步汇率配置，数据源类型: {}", sourceType);
        // 模拟同步逻辑
        return 0;
    }

    @Override
    public int updateStatus(Long id, Integer status) {
        TblGtExchangeRateConfig config = new TblGtExchangeRateConfig();
        config.setId(id);
        config.setStatus(status);
        config.setUpdateTime(new Date());
        return this.updateById(config) ? 1 : 0;
    }

    @Override
    public int batchDelete(List<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return 0;
        }
        return this.removeByIds(ids) ? ids.size() : 0;
    }
}

