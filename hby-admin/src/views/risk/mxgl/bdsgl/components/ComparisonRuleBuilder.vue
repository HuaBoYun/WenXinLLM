<template>
  <div class="comparison-rule-builder">
    <div class="rule-header">
      <h4>比对规则配置</h4>
      <el-button type="primary" size="small" @click="addRule" icon="el-icon-plus">添加规则</el-button>
    </div>

    <!-- 规则列表 -->
    <div class="rule-list" v-if="rules.length > 0">
      <div 
        v-for="(rule, index) in rules" 
        :key="rule.id"
        class="rule-item"
        :class="{ 'rule-error': rule.hasError }"
      >
        <!-- 规则序号 -->
        <div class="rule-index">{{ index + 1 }}</div>

        <!-- 规则配置 -->
        <div class="rule-config">
          <el-row :gutter="10">
            <!-- 源字段 -->
            <el-col :span="5">
              <el-select
                v-model="rule.sourceField"
                placeholder="选择源字段"
                size="small"
                @change="validateRule(rule)"
              >
                <el-option
                  v-for="field in sourceFields"
                  :key="field"
                  :label="field"
                  :value="field"
                />
              </el-select>
            </el-col>

            <!-- 比对操作符 -->
            <el-col :span="3">
              <el-select
                v-model="rule.operator"
                placeholder="操作符"
                size="small"
                @change="validateRule(rule)"
              >
                <el-option label="等于" value="=" />
                <el-option label="不等于" value="!=" />
                <el-option label="大于" value=">" />
                <el-option label="大于等于" value=">=" />
                <el-option label="小于" value="<" />
                <el-option label="小于等于" value="<=" />
                <el-option label="包含" value="LIKE" />
                <el-option label="不包含" value="NOT LIKE" />
                <el-option label="为空" value="IS NULL" />
                <el-option label="不为空" value="IS NOT NULL" />
                <el-option label="在范围内" value="IN" />
                <el-option label="不在范围内" value="NOT IN" />
              </el-select>
            </el-col>

            <!-- 比对值类型 -->
            <el-col :span="3">
              <el-select
                v-model="rule.valueType"
                placeholder="值类型"
                size="small"
                @change="handleValueTypeChange(rule)"
              >
                <el-option label="字段" value="FIELD" />
                <el-option label="固定值" value="VALUE" />
                <el-option label="参数" value="PARAM" />
              </el-select>
            </el-col>

            <!-- 比对值 -->
            <el-col :span="5">
              <!-- 字段选择 -->
              <el-select
                v-if="rule.valueType === 'FIELD'"
                v-model="rule.targetField"
                placeholder="选择目标字段"
                size="small"
                @change="validateRule(rule)"
              >
                <el-option
                  v-for="field in targetFields"
                  :key="field"
                  :label="field"
                  :value="field"
                />
              </el-select>

              <!-- 固定值输入 -->
              <el-input
                v-else-if="rule.valueType === 'VALUE'"
                v-model="rule.fixedValue"
                placeholder="输入固定值"
                size="small"
                @input="validateRule(rule)"
              />

              <!-- 参数输入 -->
              <el-input
                v-else-if="rule.valueType === 'PARAM'"
                v-model="rule.paramName"
                placeholder="输入参数名"
                size="small"
                @input="validateRule(rule)"
              />
            </el-col>

            <!-- 逻辑连接符 -->
            <el-col :span="2" v-if="index < rules.length - 1">
              <el-select
                v-model="rule.logicOperator"
                placeholder="逻辑"
                size="small"
              >
                <el-option label="AND" value="AND" />
                <el-option label="OR" value="OR" />
              </el-select>
            </el-col>

            <!-- 操作按钮 -->
            <el-col :span="2">
              <el-button-group>
                <el-button
                  type="danger"
                  size="small"
                  icon="el-icon-delete"
                  @click="removeRule(index)"
                />
                <el-button
                  type="info"
                  size="small"
                  icon="el-icon-copy-document"
                  @click="copyRule(rule)"
                />
              </el-button-group>
            </el-col>
          </el-row>
        </div>
      </div>
    </div>

    <!-- 空状态 -->
    <el-empty v-else description="暂无比对规则，点击添加规则开始配置" :image-size="80" />

    <!-- 生成的表达式预览 -->
    <div class="expression-preview" v-if="generatedExpression">
      <h5>生成的表达式：</h5>
      <el-input
        type="textarea"
        :value="generatedExpression"
        :rows="3"
        readonly
        class="expression-text"
      />
      <div class="preview-actions">
        <el-button size="small" @click="copyExpression">复制表达式</el-button>
        <el-button size="small" type="primary" @click="validateExpression">验证语法</el-button>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ComparisonRuleBuilder',
  props: {
    // 源字段列表
    sourceFields: {
      type: Array,
      default: () => []
    },
    // 目标字段列表
    targetFields: {
      type: Array,
      default: () => []
    },
    // 初始规则
    value: {
      type: Array,
      default: () => []
    }
  },
  data() {
    return {
      rules: [],
      ruleIdCounter: 1
    }
  },
  computed: {
    // 生成的表达式
    generatedExpression() {
      if (this.rules.length === 0) return ''
      
      const expressions = this.rules.map((rule, index) => {
        let expr = this.generateSingleRuleExpression(rule)
        
        // 添加逻辑连接符
        if (index < this.rules.length - 1 && rule.logicOperator) {
          expr += ` ${rule.logicOperator} `
        }
        
        return expr
      })
      
      return expressions.join('')
    }
  },
  watch: {
    value: {
      handler(newVal) {
        if (newVal && Array.isArray(newVal)) {
          this.rules = newVal.map(rule => ({
            ...rule,
            id: rule.id || this.generateRuleId()
          }))
        }
      },
      immediate: true,
      deep: true
    },
    rules: {
      handler() {
        this.$emit('input', this.rules)
        this.$emit('change', this.rules)
      },
      deep: true
    }
  },
  methods: {
    // 生成规则ID
    generateRuleId() {
      return `rule_${Date.now()}_${this.ruleIdCounter++}`
    },

    // 添加规则
    addRule() {
      const newRule = {
        id: this.generateRuleId(),
        sourceField: '',
        operator: '=',
        valueType: 'FIELD',
        targetField: '',
        fixedValue: '',
        paramName: '',
        logicOperator: 'AND',
        hasError: false
      }
      this.rules.push(newRule)
    },

    // 移除规则
    removeRule(index) {
      this.rules.splice(index, 1)
    },

    // 复制规则
    copyRule(rule) {
      const newRule = {
        ...rule,
        id: this.generateRuleId()
      }
      this.rules.push(newRule)
    },

    // 值类型变化处理
    handleValueTypeChange(rule) {
      // 清空相关字段
      rule.targetField = ''
      rule.fixedValue = ''
      rule.paramName = ''
      this.validateRule(rule)
    },

    // 验证单个规则
    validateRule(rule) {
      rule.hasError = false
      
      // 检查源字段
      if (!rule.sourceField) {
        rule.hasError = true
        return false
      }
      
      // 检查操作符
      if (!rule.operator) {
        rule.hasError = true
        return false
      }
      
      // 检查比对值
      if (rule.valueType === 'FIELD' && !rule.targetField) {
        rule.hasError = true
        return false
      } else if (rule.valueType === 'VALUE' && !rule.fixedValue) {
        rule.hasError = true
        return false
      } else if (rule.valueType === 'PARAM' && !rule.paramName) {
        rule.hasError = true
        return false
      }
      
      return true
    },

    // 生成单个规则表达式
    generateSingleRuleExpression(rule) {
      if (!this.validateRule(rule)) return ''
      
      let expr = `${rule.sourceField} ${rule.operator} `
      
      if (rule.valueType === 'FIELD') {
        expr += rule.targetField
      } else if (rule.valueType === 'VALUE') {
        // 根据操作符决定是否加引号
        if (rule.operator === 'LIKE' || rule.operator === 'NOT LIKE') {
          expr += `'%${rule.fixedValue}%'`
        } else if (isNaN(rule.fixedValue)) {
          expr += `'${rule.fixedValue}'`
        } else {
          expr += rule.fixedValue
        }
      } else if (rule.valueType === 'PARAM') {
        expr += `#{${rule.paramName}}`
      }
      
      return expr
    },

    // 复制表达式
    copyExpression() {
      if (this.generatedExpression) {
        navigator.clipboard.writeText(this.generatedExpression).then(() => {
          this.$message.success('表达式已复制到剪贴板')
        }).catch(() => {
          this.$message.error('复制失败')
        })
      }
    },

    // 验证表达式
    validateExpression() {
      if (!this.generatedExpression) {
        this.$message.warning('请先配置比对规则')
        return
      }
      
      // 检查所有规则是否有效
      const hasError = this.rules.some(rule => !this.validateRule(rule))
      if (hasError) {
        this.$message.error('存在无效的规则配置，请检查')
        return
      }
      
      this.$message.success('表达式语法正确')
      this.$emit('validate', this.generatedExpression)
    },

    // 获取生成的表达式
    getExpression() {
      return this.generatedExpression
    },

    // 验证所有规则
    validateAll() {
      return this.rules.every(rule => this.validateRule(rule))
    }
  }
}
</script>

<style scoped>
.comparison-rule-builder {
  width: 100%;
}

.rule-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
  padding-bottom: 10px;
  border-bottom: 1px solid #ebeef5;
}

.rule-header h4 {
  margin: 0;
  color: #303133;
}

.rule-list {
  margin-bottom: 20px;
}

.rule-item {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
  padding: 10px;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  background-color: #fafafa;
  transition: all 0.3s;
}

.rule-item:hover {
  border-color: #c0c4cc;
}

.rule-item.rule-error {
  border-color: #f56c6c;
  background-color: #fef0f0;
}

.rule-index {
  width: 30px;
  height: 30px;
  line-height: 30px;
  text-align: center;
  background-color: #409eff;
  color: white;
  border-radius: 50%;
  font-weight: bold;
  margin-right: 15px;
  flex-shrink: 0;
}

.rule-config {
  flex: 1;
}

.expression-preview {
  margin-top: 20px;
  padding: 15px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  background-color: #f9f9f9;
}

.expression-preview h5 {
  margin: 0 0 10px 0;
  color: #303133;
}

.expression-text {
  margin-bottom: 10px;
}

.preview-actions {
  text-align: right;
}
</style>
