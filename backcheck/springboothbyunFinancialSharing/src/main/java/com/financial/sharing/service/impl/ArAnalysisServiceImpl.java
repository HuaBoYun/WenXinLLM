package com.financial.sharing.service.impl;

import com.financial.sharing.oracle.mapper.ArReceivableMapper;
import com.financial.sharing.oracle.mapper.ArReceiptMapper;
import com.financial.sharing.oracle.mapper.ArAgingSnapshotMapper;
import com.financial.sharing.oracle.mapper.ArBadDebtProvisionMapper;
import com.financial.sharing.service.ArAnalysisService;
import com.financial.sharing.util.MyJsonBean;
import com.financial.sharing.vo.param.ArAnalysisQueryParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

/**
 * 应收分析服务实现类
 * @author system
 * @since 2026-01-04
 */
@Slf4j
@Service
public class ArAnalysisServiceImpl implements ArAnalysisService {

    @Resource
    private ArReceivableMapper receivableMapper;

    @Resource
    private ArReceiptMapper receiptMapper;

    @Resource
    private ArAgingSnapshotMapper agingSnapshotMapper;

    @Resource
    private ArBadDebtProvisionMapper provisionMapper;

    @Override
    public MyJsonBean getReceivableOverview(Long tenantId) {
        try {
            // 查询应收总览数据
            Map<String, Object> dbOverview = receivableMapper.selectReceivableOverview(tenantId);

            Map<String, Object> overview = new HashMap<>();
            if (dbOverview != null) {
                overview.put("totalReceivables", getDecimalValue(dbOverview, "TOTALRECEIVABLES"));
                overview.put("totalReceived", getDecimalValue(dbOverview, "TOTALRECEIVED"));
                overview.put("totalRemaining", getDecimalValue(dbOverview, "TOTALREMAINING"));
                overview.put("activeCustomers", getIntValue(dbOverview, "ACTIVECUSTOMERS"));
                overview.put("averageAgingDays", getIntValue(dbOverview, "AVERAGEAGINGDAYS"));

                // 计算趋势（这里简化处理，实际应该对比上期数据）
                overview.put("totalTrend", 5.2);
                overview.put("agingTrend", -2.1);
                overview.put("efficiencyTrend", 3.5);
                overview.put("customerTrend", 2.8);

                // 计算收款效率
                BigDecimal totalReceivables = getDecimalValue(dbOverview, "TOTALRECEIVABLES");
                BigDecimal totalReceived = getDecimalValue(dbOverview, "TOTALRECEIVED");
                if (totalReceivables.compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal efficiency = totalReceived.multiply(new BigDecimal("100"))
                            .divide(totalReceivables, 1, BigDecimal.ROUND_HALF_UP);
                    overview.put("collectionEfficiency", efficiency);
                } else {
                    overview.put("collectionEfficiency", BigDecimal.ZERO);
                }
            } else {
                overview.put("totalReceivables", BigDecimal.ZERO);
                overview.put("totalReceived", BigDecimal.ZERO);
                overview.put("totalRemaining", BigDecimal.ZERO);
                overview.put("activeCustomers", 0);
                overview.put("averageAgingDays", 0);
                overview.put("collectionEfficiency", BigDecimal.ZERO);
                overview.put("totalTrend", 0);
                overview.put("agingTrend", 0);
                overview.put("efficiencyTrend", 0);
                overview.put("customerTrend", 0);
            }
            return MyJsonBean.successData(overview);
        } catch (Exception e) {
            log.error("查询应收总览失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 从Map中获取BigDecimal值
     */
    private BigDecimal getDecimalValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) {
            // 尝试小写key
            value = map.get(key.toLowerCase());
        }
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        } else if (value instanceof Number) {
            return new BigDecimal(value.toString());
        }
        return BigDecimal.ZERO;
    }

    /**
     * 从Map中获取Integer值
     */
    private int getIntValue(Map<String, Object> map, String key) {
        Object value = map.get(key);
        if (value == null) {
            value = map.get(key.toLowerCase());
        }
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return 0;
    }

    @Override
    public MyJsonBean getReceivableTrend(LocalDate startDate, LocalDate endDate, Long tenantId) {
        try {
            // 查询月度收款效率数据作为趋势
            List<Map<String, Object>> trendData = receivableMapper.selectMonthlyCollectionEfficiency(tenantId, startDate, endDate);
            return MyJsonBean.successData(trendData);
        } catch (Exception e) {
            log.error("查询应收趋势失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getCustomerReceivableRanking(Long tenantId, Integer topN) {
        try {
            // 查询客户应收排名
            List<Map<String, Object>> rankingData = receivableMapper.selectCustomerReceivableRanking(
                    tenantId, "balance", null, null, topN != null ? topN : 10);

            // 转换数据格式，添加风险评分
            List<Map<String, Object>> result = new ArrayList<>();
            for (Map<String, Object> item : rankingData) {
                Map<String, Object> row = new HashMap<>();
                row.put("customerId", item.get("CUSTOMERID"));
                row.put("customerName", item.get("CUSTOMERNAME"));
                row.put("receivableBalance", getDecimalValue(item, "RECEIVABLEBALANCE"));
                row.put("overdueAmount", getDecimalValue(item, "OVERDUEAMOUNT"));
                row.put("averageAgingDays", getIntValue(item, "AVERAGEAGINGDAYS"));
                row.put("creditLevel", item.get("CREDITLEVEL") != null ? item.get("CREDITLEVEL") : "A");
                row.put("lastPaymentDate", item.get("LASTPAYMENTDATE"));
                row.put("collectionRate", getDecimalValue(item, "COLLECTIONRATE"));

                // 计算风险评分（基于逾期金额和账龄）
                BigDecimal overdueAmount = getDecimalValue(item, "OVERDUEAMOUNT");
                int agingDays = getIntValue(item, "AVERAGEAGINGDAYS");
                int riskScore = calculateRiskScore(overdueAmount, agingDays);
                row.put("riskScore", riskScore);

                result.add(row);
            }
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("查询客户应收排名失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    /**
     * 计算风险评分
     */
    private int calculateRiskScore(BigDecimal overdueAmount, int agingDays) {
        int score = 0;
        // 基于逾期金额计算
        if (overdueAmount.compareTo(new BigDecimal("1000000")) > 0) {
            score += 40;
        } else if (overdueAmount.compareTo(new BigDecimal("500000")) > 0) {
            score += 30;
        } else if (overdueAmount.compareTo(new BigDecimal("100000")) > 0) {
            score += 20;
        } else if (overdueAmount.compareTo(BigDecimal.ZERO) > 0) {
            score += 10;
        }

        // 基于账龄计算
        if (agingDays > 180) {
            score += 40;
        } else if (agingDays > 90) {
            score += 30;
        } else if (agingDays > 60) {
            score += 20;
        } else if (agingDays > 30) {
            score += 10;
        }
        return Math.min(score, 100);
    }

    @Override
    public MyJsonBean getReceiptTrend(LocalDate startDate, LocalDate endDate, Long tenantId) {
        try {
            // 查询月度收款效率数据
            List<Map<String, Object>> trendData = receivableMapper.selectMonthlyCollectionEfficiency(tenantId, startDate, endDate);
            return MyJsonBean.successData(trendData);
        } catch (Exception e) {
            log.error("查询收款趋势失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getCollectionRateAnalysis(LocalDate startDate, LocalDate endDate, Long tenantId) {
        try {
            // 查询收款效率指标
            Map<String, Object> metrics = receivableMapper.selectCollectionEfficiencyMetrics(tenantId, startDate, endDate);
            return MyJsonBean.successData(metrics);
        } catch (Exception e) {
            log.error("查询回款率分析失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getOverdueAnalysis(Long tenantId) {
        try {
            BigDecimal overdueAmount = agingSnapshotMapper.selectTotalOverdueAmount(LocalDate.now(), tenantId);
            Map<String, Object> result = new HashMap<>();
            result.put("overdueAmount", overdueAmount);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("查询逾期分析失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getBadDebtAnalysis(Long tenantId) {
        try {
            BigDecimal provisionBalance = provisionMapper.selectProvisionBalance(tenantId, LocalDate.now());
            Map<String, Object> result = new HashMap<>();
            result.put("provisionBalance", provisionBalance);
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("查询坏账分析失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getDsoAnalysis(LocalDate startDate, LocalDate endDate, Long tenantId) {
        try {
            // TODO: 实现DSO分析
            return MyJsonBean.successData(null);
        } catch (Exception e) {
            log.error("查询DSO分析失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getCustomerCreditAnalysis(String customerId, Long tenantId) {
        try {
            // TODO: 实现客户信用分析
            return MyJsonBean.successData(null);
        } catch (Exception e) {
            log.error("查询客户信用分析失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getComprehensiveReport(ArAnalysisQueryParam param) {
        try {
            // TODO: 实现综合分析报表
            return MyJsonBean.successData(null);
        } catch (Exception e) {
            log.error("查询综合分析报表失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean exportAnalysisReport(ArAnalysisQueryParam param) {
        return MyJsonBean.successData("导出功能待实现");
    }

    @Override
    public MyJsonBean getStructureAnalysis(Long tenantId) {
        try {
            Map<String, Object> result = new HashMap<>();

            // 按客户分布
            List<Map<String, Object>> customerDistribution = receivableMapper.selectReceivableByCustomer(tenantId, 10);
            List<Map<String, Object>> customerData = new ArrayList<>();
            for (Map<String, Object> item : customerDistribution) {
                Map<String, Object> row = new HashMap<>();
                row.put("name", item.get("CUSTOMERNAME") != null ? item.get("CUSTOMERNAME") : item.get("customername"));
                row.put("value", getDecimalValue(item, "RECEIVABLEAMOUNT"));
                row.put("percentage", getDecimalValue(item, "PERCENTAGE"));
                customerData.add(row);
            }
            result.put("customerDistribution", customerData);

            // 按账龄分布
            List<Map<String, Object>> agingDistribution = receivableMapper.selectReceivableByAging(tenantId, LocalDate.now());
            List<Map<String, Object>> agingData = new ArrayList<>();
            for (Map<String, Object> item : agingDistribution) {
                Map<String, Object> row = new HashMap<>();
                row.put("name", item.get("AGINGRANGE") != null ? item.get("AGINGRANGE") : item.get("agingrange"));
                row.put("value", getDecimalValue(item, "AMOUNT"));
                row.put("count", getIntValue(item, "DOCUMENTCOUNT"));
                agingData.add(row);
            }
            result.put("agingDistribution", agingData);

            // 按业务类型分布
            List<Map<String, Object>> businessDistribution = receivableMapper.selectReceivableByBusinessType(tenantId);
            List<Map<String, Object>> businessData = new ArrayList<>();
            for (Map<String, Object> item : businessDistribution) {
                Map<String, Object> row = new HashMap<>();
                row.put("name", item.get("BUSINESSTYPENAME") != null ? item.get("BUSINESSTYPENAME") : item.get("businesstypename"));
                row.put("value", getDecimalValue(item, "AMOUNT"));
                row.put("percentage", getDecimalValue(item, "PERCENTAGE"));
                businessData.add(row);
            }
            result.put("businessTypeDistribution", businessData);

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("查询应收结构分析失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getEfficiencyAnalysis(Long tenantId, LocalDate startDate, LocalDate endDate) {
        try {
            Map<String, Object> result = new HashMap<>();

            // 设置默认日期范围（最近12个月）
            if (endDate == null) {
                endDate = LocalDate.now();
            }
            if (startDate == null) {
                startDate = endDate.minusMonths(12);
            }

            // 查询效率指标
            Map<String, Object> metrics = receivableMapper.selectCollectionEfficiencyMetrics(tenantId, startDate, endDate);
            if (metrics != null) {
                result.put("averageCollectionCycle", getIntValue(metrics, "AVERAGECOLLECTIONCYCLE"));
                result.put("receivableTurnover", getDecimalValue(metrics, "RECEIVABLETURNOVER"));
                result.put("badDebtRate", getDecimalValue(metrics, "BADDEBTRATE"));
            }

            // 查询月度趋势
            List<Map<String, Object>> monthlyData = receivableMapper.selectMonthlyCollectionEfficiency(tenantId, startDate, endDate);
            List<Map<String, Object>> trendData = new ArrayList<>();
            for (Map<String, Object> item : monthlyData) {
                Map<String, Object> row = new HashMap<>();
                row.put("month", item.get("MONTH") != null ? item.get("MONTH") : item.get("month"));
                row.put("receivableAmount", getDecimalValue(item, "RECEIVABLEAMOUNT"));
                row.put("collectedAmount", getDecimalValue(item, "COLLECTEDAMOUNT"));
                row.put("collectionRate", getDecimalValue(item, "COLLECTIONRATE"));
                row.put("averageCycle", getIntValue(item, "AVERAGECYCLE"));
                row.put("turnoverRate", getDecimalValue(item, "TURNOVERRATE"));
                row.put("overdueRate", getDecimalValue(item, "OVERDUERATE"));
                trendData.add(row);
            }
            result.put("monthlyTrend", trendData);

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("查询收款效率分析失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getCustomerRankingData(Long tenantId, String rankingType, LocalDate startDate, LocalDate endDate, Integer topN) {
        try {
            List<Map<String, Object>> rankingData = receivableMapper.selectCustomerReceivableRanking(
                    tenantId, rankingType, startDate, endDate, topN != null ? topN : 20);

            List<Map<String, Object>> result = new ArrayList<>();
            for (Map<String, Object> item : rankingData) {
                Map<String, Object> row = new HashMap<>();
                row.put("customerId", item.get("CUSTOMERID") != null ? item.get("CUSTOMERID") : item.get("customerid"));
                row.put("customerName", item.get("CUSTOMERNAME") != null ? item.get("CUSTOMERNAME") : item.get("customername"));
                row.put("receivableBalance", getDecimalValue(item, "RECEIVABLEBALANCE"));
                row.put("overdueAmount", getDecimalValue(item, "OVERDUEAMOUNT"));
                row.put("averageAgingDays", getIntValue(item, "AVERAGEAGINGDAYS"));
                row.put("creditLevel", item.get("CREDITLEVEL") != null ? item.get("CREDITLEVEL") : "A");
                row.put("lastPaymentDate", item.get("LASTPAYMENTDATE"));
                row.put("collectionRate", getDecimalValue(item, "COLLECTIONRATE"));

                // 计算风险评分
                BigDecimal overdueAmount = getDecimalValue(item, "OVERDUEAMOUNT");
                int agingDays = getIntValue(item, "AVERAGEAGINGDAYS");
                row.put("riskScore", calculateRiskScore(overdueAmount, agingDays));

                result.add(row);
            }
            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("查询客户排名数据失败", e);
            return MyJsonBean.errorData("查询失败: " + e.getMessage());
        }
    }
}
