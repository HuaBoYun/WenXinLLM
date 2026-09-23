<template>
  <el-dialog title="控制链分析报告" :visible.sync="dialogVisible" width="750px" :before-close="handleClose">
    <div v-loading="loading">
      <div v-if="report">
        <!-- 报告标题 -->
        <div style="text-align:center;margin-bottom:20px">
          <h3 style="margin:0;color:#303133">{{ report.reportTitle }}</h3>
          <div style="font-size:12px;color:#909399;margin-top:6px">生成时间：{{ report.generateTime }}</div>
        </div>

        <!-- 基本信息 -->
        <el-divider content-position="left">基本信息</el-divider>
        <el-descriptions :column="2" border size="small">
          <el-descriptions-item label="被控制企业">{{ report.enterpriseName }}</el-descriptions-item>
          <el-descriptions-item label="控制方">{{ report.controllerName }}</el-descriptions-item>
          <el-descriptions-item label="控制路径" :span="2">{{ report.chainPath }}</el-descriptions-item>
          <el-descriptions-item label="最后分析时间">{{ report.lastAnalysisTime }}</el-descriptions-item>
        </el-descriptions>

        <!-- 摘要 -->
        <el-divider content-position="left">分析摘要</el-divider>
        <el-card shadow="never" style="background:#f5f7fa;margin-bottom:16px">
          <div style="font-size:14px;line-height:1.8;color:#606266">{{ report.summary }}</div>
        </el-card>

        <!-- 风险分析 -->
        <el-divider content-position="left">风险分析</el-divider>
        <el-descriptions :column="3" border size="small" style="margin-bottom:12px">
          <el-descriptions-item label="风险等级">
            <el-tag :type="riskTagType" size="small">{{ report.riskAnalysis && report.riskAnalysis.riskLevelDesc }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="稳定性">
            <el-tag :type="stabilityTagType" size="small">{{ report.riskAnalysis && report.riskAnalysis.stabilityLevelDesc }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="环路检测">
            <el-tag :type="report.riskAnalysis && report.riskAnalysis.hasLoop ? 'danger' : 'success'" size="small">
              {{ report.riskAnalysis && report.riskAnalysis.hasLoop ? '存在环路' : '无环路' }}
            </el-tag>
          </el-descriptions-item>
        </el-descriptions>
        <div v-if="report.riskAnalysis && report.riskAnalysis.riskFactors">
          <el-alert
            v-for="(factor, i) in report.riskAnalysis.riskFactors"
            :key="i"
            :title="factor"
            :type="riskAlertType"
            show-icon
            :closable="false"
            style="margin-bottom:8px"
          />
        </div>

        <!-- 建议 -->
        <el-divider content-position="left">改进建议</el-divider>
        <el-timeline>
          <el-timeline-item
            v-for="(s, i) in (report.suggestions || [])"
            :key="i"
            :color="i === 0 ? '#f56c6c' : i === 1 ? '#e6a23c' : '#67c23a'"
            :timestamp="'建议' + (i + 1)"
            placement="top"
          >
            {{ s }}
          </el-timeline-item>
        </el-timeline>
      </div>
      <el-empty v-else-if="!loading" description="暂无报告数据" />
    </div>
    <div slot="footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" icon="el-icon-printer" @click="handlePrint">打印报告</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { generateControlChainReport } from '@/api/stateAssets/controlChain'

export default {
  name: 'ControlReportDialog',
  props: {
    visible: { type: Boolean, default: false },
    chainData: { type: Object, default: () => ({}) }
  },
  data() {
    return {
      loading: false,
      report: null
    }
  },
  computed: {
    dialogVisible: {
      get() { return this.visible },
      set(val) { this.$emit('update:visible', val) }
    },
    riskTagType() {
      if (!this.report || !this.report.riskAnalysis) return 'info'
      const map = { LOW: 'success', MEDIUM: 'warning', HIGH: 'danger', CRITICAL: 'danger' }
      return map[this.report.riskAnalysis.riskLevel] || 'info'
    },
    stabilityTagType() {
      if (!this.report || !this.report.riskAnalysis) return 'info'
      const map = { HIGH: 'success', MEDIUM: 'primary', LOW: 'warning', UNSTABLE: 'danger' }
      return map[this.report.riskAnalysis.stabilityLevel] || 'info'
    },
    riskAlertType() {
      if (!this.report || !this.report.riskAnalysis) return 'info'
      const map = { LOW: 'success', MEDIUM: 'warning', HIGH: 'error', CRITICAL: 'error' }
      return map[this.report.riskAnalysis.riskLevel] || 'warning'
    }
  },
  watch: {
    visible(val) {
      if (val && this.chainData && this.chainData.chainId) {
        this.fetchReport()
      }
      if (!val) {
        this.report = null
      }
    }
  },
  methods: {
    async fetchReport() {
      this.loading = true
      try {
        const res = await generateControlChainReport({ chainId: this.chainData.chainId })
        if (res && res.result === 200 && res.data) {
          this.report = res.data
        } else {
          this.$message.error((res && res.msg) || '生成报告失败')
        }
      } catch (e) {
        console.error('生成报告失败:', e)
        this.$message.error('生成报告请求失败')
      } finally {
        this.loading = false
      }
    },
    handleClose() {
      this.dialogVisible = false
    },
    handlePrint() {
      window.print()
    }
  }
}
</script>
