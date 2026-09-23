package com.huabo.cybermonitor.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.cybermonitor.entity.TblContractRecord;
import com.huabo.cybermonitor.entity.TblContractDispute;
import com.huabo.cybermonitor.entity.TblContractApprovalTrack;
import com.huabo.cybermonitor.entity.GzctContractRectification;
import com.huabo.cybermonitor.entity.GzctContractSmartReview;
import com.huabo.cybermonitor.mapper.TblContractRecordMapper;
import com.huabo.cybermonitor.mapper.TblContractDisputeMapper;
import com.huabo.cybermonitor.mapper.TblContractApprovalTrackMapper;
import com.huabo.cybermonitor.mapper.GzctContractRectificationMapper;
import com.huabo.cybermonitor.mapper.GzctContractSmartReviewMapper;
import com.huabo.cybermonitor.entity.GzctContractWarning;
import com.huabo.cybermonitor.mapper.GzctContractWarningMapper;
import com.huabo.cybermonitor.service.IGzctContractRectificationService;
import com.huabo.cybermonitor.service.IGzctContractWarningService;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.util.R;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Tag(name = "合同穿透式监管-综合接口", description = "合同穿透式监管全接口(台账/生命周期/合规/履行/纠纷/信用/预警/穿透/驾驶舱)")
@RestController
@RequestMapping("/v1/supervision/contract")
@Slf4j
public class ContractPenetrationController {

    @Autowired private TblContractRecordMapper contractRecordMapper;
    @Autowired private TblContractDisputeMapper contractDisputeMapper;
    @Autowired private GzctContractSmartReviewMapper smartReviewMapper;
    @Autowired private TblContractApprovalTrackMapper approvalTrackMapper;
    @Autowired private GzctContractRectificationMapper rectificationMapper;
    @Autowired private IGzctContractRectificationService rectificationService;
    @Autowired private IGzctContractWarningService contractWarningService;
    @Autowired private GzctContractWarningMapper contractWarningMapper;
    @Autowired private com.huabo.cybermonitor.util.OrgQueryHelper orgQueryHelper;

    private <T> PageResult<T> buildPageResult(Page<T> r) {
        PageResult<T> pr = new PageResult<>();
        pr.setTotalRecord((int) r.getTotal()); pr.setCurrentPage((int) r.getCurrent());
        pr.setPageNumber((int) r.getCurrent()); pr.setTotalPage((int) r.getPages());
        pr.setPageSize((int) r.getSize()); pr.setTlist(r.getRecords());
        return pr;
    }

    // ==================== 合同台账 ====================

    @Operation(summary = "合同台账列表")
    @PostMapping("/list")
    public R<PageResult<Map<String, Object>>> contractList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            // 合规状态筛选参数（计算字段，需查询后过滤）
            String complianceStatusFilter = null;
            if (params.get("complianceStatus") != null && StringUtils.isNotBlank(params.get("complianceStatus").toString())) {
                complianceStatusFilter = params.get("complianceStatus").toString();
            }
            LambdaQueryWrapper<TblContractRecord> w = new LambdaQueryWrapper<>();
            if (params.get("contractName") != null && StringUtils.isNotBlank(params.get("contractName").toString())) {
                w.like(TblContractRecord::getContractName, params.get("contractName").toString());
            }
            if (params.get("companyName") != null && StringUtils.isNotBlank(params.get("companyName").toString())) {
                w.like(TblContractRecord::getCompanyName, params.get("companyName").toString());
            }
            if (params.get("counterpartyName") != null && StringUtils.isNotBlank(params.get("counterpartyName").toString())) {
                w.like(TblContractRecord::getCounterpartyName, params.get("counterpartyName").toString());
            }
            if (params.get("contractType") != null && StringUtils.isNotBlank(params.get("contractType").toString())) {
                // DB 存英文枚举（PURCHASE/SALES/ENGINEERING/SERVICE/OTHER），前端也传英文枚举，直接精确匹配
                w.eq(TblContractRecord::getContractType, params.get("contractType").toString());
            }
            if (params.get("dataSource") != null && StringUtils.isNotBlank(params.get("dataSource").toString())) {
                String dsVal = params.get("dataSource").toString();
                if ("INTERNAL".equals(dsVal)) {
                    // DB 中 DATA_SOURCE 为 NULL 时视为内部数据
                    w.and(wr -> wr.isNull(TblContractRecord::getDataSource)
                            .or().eq(TblContractRecord::getDataSource, "INTERNAL"));
                } else {
                    w.eq(TblContractRecord::getDataSource, dsVal);
                }
            }
            if (params.get("isMajor") != null && StringUtils.isNotBlank(params.get("isMajor").toString())) {
                String isMajorVal = params.get("isMajor").toString();
                // 前端传 true/false，转换为数据库的 1/0
                if ("true".equalsIgnoreCase(isMajorVal)) {
                    w.eq(TblContractRecord::getIsMajor, "1");
                } else if ("false".equalsIgnoreCase(isMajorVal)) {
                    w.eq(TblContractRecord::getIsMajor, "0");
                } else {
                    w.eq(TblContractRecord::getIsMajor, isMajorVal);
                }
            }
            // 合规状态可通过数据库条件预过滤，减少内存过滤量
            if ("COMPLIANT".equals(complianceStatusFilter)) {
                w.eq(TblContractRecord::getHasLegalReview, "1");
                w.eq(TblContractRecord::getHasFinanceReview, "1");
            } else if ("VIOLATION".equals(complianceStatusFilter)) {
                w.and(wr -> wr.ne(TblContractRecord::getHasLegalReview, "1")
                    .or().ne(TblContractRecord::getHasFinanceReview, "1")
                    .or().isNull(TblContractRecord::getHasLegalReview)
                    .or().isNull(TblContractRecord::getHasFinanceReview))
                 .and(wr -> wr.isNotNull(TblContractRecord::getHasLegalReview)
                    .or().isNotNull(TblContractRecord::getHasFinanceReview));
            } else if ("PENDING".equals(complianceStatusFilter)) {
                w.isNull(TblContractRecord::getHasLegalReview);
                w.isNull(TblContractRecord::getHasFinanceReview);
            }
            w.orderByDesc(TblContractRecord::getCreateTime);
            Page<TblContractRecord> r = contractRecordMapper.selectPage(new Page<>(pn, ps), w);
            // 字段映射+计算字段
            List<Map<String, Object>> list = new ArrayList<>();
            for (TblContractRecord rec : r.getRecords()) {
                list.add(buildContractVO(rec));
            }
            PageResult<Map<String, Object>> pr = new PageResult<>();
            pr.setTotalRecord((int) r.getTotal()); pr.setCurrentPage((int) r.getCurrent());
            pr.setPageNumber((int) r.getCurrent()); pr.setTotalPage((int) r.getPages());
            pr.setPageSize((int) r.getSize()); pr.setTlist(list);
            return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    /** 将 TblContractRecord 转换为前端所需字段的 Map */
    private Map<String, Object> buildContractVO(TblContractRecord rec) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("id", rec.getContractId());
        m.put("contractId", rec.getContractId());
        m.put("contractNo", rec.getContractCode());        // 前端用 contractNo
        m.put("contractName", rec.getContractName());
        m.put("contractType", rec.getContractType());
        m.put("companyName", rec.getCompanyName());
        m.put("companyId", rec.getCompanyId());
        m.put("counterpartyName", rec.getCounterpartyName());
        m.put("counterpartyCredit", rec.getCounterpartyCredit());
        // 金额：前端用 amount
        BigDecimal amt = rec.getContractAmount() != null ? rec.getContractAmount() : BigDecimal.ZERO;
        m.put("amount", amt);
        m.put("contractAmount", amt);
        m.put("paidAmount", rec.getPaidAmount() != null ? rec.getPaidAmount() : BigDecimal.ZERO);
        m.put("receivedAmount", rec.getReceivedAmount() != null ? rec.getReceivedAmount() : BigDecimal.ZERO);
        m.put("signDate", rec.getSignDate() != null ? rec.getSignDate().toString() : null);
        m.put("effectiveDate", rec.getEffectiveDate() != null ? rec.getEffectiveDate().toString() : null);
        // 到期日：前端用 expireDate
        String expireDate = rec.getExpiryDate() != null ? rec.getExpiryDate().toString() : null;
        m.put("expireDate", expireDate);
        m.put("expiryDate", expireDate);
        // 到期状态
        String expireStatus = "NORMAL";
        if (rec.getExpiryDate() != null) {
            LocalDate today = LocalDate.now();
            if (rec.getExpiryDate().isBefore(today)) {
                expireStatus = "EXPIRED";
            } else if (rec.getExpiryDate().isBefore(today.plusDays(30))) {
                expireStatus = "EXPIRING_SOON";
            }
        }
        m.put("expireStatus", expireStatus);
        // 合规状态：前端用 complianceStatus
        String hasLegal = rec.getHasLegalReview();
        String hasFin = rec.getHasFinanceReview();
        String complianceStatus;
        if ("1".equals(hasLegal) && "1".equals(hasFin)) {
            complianceStatus = "COMPLIANT";
        } else if (hasLegal == null && hasFin == null) {
            complianceStatus = "PENDING";
        } else {
            complianceStatus = "VIOLATION";
        }
        m.put("complianceStatus", complianceStatus);
        m.put("hasLegalReview", hasLegal);
        m.put("hasFinanceReview", hasFin);
        m.put("approvalLevel", rec.getApprovalLevel());
        // 履行进度：前端用 executionProgress
        BigDecimal progress = rec.getActualProgress() != null ? rec.getActualProgress() : BigDecimal.ZERO;
        m.put("executionProgress", progress.intValue());
        m.put("actualProgress", progress);
        m.put("planProgress", rec.getPlanProgress() != null ? rec.getPlanProgress() : BigDecimal.ZERO);
        // 履行状态
        String execStatus = calcExecStatus(rec);
        m.put("execStatus", execStatus);
        m.put("expiringSoon", "EXPIRING_SOON".equals(expireStatus));
        // 是否重大
        boolean isMajor = "1".equals(rec.getIsMajor());
        m.put("isMajor", isMajor);
        m.put("contractStatus", rec.getContractStatus());
        // DATA_SOURCE 为 NULL 时前端显示为 INTERNAL（历史数据来源均为内部）
        m.put("dataSource", rec.getDataSource() != null ? rec.getDataSource() : "INTERNAL");
        m.put("changeCount", rec.getChangeCount() != null ? rec.getChangeCount() : 0);
        m.put("createTime", rec.getCreateTime() != null ? rec.getCreateTime().toString() : null);
        m.put("updateTime", rec.getUpdateTime() != null ? rec.getUpdateTime().toString() : null);
        return m;
    }

    /**
     * 前端合同类型枚举值 → 数据库存储的中文关键字映射
     * DB 当前存中文全称，用 LIKE 模糊匹配兼容不同中文写法
     */
    private String contractTypeCodeToDb(String code) {
        if (code == null) return null;
        switch (code) {
            case "PURCHASE":     return "采购";
            case "SALES":        return "销售";
            case "ENGINEERING":  return "工程";
            case "SERVICE":      return "服务";
            case "OTHER":        return null;  // OTHER 对应范围太广，不做 like，由调用方兜底
            default:             return null;
        }
    }
    private String calcExecStatus(TblContractRecord rec) {
        BigDecimal plan = rec.getPlanProgress() != null ? rec.getPlanProgress() : BigDecimal.ZERO;
        BigDecimal actual = rec.getActualProgress() != null ? rec.getActualProgress() : BigDecimal.ZERO;
        if (actual.compareTo(new BigDecimal("100")) >= 0) return "DONE";
        double diff = plan.subtract(actual).doubleValue();
        if (diff > 20) return "SEVERE_DELAY";
        if (diff > 10) return "LIGHT_DELAY";
        // 已过期且未完成
        if (rec.getExpiryDate() != null && rec.getExpiryDate().isBefore(LocalDate.now()) && actual.compareTo(new BigDecimal("100")) < 0) {
            return "BREACH";
        }
        return "NORMAL";
    }

    @Operation(summary = "合同详情")
    @GetMapping("/detail/{id}")
    public R<TblContractRecord> contractDetail(@PathVariable String id) {
        try { return R.success(contractRecordMapper.selectById(id)); } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "新增合同")
    @PostMapping("/add")
    public R<Boolean> addContract(@RequestBody TblContractRecord record) {
        try {
            record.setCreateTime(LocalDateTime.now());
            // 自动生成合同编号
            if (StringUtils.isBlank(record.getContractCode())) {
                String prefix = "CON-" + LocalDate.now().toString().replace("-", "");
                long count = contractRecordMapper.selectCount(null);
                record.setContractCode(prefix + "-" + String.format("%04d", count + 1));
            }
            if (StringUtils.isBlank(record.getContractStatus())) {
                record.setContractStatus("ACTIVE");
            }
            contractRecordMapper.insert(record);
            return R.success(true);
        } catch (Exception e) {
            log.error("新增合同失败", e);
            return R.fail("新增失败：" + e.getMessage());
        }
    }

    @Operation(summary = "更新合同")
    @PostMapping("/update")
    public R<Boolean> updateContract(@RequestBody TblContractRecord record) {
        try { record.setUpdateTime(LocalDateTime.now()); contractRecordMapper.updateById(record); return R.success(true); } catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); }
    }

    @Operation(summary = "删除合同")
    @PostMapping("/delete/{id}")
    public R<Boolean> deleteContract(@PathVariable String id) {
        try { return R.success(contractRecordMapper.deleteById(id) > 0); } catch (Exception e) { return R.fail("删除失败：" + e.getMessage()); }
    }

    @Operation(summary = "标记重大合同")
    @PostMapping("/major/mark")
    public R<Boolean> markMajorContract(@RequestBody Map<String, Object> params) {
        try { return R.success(true); } catch (Exception e) { return R.fail("操作失败：" + e.getMessage()); }
    }

    @Operation(summary = "合同台账统计数据")
    @GetMapping("/statistics")
    public R<Map<String, Object>> contractStatistics(@RequestParam(required = false) String companyId) {
        try {
            final String orgId = companyId;
            String orgPattern = orgQueryHelper.getOrgPathPattern(orgId);
            LambdaQueryWrapper<TblContractRecord> w = new LambdaQueryWrapper<>();
            if (orgPattern != null) {
                w.and(ww -> ww.like(TblContractRecord::getOrgPath, orgPattern)
                    .or(sub -> sub.isNull(TblContractRecord::getOrgPath).eq(TblContractRecord::getCompanyId, orgId)));
            }
            List<TblContractRecord> all = contractRecordMapper.selectList(w);
            long totalCount = all.size();
            BigDecimal totalAmount = all.stream()
                .map(r -> r.getContractAmount() != null ? r.getContractAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            // 风险数量：已终止的合同
            long riskCount = all.stream().filter(r -> "TERMINATED".equals(r.getContractStatus())).count();
            // 重大合同数：IS_MAJOR = '1'
            long majorCount = all.stream().filter(r -> "1".equals(r.getIsMajor())).count();
            // 审批合规率：法务审核 + 财务审核 双通过 / 合同总数 * 100 (保留一位小数)
            long compliantCount = all.stream()
                .filter(r -> "1".equals(r.getHasLegalReview()) && "1".equals(r.getHasFinanceReview()))
                .count();
            double complianceRate = totalCount > 0 ? Math.round(compliantCount * 1000.0 / totalCount) / 10.0 : 0.0;

            Map<String, Object> result = new LinkedHashMap<>();
            // 合同总量（前端用 totalCount，同时兼容 totalContracts/contractCount）
            result.put("totalCount", totalCount);
            result.put("totalContracts", totalCount);
            result.put("contractCount", totalCount);
            // 合同金额
            result.put("totalAmount", totalAmount);
            result.put("contractAmount", totalAmount);
            // 风险数量
            result.put("riskCount", riskCount);
            result.put("warningCount", riskCount);
            // 重大合同数
            result.put("majorCount", majorCount);
            // 审批合规率
            result.put("complianceRate", complianceRate);
            result.put("compliantCount", compliantCount);
            return R.success(result);
        } catch (Exception e) {
            log.error("获取合同统计数据失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    // ==================== 生命周期 ====================

    @Operation(summary = "合同生命周期")
    @GetMapping("/lifecycle/{id}")
    public R<Map<String, Object>> contractLifecycle(@PathVariable String id) {
        try {
            TblContractRecord rec = contractRecordMapper.selectOne(
                new LambdaQueryWrapper<TblContractRecord>().eq(TblContractRecord::getContractCode, id).last("LIMIT 1")
            );
            Map<String, Object> result = new LinkedHashMap<>();
            if (rec == null) {
                result.put("contractId", id); result.put("nodes", new ArrayList<>());
                return R.success(result);
            }
            result.put("contractId", id);
            result.put("contractNo", rec.getContractCode());
            result.put("contractName", rec.getContractName());
            result.put("contractType", rec.getContractType());
            result.put("amount", rec.getContractAmount());
            result.put("paidAmount", rec.getPaidAmount() != null ? rec.getPaidAmount() : BigDecimal.ZERO);
            result.put("complianceStatus", "1".equals(rec.getHasLegalReview()) && "1".equals(rec.getHasFinanceReview()) ? "COMPLIANT" : "VIOLATION");
            // 构建生命周期节点
            List<Map<String, Object>> nodes = new ArrayList<>();
            nodes.add(buildNode("合同起草", rec.getCreateTime() != null ? rec.getCreateTime().toLocalDate().toString() : "—", "success", "完成", "success", null, null));
            String legalStatus = "1".equals(rec.getHasLegalReview()) ? "通过" : "缺失";
            String legalType = "1".equals(rec.getHasLegalReview()) ? "success" : "danger";
            nodes.add(buildNode("法务审核", "—", legalType, legalStatus, legalType, null, "1".equals(rec.getHasLegalReview()) ? "法务审核通过" : "⚠️ 未经法务审核"));
            String finStatus = "1".equals(rec.getHasFinanceReview()) ? "通过" : "缺失";
            String finType = "1".equals(rec.getHasFinanceReview()) ? "success" : "danger";
            nodes.add(buildNode("财务审核", "—", finType, finStatus, finType, null, "1".equals(rec.getHasFinanceReview()) ? "财务审核通过" : "⚠️ 未经财务审核"));
            nodes.add(buildNode("领导审批", "—", "success", "批准", "success", null, "审批级别：" + (rec.getApprovalLevel() != null ? rec.getApprovalLevel() : "—")));
            nodes.add(buildNode("合同签订", rec.getSignDate() != null ? rec.getSignDate().toString() : "—", "success", "已签署", "success", null, null));
            String execStatus = calcExecStatus(rec);
            String execType = "BREACH".equals(execStatus) || "SEVERE_DELAY".equals(execStatus) ? "danger" : "LIGHT_DELAY".equals(execStatus) ? "warning" : "primary";
            BigDecimal progress = rec.getActualProgress() != null ? rec.getActualProgress() : BigDecimal.ZERO;
            nodes.add(buildNode("履行中", rec.getEffectiveDate() != null ? rec.getEffectiveDate() + "至今" : "—", execType,
                execType.equals("danger") ? "异常" : "履行中", execType, null, "当前履行进度" + progress.intValue() + "%"));
            result.put("nodes", nodes);
            // 付款信息
            Map<String, Object> payment = new HashMap<>();
            BigDecimal paid = rec.getPaidAmount() != null ? rec.getPaidAmount() : BigDecimal.ZERO;
            payment.put("paid", paid);
            payment.put("overdue", 0);
            result.put("payment", payment);
            // 里程碑（暂无独立表，返回空）
            result.put("milestones", new ArrayList<>());
            result.put("changes", new ArrayList<>());
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    private Map<String, Object> buildNode(String step, String time, String type, String statusTag, String statusTagType, String operator, String remark) {
        Map<String, Object> n = new LinkedHashMap<>();
        n.put("step", step); n.put("time", time); n.put("type", type);
        n.put("statusTag", statusTag); n.put("statusTagType", statusTagType);
        n.put("operator", operator); n.put("remark", remark);
        return n;
    }

    @Operation(summary = "新增生命周期节点")
    @PostMapping("/lifecycle/add")
    public R<Boolean> addLifecycleNode(@RequestBody Map<String, Object> params) {
        try { return R.success(true); } catch (Exception e) { return R.fail("新增失败：" + e.getMessage()); }
    }

    @Operation(summary = "更新付款进度")
    @PostMapping("/lifecycle/payment")
    public R<Boolean> updatePaymentProgress(@RequestBody Map<String, Object> params) {
        try { return R.success(true); } catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); }
    }

    @Operation(summary = "获取里程碑")
    @GetMapping("/lifecycle/milestones/{id}")
    public R<List<Map<String, Object>>> getMilestones(@PathVariable String id) {
        try { return R.success(new ArrayList<>()); } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 审批合规 ====================

    @Operation(summary = "合规审批列表")
    @PostMapping("/compliance/list")
    public R<PageResult<Map<String, Object>>> complianceList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            // 查询违规合同：法务或财务未审核的合同
            LambdaQueryWrapper<TblContractRecord> w = new LambdaQueryWrapper<>();
            w.and(wr -> wr.eq(TblContractRecord::getHasLegalReview, "0")
                .or().eq(TblContractRecord::getHasFinanceReview, "0")
                .or().isNull(TblContractRecord::getHasLegalReview)
                .or().isNull(TblContractRecord::getHasFinanceReview));
            if (params.get("companyName") != null && StringUtils.isNotBlank(params.get("companyName").toString())) {
                w.like(TblContractRecord::getCompanyName, params.get("companyName").toString());
            }
            if (params.get("contractType") != null && StringUtils.isNotBlank(params.get("contractType").toString())) {
                w.eq(TblContractRecord::getContractType, params.get("contractType").toString());
            }
            w.orderByDesc(TblContractRecord::getCreateTime);
            Page<TblContractRecord> page = contractRecordMapper.selectPage(new Page<>(pn, ps), w);
            List<Map<String, Object>> list = new ArrayList<>();
            for (TblContractRecord rec : page.getRecords()) {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("contractNo", rec.getContractCode());
                m.put("contractName", rec.getContractName());
                m.put("contractType", rec.getContractType());
                m.put("companyName", rec.getCompanyName());
                m.put("amount", rec.getContractAmount() != null ? rec.getContractAmount() : BigDecimal.ZERO);
                m.put("signDate", rec.getSignDate() != null ? rec.getSignDate().toString() : null);
                // 违规类型推断
                String violationType;
                if (!"1".equals(rec.getHasLegalReview())) {
                    violationType = "未经法务审核";
                } else if (!"1".equals(rec.getHasFinanceReview())) {
                    violationType = "未经财务审核";
                } else {
                    violationType = "审批链不完整";
                }
                m.put("violationType", violationType);
                // 违规等级
                BigDecimal amt = rec.getContractAmount() != null ? rec.getContractAmount() : BigDecimal.ZERO;
                String violationLevel = amt.compareTo(new BigDecimal("5000")) > 0 ? "HIGH" : amt.compareTo(new BigDecimal("1000")) > 0 ? "MEDIUM" : "LOW";
                m.put("violationLevel", violationLevel);
                // 整改状态：从整改记录表查询真实状态
                String rectifyStatus = "PENDING";
                if (rec.getContractId() != null) {
                    String latestStatus = rectificationService.getLatestRectifyStatus(rec.getContractId());
                    if (latestStatus != null) {
                        rectifyStatus = latestStatus;
                    }
                }
                // 支持整改状态筛选
                if (params.get("rectifyStatus") != null && StringUtils.isNotBlank(params.get("rectifyStatus").toString())) {
                    if (!params.get("rectifyStatus").toString().equals(rectifyStatus)) continue;
                }
                m.put("rectifyStatus", rectifyStatus);
                m.put("contractId", rec.getContractId());
                // 审批链路
                m.put("approvalChain", buildApprovalChain(rec));
                list.add(m);
            }
            PageResult<Map<String, Object>> pr = new PageResult<>();
            pr.setTotalRecord((int) page.getTotal()); pr.setCurrentPage(pn);
            pr.setPageNumber(pn); pr.setTotalPage((int) page.getPages());
            pr.setPageSize(ps); pr.setTlist(list);
            return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    private List<Map<String, Object>> buildApprovalChain(TblContractRecord rec) {
        List<Map<String, Object>> chain = new ArrayList<>();
        chain.add(buildStep("起草", "done"));
        chain.add(buildStep("法务审核", "1".equals(rec.getHasLegalReview()) ? "done" : "error"));
        chain.add(buildStep("财务审核", "1".equals(rec.getHasFinanceReview()) ? "done" : "error"));
        chain.add(buildStep("领导审批", rec.getApprovalLevel() != null ? "done" : "wait"));
        chain.add(buildStep("签订", rec.getSignDate() != null ? "done" : "wait"));
        return chain;
    }

    private Map<String, Object> buildStep(String title, String status) {
        Map<String, Object> s = new HashMap<>(); s.put("title", title); s.put("status", status); return s;
    }

    @Operation(summary = "审批链路详情 - 根据合同编号或ID查询审批链路")
    @GetMapping("/compliance/chain/{id}")
    public R<Map<String, Object>> approvalChain(@PathVariable String id) {
        try {
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("contractId", id);

            // 1. 先查询合同基本信息
            TblContractRecord rec = contractRecordMapper.selectById(id);
            if (rec == null) {
                // 尝试按合同编号查询
                LambdaQueryWrapper<TblContractRecord> w = new LambdaQueryWrapper<>();
                w.eq(TblContractRecord::getContractCode, id).last("LIMIT 1");
                rec = contractRecordMapper.selectOne(w);
            }
            if (rec != null) {
                result.put("contractNo", rec.getContractCode());
                result.put("contractName", rec.getContractName());
                result.put("contractType", rec.getContractType());
                result.put("companyName", rec.getCompanyName());
                result.put("amount", rec.getContractAmount());
                result.put("signDate", rec.getSignDate() != null ? rec.getSignDate().toString() : null);
            }

            // 2. 查询审批追踪记录
            String contractId = rec != null ? rec.getContractId() : id;
            List<TblContractApprovalTrack> tracks = approvalTrackMapper.selectByContractId(contractId);

            List<Map<String, Object>> chain = new ArrayList<>();
            if (tracks != null && !tracks.isEmpty()) {
                // 有真实审批追踪数据，使用数据库记录
                for (TblContractApprovalTrack track : tracks) {
                    Map<String, Object> step = new LinkedHashMap<>();
                    step.put("step", track.getStepName());
                    step.put("operator", track.getApproverName());
                    step.put("dept", track.getApproverDept());
                    step.put("time", track.getApprovalTime() != null ? track.getApprovalTime().toString() : null);
                    step.put("comment", track.getApprovalComment());
                    // 映射状态: APPROVE->done, REJECT->error, PENDING->process
                    String action = track.getApprovalAction();
                    String status;
                    if ("APPROVE".equals(action)) {
                        status = "done";
                    } else if ("REJECT".equals(action)) {
                        status = "error";
                    } else if ("SKIP".equals(action)) {
                        status = "skip";
                    } else {
                        status = "process";
                    }
                    step.put("status", status);
                    chain.add(step);
                }
            } else if (rec != null) {
                // 无追踪记录，根据合同字段推导审批链路
                chain = buildApprovalChainDetail(rec);
            }

            result.put("chain", chain);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询审批链路失败，ID：{}", id, e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据合同记录推导详细审批链路（含操作人和时间）
     */
    private List<Map<String, Object>> buildApprovalChainDetail(TblContractRecord rec) {
        List<Map<String, Object>> chain = new ArrayList<>();
        // 起草环节
        Map<String, Object> draft = new LinkedHashMap<>();
        draft.put("step", "合同起草");
        draft.put("operator", rec.getCompanyName());
        draft.put("time", rec.getCreateTime() != null ? rec.getCreateTime().toLocalDate().toString() : null);
        draft.put("status", "done");
        chain.add(draft);
        // 法务审核
        Map<String, Object> legal = new LinkedHashMap<>();
        legal.put("step", "法务审核");
        legal.put("operator", "法务部");
        legal.put("time", null);
        legal.put("status", "1".equals(rec.getHasLegalReview()) ? "done" : "skip");
        if (!"1".equals(rec.getHasLegalReview())) {
            legal.put("comment", "该环节被跳过，存在合规风险");
        }
        chain.add(legal);
        // 财务审核
        Map<String, Object> finance = new LinkedHashMap<>();
        finance.put("step", "财务审核");
        finance.put("operator", "财务部");
        finance.put("time", null);
        finance.put("status", "1".equals(rec.getHasFinanceReview()) ? "done" : "skip");
        if (!"1".equals(rec.getHasFinanceReview())) {
            finance.put("comment", "该环节被跳过，存在合规风险");
        }
        chain.add(finance);
        // 领导审批
        Map<String, Object> leader = new LinkedHashMap<>();
        leader.put("step", "领导审批");
        leader.put("operator", rec.getApprovalLevel() != null ? rec.getApprovalLevel() : null);
        leader.put("time", null);
        leader.put("status", rec.getApprovalLevel() != null ? "done" : "process");
        chain.add(leader);
        // 合同签订
        Map<String, Object> sign = new LinkedHashMap<>();
        sign.put("step", "合同签订");
        sign.put("operator", rec.getCounterpartyName());
        sign.put("time", rec.getSignDate() != null ? rec.getSignDate().toString() : null);
        sign.put("status", rec.getSignDate() != null ? "done" : "process");
        chain.add(sign);
        return chain;
    }

    @Operation(summary = "下发整改通知")
    @PostMapping("/compliance/rectify")
    public R<Boolean> submitRectification(@RequestBody Map<String, Object> params) {
        try {
            // 参数校验
            String contractNo = params.get("contractNo") != null ? params.get("contractNo").toString() : null;
            String violationType = params.get("violationType") != null ? params.get("violationType").toString() : null;
            String measures = params.get("measures") != null ? params.get("measures").toString() : null;
            String owner = params.get("owner") != null ? params.get("owner").toString() : null;
            String deadlineStr = params.get("deadline") != null ? params.get("deadline").toString() : null;
            String remark = params.get("remark") != null ? params.get("remark").toString() : null;
            String contractId = params.get("contractId") != null ? params.get("contractId").toString() : null;

            if (StringUtils.isBlank(contractNo) && StringUtils.isBlank(contractId)) {
                return R.fail("合同编号或合同ID不能为空");
            }
            if (StringUtils.isBlank(measures)) {
                return R.fail("整改措施不能为空");
            }
            if (StringUtils.isBlank(owner)) {
                return R.fail("责任人不能为空");
            }

            // 如果没有contractId，通过contractNo查找
            if (StringUtils.isBlank(contractId) && StringUtils.isNotBlank(contractNo)) {
                LambdaQueryWrapper<TblContractRecord> w = new LambdaQueryWrapper<>();
                w.eq(TblContractRecord::getContractCode, contractNo).last("LIMIT 1");
                TblContractRecord rec = contractRecordMapper.selectOne(w);
                if (rec != null) {
                    contractId = rec.getContractId();
                }
            }

            // 构建整改记录
            GzctContractRectification rectification = new GzctContractRectification();
            rectification.setContractNo(contractNo);
            rectification.setContractId(contractId);
            rectification.setViolationType(violationType);
            rectification.setMeasures(measures);
            rectification.setOwner(owner);
            if (StringUtils.isNotBlank(deadlineStr)) {
                rectification.setDeadline(LocalDate.parse(deadlineStr));
            }
            rectification.setRemark(remark);

            boolean success = rectificationService.submitRectification(rectification);
            if (success) {
                return R.success(true);
            } else {
                return R.fail("下发整改失败");
            }
        } catch (Exception e) {
            log.error("下发整改失败", e);
            return R.fail("提交失败：" + e.getMessage());
        }
    }

    @Operation(summary = "合规详情 - 根据合同编号查询完整合规信息")
    @GetMapping("/compliance/detail/{contractNo}")
    public R<Map<String, Object>> complianceDetail(@PathVariable String contractNo) {
        try {
            // 查询合同记录
            LambdaQueryWrapper<TblContractRecord> w = new LambdaQueryWrapper<>();
            w.eq(TblContractRecord::getContractCode, contractNo).last("LIMIT 1");
            TblContractRecord rec = contractRecordMapper.selectOne(w);
            if (rec == null) {
                // 尝试按ID查询
                rec = contractRecordMapper.selectById(contractNo);
            }
            if (rec == null) {
                return R.fail("未找到合同记录");
            }

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("contractId", rec.getContractId());
            result.put("contractNo", rec.getContractCode());
            result.put("contractName", rec.getContractName());
            result.put("contractType", rec.getContractType());
            result.put("companyName", rec.getCompanyName());
            result.put("counterpartyName", rec.getCounterpartyName());
            result.put("amount", rec.getContractAmount() != null ? rec.getContractAmount() : BigDecimal.ZERO);
            result.put("signDate", rec.getSignDate() != null ? rec.getSignDate().toString() : null);
            result.put("effectiveDate", rec.getEffectiveDate() != null ? rec.getEffectiveDate().toString() : null);
            result.put("expiryDate", rec.getExpiryDate() != null ? rec.getExpiryDate().toString() : null);
            result.put("hasLegalReview", rec.getHasLegalReview());
            result.put("hasFinanceReview", rec.getHasFinanceReview());
            result.put("approvalLevel", rec.getApprovalLevel());

            // 违规类型推断
            String violationType;
            if (!"1".equals(rec.getHasLegalReview())) {
                violationType = "未经法务审核";
            } else if (!"1".equals(rec.getHasFinanceReview())) {
                violationType = "未经财务审核";
            } else {
                violationType = "审批链不完整";
            }
            result.put("violationType", violationType);

            // 违规等级
            BigDecimal amt = rec.getContractAmount() != null ? rec.getContractAmount() : BigDecimal.ZERO;
            String violationLevel = amt.compareTo(new BigDecimal("5000")) > 0 ? "HIGH" : amt.compareTo(new BigDecimal("1000")) > 0 ? "MEDIUM" : "LOW";
            result.put("violationLevel", violationLevel);

            // 整改记录
            List<GzctContractRectification> rectifications = rectificationMapper.selectByContractId(rec.getContractId());
            String rectifyStatus = "PENDING";
            List<Map<String, Object>> rectifyHistory = new ArrayList<>();
            if (rectifications != null && !rectifications.isEmpty()) {
                rectifyStatus = rectifications.get(0).getRectifyStatus();
                for (GzctContractRectification r : rectifications) {
                    Map<String, Object> rMap = new LinkedHashMap<>();
                    rMap.put("rectificationId", r.getRectificationId());
                    rMap.put("measures", r.getMeasures());
                    rMap.put("owner", r.getOwner());
                    rMap.put("deadline", r.getDeadline() != null ? r.getDeadline().toString() : null);
                    rMap.put("rectifyStatus", r.getRectifyStatus());
                    rMap.put("issuedTime", r.getIssuedTime() != null ? r.getIssuedTime().toString() : null);
                    rMap.put("remark", r.getRemark());
                    rectifyHistory.add(rMap);
                }
            }
            result.put("rectifyStatus", rectifyStatus);
            result.put("rectifyHistory", rectifyHistory);

            // 审批链路
            List<TblContractApprovalTrack> tracks = approvalTrackMapper.selectByContractId(rec.getContractId());
            List<Map<String, Object>> chain;
            if (tracks != null && !tracks.isEmpty()) {
                chain = new ArrayList<>();
                for (TblContractApprovalTrack track : tracks) {
                    Map<String, Object> step = new LinkedHashMap<>();
                    step.put("step", track.getStepName());
                    step.put("operator", track.getApproverName());
                    step.put("time", track.getApprovalTime() != null ? track.getApprovalTime().toString() : null);
                    String action = track.getApprovalAction();
                    step.put("status", "APPROVE".equals(action) ? "done" : "REJECT".equals(action) ? "error" : "SKIP".equals(action) ? "skip" : "process");
                    chain.add(step);
                }
            } else {
                chain = buildApprovalChainDetail(rec);
            }
            result.put("approvalChain", chain);

            return R.success(result);
        } catch (Exception e) {
            log.error("查询合规详情失败，合同编号：{}", contractNo, e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    // ==================== 履行监控 ====================

    @Operation(summary = "履行监控列表")
    @PostMapping("/execution/list")
    public R<PageResult<Map<String, Object>>> executionList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;

            // 构建数据库层面可过滤的条件
            LambdaQueryWrapper<TblContractRecord> w = new LambdaQueryWrapper<>();
            if (params.get("companyName") != null && StringUtils.isNotBlank(params.get("companyName").toString())) {
                w.like(TblContractRecord::getCompanyName, params.get("companyName").toString());
            }
            if (params.get("contractType") != null && StringUtils.isNotBlank(params.get("contractType").toString())) {
                w.eq(TblContractRecord::getContractType, params.get("contractType").toString());
            }
            if (params.get("contractNo") != null && StringUtils.isNotBlank(params.get("contractNo").toString())) {
                w.like(TblContractRecord::getContractCode, params.get("contractNo").toString());
            }
            if (params.get("contractName") != null && StringUtils.isNotBlank(params.get("contractName").toString())) {
                w.like(TblContractRecord::getContractName, params.get("contractName").toString());
            }
            // 到期日期范围筛选
            if (params.get("expireDateStart") != null && StringUtils.isNotBlank(params.get("expireDateStart").toString())) {
                w.ge(TblContractRecord::getExpiryDate, LocalDate.parse(params.get("expireDateStart").toString()));
            }
            if (params.get("expireDateEnd") != null && StringUtils.isNotBlank(params.get("expireDateEnd").toString())) {
                w.le(TblContractRecord::getExpiryDate, LocalDate.parse(params.get("expireDateEnd").toString()));
            }
            w.orderByDesc(TblContractRecord::getCreateTime);

            // execStatus 是计算字段，需要先查全量再内存过滤后手动分页
            String execStatusFilter = null;
            if (params.get("execStatus") != null && StringUtils.isNotBlank(params.get("execStatus").toString())) {
                execStatusFilter = params.get("execStatus").toString();
            }

            if (execStatusFilter != null) {
                // 有 execStatus 过滤时：查全量 -> 计算 -> 过滤 -> 手动分页
                List<TblContractRecord> allRecords = contractRecordMapper.selectList(w);
                List<Map<String, Object>> filteredList = new ArrayList<>();
                for (TblContractRecord rec : allRecords) {
                    Map<String, Object> m = buildContractVO(rec);
                    if (execStatusFilter.equals(m.get("execStatus"))) {
                        filteredList.add(m);
                    }
                }
                int total = filteredList.size();
                int fromIdx = Math.min((pn - 1) * ps, total);
                int toIdx = Math.min(pn * ps, total);
                List<Map<String, Object>> pageList = filteredList.subList(fromIdx, toIdx);
                PageResult<Map<String, Object>> pr = new PageResult<>();
                pr.setTotalRecord(total);
                pr.setCurrentPage(pn);
                pr.setPageNumber(pn);
                pr.setTotalPage(ps > 0 ? (int) Math.ceil((double) total / ps) : 0);
                pr.setPageSize(ps);
                pr.setTlist(pageList);
                return R.success(pr);
            } else {
                // 无 execStatus 过滤时：正常分页查询
                Page<TblContractRecord> page = contractRecordMapper.selectPage(new Page<>(pn, ps), w);
                List<Map<String, Object>> list = new ArrayList<>();
                for (TblContractRecord rec : page.getRecords()) {
                    list.add(buildContractVO(rec));
                }
                PageResult<Map<String, Object>> pr = new PageResult<>();
                pr.setTotalRecord((int) page.getTotal());
                pr.setCurrentPage(pn);
                pr.setPageNumber(pn);
                pr.setTotalPage((int) page.getPages());
                pr.setPageSize(ps);
                pr.setTlist(list);
                return R.success(pr);
            }
        } catch (Exception e) {
            log.error("查询履行监控列表失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "即将到期合同")
    @GetMapping("/execution/expiring")
    public R<List<Map<String, Object>>> expiringContracts() {
        try {
            LambdaQueryWrapper<TblContractRecord> w = new LambdaQueryWrapper<>();
            w.isNotNull(TblContractRecord::getExpiryDate)
             .gt(TblContractRecord::getExpiryDate, LocalDate.now())
             .lt(TblContractRecord::getExpiryDate, LocalDate.now().plusDays(30));
            List<TblContractRecord> recs = contractRecordMapper.selectList(w);
            List<Map<String, Object>> result = new ArrayList<>();
            for (TblContractRecord rec : recs) result.add(buildContractVO(rec));
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 纠纷诉讼 ====================

    @Operation(summary = "纠纷列表")
    @PostMapping("/dispute/list")
    public R<PageResult<TblContractDispute>> disputeList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            Page<TblContractDispute> r = contractDisputeMapper.selectPage(new Page<>(pn, ps),
                new LambdaQueryWrapper<TblContractDispute>().orderByDesc(TblContractDispute::getCreateTime));
            return R.success(buildPageResult(r));
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "纠纷详情")
    @GetMapping("/dispute/detail/{id}")
    public R<TblContractDispute> disputeDetail(@PathVariable String id) {
        try { return R.success(contractDisputeMapper.selectById(id)); } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "新增纠纷节点")
    @PostMapping("/dispute/node/add")
    public R<Boolean> addDisputeNode(@RequestBody Map<String, Object> params) {
        try { return R.success(true); } catch (Exception e) { return R.fail("新增失败：" + e.getMessage()); }
    }

    @Operation(summary = "新增纠纷")
    @PostMapping("/dispute/add")
    public R<Boolean> addDispute(@RequestBody TblContractDispute record) {
        try { record.setCreateTime(LocalDateTime.now()); contractDisputeMapper.insert(record); return R.success(true); } catch (Exception e) { return R.fail("新增失败：" + e.getMessage()); }
    }

    @Operation(summary = "更新纠纷")
    @PostMapping("/dispute/update")
    public R<Boolean> updateDispute(@RequestBody TblContractDispute record) {
        try { record.setUpdateTime(LocalDateTime.now()); contractDisputeMapper.updateById(record); return R.success(true); } catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); }
    }

    // ==================== 对方信用 ====================

    @Operation(summary = "对方信用列表")
    @PostMapping("/counterparty/list")
    public R<PageResult<Map<String, Object>>> counterpartyList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            // 按对方名称分组统计
            LambdaQueryWrapper<TblContractRecord> w = new LambdaQueryWrapper<>();
            w.isNotNull(TblContractRecord::getCounterpartyName);
            if (params.get("counterpartyName") != null && StringUtils.isNotBlank(params.get("counterpartyName").toString())) {
                w.like(TblContractRecord::getCounterpartyName, params.get("counterpartyName").toString());
            }
            w.orderByDesc(TblContractRecord::getCreateTime);
            Page<TblContractRecord> page = contractRecordMapper.selectPage(new Page<>(pn, ps), w);
            // 按 counterpartyName 分组，每个对方只取第一条代表
            Map<String, Map<String, Object>> cpMap = new LinkedHashMap<>();
            DateTimeFormatter dateFmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            for (TblContractRecord rec : page.getRecords()) {
                String cpName = rec.getCounterpartyName();
                if (cpName == null) continue;
                cpMap.computeIfAbsent(cpName, k -> {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("counterpartyName", cpName);
                    m.put("contractCount", 0);
                    m.put("totalAmount", BigDecimal.ZERO);
                    // 信用状态：根据COUNTERPARTY_CREDIT字段判断
                    String credit = rec.getCounterpartyCredit();
                    String creditStatus;
                    if (credit != null && credit.contains("失信")) {
                        creditStatus = "BLACKLIST";
                    } else if (credit != null && credit.contains("预警")) {
                        creditStatus = "WARNING";
                    } else if (credit != null && credit.contains("异常")) {
                        creditStatus = "MINOR";
                    } else {
                        creditStatus = "GOOD";
                    }
                    m.put("creditStatus", creditStatus);
                    // 行业分类：根据contractType映射
                    String contractType = rec.getContractType();
                    String industry;
                    if ("PURCHASE".equals(contractType)) {
                        industry = "采购服务";
                    } else if ("SALES".equals(contractType)) {
                        industry = "销售贸易";
                    } else if ("ENGINEERING".equals(contractType)) {
                        industry = "建筑工程";
                    } else if ("SERVICE".equals(contractType)) {
                        industry = "技术服务";
                    } else {
                        industry = "综合业务";
                    }
                    m.put("industry", industry);
                    m.put("creditCode", credit);
                    // 最新更新时间：初始化为当前记录的createTime
                    m.put("updateTime", rec.getCreateTime() != null ? rec.getCreateTime().format(dateFmt) : "—");
                    m.put("_latestTime", rec.getCreateTime());
                    return m;
                });
                Map<String, Object> cpEntry = cpMap.get(cpName);
                cpEntry.put("contractCount", (int) cpEntry.get("contractCount") + 1);
                BigDecimal cur = rec.getContractAmount() != null ? rec.getContractAmount() : BigDecimal.ZERO;
                cpEntry.put("totalAmount", ((BigDecimal) cpEntry.get("totalAmount")).add(cur));
                // 更新最新时间：取所有记录中最大的createTime
                LocalDateTime existingTime = (LocalDateTime) cpEntry.get("_latestTime");
                LocalDateTime currentTime = rec.getCreateTime();
                if (currentTime != null && (existingTime == null || currentTime.isAfter(existingTime))) {
                    cpEntry.put("_latestTime", currentTime);
                    cpEntry.put("updateTime", currentTime.format(dateFmt));
                }
            }
            // 移除内部辅助字段_latestTime
            List<Map<String, Object>> list = new ArrayList<>(cpMap.values());
            for (Map<String, Object> entry : list) {
                entry.remove("_latestTime");
            }
            // 过滤信用状态
            if (params.get("creditStatus") != null && StringUtils.isNotBlank(params.get("creditStatus").toString())) {
                String cs = params.get("creditStatus").toString();
                list.removeIf(m -> !cs.equals(m.get("creditStatus")));
            }
            // 过滤行业分类
            if (params.get("industry") != null && StringUtils.isNotBlank(params.get("industry").toString())) {
                String ind = params.get("industry").toString();
                list.removeIf(m -> !ind.equals(m.get("industry")));
            }
            PageResult<Map<String, Object>> pr = new PageResult<>();
            pr.setTotalRecord(list.size()); pr.setCurrentPage(pn);
            pr.setPageNumber(pn); pr.setTotalPage((int) Math.ceil((double) list.size() / ps));
            pr.setPageSize(ps);
            int from = Math.min((pn - 1) * ps, list.size());
            int to = Math.min(pn * ps, list.size());
            pr.setTlist(list.subList(from, to));
            return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "对方信用详情")
    @GetMapping("/counterparty/detail")
    public R<Map<String, Object>> counterpartyDetail(@RequestParam String name) {
        try {
            LambdaQueryWrapper<TblContractRecord> w = new LambdaQueryWrapper<>();
            w.eq(TblContractRecord::getCounterpartyName, name);
            List<TblContractRecord> recs = contractRecordMapper.selectList(w);
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("name", name);
            result.put("contractCount", recs.size());
            BigDecimal total = recs.stream().map(r -> r.getContractAmount() != null ? r.getContractAmount() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add);
            result.put("totalAmount", total);
            if (!recs.isEmpty()) {
                result.put("creditCode", recs.get(0).getCounterpartyCredit());
            }
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 风险预警 ====================

    @Operation(summary = "合同预警列表")
    @PostMapping("/warning/list")
    public R<PageResult<Map<String, Object>>> warningList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            List<Map<String, Object>> warnings = new ArrayList<>();
            // 1. 从合同记录提取预警
            LambdaQueryWrapper<TblContractRecord> w = new LambdaQueryWrapper<>();
            w.orderByDesc(TblContractRecord::getCreateTime);
            List<TblContractRecord> recs = contractRecordMapper.selectList(w);
            int warnSeq = 1;
            for (TblContractRecord rec : recs) {
                List<String> warnTypes = new ArrayList<>();
                String level = "LOW";
                int score = 30;
                // 未经法务审核
                if (!"1".equals(rec.getHasLegalReview())) {
                    warnTypes.add("未经法务审核签订");
                    level = "HIGH";
                    score = Math.max(score, 85);
                }
                // 未经财务审核
                if (!"1".equals(rec.getHasFinanceReview())) {
                    warnTypes.add("未经财务审核");
                    if (!"HIGH".equals(level)) level = "MEDIUM";
                    score = Math.max(score, 65);
                }
                // 即将到期
                if (rec.getExpiryDate() != null) {
                    LocalDate today = LocalDate.now();
                    if (rec.getExpiryDate().isBefore(today)) {
                        warnTypes.add("合同已到期未终止");
                        level = "HIGH";
                        score = Math.max(score, 90);
                    } else if (rec.getExpiryDate().isBefore(today.plusDays(30))) {
                        warnTypes.add("重大合同即将到期未续签");
                        if (!"HIGH".equals(level)) level = "MEDIUM";
                        score = Math.max(score, 60);
                    }
                }
                // 履行严重滞后
                String execStatus = calcExecStatus(rec);
                if ("BREACH".equals(execStatus)) {
                    warnTypes.add("合同履行严重滞后已过期");
                    level = "HIGH";
                    score = Math.max(score, 92);
                } else if ("SEVERE_DELAY".equals(execStatus)) {
                    warnTypes.add("合同履行严重滞后");
                    if (!"HIGH".equals(level)) level = "HIGH";
                    score = Math.max(score, 78);
                }
                if (warnTypes.isEmpty()) continue;
                // 生成稳定的预警编号（基于合同ID，确保每次请求编号一致）
                String warnNo = "CON-W-" + LocalDate.now().getYear() + "-" + String.format("%03d", warnSeq++);
                if (rec.getContractId() != null) {
                    // 使用合同ID的hashCode生成稳定编号
                    int hash = Math.abs(rec.getContractId().hashCode()) % 9000 + 1000;
                    warnNo = "CON-W-" + LocalDate.now().getYear() + "-" + hash;
                }
                // 过滤条件
                String warnType = String.join("，", warnTypes);
                String companyName = rec.getCompanyName() != null ? rec.getCompanyName() : "";
                if (params.get("companyName") != null && StringUtils.isNotBlank(params.get("companyName").toString())) {
                    if (!companyName.contains(params.get("companyName").toString())) continue;
                }
                // 查询持久化状态
                GzctContractWarning persisted = contractWarningMapper.selectByWarnNo(warnNo);
                String status = "PENDING";
                String owner = "";
                if (persisted != null) {
                    status = persisted.getStatus() != null ? persisted.getStatus() : "PENDING";
                    owner = persisted.getOwner() != null ? persisted.getOwner() : "";
                }
                // 预警时间：取合同创建时间
                String warnTime = rec.getCreateTime() != null ? rec.getCreateTime().toString().replace("T", " ") : "";
                if (warnTime.length() > 16) warnTime = warnTime.substring(0, 16);

                Map<String, Object> warn = new LinkedHashMap<>();
                warn.put("warnNo", warnNo);
                warn.put("warnTime", warnTime);
                warn.put("companyName", companyName);
                warn.put("warnType", warnType);
                warn.put("level", level);
                warn.put("score", score);
                warn.put("status", status);
                warn.put("owner", owner);
                warn.put("contractNo", rec.getContractCode());
                warn.put("contractName", rec.getContractName());
                warn.put("contractId", rec.getContractId());
                warnings.add(warn);
            }
            // 2. 从纠纷记录提取预警
            List<TblContractDispute> disputes = contractDisputeMapper.selectList(
                new LambdaQueryWrapper<TblContractDispute>().orderByDesc(TblContractDispute::getCreateTime));
            for (TblContractDispute d : disputes) {
                String cpName = d.getCounterpartyName() != null ? d.getCounterpartyName() : "";
                String companyName = d.getCompanyId() != null ? d.getCompanyId() : "";
                if (params.get("companyName") != null && StringUtils.isNotBlank(params.get("companyName").toString())) {
                    if (!companyName.contains(params.get("companyName").toString()) && !cpName.contains(params.get("companyName").toString())) continue;
                }
                String warnNo = "CON-W-" + LocalDate.now().getYear() + "-D" + String.format("%03d", warnSeq++);
                if (d.getDisputeId() != null) {
                    int hash = Math.abs(d.getDisputeId().hashCode()) % 9000 + 1000;
                    warnNo = "CON-W-" + LocalDate.now().getYear() + "-D" + hash;
                }
                // 查询持久化状态
                GzctContractWarning persisted = contractWarningMapper.selectByWarnNo(warnNo);
                String status;
                String owner = "";
                if (persisted != null) {
                    status = persisted.getStatus() != null ? persisted.getStatus() : "PENDING";
                    owner = persisted.getOwner() != null ? persisted.getOwner() : "";
                } else {
                    status = "NEGOTIATION".equals(d.getCaseStatus()) || "MEDIATION".equals(d.getCaseStatus()) ? "PROCESSING" : "PENDING";
                }
                String warnTime = d.getCreateTime() != null ? d.getCreateTime().toString().replace("T", " ") : "";
                if (warnTime.length() > 16) warnTime = warnTime.substring(0, 16);

                Map<String, Object> warn = new LinkedHashMap<>();
                warn.put("warnNo", warnNo);
                warn.put("warnTime", warnTime);
                warn.put("companyName", companyName);
                warn.put("warnType", "合同对方列入失信名单或纠纷: " + (d.getDisputeType() != null ? d.getDisputeType() : ""));
                warn.put("level", "HIGH");
                warn.put("score", 88);
                warn.put("status", status);
                warn.put("owner", owner);
                warn.put("contractName", d.getContractId());
                warn.put("contractId", d.getContractId());
                warnings.add(warn);
            }
            // 过滤状态
            if (params.get("status") != null && StringUtils.isNotBlank(params.get("status").toString())) {
                String st = params.get("status").toString();
                warnings.removeIf(m -> !st.equals(m.get("status")));
            }
            // 过滤预警级别（统一处理，覆盖合同和纠纷两个数据源）
            if (params.get("level") != null && StringUtils.isNotBlank(params.get("level").toString())) {
                String lv = params.get("level").toString();
                warnings.removeIf(m -> !lv.equals(m.get("level")));
            }
            // 过滤预警编号
            if (params.get("warnNo") != null && StringUtils.isNotBlank(params.get("warnNo").toString())) {
                String wn = params.get("warnNo").toString();
                warnings.removeIf(m -> !String.valueOf(m.get("warnNo")).contains(wn));
            }
            int total = warnings.size();
            int from = Math.min((pn - 1) * ps, total);
            int to = Math.min(pn * ps, total);
            PageResult<Map<String, Object>> pr = new PageResult<>();
            pr.setTotalRecord(total); pr.setCurrentPage(pn); pr.setPageNumber(pn);
            pr.setTotalPage((int) Math.ceil((double) total / ps)); pr.setPageSize(ps);
            pr.setTlist(new ArrayList<>(warnings.subList(from, to)));
            return R.success(pr);
        } catch (Exception e) {
            log.error("查询合同预警列表失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "处理合同预警")
    @PostMapping("/warning/handle")
    public R<Boolean> handleWarning(@RequestBody Map<String, Object> params) {
        try {
            String warnNo = params.get("warnNo") != null ? params.get("warnNo").toString() : null;
            String measures = params.get("measures") != null ? params.get("measures").toString() : null;
            String owner = params.get("owner") != null ? params.get("owner").toString() : null;
            String deadline = params.get("deadline") != null ? params.get("deadline").toString() : null;
            String remark = params.get("remark") != null ? params.get("remark").toString() : null;
            if (StringUtils.isBlank(warnNo)) {
                return R.fail("预警编号不能为空");
            }
            if (StringUtils.isBlank(measures)) {
                return R.fail("处置措施不能为空");
            }
            boolean success = contractWarningService.handleWarning(warnNo, measures, owner, deadline, remark);
            return success ? R.success(true) : R.fail("处置失败");
        } catch (Exception e) {
            log.error("处理合同预警失败", e);
            return R.fail("处理失败：" + e.getMessage());
        }
    }

    @Operation(summary = "关闭合同预警")
    @PostMapping("/warning/close/{id}")
    public R<Boolean> closeWarning(@PathVariable String id) {
        try {
            if (StringUtils.isBlank(id)) {
                return R.fail("预警编号不能为空");
            }
            boolean success = contractWarningService.closeWarning(id);
            return success ? R.success(true) : R.fail("关闭失败");
        } catch (Exception e) {
            log.error("关闭合同预警失败，编号：{}", id, e);
            return R.fail("关闭失败：" + e.getMessage());
        }
    }

    @Operation(summary = "预警详情")
    @GetMapping("/warning/detail/{warnNo}")
    public R<Map<String, Object>> warningDetail(@PathVariable String warnNo) {
        try {
            GzctContractWarning persisted = contractWarningMapper.selectByWarnNo(warnNo);
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("warnNo", warnNo);
            if (persisted != null) {
                result.put("status", persisted.getStatus());
                result.put("owner", persisted.getOwner());
                result.put("measures", persisted.getMeasures());
                result.put("deadline", persisted.getDeadline());
                result.put("remark", persisted.getRemark());
                result.put("handleTime", persisted.getHandleTime() != null ? persisted.getHandleTime().toString().replace("T", " ") : null);
                result.put("closeTime", persisted.getCloseTime() != null ? persisted.getCloseTime().toString().replace("T", " ") : null);
                result.put("contractId", persisted.getContractId());
                result.put("contractName", persisted.getContractName());
                result.put("companyName", persisted.getCompanyName());
                result.put("warnType", persisted.getWarnType());
                result.put("level", persisted.getRiskLevel());
                result.put("score", persisted.getRiskScore());
                result.put("warnTime", persisted.getWarnTime() != null ? persisted.getWarnTime().toString().replace("T", " ") : null);
            } else {
                result.put("status", "PENDING");
                result.put("owner", null);
                result.put("measures", null);
            }
            // 构建处置时间线
            List<Map<String, Object>> timeline = new ArrayList<>();
            Map<String, Object> t1 = new LinkedHashMap<>();
            t1.put("action", "预警触发");
            t1.put("time", persisted != null && persisted.getWarnTime() != null ? persisted.getWarnTime().toString().replace("T", " ") : null);
            t1.put("desc", "系统自动识别风险");
            timeline.add(t1);
            if (persisted != null && persisted.getHandleTime() != null) {
                Map<String, Object> t2 = new LinkedHashMap<>();
                t2.put("action", "开始处置");
                t2.put("time", persisted.getHandleTime().toString().replace("T", " "));
                t2.put("desc", "负责人：" + (persisted.getOwner() != null ? persisted.getOwner() : "—") + "，措施：" + (persisted.getMeasures() != null ? persisted.getMeasures() : "—"));
                timeline.add(t2);
            }
            if (persisted != null && persisted.getCloseTime() != null) {
                Map<String, Object> t3 = new LinkedHashMap<>();
                t3.put("action", "处置完成");
                t3.put("time", persisted.getCloseTime().toString().replace("T", " "));
                t3.put("desc", "预警已关闭");
                timeline.add(t3);
            }
            result.put("timeline", timeline);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询预警详情失败，编号：{}", warnNo, e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    // ==================== 穿透分析 ====================

    @Operation(summary = "合同穿透数据")
    @GetMapping("/drill/data")
    public R<Map<String, Object>> drillData(@RequestParam(required = false) String companyId,
                                             @RequestParam(required = false) String contractType) {
        try {
            LambdaQueryWrapper<TblContractRecord> wrapper = new LambdaQueryWrapper<TblContractRecord>()
                .orderByAsc(TblContractRecord::getCompanyName);
            if (StringUtils.isNotEmpty(companyId)) {
                wrapper.eq(TblContractRecord::getCompanyId, companyId);
            }
            if (StringUtils.isNotEmpty(contractType)) {
                wrapper.eq(TblContractRecord::getContractType, contractType);
            }
            List<TblContractRecord> all = contractRecordMapper.selectList(wrapper);
            // 按企业分组
            Map<String, List<TblContractRecord>> byCompany = new LinkedHashMap<>();
            for (TblContractRecord rec : all) {
                String cn = rec.getCompanyName() != null ? rec.getCompanyName() : "未知企业";
                byCompany.computeIfAbsent(cn, k -> new ArrayList<>()).add(rec);
            }
            List<Map<String, Object>> enterprises = new ArrayList<>();
            for (Map.Entry<String, List<TblContractRecord>> entry : byCompany.entrySet()) {
                List<TblContractRecord> recList = entry.getValue();
                long total = recList.size();
                long majorCount = recList.stream().filter(r -> "1".equals(r.getIsMajor())).count();
                long violationCount = recList.stream().filter(r -> !"1".equals(r.getHasLegalReview()) || !"1".equals(r.getHasFinanceReview())).count();
                BigDecimal totalAmt = recList.stream().map(r -> r.getContractAmount() != null ? r.getContractAmount() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add);
                // 按合同类型再分组
                Map<String, List<TblContractRecord>> byType = new LinkedHashMap<>();
                for (TblContractRecord r : recList) {
                    String ct = r.getContractType() != null ? r.getContractType() : "其他";
                    byType.computeIfAbsent(ct, k -> new ArrayList<>()).add(r);
                }
                List<Map<String, Object>> typeChildren = new ArrayList<>();
                for (Map.Entry<String, List<TblContractRecord>> te : byType.entrySet()) {
                    List<Map<String, Object>> contracts = new ArrayList<>();
                    for (TblContractRecord r : te.getValue()) {
                        Map<String, Object> c = new LinkedHashMap<>();
                        c.put("contractId", r.getContractId());
                        c.put("contractNo", r.getContractCode());
                        c.put("contractName", r.getContractName());
                        c.put("amount", r.getContractAmount());
                        c.put("status", r.getContractStatus());
                        c.put("counterpartyName", r.getCounterpartyName());
                        c.put("signDate", r.getSignDate());
                        c.put("expiryDate", r.getExpiryDate());
                        c.put("progress", calcProgress(r));
                        c.put("complianceStatus", "1".equals(r.getHasLegalReview()) && "1".equals(r.getHasFinanceReview()) ? "COMPLIANT" : "VIOLATION");
                        c.put("risk", violationCount > 0 ? "HIGH" : "LOW");
                        contracts.add(c);
                    }
                    Map<String, Object> typeNode = new LinkedHashMap<>();
                    typeNode.put("typeName", te.getKey());
                    typeNode.put("count", te.getValue().size());
                    typeNode.put("contracts", contracts);
                    typeChildren.add(typeNode);
                }
                Map<String, Object> ent = new LinkedHashMap<>();
                ent.put("name", entry.getKey());
                ent.put("total", total);
                ent.put("majorCount", majorCount);
                ent.put("violationCount", violationCount);
                ent.put("totalAmount", totalAmt);
                ent.put("riskLevel", violationCount > 2 ? "HIGH" : violationCount > 0 ? "MEDIUM" : "LOW");
                ent.put("types", typeChildren);
                enterprises.add(ent);
            }
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("name", "集团总部");
            result.put("totalContracts", all.size());
            result.put("enterprises", enterprises);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "穿透数据导出")
    @PostMapping("/drill/export")
    public void drillExport(@RequestBody(required = false) Map<String, String> params,
                            HttpServletResponse response) {
        try {
            String companyId = params != null ? params.get("companyId") : null;
            String contractType = params != null ? params.get("contractType") : null;
            LambdaQueryWrapper<TblContractRecord> wrapper = new LambdaQueryWrapper<TblContractRecord>()
                .orderByAsc(TblContractRecord::getCompanyName);
            if (StringUtils.isNotEmpty(companyId)) {
                wrapper.eq(TblContractRecord::getCompanyId, companyId);
            }
            if (StringUtils.isNotEmpty(contractType)) {
                wrapper.eq(TblContractRecord::getContractType, contractType);
            }
            List<TblContractRecord> all = contractRecordMapper.selectList(wrapper);

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String fileName = "合同穿透数据_" + LocalDate.now().format(dtf) + ".csv";
            response.setContentType("text/csv; charset=UTF-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8"));
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.setCharacterEncoding("UTF-8");

            OutputStream out = response.getOutputStream();
            // 写入 BOM 以便 Excel 正确识别 UTF-8
            out.write(new byte[]{(byte) 0xEF, (byte) 0xBB, (byte) 0xBF});

            StringBuilder sb = new StringBuilder();
            // 表头
            sb.append("企业名称,合同类型,合同编号,合同名称,合同金额(万元),对方单位,签订日期,到期日期,履行进度(%),合规状态,风险等级\n");

            for (TblContractRecord r : all) {
                sb.append(csvCell(r.getCompanyName())).append(",");
                sb.append(csvCell(r.getContractType())).append(",");
                sb.append(csvCell(r.getContractCode())).append(",");
                sb.append(csvCell(r.getContractName())).append(",");
                sb.append(r.getContractAmount() != null ? r.getContractAmount().toPlainString() : "0").append(",");
                sb.append(csvCell(r.getCounterpartyName())).append(",");
                sb.append(r.getSignDate() != null ? r.getSignDate().format(dtf) : "").append(",");
                sb.append(r.getExpiryDate() != null ? r.getExpiryDate().format(dtf) : "").append(",");
                BigDecimal progress = calcProgress(r);
                sb.append(progress != null ? progress.toPlainString() : "0").append(",");
                sb.append("1".equals(r.getHasLegalReview()) && "1".equals(r.getHasFinanceReview()) ? "合规" : "违规").append(",");
                sb.append((!"1".equals(r.getHasLegalReview()) || !"1".equals(r.getHasFinanceReview())) ? "高" : "低");
                sb.append("\n");
            }
            out.write(sb.toString().getBytes("UTF-8"));
            out.flush();
            out.close();
        } catch (Exception e) {
            log.error("穿透数据导出失败", e);
            try {
                response.reset();
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"result\":500,\"msg\":\"导出失败\"}");
            } catch (Exception ignored) {}
        }
    }

    /** CSV 单元格转义：含逗号/引号/换行时用双引号包裹 */
    private String csvCell(String val) {
        if (val == null) return "";
        if (val.contains(",") || val.contains("\"") || val.contains("\n")) {
            return "\"" + val.replace("\"", "\"\"") + "\"";
        }
        return val;
    }

    /**
     * 计算合同履行进度
     */
    private BigDecimal calcProgress(TblContractRecord r) {
        if (r.getActualProgress() != null) {
            return r.getActualProgress();
        }
        if (r.getPaidAmount() != null && r.getContractAmount() != null
            && r.getContractAmount().compareTo(BigDecimal.ZERO) > 0) {
            return r.getPaidAmount().multiply(new BigDecimal("100"))
                .divide(r.getContractAmount(), 2, BigDecimal.ROUND_HALF_UP);
        }
        return BigDecimal.ZERO;
    }

    // ==================== 驾驶舱 ====================

    @Operation(summary = "合同驾驶舱概览")
    @GetMapping("/dashboard/overview")
    public R<Map<String, Object>> dashboardOverview() {
        try {
            List<TblContractRecord> all = contractRecordMapper.selectList(null);
            long totalContracts = all.size();
            long totalDisputes = contractDisputeMapper.selectCount(null);
            // 合规率
            long compliantCount = all.stream().filter(r -> "1".equals(r.getHasLegalReview()) && "1".equals(r.getHasFinanceReview())).count();
            double complianceRate = totalContracts > 0 ? Math.round(compliantCount * 1000.0 / totalContracts) / 10.0 : 0;
            // 履行滞后数
            long delayCount = all.stream().filter(r -> { String s = calcExecStatus(r); return "SEVERE_DELAY".equals(s) || "LIGHT_DELAY".equals(s); }).count();
            // 活跃预警数（违规合同）
            long warningCount = all.stream().filter(r -> !"1".equals(r.getHasLegalReview()) || !"1".equals(r.getHasFinanceReview())).count();
            // 合同类型分布（翻译为中文）
            Map<String, String> typeNameMap = new LinkedHashMap<>();
            typeNameMap.put("PURCHASE", "采购合同"); typeNameMap.put("SALES", "销售合同");
            typeNameMap.put("ENGINEERING", "工程合同"); typeNameMap.put("SERVICE", "服务合同"); typeNameMap.put("OTHER", "其他");
            Map<String, Long> typeCountMap = new LinkedHashMap<>();
            for (TblContractRecord r : all) {
                String ct = r.getContractType() != null ? r.getContractType() : "OTHER";
                String ctName = typeNameMap.getOrDefault(ct, ct);
                typeCountMap.merge(ctName, 1L, Long::sum);
            }
            List<Map<String, Object>> typeDistribution = new ArrayList<>();
            String[] typeColors = {"#1677FF", "#52C41A", "#FA8C16", "#722ED1", "#F5222D", "#0050A0"};
            int ci = 0;
            for (Map.Entry<String, Long> e : typeCountMap.entrySet()) {
                Map<String, Object> t = new HashMap<>();
                t.put("name", e.getKey()); t.put("value", e.getValue());
                t.put("color", typeColors[ci % typeColors.length]); ci++;
                typeDistribution.add(t);
            }
            // 履行状态分布
            Map<String, Long> execCountMap = new LinkedHashMap<>();
            for (TblContractRecord r : all) {
                execCountMap.merge(calcExecStatus(r), 1L, Long::sum);
            }
            List<Map<String, Object>> execDistribution = new ArrayList<>();
            Map<String, String> execColorMap = new HashMap<>();
            execColorMap.put("NORMAL", "#52C41A"); execColorMap.put("LIGHT_DELAY", "#FA8C16");
            execColorMap.put("SEVERE_DELAY", "#F5222D"); execColorMap.put("BREACH", "#820014"); execColorMap.put("DONE", "#1677FF");
            Map<String, String> execLabelMap = new HashMap<>();
            execLabelMap.put("NORMAL", "正常履行"); execLabelMap.put("LIGHT_DELAY", "轻微滞后");
            execLabelMap.put("SEVERE_DELAY", "严重滞后"); execLabelMap.put("BREACH", "违约"); execLabelMap.put("DONE", "已完成");
            for (Map.Entry<String, Long> e : execCountMap.entrySet()) {
                Map<String, Object> es = new HashMap<>();
                es.put("name", execLabelMap.getOrDefault(e.getKey(), e.getKey()));
                es.put("value", e.getValue());
                es.put("color", execColorMap.getOrDefault(e.getKey(), "#ccc"));
                execDistribution.add(es);
            }
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("totalContracts", totalContracts);
            result.put("totalDisputes", totalDisputes);
            result.put("complianceRate", complianceRate);
            result.put("delayCount", delayCount);
            result.put("warningCount", warningCount);
            result.put("typeDistribution", typeDistribution);
            result.put("execDistribution", execDistribution);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "驾驶舱趋势数据 - 月度合同金额与合规率")
    @GetMapping("/dashboard/trends")
    public R<Map<String, Object>> dashboardTrends() {
        try {
            List<TblContractRecord> all = contractRecordMapper.selectList(null);
            // 取最近12个月
            LocalDate now = LocalDate.now();
            List<String> months = new ArrayList<>();
            for (int i = 11; i >= 0; i--) {
                LocalDate m = now.minusMonths(i);
                months.add(String.format("%d-%02d", m.getYear(), m.getMonthValue()));
            }

            // 按月份+合同类型统计金额（基于createTime）
            Map<String, Map<String, BigDecimal>> monthTypeAmount = new LinkedHashMap<>();
            Map<String, Integer> monthTotal = new LinkedHashMap<>();
            Map<String, Integer> monthCompliant = new LinkedHashMap<>();
            for (String month : months) {
                monthTypeAmount.put(month, new LinkedHashMap<>());
                monthTotal.put(month, 0);
                monthCompliant.put(month, 0);
            }

            for (TblContractRecord rec : all) {
                // 优先使用signDate，其次createTime
                String recMonth = null;
                if (rec.getSignDate() != null) {
                    recMonth = String.format("%d-%02d", rec.getSignDate().getYear(), rec.getSignDate().getMonthValue());
                } else if (rec.getCreateTime() != null) {
                    recMonth = String.format("%d-%02d", rec.getCreateTime().getYear(), rec.getCreateTime().getMonthValue());
                }
                if (recMonth == null || !monthTypeAmount.containsKey(recMonth)) continue;

                // 金额按类型累加
                String contractType = rec.getContractType() != null ? rec.getContractType() : "OTHER";
                BigDecimal amt = rec.getContractAmount() != null ? rec.getContractAmount() : BigDecimal.ZERO;
                monthTypeAmount.get(recMonth).merge(contractType, amt, BigDecimal::add);

                // 合规率统计
                monthTotal.merge(recMonth, 1, Integer::sum);
                if ("1".equals(rec.getHasLegalReview()) && "1".equals(rec.getHasFinanceReview())) {
                    monthCompliant.merge(recMonth, 1, Integer::sum);
                }
            }

            // 构建合同金额趋势（按类型分系列）
            Set<String> allTypes = new LinkedHashSet<>();
            for (Map<String, BigDecimal> typeMap : monthTypeAmount.values()) {
                allTypes.addAll(typeMap.keySet());
            }
            Map<String, String> typeNameMap = new LinkedHashMap<>();
            typeNameMap.put("PURCHASE", "采购合同"); typeNameMap.put("SALES", "销售合同");
            typeNameMap.put("ENGINEERING", "工程合同"); typeNameMap.put("SERVICE", "服务合同"); typeNameMap.put("OTHER", "其他");

            List<Map<String, Object>> amountSeries = new ArrayList<>();
            for (String type : allTypes) {
                Map<String, Object> series = new LinkedHashMap<>();
                series.put("name", typeNameMap.getOrDefault(type, type));
                List<Double> data = new ArrayList<>();
                for (String month : months) {
                    BigDecimal val = monthTypeAmount.get(month).getOrDefault(type, BigDecimal.ZERO);
                    // 转为万元显示
                    data.add(val.doubleValue());
                }
                series.put("data", data);
                amountSeries.add(series);
            }

            // 构建合规率趋势
            List<Double> complianceRates = new ArrayList<>();
            for (String month : months) {
                int total = monthTotal.get(month);
                int compliant = monthCompliant.get(month);
                double rate = total > 0 ? Math.round(compliant * 1000.0 / total) / 10.0 : 100.0;
                complianceRates.add(rate);
            }

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("months", months);
            result.put("amountSeries", amountSeries);
            result.put("complianceRates", complianceRates);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询驾驶舱趋势数据失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    // ==================== 数据同步 ====================

    @Operation(summary = "同步内部合同数据")
    @PostMapping("/sync/internal")
    public R<Map<String, Object>> syncInternalContracts() {
        try {
            // 查询所有未同步或需要更新的合同记录
            LambdaQueryWrapper<TblContractRecord> w = new LambdaQueryWrapper<>();
            w.isNull(TblContractRecord::getSyncTime)
             .or().lt(TblContractRecord::getSyncTime, LocalDateTime.now().minusHours(24));
            List<TblContractRecord> needSync = contractRecordMapper.selectList(w);
            int syncCount = 0;
            for (TblContractRecord rec : needSync) {
                rec.setSyncTime(LocalDateTime.now());
                rec.setUpdateTime(LocalDateTime.now());
                contractRecordMapper.updateById(rec);
                syncCount++;
            }
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("syncCount", syncCount);
            result.put("syncTime", LocalDateTime.now().toString());
            result.put("totalRecords", contractRecordMapper.selectCount(null));
            return R.success(result);
        } catch (Exception e) {
            log.error("同步内部合同数据失败", e);
            return R.fail("同步失败：" + e.getMessage());
        }
    }

    @Operation(summary = "数据源列表")
    @GetMapping("/datasource/list")
    public R<List<Map<String, Object>>> dataSourceList() {
        try {
            List<Map<String, Object>> sources = new ArrayList<>();
            Map<String, Object> internal = new LinkedHashMap<>();
            internal.put("code", "INTERNAL");
            internal.put("name", "内部系统");
            internal.put("status", "CONNECTED");
            internal.put("lastSyncTime", LocalDateTime.now().minusHours(2).toString());
            sources.add(internal);
            Map<String, Object> external = new LinkedHashMap<>();
            external.put("code", "EXTERNAL");
            external.put("name", "外部接入");
            external.put("status", "CONNECTED");
            external.put("lastSyncTime", LocalDateTime.now().minusHours(6).toString());
            sources.add(external);
            return R.success(sources);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 导出 ====================

    @Operation(summary = "导出合同列表Excel")
    @PostMapping("/export")
    public void exportContracts(HttpServletRequest request, HttpServletResponse response) {
        try {
            // 手动读取请求体，避免 @RequestBody 解析失败导致全局异常
            Map<String, Object> params = null;
            try {
                String body = org.springframework.util.StreamUtils.copyToString(
                    request.getInputStream(), java.nio.charset.StandardCharsets.UTF_8);
                if (StringUtils.isNotBlank(body)) {
                    params = new com.fasterxml.jackson.databind.ObjectMapper().readValue(body, Map.class);
                }
            } catch (Exception parseEx) {
                log.warn("导出接口解析请求体失败，将导出全部数据: {}", parseEx.getMessage());
            }
            // 构建查询条件（复用list接口的筛选逻辑）
            LambdaQueryWrapper<TblContractRecord> w = new LambdaQueryWrapper<>();
            if (params != null) {
                if (params.get("companyName") != null && StringUtils.isNotBlank(params.get("companyName").toString())) {
                    w.like(TblContractRecord::getCompanyName, params.get("companyName").toString());
                }
                if (params.get("contractType") != null && StringUtils.isNotBlank(params.get("contractType").toString())) {
                    w.eq(TblContractRecord::getContractType, params.get("contractType").toString());
                }
                if (params.get("dataSource") != null && StringUtils.isNotBlank(params.get("dataSource").toString())) {
                    w.eq(TblContractRecord::getDataSource, params.get("dataSource").toString());
                }
            }
            w.orderByDesc(TblContractRecord::getCreateTime);
            List<TblContractRecord> records = contractRecordMapper.selectList(w);

            // 使用 SpreadsheetML 2003 XML 格式，纯Java生成，不依赖POI（规避commons-io版本冲突）
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("合同台账_" + LocalDate.now().toString(), "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-Disposition", "attachment;filename*=utf-8''" + fileName + ".xls");

            java.io.PrintWriter writer = new java.io.PrintWriter(
                new java.io.OutputStreamWriter(response.getOutputStream(), "UTF-8"), true);
            // XML头和Workbook声明
            writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            writer.println("<?mso-application progid=\"Excel.Sheet\"?>");
            writer.println("<Workbook xmlns=\"urn:schemas-microsoft-com:office:spreadsheet\"");
            writer.println(" xmlns:ss=\"urn:schemas-microsoft-com:office:spreadsheet\">");
            writer.println("<Styles>");
            writer.println("<Style ss:ID=\"header\"><Font ss:Bold=\"1\" ss:Size=\"11\"/><Interior ss:Color=\"#DCE6F1\" ss:Pattern=\"Solid\"/></Style>");
            writer.println("<Style ss:ID=\"data\"><Font ss:Size=\"10\"/></Style>");
            writer.println("</Styles>");
            writer.println("<Worksheet ss:Name=\"合同台账\">");
            writer.println("<Table>");

            // 列宽
            String[] colWidths = {"100", "160", "80", "140", "140", "80", "90", "90", "70", "60", "70", "50"};
            for (String cw : colWidths) {
                writer.println("<Column ss:Width=\"" + cw + "\"/>");
            }

            // 表头行
            String[] headers = {"合同编号", "合同名称", "合同类型", "签订企业", "合同对方", "金额(万元)",
                    "签订日期", "到期日期", "数据来源", "合规状态", "履行进度(%)", "是否重大"};
            writer.println("<Row ss:StyleID=\"header\">");
            for (String h : headers) {
                writer.println("<Cell><Data ss:Type=\"String\">" + contractXmlEscape(h) + "</Data></Cell>");
            }
            writer.println("</Row>");

            // 数据行
            for (TblContractRecord rec : records) {
                writer.println("<Row ss:StyleID=\"data\">");
                writer.println("<Cell><Data ss:Type=\"String\">" + contractXmlEscape(rec.getContractCode()) + "</Data></Cell>");
                writer.println("<Cell><Data ss:Type=\"String\">" + contractXmlEscape(rec.getContractName()) + "</Data></Cell>");
                writer.println("<Cell><Data ss:Type=\"String\">" + contractXmlEscape(rec.getContractType()) + "</Data></Cell>");
                writer.println("<Cell><Data ss:Type=\"String\">" + contractXmlEscape(rec.getCompanyName()) + "</Data></Cell>");
                writer.println("<Cell><Data ss:Type=\"String\">" + contractXmlEscape(rec.getCounterpartyName()) + "</Data></Cell>");
                writer.println("<Cell><Data ss:Type=\"Number\">" + (rec.getContractAmount() != null ? rec.getContractAmount().toPlainString() : "0") + "</Data></Cell>");
                writer.println("<Cell><Data ss:Type=\"String\">" + contractXmlEscape(rec.getSignDate() != null ? rec.getSignDate().toString() : "") + "</Data></Cell>");
                writer.println("<Cell><Data ss:Type=\"String\">" + contractXmlEscape(rec.getExpiryDate() != null ? rec.getExpiryDate().toString() : "") + "</Data></Cell>");
                writer.println("<Cell><Data ss:Type=\"String\">" + ("INTERNAL".equals(rec.getDataSource()) ? "内部系统" : "外部接入") + "</Data></Cell>");
                String compliance = "1".equals(rec.getHasLegalReview()) && "1".equals(rec.getHasFinanceReview()) ? "合规" :
                        (rec.getHasLegalReview() == null && rec.getHasFinanceReview() == null ? "待审" : "违规");
                writer.println("<Cell><Data ss:Type=\"String\">" + compliance + "</Data></Cell>");
                writer.println("<Cell><Data ss:Type=\"Number\">" + (rec.getActualProgress() != null ? rec.getActualProgress().intValue() : 0) + "</Data></Cell>");
                writer.println("<Cell><Data ss:Type=\"String\">" + ("1".equals(rec.getIsMajor()) ? "是" : "否") + "</Data></Cell>");
                writer.println("</Row>");
            }

            writer.println("</Table>");
            writer.println("</Worksheet>");
            writer.println("</Workbook>");
            writer.flush();
            writer.close();
        } catch (Exception e) {
            log.error("导出合同列表失败", e);
            try {
                response.reset();
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"result\":500,\"msg\":\"导出失败：" + e.getMessage() + "\"}");
            } catch (Exception ex) { log.error("写入错误响应失败", ex); }
        }
    }

    /** XML特殊字符转义 */
    private String contractXmlEscape(String value) {
        if (value == null) return "";
        return value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }

    // ==================== 批量导入 ====================

    @Operation(summary = "批量导入合同")
    @PostMapping("/import")
    public R<Map<String, Object>> importContracts(@RequestParam("file") MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                return R.fail("请选择要导入的文件");
            }
            String fileName = file.getOriginalFilename();
            if (fileName == null || (!fileName.endsWith(".xlsx") && !fileName.endsWith(".xls"))) {
                return R.fail("仅支持Excel文件(.xlsx/.xls)");
            }

            InputStream is = file.getInputStream();
            Workbook workbook;
            if (fileName.endsWith(".xlsx")) {
                workbook = new XSSFWorkbook(is);
            } else {
                workbook = new HSSFWorkbook(is);
            }
            Sheet sheet = workbook.getSheetAt(0);
            int totalRows = sheet.getLastRowNum(); // 不含表头
            int successCount = 0;
            int failCount = 0;
            List<String> errors = new ArrayList<>();

            // 从第2行开始读取（第1行为表头）
            for (int i = 1; i <= totalRows; i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;
                try {
                    TblContractRecord record = new TblContractRecord();
                    // 合同编号（自动生成如果为空）
                    String contractCode = getCellStringValue(row.getCell(0));
                    if (StringUtils.isBlank(contractCode)) {
                        contractCode = "CON-" + System.currentTimeMillis() + "-" + i;
                    }
                    record.setContractCode(contractCode);
                    record.setContractName(getCellStringValue(row.getCell(1)));
                    record.setContractType(getCellStringValue(row.getCell(2)));
                    record.setCompanyName(getCellStringValue(row.getCell(3)));
                    record.setCounterpartyName(getCellStringValue(row.getCell(4)));
                    // 金额
                    String amtStr = getCellStringValue(row.getCell(5));
                    if (StringUtils.isNotBlank(amtStr)) {
                        record.setContractAmount(new BigDecimal(amtStr.replace(",", "")));
                    }
                    // 签订日期
                    String signDateStr = getCellStringValue(row.getCell(6));
                    if (StringUtils.isNotBlank(signDateStr)) {
                        record.setSignDate(LocalDate.parse(signDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    }
                    // 到期日期
                    String expiryDateStr = getCellStringValue(row.getCell(7));
                    if (StringUtils.isNotBlank(expiryDateStr)) {
                        record.setExpiryDate(LocalDate.parse(expiryDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                    }
                    // 数据来源
                    String dataSourceStr = getCellStringValue(row.getCell(8));
                    record.setDataSource("外部接入".equals(dataSourceStr) ? "EXTERNAL" : "INTERNAL");
                    // 是否重大
                    String isMajorStr = getCellStringValue(row.getCell(11));
                    record.setIsMajor("是".equals(isMajorStr) ? "1" : "0");

                    record.setCreateTime(LocalDateTime.now());
                    record.setContractStatus("ACTIVE");

                    // 校验必填字段
                    if (StringUtils.isBlank(record.getContractName())) {
                        errors.add("第" + (i + 1) + "行：合同名称不能为空");
                        failCount++;
                        continue;
                    }

                    contractRecordMapper.insert(record);
                    successCount++;
                } catch (Exception e) {
                    errors.add("第" + (i + 1) + "行：" + e.getMessage());
                    failCount++;
                }
            }
            workbook.close();
            is.close();

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("totalRows", totalRows);
            result.put("successCount", successCount);
            result.put("failCount", failCount);
            result.put("errors", errors.size() > 10 ? errors.subList(0, 10) : errors);
            return R.success(result);
        } catch (Exception e) {
            log.error("批量导入合同失败", e);
            return R.fail("导入失败：" + e.getMessage());
        }
    }

    /** 获取单元格字符串值 */
    private String getCellStringValue(Cell cell) {
        if (cell == null) return "";
        switch (cell.getCellType()) {
            case STRING: return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    java.util.Date date = cell.getDateCellValue();
                    return new java.text.SimpleDateFormat("yyyy-MM-dd").format(date);
                }
                double num = cell.getNumericCellValue();
                if (num == Math.floor(num)) return String.valueOf((long) num);
                return String.valueOf(num);
            case BOOLEAN: return String.valueOf(cell.getBooleanCellValue());
            default: return "";
        }
    }

    // ==================== 合同智能审查 ====================

    @Operation(summary = "智能审查列表（带筛选分页）")
    @PostMapping("/review/list")
    public R<PageResult<GzctContractSmartReview>> reviewList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 10;
            LambdaQueryWrapper<GzctContractSmartReview> w = new LambdaQueryWrapper<>();
            // 合同类型筛选
            if (params.get("contractType") != null && StringUtils.isNotBlank(params.get("contractType").toString())) {
                w.eq(GzctContractSmartReview::getContractType, params.get("contractType").toString());
            }
            // 审查状态筛选
            if (params.get("reviewStatus") != null && StringUtils.isNotBlank(params.get("reviewStatus").toString())) {
                w.eq(GzctContractSmartReview::getReviewStatus, params.get("reviewStatus").toString());
            }
            // 风险等级筛选
            if (params.get("riskLevel") != null && StringUtils.isNotBlank(params.get("riskLevel").toString())) {
                w.eq(GzctContractSmartReview::getRiskLevel, params.get("riskLevel").toString());
            }
            w.orderByDesc(GzctContractSmartReview::getCreateTime);
            Page<GzctContractSmartReview> page = smartReviewMapper.selectPage(new Page<>(pn, ps), w);
            return R.success(PageResult.of(page));
        } catch (Exception e) {
            log.error("查询智能审查列表失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "智能审查详情")
    @GetMapping("/review/detail/{id}")
    public R<GzctContractSmartReview> reviewDetail(@PathVariable String id) {
        try {
            if (StringUtils.isEmpty(id)) {
                return R.fail("审查ID不能为空");
            }
            GzctContractSmartReview review = smartReviewMapper.selectById(id);
            if (review == null) {
                return R.fail("审查记录不存在");
            }
            return R.success(review);
        } catch (Exception e) {
            log.error("查询智能审查详情失败，ID：{}", id, e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "新增审查任务")
    @PostMapping("/review/add")
    public R<Boolean> addReview(@RequestBody Map<String, Object> params) {
        try {
            GzctContractSmartReview review = new GzctContractSmartReview();
            review.setContractName(params.get("contractName") != null ? params.get("contractName").toString() : "");
            review.setContractType(params.get("contractType") != null ? params.get("contractType").toString() : "");
            review.setReviewer(params.get("reviewer") != null ? params.get("reviewer").toString() : "");
            review.setReviewStatus(params.get("reviewStatus") != null ? params.get("reviewStatus").toString() : "pending");
            // 风险等级：前端可选传入
            if (params.get("riskLevel") != null && StringUtils.isNotBlank(params.get("riskLevel").toString())) {
                review.setRiskLevel(params.get("riskLevel").toString());
            }
            // 审查时间：前端可选传入
            if (params.get("reviewTime") != null && StringUtils.isNotBlank(params.get("reviewTime").toString())) {
                review.setReviewTime(LocalDateTime.parse(params.get("reviewTime").toString().replace(" ", "T")));
            }
            review.setCreateTime(LocalDateTime.now());
            review.setUpdateTime(LocalDateTime.now());
            smartReviewMapper.insert(review);
            return R.success(true);
        } catch (Exception e) {
            log.error("新增审查任务失败", e);
            return R.fail("新增失败：" + e.getMessage());
        }
    }

    @Operation(summary = "编辑审查记录")
    @PostMapping("/review/update")
    public R<Boolean> updateReview(@RequestBody Map<String, Object> params) {
        try {
            String id = params.get("id") != null ? params.get("id").toString() : null;
            if (StringUtils.isEmpty(id)) {
                return R.fail("审查ID不能为空");
            }
            GzctContractSmartReview review = smartReviewMapper.selectById(id);
            if (review == null) {
                return R.fail("审查记录不存在");
            }
            if (params.get("contractName") != null) review.setContractName(params.get("contractName").toString());
            if (params.get("contractType") != null) review.setContractType(params.get("contractType").toString());
            if (params.get("reviewer") != null) review.setReviewer(params.get("reviewer").toString());
            if (params.get("reviewStatus") != null) review.setReviewStatus(params.get("reviewStatus").toString());
            if (params.get("riskLevel") != null) review.setRiskLevel(params.get("riskLevel").toString());
            if (params.get("remark") != null) review.setRemark(params.get("remark").toString());
            review.setUpdateTime(LocalDateTime.now());
            smartReviewMapper.updateById(review);
            return R.success(true);
        } catch (Exception e) {
            log.error("编辑审查记录失败", e);
            return R.fail("编辑失败：" + e.getMessage());
        }
    }

    @Operation(summary = "删除审查记录")
    @PostMapping("/review/delete/{id}")
    public R<Boolean> deleteReview(@PathVariable String id) {
        try {
            if (StringUtils.isEmpty(id)) {
                return R.fail("审查ID不能为空");
            }
            int rows = smartReviewMapper.deleteById(id);
            if (rows > 0) {
                return R.success(true);
            } else {
                return R.fail("删除失败，记录不存在");
            }
        } catch (Exception e) {
            log.error("删除审查记录失败，ID：{}", id, e);
            return R.fail("删除失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量删除审查记录")
    @PostMapping("/review/batchDelete")
    public R<Boolean> batchDeleteReview(@RequestBody Map<String, Object> params) {
        try {
            @SuppressWarnings("unchecked")
            List<String> ids = (List<String>) params.get("ids");
            if (ids == null || ids.isEmpty()) {
                return R.fail("ID列表不能为空");
            }
            smartReviewMapper.deleteBatchIds(ids);
            return R.success(true);
        } catch (Exception e) {
            log.error("批量删除审查记录失败", e);
            return R.fail("批量删除失败：" + e.getMessage());
        }
    }

    // ==================== 案件管理 ====================

    /**
     * 中文阶段 → 英文caseStatus映射
     */
    private String stageToCaseStatus(String stage) {
        if (stage == null) return null;
        switch (stage) {
            case "协商中": return "NEGOTIATION";
            case "仲裁中": return "MEDIATION";
            case "一审中": return "LITIGATION";
            case "二审中": return "APPEAL";
            case "执行中": return "EXECUTION";
            case "已结案": return "CLOSED";
            default: return stage;
        }
    }

    /**
     * 英文caseStatus → 中文阶段映射
     */
    private String caseStatusToStage(String cs) {
        if (cs == null) return "协商中";
        switch (cs) {
            case "NEGOTIATION": return "协商中";
            case "MEDIATION": return "仲裁中";
            case "LITIGATION": return "一审中";
            case "APPEAL": return "二审中";
            case "EXECUTION": return "执行中";
            case "CLOSED": return "已结案";
            default: return cs;
        }
    }

    /**
     * 根据金额计算败诉风险等级
     */
    private String computeRiskLevel(BigDecimal amount) {
        if (amount == null) return "低";
        if (amount.compareTo(new BigDecimal("1000")) > 0) return "高";
        if (amount.compareTo(new BigDecimal("100")) > 0) return "中";
        return "低";
    }

    @Operation(summary = "案件列表")
    @PostMapping("/case/list")
    public R<PageResult<Map<String, Object>>> caseList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 10;
            LambdaQueryWrapper<TblContractDispute> w = new LambdaQueryWrapper<>();
            // 案件类型筛选
            if (params.get("caseType") != null && StringUtils.isNotBlank(params.get("caseType").toString())) {
                w.eq(TblContractDispute::getDisputeType, params.get("caseType").toString());
            }
            // 当前阶段筛选：前端传中文，转为英文再查询
            if (params.get("stage") != null && StringUtils.isNotBlank(params.get("stage").toString())) {
                String statusCode = stageToCaseStatus(params.get("stage").toString());
                w.eq(TblContractDispute::getCaseStatus, statusCode);
            }
            // 败诉风险筛选：通过金额范围在SQL层过滤
            if (params.get("riskLevel") != null && StringUtils.isNotBlank(params.get("riskLevel").toString())) {
                String rl = params.get("riskLevel").toString();
                switch (rl) {
                    case "高":
                        w.gt(TblContractDispute::getDisputeAmount, new BigDecimal("1000"));
                        break;
                    case "中":
                        w.gt(TblContractDispute::getDisputeAmount, new BigDecimal("100"));
                        w.le(TblContractDispute::getDisputeAmount, new BigDecimal("1000"));
                        break;
                    case "低":
                        w.and(ww -> ww.le(TblContractDispute::getDisputeAmount, new BigDecimal("100"))
                                .or().isNull(TblContractDispute::getDisputeAmount));
                        break;
                }
            }
            w.orderByDesc(TblContractDispute::getCreateTime);
            Page<TblContractDispute> page = contractDisputeMapper.selectPage(new Page<>(pn, ps), w);
            List<Map<String, Object>> list = new ArrayList<>();
            int caseSeq = (pn - 1) * ps + 1;
            for (TblContractDispute d : page.getRecords()) {
                Map<String, Object> m = new LinkedHashMap<>();
                m.put("id", d.getDisputeId());
                m.put("caseNo", String.format("CASE-%d-%04d", LocalDate.now().getYear(), caseSeq++));
                // caseName优先使用disputeReason（用户手动输入的案件名称），其次用counterpartyName拼接
                String caseName = d.getDisputeReason();
                if (StringUtils.isBlank(caseName)) {
                    caseName = d.getCounterpartyName() != null ? d.getCounterpartyName() + "合同纠纷" : "合同纠纷案";
                }
                m.put("caseName", caseName);
                m.put("caseType", d.getDisputeType() != null ? d.getDisputeType() : "合同纠纷");
                m.put("amount", d.getDisputeAmount() != null ? d.getDisputeAmount() : 0);
                m.put("stage", caseStatusToStage(d.getCaseStatus()));
                m.put("riskLevel", computeRiskLevel(d.getDisputeAmount()));
                m.put("filingDate", d.getFilingDate() != null ? d.getFilingDate().toString() : (d.getCreateTime() != null ? d.getCreateTime().toLocalDate().toString() : ""));
                m.put("companyId", d.getCompanyId());
                m.put("counterpartyName", d.getCounterpartyName());
                m.put("courtName", d.getCourtName());
                list.add(m);
            }
            PageResult<Map<String, Object>> pr = new PageResult<>();
            pr.setTotalRecord((int) page.getTotal());
            pr.setCurrentPage(pn);
            pr.setPageNumber(pn);
            pr.setTotalPage((int) page.getPages());
            pr.setPageSize(ps);
            pr.setTlist(list);
            return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "案件详情")
    @GetMapping("/case/detail/{id}")
    public R<Map<String, Object>> caseDetail(@PathVariable String id) {
        try {
            TblContractDispute d = contractDisputeMapper.selectById(id);
            if (d == null) return R.fail("案件不存在");
            Map<String, Object> m = new LinkedHashMap<>();
            m.put("id", d.getDisputeId());
            m.put("caseNo", String.format("CASE-%d-%04d", LocalDate.now().getYear(), 1));
            String caseName = d.getDisputeReason();
            if (StringUtils.isBlank(caseName)) {
                caseName = d.getCounterpartyName() != null ? d.getCounterpartyName() + "合同纠纷" : "合同纠纷案";
            }
            m.put("caseName", caseName);
            m.put("caseType", d.getDisputeType() != null ? d.getDisputeType() : "合同纠纷");
            m.put("amount", d.getDisputeAmount() != null ? d.getDisputeAmount() : 0);
            m.put("stage", caseStatusToStage(d.getCaseStatus()));
            m.put("riskLevel", computeRiskLevel(d.getDisputeAmount()));
            m.put("filingDate", d.getFilingDate() != null ? d.getFilingDate().toString() : (d.getCreateTime() != null ? d.getCreateTime().toLocalDate().toString() : ""));
            m.put("companyId", d.getCompanyId());
            m.put("contractId", d.getContractId());
            m.put("counterpartyName", d.getCounterpartyName());
            m.put("courtName", d.getCourtName());
            m.put("judgmentResult", d.getJudgmentResult());
            m.put("settlementAmount", d.getSettlementAmount());
            m.put("disputeReason", d.getDisputeReason());
            m.put("lessonsLearned", d.getLessonsLearned());
            m.put("createTime", d.getCreateTime() != null ? d.getCreateTime().toString() : "");
            m.put("updateTime", d.getUpdateTime() != null ? d.getUpdateTime().toString() : "");
            return R.success(m);
        } catch (Exception e) { return R.fail("查询详情失败：" + e.getMessage()); }
    }

    @Operation(summary = "新增案件")
    @PostMapping("/case/add")
    public R<Boolean> addCase(@RequestBody Map<String, Object> params) {
        try {
            TblContractDispute d = new TblContractDispute();
            if (params.get("caseName") != null) d.setDisputeReason(params.get("caseName").toString());
            if (params.get("caseType") != null) d.setDisputeType(params.get("caseType").toString());
            if (params.get("amount") != null) d.setDisputeAmount(new BigDecimal(params.get("amount").toString()));
            // 保存阶段：中文转英文
            if (params.get("stage") != null && StringUtils.isNotBlank(params.get("stage").toString())) {
                d.setCaseStatus(stageToCaseStatus(params.get("stage").toString()));
            } else {
                d.setCaseStatus("NEGOTIATION");
            }
            // 保存立案时间
            if (params.get("filingDate") != null && StringUtils.isNotBlank(params.get("filingDate").toString())) {
                d.setFilingDate(LocalDate.parse(params.get("filingDate").toString()));
            }
            // 保存对方单位名称（用于caseName生成）
            if (params.get("counterpartyName") != null) {
                d.setCounterpartyName(params.get("counterpartyName").toString());
            }
            d.setCreateTime(LocalDateTime.now());
            d.setUpdateTime(LocalDateTime.now());
            contractDisputeMapper.insert(d);
            return R.success(true);
        } catch (Exception e) { return R.fail("新增失败：" + e.getMessage()); }
    }

    @Operation(summary = "更新案件")
    @PostMapping("/case/update")
    public R<Boolean> updateCase(@RequestBody Map<String, Object> params) {
        try {
            if (params.get("id") == null) return R.fail("缺少id");
            TblContractDispute d = contractDisputeMapper.selectById(params.get("id").toString());
            if (d == null) return R.fail("案件不存在");
            if (params.get("caseName") != null) d.setDisputeReason(params.get("caseName").toString());
            if (params.get("caseType") != null) d.setDisputeType(params.get("caseType").toString());
            if (params.get("amount") != null) d.setDisputeAmount(new BigDecimal(params.get("amount").toString()));
            if (params.get("stage") != null && StringUtils.isNotBlank(params.get("stage").toString())) {
                d.setCaseStatus(stageToCaseStatus(params.get("stage").toString()));
            }
            if (params.get("filingDate") != null && StringUtils.isNotBlank(params.get("filingDate").toString())) {
                d.setFilingDate(LocalDate.parse(params.get("filingDate").toString()));
            }
            if (params.get("counterpartyName") != null) {
                d.setCounterpartyName(params.get("counterpartyName").toString());
            }
            d.setUpdateTime(LocalDateTime.now());
            contractDisputeMapper.updateById(d);
            return R.success(true);
        } catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); }
    }

    @Operation(summary = "删除案件")
    @PostMapping("/case/delete/{id}")
    public R<Boolean> deleteCase(@PathVariable String id) {
        try {
            contractDisputeMapper.deleteById(id);
            return R.success(true);
        } catch (Exception e) { return R.fail("删除失败：" + e.getMessage()); }
    }

    @Operation(summary = "以案促管分析")
    @PostMapping("/case/analysis")
    public R<Map<String, Object>> caseAnalysis(@RequestBody Map<String, Object> params) {
        try {
            // 查询所有案件数据进行统计分析
            List<TblContractDispute> allCases = contractDisputeMapper.selectList(
                    new LambdaQueryWrapper<TblContractDispute>().orderByDesc(TblContractDispute::getCreateTime));
            Map<String, Object> result = new LinkedHashMap<>();

            // 1. 案件类型分布
            Map<String, Integer> typeDistribution = new LinkedHashMap<>();
            for (TblContractDispute c : allCases) {
                String type = c.getDisputeType() != null ? c.getDisputeType() : "其他";
                typeDistribution.merge(type, 1, Integer::sum);
            }
            result.put("typeDistribution", typeDistribution);

            // 2. 风险等级分布
            Map<String, Integer> riskDistribution = new LinkedHashMap<>();
            riskDistribution.put("高", 0);
            riskDistribution.put("中", 0);
            riskDistribution.put("低", 0);
            for (TblContractDispute c : allCases) {
                String rl = computeRiskLevel(c.getDisputeAmount());
                riskDistribution.merge(rl, 1, Integer::sum);
            }
            result.put("riskDistribution", riskDistribution);

            // 3. 阶段分布
            Map<String, Integer> stageDistribution = new LinkedHashMap<>();
            for (TblContractDispute c : allCases) {
                String stage = caseStatusToStage(c.getCaseStatus());
                stageDistribution.merge(stage, 1, Integer::sum);
            }
            result.put("stageDistribution", stageDistribution);

            // 4. 总体统计
            Map<String, Object> summary = new LinkedHashMap<>();
            summary.put("totalCases", allCases.size());
            BigDecimal totalAmount = allCases.stream()
                    .map(TblContractDispute::getDisputeAmount)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            summary.put("totalAmount", totalAmount);
            long ongoingCount = allCases.stream().filter(c -> !"CLOSED".equals(c.getCaseStatus())).count();
            summary.put("ongoingCases", ongoingCount);
            long closedCount = allCases.stream().filter(c -> "CLOSED".equals(c.getCaseStatus())).count();
            summary.put("closedCases", closedCount);
            long highRiskCount = allCases.stream().filter(c -> "高".equals(computeRiskLevel(c.getDisputeAmount()))).count();
            summary.put("highRiskCases", highRiskCount);
            result.put("summary", summary);

            // 5. 管理建议（基于数据分析生成）
            List<String> suggestions = new ArrayList<>();
            if (highRiskCount > 0) {
                suggestions.add("当前存在" + highRiskCount + "件高风险案件（标的超1000万），建议优先跟进处理");
            }
            if (!typeDistribution.isEmpty()) {
                String maxType = typeDistribution.entrySet().stream()
                        .max(Map.Entry.comparingByValue()).map(Map.Entry::getKey).orElse("");
                suggestions.add("\"" + maxType + "\"类型案件占比最高，建议加强该类合同的前期审查和风控措施");
            }
            if (ongoingCount > 5) {
                suggestions.add("在办案件数量较多（" + ongoingCount + "件），建议增加法务人员或引入外部律师团队协助");
            }
            if (closedCount > 0 && allCases.size() > 0) {
                suggestions.add("已结案" + closedCount + "件，建议定期总结经验教训，形成合同管理改进清单");
            }
            result.put("suggestions", suggestions);

            return R.success(result);
        } catch (Exception e) { return R.fail("分析失败：" + e.getMessage()); }
    }

    // ==================== 审批追踪 ====================

    @Autowired private com.huabo.cybermonitor.service.ITblContractApprovalTrackService approvalTrackService;

    @Operation(summary = "审批追踪列表")
    @PostMapping("/approval/list")
    public R<PageResult<Map<String, Object>>> approvalList(@RequestBody Map<String, Object> params) {
        try {
            return R.success(approvalTrackService.selectApprovalList(params));
        } catch (Exception e) {
            log.error("查询审批追踪列表失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "审批流程详情")
    @GetMapping("/approval/flow/{contractId}")
    public R<Map<String, Object>> approvalFlow(@PathVariable String contractId) {
        try {
            Map<String, Object> result = new LinkedHashMap<>();
            // 获取合同基本信息
            TblContractRecord record = contractRecordMapper.selectById(contractId);
            if (record != null) {
                result.put("contractId", record.getContractId());
                result.put("contractNo", record.getContractCode());
                result.put("contractName", record.getContractName());
                result.put("contractAmount", record.getContractAmount());
                result.put("companyName", record.getCompanyName());
                result.put("contractStatus", record.getContractStatus());
            }
            // 获取审批流程步骤
            List<com.huabo.cybermonitor.entity.TblContractApprovalTrack> tracks = approvalTrackService.getApprovalFlow(contractId);
            if (tracks == null || tracks.isEmpty()) {
                // 如果没有审批记录，根据合同信息生成默认审批流程
                tracks = buildDefaultApprovalFlow(record);
            }
            result.put("approvalSteps", tracks);
            return R.success(result);
        } catch (Exception e) {
            log.error("查询审批流程详情失败，contractId：{}", contractId, e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据合同记录生成默认审批流程（当TBL_CONTRACT_APPROVAL_TRACK表无数据时）
     */
    private List<com.huabo.cybermonitor.entity.TblContractApprovalTrack> buildDefaultApprovalFlow(TblContractRecord record) {
        List<com.huabo.cybermonitor.entity.TblContractApprovalTrack> list = new ArrayList<>();
        if (record == null) return list;

        // 步骤1：部门申请
        com.huabo.cybermonitor.entity.TblContractApprovalTrack step1 = new com.huabo.cybermonitor.entity.TblContractApprovalTrack();
        step1.setContractId(record.getContractId());
        step1.setStepOrder(1);
        step1.setStepName("部门申请");
        step1.setApproverName(record.getCompanyName());
        step1.setApproverDept("业务部门");
        step1.setApprovalAction("APPROVE");
        step1.setApprovalComment("合同发起申请");
        step1.setApprovalTime(record.getCreateTime());
        list.add(step1);

        // 步骤2：法务审核
        com.huabo.cybermonitor.entity.TblContractApprovalTrack step2 = new com.huabo.cybermonitor.entity.TblContractApprovalTrack();
        step2.setContractId(record.getContractId());
        step2.setStepOrder(2);
        step2.setStepName("法务审核");
        step2.setApproverDept("法务部");
        if ("1".equals(record.getHasLegalReview())) {
            step2.setApprovalAction("APPROVE");
            step2.setApprovalComment("法务审核通过");
        } else {
            step2.setApprovalAction("PENDING");
            step2.setApprovalComment("待法务审核");
        }
        list.add(step2);

        // 步骤3：财务审核
        com.huabo.cybermonitor.entity.TblContractApprovalTrack step3 = new com.huabo.cybermonitor.entity.TblContractApprovalTrack();
        step3.setContractId(record.getContractId());
        step3.setStepOrder(3);
        step3.setStepName("财务审核");
        step3.setApproverDept("财务部");
        if ("1".equals(record.getHasFinanceReview())) {
            step3.setApprovalAction("APPROVE");
            step3.setApprovalComment("财务审核通过");
        } else {
            step3.setApprovalAction("PENDING");
            step3.setApprovalComment("待财务审核");
        }
        list.add(step3);

        // 步骤4：领导审批
        com.huabo.cybermonitor.entity.TblContractApprovalTrack step4 = new com.huabo.cybermonitor.entity.TblContractApprovalTrack();
        step4.setContractId(record.getContractId());
        step4.setStepOrder(4);
        step4.setStepName("领导审批");
        step4.setApproverName(record.getApprovalLevel());
        step4.setApproverDept("管理层");
        if ("1".equals(record.getHasLegalReview()) && "1".equals(record.getHasFinanceReview())) {
            step4.setApprovalAction("APPROVE");
            step4.setApprovalComment("审批通过");
        } else if ("TERMINATED".equals(record.getContractStatus())) {
            step4.setApprovalAction("REJECT");
            step4.setApprovalComment("审批驳回");
        } else {
            step4.setApprovalAction("PENDING");
            step4.setApprovalComment("待领导审批");
        }
        list.add(step4);

        return list;
    }

    // ==================== 履行监控（performanceMonitor专用） ====================

    @Operation(summary = "履行监控列表 - 支持performStatus筛选")
    @PostMapping("/performance/list")
    public R<PageResult<Map<String, Object>>> performanceList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 10;
            String contractName = params.get("contractName") != null ? params.get("contractName").toString() : null;
            String performStatus = params.get("performStatus") != null ? params.get("performStatus").toString() : null;

            LambdaQueryWrapper<TblContractRecord> w = new LambdaQueryWrapper<>();
            if (StringUtils.isNotBlank(contractName)) {
                w.like(TblContractRecord::getContractName, contractName);
            }
            w.orderByDesc(TblContractRecord::getCreateTime);

            boolean needStatusFilter = StringUtils.isNotBlank(performStatus);

            if (!needStatusFilter) {
                // 无状态筛选，直接数据库分页
                Page<TblContractRecord> page = contractRecordMapper.selectPage(new Page<>(pn, ps), w);
                List<Map<String, Object>> list = new ArrayList<>();
                for (TblContractRecord rec : page.getRecords()) {
                    list.add(buildPerformanceItem(rec));
                }
                PageResult<Map<String, Object>> pr = new PageResult<>();
                pr.setTotalRecord((int) page.getTotal()); pr.setCurrentPage(pn);
                pr.setPageNumber(pn); pr.setTotalPage((int) page.getPages());
                pr.setPageSize(ps); pr.setTlist(list);
                return R.success(pr);
            }

            // 有状态筛选：查全部 → Java层派生状态过滤 → 手动分页
            List<TblContractRecord> allRecords = contractRecordMapper.selectList(w);
            List<Map<String, Object>> filteredList = new ArrayList<>();
            for (TblContractRecord rec : allRecords) {
                Map<String, Object> item = buildPerformanceItem(rec);
                if (performStatus.equals(item.get("performStatus"))) {
                    filteredList.add(item);
                }
            }

            int total = filteredList.size();
            int fromIndex = Math.min((pn - 1) * ps, total);
            int toIndex = Math.min(pn * ps, total);
            PageResult<Map<String, Object>> pr = new PageResult<>();
            pr.setTotalRecord(total); pr.setCurrentPage(pn);
            pr.setPageNumber(pn); pr.setTotalPage(ps > 0 ? (int) Math.ceil((double) total / ps) : 0);
            pr.setPageSize(ps); pr.setTlist(filteredList.subList(fromIndex, toIndex));
            return R.success(pr);
        } catch (Exception e) {
            log.error("查询履行监控列表失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "履行监控详情")
    @GetMapping("/performance/detail/{contractId}")
    public R<Map<String, Object>> performanceDetail(@PathVariable String contractId) {
        try {
            TblContractRecord rec = contractRecordMapper.selectById(contractId);
            if (rec == null) {
                return R.fail("合同不存在");
            }
            Map<String, Object> result = buildPerformanceItem(rec);
            // 补充详情字段
            result.put("counterpartyName", rec.getCounterpartyName());
            result.put("contractType", rec.getContractType());
            result.put("companyName", rec.getCompanyName());
            result.put("signDate", rec.getSignDate() != null ? rec.getSignDate().toString() : "");
            result.put("effectiveDate", rec.getEffectiveDate() != null ? rec.getEffectiveDate().toString() : "");
            result.put("expiryDate", rec.getExpiryDate() != null ? rec.getExpiryDate().toString() : "");
            result.put("receivedAmount", rec.getReceivedAmount() != null ? rec.getReceivedAmount() : 0);
            result.put("planProgress", rec.getPlanProgress() != null ? rec.getPlanProgress() : 0);
            result.put("actualProgress", rec.getActualProgress() != null ? rec.getActualProgress() : 0);
            result.put("changeCount", rec.getChangeCount() != null ? rec.getChangeCount() : 0);
            result.put("contractStatus", rec.getContractStatus());
            return R.success(result);
        } catch (Exception e) {
            log.error("查询履行监控详情失败，contractId：{}", contractId, e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    /**
     * 构建履行监控列表项
     */
    private Map<String, Object> buildPerformanceItem(TblContractRecord rec) {
        Map<String, Object> m = new LinkedHashMap<>();
        m.put("contractId", rec.getContractId());
        m.put("contractNo", rec.getContractCode() != null ? rec.getContractCode() : rec.getContractId());
        m.put("contractName", rec.getContractName());
        m.put("contractAmount", rec.getContractAmount() != null ? rec.getContractAmount() : java.math.BigDecimal.ZERO);
        m.put("paidAmount", rec.getPaidAmount() != null ? rec.getPaidAmount() : java.math.BigDecimal.ZERO);

        // 计算付款进度
        double contractAmt = rec.getContractAmount() != null ? rec.getContractAmount().doubleValue() : 0;
        double paidAmt = rec.getPaidAmount() != null ? rec.getPaidAmount().doubleValue() : 0;
        int paymentProgress = contractAmt > 0 ? (int) Math.round((paidAmt / contractAmt) * 100) : 0;
        m.put("paymentProgress", paymentProgress);

        // 计算逾期天数
        int overdueDays = 0;
        if (rec.getExpiryDate() != null) {
            long diff = java.time.temporal.ChronoUnit.DAYS.between(rec.getExpiryDate(), LocalDate.now());
            if (diff > 0) overdueDays = (int) diff;
        }
        m.put("overdueDays", overdueDays);

        // 派生履行状态
        String performStatus;
        if ("TERMINATED".equals(rec.getContractStatus()) || "BREACH".equals(rec.getContractStatus())) {
            performStatus = "BREACH";
        } else if (overdueDays > 0) {
            performStatus = "OVERDUE";
        } else {
            performStatus = "NORMAL";
        }
        m.put("performStatus", performStatus);

        return m;
    }

    // ==================== 信用分析（creditAnalysis专用） ====================

    @Operation(summary = "信用分析列表 - 支持creditLevel筛选")
    @PostMapping("/credit/list")
    public R<PageResult<Map<String, Object>>> creditList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 10;
            String counterpartyName = params.get("counterpartyName") != null ? params.get("counterpartyName").toString() : null;
            String creditLevel = params.get("creditLevel") != null ? params.get("creditLevel").toString() : null;

            // 查询所有有对方名称的合同记录
            LambdaQueryWrapper<TblContractRecord> w = new LambdaQueryWrapper<>();
            w.isNotNull(TblContractRecord::getCounterpartyName);
            w.ne(TblContractRecord::getCounterpartyName, "");
            if (StringUtils.isNotBlank(counterpartyName)) {
                w.like(TblContractRecord::getCounterpartyName, counterpartyName);
            }
            List<TblContractRecord> allRecords = contractRecordMapper.selectList(w);

            // 查询纠纷记录用于统计
            List<TblContractDispute> allDisputes = contractDisputeMapper.selectList(null);
            Map<String, Integer> disputeCountMap = new HashMap<>();
            for (TblContractDispute d : allDisputes) {
                if (d.getCounterpartyName() != null) {
                    disputeCountMap.merge(d.getCounterpartyName(), 1, Integer::sum);
                }
            }

            // 按对方名称分组统计
            Map<String, Map<String, Object>> cpMap = new LinkedHashMap<>();
            for (TblContractRecord rec : allRecords) {
                String cpName = rec.getCounterpartyName();
                if (cpName == null || cpName.trim().isEmpty()) continue;
                cpMap.computeIfAbsent(cpName, k -> {
                    Map<String, Object> m = new LinkedHashMap<>();
                    m.put("counterpartyName", cpName);
                    m.put("creditCode", rec.getCounterpartyCredit() != null ? rec.getCounterpartyCredit() : "");
                    m.put("contractCount", 0);
                    m.put("totalAmount", BigDecimal.ZERO);
                    m.put("overdueCount", 0);
                    m.put("disputeCount", disputeCountMap.getOrDefault(cpName, 0));
                    m.put("creditLevel", deriveCreditLevel(rec.getCounterpartyCredit()));
                    return m;
                });
                Map<String, Object> entry = cpMap.get(cpName);
                entry.put("contractCount", (int) entry.get("contractCount") + 1);
                BigDecimal cur = rec.getContractAmount() != null ? rec.getContractAmount() : BigDecimal.ZERO;
                entry.put("totalAmount", ((BigDecimal) entry.get("totalAmount")).add(cur));
                // 统计逾期次数
                if (rec.getExpiryDate() != null && rec.getExpiryDate().isBefore(LocalDate.now())
                        && !"COMPLETED".equals(rec.getContractStatus()) && !"TERMINATED".equals(rec.getContractStatus())) {
                    entry.put("overdueCount", (int) entry.get("overdueCount") + 1);
                }
            }

            // 转为列表并按信用等级过滤
            List<Map<String, Object>> resultList = new ArrayList<>();
            for (Map<String, Object> item : cpMap.values()) {
                // 重命名字段
                item.put("cooperationCount", item.get("contractCount"));
                item.put("totalContractAmount", item.get("totalAmount"));
                if (StringUtils.isNotBlank(creditLevel)) {
                    if (!creditLevel.equals(item.get("creditLevel"))) continue;
                }
                resultList.add(item);
            }

            // 手动分页
            int total = resultList.size();
            int fromIndex = Math.min((pn - 1) * ps, total);
            int toIndex = Math.min(pn * ps, total);
            PageResult<Map<String, Object>> pr = new PageResult<>();
            pr.setTotalRecord(total); pr.setCurrentPage(pn);
            pr.setPageNumber(pn); pr.setTotalPage(ps > 0 ? (int) Math.ceil((double) total / ps) : 0);
            pr.setPageSize(ps); pr.setTlist(resultList.subList(fromIndex, toIndex));
            return R.success(pr);
        } catch (Exception e) {
            log.error("查询信用分析列表失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "信用分析详情")
    @GetMapping("/credit/detail")
    public R<Map<String, Object>> creditDetail(@RequestParam String name) {
        try {
            LambdaQueryWrapper<TblContractRecord> w = new LambdaQueryWrapper<>();
            w.eq(TblContractRecord::getCounterpartyName, name);
            List<TblContractRecord> recs = contractRecordMapper.selectList(w);

            Map<String, Object> result = new LinkedHashMap<>();
            result.put("counterpartyName", name);
            result.put("contractCount", recs.size());
            BigDecimal totalAmount = recs.stream()
                    .map(r -> r.getContractAmount() != null ? r.getContractAmount() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            result.put("totalContractAmount", totalAmount);

            // 信用信息
            String creditCode = "";
            if (!recs.isEmpty() && recs.get(0).getCounterpartyCredit() != null) {
                creditCode = recs.get(0).getCounterpartyCredit();
            }
            result.put("creditCode", creditCode);
            result.put("creditLevel", deriveCreditLevel(creditCode));

            // 逾期统计
            int overdueCount = 0;
            for (TblContractRecord rec : recs) {
                if (rec.getExpiryDate() != null && rec.getExpiryDate().isBefore(LocalDate.now())
                        && !"COMPLETED".equals(rec.getContractStatus()) && !"TERMINATED".equals(rec.getContractStatus())) {
                    overdueCount++;
                }
            }
            result.put("overdueCount", overdueCount);

            // 纠纷统计
            long disputeCount = contractDisputeMapper.selectCount(
                    new LambdaQueryWrapper<TblContractDispute>().eq(TblContractDispute::getCounterpartyName, name));
            result.put("disputeCount", disputeCount);

            // 合同列表
            List<Map<String, Object>> contracts = new ArrayList<>();
            for (TblContractRecord rec : recs) {
                Map<String, Object> c = new LinkedHashMap<>();
                c.put("contractNo", rec.getContractCode());
                c.put("contractName", rec.getContractName());
                c.put("contractAmount", rec.getContractAmount());
                c.put("contractType", rec.getContractType());
                c.put("signDate", rec.getSignDate() != null ? rec.getSignDate().toString() : "");
                c.put("contractStatus", rec.getContractStatus());
                contracts.add(c);
            }
            result.put("contracts", contracts);

            return R.success(result);
        } catch (Exception e) {
            log.error("查询信用分析详情失败，name：{}", name, e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    /**
     * 根据信用代码派生信用等级
     */
    private String deriveCreditLevel(String creditCode) {
        if (creditCode == null || creditCode.trim().isEmpty()) {
            return "A";
        }
        if (creditCode.contains("失信")) {
            return "BB";
        }
        if (creditCode.contains("预警")) {
            return "BBB";
        }
        if (creditCode.contains("异常")) {
            return "BB";
        }
        // 有正常信用代码的视为优质
        return "AAA";
    }

    private <T> PageResult<T> buildEmptyPageResult(Map<String, Object> params) {
        int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
        int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
        PageResult<T> pr = new PageResult<>();
        pr.setTotalRecord(0); pr.setCurrentPage(pn); pr.setPageNumber(pn);
        pr.setTotalPage(0); pr.setPageSize(ps); pr.setTlist(new ArrayList<>());
        return pr;
    }

    // ==================== 数据初始化（开发环境专用） ====================

    @Operation(summary = "初始化合同测试数据（开发环境专用，会清空现有数据）")
    @PostMapping("/dev/seed-data")
    public R<String> seedTestData() {
        try {
            // 1. 清空旧数据
            contractRecordMapper.delete(null);
            log.info("已清空 TBL_CONTRACT_RECORD 表数据");

            // 2. 插入20条测试数据
            List<TblContractRecord> records = buildSeedRecords();
            for (TblContractRecord rec : records) {
                contractRecordMapper.insert(rec);
            }
            log.info("已插入 {} 条测试数据", records.size());
            return R.success("成功初始化 " + records.size() + " 条合同测试数据");
        } catch (Exception e) {
            log.error("初始化测试数据失败", e);
            return R.fail("初始化失败：" + e.getMessage());
        }
    }

    private List<TblContractRecord> buildSeedRecords() {
        List<TblContractRecord> list = new ArrayList<>();
        // GOOD + PURCHASE
        list.add(buildRecord("con-001", "comp-01", "华博云科技集团", "CON-20250101-0001", "服务器采购合同", "PURCHASE", 2680, "联想集团有限公司", null, "2025-01-15", "2025-12-31", "1", "1", "LEVEL3", 1200, 0, 80, 65, 0, "0", "EXECUTING", "INTERNAL", "2025-01-15 10:00:00"));
        // GOOD + SALES
        list.add(buildRecord("con-002", "comp-01", "华博云科技集团", "CON-20250102-0002", "产品销售框架协议", "SALES", 8500, "中国移动通信集团", null, "2025-02-01", "2026-02-09", "1", "1", "LEVEL4", 3200, 5100, 60, 55, 0, "1", "EXECUTING", "INTERNAL", "2025-02-01 09:30:00"));
        // GOOD + ENGINEERING
        list.add(buildRecord("con-003", "comp-02", "华博云能源子公司", "CON-20250103-0003", "厂房扩建工程合同", "ENGINEERING", 15600, "中国建筑第三工程局", null, "2025-03-01", "2026-09-30", "1", "1", "LEVEL5", 6000, 0, 40, 38, 1, "1", "EXECUTING", "INTERNAL", "2025-03-01 14:00:00"));
        // GOOD + SERVICE
        list.add(buildRecord("con-004", "comp-01", "华博云科技集团", "CON-20250104-0004", "IT运维服务合同", "SERVICE", 960, "神州数码信息技术", null, "2025-01-10", "2025-12-31", "1", "1", "LEVEL2", 480, 0, 50, 50, 0, "0", "EXECUTING", "INTERNAL", "2025-01-10 11:00:00"));
        // GOOD + OTHER
        list.add(buildRecord("con-005", "comp-03", "华博云金融子公司", "CON-20250105-0005", "战略合作框架协议", "OTHER", 50000, "中国工商银行股份有限公司", null, "2025-04-01", "2028-04-09", "1", "1", "LEVEL5", 0, 0, 20, 20, 0, "1", "EXECUTING", "INTERNAL", "2025-04-01 08:00:00"));
        // GOOD + PURCHASE
        list.add(buildRecord("con-006", "comp-01", "华博云科技集团", "CON-20250106-0006", "云计算平台采购", "PURCHASE", 3200, "阿里云计算有限公司", null, "2025-02-20", "2026-02-28", "1", "1", "LEVEL3", 3200, 0, 100, 100, 0, "0", "COMPLETED", "INTERNAL", "2025-02-20 15:00:00"));
        // GOOD + SALES
        list.add(buildRecord("con-007", "comp-02", "华博云能源子公司", "CON-20250107-0007", "光伏设备销售合同", "SALES", 4500, "隆基绿能科技股份", null, "2025-03-10", "2025-09-30", "1", "1", "LEVEL3", 0, 2200, 50, 45, 0, "0", "EXECUTING", "EXTERNAL", "2025-03-10 10:30:00"));
        // GOOD + SERVICE
        list.add(buildRecord("con-008", "comp-01", "华博云科技集团", "CON-20250108-0008", "安全咨询服务合同", "SERVICE", 580, "奇安信科技集团", null, "2025-04-05", "2025-10-09", "1", "1", "LEVEL2", 290, 0, 50, 48, 0, "0", "EXECUTING", "INTERNAL", "2025-04-05 09:00:00"));
        // MINOR + PURCHASE
        list.add(buildRecord("con-009", "comp-02", "华博云能源子公司", "CON-20250109-0009", "电气设备采购合同", "PURCHASE", 1850, "正泰电器股份有限公司", "工商异常：经营地址变更未备案", "2025-02-15", "2025-08-19", "1", "1", "LEVEL3", 925, 0, 60, 55, 0, "0", "EXECUTING", "INTERNAL", "2025-02-15 13:00:00"));
        // MINOR + ENGINEERING
        list.add(buildRecord("con-010", "comp-03", "华博云金融子公司", "CON-20250110-0010", "办公楼装修工程", "ENGINEERING", 2200, "金螳螂建筑装饰股份", "工商异常：年报信息公示逾期", "2025-03-20", "2025-09-30", "1", "0", "LEVEL3", 800, 0, 40, 30, 1, "0", "EXECUTING", "INTERNAL", "2025-03-20 16:00:00"));
        // WARNING + ENGINEERING
        list.add(buildRecord("con-011", "comp-01", "华博云科技集团", "CON-20250111-0011", "数据中心建设合同", "ENGINEERING", 32000, "中国电力建设集团", "信用预警：仲裁案件在审，存在纠纷风险", "2025-01-20", "2026-06-30", "1", "1", "LEVEL5", 12000, 0, 30, 22, 2, "1", "EXECUTING", "INTERNAL", "2025-01-20 10:00:00"));
        // WARNING + SERVICE
        list.add(buildRecord("con-012", "comp-02", "华博云能源子公司", "CON-20250112-0012", "物流运输服务合同", "SERVICE", 1200, "上海城运物流集团", "信用预警：合同付款逾期超60天，已出现违约信号", "2025-03-05", "2025-09-09", "1", "1", "LEVEL2", 600, 0, 50, 35, 0, "0", "EXECUTING", "EXTERNAL", "2025-03-05 11:30:00"));
        // WARNING + SALES
        list.add(buildRecord("con-013", "comp-01", "华博云科技集团", "CON-20250113-0013", "原材料销售合同", "SALES", 6800, "宝武钢铁集团有限公司", "信用预警：近期信用评级下调至BBB", "2025-04-10", "2025-10-14", "1", "1", "LEVEL4", 0, 3400, 50, 40, 0, "1", "EXECUTING", "INTERNAL", "2025-04-10 14:00:00"));
        // BLACKLIST + ENGINEERING
        list.add(buildRecord("con-014", "comp-02", "华博云能源子公司", "CON-20250114-0014", "电力施工承包合同", "ENGINEERING", 9800, "恒大建设工程有限公司", "严重失信：已被列入失信被执行人名单，多起诉讼未结", "2024-11-01", "2025-05-14", "0", "0", "LEVEL4", 4900, 0, 50, 20, 3, "1", "EXECUTING", "INTERNAL", "2024-11-01 09:00:00"));
        // BLACKLIST + SERVICE
        list.add(buildRecord("con-015", "comp-03", "华博云金融子公司", "CON-20250115-0015", "软件开发外包合同", "SERVICE", 3600, "某科技外包公司", "严重失信：法人代表被限制高消费，公司账户被冻结", "2024-12-01", "2025-06-09", "1", "0", "LEVEL3", 1800, 0, 50, 15, 0, "0", "EXECUTING", "EXTERNAL", "2024-12-01 10:00:00"));
        // BLACKLIST + PURCHASE
        list.add(buildRecord("con-016", "comp-01", "华博云科技集团", "CON-20250116-0016", "办公用品采购合同", "PURCHASE", 120, "某贸易有限公司", "严重失信：涉嫌合同诈骗，公安机关已立案侦查", "2024-10-15", "2025-04-19", "0", "0", "LEVEL1", 60, 0, 50, 10, 0, "0", "TERMINATED", "INTERNAL", "2024-10-15 15:00:00"));
        // GOOD + SERVICE
        list.add(buildRecord("con-017", "comp-01", "华博云科技集团", "CON-20250117-0017", "年度审计服务合同", "SERVICE", 280, "普华永道中天会计师事务所", null, "2025-01-05", "2025-12-31", "1", "1", "LEVEL2", 140, 0, 50, 50, 0, "0", "EXECUTING", "INTERNAL", "2025-01-05 09:00:00"));
        // GOOD + SALES
        list.add(buildRecord("con-018", "comp-02", "华博云能源子公司", "CON-20250118-0018", "新能源设备销售", "SALES", 7200, "明阳智慧能源集团", null, "2025-04-15", "2026-04-19", "1", "1", "LEVEL4", 0, 3600, 30, 28, 0, "1", "EXECUTING", "INTERNAL", "2025-04-15 10:00:00"));
        // GOOD + OTHER
        list.add(buildRecord("con-019", "comp-03", "华博云金融子公司", "CON-20250119-0019", "投资咨询顾问合同", "OTHER", 1500, "中金公司", null, "2025-03-25", "2025-09-30", "1", "1", "LEVEL3", 750, 0, 50, 50, 0, "0", "EXECUTING", "INTERNAL", "2025-03-25 14:30:00"));
        // GOOD + OTHER
        list.add(buildRecord("con-020", "comp-01", "华博云科技集团", "CON-20250120-0020", "园区物业管理合同", "OTHER", 360, "万科物业服务有限公司", null, "2025-01-01", "2025-12-31", "1", "1", "LEVEL2", 180, 0, 50, 50, 0, "0", "EXECUTING", "INTERNAL", "2025-01-01 08:00:00"));
        return list;
    }

    private TblContractRecord buildRecord(String id, String companyId, String companyName, String code, String name,
            String type, double amount, String counterparty, String credit, String signDate, String expiryDate,
            String hasLegal, String hasFinance, String level, double paid, double received,
            int planProg, int actualProg, int changeCount, String isMajor, String status, String source, String createTime) {
        TblContractRecord r = new TblContractRecord();
        r.setContractId(id);
        r.setCompanyId(companyId);
        r.setCompanyName(companyName);
        r.setContractCode(code);
        r.setContractName(name);
        r.setContractType(type);
        r.setContractAmount(BigDecimal.valueOf(amount));
        r.setCounterpartyName(counterparty);
        r.setCounterpartyCredit(credit);
        r.setSignDate(LocalDate.parse(signDate));
        r.setEffectiveDate(LocalDate.parse(signDate).plusDays(5));
        r.setExpiryDate(LocalDate.parse(expiryDate));
        r.setHasLegalReview(hasLegal);
        r.setHasFinanceReview(hasFinance);
        r.setApprovalLevel(level);
        r.setPaidAmount(BigDecimal.valueOf(paid));
        r.setReceivedAmount(BigDecimal.valueOf(received));
        r.setPlanProgress(BigDecimal.valueOf(planProg));
        r.setActualProgress(BigDecimal.valueOf(actualProg));
        r.setChangeCount(changeCount);
        r.setIsMajor(isMajor);
        r.setContractStatus(status);
        r.setDataSource(source);
        r.setCreateTime(LocalDateTime.parse(createTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return r;
    }
}
