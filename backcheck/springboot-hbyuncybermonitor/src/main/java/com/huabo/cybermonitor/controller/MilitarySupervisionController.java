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
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Tag(name = "军品穿透式监管", description = "军品业务穿透式监管全接口")
@RestController
@RequestMapping("/v1/supervision/military")
@Slf4j
public class MilitarySupervisionController {

    @Autowired private GzctMilitaryQualificationMapper qualificationMapper;
    @Autowired private GzctMilitarySupplyChainMapper supplyChainMapper;
    @Autowired private GzctMilitarySubcontractMapper subcontractMapper;
    @Autowired private GzctMilitaryContractMapper contractMapper;
    @Autowired private GzctMilitaryAlertMapper alertMapper;
    @Autowired private GzctMilitaryTaskRecordMapper taskRecordMapper;
    @Autowired private GzctMilitarySecurityMapper securityMapper;
    @Autowired private GzctMilitaryQualityMapper qualityMapper;
    @Autowired private GzctMilitaryAssetMapper assetMapper;
    @Autowired private com.huabo.cybermonitor.mapper.TblMilitaryTaskMapper militaryTaskMapper;
    @Autowired private com.huabo.cybermonitor.util.OrgQueryHelper orgQueryHelper;

    private <T> PageResult<T> buildPageResult(Page<T> r) {
        PageResult<T> pr = new PageResult<>();
        pr.setTotalRecord((int) r.getTotal()); pr.setCurrentPage((int) r.getCurrent());
        pr.setPageNumber((int) r.getCurrent()); pr.setTotalPage((int) r.getPages());
        pr.setPageSize((int) r.getSize()); pr.setTlist(r.getRecords());
        return pr;
    }

    private int getInt(Map<String, Object> params, String key, int def) {
        return params.get(key) != null ? Integer.parseInt(params.get(key).toString()) : def;
    }
    private String getStr(Map<String, Object> params, String key) {
        return params.get(key) != null ? params.get(key).toString() : null;
    }

    // ==================== 首页 ====================

    @Operation(summary = "军品首页KPI")
    @GetMapping("/home/kpi")
    public R<Map<String, Object>> homeKpi() {
        try {
            Map<String, Object> kpi = new HashMap<>();
            kpi.put("totalTasks", taskRecordMapper.selectCount(null));
            kpi.put("activeQualifications", qualificationMapper.selectCount(new LambdaQueryWrapper<GzctMilitaryQualification>().eq(GzctMilitaryQualification::getQualStatus, "VALID")));
            kpi.put("expiringQualifications", qualificationMapper.selectCount(new LambdaQueryWrapper<GzctMilitaryQualification>().eq(GzctMilitaryQualification::getIsExpiring, "1")));
            kpi.put("activeAlerts", alertMapper.selectCount(new LambdaQueryWrapper<GzctMilitaryAlert>().eq(GzctMilitaryAlert::getStatus, "PENDING")));
            long totalQual = qualificationMapper.selectCount(null);
            long validQual = qualificationMapper.selectCount(new LambdaQueryWrapper<GzctMilitaryQualification>().eq(GzctMilitaryQualification::getQualStatus, "VALID"));
            kpi.put("complianceRate", totalQual > 0 ? Math.round(validQual * 1000.0 / totalQual) / 10.0 : 0);
            kpi.put("totalContracts", contractMapper.selectCount(null));
            long foreignCount = supplyChainMapper.selectCount(new LambdaQueryWrapper<GzctMilitarySupplyChain>().eq(GzctMilitarySupplyChain::getIsDomestic, "0"));
            long totalSupply = supplyChainMapper.selectCount(null);
            kpi.put("foreignDependencyRatio", totalSupply > 0 ? Math.round(foreignCount * 1000.0 / totalSupply) / 10.0 : 0);
            return R.success(kpi);
        } catch (Exception e) { log.error("查询首页KPI失败", e); return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "军品首页树")
    @GetMapping("/home/tree")
    public R<List<Map<String, Object>>> homeTree(@RequestParam(required = false) String companyId) {
        try {
            List<GzctMilitaryTaskRecord> records = taskRecordMapper.selectList(new LambdaQueryWrapper<GzctMilitaryTaskRecord>().orderByAsc(GzctMilitaryTaskRecord::getCompanyName));
            Map<String, List<GzctMilitaryTaskRecord>> grouped = records.stream().collect(Collectors.groupingBy(r -> r.getCompanyName() != null ? r.getCompanyName() : "未知"));
            List<Map<String, Object>> tree = new ArrayList<>();
            for (Map.Entry<String, List<GzctMilitaryTaskRecord>> entry : grouped.entrySet()) {
                Map<String, Object> node = new HashMap<>();
                node.put("name", entry.getKey());
                node.put("taskCount", entry.getValue().size());
                long overdueCount = entry.getValue().stream().filter(r -> "OVERDUE".equals(r.getStatus()) || "DELAYED".equals(r.getStatus())).count();
                node.put("overdueCount", overdueCount);
                node.put("riskLevel", overdueCount > 0 ? "HIGH" : "LOW");
                tree.add(node);
            }
            return R.success(tree);
        } catch (Exception e) { log.error("查询首页树失败", e); return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "军品首页预警")
    @GetMapping("/home/warnings")
    public R<List<GzctMilitaryAlert>> homeWarnings() {
        try {
            LambdaQueryWrapper<GzctMilitaryAlert> w = new LambdaQueryWrapper<>();
            w.eq(GzctMilitaryAlert::getStatus, "PENDING").orderByDesc(GzctMilitaryAlert::getCreateTime);
            Page<GzctMilitaryAlert> page = alertMapper.selectPage(new Page<>(1, 10), w);
            return R.success(page.getRecords());
        } catch (Exception e) { log.error("查询首页预警失败", e); return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 驾驶舱 ====================

    @Operation(summary = "军品驾驶舱概览")
    @GetMapping("/dashboard/overview")
    public R<Map<String, Object>> dashboardOverview() {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("totalTasks", taskRecordMapper.selectCount(null));
            result.put("totalContracts", contractMapper.selectCount(null));
            result.put("totalAlerts", alertMapper.selectCount(new LambdaQueryWrapper<GzctMilitaryAlert>().eq(GzctMilitaryAlert::getStatus, "PENDING")));
            result.put("totalQualifications", qualificationMapper.selectCount(new LambdaQueryWrapper<GzctMilitaryQualification>().eq(GzctMilitaryQualification::getQualStatus, "VALID")));
            result.put("overdueContracts", contractMapper.selectCount(new LambdaQueryWrapper<GzctMilitaryContract>().eq(GzctMilitaryContract::getIsOverdue, "1")));
            result.put("highRiskAlerts", alertMapper.selectCount(new LambdaQueryWrapper<GzctMilitaryAlert>().eq(GzctMilitaryAlert::getLevel, "HIGH").eq(GzctMilitaryAlert::getStatus, "PENDING")));
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "军品驾驶舱趋势")
    @GetMapping("/dashboard/trend")
    public R<List<Map<String, Object>>> dashboardTrend(@RequestParam(required = false) String companyId) {
        try {
            List<GzctMilitaryTaskRecord> all = taskRecordMapper.selectList(null);
            Map<String, Long> byType = all.stream().collect(Collectors.groupingBy(r -> r.getTaskType() != null ? r.getTaskType() : "OTHER", Collectors.counting()));
            List<Map<String, Object>> trend = new ArrayList<>();
            byType.forEach((type, count) -> { Map<String, Object> m = new HashMap<>(); m.put("type", type); m.put("count", count); trend.add(m); });
            return R.success(trend);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "军品驾驶舱类型分布")
    @GetMapping("/dashboard/type-distribution")
    public R<List<Map<String, Object>>> dashboardTypeDistribution(@RequestParam(required = false) String companyId) {
        try {
            List<GzctMilitaryTaskRecord> all = taskRecordMapper.selectList(null);
            Map<String, Long> byType = all.stream().collect(Collectors.groupingBy(r -> r.getTaskType() != null ? r.getTaskType() : "OTHER", Collectors.counting()));
            List<Map<String, Object>> result = new ArrayList<>();
            byType.forEach((type, count) -> { Map<String, Object> m = new HashMap<>(); m.put("name", type); m.put("value", count); result.add(m); });
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "军品驾驶舱仪表盘-境外依赖比")
    @GetMapping("/dashboard/gauge")
    public R<Map<String, Object>> dashboardGauge(@RequestParam(required = false) String companyId) {
        try {
            long total = supplyChainMapper.selectCount(null);
            long foreign = supplyChainMapper.selectCount(new LambdaQueryWrapper<GzctMilitarySupplyChain>().eq(GzctMilitarySupplyChain::getIsDomestic, "0"));
            Map<String, Object> r = new HashMap<>();
            r.put("value", total > 0 ? Math.round(foreign * 1000.0 / total) / 10.0 : 0);
            r.put("total", total); r.put("foreign", foreign);
            return R.success(r);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "军品驾驶舱密级分布")
    @GetMapping("/dashboard/secret-distribution")
    public R<List<Map<String, Object>>> dashboardSecretDistribution(@RequestParam(required = false) String companyId) {
        try {
            List<GzctMilitaryTaskRecord> all = taskRecordMapper.selectList(null);
            Map<String, Map<String, Long>> byCompanyAndLevel = all.stream()
                .filter(r -> r.getCompanyName() != null && r.getSecretLevel() != null)
                .collect(Collectors.groupingBy(GzctMilitaryTaskRecord::getCompanyName, Collectors.groupingBy(GzctMilitaryTaskRecord::getSecretLevel, Collectors.counting())));
            List<Map<String, Object>> result = new ArrayList<>();
            byCompanyAndLevel.forEach((company, levels) -> { Map<String, Object> m = new HashMap<>(); m.put("company", company); m.put("levels", levels); result.add(m); });
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "军品任务统计(前端STAT_MAP)")
    @GetMapping("/task/statistics")
    public R<Map<String, Object>> taskStatistics(@RequestParam(required = false) String companyId) {
        try {
            final String orgId = companyId;
            String orgPattern = orgQueryHelper.getOrgPathPattern(orgId);
            // 查 GZCT_MILITARY_TASK（有 ORG_PATH 字段）
            LambdaQueryWrapper<com.huabo.cybermonitor.entity.TblMilitaryTask> tw = new LambdaQueryWrapper<>();
            if (orgPattern != null) {
                tw.and(w -> w.like(com.huabo.cybermonitor.entity.TblMilitaryTask::getOrgPath, orgPattern)
                    .or(sub -> sub.isNull(com.huabo.cybermonitor.entity.TblMilitaryTask::getOrgPath)
                        .eq(com.huabo.cybermonitor.entity.TblMilitaryTask::getCompanyId, orgId)));
            }
            List<com.huabo.cybermonitor.entity.TblMilitaryTask> tasks = militaryTaskMapper.selectList(tw);
            long taskCount = tasks.size();
            // 资质数量（暂用任务中不同密级数）
            long qualificationCount = tasks.stream().map(com.huabo.cybermonitor.entity.TblMilitaryTask::getSecurityLevel).filter(s -> s != null).distinct().count();
            // 风险数量（逾期任务）
            long warningCount = tasks.stream().filter(t -> "OVERDUE".equals(t.getTaskStatus())).count();
            Map<String, Object> result = new HashMap<>();
            result.put("taskCount", taskCount);
            result.put("totalTasks", taskCount);
            result.put("qualificationCount", qualificationCount);
            result.put("totalQualifications", qualificationCount);
            result.put("riskCount", warningCount);
            result.put("warningCount", warningCount);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 任务台账 ====================

    @Operation(summary = "任务台账列表")
    @PostMapping("/task-record/list")
    public R<PageResult<GzctMilitaryTaskRecord>> taskRecordList(@RequestBody Map<String, Object> params) {
        try {
            int pn = getInt(params, "pageNumber", 1); int ps = getInt(params, "pageSize", 15);
            String companyName = getStr(params, "companyName"); String taskType = getStr(params, "taskType");
            String secretLevel = getStr(params, "secretLevel"); String status = getStr(params, "taskStatus");
            LambdaQueryWrapper<GzctMilitaryTaskRecord> w = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(companyName)) w.like(GzctMilitaryTaskRecord::getCompanyName, companyName);
            if (StringUtils.isNotEmpty(taskType)) w.eq(GzctMilitaryTaskRecord::getTaskType, taskType);
            if (StringUtils.isNotEmpty(secretLevel)) w.eq(GzctMilitaryTaskRecord::getSecretLevel, secretLevel);
            if (StringUtils.isNotEmpty(status)) w.eq(GzctMilitaryTaskRecord::getStatus, status);
            w.orderByDesc(GzctMilitaryTaskRecord::getCreateTime);
            Page<GzctMilitaryTaskRecord> r = taskRecordMapper.selectPage(new Page<>(pn, ps), w);
            return R.success(buildPageResult(r));
        } catch (Exception e) { log.error("查询任务台账失败", e); return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "任务台账详情")
    @GetMapping("/task-record/{id}")
    public R<GzctMilitaryTaskRecord> taskRecordDetail(@PathVariable String id) {
        try { return R.success(taskRecordMapper.selectById(id)); } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "新增任务台账")
    @PostMapping("/task-record/add")
    public R<Boolean> addTaskRecord(@RequestBody GzctMilitaryTaskRecord record) {
        try { record.setCreateTime(LocalDateTime.now()); taskRecordMapper.insert(record); return R.success(true); } catch (Exception e) { return R.fail("新增失败：" + e.getMessage()); }
    }

    @Operation(summary = "更新任务台账")
    @PostMapping("/task-record/update")
    public R<Boolean> updateTaskRecord(@RequestBody GzctMilitaryTaskRecord record) {
        try { record.setUpdateTime(LocalDateTime.now()); taskRecordMapper.updateById(record); return R.success(true); } catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); }
    }

    @Operation(summary = "删除任务台账")
    @DeleteMapping("/task-record/{id}")
    public R<Boolean> deleteTaskRecord(@PathVariable String id) {
        try { return R.success(taskRecordMapper.deleteById(id) > 0); } catch (Exception e) { return R.fail("删除失败：" + e.getMessage()); }
    }

    @Operation(summary = "导出任务台账")
    @PostMapping("/task-record/export")
    public R<List<GzctMilitaryTaskRecord>> exportTaskRecords(@RequestBody Map<String, Object> params) {
        try {
            LambdaQueryWrapper<GzctMilitaryTaskRecord> w = new LambdaQueryWrapper<>();
            w.orderByDesc(GzctMilitaryTaskRecord::getCreateTime);
            return R.success(taskRecordMapper.selectList(w));
        } catch (Exception e) { return R.fail("导出失败：" + e.getMessage()); }
    }

    // ==================== 资质档案 ====================

    @Operation(summary = "资质档案列表")
    @PostMapping("/qualification/list")
    public R<PageResult<GzctMilitaryQualification>> qualificationList(@RequestBody Map<String, Object> params) {
        try {
            int pn = getInt(params, "pageNumber", 1); int ps = getInt(params, "pageSize", 15);
            String companyName = getStr(params, "companyName"); String qualType = getStr(params, "qualType");
            String qualStatus = getStr(params, "qualStatus");
            LambdaQueryWrapper<GzctMilitaryQualification> w = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(companyName)) w.like(GzctMilitaryQualification::getCompanyName, companyName);
            if (StringUtils.isNotEmpty(qualType)) w.eq(GzctMilitaryQualification::getQualType, qualType);
            if (StringUtils.isNotEmpty(qualStatus)) w.eq(GzctMilitaryQualification::getQualStatus, qualStatus);
            w.orderByDesc(GzctMilitaryQualification::getCreateTime);
            Page<GzctMilitaryQualification> r = qualificationMapper.selectPage(new Page<>(pn, ps), w);
            return R.success(buildPageResult(r));
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "资质档案详情")
    @GetMapping("/qualification/{id}")
    public R<GzctMilitaryQualification> qualificationDetail(@PathVariable String id) {
        try { return R.success(qualificationMapper.selectById(id)); } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "新增资质档案")
    @PostMapping("/qualification/add")
    public R<Boolean> addQualification(@RequestBody GzctMilitaryQualification record) {
        try { record.setCreateTime(LocalDateTime.now()); qualificationMapper.insert(record); return R.success(true); } catch (Exception e) { return R.fail("新增失败：" + e.getMessage()); }
    }

    @Operation(summary = "更新资质档案")
    @PostMapping("/qualification/update")
    public R<Boolean> updateQualification(@RequestBody GzctMilitaryQualification record) {
        try { record.setUpdateTime(LocalDateTime.now()); qualificationMapper.updateById(record); return R.success(true); } catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); }
    }

    @Operation(summary = "删除资质档案")
    @DeleteMapping("/qualification/{id}")
    public R<Boolean> deleteQualification(@PathVariable String id) {
        try { return R.success(qualificationMapper.deleteById(id) > 0); } catch (Exception e) { return R.fail("删除失败：" + e.getMessage()); }
    }

    @Operation(summary = "即将到期资质")
    @GetMapping("/qualification/expiring")
    public R<List<GzctMilitaryQualification>> expiringQualifications(@RequestParam(required = false, defaultValue = "90") Integer days) {
        try {
            LambdaQueryWrapper<GzctMilitaryQualification> w = new LambdaQueryWrapper<>();
            w.eq(GzctMilitaryQualification::getIsExpiring, "1");
            return R.success(qualificationMapper.selectList(w));
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "资质统计数据")
    @GetMapping("/qualification/statistics")
    public R<Map<String, Object>> qualificationStatistics(@RequestParam(required = false) String companyId) {
        try {
            Map<String, Object> r = new HashMap<>();
            String orgPattern = orgQueryHelper.getOrgPathPattern(companyId);
            LambdaQueryWrapper<GzctMilitaryQualification> baseWrapper = new LambdaQueryWrapper<>();
            if (orgPattern != null) baseWrapper.eq(GzctMilitaryQualification::getCompanyId, companyId);
            r.put("totalCount", qualificationMapper.selectCount(baseWrapper));
            LambdaQueryWrapper<GzctMilitaryQualification> validWrapper = new LambdaQueryWrapper<>();
            validWrapper.eq(GzctMilitaryQualification::getQualStatus, "VALID");
            if (orgPattern != null) validWrapper.eq(GzctMilitaryQualification::getCompanyId, companyId);
            r.put("validCount", qualificationMapper.selectCount(validWrapper));
            LambdaQueryWrapper<GzctMilitaryQualification> expiringWrapper = new LambdaQueryWrapper<>();
            expiringWrapper.eq(GzctMilitaryQualification::getIsExpiring, "1");
            if (orgPattern != null) expiringWrapper.eq(GzctMilitaryQualification::getCompanyId, companyId);
            r.put("expiringCount", qualificationMapper.selectCount(expiringWrapper));
            LambdaQueryWrapper<GzctMilitaryQualification> expiredWrapper = new LambdaQueryWrapper<>();
            expiredWrapper.eq(GzctMilitaryQualification::getQualStatus, "EXPIRED");
            if (orgPattern != null) expiredWrapper.eq(GzctMilitaryQualification::getCompanyId, companyId);
            r.put("expiredCount", qualificationMapper.selectCount(expiredWrapper));
            return R.success(r);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 供应链安全 ====================

    @Operation(summary = "供应链安全列表")
    @PostMapping("/supply-chain/list")
    public R<PageResult<GzctMilitarySupplyChain>> supplyChainList(@RequestBody Map<String, Object> params) {
        try {
            int pn = getInt(params, "pageNumber", 1); int ps = getInt(params, "pageSize", 15);
            String companyName = getStr(params, "companyName"); String isDomestic = getStr(params, "isDomestic");
            String isSingleSource = getStr(params, "isSingleSource"); String materialType = getStr(params, "materialType");
            LambdaQueryWrapper<GzctMilitarySupplyChain> w = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(companyName)) w.like(GzctMilitarySupplyChain::getCompanyName, companyName);
            if (StringUtils.isNotEmpty(isDomestic)) w.eq(GzctMilitarySupplyChain::getIsDomestic, isDomestic);
            if (StringUtils.isNotEmpty(isSingleSource)) w.eq(GzctMilitarySupplyChain::getIsSingleSource, isSingleSource);
            if (StringUtils.isNotEmpty(materialType)) w.like(GzctMilitarySupplyChain::getMaterialType, materialType);
            w.orderByDesc(GzctMilitarySupplyChain::getCreateTime);
            Page<GzctMilitarySupplyChain> r = supplyChainMapper.selectPage(new Page<>(pn, ps), w);
            return R.success(buildPageResult(r));
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "供应链安全详情")
    @GetMapping("/supply-chain/{id}")
    public R<GzctMilitarySupplyChain> supplyChainDetail(@PathVariable String id) {
        try { return R.success(supplyChainMapper.selectById(id)); } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "新增供应链记录")
    @PostMapping("/supply-chain/add")
    public R<Boolean> addSupplyChain(@RequestBody GzctMilitarySupplyChain record) {
        try { record.setCreateTime(LocalDateTime.now()); supplyChainMapper.insert(record); return R.success(true); } catch (Exception e) { return R.fail("新增失败：" + e.getMessage()); }
    }

    @Operation(summary = "更新供应链记录")
    @PostMapping("/supply-chain/update")
    public R<Boolean> updateSupplyChain(@RequestBody GzctMilitarySupplyChain record) {
        try { record.setUpdateTime(LocalDateTime.now()); supplyChainMapper.updateById(record); return R.success(true); } catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); }
    }

    @Operation(summary = "删除供应链记录")
    @DeleteMapping("/supply-chain/{id}")
    public R<Boolean> deleteSupplyChain(@PathVariable String id) {
        try { return R.success(supplyChainMapper.deleteById(id) > 0); } catch (Exception e) { return R.fail("删除失败：" + e.getMessage()); }
    }

    @Operation(summary = "供应链安全统计")
    @GetMapping("/supply-chain/statistics")
    public R<Map<String, Object>> supplyChainStatistics(@RequestParam(required = false) String companyId) {
        try {
            Map<String, Object> r = new HashMap<>();
            String orgPattern = orgQueryHelper.getOrgPathPattern(companyId);
            LambdaQueryWrapper<GzctMilitarySupplyChain> baseWrapper = new LambdaQueryWrapper<>();
            if (orgPattern != null) baseWrapper.eq(GzctMilitarySupplyChain::getCompanyId, companyId);
            r.put("totalSuppliers", supplyChainMapper.selectCount(baseWrapper));
            LambdaQueryWrapper<GzctMilitarySupplyChain> domesticWrapper = new LambdaQueryWrapper<>();
            domesticWrapper.eq(GzctMilitarySupplyChain::getIsDomestic, "1");
            if (orgPattern != null) domesticWrapper.eq(GzctMilitarySupplyChain::getCompanyId, companyId);
            r.put("domesticCount", supplyChainMapper.selectCount(domesticWrapper));
            LambdaQueryWrapper<GzctMilitarySupplyChain> foreignWrapper = new LambdaQueryWrapper<>();
            foreignWrapper.eq(GzctMilitarySupplyChain::getIsDomestic, "0");
            if (orgPattern != null) foreignWrapper.eq(GzctMilitarySupplyChain::getCompanyId, companyId);
            r.put("foreignCount", supplyChainMapper.selectCount(foreignWrapper));
            LambdaQueryWrapper<GzctMilitarySupplyChain> singleSourceWrapper = new LambdaQueryWrapper<>();
            singleSourceWrapper.eq(GzctMilitarySupplyChain::getIsSingleSource, "1");
            if (orgPattern != null) singleSourceWrapper.eq(GzctMilitarySupplyChain::getCompanyId, companyId);
            r.put("singleSourceCount", supplyChainMapper.selectCount(singleSourceWrapper));
            return R.success(r);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "对外依赖列表")
    @GetMapping("/supply-chain/foreign")
    public R<List<GzctMilitarySupplyChain>> foreignDependencyList(@RequestParam(required = false) String companyId) {
        try {
            LambdaQueryWrapper<GzctMilitarySupplyChain> w = new LambdaQueryWrapper<>();
            w.eq(GzctMilitarySupplyChain::getIsDomestic, "0");
            return R.success(supplyChainMapper.selectList(w));
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "单一来源列表")
    @GetMapping("/supply-chain/single-source")
    public R<List<GzctMilitarySupplyChain>> singleSourceList(@RequestParam(required = false) String companyId) {
        try {
            LambdaQueryWrapper<GzctMilitarySupplyChain> w = new LambdaQueryWrapper<>();
            w.eq(GzctMilitarySupplyChain::getIsSingleSource, "1");
            return R.success(supplyChainMapper.selectList(w));
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 分包合规 ====================

    @Operation(summary = "分包合规列表")
    @PostMapping("/subcontract/list")
    public R<PageResult<GzctMilitarySubcontract>> subcontractList(@RequestBody Map<String, Object> params) {
        try {
            int pn = getInt(params, "pageNumber", 1); int ps = getInt(params, "pageSize", 15);
            String companyName = getStr(params, "companyName"); String approvalStatus = getStr(params, "approvalStatus");
            String isCompliant = getStr(params, "isCompliant"); String subcontractType = getStr(params, "subcontractType");
            LambdaQueryWrapper<GzctMilitarySubcontract> w = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(companyName)) w.like(GzctMilitarySubcontract::getCompanyName, companyName);
            if (StringUtils.isNotEmpty(approvalStatus)) w.eq(GzctMilitarySubcontract::getApprovalStatus, approvalStatus);
            if (StringUtils.isNotEmpty(isCompliant)) w.eq(GzctMilitarySubcontract::getIsCompliant, isCompliant);
            if (StringUtils.isNotEmpty(subcontractType)) w.eq(GzctMilitarySubcontract::getSubcontractType, subcontractType);
            w.orderByDesc(GzctMilitarySubcontract::getCreateTime);
            Page<GzctMilitarySubcontract> r = subcontractMapper.selectPage(new Page<>(pn, ps), w);
            return R.success(buildPageResult(r));
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "分包合规详情")
    @GetMapping("/subcontract/{id}")
    public R<GzctMilitarySubcontract> subcontractDetail(@PathVariable String id) {
        try { return R.success(subcontractMapper.selectById(id)); } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "新增分包")
    @PostMapping("/subcontract/add")
    public R<Boolean> addSubcontract(@RequestBody GzctMilitarySubcontract record) {
        try { record.setCreateTime(LocalDateTime.now()); subcontractMapper.insert(record); return R.success(true); } catch (Exception e) { return R.fail("新增失败：" + e.getMessage()); }
    }

    @Operation(summary = "更新分包")
    @PostMapping("/subcontract/update")
    public R<Boolean> updateSubcontract(@RequestBody GzctMilitarySubcontract record) {
        try { record.setUpdateTime(LocalDateTime.now()); subcontractMapper.updateById(record); return R.success(true); } catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); }
    }

    @Operation(summary = "删除分包")
    @DeleteMapping("/subcontract/{id}")
    public R<Boolean> deleteSubcontract(@PathVariable String id) {
        try { return R.success(subcontractMapper.deleteById(id) > 0); } catch (Exception e) { return R.fail("删除失败：" + e.getMessage()); }
    }

    @Operation(summary = "分包合规统计")
    @GetMapping("/subcontract/statistics")
    public R<Map<String, Object>> subcontractStatistics(@RequestParam(required = false) String companyId) {
        try {
            Map<String, Object> r = new HashMap<>();
            r.put("totalCount", subcontractMapper.selectCount(null));
            r.put("compliantCount", subcontractMapper.selectCount(new LambdaQueryWrapper<GzctMilitarySubcontract>().eq(GzctMilitarySubcontract::getIsCompliant, "1")));
            r.put("violationCount", subcontractMapper.selectCount(new LambdaQueryWrapper<GzctMilitarySubcontract>().eq(GzctMilitarySubcontract::getIsCompliant, "0")));
            r.put("pendingCount", subcontractMapper.selectCount(new LambdaQueryWrapper<GzctMilitarySubcontract>().eq(GzctMilitarySubcontract::getApprovalStatus, "PENDING")));
            return R.success(r);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "审批分包")
    @PostMapping("/subcontract/approve")
    public R<Boolean> approveSubcontract(@RequestBody Map<String, Object> params) {
        try {
            String subId = getStr(params, "subId"); String status = getStr(params, "approvalStatus");
            if (StringUtils.isNotEmpty(subId)) {
                GzctMilitarySubcontract s = subcontractMapper.selectById(subId);
                if (s != null) { s.setApprovalStatus(status); s.setUpdateTime(LocalDateTime.now()); subcontractMapper.updateById(s); }
            }
            return R.success(true);
        } catch (Exception e) { return R.fail("审批失败：" + e.getMessage()); }
    }

    @Operation(summary = "分包违规列表")
    @GetMapping("/subcontract/violations")
    public R<List<GzctMilitarySubcontract>> subcontractViolations(@RequestParam(required = false) String companyId) {
        try {
            LambdaQueryWrapper<GzctMilitarySubcontract> w = new LambdaQueryWrapper<>();
            w.eq(GzctMilitarySubcontract::getIsCompliant, "0");
            return R.success(subcontractMapper.selectList(w));
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 合同履约 ====================

    @Operation(summary = "军品合同列表")
    @PostMapping("/contract/list")
    public R<PageResult<GzctMilitaryContract>> contractList(@RequestBody Map<String, Object> params) {
        try {
            int pn = getInt(params, "pageNumber", 1); int ps = getInt(params, "pageSize", 15);
            String companyName = getStr(params, "companyName"); String isOverdue = getStr(params, "isOverdue");
            String acceptanceStatus = getStr(params, "acceptanceStatus");
            LambdaQueryWrapper<GzctMilitaryContract> w = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(companyName)) w.like(GzctMilitaryContract::getCompanyName, companyName);
            if (StringUtils.isNotEmpty(isOverdue)) w.eq(GzctMilitaryContract::getIsOverdue, isOverdue);
            if (StringUtils.isNotEmpty(acceptanceStatus)) w.eq(GzctMilitaryContract::getAcceptanceStatus, acceptanceStatus);
            w.orderByDesc(GzctMilitaryContract::getCreateTime);
            Page<GzctMilitaryContract> r = contractMapper.selectPage(new Page<>(pn, ps), w);
            return R.success(buildPageResult(r));
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "军品合同详情")
    @GetMapping("/contract/{id}")
    public R<GzctMilitaryContract> contractDetail(@PathVariable String id) {
        try { return R.success(contractMapper.selectById(id)); } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "新增合同")
    @PostMapping("/contract/add")
    public R<Boolean> addContract(@RequestBody GzctMilitaryContract record) {
        try { record.setCreateTime(LocalDateTime.now()); contractMapper.insert(record); return R.success(true); } catch (Exception e) { return R.fail("新增失败：" + e.getMessage()); }
    }

    @Operation(summary = "更新合同")
    @PostMapping("/contract/update")
    public R<Boolean> updateContract(@RequestBody GzctMilitaryContract record) {
        try { record.setUpdateTime(LocalDateTime.now()); contractMapper.updateById(record); return R.success(true); } catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); }
    }

    @Operation(summary = "删除合同")
    @DeleteMapping("/contract/{id}")
    public R<Boolean> deleteContract(@PathVariable String id) {
        try { return R.success(contractMapper.deleteById(id) > 0); } catch (Exception e) { return R.fail("删除失败：" + e.getMessage()); }
    }

    @Operation(summary = "更新合同进度")
    @PostMapping("/contract/progress")
    public R<Boolean> updateContractProgress(@RequestBody Map<String, Object> params) {
        try {
            String contractId = getStr(params, "contractId");
            if (StringUtils.isNotEmpty(contractId)) {
                GzctMilitaryContract c = contractMapper.selectById(contractId);
                if (c != null && params.get("progressRate") != null) {
                    c.setProgressRate(new BigDecimal(params.get("progressRate").toString()));
                    c.setUpdateTime(LocalDateTime.now()); contractMapper.updateById(c);
                }
            }
            return R.success(true);
        } catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); }
    }

    @Operation(summary = "更新合同验收")
    @PostMapping("/contract/acceptance")
    public R<Boolean> updateContractAcceptance(@RequestBody Map<String, Object> params) {
        try {
            String contractId = getStr(params, "contractId"); String status = getStr(params, "acceptanceStatus");
            if (StringUtils.isNotEmpty(contractId)) {
                GzctMilitaryContract c = contractMapper.selectById(contractId);
                if (c != null) { c.setAcceptanceStatus(status); c.setUpdateTime(LocalDateTime.now()); contractMapper.updateById(c); }
            }
            return R.success(true);
        } catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); }
    }

    @Operation(summary = "逾期合同列表")
    @GetMapping("/contract/overdue")
    public R<List<GzctMilitaryContract>> overdueContracts(@RequestParam(required = false) String companyId) {
        try {
            LambdaQueryWrapper<GzctMilitaryContract> w = new LambdaQueryWrapper<>();
            w.eq(GzctMilitaryContract::getIsOverdue, "1");
            return R.success(contractMapper.selectList(w));
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "报告合同违约")
    @PostMapping("/contract/breach")
    public R<Boolean> reportBreach(@RequestBody Map<String, Object> params) {
        try {
            String contractId = getStr(params, "contractId"); String breachDesc = getStr(params, "breachDesc");
            if (StringUtils.isNotEmpty(contractId)) {
                GzctMilitaryContract c = contractMapper.selectById(contractId);
                if (c != null) { c.setIsBreach("1"); c.setBreachDesc(breachDesc); c.setUpdateTime(LocalDateTime.now()); contractMapper.updateById(c); }
            }
            return R.success(true);
        } catch (Exception e) { return R.fail("报告失败：" + e.getMessage()); }
    }

    @Operation(summary = "合同统计数据")
    @GetMapping("/contract/statistics")
    public R<Map<String, Object>> contractStatistics(@RequestParam(required = false) String companyId) {
        try {
            Map<String, Object> r = new HashMap<>();
            String orgPattern = orgQueryHelper.getOrgPathPattern(companyId);
            LambdaQueryWrapper<GzctMilitaryContract> baseWrapper = new LambdaQueryWrapper<>();
            if (orgPattern != null) baseWrapper.eq(GzctMilitaryContract::getCompanyId, companyId);
            r.put("totalCount", contractMapper.selectCount(baseWrapper));
            LambdaQueryWrapper<GzctMilitaryContract> overdueWrapper = new LambdaQueryWrapper<>();
            overdueWrapper.eq(GzctMilitaryContract::getIsOverdue, "1");
            if (orgPattern != null) overdueWrapper.eq(GzctMilitaryContract::getCompanyId, companyId);
            r.put("overdueCount", contractMapper.selectCount(overdueWrapper));
            LambdaQueryWrapper<GzctMilitaryContract> completedWrapper = new LambdaQueryWrapper<>();
            completedWrapper.eq(GzctMilitaryContract::getAcceptanceStatus, "ACCEPTED");
            if (orgPattern != null) completedWrapper.eq(GzctMilitaryContract::getCompanyId, companyId);
            r.put("completedCount", contractMapper.selectCount(completedWrapper));
            return R.success(r);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 军品风险穿透 ====================

    @Operation(summary = "军品穿透树（3层结构）")
    @PostMapping("/drill-down/tree")
    public R<Map<String, Object>> drillDownTree(@RequestBody Map<String, Object> params) {
        try {
            List<GzctMilitaryTaskRecord> all = taskRecordMapper.selectList(null);
            // 按 companyId 分组统计，构建带层级的树
            // 层级规则：companyId 前缀区分层级，如 root/c1/c1-1
            // 先按companyId聚合，确保每个企业节点数据完整
            Map<String, List<GzctMilitaryTaskRecord>> byCompanyId = all.stream()
                .filter(r -> r.getCompanyId() != null)
                .collect(Collectors.groupingBy(GzctMilitaryTaskRecord::getCompanyId));
            Map<String, List<GzctMilitaryTaskRecord>> byCompanyName = all.stream()
                .filter(r -> r.getCompanyName() != null)
                .collect(Collectors.groupingBy(GzctMilitaryTaskRecord::getCompanyName));

            // 构建节点辅助方法：通过companyId查询tasks并计算riskLevel
            java.util.function.Function<String, Map<String, Object>> buildNode = companyId -> {
                List<GzctMilitaryTaskRecord> tasks = byCompanyId.getOrDefault(companyId, new ArrayList<>());
                Map<String, Object> node = new HashMap<>();
                // 取第一条记录的companyName
                String cName = tasks.isEmpty() ? companyId : tasks.get(0).getCompanyName();
                node.put("companyId", companyId);
                node.put("name", cName);
                node.put("taskCount", tasks.size());
                long overdueCount = tasks.stream().filter(r -> "OVERDUE".equals(r.getStatus()) || "DELAYED".equals(r.getStatus())).count();
                long totalDone = tasks.stream().filter(r -> "COMPLETED".equals(r.getStatus())).count();
                node.put("completedCount", totalDone);
                double onTimeRate = tasks.isEmpty() ? 0.0 : Math.round(totalDone * 1000.0 / tasks.size()) / 10.0;
                node.put("onTimeRate", onTimeRate + "%");
                node.put("riskLevel", overdueCount >= 3 ? "HIGH" : overdueCount >= 1 ? "MEDIUM" : "LOW");
                node.put("alertCount", 0); // 后续alert查询填充
                return node;
            };

            // 查所有预警，按companyId统计
            List<GzctMilitaryAlert> allAlerts = alertMapper.selectList(
                new LambdaQueryWrapper<GzctMilitaryAlert>().eq(GzctMilitaryAlert::getStatus, "PENDING"));
            Map<String, Long> alertByCompany = allAlerts.stream()
                .filter(a -> a.getCompanyId() != null)
                .collect(Collectors.groupingBy(GzctMilitaryAlert::getCompanyId, Collectors.counting()));

            // 收集所有不重复的 companyId，按层级分组（以'-'数量判断层级）
            // 0个'-'=0层(集团), 1个'-'=1层(一级子), 2个'-'=2层(二级子)
            Set<String> allCompanyIds = byCompanyId.keySet();
            // 根节点：companyId不含'-'或只有一个固定前缀的
            List<String> level0 = allCompanyIds.stream().filter(id -> !id.contains("-") && !"root".equals(id)).sorted().collect(Collectors.toList());
            List<String> level1 = allCompanyIds.stream().filter(id -> id.chars().filter(c -> c == '-').count() == 1).sorted().collect(Collectors.toList());
            List<String> level2 = allCompanyIds.stream().filter(id -> id.chars().filter(c -> c == '-').count() >= 2).sorted().collect(Collectors.toList());

            // 如果没有明显层级区分（所有id都平级），则全部作为一级节点
            if (level0.isEmpty() && level1.isEmpty()) {
                level0 = new ArrayList<>(allCompanyIds);
                level1 = new ArrayList<>();
                level2 = new ArrayList<>();
            }

            // 构建一级节点（含children=二级节点）
            List<Map<String, Object>> children1 = new ArrayList<>();
            for (String c1id : level0) {
                Map<String, Object> c1node = buildNode.apply(c1id);
                c1node.put("alertCount", alertByCompany.getOrDefault(c1id, 0L));
                // 找属于本一级的二级子节点（id以 c1id+'-' 开头）
                List<Map<String, Object>> children2 = new ArrayList<>();
                for (String c2id : level1) {
                    if (c2id.startsWith(c1id + "-")) {
                        Map<String, Object> c2node = buildNode.apply(c2id);
                        c2node.put("alertCount", alertByCompany.getOrDefault(c2id, 0L));
                        // 找三级节点
                        List<Map<String, Object>> children3 = new ArrayList<>();
                        for (String c3id : level2) {
                            if (c3id.startsWith(c2id + "-")) {
                                Map<String, Object> c3node = buildNode.apply(c3id);
                                c3node.put("alertCount", alertByCompany.getOrDefault(c3id, 0L));
                                c3node.put("children", new ArrayList<>());
                                children3.add(c3node);
                            }
                        }
                        c2node.put("children", children3);
                        children2.add(c2node);
                    }
                }
                c1node.put("children", children2);
                children1.add(c1node);
            }

            // 构建根节点
            List<GzctMilitaryTaskRecord> rootTasks = byCompanyId.getOrDefault("root", new ArrayList<>());
            long rootTotal = all.size();
            long rootDone = all.stream().filter(r -> "COMPLETED".equals(r.getStatus())).count();
            long rootOverdue = all.stream().filter(r -> "OVERDUE".equals(r.getStatus()) || "DELAYED".equals(r.getStatus())).count();
            Map<String, Object> root = new HashMap<>();
            root.put("companyId", "root");
            root.put("name", "集团总部");
            root.put("taskCount", rootTotal);
            root.put("completedCount", rootDone);
            root.put("onTimeRate", rootTotal > 0 ? Math.round(rootDone * 1000.0 / rootTotal) / 10.0 + "%" : "0%");
            root.put("riskLevel", rootOverdue >= 5 ? "HIGH" : rootOverdue >= 2 ? "MEDIUM" : "LOW");
            root.put("alertCount", allAlerts.size());
            root.put("children", children1);
            return R.success(root);
        } catch (Exception e) { log.error("查询穿透树失败", e); return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "军品穿透详情")
    @GetMapping("/drill-down/detail/{nodeId}")
    public R<Map<String, Object>> drillDownDetail(@PathVariable String nodeId) {
        try {
            Map<String, Object> detail = new HashMap<>();
            LambdaQueryWrapper<GzctMilitaryTaskRecord> w = new LambdaQueryWrapper<>();
            // root 节点查全部，其他按 companyId 查
            if ("root".equals(nodeId)) {
                // 查全部任务
            } else {
                w.eq(GzctMilitaryTaskRecord::getCompanyId, nodeId);
            }
            w.orderByDesc(GzctMilitaryTaskRecord::getCreateTime);
            List<GzctMilitaryTaskRecord> tasks = taskRecordMapper.selectList(w);
            detail.put("tasks", tasks);
            detail.put("taskCount", tasks.size());
            long completedCount = tasks.stream().filter(r -> "COMPLETED".equals(r.getStatus())).count();
            detail.put("completedCount", completedCount);
            double onTimeRate = tasks.isEmpty() ? 0.0 : Math.round(completedCount * 1000.0 / tasks.size()) / 10.0;
            detail.put("onTimeRate", onTimeRate + "%");
            long overdueCount = tasks.stream().filter(r -> "OVERDUE".equals(r.getStatus()) || "DELAYED".equals(r.getStatus())).count();
            String riskLevel = overdueCount >= 3 ? "HIGH" : overdueCount >= 1 ? "MEDIUM" : "LOW";
            detail.put("riskLevel", riskLevel);
            // 查预警
            LambdaQueryWrapper<GzctMilitaryAlert> aw = new LambdaQueryWrapper<>();
            if (!"root".equals(nodeId)) {
                aw.eq(GzctMilitaryAlert::getCompanyId, nodeId);
            }
            aw.eq(GzctMilitaryAlert::getStatus, "PENDING").orderByDesc(GzctMilitaryAlert::getCreateTime);
            List<GzctMilitaryAlert> alerts = alertMapper.selectList(aw);
            detail.put("alerts", alerts);
            detail.put("alertCount", alerts.size());
            // 查合同金额（关联合同表，按companyId汇总）
            LambdaQueryWrapper<GzctMilitaryContract> cw = new LambdaQueryWrapper<>();
            if (!"root".equals(nodeId)) {
                cw.eq(GzctMilitaryContract::getCompanyId, nodeId);
            }
            List<GzctMilitaryContract> contracts = contractMapper.selectList(cw);
            BigDecimal totalAmount = contracts.stream()
                .filter(c -> c.getContractAmount() != null)
                .map(GzctMilitaryContract::getContractAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            detail.put("contractAmount", totalAmount.setScale(0, java.math.RoundingMode.HALF_UP).toPlainString());
            return R.success(detail);
        } catch (Exception e) { log.error("查询穿透详情失败", e); return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "军品预警列表")
    @PostMapping("/alert/list")
    public R<PageResult<GzctMilitaryAlert>> alertList(@RequestBody Map<String, Object> params) {
        try {
            int pn = getInt(params, "pageNumber", 1); int ps = getInt(params, "pageSize", 15);
            String level = getStr(params, "level"); String status = getStr(params, "status");
            String alertType = getStr(params, "alertType");
            LambdaQueryWrapper<GzctMilitaryAlert> w = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(level)) w.eq(GzctMilitaryAlert::getLevel, level);
            if (StringUtils.isNotEmpty(status)) w.eq(GzctMilitaryAlert::getStatus, status);
            if (StringUtils.isNotEmpty(alertType)) w.eq(GzctMilitaryAlert::getAlertType, alertType);
            w.orderByDesc(GzctMilitaryAlert::getCreateTime);
            Page<GzctMilitaryAlert> r = alertMapper.selectPage(new Page<>(pn, ps), w);
            return R.success(buildPageResult(r));
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "解除军品预警")
    @PostMapping("/alert/dismiss/{alertId}")
    public R<Boolean> dismissAlert(@PathVariable String alertId) {
        try {
            GzctMilitaryAlert a = alertMapper.selectById(alertId);
            if (a != null) { a.setStatus("DISMISSED"); a.setUpdateTime(LocalDateTime.now()); alertMapper.updateById(a); }
            return R.success(true);
        } catch (Exception e) { return R.fail("操作失败：" + e.getMessage()); }
    }

    // ==================== 综合统计 ====================

    @Operation(summary = "军品综合概览")
    @GetMapping("/overview")
    public R<Map<String, Object>> overview() {
        try {
            Map<String, Object> r = new HashMap<>();
            r.put("totalTasks", taskRecordMapper.selectCount(null));
            r.put("totalContracts", contractMapper.selectCount(null));
            r.put("totalAlerts", alertMapper.selectCount(new LambdaQueryWrapper<GzctMilitaryAlert>().eq(GzctMilitaryAlert::getStatus, "PENDING")));
            r.put("totalQualifications", qualificationMapper.selectCount(null));
            r.put("totalSupplyChain", supplyChainMapper.selectCount(null));
            r.put("totalSubcontracts", subcontractMapper.selectCount(null));
            return R.success(r);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "军品风险地图")
    @GetMapping("/risk-map")
    public R<List<Map<String, Object>>> riskMap(@RequestParam(required = false) String companyId) {
        try {
            List<GzctMilitaryAlert> alerts = alertMapper.selectList(new LambdaQueryWrapper<GzctMilitaryAlert>().eq(GzctMilitaryAlert::getStatus, "PENDING"));
            Map<String, Long> byCompany = alerts.stream().filter(a -> a.getCompanyName() != null).collect(Collectors.groupingBy(GzctMilitaryAlert::getCompanyName, Collectors.counting()));
            List<Map<String, Object>> result = new ArrayList<>();
            byCompany.forEach((company, count) -> { Map<String, Object> m = new HashMap<>(); m.put("company", company); m.put("alertCount", count); m.put("riskLevel", count >= 3 ? "HIGH" : count >= 1 ? "MEDIUM" : "LOW"); result.add(m); });
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "密级统计数据")
    @GetMapping("/secret-level/statistics")
    public R<Map<String, Object>> secretLevelStatistics() {
        try {
            Map<String, Object> r = new HashMap<>();
            List<GzctMilitaryTaskRecord> all = taskRecordMapper.selectList(null);
            Map<String, Long> byLevel = all.stream().filter(t -> t.getSecretLevel() != null).collect(Collectors.groupingBy(GzctMilitaryTaskRecord::getSecretLevel, Collectors.counting()));
            r.put("TOP_SECRET", byLevel.getOrDefault("TOP_SECRET", 0L));
            r.put("SECRET", byLevel.getOrDefault("SECRET", 0L));
            r.put("CONFIDENTIAL", byLevel.getOrDefault("CONFIDENTIAL", 0L));
            r.put("INTERNAL", byLevel.getOrDefault("INTERNAL", 0L));
            return R.success(r);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "预警规则列表")
    @GetMapping("/alert/rules")
    public R<List<Map<String, Object>>> alertRules() {
        try { return R.success(new ArrayList<>()); } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "新增预警规则")
    @PostMapping("/alert/rule/add")
    public R<Boolean> addAlertRule(@RequestBody Map<String, Object> params) {
        try { return R.success(true); } catch (Exception e) { return R.fail("新增失败：" + e.getMessage()); }
    }

    @Operation(summary = "更新预警规则")
    @PostMapping("/alert/rule/update")
    public R<Boolean> updateAlertRule(@RequestBody Map<String, Object> params) {
        try { return R.success(true); } catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); }
    }

    // ==================== 保密管理 ====================

    @Operation(summary = "保密管理列表")
    @PostMapping("/security/list")
    public R<PageResult<GzctMilitarySecurity>> securityList(@RequestBody Map<String, Object> params) {
        try {
            int pn = getInt(params, "pageNumber", 1); int ps = getInt(params, "pageSize", 15);
            String companyName = getStr(params, "companyName"); String secretLevel = getStr(params, "secretLevel");
            String checkResult = getStr(params, "checkResult"); String rectificationStatus = getStr(params, "rectificationStatus");
            LambdaQueryWrapper<GzctMilitarySecurity> w = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(companyName)) w.like(GzctMilitarySecurity::getCompanyName, companyName);
            if (StringUtils.isNotEmpty(secretLevel)) w.eq(GzctMilitarySecurity::getSecretLevel, secretLevel);
            if (StringUtils.isNotEmpty(checkResult)) w.eq(GzctMilitarySecurity::getCheckResult, checkResult);
            if (StringUtils.isNotEmpty(rectificationStatus)) w.eq(GzctMilitarySecurity::getRectificationStatus, rectificationStatus);
            w.orderByDesc(GzctMilitarySecurity::getCreateTime);
            Page<GzctMilitarySecurity> r = securityMapper.selectPage(new Page<>(pn, ps), w);
            return R.success(buildPageResult(r));
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "保密管理详情")
    @GetMapping("/security/{id}")
    public R<GzctMilitarySecurity> securityDetail(@PathVariable String id) {
        try { return R.success(securityMapper.selectById(id)); } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "新增保密检查")
    @PostMapping("/security/add")
    public R<Boolean> addSecurity(@RequestBody GzctMilitarySecurity record) {
        try { record.setCreateTime(LocalDateTime.now()); securityMapper.insert(record); return R.success(true); } catch (Exception e) { return R.fail("新增失败：" + e.getMessage()); }
    }

    @Operation(summary = "更新保密检查")
    @PostMapping("/security/update")
    public R<Boolean> updateSecurity(@RequestBody GzctMilitarySecurity record) {
        try { record.setUpdateTime(LocalDateTime.now()); securityMapper.updateById(record); return R.success(true); } catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); }
    }

    @Operation(summary = "删除保密检查")
    @DeleteMapping("/security/{id}")
    public R<Boolean> deleteSecurity(@PathVariable String id) {
        try { return R.success(securityMapper.deleteById(id) > 0); } catch (Exception e) { return R.fail("删除失败：" + e.getMessage()); }
    }

    @Operation(summary = "保密管理统计")
    @GetMapping("/security/statistics")
    public R<Map<String, Object>> securityStatistics() {
        try {
            Map<String, Object> r = new HashMap<>();
            r.put("totalCount", securityMapper.selectCount(null));
            r.put("passCount", securityMapper.selectCount(new LambdaQueryWrapper<GzctMilitarySecurity>().eq(GzctMilitarySecurity::getCheckResult, "PASS")));
            r.put("failCount", securityMapper.selectCount(new LambdaQueryWrapper<GzctMilitarySecurity>().eq(GzctMilitarySecurity::getCheckResult, "FAIL")));
            r.put("warningCount", securityMapper.selectCount(new LambdaQueryWrapper<GzctMilitarySecurity>().eq(GzctMilitarySecurity::getCheckResult, "WARNING")));
            return R.success(r);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 质量管理 ====================

    @Operation(summary = "质量管理列表")
    @PostMapping("/quality/list")
    public R<PageResult<GzctMilitaryQuality>> qualityList(@RequestBody Map<String, Object> params) {
        try {
            int pn = getInt(params, "pageNumber", 1); int ps = getInt(params, "pageSize", 15);
            String companyName = getStr(params, "companyName"); String inspectionResult = getStr(params, "inspectionResult");
            String productName = getStr(params, "productName");
            LambdaQueryWrapper<GzctMilitaryQuality> w = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(companyName)) w.like(GzctMilitaryQuality::getCompanyName, companyName);
            if (StringUtils.isNotEmpty(inspectionResult)) w.eq(GzctMilitaryQuality::getInspectionResult, inspectionResult);
            if (StringUtils.isNotEmpty(productName)) w.like(GzctMilitaryQuality::getProductName, productName);
            w.orderByDesc(GzctMilitaryQuality::getCreateTime);
            Page<GzctMilitaryQuality> r = qualityMapper.selectPage(new Page<>(pn, ps), w);
            return R.success(buildPageResult(r));
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "质量管理详情")
    @GetMapping("/quality/{id}")
    public R<GzctMilitaryQuality> qualityDetail(@PathVariable String id) {
        try { return R.success(qualityMapper.selectById(id)); } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "新增质量检测")
    @PostMapping("/quality/add")
    public R<Boolean> addQuality(@RequestBody GzctMilitaryQuality record) {
        try { record.setCreateTime(LocalDateTime.now()); qualityMapper.insert(record); return R.success(true); } catch (Exception e) { return R.fail("新增失败：" + e.getMessage()); }
    }

    @Operation(summary = "更新质量检测")
    @PostMapping("/quality/update")
    public R<Boolean> updateQuality(@RequestBody GzctMilitaryQuality record) {
        try { record.setUpdateTime(LocalDateTime.now()); qualityMapper.updateById(record); return R.success(true); } catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); }
    }

    @Operation(summary = "删除质量检测")
    @DeleteMapping("/quality/{id}")
    public R<Boolean> deleteQuality(@PathVariable String id) {
        try { return R.success(qualityMapper.deleteById(id) > 0); } catch (Exception e) { return R.fail("删除失败：" + e.getMessage()); }
    }

    @Operation(summary = "质量管理统计")
    @GetMapping("/quality/statistics")
    public R<Map<String, Object>> qualityStatistics() {
        try {
            Map<String, Object> r = new HashMap<>();
            r.put("totalCount", qualityMapper.selectCount(null));
            r.put("qualifiedCount", qualityMapper.selectCount(new LambdaQueryWrapper<GzctMilitaryQuality>().eq(GzctMilitaryQuality::getInspectionResult, "QUALIFIED")));
            r.put("unqualifiedCount", qualityMapper.selectCount(new LambdaQueryWrapper<GzctMilitaryQuality>().eq(GzctMilitaryQuality::getInspectionResult, "UNQUALIFIED")));
            r.put("pendingCount", qualityMapper.selectCount(new LambdaQueryWrapper<GzctMilitaryQuality>().eq(GzctMilitaryQuality::getInspectionResult, "PENDING")));
            return R.success(r);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 资产管理 ====================

    @Operation(summary = "资产管理列表")
    @PostMapping("/asset/list")
    public R<PageResult<GzctMilitaryAsset>> assetList(@RequestBody Map<String, Object> params) {
        try {
            int pn = getInt(params, "pageNumber", 1); int ps = getInt(params, "pageSize", 15);
            String companyName = getStr(params, "companyName"); String assetType = getStr(params, "assetType");
            String assetStatus = getStr(params, "assetStatus");
            LambdaQueryWrapper<GzctMilitaryAsset> w = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(companyName)) w.like(GzctMilitaryAsset::getCompanyName, companyName);
            if (StringUtils.isNotEmpty(assetType)) w.eq(GzctMilitaryAsset::getAssetType, assetType);
            if (StringUtils.isNotEmpty(assetStatus)) w.eq(GzctMilitaryAsset::getAssetStatus, assetStatus);
            w.orderByDesc(GzctMilitaryAsset::getCreateTime);
            Page<GzctMilitaryAsset> r = assetMapper.selectPage(new Page<>(pn, ps), w);
            return R.success(buildPageResult(r));
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "资产管理详情")
    @GetMapping("/asset/{id}")
    public R<GzctMilitaryAsset> assetDetail(@PathVariable String id) {
        try { return R.success(assetMapper.selectById(id)); } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "新增资产")
    @PostMapping("/asset/add")
    public R<Boolean> addAsset(@RequestBody GzctMilitaryAsset record) {
        try { record.setCreateTime(LocalDateTime.now()); assetMapper.insert(record); return R.success(true); } catch (Exception e) { return R.fail("新增失败：" + e.getMessage()); }
    }

    @Operation(summary = "更新资产")
    @PostMapping("/asset/update")
    public R<Boolean> updateAsset(@RequestBody GzctMilitaryAsset record) {
        try { record.setUpdateTime(LocalDateTime.now()); assetMapper.updateById(record); return R.success(true); } catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); }
    }

    @Operation(summary = "删除资产")
    @DeleteMapping("/asset/{id}")
    public R<Boolean> deleteAsset(@PathVariable String id) {
        try { return R.success(assetMapper.deleteById(id) > 0); } catch (Exception e) { return R.fail("删除失败：" + e.getMessage()); }
    }

    @Operation(summary = "资产管理统计")
    @GetMapping("/asset/statistics")
    public R<Map<String, Object>> assetStatistics() {
        try {
            Map<String, Object> r = new HashMap<>();
            r.put("totalCount", assetMapper.selectCount(null));
            r.put("inUseCount", assetMapper.selectCount(new LambdaQueryWrapper<GzctMilitaryAsset>().eq(GzctMilitaryAsset::getAssetStatus, "IN_USE")));
            r.put("idleCount", assetMapper.selectCount(new LambdaQueryWrapper<GzctMilitaryAsset>().eq(GzctMilitaryAsset::getAssetStatus, "IDLE")));
            r.put("scrappedCount", assetMapper.selectCount(new LambdaQueryWrapper<GzctMilitaryAsset>().eq(GzctMilitaryAsset::getAssetStatus, "SCRAPPED")));
            return R.success(r);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }
}

