<template>
  <el-dialog
    title="盘点报告"
    :visible.sync="visible"
    width="1000px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <div v-loading="loading" class="report-container">
      <div class="report-header">
        <h2>{{ reportData.title }}</h2>
        <div class="report-meta">
          <span>报告编号：{{ reportData.reportNumber }}</span>
          <span>生成时间：{{ reportData.generateTime }}</span>
          <span>生成人：{{ reportData.generator }}</span>
        </div>
      </div>

      <div class="report-summary">
        <h3>一、盘点概况</h3>
        <el-descriptions :column="3" border size="small">
          <el-descriptions-item label="盘点任务">{{ reportData.taskName }}</el-descriptions-item>
          <el-descriptions-item label="盘点类型">{{ reportData.inventoryType }}</el-descriptions-item>
          <el-descriptions-item label="盘点期间">{{ reportData.period }}</el-descriptions-item>
          <el-descriptions-item label="负责人">{{ reportData.principal }}</el-descriptions-item>
          <el-descriptions-item label="参与人员">{{ reportData.participants }}</el-descriptions-item>
          <el-descriptions-item label="盘点范围">{{ reportData.scope }}</el-descriptions-item>
        </el-descriptions>
      </div>

      <div class="report-statistics">
        <h3>二、盘点统计</h3>
        <el-row :gutter="20">
          <el-col :span="6">
            <div class="stat-box">
              <div class="stat-label">应盘资产</div>
              <div class="stat-value">{{ reportData.totalAssets }}</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-box">
              <div class="stat-label">实盘资产</div>
              <div class="stat-value success">{{ reportData.completedAssets }}</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-box">
              <div class="stat-label">盘盈数量</div>
              <div class="stat-value surplus">{{ reportData.surplusCount }}</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="stat-box">
              <div class="stat-label">盘亏数量</div>
              <div class="stat-value shortage">{{ reportData.shortageCount }}</div>
            </div>
          </el-col>
        </el-row>
      </div>

      <div class="report-differences">
        <h3>三、差异明细</h3>
        <el-table :data="reportData.differences" border stripe max-height="300">
          <el-table-column type="index" label="序号" width="60" align="center" />
          <el-table-column prop="assetCode" label="资产编码" width="120" />
          <el-table-column prop="assetName" label="资产名称" min-width="150" show-overflow-tooltip />
          <el-table-column prop="bookQuantity" label="账面数量" width="100" align="center" />
          <el-table-column prop="actualQuantity" label="实盘数量" width="100" align="center" />
          <el-table-column label="差异数量" width="100" align="center">
            <template slot-scope="scope">
              <span :class="getDifferenceClass(scope.row)">
                {{ getDifference(scope.row) }}
              </span>
            </template>
          </el-table-column>
          <el-table-column label="差异类型" width="100" align="center">
            <template slot-scope="scope">
              <el-tag :type="getResultType(scope.row)" size="small">
                {{ getResultText(scope.row) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="location" label="存放地点" width="120" />
          <el-table-column prop="remark" label="备注" min-width="150" show-overflow-tooltip />
        </el-table>
      </div>

      <div class="report-analysis">
        <h3>四、差异分析</h3>
        <div class="analysis-content">
          <p><strong>主要差异原因：</strong></p>
          <ul>
            <li v-for="(reason, index) in reportData.analysisReasons" :key="index">{{ reason }}</li>
          </ul>
          <p><strong>改进建议：</strong></p>
          <ul>
            <li v-for="(suggestion, index) in reportData.suggestions" :key="index">{{ suggestion }}</li>
          </ul>
        </div>
      </div>

      <div class="report-conclusion">
        <h3>五、结论</h3>
        <p>{{ reportData.conclusion }}</p>
      </div>

      <div class="report-signatures">
        <el-row :gutter="40">
          <el-col :span="8">
            <div class="signature-box">
              <div class="signature-label">盘点负责人：</div>
              <div class="signature-value">{{ reportData.principal }}</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="signature-box">
              <div class="signature-label">财务审核：</div>
              <div class="signature-value">_____________</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="signature-box">
              <div class="signature-label">领导审批：</div>
              <div class="signature-value">_____________</div>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="handlePrint">打印</el-button>
      <el-button type="success" @click="handleExport">导出PDF</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'InventoryReportDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    reportId: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      loading: false,
      reportData: {
        title: '',
        reportNumber: '',
        generateTime: '',
        generator: '',
        taskName: '',
        inventoryType: '',
        period: '',
        principal: '',
        participants: '',
        scope: '',
        totalAssets: 0,
        completedAssets: 0,
        surplusCount: 0,
        shortageCount: 0,
        differences: [],
        analysisReasons: [],
        suggestions: [],
        conclusion: ''
      }
    }
  },
  watch: {
    visible(val) {
      if (val && this.reportId) {
        this.loadReportData()
      }
    }
  },
  methods: {
    async loadReportData() {
      this.loading = true
      try {
        // 暂未对接 API，先以空状态展示，待后端接口提供后接入
        this.reportData = {
          title: '',
          reportNumber: '',
          generateTime: '',
          generator: '',
          taskName: '',
          inventoryType: '',
          period: '',
          principal: '',
          participants: '',
          scope: '',
          totalAssets: 0,
          completedAssets: 0,
          surplusCount: 0,
          shortageCount: 0,
          differences: [],
          analysisReasons: [],
          suggestions: [],
          conclusion: ''
        }
      } catch (error) {
        this.$message.error('加载报告数据失败')
      } finally {
        this.loading = false
      }
    },
    getDifference(row) {
      const diff = (row.actualQuantity || 0) - (row.bookQuantity || 0)
      return diff > 0 ? `+${diff}` : diff
    },
    getDifferenceClass(row) {
      const diff = (row.actualQuantity || 0) - (row.bookQuantity || 0)
      if (diff > 0) return 'difference-surplus'
      if (diff < 0) return 'difference-shortage'
      return 'difference-normal'
    },
    getResultText(row) {
      const diff = (row.actualQuantity || 0) - (row.bookQuantity || 0)
      if (diff > 0) return '盘盈'
      if (diff < 0) return '盘亏'
      return '正常'
    },
    getResultType(row) {
      const diff = (row.actualQuantity || 0) - (row.bookQuantity || 0)
      if (diff > 0) return 'success'
      if (diff < 0) return 'danger'
      return 'info'
    },
    handlePrint() {
      window.print()
    },
    handleExport() {
      try {
        const data = this.reportData || {}
        const jsonStr = JSON.stringify(data, null, 2)
        const blob = new Blob([jsonStr], { type: 'application/json' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '盘点报告导出.json'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败')
      }
    },
    handleClose() {
      this.$emit('update:visible', false)
    }
  }
}
</script>

<style lang="scss" scoped>
.report-container {
  padding: 20px;
  background: white;
}

.report-header {
  text-align: center;
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 2px solid #409EFF;

  h2 {
    margin: 0 0 15px 0;
    color: #303133;
    font-size: 24px;
  }

  .report-meta {
    color: #606266;
    font-size: 14px;

    span {
      margin: 0 15px;
    }
  }
}

.report-summary,
.report-statistics,
.report-differences,
.report-analysis,
.report-conclusion {
  margin-bottom: 30px;

  h3 {
    margin: 0 0 15px 0;
    color: #303133;
    font-size: 16px;
    font-weight: 600;
  }
}

.stat-box {
  text-align: center;
  padding: 20px;
  background: #f5f7fa;
  border-radius: 4px;

  .stat-label {
    font-size: 14px;
    color: #909399;
    margin-bottom: 10px;
  }

  .stat-value {
    font-size: 28px;
    font-weight: 600;
    color: #303133;

    &.success {
      color: #67c23a;
    }

    &.surplus {
      color: #67c23a;
    }

    &.shortage {
      color: #f56c6c;
    }
  }
}

.difference-surplus {
  color: #67c23a;
  font-weight: 600;
}

.difference-shortage {
  color: #f56c6c;
  font-weight: 600;
}

.difference-normal {
  color: #909399;
}

.analysis-content {
  line-height: 1.8;
  color: #606266;

  p {
    margin: 10px 0;
  }

  ul {
    margin: 10px 0;
    padding-left: 20px;

    li {
      margin: 5px 0;
    }
  }
}

.report-conclusion {
  p {
    line-height: 1.8;
    color: #606266;
    text-indent: 2em;
  }
}

.report-signatures {
  margin-top: 40px;
  padding-top: 20px;
  border-top: 1px solid #dcdfe6;

  .signature-box {
    text-align: center;

    .signature-label {
      font-size: 14px;
      color: #606266;
      margin-bottom: 20px;
    }

    .signature-value {
      font-size: 16px;
      color: #303133;
      padding: 10px 0;
      border-bottom: 1px solid #303133;
    }
  }
}

@media print {
  .dialog-footer {
    display: none;
  }
}
</style>

