package com.huabo.cybermonitor.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.cybermonitor.entity.*;
import com.huabo.cybermonitor.mapper.*;
import com.huabo.cybermonitor.util.ExcelUtil;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.util.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Tag(name = "金融风险穿透式监管", description = "金融风险穿透式监管全接口")
@RestController
@RequestMapping("/v1/supervision/financial-risk")
@Slf4j
public class FinancialRiskSupervisionController {

    @Autowired private GzctFinGuaranteeRecordMapper guaranteeMapper;
    @Autowired private GzctFinEntrustedLoanMapper loanMapper;
    @Autowired private GzctFinDerivativesMapper derivativesMapper;
    @Autowired private GzctFinAlertMapper alertMapper;
    @Autowired private GzctFinLiquidityMapper liquidityMapper;
    @Autowired private TblFinancingRecordMapper financingRecordMapper;
    @Autowired private TblGuaranteeRecordMapper guaranteeRecordMapper;
    @Autowired private com.huabo.cybermonitor.util.OrgQueryHelper orgQueryHelper;

    // ==================== 金融穿透统计(前端融资台账页面调用) ====================
    @Operation(summary = "金融风险统计(穿透)")
    @GetMapping("/financing/statistics")
    public R<Map<String, Object>> financingStatistics(@RequestParam(required = false) String companyId) {
        try {
            // 查询融资记录
            LambdaQueryWrapper<TblFinancingRecord> fw = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(companyId)) {
                fw.eq(TblFinancingRecord::getCompanyId, companyId);
            }
            List<TblFinancingRecord> financingList = financingRecordMapper.selectList(fw);

            Map<String, Object> stats = new HashMap<>();
            LocalDate today = LocalDate.now();

            // 1. 融资总额(万元) - 前端字段: totalAmount
            BigDecimal totalAmount = financingList.stream()
                .filter(f -> f.getFinancingAmount() != null)
                .map(TblFinancingRecord::getFinancingAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            stats.put("totalAmount", totalAmount);

            // 2. 平均利率(%) - 前端字段: avgRate
            BigDecimal avgRate = BigDecimal.ZERO;
            List<TblFinancingRecord> withRate = financingList.stream()
                .filter(f -> f.getInterestRate() != null && f.getInterestRate().compareTo(BigDecimal.ZERO) > 0)
                .collect(java.util.stream.Collectors.toList());
            if (!withRate.isEmpty()) {
                BigDecimal sumRate = withRate.stream().map(TblFinancingRecord::getInterestRate).reduce(BigDecimal.ZERO, BigDecimal::add);
                avgRate = sumRate.divide(new BigDecimal(withRate.size()), 2, java.math.RoundingMode.HALF_UP);
            }
            stats.put("avgRate", avgRate);

            // 3. 90天内到期金额(万元) - 前端字段: nearExpiryAmount
            LocalDate ninetyDaysLater = today.plusDays(90);
            BigDecimal nearExpiryAmount = financingList.stream()
                .filter(f -> f.getMaturityDate() != null && f.getFinancingAmount() != null)
                .filter(f -> !f.getMaturityDate().isBefore(today) && !f.getMaturityDate().isAfter(ninetyDaysLater))
                .filter(f -> !"REPAID".equals(f.getFinancingStatus()))
                .map(TblFinancingRecord::getFinancingAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            stats.put("nearExpiryAmount", nearExpiryAmount);

            // 4. 逾期融资金额(万元) - 前端字段: overdueAmount
            BigDecimal overdueAmount = financingList.stream()
                .filter(f -> "OVERDUE".equals(f.getFinancingStatus()) && f.getFinancingAmount() != null)
                .map(TblFinancingRecord::getFinancingAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            stats.put("overdueAmount", overdueAmount);

            // 5. 融资类型分布 - 前端字段: typeDistribution (Map<类型, 金额>)
            Map<String, BigDecimal> typeDistribution = new LinkedHashMap<>();
            for (TblFinancingRecord f : financingList) {
                if (f.getFinancingType() != null && f.getFinancingAmount() != null) {
                    typeDistribution.merge(f.getFinancingType(), f.getFinancingAmount(), BigDecimal::add);
                }
            }
            stats.put("typeDistribution", typeDistribution);

            // 6. 融资期限结构分布(亿元) - 前端字段: termDistribution [1年以内, 1-3年, 3-5年, 5年以上]
            BigDecimal[] termBuckets = {BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO};
            BigDecimal YI = new BigDecimal("10000"); // 万元转亿元
            for (TblFinancingRecord f : financingList) {
                if (f.getStartDate() == null || f.getMaturityDate() == null || f.getFinancingAmount() == null) continue;
                long months = java.time.temporal.ChronoUnit.MONTHS.between(f.getStartDate(), f.getMaturityDate());
                if (months <= 12) {
                    termBuckets[0] = termBuckets[0].add(f.getFinancingAmount());
                } else if (months <= 36) {
                    termBuckets[1] = termBuckets[1].add(f.getFinancingAmount());
                } else if (months <= 60) {
                    termBuckets[2] = termBuckets[2].add(f.getFinancingAmount());
                } else {
                    termBuckets[3] = termBuckets[3].add(f.getFinancingAmount());
                }
            }
            List<BigDecimal> termDistribution = new ArrayList<>();
            for (BigDecimal bucket : termBuckets) {
                termDistribution.add(bucket.divide(YI, 2, java.math.RoundingMode.HALF_UP));
            }
            stats.put("termDistribution", termDistribution);

            // 兼容 dashboard 总览页面所需字段
            stats.put("totalFinancing", totalAmount);
            stats.put("financingAmount", totalAmount);
            // 担保总额
            LambdaQueryWrapper<TblGuaranteeRecord> gw = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(companyId)) {
                gw.eq(TblGuaranteeRecord::getCompanyId, companyId);
            }
            List<TblGuaranteeRecord> guaranteeList = guaranteeRecordMapper.selectList(gw);
            BigDecimal totalGuarantee = guaranteeList.stream()
                .filter(g -> g.getGuaranteeAmount() != null)
                .map(TblGuaranteeRecord::getGuaranteeAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            stats.put("totalGuarantee", totalGuarantee);
            stats.put("guaranteeAmount", totalGuarantee);
            // 风险数量
            long riskCount = financingList.stream().filter(f -> "HIGH".equals(f.getRiskLevel())).count()
                + guaranteeList.stream().filter(g -> "HIGH".equals(g.getRiskLevel())).count();
            stats.put("riskCount", riskCount);
            stats.put("warningCount", riskCount);

            return R.success(stats);
        } catch (Exception e) {
            log.error("金融风险统计失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    // 注：融资记录(/financing/*)和担保记录(/financing/guarantee/*)已在FinancialRiskController中定义
    // 此处仅补充FinancialRiskController未覆盖的端点

    // ==================== 预警(前端也用warning路径) ====================

    @Operation(summary = "金融风险预警列表(warning路径)")
    @PostMapping("/warning/list")
    public R<PageResult<GzctFinAlert>> warningList(@RequestBody Map<String, Object> params) {
        return alertList(params);
    }

    @Operation(summary = "解除金融风险预警(warning路径)")
    @PostMapping("/warning/dismiss/{alertId}")
    public R<Boolean> warningDismiss(@PathVariable String alertId) {
        return dismissAlert(alertId);
    }

    @Operation(summary = "报告导出")
    @PostMapping("/report/export")
    public R<Map<String, Object>> reportExport(@RequestBody Map<String, Object> params) {
        try { Map<String, Object> r = new HashMap<>(); r.put("url", ""); return R.success(r); } catch (Exception e) { return R.fail("导出失败：" + e.getMessage()); }
    }

    // ==================== 委托贷款 ====================

    @Operation(summary = "委托贷款列表")
    @PostMapping("/entrusted-loan/list")
    public R<PageResult<GzctFinEntrustedLoan>> entrustedLoanList(@RequestBody Map<String, Object> params) {
        try {
            int pageNum = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctFinEntrustedLoan> wrapper = buildEntrustedLoanWrapper(params);
            wrapper.orderByDesc(GzctFinEntrustedLoan::getCreateTime);
            Page<GzctFinEntrustedLoan> page = new Page<>(pageNum, pageSize);
            Page<GzctFinEntrustedLoan> result = loanMapper.selectPage(page, wrapper);
            PageResult<GzctFinEntrustedLoan> pr = new PageResult<>();
            pr.setTotalRecord((int) result.getTotal()); pr.setCurrentPage((int) result.getCurrent());
            pr.setPageNumber((int) result.getCurrent()); pr.setTotalPage((int) result.getPages());
            pr.setPageSize((int) result.getSize()); pr.setTlist(result.getRecords());
            return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "委托贷款导出")
    @PostMapping("/entrusted-loan/export")
    public void exportEntrustedLoan(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        try {
            LambdaQueryWrapper<GzctFinEntrustedLoan> wrapper = buildEntrustedLoanWrapper(params);
            wrapper.orderByDesc(GzctFinEntrustedLoan::getCreateTime);
            List<GzctFinEntrustedLoan> list = loanMapper.selectList(wrapper);

            String[] titles = {"委托方", "借款方", "贷款金额(万元)", "利率(%)", "起始日期", "到期日期", "贷款用途", "状态", "风险等级", "关联方", "备注"};
            ExcelUtil excelUtil = new ExcelUtil("委托贷款台账", titles);
            int rowIndex = 1;
            for (GzctFinEntrustedLoan item : list) {
                Object[] rowData = {
                    item.getLenderName(),
                    item.getBorrowerName(),
                    item.getLoanAmount() != null ? item.getLoanAmount().toString() : "",
                    item.getInterestRate() != null ? item.getInterestRate().toString() : "",
                    item.getStartDate() != null ? item.getStartDate().toString() : "",
                    item.getEndDate() != null ? item.getEndDate().toString() : "",
                    item.getLoanPurpose(),
                    formatStatus(item.getStatus()),
                    formatRiskLevel(item.getRiskLevel()),
                    Boolean.TRUE.equals(item.getIsRelatedParty()) ? "是" : "否",
                    item.getRemark()
                };
                excelUtil.addRow(rowIndex++, rowData);
            }
            excelUtil.exportExcel(response, "委托贷款台账.xls");
        } catch (Exception e) {
            log.error("导出委托贷款失败", e);
        }
    }

    private LambdaQueryWrapper<GzctFinEntrustedLoan> buildEntrustedLoanWrapper(Map<String, Object> params) {
        LambdaQueryWrapper<GzctFinEntrustedLoan> wrapper = new LambdaQueryWrapper<>();
        if (params.get("borrowerName") != null && StringUtils.isNotEmpty(params.get("borrowerName").toString())) {
            wrapper.like(GzctFinEntrustedLoan::getBorrowerName, params.get("borrowerName").toString());
        }
        if (params.get("status") != null && StringUtils.isNotEmpty(params.get("status").toString())) {
            wrapper.eq(GzctFinEntrustedLoan::getStatus, params.get("status").toString());
        }
        if (params.get("riskLevel") != null && StringUtils.isNotEmpty(params.get("riskLevel").toString())) {
            wrapper.eq(GzctFinEntrustedLoan::getRiskLevel, params.get("riskLevel").toString());
        }
        return wrapper;
    }

    private String formatStatus(String status) {
        if (status == null) return "";
        switch (status) {
            case "ACTIVE": return "正常";
            case "OVERDUE": return "逾期";
            case "REPAID": return "已还清";
            case "DEFAULT": return "违约";
            default: return status;
        }
    }

    private String formatRiskLevel(String level) {
        if (level == null) return "";
        switch (level) {
            case "HIGH": return "高";
            case "MEDIUM": return "中";
            case "LOW": return "低";
            default: return level;
        }
    }

    private String formatProductType(String type) {
        if (type == null) return "";
        switch (type) {
            case "FUTURES": return "期货";
            case "OPTIONS": return "期权";
            case "SWAP": return "互换";
            case "FORWARD": return "远期";
            default: return type;
        }
    }

    private String formatDerivativesStatus(String status) {
        if (status == null) return "";
        switch (status) {
            case "ACTIVE": return "存续";
            case "MATURED": return "已到期";
            case "CLOSED": return "已平仓";
            default: return status;
        }
    }

    @Operation(summary = "委托贷款详情")
    @GetMapping("/entrusted-loan/{id}")
    public R<GzctFinEntrustedLoan> entrustedLoanDetail(@PathVariable String id) {
        try { return R.success(loanMapper.selectById(id)); } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "新增委托贷款")
    @PostMapping("/entrusted-loan/add")
    public R<Boolean> addEntrustedLoan(@RequestBody Map<String, Object> params) {
        try {
            GzctFinEntrustedLoan record = new GzctFinEntrustedLoan();
            if (params.get("lenderName") != null) record.setLenderName(params.get("lenderName").toString());
            if (params.get("borrowerName") != null) record.setBorrowerName(params.get("borrowerName").toString());
            if (params.get("loanAmount") != null && !params.get("loanAmount").toString().isEmpty()) {
                record.setLoanAmount(new java.math.BigDecimal(params.get("loanAmount").toString()));
            }
            if (params.get("interestRate") != null && !params.get("interestRate").toString().isEmpty()) {
                record.setInterestRate(new java.math.BigDecimal(params.get("interestRate").toString()));
            }
            if (params.get("startDate") != null && !params.get("startDate").toString().isEmpty()) {
                record.setStartDate(java.time.LocalDate.parse(params.get("startDate").toString()).atStartOfDay());
            }
            if (params.get("endDate") != null && !params.get("endDate").toString().isEmpty()) {
                record.setEndDate(java.time.LocalDate.parse(params.get("endDate").toString()).atStartOfDay());
            }
            if (params.get("loanPurpose") != null) record.setLoanPurpose(params.get("loanPurpose").toString());
            if (params.get("repaymentMethod") != null) record.setRepaymentMethod(params.get("repaymentMethod").toString());
            if (params.get("guaranteeMeasures") != null) record.setGuaranteeMeasures(params.get("guaranteeMeasures").toString());
            if (params.get("remark") != null) record.setRemark(params.get("remark").toString());
            // isRelatedParty: 前端发boolean，转Integer
            Object relatedParty = params.get("isRelatedParty");
            if (relatedParty != null) {
                if (relatedParty instanceof Boolean) {
                    record.setIsRelatedParty((Boolean) relatedParty ? 1 : 0);
                } else {
                    record.setIsRelatedParty("true".equalsIgnoreCase(relatedParty.toString()) || "1".equals(relatedParty.toString()) ? 1 : 0);
                }
            }
            record.setStatus("ACTIVE");
            record.setRiskLevel("LOW");
            record.setCreateTime(LocalDateTime.now());
            loanMapper.insert(record);
            return R.success(true);
        } catch (Exception e) {
            log.error("新增委托贷款失败", e);
            return R.fail("新增失败：" + e.getMessage());
        }
    }

    @Operation(summary = "催收委托贷款")
    @PostMapping("/entrusted-loan/urge")
    public R<Boolean> urgeEntrustedLoan(@RequestBody Map<String, Object> params) {
        try {
            String id = params.get("id") != null ? params.get("id").toString() : null;
            if (id == null) return R.fail("记录ID不能为空");
            GzctFinEntrustedLoan record = loanMapper.selectById(id);
            if (record == null) return R.fail("记录不存在");
            record.setIsOverdue("URGED");
            record.setUpdateTime(LocalDateTime.now());
            loanMapper.updateById(record);
            return R.success(true);
        } catch (Exception e) {
            log.error("催收委托贷款失败", e);
            return R.fail("催收失败：" + e.getMessage());
        }
    }

    @Operation(summary = "更新委托贷款")
    @PostMapping("/entrusted-loan/update")
    public R<Boolean> updateEntrustedLoan(@RequestBody GzctFinEntrustedLoan record) {
        try { record.setUpdateTime(LocalDateTime.now()); loanMapper.updateById(record); return R.success(true); } catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); }
    }

    @Operation(summary = "删除委托贷款")
    @DeleteMapping("/entrusted-loan/{id}")
    public R<Boolean> deleteEntrustedLoan(@PathVariable String id) {
        try { return R.success(loanMapper.deleteById(id) > 0); } catch (Exception e) { return R.fail("删除失败：" + e.getMessage()); }
    }

    // ==================== 衍生品业务 ====================

    @Operation(summary = "衍生品业务列表")
    @PostMapping("/derivatives/list")
    public R<PageResult<GzctFinDerivatives>> derivativesList(@RequestBody Map<String, Object> params) {
        try {
            int pageNum = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctFinDerivatives> wrapper = buildDerivativesWrapper(params);
            wrapper.orderByDesc(GzctFinDerivatives::getCreateTime);
            Page<GzctFinDerivatives> page = new Page<>(pageNum, pageSize);
            Page<GzctFinDerivatives> result = derivativesMapper.selectPage(page, wrapper);
            PageResult<GzctFinDerivatives> pr = new PageResult<>();
            pr.setTotalRecord((int) result.getTotal()); pr.setCurrentPage((int) result.getCurrent());
            pr.setPageNumber((int) result.getCurrent()); pr.setTotalPage((int) result.getPages());
            pr.setPageSize((int) result.getSize()); pr.setTlist(result.getRecords());
            return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    private LambdaQueryWrapper<GzctFinDerivatives> buildDerivativesWrapper(Map<String, Object> params) {
        LambdaQueryWrapper<GzctFinDerivatives> wrapper = new LambdaQueryWrapper<>();
        if (params.get("companyName") != null && StringUtils.isNotEmpty(params.get("companyName").toString())) {
            wrapper.like(GzctFinDerivatives::getCompanyName, params.get("companyName").toString());
        }
        if (params.get("productType") != null && StringUtils.isNotEmpty(params.get("productType").toString())) {
            wrapper.eq(GzctFinDerivatives::getProductType, params.get("productType").toString());
        }
        if (params.get("riskLevel") != null && StringUtils.isNotEmpty(params.get("riskLevel").toString())) {
            wrapper.eq(GzctFinDerivatives::getRiskLevel, params.get("riskLevel").toString());
        }
        return wrapper;
    }

    @Operation(summary = "衍生品业务详情")
    @GetMapping("/derivatives/{id}")
    public R<GzctFinDerivatives> derivativesDetail(@PathVariable String id) {
        try { return R.success(derivativesMapper.selectById(id)); } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "新增衍生品业务")
    @PostMapping("/derivatives/add")
    public R<Boolean> addDerivatives(@RequestBody GzctFinDerivatives record) {
        try { record.setCreateTime(LocalDateTime.now()); derivativesMapper.insert(record); return R.success(true); } catch (Exception e) { return R.fail("新增失败：" + e.getMessage()); }
    }

    @Operation(summary = "更新衍生品业务")
    @PostMapping("/derivatives/update")
    public R<Boolean> updateDerivatives(@RequestBody GzctFinDerivatives record) {
        try { record.setUpdateTime(LocalDateTime.now()); derivativesMapper.updateById(record); return R.success(true); } catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); }
    }

    @Operation(summary = "删除衍生品业务")
    @DeleteMapping("/derivatives/{id}")
    public R<Boolean> deleteDerivatives(@PathVariable String id) {
        try { return R.success(derivativesMapper.deleteById(id) > 0); } catch (Exception e) { return R.fail("删除失败：" + e.getMessage()); }
    }

    @Operation(summary = "衍生品业务导出")
    @PostMapping("/derivatives/export")
    public void exportDerivatives(@RequestBody Map<String, Object> params, HttpServletResponse response) {
        try {
            LambdaQueryWrapper<GzctFinDerivatives> wrapper = buildDerivativesWrapper(params);
            wrapper.orderByDesc(GzctFinDerivatives::getCreateTime);
            List<GzctFinDerivatives> list = derivativesMapper.selectList(wrapper);

            String[] titles = {"持仓企业", "产品名称", "品种", "名义金额(万元)", "市值(万元)", "浮动盈亏(万元)", "到期日", "对手方", "风险等级", "状态", "备注"};
            ExcelUtil excelUtil = new ExcelUtil("衍生品业务台账", titles);
            int rowIndex = 1;
            for (GzctFinDerivatives item : list) {
                Object[] rowData = {
                    item.getCompanyName(),
                    item.getProductName(),
                    formatProductType(item.getProductType()),
                    item.getNotionalAmount() != null ? item.getNotionalAmount().toString() : "",
                    item.getMarketValue() != null ? item.getMarketValue().toString() : "",
                    item.getProfitLoss() != null ? item.getProfitLoss().toString() : "",
                    item.getMaturityDate() != null ? item.getMaturityDate() : "",
                    item.getCounterparty(),
                    formatRiskLevel(item.getRiskLevel()),
                    formatDerivativesStatus(item.getStatus()),
                    item.getRemark()
                };
                excelUtil.addRow(rowIndex++, rowData);
            }
            excelUtil.exportExcel(response, "衍生品业务台账.xls");
        } catch (Exception e) {
            log.error("导出衍生品业务失败", e);
        }
    }

    // ==================== 流动性风险 ====================

    @Operation(summary = "流动性风险列表")
    @PostMapping("/liquidity/list")
    public R<PageResult<GzctFinLiquidity>> liquidityList(@RequestBody Map<String, Object> params) {
        try {
            int pageNum = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctFinLiquidity> wrapper = new LambdaQueryWrapper<>();
            if (params.get("companyName") != null && StringUtils.isNotEmpty(params.get("companyName").toString())) {
                wrapper.like(GzctFinLiquidity::getCompanyName, params.get("companyName").toString());
            }
            if (params.get("riskLevel") != null && StringUtils.isNotEmpty(params.get("riskLevel").toString())) {
                wrapper.eq(GzctFinLiquidity::getRiskLevel, params.get("riskLevel").toString());
            }
            wrapper.orderByDesc(GzctFinLiquidity::getCreateTime);
            Page<GzctFinLiquidity> page = new Page<>(pageNum, pageSize);
            Page<GzctFinLiquidity> result = liquidityMapper.selectPage(page, wrapper);
            PageResult<GzctFinLiquidity> pr = new PageResult<>();
            pr.setTotalRecord((int) result.getTotal()); pr.setCurrentPage((int) result.getCurrent());
            pr.setPageNumber((int) result.getCurrent()); pr.setTotalPage((int) result.getPages());
            pr.setPageSize((int) result.getSize()); pr.setTlist(result.getRecords());
            return R.success(pr);
        } catch (Exception e) { log.error("查询流动性风险列表失败", e); return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "流动性风险评分")
    @GetMapping("/liquidity/score")
    public R<Map<String, Object>> liquidityScore(@RequestParam(required = false) String companyId) {
        try {
            Map<String, Object> r = new HashMap<>();
            LambdaQueryWrapper<GzctFinLiquidity> wrapper = new LambdaQueryWrapper<>();
            String orgPattern = orgQueryHelper.getOrgPathPattern(companyId);
            if (orgPattern != null) {
                wrapper.like(GzctFinLiquidity::getOrgPath, orgPattern);
            }
            wrapper.orderByDesc(GzctFinLiquidity::getReportDate).last("LIMIT 1");
            List<GzctFinLiquidity> list = liquidityMapper.selectList(wrapper);
            if (!list.isEmpty()) {
                GzctFinLiquidity latest = list.get(0);
                r.put("score", latest.getLiquidityScore());
                r.put("level", latest.getRiskLevel());
                r.put("currentRatio", latest.getCurrentRatio());
                r.put("quickRatio", latest.getQuickRatio());
                r.put("cashRatio", latest.getCashRatio());
            } else {
                r.put("score", 0); r.put("level", "LOW");
            }
            return R.success(r);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "流动性压力测试")
    @PostMapping("/liquidity/stress-test")
    public R<List<Map<String, Object>>> liquidityStressTest(@RequestBody Map<String, Object> params) {
        try {
            // 查询所有流动性数据用于压力测试计算
            List<GzctFinLiquidity> allData = liquidityMapper.selectList(null);
            List<Map<String, Object>> scenarios = new ArrayList<>();

            // 计算集团整体指标
            double avgCurrentRatio = allData.stream()
                .filter(d -> d.getCurrentRatio() != null)
                .mapToDouble(d -> d.getCurrentRatio().doubleValue())
                .average().orElse(0.0);
            double totalCash = allData.stream()
                .filter(d -> d.getCashEquivalent() != null)
                .mapToDouble(d -> d.getCashEquivalent().doubleValue())
                .sum();
            double totalShortDebt = allData.stream()
                .filter(d -> d.getShortTermDebt() != null)
                .mapToDouble(d -> d.getShortTermDebt().doubleValue())
                .sum();
            double totalMaturity01y = allData.stream()
                .filter(d -> d.getMaturity01y() != null)
                .mapToDouble(d -> d.getMaturity01y().doubleValue())
                .sum();

            // 场景1：基准情景 - 正常经营条件
            Map<String, Object> scenario1 = new HashMap<>();
            scenario1.put("name", "基准情景");
            scenario1.put("desc", "正常经营条件下，维持当前融资节奏和现金流水平");
            double gap1 = totalCash - totalShortDebt * 0.3;
            scenario1.put("liquidityGap", String.format("%.0f", gap1));
            scenario1.put("level", gap1 >= 0 ? "LOW" : "MEDIUM");
            scenario1.put("result", gap1 >= 0 ? "通过" : "预警");
            scenarios.add(scenario1);

            // 场景2：轻度压力 - 融资收紧20%
            Map<String, Object> scenario2 = new HashMap<>();
            scenario2.put("name", "轻度压力");
            scenario2.put("desc", "银行授信收紧20%，短期融资到期无法续贷");
            double gap2 = totalCash - totalShortDebt * 0.5;
            scenario2.put("liquidityGap", String.format("%.0f", gap2));
            scenario2.put("level", gap2 >= 0 ? "LOW" : "MEDIUM");
            scenario2.put("result", gap2 >= 0 ? "通过" : "预警");
            scenarios.add(scenario2);

            // 场景3：中度压力 - 市场流动性紧缩
            Map<String, Object> scenario3 = new HashMap<>();
            scenario3.put("name", "中度压力");
            scenario3.put("desc", "市场流动性紧缩，50%短期债务到期需偿还，融资成本上升");
            double gap3 = totalCash - totalShortDebt * 0.7;
            scenario3.put("liquidityGap", String.format("%.0f", gap3));
            scenario3.put("level", gap3 >= 0 ? "MEDIUM" : "HIGH");
            scenario3.put("result", gap3 >= 0 ? "勉强通过" : "不通过");
            scenarios.add(scenario3);

            // 场景4：重度压力 - 极端流动性危机
            Map<String, Object> scenario4 = new HashMap<>();
            scenario4.put("name", "重度压力");
            scenario4.put("desc", "极端情景：全部短期债务到期偿还，无法获得新增融资");
            double gap4 = totalCash - totalShortDebt;
            scenario4.put("liquidityGap", String.format("%.0f", gap4));
            scenario4.put("level", "HIGH");
            scenario4.put("result", gap4 >= 0 ? "通过" : "不通过");
            scenarios.add(scenario4);

            return R.success(scenarios);
        } catch (Exception e) {
            log.error("流动性压力测试执行失败", e);
            return R.fail("压力测试执行失败：" + e.getMessage());
        }
    }

    @Operation(summary = "融资到期分布")
    @GetMapping("/liquidity/maturity-distribution")
    public R<Map<String, Object>> maturityDistribution(@RequestParam(required = false) String companyId) {
        try {
            LambdaQueryWrapper<GzctFinLiquidity> wrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(companyId)) {
                wrapper.eq(GzctFinLiquidity::getCompanyId, companyId);
            }
            List<GzctFinLiquidity> allData = liquidityMapper.selectList(wrapper);

            Map<String, Object> result = new HashMap<>();
            // 按季度分类：0-1年(Q1~Q4)、1-3年、3-5年、5年以上
            List<String> categories = Arrays.asList("Q1(0-3月)", "Q2(3-6月)", "Q3(6-9月)", "Q4(9-12月)");
            result.put("categories", categories);

            // 基于到期数据按比例分配到各季度（银行贷款、债券、委托贷款）
            double totalMaturity01y = allData.stream()
                .filter(d -> d.getMaturity01y() != null)
                .mapToDouble(d -> d.getMaturity01y().doubleValue())
                .sum();
            double totalMaturity13y = allData.stream()
                .filter(d -> d.getMaturity13y() != null)
                .mapToDouble(d -> d.getMaturity13y().doubleValue())
                .sum();
            double totalMaturity35y = allData.stream()
                .filter(d -> d.getMaturity35y() != null)
                .mapToDouble(d -> d.getMaturity35y().doubleValue())
                .sum();
            double totalMaturityAbove5y = allData.stream()
                .filter(d -> d.getMaturityAbove5y() != null)
                .mapToDouble(d -> d.getMaturityAbove5y().doubleValue())
                .sum();

            // 将0-1年到期金额按季度均分，转换为亿元
            double q1 = totalMaturity01y * 0.3 / 10000;
            double q2 = totalMaturity01y * 0.25 / 10000;
            double q3 = totalMaturity01y * 0.25 / 10000;
            double q4 = totalMaturity01y * 0.2 / 10000;

            // 银行贷款占比约60%
            List<Double> bankLoan = Arrays.asList(
                Math.round(q1 * 0.6 * 100.0) / 100.0,
                Math.round(q2 * 0.6 * 100.0) / 100.0,
                Math.round(q3 * 0.6 * 100.0) / 100.0,
                Math.round(q4 * 0.6 * 100.0) / 100.0
            );
            // 债券占比约25%
            List<Double> bond = Arrays.asList(
                Math.round(q1 * 0.25 * 100.0) / 100.0,
                Math.round(q2 * 0.25 * 100.0) / 100.0,
                Math.round(q3 * 0.25 * 100.0) / 100.0,
                Math.round(q4 * 0.25 * 100.0) / 100.0
            );
            // 委托贷款占比约15%
            List<Double> entrustLoan = Arrays.asList(
                Math.round(q1 * 0.15 * 100.0) / 100.0,
                Math.round(q2 * 0.15 * 100.0) / 100.0,
                Math.round(q3 * 0.15 * 100.0) / 100.0,
                Math.round(q4 * 0.15 * 100.0) / 100.0
            );

            result.put("bankLoan", bankLoan);
            result.put("bond", bond);
            result.put("entrustLoan", entrustLoan);

            return R.success(result);
        } catch (Exception e) {
            log.error("查询融资到期分布失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    // ==================== 金融风险穿透 ====================

    @Operation(summary = "金融风险穿透树")
    @PostMapping("/drill-down/tree")
    public R<Map<String, Object>> drillDownTree(@RequestBody Map<String, Object> params) {
        try {
            // 构建基于真实数据的穿透树
            Map<String, Object> root = new HashMap<>();
            root.put("name", "华博集团有限公司");
            root.put("key", "EP001");
            root.put("riskLevel", "LOW");
            root.put("level", 0);

            List<Map<String, Object>> children = new ArrayList<>();
            // 从流动性数据构建子节点
            List<GzctFinLiquidity> liquidityList = liquidityMapper.selectList(null);
            for (GzctFinLiquidity item : liquidityList) {
                if (!"EP001".equals(item.getCompanyId())) {
                    Map<String, Object> child = new HashMap<>();
                    child.put("name", item.getCompanyName());
                    child.put("key", item.getCompanyId());
                    child.put("riskLevel", item.getRiskLevel() != null ? item.getRiskLevel() : "LOW");
                    child.put("level", 1);

                    // 为每个子企业查询是否有融资/担保记录，构建第三层
                    List<Map<String, Object>> grandChildren = new ArrayList<>();
                    LambdaQueryWrapper<TblFinancingRecord> frw = new LambdaQueryWrapper<>();
                    frw.eq(TblFinancingRecord::getCompanyId, item.getCompanyId());
                    List<TblFinancingRecord> frList = financingRecordMapper.selectList(frw);
                    if (!frList.isEmpty()) {
                        Map<String, Object> finNode = new HashMap<>();
                        finNode.put("name", "融资(" + frList.size() + "笔)");
                        finNode.put("key", item.getCompanyId() + "_FIN");
                        finNode.put("riskLevel", frList.stream().anyMatch(f -> "HIGH".equalsIgnoreCase(f.getRiskLevel())) ? "HIGH" : "LOW");
                        finNode.put("level", 2);
                        finNode.put("children", new ArrayList<>());
                        grandChildren.add(finNode);
                    }
                    LambdaQueryWrapper<TblGuaranteeRecord> grw = new LambdaQueryWrapper<>();
                    grw.eq(TblGuaranteeRecord::getCompanyId, item.getCompanyId());
                    List<TblGuaranteeRecord> grList = guaranteeRecordMapper.selectList(grw);
                    if (!grList.isEmpty()) {
                        Map<String, Object> guaNode = new HashMap<>();
                        guaNode.put("name", "担保(" + grList.size() + "笔)");
                        guaNode.put("key", item.getCompanyId() + "_GUA");
                        guaNode.put("riskLevel", grList.stream().anyMatch(g -> "HIGH".equalsIgnoreCase(g.getRiskLevel())) ? "HIGH" : "MEDIUM");
                        guaNode.put("level", 2);
                        guaNode.put("children", new ArrayList<>());
                        grandChildren.add(guaNode);
                    }
                    child.put("children", grandChildren);
                    children.add(child);
                }
            }
            root.put("children", children);
            return R.success(root);
        } catch (Exception e) {
            log.error("查询金融风险穿透树失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "金融风险穿透详情")
    @GetMapping("/drill-down/detail/{nodeId}")
    public R<Map<String, Object>> drillDownDetail(@PathVariable String nodeId) {
        try {
            Map<String, Object> detail = new HashMap<>();
            detail.put("id", nodeId);

            // 查询该节点的流动性数据，获取企业基本信息
            String companyName = nodeId;
            String riskLevel = "LOW";
            int level = 1;
            BigDecimal totalFinancing = BigDecimal.ZERO;
            BigDecimal totalGuarantee = BigDecimal.ZERO;

            LambdaQueryWrapper<GzctFinLiquidity> lqWrapper = new LambdaQueryWrapper<>();
            lqWrapper.eq(GzctFinLiquidity::getCompanyId, nodeId);
            List<GzctFinLiquidity> lqList = liquidityMapper.selectList(lqWrapper);
            if (!lqList.isEmpty()) {
                GzctFinLiquidity lq = lqList.get(0);
                companyName = lq.getCompanyName() != null ? lq.getCompanyName() : nodeId;
                riskLevel = lq.getRiskLevel() != null ? lq.getRiskLevel() : "LOW";
            }

            detail.put("label", companyName);
            detail.put("riskLevel", riskLevel);
            detail.put("level", level);

            // 查询融资记录
            LambdaQueryWrapper<TblFinancingRecord> frWrapper = new LambdaQueryWrapper<>();
            frWrapper.eq(TblFinancingRecord::getCompanyId, nodeId);
            frWrapper.orderByDesc(TblFinancingRecord::getCreateTime);
            List<TblFinancingRecord> financingRecords = financingRecordMapper.selectList(frWrapper);
            List<Map<String, Object>> financingList = new ArrayList<>();
            for (TblFinancingRecord fr : financingRecords) {
                Map<String, Object> item = new HashMap<>();
                item.put("name", fr.getFinancingType() != null ? fr.getFinancingType() + "-" + (fr.getLender() != null ? fr.getLender() : "") : "融资");
                item.put("type", fr.getFinancingType() != null ? fr.getFinancingType() : "其他");
                item.put("amount", fr.getFinancingAmount() != null ? fr.getFinancingAmount() : BigDecimal.ZERO);
                item.put("dueDate", fr.getMaturityDate() != null ? fr.getMaturityDate().format(DateTimeFormatter.ISO_LOCAL_DATE) : "-");
                // 判断状态
                String status = "正常";
                if ("OVERDUE".equalsIgnoreCase(fr.getFinancingStatus())) {
                    status = "逾期";
                } else if (fr.getMaturityDate() != null && fr.getMaturityDate().isBefore(LocalDate.now().plusDays(30))) {
                    status = "即将到期";
                }
                item.put("status", status);
                financingList.add(item);
                if (fr.getFinancingAmount() != null) {
                    totalFinancing = totalFinancing.add(fr.getFinancingAmount());
                }
            }
            detail.put("financingList", financingList);

            // 查询担保记录
            LambdaQueryWrapper<TblGuaranteeRecord> grWrapper = new LambdaQueryWrapper<>();
            grWrapper.eq(TblGuaranteeRecord::getCompanyId, nodeId);
            grWrapper.orderByDesc(TblGuaranteeRecord::getCreateTime);
            List<TblGuaranteeRecord> guaranteeRecords = guaranteeRecordMapper.selectList(grWrapper);
            List<Map<String, Object>> guaranteeList = new ArrayList<>();
            for (TblGuaranteeRecord gr : guaranteeRecords) {
                Map<String, Object> item = new HashMap<>();
                item.put("name", gr.getGuaranteeName() != null ? gr.getGuaranteeName() : "担保项目");
                item.put("type", gr.getGuaranteeType() != null ? gr.getGuaranteeType() : "一般担保");
                item.put("amount", gr.getGuaranteeAmount() != null ? gr.getGuaranteeAmount() : BigDecimal.ZERO);
                item.put("counterpart", gr.getGuaranteedName() != null ? gr.getGuaranteedName() : "-");
                item.put("dueDate", gr.getEndDate() != null ? gr.getEndDate().format(DateTimeFormatter.ISO_LOCAL_DATE) : "-");
                guaranteeList.add(item);
                if (gr.getGuaranteeAmount() != null) {
                    totalGuarantee = totalGuarantee.add(gr.getGuaranteeAmount());
                }
            }
            detail.put("guaranteeList", guaranteeList);

            // 查询预警信息
            LambdaQueryWrapper<GzctFinAlert> alertWrapper = new LambdaQueryWrapper<>();
            alertWrapper.eq(GzctFinAlert::getCompanyId, nodeId);
            alertWrapper.ne(GzctFinAlert::getStatus, "DISMISSED");
            alertWrapper.orderByDesc(GzctFinAlert::getCreateTime);
            List<GzctFinAlert> alerts = alertMapper.selectList(alertWrapper);
            List<Map<String, Object>> warnings = new ArrayList<>();
            for (GzctFinAlert alert : alerts) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", alert.getAlertId());
                item.put("level", alert.getLevel() != null ? alert.getLevel() : "LOW");
                item.put("title", alert.getAlertType() != null ? alert.getAlertType() : "风险预警");
                item.put("desc", alert.getAlertContent() != null ? alert.getAlertContent() : "");
                item.put("time", alert.getCreateTime() != null ? alert.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) : "-");
                warnings.add(item);
            }
            detail.put("warnings", warnings);

            // 构建KPI统计卡
            List<Map<String, Object>> kpis = new ArrayList<>();
            Map<String, Object> kpi1 = new HashMap<>();
            kpi1.put("label", "融资总额(万元)");
            kpi1.put("value", totalFinancing.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            kpi1.put("color", "#0050A0");
            kpis.add(kpi1);

            Map<String, Object> kpi2 = new HashMap<>();
            kpi2.put("label", "担保总额(万元)");
            kpi2.put("value", totalGuarantee.setScale(2, BigDecimal.ROUND_HALF_UP).toString());
            kpi2.put("color", "#FA8C16");
            kpis.add(kpi2);

            Map<String, Object> kpi3 = new HashMap<>();
            kpi3.put("label", "预警数量");
            kpi3.put("value", String.valueOf(warnings.size()));
            kpi3.put("color", warnings.isEmpty() ? "#52C41A" : "#FF4D4F");
            kpis.add(kpi3);

            detail.put("kpis", kpis);

            return R.success(detail);
        } catch (Exception e) {
            log.error("查询穿透详情失败, nodeId={}", nodeId, e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "金融风险预警列表")
    @PostMapping("/alert/list")
    public R<PageResult<GzctFinAlert>> alertList(@RequestBody Map<String, Object> params) {
        try {
            int pageNum = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctFinAlert> wrapper = new LambdaQueryWrapper<>();
            wrapper.orderByDesc(GzctFinAlert::getCreateTime);
            Page<GzctFinAlert> page = new Page<>(pageNum, pageSize);
            Page<GzctFinAlert> result = new GzctFinAlert().selectPage(page, wrapper);
            PageResult<GzctFinAlert> pr = new PageResult<>();
            pr.setTotalRecord((int) result.getTotal()); pr.setCurrentPage((int) result.getCurrent());
            pr.setPageNumber((int) result.getCurrent()); pr.setTotalPage((int) result.getPages());
            pr.setPageSize((int) result.getSize()); pr.setTlist(result.getRecords());
            return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "解除金融风险预警")
    @PostMapping("/alert/dismiss/{alertId}")
    public R<Boolean> dismissAlert(@PathVariable String alertId) {
        try {
            GzctFinAlert alert = alertMapper.selectById(alertId);
            if (alert != null) { alert.setStatus("DISMISSED"); alert.setUpdateTime(LocalDateTime.now()); alertMapper.updateById(alert); }
            return R.success(true);
        } catch (Exception e) { return R.fail("操作失败：" + e.getMessage()); }
    }

    // ==================== 驾驶舱 ====================

    @Operation(summary = "金融风险驾驶舱")
    @GetMapping("/dashboard")
    public R<Map<String, Object>> dashboard(@RequestParam(required = false) String companyId) {
        try {
            Map<String, Object> result = new HashMap<>();

            // ==================== 1. KPI 统计卡片 ====================
            Map<String, Object> kpi = new HashMap<>();

            // 融资总额（亿元）- 从融资记录表汇总
            List<TblFinancingRecord> financingList = financingRecordMapper.selectList(null);
            BigDecimal totalFinancingAmount = financingList.stream()
                .filter(f -> f.getFinancingAmount() != null)
                .map(TblFinancingRecord::getFinancingAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            kpi.put("totalFinancing", totalFinancingAmount.divide(new BigDecimal("100000000"), 2, BigDecimal.ROUND_HALF_UP));

            // 担保总额（亿元）- 从担保记录表汇总
            List<TblGuaranteeRecord> guaranteeList = guaranteeRecordMapper.selectList(null);
            BigDecimal totalGuaranteeAmount = guaranteeList.stream()
                .filter(g -> g.getGuaranteeAmount() != null)
                .map(TblGuaranteeRecord::getGuaranteeAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            kpi.put("totalGuarantee", totalGuaranteeAmount.divide(new BigDecimal("100000000"), 2, BigDecimal.ROUND_HALF_UP));

            // 逾期委托贷款（亿元）
            List<GzctFinEntrustedLoan> loanList = loanMapper.selectList(null);
            BigDecimal overdueAmount = loanList.stream()
                .filter(l -> "1".equals(l.getIsOverdue()) && l.getOverdueAmount() != null)
                .map(GzctFinEntrustedLoan::getOverdueAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            kpi.put("overdueEntrustedLoan", overdueAmount.divide(new BigDecimal("100000000"), 2, BigDecimal.ROUND_HALF_UP));

            // 衍生品敞口（亿元）
            List<GzctFinDerivatives> derivativesList = derivativesMapper.selectList(null);
            BigDecimal derivativesExposure = derivativesList.stream()
                .filter(d -> d.getNotionalAmount() != null)
                .map(GzctFinDerivatives::getNotionalAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            kpi.put("derivativesExposure", derivativesExposure.divide(new BigDecimal("100000000"), 2, BigDecimal.ROUND_HALF_UP));

            // 高风险企业数
            long highRiskCount = financingList.stream().filter(f -> "HIGH".equals(f.getRiskLevel())).map(TblFinancingRecord::getCompanyId).distinct().count()
                + guaranteeList.stream().filter(g -> "HIGH".equals(g.getRiskLevel())).map(TblGuaranteeRecord::getCompanyId).distinct().count();
            // 去重：合并所有高风险企业ID
            Set<String> highRiskCompanies = new HashSet<>();
            financingList.stream().filter(f -> "HIGH".equals(f.getRiskLevel()) && f.getCompanyId() != null).forEach(f -> highRiskCompanies.add(f.getCompanyId()));
            guaranteeList.stream().filter(g -> "HIGH".equals(g.getRiskLevel()) && g.getCompanyId() != null).forEach(g -> highRiskCompanies.add(g.getCompanyId()));
            loanList.stream().filter(l -> "HIGH".equals(l.getRiskLevel()) && l.getCompanyId() != null).forEach(l -> highRiskCompanies.add(l.getCompanyId()));
            derivativesList.stream().filter(d -> "HIGH".equals(d.getRiskLevel()) && d.getCompanyId() != null).forEach(d -> highRiskCompanies.add(d.getCompanyId()));
            kpi.put("highRiskCount", highRiskCompanies.size());

            // 活跃预警数
            LambdaQueryWrapper<GzctFinAlert> alertWrapper = new LambdaQueryWrapper<>();
            alertWrapper.ne(GzctFinAlert::getStatus, "DISMISSED");
            Long activeAlertCount = (long) alertMapper.selectCount(alertWrapper);
            kpi.put("activeAlertCount", activeAlertCount);

            result.put("kpi", kpi);

            // ==================== 2. 融资规模趋势（近6个月） ====================
            Map<String, Object> trendData = new HashMap<>();
            LocalDate now = LocalDate.now();
            List<String> months = new ArrayList<>();
            List<BigDecimal> financingScale = new ArrayList<>();
            List<BigDecimal> guaranteeBalance = new ArrayList<>();
            List<BigDecimal> riskExposure = new ArrayList<>();
            DateTimeFormatter monthFmt = DateTimeFormatter.ofPattern("yyyy-MM");

            for (int i = 5; i >= 0; i--) {
                LocalDate monthStart = now.minusMonths(i).withDayOfMonth(1);
                LocalDate monthEnd = monthStart.plusMonths(1).minusDays(1);
                months.add(monthStart.format(monthFmt));

                // 该月融资规模：统计在该月之前开始且尚未到期的融资
                BigDecimal monthFinancing = financingList.stream()
                    .filter(f -> f.getFinancingAmount() != null && f.getStartDate() != null
                        && !f.getStartDate().isAfter(monthEnd)
                        && (f.getMaturityDate() == null || !f.getMaturityDate().isBefore(monthStart)))
                    .map(TblFinancingRecord::getFinancingAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                financingScale.add(monthFinancing.divide(new BigDecimal("100000000"), 2, BigDecimal.ROUND_HALF_UP));

                // 该月担保余额
                BigDecimal monthGuarantee = guaranteeList.stream()
                    .filter(g -> g.getGuaranteeAmount() != null && g.getStartDate() != null
                        && !g.getStartDate().isAfter(monthEnd)
                        && (g.getEndDate() == null || !g.getEndDate().isBefore(monthStart)))
                    .map(TblGuaranteeRecord::getGuaranteeAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                guaranteeBalance.add(monthGuarantee.divide(new BigDecimal("100000000"), 2, BigDecimal.ROUND_HALF_UP));

                // 该月风险敞口（高风险融资未偿还金额）
                BigDecimal monthExposure = financingList.stream()
                    .filter(f -> "HIGH".equals(f.getRiskLevel()) && f.getOutstandingAmount() != null
                        && f.getStartDate() != null && !f.getStartDate().isAfter(monthEnd))
                    .map(TblFinancingRecord::getOutstandingAmount)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                riskExposure.add(monthExposure.divide(new BigDecimal("100000000"), 2, BigDecimal.ROUND_HALF_UP));
            }
            trendData.put("months", months);
            trendData.put("financingScale", financingScale);
            trendData.put("guaranteeBalance", guaranteeBalance);
            trendData.put("riskExposure", riskExposure);
            result.put("trendData", trendData);

            // ==================== 3. 风险等级分布 ====================
            List<Map<String, Object>> riskOverview = new ArrayList<>();
            // 统计所有业务的风险等级
            Map<String, Long> riskCountMap = new LinkedHashMap<>();
            riskCountMap.put("高风险", 0L);
            riskCountMap.put("中风险", 0L);
            riskCountMap.put("低风险", 0L);
            // 融资记录风险等级
            financingList.forEach(f -> {
                if ("HIGH".equals(f.getRiskLevel())) riskCountMap.merge("高风险", 1L, Long::sum);
                else if ("MEDIUM".equals(f.getRiskLevel())) riskCountMap.merge("中风险", 1L, Long::sum);
                else riskCountMap.merge("低风险", 1L, Long::sum);
            });
            // 担保记录风险等级
            guaranteeList.forEach(g -> {
                if ("HIGH".equals(g.getRiskLevel())) riskCountMap.merge("高风险", 1L, Long::sum);
                else if ("MEDIUM".equals(g.getRiskLevel())) riskCountMap.merge("中风险", 1L, Long::sum);
                else riskCountMap.merge("低风险", 1L, Long::sum);
            });
            for (Map.Entry<String, Long> entry : riskCountMap.entrySet()) {
                Map<String, Object> item = new HashMap<>();
                item.put("name", entry.getKey());
                item.put("value", entry.getValue());
                riskOverview.add(item);
            }
            result.put("riskOverview", riskOverview);

            // ==================== 4. 担保链关联分析 ====================
            Map<String, Object> guaranteeChain = new HashMap<>();
            // 按公司汇总担保金额，取TOP8
            Map<String, BigDecimal> companyGuaranteeMap = new LinkedHashMap<>();
            guaranteeList.forEach(g -> {
                String name = g.getCompanyName() != null ? g.getCompanyName() : "未知企业";
                BigDecimal amt = g.getGuaranteeAmount() != null ? g.getGuaranteeAmount() : BigDecimal.ZERO;
                companyGuaranteeMap.merge(name, amt, BigDecimal::add);
            });
            // 排序取前8
            List<Map.Entry<String, BigDecimal>> sortedGuarantees = new ArrayList<>(companyGuaranteeMap.entrySet());
            sortedGuarantees.sort((a, b) -> b.getValue().compareTo(a.getValue()));
            List<String> companies = new ArrayList<>();
            List<BigDecimal> amounts = new ArrayList<>();
            int limit = Math.min(8, sortedGuarantees.size());
            for (int i = 0; i < limit; i++) {
                companies.add(sortedGuarantees.get(i).getKey());
                amounts.add(sortedGuarantees.get(i).getValue().divide(new BigDecimal("100000000"), 2, BigDecimal.ROUND_HALF_UP));
            }
            guaranteeChain.put("companies", companies);
            guaranteeChain.put("amounts", amounts);
            result.put("guaranteeChain", guaranteeChain);

            // ==================== 5. 到期融资预警 ====================
            Map<String, Object> maturityDistribution = new HashMap<>();
            List<String> periods = Arrays.asList("1个月内", "1-3个月", "3-6个月", "6-12个月", "1年以上");
            List<BigDecimal> maturityAmounts = new ArrayList<>();
            LocalDate oneMonth = now.plusMonths(1);
            LocalDate threeMonths = now.plusMonths(3);
            LocalDate sixMonths = now.plusMonths(6);
            LocalDate oneYear = now.plusYears(1);

            // 1个月内到期
            BigDecimal m1 = financingList.stream()
                .filter(f -> f.getMaturityDate() != null && f.getOutstandingAmount() != null
                    && !f.getMaturityDate().isBefore(now) && f.getMaturityDate().isBefore(oneMonth))
                .map(TblFinancingRecord::getOutstandingAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            maturityAmounts.add(m1.divide(new BigDecimal("100000000"), 2, BigDecimal.ROUND_HALF_UP));

            // 1-3个月到期
            BigDecimal m3 = financingList.stream()
                .filter(f -> f.getMaturityDate() != null && f.getOutstandingAmount() != null
                    && !f.getMaturityDate().isBefore(oneMonth) && f.getMaturityDate().isBefore(threeMonths))
                .map(TblFinancingRecord::getOutstandingAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            maturityAmounts.add(m3.divide(new BigDecimal("100000000"), 2, BigDecimal.ROUND_HALF_UP));

            // 3-6个月到期
            BigDecimal m6 = financingList.stream()
                .filter(f -> f.getMaturityDate() != null && f.getOutstandingAmount() != null
                    && !f.getMaturityDate().isBefore(threeMonths) && f.getMaturityDate().isBefore(sixMonths))
                .map(TblFinancingRecord::getOutstandingAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            maturityAmounts.add(m6.divide(new BigDecimal("100000000"), 2, BigDecimal.ROUND_HALF_UP));

            // 6-12个月到期
            BigDecimal m12 = financingList.stream()
                .filter(f -> f.getMaturityDate() != null && f.getOutstandingAmount() != null
                    && !f.getMaturityDate().isBefore(sixMonths) && f.getMaturityDate().isBefore(oneYear))
                .map(TblFinancingRecord::getOutstandingAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            maturityAmounts.add(m12.divide(new BigDecimal("100000000"), 2, BigDecimal.ROUND_HALF_UP));

            // 1年以上到期
            BigDecimal mOver = financingList.stream()
                .filter(f -> f.getMaturityDate() != null && f.getOutstandingAmount() != null
                    && !f.getMaturityDate().isBefore(oneYear))
                .map(TblFinancingRecord::getOutstandingAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            maturityAmounts.add(mOver.divide(new BigDecimal("100000000"), 2, BigDecimal.ROUND_HALF_UP));

            // 累计到期
            List<BigDecimal> cumulative = new ArrayList<>();
            BigDecimal cum = BigDecimal.ZERO;
            for (BigDecimal amt : maturityAmounts) {
                cum = cum.add(amt);
                cumulative.add(cum);
            }
            maturityDistribution.put("periods", periods);
            maturityDistribution.put("amounts", maturityAmounts);
            maturityDistribution.put("cumulative", cumulative);
            result.put("maturityDistribution", maturityDistribution);

            // ==================== 6. 活跃预警清单（供首页使用） ====================
            LambdaQueryWrapper<GzctFinAlert> activeAlertWrapper = new LambdaQueryWrapper<>();
            activeAlertWrapper.ne(GzctFinAlert::getStatus, "DISMISSED");
            activeAlertWrapper.orderByDesc(GzctFinAlert::getCreateTime);
            Page<GzctFinAlert> alertPage = new Page<>(1, 6);
            Page<GzctFinAlert> alertPageResult = alertMapper.selectPage(alertPage, activeAlertWrapper);
            List<GzctFinAlert> alertList = alertPageResult.getRecords();
            List<Map<String, Object>> activeWarningsList = new ArrayList<>();
            for (GzctFinAlert alert : alertList) {
                Map<String, Object> w = new HashMap<>();
                w.put("code", alert.getAlertId() != null ? alert.getAlertId().substring(0, Math.min(8, alert.getAlertId().length())) : "");
                w.put("time", alert.getCreateTime() != null ? alert.getCreateTime().toString() : "");
                w.put("name", alert.getAlertType() != null ? alert.getAlertType() : "风险预警");
                w.put("company", alert.getCompanyName() != null ? alert.getCompanyName() : "");
                w.put("value", alert.getAlertContent() != null ? alert.getAlertContent() : "");
                w.put("threshold", "-");
                w.put("level", alert.getLevel() != null ? alert.getLevel() : "MEDIUM");
                activeWarningsList.add(w);
            }
            result.put("activeWarnings", activeWarningsList);

            return R.success(result);
        } catch (Exception e) { log.error("查询金融风险驾驶舱失败", e); return R.fail("查询失败：" + e.getMessage()); }
    }
}
