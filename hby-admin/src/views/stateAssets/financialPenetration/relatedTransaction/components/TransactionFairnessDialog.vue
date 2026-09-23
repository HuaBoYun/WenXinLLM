<template>
  <el-dialog
    title="交易公允性分析"
    :visible.sync="dialogVisible"
    width="700px"
    :before-close="handleClose"
  >
    <el-descriptions title="交易信息" :column="2" border size="medium" style="margin-bottom: 20px;">
      <el-descriptions-item label="关联方">{{ transactionData.partyName || '-' }}</el-descriptions-item>
      <el-descriptions-item label="交易类型">{{ transactionData.transactionType || '-' }}</el-descriptions-item>
      <el-descriptions-item label="交易金额">{{ transactionData.transactionAmount || 0 }} 万元</el-descriptions-item>
      <el-descriptions-item label="是否重大">{{ transactionData.isMajor === '1' ? '是' : '否' }}</el-descriptions-item>
    </el-descriptions>

    <el-card shadow="never" style="margin-bottom: 20px;">
      <div slot="header"><span>公允性评分</span></div>
      <div style="text-align: center; padding: 20px 0;">
        <el-progress type="dashboard" :percentage="fairnessScore" :color="scoreColor" :width="150">
          <template slot="default">
            <span style="font-size: 24px; font-weight: bold;">{{ fairnessScore }}</span>
            <br>
            <span style="font-size: 12px; color: #909399;">公允性评分</span>
          </template>
        </el-progress>
      </div>
    </el-card>

    <el-card shadow="never" style="margin-bottom: 20px;">
      <div slot="header"><span>评估流程</span></div>
      <el-steps :active="3" finish-status="success" align-center>
        <el-step title="数据采集" description="收集交易基础数据" />
        <el-step title="价格比对" description="与市场价格对比" />
        <el-step title="合规审查" description="审查交易合规性" />
        <el-step title="综合评定" description="生成评估结论" />
      </el-steps>
    </el-card>

    <el-alert
      :title="conclusion.title"
      :type="conclusion.type"
      :description="conclusion.description"
      show-icon
      :closable="false"
    />

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'TransactionFairnessDialog',
  props: {
    visible: { type: Boolean, default: false },
    transactionData: { type: Object, default: () => ({}) }
  },
  computed: {
    dialogVisible: {
      get() { return this.visible },
      set(val) { this.$emit('update:visible', val) }
    },
    fairnessScore() {
      // 优先使用后端返回的评分
      if (this.transactionData.fairnessScore !== undefined) return this.transactionData.fairnessScore
      let score = 85
      const amount = Number(this.transactionData.transactionAmount) || 0
      if (amount > 10000) score -= 10
      if (amount > 50000) score -= 15
      if (this.transactionData.isMajor === '1') score -= 10
      return Math.max(Math.min(score, 100), 0)
    },
    scoreColor() {
      if (this.fairnessScore >= 80) return '#67C23A'
      if (this.fairnessScore >= 60) return '#E6A23C'
      return '#F56C6C'
    },
    conclusion() {
      // 优先使用后端返回的结论
      if (this.transactionData.conclusion) {
        const c = this.transactionData.conclusion
        if (c === '公允性良好') return { title: c, type: 'success', description: '该笔关联交易定价合理，交易条件公允，未发现利益输送迹象。' }
        if (c === '公允性一般') return { title: c, type: 'warning', description: '该笔关联交易存在一定风险，建议加强审查交易定价依据和商业合理性。' }
        return { title: c, type: 'error', description: '该笔关联交易公允性评分较低，建议重点关注是否存在利益输送或不当定价。' }
      }
      if (this.fairnessScore >= 80) return { title: '公允性良好', type: 'success', description: '该笔关联交易定价合理，交易条件公允，未发现利益输送迹象。' }
      if (this.fairnessScore >= 60) return { title: '公允性一般', type: 'warning', description: '该笔关联交易存在一定风险，建议加强审查交易定价依据和商业合理性。' }
      return { title: '公允性不足', type: 'error', description: '该笔关联交易公允性评分较低，建议重点关注是否存在利益输送或不当定价。' }
    }
  },
  methods: {
    handleClose() { this.dialogVisible = false }
  }
}
</script>

<style scoped>
.dialog-footer { text-align: right; }
</style>