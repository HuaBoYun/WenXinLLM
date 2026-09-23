<template>
  <el-dialog
    title="股东穿透分析报告"
    :visible.sync="dialogVisible"
    width="860px"
    :before-close="handleClose"
    class="report-dialog"
  >
    <div v-loading="loading" class="report-wrap">
      <!-- 报告头部 -->
      <div class="report-header">
        <div class="report-title">
          <i class="el-icon-document" style="margin-right:8px;color:#1677FF;"></i>
          股东穿透分析报告
        </div>
        <div class="report-meta">
          <span>企业：<strong>{{ reportData.enterpriseName || '-' }}</strong></span>
          <span style="margin-left:24px;">生成时间：{{ formatTime(reportData.generateTime) }}</span>
        </div>
      </div>

      <!-- 统计概览 -->
      <el-row :gutter="16" class="stat-row" v-if="reportData.totalShareholders != null">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-num">{{ reportData.totalShareholders }}</div>
            <div class="stat-label">股东总数</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card danger">
            <div class="stat-num">{{ reportData.highRiskCount }}</div>
            <div class="stat-label">高风险股东</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card warning">
            <div class="stat-num">{{ reportData.relatedPartyCount }}</div>
            <div class="stat-label">关联方股东</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card info">
            <div class="stat-num">{{ reportData.concertedActionCount }}</div>
            <div class="stat-label">一致行动人</div>
          </div>
        </el-col>
      </el-row>

      <!-- 股东明细 -->
      <div class="section-title">股东明细</div>
      <el-table
        :data="reportData.shareholders || []"
        border
        stripe
        size="small"
        style="width:100%"
        empty-text="暂无股东数据"
      >
        <el-table-column prop="shareholderName" label="股东名称" min-width="140" show-overflow-tooltip />
        <el-table-column prop="shareholderType" label="类型" width="80" align="center">
          <template slot-scope="scope">{{ typeText(scope.row.shareholderType) }}</template>
        </el-table-column>
        <el-table-column prop="shareholdingRatio" label="持股比例" width="90" align="center">
          <template slot-scope="scope">
            {{ scope.row.shareholdingRatio != null ? scope.row.shareholdingRatio + '%' : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="penetrationLevel" label="穿透层级" width="80" align="center">
          <template slot-scope="scope">
            <el-tag size="mini" :type="scope.row.penetrationLevel > 5 ? 'danger' : 'primary'">
              {{ scope.row.penetrationLevel }}层
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="ultimateController" label="最终控制人" min-width="120" show-overflow-tooltip />
        <el-table-column prop="riskLevel" label="风险等级" width="90" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.riskLevel" :type="riskTag(scope.row.riskLevel)" size="mini">
              {{ riskText(scope.row.riskLevel) }}
            </el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="isRelatedParty" label="关联方" width="70" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.isRelatedParty === '1'" type="warning" size="mini">是</el-tag>
            <span v-else style="color:#999;">否</span>
          </template>
        </el-table-column>
        <el-table-column prop="isConcertedAction" label="一致行动" width="80" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.isConcertedAction === '1'" type="info" size="mini">是</el-tag>
            <span v-else style="color:#999;">否</span>
          </template>
        </el-table-column>
      </el-table>

      <!-- 无数据提示 -->
      <div v-if="!loading && !reportData.enterpriseName" class="empty-tip">
        <i class="el-icon-warning-outline" style="font-size:36px;color:#ccc;"></i>
        <p>暂无报告数据</p>
      </div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关 闭</el-button>
    </div>
  </el-dialog>
</template>


<script>
import { generateShareholderReport } from '@/api/stateAssets/shareholderAnalysis'

export default {
  name: 'ShareholderReportDialog',
  props: {
    visible: { type: Boolean, default: false },
    rowData: { type: Object, default: () => ({}) },
  },
  data() {
    return {
      loading: false,
      reportData: {},
    }
  },
  computed: {
    dialogVisible: {
      get() { return this.visible },
      set(val) { this.$emit('update:visible', val) },
    },
  },
  watch: {
    visible(val) {
      if (val && this.rowData && this.rowData.analysisId) {
        this.loadReport()
      } else if (!val) {
        this.reportData = {}
      }
    },
  },
  methods: {
    async loadReport() {
      this.loading = true
      this.reportData = {}
      try {
        const res = await generateShareholderReport({ analysisId: this.rowData.analysisId })
        if (res && res.result === 200) {
          this.reportData = res.data || {}
        } else {
          this.$message.error(res?.msg || '报告生成失败')
        }
      } catch (e) {
        console.error('生成报告失败:', e)
        this.$message.error('报告生成请求失败')
      } finally {
        this.loading = false
      }
    },
    handleClose() {
      this.dialogVisible = false
    },
    formatTime(val) {
      if (!val) return '-'
      if (typeof val === 'string') return val
      return new Date(val).toLocaleString('zh-CN')
    },
    typeText(type) {
      const map = { ENTERPRISE: '企业', INDIVIDUAL: '个人', GOVERNMENT: '政府', FUND: '基金' }
      return map[type] || type || '-'
    },
    riskText(level) {
      const map = { LOW: '低风险', MEDIUM: '中风险', HIGH: '高风险', CRITICAL: '极高风险' }
      return map[level] || level || '-'
    },
    riskTag(level) {
      const map = { LOW: 'success', MEDIUM: 'warning', HIGH: 'danger', CRITICAL: 'danger' }
      return map[level] || 'info'
    },
  },
}
</script>

<style scoped>
.report-wrap { min-height: 200px; }
.report-header { background: linear-gradient(135deg, #003A6C 0%, #0050A0 60%, #1677FF 100%); color: #fff; border-radius: 6px; padding: 16px 20px; margin-bottom: 20px; }
.report-title { font-size: 16px; font-weight: 600; margin-bottom: 8px; }
.report-meta { font-size: 13px; opacity: 0.9; }
.stat-row { margin-bottom: 20px; }
.stat-card { background: #f5f7fa; border-radius: 6px; padding: 16px; text-align: center; border: 1px solid #e4e7ed; }
.stat-card.danger { background: #fff0f0; border-color: #fbc4c4; }
.stat-card.warning { background: #fdf6ec; border-color: #f5dab1; }
.stat-card.info { background: #f0f9ff; border-color: #b3d8ff; }
.stat-num { font-size: 28px; font-weight: bold; color: #303133; line-height: 1; }
.stat-card.danger .stat-num { color: #F56C6C; }
.stat-card.warning .stat-num { color: #E6A23C; }
.stat-card.info .stat-num { color: #409EFF; }
.stat-label { font-size: 13px; color: #909399; margin-top: 6px; }
.section-title { font-size: 14px; font-weight: 600; color: #303133; margin-bottom: 12px; padding-left: 8px; border-left: 3px solid #1677FF; }
.empty-tip { text-align: center; padding: 40px; color: #999; }
.dialog-footer { text-align: right; }
</style>
