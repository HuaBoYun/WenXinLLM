<template>
  <el-dialog title="实际控制人识别报告" :visible.sync="dialogVisible" width="680px" :before-close="handleClose">
    <div v-loading="loading">
      <div v-if="reportData && reportData.reportTitle" class="report-container">
        <!-- 报告标题 -->
        <div class="report-title">{{ reportData.reportTitle }}</div>
        <div class="report-time">生成时间：{{ formatDateTime(reportData.generateTime) }}</div>

        <!-- 基本信息 -->
        <el-divider content-position="left">基本信息</el-divider>
        <el-descriptions :column="2" border size="small">
          <el-descriptions-item label="被控制企业">{{ reportData.controlledName }}</el-descriptions-item>
          <el-descriptions-item label="实际控制人">{{ reportData.controllerName }}</el-descriptions-item>
          <el-descriptions-item label="控制方式">{{ getControlMethodText(reportData.controlMethod) }}</el-descriptions-item>
          <el-descriptions-item label="控制层级">{{ reportData.controlLevel }}层</el-descriptions-item>
          <el-descriptions-item label="控制路径" :span="2">{{ reportData.controlPath || '—' }}</el-descriptions-item>
        </el-descriptions>

        <!-- 持股比例 -->
        <el-divider content-position="left">持股比例</el-divider>
        <el-descriptions :column="3" border size="small">
          <el-descriptions-item label="直接持股">
            <span :style="{ color: getRatioColor(reportData.directRatio) }">{{ reportData.directRatio }}%</span>
          </el-descriptions-item>
          <el-descriptions-item label="间接持股">
            <span :style="{ color: getRatioColor(reportData.indirectRatio) }">{{ reportData.indirectRatio }}%</span>
          </el-descriptions-item>
          <el-descriptions-item label="综合持股">
            <span :style="{ color: getRatioColor(reportData.totalRatio), fontWeight: 'bold' }">{{ reportData.totalRatio }}%</span>
          </el-descriptions-item>
        </el-descriptions>

        <!-- 风险评估 -->
        <el-divider content-position="left">风险评估</el-divider>
        <el-descriptions :column="2" border size="small">
          <el-descriptions-item label="风险等级">
            <el-tag :type="getRiskTag(reportData.riskLevel)">{{ getRiskText(reportData.riskLevel) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="控制稳定性">
            <el-tag :type="getStabilityTag(reportData.stability)">{{ getStabilityText(reportData.stability) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="确认状态">
            <el-tag :type="getStatusTag(reportData.confirmationStatus)">{{ getStatusText(reportData.confirmationStatus) }}</el-tag>
          </el-descriptions-item>
        </el-descriptions>
      </div>
      <el-empty v-else-if="!loading" description="暂无报告数据，请先执行识别操作"></el-empty>
    </div>
    <div slot="footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" :loading="loading" @click="fetchReport">重新生成</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { generateBeneficialOwnerReport } from '@/api/stateAssets/beneficialOwner'

export default {
  name: 'ReportDialog',
  props: {
    visible: { type: Boolean, default: false },
    ownerData: { type: Object, default: () => ({}) }
  },
  data() {
    return { loading: false, reportData: null }
  },
  computed: {
    dialogVisible: {
      get() { return this.visible },
      set(val) { this.$emit('update:visible', val) }
    }
  },
  watch: {
    visible(val) {
      if (val && this.ownerData.controllerId) {
        this.fetchReport()
      } else if (!val) {
        this.reportData = null
      }
    }
  },
  methods: {
    async fetchReport() {
      this.loading = true
      try {
        const res = await generateBeneficialOwnerReport({ controllerId: this.ownerData.controllerId })
        if (res.result === 200) {
          this.reportData = res.data
        } else {
          this.$message.error(res.msg || '报告生成失败')
        }
      } catch (e) {
        this.$message.error('请求失败')
      } finally {
        this.loading = false
      }
    },
    handleClose() { this.dialogVisible = false },
    formatDateTime(str) {
      if (!str) return '—'
      return str.replace('T', ' ').substring(0, 19)
    },
    getControlMethodText(m) {
      const map = { SHAREHOLDING: '股权控制', VOTING_RIGHT: '表决权控制', AGREEMENT: '协议控制', MIXED: '混合控制', MANAGEMENT: '管理控制' }
      return map[m] || m || '—'
    },
    getRatioColor(r) {
      if (!r) return '#606266'
      if (r >= 80) return '#F56C6C'
      if (r >= 50) return '#E6A23C'
      return '#67C23A'
    },
    getRiskTag(l) { return { HIGH: 'danger', MEDIUM: 'warning', LOW: 'success', CRITICAL: 'danger' }[l] || 'info' },
    getRiskText(l) { return { HIGH: '高风险', MEDIUM: '中风险', LOW: '低风险', CRITICAL: '严重风险' }[l] || l || '—' },
    getStabilityTag(s) { return { STABLE: 'success', UNSTABLE: 'warning', VOLATILE: 'danger', UNCERTAIN: 'info' }[s] || 'info' },
    getStabilityText(s) { return { STABLE: '稳定', UNSTABLE: '不稳定', VOLATILE: '易变', UNCERTAIN: '不确定' }[s] || s || '—' },
    getStatusTag(s) { return { CONFIRMED: 'success', PENDING: 'info', REJECTED: 'danger', DISPUTED: 'warning' }[s] || 'info' },
    getStatusText(s) { return { CONFIRMED: '已确认', PENDING: '待确认', REJECTED: '已拒绝', DISPUTED: '有争议' }[s] || s || '—' },
  }
}
</script>

<style scoped>
.report-container { padding: 0 4px; }
.report-title { font-size: 16px; font-weight: bold; color: #303133; text-align: center; margin-bottom: 6px; }
.report-time { font-size: 12px; color: #909399; text-align: center; margin-bottom: 16px; }
</style>
