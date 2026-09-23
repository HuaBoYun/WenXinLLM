package com.huabo.cybermonitor.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.cybermonitor.entity.*;
import com.huabo.cybermonitor.mapper.*;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.util.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @deprecated 该Controller已被 ProcurementPenetrationController 替代，
 *             路由完全重叠，禁用以避免 Ambiguous mapping 冲突。
 */
@Deprecated
@Tag(name = "采购穿透式监管(旧)", description = "采购供应链穿透式监管全接口")
// @RestController  // 禁用：路由与 ProcurementPenetrationController 冲突
@RequestMapping("/v1/supervision/procurement")
@Slf4j
public class ProcurementSupervisionController {

    @Autowired
    private GzctProcPurchaseRecordMapper purchaseRecordMapper;
    @Autowired
    private GzctProcRelatedTransactionMapper relatedTransactionMapper;
    @Autowired
    private GzctProcBiddingMapper biddingMapper;
    @Autowired
    private GzctProcAlertMapper alertMapper;
    @Autowired
    private GzctProcContractMapper contractMapper;
    @Autowired
    private GzctProcProjectMapper projectMapper;
    @Autowired
    private GzctProcSupplierMapper supplierMapper;
    @Autowired
    private com.huabo.cybermonitor.util.OrgQueryHelper orgQueryHelper;

    // ==================== 首页 ====================

    @Operation(summary = "采购首页KPI")
    @GetMapping("/home/kpi")
    public R<Map<String, Object>> homeKpi() {
        try {
            Map<String, Object> kpi = new HashMap<>();
            List<GzctProcPurchaseRecord> records = purchaseRecordMapper.selectList(new LambdaQueryWrapper<>());
            kpi.put("totalPurchaseAmount", records.stream().map(r -> r.getAmount() != null ? r.getAmount() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add));
            kpi.put("projectCount", projectMapper.selectCount(new LambdaQueryWrapper<>()));
            kpi.put("supplierCount", supplierMapper.selectCount(new LambdaQueryWrapper<>()));
            long totalBidding = biddingMapper.selectCount(new LambdaQueryWrapper<>());
            long compliantBidding = biddingMapper.selectCount(new LambdaQueryWrapper<GzctProcBidding>().eq(GzctProcBidding::getIsCompliant, "Y"));
            kpi.put("biddingComplianceRate", totalBidding > 0 ? Math.round(compliantBidding * 1000.0 / totalBidding) / 10.0 : 0);
            kpi.put("activeAlerts", alertMapper.selectCount(new LambdaQueryWrapper<GzctProcAlert>().eq(GzctProcAlert::getStatus, "PENDING")));
            return R.success(kpi);
        } catch (Exception e) {
            log.error("获取采购首页KPI失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "采购首页树")
    @GetMapping("/home/tree")
    public R<List<Map<String, Object>>> homeTree(@RequestParam(required = false) String companyId) {
        try {
            LambdaQueryWrapper<GzctProcPurchaseRecord> wrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(companyId)) wrapper.eq(GzctProcPurchaseRecord::getCompanyId, companyId);
            List<GzctProcPurchaseRecord> records = purchaseRecordMapper.selectList(wrapper);
            List<Map<String, Object>> tree = records.stream()
                .collect(Collectors.groupingBy(GzctProcPurchaseRecord::getCompanyName))
                .entrySet().stream().map(e -> {
                    Map<String, Object> node = new HashMap<>();
                    node.put("name", e.getKey());
                    node.put("purchaseTotal", e.getValue().stream().map(r -> r.getAmount() != null ? r.getAmount() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add));
                    node.put("count", e.getValue().size());
                    return node;
                }).collect(Collectors.toList());
            return R.success(tree);
        } catch (Exception e) {
            log.error("获取采购首页树失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "采购首页预警")
    @GetMapping("/home/warnings")
    public R<List<GzctProcAlert>> homeWarnings() {
        try {
            LambdaQueryWrapper<GzctProcAlert> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(GzctProcAlert::getStatus, "PENDING");
            wrapper.orderByDesc(GzctProcAlert::getCreateTime);
            wrapper.last("LIMIT 5");
            return R.success(alertMapper.selectList(wrapper));
        } catch (Exception e) {
            log.error("获取采购首页预警失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    // ==================== 驾驶舱 ====================

    @Operation(summary = "采购驾驶舱概览")
    @GetMapping("/dashboard/overview")
    public R<Map<String, Object>> dashboardOverview() {
        try {
            Map<String, Object> result = new HashMap<>();
            Map<String, Object> kpi = new HashMap<>();
            List<GzctProcPurchaseRecord> records = purchaseRecordMapper.selectList(new LambdaQueryWrapper<>());
            kpi.put("totalPurchaseAmount", records.stream().map(r -> r.getAmount() != null ? r.getAmount() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add));
            kpi.put("supplierCount", supplierMapper.selectCount(new LambdaQueryWrapper<>()));
            long relatedCount = relatedTransactionMapper.selectCount(new LambdaQueryWrapper<>());
            BigDecimal relatedAmount = relatedTransactionMapper.selectList(new LambdaQueryWrapper<>()).stream().map(r -> r.getAmount() != null ? r.getAmount() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal totalAmount = (BigDecimal) kpi.get("totalPurchaseAmount");
            kpi.put("relatedRatio", totalAmount.compareTo(BigDecimal.ZERO) > 0 ? relatedAmount.multiply(BigDecimal.valueOf(100)).divide(totalAmount, 1, RoundingMode.HALF_UP) : BigDecimal.ZERO);
            long totalBidding = biddingMapper.selectCount(new LambdaQueryWrapper<>());
            long compliantBidding = biddingMapper.selectCount(new LambdaQueryWrapper<GzctProcBidding>().eq(GzctProcBidding::getIsCompliant, "Y"));
            kpi.put("biddingComplianceRate", totalBidding > 0 ? Math.round(compliantBidding * 1000.0 / totalBidding) / 10.0 : 0);
            kpi.put("activeAlerts", alertMapper.selectCount(new LambdaQueryWrapper<GzctProcAlert>().ne(GzctProcAlert::getStatus, "DISMISSED")));
            long contractTotal = contractMapper.selectCount(new LambdaQueryWrapper<>());
            long contractCompleted = contractMapper.selectCount(new LambdaQueryWrapper<GzctProcContract>().eq(GzctProcContract::getAcceptanceStatus, "ACCEPTED"));
            kpi.put("contractExecutionRate", contractTotal > 0 ? Math.round(contractCompleted * 1000.0 / contractTotal) / 10.0 : 0);
            result.put("kpi", kpi);
            List<GzctProcAlert> alerts = alertMapper.selectList(new LambdaQueryWrapper<GzctProcAlert>().ne(GzctProcAlert::getStatus, "DISMISSED").orderByDesc(GzctProcAlert::getCreateTime).last("LIMIT 6"));
            result.put("warnings", alerts);
            return R.success(result);
        } catch (Exception e) {
            log.error("获取采购驾驶舱概览失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "采购驾驶舱趋势")
    @GetMapping("/dashboard/trend")
    public R<List<Map<String, Object>>> dashboardTrend(@RequestParam(required = false) String companyId) {
        try {
            List<Map<String, Object>> trend = new ArrayList<>();
            String[] months = {"2025-01", "2025-02", "2025-03", "2025-04", "2025-05", "2025-06", "2025-07", "2025-08", "2025-09", "2025-10", "2025-11", "2025-12"};
            for (String m : months) {
                Map<String, Object> item = new HashMap<>();
                item.put("month", m);
                LambdaQueryWrapper<GzctProcPurchaseRecord> w = new LambdaQueryWrapper<>();
                w.apply("TO_CHAR(PURCHASE_DATE,'YYYY-MM') = {0}", m);
                if (StringUtils.isNotEmpty(companyId)) w.eq(GzctProcPurchaseRecord::getCompanyId, companyId);
                List<GzctProcPurchaseRecord> monthRecords = purchaseRecordMapper.selectList(w);
                item.put("amount", monthRecords.stream().map(r -> r.getAmount() != null ? r.getAmount() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add));
                item.put("projectCount", monthRecords.size());
                trend.add(item);
            }
            return R.success(trend);
        } catch (Exception e) {
            log.error("获取采购趋势失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    // ==================== 采购项目 ====================
    // 注意：采购项目CRUD接口（project/list, project/{id}, project/add, project/update,
    // project/{id} DELETE, project/batch/delete, project/statistics）
    // 已由 ProcurementController 提供，此处不再重复定义，避免URL映射冲突

    // ==================== 采购台账 ====================

    @Operation(summary = "采购台账列表")
    @PostMapping("/purchase-record/list")
    public R<PageResult<GzctProcPurchaseRecord>> purchaseRecordList(@RequestBody Map<String, Object> params) {
        try {
            int pageNum = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctProcPurchaseRecord> wrapper = new LambdaQueryWrapper<>();
            String companyId = (String) params.get("companyId");
            if (StringUtils.isNotEmpty(companyId)) wrapper.eq(GzctProcPurchaseRecord::getCompanyId, companyId);
            String purchaseType = (String) params.get("purchaseType");
            if (StringUtils.isNotEmpty(purchaseType)) wrapper.eq(GzctProcPurchaseRecord::getPurchaseType, purchaseType);
            String status = (String) params.get("status");
            if (StringUtils.isNotEmpty(status)) wrapper.eq(GzctProcPurchaseRecord::getStatus, status);
            wrapper.orderByDesc(GzctProcPurchaseRecord::getCreateTime);
            Page<GzctProcPurchaseRecord> page = new Page<>(pageNum, pageSize);
            Page<GzctProcPurchaseRecord> result = new GzctProcPurchaseRecord().selectPage(page, wrapper);
            PageResult<GzctProcPurchaseRecord> pr = new PageResult<>();
            pr.setTotalRecord((int) result.getTotal());
            pr.setCurrentPage((int) result.getCurrent());
            pr.setPageNumber((int) result.getCurrent());
            pr.setTotalPage((int) result.getPages());
            pr.setPageSize((int) result.getSize());
            pr.setTlist(result.getRecords());
            return R.success(pr);
        } catch (Exception e) { log.error("查询采购台账列表失败", e); return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "采购台账详情")
    @GetMapping("/purchase-record/{id}")
    public R<GzctProcPurchaseRecord> purchaseRecordDetail(@PathVariable String id) {
        try { return R.success(purchaseRecordMapper.selectById(id)); }
        catch (Exception e) { log.error("查询采购台账详情失败", e); return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "新增采购台账")
    @PostMapping("/purchase-record/add")
    public R<Boolean> addPurchaseRecord(@RequestBody GzctProcPurchaseRecord record) {
        try { record.setCreateTime(LocalDateTime.now()); purchaseRecordMapper.insert(record); return R.success(true); }
        catch (Exception e) { log.error("新增采购台账失败", e); return R.fail("新增失败：" + e.getMessage()); }
    }

    @Operation(summary = "更新采购台账")
    @PostMapping("/purchase-record/update")
    public R<Boolean> updatePurchaseRecord(@RequestBody GzctProcPurchaseRecord record) {
        try { record.setUpdateTime(LocalDateTime.now()); purchaseRecordMapper.updateById(record); return R.success(true); }
        catch (Exception e) { log.error("更新采购台账失败", e); return R.fail("更新失败：" + e.getMessage()); }
    }

    @Operation(summary = "删除采购台账")
    @DeleteMapping("/purchase-record/{id}")
    public R<Boolean> deletePurchaseRecord(@PathVariable String id) {
        try { return R.success(purchaseRecordMapper.deleteById(id) > 0); }
        catch (Exception e) { log.error("删除采购台账失败", e); return R.fail("删除失败：" + e.getMessage()); }
    }

    // ==================== 关联交易 ====================

    @Operation(summary = "关联交易列表")
    @PostMapping("/related-transaction/list")
    public R<PageResult<GzctProcRelatedTransaction>> relatedTransactionList(@RequestBody Map<String, Object> params) {
        try {
            int pageNum = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctProcRelatedTransaction> wrapper = new LambdaQueryWrapper<>();
            String companyId = (String) params.get("companyId");
            if (StringUtils.isNotEmpty(companyId)) wrapper.eq(GzctProcRelatedTransaction::getCompanyId, companyId);
            wrapper.orderByDesc(GzctProcRelatedTransaction::getCreateTime);
            Page<GzctProcRelatedTransaction> page = new Page<>(pageNum, pageSize);
            Page<GzctProcRelatedTransaction> result = new GzctProcRelatedTransaction().selectPage(page, wrapper);
            PageResult<GzctProcRelatedTransaction> pr = new PageResult<>();
            pr.setTotalRecord((int) result.getTotal());
            pr.setCurrentPage((int) result.getCurrent());
            pr.setPageNumber((int) result.getCurrent());
            pr.setTotalPage((int) result.getPages());
            pr.setPageSize((int) result.getSize());
            pr.setTlist(result.getRecords());
            return R.success(pr);
        } catch (Exception e) { log.error("查询关联交易列表失败", e); return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "关联交易详情")
    @GetMapping("/related-transaction/{id}")
    public R<GzctProcRelatedTransaction> relatedTransactionDetail(@PathVariable String id) {
        try { return R.success(relatedTransactionMapper.selectById(id)); }
        catch (Exception e) { log.error("查询关联交易详情失败", e); return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "关联交易统计")
    @GetMapping("/related-transaction/statistics")
    public R<Map<String, Object>> relatedTransactionStatistics(@RequestParam(required = false) String companyId) {
        try {
            Map<String, Object> stats = new HashMap<>();
            LambdaQueryWrapper<GzctProcRelatedTransaction> wrapper = new LambdaQueryWrapper<>();
            String orgPattern = orgQueryHelper.getOrgPathPattern(companyId);
            if (orgPattern != null) wrapper.eq(GzctProcRelatedTransaction::getCompanyId, companyId);
            List<GzctProcRelatedTransaction> list = relatedTransactionMapper.selectList(wrapper);
            stats.put("totalAmount", list.stream().map(r -> r.getAmount() != null ? r.getAmount() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add));
            stats.put("totalCount", list.size());
            stats.put("highRiskCount", list.stream().filter(r -> "HIGH".equals(r.getRiskLevel())).count());
            stats.put("undisclosedCount", list.stream().filter(r -> "N".equals(r.getIsDisclosed())).count());
            return R.success(stats);
        } catch (Exception e) { log.error("获取关联交易统计失败", e); return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "提交关联交易披露")
    @PostMapping("/related-transaction/disclosure")
    public R<Boolean> submitDisclosure(@RequestBody Map<String, Object> params) {
        try {
            String transactionId = (String) params.get("transactionId");
            GzctProcRelatedTransaction rt = relatedTransactionMapper.selectById(transactionId);
            if (rt != null) { rt.setIsDisclosed("Y"); rt.setUpdateTime(LocalDateTime.now()); relatedTransactionMapper.updateById(rt); }
            return R.success(true);
        } catch (Exception e) { log.error("提交关联交易披露失败", e); return R.fail("提交失败：" + e.getMessage()); }
    }

    // ==================== 招投标合规 ====================

    @Operation(summary = "招投标合规列表")
    @PostMapping("/bidding/list")
    public R<PageResult<GzctProcBidding>> biddingList(@RequestBody Map<String, Object> params) {
        try {
            int pageNum = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctProcBidding> wrapper = new LambdaQueryWrapper<>();
            String companyId = (String) params.get("companyId");
            if (StringUtils.isNotEmpty(companyId)) wrapper.eq(GzctProcBidding::getCompanyId, companyId);
            String isCompliant = (String) params.get("isCompliant");
            if (StringUtils.isNotEmpty(isCompliant)) wrapper.eq(GzctProcBidding::getIsCompliant, isCompliant);
            wrapper.orderByDesc(GzctProcBidding::getCreateTime);
            Page<GzctProcBidding> page = new Page<>(pageNum, pageSize);
            Page<GzctProcBidding> result = new GzctProcBidding().selectPage(page, wrapper);
            PageResult<GzctProcBidding> pr = new PageResult<>();
            pr.setTotalRecord((int) result.getTotal());
            pr.setCurrentPage((int) result.getCurrent());
            pr.setPageNumber((int) result.getCurrent());
            pr.setTotalPage((int) result.getPages());
            pr.setPageSize((int) result.getSize());
            pr.setTlist(result.getRecords());
            return R.success(pr);
        } catch (Exception e) { log.error("查询招投标合规列表失败", e); return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "招投标合规详情")
    @GetMapping("/bidding/{id}")
    public R<GzctProcBidding> biddingDetail(@PathVariable String id) {
        try { return R.success(biddingMapper.selectById(id)); }
        catch (Exception e) { log.error("查询招投标合规详情失败", e); return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "招投标合规统计")
    @GetMapping("/bidding/statistics")
    public R<Map<String, Object>> biddingStatistics(@RequestParam(required = false) String companyId) {
        try {
            Map<String, Object> stats = new HashMap<>();
            LambdaQueryWrapper<GzctProcBidding> wrapper = new LambdaQueryWrapper<>();
            String orgPattern = orgQueryHelper.getOrgPathPattern(companyId);
            if (orgPattern != null) wrapper.eq(GzctProcBidding::getCompanyId, companyId);
            List<GzctProcBidding> list = biddingMapper.selectList(wrapper);
            long totalCount = list.size();
            long compliantCount = list.stream().filter(b -> "Y".equals(b.getIsCompliant())).count();
            stats.put("totalCount", totalCount);
            stats.put("compliantCount", compliantCount);
            stats.put("complianceRate", totalCount > 0 ? Math.round(compliantCount * 1000.0 / totalCount) / 10.0 : 0);
            stats.put("violationCount", totalCount - compliantCount);
            return R.success(stats);
        } catch (Exception e) { log.error("获取招投标统计失败", e); return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "报告招投标违规")
    @PostMapping("/bidding/report-violation")
    public R<Boolean> reportViolation(@RequestBody Map<String, Object> params) {
        try {
            GzctProcAlert alert = new GzctProcAlert();
            alert.setCompanyId((String) params.get("companyId"));
            alert.setCompanyName((String) params.get("companyName"));
            alert.setAlertType("BIDDING_VIOLATION");
            alert.setAlertContent((String) params.get("violationDesc"));
            alert.setLevel("HIGH");
            alert.setStatus("PENDING");
            alert.setRelatedId((String) params.get("biddingId"));
            alert.setCreateTime(LocalDateTime.now());
            alertMapper.insert(alert);
            return R.success(true);
        } catch (Exception e) { log.error("报告招投标违规失败", e); return R.fail("报告失败：" + e.getMessage()); }
    }

    // ==================== 合同履约 ====================

    @Operation(summary = "合同履约列表")
    @PostMapping("/contract/list")
    public R<PageResult<GzctProcContract>> contractList(@RequestBody Map<String, Object> params) {
        try {
            int pageNum = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctProcContract> wrapper = new LambdaQueryWrapper<>();
            String companyId = (String) params.get("companyId");
            if (StringUtils.isNotEmpty(companyId)) wrapper.eq(GzctProcContract::getCompanyId, companyId);
            wrapper.orderByDesc(GzctProcContract::getCreateTime);
            Page<GzctProcContract> page = new Page<>(pageNum, pageSize);
            Page<GzctProcContract> result = contractMapper.selectPage(page, wrapper);
            PageResult<GzctProcContract> pr = new PageResult<>();
            pr.setTotalRecord((int) result.getTotal());
            pr.setCurrentPage((int) result.getCurrent());
            pr.setPageNumber((int) result.getCurrent());
            pr.setTotalPage((int) result.getPages());
            pr.setPageSize((int) result.getSize());
            pr.setTlist(result.getRecords());
            return R.success(pr);
        } catch (Exception e) { log.error("查询合同履约列表失败", e); return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "合同履约详情")
    @GetMapping("/contract/{id}")
    public R<GzctProcContract> contractDetail(@PathVariable String id) {
        try { return R.success(contractMapper.selectById(id)); }
        catch (Exception e) { log.error("查询合同履约详情失败", e); return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "更新合同付款")
    @PostMapping("/contract/payment")
    public R<Boolean> updateContractPayment(@RequestBody Map<String, Object> params) {
        try {
            String contractId = (String) params.get("contractId");
            GzctProcContract contract = contractMapper.selectById(contractId);
            if (contract != null) {
                if (params.get("paidAmount") != null) contract.setPaidAmount(new BigDecimal(params.get("paidAmount").toString()));
                if (contract.getContractAmount() != null && contract.getContractAmount().compareTo(BigDecimal.ZERO) > 0 && contract.getPaidAmount() != null) {
                    contract.setPaymentRate(contract.getPaidAmount().multiply(BigDecimal.valueOf(100)).divide(contract.getContractAmount(), 1, RoundingMode.HALF_UP));
                }
                contract.setUpdateTime(LocalDateTime.now());
                contractMapper.updateById(contract);
            }
            return R.success(true);
        } catch (Exception e) { log.error("更新合同付款失败", e); return R.fail("更新失败：" + e.getMessage()); }
    }

    @Operation(summary = "更新合同验收")
    @PostMapping("/contract/acceptance")
    public R<Boolean> updateContractAcceptance(@RequestBody Map<String, Object> params) {
        try {
            String contractId = (String) params.get("contractId");
            GzctProcContract contract = contractMapper.selectById(contractId);
            if (contract != null) {
                contract.setAcceptanceStatus((String) params.getOrDefault("acceptanceStatus", "ACCEPTED"));
                contract.setUpdateTime(LocalDateTime.now());
                contractMapper.updateById(contract);
            }
            return R.success(true);
        } catch (Exception e) { log.error("更新合同验收失败", e); return R.fail("更新失败：" + e.getMessage()); }
    }

    @Operation(summary = "逾期合同列表")
    @GetMapping("/contract/overdue")
    public R<List<GzctProcContract>> overdueContracts(@RequestParam(required = false) String companyId) {
        try {
            LambdaQueryWrapper<GzctProcContract> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(GzctProcContract::getIsOverdue, "Y");
            if (StringUtils.isNotEmpty(companyId)) wrapper.eq(GzctProcContract::getCompanyId, companyId);
            return R.success(contractMapper.selectList(wrapper));
        } catch (Exception e) { log.error("获取逾期合同列表失败", e); return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 供应商 ====================

    @Operation(summary = "供应商列表")
    @PostMapping("/supplier/list")
    public R<PageResult<GzctProcSupplier>> supplierList(@RequestBody Map<String, Object> params) {
        try {
            int pageNum = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctProcSupplier> wrapper = new LambdaQueryWrapper<>();
            String supplierName = (String) params.get("supplierName");
            if (StringUtils.isNotEmpty(supplierName)) wrapper.like(GzctProcSupplier::getSupplierName, supplierName);
            String blacklistStatus = (String) params.get("blacklistStatus");
            if (StringUtils.isNotEmpty(blacklistStatus)) wrapper.eq(GzctProcSupplier::getBlacklistStatus, blacklistStatus);
            String isRelated = (String) params.get("isRelated");
            if (StringUtils.isNotEmpty(isRelated)) wrapper.eq(GzctProcSupplier::getIsRelated, isRelated);
            wrapper.orderByDesc(GzctProcSupplier::getCreateTime);
            Page<GzctProcSupplier> page = new Page<>(pageNum, pageSize);
            Page<GzctProcSupplier> result = new GzctProcSupplier().selectPage(page, wrapper);
            PageResult<GzctProcSupplier> pr = new PageResult<>();
            pr.setTotalRecord((int) result.getTotal());
            pr.setCurrentPage((int) result.getCurrent());
            pr.setPageNumber((int) result.getCurrent());
            pr.setTotalPage((int) result.getPages());
            pr.setPageSize((int) result.getSize());
            pr.setTlist(result.getRecords());
            return R.success(pr);
        } catch (Exception e) { log.error("查询供应商列表失败", e); return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "供应商详情")
    @GetMapping("/supplier/{id}")
    public R<GzctProcSupplier> supplierDetail(@PathVariable String id) {
        try { return R.success(supplierMapper.selectById(id)); }
        catch (Exception e) { log.error("查询供应商详情失败", e); return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "新增供应商")
    @PostMapping("/supplier/add")
    public R<Boolean> addSupplier(@RequestBody GzctProcSupplier supplier) {
        try { supplier.setCreateTime(LocalDateTime.now()); supplierMapper.insert(supplier); return R.success(true); }
        catch (Exception e) { log.error("新增供应商失败", e); return R.fail("新增失败：" + e.getMessage()); }
    }

    @Operation(summary = "更新供应商")
    @PostMapping("/supplier/update")
    public R<Boolean> updateSupplier(@RequestBody GzctProcSupplier supplier) {
        try { supplier.setUpdateTime(LocalDateTime.now()); supplierMapper.updateById(supplier); return R.success(true); }
        catch (Exception e) { log.error("更新供应商失败", e); return R.fail("更新失败：" + e.getMessage()); }
    }

    @Operation(summary = "供应商黑名单")
    @PostMapping("/supplier/blacklist")
    public R<Boolean> blacklistSupplier(@RequestBody Map<String, Object> params) {
        try {
            String supplierId = (String) params.get("id");
            GzctProcSupplier supplier = supplierMapper.selectById(supplierId);
            if (supplier != null) { supplier.setBlacklistStatus("BLACKLIST"); supplier.setUpdateTime(LocalDateTime.now()); supplierMapper.updateById(supplier); }
            return R.success(true);
        } catch (Exception e) { log.error("供应商黑名单操作失败", e); return R.fail("操作失败：" + e.getMessage()); }
    }

    @Operation(summary = "供应商集中度")
    @GetMapping("/supplier/concentration")
    public R<Map<String, Object>> supplierConcentration(@RequestParam(required = false) String companyId) {
        try {
            Map<String, Object> result = new HashMap<>();
            List<GzctProcSupplier> all = supplierMapper.selectList(new LambdaQueryWrapper<>());
            List<Map<String, Object>> topSuppliers = all.stream()
                .sorted((a, b) -> { BigDecimal ba = b.getTotalPurchase() != null ? b.getTotalPurchase() : BigDecimal.ZERO; BigDecimal aa = a.getTotalPurchase() != null ? a.getTotalPurchase() : BigDecimal.ZERO; return ba.compareTo(aa); })
                .limit(10).map(s -> { Map<String, Object> m = new HashMap<>(); m.put("name", s.getSupplierName()); m.put("totalPurchase", s.getTotalPurchase()); m.put("purchaseRatio", s.getPurchaseRatio()); return m; })
                .collect(Collectors.toList());
            result.put("topSuppliers", topSuppliers);
            BigDecimal totalAll = all.stream().map(s -> s.getTotalPurchase() != null ? s.getTotalPurchase() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal top5Total = topSuppliers.stream().limit(5).map(m -> m.get("totalPurchase") != null ? (BigDecimal) m.get("totalPurchase") : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add);
            result.put("concentrationRate", totalAll.compareTo(BigDecimal.ZERO) > 0 ? top5Total.multiply(BigDecimal.valueOf(100)).divide(totalAll, 1, RoundingMode.HALF_UP) : BigDecimal.ZERO);
            return R.success(result);
        } catch (Exception e) { log.error("获取供应商集中度失败", e); return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 采购风险穿透 ====================

    @Operation(summary = "采购穿透树")
    @PostMapping("/drill-down/tree")
    public R<Map<String, Object>> drillDownTree(@RequestBody Map<String, Object> params) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("name", "集团总部");
            result.put("key", "root");
            LambdaQueryWrapper<GzctProcPurchaseRecord> wrapper = new LambdaQueryWrapper<>();
            String companyId = (String) params.get("companyId");
            if (StringUtils.isNotEmpty(companyId)) wrapper.eq(GzctProcPurchaseRecord::getCompanyId, companyId);
            List<GzctProcPurchaseRecord> records = purchaseRecordMapper.selectList(wrapper);
            List<Map<String, Object>> children = records.stream()
                .collect(Collectors.groupingBy(GzctProcPurchaseRecord::getCompanyName))
                .entrySet().stream().map(e -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("name", e.getKey());
                    m.put("purchaseTotal", e.getValue().stream().map(r -> r.getAmount() != null ? r.getAmount() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add));
                    m.put("count", e.getValue().size());
                    m.put("riskLevel", e.getValue().stream().anyMatch(r -> "VIOLATION".equals(r.getStatus())) ? "HIGH" : "LOW");
                    return m;
                }).collect(Collectors.toList());
            result.put("children", children);
            return R.success(result);
        } catch (Exception e) { log.error("获取采购穿透树失败", e); return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "采购穿透详情")
    @GetMapping("/drill-down/detail/{nodeId}")
    public R<Map<String, Object>> drillDownDetail(@PathVariable String nodeId) {
        try {
            Map<String, Object> detail = new HashMap<>();
            LambdaQueryWrapper<GzctProcPurchaseRecord> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(GzctProcPurchaseRecord::getCompanyName, nodeId);
            List<GzctProcPurchaseRecord> records = purchaseRecordMapper.selectList(wrapper);
            detail.put("companyName", nodeId);
            detail.put("purchaseTotal", records.stream().map(r -> r.getAmount() != null ? r.getAmount() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add));
            detail.put("purchaseCount", records.size());
            detail.put("suppliers", records.stream().map(GzctProcPurchaseRecord::getSupplierName).distinct().collect(Collectors.toList()));
            return R.success(detail);
        } catch (Exception e) { log.error("获取采购穿透详情失败", e); return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "采购预警列表")
    @PostMapping("/alert/list")
    public R<PageResult<GzctProcAlert>> alertList(@RequestBody Map<String, Object> params) {
        try {
            int pageNum = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctProcAlert> wrapper = new LambdaQueryWrapper<>();
            String status = (String) params.get("status");
            if (StringUtils.isNotEmpty(status)) wrapper.eq(GzctProcAlert::getStatus, status);
            String level = (String) params.get("level");
            if (StringUtils.isNotEmpty(level)) wrapper.eq(GzctProcAlert::getLevel, level);
            wrapper.orderByDesc(GzctProcAlert::getCreateTime);
            Page<GzctProcAlert> page = new Page<>(pageNum, pageSize);
            Page<GzctProcAlert> result = new GzctProcAlert().selectPage(page, wrapper);
            PageResult<GzctProcAlert> pr = new PageResult<>();
            pr.setTotalRecord((int) result.getTotal());
            pr.setCurrentPage((int) result.getCurrent());
            pr.setPageNumber((int) result.getCurrent());
            pr.setTotalPage((int) result.getPages());
            pr.setPageSize((int) result.getSize());
            pr.setTlist(result.getRecords());
            return R.success(pr);
        } catch (Exception e) { log.error("查询采购预警列表失败", e); return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "解除采购预警")
    @PostMapping("/alert/dismiss/{alertId}")
    public R<Boolean> dismissAlert(@PathVariable String alertId) {
        try {
            GzctProcAlert alert = alertMapper.selectById(alertId);
            if (alert != null) { alert.setStatus("DISMISSED"); alert.setUpdateTime(LocalDateTime.now()); alertMapper.updateById(alert); }
            return R.success(true);
        } catch (Exception e) { log.error("解除采购预警失败", e); return R.fail("操作失败：" + e.getMessage()); }
    }
}
