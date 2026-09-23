package com.financial.sharing.dataCollection.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.financial.sharing.dataCollection.entity.TblDataSource;
import com.financial.sharing.dataCollection.entity.TblMappingRule;
import com.financial.sharing.dataCollection.mapper.DataSourceMapper;
import com.financial.sharing.dataCollection.mapper.MappingRuleMapper;
import com.financial.sharing.dataCollection.service.MappingRuleTestService;
import com.financial.sharing.util.MyJsonBean;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
import java.util.regex.Pattern;

/**
 * 映射规则测试Service实现类
 *
 * @author Augment Agent
 * @date 2026-02-02
 */
@Slf4j
@Service
public class MappingRuleTestServiceImpl implements MappingRuleTestService {

    @Autowired
    private MappingRuleMapper mappingRuleMapper;

    @Autowired
    private DataSourceMapper dataSourceMapper;

    @Override
    public MyJsonBean testFieldMapping(TblMappingRule mappingRule, Map<String, Object> testData) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("ruleCode", mappingRule.getRuleCode());
            result.put("ruleName", mappingRule.getRuleName());

            // 解析字段映射
            String fieldMappingsStr = mappingRule.getFieldMappings();
            if (StringUtils.isBlank(fieldMappingsStr)) {
                return MyJsonBean.errorData("字段映射配置为空");
            }

            JSONArray fieldMappings = JSON.parseArray(fieldMappingsStr);
            Map<String, Object> mappedData = new HashMap<>();
            List<Map<String, Object>> mappingDetails = new ArrayList<>();

            for (int i = 0; i < fieldMappings.size(); i++) {
                JSONObject mapping = fieldMappings.getJSONObject(i);
                String sourceField = mapping.getString("source");
                String targetField = mapping.getString("target");

                Map<String, Object> detail = new HashMap<>();
                detail.put("sourceField", sourceField);
                detail.put("targetField", targetField);

                if (testData.containsKey(sourceField)) {
                    Object value = testData.get(sourceField);
                    mappedData.put(targetField, value);
                    detail.put("sourceValue", value);
                    detail.put("targetValue", value);
                    detail.put("status", "SUCCESS");
                    detail.put("message", "映射成功");
                } else {
                    detail.put("sourceValue", null);
                    detail.put("targetValue", null);
                    detail.put("status", "WARNING");
                    detail.put("message", "源字段不存在");
                }

                mappingDetails.add(detail);
            }

            result.put("testData", testData);
            result.put("mappedData", mappedData);
            result.put("mappingDetails", mappingDetails);
            result.put("totalFields", fieldMappings.size());
            result.put("successCount", mappingDetails.stream().filter(d -> "SUCCESS".equals(d.get("status"))).count());
            result.put("warningCount", mappingDetails.stream().filter(d -> "WARNING".equals(d.get("status"))).count());

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("测试字段映射失败", e);
            return MyJsonBean.errorData("测试字段映射失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean testTransformRules(TblMappingRule mappingRule, Map<String, Object> testData) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("ruleCode", mappingRule.getRuleCode());
            result.put("ruleName", mappingRule.getRuleName());

            // 解析转换规则
            String transformRulesStr = mappingRule.getTransformRules();
            if (StringUtils.isBlank(transformRulesStr)) {
                result.put("message", "转换规则配置为空");
                result.put("transformedData", testData);
                return MyJsonBean.successData(result);
            }

            JSONArray transformRules = JSON.parseArray(transformRulesStr);
            Map<String, Object> transformedData = new HashMap<>(testData);
            List<Map<String, Object>> transformDetails = new ArrayList<>();

            for (int i = 0; i < transformRules.size(); i++) {
                JSONObject rule = transformRules.getJSONObject(i);
                String field = rule.getString("field");
                String type = rule.getString("type");
                Object value = rule.get("value");

                Map<String, Object> detail = new HashMap<>();
                detail.put("field", field);
                detail.put("type", type);
                detail.put("ruleValue", value);

                if (!transformedData.containsKey(field)) {
                    detail.put("status", "WARNING");
                    detail.put("message", "字段不存在");
                    transformDetails.add(detail);
                    continue;
                }

                Object originalValue = transformedData.get(field);
                detail.put("originalValue", originalValue);

                try {
                    Object transformedValue = applyTransform(originalValue, type, value);
                    transformedData.put(field, transformedValue);
                    detail.put("transformedValue", transformedValue);
                    detail.put("status", "SUCCESS");
                    detail.put("message", "转换成功");
                } catch (Exception e) {
                    detail.put("status", "ERROR");
                    detail.put("message", "转换失败：" + e.getMessage());
                }

                transformDetails.add(detail);
            }

            result.put("testData", testData);
            result.put("transformedData", transformedData);
            result.put("transformDetails", transformDetails);
            result.put("totalRules", transformRules.size());
            result.put("successCount", transformDetails.stream().filter(d -> "SUCCESS".equals(d.get("status"))).count());
            result.put("errorCount", transformDetails.stream().filter(d -> "ERROR".equals(d.get("status"))).count());

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("测试转换规则失败", e);
            return MyJsonBean.errorData("测试转换规则失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean testValidationRules(TblMappingRule mappingRule, Map<String, Object> testData) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("ruleCode", mappingRule.getRuleCode());
            result.put("ruleName", mappingRule.getRuleName());

            // 解析校验规则
            String validationRulesStr = mappingRule.getValidationRules();
            if (StringUtils.isBlank(validationRulesStr)) {
                result.put("message", "校验规则配置为空");
                result.put("valid", true);
                return MyJsonBean.successData(result);
            }

            JSONArray validationRules = JSON.parseArray(validationRulesStr);
            List<Map<String, Object>> validationDetails = new ArrayList<>();
            boolean allValid = true;

            for (int i = 0; i < validationRules.size(); i++) {
                JSONObject rule = validationRules.getJSONObject(i);
                String field = rule.getString("field");
                String type = rule.getString("type");

                Map<String, Object> detail = new HashMap<>();
                detail.put("field", field);
                detail.put("type", type);

                if (!testData.containsKey(field)) {
                    detail.put("value", null);
                    detail.put("valid", false);
                    detail.put("message", "字段不存在");
                    allValid = false;
                    validationDetails.add(detail);
                    continue;
                }

                Object value = testData.get(field);
                detail.put("value", value);

                boolean isValid = validateValue(value, type);
                detail.put("valid", isValid);
                detail.put("message", isValid ? "校验通过" : "校验失败");

                if (!isValid) {
                    allValid = false;
                }

                validationDetails.add(detail);
            }

            result.put("testData", testData);
            result.put("validationDetails", validationDetails);
            result.put("totalRules", validationRules.size());
            result.put("passCount", validationDetails.stream().filter(d -> (Boolean) d.get("valid")).count());
            result.put("failCount", validationDetails.stream().filter(d -> !(Boolean) d.get("valid")).count());
            result.put("valid", allValid);

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("测试校验规则失败", e);
            return MyJsonBean.errorData("测试校验规则失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean testFilterCondition(TblMappingRule mappingRule, Map<String, Object> testData) {
        try {
            Map<String, Object> result = new HashMap<>();
            result.put("ruleCode", mappingRule.getRuleCode());
            result.put("ruleName", mappingRule.getRuleName());

            String filterCondition = mappingRule.getFilterCondition();
            if (StringUtils.isBlank(filterCondition)) {
                result.put("message", "过滤条件为空");
                result.put("passed", true);
                return MyJsonBean.successData(result);
            }

            result.put("filterCondition", filterCondition);
            result.put("testData", testData);

            // 简单的过滤条件评估（实际应用中可能需要更复杂的表达式解析）
            boolean passed = evaluateFilterCondition(filterCondition, testData);
            result.put("passed", passed);
            result.put("message", passed ? "通过过滤条件" : "未通过过滤条件");

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("测试过滤条件失败", e);
            return MyJsonBean.errorData("测试过滤条件失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean testCompleteRule(String ruleId, String orgId, Map<String, Object> testData) {
        try {
            // 查询映射规则
            LambdaQueryWrapper<TblMappingRule> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(TblMappingRule::getRuleId, ruleId);
            wrapper.eq(TblMappingRule::getOrgId, orgId);

            TblMappingRule mappingRule = mappingRuleMapper.selectOne(wrapper);
            if (mappingRule == null) {
                return MyJsonBean.errorData("映射规则不存在");
            }

            Map<String, Object> result = new HashMap<>();
            result.put("ruleCode", mappingRule.getRuleCode());
            result.put("ruleName", mappingRule.getRuleName());
            result.put("testData", testData);

            // 1. 测试字段映射
            MyJsonBean fieldMappingResult = testFieldMapping(mappingRule, testData);
            result.put("fieldMappingTest", fieldMappingResult.getData());

            // 2. 测试转换规则
            Map<String, Object> mappedData = (Map<String, Object>) ((Map<String, Object>) fieldMappingResult.getData()).get("mappedData");
            MyJsonBean transformResult = testTransformRules(mappingRule, mappedData);
            result.put("transformTest", transformResult.getData());

            // 3. 测试校验规则
            Map<String, Object> transformedData = (Map<String, Object>) ((Map<String, Object>) transformResult.getData()).get("transformedData");
            MyJsonBean validationResult = testValidationRules(mappingRule, transformedData);
            result.put("validationTest", validationResult.getData());

            // 4. 测试过滤条件
            MyJsonBean filterResult = testFilterCondition(mappingRule, testData);
            result.put("filterTest", filterResult.getData());

            // 汇总结果
            boolean allPassed = fieldMappingResult.getCode() == 200
                && transformResult.getCode() == 200
                && validationResult.getCode() == 200
                && filterResult.getCode() == 200;

            result.put("allPassed", allPassed);
            result.put("finalData", transformedData);

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("完整规则测试失败", e);
            return MyJsonBean.errorData("完整规则测试失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean batchTestRules(String[] ruleIds, String orgId, Map<String, Object> testData) {
        try {
            List<Map<String, Object>> results = new ArrayList<>();
            int successCount = 0;
            int failedCount = 0;

            for (String ruleId : ruleIds) {
                Map<String, Object> testResult = new HashMap<>();
                testResult.put("ruleId", ruleId);

                try {
                    MyJsonBean result = testCompleteRule(ruleId, orgId, testData);
                    if (result.getCode() == 200) {
                        testResult.put("status", "SUCCESS");
                        testResult.put("result", result.getData());
                        successCount++;
                    } else {
                        testResult.put("status", "FAILED");
                        testResult.put("message", result.getMsg());
                        failedCount++;
                    }
                } catch (Exception e) {
                    testResult.put("status", "ERROR");
                    testResult.put("message", e.getMessage());
                    failedCount++;
                }

                results.add(testResult);
            }

            Map<String, Object> summary = new HashMap<>();
            summary.put("total", ruleIds.length);
            summary.put("successCount", successCount);
            summary.put("failedCount", failedCount);
            summary.put("results", results);

            return MyJsonBean.successData(summary);
        } catch (Exception e) {
            log.error("批量测试规则失败", e);
            return MyJsonBean.errorData("批量测试规则失败：" + e.getMessage());
        }
    }

    @Override
    public MyJsonBean previewMappingResult(String ruleId, String orgId, Integer limit) {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // 查询映射规则
            LambdaQueryWrapper<TblMappingRule> ruleWrapper = new LambdaQueryWrapper<>();
            ruleWrapper.eq(TblMappingRule::getRuleId, ruleId);
            ruleWrapper.eq(TblMappingRule::getOrgId, orgId);

            TblMappingRule mappingRule = mappingRuleMapper.selectOne(ruleWrapper);
            if (mappingRule == null) {
                return MyJsonBean.errorData("映射规则不存在");
            }

            // 查询数据源
            LambdaQueryWrapper<TblDataSource> sourceWrapper = new LambdaQueryWrapper<>();
            sourceWrapper.eq(TblDataSource::getSourceId, mappingRule.getSourceId());
            sourceWrapper.eq(TblDataSource::getOrgId, orgId);

            TblDataSource dataSource = dataSourceMapper.selectOne(sourceWrapper);
            if (dataSource == null) {
                return MyJsonBean.errorData("数据源不存在");
            }

            if (!"DATABASE".equals(dataSource.getSourceType())) {
                return MyJsonBean.errorData("只支持数据库类型的数据源");
            }

            // 构建JDBC URL
            String jdbcUrl = buildJdbcUrl(dataSource);
            conn = DriverManager.getConnection(jdbcUrl, dataSource.getUsername(), dataSource.getPassword());

            // 执行源查询
            String sourceQuery = mappingRule.getSourceQuery();
            if (StringUtils.isBlank(sourceQuery)) {
                return MyJsonBean.errorData("源查询语句为空");
            }

            stmt = conn.createStatement();
            stmt.setMaxRows(limit != null ? limit : 10);
            rs = stmt.executeQuery(sourceQuery);

            // 获取列信息
            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            List<String> sourceColumns = new ArrayList<>();
            for (int i = 1; i <= columnCount; i++) {
                sourceColumns.add(metaData.getColumnName(i));
            }

            // 获取数据并应用映射
            List<Map<String, Object>> sourceData = new ArrayList<>();
            List<Map<String, Object>> mappedData = new ArrayList<>();

            while (rs.next()) {
                Map<String, Object> sourceRow = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    sourceRow.put(metaData.getColumnName(i), rs.getObject(i));
                }
                sourceData.add(sourceRow);

                // 应用字段映射
                Map<String, Object> mappedRow = applyFieldMapping(mappingRule, sourceRow);
                // 应用转换规则
                mappedRow = applyTransformRules(mappingRule, mappedRow);
                mappedData.add(mappedRow);
            }

            Map<String, Object> result = new HashMap<>();
            result.put("ruleCode", mappingRule.getRuleCode());
            result.put("ruleName", mappingRule.getRuleName());
            result.put("sourceColumns", sourceColumns);
            result.put("sourceData", sourceData);
            result.put("mappedData", mappedData);
            result.put("recordCount", sourceData.size());

            return MyJsonBean.successData(result);
        } catch (Exception e) {
            log.error("预览映射结果失败", e);
            return MyJsonBean.errorData("预览映射结果失败：" + e.getMessage());
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (Exception e) {
                    log.error("关闭ResultSet失败", e);
                }
            }
            if (stmt != null) {
                try {
                    stmt.close();
                } catch (Exception e) {
                    log.error("关闭Statement失败", e);
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (Exception e) {
                    log.error("关闭数据库连接失败", e);
                }
            }
        }
    }

    /**
     * 应用字段映射
     */
    private Map<String, Object> applyFieldMapping(TblMappingRule mappingRule, Map<String, Object> sourceData) {
        Map<String, Object> mappedData = new HashMap<>();

        try {
            String fieldMappingsStr = mappingRule.getFieldMappings();
            if (StringUtils.isBlank(fieldMappingsStr)) {
                return sourceData;
            }

            JSONArray fieldMappings = JSON.parseArray(fieldMappingsStr);
            for (int i = 0; i < fieldMappings.size(); i++) {
                JSONObject mapping = fieldMappings.getJSONObject(i);
                String sourceField = mapping.getString("source");
                String targetField = mapping.getString("target");

                if (sourceData.containsKey(sourceField)) {
                    mappedData.put(targetField, sourceData.get(sourceField));
                }
            }
        } catch (Exception e) {
            log.error("应用字段映射失败", e);
        }

        return mappedData;
    }

    /**
     * 应用转换规则
     */
    private Map<String, Object> applyTransformRules(TblMappingRule mappingRule, Map<String, Object> data) {
        Map<String, Object> transformedData = new HashMap<>(data);

        try {
            String transformRulesStr = mappingRule.getTransformRules();
            if (StringUtils.isBlank(transformRulesStr)) {
                return transformedData;
            }

            JSONArray transformRules = JSON.parseArray(transformRulesStr);
            for (int i = 0; i < transformRules.size(); i++) {
                JSONObject rule = transformRules.getJSONObject(i);
                String field = rule.getString("field");
                String type = rule.getString("type");
                Object value = rule.get("value");

                if (transformedData.containsKey(field)) {
                    Object originalValue = transformedData.get(field);
                    Object transformedValue = applyTransform(originalValue, type, value);
                    transformedData.put(field, transformedValue);
                }
            }
        } catch (Exception e) {
            log.error("应用转换规则失败", e);
        }

        return transformedData;
    }

    /**
     * 应用转换
     */
    private Object applyTransform(Object value, String type, Object ruleValue) throws Exception {
        if (value == null) {
            return null;
        }

        switch (type) {
            case "multiply":
                // 乘法
                BigDecimal num1 = new BigDecimal(value.toString());
                BigDecimal multiplier = new BigDecimal(ruleValue.toString());
                return num1.multiply(multiplier);

            case "divide":
                // 除法
                BigDecimal num2 = new BigDecimal(value.toString());
                BigDecimal divisor = new BigDecimal(ruleValue.toString());
                return num2.divide(divisor, 2, BigDecimal.ROUND_HALF_UP);

            case "add":
                // 加法
                BigDecimal num3 = new BigDecimal(value.toString());
                BigDecimal addend = new BigDecimal(ruleValue.toString());
                return num3.add(addend);

            case "subtract":
                // 减法
                BigDecimal num4 = new BigDecimal(value.toString());
                BigDecimal subtrahend = new BigDecimal(ruleValue.toString());
                return num4.subtract(subtrahend);

            case "format":
                // 格式化（日期）
                if (value instanceof Date) {
                    SimpleDateFormat sdf = new SimpleDateFormat(ruleValue.toString());
                    return sdf.format((Date) value);
                }
                return value;

            case "uppercase":
                // 转大写
                return value.toString().toUpperCase();

            case "lowercase":
                // 转小写
                return value.toString().toLowerCase();

            case "trim":
                // 去空格
                return value.toString().trim();

            case "substring":
                // 截取字符串
                String str = value.toString();
                String[] params = ruleValue.toString().split(",");
                int start = Integer.parseInt(params[0]);
                int end = params.length > 1 ? Integer.parseInt(params[1]) : str.length();
                return str.substring(start, Math.min(end, str.length()));

            default:
                return value;
        }
    }

    /**
     * 校验值
     */
    private boolean validateValue(Object value, String type) {
        if (value == null) {
            return !"required".equals(type);
        }

        String strValue = value.toString();

        switch (type) {
            case "required":
                // 必填
                return StringUtils.isNotBlank(strValue);

            case "number":
                // 数字
                try {
                    new BigDecimal(strValue);
                    return true;
                } catch (Exception e) {
                    return false;
                }

            case "integer":
                // 整数
                try {
                    Integer.parseInt(strValue);
                    return true;
                } catch (Exception e) {
                    return false;
                }

            case "email":
                // 邮箱
                String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
                return Pattern.matches(emailRegex, strValue);

            case "phone":
                // 手机号
                String phoneRegex = "^1[3-9]\\d{9}$";
                return Pattern.matches(phoneRegex, strValue);

            case "url":
                // URL
                String urlRegex = "^(http|https)://.*$";
                return Pattern.matches(urlRegex, strValue);

            case "date":
                // 日期
                try {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    sdf.parse(strValue);
                    return true;
                } catch (Exception e) {
                    return false;
                }

            case "positive":
                // 正数
                try {
                    return new BigDecimal(strValue).compareTo(BigDecimal.ZERO) > 0;
                } catch (Exception e) {
                    return false;
                }

            case "negative":
                // 负数
                try {
                    return new BigDecimal(strValue).compareTo(BigDecimal.ZERO) < 0;
                } catch (Exception e) {
                    return false;
                }

            default:
                return true;
        }
    }

    /**
     * 评估过滤条件
     */
    private boolean evaluateFilterCondition(String condition, Map<String, Object> data) {
        // 简单的条件评估实现
        // 实际应用中可能需要更复杂的表达式解析器
        try {
            // 支持简单的等于判断：field = 'value'
            if (condition.contains("=")) {
                String[] parts = condition.split("=");
                if (parts.length == 2) {
                    String field = parts[0].trim();
                    String expectedValue = parts[1].trim().replace("'", "").replace("\"", "");

                    if (data.containsKey(field)) {
                        Object actualValue = data.get(field);
                        return expectedValue.equals(String.valueOf(actualValue));
                    }
                }
            }

            // 默认返回true
            return true;
        } catch (Exception e) {
            log.error("评估过滤条件失败", e);
            return false;
        }
    }

    /**
     * 构建JDBC连接URL
     */
    private String buildJdbcUrl(TblDataSource dataSource) {
        String connectionType = dataSource.getConnectionType();
        String host = dataSource.getHost();
        Integer port = dataSource.getPort();
        String databaseName = dataSource.getDatabaseName();

        if ("JDBC".equals(connectionType)) {
            // 根据不同数据库类型构建URL
            if (databaseName != null && databaseName.toLowerCase().contains("dm")) {
                // 达梦数据库
                return String.format("jdbc:dm://%s:%d/%s", host, port, databaseName);
            } else if (databaseName != null && databaseName.toLowerCase().contains("mysql")) {
                // MySQL数据库
                return String.format("jdbc:mysql://%s:%d/%s?useUnicode=true&characterEncoding=utf8", host, port, databaseName);
            } else if (databaseName != null && databaseName.toLowerCase().contains("oracle")) {
                // Oracle数据库
                return String.format("jdbc:oracle:thin:@%s:%d:%s", host, port, databaseName);
            } else {
                // 默认
                return String.format("jdbc://%s:%d/%s", host, port, databaseName);
            }
        }
        return "";
    }
}

