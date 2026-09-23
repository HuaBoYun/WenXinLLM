<template>
  <div class="training-development">
    <el-card shadow="never" class="search-card">
      <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="80px">
        <el-form-item label="培训名称" prop="trainingName"><el-input v-model="queryForm.trainingName" placeholder="请输入培训名称" clearable /></el-form-item>
        <el-form-item label="培训类型" prop="trainingType"><el-select v-model="queryForm.trainingType" placeholder="请选择培训类型" clearable><el-option label="技能培训" value="技能培训" /><el-option label="管理培训" value="管理培训" /><el-option label="安全培训" value="安全培训" /><el-option label="合规培训" value="合规培训" /></el-select></el-form-item>
        <el-form-item label="培训状态" prop="status"><el-select v-model="queryForm.status" placeholder="请选择培训状态" clearable><el-option label="计划中" value="计划中" /><el-option label="进行中" value="进行中" /><el-option label="已完成" value="已完成" /><el-option label="已取消" value="已取消" /></el-select></el-form-item>
        <el-form-item><el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button><el-button icon="el-icon-refresh" @click="handleReset">重置</el-button></el-form-item>
      </el-form>
    </el-card>
    <el-card shadow="never" class="action-card">
      <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新建培训</el-button>
      <el-button type="warning" icon="el-icon-download" @click="handleExport">导出培训记录</el-button>
    </el-card>
    <el-card shadow="never" class="table-card">
      <el-table :data="trainingList" v-loading="loading" @selection-change="s => multipleSelection = s" stripe border>
        <el-table-column type="selection" width="55" />
        <el-table-column prop="trainingNo" label="培训编号" width="130" />
        <el-table-column prop="trainingName" label="培训名称" width="200" />
        <el-table-column prop="trainingType" label="培训类型" width="100">
          <template slot-scope="scope"><el-tag :type="getTypeColor(scope.row.trainingType)">{{ scope.row.trainingType }}</el-tag></template>
        </el-table-column>
        <el-table-column prop="trainer" label="讲师" width="120" />
        <el-table-column prop="startDate" label="开始时间" width="110" />
        <el-table-column prop="endDate" label="结束时间" width="110" />
        <el-table-column prop="maxParticipants" label="最大人数" width="90" />
        <el-table-column prop="currentParticipants" label="当前人数" width="90">
          <template slot-scope="scope"><span :class="getParticipantsClass(scope.row)">{{ scope.row.currentParticipants || 0 }}</span></template>
        </el-table-column>
        <el-table-column prop="completionRate" label="完成率" width="90">
          <template slot-scope="scope"><span class="completion-rate">{{ scope.row.completionRate != null ? scope.row.completionRate + '%' : '0%' }}</span></template>
        </el-table-column>
        <el-table-column prop="budgetAmount" label="预算(元)" width="100">
          <template slot-scope="scope"><span>¥{{ formatMoney(scope.row.budgetAmount) }}</span></template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="90">
          <template slot-scope="scope"><el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status }}</el-tag></template>
        </el-table-column>
        <el-table-column label="操作" width="240" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="success" @click="handleEnroll(scope.row)" v-if="scope.row.status === '计划中' || scope.row.status === '进行中'">报名</el-button>
            <el-button size="mini" type="warning" @click="handleEdit(scope.row)" v-if="scope.row.status !== '已完成'">编辑</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination-container"><el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="pagination.currentPage" :page-sizes="[10, 20, 50, 100]" :page-size="pagination.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="pagination.total" /></div>
    </el-card>

    <!-- 查看/新增/编辑对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="700px" :close-on-click-modal="false">
      <el-form :model="formData" ref="formRef" :rules="formRules" label-width="100px" :disabled="dialogMode === 'view'">
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="培训编号" prop="trainingNo"><el-input v-model="formData.trainingNo" placeholder="如: TRN20240801" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="培训名称" prop="trainingName"><el-input v-model="formData.trainingName" placeholder="请输入培训名称" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="培训类型" prop="trainingType"><el-select v-model="formData.trainingType" placeholder="请选择" style="width:100%"><el-option label="技能培训" value="技能培训" /><el-option label="管理培训" value="管理培训" /><el-option label="安全培训" value="安全培训" /><el-option label="合规培训" value="合规培训" /></el-select></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="讲师" prop="trainer"><el-input v-model="formData.trainer" placeholder="请输入讲师姓名" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="开始日期" prop="startDate"><el-date-picker v-model="formData.startDate" type="date" value-format="yyyy-MM-dd" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="结束日期" prop="endDate"><el-date-picker v-model="formData.endDate" type="date" value-format="yyyy-MM-dd" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8"><el-form-item label="最大人数"><el-input-number v-model="formData.maxParticipants" :min="1" style="width:100%" /></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="当前人数"><el-input-number v-model="formData.currentParticipants" :min="0" style="width:100%" /></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="完成率(%)"><el-input-number v-model="formData.completionRate" :min="0" :max="100" :precision="1" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="预算金额"><el-input-number v-model="formData.budgetAmount" :min="0" :precision="2" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="实际金额"><el-input-number v-model="formData.actualAmount" :min="0" :precision="2" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="培训评分"><el-input-number v-model="formData.score" :min="0" :max="5" :precision="1" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="状态"><el-select v-model="formData.status" style="width:100%"><el-option label="计划中" value="计划中" /><el-option label="进行中" value="进行中" /><el-option label="已完成" value="已完成" /><el-option label="已取消" value="已取消" /></el-select></el-form-item></el-col>
        </el-row>
      </el-form>
      <div slot="footer" v-if="dialogMode !== 'view'"><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" :loading="submitLoading" @click="handleSubmit">确定</el-button></div>
    </el-dialog>
  </div>
</template>

<script>
import { trainingApi } from '@/api/enterprise/hr'

export default {
  name: 'TrainingDevelopment',
  data() {
    return {
      loading: false, submitLoading: false,
      queryForm: { trainingName: '', trainingType: '', status: '' },
      trainingList: [], multipleSelection: [],
      pagination: { currentPage: 1, pageSize: 20, total: 0 },
      dialogVisible: false, dialogMode: 'add', formData: {},
      formRules: { trainingName: [{ required: true, message: '请输入培训名称', trigger: 'blur' }], trainingType: [{ required: true, message: '请选择培训类型', trigger: 'change' }], trainer: [{ required: true, message: '请输入讲师', trigger: 'blur' }] }
    }
  },
  computed: { dialogTitle() { return { add: '新建培训', edit: '编辑培训', view: '培训详情' }[this.dialogMode] || '' } },
  mounted() { this.loadTrainingList() },
  methods: {
    async loadTrainingList() {
      this.loading = true
      try { const res = await trainingApi.getTrainingList({ ...this.queryForm, pageNumber: this.pagination.currentPage || 1, pageSize: this.pagination.pageSize || 15 }); if (res && res.data) { this.trainingList = res.data.tlist || []; this.pagination.total = res.data.totalRecord || 0 } else { this.trainingList = []; this.pagination.total = 0 } }
      catch (e) { this.trainingList = []; this.pagination.total = 0 } finally { this.loading = false }
    },
    formatMoney(v) { return v != null ? Number(v).toLocaleString() : '0' },
    handleSearch() { this.pagination.currentPage = 1; this.loadTrainingList() },
    handleReset() { this.$refs.queryForm.resetFields(); this.loadTrainingList() },
    handleAdd() { this.dialogMode = 'add'; this.formData = { enterpriseId: 'ent001', enterpriseName: '贵州国资投资集团', status: '计划中', maxParticipants: 20, currentParticipants: 0, completionRate: 0 }; this.dialogVisible = true },
    handleView(row) { this.dialogMode = 'view'; this.formData = { ...row }; this.dialogVisible = true },
    handleEdit(row) { this.dialogMode = 'edit'; this.formData = { ...row }; this.dialogVisible = true },
    async handleEnroll(row) {
      try { await trainingApi.enrollTraining({ id: row.id }); this.$message.success('报名成功'); this.loadTrainingList() }
      catch (e) { this.$message.error('报名失败') }
    },
    handleDelete(row) { this.$confirm('确认删除该培训记录吗？', '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }).then(async () => { try { await trainingApi.deleteTrainingPlan(row.id); this.$message.success('删除成功'); this.loadTrainingList() } catch (e) { this.$message.error('删除失败') } }).catch(() => {}) },
    handleExport() {
      this.$message.info('正在导出培训记录...')
      trainingApi.exportTraining(this.queryForm).then(res => {
        if (res && res.data) {
          const blob = new Blob([res.data], { type: 'application/vnd.ms-excel' })
          const link = document.createElement('a')
          link.href = URL.createObjectURL(blob)
          link.download = '培训记录.xlsx'
          link.click()
          URL.revokeObjectURL(link.href)
          this.$message.success('导出成功')
        }
      }).catch(() => { this.$message.error('导出失败') })
    },
    async handleSubmit() {
      try { await this.$refs.formRef.validate() } catch (e) { return }
      this.submitLoading = true
      try {
        if (this.dialogMode === 'add') { await trainingApi.createTrainingPlan(this.formData); this.$message.success('新建成功') }
        else { await trainingApi.updateTrainingPlan(this.formData.id, this.formData); this.$message.success('编辑成功') }
        this.dialogVisible = false; this.loadTrainingList()
      } catch (e) { this.$message.error(this.dialogMode === 'add' ? '新建失败' : '编辑失败') } finally { this.submitLoading = false }
    },
    handleSizeChange(v) { this.pagination.pageSize = v; this.loadTrainingList() },
    handleCurrentChange(v) { this.pagination.currentPage = v; this.loadTrainingList() },
    getTypeColor(t) { return { '技能培训': 'primary', '管理培训': 'success', '安全培训': 'warning', '合规培训': 'danger' }[t] || 'info' },
    getParticipantsClass(row) { const r = (row.currentParticipants || 0) / (row.maxParticipants || 1); if (r >= 0.8) return 'participants-full'; if (r >= 0.5) return 'participants-half'; return 'participants-low' },
    getStatusType(s) { return { '计划中': 'info', '进行中': 'warning', '已完成': 'success', '已取消': 'danger' }[s] || 'info' }
  }
}
</script>

<style lang="scss" scoped>
.training-development {
  .search-card, .action-card, .table-card { margin-bottom: 16px; }
  .participants-full { color: #F56C6C; font-weight: bold; }
  .participants-half { color: #E6A23C; font-weight: 500; }
  .participants-low { color: #909399; }
  .completion-rate { color: #67C23A; font-weight: 500; }
  .pagination-container { margin-top: 20px; text-align: right; }
}
</style>
