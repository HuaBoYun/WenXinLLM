<template>
  <el-dialog
    :title="dialogTitle"
    :visible.sync="dialogVisible"
    width="80%"
    :before-close="handleClose"
    class="report-detail-dialog">
    <div v-if="reportData" class="report-detail-container">
      <!-- 报表头部信息 -->
      <div class="report-header">
        <div class="header-info">
          <h2 class="report-title">{{ reportData.reportName }}</h2>
          <div class="report-meta">
            <el-tag :type="getReportTypeTag(reportData.reportType)">
              {{ getReportTypeName(reportData.reportType) }}
            </el-tag>
            <el-tag :type="getStatusTag(reportData.status)" style="margin-left: 8px">
              {{ getStatusName(reportData.status) }}
            </el-tag>
            <span class="meta-item">会计期间：{{ reportData.period }}</span>
            <span class="meta-item">创建时间：{{ reportData.createTime }}</span>
            <span class="meta-item">创建人：{{ reportData.creator }}</span>
          </div>
        </div>
        <div class="header-actions">
          <el-button type="primary" icon="el-icon-edit" @click="editReport">编辑</el-button>
          <el-button type="success" icon="el-icon-download" @click="exportReport">导出</el-button>
          <el-button type="warning" icon="el-icon-printer" @click="printReport">打印</el-button>
        </div>
      </div>

      <!-- 报表内容 -->
      <div class="report-content">
        <!-- 资产负债表 -->
        <div v-if="reportData.reportType === 'balance_sheet'" class="balance-sheet">
          <h3 class="section-title">资产负债表</h3>
          <div class="financial-table">
            <el-row :gutter="24">
              <el-col :span="12">
                <div class="assets-section">
                  <h4>资产</h4>
                  <el-table :data="balanceSheetData.assets" border size="small">
                    <el-table-column prop="item" label="项目" width="200"></el-table-column>
                    <el-table-column prop="currentAmount" label="期末余额" align="right"></el-table-column>
                    <el-table-column prop="previousAmount" label="期初余额" align="right"></el-table-column>
                  </el-table>
                </div>
              </el-col>
              <el-col :span="12">
                <div class="liabilities-section">
                  <h4>负债和所有者权益</h4>
                  <el-table :data="balanceSheetData.liabilities" border size="small">
                    <el-table-column prop="item" label="项目" width="200"></el-table-column>
                    <el-table-column prop="currentAmount" label="期末余额" align="right"></el-table-column>
                    <el-table-column prop="previousAmount" label="期初余额" align="right"></el-table-column>
                  </el-table>
                </div>
              </el-col>
            </el-row>
          </div>
        </div>

        <!-- 利润表 -->
        <div v-if="reportData.reportType === 'income_statement'" class="income-statement">
          <h3 class="section-title">利润表</h3>
          <div class="financial-table">
            <el-table :data="incomeStatementData" border size="small">
              <el-table-column prop="item" label="项目" width="300"></el-table-column>
              <el-table-column prop="currentAmount" label="本期金额" align="right"></el-table-column>
              <el-table-column prop="previousAmount" label="上期金额" align="right"></el-table-column>
            </el-table>
          </div>
        </div>

        <!-- 现金流量表 -->
        <div v-if="reportData.reportType === 'cash_flow_statement'" class="cash-flow-statement">
          <h3 class="section-title">现金流量表</h3>
          <div class="financial-table">
            <el-table :data="cashFlowStatementData" border size="small">
              <el-table-column prop="item" label="项目" width="300"></el-table-column>
              <el-table-column prop="currentAmount" label="本期金额" align="right"></el-table-column>
              <el-table-column prop="previousAmount" label="上期金额" align="right"></el-table-column>
            </el-table>
          </div>
        </div>

        <!-- 财务报表附注 -->
        <div v-if="reportData.reportType === 'financial_notes'" class="financial-notes">
          <h3 class="section-title">财务报表附注</h3>
          <div class="notes-content">
            <div v-for="(note, index) in financialNotes" :key="index" class="note-item">
              <h4>{{ note.title }}</h4>
              <div class="note-content" v-html="note.content"></div>
            </div>
          </div>
        </div>
      </div>

      <!-- 报表分析 -->
      <div v-if="showAnalysis" class="report-analysis">
        <h3 class="section-title">财务分析</h3>
        <el-row :gutter="24">
          <el-col :span="8">
            <div class="analysis-card">
              <h4>偿债能力分析</h4>
              <div class="analysis-item">
                <span>流动比率：</span>
                <span class="value">{{ analysisData.currentRatio }}</span>
              </div>
              <div class="analysis-item">
                <span>速动比率：</span>
                <span class="value">{{ analysisData.quickRatio }}</span>
              </div>
              <div class="analysis-item">
                <span>资产负债率：</span>
                <span class="value">{{ analysisData.debtRatio }}</span>
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="analysis-card">
              <h4>盈利能力分析</h4>
              <div class="analysis-item">
                <span>净利润率：</span>
                <span class="value">{{ analysisData.netProfitMargin }}</span>
              </div>
              <div class="analysis-item">
                <span>总资产收益率：</span>
                <span class="value">{{ analysisData.roa }}</span>
              </div>
              <div class="analysis-item">
                <span>净资产收益率：</span>
                <span class="value">{{ analysisData.roe }}</span>
              </div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="analysis-card">
              <h4>营运能力分析</h4>
              <div class="analysis-item">
                <span>总资产周转率：</span>
                <span class="value">{{ analysisData.assetTurnover }}</span>
              </div>
              <div class="analysis-item">
                <span>应收账款周转率：</span>
                <span class="value">{{ analysisData.receivableTurnover }}</span>
              </div>
              <div class="analysis-item">
                <span>存货周转率：</span>
                <span class="value">{{ analysisData.inventoryTurnover }}</span>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>
    
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="toggleAnalysis">
        {{ showAnalysis ? '隐藏分析' : '显示分析' }}
      </el-button>
    </div>
  </el-dialog>
</template>

<script>
import {
  getBalanceSheetDetail,
  getIncomeStatementDetail,
  getCashFlowStatementDetail,
  exportReport
} from '@/api/financialSharing/reports'

export default {
  name: 'ReportDetailDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    reportData: {
      type: Object,
      default: null
    }
  },
  data() {
    return {
      showAnalysis: false,
      balanceSheetData: {
        assets: [],
        liabilities: []
      },
      incomeStatementData: [],
      cashFlowStatementData: [],
      financialNotes: [],
      analysisData: {
        currentRatio: '2.35',
        quickRatio: '1.85',
        debtRatio: '35.6%',
        netProfitMargin: '18.3%',
        roa: '12.5%',
        roe: '19.5%',
        assetTurnover: '1.2',
        receivableTurnover: '6.8',
        inventoryTurnover: '4.5'
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
    },
    dialogTitle() {
      return this.reportData ? `${this.reportData.reportName} - 详情` : '报表详情'
    }
  },
  watch: {
    visible(newVal) {
      if (newVal && this.reportData) {
        this.loadReportDetail()
      }
    }
  },
  methods: {
    async loadReportDetail() {
      try {
        let response
        switch (this.reportData.reportType) {
          case 'balance_sheet':
            response = await getBalanceSheetDetail(this.reportData.reportId)
            this.balanceSheetData = response.data
            break
          case 'income_statement':
            response = await getIncomeStatementDetail(this.reportData.reportId)
            this.incomeStatementData = response.data
            break
          case 'cash_flow_statement':
            response = await getCashFlowStatementDetail(this.reportData.reportId)
            this.cashFlowStatementData = response.data
            break
        }
      } catch (error) {
        this.$message.error('加载报告详情失败')
      }
    },
    editReport() {
      this.$emit('edit', this.reportData)
      this.handleClose()
    },
    async exportReport() {
      try {
        const response = await exportReport({
          reportId: this.reportData.reportId,
          reportType: this.reportData.reportType,
          format: 'xlsx'
        })
        // 处理文件下载
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = `${this.reportData.reportName}.xlsx`
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    },
    printReport() {
      window.print()
    },
    toggleAnalysis() {
      this.showAnalysis = !this.showAnalysis
    },
    handleClose() {
      this.dialogVisible = false
    },
    getReportTypeTag(type) {
      const tags = {
        balance_sheet: 'primary',
        income_statement: 'success',
        cash_flow_statement: 'warning',
        financial_notes: 'info'
      }
      return tags[type] || 'default'
    },
    getReportTypeName(type) {
      const names = {
        balance_sheet: '资产负债表',
        income_statement: '利润表',
        cash_flow_statement: '现金流量表',
        financial_notes: '财务报表附注'
      }
      return names[type] || type
    },
    getStatusTag(status) {
      const tags = {
        draft: 'info',
        generated: 'primary',
        reviewed: 'success',
        published: 'warning'
      }
      return tags[status] || 'default'
    },
    getStatusName(status) {
      const names = {
        draft: '草稿',
        generated: '已生成',
        reviewed: '已审核',
        published: '已发布'
      }
      return names[status] || status
    }
  }
}
</script>

<style lang="scss" scoped>
.report-detail-dialog {
  .el-dialog__body {
    padding: 0;
  }
}

.report-detail-container {
  padding: 24px;
}

.report-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  padding-bottom: 24px;
  border-bottom: 1px solid #ebeef5;
  margin-bottom: 24px;

  .header-info {
    .report-title {
      font-size: 20px;
      font-weight: 600;
      color: #303133;
      margin: 0 0 12px 0;
    }

    .report-meta {
      display: flex;
      align-items: center;
      flex-wrap: wrap;
      gap: 16px;

      .meta-item {
        color: #606266;
        font-size: 14px;
      }
    }
  }

  .header-actions {
    .el-button {
      margin-left: 8px;
    }
  }
}

.report-content {
  .section-title {
    font-size: 16px;
    font-weight: 600;
    color: #303133;
    margin: 0 0 16px 0;
    padding-bottom: 8px;
    border-bottom: 2px solid #409eff;
  }

  .financial-table {
    margin-bottom: 32px;

    h4 {
      font-size: 14px;
      font-weight: 600;
      color: #303133;
      margin: 0 0 12px 0;
    }

    .el-table {
      border-radius: 4px;
      overflow: hidden;
    }
  }

  .notes-content {
    .note-item {
      margin-bottom: 24px;

      h4 {
        font-size: 14px;
        font-weight: 600;
        color: #303133;
        margin: 0 0 8px 0;
      }

      .note-content {
        color: #606266;
        line-height: 1.6;
        padding: 12px;
        background: #f8f9fa;
        border-radius: 4px;
      }
    }
  }
}

.report-analysis {
  margin-top: 32px;
  padding-top: 24px;
  border-top: 1px solid #ebeef5;

  .analysis-card {
    background: #f8f9fa;
    border-radius: 8px;
    padding: 16px;

    h4 {
      font-size: 14px;
      font-weight: 600;
      color: #303133;
      margin: 0 0 12px 0;
    }

    .analysis-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 8px;
      font-size: 14px;

      .value {
        font-weight: 600;
        color: #409eff;
      }
    }
  }
}

.dialog-footer {
  text-align: right;
  padding: 16px 24px;
  border-top: 1px solid #ebeef5;
}
</style>
