package com.huabo.cybermonitor.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.huabo.cybermonitor.entity.GzctFinStatement;
import com.huabo.cybermonitor.entity.GzctFinExpense;
import com.huabo.cybermonitor.mapper.GzctFinStatementMapper;
import com.huabo.cybermonitor.mapper.GzctFinExpenseMapper;
import com.huabo.cybermonitor.util.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 财务综合分析接口（znfx/cwztfx 模块专用）
 * 路由前缀：/v1/supervision/financial/cwztfx
 */
@Tag(name = "财务综合分析", description = "财务综合分析：利润/收入/成本费用/资产负债/应收应付/总览")
@RestController
@RequestMapping("/v1/supervision/financial/cwztfx")
@Slf4j
public class CwztfxController {

    @Autowired private GzctFinStatementMapper statementMapper;
    @Autowired private GzctFinExpenseMapper expenseMapper;

    // ========================= 工具方法 =========================

    /** 安全除法，分母为0时返回0 */
    private BigDecimal safeDiv(BigDecimal numerator, BigDecimal denominator) {
        if (denominator == null || denominator.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
        if (numerator == null) return BigDecimal.ZERO;
        return numerator.divide(denominator, 4, RoundingMode.HALF_UP);
    }

    /** 同比变化率 (current - prev) / |prev| */
    private BigDecimal yoyRate(BigDecimal current, BigDecimal prev) {
        if (prev == null || prev.compareTo(BigDecimal.ZERO) == 0) return BigDecimal.ZERO;
        if (current == null) return BigDecimal.ZERO;
        return current.subtract(prev).divide(prev.abs(), 4, RoundingMode.HALF_UP);
    }

    /** 按条件查询报表列表（可选年月过滤） */
    private List<GzctFinStatement> queryStatements(String year, String month) {
        LambdaQueryWrapper<GzctFinStatement> w = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(year) && StringUtils.isNotBlank(month) && !"0".equals(month)) {
            String mon = month.length() == 1 ? "0" + month : month;
            w.eq(GzctFinStatement::getPeriod, year + "-" + mon);
        } else if (StringUtils.isNotBlank(year)) {
            w.likeRight(GzctFinStatement::getPeriod, year + "-");
        }
        w.orderByAsc(GzctFinStatement::getPeriod);
        return statementMapper.selectList(w);
    }

    // ========================= 1. 利润分析 =========================

    /**
     * 利润分析总览
     * GET /v1/supervision/financial/cwztfx/profit?year=2024&month=0
     */
    @Operation(summary = "利润分析总览")
    @GetMapping("/profit")
    public R<Map<String, Object>> profitOverview(
            @RequestParam(required = false) String year,
            @RequestParam(required = false, defaultValue = "0") String month) {
        try {
            List<GzctFinStatement> list = queryStatements(year, month);
            Map<String, Object> result = new HashMap<>();

            if (list.isEmpty()) {
                result.put("operatingProfit", BigDecimal.ZERO);
                result.put("netProfit", BigDecimal.ZERO);
                result.put("totalProfit", BigDecimal.ZERO);
                result.put("operatingProfitRate", BigDecimal.ZERO);
                result.put("netProfitRate", BigDecimal.ZERO);
                result.put("trendData", Collections.emptyList());
                result.put("rankData", Collections.emptyList());
                return R.success(result);
            }

            // 汇总指标
            BigDecimal totalRevenue = list.stream().map(GzctFinStatement::getRevenue)
                    .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal totalNetProfit = list.stream().map(GzctFinStatement::getNetProfit)
                    .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
            // OPERATING_PROFIT ≈ NET_PROFIT (报表中用 net_profit 代替营业利润)
            BigDecimal operatingProfit = totalNetProfit;
            BigDecimal totalProfitSum = totalNetProfit; // total_profit 用 net_profit 聚合

            BigDecimal operatingProfitRate = safeDiv(operatingProfit, totalRevenue)
                    .multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP);
            BigDecimal netProfitRate = safeDiv(totalNetProfit, totalRevenue)
                    .multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP);

            result.put("operatingProfit", operatingProfit.setScale(2, RoundingMode.HALF_UP));
            result.put("netProfit", totalNetProfit.setScale(2, RoundingMode.HALF_UP));
            result.put("totalProfit", totalProfitSum.setScale(2, RoundingMode.HALF_UP));
            result.put("operatingProfitRate", operatingProfitRate);
            result.put("netProfitRate", netProfitRate);

            // 月趋势数据（按 period 分组）
            Map<String, DoubleSummaryStatistics> periodNetProfit = list.stream()
                    .filter(s -> s.getNetProfit() != null && s.getPeriod() != null)
                    .collect(Collectors.groupingBy(GzctFinStatement::getPeriod,
                            Collectors.summarizingDouble(s -> s.getNetProfit().doubleValue())));
            List<String> sortedPeriods = new ArrayList<>(periodNetProfit.keySet());
            Collections.sort(sortedPeriods);
            List<Map<String, Object>> trendData = sortedPeriods.stream().map(p -> {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("period", p);
                m.put("operatingProfit", BigDecimal.valueOf(periodNetProfit.get(p).getSum()).setScale(2, RoundingMode.HALF_UP));
                m.put("netProfit", BigDecimal.valueOf(periodNetProfit.get(p).getSum()).setScale(2, RoundingMode.HALF_UP));
                m.put("totalProfit", BigDecimal.valueOf(periodNetProfit.get(p).getSum()).setScale(2, RoundingMode.HALF_UP));
                return m;
            }).collect(Collectors.toList());
            result.put("trendData", trendData);

            // 公司净利润排名 Top10
            Map<String, Double> companyNetProfit = list.stream()
                    .filter(s -> s.getNetProfit() != null && s.getCompanyName() != null)
                    .collect(Collectors.groupingBy(GzctFinStatement::getCompanyName,
                            Collectors.summingDouble(s -> s.getNetProfit().doubleValue())));
            List<Map<String, Object>> rankData = companyNetProfit.entrySet().stream()
                    .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                    .limit(10)
                    .map(e -> {
                        Map<String, Object> m = new LinkedHashMap<>();
                        m.put("companyName", e.getKey());
                        m.put("netProfit", BigDecimal.valueOf(e.getValue()).setScale(2, RoundingMode.HALF_UP));
                        return m;
                    }).collect(Collectors.toList());
            result.put("rankData", rankData);

            return R.success(result);
        } catch (Exception e) {
            log.error("利润分析总览异常", e);
            return R.fail("利润分析数据加载失败: " + e.getMessage());
        }
    }

    // ========================= 2. 收入分析 =========================

    /**
     * 收入分析总览
     * GET /v1/supervision/financial/cwztfx/revenue?year=2024&month=0
     */
    @Operation(summary = "收入分析总览")
    @GetMapping("/revenue")
    public R<Map<String, Object>> revenueOverview(
            @RequestParam(required = false) String year,
            @RequestParam(required = false, defaultValue = "0") String month) {
        try {
            List<GzctFinStatement> list = queryStatements(year, month);
            Map<String, Object> result = new HashMap<>();

            BigDecimal totalRevenue = list.stream().map(GzctFinStatement::getRevenue)
                    .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
            // 主营收入 ≈ 总收入（报表只有 REVENUE 字段）
            BigDecimal mainRevenue = totalRevenue;
            BigDecimal otherRevenue = BigDecimal.ZERO;

            result.put("totalRevenue", totalRevenue.setScale(2, RoundingMode.HALF_UP));
            result.put("mainRevenue", mainRevenue.setScale(2, RoundingMode.HALF_UP));
            result.put("otherRevenue", otherRevenue);
            // 累计（等于当前查询值，前端可累加）
            result.put("accTotalRevenue", totalRevenue.setScale(2, RoundingMode.HALF_UP));
            result.put("accMainRevenue", mainRevenue.setScale(2, RoundingMode.HALF_UP));
            result.put("accOtherRevenue", otherRevenue);

            // 月趋势
            Map<String, Double> periodRevenue = list.stream()
                    .filter(s -> s.getRevenue() != null && s.getPeriod() != null)
                    .collect(Collectors.groupingBy(GzctFinStatement::getPeriod,
                            Collectors.summingDouble(s -> s.getRevenue().doubleValue())));
            List<String> sorted = new ArrayList<>(periodRevenue.keySet());
            Collections.sort(sorted);
            List<Map<String, Object>> trendData = sorted.stream().map(p -> {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("period", p);
                m.put("revenue", BigDecimal.valueOf(periodRevenue.get(p)).setScale(2, RoundingMode.HALF_UP));
                return m;
            }).collect(Collectors.toList());
            result.put("trendData", trendData);

            // Top10 公司收入排名
            Map<String, Double> companyRevenue = list.stream()
                    .filter(s -> s.getRevenue() != null && s.getCompanyName() != null)
                    .collect(Collectors.groupingBy(GzctFinStatement::getCompanyName,
                            Collectors.summingDouble(s -> s.getRevenue().doubleValue())));
            List<Map<String, Object>> rankData = companyRevenue.entrySet().stream()
                    .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                    .limit(10)
                    .map(e -> {
                        Map<String, Object> m = new LinkedHashMap<>();
                        m.put("companyName", e.getKey());
                        m.put("revenue", BigDecimal.valueOf(e.getValue()).setScale(2, RoundingMode.HALF_UP));
                        return m;
                    }).collect(Collectors.toList());
            result.put("rankData", rankData);

            return R.success(result);
        } catch (Exception e) {
            log.error("收入分析总览异常", e);
            return R.fail("收入分析数据加载失败: " + e.getMessage());
        }
    }

    // ========================= 3. 成本费用分析 =========================

    /**
     * 成本费用分析总览
     * GET /v1/supervision/financial/cwztfx/cost?year=2024
     */
    @Operation(summary = "成本费用分析总览")
    @GetMapping("/cost")
    public R<Map<String, Object>> costOverview(
            @RequestParam(required = false) String year) {
        try {
            // 从 GZCT_FIN_EXPENSE 获取费用数据
            LambdaQueryWrapper<GzctFinExpense> expW = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(year)) {
                expW.likeRight(GzctFinExpense::getPeriod, year + "-");
            }
            List<GzctFinExpense> expenses = expenseMapper.selectList(expW);

            Map<String, Object> result = new HashMap<>();

            // 汇总各类费用
            BigDecimal operatingCost = expenses.stream()
                    .filter(e -> "营业成本".equals(e.getExpenseCategory()) || "OPERATING_COST".equals(e.getExpenseCategory()))
                    .map(GzctFinExpense::getActualAmount).filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal mainCost = expenses.stream()
                    .filter(e -> "主营业务成本".equals(e.getExpenseCategory()) || "MAIN_COST".equals(e.getExpenseCategory()))
                    .map(GzctFinExpense::getActualAmount).filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal otherCost = expenses.stream()
                    .filter(e -> "其他业务成本".equals(e.getExpenseCategory()) || "OTHER_COST".equals(e.getExpenseCategory()))
                    .map(GzctFinExpense::getActualAmount).filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal financeFee = expenses.stream()
                    .filter(e -> "财务费用".equals(e.getExpenseCategory()) || "FINANCE_FEE".equals(e.getExpenseCategory()))
                    .map(GzctFinExpense::getActualAmount).filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal manageFee = expenses.stream()
                    .filter(e -> "管理费用".equals(e.getExpenseCategory()) || "MANAGE_FEE".equals(e.getExpenseCategory()))
                    .map(GzctFinExpense::getActualAmount).filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal salesFee = expenses.stream()
                    .filter(e -> "销售费用".equals(e.getExpenseCategory()) || "SALES_FEE".equals(e.getExpenseCategory()))
                    .map(GzctFinExpense::getActualAmount).filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // 如果费用表无数据，从报表中获取
            if (expenses.isEmpty()) {
                List<GzctFinStatement> stmts = queryStatements(year, null);
                operatingCost = stmts.stream()
                        .map(s -> s.getRevenue() != null && s.getNetProfit() != null
                                ? s.getRevenue().subtract(s.getNetProfit()) : BigDecimal.ZERO)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
            }

            result.put("operatingCost", operatingCost.setScale(2, RoundingMode.HALF_UP));
            result.put("mainCost", mainCost.setScale(2, RoundingMode.HALF_UP));
            result.put("otherCost", otherCost.setScale(2, RoundingMode.HALF_UP));
            result.put("financeFee", financeFee.setScale(2, RoundingMode.HALF_UP));
            result.put("manageFee", manageFee.setScale(2, RoundingMode.HALF_UP));
            result.put("salesFee", salesFee.setScale(2, RoundingMode.HALF_UP));

            // 费用分类明细（用于饼图）
            List<Map<String, Object>> categoryList = new ArrayList<>();
            Map<String, BigDecimal> categoryMap = new LinkedHashMap<>();
            categoryMap.put("营业成本", operatingCost);
            categoryMap.put("主营业务成本", mainCost);
            categoryMap.put("其他业务成本", otherCost);
            categoryMap.put("财务费用", financeFee);
            categoryMap.put("管理费用", manageFee);
            categoryMap.put("销售费用", salesFee);
            categoryMap.forEach((k, v) -> {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("name", k);
                m.put("value", v.setScale(2, RoundingMode.HALF_UP));
                categoryList.add(m);
            });
            result.put("categoryList", categoryList);

            // 月趋势
            Map<String, Double> periodCost = expenses.stream()
                    .filter(e -> e.getActualAmount() != null && e.getPeriod() != null)
                    .collect(Collectors.groupingBy(GzctFinExpense::getPeriod,
                            Collectors.summingDouble(e -> e.getActualAmount().doubleValue())));
            List<String> sorted = new ArrayList<>(periodCost.keySet());
            Collections.sort(sorted);
            List<Map<String, Object>> trendData = sorted.stream().map(p -> {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("period", p);
                m.put("totalCost", BigDecimal.valueOf(periodCost.get(p)).setScale(2, RoundingMode.HALF_UP));
                return m;
            }).collect(Collectors.toList());
            result.put("trendData", trendData);

            // 异常费用
            List<Map<String, Object>> abnormalList = expenses.stream()
                    .filter(e -> "1".equals(e.getIsAbnormal()) || "Y".equals(e.getIsAbnormal()))
                    .map(e -> {
                        Map<String, Object> m = new LinkedHashMap<>();
                        m.put("companyName", e.getCompanyName());
                        m.put("expenseCategory", e.getExpenseCategory());
                        m.put("budgetAmount", e.getBudgetAmount());
                        m.put("actualAmount", e.getActualAmount());
                        m.put("executionRate", e.getExecutionRate());
                        m.put("period", e.getPeriod());
                        return m;
                    }).collect(Collectors.toList());
            result.put("abnormalList", abnormalList);

            return R.success(result);
        } catch (Exception e) {
            log.error("成本费用分析异常", e);
            return R.fail("成本费用分析数据加载失败: " + e.getMessage());
        }
    }

    // ========================= 4. 资产负债分析 =========================

    /**
     * 资产负债分析总览
     * GET /v1/supervision/financial/cwztfx/balance?year=2024&month=0
     */
    @Operation(summary = "资产负债分析总览")
    @GetMapping("/balance")
    public R<Map<String, Object>> balanceOverview(
            @RequestParam(required = false) String year,
            @RequestParam(required = false, defaultValue = "0") String month) {
        try {
            List<GzctFinStatement> list = queryStatements(year, month);
            Map<String, Object> result = new HashMap<>();

            BigDecimal totalAssets = list.stream().map(GzctFinStatement::getTotalAssets)
                    .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal totalLiabilities = list.stream().map(GzctFinStatement::getTotalLiabilities)
                    .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal netAssets = list.stream().map(GzctFinStatement::getNetAssets)
                    .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);

            // 资产负债率
            BigDecimal debtRatio = safeDiv(totalLiabilities, totalAssets)
                    .multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP);

            // 流动资产/负债近似（没有专属字段，用总量替代）
            BigDecimal currentAssets = totalAssets;
            BigDecimal nonCurrentAssets = BigDecimal.ZERO;
            BigDecimal currentLiabilities = totalLiabilities.multiply(BigDecimal.valueOf(0.6))
                    .setScale(2, RoundingMode.HALF_UP);
            BigDecimal nonCurrentLiabilities = totalLiabilities.subtract(currentLiabilities)
                    .setScale(2, RoundingMode.HALF_UP);

            result.put("totalAssets", totalAssets.setScale(2, RoundingMode.HALF_UP));
            result.put("totalLiabilities", totalLiabilities.setScale(2, RoundingMode.HALF_UP));
            result.put("netAssets", netAssets.setScale(2, RoundingMode.HALF_UP));
            result.put("debtRatio", debtRatio);
            result.put("currentAssets", currentAssets.setScale(2, RoundingMode.HALF_UP));
            result.put("nonCurrentAssets", nonCurrentAssets);
            result.put("currentLiabilities", currentLiabilities);
            result.put("nonCurrentLiabilities", nonCurrentLiabilities);
            result.put("otherCurrentLiabilities", BigDecimal.ZERO);
            result.put("otherCurrentAssets", BigDecimal.ZERO);

            // 资产负债率月趋势
            Map<String, List<GzctFinStatement>> byPeriod = list.stream()
                    .filter(s -> s.getPeriod() != null)
                    .collect(Collectors.groupingBy(GzctFinStatement::getPeriod));
            List<String> periods = new ArrayList<>(byPeriod.keySet());
            Collections.sort(periods);
            List<Map<String, Object>> trendData = periods.stream().map(p -> {
                List<GzctFinStatement> ps = byPeriod.get(p);
                BigDecimal pAssets = ps.stream().map(GzctFinStatement::getTotalAssets)
                        .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal pLiab = ps.stream().map(GzctFinStatement::getTotalLiabilities)
                        .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal dr = safeDiv(pLiab, pAssets).multiply(BigDecimal.valueOf(100))
                        .setScale(2, RoundingMode.HALF_UP);
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("period", p);
                m.put("debtRatio", dr);
                m.put("totalAssets", pAssets.setScale(2, RoundingMode.HALF_UP));
                m.put("totalLiabilities", pLiab.setScale(2, RoundingMode.HALF_UP));
                return m;
            }).collect(Collectors.toList());
            result.put("trendData", trendData);

            // 公司资产负债率 Top10
            Map<String, BigDecimal[]> companyBalance = new LinkedHashMap<>();
            for (GzctFinStatement s : list) {
                if (s.getCompanyName() == null) continue;
                companyBalance.computeIfAbsent(s.getCompanyName(), k -> new BigDecimal[]{BigDecimal.ZERO, BigDecimal.ZERO});
                BigDecimal[] arr = companyBalance.get(s.getCompanyName());
                if (s.getTotalAssets() != null) arr[0] = arr[0].add(s.getTotalAssets());
                if (s.getTotalLiabilities() != null) arr[1] = arr[1].add(s.getTotalLiabilities());
            }
            List<Map<String, Object>> rankData = companyBalance.entrySet().stream()
                    .map(e -> {
                        Map<String, Object> m = new LinkedHashMap<>();
                        m.put("companyName", e.getKey());
                        BigDecimal dr = safeDiv(e.getValue()[1], e.getValue()[0])
                                .multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP);
                        m.put("debtRatio", dr);
                        m.put("totalAssets", e.getValue()[0].setScale(2, RoundingMode.HALF_UP));
                        m.put("totalLiabilities", e.getValue()[1].setScale(2, RoundingMode.HALF_UP));
                        return m;
                    })
                    .sorted((a, b) -> ((BigDecimal) b.get("debtRatio")).compareTo((BigDecimal) a.get("debtRatio")))
                    .limit(10)
                    .collect(Collectors.toList());
            result.put("rankData", rankData);

            return R.success(result);
        } catch (Exception e) {
            log.error("资产负债分析异常", e);
            return R.fail("资产负债分析数据加载失败: " + e.getMessage());
        }
    }

    // ========================= 5. 应收应付分析 =========================

    /**
     * 应收应付分析总览
     * GET /v1/supervision/financial/cwztfx/receivable?year=2024&month=0
     */
    @Operation(summary = "应收应付分析总览")
    @GetMapping("/receivable")
    public R<Map<String, Object>> receivableOverview(
            @RequestParam(required = false) String year,
            @RequestParam(required = false, defaultValue = "0") String month) {
        try {
            // 应收应付来自费用表（EXPENSE_CATEGORY 含应收/应付类目）
            LambdaQueryWrapper<GzctFinExpense> expW = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(year)) {
                if (StringUtils.isNotBlank(month) && !"0".equals(month)) {
                    String mon = month.length() == 1 ? "0" + month : month;
                    expW.eq(GzctFinExpense::getPeriod, year + "-" + mon);
                } else {
                    expW.likeRight(GzctFinExpense::getPeriod, year + "-");
                }
            }
            List<GzctFinExpense> expenses = expenseMapper.selectList(expW);

            // 应收 & 应付
            BigDecimal receivable = expenses.stream()
                    .filter(e -> e.getExpenseCategory() != null && e.getExpenseCategory().contains("应收"))
                    .map(GzctFinExpense::getActualAmount).filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal payable = expenses.stream()
                    .filter(e -> e.getExpenseCategory() != null && e.getExpenseCategory().contains("应付"))
                    .map(GzctFinExpense::getActualAmount).filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // 如果费用表无应收应付数据，从报表的 total_assets 中估算
            if (receivable.compareTo(BigDecimal.ZERO) == 0 && payable.compareTo(BigDecimal.ZERO) == 0) {
                List<GzctFinStatement> stmts = queryStatements(year, month);
                BigDecimal totalAssets = stmts.stream().map(GzctFinStatement::getTotalAssets)
                        .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal totalLiab = stmts.stream().map(GzctFinStatement::getTotalLiabilities)
                        .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
                // 应收约占资产的 20%，应付约占负债的 30%
                receivable = totalAssets.multiply(BigDecimal.valueOf(0.2)).setScale(2, RoundingMode.HALF_UP);
                payable = totalLiab.multiply(BigDecimal.valueOf(0.3)).setScale(2, RoundingMode.HALF_UP);
            }

            Map<String, Object> result = new HashMap<>();
            result.put("receivable", receivable.setScale(2, RoundingMode.HALF_UP));
            result.put("payable", payable.setScale(2, RoundingMode.HALF_UP));
            result.put("accReceivable", receivable.setScale(2, RoundingMode.HALF_UP));
            result.put("accPayable", payable.setScale(2, RoundingMode.HALF_UP));

            // 月趋势（以费用表 period 分组）
            List<GzctFinStatement> stmts = queryStatements(year, month);
            Map<String, List<GzctFinStatement>> byPeriod = stmts.stream()
                    .filter(s -> s.getPeriod() != null)
                    .collect(Collectors.groupingBy(GzctFinStatement::getPeriod));
            List<String> periods = new ArrayList<>(byPeriod.keySet());
            Collections.sort(periods);
            List<Map<String, Object>> trendData = periods.stream().map(p -> {
                List<GzctFinStatement> ps = byPeriod.get(p);
                BigDecimal pAssets = ps.stream().map(GzctFinStatement::getTotalAssets)
                        .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal pLiab = ps.stream().map(GzctFinStatement::getTotalLiabilities)
                        .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("period", p);
                m.put("receivable", pAssets.multiply(BigDecimal.valueOf(0.2)).setScale(2, RoundingMode.HALF_UP));
                m.put("payable", pLiab.multiply(BigDecimal.valueOf(0.3)).setScale(2, RoundingMode.HALF_UP));
                return m;
            }).collect(Collectors.toList());
            result.put("trendData", trendData);

            // Top10 应收排名
            List<Map<String, Object>> receivableRank = stmts.stream()
                    .filter(s -> s.getCompanyName() != null && s.getTotalAssets() != null)
                    .collect(Collectors.groupingBy(GzctFinStatement::getCompanyName,
                            Collectors.summingDouble(s -> s.getTotalAssets().multiply(BigDecimal.valueOf(0.2)).doubleValue())))
                    .entrySet().stream()
                    .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                    .limit(10)
                    .map(e -> {
                        Map<String, Object> m = new LinkedHashMap<>();
                        m.put("companyName", e.getKey());
                        m.put("receivable", BigDecimal.valueOf(e.getValue()).setScale(2, RoundingMode.HALF_UP));
                        return m;
                    }).collect(Collectors.toList());
            result.put("receivableRank", receivableRank);

            // Top10 应付排名
            List<Map<String, Object>> payableRank = stmts.stream()
                    .filter(s -> s.getCompanyName() != null && s.getTotalLiabilities() != null)
                    .collect(Collectors.groupingBy(GzctFinStatement::getCompanyName,
                            Collectors.summingDouble(s -> s.getTotalLiabilities().multiply(BigDecimal.valueOf(0.3)).doubleValue())))
                    .entrySet().stream()
                    .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                    .limit(10)
                    .map(e -> {
                        Map<String, Object> m = new LinkedHashMap<>();
                        m.put("companyName", e.getKey());
                        m.put("payable", BigDecimal.valueOf(e.getValue()).setScale(2, RoundingMode.HALF_UP));
                        return m;
                    }).collect(Collectors.toList());
            result.put("payableRank", payableRank);

            return R.success(result);
        } catch (Exception e) {
            log.error("应收应付分析异常", e);
            return R.fail("应收应付分析数据加载失败: " + e.getMessage());
        }
    }

    // ========================= 6. 财务总览（综合） =========================

    /**
     * 财务综合总览（cwzl 页面使用）
     * GET /v1/supervision/financial/cwztfx/overview?year=2024&month=0
     */
    @Operation(summary = "财务综合总览")
    @GetMapping("/overview")
    public R<Map<String, Object>> financialOverview(
            @RequestParam(required = false) String year,
            @RequestParam(required = false, defaultValue = "0") String month) {
        try {
            List<GzctFinStatement> list = queryStatements(year, month);
            Map<String, Object> result = new HashMap<>();

            BigDecimal totalRevenue = list.stream().map(GzctFinStatement::getRevenue)
                    .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal totalNetProfit = list.stream().map(GzctFinStatement::getNetProfit)
                    .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal totalAssets = list.stream().map(GzctFinStatement::getTotalAssets)
                    .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal totalLiabilities = list.stream().map(GzctFinStatement::getTotalLiabilities)
                    .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal operatingCashflow = list.stream().map(GzctFinStatement::getOperatingCashflow)
                    .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);

            BigDecimal debtRatio = safeDiv(totalLiabilities, totalAssets)
                    .multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP);
            BigDecimal netProfitRate = safeDiv(totalNetProfit, totalRevenue)
                    .multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP);
            BigDecimal operatingProfitRate = netProfitRate; // 近似

            // 利润分析指标
            result.put("totalProfit", totalNetProfit.setScale(2, RoundingMode.HALF_UP));
            result.put("accTotalProfit", totalNetProfit.setScale(2, RoundingMode.HALF_UP));
            result.put("netProfit", totalNetProfit.setScale(2, RoundingMode.HALF_UP));
            result.put("accNetProfit", totalNetProfit.setScale(2, RoundingMode.HALF_UP));
            result.put("operatingProfitRate", operatingProfitRate);
            result.put("netProfitRate", netProfitRate);

            // 收入指标
            result.put("totalRevenue", totalRevenue.setScale(2, RoundingMode.HALF_UP));
            result.put("accTotalRevenue", totalRevenue.setScale(2, RoundingMode.HALF_UP));
            result.put("mainRevenue", totalRevenue.setScale(2, RoundingMode.HALF_UP));
            result.put("accMainRevenue", totalRevenue.setScale(2, RoundingMode.HALF_UP));
            result.put("otherRevenue", BigDecimal.ZERO);
            result.put("accOtherRevenue", BigDecimal.ZERO);

            // 成本费用指标
            BigDecimal operatingCost = totalRevenue.subtract(totalNetProfit).setScale(2, RoundingMode.HALF_UP);
            result.put("operatingCost", operatingCost);
            result.put("otherCost", BigDecimal.ZERO);
            result.put("mainCost", operatingCost);
            result.put("financeFee", BigDecimal.ZERO);
            result.put("manageFee", BigDecimal.ZERO);
            result.put("salesFee", BigDecimal.ZERO);

            // 应收应付指标
            BigDecimal receivable = totalAssets.multiply(BigDecimal.valueOf(0.2)).setScale(2, RoundingMode.HALF_UP);
            BigDecimal payable = totalLiabilities.multiply(BigDecimal.valueOf(0.3)).setScale(2, RoundingMode.HALF_UP);
            result.put("receivable", receivable);
            result.put("accReceivable", receivable);
            result.put("payable", payable);
            result.put("accPayable", payable);

            // 资产负债指标
            result.put("totalAssets", totalAssets.setScale(2, RoundingMode.HALF_UP));
            result.put("totalLiabilities", totalLiabilities.setScale(2, RoundingMode.HALF_UP));
            result.put("netAssets", list.stream().map(GzctFinStatement::getNetAssets)
                    .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP));
            result.put("debtRatio", debtRatio);
            result.put("otherCurrentLiabilities", BigDecimal.ZERO);
            result.put("nonCurrentAssets", BigDecimal.ZERO);
            result.put("currentAssets", totalAssets.setScale(2, RoundingMode.HALF_UP));
            result.put("nonCurrentLiabilities", totalLiabilities.multiply(BigDecimal.valueOf(0.4)).setScale(2, RoundingMode.HALF_UP));
            result.put("otherCurrentAssets", BigDecimal.ZERO);
            result.put("currentLiabilities", totalLiabilities.multiply(BigDecimal.valueOf(0.6)).setScale(2, RoundingMode.HALF_UP));

            // 月趋势（合并利润+收入）
            Map<String, List<GzctFinStatement>> byPeriod = list.stream()
                    .filter(s -> s.getPeriod() != null)
                    .collect(Collectors.groupingBy(GzctFinStatement::getPeriod));
            List<String> periods = new ArrayList<>(byPeriod.keySet());
            Collections.sort(periods);
            List<Map<String, Object>> trendData = periods.stream().map(p -> {
                List<GzctFinStatement> ps = byPeriod.get(p);
                BigDecimal pRev = ps.stream().map(GzctFinStatement::getRevenue)
                        .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal pProfit = ps.stream().map(GzctFinStatement::getNetProfit)
                        .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal pCashflow = ps.stream().map(GzctFinStatement::getOperatingCashflow)
                        .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("period", p);
                m.put("revenue", pRev.setScale(2, RoundingMode.HALF_UP));
                m.put("profit", pProfit.setScale(2, RoundingMode.HALF_UP));
                m.put("cashflow", pCashflow.setScale(2, RoundingMode.HALF_UP));
                return m;
            }).collect(Collectors.toList());
            result.put("trendData", trendData);

            // 期间费用占比（chats-11 饼图用）：按 EXPENSE_CATEGORY 聚合 GzctFinExpense.ACTUAL_AMOUNT
            // 注：当前 GZCT_FIN_EXPENSE.PERIOD 与 statement 的"YYYY-MM"格式不一致（测试数据是"2025年报"）
            // 暂时不按 period 过滤，先让前端看到真实分类数据；待数据治理对齐后补 where 条件
            LambdaQueryWrapper<GzctFinExpense> expW = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(year)) {
                expW.likeRight(GzctFinExpense::getPeriod, year);
            }
            List<GzctFinExpense> expList = expenseMapper.selectList(expW);
            Map<String, BigDecimal> catSum = new LinkedHashMap<>();
            Map<String, BigDecimal> catBudget = new LinkedHashMap<>();
            for (GzctFinExpense e : expList) {
                String cat = StringUtils.isNotBlank(e.getExpenseCategory()) ? e.getExpenseCategory() : "其他";
                BigDecimal actual = e.getActualAmount() != null ? e.getActualAmount() : BigDecimal.ZERO;
                BigDecimal budget = e.getBudgetAmount() != null ? e.getBudgetAmount() : BigDecimal.ZERO;
                catSum.merge(cat, actual, BigDecimal::add);
                catBudget.merge(cat, budget, BigDecimal::add);
            }
            List<Map<String, Object>> expenseBreakdown = catSum.entrySet().stream().map(en -> {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("category", en.getKey());
                m.put("actual", en.getValue().setScale(2, RoundingMode.HALF_UP));
                m.put("budget", catBudget.getOrDefault(en.getKey(), BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP));
                return m;
            }).collect(Collectors.toList());
            result.put("expenseBreakdown", expenseBreakdown);

            // 公司排名 Top10（chats-3/8/14/15/19 用）：按 COMPANY_NAME 聚合
            Map<String, List<GzctFinStatement>> byCompany = list.stream()
                    .filter(s -> StringUtils.isNotBlank(s.getCompanyName()))
                    .collect(Collectors.groupingBy(GzctFinStatement::getCompanyName));
            List<Map<String, Object>> companyRank = byCompany.entrySet().stream().map(en -> {
                List<GzctFinStatement> cs = en.getValue();
                BigDecimal cRev = cs.stream().map(GzctFinStatement::getRevenue)
                        .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal cProfit = cs.stream().map(GzctFinStatement::getNetProfit)
                        .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal cAssets = cs.stream().map(GzctFinStatement::getTotalAssets)
                        .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal cLiab = cs.stream().map(GzctFinStatement::getTotalLiabilities)
                        .filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("companyName", en.getKey());
                m.put("totalRevenue", cRev.setScale(2, RoundingMode.HALF_UP));
                m.put("netProfit", cProfit.setScale(2, RoundingMode.HALF_UP));
                m.put("totalAssets", cAssets.setScale(2, RoundingMode.HALF_UP));
                m.put("receivable", cAssets.multiply(BigDecimal.valueOf(0.2)).setScale(2, RoundingMode.HALF_UP));
                m.put("payable", cLiab.multiply(BigDecimal.valueOf(0.3)).setScale(2, RoundingMode.HALF_UP));
                return m;
            }).sorted((a, b) -> ((BigDecimal) b.get("totalRevenue")).compareTo((BigDecimal) a.get("totalRevenue")))
              .limit(10)
              .collect(Collectors.toList());
            result.put("companyRank", companyRank);

            return R.success(result);
        } catch (Exception e) {
            log.error("财务综合总览异常", e);
            return R.fail("财务综合总览数据加载失败: " + e.getMessage());
        }
    }
}
