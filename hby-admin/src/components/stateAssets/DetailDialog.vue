<template>
  <el-drawer :visible.sync="innerVisible" :title="title" :size="size" :before-close="handleClose" append-to-body>
    <div class="detail-dialog-body">
      <!-- 顶部摘要区 -->
      <div v-if="summary" class="detail-summary">
        <div class="summary-title">{{ summary.title || '' }}</div>
        <div v-if="summary.status" class="summary-status">
          <el-tag :type="summary.statusType || 'info'" size="small">{{ summary.status }}</el-tag>
        </div>
        <div v-if="summary.description" class="summary-desc">{{ summary.description }}</div>
      </div>
      <!-- 详情字段区 -->
      <div class="detail-sections">
        <div v-for="(section, si) in sections" :key="si" class="detail-section">
          <div v-if="section.title" class="section-title">{{ section.title }}</div>
          <el-descriptions :column="section.column || 2" border size="medium" class="section-desc">
            <el-descriptions-item v-for="(field, fi) in section.fields" :key="fi" :label="field.label" :span="field.span || 1">
              <template v-if="field.type === 'tag'">
                <el-tag :type="field.tagType || 'info'" size="small">{{ getFieldValue(field) }}</el-tag>
              </template>
              <template v-else-if="field.type === 'progress'">
                <el-progress :percentage="Number(getFieldValue(field)) || 0" :color="field.color" :stroke-width="14" :text-inside="true" />
              </template>
              <template v-else-if="field.type === 'amount'">
                <span class="amount-value">{{ formatAmount(getFieldValue(field)) }}</span>
              </template>
              <template v-else>
                <span>{{ getFieldValue(field) || '-' }}</span>
              </template>
            </el-descriptions-item>
          </el-descriptions>
        </div>
      </div>
      <!-- 底部插槽 -->
      <slot name="footer" :data="data"></slot>
    </div>
  </el-drawer>
</template>

<script>
/**
 * 通用详情抽屉组件
 * Props:
 *   visible - 是否显示
 *   title   - 抽屉标题
 *   data    - 详情数据对象
 *   summary - 顶部摘要 { title, status, statusType, description }
 *   sections - 字段分区 [{ title, column, fields: [{ label, key, type, tagType, color, span }] }]
 *   size    - 抽屉宽度，默认 '50%'
 */
export default {
  name: 'DetailDialog',
  props: {
    visible: { type: Boolean, default: false },
    title: { type: String, default: '详情' },
    data: { type: Object, default: () => ({}) },
    summary: { type: Object, default: null },
    sections: { type: Array, default: () => [] },
    size: { type: String, default: '50%' },
  },
  computed: {
    innerVisible: {
      get() { return this.visible },
      set(val) { this.$emit('update:visible', val) },
    },
  },
  methods: {
    handleClose(done) { this.$emit('update:visible', false); done && done() },
    getFieldValue(field) {
      if (!this.data || !field.key) return ''
      const keys = field.key.split('.')
      let val = this.data
      for (const k of keys) { val = val?.[k]; if (val === undefined) return '' }
      if (field.formatter) return field.formatter(val, this.data)
      return val
    },
    formatAmount(val) {
      const num = Number(val)
      if (isNaN(num)) return val || '-'
      return num.toLocaleString('zh-CN', { minimumFractionDigits: 2, maximumFractionDigits: 2 })
    },
  },
}
</script>

<style scoped>
.detail-dialog-body { padding: 0 20px 20px; }
.detail-summary { background: linear-gradient(135deg, #f0f5ff 0%, #e8f4f8 100%); border-radius: 8px; padding: 16px 20px; margin-bottom: 20px; }
.summary-title { font-size: 18px; font-weight: 600; color: #303133; margin-bottom: 8px; }
.summary-status { margin-bottom: 6px; }
.summary-desc { font-size: 13px; color: #909399; line-height: 1.6; }
.detail-section { margin-bottom: 20px; }
.section-title { font-size: 15px; font-weight: 600; color: #303133; padding-bottom: 10px; border-bottom: 2px solid #409EFF; margin-bottom: 12px; }
.amount-value { font-weight: 600; color: #409EFF; }
</style>

