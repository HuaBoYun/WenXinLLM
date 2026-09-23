<template>
  <div class="app-container accounting-page">
    <div class="page-header" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="page-header-left"><i class="el-icon-document-checked"></i><span>审计意见监控</span></div>
      <div class="page-header-desc">追踪审计意见类型与整改落实情况</div>
    </div>
    <el-card shadow="never" class="search-card">
      <el-form :model="queryForm" ref="queryForm" :inline="true" size="small">
        <el-form-item label="企业名称"><el-input v-model="queryForm.companyName" placeholder="请输入" clearable style="width: 180px" /></el-form-item>
        <el-form-item label="意见类型">
          <el-select v-model="queryForm.opinionType" placeholder="请选择" clearable style="width: 150px">
            <el-option label="标准无保留" value="UNQUALIFIED" /><el-option label="保留意见" value="QUALIFIED" /><el-option label="否定意见" value="ADVERSE" /><el-option label="无法表示" value="DISCLAIMER" />
          </el-select>
        </el-form-item>
        <el-form-item><el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button><el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button></el-form-item>
      </el-form>
    </el-card>
    <el-card shadow="never" style="margin-top: 10px">
      <el-table v-loading="loading" :data="list" border style="width: 100%">
        <el-table-column label="企业名称" prop="companyName" min-width="160" show-overflow-tooltip />
        <el-table-column label="审计年度" prop="auditYear" width="100" align="center" />
        <el-table-column label="审计机构" prop="auditFirm" min-width="140" show-overflow-tooltip />
        <el-table-column label="意见类型" prop="opinionType" width="120" align="center">
          <template slot-scope="scope"><el-tag :type="{ UNQUALIFIED: 'success', QUALIFIED: 'warning', ADVERSE: 'danger', DISCLAIMER: 'danger' }[scope.row.opinionType]" size="small">{{ { UNQUALIFIED: '标准无保留', QUALIFIED: '保留意见', ADVERSE: '否定意见', DISCLAIMER: '无法表示' }[scope.row.opinionType] || scope.row.opinionType }}</el-tag></template>
        </el-table-column>
        <el-table-column label="整改状态" prop="rectificationStatus" width="100" align="center" />
        <el-table-column label="操作" width="120" align="center" fixed="right">
          <template slot-scope="scope"><el-button size="mini" type="text" @click="handleView(scope.row)">查看</el-button></template>
        </el-table-column>
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
  name: 'AccountingAuditOpinion',
  data() { return { loading: false, list: [], total: 0, queryForm: { pageNumber: 1, pageSize: 10, companyName: '', opinionType: '' } } },
  created() { this.fetchData() },
  methods: {
    async fetchData() { this.loading = true; try { const res = await getAccountingPolicyList(this.queryForm); if (res && res.result === 200) { this.list = (res.data && res.data.tlist) || []; this.total = (res.data && res.data.totalRecord) || 0 } } catch (e) { this.list = [] } finally { this.loading = false } },
    handleQuery() { this.queryForm.pageNumber = 1; this.fetchData() },
    resetQuery() { this.$refs.queryForm.resetFields(); this.queryForm.pageNumber = 1; this.fetchData() },
    handleView(row) { this.$message.info('查看审计意见详情') },
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