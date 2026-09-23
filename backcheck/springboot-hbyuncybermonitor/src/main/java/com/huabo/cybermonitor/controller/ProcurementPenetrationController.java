package com.huabo.cybermonitor.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 采购穿透式监管控制器
 * 覆盖采购监管全部14个页面的接口
 */
@Tag(name = "采购穿透式监管", description = "采购穿透式监管全接口")
@RestController
@RequestMapping("/v1/supervision/procurement")
@Slf4j
public class ProcurementPenetrationController {

    @Autowired private GzctProcurementProjectMapper projectMapper;
    @Autowired private GzctPurchaseRecordMapper purchaseRecordMapper;
    @Autowired private GzctProcurementSupplierMapper supplierMapper;
    @Autowired private GzctRelatedTransactionMapper relatedTransactionMapper;
    @Autowired private GzctBiddingComplianceMapper biddingComplianceMapper;
    @Autowired private GzctContractExecutionMapper contractExecutionMapper;
    @Autowired private GzctProcurementWarningMapper warningMapper;
    @Autowired private GzctBiddingMonitorMapper biddingMonitorMapper;
    @Autowired private GzctPriceBenchmarkMapper priceBenchmarkMapper;
    @Autowired private GzctSupplyChainRiskMapper supplyChainRiskMapper;
    @Autowired private GzctFakeTradeMapper fakeTradeMapper;
    @Autowired private GzctBidderRelationMapper bidderRelationMapper;
    @Autowired private GzctProcurementTreeNodeMapper treeNodeMapper;
    @Autowired private com.huabo.cybermonitor.mapper.TblProcurementProjectMapper procProjectMapper;
    @Autowired private com.huabo.cybermonitor.util.OrgQueryHelper orgQueryHelper;

    // ==================== 工具方法 ====================

    private <T> PageResult<T> buildPageResult(List<T> list, long total, int pageNum, int pageSize) {
        PageResult<T> pr = new PageResult<>();
        pr.setTotalRecord((int) total);
        pr.setCurrentPage(pageNum);
        pr.setPageNumber(pageNum);
        pr.setTotalPage((int) Math.ceil((double) total / pageSize));
        pr.setPageSize(pageSize);
        pr.setTlist(list);
        return pr;
    }

    private int getPageNumber(Map<String, Object> params) {
        return params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
    }

    private int getPageSize(Map<String, Object> params) {
        return params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
    }

    private String getStr(Map<String, Object> params, String key) {
        Object val = params.get(key);
        return val != null && StringUtils.isNotBlank(val.toString()) ? val.toString() : null;
    }

    // ==================== 1. 首页 (Home) ====================

    @Operation(summary = "首页KPI指标")
    @GetMapping("/home/kpi")
    public R<Map<String, Object>> homeKpi() {
        try {
            Map<String, Object> kpi = new HashMap<>();

            // 1. 集团采购总额(亿元)
            List<GzctPurchaseRecord> allRecords = purchaseRecordMapper.selectList(null);
            BigDecimal totalAmount = allRecords.stream()
                .map(GzctPurchaseRecord::getContractAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            // 转换为亿元，保留2位小数
            BigDecimal totalAmountYi = totalAmount.divide(new BigDecimal("100000000"), 2, RoundingMode.HALF_UP);
            kpi.put("totalPurchaseAmount", totalAmountYi);

            // 2. 注册供应商数
            int supplierCount = supplierMapper.selectCount(null).intValue();
            kpi.put("supplierCount", supplierCount);

            // 3. 关联交易占比 - 关联交易数/采购记录总数
            int purchaseCount = allRecords.size();
            int relatedCount = relatedTransactionMapper.selectCount(null).intValue();
            String relatedRatio = "0%";
            if (purchaseCount > 0) {
                double ratio = Math.round(relatedCount * 1000.0 / purchaseCount) / 10.0;
                relatedRatio = ratio + "%";
            }
            kpi.put("relatedTransactionRatio", relatedRatio);

            // 4. 招投标合规率 - 合规记录数/总记录数
            int totalBidding = biddingComplianceMapper.selectCount(null).intValue();
            int compliantBidding = biddingComplianceMapper.selectCount(
                new LambdaQueryWrapper<GzctBiddingCompliance>().eq(GzctBiddingCompliance::getComplianceStatus, "COMPLIANT")
            ).intValue();
            String complianceRate = "0%";
            if (totalBidding > 0) {
                double rate = Math.round(compliantBidding * 1000.0 / totalBidding) / 10.0;
                complianceRate = rate + "%";
            }
            kpi.put("biddingComplianceRate", complianceRate);

            // 5. 活跃预警
            int warningCount = warningMapper.selectCount(
                new LambdaQueryWrapper<GzctProcurementWarning>().eq(GzctProcurementWarning::getStatus, "ACTIVE")
            ).intValue();
            kpi.put("activeAlerts", warningCount);

            // 6. 项目数量
            int projectCount = projectMapper.selectCount(null).intValue();
            kpi.put("projectCount", projectCount);

            return R.success(kpi);
        } catch (Exception e) {
            log.error("获取首页KPI失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    @Operation(summary = "首页组织树")
    @GetMapping("/home/tree")
    public R<List<Map<String, Object>>> homeTree() {
        try {
            List<GzctProcurementTreeNode> allNodes = treeNodeMapper.selectList(
                new LambdaQueryWrapper<GzctProcurementTreeNode>().orderByAsc(GzctProcurementTreeNode::getSortOrder)
            );
            List<Map<String, Object>> tree = buildTree(allNodes, null);
            return R.success(tree);
        } catch (Exception e) {
            log.error("获取组织树失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    @Operation(summary = "首页预警列表")
    @GetMapping("/home/warnings")
    public R<List<GzctProcurementWarning>> homeWarnings() {
        try {
            LambdaQueryWrapper<GzctProcurementWarning> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(GzctProcurementWarning::getStatus, "ACTIVE")
                   .orderByDesc(GzctProcurementWarning::getWarningTime)
                   .last("FETCH FIRST 10 ROWS ONLY");
            List<GzctProcurementWarning> warnings = warningMapper.selectList(wrapper);
            return R.success(warnings);
        } catch (Exception e) {
            log.error("获取首页预警失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    // ==================== 2. 采购项目 (Project) ====================

    @Operation(summary = "采购项目列表")
    @PostMapping("/project/list")
    public R<PageResult<GzctProcurementProject>> projectList(@RequestBody Map<String, Object> params) {
        try {
            int pageNum = getPageNumber(params);
            int pageSize = getPageSize(params);
            LambdaQueryWrapper<GzctProcurementProject> wrapper = new LambdaQueryWrapper<>();
            String projectName = getStr(params, "projectName");
            String procurementMethod = getStr(params, "procurementMethod");
            String projectStatus = getStr(params, "projectStatus");
            if (projectName != null) wrapper.like(GzctProcurementProject::getProjectName, projectName);
            if (procurementMethod != null) wrapper.eq(GzctProcurementProject::getProcurementMethod, procurementMethod);
            if (projectStatus != null) wrapper.eq(GzctProcurementProject::getProjectStatus, projectStatus);
            wrapper.orderByDesc(GzctProcurementProject::getCreateTime);
            Long total = projectMapper.selectCount(wrapper);
            wrapper.last("LIMIT " + pageSize + " OFFSET " + ((pageNum - 1) * pageSize));
            List<GzctProcurementProject> list = projectMapper.selectList(wrapper);
            return R.success(buildPageResult(list, total, pageNum, pageSize));
        } catch (Exception e) {
            log.error("查询采购项目列表失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    @Operation(summary = "采购项目详情")
    @GetMapping("/project/{id}")
    public R<GzctProcurementProject> projectDetail(@PathVariable String id) {
        try {
            return R.success(projectMapper.selectById(id));
        } catch (Exception e) {
            log.error("查询采购项目详情失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    @Operation(summary = "新增采购项目")
    @PostMapping("/project/add")
    public R<Boolean> projectAdd(@RequestBody GzctProcurementProject project) {
        try {
            project.setId(UUID.randomUUID().toString().replace("-", ""));
            project.setCreateTime(LocalDateTime.now());
            projectMapper.insert(project);
            return R.success(true);
        } catch (Exception e) {
            log.error("新增采购项目失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    @Operation(summary = "更新采购项目")
    @PostMapping("/project/update")
    public R<Boolean> projectUpdate(@RequestBody GzctProcurementProject project) {
        try {
            project.setUpdateTime(LocalDateTime.now());
            projectMapper.updateById(project);
            return R.success(true);
        } catch (Exception e) {
            log.error("更新采购项目失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    @Operation(summary = "删除采购项目")
    @DeleteMapping("/project/{id}")
    public R<Boolean> projectDelete(@PathVariable String id) {
        try {
            projectMapper.deleteById(id);
            return R.success(true);
        } catch (Exception e) {
            log.error("删除采购项目失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量删除采购项目")
    @PostMapping("/project/batch/delete")
    public R<Boolean> projectBatchDelete(@RequestBody Map<String, Object> params) {
        try {
            List<String> ids = (List<String>) params.get("ids");
            if (ids != null && !ids.isEmpty()) {
                for (String id : ids) {
                    projectMapper.deleteById(id);
                }
            }
            return R.success(true);
        } catch (Exception e) {
            log.error("批量删除采购项目失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    @Operation(summary = "采购项目统计")
    @GetMapping("/project/statistics")
    public R<Map<String, Object>> projectStatistics(@RequestParam(required = false) String companyId) {
        try {
            final String orgId = companyId;
            String orgPattern = orgQueryHelper.getOrgPathPattern(orgId);
            // 查 TBL_PROCUREMENT_PROJECT（有 ORG_PATH 字段）
            LambdaQueryWrapper<com.huabo.cybermonitor.entity.TblProcurementProject> pw = new LambdaQueryWrapper<>();
            if (orgPattern != null) {
                pw.and(w -> w.like(com.huabo.cybermonitor.entity.TblProcurementProject::getOrgPath, orgPattern)
                    .or(sub -> sub.isNull(com.huabo.cybermonitor.entity.TblProcurementProject::getOrgPath)
                        .eq(com.huabo.cybermonitor.entity.TblProcurementProject::getCompanyId, orgId)));
            }
            List<com.huabo.cybermonitor.entity.TblProcurementProject> all = procProjectMapper.selectList(pw);
            // 供应商数（去重）
            long supplierCount = all.stream().map(com.huabo.cybermonitor.entity.TblProcurementProject::getSupplierName).filter(s -> s != null).distinct().count();
            // 风险预警（关联交易数）
            long warningCount = all.stream().filter(p -> "Y".equals(p.getIsRelatedParty())).count();
            Map<String, Object> result = new HashMap<>();
            result.put("totalProjects", (long) all.size());
            result.put("projectCount", (long) all.size());
            result.put("supplierCount", supplierCount);
            result.put("totalSuppliers", supplierCount);
            result.put("riskWarnings", warningCount);
            result.put("warningCount", warningCount);
            return R.success(result);
        } catch (Exception e) {
            log.error("采购项目统计失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    // ==================== 3. 驾驶舱 (Dashboard) ====================

    @Operation(summary = "驾驶舱总览")
    @GetMapping("/dashboard/overview")
    public R<Map<String, Object>> dashboardOverview() {
        try {
            Map<String, Object> overview = new HashMap<>();
            int supplierCount = supplierMapper.selectCount(null).intValue();
            int activeAlerts = warningMapper.selectCount(
                new LambdaQueryWrapper<GzctProcurementWarning>().eq(GzctProcurementWarning::getStatus, "ACTIVE")
            ).intValue();
            // 如果预警表无数据，用 TBL_PROCUREMENT_PROJECT 的关联交易数兜底
            if (activeAlerts == 0) {
                activeAlerts = procProjectMapper.selectCount(
                    new LambdaQueryWrapper<com.huabo.cybermonitor.entity.TblProcurementProject>().eq(com.huabo.cybermonitor.entity.TblProcurementProject::getIsRelatedParty, "Y")
                ).intValue();
            }

            List<GzctPurchaseRecord> records = purchaseRecordMapper.selectList(null);
            BigDecimal totalAmount = records.stream()
                .map(GzctPurchaseRecord::getContractAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            long relatedCount = records.stream().filter(r -> "Y".equals(r.getIsRelated())).count();
            BigDecimal relatedRatio = records.size() > 0
                ? new BigDecimal(relatedCount * 100).divide(new BigDecimal(records.size()), 1, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

            int totalBidding = biddingComplianceMapper.selectCount(null).intValue();
            int violationCount = biddingComplianceMapper.selectCount(
                new LambdaQueryWrapper<GzctBiddingCompliance>().eq(GzctBiddingCompliance::getComplianceStatus, "VIOLATION")
            ).intValue();
            double complianceRate = totalBidding > 0 ? Math.round((totalBidding - violationCount) * 1000.0 / totalBidding) / 10.0 : 100.0;

            int highRiskCount = supplierMapper.selectCount(
                new LambdaQueryWrapper<GzctProcurementSupplier>().eq(GzctProcurementSupplier::getRiskLevel, "HIGH")
            ).intValue();

            overview.put("totalPurchaseAmount", totalAmount.divide(new BigDecimal(10000), 1, RoundingMode.HALF_UP));
            overview.put("supplierCount", supplierCount);
            overview.put("relatedRatio", relatedRatio + "%");
            overview.put("complianceRate", complianceRate + "%");
            overview.put("highRiskCount", highRiskCount);
            overview.put("activeAlerts", activeAlerts);

            // 采购类型分布
            Map<String, Long> typeMap = records.stream()
                .filter(r -> r.getPurchaseType() != null)
                .collect(Collectors.groupingBy(GzctPurchaseRecord::getPurchaseType, Collectors.counting()));
            List<Map<String, Object>> typeDistribution = new ArrayList<>();
            typeMap.forEach((k, v) -> { Map<String, Object> m = new HashMap<>(); m.put("name", k); m.put("value", v); typeDistribution.add(m); });
            overview.put("typeDistribution", typeDistribution);

            // 招投标方式分布
            Map<String, Long> biddingMap = records.stream()
                .filter(r -> r.getBiddingMethod() != null)
                .collect(Collectors.groupingBy(GzctPurchaseRecord::getBiddingMethod, Collectors.counting()));
            List<Map<String, Object>> biddingDistribution = new ArrayList<>();
            biddingMap.forEach((k, v) -> { Map<String, Object> m = new HashMap<>(); m.put("name", k); m.put("value", v); biddingDistribution.add(m); });
            overview.put("biddingDistribution", biddingDistribution);

            // 供应商集中度TOP10
            List<GzctProcurementSupplier> topSuppliers = supplierMapper.selectList(
                new LambdaQueryWrapper<GzctProcurementSupplier>().orderByDesc(GzctProcurementSupplier::getTotalPurchase).last("FETCH FIRST 10 ROWS ONLY")
            );
            List<Map<String, Object>> supplierConcentration = topSuppliers.stream().map(s -> {
                Map<String, Object> m = new HashMap<>();
                m.put("name", s.getSupplierName());
                m.put("value", s.getTotalPurchase());
                return m;
            }).collect(Collectors.toList());
            overview.put("supplierConcentration", supplierConcentration);

            return R.success(overview);
        } catch (Exception e) {
            log.error("获取驾驶舱总览失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    @Operation(summary = "采购趋势")
    @GetMapping("/dashboard/trend")
    public R<List<Map<String, Object>>> dashboardTrend() {
        try {
            List<GzctPurchaseRecord> records = purchaseRecordMapper.selectList(null);
            Map<String, List<GzctPurchaseRecord>> grouped = records.stream()
                .filter(r -> r.getPurchaseDate() != null)
                .collect(Collectors.groupingBy(r -> r.getPurchaseDate().toString().substring(0, 7)));

            List<Map<String, Object>> trend = grouped.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> {
                    Map<String, Object> item = new HashMap<>();
                    item.put("month", entry.getKey());
                    BigDecimal totalAmount = entry.getValue().stream()
                        .map(GzctPurchaseRecord::getContractAmount)
                        .filter(Objects::nonNull)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                    BigDecimal relatedAmount = entry.getValue().stream()
                        .filter(r -> "Y".equals(r.getIsRelated()))
                        .map(GzctPurchaseRecord::getContractAmount)
                        .filter(Objects::nonNull)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                    BigDecimal compliantAmount = entry.getValue().stream()
                        .filter(r -> "COMPLIANT".equals(r.getComplianceStatus()))
                        .map(GzctPurchaseRecord::getContractAmount)
                        .filter(Objects::nonNull)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                    item.put("totalAmount", totalAmount);
                    item.put("relatedAmount", relatedAmount);
                    item.put("compliantAmount", compliantAmount);
                    return item;
                }).collect(Collectors.toList());
            return R.success(trend);
        } catch (Exception e) {
            log.error("获取采购趋势失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    // ==================== 4. 采购台账 (Purchase Record) ====================

    @Operation(summary = "采购台账列表")
    @PostMapping("/purchase-record/list")
    public R<PageResult<GzctPurchaseRecord>> purchaseRecordList(@RequestBody Map<String, Object> params) {
        try {
            int pageNum = getPageNumber(params);
            int pageSize = getPageSize(params);
            LambdaQueryWrapper<GzctPurchaseRecord> wrapper = new LambdaQueryWrapper<>();
            
            // 查询条件
            String companyName = getStr(params, "companyName");
            String purchaseType = getStr(params, "purchaseType");
            String biddingMethod = getStr(params, "biddingMethod");
            String complianceStatus = getStr(params, "complianceStatus");
            String startDate = getStr(params, "startDate");
            String endDate = getStr(params, "endDate");
            
            // 构建查询条件
            if (companyName != null) {
                wrapper.like(GzctPurchaseRecord::getCompanyName, companyName);
            }
            if (purchaseType != null) {
                wrapper.eq(GzctPurchaseRecord::getPurchaseType, purchaseType);
            }
            if (biddingMethod != null) {
                wrapper.eq(GzctPurchaseRecord::getBiddingMethod, biddingMethod);
            }
            if (complianceStatus != null) {
                wrapper.eq(GzctPurchaseRecord::getComplianceStatus, complianceStatus);
            }
            // 添加日期范围查询
            if (startDate != null) {
                wrapper.ge(GzctPurchaseRecord::getPurchaseDate, LocalDate.parse(startDate));
            }
            if (endDate != null) {
                wrapper.le(GzctPurchaseRecord::getPurchaseDate, LocalDate.parse(endDate));
            }
            
            wrapper.orderByDesc(GzctPurchaseRecord::getCreateTime);
            
            Long total = purchaseRecordMapper.selectCount(wrapper);
            wrapper.last("LIMIT " + pageSize + " OFFSET " + ((pageNum - 1) * pageSize));
            List<GzctPurchaseRecord> list = purchaseRecordMapper.selectList(wrapper);
            
            return R.success(buildPageResult(list, total, pageNum, pageSize));
        } catch (Exception e) {
            log.error("查询采购台账列表失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    @Operation(summary = "采购台账详情")
    @GetMapping("/purchase-record/{id}")
    public R<GzctPurchaseRecord> purchaseRecordDetail(@PathVariable String id) {
        try {
            return R.success(purchaseRecordMapper.selectById(id));
        } catch (Exception e) {
            log.error("查询采购台账详情失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    @Operation(summary = "新增采购台账")
    @PostMapping("/purchase-record/add")
    public R<Boolean> purchaseRecordAdd(@RequestBody GzctPurchaseRecord record) {
        try {
            record.setId(UUID.randomUUID().toString().replace("-", ""));
            record.setCreateTime(LocalDateTime.now());
            purchaseRecordMapper.insert(record);
            return R.success(true);
        } catch (Exception e) {
            log.error("新增采购台账失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    @Operation(summary = "更新采购台账")
    @PostMapping("/purchase-record/update")
    public R<Boolean> purchaseRecordUpdate(@RequestBody GzctPurchaseRecord record) {
        try {
            record.setUpdateTime(LocalDateTime.now());
            purchaseRecordMapper.updateById(record);
            return R.success(true);
        } catch (Exception e) {
            log.error("更新采购台账失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    @Operation(summary = "删除采购台账")
    @DeleteMapping("/purchase-record/{id}")
    public R<Boolean> purchaseRecordDelete(@PathVariable String id) {
        try {
            purchaseRecordMapper.deleteById(id);
            return R.success(true);
        } catch (Exception e) {
            log.error("删除采购台账失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    @Operation(summary = "导出采购台账")
    @PostMapping("/purchase-record/export")
    public R<Boolean> purchaseRecordExport(@RequestBody Map<String, Object> params) {
        return R.success(true);
    }

    // ==================== 5. 供应商 (Supplier) ====================

    @Operation(summary = "供应商列表")
    @PostMapping("/supplier/list")
    public R<PageResult<GzctProcurementSupplier>> supplierList(@RequestBody Map<String, Object> params) {
        try {
            int pageNum = getPageNumber(params);
            int pageSize = getPageSize(params);
            LambdaQueryWrapper<GzctProcurementSupplier> wrapper = new LambdaQueryWrapper<>();
            String supplierName = getStr(params, "supplierName");
            String creditRating = getStr(params, "creditRating");
            String isBlacklisted = getStr(params, "isBlacklisted");
            String qualificationLevel = getStr(params, "qualificationLevel");
            String blacklistStatus = getStr(params, "blacklistStatus");
            String isRelated = getStr(params, "isRelated");
            if (supplierName != null) wrapper.like(GzctProcurementSupplier::getSupplierName, supplierName);
            if (creditRating != null) wrapper.eq(GzctProcurementSupplier::getCreditRating, creditRating);
            if (isBlacklisted != null) wrapper.eq(GzctProcurementSupplier::getIsBlacklisted, isBlacklisted);
            if (qualificationLevel != null) wrapper.eq(GzctProcurementSupplier::getQualificationLevel, qualificationLevel);
            if (blacklistStatus != null) wrapper.eq(GzctProcurementSupplier::getBlacklistStatus, blacklistStatus);
            if (isRelated != null) wrapper.eq(GzctProcurementSupplier::getIsRelated, isRelated);
            wrapper.orderByDesc(GzctProcurementSupplier::getCreateTime);
            Long total = supplierMapper.selectCount(wrapper);
            wrapper.last("LIMIT " + pageSize + " OFFSET " + ((pageNum - 1) * pageSize));
            List<GzctProcurementSupplier> list = supplierMapper.selectList(wrapper);
            return R.success(buildPageResult(list, total, pageNum, pageSize));
        } catch (Exception e) {
            log.error("查询供应商列表失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    @Operation(summary = "供应商详情")
    @GetMapping("/supplier/{id}")
    public R<GzctProcurementSupplier> supplierDetail(@PathVariable String id) {
        try {
            return R.success(supplierMapper.selectById(id));
        } catch (Exception e) {
            log.error("查询供应商详情失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    @Operation(summary = "新增供应商")
    @PostMapping("/supplier/add")
    public R<Boolean> supplierAdd(@RequestBody GzctProcurementSupplier supplier) {
        try {
            supplier.setId(UUID.randomUUID().toString().replace("-", ""));
            supplier.setCreateTime(LocalDateTime.now());
            supplierMapper.insert(supplier);
            return R.success(true);
        } catch (Exception e) {
            log.error("新增供应商失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    @Operation(summary = "更新供应商")
    @PostMapping("/supplier/update")
    public R<Boolean> supplierUpdate(@RequestBody GzctProcurementSupplier supplier) {
        try {
            supplier.setUpdateTime(LocalDateTime.now());
            supplierMapper.updateById(supplier);
            return R.success(true);
        } catch (Exception e) {
            log.error("更新供应商失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    @Operation(summary = "供应商加入黑名单")
    @PostMapping("/supplier/blacklist")
    public R<Boolean> supplierBlacklist(@RequestBody Map<String, Object> params) {
        try {
            String id = getStr(params, "id");
            GzctProcurementSupplier supplier = supplierMapper.selectById(id);
            if (supplier != null) {
                supplier.setBlacklistStatus("BLACKLIST");
                supplier.setUpdateTime(LocalDateTime.now());
                supplierMapper.updateById(supplier);
            }
            return R.success(true);
        } catch (Exception e) {
            log.error("供应商加入黑名单失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    @Operation(summary = "删除供应商")
    @DeleteMapping("/supplier/{id}")
    public R<Boolean> deleteSupplier(@PathVariable String id) {
        try {
            supplierMapper.deleteById(id);
            return R.success(true);
        } catch (Exception e) {
            log.error("删除供应商失败", e);
            return R.fail("删除失败：" + e.getMessage());
        }
    }

    @Operation(summary = "供应商集中度")
    @GetMapping("/supplier/concentration")
    public R<List<GzctProcurementSupplier>> supplierConcentration() {
        try {
            LambdaQueryWrapper<GzctProcurementSupplier> wrapper = new LambdaQueryWrapper<>();
            wrapper.orderByDesc(GzctProcurementSupplier::getTotalPurchase)
                   .last("FETCH FIRST 10 ROWS ONLY");
            List<GzctProcurementSupplier> top10 = supplierMapper.selectList(wrapper);
            return R.success(top10);
        } catch (Exception e) {
            log.error("查询供应商集中度失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    // ==================== 6. 关联交易 (Related Transaction) ====================

    @Operation(summary = "关联交易列表")
    @PostMapping("/related-transaction/list")
    public R<PageResult<GzctRelatedTransaction>> relatedTransactionList(@RequestBody Map<String, Object> params) {
        try {
            int pageNum = getPageNumber(params);
            int pageSize = getPageSize(params);
            LambdaQueryWrapper<GzctRelatedTransaction> wrapper = new LambdaQueryWrapper<>();
            String companyName = getStr(params, "companyName");
            String relationshipType = getStr(params, "relationshipType");
            String isOverLimit = getStr(params, "isOverLimit");
            String isDisclosed = getStr(params, "isDisclosed");
            if (companyName != null) wrapper.like(GzctRelatedTransaction::getCompanyName, companyName);
            if (relationshipType != null) wrapper.eq(GzctRelatedTransaction::getRelationshipType, relationshipType);
            if (isOverLimit != null) wrapper.eq(GzctRelatedTransaction::getIsOverLimit, isOverLimit);
            if (isDisclosed != null) wrapper.eq(GzctRelatedTransaction::getIsDisclosed, isDisclosed);
            wrapper.orderByDesc(GzctRelatedTransaction::getCreateTime);
            Long total = relatedTransactionMapper.selectCount(wrapper);
            wrapper.last("LIMIT " + pageSize + " OFFSET " + ((pageNum - 1) * pageSize));
            List<GzctRelatedTransaction> list = relatedTransactionMapper.selectList(wrapper);
            return R.success(buildPageResult(list, total, pageNum, pageSize));
        } catch (Exception e) {
            log.error("查询关联交易列表失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    @Operation(summary = "关联交易详情")
    @GetMapping("/related-transaction/{id}")
    public R<GzctRelatedTransaction> relatedTransactionDetail(@PathVariable String id) {
        try {
            return R.success(relatedTransactionMapper.selectById(id));
        } catch (Exception e) {
            log.error("查询关联交易详情失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    @Operation(summary = "关联交易统计")
    @GetMapping("/related-transaction/statistics")
    public R<Map<String, Object>> relatedTransactionStatistics() {
        try {
            List<GzctRelatedTransaction> all = relatedTransactionMapper.selectList(null);
            BigDecimal totalAmount = all.stream()
                .map(GzctRelatedTransaction::getTransAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            BigDecimal totalCompanyPurchase = all.stream()
                .map(GzctRelatedTransaction::getCompanyPurchase)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            long overLimitCount = all.stream().filter(t -> "Y".equals(t.getIsOverLimit())).count();
            long undisclosedCount = all.stream().filter(t -> "N".equals(t.getIsDisclosed())).count();

            // 集团关联交易比
            BigDecimal groupRatio = BigDecimal.ZERO;
            if (totalCompanyPurchase.compareTo(BigDecimal.ZERO) > 0) {
                groupRatio = totalAmount.multiply(new BigDecimal(100)).divide(totalCompanyPurchase, 1, RoundingMode.HALF_UP);
            }

            // 各企业关联交易占比
            Map<String, List<GzctRelatedTransaction>> byCompany = all.stream()
                .filter(t -> t.getCompanyName() != null)
                .collect(Collectors.groupingBy(GzctRelatedTransaction::getCompanyName));
            List<Map<String, Object>> companyRatioList = byCompany.entrySet().stream()
                .map(entry -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("companyName", entry.getKey());
                    BigDecimal maxRatio = entry.getValue().stream()
                        .map(GzctRelatedTransaction::getRelatedRatio)
                        .filter(Objects::nonNull)
                        .max(BigDecimal::compareTo)
                        .orElse(BigDecimal.ZERO);
                    m.put("relatedRatio", maxRatio);
                    return m;
                })
                .sorted((a, b) -> ((BigDecimal) b.get("relatedRatio")).compareTo((BigDecimal) a.get("relatedRatio")))
                .collect(Collectors.toList());

            Map<String, Object> stats = new HashMap<>();
            stats.put("totalAmount", totalAmount);
            stats.put("totalCount", all.size());
            stats.put("overLimitCount", overLimitCount);
            stats.put("undisclosedCount", undisclosedCount);
            stats.put("groupRatio", groupRatio);
            stats.put("companyRatioList", companyRatioList);
            return R.success(stats);
        } catch (Exception e) {
            log.error("关联交易统计失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    @Operation(summary = "关联交易披露")
    @PostMapping("/related-transaction/disclosure")
    public R<Boolean> relatedTransactionDisclosure(@RequestBody Map<String, Object> params) {
        try {
            String id = getStr(params, "id");
            GzctRelatedTransaction trans = relatedTransactionMapper.selectById(id);
            if (trans != null) {
                trans.setIsDisclosed("Y");
                trans.setUpdateTime(LocalDateTime.now());
                relatedTransactionMapper.updateById(trans);
            }
            return R.success(true);
        } catch (Exception e) {
            log.error("关联交易披露失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    // ==================== 7. 招投标合规 (Bidding Compliance) ====================

    @Operation(summary = "招投标合规列表")
    @PostMapping("/bidding/list")
    public R<PageResult<GzctBiddingCompliance>> biddingList(@RequestBody Map<String, Object> params) {
        try {
            int pageNum = getPageNumber(params);
            int pageSize = getPageSize(params);
            LambdaQueryWrapper<GzctBiddingCompliance> wrapper = new LambdaQueryWrapper<>();
            String companyName = getStr(params, "companyName");
            String biddingMethod = getStr(params, "biddingMethod");
            String complianceStatus = getStr(params, "complianceStatus");
            if (companyName != null) wrapper.like(GzctBiddingCompliance::getCompanyName, companyName);
            if (biddingMethod != null) wrapper.eq(GzctBiddingCompliance::getBiddingMethod, biddingMethod);
            if (complianceStatus != null) wrapper.eq(GzctBiddingCompliance::getComplianceStatus, complianceStatus);
            wrapper.orderByDesc(GzctBiddingCompliance::getCreateTime);
            Long total = biddingComplianceMapper.selectCount(wrapper);
            wrapper.last("LIMIT " + pageSize + " OFFSET " + ((pageNum - 1) * pageSize));
            List<GzctBiddingCompliance> list = biddingComplianceMapper.selectList(wrapper);
            return R.success(buildPageResult(list, total, pageNum, pageSize));
        } catch (Exception e) {
            log.error("查询招投标合规列表失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    @Operation(summary = "招投标合规详情")
    @GetMapping("/bidding/{id}")
    public R<GzctBiddingCompliance> biddingDetail(@PathVariable String id) {
        try {
            return R.success(biddingComplianceMapper.selectById(id));
        } catch (Exception e) {
            log.error("查询招投标合规详情失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    @Operation(summary = "招投标合规统计")
    @GetMapping("/bidding/statistics")
    public R<Map<String, Object>> biddingStatistics() {
        try {
            List<GzctBiddingCompliance> all = biddingComplianceMapper.selectList(null);
            int totalCount = all.size();
            long openBiddingCount = all.stream().filter(b -> "OPEN_BIDDING".equals(b.getBiddingMethod())).count();
            double openBiddingRate = totalCount > 0 ? Math.round(openBiddingCount * 1000.0 / totalCount) / 10.0 : 0;
            long violationCount = all.stream().filter(b -> "VIOLATION".equals(b.getComplianceStatus())).count();
            BigDecimal violationAmount = all.stream()
                .filter(b -> "VIOLATION".equals(b.getComplianceStatus()))
                .map(GzctBiddingCompliance::getContractAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

            Map<String, Object> stats = new HashMap<>();
            stats.put("totalCount", totalCount);
            stats.put("openBiddingRate", openBiddingRate);
            stats.put("violationCount", violationCount);
            stats.put("violationAmount", violationAmount);

            // 堆叠柱图数据：按企业分组，统计各招标方式数量
            Map<String, List<GzctBiddingCompliance>> byCompany = all.stream()
                .filter(b -> b.getCompanyName() != null)
                .collect(Collectors.groupingBy(GzctBiddingCompliance::getCompanyName));
            List<String> companies = new ArrayList<>(byCompany.keySet());
            List<Integer> openData = new ArrayList<>();
            List<Integer> negoData = new ArrayList<>();
            List<Integer> soleData = new ArrayList<>();
            List<Integer> inquiryData = new ArrayList<>();
            for (String company : companies) {
                List<GzctBiddingCompliance> companyList = byCompany.get(company);
                openData.add((int) companyList.stream().filter(b -> "OPEN_BIDDING".equals(b.getBiddingMethod())).count());
                negoData.add((int) companyList.stream().filter(b -> "NEGOTIATION".equals(b.getBiddingMethod())).count());
                soleData.add((int) companyList.stream().filter(b -> "SINGLE_SOURCE".equals(b.getBiddingMethod())).count());
                inquiryData.add((int) companyList.stream().filter(b -> "INQUIRY".equals(b.getBiddingMethod())).count());
            }
            Map<String, Object> chartData = new HashMap<>();
            chartData.put("companies", companies);
            List<Map<String, Object>> series = new ArrayList<>();
            Map<String, Object> s1 = new HashMap<>(); s1.put("name", "公开招标"); s1.put("data", openData); series.add(s1);
            Map<String, Object> s2 = new HashMap<>(); s2.put("name", "竞争性谈判"); s2.put("data", negoData); series.add(s2);
            Map<String, Object> s3 = new HashMap<>(); s3.put("name", "单一来源"); s3.put("data", soleData); series.add(s3);
            Map<String, Object> s4 = new HashMap<>(); s4.put("name", "询价采购"); s4.put("data", inquiryData); series.add(s4);
            chartData.put("series", series);
            stats.put("chartData", chartData);

            // 雷达图数据：合规率各维度
            long compliantCount = all.stream().filter(b -> "COMPLIANT".equals(b.getComplianceStatus())).count();
            double biddingCompliance = totalCount > 0 ? Math.round(compliantCount * 1000.0 / totalCount) / 10.0 : 0;
            // 从其他表获取关联交易合规率等
            long relatedTotal = relatedTransactionMapper.selectCount(null);
            long relatedOverLimit = relatedTransactionMapper.selectCount(
                new LambdaQueryWrapper<GzctRelatedTransaction>().eq(GzctRelatedTransaction::getIsOverLimit, "Y"));
            double relatedCompliance = relatedTotal > 0 ? Math.round((relatedTotal - relatedOverLimit) * 1000.0 / relatedTotal) / 10.0 : 100;
            long contractTotal = contractExecutionMapper.selectCount(null);
            long contractDefault = contractExecutionMapper.selectCount(
                new LambdaQueryWrapper<GzctContractExecution>().eq(GzctContractExecution::getContractStatus, "DEFAULT"));
            double contractRate = contractTotal > 0 ? Math.round((contractTotal - contractDefault) * 1000.0 / contractTotal) / 10.0 : 100;
            Map<String, Object> radarData = new HashMap<>();
            radarData.put("values", Arrays.asList(biddingCompliance, relatedCompliance, openBiddingRate, contractRate, 95.0));
            stats.put("radarData", radarData);

            return R.success(stats);
        } catch (Exception e) {
            log.error("招投标合规统计失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    @Operation(summary = "举报违规")
    @PostMapping("/bidding/report-violation")
    public R<Boolean> biddingReportViolation(@RequestBody Map<String, Object> params) {
        try {
            String id = getStr(params, "id");
            GzctBiddingCompliance bidding = biddingComplianceMapper.selectById(id);
            if (bidding != null) {
                bidding.setComplianceStatus("VIOLATION");
                bidding.setUpdateTime(LocalDateTime.now());
                biddingComplianceMapper.updateById(bidding);
            }
            return R.success(true);
        } catch (Exception e) {
            log.error("举报违规失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    // ==================== 8. 合同履约 (Contract Execution) ====================

    @Operation(summary = "合同履约列表")
    @PostMapping("/contract/list")
    public R<PageResult<GzctContractExecution>> contractList(@RequestBody Map<String, Object> params) {
        try {
            int pageNum = getPageNumber(params);
            int pageSize = getPageSize(params);
            LambdaQueryWrapper<GzctContractExecution> wrapper = new LambdaQueryWrapper<>();
            String companyName = getStr(params, "companyName");
            String contractStatus = getStr(params, "contractStatus");
            String acceptanceStatus = getStr(params, "acceptanceStatus");
            if (companyName != null) wrapper.like(GzctContractExecution::getCompanyName, companyName);
            if (contractStatus != null) wrapper.eq(GzctContractExecution::getContractStatus, contractStatus);
            if (acceptanceStatus != null) wrapper.eq(GzctContractExecution::getAcceptanceStatus, acceptanceStatus);
            wrapper.orderByDesc(GzctContractExecution::getCreateTime);
            Long total = contractExecutionMapper.selectCount(wrapper);
            wrapper.last("LIMIT " + pageSize + " OFFSET " + ((pageNum - 1) * pageSize));
            List<GzctContractExecution> list = contractExecutionMapper.selectList(wrapper);
            return R.success(buildPageResult(list, total, pageNum, pageSize));
        } catch (Exception e) {
            log.error("查询合同履约列表失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    @Operation(summary = "合同履约详情")
    @GetMapping("/contract/{id}")
    public R<GzctContractExecution> contractDetail(@PathVariable String id) {
        try {
            return R.success(contractExecutionMapper.selectById(id));
        } catch (Exception e) {
            log.error("查询合同履约详情失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    @Operation(summary = "合同付款")
    @PostMapping("/contract/payment")
    public R<Boolean> contractPayment(@RequestBody Map<String, Object> params) {
        try {
            String id = getStr(params, "id");
            GzctContractExecution contract = contractExecutionMapper.selectById(id);
            if (contract != null) {
                Object paidAmountObj = params.get("paidAmount");
                if (paidAmountObj != null) {
                    contract.setPaidAmount(new BigDecimal(paidAmountObj.toString()));
                }
                Object paymentRatioObj = params.get("paymentRatio");
                if (paymentRatioObj != null) {
                    contract.setPaymentRatio(Integer.parseInt(paymentRatioObj.toString()));
                }
                contract.setUpdateTime(LocalDateTime.now());
                contractExecutionMapper.updateById(contract);
            }
            return R.success(true);
        } catch (Exception e) {
            log.error("合同付款失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    @Operation(summary = "合同验收")
    @PostMapping("/contract/acceptance")
    public R<Boolean> contractAcceptance(@RequestBody Map<String, Object> params) {
        try {
            String id = getStr(params, "id");
            String acceptanceStatus = getStr(params, "acceptanceStatus");
            GzctContractExecution contract = contractExecutionMapper.selectById(id);
            if (contract != null) {
                contract.setAcceptanceStatus(acceptanceStatus != null ? acceptanceStatus : "ACCEPTED");
                contract.setUpdateTime(LocalDateTime.now());
                contractExecutionMapper.updateById(contract);
            }
            return R.success(true);
        } catch (Exception e) {
            log.error("合同验收失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    @Operation(summary = "逾期合同")
    @GetMapping("/contract/overdue")
    public R<List<GzctContractExecution>> contractOverdue() {
        try {
            LambdaQueryWrapper<GzctContractExecution> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(GzctContractExecution::getContractStatus, "OVERDUE");
            List<GzctContractExecution> overdueList = contractExecutionMapper.selectList(wrapper);
            return R.success(overdueList);
        } catch (Exception e) {
            log.error("查询逾期合同失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    // ==================== 9. 风险穿透 (Drill Down) ====================

    @Operation(summary = "风险穿透树")
    @PostMapping("/drill-down/tree")
    public R<List<Map<String, Object>>> drillDownTree(@RequestBody Map<String, Object> params) {
        try {
            LambdaQueryWrapper<GzctProcurementTreeNode> wrapper = new LambdaQueryWrapper<>();
            wrapper.orderByAsc(GzctProcurementTreeNode::getSortOrder);
            String depth = getStr(params, "depth");
            if (depth != null && !"all".equalsIgnoreCase(depth)) {
                try {
                    wrapper.le(GzctProcurementTreeNode::getNodeLevel, Integer.parseInt(depth));
                } catch (NumberFormatException ignored) {
                    // depth不是数字时查询全部层级
                }
            }
            List<GzctProcurementTreeNode> allNodes = treeNodeMapper.selectList(wrapper);
            List<Map<String, Object>> tree = buildTree(allNodes, null);
            return R.success(tree);
        } catch (Exception e) {
            log.error("获取风险穿透树失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    @Operation(summary = "风险穿透详情")
    @GetMapping("/drill-down/detail/{nodeId}")
    public R<Map<String, Object>> drillDownDetail(@PathVariable String nodeId) {
        try {
            Map<String, Object> detail = new HashMap<>();
            GzctProcurementTreeNode node = treeNodeMapper.selectById(nodeId);
            detail.put("node", node);
            if (node != null) {
                String nodeName = node.getNodeName();
                // 查询采购记录：用节点名称匹配companyName
                LambdaQueryWrapper<GzctPurchaseRecord> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(GzctPurchaseRecord::getCompanyName, nodeName)
                       .orderByDesc(GzctPurchaseRecord::getCreateTime);
                List<GzctPurchaseRecord> records = purchaseRecordMapper.selectList(wrapper);
                detail.put("purchaseRecords", records);
                // 查询该节点关联的预警数据
                LambdaQueryWrapper<GzctProcurementWarning> warnWrapper = new LambdaQueryWrapper<>();
                warnWrapper.eq(GzctProcurementWarning::getCompanyName, nodeName)
                           .orderByDesc(GzctProcurementWarning::getWarningTime);
                List<GzctProcurementWarning> warnings = warningMapper.selectList(warnWrapper);
                List<Map<String, Object>> warningList = new ArrayList<>();
                for (GzctProcurementWarning w : warnings) {
                    Map<String, Object> wMap = new HashMap<>();
                    wMap.put("id", w.getId());
                    wMap.put("level", w.getWarningLevel());
                    wMap.put("time", w.getWarningTime() != null ? w.getWarningTime().toString().replace("T", " ") : "");
                    wMap.put("title", w.getTitle());
                    wMap.put("desc", w.getContent());
                    warningList.add(wMap);
                }
                detail.put("warnings", warningList);
            }
            return R.success(detail);
        } catch (Exception e) {
            log.error("获取风险穿透详情失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    // ==================== 10. 预警 (Alert/Warning) ====================

    @Operation(summary = "预警列表")
    @PostMapping("/alert/list")
    public R<PageResult<GzctProcurementWarning>> alertList(@RequestBody Map<String, Object> params) {
        try {
            int pageNum = getPageNumber(params);
            int pageSize = getPageSize(params);
            LambdaQueryWrapper<GzctProcurementWarning> wrapper = new LambdaQueryWrapper<>();
            String warningLevel = getStr(params, "warningLevel");
            String status = getStr(params, "status");
            if (warningLevel != null) wrapper.eq(GzctProcurementWarning::getWarningLevel, warningLevel);
            if (status != null) wrapper.eq(GzctProcurementWarning::getStatus, status);
            wrapper.orderByDesc(GzctProcurementWarning::getWarningTime);
            Long total = warningMapper.selectCount(wrapper);
            wrapper.last("LIMIT " + pageSize + " OFFSET " + ((pageNum - 1) * pageSize));
            List<GzctProcurementWarning> list = warningMapper.selectList(wrapper);
            return R.success(buildPageResult(list, total, pageNum, pageSize));
        } catch (Exception e) {
            log.error("查询预警列表失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    @Operation(summary = "关闭预警")
    @PostMapping("/alert/dismiss/{alertId}")
    public R<Boolean> alertDismiss(@PathVariable String alertId) {
        try {
            GzctProcurementWarning warning = warningMapper.selectById(alertId);
            if (warning != null) {
                warning.setStatus("CLOSED");
                warning.setUpdateTime(LocalDateTime.now());
                warningMapper.updateById(warning);
            }
            return R.success(true);
        } catch (Exception e) {
            log.error("关闭预警失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    @Operation(summary = "新增预警")
    @PostMapping("/alert/add")
    public R<Boolean> alertAdd(@RequestBody GzctProcurementWarning warning) {
        try {
            warning.setId(UUID.randomUUID().toString().replace("-", ""));
            warning.setCreateTime(LocalDateTime.now());
            warning.setWarningTime(LocalDateTime.now());
            warningMapper.insert(warning);
            return R.success(true);
        } catch (Exception e) {
            log.error("新增预警失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    @Operation(summary = "处理预警")
    @PostMapping("/alert/handle")
    public R<Boolean> alertHandle(@RequestBody Map<String, Object> params) {
        try {
            String id = getStr(params, "id");
            String handler = getStr(params, "handler");
            String handleResult = getStr(params, "handleResult");
            GzctProcurementWarning warning = warningMapper.selectById(id);
            if (warning != null) {
                warning.setStatus("HANDLED");
                warning.setHandler(handler);
                warning.setHandleTime(LocalDateTime.now());
                warning.setHandleResult(handleResult);
                warning.setUpdateTime(LocalDateTime.now());
                warningMapper.updateById(warning);
            }
            return R.success(true);
        } catch (Exception e) {
            log.error("处理预警失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    // ==================== 11. 招标监控 (Bidding Monitor) ====================

    @Operation(summary = "招标监控列表")
    @PostMapping("/bidding-monitor/list")
    public R<PageResult<GzctBiddingMonitor>> biddingMonitorList(@RequestBody Map<String, Object> params) {
        try {
            int pageNum = getPageNumber(params);
            int pageSize = getPageSize(params);
            LambdaQueryWrapper<GzctBiddingMonitor> wrapper = new LambdaQueryWrapper<>();
            String projectName = getStr(params, "projectName");
            String biddingType = getStr(params, "biddingType");
            String collusionRisk = getStr(params, "collusionRisk");
            if (projectName != null) wrapper.like(GzctBiddingMonitor::getProjectName, projectName);
            if (biddingType != null) wrapper.eq(GzctBiddingMonitor::getBiddingType, biddingType);
            if (collusionRisk != null) wrapper.eq(GzctBiddingMonitor::getCollusionRisk, collusionRisk);
            // 时间范围过滤
            String startDate = getStr(params, "startDate");
            String endDate = getStr(params, "endDate");
            if (startDate != null) {
                wrapper.ge(GzctBiddingMonitor::getBiddingDate, LocalDate.parse(startDate));
            }
            if (endDate != null) {
                wrapper.le(GzctBiddingMonitor::getBiddingDate, LocalDate.parse(endDate));
            }
            wrapper.orderByDesc(GzctBiddingMonitor::getCreateTime);
            Long total = biddingMonitorMapper.selectCount(wrapper);
            wrapper.last("LIMIT " + pageSize + " OFFSET " + ((pageNum - 1) * pageSize));
            List<GzctBiddingMonitor> list = biddingMonitorMapper.selectList(wrapper);
            return R.success(buildPageResult(list, total, pageNum, pageSize));
        } catch (Exception e) {
            log.error("查询招标监控列表失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    @Operation(summary = "招标监控详情")
    @GetMapping("/bidding-monitor/{id}")
    public R<GzctBiddingMonitor> biddingMonitorDetail(@PathVariable String id) {
        try {
            return R.success(biddingMonitorMapper.selectById(id));
        } catch (Exception e) {
            log.error("查询招标监控详情失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    @Operation(summary = "新增招标监控")
    @PostMapping("/bidding-monitor/add")
    public R<Boolean> biddingMonitorAdd(@RequestBody GzctBiddingMonitor monitor) {
        try {
            monitor.setId(UUID.randomUUID().toString().replace("-", ""));
            monitor.setCreateTime(LocalDateTime.now());
            biddingMonitorMapper.insert(monitor);
            return R.success(true);
        } catch (Exception e) {
            log.error("新增招标监控失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    @Operation(summary = "更新招标监控")
    @PostMapping("/bidding-monitor/update")
    public R<Boolean> biddingMonitorUpdate(@RequestBody GzctBiddingMonitor monitor) {
        try {
            monitor.setUpdateTime(LocalDateTime.now());
            biddingMonitorMapper.updateById(monitor);
            return R.success(true);
        } catch (Exception e) {
            log.error("更新招标监控失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    @Operation(summary = "招标监控调度")
    @PostMapping("/bidding-monitor/dispatch")
    public R<Boolean> biddingMonitorDispatch(@RequestBody Map<String, Object> params) {
        return R.success(true);
    }

    @Operation(summary = "投标人关系列表")
    @PostMapping("/bidding-monitor/relation/list")
    public R<PageResult<GzctBidderRelation>> bidderRelationList(@RequestBody Map<String, Object> params) {
        try {
            int pageNum = getPageNumber(params);
            int pageSize = getPageSize(params);
            LambdaQueryWrapper<GzctBidderRelation> wrapper = new LambdaQueryWrapper<>();
            String projectId = getStr(params, "projectId");
            if (projectId != null) wrapper.eq(GzctBidderRelation::getProjectId, projectId);
            wrapper.orderByDesc(GzctBidderRelation::getCreateTime);
            Long total = bidderRelationMapper.selectCount(wrapper);
            wrapper.last("LIMIT " + pageSize + " OFFSET " + ((pageNum - 1) * pageSize));
            List<GzctBidderRelation> list = bidderRelationMapper.selectList(wrapper);
            return R.success(buildPageResult(list, total, pageNum, pageSize));
        } catch (Exception e) {
            log.error("查询投标人关系列表失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    // ==================== 12. 价格对标 (Price Benchmark) ====================

    @Operation(summary = "价格对标列表")
    @PostMapping("/price-benchmark/list")
    public R<PageResult<GzctPriceBenchmark>> priceBenchmarkList(@RequestBody Map<String, Object> params) {
        try {
            int pageNum = getPageNumber(params);
            int pageSize = getPageSize(params);
            LambdaQueryWrapper<GzctPriceBenchmark> wrapper = new LambdaQueryWrapper<>();
            String itemName = getStr(params, "itemName");
            String category = getStr(params, "category");
            String supplierName = getStr(params, "supplierName");
            if (itemName != null) wrapper.like(GzctPriceBenchmark::getItemName, itemName);
            if (category != null) wrapper.eq(GzctPriceBenchmark::getCategory, category);
            if (supplierName != null) wrapper.like(GzctPriceBenchmark::getSupplierName, supplierName);
            wrapper.orderByDesc(GzctPriceBenchmark::getCreateTime);
            Long total = priceBenchmarkMapper.selectCount(wrapper);
            wrapper.last("LIMIT " + pageSize + " OFFSET " + ((pageNum - 1) * pageSize));
            List<GzctPriceBenchmark> list = priceBenchmarkMapper.selectList(wrapper);
            return R.success(buildPageResult(list, total, pageNum, pageSize));
        } catch (Exception e) {
            log.error("查询价格对标列表失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    @Operation(summary = "价格对标详情")
    @GetMapping("/price-benchmark/{id}")
    public R<GzctPriceBenchmark> priceBenchmarkDetail(@PathVariable String id) {
        try {
            return R.success(priceBenchmarkMapper.selectById(id));
        } catch (Exception e) {
            log.error("查询价格对标详情失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    @Operation(summary = "新增价格对标")
    @PostMapping("/price-benchmark/add")
    public R<Boolean> priceBenchmarkAdd(@RequestBody GzctPriceBenchmark benchmark) {
        try {
            benchmark.setId(UUID.randomUUID().toString().replace("-", ""));
            benchmark.setCreateTime(LocalDateTime.now());
            priceBenchmarkMapper.insert(benchmark);
            return R.success(true);
        } catch (Exception e) {
            log.error("新增价格对标失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    @Operation(summary = "更新价格对标")
    @PostMapping("/price-benchmark/update")
    public R<Boolean> priceBenchmarkUpdate(@RequestBody GzctPriceBenchmark benchmark) {
        try {
            benchmark.setUpdateTime(LocalDateTime.now());
            priceBenchmarkMapper.updateById(benchmark);
            return R.success(true);
        } catch (Exception e) {
            log.error("更新价格对标失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    @Operation(summary = "删除价格对标")
    @DeleteMapping("/price-benchmark/{id}")
    public R<Boolean> priceBenchmarkDelete(@PathVariable String id) {
        try {
            priceBenchmarkMapper.deleteById(id);
            return R.success(true);
        } catch (Exception e) {
            log.error("删除价格对标失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    // ==================== 13. 供应链风险 (Supply Chain Risk) ====================

    @Operation(summary = "供应链风险列表")
    @PostMapping("/supply-chain-risk/list")
    public R<PageResult<GzctSupplyChainRisk>> supplyChainRiskList(@RequestBody Map<String, Object> params) {
        try {
            int pageNum = getPageNumber(params);
            int pageSize = getPageSize(params);
            LambdaQueryWrapper<GzctSupplyChainRisk> wrapper = new LambdaQueryWrapper<>();
            String supplierName = getStr(params, "supplierName");
            String riskLevel = getStr(params, "riskLevel");
            if (supplierName != null) wrapper.like(GzctSupplyChainRisk::getSupplierName, supplierName);
            if (riskLevel != null) wrapper.eq(GzctSupplyChainRisk::getRiskLevel, riskLevel);
            wrapper.orderByDesc(GzctSupplyChainRisk::getCreateTime);
            Long total = supplyChainRiskMapper.selectCount(wrapper);
            wrapper.last("LIMIT " + pageSize + " OFFSET " + ((pageNum - 1) * pageSize));
            List<GzctSupplyChainRisk> list = supplyChainRiskMapper.selectList(wrapper);
            return R.success(buildPageResult(list, total, pageNum, pageSize));
        } catch (Exception e) {
            log.error("查询供应链风险列表失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    @Operation(summary = "供应链风险详情")
    @GetMapping("/supply-chain-risk/{id}")
    public R<GzctSupplyChainRisk> supplyChainRiskDetail(@PathVariable String id) {
        try {
            return R.success(supplyChainRiskMapper.selectById(id));
        } catch (Exception e) {
            log.error("查询供应链风险详情失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    @Operation(summary = "新增供应链风险")
    @PostMapping("/supply-chain-risk/add")
    public R<Boolean> supplyChainRiskAdd(@RequestBody GzctSupplyChainRisk risk) {
        try {
            risk.setId(UUID.randomUUID().toString().replace("-", ""));
            risk.setCreateTime(LocalDateTime.now());
            supplyChainRiskMapper.insert(risk);
            return R.success(true);
        } catch (Exception e) {
            log.error("新增供应链风险失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    @Operation(summary = "更新供应链风险")
    @PostMapping("/supply-chain-risk/update")
    public R<Boolean> supplyChainRiskUpdate(@RequestBody GzctSupplyChainRisk risk) {
        try {
            risk.setUpdateTime(LocalDateTime.now());
            supplyChainRiskMapper.updateById(risk);
            return R.success(true);
        } catch (Exception e) {
            log.error("更新供应链风险失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    @Operation(summary = "供应链风险统计")
    @GetMapping("/supply-chain-risk/statistics")
    public R<Map<String, Object>> supplyChainRiskStatistics() {
        try {
            List<GzctSupplyChainRisk> all = supplyChainRiskMapper.selectList(null);
            long highRiskCount = all.stream().filter(r -> "HIGH".equals(r.getRiskLevel())).count();
            long hasAlternativeCount = all.stream().filter(r -> "Y".equals(r.getHasAlternative())).count();
            BigDecimal avgConcentration = BigDecimal.ZERO;
            if (!all.isEmpty()) {
                BigDecimal totalRatio = all.stream()
                    .map(GzctSupplyChainRisk::getPurchaseRatio)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                avgConcentration = totalRatio.divide(new BigDecimal(all.size()), 2, RoundingMode.HALF_UP);
            }
            Map<String, Object> stats = new HashMap<>();
            stats.put("total", all.size());
            stats.put("highRiskCount", highRiskCount);
            stats.put("alternativeCoverage", all.isEmpty() ? 0 : hasAlternativeCount * 100 / all.size());
            stats.put("avgConcentration", avgConcentration);
            return R.success(stats);
        } catch (Exception e) {
            log.error("供应链风险统计失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    @Operation(summary = "删除供应链风险记录")
    @DeleteMapping("/supply-chain-risk/{id}")
    public R<Boolean> deleteSupplyChainRisk(@PathVariable String id) {
        try {
            supplyChainRiskMapper.deleteById(id);
            return R.success(true);
        } catch (Exception e) {
            log.error("删除供应链风险记录失败", e);
            return R.fail("删除失败：" + e.getMessage());
        }
    }

    // ==================== 14. 虚假贸易 (Fake Trade) ====================

    @Operation(summary = "虚假贸易列表")
    @PostMapping("/fake-trade/list")
    public R<PageResult<GzctFakeTrade>> fakeTradeList(@RequestBody Map<String, Object> params) {
        try {
            int pageNum = getPageNumber(params);
            int pageSize = getPageSize(params);
            LambdaQueryWrapper<GzctFakeTrade> wrapper = new LambdaQueryWrapper<>();
            String checkStatus = getStr(params, "checkStatus");
            String fiveFlowStatus = getStr(params, "fiveFlowStatus");
            String tradeName = getStr(params, "tradeName");
            if (checkStatus != null) wrapper.eq(GzctFakeTrade::getCheckStatus, checkStatus);
            if (fiveFlowStatus != null) wrapper.eq(GzctFakeTrade::getFiveFlowStatus, fiveFlowStatus);
            if (tradeName != null) wrapper.like(GzctFakeTrade::getTradeName, tradeName);
            wrapper.orderByDesc(GzctFakeTrade::getCreateTime);
            Long total = fakeTradeMapper.selectCount(wrapper);
            wrapper.last("LIMIT " + pageSize + " OFFSET " + ((pageNum - 1) * pageSize));
            List<GzctFakeTrade> list = fakeTradeMapper.selectList(wrapper);
            return R.success(buildPageResult(list, total, pageNum, pageSize));
        } catch (Exception e) {
            log.error("查询虚假贸易列表失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    @Operation(summary = "虚假贸易详情")
    @GetMapping("/fake-trade/{id}")
    public R<GzctFakeTrade> fakeTradeDetail(@PathVariable String id) {
        try {
            return R.success(fakeTradeMapper.selectById(id));
        } catch (Exception e) {
            log.error("查询虚假贸易详情失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    @Operation(summary = "新增虚假贸易")
    @PostMapping("/fake-trade/add")
    public R<Boolean> fakeTradeAdd(@RequestBody GzctFakeTrade trade) {
        try {
            trade.setId(UUID.randomUUID().toString().replace("-", ""));
            trade.setCreateTime(LocalDateTime.now());
            fakeTradeMapper.insert(trade);
            return R.success(true);
        } catch (Exception e) {
            log.error("新增虚假贸易失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    @Operation(summary = "更新虚假贸易")
    @PostMapping("/fake-trade/update")
    public R<Boolean> fakeTradeUpdate(@RequestBody GzctFakeTrade trade) {
        try {
            trade.setUpdateTime(LocalDateTime.now());
            fakeTradeMapper.updateById(trade);
            return R.success(true);
        } catch (Exception e) {
            log.error("更新虚假贸易失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    @Operation(summary = "删除虚假贸易")
    @DeleteMapping("/fake-trade/{id}")
    public R<Boolean> fakeTradeDelete(@PathVariable String id) {
        try {
            fakeTradeMapper.deleteById(id);
            return R.success(true);
        } catch (Exception e) {
            log.error("删除虚假贸易失败", e);
            return R.fail("操作失败：" + e.getMessage());
        }
    }

    // ==================== 15. 报表导出 (Report Export) ====================

    @Operation(summary = "报表导出")
    @PostMapping("/report/export")
    public R<Boolean> reportExport(@RequestBody Map<String, Object> params) {
        return R.success(true);
    }

    // ==================== 私有工具方法 ====================

    /**
     * 构建树形结构
     */
    private List<Map<String, Object>> buildTree(List<GzctProcurementTreeNode> allNodes, String parentId) {
        List<Map<String, Object>> tree = new ArrayList<>();
        List<GzctProcurementTreeNode> children = allNodes.stream()
            .filter(node -> {
                if (parentId == null) {
                    return node.getParentId() == null || "".equals(node.getParentId()) || "0".equals(node.getParentId());
                }
                return parentId.equals(node.getParentId());
            })
            .collect(Collectors.toList());

        for (GzctProcurementTreeNode child : children) {
            Map<String, Object> nodeMap = new HashMap<>();
            nodeMap.put("id", child.getId());
            nodeMap.put("nodeName", child.getNodeName());
            nodeMap.put("nodeLevel", child.getNodeLevel());
            nodeMap.put("riskLevel", child.getRiskLevel());
            nodeMap.put("purchaseTotal", child.getPurchaseTotal());
            nodeMap.put("relatedRatio", child.getRelatedRatio());
            nodeMap.put("warnCount", child.getWarnCount());
            nodeMap.put("companyId", child.getCompanyId());
            List<Map<String, Object>> subChildren = buildTree(allNodes, child.getId());
            if (!subChildren.isEmpty()) {
                nodeMap.put("children", subChildren);
            }
            tree.add(nodeMap);
        }
        return tree;
    }
}
