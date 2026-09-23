package com.huabo.cybermonitor.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.huabo.cybermonitor.entity.EquityStructure;
import com.huabo.cybermonitor.entity.FinancialIndicators;
import com.huabo.cybermonitor.entity.TblPropertyRight;
import com.huabo.cybermonitor.entity.TblPropertyTransaction;
import com.huabo.cybermonitor.entity.TblAssetAllocation;
import com.huabo.cybermonitor.entity.TblAssetConcentration;
import com.huabo.cybermonitor.entity.TblAssetFlow;
import com.huabo.cybermonitor.entity.TblAssetMapping;
import com.huabo.cybermonitor.entity.TblCrossHolding;
import com.huabo.cybermonitor.entity.TblAssetQuality;
import com.huabo.cybermonitor.mapper.EquityStructureMapper;
import com.huabo.cybermonitor.mapper.FinancialIndicatorsMapper;
import com.huabo.cybermonitor.mapper.TblPropertyRightMapper;
import com.huabo.cybermonitor.mapper.TblPropertyTransactionMapper;
import com.huabo.cybermonitor.mapper.TblAssetAllocationMapper;
import com.huabo.cybermonitor.mapper.TblAssetConcentrationMapper;
import com.huabo.cybermonitor.mapper.TblAssetFlowMapper;
import com.huabo.cybermonitor.mapper.TblAssetMappingMapper;
import com.huabo.cybermonitor.mapper.TblCrossHoldingMapper;
import com.huabo.cybermonitor.mapper.TblAssetQualityMapper;
import com.huabo.cybermonitor.util.PageResult;
import com.huabo.cybermonitor.util.R;
import com.huabo.cybermonitor.util.SimpleXlsxWriter;
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
 * 资产穿透式监管控制器
 * 包含: 资产集中度/资产流向/资产映射/交叉持股/资产配置/资产质量
 * 数据来源: TBL_PROPERTY_RIGHT + FINANCIAL_INDICATORS + EQUITY_STRUCTURE
 */
@Tag(name = "资产穿透式监管", description = "资产穿透式监管综合接口")
@RestController
@RequestMapping("/v1/supervision/asset")
@Slf4j
public class AssetPenetrationController {

    @Autowired
    private TblPropertyRightMapper propertyRightMapper;
    @Autowired
    private FinancialIndicatorsMapper financialIndicatorsMapper;
    @Autowired
    private EquityStructureMapper equityStructureMapper;
    @Autowired
    private TblPropertyTransactionMapper propertyTransactionMapper;
    @Autowired
    private TblAssetAllocationMapper assetAllocationMapper;
    @Autowired
    private TblAssetConcentrationMapper assetConcentrationMapper;
    @Autowired
    private TblAssetFlowMapper assetFlowMapper;
    @Autowired
    private TblAssetMappingMapper assetMappingMapper;
    @Autowired
    private TblCrossHoldingMapper crossHoldingMapper;
    @Autowired
    private TblAssetQualityMapper assetQualityMapper;

    private <T> PageResult<T> buildPageResult(Page<T> r) {
        PageResult<T> pr = new PageResult<>();
        pr.setTotalRecord((int) r.getTotal()); pr.setCurrentPage((int) r.getCurrent());
        pr.setPageNumber((int) r.getCurrent()); pr.setTotalPage((int) r.getPages());
        pr.setPageSize((int) r.getSize()); pr.setTlist(r.getRecords());
        return pr;
    }

    // ==================== 资产集中度 ====================

    @Operation(summary = "资产集中度分析列表")
    @PostMapping("/concentration/list")
    public R<PageResult<TblAssetConcentration>> concentrationList(@RequestBody Map<String, Object> params) {
        try {
            int pn = getPageNumber(params); int ps = getPageSize(params);
            LambdaQueryWrapper<TblAssetConcentration> w = new LambdaQueryWrapper<>();
            if (params.get("companyName") != null && StringUtils.isNotEmpty(params.get("companyName").toString())) {
                w.like(TblAssetConcentration::getCompanyName, params.get("companyName").toString());
            }
            if (params.get("industry") != null && StringUtils.isNotEmpty(params.get("industry").toString())) {
                w.like(TblAssetConcentration::getIndustry, params.get("industry").toString());
            }
            if (params.get("concentrationLevel") != null && StringUtils.isNotEmpty(params.get("concentrationLevel").toString())) {
                w.eq(TblAssetConcentration::getConcentrationLevel, params.get("concentrationLevel").toString());
            }
            if (params.get("riskLevel") != null && StringUtils.isNotEmpty(params.get("riskLevel").toString())) {
                w.eq(TblAssetConcentration::getRiskLevel, params.get("riskLevel").toString());
            }
            w.orderByDesc(TblAssetConcentration::getCreateTime);
            Page<TblAssetConcentration> page = assetConcentrationMapper.selectPage(new Page<>(pn, ps), w);
            return R.success(PageResult.of(page));
        } catch (Exception e) { log.error("查询资产集中度列表失败", e); return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "资产集中度详情")
    @GetMapping("/concentration/{id}")
    public R<Map<String, Object>> concentrationDetail(@PathVariable String id) {
        try {
            TblPropertyRight pr = propertyRightMapper.selectById(id);
            if (pr == null) return R.fail("未找到记录");
            Map<String, Object> result = buildConcentrationItem(pr);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "新增资产集中度分析")
    @PostMapping("/concentration/add")
    public R<Boolean> concentrationAdd(@RequestBody TblAssetConcentration record) {
        try {
            record.setCreateTime(LocalDateTime.now());
            assetConcentrationMapper.insert(record);
            return R.success(true);
        } catch (Exception e) { log.error("新增资产集中度失败", e); return R.fail("新增失败：" + e.getMessage()); }
    }

    @Operation(summary = "更新资产集中度分析")
    @PostMapping("/concentration/update")
    public R<Boolean> concentrationUpdate(@RequestBody TblAssetConcentration record) {
        try {
            record.setUpdateTime(LocalDateTime.now());
            assetConcentrationMapper.updateById(record);
            return R.success(true);
        } catch (Exception e) { log.error("更新资产集中度失败", e); return R.fail("更新失败：" + e.getMessage()); }
    }

    @Operation(summary = "删除资产集中度分析")
    @DeleteMapping("/concentration/{id}")
    public R<Boolean> concentrationDelete(@PathVariable String id) {
        try {
            assetConcentrationMapper.deleteById(id);
            return R.success(true);
        } catch (Exception e) { log.error("删除资产集中度失败", e); return R.fail("删除失败：" + e.getMessage()); }
    }

    @Operation(summary = "计算资产集中度指标")
    @PostMapping("/concentration/calculate")
    public R<Map<String, Object>> calculateConcentration(@RequestBody Map<String, Object> params) {
        try {
            String id = params.get("concentrationId") != null ? params.get("concentrationId").toString() : null;
            if (id != null) {
                TblAssetConcentration record = assetConcentrationMapper.selectById(id);
                if (record != null && record.getTotalAssets() != null) {
                    // 计算HHI: 基于资产占比的平方和 * 10000
                    BigDecimal ratio = record.getAssetRatio() != null ? record.getAssetRatio() : BigDecimal.ZERO;
                    BigDecimal hhi = ratio.multiply(ratio).divide(new BigDecimal("100"), 4, RoundingMode.HALF_UP);
                    record.setHhiIndex(hhi.multiply(new BigDecimal("100")));
                    // 判断集中度等级
                    double hhiVal = record.getHhiIndex().doubleValue();
                    if (hhiVal >= 2500) record.setConcentrationLevel("HIGH");
                    else if (hhiVal >= 1500) record.setConcentrationLevel("MEDIUM");
                    else if (hhiVal >= 1000) record.setConcentrationLevel("LOW");
                    else record.setConcentrationLevel("DISPERSED");
                    record.setUpdateTime(LocalDateTime.now());
                    assetConcentrationMapper.updateById(record);
                    Map<String, Object> result = new HashMap<>();
                    result.put("hhiIndex", record.getHhiIndex());
                    result.put("concentrationLevel", record.getConcentrationLevel());
                    return R.success(result);
                }
            }
            return R.success(new HashMap<>());
        } catch (Exception e) { return R.fail("计算失败：" + e.getMessage()); }
    }

    @Operation(summary = "分析行业集中度")
    @PostMapping("/concentration/industry")
    public R<List<Map<String, Object>>> industryConcentration(@RequestBody Map<String, Object> params) {
        try {
            List<TblPropertyRight> all = propertyRightMapper.selectList(null);
            Map<String, List<TblPropertyRight>> grouped = all.stream()
                .filter(p -> p.getIndustry() != null).collect(Collectors.groupingBy(TblPropertyRight::getIndustry));
            List<Map<String, Object>> result = new ArrayList<>();
            grouped.forEach((industry, list) -> {
                Map<String, Object> item = new HashMap<>();
                item.put("industry", industry);
                item.put("companyCount", list.size());
                item.put("totalInvest", list.stream().map(p -> p.getInvestAmount() != null ? p.getInvestAmount() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add));
                result.add(item);
            });
            result.sort((a, b) -> ((BigDecimal) b.get("totalInvest")).compareTo((BigDecimal) a.get("totalInvest")));
            return R.success(result);
        } catch (Exception e) { return R.fail("分析失败：" + e.getMessage()); }
    }

    @Operation(summary = "分析地域集中度")
    @PostMapping("/concentration/regional")
    public R<List<Map<String, Object>>> regionalConcentration(@RequestBody Map<String, Object> params) {
        try {
            List<TblPropertyRight> all = propertyRightMapper.selectList(null);
            Map<String, List<TblPropertyRight>> grouped = all.stream()
                .filter(p -> p.getRegion() != null).collect(Collectors.groupingBy(TblPropertyRight::getRegion));
            List<Map<String, Object>> result = new ArrayList<>();
            grouped.forEach((region, list) -> {
                Map<String, Object> item = new HashMap<>();
                item.put("region", region);
                item.put("companyCount", list.size());
                item.put("totalInvest", list.stream().map(p -> p.getInvestAmount() != null ? p.getInvestAmount() : BigDecimal.ZERO).reduce(BigDecimal.ZERO, BigDecimal::add));
                result.add(item);
            });
            return R.success(result);
        } catch (Exception e) { return R.fail("分析失败：" + e.getMessage()); }
    }

    @Operation(summary = "分析资产类型集中度")
    @PostMapping("/concentration/asset-type")
    public R<List<Map<String, Object>>> assetTypeConcentration(@RequestBody Map<String, Object> params) {
        try {
            List<TblPropertyRight> all = propertyRightMapper.selectList(null);
            Map<String, List<TblPropertyRight>> grouped = all.stream()
                .filter(p -> p.getPropertyStatus() != null).collect(Collectors.groupingBy(TblPropertyRight::getPropertyStatus));
            List<Map<String, Object>> result = new ArrayList<>();
            grouped.forEach((status, list) -> {
                Map<String, Object> item = new HashMap<>();
                item.put("assetType", status);
                item.put("count", list.size());
                result.add(item);
            });
            return R.success(result);
        } catch (Exception e) { return R.fail("分析失败：" + e.getMessage()); }
    }

    @Operation(summary = "分析时间集中度")
    @PostMapping("/concentration/temporal")
    public R<List<Map<String, Object>>> temporalConcentration(@RequestBody Map<String, Object> params) {
        try { return R.success(new ArrayList<>()); }
        catch (Exception e) { return R.fail("分析失败：" + e.getMessage()); }
    }

    @Operation(summary = "计算HHI指数")
    @PostMapping("/concentration/hhi")
    public R<Map<String, Object>> calculateHHI(@RequestBody Map<String, Object> params) {
        try {
            Map<String, Object> metrics = calculateConcentrationMetrics();
            return R.success(metrics);
        } catch (Exception e) { return R.fail("计算失败：" + e.getMessage()); }
    }

    @Operation(summary = "计算CR指数")
    @PostMapping("/concentration/cr")
    public R<Map<String, Object>> calculateCR(@RequestBody Map<String, Object> params) {
        try {
            Map<String, Object> metrics = calculateConcentrationMetrics();
            return R.success(metrics);
        } catch (Exception e) { return R.fail("计算失败：" + e.getMessage()); }
    }

    @Operation(summary = "分析集中度趋势")
    @PostMapping("/concentration/trend")
    public R<Map<String, Object>> concentrationTrend(@RequestBody Map<String, Object> params) {
        try {
            Map<String, Object> result = new HashMap<>();
            List<String> dates = Arrays.asList("2024-07", "2024-08", "2024-09", "2024-10", "2024-11", "2024-12");
            String id = params.get("concentrationId") != null ? params.get("concentrationId").toString() : null;
            TblAssetConcentration record = id != null ? assetConcentrationMapper.selectById(id) : null;
            double baseHhi = record != null && record.getHhiIndex() != null ? record.getHhiIndex().doubleValue() : 2000;
            List<Double> values = new ArrayList<>();
            java.util.Random rand = new java.util.Random(baseHhi > 0 ? (long) baseHhi : 1);
            for (int i = 0; i < 6; i++) {
                values.add(Math.round((baseHhi + (rand.nextDouble() - 0.5) * 500) * 100.0) / 100.0);
            }
            result.put("dates", dates);
            result.put("values", values);
            return R.success(result);
        } catch (Exception e) { return R.fail("分析失败：" + e.getMessage()); }
    }

    @Operation(summary = "对比集中度分析")
    @PostMapping("/concentration/compare")
    public R<List<Map<String, Object>>> compareConcentration(@RequestBody Map<String, Object> params) {
        try {
            List<TblAssetConcentration> all = assetConcentrationMapper.selectList(
                new LambdaQueryWrapper<TblAssetConcentration>().orderByDesc(TblAssetConcentration::getHhiIndex).last("LIMIT 5"));
            List<Map<String, Object>> result = new ArrayList<>();
            for (TblAssetConcentration a : all) {
                Map<String, Object> item = new HashMap<>();
                item.put("concentrationId", a.getConcentrationId());
                item.put("companyName", a.getCompanyName());
                item.put("industry", a.getIndustry());
                item.put("totalAssets", a.getTotalAssets());
                item.put("hhiIndex", a.getHhiIndex());
                item.put("concentrationLevel", a.getConcentrationLevel());
                item.put("riskLevel", a.getRiskLevel());
                result.add(item);
            }
            return R.success(result);
        } catch (Exception e) { return R.fail("分析失败：" + e.getMessage()); }
    }

    @Operation(summary = "评估集中度风险")
    @PostMapping("/concentration/risk")
    public R<Map<String, Object>> assessConcentrationRisk(@RequestBody Map<String, Object> params) {
        try {
            Map<String, Object> result = calculateConcentrationMetrics();
            return R.success(result);
        } catch (Exception e) { return R.fail("评估失败：" + e.getMessage()); }
    }

    @Operation(summary = "获取集中度预警")
    @PostMapping("/concentration/alerts")
    public R<List<Map<String, Object>>> concentrationAlerts(@RequestBody Map<String, Object> params) {
        try { return R.success(new ArrayList<>()); }
        catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "分析集中度影响因素")
    @PostMapping("/concentration/factors")
    public R<List<Map<String, Object>>> concentrationFactors(@RequestBody Map<String, Object> params) {
        try { return R.success(new ArrayList<>()); }
        catch (Exception e) { return R.fail("分析失败：" + e.getMessage()); }
    }

    @Operation(summary = "模拟集中度变化")
    @PostMapping("/concentration/simulate")
    public R<Map<String, Object>> simulateConcentration(@RequestBody Map<String, Object> params) {
        try { return R.success(new HashMap<>()); }
        catch (Exception e) { return R.fail("模拟失败：" + e.getMessage()); }
    }

    @Operation(summary = "优化资产分散度")
    @PostMapping("/concentration/optimize")
    public R<Map<String, Object>> optimizeDispersion(@RequestBody Map<String, Object> params) {
        try {
            String id = params.get("concentrationId") != null ? params.get("concentrationId").toString() : null;
            TblAssetConcentration record = id != null ? assetConcentrationMapper.selectById(id) : null;
            Map<String, Object> result = new HashMap<>();
            String level = record != null ? record.getConcentrationLevel() : "MEDIUM";
            StringBuilder sb = new StringBuilder();
            sb.append("【资产集中度优化建议】\n\n");
            if ("HIGH".equals(level)) {
                sb.append("⚠️ 当前集中度等级：高集中\n\n");
                sb.append("1. 建议适当分散资产配置，降低单一行业/区域依赖\n");
                sb.append("2. 考虑增加跨行业投资，目标将HHI指数降至2500以下\n");
                sb.append("3. 评估高集中资产的流动性风险，制定应急预案\n");
                sb.append("4. 建议将资产占比最高的类别控制在50%以内\n");
            } else if ("MEDIUM".equals(level)) {
                sb.append("📊 当前集中度等级：中等\n\n");
                sb.append("1. 资产配置相对合理，建议持续监控\n");
                sb.append("2. 可适当增加新兴产业投资以优化结构\n");
                sb.append("3. 关注行业周期性风险，做好对冲准备\n");
            } else {
                sb.append("✅ 当前集中度等级：低集中/分散\n\n");
                sb.append("1. 资产配置较为分散，风险可控\n");
                sb.append("2. 注意避免过度分散导致管理效率下降\n");
                sb.append("3. 建议聚焦核心业务，保持适度集中\n");
            }
            result.put("text", sb.toString());
            result.put("level", level);
            result.put("companyName", record != null ? record.getCompanyName() : "");
            return R.success(result);
        } catch (Exception e) { return R.fail("分析失败：" + e.getMessage()); }
    }

    @Operation(summary = "获取集中度基准")
    @PostMapping("/concentration/benchmark")
    public R<Map<String, Object>> concentrationBenchmark(@RequestBody Map<String, Object> params) {
        try { return R.success(new HashMap<>()); }
        catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "分析集中度合规性")
    @PostMapping("/concentration/compliance")
    public R<Map<String, Object>> concentrationCompliance(@RequestBody Map<String, Object> params) {
        try { return R.success(new HashMap<>()); }
        catch (Exception e) { return R.fail("分析失败：" + e.getMessage()); }
    }

    @Operation(summary = "获取集中度统计")
    @PostMapping("/concentration/statistics")
    public R<Map<String, Object>> concentrationStatistics(@RequestBody(required = false) Map<String, Object> params) {
        try {
            Map<String, Object> result = new HashMap<>();
            Long total = assetConcentrationMapper.selectCount(null);
            Long highRisk = assetConcentrationMapper.selectCount(
                new LambdaQueryWrapper<TblAssetConcentration>().eq(TblAssetConcentration::getRiskLevel, "HIGH"));
            Long highConcentration = assetConcentrationMapper.selectCount(
                new LambdaQueryWrapper<TblAssetConcentration>().eq(TblAssetConcentration::getConcentrationLevel, "HIGH"));
            List<TblAssetConcentration> all = assetConcentrationMapper.selectList(null);
            double avgHHI = all.stream().filter(a -> a.getHhiIndex() != null)
                .mapToDouble(a -> a.getHhiIndex().doubleValue()).average().orElse(0);
            result.put("totalCompanies", total);
            result.put("highRiskCount", highRisk);
            result.put("highConcentrationCount", highConcentration);
            result.put("avgHHI", Math.round(avgHHI * 100.0) / 100.0);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "生成集中度报告")
    @PostMapping("/concentration/report")
    public R<Map<String, Object>> concentrationReport(@RequestBody Map<String, Object> params) {
        try {
            String id = params.get("concentrationId") != null ? params.get("concentrationId").toString() : null;
            TblAssetConcentration record = id != null ? assetConcentrationMapper.selectById(id) : null;
            Map<String, Object> result = new HashMap<>();
            result.put("reportTitle", "资产集中度分析报告");
            result.put("companyName", record != null ? record.getCompanyName() : "");
            result.put("generatedTime", LocalDateTime.now().toString());
            result.put("hhiIndex", record != null ? record.getHhiIndex() : 0);
            result.put("concentrationLevel", record != null ? record.getConcentrationLevel() : "");
            result.put("riskLevel", record != null ? record.getRiskLevel() : "");
            result.put("conclusion", record != null && "HIGH".equals(record.getConcentrationLevel())
                ? "该企业资产集中度较高，建议优化资产配置结构" : "该企业资产集中度处于合理范围");
            return R.success(result);
        } catch (Exception e) { return R.fail("生成失败：" + e.getMessage()); }
    }

    @Operation(summary = "批量计算集中度")
    @PostMapping("/concentration/batch/calculate")
    public R<Boolean> batchCalculateConcentration(@RequestBody Map<String, Object> params) {
        return R.success(true);
    }

    @Operation(summary = "批量删除集中度分析")
    @PostMapping("/concentration/batch/delete")
    public R<Boolean> batchDeleteConcentration(@RequestBody Map<String, Object> params) {
        return R.success(true);
    }

    @Operation(summary = "导出集中度数据")
    @PostMapping("/concentration/export")
    public R<List<Map<String, Object>>> exportConcentration(@RequestBody Map<String, Object> params) {
        try {
            List<TblPropertyRight> all = propertyRightMapper.selectList(null);
            return R.success(buildConcentrationData(all));
        } catch (Exception e) { return R.fail("导出失败：" + e.getMessage()); }
    }

    @Operation(summary = "导入集中度数据")
    @PostMapping("/concentration/import")
    public R<Boolean> importConcentration() {
        return R.success(true);
    }

    @Operation(summary = "获取集中度图表数据")
    @PostMapping("/concentration/chart")
    public R<Map<String, Object>> concentrationChart(@RequestBody Map<String, Object> params) {
        try {
            Map<String, Object> result = new HashMap<>();
            // 饼图数据：资产类型分布
            List<TblAssetConcentration> all = assetConcentrationMapper.selectList(null);
            Map<String, Long> typeCount = all.stream().filter(a -> a.getTopAssetType() != null)
                .collect(Collectors.groupingBy(TblAssetConcentration::getTopAssetType, Collectors.counting()));
            List<Map<String, Object>> pieData = new ArrayList<>();
            typeCount.forEach((k, v) -> { Map<String, Object> m = new HashMap<>(); m.put("name", k); m.put("value", v); pieData.add(m); });
            result.put("pieData", pieData);
            // 柱状图数据：行业集中度
            Map<String, Double> industryHhi = all.stream().filter(a -> a.getIndustry() != null && a.getHhiIndex() != null)
                .collect(Collectors.groupingBy(TblAssetConcentration::getIndustry, Collectors.averagingDouble(a -> a.getHhiIndex().doubleValue())));
            List<Map<String, Object>> barData = new ArrayList<>();
            industryHhi.forEach((k, v) -> { Map<String, Object> m = new HashMap<>(); m.put("name", k); m.put("value", Math.round(v * 100.0) / 100.0); barData.add(m); });
            result.put("barData", barData);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "分析集中度稳定性")
    @PostMapping("/concentration/stability")
    public R<Map<String, Object>> concentrationStability(@RequestBody Map<String, Object> params) {
        try { return R.success(new HashMap<>()); }
        catch (Exception e) { return R.fail("分析失败：" + e.getMessage()); }
    }

    // ==================== 资产流向 ====================

    @Operation(summary = "资产流向列表")
    @PostMapping("/flow/list")
    public R<PageResult<TblAssetFlow>> flowList(@RequestBody Map<String, Object> params) {
        try {
            int pn = getPageNumber(params); int ps = getPageSize(params);
            LambdaQueryWrapper<TblAssetFlow> w = new LambdaQueryWrapper<>();
            if (params.get("sourceCompanyName") != null && StringUtils.isNotEmpty(params.get("sourceCompanyName").toString())) {
                w.like(TblAssetFlow::getSourceCompanyName, params.get("sourceCompanyName").toString());
            }
            if (params.get("targetCompanyName") != null && StringUtils.isNotEmpty(params.get("targetCompanyName").toString())) {
                w.like(TblAssetFlow::getTargetCompanyName, params.get("targetCompanyName").toString());
            }
            if (params.get("assetType") != null && StringUtils.isNotEmpty(params.get("assetType").toString())) {
                w.eq(TblAssetFlow::getAssetType, params.get("assetType").toString());
            }
            if (params.get("flowType") != null && StringUtils.isNotEmpty(params.get("flowType").toString())) {
                w.eq(TblAssetFlow::getFlowType, params.get("flowType").toString());
            }
            if (params.get("riskLevel") != null && StringUtils.isNotEmpty(params.get("riskLevel").toString())) {
                w.eq(TblAssetFlow::getRiskLevel, params.get("riskLevel").toString());
            }
            w.orderByDesc(TblAssetFlow::getCreateTime);
            Page<TblAssetFlow> page = assetFlowMapper.selectPage(new Page<>(pn, ps), w);
            return R.success(PageResult.of(page));
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "资产流向详情")
    @GetMapping("/flow/{id}")
    public R<TblAssetFlow> flowDetail(@PathVariable String id) {
        try {
            return R.success(assetFlowMapper.selectById(id));
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "新增资产流向记录")
    @PostMapping("/flow/add")
    public R<Boolean> flowAdd(@RequestBody TblAssetFlow record) {
        try { record.setCreateTime(LocalDateTime.now()); assetFlowMapper.insert(record); return R.success(true); }
        catch (Exception e) { return R.fail("新增失败：" + e.getMessage()); }
    }

    @Operation(summary = "更新资产流向记录")
    @PostMapping("/flow/update")
    public R<Boolean> flowUpdate(@RequestBody TblAssetFlow record) {
        try { record.setUpdateTime(LocalDateTime.now()); assetFlowMapper.updateById(record); return R.success(true); }
        catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); }
    }

    @Operation(summary = "删除资产流向记录")
    @DeleteMapping("/flow/{id}")
    public R<Boolean> flowDelete(@PathVariable String id) {
        try { assetFlowMapper.deleteById(id); return R.success(true); }
        catch (Exception e) { return R.fail("删除失败：" + e.getMessage()); }
    }

    @Operation(summary = "追踪资产流向")
    @PostMapping("/flow/trace")
    public R<Map<String, Object>> traceFlow(@RequestBody Map<String, Object> params) {
        try {
            String id = params.get("flowId") != null ? params.get("flowId").toString() : null;
            TblAssetFlow flow = id != null ? assetFlowMapper.selectById(id) : null;
            Map<String, Object> result = new HashMap<>();
            if (flow != null) {
                result.put("sourceName", flow.getSourceCompanyName());
                result.put("targetName", flow.getTargetCompanyName());
                result.put("assetName", flow.getAssetName());
                result.put("flowAmount", flow.getFlowAmount());
                result.put("flowType", flow.getFlowType());
                result.put("flowDate", flow.getFlowDate());
                result.put("path", flow.getSourceCompanyName() + " → " + flow.getTargetCompanyName());
                result.put("status", "TRACED");
            }
            return R.success(result);
        } catch (Exception e) { return R.fail("追踪失败：" + e.getMessage()); }
    }

    @Operation(summary = "分析资产流向路径")
    @PostMapping("/flow/path")
    public R<Map<String, Object>> flowPath(@RequestBody Map<String, Object> params) {
        try { return R.success(new HashMap<>()); }
        catch (Exception e) { return R.fail("分析失败：" + e.getMessage()); }
    }

    @Operation(summary = "构建资产流向图")
    @PostMapping("/flow/chart")
    public R<Map<String, Object>> flowChart(@RequestBody Map<String, Object> params) {
        try {
            List<TblPropertyTransaction> transactions = propertyTransactionMapper.selectList(null);
            Map<String, Object> result = new HashMap<>();
            List<Map<String, Object>> nodes = new ArrayList<>();
            List<Map<String, Object>> links = new ArrayList<>();
            for (TblPropertyTransaction t : transactions) {
                if (t.getCounterparty() != null) {
                    Map<String, Object> node = new HashMap<>();
                    node.put("name", t.getCounterparty());
                    node.put("amount", t.getTransactionAmount());
                    nodes.add(node);
                }
            }
            result.put("nodes", nodes); result.put("links", links);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "分析资产流向模式")
    @PostMapping("/flow/pattern")
    public R<Map<String, Object>> flowPattern(@RequestBody Map<String, Object> params) {
        try { return R.success(new HashMap<>()); }
        catch (Exception e) { return R.fail("分析失败：" + e.getMessage()); }
    }

    @Operation(summary = "检测异常资产流向")
    @PostMapping("/flow/abnormal")
    public R<List<Map<String, Object>>> abnormalFlow(@RequestBody Map<String, Object> params) {
        try { return R.success(new ArrayList<>()); }
        catch (Exception e) { return R.fail("检测失败：" + e.getMessage()); }
    }

    @Operation(summary = "分析资产流向速度")
    @PostMapping("/flow/velocity")
    public R<Map<String, Object>> flowVelocity(@RequestBody Map<String, Object> params) {
        try { return R.success(new HashMap<>()); }
        catch (Exception e) { return R.fail("分析失败：" + e.getMessage()); }
    }

    @Operation(summary = "计算资产流向强度")
    @PostMapping("/flow/intensity")
    public R<Map<String, Object>> flowIntensity(@RequestBody Map<String, Object> params) {
        try { return R.success(new HashMap<>()); }
        catch (Exception e) { return R.fail("计算失败：" + e.getMessage()); }
    }

    @Operation(summary = "分析资产流向趋势")
    @PostMapping("/flow/trend")
    public R<List<Map<String, Object>>> flowTrend(@RequestBody Map<String, Object> params) {
        try { return R.success(new ArrayList<>()); }
        catch (Exception e) { return R.fail("分析失败：" + e.getMessage()); }
    }

    @Operation(summary = "预测资产流向")
    @PostMapping("/flow/predict")
    public R<Map<String, Object>> predictFlow(@RequestBody Map<String, Object> params) {
        try { return R.success(new HashMap<>()); }
        catch (Exception e) { return R.fail("预测失败：" + e.getMessage()); }
    }

    @Operation(summary = "分析资产流向集中度")
    @PostMapping("/flow/concentration")
    public R<Map<String, Object>> flowConcentration(@RequestBody Map<String, Object> params) {
        try { return R.success(new HashMap<>()); }
        catch (Exception e) { return R.fail("分析失败：" + e.getMessage()); }
    }

    @Operation(summary = "识别资产流向关键节点")
    @PostMapping("/flow/key-nodes")
    public R<List<Map<String, Object>>> flowKeyNodes(@RequestBody Map<String, Object> params) {
        try { return R.success(new ArrayList<>()); }
        catch (Exception e) { return R.fail("识别失败：" + e.getMessage()); }
    }

    @Operation(summary = "分析资产流向网络")
    @PostMapping("/flow/network")
    public R<Map<String, Object>> flowNetwork(@RequestBody Map<String, Object> params) {
        try { return R.success(new HashMap<>()); }
        catch (Exception e) { return R.fail("分析失败：" + e.getMessage()); }
    }

    @Operation(summary = "评估资产流向风险")
    @PostMapping("/flow/risk")
    public R<Map<String, Object>> flowRisk(@RequestBody Map<String, Object> params) {
        try { return R.success(new HashMap<>()); }
        catch (Exception e) { return R.fail("评估失败：" + e.getMessage()); }
    }

    @Operation(summary = "监控资产流向实时状态")
    @PostMapping("/flow/realtime")
    public R<Map<String, Object>> flowRealtime(@RequestBody Map<String, Object> params) {
        try { return R.success(new HashMap<>()); }
        catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "分析资产流向周期性")
    @PostMapping("/flow/cyclicity")
    public R<Map<String, Object>> flowCyclicity(@RequestBody Map<String, Object> params) {
        try { return R.success(new HashMap<>()); }
        catch (Exception e) { return R.fail("分析失败：" + e.getMessage()); }
    }

    @Operation(summary = "对比资产流向")
    @PostMapping("/flow/compare")
    public R<Map<String, Object>> flowCompare(@RequestBody Map<String, Object> params) {
        try { return R.success(new HashMap<>()); }
        catch (Exception e) { return R.fail("对比失败：" + e.getMessage()); }
    }

    @Operation(summary = "分析资产流向影响因素")
    @PostMapping("/flow/factors")
    public R<List<Map<String, Object>>> flowFactors(@RequestBody Map<String, Object> params) {
        try { return R.success(new ArrayList<>()); }
        catch (Exception e) { return R.fail("分析失败：" + e.getMessage()); }
    }

    @Operation(summary = "优化资产流向配置")
    @PostMapping("/flow/optimize")
    public R<Map<String, Object>> flowOptimize(@RequestBody Map<String, Object> params) {
        try { return R.success(new HashMap<>()); }
        catch (Exception e) { return R.fail("优化失败：" + e.getMessage()); }
    }

    @Operation(summary = "模拟资产流向变化")
    @PostMapping("/flow/simulate")
    public R<Map<String, Object>> flowSimulate(@RequestBody Map<String, Object> params) {
        try { return R.success(new HashMap<>()); }
        catch (Exception e) { return R.fail("模拟失败：" + e.getMessage()); }
    }

    @Operation(summary = "分析资产流向稳定性")
    @PostMapping("/flow/stability")
    public R<Map<String, Object>> flowStability(@RequestBody Map<String, Object> params) {
        try { return R.success(new HashMap<>()); }
        catch (Exception e) { return R.fail("分析失败：" + e.getMessage()); }
    }

    @Operation(summary = "获取资产流向统计")
    @PostMapping("/flow/statistics")
    public R<Map<String, Object>> flowStatistics(@RequestBody(required = false) Map<String, Object> params) {
        try {
            Map<String, Object> result = new HashMap<>();
            Long totalFlows = assetFlowMapper.selectCount(null);
            Long relatedPartyCount = assetFlowMapper.selectCount(
                new LambdaQueryWrapper<TblAssetFlow>().eq(TblAssetFlow::getIsRelatedParty, "Y"));
            Long highRiskCount = assetFlowMapper.selectCount(
                new LambdaQueryWrapper<TblAssetFlow>().eq(TblAssetFlow::getRiskLevel, "HIGH"));
            List<TblAssetFlow> all = assetFlowMapper.selectList(null);
            BigDecimal totalAmount = all.stream().filter(f -> f.getFlowAmount() != null)
                .map(TblAssetFlow::getFlowAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
            result.put("totalFlows", totalFlows);
            result.put("totalAmount", totalAmount);
            result.put("relatedPartyCount", relatedPartyCount);
            result.put("highRiskCount", highRiskCount);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "生成资产流向报告")
    @PostMapping("/flow/report")
    public R<Map<String, Object>> flowReport(@RequestBody Map<String, Object> params) {
        try { return R.success(new HashMap<>()); }
        catch (Exception e) { return R.fail("生成失败：" + e.getMessage()); }
    }

    @Operation(summary = "批量追踪资产流向")
    @PostMapping("/flow/batch/trace")
    public R<Boolean> batchTraceFlow(@RequestBody Map<String, Object> params) { return R.success(true); }

    @Operation(summary = "批量删除资产流向")
    @PostMapping("/flow/batch/delete")
    public R<Boolean> batchDeleteFlow(@RequestBody Map<String, Object> params) { return R.success(true); }

    @Operation(summary = "导出资产流向数据")
    @PostMapping("/flow/export")
    public R<List<Map<String, Object>>> exportFlow(@RequestBody Map<String, Object> params) {
        try { return R.success(new ArrayList<>()); }
        catch (Exception e) { return R.fail("导出失败：" + e.getMessage()); }
    }

    @Operation(summary = "导入资产流向数据")
    @PostMapping("/flow/import")
    public R<Boolean> importFlow() { return R.success(true); }

    @Operation(summary = "设置资产流向预警")
    @PostMapping("/flow/alert")
    public R<Boolean> flowAlert(@RequestBody Map<String, Object> params) { return R.success(true); }

    @Operation(summary = "分析资产流向合规性")
    @PostMapping("/flow/compliance")
    public R<Map<String, Object>> flowCompliance(@RequestBody Map<String, Object> params) {
        try { return R.success(new HashMap<>()); }
        catch (Exception e) { return R.fail("分析失败：" + e.getMessage()); }
    }

    // ==================== 资产映射 ====================

    @Operation(summary = "资产映射列表")
    @PostMapping("/mapping/list")
    public R<PageResult<TblAssetMapping>> mappingList(@RequestBody Map<String, Object> params) {
        try {
            int pn = getPageNumber(params); int ps = getPageSize(params);
            LambdaQueryWrapper<TblAssetMapping> w = new LambdaQueryWrapper<>();
            if (params.get("companyName") != null && StringUtils.isNotEmpty(params.get("companyName").toString())) {
                w.like(TblAssetMapping::getCompanyName, params.get("companyName").toString());
            }
            if (params.get("assetCategory") != null && StringUtils.isNotEmpty(params.get("assetCategory").toString())) {
                w.eq(TblAssetMapping::getAssetCategory, params.get("assetCategory").toString());
            }
            if (params.get("mappingStatus") != null && StringUtils.isNotEmpty(params.get("mappingStatus").toString())) {
                w.eq(TblAssetMapping::getMappingStatus, params.get("mappingStatus").toString());
            }
            w.orderByDesc(TblAssetMapping::getCreateTime);
            Page<TblAssetMapping> page = assetMappingMapper.selectPage(new Page<>(pn, ps), w);
            return R.success(PageResult.of(page));
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "资产映射详情")
    @GetMapping("/mapping/{id}")
    public R<TblAssetMapping> mappingDetail(@PathVariable String id) {
        try { return R.success(assetMappingMapper.selectById(id)); }
        catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "新增资产映射")
    @PostMapping("/mapping/add")
    public R<Boolean> mappingAdd(@RequestBody TblAssetMapping record) {
        try { record.setCreateTime(LocalDateTime.now()); assetMappingMapper.insert(record); return R.success(true); }
        catch (Exception e) { return R.fail("新增失败：" + e.getMessage()); }
    }

    @Operation(summary = "更新资产映射")
    @PostMapping("/mapping/update")
    public R<Boolean> mappingUpdate(@RequestBody TblAssetMapping record) {
        try { record.setUpdateTime(LocalDateTime.now()); assetMappingMapper.updateById(record); return R.success(true); }
        catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); }
    }

    @Operation(summary = "删除资产映射")
    @DeleteMapping("/mapping/{id}")
    public R<Boolean> mappingDelete(@PathVariable String id) {
        try { assetMappingMapper.deleteById(id); return R.success(true); }
        catch (Exception e) { return R.fail("删除失败：" + e.getMessage()); }
    }

    @Operation(summary = "构建资产映射关系")
    @PostMapping("/mapping/build")
    public R<Map<String, Object>> buildMapping(@RequestBody Map<String, Object> params) {
        try { return R.success(new HashMap<>()); }
        catch (Exception e) { return R.fail("构建失败：" + e.getMessage()); }
    }

    @Operation(summary = "分析资产关联关系")
    @PostMapping("/mapping/relations")
    public R<List<Map<String, Object>>> mappingRelations(@RequestBody Map<String, Object> params) {
        try { return R.success(new ArrayList<>()); }
        catch (Exception e) { return R.fail("分析失败：" + e.getMessage()); }
    }

    @Operation(summary = "获取资产映射图谱")
    @PostMapping("/mapping/chart")
    public R<Map<String, Object>> mappingChart(@RequestBody Map<String, Object> params) {
        try {
            List<EquityStructure> list = equityStructureMapper.selectList(null);
            Map<String, Object> result = new HashMap<>();
            List<Map<String, Object>> nodes = new ArrayList<>();
            List<Map<String, Object>> links = new ArrayList<>();
            for (EquityStructure es : list) {
                Map<String, Object> node = new HashMap<>();
                node.put("id", es.getEquityId()); node.put("name", es.getInvestorName());
                node.put("ratio", es.getShareholdingRatio()); nodes.add(node);
                if (StringUtils.isNotBlank(es.getEnterpriseId())) {
                    Map<String, Object> link = new HashMap<>();
                    link.put("source", es.getEnterpriseId()); link.put("target", es.getEquityId());
                    link.put("ratio", es.getShareholdingRatio()); links.add(link);
                }
            }
            result.put("nodes", nodes); result.put("links", links);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "追踪资产流向(映射)")
    @PostMapping("/mapping/trace")
    public R<Map<String, Object>> mappingTrace(@RequestBody Map<String, Object> params) {
        try { return R.success(new HashMap<>()); }
        catch (Exception e) { return R.fail("追踪失败：" + e.getMessage()); }
    }

    @Operation(summary = "识别资产集群")
    @PostMapping("/mapping/clusters")
    public R<List<Map<String, Object>>> assetClusters(@RequestBody Map<String, Object> params) {
        try { return R.success(new ArrayList<>()); }
        catch (Exception e) { return R.fail("识别失败：" + e.getMessage()); }
    }

    @Operation(summary = "检测资产异常")
    @PostMapping("/mapping/anomalies")
    public R<List<Map<String, Object>>> assetAnomalies(@RequestBody Map<String, Object> params) {
        try { return R.success(new ArrayList<>()); }
        catch (Exception e) { return R.fail("检测失败：" + e.getMessage()); }
    }

    @Operation(summary = "分析资产价值分布")
    @PostMapping("/mapping/value-distribution")
    public R<Map<String, Object>> valueDistribution(@RequestBody Map<String, Object> params) {
        try { return R.success(new HashMap<>()); }
        catch (Exception e) { return R.fail("分析失败：" + e.getMessage()); }
    }

    @Operation(summary = "获取资产映射统计")
    @PostMapping("/mapping/statistics")
    public R<Map<String, Object>> mappingStatistics(@RequestBody(required = false) Map<String, Object> params) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("totalMappings", assetMappingMapper.selectCount(null));
            result.put("matchedCount", assetMappingMapper.selectCount(
                new LambdaQueryWrapper<TblAssetMapping>().eq(TblAssetMapping::getMappingStatus, "MATCHED")));
            result.put("discrepancyCount", assetMappingMapper.selectCount(
                new LambdaQueryWrapper<TblAssetMapping>().eq(TblAssetMapping::getMappingStatus, "DISCREPANCY")));
            result.put("unmatchedCount", assetMappingMapper.selectCount(
                new LambdaQueryWrapper<TblAssetMapping>().eq(TblAssetMapping::getMappingStatus, "UNMATCHED")));
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "验证资产映射准确性")
    @PostMapping("/mapping/validate")
    public R<Map<String, Object>> validateMapping(@RequestBody Map<String, Object> params) {
        try {
            String id = params.get("mappingId") != null ? params.get("mappingId").toString() : null;
            TblAssetMapping record = id != null ? assetMappingMapper.selectById(id) : null;
            Map<String, Object> result = new HashMap<>();
            if (record != null) {
                boolean isValid = "MATCHED".equals(record.getMappingStatus());
                result.put("valid", isValid);
                result.put("status", record.getMappingStatus());
                result.put("message", isValid ? "资产映射验证通过，账实相符" : "资产映射存在差异，需核查处理");
                result.put("discrepancyType", record.getDiscrepancyType());
                result.put("discrepancyAmount", record.getDiscrepancyAmount());
            } else {
                result.put("valid", false);
                result.put("message", "未找到映射记录");
            }
            return R.success(result);
        } catch (Exception e) { return R.fail("验证失败：" + e.getMessage()); }
    }

    @Operation(summary = "同步资产数据")
    @PostMapping("/mapping/sync")
    public R<Boolean> syncAssetData(@RequestBody Map<String, Object> params) { return R.success(true); }

    @Operation(summary = "获取资产变更历史")
    @PostMapping("/mapping/history")
    public R<List<Map<String, Object>>> assetHistory(@RequestBody Map<String, Object> params) {
        try { return R.success(new ArrayList<>()); }
        catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "分析资产风险")
    @PostMapping("/mapping/risk")
    public R<Map<String, Object>> assetRisk(@RequestBody Map<String, Object> params) {
        try { return R.success(new HashMap<>()); }
        catch (Exception e) { return R.fail("分析失败：" + e.getMessage()); }
    }

    @Operation(summary = "生成资产映射报告")
    @PostMapping("/mapping/report")
    public R<Map<String, Object>> mappingReport(@RequestBody Map<String, Object> params) {
        try { return R.success(new HashMap<>()); }
        catch (Exception e) { return R.fail("生成失败：" + e.getMessage()); }
    }

    @Operation(summary = "批量更新资产映射")
    @PostMapping("/mapping/batch/update")
    public R<Boolean> batchUpdateMapping(@RequestBody Map<String, Object> params) { return R.success(true); }

    @Operation(summary = "批量删除资产映射")
    @PostMapping("/mapping/batch/delete")
    public R<Boolean> batchDeleteMapping(@RequestBody Map<String, Object> params) { return R.success(true); }

    @Operation(summary = "导出资产映射数据")
    @PostMapping("/mapping/export")
    public R<List<Map<String, Object>>> exportMapping(@RequestBody Map<String, Object> params) {
        try { return R.success(new ArrayList<>()); }
        catch (Exception e) { return R.fail("导出失败：" + e.getMessage()); }
    }

    @Operation(summary = "导入资产映射数据")
    @PostMapping("/mapping/import")
    public R<Boolean> importMapping() { return R.success(true); }

    @Operation(summary = "获取资产网络数据")
    @PostMapping("/mapping/network")
    public R<Map<String, Object>> assetNetwork(@RequestBody Map<String, Object> params) {
        try { return R.success(new HashMap<>()); }
        catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "分析资产依赖关系")
    @PostMapping("/mapping/dependencies")
    public R<List<Map<String, Object>>> assetDependencies(@RequestBody Map<String, Object> params) {
        try { return R.success(new ArrayList<>()); }
        catch (Exception e) { return R.fail("分析失败：" + e.getMessage()); }
    }

    @Operation(summary = "优化资产配置")
    @PostMapping("/mapping/optimize")
    public R<Map<String, Object>> optimizeAsset(@RequestBody Map<String, Object> params) {
        try { return R.success(new HashMap<>()); }
        catch (Exception e) { return R.fail("优化失败：" + e.getMessage()); }
    }

    @Operation(summary = "评估资产质量")
    @PostMapping("/mapping/quality")
    public R<Map<String, Object>> qualityAssessment(@RequestBody Map<String, Object> params) {
        try { return R.success(new HashMap<>()); }
        catch (Exception e) { return R.fail("评估失败：" + e.getMessage()); }
    }

    @Operation(summary = "监控资产状态")
    @PostMapping("/mapping/monitor")
    public R<Map<String, Object>> monitorAsset(@RequestBody Map<String, Object> params) {
        try { return R.success(new HashMap<>()); }
        catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 交叉持股 ====================

    @Operation(summary = "交叉持股分析列表")
    @PostMapping("/cross-holding/list")
    public R<PageResult<TblCrossHolding>> crossHoldingList(@RequestBody Map<String, Object> params) {
        try {
            int pn = getPageNumber(params); int ps = getPageSize(params);
            LambdaQueryWrapper<TblCrossHolding> w = new LambdaQueryWrapper<>();
            if (params.get("companyAName") != null && StringUtils.isNotEmpty(params.get("companyAName").toString())) {
                w.like(TblCrossHolding::getCompanyAName, params.get("companyAName").toString());
            }
            if (params.get("companyBName") != null && StringUtils.isNotEmpty(params.get("companyBName").toString())) {
                w.like(TblCrossHolding::getCompanyBName, params.get("companyBName").toString());
            }
            if (params.get("crossType") != null && StringUtils.isNotEmpty(params.get("crossType").toString())) {
                w.eq(TblCrossHolding::getCrossType, params.get("crossType").toString());
            }
            if (params.get("riskLevel") != null && StringUtils.isNotEmpty(params.get("riskLevel").toString())) {
                w.eq(TblCrossHolding::getRiskLevel, params.get("riskLevel").toString());
            }
            w.orderByDesc(TblCrossHolding::getCreateTime);
            Page<TblCrossHolding> page = crossHoldingMapper.selectPage(new Page<>(pn, ps), w);
            return R.success(PageResult.of(page));
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "交叉持股详情")
    @GetMapping("/cross-holding/{id}")
    public R<TblCrossHolding> crossHoldingDetail(@PathVariable String id) {
        try { return R.success(crossHoldingMapper.selectById(id)); }
        catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "新增交叉持股分析")
    @PostMapping("/cross-holding/add")
    public R<Boolean> crossHoldingAdd(@RequestBody TblCrossHolding record) {
        try { record.setCreateTime(LocalDateTime.now()); crossHoldingMapper.insert(record); return R.success(true); }
        catch (Exception e) { return R.fail("新增失败：" + e.getMessage()); }
    }

    @Operation(summary = "更新交叉持股分析")
    @PostMapping("/cross-holding/update")
    public R<Boolean> crossHoldingUpdate(@RequestBody TblCrossHolding record) {
        try { record.setUpdateTime(LocalDateTime.now()); crossHoldingMapper.updateById(record); return R.success(true); }
        catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); }
    }

    @Operation(summary = "删除交叉持股分析")
    @DeleteMapping("/cross-holding/{id}")
    public R<Boolean> crossHoldingDelete(@PathVariable String id) {
        try { crossHoldingMapper.deleteById(id); return R.success(true); }
        catch (Exception e) { return R.fail("删除失败：" + e.getMessage()); }
    }

    @Operation(summary = "获取交叉持股统计")
    @PostMapping("/cross-holding/statistics")
    public R<Map<String, Object>> crossHoldingStatistics(@RequestBody(required = false) Map<String, Object> params) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("total", crossHoldingMapper.selectCount(null));
            result.put("circularCount", crossHoldingMapper.selectCount(new LambdaQueryWrapper<TblCrossHolding>().eq(TblCrossHolding::getIsCircular, "Y")));
            result.put("highRiskCount", crossHoldingMapper.selectCount(new LambdaQueryWrapper<TblCrossHolding>().eq(TblCrossHolding::getRiskLevel, "HIGH")));
            List<TblCrossHolding> all = crossHoldingMapper.selectList(null);
            BigDecimal totalInflation = all.stream().filter(c -> c.getCapitalInflation() != null).map(TblCrossHolding::getCapitalInflation).reduce(BigDecimal.ZERO, BigDecimal::add);
            result.put("totalInflation", totalInflation);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "检测交叉持股关系")
    @PostMapping("/cross-holding/detect")
    public R<List<Map<String, Object>>> detectCrossHolding(@RequestBody Map<String, Object> params) {
        try { return R.success(new ArrayList<>()); }
        catch (Exception e) { return R.fail("检测失败：" + e.getMessage()); }
    }

    @Operation(summary = "分析交叉持股网络")
    @PostMapping("/cross-holding/network")
    public R<Map<String, Object>> crossHoldingNetwork(@RequestBody Map<String, Object> params) {
        try {
            String holdingId = (String) params.get("holdingId");
            if (StringUtils.isEmpty(holdingId)) { return R.fail("holdingId不能为空"); }
            TblCrossHolding current = crossHoldingMapper.selectById(holdingId);
            if (current == null) { return R.fail("未找到交叉持股记录"); }

            // 查询与companyA和companyB相关的所有交叉持股记录
            LambdaQueryWrapper<TblCrossHolding> wrapper = new LambdaQueryWrapper<>();
            wrapper.and(w -> w.eq(TblCrossHolding::getCompanyAId, current.getCompanyAId())
                    .or().eq(TblCrossHolding::getCompanyBId, current.getCompanyAId())
                    .or().eq(TblCrossHolding::getCompanyAId, current.getCompanyBId())
                    .or().eq(TblCrossHolding::getCompanyBId, current.getCompanyBId()));
            List<TblCrossHolding> relatedList = crossHoldingMapper.selectList(wrapper);

            // 构建nodes（去重）
            Map<String, Map<String, Object>> nodeMap = new LinkedHashMap<>();
            for (TblCrossHolding ch : relatedList) {
                nodeMap.computeIfAbsent(ch.getCompanyAId(), k -> {
                    Map<String, Object> node = new HashMap<>();
                    node.put("id", ch.getCompanyAId());
                    node.put("name", ch.getCompanyAName());
                    node.put("type", "company");
                    return node;
                });
                nodeMap.computeIfAbsent(ch.getCompanyBId(), k -> {
                    Map<String, Object> node = new HashMap<>();
                    node.put("id", ch.getCompanyBId());
                    node.put("name", ch.getCompanyBName());
                    node.put("type", "company");
                    return node;
                });
            }

            // 构建links
            List<Map<String, Object>> links = new ArrayList<>();
            for (TblCrossHolding ch : relatedList) {
                if (ch.getAHoldBRatio() != null && ch.getAHoldBRatio().compareTo(BigDecimal.ZERO) > 0) {
                    Map<String, Object> link = new HashMap<>();
                    link.put("source", ch.getCompanyAId());
                    link.put("target", ch.getCompanyBId());
                    link.put("ratio", ch.getAHoldBRatio());
                    link.put("label", ch.getAHoldBRatio().setScale(2, RoundingMode.HALF_UP) + "%");
                    links.add(link);
                }
                if (ch.getBHoldARatio() != null && ch.getBHoldARatio().compareTo(BigDecimal.ZERO) > 0) {
                    Map<String, Object> link = new HashMap<>();
                    link.put("source", ch.getCompanyBId());
                    link.put("target", ch.getCompanyAId());
                    link.put("ratio", ch.getBHoldARatio());
                    link.put("label", ch.getBHoldARatio().setScale(2, RoundingMode.HALF_UP) + "%");
                    links.add(link);
                }
            }

            // 构建路径描述
            List<String> paths = new ArrayList<>();
            if ("Y".equals(current.getIsCircular())) {
                paths.add(current.getCompanyAName() + " → " + current.getCompanyBName() + " → " + current.getCompanyAName());
            }

            Map<String, Object> result = new HashMap<>();
            result.put("nodes", new ArrayList<>(nodeMap.values()));
            result.put("links", links);
            result.put("paths", paths);
            result.put("isCircular", "Y".equals(current.getIsCircular()));
            result.put("chainLength", current.getChainLength() != null ? current.getChainLength() : 2);
            return R.success(result);
        } catch (Exception e) { return R.fail("分析失败：" + e.getMessage()); }
    }

    @Operation(summary = "识别交叉持股环路")
    @PostMapping("/cross-holding/loops")
    public R<List<Map<String, Object>>> crossHoldingLoops(@RequestBody Map<String, Object> params) {
        try { return R.success(new ArrayList<>()); }
        catch (Exception e) { return R.fail("识别失败：" + e.getMessage()); }
    }

    @Operation(summary = "计算交叉持股强度")
    @PostMapping("/cross-holding/intensity")
    public R<Map<String, Object>> crossHoldingIntensity(@RequestBody Map<String, Object> params) {
        try { return R.success(new HashMap<>()); }
        catch (Exception e) { return R.fail("计算失败：" + e.getMessage()); }
    }

    @Operation(summary = "分析交叉持股层级")
    @PostMapping("/cross-holding/levels")
    public R<List<Map<String, Object>>> crossHoldingLevels(@RequestBody Map<String, Object> params) {
        try { return R.success(new ArrayList<>()); }
        catch (Exception e) { return R.fail("分析失败：" + e.getMessage()); }
    }

    @Operation(summary = "评估交叉持股风险")
    @PostMapping("/cross-holding/risk")
    public R<Map<String, Object>> crossHoldingRisk(@RequestBody Map<String, Object> params) {
        try {
            String holdingId = (String) params.get("holdingId");
            if (StringUtils.isEmpty(holdingId)) { return R.fail("holdingId不能为空"); }
            TblCrossHolding current = crossHoldingMapper.selectById(holdingId);
            if (current == null) { return R.fail("未找到交叉持股记录"); }

            BigDecimal aRatio = current.getAHoldBRatio() != null ? current.getAHoldBRatio() : BigDecimal.ZERO;
            BigDecimal bRatio = current.getBHoldARatio() != null ? current.getBHoldARatio() : BigDecimal.ZERO;
            BigDecimal inflation = current.getCapitalInflation() != null ? current.getCapitalInflation() : BigDecimal.ZERO;
            boolean isCircular = "Y".equals(current.getIsCircular());

            // 资本虚增风险：基于capitalInflation金额
            int inflationScore = inflation.compareTo(new BigDecimal("10000")) >= 0 ? 95
                    : inflation.compareTo(new BigDecimal("5000")) >= 0 ? 90
                    : inflation.compareTo(new BigDecimal("1000")) >= 0 ? 70
                    : inflation.compareTo(new BigDecimal("500")) >= 0 ? 50 : 30;

            // 控制权风险：基于持股比例
            BigDecimal maxRatio = aRatio.max(bRatio);
            int controlScore = maxRatio.compareTo(new BigDecimal("50")) >= 0 ? 90
                    : maxRatio.compareTo(new BigDecimal("30")) >= 0 ? 70
                    : maxRatio.compareTo(new BigDecimal("20")) >= 0 ? 60 : 40;

            // 合规风险：基于是否超出监管比例（假设20%为监管红线）
            int complianceScore = (aRatio.compareTo(new BigDecimal("20")) > 0 || bRatio.compareTo(new BigDecimal("20")) > 0) ? 85 : 45;

            // 流动性风险：基于互持比例之和
            BigDecimal totalRatio = aRatio.add(bRatio);
            int liquidityScore = totalRatio.compareTo(new BigDecimal("60")) >= 0 ? 85
                    : totalRatio.compareTo(new BigDecimal("40")) >= 0 ? 70
                    : totalRatio.compareTo(new BigDecimal("20")) >= 0 ? 60 : 35;

            // 传导风险：基于是否环形及链长
            int contagionScore = isCircular ? 75 : 40;
            if (current.getChainLength() != null && current.getChainLength() > 3) {
                contagionScore = Math.min(contagionScore + 15, 95);
            }

            // 计算总分
            int overallScore = (inflationScore + controlScore + complianceScore + liquidityScore + contagionScore) / 5;
            String overallRisk = overallScore >= 80 ? "HIGH" : overallScore >= 60 ? "MEDIUM" : "LOW";

            List<Map<String, Object>> dimensions = new ArrayList<>();
            dimensions.add(buildDimension("资本虚增风险", inflationScore, "资本虚增金额" + (inflationScore >= 80 ? "较大" : inflationScore >= 60 ? "中等" : "较小")));
            dimensions.add(buildDimension("控制权风险", controlScore, maxRatio.compareTo(new BigDecimal("30")) >= 0 ? "存在间接控制关系" : "控制权影响有限"));
            dimensions.add(buildDimension("合规风险", complianceScore, complianceScore >= 80 ? "超出监管比例限制" : "在监管比例范围内"));
            dimensions.add(buildDimension("流动性风险", liquidityScore, totalRatio.compareTo(new BigDecimal("40")) >= 0 ? "持股比例较高影响流动性" : "流动性影响可控"));
            dimensions.add(buildDimension("传导风险", contagionScore, isCircular ? "存在风险传导路径" : "传导路径较短"));

            // 历史趋势（模拟季度数据）
            List<Map<String, Object>> historicalTrend = new ArrayList<>();
            int baseScore = Math.max(overallScore - 15, 30);
            String[] quarters = {"2024-Q1", "2024-Q2", "2024-Q3", "2024-Q4"};
            for (int i = 0; i < quarters.length; i++) {
                Map<String, Object> trend = new HashMap<>();
                trend.put("date", quarters[i]);
                trend.put("score", Math.min(baseScore + i * 5, overallScore));
                historicalTrend.add(trend);
            }

            Map<String, Object> result = new HashMap<>();
            result.put("overallRisk", overallRisk);
            result.put("overallScore", overallScore);
            result.put("dimensions", dimensions);
            result.put("historicalTrend", historicalTrend);
            return R.success(result);
        } catch (Exception e) { return R.fail("评估失败：" + e.getMessage()); }
    }

    private Map<String, Object> buildDimension(String name, int score, String description) {
        Map<String, Object> dim = new HashMap<>();
        dim.put("name", name);
        dim.put("score", score);
        dim.put("level", score >= 80 ? "HIGH" : score >= 60 ? "MEDIUM" : "LOW");
        dim.put("description", description);
        return dim;
    }

    @Operation(summary = "分析交叉持股稳定性")
    @PostMapping("/cross-holding/stability")
    public R<Map<String, Object>> crossHoldingStability(@RequestBody Map<String, Object> params) {
        try { return R.success(new HashMap<>()); }
        catch (Exception e) { return R.fail("分析失败：" + e.getMessage()); }
    }

    @Operation(summary = "检测异常交叉持股")
    @PostMapping("/cross-holding/anomalies")
    public R<List<Map<String, Object>>> crossHoldingAnomalies(@RequestBody Map<String, Object> params) {
        try { return R.success(new ArrayList<>()); }
        catch (Exception e) { return R.fail("检测失败：" + e.getMessage()); }
    }

    @Operation(summary = "分析交叉持股影响")
    @PostMapping("/cross-holding/impact")
    public R<Map<String, Object>> crossHoldingImpact(@RequestBody Map<String, Object> params) {
        try { return R.success(new HashMap<>()); }
        catch (Exception e) { return R.fail("分析失败：" + e.getMessage()); }
    }

    @Operation(summary = "模拟交叉持股变化")
    @PostMapping("/cross-holding/simulate")
    public R<Map<String, Object>> crossHoldingSimulate(@RequestBody Map<String, Object> params) {
        try {
            String holdingId = (String) params.get("holdingId");
            if (StringUtils.isEmpty(holdingId)) { return R.fail("holdingId不能为空"); }
            TblCrossHolding current = crossHoldingMapper.selectById(holdingId);
            if (current == null) { return R.fail("未找到交叉持股记录"); }

            BigDecimal newAHoldBRatio = params.get("newAHoldBRatio") != null
                    ? new BigDecimal(params.get("newAHoldBRatio").toString()) : current.getAHoldBRatio();
            BigDecimal newBHoldARatio = params.get("newBHoldARatio") != null
                    ? new BigDecimal(params.get("newBHoldARatio").toString()) : current.getBHoldARatio();

            BigDecimal oldARatio = current.getAHoldBRatio() != null ? current.getAHoldBRatio() : BigDecimal.ZERO;
            BigDecimal oldBRatio = current.getBHoldARatio() != null ? current.getBHoldARatio() : BigDecimal.ZERO;
            BigDecimal baseCapital = current.getCapitalInflation() != null && oldARatio.multiply(oldBRatio).compareTo(BigDecimal.ZERO) > 0
                    ? current.getCapitalInflation().multiply(new BigDecimal("10000")).divide(oldARatio.multiply(oldBRatio), 2, RoundingMode.HALF_UP)
                    : new BigDecimal("100000");

            // 计算变化前资本虚增
            BigDecimal beforeInflation = oldARatio.multiply(oldBRatio).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP)
                    .multiply(baseCapital).divide(new BigDecimal("10000"), 2, RoundingMode.HALF_UP);
            String beforeRisk = beforeInflation.compareTo(new BigDecimal("3000")) >= 0 ? "HIGH"
                    : beforeInflation.compareTo(new BigDecimal("1000")) >= 0 ? "MEDIUM" : "LOW";

            // 计算变化后资本虚增
            BigDecimal afterInflation = newAHoldBRatio.multiply(newBHoldARatio).divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP)
                    .multiply(baseCapital).divide(new BigDecimal("10000"), 2, RoundingMode.HALF_UP);
            String afterRisk = afterInflation.compareTo(new BigDecimal("3000")) >= 0 ? "HIGH"
                    : afterInflation.compareTo(new BigDecimal("1000")) >= 0 ? "MEDIUM" : "LOW";

            BigDecimal inflationReduction = beforeInflation.subtract(afterInflation);
            String suggestion = inflationReduction.compareTo(BigDecimal.ZERO) > 0 ? "建议执行此方案" : "该方案会增加风险，不建议执行";

            Map<String, Object> before = new HashMap<>();
            before.put("aHoldBRatio", oldARatio);
            before.put("bHoldARatio", oldBRatio);
            before.put("capitalInflation", beforeInflation);
            before.put("riskLevel", beforeRisk);

            Map<String, Object> after = new HashMap<>();
            after.put("aHoldBRatio", newAHoldBRatio);
            after.put("bHoldARatio", newBHoldARatio);
            after.put("capitalInflation", afterInflation);
            after.put("riskLevel", afterRisk);

            Map<String, Object> impact = new HashMap<>();
            impact.put("inflationReduction", inflationReduction);
            impact.put("riskChange", beforeRisk + " → " + afterRisk);
            impact.put("suggestion", suggestion);

            Map<String, Object> result = new HashMap<>();
            result.put("before", before);
            result.put("after", after);
            result.put("impact", impact);
            return R.success(result);
        } catch (Exception e) { return R.fail("模拟失败：" + e.getMessage()); }
    }

    @Operation(summary = "优化交叉持股结构")
    @PostMapping("/cross-holding/optimize")
    public R<Map<String, Object>> crossHoldingOptimize(@RequestBody Map<String, Object> params) {
        try {
            String holdingId = (String) params.get("holdingId");
            if (StringUtils.isEmpty(holdingId)) { return R.fail("holdingId不能为空"); }
            TblCrossHolding current = crossHoldingMapper.selectById(holdingId);
            if (current == null) { return R.fail("未找到交叉持股记录"); }

            String riskLevel = current.getRiskLevel() != null ? current.getRiskLevel() : "MEDIUM";
            String crossType = current.getCrossType() != null ? current.getCrossType() : "DIRECT";
            BigDecimal inflation = current.getCapitalInflation() != null ? current.getCapitalInflation() : BigDecimal.ZERO;

            List<Map<String, Object>> suggestions = new ArrayList<>();

            // 根据风险等级和类型生成建议
            if ("HIGH".equals(riskLevel) || "CIRCULAR".equals(crossType)) {
                Map<String, Object> s1 = new HashMap<>();
                s1.put("priority", "HIGH");
                s1.put("title", "限期解除交叉持股");
                s1.put("description", "当前交叉持股风险等级为" + riskLevel + "，建议在规定期限内逐步解除交叉持股关系");
                s1.put("expectedEffect", "降低资本虚增" + inflation.setScale(0, RoundingMode.HALF_UP) + "万元");
                s1.put("timeline", "6个月内");
                s1.put("responsible", "股权管理部");
                suggestions.add(s1);
            }

            if (current.getAHoldBRatio() != null && current.getAHoldBRatio().compareTo(new BigDecimal("20")) > 0) {
                Map<String, Object> s2 = new HashMap<>();
                s2.put("priority", "MEDIUM");
                s2.put("title", "降低A持B比例");
                s2.put("description", "A持B比例为" + current.getAHoldBRatio() + "%，超出合理范围，建议降至20%以下");
                s2.put("expectedEffect", "降低控制权集中风险");
                s2.put("timeline", "3个月内");
                s2.put("responsible", "投资管理部");
                suggestions.add(s2);
            }

            if (current.getBHoldARatio() != null && current.getBHoldARatio().compareTo(new BigDecimal("20")) > 0) {
                Map<String, Object> s3 = new HashMap<>();
                s3.put("priority", "MEDIUM");
                s3.put("title", "降低B持A比例");
                s3.put("description", "B持A比例为" + current.getBHoldARatio() + "%，超出合理范围，建议降至20%以下");
                s3.put("expectedEffect", "降低互持风险");
                s3.put("timeline", "3个月内");
                s3.put("responsible", "投资管理部");
                suggestions.add(s3);
            }

            // 通用建议
            Map<String, Object> s4 = new HashMap<>();
            s4.put("priority", "LOW");
            s4.put("title", "加强信息披露与监控");
            s4.put("description", "建立交叉持股动态监控机制，定期披露持股变动信息");
            s4.put("expectedEffect", "提升透明度，降低合规风险");
            s4.put("timeline", "持续执行");
            s4.put("responsible", "合规管理部");
            suggestions.add(s4);

            Map<String, Object> result = new HashMap<>();
            result.put("holdingId", holdingId);
            result.put("currentRisk", riskLevel);
            result.put("suggestions", suggestions);
            result.put("expectedInflationReduction", inflation);
            result.put("targetRiskLevel", "LOW");
            return R.success(result);
        } catch (Exception e) { return R.fail("优化失败：" + e.getMessage()); }
    }

    @Operation(summary = "分析交叉持股趋势")
    @PostMapping("/cross-holding/trend")
    public R<List<Map<String, Object>>> crossHoldingTrend(@RequestBody Map<String, Object> params) {
        try { return R.success(new ArrayList<>()); }
        catch (Exception e) { return R.fail("分析失败：" + e.getMessage()); }
    }

    @Operation(summary = "对比交叉持股分析")
    @PostMapping("/cross-holding/compare")
    public R<Map<String, Object>> crossHoldingCompare(@RequestBody Map<String, Object> params) {
        try { return R.success(new HashMap<>()); }
        catch (Exception e) { return R.fail("对比失败：" + e.getMessage()); }
    }

    @Operation(summary = "获取交叉持股图谱")
    @PostMapping("/cross-holding/chart")
    public R<Map<String, Object>> crossHoldingChart(@RequestBody Map<String, Object> params) {
        try { return R.success(new HashMap<>()); }
        catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "分析交叉持股集群")
    @PostMapping("/cross-holding/clusters")
    public R<List<Map<String, Object>>> crossHoldingClusters(@RequestBody Map<String, Object> params) {
        try { return R.success(new ArrayList<>()); }
        catch (Exception e) { return R.fail("分析失败：" + e.getMessage()); }
    }

    @Operation(summary = "计算交叉持股密度")
    @PostMapping("/cross-holding/density")
    public R<Map<String, Object>> crossHoldingDensity(@RequestBody Map<String, Object> params) {
        try { return R.success(new HashMap<>()); }
        catch (Exception e) { return R.fail("计算失败：" + e.getMessage()); }
    }

    @Operation(summary = "分析交叉持股传导效应")
    @PostMapping("/cross-holding/contagion")
    public R<Map<String, Object>> crossHoldingContagion(@RequestBody Map<String, Object> params) {
        try { return R.success(new HashMap<>()); }
        catch (Exception e) { return R.fail("分析失败：" + e.getMessage()); }
    }

    @Operation(summary = "评估交叉持股合规性")
    @PostMapping("/cross-holding/compliance")
    public R<Map<String, Object>> crossHoldingCompliance(@RequestBody Map<String, Object> params) {
        try { return R.success(new HashMap<>()); }
        catch (Exception e) { return R.fail("评估失败：" + e.getMessage()); }
    }

    @Operation(summary = "生成交叉持股报告")
    @PostMapping("/cross-holding/report")
    public R<Map<String, Object>> crossHoldingReport(@RequestBody Map<String, Object> params) {
        try {
            String holdingId = (String) params.get("holdingId");
            if (StringUtils.isEmpty(holdingId)) { return R.fail("holdingId不能为空"); }
            TblCrossHolding current = crossHoldingMapper.selectById(holdingId);
            if (current == null) { return R.fail("未找到交叉持股记录"); }

            BigDecimal aRatio = current.getAHoldBRatio() != null ? current.getAHoldBRatio() : BigDecimal.ZERO;
            BigDecimal bRatio = current.getBHoldARatio() != null ? current.getBHoldARatio() : BigDecimal.ZERO;
            BigDecimal inflation = current.getCapitalInflation() != null ? current.getCapitalInflation() : BigDecimal.ZERO;
            String riskLevel = current.getRiskLevel() != null ? current.getRiskLevel() : "MEDIUM";
            boolean isCircular = "Y".equals(current.getIsCircular());

            // 基本信息
            Map<String, Object> basicInfo = new HashMap<>();
            basicInfo.put("companyAName", current.getCompanyAName());
            basicInfo.put("companyBName", current.getCompanyBName());
            basicInfo.put("aHoldBRatio", aRatio);
            basicInfo.put("bHoldARatio", bRatio);

            // 风险评估
            Map<String, Object> riskAssessment = new HashMap<>();
            riskAssessment.put("riskLevel", riskLevel);
            riskAssessment.put("capitalInflation", inflation);
            riskAssessment.put("isCircular", current.getIsCircular() != null ? current.getIsCircular() : "N");
            riskAssessment.put("chainLength", current.getChainLength() != null ? current.getChainLength() : 2);

            // 分析
            Map<String, Object> analysis = new HashMap<>();
            analysis.put("crossType", current.getCrossType() != null ? current.getCrossType() : "DIRECT");
            BigDecimal intensity = aRatio.add(bRatio).compareTo(BigDecimal.ZERO) > 0
                    ? aRatio.multiply(bRatio).divide(aRatio.add(bRatio), 2, RoundingMode.HALF_UP).divide(new BigDecimal("50"), 2, RoundingMode.HALF_UP)
                    : BigDecimal.ZERO;
            analysis.put("intensity", intensity.min(BigDecimal.ONE));
            analysis.put("stability", "HIGH".equals(riskLevel) ? "UNSTABLE" : "MEDIUM".equals(riskLevel) ? "MODERATE" : "STABLE");

            // 建议
            List<String> suggestions = new ArrayList<>();
            if ("HIGH".equals(riskLevel)) {
                suggestions.add("限期解除交叉持股");
            }
            if (aRatio.compareTo(new BigDecimal("20")) > 0 || bRatio.compareTo(new BigDecimal("20")) > 0) {
                suggestions.add("降低互持比例至监管红线以下");
            }
            if (isCircular) {
                suggestions.add("打破环形持股链条");
            }
            suggestions.add("加强信息披露");
            suggestions.add("建立动态监控机制");

            // 结论
            String conclusion;
            if ("HIGH".equals(riskLevel)) {
                conclusion = "该交叉持股关系风险较高，资本虚增" + inflation.setScale(0, RoundingMode.HALF_UP) + "万元，建议限期整改";
            } else if ("MEDIUM".equals(riskLevel)) {
                conclusion = "该交叉持股关系存在一定风险，建议持续关注并适时调整持股比例";
            } else {
                conclusion = "该交叉持股关系风险可控，建议保持常规监控";
            }

            Map<String, Object> result = new HashMap<>();
            result.put("reportTitle", "交叉持股分析报告");
            result.put("generatedTime", LocalDateTime.now().toString().replace("T", " ").substring(0, 19));
            result.put("basicInfo", basicInfo);
            result.put("riskAssessment", riskAssessment);
            result.put("analysis", analysis);
            result.put("suggestions", suggestions);
            result.put("conclusion", conclusion);
            return R.success(result);
        } catch (Exception e) { return R.fail("生成失败：" + e.getMessage()); }
    }

    @Operation(summary = "批量检测交叉持股")
    @PostMapping("/cross-holding/batch/detect")
    public R<Boolean> batchDetectCrossHolding(@RequestBody Map<String, Object> params) { return R.success(true); }

    @Operation(summary = "批量删除交叉持股")
    @PostMapping("/cross-holding/batch/delete")
    public R<Boolean> batchDeleteCrossHolding(@RequestBody Map<String, Object> params) { return R.success(true); }

    @Operation(summary = "导出交叉持股数据")
    @PostMapping("/cross-holding/export")
    public R<List<Map<String, Object>>> exportCrossHolding(@RequestBody Map<String, Object> params) {
        try { return R.success(new ArrayList<>()); }
        catch (Exception e) { return R.fail("导出失败：" + e.getMessage()); }
    }

    @Operation(summary = "导入交叉持股数据")
    @PostMapping("/cross-holding/import")
    public R<Boolean> importCrossHolding() { return R.success(true); }

    @Operation(summary = "监控交叉持股变化")
    @PostMapping("/cross-holding/monitor")
    public R<Map<String, Object>> crossHoldingMonitor(@RequestBody Map<String, Object> params) {
        try { return R.success(new HashMap<>()); }
        catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 资产配置 ====================

    @Operation(summary = "资产配置统计")
    @PostMapping("/allocation/statistics")
    public R<Map<String, Object>> allocationStatistics(@RequestBody Map<String, Object> params) {
        try {
            List<TblAssetAllocation> all = assetAllocationMapper.selectList(null);
            Map<String, Object> result = new HashMap<>();
            // 资产总额(万元)
            BigDecimal totalAssets = all.stream()
                .map(a -> a.getAssetAmount() != null ? a.getAssetAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            result.put("totalAssets", totalAssets);
            // 资产类别数
            long assetCategories = all.stream()
                .map(TblAssetAllocation::getAssetType)
                .filter(Objects::nonNull).distinct().count();
            result.put("assetCategories", assetCategories);
            // 需调整资产数
            long riskAssets = all.stream()
                .filter(a -> "NEED_ADJUST".equals(a.getAllocationStatus())).count();
            result.put("riskAssets", riskAssets);
            // 配置效率均值(收益率)
            BigDecimal avgEfficiency = all.isEmpty() ? BigDecimal.ZERO :
                all.stream()
                    .map(a -> a.getYieldRate() != null ? a.getYieldRate() : BigDecimal.ZERO)
                    .reduce(BigDecimal.ZERO, BigDecimal::add)
                    .divide(BigDecimal.valueOf(all.size()), 2, RoundingMode.HALF_UP);
            result.put("allocationScore", avgEfficiency);
            return R.success(result);
        } catch (Exception e) {
            log.error("资产配置统计异常", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "资产配置列表")
    @PostMapping("/allocation/list")
    public R<PageResult<Map<String, Object>>> allocationList(@RequestBody Map<String, Object> params) {
        try {
            int pn = getPageNumber(params);
            int ps = getPageSize(params);
            LambdaQueryWrapper<TblAssetAllocation> wrapper = new LambdaQueryWrapper<>();
            // 企业名称
            String companyName = params.get("enterpriseName") != null ? params.get("enterpriseName").toString() :
                (params.get("companyName") != null ? params.get("companyName").toString() : null);
            if (StringUtils.isNotEmpty(companyName)) {
                wrapper.like(TblAssetAllocation::getCompanyName, companyName);
            }
            // 资产类型
            String assetType = params.get("assetType") != null ? params.get("assetType").toString() : null;
            if (StringUtils.isNotEmpty(assetType)) {
                wrapper.eq(TblAssetAllocation::getAssetType, assetType);
            }
            // 风险等级
            String riskLevel = params.get("riskLevel") != null ? params.get("riskLevel").toString() : null;
            if (StringUtils.isNotEmpty(riskLevel)) {
                wrapper.eq(TblAssetAllocation::getRiskLevel, riskLevel);
            }
            // 所属地区
            String region = params.get("region") != null ? params.get("region").toString() : null;
            if (StringUtils.isNotEmpty(region)) {
                wrapper.eq(TblAssetAllocation::getRegion, region);
            }
            // 所属行业
            String industry = params.get("industry") != null ? params.get("industry").toString() : null;
            if (StringUtils.isNotEmpty(industry)) {
                wrapper.eq(TblAssetAllocation::getIndustry, industry);
            }
            // 资产规模范围
            if (params.get("minAssetValue") != null && StringUtils.isNotEmpty(params.get("minAssetValue").toString())) {
                wrapper.ge(TblAssetAllocation::getAssetAmount, new BigDecimal(params.get("minAssetValue").toString()));
            }
            if (params.get("maxAssetValue") != null && StringUtils.isNotEmpty(params.get("maxAssetValue").toString())) {
                wrapper.le(TblAssetAllocation::getAssetAmount, new BigDecimal(params.get("maxAssetValue").toString()));
            }
            wrapper.orderByDesc(TblAssetAllocation::getUpdateTime);
            List<TblAssetAllocation> all = assetAllocationMapper.selectList(wrapper);
            List<Map<String, Object>> result = new ArrayList<>();
            for (TblAssetAllocation a : all) {
                Map<String, Object> item = new HashMap<>();
                item.put("allocationId", a.getAllocationId());
                item.put("enterpriseName", a.getCompanyName());
                item.put("companyName", a.getCompanyName());
                item.put("assetType", a.getAssetType());
                item.put("assetValue", a.getAssetAmount());
                item.put("assetAmount", a.getAssetAmount());
                item.put("allocationRatio", a.getAllocationRatio());
                item.put("targetRatio", a.getTargetRatio());
                item.put("deviation", a.getDeviation());
                item.put("region", a.getRegion());
                item.put("industry", a.getIndustry());
                item.put("riskLevel", a.getRiskLevel());
                item.put("allocationStatus", a.getAllocationStatus());
                item.put("allocationEfficiency", a.getYieldRate());
                item.put("yieldRate", a.getYieldRate());
                item.put("updateTime", a.getUpdateTime());
                result.add(item);
            }
            return R.success(buildManualPage(result, pn, ps));
        } catch (Exception e) {
            log.error("资产配置列表查询异常", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "新增资产配置")
    @PostMapping("/allocation/add")
    public R<Boolean> allocationAdd(@RequestBody Map<String, Object> params) {
        try {
            TblAssetAllocation entity = new TblAssetAllocation();
            entity.setCompanyName(params.get("enterpriseName") != null ? params.get("enterpriseName").toString() : null);
            entity.setCompanyId(params.get("companyId") != null ? params.get("companyId").toString() : null);
            entity.setAssetType(params.get("assetType") != null ? params.get("assetType").toString() : null);
            entity.setAssetAmount(params.get("assetAmount") != null ? new BigDecimal(params.get("assetAmount").toString()) : BigDecimal.ZERO);
            entity.setAllocationRatio(params.get("allocationRatio") != null ? new BigDecimal(params.get("allocationRatio").toString()) : null);
            entity.setTargetRatio(params.get("targetRatio") != null ? new BigDecimal(params.get("targetRatio").toString()) : null);
            entity.setYieldRate(params.get("yieldRate") != null ? new BigDecimal(params.get("yieldRate").toString()) : null);
            entity.setRiskLevel(params.get("riskLevel") != null ? params.get("riskLevel").toString() : "LOW");
            entity.setRegion(params.get("region") != null ? params.get("region").toString() : null);
            entity.setIndustry(params.get("industry") != null ? params.get("industry").toString() : null);
            entity.setAllocationStatus(params.get("allocationStatus") != null ? params.get("allocationStatus").toString() : "OPTIMAL");
            entity.setRemark(params.get("remark") != null ? params.get("remark").toString() : null);
            entity.setAdjustSuggestion(params.get("adjustSuggestion") != null ? params.get("adjustSuggestion").toString() : null);
            entity.setCreateTime(LocalDateTime.now());
            entity.setUpdateTime(LocalDateTime.now());
            // 计算偏差
            if (entity.getAllocationRatio() != null && entity.getTargetRatio() != null) {
                entity.setDeviation(entity.getAllocationRatio().subtract(entity.getTargetRatio()));
            }
            assetAllocationMapper.insert(entity);
            return R.success(true);
        } catch (Exception e) {
            log.error("新增资产配置异常", e);
            return R.fail("新增失败：" + e.getMessage());
        }
    }

    @Operation(summary = "编辑资产配置")
    @PostMapping("/allocation/update")
    public R<Boolean> allocationUpdate(@RequestBody Map<String, Object> params) {
        try {
            String allocationId = params.get("allocationId") != null ? params.get("allocationId").toString() : null;
            if (StringUtils.isEmpty(allocationId)) {
                return R.fail("配置ID不能为空");
            }
            TblAssetAllocation entity = assetAllocationMapper.selectById(allocationId);
            if (entity == null) {
                return R.fail("配置记录不存在");
            }
            if (params.get("enterpriseName") != null) entity.setCompanyName(params.get("enterpriseName").toString());
            if (params.get("assetType") != null) entity.setAssetType(params.get("assetType").toString());
            if (params.get("assetAmount") != null) entity.setAssetAmount(new BigDecimal(params.get("assetAmount").toString()));
            if (params.get("allocationRatio") != null) entity.setAllocationRatio(new BigDecimal(params.get("allocationRatio").toString()));
            if (params.get("targetRatio") != null) entity.setTargetRatio(new BigDecimal(params.get("targetRatio").toString()));
            if (params.get("yieldRate") != null) entity.setYieldRate(new BigDecimal(params.get("yieldRate").toString()));
            if (params.get("riskLevel") != null) entity.setRiskLevel(params.get("riskLevel").toString());
            if (params.get("region") != null) entity.setRegion(params.get("region").toString());
            if (params.get("industry") != null) entity.setIndustry(params.get("industry").toString());
            if (params.get("allocationStatus") != null) entity.setAllocationStatus(params.get("allocationStatus").toString());
            if (params.get("remark") != null) entity.setRemark(params.get("remark").toString());
            if (params.get("adjustSuggestion") != null) entity.setAdjustSuggestion(params.get("adjustSuggestion").toString());
            // 重新计算偏差
            if (entity.getAllocationRatio() != null && entity.getTargetRatio() != null) {
                entity.setDeviation(entity.getAllocationRatio().subtract(entity.getTargetRatio()));
            }
            entity.setUpdateTime(LocalDateTime.now());
            assetAllocationMapper.updateById(entity);
            return R.success(true);
        } catch (Exception e) {
            log.error("编辑资产配置异常", e);
            return R.fail("编辑失败：" + e.getMessage());
        }
    }

    @Operation(summary = "删除资产配置")
    @PostMapping("/allocation/delete")
    public R<Boolean> allocationDelete(@RequestBody Map<String, Object> params) {
        try {
            String allocationId = params.get("allocationId") != null ? params.get("allocationId").toString() : null;
            if (StringUtils.isEmpty(allocationId)) {
                return R.fail("配置ID不能为空");
            }
            assetAllocationMapper.deleteById(allocationId);
            return R.success(true);
        } catch (Exception e) {
            log.error("删除资产配置异常", e);
            return R.fail("删除失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取资产配置详情")
    @PostMapping("/allocation/detail")
    public R<Map<String, Object>> allocationDetail(@RequestBody Map<String, Object> params) {
        try {
            String allocationId = params.get("allocationId") != null ? params.get("allocationId").toString() : null;
            if (StringUtils.isEmpty(allocationId)) {
                return R.fail("配置ID不能为空");
            }
            TblAssetAllocation a = assetAllocationMapper.selectById(allocationId);
            if (a == null) {
                return R.fail("配置记录不存在");
            }
            Map<String, Object> item = new HashMap<>();
            item.put("allocationId", a.getAllocationId());
            item.put("enterpriseName", a.getCompanyName());
            item.put("companyName", a.getCompanyName());
            item.put("assetType", a.getAssetType());
            item.put("assetValue", a.getAssetAmount());
            item.put("assetAmount", a.getAssetAmount());
            item.put("allocationRatio", a.getAllocationRatio());
            item.put("targetRatio", a.getTargetRatio());
            item.put("deviation", a.getDeviation());
            item.put("region", a.getRegion());
            item.put("industry", a.getIndustry());
            item.put("riskLevel", a.getRiskLevel());
            item.put("allocationStatus", a.getAllocationStatus());
            item.put("allocationEfficiency", a.getYieldRate());
            item.put("yieldRate", a.getYieldRate());
            item.put("riskScore", a.getRiskScore());
            item.put("adjustSuggestion", a.getAdjustSuggestion());
            item.put("remark", a.getRemark());
            item.put("reportDate", a.getReportDate());
            item.put("createTime", a.getCreateTime());
            item.put("updateTime", a.getUpdateTime());
            return R.success(item);
        } catch (Exception e) {
            log.error("获取资产配置详情异常", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取资产配置图表数据")
    @PostMapping("/allocation/charts")
    public R<Map<String, Object>> allocationCharts(@RequestBody Map<String, Object> params) {
        try {
            List<TblAssetAllocation> all = assetAllocationMapper.selectList(null);
            Map<String, Object> result = new HashMap<>();
            // 资产类型分布
            List<Map<String, Object>> assetTypeData = new ArrayList<>();
            Map<String, BigDecimal> byType = all.stream()
                .filter(a -> a.getAssetType() != null)
                .collect(Collectors.groupingBy(TblAssetAllocation::getAssetType,
                    Collectors.reducing(BigDecimal.ZERO, a -> a.getAssetAmount() != null ? a.getAssetAmount() : BigDecimal.ZERO, BigDecimal::add)));
            Map<String, String> typeNameMap = new HashMap<>();
            typeNameMap.put("FIXED_ASSETS", "固定资产"); typeNameMap.put("CURRENT_ASSETS", "流动资产");
            typeNameMap.put("INTANGIBLE_ASSETS", "无形资产"); typeNameMap.put("INVESTMENT_ASSETS", "投资性资产");
            typeNameMap.put("FINANCIAL_ASSETS", "金融资产");
            for (Map.Entry<String, BigDecimal> entry : byType.entrySet()) {
                Map<String, Object> item = new HashMap<>();
                item.put("name", typeNameMap.getOrDefault(entry.getKey(), entry.getKey()));
                item.put("value", entry.getValue());
                assetTypeData.add(item);
            }
            result.put("assetTypeData", assetTypeData);
            // 按地区分布
            List<Map<String, Object>> assetRegionData = new ArrayList<>();
            Map<String, BigDecimal> byRegion = all.stream()
                .filter(a -> a.getRegion() != null && !a.getRegion().isEmpty())
                .collect(Collectors.groupingBy(TblAssetAllocation::getRegion,
                    Collectors.reducing(BigDecimal.ZERO, a -> a.getAssetAmount() != null ? a.getAssetAmount() : BigDecimal.ZERO, BigDecimal::add)));
            Map<String, String> regionNameMap = new HashMap<>();
            regionNameMap.put("NORTH_CHINA", "华北地区"); regionNameMap.put("EAST_CHINA", "华东地区");
            regionNameMap.put("SOUTH_CHINA", "华南地区"); regionNameMap.put("CENTRAL_CHINA", "华中地区");
            regionNameMap.put("NORTHWEST_CHINA", "西北地区"); regionNameMap.put("SOUTHWEST_CHINA", "西南地区");
            regionNameMap.put("NORTHEAST_CHINA", "东北地区");
            for (Map.Entry<String, BigDecimal> entry : byRegion.entrySet()) {
                Map<String, Object> item = new HashMap<>();
                item.put("name", regionNameMap.getOrDefault(entry.getKey(), entry.getKey()));
                item.put("value", entry.getValue());
                assetRegionData.add(item);
            }
            result.put("assetRegionData", assetRegionData);
            // 按行业分布
            List<Map<String, Object>> assetIndustryData = new ArrayList<>();
            Map<String, BigDecimal> byIndustry = all.stream()
                .filter(a -> a.getIndustry() != null && !a.getIndustry().isEmpty())
                .collect(Collectors.groupingBy(TblAssetAllocation::getIndustry,
                    Collectors.reducing(BigDecimal.ZERO, a -> a.getAssetAmount() != null ? a.getAssetAmount() : BigDecimal.ZERO, BigDecimal::add)));
            Map<String, String> industryNameMap = new HashMap<>();
            industryNameMap.put("MANUFACTURING", "制造业"); industryNameMap.put("FINANCE", "金融业");
            industryNameMap.put("REAL_ESTATE", "房地产业"); industryNameMap.put("CONSTRUCTION", "建筑业");
            industryNameMap.put("TRANSPORTATION", "交通运输业"); industryNameMap.put("IT", "信息技术业");
            industryNameMap.put("ENERGY", "能源业");
            for (Map.Entry<String, BigDecimal> entry : byIndustry.entrySet()) {
                Map<String, Object> item = new HashMap<>();
                item.put("name", industryNameMap.getOrDefault(entry.getKey(), entry.getKey()));
                item.put("value", entry.getValue());
                assetIndustryData.add(item);
            }
            result.put("assetIndustryData", assetIndustryData);
            // 配置效率趋势（按月份聚合）
            List<Map<String, Object>> efficiencyTrendData = new ArrayList<>();
            Map<String, List<TblAssetAllocation>> byMonth = all.stream()
                .filter(a -> a.getUpdateTime() != null && a.getYieldRate() != null)
                .collect(Collectors.groupingBy(a -> a.getUpdateTime().getYear() + "-" + String.format("%02d", a.getUpdateTime().getMonthValue())));
            byMonth.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(entry -> {
                Map<String, Object> item = new HashMap<>();
                item.put("date", entry.getKey());
                double avgEfficiency = entry.getValue().stream()
                    .mapToDouble(a -> a.getYieldRate().doubleValue()).average().orElse(0);
                item.put("efficiency", Math.round(avgEfficiency * 100.0) / 100.0);
                efficiencyTrendData.add(item);
            });
            result.put("efficiencyTrendData", efficiencyTrendData);
            return R.success(result);
        } catch (Exception e) {
            log.error("资产配置图表数据查询异常", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "资产配置结构分析")
    @PostMapping("/allocation/structure-analysis")
    public R<Map<String, Object>> allocationStructure(@RequestBody Map<String, Object> params) {
        try { return R.success(new HashMap<>()); }
        catch (Exception e) { return R.fail("分析失败：" + e.getMessage()); }
    }

    @Operation(summary = "资产配置合理性评估")
    @PostMapping("/allocation/rationality-assessment")
    public R<Map<String, Object>> allocationRationality(@RequestBody Map<String, Object> params) {
        try {
            String allocationId = params.get("allocationId") != null ? params.get("allocationId").toString() : null;
            if (StringUtils.isEmpty(allocationId)) {
                return R.fail("配置ID不能为空");
            }
            TblAssetAllocation entity = assetAllocationMapper.selectById(allocationId);
            if (entity == null) {
                return R.fail("配置记录不存在");
            }
            // 获取同类型所有配置用于对比
            List<TblAssetAllocation> allOfType = assetAllocationMapper.selectList(
                new LambdaQueryWrapper<TblAssetAllocation>().eq(TblAssetAllocation::getAssetType, entity.getAssetType()));
            List<TblAssetAllocation> all = assetAllocationMapper.selectList(null);

            Map<String, Object> result = new HashMap<>();

            // 计算评估指标
            List<Map<String, Object>> indicators = new ArrayList<>();
            double ratio = entity.getAllocationRatio() != null ? entity.getAllocationRatio().doubleValue() : 0;
            double yieldRate = entity.getYieldRate() != null ? entity.getYieldRate().doubleValue() : 0;
            double avgYield = all.stream().filter(a -> a.getYieldRate() != null).mapToDouble(a -> a.getYieldRate().doubleValue()).average().orElse(0);
            double avgRatio = all.stream().filter(a -> a.getAllocationRatio() != null).mapToDouble(a -> a.getAllocationRatio().doubleValue()).average().orElse(0);

            // 指标1: 配置比例合理性
            Map<String, Object> ind1 = new HashMap<>();
            ind1.put("name", "配置比例合理性");
            ind1.put("currentValue", ratio + "%");
            ind1.put("standardValue", String.format("%.1f%%", avgRatio));
            double dev1 = Math.abs(ratio - avgRatio);
            ind1.put("result", dev1 < 10 ? "优秀" : dev1 < 20 ? "良好" : "需改进");
            ind1.put("weight", "25%");
            int score1 = dev1 < 10 ? 90 : dev1 < 20 ? 75 : 55;
            ind1.put("score", score1);
            ind1.put("description", "当前配置比例与整体平均水平的偏差");
            indicators.add(ind1);

            // 指标2: 资产收益率
            Map<String, Object> ind2 = new HashMap<>();
            ind2.put("name", "资产收益率");
            ind2.put("currentValue", yieldRate + "%");
            ind2.put("standardValue", String.format("%.1f%%", avgYield));
            ind2.put("result", yieldRate >= avgYield * 1.2 ? "优秀" : yieldRate >= avgYield * 0.8 ? "良好" : "需改进");
            ind2.put("weight", "30%");
            int score2 = yieldRate >= avgYield * 1.2 ? 92 : yieldRate >= avgYield * 0.8 ? 78 : 55;
            ind2.put("score", score2);
            ind2.put("description", "收益率与同类资产平均水平对比");
            indicators.add(ind2);

            // 指标3: 风险控制
            Map<String, Object> ind3 = new HashMap<>();
            ind3.put("name", "风险控制");
            String riskLevel = entity.getRiskLevel() != null ? entity.getRiskLevel() : "MEDIUM";
            String riskText = "LOW".equals(riskLevel) ? "低风险" : "MEDIUM".equals(riskLevel) ? "中风险" : "高风险";
            ind3.put("currentValue", riskText);
            ind3.put("standardValue", "低风险");
            ind3.put("result", "LOW".equals(riskLevel) ? "优秀" : "MEDIUM".equals(riskLevel) ? "良好" : "需改进");
            ind3.put("weight", "25%");
            int score3 = "LOW".equals(riskLevel) ? 90 : "MEDIUM".equals(riskLevel) ? 70 : 45;
            ind3.put("score", score3);
            ind3.put("description", "风险等级评估");
            indicators.add(ind3);

            // 指标4: 目标偏差
            Map<String, Object> ind4 = new HashMap<>();
            ind4.put("name", "目标偏差控制");
            double deviation = entity.getDeviation() != null ? Math.abs(entity.getDeviation().doubleValue()) : 0;
            ind4.put("currentValue", deviation + "%");
            ind4.put("standardValue", "5%以内");
            ind4.put("result", deviation <= 5 ? "优秀" : deviation <= 15 ? "良好" : "需改进");
            ind4.put("weight", "20%");
            int score4 = deviation <= 5 ? 92 : deviation <= 15 ? 72 : 50;
            ind4.put("score", score4);
            ind4.put("description", "实际配置与目标配置的偏差程度");
            indicators.add(ind4);

            result.put("indicators", indicators);

            // 计算综合评分
            int totalScore = (int) Math.round(score1 * 0.25 + score2 * 0.30 + score3 * 0.25 + score4 * 0.20);
            Map<String, Object> assessResult = new HashMap<>();
            assessResult.put("totalScore", totalScore);
            assessResult.put("level", totalScore >= 90 ? 5 : totalScore >= 80 ? 4 : totalScore >= 70 ? 3 : totalScore >= 60 ? 2 : 1);
            assessResult.put("levelDescription", "该企业资产配置综合评分为" + totalScore + "分，" + (totalScore >= 80 ? "整体表现良好。" : "仍有优化空间。"));

            // 生成建议
            List<Map<String, Object>> suggestions = new ArrayList<>();
            if (score2 < 80) {
                Map<String, Object> s = new HashMap<>();
                s.put("title", "提升资产收益率");
                s.put("content", "当前收益率" + yieldRate + "%低于平均水平" + String.format("%.1f%%", avgYield) + "，建议优化资产结构以提升收益。");
                s.put("priority", "高");
                suggestions.add(s);
            }
            if (score3 < 80) {
                Map<String, Object> s = new HashMap<>();
                s.put("title", "降低风险等级");
                s.put("content", "当前风险等级为" + riskText + "，建议通过分散投资降低整体风险。");
                s.put("priority", "高");
                suggestions.add(s);
            }
            if (score4 < 80) {
                Map<String, Object> s = new HashMap<>();
                s.put("title", "缩小目标偏差");
                s.put("content", "当前配置偏差为" + deviation + "%，建议调整配置比例使其更接近目标值。");
                s.put("priority", "中");
                suggestions.add(s);
            }
            if (suggestions.isEmpty()) {
                Map<String, Object> s = new HashMap<>();
                s.put("title", "保持当前配置");
                s.put("content", "当前资产配置各项指标表现良好，建议持续监控并定期评估。");
                s.put("priority", "低");
                suggestions.add(s);
            }
            assessResult.put("suggestions", suggestions);
            result.put("result", assessResult);

            return R.success(result);
        } catch (Exception e) {
            log.error("资产配置合理性评估异常", e);
            return R.fail("评估失败：" + e.getMessage());
        }
    }

    @Operation(summary = "资产配置优化建议")
    @PostMapping("/allocation/optimization-suggestions")
    public R<Map<String, Object>> allocationSuggestions(@RequestBody Map<String, Object> params) {
        try {
            String allocationId = params.get("allocationId") != null ? params.get("allocationId").toString() : null;
            if (StringUtils.isEmpty(allocationId)) {
                return R.fail("配置ID不能为空");
            }
            TblAssetAllocation entity = assetAllocationMapper.selectById(allocationId);
            if (entity == null) {
                return R.fail("配置记录不存在");
            }
            List<TblAssetAllocation> all = assetAllocationMapper.selectList(null);
            Map<String, Object> result = new HashMap<>();

            // 当前配置
            Map<String, Object> currentConfig = new HashMap<>();
            currentConfig.put("assetType", entity.getAssetType());
            currentConfig.put("assetAmount", entity.getAssetAmount());
            currentConfig.put("allocationRatio", entity.getAllocationRatio());
            currentConfig.put("yieldRate", entity.getYieldRate());
            currentConfig.put("riskLevel", entity.getRiskLevel());
            result.put("currentConfig", currentConfig);

            // 计算同类型平均值作为优化参考
            double avgYield = all.stream().filter(a -> a.getYieldRate() != null).mapToDouble(a -> a.getYieldRate().doubleValue()).average().orElse(0);
            double avgRatio = all.stream().filter(a -> a.getAllocationRatio() != null).mapToDouble(a -> a.getAllocationRatio().doubleValue()).average().orElse(0);
            double currentYield = entity.getYieldRate() != null ? entity.getYieldRate().doubleValue() : 0;
            double currentRatio = entity.getAllocationRatio() != null ? entity.getAllocationRatio().doubleValue() : 0;

            // 优化建议列表
            List<Map<String, Object>> suggestions = new ArrayList<>();
            Map<String, Object> s1 = new HashMap<>();
            s1.put("item", "配置比例");
            s1.put("current", currentRatio + "%");
            s1.put("suggested", String.format("%.1f%%", avgRatio));
            s1.put("expectedReturn", currentRatio > avgRatio ? "降低集中度风险" : "提升配置效率");
            suggestions.add(s1);

            Map<String, Object> s2 = new HashMap<>();
            s2.put("item", "收益率目标");
            s2.put("current", currentYield + "%");
            s2.put("suggested", String.format("%.1f%%", avgYield * 1.1));
            s2.put("expectedReturn", String.format("+%.1f%%", avgYield * 1.1 - currentYield));
            suggestions.add(s2);

            Map<String, Object> s3 = new HashMap<>();
            s3.put("item", "风险等级");
            String currentRisk = entity.getRiskLevel() != null ? entity.getRiskLevel() : "MEDIUM";
            String riskCn = "LOW".equals(currentRisk) ? "低风险" : "MEDIUM".equals(currentRisk) ? "中风险" : "高风险";
            s3.put("current", riskCn);
            s3.put("suggested", "HIGH".equals(currentRisk) ? "中风险" : "低风险");
            s3.put("expectedReturn", "HIGH".equals(currentRisk) ? "显著降低风险" : "LOW".equals(currentRisk) ? "维持当前水平" : "适度降低风险");
            suggestions.add(s3);

            result.put("suggestions", suggestions);

            // 优化效果预测
            Map<String, Object> prediction = new HashMap<>();
            prediction.put("yieldImprovement", String.format("+%.1f%%", Math.max(0, avgYield * 1.1 - currentYield)));
            prediction.put("riskReduction", "HIGH".equals(currentRisk) ? "-30%" : "MEDIUM".equals(currentRisk) ? "-15%" : "0%");
            prediction.put("liquidityImprovement", currentRatio > avgRatio ? "+10%" : "+5%");
            double reasonability = 100 - Math.abs(currentRatio - avgRatio) - (currentYield < avgYield ? 10 : 0);
            prediction.put("reasonabilityScore", String.format("%.0f%%", Math.max(0, Math.min(100, reasonability))));
            result.put("prediction", prediction);

            return R.success(result);
        } catch (Exception e) {
            log.error("资产配置优化建议异常", e);
            return R.fail("查询失败：" + e.getMessage());
        }
    }

    @Operation(summary = "资产配置效率分析")
    @PostMapping("/allocation/efficiency-analysis")
    public R<Map<String, Object>> allocationEfficiency(@RequestBody Map<String, Object> params) {
        try { return R.success(new HashMap<>()); }
        catch (Exception e) { return R.fail("分析失败：" + e.getMessage()); }
    }

    @Operation(summary = "资产配置风险分散度分析")
    @PostMapping("/allocation/risk-dispersion-analysis")
    public R<Map<String, Object>> allocationRiskDispersion(@RequestBody Map<String, Object> params) {
        try { return R.success(new HashMap<>()); }
        catch (Exception e) { return R.fail("分析失败：" + e.getMessage()); }
    }

    @Operation(summary = "资产配置趋势分析")
    @PostMapping("/allocation/trend-analysis")
    public R<Map<String, Object>> allocationTrend(@RequestBody Map<String, Object> params) {
        try {
            String allocationId = params.get("allocationId") != null ? params.get("allocationId").toString() : null;
            LambdaQueryWrapper<TblAssetAllocation> wrapper = new LambdaQueryWrapper<>();
            if (StringUtils.isNotEmpty(allocationId)) {
                TblAssetAllocation target = assetAllocationMapper.selectById(allocationId);
                if (target != null && target.getCompanyName() != null) {
                    wrapper.eq(TblAssetAllocation::getCompanyName, target.getCompanyName());
                }
            }
            wrapper.orderByAsc(TblAssetAllocation::getUpdateTime);
            List<TblAssetAllocation> all = assetAllocationMapper.selectList(wrapper);
            Map<String, Object> result = new HashMap<>();
            // 资产规模趋势
            List<Map<String, Object>> assetTrend = new ArrayList<>();
            for (TblAssetAllocation a : all) {
                if (a.getUpdateTime() != null) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("date", a.getUpdateTime().toLocalDate().toString());
                    item.put("assetAmount", a.getAssetAmount());
                    item.put("yieldRate", a.getYieldRate());
                    item.put("allocationRatio", a.getAllocationRatio());
                    item.put("assetType", a.getAssetType());
                    assetTrend.add(item);
                }
            }
            result.put("trendData", assetTrend);
            // 效率趋势
            List<Map<String, Object>> efficiencyTrend = new ArrayList<>();
            Map<String, List<TblAssetAllocation>> byMonth = all.stream()
                .filter(a -> a.getUpdateTime() != null && a.getYieldRate() != null)
                .collect(Collectors.groupingBy(a -> a.getUpdateTime().getYear() + "-" + String.format("%02d", a.getUpdateTime().getMonthValue())));
            byMonth.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(entry -> {
                Map<String, Object> item = new HashMap<>();
                item.put("date", entry.getKey());
                double avgYield = entry.getValue().stream().mapToDouble(a -> a.getYieldRate().doubleValue()).average().orElse(0);
                item.put("avgYieldRate", Math.round(avgYield * 100.0) / 100.0);
                double totalAmount = entry.getValue().stream().mapToDouble(a -> a.getAssetAmount() != null ? a.getAssetAmount().doubleValue() : 0).sum();
                item.put("totalAmount", totalAmount);
                efficiencyTrend.add(item);
            });
            result.put("efficiencyTrend", efficiencyTrend);
            return R.success(result);
        } catch (Exception e) {
            log.error("资产配置趋势分析异常", e);
            return R.fail("分析失败：" + e.getMessage());
        }
    }

    @Operation(summary = "资产配置对比分析")
    @PostMapping("/allocation/comparison-analysis")
    public R<Map<String, Object>> allocationComparison(@RequestBody Map<String, Object> params) {
        try {
            String allocationId = params.get("allocationId") != null ? params.get("allocationId").toString() : null;
            List<TblAssetAllocation> all = assetAllocationMapper.selectList(null);
            Map<String, Object> result = new HashMap<>();
            // 当前配置信息
            TblAssetAllocation current = null;
            if (StringUtils.isNotEmpty(allocationId)) {
                current = assetAllocationMapper.selectById(allocationId);
            }
            if (current != null) {
                Map<String, Object> currentData = new HashMap<>();
                currentData.put("enterpriseName", current.getCompanyName());
                currentData.put("assetType", current.getAssetType());
                currentData.put("assetAmount", current.getAssetAmount());
                currentData.put("allocationRatio", current.getAllocationRatio());
                currentData.put("yieldRate", current.getYieldRate());
                currentData.put("riskLevel", current.getRiskLevel());
                result.put("currentData", currentData);
            }
            // 同类型对比
            List<Map<String, Object>> typeComparison = new ArrayList<>();
            Map<String, List<TblAssetAllocation>> byType = all.stream()
                .filter(a -> a.getAssetType() != null)
                .collect(Collectors.groupingBy(TblAssetAllocation::getAssetType));
            for (Map.Entry<String, List<TblAssetAllocation>> entry : byType.entrySet()) {
                Map<String, Object> item = new HashMap<>();
                item.put("assetType", entry.getKey());
                item.put("count", entry.getValue().size());
                double avgAmount = entry.getValue().stream().mapToDouble(a -> a.getAssetAmount() != null ? a.getAssetAmount().doubleValue() : 0).average().orElse(0);
                item.put("avgAmount", Math.round(avgAmount * 100.0) / 100.0);
                double avgYield = entry.getValue().stream().filter(a -> a.getYieldRate() != null).mapToDouble(a -> a.getYieldRate().doubleValue()).average().orElse(0);
                item.put("avgYieldRate", Math.round(avgYield * 100.0) / 100.0);
                typeComparison.add(item);
            }
            result.put("typeComparison", typeComparison);
            // 行业对比
            List<Map<String, Object>> industryComparison = new ArrayList<>();
            Map<String, List<TblAssetAllocation>> byIndustry = all.stream()
                .filter(a -> a.getIndustry() != null && !a.getIndustry().isEmpty())
                .collect(Collectors.groupingBy(TblAssetAllocation::getIndustry));
            for (Map.Entry<String, List<TblAssetAllocation>> entry : byIndustry.entrySet()) {
                Map<String, Object> item = new HashMap<>();
                item.put("industry", entry.getKey());
                item.put("count", entry.getValue().size());
                double totalAmount = entry.getValue().stream().mapToDouble(a -> a.getAssetAmount() != null ? a.getAssetAmount().doubleValue() : 0).sum();
                item.put("totalAmount", totalAmount);
                industryComparison.add(item);
            }
            result.put("industryComparison", industryComparison);
            return R.success(result);
        } catch (Exception e) {
            log.error("资产配置对比分析异常", e);
            return R.fail("对比失败：" + e.getMessage());
        }
    }

    @Operation(summary = "资产配置预警设置")
    @PostMapping("/allocation/alerts")
    public R<Boolean> allocationAlerts(@RequestBody Map<String, Object> params) { return R.success(true); }

    @Operation(summary = "获取资产配置预警信息")
    @PostMapping("/allocation/alerts/list")
    public R<List<Map<String, Object>>> allocationAlertsList(@RequestBody Map<String, Object> params) {
        try { return R.success(new ArrayList<>()); }
        catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "资产配置智能推荐")
    @PostMapping("/allocation/recommendations")
    public R<List<Map<String, Object>>> allocationRecommendations(@RequestBody Map<String, Object> params) {
        try { return R.success(new ArrayList<>()); }
        catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "批量优化资产配置")
    @PostMapping("/allocation/batch-optimize")
    public R<Map<String, Object>> batchOptimizeAllocation(@RequestBody Map<String, Object> params) {
        try {
            List<String> ids = (List<String>) params.get("allocationIds");
            if (ids == null || ids.isEmpty()) {
                return R.fail("请选择要优化的配置");
            }
            int optimizedCount = 0;
            List<Map<String, Object>> suggestions = new ArrayList<>();
            for (String id : ids) {
                TblAssetAllocation entity = assetAllocationMapper.selectById(id);
                if (entity != null) {
                    // 根据当前配置生成优化建议
                    Map<String, Object> suggestion = new HashMap<>();
                    suggestion.put("allocationId", id);
                    suggestion.put("enterpriseName", entity.getCompanyName());
                    suggestion.put("currentRatio", entity.getAllocationRatio());
                    suggestion.put("suggestedRatio", entity.getTargetRatio());
                    suggestion.put("currentYield", entity.getYieldRate());
                    // 更新状态为已优化
                    entity.setAllocationStatus("OPTIMAL");
                    entity.setUpdateTime(LocalDateTime.now());
                    assetAllocationMapper.updateById(entity);
                    optimizedCount++;
                    suggestions.add(suggestion);
                }
            }
            Map<String, Object> result = new HashMap<>();
            result.put("optimizedCount", optimizedCount);
            result.put("suggestions", suggestions);
            return R.success(result);
        } catch (Exception e) {
            log.error("批量优化资产配置异常", e);
            return R.fail("优化失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量评估资产配置")
    @PostMapping("/allocation/batch-assess")
    public R<Map<String, Object>> batchAssessAllocation(@RequestBody Map<String, Object> params) {
        try {
            List<String> ids = (List<String>) params.get("allocationIds");
            if (ids == null || ids.isEmpty()) {
                return R.fail("请选择要评估的配置");
            }
            List<Map<String, Object>> assessResults = new ArrayList<>();
            for (String id : ids) {
                TblAssetAllocation entity = assetAllocationMapper.selectById(id);
                if (entity != null) {
                    Map<String, Object> assessItem = new HashMap<>();
                    assessItem.put("allocationId", id);
                    assessItem.put("enterpriseName", entity.getCompanyName());
                    assessItem.put("assetType", entity.getAssetType());
                    // 计算评估得分
                    double score = 60;
                    if (entity.getYieldRate() != null) score += entity.getYieldRate().doubleValue() * 2;
                    if (entity.getAllocationRatio() != null && entity.getTargetRatio() != null) {
                        double deviation = Math.abs(entity.getAllocationRatio().subtract(entity.getTargetRatio()).doubleValue());
                        score -= deviation * 2;
                    }
                    score = Math.max(0, Math.min(100, score));
                    assessItem.put("score", Math.round(score * 10.0) / 10.0);
                    assessItem.put("level", score >= 80 ? "优秀" : score >= 60 ? "良好" : "需改进");
                    assessResults.add(assessItem);
                }
            }
            Map<String, Object> result = new HashMap<>();
            result.put("assessCount", assessResults.size());
            result.put("assessResults", assessResults);
            double avgScore = assessResults.stream().mapToDouble(r -> ((Number) r.get("score")).doubleValue()).average().orElse(0);
            result.put("averageScore", Math.round(avgScore * 10.0) / 10.0);
            return R.success(result);
        } catch (Exception e) {
            log.error("批量评估资产配置异常", e);
            return R.fail("评估失败：" + e.getMessage());
        }
    }

    @Operation(summary = "批量删除资产配置")
    @PostMapping("/allocation/batch-delete")
    public R<Boolean> batchDeleteAllocation(@RequestBody Map<String, Object> params) {
        try {
            List<String> ids = (List<String>) params.get("allocationIds");
            if (ids == null || ids.isEmpty()) {
                return R.fail("请选择要删除的配置");
            }
            for (String id : ids) {
                assetAllocationMapper.deleteById(id);
            }
            return R.success(true);
        } catch (Exception e) {
            log.error("批量删除资产配置异常", e);
            return R.fail("删除失败：" + e.getMessage());
        }
    }

    @Operation(summary = "导出资产配置数据")
    @PostMapping("/allocation/export")
    public void exportAllocation(@RequestBody Map<String, Object> params, javax.servlet.http.HttpServletResponse response) {
        try {
            LambdaQueryWrapper<TblAssetAllocation> wrapper = new LambdaQueryWrapper<>();
            wrapper.orderByDesc(TblAssetAllocation::getUpdateTime);
            List<TblAssetAllocation> all = assetAllocationMapper.selectList(wrapper);
            // 构建导出数据
            List<List<Object>> rows = new ArrayList<>();
            // 表头
            List<Object> header = new ArrayList<>();
            header.add("企业名称"); header.add("资产类型"); header.add("资产规模(万元)");
            header.add("配置比例(%)"); header.add("所属地区"); header.add("所属行业");
            header.add("风险等级"); header.add("配置效率(%)"); header.add("更新时间");
            rows.add(header);
            Map<String, String> typeMap = new HashMap<>();
            typeMap.put("FIXED_ASSETS", "固定资产"); typeMap.put("CURRENT_ASSETS", "流动资产");
            typeMap.put("INTANGIBLE_ASSETS", "无形资产"); typeMap.put("INVESTMENT_ASSETS", "投资性资产");
            typeMap.put("FINANCIAL_ASSETS", "金融资产");
            Map<String, String> regionMap = new HashMap<>();
            regionMap.put("NORTH_CHINA", "华北地区"); regionMap.put("EAST_CHINA", "华东地区");
            regionMap.put("SOUTH_CHINA", "华南地区"); regionMap.put("CENTRAL_CHINA", "华中地区");
            regionMap.put("NORTHWEST_CHINA", "西北地区"); regionMap.put("SOUTHWEST_CHINA", "西南地区");
            regionMap.put("NORTHEAST_CHINA", "东北地区");
            Map<String, String> industryMap = new HashMap<>();
            industryMap.put("MANUFACTURING", "制造业"); industryMap.put("FINANCE", "金融业");
            industryMap.put("REAL_ESTATE", "房地产业"); industryMap.put("CONSTRUCTION", "建筑业");
            industryMap.put("TRANSPORTATION", "交通运输业"); industryMap.put("IT", "信息技术业");
            industryMap.put("ENERGY", "能源业");
            Map<String, String> riskMap = new HashMap<>();
            riskMap.put("LOW", "低风险"); riskMap.put("MEDIUM", "中风险"); riskMap.put("HIGH", "高风险");
            for (TblAssetAllocation a : all) {
                List<Object> row = new ArrayList<>();
                row.add(a.getCompanyName() != null ? a.getCompanyName() : "");
                row.add(typeMap.getOrDefault(a.getAssetType(), a.getAssetType() != null ? a.getAssetType() : ""));
                row.add(a.getAssetAmount() != null ? a.getAssetAmount().toString() : "0");
                row.add(a.getAllocationRatio() != null ? a.getAllocationRatio().toString() : "0");
                row.add(regionMap.getOrDefault(a.getRegion(), a.getRegion() != null ? a.getRegion() : ""));
                row.add(industryMap.getOrDefault(a.getIndustry(), a.getIndustry() != null ? a.getIndustry() : ""));
                row.add(riskMap.getOrDefault(a.getRiskLevel(), a.getRiskLevel() != null ? a.getRiskLevel() : ""));
                row.add(a.getYieldRate() != null ? a.getYieldRate().toString() : "0");
                row.add(a.getUpdateTime() != null ? a.getUpdateTime().toString() : "");
                rows.add(row);
            }
            byte[] xlsxBytes = SimpleXlsxWriter.write("资产配置数据", rows);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=asset_allocation.xlsx");
            response.getOutputStream().write(xlsxBytes);
            response.getOutputStream().flush();
        } catch (Exception e) {
            log.error("导出资产配置数据异常", e);
            try {
                response.setContentType("application/json;charset=UTF-8");
                response.getWriter().write("{\"result\":500,\"msg\":\"导出失败\"}");
            } catch (Exception ex) { log.error("响应写入失败", ex); }
        }
    }

    @Operation(summary = "导入资产配置数据")
    @PostMapping("/allocation/import")
    public R<Boolean> importAllocation() { return R.success(true); }

    @Operation(summary = "生成资产配置分析报告")
    @PostMapping("/allocation/generate-report")
    public R<Map<String, Object>> allocationReport(@RequestBody Map<String, Object> params) {
        try {
            List<TblAssetAllocation> all = assetAllocationMapper.selectList(null);
            Map<String, Object> report = new HashMap<>();
            report.put("reportTitle", "资产配置分析报告");
            report.put("generateTime", LocalDateTime.now().toString());
            report.put("totalRecords", all.size());
            // 总资产规模
            BigDecimal totalAmount = all.stream()
                .map(a -> a.getAssetAmount() != null ? a.getAssetAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            report.put("totalAmount", totalAmount);
            // 资产类型分布
            Map<String, Long> typeDistribution = all.stream()
                .filter(a -> a.getAssetType() != null)
                .collect(Collectors.groupingBy(TblAssetAllocation::getAssetType, Collectors.counting()));
            report.put("typeDistribution", typeDistribution);
            // 风险分布
            Map<String, Long> riskDistribution = all.stream()
                .filter(a -> a.getRiskLevel() != null)
                .collect(Collectors.groupingBy(TblAssetAllocation::getRiskLevel, Collectors.counting()));
            report.put("riskDistribution", riskDistribution);
            // 平均收益率
            double avgYield = all.stream()
                .filter(a -> a.getYieldRate() != null)
                .mapToDouble(a -> a.getYieldRate().doubleValue())
                .average().orElse(0);
            report.put("averageYieldRate", Math.round(avgYield * 100.0) / 100.0);
            // 配置建议
            List<String> suggestions = new ArrayList<>();
            long highRiskCount = all.stream().filter(a -> "HIGH".equals(a.getRiskLevel())).count();
            if (highRiskCount > all.size() * 0.3) {
                suggestions.add("高风险资产占比过高，建议适当降低高风险资产配置比例");
            }
            if (avgYield < 5) {
                suggestions.add("整体收益率偏低，建议优化资产配置结构以提升收益");
            }
            suggestions.add("建议定期进行资产配置再平衡，保持配置比例在目标范围内");
            report.put("suggestions", suggestions);
            return R.success(report);
        } catch (Exception e) {
            log.error("生成资产配置分析报告异常", e);
            return R.fail("生成失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取资产配置监管建议")
    @PostMapping("/allocation/supervision-suggestions")
    public R<List<Map<String, Object>>> allocationSupervision(@RequestBody Map<String, Object> params) {
        try { return R.success(new ArrayList<>()); }
        catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "资产配置压力测试")
    @PostMapping("/allocation/stress-test")
    public R<Map<String, Object>> allocationStressTest(@RequestBody Map<String, Object> params) {
        try { return R.success(new HashMap<>()); }
        catch (Exception e) { return R.fail("测试失败：" + e.getMessage()); }
    }

    @Operation(summary = "资产配置情景分析")
    @PostMapping("/allocation/scenario-analysis")
    public R<Map<String, Object>> allocationScenario(@RequestBody Map<String, Object> params) {
        try { return R.success(new HashMap<>()); }
        catch (Exception e) { return R.fail("分析失败：" + e.getMessage()); }
    }

    @Operation(summary = "资产配置绩效评估")
    @PostMapping("/allocation/performance-evaluation")
    public R<Map<String, Object>> allocationPerformance(@RequestBody Map<String, Object> params) {
        try { return R.success(new HashMap<>()); }
        catch (Exception e) { return R.fail("评估失败：" + e.getMessage()); }
    }

    @Operation(summary = "资产配置基准对比")
    @PostMapping("/allocation/benchmark-comparison")
    public R<Map<String, Object>> allocationBenchmark(@RequestBody Map<String, Object> params) {
        try { return R.success(new HashMap<>()); }
        catch (Exception e) { return R.fail("对比失败：" + e.getMessage()); }
    }

    @Operation(summary = "资产配置历史版本")
    @PostMapping("/allocation/version-history")
    public R<List<Map<String, Object>>> allocationVersionHistory(@RequestBody Map<String, Object> params) {
        try { return R.success(new ArrayList<>()); }
        catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "恢复资产配置历史版本")
    @PostMapping("/allocation/restore-version")
    public R<Boolean> allocationRestoreVersion(@RequestBody Map<String, Object> params) { return R.success(true); }

    // ==================== 资产质量 ====================

    @Operation(summary = "资产质量统计")
    @PostMapping("/quality/statistics")
    public R<Map<String, Object>> qualityStatistics(@RequestBody Map<String, Object> params) {
        try {
            Map<String, Object> result = new HashMap<>();
            List<TblAssetQuality> all = assetQualityMapper.selectList(null);
            result.put("totalAssets", all.size());
            long highQuality = all.stream().filter(i -> "EXCELLENT".equals(i.getQualityLevel()) || "GOOD".equals(i.getQualityLevel())).count();
            result.put("highQualityAssets", highQuality);
            long riskAssets = all.stream().filter(i -> "RISK".equals(i.getQualityLevel()) || "POOR".equals(i.getQualityLevel())).count();
            result.put("riskAssets", riskAssets);
            double avgScore = all.stream().filter(i -> i.getQualityScore() != null).mapToDouble(i -> i.getQualityScore().doubleValue()).average().orElse(0);
            result.put("averageScore", Math.round(avgScore * 100.0) / 100.0);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "资产质量列表")
    @PostMapping("/quality/list")
    public R<PageResult<TblAssetQuality>> qualityList(@RequestBody Map<String, Object> params) {
        try {
            int pn = getPageNumber(params); int ps = getPageSize(params);
            LambdaQueryWrapper<TblAssetQuality> w = new LambdaQueryWrapper<>();
            String enterpriseName = params.get("enterpriseName") != null ? params.get("enterpriseName").toString().trim() : "";
            if (!enterpriseName.isEmpty()) w.like(TblAssetQuality::getEnterpriseName, enterpriseName);
            String assetName = params.get("assetName") != null ? params.get("assetName").toString().trim() : "";
            if (!assetName.isEmpty()) w.like(TblAssetQuality::getAssetName, assetName);
            String assetType = params.get("assetType") != null ? params.get("assetType").toString().trim() : "";
            if (!assetType.isEmpty()) w.eq(TblAssetQuality::getAssetCategory, assetType);
            String qualityLevel = params.get("qualityLevel") != null ? params.get("qualityLevel").toString().trim() : "";
            if (!qualityLevel.isEmpty()) w.eq(TblAssetQuality::getQualityLevel, qualityLevel);
            String assessmentStatus = params.get("assessmentStatus") != null ? params.get("assessmentStatus").toString().trim() : "";
            if (!assessmentStatus.isEmpty()) w.eq(TblAssetQuality::getAssessmentStatus, assessmentStatus);
            String startDate = params.get("assessmentStartDate") != null ? params.get("assessmentStartDate").toString().trim() : "";
            if (!startDate.isEmpty()) w.ge(TblAssetQuality::getAssessmentDate, LocalDateTime.parse(startDate + "T00:00:00"));
            String endDate = params.get("assessmentEndDate") != null ? params.get("assessmentEndDate").toString().trim() : "";
            if (!endDate.isEmpty()) w.le(TblAssetQuality::getAssessmentDate, LocalDateTime.parse(endDate + "T23:59:59"));
            w.orderByDesc(TblAssetQuality::getQualityScore);
            Page<TblAssetQuality> page = assetQualityMapper.selectPage(new Page<>(pn, ps), w);
            return R.success(PageResult.of(page));
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "新增资产质量评估")
    @PostMapping("/quality/add")
    public R<Boolean> qualityAdd(@RequestBody Map<String, Object> params) {
        try {
            TblAssetQuality aq = new TblAssetQuality();
            aq.setEnterpriseName(params.get("enterpriseName") != null ? params.get("enterpriseName").toString() : null);
            aq.setAssetName(params.get("assetName") != null ? params.get("assetName").toString() : null);
            aq.setAssetCategory(params.get("assetCategory") != null ? params.get("assetCategory").toString() : "FIXED_ASSETS");
            aq.setAssetValue(params.get("assetValue") != null ? new BigDecimal(params.get("assetValue").toString()) : BigDecimal.ZERO);
            aq.setQualityLevel(params.get("qualityLevel") != null ? params.get("qualityLevel").toString() : "AVERAGE");
            aq.setQualityScore(params.get("qualityScore") != null ? new BigDecimal(params.get("qualityScore").toString()) : BigDecimal.ZERO);
            aq.setRiskLevel(params.get("riskLevel") != null ? params.get("riskLevel").toString() : "LOW");
            aq.setAssessmentStatus(params.get("assessmentStatus") != null ? params.get("assessmentStatus").toString() : "PENDING");
            aq.setReturnRate(params.get("returnRate") != null ? new BigDecimal(params.get("returnRate").toString()) : null);
            aq.setQualityDescription(params.get("qualityDescription") != null ? params.get("qualityDescription").toString() : null);
            aq.setCreateTime(LocalDateTime.now());
            aq.setUpdateTime(LocalDateTime.now());
            aq.setAssessmentDate(LocalDateTime.now());
            assetQualityMapper.insert(aq);
            return R.success(true);
        } catch (Exception e) {
            log.error("新增资产质量评估失败", e);
            return R.fail("新增失败：" + e.getMessage());
        }
    }

    @Operation(summary = "编辑资产质量评估")
    @PostMapping("/quality/update")
    public R<Boolean> qualityUpdate(@RequestBody Map<String, Object> params) {
        try {
            String id = params.get("assetQualityId") != null ? params.get("assetQualityId").toString() : null;
            if (id == null) return R.fail("ID不能为空");
            TblAssetQuality aq = assetQualityMapper.selectById(id);
            if (aq == null) return R.fail("未找到记录");
            if (params.get("enterpriseName") != null) aq.setEnterpriseName(params.get("enterpriseName").toString());
            if (params.get("assetName") != null) aq.setAssetName(params.get("assetName").toString());
            if (params.get("assetCategory") != null) aq.setAssetCategory(params.get("assetCategory").toString());
            if (params.get("assetValue") != null) aq.setAssetValue(new BigDecimal(params.get("assetValue").toString()));
            if (params.get("qualityLevel") != null) aq.setQualityLevel(params.get("qualityLevel").toString());
            if (params.get("qualityScore") != null) aq.setQualityScore(new BigDecimal(params.get("qualityScore").toString()));
            if (params.get("riskLevel") != null) aq.setRiskLevel(params.get("riskLevel").toString());
            if (params.get("assessmentStatus") != null) aq.setAssessmentStatus(params.get("assessmentStatus").toString());
            if (params.get("returnRate") != null) aq.setReturnRate(new BigDecimal(params.get("returnRate").toString()));
            if (params.get("qualityDescription") != null) aq.setQualityDescription(params.get("qualityDescription").toString());
            aq.setUpdateTime(LocalDateTime.now());
            assetQualityMapper.updateById(aq);
            return R.success(true);
        } catch (Exception e) {
            log.error("编辑资产质量评估失败", e);
            return R.fail("编辑失败：" + e.getMessage());
        }
    }

    @Operation(summary = "删除资产质量评估")
    @PostMapping("/quality/delete")
    public R<Boolean> qualityDelete(@RequestBody Map<String, Object> params) {
        try {
            String id = params.get("assetQualityId") != null ? params.get("assetQualityId").toString() : null;
            if (id == null) return R.fail("ID不能为空");
            assetQualityMapper.deleteById(id);
            return R.success(true);
        } catch (Exception e) {
            log.error("删除资产质量评估失败", e);
            return R.fail("删除失败：" + e.getMessage());
        }
    }

    @Operation(summary = "获取资产质量详情")
    @PostMapping("/quality/detail")
    public R<Map<String, Object>> qualityDetail(@RequestBody Map<String, Object> params) {
        try {
            String id = params.get("indicatorId") != null ? params.get("indicatorId").toString() :
                (params.get("assetQualityId") != null ? params.get("assetQualityId").toString() : null);
            if (id == null) return R.fail("ID不能为空");
            TblAssetQuality aq = assetQualityMapper.selectById(id);
            if (aq == null) return R.fail("未找到记录");
            Map<String, Object> result = new HashMap<>();
            result.put("assetQualityId", aq.getAssetQualityId());
            result.put("enterpriseName", aq.getEnterpriseName());
            result.put("assetName", aq.getAssetName());
            result.put("assetCategory", aq.getAssetCategory());
            result.put("assetValue", aq.getAssetValue());
            result.put("qualityLevel", aq.getQualityLevel());
            result.put("comprehensiveScore", aq.getQualityScore());
            result.put("qualityScore", aq.getQualityScore());
            result.put("riskLevel", aq.getRiskLevel());
            result.put("returnRate", aq.getReturnRate());
            result.put("impairmentAmount", aq.getImpairmentAmount());
            result.put("assessmentStatus", aq.getAssessmentStatus());
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "获取资产质量图表数据")
    @PostMapping("/quality/charts")
    public R<Map<String, Object>> qualityCharts(@RequestBody Map<String, Object> params) {
        try {
            Map<String, Object> result = new HashMap<>();
            List<TblAssetQuality> all = assetQualityMapper.selectList(null);
            // 质量分布
            long excellent = all.stream().filter(i -> "EXCELLENT".equals(i.getQualityLevel())).count();
            long good = all.stream().filter(i -> "GOOD".equals(i.getQualityLevel())).count();
            long average = all.stream().filter(i -> "AVERAGE".equals(i.getQualityLevel())).count();
            long poor = all.stream().filter(i -> "POOR".equals(i.getQualityLevel())).count();
            long risk = all.stream().filter(i -> "RISK".equals(i.getQualityLevel())).count();
            List<Map<String, Object>> distribution = new ArrayList<>();
            distribution.add(buildChartItem("优质", excellent)); distribution.add(buildChartItem("良好", good));
            distribution.add(buildChartItem("一般", average)); distribution.add(buildChartItem("较差", poor));
            distribution.add(buildChartItem("风险", risk));
            result.put("qualityDistribution", distribution);
            // 趋势数据 - 按评估日期月份分组
            List<Map<String, Object>> trend = all.stream()
                .filter(i -> i.getAssessmentDate() != null && i.getQualityScore() != null)
                .sorted(Comparator.comparing(TblAssetQuality::getAssessmentDate))
                .map(i -> { Map<String, Object> m = new HashMap<>(); m.put("date", i.getAssessmentDate().toLocalDate().toString()); m.put("score", i.getQualityScore().doubleValue()); return m; })
                .collect(Collectors.toList());
            result.put("qualityTrend", trend);
            // 收益率分析
            List<Map<String, Object>> returnAnalysis = all.stream()
                .filter(i -> i.getEnterpriseName() != null && i.getReturnRate() != null)
                .map(i -> { Map<String, Object> m = new HashMap<>(); m.put("name", i.getEnterpriseName().length() > 6 ? i.getEnterpriseName().substring(0, 6) : i.getEnterpriseName()); m.put("value", i.getReturnRate().doubleValue()); return m; })
                .collect(Collectors.toList());
            result.put("returnAnalysis", returnAnalysis);
            // 减值风险分布
            long lowRisk = all.stream().filter(i -> "LOW".equals(i.getRiskLevel())).count();
            long medRisk = all.stream().filter(i -> "MEDIUM".equals(i.getRiskLevel())).count();
            long highRisk = all.stream().filter(i -> "HIGH".equals(i.getRiskLevel())).count();
            long critRisk = all.stream().filter(i -> "CRITICAL".equals(i.getRiskLevel())).count();
            List<Map<String, Object>> impairmentRisk = new ArrayList<>();
            impairmentRisk.add(buildChartItem("低风险", lowRisk)); impairmentRisk.add(buildChartItem("中风险", medRisk));
            impairmentRisk.add(buildChartItem("高风险", highRisk)); impairmentRisk.add(buildChartItem("极高风险", critRisk));
            result.put("impairmentRisk", impairmentRisk);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    private Map<String, Object> buildChartItem(String name, long value) {
        Map<String, Object> m = new HashMap<>(); m.put("name", name); m.put("value", value); return m;
    }

    @Operation(summary = "资产质量指标监控")
    @PostMapping("/quality/indicator-monitoring")
    public R<Map<String, Object>> qualityIndicatorMonitoring(@RequestBody Map<String, Object> params) {
        try {
            String id = params.get("assetQualityId") != null ? params.get("assetQualityId").toString() : null;
            Map<String, Object> result = new HashMap<>();
            if (id != null) {
                TblAssetQuality aq = assetQualityMapper.selectById(id);
                if (aq != null) {
                    double score = aq.getQualityScore() != null ? aq.getQualityScore().doubleValue() : 0;
                    List<Map<String, Object>> indicators = new ArrayList<>();
                    indicators.add(buildIndicator("资产完整性", Math.min(score + 5, 100), 90, "20%", "资产记录完整度评估"));
                    indicators.add(buildIndicator("资产准确性", score, 85, "25%", "资产信息准确度评估"));
                    indicators.add(buildIndicator("资产时效性", Math.max(score - 3, 0), 80, "15%", "资产信息更新及时性"));
                    indicators.add(buildIndicator("资产可用性", Math.min(score + 2, 100), 85, "20%", "资产可用性评估"));
                    indicators.add(buildIndicator("资产安全性", Math.max(score - 8, 0), 80, "20%", "资产安全性评估"));
                    result.put("indicators", indicators);
                }
            }
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    private Map<String, Object> buildIndicator(String name, double currentValue, double standardValue, String weight, String description) {
        Map<String, Object> m = new HashMap<>();
        m.put("name", name); m.put("currentValue", Math.round(currentValue) + "%");
        m.put("standardValue", Math.round(standardValue) + "%");
        m.put("result", currentValue >= 90 ? "优秀" : currentValue >= 75 ? "良好" : currentValue >= 60 ? "一般" : "较差");
        m.put("weight", weight); m.put("score", Math.round(currentValue)); m.put("description", description);
        return m;
    }

    @Operation(summary = "资产质量趋势分析")
    @PostMapping("/quality/trend-analysis")
    public R<List<Map<String, Object>>> qualityTrend(@RequestBody Map<String, Object> params) {
        try {
            List<TblAssetQuality> all = assetQualityMapper.selectList(new LambdaQueryWrapper<TblAssetQuality>().orderByAsc(TblAssetQuality::getAssessmentDate));
            List<Map<String, Object>> result = all.stream()
                .filter(i -> i.getAssessmentDate() != null && i.getQualityScore() != null)
                .map(i -> { Map<String, Object> m = new HashMap<>(); m.put("year", i.getAssessmentDate().getYear()); m.put("score", i.getQualityScore().doubleValue()); return m; })
                .collect(Collectors.toList());
            return R.success(result);
        } catch (Exception e) { return R.fail("分析失败：" + e.getMessage()); }
    }

    @Operation(summary = "资产质量对比分析")
    @PostMapping("/quality/comparison-analysis")
    public R<Map<String, Object>> qualityComparison(@RequestBody Map<String, Object> params) {
        try {
            String id = params.get("assetQualityId") != null ? params.get("assetQualityId").toString() : null;
            Map<String, Object> result = new HashMap<>();
            List<TblAssetQuality> all = assetQualityMapper.selectList(null);
            double avgScore = all.stream().filter(i -> i.getQualityScore() != null).mapToDouble(i -> i.getQualityScore().doubleValue()).average().orElse(0);
            result.put("industryAvgScore", Math.round(avgScore * 100.0) / 100.0);
            result.put("totalCount", all.size());
            if (id != null) {
                TblAssetQuality aq = assetQualityMapper.selectById(id);
                if (aq != null && aq.getQualityScore() != null) {
                    double myScore = aq.getQualityScore().doubleValue();
                    result.put("currentScore", myScore);
                    result.put("enterpriseName", aq.getEnterpriseName());
                    long rank = all.stream().filter(i -> i.getQualityScore() != null && i.getQualityScore().doubleValue() > myScore).count() + 1;
                    result.put("rank", rank);
                    result.put("percentile", Math.round((1.0 - (double) rank / all.size()) * 10000.0) / 100.0);
                    result.put("deviation", Math.round((myScore - avgScore) * 100.0) / 100.0);
                }
            }
            List<Map<String, Object>> top5 = all.stream().filter(i -> i.getQualityScore() != null)
                .sorted((a, b) -> b.getQualityScore().compareTo(a.getQualityScore())).limit(5)
                .map(i -> { Map<String, Object> m = new HashMap<>(); m.put("name", i.getEnterpriseName()); m.put("score", i.getQualityScore()); return m; })
                .collect(Collectors.toList());
            result.put("top5", top5);
            return R.success(result);
        } catch (Exception e) { return R.fail("对比失败：" + e.getMessage()); }
    }

    @Operation(summary = "资产质量预警设置")
    @PostMapping("/quality/alerts")
    public R<Boolean> qualityAlerts(@RequestBody Map<String, Object> params) { return R.success(true); }

    @Operation(summary = "获取资产质量预警信息")
    @PostMapping("/quality/alerts/list")
    public R<List<Map<String, Object>>> qualityAlertsList(@RequestBody Map<String, Object> params) {
        try {
            LambdaQueryWrapper<TblAssetQuality> w = new LambdaQueryWrapper<>();
            w.in(TblAssetQuality::getRiskLevel, "HIGH", "CRITICAL");
            w.orderByAsc(TblAssetQuality::getQualityScore);
            List<TblAssetQuality> alerts = assetQualityMapper.selectList(w);
            List<Map<String, Object>> result = alerts.stream().map(aq -> {
                Map<String, Object> m = new HashMap<>();
                m.put("alertId", aq.getAssetQualityId());
                m.put("enterpriseName", aq.getEnterpriseName());
                m.put("alertType", "CRITICAL".equals(aq.getRiskLevel()) ? "CRITICAL" : "WARNING");
                m.put("alertLevel", "CRITICAL".equals(aq.getRiskLevel()) ? "高" : "中");
                m.put("alertContent", aq.getEnterpriseName() + " " + aq.getAssetName() + " 质量评分" + aq.getQualityScore() + "，风险等级：" + aq.getRiskLevel());
                m.put("alertTime", aq.getUpdateTime() != null ? aq.getUpdateTime().toString().replace("T", " ") : null);
                // 根据qualityStatus判断处理状态
                String status = aq.getQualityStatus();
                if ("ATTENTION".equals(status)) {
                    m.put("handleStatus", "RESOLVED");
                } else if ("MONITORING".equals(status)) {
                    m.put("handleStatus", "PROCESSING");
                } else {
                    m.put("handleStatus", "PENDING");
                }
                return m;
            }).collect(Collectors.toList());
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "资产质量监控设置")
    @PostMapping("/quality/monitoring")
    public R<Boolean> qualityMonitoring(@RequestBody Map<String, Object> params) {
        try {
            String id = params.get("assetQualityId") != null ? params.get("assetQualityId").toString() : null;
            if (id != null) {
                TblAssetQuality aq = assetQualityMapper.selectById(id);
                if (aq != null) {
                    aq.setQualityStatus("MONITORING");
                    aq.setUpdateTime(LocalDateTime.now());
                    assetQualityMapper.updateById(aq);
                }
            }
            return R.success(true);
        } catch (Exception e) { return R.fail("监控设置失败：" + e.getMessage()); }
    }

    @Operation(summary = "处理预警")
    @PostMapping("/quality/alerts/handle")
    public R<Boolean> handleQualityAlert(@RequestBody Map<String, Object> params) {
        try {
            String id = params.get("alertId") != null ? params.get("alertId").toString() : null;
            if (id == null) return R.fail("预警ID不能为空");
            TblAssetQuality aq = assetQualityMapper.selectById(id);
            if (aq == null) return R.fail("未找到记录");
            // 处理预警：将质量状态改为ATTENTION，表示已关注
            aq.setQualityStatus("ATTENTION");
            aq.setUpdateTime(LocalDateTime.now());
            assetQualityMapper.updateById(aq);
            return R.success(true);
        } catch (Exception e) { return R.fail("处理失败：" + e.getMessage()); }
    }

    @Operation(summary = "查看预警详情")
    @PostMapping("/quality/alerts/detail")
    public R<Map<String, Object>> viewQualityAlert(@RequestBody Map<String, Object> params) {
        try {
            String id = params.get("alertId") != null ? params.get("alertId").toString() : null;
            if (id == null) return R.fail("预警ID不能为空");
            TblAssetQuality aq = assetQualityMapper.selectById(id);
            if (aq == null) return R.fail("未找到记录");
            Map<String, Object> result = new HashMap<>();
            result.put("assetQualityId", aq.getAssetQualityId());
            result.put("enterpriseName", aq.getEnterpriseName());
            result.put("assetName", aq.getAssetName());
            result.put("assetCategory", aq.getAssetCategory());
            result.put("assetValue", aq.getAssetValue());
            result.put("qualityLevel", aq.getQualityLevel());
            result.put("qualityScore", aq.getQualityScore());
            result.put("riskLevel", aq.getRiskLevel());
            result.put("impairmentAmount", aq.getImpairmentAmount());
            result.put("returnRate", aq.getReturnRate());
            result.put("assessmentStatus", aq.getAssessmentStatus());
            result.put("qualityStatus", aq.getQualityStatus());
            result.put("qualityDescription", aq.getQualityDescription());
            result.put("assessmentDate", aq.getAssessmentDate() != null ? aq.getAssessmentDate().toLocalDate().toString() : null);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "获取资产质量监控信息")
    @PostMapping("/quality/monitoring/list")
    public R<Map<String, Object>> qualityMonitoringList(@RequestBody Map<String, Object> params) {
        try {
            List<TblAssetQuality> all = assetQualityMapper.selectList(null);
            Map<String, Object> result = new HashMap<>();
            result.put("totalMonitors", all.size());
            result.put("activeMonitors", all.stream().filter(i -> "COMPLETED".equals(i.getAssessmentStatus())).count());
            result.put("alertCount", all.stream().filter(i -> "HIGH".equals(i.getRiskLevel()) || "CRITICAL".equals(i.getRiskLevel())).count());
            double avgScore = all.stream().filter(i -> i.getQualityScore() != null).mapToDouble(i -> i.getQualityScore().doubleValue()).average().orElse(0);
            result.put("avgQualityScore", Math.round(avgScore * 100.0) / 100.0);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "批量评估资产质量")
    @PostMapping("/quality/batch-assess")
    public R<Boolean> batchAssessQuality(@RequestBody Map<String, Object> params) {
        try {
            List<String> ids = (List<String>) params.get("ids");
            if (ids == null || ids.isEmpty()) return R.fail("请选择要评估的资产");
            for (String id : ids) {
                TblAssetQuality aq = assetQualityMapper.selectById(id);
                if (aq != null) { aq.setAssessmentStatus("COMPLETED"); aq.setUpdateTime(LocalDateTime.now()); assetQualityMapper.updateById(aq); }
            }
            return R.success(true);
        } catch (Exception e) { return R.fail("批量评估失败：" + e.getMessage()); }
    }

    @Operation(summary = "批量监控资产质量")
    @PostMapping("/quality/batch-monitor")
    public R<Boolean> batchMonitorQuality(@RequestBody Map<String, Object> params) {
        try {
            List<String> ids = (List<String>) params.get("ids");
            if (ids == null || ids.isEmpty()) return R.fail("请选择要监控的资产");
            for (String id : ids) {
                TblAssetQuality aq = assetQualityMapper.selectById(id);
                if (aq != null) { aq.setQualityStatus("MONITORING"); aq.setUpdateTime(LocalDateTime.now()); assetQualityMapper.updateById(aq); }
            }
            return R.success(true);
        } catch (Exception e) { return R.fail("批量监控设置失败：" + e.getMessage()); }
    }

    @Operation(summary = "批量删除资产质量评估")
    @PostMapping("/quality/batch-delete")
    public R<Boolean> batchDeleteQuality(@RequestBody Map<String, Object> params) {
        try {
            List<String> ids = (List<String>) params.get("ids");
            if (ids == null || ids.isEmpty()) return R.fail("请选择要删除的资产");
            assetQualityMapper.deleteBatchIds(ids);
            return R.success(true);
        } catch (Exception e) { return R.fail("批量删除失败：" + e.getMessage()); }
    }

    @Operation(summary = "导出资产质量数据")
    @PostMapping("/quality/export")
    public void exportQuality(@RequestBody Map<String, Object> params, javax.servlet.http.HttpServletResponse response) {
        try {
            List<TblAssetQuality> all = assetQualityMapper.selectList(null);
            List<List<Object>> rows = new ArrayList<>();
            // 表头
            List<Object> header = new ArrayList<>();
            header.add("企业名称"); header.add("资产名称"); header.add("资产类别"); header.add("资产价值(万元)");
            header.add("质量等级"); header.add("质量评分"); header.add("风险等级"); header.add("减值金额(万元)");
            header.add("收益率(%)"); header.add("评估状态"); header.add("评估时间");
            rows.add(header);
            // 数据行
            for (TblAssetQuality aq : all) {
                List<Object> row = new ArrayList<>();
                row.add(aq.getEnterpriseName());
                row.add(aq.getAssetName());
                row.add(aq.getAssetCategory());
                row.add(aq.getAssetValue());
                row.add(aq.getQualityLevel());
                row.add(aq.getQualityScore());
                row.add(aq.getRiskLevel());
                row.add(aq.getImpairmentAmount());
                row.add(aq.getReturnRate());
                row.add(aq.getAssessmentStatus());
                row.add(aq.getAssessmentDate() != null ? aq.getAssessmentDate().toLocalDate().toString() : "");
                rows.add(row);
            }
            byte[] xlsxBytes = SimpleXlsxWriter.write("资产质量评估", rows);
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=" + java.net.URLEncoder.encode("资产质量评估数据.xlsx", "UTF-8"));
            response.setHeader("Access-Control-Expose-Headers", "Content-Disposition");
            response.getOutputStream().write(xlsxBytes);
            response.getOutputStream().flush();
        } catch (Exception e) {
            log.error("导出资产质量数据失败", e);
            try { response.sendError(500, "导出失败：" + e.getMessage()); } catch (Exception ignored) {}
        }
    }

    @Operation(summary = "导入资产质量数据")
    @PostMapping("/quality/import")
    public R<Boolean> importQuality() { return R.success(true); }

    @Operation(summary = "生成资产质量分析报告")
    @PostMapping("/quality/generate-report")
    public R<Map<String, Object>> qualityReport(@RequestBody Map<String, Object> params) {
        try {
            List<TblAssetQuality> all = assetQualityMapper.selectList(null);
            Map<String, Object> report = new HashMap<>();
            report.put("reportTitle", "资产质量分析报告");
            report.put("generateTime", LocalDateTime.now().toString());
            report.put("totalAssets", all.size());
            double avgScore = all.stream().filter(i -> i.getQualityScore() != null).mapToDouble(i -> i.getQualityScore().doubleValue()).average().orElse(0);
            report.put("averageScore", Math.round(avgScore * 100.0) / 100.0);
            long excellent = all.stream().filter(i -> "EXCELLENT".equals(i.getQualityLevel())).count();
            long good = all.stream().filter(i -> "GOOD".equals(i.getQualityLevel())).count();
            long risk = all.stream().filter(i -> "RISK".equals(i.getQualityLevel()) || "POOR".equals(i.getQualityLevel())).count();
            report.put("excellentCount", excellent); report.put("goodCount", good); report.put("riskCount", risk);
            List<String> suggestions = new ArrayList<>();
            if (risk > 0) suggestions.add("存在" + risk + "项风险资产，建议加强监控和处置");
            if (avgScore < 70) suggestions.add("整体资产质量评分偏低，建议优化资产结构");
            report.put("suggestions", suggestions);
            return R.success(report);
        } catch (Exception e) { return R.fail("生成失败：" + e.getMessage()); }
    }

    @Operation(summary = "资产收益率分析")
    @PostMapping("/quality/return-rate-analysis")
    public R<Map<String, Object>> returnRateAnalysis(@RequestBody Map<String, Object> params) {
        try {
            List<TblAssetQuality> all = assetQualityMapper.selectList(null);
            Map<String, Object> result = new HashMap<>();
            result.put("avgROA", all.stream().filter(i -> i.getReturnRate() != null).mapToDouble(i -> i.getReturnRate().doubleValue()).average().orElse(0));
            return R.success(result);
        } catch (Exception e) { return R.fail("分析失败：" + e.getMessage()); }
    }

    @Operation(summary = "资产周转率分析")
    @PostMapping("/quality/turnover-rate-analysis")
    public R<Map<String, Object>> turnoverRateAnalysis(@RequestBody Map<String, Object> params) {
        try { return R.success(new HashMap<>()); }
        catch (Exception e) { return R.fail("分析失败：" + e.getMessage()); }
    }

    @Operation(summary = "资产减值风险识别")
    @PostMapping("/quality/impairment-risk-identification")
    public R<List<Map<String, Object>>> impairmentRisk(@RequestBody Map<String, Object> params) {
        try { return R.success(new ArrayList<>()); }
        catch (Exception e) { return R.fail("识别失败：" + e.getMessage()); }
    }

    @Operation(summary = "潜在减值资产识别")
    @PostMapping("/quality/potential-impairment-identification")
    public R<List<Map<String, Object>>> potentialImpairment(@RequestBody Map<String, Object> params) {
        try { return R.success(new ArrayList<>()); }
        catch (Exception e) { return R.fail("识别失败：" + e.getMessage()); }
    }

    @Operation(summary = "资产质量变化趋势预测")
    @PostMapping("/quality/change-trend-prediction")
    public R<Map<String, Object>> qualityTrendPrediction(@RequestBody Map<String, Object> params) {
        try { return R.success(new HashMap<>()); }
        catch (Exception e) { return R.fail("预测失败：" + e.getMessage()); }
    }

    @Operation(summary = "资产质量评估模型")
    @PostMapping("/quality/assessment-model")
    public R<Map<String, Object>> assessmentModel(@RequestBody Map<String, Object> params) {
        try { return R.success(new HashMap<>()); }
        catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "资产质量评分计算")
    @PostMapping("/quality/calculate-score")
    public R<Map<String, Object>> calculateScore(@RequestBody Map<String, Object> params) {
        try { return R.success(new HashMap<>()); }
        catch (Exception e) { return R.fail("计算失败：" + e.getMessage()); }
    }

    @Operation(summary = "资产质量等级评定")
    @PostMapping("/quality/evaluate-level")
    public R<Map<String, Object>> evaluateLevel(@RequestBody Map<String, Object> params) {
        try { return R.success(new HashMap<>()); }
        catch (Exception e) { return R.fail("评定失败：" + e.getMessage()); }
    }

    @Operation(summary = "资产质量基准对比")
    @PostMapping("/quality/benchmark-comparison")
    public R<Map<String, Object>> qualityBenchmark(@RequestBody Map<String, Object> params) {
        try { return R.success(new HashMap<>()); }
        catch (Exception e) { return R.fail("对比失败：" + e.getMessage()); }
    }

    @Operation(summary = "获取资产质量监管建议")
    @PostMapping("/quality/supervision-suggestions")
    public R<List<Map<String, Object>>> qualitySupervision(@RequestBody Map<String, Object> params) {
        try { return R.success(new ArrayList<>()); }
        catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "资产质量压力测试")
    @PostMapping("/quality/stress-test")
    public R<Map<String, Object>> qualityStressTest(@RequestBody Map<String, Object> params) {
        try { return R.success(new HashMap<>()); }
        catch (Exception e) { return R.fail("测试失败：" + e.getMessage()); }
    }

    @Operation(summary = "资产质量情景分析")
    @PostMapping("/quality/scenario-analysis")
    public R<Map<String, Object>> qualityScenario(@RequestBody Map<String, Object> params) {
        try { return R.success(new HashMap<>()); }
        catch (Exception e) { return R.fail("分析失败：" + e.getMessage()); }
    }

    @Operation(summary = "资产质量绩效评估")
    @PostMapping("/quality/performance-evaluation")
    public R<Map<String, Object>> qualityPerformance(@RequestBody Map<String, Object> params) {
        try { return R.success(new HashMap<>()); }
        catch (Exception e) { return R.fail("评估失败：" + e.getMessage()); }
    }

    @Operation(summary = "资产质量历史版本")
    @PostMapping("/quality/version-history")
    public R<List<Map<String, Object>>> qualityVersionHistory(@RequestBody Map<String, Object> params) {
        try { return R.success(new ArrayList<>()); }
        catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "恢复资产质量历史版本")
    @PostMapping("/quality/restore-version")
    public R<Boolean> qualityRestoreVersion(@RequestBody Map<String, Object> params) { return R.success(true); }

    // ==================== 私有辅助方法 ====================

    private int getPageNumber(Map<String, Object> params) {
        if (params.get("pageNum") != null) return Integer.parseInt(params.get("pageNum").toString());
        if (params.get("pageNumber") != null) return Integer.parseInt(params.get("pageNumber").toString());
        return 1;
    }

    private int getPageSize(Map<String, Object> params) {
        if (params.get("pageSize") != null) return Integer.parseInt(params.get("pageSize").toString());
        return 15;
    }

    private <T> PageResult<T> buildManualPage(List<T> list, int pn, int ps) {
        int total = list.size();
        int totalPages = (total + ps - 1) / ps;
        int fromIndex = Math.min((pn - 1) * ps, total);
        int toIndex = Math.min(fromIndex + ps, total);
        PageResult<T> pr = new PageResult<>();
        pr.setTotalRecord(total); pr.setCurrentPage(pn);
        pr.setPageNumber(pn); pr.setTotalPage(totalPages);
        pr.setPageSize(ps); pr.setTlist(list.subList(fromIndex, toIndex));
        return pr;
    }

    private List<Map<String, Object>> buildConcentrationData(List<TblPropertyRight> properties) {
        List<Map<String, Object>> result = new ArrayList<>();
        for (TblPropertyRight pr : properties) {
            result.add(buildConcentrationItem(pr));
        }
        return result;
    }

    private Map<String, Object> buildConcentrationItem(TblPropertyRight pr) {
        Map<String, Object> item = new HashMap<>();
        item.put("propertyId", pr.getPropertyId());
        item.put("companyName", pr.getCompanyName());
        item.put("industry", pr.getIndustry());
        item.put("region", pr.getRegion());
        item.put("investAmount", pr.getInvestAmount());
        item.put("registeredCapital", pr.getRegisteredCapital());
        item.put("equityRatio", pr.getEquityRatio());
        item.put("equityLevel", pr.getEquityLevel());
        return item;
    }

    private Map<String, Object> calculateConcentrationMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        List<TblPropertyRight> all = propertyRightMapper.selectList(null);
        metrics.put("totalCompanies", all.size());
        // 按行业计算HHI
        Map<String, List<TblPropertyRight>> byIndustry = all.stream()
            .filter(p -> p.getIndustry() != null).collect(Collectors.groupingBy(TblPropertyRight::getIndustry));
        double hhi = 0;
        double total = all.size();
        for (List<TblPropertyRight> list : byIndustry.values()) {
            double share = list.size() / total;
            hhi += share * share * 10000;
        }
        metrics.put("hhi", Math.round(hhi * 100.0) / 100.0);
        metrics.put("industryCount", byIndustry.size());
        // CR4
        List<Integer> industrySizes = byIndustry.values().stream().map(List::size).sorted(Collections.reverseOrder()).collect(Collectors.toList());
        int cr4 = 0;
        for (int i = 0; i < Math.min(4, industrySizes.size()); i++) {
            cr4 += industrySizes.get(i);
        }
        metrics.put("cr4", total > 0 ? Math.round((double) cr4 / total * 10000.0) / 100.0 : 0);
        metrics.put("concentrationLevel", hhi > 2500 ? "HIGH" : hhi > 1500 ? "MEDIUM" : "LOW");
        return metrics;
    }
}
