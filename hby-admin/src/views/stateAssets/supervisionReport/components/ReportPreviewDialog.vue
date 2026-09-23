<template>
  <el-dialog
    title="报告预览"
    :visible.sync="dialogVisible"
    width="90%"
    :close-on-click-modal="false"
    @close="handleClose"
    class="report-preview-dialog"
  >
    <div class="report-preview-container" v-loading="loading">
      <!-- 报告头部信息 -->
      <div class="report-header">
        <div class="report-title">
          <h1>{{ reportData.reportName || '监管报告' }}</h1>
          <div class="report-meta">
            <span class="meta-item">报告类型：{{ getReportTypeText(reportData.reportType) }}</span>
            <span class="meta-item">报告日期：{{ reportData.reportDate }}</span>
            <span class="meta-item">生成时间：{{ reportData.createTime }}</span>
          </div>
        </div>
        <div class="report-actions">
          <el-button type="primary" icon="el-icon-printer" @click="handlePrint">打印</el-button>
          <el-button type="success" icon="el-icon-download" @click="handleExport">导出PDF</el-button>
        </div>
      </div>

      <!-- 报告内容 -->
      <div class="report-content">
        <!-- 报告摘要 -->
        <div class="content-section" v-if="reportData.summary">
          <h2>报告摘要</h2>
          <div class="section-content">
            <p>{{ reportData.summary }}</p>
          </div>
        </div>

        <!-- 主要内容 -->
        <div class="content-section">
          <h2>报告内容</h2>
          <div class="section-content">
            <div v-html="formatContent(reportData.content)"></div>
          </div>
        </div>

        <!-- 数据统计 -->
        <div class="content-section" v-if="statisticsData.length > 0">
          <h2>数据统计</h2>
          <div class="section-content">
            <el-row :gutter="20">
              <el-col :span="8" v-for="(stat, index) in statisticsData" :key="index">
                <div class="stat-card">
                  <div class="stat-icon">
                    <i :class="stat.icon" :style="{ color: stat.color }"></i>
                  </div>
                  <div class="stat-info">
                    <div class="stat-value">{{ stat.value }}</div>
                    <div class="stat-label">{{ stat.label }}</div>
                  </div>
                </div>
              </el-col>
            </el-row>
          </div>
        </div>

        <!-- 图表展示 -->
        <div class="content-section" v-if="showCharts">
          <h2>数据分析</h2>
          <div class="section-content">
            <el-row :gutter="20">
              <el-col :span="12">
                <div class="chart-container">
                  <h4>趋势分析</h4>
                  <div ref="trendChart" style="height: 300px;"></div>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="chart-container">
                  <h4>分布情况</h4>
                  <div ref="distributionChart" style="height: 300px;"></div>
                </div>
              </el-col>
            </el-row>
          </div>
        </div>

        <!-- 关键发现 -->
        <div class="content-section" v-if="keyFindings.length > 0">
          <h2>关键发现</h2>
          <div class="section-content">
            <el-timeline>
              <el-timeline-item
                v-for="(finding, index) in keyFindings"
                :key="index"
                :type="getFindingType(finding.level)"
              >
                <div class="finding-item">
                  <h4>{{ finding.title }}</h4>
                  <p>{{ finding.description }}</p>
                  <div class="finding-meta">
                    <el-tag :type="getFindingLevelTag(finding.level)">
                      {{ finding.level }}
                    </el-tag>
                    <span class="finding-impact">影响程度: {{ finding.impact }}</span>
                  </div>
                </div>
              </el-timeline-item>
            </el-timeline>
          </div>
        </div>

        <!-- 建议措施 -->
        <div class="content-section" v-if="recommendations.length > 0">
          <h2>建议措施</h2>
          <div class="section-content">
            <div class="recommendations-list">
              <div v-for="(rec, index) in recommendations" :key="index" class="recommendation-item">
                <div class="rec-header">
                  <span class="rec-title">{{ rec.title }}</span>
                  <el-tag :type="getPriorityTag(rec.priority)">{{ rec.priority }}优先级</el-tag>
                </div>
                <div class="rec-content">{{ rec.content }}</div>
                <div class="rec-meta">
                  <span class="meta-item">预期效果: {{ rec.expectedEffect }}</span>
                  <span class="meta-item">实施周期: {{ rec.implementationPeriod }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 附件列表 -->
        <div class="content-section" v-if="attachments.length > 0">
          <h2>相关附件</h2>
          <div class="section-content">
            <el-table :data="attachments" stripe border style="width: 100%">
              <el-table-column prop="fileName" label="文件名" min-width="200"></el-table-column>
              <el-table-column prop="fileSize" label="文件大小" width="120" align="center"></el-table-column>
              <el-table-column prop="uploadTime" label="上传时间" width="160" align="center"></el-table-column>
              <el-table-column label="操作" width="120" align="center">
                <template slot-scope="scope">
                  <el-button size="mini" type="text" @click="handleDownload(scope.row)">下载</el-button>
                  <el-button size="mini" type="text" @click="handlePreviewFile(scope.row)">预览</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </div>
      </div>

      <!-- 报告尾部 -->
      <div class="report-footer">
        <div class="footer-info">
          <p>报告生成人：{{ reportData.creator }}</p>
          <p>审核人：{{ reportData.reviewer }}</p>
          <p>生成时间：{{ reportData.createTime }}</p>
        </div>
        <div class="footer-signature">
          <p>示例云国资监管系统</p>
          <p>{{ new Date().getFullYear() }}年{{ new Date().getMonth() + 1 }}月{{ new Date().getDate() }}日</p>
        </div>
      </div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="handleEdit">编辑报告</el-button>
    </div>
  </el-dialog>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'ReportPreviewDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    reportData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      loading: false,
      showCharts: true,
      statisticsData: [
        {
          label: '监管企业数',
          value: '156',
          icon: 'el-icon-office-building',
          color: '#409eff'
        },
        {
          label: '风险事件数',
          value: '23',
          icon: 'el-icon-warning',
          color: '#f56c6c'
        },
        {
          label: '合规率',
          value: '95.6%',
          icon: 'el-icon-success',
          color: '#67c23a'
        }
      ],
      keyFindings: [
        {
          title: '资产负债率偏高',
          description: '部分企业资产负债率超过70%，存在一定的财务风险',
          level: '中风险',
          impact: '中等'
        },
        {
          title: '现金流紧张',
          description: '约15%的企业现金流出现紧张情况，需要重点关注',
          level: '高风险',
          impact: '较高'
        }
      ],
      recommendations: [
        {
          title: '加强财务监控',
          content: '建立更加严格的财务监控机制，定期评估企业财务状况',
          priority: '高',
          expectedEffect: '降低财务风险20%',
          implementationPeriod: '3个月'
        },
        {
          title: '优化资金配置',
          content: '指导企业优化资金配置，提高资金使用效率',
          priority: '中',
          expectedEffect: '提升资金效率15%',
          implementationPeriod: '6个月'
        }
      ],
      attachments: [
        {
          fileName: '财务数据明细.xlsx',
          fileSize: '2.5MB',
          uploadTime: '2025-01-21 10:30:00'
        },
        {
          fileName: '风险评估报告.pdf',
          fileSize: '1.8MB',
          uploadTime: '2025-01-21 11:15:00'
        }
      ]
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
        this.$nextTick(() => {
          this.initCharts()
        })
      }
    }
  },
  methods: {
    handleClose() {
      this.dialogVisible = false
    },

    handleEdit() {
      this.$emit('edit', this.reportData)
      this.handleClose()
    },

    handlePrint() {
      window.print()
    },

    handleExport() {
      this.$message.success('正在导出PDF文件...')
    },

    handleDownload(file) {
      this.$message.success(`正在下载文件: ${file.fileName}`)
    },

    handlePreviewFile(file) {
      this.$message.success(`正在预览文件: ${file.fileName}`)
    },

    formatContent(content) {
      if (!content) return ''
      // 简单的格式化处理，将换行符转换为<br>
      return content.replace(/\n/g, '<br>')
    },

    getReportTypeText(type) {
      const typeMap = {
        'SUPERVISION': '监管报告',
        'RISK': '风险报告',
        'FINANCIAL': '财务报告',
        'COMPLIANCE': '合规报告'
      }
      return typeMap[type] || type
    },

    getFindingType(level) {
      const typeMap = {
        '高风险': 'danger',
        '中风险': 'warning',
        '低风险': 'success'
      }
      return typeMap[level] || 'primary'
    },

    getFindingLevelTag(level) {
      const tagMap = {
        '高风险': 'danger',
        '中风险': 'warning',
        '低风险': 'success'
      }
      return tagMap[level] || 'info'
    },

    getPriorityTag(priority) {
      const tagMap = {
        '高': 'danger',
        '中': 'warning',
        '低': 'success'
      }
      return tagMap[priority] || 'info'
    },

    initCharts() {
      if (!this.showCharts) return
      this.$nextTick(() => {
        this.initTrendChart()
        this.initDistributionChart()
      })
    },

    initTrendChart() {
      if (!this.$refs.trendChart) return
      const chart = echarts.init(this.$refs.trendChart)
      const option = {
        tooltip: { trigger: 'axis' },
        xAxis: {
          type: 'category',
          data: ['1月', '2月', '3月', '4月', '5月', '6月']
        },
        yAxis: { type: 'value' },
        series: [{
          name: '监管指标',
          type: 'line',
          data: [85, 88, 92, 89, 94, 96],
          smooth: true,
          itemStyle: { color: '#409eff' }
        }]
      }
      chart.setOption(option)
    },

    initDistributionChart() {
      if (!this.$refs.distributionChart) return
      const chart = echarts.init(this.$refs.distributionChart)
      const option = {
        tooltip: { trigger: 'item' },
        series: [{
          type: 'pie',
          radius: '70%',
          data: [
            { value: 45, name: '优秀' },
            { value: 35, name: '良好' },
            { value: 15, name: '一般' },
            { value: 5, name: '较差' }
          ]
        }]
      }
      chart.setOption(option)
    }
  }
}
</script>

<style scoped>
.report-preview-dialog {
  font-family: 'Microsoft YaHei', sans-serif;
}

.report-preview-container {
  max-height: 80vh;
  overflow-y: auto;
  padding: 20px;
  background-color: #fff;
}

.report-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 2px solid #409eff;
}

.report-title h1 {
  margin: 0 0 10px 0;
  color: #303133;
  font-size: 28px;
  font-weight: bold;
}

.report-meta {
  display: flex;
  gap: 20px;
  color: #606266;
  font-size: 14px;
}

.content-section {
  margin-bottom: 30px;
}

.content-section h2 {
  color: #303133;
  font-size: 20px;
  margin-bottom: 15px;
  padding-bottom: 8px;
  border-bottom: 1px solid #e4e7ed;
}

.section-content {
  padding: 15px 0;
}

.stat-card {
  display: flex;
  align-items: center;
  padding: 20px;
  border: 1px solid #e4e7ed;
  border-radius: 8px;
  margin-bottom: 15px;
}

.stat-icon {
  font-size: 32px;
  margin-right: 15px;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}

.stat-label {
  color: #909399;
  font-size: 14px;
}

.chart-container {
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  padding: 15px;
}

.chart-container h4 {
  margin: 0 0 15px 0;
  color: #303133;
}

.finding-item h4 {
  margin: 0 0 8px 0;
  color: #303133;
}

.finding-item p {
  margin: 0 0 10px 0;
  color: #606266;
}

.finding-meta {
  display: flex;
  align-items: center;
  gap: 15px;
}

.finding-impact {
  font-size: 12px;
  color: #909399;
}

.recommendation-item {
  border: 1px solid #e4e7ed;
  border-radius: 4px;
  padding: 15px;
  margin-bottom: 15px;
}

.rec-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.rec-title {
  font-weight: bold;
  color: #303133;
}

.rec-content {
  margin-bottom: 10px;
  color: #606266;
}

.rec-meta {
  display: flex;
  gap: 20px;
  font-size: 12px;
  color: #909399;
}

.report-footer {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  margin-top: 40px;
  padding-top: 20px;
  border-top: 1px solid #e4e7ed;
}

.footer-info p,
.footer-signature p {
  margin: 5px 0;
  color: #606266;
  font-size: 14px;
}

.footer-signature {
  text-align: right;
}

.dialog-footer {
  text-align: right;
}

@media print {
  .report-preview-dialog .el-dialog__header,
  .report-preview-dialog .el-dialog__footer {
    display: none;
  }
  
  .report-preview-container {
    max-height: none;
    overflow: visible;
  }
}
</style>
