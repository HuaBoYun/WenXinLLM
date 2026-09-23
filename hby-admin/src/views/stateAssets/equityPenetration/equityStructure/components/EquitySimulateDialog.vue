<template>
  <el-dialog
    title="股权变更模拟"
    :visible.sync="dialogVisible"
    width="600px"
    :before-close="handleClose"
  >
    <div class="simulate-content">
      <el-descriptions title="当前股权信息" :column="2" border style="margin-bottom:20px;">
        <el-descriptions-item label="投资方">{{ structureData.investorName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="当前持股比例">{{ structureData.shareholdingRatio || 0 }}%</el-descriptions-item>
      </el-descriptions>

      <el-form label-width="120px">
        <el-form-item label="模拟新比例%">
          <el-row :gutter="20">
            <el-col :span="16">
              <el-slider v-model="newRatio" :min="0" :max="100" :step="0.1" show-input />
            </el-col>
          </el-row>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSimulate">模 拟</el-button>
        </el-form-item>
      </el-form>

      <div v-if="showResult" class="simulate-result">
        <el-divider content-position="left">模拟结果</el-divider>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-statistic title="变动幅度" :value="ratioChange" suffix="%" :value-style="{ color: ratioChange >= 0 ? '#67C23A' : '#F56C6C' }" />
          </el-col>
          <el-col :span="12">
            <el-statistic title="新持股比例" :value="newRatio" suffix="%" />
          </el-col>
        </el-row>
        <el-alert
          v-for="(item, idx) in impactAnalysis"
          :key="idx"
          :title="item.title"
          :description="item.desc"
          :type="item.type"
          :closable="false"
          style="margin-top: 10px;"
        />
      </div>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关 闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { simulateEquityChanges } from '@/api/stateAssets/equityStructure'

export default {
  name: 'EquitySimulateDialog',
  props: {
    visible: { type: Boolean, default: false },
    structureData: { type: Object, default: () => ({}) }
  },
  data() {
    return {
      newRatio: 0,
      showResult: false,
      loading: false,
      impactAnalysis: []
    }
  },
  computed: {
    dialogVisible: {
      get() { return this.visible },
      set(val) { this.$emit('update:visible', val) }
    },
    ratioChange() {
      return Number((this.newRatio - (this.structureData.shareholdingRatio || 0)).toFixed(2))
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.newRatio = Number(this.structureData.shareholdingRatio) || 0
        this.showResult = false
        this.impactAnalysis = []
      }
    }
  },
  methods: {
    async handleSimulate() {
      this.loading = true
      this.impactAnalysis = []
      try {
        const response = await simulateEquityChanges({
          equityId: this.structureData.equityId,
          enterpriseId: this.structureData.enterpriseId,
          investorName: this.structureData.investorName,
          currentRatio: this.structureData.shareholdingRatio,
          newRatio: this.newRatio
        })
        if (response && response.result === 200 && response.data) {
          // 使用后端返回的模拟结果
          const data = response.data
          if (data.impactLevel === 'HIGH') {
            this.impactAnalysis.push({ title: '高影响变更', desc: data.suggestion || '此变更影响较大，请谨慎操作', type: 'error' })
          } else if (data.impactLevel === 'MEDIUM') {
            this.impactAnalysis.push({ title: '中等影响变更', desc: data.suggestion || '此变更有一定影响', type: 'warning' })
          } else {
            this.impactAnalysis.push({ title: '低影响变更', desc: data.suggestion || '此变更影响较小', type: 'success' })
          }
        }
      } catch (e) {
        console.error('模拟请求失败:', e)
      }

      // 补充本地分析逻辑
      const ratio = this.newRatio
      if (ratio >= 67) {
        this.impactAnalysis.push({ title: '绝对控制', desc: '持股比例≥67%，拥有绝对控制权，可单独通过所有股东会决议', type: 'success' })
      } else if (ratio > 50) {
        this.impactAnalysis.push({ title: '相对控制', desc: '持股比例>50%，拥有相对控制权，可通过一般决议', type: 'success' })
      } else if (ratio >= 34) {
        this.impactAnalysis.push({ title: '一票否决权', desc: '持股比例≥34%，拥有一票否决权，可阻止特别决议通过', type: 'warning' })
      } else if (ratio >= 10) {
        this.impactAnalysis.push({ title: '重要少数股东', desc: '持股比例≥10%，有权提议召开临时股东会', type: 'info' })
      } else if (ratio > 0) {
        this.impactAnalysis.push({ title: '小股东', desc: '持股比例较低，话语权有限', type: 'info' })
      }

      if (this.ratioChange > 0) {
        this.impactAnalysis.push({ title: '增持影响', desc: `持股比例增加${this.ratioChange}%，控制力增强`, type: 'info' })
      } else if (this.ratioChange < 0) {
        this.impactAnalysis.push({ title: '减持影响', desc: `持股比例减少${Math.abs(this.ratioChange)}%，控制力减弱`, type: 'warning' })
      }

      this.showResult = true
      this.loading = false
    },
    handleClose() {
      this.dialogVisible = false
    }
  }
}
</script>

<style scoped>
.simulate-content {
  padding: 10px 0;
}
.simulate-result {
  margin-top: 10px;
}
.dialog-footer {
  text-align: right;
}
</style>