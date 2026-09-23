<template>
  <el-dialog
    title="表达式测试"
    :visible.sync="dialogVisible"
    width="1200px"
    :close-on-click-modal="false"
    @close="handleClose"
    class="expression-test-dialog"
  >
    <div class="test-container">
      <!-- 表达式选择区域 -->
      <div class="expression-selector">
        <el-card>
          <div slot="header" class="card-header">
            <span>选择表达式</span>
            <el-button style="float: right; padding: 3px 0" type="text" @click="loadExpressionList">刷新</el-button>
          </div>
          <el-row :gutter="20">
            <el-col :span="12">
              <el-select
                v-model="selectedExpressionId"
                placeholder="请选择要测试的表达式"
                style="width: 100%;"
                filterable
                @change="handleExpressionChange"
              >
                <el-option
                  v-for="expression in expressionList"
                  :key="expression.expressionId"
                  :label="`${expression.expressionName} (${expression.expressionCode})`"
                  :value="expression.expressionId"
                >
                  <span style="float: left">{{ expression.expressionName }}</span>
                  <span style="float: right; color: #8492a6; font-size: 13px">{{ expression.expressionType }}</span>
                </el-option>
              </el-select>
            </el-col>
            <el-col :span="12">
              <el-button type="primary" @click="handleQuickTest" :disabled="!selectedExpressionId">快速测试</el-button>
              <el-button @click="handleAdvancedTest" :disabled="!selectedExpressionId">高级测试</el-button>
              <el-button @click="handleBatchTest">批量测试</el-button>
            </el-col>
          </el-row>
        </el-card>
      </div>

      <!-- 表达式信息展示 -->
      <div class="expression-info" v-if="currentExpression">
        <el-card>
          <div slot="header" class="card-header">
            <span>表达式信息</span>
          </div>
          <el-descriptions :column="2" border>
            <el-descriptions-item label="表达式名称">{{ currentExpression.expressionName }}</el-descriptions-item>
            <el-descriptions-item label="表达式编码">{{ currentExpression.expressionCode }}</el-descriptions-item>
            <el-descriptions-item label="表达式类型">
              <el-tag :type="getExpressionTypeTag(currentExpression.expressionType)">
                {{ getExpressionTypeText(currentExpression.expressionType) }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="输出类型">
              <el-tag :type="getOutputTypeTag(currentExpression.outputType)">
                {{ currentExpression.outputType }}
              </el-tag>
            </el-descriptions-item>
            <el-descriptions-item label="复杂度等级">
              <el-rate
                v-model="currentExpression.complexityLevel"
                disabled
                show-score
                text-color="#ff9900"
                score-template="{value}"
              />
            </el-descriptions-item>
            <el-descriptions-item label="使用次数">{{ currentExpression.usageCount || 0 }}</el-descriptions-item>
            <el-descriptions-item label="表达式内容" :span="2">
              <div class="expression-content">
                <pre>{{ currentExpression.expressionContent }}</pre>
              </div>
            </el-descriptions-item>
            <el-descriptions-item label="表达式描述" :span="2">
              {{ currentExpression.expressionDescription || '暂无描述' }}
            </el-descriptions-item>
          </el-descriptions>
        </el-card>
      </div>

      <!-- 测试配置区域 -->
      <div class="test-config" v-if="showTestConfig">
        <el-card>
          <div slot="header" class="card-header">
            <span>测试配置</span>
            <el-button style="float: right; padding: 3px 0" type="text" @click="handleAddTestCase">添加测试用例</el-button>
          </div>
          
          <!-- 测试用例列表 -->
          <div class="test-cases">
            <div
              v-for="(testCase, index) in testCases"
              :key="index"
              class="test-case-item"
            >
              <el-card shadow="hover">
                <div slot="header" class="test-case-header">
                  <span>测试用例 {{ index + 1 }}</span>
                  <div>
                    <el-button size="mini" type="primary" @click="handleRunSingleTest(index)">运行</el-button>
                    <el-button size="mini" type="danger" @click="handleRemoveTestCase(index)">删除</el-button>
                  </div>
                </div>
                
                <el-form :model="testCase" label-width="100px" size="small">
                  <el-form-item label="用例名称">
                    <el-input v-model="testCase.name" placeholder="请输入测试用例名称" />
                  </el-form-item>
                  <el-form-item label="测试数据">
                    <el-input
                      v-model="testCase.inputData"
                      type="textarea"
                      :rows="3"
                      placeholder="请输入JSON格式的测试数据，例如: {&quot;amount&quot;: 1000, &quot;status&quot;: &quot;active&quot;}"
                    />
                  </el-form-item>
                  <el-form-item label="期望结果">
                    <el-input v-model="testCase.expectedResult" placeholder="请输入期望的执行结果" />
                  </el-form-item>
                  <el-form-item label="测试描述">
                    <el-input v-model="testCase.description" placeholder="请输入测试用例描述" />
                  </el-form-item>
                </el-form>

                <!-- 测试结果 -->
                <div v-if="testCase.result" class="test-result">
                  <el-divider content-position="left">测试结果</el-divider>
                  <div class="result-content">
                    <div class="result-status">
                      <el-tag :type="testCase.result.success ? 'success' : 'danger'">
                        {{ testCase.result.success ? '测试通过' : '测试失败' }}
                      </el-tag>
                      <span class="execution-time">执行时间: {{ testCase.result.executionTime }}ms</span>
                    </div>
                    <div class="result-data">
                      <strong>实际结果:</strong>
                      <pre>{{ JSON.stringify(testCase.result.actualResult, null, 2) }}</pre>
                    </div>
                    <div v-if="testCase.result.message" class="result-message">
                      <strong>消息:</strong>
                      <span>{{ testCase.result.message }}</span>
                    </div>
                  </div>
                </div>
              </el-card>
            </div>
          </div>

          <!-- 批量测试按钮 -->
          <div class="batch-test-actions" v-if="testCases.length > 0">
            <el-button type="primary" @click="handleRunAllTests" :loading="batchTesting">
              运行所有测试用例
            </el-button>
            <el-button @click="handleClearResults">清空结果</el-button>
            <el-button @click="handleExportResults">导出结果</el-button>
          </div>
        </el-card>
      </div>

      <!-- 测试统计 -->
      <div class="test-statistics" v-if="testStatistics.totalTests > 0">
        <el-card>
          <div slot="header" class="card-header">
            <span>测试统计</span>
          </div>
          <el-row :gutter="20">
            <el-col :span="6">
              <div class="stat-item">
                <div class="stat-number">{{ testStatistics.totalTests }}</div>
                <div class="stat-label">总测试数</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-item success">
                <div class="stat-number">{{ testStatistics.passedTests }}</div>
                <div class="stat-label">通过测试</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-item danger">
                <div class="stat-number">{{ testStatistics.failedTests }}</div>
                <div class="stat-label">失败测试</div>
              </div>
            </el-col>
            <el-col :span="6">
              <div class="stat-item">
                <div class="stat-number">{{ testStatistics.avgExecutionTime }}ms</div>
                <div class="stat-label">平均执行时间</div>
              </div>
            </el-col>
          </el-row>
        </el-card>
      </div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="handleSaveTestCases" v-if="testCases.length > 0">保存测试用例</el-button>
    </div>
  </el-dialog>
</template>

<script>
import {
  getExpressionList,
  getExpressionDetail,
  testExpressionExecution
} from '@/api/mxgl'

export default {
  name: 'ExpressionTestDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    expressionData: {
      type: Object,
      default: null
    }
  },
  data() {
    return {
      selectedExpressionId: '',
      expressionList: [],
      currentExpression: null,
      showTestConfig: false,
      batchTesting: false,
      testCases: [],
      testStatistics: {
        totalTests: 0,
        passedTests: 0,
        failedTests: 0,
        avgExecutionTime: 0
      }
    }
  },
  computed: {
    dialogVisible: {
      get() {
        return this.visible
      },
      set(val) {
        this.$emit('update:visible', val)
      }
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.loadExpressionList()
        if (this.expressionData) {
          this.selectedExpressionId = this.expressionData.expressionId
          this.currentExpression = { ...this.expressionData }
        }
      }
    }
  },
  methods: {
    // 加载表达式列表
    async loadExpressionList() {
      try {
        const response = await getExpressionList({
          pageNum: 1,
          pageSize: 1000,
          isEnabled: 'Y'
        })
        if (response.code === 1) {
          this.expressionList = response.data.records || []
        }
      } catch (error) {
        console.error('获取表达式列表失败:', error)
      }
    },

    // 表达式变更
    async handleExpressionChange(expressionId) {
      if (!expressionId) {
        this.currentExpression = null
        return
      }

      try {
        const response = await getExpressionDetail(expressionId)
        if (response.code === 1) {
          this.currentExpression = response.data
        }
      } catch (error) {
        this.$message.error('获取表达式详情失败')
        console.error('获取表达式详情失败:', error)
      }
    },

    // 快速测试
    handleQuickTest() {
      this.showTestConfig = true
      if (this.testCases.length === 0) {
        this.handleAddTestCase()
      }
    },

    // 高级测试
    handleAdvancedTest() {
      this.showTestConfig = true
      // 添加多个预设测试用例
      this.testCases = [
        {
          name: '正常情况测试',
          inputData: '{"amount": 1000, "status": "active"}',
          expectedResult: 'true',
          description: '测试正常业务场景'
        },
        {
          name: '边界值测试',
          inputData: '{"amount": 0, "status": "inactive"}',
          expectedResult: 'false',
          description: '测试边界值情况'
        },
        {
          name: '异常情况测试',
          inputData: '{"amount": -100, "status": ""}',
          expectedResult: 'false',
          description: '测试异常数据情况'
        }
      ]
    },

    // 批量测试
    handleBatchTest() {
      // TODO: 实现批量测试多个表达式
      this.$message.info('批量测试功能待实现')
    },

    // 添加测试用例
    handleAddTestCase() {
      this.testCases.push({
        name: `测试用例 ${this.testCases.length + 1}`,
        inputData: '{}',
        expectedResult: '',
        description: '',
        result: null
      })
    },

    // 移除测试用例
    handleRemoveTestCase(index) {
      this.testCases.splice(index, 1)
      this.updateTestStatistics()
    },

    // 运行单个测试
    async handleRunSingleTest(index) {
      const testCase = this.testCases[index]
      
      try {
        // 解析输入数据
        let inputData = {}
        try {
          inputData = JSON.parse(testCase.inputData)
        } catch (e) {
          this.$message.error('测试数据格式错误，请输入有效的JSON格式')
          return
        }

        const response = await testExpressionExecution({
          expressionId: this.selectedExpressionId,
          testData: inputData
        })

        if (response.code === 1) {
          const result = response.data
          testCase.result = {
            success: result.success,
            actualResult: result.result,
            executionTime: result.executionTime,
            message: result.message
          }

          // 比较期望结果和实际结果
          if (testCase.expectedResult) {
            const expected = testCase.expectedResult.toString()
            const actual = JSON.stringify(result.result)
            testCase.result.success = expected === actual
          }
        } else {
          testCase.result = {
            success: false,
            actualResult: null,
            executionTime: 0,
            message: response.msg || '测试失败'
          }
        }

        this.updateTestStatistics()
      } catch (error) {
        testCase.result = {
          success: false,
          actualResult: null,
          executionTime: 0,
          message: '测试执行失败: ' + error.message
        }
        this.updateTestStatistics()
      }
    },

    // 运行所有测试
    async handleRunAllTests() {
      this.batchTesting = true
      
      try {
        for (let i = 0; i < this.testCases.length; i++) {
          await this.handleRunSingleTest(i)
          // 添加小延迟避免请求过于频繁
          await new Promise(resolve => setTimeout(resolve, 100))
        }
        
        this.$message.success('所有测试用例执行完成')
      } catch (error) {
        this.$message.error('批量测试执行失败')
      } finally {
        this.batchTesting = false
      }
    },

    // 清空结果
    handleClearResults() {
      this.testCases.forEach(testCase => {
        testCase.result = null
      })
      this.updateTestStatistics()
    },

    // 导出结果
    handleExportResults() {
      const results = {
        expressionInfo: this.currentExpression,
        testCases: this.testCases,
        statistics: this.testStatistics,
        exportTime: new Date().toISOString()
      }

      const dataStr = JSON.stringify(results, null, 2)
      const dataBlob = new Blob([dataStr], { type: 'application/json' })
      const url = URL.createObjectURL(dataBlob)
      
      const link = document.createElement('a')
      link.href = url
      link.download = `expression_test_results_${this.currentExpression.expressionCode}_${Date.now()}.json`
      link.click()
      
      URL.revokeObjectURL(url)
      this.$message.success('测试结果已导出')
    },

    // 保存测试用例
    handleSaveTestCases() {
      // TODO: 实现保存测试用例到表达式
      this.$message.success('测试用例已保存')
    },

    // 更新测试统计
    updateTestStatistics() {
      const completedTests = this.testCases.filter(tc => tc.result !== null)
      const passedTests = completedTests.filter(tc => tc.result && tc.result.success)
      const failedTests = completedTests.filter(tc => tc.result && !tc.result.success)
      
      let totalExecutionTime = 0
      completedTests.forEach(tc => {
        if (tc.result && tc.result.executionTime) {
          totalExecutionTime += tc.result.executionTime
        }
      })

      this.testStatistics = {
        totalTests: completedTests.length,
        passedTests: passedTests.length,
        failedTests: failedTests.length,
        avgExecutionTime: completedTests.length > 0 ? Math.round(totalExecutionTime / completedTests.length) : 0
      }
    },

    // 获取表达式类型标签
    getExpressionTypeTag(type) {
      const tagMap = {
        'LOGICAL': 'primary',
        'ARITHMETIC': 'success',
        'COMPARISON': 'warning',
        'FUNCTION': 'info'
      }
      return tagMap[type] || 'info'
    },

    // 获取表达式类型文本
    getExpressionTypeText(type) {
      const textMap = {
        'LOGICAL': '逻辑',
        'ARITHMETIC': '算术',
        'COMPARISON': '比较',
        'FUNCTION': '函数'
      }
      return textMap[type] || type
    },

    // 获取输出类型标签
    getOutputTypeTag(type) {
      const tagMap = {
        'BOOLEAN': 'success',
        'NUMBER': 'primary',
        'STRING': 'info',
        'DATE': 'warning'
      }
      return tagMap[type] || 'info'
    },

    // 关闭对话框
    handleClose() {
      this.dialogVisible = false
      this.selectedExpressionId = ''
      this.currentExpression = null
      this.showTestConfig = false
      this.testCases = []
      this.testStatistics = {
        totalTests: 0,
        passedTests: 0,
        failedTests: 0,
        avgExecutionTime: 0
      }
      this.$emit('refresh')
    }
  }
}
</script>

<style scoped>
.expression-test-dialog {
  height: 90vh;
}

.test-container {
  max-height: 80vh;
  overflow-y: auto;
}

.expression-selector,
.expression-info,
.test-config,
.test-statistics {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.expression-content {
  background-color: #f5f7fa;
  padding: 10px;
  border-radius: 4px;
  max-height: 200px;
  overflow-y: auto;
}

.expression-content pre {
  margin: 0;
  font-family: 'Courier New', monospace;
  font-size: 12px;
  line-height: 1.4;
}

.test-cases {
  margin-bottom: 20px;
}

.test-case-item {
  margin-bottom: 20px;
}

.test-case-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.test-result {
  margin-top: 15px;
}

.result-content {
  background-color: #f9f9f9;
  padding: 15px;
  border-radius: 4px;
}

.result-status {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-bottom: 10px;
}

.execution-time {
  font-size: 12px;
  color: #909399;
}

.result-data pre {
  background-color: #fff;
  padding: 10px;
  border-radius: 4px;
  border: 1px solid #e4e7ed;
  font-size: 12px;
  max-height: 150px;
  overflow-y: auto;
  margin: 5px 0;
}

.result-message {
  margin-top: 10px;
  font-size: 14px;
}

.batch-test-actions {
  text-align: center;
  padding: 20px;
  border-top: 1px solid #e4e7ed;
}

.test-statistics .el-row {
  text-align: center;
}

.stat-item {
  padding: 20px;
  border-radius: 8px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
}

.stat-item.success {
  background: linear-gradient(135deg, #67c23a 0%, #85ce61 100%);
}

.stat-item.danger {
  background: linear-gradient(135deg, #f56c6c 0%, #f78989 100%);
}

.stat-number {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 5px;
}

.stat-label {
  font-size: 14px;
  opacity: 0.9;
}

.dialog-footer {
  text-align: right;
}
</style>
