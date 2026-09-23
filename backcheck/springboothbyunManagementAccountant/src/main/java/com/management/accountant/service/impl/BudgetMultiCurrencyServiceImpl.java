package com.management.accountant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetMultiCurrency;
import com.management.accountant.oracle.mapper.BudgetMultiCurrencyMapper;
import com.management.accountant.service.BudgetMultiCurrencyService;
import com.management.accountant.util.PageResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;
import com.management.accountant.oracle.entity.budget.BudgetRateHistory;
import com.management.accountant.oracle.mapper.BudgetRateHistoryMapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;

/**
 * 预算多币种管理Service实现类
 * 
 * @description 预算多币种管理业务实现
 * @author AI Assistant
 * @date 2025-01-04
 */
@Service
@Slf4j
public class BudgetMultiCurrencyServiceImpl implements BudgetMultiCurrencyService {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());


    @Resource
    private BudgetMultiCurrencyMapper multiCurrencyMapper;

    @Resource
    private BudgetRateHistoryMapper rateHistoryMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BudgetMultiCurrency create(BudgetMultiCurrency currency) {
        if (currency == null) {
            throw new ServiceException("币种信息不能为空");
        }
        if (!StringUtils.hasText(currency.getCurrencyCode())) {
            throw new ServiceException("币种代码不能为空");
        }
        if (!StringUtils.hasText(currency.getCurrencyName())) {
            throw new ServiceException("币种名称不能为空");
        }

        if (currency.getDelFlag() == null) {
            currency.setDelFlag(0);
        }
        if (!StringUtils.hasText(currency.getCurrencyStatus())) {
            currency.setCurrencyStatus("ACTIVE");
        }
        currency.setCreateTime(new Date());
        currency.setUpdateTime(new Date());
        currency.setLastUpdateTime(new Date());
        if (currency.getIsBaseCurrency() == null) {
            currency.setIsBaseCurrency(0);
        }
        if (currency.getPrecisionVal() == null) {
            currency.setPrecisionVal(4);
        }

        int result = multiCurrencyMapper.insert(currency);
        if (result <= 0) {
            throw new ServiceException("创建币种配置失败");
        }

        log.info("创建币种配置成功，ID: {}", currency.getCurrencyId());
        return currency;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(BudgetMultiCurrency currency) {
        if (currency == null || !StringUtils.hasText(currency.getCurrencyId())) {
            throw new ServiceException("币种ID不能为空");
        }

        currency.setUpdateTime(new Date());
        int result = multiCurrencyMapper.updateById(currency);
        if (result <= 0) {
            throw new ServiceException("更新币种配置失败");
        }

        log.info("更新币种配置成功，ID: {}", currency.getCurrencyId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void delete(String currencyId) {
        if (!StringUtils.hasText(currencyId)) {
            throw new ServiceException("币种ID不能为空");
        }

        BudgetMultiCurrency currency = new BudgetMultiCurrency();
        currency.setCurrencyId(currencyId);
        currency.setDelFlag(1);
        currency.setUpdateTime(new Date());

        int result = multiCurrencyMapper.updateById(currency);
        if (result <= 0) {
            throw new ServiceException("删除币种配置失败");
        }

        log.info("删除币种配置成功，ID: {}", currencyId);
    }

    @Override
    public PageResult<BudgetMultiCurrency> getPage(Map<String, Object> params) {
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;

        QueryWrapper<BudgetMultiCurrency> wrapper = new QueryWrapper<>();
        wrapper.eq("DEL_FLAG", 0);

        if (params.get("currencyCode") != null) {
            wrapper.like("CURRENCY_CODE", params.get("currencyCode"));
        }
        if (params.get("currencyName") != null) {
            wrapper.like("CURRENCY_NAME", params.get("currencyName"));
        }
        if (params.get("currencyStatus") != null) {
            wrapper.eq("CURRENCY_STATUS", params.get("currencyStatus"));
        }

        wrapper.orderByDesc("CREATE_TIME");

        Page<BudgetMultiCurrency> page = new Page<>(pageNum, pageSize);
        IPage<BudgetMultiCurrency> pageResult = multiCurrencyMapper.selectPage(page, wrapper);

        PageResult<BudgetMultiCurrency> result = new PageResult<>();
        result.setTlist(pageResult.getRecords());
        result.setTotalRecord((int) pageResult.getTotal());
        result.setPageNo(pageNum);
        result.setPageSize(pageSize);

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateExchangeRate(Map<String, Object> params) {
        String currencyCode = (String) params.get("currencyCode");
        String currencyId = (String) params.get("currencyId");

        // 通过currencyCode查找币种
        BudgetMultiCurrency existing = null;
        if (StringUtils.hasText(currencyCode)) {
            QueryWrapper<BudgetMultiCurrency> qw = new QueryWrapper<>();
            qw.eq("CURRENCY_CODE", currencyCode).eq("DEL_FLAG", 0);
            existing = multiCurrencyMapper.selectOne(qw);
        } else if (StringUtils.hasText(currencyId)) {
            existing = multiCurrencyMapper.selectById(currencyId);
        }
        if (existing == null) {
            throw new ServiceException("币种不存在");
        }

        // 记录汇率历史
        Double oldRate = existing.getExchangeRate();
        BudgetRateHistory history = new BudgetRateHistory();
        history.setCurrencyId(existing.getCurrencyId());
        history.setCurrencyCode(existing.getCurrencyCode());
        history.setCurrencyName(existing.getCurrencyName());
        history.setOldRate(oldRate);
        history.setNewRate(oldRate);
        history.setRateSource(existing.getRateSource());
        history.setRecordDate(new Date());
        history.setCreateTime(new Date());
        history.setRemark("汇率更新记录");
        history.setDelFlag(0);
        rateHistoryMapper.insert(history);

        // 更新最后更新时间
        existing.setLastUpdateTime(new Date());
        existing.setUpdateTime(new Date());
        multiCurrencyMapper.updateById(existing);

        log.info("更新汇率成功，币种: {}", existing.getCurrencyCode());
    }

    @Override
    public Map<String, Object> convertCurrency(Map<String, Object> params) {
        String fromCurrency = (String) params.get("fromCurrency");
        String toCurrency = (String) params.get("toCurrency");
        BigDecimal amount = params.get("amount") != null ? 
            new BigDecimal(params.get("amount").toString()) : null;

        if (!StringUtils.hasText(fromCurrency)) {
            throw new ServiceException("源币种不能为空");
        }
        if (!StringUtils.hasText(toCurrency)) {
            throw new ServiceException("目标币种不能为空");
        }
        if (amount == null || amount.compareTo(BigDecimal.ZERO) < 0) {
            throw new ServiceException("金额不能为负数");
        }

        // 从数据库查询汇率
        QueryWrapper<BudgetMultiCurrency> fromQw = new QueryWrapper<>();
        fromQw.eq("CURRENCY_CODE", fromCurrency).eq("DEL_FLAG", 0);
        BudgetMultiCurrency fromEntity = multiCurrencyMapper.selectOne(fromQw);

        QueryWrapper<BudgetMultiCurrency> toQw = new QueryWrapper<>();
        toQw.eq("CURRENCY_CODE", toCurrency).eq("DEL_FLAG", 0);
        BudgetMultiCurrency toEntity = multiCurrencyMapper.selectOne(toQw);

        BigDecimal fromRate = fromEntity != null && fromEntity.getExchangeRate() != null
            ? new BigDecimal(fromEntity.getExchangeRate().toString()) : BigDecimal.ONE;
        BigDecimal toRate = toEntity != null && toEntity.getExchangeRate() != null
            ? new BigDecimal(toEntity.getExchangeRate().toString()) : BigDecimal.ONE;

        BigDecimal convertedAmount = amount.multiply(fromRate).divide(toRate, 2, RoundingMode.HALF_UP);

        Map<String, Object> result = new HashMap<>();
        result.put("fromCurrency", fromCurrency);
        result.put("toCurrency", toCurrency);
        result.put("originalAmount", amount);
        result.put("convertedAmount", convertedAmount);
        result.put("exchangeRate", fromRate.divide(toRate, 4, RoundingMode.HALF_UP));
        result.put("convertTime", new Date());

        log.info("币种转换完成，{} {} -> {} {}", amount, fromCurrency, convertedAmount, toCurrency);
        return result;
    }

    @Override
    public PageResult<Map<String, Object>> getExchangeRateHistory(Map<String, Object> params) {
        int pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;

        QueryWrapper<BudgetRateHistory> wrapper = new QueryWrapper<>();
        if (params.get("currencyCode") != null) {
            wrapper.eq("CURRENCY_CODE", params.get("currencyCode"));
        }
        if (params.get("currencyId") != null) {
            wrapper.eq("CURRENCY_ID", params.get("currencyId"));
        }
        wrapper.orderByDesc("CREATE_TIME");

        Page<BudgetRateHistory> page = new Page<>(pageNum, pageSize);
        IPage<BudgetRateHistory> pageResult = rateHistoryMapper.selectPage(page, wrapper);

        List<Map<String, Object>> history = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (BudgetRateHistory rh : pageResult.getRecords()) {
            Map<String, Object> record = new HashMap<>();
            record.put("historyId", rh.getHistoryId());
            record.put("currencyCode", rh.getCurrencyCode());
            record.put("currencyName", rh.getCurrencyName());
            record.put("exchangeRate", rh.getNewRate());
            record.put("rateChange", rh.getRateChange());
            record.put("rateSource", rh.getRateSource());
            record.put("effectiveDate", rh.getRecordDate() != null ? sdf.format(rh.getRecordDate()) : "");
            record.put("updateTime", rh.getCreateTime() != null ? sdf.format(rh.getCreateTime()) : "");
            history.add(record);
        }

        PageResult<Map<String, Object>> result = new PageResult<>();
        result.setTlist(history);
        result.setTotalRecord((int) pageResult.getTotal());
        result.setPageNo(pageNum);
        result.setPageSize(pageSize);

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String, Object> batchConvert(Map<String, Object> params) {
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> items = (List<Map<String, Object>>) params.get("items");

        if (items == null || items.isEmpty()) {
            throw new ServiceException("转换项列表不能为空");
        }

        int successCount = 0;
        int failCount = 0;
        List<Map<String, Object>> results = new ArrayList<>();

        for (Map<String, Object> item : items) {
            try {
                Map<String, Object> convertResult = convertCurrency(item);
                results.add(convertResult);
                successCount++;
            } catch (Exception e) {
                failCount++;
                log.error("批量币种转换失败", e);
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("totalCount", items.size());
        result.put("successCount", successCount);
        result.put("failCount", failCount);
        result.put("results", results);
        result.put("batchTime", new Date());

        log.info("批量币种转换完成，成功: {}, 失败: {}", successCount, failCount);
        return result;
    }

    @Override
    public Map<String, Object> getCurrencyList(Map<String, Object> params) {
        if (params == null) {
            params = new HashMap<>();
        }
        QueryWrapper<BudgetMultiCurrency> wrapper = new QueryWrapper<>();
        wrapper.eq("DEL_FLAG", 0);

        // 关键字搜索
        String keyword = (String) params.get("keyword");
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like("CURRENCY_CODE", keyword).or().like("CURRENCY_NAME", keyword));
        }
        // 币种类型筛选
        Object currencyTypeObj = params.get("currencyType");
        if (currencyTypeObj != null) {
            String ct = currencyTypeObj.toString();
            if (StringUtils.hasText(ct) && !"ALL".equals(ct)) {
                wrapper.eq("CURRENCY_TYPE", ct);
            }
        }
        // 状态筛选
        String status = (String) params.get("currencyStatus");
        if (StringUtils.hasText(status)) {
            wrapper.eq("CURRENCY_STATUS", status);
        }
        wrapper.orderByDesc("IS_BASE_CURRENCY").orderByAsc("CURRENCY_CODE");

        List<BudgetMultiCurrency> entities = multiCurrencyMapper.selectList(wrapper);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        List<Map<String, Object>> list = new ArrayList<>();
        for (BudgetMultiCurrency c : entities) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", c.getCurrencyId());
            item.put("currencyId", c.getCurrencyId());
            item.put("currencyCode", c.getCurrencyCode());
            item.put("currencyName", c.getCurrencyName());
            item.put("currencySymbol", c.getCurrencySymbol());
            item.put("exchangeRate", c.getExchangeRate());
            item.put("rateChange", c.getRateChange() != null ? c.getRateChange() : 0.0);
            item.put("lastUpdateTime", c.getLastUpdateTime() != null ? sdf.format(c.getLastUpdateTime()) : "");
            item.put("isBaseCurrency", c.getIsBaseCurrency() != null && c.getIsBaseCurrency() == 1);
            item.put("status", c.getCurrencyStatus());
            item.put("currencyType", c.getCurrencyType());
            item.put("description", c.getDescription());
            item.put("rateSource", c.getRateSource());
            item.put("updateFrequency", c.getUpdateFrequency());
            item.put("precisionVal", c.getPrecisionVal());
            item.put("roundingRule", c.getRoundingRule());
            item.put("configId", c.getCurrencyId());
            list.add(item);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("totalCount", list.size());
        return result;
    }

    @Override
    public Map<String, Object> getCurrencyStats(Map<String, Object> params) {
        Map<String, Object> stats = new HashMap<>();

        // 总币种数
        QueryWrapper<BudgetMultiCurrency> totalQw = new QueryWrapper<>();
        totalQw.eq("DEL_FLAG", 0);
        long totalCurrencies = multiCurrencyMapper.selectCount(totalQw);

        // 活跃币种数
        QueryWrapper<BudgetMultiCurrency> activeQw = new QueryWrapper<>();
        activeQw.eq("DEL_FLAG", 0).eq("CURRENCY_STATUS", "ACTIVE");
        long activeCurrencies = multiCurrencyMapper.selectCount(activeQw);

        // 今日汇率更新次数
        QueryWrapper<BudgetRateHistory> todayQw = new QueryWrapper<>();
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        todayQw.ge("CREATE_TIME", cal.getTime());
        long rateUpdates = rateHistoryMapper.selectCount(todayQw);

        // 转换次数（使用汇率历史总数作为代理）
        long conversions = rateHistoryMapper.selectCount(new QueryWrapper<>());

        // 按币种类型统计
        QueryWrapper<BudgetMultiCurrency> majorQw = new QueryWrapper<>();
        majorQw.eq("DEL_FLAG", 0).eq("CURRENCY_TYPE", "MAJOR");
        long majorCount = multiCurrencyMapper.selectCount(majorQw);

        QueryWrapper<BudgetMultiCurrency> asiaQw = new QueryWrapper<>();
        asiaQw.eq("DEL_FLAG", 0).eq("CURRENCY_TYPE", "ASIA");
        long asiaCount = multiCurrencyMapper.selectCount(asiaQw);

        QueryWrapper<BudgetMultiCurrency> europeQw = new QueryWrapper<>();
        europeQw.eq("DEL_FLAG", 0).eq("CURRENCY_TYPE", "EUROPE");
        long europeCount = multiCurrencyMapper.selectCount(europeQw);

        QueryWrapper<BudgetMultiCurrency> otherQw = new QueryWrapper<>();
        otherQw.eq("DEL_FLAG", 0).eq("CURRENCY_TYPE", "OTHER");
        long otherCount = multiCurrencyMapper.selectCount(otherQw);

        stats.put("totalCurrencies", totalCurrencies);
        stats.put("activeCurrencies", activeCurrencies);
        stats.put("rateUpdates", rateUpdates);
        stats.put("conversions", conversions);
        stats.put("majorCount", majorCount);
        stats.put("asiaCount", asiaCount);
        stats.put("europeCount", europeCount);
        stats.put("otherCount", otherCount);

        return stats;
    }

    @Override
    public Map<String, Object> getRateHistory(Map<String, Object> params) {
        String currencyCode = (String) params.get("currencyCode");
        String currencyId = (String) params.get("currencyId");

        QueryWrapper<BudgetRateHistory> wrapper = new QueryWrapper<>();
        if (StringUtils.hasText(currencyCode)) {
            wrapper.eq("CURRENCY_CODE", currencyCode);
        } else if (StringUtils.hasText(currencyId)) {
            wrapper.eq("CURRENCY_ID", currencyId);
        }
        wrapper.orderByDesc("CREATE_TIME");

        List<BudgetRateHistory> records = rateHistoryMapper.selectList(wrapper);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        List<Map<String, Object>> history = new ArrayList<>();
        for (BudgetRateHistory rh : records) {
            Map<String, Object> record = new HashMap<>();
            record.put("date", rh.getRecordDate() != null ? sdf.format(rh.getRecordDate()) : "");
            record.put("rate", rh.getNewRate());
            record.put("rateChange", rh.getRateChange());
            record.put("rateSource", rh.getRateSource());
            record.put("currencyCode", rh.getCurrencyCode());
            record.put("currencyName", rh.getCurrencyName());
            history.add(record);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("currencyId", currencyId);
        result.put("currencyCode", currencyCode);
        result.put("history", history);
        result.put("totalCount", history.size());

        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void enableCurrency(String currencyId) {
        if (!StringUtils.hasText(currencyId)) {
            throw new ServiceException("币种ID不能为空");
        }

        BudgetMultiCurrency currency = new BudgetMultiCurrency();
        currency.setCurrencyId(currencyId);
        currency.setCurrencyStatus("ACTIVE");
        currency.setUpdateTime(new Date());

        int result = multiCurrencyMapper.updateById(currency);
        if (result <= 0) {
            throw new ServiceException("启用币种失败");
        }

        log.info("启用币种成功，ID: {}", currencyId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void disableCurrency(String currencyId) {
        if (!StringUtils.hasText(currencyId)) {
            throw new ServiceException("币种ID不能为空");
        }

        BudgetMultiCurrency currency = new BudgetMultiCurrency();
        currency.setCurrencyId(currencyId);
        currency.setCurrencyStatus("INACTIVE");
        currency.setUpdateTime(new Date());

        int result = multiCurrencyMapper.updateById(currency);
        if (result <= 0) {
            throw new ServiceException("禁用币种失败");
        }

        log.info("禁用币种成功，ID: {}", currencyId);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void setBaseCurrency(String currencyId) {
        // 注意：controller层传入的是currencyCode
        if (!StringUtils.hasText(currencyId)) {
            throw new ServiceException("币种代码不能为空");
        }

        // 查找目标币种
        QueryWrapper<BudgetMultiCurrency> qw = new QueryWrapper<>();
        qw.eq("CURRENCY_CODE", currencyId).eq("DEL_FLAG", 0);
        BudgetMultiCurrency target = multiCurrencyMapper.selectOne(qw);
        if (target == null) {
            throw new ServiceException("币种不存在: " + currencyId);
        }

        // 先将所有币种的基准标志设为0
        UpdateWrapper<BudgetMultiCurrency> resetWrapper = new UpdateWrapper<>();
        resetWrapper.eq("DEL_FLAG", 0).set("IS_BASE_CURRENCY", 0);
        multiCurrencyMapper.update(null, resetWrapper);

        // 将目标币种设为基准
        target.setIsBaseCurrency(1);
        target.setUpdateTime(new Date());
        multiCurrencyMapper.updateById(target);

        log.info("设置基准币种成功，币种代码: {}", currencyId);
    }

    @Override
    public BudgetMultiCurrency getDetail(String currencyId) {
        if (!StringUtils.hasText(currencyId)) {
            throw new ServiceException("币种ID不能为空");
        }
        BudgetMultiCurrency currency = multiCurrencyMapper.selectById(currencyId);
        if (currency == null || (currency.getDelFlag() != null && currency.getDelFlag() == 1)) {
            throw new ServiceException("币种不存在");
        }
        return currency;
    }
}

