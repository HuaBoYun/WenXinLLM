<template>
  <div class="app-container invest-page">
    <div class="page-header">
      <div class="page-header-left"><i class="el-icon-document-checked"></i><span>巡视任务管理</span></div>
      <div class="page-header-desc">管理巡视核查任务分配、进度跟踪与结果汇总</div>
    </div>
    <el-card class="search-card" shadow="never">
          <el-input v-model="queryForm.taskTitle" placeholder="请输入" clearable style="width: 180px" />
        </el-form-item>
        <el-form-item label="监管领域" prop="domainType">
          <el-select v-model="queryForm.domainType" placeholder="请选择" clearable style="width: 130px">
            <el-option label="投资" value="INVESTMENT" /><el-option label="产权" value="PROPERTY" /><el-option label="财务" value="FINANCIAL" /><el-option label="金融风险" value="FINANCIAL_RISK" /><el-option label="会计" value="ACCOUNTING" /><el-option label="薪酬" value="SALARY" /><el-option label="军品" value="MILITARY" /><el-option label="采购" value="PROCUREMENT" /><el-option label="境外" value="OVERSEAS" /><el-option label="合同" value="CONTRACT" />
          </el-select>
        </el-form-item>
        <el-form-item label="任务状态" prop="taskStatus">
          <el-select v-model="queryForm.taskStatus" placeholder="请选择" clearable style="width: 120px">
            <el-option label="已创建" value="CREATED" /><el-option label="已分配" value="ASSIGNED" /><el-option label="核查中" value="INVESTIGATING" /><el-option label="已完成" value="COMPLETED" /><el-option label="已关闭" value="CLOSED" />
          </el-select>
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
      <el-table v-loading="loading" :data="list" border @selection-change="val => multipleSelection = val" style="width: 100%">
        <el-table-column type="selection" width="50" align="center" />
        <el-table-column label="任务编号" prop="taskCode" width="130" />
        <el-table-column label="任务标题" prop="taskTitle" min-width="180" show-overflow-tooltip />
        <el-table-column label="监管领域" prop="domainType" width="90" align="center" />
        <el-table-column label="涉及企业" prop="companyName" min-width="140" show-overflow-tooltip />
        <el-table-column label="任务状态" prop="taskStatus" width="90" align="center">
          <template slot-scope="scope">
            <el-tag :type="{ CREATED: 'info', ASSIGNED: '', INVESTIGATING: 'warning', COMPLETED: 'success', CLOSED: 'info' }[scope.row.taskStatus]" size="small">
              {{ { CREATED: '已创建', ASSIGNED: '已分配', INVESTIGATING: '核查中', COMPLETED: '已完成', CLOSED: '已关闭' }[scope.row.taskStatus] || scope.row.taskStatus }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="结论" prop="conclusion" width="90" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.conclusion" :type="scope.row.conclusion === 'COMPLIANT' ? 'success' : 'danger'" size="small">
              {{ { COMPLIANT: '合规', NON_COMPLIANT: '违规', PARTIALLY: '部分违规' }[scope.row.conclusion] || scope.row.conclusion }}
            </el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column label="截止日期" prop="deadline" width="110" align="center" />
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="dialogType = 'view'; form = { ...scope.row }; dialogVisible = true">查看</el-button>
            <el-button size="mini" type="text" @click="dialogType = 'edit'; form = { ...scope.row }; dialogVisible = true">编辑</el-button>
            <el-button size="mini" type="text" style="color: #F56C6C" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination background style="margin-top: 15px; text-align: right" :current-page="queryForm.pageNumber" :page-sizes="[10, 20, 50, 100]" :page-size="queryForm.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total" @size-change="val => { queryForm.pageSize = val; fetchData() }" @current-change="val => { queryForm.pageNumber = val; fetchData() }" />
    </el-card>
    <el-dialog :title="{ add: '新增核查任务', edit: '编辑核查任务', view: '查看核查任务' }[dialogType]" :visible.sync="dialogVisible" width="750px" :close-on-click-modal="false">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="任务标题" prop="taskTitle"><el-input v-model="form.taskTitle" :disabled="dialogType === 'view'" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="任务编号"><el-input v-model="form.taskCode" :disabled="dialogType === 'view'" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="监管领域" prop="domainType">
            <el-select v-model="form.domainType" :disabled="dialogType === 'view'" style="width: 100%">
              <el-option label="投资" value="INVESTMENT" /><el-option label="产权" value="PROPERTY" /><el-option label="财务" value="FINANCIAL" /><el-option label="金融风险" value="FINANCIAL_RISK" /><el-option label="会计" value="ACCOUNTING" /><el-option label="薪酬" value="SALARY" /><el-option label="军品" value="MILITARY" /><el-option label="采购" value="PROCUREMENT" /><el-option label="境外" value="OVERSEAS" /><el-option label="合同" value="CONTRACT" />
            </el-select>
          </el-form-item></el-col>
          <el-col :span="12"><el-form-item label="涉及企业"><el-input v-model="form.companyName" :disabled="dialogType === 'view'" /></el-form-item></el-col>
        </el-row>
        <el-form-item label="任务描述"><el-input v-model="form.taskDesc" type="textarea" :rows="3" :disabled="dialogType === 'view'" /></el-form-item>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="截止日期"><el-date-picker v-model="form.deadline" type="date" value-format="yyyy-MM-dd" style="width: 100%" :disabled="dialogType === 'view'" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="结论" v-if="dialogType !== 'add'">
            <el-select v-model="form.conclusion" :disabled="dialogType === 'view'" style="width: 100%">
              <el-option label="合规" value="COMPLIANT" /><el-option label="违规" value="NON_COMPLIANT" /><el-option label="部分违规" value="PARTIALLY" />
            </el-select>
          </el-form-item></el-col>
        </el-row>
        <el-form-item label="结论说明" v-if="form.conclusion"><el-input v-model="form.conclusionDesc" type="textarea" :rows="3" :disabled="dialogType === 'view'" /></el-form-item>
      </el-form>
      <div slot="footer" v-if="dialogType !== 'view'">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { getInvestigationTaskList, addInvestigationTask, updateInvestigationTask, deleteInvestigationTask, batchDeleteInvestigationTask } from '@/api/stateAssets/investigationPenetration'
export default {
  name: 'InvestigationPenetrationTask',
  data() {
    return {
      loading: false, submitLoading: false, list: [], total: 0, multipleSelection: [],
      queryForm: { pageNumber: 1, pageSize: 10, taskTitle: '', domainType: '', taskStatus: '' },
      dialogVisible: false, dialogType: 'add', form: {},
      rules: { taskTitle: [{ required: true, message: '请输入任务标题', trigger: 'blur' }], domainType: [{ required: true, message: '请选择监管领域', trigger: 'change' }] },
    }
  },
  created() { this.fetchData() },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const res = await getInvestigationTaskList(this.queryForm)
        if (res && res.result === 200) { this.list = (res.data && res.data.tlist) || []; this.total = (res.data && res.data.totalRecord) || 0 }
        else { this.list = []; this.total = 0 }
      } catch (e) { this.list = []; this.total = 0 } finally { this.loading = false }
    },
    handleQuery() { this.queryForm.pageNumber = 1; this.fetchData() },
    resetQuery() { this.$refs.queryForm.resetFields(); this.queryForm.pageNumber = 1; this.fetchData() },
    handleAdd() { this.dialogType = 'add'; this.form = {}; this.dialogVisible = true },
    async handleSubmit() {
      this.$refs.form.validate(async (valid) => {
        if (!valid) return; this.submitLoading = true
        try {
          const res = await (this.dialogType === 'add' ? addInvestigationTask : updateInvestigationTask)(this.form)
          if (res && res.result === 200) { this.$message.success('操作成功'); this.dialogVisible = false; this.fetchData() } else { this.$message.error(res.msg || '操作失败') }
        } catch (e) { this.$message.error('操作失败') } finally { this.submitLoading = false }
      })
    },
    handleDelete(row) {
      this.$confirm('确认删除？', '提示', { type: 'warning' }).then(async () => {
        const res = await deleteInvestigationTask(row.taskId); if (res && res.result === 200) { this.$message.success('删除成功'); this.fetchData() }
      }).catch(() => {})
    },
    handleBatchDelete() {
      const ids = this.multipleSelection.map(i => i.taskId)
      this.$confirm(`确认删除 ${ids.length} 条？`, '提示', { type: 'warning' }).then(async () => {
        const res = await batchDeleteInvestigationTask(ids); if (res && res.result === 200) { this.$message.success('删除成功'); this.fetchData() }
      }).catch(() => {})
    },
  },
}
</script>
<style scoped>.search-card { margin-bottom: 0; } .search-card .el-form-item { margin-bottom: 0; }</style>
<style lang="scss" scoped>
.invest-page { padding: 16px; background: #f0f2f5; min-height: calc(100vh - 84px); }
.page-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 14px; padding: 14px 20px; background: linear-gradient(135deg, #d46b08 0%, #fa8c16 100%); border-radius: 6px; color: #fff; }
.page-header-left { display: flex; align-items: center; font-size: 16px; font-weight: 600; i { font-size: 22px; margin-right: 10px; } }
.page-header-desc { font-size: 13px; opacity: 0.85; }
::v-deep .el-table th { background: #fff7e6 !important; }
::v-deep .el-card { border-radius: 6px; }
</style>

