<template>
  <div class="evaluation-model-test">
    <el-tabs v-model="activeTab" type="border-card">
      <!-- 测试配置 -->
      <el-tab-pane label="测试配置" name="config">
        <el-form :model="testForm" :rules="testRules" ref="testForm" label-width="120px">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="测试数据源">
                <el-input
                  v-model="modelDataSourceInfo"
                  placeholder="自动获取模型数据源"
                  readonly
                  style="background-color: #f5f7fa;"
                >
                  <template slot="prepend">
                    <i class="el-icon-database"></i>
                  </template>
                </el-input>
                <div v-if="!modelDataSourceInfo" class="data-source-tip">
                  <i class="el-icon-warning" style="color: #e6a23c;"></i>
                  <span style="color: #e6a23c; font-size: 12px;">该模型未关联数据源</span>
                </div>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="测试数据量" prop="testDataSize">
                <el-input-number
                  v-model="testForm.testDataSize"
                  :min="1"
                  :max="10000"
                  placeholder="测试数据量"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="测试类型" prop="testType">
                <el-select v-model="testForm.testType" placeholder="请选择测试类型" style="width: 100%">
                  <el-option label="功能测试" value="FUNCTIONAL" />
                  <el-option label="性能测试" value="PERFORMANCE" />
                  <el-option label="准确性测试" value="ACCURACY" />
                  <el-option label="压力测试" value="STRESS" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="并发数" prop="concurrency">
                <el-input-number
                  v-model="testForm.concurrency"
                  :min="1"
                  :max="100"
                  placeholder="并发数"
                  style="width: 100%"
                />
              </el-form-item>
            </el-col>
          </el-row>

          <el-form-item label="测试参数">
            <el-input
              v-model="testForm.testParameters"
              type="textarea"
              :rows="4"
              placeholder="请输入测试参数（JSON格式）"
            />
          </el-form-item>

          <el-form-item label="预期结果">
            <el-input
              v-model="testForm.expectedResult"
              type="textarea"
              :rows="3"
              placeholder="请输入预期测试结果"
            />
          </el-form-item>

          <el-form-item>
            <el-button type="primary" @click="handleStartTest" :loading="testing">
              <i class="el-icon-video-play"></i> 开始测试
            </el-button>
            <el-button @click="handleStopTest" :disabled="!testing">
              <i class="el-icon-video-pause"></i> 停止测试
            </el-button>
            <el-button @click="handleClearResult">
              <i class="el-icon-delete"></i> 清空结果
            </el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>

      <!-- 测试结果 -->
      <el-tab-pane label="测试结果" name="result">
        <div v-if="testResult" class="test-result">
          <!-- 测试概览 -->
          <div class="result-overview">
            <el-row :gutter="20">
              <el-col :span="6">
                <div class="stat-card">
                  <div class="stat-number" :class="testResult.success ? 'success' : 'error'">
                    {{ testResult.success ? '成功' : '失败' }}
                  </div>
                  <div class="stat-label">测试状态</div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="stat-card">
                  <div class="stat-number">{{ testResult.accuracy || 0 }}%</div>
                  <div class="stat-label">准确率</div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="stat-card">
                  <div class="stat-number">{{ testResult.executionTime || 0 }}ms</div>
                  <div class="stat-label">执行时间</div>
                </div>
              </el-col>
              <el-col :span="6">
                <div class="stat-card">
                  <div class="stat-number">{{ testResult.throughput || 0 }}</div>
                  <div class="stat-label">吞吐量/秒</div>
                </div>
              </el-col>
            </el-row>
          </div>

          <!-- 详细结果 -->
          <div class="result-details">
            <el-card header="测试详情">
              <el-descriptions :column="2" border>
                <el-descriptions-item label="测试开始时间">
                  {{ formatDate(testResult.startTime) }}
                </el-descriptions-item>
                <el-descriptions-item label="测试结束时间">
                  {{ formatDate(testResult.endTime) }}
                </el-descriptions-item>
                <el-descriptions-item label="测试数据量">
                  {{ testResult.testDataCount }}
                </el-descriptions-item>
                <el-descriptions-item label="成功数量">
                  {{ testResult.successCount }}
                </el-descriptions-item>
                <el-descriptions-item label="失败数量">
                  {{ testResult.failureCount }}
                </el-descriptions-item>
                <el-descriptions-item label="错误率">
                  {{ testResult.errorRate }}%
                </el-descriptions-item>
              </el-descriptions>
            </el-card>

            <!-- 性能指标 -->
            <el-card header="性能指标" style="margin-top: 20px;">
              <el-row :gutter="20">
                <el-col :span="8">
                  <div class="metric-item">
                    <div class="metric-label">平均响应时间</div>
                    <div class="metric-value">{{ testResult.avgResponseTime || 0 }}ms</div>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="metric-item">
                    <div class="metric-label">最大响应时间</div>
                    <div class="metric-value">{{ testResult.maxResponseTime || 0 }}ms</div>
                  </div>
                </el-col>
                <el-col :span="8">
                  <div class="metric-item">
                    <div class="metric-label">最小响应时间</div>
                    <div class="metric-value">{{ testResult.minResponseTime || 0 }}ms</div>
                  </div>
                </el-col>
              </el-row>
            </el-card>

            <!-- 错误信息 -->
            <el-card v-if="testResult.errors && testResult.errors.length" header="错误信息" style="margin-top: 20px;">
              <el-table :data="testResult.errors" border stripe>
                <el-table-column prop="errorType" label="错误类型" width="120" />
                <el-table-column prop="errorMessage" label="错误信息" show-overflow-tooltip />
                <el-table-column prop="errorCount" label="错误次数" width="100" />
              </el-table>
            </el-card>
          </div>
        </div>

        <div v-else class="no-result">
          <el-empty description="暂无测试结果，请先执行测试" />
        </div>
      </el-tab-pane>

      <!-- 测试日志 -->
      <el-tab-pane label="测试日志" name="log">
        <div class="test-log">
          <div class="log-toolbar">
            <el-button size="small" @click="handleClearLog">清空日志</el-button>
            <el-button size="small" @click="handleDownloadLog">下载日志</el-button>
          </div>
          <div class="log-content">
            <pre v-if="testLog">{{ testLog }}</pre>
            <div v-else class="no-log">暂无测试日志</div>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>

    <div class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="handleSaveResult" :disabled="!testResult">保存结果</el-button>
    </div>
  </div>
</template>

<script>
import { testEvaluationModel, getDataModelDetail, getDataSourceDetail, getCombinationDetail } from '@/api/mxgl'

export default {
  name: 'EvaluationModelTest',
  props: {
    modelData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      activeTab: 'config',
      testing: false,
      modelDataSourceInfo: '', // 模型关联的数据源信息
      testForm: {
        testDataSize: 100,
        testType: 'FUNCTIONAL',
        concurrency: 1,
        testParameters: '',
        expectedResult: ''
      },
      testRules: {
        testDataSize: [
          { required: true, message: '请输入测试数据量', trigger: 'blur' }
        ],
        testType: [
          { required: true, message: '请选择测试类型', trigger: 'change' }
        ]
      },
      testResult: null,
      testLog: ''
    }
  },
  mounted() {
    console.log('EvaluationModelTest 组件挂载，modelData:', this.modelData)
    // 组件挂载时获取模型数据源信息
    this.loadModelDataSource()
  },
  watch: {
    modelData: {
      handler(newVal, oldVal) {
        console.log('=== modelData watch 被触发 ===')
        console.log('新值:', newVal)
        console.log('旧值:', oldVal)
        if (newVal && newVal.evalModelId) {
          console.log('modelData 有效，调用 loadModelDataSource')
          this.loadModelDataSource()
        } else {
          console.log('modelData 无效或缺少 evalModelId')
        }
      },
      immediate: true,
      deep: true
    }
  },
  methods: {
    // 获取模型关联的数据源信息
    async loadModelDataSource() {
      console.log('loadModelDataSource 被调用，modelData:', this.modelData)

      if (!this.modelData || !this.modelData.dataModelId) {
        console.log('模型数据不完整，dataModelId:', this.modelData?.dataModelId)
        this.modelDataSourceInfo = this.modelData ? '该模型未关联数据模型' : '模型数据为空'
        return
      }

      try {
        console.log('获取数据模型详情，dataModelId:', this.modelData.dataModelId)

        // 判断是数据模型还是指标组合分析
        const isComboModel = this.modelData.dataModelId.startsWith('COMB')
        console.log('模型类型判断:', isComboModel ? '指标组合分析' : '数据模型管理')

        let dataModel = null

        if (isComboModel) {
          // 指标组合分析：直接显示固定文本，不需要获取具体数据源
          console.log('指标组合分析模型，使用固定数据源显示')
          this.modelDataSourceInfo = '组合指标测试数据源'
          return
        } else {
          // 数据模型管理：调用getDataModelDetail
          const dataModelResponse = await getDataModelDetail(this.modelData.dataModelId)
          if (dataModelResponse.code === 1 && dataModelResponse.data) {
            dataModel = dataModelResponse.data
            console.log('数据模型详情:', dataModel)
          } else {
            this.modelDataSourceInfo = `数据模型不存在 (ID: ${this.modelData.dataModelId})`
            console.error('获取数据模型详情失败:', dataModelResponse.msg)
            return
          }
        }

        // 处理数据源信息
        if (dataModel && dataModel.dataSourceId) {

          // 2. 获取数据源详情
            const dataSourceResponse = await getDataSourceDetail(dataModel.dataSourceId)
            console.log('数据源API完整响应:', dataSourceResponse)

            if (dataSourceResponse.code === 1) {
              // 数据可能在data字段或result字段中，需要检查哪个字段有有效数据
              let dataSource = null

              // 检查data字段是否有有效数据（不是空对象）
              if (dataSourceResponse.data && Object.keys(dataSourceResponse.data).length > 0) {
                dataSource = dataSourceResponse.data
                console.log('使用data字段的数据源信息:', dataSource)
              } else if (dataSourceResponse.result) {
                dataSource = dataSourceResponse.result
                console.log('使用result字段的数据源信息:', dataSource)
              }

              if (dataSource && dataSource.sourceName) {
                this.modelDataSourceInfo = `${dataSource.sourceName} (${dataSource.sourceType})`
                console.log('设置数据源显示信息:', this.modelDataSourceInfo)
              } else {
                this.modelDataSourceInfo = '数据源信息不完整'
                console.error('数据源信息不完整:', dataSource)
              }
            } else {
              this.modelDataSourceInfo = '数据源信息获取失败'
              console.error('获取数据源详情失败:', dataSourceResponse.msg)
            }
        } else {
          this.modelDataSourceInfo = '数据模型未关联数据源'
        }
      } catch (error) {
        this.modelDataSourceInfo = '获取数据源信息异常'
        console.error('获取模型数据源信息异常:', error)
      }
    },
    // 开始测试
    async handleStartTest() {
      this.$refs.testForm.validate(async (valid) => {
        if (valid) {
          this.testing = true
          this.testLog = '开始执行模型测试...\n'
          
          try {
            // 验证测试参数JSON格式
            if (this.testForm.testParameters) {
              try {
                JSON.parse(this.testForm.testParameters)
              } catch (error) {
                this.$message.error('测试参数格式不正确，请输入有效的JSON格式')
                this.testing = false
                return
              }
            }

            const response = await testEvaluationModel({
              modelId: this.modelData.evalModelId,
              ...this.testForm
            })

            if (response.code === 1) {
              this.testResult = response.data
              this.testLog += '测试执行完成\n'
              this.testLog += `测试结果: ${this.testResult.success ? '成功' : '失败'}\n`
              this.testLog += `准确率: ${this.testResult.accuracy}%\n`
              this.testLog += `执行时间: ${this.testResult.executionTime}ms\n`
              this.activeTab = 'result'
              this.$message.success('测试执行完成')
            } else {
              this.testLog += `测试失败: ${response.msg}\n`
              this.$message.error(response.msg || '测试执行失败')
            }
          } catch (error) {
            this.testLog += `测试异常: ${error.message}\n`
            this.$message.error('测试执行异常')
          } finally {
            this.testing = false
          }
        }
      })
    },
    // 停止测试
    handleStopTest() {
      this.testing = false
      this.testLog += '测试已停止\n'
      this.$message.info('测试已停止')
    },
    // 清空结果
    handleClearResult() {
      this.testResult = null
      this.$message.success('测试结果已清空')
    },
    // 清空日志
    handleClearLog() {
      this.testLog = ''
      this.$message.success('测试日志已清空')
    },
    // 下载日志
    handleDownloadLog() {
      if (!this.testLog) {
        this.$message.warning('暂无日志可下载')
        return
      }
      
      const blob = new Blob([this.testLog], { type: 'text/plain' })
      const url = window.URL.createObjectURL(blob)
      const link = document.createElement('a')
      link.href = url
      link.download = `model_test_log_${new Date().getTime()}.txt`
      link.click()
      window.URL.revokeObjectURL(url)
    },
    // 保存结果
    handleSaveResult() {
      this.$message.success('测试结果已保存')
    },
    // 关闭
    handleClose() {
      this.$emit('close')
    },
    // 格式化日期
    formatDate(date) {
      if (!date) return '-'
      return new Date(date).toLocaleString('zh-CN')
    }
  }
}
</script>

<style lang="scss" scoped>
.evaluation-model-test {
  .test-result {
    .result-overview {
      margin-bottom: 20px;

      .stat-card {
        background: #fff;
        padding: 20px;
        border-radius: 4px;
        text-align: center;
        border: 1px solid #ebeef5;

        .stat-number {
          font-size: 24px;
          font-weight: bold;
          margin-bottom: 8px;

          &.success {
            color: #67c23a;
          }

          &.error {
            color: #f56c6c;
          }
        }

        .stat-label {
          font-size: 14px;
          color: #666;
        }
      }
    }

    .metric-item {
      text-align: center;
      padding: 15px;
      border: 1px solid #ebeef5;
      border-radius: 4px;

      .metric-label {
        font-size: 14px;
        color: #666;
        margin-bottom: 8px;
      }

      .metric-value {
        font-size: 18px;
        font-weight: bold;
        color: #409eff;
      }
    }
  }

  .no-result {
    text-align: center;
    padding: 40px;
  }

  .test-log {
    .log-toolbar {
      margin-bottom: 10px;
      text-align: right;
    }

    .log-content {
      background: #f5f5f5;
      border: 1px solid #ddd;
      border-radius: 4px;
      height: 400px;
      overflow-y: auto;
      padding: 10px;

      pre {
        margin: 0;
        font-family: 'Courier New', monospace;
        font-size: 12px;
        line-height: 1.4;
        white-space: pre-wrap;
        word-wrap: break-word;
      }

      .no-log {
        text-align: center;
        color: #999;
        padding: 50px;
      }
    }
  }

  .dialog-footer {
    text-align: right;
    margin-top: 20px;
    padding-top: 20px;
    border-top: 1px solid #ebeef5;

    .el-button {
      margin-left: 10px;
    }
  }

  .data-source-tip {
    margin-top: 5px;
    display: flex;
    align-items: center;
    gap: 5px;
  }
}
</style>
