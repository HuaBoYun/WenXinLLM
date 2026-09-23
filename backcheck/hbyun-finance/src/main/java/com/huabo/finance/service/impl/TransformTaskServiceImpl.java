package com.huabo.finance.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.util.JsonBean;
import com.hbfk.util.ResponseFormat;
import com.huabo.finance.dto.FieldMappingDTO;
import com.huabo.finance.dto.TransformRuleDTO;
import com.huabo.finance.dto.TransformTaskDTO;
import com.huabo.finance.entity.*;
import com.huabo.finance.mapper.*;
import com.huabo.finance.service.ITransformTaskService;
import com.huabo.finance.service.algorithm.TransformAlgorithmEngine;
import com.huabo.finance.vo.FieldInfoVO;
import com.huabo.finance.vo.SourceTableVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 转换任务Service实现类
 *
 * @author 华博云开发团队
 * @since 2025-10-24
 */
@Slf4j
@Service
public class TransformTaskServiceImpl extends ServiceImpl<TransformTaskMapper, TransformTask>
        implements ITransformTaskService {

    @Autowired
    private TransformTaskMapper transformTaskMapper;

    @Autowired
    private TransformFieldMappingMapper transformFieldMappingMapper;

    @Autowired
    private TransformRuleMapper transformRuleMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private CollectionTaskMapper collectionTaskMapper;

    @Autowired
    private BdFinversionMapper bdFinversionMapper;

    @Autowired
    private BdInitSqlconfigMapper bdInitSqlconfigMapper;

    @Autowired
    private TransformAlgorithmEngine algorithmEngine;

    /**
     * 获取采集任务的源表列表
     */
    @Override
    public JsonBean getSourceTables(String taskId) {
        try {
            // 查询采集任务详情表,获取源表列表
            String sql = "SELECT DISTINCT SOURCE_TABLE_NAME AS TABLE_NAME, " +
                        "COUNT(*) AS FIELD_COUNT " +
                        "FROM TBL_COLLECTION_TASK_DETAIL " +
                        "WHERE TASK_ID = ? " +
                        "GROUP BY SOURCE_TABLE_NAME " +
                        "ORDER BY SOURCE_TABLE_NAME";
            
            List<Map<String, Object>> tables = jdbcTemplate.queryForList(sql, taskId);
            
            List<SourceTableVO> result = new ArrayList<>();
            for (Map<String, Object> table : tables) {
                SourceTableVO vo = new SourceTableVO();
                vo.setTableName((String) table.get("TABLE_NAME"));
                vo.setFieldCount(((Number) table.get("FIELD_COUNT")).intValue());
                
                // 查询记录数量
                try {
                    String countSql = "SELECT COUNT(*) FROM " + vo.getTableName();
                    Integer count = jdbcTemplate.queryForObject(countSql, Integer.class);
                    vo.setRecordCount(count);
                } catch (Exception e) {
                    vo.setRecordCount(0);
                }
                
                result.add(vo);
            }
            
            return ResponseFormat.retParam(1, 200, result);
        } catch (Exception e) {
            log.error("获取源表列表失败", e);
            return ResponseFormat.retParam(0, "获取源表列表失败: " + e.getMessage(), null);
        }
    }

    /**
     * 获取源表字段列表
     */
    @Override
    public JsonBean getSourceFields(String taskId, String tableName) {
        try {
            // 查询表字段信息
            String sql = "SELECT COLUMN_NAME, DATA_TYPE, DATA_LENGTH, DATA_PRECISION, " +
                        "NULLABLE, COMMENTS " +
                        "FROM USER_TAB_COLUMNS " +
                        "LEFT JOIN USER_COL_COMMENTS USING (TABLE_NAME, COLUMN_NAME) " +
                        "WHERE TABLE_NAME = ? " +
                        "ORDER BY COLUMN_ID";
            
            List<Map<String, Object>> fields = jdbcTemplate.queryForList(sql, tableName);
            
            List<FieldInfoVO> result = new ArrayList<>();
            for (Map<String, Object> field : fields) {
                FieldInfoVO vo = new FieldInfoVO();
                vo.setFieldName((String) field.get("COLUMN_NAME"));
                vo.setFieldType((String) field.get("DATA_TYPE"));
                vo.setFieldComment((String) field.get("COMMENTS"));
                
                Object length = field.get("DATA_LENGTH");
                vo.setLength(length != null ? ((Number) length).intValue() : null);
                
                Object precision = field.get("DATA_PRECISION");
                vo.setPrecision(precision != null ? ((Number) precision).intValue() : null);
                
                vo.setNullable((String) field.get("NULLABLE"));
                
                result.add(vo);
            }
            
            return ResponseFormat.retParam(1, 200, result);
        } catch (Exception e) {
            log.error("获取源表字段列表失败", e);
            return ResponseFormat.retParam(0, "获取源表字段列表失败: " + e.getMessage(), null);
        }
    }

    /**
     * 获取系统表列表
     */
    @Override
    public JsonBean getSystemTables() {
        try {
            // 系统表列表(基于流程.md定义)
            List<SourceTableVO> systemTables = Arrays.asList(
                createTableVO("ORG_ORGS", "财务组织信息表"),
                createTableVO("BD_VOUCHERTYPE", "凭证类别"),
                createTableVO("BD_ACCTYPE", "科目类型"),
                createTableVO("BD_ACCOUNT", "会计科目基本信息"),
                createTableVO("BD_ACCCHART", "科目表"),
                createTableVO("BD_CURRTYPE", "币种"),
                createTableVO("GL_VOUCHER", "凭证库表"),
                createTableVO("GL_DETAIL", "凭证明细"),
                createTableVO("GL_BALANCE", "凭证余额"),
                createTableVO("BD_ACCSYSTEM", "会计准则"),
                createTableVO("BD_ACCPERIOD", "会计期间"),
                createTableVO("BD_ACCPERIODMONTH", "会计期间月份"),
                createTableVO("BD_ACCPERIODQUART", "会计期间季度"),
                createTableVO("BD_ACCPERIODSCHEME", "会计期间方案"),
                createTableVO("BD_ACCASS", "辅助核算"),
                createTableVO("BD_ACCASSITEM", "辅助核算项"),
                createTableVO("BD_ACCASOA", "辅助核算科目"),
                createTableVO("BD_ACCOUNTMAP", "科目映射"),
                createTableVO("BD_TAXCODE", "税码"),
                createTableVO("BD_TAXRATE", "税率"),
                createTableVO("BD_CURRINFO", "币种信息"),
                createTableVO("BD_CURRRATE", "汇率"),
                createTableVO("BD_AVGRATE", "平均汇率"),
                createTableVO("BD_ADJUSTRATE", "调整汇率"),
                createTableVO("BD_EXRATESCHEME", "汇率方案"),
                createTableVO("BD_TIMEZONE", "时区"),
                createTableVO("FA_ACCBOOKINFO", "账簿信息"),
                createTableVO("FA_ACCBOOK_USER", "账簿用户"),
                createTableVO("FA_ACCBOOK_ROLE", "账簿角色"),
                createTableVO("ORG_SETOFBOOK", "账套"),
                createTableVO("GL_ASSDETAIL", "辅助核算明细"),
                createTableVO("GL_ASSBALANE", "辅助核算余额")
            );
            
            return ResponseFormat.retParam(1, 200, systemTables);
        } catch (Exception e) {
            log.error("获取系统表列表失败", e);
            return ResponseFormat.retParam(0, "获取系统表列表失败: " + e.getMessage(), null);
        }
    }

    /**
     * 获取系统表字段列表
     */
    @Override
    public JsonBean getSystemFields(String tableName) {
        try {
            // 查询系统表字段信息
            String sql = "SELECT COLUMN_NAME, DATA_TYPE, DATA_LENGTH, DATA_PRECISION, " +
                        "NULLABLE, COMMENTS " +
                        "FROM USER_TAB_COLUMNS " +
                        "LEFT JOIN USER_COL_COMMENTS USING (TABLE_NAME, COLUMN_NAME) " +
                        "WHERE TABLE_NAME = ? " +
                        "ORDER BY COLUMN_ID";
            
            List<Map<String, Object>> fields = jdbcTemplate.queryForList(sql, tableName);
            
            List<FieldInfoVO> result = new ArrayList<>();
            for (Map<String, Object> field : fields) {
                FieldInfoVO vo = new FieldInfoVO();
                vo.setFieldName((String) field.get("COLUMN_NAME"));
                vo.setFieldType((String) field.get("DATA_TYPE"));
                vo.setFieldComment((String) field.get("COMMENTS"));
                
                Object length = field.get("DATA_LENGTH");
                vo.setLength(length != null ? ((Number) length).intValue() : null);
                
                Object precision = field.get("DATA_PRECISION");
                vo.setPrecision(precision != null ? ((Number) precision).intValue() : null);
                
                vo.setNullable((String) field.get("NULLABLE"));
                
                result.add(vo);
            }
            
            return ResponseFormat.retParam(1, 200, result);
        } catch (Exception e) {
            log.error("获取系统表字段列表失败", e);
            return ResponseFormat.retParam(0, "获取系统表字段列表失败: " + e.getMessage(), null);
        }
    }

    /**
     * 验证字段类型匹配
     */
    @Override
    public JsonBean validateTypeConversion(String sourceType, String targetType, List<String> sampleData) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("canConvert", true);
            result.put("failedData", new ArrayList<>());
            result.put("message", "类型匹配");
            
            // 类型转换规则
            if ("VARCHAR".equalsIgnoreCase(sourceType) || "VARCHAR2".equalsIgnoreCase(sourceType)) {
                if ("NUMBER".equalsIgnoreCase(targetType)) {
                    // 字符串转数字,需要验证
                    List<String> failedData = new ArrayList<>();
                    for (String data : sampleData) {
                        if (data != null && !data.trim().isEmpty()) {
                            try {
                                Double.parseDouble(data);
                            } catch (NumberFormatException e) {
                                failedData.add(data);
                            }
                        }
                    }
                    
                    if (!failedData.isEmpty()) {
                        result.put("canConvert", false);
                        result.put("failedData", failedData);
                        result.put("message", "存在" + failedData.size() + "条无法转换为数字的数据");
                    }
                }
            } else if ("NUMBER".equalsIgnoreCase(sourceType)) {
                if ("DATE".equalsIgnoreCase(targetType) || "TIMESTAMP".equalsIgnoreCase(targetType)) {
                    result.put("canConvert", false);
                    result.put("message", "数字类型不支持转换为日期类型");
                }
            }
            
            return ResponseFormat.retParam(1, 200, result);
        } catch (Exception e) {
            log.error("验证字段类型匹配失败", e);
            return ResponseFormat.retParam(0, "验证字段类型匹配失败: " + e.getMessage(), null);
        }
    }

    /**
     * 创建转换任务
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean createTransformTask(TransformTaskDTO taskDTO, String createUser) {
        try {
            String taskId = "transform_" + System.currentTimeMillis();
            
            // 1. 创建转换任务记录
            TransformTask task = new TransformTask();
            task.setTaskId(taskId);
            task.setTaskName(taskDTO.getTaskName());
            task.setCollectionTaskId(taskDTO.getCollectionTaskId());
            task.setStatus("PENDING");
            task.setTotalRecords(0);
            task.setSuccessRecords(0);
            task.setFailedRecords(0);
            task.setRemark(taskDTO.getRemark());
            task.setCreateTime(new Date());
            task.setUpdateTime(new Date());
            
            transformTaskMapper.insert(task);
            
            // 2. 保存字段映射配置 - 使用TransformFieldMapping
            List<String> mappingIds = new ArrayList<>();
            if (taskDTO.getMappings() != null) {
                for (int i = 0; i < taskDTO.getMappings().size(); i++) {
                    FieldMappingDTO mappingDTO = taskDTO.getMappings().get(i);
                    String mappingId = "mapping_" + System.currentTimeMillis() + "_" + i;
                    mappingIds.add(mappingId);

                    TransformFieldMapping mapping = new TransformFieldMapping();
                    BeanUtils.copyProperties(mappingDTO, mapping);
                    mapping.setMappingId(mappingId);
                    mapping.setTransformTaskId(taskId);  // 使用transformTaskId字段
                    mapping.setIsEnabled(1);  // 使用Integer类型
                    mapping.setCreateTime(new Date());
                    mapping.setUpdateTime(new Date());

                    transformFieldMappingMapper.insert(mapping);
                }
            }
            
            // 3. 保存计算规则
            if (taskDTO.getRules() != null) {
                for (TransformRuleDTO ruleDTO : taskDTO.getRules()) {
                    String ruleId = "rule_" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString().substring(0, 8);
                    
                    TransformRule rule = new TransformRule();
                    BeanUtils.copyProperties(ruleDTO, rule);
                    rule.setRuleId(ruleId);
                    
                    // 根据mappingIndex获取对应的mappingId
                    if (ruleDTO.getMappingIndex() != null && ruleDTO.getMappingIndex() < mappingIds.size()) {
                        rule.setMappingId(mappingIds.get(ruleDTO.getMappingIndex()));
                    }
                    
                    rule.setIsEnabled("1");
                    rule.setCreateTime(new Date());
                    rule.setUpdateTime(new Date());
                    
                    transformRuleMapper.insert(rule);
                }
            }
            
            log.info("转换任务创建成功，任务ID: {}", taskId);
            
            Map<String, Object> result = new HashMap<>();
            result.put("taskId", taskId);
            result.put("status", "PENDING");
            
            return ResponseFormat.retParam(1, 200, result);
            
        } catch (Exception e) {
            log.error("创建转换任务失败", e);
            throw new RuntimeException("创建转换任务失败: " + e.getMessage(), e);
        }
    }

    /**
     * 执行转换任务
     * 转换优先级:
     * 1. 优先执行BD_INIT_SQLCONFIG表中的SQL配置转换
     * 2. 然后执行字段映射配置转换
     * 3. 最后执行算法配置转换
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean executeTransform(String taskId) {
        try {
            log.info("========== 开始执行转换任务: {} ==========", taskId);

            // 1. 查询任务信息
            TransformTask task = transformTaskMapper.selectById(taskId);
            if (task == null) {
                return ResponseFormat.retParam(0, "转换任务不存在", null);
            }

            // 2. 更新任务状态为运行中
            task.setStatus("RUNNING");
            task.setStartTime(new Date());
            task.setUpdateTime(new Date());
            transformTaskMapper.updateById(task);

            int totalCount = 0;

            // ========== 第一优先级: 执行SQL配置转换 ==========
            log.info("步骤1: 检查并执行SQL配置转换");
            int sqlConfigCount = executeSqlConfigTransform(task.getCollectionTaskId());
            totalCount += sqlConfigCount;
            log.info("SQL配置转换完成,共转换{}条记录", sqlConfigCount);

            // ========== 第二优先级: 执行字段映射配置转换 ==========
            log.info("步骤2: 检查并执行字段映射配置转换");

            // 3. 查询字段映射配置 - 使用TransformFieldMapping
            List<TransformFieldMapping> mappings = transformFieldMappingMapper.selectByTransformTaskId(taskId);

            if (mappings == null || mappings.isEmpty()) {
                log.info("未找到字段映射配置,跳过字段映射转换");
            } else {
                log.info("转换任务[{}]查询到{}条字段映射配置", taskId, mappings.size());

                // 4. 按目标表分组
                Map<String, List<TransformFieldMapping>> tableGroupMap = new HashMap<>();
                for (TransformFieldMapping mapping : mappings) {
                    String targetTable = mapping.getTargetTable();
                    tableGroupMap.computeIfAbsent(targetTable, k -> new ArrayList<>()).add(mapping);
                }

                log.info("共需转换 {} 个目标表", tableGroupMap.size());

                // 5. 逐表执行转换
                for (Map.Entry<String, List<TransformFieldMapping>> entry : tableGroupMap.entrySet()) {
                    String targetTable = entry.getKey();
                    List<TransformFieldMapping> tableMappings = entry.getValue();

                    log.info("开始转换目标表: {}", targetTable);

                    // 5.1 构建查询SQL
                    String selectSql = buildSelectSql(task.getCollectionTaskId(), tableMappings);
                    log.info("查询SQL: {}", selectSql);

                    // 5.2 执行查询
                    List<Map<String, Object>> sourceData = jdbcTemplate.queryForList(selectSql);
                    log.info("查询到 {} 条源数据", sourceData.size());

                    // 打印第一条数据用于调试
                    if (!sourceData.isEmpty()) {
                        log.info("第一条查询结果: {}", sourceData.get(0));
                    }

                    if (sourceData.isEmpty()) {
                        continue;
                    }

                    // 5.3 构建插入SQL
                    String insertSql = buildInsertSql(targetTable, tableMappings);
                    log.info("插入SQL: {}", insertSql);

                    // 5.4 批量插入数据
                    int count = batchInsertData(insertSql, sourceData, tableMappings);
                    totalCount += count;

                    log.info("目标表 {} 转换完成，插入 {} 条记录", targetTable, count);
                }
            }

            // ========== 第三优先级: 执行算法配置转换 ==========
            log.info("步骤3: 检查并执行算法配置转换");
            try {
                algorithmEngine.executeAlgorithms(taskId);
                log.info("算法配置转换执行成功, taskId={}", taskId);
            } catch (Exception e) {
                log.error("算法配置转换执行失败, taskId={}", taskId, e);
                // 算法执行失败不影响主流程,记录错误信息
                task.setErrorMessage("算法执行失败: " + e.getMessage());
            }

            // 7. 更新任务状态为已完成
            task.setStatus("COMPLETED");
            task.setEndTime(new Date());
            task.setTotalRecords(totalCount);
            task.setSuccessRecords(totalCount);
            task.setFailedRecords(0);
            task.setUpdateTime(new Date());
            transformTaskMapper.updateById(task);

            log.info("转换任务执行成功，任务ID: {}, 转换记录数: {}", taskId, totalCount);

            Map<String, Object> result = new HashMap<>();
            result.put("taskId", taskId);
            result.put("transformCount", totalCount);  // 保留原字段,兼容其他调用
            result.put("successCount", totalCount);    // 新增字段,匹配前端期望
            result.put("message", "成功转换 " + totalCount + " 条记录");

            return ResponseFormat.retParam(1, 200, result);

        } catch (Exception e) {
            log.error("执行转换任务失败", e);

            // 更新任务状态为失败
            try {
                TransformTask task = transformTaskMapper.selectById(taskId);
                if (task != null) {
                    task.setStatus("FAILED");
                    task.setErrorMessage(e.getMessage());
                    task.setUpdateTime(new Date());
                    transformTaskMapper.updateById(task);
                }
            } catch (Exception ex) {
                log.error("更新任务状态失败", ex);
            }

            throw new RuntimeException("执行转换任务失败: " + e.getMessage(), e);
        }
    }

    /**
     * 构建查询SQL
     */
    private String buildSelectSql(String collectionTaskId, List<TransformFieldMapping> mappings) {
        // 获取第一个字段映射的源表(固定值映射没有源表)
        String sourceTable = null;
        for (TransformFieldMapping mapping : mappings) {
            if (mapping.getSourceTable() != null && !mapping.getSourceTable().trim().isEmpty()) {
                sourceTable = mapping.getSourceTable();
                break;
            }
        }

        // 构建SELECT字段列表
        StringBuilder selectFields = new StringBuilder();
        for (int i = 0; i < mappings.size(); i++) {
            TransformFieldMapping mapping = mappings.get(i);

            // 根据映射类型构建字段表达式
            String fieldExpression;

            // 1. 优先检查是否有固定值
            if (mapping.getDefaultValue() != null && !mapping.getDefaultValue().trim().isEmpty()) {
                // 固定值:使用SQL字面量
                fieldExpression = "'" + mapping.getDefaultValue().replace("'", "''") + "'";
            }
            // 2. 检查是否有转换表达式
            else if (mapping.getTransformExpression() != null && !mapping.getTransformExpression().trim().isEmpty()) {
                // 使用转换表达式
                fieldExpression = mapping.getTransformExpression();
            }
            // 3. 直接字段映射
            else if (mapping.getSourceField() != null && !mapping.getSourceField().trim().isEmpty()) {
                // 直接映射
                fieldExpression = mapping.getSourceField();
            }
            // 4. 默认NULL
            else {
                fieldExpression = "NULL";
            }

            selectFields.append(fieldExpression).append(" AS ").append(mapping.getTargetField());

            if (i < mappings.size() - 1) {
                selectFields.append(", ");
            }
        }

        // 如果所有字段都是固定值,需要从一个虚拟表查询以获得一行数据
        if (sourceTable == null) {
            // 达梦数据库使用DUAL虚拟表
            return "SELECT " + selectFields.toString() + " FROM DUAL";
        }

        // 构建完整SQL
        return "SELECT " + selectFields.toString() + " FROM " + sourceTable;
    }

    /**
     * 构建插入SQL
     */
    private String buildInsertSql(String targetTable, List<TransformFieldMapping> mappings) {
        StringBuilder fields = new StringBuilder();
        StringBuilder placeholders = new StringBuilder();

        for (int i = 0; i < mappings.size(); i++) {
            fields.append(mappings.get(i).getTargetField());
            placeholders.append("?");

            if (i < mappings.size() - 1) {
                fields.append(", ");
                placeholders.append(", ");
            }
        }

        return "INSERT INTO " + targetTable + " (" + fields.toString() + ") VALUES (" + placeholders.toString() + ")";
    }

    /**
     * 批量插入数据
     */
    private int batchInsertData(String insertSql, List<Map<String, Object>> sourceData, List<TransformFieldMapping> mappings) {
        List<Object[]> batchArgs = new ArrayList<>();

        for (Map<String, Object> row : sourceData) {
            Object[] args = new Object[mappings.size()];
            for (int i = 0; i < mappings.size(); i++) {
                String targetField = mappings.get(i).getTargetField();

                // 尝试多种大小写方式获取值
                Object value = row.get(targetField);
                if (value == null) {
                    // 尝试大写
                    value = row.get(targetField.toUpperCase());
                }
                if (value == null) {
                    // 尝试小写
                    value = row.get(targetField.toLowerCase());
                }

                // 如果还是null,打印调试信息
                if (value == null) {
                    log.warn("字段 {} 在查询结果中未找到值, 可用的key: {}", targetField, row.keySet());
                }

                args[i] = value;
            }
            batchArgs.add(args);
        }

        // 打印第一条插入数据用于调试
        if (!batchArgs.isEmpty()) {
            log.info("第一条插入参数: {}", java.util.Arrays.toString(batchArgs.get(0)));
            log.info("字段映射: {}", mappings.stream()
                .map(m -> m.getTargetField() + "=" + (m.getDefaultValue() != null ? "固定值:" + m.getDefaultValue() : "字段:" + m.getSourceField()))
                .collect(java.util.stream.Collectors.joining(", ")));
        }

        int[] results = jdbcTemplate.batchUpdate(insertSql, batchArgs);
        return results.length;
    }

    /**
     * 获取转换任务列表
     */
    @Override
    public JsonBean getTaskList(Integer pageNumber, Integer pageSize) {
        try {
            // 使用MyBatis-Plus分页查询
            com.baomidou.mybatisplus.extension.plugins.pagination.Page<TransformTask> page =
                new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageNumber, pageSize);

            // 禁用自动优化COUNT SQL，避免达梦数据库兼容性问题
            page.setOptimizeCountSql(false);

            com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<TransformTask> queryWrapper =
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
            queryWrapper.orderByDesc("CREATE_TIME");

            com.baomidou.mybatisplus.extension.plugins.pagination.Page<TransformTask> resultPage =
                transformTaskMapper.selectPage(page, queryWrapper);

            Map<String, Object> data = new HashMap<>();
            data.put("total", resultPage.getTotal());
            data.put("pageNum", pageNumber);
            data.put("pageSize", pageSize);
            data.put("list", resultPage.getRecords());

            return ResponseFormat.retParam(1, 200, data);
        } catch (Exception e) {
            log.error("获取转换任务列表失败", e);
            return ResponseFormat.retParam(0, "获取转换任务列表失败: " + e.getMessage(), null);
        }
    }

    /**
     * 获取转换任务详情
     */
    @Override
    public JsonBean getTaskDetail(String taskId) {
        try {
            TransformTask task = transformTaskMapper.selectById(taskId);
            if (task == null) {
                return ResponseFormat.retParam(0, "转换任务不存在", null);
            }

            // 查询字段映射 - 使用TransformFieldMapping
            List<TransformFieldMapping> mappings = transformFieldMappingMapper.selectByTransformTaskId(taskId);

            // 查询计算规则
            List<TransformRule> rules = new ArrayList<>();
            for (TransformFieldMapping mapping : mappings) {
                com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<TransformRule> ruleWrapper =
                    new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
                ruleWrapper.eq("MAPPING_ID", mapping.getMappingId());
                List<TransformRule> mappingRules = transformRuleMapper.selectList(ruleWrapper);
                rules.addAll(mappingRules);
            }

            Map<String, Object> data = new HashMap<>();
            data.put("task", task);
            data.put("mappings", mappings);
            data.put("rules", rules);

            return ResponseFormat.retParam(1, 200, data);
        } catch (Exception e) {
            log.error("获取转换任务详情失败", e);
            return ResponseFormat.retParam(0, "获取转换任务详情失败: " + e.getMessage(), null);
        }
    }

    /**
     * 暂停转换任务
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean pauseTask(String taskId) {
        try {
            TransformTask task = transformTaskMapper.selectById(taskId);
            if (task == null) {
                return ResponseFormat.retParam(0, "转换任务不存在", null);
            }

            if (!"RUNNING".equals(task.getStatus())) {
                return ResponseFormat.retParam(0, "只能暂停运行中的任务", null);
            }

            task.setStatus("PAUSED");
            task.setUpdateTime(new Date());
            transformTaskMapper.updateById(task);

            log.info("转换任务暂停成功，任务ID: {}", taskId);
            return ResponseFormat.retParam(1, 200, null);
        } catch (Exception e) {
            log.error("暂停转换任务失败", e);
            return ResponseFormat.retParam(0, "暂停转换任务失败: " + e.getMessage(), null);
        }
    }

    /**
     * 恢复转换任务
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean resumeTask(String taskId) {
        try {
            TransformTask task = transformTaskMapper.selectById(taskId);
            if (task == null) {
                return ResponseFormat.retParam(0, "转换任务不存在", null);
            }

            if (!"PAUSED".equals(task.getStatus())) {
                return ResponseFormat.retParam(0, "只能恢复已暂停的任务", null);
            }

            task.setStatus("RUNNING");
            task.setUpdateTime(new Date());
            transformTaskMapper.updateById(task);

            log.info("转换任务恢复成功，任务ID: {}", taskId);
            return ResponseFormat.retParam(1, 200, null);
        } catch (Exception e) {
            log.error("恢复转换任务失败", e);
            return ResponseFormat.retParam(0, "恢复转换任务失败: " + e.getMessage(), null);
        }
    }

    /**
     * 取消转换任务
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean cancelTask(String taskId) {
        try {
            TransformTask task = transformTaskMapper.selectById(taskId);
            if (task == null) {
                return ResponseFormat.retParam(0, "转换任务不存在", null);
            }

            if ("COMPLETED".equals(task.getStatus()) || "CANCELLED".equals(task.getStatus())) {
                return ResponseFormat.retParam(0, "任务已完成或已取消，无法取消", null);
            }

            task.setStatus("CANCELLED");
            task.setUpdateTime(new Date());
            transformTaskMapper.updateById(task);

            log.info("转换任务取消成功，任务ID: {}", taskId);
            return ResponseFormat.retParam(1, 200, null);
        } catch (Exception e) {
            log.error("取消转换任务失败", e);
            return ResponseFormat.retParam(0, "取消转换任务失败: " + e.getMessage(), null);
        }
    }

    /**
     * 删除转换任务
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean deleteTask(String taskId) {
        try {
            TransformTask task = transformTaskMapper.selectById(taskId);
            if (task == null) {
                return ResponseFormat.retParam(0, "转换任务不存在", null);
            }

            // 删除计算规则 - 使用TransformFieldMapping
            List<TransformFieldMapping> mappings = transformFieldMappingMapper.selectByTransformTaskId(taskId);

            for (TransformFieldMapping mapping : mappings) {
                com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<TransformRule> ruleWrapper =
                    new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
                ruleWrapper.eq("MAPPING_ID", mapping.getMappingId());
                transformRuleMapper.delete(ruleWrapper);
            }

            // 删除字段映射
            com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<TransformFieldMapping> mappingWrapper =
                new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
            mappingWrapper.eq("TRANSFORM_TASK_ID", taskId);
            transformFieldMappingMapper.delete(mappingWrapper);

            // 删除任务
            transformTaskMapper.deleteById(taskId);

            log.info("转换任务删除成功，任务ID: {}", taskId);
            return ResponseFormat.retParam(1, 200, null);
        } catch (Exception e) {
            log.error("删除转换任务失败", e);
            return ResponseFormat.retParam(0, "删除转换任务失败: " + e.getMessage(), null);
        }
    }

    /**
     * 创建表VO辅助方法
     */
    private SourceTableVO createTableVO(String tableName, String tableComment) {
        SourceTableVO vo = new SourceTableVO();
        vo.setTableName(tableName);
        vo.setTableComment(tableComment);
        return vo;
    }

    /**
     * 根据采集任务ID查询财务版本信息
     * 数据关联: TBL_COLLECTION_TASK.PLAN_ID → BD_FINVERSION.FID
     */
    @Override
    public JsonBean getFinanceVersionByCollectionTask(String collectionTaskId) {
        try {
            log.info("========== 开始查询采集任务关联的财务版本信息 ==========");
            log.info("输入参数: collectionTaskId={}", collectionTaskId);

            // 1. 根据采集任务ID查询采集任务,获取PLAN_ID
            log.info("步骤1: 查询采集任务表 TBL_COLLECTION_TASK, TASK_ID={}", collectionTaskId);
            CollectionTask collectionTask = collectionTaskMapper.selectById(collectionTaskId);

            if (collectionTask == null) {
                log.warn("❌ 采集任务不存在, collectionTaskId={}", collectionTaskId);
                return ResponseFormat.retParam(0, "采集任务不存在,taskId=" + collectionTaskId, null);
            }

            String planId = collectionTask.getPlanId();
            log.info("✅ 查询到采集任务, taskId={}, planId={}", collectionTaskId, planId);

            if (planId == null || planId.trim().isEmpty()) {
                log.warn("❌ 采集任务的PLAN_ID为空", collectionTaskId);
                return ResponseFormat.retParam(0, "采集任务的PLAN_ID为空", null);
            }

            // 2. 根据PLAN_ID查询BD_FINVERSION表 (PLAN_ID对应BD_FINVERSION.FID)
            log.info("步骤2: 查询财务版本表 BD_FINVERSION, FID={}", planId);
            BdFinversion finVersion = bdFinversionMapper.selectById(planId);

            if (finVersion == null) {
                log.warn("❌ 财务版本不存在, FID={}", planId);
                return ResponseFormat.retParam(0, "财务版本不存在,FID=" + planId, null);
            }

            log.info("✅ 查询到财务版本, FID={}, HANDTEXT={}", finVersion.getFid(), finVersion.getHandtext());

            // 3. 构造返回数据
            Map<String, Object> result = new HashMap<>();
            result.put("fid", finVersion.getFid());
            result.put("fname", finVersion.getHandtext());
            result.put("pid", finVersion.getPid());

            log.info("========== 查询完成,返回财务版本信息 ==========");
            return ResponseFormat.retParam(1, 200, result);

        } catch (Exception e) {
            log.error("查询财务版本信息失败, collectionTaskId={}", collectionTaskId, e);
            return ResponseFormat.retParam(0, "查询失败: " + e.getMessage(), null);
        }
    }

    /**
     * 根据财务版本FID查询转换配置
     * 查询BD_INIT_SQLCONFIG表,条件: FVERSIONID = versionFid
     * 返回字段: FID, FNAME, FVERSIONID等
     */
    @Override
    public JsonBean getTransformConfigByVersion(String versionFid) {
        try {
            log.info("========== 开始查询财务版本的转换配置 ==========");
            log.info("输入参数: versionFid={}", versionFid);
            log.info("查询表: BD_INIT_SQLCONFIG, 条件: FVERSIONID={}", versionFid);

            // 根据FVERSIONID查询BD_INIT_SQLCONFIG表
            QueryWrapper<BdInitSqlconfig> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("FVERSIONID", versionFid);
            queryWrapper.orderByAsc("FID");
            List<BdInitSqlconfig> configList = bdInitSqlconfigMapper.selectList(queryWrapper);

            if (configList == null || configList.isEmpty()) {
                log.info("❌ 未找到转换配置, versionFid={}", versionFid);
                return ResponseFormat.retParam(1, 200, new ArrayList<>());
            }

            log.info("✅ 查询到{}条转换配置", configList.size());
            for (int i = 0; i < configList.size(); i++) {
                BdInitSqlconfig config = configList.get(i);
                log.info("  配置{}: FID={}, FNAME={}, FVERSIONID={}",
                    (i + 1), config.getFid(), config.getFname(), config.getFversionid());
            }

            log.info("========== 查询完成,返回转换配置列表 ==========");
            return ResponseFormat.retParam(1, 200, configList);

        } catch (Exception e) {
            log.error("查询转换配置失败, versionFid={}", versionFid, e);
            return ResponseFormat.retParam(0, "查询失败: " + e.getMessage(), null);
        }
    }

    /**
     * 执行SQL配置转换(第一优先级)
     * 根据采集任务关联的财务版本,查询BD_INIT_SQLCONFIG表的配置并执行转换
     *
     * @param collectionTaskId 采集任务ID
     * @return 转换的记录数
     */
    private int executeSqlConfigTransform(String collectionTaskId) {
        int totalCount = 0;

        try {
            log.info("========== 开始执行SQL配置转换 ==========");
            log.info("采集任务ID: {}", collectionTaskId);

            // 1. 根据采集任务ID查询采集任务,获取PLAN_ID
            CollectionTask collectionTask = collectionTaskMapper.selectById(collectionTaskId);
            if (collectionTask == null) {
                log.warn("采集任务不存在, collectionTaskId={}", collectionTaskId);
                return 0;
            }

            String planId = collectionTask.getPlanId();
            if (planId == null || planId.trim().isEmpty()) {
                log.warn("采集任务的PLAN_ID为空, collectionTaskId={}", collectionTaskId);
                return 0;
            }

            log.info("采集任务PLAN_ID: {}", planId);

            // 2. 根据PLAN_ID查询BD_FINVERSION表(PLAN_ID对应BD_FINVERSION.FID)
            BdFinversion finVersion = bdFinversionMapper.selectById(planId);
            if (finVersion == null) {
                log.warn("财务版本不存在, FID={}", planId);
                return 0;
            }

            String versionFid = finVersion.getFid();
            log.info("财务版本FID: {}, FNAME: {}", versionFid, finVersion.getHandtext());

            // 3. 根据财务版本FID查询BD_INIT_SQLCONFIG表的配置
            QueryWrapper<BdInitSqlconfig> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("FVERSIONID", versionFid);
            queryWrapper.orderByAsc("FID");
            List<BdInitSqlconfig> configList = bdInitSqlconfigMapper.selectList(queryWrapper);

            if (configList == null || configList.isEmpty()) {
                log.info("未找到SQL转换配置, versionFid={}", versionFid);
                return 0;
            }

            log.info("找到{}条SQL转换配置", configList.size());

            // 4. 逐个执行SQL配置
            for (int i = 0; i < configList.size(); i++) {
                BdInitSqlconfig config = configList.get(i);
                log.info("---------- 执行第{}条SQL配置 ----------", (i + 1));
                log.info("配置名称: {}", config.getFname());
                log.info("目标表: {}", config.getFtable());
                log.info("查询SQL: {}", config.getFinitsql());
                log.info("补充条件: {}", config.getFinSpecificityCol());

                try {
                    int count = executeSingleSqlConfig(config);
                    totalCount += count;
                    log.info("第{}条SQL配置执行成功,插入{}条记录", (i + 1), count);
                } catch (Exception e) {
                    log.error("第{}条SQL配置执行失败: {}", (i + 1), config.getFname(), e);
                    // 继续执行下一条配置
                }
            }

            log.info("========== SQL配置转换完成,共转换{}条记录 ==========", totalCount);

        } catch (Exception e) {
            log.error("执行SQL配置转换失败", e);
        }

        return totalCount;
    }

    /**
     * 执行单个SQL配置
     *
     * @param config SQL配置
     * @return 插入的记录数
     */
    private int executeSingleSqlConfig(BdInitSqlconfig config) throws Exception {
        String targetTable = config.getFtable();
        String querySql = config.getFinitsql();
        String additionalCondition = config.getFinSpecificityCol();

        if (targetTable == null || targetTable.trim().isEmpty()) {
            throw new Exception("目标表名为空");
        }

        if (querySql == null || querySql.trim().isEmpty()) {
            throw new Exception("查询SQL为空");
        }

        // 如果有补充条件,追加到查询SQL
        String finalSql = querySql;
        if (additionalCondition != null && !additionalCondition.trim().isEmpty()) {
            String trimmedCondition = additionalCondition.trim();

            // 检查补充条件是否以AND或OR开头(不区分大小写)
            boolean startsWithAnd = trimmedCondition.toUpperCase().startsWith("AND ");
            boolean startsWithOr = trimmedCondition.toUpperCase().startsWith("OR ");

            if (querySql.toUpperCase().contains("WHERE")) {
                // SQL中已经有WHERE子句
                if (startsWithAnd || startsWithOr) {
                    // 补充条件已经以AND/OR开头,直接拼接
                    finalSql = querySql + " " + trimmedCondition;
                } else {
                    // 补充条件没有AND/OR,需要添加AND
                    finalSql = querySql + " AND " + trimmedCondition;
                }
            } else {
                // SQL中没有WHERE子句
                if (startsWithAnd) {
                    // 去掉开头的AND,使用WHERE
                    String conditionWithoutAnd = trimmedCondition.substring(3).trim();
                    finalSql = querySql + " WHERE " + conditionWithoutAnd;
                } else if (startsWithOr) {
                    // 去掉开头的OR,使用WHERE
                    String conditionWithoutOr = trimmedCondition.substring(2).trim();
                    finalSql = querySql + " WHERE " + conditionWithoutOr;
                } else {
                    // 直接使用WHERE
                    finalSql = querySql + " WHERE " + trimmedCondition;
                }
            }
        }

        log.info("最终查询SQL: {}", finalSql);

        // 执行查询
        List<Map<String, Object>> sourceData = jdbcTemplate.queryForList(finalSql);
        log.info("查询到{}条源数据", sourceData.size());

        if (sourceData.isEmpty()) {
            return 0;
        }

        // 打印第一条数据用于调试
        if (!sourceData.isEmpty()) {
            log.info("第一条查询结果: {}", sourceData.get(0));
        }

        // 构建插入SQL
        Map<String, Object> firstRow = sourceData.get(0);
        List<String> fieldNames = new ArrayList<>(firstRow.keySet());

        StringBuilder insertSql = new StringBuilder("INSERT INTO ");
        insertSql.append(targetTable).append(" (");
        for (int i = 0; i < fieldNames.size(); i++) {
            if (i > 0) insertSql.append(", ");
            insertSql.append(fieldNames.get(i));
        }
        insertSql.append(") VALUES (");
        for (int i = 0; i < fieldNames.size(); i++) {
            if (i > 0) insertSql.append(", ");
            insertSql.append("?");
        }
        insertSql.append(")");

        log.info("插入SQL: {}", insertSql.toString());

        // 批量插入数据
        List<Object[]> batchArgs = new ArrayList<>();
        for (Map<String, Object> row : sourceData) {
            Object[] args = new Object[fieldNames.size()];
            for (int i = 0; i < fieldNames.size(); i++) {
                args[i] = row.get(fieldNames.get(i));
            }
            batchArgs.add(args);
        }

        int[] results = jdbcTemplate.batchUpdate(insertSql.toString(), batchArgs);
        return results.length;
    }
}

