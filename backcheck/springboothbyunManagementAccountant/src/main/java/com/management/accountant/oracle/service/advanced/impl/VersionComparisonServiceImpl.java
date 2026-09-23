package com.management.accountant.oracle.service.advanced.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.oracle.entity.advanced.VersionComparison;
import com.management.accountant.oracle.entity.advanced.VersionComparisonDiff;
import com.management.accountant.oracle.entity.advanced.VersionComparisonLog;
import com.management.accountant.oracle.entity.budget.BudgetVersion;
import com.management.accountant.oracle.mapper.advanced.VersionComparisonMapper;
import com.management.accountant.oracle.mapper.advanced.VersionComparisonDiffMapper;
import com.management.accountant.oracle.mapper.advanced.VersionComparisonLogMapper;
import com.management.accountant.oracle.mapper.budget.BudgetVersionMapper;
import com.management.accountant.oracle.service.advanced.VersionComparisonService;
import com.management.accountant.util.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

@Slf4j
@Service("versionComparisonServiceOracle")
public class VersionComparisonServiceImpl implements VersionComparisonService {

    @Resource
    private VersionComparisonMapper comparisonMapper;
    @Resource
    private BudgetVersionMapper budgetVersionMapper;
    @Resource
    private VersionComparisonDiffMapper diffMapper;
    @Resource
    private VersionComparisonLogMapper logMapper;
    private static final SnowflakeIdWorker idWorker = new SnowflakeIdWorker(1, 1);

    @Override
    public List<VersionComparison> selectList(Map<String, Object> params) {
        return comparisonMapper.selectList(buildWrapper(params));
    }

    @Override
    public Page<VersionComparison> selectPage(Map<String, Object> params, Integer pageNum, Integer pageSize) {
        return comparisonMapper.selectPage(new Page<>(pageNum, pageSize), buildWrapper(params));
    }

    @Override
    public VersionComparison selectById(String comparisonId) { return comparisonMapper.selectById(comparisonId); }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean insert(VersionComparison comparison) {
        comparison.setComparisonId("VC" + idWorker.nextId());
        comparison.setCreateTime(new Date());
        comparison.setDelFlag(0);
        if (comparison.getComparisonStatus() == null) {
            comparison.setComparisonStatus("PENDING");
        }
        if (comparison.getDifferenceCount() == null) {
            comparison.setDifferenceCount(0);
        }
        return comparisonMapper.insert(comparison) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean update(VersionComparison comparison) {
        comparison.setUpdateTime(new Date());
        return comparisonMapper.updateById(comparison) > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteById(String id) {
        VersionComparison c = comparisonMapper.selectById(id);
        if (c != null) { c.setDelFlag(1); c.setUpdateTime(new Date()); return comparisonMapper.updateById(c) > 0; }
        return false;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean copyComparison(String id) {
        VersionComparison src = comparisonMapper.selectById(id);
        if (src == null) return false;
        VersionComparison copy = new VersionComparison();
        copy.setComparisonId("VC" + idWorker.nextId());
        copy.setComparisonName(src.getComparisonName() + " - 副本");
        copy.setComparisonType(src.getComparisonType());
        copy.setSourceVersion(src.getSourceVersion());
        copy.setTargetVersion(src.getTargetVersion());
        copy.setComparisonScope(src.getComparisonScope());
        copy.setComparisonStatus("PENDING");
        copy.setDescription(src.getDescription());
        copy.setCreateBy(src.getCreateBy());
        copy.setCreateTime(new Date());
        copy.setDelFlag(0);
        return comparisonMapper.insert(copy) > 0;
    }

    @Override
    public boolean recompare(String id) {
        VersionComparison c = comparisonMapper.selectById(id);
        if (c == null) return false;
        c.setComparisonStatus("COMPARING");
        c.setUpdateTime(new Date());
        comparisonMapper.updateById(c);
        c.setComparisonStatus("COMPLETED");
        c.setUpdateTime(new Date());
        return comparisonMapper.updateById(c) > 0;
    }

    @Override
    public List<Map<String, Object>> getDifferences(String comparisonId) {
        QueryWrapper<VersionComparisonDiff> w = new QueryWrapper<>();
        w.eq("COMPARISON_ID", comparisonId);
        w.eq("DEL_FLAG", 0);
        w.orderByAsc("CREATE_TIME");
        List<VersionComparisonDiff> diffs = diffMapper.selectList(w);
        List<Map<String, Object>> result = new ArrayList<>();
        for (VersionComparisonDiff d : diffs) {
            Map<String, Object> map = new HashMap<>();
            map.put("diffId", d.getDiffId());
            map.put("fieldName", d.getFieldName());
            map.put("fieldPath", d.getFieldPath());
            map.put("sourceValue", d.getSourceValue());
            map.put("targetValue", d.getTargetValue());
            map.put("differenceType", d.getDifferenceType());
            map.put("impact", d.getImpact());
            map.put("recommendation", d.getRecommendation());
            result.add(map);
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> getLogs(String comparisonId) {
        QueryWrapper<VersionComparisonLog> w = new QueryWrapper<>();
        w.eq("COMPARISON_ID", comparisonId);
        w.eq("DEL_FLAG", 0);
        w.orderByDesc("LOG_TIME");
        List<VersionComparisonLog> logs = logMapper.selectList(w);
        List<Map<String, Object>> result = new ArrayList<>();
        for (VersionComparisonLog l : logs) {
            Map<String, Object> map = new HashMap<>();
            map.put("logId", l.getLogId());
            map.put("logTime", l.getLogTime());
            map.put("operation", l.getOperation());
            map.put("operator", l.getOperator());
            map.put("description", l.getDescription());
            map.put("result", l.getResult());
            result.add(map);
        }
        return result;
    }

    @Override
    public Map<String, Object> getExportData(String comparisonId) {
        Map<String, Object> exportData = new HashMap<>();
        VersionComparison comparison = comparisonMapper.selectById(comparisonId);
        if (comparison != null) {
            exportData.put("comparisonName", comparison.getComparisonName());
            exportData.put("comparisonType", comparison.getComparisonType());
            exportData.put("sourceVersion", comparison.getSourceVersion());
            exportData.put("targetVersion", comparison.getTargetVersion());
            exportData.put("comparisonStatus", comparison.getComparisonStatus());
            exportData.put("differenceCount", comparison.getDifferenceCount());
            exportData.put("createBy", comparison.getCreateBy());
            exportData.put("createTime", comparison.getCreateTime());
            exportData.put("description", comparison.getDescription());
        }
        exportData.put("differences", getDifferences(comparisonId));
        exportData.put("logs", getLogs(comparisonId));
        return exportData;
    }

    @Override
    public List<Map<String, Object>> getAvailableVersions() {
        QueryWrapper<BudgetVersion> w = new QueryWrapper<>();
        w.eq("DEL_FLAG", 0);
        w.orderByDesc("CREATE_TIME");
        List<BudgetVersion> versions = budgetVersionMapper.selectList(w);
        List<Map<String, Object>> result = new ArrayList<>();
        for (BudgetVersion v : versions) {
            Map<String, Object> map = new HashMap<>();
            map.put("id", v.getVersionId());
            map.put("version", v.getVersionNumber());
            map.put("versionName", v.getVersionName());
            map.put("versionType", v.getVersionType());
            map.put("versionStatus", v.getVersionStatus());
            map.put("fiscalYear", v.getFiscalYear());
            result.add(map);
        }
        return result;
    }

    @Override
    public Map<String, Object> getStats() {
        Map<String, Object> stats = new HashMap<>();
        QueryWrapper<VersionComparison> w = new QueryWrapper<>();
        w.eq("DEL_FLAG", 0);
        long totalComparisons = comparisonMapper.selectCount(w);
        stats.put("totalComparisons", totalComparisons);

        // totalVersions: 预算版本总数
        QueryWrapper<BudgetVersion> vw = new QueryWrapper<>();
        vw.eq("DEL_FLAG", 0);
        stats.put("totalVersions", budgetVersionMapper.selectCount(vw));

        // avgDifferences: 平均差异数
        List<VersionComparison> allComparisons = comparisonMapper.selectList(w);
        double avgDiff = 0.0;
        if (allComparisons != null && !allComparisons.isEmpty()) {
            int totalDiff = 0;
            for (VersionComparison vc : allComparisons) {
                if (vc.getDifferenceCount() != null) {
                    totalDiff += vc.getDifferenceCount();
                }
            }
            avgDiff = Math.round((double) totalDiff / allComparisons.size() * 10.0) / 10.0;
        }
        stats.put("avgDifferences", avgDiff);

        // accuracy: 已完成对比占比
        double accuracy = 0.0;
        if (totalComparisons > 0) {
            QueryWrapper<VersionComparison> cw = new QueryWrapper<>();
            cw.eq("DEL_FLAG", 0);
            cw.eq("COMPARISON_STATUS", "COMPLETED");
            long completedCount = comparisonMapper.selectCount(cw);
            accuracy = Math.round((double) completedCount / totalComparisons * 1000.0) / 10.0;
        }
        stats.put("accuracy", accuracy);

        // 按对比类型统计
        String[] types = {"FULL_COMPARISON", "INCREMENTAL_COMPARISON", "FIELD_COMPARISON", "STRUCTURE_COMPARISON"};
        String[] keys = {"fullComparisonCount", "incrementalComparisonCount", "fieldComparisonCount", "structureComparisonCount"};
        for (int i = 0; i < types.length; i++) {
            QueryWrapper<VersionComparison> tw = new QueryWrapper<>();
            tw.eq("DEL_FLAG", 0);
            tw.eq("COMPARISON_TYPE", types[i]);
            stats.put(keys[i], comparisonMapper.selectCount(tw));
        }

        return stats;
    }

    private QueryWrapper<VersionComparison> buildWrapper(Map<String, Object> params) {
        QueryWrapper<VersionComparison> w = new QueryWrapper<>();
        w.eq("DEL_FLAG", 0);
        if (params != null) {
            String keyword = (String) params.get("keyword");
            if (StringUtils.hasText(keyword)) w.like("COMPARISON_NAME", keyword);
            String type = (String) params.get("comparisonType");
            if (StringUtils.hasText(type)) w.eq("COMPARISON_TYPE", type);
        }
        w.orderByDesc("CREATE_TIME");
        return w;
    }
}
