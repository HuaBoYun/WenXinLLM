<template>
  <div class="app-container procurement-page" :style="themeVars">
    <div class="page-header">
      <div class="page-header-left"><i class="el-icon-shopping-cart-2"></i><span>采购项目管理</span></div>
      <div class="page-header-desc">管理采购项目立项、审批流程与执行进度</div>
    </div>
    <el-card class="search-card" shadow="never">
      <el-form :model="queryForm" ref="queryForm" :inline="true" size="small">
        <el-form-item label="项目名称" prop="projectName">
          <el-input v-model="queryForm.projectName" placeholder="请输入" clearable style="width: 180px" />
        </el-form-item>
        <el-form-item label="采购方式" prop="procurementMethod">
          <el-select v-model="queryForm.procurementMethod" placeholder="请选择" clearable style="width: 150px">
            <el-option label="公开招标" value="PUBLIC_BIDDING" />
            <el-option label="邀请招标" value="INVITED_BIDDING" />
            <el-option label="竞争性谈判" value="NEGOTIATION" />
            <el-option label="单一来源" value="SOLE_SOURCE" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
          <el-button type="success" icon="el-icon-download" @click="handleExport">导出</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card shadow="never" style="margin-top: 10px">
      <div style="margin-bottom: 10px">
        <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAdd">新增</el-button>
        <el-button type="danger" size="small" icon="el-icon-delete" :disabled="multipleSelection.length === 0" @click="handleBatchDelete">批量删除</el-button>
      </div>
      <el-table v-loading="loading" :data="list" border @selection-change="val => multipleSelection = val" style="width: 100%">
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column label="项目名称" prop="projectName" min-width="160" show-overflow-tooltip />
        <el-table-column label="采购企业" prop="companyName" min-width="140" show-overflow-tooltip />
        <el-table-column label="采购方式" prop="procurementMethod" width="110" align="center">
          <template slot-scope="scope">
            {{ { PUBLIC_BIDDING: '公开招标', INVITED_BIDDING: '邀请招标', NEGOTIATION: '竞争性谈判', SOLE_SOURCE: '单一来源' }[scope.row.procurementMethod] || scope.row.procurementMethod }}
          </template>
        </el-table-column>
        <el-table-column label="预算金额(万元)" prop="budgetAmount" width="130" align="right" />
        <el-table-column label="中标金额(万元)" prop="winningAmount" width="130" align="right" />
        <el-table-column label="中标供应商" prop="winningSupplier" min-width="140" show-overflow-tooltip />
        <el-table-column label="项目状态" prop="projectStatus" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="{ PLANNING: 'info', BIDDING: 'warning', EXECUTING: '', COMPLETED: 'success' }[scope.row.projectStatus]" size="small">
              {{ { PLANNING: '筹备中', BIDDING: '招标中', EXECUTING: '执行中', COMPLETED: '已完成' }[scope.row.projectStatus] || scope.row.projectStatus }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="text" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="text" style="color: #F56C6C" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination background style="margin-top: 15px; text-align: right" :current-page="queryForm.pageNumber" :page-sizes="[10, 20, 50, 100]" :page-size="queryForm.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total" @size-change="val => { queryForm.pageSize = val; fetchData() }" @current-change="val => { queryForm.pageNumber = val; fetchData() }" />
    </el-card>
    <!-- 新增/编辑/查看对话框 -->
    <el-dialog :title="{ add: '新增采购项目', edit: '编辑采购项目', view: '查看采购项目' }[dialogType]" :visible.sync="dialogVisible" width="700px" :close-on-click-modal="false">
      <el-form :model="form" :rules="rules" ref="form" label-width="120px">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="项目名称" prop="projectName">
              <el-input v-model="form.projectName" :disabled="dialogType === 'view'" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="采购企业" prop="companyName">
              <el-input v-model="form.companyName" :disabled="dialogType === 'view'" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="采购方式" prop="procurementMethod">
              <el-select v-model="form.procurementMethod" :disabled="dialogType === 'view'" style="width: 100%">
                <el-option label="公开招标" value="PUBLIC_BIDDING" />
                <el-option label="邀请招标" value="INVITED_BIDDING" />
                <el-option label="竞争性谈判" value="NEGOTIATION" />
                <el-option label="单一来源" value="SOLE_SOURCE" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="预算金额(万元)" prop="budgetAmount">
              <el-input-number v-model="form.budgetAmount" :min="0" :precision="2" style="width: 100%" :disabled="dialogType === 'view'" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="中标供应商" prop="winningSupplier">
              <el-input v-model="form.winningSupplier" :disabled="dialogType === 'view'" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="中标金额(万元)" prop="winningAmount">
              <el-input-number v-model="form.winningAmount" :min="0" :precision="2" style="width: 100%" :disabled="dialogType === 'view'" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="项目状态" prop="projectStatus">
              <el-select v-model="form.projectStatus" :disabled="dialogType === 'view'" style="width: 100%">
                <el-option label="筹备中" value="PLANNING" />
                <el-option label="招标中" value="BIDDING" />
                <el-option label="执行中" value="EXECUTING" />
                <el-option label="已完成" value="COMPLETED" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="开始日期" prop="startDate">
              <el-date-picker v-model="form.startDate" type="date" value-format="yyyy-MM-dd" style="width: 100%" :disabled="dialogType === 'view'" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="结束日期" prop="endDate">
              <el-date-picker v-model="form.endDate" type="date" value-format="yyyy-MM-dd" style="width: 100%" :disabled="dialogType === 'view'" />
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div slot="footer" v-if="dialogType !== 'view'">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { getProcurementProjectList, addProcurementProject, updateProcurementProject, deleteProcurementProject, batchDeleteProcurementProject, exportProcurementReport } from '@/api/stateAssets/procurementPenetration'
import { investThemeMixin } from '../../themeMixin'
export default {
  name: 'ProcurementPenetrationProject',
  mixins: [investThemeMixin],
  data() {
    return {
      loading: false,
      submitLoading: false,
      list: [],
      total: 0,
      multipleSelection: [],
      queryForm: { pageNumber: 1, pageSize: 10, projectName: '', procurementMethod: '' },
      dialogVisible: false,
      dialogType: 'add',
      form: {},
      rules: {
        projectName: [{ required: true, message: '请输入项目名称', trigger: 'blur' }],
        companyName: [{ required: true, message: '请输入采购企业', trigger: 'blur' }],
        procurementMethod: [{ required: true, message: '请选择采购方式', trigger: 'change' }],
      },
    }
  },
  created() { this.fetchData() },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const res = await getProcurementProjectList(this.queryForm)
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
    handleQuery() { this.queryForm.pageNumber = 1; this.fetchData() },
    resetQuery() {
      this.$refs.queryForm.resetFields()
      this.queryForm.pageNumber = 1
      this.fetchData()
    },
    handleAdd() { this.dialogType = 'add'; this.form = { projectStatus: 'PLANNING' }; this.dialogVisible = true },
    handleView(row) { this.dialogType = 'view'; this.form = { ...row }; this.dialogVisible = true },
    handleEdit(row) { this.dialogType = 'edit'; this.form = { ...row }; this.dialogVisible = true },
    async handleSubmit() {
      this.$refs.form.validate(async (valid) => {
        if (!valid) return
        this.submitLoading = true
        try {
          const apiFn = this.dialogType === 'add' ? addProcurementProject : updateProcurementProject
          const res = await apiFn(this.form)
          if (res && res.result === 200) {
            this.$message.success('操作成功')
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
      this.$confirm('确认删除该采购项目？', '提示', { type: 'warning' }).then(async () => {
        try {
          const res = await deleteProcurementProject(row.procurementId || row.id)
          if (res && res.result === 200) { this.$message.success('删除成功'); this.fetchData() } else { this.$message.error(res.msg || '删除失败') }
        } catch (e) { this.$message.error('删除失败') }
      }).catch(() => {})
    },
    handleBatchDelete() {
      const ids = this.multipleSelection.map(i => i.procurementId || i.id)
      this.$confirm(`确认删除选中的 ${ids.length} 条记录？`, '提示', { type: 'warning' }).then(async () => {
        try {
          const res = await batchDeleteProcurementProject({ ids })
          if (res && res.result === 200) { this.$message.success('删除成功'); this.fetchData() } else { this.$message.error(res.msg || '删除失败') }
        } catch (e) { this.$message.error('删除失败') }
      }).catch(() => {})
    },
    async handleExport() {
      try {
        // 获取全部数据用于导出
        const allParams = { ...this.queryForm, pageNumber: 1, pageSize: 99999 }
        const res = await getProcurementProjectList(allParams)
        if (res && res.result === 200 && res.data && res.data.tlist) {
          const allData = res.data.tlist
          const statusMap = { PLANNING: '筹备中', BIDDING: '招标中', EXECUTING: '执行中', COMPLETED: '已完成' }
          const methodMap = { PUBLIC_BIDDING: '公开招标', INVITED_BIDDING: '邀请招标', NEGOTIATION: '竞争性谈判', SOLE_SOURCE: '单一来源' }
          const headers = ['项目名称', '采购企业', '采购方式', '预算金额(万元)', '中标金额(万元)', '中标供应商', '项目状态']
          const rows = allData.map(r => [
            r.projectName || '', r.companyName || '', methodMap[r.procurementMethod] || r.procurementMethod || '',
            r.budgetAmount || '', r.winningAmount || '', r.winningSupplier || '',
            statusMap[r.projectStatus] || r.projectStatus || ''
          ])
          const csvContent = '\uFEFF' + [headers.join(','), ...rows.map(r => r.map(c => '"' + String(c).replace(/"/g, '""') + '"').join(','))].join('\n')
          const blob = new Blob([csvContent], { type: 'text/csv;charset=utf-8;' })
          const url = window.URL.createObjectURL(blob)
          const a = document.createElement('a')
          a.href = url
          a.download = '采购项目列表.csv'
          a.click()
          window.URL.revokeObjectURL(url)
          this.$message.success('导出成功')
        } else {
          this.$message.warning('暂无数据可导出')
        }
      } catch (e) {
        this.$message.error('导出失败')
      }
    },
  },
}
</script>
<style scoped>.search-card { margin-bottom: 0; } .search-card .el-form-item { margin-bottom: 0; }</style>
<style lang="scss" scoped>
.procurement-page { padding: 16px; background: #f0f2f5; min-height: calc(100vh - 84px); }
.page-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 14px; padding: 14px 20px; background: linear-gradient(135deg, var(--ip-primary, #003A6C) 0%, var(--ip-secondary, #0050A0) 60%, var(--ip-bright, #1677FF) 100%); border-radius: 6px; color: #fff; }
.page-header-left { display: flex; align-items: center; font-size: 16px; font-weight: 600; i { font-size: 22px; margin-right: 10px; } }
.page-header-desc { font-size: 13px; opacity: 0.85; }
::v-deep .el-table th { background: var(--ip-light-bg, #EBF1FF) !important; color: var(--ip-secondary, #0050A0); font-weight: 600; }
::v-deep .el-card { border-radius: 6px; }
</style>