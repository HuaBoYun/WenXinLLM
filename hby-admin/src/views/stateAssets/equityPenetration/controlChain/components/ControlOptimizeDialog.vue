<template>
  <el-dialog title="控制链优化建议" :visible.sync="dialogVisible" width="680px" :before-close="handleClose">
    <div v-loading="loading">
      <div v-if="chainData && chainData.enterpriseName">
        <el-alert :title="`${chainData.enterpriseName} — 控制链优化分析`" type="info" show-icon :closable="false" style="margin-bottom:16px" />

        <!-- 当前状态评估 -->
        <el-divider content-position="left">当前状态评估</el-divider>
        <el-descriptions :column="2" border size="small">
          <el-descriptions-item label="控制强度">
            <span :style="{ color: chainData.controlStrength < 50 ? '#f56c6c' : '#67c23a', fontWeight:'bold' }">{{ chainData.controlStrength }}%</span>
          </el-descriptions-item>
          <el-descriptions-item label="链路长度">{{ chainData.chainLength }}级</el-descriptions-item>
          <el-descriptions-item label="稳定性">{{ stabilityMap[chainData.stabilityLevel] }}</el-descriptions-item>
          <el-descriptions-item label="综合评分">
            <el-rate :value="ratingScore" disabled show-score :colors="['#f56c6c','#e6a23c','#67c23a']" />
          </el-descriptions-item>
        </el-descriptions>

        <!-- 优化建议 -->
        <el-divider content-position="left">优化建议</el-divider>
        <el-timeline v-if="mappedSuggestions.length">
          <el-timeline-item v-for="(s, i) in mappedSuggestions" :key="i" :color="s.color" :timestamp="s.priorityLabel" placement="top">
            <el-card shadow="never" style="padding:8px 12px">
              <div style="font-weight:600;margin-bottom:4px">{{ s.title }}</div>
              <div style="font-size:13px;color:#666">{{ s.desc }}</div>
              <div style="margin-top:6px">
                <el-tag size="mini" :type="s.difficultyType">难度：{{ s.difficultyLabel }}</el-tag>
                <el-tag size="mini" style="margin-left:6px">预期提升：+{{ s.improvement }}%</el-tag>
              </div>
            </el-card>
          </el-timeline-item>
        </el-timeline>
        <el-empty v-else-if="!loading" description="暂无优化建议" :image-size="80" />
      </div>
    </div>
    <div slot="footer"><el-button @click="handleClose">关闭</el-button></div>
  </el-dialog>
</template>
<script>
import { optimizeControlStructure } from '@/api/stateAssets/controlChain'

export default {
  name: 'ControlOptimizeDialog',
  props: {
    visible: { type: Boolean, default: false },
    chainData: { type: Object, default: () => ({}) }
  },
  data() {
    return {
      currentScore: 0,
      suggestions: [],
      loading: false
    }
  },
  computed: {
    dialogVisible: {
      get() { return this.visible },
      set(val) { this.$emit('update:visible', val) }
    },
    stabilityMap() {
      return { HIGH: '高稳定', MEDIUM: '中稳定', LOW: '低稳定', UNSTABLE: '不稳定' }
    },
    ratingScore() {
      const score = Math.min(this.currentScore / 20, 5)
      return Math.max(0, score)
    },
    mappedSuggestions() {
      return this.suggestions.map(item => ({
        title: item.title,
        desc: item.desc,
        color: this.getPriorityColor(item.priority),
        priorityLabel: this.getPriorityLabel(item.priority),
        difficultyLabel: this.getDifficultyLabel(item.difficulty),
        difficultyType: this.getDifficultyType(item.difficulty),
        improvement: item.improvement
      }))
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.fetchOptimizeSuggestions()
      } else {
        this.currentScore = 0
        this.suggestions = []
      }
    }
  },
  methods: {
    async fetchOptimizeSuggestions() {
      if (!this.chainData || !this.chainData.chainId) return
      this.loading = true
      try {
        const res = await optimizeControlStructure({ chainId: this.chainData.chainId })
        if (res && res.result === 200 && res.data) {
          this.currentScore = res.data.currentScore || 0
          this.suggestions = res.data.suggestions || []
        } else {
          this.$message.warning('获取优化建议失败')
        }
      } catch (e) {
        console.error('获取控制链优化建议失败', e)
        this.$message.error('获取优化建议失败，请稍后重试')
      } finally {
        this.loading = false
      }
    },
    getPriorityColor(priority) {
      const map = { HIGH: '#f56c6c', CRITICAL: '#f56c6c', MEDIUM: '#e6a23c', LOW: '#67c23a' }
      return map[priority] || '#909399'
    },
    getPriorityLabel(priority) {
      const map = { CRITICAL: '紧急', HIGH: '高优先', MEDIUM: '中优先', LOW: '常规' }
      return map[priority] || '常规'
    },
    getDifficultyLabel(difficulty) {
      const map = { HIGH: '高', MEDIUM: '中', LOW: '低' }
      return map[difficulty] || '中'
    },
    getDifficultyType(difficulty) {
      const map = { HIGH: 'danger', MEDIUM: 'warning', LOW: 'success' }
      return map[difficulty] || 'info'
    },
    handleClose() {
      this.dialogVisible = false
    }
  }
}
</script>