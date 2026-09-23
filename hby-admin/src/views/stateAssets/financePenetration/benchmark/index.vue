<template>
  <div class="app-container finance-page">
    <div class="page-header" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="page-header-left"><i class="el-icon-s-marketing"></i><span>财务指标对标分析</span></div>
      <div class="page-header-desc">对标行业标杆分析企业财务健康度</div>
    </div>
    <el-card shadow="never" class="search-card">
      <el-form :model="queryForm" ref="queryForm" :inline="true" size="small">
        <el-form-item label="企业名称" prop="companyName">
          <el-input v-model="queryForm.companyName" placeholder="请输入" clearable style="width: 160px" />
        </el-form-item>
        <el-form-item label="报告期" prop="reportPeriod">
          <el-input v-model="queryForm.reportPeriod" placeholder="如 2026-Q1" clearable style="width: 130px" />
        </el-form-item>
        <el-form-item label="报表类型" prop="statementType">
          <el-select v-model="queryForm.statementType" placeholder="请选择" clearable style="width: 130px">
            <el-option label="资产负债表" value="BALANCE_SHEET" />
            <el-option label="利润表" value="INCOME_STATEMENT" />
            <el-option label="现金流量表" value="CASH_FLOW" />
          </el-select>
        </el-form-item>
        <el-form-item label="审计状态" prop="auditStatus">
          <el-select v-model="queryForm.auditStatus" placeholder="请选择" clearable style="width: 120px">
            <el-option label="草稿" value="DRAFT" />
            <el-option label="已提交" value="SUBMITTED" />
            <el-option label="已审核" value="AUDITED" />
          </el-select>
        </el-form-item>
        <el-form-item label="所属行业" prop="industry">
          <el-input v-model="queryForm.industry" placeholder="请输入" clearable style="width: 130px" />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card shadow="never" style="margin-top: 10px">
      <el-table v-loading="loading" :data="list" border style="width: 100%">
        <el-table-column label="企业名称" prop="companyName" min-width="160" show-overflow-tooltip />
        <el-table-column label="报告期" prop="period" width="100" align="center" />
        <el-table-column label="报表类型" prop="statementType" width="110" align="center">
          <template slot-scope="scope">
            {{ statementTypeMap[scope.row.statementType] || scope.row.statementType }}
          </template>
        </el-table-column>
        <el-table-column label="总资产(万元)" prop="totalAssets" width="130" align="right">
          <template slot-scope="scope">{{ formatNum(scope.row.totalAssets) }}</template>
        </el-table-column>
        <el-table-column label="总负债(万元)" prop="totalLiabilities" width="130" align="right">
          <template slot-scope="scope">{{ formatNum(scope.row.totalLiabilities) }}</template>
        </el-table-column>
        <el-table-column label="净资产(万元)" prop="netAssets" width="130" align="right">
          <template slot-scope="scope">{{ formatNum(scope.row.netAssets) }}</template>
        </el-table-column>
        <el-table-column label="营业收入(万元)" prop="revenue" width="130" align="right">
          <template slot-scope="scope">{{ formatNum(scope.row.revenue) }}</template>
        </el-table-column>
        <el-table-column label="净利润(万元)" prop="netProfit" width="120" align="right">
          <template slot-scope="scope">{{ formatNum(scope.row.netProfit) }}</template>
        </el-table-column>
        <el-table-column label="资产负债率(%)" width="120" align="right">
          <template slot-scope="scope">
            <span :style="{ color: calcDebtRatio(scope.row) > 70 ? '#F56C6C' : '#303133' }">{{ calcDebtRatio(scope.row) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="净利润率(%)" width="110" align="right">
          <template slot-scope="scope">{{ calcProfitMargin(scope.row) }}</template>
        </el-table-column>
        <el-table-column label="审计状态" prop="auditStatus" width="90" align="center">
          <template slot-scope="scope">
            <el-tag :type="auditStatusTagType(scope.row.auditStatus)" size="small">
              {{ auditStatusMap[scope.row.auditStatus] || scope.row.auditStatus }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        background
        style="margin-top: 15px; text-align: right"
        :current-page="queryForm.pageNumber"
        :page-sizes="[10, 20, 50]"
        :page-size="queryForm.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>
  </div>
</template>
<script>
import { getFinanceStatementList } from '@/api/stateAssets/financePenetration'
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
  name: 'FinanceBenchmark',
  data() {
    return {
      loading: false,
      list: [],
      total: 0,
      queryForm: {
        pageNumber: 1,
        pageSize: 10,
        companyName: '',
        reportPeriod: '',
        statementType: '',
        auditStatus: '',
        industry: '',
      },
      /** 报表类型中文映射 */
      statementTypeMap: {
        BALANCE_SHEET: '资产负债表',
        INCOME_STATEMENT: '利润表',
        CASH_FLOW: '现金流量表',
        EQUITY_CHANGE: '权益变动表',
        CONSOLIDATED: '合并报表',
        NOTES: '报表附注',
      },
      /** 审计状态中文映射 */
      auditStatusMap: {
        DRAFT: '草稿',
        SUBMITTED: '已提交',
        AUDITED: '已审核',
        APPROVED: '已审核',
        NOT_AUDITED: '未审计',
        IN_PROGRESS: '审计中',
        COMPLETED: '审计完成',
        QUALIFIED: '审计合格',
        UNQUALIFIED: '审计不合格',
      },
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const params = {
          pageNumber: this.queryForm.pageNumber,
          pageSize: this.queryForm.pageSize,
          companyName: this.queryForm.companyName,
          periodKeyword: this.queryForm.reportPeriod,
          statementType: this.queryForm.statementType,
          auditStatus: this.queryForm.auditStatus,
          industry: this.queryForm.industry,
        }
        const res = await getFinanceStatementList(params)
        if (res && res.result === 200) {
          this.list = (res.data && res.data.tlist) || []
          this.total = (res.data && res.data.totalRecord) || 0
        } else {
          this.list = []
          this.total = 0
        }
      } catch (e) {
        this.list = []
        this.total = 0
      } finally {
        this.loading = false
      }
    },
    handleQuery() {
      this.queryForm.pageNumber = 1
      this.fetchData()
    },
    resetQuery() {
      this.queryForm = {
        pageNumber: 1,
        pageSize: this.queryForm.pageSize,
        companyName: '',
        reportPeriod: '',
        statementType: '',
        auditStatus: '',
        industry: '',
      }
      this.fetchData()
    },
    handleSizeChange(val) {
      this.queryForm.pageSize = val
      this.fetchData()
    },
    handleCurrentChange(val) {
      this.queryForm.pageNumber = val
      this.fetchData()
    },
    formatNum(val) {
      return val != null ? Number(val).toLocaleString() : '-'
    },
    calcDebtRatio(row) {
      if (row.totalAssets && row.totalLiabilities && Number(row.totalAssets) !== 0) {
        return (Number(row.totalLiabilities) / Number(row.totalAssets) * 100).toFixed(2)
      }
      return '-'
    },
    calcProfitMargin(row) {
      if (row.revenue && row.netProfit && Number(row.revenue) !== 0) {
        return (Number(row.netProfit) / Number(row.revenue) * 100).toFixed(2)
      }
      return '-'
    },
    auditStatusTagType(status) {
      const map = { AUDITED: 'success', APPROVED: 'success', COMPLETED: 'success', QUALIFIED: 'success', SUBMITTED: '', IN_PROGRESS: '', DRAFT: 'warning', NOT_AUDITED: 'info', UNQUALIFIED: 'danger' }
      return map[status] || 'info'
    },
  },
}
</script>
<style lang="scss" scoped>
.finance-page { padding: 16px; background: #f0f2f5; min-height: calc(100vh - 84px); }
.page-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 14px; padding: 14px 20px; border-radius: 6px; color: #fff; }
.page-header-left { display: flex; align-items: center; font-size: 16px; font-weight: 600; i { font-size: 22px; margin-right: 10px; } }
.page-header-desc { font-size: 13px; opacity: 0.85; }
.search-card { border-left: 3px solid #722ed1; }
.search-card .el-form-item { margin-bottom: 0; }
::v-deep .el-table th { background: #f9f0ff; }
::v-deep .el-card { border-radius: 6px; }
</style>