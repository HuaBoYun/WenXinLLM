<template>
  <el-dialog
    title="绩效模拟分析"
    :visible.sync="dialogVisible"
    width="70%"
    :before-close="handleClose"
  >
    <el-form :model="simulateForm" label-width="120px" size="small">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="企业名称">
            <el-input v-model="simulateForm.companyName" disabled />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="模拟场景">
            <el-select v-model="simulateForm.scenario" placeholder="请选择模拟场景">
              <el-option label="乐观场景" value="OPTIMISTIC" />
              <el-option label="基准场景" value="BASELINE" />
              <el-option label="悲观场景" value="PESSIMISTIC" />
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="营收增长率(%)">
            <el-input-number v-model="simulateForm.revenueGrowth" :min="-50" :max="100" :precision="1" style="width:100%" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="利润增长率(%)">
            <el-input-number v-model="simulateForm.profitGrowth" :min="-50" :max="100" :precision="1" style="width:100%" />
          </el-form-item>
        </el-col>
      </el-row>
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item label="资产回报率目标(%)">
            <el-input-number v-model="simulateForm.targetROA" :min="0" :max="50" :precision="1" style="width:100%" />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item label="权益回报率目标(%)">
            <el-input-number v-model="simulateForm.targetROE" :min="0" :max="50" :precision="1" style="width:100%" />
          </el-form-item>
        </el-col>
      </el-row>
    </el-form>

    <el-divider>模拟结果</el-divider>

    <el-row :gutter="20" v-if="simulateResult">
      <el-col :span="6">
        <el-card shadow="hover" class="result-card">
          <div class="result-value" style="color:#1677FF">{{ simulateResult.projectedScore }}分</div>
          <div class="result-label">预测绩效评分</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="result-card">
          <div class="result-value" style="color:#52C41A">{{ simulateResult.projectedROE }}%</div>
          <div class="result-label">预测ROE</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="result-card">
          <div class="result-value" style="color:#FA8C16">{{ simulateResult.projectedROA }}%</div>
          <div class="result-label">预测ROA</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="result-card">
          <div class="result-value" :style="{color: simulateResult.change >= 0 ? '#52C41A' : '#F5222D'}">{{ simulateResult.change >= 0 ? '+' : '' }}{{ simulateResult.change }}%</div>
          <div class="result-label">绩效变化</div>
        </el-card>
      </el-col>
    </el-row>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="runSimulate" :loading="simulating">运行模拟</el-button>
    </div>
  </el-dialog>
</template>

<script>
export default {
  name: 'PerformanceSimulateDialog',
  props: {
    visible: { type: Boolean, default: false },
    data: { type: Object, default: () => ({}) }
  },
  data() {
    return {
      simulating: false,
      simulateForm: {
        companyName: '',
        scenario: 'BASELINE',
        revenueGrowth: 5.0,
        profitGrowth: 3.0,
        targetROA: 5.0,
        targetROE: 10.0
      },
      simulateResult: null
    }
  },
  computed: {
    dialogVisible: {
      get() { return this.visible },
      set(val) { this.$emit('update:visible', val) }
    }
  },
  watch: {
    visible(val) {
      if (val) {
        this.simulateForm.companyName = this.data.enterpriseName || this.data.companyName || ''
        this.simulateResult = null
      }
    }
  },
  methods: {
    runSimulate() {
      this.simulating = true
      setTimeout(() => {
        const base = this.data.performanceScore || 78
        const growthFactor = this.simulateForm.revenueGrowth / 10
        const profitFactor = this.simulateForm.profitGrowth / 10
        const scenarioFactor = this.simulateForm.scenario === 'OPTIMISTIC' ? 1.1 : this.simulateForm.scenario === 'PESSIMISTIC' ? 0.85 : 1.0
        const projectedScore = Math.min(100, Math.max(0, Math.round((base + growthFactor + profitFactor) * scenarioFactor)))
        const change = Math.round((projectedScore - base) / base * 100 * 10) / 10
        this.simulateResult = {
          projectedScore,
          projectedROE: Math.round((this.simulateForm.targetROE * scenarioFactor) * 10) / 10,
          projectedROA: Math.round((this.simulateForm.targetROA * scenarioFactor) * 10) / 10,
          change
        }
        this.simulating = false
      }, 800)
    },
    handleClose() { this.dialogVisible = false }
  }
}
</script>

<style scoped>
.dialog-footer { text-align: right; }
.result-card { text-align: center; padding: 10px; }
.result-value { font-size: 24px; font-weight: 700; }
.result-label { font-size: 12px; color: #888; margin-top: 4px; }
</style>
