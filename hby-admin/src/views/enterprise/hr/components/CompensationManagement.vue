<template>
  <div class="compensation-management">
    <el-card shadow="never" class="search-card">
      <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="80px">
        <el-form-item label="员工姓名" prop="employeeName"><el-input v-model="queryForm.employeeName" placeholder="请输入员工姓名" clearable /></el-form-item>
        <el-form-item label="部门" prop="department"><el-select v-model="queryForm.department" placeholder="请选择部门" clearable><el-option v-for="d in departmentOptions" :key="d" :label="d" :value="d" /></el-select></el-form-item>
        <el-form-item label="职级" prop="level"><el-select v-model="queryForm.level" placeholder="请选择职级" clearable><el-option v-for="l in levelOptions" :key="l" :label="l" :value="l" /></el-select></el-form-item>
        <el-form-item><el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button><el-button icon="el-icon-refresh" @click="handleReset">重置</el-button></el-form-item>
      </el-form>
    </el-card>
    <el-card shadow="never" class="action-card">
      <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增薪酬方案</el-button>
      <el-button type="success" icon="el-icon-upload2" @click="handleBatchAdjust" :disabled="multipleSelection.length === 0">批量调薪</el-button>
      <el-button type="warning" icon="el-icon-download" @click="handleExport">导出薪酬数据</el-button>
    </el-card>
    <el-card shadow="never" class="table-card">
      <el-table :data="compensationList" v-loading="loading" @selection-change="handleSelectionChange" stripe border>
        <el-table-column type="selection" width="55" />
        <el-table-column prop="employeeNo" label="员工编号" width="130" />
        <el-table-column prop="employeeName" label="员工姓名" width="100" />
        <el-table-column prop="department" label="部门" width="110" />
        <el-table-column prop="position" label="职位" width="110" />
        <el-table-column prop="level" label="职级" width="80" />
        <el-table-column prop="baseSalary" label="基本工资" width="110"><template slot-scope="scope"><span class="salary-amount">¥{{ formatMoney(scope.row.baseSalary) }}</span></template></el-table-column>
        <el-table-column prop="performanceBonus" label="绩效奖金" width="110"><template slot-scope="scope"><span class="salary-amount">¥{{ formatMoney(scope.row.performanceBonus) }}</span></template></el-table-column>
        <el-table-column prop="allowance" label="津贴补贴" width="110"><template slot-scope="scope"><span class="salary-amount">¥{{ formatMoney(scope.row.allowance) }}</span></template></el-table-column>
        <el-table-column prop="totalSalary" label="总薪酬" width="110"><template slot-scope="scope"><span class="total-salary">¥{{ formatMoney(scope.row.totalSalary) }}</span></template></el-table-column>
        <el-table-column prop="lastAdjustDate" label="最后调薪" width="110" />
        <el-table-column prop="status" label="状态" width="90"><template slot-scope="scope"><el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status }}</el-tag></template></el-table-column>
        <el-table-column label="操作" width="240" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="success" @click="handleAdjust(scope.row)">调薪</el-button>
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
          <el-col :span="12"><el-form-item label="员工编号" prop="employeeNo"><el-input v-model="formData.employeeNo" placeholder="请输入员工编号" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="员工姓名" prop="employeeName"><el-input v-model="formData.employeeName" placeholder="请输入员工姓名" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="部门" prop="department"><el-select v-model="formData.department" placeholder="请选择" style="width:100%"><el-option v-for="d in departmentOptions" :key="d" :label="d" :value="d" /></el-select></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="职位" prop="position"><el-input v-model="formData.position" placeholder="请输入职位" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="职级" prop="level"><el-select v-model="formData.level" placeholder="请选择" style="width:100%"><el-option v-for="l in levelOptions" :key="l" :label="l" :value="l" /></el-select></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="状态"><el-select v-model="formData.status" style="width:100%"><el-option label="正常" value="正常" /><el-option label="调薪中" value="调薪中" /><el-option label="冻结" value="冻结" /></el-select></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8"><el-form-item label="基本工资" prop="baseSalary"><el-input-number v-model="formData.baseSalary" :min="0" :precision="2" style="width:100%" /></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="绩效奖金"><el-input-number v-model="formData.performanceBonus" :min="0" :precision="2" style="width:100%" /></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="津贴补贴"><el-input-number v-model="formData.allowance" :min="0" :precision="2" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8"><el-form-item label="社保"><el-input-number v-model="formData.socialInsurance" :min="0" :precision="2" style="width:100%" /></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="公积金"><el-input-number v-model="formData.housingFund" :min="0" :precision="2" style="width:100%" /></el-form-item></el-col>
          <el-col :span="8"><el-form-item label="最后调薪"><el-date-picker v-model="formData.lastAdjustDate" type="date" value-format="yyyy-MM-dd" style="width:100%" /></el-form-item></el-col>
        </el-row>
      </el-form>
      <div slot="footer" v-if="dialogMode !== 'view'"><el-button @click="dialogVisible = false">取消</el-button><el-button type="primary" :loading="submitLoading" @click="handleSubmit">确定</el-button></div>
    </el-dialog>

    <!-- 调薪对话框 -->
    <el-dialog title="薪酬调整" :visible.sync="adjustDialogVisible" width="500px" :close-on-click-modal="false">
      <el-form :model="adjustForm" ref="adjustFormRef" label-width="100px">
        <el-form-item label="员工"><span>{{ adjustForm.employeeName }} ({{ adjustForm.employeeNo }})</span></el-form-item>
        <el-form-item label="当前总薪酬"><span class="total-salary">¥{{ formatMoney(adjustForm.currentTotal) }}</span></el-form-item>
        <el-form-item label="基本工资" prop="baseSalary"><el-input-number v-model="adjustForm.baseSalary" :min="0" :precision="2" style="width:100%" /></el-form-item>
        <el-form-item label="绩效奖金" prop="performanceBonus"><el-input-number v-model="adjustForm.performanceBonus" :min="0" :precision="2" style="width:100%" /></el-form-item>
        <el-form-item label="津贴补贴" prop="allowance"><el-input-number v-model="adjustForm.allowance" :min="0" :precision="2" style="width:100%" /></el-form-item>
      </el-form>
      <div slot="footer"><el-button @click="adjustDialogVisible = false">取消</el-button><el-button type="primary" :loading="submitLoading" @click="handleAdjustSubmit">确定调薪</el-button></div>
    </el-dialog>

    <!-- 批量调薪对话框 -->
    <el-dialog title="批量调薪" :visible.sync="batchDialogVisible" width="500px" :close-on-click-modal="false">
      <p>已选择 <b>{{ multipleSelection.length }}</b> 名员工进行批量调薪</p>
      <el-form :model="batchForm" label-width="100px" style="margin-top:20px">
        <el-form-item label="调整方式"><el-radio-group v-model="batchForm.adjustType"><el-radio label="percent">按比例(%)</el-radio><el-radio label="fixed">固定金额</el-radio></el-radio-group></el-form-item>
        <el-form-item label="调整值"><el-input-number v-model="batchForm.adjustValue" :precision="2" style="width:100%" /></el-form-item>
      </el-form>
      <div slot="footer"><el-button @click="batchDialogVisible = false">取消</el-button><el-button type="primary" :loading="submitLoading" @click="handleBatchSubmit">确定</el-button></div>
    </el-dialog>
  </div>
</template>

<script>
import { compensationApi, organizationApi } from '@/api/enterprise/hr'

export default {
  name: 'CompensationManagement',
  data() {
    return {
      loading: false, submitLoading: false,
      queryForm: { employeeName: '', department: '', level: '' },
      compensationList: [], multipleSelection: [],
      departmentOptions: [], levelOptions: ['P1','P2','P3','P4','P5','P6','P7','P8','P9'],
      pagination: { currentPage: 1, pageSize: 20, total: 0 },
      dialogVisible: false, dialogMode: 'add', formData: {},
      formRules: { employeeName: [{ required: true, message: '请输入员工姓名', trigger: 'blur' }], employeeNo: [{ required: true, message: '请输入员工编号', trigger: 'blur' }] },
      adjustDialogVisible: false, adjustForm: { id: '', employeeName: '', employeeNo: '', currentTotal: 0, baseSalary: 0, performanceBonus: 0, allowance: 0 },
      batchDialogVisible: false, batchForm: { adjustType: 'percent', adjustValue: 0 }
    }
  },
  computed: { dialogTitle() { return { add: '新增薪酬方案', edit: '编辑薪酬方案', view: '薪酬详情' }[this.dialogMode] || '' } },
  mounted() { this.loadDepartmentOptions(); this.loadCompensationList() },
  methods: {
    async loadDepartmentOptions() { try { const res = await organizationApi.getDepartmentList({}); if (res && res.data) { const depts = []; const ex = l => { l.forEach(d => { if (d.deptName) depts.push(d.deptName); if (d.children) ex(d.children) }) }; ex(res.data || []); this.departmentOptions = [...new Set(depts)] } } catch (e) {} },
    async loadCompensationList() {
      this.loading = true
      try { const res = await compensationApi.getCompensationList({ ...this.queryForm, pageNumber: this.pagination.currentPage || 1, pageSize: this.pagination.pageSize || 15 }); if (res && res.data) { this.compensationList = res.data.tlist || []; this.pagination.total = res.data.totalRecord || 0 } else { this.compensationList = []; this.pagination.total = 0 } }
      catch (e) { this.compensationList = []; this.pagination.total = 0 } finally { this.loading = false }
    },
    formatMoney(v) { return v != null ? Number(v).toLocaleString() : '0' },
    handleSearch() { this.pagination.currentPage = 1; this.loadCompensationList() },
    handleReset() { this.$refs.queryForm.resetFields(); this.loadCompensationList() },
    handleAdd() { this.dialogMode = 'add'; this.formData = { enterpriseId: 'ent001', enterpriseName: '贵州国资投资集团', status: '正常', baseSalary: 0, performanceBonus: 0, allowance: 0, socialInsurance: 0, housingFund: 0 }; this.dialogVisible = true },
    handleView(row) { this.dialogMode = 'view'; this.formData = { ...row }; this.dialogVisible = true },
    handleEdit(row) { this.dialogMode = 'edit'; this.formData = { ...row }; this.dialogVisible = true },
    handleAdjust(row) { this.adjustForm = { id: row.id, employeeName: row.employeeName, employeeNo: row.employeeNo, currentTotal: row.totalSalary, baseSalary: row.baseSalary, performanceBonus: row.performanceBonus, allowance: row.allowance }; this.adjustDialogVisible = true },
    handleDelete(row) { this.$confirm('确认删除该薪酬记录吗？', '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }).then(async () => { try { await compensationApi.deleteCompensation(row.id); this.$message.success('删除成功'); this.loadCompensationList() } catch (e) { this.$message.error('删除失败') } }).catch(() => {}) },
    handleBatchAdjust() { if (this.multipleSelection.length === 0) { this.$message.warning('请选择要调薪的员工'); return }; this.batchForm = { adjustType: 'percent', adjustValue: 0 }; this.batchDialogVisible = true },
    handleExport() {
      this.$message.info('正在导出薪酬数据...')
      compensationApi.exportCompensation(this.queryForm).then(res => {
        if (res && res.data) {
          const blob = new Blob([res.data], { type: 'application/vnd.ms-excel' })
          const link = document.createElement('a')
          link.href = URL.createObjectURL(blob)
          link.download = '薪酬数据.xlsx'
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
        const total = (this.formData.baseSalary || 0) + (this.formData.performanceBonus || 0) + (this.formData.allowance || 0)
        this.formData.totalSalary = total
        if (this.dialogMode === 'add') { await compensationApi.addCompensation(this.formData); this.$message.success('新增成功') }
        else { await compensationApi.updateCompensation(this.formData.id, this.formData); this.$message.success('编辑成功') }
        this.dialogVisible = false; this.loadCompensationList()
      } catch (e) { this.$message.error(this.dialogMode === 'add' ? '新增失败' : '编辑失败') } finally { this.submitLoading = false }
    },
    async handleAdjustSubmit() {
      this.submitLoading = true
      try { await compensationApi.adjustSalary(this.adjustForm); this.$message.success('调薪成功'); this.adjustDialogVisible = false; this.loadCompensationList() }
      catch (e) { this.$message.error('调薪失败') } finally { this.submitLoading = false }
    },
    async handleBatchSubmit() {
      this.submitLoading = true
      try {
        for (const item of this.multipleSelection) {
          const data = { id: item.id, baseSalary: item.baseSalary, performanceBonus: item.performanceBonus, allowance: item.allowance }
          if (this.batchForm.adjustType === 'percent') { const r = 1 + this.batchForm.adjustValue / 100; data.baseSalary = Math.round(data.baseSalary * r * 100) / 100; data.performanceBonus = Math.round(data.performanceBonus * r * 100) / 100; data.allowance = Math.round(data.allowance * r * 100) / 100 }
          else { data.baseSalary += this.batchForm.adjustValue; data.performanceBonus += this.batchForm.adjustValue; data.allowance += this.batchForm.adjustValue }
          await compensationApi.adjustSalary(data)
        }
        this.$message.success('批量调薪成功'); this.batchDialogVisible = false; this.loadCompensationList()
      } catch (e) { this.$message.error('批量调薪失败') } finally { this.submitLoading = false }
    },
    handleSelectionChange(s) { this.multipleSelection = s },
    handleSizeChange(v) { this.pagination.pageSize = v; this.loadCompensationList() },
    handleCurrentChange(v) { this.pagination.currentPage = v; this.loadCompensationList() },
    getStatusType(s) { return { '正常': 'success', '调薪中': 'warning', '冻结': 'danger' }[s] || 'info' }
  }
}
</script>

<style lang="scss" scoped>
.compensation-management {
  .search-card, .action-card, .table-card { margin-bottom: 16px; }
  .salary-amount { color: #409EFF; font-weight: 500; }
  .total-salary { color: #E6A23C; font-weight: bold; font-size: 14px; }
  .pagination-container { margin-top: 20px; text-align: right; }
}
</style>
