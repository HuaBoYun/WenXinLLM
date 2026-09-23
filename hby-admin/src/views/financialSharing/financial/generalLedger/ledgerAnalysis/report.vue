<template>
  <div class="report-generation-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-document"></i>
          财务报告生成
        </h1>
        <p class="page-description">生成和导出各类财务分析报告</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-back" @click="goBack">
          返回分析
        </el-button>
      </div>
    </div>

    <!-- 报告配置 -->
    <div class="report-config">
      <el-steps :active="currentStep" finish-status="success" align-center>
        <el-step title="选择报告类型" description="选择要生成的报告类型"></el-step>
        <el-step title="配置参数" description="设置报告生成参数"></el-step>
        <el-step title="生成报告" description="正在生成报告内容"></el-step>
        <el-step title="预览导出" description="预览并导出报告"></el-step>
      </el-steps>

      <!-- 步骤1：选择报告类型 -->
      <div class="step-content" v-if="currentStep === 0">
        <h3>选择报告类型</h3>
        <el-row :gutter="24">
          <el-col :span="8" v-for="reportType in reportTypes" :key="reportType.value">
            <div
              class="report-type-card"
              :class="{ active: selectedReportType === reportType.value }"
              @click="selectReportType(reportType)">
              <div class="card-icon">
                <i :class="reportType.icon"></i>
              </div>
              <div class="card-content">
                <h4>{{ reportType.name }}</h4>
                <p>{{ reportType.description }}</p>
                <div class="card-tags">
                  <el-tag
                    v-for="tag in reportType.tags"
                    :key="tag"
                    size="mini"
                    style="margin-right: 5px;">
                    {{ tag }}
                  </el-tag>
                </div>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 步骤2：配置参数 -->
      <div class="step-content" v-if="currentStep === 1">
        <h3>配置报告参数</h3>
        <el-form :model="reportConfig" :rules="configRules" ref="configForm" label-width="120px">
          <el-row :gutter="24">
            <el-col :span="12">
              <el-form-item label="报告期间" prop="period">
                <el-date-picker
                  v-model="reportConfig.period"
                  type="month"
                  placeholder="选择报告期间"
                  format="yyyy-MM"
                  value-format="yyyy-MM">
                </el-date-picker>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="对比期间" prop="comparePeriod">
                <el-date-picker
                  v-model="reportConfig.comparePeriod"
                  type="month"
                  placeholder="选择对比期间"
                  format="yyyy-MM"
                  value-format="yyyy-MM">
                </el-date-picker>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row :gutter="24">
            <el-col :span="12">
              <el-form-item label="报告格式" prop="format">
                <el-select v-model="reportConfig.format" placeholder="请选择报告格式">
                  <el-option label="Word文档" value="docx"></el-option>
                  <el-option label="PDF文档" value="pdf"></el-option>
                  <el-option label="Excel表格" value="xlsx"></el-option>
                  <el-option label="HTML网页" value="html"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="报告语言" prop="language">
                <el-select v-model="reportConfig.language" placeholder="请选择报告语言">
                  <el-option label="中文" value="zh-CN"></el-option>
                  <el-option label="英文" value="en-US"></el-option>
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="包含内容" prop="contents">
            <el-checkbox-group v-model="reportConfig.contents">
              <el-checkbox label="summary">执行摘要</el-checkbox>
              <el-checkbox label="charts">图表分析</el-checkbox>
              <el-checkbox label="details">详细数据</el-checkbox>
              <el-checkbox label="trends">趋势分析</el-checkbox>
              <el-checkbox label="comparison">对比分析</el-checkbox>
              <el-checkbox label="abnormal">异常说明</el-checkbox>
              <el-checkbox label="suggestions">改进建议</el-checkbox>
            </el-checkbox-group>
          </el-form-item>
          <el-form-item label="筛选条件" prop="filters">
            <el-card shadow="never">
              <div slot="header">
                <span>科目筛选</span>
                <el-button style="float: right; padding: 3px 0" type="text" @click="showSubjectSelector">
                  选择科目
                </el-button>
              </div>
              <div class="selected-subjects">
                <el-tag
                  v-for="subject in selectedSubjects"
                  :key="subject.id"
                  closable
                  @close="removeSubject(subject)">
                  {{ subject.name }}
                </el-tag>
                <span v-if="selectedSubjects.length === 0" class="empty-tip">未选择科目，将包含所有科目</span>
              </div>
            </el-card>
          </el-form-item>
          <el-form-item label="自定义样式" prop="customStyle">
            <el-switch
              v-model="reportConfig.customStyle"
              active-text="使用自定义样式"
              inactive-text="使用默认样式">
            </el-switch>
          </el-form-item>
        </el-form>
      </div>

      <!-- 步骤3：生成报告 -->
      <div class="step-content" v-if="currentStep === 2">
        <div class="generating-section">
          <div class="generating-icon">
            <i class="el-icon-loading"></i>
          </div>
          <h3>正在生成报告...</h3>
          <div class="progress-info">
            <p>{{ generateProgressText }}</p>
            <el-progress :percentage="generateProgress" :status="generateStatus"></el-progress>
          </div>
        </div>
      </div>

      <!-- 步骤4：预览导出 -->
      <div class="step-content" v-if="currentStep === 3">
        <div class="preview-section">
          <h3>报告预览</h3>
          <div class="preview-actions">
            <el-button type="success" icon="el-icon-download" @click="downloadReport">
              下载报告
            </el-button>
            <el-button type="primary" icon="el-icon-view" @click="previewReport">
              在线预览
            </el-button>
            <el-button type="warning" icon="el-icon-share" @click="shareReport">
              分享报告
            </el-button>
            <el-button icon="el-icon-refresh" @click="regenerateReport">
              重新生成
            </el-button>
          </div>

          <!-- 报告内容预览 -->
          <div class="report-preview" v-if="reportData">
            <div class="report-header">
              <h2>{{ reportData.title }}</h2>
              <p class="report-subtitle">{{ reportData.subtitle }}</p>
              <div class="report-meta">
                <span>生成时间：{{ reportData.generateTime }}</span>
                <span>报告期间：{{ reportConfig.period }}</span>
                <span>数据期间：{{ reportData.dataRange }}</span>
              </div>
            </div>

            <!-- 执行摘要 -->
            <div class="report-section" v-if="reportConfig.contents.includes('summary')">
              <h4>一、执行摘要</h4>
              <div class="summary-content">
                <p>{{ reportData.summary }}</p>
                <el-row :gutter="24">
                  <el-col :span="8" v-for="(item, index) in reportData.keyMetrics" :key="index">
                    <div class="metric-card">
                      <div class="metric-value">{{ formatAmount(item.value) }}</div>
                      <div class="metric-label">{{ item.label }}</div>
                      <div class="metric-change" :class="item.change >= 0 ? 'positive' : 'negative'">
                        {{ item.change >= 0 ? '+' : '' }}{{ (item.change * 100).toFixed(2) }}%
                      </div>
                    </div>
                  </el-col>
                </el-row>
              </div>
            </div>

            <!-- 图表分析 -->
            <div class="report-section" v-if="reportConfig.contents.includes('charts')">
              <h4>二、图表分析</h4>
              <div class="charts-grid">
                <div class="chart-item" v-for="(chart, index) in reportData.charts" :key="index">
                  <h5>{{ chart.title }}</h5>
                  <div class="chart-placeholder">
                    <i class="el-icon-data-line"></i>
                    <p>{{ chart.description }}</p>
                  </div>
                </div>
              </div>
            </div>

            <!-- 趋势分析 -->
            <div class="report-section" v-if="reportConfig.contents.includes('trends')">
              <h4>三、趋势分析</h4>
              <el-table :data="reportData.trendData" border>
                <el-table-column prop="period" label="期间" width="120"></el-table-column>
                <el-table-column prop="totalAssets" label="总资产" align="right">
                  <template slot-scope="scope">
                    {{ formatAmount(scope.row.totalAssets) }}
                  </template>
                </el-table-column>
                <el-table-column prop="totalLiabilities" label="总负债" align="right">
                  <template slot-scope="scope">
                    {{ formatAmount(scope.row.totalLiabilities) }}
                  </template>
                </el-table-column>
                <el-table-column prop="netAssets" label="净资产" align="right">
                  <template slot-scope="scope">
                    {{ formatAmount(scope.row.netAssets) }}
                  </template>
                </el-table-column>
                <el-table-column prop="growthRate" label="增长率" align="right">
                  <template slot-scope="scope">
                    <span :class="scope.row.growthRate >= 0 ? 'positive' : 'negative'">
                      {{ scope.row.growthRate >= 0 ? '+' : '' }}{{ (scope.row.growthRate * 100).toFixed(2) }}%
                    </span>
                  </template>
                </el-table-column>
              </el-table>
            </div>

            <!-- 建议部分 -->
            <div class="report-section" v-if="reportConfig.contents.includes('suggestions')">
              <h4>四、改进建议</h4>
              <div class="suggestions-list">
                <div class="suggestion-item" v-for="(suggestion, index) in reportData.suggestions" :key="index">
                  <div class="suggestion-header">
                    <el-tag :type="suggestion.priority === 'high' ? 'danger' : suggestion.priority === 'medium' ? 'warning' : 'info'">
                      {{ suggestion.priority === 'high' ? '高优先级' : suggestion.priority === 'medium' ? '中优先级' : '低优先级' }}
                    </el-tag>
                    <h5>{{ suggestion.title }}</h5>
                  </div>
                  <p>{{ suggestion.content }}</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 步骤操作按钮 -->
    <div class="step-actions">
      <el-button @click="prevStep" :disabled="currentStep === 0">上一步</el-button>
      <el-button
        type="primary"
        @click="nextStep"
        :disabled="currentStep === 3 || (currentStep === 2 && generating)">
        {{ currentStep === 3 ? '完成' : '下一步' }}
      </el-button>
    </div>

    <!-- 科目选择对话框 -->
    <el-dialog
      title="选择科目"
      :visible.sync="subjectSelectorVisible"
      width="800px">
      <el-transfer
        v-model="selectedSubjectIds"
        :data="subjectOptions"
        :titles="['可选科目', '已选科目']"
        :props="{
          key: 'id',
          label: 'name'
        }"
        filterable
        :filter-method="filterMethod">
      </el-transfer>
      <span slot="footer">
        <el-button @click="subjectSelectorVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmSubjectSelection">确定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { generateAnalysisReport } from '@/api/financialSharing/generalLedger'

export default {
  name: 'ReportGeneration',
  data() {
    return {
      currentStep: 0,
      selectedReportType: '',
      generating: false,
      generateProgress: 0,
      generateStatus: '',
      generateProgressText: '准备生成...',
      subjectSelectorVisible: false,
      selectedSubjectIds: [],
      selectedSubjects: [],
      reportData: null,
      reportTypes: [
        {
          value: 'balance',
          name: '资产负债分析报告',
          description: '分析企业资产负债结构和变化趋势',
          icon: 'el-icon-s-finance',
          tags: ['月度', '季度', '年度']
        },
        {
          value: 'profit',
          name: '利润分析报告',
          description: '分析企业盈利能力和利润构成',
          icon: 'el-icon-coin',
          tags: ['月度', '季度', '年度']
        },
        {
          value: 'cashflow',
          name: '现金流量分析报告',
          description: '分析企业现金流入流出情况',
          icon: 'el-icon-water',
          tags: ['月度', '季度', '年度']
        },
        {
          value: 'trend',
          name: '趋势分析报告',
          description: '分析财务指标的历史趋势',
          icon: 'el-icon-data-line',
          tags: ['多期间对比']
        },
        {
          value: 'ratio',
          name: '财务比率分析报告',
          description: '分析各项财务比率指标',
          icon: 'el-icon-pie-chart',
          tags: ['综合分析']
        },
        {
          value: 'abnormal',
          name: '异常分析报告',
          description: '识别和分析财务异常数据',
          icon: 'el-icon-warning',
          tags: ['风险识别']
        }
      ],
      reportConfig: {
        period: '',
        comparePeriod: '',
        format: 'pdf',
        language: 'zh-CN',
        contents: ['summary', 'charts', 'details'],
        filters: {},
        customStyle: false
      },
      configRules: {
        period: [
          { required: true, message: '请选择报告期间', trigger: 'change' }
        ],
        format: [
          { required: true, message: '请选择报告格式', trigger: 'change' }
        ],
        language: [
          { required: true, message: '请选择报告语言', trigger: 'change' }
        ]
      },
      subjectOptions: []
    }
  },
  mounted() {
    this.initDefaultPeriod()
    this.loadSubjectOptions()
  },
  methods: {
    initDefaultPeriod() {
      const now = new Date()
      this.reportConfig.period = now.getFullYear() + '-' + String(now.getMonth() + 1).padStart(2, '0')
    },
    async loadSubjectOptions() {
      // 暂未对接 API，先以空状态展示，待后端接口提供后接入
      this.subjectOptions = []
    },
    selectReportType(type) {
      this.selectedReportType = type.value
    },
    nextStep() {
      if (this.currentStep === 0) {
        if (!this.selectedReportType) {
          this.$message.warning('请选择报告类型')
          return
        }
        this.currentStep = 1
      } else if (this.currentStep === 1) {
        this.$refs.configForm.validate((valid) => {
          if (valid) {
            this.currentStep = 2
            this.generateReport()
          }
        })
      } else if (this.currentStep === 2) {
        this.currentStep = 3
      } else if (this.currentStep === 3) {
        this.$message.success('报告生成完成')
        this.goBack()
      }
    },
    prevStep() {
      if (this.currentStep > 0) {
        this.currentStep--
      }
    },
    async generateReport() {
      this.generating = true
      this.generateProgress = 0
      this.generateStatus = ''

      try {
        const steps = [
          { progress: 20, text: '正在收集数据...' },
          { progress: 40, text: '正在分析数据...' },
          { progress: 60, text: '正在生成图表...' },
          { progress: 80, text: '正在生成报告内容...' },
          { progress: 100, text: '报告生成完成' }
        ]

        for (const step of steps) {
          await new Promise(resolve => setTimeout(resolve, 1000))
          this.generateProgress = step.progress
          this.generateProgressText = step.text
        }

        // 暂未对接 API，先以空状态展示，待后端接口提供后接入
        this.reportData = {
          title: '',
          subtitle: '',
          generateTime: '',
          dataRange: '',
          summary: '',
          keyMetrics: [],
          charts: [],
          trendData: [],
          suggestions: []
        }

        this.generateStatus = 'success'
      } catch (error) {
        this.generateStatus = 'exception'
        this.$message.error('报告生成失败：' + error.message)
      } finally {
        this.generating = false
      }
    },
    downloadReport() {
      const link = document.createElement('a')
      link.href = '#'
      link.download = `${this.getReportTypeName()}_${this.reportConfig.period}.${this.reportConfig.format}`
      link.click()
      this.$message.success('报告下载成功')
    },
    previewReport() {
      if (this.reportData) {
        const content = `<div style="max-height:400px;overflow:auto"><h3>${this.reportData.title || '报告预览'}</h3><p>${this.reportData.summary || '暂无摘要'}</p></div>`
        this.$alert(content, '在线预览', { dangerouslyUseHTMLString: true })
      } else {
        this.$message.warning('请先生成报告')
      }
    },
    shareReport() {
      const url = window.location.href
      navigator.clipboard.writeText(url).then(() => {
        this.$message.success('链接已复制到剪贴板')
      }).catch(() => {
        this.$message.info('链接: ' + url)
      })
    },
    regenerateReport() {
      this.currentStep = 2
      this.generateReport()
    },
    goBack() {
      this.$router.go(-1)
    },
    showSubjectSelector() {
      this.subjectSelectorVisible = true
    },
    confirmSubjectSelection() {
      this.selectedSubjects = this.subjectOptions.filter(item =>
        this.selectedSubjectIds.includes(item.id)
      )
      this.subjectSelectorVisible = false
    },
    removeSubject(subject) {
      const index = this.selectedSubjectIds.indexOf(subject.id)
      if (index > -1) {
        this.selectedSubjectIds.splice(index, 1)
      }
      const idx = this.selectedSubjects.findIndex(item => item.id === subject.id)
      if (idx > -1) {
        this.selectedSubjects.splice(idx, 1)
      }
    },
    filterMethod(query, item) {
      return item.name.includes(query)
    },
    getReportTypeName() {
      const type = this.reportTypes.find(t => t.value === this.selectedReportType)
      return type ? type.name : '财务分析报告'
    },
    getExportParams() {
      return {
        reportType: this.selectedReportType,
        config: this.reportConfig,
        filters: this.selectedSubjects.map(s => s.id)
      }
    },
    formatAmount(amount) {
      if (amount >= 10000) {
        return (amount / 10000).toFixed(2) + '万'
      }
      return amount.toLocaleString()
    }
  }
}
</script>

<style lang="scss" scoped>
.report-generation-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 84px);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: white;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

  .header-left {
    .page-title {
      font-size: 24px;
      font-weight: 600;
      color: #303133;
      margin: 0 0 8px 0;
      display: flex;
      align-items: center;

      i {
        margin-right: 12px;
        color: #409eff;
      }
    }

    .page-description {
      color: #606266;
      font-size: 14px;
      margin: 0;
    }
  }
}

.report-config {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
  margin-bottom: 24px;
}

.step-content {
  margin-top: 40px;
  min-height: 400px;

  h3 {
    margin: 0 0 24px 0;
    font-size: 18px;
    color: #303133;
  }
}

.report-type-card {
  background: #f8f9fa;
  border: 2px solid transparent;
  border-radius: 8px;
  padding: 20px;
  cursor: pointer;
  transition: all 0.3s ease;
  height: 100%;

  &:hover {
    background: #e9ecef;
  }

  &.active {
    background: #e3f2fd;
    border-color: #409eff;
  }

  .card-icon {
    font-size: 48px;
    color: #409eff;
    margin-bottom: 16px;
    text-align: center;

    i {
      font-size: 48px;
    }
  }

  .card-content {
    h4 {
      margin: 0 0 8px 0;
      font-size: 16px;
      color: #303133;
    }

    p {
      margin: 0 0 12px 0;
      color: #606266;
      font-size: 14px;
      line-height: 1.5;
    }

    .card-tags {
      margin-top: 12px;
    }
  }
}

.generating-section {
  text-align: center;
  padding: 60px 0;

  .generating-icon {
    font-size: 64px;
    color: #409eff;
    margin-bottom: 24px;

    i {
      animation: rotate 2s linear infinite;
    }
  }

  .progress-info {
    max-width: 400px;
    margin: 0 auto;

    p {
      margin-bottom: 16px;
      color: #606266;
    }
  }
}

.preview-section {
  .preview-actions {
    margin-bottom: 24px;
    text-align: center;

    .el-button {
      margin: 0 8px;
    }
  }

  .report-preview {
    border: 1px solid #ebeef5;
    border-radius: 8px;
    padding: 24px;
    background: white;

    .report-header {
      text-align: center;
      margin-bottom: 32px;
      padding-bottom: 20px;
      border-bottom: 2px solid #ebeef5;

      h2 {
        margin: 0 0 8px 0;
        font-size: 24px;
        color: #303133;
      }

      .report-subtitle {
        margin: 0 0 16px 0;
        color: #606266;
        font-size: 16px;
      }

      .report-meta {
        span {
          margin: 0 16px;
          color: #909399;
          font-size: 14px;
        }
      }
    }

    .report-section {
      margin-bottom: 32px;

      h4 {
        margin: 0 0 16px 0;
        font-size: 18px;
        color: #303133;
        font-weight: 600;
      }
    }

    .metric-card {
      background: #f8f9fa;
      border-radius: 8px;
      padding: 16px;
      text-align: center;

      .metric-value {
        font-size: 24px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 8px;
      }

      .metric-label {
        color: #606266;
        font-size: 14px;
        margin-bottom: 8px;
      }

      .metric-change {
        font-size: 14px;
        font-weight: 600;

        &.positive {
          color: #67c23a;
        }

        &.negative {
          color: #f56c6c;
        }
      }
    }

    .charts-grid {
      display: grid;
      grid-template-columns: repeat(2, 1fr);
      gap: 20px;

      .chart-item {
        background: #f8f9fa;
        border-radius: 8px;
        padding: 20px;

        h5 {
          margin: 0 0 16px 0;
          color: #303133;
        }

        .chart-placeholder {
          height: 200px;
          display: flex;
          flex-direction: column;
          align-items: center;
          justify-content: center;
          color: #909399;

          i {
            font-size: 48px;
            margin-bottom: 16px;
          }
        }
      }
    }

    .suggestions-list {
      .suggestion-item {
        background: #f8f9fa;
        border-radius: 8px;
        padding: 16px;
        margin-bottom: 12px;

        .suggestion-header {
          display: flex;
          align-items: center;
          margin-bottom: 8px;

          h5 {
            margin: 0 0 0 12px;
            color: #303133;
          }
        }

        p {
          margin: 0;
          color: #606266;
          line-height: 1.6;
        }
      }
    }
  }
}

.step-actions {
  text-align: center;
  margin-top: 32px;

  .el-button {
    margin: 0 8px;
  }
}

.selected-subjects {
  min-height: 40px;

  .el-tag {
    margin-right: 8px;
    margin-bottom: 8px;
  }

  .empty-tip {
    color: #909399;
    font-size: 14px;
  }
}

.positive {
  color: #67c23a;
}

.negative {
  color: #f56c6c;
}

@keyframes rotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}
</style>