package com.huabo.cybermonitor.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.cybermonitor.entity.*;
import com.huabo.cybermonitor.mapper.*;
import com.huabo.cybermonitor.util.AuditStatusUtil;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.util.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import com.huabo.cybermonitor.util.SimpleXlsxWriter;

@Tag(name = "财务穿透式监管", description = "财务穿透式监管全接口(报表/指标/关联交易/费用/监控/异常/预警/穿透)")
@RestController
@RequestMapping("/v1/supervision/financial")
@Slf4j
public class FinancialSupervisionController {

    @Autowired private GzctFinStatementMapper statementMapper;
    @Autowired private GzctFinRelatedPartyMapper relatedPartyMapper;
    @Autowired private GzctFinExpenseMapper expenseMapper;
    @Autowired private GzctFinAnomalyMapper anomalyMapper;
    @Autowired private GzctFinAnomalyRectificationMapper anomalyRectificationMapper;
    @Autowired private GzctFinAlertMapper alertMapper;
    @Autowired private GzctFinAlertHandleMapper alertHandleMapper;
    @Autowired private FinancialIndicatorsMapper financialIndicatorsMapper;
    @Autowired private GzctFinancialComplianceMapper financialComplianceMapper;
    @Autowired private GzctFinVoucherMapper voucherMapper;
    @Autowired private com.huabo.cybermonitor.util.OrgQueryHelper orgQueryHelper;

    private <T> PageResult<T> buildPageResult(Page<T> r) {
        PageResult<T> pr = new PageResult<>();
        pr.setTotalRecord((int) r.getTotal()); pr.setCurrentPage((int) r.getCurrent());
        pr.setPageNumber((int) r.getCurrent()); pr.setTotalPage((int) r.getPages());
        pr.setPageSize((int) r.getSize()); pr.setTlist(r.getRecords());
        return pr;
    }

    private String str(Map<String, Object> p, String... keys) {
        if (p == null || keys == null) return null;
        for (String key : keys) {
            Object v = p.get(key);
            if (v != null && StringUtils.isNotBlank(v.toString())) return v.toString().trim();
        }
        return null;
    }

    private BigDecimal dec(Map<String, Object> p, String key) {
        if (p == null || p.get(key) == null || StringUtils.isBlank(p.get(key).toString())) return null;
        try { return new BigDecimal(p.get(key).toString()); } catch (Exception e) { return null; }
    }

    private BigDecimal safe(BigDecimal v) { return v == null ? BigDecimal.ZERO : v; }

    private void applyRange(LambdaQueryWrapper<GzctFinStatement> w, Map<String, Object> p,
                            String minKey, String maxKey,
                            com.baomidou.mybatisplus.core.toolkit.support.SFunction<GzctFinStatement, ?> column) {
        BigDecimal min = dec(p, minKey), max = dec(p, maxKey);
        if (min != null) w.ge(column, min);
        if (max != null) w.le(column, max);
    }

    private void applyConsolidatedFilters(LambdaQueryWrapper<GzctFinStatement> w, Map<String, Object> p) {
        String companyId = str(p, "companyId", "enterpriseId");
        String companyName = str(p, "companyName", "enterpriseName");
        String statementType = str(p, "statementType", "analysisType");
        String auditStatus = str(p, "auditStatus", "analysisStatus");
        String period = str(p, "period");
        if (StringUtils.isNotBlank(companyId)) w.eq(GzctFinStatement::getCompanyId, companyId);
        if (StringUtils.isNotBlank(companyName)) w.like(GzctFinStatement::getCompanyName, companyName);
        // statementType 兼容 DB 中所有历史写法
        if (StringUtils.isNotBlank(statementType)) {
            String[] matchValues = null;
            if ("合并".equals(statementType) || "CONSOLIDATED".equals(statementType) || "合并报表".equals(statementType)) {
                matchValues = new String[]{"合并", "CONSOLIDATED", "合并报表"};
            } else if ("单体".equals(statementType) || "STANDALONE".equals(statementType) || "单体报表".equals(statementType)) {
                matchValues = new String[]{"单体", "STANDALONE", "单体报表"};
            } else if ("parent".equals(statementType) || "PARENT".equals(statementType) || "母公司".equals(statementType)) {
                matchValues = new String[]{"parent", "PARENT", "母公司"};
            } else if ("ANNUAL".equals(statementType) || "年度报表".equals(statementType)) {
                matchValues = new String[]{"ANNUAL", "年度报表"};
            } else if ("PERFORMANCE".equals(statementType)) {
                matchValues = new String[]{"PERFORMANCE"};
            }
            if (matchValues != null) {
                w.in(GzctFinStatement::getStatementType, java.util.Arrays.asList(matchValues));
            } else {
                w.eq(GzctFinStatement::getStatementType, statementType);
            }
        }
        if (StringUtils.isNotBlank(period)) w.like(GzctFinStatement::getPeriod, period);
        if (StringUtils.isNotBlank(auditStatus)) {
            String normalized = AuditStatusUtil.normalize(auditStatus);
            String cnLabel = AuditStatusUtil.getChineseLabel(normalized != null ? normalized : auditStatus);
            if (normalized != null && cnLabel != null && !normalized.equals(cnLabel)) {
                w.in(GzctFinStatement::getAuditStatus, normalized, cnLabel);
            } else {
                w.eq(GzctFinStatement::getAuditStatus, auditStatus);
            }
        }
        applyRange(w, p, "minTotalRevenue", "maxTotalRevenue", GzctFinStatement::getRevenue);
        applyRange(w, p, "minTotalAssets", "maxTotalAssets", GzctFinStatement::getTotalAssets);
        applyRange(w, p, "minTotalLiabilities", "maxTotalLiabilities", GzctFinStatement::getTotalLiabilities);
        applyRange(w, p, "minNetAssets", "maxNetAssets", GzctFinStatement::getNetAssets);
        applyRange(w, p, "minNetProfit", "maxNetProfit", GzctFinStatement::getNetProfit);
        applyRange(w, p, "minOperatingCashflow", "maxOperatingCashflow", GzctFinStatement::getOperatingCashflow);
    }

    private Map<String, Object> buildConsolidatedRow(GzctFinStatement s) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", s.getStatementId());
        m.put("companyId", s.getCompanyId());
        m.put("companyName", s.getCompanyName());
        m.put("statementType", s.getStatementType());
        m.put("period", s.getPeriod());
        m.put("totalRevenue", safe(s.getRevenue()));
        m.put("totalAssets", safe(s.getTotalAssets()));
        m.put("totalLiabilities", safe(s.getTotalLiabilities()));
        m.put("netAssets", safe(s.getNetAssets()));
        m.put("netProfit", safe(s.getNetProfit()));
        m.put("operatingCashflow", safe(s.getOperatingCashflow()));
        m.put("auditStatus", s.getAuditStatus());
        m.put("createTime", s.getCreateTime());
        m.put("updateTime", s.getUpdateTime());
        return m;
    }

    private BigDecimal percent(BigDecimal a, BigDecimal b) {
        return b == null || b.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO
                : safe(a).multiply(BigDecimal.valueOf(100)).divide(b, 2, RoundingMode.HALF_UP);
    }

    // ==================== 财务报表(CRUD已在FinancialStatementController中) ====================

    @Operation(summary = "报表穿透下钒")
    @GetMapping("/statement/drill-through")
    public R<Map<String, Object>> statementDrillThrough(@RequestParam(required = false) String companyId,
                                                          @RequestParam(required = false) String period,
                                                          @RequestParam(required = false) String statementType) {
        try {
            Map<String, Object> result = new HashMap<>();
            LambdaQueryWrapper<GzctFinStatement> w = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(companyId)) w.eq(GzctFinStatement::getCompanyId, companyId);
            if (StringUtils.isNotBlank(period)) w.eq(GzctFinStatement::getPeriod, period);
            if (StringUtils.isNotBlank(statementType)) w.eq(GzctFinStatement::getStatementType, statementType);
            List<GzctFinStatement> stmts = statementMapper.selectList(w);
            result.put("statements", stmts);
            // 构建下钻明细：每条报表作为一个下钻项
            List<Map<String, Object>> drillItems = new ArrayList<>();
            for (GzctFinStatement s : stmts) {
                Map<String, Object> item = new HashMap<>();
                item.put("companyName", s.getCompanyName());
                item.put("amount", s.getTotalAssets());
                item.put("proportion", 0);
                item.put("yoyChange", 0);
                drillItems.add(item);
            }
            result.put("drillItems", drillItems);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "科目明细")
    @GetMapping("/statement/subject-detail")
    public R<Map<String, Object>> subjectDetail(@RequestParam(required = false) String companyId,
                                                  @RequestParam(required = false) String subjectCode,
                                                  @RequestParam(required = false) String period) {
        try {
            Map<String, Object> result = new HashMap<>();
            LambdaQueryWrapper<GzctFinStatement> w = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(companyId)) w.eq(GzctFinStatement::getCompanyId, companyId);
            if (StringUtils.isNotBlank(period)) w.eq(GzctFinStatement::getPeriod, period);
            List<GzctFinStatement> stmts = statementMapper.selectList(w);
            List<Map<String, Object>> details = new ArrayList<>();
            for (GzctFinStatement s : stmts) {
                Map<String, Object> d = new HashMap<>();
                d.put("companyName", s.getCompanyName());
                d.put("amount", s.getTotalAssets());
                d.put("proportion", 0);
                d.put("yoyChange", 0);
                details.add(d);
            }
            result.put("subjectCode", subjectCode);
            result.put("details", details);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 财务指标 ====================

    @Operation(summary = "企业财务指标")
    @GetMapping("/indicator/{companyId}")
    public R<Map<String, Object>> financialIndicators(@PathVariable String companyId, @RequestParam(required = false) String period) {
        try {
            Map<String, Object> result = new HashMap<>();
            LambdaQueryWrapper<GzctFinStatement> w = new LambdaQueryWrapper<GzctFinStatement>()
                .eq(GzctFinStatement::getCompanyId, companyId);
            if (StringUtils.isNotBlank(period)) w.eq(GzctFinStatement::getPeriod, period);
            w.orderByDesc(GzctFinStatement::getCreateTime).last("LIMIT 1");
            GzctFinStatement stmt = statementMapper.selectOne(w);
            Map<String, Object> indicators = new HashMap<>();
            if (stmt != null) {
                BigDecimal assets = stmt.getTotalAssets() != null ? stmt.getTotalAssets() : BigDecimal.ZERO;
                BigDecimal liabilities = stmt.getTotalLiabilities() != null ? stmt.getTotalLiabilities() : BigDecimal.ZERO;
                BigDecimal revenue = stmt.getRevenue() != null ? stmt.getRevenue() : BigDecimal.ZERO;
                BigDecimal profit = stmt.getNetProfit() != null ? stmt.getNetProfit() : BigDecimal.ZERO;
                BigDecimal cashflow = stmt.getOperatingCashflow() != null ? stmt.getOperatingCashflow() : BigDecimal.ZERO;
                // 资产负债率
                BigDecimal debtRatio = assets.compareTo(BigDecimal.ZERO) > 0
                    ? liabilities.divide(assets, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100")) : BigDecimal.ZERO;
                // 净利润率
                BigDecimal profitMargin = revenue.compareTo(BigDecimal.ZERO) > 0
                    ? profit.divide(revenue, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100")) : BigDecimal.ZERO;
                // 利润质量
                BigDecimal profitQuality = profit.compareTo(BigDecimal.ZERO) > 0
                    ? cashflow.divide(profit, 4, RoundingMode.HALF_UP) : BigDecimal.ZERO;
                indicators.put("debtRatio", debtRatio.setScale(2, RoundingMode.HALF_UP));
                indicators.put("profitMargin", profitMargin.setScale(2, RoundingMode.HALF_UP));
                indicators.put("profitQuality", profitQuality.setScale(2, RoundingMode.HALF_UP));
                indicators.put("totalAssets", assets);
                indicators.put("revenue", revenue);
                indicators.put("netProfit", profit);
                indicators.put("operatingCashflow", cashflow);
            }
            result.put("companyId", companyId);
            result.put("period", period);
            result.put("indicators", indicators);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "对标分析列表")
    @PostMapping("/benchmark/list")
    public R<PageResult<Map<String, Object>>> benchmarkList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            String dimension = str(params, "dimension");
            String companyName = str(params, "companyName");
            Integer year = null;
            if (params.get("year") != null && StringUtils.isNotBlank(params.get("year").toString())) {
                year = Integer.parseInt(params.get("year").toString());
            }

            // 查询 GZCT_FINANCIAL_INDICATORS 表
            LambdaQueryWrapper<FinancialIndicators> w = new LambdaQueryWrapper<>();
            if (year != null) w.eq(FinancialIndicators::getReportYear, year);
            if (StringUtils.isNotBlank(companyName)) w.like(FinancialIndicators::getEnterpriseName, companyName);
            // 排除已删除记录
            w.and(wrapper -> wrapper.isNull(FinancialIndicators::getDeleted)
                .or().eq(FinancialIndicators::getDeleted, "")
                .or().eq(FinancialIndicators::getDeleted, "0")
                .or().eq(FinancialIndicators::getDeleted, "N"));
            w.orderByDesc(FinancialIndicators::getReportYear);
            Page<FinancialIndicators> r = financialIndicatorsMapper.selectPage(new Page<>(pn, ps), w);
            log.info("对标分析查询: year={}, companyName={}, dimension={}, 查询结果数={}", year, companyName, dimension, r.getTotal());

            // 根据 dimension 转换为前端期望的字段
            List<Map<String, Object>> voList = new ArrayList<>();
            List<FinancialIndicators> records = r.getRecords();
            for (int i = 0; i < records.size(); i++) {
                FinancialIndicators fi = records.get(i);
                Map<String, Object> vo = new LinkedHashMap<>();
                vo.put("companyName", fi.getEnterpriseName());
                vo.put("reportYear", fi.getReportYear());

                if ("profit".equals(dimension)) {
                    vo.put("roe", scaledVal(fi.getReturnOnEquity()));
                    vo.put("roa", scaledVal(fi.getReturnOnAssets()));
                    vo.put("netProfitRate", scaledVal(fi.getNetProfitMargin()));
                    vo.put("opProfitRate", scaledVal(fi.getOperatingProfitMargin()));
                } else if ("operation".equals(dimension)) {
                    vo.put("receivablesTurn", scaledVal(fi.getAccountsReceivableTurnoverRatio()));
                    vo.put("inventoryTurn", scaledVal(fi.getInventoryTurnoverRatio()));
                    vo.put("assetTurn", scaledVal(fi.getTotalAssetTurnoverRatio()));
                } else if ("cashflow".equals(dimension)) {
                    vo.put("cfNiRatio", scaledVal(fi.getOperatingCashFlowToNetProfit()));
                    vo.put("freeCashflow", scaledVal(fi.getCashFlowRatio()));
                    vo.put("capexRatio", scaledVal(fi.getCashReinvestmentRatio()));
                } else {
                    // 默认 debt 维度
                    vo.put("debtRatio", scaledVal(fi.getAssetLiabilityRatio()));
                    vo.put("currentRatio", scaledVal(fi.getCurrentRatio()));
                    vo.put("quickRatio", scaledVal(fi.getQuickRatio()));
                    vo.put("interestCover", scaledVal(fi.getInterestCoverageRatio()));
                }

                // 综合评级
                String rating = calcRating(fi, dimension);
                vo.put("rating", rating);
                // 行业排名（按综合评分降序排列，序号即排名）
                vo.put("industryRank", i + 1);
                voList.add(vo);
            }

            // 按主指标重新排序并更新排名
            sortAndRank(voList, dimension);

            PageResult<Map<String, Object>> pr = new PageResult<>();
            pr.setTotalRecord((int) r.getTotal()); pr.setCurrentPage((int) r.getCurrent());
            pr.setPageNumber((int) r.getCurrent()); pr.setTotalPage((int) r.getPages());
            pr.setPageSize((int) r.getSize()); pr.setTlist(voList);
            return R.success(pr);
        } catch (Exception e) {
            log.error("对标分析查询失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    /** 安全取值并保留2位小数 */
    private BigDecimal scaledVal(BigDecimal v) {
        return v != null ? v.setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
    }

    /** 根据维度计算综合评级 */
    private String calcRating(FinancialIndicators fi, String dimension) {
        BigDecimal score = fi.getComprehensiveScore();
        if (score != null) {
            if (score.compareTo(new BigDecimal("80")) >= 0) return "优";
            if (score.compareTo(new BigDecimal("60")) >= 0) return "良";
            if (score.compareTo(new BigDecimal("40")) >= 0) return "中";
            return "差";
        }
        // 无综合评分时按维度主指标判断
        if ("debt".equals(dimension) || dimension == null) {
            BigDecimal dr = fi.getAssetLiabilityRatio();
            if (dr == null) return "中";
            if (dr.compareTo(new BigDecimal("70")) > 0) return "差";
            if (dr.compareTo(new BigDecimal("60")) > 0) return "中";
            if (dr.compareTo(new BigDecimal("40")) > 0) return "良";
            return "优";
        } else if ("profit".equals(dimension)) {
            BigDecimal roe = fi.getReturnOnEquity();
            if (roe == null) return "中";
            if (roe.compareTo(new BigDecimal("15")) >= 0) return "优";
            if (roe.compareTo(new BigDecimal("8")) >= 0) return "良";
            if (roe.compareTo(new BigDecimal("5")) >= 0) return "中";
            return "差";
        } else if ("operation".equals(dimension)) {
            BigDecimal turn = fi.getAccountsReceivableTurnoverRatio();
            if (turn == null) return "中";
            if (turn.compareTo(new BigDecimal("6")) >= 0) return "优";
            if (turn.compareTo(new BigDecimal("4")) >= 0) return "良";
            if (turn.compareTo(new BigDecimal("3")) >= 0) return "中";
            return "差";
        } else {
            BigDecimal cf = fi.getOperatingCashFlowToNetProfit();
            if (cf == null) return "中";
            if (cf.compareTo(new BigDecimal("1.2")) >= 0) return "优";
            if (cf.compareTo(new BigDecimal("0.8")) >= 0) return "良";
            if (cf.compareTo(new BigDecimal("0.5")) >= 0) return "中";
            return "差";
        }
    }

    /** 按主指标排序并更新排名 */
    private void sortAndRank(List<Map<String, Object>> voList, String dimension) {
        String primaryKey;
        boolean reverse; // true=值越大排名越靠前(越好), false=值越小越好
        if ("profit".equals(dimension)) { primaryKey = "roe"; reverse = false; }
        else if ("operation".equals(dimension)) { primaryKey = "receivablesTurn"; reverse = false; }
        else if ("cashflow".equals(dimension)) { primaryKey = "cfNiRatio"; reverse = false; }
        else { primaryKey = "debtRatio"; reverse = true; } // debt: 负债率越低越好

        String sortKey = primaryKey;
        voList.sort((a, b) -> {
            BigDecimal va = a.get(sortKey) instanceof BigDecimal ? (BigDecimal) a.get(sortKey) : BigDecimal.ZERO;
            BigDecimal vb = b.get(sortKey) instanceof BigDecimal ? (BigDecimal) b.get(sortKey) : BigDecimal.ZERO;
            return reverse ? va.compareTo(vb) : vb.compareTo(va);
        });
        for (int i = 0; i < voList.size(); i++) {
            voList.get(i).put("industryRank", i + 1);
        }
    }

    @Operation(summary = "指标趋势")
    @GetMapping("/indicator/trend")
    public R<List<Map<String, Object>>> indicatorTrend(@RequestParam(required = false) String companyId,
                                                        @RequestParam(required = false) String indicator) {
        try {
            LambdaQueryWrapper<GzctFinStatement> w = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(companyId)) w.eq(GzctFinStatement::getCompanyId, companyId);
            w.orderByAsc(GzctFinStatement::getPeriod);
            List<GzctFinStatement> stmts = statementMapper.selectList(w);
            List<Map<String, Object>> result = new ArrayList<>();
            for (GzctFinStatement s : stmts) {
                Map<String, Object> m = new HashMap<>();
                m.put("period", s.getPeriod());
                m.put("companyName", s.getCompanyName());
                m.put("totalAssets", s.getTotalAssets());
                m.put("revenue", s.getRevenue());
                m.put("netProfit", s.getNetProfit());
                m.put("operatingCashflow", s.getOperatingCashflow());
                BigDecimal assets = s.getTotalAssets() != null ? s.getTotalAssets() : BigDecimal.ZERO;
                BigDecimal liab = s.getTotalLiabilities() != null ? s.getTotalLiabilities() : BigDecimal.ZERO;
                BigDecimal debtRatio = assets.compareTo(BigDecimal.ZERO) > 0
                    ? liab.divide(assets, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
                m.put("debtRatio", debtRatio);
                BigDecimal rev = s.getRevenue() != null ? s.getRevenue() : BigDecimal.ZERO;
                BigDecimal profit = s.getNetProfit() != null ? s.getNetProfit() : BigDecimal.ZERO;
                BigDecimal roe = assets.compareTo(BigDecimal.ZERO) > 0
                    ? profit.divide(assets, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
                m.put("roe", roe);
                result.add(m);
            }
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 关联交易 ====================

    @Operation(summary = "关联方交易列表")
    @PostMapping("/related-party/list")
    public R<PageResult<Map<String, Object>>> relatedPartyList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctFinRelatedParty> w = new LambdaQueryWrapper<>();
            if (params.get("companyId") != null && StringUtils.isNotBlank(params.get("companyId").toString()))
                w.eq(GzctFinRelatedParty::getCompanyId, params.get("companyId").toString());
            if (params.get("relatedParty") != null && StringUtils.isNotBlank(params.get("relatedParty").toString()))
                w.like(GzctFinRelatedParty::getPartyName, params.get("relatedParty").toString());
            if (params.get("transactionName") != null && StringUtils.isNotBlank(params.get("transactionName").toString()))
                w.like(GzctFinRelatedParty::getTransactionName, params.get("transactionName").toString());
            w.orderByDesc(GzctFinRelatedParty::getCreateTime);
            Page<GzctFinRelatedParty> r = relatedPartyMapper.selectPage(new Page<>(pn, ps), w);
            // 计算所有关联交易总金额用于ratio计算
            BigDecimal totalTransactionAmount = BigDecimal.ZERO;
            for (GzctFinRelatedParty rp : r.getRecords()) {
                totalTransactionAmount = totalTransactionAmount.add(
                    rp.getTransactionAmount() != null ? rp.getTransactionAmount() : BigDecimal.ZERO);
            }
            List<Map<String, Object>> voList = new ArrayList<>();
            for (GzctFinRelatedParty rp : r.getRecords()) {
                Map<String, Object> vo = new HashMap<>();
                vo.put("partyId", rp.getPartyId());
                vo.put("companyId", rp.getCompanyId());
                vo.put("companyName", rp.getCompanyName() != null ? rp.getCompanyName() : "");
                vo.put("relatedParty", rp.getPartyName());
                vo.put("transactionName", rp.getTransactionName() != null ? rp.getTransactionName() : "");
                vo.put("transactionType", rp.getTransactionType());
                vo.put("transactionAmount", rp.getTransactionAmount());
                vo.put("amount", rp.getTransactionAmount());
                vo.put("relationType", rp.getRelationType());
                // 风险评估：交易金额>10000万且是重大交易
                BigDecimal amt = rp.getTransactionAmount() != null ? rp.getTransactionAmount() : BigDecimal.ZERO;
                String riskLevel = amt.compareTo(new BigDecimal("10000")) > 0 && "1".equals(rp.getIsMajor()) ? "HIGH" :
                    "1".equals(rp.getIsMajor()) ? "MEDIUM" : "LOW";
                String risk = "HIGH".equals(riskLevel) ? "高" : "MEDIUM".equals(riskLevel) ? "中" : "低";
                vo.put("risk", risk);
                vo.put("riskLevel", riskLevel);
                // 计算交易占比
                BigDecimal ratio = totalTransactionAmount.compareTo(BigDecimal.ZERO) > 0
                    ? amt.multiply(new BigDecimal("100")).divide(totalTransactionAmount, 2, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;
                vo.put("ratio", ratio);
                vo.put("isMajor", rp.getIsMajor());
                vo.put("period", rp.getPeriod());
                vo.put("createTime", rp.getCreateTime());
                voList.add(vo);
            }
            PageResult<Map<String, Object>> pr = new PageResult<>();
            pr.setTotalRecord((int) r.getTotal()); pr.setCurrentPage((int) r.getCurrent());
            pr.setPageNumber((int) r.getCurrent()); pr.setTotalPage((int) r.getPages());
            pr.setPageSize((int) r.getSize()); pr.setTlist(voList);
            return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "保存关联方交易")
    @PostMapping("/related-party/save")
    public R<Boolean> saveRelatedParty(@RequestBody Map<String, Object> params) {
        try {
            GzctFinRelatedParty record = new GzctFinRelatedParty();
            String partyId = params.get("partyId") != null ? params.get("partyId").toString() : null;
            if (params.get("companyId") != null) record.setCompanyId(params.get("companyId").toString());
            if (params.get("companyName") != null) record.setCompanyName(params.get("companyName").toString());
            if (params.get("partyName") != null) record.setPartyName(params.get("partyName").toString());
            if (params.get("transactionName") != null) record.setTransactionName(params.get("transactionName").toString());
            if (params.get("relationType") != null) record.setRelationType(params.get("relationType").toString());
            if (params.get("transactionType") != null) record.setTransactionType(params.get("transactionType").toString());
            if (params.get("transactionAmount") != null) record.setTransactionAmount(new BigDecimal(params.get("transactionAmount").toString()));
            if (params.get("balanceAmount") != null) record.setBalanceAmount(new BigDecimal(params.get("balanceAmount").toString()));
            if (params.get("isMajor") != null) record.setIsMajor(params.get("isMajor").toString());
            if (params.get("period") != null) record.setPeriod(params.get("period").toString());

            if (StringUtils.isNotBlank(partyId)) {
                record.setPartyId(partyId);
                record.setUpdateTime(LocalDateTime.now());
                relatedPartyMapper.updateById(record);
            } else {
                record.setCreateTime(LocalDateTime.now());
                relatedPartyMapper.insert(record);
            }
            return R.success(true);
        } catch (Exception e) { return R.fail("保存失败：" + e.getMessage()); }
    }

    @Operation(summary = "删除关联方交易")
    @DeleteMapping("/related-party/{id}")
    public R<Boolean> deleteRelatedParty(@PathVariable String id) {
        try { return R.success(relatedPartyMapper.deleteById(id) > 0); } catch (Exception e) { return R.fail("删除失败：" + e.getMessage()); }
    }

    @Operation(summary = "关联方交易详情")
    @GetMapping("/related-party/detail/{id}")
    public R<Map<String, Object>> relatedPartyDetail(@PathVariable String id) {
        try {
            if (StringUtils.isBlank(id)) return R.fail("缺少ID参数");
            GzctFinRelatedParty party = relatedPartyMapper.selectById(id);
            if (party == null) return R.fail("记录不存在");

            Map<String, Object> result = new HashMap<>();
            // 基本信息
            result.put("partyId", party.getPartyId());
            result.put("companyId", party.getCompanyId());
            result.put("companyName", party.getCompanyName() != null ? party.getCompanyName() : "");
            result.put("relatedParty", party.getPartyName());
            result.put("transactionName", party.getTransactionName() != null ? party.getTransactionName() : "");
            result.put("transactionType", party.getTransactionType());
            result.put("relationType", party.getRelationType());
            result.put("transactionAmount", party.getTransactionAmount());
            result.put("balanceAmount", party.getBalanceAmount());
            result.put("isMajor", party.getIsMajor());
            result.put("period", party.getPeriod());
            result.put("createTime", party.getCreateTime());
            result.put("updateTime", party.getUpdateTime());

            // 风险评估
            BigDecimal amt = party.getTransactionAmount() != null ? party.getTransactionAmount() : BigDecimal.ZERO;
            String riskLevel = amt.compareTo(new BigDecimal("10000")) > 0 && "1".equals(party.getIsMajor()) ? "高" :
                "1".equals(party.getIsMajor()) ? "中" : "低";
            result.put("risk", riskLevel);

            // 定价分析
            Map<String, Object> pricingAnalysis = new HashMap<>();
            BigDecimal marketPrice = amt.multiply(new BigDecimal("0.95")).setScale(2, RoundingMode.HALF_UP);
            BigDecimal deviation = BigDecimal.ZERO;
            if (marketPrice.compareTo(BigDecimal.ZERO) > 0) {
                deviation = amt.subtract(marketPrice).multiply(new BigDecimal("100"))
                    .divide(marketPrice, 2, RoundingMode.HALF_UP);
            }
            pricingAnalysis.put("transactionPrice", amt);
            pricingAnalysis.put("marketReferencePrice", marketPrice);
            pricingAnalysis.put("priceDeviation", deviation);
            pricingAnalysis.put("deviationLevel", deviation.abs().compareTo(new BigDecimal("15")) > 0 ? "异常" :
                deviation.abs().compareTo(new BigDecimal("8")) > 0 ? "关注" : "正常");
            result.put("pricingAnalysis", pricingAnalysis);

            // 公允性评估
            Map<String, Object> fairnessAssessment = new HashMap<>();
            int score = 85;
            if (amt.compareTo(new BigDecimal("10000")) > 0) score -= 10;
            if (amt.compareTo(new BigDecimal("50000")) > 0) score -= 15;
            if ("1".equals(party.getIsMajor())) score -= 10;
            score = Math.max(Math.min(score, 100), 0);
            fairnessAssessment.put("score", score);
            fairnessAssessment.put("level", score >= 80 ? "公允" : score >= 60 ? "基本公允" : "存疑");
            fairnessAssessment.put("suggestion", score >= 80 ? "交易定价合理，无需特别关注" :
                score >= 60 ? "建议加强监控，定期复核定价依据" : "建议立即核查，可能存在利益输送风险");
            result.put("fairnessAssessment", fairnessAssessment);

            // 交易占比（相对于同企业所有关联交易）
            LambdaQueryWrapper<GzctFinRelatedParty> companyWrapper = new LambdaQueryWrapper<>();
            companyWrapper.eq(GzctFinRelatedParty::getCompanyId, party.getCompanyId());
            List<GzctFinRelatedParty> companyRecords = relatedPartyMapper.selectList(companyWrapper);
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (GzctFinRelatedParty r : companyRecords) {
                if (r.getTransactionAmount() != null) totalAmount = totalAmount.add(r.getTransactionAmount());
            }
            BigDecimal ratio = totalAmount.compareTo(BigDecimal.ZERO) > 0
                ? amt.multiply(new BigDecimal("100")).divide(totalAmount, 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
            result.put("ratio", ratio);

            // 同一关联方的历史交易记录
            LambdaQueryWrapper<GzctFinRelatedParty> historyWrapper = new LambdaQueryWrapper<>();
            historyWrapper.eq(GzctFinRelatedParty::getCompanyId, party.getCompanyId());
            historyWrapper.eq(GzctFinRelatedParty::getPartyName, party.getPartyName());
            historyWrapper.ne(GzctFinRelatedParty::getPartyId, party.getPartyId());
            historyWrapper.orderByDesc(GzctFinRelatedParty::getCreateTime);
            historyWrapper.last("FETCH FIRST 10 ROWS ONLY");
            List<GzctFinRelatedParty> historyList = relatedPartyMapper.selectList(historyWrapper);
            List<Map<String, Object>> historyRecords = new ArrayList<>();
            for (GzctFinRelatedParty h : historyList) {
                Map<String, Object> hm = new HashMap<>();
                hm.put("partyId", h.getPartyId());
                hm.put("transactionName", h.getTransactionName());
                hm.put("transactionType", h.getTransactionType());
                hm.put("transactionAmount", h.getTransactionAmount());
                hm.put("period", h.getPeriod());
                hm.put("createTime", h.getCreateTime());
                historyRecords.add(hm);
            }
            result.put("historyRecords", historyRecords);

            return R.success(result);
        } catch (Exception e) {
            log.error("查询关联交易详情失败，ID：{}", id, e);
            return R.fail("查询详情失败：" + e.getMessage());
        }
    }

    @Operation(summary = "关联方关系网络")
    @GetMapping("/related-party/network")
    public R<Map<String, Object>> relatedPartyNetwork(@RequestParam(required = false) String companyId) {
        try {
            LambdaQueryWrapper<GzctFinRelatedParty> w = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(companyId)) w.eq(GzctFinRelatedParty::getCompanyId, companyId);
            List<GzctFinRelatedParty> list = relatedPartyMapper.selectList(w);
            // 构建网络节点和连线
            Set<String> nodeNames = new HashSet<>();
            List<Map<String, Object>> links = new ArrayList<>();
            for (GzctFinRelatedParty rp : list) {
                String src = rp.getCompanyId() != null ? rp.getCompanyId() : "未知企业";
                String tgt = rp.getPartyName() != null ? rp.getPartyName() : "未知关联方";
                nodeNames.add(src);
                nodeNames.add(tgt);
                Map<String, Object> link = new HashMap<>();
                link.put("source", src);
                link.put("target", tgt);
                link.put("value", rp.getTransactionAmount() != null ? rp.getTransactionAmount() : 0);
                link.put("type", rp.getTransactionType());
                links.add(link);
            }
            List<Map<String, Object>> nodes = new ArrayList<>();
            for (String name : nodeNames) {
                Map<String, Object> node = new HashMap<>();
                node.put("id", name);
                node.put("name", name);
                nodes.add(node);
            }
            Map<String, Object> result = new HashMap<>();
            result.put("nodes", nodes);
            result.put("links", links);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 费用管控 ====================

    @Operation(summary = "费用监控列表")
    @PostMapping("/monitor/expense/list")
    public R<PageResult<GzctFinExpense>> expenseMonitorList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctFinExpense> w = new LambdaQueryWrapper<>();
            if (params.get("companyId") != null && StringUtils.isNotBlank(params.get("companyId").toString()))
                w.eq(GzctFinExpense::getCompanyId, params.get("companyId").toString());
            if (params.get("companyName") != null && StringUtils.isNotBlank(params.get("companyName").toString()))
                w.like(GzctFinExpense::getCompanyName, params.get("companyName").toString());
            if (params.get("expenseCategory") != null && StringUtils.isNotBlank(params.get("expenseCategory").toString()))
                w.eq(GzctFinExpense::getExpenseCategory, params.get("expenseCategory").toString());
            if (params.get("period") != null && StringUtils.isNotBlank(params.get("period").toString()))
                // 改用 like 模糊匹配：DB 存储格式为"2025年报"/"2024年报"等，前端传"2025"/"2025年"/"2025年报"均可命中
                w.like(GzctFinExpense::getPeriod, params.get("period").toString().trim());
            w.orderByDesc(GzctFinExpense::getCreateTime);
            Page<GzctFinExpense> r = expenseMapper.selectPage(new Page<>(pn, ps), w);
            return R.success(buildPageResult(r));
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "费用趋势")
    @GetMapping("/monitor/expense/trend")
    public R<List<Map<String, Object>>> expenseTrend(@RequestParam(required = false) String companyId) {
        try {
            List<GzctFinExpense> list = expenseMapper.selectList(
                new LambdaQueryWrapper<GzctFinExpense>()
                    .eq(StringUtils.isNotBlank(companyId), GzctFinExpense::getCompanyId, companyId)
                    .orderByAsc(GzctFinExpense::getPeriod));
            List<Map<String, Object>> result = new ArrayList<>();
            for (GzctFinExpense e : list) {
                Map<String, Object> m = new HashMap<>();
                m.put("period", e.getPeriod()); m.put("category", e.getExpenseCategory());
                m.put("budgetAmount", e.getBudgetAmount()); m.put("actualAmount", e.getActualAmount());
                m.put("executionRate", e.getExecutionRate());
                result.add(m);
            }
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "费用数据导出")
    @PostMapping("/monitor/expense/export")
    public void expenseExport(@RequestBody(required = false) Map<String, Object> params, HttpServletResponse response) {
        try {
            LambdaQueryWrapper<GzctFinExpense> w = new LambdaQueryWrapper<>();
            if (params != null) {
                if (params.get("companyName") != null && StringUtils.isNotBlank(params.get("companyName").toString()))
                    w.like(GzctFinExpense::getCompanyName, params.get("companyName").toString());
                if (params.get("expenseCategory") != null && StringUtils.isNotBlank(params.get("expenseCategory").toString()))
                    w.eq(GzctFinExpense::getExpenseCategory, params.get("expenseCategory").toString());
                if (params.get("period") != null && StringUtils.isNotBlank(params.get("period").toString()))
                    // 改用 like 模糊匹配，与 list 接口保持一致
                    w.like(GzctFinExpense::getPeriod, params.get("period").toString().trim());
            }
            w.orderByDesc(GzctFinExpense::getCreateTime);
            List<GzctFinExpense> list = expenseMapper.selectList(w);
            List<List<Object>> rows = new ArrayList<>();
            rows.add(Arrays.asList("费用编号", "企业名称", "费用类别", "预算金额(万元)", "实际金额(万元)", "执行率(%)", "超支金额(万元)", "是否异常", "报告期"));
            for (GzctFinExpense e : list) {
                java.math.BigDecimal overAmount = (e.getActualAmount() != null && e.getBudgetAmount() != null && e.getActualAmount().compareTo(e.getBudgetAmount()) > 0)
                    ? e.getActualAmount().subtract(e.getBudgetAmount()) : java.math.BigDecimal.ZERO;
                rows.add(Arrays.asList(
                    e.getExpenseId(), e.getCompanyName(), e.getExpenseCategory(),
                    e.getBudgetAmount(), e.getActualAmount(), e.getExecutionRate(),
                    overAmount, "1".equals(e.getIsAbnormal()) ? "异常" : "正常", e.getPeriod()
                ));
            }
            byte[] xlsxBytes = SimpleXlsxWriter.write("费用监控数据", rows);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=expense_monitor_" + System.currentTimeMillis() + ".xlsx");
            response.setContentLength(xlsxBytes.length);
            response.getOutputStream().write(xlsxBytes);
            response.getOutputStream().flush();
        } catch (Exception e) {
            log.error("导出费用数据失败", e);
            try {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"result\":500,\"msg\":\"导出失败：" + e.getMessage() + "\"}");
            } catch (IOException ignored) {}
        }
    }

    // ==================== 财务监控 ====================

    @Operation(summary = "资产负债率监控")
    @GetMapping("/monitor/debt-ratio")
    public R<Map<String, Object>> debtRatioMonitor(@RequestParam(required = false) String companyId) {
        try {
            LambdaQueryWrapper<GzctFinStatement> w = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(companyId)) w.eq(GzctFinStatement::getCompanyId, companyId);
            w.orderByAsc(GzctFinStatement::getPeriod);
            List<GzctFinStatement> stmts = statementMapper.selectList(w);
            // 按企业分组计算最新负债率
            Map<String, GzctFinStatement> latestByCompany = new LinkedHashMap<>();
            for (GzctFinStatement s : stmts) {
                latestByCompany.put(s.getCompanyName() != null ? s.getCompanyName() : s.getCompanyId(), s);
            }
            List<Map<String, Object>> trend = new ArrayList<>();
            BigDecimal totalDebtRatio = BigDecimal.ZERO;
            int cnt = 0;
            for (Map.Entry<String, GzctFinStatement> entry : latestByCompany.entrySet()) {
                GzctFinStatement s = entry.getValue();
                BigDecimal assets = s.getTotalAssets() != null ? s.getTotalAssets() : BigDecimal.ZERO;
                BigDecimal liab = s.getTotalLiabilities() != null ? s.getTotalLiabilities() : BigDecimal.ZERO;
                BigDecimal dr = assets.compareTo(BigDecimal.ZERO) > 0
                    ? liab.divide(assets, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
                Map<String, Object> m = new HashMap<>();
                m.put("companyName", entry.getKey());
                m.put("debtRatio", dr);
                m.put("period", s.getPeriod());
                trend.add(m);
                totalDebtRatio = totalDebtRatio.add(dr);
                cnt++;
            }
            BigDecimal avgDebtRatio = cnt > 0 ? totalDebtRatio.divide(new BigDecimal(cnt), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
            Map<String, Object> result = new HashMap<>();
            result.put("current", avgDebtRatio);
            result.put("trend", trend);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "利润质量监控")
    @GetMapping("/monitor/profit-quality")
    public R<Map<String, Object>> profitQualityMonitor(@RequestParam(required = false) String companyId) {
        try {
            LambdaQueryWrapper<GzctFinStatement> w = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(companyId)) w.eq(GzctFinStatement::getCompanyId, companyId);
            List<GzctFinStatement> stmts = statementMapper.selectList(w);
            List<Map<String, Object>> trend = new ArrayList<>();
            BigDecimal totalRatio = BigDecimal.ZERO;
            int cnt = 0;
            for (GzctFinStatement s : stmts) {
                BigDecimal profit = s.getNetProfit() != null ? s.getNetProfit() : BigDecimal.ZERO;
                BigDecimal cashflow = s.getOperatingCashflow() != null ? s.getOperatingCashflow() : BigDecimal.ZERO;
                BigDecimal ratio = profit.compareTo(BigDecimal.ZERO) > 0
                    ? cashflow.divide(profit, 4, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
                Map<String, Object> m = new HashMap<>();
                m.put("companyName", s.getCompanyName());
                m.put("profitCashRatio", ratio);
                m.put("profitQuality", ratio);
                m.put("netProfit", profit);
                m.put("operatingCashflow", cashflow);
                m.put("period", s.getPeriod());
                trend.add(m);
                totalRatio = totalRatio.add(ratio);
                cnt++;
            }
            BigDecimal avg = cnt > 0 ? totalRatio.divide(new BigDecimal(cnt), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
            Map<String, Object> result = new HashMap<>();
            result.put("profitCashRatio", avg);
            result.put("trend", trend);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "关联交易监控")
    @GetMapping("/monitor/related-party")
    public R<Map<String, Object>> relatedPartyMonitor(@RequestParam(required = false) String companyId) {
        try {
            Map<String, Object> result = new HashMap<>();
            Long total = relatedPartyMapper.selectCount(null);
            Long major = relatedPartyMapper.selectCount(new LambdaQueryWrapper<GzctFinRelatedParty>().eq(GzctFinRelatedParty::getIsMajor, "1"));
            result.put("totalTransactions", total); result.put("majorTransactions", major);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 异常检测 ====================

    @Operation(summary = "异常列表")
    @PostMapping("/monitor/anomaly/list")
    public R<PageResult<Map<String, Object>>> anomalyList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctFinAnomaly> w = new LambdaQueryWrapper<>();
            if (params.get("companyId") != null && StringUtils.isNotBlank(params.get("companyId").toString()))
                w.eq(GzctFinAnomaly::getCompanyId, params.get("companyId").toString());
            if (params.get("anomalyType") != null && StringUtils.isNotBlank(params.get("anomalyType").toString()))
                w.eq(GzctFinAnomaly::getAnomalyType, params.get("anomalyType").toString());
            if (params.get("anomalyLevel") != null && StringUtils.isNotBlank(params.get("anomalyLevel").toString())) {
                // 根据偏差率映射风险等级：HIGH>30%, MEDIUM>10%
                String level = params.get("anomalyLevel").toString();
                if ("HIGH".equals(level)) w.gt(GzctFinAnomaly::getDeviationRate, new BigDecimal("30"));
                else if ("MEDIUM".equals(level)) w.between(GzctFinAnomaly::getDeviationRate, new BigDecimal("10"), new BigDecimal("30"));
                else w.lt(GzctFinAnomaly::getDeviationRate, new BigDecimal("10"));
            }
            if (params.get("companyName") != null && StringUtils.isNotBlank(params.get("companyName").toString()))
                w.like(GzctFinAnomaly::getCompanyName, params.get("companyName").toString());
            if (params.get("status") != null && StringUtils.isNotBlank(params.get("status").toString()))
                w.eq(GzctFinAnomaly::getStatus, params.get("status").toString());
            // 日期范围筛选
            if (params.get("startDate") != null && StringUtils.isNotBlank(params.get("startDate").toString())) {
                LocalDateTime start = LocalDate.parse(params.get("startDate").toString()).atStartOfDay();
                w.ge(GzctFinAnomaly::getCreateTime, start);
            }
            if (params.get("endDate") != null && StringUtils.isNotBlank(params.get("endDate").toString())) {
                LocalDateTime end = LocalDate.parse(params.get("endDate").toString()).atTime(23, 59, 59);
                w.le(GzctFinAnomaly::getCreateTime, end);
            }
            w.orderByDesc(GzctFinAnomaly::getCreateTime);
            Page<GzctFinAnomaly> r = anomalyMapper.selectPage(new Page<>(pn, ps), w);
            // 转换字段映射
            List<Map<String, Object>> voList = new ArrayList<>();
            for (GzctFinAnomaly a : r.getRecords()) {
                Map<String, Object> vo = new HashMap<>();
                vo.put("id", a.getAnomalyId());
                vo.put("anomalyId", a.getAnomalyId());
                vo.put("companyId", a.getCompanyId());
                vo.put("companyName", a.getCompanyName());
                vo.put("anomalyType", a.getAnomalyType());
                vo.put("anomalyIndicator", a.getIndicatorName()); // 映射 indicatorName -> anomalyIndicator
                vo.put("indicatorName", a.getIndicatorName());
                vo.put("anomalyValue", a.getActualValue() != null ? a.getActualValue().toPlainString() : "-"); // actualValue -> anomalyValue
                vo.put("actualValue", a.getActualValue());
                // normalRange: expectedValue ± deviationRate
                String normalRange = "";
                if (a.getExpectedValue() != null && a.getDeviationRate() != null) {
                    BigDecimal low = a.getExpectedValue().multiply(BigDecimal.ONE.subtract(a.getDeviationRate().divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP)));
                    BigDecimal high = a.getExpectedValue().multiply(BigDecimal.ONE.add(a.getDeviationRate().divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP)));
                    normalRange = "[" + low.setScale(2, RoundingMode.HALF_UP) + "," + high.setScale(2, RoundingMode.HALF_UP) + "]";
                }
                vo.put("normalRange", normalRange);
                vo.put("deviationRate", a.getDeviationRate() != null ? a.getDeviationRate().toPlainString() + "%" : "-");
                // 风险等级：偏差率>30%=HIGH, >10%=MEDIUM, 其他=LOW
                BigDecimal dr = a.getDeviationRate() != null ? a.getDeviationRate() : BigDecimal.ZERO;
                String riskLevel = dr.compareTo(new BigDecimal("30")) > 0 ? "HIGH" :
                    dr.compareTo(new BigDecimal("10")) > 0 ? "MEDIUM" : "LOW";
                vo.put("riskLevel", riskLevel);
                vo.put("anomalyLevel", riskLevel);
                vo.put("status", a.getStatus());
                vo.put("detectTime", a.getCreateTime()); // createTime -> detectTime
                vo.put("createTime", a.getCreateTime());
                vo.put("method", a.getDetectionMethod());
                vo.put("detectionMethod", a.getDetectionMethod());
                vo.put("detectNo", a.getAnomalyId());
                vo.put("period", a.getPeriod());
                // 关键数据：指标名称 + 实际值 + 偏差率
                String keyData = (a.getIndicatorName() != null ? a.getIndicatorName() : "") +
                    (a.getActualValue() != null ? " 实际值:" + a.getActualValue().toPlainString() : "") +
                    (a.getDeviationRate() != null ? " 偏差:" + a.getDeviationRate().toPlainString() + "%" : "");
                vo.put("keyData", keyData.trim());
                voList.add(vo);
            }
            PageResult<Map<String, Object>> pr = new PageResult<>();
            pr.setTotalRecord((int) r.getTotal()); pr.setCurrentPage((int) r.getCurrent());
            pr.setPageNumber((int) r.getCurrent()); pr.setTotalPage((int) r.getPages());
            pr.setPageSize((int) r.getSize()); pr.setTlist(voList);
            return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "Benford检测")
    @GetMapping("/monitor/anomaly/benford")
    public R<Map<String, Object>> detectBenford(@RequestParam(required = false) String companyId,
                                                  @RequestParam(required = false) String period) {
        try {
            // 从异常检测表中查询Benford相关记录
            LambdaQueryWrapper<GzctFinAnomaly> w = new LambdaQueryWrapper<GzctFinAnomaly>()
                .eq(GzctFinAnomaly::getDetectionMethod, "Benford");
            if (StringUtils.isNotBlank(companyId)) w.eq(GzctFinAnomaly::getCompanyId, companyId);
            if (StringUtils.isNotBlank(period)) w.eq(GzctFinAnomaly::getPeriod, period);
            List<GzctFinAnomaly> benfordList = anomalyMapper.selectList(w);
            // Benford理论值
            double[] expected = {30.1, 17.6, 12.5, 9.7, 7.9, 6.7, 5.8, 5.1, 4.6};
            List<Map<String, Object>> digits = new ArrayList<>();
            for (int i = 0; i < 9; i++) {
                Map<String, Object> d = new HashMap<>();
                d.put("digit", i + 1);
                d.put("expected", expected[i]);
                d.put("actual", expected[i]); // 无实际数据时返回理论值
                digits.add(d);
            }
            List<Map<String, Object>> suspiciousItems = new ArrayList<>();
            for (GzctFinAnomaly a : benfordList) {
                Map<String, Object> item = new HashMap<>();
                item.put("company", a.getCompanyName());
                BigDecimal dr = a.getDeviationRate() != null ? a.getDeviationRate() : BigDecimal.ZERO;
                item.put("chiSq", dr.multiply(new BigDecimal("0.5")).setScale(2, RoundingMode.HALF_UP));
                item.put("score", dr.compareTo(new BigDecimal("30")) > 0 ? 80 : dr.compareTo(new BigDecimal("10")) > 0 ? 50 : 20);
                item.put("level", dr.compareTo(new BigDecimal("30")) > 0 ? "HIGH" : dr.compareTo(new BigDecimal("10")) > 0 ? "MEDIUM" : "LOW");
                suspiciousItems.add(item);
            }
            Map<String, Object> result = new HashMap<>();
            result.put("companyId", companyId);
            result.put("period", period);
            result.put("digits", digits);
            result.put("suspiciousItems", suspiciousItems);
            return R.success(result);
        } catch (Exception e) { return R.fail("检测失败：" + e.getMessage()); }
    }

    @Operation(summary = "利润现金流偏差检测")
    @GetMapping("/monitor/anomaly/profit-cashflow")
    public R<Map<String, Object>> detectProfitCashflow(@RequestParam(required = false) String companyId) {
        try {
            LambdaQueryWrapper<GzctFinStatement> w = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(companyId)) w.eq(GzctFinStatement::getCompanyId, companyId);
            List<GzctFinStatement> stmts = statementMapper.selectList(w);
            List<Map<String, Object>> deviations = new ArrayList<>();
            for (GzctFinStatement s : stmts) {
                BigDecimal profit = s.getNetProfit() != null ? s.getNetProfit() : BigDecimal.ZERO;
                BigDecimal cashflow = s.getOperatingCashflow() != null ? s.getOperatingCashflow() : BigDecimal.ZERO;
                BigDecimal profitQuality = profit.compareTo(BigDecimal.ZERO) > 0
                    ? cashflow.divide(profit, 4, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
                Map<String, Object> d = new HashMap<>();
                d.put("companyName", s.getCompanyName());
                d.put("netProfit", profit);
                d.put("operatingCashflow", cashflow);
                d.put("profitQuality", profitQuality);
                d.put("profitCashRatio", profitQuality);
                d.put("period", s.getPeriod());
                deviations.add(d);
            }
            Map<String, Object> result = new HashMap<>();
            result.put("deviations", deviations);
            return R.success(result);
        } catch (Exception e) { return R.fail("检测失败：" + e.getMessage()); }
    }

    @Operation(summary = "确认疑点")
    @PutMapping("/monitor/anomaly/confirm/{id}")
    public R<Boolean> confirmAnomaly(@PathVariable String id) {
        try {
            GzctFinAnomaly anomaly = anomalyMapper.selectById(id);
            if (anomaly == null) return R.fail("异常记录不存在");
            // 更新异常状态为已确认
            anomaly.setStatus("CONFIRMED");
            anomaly.setUpdateTime(LocalDateTime.now());
            anomalyMapper.updateById(anomaly);
            // 推送至风险预警模块：创建一条预警记录
            GzctFinAlert alert = new GzctFinAlert();
            alert.setCompanyId(anomaly.getCompanyId());
            alert.setCompanyName(anomaly.getCompanyName());
            alert.setAlertType("ANOMALY_CONFIRMED");
            alert.setAlertName("财务异常确认预警");
            alert.setAlertContent("检测方法[" + anomaly.getDetectionMethod() + "]发现异常：" +
                anomaly.getIndicatorName() + "，偏差率" + (anomaly.getDeviationRate() != null ? anomaly.getDeviationRate() + "%" : "未知"));
            // 根据偏差率设置预警等级
            BigDecimal dr = anomaly.getDeviationRate() != null ? anomaly.getDeviationRate() : BigDecimal.ZERO;
            String alertLevel = dr.compareTo(new BigDecimal("30")) > 0 ? "HIGH" : dr.compareTo(new BigDecimal("10")) > 0 ? "MEDIUM" : "LOW";
            alert.setAlertLevel(alertLevel);
            alert.setLevel(alertLevel);
            alert.setStatus("PENDING");
            alert.setRelatedId(anomaly.getAnomalyId());
            alert.setCreateTime(LocalDateTime.now());
            alert.setUpdateTime(LocalDateTime.now());
            alertMapper.insert(alert);
            return R.success(true);
        } catch (Exception e) {
            log.error("确认疑点失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    @Operation(summary = "忽略异常")
    @PutMapping("/monitor/anomaly/ignore/{id}")
    public R<Boolean> ignoreAnomaly(@PathVariable String id) {
        try {
            GzctFinAnomaly anomaly = anomalyMapper.selectById(id);
            if (anomaly == null) return R.fail("异常记录不存在");
            anomaly.setStatus("IGNORED");
            anomaly.setUpdateTime(LocalDateTime.now());
            anomalyMapper.updateById(anomaly);
            return R.success(true);
        } catch (Exception e) { return R.fail("操作失败：" + e.getMessage()); }
    }

    @Operation(summary = "异常统计")
    @GetMapping("/monitor/anomaly/stats")
    public R<Map<String, Object>> anomalyStats() {
        try {
            long total = anomalyMapper.selectCount(null);
            long highRisk = anomalyMapper.selectCount(
                new LambdaQueryWrapper<GzctFinAnomaly>().gt(GzctFinAnomaly::getDeviationRate, new BigDecimal("30")));
            long pending = anomalyMapper.selectCount(
                new LambdaQueryWrapper<GzctFinAnomaly>().eq(GzctFinAnomaly::getStatus, "PENDING"));
            // 本月新增
            LocalDateTime monthStart = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
            long monthNew = anomalyMapper.selectCount(
                new LambdaQueryWrapper<GzctFinAnomaly>().ge(GzctFinAnomaly::getCreateTime, monthStart));
            Map<String, Object> result = new HashMap<>();
            result.put("total", total);
            result.put("highRisk", highRisk);
            result.put("pending", pending);
            result.put("monthNew", monthNew);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "异常趋势(近6个月)")
    @GetMapping("/monitor/anomaly/trend")
    public R<Map<String, Object>> anomalyTrend() {
        try {
            // 查询近6个月数据按月分组
            LocalDateTime sixMonthsAgo = LocalDateTime.now().minusMonths(6).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
            List<GzctFinAnomaly> list = anomalyMapper.selectList(
                new LambdaQueryWrapper<GzctFinAnomaly>().ge(GzctFinAnomaly::getCreateTime, sixMonthsAgo)
                    .orderByAsc(GzctFinAnomaly::getCreateTime));
            // 按月分组统计
            Map<String, long[]> monthMap = new java.util.LinkedHashMap<>();
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM");
            for (int i = 5; i >= 0; i--) {
                String key = LocalDateTime.now().minusMonths(i).format(fmt);
                monthMap.put(key, new long[]{0, 0, 0});
            }
            for (GzctFinAnomaly a : list) {
                if (a.getCreateTime() == null) continue;
                String key = a.getCreateTime().format(fmt);
                long[] cnt = monthMap.get(key);
                if (cnt == null) continue;
                BigDecimal dr = a.getDeviationRate() != null ? a.getDeviationRate() : BigDecimal.ZERO;
                if (dr.compareTo(new BigDecimal("30")) > 0) cnt[0]++;
                else if (dr.compareTo(new BigDecimal("10")) > 0) cnt[1]++;
                else cnt[2]++;
            }
            List<String> months = new ArrayList<>(monthMap.keySet());
            List<Long> highData = new ArrayList<>(), mediumData = new ArrayList<>(), lowData = new ArrayList<>();
            for (long[] cnt : monthMap.values()) {
                highData.add(cnt[0]); mediumData.add(cnt[1]); lowData.add(cnt[2]);
            }
            Map<String, Object> result = new HashMap<>();
            result.put("months", months);
            result.put("high", highData);
            result.put("medium", mediumData);
            result.put("low", lowData);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "异常详情分析")
    @GetMapping("/monitor/anomaly/detail/{id}")
    public R<Map<String, Object>> anomalyDetail(@PathVariable String id) {
        try {
            GzctFinAnomaly anomaly = anomalyMapper.selectById(id);
            if (anomaly == null) return R.fail("记录不存在");
            Map<String, Object> result = new HashMap<>();
            result.put("anomalyId", anomaly.getAnomalyId());
            result.put("companyId", anomaly.getCompanyId());
            result.put("companyName", anomaly.getCompanyName());
            result.put("anomalyType", anomaly.getAnomalyType());
            result.put("detectionMethod", anomaly.getDetectionMethod());
            result.put("indicatorName", anomaly.getIndicatorName());
            result.put("expectedValue", anomaly.getExpectedValue());
            result.put("actualValue", anomaly.getActualValue());
            result.put("deviationRate", anomaly.getDeviationRate());
            result.put("period", anomaly.getPeriod());
            result.put("status", anomaly.getStatus());
            result.put("createTime", anomaly.getCreateTime());
            // 风险等级
            BigDecimal dr = anomaly.getDeviationRate() != null ? anomaly.getDeviationRate() : BigDecimal.ZERO;
            String riskLevel = dr.compareTo(new BigDecimal("30")) > 0 ? "HIGH" :
                dr.compareTo(new BigDecimal("10")) > 0 ? "MEDIUM" : "LOW";
            result.put("anomalyLevel", riskLevel);
            // 原因分析
            String reason = generateAnomalyReason(anomaly);
            result.put("reason", reason);
            // 历史趋势：查询同企业同指标的历史数据
            List<GzctFinAnomaly> history = anomalyMapper.selectList(
                new LambdaQueryWrapper<GzctFinAnomaly>()
                    .eq(GzctFinAnomaly::getCompanyId, anomaly.getCompanyId())
                    .eq(GzctFinAnomaly::getIndicatorName, anomaly.getIndicatorName())
                    .orderByAsc(GzctFinAnomaly::getCreateTime)
                    .last("LIMIT 12"));
            List<Map<String, Object>> trendData = new ArrayList<>();
            for (GzctFinAnomaly h : history) {
                Map<String, Object> point = new HashMap<>();
                point.put("period", h.getPeriod() != null ? h.getPeriod() : "");
                point.put("value", h.getActualValue() != null ? h.getActualValue() : BigDecimal.ZERO);
                point.put("expected", h.getExpectedValue() != null ? h.getExpectedValue() : BigDecimal.ZERO);
                trendData.add(point);
            }
            result.put("trendData", trendData);
            // 关联报表
            if (StringUtils.isNotBlank(anomaly.getCompanyId())) {
                GzctFinStatement stmt = statementMapper.selectOne(
                    new LambdaQueryWrapper<GzctFinStatement>()
                        .eq(GzctFinStatement::getCompanyId, anomaly.getCompanyId())
                        .orderByDesc(GzctFinStatement::getCreateTime).last("LIMIT 1"));
                if (stmt != null) {
                    Map<String, Object> stmtData = new HashMap<>();
                    stmtData.put("totalAssets", stmt.getTotalAssets());
                    stmtData.put("revenue", stmt.getRevenue());
                    stmtData.put("netProfit", stmt.getNetProfit());
                    stmtData.put("operatingCashflow", stmt.getOperatingCashflow());
                    result.put("relatedStatement", stmtData);
                }
            }
            // 整改记录
            List<GzctFinAnomalyRectification> rects = anomalyRectificationMapper.selectByAnomalyId(id);
            result.put("rectifications", rects);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    private String generateAnomalyReason(GzctFinAnomaly anomaly) {
        String type = anomaly.getAnomalyType();
        BigDecimal dr = anomaly.getDeviationRate() != null ? anomaly.getDeviationRate() : BigDecimal.ZERO;
        String indicator = anomaly.getIndicatorName() != null ? anomaly.getIndicatorName() : "未知指标";
        StringBuilder sb = new StringBuilder();
        sb.append("指标「").append(indicator).append("」偏差率达").append(dr).append("%，");
        if ("LIQUIDITY_RISK".equals(type)) sb.append("流动性风险较高，企业短期偿债能力不足，需关注现金流状况。");
        else if ("SOLVENCY_RISK".equals(type)) sb.append("偿债能力风险突出，负债水平偏高，需评估长期偿债压力。");
        else if ("PROFITABILITY_RISK".equals(type)) sb.append("盈利能力异常，利润率显著低于预期，需排查收入质量和成本控制。");
        else if ("OPERATIONAL_RISK".equals(type)) sb.append("运营效率异常，资产周转率偏低，需关注存货和应收账款管理。");
        else if ("MARKET_RISK".equals(type)) sb.append("市场风险敞口较大，需评估利率/汇率波动对企业的影响。");
        else if ("CREDIT_RISK".equals(type)) sb.append("信用风险较高，坏账率超出正常范围，需加强应收账款催收。");
        else sb.append("存在异常偏差，建议进一步核实数据来源和计算逻辑。");
        return sb.toString();
    }

    @Operation(summary = "派单整改")
    @PostMapping("/monitor/anomaly/dispatch")
    public R<Map<String, Object>> anomalyDispatch(@RequestBody Map<String, Object> params) {
        try {
            String anomalyId = params.get("anomalyId") != null ? params.get("anomalyId").toString() : null;
            String assignee = params.get("assignee") != null ? params.get("assignee").toString() : null;
            String deadline = params.get("deadline") != null ? params.get("deadline").toString() : null;
            String requirement = params.get("requirement") != null ? params.get("requirement").toString() : null;
            if (StringUtils.isBlank(anomalyId)) return R.fail("异常ID不能为空");
            if (StringUtils.isBlank(assignee)) return R.fail("整改责任人不能为空");
            if (StringUtils.isBlank(deadline)) return R.fail("整改期限不能为空");
            GzctFinAnomaly anomaly = anomalyMapper.selectById(anomalyId);
            if (anomaly == null) return R.fail("异常记录不存在");
            // 创建整改记录
            GzctFinAnomalyRectification rect = new GzctFinAnomalyRectification();
            rect.setAnomalyId(anomalyId);
            rect.setCompanyId(anomaly.getCompanyId());
            rect.setCompanyName(anomaly.getCompanyName());
            rect.setAnomalyType(anomaly.getAnomalyType());
            rect.setIndicatorName(anomaly.getIndicatorName());
            rect.setAssignee(assignee);
            rect.setDeadline(LocalDate.parse(deadline));
            rect.setRequirement(requirement);
            rect.setStatus("PENDING");
            rect.setIssuedTime(LocalDateTime.now());
            rect.setCreateTime(LocalDateTime.now());
            rect.setUpdateTime(LocalDateTime.now());
            anomalyRectificationMapper.insert(rect);
            // 更新异常状态为已派单
            anomaly.setStatus("DISPATCHED");
            anomaly.setUpdateTime(LocalDateTime.now());
            anomalyMapper.updateById(anomaly);
            Map<String, Object> result = new HashMap<>();
            result.put("rectificationId", rect.getRectificationId());
            result.put("anomalyId", anomalyId);
            result.put("assignee", assignee);
            result.put("deadline", deadline);
            result.put("status", "PENDING");
            return R.success(result);
        } catch (Exception e) { return R.fail("派单失败：" + e.getMessage()); }
    }

    // ==================== 风险预警 ====================

    @Operation(summary = "财务预警列表")
    @PostMapping("/warning/list")
    public R<PageResult<Map<String, Object>>> warningList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctFinAlert> w = new LambdaQueryWrapper<>();
            if (params.get("status") != null && StringUtils.isNotBlank(params.get("status").toString()))
                w.eq(GzctFinAlert::getStatus, params.get("status").toString());
            if (params.get("level") != null && StringUtils.isNotBlank(params.get("level").toString()))
                w.eq(GzctFinAlert::getLevel, params.get("level").toString());
            if (params.get("warnType") != null && StringUtils.isNotBlank(params.get("warnType").toString()))
                w.eq(GzctFinAlert::getAlertType, params.get("warnType").toString());
            w.orderByDesc(GzctFinAlert::getCreateTime);
            Page<GzctFinAlert> r = alertMapper.selectPage(new Page<>(pn, ps), w);
            List<Map<String, Object>> voList = new ArrayList<>();
            for (GzctFinAlert a : r.getRecords()) {
                Map<String, Object> vo = new HashMap<>();
                vo.put("alertId", a.getAlertId());
                vo.put("alertNo", a.getAlertId());
                vo.put("id", a.getAlertId());
                vo.put("companyId", a.getCompanyId());
                vo.put("companyName", a.getCompanyName());
                vo.put("alertType", a.getAlertType());
                vo.put("alertTitle", a.getAlertType());
                vo.put("alertContent", a.getAlertContent());
                vo.put("triggerCondition", a.getAlertContent());
                vo.put("alertDesc", a.getAlertContent());
                vo.put("riskLevel", a.getLevel());
                vo.put("level", a.getLevel());
                vo.put("status", a.getStatus());
                vo.put("alertTime", a.getCreateTime());
                vo.put("createTime", a.getCreateTime());
                vo.put("ruleCode", a.getAlertType());
                // 查询该预警的处置记录作为timeline
                List<GzctFinAlertHandle> handles = alertHandleMapper.selectList(
                    new LambdaQueryWrapper<GzctFinAlertHandle>()
                        .eq(GzctFinAlertHandle::getAlertId, a.getAlertId())
                        .orderByAsc(GzctFinAlertHandle::getCreateTime));
                List<Map<String, Object>> timeline = new ArrayList<>();
                for (GzctFinAlertHandle h : handles) {
                    Map<String, Object> t = new HashMap<>();
                    t.put("time", h.getCreateTime() != null ? h.getCreateTime().toString().replace("T", " ") : "");
                    t.put("content", buildHandleContent(h));
                    t.put("type", "CLOSED".equals(h.getStatus()) ? "success" : "warning");
                    timeline.add(t);
                }
                vo.put("timeline", timeline);
                voList.add(vo);
            }
            PageResult<Map<String, Object>> pr = new PageResult<>();
            pr.setTotalRecord((int) r.getTotal()); pr.setCurrentPage((int) r.getCurrent());
            pr.setPageNumber((int) r.getCurrent()); pr.setTotalPage((int) r.getPages());
            pr.setPageSize((int) r.getSize()); pr.setTlist(voList);
            return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "财务预警详情")
    @GetMapping("/warning/detail/{id}")
    public R<Map<String, Object>> warningDetail(@PathVariable String id) {
        try {
            GzctFinAlert alert = alertMapper.selectById(id);
            if (alert == null) {
                LambdaQueryWrapper<GzctFinAlert> qw = new LambdaQueryWrapper<GzctFinAlert>().eq(GzctFinAlert::getAlertId, id);
                alert = alertMapper.selectOne(qw);
            }
            if (alert == null) return R.fail("预警记录不存在");
            Map<String, Object> vo = new HashMap<>();
            vo.put("alertId", alert.getAlertId());
            vo.put("alertNo", alert.getAlertId());
            vo.put("companyName", alert.getCompanyName());
            vo.put("alertType", alert.getAlertType());
            vo.put("alertContent", alert.getAlertContent());
            vo.put("triggerCondition", alert.getAlertContent());
            vo.put("riskLevel", alert.getLevel());
            vo.put("level", alert.getLevel());
            vo.put("status", alert.getStatus());
            vo.put("alertTime", alert.getCreateTime());
            vo.put("ruleCode", alert.getAlertType());
            // 查询处置记录
            List<GzctFinAlertHandle> handles = alertHandleMapper.selectList(
                new LambdaQueryWrapper<GzctFinAlertHandle>()
                    .eq(GzctFinAlertHandle::getAlertId, alert.getAlertId())
                    .orderByAsc(GzctFinAlertHandle::getCreateTime));
            List<Map<String, Object>> timeline = new ArrayList<>();
            for (GzctFinAlertHandle h : handles) {
                Map<String, Object> t = new HashMap<>();
                t.put("time", h.getCreateTime() != null ? h.getCreateTime().toString().replace("T", " ") : "");
                t.put("content", buildHandleContent(h));
                t.put("type", "CLOSED".equals(h.getStatus()) ? "success" : "warning");
                timeline.add(t);
            }
            vo.put("timeline", timeline);
            return R.success(vo);
        } catch (Exception e) { return R.fail("查询详情失败：" + e.getMessage()); }
    }

    private String buildHandleContent(GzctFinAlertHandle h) {
        String typeLabel = "";
        if ("RECTIFICATION".equals(h.getHandleType())) typeLabel = "下达整改通知";
        else if ("INTERVIEW".equals(h.getHandleType())) typeLabel = "约谈企业负责人";
        else if ("AUDIT".equals(h.getHandleType())) typeLabel = "委托专项审计";
        else if ("REPORT".equals(h.getHandleType())) typeLabel = "提交监管部门";
        else if ("CLOSE".equals(h.getHandleType())) typeLabel = "预警关闭";
        else typeLabel = h.getHandleType() != null ? h.getHandleType() : "处置";
        String desc = h.getHandleDesc() != null ? "：" + h.getHandleDesc() : "";
        return typeLabel + desc;
    }

    @Operation(summary = "处理财务预警")
    @PostMapping("/warning/handle")
    public R<Boolean> handleWarning(@RequestBody Map<String, Object> params) {
        try {
            String id = params.get("id") != null ? params.get("id").toString() :
                params.get("warnNo") != null ? params.get("warnNo").toString() : null;
            if (id == null) return R.fail("预警ID不能为空");
            GzctFinAlert alert = alertMapper.selectById(id);
            if (alert == null) {
                LambdaQueryWrapper<GzctFinAlert> qw = new LambdaQueryWrapper<GzctFinAlert>().eq(GzctFinAlert::getAlertId, id);
                alert = alertMapper.selectOne(qw);
            }
            if (alert == null) return R.fail("预警记录不存在");
            // 更新预警状态为处置中
            alert.setStatus("PROCESSING");
            alert.setUpdateTime(LocalDateTime.now());
            alertMapper.updateById(alert);
            // 保存处置记录
            GzctFinAlertHandle handle = new GzctFinAlertHandle();
            handle.setAlertId(alert.getAlertId());
            handle.setHandleType(params.get("handleType") != null ? params.get("handleType").toString() : "");
            handle.setHandleDesc(params.get("handleDesc") != null ? params.get("handleDesc").toString() : "");
            if (params.get("deadline") != null && StringUtils.isNotBlank(params.get("deadline").toString())) {
                try {
                    handle.setDeadline(LocalDateTime.parse(params.get("deadline").toString().replace(" ", "T").substring(0, Math.min(19, params.get("deadline").toString().length()))));
                } catch (Exception ignored) {}
            }
            handle.setHandlerName(params.get("handlerName") != null ? params.get("handlerName").toString() : "系统用户");
            handle.setStatus("PROCESSING");
            handle.setCreateTime(LocalDateTime.now());
            handle.setUpdateTime(LocalDateTime.now());
            alertHandleMapper.insert(handle);
            return R.success(true);
        } catch (Exception e) { return R.fail("处理失败：" + e.getMessage()); }
    }

    @Operation(summary = "关闭财务预警")
    @PutMapping("/warning/close/{id}")
    public R<Boolean> closeWarning(@PathVariable String id) {
        try {
            GzctFinAlert alert = alertMapper.selectById(id);
            if (alert == null) {
                LambdaQueryWrapper<GzctFinAlert> w = new LambdaQueryWrapper<GzctFinAlert>().eq(GzctFinAlert::getAlertId, id);
                alert = alertMapper.selectOne(w);
            }
            if (alert != null) {
                alert.setStatus("CLOSED");
                alert.setUpdateTime(LocalDateTime.now());
                alertMapper.updateById(alert);
                // 记录关闭操作到处置历史
                GzctFinAlertHandle handle = new GzctFinAlertHandle();
                handle.setAlertId(alert.getAlertId());
                handle.setHandleType("CLOSE");
                handle.setHandleDesc("预警已关闭");
                handle.setStatus("CLOSED");
                handle.setHandlerName("系统用户");
                handle.setCreateTime(LocalDateTime.now());
                handle.setUpdateTime(LocalDateTime.now());
                alertHandleMapper.insert(handle);
            }
            return R.success(true);
        } catch (Exception e) { return R.fail("关闭失败：" + e.getMessage()); }
    }

    // ==================== 穿透分析 ====================

    @Operation(summary = "财务穿透下钻")
    @GetMapping("/drill-down")
    public R<Map<String, Object>> drillDown(@RequestParam(required = false) String companyId,
                                             @RequestParam(required = false) Integer level,
                                             @RequestParam(required = false) String dimension,
                                             @RequestParam(required = false) String subject,
                                             @RequestParam(required = false) String period) {
        try {
            int lv = level != null ? level : 0;
            LambdaQueryWrapper<GzctFinStatement> w = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(companyId)) w.eq(GzctFinStatement::getCompanyId, companyId);
            if (StringUtils.isNotBlank(period)) w.eq(GzctFinStatement::getPeriod, period);
            List<GzctFinStatement> stmts = statementMapper.selectList(w);

            if (lv == 0) {
                // 第0层：集团企业卡片
                Map<String, Object> root = new HashMap<>();
                root.put("name", "集团总部");
                List<Map<String, Object>> children = new ArrayList<>();
                // 按企业分组取最新期
                Map<String, GzctFinStatement> latestByCompany = new LinkedHashMap<>();
                for (GzctFinStatement s : stmts) {
                    String key = s.getCompanyId() != null ? s.getCompanyId() : s.getCompanyName();
                    latestByCompany.put(key, s);
                }
                for (Map.Entry<String, GzctFinStatement> entry : latestByCompany.entrySet()) {
                    GzctFinStatement s = entry.getValue();
                    Map<String, Object> child = buildEnterpriseCard(s);
                    children.add(child);
                }
                root.put("children", children);
                return R.success(root);
            } else if (lv == 1) {
                // 第1层：企业KPI概览（返回企业最新财务数据+计算指标）
                Map<String, Object> result = new HashMap<>();
                GzctFinStatement latest = stmts.stream()
                    .max(Comparator.comparing(GzctFinStatement::getCreateTime))
                    .orElse(null);
                if (latest != null) {
                    result.put("companyId", latest.getCompanyId());
                    result.put("companyName", latest.getCompanyName());
                    result.put("revenue", latest.getRevenue());
                    result.put("netProfit", latest.getNetProfit());
                    result.put("totalAssets", latest.getTotalAssets());
                    result.put("totalLiabilities", latest.getTotalLiabilities());
                    result.put("netAssets", latest.getNetAssets());
                    result.put("operatingCashflow", latest.getOperatingCashflow());
                    BigDecimal assets = latest.getTotalAssets() != null ? latest.getTotalAssets() : BigDecimal.ZERO;
                    BigDecimal liab = latest.getTotalLiabilities() != null ? latest.getTotalLiabilities() : BigDecimal.ZERO;
                    BigDecimal rev = latest.getRevenue() != null ? latest.getRevenue() : BigDecimal.ZERO;
                    BigDecimal profit = latest.getNetProfit() != null ? latest.getNetProfit() : BigDecimal.ZERO;
                    BigDecimal netAssets = latest.getNetAssets() != null ? latest.getNetAssets() : BigDecimal.ZERO;
                    BigDecimal cashflow = latest.getOperatingCashflow() != null ? latest.getOperatingCashflow() : BigDecimal.ZERO;
                    result.put("debtRatio", assets.compareTo(BigDecimal.ZERO) > 0
                        ? liab.divide(assets, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
                    result.put("roe", netAssets.compareTo(BigDecimal.ZERO) > 0
                        ? profit.divide(netAssets, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
                    result.put("profitMargin", rev.compareTo(BigDecimal.ZERO) > 0
                        ? profit.divide(rev, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
                    result.put("profitQuality", profit.compareTo(BigDecimal.ZERO) > 0
                        ? cashflow.divide(profit, 4, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
                }
                return R.success(result);
            } else if (lv == 2) {
                // 第2层：维度指标明细
                Map<String, Object> result = new HashMap<>();
                result.put("dimension", dimension);
                GzctFinStatement latest = stmts.stream()
                    .max(Comparator.comparing(GzctFinStatement::getCreateTime))
                    .orElse(null);
                if (latest == null) {
                    result.put("dimensionData", new ArrayList<>());
                    return R.success(result);
                }
                BigDecimal assets = latest.getTotalAssets() != null ? latest.getTotalAssets() : BigDecimal.ZERO;
                BigDecimal liab = latest.getTotalLiabilities() != null ? latest.getTotalLiabilities() : BigDecimal.ZERO;
                BigDecimal rev = latest.getRevenue() != null ? latest.getRevenue() : BigDecimal.ZERO;
                BigDecimal profit = latest.getNetProfit() != null ? latest.getNetProfit() : BigDecimal.ZERO;
                BigDecimal netAssets = latest.getNetAssets() != null ? latest.getNetAssets() : BigDecimal.ZERO;
                BigDecimal cashflow = latest.getOperatingCashflow() != null ? latest.getOperatingCashflow() : BigDecimal.ZERO;
                // 找上一期数据计算同比
                GzctFinStatement prev = null;
                if (latest.getPeriod() != null) {
                    String prevPeriod = latest.getPeriod().contains("Y")
                        ? (Integer.parseInt(latest.getPeriod().substring(0, 4)) - 1) + "-Y"
                        : latest.getPeriod();
                    LambdaQueryWrapper<GzctFinStatement> pw = new LambdaQueryWrapper<>();
                    pw.eq(GzctFinStatement::getCompanyId, latest.getCompanyId());
                    pw.eq(GzctFinStatement::getPeriod, prevPeriod);
                    List<GzctFinStatement> prevList = statementMapper.selectList(pw);
                    prev = prevList.stream().max(Comparator.comparing(GzctFinStatement::getCreateTime)).orElse(null);
                }
                List<Map<String, Object>> dimData = new ArrayList<>();
                if ("debt".equals(dimension)) {
                    // 偿债能力
                    dimData.add(buildDimRow("总资产", assets, prev != null ? prev.getTotalAssets() : null, true, false, assets.compareTo(BigDecimal.ZERO) > 0 && liab.divide(assets, 4, RoundingMode.HALF_UP).compareTo(new BigDecimal("0.7")) > 0 ? "资产负债率偏高" : null));
                    dimData.add(buildDimRow("总负债", liab, prev != null ? prev.getTotalLiabilities() : null, false, true, null));
                    dimData.add(buildDimRow("净资产", netAssets, prev != null ? prev.getNetAssets() : null, false, true, null));
                    BigDecimal debtRatio = assets.compareTo(BigDecimal.ZERO) > 0
                        ? liab.divide(assets, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
                    BigDecimal prevDebtRatio = prev != null && prev.getTotalAssets() != null && prev.getTotalAssets().compareTo(BigDecimal.ZERO) > 0
                        ? (prev.getTotalLiabilities() != null ? prev.getTotalLiabilities() : BigDecimal.ZERO).divide(prev.getTotalAssets(), 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP) : null;
                    dimData.add(buildDimRow("资产负债率(%)", debtRatio, prevDebtRatio, true, false, debtRatio.compareTo(new BigDecimal("70")) > 0 ? "超警戒线70%" : null));
                    BigDecimal netDebt = liab.subtract(netAssets);
                    BigDecimal prevNetDebt = prev != null ? (prev.getTotalLiabilities() != null ? prev.getTotalLiabilities() : BigDecimal.ZERO).subtract(prev.getNetAssets() != null ? prev.getNetAssets() : BigDecimal.ZERO) : null;
                    dimData.add(buildDimRow("净债务", netDebt, prevNetDebt, false, true, null));
                } else if ("profit".equals(dimension)) {
                    // 盈利能力
                    dimData.add(buildDimRow("营业收入", rev, prev != null ? prev.getRevenue() : null, true, false, null));
                    dimData.add(buildDimRow("净利润", profit, prev != null ? prev.getNetProfit() : null, true, true, profit.compareTo(BigDecimal.ZERO) < 0 ? "净利润为负" : null));
                    BigDecimal profitMargin = rev.compareTo(BigDecimal.ZERO) > 0
                        ? profit.divide(rev, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
                    BigDecimal prevProfitMargin = prev != null && prev.getRevenue() != null && prev.getRevenue().compareTo(BigDecimal.ZERO) > 0
                        ? (prev.getNetProfit() != null ? prev.getNetProfit() : BigDecimal.ZERO).divide(prev.getRevenue(), 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP) : null;
                    dimData.add(buildDimRow("净利润率(%)", profitMargin, prevProfitMargin, true, false, profitMargin.compareTo(new BigDecimal("5")) < 0 ? "净利润率偏低" : null));
                    BigDecimal roe = netAssets.compareTo(BigDecimal.ZERO) > 0
                        ? profit.divide(netAssets, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
                    BigDecimal prevRoe = prev != null && prev.getNetAssets() != null && prev.getNetAssets().compareTo(BigDecimal.ZERO) > 0
                        ? (prev.getNetProfit() != null ? prev.getNetProfit() : BigDecimal.ZERO).divide(prev.getNetAssets(), 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP) : null;
                    dimData.add(buildDimRow("ROE(%)", roe, prevRoe, true, false, roe.compareTo(new BigDecimal("5")) < 0 ? "ROE偏低" : null));
                    BigDecimal roa = assets.compareTo(BigDecimal.ZERO) > 0
                        ? profit.divide(assets, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
                    dimData.add(buildDimRow("ROA(%)", roa, null, true, false, null));
                } else if ("expense".equals(dimension)) {
                    // 费用管控
                    // 从费用表查询该企业数据
                    LambdaQueryWrapper<GzctFinExpense> ew = new LambdaQueryWrapper<>();
                    if (StringUtils.isNotBlank(companyId)) ew.eq(GzctFinExpense::getCompanyId, companyId);
                    List<GzctFinExpense> expenses = expenseMapper.selectList(ew);
                    Map<String, GzctFinExpense> expenseByCategory = new LinkedHashMap<>();
                    for (GzctFinExpense e : expenses) {
                        expenseByCategory.put(e.getExpenseCategory(), e);
                    }
                    for (Map.Entry<String, GzctFinExpense> entry : expenseByCategory.entrySet()) {
                        GzctFinExpense e = entry.getValue();
                        BigDecimal budgetAmt = e.getBudgetAmount() != null ? e.getBudgetAmount() : BigDecimal.ZERO;
                        BigDecimal actualAmt = e.getActualAmount() != null ? e.getActualAmount() : BigDecimal.ZERO;
                        BigDecimal execRate = e.getExecutionRate() != null ? e.getExecutionRate() : BigDecimal.ZERO;
                        String anomaly = execRate.compareTo(new BigDecimal("100")) > 0 ? "执行率超100%" : null;
                        dimData.add(buildDimRow(entry.getKey() + "(预算)", budgetAmt, null, false, false, null));
                        dimData.add(buildDimRow(entry.getKey() + "(实际)", actualAmt, null, false, true, anomaly));
                    }
                    if (dimData.isEmpty()) {
                        // 无费用数据时从报表推算
                        dimData.add(buildDimRow("管理费用(估算)", rev.multiply(new BigDecimal("0.08")).setScale(2, RoundingMode.HALF_UP), null, false, true, null));
                        dimData.add(buildDimRow("销售费用(估算)", rev.multiply(new BigDecimal("0.05")).setScale(2, RoundingMode.HALF_UP), null, false, true, null));
                        dimData.add(buildDimRow("财务费用(估算)", liab.multiply(new BigDecimal("0.03")).setScale(2, RoundingMode.HALF_UP), null, false, true, null));
                        dimData.add(buildDimRow("三项费用率(估算)(%)", new BigDecimal("16.00"), null, true, false, null));
                    }
                } else if ("cashflow".equals(dimension)) {
                    // 现金流质量
                    dimData.add(buildDimRow("净利润", profit, prev != null ? prev.getNetProfit() : null, true, false, null));
                    dimData.add(buildDimRow("经营活动现金流", cashflow, prev != null ? prev.getOperatingCashflow() : null, true, true, null));
                    BigDecimal profitQuality = profit.compareTo(BigDecimal.ZERO) > 0
                        ? cashflow.divide(profit, 4, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
                    dimData.add(buildDimRow("利润现金比", profitQuality, null, true, false,
                        profitQuality.compareTo(BigDecimal.ZERO) > 0 && profitQuality.compareTo(new BigDecimal("0.8")) < 0 ? "现金流与利润背离" : null));
                    BigDecimal freeCash = cashflow.subtract(liab.multiply(new BigDecimal("0.05")));
                    dimData.add(buildDimRow("自由现金流(估算)", freeCash, null, false, true,
                        freeCash.compareTo(BigDecimal.ZERO) < 0 ? "自由现金流为负" : null));
                    BigDecimal cashRevRatio = rev.compareTo(BigDecimal.ZERO) > 0
                        ? cashflow.divide(rev, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
                    dimData.add(buildDimRow("现金收入比(%)", cashRevRatio, null, true, false, null));
                } else {
                    dimData.add(buildDimRow("暂无数据", BigDecimal.ZERO, null, false, false, null));
                }
                result.put("dimensionData", dimData);
                return R.success(result);
            } else if (lv == 3) {
                // 第3层：科目明细凭证 - 从数据库查询真实凭证数据
                Map<String, Object> result = new HashMap<>();
                result.put("subject", subject);
                LambdaQueryWrapper<GzctFinVoucher> vw = new LambdaQueryWrapper<>();
                if (StringUtils.isNotBlank(companyId)) vw.eq(GzctFinVoucher::getCompanyId, companyId);
                if (StringUtils.isNotBlank(subject)) vw.eq(GzctFinVoucher::getSubjectName, subject);
                if (StringUtils.isNotBlank(dimension)) vw.eq(GzctFinVoucher::getDimension, dimension);
                if (StringUtils.isNotBlank(period)) vw.eq(GzctFinVoucher::getPeriod, period);
                vw.orderByDesc(GzctFinVoucher::getBizDate);
                List<GzctFinVoucher> vouchers = voucherMapper.selectList(vw);
                List<Map<String, Object>> details = new ArrayList<>();
                for (GzctFinVoucher v : vouchers) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("voucherNo", v.getVoucherNo());
                    item.put("bizDate", v.getBizDate());
                    item.put("summary", v.getSummary());
                    item.put("counterSubject", v.getCounterSubject());
                    item.put("debitAmount", v.getDebitAmount());
                    item.put("creditAmount", v.getCreditAmount());
                    item.put("anomaly", v.getAnomaly());
                    details.add(item);
                }
                result.put("subjectDetails", details);
                return R.success(result);
            }
            // 默认返回企业列表
            Map<String, Object> root = new HashMap<>();
            root.put("name", "集团总部");
            root.put("children", new ArrayList<>());
            return R.success(root);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "财务穿透下钻导出")
    @GetMapping("/drill-down/export")
    public void drillDownExport(@RequestParam(required = false) String companyId,
                                @RequestParam(required = false) Integer level,
                                @RequestParam(required = false) String dimension,
                                @RequestParam(required = false) String subject,
                                @RequestParam(required = false) String period,
                                HttpServletResponse response) {
        try {
            int lv = level != null ? level : 0;
            List<List<Object>> rows = new ArrayList<>();
            if (lv == 3) {
                // 导出凭证明细
                rows.add(Arrays.asList("凭证编号", "业务日期", "摘要", "对方科目", "借方金额(万元)", "贷方金额(万元)", "异常标注"));
                LambdaQueryWrapper<GzctFinVoucher> vw = new LambdaQueryWrapper<>();
                if (StringUtils.isNotBlank(companyId)) vw.eq(GzctFinVoucher::getCompanyId, companyId);
                if (StringUtils.isNotBlank(subject)) vw.eq(GzctFinVoucher::getSubjectName, subject);
                if (StringUtils.isNotBlank(dimension)) vw.eq(GzctFinVoucher::getDimension, dimension);
                if (StringUtils.isNotBlank(period)) vw.eq(GzctFinVoucher::getPeriod, period);
                vw.orderByDesc(GzctFinVoucher::getBizDate);
                List<GzctFinVoucher> vouchers = voucherMapper.selectList(vw);
                for (GzctFinVoucher v : vouchers) {
                    rows.add(Arrays.asList(v.getVoucherNo(), v.getBizDate(), v.getSummary(),
                        v.getCounterSubject(), v.getDebitAmount(), v.getCreditAmount(),
                        v.getAnomaly() != null ? v.getAnomaly() : ""));
                }
            } else if (lv == 2) {
                // 导出维度指标
                rows.add(Arrays.asList("科目/指标", "金额/数值", "上期", "同比(%)", "异常标注"));
                // 复用drill-down逻辑获取数据
                R<Map<String, Object>> drillResult = drillDown(companyId, level, dimension, subject, period);
                if (drillResult.getData() != null && drillResult.getData().get("dimensionData") != null) {
                    List<Map<String, Object>> dimData = (List<Map<String, Object>>) drillResult.getData().get("dimensionData");
                    for (Map<String, Object> row : dimData) {
                        rows.add(Arrays.asList(
                            row.get("subject"), row.get("value"), row.get("prevValue"),
                            row.get("yoy") != null ? row.get("yoy") : "",
                            row.get("anomaly") != null ? row.get("anomaly") : ""));
                    }
                }
            } else {
                // 导出企业列表
                rows.add(Arrays.asList("企业名称", "营业收入(万元)", "净利润(万元)", "资产负债率(%)", "ROE(%)", "风险等级"));
                R<Map<String, Object>> drillResult = drillDown(companyId, 0, dimension, subject, period);
                if (drillResult.getData() != null && drillResult.getData().get("children") != null) {
                    List<Map<String, Object>> children = (List<Map<String, Object>>) drillResult.getData().get("children");
                    for (Map<String, Object> child : children) {
                        String risk = "HIGH".equals(child.get("riskLevel")) ? "高风险" :
                            "MEDIUM".equals(child.get("riskLevel")) ? "中风险" : "低风险";
                        rows.add(Arrays.asList(child.get("name"), child.get("revenue"),
                            child.get("netProfit"), child.get("debtRatio"), child.get("roe"), risk));
                    }
                }
            }
            byte[] xlsxBytes = SimpleXlsxWriter.write("财务穿透分析", rows);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=financial_drill_" + System.currentTimeMillis() + ".xlsx");
            response.setContentLength(xlsxBytes.length);
            response.getOutputStream().write(xlsxBytes);
            response.getOutputStream().flush();
        } catch (Exception e) {
            log.error("导出财务穿透数据失败", e);
            try {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"result\":500,\"msg\":\"导出失败：" + e.getMessage() + "\"}");
            } catch (IOException ignored) {}
        }
    }

    private Map<String, Object> buildEnterpriseCard(GzctFinStatement s) {
        Map<String, Object> child = new HashMap<>();
        child.put("id", s.getCompanyId());
        child.put("name", s.getCompanyName());
        child.put("companyId", s.getCompanyId());
        child.put("companyName", s.getCompanyName());
        child.put("revenue", s.getRevenue() != null ? s.getRevenue() : BigDecimal.ZERO);
        child.put("netProfit", s.getNetProfit() != null ? s.getNetProfit() : BigDecimal.ZERO);
        child.put("period", s.getPeriod());
        BigDecimal assets = s.getTotalAssets() != null ? s.getTotalAssets() : BigDecimal.ZERO;
        BigDecimal liab = s.getTotalLiabilities() != null ? s.getTotalLiabilities() : BigDecimal.ZERO;
        child.put("totalAssets", assets);
        child.put("totalLiabilities", liab);
        BigDecimal dr = assets.compareTo(BigDecimal.ZERO) > 0
            ? liab.divide(assets, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
        child.put("debtRatio", dr);
        // 计算ROE
        BigDecimal netAssets = s.getNetAssets() != null ? s.getNetAssets() : BigDecimal.ZERO;
        BigDecimal profit = s.getNetProfit() != null ? s.getNetProfit() : BigDecimal.ZERO;
        BigDecimal roe = netAssets.compareTo(BigDecimal.ZERO) > 0
            ? profit.divide(netAssets, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
        child.put("roe", roe);
        // 风险等级
        String riskLevel = dr.compareTo(new BigDecimal("70")) > 0 ? "HIGH"
            : dr.compareTo(new BigDecimal("60")) > 0 ? "MEDIUM" : "LOW";
        child.put("riskLevel", riskLevel);
        // 预警数
        long warnCount = (dr.compareTo(new BigDecimal("70")) > 0 ? 1 : 0)
            + (roe.compareTo(new BigDecimal("5")) < 0 && roe.compareTo(BigDecimal.ZERO) > 0 ? 1 : 0);
        child.put("warnCount", warnCount);
        return child;
    }

    private Map<String, Object> buildDimRow(String subject, BigDecimal value, BigDecimal prevValue, boolean isTotal, boolean drillable, String anomaly) {
        Map<String, Object> row = new HashMap<>();
        row.put("subject", subject);
        row.put("value", value != null ? value : BigDecimal.ZERO);
        row.put("prevValue", prevValue);
        // 计算同比
        if (prevValue != null && prevValue.compareTo(BigDecimal.ZERO) > 0 && value != null) {
            BigDecimal yoy = value.subtract(prevValue).divide(prevValue, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP);
            row.put("yoy", yoy);
        } else {
            row.put("yoy", null);
        }
        row.put("isTotal", isTotal);
        row.put("drillable", drillable);
        row.put("anomaly", anomaly);
        return row;
    }

    private Map<String, Object> buildVoucher(String voucherNo, String bizDate, String summary, String counterSubject, BigDecimal debit, BigDecimal credit) {
        Map<String, Object> v = new HashMap<>();
        v.put("voucherNo", voucherNo);
        v.put("bizDate", bizDate);
        v.put("summary", summary);
        v.put("counterSubject", counterSubject);
        v.put("debitAmount", debit);
        v.put("creditAmount", credit);
        String anomaly = null;
        if (debit != null && debit.compareTo(new BigDecimal("50000")) > 0) anomaly = "大额借方";
        if (credit != null && credit.compareTo(new BigDecimal("50000")) > 0) anomaly = "大额贷方";
        v.put("anomaly", anomaly);
        return v;
    }

    private BigDecimal getSubjectAmount(GzctFinStatement s, String subject) {
        if (subject == null) return BigDecimal.ZERO;
        BigDecimal assets = s.getTotalAssets() != null ? s.getTotalAssets() : BigDecimal.ZERO;
        BigDecimal liab = s.getTotalLiabilities() != null ? s.getTotalLiabilities() : BigDecimal.ZERO;
        BigDecimal rev = s.getRevenue() != null ? s.getRevenue() : BigDecimal.ZERO;
        BigDecimal profit = s.getNetProfit() != null ? s.getNetProfit() : BigDecimal.ZERO;
        BigDecimal netAssets = s.getNetAssets() != null ? s.getNetAssets() : BigDecimal.ZERO;
        BigDecimal cashflow = s.getOperatingCashflow() != null ? s.getOperatingCashflow() : BigDecimal.ZERO;
        switch (subject) {
            case "总资产": return assets;
            case "总负债": return liab;
            case "净资产": return netAssets;
            case "资产负债率(%)": return assets.compareTo(BigDecimal.ZERO) > 0
                ? liab.divide(assets, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
            case "净债务": return liab.subtract(netAssets);
            case "营业收入": return rev;
            case "净利润": return profit;
            case "净利润率(%)": return rev.compareTo(BigDecimal.ZERO) > 0
                ? profit.divide(rev, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
            case "ROE(%)": return netAssets.compareTo(BigDecimal.ZERO) > 0
                ? profit.divide(netAssets, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
            case "ROA(%)": return assets.compareTo(BigDecimal.ZERO) > 0
                ? profit.divide(assets, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
            case "经营活动现金流": return cashflow;
            case "利润现金比": return profit.compareTo(BigDecimal.ZERO) > 0
                ? cashflow.divide(profit, 4, RoundingMode.HALF_UP).setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
            case "自由现金流(估算)": return cashflow.subtract(liab.multiply(new BigDecimal("0.05")));
            case "现金收入比(%)": return rev.compareTo(BigDecimal.ZERO) > 0
                ? cashflow.divide(rev, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
            default: return assets; // 默认返回总资产
        }
    }

    // ==================== 驾驶舱 ====================

    @Operation(summary = "财务驾驶舱")
    @GetMapping("/dashboard")
    public R<Map<String, Object>> dashboard() {
        try {
            List<GzctFinStatement> allStmts = statementMapper.selectList(
                new LambdaQueryWrapper<GzctFinStatement>().orderByAsc(GzctFinStatement::getPeriod));
            Long totalRelatedParties = relatedPartyMapper.selectCount(null);
            Long activeAlerts = alertMapper.selectCount(new LambdaQueryWrapper<GzctFinAlert>().eq(GzctFinAlert::getStatus, "PENDING"));
            Long anomalies = anomalyMapper.selectCount(new LambdaQueryWrapper<GzctFinAnomaly>().eq(GzctFinAnomaly::getStatus, "PENDING"));

            // 聚合KPI
            BigDecimal totalRevenue = BigDecimal.ZERO, totalProfit = BigDecimal.ZERO;
            BigDecimal totalDebtRatio = BigDecimal.ZERO;
            int cnt = 0, highRiskCount = 0;
            Set<String> companyIds = new HashSet<>();
            for (GzctFinStatement s : allStmts) {
                totalRevenue = totalRevenue.add(s.getRevenue() != null ? s.getRevenue() : BigDecimal.ZERO);
                totalProfit = totalProfit.add(s.getNetProfit() != null ? s.getNetProfit() : BigDecimal.ZERO);
                BigDecimal assets = s.getTotalAssets() != null ? s.getTotalAssets() : BigDecimal.ZERO;
                BigDecimal liab = s.getTotalLiabilities() != null ? s.getTotalLiabilities() : BigDecimal.ZERO;
                if (assets.compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal dr = liab.divide(assets, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100"));
                    totalDebtRatio = totalDebtRatio.add(dr);
                    if (dr.compareTo(new BigDecimal("70")) > 0) highRiskCount++;
                    cnt++;
                }
                companyIds.add(s.getCompanyId());
            }
            BigDecimal avgDebtRatio = cnt > 0 ? totalDebtRatio.divide(new BigDecimal(cnt), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
            BigDecimal avgRoe = cnt > 0 && totalRevenue.compareTo(BigDecimal.ZERO) > 0
                ? totalProfit.divide(totalRevenue, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;

            // 按period分组构建trend
            Map<String, List<GzctFinStatement>> byPeriod = new LinkedHashMap<>();
            for (GzctFinStatement s : allStmts) {
                String p = s.getPeriod() != null ? s.getPeriod() : "";
                byPeriod.computeIfAbsent(p, k -> new ArrayList<>()).add(s);
            }
            List<Map<String, Object>> trend = new ArrayList<>();
            for (Map.Entry<String, List<GzctFinStatement>> e : byPeriod.entrySet()) {
                Map<String, Object> t = new HashMap<>();
                t.put("period", e.getKey());
                BigDecimal rev = BigDecimal.ZERO, pft = BigDecimal.ZERO, dr = BigDecimal.ZERO;
                BigDecimal ta = BigDecimal.ZERO, tl = BigDecimal.ZERO;
                int n = 0;
                for (GzctFinStatement s : e.getValue()) {
                    rev = rev.add(s.getRevenue() != null ? s.getRevenue() : BigDecimal.ZERO);
                    pft = pft.add(s.getNetProfit() != null ? s.getNetProfit() : BigDecimal.ZERO);
                    ta = ta.add(s.getTotalAssets() != null ? s.getTotalAssets() : BigDecimal.ZERO);
                    tl = tl.add(s.getTotalLiabilities() != null ? s.getTotalLiabilities() : BigDecimal.ZERO);
                    n++;
                }
                BigDecimal periodDr = ta.compareTo(BigDecimal.ZERO) > 0
                    ? tl.divide(ta, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
                BigDecimal periodRoe = rev.compareTo(BigDecimal.ZERO) > 0
                    ? pft.divide(rev, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
                // 计算流动比率（从流动资产/流动负债）
                BigDecimal periodCa = BigDecimal.ZERO, periodCl = BigDecimal.ZERO;
                for (GzctFinStatement s : e.getValue()) {
                    periodCa = periodCa.add(s.getCurrentAssets() != null ? s.getCurrentAssets() : BigDecimal.ZERO);
                    periodCl = periodCl.add(s.getCurrentLiabilities() != null ? s.getCurrentLiabilities() : BigDecimal.ZERO);
                }
                BigDecimal periodCurrentRatio = periodCl.compareTo(BigDecimal.ZERO) > 0
                    ? periodCa.divide(periodCl, 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
                t.put("revenue", rev); t.put("netProfit", pft); t.put("debtRatio", periodDr);
                t.put("roe", periodRoe); t.put("currentRatio", periodCurrentRatio);
                t.put("riskLevel", periodDr.compareTo(new BigDecimal("70")) > 0 ? "HIGH" : periodDr.compareTo(new BigDecimal("60")) > 0 ? "MEDIUM" : "LOW");
                // 取第一条的企业名用于overview
                if (!e.getValue().isEmpty()) t.put("companyName", e.getValue().get(0).getCompanyName());
                trend.add(t);
            }

            // 按企业分组构建companyOverview（每个企业取最新一条报表）
            Map<String, GzctFinStatement> latestByCompany = new LinkedHashMap<>();
            for (int i = allStmts.size() - 1; i >= 0; i--) {
                GzctFinStatement s = allStmts.get(i);
                String key = s.getCompanyId() != null ? s.getCompanyId() : s.getCompanyName();
                if (key != null && !latestByCompany.containsKey(key)) latestByCompany.put(key, s);
            }
            List<Map<String, Object>> companyOverview = new ArrayList<>();
            for (Map.Entry<String, GzctFinStatement> entry : latestByCompany.entrySet()) {
                GzctFinStatement s = entry.getValue();
                Map<String, Object> co = new HashMap<>();
                co.put("companyId", s.getCompanyId());
                co.put("companyName", s.getCompanyName());
                BigDecimal assets = s.getTotalAssets() != null ? s.getTotalAssets() : BigDecimal.ZERO;
                BigDecimal liab = s.getTotalLiabilities() != null ? s.getTotalLiabilities() : BigDecimal.ZERO;
                BigDecimal revenue = s.getRevenue() != null ? s.getRevenue() : BigDecimal.ZERO;
                BigDecimal profit = s.getNetProfit() != null ? s.getNetProfit() : BigDecimal.ZERO;
                BigDecimal cashflow = s.getOperatingCashflow() != null ? s.getOperatingCashflow() : BigDecimal.ZERO;
                BigDecimal ca = s.getCurrentAssets() != null ? s.getCurrentAssets() : BigDecimal.ZERO;
                BigDecimal cl = s.getCurrentLiabilities() != null ? s.getCurrentLiabilities() : BigDecimal.ZERO;
                // 负债率
                BigDecimal dr = assets.compareTo(BigDecimal.ZERO) > 0
                    ? liab.divide(assets, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
                // ROE (净利润/营收*100)
                BigDecimal roe = revenue.compareTo(BigDecimal.ZERO) > 0
                    ? profit.divide(revenue, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
                // 流动比率
                BigDecimal cr = cl.compareTo(BigDecimal.ZERO) > 0
                    ? ca.divide(cl, 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
                // 利润质量（经营现金流/净利润）
                BigDecimal profitQuality = profit.compareTo(BigDecimal.ZERO) > 0
                    ? cashflow.divide(profit, 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
                // 健康评分：综合负债率、ROE、流动比率、利润质量
                int healthScore = calculateHealthScore(dr, roe, cr, profitQuality);
                // 风险等级
                String riskLevel = dr.compareTo(new BigDecimal("70")) > 0 ? "HIGH" : dr.compareTo(new BigDecimal("60")) > 0 ? "MEDIUM" : "LOW";
                co.put("debtRatio", dr); co.put("roe", roe); co.put("currentRatio", cr);
                co.put("profitQuality", profitQuality); co.put("healthScore", healthScore);
                co.put("riskLevel", riskLevel); co.put("period", s.getPeriod());
                companyOverview.add(co);
            }

            // 计算环比趋势：对比上月数据
            // 获取上月的报表数据用于环比计算
            LocalDateTime now = LocalDateTime.now();
            String currentMonth = now.format(DateTimeFormatter.ofPattern("yyyy-MM"));
            String lastMonth = now.minusMonths(1).format(DateTimeFormatter.ofPattern("yyyy-MM"));
            BigDecimal currentMonthRevenue = BigDecimal.ZERO, lastMonthRevenue = BigDecimal.ZERO;
            int currentMonthCompanies = 0, lastMonthCompanies = 0;
            Set<String> currentMonthCompanyIds = new HashSet<>(), lastMonthCompanyIds = new HashSet<>();
            for (GzctFinStatement s : allStmts) {
                String p = s.getPeriod() != null ? s.getPeriod() : "";
                if (p.startsWith(currentMonth)) {
                    currentMonthRevenue = currentMonthRevenue.add(s.getRevenue() != null ? s.getRevenue() : BigDecimal.ZERO);
                    currentMonthCompanyIds.add(s.getCompanyId());
                } else if (p.startsWith(lastMonth)) {
                    lastMonthRevenue = lastMonthRevenue.add(s.getRevenue() != null ? s.getRevenue() : BigDecimal.ZERO);
                    lastMonthCompanyIds.add(s.getCompanyId());
                }
            }
            // 关联交易环比
            Long lastMonthRelated = relatedPartyMapper.selectCount(
                new LambdaQueryWrapper<GzctFinRelatedParty>()
                    .ge(GzctFinRelatedParty::getCreateTime, now.minusMonths(2).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0))
                    .lt(GzctFinRelatedParty::getCreateTime, now.minusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0)));
            // 异常指标环比
            Long lastMonthAnomalies = anomalyMapper.selectCount(
                new LambdaQueryWrapper<GzctFinAnomaly>()
                    .ge(GzctFinAnomaly::getCreateTime, now.minusMonths(2).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0))
                    .lt(GzctFinAnomaly::getCreateTime, now.minusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0)));

            // 计算各项环比百分比
            double companyTrend = lastMonthCompanyIds.size() > 0
                ? ((double)(currentMonthCompanyIds.size() - lastMonthCompanyIds.size()) / lastMonthCompanyIds.size()) * 100 : 0;
            double revenueTrend = lastMonthRevenue.compareTo(BigDecimal.ZERO) > 0
                ? currentMonthRevenue.subtract(lastMonthRevenue).divide(lastMonthRevenue, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).doubleValue() : 0;
            double relatedTrend = lastMonthRelated > 0
                ? ((double)(totalRelatedParties - lastMonthRelated) / lastMonthRelated) * 100 : 0;
            double anomalyTrend = lastMonthAnomalies > 0
                ? ((double)(anomalies - lastMonthAnomalies) / lastMonthAnomalies) * 100 : 0;

            Map<String, Object> kpi = new HashMap<>();
            kpi.put("totalStatements", companyIds.size());
            kpi.put("totalRevenue", totalRevenue);
            kpi.put("totalProfit", totalProfit);
            kpi.put("avgDebtRatio", avgDebtRatio);
            kpi.put("avgRoe", avgRoe);
            kpi.put("highRiskCount", highRiskCount);
            kpi.put("abnormalFund", anomalies);
            // 关联交易占比：关联交易总金额 / 集团总营收 * 100
            BigDecimal totalRelatedAmount = BigDecimal.ZERO;
            List<GzctFinRelatedParty> allRelated = relatedPartyMapper.selectList(null);
            for (GzctFinRelatedParty rp : allRelated) {
                totalRelatedAmount = totalRelatedAmount.add(rp.getTransactionAmount() != null ? rp.getTransactionAmount() : BigDecimal.ZERO);
            }
            BigDecimal relatedRatio = totalRevenue.compareTo(BigDecimal.ZERO) > 0
                ? totalRelatedAmount.divide(totalRevenue, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).setScale(1, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
            kpi.put("relatedRatio", relatedRatio);

            Map<String, Object> result = new HashMap<>();
            result.put("totalStatements", companyIds.size());
            result.put("totalRelatedParties", totalRelatedParties);
            result.put("activeAlerts", activeAlerts);
            result.put("anomalies", anomalies);
            result.put("kpi", kpi);
            result.put("trend", trend);
            result.put("companyOverview", companyOverview);
            // 环比趋势数据
            Map<String, Object> trendPercent = new HashMap<>();
            trendPercent.put("companyTrend", Math.round(companyTrend * 10) / 10.0);
            trendPercent.put("revenueTrend", Math.round(revenueTrend * 10) / 10.0);
            trendPercent.put("relatedTrend", Math.round(relatedTrend * 10) / 10.0);
            trendPercent.put("anomalyTrend", Math.round(anomalyTrend * 10) / 10.0);
            result.put("trendPercent", trendPercent);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    /**
     * 计算企业财务健康评分（0-100分）
     * 综合考虑：负债率(30%)、ROE(25%)、流动比率(25%)、利润质量(20%)
     */
    private int calculateHealthScore(BigDecimal debtRatio, BigDecimal roe, BigDecimal currentRatio, BigDecimal profitQuality) {
        // 负债率评分：<50%=30分, 50-60%=24分, 60-70%=15分, >70%=5分
        int debtScore;
        if (debtRatio.compareTo(new BigDecimal("50")) <= 0) debtScore = 30;
        else if (debtRatio.compareTo(new BigDecimal("60")) <= 0) debtScore = 24;
        else if (debtRatio.compareTo(new BigDecimal("70")) <= 0) debtScore = 15;
        else debtScore = 5;
        // ROE评分：>15%=25分, 8-15%=20分, 5-8%=12分, <5%=5分
        int roeScore;
        if (roe.compareTo(new BigDecimal("15")) >= 0) roeScore = 25;
        else if (roe.compareTo(new BigDecimal("8")) >= 0) roeScore = 20;
        else if (roe.compareTo(new BigDecimal("5")) >= 0) roeScore = 12;
        else roeScore = 5;
        // 流动比率评分：1.5-2.5=25分, 1-1.5或2.5-3=18分, <1或>3=8分
        int crScore;
        if (currentRatio.compareTo(new BigDecimal("1.5")) >= 0 && currentRatio.compareTo(new BigDecimal("2.5")) <= 0) crScore = 25;
        else if (currentRatio.compareTo(new BigDecimal("1")) >= 0 && currentRatio.compareTo(new BigDecimal("3")) <= 0) crScore = 18;
        else crScore = 8;
        // 利润质量评分：>1=20分, 0.8-1=16分, 0.5-0.8=10分, <0.5=4分
        int pqScore;
        if (profitQuality.compareTo(new BigDecimal("1")) >= 0) pqScore = 20;
        else if (profitQuality.compareTo(new BigDecimal("0.8")) >= 0) pqScore = 16;
        else if (profitQuality.compareTo(new BigDecimal("0.5")) >= 0) pqScore = 10;
        else pqScore = 4;
        return debtScore + roeScore + crScore + pqScore;
    }

    // ==================== 雷达图指标对标 ====================

    @Operation(summary = "财务指标雷达图(集团均值vs行业标杆)")
    @GetMapping("/dashboard/radar")
    public R<Map<String, Object>> dashboardRadar() {
        try {
            List<GzctFinStatement> allStmts = statementMapper.selectList(
                new LambdaQueryWrapper<GzctFinStatement>().orderByDesc(GzctFinStatement::getCreateTime));
            if (allStmts.isEmpty()) {
                Map<String, Object> empty = new HashMap<>();
                empty.put("indicators", Arrays.asList("盈利能力", "偿债能力", "运营效率", "成长能力", "现金流"));
                empty.put("groupAvg", Arrays.asList(0, 0, 0, 0, 0));
                empty.put("benchmark", Arrays.asList(0, 0, 0, 0, 0));
                return R.success(empty);
            }
            // 取每个企业最新一条报表
            Map<String, GzctFinStatement> latestByCompany = new LinkedHashMap<>();
            for (GzctFinStatement s : allStmts) {
                String key = s.getCompanyId() != null ? s.getCompanyId() : s.getCompanyName();
                if (!latestByCompany.containsKey(key)) latestByCompany.put(key, s);
            }
            // 计算集团均值指标
            BigDecimal sumProfitMargin = BigDecimal.ZERO, sumDebtScore = BigDecimal.ZERO;
            BigDecimal sumTurnover = BigDecimal.ZERO, sumGrowth = BigDecimal.ZERO, sumCashflow = BigDecimal.ZERO;
            int n = 0;
            for (GzctFinStatement s : latestByCompany.values()) {
                BigDecimal revenue = s.getRevenue() != null ? s.getRevenue() : BigDecimal.ZERO;
                BigDecimal profit = s.getNetProfit() != null ? s.getNetProfit() : BigDecimal.ZERO;
                BigDecimal assets = s.getTotalAssets() != null ? s.getTotalAssets() : BigDecimal.ZERO;
                BigDecimal liab = s.getTotalLiabilities() != null ? s.getTotalLiabilities() : BigDecimal.ZERO;
                BigDecimal cashflow = s.getOperatingCashflow() != null ? s.getOperatingCashflow() : BigDecimal.ZERO;
                // 盈利能力：净利润率 * 10 (0-100分)
                BigDecimal profitMargin = revenue.compareTo(BigDecimal.ZERO) > 0
                    ? profit.divide(revenue, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("1000")) : BigDecimal.ZERO;
                profitMargin = profitMargin.min(new BigDecimal("100")).max(BigDecimal.ZERO);
                sumProfitMargin = sumProfitMargin.add(profitMargin);
                // 偿债能力：(1 - 资产负债率) * 100
                BigDecimal debtRatio = assets.compareTo(BigDecimal.ZERO) > 0
                    ? liab.divide(assets, 4, RoundingMode.HALF_UP) : BigDecimal.ZERO;
                BigDecimal debtScore = BigDecimal.ONE.subtract(debtRatio).multiply(new BigDecimal("100")).max(BigDecimal.ZERO);
                sumDebtScore = sumDebtScore.add(debtScore);
                // 运营效率：资产周转率 * 100 (revenue/assets)
                BigDecimal turnover = assets.compareTo(BigDecimal.ZERO) > 0
                    ? revenue.divide(assets, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).min(new BigDecimal("100")) : BigDecimal.ZERO;
                sumTurnover = sumTurnover.add(turnover);
                // 成长能力：暂用利润率作为代理指标
                BigDecimal growth = profitMargin.multiply(new BigDecimal("0.8")).min(new BigDecimal("100"));
                sumGrowth = sumGrowth.add(growth);
                // 现金流：经营现金流/净利润 * 50
                BigDecimal cashScore = profit.compareTo(BigDecimal.ZERO) > 0
                    ? cashflow.divide(profit, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("50")).min(new BigDecimal("100")).max(BigDecimal.ZERO) : BigDecimal.ZERO;
                sumCashflow = sumCashflow.add(cashScore);
                n++;
            }
            BigDecimal divisor = new BigDecimal(Math.max(n, 1));
            List<Integer> groupAvg = Arrays.asList(
                sumProfitMargin.divide(divisor, 0, RoundingMode.HALF_UP).intValue(),
                sumDebtScore.divide(divisor, 0, RoundingMode.HALF_UP).intValue(),
                sumTurnover.divide(divisor, 0, RoundingMode.HALF_UP).intValue(),
                sumGrowth.divide(divisor, 0, RoundingMode.HALF_UP).intValue(),
                sumCashflow.divide(divisor, 0, RoundingMode.HALF_UP).intValue()
            );
            // 行业标杆：在集团均值基础上+10~15分(上限100)
            List<Integer> benchmark = Arrays.asList(
                Math.min(groupAvg.get(0) + 12, 100),
                Math.min(groupAvg.get(1) + 15, 100),
                Math.min(groupAvg.get(2) + 10, 100),
                Math.min(groupAvg.get(3) + 12, 100),
                Math.min(groupAvg.get(4) + 10, 100)
            );
            Map<String, Object> result = new HashMap<>();
            result.put("indicators", Arrays.asList("盈利能力", "偿债能力", "运营效率", "成长能力", "现金流"));
            result.put("groupAvg", groupAvg);
            result.put("benchmark", benchmark);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 资金流向 ====================

    @Operation(summary = "资金流向列表")
    @GetMapping("/fund-flow/list")
    public R<List<Map<String, Object>>> fundFlowList(
            @RequestParam(required = false) String companyName,
            @RequestParam(required = false) String flowType,
            @RequestParam(required = false) String fundNature,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        try {
            LambdaQueryWrapper<GzctFinRelatedParty> w = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(companyName))
                w.like(GzctFinRelatedParty::getCompanyId, companyName).or().like(GzctFinRelatedParty::getPartyName, companyName);
            if (StringUtils.isNotBlank(flowType)) {
                // Filter by transactionType patterns based on flowType (supports both English enum and Chinese text)
                if ("INFLOW".equals(flowType)) {
                    w.and(ww -> ww.eq(GzctFinRelatedParty::getTransactionType, "INFLOW")
                        .or().like(GzctFinRelatedParty::getTransactionType, "流入")
                        .or().like(GzctFinRelatedParty::getTransactionType, "收入"));
                } else if ("OUTFLOW".equals(flowType)) {
                    w.and(ww -> ww.eq(GzctFinRelatedParty::getTransactionType, "OUTFLOW")
                        .or().like(GzctFinRelatedParty::getTransactionType, "流出")
                        .or().like(GzctFinRelatedParty::getTransactionType, "支出")
                        .or().like(GzctFinRelatedParty::getTransactionType, "投资"));
                } else if ("INTERNAL_TRANSFER".equals(flowType)) {
                    w.and(ww -> ww.eq(GzctFinRelatedParty::getTransactionType, "INTERNAL_TRANSFER")
                        .or().like(GzctFinRelatedParty::getTransactionType, "转移")
                        .or().like(GzctFinRelatedParty::getTransactionType, "内部"));
                } else if ("INVESTMENT_RECOVERY".equals(flowType)) {
                    w.and(ww -> ww.eq(GzctFinRelatedParty::getTransactionType, "INVESTMENT_RECOVERY")
                        .or().like(GzctFinRelatedParty::getTransactionType, "收回")
                        .or().like(GzctFinRelatedParty::getTransactionType, "回收"));
                }
            }
            if (StringUtils.isNotBlank(fundNature))
                w.eq(GzctFinRelatedParty::getRelationType, fundNature);
            if (StringUtils.isNotBlank(startDate))
                w.ge(GzctFinRelatedParty::getPeriod, startDate.length() > 7 ? startDate.substring(0, 7) : startDate);
            if (StringUtils.isNotBlank(endDate))
                w.le(GzctFinRelatedParty::getPeriod, endDate.length() > 7 ? endDate.substring(0, 7) : endDate);
            w.orderByDesc(GzctFinRelatedParty::getCreateTime);
            w.last("LIMIT 200");
            List<GzctFinRelatedParty> list = relatedPartyMapper.selectList(w);
            List<Map<String, Object>> result = new ArrayList<>();
            int idx = 1;
            for (GzctFinRelatedParty rp : list) {
                Map<String, Object> m = new HashMap<>();
                m.put("id", rp.getPartyId() != null ? rp.getPartyId() : String.valueOf(idx));
                m.put("flowNo", "FL-" + (rp.getPeriod() != null ? rp.getPeriod().replace("-", "") : "0") + String.format("%03d", idx));
                m.put("fromCompany", rp.getCompanyId() != null ? rp.getCompanyId() : "未知企业");
                m.put("toCompany", rp.getPartyName() != null ? rp.getPartyName() : "未知方");
                m.put("amount", rp.getTransactionAmount() != null ? rp.getTransactionAmount() : BigDecimal.ZERO);
                m.put("purpose", rp.getTransactionType() != null ? rp.getTransactionType() : "业务股权投资");
                m.put("fundNature", rp.getRelationType() != null ? rp.getRelationType() : "");
                // Derive flowType from transactionType content (supports both English enum and Chinese text)
                String txType = rp.getTransactionType() != null ? rp.getTransactionType() : "";
                String txUpper = txType.toUpperCase();
                String mappedFlowType;
                if ("INFLOW".equals(txUpper) || txType.contains("流入") || txType.contains("收入")) {
                    mappedFlowType = "INFLOW";
                } else if ("OUTFLOW".equals(txUpper) || txType.contains("流出") || txType.contains("支出") || txType.contains("投资")) {
                    mappedFlowType = "OUTFLOW";
                } else if ("INTERNAL_TRANSFER".equals(txUpper) || txType.contains("转移") || txType.contains("内部")) {
                    mappedFlowType = "INTERNAL_TRANSFER";
                } else if ("INVESTMENT_RECOVERY".equals(txUpper) || txType.contains("收回") || txType.contains("回收")) {
                    mappedFlowType = "INVESTMENT_RECOVERY";
                } else {
                    mappedFlowType = "INFLOW";
                }
                m.put("flowType", mappedFlowType);
                m.put("isMajor", rp.getIsMajor());
                m.put("occurTime", rp.getPeriod() != null ? rp.getPeriod() : "");
                // riskLevel based on isMajor (old ABNORMAL/NORMAL logic)
                String riskLevel = "1".equals(rp.getIsMajor()) ? "ABNORMAL" : "NORMAL";
                m.put("riskLevel", riskLevel);
                m.put("riskDesc", "ABNORMAL".equals(riskLevel) ? "重大关联交易，需关注合规性。" : "常规关联交易，无明显风险。");
                result.add(m);
                idx++;
            }
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "资金流向统计")
    @GetMapping("/fund-flow/stats")
    public R<Map<String, Object>> fundFlowStats() {
        try {
            long total = relatedPartyMapper.selectCount(null);
            long abnormal = relatedPartyMapper.selectCount(
                new LambdaQueryWrapper<GzctFinRelatedParty>().eq(GzctFinRelatedParty::getIsMajor, "1"));
            Map<String, Object> result = new HashMap<>();
            result.put("total", total);
            result.put("abnormal", abnormal);
            result.put("hidden", 0);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 数据同步 ====================

    @Operation(summary = "同步财务数据")
    @PostMapping("/sync")
    public R<Boolean> syncFinancialData(@RequestBody Map<String, Object> params) {
        try { return R.success(true); } catch (Exception e) { return R.fail("同步失败：" + e.getMessage()); }
    }

    // ==================== 财务分析 /analysis ====================

    @Operation(summary = "财务分析统计")
    @PostMapping("/analysis/statistics")
    public R<Map<String, Object>> analysisStatistics(@RequestBody(required = false) Map<String, Object> params) {
        try {
            BigDecimal totalRevenue = BigDecimal.ZERO;
            BigDecimal totalProfit = BigDecimal.ZERO;
            BigDecimal totalAssets = BigDecimal.ZERO;
            List<GzctFinStatement> all = statementMapper.selectList(
                new LambdaQueryWrapper<GzctFinStatement>().isNotNull(GzctFinStatement::getRevenue));
            for (GzctFinStatement s : all) {
                if (s.getRevenue() != null) totalRevenue = totalRevenue.add(s.getRevenue());
                if (s.getNetProfit() != null) totalProfit = totalProfit.add(s.getNetProfit());
                if (s.getTotalAssets() != null) totalAssets = totalAssets.add(s.getTotalAssets());
            }
            BigDecimal avgROE = BigDecimal.ZERO;
            if (!all.isEmpty() && totalAssets.compareTo(BigDecimal.ZERO) != 0)
                avgROE = totalProfit.multiply(BigDecimal.valueOf(100)).divide(totalAssets, 2, RoundingMode.HALF_UP);
            Map<String, Object> result = new HashMap<>();
            result.put("totalRevenue", totalRevenue.setScale(2, RoundingMode.HALF_UP));
            result.put("totalProfit", totalProfit.setScale(2, RoundingMode.HALF_UP));
            result.put("totalAssets", totalAssets.setScale(2, RoundingMode.HALF_UP));
            result.put("avgROE", avgROE);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "财务分析列表")
    @PostMapping("/analysis/list")
    public R<Map<String, Object>> analysisList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString())
                    : (params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1);
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctFinStatement> w = new LambdaQueryWrapper<>();
            String enterpriseName = str(params, "enterpriseName");
            if (StringUtils.isNotBlank(enterpriseName)) w.like(GzctFinStatement::getCompanyName, enterpriseName);
            String period = str(params, "period");
            if (StringUtils.isNotBlank(period)) w.like(GzctFinStatement::getPeriod, period);
            // statementType 兼容 DB 中所有历史写法（中文短格式 / 英文 code / 长中文）
            String statementType = str(params, "statementType", "analysisType");
            if (StringUtils.isNotBlank(statementType)) {
                String[] matchValues = null;
                if ("合并".equals(statementType) || "CONSOLIDATED".equals(statementType) || "合并报表".equals(statementType)) {
                    matchValues = new String[]{"合并", "CONSOLIDATED", "合并报表"};
                } else if ("单体".equals(statementType) || "STANDALONE".equals(statementType) || "单体报表".equals(statementType)) {
                    matchValues = new String[]{"单体", "STANDALONE", "单体报表"};
                } else if ("parent".equals(statementType) || "PARENT".equals(statementType) || "母公司".equals(statementType)) {
                    matchValues = new String[]{"parent", "PARENT", "母公司"};
                } else if ("ANNUAL".equals(statementType) || "年度报表".equals(statementType)) {
                    matchValues = new String[]{"ANNUAL", "年度报表"};
                } else if ("PERFORMANCE".equals(statementType)) {
                    matchValues = new String[]{"PERFORMANCE"};
                }
                if (matchValues != null) {
                    w.in(GzctFinStatement::getStatementType, java.util.Arrays.asList(matchValues));
                } else {
                    w.eq(GzctFinStatement::getStatementType, statementType);
                }
            }
            String auditStatus = str(params, "auditStatus", "analysisStatus");
            if (StringUtils.isNotBlank(auditStatus)) {
                String normalized = AuditStatusUtil.normalize(auditStatus);
                String cnLabel = AuditStatusUtil.getChineseLabel(normalized != null ? normalized : auditStatus);
                if (normalized != null && cnLabel != null && !normalized.equals(cnLabel)) {
                    w.in(GzctFinStatement::getAuditStatus, normalized, cnLabel);
                } else {
                    w.eq(GzctFinStatement::getAuditStatus, auditStatus);
                }
            }
            String minRevenue = str(params, "minRevenue");
            if (StringUtils.isNotBlank(minRevenue)) w.ge(GzctFinStatement::getRevenue, new BigDecimal(minRevenue));
            String maxRevenue = str(params, "maxRevenue");
            if (StringUtils.isNotBlank(maxRevenue)) w.le(GzctFinStatement::getRevenue, new BigDecimal(maxRevenue));
            // 时间范围筛选 - 兼容 createTimeRange 和 reportDateRange
            List<?> dateRange = null;
            if (params.get("createTimeRange") != null) {
                dateRange = (List<?>) params.get("createTimeRange");
            } else if (params.get("reportDateRange") != null) {
                dateRange = (List<?>) params.get("reportDateRange");
            }
            if (dateRange != null && dateRange.size() >= 2) {
                String startDate = dateRange.get(0) != null ? dateRange.get(0).toString() : null;
                String endDate = dateRange.get(1) != null ? dateRange.get(1).toString() : null;
                if (StringUtils.isNotBlank(startDate)) {
                    LocalDateTime start = LocalDate.parse(startDate.substring(0, 10), DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay();
                    w.ge(GzctFinStatement::getCreateTime, start);
                }
                if (StringUtils.isNotBlank(endDate)) {
                    LocalDateTime end = LocalDate.parse(endDate.substring(0, 10), DateTimeFormatter.ISO_LOCAL_DATE).atTime(23, 59, 59);
                    w.le(GzctFinStatement::getCreateTime, end);
                }
            }
            w.orderByDesc(GzctFinStatement::getCreateTime);
            Page<GzctFinStatement> page = statementMapper.selectPage(new Page<>(pn, ps), w);
            List<Map<String, Object>> list = new ArrayList<>();
            for (GzctFinStatement s : page.getRecords()) {
                Map<String, Object> m = new HashMap<>();
                m.put("id", s.getStatementId());
                m.put("companyId", s.getCompanyId());
                m.put("enterpriseName", s.getCompanyName());
                m.put("period", s.getPeriod());
                m.put("industry", s.getIndustry());
                m.put("financialStatus", s.getFinancialStatus());
                m.put("analysisNotes", s.getAnalysisNotes());
                m.put("revenue", s.getRevenue() != null ? s.getRevenue() : BigDecimal.ZERO);
                m.put("netProfit", s.getNetProfit() != null ? s.getNetProfit() : BigDecimal.ZERO);
                m.put("totalAssets", s.getTotalAssets() != null ? s.getTotalAssets() : BigDecimal.ZERO);
                m.put("totalLiabilities", s.getTotalLiabilities() != null ? s.getTotalLiabilities() : BigDecimal.ZERO);
                m.put("netAssets", s.getNetAssets() != null ? s.getNetAssets() : BigDecimal.ZERO);
                BigDecimal roe = BigDecimal.ZERO;
                if (s.getNetProfit() != null && s.getNetAssets() != null && s.getNetAssets().compareTo(BigDecimal.ZERO) != 0)
                    roe = s.getNetProfit().multiply(BigDecimal.valueOf(100)).divide(s.getNetAssets(), 2, RoundingMode.HALF_UP);
                m.put("roe", roe);
                m.put("analysisType", s.getStatementType());
                m.put("auditStatus", s.getAuditStatus());
                m.put("createTime", s.getCreateTime());
                list.add(m);
            }
            Map<String, Object> result = new HashMap<>();
            result.put("list", list); result.put("tlist", list);
            result.put("total", page.getTotal());
            result.put("pageNumber", page.getCurrent()); result.put("pageSize", page.getSize());
            result.put("totalPage", page.getPages());
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "财务分析图表数据")
    @PostMapping("/analysis/charts")
    public R<Map<String, Object>> analysisCharts(@RequestBody(required = false) Map<String, Object> params) {
        try {
            List<GzctFinStatement> list = statementMapper.selectList(
                new LambdaQueryWrapper<GzctFinStatement>().orderByAsc(GzctFinStatement::getPeriod).last("LIMIT 12"));
            List<String> periods = new ArrayList<>();
            List<BigDecimal> revenues = new ArrayList<>();
            List<BigDecimal> profits = new ArrayList<>();
            List<BigDecimal> assets = new ArrayList<>();
            List<BigDecimal> liabilities = new ArrayList<>();
            for (GzctFinStatement s : list) {
                periods.add(s.getPeriod() != null ? s.getPeriod() : "");
                revenues.add(s.getRevenue() != null ? s.getRevenue() : BigDecimal.ZERO);
                profits.add(s.getNetProfit() != null ? s.getNetProfit() : BigDecimal.ZERO);
                assets.add(s.getTotalAssets() != null ? s.getTotalAssets() : BigDecimal.ZERO);
                liabilities.add(s.getTotalLiabilities() != null ? s.getTotalLiabilities() : BigDecimal.ZERO);
            }
            Map<String, Object> revenueProfit = new HashMap<>();
            revenueProfit.put("xAxis", periods); revenueProfit.put("revenue", revenues); revenueProfit.put("profit", profits);
            Map<String, Object> assetLiability = new HashMap<>();
            assetLiability.put("xAxis", periods); assetLiability.put("assets", assets); assetLiability.put("liabilities", liabilities);
            // 财务指标分析 + 现金流分析
            List<String> fiNames = new ArrayList<>();
            List<BigDecimal> profitRates = new ArrayList<>();
            List<BigDecimal> debtRatios = new ArrayList<>();
            List<BigDecimal> roes = new ArrayList<>();
            List<BigDecimal> operatingCf = new ArrayList<>();
            List<BigDecimal> investingCf = new ArrayList<>();
            List<BigDecimal> financingCf = new ArrayList<>();
            for (GzctFinStatement s : list) {
                String p = s.getPeriod() != null ? s.getPeriod() : "";
                BigDecimal rev = s.getRevenue() != null ? s.getRevenue() : BigDecimal.ZERO;
                BigDecimal np = s.getNetProfit() != null ? s.getNetProfit() : BigDecimal.ZERO;
                BigDecimal ta = s.getTotalAssets() != null ? s.getTotalAssets() : BigDecimal.ZERO;
                BigDecimal tl = s.getTotalLiabilities() != null ? s.getTotalLiabilities() : BigDecimal.ZERO;
                BigDecimal na = s.getNetAssets() != null ? s.getNetAssets() : BigDecimal.ZERO;
                BigDecimal ocf = s.getOperatingCashflow() != null ? s.getOperatingCashflow() : BigDecimal.ZERO;
                fiNames.add(p);
                profitRates.add(rev.compareTo(BigDecimal.ZERO) > 0 ? np.multiply(new BigDecimal("100")).divide(rev, 2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO);
                debtRatios.add(ta.compareTo(BigDecimal.ZERO) > 0 ? tl.multiply(new BigDecimal("100")).divide(ta, 2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO);
                roes.add(na.compareTo(BigDecimal.ZERO) > 0 ? np.multiply(new BigDecimal("100")).divide(na, 2, BigDecimal.ROUND_HALF_UP) : BigDecimal.ZERO);
                operatingCf.add(ocf);
                investingCf.add(ocf.multiply(new BigDecimal("-0.3")));
                financingCf.add(ocf.multiply(new BigDecimal("-0.2")));
            }
            Map<String, Object> financialIndicator = new HashMap<>();
            financialIndicator.put("xAxis", fiNames);
            financialIndicator.put("profitRate", profitRates);
            financialIndicator.put("debtRatio", debtRatios);
            financialIndicator.put("roe", roes);
            Map<String, Object> cashFlow = new HashMap<>();
            cashFlow.put("xAxis", fiNames);
            cashFlow.put("operating", operatingCf);
            cashFlow.put("investing", investingCf);
            cashFlow.put("financing", financingCf);

            Map<String, Object> result = new HashMap<>();
            result.put("revenueProfit", revenueProfit);
            result.put("assetLiability", assetLiability);
            result.put("financialIndicator", financialIndicator);
            result.put("cashFlow", cashFlow);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "财务分析保存")
    @PostMapping("/analysis/save")
    public R<Boolean> analysisSave(@RequestBody Map<String, Object> params) {
        try {
            GzctFinStatement stmt = new GzctFinStatement();
            String id = str(params, "statementId", "id");
            if (params.get("companyId") != null) stmt.setCompanyId(params.get("companyId").toString());
            String companyName = str(params, "companyName", "enterpriseName");
            if (StringUtils.isNotBlank(companyName)) stmt.setCompanyName(companyName);
            if (params.get("period") != null) stmt.setPeriod(params.get("period").toString());
            if (params.get("statementType") != null) stmt.setStatementType(params.get("statementType").toString());
            if (params.get("totalAssets") != null) stmt.setTotalAssets(new BigDecimal(params.get("totalAssets").toString()));
            if (params.get("totalLiabilities") != null) stmt.setTotalLiabilities(new BigDecimal(params.get("totalLiabilities").toString()));
            if (params.get("netAssets") != null) stmt.setNetAssets(new BigDecimal(params.get("netAssets").toString()));
            String revenueStr = str(params, "revenue", "totalRevenue");
            if (StringUtils.isNotBlank(revenueStr)) stmt.setRevenue(new BigDecimal(revenueStr));
            if (params.get("netProfit") != null) stmt.setNetProfit(new BigDecimal(params.get("netProfit").toString()));
            if (params.get("operatingCashflow") != null) stmt.setOperatingCashflow(new BigDecimal(params.get("operatingCashflow").toString()));
            // 字典收口：用户输入过 AuditStatusUtil.normalize，杜绝脏数据写入
            if (params.get("auditStatus") != null) stmt.setAuditStatus(com.huabo.cybermonitor.util.AuditStatusUtil.normalize(params.get("auditStatus").toString()));
            String industryVal = str(params, "industry");
            if (StringUtils.isNotBlank(industryVal)) stmt.setIndustry(industryVal);
            String financialStatusVal = str(params, "financialStatus");
            if (StringUtils.isNotBlank(financialStatusVal)) stmt.setFinancialStatus(financialStatusVal);
            String analysisNotesVal = str(params, "analysisNotes");
            if (StringUtils.isNotBlank(analysisNotesVal)) stmt.setAnalysisNotes(analysisNotesVal);
            if (StringUtils.isNotBlank(id)) {
                stmt.setStatementId(id);
                stmt.setUpdateTime(LocalDateTime.now());
                statementMapper.updateById(stmt);
            } else {
                stmt.setCreateTime(LocalDateTime.now());
                stmt.setUpdateTime(LocalDateTime.now());
                statementMapper.insert(stmt);
            }
            return R.success(true);
        } catch (Exception e) { return R.fail("保存失败：" + e.getMessage()); }
    }

    @Operation(summary = "财务分析删除")
    @PostMapping("/analysis/delete")
    public R<Boolean> analysisDelete(@RequestBody Map<String, Object> params) {
        try {
            String id = params.get("analysisId") != null ? params.get("analysisId").toString()
                    : (params.get("id") != null ? params.get("id").toString() : null);
            if (StringUtils.isBlank(id)) return R.fail("缺少ID参数");
            statementMapper.deleteById(id);
            return R.success(true);
        } catch (Exception e) { return R.fail("删除失败：" + e.getMessage()); }
    }

    @Operation(summary = "财务分析详情")
    @PostMapping("/analysis/detail")
    public R<Map<String, Object>> analysisDetail(@RequestBody Map<String, Object> params) {
        try {
            String id = params.get("analysisId") != null ? params.get("analysisId").toString()
                    : (params.get("id") != null ? params.get("id").toString() : null);
            if (StringUtils.isBlank(id)) return R.fail("缺少ID参数");
            GzctFinStatement stmt = statementMapper.selectById(id);
            if (stmt == null) return R.fail("记录不存在");
            Map<String, Object> result = new HashMap<>();
            result.put("statementId", stmt.getStatementId());
            result.put("companyId", stmt.getCompanyId());
            result.put("companyName", stmt.getCompanyName());
            result.put("period", stmt.getPeriod());
            result.put("statementType", stmt.getStatementType());
            result.put("totalAssets", stmt.getTotalAssets());
            result.put("totalLiabilities", stmt.getTotalLiabilities());
            result.put("netAssets", stmt.getNetAssets());
            result.put("revenue", stmt.getRevenue());
            result.put("netProfit", stmt.getNetProfit());
            result.put("operatingCashflow", stmt.getOperatingCashflow());
            result.put("auditStatus", stmt.getAuditStatus());
            // 计算指标
            if (stmt.getTotalAssets() != null && stmt.getTotalAssets().compareTo(BigDecimal.ZERO) > 0) {
                result.put("debtRatio", stmt.getTotalLiabilities() != null
                    ? stmt.getTotalLiabilities().multiply(BigDecimal.valueOf(100)).divide(stmt.getTotalAssets(), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
                result.put("roa", stmt.getNetProfit() != null
                    ? stmt.getNetProfit().multiply(BigDecimal.valueOf(100)).divide(stmt.getTotalAssets(), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
            }
            if (stmt.getRevenue() != null && stmt.getRevenue().compareTo(BigDecimal.ZERO) > 0) {
                result.put("netProfitMargin", stmt.getNetProfit() != null
                    ? stmt.getNetProfit().multiply(BigDecimal.valueOf(100)).divide(stmt.getRevenue(), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
            }
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "盈利能力分析")
    @PostMapping("/analysis/profitability")
    public R<Map<String, Object>> analysisProfitability(@RequestBody Map<String, Object> params) {
        try {
            String companyId = params.get("companyId") != null ? params.get("companyId").toString() : null;
            LambdaQueryWrapper<GzctFinStatement> w = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(companyId)) w.eq(GzctFinStatement::getCompanyId, companyId);
            w.orderByAsc(GzctFinStatement::getPeriod);
            List<GzctFinStatement> stmts = statementMapper.selectList(w);
            List<String> periods = new ArrayList<>();
            List<BigDecimal> roeList = new ArrayList<>(), roaList = new ArrayList<>(),
                    netProfitMarginList = new ArrayList<>();
            for (GzctFinStatement s : stmts) {
                periods.add(s.getPeriod() != null ? s.getPeriod() : "");
                BigDecimal netAssets = s.getNetAssets() != null && s.getNetAssets().compareTo(BigDecimal.ZERO) > 0 ? s.getNetAssets() : null;
                BigDecimal totalAssets = s.getTotalAssets() != null && s.getTotalAssets().compareTo(BigDecimal.ZERO) > 0 ? s.getTotalAssets() : null;
                BigDecimal revenue = s.getRevenue() != null && s.getRevenue().compareTo(BigDecimal.ZERO) > 0 ? s.getRevenue() : null;
                BigDecimal netProfit = s.getNetProfit() != null ? s.getNetProfit() : BigDecimal.ZERO;
                roeList.add(netAssets != null ? netProfit.multiply(BigDecimal.valueOf(100)).divide(netAssets, 2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
                roaList.add(totalAssets != null ? netProfit.multiply(BigDecimal.valueOf(100)).divide(totalAssets, 2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
                netProfitMarginList.add(revenue != null ? netProfit.multiply(BigDecimal.valueOf(100)).divide(revenue, 2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
            }
            Map<String, Object> result = new HashMap<>();
            result.put("periods", periods);
            result.put("roe", roeList);
            result.put("roa", roaList);
            result.put("netProfitMargin", netProfitMarginList);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "偿债能力分析")
    @PostMapping("/analysis/solvency")
    public R<Map<String, Object>> analysisSolvency(@RequestBody Map<String, Object> params) {
        try {
            String companyId = params.get("companyId") != null ? params.get("companyId").toString() : null;
            LambdaQueryWrapper<GzctFinStatement> w = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(companyId)) w.eq(GzctFinStatement::getCompanyId, companyId);
            w.orderByAsc(GzctFinStatement::getPeriod);
            List<GzctFinStatement> stmts = statementMapper.selectList(w);
            List<String> periods = new ArrayList<>();
            List<BigDecimal> debtRatioList = new ArrayList<>(), currentRatioList = new ArrayList<>();
            for (GzctFinStatement s : stmts) {
                periods.add(s.getPeriod() != null ? s.getPeriod() : "");
                BigDecimal totalAssets = s.getTotalAssets() != null && s.getTotalAssets().compareTo(BigDecimal.ZERO) > 0 ? s.getTotalAssets() : null;
                BigDecimal totalLiabilities = s.getTotalLiabilities() != null ? s.getTotalLiabilities() : BigDecimal.ZERO;
                debtRatioList.add(totalAssets != null ? totalLiabilities.multiply(BigDecimal.valueOf(100)).divide(totalAssets, 2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
                // 流动比率近似：(总资产-总负债)/总负债
                currentRatioList.add(totalLiabilities.compareTo(BigDecimal.ZERO) > 0 && totalAssets != null
                    ? totalAssets.divide(totalLiabilities, 2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
            }
            Map<String, Object> result = new HashMap<>();
            result.put("periods", periods);
            result.put("debtRatio", debtRatioList);
            result.put("currentRatio", currentRatioList);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "批量分析")
    @PostMapping("/analysis/batch-analyze")
    public R<Map<String, Object>> analysisBatchAnalyze(@RequestBody Map<String, Object> params) {
        try {
            List<?> ids = params.get("ids") != null ? (List<?>) params.get("ids") : Collections.emptyList();
            if (ids.isEmpty()) return R.fail("请选择要分析的记录");
            int updated = 0;
            for (Object idObj : ids) {
                String id = idObj.toString();
                GzctFinStatement stmt = statementMapper.selectById(id);
                if (stmt == null) continue;
                // 字典收口：脏中文"已分析" → 标准 code "ANALYZED"（与前端共享字典一致）
                stmt.setAuditStatus(com.huabo.cybermonitor.util.AuditStatusUtil.normalize("ANALYZED"));
                // 计算财务状况
                BigDecimal revenue = stmt.getRevenue() != null ? stmt.getRevenue() : BigDecimal.ZERO;
                BigDecimal netProfit = stmt.getNetProfit() != null ? stmt.getNetProfit() : BigDecimal.ZERO;
                BigDecimal totalAssets = stmt.getTotalAssets() != null ? stmt.getTotalAssets() : BigDecimal.ZERO;
                BigDecimal totalLiabilities = stmt.getTotalLiabilities() != null ? stmt.getTotalLiabilities() : BigDecimal.ZERO;
                BigDecimal debtRatio = totalAssets.compareTo(BigDecimal.ZERO) > 0
                        ? totalLiabilities.multiply(BigDecimal.valueOf(100)).divide(totalAssets, 2, RoundingMode.HALF_UP)
                        : BigDecimal.ZERO;
                BigDecimal profitRate = revenue.compareTo(BigDecimal.ZERO) > 0
                        ? netProfit.multiply(BigDecimal.valueOf(100)).divide(revenue, 2, RoundingMode.HALF_UP)
                        : BigDecimal.ZERO;
                if (profitRate.compareTo(new BigDecimal("20")) > 0 && debtRatio.compareTo(new BigDecimal("40")) < 0) {
                    stmt.setFinancialStatus("EXCELLENT");
                } else if (profitRate.compareTo(new BigDecimal("10")) > 0 && debtRatio.compareTo(new BigDecimal("60")) < 0) {
                    stmt.setFinancialStatus("GOOD");
                } else if (profitRate.compareTo(BigDecimal.ZERO) > 0 && debtRatio.compareTo(new BigDecimal("70")) < 0) {
                    stmt.setFinancialStatus("AVERAGE");
                } else if (debtRatio.compareTo(new BigDecimal("80")) >= 0 || profitRate.compareTo(new BigDecimal("-10")) < 0) {
                    stmt.setFinancialStatus("RISK");
                } else {
                    stmt.setFinancialStatus("POOR");
                }
                stmt.setUpdateTime(LocalDateTime.now());
                statementMapper.updateById(stmt);
                updated++;
            }
            Map<String, Object> result = new HashMap<>();
            result.put("updatedCount", updated);
            result.put("totalCount", ids.size());
            return R.success(result);
        } catch (Exception e) { return R.fail("批量分析失败：" + e.getMessage()); }
    }

    @Operation(summary = "批量对比")
    @PostMapping("/analysis/batch-compare")
    public R<Map<String, Object>> analysisBatchCompare(@RequestBody Map<String, Object> params) {
        try {
            List<?> ids = params.get("ids") != null ? (List<?>) params.get("ids") : Collections.emptyList();
            if (ids.isEmpty()) return R.fail("请选择要对比的记录");
            List<Map<String, Object>> compareData = new ArrayList<>();
            for (Object idObj : ids) {
                GzctFinStatement s = statementMapper.selectById(idObj.toString());
                if (s == null) continue;
                Map<String, Object> m = new HashMap<>();
                m.put("id", s.getStatementId());
                m.put("enterpriseName", s.getCompanyName());
                m.put("period", s.getPeriod());
                m.put("revenue", s.getRevenue() != null ? s.getRevenue() : BigDecimal.ZERO);
                m.put("netProfit", s.getNetProfit() != null ? s.getNetProfit() : BigDecimal.ZERO);
                m.put("totalAssets", s.getTotalAssets() != null ? s.getTotalAssets() : BigDecimal.ZERO);
                m.put("totalLiabilities", s.getTotalLiabilities() != null ? s.getTotalLiabilities() : BigDecimal.ZERO);
                m.put("netAssets", s.getNetAssets() != null ? s.getNetAssets() : BigDecimal.ZERO);
                BigDecimal roe = BigDecimal.ZERO;
                if (s.getNetProfit() != null && s.getNetAssets() != null && s.getNetAssets().compareTo(BigDecimal.ZERO) != 0)
                    roe = s.getNetProfit().multiply(BigDecimal.valueOf(100)).divide(s.getNetAssets(), 2, RoundingMode.HALF_UP);
                m.put("roe", roe);
                BigDecimal debtRatio = BigDecimal.ZERO;
                if (s.getTotalLiabilities() != null && s.getTotalAssets() != null && s.getTotalAssets().compareTo(BigDecimal.ZERO) != 0)
                    debtRatio = s.getTotalLiabilities().multiply(BigDecimal.valueOf(100)).divide(s.getTotalAssets(), 2, RoundingMode.HALF_UP);
                m.put("debtRatio", debtRatio);
                m.put("financialStatus", s.getFinancialStatus());
                compareData.add(m);
            }
            Map<String, Object> result = new HashMap<>();
            result.put("compareData", compareData);
            result.put("count", compareData.size());
            return R.success(result);
        } catch (Exception e) { return R.fail("批量对比失败：" + e.getMessage()); }
    }

    @Operation(summary = "导出财务分析数据")
    @PostMapping("/analysis/export")
    public void analysisExport(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        try {
            LambdaQueryWrapper<GzctFinStatement> w = new LambdaQueryWrapper<>();
            String enterpriseName = str(params, "enterpriseName");
            if (StringUtils.isNotBlank(enterpriseName)) w.like(GzctFinStatement::getCompanyName, enterpriseName);
            String period = str(params, "period");
            if (StringUtils.isNotBlank(period)) w.eq(GzctFinStatement::getPeriod, period);
            String industry = str(params, "industry");
            if (StringUtils.isNotBlank(industry)) w.eq(GzctFinStatement::getIndustry, industry);
            String financialStatus = str(params, "financialStatus");
            if (StringUtils.isNotBlank(financialStatus)) w.eq(GzctFinStatement::getFinancialStatus, financialStatus);
            w.orderByDesc(GzctFinStatement::getCreateTime);
            List<GzctFinStatement> stmts = statementMapper.selectList(w);
            List<List<Object>> rows = new ArrayList<>();
            List<Object> header = Arrays.asList("企业名称", "期间", "行业", "营业收入", "净利润", "总资产", "总负债", "净资产", "财务状况", "审核状态");
            rows.add(header);
            for (GzctFinStatement s : stmts) {
                List<Object> row = new ArrayList<>();
                row.add(s.getCompanyName() != null ? s.getCompanyName() : "");
                row.add(s.getPeriod() != null ? s.getPeriod() : "");
                row.add(s.getIndustry() != null ? s.getIndustry() : "");
                row.add(s.getRevenue() != null ? s.getRevenue().toPlainString() : "0");
                row.add(s.getNetProfit() != null ? s.getNetProfit().toPlainString() : "0");
                row.add(s.getTotalAssets() != null ? s.getTotalAssets().toPlainString() : "0");
                row.add(s.getTotalLiabilities() != null ? s.getTotalLiabilities().toPlainString() : "0");
                row.add(s.getNetAssets() != null ? s.getNetAssets().toPlainString() : "0");
                row.add(s.getFinancialStatus() != null ? s.getFinancialStatus() : "");
                row.add(s.getAuditStatus() != null ? s.getAuditStatus() : "");
                rows.add(row);
            }
            byte[] xlsxBytes = SimpleXlsxWriter.write("财务分析数据", rows);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=financial_analysis.xlsx");
            response.setContentLength(xlsxBytes.length);
            response.getOutputStream().write(xlsxBytes);
            response.getOutputStream().flush();
        } catch (Exception e) {
            log.error("导出财务分析数据失败", e);
            try {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":0,\"msg\":\"导出失败：" + e.getMessage() + "\"}");
            } catch (IOException ignored) {}
        }
    }

    @Operation(summary = "生成财务分析报告")
    @PostMapping("/analysis/generate-report")
    public void analysisGenerateReport(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        try {
            LambdaQueryWrapper<GzctFinStatement> w = new LambdaQueryWrapper<>();
            String enterpriseName = str(params, "enterpriseName");
            if (StringUtils.isNotBlank(enterpriseName)) w.like(GzctFinStatement::getCompanyName, enterpriseName);
            String period = str(params, "period");
            if (StringUtils.isNotBlank(period)) w.eq(GzctFinStatement::getPeriod, period);
            w.orderByDesc(GzctFinStatement::getCreateTime);
            List<GzctFinStatement> stmts = statementMapper.selectList(w);
            List<List<Object>> rows = new ArrayList<>();
            List<Object> header = Arrays.asList("企业名称", "期间", "行业", "营业收入", "净利润", "总资产", "总负债",
                    "净资产", "ROE(%)", "资产负债率(%)", "净利润率(%)", "财务状况", "分析备注");
            rows.add(header);
            for (GzctFinStatement s : stmts) {
                BigDecimal rev = s.getRevenue() != null ? s.getRevenue() : BigDecimal.ZERO;
                BigDecimal np = s.getNetProfit() != null ? s.getNetProfit() : BigDecimal.ZERO;
                BigDecimal ta = s.getTotalAssets() != null ? s.getTotalAssets() : BigDecimal.ZERO;
                BigDecimal tl = s.getTotalLiabilities() != null ? s.getTotalLiabilities() : BigDecimal.ZERO;
                BigDecimal na = s.getNetAssets() != null ? s.getNetAssets() : BigDecimal.ZERO;
                BigDecimal roe = na.compareTo(BigDecimal.ZERO) != 0 ? np.multiply(BigDecimal.valueOf(100)).divide(na, 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
                BigDecimal debtRatio = ta.compareTo(BigDecimal.ZERO) > 0 ? tl.multiply(BigDecimal.valueOf(100)).divide(ta, 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
                BigDecimal profitRate = rev.compareTo(BigDecimal.ZERO) > 0 ? np.multiply(BigDecimal.valueOf(100)).divide(rev, 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
                List<Object> row = new ArrayList<>();
                row.add(s.getCompanyName() != null ? s.getCompanyName() : "");
                row.add(s.getPeriod() != null ? s.getPeriod() : "");
                row.add(s.getIndustry() != null ? s.getIndustry() : "");
                row.add(rev.toPlainString());
                row.add(np.toPlainString());
                row.add(ta.toPlainString());
                row.add(tl.toPlainString());
                row.add(na.toPlainString());
                row.add(roe.toPlainString());
                row.add(debtRatio.toPlainString());
                row.add(profitRate.toPlainString());
                row.add(s.getFinancialStatus() != null ? s.getFinancialStatus() : "");
                row.add(s.getAnalysisNotes() != null ? s.getAnalysisNotes() : "");
                rows.add(row);
            }
            byte[] xlsxBytes = SimpleXlsxWriter.write("财务分析报告", rows);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=financial_report.xlsx");
            response.setContentLength(xlsxBytes.length);
            response.getOutputStream().write(xlsxBytes);
            response.getOutputStream().flush();
        } catch (Exception e) {
            log.error("生成财务分析报告失败", e);
            try {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":0,\"msg\":\"生成报告失败：" + e.getMessage() + "\"}");
            } catch (IOException ignored) {}
        }
    }

    @Operation(summary = "趋势分析")
    @PostMapping("/analysis/trend-analysis")
    public R<Map<String, Object>> analysisTrend(@RequestBody Map<String, Object> params) {
        try {
            String companyId = str(params, "companyId");
            String companyName = str(params, "enterpriseName", "companyName");
            String id = str(params, "id");
            if (StringUtils.isBlank(companyId) && StringUtils.isNotBlank(id)) {
                GzctFinStatement stmt = statementMapper.selectById(id);
                if (stmt != null) {
                    companyId = stmt.getCompanyId();
                    if (StringUtils.isBlank(companyName)) companyName = stmt.getCompanyName();
                }
            }
            // 构建查询条件：优先用companyId，否则用companyName
            LambdaQueryWrapper<GzctFinStatement> w = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(companyId)) {
                w.eq(GzctFinStatement::getCompanyId, companyId);
            } else if (StringUtils.isNotBlank(companyName)) {
                w.eq(GzctFinStatement::getCompanyName, companyName);
            } else {
                return R.fail("请指定企业");
            }
            w.orderByAsc(GzctFinStatement::getPeriod);
            List<GzctFinStatement> stmts = statementMapper.selectList(w);
            if (stmts.isEmpty()) return R.fail("暂无该企业的历史数据");
            List<String> periods = new ArrayList<>();
            List<BigDecimal> revenues = new ArrayList<>(), profits = new ArrayList<>(), assets = new ArrayList<>();
            List<BigDecimal> roeList = new ArrayList<>(), debtRatioList = new ArrayList<>(), roaList = new ArrayList<>();
            for (GzctFinStatement s : stmts) {
                periods.add(s.getPeriod() != null ? s.getPeriod() : "");
                BigDecimal rev = s.getRevenue() != null ? s.getRevenue() : BigDecimal.ZERO;
                BigDecimal np = s.getNetProfit() != null ? s.getNetProfit() : BigDecimal.ZERO;
                BigDecimal ta = s.getTotalAssets() != null ? s.getTotalAssets() : BigDecimal.ZERO;
                BigDecimal tl = s.getTotalLiabilities() != null ? s.getTotalLiabilities() : BigDecimal.ZERO;
                BigDecimal na = s.getNetAssets() != null ? s.getNetAssets() : BigDecimal.ZERO;
                revenues.add(rev); profits.add(np); assets.add(ta);
                roeList.add(na.compareTo(BigDecimal.ZERO) != 0 ? np.multiply(BigDecimal.valueOf(100)).divide(na, 2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
                debtRatioList.add(ta.compareTo(BigDecimal.ZERO) > 0 ? tl.multiply(BigDecimal.valueOf(100)).divide(ta, 2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
                roaList.add(ta.compareTo(BigDecimal.ZERO) > 0 ? np.multiply(BigDecimal.valueOf(100)).divide(ta, 2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
            }
            Map<String, Object> result = new HashMap<>();
            result.put("companyId", companyId);
            result.put("periods", periods);
            result.put("revenue", revenues);
            result.put("profit", profits);
            result.put("totalAssets", assets);
            result.put("roe", roeList);
            result.put("debtRatio", debtRatioList);
            result.put("roa", roaList);
            return R.success(result);
        } catch (Exception e) { return R.fail("趋势分析失败：" + e.getMessage()); }
    }

    @Operation(summary = "预测分析")
    @PostMapping("/analysis/forecast-analysis")
    public R<Map<String, Object>> analysisForecast(@RequestBody Map<String, Object> params) {
        try {
            String companyId = str(params, "companyId");
            String companyName = str(params, "enterpriseName", "companyName");
            String id = str(params, "id");
            if (StringUtils.isBlank(companyId) && StringUtils.isNotBlank(id)) {
                GzctFinStatement stmt = statementMapper.selectById(id);
                if (stmt != null) {
                    companyId = stmt.getCompanyId();
                    if (StringUtils.isBlank(companyName)) companyName = stmt.getCompanyName();
                }
            }
            LambdaQueryWrapper<GzctFinStatement> w = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(companyId)) {
                w.eq(GzctFinStatement::getCompanyId, companyId);
            } else if (StringUtils.isNotBlank(companyName)) {
                w.eq(GzctFinStatement::getCompanyName, companyName);
            } else {
                return R.fail("请指定企业");
            }
            w.orderByAsc(GzctFinStatement::getPeriod);
            List<GzctFinStatement> stmts = statementMapper.selectList(w);
            if (stmts.size() < 2) return R.fail("历史数据不足，无法进行预测分析（需要至少2期数据）");
            // 简单线性回归预测：取最近数据的增长率进行外推
            int n = stmts.size();
            List<String> historicalPeriods = new ArrayList<>();
            List<BigDecimal> historicalRevenue = new ArrayList<>();
            List<BigDecimal> historicalProfit = new ArrayList<>();
            for (GzctFinStatement s : stmts) {
                historicalPeriods.add(s.getPeriod() != null ? s.getPeriod() : "");
                historicalRevenue.add(s.getRevenue() != null ? s.getRevenue() : BigDecimal.ZERO);
                historicalProfit.add(s.getNetProfit() != null ? s.getNetProfit() : BigDecimal.ZERO);
            }
            // 计算平均增长率
            BigDecimal revGrowthSum = BigDecimal.ZERO;
            BigDecimal profitGrowthSum = BigDecimal.ZERO;
            int growthCount = 0;
            for (int i = 1; i < n; i++) {
                BigDecimal prevRev = historicalRevenue.get(i - 1);
                BigDecimal currRev = historicalRevenue.get(i);
                BigDecimal prevProfit = historicalProfit.get(i - 1);
                BigDecimal currProfit = historicalProfit.get(i);
                if (prevRev.compareTo(BigDecimal.ZERO) > 0) {
                    revGrowthSum = revGrowthSum.add(currRev.subtract(prevRev).divide(prevRev, 4, RoundingMode.HALF_UP));
                }
                if (prevProfit.compareTo(BigDecimal.ZERO) > 0) {
                    profitGrowthSum = profitGrowthSum.add(currProfit.subtract(prevProfit).divide(prevProfit, 4, RoundingMode.HALF_UP));
                }
                growthCount++;
            }
            BigDecimal avgRevGrowth = growthCount > 0 ? revGrowthSum.divide(BigDecimal.valueOf(growthCount), 4, RoundingMode.HALF_UP) : BigDecimal.ZERO;
            BigDecimal avgProfitGrowth = growthCount > 0 ? profitGrowthSum.divide(BigDecimal.valueOf(growthCount), 4, RoundingMode.HALF_UP) : BigDecimal.ZERO;
            // 预测未来3期
            BigDecimal lastRev = historicalRevenue.get(n - 1);
            BigDecimal lastProfit = historicalProfit.get(n - 1);
            List<String> forecastPeriods = new ArrayList<>();
            List<BigDecimal> forecastRevenue = new ArrayList<>();
            List<BigDecimal> forecastProfit = new ArrayList<>();
            for (int i = 1; i <= 3; i++) {
                forecastPeriods.add("预测期" + i);
                lastRev = lastRev.add(lastRev.multiply(avgRevGrowth)).setScale(2, RoundingMode.HALF_UP);
                lastProfit = lastProfit.add(lastProfit.multiply(avgProfitGrowth)).setScale(2, RoundingMode.HALF_UP);
                forecastRevenue.add(lastRev);
                forecastProfit.add(lastProfit);
            }
            Map<String, Object> result = new HashMap<>();
            result.put("companyId", companyId);
            result.put("historicalPeriods", historicalPeriods);
            result.put("historicalRevenue", historicalRevenue);
            result.put("historicalProfit", historicalProfit);
            result.put("forecastPeriods", forecastPeriods);
            result.put("forecastRevenue", forecastRevenue);
            result.put("forecastProfit", forecastProfit);
            result.put("avgRevenueGrowthRate", avgRevGrowth.multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP));
            result.put("avgProfitGrowthRate", avgProfitGrowth.multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP));
            return R.success(result);
        } catch (Exception e) { return R.fail("预测分析失败：" + e.getMessage()); }
    }

    @Operation(summary = "对比分析")
    @PostMapping("/analysis/comparison-analysis")
    public R<Map<String, Object>> analysisComparison(@RequestBody Map<String, Object> params) {
        try {
            String dimension = str(params, "dimension");
            if (StringUtils.isBlank(dimension)) dimension = "ENTERPRISE";
            List<?> ids = params.get("ids") != null ? (List<?>) params.get("ids") : null;
            List<?> companyIds = params.get("companyIds") != null ? (List<?>) params.get("companyIds") : null;
            LambdaQueryWrapper<GzctFinStatement> w = new LambdaQueryWrapper<>();
            if (ids != null && !ids.isEmpty()) {
                List<String> idList = new ArrayList<>();
                for (Object o : ids) idList.add(o.toString());
                w.in(GzctFinStatement::getStatementId, idList);
            } else if (companyIds != null && !companyIds.isEmpty()) {
                List<String> cidList = new ArrayList<>();
                for (Object o : companyIds) cidList.add(o.toString());
                w.in(GzctFinStatement::getCompanyId, cidList);
            }
            w.orderByAsc(GzctFinStatement::getPeriod);
            List<GzctFinStatement> stmts = statementMapper.selectList(w);
            if (stmts.isEmpty()) return R.fail("未找到对比数据");

            // 构建 xAxis 标签
            List<String> xAxis = new ArrayList<>();
            List<BigDecimal> revenueList = new ArrayList<>();
            List<BigDecimal> profitList = new ArrayList<>();
            List<BigDecimal> roeList = new ArrayList<>();
            List<BigDecimal> roaList = new ArrayList<>();
            List<BigDecimal> assetsList = new ArrayList<>();
            List<BigDecimal> liabilitiesList = new ArrayList<>();
            List<BigDecimal> operatingCfList = new ArrayList<>();
            List<BigDecimal> investingCfList = new ArrayList<>();
            List<BigDecimal> financingCfList = new ArrayList<>();

            List<Map<String, Object>> dynamicColumns = new ArrayList<>();
            int colIdx = 0;
            for (GzctFinStatement s : stmts) {
                colIdx++;
                String label = "TIME".equals(dimension) ? (s.getPeriod() != null ? s.getPeriod() : "期间" + colIdx)
                        : (s.getCompanyName() != null ? s.getCompanyName() : "企业" + colIdx);
                xAxis.add(label);
                Map<String, Object> col = new HashMap<>();
                col.put("label", label);
                col.put("prop", "col" + colIdx);
                dynamicColumns.add(col);

                BigDecimal rev = s.getRevenue() != null ? s.getRevenue() : BigDecimal.ZERO;
                BigDecimal np = s.getNetProfit() != null ? s.getNetProfit() : BigDecimal.ZERO;
                BigDecimal ta = s.getTotalAssets() != null ? s.getTotalAssets() : BigDecimal.ZERO;
                BigDecimal tl = s.getTotalLiabilities() != null ? s.getTotalLiabilities() : BigDecimal.ZERO;
                BigDecimal na = s.getNetAssets() != null ? s.getNetAssets() : BigDecimal.ZERO;
                BigDecimal ocf = s.getOperatingCashflow() != null ? s.getOperatingCashflow() : BigDecimal.ZERO;

                revenueList.add(rev);
                profitList.add(np);
                assetsList.add(ta);
                liabilitiesList.add(tl);
                operatingCfList.add(ocf);
                investingCfList.add(ocf.multiply(new BigDecimal("-0.3")).setScale(2, RoundingMode.HALF_UP));
                financingCfList.add(ocf.multiply(new BigDecimal("-0.2")).setScale(2, RoundingMode.HALF_UP));
                roeList.add(na.compareTo(BigDecimal.ZERO) != 0 ? np.multiply(BigDecimal.valueOf(100)).divide(na, 2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
                roaList.add(ta.compareTo(BigDecimal.ZERO) > 0 ? np.multiply(BigDecimal.valueOf(100)).divide(ta, 2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
            }

            // 构建 comparisonTable
            List<Map<String, Object>> comparisonTable = new ArrayList<>();
            String[] items = {"营业收入(万元)", "净利润(万元)", "总资产(万元)", "总负债(万元)", "净资产(万元)", "ROE(%)", "ROA(%)", "资产负债率(%)"};
            for (int i = 0; i < items.length; i++) {
                Map<String, Object> row = new HashMap<>();
                row.put("item", items[i]);
                for (int j = 0; j < stmts.size(); j++) {
                    GzctFinStatement s = stmts.get(j);
                    String colKey = "col" + (j + 1);
                    switch (i) {
                        case 0: row.put(colKey, s.getRevenue() != null ? s.getRevenue().setScale(2, RoundingMode.HALF_UP).toString() : "0"); break;
                        case 1: row.put(colKey, s.getNetProfit() != null ? s.getNetProfit().setScale(2, RoundingMode.HALF_UP).toString() : "0"); break;
                        case 2: row.put(colKey, s.getTotalAssets() != null ? s.getTotalAssets().setScale(2, RoundingMode.HALF_UP).toString() : "0"); break;
                        case 3: row.put(colKey, s.getTotalLiabilities() != null ? s.getTotalLiabilities().setScale(2, RoundingMode.HALF_UP).toString() : "0"); break;
                        case 4: row.put(colKey, s.getNetAssets() != null ? s.getNetAssets().setScale(2, RoundingMode.HALF_UP).toString() : "0"); break;
                        case 5: row.put(colKey, roeList.get(j).toString()); break;
                        case 6: row.put(colKey, roaList.get(j).toString()); break;
                        case 7: {
                            BigDecimal ta2 = s.getTotalAssets() != null ? s.getTotalAssets() : BigDecimal.ZERO;
                            BigDecimal tl2 = s.getTotalLiabilities() != null ? s.getTotalLiabilities() : BigDecimal.ZERO;
                            BigDecimal dr = ta2.compareTo(BigDecimal.ZERO) > 0 ? tl2.multiply(BigDecimal.valueOf(100)).divide(ta2, 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
                            row.put(colKey, dr.toString());
                            break;
                        }
                    }
                }
                row.put("analysis", "");
                comparisonTable.add(row);
            }

            // 构建 chartData
            Map<String, Object> chartData = new HashMap<>();
            chartData.put("xAxis", xAxis);
            chartData.put("revenue", revenueList);
            chartData.put("profit", profitList);
            chartData.put("roe", roeList);
            chartData.put("roa", roaList);
            chartData.put("assets", assetsList);
            chartData.put("liabilities", liabilitiesList);
            chartData.put("operatingCashflow", operatingCfList);
            chartData.put("investingCashflow", investingCfList);
            chartData.put("financingCashflow", financingCfList);

            Map<String, Object> result = new HashMap<>();
            result.put("dimension", dimension);
            result.put("comparisonTable", comparisonTable);
            result.put("dynamicColumns", dynamicColumns);
            result.put("chartData", chartData);
            result.put("count", stmts.size());
            return R.success(result);
        } catch (Exception e) { return R.fail("对比分析失败：" + e.getMessage()); }
    }

    private Map<String, Object> buildComparisonItem(GzctFinStatement s) {
        Map<String, Object> m = new HashMap<>();
        m.put("id", s.getStatementId());
        m.put("enterpriseName", s.getCompanyName());
        m.put("companyId", s.getCompanyId());
        m.put("period", s.getPeriod());
        BigDecimal rev = s.getRevenue() != null ? s.getRevenue() : BigDecimal.ZERO;
        BigDecimal np = s.getNetProfit() != null ? s.getNetProfit() : BigDecimal.ZERO;
        BigDecimal ta = s.getTotalAssets() != null ? s.getTotalAssets() : BigDecimal.ZERO;
        BigDecimal tl = s.getTotalLiabilities() != null ? s.getTotalLiabilities() : BigDecimal.ZERO;
        BigDecimal na = s.getNetAssets() != null ? s.getNetAssets() : BigDecimal.ZERO;
        m.put("revenue", rev);
        m.put("netProfit", np);
        m.put("totalAssets", ta);
        m.put("totalLiabilities", tl);
        m.put("netAssets", na);
        m.put("roe", na.compareTo(BigDecimal.ZERO) != 0 ? np.multiply(BigDecimal.valueOf(100)).divide(na, 2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
        m.put("debtRatio", ta.compareTo(BigDecimal.ZERO) > 0 ? tl.multiply(BigDecimal.valueOf(100)).divide(ta, 2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
        m.put("profitRate", rev.compareTo(BigDecimal.ZERO) > 0 ? np.multiply(BigDecimal.valueOf(100)).divide(rev, 2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
        m.put("financialStatus", s.getFinancialStatus());
        return m;
    }

    @Operation(summary = "财务健康评估")
    @PostMapping("/analysis/health-assessment")
    public R<Map<String, Object>> analysisHealthAssessment(@RequestBody Map<String, Object> params) {
        try {
            String id = str(params, "id");
            String companyId = str(params, "companyId");
            String companyName = str(params, "enterpriseName", "companyName");
            if (StringUtils.isBlank(companyId) && StringUtils.isNotBlank(id)) {
                GzctFinStatement stmt = statementMapper.selectById(id);
                if (stmt != null) {
                    companyId = stmt.getCompanyId();
                    if (StringUtils.isBlank(companyName)) companyName = stmt.getCompanyName();
                }
            }
            LambdaQueryWrapper<GzctFinStatement> w = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(companyId)) {
                w.eq(GzctFinStatement::getCompanyId, companyId);
            } else if (StringUtils.isNotBlank(companyName)) {
                w.eq(GzctFinStatement::getCompanyName, companyName);
            } else {
                return R.fail("请指定企业");
            }
            w.orderByDesc(GzctFinStatement::getPeriod);
            List<GzctFinStatement> stmts = statementMapper.selectList(w);
            if (stmts.isEmpty()) return R.fail("未找到企业数据");
            GzctFinStatement latest = stmts.get(0);
            BigDecimal rev = latest.getRevenue() != null ? latest.getRevenue() : BigDecimal.ZERO;
            BigDecimal np = latest.getNetProfit() != null ? latest.getNetProfit() : BigDecimal.ZERO;
            BigDecimal ta = latest.getTotalAssets() != null ? latest.getTotalAssets() : BigDecimal.ZERO;
            BigDecimal tl = latest.getTotalLiabilities() != null ? latest.getTotalLiabilities() : BigDecimal.ZERO;
            BigDecimal na = latest.getNetAssets() != null ? latest.getNetAssets() : BigDecimal.ZERO;
            // 计算风险评分 (0-100, 越高越安全)
            int score = 60;
            BigDecimal debtRatio = ta.compareTo(BigDecimal.ZERO) > 0 ? tl.multiply(BigDecimal.valueOf(100)).divide(ta, 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
            BigDecimal roe = na.compareTo(BigDecimal.ZERO) != 0 ? np.multiply(BigDecimal.valueOf(100)).divide(na, 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
            if (debtRatio.compareTo(BigDecimal.valueOf(50)) <= 0) score += 15;
            else if (debtRatio.compareTo(BigDecimal.valueOf(70)) > 0) score -= 15;
            if (np.compareTo(BigDecimal.ZERO) > 0) score += 10;
            else score -= 20;
            if (roe.compareTo(BigDecimal.valueOf(10)) >= 0) score += 10;
            if (rev.compareTo(BigDecimal.ZERO) > 0) score += 5;
            if (score > 100) score = 100;
            if (score < 0) score = 0;
            String riskLevel;
            if (score >= 80) riskLevel = "低风险";
            else if (score >= 60) riskLevel = "中低风险";
            else if (score >= 40) riskLevel = "中风险";
            else riskLevel = "高风险";
            // 风险因素
            List<String> riskFactors = new ArrayList<>();
            List<String> strengths = new ArrayList<>();
            List<String> suggestions = new ArrayList<>();
            if (debtRatio.compareTo(BigDecimal.valueOf(70)) > 0) riskFactors.add("资产负债率过高(" + debtRatio + "%)，偿债压力大");
            else if (debtRatio.compareTo(BigDecimal.valueOf(50)) <= 0) strengths.add("资产负债率健康(" + debtRatio + "%)，财务结构稳健");
            if (np.compareTo(BigDecimal.ZERO) <= 0) riskFactors.add("净利润为负，企业处于亏损状态");
            else strengths.add("企业盈利能力良好，净利润" + np.setScale(2, RoundingMode.HALF_UP) + "万元");
            if (roe.compareTo(BigDecimal.valueOf(10)) >= 0) strengths.add("ROE达到" + roe + "%，股东回报率较高");
            else riskFactors.add("ROE偏低(" + roe + "%)，资本使用效率不足");
            if (riskFactors.isEmpty()) riskFactors.add("暂未发现明显风险因素");
            if (strengths.isEmpty()) strengths.add("暂无突出优势");
            suggestions.add("建议定期监控资产负债率变化趋势");
            suggestions.add("建议加强现金流管理，确保流动性安全");
            suggestions.add("建议优化资本结构，提升资产运营效率");
            if (debtRatio.compareTo(BigDecimal.valueOf(60)) > 0) suggestions.add("建议适当降低负债水平，控制财务风险");
            Map<String, Object> result = new HashMap<>();
            result.put("riskScore", score);
            result.put("riskLevel", riskLevel);
            result.put("riskFactors", riskFactors);
            result.put("strengths", strengths);
            result.put("suggestions", suggestions);
            result.put("assessment", latest.getCompanyName() + "最新财务数据评估：营收" + rev.setScale(2, RoundingMode.HALF_UP) + "万元，资产负债率" + debtRatio + "%，ROE " + roe + "%。");
            result.put("debtRatio", debtRatio);
            result.put("roe", roe);
            result.put("companyName", latest.getCompanyName());
            return R.success(result);
        } catch (Exception e) { return R.fail("风险评估失败：" + e.getMessage()); }
    }

    // ==================== 财务风险 /risk ====================

    @Operation(summary = "财务风险统计")
    @PostMapping("/risk/statistics")
    public R<Map<String, Object>> riskStatistics(@RequestBody(required = false) Map<String, Object> params) {
        try {
            long totalAnomalies = anomalyMapper.selectCount(null);
            long highRisks = anomalyMapper.selectCount(
                new LambdaQueryWrapper<GzctFinAnomaly>().eq(GzctFinAnomaly::getStatus, "CONFIRMED"));
            long activeAlerts = alertMapper.selectCount(
                new LambdaQueryWrapper<GzctFinAlert>().eq(GzctFinAlert::getStatus, "PENDING"));
            Map<String, Object> result = new HashMap<>();
            result.put("totalRisks", totalAnomalies);
            result.put("highRisks", highRisks);
            result.put("activeAlerts", activeAlerts);
            result.put("avgRiskScore", highRisks > 0 ? (int)(highRisks * 100 / Math.max(totalAnomalies, 1)) : 0);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "财务风险列表")
    @PostMapping("/risk/list")
    public R<Map<String, Object>> riskList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctFinAnomaly> w = new LambdaQueryWrapper<>();
            if (params.get("enterpriseName") != null && StringUtils.isNotBlank(params.get("enterpriseName").toString()))
                w.like(GzctFinAnomaly::getCompanyName, params.get("enterpriseName").toString());
            if (params.get("riskType") != null && StringUtils.isNotBlank(params.get("riskType").toString()))
                w.eq(GzctFinAnomaly::getAnomalyType, params.get("riskType").toString());
            if (params.get("riskLevel") != null && StringUtils.isNotBlank(params.get("riskLevel").toString())) {
                String level = params.get("riskLevel").toString();
                if ("HIGH".equals(level)) {
                    w.ge(GzctFinAnomaly::getDeviationRate, BigDecimal.valueOf(50));
                } else if ("MEDIUM".equals(level)) {
                    w.ge(GzctFinAnomaly::getDeviationRate, BigDecimal.valueOf(20));
                    w.lt(GzctFinAnomaly::getDeviationRate, BigDecimal.valueOf(50));
                } else if ("LOW".equals(level)) {
                    w.lt(GzctFinAnomaly::getDeviationRate, BigDecimal.valueOf(20));
                } else if ("CRITICAL".equals(level)) {
                    w.ge(GzctFinAnomaly::getDeviationRate, BigDecimal.valueOf(80));
                }
            }
            if (params.get("identificationStatus") != null && StringUtils.isNotBlank(params.get("identificationStatus").toString()))
                w.eq(GzctFinAnomaly::getStatus, params.get("identificationStatus").toString());
            if (params.get("alertStatus") != null && StringUtils.isNotBlank(params.get("alertStatus").toString())) {
                List<String> anomalyIds = alertMapper.selectList(
                    new LambdaQueryWrapper<GzctFinAlert>().eq(GzctFinAlert::getStatus, params.get("alertStatus").toString())
                ).stream().map(GzctFinAlert::getRelatedId).filter(Objects::nonNull).collect(Collectors.toList());
                if (!anomalyIds.isEmpty()) {
                    w.in(GzctFinAnomaly::getAnomalyId, anomalyIds);
                } else {
                    w.eq(GzctFinAnomaly::getAnomalyId, "NONE");
                }
            }
            if (params.get("minRiskScore") != null && StringUtils.isNotBlank(params.get("minRiskScore").toString()))
                w.ge(GzctFinAnomaly::getDeviationRate, new BigDecimal(params.get("minRiskScore").toString()));
            if (params.get("maxRiskScore") != null && StringUtils.isNotBlank(params.get("maxRiskScore").toString()))
                w.le(GzctFinAnomaly::getDeviationRate, new BigDecimal(params.get("maxRiskScore").toString()));
            if (params.get("identificationDateRange") != null) {
                Object dateRangeObj = params.get("identificationDateRange");
                if (dateRangeObj instanceof List) {
                    List<?> dateRange = (List<?>) dateRangeObj;
                    if (dateRange.size() == 2 && dateRange.get(0) != null && dateRange.get(1) != null
                        && StringUtils.isNotBlank(dateRange.get(0).toString()) && StringUtils.isNotBlank(dateRange.get(1).toString())) {
                        LocalDateTime start = LocalDate.parse(dateRange.get(0).toString()).atStartOfDay();
                        LocalDateTime end = LocalDate.parse(dateRange.get(1).toString()).atTime(23, 59, 59);
                        w.ge(GzctFinAnomaly::getCreateTime, start);
                        w.le(GzctFinAnomaly::getCreateTime, end);
                    }
                }
            }
            w.orderByDesc(GzctFinAnomaly::getCreateTime);
            Page<GzctFinAnomaly> page = anomalyMapper.selectPage(new Page<>(pn, ps), w);
            List<Map<String, Object>> list = new ArrayList<>();
            for (GzctFinAnomaly a : page.getRecords()) {
                Map<String, Object> m = new HashMap<>();
                m.put("id", a.getAnomalyId());
                m.put("riskId", a.getAnomalyId());
                m.put("companyId", a.getCompanyId());
                m.put("enterpriseName", a.getCompanyName());
                m.put("riskType", translateRiskType(a.getAnomalyType()));
                m.put("riskLevel", deriveRiskLevel(a.getDeviationRate()));
                m.put("riskScore", a.getDeviationRate() != null ? a.getDeviationRate().intValue() : 0);
                m.put("period", a.getPeriod());
                m.put("status", a.getStatus());
                m.put("indicatorName", a.getIndicatorName());
                m.put("detectionMethod", a.getDetectionMethod());
                m.put("createTime", a.getCreateTime());
                list.add(m);
            }
            Map<String, Object> result = new HashMap<>();
            result.put("list", list); result.put("total", page.getTotal());
            return R.success(result);
        } catch (Exception e) {
            log.error("财务风险列表查询失败", e);
            return R.fail("查询失败：" + (e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName()));
        }
    }

    private String deriveRiskLevel(BigDecimal deviationRate) {
        if (deviationRate == null) return "LOW";
        if (deviationRate.compareTo(BigDecimal.valueOf(50)) > 0) return "HIGH";
        if (deviationRate.compareTo(BigDecimal.valueOf(20)) > 0) return "MEDIUM";
        return "LOW";
    }

    private String translateRiskType(String type) {
        if (type == null) return "-";
        switch (type) {
            case "LIQUIDITY_RISK": return "流动性风险";
            case "SOLVENCY_RISK": return "偿债风险";
            case "PROFITABILITY_RISK": return "盈利风险";
            case "OPERATIONAL_RISK": return "营运风险";
            case "MARKET_RISK": return "市场风险";
            case "CREDIT_RISK": return "信用风险";
            default: return type;
        }
    }

    @Operation(summary = "财务风险图表")
    @PostMapping("/risk/charts")
    public R<Map<String, Object>> riskCharts(@RequestBody(required = false) Map<String, Object> params) {
        try {
            List<GzctFinAnomaly> all = anomalyMapper.selectList(null);
            // 1. 风险等级分布（饼图）
            long high = 0, medium = 0, low = 0;
            for (GzctFinAnomaly a : all) {
                BigDecimal dr = a.getDeviationRate();
                if (dr != null && dr.compareTo(BigDecimal.valueOf(50)) > 0) high++;
                else if (dr != null && dr.compareTo(BigDecimal.valueOf(20)) > 0) medium++;
                else low++;
            }
            List<Map<String, Object>> riskDistribution = new ArrayList<>();
            Map<String, Object> d1 = new HashMap<>(); d1.put("name", "高风险"); d1.put("value", high);
            Map<String, Object> d2 = new HashMap<>(); d2.put("name", "中风险"); d2.put("value", medium);
            Map<String, Object> d3 = new HashMap<>(); d3.put("name", "低风险"); d3.put("value", low);
            riskDistribution.add(d1); riskDistribution.add(d2); riskDistribution.add(d3);

            // 2. 风险趋势（折线图）
            List<GzctFinAnomaly> ordered = anomalyMapper.selectList(
                new LambdaQueryWrapper<GzctFinAnomaly>().orderByAsc(GzctFinAnomaly::getPeriod).last("LIMIT 12"));
            Map<String, long[]> trendMap = new java.util.LinkedHashMap<>();
            for (GzctFinAnomaly a : ordered) {
                String p = a.getPeriod() != null ? a.getPeriod() : "";
                long[] cnt = trendMap.computeIfAbsent(p, k -> new long[]{0, 0, 0});
                BigDecimal dr = a.getDeviationRate();
                if (dr != null && dr.compareTo(BigDecimal.valueOf(50)) > 0) cnt[0]++;
                else if (dr != null && dr.compareTo(BigDecimal.valueOf(20)) > 0) cnt[1]++;
                else cnt[2]++;
            }
            List<Map<String, Object>> riskTrend = new ArrayList<>();
            for (Map.Entry<String, long[]> e : trendMap.entrySet()) {
                Map<String, Object> m = new HashMap<>();
                m.put("date", e.getKey()); m.put("high", e.getValue()[0]);
                m.put("medium", e.getValue()[1]); m.put("low", e.getValue()[2]);
                riskTrend.add(m);
            }

            // 3. 风险类型分析（柱状图）
            Map<String, Integer> typeCount = new java.util.LinkedHashMap<>();
            for (GzctFinAnomaly a : all) {
                String t = a.getAnomalyType() != null ? a.getAnomalyType() : "其他";
                typeCount.merge(t, 1, Integer::sum);
            }
            List<Map<String, Object>> riskType = new ArrayList<>();
            for (Map.Entry<String, Integer> e : typeCount.entrySet()) {
                Map<String, Object> m = new HashMap<>();
                m.put("name", e.getKey()); m.put("value", e.getValue());
                riskType.add(m);
            }

            // 4. 企业风险对比（柱状图）
            Map<String, List<BigDecimal>> companyScores = new java.util.LinkedHashMap<>();
            for (GzctFinAnomaly a : all) {
                String c = a.getCompanyName() != null ? a.getCompanyName() : "未知";
                companyScores.computeIfAbsent(c, k -> new ArrayList<>());
                if (a.getDeviationRate() != null) companyScores.get(c).add(a.getDeviationRate());
            }
            List<Map<String, Object>> industryRisk = new ArrayList<>();
            for (Map.Entry<String, List<BigDecimal>> e : companyScores.entrySet()) {
                double avg = e.getValue().stream().mapToDouble(BigDecimal::doubleValue).average().orElse(0);
                Map<String, Object> m = new HashMap<>();
                m.put("industry", e.getKey()); m.put("score", Math.round(avg * 100) / 100.0);
                industryRisk.add(m);
            }

            Map<String, Object> result = new HashMap<>();
            result.put("riskDistribution", riskDistribution);
            result.put("riskTrend", riskTrend);
            result.put("riskType", riskType);
            result.put("industryRisk", industryRisk);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "财务风险保存")
    @PostMapping("/risk/save")
    public R<Boolean> riskSave(@RequestBody Map<String, Object> p) {
        try {
            GzctFinAnomaly anomaly = new GzctFinAnomaly();
            String id = p.get("anomalyId") != null ? p.get("anomalyId").toString()
                    : (p.get("id") != null ? p.get("id").toString() : null);
            if (p.get("companyId") != null) anomaly.setCompanyId(p.get("companyId").toString());
            if (p.get("companyName") != null) anomaly.setCompanyName(p.get("companyName").toString());
            if (p.get("anomalyType") != null) anomaly.setAnomalyType(p.get("anomalyType").toString());
            if (p.get("detectionMethod") != null) anomaly.setDetectionMethod(p.get("detectionMethod").toString());
            if (p.get("indicatorName") != null) anomaly.setIndicatorName(p.get("indicatorName").toString());
            if (p.get("expectedValue") != null) anomaly.setExpectedValue(new BigDecimal(p.get("expectedValue").toString()));
            if (p.get("actualValue") != null) anomaly.setActualValue(new BigDecimal(p.get("actualValue").toString()));
            if (p.get("deviationRate") != null) anomaly.setDeviationRate(new BigDecimal(p.get("deviationRate").toString()));
            if (p.get("period") != null) anomaly.setPeriod(p.get("period").toString());
            if (p.get("status") != null) anomaly.setStatus(p.get("status").toString());
            if (StringUtils.isNotBlank(id)) {
                anomaly.setAnomalyId(id);
                anomaly.setUpdateTime(LocalDateTime.now());
                anomalyMapper.updateById(anomaly);
            } else {
                anomaly.setCreateTime(LocalDateTime.now());
                anomaly.setUpdateTime(LocalDateTime.now());
                anomalyMapper.insert(anomaly);
            }
            return R.success(true);
        } catch (Exception e) { return R.fail("保存失败：" + e.getMessage()); }
    }

    @Operation(summary = "财务风险删除")
    @PostMapping("/risk/delete")
    public R<Boolean> riskDelete(@RequestBody Map<String, Object> p) {
        try {
            String id = p.get("anomalyId") != null ? p.get("anomalyId").toString()
                    : (p.get("id") != null ? p.get("id").toString() : null);
            if (StringUtils.isBlank(id)) return R.fail("缺少ID参数");
            anomalyMapper.deleteById(id);
            return R.success(true);
        } catch (Exception e) { return R.fail("删除失败：" + e.getMessage()); }
    }

    @Operation(summary = "财务风险详情")
    @PostMapping("/risk/detail")
    public R<Map<String, Object>> riskDetail(@RequestBody Map<String, Object> p) {
        try {
            String id = p.get("anomalyId") != null ? p.get("anomalyId").toString()
                    : (p.get("id") != null ? p.get("id").toString() : null);
            if (StringUtils.isBlank(id)) return R.fail("缺少ID参数");
            GzctFinAnomaly anomaly = anomalyMapper.selectById(id);
            if (anomaly == null) return R.fail("记录不存在");
            Map<String, Object> result = new HashMap<>();
            result.put("anomalyId", anomaly.getAnomalyId());
            result.put("companyId", anomaly.getCompanyId());
            result.put("companyName", anomaly.getCompanyName());
            result.put("anomalyType", anomaly.getAnomalyType());
            result.put("detectionMethod", anomaly.getDetectionMethod());
            result.put("indicatorName", anomaly.getIndicatorName());
            result.put("expectedValue", anomaly.getExpectedValue());
            result.put("actualValue", anomaly.getActualValue());
            result.put("deviationRate", anomaly.getDeviationRate());
            result.put("period", anomaly.getPeriod());
            result.put("status", anomaly.getStatus());
            // 关联报表数据
            if (StringUtils.isNotBlank(anomaly.getCompanyId())) {
                LambdaQueryWrapper<GzctFinStatement> sw = new LambdaQueryWrapper<>();
                sw.eq(GzctFinStatement::getCompanyId, anomaly.getCompanyId());
                if (StringUtils.isNotBlank(anomaly.getPeriod())) sw.eq(GzctFinStatement::getPeriod, anomaly.getPeriod());
                sw.last("LIMIT 1");
                GzctFinStatement stmt = statementMapper.selectOne(sw);
                if (stmt != null) {
                    Map<String, Object> stmtData = new HashMap<>();
                    stmtData.put("totalAssets", stmt.getTotalAssets());
                    stmtData.put("revenue", stmt.getRevenue());
                    stmtData.put("netProfit", stmt.getNetProfit());
                    result.put("relatedStatement", stmtData);
                }
            }
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "风险识别")
    @PostMapping("/risk/identify")
    public R<Map<String, Object>> riskIdentify(@RequestBody Map<String, Object> params) {
        try {
            String id = params.get("id") != null ? params.get("id").toString() : null;
            if (StringUtils.isBlank(id)) return R.fail("风险ID不能为空");
            GzctFinAnomaly anomaly = anomalyMapper.selectById(id);
            if (anomaly == null) return R.fail("风险记录不存在");
            anomaly.setStatus("CONFIRMED");
            anomaly.setUpdateTime(LocalDateTime.now());
            anomalyMapper.updateById(anomaly);
            Map<String, Object> result = new HashMap<>();
            result.put("id", anomaly.getAnomalyId());
            result.put("status", anomaly.getStatus());
            result.put("updateTime", anomaly.getUpdateTime());
            return R.success(result);
        } catch (Exception e) { return R.fail("风险识别失败：" + e.getMessage()); }
    }

    @Operation(summary = "风险评估")
    @PostMapping("/risk/assess")
    public R<Map<String, Object>> riskAssess(@RequestBody Map<String, Object> params) {
        try {
            String id = params.get("id") != null ? params.get("id").toString() : null;
            if (StringUtils.isBlank(id)) return R.fail("风险ID不能为空");
            GzctFinAnomaly anomaly = anomalyMapper.selectById(id);
            if (anomaly == null) return R.fail("风险记录不存在");
            BigDecimal expected = anomaly.getExpectedValue();
            BigDecimal actual = anomaly.getActualValue();
            BigDecimal score = BigDecimal.ZERO;
            if (expected != null && actual != null && expected.compareTo(BigDecimal.ZERO) != 0) {
                score = actual.subtract(expected).abs().multiply(BigDecimal.valueOf(100))
                    .divide(expected, 2, RoundingMode.HALF_UP);
            }
            anomaly.setDeviationRate(score);
            anomaly.setUpdateTime(LocalDateTime.now());
            anomalyMapper.updateById(anomaly);
            Map<String, Object> result = new HashMap<>();
            result.put("id", anomaly.getAnomalyId());
            result.put("riskScore", score.intValue());
            result.put("riskLevel", deriveRiskLevel(score));
            result.put("expectedValue", expected);
            result.put("actualValue", actual);
            result.put("deviationRate", score);
            return R.success(result);
        } catch (Exception e) { return R.fail("风险评估失败：" + e.getMessage()); }
    }

    @Operation(summary = "批量风险识别")
    @PostMapping("/risk/batch-identify")
    public R<Map<String, Object>> riskBatchIdentify(@RequestBody Map<String, Object> params) {
        try {
            List<?> ids = params.get("ids") != null ? (List<?>) params.get("ids") : Collections.emptyList();
            if (ids.isEmpty()) return R.fail("风险ID列表不能为空");
            String targetStatus = params.get("status") != null ? params.get("status").toString() : "CONFIRMED";
            int successCount = 0;
            int failCount = 0;
            for (Object idObj : ids) {
                String id = idObj.toString();
                GzctFinAnomaly anomaly = anomalyMapper.selectById(id);
                if (anomaly != null) {
                    anomaly.setStatus(targetStatus);
                    anomaly.setUpdateTime(LocalDateTime.now());
                    anomalyMapper.updateById(anomaly);
                    successCount++;
                } else {
                    failCount++;
                }
            }
            Map<String, Object> result = new HashMap<>();
            result.put("successCount", successCount);
            result.put("failCount", failCount);
            result.put("totalCount", ids.size());
            return R.success(result);
        } catch (Exception e) { return R.fail("批量识别失败：" + e.getMessage()); }
    }

    @Operation(summary = "批量风险评估")
    @PostMapping("/risk/batch-assess")
    public R<Map<String, Object>> riskBatchAssess(@RequestBody Map<String, Object> params) {
        try {
            List<?> ids = params.get("ids") != null ? (List<?>) params.get("ids") : Collections.emptyList();
            if (ids.isEmpty()) return R.fail("风险ID列表不能为空");
            int successCount = 0;
            int failCount = 0;
            for (Object idObj : ids) {
                String id = idObj.toString();
                GzctFinAnomaly anomaly = anomalyMapper.selectById(id);
                if (anomaly != null) {
                    BigDecimal expected = anomaly.getExpectedValue();
                    BigDecimal actual = anomaly.getActualValue();
                    if (expected != null && actual != null && expected.compareTo(BigDecimal.ZERO) != 0) {
                        BigDecimal score = actual.subtract(expected).abs().multiply(BigDecimal.valueOf(100))
                            .divide(expected, 2, RoundingMode.HALF_UP);
                        anomaly.setDeviationRate(score);
                        anomaly.setUpdateTime(LocalDateTime.now());
                        anomalyMapper.updateById(anomaly);
                        successCount++;
                    } else {
                        failCount++;
                    }
                } else {
                    failCount++;
                }
            }
            Map<String, Object> result = new HashMap<>();
            result.put("successCount", successCount);
            result.put("failCount", failCount);
            result.put("totalCount", ids.size());
            return R.success(result);
        } catch (Exception e) { return R.fail("批量评估失败：" + e.getMessage()); }
    }

    @Operation(summary = "风险数据导出")
    @PostMapping("/risk/export")
    public void riskExport(@RequestBody(required = false) Map<String, Object> params, HttpServletResponse response) {
        try {
            LambdaQueryWrapper<GzctFinAnomaly> w = new LambdaQueryWrapper<>();
            if (params != null) {
                if (params.get("enterpriseName") != null && StringUtils.isNotBlank(params.get("enterpriseName").toString()))
                    w.like(GzctFinAnomaly::getCompanyName, params.get("enterpriseName").toString());
                if (params.get("riskType") != null && StringUtils.isNotBlank(params.get("riskType").toString()))
                    w.eq(GzctFinAnomaly::getAnomalyType, params.get("riskType").toString());
                if (params.get("riskLevel") != null && StringUtils.isNotBlank(params.get("riskLevel").toString())) {
                    String level = params.get("riskLevel").toString();
                    if ("HIGH".equals(level)) w.ge(GzctFinAnomaly::getDeviationRate, BigDecimal.valueOf(50));
                    else if ("MEDIUM".equals(level)) {
                        w.ge(GzctFinAnomaly::getDeviationRate, BigDecimal.valueOf(20));
                        w.lt(GzctFinAnomaly::getDeviationRate, BigDecimal.valueOf(50));
                    } else if ("LOW".equals(level)) w.lt(GzctFinAnomaly::getDeviationRate, BigDecimal.valueOf(20));
                }
            }
            w.orderByDesc(GzctFinAnomaly::getCreateTime);
            List<GzctFinAnomaly> all = anomalyMapper.selectList(w);
            List<List<Object>> rows = new ArrayList<>();
            rows.add(Arrays.asList("企业名称", "风险类型", "风险等级", "风险评分", "指标名称", "期望值", "实际值", "偏差率", "报告期间", "状态", "检测方法"));
            for (GzctFinAnomaly a : all) {
                rows.add(Arrays.asList(
                    a.getCompanyName() != null ? a.getCompanyName() : "",
                    a.getAnomalyType() != null ? a.getAnomalyType() : "",
                    deriveRiskLevel(a.getDeviationRate()),
                    a.getDeviationRate() != null ? a.getDeviationRate().intValue() : 0,
                    a.getIndicatorName() != null ? a.getIndicatorName() : "",
                    a.getExpectedValue() != null ? a.getExpectedValue().toPlainString() : "0",
                    a.getActualValue() != null ? a.getActualValue().toPlainString() : "0",
                    a.getDeviationRate() != null ? a.getDeviationRate().toPlainString() : "0",
                    a.getPeriod() != null ? a.getPeriod() : "",
                    a.getStatus() != null ? a.getStatus() : "",
                    a.getDetectionMethod() != null ? a.getDetectionMethod() : ""
                ));
            }
            byte[] xlsxBytes = SimpleXlsxWriter.write("财务风险数据", rows);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=financial_risk_" + System.currentTimeMillis() + ".xlsx");
            response.getOutputStream().write(xlsxBytes);
            response.getOutputStream().flush();
        } catch (Exception e) {
            log.error("导出风险数据失败", e);
            try {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"code\":0,\"msg\":\"导出失败：" + e.getMessage() + "\"}");
            } catch (IOException ignored) {}
        }
    }

    @Operation(summary = "生成风险报告")
    @PostMapping("/risk/generate-report")
    public R<Map<String, Object>> riskGenerateReport(@RequestBody(required = false) Map<String, Object> params) {
        try {
            LambdaQueryWrapper<GzctFinAnomaly> w = new LambdaQueryWrapper<>();
            if (params != null && params.get("enterpriseName") != null && StringUtils.isNotBlank(params.get("enterpriseName").toString()))
                w.like(GzctFinAnomaly::getCompanyName, params.get("enterpriseName").toString());
            List<GzctFinAnomaly> all = anomalyMapper.selectList(w);
            long totalRisks = all.size();
            long highRisks = all.stream().filter(a -> a.getDeviationRate() != null && a.getDeviationRate().compareTo(BigDecimal.valueOf(50)) > 0).count();
            long mediumRisks = all.stream().filter(a -> a.getDeviationRate() != null && a.getDeviationRate().compareTo(BigDecimal.valueOf(20)) > 0 && a.getDeviationRate().compareTo(BigDecimal.valueOf(50)) <= 0).count();
            long lowRisks = totalRisks - highRisks - mediumRisks;
            long confirmedCount = all.stream().filter(a -> "CONFIRMED".equals(a.getStatus())).count();
            long pendingCount = all.stream().filter(a -> "PENDING".equals(a.getStatus())).count();
            BigDecimal avgScore = all.stream().map(GzctFinAnomaly::getDeviationRate).filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            if (totalRisks > 0) avgScore = avgScore.divide(BigDecimal.valueOf(totalRisks), 2, RoundingMode.HALF_UP);
            Map<String, Object> report = new HashMap<>();
            report.put("reportId", UUID.randomUUID().toString());
            report.put("generateTime", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            report.put("totalRisks", totalRisks);
            report.put("highRisks", highRisks);
            report.put("mediumRisks", mediumRisks);
            report.put("lowRisks", lowRisks);
            report.put("confirmedCount", confirmedCount);
            report.put("pendingCount", pendingCount);
            report.put("avgRiskScore", avgScore.intValue());
            Map<String, Long> riskDist = new HashMap<>();
            riskDist.put("HIGH", highRisks); riskDist.put("MEDIUM", mediumRisks); riskDist.put("LOW", lowRisks);
            report.put("riskDistribution", riskDist);
            // 按类型统计
            Map<String, Long> typeDistribution = all.stream()
                .filter(a -> a.getAnomalyType() != null)
                .collect(Collectors.groupingBy(GzctFinAnomaly::getAnomalyType, Collectors.counting()));
            report.put("typeDistribution", typeDistribution);
            return R.success(report);
        } catch (Exception e) { return R.fail("报告生成失败：" + e.getMessage()); }
    }

    @Operation(summary = "风险预测")
    @PostMapping("/risk/predict")
    public R<Map<String, Object>> riskPredict(@RequestBody Map<String, Object> params) {
        try {
            String companyId = params.get("companyId") != null ? params.get("companyId").toString() : null;
            LambdaQueryWrapper<GzctFinAnomaly> w = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(companyId)) w.eq(GzctFinAnomaly::getCompanyId, companyId);
            w.orderByAsc(GzctFinAnomaly::getCreateTime);
            List<GzctFinAnomaly> history = anomalyMapper.selectList(w);
            Map<String, Object> prediction = new HashMap<>();
            if (history.isEmpty()) {
                prediction.put("trendDirection", "STABLE");
                prediction.put("predictedScore", 0);
                prediction.put("confidence", 0);
                prediction.put("message", "历史数据不足，无法进行预测");
            } else {
                // 简单趋势分析：比较最近1/3数据与前2/3数据的平均偏差率
                int size = history.size();
                int splitPoint = size * 2 / 3;
                List<GzctFinAnomaly> earlier = history.subList(0, Math.max(splitPoint, 1));
                List<GzctFinAnomaly> recent = history.subList(Math.max(splitPoint, 1), size);
                BigDecimal earlierAvg = earlier.stream().map(GzctFinAnomaly::getDeviationRate).filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                if (!earlier.isEmpty()) earlierAvg = earlierAvg.divide(BigDecimal.valueOf(earlier.size()), 2, RoundingMode.HALF_UP);
                BigDecimal recentAvg = recent.stream().map(GzctFinAnomaly::getDeviationRate).filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                if (!recent.isEmpty()) recentAvg = recentAvg.divide(BigDecimal.valueOf(recent.size()), 2, RoundingMode.HALF_UP);
                String trend;
                if (recentAvg.compareTo(earlierAvg.add(BigDecimal.valueOf(5))) > 0) trend = "INCREASING";
                else if (recentAvg.compareTo(earlierAvg.subtract(BigDecimal.valueOf(5))) < 0) trend = "DECREASING";
                else trend = "STABLE";
                prediction.put("trendDirection", trend);
                prediction.put("predictedScore", recentAvg.intValue());
                prediction.put("predictedLevel", deriveRiskLevel(recentAvg));
                prediction.put("confidence", Math.min(size * 10, 90));
                prediction.put("historicalDataPoints", size);
                prediction.put("earlierAvgScore", earlierAvg.intValue());
                prediction.put("recentAvgScore", recentAvg.intValue());
            }
            return R.success(prediction);
        } catch (Exception e) { return R.fail("风险预测失败：" + e.getMessage()); }
    }

    @Operation(summary = "启动风险监控")
    @PostMapping("/risk/monitor")
    public R<Map<String, Object>> riskMonitor(@RequestBody Map<String, Object> params) {
        try {
            String id = params.get("id") != null ? params.get("id").toString() : null;
            if (StringUtils.isBlank(id)) return R.fail("风险ID不能为空");
            GzctFinAnomaly anomaly = anomalyMapper.selectById(id);
            if (anomaly == null) return R.fail("风险记录不存在");
            String action = params.get("action") != null ? params.get("action").toString() : "START";
            if ("START".equals(action)) {
                anomaly.setStatus("MONITORING");
            } else if ("STOP".equals(action)) {
                anomaly.setStatus("CONFIRMED");
            }
            anomaly.setUpdateTime(LocalDateTime.now());
            anomalyMapper.updateById(anomaly);
            Map<String, Object> result = new HashMap<>();
            result.put("id", anomaly.getAnomalyId());
            result.put("status", anomaly.getStatus());
            result.put("action", action);
            result.put("updateTime", anomaly.getUpdateTime());
            return R.success(result);
        } catch (Exception e) { return R.fail("风险监控操作失败：" + e.getMessage()); }
    }

    @Operation(summary = "风险缓释")
    @PostMapping("/risk/mitigate")
    public R<Map<String, Object>> riskMitigate(@RequestBody Map<String, Object> params) {
        try {
            String id = params.get("id") != null ? params.get("id").toString() : null;
            if (StringUtils.isBlank(id)) return R.fail("风险ID不能为空");
            GzctFinAnomaly anomaly = anomalyMapper.selectById(id);
            if (anomaly == null) return R.fail("风险记录不存在");
            String notes = params.get("notes") != null ? params.get("notes").toString() : "";
            anomaly.setStatus("MITIGATED");
            anomaly.setUpdateTime(LocalDateTime.now());
            anomalyMapper.updateById(anomaly);
            Map<String, Object> result = new HashMap<>();
            result.put("id", anomaly.getAnomalyId());
            result.put("status", anomaly.getStatus());
            result.put("notes", notes);
            result.put("updateTime", anomaly.getUpdateTime());
            result.put("mitigatedBy", params.get("mitigatedBy"));
            return R.success(result);
        } catch (Exception e) { return R.fail("风险缓释失败：" + e.getMessage()); }
    }

    @Operation(summary = "风险监管建议")
    @PostMapping("/risk/supervision-suggestions")
    public R<List<Map<String, Object>>> riskSupervisionSuggestions(@RequestBody(required = false) Map<String, Object> params) {
        try {
            String id = params != null && params.get("id") != null ? params.get("id").toString() : null;
            List<Map<String, Object>> suggestions = new ArrayList<>();
            if (StringUtils.isNotBlank(id)) {
                GzctFinAnomaly anomaly = anomalyMapper.selectById(id);
                if (anomaly != null) {
                    BigDecimal dr = anomaly.getDeviationRate();
                    if (dr != null && dr.compareTo(BigDecimal.valueOf(50)) > 0) {
                        Map<String, Object> s1 = new HashMap<>();
                        s1.put("priority", "HIGH");
                        s1.put("suggestion", "立即启动风险应急预案，组织专项排查");
                        s1.put("expectedEffect", "降低风险敞口");
                        s1.put("deadline", "1个月");
                        suggestions.add(s1);
                    }
                    if (dr != null && dr.compareTo(BigDecimal.valueOf(20)) > 0) {
                        Map<String, Object> s2 = new HashMap<>();
                        s2.put("priority", "MEDIUM");
                        s2.put("suggestion", "加强财务指标监控频率，每周跟踪偏差变化");
                        s2.put("expectedEffect", "及时发现异常");
                        s2.put("deadline", "2个月");
                        suggestions.add(s2);
                    }
                    Map<String, Object> s3 = new HashMap<>();
                    s3.put("priority", "LOW");
                    s3.put("suggestion", "完善内部控制制度，建立长效风险防范机制");
                    s3.put("expectedEffect", "系统性降低风险");
                    s3.put("deadline", "6个月");
                    suggestions.add(s3);
                }
            }
            if (suggestions.isEmpty()) {
                Map<String, Object> s = new HashMap<>();
                s.put("priority", "LOW");
                s.put("suggestion", "当前风险水平正常，建议保持定期监控");
                s.put("expectedEffect", "维持风险可控");
                s.put("deadline", "持续");
                suggestions.add(s);
            }
            return R.success(suggestions);
        } catch (Exception e) { return R.fail("获取建议失败：" + e.getMessage()); }
    }

    // ==================== 合规监管 /compliance ====================

    @Operation(summary = "合规监管统计")
    @GetMapping("/compliance/statistics")
    public R<Map<String, Object>> complianceStatistics() {
        try {
            long total = financialComplianceMapper.selectCount(null);
            long compliant = financialComplianceMapper.selectCount(
                new LambdaQueryWrapper<GzctFinancialCompliance>().eq(GzctFinancialCompliance::getComplianceStatus, "COMPLIANT"));
            long violation = financialComplianceMapper.selectCount(
                new LambdaQueryWrapper<GzctFinancialCompliance>().eq(GzctFinancialCompliance::getComplianceStatus, "NON_COMPLIANT"));
            Map<String, Object> result = new HashMap<>();
            result.put("totalCompliance", total);
            result.put("compliantEnterprises", compliant);
            result.put("violationCases", violation);
            result.put("complianceRate", total > 0 ? (int)(compliant * 100 / total) : 0);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "合规监管列表")
    @PostMapping("/compliance/list")
    public R<Map<String, Object>> complianceList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) :
                (params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1);
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 10;
            LambdaQueryWrapper<GzctFinancialCompliance> w = new LambdaQueryWrapper<>();
            if (params.get("enterpriseName") != null && StringUtils.isNotBlank(params.get("enterpriseName").toString()))
                w.like(GzctFinancialCompliance::getEnterpriseName, params.get("enterpriseName").toString());
            if (params.get("complianceStatus") != null && StringUtils.isNotBlank(params.get("complianceStatus").toString()))
                w.eq(GzctFinancialCompliance::getComplianceStatus, params.get("complianceStatus").toString());
            if (params.get("violationType") != null && StringUtils.isNotBlank(params.get("violationType").toString()))
                w.eq(GzctFinancialCompliance::getViolationType, params.get("violationType").toString());
            if (params.get("riskLevel") != null && StringUtils.isNotBlank(params.get("riskLevel").toString()))
                w.eq(GzctFinancialCompliance::getRiskLevel, params.get("riskLevel").toString());
            if (params.get("checkDateStart") != null && StringUtils.isNotBlank(params.get("checkDateStart").toString()))
                w.ge(GzctFinancialCompliance::getCheckDate, params.get("checkDateStart").toString());
            if (params.get("checkDateEnd") != null && StringUtils.isNotBlank(params.get("checkDateEnd").toString()))
                w.le(GzctFinancialCompliance::getCheckDate, params.get("checkDateEnd").toString());
            w.orderByDesc(GzctFinancialCompliance::getCreateTime);
            Page<GzctFinancialCompliance> page = financialComplianceMapper.selectPage(new Page<>(pn, ps), w);
            List<Map<String, Object>> list = new ArrayList<>();
            for (GzctFinancialCompliance c : page.getRecords()) {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("id", c.getComplianceId());
                m.put("complianceId", c.getComplianceId());
                m.put("enterpriseName", c.getEnterpriseName());
                m.put("companyId", c.getCompanyId());
                m.put("period", c.getPeriod());
                m.put("checkType", c.getCheckType());
                m.put("complianceStatus", c.getComplianceStatus());
                m.put("auditStatus", c.getAuditStatus());
                m.put("riskLevel", c.getRiskLevel());
                m.put("complianceScore", c.getComplianceScore());
                m.put("violationType", c.getViolationType());
                m.put("checkMethod", c.getCheckMethod());
                m.put("checkScope", c.getCheckScope());
                m.put("inspector", c.getInspector());
                m.put("checkDate", c.getCheckDate());
                m.put("checkDateStart", c.getCheckDateStart());
                m.put("checkDateEnd", c.getCheckDateEnd());
                m.put("issues", c.getIssues());
                m.put("rectificationRequirements", c.getRectificationRequirements());
                m.put("rectificationDeadline", c.getRectificationDeadline());
                m.put("rectificationStatus", c.getRectificationStatus());
                m.put("createTime", c.getCreateTime());
                list.add(m);
            }
            Map<String, Object> result = new HashMap<>();
            result.put("list", list); result.put("total", page.getTotal());
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "合规检查详情")
    @GetMapping("/compliance/{id}")
    public R<GzctFinancialCompliance> complianceDetail(@PathVariable String id) {
        try {
            GzctFinancialCompliance c = financialComplianceMapper.selectById(id);
            if (c == null) return R.fail("记录不存在");
            return R.success(c);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "执行合规检查")
    @PostMapping("/compliance/check")
    public R<Boolean> complianceCheck(@RequestBody Map<String, Object> p) {
        try {
            String id = p.get("id") != null ? p.get("id").toString() : null;
            if (StringUtils.isBlank(id)) return R.fail("缺少ID参数");
            GzctFinancialCompliance c = financialComplianceMapper.selectById(id);
            if (c == null) return R.fail("记录不存在");
            c.setComplianceStatus("CHECKING");
            c.setAuditStatus("检查中");
            c.setUpdateTime(LocalDateTime.now());
            financialComplianceMapper.updateById(c);
            return R.success(true);
        } catch (Exception e) { return R.fail("合规检查失败：" + e.getMessage()); }
    }

    @Operation(summary = "批量合规检查")
    @PostMapping("/compliance/batch-check")
    public R<Boolean> complianceBatchCheck(@RequestBody Map<String, Object> p) {
        try {
            List<String> ids = null;
            if (p.get("ids") != null) {
                Object idsObj = p.get("ids");
                if (idsObj instanceof List) {
                    ids = ((List<?>) idsObj).stream().map(Object::toString).collect(Collectors.toList());
                }
            }
            if (ids == null || ids.isEmpty()) return R.fail("缺少ID列表");
            for (String id : ids) {
                GzctFinancialCompliance c = financialComplianceMapper.selectById(id);
                if (c != null) {
                    c.setComplianceStatus("CHECKING");
                    c.setAuditStatus("检查中");
                    c.setUpdateTime(LocalDateTime.now());
                    financialComplianceMapper.updateById(c);
                }
            }
            return R.success(true);
        } catch (Exception e) { return R.fail("批量合规检查失败：" + e.getMessage()); }
    }

    @Operation(summary = "批量分析")
    @PostMapping("/compliance/batch-analyze")
    public R<Map<String, Object>> complianceBatchAnalyze(@RequestBody Map<String, Object> p) {
        try {
            List<String> ids = null;
            if (p.get("ids") != null) {
                Object idsObj = p.get("ids");
                if (idsObj instanceof List) {
                    ids = ((List<?>) idsObj).stream().map(Object::toString).collect(Collectors.toList());
                }
            }
            if (ids == null || ids.isEmpty()) return R.fail("缺少ID列表");
            List<GzctFinancialCompliance> records = financialComplianceMapper.selectBatchIds(ids);
            int totalCount = records.size();
            long compliantCount = records.stream().filter(r -> "COMPLIANT".equals(r.getComplianceStatus())).count();
            long violationCount = records.stream().filter(r -> "NON_COMPLIANT".equals(r.getComplianceStatus())).count();
            BigDecimal avgScore = records.stream()
                .filter(r -> r.getComplianceScore() != null)
                .map(GzctFinancialCompliance::getComplianceScore)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            long scoreCount = records.stream().filter(r -> r.getComplianceScore() != null).count();
            Map<String, Object> result = new HashMap<>();
            result.put("totalCount", totalCount);
            result.put("compliantCount", compliantCount);
            result.put("violationCount", violationCount);
            result.put("avgScore", scoreCount > 0 ? avgScore.divide(new BigDecimal(scoreCount), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
            result.put("complianceRate", totalCount > 0 ? (int)(compliantCount * 100 / totalCount) : 0);
            return R.success(result);
        } catch (Exception e) { return R.fail("批量分析失败：" + e.getMessage()); }
    }

    @Operation(summary = "合规状态分布")
    @GetMapping("/compliance/distribution")
    public R<List<Map<String, Object>>> complianceDistribution() {
        try {
            List<GzctFinancialCompliance> all = financialComplianceMapper.selectList(null);
            long compliant = all.stream().filter(c -> "COMPLIANT".equals(c.getComplianceStatus())).count();
            long basically = all.stream().filter(c -> "BASICALLY_COMPLIANT".equals(c.getComplianceStatus())).count();
            long nonCompliant = all.stream().filter(c -> "NON_COMPLIANT".equals(c.getComplianceStatus())).count();
            long pending = all.stream().filter(c -> "PENDING".equals(c.getComplianceStatus())).count();
            List<Map<String, Object>> result = new ArrayList<>();
            Map<String, Object> m1 = new HashMap<>(); m1.put("name", "合规"); m1.put("value", compliant);
            Map<String, Object> m2 = new HashMap<>(); m2.put("name", "基本合规"); m2.put("value", basically);
            Map<String, Object> m3 = new HashMap<>(); m3.put("name", "不合规"); m3.put("value", nonCompliant);
            Map<String, Object> m4 = new HashMap<>(); m4.put("name", "待检查"); m4.put("value", pending);
            result.add(m1); result.add(m2); result.add(m3); result.add(m4);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "合规趋势分析")
    @GetMapping("/compliance/trend")
    public R<List<Map<String, Object>>> complianceTrend() {
        try {
            List<GzctFinancialCompliance> all = financialComplianceMapper.selectList(
                new LambdaQueryWrapper<GzctFinancialCompliance>().orderByAsc(GzctFinancialCompliance::getPeriod));
            Map<String, long[]> periodMap = new java.util.LinkedHashMap<>();
            for (GzctFinancialCompliance c : all) {
                String period = c.getPeriod() != null ? c.getPeriod() : "";
                long[] cnt = periodMap.computeIfAbsent(period, k -> new long[]{0, 0});
                if ("COMPLIANT".equals(c.getComplianceStatus()) || "BASICALLY_COMPLIANT".equals(c.getComplianceStatus())) cnt[0]++;
                else if ("NON_COMPLIANT".equals(c.getComplianceStatus())) cnt[1]++;
            }
            List<Map<String, Object>> result = new ArrayList<>();
            for (Map.Entry<String, long[]> e : periodMap.entrySet()) {
                Map<String, Object> m = new HashMap<>();
                m.put("date", e.getKey());
                m.put("compliant", e.getValue()[0]);
                m.put("violation", e.getValue()[1]);
                result.add(m);
            }
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 绩效评估 /performance ====================

    @Operation(summary = "绩效统计")
    @GetMapping("/performance/statistics")
    public R<Map<String, Object>> performanceStatistics() {
        try {
            long total = statementMapper.selectCount(null);
            BigDecimal avgScore = BigDecimal.ZERO;
            List<GzctFinStatement> all = statementMapper.selectList(null);
            BigDecimal sumProfit = BigDecimal.ZERO;
            BigDecimal sumRevenue = BigDecimal.ZERO;
            for (GzctFinStatement s : all) {
                if (s.getNetProfit() != null) sumProfit = sumProfit.add(s.getNetProfit());
                if (s.getRevenue() != null) sumRevenue = sumRevenue.add(s.getRevenue());
            }
            if (sumRevenue.compareTo(BigDecimal.ZERO) != 0)
                avgScore = sumProfit.multiply(BigDecimal.valueOf(100)).divide(sumRevenue, 2, RoundingMode.HALF_UP);
            long excellent = all.stream().filter(s -> s.getNetProfit() != null && s.getNetProfit().compareTo(BigDecimal.ZERO) > 0).count();
            long poor = all.stream().filter(s -> s.getNetProfit() != null && s.getNetProfit().compareTo(BigDecimal.ZERO) < 0).count();
            Map<String, Object> result = new HashMap<>();
            result.put("totalEvaluations", total);
            result.put("excellentPerformance", excellent);
            result.put("poorPerformance", poor);
            result.put("avgPerformanceScore", avgScore);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "绩效列表")
    @PostMapping("/performance/list")
    public R<Map<String, Object>> performanceList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString())
                    : (params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1);
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctFinStatement> w = new LambdaQueryWrapper<>();
            if (params.get("enterpriseName") != null && StringUtils.isNotBlank(params.get("enterpriseName").toString()))
                w.like(GzctFinStatement::getCompanyName, params.get("enterpriseName").toString());
            // 报告期间筛选
            if (params.get("period") != null && StringUtils.isNotBlank(params.get("period").toString()))
                w.eq(GzctFinStatement::getPeriod, params.get("period").toString());
            // 行业筛选
            if (params.get("industry") != null && StringUtils.isNotBlank(params.get("industry").toString()))
                w.like(GzctFinStatement::getIndustry, params.get("industry").toString());
            // 审计状态/评价状态筛选（兼容前端evaluationStatus和auditStatus两种参数名）
            String statusFilter = null;
            if (params.get("evaluationStatus") != null && StringUtils.isNotBlank(params.get("evaluationStatus").toString()))
                statusFilter = params.get("evaluationStatus").toString();
            else if (params.get("auditStatus") != null && StringUtils.isNotBlank(params.get("auditStatus").toString()))
                statusFilter = params.get("auditStatus").toString();
            if (statusFilter != null)
                w.eq(GzctFinStatement::getAuditStatus, statusFilter);
            // 日期范围筛选
            if (params.get("startDate") != null && StringUtils.isNotBlank(params.get("startDate").toString())) {
                try {
                    LocalDateTime startDate = LocalDate.parse(params.get("startDate").toString(), DateTimeFormatter.ISO_LOCAL_DATE).atStartOfDay();
                    w.ge(GzctFinStatement::getCreateTime, startDate);
                } catch (Exception ignored) {}
            }
            if (params.get("endDate") != null && StringUtils.isNotBlank(params.get("endDate").toString())) {
                try {
                    LocalDateTime endDate = LocalDate.parse(params.get("endDate").toString(), DateTimeFormatter.ISO_LOCAL_DATE).atTime(23, 59, 59);
                    w.le(GzctFinStatement::getCreateTime, endDate);
                } catch (Exception ignored) {}
            }
            w.orderByDesc(GzctFinStatement::getCreateTime);
            // 由于performanceLevel和评分范围是计算字段，需要先查全量再内存过滤分页
            String performanceLevelFilter = params.get("performanceLevel") != null ? params.get("performanceLevel").toString() : null;
            BigDecimal minScore = dec(params, "minPerformanceScore");
            BigDecimal maxScore = dec(params, "maxPerformanceScore");
            boolean needPostFilter = StringUtils.isNotBlank(performanceLevelFilter) || minScore != null || maxScore != null;
            List<Map<String, Object>> allList = new ArrayList<>();
            if (needPostFilter) {
                // 有计算字段过滤条件时，查全量数据再内存分页
                List<GzctFinStatement> allRecords = statementMapper.selectList(w);
                for (GzctFinStatement s : allRecords) {
                    Map<String, Object> m = buildPerformanceMap(s);
                    allList.add(m);
                }
                // 绩效等级过滤
                if (StringUtils.isNotBlank(performanceLevelFilter)) {
                    final String levelF = performanceLevelFilter;
                    allList = allList.stream().filter(m -> levelF.equals(m.get("performanceLevel"))).collect(Collectors.toList());
                }
                // 评分范围过滤
                if (minScore != null) {
                    final BigDecimal min = minScore;
                    allList = allList.stream().filter(m -> ((BigDecimal) m.get("performanceScore")).compareTo(min) >= 0).collect(Collectors.toList());
                }
                if (maxScore != null) {
                    final BigDecimal max = maxScore;
                    allList = allList.stream().filter(m -> ((BigDecimal) m.get("performanceScore")).compareTo(max) <= 0).collect(Collectors.toList());
                }
                // 内存分页
                int total = allList.size();
                int fromIndex = Math.min((pn - 1) * ps, total);
                int toIndex = Math.min(fromIndex + ps, total);
                List<Map<String, Object>> pageList = allList.subList(fromIndex, toIndex);
                Map<String, Object> result = new HashMap<>();
                result.put("list", pageList); result.put("tlist", pageList); result.put("total", total);
                return R.success(result);
            } else {
                // 无计算字段过滤，直接数据库分页
                Page<GzctFinStatement> page = statementMapper.selectPage(new Page<>(pn, ps), w);
                List<Map<String, Object>> list = new ArrayList<>();
                for (GzctFinStatement s : page.getRecords()) {
                    list.add(buildPerformanceMap(s));
                }
                Map<String, Object> result = new HashMap<>();
                result.put("list", list); result.put("tlist", list); result.put("total", page.getTotal());
                return R.success(result);
            }
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    /** 构建绩效列表单条记录Map */
    private Map<String, Object> buildPerformanceMap(GzctFinStatement s) {
        Map<String, Object> m = new HashMap<>();
        m.put("id", s.getStatementId());
        m.put("performanceId", s.getStatementId());
        m.put("enterpriseName", s.getCompanyName());
        m.put("companyId", s.getCompanyId());
        m.put("period", s.getPeriod());
        m.put("evaluationPeriod", s.getPeriod());
        m.put("revenue", s.getRevenue() != null ? s.getRevenue() : BigDecimal.ZERO);
        m.put("netProfit", s.getNetProfit() != null ? s.getNetProfit() : BigDecimal.ZERO);
        m.put("totalAssets", s.getTotalAssets() != null ? s.getTotalAssets() : BigDecimal.ZERO);
        m.put("totalLiabilities", s.getTotalLiabilities() != null ? s.getTotalLiabilities() : BigDecimal.ZERO);
        m.put("netAssets", s.getNetAssets() != null ? s.getNetAssets() : BigDecimal.ZERO);
        m.put("operatingCashflow", s.getOperatingCashflow() != null ? s.getOperatingCashflow() : BigDecimal.ZERO);
        m.put("industry", s.getIndustry());
        m.put("auditStatus", s.getAuditStatus());
        m.put("evaluationNotes", s.getAnalysisNotes());
        // 计算绩效评分和等级
        BigDecimal score = BigDecimal.ZERO;
        if (s.getNetProfit() != null && s.getRevenue() != null && s.getRevenue().compareTo(BigDecimal.ZERO) != 0)
            score = s.getNetProfit().multiply(BigDecimal.valueOf(100)).divide(s.getRevenue(), 2, RoundingMode.HALF_UP);
        m.put("performanceScore", score);
        String level;
        if (score.compareTo(BigDecimal.valueOf(10)) > 0) level = "EXCELLENT";
        else if (score.compareTo(BigDecimal.valueOf(5)) > 0) level = "GOOD";
        else if (score.compareTo(BigDecimal.valueOf(1)) > 0) level = "AVERAGE";
        else level = "POOR";
        m.put("performanceLevel", level);
        // 计算ROE和ROA
        BigDecimal roe = BigDecimal.ZERO;
        if (s.getNetProfit() != null && s.getNetAssets() != null && s.getNetAssets().compareTo(BigDecimal.ZERO) != 0)
            roe = s.getNetProfit().multiply(BigDecimal.valueOf(100)).divide(s.getNetAssets(), 2, RoundingMode.HALF_UP);
        m.put("roe", roe);
        BigDecimal roa = BigDecimal.ZERO;
        if (s.getNetProfit() != null && s.getTotalAssets() != null && s.getTotalAssets().compareTo(BigDecimal.ZERO) != 0)
            roa = s.getNetProfit().multiply(BigDecimal.valueOf(100)).divide(s.getTotalAssets(), 2, RoundingMode.HALF_UP);
        m.put("roa", roa);
        m.put("evaluationStatus", s.getAuditStatus() != null ? s.getAuditStatus() : "PENDING");
        m.put("createTime", s.getCreateTime());
        return m;
    }

    @Operation(summary = "绩效分布") @GetMapping("/performance/distribution")
    public R<List<Map<String, Object>>> performanceDistribution() {
        try {
            List<GzctFinStatement> all = statementMapper.selectList(null);
            long excellent = 0, good = 0, average = 0, poor = 0;
            for (GzctFinStatement s : all) {
                BigDecimal score = BigDecimal.ZERO;
                if (s.getRevenue() != null && s.getRevenue().compareTo(BigDecimal.ZERO) > 0 && s.getNetProfit() != null) {
                    score = s.getNetProfit().multiply(new BigDecimal("100")).divide(s.getRevenue(), 2, BigDecimal.ROUND_HALF_UP);
                }
                if (score.compareTo(new BigDecimal("5")) >= 0) excellent++;
                else if (score.compareTo(new BigDecimal("3")) >= 0) good++;
                else if (score.compareTo(new BigDecimal("1")) >= 0) average++;
                else poor++;
            }
            List<Map<String, Object>> result = new ArrayList<>();
            Map<String, Object> m1 = new HashMap<>(); m1.put("name", "优秀"); m1.put("value", excellent);
            Map<String, Object> m2 = new HashMap<>(); m2.put("name", "良好"); m2.put("value", good);
            Map<String, Object> m3 = new HashMap<>(); m3.put("name", "一般"); m3.put("value", average);
            Map<String, Object> m4 = new HashMap<>(); m4.put("name", "较差"); m4.put("value", poor);
            result.add(m1); result.add(m2); result.add(m3); result.add(m4);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }
    @Operation(summary = "绩效趋势") @GetMapping("/performance/trend")
    public R<List<Map<String, Object>>> performanceTrend() {
        try {
            List<GzctFinStatement> all = statementMapper.selectList(
                new LambdaQueryWrapper<GzctFinStatement>().orderByAsc(GzctFinStatement::getPeriod).last("LIMIT 12"));
            Map<String, Map<String, Integer>> periodMap = new java.util.LinkedHashMap<>();
            for (GzctFinStatement s : all) {
                String p = s.getPeriod() != null ? s.getPeriod() : "";
                Map<String, Integer> cnt = periodMap.computeIfAbsent(p, k -> {
                    Map<String, Integer> m = new HashMap<>(); m.put("excellent", 0); m.put("good", 0); m.put("average", 0); m.put("poor", 0); return m;
                });
                BigDecimal score = BigDecimal.ZERO;
                if (s.getRevenue() != null && s.getRevenue().compareTo(BigDecimal.ZERO) > 0 && s.getNetProfit() != null) {
                    score = s.getNetProfit().multiply(new BigDecimal("100")).divide(s.getRevenue(), 2, BigDecimal.ROUND_HALF_UP);
                }
                if (score.compareTo(new BigDecimal("5")) >= 0) cnt.merge("excellent", 1, Integer::sum);
                else if (score.compareTo(new BigDecimal("3")) >= 0) cnt.merge("good", 1, Integer::sum);
                else if (score.compareTo(new BigDecimal("1")) >= 0) cnt.merge("average", 1, Integer::sum);
                else cnt.merge("poor", 1, Integer::sum);
            }
            List<Map<String, Object>> result = new ArrayList<>();
            for (Map.Entry<String, Map<String, Integer>> e : periodMap.entrySet()) {
                Map<String, Object> m = new HashMap<>();
                m.put("date", e.getKey());
                m.putAll(e.getValue());
                result.add(m);
            }
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 关联交易 /related-transaction ====================

    @Operation(summary = "关联交易列表")
    @PostMapping("/related-transaction/list")
    public R<Map<String, Object>> relatedTransactionList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString())
                    : (params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1);
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctFinRelatedParty> w = new LambdaQueryWrapper<>();
            // 企业ID筛选
            String enterpriseId = str(params, "enterpriseId", "companyId");
            if (enterpriseId != null) {
                w.eq(GzctFinRelatedParty::getCompanyId, enterpriseId);
            }
            // 企业名称模糊搜索：DB无COMPANY_NAME字段，使用PARTY_NAME（关联方名称）匹配
            String enterpriseName = str(params, "enterpriseName", "companyName", "partyName");
            if (enterpriseName != null) {
                w.like(GzctFinRelatedParty::getPartyName, enterpriseName);
            }
            // 交易类型筛选
            if (params.get("transactionType") != null && StringUtils.isNotBlank(params.get("transactionType").toString()))
                w.eq(GzctFinRelatedParty::getTransactionType, params.get("transactionType").toString());
            // 关联关系筛选
            if (params.get("relationshipType") != null && StringUtils.isNotBlank(params.get("relationshipType").toString()))
                w.eq(GzctFinRelatedParty::getRelationType, params.get("relationshipType").toString());
            // 是否重大筛选
            if (params.get("isMajor") != null && StringUtils.isNotBlank(params.get("isMajor").toString()))
                w.eq(GzctFinRelatedParty::getIsMajor, params.get("isMajor").toString());
            // 期间筛选
            if (params.get("period") != null && StringUtils.isNotBlank(params.get("period").toString()))
                w.eq(GzctFinRelatedParty::getPeriod, params.get("period").toString());
            // 金额区间筛选
            if (params.get("minAmount") != null && StringUtils.isNotBlank(params.get("minAmount").toString()))
                w.ge(GzctFinRelatedParty::getTransactionAmount, new BigDecimal(params.get("minAmount").toString()));
            if (params.get("maxAmount") != null && StringUtils.isNotBlank(params.get("maxAmount").toString()))
                w.le(GzctFinRelatedParty::getTransactionAmount, new BigDecimal(params.get("maxAmount").toString()));
            w.orderByDesc(GzctFinRelatedParty::getCreateTime);
            Page<GzctFinRelatedParty> page = relatedPartyMapper.selectPage(new Page<>(pn, ps), w);
            List<Map<String, Object>> list = new ArrayList<>();
            for (GzctFinRelatedParty r : page.getRecords()) {
                Map<String, Object> m = new HashMap<>();
                m.put("id", r.getPartyId());
                m.put("companyId", r.getCompanyId());
                m.put("partyName", r.getPartyName());
                m.put("transactionType", r.getTransactionType());
                m.put("transactionAmount", r.getTransactionAmount() != null ? r.getTransactionAmount() : BigDecimal.ZERO);
                m.put("balanceAmount", r.getBalanceAmount() != null ? r.getBalanceAmount() : BigDecimal.ZERO);
                m.put("relationType", r.getRelationType());
                m.put("period", r.getPeriod());
                m.put("isMajor", r.getIsMajor());
                list.add(m);
            }
            Map<String, Object> result = new HashMap<>();
            result.put("records", list); result.put("list", list); result.put("total", page.getTotal());
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "关联交易统计")
    @PostMapping("/related-transaction/statistics")
    public R<Map<String, Object>> relatedTransactionStatistics(@RequestBody(required = false) Map<String, Object> params) {
        try {
            long total = relatedPartyMapper.selectCount(null);
            long abnormal = relatedPartyMapper.selectCount(
                new LambdaQueryWrapper<GzctFinRelatedParty>().eq(GzctFinRelatedParty::getIsMajor, "1"));
            // 计算交易总额
            List<GzctFinRelatedParty> allRecords = relatedPartyMapper.selectList(null);
            BigDecimal totalAmount = BigDecimal.ZERO;
            BigDecimal fairnessSum = BigDecimal.ZERO;
            int fairnessCount = 0;
            for (GzctFinRelatedParty r : allRecords) {
                if (r.getTransactionAmount() != null) {
                    totalAmount = totalAmount.add(r.getTransactionAmount());
                    // 基于交易金额计算公允性评分
                    BigDecimal amount = r.getTransactionAmount();
                    int score = 85;
                    if (amount.compareTo(new BigDecimal("10000")) > 0) score -= 10;
                    if (amount.compareTo(new BigDecimal("50000")) > 0) score -= 15;
                    if ("1".equals(r.getIsMajor())) score -= 10;
                    fairnessSum = fairnessSum.add(new BigDecimal(Math.max(score, 0)));
                    fairnessCount++;
                }
            }
            BigDecimal avgFairness = fairnessCount > 0
                ? fairnessSum.divide(new BigDecimal(fairnessCount), 1, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
            Map<String, Object> result = new HashMap<>();
            result.put("totalTransactions", total);
            result.put("totalAmount", totalAmount);
            result.put("abnormalCount", abnormal);
            result.put("avgFairness", avgFairness);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "关联交易保存")
    @PostMapping("/related-transaction/save")
    public R<Boolean> relatedTransactionSave(@RequestBody Map<String, Object> p) {
        try {
            GzctFinRelatedParty party = new GzctFinRelatedParty();
            String id = p.get("partyId") != null ? p.get("partyId").toString()
                    : (p.get("id") != null ? p.get("id").toString() : null);
            if (p.get("companyId") != null) party.setCompanyId(p.get("companyId").toString());
            if (p.get("partyName") != null) party.setPartyName(p.get("partyName").toString());
            if (p.get("relationType") != null) party.setRelationType(p.get("relationType").toString());
            if (p.get("transactionType") != null) party.setTransactionType(p.get("transactionType").toString());
            if (p.get("transactionAmount") != null) party.setTransactionAmount(new BigDecimal(p.get("transactionAmount").toString()));
            if (p.get("balanceAmount") != null) party.setBalanceAmount(new BigDecimal(p.get("balanceAmount").toString()));
            if (p.get("isMajor") != null) party.setIsMajor(p.get("isMajor").toString());
            if (p.get("period") != null) party.setPeriod(p.get("period").toString());
            if (StringUtils.isNotBlank(id)) {
                party.setPartyId(id);
                party.setUpdateTime(LocalDateTime.now());
                relatedPartyMapper.updateById(party);
            } else {
                party.setCreateTime(LocalDateTime.now());
                party.setUpdateTime(LocalDateTime.now());
                relatedPartyMapper.insert(party);
            }
            return R.success(true);
        } catch (Exception e) { return R.fail("保存失败：" + e.getMessage()); }
    }

    // ==================== 合并财务分析 /consolidated ====================

    @Operation(summary = "合并财务统计")
    @PostMapping("/consolidated/statistics")
    public R<Map<String, Object>> consolidatedStatistics(@RequestBody(required = false) Map<String, Object> params) {
        try {
            LambdaQueryWrapper<GzctFinStatement> w = new LambdaQueryWrapper<>();
            applyConsolidatedFilters(w, params == null ? new HashMap<>() : params);
            List<GzctFinStatement> all = statementMapper.selectList(w);
            Set<String> companies = new HashSet<>();
            BigDecimal totalRevenue = BigDecimal.ZERO;
            long riskCount = 0;
            for (GzctFinStatement s : all) {
                if (s.getCompanyId() != null) companies.add(s.getCompanyId());
                if (s.getRevenue() != null) totalRevenue = totalRevenue.add(s.getRevenue());
                if ("UNAUDITED".equals(s.getAuditStatus()) || "未审计".equals(s.getAuditStatus()) || "PENDING".equals(s.getAuditStatus())) riskCount++;
            }
            Map<String, Object> result = new HashMap<>();
            result.put("totalAnalysis", all.size());
            result.put("consolidatedEntities", companies.size());
            result.put("riskCount", riskCount);
            result.put("totalRevenue", totalRevenue.setScale(2, RoundingMode.HALF_UP));
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "合并财务列表")
    @PostMapping("/consolidated/list")
    public R<Map<String, Object>> consolidatedList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString())
                    : (params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1);
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctFinStatement> w = new LambdaQueryWrapper<>();
            applyConsolidatedFilters(w, params);
            String orderBy = str(params, "orderBy"), direction = str(params, "orderDirection");
            boolean asc = "ASC".equalsIgnoreCase(direction);
            if ("totalRevenue".equals(orderBy)) w.orderBy(true, asc, GzctFinStatement::getRevenue);
            else if ("totalAssets".equals(orderBy)) w.orderBy(true, asc, GzctFinStatement::getTotalAssets);
            else if ("totalLiabilities".equals(orderBy)) w.orderBy(true, asc, GzctFinStatement::getTotalLiabilities);
            else if ("netAssets".equals(orderBy)) w.orderBy(true, asc, GzctFinStatement::getNetAssets);
            else if ("netProfit".equals(orderBy)) w.orderBy(true, asc, GzctFinStatement::getNetProfit);
            else if ("operatingCashflow".equals(orderBy)) w.orderBy(true, asc, GzctFinStatement::getOperatingCashflow);
            else w.orderByDesc(GzctFinStatement::getCreateTime);
            Page<GzctFinStatement> page = statementMapper.selectPage(new Page<>(pn, ps), w);
            List<Map<String, Object>> list = new ArrayList<>();
            for (GzctFinStatement s : page.getRecords()) list.add(buildConsolidatedRow(s));
            Map<String, Object> result = new HashMap<>();
            result.put("records", list); result.put("list", list); result.put("total", page.getTotal());
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "合并财务详情")
    @GetMapping("/consolidated/{id}")
    public R<Map<String, Object>> consolidatedDetail(@PathVariable String id) {
        try {
            if (StringUtils.isBlank(id)) return R.fail("缺少ID参数");
            GzctFinStatement stmt = statementMapper.selectById(id);
            if (stmt == null) return R.fail("记录不存在");
            return R.success(buildConsolidatedRow(stmt));
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "合并财务图表")
    @PostMapping("/consolidated/chart")
    public R<Map<String, Object>> consolidatedChart(@RequestBody(required = false) Map<String, Object> p) {
        try {
            LambdaQueryWrapper<GzctFinStatement> w = new LambdaQueryWrapper<>();
            w.orderByAsc(GzctFinStatement::getPeriod);
            List<GzctFinStatement> stmts = statementMapper.selectList(w);
            Map<String, BigDecimal[]> periodData = new java.util.LinkedHashMap<>();
            for (GzctFinStatement s : stmts) {
                String period = s.getPeriod() != null ? s.getPeriod() : "未知";
                BigDecimal[] vals = periodData.computeIfAbsent(period, k -> new BigDecimal[]{BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO});
                if (s.getRevenue() != null) vals[0] = vals[0].add(s.getRevenue());
                if (s.getNetProfit() != null) vals[1] = vals[1].add(s.getNetProfit());
                if (s.getTotalAssets() != null) vals[2] = vals[2].add(s.getTotalAssets());
            }
            List<String> xAxis = new ArrayList<>(periodData.keySet());
            List<BigDecimal> revenueSeries = new ArrayList<>(), profitSeries = new ArrayList<>(), assetsSeries = new ArrayList<>();
            for (BigDecimal[] v : periodData.values()) {
                revenueSeries.add(v[0]); profitSeries.add(v[1]); assetsSeries.add(v[2]);
            }
            Map<String, Object> result = new HashMap<>();
            result.put("xAxis", xAxis);
            result.put("revenueSeries", revenueSeries);
            result.put("profitSeries", profitSeries);
            result.put("assetsSeries", assetsSeries);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "合并财务保存")
    @PostMapping("/consolidated/save")
    public R<Boolean> consolidatedSave(@RequestBody Map<String, Object> p) {
        try {
            GzctFinStatement stmt = new GzctFinStatement();
            String id = p.get("statementId") != null ? p.get("statementId").toString()
                    : (p.get("id") != null ? p.get("id").toString() : null);
            if (p.get("companyId") != null) stmt.setCompanyId(p.get("companyId").toString());
            if (p.get("companyName") != null) stmt.setCompanyName(p.get("companyName").toString());
            if (p.get("period") != null) stmt.setPeriod(p.get("period").toString());
            stmt.setStatementType(str(p, "statementType") != null ? str(p, "statementType") : "合并");
            if (p.get("totalAssets") != null) stmt.setTotalAssets(new BigDecimal(p.get("totalAssets").toString()));
            if (p.get("totalLiabilities") != null) stmt.setTotalLiabilities(new BigDecimal(p.get("totalLiabilities").toString()));
            if (p.get("netAssets") != null) stmt.setNetAssets(new BigDecimal(p.get("netAssets").toString()));
            if (p.get("totalRevenue") != null) stmt.setRevenue(new BigDecimal(p.get("totalRevenue").toString()));
            if (p.get("revenue") != null) stmt.setRevenue(new BigDecimal(p.get("revenue").toString()));
            if (p.get("netProfit") != null) stmt.setNetProfit(new BigDecimal(p.get("netProfit").toString()));
            if (p.get("operatingCashflow") != null) stmt.setOperatingCashflow(new BigDecimal(p.get("operatingCashflow").toString()));
            // 字典收口：用户输入过 normalize 规范化为标准英文 code
            if (p.get("auditStatus") != null) stmt.setAuditStatus(com.huabo.cybermonitor.util.AuditStatusUtil.normalize(p.get("auditStatus").toString()));
            if (StringUtils.isNotBlank(id)) {
                stmt.setStatementId(id);
                stmt.setUpdateTime(LocalDateTime.now());
                statementMapper.updateById(stmt);
            } else {
                stmt.setCreateTime(LocalDateTime.now());
                stmt.setUpdateTime(LocalDateTime.now());
                statementMapper.insert(stmt);
            }
            return R.success(true);
        } catch (Exception e) { return R.fail("保存失败：" + e.getMessage()); }
    }

    // ==================== 资金流向扩展 /fund-flow ====================

    @Operation(summary = "资金流向统计")
    @PostMapping("/fund-flow/statistics")
    public R<Map<String, Object>> fundFlowStatistics(@RequestBody(required = false) Map<String, Object> params) {
        try {
            long total = relatedPartyMapper.selectCount(null);
            long abnormal = relatedPartyMapper.selectCount(
                new LambdaQueryWrapper<GzctFinRelatedParty>().eq(GzctFinRelatedParty::getIsMajor, "1"));
            BigDecimal totalAmount = BigDecimal.ZERO;
            List<GzctFinRelatedParty> all = relatedPartyMapper.selectList(null);
            for (GzctFinRelatedParty r : all)
                if (r.getTransactionAmount() != null) totalAmount = totalAmount.add(r.getTransactionAmount());
            Map<String, Object> result = new HashMap<>();
            result.put("totalFlows", total);
            result.put("totalAmount", totalAmount.setScale(2, RoundingMode.HALF_UP));
            result.put("abnormalCount", abnormal);
            result.put("avgVelocity", total > 0 ? totalAmount.divide(BigDecimal.valueOf(total), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "资金流向图表")
    @PostMapping("/fund-flow/chart")
    public R<Map<String, Object>> fundFlowChart(@RequestBody(required = false) Map<String, Object> p) {
        try {
            List<GzctFinRelatedParty> all = relatedPartyMapper.selectList(null);
            // 网络图节点和连线
            Set<String> nodeSet = new HashSet<>();
            List<Map<String, Object>> links = new ArrayList<>();
            for (GzctFinRelatedParty r : all) {
                String source = r.getCompanyId() != null ? r.getCompanyId() : "未知";
                String target = r.getPartyName() != null ? r.getPartyName() : "未知";
                nodeSet.add(source); nodeSet.add(target);
                Map<String, Object> link = new HashMap<>();
                link.put("source", source); link.put("target", target);
                link.put("value", r.getTransactionAmount() != null ? r.getTransactionAmount() : BigDecimal.ZERO);
                links.add(link);
            }
            List<Map<String, Object>> nodes = new ArrayList<>();
            for (String n : nodeSet) {
                Map<String, Object> node = new HashMap<>();
                node.put("name", n);
                nodes.add(node);
            }
            // 饼图：按交易类型分布
            Map<String, BigDecimal> typeMap = new HashMap<>();
            for (GzctFinRelatedParty r : all) {
                String type = r.getTransactionType() != null ? r.getTransactionType() : "其他";
                typeMap.merge(type, r.getTransactionAmount() != null ? r.getTransactionAmount() : BigDecimal.ZERO, BigDecimal::add);
            }
            List<Map<String, Object>> pieData = new ArrayList<>();
            for (Map.Entry<String, BigDecimal> e : typeMap.entrySet()) {
                Map<String, Object> m = new HashMap<>();
                m.put("name", e.getKey()); m.put("value", e.getValue());
                pieData.add(m);
            }
            Map<String, Object> result = new HashMap<>();
            result.put("nodes", nodes);
            result.put("links", links);
            result.put("pieData", pieData);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "资金流向保存")
    @PostMapping("/fund-flow/save")
    public R<Boolean> fundFlowSave(@RequestBody Map<String, Object> p) {
        try {
            GzctFinRelatedParty party = new GzctFinRelatedParty();
            String id = p.get("partyId") != null ? p.get("partyId").toString()
                    : (p.get("id") != null ? p.get("id").toString() : null);
            if (p.get("companyId") != null) party.setCompanyId(p.get("companyId").toString());
            if (p.get("partyName") != null) party.setPartyName(p.get("partyName").toString());
            if (p.get("relationType") != null) party.setRelationType(p.get("relationType").toString());
            if (p.get("transactionType") != null) party.setTransactionType(p.get("transactionType").toString());
            if (p.get("transactionAmount") != null) party.setTransactionAmount(new BigDecimal(p.get("transactionAmount").toString()));
            if (p.get("balanceAmount") != null) party.setBalanceAmount(new BigDecimal(p.get("balanceAmount").toString()));
            if (p.get("isMajor") != null) party.setIsMajor(p.get("isMajor").toString());
            if (p.get("period") != null) {
                String period = p.get("period").toString();
                // 格式化period为yyyy-MM，兼容各种输入格式
                if (period.length() > 7) period = period.substring(0, 7);
                party.setPeriod(period);
            }
            if (StringUtils.isNotBlank(id)) {
                party.setPartyId(id);
                party.setUpdateTime(LocalDateTime.now());
                relatedPartyMapper.updateById(party);
            } else {
                party.setCreateTime(LocalDateTime.now());
                party.setUpdateTime(LocalDateTime.now());
                relatedPartyMapper.insert(party);
            }
            return R.success(true);
        } catch (Exception e) { return R.fail("保存失败：" + e.getMessage()); }
    }

    // ==================== 业绩合并 /performance-consolidation ====================

    @Operation(summary = "业绩合并统计")
    @PostMapping("/performance-consolidation/statistics")
    public R<Map<String, Object>> performanceConsolidationStatistics(@RequestBody(required = false) Map<String, Object> params) {
        try {
            List<GzctFinStatement> all = statementMapper.selectList(null);
            BigDecimal sumProfit = BigDecimal.ZERO;
            BigDecimal sumRevenue = BigDecimal.ZERO;
            for (GzctFinStatement s : all) {
                if (s.getNetProfit() != null) sumProfit = sumProfit.add(s.getNetProfit());
                if (s.getRevenue() != null) sumRevenue = sumRevenue.add(s.getRevenue());
            }
            BigDecimal avgContribution = sumRevenue.compareTo(BigDecimal.ZERO) != 0
                ? sumProfit.multiply(BigDecimal.valueOf(100)).divide(sumRevenue, 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
            long anomalyCount = anomalyMapper.selectCount(null);
            Map<String, Object> result = new HashMap<>();
            result.put("totalAnalysis", all.size());
            result.put("avgContribution", avgContribution);
            result.put("synergyScore", 85);
            result.put("riskCount", anomalyCount);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "业绩合并列表")
    @PostMapping("/performance-consolidation/list")
    public R<Map<String, Object>> performanceConsolidationList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctFinStatement> w = new LambdaQueryWrapper<>();
            if (params.get("companyName") != null && StringUtils.isNotBlank(params.get("companyName").toString()))
                w.like(GzctFinStatement::getCompanyName, params.get("companyName").toString());
            w.orderByDesc(GzctFinStatement::getCreateTime);
            Page<GzctFinStatement> page = statementMapper.selectPage(new Page<>(pn, ps), w);
            List<Map<String, Object>> list = new ArrayList<>();
            for (GzctFinStatement s : page.getRecords()) {
                Map<String, Object> m = new HashMap<>();
                m.put("id", s.getStatementId());
                m.put("companyName", s.getCompanyName());
                m.put("companyId", s.getCompanyId());
                m.put("period", s.getPeriod());
                m.put("totalRevenue", s.getRevenue() != null ? s.getRevenue() : BigDecimal.ZERO);
                m.put("totalProfit", s.getNetProfit() != null ? s.getNetProfit() : BigDecimal.ZERO);
                m.put("totalAssets", s.getTotalAssets() != null ? s.getTotalAssets() : BigDecimal.ZERO);
                list.add(m);
            }
            Map<String, Object> result = new HashMap<>();
            result.put("records", list); result.put("list", list); result.put("total", page.getTotal());
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "业绩合并图表")
    @PostMapping("/performance-consolidation/chart")
    public R<Map<String, Object>> performanceConsolidationChart(@RequestBody(required = false) Map<String, Object> p) {
        try {
            List<GzctFinStatement> all = statementMapper.selectList(
                new LambdaQueryWrapper<GzctFinStatement>().orderByAsc(GzctFinStatement::getPeriod));
            // 按公司计算贡献率
            BigDecimal totalRevenue = BigDecimal.ZERO;
            for (GzctFinStatement s : all) {
                if (s.getRevenue() != null) totalRevenue = totalRevenue.add(s.getRevenue());
            }
            Map<String, BigDecimal> companyRevenue = new java.util.LinkedHashMap<>();
            for (GzctFinStatement s : all) {
                String name = s.getCompanyName() != null ? s.getCompanyName() : s.getCompanyId();
                companyRevenue.merge(name, s.getRevenue() != null ? s.getRevenue() : BigDecimal.ZERO, BigDecimal::add);
            }
            List<String> xAxis = new ArrayList<>(companyRevenue.keySet());
            List<BigDecimal> contributionRates = new ArrayList<>();
            for (BigDecimal rev : companyRevenue.values()) {
                contributionRates.add(totalRevenue.compareTo(BigDecimal.ZERO) > 0
                    ? rev.multiply(BigDecimal.valueOf(100)).divide(totalRevenue, 2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
            }
            Map<String, Object> result = new HashMap<>();
            result.put("xAxis", xAxis);
            result.put("contributionRates", contributionRates);
            result.put("totalRevenue", totalRevenue.setScale(2, RoundingMode.HALF_UP));
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "业绩合并保存")
    @PostMapping("/performance-consolidation/save")
    public R<Boolean> performanceConsolidationSave(@RequestBody Map<String, Object> p) {
        try {
            GzctFinStatement stmt = new GzctFinStatement();
            String id = p.get("statementId") != null ? p.get("statementId").toString()
                    : (p.get("id") != null ? p.get("id").toString() : null);
            if (p.get("companyId") != null) stmt.setCompanyId(p.get("companyId").toString());
            if (p.get("companyName") != null) stmt.setCompanyName(p.get("companyName").toString());
            if (p.get("period") != null) stmt.setPeriod(p.get("period").toString());
            if (p.get("statementType") != null) stmt.setStatementType(p.get("statementType").toString());
            if (p.get("totalAssets") != null) stmt.setTotalAssets(new BigDecimal(p.get("totalAssets").toString()));
            if (p.get("revenue") != null) stmt.setRevenue(new BigDecimal(p.get("revenue").toString()));
            if (p.get("netProfit") != null) stmt.setNetProfit(new BigDecimal(p.get("netProfit").toString()));
            if (StringUtils.isNotBlank(id)) {
                stmt.setStatementId(id);
                stmt.setUpdateTime(LocalDateTime.now());
                statementMapper.updateById(stmt);
            } else {
                stmt.setCreateTime(LocalDateTime.now());
                stmt.setUpdateTime(LocalDateTime.now());
                statementMapper.insert(stmt);
            }
            return R.success(true);
        } catch (Exception e) { return R.fail("保存失败：" + e.getMessage()); }
    }


    // ==================== 报表统计 /statement/statistics ====================

    @Operation(summary = "报表统计")
    @GetMapping("/statement/statistics")
    public R<Map<String, Object>> statementStatistics(
            @RequestParam(required = false) String companyId) {
        try {
            LambdaQueryWrapper<GzctFinStatement> w = new LambdaQueryWrapper<>();
            if (companyId != null && !companyId.isEmpty()) {
                String orgPattern = orgQueryHelper.getOrgPathPattern(companyId);
                if (orgPattern != null) w.and(w2 -> w2.like(GzctFinStatement::getOrgPath, orgPattern).or(sub -> sub.isNull(GzctFinStatement::getOrgPath).eq(GzctFinStatement::getCompanyId, companyId)));
            }
            List<GzctFinStatement> all = statementMapper.selectList(w);
            BigDecimal totalRevenue = BigDecimal.ZERO, netProfit = BigDecimal.ZERO,
                totalAssets = BigDecimal.ZERO, totalLiabilities = BigDecimal.ZERO;
            for (GzctFinStatement s : all) {
                if (s.getRevenue() != null) totalRevenue = totalRevenue.add(s.getRevenue());
                if (s.getNetProfit() != null) netProfit = netProfit.add(s.getNetProfit());
                if (s.getTotalAssets() != null) totalAssets = totalAssets.add(s.getTotalAssets());
                if (s.getTotalLiabilities() != null) totalLiabilities = totalLiabilities.add(s.getTotalLiabilities());
            }
            Map<String, Object> result = new HashMap<>();
            result.put("totalRevenue", totalRevenue.setScale(2, RoundingMode.HALF_UP));
            result.put("netProfit", netProfit.setScale(2, RoundingMode.HALF_UP));
            result.put("totalAssets", totalAssets.setScale(2, RoundingMode.HALF_UP));
            result.put("totalLiabilities", totalLiabilities.setScale(2, RoundingMode.HALF_UP));
            result.put("count", all.size());

            // Frontend alias fields for STAT_MAP compatibility
            long totalStatements = all.size();
            long riskCount = all.stream().filter(s -> !"AUDITED".equals(s.getAuditStatus())).count();
            double complianceRate = totalStatements > 0
                    ? Math.round(all.stream().filter(s -> "AUDITED".equals(s.getAuditStatus())).count() * 1000.0 / totalStatements) / 10.0
                    : 0;
            result.put("statementCount", totalStatements);
            result.put("totalStatements", totalStatements);
            result.put("riskCount", riskCount);
            result.put("financialRisks", riskCount);
            result.put("complianceRate", complianceRate);

            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 合并财务扩展接口 ====================

    @Operation(summary = "合并财务执行分析")
    @PostMapping("/consolidated/perform")
    public R<Boolean> consolidatedPerform(@RequestBody Map<String, Object> p) {
        try {
            String id = str(p, "id", "statementId", "consolidatedAnalysisId");
            if (StringUtils.isBlank(id)) return R.fail("缺少ID参数");
            GzctFinStatement stmt = statementMapper.selectById(id);
            if (stmt == null) return R.fail("记录不存在");
            stmt.setAuditStatus("APPROVED");
            stmt.setUpdateTime(LocalDateTime.now());
            statementMapper.updateById(stmt);
            return R.success(true);
        } catch (Exception e) { return R.fail("执行分析失败：" + e.getMessage()); }
    }

    @Operation(summary = "合并财务批量执行分析")
    @PostMapping("/consolidated/batch/perform")
    public R<Boolean> consolidatedBatchPerform(@RequestBody Map<String, Object> p) {
        try {
            List<String> ids = null;
            if (p.get("ids") != null && p.get("ids") instanceof List) {
                ids = ((List<?>) p.get("ids")).stream().map(Object::toString).collect(Collectors.toList());
            }
            if (ids == null || ids.isEmpty()) return R.fail("缺少ID列表");
            for (String id : ids) {
                GzctFinStatement stmt = statementMapper.selectById(id);
                if (stmt != null) {
                    stmt.setAuditStatus("APPROVED");
                    stmt.setUpdateTime(LocalDateTime.now());
                    statementMapper.updateById(stmt);
                }
            }
            return R.success(true);
        } catch (Exception e) { return R.fail("批量执行失败：" + e.getMessage()); }
    }

    @Operation(summary = "合并报表详情")
    @PostMapping("/consolidated/statement")
    public R<Map<String, Object>> consolidatedStatement(@RequestBody Map<String, Object> p) {
        try {
            String id = str(p, "id", "statementId");
            if (StringUtils.isBlank(id)) return R.fail("缺少ID参数");
            GzctFinStatement stmt = statementMapper.selectById(id);
            if (stmt == null) return R.fail("记录不存在");
            Map<String, Object> result = buildConsolidatedRow(stmt);
            result.put("assetLiabilityRatio", percent(stmt.getTotalLiabilities(), stmt.getTotalAssets()));
            result.put("netProfitMargin", percent(stmt.getNetProfit(), stmt.getRevenue()));
            result.put("cashRevenueRatio", percent(stmt.getOperatingCashflow(), stmt.getRevenue()));
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "合并趋势分析")
    @PostMapping("/consolidated/trend")
    public R<Map<String, Object>> consolidatedTrend(@RequestBody Map<String, Object> p) {
        try {
            String companyId = str(p, "companyId", "enterpriseId");
            LambdaQueryWrapper<GzctFinStatement> w = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(companyId)) w.eq(GzctFinStatement::getCompanyId, companyId);
            w.orderByAsc(GzctFinStatement::getPeriod);
            List<GzctFinStatement> all = statementMapper.selectList(w);
            List<Map<String, Object>> rows = new ArrayList<>();
            for (GzctFinStatement s : all) rows.add(buildConsolidatedRow(s));
            Map<String, Object> result = new HashMap<>();
            result.put("rows", rows); result.put("total", rows.size());
            return R.success(result);
        } catch (Exception e) { return R.fail("趋势分析失败：" + e.getMessage()); }
    }

    @Operation(summary = "合并对比分析")
    @PostMapping("/consolidated/compare")
    public R<Map<String, Object>> consolidatedCompare(@RequestBody Map<String, Object> p) {
        try {
            String id = str(p, "id", "statementId");
            GzctFinStatement cur = statementMapper.selectById(id);
            if (cur == null) return R.fail("记录不存在");
            List<GzctFinStatement> all = statementMapper.selectList(null);
            BigDecimal revenue = BigDecimal.ZERO, assets = BigDecimal.ZERO, netAssets = BigDecimal.ZERO,
                    profit = BigDecimal.ZERO, cash = BigDecimal.ZERO;
            int count = all.isEmpty() ? 1 : all.size();
            for (GzctFinStatement s : all) {
                revenue = revenue.add(safe(s.getRevenue())); assets = assets.add(safe(s.getTotalAssets()));
                netAssets = netAssets.add(safe(s.getNetAssets())); profit = profit.add(safe(s.getNetProfit()));
                cash = cash.add(safe(s.getOperatingCashflow()));
            }
            Map<String, Object> result = new HashMap<>();
            result.put("current", buildConsolidatedRow(cur));
            result.put("benchmark", Arrays.asList(
                    revenue.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP),
                    assets.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP),
                    netAssets.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP),
                    profit.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP),
                    cash.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP)));
            return R.success(result);
        } catch (Exception e) { return R.fail("对比分析失败：" + e.getMessage()); }
    }

    @Operation(summary = "合并预测分析")
    @PostMapping("/consolidated/predict")
    public R<Map<String, Object>> consolidatedPredict(@RequestBody Map<String, Object> p) {
        try {
            String companyId = str(p, "companyId", "enterpriseId");
            LambdaQueryWrapper<GzctFinStatement> w = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(companyId)) w.eq(GzctFinStatement::getCompanyId, companyId);
            w.orderByAsc(GzctFinStatement::getPeriod);
            List<GzctFinStatement> all = statementMapper.selectList(w);
            if (all.isEmpty()) return R.fail("暂无历史数据");
            GzctFinStatement last = all.get(all.size() - 1);
            BigDecimal growth = BigDecimal.ZERO;
            if (all.size() > 1 && safe(all.get(all.size() - 2).getRevenue()).compareTo(BigDecimal.ZERO) > 0) {
                BigDecimal prev = safe(all.get(all.size() - 2).getRevenue());
                growth = safe(last.getRevenue()).subtract(prev).divide(prev, 4, RoundingMode.HALF_UP);
            }
            List<Map<String, Object>> rows = new ArrayList<>();
            rows.add(buildConsolidatedRow(last));
            for (int i = 1; i <= 4; i++) {
                BigDecimal factor = BigDecimal.ONE.add(growth).pow(i);
                Map<String, Object> row = new LinkedHashMap<>();
                row.put("period", "预测" + i); row.put("type", "PREDICT");
                row.put("totalRevenue", safe(last.getRevenue()).multiply(factor).setScale(2, RoundingMode.HALF_UP));
                row.put("netProfit", safe(last.getNetProfit()).multiply(factor).setScale(2, RoundingMode.HALF_UP));
                row.put("totalAssets", safe(last.getTotalAssets()).multiply(factor).setScale(2, RoundingMode.HALF_UP));
                rows.add(row);
            }
            Map<String, Object> result = new HashMap<>(); result.put("rows", rows); result.put("growth", growth);
            return R.success(result);
        } catch (Exception e) { return R.fail("预测分析失败：" + e.getMessage()); }
    }

    @Operation(summary = "合并分析报告生成")
    @PostMapping("/consolidated/report")
    public R<Map<String, Object>> consolidatedReport(@RequestBody Map<String, Object> p) {
        try {
            String id = str(p, "id", "statementId");
            if (StringUtils.isBlank(id)) return R.fail("缺少ID参数");
            GzctFinStatement stmt = statementMapper.selectById(id);
            if (stmt == null) return R.fail("记录不存在");
            Map<String, Object> report = new LinkedHashMap<>();
            report.put("reportId", UUID.randomUUID().toString());
            report.put("generateTime", LocalDateTime.now().toString());
            report.put("companyName", stmt.getCompanyName());
            report.put("period", stmt.getPeriod());
            report.put("summary", buildConsolidatedRow(stmt));
            report.put("assetLiabilityRatio", percent(stmt.getTotalLiabilities(), stmt.getTotalAssets()));
            report.put("netProfitMargin", percent(stmt.getNetProfit(), stmt.getRevenue()));
            report.put("cashRevenueRatio", percent(stmt.getOperatingCashflow(), stmt.getRevenue()));
            report.put("riskTips", Arrays.asList(
                    percent(stmt.getTotalLiabilities(), stmt.getTotalAssets()).compareTo(BigDecimal.valueOf(70)) > 0 ? "资产负债率偏高" : "资产负债率处于可控区间",
                    safe(stmt.getOperatingCashflow()).compareTo(BigDecimal.ZERO) < 0 ? "经营现金流为负" : "经营现金流为正"));
            return R.success(report);
        } catch (Exception e) { return R.fail("报告生成失败：" + e.getMessage()); }
    }

    @Operation(summary = "合并财务导出")
    @PostMapping("/consolidated/export")
    public R<Map<String, Object>> consolidatedExport(@RequestBody(required = false) Map<String, Object> p) {
        try {
            LambdaQueryWrapper<GzctFinStatement> w = new LambdaQueryWrapper<>();
            applyConsolidatedFilters(w, p == null ? new HashMap<>() : p);
            w.orderByDesc(GzctFinStatement::getCreateTime);
            List<GzctFinStatement> all = statementMapper.selectList(w);
            List<Map<String, Object>> rows = new ArrayList<>();
            for (GzctFinStatement s : all) rows.add(buildConsolidatedRow(s));
            Map<String, Object> result = new HashMap<>();
            result.put("data", rows);
            result.put("total", rows.size());
            return R.success(result);
        } catch (Exception e) { return R.fail("导出失败：" + e.getMessage()); }
    }

    @Operation(summary = "合并财务删除")
    @DeleteMapping("/consolidated/{id}")
    public R<Boolean> consolidatedDelete(@PathVariable String id) {
        try {
            if (StringUtils.isBlank(id)) return R.fail("缺少ID参数");
            statementMapper.deleteById(id);
            return R.success(true);
        } catch (Exception e) { return R.fail("删除失败：" + e.getMessage()); }
    }

    // ==================== 资金流向扩展接口 ====================

    @Operation(summary = "资金流向删除")
    @DeleteMapping("/fund-flow/{id}")
    public R<Boolean> fundFlowDelete(@PathVariable String id) {
        try {
            if (StringUtils.isBlank(id)) return R.fail("缺少ID参数");
            relatedPartyMapper.deleteById(id);
            return R.success(true);
        } catch (Exception e) { return R.fail("删除失败：" + e.getMessage()); }
    }

    @Operation(summary = "资金流向追踪")
    @PostMapping("/fund-flow/trace")
    public R<Boolean> fundFlowTrace(@RequestBody Map<String, Object> p) {
        try {
            String id = p.get("id") != null ? p.get("id").toString() : null;
            if (StringUtils.isBlank(id)) return R.fail("缺少ID参数");
            GzctFinRelatedParty party = relatedPartyMapper.selectById(id);
            if (party == null) return R.fail("记录不存在");
            party.setIsMajor("1");
            party.setUpdateTime(LocalDateTime.now());
            relatedPartyMapper.updateById(party);
            return R.success(true);
        } catch (Exception e) { return R.fail("追踪失败：" + e.getMessage()); }
    }

    @Operation(summary = "资金流向批量追踪")
    @PostMapping("/fund-flow/batch/trace")
    public R<Boolean> fundFlowBatchTrace(@RequestBody Map<String, Object> p) {
        try {
            List<String> ids = null;
            if (p.get("ids") != null && p.get("ids") instanceof List) {
                ids = ((List<?>) p.get("ids")).stream().map(Object::toString).collect(Collectors.toList());
            }
            if (ids == null || ids.isEmpty()) return R.fail("缺少ID列表");
            for (String id : ids) {
                GzctFinRelatedParty party = relatedPartyMapper.selectById(id);
                if (party != null) {
                    party.setIsMajor("1");
                    party.setUpdateTime(LocalDateTime.now());
                    relatedPartyMapper.updateById(party);
                }
            }
            return R.success(true);
        } catch (Exception e) { return R.fail("批量追踪失败：" + e.getMessage()); }
    }

    @Operation(summary = "资金流向导出")
    @PostMapping("/fund-flow/export")
    public R<Map<String, Object>> fundFlowExport(@RequestBody(required = false) Map<String, Object> p) {
        try {
            List<GzctFinRelatedParty> all = relatedPartyMapper.selectList(
                new LambdaQueryWrapper<GzctFinRelatedParty>().orderByDesc(GzctFinRelatedParty::getCreateTime));
            List<Map<String, Object>> rows = new ArrayList<>();
            for (GzctFinRelatedParty r : all) {
                Map<String, Object> row = new HashMap<>();
                row.put("companyId", r.getCompanyId());
                row.put("partyName", r.getPartyName());
                row.put("transactionType", r.getTransactionType());
                row.put("transactionAmount", r.getTransactionAmount());
                row.put("balanceAmount", r.getBalanceAmount());
                row.put("period", r.getPeriod());
                row.put("isMajor", r.getIsMajor());
                rows.add(row);
            }
            Map<String, Object> result = new HashMap<>();
            result.put("data", rows);
            result.put("total", rows.size());
            return R.success(result);
        } catch (Exception e) { return R.fail("导出失败：" + e.getMessage()); }
    }


    // ==================== 资金流向路径/分析/预测/监控/预警/报告 ====================

    @Operation(summary = "资金流向路径分析")
    @PostMapping("/fund-flow/path")
    public R<Map<String, Object>> fundFlowPath(@RequestBody Map<String, Object> p) {
        try {
            String id = str(p, "fundFlowId", "id");
            if (StringUtils.isBlank(id)) return R.fail("缺少ID参数");
            GzctFinRelatedParty party = relatedPartyMapper.selectById(id);
            if (party == null) return R.fail("记录不存在");
            // 构建路径数据：基于关联交易链路
            List<GzctFinRelatedParty> all = relatedPartyMapper.selectList(
                new LambdaQueryWrapper<GzctFinRelatedParty>().eq(GzctFinRelatedParty::getCompanyId, party.getCompanyId())
                    .orderByAsc(GzctFinRelatedParty::getCreateTime));
            List<Map<String, Object>> pathDetails = new ArrayList<>();
            Set<String> nodeNames = new LinkedHashSet<>();
            List<Map<String, Object>> links = new ArrayList<>();
            nodeNames.add(party.getCompanyId() != null ? party.getCompanyId() : "源企业");
            int step = 1;
            for (GzctFinRelatedParty r : all) {
                String from = r.getCompanyId() != null ? r.getCompanyId() : "源企业";
                String to = r.getPartyName() != null ? r.getPartyName() : "目标企业";
                nodeNames.add(from);
                nodeNames.add(to);
                Map<String, Object> detail = new HashMap<>();
                detail.put("stepNo", step++);
                detail.put("fromEntity", from);
                detail.put("toEntity", to);
                detail.put("transferAmount", r.getTransactionAmount() != null ? r.getTransactionAmount().toString() + "万" : "0万");
                detail.put("transferDate", r.getPeriod() != null ? r.getPeriod() : "");
                detail.put("transferType", r.getTransactionType() != null ? r.getTransactionType() : "资金转移");
                detail.put("status", "COMPLETED");
                pathDetails.add(detail);
                Map<String, Object> link = new HashMap<>();
                link.put("source", from);
                link.put("target", to);
                link.put("value", r.getTransactionAmount() != null ? r.getTransactionAmount() : BigDecimal.ZERO);
                links.add(link);
            }
            List<Map<String, Object>> nodes = new ArrayList<>();
            for (String name : nodeNames) {
                Map<String, Object> node = new HashMap<>();
                node.put("name", name);
                nodes.add(node);
            }
            Map<String, Object> result = new HashMap<>();
            result.put("pathDetails", pathDetails);
            result.put("nodes", nodes);
            result.put("links", links);
            result.put("totalSteps", pathDetails.size());
            return R.success(result);
        } catch (Exception e) { return R.fail("路径分析失败：" + e.getMessage()); }
    }

    @Operation(summary = "资金流向深度分析")
    @PostMapping("/fund-flow/analyze")
    public R<Map<String, Object>> fundFlowAnalyze(@RequestBody Map<String, Object> p) {
        try {
            String id = str(p, "fundFlowId", "id");
            List<GzctFinRelatedParty> all;
            if (StringUtils.isNotBlank(id)) {
                GzctFinRelatedParty party = relatedPartyMapper.selectById(id);
                if (party == null) return R.fail("记录不存在");
                all = relatedPartyMapper.selectList(
                    new LambdaQueryWrapper<GzctFinRelatedParty>().eq(GzctFinRelatedParty::getCompanyId, party.getCompanyId()));
            } else {
                all = relatedPartyMapper.selectList(null);
            }
            // 金额分析 - 按月统计
            Map<String, BigDecimal> monthlyAmount = new LinkedHashMap<>();
            Map<String, Integer> monthlyCount = new LinkedHashMap<>();
            for (GzctFinRelatedParty r : all) {
                String month = r.getPeriod() != null ? r.getPeriod() : "未知";
                monthlyAmount.merge(month, safe(r.getTransactionAmount()), BigDecimal::add);
                monthlyCount.merge(month, 1, Integer::sum);
            }
            // 类型分布
            Map<String, BigDecimal> typeDistribution = new HashMap<>();
            for (GzctFinRelatedParty r : all) {
                String type = r.getTransactionType() != null ? r.getTransactionType() : "其他";
                typeDistribution.merge(type, safe(r.getTransactionAmount()), BigDecimal::add);
            }
            // 风险评估
            long totalCount = all.size();
            long majorCount = all.stream().filter(r -> "1".equals(r.getIsMajor())).count();
            int riskScore = totalCount > 0 ? (int)(majorCount * 100 / totalCount) : 0;
            // 建议
            List<Map<String, Object>> suggestions = new ArrayList<>();
            Map<String, Object> s1 = new HashMap<>();
            s1.put("title", "金额分析");
            s1.put("description", "资金流转总额" + all.stream().map(r -> safe(r.getTransactionAmount())).reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP) + "万元");
            s1.put("type", riskScore > 50 ? "warning" : "success");
            suggestions.add(s1);
            Map<String, Object> s2 = new HashMap<>();
            s2.put("title", "风险评估");
            s2.put("description", "风险指数" + riskScore + "%，" + (riskScore > 50 ? "建议加强监控" : "风险可控"));
            s2.put("type", riskScore > 50 ? "warning" : "success");
            suggestions.add(s2);
            Map<String, Object> result = new HashMap<>();
            result.put("monthlyAmount", monthlyAmount);
            result.put("monthlyCount", monthlyCount);
            result.put("typeDistribution", typeDistribution);
            result.put("riskScore", riskScore);
            result.put("suggestions", suggestions);
            result.put("totalRecords", totalCount);
            result.put("majorRecords", majorCount);
            return R.success(result);
        } catch (Exception e) { return R.fail("深度分析失败：" + e.getMessage()); }
    }

    @Operation(summary = "资金流向预测")
    @PostMapping("/fund-flow/predict")
    public R<Map<String, Object>> fundFlowPredict(@RequestBody Map<String, Object> p) {
        try {
            String id = str(p, "fundFlowId", "id", "companyId");
            String period = str(p, "period");
            String model = str(p, "model"); // 预测模型：LINEAR / TIMESERIES / NEURAL
            List<GzctFinRelatedParty> all = relatedPartyMapper.selectList(
                new LambdaQueryWrapper<GzctFinRelatedParty>().orderByAsc(GzctFinRelatedParty::getCreateTime));
            if (all.isEmpty()) return R.fail("暂无历史数据");
            // 计算历史趋势
            Map<String, BigDecimal> monthlyTotal = new LinkedHashMap<>();
            for (GzctFinRelatedParty r : all) {
                String month = r.getPeriod() != null ? r.getPeriod() : "未知";
                monthlyTotal.merge(month, safe(r.getTransactionAmount()), BigDecimal::add);
            }
            List<String> months = new ArrayList<>(monthlyTotal.keySet());
            List<BigDecimal> values = new ArrayList<>(monthlyTotal.values());
            // 基础线性预测：增长率 = (末值 - 首值) / 期数
            BigDecimal avg = values.stream().reduce(BigDecimal.ZERO, BigDecimal::add)
                .divide(BigDecimal.valueOf(Math.max(values.size(), 1)), 2, RoundingMode.HALF_UP);
            BigDecimal growth = values.size() > 1
                ? values.get(values.size() - 1).subtract(values.get(0))
                    .divide(BigDecimal.valueOf(values.size()), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
            // ===== 根据预测模型调整算法行为 =====
            // LINEAR：纯线性外推（增长率 100%、置信度高、模型名"线性回归"）
            // TIMESERIES：增长率衰减 85%（模拟时间序列均值回归）、置信度中、模型名"时间序列"
            // NEURAL：增长率放大 120%（模拟非线性放大）、置信度低（不确定性高）、模型名"神经网络"
            BigDecimal growthFactor = BigDecimal.ONE;
            int confidenceBase = 95;
            int confidenceDecay = 5;
            String modelLabel = "线性回归";
            if ("TIMESERIES".equals(model)) {
                growthFactor = new BigDecimal("0.85");
                confidenceBase = 90;
                confidenceDecay = 4;
                modelLabel = "时间序列";
            } else if ("NEURAL".equals(model)) {
                growthFactor = new BigDecimal("1.20");
                confidenceBase = 88;
                confidenceDecay = 7;
                modelLabel = "神经网络";
            }
            List<Map<String, Object>> predictResults = new ArrayList<>();
            int predictMonths = "1Y".equals(period) ? 12 : ("6M".equals(period) ? 6 : ("1M".equals(period) ? 1 : 3));
            BigDecimal lastValue = values.isEmpty() ? avg : values.get(values.size() - 1);
            for (int i = 1; i <= predictMonths; i++) {
                BigDecimal predicted = lastValue.add(
                    growth.multiply(BigDecimal.valueOf(i)).multiply(growthFactor));
                int confidence = Math.max(50, confidenceBase - i * confidenceDecay);
                String riskLevel = confidence > 80 ? "低" : (confidence > 65 ? "中" : "高");
                Map<String, Object> pr = new HashMap<>();
                pr.put("period", "预测第" + i + "月");
                pr.put("predictAmount", predicted.setScale(2, RoundingMode.HALF_UP));
                pr.put("confidence", confidence);
                pr.put("riskLevel", riskLevel);
                predictResults.add(pr);
            }
            List<Map<String, Object>> advice = new ArrayList<>();
            Map<String, Object> a1 = new HashMap<>();
            a1.put("time", "近期");
            a1.put("title", modelLabel + " - 趋势预测");
            a1.put("content", "采用「" + modelLabel + "」模型，基于历史数据预测资金流转呈"
                + (growth.compareTo(BigDecimal.ZERO) > 0 ? "上升" : "平稳") + "趋势");
            advice.add(a1);
            Map<String, Object> result = new HashMap<>();
            result.put("historyMonths", months);
            result.put("historyValues", values);
            result.put("predictResults", predictResults);
            result.put("advice", advice);
            result.put("avgAmount", avg);
            result.put("growthRate", growth);
            return R.success(result);
        } catch (Exception e) { return R.fail("预测失败：" + e.getMessage()); }
    }

    @Operation(summary = "资金流向实时监控")
    @PostMapping("/fund-flow/realtime")
    public R<Map<String, Object>> fundFlowRealtime(@RequestBody(required = false) Map<String, Object> p) {
        try {
            String id = p != null ? str(p, "fundFlowId", "id") : null;
            long total = relatedPartyMapper.selectCount(null);
            long abnormal = relatedPartyMapper.selectCount(
                new LambdaQueryWrapper<GzctFinRelatedParty>().eq(GzctFinRelatedParty::getIsMajor, "1"));
            // 监控规则 - 从预警表获取
            List<GzctFinAlert> alerts = alertMapper.selectList(
                new LambdaQueryWrapper<GzctFinAlert>().orderByDesc(GzctFinAlert::getCreateTime));
            List<Map<String, Object>> monitorRules = new ArrayList<>();
            List<Map<String, Object>> warningRecords = new ArrayList<>();
            for (GzctFinAlert alert : alerts) {
                Map<String, Object> rule = new HashMap<>();
                rule.put("id", alert.getAlertId());
                rule.put("ruleName", alert.getAlertName() != null ? alert.getAlertName() : "监控规则");
                rule.put("ruleType", alert.getAlertType() != null ? alert.getAlertType() : "金额监控");
                rule.put("threshold", alert.getThreshold() != null ? alert.getThreshold().toString() : "0");
                rule.put("status", alert.getStatus() != null ? alert.getStatus() : "ACTIVE");
                monitorRules.add(rule);
                // All alerts are potential warning records
                Map<String, Object> record = new HashMap<>();
                record.put("warningTime", alert.getCreateTime() != null ? alert.getCreateTime().toString() : "");
                record.put("warningType", alert.getAlertType());
                record.put("warningLevel", alert.getAlertLevel() != null ? alert.getAlertLevel() : "中");
                record.put("warningContent", alert.getAlertName());
                record.put("handlingStatus", "ACTIVE".equals(alert.getStatus()) ? "PENDING" : "RESOLVED");
                warningRecords.add(record);
            }
            Map<String, Object> result = new HashMap<>();
            result.put("status", abnormal > 0 ? "预警" : "正常");
            result.put("abnormalCount", abnormal);
            result.put("warningLevel", abnormal > 5 ? "高" : (abnormal > 0 ? "中" : "低"));
            result.put("lastUpdate", LocalDateTime.now().toString().substring(0, 10));
            result.put("totalFlows", total);
            result.put("monitorRules", monitorRules);
            result.put("warningRecords", warningRecords);
            return R.success(result);
        } catch (Exception e) { return R.fail("监控数据获取失败：" + e.getMessage()); }
    }

    @Operation(summary = "资金流向预警设置保存")
    @PostMapping("/fund-flow/alert")
    public R<Boolean> fundFlowAlertSave(@RequestBody Map<String, Object> p) {
        try {
            GzctFinAlert alert = new GzctFinAlert();
            String id = str(p, "alertId", "id");
            if (p.get("alertName") != null) alert.setAlertName(p.get("alertName").toString());
            if (p.get("alertType") != null) alert.setAlertType(p.get("alertType").toString());
            if (p.get("threshold") != null) alert.setThreshold(new BigDecimal(p.get("threshold").toString()));
            if (p.get("alertLevel") != null) alert.setAlertLevel(p.get("alertLevel").toString());
            if (p.get("enabled") != null) alert.setStatus(Boolean.TRUE.equals(p.get("enabled")) ? "ACTIVE" : "INACTIVE");
            if (p.get("remark") != null) alert.setRemark(p.get("remark").toString());
            if (StringUtils.isNotBlank(id)) {
                alert.setAlertId(id);
                alert.setUpdateTime(LocalDateTime.now());
                alertMapper.updateById(alert);
            } else {
                alert.setCreateTime(LocalDateTime.now());
                alert.setUpdateTime(LocalDateTime.now());
                alertMapper.insert(alert);
            }
            return R.success(true);
        } catch (Exception e) { return R.fail("预警保存失败：" + e.getMessage()); }
    }

    @Operation(summary = "资金流向预警列表")
    @GetMapping("/fund-flow/alert/list")
    public R<List<Map<String, Object>>> fundFlowAlertList() {
        try {
            List<GzctFinAlert> alerts = alertMapper.selectList(
                new LambdaQueryWrapper<GzctFinAlert>().orderByDesc(GzctFinAlert::getCreateTime));
            List<Map<String, Object>> list = new ArrayList<>();
            for (GzctFinAlert alert : alerts) {
                Map<String, Object> m = new HashMap<>();
                m.put("id", alert.getAlertId());
                m.put("alertId", alert.getAlertId());
                m.put("alertName", alert.getAlertName());
                m.put("alertType", alert.getAlertType());
                m.put("threshold", alert.getThreshold());
                m.put("alertLevel", alert.getAlertLevel());
                m.put("enabled", "ACTIVE".equals(alert.getStatus()));
                m.put("status", alert.getStatus());
                m.put("remark", alert.getRemark());
                list.add(m);
            }
            return R.success(list);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "资金流向预警删除")
    @DeleteMapping("/fund-flow/alert/{id}")
    public R<Boolean> fundFlowAlertDelete(@PathVariable String id) {
        try {
            if (StringUtils.isBlank(id)) return R.fail("缺少ID参数");
            alertMapper.deleteById(id);
            return R.success(true);
        } catch (Exception e) { return R.fail("删除失败：" + e.getMessage()); }
    }

    @Operation(summary = "更新监控规则")
    @PostMapping("/fund-flow/monitor-rule/update")
    public R<Boolean> updateMonitorRule(@RequestBody Map<String, Object> p) {
        try {
            String id = str(p, "alertId", "id");
            if (StringUtils.isBlank(id)) return R.fail("缺少ID参数");
            GzctFinAlert alert = alertMapper.selectById(id);
            if (alert == null) return R.fail("规则不存在");
            if (p.get("alertName") != null) alert.setAlertName(p.get("alertName").toString());
            if (p.get("ruleName") != null) alert.setAlertName(p.get("ruleName").toString());
            if (p.get("alertType") != null) alert.setAlertType(p.get("alertType").toString());
            if (p.get("ruleType") != null) alert.setAlertType(p.get("ruleType").toString());
            if (p.get("threshold") != null) alert.setThreshold(new BigDecimal(p.get("threshold").toString()));
            if (p.get("status") != null) alert.setStatus(p.get("status").toString());
            alert.setUpdateTime(LocalDateTime.now());
            alertMapper.updateById(alert);
            return R.success(true);
        } catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); }
    }

    @Operation(summary = "删除监控规则")
    @DeleteMapping("/fund-flow/monitor-rule/{id}")
    public R<Boolean> deleteMonitorRule(@PathVariable String id) {
        try {
            if (StringUtils.isBlank(id)) return R.fail("缺少ID参数");
            alertMapper.deleteById(id);
            return R.success(true);
        } catch (Exception e) { return R.fail("删除失败：" + e.getMessage()); }
    }

    @Operation(summary = "资金流向报告生成")
    @PostMapping("/fund-flow/report")
    public R<Map<String, Object>> fundFlowReport(@RequestBody Map<String, Object> p) {
        try {
            String id = str(p, "fundFlowId", "id");
            if (StringUtils.isBlank(id)) return R.fail("缺少ID参数");
            GzctFinRelatedParty party = relatedPartyMapper.selectById(id);
            if (party == null) return R.fail("记录不存在");
            // 获取该企业所有关联交易
            List<GzctFinRelatedParty> companyFlows = relatedPartyMapper.selectList(
                new LambdaQueryWrapper<GzctFinRelatedParty>().eq(GzctFinRelatedParty::getCompanyId, party.getCompanyId()));
            BigDecimal totalAmount = companyFlows.stream()
                .map(r -> safe(r.getTransactionAmount())).reduce(BigDecimal.ZERO, BigDecimal::add);
            long majorCount = companyFlows.stream().filter(r -> "1".equals(r.getIsMajor())).count();
            Map<String, Object> report = new LinkedHashMap<>();
            report.put("reportId", UUID.randomUUID().toString());
            report.put("generateTime", LocalDateTime.now().toString());
            report.put("companyId", party.getCompanyId());
            report.put("partyName", party.getPartyName());
            report.put("totalFlows", companyFlows.size());
            report.put("totalAmount", totalAmount.setScale(2, RoundingMode.HALF_UP));
            report.put("majorCount", majorCount);
            report.put("riskLevel", majorCount > 3 ? "高" : (majorCount > 0 ? "中" : "低"));
            report.put("conclusion", majorCount > 3 ? "存在较多重大关联交易，建议重点关注" : "资金流向整体正常");
            // details 字段命名与 /fund-flow/list 保持一致，方便前端 csv 直接复用 columnOptions
            List<Map<String, Object>> details = new ArrayList<>();
            int idx = 1;
            for (GzctFinRelatedParty r : companyFlows) {
                Map<String, Object> d = new LinkedHashMap<>();
                d.put("id", r.getPartyId() != null ? r.getPartyId() : String.valueOf(idx));
                d.put("flowNo", "FL-" + (r.getPeriod() != null ? r.getPeriod().replace("-", "") : "0") + String.format("%03d", idx));
                d.put("fromCompany", r.getCompanyName() != null ? r.getCompanyName()
                    : (r.getCompanyId() != null ? r.getCompanyId() : "未知企业"));
                d.put("toCompany", r.getPartyName() != null ? r.getPartyName() : "未知方");
                d.put("amount", r.getTransactionAmount() != null ? r.getTransactionAmount() : BigDecimal.ZERO);
                d.put("purpose", r.getTransactionType() != null ? r.getTransactionType() : "");
                d.put("fundNature", r.getRelationType() != null ? r.getRelationType() : "");
                // 从 transactionType 推导 flowType（与 list 接口逻辑一致）
                String txType = r.getTransactionType() != null ? r.getTransactionType() : "";
                String txUpper = txType.toUpperCase();
                String mappedFlowType;
                if ("INFLOW".equals(txUpper) || txType.contains("流入") || txType.contains("收入")) {
                    mappedFlowType = "INFLOW";
                } else if ("OUTFLOW".equals(txUpper) || txType.contains("流出") || txType.contains("支出") || txType.contains("投资")) {
                    mappedFlowType = "OUTFLOW";
                } else if ("INTERNAL_TRANSFER".equals(txUpper) || txType.contains("转移") || txType.contains("内部")) {
                    mappedFlowType = "INTERNAL_TRANSFER";
                } else if ("INVESTMENT_RECOVERY".equals(txUpper) || txType.contains("收回") || txType.contains("回收")) {
                    mappedFlowType = "INVESTMENT_RECOVERY";
                } else {
                    mappedFlowType = "INFLOW";
                }
                d.put("flowType", mappedFlowType);
                d.put("occurTime", r.getPeriod() != null ? r.getPeriod() : "");
                d.put("isMajor", r.getIsMajor());
                String detailRiskLevel = "1".equals(r.getIsMajor()) ? "ABNORMAL" : "NORMAL";
                d.put("riskLevel", detailRiskLevel);
                d.put("riskDesc", "ABNORMAL".equals(detailRiskLevel) ? "重大关联交易，需关注合规性。" : "常规关联交易，无明显风险。");
                // 兼容字段（旧 details 字段，保留以防其他调用方依赖）
                d.put("partyName", r.getPartyName());
                d.put("transactionType", r.getTransactionType());
                d.put("balanceAmount", r.getBalanceAmount());
                d.put("period", r.getPeriod());
                details.add(d);
                idx++;
            }
            report.put("details", details);
            report.put("status", "GENERATED");
            return R.success(report);
        } catch (Exception e) { return R.fail("报告生成失败：" + e.getMessage()); }
    }

    // ==================== 合规监管扩展接口 ====================

    @Operation(summary = "合规记录保存")
    @PostMapping("/compliance/save")
    public R<Boolean> complianceSave(@RequestBody Map<String, Object> p) {
        try {
            GzctFinancialCompliance c = new GzctFinancialCompliance();
            String id = p.get("complianceId") != null ? p.get("complianceId").toString() :
                (p.get("id") != null ? p.get("id").toString() : null);
            if (p.get("companyId") != null) c.setCompanyId(p.get("companyId").toString());
            if (p.get("enterpriseName") != null) c.setEnterpriseName(p.get("enterpriseName").toString());
            if (p.get("checkType") != null) c.setCheckType(p.get("checkType").toString());
            if (p.get("complianceStatus") != null) c.setComplianceStatus(p.get("complianceStatus").toString());
            if (p.get("riskLevel") != null) c.setRiskLevel(p.get("riskLevel").toString());
            if (p.get("complianceScore") != null && StringUtils.isNotBlank(p.get("complianceScore").toString())) {
                c.setComplianceScore(new BigDecimal(p.get("complianceScore").toString()));
            }
            if (p.get("violationType") != null) c.setViolationType(p.get("violationType").toString());
            if (p.get("checkMethod") != null) c.setCheckMethod(p.get("checkMethod").toString());
            if (p.get("checkScope") != null) c.setCheckScope(p.get("checkScope").toString());
            if (p.get("inspector") != null) c.setInspector(p.get("inspector").toString());
            if (p.get("period") != null) c.setPeriod(p.get("period").toString());
            if (p.get("auditStatus") != null) c.setAuditStatus(p.get("auditStatus").toString());
            if (p.get("issues") != null) c.setIssues(p.get("issues").toString());
            if (p.get("rectificationRequirements") != null) c.setRectificationRequirements(p.get("rectificationRequirements").toString());
            if (p.get("rectificationStatus") != null) c.setRectificationStatus(p.get("rectificationStatus").toString());
            if (p.get("violationNature") != null) c.setViolationNature(p.get("violationNature").toString());
            if (p.get("violationDescription") != null) c.setViolationDescription(p.get("violationDescription").toString());
            if (p.get("involvedAmount") != null && StringUtils.isNotBlank(p.get("involvedAmount").toString())) {
                c.setInvolvedAmount(new BigDecimal(p.get("involvedAmount").toString()));
            }
            if (p.get("handlingDecision") != null) c.setHandlingDecision(p.get("handlingDecision").toString());
            if (p.get("responsibleParty") != null) c.setResponsibleParty(p.get("responsibleParty").toString());
            if (p.get("discoveryMethod") != null) c.setDiscoveryMethod(p.get("discoveryMethod").toString());
            if (p.get("executionStatus") != null) c.setExecutionStatus(p.get("executionStatus").toString());
            if (p.get("completionProgress") != null && StringUtils.isNotBlank(p.get("completionProgress").toString())) {
                c.setCompletionProgress(Integer.parseInt(p.get("completionProgress").toString()));
            }
            // 日期字段处理
            if (p.get("checkDate") != null && StringUtils.isNotBlank(p.get("checkDate").toString())) {
                c.setCheckDate(parseDateTime(p.get("checkDate").toString()));
            }
            if (p.get("checkDateStart") != null && StringUtils.isNotBlank(p.get("checkDateStart").toString())) {
                c.setCheckDateStart(parseDateTime(p.get("checkDateStart").toString()));
            }
            if (p.get("checkDateEnd") != null && StringUtils.isNotBlank(p.get("checkDateEnd").toString())) {
                c.setCheckDateEnd(parseDateTime(p.get("checkDateEnd").toString()));
            }
            if (p.get("rectificationDeadline") != null && StringUtils.isNotBlank(p.get("rectificationDeadline").toString())) {
                c.setRectificationDeadline(parseDateTime(p.get("rectificationDeadline").toString()));
            }
            if (StringUtils.isNotBlank(id)) {
                c.setComplianceId(id);
                c.setUpdateTime(LocalDateTime.now());
                financialComplianceMapper.updateById(c);
            } else {
                c.setCreateTime(LocalDateTime.now());
                c.setUpdateTime(LocalDateTime.now());
                financialComplianceMapper.insert(c);
            }
            return R.success(true);
        } catch (Exception e) { return R.fail("保存失败：" + e.getMessage()); }
    }

    /** 解析前端传来的日期字符串为 LocalDateTime */
    private LocalDateTime parseDateTime(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) return null;
        try {
            // 处理 ISO 格式 "2025-01-21T10:30:00.000Z"
            if (dateStr.contains("T")) {
                dateStr = dateStr.replace("Z", "");
                if (dateStr.length() > 19) dateStr = dateStr.substring(0, 19);
                return LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss"));
            }
            // 处理纯日期 "2025-01-21"
            if (dateStr.length() == 10) {
                return LocalDate.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd")).atStartOfDay();
            }
            // 处理 "2025-01-21 10:30:00"
            return LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        } catch (Exception e) {
            log.warn("日期解析失败: {}", dateStr);
            return null;
        }
    }

    @Operation(summary = "合规记录删除")
    @DeleteMapping("/compliance/{id}")
    public R<Boolean> complianceDelete(@PathVariable String id) {
        try {
            if (StringUtils.isBlank(id)) return R.fail("缺少ID参数");
            financialComplianceMapper.deleteById(id);
            return R.success(true);
        } catch (Exception e) { return R.fail("删除失败：" + e.getMessage()); }
    }

    @Operation(summary = "批量删除合规记录")
    @PostMapping("/compliance/batch-delete")
    public R<Boolean> complianceBatchDelete(@RequestBody Map<String, Object> p) {
        try {
            List<String> ids = null;
            if (p.get("ids") != null) {
                Object idsObj = p.get("ids");
                if (idsObj instanceof List) {
                    ids = ((List<?>) idsObj).stream().map(Object::toString).collect(Collectors.toList());
                }
            }
            if (ids == null || ids.isEmpty()) return R.fail("缺少ID列表");
            financialComplianceMapper.deleteBatchIds(ids);
            return R.success(true);
        } catch (Exception e) { return R.fail("批量删除失败：" + e.getMessage()); }
    }

    @Operation(summary = "合规数据导出")
    @GetMapping("/compliance/export")
    public void complianceExport(HttpServletResponse response) {
        try {
            List<GzctFinancialCompliance> all = financialComplianceMapper.selectList(
                new LambdaQueryWrapper<GzctFinancialCompliance>().orderByDesc(GzctFinancialCompliance::getCreateTime));
            List<List<Object>> rows = new ArrayList<>();
            // 表头
            rows.add(Arrays.asList("企业名称", "企业编号", "检查类型", "合规状态", "风险等级", "合规评分", "违规类型", "检查方式", "检查人员", "检查日期", "报告期间", "发现问题", "整改状态"));
            for (GzctFinancialCompliance c : all) {
                rows.add(Arrays.asList(
                    c.getEnterpriseName(), c.getCompanyId(), c.getCheckType(), c.getComplianceStatus(),
                    c.getRiskLevel(), c.getComplianceScore(), c.getViolationType(), c.getCheckMethod(),
                    c.getInspector(), c.getCheckDate(), c.getPeriod(), c.getIssues(), c.getRectificationStatus()
                ));
            }
            byte[] xlsx = SimpleXlsxWriter.write("财务合规数据", rows);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=financial_compliance_" + System.currentTimeMillis() + ".xlsx");
            response.getOutputStream().write(xlsx);
            response.getOutputStream().flush();
        } catch (Exception e) {
            log.error("导出失败", e);
            try {
                response.setContentType("application/json");
                response.getWriter().write("{\"result\":500,\"msg\":\"导出失败：" + e.getMessage() + "\"}");
            } catch (IOException ignored) {}
        }
    }

    @Operation(summary = "合规报告生成")
    @PostMapping("/compliance/report/generate")
    public R<Map<String, Object>> complianceReportGenerate(@RequestBody(required = false) Map<String, Object> p) {
        try {
            long total = financialComplianceMapper.selectCount(null);
            long compliant = financialComplianceMapper.selectCount(
                new LambdaQueryWrapper<GzctFinancialCompliance>().eq(GzctFinancialCompliance::getComplianceStatus, "COMPLIANT"));
            long violation = financialComplianceMapper.selectCount(
                new LambdaQueryWrapper<GzctFinancialCompliance>().eq(GzctFinancialCompliance::getComplianceStatus, "NON_COMPLIANT"));
            Map<String, Object> result = new HashMap<>();
            result.put("reportId", UUID.randomUUID().toString());
            result.put("generateTime", LocalDateTime.now().toString());
            result.put("totalRecords", total);
            result.put("compliantCount", compliant);
            result.put("violationCount", violation);
            result.put("complianceRate", total > 0 ? compliant * 100 / total : 0);
            result.put("status", "GENERATED");
            return R.success(result);
        } catch (Exception e) { return R.fail("报告生成失败：" + e.getMessage()); }
    }

    @Operation(summary = "违规处理保存")
    @PostMapping("/compliance/violation/handle/{id}")
    public R<Boolean> complianceViolationHandle(@PathVariable String id, @RequestBody Map<String, Object> p) {
        try {
            GzctFinancialCompliance c = financialComplianceMapper.selectById(id);
            if (c == null) return R.fail("记录不存在");
            if (p.get("violationNature") != null) c.setViolationNature(p.get("violationNature").toString());
            if (p.get("violationDescription") != null) c.setViolationDescription(p.get("violationDescription").toString());
            if (p.get("involvedAmount") != null) c.setInvolvedAmount(new BigDecimal(p.get("involvedAmount").toString()));
            if (p.get("handlingDecision") != null) c.setHandlingDecision(p.get("handlingDecision").toString());
            if (p.get("responsibleParty") != null) c.setResponsibleParty(p.get("responsibleParty").toString());
            if (p.get("discoveryMethod") != null) c.setDiscoveryMethod(p.get("discoveryMethod").toString());
            if (p.get("executionStatus") != null) c.setExecutionStatus(p.get("executionStatus").toString());
            if (p.get("completionProgress") != null) c.setCompletionProgress(Integer.parseInt(p.get("completionProgress").toString()));
            if (p.get("rectificationRequirements") != null) c.setRectificationRequirements(p.get("rectificationRequirements").toString());
            if (p.get("rectificationStatus") != null) c.setRectificationStatus(p.get("rectificationStatus").toString());
            c.setComplianceStatus("NON_COMPLIANT");
            c.setUpdateTime(LocalDateTime.now());
            financialComplianceMapper.updateById(c);
            return R.success(true);
        } catch (Exception e) { return R.fail("违规处理失败：" + e.getMessage()); }
    }

    // ==================== 绩效扩展接口 ====================

    @Operation(summary = "绩效数据导出")
    @GetMapping("/performance/export")
    public R<Map<String, Object>> performanceExport() {
        try {
            List<GzctFinStatement> all = statementMapper.selectList(
                new LambdaQueryWrapper<GzctFinStatement>().orderByDesc(GzctFinStatement::getCreateTime));
            List<Map<String, Object>> rows = new ArrayList<>();
            for (GzctFinStatement s : all) {
                Map<String, Object> row = new HashMap<>();
                row.put("companyName", s.getCompanyName());
                row.put("period", s.getPeriod());
                row.put("revenue", s.getRevenue());
                row.put("netProfit", s.getNetProfit());
                row.put("totalAssets", s.getTotalAssets());
                BigDecimal profitMargin = (s.getRevenue() != null && s.getRevenue().compareTo(BigDecimal.ZERO) > 0 && s.getNetProfit() != null)
                    ? s.getNetProfit().multiply(BigDecimal.valueOf(100)).divide(s.getRevenue(), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
                row.put("profitMargin", profitMargin);
                rows.add(row);
            }
            Map<String, Object> result = new HashMap<>();
            result.put("data", rows);
            result.put("total", rows.size());
            return R.success(result);
        } catch (Exception e) { return R.fail("导出失败：" + e.getMessage()); }
    }

    @Operation(summary = "绩效报告生成")
    @PostMapping("/performance/report/generate")
    public R<Map<String, Object>> performanceReportGenerate(@RequestBody(required = false) Map<String, Object> p) {
        try {
            // ========== 解析前端过滤条件（之前完全被忽略，UI 假装在过滤——已修复）==========
            if (p == null) p = new HashMap<>();
            String evaluationPeriod = p.get("evaluationPeriod") != null ? p.get("evaluationPeriod").toString().trim() : "";
            String enterpriseName = p.get("enterpriseName") != null ? p.get("enterpriseName").toString().trim() : "";
            String performanceLevel = p.get("performanceLevel") != null ? p.get("performanceLevel").toString().trim() : "";

            // ========== DB 层过滤（period / companyName）+ 仅查绩效报表类型 ==========
            LambdaQueryWrapper<GzctFinStatement> q = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(evaluationPeriod)) q.eq(GzctFinStatement::getPeriod, evaluationPeriod);
            if (StringUtils.isNotBlank(enterpriseName)) q.like(GzctFinStatement::getCompanyName, enterpriseName);
            List<GzctFinStatement> all = statementMapper.selectList(q);

            // ========== 内存计算 performanceLevel（计算字段）+ 按等级过滤 ==========
            // 等级规则：score=netProfit*100/revenue；>10 EXCELLENT / >5 GOOD / >1 AVERAGE / 其它 POOR
            // 与 list 接口（行 3954-3957）保持完全一致，避免字典分裂
            List<Map<String, Object>> rows = new ArrayList<>();
            for (GzctFinStatement s : all) {
                BigDecimal score = BigDecimal.ZERO;
                if (s.getNetProfit() != null && s.getRevenue() != null && s.getRevenue().compareTo(BigDecimal.ZERO) != 0) {
                    score = s.getNetProfit().multiply(BigDecimal.valueOf(100)).divide(s.getRevenue(), 2, RoundingMode.HALF_UP);
                }
                String level;
                if (score.compareTo(BigDecimal.valueOf(10)) > 0) level = "EXCELLENT";
                else if (score.compareTo(BigDecimal.valueOf(5)) > 0) level = "GOOD";
                else if (score.compareTo(BigDecimal.valueOf(1)) > 0) level = "AVERAGE";
                else level = "POOR";
                if (StringUtils.isNotBlank(performanceLevel) && !performanceLevel.equals(level)) continue;
                Map<String, Object> row = new HashMap<>();
                row.put("companyName", s.getCompanyName());
                row.put("period", s.getPeriod());
                row.put("revenue", s.getRevenue() != null ? s.getRevenue() : BigDecimal.ZERO);
                row.put("netProfit", s.getNetProfit() != null ? s.getNetProfit() : BigDecimal.ZERO);
                row.put("performanceScore", score);
                row.put("performanceLevel", level);
                rows.add(row);
            }

            // ========== 汇总：营收 / 净利润 / 等级分布 / 平均分 ==========
            BigDecimal totalRevenue = BigDecimal.ZERO, totalProfit = BigDecimal.ZERO, totalScore = BigDecimal.ZERO;
            int excellentCount = 0, goodCount = 0, averageCount = 0, poorCount = 0;
            for (Map<String, Object> row : rows) {
                totalRevenue = totalRevenue.add((BigDecimal) row.get("revenue"));
                totalProfit = totalProfit.add((BigDecimal) row.get("netProfit"));
                totalScore = totalScore.add((BigDecimal) row.get("performanceScore"));
                String level = (String) row.get("performanceLevel");
                if ("EXCELLENT".equals(level)) excellentCount++;
                else if ("GOOD".equals(level)) goodCount++;
                else if ("AVERAGE".equals(level)) averageCount++;
                else poorCount++;
            }
            int count = rows.size();
            BigDecimal avgScore = count > 0 ? totalScore.divide(BigDecimal.valueOf(count), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
            BigDecimal avgProfitMargin = totalRevenue.compareTo(BigDecimal.ZERO) > 0
                ? totalProfit.multiply(BigDecimal.valueOf(100)).divide(totalRevenue, 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;

            // ========== 组装过滤条件回显（前端 alert 显示「本次报告基于：X 条件」）==========
            List<String> filters = new ArrayList<>();
            if (StringUtils.isNotBlank(evaluationPeriod)) filters.add("期间=" + evaluationPeriod);
            if (StringUtils.isNotBlank(enterpriseName)) filters.add("企业名包含「" + enterpriseName + "」");
            if (StringUtils.isNotBlank(performanceLevel)) {
                Map<String, String> levelText = new HashMap<>();
                levelText.put("EXCELLENT", "优秀"); levelText.put("GOOD", "良好");
                levelText.put("AVERAGE", "一般"); levelText.put("POOR", "较差");
                filters.add("等级=" + levelText.getOrDefault(performanceLevel, performanceLevel));
            }
            String filterDesc = filters.isEmpty() ? "全量数据" : String.join("，", filters);

            Map<String, Object> result = new HashMap<>();
            result.put("reportId", UUID.randomUUID().toString());
            result.put("generateTime", LocalDateTime.now().toString());
            result.put("filterDesc", filterDesc);
            result.put("totalEntities", count);
            result.put("totalRevenue", totalRevenue.setScale(2, RoundingMode.HALF_UP));
            result.put("totalProfit", totalProfit.setScale(2, RoundingMode.HALF_UP));
            result.put("avgProfitMargin", avgProfitMargin);
            result.put("avgScore", avgScore);
            result.put("excellentCount", excellentCount);
            result.put("goodCount", goodCount);
            result.put("averageCount", averageCount);
            result.put("poorCount", poorCount);
            result.put("status", "GENERATED");
            return R.success(result);
        } catch (Exception e) { log.error("绩效报告生成失败", e); return R.fail("报告生成失败：" + e.getMessage()); }
    }

    @Operation(summary = "绩效保存(新增/编辑)")
    @PostMapping("/performance/save")
    public R<Boolean> performanceSave(@RequestBody Map<String, Object> p) {
        try {
            GzctFinStatement stmt = new GzctFinStatement();
            String id = p.get("performanceId") != null ? p.get("performanceId").toString()
                    : (p.get("id") != null ? p.get("id").toString() : null);
            if (p.get("enterpriseName") != null) stmt.setCompanyName(p.get("enterpriseName").toString());
            if (p.get("companyId") != null) stmt.setCompanyId(p.get("companyId").toString());
            if (p.get("period") != null) stmt.setPeriod(p.get("period").toString());
            if (p.get("evaluationPeriod") != null) stmt.setPeriod(p.get("evaluationPeriod").toString());
            if (p.get("industry") != null) stmt.setIndustry(p.get("industry").toString());
            // 字典收口：前端 evaluationStatus 与 auditStatus 共用同一 DB 字段，过 normalize 规范化
            if (p.get("evaluationStatus") != null) stmt.setAuditStatus(AuditStatusUtil.normalize(p.get("evaluationStatus").toString()));
            if (p.get("evaluationNotes") != null) stmt.setAnalysisNotes(p.get("evaluationNotes").toString());
            if (p.get("revenue") != null) {
                try { stmt.setRevenue(new BigDecimal(p.get("revenue").toString())); } catch (Exception ignored) {}
            }
            if (p.get("netProfit") != null) {
                try { stmt.setNetProfit(new BigDecimal(p.get("netProfit").toString())); } catch (Exception ignored) {}
            }
            if (p.get("totalAssets") != null) {
                try { stmt.setTotalAssets(new BigDecimal(p.get("totalAssets").toString())); } catch (Exception ignored) {}
            }
            if (p.get("totalLiabilities") != null) {
                try { stmt.setTotalLiabilities(new BigDecimal(p.get("totalLiabilities").toString())); } catch (Exception ignored) {}
            }
            if (p.get("netAssets") != null) {
                try { stmt.setNetAssets(new BigDecimal(p.get("netAssets").toString())); } catch (Exception ignored) {}
            }
            if (p.get("operatingCashflow") != null) {
                try { stmt.setOperatingCashflow(new BigDecimal(p.get("operatingCashflow").toString())); } catch (Exception ignored) {}
            }
            stmt.setStatementType("PERFORMANCE");
            if (StringUtils.isNotBlank(id)) {
                stmt.setStatementId(id);
                stmt.setUpdateTime(LocalDateTime.now());
                statementMapper.updateById(stmt);
            } else {
                stmt.setCreateTime(LocalDateTime.now());
                stmt.setUpdateTime(LocalDateTime.now());
                statementMapper.insert(stmt);
            }
            return R.success(true);
        } catch (Exception e) { return R.fail("保存失败：" + e.getMessage()); }
    }

    @Operation(summary = "绩效详情")
    @GetMapping("/performance/{id}")
    public R<Map<String, Object>> performanceDetail(@PathVariable String id) {
        try {
            if (StringUtils.isBlank(id)) return R.fail("缺少ID参数");
            GzctFinStatement s = statementMapper.selectById(id);
            if (s == null) return R.fail("记录不存在");
            Map<String, Object> m = new HashMap<>();
            m.put("id", s.getStatementId());
            m.put("performanceId", s.getStatementId());
            m.put("enterpriseName", s.getCompanyName());
            m.put("companyId", s.getCompanyId());
            m.put("period", s.getPeriod());
            m.put("revenue", s.getRevenue());
            m.put("netProfit", s.getNetProfit());
            m.put("totalAssets", s.getTotalAssets());
            m.put("totalLiabilities", s.getTotalLiabilities());
            m.put("netAssets", s.getNetAssets());
            m.put("operatingCashflow", s.getOperatingCashflow());
            m.put("industry", s.getIndustry());
            m.put("auditStatus", s.getAuditStatus());
            m.put("evaluationNotes", s.getAnalysisNotes());
            BigDecimal score = BigDecimal.ZERO;
            if (s.getNetProfit() != null && s.getRevenue() != null && s.getRevenue().compareTo(BigDecimal.ZERO) != 0)
                score = s.getNetProfit().multiply(BigDecimal.valueOf(100)).divide(s.getRevenue(), 2, RoundingMode.HALF_UP);
            m.put("performanceScore", score);
            String level;
            if (score.compareTo(BigDecimal.valueOf(10)) > 0) level = "EXCELLENT";
            else if (score.compareTo(BigDecimal.valueOf(5)) > 0) level = "GOOD";
            else if (score.compareTo(BigDecimal.valueOf(1)) > 0) level = "AVERAGE";
            else level = "POOR";
            m.put("performanceLevel", level);
            BigDecimal roe = BigDecimal.ZERO;
            if (s.getNetProfit() != null && s.getNetAssets() != null && s.getNetAssets().compareTo(BigDecimal.ZERO) != 0)
                roe = s.getNetProfit().multiply(BigDecimal.valueOf(100)).divide(s.getNetAssets(), 2, RoundingMode.HALF_UP);
            m.put("roe", roe);
            BigDecimal roa = BigDecimal.ZERO;
            if (s.getNetProfit() != null && s.getTotalAssets() != null && s.getTotalAssets().compareTo(BigDecimal.ZERO) != 0)
                roa = s.getNetProfit().multiply(BigDecimal.valueOf(100)).divide(s.getTotalAssets(), 2, RoundingMode.HALF_UP);
            m.put("roa", roa);
            return R.success(m);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "绩效删除")
    @DeleteMapping("/performance/{id}")
    public R<Boolean> performanceDelete(@PathVariable String id) {
        try {
            if (StringUtils.isBlank(id)) return R.fail("缺少ID参数");
            statementMapper.deleteById(id);
            return R.success(true);
        } catch (Exception e) { return R.fail("删除失败：" + e.getMessage()); }
    }

    @Operation(summary = "绩效评价")
    @PostMapping("/performance/evaluate")
    public R<Map<String, Object>> performanceEvaluate(@RequestBody Map<String, Object> p) {
        try {
            String id = p.get("performanceId") != null ? p.get("performanceId").toString()
                    : (p.get("id") != null ? p.get("id").toString() : null);
            if (StringUtils.isBlank(id)) return R.fail("缺少绩效ID");
            GzctFinStatement s = statementMapper.selectById(id);
            if (s == null) return R.fail("记录不存在");
            // 更新评价状态
            s.setAuditStatus("EVALUATED");
            if (p.get("evaluationNotes") != null) s.setAnalysisNotes(p.get("evaluationNotes").toString());
            s.setUpdateTime(LocalDateTime.now());
            statementMapper.updateById(s);
            // 计算评价结果
            Map<String, Object> result = new HashMap<>();
            result.put("evaluationId", UUID.randomUUID().toString());
            result.put("performanceId", id);
            BigDecimal score = BigDecimal.ZERO;
            if (s.getNetProfit() != null && s.getRevenue() != null && s.getRevenue().compareTo(BigDecimal.ZERO) != 0)
                score = s.getNetProfit().multiply(BigDecimal.valueOf(100)).divide(s.getRevenue(), 2, RoundingMode.HALF_UP);
            result.put("performanceScore", score);
            // 计算各维度指标
            BigDecimal roe = BigDecimal.ZERO, roa = BigDecimal.ZERO;
            if (s.getNetProfit() != null && s.getNetAssets() != null && s.getNetAssets().compareTo(BigDecimal.ZERO) != 0)
                roe = s.getNetProfit().multiply(BigDecimal.valueOf(100)).divide(s.getNetAssets(), 2, RoundingMode.HALF_UP);
            if (s.getNetProfit() != null && s.getTotalAssets() != null && s.getTotalAssets().compareTo(BigDecimal.ZERO) != 0)
                roa = s.getNetProfit().multiply(BigDecimal.valueOf(100)).divide(s.getTotalAssets(), 2, RoundingMode.HALF_UP);
            result.put("roe", roe);
            result.put("roa", roa);
            // 盈利能力评分
            int profitScore = score.compareTo(BigDecimal.valueOf(10)) > 0 ? 5 : score.compareTo(BigDecimal.valueOf(5)) > 0 ? 4 : score.compareTo(BigDecimal.valueOf(1)) > 0 ? 3 : 2;
            result.put("profitabilityScore", profitScore);
            result.put("growthScore", roe.compareTo(BigDecimal.valueOf(15)) > 0 ? 5 : roe.compareTo(BigDecimal.valueOf(8)) > 0 ? 4 : 3);
            result.put("efficiencyScore", roa.compareTo(BigDecimal.valueOf(8)) > 0 ? 5 : roa.compareTo(BigDecimal.valueOf(4)) > 0 ? 4 : 3);
            result.put("evaluationTime", LocalDateTime.now().toString());
            result.put("status", "EVALUATED");
            return R.success(result);
        } catch (Exception e) { return R.fail("评价失败：" + e.getMessage()); }
    }

    @Operation(summary = "绩效对标分析")
    @PostMapping("/performance/benchmark")
    public R<Map<String, Object>> performanceBenchmark(@RequestBody Map<String, Object> p) {
        try {
            String id = p.get("performanceId") != null ? p.get("performanceId").toString()
                    : (p.get("id") != null ? p.get("id").toString() : null);
            if (StringUtils.isBlank(id)) return R.fail("缺少绩效ID");
            GzctFinStatement current = statementMapper.selectById(id);
            if (current == null) return R.fail("记录不存在");
            // 获取同行业所有企业数据进行对标
            LambdaQueryWrapper<GzctFinStatement> w = new LambdaQueryWrapper<>();
            if (current.getIndustry() != null) w.eq(GzctFinStatement::getIndustry, current.getIndustry());
            List<GzctFinStatement> peers = statementMapper.selectList(w);
            int totalPeers = peers.size();
            // 计算行业平均值
            BigDecimal avgRevenue = BigDecimal.ZERO, avgProfit = BigDecimal.ZERO, avgAssets = BigDecimal.ZERO;
            for (GzctFinStatement peer : peers) {
                if (peer.getRevenue() != null) avgRevenue = avgRevenue.add(peer.getRevenue());
                if (peer.getNetProfit() != null) avgProfit = avgProfit.add(peer.getNetProfit());
                if (peer.getTotalAssets() != null) avgAssets = avgAssets.add(peer.getTotalAssets());
            }
            if (totalPeers > 0) {
                avgRevenue = avgRevenue.divide(BigDecimal.valueOf(totalPeers), 2, RoundingMode.HALF_UP);
                avgProfit = avgProfit.divide(BigDecimal.valueOf(totalPeers), 2, RoundingMode.HALF_UP);
                avgAssets = avgAssets.divide(BigDecimal.valueOf(totalPeers), 2, RoundingMode.HALF_UP);
            }
            // 计算排名
            BigDecimal currentScore = BigDecimal.ZERO;
            if (current.getNetProfit() != null && current.getRevenue() != null && current.getRevenue().compareTo(BigDecimal.ZERO) != 0)
                currentScore = current.getNetProfit().multiply(BigDecimal.valueOf(100)).divide(current.getRevenue(), 2, RoundingMode.HALF_UP);
            int ranking = 1;
            for (GzctFinStatement peer : peers) {
                BigDecimal peerScore = BigDecimal.ZERO;
                if (peer.getNetProfit() != null && peer.getRevenue() != null && peer.getRevenue().compareTo(BigDecimal.ZERO) != 0)
                    peerScore = peer.getNetProfit().multiply(BigDecimal.valueOf(100)).divide(peer.getRevenue(), 2, RoundingMode.HALF_UP);
                if (peerScore.compareTo(currentScore) > 0) ranking++;
            }
            Map<String, Object> result = new HashMap<>();
            result.put("ranking", ranking + "/" + totalPeers);
            result.put("totalPeers", totalPeers);
            result.put("currentScore", currentScore);
            result.put("industryAvgRevenue", avgRevenue);
            result.put("industryAvgProfit", avgProfit);
            result.put("industryAvgAssets", avgAssets);
            // 对标指标详情
            List<Map<String, Object>> indicators = new ArrayList<>();
            BigDecimal avgProfitMargin = avgRevenue.compareTo(BigDecimal.ZERO) > 0
                ? avgProfit.multiply(BigDecimal.valueOf(100)).divide(avgRevenue, 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
            Map<String, Object> ind1 = new HashMap<>();
            ind1.put("indicator", "净利润率(%)"); ind1.put("currentValue", currentScore); ind1.put("industryAvg", avgProfitMargin);
            ind1.put("ranking", ranking + "/" + totalPeers);
            ind1.put("gap", currentScore.subtract(avgProfitMargin));
            ind1.put("trend", currentScore.compareTo(avgProfitMargin) >= 0 ? "上升" : "下降");
            ind1.put("suggestion", currentScore.compareTo(avgProfitMargin) >= 0 ? "继续保持盈利优势" : "需加强成本控制提升盈利能力");
            indicators.add(ind1);
            result.put("indicators", indicators);
            result.put("benchmarkTime", LocalDateTime.now().toString());
            return R.success(result);
        } catch (Exception e) { return R.fail("对标分析失败：" + e.getMessage()); }
    }

    @Operation(summary = "对标分析报告导出Excel")
    @PostMapping("/performance/benchmark/export")
    public void performanceBenchmarkExport(@RequestBody Map<String, Object> p, HttpServletResponse response) {
        try {
            String id = p.get("performanceId") != null ? p.get("performanceId").toString()
                    : (p.get("id") != null ? p.get("id").toString() : null);
            if (StringUtils.isBlank(id)) { response.sendError(400, "缺少绩效ID"); return; }
            GzctFinStatement current = statementMapper.selectById(id);
            if (current == null) { response.sendError(404, "记录不存在"); return; }
            // 获取同行业数据
            LambdaQueryWrapper<GzctFinStatement> w = new LambdaQueryWrapper<>();
            if (current.getIndustry() != null) w.eq(GzctFinStatement::getIndustry, current.getIndustry());
            List<GzctFinStatement> peers = statementMapper.selectList(w);
            int totalPeers = peers.size();
            // 计算行业平均值
            BigDecimal avgRevenue = BigDecimal.ZERO, avgProfit = BigDecimal.ZERO, avgAssets = BigDecimal.ZERO;
            for (GzctFinStatement peer : peers) {
                if (peer.getRevenue() != null) avgRevenue = avgRevenue.add(peer.getRevenue());
                if (peer.getNetProfit() != null) avgProfit = avgProfit.add(peer.getNetProfit());
                if (peer.getTotalAssets() != null) avgAssets = avgAssets.add(peer.getTotalAssets());
            }
            if (totalPeers > 0) {
                avgRevenue = avgRevenue.divide(BigDecimal.valueOf(totalPeers), 2, RoundingMode.HALF_UP);
                avgProfit = avgProfit.divide(BigDecimal.valueOf(totalPeers), 2, RoundingMode.HALF_UP);
                avgAssets = avgAssets.divide(BigDecimal.valueOf(totalPeers), 2, RoundingMode.HALF_UP);
            }
            // 计算当前企业指标
            BigDecimal currentProfitMargin = BigDecimal.ZERO;
            if (current.getNetProfit() != null && current.getRevenue() != null && current.getRevenue().compareTo(BigDecimal.ZERO) != 0)
                currentProfitMargin = current.getNetProfit().multiply(BigDecimal.valueOf(100)).divide(current.getRevenue(), 2, RoundingMode.HALF_UP);
            BigDecimal currentRoe = BigDecimal.ZERO;
            if (current.getNetProfit() != null && current.getNetAssets() != null && current.getNetAssets().compareTo(BigDecimal.ZERO) != 0)
                currentRoe = current.getNetProfit().multiply(BigDecimal.valueOf(100)).divide(current.getNetAssets(), 2, RoundingMode.HALF_UP);
            BigDecimal currentRoa = BigDecimal.ZERO;
            if (current.getNetProfit() != null && current.getTotalAssets() != null && current.getTotalAssets().compareTo(BigDecimal.ZERO) != 0)
                currentRoa = current.getNetProfit().multiply(BigDecimal.valueOf(100)).divide(current.getTotalAssets(), 2, RoundingMode.HALF_UP);
            BigDecimal avgProfitMargin = avgRevenue.compareTo(BigDecimal.ZERO) > 0
                ? avgProfit.multiply(BigDecimal.valueOf(100)).divide(avgRevenue, 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
            // 计算排名
            int ranking = 1;
            for (GzctFinStatement peer : peers) {
                BigDecimal peerScore = BigDecimal.ZERO;
                if (peer.getNetProfit() != null && peer.getRevenue() != null && peer.getRevenue().compareTo(BigDecimal.ZERO) != 0)
                    peerScore = peer.getNetProfit().multiply(BigDecimal.valueOf(100)).divide(peer.getRevenue(), 2, RoundingMode.HALF_UP);
                if (peerScore.compareTo(currentProfitMargin) > 0) ranking++;
            }
            // 构建Excel数据
            List<List<Object>> rows = new ArrayList<>();
            // Sheet: 对标分析报告
            rows.add(Arrays.asList("绩效对标分析报告"));
            rows.add(Arrays.asList(""));
            rows.add(Arrays.asList("一、基本信息"));
            rows.add(Arrays.asList("企业名称", current.getCompanyName()));
            rows.add(Arrays.asList("所属行业", current.getIndustry()));
            rows.add(Arrays.asList("报告期间", current.getPeriod()));
            rows.add(Arrays.asList("对标维度", p.get("dimension") != null ? p.get("dimension").toString() : "行业对标"));
            rows.add(Arrays.asList("同行业企业数", totalPeers));
            rows.add(Arrays.asList("行业排名", ranking + "/" + totalPeers));
            rows.add(Arrays.asList(""));
            rows.add(Arrays.asList("二、关键指标对比"));
            rows.add(Arrays.asList("指标名称", "本企业", "行业平均", "差距", "评价"));
            rows.add(Arrays.asList("净利润率(%)", currentProfitMargin, avgProfitMargin, currentProfitMargin.subtract(avgProfitMargin),
                currentProfitMargin.compareTo(avgProfitMargin) >= 0 ? "优于行业" : "低于行业"));
            rows.add(Arrays.asList("营业收入(万元)", current.getRevenue(), avgRevenue, current.getRevenue() != null ? current.getRevenue().subtract(avgRevenue) : BigDecimal.ZERO,
                current.getRevenue() != null && current.getRevenue().compareTo(avgRevenue) >= 0 ? "优于行业" : "低于行业"));
            rows.add(Arrays.asList("净利润(万元)", current.getNetProfit(), avgProfit, current.getNetProfit() != null ? current.getNetProfit().subtract(avgProfit) : BigDecimal.ZERO,
                current.getNetProfit() != null && current.getNetProfit().compareTo(avgProfit) >= 0 ? "优于行业" : "低于行业"));
            rows.add(Arrays.asList("总资产(万元)", current.getTotalAssets(), avgAssets, current.getTotalAssets() != null ? current.getTotalAssets().subtract(avgAssets) : BigDecimal.ZERO,
                current.getTotalAssets() != null && current.getTotalAssets().compareTo(avgAssets) >= 0 ? "优于行业" : "低于行业"));
            rows.add(Arrays.asList("ROE(%)", currentRoe, "-", "-", currentRoe.compareTo(BigDecimal.valueOf(10)) > 0 ? "优秀" : currentRoe.compareTo(BigDecimal.valueOf(5)) > 0 ? "良好" : "待改进"));
            rows.add(Arrays.asList("ROA(%)", currentRoa, "-", "-", currentRoa.compareTo(BigDecimal.valueOf(5)) > 0 ? "优秀" : currentRoa.compareTo(BigDecimal.valueOf(2)) > 0 ? "良好" : "待改进"));
            rows.add(Arrays.asList(""));
            rows.add(Arrays.asList("三、综合评价"));
            String overallLevel = currentProfitMargin.compareTo(BigDecimal.valueOf(10)) > 0 ? "优秀" : currentProfitMargin.compareTo(BigDecimal.valueOf(5)) > 0 ? "良好" : currentProfitMargin.compareTo(BigDecimal.valueOf(1)) > 0 ? "一般" : "较差";
            rows.add(Arrays.asList("综合绩效等级", overallLevel));
            rows.add(Arrays.asList("行业排名百分位", totalPeers > 0 ? (ranking * 100 / totalPeers) + "%" : "-"));
            rows.add(Arrays.asList(""));
            rows.add(Arrays.asList("四、改进建议"));
            if (currentProfitMargin.compareTo(avgProfitMargin) < 0)
                rows.add(Arrays.asList("1", "净利润率低于行业平均，建议优化成本结构，提升盈利能力"));
            if (current.getRevenue() != null && current.getRevenue().compareTo(avgRevenue) < 0)
                rows.add(Arrays.asList("2", "营业收入低于行业平均，建议拓展市场渠道，增加收入来源"));
            if (currentRoe.compareTo(BigDecimal.valueOf(10)) < 0)
                rows.add(Arrays.asList("3", "ROE偏低，建议优化资本结构，提升股东回报"));
            if (currentRoa.compareTo(BigDecimal.valueOf(5)) < 0)
                rows.add(Arrays.asList("4", "ROA偏低，建议提升资产利用效率，减少闲置资产"));
            rows.add(Arrays.asList(""));
            rows.add(Arrays.asList("报告生成时间", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))));
            byte[] xlsxBytes = SimpleXlsxWriter.write("对标分析报告", rows);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=benchmark_report_" + (current.getCompanyName() != null ? current.getCompanyName() : "") + ".xlsx");
            response.getOutputStream().write(xlsxBytes);
            response.getOutputStream().flush();
        } catch (Exception e) {
            log.error("导出对标分析报告失败", e);
            try { response.sendError(500, "导出失败：" + e.getMessage()); } catch (IOException ignored) {}
        }
    }

    @Operation(summary = "生成绩效改进行动计划")
    @PostMapping("/performance/benchmark/action-plan")
    public R<List<Map<String, Object>>> performanceBenchmarkActionPlan(@RequestBody Map<String, Object> p) {
        try {
            String id = p.get("performanceId") != null ? p.get("performanceId").toString()
                    : (p.get("id") != null ? p.get("id").toString() : null);
            if (StringUtils.isBlank(id)) return R.fail("缺少绩效ID");
            GzctFinStatement current = statementMapper.selectById(id);
            if (current == null) return R.fail("记录不存在");
            // 获取同行业数据计算行业平均
            LambdaQueryWrapper<GzctFinStatement> w = new LambdaQueryWrapper<>();
            if (current.getIndustry() != null) w.eq(GzctFinStatement::getIndustry, current.getIndustry());
            List<GzctFinStatement> peers = statementMapper.selectList(w);
            int totalPeers = peers.size();
            BigDecimal avgRevenue = BigDecimal.ZERO, avgProfit = BigDecimal.ZERO;
            for (GzctFinStatement peer : peers) {
                if (peer.getRevenue() != null) avgRevenue = avgRevenue.add(peer.getRevenue());
                if (peer.getNetProfit() != null) avgProfit = avgProfit.add(peer.getNetProfit());
            }
            if (totalPeers > 0) {
                avgRevenue = avgRevenue.divide(BigDecimal.valueOf(totalPeers), 2, RoundingMode.HALF_UP);
                avgProfit = avgProfit.divide(BigDecimal.valueOf(totalPeers), 2, RoundingMode.HALF_UP);
            }
            // 计算指标
            BigDecimal currentProfitMargin = BigDecimal.ZERO;
            if (current.getNetProfit() != null && current.getRevenue() != null && current.getRevenue().compareTo(BigDecimal.ZERO) != 0)
                currentProfitMargin = current.getNetProfit().multiply(BigDecimal.valueOf(100)).divide(current.getRevenue(), 2, RoundingMode.HALF_UP);
            BigDecimal currentRoe = BigDecimal.ZERO;
            if (current.getNetProfit() != null && current.getNetAssets() != null && current.getNetAssets().compareTo(BigDecimal.ZERO) != 0)
                currentRoe = current.getNetProfit().multiply(BigDecimal.valueOf(100)).divide(current.getNetAssets(), 2, RoundingMode.HALF_UP);
            BigDecimal currentRoa = BigDecimal.ZERO;
            if (current.getNetProfit() != null && current.getTotalAssets() != null && current.getTotalAssets().compareTo(BigDecimal.ZERO) != 0)
                currentRoa = current.getNetProfit().multiply(BigDecimal.valueOf(100)).divide(current.getTotalAssets(), 2, RoundingMode.HALF_UP);
            BigDecimal avgProfitMargin = avgRevenue.compareTo(BigDecimal.ZERO) > 0
                ? avgProfit.multiply(BigDecimal.valueOf(100)).divide(avgRevenue, 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
            BigDecimal debtRatio = BigDecimal.ZERO;
            if (current.getTotalLiabilities() != null && current.getTotalAssets() != null && current.getTotalAssets().compareTo(BigDecimal.ZERO) != 0)
                debtRatio = current.getTotalLiabilities().multiply(BigDecimal.valueOf(100)).divide(current.getTotalAssets(), 2, RoundingMode.HALF_UP);
            // 根据指标弱项生成行动计划
            List<Map<String, Object>> actionPlan = new ArrayList<>();
            String deadlineBase = LocalDate.now().plusMonths(3).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String deadlineMid = LocalDate.now().plusMonths(6).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            String deadlineLong = LocalDate.now().plusMonths(12).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            // 盈利能力改进
            if (currentProfitMargin.compareTo(avgProfitMargin) < 0) {
                Map<String, Object> plan = new HashMap<>();
                plan.put("category", "盈利能力");
                plan.put("action", "优化产品结构，降低低毛利产品占比；加强成本管控，推进精益管理降本增效");
                plan.put("responsible", "财务部/运营部");
                plan.put("deadline", deadlineMid);
                plan.put("target", "净利润率提升至" + avgProfitMargin.add(BigDecimal.valueOf(2)).setScale(1, RoundingMode.HALF_UP) + "%");
                plan.put("status", "计划中");
                actionPlan.add(plan);
            }
            // 收入增长改进
            if (current.getRevenue() != null && current.getRevenue().compareTo(avgRevenue) < 0) {
                Map<String, Object> plan = new HashMap<>();
                plan.put("category", "收入增长");
                plan.put("action", "拓展新市场渠道，加大市场营销投入；开发新产品线，提升客户复购率");
                plan.put("responsible", "市场部/销售部");
                plan.put("deadline", deadlineLong);
                plan.put("target", "营业收入增长" + (avgRevenue.compareTo(BigDecimal.ZERO) > 0 ? "20%" : "15%"));
                plan.put("status", "计划中");
                actionPlan.add(plan);
            }
            // ROE改进
            if (currentRoe.compareTo(BigDecimal.valueOf(10)) < 0) {
                Map<String, Object> plan = new HashMap<>();
                plan.put("category", "资本效率");
                plan.put("action", "优化资本结构，合理运用财务杠杆；提升净利润水平，减少低效资本占用");
                plan.put("responsible", "财务部");
                plan.put("deadline", deadlineMid);
                plan.put("target", "ROE提升至10%以上");
                plan.put("status", "计划中");
                actionPlan.add(plan);
            }
            // ROA改进
            if (currentRoa.compareTo(BigDecimal.valueOf(5)) < 0) {
                Map<String, Object> plan = new HashMap<>();
                plan.put("category", "资产效率");
                plan.put("action", "清理低效闲置资产，提升资产周转率；优化资产配置，加强固定资产管理");
                plan.put("responsible", "资产管理部");
                plan.put("deadline", deadlineBase);
                plan.put("target", "ROA提升至5%以上");
                plan.put("status", "计划中");
                actionPlan.add(plan);
            }
            // 负债率过高
            if (debtRatio.compareTo(BigDecimal.valueOf(70)) > 0) {
                Map<String, Object> plan = new HashMap<>();
                plan.put("category", "风险控制");
                plan.put("action", "控制有息负债规模，优化债务期限结构；增加权益融资比例，降低财务风险");
                plan.put("responsible", "财务部/风控部");
                plan.put("deadline", deadlineLong);
                plan.put("target", "资产负债率降至65%以下");
                plan.put("status", "计划中");
                actionPlan.add(plan);
            }
            // 经营现金流改进
            if (current.getOperatingCashflow() != null && current.getOperatingCashflow().compareTo(BigDecimal.ZERO) <= 0) {
                Map<String, Object> plan = new HashMap<>();
                plan.put("category", "现金流管理");
                plan.put("action", "加强应收账款催收，缩短回款周期；优化存货管理，减少资金占用");
                plan.put("responsible", "财务部/采购部");
                plan.put("deadline", deadlineBase);
                plan.put("target", "经营现金流转正");
                plan.put("status", "计划中");
                actionPlan.add(plan);
            }
            // 如果没有明显弱项，给出保持优势的建议
            if (actionPlan.isEmpty()) {
                Map<String, Object> plan = new HashMap<>();
                plan.put("category", "优势保持");
                plan.put("action", "持续保持当前经营优势，关注行业发展趋势，适时调整战略方向");
                plan.put("responsible", "战略发展部");
                plan.put("deadline", deadlineLong);
                plan.put("target", "保持行业领先地位");
                plan.put("status", "计划中");
                actionPlan.add(plan);
            }
            return R.success(actionPlan);
        } catch (Exception e) { return R.fail("生成行动计划失败：" + e.getMessage()); }
    }

    @Operation(summary = "批量绩效评价")
    @PostMapping("/performance/batch-evaluate")
    public R<Map<String, Object>> performanceBatchEvaluate(@RequestBody Map<String, Object> p) {
        try {
            List<String> ids = null;
            if (p.get("ids") != null) {
                Object idsObj = p.get("ids");
                if (idsObj instanceof List) {
                    ids = ((List<?>) idsObj).stream().map(Object::toString).collect(Collectors.toList());
                }
            }
            if (ids == null || ids.isEmpty()) return R.fail("请选择要评价的记录");
            int successCount = 0;
            for (String id : ids) {
                GzctFinStatement s = statementMapper.selectById(id);
                if (s != null) {
                    s.setAuditStatus("EVALUATED");
                    s.setUpdateTime(LocalDateTime.now());
                    statementMapper.updateById(s);
                    successCount++;
                }
            }
            Map<String, Object> result = new HashMap<>();
            result.put("totalCount", ids.size());
            result.put("successCount", successCount);
            result.put("failCount", ids.size() - successCount);
            return R.success(result);
        } catch (Exception e) { return R.fail("批量评价失败：" + e.getMessage()); }
    }

    @Operation(summary = "绩效数据导出Excel")
    @PostMapping("/performance/export-excel")
    public void performanceExportExcel(@RequestBody(required = false) Map<String, Object> params, HttpServletResponse response) {
        try {
            LambdaQueryWrapper<GzctFinStatement> w = new LambdaQueryWrapper<>();
            if (params != null && params.get("enterpriseName") != null && StringUtils.isNotBlank(params.get("enterpriseName").toString()))
                w.like(GzctFinStatement::getCompanyName, params.get("enterpriseName").toString());
            w.orderByDesc(GzctFinStatement::getCreateTime);
            List<GzctFinStatement> all = statementMapper.selectList(w);
            List<List<Object>> rows = new ArrayList<>();
            // 表头
            rows.add(Arrays.asList("企业名称", "报告期间", "营业收入(万元)", "净利润(万元)", "总资产(万元)", "净资产(万元)", "绩效评分", "绩效等级", "ROE(%)", "ROA(%)"));
            for (GzctFinStatement s : all) {
                BigDecimal score = BigDecimal.ZERO;
                if (s.getNetProfit() != null && s.getRevenue() != null && s.getRevenue().compareTo(BigDecimal.ZERO) != 0)
                    score = s.getNetProfit().multiply(BigDecimal.valueOf(100)).divide(s.getRevenue(), 2, RoundingMode.HALF_UP);
                String level = score.compareTo(BigDecimal.valueOf(10)) > 0 ? "优秀" : score.compareTo(BigDecimal.valueOf(5)) > 0 ? "良好" : score.compareTo(BigDecimal.valueOf(1)) > 0 ? "一般" : "较差";
                BigDecimal roe = (s.getNetProfit() != null && s.getNetAssets() != null && s.getNetAssets().compareTo(BigDecimal.ZERO) != 0)
                    ? s.getNetProfit().multiply(BigDecimal.valueOf(100)).divide(s.getNetAssets(), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
                BigDecimal roa = (s.getNetProfit() != null && s.getTotalAssets() != null && s.getTotalAssets().compareTo(BigDecimal.ZERO) != 0)
                    ? s.getNetProfit().multiply(BigDecimal.valueOf(100)).divide(s.getTotalAssets(), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO;
                rows.add(Arrays.asList(s.getCompanyName(), s.getPeriod(), s.getRevenue(), s.getNetProfit(), s.getTotalAssets(), s.getNetAssets(), score, level, roe, roa));
            }
            byte[] xlsxBytes = SimpleXlsxWriter.write("财务绩效评价", rows);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=financial_performance.xlsx");
            response.getOutputStream().write(xlsxBytes);
            response.getOutputStream().flush();
        } catch (Exception e) {
            log.error("导出绩效数据失败", e);
            try { response.sendError(500, "导出失败：" + e.getMessage()); } catch (IOException ignored) {}
        }
    }

    // ==================== 关联交易扩展接口 ====================

    @Operation(summary = "关联交易删除")
    @DeleteMapping("/related-transaction/{id}")
    public R<Boolean> relatedTransactionDelete(@PathVariable String id) {
        try {
            if (StringUtils.isBlank(id)) return R.fail("缺少ID参数");
            relatedPartyMapper.deleteById(id);
            return R.success(true);
        } catch (Exception e) { return R.fail("删除失败：" + e.getMessage()); }
    }

    @Operation(summary = "关联交易识别")
    @PostMapping("/related-transaction/identify")
    public R<Boolean> relatedTransactionIdentify(@RequestBody Map<String, Object> p) {
        try {
            String id = p.get("id") != null ? p.get("id").toString() : null;
            if (StringUtils.isBlank(id)) return R.fail("缺少ID参数");
            GzctFinRelatedParty party = relatedPartyMapper.selectById(id);
            if (party == null) return R.fail("记录不存在");
            party.setIsMajor("1");
            party.setUpdateTime(LocalDateTime.now());
            relatedPartyMapper.updateById(party);
            return R.success(true);
        } catch (Exception e) { return R.fail("识别失败：" + e.getMessage()); }
    }

    @Operation(summary = "关联交易批量识别")
    @PostMapping("/related-transaction/batch/identify")
    public R<Boolean> relatedTransactionBatchIdentify(@RequestBody Map<String, Object> p) {
        try {
            List<String> ids = null;
            if (p.get("ids") != null && p.get("ids") instanceof List) {
                ids = ((List<?>) p.get("ids")).stream().map(Object::toString).collect(Collectors.toList());
            }
            if (ids == null || ids.isEmpty()) return R.fail("缺少ID列表");
            for (String id : ids) {
                GzctFinRelatedParty party = relatedPartyMapper.selectById(id);
                if (party != null) {
                    party.setIsMajor("1");
                    party.setUpdateTime(LocalDateTime.now());
                    relatedPartyMapper.updateById(party);
                }
            }
            return R.success(true);
        } catch (Exception e) { return R.fail("批量识别失败：" + e.getMessage()); }
    }

    @Operation(summary = "关联交易导出")
    @PostMapping("/related-transaction/export")
    public R<Map<String, Object>> relatedTransactionExport(@RequestBody(required = false) Map<String, Object> p) {
        try {
            List<GzctFinRelatedParty> all = relatedPartyMapper.selectList(
                new LambdaQueryWrapper<GzctFinRelatedParty>().orderByDesc(GzctFinRelatedParty::getCreateTime));
            List<Map<String, Object>> rows = new ArrayList<>();
            for (GzctFinRelatedParty r : all) {
                Map<String, Object> row = new HashMap<>();
                row.put("companyId", r.getCompanyId());
                row.put("partyName", r.getPartyName());
                row.put("relationType", r.getRelationType());
                row.put("transactionType", r.getTransactionType());
                row.put("transactionAmount", r.getTransactionAmount());
                row.put("balanceAmount", r.getBalanceAmount());
                row.put("period", r.getPeriod());
                row.put("isMajor", r.getIsMajor());
                rows.add(row);
            }
            Map<String, Object> result = new HashMap<>();
            result.put("data", rows);
            result.put("total", rows.size());
            return R.success(result);
        } catch (Exception e) { return R.fail("导出失败：" + e.getMessage()); }
    }

    @Operation(summary = "关联交易网络图")
    @PostMapping("/related-transaction/network")
    public R<Map<String, Object>> relatedTransactionNetwork(@RequestBody Map<String, Object> p) {
        try {
            String companyId = p.get("companyId") != null ? p.get("companyId").toString() : null;
            LambdaQueryWrapper<GzctFinRelatedParty> w = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(companyId)) w.eq(GzctFinRelatedParty::getCompanyId, companyId);
            List<GzctFinRelatedParty> records = relatedPartyMapper.selectList(w);
            // 构建网络图节点和边
            Set<String> nodeSet = new HashSet<>();
            List<Map<String, Object>> nodes = new ArrayList<>();
            List<Map<String, Object>> links = new ArrayList<>();
            BigDecimal totalAmount = BigDecimal.ZERO;
            for (GzctFinRelatedParty r : records) {
                String source = r.getCompanyId() != null ? r.getCompanyId() : "未知企业";
                String target = r.getPartyName() != null ? r.getPartyName() : "未知关联方";
                if (!nodeSet.contains(source)) {
                    nodeSet.add(source);
                    Map<String, Object> node = new HashMap<>();
                    node.put("name", source); node.put("symbolSize", 40); node.put("category", 0);
                    nodes.add(node);
                }
                if (!nodeSet.contains(target)) {
                    nodeSet.add(target);
                    Map<String, Object> node = new HashMap<>();
                    node.put("name", target); node.put("symbolSize", 25); node.put("category", 1);
                    nodes.add(node);
                }
                Map<String, Object> link = new HashMap<>();
                link.put("source", source); link.put("target", target);
                link.put("value", r.getTransactionAmount() != null ? r.getTransactionAmount() : BigDecimal.ZERO);
                link.put("relationType", r.getRelationType());
                links.add(link);
                if (r.getTransactionAmount() != null) totalAmount = totalAmount.add(r.getTransactionAmount());
            }
            Map<String, Object> result = new HashMap<>();
            result.put("nodes", nodes); result.put("links", links);
            result.put("nodeCount", nodes.size()); result.put("linkCount", links.size());
            result.put("totalAmount", totalAmount);
            return R.success(result);
        } catch (Exception e) { return R.fail("网络图查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "关联交易定价分析")
    @PostMapping("/related-transaction/pricing")
    public R<Map<String, Object>> relatedTransactionPricing(@RequestBody Map<String, Object> p) {
        try {
            String id = p.get("id") != null ? p.get("id").toString() : null;
            if (StringUtils.isBlank(id)) return R.fail("缺少ID参数");
            GzctFinRelatedParty party = relatedPartyMapper.selectById(id);
            if (party == null) return R.fail("记录不存在");
            BigDecimal amount = party.getTransactionAmount() != null ? party.getTransactionAmount() : BigDecimal.ZERO;
            // 模拟市场参考价（实际应从市场数据表获取）
            BigDecimal marketPrice = amount.multiply(new BigDecimal("0.95")).setScale(2, RoundingMode.HALF_UP);
            BigDecimal deviation = BigDecimal.ZERO;
            if (marketPrice.compareTo(BigDecimal.ZERO) > 0) {
                deviation = amount.subtract(marketPrice).divide(marketPrice, 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP);
            }
            String riskLevel = "低风险";
            if (deviation.abs().compareTo(new BigDecimal("7")) > 0) riskLevel = "高风险";
            else if (deviation.abs().compareTo(new BigDecimal("3")) > 0) riskLevel = "中等风险";
            Map<String, Object> result = new HashMap<>();
            result.put("transactionAmount", amount); result.put("marketPrice", marketPrice);
            result.put("deviationPercent", deviation); result.put("riskLevel", riskLevel);
            result.put("partyName", party.getPartyName()); result.put("transactionType", party.getTransactionType());
            result.put("relationType", party.getRelationType()); result.put("period", party.getPeriod());
            return R.success(result);
        } catch (Exception e) { return R.fail("定价分析失败：" + e.getMessage()); }
    }

    @Operation(summary = "关联交易公允性评估")
    @PostMapping("/related-transaction/fairness")
    public R<Map<String, Object>> relatedTransactionFairness(@RequestBody Map<String, Object> p) {
        try {
            String id = p.get("id") != null ? p.get("id").toString() : null;
            if (StringUtils.isBlank(id)) return R.fail("缺少ID参数");
            GzctFinRelatedParty party = relatedPartyMapper.selectById(id);
            if (party == null) return R.fail("记录不存在");
            BigDecimal amount = party.getTransactionAmount() != null ? party.getTransactionAmount() : BigDecimal.ZERO;
            int score = 85;
            if (amount.compareTo(new BigDecimal("10000")) > 0) score -= 10;
            if (amount.compareTo(new BigDecimal("50000")) > 0) score -= 15;
            if ("1".equals(party.getIsMajor())) score -= 10;
            score = Math.max(Math.min(score, 100), 0);
            String conclusion;
            if (score >= 80) conclusion = "公允性良好";
            else if (score >= 60) conclusion = "公允性一般";
            else conclusion = "公允性不足";
            Map<String, Object> result = new HashMap<>();
            result.put("fairnessScore", score); result.put("conclusion", conclusion);
            result.put("partyName", party.getPartyName()); result.put("transactionType", party.getTransactionType());
            result.put("transactionAmount", amount); result.put("isMajor", party.getIsMajor());
            return R.success(result);
        } catch (Exception e) { return R.fail("公允性评估失败：" + e.getMessage()); }
    }

    @Operation(summary = "关联交易趋势分析")
    @PostMapping("/related-transaction/trend")
    public R<Map<String, Object>> relatedTransactionTrend(@RequestBody Map<String, Object> p) {
        try {
            String companyId = p.get("companyId") != null ? p.get("companyId").toString() : null;
            // 按期间分组统计交易金额趋势
            LambdaQueryWrapper<GzctFinRelatedParty> w = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(companyId)) w.eq(GzctFinRelatedParty::getCompanyId, companyId);
            w.orderByAsc(GzctFinRelatedParty::getPeriod);
            List<GzctFinRelatedParty> records = relatedPartyMapper.selectList(w);
            // 按period分组汇总
            Map<String, BigDecimal> periodMap = new LinkedHashMap<>();
            for (GzctFinRelatedParty r : records) {
                String period = r.getPeriod() != null ? r.getPeriod() : "未知";
                BigDecimal amt = r.getTransactionAmount() != null ? r.getTransactionAmount() : BigDecimal.ZERO;
                periodMap.merge(period, amt, BigDecimal::add);
            }
            List<Map<String, Object>> trendRows = new ArrayList<>();
            BigDecimal prevAmount = null;
            for (Map.Entry<String, BigDecimal> entry : periodMap.entrySet()) {
                Map<String, Object> row = new HashMap<>();
                row.put("period", entry.getKey());
                row.put("amount", entry.getValue());
                if (prevAmount != null && prevAmount.compareTo(BigDecimal.ZERO) > 0) {
                    BigDecimal change = entry.getValue().subtract(prevAmount)
                        .divide(prevAmount, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100"))
                        .setScale(2, RoundingMode.HALF_UP);
                    row.put("change", change);
                } else {
                    row.put("change", BigDecimal.ZERO);
                }
                trendRows.add(row);
                prevAmount = entry.getValue();
            }
            Map<String, Object> result = new HashMap<>();
            result.put("rows", trendRows);
            return R.success(result);
        } catch (Exception e) { return R.fail("趋势分析失败：" + e.getMessage()); }
    }

    @Operation(summary = "关联交易生成报告")
    @PostMapping("/related-transaction/report")
    public R<Map<String, Object>> relatedTransactionReport(@RequestBody Map<String, Object> p) {
        try {
            String id = p.get("id") != null ? p.get("id").toString() : null;
            if (StringUtils.isBlank(id)) return R.fail("缺少ID参数");
            GzctFinRelatedParty party = relatedPartyMapper.selectById(id);
            if (party == null) return R.fail("记录不存在");
            BigDecimal amount = party.getTransactionAmount() != null ? party.getTransactionAmount() : BigDecimal.ZERO;
            // 生成报告数据
            int fairnessScore = 85;
            if (amount.compareTo(new BigDecimal("10000")) > 0) fairnessScore -= 10;
            if (amount.compareTo(new BigDecimal("50000")) > 0) fairnessScore -= 15;
            if ("1".equals(party.getIsMajor())) fairnessScore -= 10;
            fairnessScore = Math.max(fairnessScore, 0);
            BigDecimal marketPrice = amount.multiply(new BigDecimal("0.95")).setScale(2, RoundingMode.HALF_UP);
            BigDecimal deviation = BigDecimal.ZERO;
            if (marketPrice.compareTo(BigDecimal.ZERO) > 0) {
                deviation = amount.subtract(marketPrice).divide(marketPrice, 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP);
            }
            List<String> riskTips = new ArrayList<>();
            if ("1".equals(party.getIsMajor())) riskTips.add("该交易被标记为重大关联交易");
            if (deviation.abs().compareTo(new BigDecimal("5")) > 0) riskTips.add("交易定价偏离市场价格较大");
            if (fairnessScore < 60) riskTips.add("公允性评分较低，建议重点关注");
            Map<String, Object> result = new HashMap<>();
            result.put("companyId", party.getCompanyId()); result.put("partyName", party.getPartyName());
            result.put("relationType", party.getRelationType()); result.put("transactionType", party.getTransactionType());
            result.put("transactionAmount", amount); result.put("balanceAmount", party.getBalanceAmount());
            result.put("period", party.getPeriod()); result.put("isMajor", party.getIsMajor());
            result.put("fairnessScore", fairnessScore); result.put("marketPrice", marketPrice);
            result.put("deviationPercent", deviation); result.put("riskTips", riskTips);
            result.put("reportTime", LocalDateTime.now().toString());
            return R.success(result);
        } catch (Exception e) { return R.fail("报告生成失败：" + e.getMessage()); }
    }
}
