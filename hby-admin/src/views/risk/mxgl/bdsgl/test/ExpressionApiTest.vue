<template>
  <div class="api-test-container">
    <el-card class="test-card">
      <div slot="header" class="clearfix">
        <span>表达式管理API测试</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="runAllTests">运行所有测试</el-button>
      </div>
      
      <!-- 中文公式解析测试 -->
      <el-collapse v-model="activeNames">
        <el-collapse-item title="中文公式解析测试" name="parse">
          <el-form :model="parseForm" label-width="120px">
            <el-form-item label="中文公式">
              <el-input v-model="parseForm.chineseFormula" placeholder="请输入中文公式，如：金额 大于 10000" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="testParseFormula" :loading="parseLoading">测试解析</el-button>
            </el-form-item>
          </el-form>
          <div v-if="parseResult" class="test-result">
            <h4>解析结果：</h4>
            <pre>{{ JSON.stringify(parseResult, null, 2) }}</pre>
          </div>
        </el-collapse-item>

        <!-- 中文公式验证测试 -->
        <el-collapse-item title="中文公式验证测试" name="validate">
          <el-form :model="validateForm" label-width="120px">
            <el-form-item label="中文公式">
              <el-input v-model="validateForm.chineseFormula" placeholder="请输入中文公式进行验证" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="testValidateFormula" :loading="validateLoading">测试验证</el-button>
            </el-form-item>
          </el-form>
          <div v-if="validateResult" class="test-result">
            <h4>验证结果：</h4>
            <pre>{{ JSON.stringify(validateResult, null, 2) }}</pre>
          </div>
        </el-collapse-item>

        <!-- 运算符获取测试 -->
        <el-collapse-item title="运算符获取测试" name="operators">
          <el-button type="primary" @click="testGetOperators" :loading="operatorsLoading">获取支持的运算符</el-button>
          <div v-if="operatorsResult" class="test-result">
            <h4>运算符列表：</h4>
            <pre>{{ JSON.stringify(operatorsResult, null, 2) }}</pre>
          </div>
        </el-collapse-item>

        <!-- 模板获取测试 -->
        <el-collapse-item title="模板获取测试" name="templates">
          <el-form :model="templateForm" label-width="120px">
            <el-form-item label="模板分类">
              <el-select v-model="templateForm.category" placeholder="请选择分类" clearable>
                <el-option label="财务审计" value="FINANCIAL_AUDIT" />
                <el-option label="风险控制" value="RISK_CONTROL" />
                <el-option label="合规检查" value="COMPLIANCE_CHECK" />
                <el-option label="数据验证" value="DATA_VALIDATION" />
              </el-select>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="testGetTemplates" :loading="templatesLoading">获取模板列表</el-button>
            </el-form-item>
          </el-form>
          <div v-if="templatesResult" class="test-result">
            <h4>模板列表：</h4>
            <pre>{{ JSON.stringify(templatesResult, null, 2) }}</pre>
          </div>
        </el-collapse-item>

        <!-- 模板分类测试 -->
        <el-collapse-item title="模板分类测试" name="categories">
          <el-button type="primary" @click="testGetCategories" :loading="categoriesLoading">获取模板分类</el-button>
          <div v-if="categoriesResult" class="test-result">
            <h4>分类列表：</h4>
            <pre>{{ JSON.stringify(categoriesResult, null, 2) }}</pre>
          </div>
        </el-collapse-item>
      </el-collapse>

      <!-- 测试结果汇总 -->
      <div v-if="testSummary.length > 0" class="test-summary">
        <h3>测试结果汇总</h3>
        <el-table :data="testSummary" style="width: 100%">
          <el-table-column prop="testName" label="测试项目" width="200" />
          <el-table-column prop="status" label="状态" width="100">
            <template slot-scope="scope">
              <el-tag :type="scope.row.status === '成功' ? 'success' : 'danger'">
                {{ scope.row.status }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="message" label="结果信息" />
          <el-table-column prop="duration" label="耗时(ms)" width="100" />
        </el-table>
      </div>
    </el-card>
  </div>
</template>

<script>
import {
  parseChineseFormula,
  validateChineseFormula,
  getSupportedOperators,
  getChineseFormulaTemplates,
  getTemplateCategories
} from '@/api/mxgl'

export default {
  name: 'ExpressionApiTest',
  data() {
    return {
      activeNames: ['parse'],
      
      // 解析测试
      parseForm: {
        chineseFormula: '金额 大于 10000 并且 状态 等于 有效'
      },
      parseResult: null,
      parseLoading: false,
      
      // 验证测试
      validateForm: {
        chineseFormula: '金额 大于 10000'
      },
      validateResult: null,
      validateLoading: false,
      
      // 运算符测试
      operatorsResult: null,
      operatorsLoading: false,
      
      // 模板测试
      templateForm: {
        category: ''
      },
      templatesResult: null,
      templatesLoading: false,
      
      // 分类测试
      categoriesResult: null,
      categoriesLoading: false,
      
      // 测试汇总
      testSummary: []
    }
  },
  methods: {
    async testParseFormula() {
      this.parseLoading = true
      const startTime = Date.now()
      
      try {
        const response = await parseChineseFormula({
          chineseFormula: this.parseForm.chineseFormula,
          sourceDataConfig: JSON.stringify({
            dataSourceId: 'DS001',
            tableName: 'TBL_TRANSACTION'
          })
        })
        
        this.parseResult = response
        this.addTestResult('中文公式解析', '成功', response.msg || '解析完成', Date.now() - startTime)
        
        if (response.code === 1) {
          this.$message.success('解析测试成功')
        } else {
          this.$message.error('解析测试失败: ' + response.msg)
        }
      } catch (error) {
        this.parseResult = { error: error.message }
        this.addTestResult('中文公式解析', '失败', error.message, Date.now() - startTime)
        this.$message.error('解析测试异常: ' + error.message)
      } finally {
        this.parseLoading = false
      }
    },

    async testValidateFormula() {
      this.validateLoading = true
      const startTime = Date.now()
      
      try {
        const response = await validateChineseFormula({
          chineseFormula: this.validateForm.chineseFormula
        })
        
        this.validateResult = response
        this.addTestResult('中文公式验证', '成功', response.msg || '验证完成', Date.now() - startTime)
        
        if (response.code === 1) {
          this.$message.success('验证测试成功')
        } else {
          this.$message.error('验证测试失败: ' + response.msg)
        }
      } catch (error) {
        this.validateResult = { error: error.message }
        this.addTestResult('中文公式验证', '失败', error.message, Date.now() - startTime)
        this.$message.error('验证测试异常: ' + error.message)
      } finally {
        this.validateLoading = false
      }
    },

    async testGetOperators() {
      this.operatorsLoading = true
      const startTime = Date.now()
      
      try {
        const response = await getSupportedOperators()
        
        this.operatorsResult = response
        this.addTestResult('获取运算符', '成功', `获取到${response.data?.length || 0}个运算符`, Date.now() - startTime)
        
        if (response.code === 1) {
          this.$message.success('运算符获取成功')
        } else {
          this.$message.error('运算符获取失败: ' + response.msg)
        }
      } catch (error) {
        this.operatorsResult = { error: error.message }
        this.addTestResult('获取运算符', '失败', error.message, Date.now() - startTime)
        this.$message.error('运算符获取异常: ' + error.message)
      } finally {
        this.operatorsLoading = false
      }
    },

    async testGetTemplates() {
      this.templatesLoading = true
      const startTime = Date.now()
      
      try {
        const response = await getChineseFormulaTemplates(this.templateForm.category)
        
        this.templatesResult = response
        this.addTestResult('获取模板列表', '成功', `获取到${response.data?.length || 0}个模板`, Date.now() - startTime)
        
        if (response.code === 1) {
          this.$message.success('模板获取成功')
        } else {
          this.$message.error('模板获取失败: ' + response.msg)
        }
      } catch (error) {
        this.templatesResult = { error: error.message }
        this.addTestResult('获取模板列表', '失败', error.message, Date.now() - startTime)
        this.$message.error('模板获取异常: ' + error.message)
      } finally {
        this.templatesLoading = false
      }
    },

    async testGetCategories() {
      this.categoriesLoading = true
      const startTime = Date.now()
      
      try {
        const response = await getTemplateCategories()
        
        this.categoriesResult = response
        this.addTestResult('获取模板分类', '成功', `获取到${response.data?.length || 0}个分类`, Date.now() - startTime)
        
        if (response.code === 1) {
          this.$message.success('分类获取成功')
        } else {
          this.$message.error('分类获取失败: ' + response.msg)
        }
      } catch (error) {
        this.categoriesResult = { error: error.message }
        this.addTestResult('获取模板分类', '失败', error.message, Date.now() - startTime)
        this.$message.error('分类获取异常: ' + error.message)
      } finally {
        this.categoriesLoading = false
      }
    },

    async runAllTests() {
      this.testSummary = []
      this.$message.info('开始运行所有测试...')
      
      await this.testParseFormula()
      await this.testValidateFormula()
      await this.testGetOperators()
      await this.testGetTemplates()
      await this.testGetCategories()
      
      const successCount = this.testSummary.filter(test => test.status === '成功').length
      const totalCount = this.testSummary.length
      
      if (successCount === totalCount) {
        this.$message.success(`所有测试通过！(${successCount}/${totalCount})`)
      } else {
        this.$message.warning(`部分测试失败 (${successCount}/${totalCount})`)
      }
    },

    addTestResult(testName, status, message, duration) {
      this.testSummary.push({
        testName,
        status,
        message,
        duration
      })
    }
  }
}
</script>

<style scoped>
.api-test-container {
  padding: 20px;
}

.test-card {
  max-width: 1200px;
  margin: 0 auto;
}

.test-result {
  margin-top: 20px;
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.test-result h4 {
  margin-top: 0;
  color: #303133;
}

.test-result pre {
  background-color: #fff;
  padding: 10px;
  border-radius: 4px;
  border: 1px solid #e4e7ed;
  font-size: 12px;
  max-height: 300px;
  overflow-y: auto;
}

.test-summary {
  margin-top: 30px;
  padding-top: 20px;
  border-top: 1px solid #e4e7ed;
}

.test-summary h3 {
  margin-bottom: 15px;
  color: #303133;
}
</style>
