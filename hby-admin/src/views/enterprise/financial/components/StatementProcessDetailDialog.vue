<template>
  <el-dialog
    title="财务报表编制流程详情"
    :visible.sync="dialogVisible"
    width="80%"
    :before-close="handleClose"
  >
    <div v-if="processData && processData.processId">
      <el-descriptions :column="2" border>
        <el-descriptions-item label="报表名称" :span="2">{{ processData.statementName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="报表类型">
          <el-tag :type="getTypeTag(processData.statementType)" size="small">{{ getTypeText(processData.statementType) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="报告期间">{{ processData.reportingPeriod || '-' }}</el-descriptions-item>
        <el-descriptions-item label="编制状态">
          <el-tag :type="getStatusTag(processData.compilationStatus)" size="small">{{ getStatusText(processData.compilationStatus) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="审核状态">
          <el-tag :type="getAuditTag(processData.auditStatus)" size="small">{{ getAuditText(processData.auditStatus) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="编制负责人">{{ processData.compilationManager || '-' }}</el-descriptions-item>
        <el-descriptions-item label="编制部门">{{ processData.compilationDepartment || '-' }}</el-descriptions-item>
        <el-descriptions-item label="计划开始时间">{{ formatTime(processData.plannedStartTime) }}</el-descriptions-item>
        <el-descriptions-item label="计划结束时间">{{ formatTime(processData.plannedEndTime) }}</el-descriptions-item>
        <el-descriptions-item label="实际开始时间">{{ formatTime(processData.actualStartTime) }}</el-descriptions-item>
        <el-descriptions-item label="实际结束时间">{{ formatTime(processData.actualEndTime) }}</el-descriptions-item>
        <el-descriptions-item label="数据来源">{{ processData.dataSources || '-' }}</el-descriptions-item>
        <el-descriptions-item label="参与人员">{{ processData.participants || '-' }}</el-descriptions-item>
      </el-descriptions>

      <el-divider content-position="left">编制进度</el-divider>
      <el-progress
        :percentage="Number(processData.compilationProgress) || 0"
        :stroke-width="18"
        :text-inside="true"
        :color="getProgressColor(processData.compilationProgress)"
        style="margin-bottom: 20px;"
      />

      <el-divider content-position="left">质量评分</el-divider>
      <el-row :gutter="20">
        <el-col :span="5">
          <div class="score-item">
            <div class="score-value">{{ processData.accuracyScore || 0 }}</div>
            <div class="score-label">准确性</div>
          </div>
        </el-col>
        <el-col :span="5">
          <div class="score-item">
            <div class="score-value">{{ processData.completenessScore || 0 }}</div>
            <div class="score-label">完整性</div>
          </div>
        </el-col>
        <el-col :span="5">
          <div class="score-item">
            <div class="score-value">{{ processData.timelinessScore || 0 }}</div>
            <div class="score-label">及时性</div>
          </div>
        </el-col>
        <el-col :span="5">
          <div class="score-item">
            <div class="score-value">{{ processData.complianceScore || 0 }}</div>
            <div class="score-label">合规性</div>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="score-item total">
            <div class="score-value">{{ processData.overallQualityScore || 0 }}</div>
            <div class="score-label">综合评分</div>
          </div>
        </el-col>
      </el-row>

      <el-divider content-position="left">审核信息</el-divider>
      <el-descriptions :column="2" border size="small">
        <el-descriptions-item label="初审人">{{ processData.firstAuditor || '-' }}</el-descriptions-item>
        <el-descriptions-item label="初审意见">{{ processData.firstAuditOpinion || '-' }}</el-descriptions-item>
        <el-descriptions-item label="复审人">{{ processData.secondAuditor || '-' }}</el-descriptions-item>
        <el-descriptions-item label="复审意见">{{ processData.secondAuditOpinion || '-' }}</el-descriptions-item>
        <el-descriptions-item label="终审人">{{ processData.finalAuditor || '-' }}</el-descriptions-item>
        <el-descriptions-item label="终审意见">{{ processData.finalAuditOpinion || '-' }}</el-descriptions-item>
      </el-descriptions>

      <el-divider content-position="left">备注</el-divider>
      <p style="color: #606266;">{{ processData.remarks || '暂无备注' }}</p>
    </div>
    <div v-else style="text-align: center; padding: 40px; color: #909399;">
      暂无数据
    </div>

    <span slot="footer">
      <el-button @click="handleClose">关 闭</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  name: 'StatementProcessDetailDialog',
  props: {
    visible: { type: Boolean, default: false },
    processData: { type: Object, default: () => ({}) }
  },
  computed: {
    dialogVisible: {
      get() { return this.visible },
      set(val) { this.$emit('update:visible', val) }
    }
  },
  methods: {
    handleClose() { this.dialogVisible = false },
    formatTime(t) {
      if (!t) return '-'
      if (typeof t === 'string') return t.replace('T', ' ').substring(0, 19)
      return t
    },
    getProgressColor(p) {
      if (p < 30) return '#f56c6c'
      if (p < 70) return '#e6a23c'
      return '#67c23a'
    },
    getTypeTag(type) {
      const m = { 'BALANCE_SHEET': 'primary', 'INCOME_STATEMENT': 'success', 'CASH_FLOW': 'warning', 'EQUITY_CHANGE': 'info', 'balance_sheet': 'primary', 'income_statement': 'success', 'cash_flow': 'warning', 'equity_change': 'info' }
      return m[type] || 'info'
    },
    getTypeText(type) {
      const m = { 'BALANCE_SHEET': '资产负债表', 'INCOME_STATEMENT': '利润表', 'CASH_FLOW': '现金流量表', 'EQUITY_CHANGE': '所有者权益变动表', 'balance_sheet': '资产负债表', 'income_statement': '利润表', 'cash_flow': '现金流量表', 'equity_change': '所有者权益变动表' }
      return m[type] || type || '-'
    },
    getStatusTag(s) {
      const m = { 'completed': 'success', 'COMPLETED': 'success', 'in_progress': 'warning', 'IN_PROGRESS': 'warning', 'not_started': 'info', 'NOT_STARTED': 'info', 'paused': 'danger', 'PAUSED': 'danger', '已完成': 'success', '进行中': 'warning', '待开始': 'info', '已暂停': 'danger' }
      return m[s] || 'info'
    },
    getStatusText(s) {
      const m = { 'completed': '已完成', 'COMPLETED': '已完成', 'in_progress': '进行中', 'IN_PROGRESS': '进行中', 'not_started': '待开始', 'NOT_STARTED': '待开始', 'paused': '已暂停', 'PAUSED': '已暂停', 'cancelled': '已取消', 'CANCELLED': '已取消' }
      return m[s] || s || '-'
    },
    getAuditTag(s) {
      const m = { 'approved': 'success', 'pending': 'warning', 'pending_audit': 'warning', 'rejected': 'danger', 'NOT_SUBMITTED': 'info', 'not_submitted': 'info', '未提交': 'info', '待审核': 'warning', '终审通过': 'success' }
      return m[s] || 'info'
    },
    getAuditText(s) {
      const m = { 'approved': '已通过', 'pending': '待审核', 'pending_audit': '待审核', 'rejected': '已驳回', 'NOT_SUBMITTED': '未提交', 'not_submitted': '未提交', 'FIRST_AUDIT_PASSED': '初审通过', 'FINAL_AUDIT_PASSED': '终审通过' }
      return m[s] || s || '-'
    }
  }
}
</script>

<style scoped>
.score-item { text-align: center; padding: 15px 0; }
.score-value { font-size: 24px; font-weight: bold; color: #409EFF; }
.score-item.total .score-value { color: #67C23A; }
.score-label { font-size: 12px; color: #909399; margin-top: 5px; }
</style>
