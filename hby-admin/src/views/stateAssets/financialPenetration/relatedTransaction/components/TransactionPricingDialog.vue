<template>
  <el-dialog
    title="交易定价分析"
    :visible.sync="dialogVisible"
    width="700px"
    :before-close="handleClose"
  >
    <el-descriptions title="交易基本信息" :column="2" border size="medium" style="margin-bottom: 20px;">
      <el-descriptions-item label="关联方">{{ transactionData.partyName || '-' }}</el-descriptions-item>
      <el-descriptions-item label="交易类型">{{ transactionData.transactionType || '-' }}</el-descriptions-item>
      <el-descriptions-item label="资金性质">{{ formatFundNature(transactionData.relationType) }}</el-descriptions-item>
      <el-descriptions-item label="所属期间">{{ transactionData.period || '-' }}</el-descriptions-item>
    </el-descriptions>

    <el-card shadow="never" style="margin-bottom: 20px;">
      <div slot="header"><span>定价对比分析</span></div>
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="pricing-item">
            <div class="pricing-label">交易价格</div>
            <div class="pricing-value">{{ transactionData.transactionAmount || 0 }} 万元</div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="pricing-item">
            <div class="pricing-label">市场参考价</div>
            <div class="pricing-value">{{ marketPrice }} 万元</div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="pricing-item">
            <div class="pricing-label">偏离度</div>
            <div class="pricing-value" :style="{ color: deviationColor }">{{ deviationPercent }}%</div>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <el-card shadow="never">
      <div slot="header"><span>价格偏离评估</span></div>
      <div style="margin-bottom: 12px;">
        <span>偏离程度：</span>
        <el-progress
          :percentage="Math.min(Math.abs(deviationPercent) * 10, 100)"
          :color="deviationColor"
          :stroke-width="18"
          :text-inside="true"
          :format="() => deviationPercent + '%'"
        />
      </div>
      <el-alert
        :title="riskAssessment.title"
        :type="riskAssessment.type"
        :description="riskAssessment.description"
        show-icon
        :closable="false"
      />
    </el-card>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'TransactionPricingDialog',
  props: {
    visible: { type: Boolean, default: false },
    transactionData: { type: Object, default: () => ({}) }
  },
  computed: {
    dialogVisible: {
      get() { return this.visible },
      set(val) { this.$emit('update:visible', val) }
    },
    marketPrice() {
      return this.transactionData.marketPrice || ((this.transactionData.transactionAmount || 0) * 0.95).toFixed(2)
    },
    deviationPercent() {
      if (this.transactionData.deviationPercent !== undefined) return this.transactionData.deviationPercent
      const amount = this.transactionData.transactionAmount || 0
      if (!amount) return 0
      return (((amount - this.marketPrice) / this.marketPrice) * 100).toFixed(2)
    },
    deviationColor() {
      const d = Math.abs(this.deviationPercent)
      if (d <= 3) return '#67C23A'
      if (d <= 7) return '#E6A23C'
      return '#F56C6C'
    },
    riskAssessment() {
      if (this.transactionData.riskLevel) {
        const level = this.transactionData.riskLevel
        if (level === '低风险') return { title: '低风险', type: 'success', description: '交易定价与市场价格偏离较小，定价合理。' }
        if (level === '中等风险') return { title: '中等风险', type: 'warning', description: '交易定价与市场价格存在一定偏离，建议关注定价依据。' }
        return { title: '高风险', type: 'error', description: '交易定价与市场价格偏离较大，需重点审查定价合理性。' }
      }
      const d = Math.abs(this.deviationPercent)
      if (d <= 3) return { title: '低风险', type: 'success', description: '交易定价与市场价格偏离较小，定价合理。' }
      if (d <= 7) return { title: '中等风险', type: 'warning', description: '交易定价与市场价格存在一定偏离，建议关注定价依据。' }
      return { title: '高风险', type: 'error', description: '交易定价与市场价格偏离较大，需重点审查定价合理性。' }
    }
  },
  methods: {
    handleClose() { this.dialogVisible = false },
    formatFundNature(value) {
      const map = { OPERATING: '经营性', INVESTING: '投资性', FINANCING: '筹资性', OWN_FUND: '自有资金', BORROWED_FUND: '借入资金', INVESTMENT_FUND: '投资资金', OTHER: '其他' }
      return map[value] || value || '-'
    }
  }
}
</script>

<style scoped>
.dialog-footer { text-align: right; }
.pricing-item { text-align: center; padding: 16px 0; }
.pricing-label { font-size: 13px; color: #909399; margin-bottom: 8px; }
.pricing-value { font-size: 20px; font-weight: bold; color: #303133; }
</style>