package com.global.treasurer.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.global.treasurer.dto.FinancingCostDTO;
import com.global.treasurer.dto.FinancingCostQueryDTO;
import com.global.treasurer.entity.TblFinancingCostAnalysis;
import com.global.treasurer.exception.ServiceException;
import com.global.treasurer.mapper.FinancingCostMapper;
import com.global.treasurer.service.FinancingCostService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 融资成本分析服务实现类
 *
 * @author 华博云开发团队
 * @since 2025-01-13
 */
@Service
public class FinancingCostServiceImpl implements FinancingCostService {
    @Autowired
    private FinancingCostMapper financingCostMapper;

    @Override
    public PageInfo<TblFinancingCostAnalysis> getCostList(FinancingCostQueryDTO queryDTO) {
        PageHelper.startPage(queryDTO.getPageNum(), queryDTO.getPageSize());
        Map<String, Object> params = new HashMap<>();
        params.put("financingId", queryDTO.getFinancingId());
        params.put("financingType", queryDTO.getFinancingType());
        params.put("currencyCode", queryDTO.getCurrencyCode());
        params.put("companyId", queryDTO.getCompanyId());
        params.put("periodType", queryDTO.getPeriodType());
        params.put("periodValue", queryDTO.getPeriodValue());
        params.put("startDate", queryDTO.getStartDate());
        params.put("endDate", queryDTO.getEndDate());
        params.put("minCostRate", queryDTO.getMinCostRate());
        params.put("maxCostRate", queryDTO.getMaxCostRate());
        List<TblFinancingCostAnalysis> list = financingCostMapper.selectCostList(params);
        return new PageInfo<>(list);
    }

    @Override
    public TblFinancingCostAnalysis getCostById(Long analysisId) {
        TblFinancingCostAnalysis cost = financingCostMapper.selectCostById(analysisId);
        if (cost == null) {
            throw new ServiceException(404, "融资成本分析记录不存在");
        }
        return cost;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblFinancingCostAnalysis saveCost(FinancingCostDTO dto) {
        TblFinancingCostAnalysis cost = new TblFinancingCostAnalysis();
        BeanUtils.copyProperties(dto, cost);

        // 计算总成本
        BigDecimal totalCost = calculateTotalCost(dto);
        cost.setTotalCost(totalCost);

        // 计算成本率
        if (dto.getPrincipalAmount() != null && dto.getPrincipalAmount().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal costRate = totalCost.divide(dto.getPrincipalAmount(), 4, BigDecimal.ROUND_HALF_UP)
                    .multiply(new BigDecimal("100"));
            cost.setCostRate(costRate);
        }

        if (dto.getAnalysisId() == null) {
            cost.setCreatedTime(new Date());
            financingCostMapper.insert(cost);
        } else {
            financingCostMapper.updateById(cost);
        }
        return cost;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCost(Long analysisId) {
        TblFinancingCostAnalysis cost = getCostById(analysisId);
        financingCostMapper.deleteById(cost.getAnalysisId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteCosts(List<Long> analysisIds) {
        for (Long id : analysisIds) {
            financingCostMapper.deleteById(id);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TblFinancingCostAnalysis performAnalysis(Long financingId, String analysisDate, String periodType) {
        FinancingCostDTO dto = new FinancingCostDTO();
        dto.setFinancingId(financingId);
        dto.setFinancingType("BANK_LOAN"); // 默认类型，实际应查询获取
        dto.setPrincipalAmount(BigDecimal.ZERO);
        dto.setInterestExpense(BigDecimal.ZERO);
        dto.setFeeCost(BigDecimal.ZERO);
        dto.setGuaranteeCost(BigDecimal.ZERO);
        dto.setOtherCost(BigDecimal.ZERO);
        dto.setCurrencyCode("CNY");

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            dto.setAnalysisDate(sdf.parse(analysisDate));
        } catch (Exception e) {
            throw new ServiceException(400, "日期格式错误");
        }

        dto.setPeriodType(periodType);
        dto.setPeriodValue(calculatePeriodValue(analysisDate, periodType));

        return saveCost(dto);
    }

    @Override
    public Map<String, Object> getCostStatistics(Long companyId, String periodType, String periodValue) {
        Map<String, Object> statistics = financingCostMapper.selectCostStatistics(companyId, periodType, periodValue);
        if (statistics == null) {
            statistics = new HashMap<>();
        }

        // 确保所有字段都有默认值，兼容大小写字段名（达梦数据库可能返回大写）
        ensureField(statistics, "totalCount", 0);
        ensureField(statistics, "totalPrincipal", BigDecimal.ZERO);
        ensureField(statistics, "totalInterest", BigDecimal.ZERO);
        ensureField(statistics, "totalFee", BigDecimal.ZERO);
        ensureField(statistics, "totalGuarantee", BigDecimal.ZERO);
        ensureField(statistics, "totalOther", BigDecimal.ZERO);
        ensureField(statistics, "totalCost", BigDecimal.ZERO);
        ensureField(statistics, "avgCostRate", BigDecimal.ZERO);
        ensureField(statistics, "maxCostRate", BigDecimal.ZERO);
        ensureField(statistics, "minCostRate", BigDecimal.ZERO);

        return statistics;
    }

    /**
     * 确保字段存在，兼容大小写
     */
    private void ensureField(Map<String, Object> map, String fieldName, Object defaultValue) {
        // 先检查小写
        if (map.containsKey(fieldName) && map.get(fieldName) != null) {
            return;
        }
        // 再检查大写
        String upperFieldName = fieldName.toUpperCase();
        if (map.containsKey(upperFieldName) && map.get(upperFieldName) != null) {
            map.put(fieldName, map.get(upperFieldName));
            return;
        }
        // 都没有则设置默认值
        map.put(fieldName, defaultValue);
    }

    @Override
    public List<Map<String, Object>> getCostComparison(Map<String, Object> params) {
        return financingCostMapper.selectCostComparison(params);
    }

    @Override
    public List<Map<String, Object>> getCostTrend(Long companyId, String startDate, String endDate) {
        return financingCostMapper.selectCostTrend(companyId, startDate, endDate);
    }

    @Override
    public List<TblFinancingCostAnalysis> exportCostReport(Map<String, Object> params) {
        return financingCostMapper.selectCostList(params);
    }

    /**
     * 计算总成本
     */
    private BigDecimal calculateTotalCost(FinancingCostDTO dto) {
        BigDecimal total = BigDecimal.ZERO;

        if (dto.getInterestExpense() != null) {
            total = total.add(dto.getInterestExpense());
        }
        if (dto.getFeeCost() != null) {
            total = total.add(dto.getFeeCost());
        }
        if (dto.getGuaranteeCost() != null) {
            total = total.add(dto.getGuaranteeCost());
        }
        if (dto.getOtherCost() != null) {
            total = total.add(dto.getOtherCost());
        }

        return total;
    }

    /**
     * 计算周期值
     */
    private String calculatePeriodValue(String dateStr, String periodType) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(dateStr);
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);

            if ("MONTH".equals(periodType)) {
                return cal.get(Calendar.YEAR) + "-" + String.format("%02d", cal.get(Calendar.MONTH) + 1);
            } else if ("QUARTER".equals(periodType)) {
                int quarter = (cal.get(Calendar.MONTH) / 3) + 1;
                return cal.get(Calendar.YEAR) + "-Q" + quarter;
            } else if ("YEAR".equals(periodType)) {
                return String.valueOf(cal.get(Calendar.YEAR));
            }
        } catch (Exception e) {
            // 默认返回当前年月
        }
        return new SimpleDateFormat("yyyy-MM").format(new Date());
    }
}

