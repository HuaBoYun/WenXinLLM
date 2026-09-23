<template>
  <el-dialog
    title="股权穿透结构图谱"
    :visible.sync="dialogVisible"
    width="80%"
    :before-close="handleClose"
    class="equity-chart-dialog"
  >
    <div v-loading="loading">
      <!-- 树形结构展示 -->
      <div v-if="treeData.length > 0" class="chart-tree">
        <h4 style="margin-bottom: 15px;">股东结构</h4>
        <el-table :data="treeData" border style="width: 100%">
          <el-table-column prop="name" label="投资方名称" />
          <el-table-column prop="ratio" label="持股比例">
            <template slot-scope="{ row }">
              <el-progress :percentage="Number(row.ratio) || 0" :format="() => (row.ratio || 0) + '%'" />
            </template>
          </el-table-column>
          <el-table-column prop="type" label="投资方类型">
            <template slot-scope="{ row }">
              <el-tag size="small">{{ typeMap[row.type] || row.type || '-' }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="controlType" label="控制类型">
            <template slot-scope="{ row }">
              {{ controlMap[row.controlType] || row.controlType || '-' }}
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 降级展示：当前行数据 -->
      <div v-else class="chart-fallback">
        <el-descriptions title="当前股权信息" :column="2" border>
          <el-descriptions-item label="投资方名称">{{ structureData.investorName || '-' }}</el-descriptions-item>
          <el-descriptions-item label="持股比例">{{ structureData.shareholdingRatio || 0 }}%</el-descriptions-item>
          <el-descriptions-item label="控制类型">{{ controlMap[structureData.controlType] || '-' }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="statusTagType(structureData.status)" size="small">{{ statusMap[structureData.status] || '-' }}</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="投资方类型">{{ typeMap[structureData.investorType] || '-' }}</el-descriptions-item>
          <el-descriptions-item label="持股金额(万)">{{ structureData.shareholdingAmount || '-' }}</el-descriptions-item>
        </el-descriptions>
      </div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关 闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { buildEquityStructureChart } from '@/api/stateAssets/equityStructure'

export default {
  name: 'EquityStructureChartDialog',
  props: {
    visible: { type: Boolean, default: false },
    structureData: { type: Object, default: () => ({}) }
  },
  data() {
    return {
      loading: false,
      treeData: [],
      typeMap: { ENTERPRISE: '企业', INDIVIDUAL: '个人', GOVERNMENT: '政府', FUND: '基金' },
      controlMap: { DIRECT: '直接控制', INDIRECT: '间接控制' },
      statusMap: { NORMAL: '正常', PLEDGED: '质押', FROZEN: '冻结' }
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
        this.fetchChartData()
      }
    }
  },
  methods: {
    async fetchChartData() {
      this.loading = true
      this.treeData = []
      try {
        const response = await buildEquityStructureChart({ enterpriseId: this.structureData.enterpriseId })
        if (response.result === 200 && response.data && response.data.nodes) {
          this.treeData = response.data.nodes
        }
      } catch (e) {
        // 降级展示当前行数据
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
.chart-tree {
  min-height: 200px;
}
.chart-fallback {
  padding: 20px 0;
}
.dialog-footer {
  text-align: right;
}
</style>