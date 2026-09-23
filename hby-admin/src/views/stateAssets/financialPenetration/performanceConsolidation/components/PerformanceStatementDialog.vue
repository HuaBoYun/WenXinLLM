<template>
  <el-dialog
    title="绩效报表"
    :visible.sync="dialogVisible"
    width="70%"
    :before-close="handleClose"
  >
    <div class="statement-content">
      <!-- 基本信息 -->
      <el-descriptions title="基本信息" :column="2" border class="section">
        <el-descriptions-item label="企业名称">{{ data.companyName || '--' }}</el-descriptions-item>
        <el-descriptions-item label="统计期间">{{ data.period || '--' }}</el-descriptions-item>
      </el-descriptions>

      <!-- 核心指标 -->
      <h4 class="section-title">核心指标</h4>
      <el-row :gutter="16" class="metrics-grid">
        <el-col :span="8">
          <div class="metric-card">
            <div class="metric-value">{{ formatNumber(data.totalRevenue) }}</div>
            <div class="metric-label">营业总收入(万元)</div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="metric-card">
            <div class="metric-value">{{ formatNumber(data.netProfit) }}</div>
            <div class="metric-label">净利润(万元)</div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="metric-card">
            <div class="metric-value">{{ formatNumber(data.totalAssets) }}</div>
            <div class="metric-label">总资产(万元)</div>
          </div>
        </el-col>
      </el-row>
      <el-row :gutter="16" class="metrics-grid">
        <el-col :span="8">
          <div class="metric-card">
            <div class="metric-value">{{ data.contribution || '--' }}%</div>
            <div class="metric-label">贡献率</div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="metric-card">
            <div class="metric-value">{{ data.growthRate || '--' }}%</div>
            <div class="metric-label">增长率</div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="metric-card">
            <div class="metric-value">{{ profitMargin }}%</div>
            <div class="metric-label">利润率</div>
          </div>
        </el-col>
      </el-row>

      <!-- 绩效评级 -->
      <h4 class="section-title">绩效评级</h4>
      <div class="rating-section">
        <el-tag :type="ratingInfo.type" size="medium" effect="dark">
          {{ ratingInfo.label }}
        </el-tag>
        <span class="rating-desc">{{ ratingInfo.desc }}</span>
      </div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'PerformanceStatementDialog',
  props: {
    visible: { type: Boolean, default: false },
    data: { type: Object, default: () => ({}) }
  },
  computed: {
    dialogVisible: {
      get() { return this.visible },
      set(val) { this.$emit('update:visible', val) }
    },
    profitMargin() {
      if (!this.data.totalRevenue || !this.data.netProfit) return '--'
      return ((this.data.netProfit / this.data.totalRevenue) * 100).toFixed(2)
    },
    ratingInfo() {
      const contribution = this.data.contribution || 0
      const growthRate = this.data.growthRate || 0
      const score = contribution * 0.6 + growthRate * 0.4
      if (score >= 30) return { label: '优秀', type: 'success', desc: '各项指标表现优异，继续保持' }
      if (score >= 20) return { label: '良好', type: '', desc: '整体表现良好，部分指标可进一步提升' }
      if (score >= 10) return { label: '一般', type: 'warning', desc: '部分指标需要关注和改善' }
      return { label: '待改进', type: 'danger', desc: '多项指标低于预期，需重点关注' }
    }
  },
  methods: {
    handleClose() { this.dialogVisible = false },
    formatNumber(val) {
      if (val === null || val === undefined) return '--'
      return Number(val).toLocaleString()
    }
  }
}
</script>

<style scoped>
.dialog-footer { text-align: right; }
.statement-content { padding: 0 10px; }
.section { margin-bottom: 20px; }
.section-title {
  margin: 20px 0 12px;
  font-size: 14px;
  color: #303133;
  border-left: 3px solid #409EFF;
  padding-left: 8px;
}
.metrics-grid { margin-bottom: 12px; }
.metric-card {
  background: #f5f7fa;
  border-radius: 4px;
  padding: 16px;
  text-align: center;
}
.metric-value {
  font-size: 20px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 4px;
}
.metric-label {
  font-size: 12px;
  color: #909399;
}
.rating-section {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 0;
}
.rating-desc {
  font-size: 13px;
  color: #606266;
}
</style>