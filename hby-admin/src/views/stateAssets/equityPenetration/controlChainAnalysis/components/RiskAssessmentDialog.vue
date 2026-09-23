<template>
  <el-dialog
    title="控制链风险评估"
    :visible.sync="dialogVisible"
    width="700px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <div v-loading="loading" class="risk-container">
      <el-card shadow="never" class="mb-15">
        <div slot="header"><span>评估对象</span></div>
        <el-descriptions :column="2" border size="small">
          <el-descriptions-item label="企业名称">{{ chainData.enterpriseName }}</el-descriptions-item>
          <el-descriptions-item label="控制链总数">{{ chainData.totalChains || 0 }}</el-descriptions-item>
          <el-descriptions-item label="最大链长">{{ chainData.maxChainLength || 0 }} 级</el-descriptions-item>
          <el-descriptions-item label="平均控制强度">{{ chainData.avgControlStrength || 0 }}%</el-descriptions-item>
        </el-descriptions>
      </el-card>
      <el-card shadow="never" class="mb-15">
        <div slot="header"><span>风险评估结果</span></div>
        <el-row :gutter="20">
          <el-col :span="8">
            <div class="risk-level-box" :style="{ borderColor: riskColor }">
              <div class="risk-level-label">整体风险等级</div>
              <div class="risk-level-value" :style="{ color: riskColor }">{{ riskText }}</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="risk-stat-box">
              <div class="risk-stat-value" style="color: #F56C6C;">{{ riskData.highRiskCount || 0 }}</div>
              <div class="risk-stat-label">高风险控制链</div>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="risk-stat-box">
              <div class="risk-stat-value" style="color: #E6A23C;">{{ riskData.loopCount || 0 }}</div>
              <div class="risk-stat-label">环路数量</div>
            </div>
          </el-col>
        </el-row>
      </el-card>
      <el-card shadow="never" class="mb-15">
        <div slot="header"><span>风险因素分析</span></div>
        <el-table :data="riskFactors" border size="small" style="width: 100%;">
          <el-table-column label="风险因素" prop="factor" min-width="180" />
          <el-table-column label="风险等级" prop="level" width="100" align="center">
            <template slot-scope="scope">
              <el-tag :type="scope.row.levelType" size="mini">{{ scope.row.level }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="说明" prop="description" min-width="200" />
        </el-table>
      </el-card>
      <el-card shadow="never">
        <div slot="header"><span>评估建议</span></div>
        <div class="suggestion-text">{{ suggestion }}</div>
      </el-card>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="handleRefreshAssessment" :loading="loading">重新评估</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getControlChainRiskAssessment } from '@/api/stateAssets/controlChainAnalysis'
export default {
  name: 'RiskAssessmentDialog',
  props: {
    visible: { type: Boolean, default: false },
    chainData: { type: Object, default: () => ({}) }
  },
  data() {
    return { loading: false, riskData: {}, riskFactors: [] }
  },
  computed: {
    dialogVisible: {
      get() { return this.visible },
      set(val) { this.$emit('update:visible', val) }
    },
    riskLevel() { return this.riskData.overallRiskLevel || 'LOW' },
    riskColor() {
      return { HIGH: '#F56C6C', MEDIUM: '#E6A23C', LOW: '#67C23A' }[this.riskLevel] || '#909399'
    },
    riskText() {
      return { HIGH: '高风险', MEDIUM: '中风险', LOW: '低风险' }[this.riskLevel] || '未评估'
    },
    suggestion() {
      if ((this.riskData.highRiskCount || 0) > 0) {
        return '存在高风险控制链，建议重点关注并加强监管。需排查环路结构和过长链路，防范利益输送和控制权不稳定风险。'
      }
      if ((this.riskData.loopCount || 0) > 0) {
        return '存在控制环路，建议理清股权关系，消除交叉持股带来的权责不清风险。'
      }
      return '当前控制链风险可控，建议持续监测控制链变化，定期进行风险评估。'
    }
  },
  watch: {
    visible(val) { if (val) this.fetchRiskAssessment() }
  },
  methods: {
    async fetchRiskAssessment() {
      this.loading = true
      try {
        const res = await getControlChainRiskAssessment({
          enterpriseId: this.chainData.enterpriseId, statId: this.chainData.statId
        })
        if (res && res.result === 200) {
          this.riskData = res.data || {}
          this.buildRiskFactors()
        } else { this.$message.error(res?.msg || '风险评估失败') }
      } catch (e) {
        console.error('风险评估失败:', e)
        this.$message.error('风险评估请求失败')
      } finally { this.loading = false }
    },
    buildRiskFactors() {
      const factors = []
      const maxLen = this.chainData.maxChainLength || 0
      const loopCount = this.riskData.loopCount || this.chainData.loopCount || 0
      const highRisk = this.riskData.highRiskCount || this.chainData.highRiskCount || 0
      const strength = this.chainData.avgControlStrength || 0
      if (maxLen > 4) {
        factors.push({ factor: '控制链路过长', level: '高', levelType: 'danger', description: '最大链长' + maxLen + '级，超过4级预警阈值' })
      } else if (maxLen > 2) {
        factors.push({ factor: '控制链路较长', level: '中', levelType: 'warning', description: '最大链长' + maxLen + '级，需关注管理效率' })
      }
      if (loopCount > 0) {
        factors.push({ factor: '存在控制环路', level: '高', levelType: 'danger', description: '检测到' + loopCount + '个环路，可能导致利益冲突' })
      }
      if (highRisk > 0) {
        factors.push({ factor: '高风险控制链', level: '高', levelType: 'danger', description: '存在' + highRisk + '条高风险控制链' })
      }
      if (strength < 50) {
        factors.push({ factor: '控制强度不足', level: '中', levelType: 'warning', description: '平均控制强度' + strength + '%，低于50%阈值' })
      }
      if (factors.length === 0) {
        factors.push({ factor: '整体风险可控', level: '低', levelType: 'success', description: '未发现明显风险因素' })
      }
      this.riskFactors = factors
    },
    handleRefreshAssessment() { this.fetchRiskAssessment() },
    handleClose() { this.dialogVisible = false }
  }
}
</script>

<style scoped>
.risk-container { max-height: 65vh; overflow-y: auto; }
.mb-15 { margin-bottom: 15px; }
.risk-level-box { text-align: center; padding: 15px; border: 2px solid; border-radius: 8px; }
.risk-level-label { font-size: 13px; color: #909399; margin-bottom: 8px; }
.risk-level-value { font-size: 24px; font-weight: bold; }
.risk-stat-box { text-align: center; padding: 15px; }
.risk-stat-value { font-size: 28px; font-weight: bold; line-height: 1; }
.risk-stat-label { font-size: 13px; color: #909399; margin-top: 8px; }
.suggestion-text { line-height: 1.8; color: #606266; font-size: 14px; }
.dialog-footer { text-align: right; }
</style>
