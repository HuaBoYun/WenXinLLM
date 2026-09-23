package com.financial.sharing.dataCollection.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.financial.sharing.dataCollection.entity.TblCollectionLog;
import com.financial.sharing.dataCollection.entity.TblCollectionTask;
import com.financial.sharing.dataCollection.entity.TblDataSource;
import com.financial.sharing.dataCollection.entity.TblMappingRule;
import com.financial.sharing.dataCollection.mapper.CollectionLogMapper;
import com.financial.sharing.dataCollection.mapper.CollectionTaskMapper;
import com.financial.sharing.dataCollection.mapper.DataSourceMapper;
import com.financial.sharing.dataCollection.mapper.MappingRuleMapper;
import com.financial.sharing.dataCollection.service.CollectionTaskExecutionService;
import com.hbfk.util.redis.Random.RandomUtil;
import com.financial.sharing.util.MyJsonBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.concurrent.*;

/**
 * 归集任务执行Service实现类
 * 
 * @author Augment Agent
 * @date 2026-02-02
 */
@Slf4j
@Service
public class CollectionTaskExecutionServiceImpl implements CollectionTaskExecutionService {

    @Resource
    private CollectionTaskMapper collectionTaskMapper;

    @Resource
    private DataSourceMapper dataSourceMapper;

    @Resource
    private MappingRuleMapper mappingRuleMapper;

    @Resource
    private CollectionLogMapper collectionLogMapper;

    /**
     * 线程池：用于异步执行归集任务
     */
    private static final ExecutorService EXECUTOR = new ThreadPoolExecutor(
            2,  // 核心线程数
            5,  // 最大线程数
            60L, TimeUnit.SECONDS,  // 空闲线程存活时间
            new LinkedBlockingQueue<>(100),  // 任务队列
            new ThreadFactory() {
                private int count = 0;
                @Override
                public Thread newThread(Runnable r) {
                    return new Thread(r, "CollectionTask-" + (++count));
                }
            },
            new ThreadPoolExecutor.CallerRunsPolicy()  // 拒绝策略
    );

    /**
     * 任务控制信号：用于停止任务
     * Key: taskId, Value: "STOP"
     */
    private static final ConcurrentHashMap<String, String> TASK_CONTROL_SIGNALS = new ConcurrentHashMap<>();

    @Override
    public void executeTaskAsync(String taskId, String orgId) {
        // 提交到线程池异步执行
        EXECUTOR.submit(() -> {
            try {
                log.info("开始异步执行归集任务: {}", taskId);
                executeTask(taskId, orgId);
            } catch (Exception e) {
                log.error("异步执行归集任务失败: {}", taskId, e);
            }
        });
    }

    @Override
    public MyJsonBean stopTaskExecution(String taskId, String orgId) {
        try {
            // 设置停止信号
            TASK_CONTROL_SIGNALS.put(taskId, "STOP");

            // 更新任务状态
            TblCollectionTask task = getTask(taskId, orgId);
            if (task == null) {
                return MyJsonBean.errorData("归集任务不存在");
            }

            task.setExecuteStatus("PENDING");
            task.setUpdateTime(new Date());
            collectionTaskMapper.updateById(task);

            log.info("归集任务已停止: {}", taskId);
            return MyJsonBean.successData("任务已停止");
        } catch (Exception e) {
            log.error("停止归集任务失败", e);
            return MyJsonBean.errorData("停止归集任务失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean getTaskProgress(String taskId, String orgId) {
        try {
            TblCollectionTask task = getTask(taskId, orgId);
            if (task == null) {
                return MyJsonBean.errorData("归集任务不存在");
            }

            Map<String, Object> progress = new HashMap<>();
            progress.put("taskId", task.getTaskId());
            progress.put("taskName", task.getTaskName());
            progress.put("executeStatus", task.getExecuteStatus());
            progress.put("totalCount", task.getTotalCount());
            progress.put("successCount", task.getSuccessCount());
            progress.put("failedCount", task.getFailedCount());
            progress.put("skipCount", task.getSkipCount());
            progress.put("executeDuration", task.getExecuteDuration());
            progress.put("errorMessage", task.getErrorMessage());

            // 计算进度百分比
            if (task.getTotalCount() != null && task.getTotalCount() > 0) {
                int processed = (task.getSuccessCount() != null ? task.getSuccessCount() : 0)
                        + (task.getFailedCount() != null ? task.getFailedCount() : 0)
                        + (task.getSkipCount() != null ? task.getSkipCount() : 0);
                double percentage = (double) processed / task.getTotalCount() * 100;
                progress.put("percentage", String.format("%.2f", percentage));
            } else {
                progress.put("percentage", "0.00");
            }

            return MyJsonBean.successData(progress);
        } catch (Exception e) {
            log.error("获取任务执行进度失败", e);
            return MyJsonBean.errorData("获取任务执行进度失败：" + e.getMessage());
        }
    }

    /**
     * 执行归集任务（核心方法）
     */
    private void executeTask(String taskId, String orgId) {
        long startTime = System.currentTimeMillis();
        Connection sourceConn = null;

        // 创建执行日志
        TblCollectionLog executionLog = new TblCollectionLog();
        executionLog.setLogId(RandomUtil.uuStringId());
        executionLog.setTaskId(taskId);
        executionLog.setStartTime(new Date(startTime));
        executionLog.setOrgId(orgId);

        try {
            // 1. 获取任务信息
            TblCollectionTask task = getTask(taskId, orgId);
            if (task == null) {
                log.error("归集任务不存在: {}", taskId);
                return;
            }

            // 设置日志基本信息
            executionLog.setTaskCode(task.getTaskCode());
            executionLog.setTaskName(task.getTaskName());
            executionLog.setExecuteType(task.getTaskType());

            log.info("开始执行归集任务: {} - {}", task.getTaskCode(), task.getTaskName());

            // 2. 获取数据源信息
            TblDataSource dataSource = getDataSource(task.getSourceId(), orgId);
            if (dataSource == null) {
                updateTaskFailed(task, "数据源不存在");
                return;
            }

            // 3. 获取映射规则列表
            List<TblMappingRule> mappingRules = getMappingRules(task.getRuleIds(), orgId);
            if (mappingRules.isEmpty()) {
                updateTaskFailed(task, "映射规则不存在");
                return;
            }

            // 4. 初始化统计数据
            int totalCount = 0;
            int successCount = 0;
            int failedCount = 0;
            int skipCount = 0;

            // 5. 按规则顺序执行归集
            for (TblMappingRule rule : mappingRules) {
                // 检查停止信号
                if (checkStopSignal(taskId)) {
                    log.info("归集任务被停止: {}", taskId);
                    updateTaskStopped(task, totalCount, successCount, failedCount, skipCount, startTime);

                    // 保存停止日志
                    executionLog.setExecuteStatus("STOPPED");
                    executionLog.setEndTime(new Date());
                    executionLog.setExecuteDuration(System.currentTimeMillis() - startTime);
                    executionLog.setTotalCount(totalCount);
                    executionLog.setSuccessCount(successCount);
                    executionLog.setFailedCount(failedCount);
                    executionLog.setSkipCount(skipCount);
                    executionLog.setErrorMessage("任务被手动停止");
                    collectionLogMapper.insert(executionLog);

                    return;
                }

                log.info("执行映射规则: {} - {}", rule.getRuleCode(), rule.getRuleName());

                try {
                    // 执行单个规则的归集
                    Map<String, Integer> ruleResult = executeRule(dataSource, rule, orgId);
                    totalCount += ruleResult.get("total");
                    successCount += ruleResult.get("success");
                    failedCount += ruleResult.get("failed");
                    skipCount += ruleResult.get("skip");

                    // 更新任务进度
                    updateTaskProgress(task, totalCount, successCount, failedCount, skipCount);

                } catch (Exception e) {
                    log.error("执行映射规则失败: {} - {}", rule.getRuleCode(), e.getMessage(), e);
                    failedCount++;
                }
            }

            // 6. 更新任务状态为成功
            updateTaskSuccess(task, totalCount, successCount, failedCount, skipCount, startTime);
            log.info("归集任务执行完成: {}, 总数: {}, 成功: {}, 失败: {}, 跳过: {}",
                    taskId, totalCount, successCount, failedCount, skipCount);

            // 保存成功日志
            executionLog.setExecuteStatus("SUCCESS");
            executionLog.setEndTime(new Date());
            executionLog.setExecuteDuration(System.currentTimeMillis() - startTime);
            executionLog.setTotalCount(totalCount);
            executionLog.setSuccessCount(successCount);
            executionLog.setFailedCount(failedCount);
            executionLog.setSkipCount(skipCount);
            collectionLogMapper.insert(executionLog);

        } catch (Exception e) {
            log.error("执行归集任务失败: {}", taskId, e);
            TblCollectionTask task = getTask(taskId, orgId);
            if (task != null) {
                updateTaskFailed(task, e.getMessage());
            }

            // 保存失败日志
            executionLog.setExecuteStatus("FAILED");
            executionLog.setEndTime(new Date());
            executionLog.setExecuteDuration(System.currentTimeMillis() - startTime);
            executionLog.setErrorMessage(e.getMessage());
            collectionLogMapper.insert(executionLog);

        } finally {
            // 清除停止信号
            TASK_CONTROL_SIGNALS.remove(taskId);
            // 关闭连接
            closeConnection(sourceConn);
        }
    }

    /**
     * 执行单个映射规则的归集
     */
    private Map<String, Integer> executeRule(TblDataSource dataSource, TblMappingRule rule, String orgId) throws Exception {
        Map<String, Integer> result = new HashMap<>();
        result.put("total", 0);
        result.put("success", 0);
        result.put("failed", 0);
        result.put("skip", 0);

        Connection sourceConn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // 1. 建立数据源连接
            sourceConn = createConnection(dataSource);

            // 2. 构建查询SQL
            String querySql = buildQuerySql(rule);
            log.debug("查询SQL: {}", querySql);

            // 3. 执行查询
            stmt = sourceConn.createStatement();
            rs = stmt.executeQuery(querySql);

            // 4. 获取结果集元数据
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            // 5. 解析字段映射
            Map<String, String> fieldMappings = parseFieldMappings(rule.getFieldMappings());

            // 6. 批量处理数据
            int batchSize = rule.getBatchSize() != null ? rule.getBatchSize() : 1000;
            List<Map<String, Object>> batch = new ArrayList<>();

            while (rs.next()) {
                // 读取源数据
                Map<String, Object> sourceData = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object columnValue = rs.getObject(i);
                    sourceData.put(columnName, columnValue);
                }

                // 应用映射规则
                Map<String, Object> mappedData = applyMappings(sourceData, rule, fieldMappings);

                // 检查过滤条件
                if (!checkFilterCondition(sourceData, rule.getFilterCondition())) {
                    result.put("skip", result.get("skip") + 1);
                    continue;
                }

                // 应用转换规则
                mappedData = applyTransformRules(mappedData, rule.getTransformRules());

                // 应用校验规则
                if (!applyValidationRules(mappedData, rule.getValidationRules())) {
                    result.put("failed", result.get("failed") + 1);
                    continue;
                }

                batch.add(mappedData);
                result.put("total", result.get("total") + 1);

                // 达到批次大小，执行批量插入
                if (batch.size() >= batchSize) {
                    int inserted = batchInsert(sourceConn, rule.getTargetTable(), batch, rule.getConflictStrategy());
                    result.put("success", result.get("success") + inserted);
                    result.put("failed", result.get("failed") + (batch.size() - inserted));
                    batch.clear();
                }
            }

            // 处理剩余数据
            if (!batch.isEmpty()) {
                int inserted = batchInsert(sourceConn, rule.getTargetTable(), batch, rule.getConflictStrategy());
                result.put("success", result.get("success") + inserted);
                result.put("failed", result.get("failed") + (batch.size() - inserted));
            }

            return result;

        } finally {
            closeResultSet(rs);
            closeStatement(stmt);
            closeConnection(sourceConn);
        }
    }

    /**
     * 建立数据源连接
     */
    private Connection createConnection(TblDataSource dataSource) throws Exception {
        if (!"DATABASE".equals(dataSource.getSourceType())) {
            throw new Exception("当前仅支持数据库类型的数据源");
        }

        String jdbcUrl = buildJdbcUrl(dataSource);
        return DriverManager.getConnection(jdbcUrl, dataSource.getUsername(), dataSource.getPassword());
    }

    /**
     * 构建JDBC URL
     */
    private String buildJdbcUrl(TblDataSource dataSource) {
        String connectionType = dataSource.getConnectionType();
        String host = dataSource.getHost();
        Integer port = dataSource.getPort();
        String databaseName = dataSource.getDatabaseName();

        if ("DM".equals(connectionType) || "DAMENG".equals(connectionType)) {
            // 达梦数据库
            return String.format("jdbc:dm://%s:%d/%s", host, port, databaseName);
        } else if ("MYSQL".equals(connectionType)) {
            // MySQL数据库
            return String.format("jdbc:mysql://%s:%d/%s?useUnicode=true&characterEncoding=utf8&useSSL=false",
                    host, port, databaseName);
        } else if ("ORACLE".equals(connectionType)) {
            // Oracle数据库
            return String.format("jdbc:oracle:thin:@%s:%d:%s", host, port, databaseName);
        } else {
            throw new RuntimeException("不支持的数据库类型: " + connectionType);
        }
    }

    /**
     * 构建查询SQL
     */
    private String buildQuerySql(TblMappingRule rule) {
        String sourceQuery = rule.getSourceQuery();

        // 如果是增量同步，添加增量条件
        if ("Y".equals(rule.getIsIncremental()) && StringUtils.isNotBlank(rule.getIncrementalField())) {
            String incrementalField = rule.getIncrementalField();
            String lastSyncValue = rule.getLastSyncValue();

            if (StringUtils.isNotBlank(lastSyncValue)) {
                // 如果SQL中已有WHERE子句
                if (sourceQuery.toUpperCase().contains("WHERE")) {
                    sourceQuery += " AND " + incrementalField + " > '" + lastSyncValue + "'";
                } else {
                    sourceQuery += " WHERE " + incrementalField + " > '" + lastSyncValue + "'";
                }
            }
        }

        return sourceQuery;
    }

    /**
     * 解析字段映射
     */
    private Map<String, String> parseFieldMappings(String fieldMappingsStr) {
        Map<String, String> mappings = new HashMap<>();

        if (StringUtils.isBlank(fieldMappingsStr)) {
            return mappings;
        }

        try {
            JSONArray fieldMappings = JSON.parseArray(fieldMappingsStr);
            for (int i = 0; i < fieldMappings.size(); i++) {
                JSONObject mapping = fieldMappings.getJSONObject(i);
                String sourceField = mapping.getString("sourceField");
                String targetField = mapping.getString("targetField");
                mappings.put(sourceField, targetField);
            }
        } catch (Exception e) {
            log.error("解析字段映射失败", e);
        }

        return mappings;
    }

    /**
     * 应用字段映射
     */
    private Map<String, Object> applyMappings(Map<String, Object> sourceData, TblMappingRule rule,
                                               Map<String, String> fieldMappings) {
        Map<String, Object> mappedData = new HashMap<>();

        for (Map.Entry<String, String> entry : fieldMappings.entrySet()) {
            String sourceField = entry.getKey();
            String targetField = entry.getValue();

            if (sourceData.containsKey(sourceField)) {
                mappedData.put(targetField, sourceData.get(sourceField));
            }
        }

        return mappedData;
    }

    /**
     * 检查过滤条件
     */
    private boolean checkFilterCondition(Map<String, Object> sourceData, String filterCondition) {
        if (StringUtils.isBlank(filterCondition)) {
            return true;
        }

        try {
            // 简单的等于条件判断：field=value
            String[] parts = filterCondition.split("=");
            if (parts.length == 2) {
                String field = parts[0].trim();
                String value = parts[1].trim();

                Object fieldValue = sourceData.get(field);
                if (fieldValue == null) {
                    return false;
                }

                return value.equals(fieldValue.toString());
            }
        } catch (Exception e) {
            log.error("检查过滤条件失败", e);
        }

        return true;
    }

    /**
     * 应用转换规则
     */
    private Map<String, Object> applyTransformRules(Map<String, Object> mappedData, String transformRulesStr) {
        if (StringUtils.isBlank(transformRulesStr)) {
            return mappedData;
        }

        try {
            JSONArray transformRules = JSON.parseArray(transformRulesStr);
            for (int i = 0; i < transformRules.size(); i++) {
                JSONObject rule = transformRules.getJSONObject(i);
                String field = rule.getString("field");
                String type = rule.getString("type");
                Object value = rule.get("value");

                if (mappedData.containsKey(field)) {
                    Object originalValue = mappedData.get(field);
                    Object transformedValue = applyTransform(originalValue, type, value);
                    mappedData.put(field, transformedValue);
                }
            }
        } catch (Exception e) {
            log.error("应用转换规则失败", e);
        }

        return mappedData;
    }

    /**
     * 应用单个转换
     */
    private Object applyTransform(Object value, String type, Object param) {
        if (value == null) {
            return null;
        }

        try {
            switch (type) {
                case "multiply":
                    return Double.parseDouble(value.toString()) * Double.parseDouble(param.toString());
                case "divide":
                    double divisor = Double.parseDouble(param.toString());
                    if (divisor == 0) {
                        return value;
                    }
                    return Double.parseDouble(value.toString()) / divisor;
                case "add":
                    return Double.parseDouble(value.toString()) + Double.parseDouble(param.toString());
                case "subtract":
                    return Double.parseDouble(value.toString()) - Double.parseDouble(param.toString());
                case "uppercase":
                    return value.toString().toUpperCase();
                case "lowercase":
                    return value.toString().toLowerCase();
                case "trim":
                    return value.toString().trim();
                case "substring":
                    String[] range = param.toString().split(",");
                    int start = Integer.parseInt(range[0]);
                    int end = range.length > 1 ? Integer.parseInt(range[1]) : value.toString().length();
                    return value.toString().substring(start, end);
                case "replace":
                    String[] replaceParams = param.toString().split(",");
                    if (replaceParams.length == 2) {
                        return value.toString().replace(replaceParams[0], replaceParams[1]);
                    }
                    return value;
                default:
                    return value;
            }
        } catch (Exception e) {
            log.error("应用转换失败: type={}, value={}", type, value, e);
            return value;
        }
    }

    /**
     * 应用校验规则
     */
    private boolean applyValidationRules(Map<String, Object> mappedData, String validationRulesStr) {
        if (StringUtils.isBlank(validationRulesStr)) {
            return true;
        }

        try {
            JSONArray validationRules = JSON.parseArray(validationRulesStr);
            for (int i = 0; i < validationRules.size(); i++) {
                JSONObject rule = validationRules.getJSONObject(i);
                String field = rule.getString("field");
                String type = rule.getString("type");

                if (!mappedData.containsKey(field)) {
                    continue;
                }

                Object value = mappedData.get(field);
                if (!validateField(value, type)) {
                    log.warn("字段校验失败: field={}, type={}, value={}", field, type, value);
                    return false;
                }
            }
        } catch (Exception e) {
            log.error("应用校验规则失败", e);
            return false;
        }

        return true;
    }

    /**
     * 校验单个字段
     */
    private boolean validateField(Object value, String type) {
        if (value == null) {
            return !"required".equals(type);
        }

        String valueStr = value.toString();

        switch (type) {
            case "required":
                return StringUtils.isNotBlank(valueStr);
            case "number":
                try {
                    Double.parseDouble(valueStr);
                    return true;
                } catch (Exception e) {
                    return false;
                }
            case "integer":
                try {
                    Integer.parseInt(valueStr);
                    return true;
                } catch (Exception e) {
                    return false;
                }
            case "positive":
                try {
                    return Double.parseDouble(valueStr) > 0;
                } catch (Exception e) {
                    return false;
                }
            case "negative":
                try {
                    return Double.parseDouble(valueStr) < 0;
                } catch (Exception e) {
                    return false;
                }
            case "email":
                return valueStr.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
            case "phone":
                return valueStr.matches("^1[3-9]\\d{9}$");
            case "url":
                return valueStr.matches("^(http|https)://.*$");
            default:
                return true;
        }
    }

    /**
     * 批量插入数据
     */
    private int batchInsert(Connection conn, String targetTable, List<Map<String, Object>> batch, String conflictStrategy) {
        if (batch.isEmpty()) {
            return 0;
        }

        int successCount = 0;
        PreparedStatement pstmt = null;

        try {
            // 获取字段列表
            Set<String> fields = batch.get(0).keySet();

            // 构建INSERT SQL
            StringBuilder sql = new StringBuilder("INSERT INTO ");
            sql.append(targetTable).append(" (");
            sql.append(String.join(", ", fields));
            sql.append(") VALUES (");
            sql.append(String.join(", ", Collections.nCopies(fields.size(), "?")));
            sql.append(")");

            pstmt = conn.prepareStatement(sql.toString());

            for (Map<String, Object> data : batch) {
                try {
                    int paramIndex = 1;
                    for (String field : fields) {
                        pstmt.setObject(paramIndex++, data.get(field));
                    }

                    pstmt.executeUpdate();
                    successCount++;

                } catch (SQLException e) {
                    // 根据冲突策略处理
                    if ("SKIP".equals(conflictStrategy)) {
                        log.debug("跳过冲突记录");
                    } else if ("UPDATE".equals(conflictStrategy)) {
                        // TODO: 实现UPDATE逻辑
                        log.warn("UPDATE策略暂未实现");
                    } else {
                        log.error("插入数据失败", e);
                    }
                }
            }

        } catch (Exception e) {
            log.error("批量插入数据失败", e);
        } finally {
            closeStatement(pstmt);
        }

        return successCount;
    }

    /**
     * 获取任务信息
     */
    private TblCollectionTask getTask(String taskId, String orgId) {
        LambdaQueryWrapper<TblCollectionTask> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TblCollectionTask::getTaskId, taskId);
        wrapper.eq(TblCollectionTask::getOrgId, orgId);
        return collectionTaskMapper.selectOne(wrapper);
    }

    /**
     * 获取数据源信息
     */
    private TblDataSource getDataSource(String sourceId, String orgId) {
        LambdaQueryWrapper<TblDataSource> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TblDataSource::getSourceId, sourceId);
        wrapper.eq(TblDataSource::getOrgId, orgId);
        return dataSourceMapper.selectOne(wrapper);
    }

    /**
     * 获取映射规则列表
     */
    private List<TblMappingRule> getMappingRules(String ruleIdsStr, String orgId) {
        List<TblMappingRule> rules = new ArrayList<>();

        if (StringUtils.isBlank(ruleIdsStr)) {
            return rules;
        }

        try {
            JSONArray ruleIds = JSON.parseArray(ruleIdsStr);
            for (int i = 0; i < ruleIds.size(); i++) {
                String ruleId = ruleIds.getString(i);

                LambdaQueryWrapper<TblMappingRule> wrapper = new LambdaQueryWrapper<>();
                wrapper.eq(TblMappingRule::getRuleId, ruleId);
                wrapper.eq(TblMappingRule::getOrgId, orgId);
                wrapper.eq(TblMappingRule::getIsEnabled, "Y");
                wrapper.orderByAsc(TblMappingRule::getSortNo);

                TblMappingRule rule = mappingRuleMapper.selectOne(wrapper);
                if (rule != null) {
                    rules.add(rule);
                }
            }
        } catch (Exception e) {
            log.error("获取映射规则列表失败", e);
        }

        return rules;
    }

    /**
     * 检查停止信号
     */
    private boolean checkStopSignal(String taskId) {
        return "STOP".equals(TASK_CONTROL_SIGNALS.get(taskId));
    }

    /**
     * 更新任务进度
     */
    private void updateTaskProgress(TblCollectionTask task, int totalCount, int successCount,
                                     int failedCount, int skipCount) {
        task.setTotalCount(totalCount);
        task.setSuccessCount(successCount);
        task.setFailedCount(failedCount);
        task.setSkipCount(skipCount);
        task.setUpdateTime(new Date());
        collectionTaskMapper.updateById(task);
    }

    /**
     * 更新任务状态为成功
     */
    private void updateTaskSuccess(TblCollectionTask task, int totalCount, int successCount,
                                    int failedCount, int skipCount, long startTime) {
        long duration = (System.currentTimeMillis() - startTime) / 1000;

        task.setExecuteStatus("SUCCESS");
        task.setTotalCount(totalCount);
        task.setSuccessCount(successCount);
        task.setFailedCount(failedCount);
        task.setSkipCount(skipCount);
        task.setExecuteDuration((int) duration);
        task.setLastExecuteTime(new Date());
        task.setUpdateTime(new Date());
        task.setErrorMessage(null);

        collectionTaskMapper.updateById(task);
    }

    /**
     * 更新任务状态为失败
     */
    private void updateTaskFailed(TblCollectionTask task, String errorMessage) {
        task.setExecuteStatus("FAILED");
        task.setErrorMessage(errorMessage);
        task.setLastExecuteTime(new Date());
        task.setUpdateTime(new Date());
        collectionTaskMapper.updateById(task);
    }

    /**
     * 更新任务状态为已停止
     */
    private void updateTaskStopped(TblCollectionTask task, int totalCount, int successCount,
                                    int failedCount, int skipCount, long startTime) {
        long duration = (System.currentTimeMillis() - startTime) / 1000;

        task.setExecuteStatus("PENDING");
        task.setTotalCount(totalCount);
        task.setSuccessCount(successCount);
        task.setFailedCount(failedCount);
        task.setSkipCount(skipCount);
        task.setExecuteDuration((int) duration);
        task.setLastExecuteTime(new Date());
        task.setUpdateTime(new Date());
        task.setErrorMessage("任务被手动停止");

        collectionTaskMapper.updateById(task);
    }

    /**
     * 关闭数据库连接
     */
    private void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (Exception e) {
                log.error("关闭数据库连接失败", e);
            }
        }
    }

    /**
     * 关闭Statement
     */
    private void closeStatement(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (Exception e) {
                log.error("关闭Statement失败", e);
            }
        }
    }

    /**
     * 关闭ResultSet
     */
    private void closeResultSet(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (Exception e) {
                log.error("关闭ResultSet失败", e);
            }
        }
    }
}


