package com.global.treasurer.service.impl;

import com.global.treasurer.mapper.BillInstrumentMapper;
import com.global.treasurer.mapper.CommercialBillMapper;
import com.global.treasurer.mapper.ElectronicBillMapper;
import com.global.treasurer.service.IBillStatisticsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 票据统计Service实现类
 *
 * 从 TBL_BILL_INSTRUMENT 表查询统计数据
 *
 * @author 华博云开发团队
 * @since 2025-12-29
 */
@Service
public class BillStatisticsServiceImpl implements IBillStatisticsService {
    private static final Logger log = LoggerFactory.getLogger(BillStatisticsServiceImpl.class);

    @Autowired(required = false)
    private BillInstrumentMapper billInstrumentMapper;

    @Autowired(required = false)
    private CommercialBillMapper commercialBillMapper;

    @Autowired(required = false)
    private ElectronicBillMapper electronicBillMapper;

    /**
     * 票据类型名称映射
     */
    private static final Map<String, String> BILL_TYPE_NAME_MAP = new HashMap<>();
    static {
        BILL_TYPE_NAME_MAP.put("BANK_ACCEPTANCE", "银行承兑汇票");
        BILL_TYPE_NAME_MAP.put("COMMERCIAL_ACCEPTANCE", "商业承兑汇票");
        BILL_TYPE_NAME_MAP.put("CHECK", "支票");
        BILL_TYPE_NAME_MAP.put("PROMISSORY_NOTE", "本票");
        BILL_TYPE_NAME_MAP.put("ELECTRONIC_BILL", "电子票据");
    }

    /**
     * 票据状态名称映射
     */
    private static final Map<String, String> BILL_STATUS_NAME_MAP = new HashMap<>();
    static {
        BILL_STATUS_NAME_MAP.put("HOLDING", "持有");
        BILL_STATUS_NAME_MAP.put("ENDORSED", "已背书");
        BILL_STATUS_NAME_MAP.put("DISCOUNTED", "已贴现");
        BILL_STATUS_NAME_MAP.put("MATURED", "已到期");
        BILL_STATUS_NAME_MAP.put("CANCELLED", "已作废");
        BILL_STATUS_NAME_MAP.put("NORMAL", "正常");
        BILL_STATUS_NAME_MAP.put("VALID", "有效");
        BILL_STATUS_NAME_MAP.put("ACTIVE", "活跃");
    }

    @Override
    public Map<String, Object> getStatisticsOverview(Map<String, Object> params) {
        log.info("获取统计概览, 参数: {}", params);
        Map<String, Object> result = new HashMap<>();

        try {
            // 查询票据总数和总金额
            if (billInstrumentMapper != null) {
                Map<String, Object> stats = billInstrumentMapper.getBillStatistics();
                if (stats != null) {
                    int totalCount = getIntValue(getMapValue(stats, "totalCount"));
                    BigDecimal totalAmount = getBigDecimalValue(getMapValue(stats, "totalAmount"));
                    int activeCount = getIntValue(getMapValue(stats, "activeCount"));

                    result.put("totalCount", totalCount);
                    // 转换为万元
                    result.put("totalAmount", totalAmount.divide(BigDecimal.valueOf(10000), 2, RoundingMode.HALF_UP));
                    result.put("avgAmount", getBigDecimalValue(getMapValue(stats, "avgAmount")));
                    result.put("maxAmount", getBigDecimalValue(getMapValue(stats, "maxAmount")));
                    result.put("minAmount", getBigDecimalValue(getMapValue(stats, "minAmount")));
                    result.put("activeCount", activeCount);

                    // 计算有效率
                    double activeRate = totalCount > 0 ? (activeCount * 100.0 / totalCount) : 0.0;
                    result.put("activeRate", Math.round(activeRate * 10.0) / 10.0);
                } else {
                    setDefaultStatistics(result);
                }

                // 计算月增长率
                double growthRate = calculateGrowthRate();
                result.put("growthRate", growthRate);

                // 获取统计表格数据
                List<Map<String, Object>> tableData = getStatisticsTableData(params);
                result.put("tlist", tableData);

            } else {
                setDefaultStatistics(result);
            }

        } catch (Exception e) {
            log.error("查询统计数据失败,使用默认数据", e);
            setDefaultStatistics(result);
        }

        return result;
    }

    /**
     * 计算月增长率
     */
    private double calculateGrowthRate() {
        try {
            if (billInstrumentMapper == null) {
                return 0.0;
            }

            Map<String, Object> lastMonth = billInstrumentMapper.getLastMonthStatistics();
            Map<String, Object> currentMonth = billInstrumentMapper.getCurrentMonthStatistics();

            if (lastMonth == null || currentMonth == null) {
                return 0.0;
            }

            int lastMonthCount = getIntValue(getMapValue(lastMonth, "totalCount"));
            int currentMonthCount = getIntValue(getMapValue(currentMonth, "totalCount"));

            if (lastMonthCount == 0) {
                return currentMonthCount > 0 ? 100.0 : 0.0;
            }

            double rate = ((currentMonthCount - lastMonthCount) * 100.0) / lastMonthCount;
            return Math.round(rate * 10.0) / 10.0;
        } catch (Exception e) {
            log.error("计算增长率失败", e);
            return 0.0;
        }
    }

    /**
     * 获取统计表格数据
     */
    private List<Map<String, Object>> getStatisticsTableData(Map<String, Object> params) {
        List<Map<String, Object>> tableData = new ArrayList<>();

        try {
            if (billInstrumentMapper == null) {
                return tableData;
            }

            Map<String, Object> queryParams = extractFilterParams(params);

            List<Map<String, Object>> rawData = billInstrumentMapper.getStatisticsTableData(queryParams);
            String dimension = (String) queryParams.get("dimension");

            if (rawData == null || rawData.isEmpty()) {
                return tableData;
            }

            // 计算总数用于百分比
            int totalCount = rawData.stream()
                    .mapToInt(item -> getIntValue(getMapValue(item, "count")))
                    .sum();

            for (Map<String, Object> item : rawData) {
                Map<String, Object> row = new HashMap<>();
                String category = getStringValue(item, "category");

                // 转换类别名称
                if ("billType".equals(dimension)) {
                    row.put("category", BILL_TYPE_NAME_MAP.getOrDefault(category, category));
                } else if ("billStatus".equals(dimension)) {
                    row.put("category", BILL_STATUS_NAME_MAP.getOrDefault(category, category));
                } else {
                    row.put("category", category != null ? category : "未知");
                }

                int count = getIntValue(getMapValue(item, "count"));
                BigDecimal amount = getBigDecimalValue(getMapValue(item, "amount"));
                BigDecimal avgAmount = getBigDecimalValue(getMapValue(item, "avgAmount"));

                row.put("count", count);
                row.put("amount", amount);
                row.put("avgAmount", avgAmount);
                row.put("percentage", totalCount > 0 ? Math.round(count * 1000.0 / totalCount) / 10.0 : 0);
                row.put("growth", 0); // 环比增长需要历史数据对比
                row.put("remark", "");

                tableData.add(row);
            }

        } catch (Exception e) {
            log.error("获取统计表格数据失败", e);
        }

        return tableData;
    }

    /**
     * 设置默认统计数据
     */
    private void setDefaultStatistics(Map<String, Object> result) {
        result.put("totalCount", 0);
        result.put("totalAmount", BigDecimal.ZERO);
        result.put("avgAmount", BigDecimal.ZERO);
        result.put("maxAmount", BigDecimal.ZERO);
        result.put("minAmount", BigDecimal.ZERO);
        result.put("activeCount", 0);
        result.put("activeRate", 0.0);
        result.put("growthRate", 0.0);
        result.put("tlist", new ArrayList<>());
    }

    @Override
    public Map<String, Object> getTypeStatistics(Map<String, Object> params) {
        log.info("获取类型统计, 参数: {}", params);
        Map<String, Object> result = new HashMap<>();

        try {
            Map<String, Object> queryParams = extractFilterParams(params);
            String dimension = (String) queryParams.get("dimension");
            List<Map<String, Object>> rawData = null;

            if (billInstrumentMapper != null) {
                // 使用支持筛选的 getStatisticsTableData 查询
                rawData = billInstrumentMapper.getStatisticsTableData(queryParams);
            }

            List<Map<String, Object>> typeList = new ArrayList<>();

            if (rawData != null && !rawData.isEmpty()) {
                for (Map<String, Object> item : rawData) {
                    Map<String, Object> typeData = new HashMap<>();
                    String category = getStringValue(item, "category");
                    String name;

                    switch (dimension) {
                        case "billStatus":
                            name = BILL_STATUS_NAME_MAP.getOrDefault(category, category);
                            break;
                        case "acceptingBank":
                            name = category != null ? category : "未知承兑人";
                            break;
                        case "billType":
                        default:
                            name = BILL_TYPE_NAME_MAP.getOrDefault(category, category);
                            break;
                    }

                    typeData.put("name", name != null ? name : "未知");
                    typeData.put("value", getIntValue(getMapValue(item, "count")));
                    typeData.put("amount", getBigDecimalValue(getMapValue(item, "amount"))
                            .divide(BigDecimal.valueOf(10000), 2, RoundingMode.HALF_UP));

                    typeList.add(typeData);
                }
            }

            result.put("typeList", typeList);

        } catch (Exception e) {
            log.error("获取类型统计失败", e);
            result.put("typeList", new ArrayList<>());
        }

        return result;
    }

    @Override
    public Map<String, Object> getTrendAnalysis(Map<String, Object> params) {
        log.info("获取趋势分析, 参数: {}", params);
        Map<String, Object> result = new HashMap<>();

        try {
            // 获取天数参数，默认30天
            int days = 30;
            if (params != null && params.get("period") != null) {
                String period = (String) params.get("period");
                switch (period) {
                    case "7D":
                        days = 7;
                        break;
                    case "30D":
                        days = 30;
                        break;
                    case "90D":
                        days = 90;
                        break;
                    case "1Y":
                        days = 365;
                        break;
                    default:
                        days = 30;
                }
            }

            // 计算日期范围
            Calendar cal = Calendar.getInstance();
            Date endDate = cal.getTime();
            cal.add(Calendar.DAY_OF_MONTH, -(days - 1));
            Date startDate = cal.getTime();

            List<String> dates = new ArrayList<>();
            List<Integer> countData = new ArrayList<>();
            List<BigDecimal> amountData = new ArrayList<>();

            if (billInstrumentMapper != null) {
                Map<String, Object> filterParams = extractFilterParams(params);
                Map<String, Object> trendParams = new HashMap<>();
                trendParams.put("startDate", startDate);
                trendParams.put("endDate", endDate);
                // 传递筛选条件
                if (filterParams.containsKey("billTypes")) {
                    trendParams.put("billTypes", filterParams.get("billTypes"));
                }
                if (filterParams.containsKey("billStatuses")) {
                    trendParams.put("billStatuses", filterParams.get("billStatuses"));
                }
                List<Map<String, Object>> trendData = billInstrumentMapper.getBillDailyTrend(trendParams);

                // 创建日期到数据的映射
                Map<String, Map<String, Object>> dataMap = new HashMap<>();
                if (trendData != null) {
                    for (Map<String, Object> item : trendData) {
                        String dateLabel = getStringValue(item, "dateLabel");
                        if (dateLabel != null) {
                            dataMap.put(dateLabel, item);
                        }
                    }
                }

                // 生成完整的日期序列
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
                cal.setTime(startDate);

                for (int i = 0; i < days; i++) {
                    String dateLabel = sdf.format(cal.getTime());
                    dates.add(dateLabel);

                    Map<String, Object> dayData = dataMap.get(dateLabel);
                    if (dayData != null) {
                        countData.add(getIntValue(getMapValue(dayData, "count")));
                        amountData.add(getBigDecimalValue(getMapValue(dayData, "amount"))
                                .divide(BigDecimal.valueOf(10000), 2, RoundingMode.HALF_UP));
                    } else {
                        countData.add(0);
                        amountData.add(BigDecimal.ZERO);
                    }

                    cal.add(Calendar.DAY_OF_MONTH, 1);
                }
            } else {
                // 如果没有Mapper，生成空数据
                SimpleDateFormat sdf = new SimpleDateFormat("MM/dd");
                cal.setTime(startDate);
                for (int i = 0; i < days; i++) {
                    dates.add(sdf.format(cal.getTime()));
                    countData.add(0);
                    amountData.add(BigDecimal.ZERO);
                    cal.add(Calendar.DAY_OF_MONTH, 1);
                }
            }

            result.put("dates", dates);
            result.put("countData", countData);
            result.put("amountData", amountData);

        } catch (Exception e) {
            log.error("获取趋势分析失败", e);
            result.put("dates", new ArrayList<>());
            result.put("countData", new ArrayList<>());
            result.put("amountData", new ArrayList<>());
        }

        return result;
    }

    /**
     * 从前端参数中提取筛选条件，构建 mapper 查询参数
     * 前端发送: dimension, startDate, endDate, billType(逗号分隔), billStatus(逗号分隔)
     */
    private Map<String, Object> extractFilterParams(Map<String, Object> params) {
        Map<String, Object> queryParams = new HashMap<>();
        if (params == null) {
            queryParams.put("dimension", "billType");
            return queryParams;
        }

        // 维度
        String dimension = (String) params.get("dimension");
        queryParams.put("dimension", dimension != null ? dimension : "billType");

        // 日期范围
        String startDate = (String) params.get("startDate");
        String endDate = (String) params.get("endDate");
        if (startDate != null && !startDate.isEmpty()) {
            queryParams.put("startDate", startDate + " 00:00:00");
        }
        if (endDate != null && !endDate.isEmpty()) {
            queryParams.put("endDate", endDate + " 23:59:59");
        }

        // 票据类型（逗号分隔字符串 → List）
        String billType = (String) params.get("billType");
        if (billType != null && !billType.isEmpty()) {
            queryParams.put("billTypes", Arrays.asList(billType.split(",")));
        }

        // 票据状态（逗号分隔字符串 → List）
        String billStatus = (String) params.get("billStatus");
        if (billStatus != null && !billStatus.isEmpty()) {
            queryParams.put("billStatuses", Arrays.asList(billStatus.split(",")));
        }

        return queryParams;
    }

    /**
     * 大小写无关的Map取值（兼容达梦数据库返回大写列名）
     */
    private Object getMapValue(Map<String, Object> map, String key) {
        if (map == null || key == null) return null;
        Object value = map.get(key);
        if (value != null) return value;
        // 尝试大写key
        value = map.get(key.toUpperCase());
        if (value != null) return value;
        // 尝试小写key
        value = map.get(key.toLowerCase());
        if (value != null) return value;
        // 遍历查找（兜底）
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (key.equalsIgnoreCase(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    /**
     * 大小写无关的Map取String值
     */
    private String getStringValue(Map<String, Object> map, String key) {
        Object value = getMapValue(map, key);
        return value != null ? value.toString() : null;
    }

    /**
     * 安全获取int值
     */
    private int getIntValue(Object obj) {
        if (obj == null) {
            return 0;
        }
        if (obj instanceof Number) {
            return ((Number) obj).intValue();
        }
        try {
            return Integer.parseInt(obj.toString());
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    /**
     * 安全获取BigDecimal值
     */
    private BigDecimal getBigDecimalValue(Object obj) {
        if (obj == null) {
            return BigDecimal.ZERO;
        }
        if (obj instanceof BigDecimal) {
            return (BigDecimal) obj;
        }
        if (obj instanceof Number) {
            return BigDecimal.valueOf(((Number) obj).doubleValue());
        }
        try {
            return new BigDecimal(obj.toString());
        } catch (NumberFormatException e) {
            return BigDecimal.ZERO;
        }
    }
}
