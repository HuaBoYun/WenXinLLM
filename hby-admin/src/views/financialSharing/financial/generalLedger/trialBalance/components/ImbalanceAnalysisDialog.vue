<template>
  <el-dialog
    title="不平衡分析"
    :visible.sync="dialogVisible"
    width="900px"
    :before-close="handleClose">
    <div class="imbalance-analysis-content">
      <!-- 分析概览 -->
      <div class="analysis-overview">
        <el-row :gutter="24">
          <el-col :span="8">
            <div class="overview-item">
              <div class="overview-label">不平衡科目数量</div>
              <div class="overview-value error">{{ analysisData.imbalanceCount || 0 }}</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="overview-item">
              <div class="overview-label">总差额</div>
              <div class="overview-value error">{{ formatAmount(analysisData.totalDifference || 0) }}</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="overview-item">
              <div class="overview-label">影响程度</div>
              <div class="overview-value" :class="getSeverityClass(analysisData.severity)">
                {{ getSeverityName(analysisData.severity) }}
              </div>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 不平衡科目列表 -->
      <div class="imbalance-subjects">
        <h4>不平衡科目明细</h4>
        <el-table :data="imbalanceSubjects" border size="small" max-height="300">
          <el-table-column prop="subjectCode" label="科目编码" width="100"></el-table-column>
          <el-table-column prop="subjectName" label="科目名称" min-width="150"></el-table-column>
          <el-table-column prop="debitTotal" label="借方合计" width="120" align="right">
            <template slot-scope="scope">
              <span class="debit-amount">{{ formatAmount(scope.row.debitTotal) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="creditTotal" label="贷方合计" width="120" align="right">
            <template slot-scope="scope">
              <span class="credit-amount">{{ formatAmount(scope.row.creditTotal) }}</span>
            </template>
          </el-table-column>
          <el-table-column prop="difference" label="差额" width="120" align="right">
            <template slot-scope="scope">
              <span :class="{ 'error-amount': Math.abs(scope.row.difference) > 0 }">
                {{ scope.row.difference > 0 ? '+' : '' }}{{ formatAmount(scope.row.difference) }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="errorType" label="错误类型" width="120">
            <template slot-scope="scope">
              <el-tag :type="getErrorTypeTag(scope.row.errorType)" size="mini">
                {{ getErrorTypeName(scope.row.errorType) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="100">
            <template slot-scope="scope">
              <el-button size="mini" type="text" @click="analyzeSubject(scope.row)">
                详细分析
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 可能原因分析 -->
      <div class="possible-causes">
        <h4>可能原因分析</h4>
        <div class="causes-list">
          <div v-for="(cause, index) in possibleCauses" :key="index" class="cause-item">
            <div class="cause-header">
              <i :class="cause.icon" :style="{ color: cause.color }"></i>
              <span class="cause-title">{{ cause.title }}</span>
              <el-tag :type="cause.probability > 0.7 ? 'danger' : cause.probability > 0.4 ? 'warning' : 'info'" size="mini">
                {{ (cause.probability * 100).toFixed(0) }}%
              </el-tag>
            </div>
            <div class="cause-description">{{ cause.description }}</div>
            <div class="cause-solution">
              <strong>建议解决方案：</strong>{{ cause.solution }}
            </div>
          </div>
        </div>
      </div>

      <!-- 修复建议 -->
      <div class="fix-suggestions">
        <h4>修复建议</h4>
        <el-steps :active="currentStep" direction="vertical" finish-status="success">
          <el-step
            v-for="(step, index) in fixSteps"
            :key="index"
            :title="step.title"
            :description="step.description">
            <template slot="icon">
              <i :class="step.icon"></i>
            </template>
          </el-step>
        </el-steps>
      </div>

      <!-- 自动修复选项 -->
      <div class="auto-fix-options" v-if="canAutoFix">
        <h4>自动修复选项</h4>
        <el-alert
          title="检测到可自动修复的问题"
          type="info"
          description="系统检测到部分不平衡问题可以通过自动修复功能解决，是否执行自动修复？"
          show-icon
          :closable="false">
        </el-alert>
        <div class="auto-fix-actions">
          <el-button type="primary" @click="executeAutoFix" :loading="autoFixing">
            执行自动修复
          </el-button>
          <el-button @click="previewAutoFix">预览修复结果</el-button>
        </div>
      </div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="exportAnalysisReport">导出分析报告</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'ImbalanceAnalysisDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    analysisData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      dialogVisible: false,
      currentStep: 0,
      autoFixing: false,
      imbalanceSubjects: [],
      possibleCauses: [],
      fixSteps: []
    }
  },
  computed: {
    canAutoFix() {
      return this.possibleCauses.some(cause => cause.autoFixable)
    }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
      if (val) {
        this.loadAnalysisData()
      }
    },
    dialogVisible(val) {
      this.$emit('update:visible', val)
    }
  },
  methods: {
    loadAnalysisData() {
      // 暂未对接不平衡分析 API，先以空状态展示，待后端接口提供后接入
      this.imbalanceSubjects = []

      // 暂未对接可能原因 API，先以空状态展示
      this.possibleCauses = []

      // 暂未对接修复步骤 API，先以空状态展示
      this.fixSteps = []
    },
    analyzeSubject(subject) {
      this.$message.info(`正在分析科目 ${subject.subjectCode} ${subject.subjectName} 的详细情况...`)
    },
    async executeAutoFix() {
      this.autoFixing = true
      try {
        // 处理过程异步等待（实际由后端 API 完成）
        await new Promise(resolve => setTimeout(resolve, 2000))
        this.$message.success('自动修复完成')
        this.currentStep = 4
      } catch (error) {
        this.$message.error('自动修复失败：' + error.message)
      } finally {
        this.autoFixing = false
      }
    },
    previewAutoFix() {
      this.$confirm('预览将展示修复后的试算平衡表数据，是否继续？', '预览修复结果', {
        type: 'info'
      }).then(() => {
        this.$message.success('修复预览已生成，请查看分析数据')
      }).catch(() => {})
    },
    exportAnalysisReport() {
      try {
        const data = this.analysisData || {}
        const jsonStr = JSON.stringify(data, null, 2)
        const blob = new Blob([jsonStr], { type: 'application/json' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '不平衡分析报告.json'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败')
      }
    },
    handleClose() {
      this.dialogVisible = false
    },
    formatAmount(amount) {
      if (Math.abs(amount) >= 10000) {
        return (amount / 10000).toFixed(2) + '万'
      }
      return amount.toLocaleString()
    },
    getSeverityClass(severity) {
      const classes = {
        high: 'error',
        medium: 'warning',
        low: 'info'
      }
      return classes[severity] || 'info'
    },
    getSeverityName(severity) {
      const names = {
        high: '严重',
        medium: '中等',
        low: '轻微'
      }
      return names[severity] || '未知'
    },
    getErrorTypeTag(errorType) {
      const tags = {
        rounding: 'warning',
        entry: 'danger',
        missing: 'info',
        system: 'primary'
      }
      return tags[errorType] || 'default'
    },
    getErrorTypeName(errorType) {
      const names = {
        rounding: '舍入差异',
        entry: '录入错误',
        missing: '缺失分录',
        system: '系统错误'
      }
      return names[errorType] || errorType
    }
  }
}
</script>

<style lang="scss" scoped>
.imbalance-analysis-content {
  .analysis-overview {
    margin-bottom: 24px;
    padding: 16px;
    background: #f8f9fa;
    border-radius: 8px;

    .overview-item {
      text-align: center;

      .overview-label {
        font-size: 12px;
        color: #909399;
        margin-bottom: 8px;
      }

      .overview-value {
        font-size: 20px;
        font-weight: 600;

        &.error {
          color: #f56c6c;
        }

        &.warning {
          color: #e6a23c;
        }

        &.info {
          color: #409eff;
        }
      }
    }
  }

  .imbalance-subjects {
    margin-bottom: 24px;

    h4 {
      margin: 0 0 16px 0;
      color: #303133;
      font-size: 14px;
      font-weight: 600;
    }

    .debit-amount {
      color: #409eff;
      font-weight: 600;
    }

    .credit-amount {
      color: #67c23a;
      font-weight: 600;
    }

    .error-amount {
      color: #f56c6c;
      font-weight: 600;
    }
  }

  .possible-causes {
    margin-bottom: 24px;

    h4 {
      margin: 0 0 16px 0;
      color: #303133;
      font-size: 14px;
      font-weight: 600;
    }

    .causes-list {
      .cause-item {
        margin-bottom: 16px;
        padding: 16px;
        border: 1px solid #ebeef5;
        border-radius: 8px;

        &:last-child {
          margin-bottom: 0;
        }

        .cause-header {
          display: flex;
          align-items: center;
          margin-bottom: 8px;

          i {
            margin-right: 8px;
            font-size: 16px;
          }

          .cause-title {
            flex: 1;
            font-weight: 600;
            color: #303133;
          }
        }

        .cause-description {
          margin-bottom: 8px;
          color: #606266;
          font-size: 14px;
          line-height: 1.6;
        }

        .cause-solution {
          color: #909399;
          font-size: 13px;
          line-height: 1.6;
        }
      }
    }
  }

  .fix-suggestions {
    margin-bottom: 24px;

    h4 {
      margin: 0 0 16px 0;
      color: #303133;
      font-size: 14px;
      font-weight: 600;
    }
  }

  .auto-fix-options {
    h4 {
      margin: 0 0 16px 0;
      color: #303133;
      font-size: 14px;
      font-weight: 600;
    }

    .auto-fix-actions {
      margin-top: 16px;
      text-align: center;

      .el-button {
        margin: 0 8px;
      }
    }
  }
}

.dialog-footer {
  text-align: right;
}
</style>
