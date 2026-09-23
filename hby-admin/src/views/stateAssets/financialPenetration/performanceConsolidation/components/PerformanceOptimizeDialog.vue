<template>
  <el-dialog
    title="绩效优化建议"
    :visible.sync="dialogVisible"
    width="65%"
    :before-close="handleClose"
  >
    <div class="optimize-content">
      <!-- 总体评估 -->
      <el-alert
        :title="overallAssessment.title"
        :type="overallAssessment.type"
        :description="overallAssessment.desc"
        show-icon
        :closable="false"
        class="assessment-alert"
      />

      <!-- 优化建议时间线 -->
      <h4 class="section-title">优化建议</h4>
      <el-timeline v-if="suggestions.length > 0">
        <el-timeline-item
          v-for="(item, index) in suggestions"
          :key="index"
          :type="item.priority === 'high' ? 'danger' : item.priority === 'medium' ? 'warning' : 'primary'"
          :timestamp="item.category"
          placement="top"
        >
          <el-card shadow="hover" class="suggestion-card">
            <div class="suggestion-title">
              <el-tag :type="item.priority === 'high' ? 'danger' : item.priority === 'medium' ? 'warning' : ''" size="mini">
                {{ item.priority === 'high' ? '高优先' : item.priority === 'medium' ? '中优先' : '建议' }}
              </el-tag>
              <span>{{ item.title }}</span>
            </div>
            <p class="suggestion-desc">{{ item.description }}</p>
            <p class="suggestion-action">建议措施：{{ item.action }}</p>
          </el-card>
        </el-timeline-item>
      </el-timeline>
      <el-empty v-else description="暂无优化建议，各项指标表现良好" />
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'PerformanceOptimizeDialog',
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
      if (!this.data.totalRevenue || !this.data.netProfit) return 0
      return (this.data.netProfit / this.data.totalRevenue) * 100
    },
    suggestions() {
      const list = []
      const d = this.data || {}

      if (this.profitMargin < 5) {
        list.push({
          category: '利润管理',
          title: '提升利润率',
          priority: 'high',
          description: `当前利润率为${this.profitMargin.toFixed(2)}%，低于5%的基准线，盈利能力偏弱。`,
          action: '优化成本结构，提升高毛利业务占比，加强费用管控。'
        })
      }

      if ((d.contribution || 0) < 20) {
        list.push({
          category: '贡献度管理',
          title: '提升贡献度',
          priority: 'high',
          description: `当前贡献率为${d.contribution || 0}%，低于20%的基准线，对集团整体贡献不足。`,
          action: '扩大业务规模，提升核心业务竞争力，增加市场份额。'
        })
      }

      if ((d.growthRate || 0) < 5) {
        list.push({
          category: '增长管理',
          title: '提升增长率',
          priority: 'medium',
          description: `当前增长率为${d.growthRate || 0}%，增长动力不足。`,
          action: '拓展新业务领域，加大研发投入，寻找新的利润增长点。'
        })
      }

      if ((d.totalAssets || 0) > 0 && (d.netProfit || 0) / (d.totalAssets || 1) * 100 < 3) {
        list.push({
          category: '资产管理',
          title: '提升资产回报率',
          priority: 'medium',
          description: '资产回报率偏低，资产利用效率有待提升。',
          action: '优化资产配置，处置低效资产，提升资产周转率。'
        })
      }

      if (this.profitMargin >= 5 && (d.contribution || 0) >= 20) {
        list.push({
          category: '持续优化',
          title: '保持竞争优势',
          priority: 'low',
          description: '当前各项核心指标表现良好。',
          action: '持续关注行业动态，保持创新投入，巩固市场地位。'
        })
      }

      return list
    },
    overallAssessment() {
      const highCount = this.suggestions.filter(s => s.priority === 'high').length
      const mediumCount = this.suggestions.filter(s => s.priority === 'medium').length
      if (highCount >= 2) {
        return { type: 'error', title: '绩效评估：需重点关注', desc: `存在${highCount}项高优先级问题，建议立即制定改进计划。` }
      }
      if (highCount === 1 || mediumCount >= 2) {
        return { type: 'warning', title: '绩效评估：有待改善', desc: '部分指标低于基准线，建议针对性优化。' }
      }
      return { type: 'success', title: '绩效评估：表现良好', desc: '各项指标均达到或超过基准线，继续保持。' }
    }
  },
  methods: {
    handleClose() { this.dialogVisible = false }
  }
}
</script>

<style scoped>
.dialog-footer { text-align: right; }
.optimize-content { padding: 0 10px; }
.assessment-alert { margin-bottom: 20px; }
.section-title {
  margin: 20px 0 16px;
  font-size: 14px;
  color: #303133;
  border-left: 3px solid #409EFF;
  padding-left: 8px;
}
.suggestion-card { margin-bottom: 4px; }
.suggestion-title {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: bold;
  margin-bottom: 8px;
}
.suggestion-desc {
  font-size: 13px;
  color: #606266;
  margin: 4px 0;
}
.suggestion-action {
  font-size: 13px;
  color: #409EFF;
  margin: 4px 0 0;
}
</style>