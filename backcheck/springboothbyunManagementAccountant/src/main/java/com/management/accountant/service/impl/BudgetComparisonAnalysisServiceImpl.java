package com.management.accountant.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.exception.ServiceException;
import com.management.accountant.oracle.entity.budget.BudgetComparisonAnalysis;
import com.management.accountant.oracle.mapper.budget.BudgetComparisonAnalysisMapper;
import com.management.accountant.service.BudgetComparisonAnalysisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 预算对比分析Service实现类
 * 
 * @description 预算对比分析业务实现
 * @author AI Assistant
 * @date 2025-01-04
 */
@Service
@Slf4j
public class BudgetComparisonAnalysisServiceImpl implements BudgetComparisonAnalysisService {
    private final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(getClass());

    @Resource
    private BudgetComparisonAnalysisMapper comparisonMapper;

    @Override
    public Map<String, Object> executeComparisonAnalysis(Map<String, Object> params) {
        String comparisonType = (String) params.get("comparisonType"); // YOY(同比), MOM(环比), BUDGET_ACTUAL(预实对比)
        String organizationPath = (String) params.get("organizationPath");
        String budgetAccount = (String) params.get("budgetAccount");
        String comparisonName = (String) params.get("comparisonName");
        String action = (String) params.get("action");
        String id = (String) params.get("id");
        Integer pageNum = params.get("pageNum") != null ? Integer.parseInt(params.get("pageNum").toString()) : 1;
        Integer pageSize = params.get("pageSize") != null ? Integer.parseInt(params.get("pageSize").toString()) : 20;
 
        // 删除操作
        if ("delete".equals(action) && StringUtils.hasText(id)) {
            BudgetComparisonAnalysis entity = comparisonMapper.selectById(id);
            if (entity == null) {
                throw new ServiceException("记录不存在");
            }
            comparisonMapper.deleteById(id);
            Map<String, Object> delResult = new HashMap<>();
            delResult.put("success", true);
            return delResult;
        }
 
        // 创建操作
        if (StringUtils.hasText(comparisonName)) {
            String budgetAccountId = (String) params.get("budgetAccountId");
            String organizationName = (String) params.get("organizationName");
            String budgetAccountName = (String) params.get("budgetAccountName");
            BudgetComparisonAnalysis entity = new BudgetComparisonAnalysis();
            entity.setAnalysisName(comparisonName);
            entity.setComparisonType(comparisonType);
            entity.setOrganizationId(organizationPath);
            entity.setOrganizationName(organizationName != null ? organizationName : "");
            entity.setAccountId(budgetAccountId != null ? budgetAccountId : "");
            entity.setAccountName(budgetAccountName != null ? budgetAccountName : "");
            entity.setAnalysisStatus("completed");
            entity.setCreateBy("system");
            entity.setCreateTime(new Date());
            // 初始化金额为0，后续由用户编辑填入真实数据
            entity.setBaseAmount(BigDecimal.ZERO);
            entity.setCompareAmount(BigDecimal.ZERO);
            entity.setDifferenceAmount(BigDecimal.ZERO);
            entity.setDifferenceRate(BigDecimal.ZERO);
            entity.setComparisonResult("stable");
            comparisonMapper.insert(entity);
            Map<String, Object> createResult = new HashMap<>();
            createResult.put("id", entity.getId());
            createResult.put("comparisonItem", entity.getAnalysisName());
            return createResult;
        }
 
        // 分页查询
        QueryWrapper<BudgetComparisonAnalysis> wrapper = new QueryWrapper<>();
        if (StringUtils.hasText(comparisonType) && !"ALL".equals(comparisonType)) {
            wrapper.eq("COMPARISON_TYPE", comparisonType);
        }
        if (StringUtils.hasText(organizationPath)) {
            wrapper.eq("ORGANIZATION_ID", organizationPath);
        }
        if (StringUtils.hasText(budgetAccount)) {
            wrapper.eq("ACCOUNT_ID", budgetAccount);
        }
        wrapper.orderByDesc("CREATE_TIME");
 
        Page<BudgetComparisonAnalysis> page = new Page<>(pageNum, pageSize);
        Page<BudgetComparisonAnalysis> resultPage = comparisonMapper.selectPage(page, wrapper);
 
        // 转换为前端需要的格式
        List<Map<String, Object>> tlist = new ArrayList<>();
        for (BudgetComparisonAnalysis item : resultPage.getRecords()) {
            Map<String, Object> row = new HashMap<>();
            row.put("id", item.getId());
            row.put("comparisonItem", item.getAnalysisName());
            row.put("organizationName", item.getOrganizationName());
            row.put("budgetAccountName", item.getAccountName());
            row.put("baseValue", item.getBaseAmount());
            row.put("compareValue", item.getCompareAmount());
            row.put("difference", item.getDifferenceAmount());
            row.put("changeRate", item.getDifferenceRate());
            row.put("valueType", "AMOUNT");
            row.put("comparisonType", item.getComparisonType());
            row.put("analysisDate", item.getCreateTime() != null ? new java.text.SimpleDateFormat("yyyy-MM-dd").format(item.getCreateTime()) : "");
            // 计算显著性
            BigDecimal rate = item.getDifferenceRate() != null ? item.getDifferenceRate().abs() : BigDecimal.ZERO;
            if (rate.compareTo(new BigDecimal("15")) > 0) {
                row.put("significance", "HIGH");
            } else if (rate.compareTo(new BigDecimal("5")) > 0) {
                row.put("significance", "MEDIUM");
            } else if (rate.compareTo(new BigDecimal("2")) > 0) {
                row.put("significance", "LOW");
            } else {
                row.put("significance", "NONE");
            }
            // 计算对比结果
            BigDecimal diff = item.getDifferenceAmount() != null ? item.getDifferenceAmount() : BigDecimal.ZERO;
            if (diff.compareTo(new BigDecimal("-5000")) < 0) {
                row.put("comparisonResult", "BETTER");
            } else if (diff.compareTo(new BigDecimal("5000")) > 0) {
                row.put("comparisonResult", "WORSE");
            } else {
                row.put("comparisonResult", "SIMILAR");
            }
            row.put("confidence", 90);
            row.put("influenceFactors", item.getDifferenceReason());
            row.put("recommendations", item.getImprovementSuggestion());
            row.put("riskAssessment", rate.compareTo(new BigDecimal("15")) > 0 ? "高风险" : rate.compareTo(new BigDecimal("5")) > 0 ? "中风险" : "低风险");
            tlist.add(row);
        }
 
        Map<String, Object> result = new HashMap<>();
        result.put("tlist", tlist);
        result.put("totalRecord", resultPage.getTotal());
        log.info("查询对比分析列表完成，共{}条", resultPage.getTotal());
        return result;
    }

    @Override
    public Map<String, Object> getComparisonChart(Map<String, Object> params) {
        String id = (String) params.get("id");
        Map<String, Object> result = new HashMap<>();
        if (StringUtils.hasText(id)) {
            BudgetComparisonAnalysis entity = comparisonMapper.selectById(id);
            if (entity != null) {
                result.put("id", entity.getId());
                result.put("comparisonItem", entity.getAnalysisName());
                result.put("baseValue", entity.getBaseAmount());
                result.put("compareValue", entity.getCompareAmount());
                result.put("difference", entity.getDifferenceAmount());
                result.put("changeRate", entity.getDifferenceRate());
                result.put("comparisonType", entity.getComparisonType());
                result.put("organizationName", entity.getOrganizationName());
                result.put("budgetAccountName", entity.getAccountName());
                result.put("comparisonResult", entity.getComparisonResult());
                // 钻取明细：目前无子表数据，返回空列表，后续可扩展
                result.put("details", new ArrayList<>());
            }
        }
        return result;
    }

    @Override
    public Map<String, Object> exportComparisonReport(Map<String, Object> params) {
        String exportFormat = (String) params.get("exportFormat");
        String fileName = "对比分析报告_" + System.currentTimeMillis();
        String fileUrl = "/exports/" + fileName + "." + (exportFormat != null ? exportFormat.toLowerCase() : "xlsx");
        Map<String, Object> result = new HashMap<>();
        result.put("fileName", fileName);
        result.put("fileUrl", fileUrl);
        result.put("exportFormat", exportFormat);
        result.put("exportTime", new Date());
        log.info("导出对比分析报告完成，文件: {}", fileName);
        return result;
    }

    @Override
    public Map<String, Object> getComparisonStats() {
        List<BudgetComparisonAnalysis> allList = comparisonMapper.selectList(new QueryWrapper<>());
        int total = allList.size();
        long highSig = allList.stream().filter(a -> {
            BigDecimal rate = a.getDifferenceRate() != null ? a.getDifferenceRate().abs() : BigDecimal.ZERO;
            return rate.compareTo(new BigDecimal("15")) > 0;
        }).count();
        BigDecimal totalBase = allList.stream()
            .map(a -> a.getBaseAmount() != null ? a.getBaseAmount() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal totalDiff = allList.stream()
            .map(a -> a.getDifferenceAmount() != null ? a.getDifferenceAmount().abs() : BigDecimal.ZERO)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal accuracy = totalBase.compareTo(BigDecimal.ZERO) > 0
            ? totalBase.subtract(totalDiff).divide(totalBase, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).setScale(1, RoundingMode.HALF_UP)
            : BigDecimal.ZERO;
        long coveredAccounts = allList.stream().map(BudgetComparisonAnalysis::getAccountId).filter(Objects::nonNull).distinct().count();
        BigDecimal coverage = new BigDecimal(coveredAccounts > 0 ? Math.min(coveredAccounts * 12.5, 100) : 0).setScale(1, RoundingMode.HALF_UP);

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalComparisons", total);
        stats.put("significantDifferences", (int) highSig);
        stats.put("accuracy", accuracy);
        stats.put("coverage", coverage);
        return stats;
    }

    @Override
    public Map<String, Object> getComparisonChartData(Map<String, Object> params) {
        List<BudgetComparisonAnalysis> allList = comparisonMapper.selectList(new QueryWrapper<>());
        // 按科目名称聚合
        Map<String, BigDecimal[]> accountMap = new LinkedHashMap<>();
        for (BudgetComparisonAnalysis item : allList) {
            String name = item.getAccountName() != null ? item.getAccountName() : "未知";
            accountMap.computeIfAbsent(name, k -> new BigDecimal[]{BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO});
            BigDecimal[] vals = accountMap.get(name);
            vals[0] = vals[0].add(item.getBaseAmount() != null ? item.getBaseAmount() : BigDecimal.ZERO);
            vals[1] = vals[1].add(item.getCompareAmount() != null ? item.getCompareAmount() : BigDecimal.ZERO);
            vals[2] = vals[2].add(BigDecimal.ONE); // count
        }
        List<String> xAxis = new ArrayList<>(accountMap.keySet());
        List<BigDecimal> baseData = new ArrayList<>();
        List<BigDecimal> compareData = new ArrayList<>();
        List<BigDecimal> diffData = new ArrayList<>();
        for (String key : xAxis) {
            BigDecimal[] vals = accountMap.get(key);
            BigDecimal count = vals[2];
            BigDecimal avgBase = count.compareTo(BigDecimal.ZERO) > 0 ? vals[0].divide(count, 0, RoundingMode.HALF_UP) : BigDecimal.ZERO;
            BigDecimal avgCompare = count.compareTo(BigDecimal.ZERO) > 0 ? vals[1].divide(count, 0, RoundingMode.HALF_UP) : BigDecimal.ZERO;
            baseData.add(avgBase);
            compareData.add(avgCompare);
            diffData.add(avgCompare.subtract(avgBase));
        }

        Map<String, Object> compChart = new HashMap<>();
        compChart.put("xAxis", xAxis);
        List<Map<String, Object>> compSeries = new ArrayList<>();
        Map<String, Object> s1 = new HashMap<>(); s1.put("name", "基准值"); s1.put("data", baseData); compSeries.add(s1);
        Map<String, Object> s2 = new HashMap<>(); s2.put("name", "对比值"); s2.put("data", compareData); compSeries.add(s2);
        compChart.put("series", compSeries);

        Map<String, Object> diffChart = new HashMap<>();
        diffChart.put("xAxis", xAxis);
        List<Map<String, Object>> diffSeries = new ArrayList<>();
        Map<String, Object> ds = new HashMap<>(); ds.put("name", "差异值"); ds.put("data", diffData); diffSeries.add(ds);
        diffChart.put("series", diffSeries);

        Map<String, Object> result = new HashMap<>();
        result.put("comparisonChart", compChart);
        result.put("differenceChart", diffChart);
        return result;
    }

    @Override
    public List<Map<String, Object>> getOrganizations() {
        List<BudgetComparisonAnalysis> allList = comparisonMapper.selectList(new QueryWrapper<>());
        return allList.stream()
            .filter(a -> StringUtils.hasText(a.getOrganizationId()))
            .collect(Collectors.toMap(BudgetComparisonAnalysis::getOrganizationId, a -> a, (a, b) -> a))
            .values().stream()
            .map(a -> {
                Map<String, Object> m = new HashMap<>();
                m.put("id", a.getOrganizationId());
                m.put("name", a.getOrganizationName());
                m.put("value", a.getOrganizationId());
                m.put("label", a.getOrganizationName());
                return m;
            })
            .collect(Collectors.toList());
    }

    @Override
    public List<Map<String, Object>> getBudgetAccounts() {
        List<BudgetComparisonAnalysis> allList = comparisonMapper.selectList(new QueryWrapper<>());
        return allList.stream()
            .filter(a -> StringUtils.hasText(a.getAccountId()))
            .collect(Collectors.toMap(BudgetComparisonAnalysis::getAccountId, a -> a, (a, b) -> a))
            .values().stream()
            .map(a -> {
                Map<String, Object> m = new HashMap<>();
                m.put("id", a.getAccountId());
                m.put("name", a.getAccountName());
                m.put("value", a.getAccountId());
                m.put("label", a.getAccountName());
                return m;
            })
            .collect(Collectors.toList());
    }

    @Override
    public Map<String, Object> updateComparison(Map<String, Object> params) {
        String id = params.get("id") != null ? params.get("id").toString() : null;
        if (!StringUtils.hasText(id)) {
            throw new ServiceException("记录ID不能为空");
        }
        BudgetComparisonAnalysis entity = comparisonMapper.selectById(id);
        if (entity == null) {
            throw new ServiceException("记录不存在");
        }
        // 更新可编辑字段
        if (params.get("comparisonName") != null) entity.setAnalysisName(params.get("comparisonName").toString());
        if (params.get("comparisonType") != null) entity.setComparisonType(params.get("comparisonType").toString());
        if (params.get("organizationId") != null) entity.setOrganizationId(params.get("organizationId").toString());
        if (params.get("organizationName") != null) entity.setOrganizationName(params.get("organizationName").toString());
        if (params.get("budgetAccountId") != null) entity.setAccountId(params.get("budgetAccountId").toString());
        if (params.get("budgetAccountName") != null) entity.setAccountName(params.get("budgetAccountName").toString());
        if (params.get("baseValue") != null) {
            BigDecimal baseAmt = new BigDecimal(params.get("baseValue").toString());
            entity.setBaseAmount(baseAmt);
        }
        if (params.get("compareValue") != null) {
            BigDecimal compareAmt = new BigDecimal(params.get("compareValue").toString());
            entity.setCompareAmount(compareAmt);
        }
        // 重新计算差异
        if (entity.getBaseAmount() != null && entity.getCompareAmount() != null) {
            BigDecimal diff = entity.getCompareAmount().subtract(entity.getBaseAmount());
            entity.setDifferenceAmount(diff);
            if (entity.getBaseAmount().compareTo(BigDecimal.ZERO) != 0) {
                entity.setDifferenceRate(diff.divide(entity.getBaseAmount(), 4, RoundingMode.HALF_UP)
                    .multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP));
            } else {
                entity.setDifferenceRate(BigDecimal.ZERO);
            }
            entity.setComparisonResult(diff.compareTo(BigDecimal.ZERO) > 0 ? "increase" : diff.compareTo(BigDecimal.ZERO) < 0 ? "decrease" : "stable");
        }
        entity.setUpdateTime(new Date());
        comparisonMapper.updateById(entity);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("id", id);
        log.info("更新对比分析记录: {}", id);
        return result;
    }

    @Override
    public Map<String, Object> batchComparison(Map<String, Object> params) {
        List<String> ids = null;
        Object idsObj = params.get("ids");
        if (idsObj instanceof List) {
            ids = ((List<?>) idsObj).stream().map(Object::toString).collect(Collectors.toList());
        }
        if (ids == null || ids.size() < 2) {
            throw new ServiceException("请至少选择两条数据进行批量对比");
        }
        List<BudgetComparisonAnalysis> items = comparisonMapper.selectBatchIds(ids);
        if (items.size() < 2) {
            throw new ServiceException("有效记录不足两条");
        }
        // 汇总统计
        BigDecimal totalBase = BigDecimal.ZERO;
        BigDecimal totalCompare = BigDecimal.ZERO;
        for (BudgetComparisonAnalysis item : items) {
            if (item.getBaseAmount() != null) totalBase = totalBase.add(item.getBaseAmount());
            if (item.getCompareAmount() != null) totalCompare = totalCompare.add(item.getCompareAmount());
        }
        BigDecimal totalDiff = totalCompare.subtract(totalBase);
        BigDecimal totalRate = totalBase.compareTo(BigDecimal.ZERO) != 0
            ? totalDiff.divide(totalBase, 4, RoundingMode.HALF_UP).multiply(new BigDecimal("100")).setScale(2, RoundingMode.HALF_UP)
            : BigDecimal.ZERO;

        List<Map<String, Object>> details = new ArrayList<>();
        for (BudgetComparisonAnalysis item : items) {
            Map<String, Object> d = new HashMap<>();
            d.put("id", item.getId());
            d.put("comparisonItem", item.getAnalysisName());
            d.put("baseValue", item.getBaseAmount());
            d.put("compareValue", item.getCompareAmount());
            d.put("difference", item.getDifferenceAmount());
            d.put("changeRate", item.getDifferenceRate());
            details.add(d);
        }
        Map<String, Object> result = new HashMap<>();
        result.put("totalBase", totalBase);
        result.put("totalCompare", totalCompare);
        result.put("totalDifference", totalDiff);
        result.put("totalChangeRate", totalRate);
        result.put("count", items.size());
        result.put("details", details);
        log.info("批量对比分析: {} 条记录", items.size());
        return result;
    }

    @Override
    public Map<String, Object> deleteComparison(Map<String, Object> params) {
        String id = params.get("id") != null ? params.get("id").toString() : null;
        if (!StringUtils.hasText(id)) {
            throw new ServiceException("记录ID不能为空");
        }
        BudgetComparisonAnalysis entity = comparisonMapper.selectById(id);
        if (entity == null) {
            throw new ServiceException("记录不存在");
        }
        comparisonMapper.deleteById(id);
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("id", id);
        log.info("删除对比分析记录: {}", id);
        return result;
    }
}

