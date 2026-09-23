<template>
  <el-dialog title="控制变更模拟" :visible.sync="dialogVisible" width="650px" :before-close="handleClose">
    <div v-if="chainData && chainData.enterpriseName">
      <el-alert title="模拟股权变更对控制链的影响，不会修改实际数据" type="info" show-icon :closable="false" style="margin-bottom:16px" />
      <el-form :model="simForm" label-width="120px" size="small">
        <el-form-item label="目标企业"><el-input :value="chainData.enterpriseName" disabled /></el-form-item>
        <el-form-item label="当前控制强度"><el-progress :percentage="chainData.controlStrength" style="width:200px;display:inline-block" /> <span style="margin-left:8px">{{ chainData.controlStrength }}%</span></el-form-item>
        <el-form-item label="模拟变更类型">
          <el-radio-group v-model="simForm.changeType">
            <el-radio label="INCREASE">增持股权</el-radio>
            <el-radio label="DECREASE">减持股权</el-radio>
            <el-radio label="TRANSFER">股权转让</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="变更比例(%)"><el-input-number v-model="simForm.changeRatio" :min="1" :max="100" :step="5" style="width:200px" /></el-form-item>
        <el-form-item>
          <el-button type="primary" :loading="loading" @click="runSimulation">执行模拟</el-button>
          <el-button @click="simResult = null">重置</el-button>
        </el-form-item>
      </el-form>
      <div v-if="simResult">
        <el-divider content-position="left">模拟结果</el-divider>
        <el-descriptions :column="2" border size="small">
          <el-descriptions-item label="变更前控制强度">{{ simResult.originalStrength }}%</el-descriptions-item>
          <el-descriptions-item label="变更后控制强度">
            <span :style="{ color: simResult.newStrength < 50 ? '#f56c6c' : '#67c23a', fontWeight:'bold' }">{{ simResult.newStrength }}%</span>
          </el-descriptions-item>
          <el-descriptions-item label="控制状态变化">
            <el-tag :type="getImpactTagType(simResult.impactLevel)" size="small">{{ getStatusLabel(simResult.statusChange) }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="风险评估">{{ simResult.riskDesc }}</el-descriptions-item>
        </el-descriptions>
        <el-alert v-if="simResult.newStrength < 50" title="模拟结果显示控制强度将低于50%安全线，建议谨慎操作" type="error" show-icon :closable="false" style="margin-top:12px" />
      </div>
    </div>
    <div slot="footer"><el-button @click="handleClose">关闭</el-button></div>
  </el-dialog>
</template>
<script>
import { simulateControlChanges } from '@/api/stateAssets/controlChain'

export default {
  name: 'ControlSimulateDialog',
  props: {
    visible: { type: Boolean, default: false },
    chainData: { type: Object, default: () => ({}) }
  },
  data() {
    return {
      simForm: { changeType: 'INCREASE', changeRatio: 10 },
      simResult: null,
      loading: false
    }
  },
  computed: {
    dialogVisible: {
      get() { return this.visible },
      set(val) { this.$emit('update:visible', val) }
    }
  },
  methods: {
    handleClose() {
      this.dialogVisible = false
      this.simResult = null
    },
    getStatusLabel(statusChange) {
      const map = {
        CONTROL_LOSS: '失控',
        MAJOR_CHANGE: '重大变化',
        MODERATE_CHANGE: '中等变化',
        MINOR_CHANGE: '轻微变化',
        STABLE: '稳定'
      }
      return map[statusChange] || statusChange
    },
    getImpactTagType(impactLevel) {
      const map = {
        CRITICAL: 'danger',
        HIGH: 'danger',
        MEDIUM: 'warning',
        LOW: 'success'
      }
      return map[impactLevel] || 'info'
    },
    async runSimulation() {
      this.loading = true
      this.simResult = null
      try {
        const res = await simulateControlChanges({
          chainId: this.chainData.chainId,
          changeType: this.simForm.changeType,
          changeRatio: this.simForm.changeRatio
        })
        if (res.result === 200 && res.data) {
          this.simResult = res.data
        } else {
          this.$message.error(res.msg || '模拟请求失败')
        }
      } catch (error) {
        this.$message.error('模拟请求异常，请稍后重试')
      } finally {
        this.loading = false
      }
    }
  }
}
</script>