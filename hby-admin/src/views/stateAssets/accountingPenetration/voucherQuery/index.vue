<template>
  <div class="app-container accounting-page">
    <div class="page-header" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="page-header-left"><i class="el-icon-tickets"></i><span>会计凭证查询</span></div>
      <div class="page-header-desc">穿透查询原始凭证与借贷方异常标记</div>
    </div>
    <el-card shadow="never" class="search-card">
      <div slot="header"><span>查询条件</span></div>
      <el-form :model="queryForm" ref="queryForm" :inline="true" size="small">
        <el-form-item label="凭证号"><el-input v-model="queryForm.voucherNo" placeholder="请输入" clearable style="width: 150px" /></el-form-item>
        <el-form-item label="企业名称"><el-input v-model="queryForm.companyName" placeholder="请输入" clearable style="width: 180px" /></el-form-item>
        <el-form-item label="异常标记">
          <el-select v-model="queryForm.anomalyFlag" placeholder="请选择" clearable style="width: 120px">
            <el-option label="正常" value="N" /><el-option label="异常" value="Y" />
          </el-select>
        </el-form-item>
        <el-form-item><el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button><el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button></el-form-item>
      </el-form>
    </el-card>
    <el-card shadow="never" style="margin-top: 10px">
      <el-table v-loading="loading" :data="list" border style="width: 100%">
        <el-table-column label="凭证号" prop="voucherNo" width="120" />
        <el-table-column label="企业名称" prop="companyName" min-width="140" show-overflow-tooltip />
        <el-table-column label="摘要" prop="summary" min-width="180" show-overflow-tooltip />
        <el-table-column label="借方金额" prop="debitAmount" width="120" align="right" />
        <el-table-column label="贷方金额" prop="creditAmount" width="120" align="right" />
        <el-table-column label="异常标记" prop="anomalyFlag" width="80" align="center">
          <template slot-scope="scope"><el-tag v-if="scope.row.anomalyFlag === 'Y'" type="danger" size="small">异常</el-tag><span v-else>正常</span></template>
        </el-table-column>
        <el-table-column label="凭证日期" prop="voucherDate" width="120" align="center" />
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
  name: 'AccountingVoucherQuery',
  data() { return { loading: false, list: [], total: 0, queryForm: { pageNumber: 1, pageSize: 10, voucherNo: '', companyName: '', anomalyFlag: '' } } },
  created() { this.fetchData() },
  methods: {
    async fetchData() { this.loading = true; try { const res = await getAccountingPolicyList(this.queryForm); if (res && res.result === 200) { this.list = (res.data && res.data.tlist) || []; this.total = (res.data && res.data.totalRecord) || 0 } } catch (e) { this.list = [] } finally { this.loading = false } },
    handleQuery() { this.queryForm.pageNumber = 1; this.fetchData() },
    resetQuery() { this.$refs.queryForm.resetFields(); this.queryForm.pageNumber = 1; this.fetchData() },
  },
}
</script>
<style lang="scss" scoped>
.accounting-page { padding: 16px; background: #f0f2f5; min-height: calc(100vh - 84px); }
.page-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 14px; padding: 14px 20px; border-radius: 6px; color: #fff; }
.page-header-left { display: flex; align-items: center; font-size: 16px; font-weight: 600; i { font-size: 22px; margin-right: 10px; } }
.page-header-desc { font-size: 13px; opacity: 0.85; }
.search-card { border-left: 3px solid #13c2c2; }
::v-deep .el-table th { background: #e6fffb; }
::v-deep .el-card { border-radius: 6px; }
</style>