<template>
  <div class="domain-dashboard">
    <el-row :gutter="16" class="stat-row">
      <el-col :span="6" v-for="(item, index) in statCards" :key="index">
        <el-card shadow="hover" class="stat-card" :body-style="{ padding: '16px' }">
          <div class="stat-value" :style="{ color: item.color || '#409EFF' }">{{ item.value }}</div>
          <div class="stat-label">{{ item.label }}</div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="16" style="margin-top: 16px">
      <el-col :span="12">
        <el-card shadow="never">
          <div slot="header" class="card-header">
            <span>{{ chartTitle || '数据趋势' }}</span>
          </div>
          <div ref="chartContainer" class="chart-container">
            <slot name="chart"></slot>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="never">
          <div slot="header" class="card-header">
            <span>预警信息</span>
            <el-button size="mini" type="text" @click="$emit('view-all-warnings')">查看全部</el-button>
          </div>
          <div class="warning-list">
            <div v-if="warnings.length === 0" class="empty-tip">暂无预警信息</div>
            <div v-for="(w, i) in warnings" :key="i" class="warning-item" @click="$emit('warning-click', w)">
              <el-tag :type="{ HIGH: 'danger', MEDIUM: 'warning', LOW: 'info' }[w.level]" size="mini">
                {{ { HIGH: '高', MEDIUM: '中', LOW: '低' }[w.level] || w.level }}
              </el-tag>
              <span class="warning-text">{{ w.title }}</span>
              <span class="warning-time">{{ w.time }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <slot name="extra"></slot>
  </div>
</template>
<script>
/**
 * 领域驾驶舱模板组件
 * 提供统一的驾驶舱布局：统计卡片 + 图表区 + 预警列表
 * Props:
 *   domainType - 监管领域类型
 *   statCards  - 统计卡片数据 [{ label, value, color }]
 *   warnings   - 预警列表 [{ level, title, time }]
 *   chartTitle - 图表区标题
 */
export default {
  name: 'DomainDashboard',
  props: {
    domainType: { type: String, default: '' },
    statCards: { type: Array, default: () => [] },
    warnings: { type: Array, default: () => [] },
    chartTitle: { type: String, default: '数据趋势' },
  },
}
</script>
<style scoped>
.stat-row { margin-bottom: 0; }
.stat-card { text-align: center; }
.stat-value { font-size: 28px; font-weight: 700; line-height: 1.4; }
.stat-label { font-size: 13px; color: #909399; margin-top: 4px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
.chart-container { height: 300px; }
.warning-list { max-height: 300px; overflow-y: auto; }
.warning-item { display: flex; align-items: center; padding: 8px 0; border-bottom: 1px solid #f0f0f0; cursor: pointer; }
.warning-item:hover { background: #f5f7fa; }
.warning-text { flex: 1; margin: 0 8px; font-size: 13px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.warning-time { font-size: 12px; color: #c0c4cc; white-space: nowrap; }
.empty-tip { text-align: center; color: #c0c4cc; padding: 40px 0; font-size: 13px; }
</style>

