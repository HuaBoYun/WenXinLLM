<template>
  <el-dialog
    title="股权结构优化建议"
    :visible.sync="dialogVisible"
    width="650px"
    :before-close="handleClose"
  >
    <div class="optimize-content" v-loading="loading">
      <el-descriptions title="当前股权信息" :column="2" border style="margin-bottom:20px;">
        <el-descriptions-item label="投资方">{{ structureData.investorName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="持股比例">{{ structureData.shareholdingRatio || 0 }}%</el-descriptions-item>
        <el-descriptions-item label="控制类型">{{ controlMap[structureData.controlType] || '-' }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="statusTagType(structureData.status)" size="small">{{ statusMap[structureData.status] || '-' }}</el-tag>
        </el-descriptions-item>
      </el-descriptions>

      <el-divider content-position="left">优化建议</el-divider>

      <div v-if="suggestions.length > 0">
        <el-alert
          v-for="(item, idx) in suggestions"
          :key="idx"
          :title="item.title"
          :description="item.desc"
          :type="item.type"
          show-icon
          :closable="false"
          style="margin-bottom: 10px;"
        />
      </div>
      <div v-else style="text-align:center;padding:20px;color:#999;">
        当前股权结构良好，暂无优化建议
      </div>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关 闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { optimizeEquityStructure } from '@/api/stateAssets/equityStructure'

export default {
  name: 'EquityOptimizeDialog',
  props: {
    visible: { type: Boolean, default: false },
    structureData: { type: Object, default: () => ({}) }
  },
  data() {
    return {
      loading: false,
      controlMap: { DIRECT: '直接控制', INDIRECT: '间接控制' },
      statusMap: { NORMAL: '正常', PLEDGED: '质押', FROZEN: '冻结' },
      backendSuggestions: []
    }
  },
  computed: {
    dialogVisible: {
      get() { return this.visible },
      set(val) { this.$emit('update:visible', val) }
    },
    suggestions() {
      const list = [...this.backendSuggestions]
      const data = this.structureData || {}

      if (data.status === 'PLEDGED') {
        list.push({ title: '质押风险', desc: '当前股权处于质押状态，建议尽快解除质押以降低风险', type: 'warning' })
      }
      if (data.status === 'FROZEN') {
        list.push({ title: '冻结风险', desc: '当前股权处于冻结状态，建议排查冻结原因并尽快解冻', type: 'error' })
      }
      if (Number(data.shareholdingRatio) > 80) {
        list.push({ title: '持股集中度过高', desc: '持股比例超过80%，建议适当分散股权以优化治理结构', type: 'warning' })
      }
      if (data.controlType === 'INDIRECT') {
        list.push({ title: '间接控制链路', desc: '间接控制链路较长，建议简化层级以提高管理效率', type: 'info' })
      }

      list.push({ title: '定期审查', desc: '建议定期审查股权结构，确保符合监管要求和公司治理最佳实践', type: 'info' })
      list.push({ title: '信息披露', desc: '确保股权变动信息及时披露，保持透明度', type: 'info' })

      return list
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.fetchOptimizeSuggestions()
      }
    }
  },
  methods: {
    async fetchOptimizeSuggestions() {
      this.loading = true
      this.backendSuggestions = []
      try {
        const response = await optimizeEquityStructure({
          enterpriseId: this.structureData.enterpriseId,
          equityId: this.structureData.equityId
        })
        if (response && response.result === 200 && response.data) {
          const data = response.data
          if (data.suggestions && Array.isArray(data.suggestions)) {
            data.suggestions.forEach(s => {
              this.backendSuggestions.push({ title: '优化建议', desc: s, type: 'warning' })
            })
          }
          if (data.currentScore && data.optimizedScore) {
            this.backendSuggestions.unshift({
              title: '评分预测',
              desc: `当前结构评分: ${data.currentScore}分，优化后预计: ${data.optimizedScore}分`,
              type: data.currentScore < 70 ? 'error' : 'success'
            })
          }
        }
      } catch (e) {
        console.error('获取优化建议失败:', e)
      } finally {
        this.loading = false
      }
    },
    statusTagType(status) {
      const map = { NORMAL: 'success', PLEDGED: 'warning', FROZEN: 'danger' }
      return map[status] || 'info'
    },
    handleClose() {
      this.dialogVisible = false
    }
  }
}
</script>

<style scoped>
.optimize-content {
  padding: 10px 0;
}
.dialog-footer {
  text-align: right;
}
</style>