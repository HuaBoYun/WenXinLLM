<template>
  <div class="performance-evaluation">
    <el-card shadow="never" class="search-card">
      <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="80px">
        <el-form-item label="员工姓名" prop="employeeName"><el-input v-model="queryForm.employeeName" placeholder="请输入员工姓名" clearable /></el-form-item>
        <el-form-item label="部门" prop="department"><el-select v-model="queryForm.department" placeholder="请选择部门" clearable><el-option v-for="d in departmentOptions" :key="d" :label="d" :value="d" /></el-select></el-form-item>
        <el-form-item label="考核年度" prop="assessmentYear"><el-date-picker v-model="queryForm.assessmentYear" type="year" value-format="yyyy" placeholder="请选择年度" clearable style="width:140px" /></el-form-item>
        <el-form-item label="考核周期" prop="evaluationPeriod"><el-select v-model="queryForm.evaluationPeriod" placeholder="请选择考核周期" clearable><el-option label="上半年" value="上半年" /><el-option label="下半年" value="下半年" /><el-option label="Q1" value="Q1" /><el-option label="Q2" value="Q2" /><el-option label="Q3" value="Q3" /><el-option label="Q4" value="Q4" /></el-select></el-form-item>
        <el-form-item label="考核等级" prop="grade"><el-select v-model="queryForm.grade" placeholder="请选择" clearable><el-option label="优秀" value="优秀" /><el-option label="良好" value="良好" /><el-option label="合格" value="合格" /><el-option label="待改进" value="待改进" /></el-select></el-form-item>
        <el-form-item><el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button><el-button icon="el-icon-refresh" @click="handleReset">重置</el-button></el-form-item>
      </el-form>
    </el-card>
    <el-card shadow="never" class="action-card">
      <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新建考核</el-button>
      <el-button type="warning" icon="el-icon-download" @click="handleExport">导出考核结果</el-button>
    </el-card>
    <el-card shadow="never" class="table-card">
      <el-table :data="evaluationList" v-loading="loading" @selection-change="s => multipleSelection = s" stripe border>
        <el-table-column type="selection" width="55" />
        <el-table-column prop="employeeId" label="员工编号" width="100" />
        <el-table-column prop="employeeName" label="员工姓名" width="100" />
        <el-table-column prop="department" label="部门" width="110" />
        <el-table-column prop="position" label="职位" width="120" />
        <el-table-column prop="assessmentPeriod" label="考核周期" width="100">
          <template slot-scope="scope"><span>{{ scope.row.assessmentYear }}{{ scope.row.assessmentPeriod }}</span></template>
        </el-table-column>
        <el-table-column prop="evaluator" label="考核人" width="100" />
        <el-table-column prop="workPerformance" label="工作绩效" width="90">
          <template slot-scope="scope"><span class="score">{{ scope.row.workPerformance != null ? scope.row.workPerformance + '分' : '-' }}</span></template>
        </el-table-column>
        <el-table-column prop="abilityEvaluation" label="能力评价" width="90">
          <template slot-scope="scope"><span class="score">{{ scope.row.abilityEvaluation != null ? scope.row.abilityEvaluation + '分' : '-' }}</span></template>
        </el-table-column>
        <el-table-column prop="attitudeEvaluation" label="态度评价" width="90">
          <template slot-scope="scope"><span class="score">{{ scope.row.attitudeEvaluation != null ? scope.row.attitudeEvaluation + '分' : '-' }}</span></template>
        </el-table-column>
        <el-table-column prop="totalScore" label="总分" width="90">
          <template slot-scope="scope"><span class="total-score">{{ scope.row.totalScore != null ? scope.row.totalScore + '分' : '-' }}</span></template>
        </el-table-column>
        <el-table-column prop="grade" label="考核等级" width="90">
          <template slot-scope="scope"><el-tag :type="getLevelType(scope.row.grade)">{{ scope.row.grade || '-' }}</el-tag></template>
        </el-table-column>
        <el-table-column label="操作" width="240" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="success" @click="handleEvaluate(scope.row)" v-if="!scope.row.grade">考核</el-button>
            <el-button size="mini" type="warning" @click="handleEdit(scope.row)">编辑</el-button>
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
          <el-col :span="12"><el-form-item label="员工ID" prop="employeeId"><el-input v-model="formData.employeeId" placeholder="请输入员工ID" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="员工姓名" prop="employeeName"><el-input v-model="formData.employeeName" placeholder="请输入员工姓名" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="部门" prop="department"><el-select v-model="formData.department" placeholder="请选择" style="width:100%"><el-option v-for="d in departmentOptions" :key="d" :label="d" :value="d" /></el-select></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="职位" prop="position"><el-input v-model="formData.position" placeholder="请输入职位" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="考核年度" prop="assessmentYear"><el-date-picker v-model="formData.assessmentYear" type="year" value-format="yyyy" placeholder="请选择考核年度" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="考核周期" prop="assessmentPeriod"><el-select v-model="formData.assessmentPeriod" placeholder="请选择考核周期" style="width:100%"><el-option label="上半年" value="上半年" /><el-option label="下半年" value="下半年" /><el-option label="Q1" value="Q1" /><el-option label="Q2" value="Q2" /><el-option label="Q3" value="Q3" /><el-option label="Q4" value="Q4" /></el-select></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="考核人" prop="evaluator"><el-input v-model="formData.evaluator" placeholder="请输入考核人" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="考核等级"><el-select v-model="formData.grade" placeholder="请选择" clearable style="width:100%"><el-option label="优秀" value="优秀" /><el-option label="良好" value="良好" /><el-option label="合格" value="合格" /><el-option label="待改进" value="待改进" /></el-select></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8"><el-form-item label="工作绩效"><el-input-number v-model="formData.workPerformance" :min="0" :max="100" :precision="1" style="width:100%" /></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="能力评价"><el-input-number v-model="formData.abilityEvaluation" :min="0" :max="100" :precision="1" style="width:100%" /></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="态度评价"><el-input-number v-model="formData.attitudeEvaluation" :min="0" :max="100" :precision="1" style="width:100%" /></el-form-item></el-col>
        </el-row>
      </el-form>
      <div slot="footer" v-if="dialogMode !== 'view'"><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" :loading="submitLoading" @click="handleSubmit">确定</el-button></div>
    </el-dialog>
  </div>
</template>

<script>
import { performanceApi, organizationApi } from '@/api/enterprise/hr'

export default {
  name: 'PerformanceEvaluation',
  data() {
    return {
      loading: false, submitLoading: false,
      queryForm: { employeeName: '', department: '', assessmentYear: '', evaluationPeriod: '', grade: '' },
      evaluationList: [], multipleSelection: [], departmentOptions: [],
      pagination: { currentPage: 1, pageSize: 20, total: 0 },
      dialogVisible: false, dialogMode: 'add', formData: {},
      formRules: { employeeName: [{ required: true, message: '请输入员工姓名', trigger: 'blur' }], assessmentYear: [{ required: true, message: '请输入考核年度', trigger: 'blur' }], assessmentPeriod: [{ required: true, message: '请选择考核周期', trigger: 'change' }] }
    }
  },
  computed: { dialogTitle() { return { add: '新建考核', edit: '编辑考核', view: '考核详情' }[this.dialogMode] || '' } },
  mounted() { this.loadDepartmentOptions(); this.loadEvaluationList() },
  methods: {
    async loadDepartmentOptions() { try { const res = await organizationApi.getDepartmentList({}); if (res && res.data) { const depts = []; const ex = l => { l.forEach(d => { if (d.deptName) depts.push(d.deptName); if (d.children) ex(d.children) }) }; ex(res.data || []); this.departmentOptions = [...new Set(depts)] } } catch (e) {} },
    async loadEvaluationList() {
      this.loading = true
      try { const res = await performanceApi.getPerformanceList({ ...this.queryForm, pageNumber: this.pagination.currentPage || 1, pageSize: this.pagination.pageSize || 15 }); if (res && res.data) { this.evaluationList = res.data.tlist || []; this.pagination.total = res.data.totalRecord || 0 } else { this.evaluationList = []; this.pagination.total = 0 } }
      catch (e) { this.evaluationList = []; this.pagination.total = 0 } finally { this.loading = false }
    },
    handleSearch() { this.pagination.currentPage = 1; this.loadEvaluationList() },
    handleReset() { this.$refs.queryForm.resetFields(); this.loadEvaluationList() },
    handleAdd() { this.dialogMode = 'add'; this.formData = { enterpriseId: 'ent001', enterpriseName: '贵州国资投资集团', assessmentYear: '2024', assessmentPeriod: '上半年' }; this.dialogVisible = true },
    handleView(row) { this.dialogMode = 'view'; this.formData = { ...row }; this.dialogVisible = true },
    handleEdit(row) { this.dialogMode = 'edit'; this.formData = { ...row }; this.dialogVisible = true },
    handleEvaluate(row) { this.dialogMode = 'edit'; this.formData = { ...row }; this.dialogVisible = true },
    handleDelete(row) { this.$confirm('确认删除该考核记录吗？', '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }).then(async () => { try { await performanceApi.deletePerformanceEvaluation(row.id); this.$message.success('删除成功'); this.loadEvaluationList() } catch (e) { this.$message.error('删除失败') } }).catch(() => {}) },
    handleExport() {
      this.$message.info('正在导出考核结果...')
      performanceApi.exportPerformance(this.queryForm).then(res => {
        if (res && res.data) {
          const blob = new Blob([res.data], { type: 'application/vnd.ms-excel' })
          const link = document.createElement('a')
          link.href = URL.createObjectURL(blob)
          link.download = '绩效考核数据.xlsx'
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
        // 自动计算总分
        const wp = this.formData.workPerformance || 0; const ae = this.formData.abilityEvaluation || 0; const at = this.formData.attitudeEvaluation || 0
        const total = Math.round((wp + ae + at) * 10) / 10
        this.formData.totalScore = total
        this.formData.score = total
        if (this.dialogMode === 'add') { await performanceApi.createPerformanceEvaluation(this.formData); this.$message.success('新建成功') }
        else { await performanceApi.updatePerformanceEvaluation(this.formData.id, this.formData); this.$message.success('编辑成功') }
        this.dialogVisible = false; this.loadEvaluationList()
      } catch (e) { this.$message.error(this.dialogMode === 'add' ? '新建失败' : '编辑失败') } finally { this.submitLoading = false }
    },
    handleSizeChange(v) { this.pagination.pageSize = v; this.loadEvaluationList() },
    handleCurrentChange(v) { this.pagination.currentPage = v; this.loadEvaluationList() },
    getLevelType(l) { return { '优秀': 'success', '良好': 'primary', '合格': 'warning', '待改进': 'danger' }[l] || 'info' }
  }
}
</script>

<style lang="scss" scoped>
.performance-evaluation {
  .search-card, .action-card, .table-card { margin-bottom: 16px; }
  .score { color: #409EFF; font-weight: 500; }
  .total-score { color: #E6A23C; font-weight: bold; font-size: 14px; }
  .pagination-container { margin-top: 20px; text-align: right; }
}
</style>
