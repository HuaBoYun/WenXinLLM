package com.huabo.cybermonitor.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.cybermonitor.entity.*;
import com.huabo.cybermonitor.mapper.GzctIndustryCompetitivenessMapper;
import com.huabo.cybermonitor.mapper.GzctIndustryLayoutMapper;
import com.huabo.cybermonitor.mapper.GzctIndustryMonitorMapper;
import com.huabo.cybermonitor.mapper.GzctIndustrySynergyMapper;
import com.huabo.cybermonitor.mapper.GzctIndustryWarningMapper;
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
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Tag(name = "行业穿透式监管", description = "行业业务穿透式监管全接口")
@RestController
@RequestMapping("/v1/supervision/industry")
@Slf4j
public class IndustrySupervisionController {

    @Autowired
    private GzctIndustryLayoutMapper layoutMapper;
    @Autowired
    private GzctIndustryMonitorMapper monitorMapper;
    @Autowired
    private GzctIndustryWarningMapper warningMapper;
    @Autowired
    private GzctIndustryCompetitivenessMapper competitivenessMapper;
    @Autowired
    private GzctIndustrySynergyMapper synergyMapper;
    @Autowired
    private com.huabo.cybermonitor.util.OrgQueryHelper orgQueryHelper;

    private <T> PageResult<T> buildPageResult(Page<T> r) {
        PageResult<T> pr = new PageResult<>();
        pr.setTotalRecord((int) r.getTotal()); pr.setCurrentPage((int) r.getCurrent());
        pr.setPageNumber((int) r.getCurrent()); pr.setTotalPage((int) r.getPages());
        pr.setPageSize((int) r.getSize()); pr.setTlist(r.getRecords());
        return pr;
    }

    // ==================== 行业布局管理 ====================

    @Operation(summary = "行业布局列表")
    @PostMapping("/layout/list")
    public R<PageResult<Map<String, Object>>> layoutList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 50;
            LambdaQueryWrapper<GzctIndustryLayout> w = new LambdaQueryWrapper<>();
            if (params.get("companyName") != null && StringUtils.isNotBlank(params.get("companyName").toString())) {
                w.like(GzctIndustryLayout::getCompanyName, params.get("companyName").toString());
            }
            if (params.get("industryGroup") != null && StringUtils.isNotBlank(params.get("industryGroup").toString())) {
                // 前端传来的是映射后的分组名（如"能源行业"），需转为数据库关键词进行OR模糊匹配
                List<String> keywords = getIndustryKeywords(params.get("industryGroup").toString());
                if (!keywords.isEmpty()) {
                    w.and(qw -> {
                        for (int i = 0; i < keywords.size(); i++) {
                            if (i == 0) {
                                qw.like(GzctIndustryLayout::getIndustryName, keywords.get(i));
                            } else {
                                qw.or().like(GzctIndustryLayout::getIndustryName, keywords.get(i));
                            }
                        }
                    });
                } else {
                    // 兜底：直接用原始值匹配
                    w.like(GzctIndustryLayout::getIndustryName, params.get("industryGroup").toString());
                }
            }
            if (params.get("industryName") != null && StringUtils.isNotBlank(params.get("industryName").toString())) {
                w.like(GzctIndustryLayout::getIndustryName, params.get("industryName").toString());
            }
            // 主业认定筛选
            if (params.get("mainBizType") != null && !"".equals(params.get("mainBizType").toString())) {
                String mainBizVal = Boolean.TRUE.equals(params.get("mainBizType")) || "true".equals(params.get("mainBizType").toString()) ? "1" : "0";
                w.eq(GzctIndustryLayout::getIsMainIndustry, mainBizVal);
            }
            // 竞争力等级筛选
            if (params.get("competitiveness") != null && StringUtils.isNotBlank(params.get("competitiveness").toString())) {
                w.eq(GzctIndustryLayout::getCompetitiveness, params.get("competitiveness").toString());
            }
            // 风险等级筛选
            if (params.get("riskLevel") != null && StringUtils.isNotBlank(params.get("riskLevel").toString())) {
                w.eq(GzctIndustryLayout::getRiskLevel, params.get("riskLevel").toString());
            }
            w.orderByDesc(GzctIndustryLayout::getCreateTime);
            Page<GzctIndustryLayout> page = layoutMapper.selectPage(new Page<>(pn, ps), w);
            List<Map<String, Object>> mappedList = page.getRecords().stream().map(l -> {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("id", l.getLayoutId());
                map.put("companyName", l.getCompanyName());
                map.put("industryGroup", mapToIndustryGroup(l.getIndustryName()));
                map.put("subIndustry", l.getSubIndustry());
                map.put("isMainBiz", "1".equals(l.getIsMainIndustry()));
                map.put("revenue", l.getRevenue() != null ? l.getRevenue().divide(BigDecimal.valueOf(10000), 1, RoundingMode.HALF_UP) : BigDecimal.ZERO);
                map.put("revenueRatio", l.getRevenueRatio());
                map.put("netMargin", l.getNetMargin());
                map.put("competitiveness", l.getCompetitiveness());
                map.put("riskLevel", l.getRiskLevel());
                map.put("isKeyMonitor", "1".equals(l.getIsKeyMonitor()));
                return map;
            }).collect(Collectors.toList());
            PageResult<Map<String, Object>> pr = new PageResult<>();
            pr.setTotalRecord((int) page.getTotal());
            pr.setCurrentPage((int) page.getCurrent());
            pr.setPageNumber((int) page.getCurrent());
            pr.setTotalPage((int) page.getPages());
            pr.setPageSize((int) page.getSize());
            pr.setTlist(mappedList);
            return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "行业布局详情")
    @GetMapping("/layout/detail/{id}")
    public R<GzctIndustryLayout> layoutDetail(@PathVariable String id) {
        try { return R.success(layoutMapper.selectById(id)); } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "新增行业布局")
    @PostMapping("/layout/add")
    public R<Map<String, Object>> addLayout(@RequestBody Map<String, Object> params) {
        try {
            GzctIndustryLayout record = new GzctIndustryLayout();
            record.setCompanyName(params.get("companyName") != null ? params.get("companyName").toString() : null);
            String industryGroup = params.get("industryGroup") != null ? params.get("industryGroup").toString() : null;
            record.setIndustryName(industryGroup);
            record.setIndustryCode(mapIndustryNameToCode(industryGroup));
            record.setSubIndustry(params.get("subIndustry") != null ? params.get("subIndustry").toString() : null);
            record.setIsMainIndustry(Boolean.TRUE.equals(params.get("isMainBiz")) ? "1" : "0");
            record.setRevenue(params.get("revenue") != null ? new BigDecimal(params.get("revenue").toString()).multiply(BigDecimal.valueOf(10000)) : null);
            record.setNetMargin(params.get("netMargin") != null ? new BigDecimal(params.get("netMargin").toString()) : null);
            record.setCompetitiveness(params.get("competitiveness") != null ? params.get("competitiveness").toString() : null);
            record.setRiskLevel(params.get("riskLevel") != null ? params.get("riskLevel").toString() : null);
            record.setIsKeyMonitor(Boolean.TRUE.equals(params.get("isKeyMonitor")) ? "1" : "0");
            record.setCreateTime(LocalDateTime.now());
            layoutMapper.insert(record);
            Map<String, Object> result = new HashMap<>();
            result.put("id", record.getLayoutId());
            return R.success(result);
        } catch (Exception e) { return R.fail("新增失败：" + e.getMessage()); }
    }

    @Operation(summary = "更新行业布局")
    @PostMapping("/layout/update")
    public R<Boolean> updateLayout(@RequestBody Map<String, Object> params) {
        try {
            String id = params.get("id") != null ? params.get("id").toString() : null;
            if (id == null) return R.fail("ID不能为空");
            GzctIndustryLayout record = layoutMapper.selectById(id);
            if (record == null) return R.fail("记录不存在");
            if (params.get("companyName") != null) record.setCompanyName(params.get("companyName").toString());
            if (params.get("industryGroup") != null) {
                String industryGroup = params.get("industryGroup").toString();
                record.setIndustryName(industryGroup);
                record.setIndustryCode(mapIndustryNameToCode(industryGroup));
            }
            if (params.get("subIndustry") != null) record.setSubIndustry(params.get("subIndustry").toString());
            if (params.containsKey("isMainBiz")) record.setIsMainIndustry(Boolean.TRUE.equals(params.get("isMainBiz")) ? "1" : "0");
            if (params.get("revenue") != null) record.setRevenue(new BigDecimal(params.get("revenue").toString()).multiply(BigDecimal.valueOf(10000)));
            if (params.get("netMargin") != null) record.setNetMargin(new BigDecimal(params.get("netMargin").toString()));
            if (params.get("competitiveness") != null) record.setCompetitiveness(params.get("competitiveness").toString());
            if (params.get("riskLevel") != null) record.setRiskLevel(params.get("riskLevel").toString());
            if (params.containsKey("isKeyMonitor")) record.setIsKeyMonitor(Boolean.TRUE.equals(params.get("isKeyMonitor")) ? "1" : "0");
            record.setUpdateTime(LocalDateTime.now());
            layoutMapper.updateById(record);
            return R.success(true);
        } catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); }
    }

    @Operation(summary = "删除行业布局")
    @DeleteMapping("/layout/{id}")
    public R<Boolean> deleteLayout(@PathVariable String id) {
        try { return R.success(layoutMapper.deleteById(id) > 0); } catch (Exception e) { return R.fail("删除失败：" + e.getMessage()); }
    }

    @Operation(summary = "更新主业标识")
    @PostMapping("/layout/updateMainBiz")
    public R<Boolean> updateMainBiz(@RequestBody Map<String, Object> params) {
        try {
            String id = params.get("id") != null ? params.get("id").toString() : null;
            if (id == null) return R.fail("ID不能为空");
            GzctIndustryLayout layout = layoutMapper.selectById(id);
            if (layout != null) {
                if (params.containsKey("isMainBiz")) {
                    layout.setIsMainIndustry(Boolean.TRUE.equals(params.get("isMainBiz")) ? "1" : "0");
                } else if (params.get("isMainIndustry") != null) {
                    layout.setIsMainIndustry(params.get("isMainIndustry").toString());
                }
                layout.setUpdateTime(LocalDateTime.now());
                layoutMapper.updateById(layout);
            }
            return R.success(true);
        } catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); }
    }

    @Operation(summary = "更新重点监控状态")
    @PostMapping("/layout/updateKeyMonitor")
    public R<Boolean> updateKeyMonitor(@RequestBody Map<String, Object> params) {
        try {
            String id = params.get("id") != null ? params.get("id").toString() : null;
            if (id == null) return R.fail("ID不能为空");
            GzctIndustryLayout layout = layoutMapper.selectById(id);
            if (layout == null) return R.fail("记录不存在");
            if (params.containsKey("isKeyMonitor")) {
                Object val = params.get("isKeyMonitor");
                layout.setIsKeyMonitor(Boolean.TRUE.equals(val) || "true".equals(String.valueOf(val)) ? "1" : "0");
            }
            layout.setUpdateTime(LocalDateTime.now());
            layoutMapper.updateById(layout);
            return R.success(true);
        } catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); }
    }

    // ==================== 行业统计 ====================

    @Operation(summary = "行业统计概览")
    @GetMapping("/statistics/overview")
    public R<Map<String, Object>> statisticsOverview(@RequestParam(required = false) String companyId) {
        try {
            Map<String, Object> result = new HashMap<>();
            String orgPattern = orgQueryHelper.getOrgPathPattern(companyId);
            LambdaQueryWrapper<GzctIndustryLayout> layoutBaseWrapper = new LambdaQueryWrapper<>();
            if (orgPattern != null) layoutBaseWrapper.and(w -> w.like(GzctIndustryLayout::getOrgPath, orgPattern).or(sub -> sub.isNull(GzctIndustryLayout::getOrgPath).eq(GzctIndustryLayout::getCompanyId, companyId)));
            List<GzctIndustryLayout> allLayouts = layoutMapper.selectList(layoutBaseWrapper);
            long totalLayouts = allLayouts.size();
            long mainCount = allLayouts.stream().filter(l -> "1".equals(l.getIsMainIndustry()) || "Y".equals(l.getIsMainIndustry())).count();
            // 风险数量：RISK_LEVEL='HIGH' 的行业数
            long riskCount = allLayouts.stream().filter(l -> "HIGH".equals(l.getRiskLevel())).count();
            result.put("totalIndustries", totalLayouts);
            result.put("industryCount", totalLayouts);
            result.put("mainIndustryCount", mainCount);
            result.put("nonMainIndustryCount", totalLayouts - mainCount);
            // 前端 STAT_MAP 期望字段
            result.put("companyCount", totalLayouts);
            result.put("enterpriseCount", totalLayouts);
            result.put("riskCount", riskCount);
            result.put("warningCount", riskCount);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "行业分布统计 - 饼图格式")
    @GetMapping("/statistics/distribution")
    public R<Map<String, Object>> statisticsDistribution() {
        try {
            List<GzctIndustryLayout> all = layoutMapper.selectList(null);
            Map<String, BigDecimal> revByIndustry = new LinkedHashMap<>();
            for (GzctIndustryLayout l : all) {
                String name = l.getIndustryName() != null ? l.getIndustryName() : "未知";
                revByIndustry.merge(name, l.getRevenue() != null ? l.getRevenue() : BigDecimal.ZERO, BigDecimal::add);
            }
            List<Map<String, Object>> pieData = new ArrayList<>();
            for (Map.Entry<String, BigDecimal> e : revByIndustry.entrySet()) {
                pieData.add(buildPieItem(e.getKey(), e.getValue().divide(BigDecimal.valueOf(10000), 1, RoundingMode.HALF_UP)));
            }
            Map<String, Object> result = new HashMap<>();
            result.put("pieData", pieData);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "行业营收趋势 - ECharts格式")
    @GetMapping("/statistics/revenue-trend")
    public R<Map<String, Object>> statisticsRevenueTrend() {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("legend", Arrays.asList("能源", "金融", "制造业", "基础设施", "公共服务"));
            result.put("xAxis", Arrays.asList("2019", "2020", "2021", "2022", "2023", "2024"));
            List<Map<String, Object>> series = new ArrayList<>();
            series.add(buildSeriesItem("能源", "line", Arrays.asList(6800, 7200, 7800, 8200, 8600, 8900)));
            series.add(buildSeriesItem("金融", "line", Arrays.asList(2800, 3000, 3200, 3100, 3300, 3500)));
            series.add(buildSeriesItem("制造业", "line", Arrays.asList(3800, 4000, 4200, 4400, 4500, 4800)));
            series.add(buildSeriesItem("基础设施", "line", Arrays.asList(2200, 2400, 2600, 2700, 2800, 2900)));
            series.add(buildSeriesItem("公共服务", "line", Arrays.asList(1500, 1600, 1700, 1750, 1800, 1900)));
            result.put("series", series);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "行业风险矩阵")
    @GetMapping("/statistics/risk-matrix")
    public R<List<Map<String, Object>>> statisticsRiskMatrix() {
        try {
            List<GzctIndustryLayout> all = layoutMapper.selectList(null);
            List<Map<String, Object>> result = new ArrayList<>();
            Map<String, List<GzctIndustryLayout>> grouped = new LinkedHashMap<>();
            for (GzctIndustryLayout l : all) {
                String name = l.getIndustryName() != null ? l.getIndustryName() : "未知";
                grouped.computeIfAbsent(name, k -> new ArrayList<>()).add(l);
            }
            for (Map.Entry<String, List<GzctIndustryLayout>> e : grouped.entrySet()) {
                Map<String, Object> m = new HashMap<>();
                m.put("industryName", e.getKey());
                m.put("companyCount", e.getValue().size());
                long highRisk = e.getValue().stream().filter(x -> "HIGH".equals(x.getRiskLevel())).count();
                m.put("highRiskCount", highRisk);
                result.add(m);
            }
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 五大行业专项 ====================

    @Operation(summary = "能源行业列表")
    @PostMapping("/energy/list")
    public R<PageResult<Map<String, Object>>> energyList(@RequestBody Map<String, Object> params) {
        return getIndustryListMapped("ENERGY", params, "energyType");
    }

    @Operation(summary = "新增能源监管记录")
    @PostMapping("/energy/add")
    public R<Map<String, Object>> addEnergyMonitor(@RequestBody Map<String, Object> params) {
        try {
            GzctIndustryMonitor record = new GzctIndustryMonitor();
            record.setIndustryCode("ENERGY");
            record.setIndustryName("能源行业");
            record.setCompanyName(params.get("companyName") != null ? params.get("companyName").toString() : null);
            record.setCompanyId(params.get("companyId") != null ? params.get("companyId").toString() : null);
            record.setSubType(params.get("subType") != null ? params.get("subType").toString() : null);
            record.setRevenue(params.get("capacity") != null ? new BigDecimal(params.get("capacity").toString()) : null);
            record.setRenewableRatio(params.get("cleanRatio") != null ? new BigDecimal(params.get("cleanRatio").toString()) : null);
            record.setCarbonIntensity(params.get("carbonEmission") != null ? new BigDecimal(params.get("carbonEmission").toString()) : null);
            record.setNetMargin(params.get("netMargin") != null ? new BigDecimal(params.get("netMargin").toString()) : null);
            record.setSafetyRating(params.get("safetyLevel") != null ? params.get("safetyLevel").toString() : null);
            record.setRiskLevel(params.get("riskLevel") != null ? params.get("riskLevel").toString() : "LOW");
            record.setCompetitiveness(params.get("competitiveness") != null ? params.get("competitiveness").toString() : null);
            record.setMajorRisks(params.get("incidents") != null ? Integer.parseInt(params.get("incidents").toString()) : 0);
            record.setCreateTime(LocalDateTime.now());
            monitorMapper.insert(record);
            Map<String, Object> result = new HashMap<>();
            result.put("id", record.getMonitorId());
            return R.success(result);
        } catch (Exception e) {
            log.error("新增能源监管记录失败", e);
            return R.fail("新增失败：" + e.getMessage());
        }
    }

    @Operation(summary = "导出能源监管数据")
    @PostMapping("/energy/export")
    public R<List<Map<String, Object>>> exportEnergyData(@RequestBody Map<String, Object> params) {
        try {
            LambdaQueryWrapper<GzctIndustryMonitor> w = new LambdaQueryWrapper<>();
            w.eq(GzctIndustryMonitor::getIndustryCode, "ENERGY");
            if (params.get("companyName") != null && StringUtils.isNotBlank(params.get("companyName").toString())) {
                w.like(GzctIndustryMonitor::getCompanyName, params.get("companyName").toString());
            }
            if (params.get("subType") != null && StringUtils.isNotBlank(params.get("subType").toString())) {
                w.like(GzctIndustryMonitor::getSubType, params.get("subType").toString());
            }
            if (params.get("riskLevel") != null && StringUtils.isNotBlank(params.get("riskLevel").toString())) {
                w.eq(GzctIndustryMonitor::getRiskLevel, params.get("riskLevel").toString());
            }
            w.orderByDesc(GzctIndustryMonitor::getCreateTime);
            List<GzctIndustryMonitor> list = monitorMapper.selectList(w);
            List<Map<String, Object>> exportList = list.stream().map(m -> {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("企业名称", m.getCompanyName());
                map.put("能源子类型", m.getSubType());
                map.put("装机容量(万千瓦)", m.getRevenue());
                map.put("清洁能源占比(%)", m.getRenewableRatio());
                map.put("碳排放(万吨)", m.getCarbonIntensity());
                map.put("安全等级", m.getSafetyRating());
                map.put("风险等级", "HIGH".equals(m.getRiskLevel()) ? "风险" : "MEDIUM".equals(m.getRiskLevel()) ? "预警" : "正常");
                map.put("安全事故数", m.getMajorRisks());
                map.put("创建时间", m.getCreateTime() != null ? m.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "");
                return map;
            }).collect(Collectors.toList());
            return R.success(exportList);
        } catch (Exception e) {
            log.error("导出能源监管数据失败", e);
            return R.fail("导出失败：" + e.getMessage());
        }
    }

    @Operation(summary = "能源监管详情")
    @GetMapping("/energy/detail/{id}")
    public R<Map<String, Object>> getEnergyDetail(@PathVariable String id) {
        try {
            GzctIndustryMonitor record = monitorMapper.selectById(id);
            if (record == null) return R.fail("记录不存在");
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("id", record.getMonitorId());
            result.put("companyName", record.getCompanyName());
            result.put("subType", record.getSubType());
            result.put("capacity", record.getRevenue());
            result.put("cleanRatio", record.getRenewableRatio());
            result.put("carbonEmission", record.getCarbonIntensity());
            result.put("netMargin", record.getNetMargin());
            result.put("competitiveness", record.getCompetitiveness());
            result.put("safetyLevel", record.getSafetyRating());
            result.put("riskLevel", record.getRiskLevel());
            result.put("incidents", record.getMajorRisks());
            result.put("createTime", record.getCreateTime() != null ? record.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "");
            return R.success(result);
        } catch (Exception e) {
            log.error("查询能源监管详情失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "更新能源监管记录")
    @PostMapping("/energy/update")
    public R<Boolean> updateEnergyMonitor(@RequestBody Map<String, Object> params) {
        try {
            String id = params.get("id") != null ? params.get("id").toString() : null;
            if (id == null) return R.fail("ID不能为空");
            GzctIndustryMonitor record = monitorMapper.selectById(id);
            if (record == null) return R.fail("记录不存在");
            if (params.get("companyName") != null) record.setCompanyName(params.get("companyName").toString());
            if (params.get("subType") != null) record.setSubType(params.get("subType").toString());
            if (params.get("capacity") != null) record.setRevenue(new BigDecimal(params.get("capacity").toString()));
            if (params.get("cleanRatio") != null) record.setRenewableRatio(new BigDecimal(params.get("cleanRatio").toString()));
            if (params.get("carbonEmission") != null) record.setCarbonIntensity(new BigDecimal(params.get("carbonEmission").toString()));
            if (params.get("netMargin") != null) record.setNetMargin(new BigDecimal(params.get("netMargin").toString()));
            if (params.get("competitiveness") != null) record.setCompetitiveness(params.get("competitiveness").toString());
            if (params.get("safetyLevel") != null) record.setSafetyRating(params.get("safetyLevel").toString());
            if (params.get("riskLevel") != null) record.setRiskLevel(params.get("riskLevel").toString());
            if (params.get("incidents") != null) record.setMajorRisks(Integer.parseInt(params.get("incidents").toString()));
            record.setUpdateTime(LocalDateTime.now());
            monitorMapper.updateById(record);
            return R.success(true);
        } catch (Exception e) {
            log.error("更新能源监管记录失败", e);
            return R.fail("更新失败：" + e.getMessage());
        }
    }

    @Operation(summary = "删除能源监管记录")
    @DeleteMapping("/energy/{id}")
    public R<Boolean> deleteEnergyMonitor(@PathVariable String id) {
        try {
            GzctIndustryMonitor record = monitorMapper.selectById(id);
            if (record == null) return R.fail("记录不存在");
            int rows = monitorMapper.deleteById(id);
            return R.success(rows > 0);
        } catch (Exception e) {
            log.error("删除能源监管记录失败", e);
            return R.fail("删除失败：" + e.getMessage());
        }
    }

    @Operation(summary = "能源转型统计")
    @GetMapping("/energy/transition-stats")
    public R<Map<String, Object>> energyTransitionStats() {
        try {
            List<GzctIndustryMonitor> list = monitorMapper.selectList(
                new LambdaQueryWrapper<GzctIndustryMonitor>().eq(GzctIndustryMonitor::getIndustryCode, "ENERGY"));
            Map<String, Object> result = new HashMap<>();
            // 趋势图数据
            result.put("legend", Arrays.asList("传统能源营收", "新能源营收", "新能源占比"));
            result.put("xAxis", Arrays.asList("2020", "2021", "2022", "2023", "2024"));
            List<Map<String, Object>> series = new ArrayList<>();
            series.add(buildSeriesItem("传统能源营收", "bar", Arrays.asList(2800, 2650, 2500, 2350, 2200)));
            series.add(buildSeriesItem("新能源营收", "bar", Arrays.asList(800, 1100, 1500, 1900, 2400)));
            Map<String, Object> ratioSeries = buildSeriesItem("新能源占比", "line", Arrays.asList(22, 29, 38, 45, 52));
            ratioSeries.put("yAxisIndex", 1);
            series.add(ratioSeries);
            result.put("series", series);
            // 饼图数据
            List<Map<String, Object>> pieData = new ArrayList<>();
            pieData.add(buildPieItem("电力输配", 2890));
            pieData.add(buildPieItem("石油化工", 3210));
            pieData.add(buildPieItem("煤炭电力", 680));
            pieData.add(buildPieItem("新能源", 450));
            Map<String, Object> distData = new HashMap<>();
            distData.put("pieData", pieData);
            result.put("distData", distData);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "金融行业列表")
    @PostMapping("/financial/list")
    public R<PageResult<Map<String, Object>>> financialList(@RequestBody Map<String, Object> params) {
        return getIndustryListMapped("FINANCIAL", params, "finType");
    }

    @Operation(summary = "新增金融监管记录")
    @PostMapping("/financial/add")
    public R<Map<String, Object>> addFinancialMonitor(@RequestBody Map<String, Object> params) {
        try {
            GzctIndustryMonitor record = new GzctIndustryMonitor();
            record.setIndustryCode("FINANCIAL");
            record.setIndustryName("金融行业");
            record.setCompanyName(params.get("companyName") != null ? params.get("companyName").toString() : null);
            record.setSubType(params.get("subType") != null ? params.get("subType").toString() : null);
            record.setTotalRevenue(params.get("totalAssets") != null ? new BigDecimal(params.get("totalAssets").toString()) : null);
            record.setBadDebtRatio(params.get("badDebtRatio") != null ? new BigDecimal(params.get("badDebtRatio").toString()) : null);
            record.setCapitalRatio(params.get("capitalRatio") != null ? new BigDecimal(params.get("capitalRatio").toString()) : null);
            record.setNetMargin(params.get("netMargin") != null ? new BigDecimal(params.get("netMargin").toString()) : null);
            record.setComplianceStatus(params.get("complianceStatus") != null ? params.get("complianceStatus").toString() : "COMPLIANT");
            record.setRiskLevel(params.get("riskLevel") != null ? params.get("riskLevel").toString() : "LOW");
            record.setCreateTime(LocalDateTime.now());
            monitorMapper.insert(record);
            Map<String, Object> result = new HashMap<>();
            result.put("id", record.getMonitorId());
            return R.success(result);
        } catch (Exception e) {
            log.error("新增金融监管记录失败", e);
            return R.fail("新增失败：" + e.getMessage());
        }
    }

    @Operation(summary = "金融监管详情")
    @GetMapping("/financial/detail/{id}")
    public R<Map<String, Object>> getFinancialDetail(@PathVariable String id) {
        try {
            GzctIndustryMonitor record = monitorMapper.selectById(id);
            if (record == null) return R.fail("记录不存在");
            Map<String, Object> result = new LinkedHashMap<>();
            result.put("id", record.getMonitorId());
            result.put("companyName", record.getCompanyName());
            result.put("subType", record.getSubType());
            result.put("totalAssets", record.getTotalRevenue());
            result.put("badDebtRatio", record.getBadDebtRatio());
            result.put("capitalRatio", record.getCapitalRatio());
            result.put("netMargin", record.getNetMargin());
            result.put("complianceStatus", record.getComplianceStatus());
            result.put("riskLevel", record.getRiskLevel());
            result.put("createTime", record.getCreateTime() != null ? record.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "");
            return R.success(result);
        } catch (Exception e) {
            log.error("查询金融监管详情失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "更新金融监管记录")
    @PostMapping("/financial/update")
    public R<Boolean> updateFinancialMonitor(@RequestBody Map<String, Object> params) {
        try {
            String id = params.get("id") != null ? params.get("id").toString() : null;
            if (id == null) return R.fail("ID不能为空");
            GzctIndustryMonitor record = monitorMapper.selectById(id);
            if (record == null) return R.fail("记录不存在");
            if (params.get("companyName") != null) record.setCompanyName(params.get("companyName").toString());
            if (params.get("subType") != null) record.setSubType(params.get("subType").toString());
            if (params.get("totalAssets") != null) record.setTotalRevenue(new BigDecimal(params.get("totalAssets").toString()));
            if (params.get("badDebtRatio") != null) record.setBadDebtRatio(new BigDecimal(params.get("badDebtRatio").toString()));
            if (params.get("capitalRatio") != null) record.setCapitalRatio(new BigDecimal(params.get("capitalRatio").toString()));
            if (params.get("netMargin") != null) record.setNetMargin(new BigDecimal(params.get("netMargin").toString()));
            if (params.get("complianceStatus") != null) record.setComplianceStatus(params.get("complianceStatus").toString());
            if (params.get("riskLevel") != null) record.setRiskLevel(params.get("riskLevel").toString());
            record.setUpdateTime(LocalDateTime.now());
            monitorMapper.updateById(record);
            return R.success(true);
        } catch (Exception e) {
            log.error("更新金融监管记录失败", e);
            return R.fail("更新失败：" + e.getMessage());
        }
    }

    @Operation(summary = "删除金融监管记录")
    @DeleteMapping("/financial/{id}")
    public R<Boolean> deleteFinancialMonitor(@PathVariable String id) {
        try {
            GzctIndustryMonitor record = monitorMapper.selectById(id);
            if (record == null) return R.fail("记录不存在");
            int rows = monitorMapper.deleteById(id);
            return R.success(rows > 0);
        } catch (Exception e) {
            log.error("删除金融监管记录失败", e);
            return R.fail("删除失败：" + e.getMessage());
        }
    }

    @Operation(summary = "导出金融监管数据")
    @PostMapping("/financial/export")
    public R<List<Map<String, Object>>> exportFinancialData(@RequestBody Map<String, Object> params) {
        try {
            LambdaQueryWrapper<GzctIndustryMonitor> w = new LambdaQueryWrapper<>();
            w.eq(GzctIndustryMonitor::getIndustryCode, "FINANCIAL");
            if (params.get("companyName") != null && StringUtils.isNotBlank(params.get("companyName").toString())) {
                w.like(GzctIndustryMonitor::getCompanyName, params.get("companyName").toString());
            }
            if (params.get("subType") != null && StringUtils.isNotBlank(params.get("subType").toString())) {
                w.like(GzctIndustryMonitor::getSubType, params.get("subType").toString());
            }
            if (params.get("riskLevel") != null && StringUtils.isNotBlank(params.get("riskLevel").toString())) {
                w.eq(GzctIndustryMonitor::getRiskLevel, params.get("riskLevel").toString());
            }
            w.orderByDesc(GzctIndustryMonitor::getCreateTime);
            List<GzctIndustryMonitor> list = monitorMapper.selectList(w);
            List<Map<String, Object>> exportList = list.stream().map(m -> {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("企业名称", m.getCompanyName());
                map.put("金融子类型", m.getSubType());
                map.put("资产规模(亿)", m.getTotalRevenue());
                map.put("不良率(%)", m.getBadDebtRatio());
                map.put("资本充足率(%)", m.getCapitalRatio());
                map.put("合规状态", m.getComplianceStatus());
                map.put("风险等级", "HIGH".equals(m.getRiskLevel()) ? "高风险" : "MEDIUM".equals(m.getRiskLevel()) ? "中风险" : "正常");
                map.put("创建时间", m.getCreateTime() != null ? m.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "");
                return map;
            }).collect(Collectors.toList());
            return R.success(exportList);
        } catch (Exception e) {
            log.error("导出金融监管数据失败", e);
            return R.fail("导出失败：" + e.getMessage());
        }
    }

    @Operation(summary = "金融风险指标")
    @GetMapping("/financial/risk-indicators")
    public R<Map<String, Object>> financialRiskIndicators() {
        try {
            Map<String, Object> result = new HashMap<>();
            // 趋势图
            Map<String, Object> trendData = new HashMap<>();
            trendData.put("legend", Arrays.asList("银行资产", "保险资产", "证券资产"));
            trendData.put("xAxis", Arrays.asList("2020", "2021", "2022", "2023", "2024"));
            List<Map<String, Object>> trendSeries = new ArrayList<>();
            trendSeries.add(buildSeriesItem("银行资产", "line", Arrays.asList(35000, 38000, 40000, 42000, 45000)));
            trendSeries.add(buildSeriesItem("保险资产", "line", Arrays.asList(4500, 5000, 5500, 5800, 6200)));
            trendSeries.add(buildSeriesItem("证券资产", "line", Arrays.asList(2000, 2500, 2800, 3000, 3200)));
            trendData.put("series", trendSeries);
            result.put("trendData", trendData);
            // 不良率柱状图
            Map<String, Object> badDebtData = new HashMap<>();
            badDebtData.put("yAxis", Arrays.asList("商业银行", "保险", "证券", "基金", "融资租赁"));
            List<Map<String, Object>> bdSeries = new ArrayList<>();
            bdSeries.add(buildSeriesItem("不良率", "bar", Arrays.asList(1.42, 0.85, 0.62, 0.35, 2.80)));
            badDebtData.put("series", bdSeries);
            result.put("badDebtData", badDebtData);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "制造业列表")
    @PostMapping("/manufacturing/list")
    public R<PageResult<Map<String, Object>>> manufacturingList(@RequestBody Map<String, Object> params) {
        return getIndustryListMapped("MANUFACTURING", params, "mfgType");
    }

    @Operation(summary = "制造业研发统计")
    @GetMapping("/manufacturing/rd-stats")
    public R<Map<String, Object>> manufacturingRdStats() {
        try {
            Map<String, Object> result = new HashMap<>();
            // 柱状图
            Map<String, Object> barData = new HashMap<>();
            barData.put("yAxis", Arrays.asList("轨道交通装备", "航空航天", "电子信息", "化工材料", "精密制造"));
            List<Map<String, Object>> barSeries = new ArrayList<>();
            barSeries.add(buildSeriesItem("营收", "bar", Arrays.asList(2350, 3800, 1200, 890, 650)));
            barData.put("series", barSeries);
            result.put("barData", barData);
            // 趋势图
            Map<String, Object> trendData = new HashMap<>();
            trendData.put("legend", Arrays.asList("轨道交通", "航空航天", "行业平均"));
            trendData.put("xAxis", Arrays.asList("2020", "2021", "2022", "2023", "2024"));
            List<Map<String, Object>> trendSeries = new ArrayList<>();
            trendSeries.add(buildSeriesItem("轨道交通", "line", Arrays.asList(5.2, 5.8, 6.2, 6.5, 6.8)));
            trendSeries.add(buildSeriesItem("航空航天", "line", Arrays.asList(9.5, 10.2, 11.0, 11.8, 12.5)));
            trendSeries.add(buildSeriesItem("行业平均", "line", Arrays.asList(4.5, 5.0, 5.5, 5.8, 6.2)));
            trendData.put("series", trendSeries);
            result.put("trendData", trendData);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "基础设施列表")
    @PostMapping("/infrastructure/list")
    public R<PageResult<Map<String, Object>>> infrastructureList(@RequestBody Map<String, Object> params) {
        return getIndustryListMapped("INFRASTRUCTURE", params, "infraType");
    }

    @Operation(summary = "基础设施统计")
    @GetMapping("/infrastructure/stats")
    public R<Map<String, Object>> infrastructureStats() {
        try {
            Map<String, Object> result = new HashMap<>();
            // 饼图
            Map<String, Object> distData = new HashMap<>();
            List<Map<String, Object>> pieData = new ArrayList<>();
            pieData.add(buildPieItem("交通基建", 7200));
            pieData.add(buildPieItem("水利工程", 4200));
            pieData.add(buildPieItem("通信基建", 2800));
            pieData.add(buildPieItem("城市基建", 3500));
            distData.put("pieData", pieData);
            result.put("distData", distData);
            // 趋势图
            Map<String, Object> trendData = new HashMap<>();
            trendData.put("legend", Arrays.asList("交通基建", "水利工程", "行业平均"));
            trendData.put("xAxis", Arrays.asList("2020", "2021", "2022", "2023", "2024"));
            List<Map<String, Object>> trendSeries = new ArrayList<>();
            trendSeries.add(buildSeriesItem("交通基建", "line", Arrays.asList(5.2, 5.5, 5.8, 5.6, 5.8)));
            trendSeries.add(buildSeriesItem("水利工程", "line", Arrays.asList(6.0, 6.2, 6.1, 6.3, 6.2)));
            trendSeries.add(buildSeriesItem("行业平均", "line", Arrays.asList(5.5, 5.7, 5.8, 5.8, 5.9)));
            trendData.put("series", trendSeries);
            result.put("trendData", trendData);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "公共服务列表")
    @PostMapping("/publicService/list")
    public R<PageResult<Map<String, Object>>> publicServiceList(@RequestBody Map<String, Object> params) {
        return getIndustryListMapped("PUBLIC_SERVICE", params, "serviceType");
    }

    @Operation(summary = "公共服务统计")
    @GetMapping("/publicService/stats")
    public R<Map<String, Object>> publicServiceStats() {
        try { return R.success(getIndustryStats("PUBLIC_SERVICE")); } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 制造/基础设施/公共服务 通用CRUD ====================

    @Operation(summary = "")
    @PostMapping("/manufacturing/add")
    public R<Map<String, Object>> addManufacturing(@RequestBody Map<String, Object> p) { return genericAdd("MANUFACTURING", "制造行业", p); }
    @Operation(summary = "更新")
    @GetMapping("/manufacturing/detail/{id}")
    public R<Map<String, Object>> detailManufacturing(@PathVariable String id) { return genericDetail(id); }
    @Operation(summary = "更新")
    @PostMapping("/manufacturing/update")
    public R<Boolean> updateManufacturing(@RequestBody Map<String, Object> p) { return genericUpdate(p); }
    @Operation(summary = "删除")
    @DeleteMapping("/manufacturing/{id}")
    public R<Boolean> deleteManufacturing(@PathVariable String id) { return genericDelete(id); }
    @Operation(summary = "")
    @PostMapping("/manufacturing/export")
    public R<List<Map<String, Object>>> exportManufacturing(@RequestBody Map<String, Object> p) { return genericExport("MANUFACTURING", p); }

    @Operation(summary = "")
    @PostMapping("/infrastructure/add")
    public R<Map<String, Object>> addInfrastructure(@RequestBody Map<String, Object> p) { return genericAdd("INFRASTRUCTURE", "基础设施", p); }
    @Operation(summary = "更新")
    @GetMapping("/infrastructure/detail/{id}")
    public R<Map<String, Object>> detailInfrastructure(@PathVariable String id) { return genericDetail(id); }
    @Operation(summary = "更新")
    @PostMapping("/infrastructure/update")
    public R<Boolean> updateInfrastructure(@RequestBody Map<String, Object> p) { return genericUpdate(p); }
    @Operation(summary = "删除")
    @DeleteMapping("/infrastructure/{id}")
    public R<Boolean> deleteInfrastructure(@PathVariable String id) { return genericDelete(id); }
    @Operation(summary = "")
    @PostMapping("/infrastructure/export")
    public R<List<Map<String, Object>>> exportInfrastructure(@RequestBody Map<String, Object> p) { return genericExport("INFRASTRUCTURE", p); }

    @Operation(summary = "")
    @PostMapping("/publicService/add")
    public R<Map<String, Object>> addPublicService(@RequestBody Map<String, Object> p) { return genericAdd("PUBLIC_SERVICE", "公共服务", p); }
    @Operation(summary = "更新")
    @GetMapping("/publicService/detail/{id}")
    public R<Map<String, Object>> detailPublicService(@PathVariable String id) { return genericDetail(id); }
    @Operation(summary = "更新")
    @PostMapping("/publicService/update")
    public R<Boolean> updatePublicService(@RequestBody Map<String, Object> p) { return genericUpdate(p); }
    @Operation(summary = "删除")
    @DeleteMapping("/publicService/{id}")
    public R<Boolean> deletePublicService(@PathVariable String id) { return genericDelete(id); }
    @Operation(summary = "")
    @PostMapping("/publicService/export")
    public R<List<Map<String, Object>>> exportPublicService(@RequestBody Map<String, Object> p) { return genericExport("PUBLIC_SERVICE", p); }

    /** 通用新增 */
    private R<Map<String, Object>> genericAdd(String industryCode, String industryName, Map<String, Object> params) {
        try {
            GzctIndustryMonitor record = new GzctIndustryMonitor();
            record.setIndustryCode(industryCode);
            record.setIndustryName(industryName);
            record.setCompanyName(params.get("companyName") != null ? params.get("companyName").toString() : null);
            record.setSubType(params.get("subType") != null ? params.get("subType").toString() : null);
            record.setRevenue(params.get("revenue") != null ? new BigDecimal(params.get("revenue").toString()) : null);
            record.setTotalRevenue(params.get("totalAssets") != null ? new BigDecimal(params.get("totalAssets").toString()) : null);
            record.setRiskLevel(params.get("riskLevel") != null ? params.get("riskLevel").toString() : "LOW");
            record.setSafetyRating(params.get("safetyLevel") != null ? params.get("safetyLevel").toString() : null);
            record.setRdIntensity(params.get("rdIntensity") != null ? new BigDecimal(params.get("rdIntensity").toString()) : null);
            record.setSmartLevel(params.get("smartLevel") != null ? params.get("smartLevel").toString() : null);
            record.setTechSelfRate(params.get("techSelfRate") != null ? new BigDecimal(params.get("techSelfRate").toString()) : null);
            record.setCompetitiveness(params.get("competitiveness") != null ? params.get("competitiveness").toString() : null);
            record.setMajorRisks(params.get("incidents") != null ? Integer.parseInt(params.get("incidents").toString()) : 0);
            record.setCreateTime(LocalDateTime.now());
            monitorMapper.insert(record);
            Map<String, Object> result = new HashMap<>();
            result.put("id", record.getMonitorId());
            return R.success(result);
        } catch (Exception e) { log.error("新增监管记录失败", e); return R.fail("新增失败：" + e.getMessage()); }
    }

    /** 通用详情 */
    private R<Map<String, Object>> genericDetail(String id) {
        try {
            GzctIndustryMonitor m = monitorMapper.selectById(id);
            if (m == null) return R.fail("记录不存在");
            Map<String, Object> r = new LinkedHashMap<>();
            r.put("id", m.getMonitorId()); r.put("companyName", m.getCompanyName());
            r.put("subType", m.getSubType()); r.put("revenue", m.getRevenue());
            r.put("totalAssets", m.getTotalRevenue()); r.put("riskLevel", m.getRiskLevel());
            r.put("safetyLevel", m.getSafetyRating()); r.put("rdIntensity", m.getRdIntensity());
            r.put("smartLevel", m.getSmartLevel()); r.put("incidents", m.getMajorRisks());
            r.put("techSelfRate", m.getTechSelfRate()); r.put("competitiveness", m.getCompetitiveness());
            r.put("createTime", m.getCreateTime() != null ? m.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "");
            return R.success(r);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    /** 通用更新 */
    private R<Boolean> genericUpdate(Map<String, Object> params) {
        try {
            String id = params.get("id") != null ? params.get("id").toString() : null;
            if (id == null) return R.fail("ID不能为空");
            GzctIndustryMonitor record = monitorMapper.selectById(id);
            if (record == null) return R.fail("记录不存在");
            if (params.get("companyName") != null) record.setCompanyName(params.get("companyName").toString());
            if (params.get("subType") != null) record.setSubType(params.get("subType").toString());
            if (params.get("revenue") != null) record.setRevenue(new BigDecimal(params.get("revenue").toString()));
            if (params.get("totalAssets") != null) record.setTotalRevenue(new BigDecimal(params.get("totalAssets").toString()));
            if (params.get("riskLevel") != null) record.setRiskLevel(params.get("riskLevel").toString());
            if (params.get("safetyLevel") != null) record.setSafetyRating(params.get("safetyLevel").toString());
            if (params.get("rdIntensity") != null) record.setRdIntensity(new BigDecimal(params.get("rdIntensity").toString()));
            if (params.get("smartLevel") != null) record.setSmartLevel(params.get("smartLevel").toString());
            if (params.get("techSelfRate") != null) record.setTechSelfRate(new BigDecimal(params.get("techSelfRate").toString()));
            if (params.get("competitiveness") != null) record.setCompetitiveness(params.get("competitiveness").toString());
            if (params.get("incidents") != null) record.setMajorRisks(Integer.parseInt(params.get("incidents").toString()));
            record.setUpdateTime(LocalDateTime.now());
            monitorMapper.updateById(record);
            return R.success(true);
        } catch (Exception e) { log.error("更新监管记录失败", e); return R.fail("更新失败：" + e.getMessage()); }
    }

    /** 通用删除 */
    private R<Boolean> genericDelete(String id) {
        try {
            if (monitorMapper.selectById(id) == null) return R.fail("记录不存在");
            return R.success(monitorMapper.deleteById(id) > 0);
        } catch (Exception e) { log.error("删除监管记录失败", e); return R.fail("删除失败：" + e.getMessage()); }
    }

    /** 通用导出 */
    private R<List<Map<String, Object>>> genericExport(String industryCode, Map<String, Object> params) {
        try {
            LambdaQueryWrapper<GzctIndustryMonitor> w = new LambdaQueryWrapper<>();
            w.eq(GzctIndustryMonitor::getIndustryCode, industryCode);
            if (params.get("companyName") != null && StringUtils.isNotBlank(params.get("companyName").toString()))
                w.like(GzctIndustryMonitor::getCompanyName, params.get("companyName").toString());
            if (params.get("subType") != null && StringUtils.isNotBlank(params.get("subType").toString()))
                w.like(GzctIndustryMonitor::getSubType, params.get("subType").toString());
            if (params.get("riskLevel") != null && StringUtils.isNotBlank(params.get("riskLevel").toString()))
                w.eq(GzctIndustryMonitor::getRiskLevel, params.get("riskLevel").toString());
            w.orderByDesc(GzctIndustryMonitor::getCreateTime);
            List<GzctIndustryMonitor> list = monitorMapper.selectList(w);
            List<Map<String, Object>> exportList = list.stream().map(m -> {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("企业名称", m.getCompanyName());
                map.put("子类型", m.getSubType());
                map.put("营收/资产规模", m.getRevenue() != null ? m.getRevenue() : m.getTotalRevenue());
                map.put("风险等级", "HIGH".equals(m.getRiskLevel()) ? "高风险" : "MEDIUM".equals(m.getRiskLevel()) ? "中风险" : "正常");
                map.put("安全等级", m.getSafetyRating());
                map.put("研发强度(%)", m.getRdIntensity());
                map.put("创建时间", m.getCreateTime() != null ? m.getCreateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "");
                return map;
            }).collect(Collectors.toList());
            return R.success(exportList);
        } catch (Exception e) { log.error("导出数据失败", e); return R.fail("导出失败：" + e.getMessage()); }
    }

    /** 通用行业列表查询，将subType映射为前端期望的字段名 */
    private R<PageResult<Map<String, Object>>> getIndustryListMapped(String industryCode, Map<String, Object> params, String typeFieldName) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString())
                    : (params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1);
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 50;
            LambdaQueryWrapper<GzctIndustryMonitor> w = new LambdaQueryWrapper<>();
            w.eq(GzctIndustryMonitor::getIndustryCode, industryCode);
            // 支持 companyName 和 enterpriseName 两种参数名
            String nameParam = params.get("companyName") != null ? params.get("companyName").toString()
                    : (params.get("enterpriseName") != null ? params.get("enterpriseName").toString() : null);
            if (StringUtils.isNotBlank(nameParam)) {
                w.like(GzctIndustryMonitor::getCompanyName, nameParam);
            }
            // 支持 subType / industryType 筛选
            String subTypeParam = params.get("subType") != null ? params.get("subType").toString()
                    : (params.get("industryType") != null ? params.get("industryType").toString() : null);
            if (StringUtils.isNotBlank(subTypeParam)) {
                w.like(GzctIndustryMonitor::getSubType, subTypeParam);
            }
            // 支持 riskLevel / status 筛选 (前端传中文状态，需映射)
            String statusParam = params.get("riskLevel") != null ? params.get("riskLevel").toString()
                    : (params.get("status") != null ? params.get("status").toString() : null);
            if (StringUtils.isNotBlank(statusParam)) {
                String mappedLevel = statusParam;
                if ("正常".equals(statusParam)) mappedLevel = "LOW";
                else if ("预警".equals(statusParam)) mappedLevel = "MEDIUM";
                else if ("风险".equals(statusParam)) mappedLevel = "HIGH";
                w.eq(GzctIndustryMonitor::getRiskLevel, mappedLevel);
            }
            w.orderByDesc(GzctIndustryMonitor::getCreateTime);
            Page<GzctIndustryMonitor> page = monitorMapper.selectPage(new Page<>(pn, ps), w);
            // 转换为前端期望的字段名
            List<Map<String, Object>> mappedList = page.getRecords().stream().map(m -> {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("id", m.getMonitorId());
                map.put("companyName", m.getCompanyName());
                map.put(typeFieldName, m.getSubType()); // energyType/finType/mfgType/infraType/serviceType
                map.put("subType", m.getSubType());
                map.put("revenue", m.getRevenue() != null ? m.getRevenue() : m.getTotalRevenue());
                map.put("netMargin", m.getNetMargin());
                map.put("competitiveness", m.getCompetitiveness());
                map.put("riskLevel", m.getRiskLevel());
                // 能源特有
                map.put("renewableRatio", m.getRenewableRatio());
                map.put("carbonIntensity", m.getCarbonIntensity());
                // 金融特有
                map.put("totalAssets", m.getTotalRevenue());
                map.put("badDebtRatio", m.getBadDebtRatio());
                map.put("capitalRatio", m.getCapitalRatio());
                map.put("complianceStatus", m.getComplianceStatus() != null ? m.getComplianceStatus().replace("合规", "COMPLIANT").replace("警示", "WARNING").replace("整改中", "WARNING").replace("违规", "VIOLATION") : "COMPLIANT");
                // 制造特有
                map.put("rdIntensity", m.getRdIntensity());
                map.put("smartLevel", m.getSmartLevel());
                map.put("techSelfRate", m.getTechSelfRate());
                // 基础设施特有
                map.put("totalAssets", m.getTotalRevenue());
                map.put("assetReturn", m.getAssetReturn());
                map.put("debtRatio", m.getDebtRatio());
                map.put("safetyRating", m.getSafetyRating());
                map.put("majorRisks", m.getMajorRisks());
                // 公共服务特有
                map.put("subsidyRatio", m.getSubsidyRatio());
                map.put("lossYears", m.getLossYears());
                map.put("socialScore", m.getSocialScore());
                map.put("profitStatus", m.getProfitStatus() != null ? m.getProfitStatus().replace("盈利", "PROFIT").replace("微利", "SLIGHT_PROFIT").replace("亏损", "LOSS") : null);
                // 能源类型映射
                if ("ENERGY".equals(industryCode) && m.getSubType() != null) {
                    String energyType = (m.getSubType().contains("新能源") || m.getSubType().contains("风电") || m.getSubType().contains("光伏")) ? "新能源" : "传统能源";
                    map.put("energyType", energyType);
                }
                return map;
            }).collect(Collectors.toList());
            PageResult<Map<String, Object>> pr = new PageResult<>();
            pr.setTotalRecord((int) page.getTotal());
            pr.setCurrentPage((int) page.getCurrent());
            pr.setPageNumber((int) page.getCurrent());
            pr.setTotalPage((int) page.getPages());
            pr.setPageSize((int) page.getSize());
            pr.setTlist(mappedList);
            return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    private Map<String, Object> getIndustryStats(String industryCode) {
        Map<String, Object> result = new HashMap<>();
        LambdaQueryWrapper<GzctIndustryMonitor> w = new LambdaQueryWrapper<>();
        w.eq(GzctIndustryMonitor::getIndustryCode, industryCode);
        Long count = monitorMapper.selectCount(w);
        result.put("totalCount", count);
        result.put("industryCode", industryCode);
        return result;
    }

    // ==================== 竞争力分析 ====================

    @Operation(summary = "竞争力分析列表")
    @PostMapping("/competitiveness/list")
    public R<PageResult<Map<String, Object>>> competitivenessList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 50;
            LambdaQueryWrapper<GzctIndustryCompetitiveness> w = new LambdaQueryWrapper<>();
            if (params.get("companyName") != null && StringUtils.isNotBlank(params.get("companyName").toString())) {
                w.like(GzctIndustryCompetitiveness::getCompanyName, params.get("companyName").toString());
            }
            if (params.get("industry") != null && StringUtils.isNotBlank(params.get("industry").toString())) {
                // 前端传"能源行业"，数据库存"能源"，做模糊匹配
                String industryParam = params.get("industry").toString().replace("行业", "");
                w.like(GzctIndustryCompetitiveness::getIndustryName, industryParam);
            }
            w.orderByDesc(GzctIndustryCompetitiveness::getTotalScore);
            Page<GzctIndustryCompetitiveness> page = competitivenessMapper.selectPage(new Page<>(pn, ps), w);
            List<Map<String, Object>> mappedList = page.getRecords().stream().map(c -> {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("id", c.getCompetId());
                map.put("companyName", c.getCompanyName());
                map.put("industry", mapToIndustryGroup(c.getIndustryName()));
                map.put("totalScore", c.getTotalScore() != null ? c.getTotalScore().doubleValue() : 0);
                map.put("rdScore", c.getRdScore() != null ? c.getRdScore().doubleValue() : 0);
                map.put("marketScore", c.getMarketScore() != null ? c.getMarketScore().doubleValue() : 0);
                map.put("profitScore", c.getProfitScore() != null ? c.getProfitScore().doubleValue() : 0);
                map.put("brandScore", c.getBrandScore() != null ? c.getBrandScore().doubleValue() : 0);
                map.put("innovScore", c.getInnovScore() != null ? c.getInnovScore().doubleValue() : 0);
                map.put("industryRank", c.getRankIndustry() != null ? c.getRankIndustry() : 0);
                map.put("trend", c.getTrend() != null ? c.getTrend() : "STABLE");
                return map;
            }).collect(Collectors.toList());
            PageResult<Map<String, Object>> pr = new PageResult<>();
            pr.setTotalRecord((int) page.getTotal());
            pr.setCurrentPage((int) page.getCurrent());
            pr.setPageNumber((int) page.getCurrent());
            pr.setTotalPage((int) page.getPages());
            pr.setPageSize((int) page.getSize());
            pr.setTlist(mappedList);
            return R.success(pr);
        } catch (Exception e) {
            log.error("竞争力列表查询失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "竞争力排名")
    @GetMapping("/competitiveness/ranking")
    public R<Map<String, Object>> competitivenessRanking() {
        try {
            List<GzctIndustryCompetitiveness> list = competitivenessMapper.selectList(
                new LambdaQueryWrapper<GzctIndustryCompetitiveness>().orderByDesc(GzctIndustryCompetitiveness::getTotalScore));
            Map<String, Object> result = new HashMap<>();
            // 排名列表
            List<Map<String, Object>> rankList = list.stream().limit(8).map(c -> {
                Map<String, Object> m = new HashMap<>();
                m.put("companyName", c.getCompanyName());
                m.put("totalScore", c.getTotalScore() != null ? c.getTotalScore().doubleValue() : 0);
                return m;
            }).collect(Collectors.toList());
            result.put("rankList", rankList);
            // 雷达图数据
            Map<String, Object> radarData = new HashMap<>();
            radarData.put("indicator", Arrays.asList(
                buildIndicator("研发投入", 100), buildIndicator("市场份额", 100),
                buildIndicator("盈利能力", 100), buildIndicator("品牌价值", 100), buildIndicator("创新能力", 100)));
            // 按行业分组取平均
            Map<String, List<GzctIndustryCompetitiveness>> grouped = list.stream()
                .collect(Collectors.groupingBy(c -> mapToIndustryGroup(c.getIndustryName() != null ? c.getIndustryName() : "未知")));
            List<String> legend = new ArrayList<>(grouped.keySet());
            List<Map<String, Object>> radarSeries = new ArrayList<>();
            for (Map.Entry<String, List<GzctIndustryCompetitiveness>> entry : grouped.entrySet()) {
                List<GzctIndustryCompetitiveness> items = entry.getValue();
                double avgRd = items.stream().mapToDouble(c -> c.getRdScore() != null ? c.getRdScore().doubleValue() : 0).average().orElse(0);
                double avgMarket = items.stream().mapToDouble(c -> c.getMarketScore() != null ? c.getMarketScore().doubleValue() : 0).average().orElse(0);
                double avgProfit = items.stream().mapToDouble(c -> c.getProfitScore() != null ? c.getProfitScore().doubleValue() : 0).average().orElse(0);
                double avgBrand = items.stream().mapToDouble(c -> c.getBrandScore() != null ? c.getBrandScore().doubleValue() : 0).average().orElse(0);
                double avgInnov = items.stream().mapToDouble(c -> c.getInnovScore() != null ? c.getInnovScore().doubleValue() : 0).average().orElse(0);
                Map<String, Object> seriesItem = new HashMap<>();
                seriesItem.put("name", entry.getKey());
                seriesItem.put("value", Arrays.asList(avgRd, avgMarket, avgProfit, avgBrand, avgInnov));
                radarSeries.add(seriesItem);
            }
            radarData.put("legend", legend);
            radarData.put("series", radarSeries);
            result.put("radarData", radarData);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 产业协同 ====================

    @Operation(summary = "产业协同列表")
    @GetMapping("/synergy/list")
    public R<Map<String, Object>> synergyList() {
        try {
            List<GzctIndustrySynergy> list = synergyMapper.selectList(
                new LambdaQueryWrapper<GzctIndustrySynergy>().orderByDesc(GzctIndustrySynergy::getAnnualValue));
            Map<String, Object> result = new HashMap<>();
            // 列表数据
            List<Map<String, Object>> mappedList = list.stream().map(s -> {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("id", s.getSynergyId());
                map.put("synergyType", s.getSynergyType());
                map.put("fromIndustry", s.getFromIndustry());
                map.put("toIndustry", s.getToIndustry());
                map.put("enterpriseCount", s.getEnterpriseCount());
                map.put("annualValue", s.getAnnualValue() != null ? s.getAnnualValue().intValue() : 0);
                map.put("strength", s.getStrength());
                map.put("maturity", s.getMaturity());
                map.put("desc", s.getDescription());
                return map;
            }).collect(Collectors.toList());
            result.put("list", mappedList);
            // KPI数据
            Map<String, Object> kpiData = new HashMap<>();
            kpiData.put("projectCount", list.size() + "个");
            BigDecimal totalValue = list.stream().map(s -> s.getAnnualValue() != null ? s.getAnnualValue() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add);
            kpiData.put("annualSaving", totalValue.divide(BigDecimal.valueOf(10000), 1, RoundingMode.HALF_UP) + "亿");
            kpiData.put("internalTrade", totalValue.multiply(BigDecimal.valueOf(2)).divide(BigDecimal.valueOf(10000), 1, RoundingMode.HALF_UP) + "亿");
            kpiData.put("coverageRatio", "78.5%");
            result.put("kpiData", kpiData);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "产业协同矩阵")
    @GetMapping("/synergy/matrix")
    public R<Map<String, Map<String, Integer>>> synergyMatrix() {
        try {
            List<GzctIndustrySynergy> list = synergyMapper.selectList(null);
            Map<String, Map<String, Integer>> matrix = new LinkedHashMap<>();
            String[] industries = {"能源行业", "金融行业", "制造行业", "基础设施", "公共服务"};
            for (String ind : industries) {
                matrix.put(ind, new LinkedHashMap<>());
                for (String ind2 : industries) {
                    matrix.get(ind).put(ind2, 0);
                }
            }
            for (GzctIndustrySynergy s : list) {
                String from = mapToIndustryGroup(s.getFromIndustry());
                String to = mapToIndustryGroup(s.getToIndustry());
                if (matrix.containsKey(from) && matrix.get(from).containsKey(to)) {
                    int score = s.getSynergyScore() != null ? s.getSynergyScore().intValue() : 0;
                    matrix.get(from).put(to, Math.max(matrix.get(from).get(to), score));
                }
            }
            return R.success(matrix);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 首页KPI ====================

    @Operation(summary = "行业首页KPI")
    @GetMapping("/kpi")
    public R<Map<String, Object>> kpi() {
        try {
            Map<String, Object> result = new HashMap<>();

            // 2. 五大行业群概览（先算，后面汇总依赖它）
            List<Map<String, Object>> industryGroups = buildHomeIndustryGroups();
            result.put("industryGroups", industryGroups);

            // 从五大行业群汇总企业总数（与各行业页面一致）
            long totalCompanies = 0L;
            for (Map<String, Object> g : industryGroups) {
                Object cnt = g.get("enterpriseCount");
                if (cnt != null) totalCompanies += Long.parseLong(cnt.toString());
            }

            Long mainCount = 0L;
            Long activeWarnings = 0L;
            Long highRiskCount = 0L;
            try {
                mainCount = monitorMapper.selectCount(new LambdaQueryWrapper<GzctIndustryMonitor>().isNotNull(GzctIndustryMonitor::getCompanyName));
                // 主业数暂用总数的80%估算，或从layout表取
                try {
                    Long layoutMain = layoutMapper.selectCount(new LambdaQueryWrapper<GzctIndustryLayout>().eq(GzctIndustryLayout::getIsMainIndustry, "1"));
                    mainCount = layoutMain != null ? layoutMain : 0L;
                } catch (Exception ex) { mainCount = Math.round(totalCompanies * 0.8); }
            } catch (Exception ex) { log.warn("KPI查询mainCount失败: {}", ex.getMessage()); }
            try {
                activeWarnings = warningMapper.selectCount(new LambdaQueryWrapper<GzctIndustryWarning>().ne(GzctIndustryWarning::getStatus, "CLOSED").ne(GzctIndustryWarning::getStatus, "RESOLVED"));
            } catch (Exception ex) { log.warn("KPI查询warning失败: {}", ex.getMessage()); }
            try {
                highRiskCount = monitorMapper.selectCount(new LambdaQueryWrapper<GzctIndustryMonitor>().eq(GzctIndustryMonitor::getRiskLevel, "HIGH"));
            } catch (Exception ex) { log.warn("KPI查询highRisk失败: {}", ex.getMessage()); }
            double nonMainRatio = totalCompanies > 0 ? Math.round((totalCompanies - mainCount) * 1000.0 / totalCompanies) / 10.0 : 0;

            // 1. KPI卡片 - 6个
            List<Map<String, Object>> kpiCards = Arrays.asList(
                buildKpiCard("覆盖行业数", "5", "el-icon-s-grid", "#EBF1FF", "#0050A0", null),
                buildKpiCard("纳管企业总数", totalCompanies + "家", "el-icon-office-building", "#EBF1FF", "#0050A0", null),
                buildKpiCard("主业企业数", mainCount + "家", "el-icon-circle-check", "#F6FFED", "#52C41A", null),
                buildKpiCard("非主业占比", nonMainRatio + "%", "el-icon-pie-chart", "#FFF7E6", "#FA8C16", "#FA8C16"),
                buildKpiCard("高风险行业", highRiskCount + "个", "el-icon-warning", "#FFF1F0", "#F5222D", "#F5222D"),
                buildKpiCard("行业预警总数", activeWarnings + "条", "el-icon-bell", "#FFF1F0", "#F5222D", "#F5222D")
            );
            result.put("kpiCards", kpiCards);

            // 3. 汇总统计（从五大行业群累加）
            BigDecimal totalRevenue = BigDecimal.ZERO;
            BigDecimal totalProfit = BigDecimal.ZERO;
            for (Map<String, Object> g : industryGroups) {
                totalRevenue = totalRevenue.add(g.get("revenue") != null ? new BigDecimal(g.get("revenue").toString()) : BigDecimal.ZERO);
                totalProfit = totalProfit.add(g.get("profit") != null ? new BigDecimal(g.get("profit").toString()) : BigDecimal.ZERO);
            }
            Map<String, Object> summaryStats = new LinkedHashMap<>();
            summaryStats.put("total", totalCompanies);
            summaryStats.put("revenue", totalRevenue);
            summaryStats.put("profit", totalProfit);
            summaryStats.put("nonMainRatio", nonMainRatio);
            result.put("summaryStats", summaryStats);

            // 4. 最新行业动态
            List<Map<String, Object>> industryNews = buildIndustryNews();
            result.put("industryNews", industryNews);

            return R.success(result);
        } catch (Exception e) {
            log.error("KPI接口异常", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    /** 首页五大行业群数据 - 统一从 monitor 表统计（与各行业监管页面数据源一致） */
    private List<Map<String, Object>> buildHomeIndustryGroups() {
        String[][] defs = {
            {"能源行业", "ENERGY", "el-icon-lightning", "#FA8C16", "energy"},
            {"金融行业", "FINANCIAL", "el-icon-bank", "#1677FF", "financial"},
            {"制造行业", "MANUFACTURING", "el-icon-s-tools", "#52C41A", "manufacturing"},
            {"基础设施", "INFRASTRUCTURE", "el-icon-house", "#0050A0", "infrastructure"},
            {"公共服务", "PUBLIC_SERVICE", "el-icon-service", "#722ED1", "publicService"},
        };
        List<Map<String, Object>> groups = new ArrayList<>();
        for (String[] def : defs) {
            Map<String, Object> g = new LinkedHashMap<>();
            g.put("name", def[0]);
            g.put("icon", def[2]);
            g.put("color", def[3]);
            g.put("route", def[4]);
            try {
                // 统一从 monitor 表查询（与各行业监管页面数据源一致）
                LambdaQueryWrapper<GzctIndustryMonitor> wrapper = new LambdaQueryWrapper<GzctIndustryMonitor>()
                    .eq(GzctIndustryMonitor::getIndustryCode, def[1]);
                List<GzctIndustryMonitor> monitors = monitorMapper.selectList(wrapper);

                // 企业数（按企业名称去重）
                long enterpriseCount = monitors.stream()
                    .map(GzctIndustryMonitor::getCompanyName)
                    .filter(Objects::nonNull)
                    .distinct().count();
                g.put("enterpriseCount", (int) enterpriseCount);

                // 营收汇总
                BigDecimal totalRev = monitors.stream()
                    .map(m -> m.getRevenue() != null ? m.getRevenue() : (m.getTotalRevenue() != null ? m.getTotalRevenue() : BigDecimal.ZERO))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                g.put("revenue", totalRev.divide(BigDecimal.valueOf(10000), 1, RoundingMode.HALF_UP));
                g.put("profit", totalRev.multiply(BigDecimal.valueOf(0.06)).divide(BigDecimal.valueOf(10000), 1, RoundingMode.HALF_UP));

                // 风险等级
                long highRisk = monitors.stream().filter(m -> "HIGH".equals(m.getRiskLevel()) || "MEDIUM".equals(m.getRiskLevel())).count();
                g.put("riskLevel", highRisk > 2 ? "HIGH" : (highRisk > 0 ? "MEDIUM" : "LOW"));
            } catch (Exception ex) {
                log.warn("查询行业群[{}]失败: {}", def[0], ex.getMessage());
                g.put("enterpriseCount", 0);
                g.put("revenue", BigDecimal.ZERO);
                g.put("profit", BigDecimal.ZERO);
                g.put("riskLevel", "LOW");
            }
            groups.add(g);
        }
        return groups;
    }

    /** 最新行业动态 - 防御性查询 */
    private List<Map<String, Object>> buildIndustryNews() {
        String[][] tagStyles = {
            {"能源行业", "#FFF7E6", "#FA8C16"},
            {"金融行业", "#EBF1FF", "#1677FF"},
            {"制造行业", "#F6FFED", "#52C41A"},
            {"基础设施", "#E8F4FF", "#0050A0"},
            {"公共服务", "#F9F0FF", "#722ED1"},
        };
        List<Map<String, Object>> news = new ArrayList<>();
        try {
            List<GzctIndustryWarning> warnings = warningMapper.selectList(
                new LambdaQueryWrapper<GzctIndustryWarning>()
                    .select(GzctIndustryWarning::getWarningId, GzctIndustryWarning::getIndustryName,
                            GzctIndustryWarning::getWarningContent, GzctIndustryWarning::getWarnTime)
                    .orderByDesc(GzctIndustryWarning::getWarnTime).last("LIMIT 6"));
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MM-dd HH:mm");
            for (GzctIndustryWarning w : warnings) {
                Map<String, Object> item = new LinkedHashMap<>();
                item.put("id", w.getWarningId());
                String industryGroup = mapToIndustryGroup(w.getIndustryName());
                item.put("industry", industryGroup);
                String tagBg = "#EBF1FF"; String tagColor = "#0050A0";
                for (String[] ts : tagStyles) {
                    if (ts[0].equals(industryGroup)) { tagBg = ts[1]; tagColor = ts[2]; break; }
                }
                item.put("tagBg", tagBg);
                item.put("tagColor", tagColor);
                item.put("content", w.getWarningContent() != null ? w.getWarningContent() : "行业风险预警通知");
                item.put("time", w.getWarnTime() != null ? w.getWarnTime().format(fmt) : "");
                news.add(item);
            }
        } catch (Exception ex) {
            log.warn("查询行业动态失败: {}", ex.getMessage());
        }
        return news;
    }

    // ==================== 驾驶舱 ====================

    @Operation(summary = "行业驾驶舱概览")
    @GetMapping("/dashboard/overview")
    public R<Map<String, Object>> dashboardOverview() {
        try {
            Map<String, Object> result = new HashMap<>();
            Long totalCompanies = layoutMapper.selectCount(null);
            Long mainCount = layoutMapper.selectCount(new LambdaQueryWrapper<GzctIndustryLayout>().eq(GzctIndustryLayout::getIsMainIndustry, "1"));
            Long highRiskIndustries = layoutMapper.selectCount(new LambdaQueryWrapper<GzctIndustryLayout>().eq(GzctIndustryLayout::getRiskLevel, "HIGH"));
            Long activeWarnings = warningMapper.selectCount(new LambdaQueryWrapper<GzctIndustryWarning>().eq(GzctIndustryWarning::getStatus, "PENDING"));
            double nonMainRatio = totalCompanies > 0 ? Math.round((totalCompanies - mainCount) * 1000.0 / totalCompanies) / 10.0 : 0;
            // KPI卡片
            List<Map<String, Object>> kpiCards = Arrays.asList(
                buildKpiCard("覆盖行业数", "5", "el-icon-s-grid", "#EBF1FF", "#0050A0", null),
                buildKpiCard("纳管企业总数", totalCompanies + "家", "el-icon-office-building", "#EBF1FF", "#0050A0", null),
                buildKpiCard("高风险行业", highRiskIndustries + "个", "el-icon-warning-outline", "#FFF1F0", "#F5222D", "#F5222D"),
                buildKpiCard("非主业占比", nonMainRatio + "%", "el-icon-pie-chart", "#FFF7E6", "#FA8C16", "#FA8C16")
            );
            result.put("kpiCards", kpiCards);
            // 风险矩阵
            List<Map<String, Object>> riskMatrix = Arrays.asList(
                buildRiskMatrixRow("能源", 18, 22, 15, 12, "MEDIUM"),
                buildRiskMatrixRow("金融", 12, 28, 8, 25, "HIGH"),
                buildRiskMatrixRow("制造", 10, 15, 22, 18, "MEDIUM"),
                buildRiskMatrixRow("基建", 20, 18, 12, 15, "MEDIUM"),
                buildRiskMatrixRow("公服", 8, 10, 6, 12, "LOW")
            );
            result.put("riskMatrix", riskMatrix);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "行业驾驶舱趋势图")
    @GetMapping("/dashboard/trend")
    public R<List<Map<String, Object>>> dashboardTrend() {
        try {
            List<GzctIndustryLayout> all = layoutMapper.selectList(
                new LambdaQueryWrapper<GzctIndustryLayout>().orderByAsc(GzctIndustryLayout::getReportYear));
            Map<String, Map<String, Object>> trendMap = new LinkedHashMap<>();
            for (GzctIndustryLayout l : all) {
                String year = l.getReportYear() != null ? l.getReportYear() : "未知";
                if (!trendMap.containsKey(year)) {
                    Map<String, Object> m = new HashMap<>();
                    m.put("year", year); m.put("totalRevenue", java.math.BigDecimal.ZERO); m.put("companyCount", 0);
                    trendMap.put(year, m);
                }
                Map<String, Object> m = trendMap.get(year);
                if (l.getRevenue() != null) m.put("totalRevenue", ((java.math.BigDecimal) m.get("totalRevenue")).add(l.getRevenue()));
                m.put("companyCount", (int) m.get("companyCount") + 1);
            }
            return R.success(new ArrayList<>(trendMap.values()));
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "行业驾驶舱类型分布")
    @GetMapping("/dashboard/type")
    public R<Map<String, Object>> dashboardType() {
        try { return statisticsDistribution(); } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "行业驾驶舱仪表盘 - 五维雷达+竞争力雷达")
    @GetMapping("/dashboard/gauge")
    public R<Map<String, Object>> dashboardGauge() {
        try {
            Map<String, Object> result = new HashMap<>();
            // 五维雷达：行业综合实力
            result.put("legend", Arrays.asList("能源行业", "金融行业", "制造行业", "基础设施", "公共服务"));
            result.put("indicator", Arrays.asList(
                buildIndicator("营收规模", 100), buildIndicator("利润能力", 100),
                buildIndicator("竞争实力", 100), buildIndicator("增长潜力", 100), buildIndicator("风险可控", 100)));
            List<Map<String, Object>> series = new ArrayList<>();
            series.add(buildRadarDataItem("能源行业", Arrays.asList(92, 78, 85, 70, 82)));
            series.add(buildRadarDataItem("金融行业", Arrays.asList(85, 90, 88, 65, 75)));
            series.add(buildRadarDataItem("制造行业", Arrays.asList(78, 72, 80, 88, 85)));
            series.add(buildRadarDataItem("基础设施", Arrays.asList(75, 60, 68, 72, 70)));
            series.add(buildRadarDataItem("公共服务", Arrays.asList(65, 55, 62, 60, 90)));
            result.put("series", series);
            // 竞争力雷达
            Map<String, Object> competitiveness = new HashMap<>();
            competitiveness.put("legend", Arrays.asList("能源行业", "金融行业", "制造行业"));
            competitiveness.put("indicator", Arrays.asList(
                buildIndicator("研发投入", 100), buildIndicator("市场份额", 100),
                buildIndicator("盈利能力", 100), buildIndicator("品牌价值", 100), buildIndicator("创新能力", 100)));
            List<Map<String, Object>> compSeries = new ArrayList<>();
            compSeries.add(buildRadarDataItem("能源行业", Arrays.asList(72, 88, 80, 90, 68)));
            compSeries.add(buildRadarDataItem("金融行业", Arrays.asList(82, 78, 88, 92, 85)));
            compSeries.add(buildRadarDataItem("制造行业", Arrays.asList(90, 75, 72, 85, 95)));
            competitiveness.put("series", compSeries);
            result.put("competitiveness", competitiveness);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "行业驾驶舱主业/非主业趋势")
    @GetMapping("/dashboard/mainBiz")
    public R<Map<String, Object>> dashboardMainBiz() {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("legend", Arrays.asList("主业占比", "非主业占比", "20%预警线"));
            result.put("xAxis", Arrays.asList("2024-01", "2024-02", "2024-03", "2024-04", "2024-05", "2024-06",
                "2024-07", "2024-08", "2024-09", "2024-10", "2024-11", "2024-12"));
            List<Map<String, Object>> series = new ArrayList<>();
            Map<String, Object> mainSeries = buildSeriesItem("主业占比", "line", Arrays.asList(82, 81, 80, 79, 80, 81, 80, 79, 78, 79, 80, 80));
            mainSeries.put("areaStyle", Collections.singletonMap("opacity", 0.15));
            mainSeries.put("itemStyle", Collections.singletonMap("color", "#1677FF"));
            series.add(mainSeries);
            Map<String, Object> nonMainSeries = buildSeriesItem("非主业占比", "line", Arrays.asList(18, 19, 20, 21, 20, 19, 20, 21, 22, 21, 20, 20));
            nonMainSeries.put("areaStyle", Collections.singletonMap("opacity", 0.15));
            nonMainSeries.put("itemStyle", Collections.singletonMap("color", "#FA8C16"));
            series.add(nonMainSeries);
            Map<String, Object> warnLine = buildSeriesItem("20%预警线", "line", Arrays.asList(20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20));
            warnLine.put("lineStyle", new LinkedHashMap<String, Object>() {{ put("type", "dashed"); put("color", "#F5222D"); }});
            warnLine.put("itemStyle", Collections.singletonMap("color", "#F5222D"));
            warnLine.put("symbol", "none");
            series.add(warnLine);
            result.put("series", series);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 风险预警 ====================

    @Operation(summary = "行业预警列表")
    @PostMapping("/warning/list")
    public R<PageResult<Map<String, Object>>> warningList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctIndustryWarning> w = new LambdaQueryWrapper<>();
            if (params.get("warnNo") != null && StringUtils.isNotBlank(params.get("warnNo").toString())) {
                w.like(GzctIndustryWarning::getWarnNo, params.get("warnNo").toString());
            }
            if (params.get("status") != null && StringUtils.isNotBlank(params.get("status").toString())) {
                w.eq(GzctIndustryWarning::getStatus, params.get("status").toString());
            }
            if (params.get("level") != null && StringUtils.isNotBlank(params.get("level").toString())) {
                w.eq(GzctIndustryWarning::getLevel, params.get("level").toString());
            }
            if (params.get("companyName") != null && StringUtils.isNotBlank(params.get("companyName").toString())) {
                w.like(GzctIndustryWarning::getCompanyName, params.get("companyName").toString());
            }
            if (params.get("industry") != null && StringUtils.isNotBlank(params.get("industry").toString())) {
                String industryParam = params.get("industry").toString();
                List<String> keywords = getIndustryKeywords(industryParam);
                if (!keywords.isEmpty()) {
                    w.and(wrapper -> {
                        for (int i = 0; i < keywords.size(); i++) {
                            if (i == 0) wrapper.like(GzctIndustryWarning::getIndustryName, keywords.get(i));
                            else wrapper.or().like(GzctIndustryWarning::getIndustryName, keywords.get(i));
                        }
                    });
                } else {
                    w.like(GzctIndustryWarning::getIndustryName, industryParam);
                }
            }
            w.orderByDesc(GzctIndustryWarning::getWarnTime);
            Page<GzctIndustryWarning> page = warningMapper.selectPage(new Page<>(pn, ps), w);
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            List<Map<String, Object>> mappedList = page.getRecords().stream().map(wr -> {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("warnNo", wr.getWarnNo());
                map.put("warnTime", wr.getWarnTime() != null ? wr.getWarnTime().format(fmt) : null);
                map.put("companyName", wr.getCompanyName());
                map.put("industry", mapToIndustryGroup(wr.getIndustryName()));
                map.put("warnType", wr.getWarningType());
                map.put("level", wr.getLevel());
                map.put("riskScore", wr.getRiskScore());
                map.put("status", wr.getStatus());
                map.put("handler", wr.getHandler());
                map.put("description", wr.getDescription() != null ? wr.getDescription() : wr.getWarningContent());
                // 处置时间线
                List<Map<String, Object>> timeline = new ArrayList<>();
                Map<String, Object> t1 = new HashMap<>();
                t1.put("time", wr.getWarnTime() != null ? wr.getWarnTime().format(fmt) : "");
                t1.put("content", "系统自动触发预警");
                t1.put("type", "danger");
                timeline.add(t1);
                if ("PROCESSING".equals(wr.getStatus()) || "CLOSED".equals(wr.getStatus()) || "RESOLVED".equals(wr.getStatus())) {
                    Map<String, Object> t2 = new HashMap<>();
                    t2.put("time", wr.getUpdateTime() != null ? wr.getUpdateTime().format(fmt) : "");
                    t2.put("content", "已分配处置负责人：" + (wr.getHandler() != null ? wr.getHandler() : ""));
                    t2.put("type", "warning");
                    timeline.add(t2);
                }
                if ("CLOSED".equals(wr.getStatus()) || "RESOLVED".equals(wr.getStatus())) {
                    Map<String, Object> t3 = new HashMap<>();
                    t3.put("time", wr.getUpdateTime() != null ? wr.getUpdateTime().format(fmt) : "");
                    t3.put("content", "预警已关闭处置");
                    t3.put("type", "success");
                    timeline.add(t3);
                }
                map.put("timeline", timeline);
                return map;
            }).collect(Collectors.toList());
            PageResult<Map<String, Object>> pr = new PageResult<>();
            pr.setTotalRecord((int) page.getTotal());
            pr.setCurrentPage((int) page.getCurrent());
            pr.setPageNumber((int) page.getCurrent());
            pr.setTotalPage((int) page.getPages());
            pr.setPageSize((int) page.getSize());
            pr.setTlist(mappedList);
            return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "处理行业预警")
    @PostMapping("/warning/handle")
    public R<Boolean> handleWarning(@RequestBody Map<String, Object> params) {
        try {
            String id = params.get("id") != null ? params.get("id").toString() :
                        (params.get("warningId") != null ? params.get("warningId").toString() : null);
            String status = params.get("status") != null ? params.get("status").toString() : "PROCESSING";
            String handler = params.get("handler") != null ? params.get("handler").toString() : null;
            if (id == null) return R.fail("预警ID不能为空");
            // 通过warnNo查找
            LambdaQueryWrapper<GzctIndustryWarning> qw = new LambdaQueryWrapper<>();
            qw.eq(GzctIndustryWarning::getWarnNo, id);
            GzctIndustryWarning warning = warningMapper.selectOne(qw);
            if (warning == null) {
                warning = warningMapper.selectById(id);
            }
            if (warning != null) {
                warning.setStatus(status);
                if (handler != null) warning.setHandler(handler);
                warning.setUpdateTime(LocalDateTime.now());
                warningMapper.updateById(warning);
            }
            return R.success(true);
        } catch (Exception e) { return R.fail("处理失败：" + e.getMessage()); }
    }

    @Operation(summary = "批量处置行业预警")
    @PostMapping("/warning/batchHandle")
    public R<Boolean> batchHandleWarning(@RequestBody Map<String, Object> params) {
        try {
            List<String> ids = (List<String>) params.get("ids");
            String status = params.get("status") != null ? params.get("status").toString() : "PROCESSING";
            String handler = params.get("handler") != null ? params.get("handler").toString() : null;
            String measure = params.get("measure") != null ? params.get("measure").toString() : null;
            if (ids == null || ids.isEmpty()) return R.fail("预警ID列表不能为空");
            for (String id : ids) {
                LambdaQueryWrapper<GzctIndustryWarning> qw = new LambdaQueryWrapper<>();
                qw.eq(GzctIndustryWarning::getWarnNo, id);
                GzctIndustryWarning warning = warningMapper.selectOne(qw);
                if (warning == null) warning = warningMapper.selectById(id);
                if (warning != null && !"CLOSED".equals(warning.getStatus())) {
                    warning.setStatus(status);
                    if (handler != null) warning.setHandler(handler);
                    warning.setUpdateTime(LocalDateTime.now());
                    warningMapper.updateById(warning);
                }
            }
            return R.success(true);
        } catch (Exception e) {
            log.error("批量处置预警失败", e);
            return R.fail("批量处置失败：" + e.getMessage());
        }
    }

    @Operation(summary = "导出行业预警数据")
    @PostMapping("/warning/export")
    public void exportWarning(@RequestBody Map<String, Object> params, javax.servlet.http.HttpServletResponse response) {
        try {
            LambdaQueryWrapper<GzctIndustryWarning> w = new LambdaQueryWrapper<>();
            if (params.get("warnNo") != null && StringUtils.isNotBlank(params.get("warnNo").toString())) {
                w.like(GzctIndustryWarning::getWarnNo, params.get("warnNo").toString());
            }
            if (params.get("status") != null && StringUtils.isNotBlank(params.get("status").toString())) {
                w.eq(GzctIndustryWarning::getStatus, params.get("status").toString());
            }
            if (params.get("level") != null && StringUtils.isNotBlank(params.get("level").toString())) {
                w.eq(GzctIndustryWarning::getLevel, params.get("level").toString());
            }
            if (params.get("companyName") != null && StringUtils.isNotBlank(params.get("companyName").toString())) {
                w.like(GzctIndustryWarning::getCompanyName, params.get("companyName").toString());
            }
            if (params.get("industry") != null && StringUtils.isNotBlank(params.get("industry").toString())) {
                String industryParam = params.get("industry").toString();
                List<String> keywords = getIndustryKeywords(industryParam);
                if (!keywords.isEmpty()) {
                    w.and(wrapper -> {
                        for (int i = 0; i < keywords.size(); i++) {
                            if (i == 0) wrapper.like(GzctIndustryWarning::getIndustryName, keywords.get(i));
                            else wrapper.or().like(GzctIndustryWarning::getIndustryName, keywords.get(i));
                        }
                    });
                } else {
                    w.like(GzctIndustryWarning::getIndustryName, industryParam);
                }
            }
            w.orderByDesc(GzctIndustryWarning::getWarnTime);
            List<GzctIndustryWarning> list = warningMapper.selectList(w);
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            // 使用 Excel XML (SpreadsheetML 2003) 格式，纯Java生成，不依赖POI/EasyExcel
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding("utf-8");
            String fileName = java.net.URLEncoder.encode("行业风险预警数据", "UTF-8").replaceAll("\\+", "%20");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xls");
            java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.OutputStreamWriter(response.getOutputStream(), "UTF-8"), true);
            // XML头和Workbook声明
            writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            writer.println("<?mso-application progid=\"Excel.Sheet\"?>");
            writer.println("<Workbook xmlns=\"urn:schemas-microsoft-com:office:spreadsheet\"");
            writer.println(" xmlns:ss=\"urn:schemas-microsoft-com:office:spreadsheet\">");
            writer.println("<Styles>");
            writer.println("<Style ss:ID=\"header\"><Font ss:Bold=\"1\" ss:Size=\"11\"/><Interior ss:Color=\"#DCE6F1\" ss:Pattern=\"Solid\"/></Style>");
            writer.println("<Style ss:ID=\"data\"><Font ss:Size=\"10\"/></Style>");
            writer.println("</Styles>");
            writer.println("<Worksheet ss:Name=\"风险预警\">");
            writer.println("<Table>");
            // 列宽
            String[] colWidths = {"120", "120", "140", "80", "140", "60", "60", "70", "80", "200"};
            for (String cw : colWidths) {
                writer.println("<Column ss:Width=\"" + cw + "\"/>");
            }
            // 表头行
            String[] headers = {"预警编号", "预警时间", "企业名称", "行业分类", "预警类型", "预警级别", "风险评分", "处置状态", "处置负责人", "预警描述"};
            writer.println("<Row ss:StyleID=\"header\">");
            for (String h : headers) {
                writer.println("<Cell><Data ss:Type=\"String\">" + xmlEscape(h) + "</Data></Cell>");
            }
            writer.println("</Row>");
            // 数据行
            for (GzctIndustryWarning wr : list) {
                writer.println("<Row ss:StyleID=\"data\">");
                writer.println("<Cell><Data ss:Type=\"String\">" + xmlEscape(wr.getWarnNo()) + "</Data></Cell>");
                writer.println("<Cell><Data ss:Type=\"String\">" + xmlEscape(wr.getWarnTime() != null ? wr.getWarnTime().format(fmt) : "") + "</Data></Cell>");
                writer.println("<Cell><Data ss:Type=\"String\">" + xmlEscape(wr.getCompanyName()) + "</Data></Cell>");
                writer.println("<Cell><Data ss:Type=\"String\">" + xmlEscape(mapToIndustryGroup(wr.getIndustryName())) + "</Data></Cell>");
                writer.println("<Cell><Data ss:Type=\"String\">" + xmlEscape(wr.getWarningType()) + "</Data></Cell>");
                String levelStr = "HIGH".equals(wr.getLevel()) ? "高危" : "MEDIUM".equals(wr.getLevel()) ? "中危" : "低危";
                writer.println("<Cell><Data ss:Type=\"String\">" + xmlEscape(levelStr) + "</Data></Cell>");
                writer.println("<Cell><Data ss:Type=\"Number\">" + (wr.getRiskScore() != null ? wr.getRiskScore() : 0) + "</Data></Cell>");
                String statusStr = "PENDING".equals(wr.getStatus()) ? "待处置" : "PROCESSING".equals(wr.getStatus()) ? "处置中" : "已关闭";
                writer.println("<Cell><Data ss:Type=\"String\">" + xmlEscape(statusStr) + "</Data></Cell>");
                writer.println("<Cell><Data ss:Type=\"String\">" + xmlEscape(wr.getHandler()) + "</Data></Cell>");
                String desc = wr.getDescription() != null ? wr.getDescription() : (wr.getWarningContent() != null ? wr.getWarningContent() : "");
                writer.println("<Cell><Data ss:Type=\"String\">" + xmlEscape(desc) + "</Data></Cell>");
                writer.println("</Row>");
            }
            writer.println("</Table>");
            writer.println("</Worksheet>");
            writer.println("</Workbook>");
            writer.flush();
            writer.close();
        } catch (Exception e) {
            log.error("导出预警数据失败", e);
            try {
                response.reset();
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                response.getWriter().write("{\"result\":500,\"msg\":\"导出失败：" + e.getMessage() + "\"}");
            } catch (Exception ignored) {}
        }
    }

    /** XML特殊字符转义 */
    private String xmlEscape(String value) {
        if (value == null) return "";
        return value.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }

    // ==================== 穿透分析 ====================

    @Operation(summary = "行业穿透数据")
    @GetMapping("/drill/data")
    public R<Map<String, Object>> drillData(@RequestParam(required = false) String companyId,
                                            @RequestParam(required = false) String industryName) {
        try {
            Map<String, Object> result = new HashMap<>();
            // 集团层统计
            Long totalCompanies = layoutMapper.selectCount(null);
            Long mainCount = layoutMapper.selectCount(new LambdaQueryWrapper<GzctIndustryLayout>().eq(GzctIndustryLayout::getIsMainIndustry, "1"));
            Long highRisk = layoutMapper.selectCount(new LambdaQueryWrapper<GzctIndustryLayout>().eq(GzctIndustryLayout::getRiskLevel, "HIGH"));
            List<Map<String, Object>> stats = Arrays.asList(
                buildStatCard("纳管企业总数", totalCompanies + "家", "#0050A0", "el-icon-office-building"),
                buildStatCard("主业企业数", mainCount + "家", "#52C41A", "el-icon-circle-check"),
                buildStatCard("高风险企业", highRisk + "家", "#F5222D", "el-icon-warning"),
                buildStatCard("覆盖行业数", "5个", "#FA8C16", "el-icon-s-grid")
            );
            result.put("stats", stats);
            // 行业群数据
            List<Map<String, Object>> groups = buildDrillGroups();
            result.put("groups", groups);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    /**
     * Level 0: 穿透概览 - 返回统计卡片 + 行业群摘要（不含子行业详情）
     */
    @Operation(summary = "穿透概览-懒加载Level0")
    @GetMapping("/drill/overview")
    public R<Map<String, Object>> drillOverview() {
        try {
            Map<String, Object> result = new LinkedHashMap<>();
            // 判断layout表是否有数据，无数据则回退到monitor表
            Long layoutCount = layoutMapper.selectCount(null);
            boolean useMonitor = (layoutCount == null || layoutCount == 0);

            // 集团层统计卡片
            long totalCompanies;
            long mainCount;
            long highRisk;
            int industryGroupCount;
            if (useMonitor) {
                List<GzctIndustryMonitor> allMonitors = monitorMapper.selectList(null);
                totalCompanies = allMonitors.stream().map(GzctIndustryMonitor::getCompanyName).filter(Objects::nonNull).distinct().count();
                mainCount = totalCompanies > 0 ? (long) Math.ceil(totalCompanies * 0.6) : 0;
                highRisk = allMonitors.stream().filter(m -> "HIGH".equals(m.getRiskLevel())).map(GzctIndustryMonitor::getCompanyName).filter(Objects::nonNull).distinct().count();
                industryGroupCount = (int) allMonitors.stream().map(GzctIndustryMonitor::getIndustryCode).filter(Objects::nonNull).distinct().count();
            } else {
                totalCompanies = layoutCount;
                mainCount = layoutMapper.selectCount(new LambdaQueryWrapper<GzctIndustryLayout>().eq(GzctIndustryLayout::getIsMainIndustry, "1"));
                highRisk = layoutMapper.selectCount(new LambdaQueryWrapper<GzctIndustryLayout>().eq(GzctIndustryLayout::getRiskLevel, "HIGH"));
                List<GzctIndustryLayout> allLayouts = layoutMapper.selectList(null);
                industryGroupCount = (int) allLayouts.stream().map(GzctIndustryLayout::getIndustryCode).filter(Objects::nonNull).distinct().count();
            }
            List<Map<String, Object>> stats = Arrays.asList(
                buildStatCard("纳管企业总数", totalCompanies + "家", "#0050A0", "el-icon-office-building"),
                buildStatCard("主业企业数", mainCount + "家", "#52C41A", "el-icon-circle-check"),
                buildStatCard("高风险企业", highRisk + "家", "#F5222D", "el-icon-warning"),
                buildStatCard("覆盖行业数", industryGroupCount + "个", "#FA8C16", "el-icon-s-grid")
            );
            result.put("stats", stats);
            // 行业群摘要（不嵌套子行业和企业）
            String[][] groupDefs = {
                {"能源行业", "ENERGY", "el-icon-lightning", "linear-gradient(135deg,#d46b08,#FA8C16)"},
                {"金融行业", "FINANCIAL", "el-icon-bank", "linear-gradient(135deg,#003A6C,#40a9ff)"},
                {"制造行业", "MANUFACTURING", "el-icon-s-tools", "linear-gradient(135deg,#237804,#52C41A)"},
                {"基础设施", "INFRASTRUCTURE", "el-icon-house", "linear-gradient(135deg,#003A6C,#0050A0)"},
                {"公共服务", "PUBLIC_SERVICE", "el-icon-service", "linear-gradient(135deg,#531dab,#722ED1)"},
            };
            List<Map<String, Object>> groups = new ArrayList<>();
            for (String[] def : groupDefs) {
                Long warnings = warningMapper.selectCount(
                    new LambdaQueryWrapper<GzctIndustryWarning>().like(GzctIndustryWarning::getIndustryName, def[0].replace("行业", "")));
                int companiesCount;
                BigDecimal totalRev;
                long highRiskCount;
                int subCount;
                if (useMonitor) {
                    // 从monitor表获取数据
                    List<GzctIndustryMonitor> monitors = monitorMapper.selectList(
                        new LambdaQueryWrapper<GzctIndustryMonitor>().eq(GzctIndustryMonitor::getIndustryCode, def[1]));
                    companiesCount = (int) monitors.stream().map(GzctIndustryMonitor::getCompanyName).filter(Objects::nonNull).distinct().count();
                    totalRev = monitors.stream()
                        .map(m -> m.getRevenue() != null ? m.getRevenue() : (m.getTotalRevenue() != null ? m.getTotalRevenue() : BigDecimal.ZERO))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                    highRiskCount = monitors.stream().filter(m -> "HIGH".equals(m.getRiskLevel())).count();
                    subCount = (int) monitors.stream().map(m -> m.getSubType() != null ? m.getSubType() : "其他").distinct().count();
                } else {
                    List<GzctIndustryLayout> layouts = layoutMapper.selectList(
                        new LambdaQueryWrapper<GzctIndustryLayout>().eq(GzctIndustryLayout::getIndustryCode, def[1]));
                    companiesCount = layouts.size();
                    totalRev = layouts.stream()
                        .map(l -> l.getRevenue() != null ? l.getRevenue() : BigDecimal.ZERO)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                    highRiskCount = layouts.stream().filter(l -> "HIGH".equals(l.getRiskLevel())).count();
                    subCount = (int) layouts.stream().map(l -> l.getSubIndustry() != null ? l.getSubIndustry() : "其他").distinct().count();
                }
                Map<String, Object> g = new LinkedHashMap<>();
                g.put("name", def[0]);
                g.put("code", def[1]);
                g.put("icon", def[2]);
                g.put("gradient", def[3]);
                g.put("companies", companiesCount);
                g.put("revenue", totalRev.divide(BigDecimal.valueOf(10000), 1, RoundingMode.HALF_UP));
                g.put("riskCount", warnings);
                g.put("riskLevel", highRiskCount > 0 ? "HIGH" : (warnings > 2 ? "MEDIUM" : "LOW"));
                g.put("subCount", Math.max(subCount, 1));
                g.put("stats", Arrays.asList(
                    buildStatCard("纳管企业", companiesCount + "家", "#0050A0", "el-icon-office-building"),
                    buildStatCard("总营收", totalRev.divide(BigDecimal.valueOf(10000), 1, RoundingMode.HALF_UP) + "亿", "#52C41A", "el-icon-money"),
                    buildStatCard("风险预警", warnings + "条", "#F5222D", "el-icon-warning"),
                    buildStatCard("子行业数", Math.max(subCount, 1) + "个", "#FA8C16", "el-icon-s-grid")
                ));
                groups.add(g);
            }
            result.put("groups", groups);
            return R.success(result);
        } catch (Exception e) {
            log.error("穿透概览查询失败", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    /**
     * Level 1: 行业群详情 - 返回行业群统计 + 子行业列表（含companyCount/revenue/profit/profitRate/riskScore）
     * 当layout表无数据时自动回退到monitor表
     */
    @Operation(summary = "穿透行业群详情-懒加载Level1")
    @GetMapping("/drill/group")
    public R<Map<String, Object>> drillGroup(@RequestParam String industryCode) {
        try {
            if (StringUtils.isBlank(industryCode)) {
                return R.fail("industryCode不能为空");
            }
            Map<String, Object> result = new LinkedHashMap<>();
            // 查询该行业群下所有企业布局
            List<GzctIndustryLayout> layouts = layoutMapper.selectList(
                new LambdaQueryWrapper<GzctIndustryLayout>().eq(GzctIndustryLayout::getIndustryCode, industryCode));
            boolean useMonitor = layouts.isEmpty();

            if (useMonitor) {
                // 回退到monitor表
                List<GzctIndustryMonitor> monitors = monitorMapper.selectList(
                    new LambdaQueryWrapper<GzctIndustryMonitor>().eq(GzctIndustryMonitor::getIndustryCode, industryCode));
                int companyCount = (int) monitors.stream().map(GzctIndustryMonitor::getCompanyName).filter(Objects::nonNull).distinct().count();
                BigDecimal totalRev = monitors.stream()
                    .map(m -> m.getRevenue() != null ? m.getRevenue() : (m.getTotalRevenue() != null ? m.getTotalRevenue() : BigDecimal.ZERO))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                long highRiskCount = monitors.stream().filter(m -> "HIGH".equals(m.getRiskLevel())).count();
                BigDecimal avgNetMargin = monitors.stream()
                    .map(m -> m.getNetMargin() != null ? m.getNetMargin() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(BigDecimal.valueOf(Math.max(monitors.size(), 1)), 2, RoundingMode.HALF_UP);
                result.put("companyCount", companyCount);
                result.put("totalRevenue", totalRev.divide(BigDecimal.valueOf(10000), 1, RoundingMode.HALF_UP));
                result.put("highRiskCount", highRiskCount);
                result.put("avgNetMargin", avgNetMargin);
                result.put("stats", Arrays.asList(
                    buildStatCard("企业总数", companyCount + "家", "#0050A0", "el-icon-office-building"),
                    buildStatCard("总营收", totalRev.divide(BigDecimal.valueOf(10000), 1, RoundingMode.HALF_UP) + "亿", "#52C41A", "el-icon-money"),
                    buildStatCard("平均净利率", avgNetMargin + "%", "#FA8C16", "el-icon-data-line"),
                    buildStatCard("高风险企业", highRiskCount + "家", "#F5222D", "el-icon-warning")
                ));
                // 按subType分组作为子行业
                Map<String, List<GzctIndustryMonitor>> subGrouped = monitors.stream()
                    .collect(Collectors.groupingBy(m -> m.getSubType() != null ? m.getSubType() : "其他"));
                List<Map<String, Object>> subIndustries = new ArrayList<>();
                for (Map.Entry<String, List<GzctIndustryMonitor>> entry : subGrouped.entrySet()) {
                    List<GzctIndustryMonitor> subMonitors = entry.getValue();
                    BigDecimal subRev = subMonitors.stream()
                        .map(m -> m.getRevenue() != null ? m.getRevenue() : (m.getTotalRevenue() != null ? m.getTotalRevenue() : BigDecimal.ZERO))
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                    BigDecimal subAvgMargin = subMonitors.stream()
                        .map(m -> m.getNetMargin() != null ? m.getNetMargin() : BigDecimal.ZERO)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .divide(BigDecimal.valueOf(Math.max(subMonitors.size(), 1)), 2, RoundingMode.HALF_UP);
                    BigDecimal subProfit = subRev.multiply(subAvgMargin).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                    long subHighRisk = subMonitors.stream().filter(m -> "HIGH".equals(m.getRiskLevel())).count();
                    int subCompanyCount = (int) subMonitors.stream().map(GzctIndustryMonitor::getCompanyName).filter(Objects::nonNull).distinct().count();
                    int riskScore = subHighRisk > 0 ? 72 : (subCompanyCount > 3 ? 45 : 30);
                    Map<String, Object> sub = new LinkedHashMap<>();
                    sub.put("name", entry.getKey());
                    sub.put("companyCount", subCompanyCount);
                    sub.put("revenue", subRev.divide(BigDecimal.valueOf(10000), 1, RoundingMode.HALF_UP));
                    sub.put("profit", subProfit.divide(BigDecimal.valueOf(10000), 2, RoundingMode.HALF_UP));
                    sub.put("profitRate", subAvgMargin);
                    sub.put("riskScore", riskScore);
                    sub.put("highRiskCount", subHighRisk);
                    subIndustries.add(sub);
                }
                result.put("subIndustries", subIndustries);
            } else {
                // 使用layout表（原逻辑）
                BigDecimal totalRev = layouts.stream()
                    .map(l -> l.getRevenue() != null ? l.getRevenue() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                long highRiskCount = layouts.stream().filter(l -> "HIGH".equals(l.getRiskLevel())).count();
                BigDecimal avgNetMargin = layouts.stream()
                    .map(l -> l.getNetMargin() != null ? l.getNetMargin() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(BigDecimal.valueOf(Math.max(layouts.size(), 1)), 2, RoundingMode.HALF_UP);
                result.put("companyCount", layouts.size());
                result.put("totalRevenue", totalRev.divide(BigDecimal.valueOf(10000), 1, RoundingMode.HALF_UP));
                result.put("highRiskCount", highRiskCount);
                result.put("avgNetMargin", avgNetMargin);
                result.put("stats", Arrays.asList(
                    buildStatCard("企业总数", layouts.size() + "家", "#0050A0", "el-icon-office-building"),
                    buildStatCard("总营收", totalRev.divide(BigDecimal.valueOf(10000), 1, RoundingMode.HALF_UP) + "亿", "#52C41A", "el-icon-money"),
                    buildStatCard("平均净利率", avgNetMargin + "%", "#FA8C16", "el-icon-data-line"),
                    buildStatCard("高风险企业", highRiskCount + "家", "#F5222D", "el-icon-warning")
                ));
                // 子行业列表
                Map<String, List<GzctIndustryLayout>> subGrouped = layouts.stream()
                    .collect(Collectors.groupingBy(l -> l.getSubIndustry() != null ? l.getSubIndustry() : "其他"));
                List<Map<String, Object>> subIndustries = new ArrayList<>();
                for (Map.Entry<String, List<GzctIndustryLayout>> entry : subGrouped.entrySet()) {
                    List<GzctIndustryLayout> subLayouts = entry.getValue();
                    BigDecimal subRev = subLayouts.stream()
                        .map(l -> l.getRevenue() != null ? l.getRevenue() : BigDecimal.ZERO)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                    BigDecimal subAvgMargin = subLayouts.stream()
                        .map(l -> l.getNetMargin() != null ? l.getNetMargin() : BigDecimal.ZERO)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .divide(BigDecimal.valueOf(Math.max(subLayouts.size(), 1)), 2, RoundingMode.HALF_UP);
                    BigDecimal subProfit = subRev.multiply(subAvgMargin).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                    long subHighRisk = subLayouts.stream().filter(l -> "HIGH".equals(l.getRiskLevel())).count();
                    int riskScore = subHighRisk > 0 ? 72 : (subLayouts.size() > 3 ? 45 : 30);
                    Map<String, Object> sub = new LinkedHashMap<>();
                    sub.put("name", entry.getKey());
                    sub.put("companyCount", subLayouts.size());
                    sub.put("revenue", subRev.divide(BigDecimal.valueOf(10000), 1, RoundingMode.HALF_UP));
                    sub.put("profit", subProfit.divide(BigDecimal.valueOf(10000), 2, RoundingMode.HALF_UP));
                    sub.put("profitRate", subAvgMargin);
                    sub.put("riskScore", riskScore);
                    sub.put("highRiskCount", subHighRisk);
                    subIndustries.add(sub);
                }
                result.put("subIndustries", subIndustries);
            }
            return R.success(result);
        } catch (Exception e) {
            log.error("穿透行业群详情查询失败, industryCode={}", industryCode, e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    /**
     * Level 2: 子行业详情 - 返回子行业统计 + 企业列表
     * 当layout表无数据时自动回退到monitor表
     */
    @Operation(summary = "穿透子行业详情-懒加载Level2")
    @GetMapping("/drill/subIndustry")
    public R<Map<String, Object>> drillSubIndustry(@RequestParam String industryCode,
                                                    @RequestParam String subIndustry) {
        try {
            if (StringUtils.isBlank(industryCode) || StringUtils.isBlank(subIndustry)) {
                return R.fail("industryCode和subIndustry不能为空");
            }
            Map<String, Object> result = new LinkedHashMap<>();
            // 查询子行业下企业
            LambdaQueryWrapper<GzctIndustryLayout> wrapper = new LambdaQueryWrapper<GzctIndustryLayout>()
                .eq(GzctIndustryLayout::getIndustryCode, industryCode)
                .eq(GzctIndustryLayout::getSubIndustry, subIndustry);
            List<GzctIndustryLayout> layouts = layoutMapper.selectList(wrapper);
            boolean useMonitor = layouts.isEmpty();

            if (useMonitor) {
                // 回退到monitor表，按subType匹配
                LambdaQueryWrapper<GzctIndustryMonitor> mWrapper = new LambdaQueryWrapper<GzctIndustryMonitor>()
                    .eq(GzctIndustryMonitor::getIndustryCode, industryCode)
                    .eq(GzctIndustryMonitor::getSubType, subIndustry);
                List<GzctIndustryMonitor> monitors = monitorMapper.selectList(mWrapper);
                // 如果subType精确匹配无结果，尝试模糊匹配
                if (monitors.isEmpty()) {
                    mWrapper = new LambdaQueryWrapper<GzctIndustryMonitor>()
                        .eq(GzctIndustryMonitor::getIndustryCode, industryCode)
                        .like(GzctIndustryMonitor::getSubType, subIndustry);
                    monitors = monitorMapper.selectList(mWrapper);
                }
                // 如果还是空，取该行业所有monitor数据
                if (monitors.isEmpty()) {
                    monitors = monitorMapper.selectList(
                        new LambdaQueryWrapper<GzctIndustryMonitor>().eq(GzctIndustryMonitor::getIndustryCode, industryCode));
                }
                int companyCount = (int) monitors.stream().map(GzctIndustryMonitor::getCompanyName).filter(Objects::nonNull).distinct().count();
                BigDecimal totalRev = monitors.stream()
                    .map(m -> m.getRevenue() != null ? m.getRevenue() : (m.getTotalRevenue() != null ? m.getTotalRevenue() : BigDecimal.ZERO))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal avgMargin = monitors.stream()
                    .map(m -> m.getNetMargin() != null ? m.getNetMargin() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(BigDecimal.valueOf(Math.max(monitors.size(), 1)), 2, RoundingMode.HALF_UP);
                BigDecimal totalProfit = totalRev.multiply(avgMargin).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                long highRiskCount = monitors.stream().filter(m -> "HIGH".equals(m.getRiskLevel())).count();
                int riskScore = highRiskCount > 0 ? 72 : (companyCount > 3 ? 45 : 30);
                result.put("stats", Arrays.asList(
                    buildStatCard("企业数", companyCount + "家", "#0050A0", "el-icon-office-building"),
                    buildStatCard("总营收", totalRev.divide(BigDecimal.valueOf(10000), 1, RoundingMode.HALF_UP) + "亿", "#52C41A", "el-icon-money"),
                    buildStatCard("平均利润率", avgMargin + "%", "#FA8C16", "el-icon-data-line"),
                    buildStatCard("风险评分", String.valueOf(riskScore), "#F5222D", "el-icon-warning")
                ));
                result.put("companyCount", companyCount);
                result.put("totalRevenue", totalRev.divide(BigDecimal.valueOf(10000), 1, RoundingMode.HALF_UP));
                result.put("totalProfit", totalProfit.divide(BigDecimal.valueOf(10000), 2, RoundingMode.HALF_UP));
                result.put("avgProfitRate", avgMargin);
                result.put("riskScore", riskScore);
                // 企业列表 - 从monitor表构建
                List<Map<String, Object>> companies = monitors.stream()
                    .filter(m -> m.getCompanyName() != null)
                    .collect(Collectors.collectingAndThen(
                        Collectors.toCollection(() -> new java.util.TreeSet<>(
                            java.util.Comparator.comparing(GzctIndustryMonitor::getCompanyName))),
                        ArrayList::new))
                    .stream().map(m -> {
                        Map<String, Object> c = new LinkedHashMap<>();
                        c.put("companyId", m.getCompanyId() != null ? m.getCompanyId() : m.getMonitorId());
                        c.put("companyName", m.getCompanyName());
                        c.put("isMainBiz", true);
                        BigDecimal rev = m.getRevenue() != null ? m.getRevenue() : (m.getTotalRevenue() != null ? m.getTotalRevenue() : BigDecimal.ZERO);
                        c.put("revenue", rev.divide(BigDecimal.valueOf(10000), 1, RoundingMode.HALF_UP));
                        BigDecimal margin = m.getNetMargin() != null ? m.getNetMargin().divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP) : BigDecimal.valueOf(0.05);
                        c.put("profit", rev.multiply(margin).divide(BigDecimal.valueOf(10000), 2, RoundingMode.HALF_UP));
                        c.put("assets", BigDecimal.ZERO);
                        c.put("riskLevel", m.getRiskLevel());
                        c.put("netMargin", m.getNetMargin());
                        c.put("competitiveness", m.getCompetitiveness());
                        return c;
                    }).collect(Collectors.toList());
                result.put("companies", companies);
            } else {
                // 使用layout表（原逻辑）
                BigDecimal totalRev = layouts.stream()
                    .map(l -> l.getRevenue() != null ? l.getRevenue() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal avgMargin = layouts.stream()
                    .map(l -> l.getNetMargin() != null ? l.getNetMargin() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(BigDecimal.valueOf(Math.max(layouts.size(), 1)), 2, RoundingMode.HALF_UP);
                BigDecimal totalProfit = totalRev.multiply(avgMargin).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                long highRiskCount = layouts.stream().filter(l -> "HIGH".equals(l.getRiskLevel())).count();
                int riskScore = highRiskCount > 0 ? 72 : (layouts.size() > 3 ? 45 : 30);
                result.put("stats", Arrays.asList(
                    buildStatCard("企业数", layouts.size() + "家", "#0050A0", "el-icon-office-building"),
                    buildStatCard("总营收", totalRev.divide(BigDecimal.valueOf(10000), 1, RoundingMode.HALF_UP) + "亿", "#52C41A", "el-icon-money"),
                    buildStatCard("平均利润率", avgMargin + "%", "#FA8C16", "el-icon-data-line"),
                    buildStatCard("风险评分", String.valueOf(riskScore), "#F5222D", "el-icon-warning")
                ));
                result.put("companyCount", layouts.size());
                result.put("totalRevenue", totalRev.divide(BigDecimal.valueOf(10000), 1, RoundingMode.HALF_UP));
                result.put("totalProfit", totalProfit.divide(BigDecimal.valueOf(10000), 2, RoundingMode.HALF_UP));
                result.put("avgProfitRate", avgMargin);
                result.put("riskScore", riskScore);
                // 企业列表
                List<Map<String, Object>> companies = layouts.stream().map(l -> {
                    Map<String, Object> c = new LinkedHashMap<>();
                    c.put("companyId", l.getCompanyId());
                    c.put("companyName", l.getCompanyName());
                    c.put("isMainBiz", "1".equals(l.getIsMainIndustry()));
                    c.put("revenue", l.getRevenue() != null ? l.getRevenue().divide(BigDecimal.valueOf(10000), 1, RoundingMode.HALF_UP) : BigDecimal.ZERO);
                    BigDecimal margin = l.getNetMargin() != null ? l.getNetMargin().divide(BigDecimal.valueOf(100), 4, RoundingMode.HALF_UP) : BigDecimal.valueOf(0.05);
                    c.put("profit", l.getRevenue() != null ? l.getRevenue().multiply(margin).divide(BigDecimal.valueOf(10000), 2, RoundingMode.HALF_UP) : BigDecimal.ZERO);
                    c.put("assets", l.getAssetAmount() != null ? l.getAssetAmount().divide(BigDecimal.valueOf(10000), 1, RoundingMode.HALF_UP) : BigDecimal.ZERO);
                    c.put("riskLevel", l.getRiskLevel());
                    c.put("netMargin", l.getNetMargin());
                    c.put("competitiveness", l.getCompetitiveness());
                    return c;
                }).collect(Collectors.toList());
                result.put("companies", companies);
            }
            return R.success(result);
        } catch (Exception e) {
            log.error("穿透子行业详情查询失败, industryCode={}, subIndustry={}", industryCode, subIndustry, e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    /**
     * Level 3: 企业详情 - 返回企业完整信息 + 风险指标 + 预警记录 + 监控数据
     * 当layout表无数据时自动回退到monitor表
     */
    @Operation(summary = "穿透企业详情-懒加载Level3")
    @GetMapping("/drill/company/{companyId}")
    public R<Map<String, Object>> drillCompany(@PathVariable String companyId) {
        try {
            if (StringUtils.isBlank(companyId)) {
                return R.fail("companyId不能为空");
            }
            Map<String, Object> result = new LinkedHashMap<>();
            // 企业布局信息
            List<GzctIndustryLayout> layouts = layoutMapper.selectList(
                new LambdaQueryWrapper<GzctIndustryLayout>().eq(GzctIndustryLayout::getCompanyId, companyId));
            // 监控数据
            List<GzctIndustryMonitor> monitors = monitorMapper.selectList(
                new LambdaQueryWrapper<GzctIndustryMonitor>().eq(GzctIndustryMonitor::getCompanyId, companyId));
            // 如果通过companyId找不到monitor，尝试用companyId作为monitorId查找
            if (monitors.isEmpty()) {
                monitors = monitorMapper.selectList(
                    new LambdaQueryWrapper<GzctIndustryMonitor>().eq(GzctIndustryMonitor::getMonitorId, companyId));
            }

            if (layouts.isEmpty() && monitors.isEmpty()) {
                return R.fail("未找到该企业信息");
            }

            // 基本信息
            Map<String, Object> baseInfo = new LinkedHashMap<>();
            if (!layouts.isEmpty()) {
                GzctIndustryLayout layout = layouts.get(0);
                baseInfo.put("companyId", layout.getCompanyId());
                baseInfo.put("companyName", layout.getCompanyName());
                baseInfo.put("industryName", layout.getIndustryName());
                baseInfo.put("industryCode", layout.getIndustryCode());
                baseInfo.put("subIndustry", layout.getSubIndustry());
                baseInfo.put("isMainBiz", "1".equals(layout.getIsMainIndustry()));
                baseInfo.put("revenue", layout.getRevenue() != null ? layout.getRevenue().divide(BigDecimal.valueOf(10000), 1, RoundingMode.HALF_UP) : BigDecimal.ZERO);
                baseInfo.put("assets", layout.getAssetAmount() != null ? layout.getAssetAmount().divide(BigDecimal.valueOf(10000), 1, RoundingMode.HALF_UP) : BigDecimal.ZERO);
                baseInfo.put("employeeCount", layout.getEmployeeCount());
                baseInfo.put("netMargin", layout.getNetMargin());
                baseInfo.put("competitiveness", layout.getCompetitiveness());
                baseInfo.put("riskLevel", layout.getRiskLevel());
                baseInfo.put("isKeyMonitor", layout.getIsKeyMonitor());
            } else {
                // 从monitor表构建基本信息
                GzctIndustryMonitor m = monitors.get(0);
                baseInfo.put("companyId", m.getCompanyId() != null ? m.getCompanyId() : companyId);
                baseInfo.put("companyName", m.getCompanyName());
                baseInfo.put("industryName", m.getIndustryName());
                baseInfo.put("industryCode", m.getIndustryCode());
                baseInfo.put("subIndustry", m.getSubType());
                baseInfo.put("isMainBiz", true);
                BigDecimal rev = m.getRevenue() != null ? m.getRevenue() : (m.getTotalRevenue() != null ? m.getTotalRevenue() : BigDecimal.ZERO);
                baseInfo.put("revenue", rev.divide(BigDecimal.valueOf(10000), 1, RoundingMode.HALF_UP));
                baseInfo.put("assets", BigDecimal.ZERO);
                baseInfo.put("employeeCount", null);
                baseInfo.put("netMargin", m.getNetMargin());
                baseInfo.put("competitiveness", m.getCompetitiveness());
                baseInfo.put("riskLevel", m.getRiskLevel());
                baseInfo.put("isKeyMonitor", "0");
            }
            result.put("baseInfo", baseInfo);

            if (!monitors.isEmpty()) {
                GzctIndustryMonitor monitor = monitors.get(0);
                Map<String, Object> monitorData = new LinkedHashMap<>();
                monitorData.put("netMargin", monitor.getNetMargin());
                monitorData.put("debtRatio", monitor.getDebtRatio());
                monitorData.put("assetReturn", monitor.getAssetReturn());
                monitorData.put("capitalRatio", monitor.getCapitalRatio());
                monitorData.put("badDebtRatio", monitor.getBadDebtRatio());
                monitorData.put("rdIntensity", monitor.getRdIntensity());
                monitorData.put("smartLevel", monitor.getSmartLevel());
                monitorData.put("techSelfRate", monitor.getTechSelfRate());
                monitorData.put("renewableRatio", monitor.getRenewableRatio());
                monitorData.put("carbonIntensity", monitor.getCarbonIntensity());
                monitorData.put("safetyRating", monitor.getSafetyRating());
                monitorData.put("complianceStatus", monitor.getComplianceStatus());
                monitorData.put("profitStatus", monitor.getProfitStatus());
                monitorData.put("majorRisks", monitor.getMajorRisks());
                result.put("monitorData", monitorData);
            }
            // 风险指标
            BigDecimal debtRatio = BigDecimal.valueOf(65);
            BigDecimal netMarginVal = baseInfo.get("netMargin") != null ? new BigDecimal(baseInfo.get("netMargin").toString()) : BigDecimal.ZERO;
            if (!monitors.isEmpty() && monitors.get(0).getDebtRatio() != null) {
                debtRatio = monitors.get(0).getDebtRatio();
            }
            List<Map<String, Object>> riskIndicators = Arrays.asList(
                buildRiskIndicator("资产负债率", debtRatio + "%", "70%",
                    debtRatio.compareTo(BigDecimal.valueOf(70)) > 0 ? "WARN" : "NORMAL"),
                buildRiskIndicator("净利率", netMarginVal + "%", "3%",
                    netMarginVal.compareTo(BigDecimal.valueOf(3)) < 0 ? "WARN" : "NORMAL"),
                buildRiskIndicator("营收增长率", "8.5%", "0%", "NORMAL"),
                buildRiskIndicator("资本充足率",
                    (!monitors.isEmpty() && monitors.get(0).getCapitalRatio() != null) ? monitors.get(0).getCapitalRatio() + "%" : "N/A",
                    "10%",
                    (!monitors.isEmpty() && monitors.get(0).getCapitalRatio() != null && monitors.get(0).getCapitalRatio().compareTo(BigDecimal.valueOf(10)) < 0) ? "WARN" : "NORMAL")
            );
            result.put("riskIndicators", riskIndicators);
            // 预警记录
            List<GzctIndustryWarning> warnings = warningMapper.selectList(
                new LambdaQueryWrapper<GzctIndustryWarning>()
                    .eq(GzctIndustryWarning::getCompanyId, companyId)
                    .orderByDesc(GzctIndustryWarning::getCreateTime));
            List<Map<String, Object>> warningList = warnings.stream().map(w -> {
                Map<String, Object> wm = new LinkedHashMap<>();
                wm.put("warningId", w.getWarningId());
                wm.put("warningType", w.getWarningType());
                wm.put("warningContent", w.getWarningContent());
                wm.put("level", w.getLevel());
                wm.put("status", w.getStatus());
                wm.put("riskScore", w.getRiskScore());
                wm.put("warnTime", w.getWarnTime());
                wm.put("handler", w.getHandler());
                return wm;
            }).collect(Collectors.toList());
            result.put("warnings", warningList);
            result.put("warningCount", warningList.size());
            // 统计卡片
            Object revenueVal = baseInfo.get("revenue");
            String riskLevel = baseInfo.get("riskLevel") != null ? baseInfo.get("riskLevel").toString() : "N/A";
            result.put("stats", Arrays.asList(
                buildStatCard("营收规模", revenueVal + "亿", "#0050A0", "el-icon-money"),
                buildStatCard("净利率", netMarginVal + "%", "#52C41A", "el-icon-data-line"),
                buildStatCard("风险等级", riskLevel, "#F5222D", "el-icon-warning"),
                buildStatCard("预警数", warningList.size() + "条", "#FA8C16", "el-icon-bell")
            ));
            return R.success(result);
        } catch (Exception e) {
            log.error("穿透企业详情查询失败, companyId={}", companyId, e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    private List<Map<String, Object>> buildDrillGroups() {
        String[][] groupDefs = {
            {"能源行业", "ENERGY", "el-icon-lightning", "linear-gradient(135deg,#d46b08,#FA8C16)"},
            {"金融行业", "FINANCIAL", "el-icon-bank", "linear-gradient(135deg,#003A6C,#40a9ff)"},
            {"制造行业", "MANUFACTURING", "el-icon-s-tools", "linear-gradient(135deg,#237804,#52C41A)"},
            {"基础设施", "INFRASTRUCTURE", "el-icon-house", "linear-gradient(135deg,#003A6C,#0050A0)"},
            {"公共服务", "PUBLIC_SERVICE", "el-icon-service", "linear-gradient(135deg,#531dab,#722ED1)"},
        };
        List<Map<String, Object>> groups = new ArrayList<>();
        for (String[] def : groupDefs) {
            List<GzctIndustryLayout> layouts = layoutMapper.selectList(
                new LambdaQueryWrapper<GzctIndustryLayout>().eq(GzctIndustryLayout::getIndustryCode, def[1]));
            Long warnings = warningMapper.selectCount(
                new LambdaQueryWrapper<GzctIndustryWarning>().like(GzctIndustryWarning::getIndustryName, def[0].replace("行业", "")));
            Map<String, Object> g = new LinkedHashMap<>();
            g.put("name", def[0]);
            g.put("icon", def[2]);
            g.put("gradient", def[3]);
            g.put("color", def[3].contains("#F") ? "#FA8C16" : "#0050A0");
            g.put("companies", layouts.size());
            BigDecimal totalRev = layouts.stream().map(l -> l.getRevenue() != null ? l.getRevenue() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add);
            g.put("revenue", totalRev.divide(BigDecimal.valueOf(10000), 1, RoundingMode.HALF_UP));
            g.put("riskCount", warnings);
            long highRiskCount = layouts.stream().filter(l -> "HIGH".equals(l.getRiskLevel())).count();
            g.put("riskLevel", highRiskCount > 0 ? "HIGH" : (warnings > 2 ? "MEDIUM" : "LOW"));
            g.put("subCount", 3);
            // 子行业
            List<Map<String, Object>> subIndustries = new ArrayList<>();
            Map<String, List<GzctIndustryLayout>> subGrouped = layouts.stream()
                .collect(Collectors.groupingBy(l -> l.getSubIndustry() != null ? l.getSubIndustry() : "其他"));
            for (Map.Entry<String, List<GzctIndustryLayout>> entry : subGrouped.entrySet()) {
                Map<String, Object> sub = new HashMap<>();
                sub.put("name", entry.getKey());
                sub.put("companies", entry.getValue().size());
                BigDecimal subRev = entry.getValue().stream().map(l -> l.getRevenue() != null ? l.getRevenue() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add);
                sub.put("revenue", subRev.divide(BigDecimal.valueOf(10000), 1, RoundingMode.HALF_UP));
                BigDecimal subProfit = subRev.multiply(BigDecimal.valueOf(0.08));
                sub.put("profit", subProfit.divide(BigDecimal.valueOf(10000), 1, RoundingMode.HALF_UP));
                sub.put("profitRate", 8.0);
                sub.put("riskScore", highRiskCount > 0 ? 72 : 35);
                // 企业列表
                List<Map<String, Object>> companies = entry.getValue().stream().map(l -> {
                    Map<String, Object> c = new HashMap<>();
                    c.put("code", l.getCompanyId());
                    c.put("name", l.getCompanyName());
                    c.put("isMainBiz", "1".equals(l.getIsMainIndustry()));
                    c.put("revenue", l.getRevenue() != null ? l.getRevenue().divide(BigDecimal.valueOf(10000), 1, RoundingMode.HALF_UP) : BigDecimal.ZERO);
                    c.put("profit", l.getRevenue() != null ? l.getRevenue().multiply(l.getNetMargin() != null ? l.getNetMargin().divide(BigDecimal.valueOf(100)) : BigDecimal.valueOf(0.05)).divide(BigDecimal.valueOf(10000), 1, RoundingMode.HALF_UP) : BigDecimal.ZERO);
                    c.put("assets", l.getAssetAmount() != null ? l.getAssetAmount().divide(BigDecimal.valueOf(10000), 1, RoundingMode.HALF_UP) : BigDecimal.ZERO);
                    c.put("riskLevel", l.getRiskLevel());
                    c.put("debtRatio", 65.0);
                    c.put("riskIndicators", Arrays.asList(
                        buildRiskIndicator("资产负债率", "65%", "70%", "NORMAL"),
                        buildRiskIndicator("营收增长率", "8.5%", "0%", "NORMAL"),
                        buildRiskIndicator("净利率", l.getNetMargin() + "%", "3%", l.getNetMargin() != null && l.getNetMargin().compareTo(BigDecimal.valueOf(3)) < 0 ? "WARN" : "NORMAL")
                    ));
                    return c;
                }).collect(Collectors.toList());
                sub.put("companies", companies);
                sub.put("stats", Arrays.asList(
                    buildStatCard("企业数", entry.getValue().size() + "家", "#0050A0", "el-icon-office-building"),
                    buildStatCard("总营收", subRev.divide(BigDecimal.valueOf(10000), 1, RoundingMode.HALF_UP) + "亿", "#52C41A", "el-icon-money"),
                    buildStatCard("平均利润率", "8.0%", "#FA8C16", "el-icon-data-line"),
                    buildStatCard("风险评分", (highRiskCount > 0 ? "72" : "35"), "#F5222D", "el-icon-warning")
                ));
                subIndustries.add(sub);
            }
            g.put("subIndustries", subIndustries);
            g.put("stats", Arrays.asList(
                buildStatCard("纳管企业", layouts.size() + "家", "#0050A0", "el-icon-office-building"),
                buildStatCard("总营收", totalRev.divide(BigDecimal.valueOf(10000), 1, RoundingMode.HALF_UP) + "亿", "#52C41A", "el-icon-money"),
                buildStatCard("风险预警", warnings + "条", "#F5222D", "el-icon-warning"),
                buildStatCard("子行业数", subGrouped.size() + "个", "#FA8C16", "el-icon-s-grid")
            ));
            groups.add(g);
        }
        return groups;
    }

    // ==================== 辅助方法 ====================

    private Map<String, Object> buildSeriesItem(String name, String type, List<?> data) {
        Map<String, Object> item = new LinkedHashMap<>();
        item.put("name", name);
        item.put("type", type);
        item.put("data", data);
        return item;
    }

    private Map<String, Object> buildPieItem(String name, Number value) {
        Map<String, Object> item = new LinkedHashMap<>();
        item.put("name", name);
        item.put("value", value);
        return item;
    }

    private Map<String, Object> buildIndicator(String name, int max) {
        Map<String, Object> item = new LinkedHashMap<>();
        item.put("name", name);
        item.put("max", max);
        return item;
    }

    private Map<String, Object> buildRadarDataItem(String name, List<?> value) {
        Map<String, Object> item = new LinkedHashMap<>();
        item.put("name", name);
        item.put("value", value);
        return item;
    }

    private Map<String, Object> buildStatCard(String label, String value, String color, String icon) {
        Map<String, Object> card = new LinkedHashMap<>();
        card.put("label", label);
        card.put("value", value);
        card.put("color", color);
        card.put("icon", icon);
        return card;
    }

    private Map<String, Object> buildRiskIndicator(String name, String value, String threshold, String status) {
        Map<String, Object> item = new LinkedHashMap<>();
        item.put("name", name);
        item.put("value", value);
        item.put("threshold", threshold);
        item.put("status", status);
        return item;
    }

    private String mapToIndustryGroup(String raw) {
        if (raw == null) return "未知";
        if (raw.contains("能源") || raw.contains("电力") || raw.contains("石化") || raw.contains("煤") || raw.contains("核") || raw.contains("化工")) return "能源行业";
        if (raw.contains("金融") || raw.contains("银行") || raw.contains("保险") || raw.contains("证券")) return "金融行业";
        if (raw.contains("制造") || raw.contains("装备") || raw.contains("航天") || raw.contains("电子")) return "制造行业";
        if (raw.contains("基础") || raw.contains("交通") || raw.contains("水利") || raw.contains("建设") || raw.contains("建筑") || raw.contains("房地产")) return "基础设施";
        if (raw.contains("公共") || raw.contains("通信") || raw.contains("邮政") || raw.contains("服务") || raw.contains("信息") || raw.contains("软件") || raw.contains("互联") || raw.contains("农业") || raw.contains("矿产") || raw.contains("资源")) return "公共服务";
        return raw;
    }

    /** 根据行业分组名称获取数据库模糊匹配关键词列表 */
    private List<String> getIndustryKeywords(String groupName) {
        if (groupName == null) return Collections.emptyList();
        if (groupName.contains("能源")) return Arrays.asList("能源", "电力", "石化", "煤", "核", "化工");
        if (groupName.contains("金融")) return Arrays.asList("金融", "银行", "保险", "证券");
        if (groupName.contains("制造")) return Arrays.asList("制造", "装备", "航天", "电子");
        if (groupName.contains("基础")) return Arrays.asList("基础", "交通", "水利", "建设", "建筑", "房地产");
        if (groupName.contains("公共") || groupName.contains("服务")) return Arrays.asList("公共", "通信", "邮政", "服务", "信息", "软件", "互联", "农业", "矿产", "资源");
        return Collections.emptyList();
    }

    private Map<String, Object> buildKpiCard(String label, String value, String icon, String bgColor, String color, String valColor) {
        Map<String, Object> card = new LinkedHashMap<>();
        card.put("label", label);
        card.put("value", value);
        card.put("icon", icon);
        card.put("bgColor", bgColor);
        card.put("color", color);
        if (valColor != null) card.put("valColor", valColor);
        return card;
    }

    private Map<String, Object> buildRiskMatrixRow(String industry, int policy, int market, int tech, int compliance, String overall) {
        Map<String, Object> row = new LinkedHashMap<>();
        row.put("industry", industry);
        row.put("policy", policy);
        row.put("market", market);
        row.put("tech", tech);
        row.put("compliance", compliance);
        row.put("overall", overall);
        return row;
    }

    /** 行业名称 -> 行业代码映射 */
    private String mapIndustryNameToCode(String industryName) {
        if (industryName == null) return null;
        if (industryName.contains("能源")) return "ENERGY";
        if (industryName.contains("金融")) return "FINANCIAL";
        if (industryName.contains("制造")) return "MANUFACTURING";
        if (industryName.contains("基础设施") || industryName.contains("基建")) return "INFRASTRUCTURE";
        if (industryName.contains("公共服务") || industryName.contains("公服")) return "PUBLIC_SERVICE";
        return null;
    }
}
