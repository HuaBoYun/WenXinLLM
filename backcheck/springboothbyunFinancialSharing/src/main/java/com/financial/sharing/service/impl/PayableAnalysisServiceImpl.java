package com.financial.sharing.service.impl;

import com.financial.sharing.dto.param.PayableAnalysisQueryParam;
import com.financial.sharing.oracle.mapper.PayableBillMapper;
import com.financial.sharing.oracle.mapper.PaymentOrderMapper;
import com.financial.sharing.oracle.mapper.SupplierMapper;
import com.financial.sharing.service.PayableAnalysisService;
import com.financial.sharing.vo.result.PayableAnalysisVO;
import com.financial.sharing.vo.result.PayableSummaryVO;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * 应付分析Service实现类 (使用Oracle数据库)
 * @author system
 * @since 2025-01-05
 */
@Service
public class PayableAnalysisServiceImpl implements PayableAnalysisService {

    @Resource(name = "oraclePayableDocumentMapper")
    private com.financial.sharing.oracle.mapper.PayableDocumentMapper payableDocumentMapper;
    @Resource
    private PaymentOrderMapper paymentOrderMapper;
    @Resource
    private SupplierMapper supplierMapper;
    @Resource
    private PayableBillMapper payableBillMapper;

    @Override
    public PayableSummaryVO getSummary() {
        PayableSummaryVO vo = new PayableSummaryVO();
        Map<String, BigDecimal> payableSummary = payableDocumentMapper.selectPayableSummary(null);
        vo.setTotalPayable(payableSummary.getOrDefault("totalPayable", BigDecimal.ZERO));
        vo.setTotalPaid(payableSummary.getOrDefault("totalPaid", BigDecimal.ZERO));
        vo.setTotalUnpaid(payableSummary.getOrDefault("totalUnpaid", BigDecimal.ZERO));

        // 暂时不查询票据数据，因为票据表在 MySQL 中，当前系统使用达梦数据库
        // TODO: 如果需要票据统计，需要创建 Oracle 版本的 PayableBillMapper
        vo.setTotalBillAmount(BigDecimal.ZERO);

        return vo;
    }

    @Override
    public List<PayableAnalysisVO.AgingAnalysisItem> getAgingAnalysis(PayableAnalysisQueryParam param) {
        // 从数据库查询真实账龄数据
        List<Map<String, Object>> agingData = payableDocumentMapper.selectAgingAnalysis();

        // 计算总金额用于计算百分比
        BigDecimal totalAmount = agingData.stream()
            .map(item -> (BigDecimal) item.get("AMOUNT"))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 转换为VO对象
        List<PayableAnalysisVO.AgingAnalysisItem> result = new ArrayList<>();
        for (Map<String, Object> item : agingData) {
            PayableAnalysisVO.AgingAnalysisItem vo = new PayableAnalysisVO.AgingAnalysisItem();
            vo.setAgingRange((String) item.get("AGINGRANGE"));
            vo.setAmount((BigDecimal) item.get("AMOUNT"));
            vo.setCount((Integer) item.get("COUNT"));

            // 计算百分比
            if (totalAmount.compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal percentage = ((BigDecimal) item.get("AMOUNT"))
                    .divide(totalAmount, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
                vo.setPercentage(percentage);
            } else {
                vo.setPercentage(BigDecimal.ZERO);
            }

            result.add(vo);
        }
        return result;
    }

    @Override
    public List<PayableAnalysisVO.SupplierDistributionItem> getSupplierDistribution(PayableAnalysisQueryParam param) {
        List<PayableAnalysisVO.SupplierDistributionItem> result = new ArrayList<>();
        return result;
    }

    @Override
    public List<PayableAnalysisVO.TrendItem> getTrendAnalysis(PayableAnalysisQueryParam param) {
        List<PayableAnalysisVO.TrendItem> result = new ArrayList<>();
        String[] months = {"2024-07", "2024-08", "2024-09", "2024-10", "2024-11", "2024-12"};
        for (String month : months) {
            PayableAnalysisVO.TrendItem item = new PayableAnalysisVO.TrendItem();
            item.setPeriod(month);
            item.setPayableAmount(BigDecimal.valueOf(Math.random() * 100000).setScale(2, RoundingMode.HALF_UP));
            item.setPaymentAmount(BigDecimal.valueOf(Math.random() * 80000).setScale(2, RoundingMode.HALF_UP));
            result.add(item);
        }
        return result;
    }

    /**
     * 获取结构分析数据(用于饼图展示)
     * @param param 查询参数
     * @return 结构分析数据列表
     */
    public List<Map<String, Object>> getStructureAnalysis(PayableAnalysisQueryParam param) {
        List<Map<String, Object>> result = new ArrayList<>();

        // 根据维度返回不同的结构数据
        int dimension = param != null && param.getDimension() != null ? param.getDimension() : 1;

        if (dimension == 1) {
            // 按业务类型统计
            String[] categories = {"原材料采购", "设备采购", "服务费用", "其他费用"};
            BigDecimal[] amounts = {
                BigDecimal.valueOf(3200000),
                BigDecimal.valueOf(2800000),
                BigDecimal.valueOf(1960000),
                BigDecimal.valueOf(1000000)
            };
            double[] growthRates = {12.5, 8.3, -2.1, 15.8};

            BigDecimal total = BigDecimal.ZERO;
            for (BigDecimal amount : amounts) {
                total = total.add(amount);
            }

            for (int i = 0; i < categories.length; i++) {
                Map<String, Object> item = new HashMap<>();
                item.put("category", categories[i]);
                item.put("amount", amounts[i]);
                item.put("percentage", amounts[i].divide(total, 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP));
                item.put("growth", growthRates[i]);
                item.put("rank", i + 1);
                result.add(item);
            }
        } else if (dimension == 2) {
            // 按时间统计
            String[] categories = {"2024-07", "2024-08", "2024-09", "2024-10", "2024-11", "2024-12"};
            for (int i = 0; i < categories.length; i++) {
                Map<String, Object> item = new HashMap<>();
                BigDecimal amount = BigDecimal.valueOf(800000 + Math.random() * 400000)
                        .setScale(2, RoundingMode.HALF_UP);
                item.put("category", categories[i]);
                item.put("amount", amount);
                item.put("percentage", BigDecimal.valueOf(100.0 / categories.length).setScale(2, RoundingMode.HALF_UP));
                item.put("growth", (Math.random() * 20 - 5));
                item.put("rank", i + 1);
                result.add(item);
            }
        } else if (dimension == 3) {
            // 按供应商统计(取TOP10)
            String[] suppliers = {"供应商A", "供应商B", "供应商C", "供应商D", "供应商E",
                                  "供应商F", "供应商G", "供应商H", "供应商I", "供应商J"};
            BigDecimal total = BigDecimal.valueOf(8960000);

            for (int i = 0; i < suppliers.length; i++) {
                Map<String, Object> item = new HashMap<>();
                BigDecimal amount = BigDecimal.valueOf(500000 + Math.random() * 800000)
                        .setScale(2, RoundingMode.HALF_UP);
                item.put("category", suppliers[i]);
                item.put("amount", amount);
                item.put("percentage", amount.divide(total, 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP));
                item.put("growth", (Math.random() * 30 - 10));
                item.put("rank", i + 1);
                result.add(item);
            }
        } else {
            // 按账龄统计
            String[] agingRanges = {"30天内", "31-60天", "61-90天", "91-180天", "180天以上"};
            BigDecimal total = BigDecimal.valueOf(8960000);

            for (int i = 0; i < agingRanges.length; i++) {
                Map<String, Object> item = new HashMap<>();
                BigDecimal amount = BigDecimal.valueOf(500000 + Math.random() * 2000000)
                        .setScale(2, RoundingMode.HALF_UP);
                item.put("category", agingRanges[i]);
                item.put("amount", amount);
                item.put("percentage", amount.divide(total, 4, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP));
                item.put("growth", 0.0); // 账龄维度不计算增长率
                item.put("rank", i + 1);
                result.add(item);
            }
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> getPaymentPlan(PayableAnalysisQueryParam param) {
        return new ArrayList<>();
    }

    @Override
    public Map<String, Object> getOverdueAnalysis() {
        Map<String, Object> result = new HashMap<>();
        result.put("overdueCount", payableDocumentMapper.selectOverdueList().size());
        result.put("overdueAmount", BigDecimal.ZERO);
        return result;
    }

    @Override
    public List<Map<String, Object>> getSupplierRanking(PayableAnalysisQueryParam param) {
        return new ArrayList<>();
    }

    @Override
    public List<Map<String, Object>> getBusinessTypeDistribution() {
        List<Map<String, Object>> result = new ArrayList<>();
        String[] types = {"采购应付", "费用应付", "其他应付"};
        for (int i = 0; i < types.length; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("businessType", i + 1);
            item.put("businessTypeName", types[i]);
            item.put("amount", BigDecimal.valueOf(Math.random() * 50000).setScale(2, RoundingMode.HALF_UP));
            result.add(item);
        }
        return result;
    }

    @Override
    public Map<String, Object> getBillStatistics() {
        Map<String, Object> result = new HashMap<>();
        Map<String, BigDecimal> summary = payableBillMapper.selectBillSummary();
        result.putAll(summary);
        result.put("dueSoonList", payableBillMapper.selectDueSoonList(7));
        return result;
    }

    @Override
    public String exportReport(PayableAnalysisQueryParam param) {
        return "/export/payable_report_" + System.currentTimeMillis() + ".xlsx";
    }

    @Override
    public List<Map<String, Object>> getCashFlowForecast(PayableAnalysisQueryParam param) {
        // 获取预测月数,默认6个月
        Integer forecastMonths = param != null && param.getMonths() != null ? param.getMonths() : 6;

        // 获取历史数据(最近12个月)
        List<Map<String, Object>> historyData = payableDocumentMapper.selectMonthlyPaymentTrend(12);

        // 如果没有历史数据,返回空列表
        if (historyData.isEmpty()) {
            return new ArrayList<>();
        }

        // 计算平均付款金额和标准差(用于预测区间)
        BigDecimal avgPayment = historyData.stream()
            .map(item -> (BigDecimal) item.get("PAYMENTAMOUNT"))
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .divide(BigDecimal.valueOf(historyData.size()), 2, RoundingMode.HALF_UP);

        // 计算标准差
        BigDecimal variance = historyData.stream()
            .map(item -> {
                BigDecimal amount = (BigDecimal) item.get("PAYMENTAMOUNT");
                return amount.subtract(avgPayment).pow(2);
            })
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .divide(BigDecimal.valueOf(historyData.size()), 2, RoundingMode.HALF_UP);

        BigDecimal stdDev = BigDecimal.valueOf(Math.sqrt(variance.doubleValue()));

        // 生成未来N个月的预测
        List<Map<String, Object>> forecastData = new ArrayList<>();
        Calendar cal = Calendar.getInstance();

        for (int i = 1; i <= forecastMonths; i++) {
            cal.add(Calendar.MONTH, 1);
            String period = String.format("%d-%02d", cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1);

            // 预测值 = 平均值(可以使用更复杂的算法)
            BigDecimal expectedPayment = avgPayment;

            // 置信区间(95%置信度: ±1.96倍标准差)
            BigDecimal upperBound = expectedPayment.add(stdDev.multiply(BigDecimal.valueOf(1.96)));
            BigDecimal lowerBound = expectedPayment.subtract(stdDev.multiply(BigDecimal.valueOf(1.96)));
            if (lowerBound.compareTo(BigDecimal.ZERO) < 0) {
                lowerBound = BigDecimal.ZERO;
            }

            // 置信度(根据预测月数递减)
            int confidence = Math.max(70, 95 - (i - 1) * 3);

            Map<String, Object> item = new HashMap<>();
            item.put("period", period);
            item.put("expectedPayment", expectedPayment.setScale(2, RoundingMode.HALF_UP));
            item.put("upperBound", upperBound.setScale(2, RoundingMode.HALF_UP));
            item.put("lowerBound", lowerBound.setScale(2, RoundingMode.HALF_UP));
            item.put("confidence", confidence);

            forecastData.add(item);
        }
        return forecastData;
    }
}

