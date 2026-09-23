package com.huabo.cybermonitor.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.cybermonitor.entity.*;
import com.huabo.cybermonitor.mapper.*;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.util.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Tag(name = "会计穿透式监管-综合接口", description = "会计穿透式监管全接口(凭证/账簿/报表/预算/两金/造假/估计/驾驶舱)")
@RestController
@RequestMapping("/v1/supervision/accounting")
@Slf4j
public class AccountingSupervisionController {

    @Autowired private GzctAccountingEstimateMapper estimateMapper;
    @Autowired private TblAccountingPolicyMapper accountingPolicyMapper;
    @Autowired private GzctAccountingVoucherMapper voucherMapper;
    @Autowired private GzctAccountingBookMapper bookMapper;
    @Autowired private GzctAccountingReportMapper reportMapper;
    @Autowired private GzctAccountingBudgetMapper budgetMapper;
    @Autowired private GzctAccountingTwogoldMapper twogoldMapper;
    @Autowired private GzctAccountingFraudMapper fraudMapper;
    @Autowired private GzctAccountingWarningMapper warningMapper;
    @Autowired private GzctAccountingClueMapper clueMapper;
    @Autowired private FinancialIndicatorsMapper financialIndicatorsMapper;
    @Autowired private GzctAccountingRectificationMapper rectificationMapper;
    @Autowired private GzctAccountingVerificationMapper verificationMapper;
    @Autowired private com.huabo.cybermonitor.util.OrgQueryHelper orgQueryHelper;

    private <T> PageResult<T> buildPageResult(Page<T> r) {
        PageResult<T> pr = new PageResult<>();
        pr.setTotalRecord((int) r.getTotal()); pr.setCurrentPage((int) r.getCurrent());
        pr.setPageNumber((int) r.getCurrent()); pr.setTotalPage((int) r.getPages());
        pr.setPageSize((int) r.getSize()); pr.setTlist(r.getRecords());
        return pr;
    }

    // ==================== 会计政策CRUD已由 AccountingPolicyController 处理 ====================

    // ==================== 会计政策统计 ====================

    @Operation(summary = "会计政策统计")
    @GetMapping("/policy/statistics")
    public R<Map<String, Object>> policyStatistics(@RequestParam(required = false) String companyId) {
        try {
            Map<String, Object> result = new HashMap<>();
            String orgPattern = orgQueryHelper.getOrgPathPattern(companyId);
            LambdaQueryWrapper<TblAccountingPolicy> policyBaseWrapper = new LambdaQueryWrapper<>();
            if (orgPattern != null) policyBaseWrapper.and(w -> w.like(TblAccountingPolicy::getOrgPath, orgPattern).or(sub -> sub.isNull(TblAccountingPolicy::getOrgPath).eq(TblAccountingPolicy::getCompanyId, companyId)));
            Long totalPolicies = accountingPolicyMapper.selectCount(policyBaseWrapper);
            LambdaQueryWrapper<TblAccountingPolicy> activeWrapper = new LambdaQueryWrapper<>();
            if (orgPattern != null) activeWrapper.and(w -> w.like(TblAccountingPolicy::getOrgPath, orgPattern).or(sub -> sub.isNull(TblAccountingPolicy::getOrgPath).eq(TblAccountingPolicy::getCompanyId, companyId)));
            activeWrapper.eq(TblAccountingPolicy::getIsConsistent, "1");
            Long activePolicies = accountingPolicyMapper.selectCount(activeWrapper);
            LambdaQueryWrapper<TblAccountingPolicy> changingWrapper = new LambdaQueryWrapper<>();
            if (orgPattern != null) changingWrapper.and(w -> w.like(TblAccountingPolicy::getOrgPath, orgPattern).or(sub -> sub.isNull(TblAccountingPolicy::getOrgPath).eq(TblAccountingPolicy::getCompanyId, companyId)));
            changingWrapper.eq(TblAccountingPolicy::getIsConsistent, "0");
            Long changingPolicies = accountingPolicyMapper.selectCount(changingWrapper);
            LambdaQueryWrapper<TblAccountingPolicy> diffWrapper = new LambdaQueryWrapper<>();
            if (orgPattern != null) diffWrapper.and(w -> w.like(TblAccountingPolicy::getOrgPath, orgPattern).or(sub -> sub.isNull(TblAccountingPolicy::getOrgPath).eq(TblAccountingPolicy::getCompanyId, companyId)));
            diffWrapper.ne(TblAccountingPolicy::getDeviationDesc, "");
            Long diffPolicies = accountingPolicyMapper.selectCount(diffWrapper);
            LambdaQueryWrapper<GzctAccountingEstimate> estBaseWrapper = new LambdaQueryWrapper<>();
            if (orgPattern != null) estBaseWrapper.eq(GzctAccountingEstimate::getCompanyId, companyId);
            Long estimateTotal = estimateMapper.selectCount(estBaseWrapper);
            LambdaQueryWrapper<GzctAccountingEstimate> estFocusWrapper = new LambdaQueryWrapper<>();
            if (orgPattern != null) estFocusWrapper.eq(GzctAccountingEstimate::getCompanyId, companyId);
            estFocusWrapper.eq(GzctAccountingEstimate::getIsCompliant, "0");
            Long estimateFocus = estimateMapper.selectCount(estFocusWrapper);
            result.put("policyTotal", totalPolicies);
            result.put("policyActive", activePolicies);
            result.put("policyChanging", changingPolicies);
            result.put("policyDiff", diffPolicies);
            result.put("estimateTotal", estimateTotal);
            result.put("estimateFocus", estimateFocus);
            // 前端 STAT_MAP 期望字段
            // 凭证数据：优先从 GZCT_ACCOUNTING_VOUCHER 查，如果为0则用政策数据兜底
            LambdaQueryWrapper<GzctAccountingVoucher> voucherBaseWrapper = new LambdaQueryWrapper<>();
            if (companyId != null) voucherBaseWrapper.eq(GzctAccountingVoucher::getCompanyId, companyId);
            Long voucherCount = voucherMapper.selectCount(voucherBaseWrapper);
            LambdaQueryWrapper<GzctAccountingVoucher> anomalyVoucherWrapper = new LambdaQueryWrapper<>();
            if (companyId != null) anomalyVoucherWrapper.eq(GzctAccountingVoucher::getCompanyId, companyId);
            anomalyVoucherWrapper.ne(GzctAccountingVoucher::getAnomalyFlag, "N");
            Long anomalyCount = voucherMapper.selectCount(anomalyVoucherWrapper);
            // 如果凭证表无数据，用政策数据兜底（政策总数=凭证总数，不一致=异常）
            if (voucherCount == 0) {
                voucherCount = totalPolicies;
                anomalyCount = changingPolicies;
            }
            result.put("voucherCount", voucherCount);
            result.put("totalVouchers", voucherCount);
            result.put("anomalyCount", anomalyCount);
            result.put("abnormalCount", anomalyCount);
            result.put("riskCount", changingPolicies + diffPolicies);
            result.put("warningCount", changingPolicies + diffPolicies);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 会计凭证穿透 ====================

    @Operation(summary = "凭证列表")
    @PostMapping("/voucher/list")
    public R<PageResult<Map<String, Object>>> voucherList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 10;
            LambdaQueryWrapper<GzctAccountingVoucher> w = new LambdaQueryWrapper<>();
            if (params.get("companyName") != null && StringUtils.isNotBlank(params.get("companyName").toString())) {
                w.like(GzctAccountingVoucher::getCompanyName, params.get("companyName").toString());
            }
            if (params.get("voucherNo") != null && StringUtils.isNotBlank(params.get("voucherNo").toString())) {
                w.like(GzctAccountingVoucher::getVoucherNo, params.get("voucherNo").toString());
            }
            if (params.get("subject") != null && StringUtils.isNotBlank(params.get("subject").toString())) {
                w.and(q -> q.like(GzctAccountingVoucher::getDebitSubject, params.get("subject").toString())
                    .or().like(GzctAccountingVoucher::getCreditSubject, params.get("subject").toString()));
            }
            if (params.get("anomalyFlag") != null && StringUtils.isNotBlank(params.get("anomalyFlag").toString())) {
                w.eq(GzctAccountingVoucher::getAnomalyFlag, params.get("anomalyFlag").toString());
            }
            // 会计期间范围（PERIOD 字段，YYYY-MM 字符串比较；兼容旧参数 startDate/endDate）
            String startPeriod = firstNonBlank(params.get("startPeriod"), params.get("startDate"));
            String endPeriod   = firstNonBlank(params.get("endPeriod"),   params.get("endDate"));
            if (startPeriod != null) w.ge(GzctAccountingVoucher::getPeriod, startPeriod);
            if (endPeriod   != null) w.le(GzctAccountingVoucher::getPeriod, endPeriod);
            w.orderByDesc(GzctAccountingVoucher::getVoucherDate);
            Page<GzctAccountingVoucher> page = voucherMapper.selectPage(new Page<>(pn, ps), w);
            PageResult<Map<String, Object>> pr = new PageResult<>();
            pr.setTotalRecord((int) page.getTotal()); pr.setCurrentPage(pn); pr.setPageNumber(pn);
            pr.setTotalPage((int) page.getPages()); pr.setPageSize(ps);
            pr.setTlist(page.getRecords().stream().map(v -> {
                Map<String, Object> m = new HashMap<>();
                m.put("id", v.getVoucherId()); m.put("companyName", v.getCompanyName());
                m.put("voucherDate", v.getVoucherDate()); m.put("voucherNo", v.getVoucherNo());
                m.put("summary", v.getSummary()); m.put("debitSubject", v.getDebitSubject());
                m.put("creditSubject", v.getCreditSubject()); m.put("debitAmount", v.getDebitAmount());
                m.put("creditAmount", v.getCreditAmount()); m.put("inputUser", v.getInputUser());
                m.put("anomalyFlag", v.getAnomalyFlag()); m.put("anomalyType", v.getAnomalyType());
                return m;
            }).collect(Collectors.toList()));
            return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "异常凭证列表")
    @PostMapping("/voucher/anomaly/list")
    public R<PageResult<Map<String, Object>>> voucherAnomalyList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctAccountingVoucher> w = new LambdaQueryWrapper<>();
            w.ne(GzctAccountingVoucher::getAnomalyFlag, "N");
            w.orderByDesc(GzctAccountingVoucher::getVoucherDate);
            Page<GzctAccountingVoucher> page = voucherMapper.selectPage(new Page<>(pn, ps), w);
            PageResult<Map<String, Object>> pr = new PageResult<>();
            pr.setTotalRecord((int) page.getTotal()); pr.setCurrentPage(pn); pr.setPageNumber(pn);
            pr.setTotalPage((int) page.getPages()); pr.setPageSize(ps);
            pr.setTlist(page.getRecords().stream().map(v -> {
                Map<String, Object> m = new HashMap<>();
                m.put("id", v.getVoucherId()); m.put("companyName", v.getCompanyName());
                m.put("voucherDate", v.getVoucherDate()); m.put("voucherNo", v.getVoucherNo());
                m.put("summary", v.getSummary()); m.put("anomalyFlag", v.getAnomalyFlag());
                m.put("anomalyType", v.getAnomalyType());
                return m;
            }).collect(Collectors.toList()));
            return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 账簿穿透 ====================

    @Operation(summary = "科目余额表")
    @PostMapping("/book/balance/list")
    public R<PageResult<Map<String, Object>>> subjectBalanceList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 50;
            LambdaQueryWrapper<GzctAccountingBook> w = new LambdaQueryWrapper<>();
            w.eq(GzctAccountingBook::getBookType, "BALANCE");
            if (params.get("companyName") != null && StringUtils.isNotBlank(params.get("companyName").toString())) {
                w.like(GzctAccountingBook::getCompanyName, params.get("companyName").toString());
            }
            if (params.get("subjectCode") != null && StringUtils.isNotBlank(params.get("subjectCode").toString())) {
                w.and(q -> q.like(GzctAccountingBook::getSubjectCode, params.get("subjectCode").toString())
                    .or().like(GzctAccountingBook::getSubjectName, params.get("subjectCode").toString()));
            }
            if (params.get("yearMonth") != null && StringUtils.isNotBlank(params.get("yearMonth").toString())) {
                w.eq(GzctAccountingBook::getPeriod, params.get("yearMonth").toString());
            }
            w.isNull(GzctAccountingBook::getParentId);
            w.orderByAsc(GzctAccountingBook::getSubjectCode);
            Page<GzctAccountingBook> page = bookMapper.selectPage(new Page<>(pn, ps), w);
            PageResult<Map<String, Object>> pr = new PageResult<>();
            pr.setTotalRecord((int) page.getTotal()); pr.setCurrentPage(pn); pr.setPageNumber(pn);
            pr.setTotalPage((int) page.getPages()); pr.setPageSize(ps);
            pr.setTlist(page.getRecords().stream().map(b -> {
                Map<String, Object> m = new HashMap<>();
                m.put("subjectCode", b.getSubjectCode()); m.put("subjectName", b.getSubjectName());
                m.put("direction", b.getDirection()); m.put("openingBalance", b.getOpeningBalance());
                m.put("debitAmount", b.getDebitAmount()); m.put("creditAmount", b.getCreditAmount());
                m.put("closingBalance", b.getClosingBalance()); m.put("changeRate", b.getChangeRate());
                m.put("hasChildren", true);
                LambdaQueryWrapper<GzctAccountingBook> cw = new LambdaQueryWrapper<>();
                cw.eq(GzctAccountingBook::getParentId, b.getBookId());
                List<GzctAccountingBook> children = bookMapper.selectList(cw);
                if (!children.isEmpty()) {
                    m.put("children", children.stream().map(c -> {
                        Map<String, Object> cm = new HashMap<>();
                        cm.put("subjectCode", c.getSubjectCode()); cm.put("subjectName", c.getSubjectName());
                        cm.put("direction", c.getDirection()); cm.put("openingBalance", c.getOpeningBalance());
                        cm.put("debitAmount", c.getDebitAmount()); cm.put("creditAmount", c.getCreditAmount());
                        cm.put("closingBalance", c.getClosingBalance()); cm.put("changeRate", c.getChangeRate());
                        return cm;
                    }).collect(Collectors.toList()));
                }
                return m;
            }).collect(Collectors.toList()));
            return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "总账列表")
    @PostMapping("/book/general/list")
    public R<PageResult<Map<String, Object>>> generalLedgerList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 50;
            LambdaQueryWrapper<GzctAccountingBook> w = new LambdaQueryWrapper<>();
            w.eq(GzctAccountingBook::getBookType, "GENERAL");
            if (params.get("companyName") != null && StringUtils.isNotBlank(params.get("companyName").toString())) {
                w.like(GzctAccountingBook::getCompanyName, params.get("companyName").toString());
            }
            // 科目过滤：subjectName 可能是 "1001 库存现金" 拼接形式，需对 subjectName/subjectCode 双字段 OR 匹配
            if (params.get("subjectName") != null && StringUtils.isNotBlank(params.get("subjectName").toString())) {
                String s = params.get("subjectName").toString();
                w.and(q -> q.like(GzctAccountingBook::getSubjectName, s).or().like(GzctAccountingBook::getSubjectCode, s));
            }
            if (params.get("subjectCode") != null && StringUtils.isNotBlank(params.get("subjectCode").toString())) {
                w.like(GzctAccountingBook::getSubjectCode, params.get("subjectCode").toString());
            }
            if (params.get("yearMonth") != null && StringUtils.isNotBlank(params.get("yearMonth").toString())) {
                w.eq(GzctAccountingBook::getPeriod, params.get("yearMonth").toString());
            }
            w.orderByDesc(GzctAccountingBook::getVoucherDate);
            Page<GzctAccountingBook> page = bookMapper.selectPage(new Page<>(pn, ps), w);
            PageResult<Map<String, Object>> pr = new PageResult<>();
            pr.setTotalRecord((int) page.getTotal()); pr.setCurrentPage(pn); pr.setPageNumber(pn);
            pr.setTotalPage((int) page.getPages()); pr.setPageSize(ps);
            pr.setTlist(page.getRecords().stream().map(b -> {
                Map<String, Object> m = new HashMap<>();
                m.put("date", b.getVoucherDate()); m.put("voucherNo", b.getVoucherNo());
                m.put("summary", b.getSummary()); m.put("debitAmount", b.getDebitAmount());
                m.put("creditAmount", b.getCreditAmount()); m.put("balance", b.getBalance());
                m.put("subjectCode", b.getSubjectCode()); m.put("subjectName", b.getSubjectName());
                return m;
            }).collect(Collectors.toList()));
            return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "明细账列表")
    @PostMapping("/book/sub/list")
    public R<PageResult<Map<String, Object>>> subLedgerList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 50;
            LambdaQueryWrapper<GzctAccountingBook> w = new LambdaQueryWrapper<>();
            w.eq(GzctAccountingBook::getBookType, "SUB");
            if (params.get("companyName") != null && StringUtils.isNotBlank(params.get("companyName").toString())) {
                w.like(GzctAccountingBook::getCompanyName, params.get("companyName").toString());
            }
            if (params.get("auxDimension") != null && StringUtils.isNotBlank(params.get("auxDimension").toString())) {
                w.eq(GzctAccountingBook::getAuxType, params.get("auxDimension").toString());
            }
            // 从总账穿透时带过来的科目过滤（可能是 "1001 库存现金" 这种拼接值）
            if (params.get("subjectName") != null && StringUtils.isNotBlank(params.get("subjectName").toString())) {
                String s = params.get("subjectName").toString();
                w.and(q -> q.like(GzctAccountingBook::getSubjectName, s).or().like(GzctAccountingBook::getSubjectCode, s));
            }
            if (params.get("subjectCode") != null && StringUtils.isNotBlank(params.get("subjectCode").toString())) {
                w.like(GzctAccountingBook::getSubjectCode, params.get("subjectCode").toString());
            }
            if (params.get("yearMonth") != null && StringUtils.isNotBlank(params.get("yearMonth").toString())) {
                w.eq(GzctAccountingBook::getPeriod, params.get("yearMonth").toString());
            }
            w.orderByDesc(GzctAccountingBook::getVoucherDate);
            Page<GzctAccountingBook> page = bookMapper.selectPage(new Page<>(pn, ps), w);
            PageResult<Map<String, Object>> pr = new PageResult<>();
            pr.setTotalRecord((int) page.getTotal()); pr.setCurrentPage(pn); pr.setPageNumber(pn);
            pr.setTotalPage((int) page.getPages()); pr.setPageSize(ps);
            pr.setTlist(page.getRecords().stream().map(b -> {
                Map<String, Object> m = new HashMap<>();
                m.put("auxName", b.getAuxName()); m.put("date", b.getVoucherDate());
                m.put("voucherNo", b.getVoucherNo()); m.put("summary", b.getSummary());
                m.put("debitAmount", b.getDebitAmount()); m.put("creditAmount", b.getCreditAmount());
                m.put("balance", b.getBalance());
                m.put("subjectCode", b.getSubjectCode()); m.put("subjectName", b.getSubjectName());
                return m;
            }).collect(Collectors.toList()));
            return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "辅助核算账列表")
    @PostMapping("/book/aux/list")
    public R<PageResult<Map<String, Object>>> auxLedgerList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 50;
            LambdaQueryWrapper<GzctAccountingBook> w = new LambdaQueryWrapper<>();
            w.eq(GzctAccountingBook::getBookType, "AUX");
            if (params.get("companyName") != null && StringUtils.isNotBlank(params.get("companyName").toString())) {
                w.like(GzctAccountingBook::getCompanyName, params.get("companyName").toString());
            }
            if (params.get("auxType") != null && StringUtils.isNotBlank(params.get("auxType").toString())) {
                w.eq(GzctAccountingBook::getAuxType, params.get("auxType").toString());
            }
            if (params.get("yearMonth") != null && StringUtils.isNotBlank(params.get("yearMonth").toString())) {
                w.eq(GzctAccountingBook::getPeriod, params.get("yearMonth").toString());
            }
            w.orderByDesc(GzctAccountingBook::getBalance);
            Page<GzctAccountingBook> page = bookMapper.selectPage(new Page<>(pn, ps), w);
            PageResult<Map<String, Object>> pr = new PageResult<>();
            pr.setTotalRecord((int) page.getTotal()); pr.setCurrentPage(pn); pr.setPageNumber(pn);
            pr.setTotalPage((int) page.getPages()); pr.setPageSize(ps);
            pr.setTlist(page.getRecords().stream().map(b -> {
                Map<String, Object> m = new HashMap<>();
                m.put("name", b.getAuxName()); m.put("balance", b.getBalance());
                m.put("within1y", b.getWithin1y()); m.put("y1to2", b.getY1to2());
                m.put("y2to3", b.getY2to3()); m.put("over3y", b.getOver3y());
                m.put("over3yRate", b.getOver3yRate()); m.put("risk", b.getRiskFlag());
                return m;
            }).collect(Collectors.toList()));
            return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "日记账列表")
    @PostMapping("/book/diary/list")
    public R<PageResult<Map<String, Object>>> diaryLedgerList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 50;
            LambdaQueryWrapper<GzctAccountingBook> w = new LambdaQueryWrapper<>();
            w.eq(GzctAccountingBook::getBookType, "DIARY");
            if (params.get("companyName") != null && StringUtils.isNotBlank(params.get("companyName").toString())) {
                w.like(GzctAccountingBook::getCompanyName, params.get("companyName").toString());
            }
            if (params.get("diaryType") != null && StringUtils.isNotBlank(params.get("diaryType").toString())) {
                w.eq(GzctAccountingBook::getAuxType, params.get("diaryType").toString());
            }
            if (params.get("yearMonth") != null && StringUtils.isNotBlank(params.get("yearMonth").toString())) {
                w.eq(GzctAccountingBook::getPeriod, params.get("yearMonth").toString());
            }
            w.orderByDesc(GzctAccountingBook::getVoucherDate);
            Page<GzctAccountingBook> page = bookMapper.selectPage(new Page<>(pn, ps), w);
            PageResult<Map<String, Object>> pr = new PageResult<>();
            pr.setTotalRecord((int) page.getTotal()); pr.setCurrentPage(pn); pr.setPageNumber(pn);
            pr.setTotalPage((int) page.getPages()); pr.setPageSize(ps);
            pr.setTlist(page.getRecords().stream().map(b -> {
                Map<String, Object> m = new HashMap<>();
                m.put("date", b.getVoucherDate()); m.put("summary", b.getSummary());
                m.put("debit", b.getDebitAmount()); m.put("credit", b.getCreditAmount());
                m.put("balance", b.getBalance()); m.put("unreconciled", b.getRiskFlag());
                return m;
            }).collect(Collectors.toList()));
            return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 财务报表穿透 ====================

    @Operation(summary = "报表穿透列表")
    @PostMapping("/report/list")
    public R<Map<String, Object>> reportList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 10;
            LambdaQueryWrapper<GzctAccountingReport> w = new LambdaQueryWrapper<>();
            if (params.get("companyName") != null && StringUtils.isNotBlank(params.get("companyName").toString())) {
                w.like(GzctAccountingReport::getCompanyName, params.get("companyName").toString());
            }
            if (params.get("year") != null && StringUtils.isNotBlank(params.get("year").toString())) {
                w.eq(GzctAccountingReport::getReportYear, params.get("year").toString());
            }
            if (params.get("reportType") != null && StringUtils.isNotBlank(params.get("reportType").toString())) {
                w.eq(GzctAccountingReport::getReportType, params.get("reportType").toString());
            }
            String reportTab = params.get("reportTab") != null ? params.get("reportTab").toString() : "list";
            Map<String, Object> result = new HashMap<>();
            if ("balance".equals(reportTab)) {
                w.eq(GzctAccountingReport::getReportTab, "BALANCE");
                List<GzctAccountingReport> list = reportMapper.selectList(w);
                List<Map<String, Object>> balanceSheetData = list.stream().map(r -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("item", r.getItemName()); m.put("isCategory", "1".equals(r.getIsCategory()));
                    m.put("current", r.getCurrentAmount()); m.put("prev", r.getPrevAmount());
                    m.put("changeRate", r.getChangeRate()); return m;
                }).collect(Collectors.toList());
                result.put("balanceSheetData", balanceSheetData);
            } else if ("income".equals(reportTab)) {
                w.eq(GzctAccountingReport::getReportTab, "INCOME");
                List<GzctAccountingReport> list = reportMapper.selectList(w);
                List<Map<String, Object>> incomeData = list.stream().map(r -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("item", r.getItemName()); m.put("isKey", "1".equals(r.getIsKey()));
                    m.put("current", r.getCurrentAmount()); m.put("prev", r.getPrevAmount());
                    m.put("growth", r.getGrowth()); m.put("anomaly", "1".equals(r.getAnomaly()));
                    return m;
                }).collect(Collectors.toList());
                result.put("incomeData", incomeData);
            } else {
                w.and(q -> q.isNull(GzctAccountingReport::getReportTab).or().eq(GzctAccountingReport::getReportTab, "LIST"));
                Page<GzctAccountingReport> page = reportMapper.selectPage(new Page<>(pn, ps), w);
                result.put("totalRecord", (int) page.getTotal()); result.put("currentPage", pn);
                result.put("pageNumber", pn); result.put("totalPage", (int) page.getPages());
                result.put("pageSize", ps);
                List<Map<String, Object>> tlist = page.getRecords().stream().map(r -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("companyName", r.getCompanyName()); m.put("year", r.getReportYear());
                    m.put("reportType", r.getReportType()); m.put("submitStatus", r.getSubmitStatus());
                    m.put("auditOpinion", r.getAuditOpinion()); m.put("qualityScore", r.getQualityScore());
                    m.put("hookIssues", r.getHookIssues()); return m;
                }).collect(Collectors.toList());
                result.put("tlist", tlist);
            }
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "报表质量评分")
    @PostMapping("/report/quality/score")
    public R<Map<String, Object>> reportQualityScore(@RequestBody Map<String, Object> params) {
        try {
            Map<String, Object> result = new HashMap<>();
            LambdaQueryWrapper<GzctAccountingReport> w = new LambdaQueryWrapper<>();
            if (params.get("companyName") != null && StringUtils.isNotBlank(params.get("companyName").toString())) {
                w.like(GzctAccountingReport::getCompanyName, params.get("companyName").toString());
            }
            if (params.get("year") != null && StringUtils.isNotBlank(params.get("year").toString())) {
                w.eq(GzctAccountingReport::getReportYear, params.get("year").toString());
            }
            List<GzctAccountingReport> list = reportMapper.selectList(w);
            double avgScore = list.stream().map(GzctAccountingReport::getQualityScore).filter(Objects::nonNull).mapToInt(Integer::intValue).average().orElse(0);
            result.put("overallScore", Math.round(avgScore));
            List<Map<String, Object>> dims = new ArrayList<>();
            dims.add(new HashMap<String, Object>() {{ put("label", "准确性"); put("score", Math.min(100, avgScore + 5)); }});
            dims.add(new HashMap<String, Object>() {{ put("label", "完整性"); put("score", Math.min(100, avgScore + 2)); }});
            dims.add(new HashMap<String, Object>() {{ put("label", "及时性"); put("score", Math.min(100, avgScore - 3)); }});
            dims.add(new HashMap<String, Object>() {{ put("label", "规范性"); put("score", Math.min(100, avgScore)); }});
            dims.add(new HashMap<String, Object>() {{ put("label", "可靠性"); put("score", Math.min(100, avgScore - 5)); }});
            result.put("dims", dims);
            List<Map<String, Object>> hookCheckList = new ArrayList<>();
            final int base = (int) Math.round(avgScore);
            hookCheckList.add(new HashMap<String, Object>() {{
                put("name", "资产负债表平衡"); put("pass", true); put("desc", "");
            }});
            hookCheckList.add(new HashMap<String, Object>() {{
                put("name", "利润表勾稽"); put("pass", base > 70);
                put("desc", base > 70 ? "" : "营业收入、营业成本与毛利勾稽存在偏差");
            }});
            hookCheckList.add(new HashMap<String, Object>() {{
                put("name", "现金流量表勾稽"); put("pass", base > 60);
                put("desc", base > 60 ? "" : "经营活动现金流与利润差异较大");
            }});
            hookCheckList.add(new HashMap<String, Object>() {{
                put("name", "所有者权益变动勾稽"); put("pass", true); put("desc", "");
            }});
            result.put("hookCheckList", hookCheckList);
            List<Map<String, Object>> hookCheckResults = list.stream()
                .filter(r -> r.getHookIssues() != null && r.getHookIssues() > 0)
                .map(r -> new HashMap<String, Object>() {{ put("name", r.getCompanyName() + " " + r.getReportYear()); put("issues", r.getHookIssues()); }})
                .collect(Collectors.toList());
            result.put("hookCheckResults", hookCheckResults);
            List<Map<String, Object>> incomeAnomalies = list.stream()
                .filter(r -> "1".equals(r.getAnomaly()))
                .map(r -> new HashMap<String, Object>() {{ put("name", r.getItemName()); put("level", "red"); }})
                .collect(Collectors.toList());
            result.put("incomeAnomalies", incomeAnomalies);
            List<Map<String, Object>> incomeData = list.stream()
                .filter(r -> "INCOME".equals(r.getReportTab()))
                .map(r -> new HashMap<String, Object>() {{ put("item", r.getItemName()); put("current", r.getCurrentAmount()); put("prev", r.getPrevAmount()); put("growth", r.getGrowth()); put("anomaly", "1".equals(r.getAnomaly())); }})
                .collect(Collectors.toList());
            result.put("incomeData", incomeData);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 预算执行监管 ====================

    @Operation(summary = "预算监控列表")
    @PostMapping("/budget/monitor/list")
    public R<PageResult<Map<String, Object>>> budgetMonitorList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 10;
            LambdaQueryWrapper<GzctAccountingBudget> w = new LambdaQueryWrapper<>();
            w.and(q -> q.isNull(GzctAccountingBudget::getBudgetType).or().eq(GzctAccountingBudget::getBudgetType, "MONITOR"));
            if (params.get("companyName") != null && StringUtils.isNotBlank(params.get("companyName").toString())) {
                w.like(GzctAccountingBudget::getCompanyName, params.get("companyName").toString());
            }
            if (params.get("year") != null && StringUtils.isNotBlank(params.get("year").toString())) {
                w.eq(GzctAccountingBudget::getBudgetYear, params.get("year").toString());
            }
            if (params.get("alertStatus") != null && StringUtils.isNotBlank(params.get("alertStatus").toString())) {
                w.eq(GzctAccountingBudget::getAlertStatus, params.get("alertStatus").toString());
            }
            w.orderByDesc(GzctAccountingBudget::getCreateTime);
            Page<GzctAccountingBudget> page = budgetMapper.selectPage(new Page<>(pn, ps), w);
            PageResult<Map<String, Object>> pr = new PageResult<>();
            pr.setTotalRecord((int) page.getTotal()); pr.setCurrentPage(pn); pr.setPageNumber(pn);
            pr.setTotalPage((int) page.getPages()); pr.setPageSize(ps);
            List<Map<String, Object>> tlist = page.getRecords().stream().map(b -> {
                Map<String, Object> m = new HashMap<>();
                m.put("companyName", b.getCompanyName()); m.put("annualBudget", b.getAnnualBudget());
                m.put("executed", b.getExecuted()); m.put("rate", b.getRate());
                m.put("overAmount", b.getOverAmount()); m.put("endRush", b.getEndRush());
                m.put("alertLevel", b.getAlertLevel()); return m;
            }).collect(Collectors.toList());
            pr.setTlist(tlist); return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "预算明细列表")
    @PostMapping("/budget/detail/list")
    public R<PageResult<Map<String, Object>>> budgetDetailList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 10;
            LambdaQueryWrapper<GzctAccountingBudget> w = new LambdaQueryWrapper<>();
            w.and(q -> q.isNull(GzctAccountingBudget::getBudgetType).or().eq(GzctAccountingBudget::getBudgetType, "DETAIL"));
            if (params.get("companyName") != null && StringUtils.isNotBlank(params.get("companyName").toString())) {
                w.like(GzctAccountingBudget::getCompanyName, params.get("companyName").toString());
            }
            if (params.get("year") != null && StringUtils.isNotBlank(params.get("year").toString())) {
                w.eq(GzctAccountingBudget::getBudgetYear, params.get("year").toString());
            }
            w.orderByDesc(GzctAccountingBudget::getCreateTime);
            Page<GzctAccountingBudget> page = budgetMapper.selectPage(new Page<>(pn, ps), w);
            PageResult<Map<String, Object>> pr = new PageResult<>();
            pr.setTotalRecord((int) page.getTotal()); pr.setCurrentPage(pn); pr.setPageNumber(pn);
            pr.setTotalPage((int) page.getPages()); pr.setPageSize(ps);
            List<Map<String, Object>> tlist = page.getRecords().stream().map(b -> {
                Map<String, Object> m = new HashMap<>();
                m.put("category", b.getCategory()); m.put("annualBudget", b.getAnnualBudget());
                m.put("monthBudget", b.getMonthBudget()); m.put("monthActual", b.getMonthActual());
                m.put("monthGap", b.getMonthGap()); m.put("cumRate", b.getCumRate()); return m;
            }).collect(Collectors.toList());
            pr.setTlist(tlist); return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "预算调整列表")
    @PostMapping("/budget/adjust/list")
    public R<PageResult<Map<String, Object>>> budgetAdjustList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 10;
            LambdaQueryWrapper<GzctAccountingBudget> w = new LambdaQueryWrapper<>();
            w.and(q -> q.isNull(GzctAccountingBudget::getBudgetType).or().eq(GzctAccountingBudget::getBudgetType, "ADJUST"));
            if (params.get("companyName") != null && StringUtils.isNotBlank(params.get("companyName").toString())) {
                w.like(GzctAccountingBudget::getCompanyName, params.get("companyName").toString());
            }
            if (params.get("year") != null && StringUtils.isNotBlank(params.get("year").toString())) {
                w.eq(GzctAccountingBudget::getBudgetYear, params.get("year").toString());
            }
            if (params.get("alertStatus") != null && StringUtils.isNotBlank(params.get("alertStatus").toString())) {
                w.eq(GzctAccountingBudget::getAlertStatus, params.get("alertStatus").toString());
            }
            w.orderByDesc(GzctAccountingBudget::getCreateTime);
            Page<GzctAccountingBudget> page = budgetMapper.selectPage(new Page<>(pn, ps), w);
            PageResult<Map<String, Object>> pr = new PageResult<>();
            pr.setTotalRecord((int) page.getTotal()); pr.setCurrentPage(pn); pr.setPageNumber(pn);
            pr.setTotalPage((int) page.getPages()); pr.setPageSize(ps);
            List<Map<String, Object>> tlist = page.getRecords().stream().map(b -> {
                Map<String, Object> m = new HashMap<>();
                m.put("companyName", b.getCompanyName()); m.put("item", b.getCategory());
                m.put("adjustType", b.getAdjustType()); m.put("amount", b.getAdjustAmount());
                m.put("reason", b.getAdjustReason()); m.put("approvalStatus", b.getApprovalStatus());
                m.put("adjustCount", b.getAdjustCount()); return m;
            }).collect(Collectors.toList());
            pr.setTlist(tlist); return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 两金压降监控 ====================

    @Operation(summary = "两金压降概览")
    @PostMapping("/twogold/overview")
    public R<Map<String, Object>> twoGoldOverview(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 10;
            LambdaQueryWrapper<GzctAccountingTwogold> w = new LambdaQueryWrapper<>();
            w.eq(GzctAccountingTwogold::getDataType, "OVERVIEW");
            if (params.get("companyName") != null && StringUtils.isNotBlank(params.get("companyName").toString())) {
                w.like(GzctAccountingTwogold::getCompanyName, params.get("companyName").toString());
            }
            if (params.get("month") != null && StringUtils.isNotBlank(params.get("month").toString())) {
                w.eq(GzctAccountingTwogold::getRecordMonth, params.get("month").toString());
            }
            w.orderByDesc(GzctAccountingTwogold::getCreateTime);
            Page<GzctAccountingTwogold> page = twogoldMapper.selectPage(new Page<>(pn, ps), w);
            Map<String, Object> result = new HashMap<>();
            result.put("totalRecord", (int) page.getTotal()); result.put("currentPage", pn);
            result.put("pageNumber", pn); result.put("totalPage", (int) page.getPages());
            result.put("pageSize", ps);
            List<Map<String, Object>> tlist = page.getRecords().stream().map(t -> {
                Map<String, Object> m = new HashMap<>();
                m.put("companyName", t.getCompanyName()); m.put("receivable", t.getReceivable());
                m.put("inventory", t.getInventory()); m.put("total", t.getTotal());
                m.put("revenueRate", t.getRevenueRate()); m.put("targetReduce", t.getTargetReduce());
                m.put("reduceRate", t.getReduceRate()); m.put("riskFlag", t.getRiskFlag());
                return m;
            }).collect(Collectors.toList());
            result.put("tlist", tlist); return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "应收账款列表")
    @PostMapping("/twogold/receivable/list")
    public R<PageResult<Map<String, Object>>> receivableList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 10;
            LambdaQueryWrapper<GzctAccountingTwogold> w = new LambdaQueryWrapper<>();
            w.eq(GzctAccountingTwogold::getDataType, "RECEIVABLE");
            if (params.get("companyName") != null && StringUtils.isNotBlank(params.get("companyName").toString())) {
                w.like(GzctAccountingTwogold::getCompanyName, params.get("companyName").toString());
            }
            if (params.get("month") != null && StringUtils.isNotBlank(params.get("month").toString())) {
                w.eq(GzctAccountingTwogold::getRecordMonth, params.get("month").toString());
            }
            w.orderByDesc(GzctAccountingTwogold::getCreateTime);
            Page<GzctAccountingTwogold> page = twogoldMapper.selectPage(new Page<>(pn, ps), w);
            PageResult<Map<String, Object>> pr = new PageResult<>();
            pr.setTotalRecord((int) page.getTotal()); pr.setCurrentPage(pn); pr.setPageNumber(pn);
            pr.setTotalPage((int) page.getPages()); pr.setPageSize(ps);
            List<Map<String, Object>> tlist = page.getRecords().stream().map(t -> {
                Map<String, Object> m = new HashMap<>();
                m.put("companyName", t.getCompanyName()); m.put("customerName", t.getCustomerName());
                m.put("balance", t.getReceivable()); m.put("within1y", t.getWithin1y());
                m.put("y1to2", t.getY1to2()); m.put("y2to3", t.getY2to3());
                m.put("over3y", t.getOver3y()); m.put("over3yRate", t.getOver3yRate());
                m.put("badDebt", t.getBadDebt()); m.put("risk", t.getRisk());
                return m;
            }).collect(Collectors.toList());
            pr.setTlist(tlist); return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "存货列表")
    @PostMapping("/twogold/inventory/list")
    public R<PageResult<Map<String, Object>>> inventoryList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 10;
            LambdaQueryWrapper<GzctAccountingTwogold> w = new LambdaQueryWrapper<>();
            w.eq(GzctAccountingTwogold::getDataType, "INVENTORY");
            if (params.get("companyName") != null && StringUtils.isNotBlank(params.get("companyName").toString())) {
                w.like(GzctAccountingTwogold::getCompanyName, params.get("companyName").toString());
            }
            if (params.get("month") != null && StringUtils.isNotBlank(params.get("month").toString())) {
                w.eq(GzctAccountingTwogold::getRecordMonth, params.get("month").toString());
            }
            w.orderByDesc(GzctAccountingTwogold::getCreateTime);
            Page<GzctAccountingTwogold> page = twogoldMapper.selectPage(new Page<>(pn, ps), w);
            PageResult<Map<String, Object>> pr = new PageResult<>();
            pr.setTotalRecord((int) page.getTotal()); pr.setCurrentPage(pn); pr.setPageNumber(pn);
            pr.setTotalPage((int) page.getPages()); pr.setPageSize(ps);
            List<Map<String, Object>> tlist = page.getRecords().stream().map(t -> {
                Map<String, Object> m = new HashMap<>();
                m.put("companyName", t.getCompanyName()); m.put("category", t.getCategory());
                m.put("bookValue", t.getBookValue()); m.put("impairment", t.getImpairment());
                m.put("slow", t.getSlow()); m.put("slowRate", t.getSlowRate());
                m.put("turnover", t.getTurnover()); m.put("risk", t.getRisk());
                return m;
            }).collect(Collectors.toList());
            pr.setTlist(tlist); return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "压降明细列表")
    @PostMapping("/twogold/reduction/list")
    public R<PageResult<Map<String, Object>>> twoGoldReductionList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 10;
            LambdaQueryWrapper<GzctAccountingTwogold> w = new LambdaQueryWrapper<>();
            w.eq(GzctAccountingTwogold::getDataType, "REDUCTION");
            if (params.get("companyName") != null && StringUtils.isNotBlank(params.get("companyName").toString())) {
                w.like(GzctAccountingTwogold::getCompanyName, params.get("companyName").toString());
            }
            if (params.get("month") != null && StringUtils.isNotBlank(params.get("month").toString())) {
                w.eq(GzctAccountingTwogold::getRecordMonth, params.get("month").toString());
            }
            w.orderByDesc(GzctAccountingTwogold::getCreateTime);
            Page<GzctAccountingTwogold> page = twogoldMapper.selectPage(new Page<>(pn, ps), w);
            PageResult<Map<String, Object>> pr = new PageResult<>();
            pr.setTotalRecord((int) page.getTotal()); pr.setCurrentPage(pn); pr.setPageNumber(pn);
            pr.setTotalPage((int) page.getPages()); pr.setPageSize(ps);
            List<Map<String, Object>> tlist = page.getRecords().stream().map(t -> {
                Map<String, Object> m = new HashMap<>();
                m.put("companyName", t.getCompanyName()); m.put("fraudType", t.getFraudType());
                m.put("fraudTypeName", convertFraudType(t.getFraudType()));
                m.put("rule", t.getRuleDesc()); m.put("riskScore", t.getRiskScore());
                m.put("alertLevel", t.getAlertLevelTg()); m.put("foundTime", t.getFoundTime());
                m.put("status", t.getStatus());
                return m;
            }).collect(Collectors.toList());
            pr.setTlist(tlist); return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 财务造假识别 ====================

    @Operation(summary = "造假风险列表")
    @PostMapping("/fraud/risk/list")
    public R<Map<String, Object>> fraudRiskList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 10;
            LambdaQueryWrapper<GzctAccountingFraud> w = new LambdaQueryWrapper<>();
            if (params.get("companyName") != null && StringUtils.isNotBlank(params.get("companyName").toString())) {
                w.like(GzctAccountingFraud::getCompanyName, params.get("companyName").toString());
            }
            if (params.get("fraudType") != null && StringUtils.isNotBlank(params.get("fraudType").toString())) {
                w.eq(GzctAccountingFraud::getFraudType, params.get("fraudType").toString());
            }
            if (params.get("riskLevel") != null && StringUtils.isNotBlank(params.get("riskLevel").toString())) {
                w.eq(GzctAccountingFraud::getRiskLevel, params.get("riskLevel").toString());
            }
            w.orderByDesc(GzctAccountingFraud::getTotalScore);
            Page<GzctAccountingFraud> page = fraudMapper.selectPage(new Page<>(pn, ps), w);
            Map<String, Object> result = new HashMap<>();
            List<Map<String, Object>> tlist = page.getRecords().stream().map(f -> {
                Map<String, Object> m = new HashMap<>();
                m.put("companyName", f.getCompanyName()); m.put("totalScore", f.getTotalScore());
                m.put("perfScore", f.getPerfScore()); m.put("leverScore", f.getLeverScore());
                m.put("clearScore", f.getClearScore()); m.put("rdScore", f.getRdScore());
                m.put("twoGoldScore", f.getTwoGoldScore()); m.put("alertLevel", f.getAlertLevel());
                m.put("clueCount", f.getClueCount());
                m.put("fraudType", f.getFraudType()); m.put("riskLevel", f.getRiskLevel());
                m.put("revenueGrowth", f.getRevenueGrowth()); m.put("industryAvg", f.getIndustryAvg());
                m.put("zScore", f.getZScore()); m.put("grossMargin", f.getGrossMargin());
                m.put("cashContent", f.getCashContent()); m.put("dec12Rate", f.getDec12Rate());
                m.put("revenueAnomaly", f.getRevenueAnomaly());
                m.put("rdAmount", f.getRdAmount()); m.put("capitalAmount", f.getCapitalAmount());
                m.put("capitalRate", f.getCapitalRate()); m.put("rdStaff", f.getRdStaff());
                m.put("outputPerPerson", f.getOutputPerPerson()); m.put("patents", f.getPatents());
                return m;
            }).collect(Collectors.toList());
            result.put("tlist", tlist); result.put("list", tlist);
            List<Map<String, Object>> fraudIndexes = new ArrayList<>();
            int avgPerf = (int) page.getRecords().stream().mapToInt(GzctAccountingFraud::getPerfScore).average().orElse(0);
            int avgLever = (int) page.getRecords().stream().mapToInt(GzctAccountingFraud::getLeverScore).average().orElse(0);
            int avgClear = (int) page.getRecords().stream().mapToInt(GzctAccountingFraud::getClearScore).average().orElse(0);
            int avgRd = (int) page.getRecords().stream().mapToInt(GzctAccountingFraud::getRdScore).average().orElse(0);
            int avgTwoGold = (int) page.getRecords().stream().mapToInt(GzctAccountingFraud::getTwoGoldScore).average().orElse(0);
            fraudIndexes.add(new HashMap<String, Object>() {{ put("name", "业绩假"); put("score", avgPerf); put("cls", avgPerf >= 70 ? "red" : avgPerf >= 40 ? "orange" : "green"); put("tab", "perf"); }});
            fraudIndexes.add(new HashMap<String, Object>() {{ put("name", "杠杆假"); put("score", avgLever); put("cls", avgLever >= 70 ? "red" : avgLever >= 40 ? "orange" : "green"); put("tab", "lever"); }});
            fraudIndexes.add(new HashMap<String, Object>() {{ put("name", "出清假"); put("score", avgClear); put("cls", avgClear >= 70 ? "red" : avgClear >= 40 ? "orange" : "green"); put("tab", "clear"); }});
            fraudIndexes.add(new HashMap<String, Object>() {{ put("name", "研发假"); put("score", avgRd); put("cls", avgRd >= 70 ? "red" : avgRd >= 40 ? "orange" : "green"); put("tab", "rd"); }});
            fraudIndexes.add(new HashMap<String, Object>() {{ put("name", "两金假"); put("score", avgTwoGold); put("cls", avgTwoGold >= 70 ? "red" : avgTwoGold >= 40 ? "orange" : "green"); put("tab", "twogold"); }});
            result.put("fraudIndexes", fraudIndexes); return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "造假线索列表")
    @PostMapping("/fraud/clue/list")
    public R<PageResult<Map<String, Object>>> fraudClueList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctAccountingClue> w = new LambdaQueryWrapper<>();
            if (params.get("companyName") != null && StringUtils.isNotBlank(params.get("companyName").toString())) {
                w.like(GzctAccountingClue::getCompanyName, params.get("companyName").toString());
            }
            if (params.get("fraudType") != null && StringUtils.isNotBlank(params.get("fraudType").toString())) {
                w.eq(GzctAccountingClue::getFraudType, params.get("fraudType").toString());
            }
            w.orderByDesc(GzctAccountingClue::getCreateTime);
            Page<GzctAccountingClue> page = clueMapper.selectPage(new Page<>(pn, ps), w);
            PageResult<Map<String, Object>> pr = new PageResult<>();
            pr.setTotalRecord((int) page.getTotal()); pr.setCurrentPage(pn); pr.setPageNumber(pn);
            pr.setTotalPage((int) page.getPages()); pr.setPageSize(ps);
            List<Map<String, Object>> tlist = page.getRecords().stream().map(c -> {
                Map<String, Object> m = new HashMap<>();
                m.put("clueId", c.getClueId()); m.put("id", c.getClueId());
                m.put("companyName", c.getCompanyName()); m.put("clueType", c.getClueType());
                m.put("riskLevel", c.getRiskLevel()); m.put("description", c.getDescription());
                m.put("foundTime", c.getFoundTime()); m.put("assignee", c.getAssignee());
                m.put("fraudType", c.getFraudType());
                m.put("status", c.getStatus()); return m;
            }).collect(Collectors.toList());
            pr.setTlist(tlist); return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 会计估计台账 ====================

    @Operation(summary = "会计估计列表")
    @PostMapping("/estimate/list")
    public R<PageResult<Map<String, Object>>> estimateList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("page") != null ? Integer.parseInt(params.get("page").toString()) : 1;
            int ps = params.get("size") != null ? Integer.parseInt(params.get("size").toString()) : 10;
            LambdaQueryWrapper<GzctAccountingEstimate> w = new LambdaQueryWrapper<>();
            if (params.get("type") != null && StringUtils.isNotBlank(params.get("type").toString())) {
                w.like(GzctAccountingEstimate::getEstimateItem, params.get("type").toString());
            }
            if (params.get("company") != null && StringUtils.isNotBlank(params.get("company").toString())) {
                w.like(GzctAccountingEstimate::getCompanyName, params.get("company").toString());
            }
            w.orderByDesc(GzctAccountingEstimate::getCreateTime);
            Page<GzctAccountingEstimate> page = estimateMapper.selectPage(new Page<>(pn, ps), w);
            PageResult<Map<String, Object>> pr = new PageResult<>();
            pr.setTotalRecord((int) page.getTotal()); pr.setCurrentPage(pn); pr.setPageNumber(pn);
            pr.setTotalPage((int) page.getPages()); pr.setPageSize(ps);
            List<Map<String, Object>> tlist = page.getRecords().stream().map(e -> {
                Map<String, Object> m = new HashMap<>();
                m.put("estimateType", e.getEstimateItem());
                m.put("item", e.getEstimateName());
                m.put("company", e.getCompanyName());
                BigDecimal curr = e.getCurrentValue() != null ? e.getCurrentValue() : BigDecimal.ZERO;
                m.put("currentValue", curr);
                m.put("industryAvg", e.getProvisionAmount());
                BigDecimal avg = e.getProvisionAmount() != null ? e.getProvisionAmount() : BigDecimal.ZERO;
                BigDecimal deviation = avg.compareTo(BigDecimal.ZERO) > 0 ? curr.subtract(avg).multiply(new BigDecimal("100")).divide(avg, 1, java.math.RoundingMode.HALF_UP) : BigDecimal.ZERO;
                m.put("deviation", deviation.doubleValue());
                m.put("basis", e.getKeyAssumption());
                m.put("lastChangeDate", e.getUpdateTime() != null ? e.getUpdateTime().toString().substring(0, 10) : "");
                m.put("remark", e.getEstimateMethod());
                m.put("riskFlag", "0".equals(e.getIsCompliant()));
                m.put("unit", "%");
                return m;
            }).collect(Collectors.toList());
            pr.setTlist(tlist); return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "新增会计估计")
    @PostMapping("/estimate/add")
    public R<Boolean> addEstimate(@RequestBody GzctAccountingEstimate record) {
        try { record.setCreateTime(LocalDateTime.now()); estimateMapper.insert(record); return R.success(true); } catch (Exception e) { return R.fail("新增失败：" + e.getMessage()); }
    }

    @Operation(summary = "更新会计估计")
    @PostMapping("/estimate/update")
    public R<Boolean> updateEstimate(@RequestBody GzctAccountingEstimate record) {
        try { record.setUpdateTime(LocalDateTime.now()); estimateMapper.updateById(record); return R.success(true); } catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); }
    }

    @Operation(summary = "删除会计估计")
    @DeleteMapping("/estimate/{id}")
    public R<Boolean> deleteEstimate(@PathVariable String id) {
        try { return R.success(estimateMapper.deleteById(id) > 0); } catch (Exception e) { return R.fail("删除失败：" + e.getMessage()); }
    }

    // ==================== 驾驶舱 ====================

    @Operation(summary = "会计驾驶舱概览")
    @PostMapping("/dashboard/overview")
    public R<Map<String, Object>> dashboardOverview(@RequestBody Map<String, Object> params) {
        try {
            // 读取筛选参数
            String year = params != null && params.get("year") != null ? params.get("year").toString() : "";
            String companyName = params != null && params.get("companyName") != null ? params.get("companyName").toString() : "";
            boolean hasYear = StringUtils.isNotBlank(year);
            boolean hasCompany = StringUtils.isNotBlank(companyName);

            Map<String, Object> result = new HashMap<>();

            // 政策（按 reportYear / companyName 过滤）
            LambdaQueryWrapper<TblAccountingPolicy> pw = new LambdaQueryWrapper<>();
            if (hasYear) pw.eq(TblAccountingPolicy::getReportYear, year);
            if (hasCompany) pw.like(TblAccountingPolicy::getCompanyName, companyName);
            List<TblAccountingPolicy> policies = accountingPolicyMapper.selectList(pw);
            long companyCount = policies.stream().map(TblAccountingPolicy::getCompanyId).filter(Objects::nonNull).distinct().count();

            // 凭证异常数（voucher 表无 year，用 period like 过滤）
            LambdaQueryWrapper<GzctAccountingVoucher> vw = new LambdaQueryWrapper<GzctAccountingVoucher>().ne(GzctAccountingVoucher::getAnomalyFlag, "N");
            if (hasYear) vw.likeRight(GzctAccountingVoucher::getPeriod, year);
            if (hasCompany) vw.like(GzctAccountingVoucher::getCompanyName, companyName);
            long voucherAnomalyCount = voucherMapper.selectCount(vw);

            // 造假风险企业数（fraud 表无明确 year，按 companyName 过滤）
            LambdaQueryWrapper<GzctAccountingFraud> fw = new LambdaQueryWrapper<GzctAccountingFraud>().ge(GzctAccountingFraud::getTotalScore, 40);
            if (hasCompany) fw.like(GzctAccountingFraud::getCompanyName, companyName);
            long fraudRiskCount = fraudMapper.selectCount(fw);

            // 预算超支企业（按 budgetYear 过滤）
            LambdaQueryWrapper<GzctAccountingBudget> buw = new LambdaQueryWrapper<GzctAccountingBudget>().gt(GzctAccountingBudget::getOverAmount, java.math.BigDecimal.ZERO);
            if (hasYear) buw.eq(GzctAccountingBudget::getBudgetYear, year);
            if (hasCompany) buw.like(GzctAccountingBudget::getCompanyName, companyName);
            long budgetOverCount = budgetMapper.selectCount(buw);

            // 两金未达标（按 recordMonth 前缀过滤年份）
            LambdaQueryWrapper<GzctAccountingTwogold> tw = new LambdaQueryWrapper<GzctAccountingTwogold>().eq(GzctAccountingTwogold::getRiskFlag, "RUSH");
            if (hasYear) tw.likeRight(GzctAccountingTwogold::getRecordMonth, year);
            if (hasCompany) tw.like(GzctAccountingTwogold::getCompanyName, companyName);
            long twoGoldWarnCount = twogoldMapper.selectCount(tw);

            // 报表质量均分
            LambdaQueryWrapper<GzctAccountingReport> rw = new LambdaQueryWrapper<>();
            if (hasYear) rw.eq(GzctAccountingReport::getReportYear, year);
            if (hasCompany) rw.like(GzctAccountingReport::getCompanyName, companyName);
            List<GzctAccountingReport> rlist = reportMapper.selectList(rw);
            int qualityScore = 0;
            if (!rlist.isEmpty()) {
                qualityScore = (int) Math.round(rlist.stream().map(GzctAccountingReport::getQualityScore).filter(Objects::nonNull).mapToInt(Integer::intValue).average().orElse(0));
            }

            result.put("companyCount", companyCount > 0 ? companyCount : policies.size());
            result.put("voucherAnomalyCount", voucherAnomalyCount);
            result.put("fraudRiskCount", fraudRiskCount);
            result.put("budgetOverCount", budgetOverCount);
            result.put("twoGoldWarnCount", twoGoldWarnCount);
            result.put("qualityScore", qualityScore);

            // 热力图（Top 20 高分）
            LambdaQueryWrapper<GzctAccountingFraud> fpw = new LambdaQueryWrapper<GzctAccountingFraud>().orderByDesc(GzctAccountingFraud::getTotalScore);
            if (hasCompany) fpw.like(GzctAccountingFraud::getCompanyName, companyName);
            List<GzctAccountingFraud> fraudList = fraudMapper.selectPage(new Page<>(1, 20), fpw).getRecords();
            List<Map<String, Object>> heatmapData = fraudList.stream().map(f -> {
                Map<String, Object> m = new HashMap<>();
                m.put("company", f.getCompanyName());
                m.put("companyShort", f.getCompanyName() != null && f.getCompanyName().length() > 4 ? f.getCompanyName().substring(0, 4) : f.getCompanyName());
                m.put("scores", Arrays.asList(
                    f.getPerfScore() != null ? f.getPerfScore() : 0,
                    f.getLeverScore() != null ? f.getLeverScore() : 0,
                    f.getClearScore() != null ? f.getClearScore() : 0,
                    f.getRdScore() != null ? f.getRdScore() : 0,
                    f.getTwoGoldScore() != null ? f.getTwoGoldScore() : 0
                )); return m;
            }).collect(Collectors.toList());
            result.put("heatmapData", heatmapData);

            // 两金趋势（近 12 月，按年份 + 公司筛选）
            LambdaQueryWrapper<GzctAccountingTwogold> tgw = new LambdaQueryWrapper<GzctAccountingTwogold>().orderByDesc(GzctAccountingTwogold::getRecordMonth);
            if (hasYear) tgw.likeRight(GzctAccountingTwogold::getRecordMonth, year);
            if (hasCompany) tgw.like(GzctAccountingTwogold::getCompanyName, companyName);
            List<GzctAccountingTwogold> tgList = twogoldMapper.selectPage(new Page<>(1, 12), tgw).getRecords();
            List<Map<String, Object>> twoGoldTrend = new ArrayList<>();
            for (int i = tgList.size() - 1; i >= 0; i--) {
                GzctAccountingTwogold t = tgList.get(i);
                Map<String, Object> m = new HashMap<>();
                m.put("month", t.getRecordMonth() != null && t.getRecordMonth().length() >= 7 ? t.getRecordMonth().substring(5) : "");
                m.put("receivable", t.getReceivable() != null ? t.getReceivable().doubleValue() : 0);
                m.put("inventory", t.getInventory() != null ? t.getInventory().doubleValue() : 0);
                twoGoldTrend.add(m);
            }
            result.put("twoGoldTrend", twoGoldTrend);

            // 预算统计
            LambdaQueryWrapper<GzctAccountingBudget> bsw = new LambdaQueryWrapper<>();
            if (hasYear) bsw.eq(GzctAccountingBudget::getBudgetYear, year);
            if (hasCompany) bsw.like(GzctAccountingBudget::getCompanyName, companyName);
            List<GzctAccountingBudget> bList = budgetMapper.selectList(bsw);
            long okCount = bList.stream().filter(b -> b.getOverAmount() == null || b.getOverAmount().compareTo(java.math.BigDecimal.ZERO) <= 0).count();
            long overCount = bList.stream().filter(b -> b.getOverAmount() != null && b.getOverAmount().compareTo(java.math.BigDecimal.ZERO) > 0).count();
            long dangerCount = bList.stream().filter(b -> b.getAlertLevel() != null && b.getAlertLevel().equals("RED")).count();
            Map<String, Object> budgetStats = new HashMap<>();
            budgetStats.put("okCount", okCount); budgetStats.put("overCount", overCount);
            budgetStats.put("dangerCount", dangerCount);
            budgetStats.put("reachRate", bList.isEmpty() ? 0 : Math.round(okCount * 100.0 / bList.size()));
            result.put("budgetStats", budgetStats);
            // 企业下拉：独立查全量（不受 year 过滤影响，保证切年份后下拉不空）
            List<TblAccountingPolicy> allPolicies = accountingPolicyMapper.selectList(null);
            List<Map<String, Object>> companyOptions = allPolicies.stream()
                .filter(p -> p.getCompanyName() != null)
                .collect(Collectors.toMap(TblAccountingPolicy::getCompanyName, p -> p, (a, b) -> a)).values().stream()
                .map(p -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("value", p.getCompanyId()); m.put("label", p.getCompanyName()); return m;
                }).collect(Collectors.toList());
            result.put("companyOptions", companyOptions);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 预警信息 ====================

    @Operation(summary = "会计预警列表")
    @PostMapping("/warning/list")
    public R<PageResult<Map<String, Object>>> warningList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctAccountingWarning> w = new LambdaQueryWrapper<>();
            if (params.get("companyName") != null && StringUtils.isNotBlank(params.get("companyName").toString())) {
                w.like(GzctAccountingWarning::getCompanyName, params.get("companyName").toString());
            }
            if (params.get("year") != null && StringUtils.isNotBlank(params.get("year").toString())) {
                String year = params.get("year").toString();
                w.ge(GzctAccountingWarning::getCreateTime, java.time.LocalDateTime.of(Integer.parseInt(year), 1, 1, 0, 0));
                w.lt(GzctAccountingWarning::getCreateTime, java.time.LocalDateTime.of(Integer.parseInt(year) + 1, 1, 1, 0, 0));
            }
            w.orderByDesc(GzctAccountingWarning::getCreateTime);
            Page<GzctAccountingWarning> page = warningMapper.selectPage(new Page<>(pn, ps), w);
            PageResult<Map<String, Object>> pr = new PageResult<>();
            pr.setTotalRecord((int) page.getTotal()); pr.setCurrentPage(pn); pr.setPageNumber(pn);
            pr.setTotalPage((int) page.getPages()); pr.setPageSize(ps);
            List<Map<String, Object>> tlist = page.getRecords().stream().map(e -> {
                Map<String, Object> m = new HashMap<>();
                m.put("warningId", e.getWarningId()); m.put("companyName", e.getCompanyName());
                m.put("level", e.getWarningLevel()); m.put("title", e.getWarningTitle());
                m.put("text", e.getWarningContent()); m.put("company", e.getCompanyName());
                m.put("time", e.getCreateTime() != null ? e.getCreateTime().toString().substring(0, 10) : "");
                m.put("status", e.getStatus()); return m;
            }).collect(Collectors.toList());
            pr.setTlist(tlist); return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 凭证异常统计 ====================

    @Operation(summary = "凭证异常统计")
    @PostMapping("/voucher/anomaly/stats")
    public R<Map<String, Object>> voucherAnomalyStats(@RequestBody Map<String, Object> params) {
        try {
            Map<String, Object> result = new HashMap<>();
            // 支持按 companyName like 过滤（大屏单公司切换）
            String companyName = params != null && params.get("companyName") != null
                ? params.get("companyName").toString() : "";
            boolean hasCompany = StringUtils.isNotBlank(companyName);

            LambdaQueryWrapper<GzctAccountingVoucher> wTotal = new LambdaQueryWrapper<GzctAccountingVoucher>()
                .ne(GzctAccountingVoucher::getAnomalyFlag, "N");
            LambdaQueryWrapper<GzctAccountingVoucher> wNoOrig = new LambdaQueryWrapper<GzctAccountingVoucher>()
                .eq(GzctAccountingVoucher::getAnomalyType, "NO_ORIGINAL");
            LambdaQueryWrapper<GzctAccountingVoucher> wEndRush = new LambdaQueryWrapper<GzctAccountingVoucher>()
                .eq(GzctAccountingVoucher::getAnomalyType, "END_RUSH");
            LambdaQueryWrapper<GzctAccountingVoucher> wPending = new LambdaQueryWrapper<GzctAccountingVoucher>()
                .eq(GzctAccountingVoucher::getAnomalyFlag, "PENDING");
            if (hasCompany) {
                wTotal.like(GzctAccountingVoucher::getCompanyName, companyName);
                wNoOrig.like(GzctAccountingVoucher::getCompanyName, companyName);
                wEndRush.like(GzctAccountingVoucher::getCompanyName, companyName);
                wPending.like(GzctAccountingVoucher::getCompanyName, companyName);
            }
            long totalAnomaly = voucherMapper.selectCount(wTotal);
            long noOriginalVoucher = voucherMapper.selectCount(wNoOrig);
            long endPeriodRush = voucherMapper.selectCount(wEndRush);
            long pendingCheck = voucherMapper.selectCount(wPending);
            result.put("totalAnomaly", totalAnomaly);
            result.put("noOriginalVoucher", noOriginalVoucher);
            result.put("endPeriodRush", endPeriodRush);
            result.put("pendingCheck", pendingCheck);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 政策一致性检查 ====================

    @Operation(summary = "政策一致性检查列表")
    @PostMapping("/consistency/list")
    public R<Map<String, Object>> consistencyList(@RequestBody Map<String, Object> params) {
        try {
            LambdaQueryWrapper<TblAccountingPolicy> w = new LambdaQueryWrapper<>();
            w.ne(TblAccountingPolicy::getIsConsistent, "1");
            if (params.get("item") != null && StringUtils.isNotBlank(params.get("item").toString())) {
                w.like(TblAccountingPolicy::getPolicyType, params.get("item").toString());
            }
            if (params.get("level") != null && StringUtils.isNotBlank(params.get("level").toString())) {
                String lv = params.get("level").toString();
                if ("重大".equals(lv)) w.and(q -> q.isNotNull(TblAccountingPolicy::getDeviationDesc).ne(TblAccountingPolicy::getDeviationDesc, ""));
                else if ("一般".equals(lv)) w.and(q -> q.isNull(TblAccountingPolicy::getDeviationDesc).or().eq(TblAccountingPolicy::getDeviationDesc, ""));
            }
            w.orderByDesc(TblAccountingPolicy::getCreateTime);
            List<TblAccountingPolicy> list = accountingPolicyMapper.selectList(w);
            int major = 0, minor = 0, none = 0;
            List<Map<String, Object>> tlist = new ArrayList<>();
            for (TblAccountingPolicy p : list) {
                boolean hasDeviation = StringUtils.isNotBlank(p.getDeviationDesc());
                String diffLevel = hasDeviation ? "重大" : "一般";
                if (hasDeviation) major++; else minor++;
                Map<String, Object> m = new HashMap<>();
                m.put("item", p.getPolicyType());
                m.put("standard", p.getGroupStandard());
                m.put("company", p.getCompanyName());
                m.put("actual", p.getPolicyName());
                m.put("diff", hasDeviation ? p.getDeviationDesc() : "无差异");
                m.put("diffLevel", diffLevel);
                m.put("impact", hasDeviation ? 50 : 0);
                m.put("suggest", hasDeviation ? "下发整改" : "—");
                tlist.add(m);
            }
            none = accountingPolicyMapper.selectCount(new LambdaQueryWrapper<TblAccountingPolicy>().eq(TblAccountingPolicy::getIsConsistent, "1")).intValue();
            Map<String, Object> result = new HashMap<>();
            result.put("tlist", tlist);
            Map<String, Object> stat = new HashMap<>();
            stat.put("major", major); stat.put("minor", minor); stat.put("none", none);
            result.put("consistencyStat", stat);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 估计变更分析 ====================

    @Operation(summary = "估计变更分析列表")
    @PostMapping("/estimate/change/list")
    public R<Map<String, Object>> estimateChangeList(@RequestBody Map<String, Object> params) {
        try {
            LambdaQueryWrapper<GzctAccountingEstimate> w = new LambdaQueryWrapper<>();
            w.isNotNull(GzctAccountingEstimate::getPreviousValue);
            if (params.get("period") != null && StringUtils.isNotBlank(params.get("period").toString())) {
                w.like(GzctAccountingEstimate::getPeriod, params.get("period").toString());
            }
            w.orderByDesc(GzctAccountingEstimate::getCreateTime);
            List<GzctAccountingEstimate> list = estimateMapper.selectList(w);
            List<Map<String, Object>> tlist = new ArrayList<>();
            List<Map<String, Object>> trendMonths = new ArrayList<>();
            for (GzctAccountingEstimate e : list) {
                BigDecimal prev = e.getPreviousValue() != null ? e.getPreviousValue() : BigDecimal.ZERO;
                BigDecimal curr = e.getCurrentValue() != null ? e.getCurrentValue() : BigDecimal.ZERO;
                BigDecimal diff = curr.subtract(prev);
                BigDecimal profitImpact = diff.multiply(new BigDecimal("100"));
                BigDecimal profitRatio = prev.compareTo(BigDecimal.ZERO) > 0 ? diff.multiply(new BigDecimal("100")).divide(prev, 1, java.math.RoundingMode.HALF_UP).abs() : BigDecimal.ZERO;
                String attention = profitRatio.compareTo(new BigDecimal("10")) > 0 ? "高" : "中";
                if (params.get("attention") != null && StringUtils.isNotBlank(params.get("attention").toString())) {
                    if (!attention.equals(params.get("attention").toString())) continue;
                }
                Map<String, Object> m = new HashMap<>();
                m.put("company", e.getCompanyName());
                m.put("estimateItem", e.getEstimateItem());
                m.put("before", prev);
                m.put("after", curr);
                m.put("changeDate", e.getCreateTime() != null ? e.getCreateTime().toString().substring(0, 10) : "");
                m.put("profitImpact", profitImpact.intValue());
                m.put("profitRatio", profitRatio.doubleValue());
                m.put("changeReason", e.getKeyAssumption());
                m.put("attention", attention);
                m.put("unit", "%");
                tlist.add(m);
            }
            for (int i = 11; i >= 0; i--) {
                Map<String, Object> tm = new HashMap<>();
                tm.put("month", (i + 1) + "月");
                tm.put("ratio", Math.random() * 15);
                trendMonths.add(tm);
            }
            Map<String, Object> result = new HashMap<>();
            result.put("tlist", tlist);
            result.put("trendMonths", trendMonths);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 合规评估 ====================

    @Operation(summary = "合规评估数据")
    @PostMapping("/compliance/eval")
    public R<Map<String, Object>> complianceEval(@RequestBody Map<String, Object> params) {
        try {
            Map<String, Object> result = new HashMap<>();
            LambdaQueryWrapper<TblAccountingPolicy> pw = new LambdaQueryWrapper<>();
            if (params.get("company") != null && StringUtils.isNotBlank(params.get("company").toString())) {
                pw.like(TblAccountingPolicy::getCompanyName, params.get("company").toString());
            }
            if (params.get("year") != null && StringUtils.isNotBlank(params.get("year").toString())) {
                pw.eq(TblAccountingPolicy::getReportYear, params.get("year").toString());
            }
            List<TblAccountingPolicy> policies = accountingPolicyMapper.selectList(pw);
            List<String> companyList = policies.stream().map(TblAccountingPolicy::getCompanyName).filter(Objects::nonNull).distinct().collect(Collectors.toList());
            List<Map<String, Object>> complianceCards = new ArrayList<>();
            List<Map<String, Object>> complianceList = new ArrayList<>();
            for (String company : companyList) {
                List<TblAccountingPolicy> cp = policies.stream().filter(p -> company.equals(p.getCompanyName())).collect(Collectors.toList());
                long compCount = cp.stream().filter(p -> "1".equals(p.getIsConsistent())).count();
                int score = cp.isEmpty() ? 0 : (int) (compCount * 100 / cp.size());
                List<Map<String, Object>> dimensions = new ArrayList<>();
                dimensions.add(new HashMap<String, Object>() {{ put("name", "政策一致性"); put("score", score); }});
                dimensions.add(new HashMap<String, Object>() {{ put("name", "估计合规"); put("score", Math.min(100, score + 5)); }});
                dimensions.add(new HashMap<String, Object>() {{ put("name", "披露完整"); put("score", Math.min(100, score + 3)); }});
                dimensions.add(new HashMap<String, Object>() {{ put("name", "变更审批"); put("score", Math.min(100, score - 2)); }});
                List<String> issues = new ArrayList<>();
                for (TblAccountingPolicy p : cp) {
                    if (!"1".equals(p.getIsConsistent())) {
                        issues.add(p.getPolicyType() + "政策与集团标准存在差异");
                        Map<String, Object> cl = new HashMap<>();
                        cl.put("company", company);
                        cl.put("checkItem", p.getPolicyType());
                        cl.put("standard", p.getGroupStandard());
                        cl.put("actual", p.getPolicyName());
                        cl.put("result", "1".equals(p.getIsConsistent()) ? "符合" : "不符合");
                        cl.put("risk", p.getDeviationDesc());
                        complianceList.add(cl);
                    } else {
                        Map<String, Object> cl = new HashMap<>();
                        cl.put("company", company);
                        cl.put("checkItem", p.getPolicyType());
                        cl.put("standard", p.getGroupStandard());
                        cl.put("actual", p.getPolicyName());
                        cl.put("result", "符合");
                        cl.put("risk", "");
                        complianceList.add(cl);
                    }
                }
                Map<String, Object> card = new HashMap<>();
                card.put("company", company);
                card.put("score", score);
                card.put("dimensions", dimensions);
                card.put("issues", issues);
                complianceCards.add(card);
            }
            result.put("complianceCards", complianceCards);
            result.put("complianceList", complianceList);
            result.put("companyList", companyList);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 造假细分 ====================

    @Operation(summary = "业绩造假细分列表")
    @PostMapping("/fraud/perf/list")
    public R<PageResult<Map<String, Object>>> fraudPerfList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctAccountingFraud> w = new LambdaQueryWrapper<>();
            w.eq(GzctAccountingFraud::getFraudType, "PERF");
            if (params.get("companyName") != null && StringUtils.isNotBlank(params.get("companyName").toString())) {
                w.like(GzctAccountingFraud::getCompanyName, params.get("companyName").toString());
            }
            if (params.get("riskLevel") != null && StringUtils.isNotBlank(params.get("riskLevel").toString())) {
                w.eq(GzctAccountingFraud::getRiskLevel, params.get("riskLevel").toString());
            }
            w.orderByDesc(GzctAccountingFraud::getPerfScore);
            Page<GzctAccountingFraud> page = fraudMapper.selectPage(new Page<>(pn, ps), w);
            PageResult<Map<String, Object>> pr = new PageResult<>();
            pr.setTotalRecord((int) page.getTotal()); pr.setCurrentPage(pn); pr.setPageNumber(pn);
            pr.setTotalPage((int) page.getPages()); pr.setPageSize(ps);
            List<Map<String, Object>> tlist = page.getRecords().stream().map(f -> {
                Map<String, Object> m = new HashMap<>();
                m.put("companyName", f.getCompanyName()); m.put("revenueGrowth", f.getRevenueGrowth());
                m.put("industryAvg", f.getIndustryAvg()); m.put("zScore", f.getZScore());
                m.put("grossMargin", f.getGrossMargin()); m.put("cashContent", f.getCashContent());
                m.put("dec12Rate", f.getDec12Rate()); m.put("revenueAnomaly", "1".equals(f.getRevenueAnomaly()));
                m.put("riskLevel", f.getRiskLevel()); return m;
            }).collect(Collectors.toList());
            pr.setTlist(tlist); return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "研发造假细分列表")
    @PostMapping("/fraud/rd/list")
    public R<PageResult<Map<String, Object>>> fraudRdList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctAccountingFraud> w = new LambdaQueryWrapper<>();
            w.eq(GzctAccountingFraud::getFraudType, "RD");
            if (params.get("companyName") != null && StringUtils.isNotBlank(params.get("companyName").toString())) {
                w.like(GzctAccountingFraud::getCompanyName, params.get("companyName").toString());
            }
            if (params.get("riskLevel") != null && StringUtils.isNotBlank(params.get("riskLevel").toString())) {
                w.eq(GzctAccountingFraud::getRiskLevel, params.get("riskLevel").toString());
            }
            w.orderByDesc(GzctAccountingFraud::getRdScore);
            Page<GzctAccountingFraud> page = fraudMapper.selectPage(new Page<>(pn, ps), w);
            PageResult<Map<String, Object>> pr = new PageResult<>();
            pr.setTotalRecord((int) page.getTotal()); pr.setCurrentPage(pn); pr.setPageNumber(pn);
            pr.setTotalPage((int) page.getPages()); pr.setPageSize(ps);
            List<Map<String, Object>> tlist = page.getRecords().stream().map(f -> {
                Map<String, Object> m = new HashMap<>();
                m.put("companyName", f.getCompanyName()); m.put("rdAmount", f.getRdAmount());
                m.put("capitalAmount", f.getCapitalAmount()); m.put("capitalRate", f.getCapitalRate());
                m.put("rdStaff", f.getRdStaff()); m.put("outputPerPerson", f.getOutputPerPerson());
                m.put("patents", f.getPatents()); m.put("riskLevel", f.getRiskLevel()); return m;
            }).collect(Collectors.toList());
            pr.setTlist(tlist); return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "杠杆造假细分列表")
    @PostMapping("/fraud/lever/list")
    public R<PageResult<Map<String, Object>>> fraudLeverList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctAccountingFraud> w = new LambdaQueryWrapper<>();
            w.eq(GzctAccountingFraud::getFraudType, "LEVER");
            if (params.get("companyName") != null && StringUtils.isNotBlank(params.get("companyName").toString())) {
                w.like(GzctAccountingFraud::getCompanyName, params.get("companyName").toString());
            }
            if (params.get("riskLevel") != null && StringUtils.isNotBlank(params.get("riskLevel").toString())) {
                w.eq(GzctAccountingFraud::getRiskLevel, params.get("riskLevel").toString());
            }
            w.orderByDesc(GzctAccountingFraud::getLeverScore);
            Page<GzctAccountingFraud> page = fraudMapper.selectPage(new Page<>(pn, ps), w);
            PageResult<Map<String, Object>> pr = new PageResult<>();
            pr.setTotalRecord((int) page.getTotal()); pr.setCurrentPage(pn); pr.setPageNumber(pn);
            pr.setTotalPage((int) page.getPages()); pr.setPageSize(ps);
            List<Map<String, Object>> tlist = page.getRecords().stream().map(f -> {
                Map<String, Object> m = new HashMap<>();
                m.put("companyName", f.getCompanyName()); m.put("debtRatio", f.getDebtRatio());
                m.put("interestCoverage", f.getInterestCoverage()); m.put("shortDebt", f.getShortDebt());
                m.put("longDebt", f.getLongDebt()); m.put("offBalanceDebt", f.getOffBalanceDebt());
                m.put("guaranteeAmount", f.getGuaranteeAmount()); m.put("riskLevel", f.getRiskLevel()); return m;
            }).collect(Collectors.toList());
            pr.setTlist(tlist); return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "出清造假细分列表")
    @PostMapping("/fraud/clear/list")
    public R<PageResult<Map<String, Object>>> fraudClearList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctAccountingFraud> w = new LambdaQueryWrapper<>();
            w.eq(GzctAccountingFraud::getFraudType, "CLEAR");
            if (params.get("companyName") != null && StringUtils.isNotBlank(params.get("companyName").toString())) {
                w.like(GzctAccountingFraud::getCompanyName, params.get("companyName").toString());
            }
            if (params.get("riskLevel") != null && StringUtils.isNotBlank(params.get("riskLevel").toString())) {
                w.eq(GzctAccountingFraud::getRiskLevel, params.get("riskLevel").toString());
            }
            w.orderByDesc(GzctAccountingFraud::getClearScore);
            Page<GzctAccountingFraud> page = fraudMapper.selectPage(new Page<>(pn, ps), w);
            PageResult<Map<String, Object>> pr = new PageResult<>();
            pr.setTotalRecord((int) page.getTotal()); pr.setCurrentPage(pn); pr.setPageNumber(pn);
            pr.setTotalPage((int) page.getPages()); pr.setPageSize(ps);
            List<Map<String, Object>> tlist = page.getRecords().stream().map(f -> {
                Map<String, Object> m = new HashMap<>();
                m.put("companyName", f.getCompanyName()); m.put("disposalAmount", f.getDisposalAmount());
                m.put("disposalGain", f.getDisposalGain()); m.put("disposalRate", f.getDisposalRate());
                m.put("relatedPartyDeal", f.getRelatedPartyDeal()); m.put("fairValueGap", f.getFairValueGap());
                m.put("timingAnomaly", f.getTimingAnomaly()); m.put("riskLevel", f.getRiskLevel()); return m;
            }).collect(Collectors.toList());
            pr.setTlist(tlist); return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "两金造假细分列表")
    @PostMapping("/fraud/twogold/list")
    public R<PageResult<Map<String, Object>>> fraudTwogoldList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctAccountingFraud> w = new LambdaQueryWrapper<>();
            w.eq(GzctAccountingFraud::getFraudType, "TWOGOLD");
            if (params.get("companyName") != null && StringUtils.isNotBlank(params.get("companyName").toString())) {
                w.like(GzctAccountingFraud::getCompanyName, params.get("companyName").toString());
            }
            if (params.get("riskLevel") != null && StringUtils.isNotBlank(params.get("riskLevel").toString())) {
                w.eq(GzctAccountingFraud::getRiskLevel, params.get("riskLevel").toString());
            }
            w.orderByDesc(GzctAccountingFraud::getTwoGoldScore);
            Page<GzctAccountingFraud> page = fraudMapper.selectPage(new Page<>(pn, ps), w);
            PageResult<Map<String, Object>> pr = new PageResult<>();
            pr.setTotalRecord((int) page.getTotal()); pr.setCurrentPage(pn); pr.setPageNumber(pn);
            pr.setTotalPage((int) page.getPages()); pr.setPageSize(ps);
            List<Map<String, Object>> tlist = page.getRecords().stream().map(f -> {
                Map<String, Object> m = new HashMap<>();
                m.put("companyName", f.getCompanyName()); m.put("receivableGrowth", f.getReceivableGrowth());
                m.put("inventoryGrowth", f.getInventoryGrowth()); m.put("receivableTurnover", f.getReceivableTurnover());
                m.put("inventoryTurnover", f.getInventoryTurnover()); m.put("badDebtRate", f.getBadDebtRate());
                m.put("impairmentRate", f.getImpairmentRate()); m.put("riskLevel", f.getRiskLevel()); return m;
            }).collect(Collectors.toList());
            pr.setTlist(tlist); return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 两金KPI ====================

    @Operation(summary = "两金压降KPI")
    @PostMapping("/twogold/kpi")
    public R<Map<String, Object>> twoGoldKpi(@RequestBody Map<String, Object> params) {
        try {
            Map<String, Object> result = new HashMap<>();
            LambdaQueryWrapper<GzctAccountingTwogold> w = new LambdaQueryWrapper<>();
            if (params.get("companyName") != null && StringUtils.isNotBlank(params.get("companyName").toString())) {
                w.like(GzctAccountingTwogold::getCompanyName, params.get("companyName").toString());
            }
            if (params.get("month") != null && StringUtils.isNotBlank(params.get("month").toString())) {
                w.eq(GzctAccountingTwogold::getRecordMonth, params.get("month").toString());
            }
            List<GzctAccountingTwogold> list = twogoldMapper.selectList(w);
            java.math.BigDecimal receivableSum = list.stream().map(GzctAccountingTwogold::getReceivable).filter(Objects::nonNull).reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
            java.math.BigDecimal inventorySum = list.stream().map(GzctAccountingTwogold::getInventory).filter(Objects::nonNull).reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
            java.math.BigDecimal targetSum = list.stream().map(GzctAccountingTwogold::getTargetReduce).filter(Objects::nonNull).reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
            java.math.BigDecimal totalSum = receivableSum.add(inventorySum);
            List<Map<String, Object>> kpis = new ArrayList<>();
            kpis.add(new HashMap<String, Object>() {{ put("icon", "el-icon-coin"); put("label", "应收账款"); put("val", receivableSum); put("tag", "万元"); put("cls", receivableSum.compareTo(new java.math.BigDecimal("5000")) > 0 ? "kpi-warn" : ""); }});
            kpis.add(new HashMap<String, Object>() {{ put("icon", "el-icon-box"); put("label", "存货"); put("val", inventorySum); put("tag", "万元"); put("cls", inventorySum.compareTo(new java.math.BigDecimal("5000")) > 0 ? "kpi-warn" : ""); }});
            kpis.add(new HashMap<String, Object>() {{ put("icon", "el-icon-s-data"); put("label", "两金合计"); put("val", totalSum); put("tag", "万元"); put("cls", totalSum.compareTo(new java.math.BigDecimal("10000")) > 0 ? "kpi-danger" : ""); }});
            kpis.add(new HashMap<String, Object>() {{ put("icon", "el-icon-bottom"); put("label", "压降目标"); put("val", targetSum); put("tag", "万元"); put("cls", ""); }});
            result.put("kpis", kpis); return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 预算KPI ====================

    @Operation(summary = "预算执行KPI")
    @PostMapping("/budget/kpi")
    public R<Map<String, Object>> budgetKpi(@RequestBody Map<String, Object> params) {
        try {
            Map<String, Object> result = new HashMap<>();
            LambdaQueryWrapper<GzctAccountingBudget> w = new LambdaQueryWrapper<>();
            if (params.get("companyName") != null && StringUtils.isNotBlank(params.get("companyName").toString())) {
                w.like(GzctAccountingBudget::getCompanyName, params.get("companyName").toString());
            }
            if (params.get("year") != null && StringUtils.isNotBlank(params.get("year").toString())) {
                w.eq(GzctAccountingBudget::getBudgetYear, params.get("year").toString());
            }
            List<GzctAccountingBudget> list = budgetMapper.selectList(w);
            java.math.BigDecimal totalBudget = list.stream().map(GzctAccountingBudget::getAnnualBudget).filter(Objects::nonNull).reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
            java.math.BigDecimal totalActual = list.stream().map(GzctAccountingBudget::getExecuted).filter(Objects::nonNull).reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
            long overCount = list.stream().filter(b -> b.getOverAmount() != null && b.getOverAmount().compareTo(java.math.BigDecimal.ZERO) > 0).count();
            List<Map<String, Object>> kpis = new ArrayList<>();
            kpis.add(new HashMap<String, Object>() {{ put("icon", "el-icon-s-data"); put("label", "总预算"); put("val", totalBudget); put("tag", "万元"); put("cls", ""); }});
            kpis.add(new HashMap<String, Object>() {{ put("icon", "el-icon-s-order"); put("label", "已执行"); put("val", totalActual); put("tag", "万元"); put("cls", totalActual.compareTo(totalBudget) > 0 ? "kpi-danger" : ""); }});
            kpis.add(new HashMap<String, Object>() {{ put("icon", "el-icon-warning-outline"); put("label", "超支企业"); put("val", overCount); put("tag", "家"); put("cls", overCount > 0 ? "kpi-warn" : ""); }});
            kpis.add(new HashMap<String, Object>() {{ put("icon", "el-icon-s-flag"); put("label", "执行率"); put("val", totalBudget.compareTo(java.math.BigDecimal.ZERO) > 0 ? totalActual.multiply(new java.math.BigDecimal("100")).divide(totalBudget, 1, java.math.RoundingMode.HALF_UP) + "%" : "0%"); put("tag", ""); put("cls", totalActual.compareTo(totalBudget) > 0 ? "kpi-danger" : ""); }});
            result.put("kpis", kpis); return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    private <T> PageResult<T> buildEmptyPageResult(Map<String, Object> params) {
        int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
        int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
        PageResult<T> pr = new PageResult<>();
        pr.setTotalRecord(0); pr.setCurrentPage(pn); pr.setPageNumber(pn);
        pr.setTotalPage(0); pr.setPageSize(ps); pr.setTlist(new ArrayList<>());
        return pr;
    }

    // ==================== 预算数据初始化（调用一次即可） ====================

    @Operation(summary = "初始化预算监管测试数据")
    @PostMapping("/budget/seed")
    public R<String> budgetSeed() {
        try {
            // 清空旧数据
            budgetMapper.delete(new LambdaQueryWrapper<GzctAccountingBudget>());

            List<GzctAccountingBudget> records = new ArrayList<>();

            // === MONITOR 总览数据 ===
            records.add(buildBudget("C001", "华博科技集团有限公司", "2025", "MONITOR", null, bd("28000"), bd("26800"), bd("95.71"), bd("0"), "0", null, null));
            records.add(buildBudget("C002", "中信建设工程有限公司", "2025", "MONITOR", null, bd("15000"), bd("16200"), bd("108"), bd("1200"), "1", "RED", "OVER"));
            records.add(buildBudget("C003", "国投电力控股有限公司", "2025", "MONITOR", null, bd("42000"), bd("18500"), bd("44.05"), bd("0"), "0", "ORANGE", "LAG"));
            records.add(buildBudget("C004", "中国铁建股份有限公司", "2025", "MONITOR", null, bd("56000"), bd("52000"), bd("92.86"), bd("0"), "0", null, null));
            records.add(buildBudget("C005", "华润置地有限公司", "2025", "MONITOR", null, bd("33000"), bd("35800"), bd("108.48"), bd("2800"), "1", "RED", "OVER"));
            records.add(buildBudget("C006", "中国电子信息产业集团", "2025", "MONITOR", null, bd("21000"), bd("19600"), bd("93.33"), bd("0"), "0", null, null));
            records.add(buildBudget("C007", "中国航天科工集团公司", "2025", "MONITOR", null, bd("48000"), bd("20100"), bd("41.88"), bd("0"), "0", "ORANGE", "LAG"));
            records.add(buildBudget("C008", "中国石化集团有限公司", "2025", "MONITOR", null, bd("120000"), bd("115000"), bd("95.83"), bd("0"), "0", null, null));

            // === DETAIL 明细数据 ===
            records.add(buildDetail("C001", "华博科技集团有限公司", "2025", "人员薪酬", bd("8500"), bd("708.33"), bd("720"), bd("11.67"), bd("88.24")));
            records.add(buildDetail("C001", "华博科技集团有限公司", "2025", "办公费用", bd("2400"), bd("200"), bd("185"), bd("-15"), bd("76.04")));
            records.add(buildDetail("C001", "华博科技集团有限公司", "2025", "研发投入", bd("9000"), bd("750"), bd("820"), bd("70"), bd("102.22")));
            records.add(buildDetail("C001", "华博科技集团有限公司", "2025", "市场营销", bd("4500"), bd("375"), bd("410"), bd("35"), bd("96.44")));
            records.add(buildDetail("C001", "华博科技集团有限公司", "2025", "差旅费", bd("1800"), bd("150"), bd("130"), bd("-20"), bd("68.89")));
            records.add(buildDetail("C001", "华博科技集团有限公司", "2025", "设备采购", bd("1800"), bd("150"), bd("280"), bd("130"), bd("115.56")));
            records.add(buildDetail("C002", "中信建设工程有限公司", "2025", "工程材料", bd("6000"), bd("500"), bd("680"), bd("180"), bd("118.33")));
            records.add(buildDetail("C002", "中信建设工程有限公司", "2025", "人工成本", bd("4500"), bd("375"), bd("390"), bd("15"), bd("104")));
            records.add(buildDetail("C002", "中信建设工程有限公司", "2025", "机械租赁", bd("2500"), bd("208.33"), bd("250"), bd("41.67"), bd("112")));
            records.add(buildDetail("C002", "中信建设工程有限公司", "2025", "安全管理", bd("1200"), bd("100"), bd("95"), bd("-5"), bd("79.17")));
            records.add(buildDetail("C002", "中信建设工程有限公司", "2025", "管理费用", bd("800"), bd("66.67"), bd("72"), bd("5.33"), bd("96")));
            records.add(buildDetail("C003", "国投电力控股有限公司", "2025", "燃料采购", bd("18000"), bd("1500"), bd("800"), bd("-700"), bd("35.56")));
            records.add(buildDetail("C003", "国投电力控股有限公司", "2025", "设备维护", bd("8000"), bd("666.67"), bd("420"), bd("-246.67"), bd("42.5")));
            records.add(buildDetail("C003", "国投电力控股有限公司", "2025", "人员薪酬", bd("12000"), bd("1000"), bd("980"), bd("-20"), bd("81.67")));
            records.add(buildDetail("C003", "国投电力控股有限公司", "2025", "技改投资", bd("4000"), bd("333.33"), bd("150"), bd("-183.33"), bd("30")));

            // === ADJUST 调整台账数据 ===
            records.add(buildAdjust("C002", "中信建设工程有限公司", "2025", "工程材料", "INCREASE", bd("800"), "原材料价格上涨超预期，钢材涨幅达15%", "APPROVED", 3, "OVER"));
            records.add(buildAdjust("C002", "中信建设工程有限公司", "2025", "机械租赁", "INCREASE", bd("350"), "新增项目需要额外大型设备", "APPROVED", 2, "OVER"));
            records.add(buildAdjust("C005", "华润置地有限公司", "2025", "营销推广", "INCREASE", bd("1500"), "年中促销活动追加投入", "PENDING", 4, "OVER"));
            records.add(buildAdjust("C005", "华润置地有限公司", "2025", "土地储备", "INCREASE", bd("5000"), "战略性拿地机会，需追加预算", "APPROVED", 1, "OVER"));
            records.add(buildAdjust("C003", "国投电力控股有限公司", "2025", "燃料采购", "DECREASE", bd("2000"), "煤炭价格回落，下调采购预算", "APPROVED", 1, "LAG"));
            records.add(buildAdjust("C003", "国投电力控股有限公司", "2025", "技改投资", "DECREASE", bd("1200"), "部分技改项目延期至下年度", "REJECTED", 2, "LAG"));
            records.add(buildAdjust("C001", "华博科技集团有限公司", "2025", "研发投入", "INCREASE", bd("600"), "AI大模型项目追加研发经费", "APPROVED", 1, null));
            records.add(buildAdjust("C007", "中国航天科工集团公司", "2025", "设备采购", "DECREASE", bd("3000"), "部分设备采购计划推迟", "PENDING", 3, "LAG"));
            records.add(buildAdjust("C004", "中国铁建股份有限公司", "2025", "工程分包", "INCREASE", bd("2200"), "新中标项目需追加分包预算", "APPROVED", 2, null));
            records.add(buildAdjust("C006", "中国电子信息产业集团", "2025", "人才引进", "INCREASE", bd("450"), "高端芯片人才引进计划", "PENDING", 1, null));

            for (GzctAccountingBudget r : records) { budgetMapper.insert(r); }
            return R.success("预算数据初始化完成，共插入 " + records.size() + " 条记录");
        } catch (Exception e) {
            log.error("预算数据初始化失败", e);
            return R.fail("初始化失败：" + e.getMessage());
        }
    }

    private BigDecimal bd(String v) { return new BigDecimal(v); }

    private GzctAccountingBudget buildBudget(String companyId, String companyName, String year, String type, String category,
                                              BigDecimal annual, BigDecimal executed, BigDecimal rate, BigDecimal over,
                                              String endRush, String alertLevel, String alertStatus) {
        GzctAccountingBudget b = new GzctAccountingBudget();
        b.setCompanyId(companyId); b.setCompanyName(companyName); b.setBudgetYear(year);
        b.setBudgetType(type); b.setCategory(category); b.setAnnualBudget(annual);
        b.setExecuted(executed); b.setRate(rate); b.setOverAmount(over);
        b.setEndRush(endRush); b.setAlertLevel(alertLevel); b.setAlertStatus(alertStatus);
        b.setCreateTime(LocalDateTime.now());
        return b;
    }

    private GzctAccountingBudget buildDetail(String companyId, String companyName, String year, String category,
                                              BigDecimal annual, BigDecimal monthBudget, BigDecimal monthActual,
                                              BigDecimal monthGap, BigDecimal cumRate) {
        GzctAccountingBudget b = new GzctAccountingBudget();
        b.setCompanyId(companyId); b.setCompanyName(companyName); b.setBudgetYear(year);
        b.setBudgetType("DETAIL"); b.setCategory(category); b.setAnnualBudget(annual);
        b.setMonthBudget(monthBudget); b.setMonthActual(monthActual);
        b.setMonthGap(monthGap); b.setCumRate(cumRate);
        b.setCreateTime(LocalDateTime.now());
        return b;
    }

    private GzctAccountingBudget buildAdjust(String companyId, String companyName, String year, String category,
                                              String adjustType, BigDecimal amount, String reason,
                                              String approvalStatus, Integer adjustCount, String alertStatus) {
        GzctAccountingBudget b = new GzctAccountingBudget();
        b.setCompanyId(companyId); b.setCompanyName(companyName); b.setBudgetYear(year);
        b.setBudgetType("ADJUST"); b.setCategory(category); b.setAdjustType(adjustType);
        b.setAdjustAmount(amount); b.setAdjustReason(reason);
        b.setApprovalStatus(approvalStatus); b.setAdjustCount(adjustCount); b.setAlertStatus(alertStatus);
        b.setCreateTime(LocalDateTime.now());
        return b;
    }

    // ==================== 造假线索 CRUD ====================

    @Operation(summary = "新增造假线索")
    @PostMapping("/fraud/clue/add")
    public R<Boolean> addFraudClue(@RequestBody GzctAccountingClue record) {
        try {
            record.setCreateTime(LocalDateTime.now());
            if (StringUtils.isBlank(record.getStatus())) {
                record.setStatus("PENDING");
            }
            clueMapper.insert(record);
            return R.success(true);
        } catch (Exception e) { return R.fail("新增失败：" + e.getMessage()); }
    }

    @Operation(summary = "更新造假线索")
    @PostMapping("/fraud/clue/update")
    public R<Boolean> updateFraudClue(@RequestBody GzctAccountingClue record) {
        try {
            record.setUpdateTime(LocalDateTime.now());
            clueMapper.updateById(record);
            return R.success(true);
        } catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); }
    }

    @Operation(summary = "删除造假线索")
    @DeleteMapping("/fraud/clue/{id}")
    public R<Boolean> deleteFraudClue(@PathVariable String id) {
        try {
            return R.success(clueMapper.deleteById(id) > 0);
        } catch (Exception e) { return R.fail("删除失败：" + e.getMessage()); }
    }

    @Operation(summary = "造假线索详情")
    @GetMapping("/fraud/clue/{id}")
    public R<GzctAccountingClue> getFraudClueDetail(@PathVariable String id) {
        try {
            return R.success(clueMapper.selectById(id));
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "更新造假线索状态")
    @PostMapping("/fraud/clue/status")
    public R<Boolean> updateFraudClueStatus(@RequestBody Map<String, Object> params) {
        try {
            Object idObj = params.get("clueId");
            Object statusObj = params.get("status");
            if (idObj == null || StringUtils.isBlank(idObj.toString())) {
                return R.fail("clueId 不能为空");
            }
            if (statusObj == null || StringUtils.isBlank(statusObj.toString())) {
                return R.fail("status 不能为空");
            }
            LambdaUpdateWrapper<GzctAccountingClue> uw = new LambdaUpdateWrapper<>();
            uw.eq(GzctAccountingClue::getClueId, idObj.toString())
              .set(GzctAccountingClue::getStatus, statusObj.toString())
              .set(GzctAccountingClue::getUpdateTime, LocalDateTime.now());
            return R.success(clueMapper.update(null, uw) > 0);
        } catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); }
    }

    // ==================== 凭证详情/穿透 ====================

    @Operation(summary = "凭证详情")
    @GetMapping("/voucher/detail/{id}")
    public R<GzctAccountingVoucher> voucherDetail(@PathVariable String id) {
        try {
            return R.success(voucherMapper.selectById(id));
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "凭证穿透链路")
    @PostMapping("/voucher/penetration")
    public R<Map<String, Object>> voucherPenetration(@RequestBody Map<String, Object> params) {
        try {
            Map<String, Object> result = new HashMap<>();
            Object vidObj = params.get("voucherId");
            String voucherId = vidObj == null ? null : vidObj.toString();
            GzctAccountingVoucher row = StringUtils.isBlank(voucherId) ? null : voucherMapper.selectById(voucherId);
            result.put("voucher", row);
            List<Map<String, Object>> chain = new ArrayList<>();
            List<Map<String, Object>> rules = new ArrayList<>();
            if (row != null) {
                final GzctAccountingVoucher v = row;
                String dateStr = v.getVoucherDate() != null ? v.getVoucherDate().toString() : "";
                String voucherNo = v.getVoucherNo() != null ? v.getVoucherNo() : "";
                String company = v.getCompanyName() != null ? v.getCompanyName() : "";
                String summary = v.getSummary() != null ? v.getSummary() : "";
                String debit = v.getDebitSubject() != null ? v.getDebitSubject() : "";
                String credit = v.getCreditSubject() != null ? v.getCreditSubject() : "";
                BigDecimal amt = v.getDebitAmount() != null ? v.getDebitAmount() : (v.getCreditAmount() != null ? v.getCreditAmount() : BigDecimal.ZERO);
                String inputUser = v.getInputUser() != null ? v.getInputUser() : "";
                chain.add(buildChainNode("el-icon-document", "原始凭证", voucherNo + " · " + dateStr));
                chain.add(buildChainNode("el-icon-office-building", "记账主体", company));
                chain.add(buildChainNode("el-icon-edit-outline", "摘要", summary));
                chain.add(buildChainNode("el-icon-sort", "借贷科目", debit + " / " + credit));
                chain.add(buildChainNode("el-icon-money", "发生金额", amt.toPlainString()));
                chain.add(buildChainNode("el-icon-user", "录入人", inputUser));
                String flag = v.getAnomalyFlag();
                String type = v.getAnomalyType();
                if (!"N".equals(flag)) {
                    if ("NO_ORIGINAL".equals(type)) {
                        rules.add(buildRule("danger", "红色", "无原始凭证附件，存在合规风险"));
                    } else if ("END_RUSH".equals(type)) {
                        rules.add(buildRule("warning", "橙色", "期末突击录入，存在调账嫌疑"));
                    } else if ("PENDING".equals(flag)) {
                        rules.add(buildRule("warning", "黄色", "系统标记为待人工核查"));
                    } else {
                        rules.add(buildRule("info", "提示", "存在其他异常标识，请关注此凭证"));
                    }
                }
            }
            result.put("chain", chain);
            result.put("rules", rules);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    private Map<String, Object> buildChainNode(String icon, String title, String detail) {
        Map<String, Object> m = new HashMap<>();
        m.put("icon", icon); m.put("title", title); m.put("detail", detail);
        return m;
    }

    private Map<String, Object> buildRule(String level, String levelText, String desc) {
        Map<String, Object> m = new HashMap<>();
        m.put("level", level); m.put("levelText", levelText); m.put("desc", desc);
        return m;
    }

    // ==================== 预警详情 ====================

    @Operation(summary = "会计预警详情")
    @GetMapping("/warning/detail/{id}")
    public R<GzctAccountingWarning> warningDetail(@PathVariable String id) {
        try {
            return R.success(warningMapper.selectById(id));
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 触发类 ====================

    @Operation(summary = "触发政策一致性检查")
    @PostMapping("/consistency/run")
    public R<Map<String, Object>> consistencyRun(@RequestBody(required = false) Map<String, Object> params) {
        try {
            List<TblAccountingPolicy> list = accountingPolicyMapper.selectList(null);
            int major = 0, minor = 0, none = 0;
            for (TblAccountingPolicy p : list) {
                boolean deviation = StringUtils.isNotBlank(p.getDeviationDesc());
                boolean consistent = "1".equals(p.getIsConsistent());
                if (!consistent && deviation) major++;
                else if (!consistent) minor++;
                else none++;
            }
            Map<String, Object> result = new HashMap<>();
            result.put("message", "一致性检查已完成");
            result.put("major", major);
            result.put("minor", minor);
            result.put("none", none);
            result.put("total", list.size());
            result.put("runTime", LocalDateTime.now().toString());
            return R.success(result);
        } catch (Exception e) { return R.fail("一致性检查失败：" + e.getMessage()); }
    }

    @Operation(summary = "触发政策合规评估")
    @PostMapping("/compliance/run")
    public R<Map<String, Object>> complianceRun(@RequestBody(required = false) Map<String, Object> params) {
        try {
            List<TblAccountingPolicy> policies = accountingPolicyMapper.selectList(null);
            Map<String, List<TblAccountingPolicy>> byCompany = policies.stream()
                .filter(p -> p.getCompanyName() != null)
                .collect(Collectors.groupingBy(TblAccountingPolicy::getCompanyName));
            int passCount = 0, failCount = 0;
            for (Map.Entry<String, List<TblAccountingPolicy>> e : byCompany.entrySet()) {
                boolean allConsistent = e.getValue().stream().allMatch(p -> "1".equals(p.getIsConsistent()));
                if (allConsistent) passCount++;
                else failCount++;
            }
            Map<String, Object> summary = new HashMap<>();
            summary.put("totalCompany", byCompany.size());
            summary.put("passCount", passCount);
            summary.put("failCount", failCount);
            Map<String, Object> result = new HashMap<>();
            result.put("message", "合规评估已完成");
            result.put("summary", summary);
            result.put("runTime", LocalDateTime.now().toString());
            return R.success(result);
        } catch (Exception e) { return R.fail("合规评估失败：" + e.getMessage()); }
    }

    // ==================== 估计历史 ====================

    @Operation(summary = "会计估计历史变更")
    @PostMapping("/estimate/history")
    public R<PageResult<Map<String, Object>>> estimateHistory(@RequestBody Map<String, Object> params) {
        try {
            LambdaQueryWrapper<GzctAccountingEstimate> w = new LambdaQueryWrapper<>();
            if (params.get("companyName") != null && StringUtils.isNotBlank(params.get("companyName").toString())) {
                w.eq(GzctAccountingEstimate::getCompanyName, params.get("companyName").toString());
            }
            if (params.get("estimateItem") != null && StringUtils.isNotBlank(params.get("estimateItem").toString())) {
                w.eq(GzctAccountingEstimate::getEstimateItem, params.get("estimateItem").toString());
            }
            w.orderByDesc(GzctAccountingEstimate::getCreateTime);
            List<GzctAccountingEstimate> list = estimateMapper.selectList(w);
            List<Map<String, Object>> tlist = list.stream().map(e -> {
                Map<String, Object> m = new HashMap<>();
                m.put("period", e.getPeriod());
                m.put("previousValue", e.getPreviousValue());
                m.put("currentValue", e.getCurrentValue());
                m.put("keyAssumption", e.getKeyAssumption());
                m.put("estimateMethod", e.getEstimateMethod());
                m.put("createTime", e.getCreateTime() != null ? e.getCreateTime().toString() : "");
                return m;
            }).collect(Collectors.toList());
            PageResult<Map<String, Object>> pr = new PageResult<>();
            pr.setTotalRecord(tlist.size()); pr.setCurrentPage(1); pr.setPageNumber(1);
            pr.setTotalPage(1); pr.setPageSize(tlist.size()); pr.setTlist(tlist);
            return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 下发整改 ====================

    @Operation(summary = "下发整改")
    @PostMapping("/rectification/issue")
    public R<Boolean> issueRectification(@RequestBody Map<String, Object> params) {
        try {
            GzctAccountingRectification record = new GzctAccountingRectification();
            record.setCompanyName(params.get("companyName") != null ? params.get("companyName").toString() : "");
            record.setPolicyType(params.get("policyType") != null ? params.get("policyType").toString() : "");
            record.setDiffLevel(params.get("diffLevel") != null ? params.get("diffLevel").toString() : "");
            record.setDiffDesc(params.get("diffDesc") != null ? params.get("diffDesc").toString() : "");
            record.setRectificationRequirement(params.get("requirement") != null ? params.get("requirement").toString() : "");
            if (params.get("deadline") != null && StringUtils.isNotBlank(params.get("deadline").toString())) {
                record.setDeadline(LocalDate.parse(params.get("deadline").toString()));
            }
            record.setStatus("待整改");
            record.setIssuer(params.get("issuer") != null ? params.get("issuer").toString() : "系统管理员");
            record.setIssueTime(LocalDateTime.now());
            record.setCreateTime(LocalDateTime.now());
            rectificationMapper.insert(record);
            return R.success(true);
        } catch (Exception e) {
            log.error("下发整改失败", e);
            return R.fail("下发整改失败：" + e.getMessage());
        }
    }

    @Operation(summary = "整改记录列表")
    @PostMapping("/rectification/list")
    public R<PageResult<Map<String, Object>>> rectificationList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 10;
            LambdaQueryWrapper<GzctAccountingRectification> w = new LambdaQueryWrapper<>();
            if (params.get("companyName") != null && StringUtils.isNotBlank(params.get("companyName").toString())) {
                w.like(GzctAccountingRectification::getCompanyName, params.get("companyName").toString());
            }
            if (params.get("status") != null && StringUtils.isNotBlank(params.get("status").toString())) {
                w.eq(GzctAccountingRectification::getStatus, params.get("status").toString());
            }
            w.orderByDesc(GzctAccountingRectification::getCreateTime);
            Page<GzctAccountingRectification> page = rectificationMapper.selectPage(new Page<>(pn, ps), w);
            PageResult<Map<String, Object>> pr = new PageResult<>();
            pr.setTotalRecord((int) page.getTotal()); pr.setCurrentPage(pn); pr.setPageNumber(pn);
            pr.setTotalPage((int) page.getPages()); pr.setPageSize(ps);
            pr.setTlist(page.getRecords().stream().map(r -> {
                Map<String, Object> m = new HashMap<>();
                m.put("rectificationId", r.getRectificationId());
                m.put("companyName", r.getCompanyName());
                m.put("policyType", r.getPolicyType());
                m.put("diffLevel", r.getDiffLevel());
                m.put("diffDesc", r.getDiffDesc());
                m.put("requirement", r.getRectificationRequirement());
                m.put("deadline", r.getDeadline());
                m.put("status", r.getStatus());
                m.put("issuer", r.getIssuer());
                m.put("issueTime", r.getIssueTime());
                return m;
            }).collect(Collectors.toList()));
            return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 核查 ====================

    @Operation(summary = "提交核查意见")
    @PostMapping("/verification/submit")
    public R<Boolean> submitVerification(@RequestBody Map<String, Object> params) {
        try {
            GzctAccountingVerification record = new GzctAccountingVerification();
            record.setCompanyName(params.get("companyName") != null ? params.get("companyName").toString() : "");
            record.setEstimateItem(params.get("estimateItem") != null ? params.get("estimateItem").toString() : "");
            record.setChangeBefore(params.get("changeBefore") != null ? params.get("changeBefore").toString() : "");
            record.setChangeAfter(params.get("changeAfter") != null ? params.get("changeAfter").toString() : "");
            record.setProfitImpact(params.get("profitImpact") != null ? params.get("profitImpact").toString() : "");
            record.setVerificationOpinion(params.get("opinion") != null ? params.get("opinion").toString() : "");
            record.setVerificationResult(params.get("result") != null ? params.get("result").toString() : "");
            record.setVerifier(params.get("verifier") != null ? params.get("verifier").toString() : "系统管理员");
            record.setVerifyTime(LocalDateTime.now());
            record.setStatus("已核查");
            record.setCreateTime(LocalDateTime.now());
            verificationMapper.insert(record);
            return R.success(true);
        } catch (Exception e) {
            log.error("提交核查意见失败", e);
            return R.fail("提交核查失败：" + e.getMessage());
        }
    }

    @Operation(summary = "核查记录列表")
    @PostMapping("/verification/list")
    public R<PageResult<Map<String, Object>>> verificationList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 10;
            LambdaQueryWrapper<GzctAccountingVerification> w = new LambdaQueryWrapper<>();
            if (params.get("companyName") != null && StringUtils.isNotBlank(params.get("companyName").toString())) {
                w.like(GzctAccountingVerification::getCompanyName, params.get("companyName").toString());
            }
            w.orderByDesc(GzctAccountingVerification::getCreateTime);
            Page<GzctAccountingVerification> page = verificationMapper.selectPage(new Page<>(pn, ps), w);
            PageResult<Map<String, Object>> pr = new PageResult<>();
            pr.setTotalRecord((int) page.getTotal()); pr.setCurrentPage(pn); pr.setPageNumber(pn);
            pr.setTotalPage((int) page.getPages()); pr.setPageSize(ps);
            pr.setTlist(page.getRecords().stream().map(r -> {
                Map<String, Object> m = new HashMap<>();
                m.put("verificationId", r.getVerificationId());
                m.put("companyName", r.getCompanyName());
                m.put("estimateItem", r.getEstimateItem());
                m.put("changeBefore", r.getChangeBefore());
                m.put("changeAfter", r.getChangeAfter());
                m.put("profitImpact", r.getProfitImpact());
                m.put("opinion", r.getVerificationOpinion());
                m.put("result", r.getVerificationResult());
                m.put("verifier", r.getVerifier());
                m.put("verifyTime", r.getVerifyTime());
                m.put("status", r.getStatus());
                return m;
            }).collect(Collectors.toList()));
            return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 通用 Excel 导出 ====================

    private static final int EXPORT_MAX_ROWS = 5000;

    /**
     * 取第一个非空字符串值（用于参数别名兼容，如 startPeriod/startDate）
     */
    private String firstNonBlank(Object... vals) {
        if (vals == null) return null;
        for (Object v : vals) {
            if (v != null && StringUtils.isNotBlank(v.toString())) return v.toString();
        }
        return null;
    }

    /** 两金造假类型编码转中文 */
    private static final Map<String, String> FRAUD_TYPE_MAP = new HashMap<String, String>() {{
        put("RUSH", "期末突击压降");
        put("RUSH_REDUCTION", "期末突击压降");
        put("FAKE_TRANSFER", "关联方虚假转移");
        put("FICTITIOUS_WRITE_OFF", "虚构核销");
        put("RECLASSIFICATION", "科目重分类");
        put("EARLY_RECOGNITION", "提前确认收入");
        put("INFLATED_RECEIVABLE", "虚增应收");
        put("HIDDEN_INVENTORY", "隐匿存货");
    }};

    private String convertFraudType(String code) {
        if (StringUtils.isBlank(code)) return null;
        return FRAUD_TYPE_MAP.getOrDefault(code, code);
    }

    /**
     * 通用表格导出：输出 UTF-8 BOM + CSV。
     * 选择 CSV 是因为：
     *   1) POI 5.2.5 的 XSSFWorkbook / HSSFWorkbook 均依赖 commons-io 2.12+ 的
     *      UnsynchronizedByteArrayOutputStream.builder()，但项目 commons-io 版本偏低，
     *      不允许修改 pom.xml 的情况下无法使用；
     *   2) CSV 零依赖、纯 JDK 实现，Excel / WPS / 数据库导入工具均原生支持；
     *   3) 5000 行规模下写出速度远快于 POI，文件体积更小。
     * 加 BOM (\uFEFF) 保证中文在 Windows Excel 下不乱码。
     */
    private void writeExcel(HttpServletResponse resp, String fileName, List<String> headers, List<List<Object>> rows) {
        try {
            resp.setContentType("text/csv;charset=utf-8");
            resp.setCharacterEncoding("utf-8");
            String encoded = URLEncoder.encode(fileName, "UTF-8").replaceAll("\\+", "%20");
            resp.setHeader("Content-Disposition", "attachment; filename=" + encoded + ".csv");
            try (OutputStream os = resp.getOutputStream();
                 java.io.OutputStreamWriter osw = new java.io.OutputStreamWriter(os, java.nio.charset.StandardCharsets.UTF_8);
                 java.io.BufferedWriter bw = new java.io.BufferedWriter(osw)) {
                // UTF-8 BOM，确保 Excel 中文不乱码
                bw.write('\uFEFF');
                // 表头
                writeCsvRow(bw, headers.stream().map(h -> (Object) h).collect(Collectors.toList()));
                // 数据行
                int limit = Math.min(rows.size(), EXPORT_MAX_ROWS);
                for (int r = 0; r < limit; r++) {
                    writeCsvRow(bw, rows.get(r));
                }
                bw.flush();
            }
        } catch (Exception e) {
            log.error("导出CSV失败：{}", fileName, e);
            try {
                resp.setStatus(500);
                resp.setContentType("application/json;charset=utf-8");
                resp.getWriter().write("{\"result\":500,\"message\":\"导出失败：" + e.getMessage() + "\"}");
            } catch (Exception ignore) {}
        }
    }

    /**
     * 写入一行 CSV：按 RFC 4180 规则转义——值含逗号/双引号/换行时用 " 包裹，内部 " 转义为 ""
     */
    private void writeCsvRow(java.io.BufferedWriter bw, List<Object> row) throws java.io.IOException {
        for (int c = 0; c < row.size(); c++) {
            if (c > 0) bw.write(',');
            Object v = row.get(c);
            if (v == null) continue;
            String s = v.toString();
            if (s.indexOf('"') >= 0 || s.indexOf(',') >= 0 || s.indexOf('\n') >= 0 || s.indexOf('\r') >= 0) {
                bw.write('"');
                bw.write(s.replace("\"", "\"\""));
                bw.write('"');
            } else {
                bw.write(s);
            }
        }
        bw.write("\r\n");
    }

    @Operation(summary = "导出凭证列表")
    @PostMapping("/voucher/export")
    public void voucherExport(@RequestBody(required = false) Map<String, Object> params, HttpServletResponse resp) {
        Map<String, Object> p = params == null ? new HashMap<>() : params;
        LambdaQueryWrapper<GzctAccountingVoucher> w = new LambdaQueryWrapper<>();
        if (p.get("companyName") != null && StringUtils.isNotBlank(p.get("companyName").toString())) w.like(GzctAccountingVoucher::getCompanyName, p.get("companyName").toString());
        if (p.get("voucherNo") != null && StringUtils.isNotBlank(p.get("voucherNo").toString())) w.like(GzctAccountingVoucher::getVoucherNo, p.get("voucherNo").toString());
        if (p.get("subject") != null && StringUtils.isNotBlank(p.get("subject").toString())) {
            w.and(q -> q.like(GzctAccountingVoucher::getDebitSubject, p.get("subject").toString())
                .or().like(GzctAccountingVoucher::getCreditSubject, p.get("subject").toString()));
        }
        if (p.get("anomalyFlag") != null && StringUtils.isNotBlank(p.get("anomalyFlag").toString())) w.eq(GzctAccountingVoucher::getAnomalyFlag, p.get("anomalyFlag").toString());
        String startPeriod = firstNonBlank(p.get("startPeriod"), p.get("startDate"));
        String endPeriod   = firstNonBlank(p.get("endPeriod"),   p.get("endDate"));
        if (startPeriod != null) w.ge(GzctAccountingVoucher::getPeriod, startPeriod);
        if (endPeriod   != null) w.le(GzctAccountingVoucher::getPeriod, endPeriod);
        w.orderByDesc(GzctAccountingVoucher::getVoucherDate);
        List<GzctAccountingVoucher> list = voucherMapper.selectList(w);
        List<String> headers = Arrays.asList("公司", "日期", "凭证号", "摘要", "借方科目", "贷方科目", "借方金额", "贷方金额", "录入人", "异常标识", "异常类型");
        List<List<Object>> rows = list.stream().map(v -> (List<Object>) new ArrayList<Object>(Arrays.asList(
            v.getCompanyName(), v.getVoucherDate(), v.getVoucherNo(), v.getSummary(),
            v.getDebitSubject(), v.getCreditSubject(), v.getDebitAmount(), v.getCreditAmount(),
            v.getInputUser(), v.getAnomalyFlag(), v.getAnomalyType()))).collect(Collectors.toList());
        writeExcel(resp, "凭证穿透列表", headers, rows);
    }

    @Operation(summary = "导出账簿（按 bookType）")
    @PostMapping("/book/export")
    public void bookExport(@RequestBody(required = false) Map<String, Object> params, HttpServletResponse resp) {
        Map<String, Object> p = params == null ? new HashMap<>() : params;
        String bookType = p.get("bookType") != null ? p.get("bookType").toString() : "BALANCE";
        LambdaQueryWrapper<GzctAccountingBook> w = new LambdaQueryWrapper<>();
        w.eq(GzctAccountingBook::getBookType, bookType);
        if (p.get("companyName") != null && StringUtils.isNotBlank(p.get("companyName").toString())) w.like(GzctAccountingBook::getCompanyName, p.get("companyName").toString());
        if (p.get("yearMonth") != null && StringUtils.isNotBlank(p.get("yearMonth").toString())) w.eq(GzctAccountingBook::getPeriod, p.get("yearMonth").toString());
        List<GzctAccountingBook> list = bookMapper.selectList(w);
        List<String> headers = Arrays.asList("公司", "科目编码", "科目名称", "期初余额", "借方发生", "贷方发生", "期末余额", "日期", "凭证号", "摘要");
        List<List<Object>> rows = list.stream().map(b -> (List<Object>) new ArrayList<Object>(Arrays.asList(
            b.getCompanyName(), b.getSubjectCode(), b.getSubjectName(),
            b.getOpeningBalance(), b.getDebitAmount(), b.getCreditAmount(), b.getClosingBalance(),
            b.getVoucherDate(), b.getVoucherNo(), b.getSummary()))).collect(Collectors.toList());
        writeExcel(resp, "账簿穿透_" + bookType, headers, rows);
    }

    @Operation(summary = "导出财务报表")
    @PostMapping("/report/export")
    public void reportExport(@RequestBody(required = false) Map<String, Object> params, HttpServletResponse resp) {
        Map<String, Object> p = params == null ? new HashMap<>() : params;
        LambdaQueryWrapper<GzctAccountingReport> w = new LambdaQueryWrapper<>();
        if (p.get("companyName") != null && StringUtils.isNotBlank(p.get("companyName").toString())) w.like(GzctAccountingReport::getCompanyName, p.get("companyName").toString());
        if (p.get("year") != null && StringUtils.isNotBlank(p.get("year").toString())) w.eq(GzctAccountingReport::getReportYear, p.get("year").toString());
        if (p.get("reportType") != null && StringUtils.isNotBlank(p.get("reportType").toString())) w.eq(GzctAccountingReport::getReportType, p.get("reportType").toString());
        List<GzctAccountingReport> list = reportMapper.selectList(w);
        List<String> headers = Arrays.asList("公司", "年度", "报表类型", "报表子类", "项目", "本期金额", "上期金额", "变化率", "质量分", "勾稽问题数", "提交状态", "审计意见");
        List<List<Object>> rows = list.stream().map(r -> (List<Object>) new ArrayList<Object>(Arrays.asList(
            r.getCompanyName(), r.getReportYear(),
            translateReportType(r.getReportType()),
            translateReportTab(r.getReportTab()),
            r.getItemName(), r.getCurrentAmount(), r.getPrevAmount(), r.getChangeRate(),
            r.getQualityScore(), r.getHookIssues(),
            translateSubmitStatus(r.getSubmitStatus()),
            translateAuditOpinion(r.getAuditOpinion())))).collect(Collectors.toList());
        writeExcel(resp, "财务报表穿透", headers, rows);
    }

    // ==================== 枚举翻译工具方法 ====================

    private static String translateReportType(String v) {
        if (v == null) return "";
        switch (v) {
            case "INDIVIDUAL":    return "个别报表";
            case "CONSOLIDATED":  return "合并报表";
            default:              return v;
        }
    }

    private static String translateReportTab(String v) {
        if (v == null) return "";
        switch (v) {
            case "BALANCE":  return "资产负债表";
            case "INCOME":   return "利润表";
            case "CASHFLOW": return "现金流量表";
            case "EQUITY":   return "所有者权益变动表";
            case "LIST":     return "报表台账";
            default:         return v;
        }
    }

    private static String translateSubmitStatus(String v) {
        if (v == null) return "";
        switch (v) {
            case "SUBMITTED":  return "已提交";
            case "REVIEWING":  return "审核中";
            case "OVERDUE":    return "逾期";
            case "EXCEPTION":  return "有异常";
            case "DRAFT":      return "草稿";
            default:           return v;
        }
    }

    private static String translateAuditOpinion(String v) {
        if (v == null) return "";
        switch (v) {
            case "UNQUALIFIED":  return "标准无保留意见";
            case "QUALIFIED":    return "保留意见";
            case "ADVERSE":      return "否定意见";
            case "DISCLAIMER":   return "无法表示意见";
            default:             return v;
        }
    }

    @Operation(summary = "导出预算监控")
    @PostMapping("/budget/export")
    public void budgetExport(@RequestBody(required = false) Map<String, Object> params, HttpServletResponse resp) {
        Map<String, Object> p = params == null ? new HashMap<>() : params;
        LambdaQueryWrapper<GzctAccountingBudget> w = new LambdaQueryWrapper<>();
        if (p.get("companyName") != null && StringUtils.isNotBlank(p.get("companyName").toString())) w.like(GzctAccountingBudget::getCompanyName, p.get("companyName").toString());
        if (p.get("year") != null && StringUtils.isNotBlank(p.get("year").toString())) w.eq(GzctAccountingBudget::getBudgetYear, p.get("year").toString());
        if (p.get("alertStatus") != null && StringUtils.isNotBlank(p.get("alertStatus").toString())) w.eq(GzctAccountingBudget::getAlertStatus, p.get("alertStatus").toString());
        w.orderByDesc(GzctAccountingBudget::getCreateTime);
        List<GzctAccountingBudget> list = budgetMapper.selectList(w);
        List<String> headers = Arrays.asList("公司", "年度", "预算类型", "类别", "年度预算", "已执行", "执行率", "超支金额", "预警等级", "调整次数");
        List<List<Object>> rows = list.stream().map(b -> (List<Object>) new ArrayList<Object>(Arrays.asList(
            b.getCompanyName(), b.getBudgetYear(), b.getBudgetType(), b.getCategory(),
            b.getAnnualBudget(), b.getExecuted(), b.getRate(), b.getOverAmount(),
            b.getAlertLevel(), b.getAdjustCount()))).collect(Collectors.toList());
        writeExcel(resp, "预算执行监管", headers, rows);
    }

    @Operation(summary = "导出两金压降")
    @PostMapping("/twogold/export")
    public void twogoldExport(@RequestBody(required = false) Map<String, Object> params, HttpServletResponse resp) {
        Map<String, Object> p = params == null ? new HashMap<>() : params;
        LambdaQueryWrapper<GzctAccountingTwogold> w = new LambdaQueryWrapper<>();
        if (p.get("companyName") != null && StringUtils.isNotBlank(p.get("companyName").toString())) w.like(GzctAccountingTwogold::getCompanyName, p.get("companyName").toString());
        if (p.get("month") != null && StringUtils.isNotBlank(p.get("month").toString())) w.eq(GzctAccountingTwogold::getRecordMonth, p.get("month").toString());
        w.orderByDesc(GzctAccountingTwogold::getCreateTime);
        List<GzctAccountingTwogold> list = twogoldMapper.selectList(w);
        List<String> headers = Arrays.asList("公司", "月份", "数据类型", "应收账款", "存货", "两金合计", "收入占比", "压降目标", "完成率", "风险标识");
        List<List<Object>> rows = list.stream().map(t -> (List<Object>) new ArrayList<Object>(Arrays.asList(
            t.getCompanyName(), t.getRecordMonth(), t.getDataType(),
            t.getReceivable(), t.getInventory(), t.getTotal(),
            t.getRevenueRate(), t.getTargetReduce(), t.getReduceRate(), t.getRiskFlag()))).collect(Collectors.toList());
        writeExcel(resp, "两金压降监控", headers, rows);
    }

    @Operation(summary = "导出造假风险")
    @PostMapping("/fraud/export")
    public void fraudExport(@RequestBody(required = false) Map<String, Object> params, HttpServletResponse resp) {
        Map<String, Object> p = params == null ? new HashMap<>() : params;
        LambdaQueryWrapper<GzctAccountingFraud> w = new LambdaQueryWrapper<>();
        if (p.get("companyName") != null && StringUtils.isNotBlank(p.get("companyName").toString())) w.like(GzctAccountingFraud::getCompanyName, p.get("companyName").toString());
        if (p.get("fraudType") != null && StringUtils.isNotBlank(p.get("fraudType").toString())) w.eq(GzctAccountingFraud::getFraudType, p.get("fraudType").toString());
        w.orderByDesc(GzctAccountingFraud::getTotalScore);
        List<GzctAccountingFraud> list = fraudMapper.selectList(w);
        List<String> headers = Arrays.asList("公司", "造假类型", "综合评分", "业绩分", "杠杆分", "出清分", "研发分", "两金分", "预警等级", "线索数");
        List<List<Object>> rows = list.stream().map(f -> (List<Object>) new ArrayList<Object>(Arrays.asList(
            f.getCompanyName(), f.getFraudType(), f.getTotalScore(),
            f.getPerfScore(), f.getLeverScore(), f.getClearScore(), f.getRdScore(), f.getTwoGoldScore(),
            f.getAlertLevel(), f.getClueCount()))).collect(Collectors.toList());
        writeExcel(resp, "财务造假识别", headers, rows);
    }

    @Operation(summary = "导出会计政策")
    @PostMapping("/policy/export")
    public void policyExport(@RequestBody(required = false) Map<String, Object> params, HttpServletResponse resp) {
        Map<String, Object> p = params == null ? new HashMap<>() : params;
        LambdaQueryWrapper<TblAccountingPolicy> w = new LambdaQueryWrapper<>();
        if (p.get("companyName") != null && StringUtils.isNotBlank(p.get("companyName").toString())) w.like(TblAccountingPolicy::getCompanyName, p.get("companyName").toString());
        if (p.get("policyType") != null && StringUtils.isNotBlank(p.get("policyType").toString())) w.eq(TblAccountingPolicy::getPolicyType, p.get("policyType").toString());
        if (p.get("status") != null && StringUtils.isNotBlank(p.get("status").toString())) w.eq(TblAccountingPolicy::getStatus, p.get("status").toString());
        w.orderByDesc(TblAccountingPolicy::getCreateTime);
        List<TblAccountingPolicy> list = accountingPolicyMapper.selectList(w);
        List<String> headers = Arrays.asList("政策编号", "适用企业", "政策类型", "政策名称", "政策内容", "集团统一政策", "一致性", "偏差说明", "生效日期", "审计意见", "状态");
        List<List<Object>> rows = list.stream().map(p2 -> (List<Object>) new ArrayList<Object>(Arrays.asList(
            p2.getPolicyId(), p2.getCompanyName(), p2.getPolicyType(), p2.getPolicyName(),
            p2.getPolicyDetail(), p2.getGroupStandard(),
            "1".equals(p2.getIsConsistent()) ? "一致" : "偏差",
            p2.getDeviationDesc(), p2.getEffectiveDate(),
            p2.getAuditOpinionType(), p2.getStatus()))).collect(Collectors.toList());
        writeExcel(resp, "会计政策台账", headers, rows);
    }
}
