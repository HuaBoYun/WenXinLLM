<template>
  <div class="app-container accounting-page">
    <div class="page-header" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="page-header-left"><i class="el-icon-edit-outline"></i><span>会计估计分析</span></div>
      <div class="page-header-desc">分析会计估计变更的合理性与影响金额</div>
    </div>
    <el-card shadow="never">
      <el-table v-loading="loading" :data="list" border style="width: 100%">
        <el-table-column label="企业名称" prop="companyName" min-width="160" show-overflow-tooltip />
        <el-table-column label="估计项目" prop="estimateItem" min-width="140" show-overflow-tooltip />
        <el-table-column label="变更前" prop="beforeValue" width="120" align="right" />
        <el-table-column label="变更后" prop="afterValue" width="120" align="right" />
        <el-table-column label="影响金额(万元)" prop="impactAmount" width="130" align="right">
          <template slot-scope="scope"><span :style="{ color: scope.row.impactAmount < 0 ? '#F56C6C' : '#67C23A' }">{{ scope.row.impactAmount }}</span></template>
        </el-table-column>
        <el-table-column label="合理性评估" prop="reasonableness" width="100" align="center">
          <template slot-scope="scope"><el-tag :type="scope.row.reasonableness === 'REASONABLE' ? 'success' : 'warning'" size="small">{{ scope.row.reasonableness === 'REASONABLE' ? '合理' : '待评估' }}</el-tag></template>
        </el-table-column>
        <el-table-column label="变更日期" prop="changeDate" width="120" align="center" />
      </el-table>
      <el-pagination background style="margin-top: 15px; text-align: right" :current-page="queryForm.pageNumber" :page-sizes="[10, 20, 50]" :page-size="queryForm.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total" @size-change="val => { queryForm.pageSize = val; fetchData() }" @current-change="val => { queryForm.pageNumber = val; fetchData() }" />
    </el-card>
  </div>
</template>
<script>
import { getAccountingPolicyList } from '@/api/stateAssets/accountingPenetration'
import { mapGetters } from 'vuex'
export default {
  computed: {
    ...mapGetters({ theme: 'settings/theme' }),
    themeColor() {
      const map = { red: '#e50113', green: '#41b584', ocean: '#1890ff', white: '#1890ff', default: '#1890ff' }
      return map[(this.theme && this.theme.themeName) || 'default'] || '#1890ff'
    },
    themeColorLight() {
      const map = { red: '#fff1f0', green: '#f6ffed', ocean: '#e6f7ff', white: '#e6f7ff', default: '#e6f7ff' }
      return map[(this.theme && this.theme.themeName) || 'default'] || '#e6f7ff'
    },
  },
  name: 'AccountingEstimateAnalysis',
  data() { return { loading: false, list: [], total: 0, queryForm: { pageNumber: 1, pageSize: 10 } } },
  created() { this.fetchData() },
  methods: {
    async fetchData() { this.loading = true; try { const res = await getAccountingPolicyList(this.queryForm); if (res && res.result === 200) { this.list = (res.data && res.data.tlist) || []; this.total = (res.data && res.data.totalRecord) || 0 } } catch (e) { this.list = [] } finally { this.loading = false } },
  },
}
</script>
<style lang="scss" scoped>
.accounting-page { padding: 16px; background: #f0f2f5; min-height: calc(100vh - 84px); }
.page-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 14px; padding: 14px 20px; border-radius: 6px; color: #fff; }
.page-header-left { display: flex; align-items: center; font-size: 16px; font-weight: 600; i { font-size: 22px; margin-right: 10px; } }
.page-header-desc { font-size: 13px; opacity: 0.85; }
::v-deep .el-table th { background: #e6fffb; }
::v-deep .el-card { border-radius: 6px; }
</style>