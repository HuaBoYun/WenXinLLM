<template>
  <div class="employee-management">
    <!-- 搜索和操作区域 -->
    <div class="search-section">
      <el-form :model="queryForm" ref="queryForm" :inline="true" class="search-form">
        <el-form-item label="员工姓名" prop="employeeName">
          <el-input v-model="queryForm.employeeName" placeholder="请输入员工姓名" clearable style="width: 200px" />
        </el-form-item>
        <el-form-item label="部门" prop="department">
          <el-select v-model="queryForm.department" placeholder="请选择部门" clearable style="width: 200px">
            <el-option v-for="d in departmentOptions" :key="d" :label="d" :value="d" />
          </el-select>
        </el-form-item>
        <el-form-item label="在职状态" prop="status">
          <el-select v-model="queryForm.status" placeholder="请选择状态" clearable style="width: 150px">
            <el-option label="在职" value="在职" />
            <el-option label="离职" value="离职" />
            <el-option label="试用期" value="试用期" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
      <div class="action-buttons">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增员工</el-button>
        <el-button type="success" icon="el-icon-upload" @click="handleBatchImport">批量导入</el-button>
        <el-button type="warning" icon="el-icon-download" @click="handleExport">导出数据</el-button>
      </div>
    </div>

    <!-- 员工列表 -->
    <div class="table-section">
      <el-table :data="employeeList" v-loading="loading" stripe border style="width: 100%" @selection-change="handleSelectionChange">
        <el-table-column type="selection" width="55" />
        <el-table-column prop="employeeNo" label="员工编号" width="130" />
        <el-table-column prop="employeeName" label="姓名" width="100" />
        <el-table-column prop="gender" label="性别" width="70">
          <template slot-scope="scope">
            <el-tag :type="scope.row.gender === '男' ? 'primary' : 'success'" size="small">{{ scope.row.gender }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="department" label="部门" width="120" />
        <el-table-column prop="position" label="职位" width="120" />
        <el-table-column prop="salaryLevel" label="职级" width="80" />
        <el-table-column prop="education" label="学历" width="80" />
        <el-table-column prop="entryDate" label="入职日期" width="120" />
        <el-table-column prop="phone" label="联系电话" width="130" />
        <el-table-column prop="status" label="状态" width="90">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)" size="small">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="240" fixed="right">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleView(scope.row)">查看</el-button>
            <el-button type="text" size="small" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button type="text" size="small" @click="handleTransfer(scope.row)">调岗</el-button>
            <el-button type="text" size="small" style="color: #f56c6c" @click="handleDelete(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div class="pagination-section">
        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="pagination.currentPage" :page-sizes="[10, 20, 50, 100]" :page-size="pagination.pageSize" layout="total, sizes, prev, pager, next, jumper" :total="pagination.total" />
      </div>
    </div>

    <!-- 查看/编辑/新增 对话框 -->
    <el-dialog :title="dialogTitle" :visible.sync="dialogVisible" width="700px" :close-on-click-modal="false">
      <el-form :model="formData" ref="formRef" :rules="formRules" label-width="100px" :disabled="dialogMode === 'view'">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="员工姓名" prop="employeeName"><el-input v-model="formData.employeeName" placeholder="请输入员工姓名" /></el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="员工编号" prop="employeeNo"><el-input v-model="formData.employeeNo" placeholder="请输入员工编号" /></el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="性别" prop="gender"><el-select v-model="formData.gender" placeholder="请选择" style="width:100%"><el-option label="男" value="男" /><el-option label="女" value="女" /></el-select></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="部门" prop="department"><el-select v-model="formData.department" placeholder="请选择部门" style="width:100%"><el-option v-for="d in departmentOptions" :key="d" :label="d" :value="d" /></el-select></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="职位" prop="position"><el-input v-model="formData.position" placeholder="请输入职位" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="职级" prop="salaryLevel"><el-select v-model="formData.salaryLevel" placeholder="请选择职级" style="width:100%"><el-option v-for="l in ['P1','P2','P3','P4','P5','P6','P7','P8','P9']" :key="l" :label="l" :value="l" /></el-select></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="学历" prop="education"><el-select v-model="formData.education" placeholder="请选择学历" style="width:100%"><el-option v-for="e in ['博士','硕士','本科','大专','高中']" :key="e" :label="e" :value="e" /></el-select></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="在职状态" prop="status"><el-select v-model="formData.status" placeholder="请选择状态" style="width:100%"><el-option label="在职" value="在职" /><el-option label="离职" value="离职" /><el-option label="试用期" value="试用期" /></el-select></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="入职日期" prop="entryDate"><el-date-picker v-model="formData.entryDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" style="width:100%" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="合同到期"><el-date-picker v-model="formData.contractEndDate" type="date" value-format="yyyy-MM-dd" placeholder="选择日期" style="width:100%" /></el-form-item></el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12"><el-form-item label="联系电话" prop="phone"><el-input v-model="formData.phone" placeholder="请输入联系电话" /></el-form-item></el-col>
          <el-col :span="12"><el-form-item label="邮箱" prop="email"><el-input v-model="formData.email" placeholder="请输入邮箱" /></el-form-item></el-col>
        </el-row>
      </el-form>
      <div slot="footer" v-if="dialogMode !== 'view'">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleSubmit">确定</el-button>
      </div>
    </el-dialog>

    <!-- 调岗对话框 -->
    <el-dialog title="员工调岗" :visible.sync="transferDialogVisible" width="500px" :close-on-click-modal="false">
      <el-form :model="transferForm" ref="transferFormRef" label-width="100px">
        <el-form-item label="员工姓名"><span>{{ selectedEmployee && selectedEmployee.employeeName }}</span></el-form-item>
        <el-form-item label="当前部门"><span>{{ selectedEmployee && selectedEmployee.department }}</span></el-form-item>
        <el-form-item label="当前职位"><span>{{ selectedEmployee && selectedEmployee.position }}</span></el-form-item>
        <el-form-item label="新部门" prop="department"><el-select v-model="transferForm.department" placeholder="请选择新部门" style="width:100%"><el-option v-for="d in departmentOptions" :key="d" :label="d" :value="d" /></el-select></el-form-item>
        <el-form-item label="新职位" prop="position"><el-input v-model="transferForm.position" placeholder="请输入新职位" /></el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="transferDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="submitLoading" @click="handleTransferSubmit">确定</el-button>
      </div>
    </el-dialog>

    <!-- 批量导入对话框 -->
    <el-dialog title="批量导入员工" :visible.sync="importDialogVisible" width="550px" :close-on-click-modal="false">
      <div class="import-tips">
        <p><i class="el-icon-info"></i> 请按照模板格式准备Excel文件，支持 .xlsx 格式</p>
        <p>模板字段：员工姓名、员工编号、性别、部门、职位、职级、学历、入职日期、联系电话、邮箱</p>
      </div>
      <el-upload
        ref="importUpload"
        class="upload-area"
        drag
        action=""
        :auto-upload="false"
        :limit="1"
        accept=".xlsx,.xls"
        :on-change="handleImportFileChange"
        :on-exceed="() => $message.warning('只能上传一个文件')"
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">只能上传xlsx/xls文件</div>
      </el-upload>
      <div slot="footer">
        <el-button @click="importDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="importLoading" @click="handleImportSubmit" :disabled="!importFile">确定导入</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { employeeApi, organizationApi } from '@/api/enterprise/hr'

export default {
  name: 'EmployeeManagement',
  data() {
    return {
      loading: false,
      submitLoading: false,
      queryForm: { employeeName: '', department: '', status: '' },
      employeeList: [],
      departmentOptions: [],
      selectedEmployees: [],
      selectedEmployee: null,
      pagination: { currentPage: 1, pageSize: 20, total: 0 },
      // 对话框
      dialogVisible: false,
      dialogMode: 'add', // add/edit/view
      formData: {},
      formRules: {
        employeeName: [{ required: true, message: '请输入员工姓名', trigger: 'blur' }],
        employeeNo: [{ required: true, message: '请输入员工编号', trigger: 'blur' }],
        department: [{ required: true, message: '请选择部门', trigger: 'change' }],
        position: [{ required: true, message: '请输入职位', trigger: 'blur' }]
      },
      // 调岗
      transferDialogVisible: false,
      transferForm: { department: '', position: '' },
      // 批量导入
      importDialogVisible: false,
      importLoading: false,
      importFile: null
    }
  },
  computed: {
    dialogTitle() {
      const map = { add: '新增员工', edit: '编辑员工', view: '员工详情' }
      return map[this.dialogMode] || ''
    }
  },
  mounted() {
    this.loadDepartmentOptions()
    this.loadEmployeeList()
  },
  methods: {
    async loadDepartmentOptions() {
      try {
        const res = await organizationApi.getDepartmentList({})
        if (res && res.data) {
          const depts = []
          const extractDepts = (list) => { list.forEach(d => { if (d.deptName) depts.push(d.deptName); if (d.children) extractDepts(d.children) }) }
          extractDepts(res.data || [])
          this.departmentOptions = [...new Set(depts)]
        }
      } catch (e) { console.error('加载部门选项失败', e) }
    },
    async loadEmployeeList() {
      this.loading = true
      try {
        const res = await employeeApi.getEmployeeList({ ...this.queryForm, pageNumber: this.pagination.currentPage, pageSize: this.pagination.pageSize })
        if (res && res.data) {
          this.employeeList = res.data.tlist || []
          this.pagination.total = res.data.totalRecord || 0
        } else { this.employeeList = []; this.pagination.total = 0 }
      } catch (e) { console.error('加载员工列表失败:', e); this.$message.error('加载员工列表失败') }
      finally { this.loading = false }
    },
    handleSearch() { this.pagination.currentPage = 1; this.loadEmployeeList() },
    handleReset() { this.$refs.queryForm.resetFields(); this.loadEmployeeList() },
    handleAdd() { this.dialogMode = 'add'; this.formData = { enterpriseId: 'ent001', enterpriseName: '贵州国资投资集团', status: '在职' }; this.dialogVisible = true },
    handleView(row) { this.dialogMode = 'view'; this.formData = { ...row }; this.dialogVisible = true },
    handleEdit(row) { this.dialogMode = 'edit'; this.formData = { ...row }; this.dialogVisible = true },
    handleTransfer(row) { this.selectedEmployee = row; this.transferForm = { department: row.department, position: row.position }; this.transferDialogVisible = true },
    handleDelete(row) {
      this.$confirm('确认删除该员工吗？', '提示', { confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning' }).then(async () => {
        try { await employeeApi.deleteEmployee(row.id); this.$message.success('删除成功'); this.loadEmployeeList() } catch (e) { this.$message.error('删除失败') }
      }).catch(() => {})
    },
    async handleSubmit() {
      try { await this.$refs.formRef.validate() } catch (e) { return }
      this.submitLoading = true
      try {
        if (this.dialogMode === 'add') { await employeeApi.createEmployee(this.formData); this.$message.success('新增成功') }
        else { await employeeApi.updateEmployee(this.formData.id, this.formData); this.$message.success('编辑成功') }
        this.dialogVisible = false; this.loadEmployeeList()
      } catch (e) { this.$message.error(this.dialogMode === 'add' ? '新增失败' : '编辑失败') }
      finally { this.submitLoading = false }
    },
    async handleTransferSubmit() {
      this.submitLoading = true
      try { await employeeApi.transferEmployee(this.selectedEmployee.id, this.transferForm); this.$message.success('调岗成功'); this.transferDialogVisible = false; this.loadEmployeeList() }
      catch (e) { this.$message.error('调岗失败') }
      finally { this.submitLoading = false }
    },
    handleBatchImport() { this.importFile = null; this.importDialogVisible = true; this.$nextTick(() => { if (this.$refs.importUpload) this.$refs.importUpload.clearFiles() }) },
    handleImportFileChange(file) { this.importFile = file.raw },
    async handleImportSubmit() {
      if (!this.importFile) { this.$message.warning('请选择要导入的文件'); return }
      this.importLoading = true
      try {
        const formData = new FormData()
        formData.append('file', this.importFile)
        await employeeApi.batchImportEmployee(formData)
        this.$message.success('导入成功')
        this.importDialogVisible = false
        this.loadEmployeeList()
      } catch (e) { this.$message.error('导入失败：' + (e.message || '请检查文件格式')) }
      finally { this.importLoading = false }
    },
    async handleExport() {
      try {
        const res = await employeeApi.exportEmployee(this.queryForm)
        if (res && res.data) { const blob = new Blob([res.data], { type: 'application/vnd.ms-excel' }); const link = document.createElement('a'); link.href = URL.createObjectURL(blob); link.download = '员工数据.xlsx'; link.click(); URL.revokeObjectURL(link.href); this.$message.success('导出成功') }
      } catch (e) { this.$message.error('导出失败') }
    },
    handleSelectionChange(selection) { this.selectedEmployees = selection },
    handleSizeChange(val) { this.pagination.pageSize = val; this.loadEmployeeList() },
    handleCurrentChange(val) { this.pagination.currentPage = val; this.loadEmployeeList() },
    getStatusType(status) { return { '在职': 'success', '离职': 'danger', '试用期': 'warning' }[status] || 'info' }
  }
}
</script>

<style lang="scss" scoped>
.employee-management {
  .search-section {
    background: white; padding: 20px; border-radius: 8px; margin-bottom: 20px; box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    .search-form { margin-bottom: 20px; }
    .action-buttons { text-align: right; }
  }
  .table-section {
    background: white; padding: 20px; border-radius: 8px; box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    .pagination-section { margin-top: 20px; text-align: right; }
  }
  .import-tips {
    margin-bottom: 16px; padding: 12px; background: #f4f4f5; border-radius: 4px;
    p { margin: 4px 0; font-size: 13px; color: #606266; i { color: #409EFF; margin-right: 4px; } }
  }
  .upload-area { text-align: center; }
}
</style>
