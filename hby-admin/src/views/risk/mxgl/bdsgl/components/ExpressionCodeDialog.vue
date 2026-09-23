<template>
  <el-dialog
    title="生成表达式代码"
    :visible.sync="visible"
    width="900px"
    :before-close="handleClose"
    append-to-body
  >
    <el-form ref="codeForm" :model="codeForm" label-width="120px" size="small">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="代码语言">
            <el-select v-model="codeForm.language" placeholder="请选择代码语言" style="width: 100%" @change="generateCode">
              <el-option label="Java" value="JAVA" />
              <el-option label="JavaScript" value="JAVASCRIPT" />
              <el-option label="Python" value="PYTHON" />
              <el-option label="SQL" value="SQL" />
            </el-select>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="代码类型">
            <el-select v-model="codeForm.codeType" placeholder="请选择代码类型" style="width: 100%" @change="generateCode">
              <el-option label="表达式解析器" value="PARSER" />
              <el-option label="验证器" value="VALIDATOR" />
              <el-option label="执行器" value="EXECUTOR" />
              <el-option label="单元测试" value="TEST" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>
    
    <el-divider content-position="left">生成的代码</el-divider>
    
    <div class="code-container">
      <div class="code-header">
        <el-button type="primary" size="small" @click="copyCode">复制代码</el-button>
        <el-button type="success" size="small" @click="downloadCode">下载文件</el-button>
      </div>
      
      <el-input
        v-model="generatedCode"
        type="textarea"
        :rows="20"
        readonly
        placeholder="请选择代码语言和类型"
        class="code-textarea"
      />
    </div>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'ExpressionCodeDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    data: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      codeForm: {
        language: 'JAVA',
        codeType: 'PARSER'
      },
      generatedCode: ''
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.generateCode()
      }
    }
  },
  methods: {
    generateCode() {
      if (!this.codeForm.language || !this.codeForm.codeType) return
      
      const { language, codeType } = this.codeForm
      
      if (language === 'JAVA') {
        if (codeType === 'PARSER') {
          this.generateJavaParser()
        } else if (codeType === 'VALIDATOR') {
          this.generateJavaValidator()
        } else if (codeType === 'EXECUTOR') {
          this.generateJavaExecutor()
        } else if (codeType === 'TEST') {
          this.generateJavaTest()
        }
      } else if (language === 'JAVASCRIPT') {
        this.generateJavaScript()
      } else if (language === 'PYTHON') {
        this.generatePython()
      } else if (language === 'SQL') {
        this.generateSQL()
      }
    },
    
    generateJavaParser() {
      this.generatedCode = `package com.huabo.expression;

import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

/**
 * ${this.data.ruleName || '表达式规则'} 解析器
 * 
 * @author 系统生成
 * @date ${new Date().toLocaleDateString()}
 */
public class ExpressionParser {
    
    private static final Pattern VARIABLE_PATTERN = Pattern.compile("\\$\\{([^}]+)\\}");
    
    /**
     * 解析表达式
     * @param expression 表达式内容
     * @param variables 变量值映射
     * @return 解析后的表达式
     */
    public String parseExpression(String expression, Map<String, Object> variables) {
        if (expression == null || expression.isEmpty()) {
            return expression;
        }
        
        Matcher matcher = VARIABLE_PATTERN.matcher(expression);
        StringBuffer result = new StringBuffer();
        
        while (matcher.find()) {
            String variableName = matcher.group(1);
            Object value = variables.get(variableName);
            String replacement = value != null ? value.toString() : "null";
            matcher.appendReplacement(result, replacement);
        }
        matcher.appendTail(result);
        
        return result.toString();
    }
    
    /**
     * 验证表达式语法
     * @param expression 表达式内容
     * @return 是否有效
     */
    public boolean validateSyntax(String expression) {
        try {
            // 简单的语法验证
            return expression != null && !expression.trim().isEmpty();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * 执行表达式
     * @param expression 表达式内容
     * @param variables 变量值映射
     * @return 执行结果
     */
    public Object executeExpression(String expression, Map<String, Object> variables) {
        String parsedExpression = parseExpression(expression, variables);
        // 这里可以集成表达式引擎，如 MVEL、SpEL 等
        return evaluateExpression(parsedExpression);
    }
    
    private Object evaluateExpression(String expression) {
        // 实际实现中应该使用专业的表达式引擎
        // 这里只是示例
        return true;
    }
}`
    },
    
    generateJavaValidator() {
      this.generatedCode = `package com.huabo.expression;

import java.util.Map;
import java.util.List;
import java.util.ArrayList;

/**
 * ${this.data.ruleName || '表达式规则'} 验证器
 */
public class ExpressionValidator {
    
    /**
     * 验证表达式规则
     * @param data 待验证数据
     * @return 验证结果
     */
    public ValidationResult validate(Map<String, Object> data) {
        ValidationResult result = new ValidationResult();
        
        try {
            // 执行表达式: ${this.data.expression || '${amount} > 1000'}
            boolean isValid = executeRule(data);
            result.setValid(isValid);
            result.setMessage(isValid ? "验证通过" : "验证失败");
        } catch (Exception e) {
            result.setValid(false);
            result.setMessage("验证异常: " + e.getMessage());
        }
        
        return result;
    }
    
    private boolean executeRule(Map<String, Object> data) {
        // 实现具体的规则逻辑
        return true;
    }
    
    public static class ValidationResult {
        private boolean valid;
        private String message;
        private List<String> errors = new ArrayList<>();
        
        // getters and setters
        public boolean isValid() { return valid; }
        public void setValid(boolean valid) { this.valid = valid; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
        public List<String> getErrors() { return errors; }
        public void setErrors(List<String> errors) { this.errors = errors; }
    }
}`
    },
    
    generateJavaExecutor() {
      this.generatedCode = `package com.huabo.expression;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * ${this.data.ruleName || '表达式规则'} 执行器
 */
public class ExpressionExecutor {
    
    /**
     * 同步执行表达式
     */
    public ExecutionResult execute(Map<String, Object> context) {
        ExecutionResult result = new ExecutionResult();
        long startTime = System.currentTimeMillis();
        
        try {
            // 执行表达式逻辑
            Object value = evaluateExpression(context);
            
            result.setSuccess(true);
            result.setResult(value);
            result.setExecutionTime(System.currentTimeMillis() - startTime);
        } catch (Exception e) {
            result.setSuccess(false);
            result.setError(e.getMessage());
            result.setExecutionTime(System.currentTimeMillis() - startTime);
        }
        
        return result;
    }
    
    /**
     * 异步执行表达式
     */
    public CompletableFuture<ExecutionResult> executeAsync(Map<String, Object> context) {
        return CompletableFuture.supplyAsync(() -> execute(context));
    }
    
    private Object evaluateExpression(Map<String, Object> context) {
        // 实现表达式执行逻辑
        // 表达式: ${this.data.expression || '${amount} > 1000'}
        return true;
    }
    
    public static class ExecutionResult {
        private boolean success;
        private Object result;
        private String error;
        private long executionTime;
        
        // getters and setters
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public Object getResult() { return result; }
        public void setResult(Object result) { this.result = result; }
        public String getError() { return error; }
        public void setError(String error) { this.error = error; }
        public long getExecutionTime() { return executionTime; }
        public void setExecutionTime(long executionTime) { this.executionTime = executionTime; }
    }
}`
    },
    
    generateJavaTest() {
      this.generatedCode = `package com.huabo.expression;

import org.junit.Test;
import org.junit.Assert;
import java.util.HashMap;
import java.util.Map;

/**
 * ${this.data.ruleName || '表达式规则'} 单元测试
 */
public class ExpressionTest {
    
    @Test
    public void testExpressionParser() {
        ExpressionParser parser = new ExpressionParser();
        Map<String, Object> variables = new HashMap<>();
        variables.put("amount", 1500);
        variables.put("type", "URGENT");
        
        String expression = "${this.data.expression || '${amount} > 1000 && ${type} == \"URGENT\"'}";
        String result = parser.parseExpression(expression, variables);
        
        Assert.assertNotNull(result);
        System.out.println("解析结果: " + result);
    }
    
    @Test
    public void testExpressionValidator() {
        ExpressionValidator validator = new ExpressionValidator();
        Map<String, Object> data = new HashMap<>();
        data.put("amount", 1500);
        data.put("type", "URGENT");
        
        ExpressionValidator.ValidationResult result = validator.validate(data);
        
        Assert.assertTrue(result.isValid());
        System.out.println("验证结果: " + result.getMessage());
    }
    
    @Test
    public void testExpressionExecutor() {
        ExpressionExecutor executor = new ExpressionExecutor();
        Map<String, Object> context = new HashMap<>();
        context.put("amount", 1500);
        context.put("type", "URGENT");
        
        ExpressionExecutor.ExecutionResult result = executor.execute(context);
        
        Assert.assertTrue(result.isSuccess());
        System.out.println("执行结果: " + result.getResult());
        System.out.println("执行时间: " + result.getExecutionTime() + "ms");
    }
}`
    },
    
    generateJavaScript() {
      this.generatedCode = `// ${this.data.ruleName || '表达式规则'} JavaScript实现

class ExpressionEngine {
    
    /**
     * 解析表达式变量
     */
    parseVariables(expression, variables) {
        return expression.replace(/\$\{([^}]+)\}/g, (match, varName) => {
            return variables[varName] !== undefined ? variables[varName] : 'null';
        });
    }
    
    /**
     * 执行表达式
     */
    execute(expression, variables) {
        try {
            const parsedExpression = this.parseVariables(expression, variables);
            // 注意：实际使用中应该使用安全的表达式解析器
            return eval(parsedExpression);
        } catch (error) {
            console.error('表达式执行错误:', error);
            return false;
        }
    }
    
    /**
     * 验证表达式
     */
    validate(data) {
        const expression = '${this.data.expression || '${amount} > 1000'}';
        return this.execute(expression, data);
    }
}

// 使用示例
const engine = new ExpressionEngine();
const testData = {
    amount: 1500,
    type: 'URGENT'
};

const result = engine.validate(testData);
console.log('验证结果:', result);`
    },
    
    generatePython() {
      this.generatedCode = `# ${this.data.ruleName || '表达式规则'} Python实现

import re
import operator

class ExpressionEngine:
    
    def __init__(self):
        self.operators = {
            '>': operator.gt,
            '>=': operator.ge,
            '<': operator.lt,
            '<=': operator.le,
            '==': operator.eq,
            '!=': operator.ne,
            'and': operator.and_,
            'or': operator.or_
        }
    
    def parse_variables(self, expression, variables):
        """解析表达式中的变量"""
        def replace_var(match):
            var_name = match.group(1)
            return str(variables.get(var_name, 'None'))
        
        return re.sub(r'\$\{([^}]+)\}', replace_var, expression)
    
    def execute(self, expression, variables):
        """执行表达式"""
        try:
            parsed_expression = self.parse_variables(expression, variables)
            # 注意：实际使用中应该使用安全的表达式解析器
            return eval(parsed_expression)
        except Exception as e:
            print(f"表达式执行错误: {e}")
            return False
    
    def validate(self, data):
        """验证数据"""
        expression = '${this.data.expression || '${amount} > 1000'}'
        return self.execute(expression, data)

# 使用示例
if __name__ == "__main__":
    engine = ExpressionEngine()
    test_data = {
        'amount': 1500,
        'type': 'URGENT'
    }
    
    result = engine.validate(test_data)
    print(f"验证结果: {result}")`
    },
    
    generateSQL() {
      this.generatedCode = `-- ${this.data.ruleName || '表达式规则'} SQL实现

-- 创建表达式规则表
CREATE TABLE TBL_EXPRESSION_RULE (
    RULE_ID VARCHAR(32) PRIMARY KEY,
    RULE_NAME VARCHAR(100) NOT NULL,
    RULE_CODE VARCHAR(50) NOT NULL,
    RULE_TYPE VARCHAR(20) NOT NULL,
    EXPRESSION TEXT NOT NULL,
    STATUS VARCHAR(20) DEFAULT 'ACTIVE',
    CREATE_TIME TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 插入当前规则
INSERT INTO TBL_EXPRESSION_RULE (
    RULE_ID, RULE_NAME, RULE_CODE, RULE_TYPE, EXPRESSION
) VALUES (
    '${this.data.ruleId || 'RULE_001'}',
    '${this.data.ruleName || '表达式规则'}',
    '${this.data.ruleCode || 'RULE_001'}',
    '${this.data.ruleType || 'NUMERIC'}',
    '${this.data.expression || '${amount} > 1000'}'
);

-- 查询规则
SELECT * FROM TBL_EXPRESSION_RULE 
WHERE RULE_CODE = '${this.data.ruleCode || 'RULE_001'}';

-- 应用规则的示例查询
SELECT 
    DATA_ID,
    AMOUNT,
    TYPE,
    CASE 
        WHEN AMOUNT > 1000 THEN '高风险'
        ELSE '正常'
    END AS RISK_LEVEL
FROM TBL_BUSINESS_DATA
WHERE STATUS = 'ACTIVE';`
    },
    
    copyCode() {
      if (navigator.clipboard) {
        navigator.clipboard.writeText(this.generatedCode).then(() => {
          this.$message.success('代码已复制到剪贴板')
        })
      } else {
        const textarea = document.createElement('textarea')
        textarea.value = this.generatedCode
        document.body.appendChild(textarea)
        textarea.select()
        document.execCommand('copy')
        document.body.removeChild(textarea)
        this.$message.success('代码已复制到剪贴板')
      }
    },
    
    downloadCode() {
      if (!this.generatedCode) {
        this.$message.warning('请先生成代码')
        return
      }
      
      const extensions = {
        'JAVA': '.java',
        'JAVASCRIPT': '.js',
        'PYTHON': '.py',
        'SQL': '.sql'
      }
      
      const fileName = (this.data.ruleCode || 'Expression') + (extensions[this.codeForm.language] || '.txt')
      const blob = new Blob([this.generatedCode], { type: 'text/plain' })
      const url = URL.createObjectURL(blob)
      
      const link = document.createElement('a')
      link.href = url
      link.download = fileName
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
      URL.revokeObjectURL(url)
      
      this.$message.success('文件下载成功')
    },
    
    handleClose() {
      this.$emit('update:visible', false)
    }
  }
}
</script>

<style scoped>
.code-container {
  margin-top: 20px;
}

.code-header {
  margin-bottom: 10px;
  text-align: right;
}

.code-textarea {
  font-family: 'Courier New', monospace;
  font-size: 12px;
}

.dialog-footer {
  text-align: right;
}
</style>
