<template>
  <div class="app-container invest-page">
    <div class="page-header">
      <div class="page-header-left"><i class="el-icon-edit-outline"></i><span>整改跟踪</span></div>
      <div class="page-header-desc">跟踪巡视发现问题的整改进度、验证结果与闭环管理</div>
    </div>
    <el-card class="search-card" shadow="never">
          <el-select v-model="queryForm.rectStatus" placeholder="请选择" clearable style="width: 130px">
            <el-option label="待整改" value="PENDING" /><el-option label="整改中" value="IN_PROGRESS" /><el-option label="已提交" value="SUBMITTED" /><el-option label="已通过" value="PASSED" /><el-option label="已驳回" value="REJECTED" />
          </el-select>
        </el-form-item>
        <el-form-item label="验证结果" prop="verifyResult">
          <el-select v-model="queryForm.verifyResult" placeholder="请选择" clearable style="width: 120px">
            <el-option label="通过" value="PASS" /><el-option label="不通过" value="FAIL" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>
    <el-card shadow="never" style="margin-top: 10px">
      <div style="margin-bottom: 10px"><el-button type="primary" size="small" icon="el-icon-plus" @click="handleAdd">新增</el-button></div>
      <el-table v-loading="loading" :data="list" border style="width: 100%">
        <el-table-column label="关联任务ID" prop="taskId" width="130" show-overflow-tooltip />
        <el-table-column label="整改要求" prop="requirement" min-width="200" show-overflow-tooltip />
        <el-table-column label="责任人" prop="responsibleUser" width="100" align="center" />
        <el-table-column label="截止时间" prop="deadline" width="110" align="center" />
        <el-table-column label="整改状态" prop="rectStatus" width="90" align="center">
          <template slot-scope="scope">
            <el-tag :type="{ PENDING: 'info', IN_PROGRESS: 'warning', SUBMITTED: '', PASSED: 'success', REJECTED: 'danger' }[scope.row.rectStatus]" size="small">
              {{ { PENDING: '待整改', IN_PROGRESS: '整改中', SUBMITTED: '已提交', PASSED: '已通过', REJECTED: '已驳回' }[scope.row.rectStatus] || scope.row.rectStatus }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="验证结果" prop="verifyResult" width="90" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.verifyResult" :type="scope.row.verifyResult === 'PASS' ? 'success' : 'danger'" size="small">{{ scope.row.verifyResult === 'PASS' ? '通过' : '不通过' }}</el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column label="提交时间" prop="submitTime" width="110" align="center" />
        <el-table-column label="操作" width="150" align="center" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="dialogType = 'view'; form = { ...scope.row }; dialogVisible = true">查看</el-button>
            <el-button size="mini" type="text" @click="dialogType = 'edit'; form = { ...scope.row }; dialogVisible = true">编辑</el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination background style="margin-top: 15px; text-align: right" :current-page="queryForm.pageNumber" :page-sizes="[10, 20, 50, 100]" :page-size="queryForm.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="total" @size-change="val => { queryForm.pageSize = val; fetchData() }" @current-change="val => { queryForm.pageNumber = val; fetchData() }" />
    </el-card>
    <el-dialog :title="{ add: '新增整改记录', edit: '编辑整改记录', view: '查看整改记录' }[dialogType]" :visible.sync="dialogVisible" width="700px" :close-on-click-modal="false">
      <el-form :model="form" :rules="rules" ref="form" label-width="100px">
        <el-form-item label="关联任务" prop="taskId"><el-input v-model="form.taskId" :disabled="dialogType === 'view' || dialogType === 'edit'" placeholder="请输入核查任务ID" /></el-form-item>
        <el-form-item label="整改要求" prop="requirement"><el-input v-model="form.requirement" type="textarea" :rows="3" :disabled="dialogType === 'view'" /></el-form-item>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="责任人"><el-input v-model="form.responsibleUser" :disabled="dialogType === 'view'" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="截止时间"><el-date-picker v-model="form.deadline" type="date" value-format="yyyy-MM-dd" style="width: 100%" :disabled="dialogType === 'view'" /></el-form-item></el-col>
        </el-row>
        <el-form-item label="整改进展"><el-input v-model="form.progressDesc" type="textarea" :rows="3" :disabled="dialogType === 'view'" /></el-form-item>
        <el-form-item label="验证意见" v-if="form.verifyComment || dialogType === 'view'"><el-input v-model="form.verifyComment" type="textarea" :rows="2" disabled /></el-form-item>
      </el-form>
      <div slot="footer" v-if="dialogType !== 'view'">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { getRectificationRecordList, addRectificationRecord, updateRectificationRecord } from '@/api/stateAssets/investigationPenetration'
export default {
  name: 'InvestigationPenetrationRectification',
  data() {
    return {
      loading: false, submitLoading: false, list: [], total: 0,
      queryForm: { pageNumber: 1, pageSize: 10, rectStatus: '', verifyResult: '' },
      dialogVisible: false, dialogType: 'add', form: {},
      rules: { taskId: [{ required: true, message: '请输入关联任务ID', trigger: 'blur' }], requirement: [{ required: true, message: '请输入整改要求', trigger: 'blur' }] },
    }
  },
  created() { this.fetchData() },
  methods: {
    async fetchData() {
      this.loading = true
      try {
        const res = await getRectificationRecordList(this.queryForm)
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
          const res = await (this.dialogType === 'add' ? addRectificationRecord : updateRectificationRecord)(this.form)
          if (res && res.result === 200) { this.$message.success('操作成功'); this.dialogVisible = false; this.fetchData() } else { this.$message.error(res.msg || '操作失败') }
        } catch (e) { this.$message.error('操作失败') } finally { this.submitLoading = false }
      })
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

