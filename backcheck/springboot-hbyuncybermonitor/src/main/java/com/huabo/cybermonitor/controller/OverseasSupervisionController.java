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

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Tag(name = "境外穿透式监管", description = "境外穿透式监管全接口")
@RestController
@RequestMapping("/v1/supervision/overseas")
@Slf4j
public class OverseasSupervisionController {

    @Autowired private GzctOverseasInvestMapper investMapper;
    @Autowired private GzctOverseasOperationMapper operationMapper;
    @Autowired private GzctOverseasCountryRiskMapper countryRiskMapper;
    @Autowired private GzctOverseasForexMapper forexMapper;
    @Autowired private GzctOverseasComplianceMapper complianceMapper;
    @Autowired private GzctOverseasPersonnelMapper personnelMapper;
    @Autowired private GzctOverseasEmergencyMapper emergencyMapper;
    @Autowired private GzctOverseasWarningMapper warningMapper;
    @Autowired private GzctOverseasLeaderMapper leaderMapper;
    @Autowired private GzctOverseasEmergencyPlanMapper emergencyPlanMapper;
    @Autowired private com.huabo.cybermonitor.util.OrgQueryHelper orgQueryHelper;

    // ==================== 境外投资 ====================

    @Operation(summary = "境外投资列表")
    @PostMapping("/invest/list")
    public R<PageResult<GzctOverseasInvest>> investList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctOverseasInvest> w = new LambdaQueryWrapper<>();
            // 企业名称模糊查询（同时匹配 companyName 和 unitName）
            if (params.get("companyName") != null && StringUtils.isNotBlank(params.get("companyName").toString())) {
                String companyName = params.get("companyName").toString();
                w.and(wrapper -> wrapper.like(GzctOverseasInvest::getCompanyName, companyName)
                        .or().like(GzctOverseasInvest::getUnitName, companyName));
            }
            if (params.get("country") != null && StringUtils.isNotBlank(params.get("country").toString())) {
                w.like(GzctOverseasInvest::getCountry, params.get("country").toString());
            }
            if (params.get("projectName") != null && StringUtils.isNotBlank(params.get("projectName").toString())) {
                w.like(GzctOverseasInvest::getProjectName, params.get("projectName").toString());
            }
            // 投资类型精确匹配
            if (params.get("investType") != null && StringUtils.isNotBlank(params.get("investType").toString())) {
                w.eq(GzctOverseasInvest::getInvestType, params.get("investType").toString());
            }
            if (params.get("approvalStatus") != null && StringUtils.isNotBlank(params.get("approvalStatus").toString())) {
                w.eq(GzctOverseasInvest::getApprovalStatus, params.get("approvalStatus").toString());
            }
            if (params.get("riskLevel") != null && StringUtils.isNotBlank(params.get("riskLevel").toString())) {
                w.eq(GzctOverseasInvest::getRiskLevel, params.get("riskLevel").toString());
            }
            w.orderByDesc(GzctOverseasInvest::getCreateTime);
            Page<GzctOverseasInvest> r = new GzctOverseasInvest().selectPage(new Page<>(pn, ps), w);
            PageResult<GzctOverseasInvest> pr = new PageResult<>();
            pr.setTotalRecord((int) r.getTotal()); pr.setCurrentPage((int) r.getCurrent());
            pr.setPageNumber((int) r.getCurrent()); pr.setTotalPage((int) r.getPages());
            pr.setPageSize((int) r.getSize()); pr.setTlist(r.getRecords());
            return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "境外投资详情")
    @GetMapping("/invest/{id}")
    public R<GzctOverseasInvest> investDetail(@PathVariable String id) {
        try {
            GzctOverseasInvest invest = investMapper.selectById(id);
            if (invest == null) { return R.fail("未找到该投资记录"); }
            return R.success(invest);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "新增境外投资")
    @PostMapping("/invest/add")
    public R<Boolean> addInvest(@RequestBody GzctOverseasInvest record) {
        try { record.setCreateTime(LocalDateTime.now()); investMapper.insert(record); return R.success(true); } catch (Exception e) { return R.fail("新增失败：" + e.getMessage()); }
    }

    @Operation(summary = "更新境外投资")
    @PostMapping("/invest/update")
    public R<Boolean> updateInvest(@RequestBody GzctOverseasInvest record) {
        try { record.setUpdateTime(LocalDateTime.now()); investMapper.updateById(record); return R.success(true); } catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); }
    }

    @Operation(summary = "删除境外投资")
    @DeleteMapping("/invest/{id}")
    public R<Boolean> deleteInvest(@PathVariable String id) {
        try { return R.success(investMapper.deleteById(id) > 0); } catch (Exception e) { return R.fail("删除失败：" + e.getMessage()); }
    }

    @Operation(summary = "批量删除境外投资")
    @PostMapping("/invest/batch/delete")
    public R<Boolean> batchDeleteInvest(@RequestBody Map<String, Object> params) {
        try {
            Object idsObj = params.get("ids");
            if (idsObj == null) { return R.fail("请提供要删除的ID列表"); }
            List<String> ids;
            if (idsObj instanceof List) {
                ids = ((List<?>) idsObj).stream().map(Object::toString).collect(Collectors.toList());
            } else {
                ids = Arrays.asList(idsObj.toString().split(","));
            }
            if (ids.isEmpty()) { return R.fail("请提供要删除的ID列表"); }
            int deleted = investMapper.deleteBatchIds(ids);
            return R.success(deleted > 0);
        } catch (Exception e) { return R.fail("批量删除失败：" + e.getMessage()); }
    }

    @Operation(summary = "境外投资审批")
    @GetMapping("/invest/approval/{id}")
    public R<Map<String, Object>> investApproval(@PathVariable String id) {
        try { Map<String, Object> r = new HashMap<>(); r.put("investId", id); r.put("approvalStatus", "PENDING"); return R.success(r); } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 境外经营 ====================

    @Operation(summary = "境外经营统计")
    @GetMapping("/operation/stats")
    public R<Map<String, Object>> operationStats() {
        try {
            Map<String, Object> r = new HashMap<>();
            List<GzctOverseasOperation> all = operationMapper.selectList(null);

            // KPI统计
            java.math.BigDecimal totalRevenue = all.stream()
                .map(o -> o.getRevenue() != null ? o.getRevenue() : java.math.BigDecimal.ZERO)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
            java.math.BigDecimal totalProfit = all.stream()
                .map(o -> o.getProfit() != null ? o.getProfit() : java.math.BigDecimal.ZERO)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
            long profitableCount = all.stream()
                .filter(o -> o.getProfit() != null && o.getProfit().compareTo(java.math.BigDecimal.ZERO) > 0)
                .count();
            long lossCount = all.stream()
                .filter(o -> o.getProfit() != null && o.getProfit().compareTo(java.math.BigDecimal.ZERO) < 0)
                .count();
            double profitRatio = all.isEmpty() ? 0 : Math.round(profitableCount * 1000.0 / all.size()) / 10.0;

            r.put("revenue", totalRevenue.divide(new java.math.BigDecimal("10000"), 2, java.math.BigDecimal.ROUND_HALF_UP));
            r.put("profit", totalProfit.divide(new java.math.BigDecimal("10000"), 2, java.math.BigDecimal.ROUND_HALF_UP));
            r.put("profitRatio", profitRatio);
            r.put("lossCount", lossCount);
            r.put("unitCount", all.size());

            // 趋势图数据 - 按年份分组
            Map<String, java.math.BigDecimal[]> yearMap = new LinkedHashMap<>();
            for (GzctOverseasOperation op : all) {
                String year = op.getReportYear() != null ? op.getReportYear() : "2024";
                yearMap.computeIfAbsent(year, k -> new java.math.BigDecimal[]{java.math.BigDecimal.ZERO, java.math.BigDecimal.ZERO});
                java.math.BigDecimal[] arr = yearMap.get(year);
                arr[0] = arr[0].add(op.getRevenue() != null ? op.getRevenue() : java.math.BigDecimal.ZERO);
                arr[1] = arr[1].add(op.getProfit() != null ? op.getProfit() : java.math.BigDecimal.ZERO);
            }
            Map<String, Object> trendData = new HashMap<>();
            List<String> years = new ArrayList<>(yearMap.keySet());
            Collections.sort(years);
            List<Object> trendRevenue = new ArrayList<>();
            List<Object> trendProfit = new ArrayList<>();
            List<Object> growthRate = new ArrayList<>();
            java.math.BigDecimal prevRevenue = null;
            for (String year : years) {
                java.math.BigDecimal[] arr = yearMap.get(year);
                java.math.BigDecimal rev = arr[0].divide(new java.math.BigDecimal("10000"), 2, java.math.BigDecimal.ROUND_HALF_UP);
                java.math.BigDecimal prof = arr[1].divide(new java.math.BigDecimal("10000"), 2, java.math.BigDecimal.ROUND_HALF_UP);
                trendRevenue.add(rev);
                trendProfit.add(prof);
                if (prevRevenue != null && prevRevenue.compareTo(java.math.BigDecimal.ZERO) > 0) {
                    double rate = arr[0].subtract(prevRevenue.multiply(new java.math.BigDecimal("10000")))
                        .divide(prevRevenue.multiply(new java.math.BigDecimal("10000")), 4, java.math.BigDecimal.ROUND_HALF_UP)
                        .multiply(new java.math.BigDecimal("100")).doubleValue();
                    growthRate.add(Math.round(rate * 10.0) / 10.0);
                } else {
                    growthRate.add(0);
                }
                prevRevenue = rev;
            }
            trendData.put("years", years);
            trendData.put("revenue", trendRevenue);
            trendData.put("profit", trendProfit);
            trendData.put("growthRate", growthRate);
            r.put("trendData", trendData);

            // 饼图数据 - 按国家分组营收
            Map<String, java.math.BigDecimal> countryRevMap = new LinkedHashMap<>();
            for (GzctOverseasOperation op : all) {
                String country = op.getCountry() != null ? op.getCountry() : "未知";
                countryRevMap.merge(country, op.getRevenue() != null ? op.getRevenue() : java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
            }
            List<Map<String, Object>> pieData = new ArrayList<>();
            for (Map.Entry<String, java.math.BigDecimal> e : countryRevMap.entrySet()) {
                Map<String, Object> m = new HashMap<>();
                m.put("name", e.getKey());
                m.put("value", e.getValue().divide(new java.math.BigDecimal("10000"), 2, java.math.BigDecimal.ROUND_HALF_UP));
                pieData.add(m);
            }
            r.put("pieData", pieData);

            return R.success(r);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "境外经营列表")
    @PostMapping("/operation/list")
    public R<PageResult<GzctOverseasOperation>> operationList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctOverseasOperation> w = new LambdaQueryWrapper<>();
            if (params.get("country") != null && StringUtils.isNotBlank(params.get("country").toString())) {
                w.like(GzctOverseasOperation::getCountry, params.get("country").toString());
            }
            if (params.get("companyName") != null && StringUtils.isNotBlank(params.get("companyName").toString())) {
                w.like(GzctOverseasOperation::getCompanyName, params.get("companyName").toString());
            }
            if (params.get("reportYear") != null && StringUtils.isNotBlank(params.get("reportYear").toString())) {
                w.eq(GzctOverseasOperation::getReportYear, params.get("reportYear").toString());
            }
            if (params.get("unitName") != null && StringUtils.isNotBlank(params.get("unitName").toString())) {
                w.like(GzctOverseasOperation::getCompanyName, params.get("unitName").toString());
            }
            if (params.get("operationStatus") != null && StringUtils.isNotBlank(params.get("operationStatus").toString())) {
                w.eq(GzctOverseasOperation::getOperationStatus, params.get("operationStatus").toString());
            }
            w.orderByDesc(GzctOverseasOperation::getCreateTime);
            Page<GzctOverseasOperation> r = new GzctOverseasOperation().selectPage(new Page<>(pn, ps), w);
            PageResult<GzctOverseasOperation> pr = new PageResult<>();
            pr.setTotalRecord((int) r.getTotal()); pr.setCurrentPage((int) r.getCurrent());
            pr.setPageNumber((int) r.getCurrent()); pr.setTotalPage((int) r.getPages());
            pr.setPageSize((int) r.getSize()); pr.setTlist(r.getRecords());
            return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "新增境外经营")
    @PostMapping("/operation/add")
    public R<Boolean> addOperation(@RequestBody GzctOverseasOperation record) {
        try { record.setCreateTime(LocalDateTime.now()); operationMapper.insert(record); return R.success(true); } catch (Exception e) { return R.fail("新增失败：" + e.getMessage()); }
    }

    @Operation(summary = "更新境外经营")
    @PostMapping("/operation/update")
    public R<Boolean> updateOperation(@RequestBody GzctOverseasOperation record) {
        try { record.setUpdateTime(LocalDateTime.now()); operationMapper.updateById(record); return R.success(true); } catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); }
    }

    @Operation(summary = "删除境外经营")
    @DeleteMapping("/operation/{id}")
    public R<Boolean> deleteOperation(@PathVariable String id) {
        try { return R.success(operationMapper.deleteById(id) > 0); } catch (Exception e) { return R.fail("删除失败：" + e.getMessage()); }
    }

    // ==================== 国别风险 ====================

    @Operation(summary = "国别风险列表")
    @PostMapping("/country/risk/list")
    public R<PageResult<GzctOverseasCountryRisk>> countryRiskList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctOverseasCountryRisk> w = new LambdaQueryWrapper<>();
            if (params.get("country") != null && StringUtils.isNotBlank(params.get("country").toString())) {
                w.like(GzctOverseasCountryRisk::getCountry, params.get("country").toString());
            }
            if (params.get("riskLevel") != null && StringUtils.isNotBlank(params.get("riskLevel").toString())) {
                w.eq(GzctOverseasCountryRisk::getRiskLevel, params.get("riskLevel").toString());
            }
            w.orderByDesc(GzctOverseasCountryRisk::getCreateTime);
            Page<GzctOverseasCountryRisk> r = new GzctOverseasCountryRisk().selectPage(new Page<>(pn, ps), w);
            PageResult<GzctOverseasCountryRisk> pr = new PageResult<>();
            pr.setTotalRecord((int) r.getTotal()); pr.setCurrentPage((int) r.getCurrent());
            pr.setPageNumber((int) r.getCurrent()); pr.setTotalPage((int) r.getPages());
            pr.setPageSize((int) r.getSize()); pr.setTlist(r.getRecords());
            return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "国别风险统计")
    @GetMapping("/country/risk/stats")
    public R<Map<String, Object>> countryRiskStats() {
        try {
            Map<String, Object> r = new HashMap<>();
            r.put("highRiskCount", countryRiskMapper.selectCount(new LambdaQueryWrapper<GzctOverseasCountryRisk>().eq(GzctOverseasCountryRisk::getRiskLevel, "HIGH")));
            r.put("mediumRiskCount", countryRiskMapper.selectCount(new LambdaQueryWrapper<GzctOverseasCountryRisk>().eq(GzctOverseasCountryRisk::getRiskLevel, "MEDIUM")));
            r.put("lowRiskCount", countryRiskMapper.selectCount(new LambdaQueryWrapper<GzctOverseasCountryRisk>().eq(GzctOverseasCountryRisk::getRiskLevel, "LOW")));
            r.put("totalCountries", countryRiskMapper.selectCount(null));
            return R.success(r);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 外汇风险 ====================

    @Operation(summary = "外汇风险列表")
    @PostMapping("/forex/list")
    public R<PageResult<GzctOverseasForex>> forexList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctOverseasForex> w = new LambdaQueryWrapper<>();
            if (params.get("currency") != null && StringUtils.isNotBlank(params.get("currency").toString())) {
                w.eq(GzctOverseasForex::getCurrency, params.get("currency").toString());
            }
            if (params.get("riskLevel") != null && StringUtils.isNotBlank(params.get("riskLevel").toString())) {
                w.eq(GzctOverseasForex::getRiskLevel, params.get("riskLevel").toString());
            }
            if (params.get("companyName") != null && StringUtils.isNotBlank(params.get("companyName").toString())) {
                w.like(GzctOverseasForex::getCompanyName, params.get("companyName").toString());
            }
            w.orderByDesc(GzctOverseasForex::getCreateTime);
            Page<GzctOverseasForex> r = new GzctOverseasForex().selectPage(new Page<>(pn, ps), w);
            PageResult<GzctOverseasForex> pr = new PageResult<>();
            pr.setTotalRecord((int) r.getTotal()); pr.setCurrentPage((int) r.getCurrent());
            pr.setPageNumber((int) r.getCurrent()); pr.setTotalPage((int) r.getPages());
            pr.setPageSize((int) r.getSize()); pr.setTlist(r.getRecords());
            return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "外汇风险统计")
    @GetMapping("/forex/stats")
    public R<Map<String, Object>> forexStats() {
        try {
            Map<String, Object> r = new HashMap<>();
            List<GzctOverseasForex> all = forexMapper.selectList(null);

            java.math.BigDecimal totalExposure = all.stream()
                .map(f -> f.getExposureAmount() != null ? f.getExposureAmount() : java.math.BigDecimal.ZERO)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
            java.math.BigDecimal totalHedge = all.stream()
                .map(f -> f.getHedgeAmount() != null ? f.getHedgeAmount() : java.math.BigDecimal.ZERO)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
            double avgRatio = all.stream()
                .filter(f -> f.getHedgeRatio() != null)
                .mapToDouble(f -> f.getHedgeRatio().doubleValue())
                .average().orElse(0);
            java.math.BigDecimal totalImpact = all.stream()
                .map(f -> f.getImpactAmount() != null ? f.getImpactAmount() : java.math.BigDecimal.ZERO)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
            long currencyCount = all.stream()
                .map(GzctOverseasForex::getCurrency)
                .filter(Objects::nonNull)
                .distinct().count();

            r.put("totalExposure", totalExposure);
            r.put("totalHedge", totalHedge);
            r.put("avgHedgeRatio", Math.round(avgRatio * 100.0) / 100.0);
            // 前端KPI期望字段
            r.put("forexExposure", totalExposure.divide(new java.math.BigDecimal("10000"), 2, java.math.BigDecimal.ROUND_HALF_UP));
            r.put("hedgeRatio", Math.round(avgRatio * 100.0) / 100.0);
            r.put("exchangeGainLoss", totalImpact);
            r.put("currencyCount", currencyCount);

            // 玫瑰图数据 - 按币种分组的敞口金额
            Map<String, java.math.BigDecimal> currencyMap = new LinkedHashMap<>();
            for (GzctOverseasForex f : all) {
                String currency = f.getCurrency() != null ? f.getCurrency() : "其他";
                currencyMap.merge(currency, f.getExposureAmount() != null ? f.getExposureAmount() : java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
            }
            List<Map<String, Object>> roseData = new ArrayList<>();
            for (Map.Entry<String, java.math.BigDecimal> entry : currencyMap.entrySet()) {
                Map<String, Object> item = new HashMap<>();
                item.put("name", entry.getKey());
                item.put("value", entry.getValue());
                roseData.add(item);
            }
            r.put("roseData", roseData);

            // 汇率趋势数据
            Map<String, Object> rateData = new HashMap<>();
            rateData.put("legends", Arrays.asList("USD/CNY", "EUR/CNY", "GBP/CNY"));
            rateData.put("months", Arrays.asList("1月", "2月", "3月", "4月", "5月", "6月", "7月", "8月", "9月", "10月", "11月", "12月"));
            List<Map<String, Object>> series = new ArrayList<>();
            Map<String, Object> usdSeries = new HashMap<>();
            usdSeries.put("name", "USD/CNY");
            usdSeries.put("data", Arrays.asList(7.18, 7.20, 7.22, 7.24, 7.21, 7.25, 7.27, 7.24, 7.22, 7.20, 7.23, 7.24));
            usdSeries.put("color", "#1677FF");
            series.add(usdSeries);
            Map<String, Object> eurSeries = new HashMap<>();
            eurSeries.put("name", "EUR/CNY");
            eurSeries.put("data", Arrays.asList(7.80, 7.82, 7.85, 7.83, 7.81, 7.86, 7.88, 7.85, 7.83, 7.80, 7.84, 7.86));
            eurSeries.put("color", "#52C41A");
            series.add(eurSeries);
            Map<String, Object> gbpSeries = new HashMap<>();
            gbpSeries.put("name", "GBP/CNY");
            gbpSeries.put("data", Arrays.asList(9.10, 9.12, 9.15, 9.18, 9.14, 9.20, 9.22, 9.18, 9.15, 9.12, 9.16, 9.18));
            gbpSeries.put("color", "#FA8C16");
            series.add(gbpSeries);
            rateData.put("series", series);
            r.put("rateData", rateData);

            return R.success(r);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 境外合规 ====================

    @Operation(summary = "境外合规列表")
    @PostMapping("/compliance/list")
    public R<PageResult<GzctOverseasCompliance>> complianceList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctOverseasCompliance> w = new LambdaQueryWrapper<>();
            if (params.get("country") != null && StringUtils.isNotBlank(params.get("country").toString())) {
                w.like(GzctOverseasCompliance::getCountry, params.get("country").toString());
            }
            if (params.get("complianceType") != null && StringUtils.isNotBlank(params.get("complianceType").toString())) {
                w.eq(GzctOverseasCompliance::getComplianceType, params.get("complianceType").toString());
            }
            if (params.get("isCompliant") != null && StringUtils.isNotBlank(params.get("isCompliant").toString())) {
                w.eq(GzctOverseasCompliance::getIsCompliant, params.get("isCompliant").toString());
            }
            w.orderByDesc(GzctOverseasCompliance::getCreateTime);
            Page<GzctOverseasCompliance> r = new GzctOverseasCompliance().selectPage(new Page<>(pn, ps), w);
            PageResult<GzctOverseasCompliance> pr = new PageResult<>();
            pr.setTotalRecord((int) r.getTotal()); pr.setCurrentPage((int) r.getCurrent());
            pr.setPageNumber((int) r.getCurrent()); pr.setTotalPage((int) r.getPages());
            pr.setPageSize((int) r.getSize()); pr.setTlist(r.getRecords());
            return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "新增境外合规")
    @PostMapping("/compliance/add")
    public R<Boolean> addCompliance(@RequestBody GzctOverseasCompliance record) {
        try { record.setCreateTime(LocalDateTime.now()); complianceMapper.insert(record); return R.success(true); } catch (Exception e) { return R.fail("新增失败：" + e.getMessage()); }
    }

    @Operation(summary = "更新境外合规")
    @PostMapping("/compliance/update")
    public R<Boolean> updateCompliance(@RequestBody GzctOverseasCompliance record) {
        try { record.setUpdateTime(LocalDateTime.now()); complianceMapper.updateById(record); return R.success(true); } catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); }
    }

    @Operation(summary = "删除境外合规")
    @DeleteMapping("/compliance/{id}")
    public R<Boolean> deleteCompliance(@PathVariable String id) {
        try { return R.success(complianceMapper.deleteById(id) > 0); } catch (Exception e) { return R.fail("删除失败：" + e.getMessage()); }
    }

    @Operation(summary = "境外违规列表")
    @PostMapping("/compliance/violation/list")
    public R<PageResult<GzctOverseasCompliance>> violationList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctOverseasCompliance> w = new LambdaQueryWrapper<>();
            w.eq(GzctOverseasCompliance::getIsCompliant, "0");
            Page<GzctOverseasCompliance> r = new GzctOverseasCompliance().selectPage(new Page<>(pn, ps), w);
            PageResult<GzctOverseasCompliance> pr = new PageResult<>();
            pr.setTotalRecord((int) r.getTotal()); pr.setCurrentPage((int) r.getCurrent());
            pr.setPageNumber((int) r.getCurrent()); pr.setTotalPage((int) r.getPages());
            pr.setPageSize((int) r.getSize()); pr.setTlist(r.getRecords());
            return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 境外人员 ====================

    @Operation(summary = "境外人员列表")
    @PostMapping("/personnel/list")
    public R<PageResult<GzctOverseasPersonnel>> personnelList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctOverseasPersonnel> w = new LambdaQueryWrapper<>();
            if (params.get("country") != null && StringUtils.isNotBlank(params.get("country").toString())) {
                w.like(GzctOverseasPersonnel::getCountry, params.get("country").toString());
            }
            if (params.get("personnelName") != null && StringUtils.isNotBlank(params.get("personnelName").toString())) {
                w.like(GzctOverseasPersonnel::getPersonnelName, params.get("personnelName").toString());
            }
            if (params.get("safetyStatus") != null && StringUtils.isNotBlank(params.get("safetyStatus").toString())) {
                w.eq(GzctOverseasPersonnel::getSafetyStatus, params.get("safetyStatus").toString());
            }
            w.orderByDesc(GzctOverseasPersonnel::getCreateTime);
            Page<GzctOverseasPersonnel> r = new GzctOverseasPersonnel().selectPage(new Page<>(pn, ps), w);
            PageResult<GzctOverseasPersonnel> pr = new PageResult<>();
            pr.setTotalRecord((int) r.getTotal()); pr.setCurrentPage((int) r.getCurrent());
            pr.setPageNumber((int) r.getCurrent()); pr.setTotalPage((int) r.getPages());
            pr.setPageSize((int) r.getSize()); pr.setTlist(r.getRecords());
            return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "境外人员统计")
    @GetMapping("/personnel/stats")
    public R<Map<String, Object>> personnelStats() {
        try {
            Map<String, Object> r = new HashMap<>();
            r.put("totalCount", personnelMapper.selectCount(null));
            r.put("atRiskCount", personnelMapper.selectCount(new LambdaQueryWrapper<GzctOverseasPersonnel>().eq(GzctOverseasPersonnel::getSafetyStatus, "AT_RISK")));
            r.put("emergencyCount", personnelMapper.selectCount(new LambdaQueryWrapper<GzctOverseasPersonnel>().eq(GzctOverseasPersonnel::getSafetyStatus, "EMERGENCY")));
            return R.success(r);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "新增境外人员")
    @PostMapping("/personnel/add")
    public R<Boolean> addPersonnel(@RequestBody GzctOverseasPersonnel record) {
        try { record.setCreateTime(LocalDateTime.now()); personnelMapper.insert(record); return R.success(true); } catch (Exception e) { return R.fail("新增失败：" + e.getMessage()); }
    }

    @Operation(summary = "更新境外人员")
    @PostMapping("/personnel/update")
    public R<Boolean> updatePersonnel(@RequestBody GzctOverseasPersonnel record) {
        try { record.setUpdateTime(LocalDateTime.now()); personnelMapper.updateById(record); return R.success(true); } catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); }
    }

    @Operation(summary = "删除境外人员")
    @DeleteMapping("/personnel/{id}")
    public R<Boolean> deletePersonnel(@PathVariable String id) {
        try {
            int rows = personnelMapper.deleteById(id);
            if (rows > 0) {
                return R.success(true);
            } else {
                return R.fail("删除失败：记录不存在");
            }
        } catch (Exception e) {
            return R.fail("删除失败：" + e.getMessage());
        }
    }

    // ==================== 应急事件 ====================

    @Operation(summary = "应急事件列表")
    @PostMapping("/emergency/list")
    public R<PageResult<GzctOverseasEmergency>> emergencyList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctOverseasEmergency> w = new LambdaQueryWrapper<>();
            if (params.get("country") != null && StringUtils.isNotBlank(params.get("country").toString())) {
                w.like(GzctOverseasEmergency::getCountry, params.get("country").toString());
            }
            if (params.get("eventType") != null && StringUtils.isNotBlank(params.get("eventType").toString())) {
                w.eq(GzctOverseasEmergency::getEventType, params.get("eventType").toString());
            }
            if (params.get("severity") != null && StringUtils.isNotBlank(params.get("severity").toString())) {
                w.eq(GzctOverseasEmergency::getSeverity, params.get("severity").toString());
            }
            if (params.get("status") != null && StringUtils.isNotBlank(params.get("status").toString())) {
                w.eq(GzctOverseasEmergency::getStatus, params.get("status").toString());
            }
            w.orderByDesc(GzctOverseasEmergency::getCreateTime);
            Page<GzctOverseasEmergency> r = new GzctOverseasEmergency().selectPage(new Page<>(pn, ps), w);
            PageResult<GzctOverseasEmergency> pr = new PageResult<>();
            pr.setTotalRecord((int) r.getTotal()); pr.setCurrentPage((int) r.getCurrent());
            pr.setPageNumber((int) r.getCurrent()); pr.setTotalPage((int) r.getPages());
            pr.setPageSize((int) r.getSize()); pr.setTlist(r.getRecords());
            return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "新增应急事件")
    @PostMapping("/emergency/add")
    public R<Boolean> addEmergency(@RequestBody GzctOverseasEmergency record) {
        try { record.setCreateTime(LocalDateTime.now()); emergencyMapper.insert(record); return R.success(true); } catch (Exception e) { return R.fail("新增失败：" + e.getMessage()); }
    }

    @Operation(summary = "更新应急事件")
    @PostMapping("/emergency/update")
    public R<Boolean> updateEmergency(@RequestBody GzctOverseasEmergency record) {
        try { record.setUpdateTime(LocalDateTime.now()); emergencyMapper.updateById(record); return R.success(true); } catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); }
    }

    @Operation(summary = "删除应急事件")
    @DeleteMapping("/emergency/{id}")
    public R<Boolean> deleteEmergency(@PathVariable String id) {
        try { return R.success(emergencyMapper.deleteById(id) > 0); } catch (Exception e) { return R.fail("删除失败：" + e.getMessage()); }
    }

    // ==================== 首页KPI ====================

    @Operation(summary = "境外首页KPI")
    @GetMapping("/kpi")
    public R<Map<String, Object>> kpi() {
        Map<String, Object> result = new HashMap<>();
        // 设置默认值，确保即使部分表查询失败也能返回有效数据
        result.put("unitCount", 0L);
        result.put("investTotal", java.math.BigDecimal.ZERO);
        result.put("personnelCount", 0L);
        result.put("highRiskCountries", 0L);
        result.put("forexExposure", java.math.BigDecimal.ZERO);
        result.put("complianceWarning", 0L);
        result.put("investCount", 0L);
        result.put("countryCount", 0L);
        result.put("currencyCount", 0L);
        result.put("complianceCount", 0L);
        result.put("recentWarnings", new ArrayList<>());

        // 境外单位数和投资总额
        try {
            List<GzctOverseasInvest> allInvests = investMapper.selectList(null);
            long unitCount = allInvests.stream().map(GzctOverseasInvest::getUnitId).filter(Objects::nonNull).distinct().count();
            result.put("unitCount", unitCount);
            java.math.BigDecimal investTotalRaw = allInvests.stream()
                .map(GzctOverseasInvest::getInvestAmount)
                .filter(Objects::nonNull)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
            result.put("investTotal", investTotalRaw.divide(new java.math.BigDecimal("10000"), 2, java.math.BigDecimal.ROUND_HALF_UP));
            result.put("investCount", (long) allInvests.size());
        } catch (Exception e) {
            log.warn("查询境外投资数据失败: {}", e.getMessage());
        }

        // 境外人员数
        try {
            Long personnelCount = personnelMapper.selectCount(null);
            result.put("personnelCount", personnelCount);
        } catch (Exception e) {
            log.warn("查询境外人员数据失败: {}", e.getMessage());
        }

        // 高风险国家数和国家总数
        try {
            Long highRiskCountries = countryRiskMapper.selectCount(new LambdaQueryWrapper<GzctOverseasCountryRisk>().eq(GzctOverseasCountryRisk::getRiskLevel, "HIGH"));
            result.put("highRiskCountries", highRiskCountries);
            Long countryCount = countryRiskMapper.selectCount(null);
            result.put("countryCount", countryCount);
        } catch (Exception e) {
            log.warn("查询国别风险数据失败: {}", e.getMessage());
        }

        // 外汇敞口和币种数
        try {
            List<GzctOverseasForex> allForex = forexMapper.selectList(null);
            java.math.BigDecimal forexExposureRaw = allForex.stream()
                .map(GzctOverseasForex::getExposureAmount)
                .filter(Objects::nonNull)
                .reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
            result.put("forexExposure", forexExposureRaw.divide(new java.math.BigDecimal("10000"), 2, java.math.BigDecimal.ROUND_HALF_UP));
            long currencyCount = allForex.stream().map(GzctOverseasForex::getCurrency).filter(Objects::nonNull).distinct().count();
            result.put("currencyCount", currencyCount);
        } catch (Exception e) {
            log.warn("查询外汇数据失败: {}", e.getMessage());
        }

        // 合规预警数和合规条数
        try {
            Long complianceWarning = warningMapper.selectCount(new LambdaQueryWrapper<GzctOverseasWarning>().eq(GzctOverseasWarning::getStatus, "PENDING"));
            result.put("complianceWarning", complianceWarning);
        } catch (Exception e) {
            log.warn("查询预警数据失败: {}", e.getMessage());
        }

        try {
            Long complianceCount = complianceMapper.selectCount(null);
            result.put("complianceCount", complianceCount);
        } catch (Exception e) {
            log.warn("查询合规数据失败: {}", e.getMessage());
        }

        // 最近预警（达梦兼容分页）
        try {
            result.put("recentWarnings", warningMapper.selectList(
                new LambdaQueryWrapper<GzctOverseasWarning>().eq(GzctOverseasWarning::getStatus, "PENDING").orderByDesc(GzctOverseasWarning::getCreateTime).last("FETCH FIRST 5 ROWS ONLY")));
        } catch (Exception e) {
            log.warn("查询最近预警失败: {}", e.getMessage());
        }

        return R.success(result);
    }

    // ==================== 境外驾驶舱 ====================

    @Operation(summary = "境外驾驶舱概览")
    @GetMapping("/dashboard")
    public R<Map<String, Object>> dashboard(@RequestParam(required = false) String companyId) {
        try {
            Map<String, Object> result = new HashMap<>();
            final String orgId = companyId;
            String orgPattern = orgQueryHelper.getOrgPathPattern(orgId);
            // 前端期望字段：companyCount/countryCount/investTotal/warningCount
            // 境外企业数（按unitId去重）
            LambdaQueryWrapper<GzctOverseasInvest> investWrapper = new LambdaQueryWrapper<>();
            if (orgPattern != null) investWrapper.and(w -> w.like(GzctOverseasInvest::getOrgPath, orgPattern).or(sub -> sub.isNull(GzctOverseasInvest::getOrgPath).eq(GzctOverseasInvest::getUnitId, orgId)));
            List<GzctOverseasInvest> allInvests = investMapper.selectList(investWrapper);
            long companyCount = allInvests.stream().map(GzctOverseasInvest::getUnitId).filter(Objects::nonNull).distinct().count();
            result.put("companyCount", companyCount);
            // 涉及国家数
            Long countryCount = countryRiskMapper.selectCount(null);
            result.put("countryCount", countryCount);
            result.put("countries", countryCount);
            // 投资总额（亿美元）
            java.math.BigDecimal investTotalRaw = allInvests.stream().map(GzctOverseasInvest::getInvestAmount).filter(Objects::nonNull).reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
            result.put("investTotal", investTotalRaw.divide(new java.math.BigDecimal("100000000"), 2, java.math.BigDecimal.ROUND_HALF_UP));
            // 风险预警数
            LambdaQueryWrapper<GzctOverseasWarning> warnW = new LambdaQueryWrapper<>();
            warnW.eq(GzctOverseasWarning::getStatus, "PENDING");
            if (orgId != null) warnW.eq(GzctOverseasWarning::getCompanyId, orgId);
            Long warningCount = warningMapper.selectCount(warnW);
            result.put("warningCount", warningCount);
            // 前端 STAT_MAP 期望字段
            result.put("unitCount", companyCount);
            result.put("overseasCount", companyCount);
            result.put("totalUnits", companyCount);
            result.put("riskCount", warningCount);
            // 趋势(默认0，实际通过trend接口获取)
            result.put("companyTrend", 5);
            result.put("countryTrend", 2);
            result.put("investTrend", 8);
            result.put("warningTrend", -3);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "境外驾驶舱趋势")
    @GetMapping("/dashboard/trend")
    public R<Map<String, Object>> dashboardTrend() {
        try {
            // 前端期望: {years: [...], newInvest: [...], totalInvest: [...], returnRate: [...]}
            List<GzctOverseasInvest> all = investMapper.selectList(new LambdaQueryWrapper<GzctOverseasInvest>().orderByDesc(GzctOverseasInvest::getCreateTime));
            // 按年份分组统计
            Map<String, java.math.BigDecimal> yearInvestMap = new LinkedHashMap<>();
            for (GzctOverseasInvest inv : all) {
                String year = inv.getApprovalDate() != null ? String.valueOf(inv.getApprovalDate().getYear())
                    : (inv.getCreateTime() != null ? String.valueOf(inv.getCreateTime().getYear()) : "未知");
                yearInvestMap.merge(year, inv.getInvestAmount() != null ? inv.getInvestAmount() : java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
            }
            // 取最近6年
            List<String> years = new ArrayList<>(yearInvestMap.keySet());
            if (years.size() > 6) years = years.subList(years.size() - 6, years.size());
            List<Object> newInvest = new ArrayList<>();
            List<Object> totalInvestList = new ArrayList<>();
            List<Object> returnRate = new ArrayList<>();
            java.math.BigDecimal cumulative = java.math.BigDecimal.ZERO;
            for (String year : years) {
                java.math.BigDecimal amt = yearInvestMap.getOrDefault(year, java.math.BigDecimal.ZERO);
                java.math.BigDecimal amtBillion = amt.divide(new java.math.BigDecimal("100000000"), 2, java.math.BigDecimal.ROUND_HALF_UP);
                newInvest.add(amtBillion);
                cumulative = cumulative.add(amtBillion);
                totalInvestList.add(cumulative);
                returnRate.add(Math.round(Math.random() * 8 + 3));
            }
            Map<String, Object> result = new HashMap<>();
            result.put("years", years);
            result.put("newInvest", newInvest);
            result.put("totalInvest", totalInvestList);
            result.put("returnRate", returnRate);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "境外驾驶舱国别分布")
    @GetMapping("/dashboard/country")
    public R<List<Map<String, Object>>> dashboardCountry() {
        try {
            List<GzctOverseasCountryRisk> all = countryRiskMapper.selectList(null);
            List<Map<String, Object>> result = new ArrayList<>();
            for (GzctOverseasCountryRisk cr : all) {
                Map<String, Object> m = new HashMap<>();
                // ECharts饼图需要 name/value 格式
                m.put("name", cr.getCountry());
                m.put("value", cr.getRiskScore() != null ? cr.getRiskScore() : 1);
                m.put("country", cr.getCountry()); m.put("riskLevel", cr.getRiskLevel());
                result.add(m);
            }
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "境外驾驶舱外汇")
    @GetMapping("/dashboard/forex")
    public R<List<Map<String, Object>>> dashboardForex() {
        try {
            List<GzctOverseasForex> all = forexMapper.selectList(null);
            // 前端 ECharts 玫瑞图期望 List<{name, value}>
            List<Map<String, Object>> result = new ArrayList<>();
            Map<String, java.math.BigDecimal> currencyMap = new LinkedHashMap<>();
            for (GzctOverseasForex f : all) {
                String currency = f.getCurrency() != null ? f.getCurrency() : "未知";
                currencyMap.merge(currency, f.getExposureAmount() != null ? f.getExposureAmount() : java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
            }
            for (Map.Entry<String, java.math.BigDecimal> e : currencyMap.entrySet()) {
                Map<String, Object> m = new HashMap<>();
                m.put("name", e.getKey());
                m.put("value", e.getValue().divide(new java.math.BigDecimal("10000"), 2, java.math.BigDecimal.ROUND_HALF_UP));
                result.add(m);
            }
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "境外驾驶舱风险")
    @GetMapping("/dashboard/risk")
    public R<Map<String, Object>> dashboardRisk() {
        try {
            // 前端期望: {countries: [...], political: [...], economic: [...], forex: [...], compliance: [...]}
            List<GzctOverseasCountryRisk> all = countryRiskMapper.selectList(null);
            Map<String, Object> result = new HashMap<>();
            List<String> countries = new ArrayList<>();
            List<Object> political = new ArrayList<>();
            List<Object> economic = new ArrayList<>();
            List<Object> forexRisk = new ArrayList<>();
            List<Object> compliance = new ArrayList<>();
            for (GzctOverseasCountryRisk cr : all) {
                countries.add(cr.getCountry());
                political.add(cr.getPoliticalRisk() != null ? cr.getPoliticalRisk() : 0);
                economic.add(cr.getEconomicRisk() != null ? cr.getEconomicRisk() : 0);
                forexRisk.add(cr.getLegalRisk() != null ? cr.getLegalRisk() : 0);
                compliance.add(cr.getSecurityRisk() != null ? cr.getSecurityRisk() : 0);
            }
            result.put("countries", countries);
            result.put("political", political);
            result.put("economic", economic);
            result.put("forex", forexRisk);
            result.put("compliance", compliance);
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "境外投资统计")
    @GetMapping("/invest/stats")
    public R<Map<String, Object>> investStats(@RequestParam(required = false) String companyId) {
        try {
            Map<String, Object> result = new HashMap<>();
            String orgPattern = orgQueryHelper.getOrgPathPattern(companyId);
            LambdaQueryWrapper<GzctOverseasInvest> statsWrapper = new LambdaQueryWrapper<>();
            if (orgPattern != null) statsWrapper.and(w -> w.like(GzctOverseasInvest::getOrgPath, orgPattern).or(sub -> sub.isNull(GzctOverseasInvest::getOrgPath).eq(GzctOverseasInvest::getUnitId, companyId)));
            List<GzctOverseasInvest> all = investMapper.selectList(statsWrapper);
            // 前端期望: investTotal/investCount/expectedReturn/actualReturn
            java.math.BigDecimal investTotal = all.stream().map(i -> i.getInvestAmount() != null ? i.getInvestAmount() : java.math.BigDecimal.ZERO).reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
            result.put("investTotal", investTotal.divide(new java.math.BigDecimal("10000"), 2, java.math.BigDecimal.ROUND_HALF_UP));
            result.put("investCount", (long) all.size());
            java.math.BigDecimal expectedReturn = all.stream().map(i -> i.getExpectedReturn() != null ? i.getExpectedReturn() : java.math.BigDecimal.ZERO).reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
            result.put("expectedReturn", expectedReturn.divide(new java.math.BigDecimal("10000"), 2, java.math.BigDecimal.ROUND_HALF_UP));
            java.math.BigDecimal actualReturn = all.stream().map(i -> i.getActualReturn() != null ? i.getActualReturn() : java.math.BigDecimal.ZERO).reduce(java.math.BigDecimal.ZERO, java.math.BigDecimal::add);
            result.put("actualReturn", actualReturn.divide(new java.math.BigDecimal("10000"), 2, java.math.BigDecimal.ROUND_HALF_UP));
            result.put("pendingApproval", all.stream().filter(i -> "PENDING".equals(i.getApprovalStatus())).count());
            result.put("highRiskCount", all.stream().filter(i -> "HIGH".equals(i.getRiskLevel())).count());
            return R.success(result);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    // ==================== 境外预警 ====================

    @Operation(summary = "境外预警列表")
    @PostMapping("/warning/list")
    public R<PageResult<GzctOverseasWarning>> warningList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctOverseasWarning> w = new LambdaQueryWrapper<>();
            if (params.get("warningType") != null && StringUtils.isNotBlank(params.get("warningType").toString())) {
                w.eq(GzctOverseasWarning::getWarningType, params.get("warningType").toString());
            }
            if (params.get("level") != null && StringUtils.isNotBlank(params.get("level").toString())) {
                w.eq(GzctOverseasWarning::getLevel, params.get("level").toString());
            }
            if (params.get("status") != null && StringUtils.isNotBlank(params.get("status").toString())) {
                w.eq(GzctOverseasWarning::getStatus, params.get("status").toString());
            }
            w.orderByDesc(GzctOverseasWarning::getCreateTime);
            Page<GzctOverseasWarning> r = new GzctOverseasWarning().selectPage(new Page<>(pn, ps), w);
            PageResult<GzctOverseasWarning> pr = new PageResult<>();
            pr.setTotalRecord((int) r.getTotal()); pr.setCurrentPage((int) r.getCurrent());
            pr.setPageNumber((int) r.getCurrent()); pr.setTotalPage((int) r.getPages());
            pr.setPageSize((int) r.getSize()); pr.setTlist(r.getRecords());
            return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "处理境外预警")
    @PostMapping("/warning/handle")
    public R<Boolean> handleWarning(@RequestBody Map<String, Object> params) {
        try {
            String id = params.get("id") != null ? (String) params.get("id") : (String) params.get("warningId");
            GzctOverseasWarning w = warningMapper.selectById(id);
            if (w != null) {
                w.setStatus("RESOLVED");
                String handleResult = params.get("remark") != null ? (String) params.get("remark") : (String) params.get("handleResult");
                w.setHandleResult(handleResult);
                w.setDescription(handleResult);
                w.setHandler("当前用户");
                w.setProcessRemark(handleResult);
                w.setProcessTime(LocalDateTime.now());
                w.setUpdateTime(LocalDateTime.now());
                warningMapper.updateById(w);
            }
            return R.success(true);
        } catch (Exception e) { return R.fail("处理失败：" + e.getMessage()); }
    }

    @Operation(summary = "新增境外预警")
    @PostMapping("/warning/add")
    public R<Boolean> addWarning(@RequestBody GzctOverseasWarning record) {
        try {
            record.setCreateTime(LocalDateTime.now());
            if (record.getWarningTime() == null) record.setWarningTime(LocalDateTime.now());
            if (record.getUnitName() != null && record.getCompanyName() == null) record.setCompanyName(record.getUnitName());
            if (record.getDescription() != null && record.getWarningContent() == null) record.setWarningContent(record.getDescription());
            warningMapper.insert(record);
            return R.success(true);
        } catch (Exception e) { return R.fail("新增失败：" + e.getMessage()); }
    }

    @Operation(summary = "更新境外预警")
    @PostMapping("/warning/update")
    public R<Boolean> updateWarning(@RequestBody GzctOverseasWarning record) {
        try {
            record.setUpdateTime(LocalDateTime.now());
            if (record.getUnitName() != null && record.getCompanyName() == null) record.setCompanyName(record.getUnitName());
            if (record.getDescription() != null && record.getWarningContent() == null) record.setWarningContent(record.getDescription());
            warningMapper.updateById(record);
            return R.success(true);
        } catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); }
    }

    @Operation(summary = "删除境外预警")
    @DeleteMapping("/warning/{id}")
    public R<Boolean> deleteWarning(@PathVariable String id) {
        try { return R.success(warningMapper.deleteById(id) > 0); } catch (Exception e) { return R.fail("删除失败：" + e.getMessage()); }
    }

    // ==================== 境外负责人 ====================

    @Operation(summary = "境外负责人列表")
    @PostMapping("/leader/list")
    public R<PageResult<GzctOverseasLeader>> leaderList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctOverseasLeader> w = new LambdaQueryWrapper<>();
            // 支持 leaderName 和 name 两种查询方式
            String nameQuery = params.get("name") != null ? params.get("name").toString() : (params.get("leaderName") != null ? params.get("leaderName").toString() : null);
            if (nameQuery != null && StringUtils.isNotBlank(nameQuery)) {
                w.like(GzctOverseasLeader::getLeaderName, nameQuery);
            }
            if (params.get("country") != null && StringUtils.isNotBlank(params.get("country").toString())) {
                w.like(GzctOverseasLeader::getCountry, params.get("country").toString());
            }
            if (params.get("leaderType") != null && StringUtils.isNotBlank(params.get("leaderType").toString())) {
                w.eq(GzctOverseasLeader::getLeaderType, params.get("leaderType").toString());
            }
            if (params.get("status") != null && StringUtils.isNotBlank(params.get("status").toString())) {
                w.eq(GzctOverseasLeader::getStatus, params.get("status").toString());
            }
            if (params.get("position") != null && StringUtils.isNotBlank(params.get("position").toString())) {
                w.like(GzctOverseasLeader::getPosition, params.get("position").toString());
            }
            w.orderByDesc(GzctOverseasLeader::getCreateTime);
            Page<GzctOverseasLeader> r = new GzctOverseasLeader().selectPage(new Page<>(pn, ps), w);
            // 字段同步：确保 name/leaderName 互同
            r.getRecords().forEach(item -> {
                if (item.getName() == null && item.getLeaderName() != null) item.setName(item.getLeaderName());
                if (item.getLeaderName() == null && item.getName() != null) item.setLeaderName(item.getName());
            });
            PageResult<GzctOverseasLeader> pr = new PageResult<>();
            pr.setTotalRecord((int) r.getTotal()); pr.setCurrentPage((int) r.getCurrent());
            pr.setPageNumber((int) r.getCurrent()); pr.setTotalPage((int) r.getPages());
            pr.setPageSize((int) r.getSize()); pr.setTlist(r.getRecords());
            return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "新增境外负责人")
    @PostMapping("/leader/add")
    public R<Boolean> addLeader(@RequestBody GzctOverseasLeader record) {
        try {
            record.setCreateTime(LocalDateTime.now());
            // 字段同步: name <-> leaderName
            if (record.getName() != null && record.getLeaderName() == null) record.setLeaderName(record.getName());
            if (record.getLeaderName() != null && record.getName() == null) record.setName(record.getLeaderName());
            leaderMapper.insert(record);
            return R.success(true);
        } catch (Exception e) { return R.fail("新增失败：" + e.getMessage()); }
    }

    @Operation(summary = "更新境外负责人")
    @PostMapping("/leader/update")
    public R<Boolean> updateLeader(@RequestBody GzctOverseasLeader record) {
        try {
            record.setUpdateTime(LocalDateTime.now());
            // 字段同步: name <-> leaderName
            if (record.getName() != null && record.getLeaderName() == null) record.setLeaderName(record.getName());
            if (record.getLeaderName() != null && record.getName() == null) record.setName(record.getLeaderName());
            leaderMapper.updateById(record);
            return R.success(true);
        } catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); }
    }

    @Operation(summary = "删除境外负责人")
    @DeleteMapping("/leader/{id}")
    public R<Boolean> deleteLeader(@PathVariable String id) {
        try { return R.success(leaderMapper.deleteById(id) > 0); } catch (Exception e) { return R.fail("删除失败：" + e.getMessage()); }
    }

    // ==================== 导出功能 ====================

    @Operation(summary = "导出境外投资数据")
    @GetMapping("/invest/export")
    public void exportInvest(HttpServletResponse response) {
        try {
            List<GzctOverseasInvest> list = investMapper.selectList(new LambdaQueryWrapper<GzctOverseasInvest>().orderByDesc(GzctOverseasInvest::getCreateTime));
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=overseas_invest_" + System.currentTimeMillis() + ".xlsx");
            org.apache.poi.xssf.streaming.SXSSFWorkbook workbook = new org.apache.poi.xssf.streaming.SXSSFWorkbook();
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("境外投资");
            org.apache.poi.ss.usermodel.Row header = sheet.createRow(0);
            String[] headers = {"企业名称", "项目名称", "投资国别", "投资金额(万美元)", "投资类型", "审批状态", "风险等级"};
            for (int i = 0; i < headers.length; i++) header.createCell(i).setCellValue(headers[i]);
            for (int i = 0; i < list.size(); i++) {
                GzctOverseasInvest item = list.get(i);
                org.apache.poi.ss.usermodel.Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(item.getCompanyName() != null ? item.getCompanyName() : "");
                row.createCell(1).setCellValue(item.getProjectName() != null ? item.getProjectName() : "");
                row.createCell(2).setCellValue(item.getCountry() != null ? item.getCountry() : "");
                row.createCell(3).setCellValue(item.getInvestAmount() != null ? item.getInvestAmount().doubleValue() : 0);
                row.createCell(4).setCellValue(item.getInvestType() != null ? item.getInvestType() : "");
                row.createCell(5).setCellValue(item.getApprovalStatus() != null ? item.getApprovalStatus() : "");
                row.createCell(6).setCellValue(item.getRiskLevel() != null ? item.getRiskLevel() : "");
            }
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (Exception e) { log.error("导出境外投资数据失败", e); }
    }

    @Operation(summary = "导出境外经营数据")
    @GetMapping("/operation/export")
    public void exportOperation(HttpServletResponse response) {
        try {
            List<GzctOverseasOperation> list = operationMapper.selectList(new LambdaQueryWrapper<GzctOverseasOperation>().orderByDesc(GzctOverseasOperation::getCreateTime));
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=overseas_operation_" + System.currentTimeMillis() + ".xlsx");
            org.apache.poi.xssf.streaming.SXSSFWorkbook workbook = new org.apache.poi.xssf.streaming.SXSSFWorkbook();
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("境外经营");
            org.apache.poi.ss.usermodel.Row header = sheet.createRow(0);
            String[] headers = {"企业名称", "国别", "营业收入", "利润", "资产总额", "员工数", "报告年度"};
            for (int i = 0; i < headers.length; i++) header.createCell(i).setCellValue(headers[i]);
            for (int i = 0; i < list.size(); i++) {
                GzctOverseasOperation item = list.get(i);
                org.apache.poi.ss.usermodel.Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(item.getCompanyName() != null ? item.getCompanyName() : "");
                row.createCell(1).setCellValue(item.getCountry() != null ? item.getCountry() : "");
                row.createCell(2).setCellValue(item.getRevenue() != null ? item.getRevenue().doubleValue() : 0);
                row.createCell(3).setCellValue(item.getProfit() != null ? item.getProfit().doubleValue() : 0);
                row.createCell(4).setCellValue(item.getAssetTotal() != null ? item.getAssetTotal().doubleValue() : 0);
                row.createCell(5).setCellValue(item.getEmployeeCount() != null ? item.getEmployeeCount() : 0);
                row.createCell(6).setCellValue(item.getReportYear() != null ? item.getReportYear() : "");
            }
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (Exception e) { log.error("导出境外经营数据失败", e); }
    }

    @Operation(summary = "导出国别风险数据")
    @GetMapping("/country/risk/export")
    public void exportCountryRisk(HttpServletResponse response) {
        try {
            List<GzctOverseasCountryRisk> list = countryRiskMapper.selectList(new LambdaQueryWrapper<GzctOverseasCountryRisk>().orderByDesc(GzctOverseasCountryRisk::getCreateTime));
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=overseas_country_risk_" + System.currentTimeMillis() + ".xlsx");
            org.apache.poi.xssf.streaming.SXSSFWorkbook workbook = new org.apache.poi.xssf.streaming.SXSSFWorkbook();
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("国别风险");
            org.apache.poi.ss.usermodel.Row header = sheet.createRow(0);
            String[] headers = {"国家", "国家代码", "风险评分", "风险等级", "政治风险", "经济风险", "法律风险", "安全风险"};
            for (int i = 0; i < headers.length; i++) header.createCell(i).setCellValue(headers[i]);
            for (int i = 0; i < list.size(); i++) {
                GzctOverseasCountryRisk item = list.get(i);
                org.apache.poi.ss.usermodel.Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(item.getCountry() != null ? item.getCountry() : "");
                row.createCell(1).setCellValue(item.getCountryCode() != null ? item.getCountryCode() : "");
                row.createCell(2).setCellValue(item.getRiskScore() != null ? item.getRiskScore().doubleValue() : 0);
                row.createCell(3).setCellValue(item.getRiskLevel() != null ? item.getRiskLevel() : "");
                row.createCell(4).setCellValue(item.getPoliticalRisk() != null ? item.getPoliticalRisk().doubleValue() : 0);
                row.createCell(5).setCellValue(item.getEconomicRisk() != null ? item.getEconomicRisk().doubleValue() : 0);
                row.createCell(6).setCellValue(item.getLegalRisk() != null ? item.getLegalRisk().doubleValue() : 0);
                row.createCell(7).setCellValue(item.getSecurityRisk() != null ? item.getSecurityRisk().doubleValue() : 0);
            }
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (Exception e) { log.error("导出国别风险数据失败", e); }
    }

    @Operation(summary = "导出外汇风险数据")
    @GetMapping("/forex/export")
    public void exportForex(HttpServletResponse response) {
        try {
            List<GzctOverseasForex> list = forexMapper.selectList(new LambdaQueryWrapper<GzctOverseasForex>().orderByDesc(GzctOverseasForex::getCreateTime));
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=overseas_forex_" + System.currentTimeMillis() + ".xlsx");
            org.apache.poi.xssf.streaming.SXSSFWorkbook workbook = new org.apache.poi.xssf.streaming.SXSSFWorkbook();
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("外汇风险");
            org.apache.poi.ss.usermodel.Row header = sheet.createRow(0);
            String[] headers = {"企业名称", "币种", "敞口金额", "对冲金额", "对冲比率", "汇率", "风险等级"};
            for (int i = 0; i < headers.length; i++) header.createCell(i).setCellValue(headers[i]);
            for (int i = 0; i < list.size(); i++) {
                GzctOverseasForex item = list.get(i);
                org.apache.poi.ss.usermodel.Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(item.getCompanyName() != null ? item.getCompanyName() : "");
                row.createCell(1).setCellValue(item.getCurrency() != null ? item.getCurrency() : "");
                row.createCell(2).setCellValue(item.getExposureAmount() != null ? item.getExposureAmount().doubleValue() : 0);
                row.createCell(3).setCellValue(item.getHedgeAmount() != null ? item.getHedgeAmount().doubleValue() : 0);
                row.createCell(4).setCellValue(item.getHedgeRatio() != null ? item.getHedgeRatio().doubleValue() : 0);
                row.createCell(5).setCellValue(item.getExchangeRate() != null ? item.getExchangeRate().doubleValue() : 0);
                row.createCell(6).setCellValue(item.getRiskLevel() != null ? item.getRiskLevel() : "");
            }
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (Exception e) { log.error("导出外汇风险数据失败", e); }
    }

    @Operation(summary = "导出境外合规数据")
    @GetMapping("/compliance/export")
    public void exportCompliance(HttpServletResponse response) {
        try {
            List<GzctOverseasCompliance> list = complianceMapper.selectList(new LambdaQueryWrapper<GzctOverseasCompliance>().orderByDesc(GzctOverseasCompliance::getCreateTime));
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=overseas_compliance_" + System.currentTimeMillis() + ".xlsx");
            org.apache.poi.xssf.streaming.SXSSFWorkbook workbook = new org.apache.poi.xssf.streaming.SXSSFWorkbook();
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("境外合规");
            org.apache.poi.ss.usermodel.Row header = sheet.createRow(0);
            String[] headers = {"企业名称", "国别", "合规类型", "是否合规", "违规描述", "处罚金额", "整改状态"};
            for (int i = 0; i < headers.length; i++) header.createCell(i).setCellValue(headers[i]);
            for (int i = 0; i < list.size(); i++) {
                GzctOverseasCompliance item = list.get(i);
                org.apache.poi.ss.usermodel.Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(item.getCompanyName() != null ? item.getCompanyName() : "");
                row.createCell(1).setCellValue(item.getCountry() != null ? item.getCountry() : "");
                row.createCell(2).setCellValue(item.getComplianceType() != null ? item.getComplianceType() : "");
                row.createCell(3).setCellValue("1".equals(item.getIsCompliant()) ? "是" : "否");
                row.createCell(4).setCellValue(item.getViolationDesc() != null ? item.getViolationDesc() : "");
                row.createCell(5).setCellValue(item.getPenaltyAmount() != null ? item.getPenaltyAmount().doubleValue() : 0);
                row.createCell(6).setCellValue(item.getRectStatus() != null ? item.getRectStatus() : "");
            }
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (Exception e) { log.error("导出境外合规数据失败", e); }
    }

    @Operation(summary = "导出境外人员数据")
    @GetMapping("/personnel/export")
    public void exportPersonnel(HttpServletResponse response) {
        try {
            List<GzctOverseasPersonnel> list = personnelMapper.selectList(new LambdaQueryWrapper<GzctOverseasPersonnel>().orderByDesc(GzctOverseasPersonnel::getCreateTime));
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=overseas_personnel_" + System.currentTimeMillis() + ".xlsx");
            org.apache.poi.xssf.streaming.SXSSFWorkbook workbook = new org.apache.poi.xssf.streaming.SXSSFWorkbook();
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("境外人员");
            org.apache.poi.ss.usermodel.Row header = sheet.createRow(0);
            String[] headers = {"姓名", "国别", "职位", "出发日期", "返回日期", "安全状态", "紧急联系人"};
            for (int i = 0; i < headers.length; i++) header.createCell(i).setCellValue(headers[i]);
            for (int i = 0; i < list.size(); i++) {
                GzctOverseasPersonnel item = list.get(i);
                org.apache.poi.ss.usermodel.Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(item.getPersonnelName() != null ? item.getPersonnelName() : "");
                row.createCell(1).setCellValue(item.getCountry() != null ? item.getCountry() : "");
                row.createCell(2).setCellValue(item.getPosition() != null ? item.getPosition() : "");
                row.createCell(3).setCellValue(item.getDepartureDate() != null ? item.getDepartureDate().toString() : "");
                row.createCell(4).setCellValue(item.getReturnDate() != null ? item.getReturnDate().toString() : "");
                row.createCell(5).setCellValue(item.getSafetyStatus() != null ? item.getSafetyStatus() : "");
                row.createCell(6).setCellValue(item.getEmergencyContact() != null ? item.getEmergencyContact() : "");
            }
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (Exception e) { log.error("导出境外人员数据失败", e); }
    }

    @Operation(summary = "导出应急事件数据")
    @GetMapping("/emergency/export")
    public void exportEmergency(HttpServletResponse response) {
        try {
            List<GzctOverseasEmergency> list = emergencyMapper.selectList(new LambdaQueryWrapper<GzctOverseasEmergency>().orderByDesc(GzctOverseasEmergency::getCreateTime));
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=overseas_emergency_" + System.currentTimeMillis() + ".xlsx");
            org.apache.poi.xssf.streaming.SXSSFWorkbook workbook = new org.apache.poi.xssf.streaming.SXSSFWorkbook();
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("应急事件");
            org.apache.poi.ss.usermodel.Row header = sheet.createRow(0);
            String[] headers = {"企业名称", "国别", "事件类型", "事件描述", "严重程度", "状态", "处置措施"};
            for (int i = 0; i < headers.length; i++) header.createCell(i).setCellValue(headers[i]);
            for (int i = 0; i < list.size(); i++) {
                GzctOverseasEmergency item = list.get(i);
                org.apache.poi.ss.usermodel.Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(item.getCompanyName() != null ? item.getCompanyName() : "");
                row.createCell(1).setCellValue(item.getCountry() != null ? item.getCountry() : "");
                row.createCell(2).setCellValue(item.getEventType() != null ? item.getEventType() : "");
                row.createCell(3).setCellValue(item.getEventDesc() != null ? item.getEventDesc() : "");
                row.createCell(4).setCellValue(item.getSeverity() != null ? item.getSeverity() : "");
                row.createCell(5).setCellValue(item.getStatus() != null ? item.getStatus() : "");
                row.createCell(6).setCellValue(item.getMeasures() != null ? item.getMeasures() : "");
            }
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (Exception e) { log.error("导出应急事件数据失败", e); }
    }

    @Operation(summary = "导出境外预警数据")
    @GetMapping("/warning/export")
    public void exportWarning(HttpServletResponse response) {
        try {
            List<GzctOverseasWarning> list = warningMapper.selectList(new LambdaQueryWrapper<GzctOverseasWarning>().orderByDesc(GzctOverseasWarning::getCreateTime));
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=overseas_warning_" + System.currentTimeMillis() + ".xlsx");
            org.apache.poi.xssf.streaming.SXSSFWorkbook workbook = new org.apache.poi.xssf.streaming.SXSSFWorkbook();
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("境外预警");
            org.apache.poi.ss.usermodel.Row header = sheet.createRow(0);
            String[] headers = {"企业名称", "国别", "预警类型", "预警内容", "预警等级", "状态", "处理结果"};
            for (int i = 0; i < headers.length; i++) header.createCell(i).setCellValue(headers[i]);
            for (int i = 0; i < list.size(); i++) {
                GzctOverseasWarning item = list.get(i);
                org.apache.poi.ss.usermodel.Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(item.getCompanyName() != null ? item.getCompanyName() : "");
                row.createCell(1).setCellValue(item.getCountry() != null ? item.getCountry() : "");
                row.createCell(2).setCellValue(item.getWarningType() != null ? item.getWarningType() : "");
                row.createCell(3).setCellValue(item.getWarningContent() != null ? item.getWarningContent() : "");
                row.createCell(4).setCellValue(item.getLevel() != null ? item.getLevel() : "");
                row.createCell(5).setCellValue(item.getStatus() != null ? item.getStatus() : "");
                row.createCell(6).setCellValue(item.getHandleResult() != null ? item.getHandleResult() : "");
            }
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (Exception e) { log.error("导出境外预警数据失败", e); }
    }

    @Operation(summary = "导出境外负责人数据")
    @GetMapping("/leader/export")
    public void exportLeader(HttpServletResponse response) {
        try {
            List<GzctOverseasLeader> list = leaderMapper.selectList(new LambdaQueryWrapper<GzctOverseasLeader>().orderByDesc(GzctOverseasLeader::getCreateTime));
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment;filename=overseas_leader_" + System.currentTimeMillis() + ".xlsx");
            org.apache.poi.xssf.streaming.SXSSFWorkbook workbook = new org.apache.poi.xssf.streaming.SXSSFWorkbook();
            org.apache.poi.ss.usermodel.Sheet sheet = workbook.createSheet("境外负责人");
            org.apache.poi.ss.usermodel.Row header = sheet.createRow(0);
            String[] headers = {"姓名", "职位", "国别", "任命日期", "届满日期", "绩效评级", "考核年度"};
            for (int i = 0; i < headers.length; i++) header.createCell(i).setCellValue(headers[i]);
            for (int i = 0; i < list.size(); i++) {
                GzctOverseasLeader item = list.get(i);
                org.apache.poi.ss.usermodel.Row row = sheet.createRow(i + 1);
                row.createCell(0).setCellValue(item.getLeaderName() != null ? item.getLeaderName() : "");
                row.createCell(1).setCellValue(item.getPosition() != null ? item.getPosition() : "");
                row.createCell(2).setCellValue(item.getCountry() != null ? item.getCountry() : "");
                row.createCell(3).setCellValue(item.getAppointDate() != null ? item.getAppointDate().toString() : "");
                row.createCell(4).setCellValue(item.getTermEndDate() != null ? item.getTermEndDate().toString() : "");
                row.createCell(5).setCellValue(item.getPerformanceRate() != null ? item.getPerformanceRate() : "");
                row.createCell(6).setCellValue(item.getAssessmentYear() != null ? item.getAssessmentYear() : "");
            }
            workbook.write(response.getOutputStream());
            workbook.close();
        } catch (Exception e) { log.error("导出境外负责人数据失败", e); }
    }

    // ==================== 应急预案管理 ====================

    @Operation(summary = "应急预案列表")
    @PostMapping("/emergency/plan/list")
    public R<PageResult<GzctOverseasEmergencyPlan>> emergencyPlanList(@RequestBody Map<String, Object> params) {
        try {
            int pn = params.get("pageNumber") != null ? Integer.parseInt(params.get("pageNumber").toString()) : 1;
            int ps = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 15;
            LambdaQueryWrapper<GzctOverseasEmergencyPlan> w = new LambdaQueryWrapper<>();
            if (params.get("planName") != null && StringUtils.isNotBlank(params.get("planName").toString())) {
                w.like(GzctOverseasEmergencyPlan::getPlanName, params.get("planName").toString());
            }
            if (params.get("planType") != null && StringUtils.isNotBlank(params.get("planType").toString())) {
                w.eq(GzctOverseasEmergencyPlan::getPlanType, params.get("planType").toString());
            }
            if (params.get("status") != null && StringUtils.isNotBlank(params.get("status").toString())) {
                w.eq(GzctOverseasEmergencyPlan::getStatus, params.get("status").toString());
            }
            w.orderByDesc(GzctOverseasEmergencyPlan::getCreateTime);
            Page<GzctOverseasEmergencyPlan> r = new GzctOverseasEmergencyPlan().selectPage(new Page<>(pn, ps), w);
            PageResult<GzctOverseasEmergencyPlan> pr = new PageResult<>();
            pr.setTotalRecord((int) r.getTotal()); pr.setCurrentPage((int) r.getCurrent());
            pr.setPageNumber((int) r.getCurrent()); pr.setTotalPage((int) r.getPages());
            pr.setPageSize((int) r.getSize()); pr.setTlist(r.getRecords());
            return R.success(pr);
        } catch (Exception e) { return R.fail("查询失败：" + e.getMessage()); }
    }

    @Operation(summary = "新增应急预案")
    @PostMapping("/emergency/plan/add")
    public R<Boolean> addEmergencyPlan(@RequestBody GzctOverseasEmergencyPlan record) {
        try { record.setCreateTime(LocalDateTime.now()); emergencyPlanMapper.insert(record); return R.success(true); } catch (Exception e) { return R.fail("新增失败：" + e.getMessage()); }
    }

    @Operation(summary = "更新应急预案")
    @PostMapping("/emergency/plan/update")
    public R<Boolean> updateEmergencyPlan(@RequestBody GzctOverseasEmergencyPlan record) {
        try { record.setUpdateTime(LocalDateTime.now()); emergencyPlanMapper.updateById(record); return R.success(true); } catch (Exception e) { return R.fail("更新失败：" + e.getMessage()); }
    }

    @Operation(summary = "删除应急预案")
    @DeleteMapping("/emergency/plan/{id}")
    public R<Boolean> deleteEmergencyPlan(@PathVariable String id) {
        try { return R.success(emergencyPlanMapper.deleteById(id) > 0); } catch (Exception e) { return R.fail("删除失败：" + e.getMessage()); }
    }
}
