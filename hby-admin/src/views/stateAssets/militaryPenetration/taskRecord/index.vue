<template>
  <div class="military-page" :style="themeVars">
    <div class="page-header"><h2><i class="el-icon-s-flag"></i> 军品任务台账</h2><p>科研生产任务登记、进度跟踪与合规预警</p></div>
    <el-card shadow="never" class="search-card">
      <el-form :model="queryForm" inline size="small">
        <el-form-item label="企业名称"><el-input v-model="queryForm.companyName" placeholder="请输入" clearable style="width:160px" /></el-form-item>
        <el-form-item label="任务类型"><el-select v-model="queryForm.taskType" placeholder="请选择" clearable style="width:140px">
          <el-option label="装备科研" value="RESEARCH" /><el-option label="批量生产" value="PRODUCTION" /><el-option label="技术改造" value="REFORM" /><el-option label="维修保障" value="MAINTENANCE" />
        </el-select></el-form-item>
        <el-form-item label="密级"><el-select v-model="queryForm.secretLevel" placeholder="请选择" clearable style="width:120px">
          <el-option label="绝密" value="TOP_SECRET" /><el-option label="机密" value="SECRET" /><el-option label="秘密" value="CONFIDENTIAL" /><el-option label="内部" value="INTERNAL" />
        </el-select></el-form-item>
        <el-form-item label="状态"><el-select v-model="queryForm.taskStatus" placeholder="请选择" clearable style="width:120px">
          <el-option label="进行中" value="IN_PROGRESS" /><el-option label="已完成" value="COMPLETED" /><el-option label="已延期" value="OVERDUE" />
        </el-select></el-form-item>
        <el-form-item><el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button><el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button></el-form-item>
      </el-form>
    </el-card>
    <el-card shadow="never" style="margin-top:10px">
      <div style="margin-bottom:10px">
        <el-button type="primary" size="small" icon="el-icon-plus" @click="handleAdd">新增</el-button>
      </div>
      <el-table v-loading="loading" :data="list" border style="width:100%" size="small">
        <el-table-column label="任务名称" prop="taskName" min-width="180" show-overflow-tooltip />
        <el-table-column label="承制企业" prop="companyName" min-width="150" show-overflow-tooltip />
        <el-table-column label="任务类型" prop="taskType" width="90" align="center">
          <template slot-scope="{row}">{{ {RESEARCH:'装备科研',PRODUCTION:'批量生产',REFORM:'技术改造',MAINTENANCE:'维修保障'}[row.taskType] || row.taskType }}</template>
        </el-table-column>
        <el-table-column label="密级" prop="secretLevel" width="80" align="center">
          <template slot-scope="{row}"><el-tag :type="{TOP_SECRET:'danger',SECRET:'warning',CONFIDENTIAL:'info',INTERNAL:'success'}[row.secretLevel]" size="mini">{{ {TOP_SECRET:'绝密',SECRET:'机密',CONFIDENTIAL:'秘密',INTERNAL:'内部'}[row.secretLevel] || row.secretLevel }}</el-tag></template>
        </el-table-column>
        <el-table-column label="进度(%)" prop="progressRate" width="80" align="center" />
        <el-table-column label="状态" prop="status" width="80" align="center">
          <template slot-scope="{row}"><el-tag :type="{IN_PROGRESS:'primary',COMPLETED:'success',OVERDUE:'danger',PENDING:'info'}[row.status]" size="mini">{{ {IN_PROGRESS:'进行中',COMPLETED:'已完成',OVERDUE:'已延期',PENDING:'待开始'}[row.status] || row.status }}</el-tag></template>
        </el-table-column>
        <el-table-column label="计划完成" prop="planEndDate" width="110" align="center" />
        <el-table-column label="操作" width="180" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="text" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="text" style="color:#F56C6C" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination background style="margin-top:12px;text-align:right" :current-page="queryForm.pageNumber" :page-sizes="[10,20,50]" :page-size="queryForm.pageSize" layout="total,sizes,prev,pager,next,jumper" :total="total" @size-change="val=>{queryForm.pageSize=val;fetchData()}" @current-change="val=>{queryForm.pageNumber=val;fetchData()}" />
    </el-card>
    <el-dialog :title="{add:'新增任务',edit:'编辑任务',view:'查看任务'}[dialogType]" :visible.sync="dialogVisible" width="650px" :close-on-click-modal="false">
      <el-form :model="form" ref="form" label-width="100px" :rules="rules">
        <el-form-item label="任务名称" prop="taskName"><el-input v-model="form.taskName" :disabled="dialogType==='view'" /></el-form-item>
        <el-form-item label="承制企业"><el-input v-model="form.companyName" :disabled="dialogType==='view'" /></el-form-item>
        <el-form-item label="任务类型"><el-select v-model="form.taskType" :disabled="dialogType==='view'" style="width:100%"><el-option label="装备科研" value="RESEARCH" /><el-option label="批量生产" value="PRODUCTION" /><el-option label="技术改造" value="REFORM" /><el-option label="维修保障" value="MAINTENANCE" /></el-select></el-form-item>
        <el-form-item label="密级"><el-select v-model="form.secretLevel" :disabled="dialogType==='view'" style="width:100%"><el-option label="绝密" value="TOP_SECRET" /><el-option label="机密" value="SECRET" /><el-option label="秘密" value="CONFIDENTIAL" /><el-option label="内部" value="INTERNAL" /></el-select></el-form-item>
        <el-form-item label="进度(%)"><el-input-number v-model="form.progressRate" :min="0" :max="100" :disabled="dialogType==='view'" style="width:100%" /></el-form-item>
        <el-form-item label="状态"><el-select v-model="form.status" :disabled="dialogType==='view'" style="width:100%"><el-option label="待开始" value="PENDING" /><el-option label="进行中" value="IN_PROGRESS" /><el-option label="已完成" value="COMPLETED" /><el-option label="已延期" value="OVERDUE" /></el-select></el-form-item>
        <el-form-item label="计划完成"><el-date-picker v-model="form.planEndDate" type="date" value-format="yyyy-MM-dd" :disabled="dialogType==='view'" style="width:100%" /></el-form-item>
      </el-form>
      <div slot="footer" v-if="dialogType!=='view'">
        <el-button @click="dialogVisible=false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getTaskRecordList, addTaskRecord, updateTaskRecord, deleteTaskRecord } from '@/api/stateAssets/militaryPenetration'
import { investThemeMixin } from '../../themeMixin'
export default {
  name: 'MilitaryTaskRecord',
  mixins: [investThemeMixin],
  data() {
    return {
      loading: false, submitLoading: false, list: [], total: 0,
      queryForm: { companyName: '', taskType: '', secretLevel: '', taskStatus: '', pageNumber: 1, pageSize: 10 },
      dialogVisible: false, dialogType: 'add', form: {},
      rules: { taskName: [{ required: true, message: '请输入任务名称', trigger: 'blur' }] },
    }
  },
  created() { this.fetchData() },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const res = await getTaskRecordList(this.queryForm)
        if (res && res.result === 200) { this.list = (res.data && res.data.tlist) || []; this.total = (res.data && res.data.totalRecord) || 0 }
        else { this.list = []; this.total = 0 }
      } catch (e) { this.list = []; this.total = 0 } finally { this.loading = false }
    },
    handleQuery() { this.queryForm.pageNumber = 1; this.fetchData() },
    resetQuery() { this.queryForm = { companyName: '', taskType: '', secretLevel: '', taskStatus: '', pageNumber: 1, pageSize: 10 }; this.fetchData() },
    handleAdd() { this.dialogType = 'add'; this.form = {}; this.dialogVisible = true },
    handleView(row) { this.dialogType = 'view'; this.form = { ...row }; this.dialogVisible = true },
    handleEdit(row) { this.dialogType = 'edit'; this.form = { ...row }; this.dialogVisible = true },
    async handleSubmit() {
      this.$refs.form.validate(async (valid) => {
        if (!valid) return; this.submitLoading = true
        try {
          const res = await (this.dialogType === 'add' ? addTaskRecord : updateTaskRecord)(this.form)
          if (res && res.result === 200) { this.$message.success('操作成功'); this.dialogVisible = false; this.fetchData() } else { this.$message.error(res.msg || '操作失败') }
        } catch (e) { this.$message.error('操作失败') } finally { this.submitLoading = false }
      })
    },
    handleDelete(row) {
      this.$confirm('确认删除该任务？', '提示', { type: 'warning' }).then(async () => {
        const res = await deleteTaskRecord(row.recordId)
        if (res && res.result === 200) { this.$message.success('删除成功'); this.fetchData() }
      }).catch(() => {})
    },
  },
}
</script>

<style lang="scss" scoped>
.military-page { padding:16px; background:#f0f2f5; min-height:calc(100vh - 84px); }
.page-header { display:flex; align-items:center; justify-content:space-between; margin-bottom:16px; padding:18px 24px; background:linear-gradient(135deg, var(--ip-primary, #003A6C) 0%, var(--ip-secondary, #0050A0) 60%, var(--ip-bright, #1677FF) 100%); border-radius:6px; color:#fff;
  h2 { font-size:20px; margin:0 0 4px; i { margin-right:8px; } } p { font-size:13px; opacity:0.85; margin:0; } }
.stat-row { margin-bottom:16px; }
.stat-card { background:#fff; border-radius:6px; padding:16px 14px; box-shadow:0 1px 4px rgba(0,0,0,0.08);
  .stat-val { font-size:22px; font-weight:700; line-height:1.2; } .stat-label { font-size:12px; color:#8C8C8C; margin-top:4px; } }
.search-card { margin-bottom:0; ::v-deep .el-form-item { margin-bottom:0; } }
.chart-box-sm { height:220px; width:100%; }
::v-deep .el-table th { background:var(--ip-light-bg, #EBF1FF) !important; color:var(--ip-secondary, #0050A0); font-weight:600; }
::v-deep .el-table .row-overdue td { background:#FFF1F0 !important; }
::v-deep .el-table .row-top-secret td:first-child { border-left:3px solid #CF1322 !important; }
::v-deep .el-table .row-warning td { background:#FFF7E6 !important; }
::v-deep .el-card { border-radius:6px; }
</style>
