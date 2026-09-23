<template>
  <el-dialog
    title="报表详情"
    :visible.sync="dialogVisible"
    width="800px"
    @close="handleClose"
  >
    <el-descriptions :column="2" border>
      <el-descriptions-item label="企业名称" :span="2">
        {{ statementData.enterpriseName || '-' }}
      </el-descriptions-item>
      <el-descriptions-item label="报表类型">
        <el-tag :type="getStatementTypeTag(statementData.reportType)">
          {{ getStatementTypeText(statementData.reportType) }}
        </el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="报告期间">
        {{ getReportPeriodText(statementData.reportPeriod) }}
      </el-descriptions-item>
      <el-descriptions-item label="报告年度">
        {{ statementData.reportYear || '-' }}
      </el-descriptions-item>
      <el-descriptions-item label="状态">
        <el-tag :type="getStatusTag(statementData.status)">
          {{ getStatusText(statementData.status) }}
        </el-tag>
      </el-descriptions-item>
      <el-descriptions-item label="创建时间">
        {{ statementData.createTime || '-' }}
      </el-descriptions-item>
      <el-descriptions-item label="更新时间">
        {{ statementData.updateTime || '-' }}
      </el-descriptions-item>
    </el-descriptions>

    <el-divider content-position="left">财务数据（单位：万元）</el-divider>
    <el-table :data="financialItems" border size="medium" style="width: 100%;">
      <el-table-column prop="label" label="项目" width="180"></el-table-column>
      <el-table-column prop="value" label="金额（万元）" align="right">
        <template slot-scope="scope">
          <span :class="scope.row.valueClass">{{ scope.row.value }}</span>
        </template>
      </el-table-column>
    </el-table>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" icon="el-icon-download" @click="exportStatement">导出</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'StatementDetailDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    statementData: {
      type: Object,
      default: () => ({})
    }
  },
  computed: {
    dialogVisible: {
      get() { return this.visible },
      set(val) { this.$emit('update:visible', val) }
    },
    financialItems() {
      const d = this.statementData
      if (!d || !d.id) return []
      return [
        { label: '总资产', value: this.formatAmount(d.totalAssets), valueClass: '' },
        { label: '总负债', value: this.formatAmount(d.totalLiabilities), valueClass: '' },
        { label: '净资产', value: this.formatAmount(d.netAssets), valueClass: '' },
        { label: '营业收入', value: this.formatAmount(d.operatingRevenue), valueClass: '' },
        { label: '净利润', value: this.formatAmount(d.netProfit), valueClass: d.netProfit < 0 ? 'text-danger' : 'text-success' },
        { label: '现金流量', value: this.formatAmount(d.cashFlow), valueClass: d.cashFlow < 0 ? 'text-danger' : '' }
      ]
    }
  },
  methods: {
    formatAmount(amount) {
      if (!amount && amount !== 0) return '0.00'
      const num = typeof amount === 'string' ? parseFloat(amount) : amount
      if (isNaN(num)) return '0.00'
      return (num / 10000).toFixed(2)
    },
    getStatementTypeTag(type) {
      const map = { 'BALANCE_SHEET': 'primary', 'INCOME_STATEMENT': 'success', 'CASH_FLOW': 'warning', 'EQUITY_CHANGE': 'info' }
      return map[type] || 'info'
    },
    getStatementTypeText(type) {
      const map = { 'BALANCE_SHEET': '资产负债表', 'INCOME_STATEMENT': '利润表', 'CASH_FLOW': '现金流量表', 'EQUITY_CHANGE': '所有者权益变动表' }
      return map[type] || type || '-'
    },
    getReportPeriodText(period) {
      const map = { 'MONTHLY': '月报', 'QUARTERLY': '季报', 'SEMI_ANNUAL': '半年报', 'ANNUAL': '年报' }
      return map[period] || period || '-'
    },
    getStatusTag(status) {
      const map = { 'DRAFT': 'info', 'SUBMITTED': 'warning', 'AUDITED': 'success', 'PUBLISHED': 'primary' }
      return map[status] || 'info'
    },
    getStatusText(status) {
      const map = { 'DRAFT': '草稿', 'SUBMITTED': '已提交', 'AUDITED': '已审核', 'PUBLISHED': '已发布' }
      return map[status] || status || '-'
    },
    exportStatement() {
      const d = this.statementData
      if (!d || !d.id) {
        this.$message.warning('无数据可导出')
        return
      }
      const typeText = this.getStatementTypeText(d.reportType)
      const periodText = this.getReportPeriodText(d.reportPeriod)
      const headers = ['项目', '金额（万元）']
      const rows = this.financialItems.map(item => [item.label, item.value])
      const meta = [
        ['企业名称', d.enterpriseName || ''],
        ['报表类型', typeText],
        ['报告期间', periodText],
        ['报告年度', d.reportYear || ''],
        ['状态', this.getStatusText(d.status)],
        ['创建时间', d.createTime || ''],
        ['']
      ]
      const BOM = '\uFEFF'
      const csvLines = [
        ...meta.map(r => r.join(',')),
        headers.join(','),
        ...rows.map(r => r.map(v => '"' + String(v).replace(/"/g, '""') + '"').join(','))
      ]
      const blob = new Blob([BOM + csvLines.join('\n')], { type: 'text/csv;charset=utf-8;' })
      const fileName = typeText + '_' + (d.reportYear || '') + periodText + '.csv'
      const link = document.createElement('a')
      link.href = URL.createObjectURL(blob)
      link.download = fileName
      document.body.appendChild(link)
      link.click()
      document.body.removeChild(link)
      URL.revokeObjectURL(link.href)
      this.$message.success('导出成功')
    },
    handleClose() {
      this.dialogVisible = false
    }
  }
}
</script>

<style scoped>
.text-danger { color: #F56C6C; font-weight: bold; }
.text-success { color: #67C23A; font-weight: bold; }
</style>
