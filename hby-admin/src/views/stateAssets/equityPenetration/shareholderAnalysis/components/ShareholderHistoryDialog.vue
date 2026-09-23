<template>
  <el-dialog
    title="分析变更历史"
    :visible.sync="dialogVisible"
    width="650px"
    :before-close="handleClose"
  >
    <div v-if="shareholderData && shareholderData.analysisId">
      <el-descriptions :column="2" border size="small" style="margin-bottom: 20px;">
        <el-descriptions-item label="股东名称">{{ shareholderData.shareholderName }}</el-descriptions-item>
        <el-descriptions-item label="企业名称">{{ shareholderData.enterpriseName }}</el-descriptions-item>
        <el-descriptions-item label="当前状态">
          <el-tag :type="statusTag" size="small">{{ statusText }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="分析时间">{{ shareholderData.analysisTime || '-' }}</el-descriptions-item>
      </el-descriptions>

      <div v-loading="historyLoading">
        <h4 style="margin-bottom: 12px;">变更时间线</h4>
        <el-timeline v-if="historyList.length > 0">
          <el-timeline-item
            v-for="(item, index) in historyList"
            :key="index"
            :timestamp="formatTime(item.changeTime || item.updateTime)"
            placement="top"
            :type="getTimelineType(index)"
          >
            <el-card shadow="never" class="timeline-card">
              <p>{{ item.shareholderName || shareholderData.shareholderName }}</p>
              <p class="timeline-detail">持股比例: {{ item.shareholdingRatio != null ? item.shareholdingRatio + '%' : '-' }}</p>
              <p class="timeline-detail">风险等级: {{ riskLevelText(item.riskLevel) }}</p>
              <p class="timeline-detail">分析状态: {{ analysisStatusText(item.analysisStatus) }}</p>
            </el-card>
          </el-timeline-item>
        </el-timeline>
        <div v-else style="text-align:center;color:#999;padding:20px;">
          暂无变更记录
        </div>
      </div>
    </div>
    <div v-else style="text-align: center; padding: 40px; color: #999;">
      <i class="el-icon-warning-outline" style="font-size: 36px;"></i>
      <p>暂无数据</p>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关 闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getShareholderChangeHistory } from '@/api/stateAssets/shareholderAnalysis'

export default {
  name: 'ShareholderHistoryDialog',
  props: {
    visible: { type: Boolean, default: false },
    shareholderData: { type: Object, default: () => ({}) },
  },
  data() {
    return {
      historyLoading: false,
      historyList: [],
    }
  },
  computed: {
    dialogVisible: {
      get() { return this.visible },
      set(val) { this.$emit('update:visible', val) },
    },
    statusText() {
      const map = { PENDING: '待分析', ANALYZING: '分析中', COMPLETED: '已完成', FAILED: '分析失败' }
      return map[this.shareholderData.analysisStatus] || this.shareholderData.analysisStatus || '-'
    },
    statusTag() {
      const map = { PENDING: 'info', ANALYZING: 'warning', COMPLETED: 'success', FAILED: 'danger' }
      return map[this.shareholderData.analysisStatus] || 'info'
    },
  },
  watch: {
    visible(val) {
      if (val && this.shareholderData && this.shareholderData.enterpriseId) {
        this.loadHistory()
      }
    },
  },
  methods: {
    async loadHistory() {
      this.historyLoading = true
      this.historyList = []
      try {
        const res = await getShareholderChangeHistory({
          enterpriseId: this.shareholderData.enterpriseId,
        })
        if (res && res.result === 200) {
          this.historyList = res.data || []
        }
      } catch (e) {
        console.error('获取变更历史失败:', e)
      } finally {
        this.historyLoading = false
      }
    },
    handleClose() { this.dialogVisible = false },
    formatTime(val) {
      if (!val) return '-'
      if (typeof val === 'string') return val
      return new Date(val).toLocaleString('zh-CN')
    },
    getTimelineType(index) {
      const types = ['primary', 'success', 'warning', 'info']
      return types[index % types.length]
    },
    riskLevelText(level) {
      const map = { LOW: '低风险', MEDIUM: '中风险', HIGH: '高风险', CRITICAL: '极高风险' }
      return map[level] || level || '-'
    },
    analysisStatusText(status) {
      const map = { PENDING: '待分析', ANALYZING: '分析中', COMPLETED: '已完成', FAILED: '分析失败' }
      return map[status] || status || '-'
    },
  },
}
</script>

<style scoped>
.dialog-footer { text-align: right; }
.timeline-card { padding: 8px 12px; }
.timeline-card p { margin: 0; font-size: 14px; }
.timeline-detail { color: #909399; font-size: 12px; margin-top: 4px; }
</style>