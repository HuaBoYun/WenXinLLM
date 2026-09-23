<template>
  <div class="app-container military-page" :style="themeVars">
    <div class="page-header">
      <div class="page-header-left"><i class="el-icon-s-flag"></i><span>军品任务管理</span></div>
      <div class="page-header-desc">管理军品科研生产任务、进度跟踪与交付管理</div>
    </div>
    <el-card class="search-card" shadow="never">
      <el-form ref="queryForm" :model="queryForm" inline size="small">
        <el-form-item label="任务名称" prop="taskName">
          <el-input v-model="queryForm.taskName" placeholder="请输入任务名称" clearable style="width: 180px" />
        </el-form-item>
        <el-form-item label="任务类型" prop="taskType">
          <el-select v-model="queryForm.taskType" placeholder="请选择" clearable style="width: 130px">
            <el-option label="科研" value="科研" />
            <el-option label="生产" value="生产" />
            <el-option label="维修" value="维修" />
            <el-option label="保障" value="保障" />
          </el-select>
        </el-form-item>
        <el-form-item label="任务状态" prop="taskStatus">
          <el-select v-model="queryForm.taskStatus" placeholder="请选择" clearable style="width: 130px">
            <el-option label="待启动" value="PENDING" />
            <el-option label="执行中" value="IN_PROGRESS" />
            <el-option label="已完成" value="COMPLETED" />
            <el-option label="已逾期" value="OVERDUE" />
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
        <el-table-column label="任务名称" prop="taskName" min-width="160" show-overflow-tooltip />
        <el-table-column label="任务编号" prop="taskCode" width="130" />
        <el-table-column label="承担单位" prop="companyName" min-width="140" show-overflow-tooltip />
        <el-table-column label="任务类型" prop="taskType" width="90" align="center" />
        <el-table-column label="合同金额(万元)" prop="contractAmount" width="130" align="right">
          <template slot-scope="{row}">{{ row.contractAmount != null ? Number(row.contractAmount).toLocaleString() : '-' }}</template>
        </el-table-column>
        <el-table-column label="实际进度(%)" prop="actualProgress" width="110" align="center">
          <template slot-scope="{row}">
            <el-progress v-if="row.actualProgress != null" :percentage="Number(row.actualProgress)" :stroke-width="14" :text-inside="true" :color="Number(row.actualProgress) >= 80 ? '#67C23A' : Number(row.actualProgress) >= 50 ? '#E6A23C' : '#F56C6C'" />
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column label="密级" prop="securityLevel" width="80" align="center">
          <template slot-scope="{row}">
            <el-tag :type="{TOP_SECRET:'danger',SECRET:'warning',CONFIDENTIAL:'info'}[row.securityLevel]" size="mini">{{ {TOP_SECRET:'绝密',SECRET:'机密',CONFIDENTIAL:'秘密'}[row.securityLevel] || row.securityLevel || '-' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="任务状态" prop="taskStatus" width="90" align="center">
          <template slot-scope="{row}">
            <el-tag :type="{ PENDING: 'info', IN_PROGRESS: '', COMPLETED: 'success', OVERDUE: 'danger' }[row.taskStatus]" size="small">
              {{ { PENDING: '待启动', IN_PROGRESS: '执行中', COMPLETED: '已完成', OVERDUE: '已逾期' }[row.taskStatus] || row.taskStatus }}
            </el-tag>
          </template>
        </el-table-column>
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
    <el-dialog :title="{ add: '新增军品任务', edit: '编辑军品任务', view: '查看军品任务' }[dialogType]" :visible.sync="dialogVisible" width="700px" :close-on-click-modal="false">
      <el-form :model="form" :rules="rules" ref="form" label-width="120px">
        <el-form-item label="任务名称" prop="taskName"><el-input v-model="form.taskName" :disabled="dialogType === 'view'" /></el-form-item>
        <el-form-item label="任务编号"><el-input v-model="form.taskCode" :disabled="dialogType === 'view'" /></el-form-item>
        <el-form-item label="承担单位"><el-input v-model="form.companyName" :disabled="dialogType === 'view'" /></el-form-item>
        <el-form-item label="任务类型">
          <el-select v-model="form.taskType" :disabled="dialogType === 'view'" style="width: 100%">
            <el-option label="科研" value="科研" /><el-option label="生产" value="生产" /><el-option label="维修" value="维修" /><el-option label="保障" value="保障" />
          </el-select>
        </el-form-item>
        <el-form-item label="合同金额(万元)"><el-input-number v-model="form.contractAmount" :min="0" :precision="2" style="width: 100%" :disabled="dialogType === 'view'" /></el-form-item>
        <el-form-item label="密级">
          <el-select v-model="form.securityLevel" :disabled="dialogType === 'view'" style="width: 100%">
            <el-option label="绝密" value="TOP_SECRET" /><el-option label="机密" value="SECRET" /><el-option label="秘密" value="CONFIDENTIAL" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" v-if="dialogType !== 'view'">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { getMilitaryTaskList, addMilitaryTask, updateMilitaryTask, deleteMilitaryTask, batchDeleteMilitaryTask } from '@/api/stateAssets/militaryPenetration'
import { investThemeMixin } from '../../themeMixin'
export default {
  name: 'MilitaryPenetrationTask',
  mixins: [investThemeMixin],
  data() {
    return {
      loading: false, submitLoading: false, list: [], total: 0, multipleSelection: [],
      queryForm: { pageNumber: 1, pageSize: 10, taskName: '', taskType: '', taskStatus: '' },
      dialogVisible: false, dialogType: 'add', form: {},
      rules: { taskName: [{ required: true, message: '请输入任务名称', trigger: 'blur' }] },
    }
  },
  created() { this.fetchData() },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const res = await getMilitaryTaskList(this.queryForm)
        if (res && res.result === 200) { this.list = (res.data && res.data.tlist) || []; this.total = (res.data && res.data.totalRecord) || 0 }
        else { this.list = []; this.total = 0 }
      } catch (e) { this.list = []; this.total = 0 } finally { this.loading = false }
    },
    handleQuery() { this.queryForm.pageNumber = 1; this.fetchData() },
    resetQuery() { this.queryForm = { pageNumber: 1, pageSize: 10, taskName: '', taskType: '', taskStatus: '' }; this.fetchData() },
    handleAdd() { this.dialogType = 'add'; this.form = {}; this.dialogVisible = true },
    async handleSubmit() {
      this.$refs.form.validate(async (valid) => {
        if (!valid) return; this.submitLoading = true
        try {
          const res = await (this.dialogType === 'add' ? addMilitaryTask : updateMilitaryTask)(this.form)
          if (res && res.result === 200) { this.$message.success('操作成功'); this.dialogVisible = false; this.fetchData() } else { this.$message.error(res.msg || '操作失败') }
        } catch (e) { this.$message.error('操作失败') } finally { this.submitLoading = false }
      })
    },
    handleDelete(row) {
      this.$confirm('确认删除该任务？', '提示', { type: 'warning' }).then(async () => {
        const res = await deleteMilitaryTask(row.taskId); if (res && res.result === 200) { this.$message.success('删除成功'); this.fetchData() }
      }).catch(() => {})
    },
    handleBatchDelete() {
      const ids = this.multipleSelection.map(i => i.taskId)
      this.$confirm(`确认删除选中的 ${ids.length} 条任务？`, '提示', { type: 'warning' }).then(async () => {
        const res = await batchDeleteMilitaryTask(ids); if (res && res.result === 200) { this.$message.success('删除成功'); this.fetchData() }
      }).catch(() => {})
    },
  },
}
</script>
<style scoped>.search-card { margin-bottom: 0; } .search-card .el-form-item { margin-bottom: 0; }</style>
<style lang="scss" scoped>
.military-page { padding: 16px; background: #f0f2f5; min-height: calc(100vh - 84px); }
.page-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 14px; padding: 14px 20px; background: linear-gradient(135deg, var(--ip-primary, #003A6C) 0%, var(--ip-secondary, #0050A0) 60%, var(--ip-bright, #1677FF) 100%); border-radius: 6px; color: #fff; }
.page-header-left { display: flex; align-items: center; font-size: 16px; font-weight: 600; i { font-size: 22px; margin-right: 10px; } }
.page-header-desc { font-size: 13px; opacity: 0.85; }
::v-deep .el-table th { background: var(--ip-light-bg, #EBF1FF) !important; color: var(--ip-secondary, #0050A0); font-weight: 600; }
::v-deep .el-card { border-radius: 6px; }
</style>

