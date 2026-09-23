package com.financial.sharing.oracle.service.impl;

import com.financial.sharing.oracle.dto.SqlPerformanceReportDTO;
import com.financial.sharing.oracle.dto.QueryOptimizationDTO;
import com.financial.sharing.oracle.mapper.SqlOptimizationMapper;
import com.financial.sharing.oracle.service.SqlOptimizationService;
import com.hbfk.util.JsonBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * SQL性能优化服务实现
 *
 * @author 赵工
 * @since 2025-12-07
 */
@Slf4j
@Service
public class SqlOptimizationServiceImpl implements SqlOptimizationService {

    @Autowired
    private SqlOptimizationMapper sqlOptimizationMapper;

    @Override
    public JsonBean analyzeSlowQueries(String startDate, String endDate, Long minExecuteTime) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("startDate", startDate);
            params.put("endDate", endDate);
            params.put("minExecuteTime", minExecuteTime);

            List<Map<String, Object>> slowQueries = sqlOptimizationMapper.selectSlowQueries(params);

            // 按执行时间排序
            slowQueries.sort((a, b) -> {
                Long timeA = (Long) a.get("ELAPSED_TIME");
                Long timeB = (Long) b.get("ELAPSED_TIME");
                return timeB.compareTo(timeA);
            });

            // 生成优化建议
            for (Map<String, Object> query : slowQueries) {
                String sqlText = (String) query.get("SQL_TEXT");
                List<Map<String, Object>> recommendations = generateOptimizationRecommendations(sqlText);
                query.put("recommendations", recommendations);
            }

            return createSuccessJson(slowQueries);
        } catch (Exception e) {
            log.error("分析慢查询失败", e);
            return createErrorJson("分析慢查询失败: " + e.getMessage());
        }
    }

    @Override
    public JsonBean getTablespaceUsage() {
        try {
            List<Map<String, Object>> tablespaces = sqlOptimizationMapper.selectTablespaceUsage();

            // 计算使用率
            for (Map<String, Object> tablespace : tablespaces) {
                Double totalSpace = (Double) tablespace.get("TOTAL_SPACE");
                Double usedSpace = (Double) tablespace.get("USED_SPACE");
                if (totalSpace != null && totalSpace > 0) {
                    Double usagePercent = (usedSpace / totalSpace) * 100;
                    tablespace.put("USAGE_PERCENT", Math.round(usagePercent * 100.0) / 100.0);

                    // 添加警告级别
                    if (usagePercent >= 90) {
                        tablespace.put("WARNING_LEVEL", "CRITICAL");
                    } else if (usagePercent >= 80) {
                        tablespace.put("WARNING_LEVEL", "WARNING");
                    } else {
                        tablespace.put("WARNING_LEVEL", "NORMAL");
                    }
                }
            }

            return createSuccessJson(tablespaces);
        } catch (Exception e) {
            log.error("获取表空间使用情况失败", e);
            return createErrorJson("获取表空间使用情况失败: " + e.getMessage());
        }
    }

    @Override
    public JsonBean analyzeIndexUsage(String tableName) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("tableName", tableName);

            List<Map<String, Object>> indexUsage = sqlOptimizationMapper.selectIndexUsage(params);

            // 分析未使用的索引
            List<Map<String, Object>> unusedIndexes = sqlOptimizationMapper.selectUnusedIndexes(params);

            Map<String, Object> result = new HashMap<>();
            result.put("indexUsage", indexUsage);
            result.put("unusedIndexes", unusedIndexes);

            return createSuccessJson(result);
        } catch (Exception e) {
            log.error("分析索引使用情况失败", e);
            return createErrorJson("分析索引使用情况失败: " + e.getMessage());
        }
    }

    @Override
    public JsonBean getExecutionPlan(String sql) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("sqlText", sql);

            List<Map<String, Object>> plan = sqlOptimizationMapper.selectExecutionPlan(params);

            // 解析执行计划，添加成本分析
            parseExecutionPlan(plan);

            return createSuccessJson(plan);
        } catch (Exception e) {
            log.error("获取执行计划失败", e);
            return createErrorJson("获取执行计划失败: " + e.getMessage());
        }
    }

    @Override
    public JsonBean generatePerformanceReport(String reportType) {
        try {
            Map<String, Object> report = new HashMap<>();

            // 获取基本性能指标
            Map<String, Object> performanceMetrics = sqlOptimizationMapper.selectPerformanceMetrics();
            report.put("metrics", performanceMetrics);

            // 获取TOP慢查询
            List<Map<String, Object>> topSlowQueries = sqlOptimizationMapper.selectTopSlowQueries(20);
            report.put("topSlowQueries", topSlowQueries);

            // 获取资源使用情况
            Map<String, Object> resourceUsage = sqlOptimizationMapper.selectResourceUsage();
            report.put("resourceUsage", resourceUsage);

            // 获取主要等待事件
            List<Map<String, Object>> waitEvents = sqlOptimizationMapper.selectTopWaitEvents(10);
            report.put("waitEvents", waitEvents);

            // 添加报告类型和生成时间
            report.put("reportType", reportType);
            report.put("generateTime", System.currentTimeMillis());

            return createSuccessJson(report);
        } catch (Exception e) {
            log.error("生成性能报告失败", e);
            return createErrorJson("生成性能报告失败: " + e.getMessage());
        }
    }

    @Override
    public JsonBean getWaitEvents() {
        try {
            List<Map<String, Object>> waitEvents = sqlOptimizationMapper.selectWaitEvents();

            // 计算百分比
            Long totalTime = waitEvents.stream()
                .mapToLong(e -> (Long) e.get("TIME_WAITED"))
                .sum();

            for (Map<String, Object> event : waitEvents) {
                Long waitTime = (Long) event.get("TIME_WAITED");
                if (totalTime > 0) {
                    Double percentage = (waitTime.doubleValue() / totalTime) * 100;
                    event.put("PERCENTAGE", Math.round(percentage * 100.0) / 100.0);
                }
            }

            return createSuccessJson(waitEvents);
        } catch (Exception e) {
            log.error("获取等待事件失败", e);
            return createErrorJson("获取等待事件失败: " + e.getMessage());
        }
    }

    @Override
    public JsonBean analyzeTableFragmentation(String schema) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("schema", schema);

            List<Map<String, Object>> fragmentation = sqlOptimizationMapper.selectTableFragmentation(params);

            // 添加碎片率计算和优化建议
            for (Map<String, Object> table : fragmentation) {
                Long numRows = (Long) table.get("NUM_ROWS");
                Long blocks = (Long) table.get("BLOCKS");
                Long emptyBlocks = (Long) table.get("EMPTY_BLOCKS");

                if (blocks != null && blocks > 0) {
                    Double fragmentationRate = (emptyBlocks.doubleValue() / blocks) * 100;
                    table.put("FRAGMENTATION_RATE", Math.round(fragmentationRate * 100.0) / 100.0);

                    // 添加优化建议
                    if (fragmentationRate > 20) {
                        table.put("RECOMMENDATION", "建议重建表或移动表段以减少碎片");
                    } else if (fragmentationRate > 10) {
                        table.put("RECOMMENDATION", "建议定期收集统计信息");
                    } else {
                        table.put("RECOMMENDATION", "碎片率正常，无需处理");
                    }
                }
            }

            return createSuccessJson(fragmentation);
        } catch (Exception e) {
            log.error("分析表碎片失败", e);
            return createErrorJson("分析表碎片失败: " + e.getMessage());
        }
    }

    @Override
    public JsonBean generateIndexRecommendations(String tableName) {
        try {
            Map<String, Object> result = new HashMap<>();

            // 获取表的列信息
            List<Map<String, Object>> columns = sqlOptimizationMapper.selectTableColumns(tableName);

            // 分析WHERE条件中的列
            List<Map<String, Object>> frequentColumns = sqlOptimizationMapper.selectFrequentQueryColumns(tableName);

            // 生成索引建议
            List<Map<String, Object>> recommendations = generateIndexSuggestions(columns, frequentColumns);

            result.put("currentIndexes", sqlOptimizationMapper.selectTableIndexes(tableName));
            result.put("recommendations", recommendations);

            return createSuccessJson(result);
        } catch (Exception e) {
            log.error("生成索引建议失败", e);
            return createErrorJson("生成索引建议失败: " + e.getMessage());
        }
    }

    @Override
    public JsonBean monitorLongRunningQueries(Long threshold) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("threshold", threshold);

            List<Map<String, Object>> longRunningQueries = sqlOptimizationMapper.selectLongRunningQueries(params);

            // 添加 kill 语句建议
            for (Map<String, Object> query : longRunningQueries) {
                Long sessionId = (Long) query.get("SID");
                Long serialNum = (Long) query.get("SERIAL#");
                query.put("KILL_COMMAND", String.format("ALTER SYSTEM KILL SESSION '%d,%d'", sessionId, serialNum));
            }

            return createSuccessJson(longRunningQueries);
        } catch (Exception e) {
            log.error("监控长时间运行查询失败", e);
            return createErrorJson("监控长时间运行查询失败: " + e.getMessage());
        }
    }

    @Override
    public JsonBean batchOptimizeQueries(List<QueryOptimizationDTO> optimizations) {
        try {
            Map<String, Object> result = new HashMap<>();
            int successCount = 0;
            int failCount = 0;

            for (QueryOptimizationDTO optimization : optimizations) {
                try {
                    // 执行优化操作
                    optimizeSingleQuery(optimization);
                    successCount++;
                } catch (Exception e) {
                    log.error("优化查询失败: {}", optimization.getSqlId(), e);
                    failCount++;
                }
            }

            result.put("totalCount", optimizations.size());
            result.put("successCount", successCount);
            result.put("failCount", failCount);

            return createSuccessJson(result);
        } catch (Exception e) {
            log.error("批量优化查询失败", e);
            return createErrorJson("批量优化查询失败: " + e.getMessage());
        }
    }

    @Override
    public JsonBean getTableStatistics(String tableName) {
        try {
            Map<String, Object> statistics = sqlOptimizationMapper.selectTableStatistics(tableName);
            return createSuccessJson(statistics);
        } catch (Exception e) {
            log.error("获取表统计信息失败", e);
            return createErrorJson("获取表统计信息失败: " + e.getMessage());
        }
    }

    @Override
    public JsonBean updateTableStatistics(String tableName, Integer estimatePercent) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("tableName", tableName);
            params.put("estimatePercent", estimatePercent != null ? estimatePercent : 100);

            sqlOptimizationMapper.updateTableStatistics(params);

            return createSuccessJson("统计信息更新成功");
        } catch (Exception e) {
            log.error("更新表统计信息失败", e);
            return createErrorJson("更新表统计信息失败: " + e.getMessage());
        }
    }

    @Override
    public JsonBean checkMissingIndexes(String schema) {
        try {
            List<Map<String, Object>> missingIndexes = sqlOptimizationMapper.selectMissingIndexes(schema);

            // 为每个建议添加影响评估
            for (Map<String, Object> index : missingIndexes) {
                Long estimatedBenefit = (Long) index.get("ESTIMATED_BENEFIT");
                if (estimatedBenefit != null) {
                    if (estimatedBenefit > 10000) {
                        index.put("PRIORITY", "HIGH");
                    } else if (estimatedBenefit > 1000) {
                        index.put("PRIORITY", "MEDIUM");
                    } else {
                        index.put("PRIORITY", "LOW");
                    }
                }
            }

            return createSuccessJson(missingIndexes);
        } catch (Exception e) {
            log.error("检查缺失索引失败", e);
            return createErrorJson("检查缺失索引失败: " + e.getMessage());
        }
    }

    @Override
    public JsonBean getSqlExecutionHistory(String sqlId, Integer days) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("sqlId", sqlId);
            params.put("days", days != null ? days : 7);

            List<Map<String, Object>> history = sqlOptimizationMapper.selectSqlExecutionHistory(params);

            return createSuccessJson(history);
        } catch (Exception e) {
            log.error("获取SQL执行历史失败", e);
            return createErrorJson("获取SQL执行历史失败: " + e.getMessage());
        }
    }

    @Override
    public JsonBean generatePerformanceDashboard() {
        try {
            Map<String, Object> dashboard = new HashMap<>();

            // 核心性能指标
            dashboard.put("performanceMetrics", sqlOptimizationMapper.selectPerformanceMetrics());

            // 实时会话数
            dashboard.put("activeSessions", sqlOptimizationMapper.selectActiveSessions());

            // TOP 5 慢查询
            dashboard.put("topSlowQueries", sqlOptimizationMapper.selectTopSlowQueries(5));

            // 表空间使用情况
            dashboard.put("tablespaces", getTablespaceUsage());

            // 等待事件TOP 5
            dashboard.put("topWaitEvents", sqlOptimizationMapper.selectTopWaitEvents(5));

            // 缓存命中率
            dashboard.put("cacheHitRatio", sqlOptimizationMapper.selectCacheHitRatio());

            dashboard.put("lastUpdateTime", System.currentTimeMillis());

            return createSuccessJson(dashboard);
        } catch (Exception e) {
            log.error("生成性能监控看板失败", e);
            return createErrorJson("生成性能监控看板失败: " + e.getMessage());
        }
    }

    /**
     * 生成优化建议
     */
    private List<Map<String, Object>> generateOptimizationRecommendations(String sqlText) {
        List<Map<String, Object>> recommendations = new java.util.ArrayList<>();

        // 检查常见性能问题
        if (sqlText.toUpperCase().contains("SELECT *")) {
            Map<String, Object> rec = new HashMap<>();
            rec.put("type", "COLUMN_SELECTION");
            rec.put("message", "避免使用SELECT *，只查询需要的列");
            rec.put("severity", "MEDIUM");
            recommendations.add(rec);
        }

        if (sqlText.toUpperCase().contains("LIKE '%") || sqlText.toUpperCase().contains("LIKE '%")) {
            Map<String, Object> rec = new HashMap<>();
            rec.put("type", "LIKE_PATTERN");
            rec.put("message", "避免使用前导通配符的LIKE查询");
            rec.put("severity", "HIGH");
            recommendations.add(rec);
        }

        if (sqlText.toUpperCase().contains("ORDER BY") && !sqlText.toUpperCase().contains("LIMIT")) {
            Map<String, Object> rec = new HashMap<>();
            rec.put("type", "ORDER_BY");
            rec.put("message", "ORDER BY建议配合LIMIT使用，避免全排序");
            rec.put("severity", "MEDIUM");
            recommendations.add(rec);
        }

        return recommendations;
    }

    /**
     * 解析执行计划
     */
    private void parseExecutionPlan(List<Map<String, Object>> plan) {
        // 添加成本分析
        Long totalCost = plan.stream()
            .filter(p -> p.get("COST") != null)
            .mapToLong(p -> ((Number) p.get("COST")).longValue())
            .sum();

        for (Map<String, Object> step : plan) {
            if (step.get("COST") != null) {
                Long stepCost = ((Number) step.get("COST")).longValue();
                if (totalCost > 0) {
                    Double costPercentage = (stepCost.doubleValue() / totalCost) * 100;
                    step.put("COST_PERCENTAGE", Math.round(costPercentage * 100.0) / 100.0);
                }
            }
        }
    }

    /**
     * 生成索引建议
     */
    private List<Map<String, Object>> generateIndexSuggestions(
            List<Map<String, Object>> columns,
            List<Map<String, Object>> frequentColumns) {

        List<Map<String, Object>> suggestions = new java.util.ArrayList<>();

        // 基于频繁查询的列生成建议
        for (Map<String, Object> column : frequentColumns) {
            String columnName = (String) column.get("COLUMN_NAME");
            Long frequency = (Long) column.get("FREQUENCY");

            if (frequency > 100) {
                Map<String, Object> suggestion = new HashMap<>();
                suggestion.put("columnName", columnName);
                suggestion.put("indexType", "BTREE");
                suggestion.put("reason", String.format("该列在查询中频繁使用(%d次)", frequency));
                suggestion.put("estimatedBenefit", frequency * 10);
                suggestions.add(suggestion);
            }
        }

        return suggestions;
    }

    // JsonBean 辅助方法
    private JsonBean createSuccessJson(Object data) {
        JsonBean json = new JsonBean();
        json.setCode(1);
        json.setMsg("操作成功");
        json.setData(data);
        return json;
    }

    private JsonBean createErrorJson(String message) {
        JsonBean json = new JsonBean();
        json.setCode(0);
        json.setMsg(message);
        return json;
    }

    /**
     * 优化单个查询
     */
    private void optimizeSingleQuery(QueryOptimizationDTO optimization) {
        switch (optimization.getType()) {
            case "ADD_INDEX":
                // 执行添加索引操作
                String indexSql = String.format("CREATE INDEX IDX_%s_%s ON %s(%s)",
                    optimization.getTableName(),
                    optimization.getColumnName(),
                    optimization.getTableName(),
                    optimization.getColumnName());
                sqlOptimizationMapper.executeSql(indexSql);
                break;

            case "UPDATE_STATS":
                // 更新统计信息
                updateTableStatistics(optimization.getTableName(), optimization.getEstimatePercent());
                break;

            case "REBUILD_INDEX":
                // 重建索引
                String rebuildSql = String.format("ALTER INDEX %s REBUILD", optimization.getIndexName());
                sqlOptimizationMapper.executeSql(rebuildSql);
                break;

            default:
                throw new IllegalArgumentException("不支持的优化类型: " + optimization.getType());
        }
    }
}