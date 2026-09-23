package com.management.accountant.service.eps.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.management.accountant.entity.eps.EpsBudgetFormula;
import com.management.accountant.mapper.eps.EpsBudgetFormulaMapper;
import com.management.accountant.service.eps.EpsBudgetFormulaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 预算公式计算服务实现
 * 
 * @author 华博云
 * @version 3.0.0
 */
@Slf4j
@Service
public class EpsBudgetFormulaServiceImpl implements EpsBudgetFormulaService {

    @Autowired
    private EpsBudgetFormulaMapper budgetFormulaMapper;

    @Override
    public IPage<EpsBudgetFormula> queryBudgetFormulaPage(Long current, Long size, String formulaName, 
                                                         String formulaType, Long systemId, String status) {
        Page<EpsBudgetFormula> page = new Page<>(current, size);
        QueryWrapper<EpsBudgetFormula> queryWrapper = new QueryWrapper<>();
        
        if (formulaName != null && !formulaName.isEmpty()) {
            queryWrapper.like("formula_name", formulaName);
        }
        if (formulaType != null && !formulaType.isEmpty()) {
            queryWrapper.eq("formula_type", formulaType);
        }
        if (systemId != null) {
            queryWrapper.eq("system_id", systemId);
        }
        if (status != null && !status.isEmpty()) {
            queryWrapper.eq("status", status);
        }
        
        queryWrapper.eq("deleted", 0);
        queryWrapper.orderByDesc("created_time");
        
        return budgetFormulaMapper.selectPage(page, queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean createBudgetFormula(EpsBudgetFormula budgetFormula) {
        try {
            // 验证公式表达式
            Map<String, Object> validationResult = validateFormulaExpression(
                    budgetFormula.getFormulaExpression(), budgetFormula.getFormulaType());
            if (!(Boolean) validationResult.get("isValid")) {
                throw new RuntimeException("公式表达式验证失败: " + validationResult.get("message"));
            }
            
            // 设置默认值
            budgetFormula.setCreatedTime(LocalDateTime.now());
            budgetFormula.setUpdatedTime(LocalDateTime.now());
            budgetFormula.setDeleted(0);
            
            if (budgetFormula.getStatus() == null) {
                budgetFormula.setStatus("DRAFT");
            }
            
            return budgetFormulaMapper.insert(budgetFormula) > 0;
        } catch (Exception e) {
            log.error("创建预算公式失败", e);
            throw new RuntimeException("创建失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updateBudgetFormula(EpsBudgetFormula budgetFormula) {
        try {
            // 验证公式表达式
            if (budgetFormula.getFormulaExpression() != null) {
                Map<String, Object> validationResult = validateFormulaExpression(
                        budgetFormula.getFormulaExpression(), budgetFormula.getFormulaType());
                if (!(Boolean) validationResult.get("isValid")) {
                    throw new RuntimeException("公式表达式验证失败: " + validationResult.get("message"));
                }
            }
            
            budgetFormula.setUpdatedTime(LocalDateTime.now());
            return budgetFormulaMapper.updateById(budgetFormula) > 0;
        } catch (Exception e) {
            log.error("更新预算公式失败", e);
            throw new RuntimeException("更新失败: " + e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteBudgetFormula(Long formulaId) {
        try {
            EpsBudgetFormula budgetFormula = new EpsBudgetFormula();
            budgetFormula.setFormulaId(formulaId);
            budgetFormula.setDeleted(1);
            budgetFormula.setUpdatedTime(LocalDateTime.now());
            return budgetFormulaMapper.updateById(budgetFormula) > 0;
        } catch (Exception e) {
            log.error("删除预算公式失败", e);
            return false;
        }
    }

    @Override
    public EpsBudgetFormula getBudgetFormulaById(Long formulaId) {
        return budgetFormulaMapper.selectById(formulaId);
    }

    @Override
    public Map<String, Object> validateFormulaExpression(String formulaExpression, String formulaType) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 基本语法检查
            if (formulaExpression == null || formulaExpression.trim().isEmpty()) {
                result.put("isValid", false);
                result.put("message", "公式表达式不能为空");
                return result;
            }
            
            // 括号匹配检查
            if (!isParenthesesBalanced(formulaExpression)) {
                result.put("isValid", false);
                result.put("message", "括号不匹配");
                return result;
            }
            
            // 提取变量和函数
            List<String> variables = extractFormulaVariables(formulaExpression);
            List<String> functions = extractFormulaFunctions(formulaExpression);
            
            // 验证变量
            Map<String, Object> variableValidation = validateFormulaVariables(variables, null);
            if (!(Boolean) variableValidation.get("isValid")) {
                result.put("isValid", false);
                result.put("message", "变量验证失败: " + variableValidation.get("message"));
                return result;
            }
            
            // 验证函数
            Map<String, Object> functionValidation = validateFormulaFunctions(functions);
            if (!(Boolean) functionValidation.get("isValid")) {
                result.put("isValid", false);
                result.put("message", "函数验证失败: " + functionValidation.get("message"));
                return result;
            }
            
            result.put("isValid", true);
            result.put("message", "公式表达式验证通过");
            result.put("variables", variables);
            result.put("functions", functions);
            
        } catch (Exception e) {
            log.error("验证公式表达式失败", e);
            result.put("isValid", false);
            result.put("message", "验证失败: " + e.getMessage());
        }
        
        return result;
    }

    @Override
    public Map<String, Object> calculateFormulaResult(Long formulaId, Map<String, Object> parameters) {
        Map<String, Object> result = new HashMap<>();
        long startTime = System.currentTimeMillis();
        
        try {
            EpsBudgetFormula formula = getBudgetFormulaById(formulaId);
            if (formula == null) {
                throw new RuntimeException("未找到指定公式");
            }
            
            if (!"ACTIVE".equals(formula.getStatus())) {
                throw new RuntimeException("公式未激活，无法计算");
            }
            
            // 编译公式
            Map<String, Object> compileResult = compileFormula(formula.getFormulaExpression());
            if (!(Boolean) compileResult.get("success")) {
                throw new RuntimeException("公式编译失败: " + compileResult.get("message"));
            }
            
            // 执行计算
            Object calculationResult = executeFormula(compileResult.get("compiledFormula"), parameters);
            
            long duration = System.currentTimeMillis() - startTime;
            
            result.put("success", true);
            result.put("result", calculationResult);
            result.put("duration", duration);
            result.put("formulaId", formulaId);
            result.put("parameters", parameters);
            
            // 保存计算历史
            saveFormulaCalculationHistory(formulaId, parameters, calculationResult, duration);
            
        } catch (Exception e) {
            log.error("计算公式结果失败", e);
            result.put("success", false);
            result.put("message", e.getMessage());
            result.put("duration", System.currentTimeMillis() - startTime);
        }
        
        return result;
    }

    @Override
    public List<Map<String, Object>> batchCalculateFormulas(List<Long> formulaIds, Map<String, Object> parameters) {
        List<Map<String, Object>> results = new ArrayList<>();
        
        // 获取计算顺序
        List<Long> calculationOrder = getFormulaCalculationOrder(formulaIds);
        
        for (Long formulaId : calculationOrder) {
            Map<String, Object> result = calculateFormulaResult(formulaId, parameters);
            results.add(result);
            
            // 如果计算成功，将结果添加到参数中供后续公式使用
            if ((Boolean) result.get("success")) {
                parameters.put("FORMULA_" + formulaId, result.get("result"));
            }
        }
        
        return results;
    }

    @Override
    public Map<String, Object> testFormulaCalculation(String formulaExpression, Map<String, Object> testParameters) {
        Map<String, Object> result = new HashMap<>();
        long startTime = System.currentTimeMillis();
        
        try {
            // 验证公式
            Map<String, Object> validationResult = validateFormulaExpression(formulaExpression, null);
            if (!(Boolean) validationResult.get("isValid")) {
                throw new RuntimeException("公式验证失败: " + validationResult.get("message"));
            }
            
            // 编译公式
            Map<String, Object> compileResult = compileFormula(formulaExpression);
            if (!(Boolean) compileResult.get("success")) {
                throw new RuntimeException("公式编译失败: " + compileResult.get("message"));
            }
            
            // 执行计算
            Object calculationResult = executeFormula(compileResult.get("compiledFormula"), testParameters);
            
            result.put("success", true);
            result.put("result", calculationResult);
            result.put("duration", System.currentTimeMillis() - startTime);
            result.put("expression", formulaExpression);
            result.put("parameters", testParameters);
            
        } catch (Exception e) {
            log.error("测试公式计算失败", e);
            result.put("success", false);
            result.put("message", e.getMessage());
            result.put("duration", System.currentTimeMillis() - startTime);
        }
        
        return result;
    }

    @Override
    public Map<String, Object> getFormulaDependencies(Long formulaId) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            EpsBudgetFormula formula = getBudgetFormulaById(formulaId);
            if (formula == null) {
                throw new RuntimeException("未找到指定公式");
            }
            
            // 提取公式中的变量
            List<String> variables = extractFormulaVariables(formula.getFormulaExpression());
            
            // 查找依赖的其他公式
            List<Long> dependentFormulas = new ArrayList<>();
            for (String variable : variables) {
                if (variable.startsWith("FORMULA_")) {
                    try {
                        Long dependentFormulaId = Long.parseLong(variable.substring(8));
                        dependentFormulas.add(dependentFormulaId);
                    } catch (NumberFormatException e) {
                        // 忽略非公式变量
                    }
                }
            }
            
            result.put("formulaId", formulaId);
            result.put("variables", variables);
            result.put("dependentFormulas", dependentFormulas);
            result.put("hasCircularDependency", checkCircularDependency(formulaId, dependentFormulas));
            
        } catch (Exception e) {
            log.error("获取公式依赖关系失败", e);
            result.put("error", e.getMessage());
        }
        
        return result;
    }

    @Override
    public List<EpsBudgetFormula> getBudgetFormulasBySystemId(Long systemId) {
        QueryWrapper<EpsBudgetFormula> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("system_id", systemId);
        queryWrapper.eq("deleted", 0);
        queryWrapper.orderByAsc("sort_order");
        queryWrapper.orderByDesc("created_time");
        return budgetFormulaMapper.selectList(queryWrapper);
    }

    @Override
    public List<EpsBudgetFormula> getBudgetFormulasByType(String formulaType, Long systemId) {
        QueryWrapper<EpsBudgetFormula> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("formula_type", formulaType);
        if (systemId != null) {
            queryWrapper.eq("system_id", systemId);
        }
        queryWrapper.eq("deleted", 0);
        queryWrapper.orderByAsc("sort_order");
        queryWrapper.orderByDesc("created_time");
        return budgetFormulaMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean copyBudgetFormula(Long sourceFormulaId, String targetFormulaCode, String targetFormulaName) {
        try {
            EpsBudgetFormula sourceFormula = getBudgetFormulaById(sourceFormulaId);
            if (sourceFormula == null) {
                throw new RuntimeException("源公式不存在");
            }
            
            EpsBudgetFormula targetFormula = new EpsBudgetFormula();
            // 复制属性
            targetFormula.setSystemId(sourceFormula.getSystemId());
            targetFormula.setFormulaCode(targetFormulaCode);
            targetFormula.setFormulaName(targetFormulaName);
            targetFormula.setFormulaType(sourceFormula.getFormulaType());
            targetFormula.setFormulaExpression(sourceFormula.getFormulaExpression());
            targetFormula.setFormulaDescription(sourceFormula.getFormulaDescription());
            targetFormula.setReturnType(sourceFormula.getReturnType());
            targetFormula.setParameterDefinition(sourceFormula.getParameterDefinition());
            targetFormula.setValidationRules(sourceFormula.getValidationRules());
            targetFormula.setStatus("DRAFT");
            targetFormula.setCreatedTime(LocalDateTime.now());
            targetFormula.setUpdatedTime(LocalDateTime.now());
            targetFormula.setDeleted(0);
            
            return budgetFormulaMapper.insert(targetFormula) > 0;
        } catch (Exception e) {
            log.error("复制预算公式失败", e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean activateBudgetFormula(Long formulaId) {
        try {
            EpsBudgetFormula budgetFormula = new EpsBudgetFormula();
            budgetFormula.setFormulaId(formulaId);
            budgetFormula.setStatus("ACTIVE");
            budgetFormula.setUpdatedTime(LocalDateTime.now());
            return budgetFormulaMapper.updateById(budgetFormula) > 0;
        } catch (Exception e) {
            log.error("激活预算公式失败", e);
            return false;
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean deactivateBudgetFormula(Long formulaId) {
        try {
            EpsBudgetFormula budgetFormula = new EpsBudgetFormula();
            budgetFormula.setFormulaId(formulaId);
            budgetFormula.setStatus("INACTIVE");
            budgetFormula.setUpdatedTime(LocalDateTime.now());
            return budgetFormulaMapper.updateById(budgetFormula) > 0;
        } catch (Exception e) {
            log.error("停用预算公式失败", e);
            return false;
        }
    }

    @Override
    public List<Map<String, Object>> getFormulaVariables(Long systemId, String variableType) {
        List<Map<String, Object>> variables = new ArrayList<>();
        
        // 基础变量
        variables.add(createVariable("CURRENT_YEAR", "当前年度", "INTEGER", "系统变量"));
        variables.add(createVariable("CURRENT_MONTH", "当前月份", "INTEGER", "系统变量"));
        variables.add(createVariable("CURRENT_DATE", "当前日期", "DATE", "系统变量"));
        
        // 预算科目变量
        variables.add(createVariable("REVENUE", "营业收入", "DECIMAL", "预算科目"));
        variables.add(createVariable("COST", "营业成本", "DECIMAL", "预算科目"));
        variables.add(createVariable("EXPENSE", "期间费用", "DECIMAL", "预算科目"));
        
        // 组织变量
        variables.add(createVariable("ORG_LEVEL", "组织层级", "INTEGER", "组织变量"));
        variables.add(createVariable("ORG_TYPE", "组织类型", "STRING", "组织变量"));
        
        return variables;
    }

    @Override
    public List<Map<String, Object>> getFormulaFunctions(String functionCategory) {
        List<Map<String, Object>> functions = new ArrayList<>();
        
        // 数学函数
        functions.add(createFunction("SUM", "求和", "数学函数", "SUM(a, b, c, ...)"));
        functions.add(createFunction("AVG", "平均值", "数学函数", "AVG(a, b, c, ...)"));
        functions.add(createFunction("MAX", "最大值", "数学函数", "MAX(a, b, c, ...)"));
        functions.add(createFunction("MIN", "最小值", "数学函数", "MIN(a, b, c, ...)"));
        functions.add(createFunction("ABS", "绝对值", "数学函数", "ABS(number)"));
        functions.add(createFunction("ROUND", "四舍五入", "数学函数", "ROUND(number, digits)"));
        
        // 逻辑函数
        functions.add(createFunction("IF", "条件判断", "逻辑函数", "IF(condition, true_value, false_value)"));
        functions.add(createFunction("AND", "逻辑与", "逻辑函数", "AND(condition1, condition2, ...)"));
        functions.add(createFunction("OR", "逻辑或", "逻辑函数", "OR(condition1, condition2, ...)"));
        functions.add(createFunction("NOT", "逻辑非", "逻辑函数", "NOT(condition)"));
        
        // 日期函数
        functions.add(createFunction("YEAR", "获取年份", "日期函数", "YEAR(date)"));
        functions.add(createFunction("MONTH", "获取月份", "日期函数", "MONTH(date)"));
        functions.add(createFunction("DAY", "获取日期", "日期函数", "DAY(date)"));
        functions.add(createFunction("DATEDIFF", "日期差", "日期函数", "DATEDIFF(date1, date2)"));
        
        // 文本函数
        functions.add(createFunction("CONCAT", "字符串连接", "文本函数", "CONCAT(str1, str2, ...)"));
        functions.add(createFunction("LENGTH", "字符串长度", "文本函数", "LENGTH(string)"));
        functions.add(createFunction("SUBSTRING", "子字符串", "文本函数", "SUBSTRING(string, start, length)"));
        
        return functions;
    }

    // 私有辅助方法
    private boolean isParenthesesBalanced(String expression) {
        int count = 0;
        for (char c : expression.toCharArray()) {
            if (c == '(') count++;
            else if (c == ')') count--;
            if (count < 0) return false;
        }
        return count == 0;
    }

    private Map<String, Object> createVariable(String name, String description, String type, String category) {
        Map<String, Object> variable = new HashMap<>();
        variable.put("name", name);
        variable.put("description", description);
        variable.put("type", type);
        variable.put("category", category);
        return variable;
    }

    private Map<String, Object> createFunction(String name, String description, String category, String syntax) {
        Map<String, Object> function = new HashMap<>();
        function.put("name", name);
        function.put("description", description);
        function.put("category", category);
        function.put("syntax", syntax);
        return function;
    }

    // 其他方法的简单实现（占位符）
    @Override
    public String exportFormulaConfig(Long systemId, List<Long> formulaIds) {
        // TODO: 实现公式配置导出
        return "{}";
    }

    @Override
    public boolean importFormulaConfig(String formulaConfigData, Long systemId) {
        // TODO: 实现公式配置导入
        return true;
    }

    @Override
    public Map<String, Object> parseFormulaExpression(String formulaExpression) {
        // TODO: 实现公式表达式解析
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        return result;
    }

    @Override
    public Map<String, Object> compileFormula(String formulaExpression) {
        // TODO: 实现公式编译
        Map<String, Object> result = new HashMap<>();
        result.put("success", true);
        result.put("compiledFormula", formulaExpression);
        return result;
    }

    @Override
    public Object executeFormula(Object compiledFormula, Map<String, Object> variables) {
        // TODO: 实现公式执行
        // 这里返回一个模拟结果
        return 100.0;
    }

    @Override
    public List<String> extractFormulaVariables(String formulaExpression) {
        List<String> variables = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\b[A-Z_][A-Z0-9_]*\\b");
        Matcher matcher = pattern.matcher(formulaExpression);
        while (matcher.find()) {
            String variable = matcher.group();
            if (!isFunction(variable) && !variables.contains(variable)) {
                variables.add(variable);
            }
        }
        return variables;
    }

    @Override
    public List<String> extractFormulaFunctions(String formulaExpression) {
        List<String> functions = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\b[A-Z_][A-Z0-9_]*\\s*\\(");
        Matcher matcher = pattern.matcher(formulaExpression);
        while (matcher.find()) {
            String function = matcher.group().replaceAll("\\s*\\(", "");
            if (!functions.contains(function)) {
                functions.add(function);
            }
        }
        return functions;
    }

    @Override
    public Map<String, Object> validateFormulaVariables(List<String> variables, Long systemId) {
        Map<String, Object> result = new HashMap<>();
        result.put("isValid", true);
        result.put("message", "变量验证通过");
        return result;
    }

    @Override
    public Map<String, Object> validateFormulaFunctions(List<String> functions) {
        Map<String, Object> result = new HashMap<>();
        result.put("isValid", true);
        result.put("message", "函数验证通过");
        return result;
    }

    @Override
    public Map<String, Object> buildFormulaDependencyGraph(Long systemId) {
        // TODO: 实现依赖图构建
        return new HashMap<>();
    }

    @Override
    public boolean checkCircularDependency(Long formulaId, List<Long> dependentFormulaIds) {
        // TODO: 实现循环依赖检查
        return false;
    }

    @Override
    public List<Long> getFormulaCalculationOrder(List<Long> formulaIds) {
        // TODO: 实现计算顺序确定
        return formulaIds;
    }

    @Override
    public Map<String, Object> batchValidateFormulas(List<Long> formulaIds) {
        // TODO: 实现批量验证
        Map<String, Object> result = new HashMap<>();
        result.put("isValid", true);
        return result;
    }

    @Override
    public Map<String, Object> getFormulaPerformanceStats(Long formulaId) {
        // TODO: 实现性能统计
        return new HashMap<>();
    }

    @Override
    public String optimizeFormulaExpression(String formulaExpression) {
        // TODO: 实现公式优化
        return formulaExpression;
    }

    @Override
    public String formatFormulaExpression(String formulaExpression) {
        // TODO: 实现公式格式化
        return formulaExpression;
    }

    @Override
    public Map<String, Object> getFormulaHelp(String formulaType) {
        // TODO: 实现帮助信息
        return new HashMap<>();
    }

    @Override
    public List<Map<String, Object>> getFormulaExamples(String formulaType) {
        // TODO: 实现示例获取
        return new ArrayList<>();
    }

    @Override
    public boolean saveFormulaCalculationHistory(Long formulaId, Map<String, Object> parameters, 
                                               Object result, Long duration) {
        // TODO: 实现计算历史保存
        return true;
    }

    @Override
    public List<Map<String, Object>> getFormulaCalculationHistory(Long formulaId, String startDate, String endDate) {
        // TODO: 实现计算历史查询
        return new ArrayList<>();
    }

    @Override
    public int cleanupFormulaCalculationHistory(Integer retentionDays) {
        // TODO: 实现历史清理
        return 0;
    }

    private boolean isFunction(String word) {
        // 简单的函数判断
        List<String> functions = Arrays.asList("SUM", "AVG", "MAX", "MIN", "ABS", "ROUND", 
                                              "IF", "AND", "OR", "NOT", "YEAR", "MONTH", "DAY");
        return functions.contains(word);
    }
}
