<template>
  <div class="fin-page">
    <div class="page-header" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <i class="el-icon-money" />
      <span>融资记录台账</span>
    </div>
    <div class="stat-cards">
      <div class="stat-card">
        <div class="stat-label">融资总额(万元)</div>
        <div class="stat-value">{{ statistics.totalAmount || 0 }}</div>
      </div>
      <div class="stat-card">
        <div class="stat-label">融资笔数</div>
        <div class="stat-value">{{ statistics.totalCount || 0 }}</div>
      </div>
      <div class="stat-card warning">
        <div class="stat-label">高风险融资</div>
        <div class="stat-value">{{ statistics.highRiskCount || 0 }}</div>
      </div>
      <div class="stat-card danger">
        <div class="stat-label">即将到期</div>
        <div class="stat-value">{{ statistics.expiringCount || 0 }}</div>
      </div>
    </div>
    <el-form :model="queryForm" inline class="query-form">
      <el-form-item label="企业名称">
        <el-input v-model="queryForm.enterpriseName" placeholder="请输入企业名称" clearable />
      </el-form-item>
      <el-form-item label="融资类型">
        <el-select v-model="queryForm.financingType" placeholder="请选择" clearable>
          <el-option label="银行贷款" value="银行贷款" />
          <el-option label="债券融资" value="债券融资" />
          <el-option label="股权融资" value="股权融资" />
          <el-option label="信托融资" value="信托融资" />
        </el-select>
      </el-form-item>
      <el-form-item label="风险等级">
        <el-select v-model="queryForm.riskLevel" placeholder="请选择" clearable>
          <el-option label="低风险" value="低风险" />
          <el-option label="中风险" value="中风险" />
          <el-option label="高风险" value="高风险" />
        </el-select>
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="queryForm.status" placeholder="请选择" clearable>
          <el-option label="正常" value="正常" />
          <el-option label="即将到期" value="即将到期" />
          <el-option label="已逾期" value="已逾期" />
          <el-option label="已结清" value="已结清" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        <el-button type="success" icon="el-icon-download" @click="handleExport">导出</el-button>
      </el-form-item>
    </el-form>
    <el-table v-loading="loading" :data="tableData" border stripe>
      <el-table-column prop="enterpriseName" label="企业名称" min-width="150" show-overflow-tooltip />
      <el-table-column prop="financingType" label="融资类型" width="110" />
      <el-table-column prop="amount" label="融资金额(万元)" width="130" align="right" />
      <el-table-column prop="interestRate" label="利率(%)" width="100" align="right" />
      <el-table-column prop="startDate" label="起始日期" width="120" />
      <el-table-column prop="endDate" label="到期日期" width="120" />
      <el-table-column prop="riskLevel" label="风险等级" width="100">
        <template slot-scope="{ row }">
          <el-tag :type="riskTagType(row.riskLevel)" size="small">{{ row.riskLevel }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template slot-scope="{ row }">
          <el-tag :type="statusTagType(row.status)" size="small">{{ row.status }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="120" fixed="right">
        <template slot-scope="{ row }">
          <el-button type="text" size="small" @click="handleDetail(row)">详情</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      :current-page="queryForm.pageNum"
      :page-size="queryForm.pageSize"
      :total="total"
      layout="total, sizes, prev, pager, next, jumper"
      :page-sizes="[10, 20, 50]"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>
</template>

<script>
import { getFinancingRecordList, getFinancialRiskStatistics } from '@/api/stateAssets/financialRiskPenetration'
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
  name: 'Rzjltz',
  data() {
    return {
      loading: false,
      tableData: [],
      total: 0,
      statistics: {},
      queryForm: { enterpriseName: '', financingType: '', riskLevel: '', status: '', pageNum: 1, pageSize: 10 },
    }
  },
  created() {
    this.fetchData()
    this.fetchStatistics()
  },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const res = await getFinancingRecordList(this.queryForm)
        if (res.code === 1) {
          this.tableData = res.data.list || []
          this.total = res.data.total || 0
        }
      } finally {
        this.loading = false
      }
    },
    async fetchStatistics() {
      const res = await getFinancialRiskStatistics({ type: 'financing' })
      if (res.code === 1) this.statistics = res.data || {}
    },
    handleQuery() { this.queryForm.pageNum = 1; this.fetchData() },
    resetQuery() {
      this.queryForm = { enterpriseName: '', financingType: '', riskLevel: '', status: '', pageNum: 1, pageSize: 10 }
      this.fetchData()
    },
    handleSizeChange(val) { this.queryForm.pageSize = val; this.handleQuery() },
    handleCurrentChange(val) { this.queryForm.pageNum = val; this.fetchData() },
    handleExport() { this.$message.info('导出功能开发中') },
    handleDetail(row) { this.$message.info('查看详情: ' + row.enterpriseName) },
    riskTagType(level) { return { '高风险': 'danger', '中风险': 'warning', '低风险': 'success' }[level] || 'info' },
    statusTagType(status) { return { '已逾期': 'danger', '即将到期': 'warning', '正常': 'success', '已结清': 'info' }[status] || 'info' },
  },
}
</script>

<style lang="scss" scoped>
.fin-page { padding: 20px; }
.page-header { font-size: 18px; font-weight: bold; margin-bottom: 20px; i { margin-right: 8px; color: #409eff; } }
.stat-cards { display: flex; gap: 16px; margin-bottom: 20px; }
.stat-card { flex: 1; padding: 16px; background: #f0f9ff; border-radius: 8px; text-align: center;
  .stat-label { font-size: 13px; color: #666; margin-bottom: 8px; }
  .stat-value { font-size: 24px; font-weight: bold; color: #409eff; }
  &.warning .stat-value { color: #e6a23c; }
  &.danger .stat-value { color: #f56c6c; }
}
.query-form { margin-bottom: 16px; }
.el-pagination { margin-top: 16px; text-align: right; }
</style>
