package com.huabo.finance.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hbfk.entity.TblStaffUtil;
import com.hbfk.util.JsonBean;
import com.hbfk.util.redis.Random.RandomUtil;
import com.hbfk.util.ResponseFormat;
import com.huabo.finance.entity.TransformRule;
import com.huabo.finance.mapper.GatherFinanceDataMapper;
import com.huabo.finance.mapper.TransformRuleMapper;
import com.huabo.finance.service.TransformRuleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 转化规则服务实现类
 * 
 * @author 华博云开发团队
 * @since 2025-01-21
 */
@Slf4j
@Service
public class TransformRuleServiceImpl extends ServiceImpl<TransformRuleMapper, TransformRule> 
        implements TransformRuleService {

    @Resource
    private TransformRuleMapper transformRuleMapper;

    @Resource
    private GatherFinanceDataMapper gatherFinanceDataMapper;

    @Override
    public JsonBean saveTransformRule(TblStaffUtil staff, TransformRule rule) throws Exception {
        if (StringUtils.isNotBlank(rule.getRuleId())) {
            // 更新
            rule.setUpdateTime(new Date());
            transformRuleMapper.updateById(rule);
        } else {
            // 新增
            rule.setRuleId(RandomUtil.uuStringId());
            rule.setCreateTime(new Date());
            rule.setIsEnabled("1"); // 默认启用
            transformRuleMapper.insert(rule);
        }
        return ResponseFormat.retParam(1, "保存成功", rule);
    }

    @Override
    public JsonBean getRulesByCollectionTaskId(String collectionTaskId) throws Exception {
        List<TransformRule> ruleList = transformRuleMapper.selectByCollectionTaskId(collectionTaskId);
        return ResponseFormat.retParam(1, 200, ruleList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public JsonBean executeTransformRule(TransformRule rule) throws Exception {
        log.info("开始执行转化规则[{}]: {} -> {}", rule.getRuleId(), rule.getSourceTable(), rule.getTargetTable());
        
        try {
            // 1. 生成转化SQL
            String sql = generateTransformSql(rule);
            log.info("生成的转化SQL: {}", sql);

            // 2. 执行SQL
            gatherFinanceDataMapper.executeInsertSql(sql);

            log.info("转化规则[{}]执行成功", rule.getRuleId());
            return ResponseFormat.retParam(1, "执行成功", null);

        } catch (Exception e) {
            log.error("转化规则[{}]执行失败", rule.getRuleId(), e);
            throw new Exception("转化规则执行失败: " + e.getMessage(), e);
        }
    }

    @Override
    public JsonBean previewTransformSql(TransformRule rule) throws Exception {
        String sql = generateTransformSql(rule);
        return ResponseFormat.retParam(1, 200, sql);
    }

    @Override
    public JsonBean deleteTransformRule(String ruleId) throws Exception {
        transformRuleMapper.deleteById(ruleId);
        return ResponseFormat.retParam(1, "删除成功", null);
    }

    /**
     * 生成转化SQL
     */
    private String generateTransformSql(TransformRule rule) throws Exception {
        String ruleType = rule.getRuleType();
        String ruleConfig = rule.getRuleConfig();

        if ("DIRECT".equals(ruleType)) {
            // 直接映射
            return generateDirectSql(rule, ruleConfig);
        } else if ("CASE_WHEN".equals(ruleType)) {
            // CASE WHEN条件转换
            return generateCaseWhenSql(rule, ruleConfig);
        } else if ("GROUP_BY".equals(ruleType)) {
            // GROUP BY分组聚合
            return generateGroupBySql(rule, ruleConfig);
        } else if ("CUSTOM_SQL".equals(ruleType)) {
            // 自定义SQL
            return generateCustomSql(rule, ruleConfig);
        } else {
            throw new Exception("不支持的规则类型: " + ruleType);
        }
    }

    /**
     * 生成直接映射SQL
     */
    private String generateDirectSql(TransformRule rule, String ruleConfig) throws Exception {
        JSONObject config = JSON.parseObject(ruleConfig);
        JSONArray fieldMappings = config.getJSONArray("fieldMappings");

        StringBuilder selectFields = new StringBuilder();
        for (int i = 0; i < fieldMappings.size(); i++) {
            JSONObject mapping = fieldMappings.getJSONObject(i);
            String sourceField = mapping.getString("sourceField");
            String targetField = mapping.getString("targetField");
            
            if (i > 0) {
                selectFields.append(", ");
            }
            selectFields.append(sourceField).append(" AS ").append(targetField);
        }

        String sql = String.format("INSERT INTO %s SELECT %s FROM %s",
            rule.getTargetTable(), selectFields.toString(), rule.getSourceTable());

        // 添加WHERE条件
        String whereCondition = config.getString("whereCondition");
        if (StringUtils.isNotBlank(whereCondition)) {
            sql += " WHERE " + whereCondition;
        }

        return sql;
    }

    /**
     * 生成CASE WHEN SQL
     */
    private String generateCaseWhenSql(TransformRule rule, String ruleConfig) throws Exception {
        JSONObject config = JSON.parseObject(ruleConfig);
        JSONArray fieldMappings = config.getJSONArray("fieldMappings");

        StringBuilder selectFields = new StringBuilder();
        for (int i = 0; i < fieldMappings.size(); i++) {
            JSONObject mapping = fieldMappings.getJSONObject(i);
            String sourceField = mapping.getString("sourceField");
            String targetField = mapping.getString("targetField");
            JSONArray caseWhenRules = mapping.getJSONArray("caseWhenRules");

            if (i > 0) {
                selectFields.append(", ");
            }

            if (caseWhenRules != null && caseWhenRules.size() > 0) {
                // 有CASE WHEN规则
                selectFields.append("CASE ");
                for (int j = 0; j < caseWhenRules.size(); j++) {
                    JSONObject caseRule = caseWhenRules.getJSONObject(j);
                    String condition = caseRule.getString("condition");
                    String value = caseRule.getString("value");
                    selectFields.append("WHEN ").append(condition).append(" THEN ").append(value).append(" ");
                }
                String elseValue = mapping.getString("elseValue");
                if (StringUtils.isNotBlank(elseValue)) {
                    selectFields.append("ELSE ").append(elseValue).append(" ");
                }
                selectFields.append("END AS ").append(targetField);
            } else {
                // 直接映射
                selectFields.append(sourceField).append(" AS ").append(targetField);
            }
        }

        String sql = String.format("INSERT INTO %s SELECT %s FROM %s",
            rule.getTargetTable(), selectFields.toString(), rule.getSourceTable());

        // 添加WHERE条件
        String whereCondition = config.getString("whereCondition");
        if (StringUtils.isNotBlank(whereCondition)) {
            sql += " WHERE " + whereCondition;
        }

        return sql;
    }

    /**
     * 生成GROUP BY SQL
     */
    private String generateGroupBySql(TransformRule rule, String ruleConfig) throws Exception {
        JSONObject config = JSON.parseObject(ruleConfig);
        JSONArray groupByFields = config.getJSONArray("groupByFields");
        JSONArray aggregateFields = config.getJSONArray("aggregateFields");

        StringBuilder selectFields = new StringBuilder();
        
        // 分组字段
        for (int i = 0; i < groupByFields.size(); i++) {
            if (i > 0) {
                selectFields.append(", ");
            }
            selectFields.append(groupByFields.getString(i));
        }

        // 聚合字段
        for (int i = 0; i < aggregateFields.size(); i++) {
            JSONObject aggField = aggregateFields.getJSONObject(i);
            String sourceField = aggField.getString("sourceField");
            String targetField = aggField.getString("targetField");
            String aggFunc = aggField.getString("aggregateFunc"); // SUM/AVG/MAX/MIN/COUNT

            selectFields.append(", ").append(aggFunc).append("(").append(sourceField).append(") AS ").append(targetField);
        }

        StringBuilder groupByClause = new StringBuilder();
        for (int i = 0; i < groupByFields.size(); i++) {
            if (i > 0) {
                groupByClause.append(", ");
            }
            groupByClause.append(groupByFields.getString(i));
        }

        String sql = String.format("INSERT INTO %s SELECT %s FROM %s GROUP BY %s",
            rule.getTargetTable(), selectFields.toString(), rule.getSourceTable(), groupByClause.toString());

        // 添加WHERE条件
        String whereCondition = config.getString("whereCondition");
        if (StringUtils.isNotBlank(whereCondition)) {
            sql = String.format("INSERT INTO %s SELECT %s FROM %s WHERE %s GROUP BY %s",
                rule.getTargetTable(), selectFields.toString(), rule.getSourceTable(), 
                whereCondition, groupByClause.toString());
        }

        return sql;
    }

    /**
     * 生成自定义SQL
     * 支持用户直接编写完整的INSERT INTO ... SELECT SQL
     */
    private String generateCustomSql(TransformRule rule, String ruleConfig) throws Exception {
        JSONObject config = JSON.parseObject(ruleConfig);
        String customSql = config.getString("sql");

        if (StringUtils.isBlank(customSql)) {
            throw new Exception("自定义SQL不能为空");
        }

        // SQL安全验证
        validateCustomSql(customSql);

        // 参数替换
        if (config.containsKey("parameters")) {
            JSONObject params = config.getJSONObject("parameters");
            for (String key : params.keySet()) {
                String value = params.getString(key);
                customSql = customSql.replace("${" + key + "}", value);
            }
        }

        log.info("生成自定义SQL: {}", customSql);
        return customSql;
    }

    /**
     * 验证自定义SQL的安全性
     */
    private void validateCustomSql(String sql) throws Exception {
        String upperSql = sql.trim().toUpperCase();

        // 只允许INSERT INTO ... SELECT语句
        if (!upperSql.startsWith("INSERT INTO")) {
            throw new Exception("自定义SQL必须以INSERT INTO开头");
        }

        if (!upperSql.contains("SELECT")) {
            throw new Exception("自定义SQL必须包含SELECT子句");
        }

        // 禁止危险操作
        String[] dangerousKeywords = {"DROP", "TRUNCATE", "DELETE", "UPDATE", "ALTER", "CREATE", "GRANT", "REVOKE"};
        for (String keyword : dangerousKeywords) {
            if (upperSql.contains(keyword)) {
                throw new Exception("自定义SQL不允许包含危险操作: " + keyword);
            }
        }

        // 禁止多语句执行
        if (upperSql.contains(";")) {
            throw new Exception("自定义SQL不允许包含分号,禁止执行多条语句");
        }

        log.info("自定义SQL安全验证通过");
    }
}

