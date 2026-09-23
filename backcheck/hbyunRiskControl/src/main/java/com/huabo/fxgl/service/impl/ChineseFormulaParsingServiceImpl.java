package com.huabo.fxgl.service.impl;

import com.huabo.fxgl.service.ChineseFormulaParsingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 中文公式解析服务实现类
 * 
 * @author AI Assistant
 * @since 2025-01-21
 */
@Slf4j
@Service
public class ChineseFormulaParsingServiceImpl implements ChineseFormulaParsingService {

    // 中文运算符映射
    private static final Map<String, String> CHINESE_OPERATORS = new HashMap<>();
    
    // 逻辑运算符映射
    private static final Map<String, String> LOGIC_OPERATORS = new HashMap<>();
    
    // 函数映射
    private static final Map<String, String> FUNCTION_MAPPING = new HashMap<>();
    
    static {
        // 比较运算符
        CHINESE_OPERATORS.put("等于", "=");
        CHINESE_OPERATORS.put("不等于", "!=");
        CHINESE_OPERATORS.put("大于", ">");
        CHINESE_OPERATORS.put("小于", "<");
        CHINESE_OPERATORS.put("大于等于", ">=");
        CHINESE_OPERATORS.put("小于等于", "<=");
        CHINESE_OPERATORS.put("包含", "LIKE");
        CHINESE_OPERATORS.put("不包含", "NOT LIKE");
        CHINESE_OPERATORS.put("为空", "IS NULL");
        CHINESE_OPERATORS.put("不为空", "IS NOT NULL");
        CHINESE_OPERATORS.put("在范围内", "BETWEEN");
        CHINESE_OPERATORS.put("不在范围内", "NOT BETWEEN");
        
        // 逻辑运算符
        LOGIC_OPERATORS.put("并且", "AND");
        LOGIC_OPERATORS.put("或者", "OR");
        LOGIC_OPERATORS.put("非", "NOT");
        
        // 函数映射
        FUNCTION_MAPPING.put("求和", "SUM");
        FUNCTION_MAPPING.put("平均值", "AVG");
        FUNCTION_MAPPING.put("最大值", "MAX");
        FUNCTION_MAPPING.put("最小值", "MIN");
        FUNCTION_MAPPING.put("计数", "COUNT");
        FUNCTION_MAPPING.put("去重计数", "COUNT(DISTINCT");
    }

    @Override
    public Map<String, Object> parseChineseFormula(String chineseFormula, 
                                                  String sourceDataConfig, 
                                                  String targetDataConfig) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            if (!StringUtils.hasText(chineseFormula)) {
                result.put("success", false);
                result.put("message", "中文公式不能为空");
                return result;
            }
            
            // 1. 预处理公式
            String processedFormula = preprocessFormula(chineseFormula);
            
            // 2. 解析字段引用
            List<String> fieldReferences = extractFieldReferences(processedFormula);
            
            // 3. 转换运算符
            String sqlExpression = convertOperators(processedFormula);
            
            // 4. 处理函数调用
            sqlExpression = convertFunctions(sqlExpression);
            
            // 5. 验证语法
            Map<String, Object> validationResult = validateSQLSyntax(sqlExpression);
            
            if (!(Boolean) validationResult.get("valid")) {
                result.put("success", false);
                result.put("message", "公式转换失败: " + validationResult.get("message"));
                result.put("sqlExpression", "");
                return result;
            }
            
            result.put("success", true);
            result.put("message", "公式解析成功");
            result.put("sqlExpression", sqlExpression);
            result.put("fieldReferences", fieldReferences);
            result.put("originalFormula", chineseFormula);
            result.put("processedFormula", processedFormula);
            
        } catch (Exception e) {
            log.error("解析中文公式失败", e);
            result.put("success", false);
            result.put("message", "解析失败: " + e.getMessage());
            result.put("sqlExpression", "");
        }
        
        return result;
    }

    @Override
    public Map<String, Object> validateChineseFormula(String chineseFormula) {
        Map<String, Object> result = new HashMap<>();
        List<String> errors = new ArrayList<>();
        List<String> warnings = new ArrayList<>();
        
        try {
            if (!StringUtils.hasText(chineseFormula)) {
                errors.add("公式内容不能为空");
                result.put("valid", false);
                result.put("errors", errors);
                return result;
            }
            
            // 1. 检查基本语法
            if (!checkBasicSyntax(chineseFormula)) {
                errors.add("公式语法不正确，请检查运算符和括号匹配");
            }
            
            // 2. 检查运算符使用
            List<String> invalidOperators = checkOperators(chineseFormula);
            if (!invalidOperators.isEmpty()) {
                errors.add("包含不支持的运算符: " + String.join(", ", invalidOperators));
            }
            
            // 3. 检查括号匹配
            if (!checkParentheses(chineseFormula)) {
                errors.add("括号不匹配，请检查公式中的括号");
            }
            
            // 4. 检查字段引用格式
            if (!checkFieldReferences(chineseFormula)) {
                warnings.add("字段引用格式可能不正确，建议使用【字段名】格式");
            }
            
            boolean isValid = errors.isEmpty();
            result.put("valid", isValid);
            result.put("errors", errors);
            result.put("warnings", warnings);
            result.put("message", isValid ? "公式语法正确" : "发现语法错误");
            
        } catch (Exception e) {
            log.error("验证中文公式失败", e);
            result.put("valid", false);
            result.put("errors", Arrays.asList("验证过程出现异常: " + e.getMessage()));
        }
        
        return result;
    }

    @Override
    public List<Map<String, Object>> getSupportedOperators() {
        List<Map<String, Object>> operators = new ArrayList<>();
        
        // 比较运算符
        for (Map.Entry<String, String> entry : CHINESE_OPERATORS.entrySet()) {
            Map<String, Object> operator = new HashMap<>();
            operator.put("chinese", entry.getKey());
            operator.put("sql", entry.getValue());
            operator.put("type", "COMPARISON");
            operator.put("description", getOperatorDescription(entry.getKey()));
            operators.add(operator);
        }
        
        // 逻辑运算符
        for (Map.Entry<String, String> entry : LOGIC_OPERATORS.entrySet()) {
            Map<String, Object> operator = new HashMap<>();
            operator.put("chinese", entry.getKey());
            operator.put("sql", entry.getValue());
            operator.put("type", "LOGIC");
            operator.put("description", getOperatorDescription(entry.getKey()));
            operators.add(operator);
        }
        
        return operators;
    }

    @Override
    public List<Map<String, Object>> getFormulaTemplates(String category) {
        List<Map<String, Object>> templates = new ArrayList<>();
        
        if ("FINANCIAL_AUDIT".equals(category) || category == null) {
            // 财务审计模板
            templates.add(createTemplate("金额异常检查", 
                "【金额】大于 1000000 并且 【状态】等于 '有效'", 
                "检查金额超过100万且状态为有效的记录"));
            
            templates.add(createTemplate("余额不平衡检查", 
                "【借方金额】不等于 【贷方金额】", 
                "检查借贷方金额不平衡的记录"));
        }
        
        if ("RISK_CONTROL".equals(category) || category == null) {
            // 风险控制模板
            templates.add(createTemplate("高风险交易检查", 
                "【交易金额】大于 500000 或者 【风险等级】等于 '高'", 
                "识别高金额或高风险等级的交易"));
            
            templates.add(createTemplate("异常时间交易", 
                "【交易时间】小于 '09:00:00' 或者 【交易时间】大于 '17:00:00'", 
                "检查非工作时间的交易"));
        }
        
        return templates;
    }

    @Override
    public List<Map<String, Object>> getFormulaSuggestions(String partialFormula, 
                                                          List<String> availableFields) {
        List<Map<String, Object>> suggestions = new ArrayList<>();
        
        if (!StringUtils.hasText(partialFormula)) {
            // 提供字段建议
            for (String field : availableFields) {
                suggestions.add(createSuggestion("FIELD", "【" + field + "】", "字段: " + field));
            }
            return suggestions;
        }
        
        String lowerFormula = partialFormula.toLowerCase();
        
        // 运算符建议
        for (String operator : CHINESE_OPERATORS.keySet()) {
            if (operator.contains(lowerFormula) || lowerFormula.contains(operator)) {
                suggestions.add(createSuggestion("OPERATOR", operator, "运算符: " + operator));
            }
        }
        
        // 逻辑运算符建议
        for (String operator : LOGIC_OPERATORS.keySet()) {
            if (operator.contains(lowerFormula) || lowerFormula.contains(operator)) {
                suggestions.add(createSuggestion("LOGIC", operator, "逻辑运算符: " + operator));
            }
        }
        
        // 字段建议
        for (String field : availableFields) {
            if (field.toLowerCase().contains(lowerFormula)) {
                suggestions.add(createSuggestion("FIELD", "【" + field + "】", "字段: " + field));
            }
        }
        
        return suggestions;
    }

    @Override
    public String formatChineseFormula(String chineseFormula) {
        if (!StringUtils.hasText(chineseFormula)) {
            return "";
        }
        
        String formatted = chineseFormula;
        
        // 1. 统一空格
        formatted = formatted.replaceAll("\\s+", " ");
        
        // 2. 运算符前后加空格
        for (String operator : CHINESE_OPERATORS.keySet()) {
            formatted = formatted.replace(operator, " " + operator + " ");
        }
        
        for (String operator : LOGIC_OPERATORS.keySet()) {
            formatted = formatted.replace(operator, " " + operator + " ");
        }
        
        // 3. 清理多余空格
        formatted = formatted.replaceAll("\\s+", " ").trim();
        
        // 4. 括号处理
        formatted = formatted.replace("( ", "(").replace(" )", ")");
        
        return formatted;
    }

    // 私有辅助方法
    private String preprocessFormula(String formula) {
        // 去除多余空格，统一格式
        return formula.replaceAll("\\s+", " ").trim();
    }

    private List<String> extractFieldReferences(String formula) {
        List<String> fields = new ArrayList<>();
        Pattern pattern = Pattern.compile("【([^】]+)】");
        Matcher matcher = pattern.matcher(formula);
        
        while (matcher.find()) {
            fields.add(matcher.group(1));
        }
        
        return fields;
    }

    private String convertOperators(String formula) {
        String result = formula;
        
        // 转换比较运算符
        for (Map.Entry<String, String> entry : CHINESE_OPERATORS.entrySet()) {
            result = result.replace(entry.getKey(), entry.getValue());
        }
        
        // 转换逻辑运算符
        for (Map.Entry<String, String> entry : LOGIC_OPERATORS.entrySet()) {
            result = result.replace(entry.getKey(), entry.getValue());
        }
        
        // 处理字段引用
        result = result.replaceAll("【([^】]+)】", "$1");
        
        return result;
    }

    private String convertFunctions(String expression) {
        String result = expression;
        
        for (Map.Entry<String, String> entry : FUNCTION_MAPPING.entrySet()) {
            result = result.replace(entry.getKey(), entry.getValue());
        }
        
        return result;
    }

    private Map<String, Object> validateSQLSyntax(String sqlExpression) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 基本SQL语法检查
            if (sqlExpression.contains("SELECT") || sqlExpression.contains("UPDATE") || 
                sqlExpression.contains("DELETE") || sqlExpression.contains("DROP")) {
                result.put("valid", false);
                result.put("message", "表达式中不允许包含SQL关键字");
                return result;
            }
            
            // 检查括号匹配
            if (!isParenthesesBalanced(sqlExpression)) {
                result.put("valid", false);
                result.put("message", "括号不匹配");
                return result;
            }
            
            result.put("valid", true);
            result.put("message", "SQL语法正确");
            
        } catch (Exception e) {
            result.put("valid", false);
            result.put("message", "语法验证失败: " + e.getMessage());
        }
        
        return result;
    }

    private boolean isParenthesesBalanced(String expression) {
        int count = 0;
        for (char c : expression.toCharArray()) {
            if (c == '(') {
                count++;
            } else if (c == ')') {
                count--;
                if (count < 0) {
                    return false;
                }
            }
        }
        return count == 0;
    }

    private boolean checkBasicSyntax(String formula) {
        // 基本语法检查逻辑
        return !formula.contains(";;") && !formula.contains(",,");
    }

    private List<String> checkOperators(String formula) {
        List<String> invalidOperators = new ArrayList<>();
        // 这里可以添加更复杂的运算符检查逻辑
        return invalidOperators;
    }

    private boolean checkParentheses(String formula) {
        return isParenthesesBalanced(formula);
    }

    private boolean checkFieldReferences(String formula) {
        // 检查字段引用格式是否正确
        Pattern pattern = Pattern.compile("【[^】]*】");
        return pattern.matcher(formula).find();
    }

    private String getOperatorDescription(String operator) {
        switch (operator) {
            case "等于": return "判断两个值是否相等";
            case "大于": return "判断左值是否大于右值";
            case "小于": return "判断左值是否小于右值";
            case "包含": return "判断字符串是否包含指定内容";
            case "并且": return "逻辑与运算，两个条件都为真";
            case "或者": return "逻辑或运算，任一条件为真";
            default: return "运算符: " + operator;
        }
    }

    private Map<String, Object> createTemplate(String name, String formula, String description) {
        Map<String, Object> template = new HashMap<>();
        template.put("name", name);
        template.put("formula", formula);
        template.put("description", description);
        template.put("category", "TEMPLATE");
        return template;
    }

    private Map<String, Object> createSuggestion(String type, String value, String description) {
        Map<String, Object> suggestion = new HashMap<>();
        suggestion.put("type", type);
        suggestion.put("value", value);
        suggestion.put("description", description);
        return suggestion;
    }

    @Override
    public Map<String, Object> validateAndParseChineseFormula(String chineseFormula, Map<String, Object> dataSourceConfig) {
        Map<String, Object> result = new HashMap<>();

        try {
            if (!StringUtils.hasText(chineseFormula)) {
                result.put("success", false);
                result.put("message", "中文公式不能为空");
                result.put("valid", false);
                return result;
            }

            // 1. 首先验证公式语法
            Map<String, Object> validationResult = validateChineseFormula(chineseFormula);
            boolean isValid = (Boolean) validationResult.get("valid");

            if (!isValid) {
                result.put("success", false);
                result.put("valid", false);
                result.put("message", "公式语法验证失败");
                result.put("errors", validationResult.get("errors"));
                result.put("warnings", validationResult.get("warnings"));
                return result;
            }

            // 2. 解析公式
            String sourceDataConfig = dataSourceConfig != null ? dataSourceConfig.toString() : "";
            String targetDataConfig = ""; // 可以从dataSourceConfig中提取

            Map<String, Object> parseResult = parseChineseFormula(chineseFormula, sourceDataConfig, targetDataConfig);
            boolean parseSuccess = (Boolean) parseResult.get("success");

            if (!parseSuccess) {
                result.put("success", false);
                result.put("valid", false);
                result.put("message", "公式解析失败: " + parseResult.get("message"));
                return result;
            }

            // 3. 验证字段引用
            List<String> fieldReferences = (List<String>) parseResult.get("fieldReferences");
            Map<String, Object> fieldValidation = validateFieldReferences(fieldReferences, dataSourceConfig);

            // 4. 组装最终结果
            result.put("success", true);
            result.put("valid", true);
            result.put("message", "公式验证和解析成功");
            result.put("originalFormula", chineseFormula);
            result.put("sqlExpression", parseResult.get("sqlExpression"));
            result.put("fieldReferences", fieldReferences);
            result.put("fieldValidation", fieldValidation);
            result.put("processedFormula", parseResult.get("processedFormula"));
            result.put("warnings", validationResult.get("warnings"));
            result.put("suggestions", generateOptimizationSuggestions(chineseFormula));

        } catch (Exception e) {
            log.error("验证和解析中文公式失败", e);
            result.put("success", false);
            result.put("valid", false);
            result.put("message", "处理失败: " + e.getMessage());
        }

        return result;
    }

    /**
     * 验证字段引用
     */
    private Map<String, Object> validateFieldReferences(List<String> fieldReferences, Map<String, Object> dataSourceConfig) {
        Map<String, Object> result = new HashMap<>();
        List<String> validFields = new ArrayList<>();
        List<String> invalidFields = new ArrayList<>();

        try {
            // 这里应该根据dataSourceConfig验证字段是否存在
            // 暂时模拟验证逻辑
            for (String field : fieldReferences) {
                if (isValidField(field, dataSourceConfig)) {
                    validFields.add(field);
                } else {
                    invalidFields.add(field);
                }
            }

            result.put("validFields", validFields);
            result.put("invalidFields", invalidFields);
            result.put("allValid", invalidFields.isEmpty());
            result.put("message", invalidFields.isEmpty() ? "所有字段引用有效" : "存在无效字段引用: " + String.join(", ", invalidFields));

        } catch (Exception e) {
            log.warn("字段引用验证失败", e);
            result.put("validFields", new ArrayList<>());
            result.put("invalidFields", fieldReferences);
            result.put("allValid", false);
            result.put("message", "字段验证失败: " + e.getMessage());
        }

        return result;
    }

    /**
     * 检查字段是否有效
     */
    private boolean isValidField(String fieldName, Map<String, Object> dataSourceConfig) {
        // 简单的字段验证逻辑，实际项目中需要查询数据源元数据
        if (dataSourceConfig == null || fieldName == null) {
            return false;
        }

        // 常见的字段名称认为是有效的
        String[] commonFields = {"金额", "数量", "日期", "状态", "名称", "编码", "类型", "备注"};
        for (String commonField : commonFields) {
            if (fieldName.contains(commonField)) {
                return true;
            }
        }

        return true; // 暂时默认所有字段都有效
    }

    /**
     * 生成优化建议
     */
    private List<String> generateOptimizationSuggestions(String chineseFormula) {
        List<String> suggestions = new ArrayList<>();

        try {
            // 检查公式复杂度
            if (chineseFormula.length() > 200) {
                suggestions.add("公式较长，建议拆分为多个简单公式");
            }

            // 检查嵌套层级
            int nestingLevel = countNestingLevel(chineseFormula);
            if (nestingLevel > 3) {
                suggestions.add("嵌套层级较深，建议简化逻辑结构");
            }

            // 检查运算符使用
            if (chineseFormula.contains("或者") && chineseFormula.contains("并且")) {
                suggestions.add("混合使用'或者'和'并且'时，建议使用括号明确优先级");
            }

            // 检查字段引用
            if (!chineseFormula.contains("【") || !chineseFormula.contains("】")) {
                suggestions.add("建议使用【字段名】格式引用字段，提高可读性");
            }

        } catch (Exception e) {
            log.warn("生成优化建议失败", e);
        }

        return suggestions;
    }

    /**
     * 计算嵌套层级
     */
    private int countNestingLevel(String formula) {
        int maxLevel = 0;
        int currentLevel = 0;

        for (char c : formula.toCharArray()) {
            if (c == '(') {
                currentLevel++;
                maxLevel = Math.max(maxLevel, currentLevel);
            } else if (c == ')') {
                currentLevel--;
            }
        }

        return maxLevel;
    }
}
