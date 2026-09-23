<template>
  <div class="app-container finance-page">
    <div class="page-header" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="page-header-left"><i class="el-icon-s-data"></i><span>财务报表管理</span></div>
      <div class="page-header-desc">管理企业财务报表数据与审核流程</div>
    </div>
    <el-card class="search-card" shadow="never">
      <el-form :model="queryForm" ref="queryForm" :inline="true" size="small">
        <el-form-item label="企业名称" prop="companyName">
          <el-input v-model="queryForm.companyName" placeholder="请输入" clearable style="width:160px" />
        </el-form-item>
        <el-form-item label="报表类型" prop="statementType">
          <el-select v-model="queryForm.statementType" placeholder="请选择" clearable style="width: 150px">
            <el-option label="资产负债表" value="BALANCE_SHEET" />
            <el-option label="利润表" value="INCOME_STATEMENT" />
            <el-option label="现金流量表" value="CASH_FLOW" />
          </el-select>
        </el-form-item>
        <el-form-item label="报告期" prop="reportPeriod">
          <el-input v-model="queryForm.reportPeriod" placeholder="如：2026-Q1" clearable style="width: 140px" />
        </el-form-item>
        <el-form-item label="审核状态" prop="auditStatus">
          <el-select v-model="queryForm.auditStatus" placeholder="请选择" clearable style="width: 120px">
            <el-option label="草稿" value="DRAFT" />
            <el-option label="已提交" value="SUBMITTED" />
            <el-option label="已审核" value="AUDITED" />
          </el-select>
        </el-form-item>
        <el-form-item label="创建时间" prop="timeRange">
          <el-date-picker
            v-model="queryForm.timeRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="yyyy-MM-dd"
            style="width: 260px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card shadow="never" style="margin-top: 10px">
      <div style="margin-bottom: 10px">
        <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAdd">新增</el-button>
        <el-button type="danger" size="small" icon="el-icon-delete" :disabled="multipleSelection.length === 0" @click="handleBatchDelete">批量删除</el-button>
      </div>
      <el-table v-loading="loading" :data="list" border @selection-change="handleSelectionChange" style="width: 100%">
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column label="企业名称" prop="companyName" min-width="160" show-overflow-tooltip />
        <el-table-column label="报表类型" prop="statementType" width="120" align="center">
          <template slot-scope="scope">
            {{ statementTypeMap[scope.row.statementType] || scope.row.statementType }}
          </template>
        </el-table-column>
        <el-table-column label="报告期" prop="period" width="100" align="center" />
        <el-table-column label="总资产(万元)" prop="totalAssets" width="130" align="right">
          <template slot-scope="scope">
            {{ scope.row.totalAssets != null ? Number(scope.row.totalAssets).toLocaleString() : '-' }}
          </template>
        </el-table-column>
        <el-table-column label="净利润(万元)" prop="netProfit" width="130" align="right">
          <template slot-scope="scope">
            {{ scope.row.netProfit != null ? Number(scope.row.netProfit).toLocaleString() : '-' }}
          </template>
        </el-table-column>
        <el-table-column label="营业收入(万元)" prop="revenue" width="140" align="right">
          <template slot-scope="scope">
            {{ scope.row.revenue != null ? Number(scope.row.revenue).toLocaleString() : '-' }}
          </template>
        </el-table-column>
        <el-table-column label="审核状态" prop="auditStatus" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="auditStatusTagType(scope.row.auditStatus)" size="small">
              {{ auditStatusMap[scope.row.auditStatus] || scope.row.auditStatus }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="创建时间" prop="createTime" width="160" align="center" show-overflow-tooltip />
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="text" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="text" style="color: #F56C6C" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination
        background
        style="margin-top: 15px; text-align: right"
        :current-page="queryForm.pageNumber"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="queryForm.pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </el-card>
    <!-- 新增/编辑/查看 弹窗 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="750px" :close-on-click-modal="false" @closed="handleDialogClosed">
      <el-form :model="form" :rules="dialogType !== 'view' ? rules : {}" ref="form" label-width="130px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="企业名称" prop="companyName">
              <el-input v-model="form.companyName" :disabled="dialogType === 'view'" placeholder="请输入企业名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="报表类型" prop="statementType">
              <el-select v-model="form.statementType" :disabled="dialogType === 'view'" style="width: 100%" placeholder="请选择">
                <el-option label="资产负债表" value="BALANCE_SHEET" />
                <el-option label="利润表" value="INCOME_STATEMENT" />
                <el-option label="现金流量表" value="CASH_FLOW" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="报告期" prop="period">
              <el-input v-model="form.period" placeholder="如：2026-Q1" :disabled="dialogType === 'view'" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="审核状态" prop="auditStatus">
              <el-select v-model="form.auditStatus" :disabled="dialogType === 'view'" style="width: 100%" placeholder="请选择">
                <el-option label="草稿" value="DRAFT" />
                <el-option label="已提交" value="SUBMITTED" />
                <el-option label="已审核" value="AUDITED" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="总资产(万元)">
              <el-input-number v-model="form.totalAssets" :min="0" :precision="2" :controls="false" style="width: 100%" :disabled="dialogType === 'view'" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="总负债(万元)">
              <el-input-number v-model="form.totalLiabilities" :min="0" :precision="2" :controls="false" style="width: 100%" :disabled="dialogType === 'view'" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="净资产(万元)">
              <el-input-number v-model="form.netAssets" :precision="2" :controls="false" style="width: 100%" :disabled="dialogType === 'view'" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="营业收入(万元)">
              <el-input-number v-model="form.revenue" :precision="2" :controls="false" style="width: 100%" :disabled="dialogType === 'view'" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="净利润(万元)">
              <el-input-number v-model="form.netProfit" :precision="2" :controls="false" style="width: 100%" :disabled="dialogType === 'view'" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="经营现金流(万元)">
              <el-input-number v-model="form.operatingCashflow" :precision="2" :controls="false" style="width: 100%" :disabled="dialogType === 'view'" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" v-if="dialogType !== 'view'">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确 定</el-button>
      </div>
      <div slot="footer" v-else>
        <el-button @click="dialogVisible = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { getFinanceStatementList, addFinanceStatement, updateFinanceStatement, deleteFinanceStatement, batchDeleteFinanceStatement } from '@/api/stateAssets/financePenetration'
import { mapGetters } from 'vuex'
export default {
  name: 'FinancePenetrationReport',
  data() {
    return {
      loading: false,
      submitLoading: false,
      list: [],
      total: 0,
      multipleSelection: [],
      queryForm: {
        pageNumber: 1,
        pageSize: 10,
        companyName: '',
        statementType: '',
        reportPeriod: '',
        auditStatus: '',
        timeRange: null,
      },
      dialogVisible: false,
      dialogType: 'add',
      form: {},
      rules: {
        companyName: [{ required: true, message: '请输入企业名称', trigger: 'blur' }],
        statementType: [{ required: true, message: '请选择报表类型', trigger: 'change' }],
        period: [{ required: true, message: '请输入报告期', trigger: 'blur' }],
      },
      statementTypeMap: {
        BALANCE_SHEET: '资产负债表',
        INCOME_STATEMENT: '利润表',
        CASH_FLOW: '现金流量表',
      },
      auditStatusMap: {
        DRAFT: '草稿',
        SUBMITTED: '已提交',
        AUDITED: '已审核',
        APPROVED: '已审核',
      },
    }
  },
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
    dialogTitle() {
      const map = { add: '新增财务报表', edit: '编辑财务报表', view: '查看财务报表' }
      return map[this.dialogType] || ''
    },
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
          statementType: this.queryForm.statementType,
          periodKeyword: this.queryForm.reportPeriod,
          auditStatus: this.queryForm.auditStatus,
        }
        if (this.queryForm.timeRange && this.queryForm.timeRange.length === 2) {
          params.startTime = this.queryForm.timeRange[0]
          params.endTime = this.queryForm.timeRange[1]
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
        statementType: '',
        reportPeriod: '',
        auditStatus: '',
        timeRange: null,
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
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    handleAdd() {
      this.dialogType = 'add'
      this.form = {
        companyName: '',
        statementType: '',
        period: '',
        auditStatus: 'DRAFT',
        totalAssets: null,
        totalLiabilities: null,
        netAssets: null,
        revenue: null,
        netProfit: null,
        operatingCashflow: null,
      }
      this.dialogVisible = true
    },
    handleView(row) {
      this.dialogType = 'view'
      this.form = {
        statementId: row.statementId,
        companyName: row.companyName,
        statementType: row.statementType,
        period: row.period,
        auditStatus: row.auditStatus,
        totalAssets: row.totalAssets != null ? Number(row.totalAssets) : null,
        totalLiabilities: row.totalLiabilities != null ? Number(row.totalLiabilities) : null,
        netAssets: row.netAssets != null ? Number(row.netAssets) : null,
        revenue: row.revenue != null ? Number(row.revenue) : null,
        netProfit: row.netProfit != null ? Number(row.netProfit) : null,
        operatingCashflow: row.operatingCashflow != null ? Number(row.operatingCashflow) : null,
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogType = 'edit'
      this.form = {
        statementId: row.statementId,
        companyName: row.companyName,
        statementType: row.statementType,
        period: row.period,
        auditStatus: row.auditStatus,
        totalAssets: row.totalAssets != null ? Number(row.totalAssets) : null,
        totalLiabilities: row.totalLiabilities != null ? Number(row.totalLiabilities) : null,
        netAssets: row.netAssets != null ? Number(row.netAssets) : null,
        revenue: row.revenue != null ? Number(row.revenue) : null,
        netProfit: row.netProfit != null ? Number(row.netProfit) : null,
        operatingCashflow: row.operatingCashflow != null ? Number(row.operatingCashflow) : null,
      }
      this.dialogVisible = true
    },
    handleSubmit() {
      this.$refs.form.validate(async (valid) => {
        if (!valid) return
        this.submitLoading = true
        try {
          const submitData = { ...this.form }
          let res
          if (this.dialogType === 'add') {
            res = await addFinanceStatement(submitData)
          } else {
            res = await updateFinanceStatement(submitData)
          }
          if (res && res.result === 200) {
            this.$message.success(this.dialogType === 'add' ? '新增成功' : '更新成功')
            this.dialogVisible = false
            this.fetchData()
          } else {
            this.$message.error(res.msg || '操作失败')
          }
        } catch (e) {
          this.$message.error('操作失败')
        } finally {
          this.submitLoading = false
        }
      })
    },
    handleDelete(row) {
      this.$confirm('确认删除该条财务报表记录？删除后不可恢复。', '提示', { type: 'warning' }).then(async () => {
        try {
          const res = await deleteFinanceStatement(row.statementId)
          if (res && res.result === 200) {
            this.$message.success('删除成功')
            this.fetchData()
          } else {
            this.$message.error(res.msg || '删除失败')
          }
        } catch (e) {
          this.$message.error('删除失败')
        }
      }).catch(() => {})
    },
    handleBatchDelete() {
      const ids = this.multipleSelection.map(i => i.statementId)
      this.$confirm(`确认删除选中的 ${ids.length} 条记录？删除后不可恢复。`, '提示', { type: 'warning' }).then(async () => {
        try {
          const res = await batchDeleteFinanceStatement(ids)
          if (res && res.result === 200) {
            this.$message.success('批量删除成功')
            this.fetchData()
          } else {
            this.$message.error(res.msg || '批量删除失败')
          }
        } catch (e) {
          this.$message.error('批量删除失败')
        }
      }).catch(() => {})
    },
    handleDialogClosed() {
      if (this.$refs.form) {
        this.$refs.form.resetFields()
      }
    },
    auditStatusTagType(status) {
      const map = { AUDITED: 'success', APPROVED: 'success', SUBMITTED: '', DRAFT: 'warning' }
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
.search-card { margin-bottom: 0; border-left: 3px solid #722ed1; }
.search-card .el-form-item { margin-bottom: 0; }
::v-deep .el-table th { background: #f9f0ff; }
::v-deep .el-card { border-radius: 6px; }
</style>