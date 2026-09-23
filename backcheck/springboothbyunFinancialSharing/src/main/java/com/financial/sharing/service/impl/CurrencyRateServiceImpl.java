package com.financial.sharing.service.impl;

import com.financial.sharing.util.Java8Collections;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.financial.sharing.config.DateBaseConfig;
import com.financial.sharing.service.CurrencyRateService;
import com.financial.sharing.util.PageResult;
import com.financial.sharing.vo.param.CurrencyRateQueryParam;
import com.financial.sharing.vo.param.CurrencyRateSaveParam;
import com.financial.sharing.vo.result.CurrencyRateVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 币种汇率服务实现类
 * 
 * @author system
 * @since 2024-12-19
 */
@Slf4j
@Service
public class CurrencyRateServiceImpl implements CurrencyRateService {

    @Autowired
    private DateBaseConfig dateBaseConfig;

    // 汇率类型名称映射
    private static final Map<Integer, String> RATE_TYPE_NAME_MAP = new HashMap<>();
    
    static {
        RATE_TYPE_NAME_MAP.put(1, "即期汇率");
        RATE_TYPE_NAME_MAP.put(2, "远期汇率");
        RATE_TYPE_NAME_MAP.put(3, "固定汇率");
    }

    @Override
    public PageResult<CurrencyRateVO> getCurrencyRatePage(CurrencyRateQueryParam param) {
        // 确保分页参数不为空
        if (param.getPageNum() == null || param.getPageNum() <= 0) {
            param.setPageNum(1);
        }
        if (param.getPageSize() == null || param.getPageSize() <= 0) {
            param.setPageSize(15);
        }

        log.info("汇率分页参数: pageNum={}, pageSize={}, pageNumber={}",
            param.getPageNum(), param.getPageSize(), param.getPageNumber());

        // 使用PageHelper进行分页 - 修复分页参数映射问题
        PageHelper.startPage(param.getPageNum(), param.getPageSize());

        List<CurrencyRateVO> list = dateBaseConfig.getOracleCurrencyRateMapper().selectCurrencyRatePage(
            param.getBookId(), param.getTenantId(), param.getCurrencyCode(), param.getCurrencyName(),
            param.getRateType(), param.getRateDateStart(), param.getRateDateEnd(),
            param.getIsBaseCurrency(), param.getIsEnabled());

        // 使用PageInfo包装分页结果
        PageInfo<CurrencyRateVO> pageInfo = new PageInfo<>(list);

        // 设置汇率类型名称
        if (!Java8Collections.isEmpty(pageInfo.getList())) {
            pageInfo.getList().forEach(this::setRateTypeName);
        }
        return new PageResult<>((int)pageInfo.getTotal(), pageInfo.getPageNum(), pageInfo.getPages(), pageInfo.getPageSize(), pageInfo.getList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CurrencyRateVO saveOrUpdateCurrencyRate(CurrencyRateSaveParam param) {
        // 检查币种和日期是否重复
        if (checkCurrencyAndDateExists(param.getCurrencyCode(), param.getRateDate(),
                param.getBookId(), param.getTenantId(), param.getRateId())) {
            throw new RuntimeException("该币种在此日期的汇率已存在");
        }

        boolean success = false;

        // 使用Oracle数据库
        com.financial.sharing.oracle.entity.CurrencyRateEntity oracleEntity =
            new com.financial.sharing.oracle.entity.CurrencyRateEntity();
        BeanUtils.copyProperties(param, oracleEntity);

        // 设置默认值
        if (oracleEntity.getIsEnabled() == null) {
            oracleEntity.setIsEnabled(1);
        }
        if (oracleEntity.getIsBaseCurrency() == null) {
            oracleEntity.setIsBaseCurrency(0);
        }

        // 如果设置为本位币，需要先清除其他本位币标识
        if (oracleEntity.getIsBaseCurrency() != null && oracleEntity.getIsBaseCurrency() == 1) {
            clearBaseCurrency(param.getBookId(), param.getTenantId());
        }
        if (param.getRateId() == null) {
            success = dateBaseConfig.getOracleCurrencyRateMapper().insert(oracleEntity) > 0;
        } else {
            success = dateBaseConfig.getOracleCurrencyRateMapper().updateById(oracleEntity) > 0;
        }
        if (success) {
            param.setRateId(oracleEntity.getRateId());
        }
        if (!success) {
            throw new RuntimeException("保存币种汇率失败");
        }
        return getCurrencyRateById(param.getRateId());
    }

    @Override
    public CurrencyRateVO getCurrencyRateById(Long rateId) {
        com.financial.sharing.oracle.entity.CurrencyRateEntity oracleEntity =
            dateBaseConfig.getOracleCurrencyRateMapper().selectById(rateId);
        if (oracleEntity == null) {
            return null;
        }

        // 手动映射字段，避免BeanUtils.copyProperties的问题
        CurrencyRateVO vo = new CurrencyRateVO();
        vo.setRateId(oracleEntity.getRateId());
        vo.setCurrencyCode(oracleEntity.getCurrencyCode());
        vo.setCurrencyName(oracleEntity.getCurrencyName());
        vo.setRateType(oracleEntity.getRateType());
        vo.setExchangeRate(oracleEntity.getExchangeRate());
        vo.setRateDate(oracleEntity.getRateDate());
        vo.setIsBaseCurrency(oracleEntity.getIsBaseCurrency());
        vo.setIsEnabled(oracleEntity.getIsEnabled());
        vo.setBookId(oracleEntity.getBookId());
        vo.setTenantId(oracleEntity.getTenantId());
        vo.setVersion(oracleEntity.getVersion());
        vo.setCreateTime(oracleEntity.getCreateTime());
        vo.setUpdateTime(oracleEntity.getUpdateTime());
        vo.setCreator(oracleEntity.getCreator());
        vo.setUpdater(oracleEntity.getUpdater());

        setRateTypeName(vo);
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteCurrencyRate(Long rateId) {
        // 检查是否为本位币
        CurrencyRateVO currencyRate = getCurrencyRateById(rateId);
        if (currencyRate != null && currencyRate.getIsBaseCurrency() != null && currencyRate.getIsBaseCurrency() == 1) {
            throw new RuntimeException("本位币不能删除");
        }

        boolean success = dateBaseConfig.getOracleCurrencyRateMapper().deleteById(rateId) > 0;

        return success;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchDeleteCurrencyRates(List<Long> rateIds) {
        if (Java8Collections.isEmpty(rateIds)) {
            return false;
        }

        int result = dateBaseConfig.getOracleCurrencyRateMapper().batchDelete(rateIds, null);

        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateCurrencyRateStatus(Long rateId, Integer isEnabled) {
        int result = dateBaseConfig.getOracleCurrencyRateMapper().batchUpdateStatus(
            Java8Collections.listOf(rateId), isEnabled, null);

        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean batchUpdateCurrencyRateStatus(List<Long> rateIds, Integer isEnabled) {
        if (Java8Collections.isEmpty(rateIds)) {
            return false;
        }

        int result = dateBaseConfig.getOracleCurrencyRateMapper().batchUpdateStatus(rateIds, isEnabled, null);

        return result > 0;
    }

    @Override
    public boolean checkCurrencyAndDateExists(String currencyCode, LocalDate rateDate, Long bookId, Long tenantId, Long excludeId) {
        com.financial.sharing.oracle.entity.CurrencyRateEntity oracleEntity =
            dateBaseConfig.getOracleCurrencyRateMapper().selectByCurrencyAndDate(
                currencyCode, rateDate, bookId, tenantId, excludeId);
        return oracleEntity != null;
    }

    @Override
    public CurrencyRateVO getLatestRate(String currencyCode, Long bookId, Long tenantId) {
        CurrencyRateVO vo = dateBaseConfig.getOracleCurrencyRateMapper().selectLatestRate(currencyCode, bookId, tenantId);

        if (vo != null) {
            setRateTypeName(vo);
        }
        return vo;
    }

    @Override
    public List<CurrencyRateVO> getCurrencyRatesByDateRange(String currencyCode, LocalDate startDate, LocalDate endDate, Long bookId, Long tenantId) {
        List<CurrencyRateVO> list = dateBaseConfig.getOracleCurrencyRateMapper().selectByDateRange(currencyCode, startDate, endDate, bookId, tenantId);

        if (!Java8Collections.isEmpty(list)) {
            list.forEach(this::setRateTypeName);
        }
        return list;
    }

    @Override
    public List<CurrencyRateVO> getEnabledCurrencies(Long bookId, Long tenantId) {
        List<CurrencyRateVO> list = dateBaseConfig.getOracleCurrencyRateMapper().selectEnabledCurrencies(bookId, tenantId);

        if (!Java8Collections.isEmpty(list)) {
            list.forEach(this::setRateTypeName);
        }
        return list;
    }

    @Override
    public CurrencyRateVO getBaseCurrency(Long bookId, Long tenantId) {
        CurrencyRateVO vo = dateBaseConfig.getOracleCurrencyRateMapper().selectBaseCurrency(bookId, tenantId);

        if (vo != null) {
            setRateTypeName(vo);
        }
        return vo;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean setBaseCurrency(Long rateId, Long bookId, Long tenantId) {
        // 先清除所有本位币标识
        clearBaseCurrency(bookId, tenantId);

        // 设置新的本位币
        int result = dateBaseConfig.getOracleCurrencyRateMapper().setBaseCurrency(rateId, bookId, tenantId, null);

        return result > 0;
    }

    @Override
    public List<String> getCurrencyCodes(Long bookId, Long tenantId) {
        return dateBaseConfig.getOracleCurrencyRateMapper().selectCurrencyCodes(bookId, tenantId);
    }

    @Override
    public BigDecimal convertCurrency(BigDecimal amount, String fromCurrency, String toCurrency, Long bookId, Long tenantId) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }
        if (fromCurrency.equals(toCurrency)) {
            return amount;
        }

        // 获取源币种汇率
        CurrencyRateVO fromRate = getLatestRate(fromCurrency, bookId, tenantId);
        if (fromRate == null) {
            throw new RuntimeException("未找到币种 " + fromCurrency + " 的汇率");
        }

        // 获取目标币种汇率
        CurrencyRateVO toRate = getLatestRate(toCurrency, bookId, tenantId);
        if (toRate == null) {
            throw new RuntimeException("未找到币种 " + toCurrency + " 的汇率");
        }

        // 通过本位币进行转换：原币种 -> 本位币 -> 目标币种
        BigDecimal baseAmount = amount.multiply(fromRate.getExchangeRate());
        BigDecimal targetAmount = baseAmount.divide(toRate.getExchangeRate(), 6, RoundingMode.HALF_UP);

        return targetAmount;
    }

    /**
     * 设置汇率类型名称
     */
    private void setRateTypeName(CurrencyRateVO vo) {
        if (vo != null && vo.getRateType() != null) {
            vo.setRateTypeName(RATE_TYPE_NAME_MAP.getOrDefault(vo.getRateType(), "未知"));
        }
    }

    /**
     * 清除本位币标识
     */
    private void clearBaseCurrency(Long bookId, Long tenantId) {
        dateBaseConfig.getOracleCurrencyRateMapper().clearBaseCurrency(bookId, tenantId, null);
    }
}

