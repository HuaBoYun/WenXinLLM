<template>
  <div class="auxiliary-accounting-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1 class="page-title">
          <i class="el-icon-s-grid"></i>
          辅助核算项目管理
        </h1>
        <p class="page-description">管理部门、职员、客户、供应商、项目等辅助核算项目的基础信息</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">
          新增项目
        </el-button>
        <el-button type="success" icon="el-icon-download" @click="handleExport">
          导出数据
        </el-button>
        <el-button type="warning" icon="el-icon-upload2" @click="handleImport">
          批量导入
        </el-button>
      </div>
    </div>

    <!-- 标签页切换不同类型 -->
    <div class="tab-container">
      <el-tabs v-model="activeTab" @tab-click="handleTabChange">
        <el-tab-pane label="部门核算" name="department">
          <DepartmentTable
            ref="departmentTable"
            :table-data="departmentList"
            :loading="loading"
            @edit="handleEdit"
            @delete="handleDelete"
            @refresh="loadDepartmentList" />
        </el-tab-pane>
        <el-tab-pane label="职员核算" name="employee">
          <EmployeeTable
            ref="employeeTable"
            :table-data="employeeList"
            :loading="loading"
            @edit="handleEdit"
            @delete="handleDelete"
            @refresh="loadEmployeeList" />
        </el-tab-pane>
        <el-tab-pane label="客户核算" name="customer">
          <CustomerTable
            ref="customerTable"
            :table-data="customerList"
            :loading="loading"
            @edit="handleEdit"
            @delete="handleDelete"
            @refresh="loadCustomerList" />
        </el-tab-pane>
        <el-tab-pane label="供应商核算" name="supplier">
          <SupplierTable
            ref="supplierTable"
            :table-data="supplierList"
            :loading="loading"
            @edit="handleEdit"
            @delete="handleDelete"
            @refresh="loadSupplierList" />
        </el-tab-pane>
        <el-tab-pane label="项目核算" name="project">
          <ProjectTable
            ref="projectTable"
            :table-data="projectList"
            :loading="loading"
            @edit="handleEdit"
            @delete="handleDelete"
            @refresh="loadProjectList" />
        </el-tab-pane>
      </el-tabs>
    </div>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="600px"
      :close-on-click-modal="false">
      <!-- 部门表单 -->
      <DepartmentForm
        v-if="activeTab === 'department'"
        ref="departmentForm"
        :form-data="formData"
        :is-edit="isEdit" />

      <!-- 职员表单 -->
      <EmployeeForm
        v-else-if="activeTab === 'employee'"
        ref="employeeForm"
        :form-data="formData"
        :is-edit="isEdit" />

      <!-- 客户表单 -->
      <CustomerForm
        v-else-if="activeTab === 'customer'"
        ref="customerForm"
        :form-data="formData"
        :is-edit="isEdit" />

      <!-- 供应商表单 -->
      <SupplierForm
        v-else-if="activeTab === 'supplier'"
        ref="supplierForm"
        :form-data="formData"
        :is-edit="isEdit" />

      <!-- 项目表单 -->
      <ProjectForm
        v-else-if="activeTab === 'project'"
        ref="projectForm"
        :form-data="formData"
        :is-edit="isEdit" />

      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleSubmit">确 定</el-button>
      </span>
    </el-dialog>

    <!-- 批量导入对话框 -->
    <el-dialog
      title="批量导入"
      :visible.sync="importDialogVisible"
      width="500px"
      :close-on-click-modal="false">
      <el-form label-width="100px">
        <el-form-item label="数据类型">
          <el-select v-model="importType" placeholder="请选择数据类型">
            <el-option label="部门" value="department"></el-option>
            <el-option label="职员" value="employee"></el-option>
            <el-option label="客户" value="customer"></el-option>
            <el-option label="供应商" value="supplier"></el-option>
            <el-option label="项目" value="project"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="上传文件">
          <el-upload
            class="upload-demo"
            drag
            action=""
            :auto-upload="false"
            :on-change="handleFileChange"
            :limit="1"
            accept=".xlsx,.xls">
            <i class="el-icon-upload"></i>
            <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
            <div class="el-upload__tip" slot="tip">只能上传xlsx/xls文件，且不超过10MB</div>
          </el-upload>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="importDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="handleImportSubmit">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import DepartmentTable from './components/DepartmentTable'
import EmployeeTable from './components/EmployeeTable'
import CustomerTable from './components/CustomerTable'
import SupplierTable from './components/SupplierTable'
import ProjectTable from './components/ProjectTable'
import DepartmentForm from './forms/DepartmentForm'
import EmployeeForm from './forms/EmployeeForm'
import CustomerForm from './forms/CustomerForm'
import SupplierForm from './forms/SupplierForm'
import ProjectForm from './forms/ProjectForm'
import { getAuxiliaryItemPage, saveOrUpdateAuxiliaryItem, deleteAuxiliaryItem } from '@/api/financialSharing/system'

export default {
  name: 'AuxiliaryAccountingIndex',
  components: {
    DepartmentTable,
    EmployeeTable,
    CustomerTable,
    SupplierTable,
    ProjectTable,
    DepartmentForm,
    EmployeeForm,
    CustomerForm,
    SupplierForm,
    ProjectForm
  },
  data() {
    return {
      loading: false,
      activeTab: 'department',
      dialogVisible: false,
      importDialogVisible: false,
      isEdit: false,
      departmentList: [],
      employeeList: [],
      customerList: [],
      supplierList: [],
      projectList: [],
      formData: {},
      importType: '',
      importFile: null
    }
  },
  computed: {
    dialogTitle() {
      const titles = {
        department: '部门',
        employee: '职员',
        customer: '客户',
        supplier: '供应商',
        project: '项目'
      }
      return `${this.isEdit ? '编辑' : '新增'}${titles[this.activeTab]}`
    }
  },
  mounted() {
    this.loadDepartmentList()
  },
  methods: {
    async loadDepartmentList() {
      this.loading = true
      try {
        const response = await getAuxiliaryItemPage({
          auxiliaryType: 'DEPARTMENT',
          pageNo: 1,
          pageSize: 100
        })
        if (response.code === 1) {
          this.departmentList = response.data.tlist || []
        } else {
          this.$message.error('加载部门数据失败：' + response.msg)
        }
      } catch (error) {
        this.$message.error('加载部门数据失败：' + error.message)
      } finally {
        this.loading = false
      }
    },
    async loadEmployeeList() {
      this.loading = true
      try {
        const response = await getAuxiliaryItemPage({
          auxiliaryType: 'EMPLOYEE',
          pageNo: 1,
          pageSize: 100
        })
        if (response.code === 1) {
          this.employeeList = response.data.tlist || []
        } else {
          this.$message.error('加载职员数据失败：' + response.msg)
        }
      } catch (error) {
        this.$message.error('加载职员数据失败：' + error.message)
      } finally {
        this.loading = false
      }
    },
    async loadCustomerList() {
      this.loading = true
      try {
        const response = await getAuxiliaryItemPage({
          auxiliaryType: 'CUSTOMER',
          pageNo: 1,
          pageSize: 100
        })
        if (response.code === 1) {
          this.customerList = response.data.tlist || []
        } else {
          this.$message.error('加载客户数据失败：' + response.msg)
        }
      } catch (error) {
        this.$message.error('加载客户数据失败：' + error.message)
      } finally {
        this.loading = false
      }
    },
    async loadSupplierList() {
      this.loading = true
      try {
        const response = await getAuxiliaryItemPage({
          auxiliaryType: 'SUPPLIER',
          pageNo: 1,
          pageSize: 100
        })
        if (response.code === 1) {
          this.supplierList = response.data.tlist || []
        } else {
          this.$message.error('加载供应商数据失败：' + response.msg)
        }
      } catch (error) {
        this.$message.error('加载供应商数据失败：' + error.message)
      } finally {
        this.loading = false
      }
    },
    async loadProjectList() {
      this.loading = true
      try {
        const response = await getAuxiliaryItemPage({
          auxiliaryType: 'PROJECT',
          pageNo: 1,
          pageSize: 100
        })
        if (response.code === 1) {
          this.projectList = response.data.tlist || []
        } else {
          this.$message.error('加载项目数据失败：' + response.msg)
        }
      } catch (error) {
        this.$message.error('加载项目数据失败：' + error.message)
      } finally {
        this.loading = false
      }
    },
    handleTabChange(tab) {
      const tabName = tab.name
      switch (tabName) {
        case 'department':
          if (this.departmentList.length === 0) {
            this.loadDepartmentList()
          }
          break
        case 'employee':
          if (this.employeeList.length === 0) {
            this.loadEmployeeList()
          }
          break
        case 'customer':
          if (this.customerList.length === 0) {
            this.loadCustomerList()
          }
          break
        case 'supplier':
          if (this.supplierList.length === 0) {
            this.loadSupplierList()
          }
          break
        case 'project':
          if (this.projectList.length === 0) {
            this.loadProjectList()
          }
          break
      }
    },
    handleAdd() {
      this.isEdit = false
      this.formData = {}
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.isEdit = true
      this.formData = { ...row }
      this.dialogVisible = true
    },
    async handleDelete(row) {
      try {
        await this.$confirm(`确定要删除项目"${row.itemName || row.name}"吗？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const response = await deleteAuxiliaryItem(row.itemId || row.id)
        if (response.code === 1) {
          this.$message.success('删除成功')
          this.refreshCurrentTab()
        } else {
          this.$message.error('删除失败：' + response.msg)
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败：' + error.message)
        }
      }
    },
    async handleSubmit() {
      // 根据当前标签页获取表单组件并验证
      let formRef = null
      switch (this.activeTab) {
        case 'department':
          formRef = this.$refs.departmentForm
          break
        case 'employee':
          formRef = this.$refs.employeeForm
          break
        case 'customer':
          formRef = this.$refs.customerForm
          break
        case 'supplier':
          formRef = this.$refs.supplierForm
          break
        case 'project':
          formRef = this.$refs.projectForm
          break
      }

      if (formRef) {
        try {
          await formRef.validate()

          // 准备提交数据
          const submitData = {
            ...this.formData,
            auxiliaryType: this.getAuxiliaryTypeByTab()
          }

          const response = await saveOrUpdateAuxiliaryItem(submitData)
          if (response.code === 1) {
            this.$message.success(this.isEdit ? '修改成功' : '新增成功')
            this.dialogVisible = false
            this.refreshCurrentTab()
          } else {
            this.$message.error('保存失败：' + response.msg)
          }
        } catch (error) {
          if (error !== false) {
            this.$message.error('表单验证失败')
          }
        }
      }
    },
    getAuxiliaryTypeByTab() {
      const typeMap = {
        department: 'DEPARTMENT',
        employee: 'EMPLOYEE',
        customer: 'CUSTOMER',
        supplier: 'SUPPLIER',
        project: 'PROJECT'
      }
      return typeMap[this.activeTab] || 'OTHER'
    },
    refreshCurrentTab() {
      // 刷新当前标签页的数据
      switch (this.activeTab) {
        case 'department':
          this.loadDepartmentList()
          break
        case 'employee':
          this.loadEmployeeList()
          break
        case 'customer':
          this.loadCustomerList()
          break
        case 'supplier':
          this.loadSupplierList()
          break
        case 'project':
          this.loadProjectList()
          break
      }
    },
    handleExport() {
      // 导出当前标签页的数据
      this.$message.success('导出成功')
    },
    handleImport() {
      this.importType = this.activeTab
      this.importFile = null
      this.importDialogVisible = true
    },
    handleFileChange(file) {
      this.importFile = file.raw
    },
    handleImportSubmit() {
      if (!this.importFile) {
        this.$message.error('请选择要导入的文件')
        return
      }
      // 处理文件上传
      this.$message.success('导入成功')
      this.importDialogVisible = false
      this.refreshCurrentTab()
    }
  }
}
</script>

<style lang="scss" scoped>
.auxiliary-accounting-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: calc(100vh - 84px);
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  background: white;
  border-radius: 12px;
  padding: 24px;
  margin-bottom: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

  .header-left {
    .page-title {
      font-size: 24px;
      font-weight: 600;
      color: #303133;
      margin: 0 0 8px 0;
      display: flex;
      align-items: center;

      i {
        margin-right: 12px;
        color: #409eff;
      }
    }

    .page-description {
      color: #606266;
      font-size: 14px;
      margin: 0;
    }
  }

  .header-right {
    .el-button {
      margin-left: 12px;
    }
  }
}

.tab-container {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.upload-demo {
  text-align: center;
}

::v-deep .el-tabs__content {
  padding: 20px 0;
}
</style>