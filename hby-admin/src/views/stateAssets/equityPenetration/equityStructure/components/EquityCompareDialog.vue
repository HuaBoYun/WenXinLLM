<template>
  <el-dialog
    title="股权结构对比分析"
    :visible.sync="dialogVisible"
    width="80%"
    :before-close="handleClose"
  >
    <div v-loading="loading">
      <el-alert
        v-if="structureData.investorName"
        :title="'当前选中: ' + structureData.investorName + ' (持股' + (structureData.shareholdingRatio || 0) + '%)'"
        type="info"
        :closable="false"
        style="margin-bottom: 15px;"
      />
      <el-table
        :data="compareList"
        border
        style="width: 100%"
        :row-class-name="highlightRow"
      >
        <el-table-column prop="investorName" label="投资方名称" min-width="120" />
        <el-table-column prop="investorType" label="投资方类型" width="100">
          <template slot-scope="{ row }">
            {{ typeMap[row.investorType] || row.investorType || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="shareholdingRatio" label="持股比例" width="120">
          <template slot-scope="{ row }">
            <el-progress :percentage="Number(row.shareholdingRatio) || 0" :format="() => (row.shareholdingRatio || 0) + '%'" />
          </template>
        </el-table-column>
        <el-table-column prop="shareholdingAmount" label="持股金额(万)" width="120" />
        <el-table-column prop="shareType" label="股份类型" width="100">
          <template slot-scope="{ row }">
            {{ shareTypeMap[row.shareType] || row.shareType || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="votingRatio" label="表决权比例" width="100">
          <template slot-scope="{ row }">
            {{ row.votingRatio ? row.votingRatio + '%' : '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="controlType" label="控制类型" width="100">
          <template slot-scope="{ row }">
            {{ controlMap[row.controlType] || row.controlType || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="80">
          <template slot-scope="{ row }">
            <el-tag :type="statusTagType(row.status)" size="small">{{ statusMap[row.status] || '-' }}</el-tag>
          </template>
        </el-table-column>
      </el-table>
      <div v-if="compareList.length === 0 && !loading" style="text-align:center;padding:30px;color:#999;">
        暂无同企业股权数据
      </div>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关 闭</el-button>
    </div>
  </el-dialog>
</template>

<script>
import { getEquityStructureList } from '@/api/stateAssets/equityStructure'

export default {
  name: 'EquityCompareDialog',
  props: {
    visible: { type: Boolean, default: false },
    structureData: { type: Object, default: () => ({}) }
  },
  data() {
    return {
      loading: false,
      compareList: [],
      typeMap: { ENTERPRISE: '企业', INDIVIDUAL: '个人', GOVERNMENT: '政府', FUND: '基金' },
      controlMap: { DIRECT: '直接', INDIRECT: '间接' },
      statusMap: { NORMAL: '正常', PLEDGED: '质押', FROZEN: '冻结' },
      shareTypeMap: { COMMON: '普通股', PREFERRED: '优先股' }
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
        this.fetchCompareData()
      }
    }
  },
  methods: {
    async fetchCompareData() {
      this.loading = true
      this.compareList = []
      try {
        const response = await getEquityStructureList({ enterpriseId: this.structureData.enterpriseId, pageSize: 50 })
        if (response.result === 200 && response.data) {
          this.compareList = response.data.tlist || response.data.list || response.data.records || []
        }
      } catch (e) {
        this.$message.error('获取对比数据失败')
      } finally {
        this.loading = false
      }
    },
    highlightRow({ row }) {
      if (row.equityId === this.structureData.equityId) {
        return 'current-row-highlight'
      }
      return ''
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
.dialog-footer {
  text-align: right;
}
/deep/ .current-row-highlight {
  background-color: #ecf5ff !important;
}
</style>