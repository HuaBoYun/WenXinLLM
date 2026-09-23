<template>
  <div class="risk-top10">
    <div class="title-container">
      <h3 class="chart-title">风险 top10</h3>
    </div>
    <el-table
      v-loading="listLoading"
      border
      :data="tableData"
      max-height="480"
      stripe
      style="width: 100%"
    >
      <el-table-column align="center" label="序号" width="80">
        <template #default="scope">
          {{ scope.$index + 1 }}
        </template>
      </el-table-column>

      <el-table-column
        align="center"
        label="风险描述"
        prop="riskdes"
        show-overflow-tooltip
      />

      <el-table-column align="center" label="风险变化趋势" width="130">
        <template #default="{ row }">
          <el-button
            :disabled="!row.evaluationid"
            type="text"
            @click="handleTrendClick(row)"
          >
            {{ formatRiskChange(row.riskchange) }}
          </el-button>
        </template>
      </el-table-column>

      <el-table-column
        align="center"
        label="责任部门"
        prop="orgname"
        show-overflow-tooltip
      />

      <el-table-column
        align="center"
        label="责任领导"
        prop="field6"
        show-overflow-tooltip
      />
    </el-table>

    <el-dialog
      :append-to-body="true"
      :close-on-click-modal="false"
      title="详情"
      :visible.sync="trendDialogVisible"
      width="1000px"
      @close="closeTrendDialog"
    >
      <valueEdit
        v-if="trendDialogVisible"
        :cur-row="trendCurRow"
        @close="closeTrendDialog"
        @fetchData="fetchData"
      />
    </el-dialog>
  </div>
</template>

<script>
  import { getRiskTopList } from '@/api/risk/home'
  import valueEdit from '@/views/risk/riskvalue/valueEdit'

  export default {
    name: 'RiskTop10',
    components: { valueEdit },
    data() {
      return {
        listLoading: false,
        tableData: [],
        trendDialogVisible: false,
        trendCurRow: {},
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      async fetchData() {
        this.listLoading = true
        try {
          const res = await getRiskTopList()
          if (res && res.code == 1) {
            const list =
              (res.data && (res.data.data || res.data.list || res.data)) || []
            const normalized = Array.isArray(list) ? [...list] : []
            normalized.sort(
              (a, b) => Number(a.riskorder || 999) - Number(b.riskorder || 999)
            )
            this.tableData = normalized.slice(0, 10)
          }
        } catch (error) {
          console.error('获取风险top10失败:', error)
          this.$message.error('获取风险top10失败')
        } finally {
          this.listLoading = false
        }
      },
      formatRiskChange(value) {
        const levelMap = {
          1: '升高',
          2: '持平',
          3: '下降',
        }
        return levelMap[value] || value || '--'
      },
      handleTrendClick(row) {
        if (!row.evaluationid) {
          this.$message.warning('未找到对应的月度评估ID')
          return
        }

        this.trendCurRow = {
          id: row.evaluationid,
          riskid: row.riskid || '',
          disabled: true,
        }
        this.trendDialogVisible = true
      },
      closeTrendDialog() {
        this.trendDialogVisible = false
        this.trendCurRow = {}
      },
    },
  }
</script>

<style scoped lang="scss">
  .risk-top10 {
    padding: 20px;
  }
  .title-container {
    margin-bottom: 10px;
  }

  .chart-title {
    margin: 0;
    font-size: 14px;
    color: #ff8c00;
    text-align: left;
  }
</style>
