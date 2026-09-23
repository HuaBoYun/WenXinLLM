package com.management.accountant.oracle.service.advanced;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.oracle.entity.advanced.VersionComparison;

import java.util.List;
import java.util.Map;

public interface VersionComparisonService {
    List<VersionComparison> selectList(Map<String, Object> params);
    Page<VersionComparison> selectPage(Map<String, Object> params, Integer pageNum, Integer pageSize);
    VersionComparison selectById(String comparisonId);
    boolean insert(VersionComparison comparison);
    boolean update(VersionComparison comparison);
    boolean deleteById(String comparisonId);
    boolean copyComparison(String comparisonId);
    boolean recompare(String comparisonId);
    List<Map<String, Object>> getDifferences(String comparisonId);
    List<Map<String, Object>> getLogs(String comparisonId);
    Map<String, Object> getExportData(String comparisonId);
    List<Map<String, Object>> getAvailableVersions();
    Map<String, Object> getStats();
}
