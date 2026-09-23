<template>
  <el-dialog
    :title="isEdit ? '编辑表达式规则' : '新增表达式规则'"
    :visible.sync="visible"
    width="1200px"
    :before-close="handleClose"
    append-to-body
    class="expression-edit-dialog"
  >
    <el-form
      ref="form"
      :model="form"
      :rules="rules"
      label-width="120px"
      size="small"
    >
      <!-- 基本信息 -->
      <div class="form-section">
        <h4 class="section-title">基本信息</h4>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="表达式名称" prop="expressionName">
              <el-input v-model="form.expressionName" placeholder="请输入表达式名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="表达式编码" prop="expressionCode">
              <el-input v-model="form.expressionCode" placeholder="请输入表达式编码" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="表达式类型" prop="expressionType">
              <el-select v-model="form.expressionType" placeholder="请选择类型" style="width: 100%">
                <el-option label="逻辑表达式" value="LOGICAL" />
                <el-option label="算术表达式" value="ARITHMETIC" />
                <el-option label="比较表达式" value="COMPARISON" />
                <el-option label="函数表达式" value="FUNCTION" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="表达式分类" prop="expressionCategory">
              <el-select v-model="form.expressionCategory" placeholder="请选择分类" style="width: 100%">
                <el-option label="财务审计" value="FINANCIAL_AUDIT" />
                <el-option label="风险控制" value="RISK_CONTROL" />
                <el-option label="合规检查" value="COMPLIANCE_CHECK" />
                <el-option label="数据验证" value="DATA_VALIDATION" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="输出类型" prop="outputType">
              <el-select v-model="form.outputType" placeholder="请选择输出类型" style="width: 100%">
                <el-option label="BOOLEAN" value="BOOLEAN" />
                <el-option label="NUMBER" value="NUMBER" />
                <el-option label="STRING" value="STRING" />
                <el-option label="DATE" value="DATE" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="表达式描述" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="2"
            placeholder="请输入表达式描述"
          />
        </el-form-item>
      </div>

      <!-- 数据源配置 -->
      <div class="form-section">
        <h4 class="section-title">数据源配置</h4>

        <!-- 源数据源配置 -->
        <div class="datasource-config">
          <h5>源数据源</h5>
          <DataSourceFieldSelector
            v-model="form.sourceDataConfig"
            :required="true"
            @change="handleSourceDataChange"
          />
        </div>

        <!-- 目标数据源配置（可选） -->
        <div class="datasource-config" v-if="form.expressionType === 'COMPARISON'">
          <h5>
            目标数据源（可选）
            <el-tooltip content="用于跨数据源字段比对，如果不选择则与源数据源相同" placement="top">
              <i class="el-icon-question"></i>
            </el-tooltip>
          </h5>
          <el-checkbox v-model="enableTargetDataSource" @change="handleTargetDataSourceToggle">
            启用目标数据源
          </el-checkbox>
          <DataSourceFieldSelector
            v-if="enableTargetDataSource"
            v-model="form.targetDataConfig"
            @change="handleTargetDataChange"
          />
        </div>
      </div>

      <!-- 比对规则配置 -->
      <div class="form-section" v-if="form.expressionType === 'COMPARISON'">
        <h4 class="section-title">比对规则配置</h4>
        <ComparisonRuleBuilder
          v-model="form.comparisonRules"
          :source-fields="sourceFields"
          :target-fields="targetFields"
          @change="handleRulesChange"
          @validate="handleRuleValidate"
        />
      </div>

      <!-- 表达式内容 -->
      <div class="form-section">
        <h4 class="section-title">表达式内容</h4>
        <el-form-item label="表达式内容" prop="expressionContent">
          <el-input
            v-model="form.expressionContent"
            type="textarea"
            :rows="6"
            placeholder="表达式内容将根据比对规则自动生成，也可以手动编辑"
            :readonly="form.expressionType === 'COMPARISON' && form.comparisonRules.length > 0"
          />
          <div class="expression-help">
            <el-button type="text" @click="showExpressionHelp">表达式语法帮助</el-button>
            <el-button type="text" @click="validateExpression">验证表达式</el-button>
            <el-button type="text" @click="generateFromRules" v-if="form.expressionType === 'COMPARISON'">
              从规则生成
            </el-button>
          </div>
        </el-form-item>
      </div>

      <!-- 其他配置 -->
      <div class="form-section">
        <h4 class="section-title">其他配置</h4>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="状态" prop="isEnabled">
              <el-radio-group v-model="form.isEnabled">
                <el-radio label="Y">启用</el-radio>
                <el-radio label="N">禁用</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="是否模板" prop="isTemplate">
              <el-switch
                v-model="form.isTemplate"
                active-value="Y"
                inactive-value="N"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="复杂度等级" prop="complexityLevel">
              <el-rate
                v-model="form.complexityLevel"
                :max="5"
                show-text
                :texts="['很简单', '简单', '一般', '复杂', '很复杂']"
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="标签">
          <el-tag
            v-for="tag in form.tags"
            :key="tag"
            closable
            @close="removeTag(tag)"
            style="margin-right: 10px;"
          >
            {{ tag }}
          </el-tag>
          <el-input
            v-if="inputVisible"
            ref="saveTagInput"
            v-model="inputValue"
            size="small"
          style="width: 100px;"
          @keyup.enter.native="handleInputConfirm"
          @blur="handleInputConfirm"
        />
        <el-button v-else size="small" @click="showInput">+ 添加标签</el-button>
      </el-form-item>
      </div>
    </el-form>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">取消</el-button>
      <el-button type="primary" @click="handleSubmit" :loading="loading">确定</el-button>
    </div>
    
    <!-- 表达式帮助对话框 -->
    <el-dialog
      title="表达式语法帮助"
      :visible.sync="helpVisible"
      width="600px"
      append-to-body
    >
      <div class="help-content">
        <h4>变量引用</h4>
        <p>使用 ${变量名} 的格式引用变量，如：${amount}、${type}</p>
        
        <h4>运算符</h4>
        <ul>
          <li>算术运算：+、-、*、/、%</li>
          <li>比较运算：>、>=、<、<=、==、!=</li>
          <li>逻辑运算：&&、||、!</li>
        </ul>
        
        <h4>函数</h4>
        <ul>
          <li>字符串函数：length(str)、substring(str, start, end)、contains(str, substr)</li>
          <li>数学函数：abs(num)、max(a, b)、min(a, b)、round(num)</li>
          <li>日期函数：now()、dateFormat(date, format)、dateDiff(date1, date2)</li>
        </ul>
        
        <h4>示例</h4>
        <pre>
${amount} > 1000 && ${type} == 'URGENT'
length(${name}) > 5
${createTime} > dateFormat(now(), 'yyyy-MM-dd')
        </pre>
      </div>
    </el-dialog>
  </el-dialog>
</template>

<script>
import { saveExpression, validateExpressionSyntax } from '@/api/mxgl'
import DataSourceFieldSelector from './DataSourceFieldSelector.vue'
import ComparisonRuleBuilder from './ComparisonRuleBuilder.vue'

export default {
  name: 'ExpressionEditDialog',
  components: {
    DataSourceFieldSelector,
    ComparisonRuleBuilder
  },
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    expressionData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      loading: false,
      helpVisible: false,
      inputVisible: false,
      inputValue: '',

      // 数据源相关
      enableTargetDataSource: false,
      sourceFields: [],
      targetFields: [],

      form: {
        expressionId: '',
        expressionName: '',
        expressionCode: '',
        expressionType: 'COMPARISON',
        expressionCategory: 'FINANCIAL_AUDIT',
        outputType: 'BOOLEAN',
        description: '',
        expressionContent: '',
        isEnabled: 'Y',
        isTemplate: 'N',
        complexityLevel: 1,
        tags: [],

        // 数据源配置
        sourceDataConfig: {
          dataSourceId: '',
          tableName: '',
          selectedFields: []
        },
        targetDataConfig: {
          dataSourceId: '',
          tableName: '',
          selectedFields: []
        },

        // 比对规则
        comparisonRules: []
      },
      rules: {
        expressionName: [
          { required: true, message: '请输入表达式名称', trigger: 'blur' }
        ],
        expressionCode: [
          { required: true, message: '请输入表达式编码', trigger: 'blur' }
        ],
        expressionType: [
          { required: true, message: '请选择表达式类型', trigger: 'change' }
        ],
        expressionCategory: [
          { required: true, message: '请选择表达式分类', trigger: 'change' }
        ],
        outputType: [
          { required: true, message: '请选择输出类型', trigger: 'change' }
        ],
        expressionContent: [
          { required: true, message: '请输入表达式内容', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    isEdit() {
      return !!this.expressionData.expressionId
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.initForm()
      }
    },
    'form.sourceDataConfig': {
      handler(newVal) {
        if (newVal && newVal.selectedFields) {
          this.sourceFields = newVal.selectedFields
        }
      },
      deep: true
    },
    'form.targetDataConfig': {
      handler(newVal) {
        if (newVal && newVal.selectedFields) {
          this.targetFields = newVal.selectedFields
        }
      },
      deep: true
    }
  },
  methods: {
    initForm() {
      if (this.isEdit) {
        this.form = { ...this.expressionData }
        this.form.tags = this.expressionData.tags || []
        this.form.sourceDataConfig = this.expressionData.sourceDataConfig || {
          dataSourceId: '',
          tableName: '',
          selectedFields: []
        }
        this.form.targetDataConfig = this.expressionData.targetDataConfig || {
          dataSourceId: '',
          tableName: '',
          selectedFields: []
        }
        this.form.comparisonRules = this.expressionData.comparisonRules || []
        this.enableTargetDataSource = !!(this.form.targetDataConfig.dataSourceId)
      } else {
        this.form = {
          expressionId: '',
          expressionName: '',
          expressionCode: '',
          expressionType: 'COMPARISON',
          expressionCategory: 'FINANCIAL_AUDIT',
          outputType: 'BOOLEAN',
          description: '',
          expressionContent: '',
          isEnabled: 'Y',
          isTemplate: 'N',
          complexityLevel: 1,
          tags: [],
          sourceDataConfig: {
            dataSourceId: '',
            tableName: '',
            selectedFields: []
          },
          targetDataConfig: {
            dataSourceId: '',
            tableName: '',
            selectedFields: []
          },
          comparisonRules: []
        }
        this.enableTargetDataSource = false
      }
    },

    // 数据源变化处理
    handleSourceDataChange(config) {
      this.form.sourceDataConfig = config
      this.sourceFields = config.selectedFields || []
    },

    handleTargetDataChange(config) {
      this.form.targetDataConfig = config
      this.targetFields = config.selectedFields || []
    },

    handleTargetDataSourceToggle(enabled) {
      this.enableTargetDataSource = enabled
      if (!enabled) {
        this.form.targetDataConfig = {
          dataSourceId: '',
          tableName: '',
          selectedFields: []
        }
        this.targetFields = this.sourceFields // 使用源字段作为目标字段
      }
    },

    // 比对规则变化处理
    handleRulesChange(rules) {
      this.form.comparisonRules = rules
      this.generateExpressionFromRules()
    },

    handleRuleValidate(expression) {
      this.form.expressionContent = expression
    },

    // 从规则生成表达式
    generateFromRules() {
      this.generateExpressionFromRules()
    },

    generateExpressionFromRules() {
      if (this.form.comparisonRules.length === 0) return

      const expressions = this.form.comparisonRules.map((rule, index) => {
        let expr = this.generateSingleRuleExpression(rule)
        if (index < this.form.comparisonRules.length - 1 && rule.logicOperator) {
          expr += ` ${rule.logicOperator} `
        }
        return expr
      })

      this.form.expressionContent = expressions.join('')
    },

    generateSingleRuleExpression(rule) {
      if (!rule.sourceField || !rule.operator) return ''

      let expr = `${rule.sourceField} ${rule.operator} `

      if (rule.valueType === 'FIELD') {
        expr += rule.targetField
      } else if (rule.valueType === 'VALUE') {
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

    showExpressionHelp() {
      this.helpVisible = true
    },

    async validateExpression() {
      if (!this.form.expressionContent) {
        this.$message.warning('请先输入表达式内容')
        return
      }

      try {
        const response = await validateExpressionSyntax({
          expression: this.form.expressionContent,
          expressionType: this.form.expressionType
        })

        if (response.code === 1) {
          this.$message.success('表达式语法正确')
        } else {
          this.$message.error('表达式语法错误：' + response.msg)
        }
      } catch (error) {
        this.$message.error('验证失败：' + error.message)
      }
    },
    
    showInput() {
      this.inputVisible = true
      this.$nextTick(() => {
        this.$refs.saveTagInput.$refs.input.focus()
      })
    },
    
    handleInputConfirm() {
      const inputValue = this.inputValue
      if (inputValue && !this.form.tags.includes(inputValue)) {
        this.form.tags.push(inputValue)
      }
      this.inputVisible = false
      this.inputValue = ''
    },
    
    removeTag(tag) {
      this.form.tags.splice(this.form.tags.indexOf(tag), 1)
    },
    
    handleSubmit() {
      this.$refs.form.validate(async (valid) => {
        if (valid) {
          // 验证数据源配置
          if (this.form.expressionType === 'COMPARISON') {
            if (!this.form.sourceDataConfig.dataSourceId) {
              this.$message.warning('请选择源数据源')
              return
            }
            if (!this.form.sourceDataConfig.tableName) {
              this.$message.warning('请选择源数据表')
              return
            }
            if (this.form.sourceDataConfig.selectedFields.length === 0) {
              this.$message.warning('请至少选择一个源字段')
              return
            }
            if (this.form.comparisonRules.length === 0) {
              this.$message.warning('请至少配置一个比对规则')
              return
            }
          }

          this.loading = true
          try {
            const response = await saveExpression(this.form)
            if (response.code === 1) {
              this.$message.success(this.isEdit ? '修改成功' : '新增成功')
              this.$emit('refresh')
              this.handleClose()
            } else {
              this.$message.error(response.msg || '操作失败')
            }
          } catch (error) {
            this.$message.error('操作失败：' + error.message)
          } finally {
            this.loading = false
          }
        }
      })
    },
    
    handleClose() {
      this.$emit('update:visible', false)
    }
  }
}
</script>

<style scoped>
.expression-edit-dialog {
  max-height: 90vh;
  overflow-y: auto;
}

.form-section {
  margin-bottom: 30px;
  padding: 20px;
  border: 1px solid #ebeef5;
  border-radius: 6px;
  background-color: #fafafa;
}

.section-title {
  margin: 0 0 20px 0;
  padding-bottom: 10px;
  border-bottom: 2px solid #409eff;
  color: #303133;
  font-size: 16px;
  font-weight: 600;
}

.datasource-config {
  margin-bottom: 20px;
  padding: 15px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  background-color: #ffffff;
}

.datasource-config h5 {
  margin: 0 0 15px 0;
  color: #606266;
  font-size: 14px;
  font-weight: 500;
}

.expression-help {
  margin-top: 5px;
  text-align: right;
}

.help-content h4 {
  color: #409EFF;
  margin-top: 20px;
  margin-bottom: 10px;
}

.help-content ul {
  margin-left: 20px;
}

.help-content pre {
  background-color: #f5f5f5;
  padding: 10px;
  border-radius: 4px;
  margin-top: 10px;
}

.dialog-footer {
  text-align: right;
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #ebeef5;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .expression-edit-dialog {
    width: 95% !important;
    margin: 0 auto;
  }

  .form-section {
    padding: 15px;
  }
}
</style>
