<template>
  <el-dialog
    title="股东穿透图谱"
    :visible.sync="dialogVisible"
    width="750px"
    :before-close="handleClose"
  >
    <div v-if="shareholderData && shareholderData.shareholderName">
      <el-descriptions title="股东基本信息" :column="2" border size="medium">
        <el-descriptions-item label="企业名称">{{ shareholderData.enterpriseName }}</el-descriptions-item>
        <el-descriptions-item label="股东名称">{{ shareholderData.shareholderName }}</el-descriptions-item>
        <el-descriptions-item label="股东类型">{{ typeText }}</el-descriptions-item>
        <el-descriptions-item label="持股比例">{{ shareholderData.shareholdingRatio }}%</el-descriptions-item>
        <el-descriptions-item label="持股金额(万)">{{ shareholderData.shareholdingAmount || '-' }}</el-descriptions-item>
        <el-descriptions-item label="风险等级">
          <el-tag :type="riskTag" size="small">{{ riskText }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="是否关联方">{{ shareholderData.isRelatedParty === '1' ? '是' : '否' }}</el-descriptions-item>
        <el-descriptions-item label="最终控制人">{{ shareholderData.ultimateController || '-' }}</el-descriptions-item>
      </el-descriptions>

      <div style="margin-top: 24px;" v-loading="networkLoading">
        <h4 style="margin-bottom: 12px;">穿透路径</h4>
        <el-steps :active="shareholderData.penetrationLevel || 1" align-center finish-status="success">
          <el-step
            v-for="level in penetrationSteps"
            :key="level"
            :title="'第' + level + '层'"
            :description="level === (shareholderData.penetrationLevel || 1) ? shareholderData.ultimateController || '最终控制人' : ''"
          />
        </el-steps>

        <div v-if="networkNodes.length > 0" style="margin-top: 20px;">
          <h4 style="margin-bottom: 12px;">关联股东列表（共 {{ networkNodes.length }} 个）</h4>
          <el-table :data="networkNodes" size="small" border stripe>
            <el-table-column prop="name" label="股东名称" show-overflow-tooltip />
            <el-table-column prop="type" label="类型" width="80" align="center">
              <template slot-scope="scope">{{ typeTextMap[scope.row.type] || scope.row.type || '-' }}</template>
            </el-table-column>
            <el-table-column prop="ratio" label="持股比例" width="100" align="center">
              <template slot-scope="scope">{{ scope.row.ratio != null ? scope.row.ratio + '%' : '-' }}</template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </div>
    <div v-else style="text-align: center; padding: 40px; color: #999;">
      <i class="el-icon-warning-outline" style="font-size: 36px;"></i>
      <p>暂无股东数据</p>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关 闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getShareholderNetworkData } from '@/api/stateAssets/shareholderAnalysis'

export default {
  name: 'ShareholderChartDialog',
  props: {
    visible: { type: Boolean, default: false },
    shareholderData: { type: Object, default: () => ({}) },
  },
  data() {
    return {
      networkLoading: false,
      networkNodes: [],
      typeTextMap: {
        ENTERPRISE: '企业',
        INDIVIDUAL: '个人',
        GOVERNMENT: '政府',
        FUND: '基金',
      },
    }
  },
  computed: {
    dialogVisible: {
      get() { return this.visible },
      set(val) { this.$emit('update:visible', val) },
    },
    typeText() {
      return this.typeTextMap[this.shareholderData.shareholderType] || this.shareholderData.shareholderType || '-'
    },
    riskText() {
      const map = { LOW: '低风险', MEDIUM: '中风险', HIGH: '高风险', CRITICAL: '极高风险' }
      return map[this.shareholderData.riskLevel] || this.shareholderData.riskLevel || '-'
    },
    riskTag() {
      const map = { LOW: 'success', MEDIUM: 'warning', HIGH: 'danger', CRITICAL: 'danger' }
      return map[this.shareholderData.riskLevel] || 'info'
    },
    penetrationSteps() {
      const level = this.shareholderData.penetrationLevel || 1
      return Array.from({ length: level }, (_, i) => i + 1)
    },
  },
  watch: {
    visible(val) {
      if (val && this.shareholderData && this.shareholderData.enterpriseId) {
        this.loadNetworkData()
      }
    },
  },
  methods: {
    async loadNetworkData() {
      this.networkLoading = true
      this.networkNodes = []
      try {
        const res = await getShareholderNetworkData({
          enterpriseId: this.shareholderData.enterpriseId,
        })
        if (res && res.result === 200 && res.data) {
          this.networkNodes = res.data.nodes || []
        }
      } catch (e) {
        console.error('获取网络图数据失败:', e)
      } finally {
        this.networkLoading = false
      }
    },
    handleClose() { this.dialogVisible = false },
  },
}
</script>

<style scoped>
.dialog-footer { text-align: right; }
</style>