<template>
  <div class="expense-parameter-container">
    <div class="search-container">
      <el-form :model="searchForm" ref="searchForm" :inline="true" class="search-form">
        <el-form-item label="参数名称" prop="parameterName">
          <el-input
            v-model="searchForm.parameterName"
            placeholder="请输入参数名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="参数类型" prop="parameterType">
          <el-select
            v-model="searchForm.parameterType"
            placeholder="请选择参数类型"
            clearable
            style="width: 150px"
          >
            <el-option label="系统参数" value="SYSTEM" />
            <el-option label="业务参数" value="BUSINESS" />
            <el-option label="流程参数" value="WORKFLOW" />
            <el-option label="界面参数" value="UI" />
            <el-option label="安全参数" value="SECURITY" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态" prop="isEnabled">
          <el-select
            v-model="searchForm.isEnabled"
            placeholder="请选择状态"
            clearable
            style="width: 120px"
          >
            <el-option label="启用" :value="true" />
            <el-option label="禁用" :value="false" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">查询</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <div class="toolbar">
      <el-button type="primary" @click="handleAdd">新增</el-button>
      <el-button type="danger" @click="handleBatchDelete" :disabled="!multipleSelection.length">
        批量删除
      </el-button>
    </div>

    <div class="table-container">
      <el-table
        :data="tableData"
        v-loading="loading"
        @selection-change="handleSelectionChange"
        stripe
        border
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="parameterCode" label="参数编码" width="150" />
        <el-table-column prop="parameterName" label="参数名称" min-width="200" />
        <el-table-column prop="parameterTypeName" label="参数类型" width="120" />
        <el-table-column prop="parameterValue" label="参数值" min-width="180" show-overflow-tooltip />
        <el-table-column prop="dataType" label="数据类型" width="100">
          <template slot-scope="scope">
            <el-tag size="small" :type="getDataTypeColor(scope.row.dataType)">
              {{ scope.row.dataTypeName }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="isRequired" label="必填" width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isRequired ? 'danger' : 'info'" size="small">
              {{ scope.row.isRequired ? '是' : '否' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="isEditable" label="可编辑" width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isEditable ? 'success' : 'warning'" size="small">
              {{ scope.row.isEditable ? '是' : '否' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="isEnabled" label="状态" width="80">
          <template slot-scope="scope">
            <el-tag :type="scope.row.isEnabled ? 'success' : 'danger'">
              {{ scope.row.isEnabled ? '启用' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="orgName" label="所属组织" width="120" />
        <el-table-column prop="createTime" label="创建时间" width="160" />
        <el-table-column label="操作" width="350" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="info" @click="handleOptions(scope.row)">选项配置</el-button>
            <el-button size="mini" type="success" @click="handlePermissions(scope.row)">权限配置</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
            <el-button 
              size="mini" 
              :type="scope.row.isEnabled ? 'warning' : 'success'"
              @click="handleToggleStatus(scope.row)"
            >
              {{ scope.row.isEnabled ? '禁用' : '启用' }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-container">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pagination.currentPage"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pagination.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
        />
      </div>
    </div>

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="800px"
      @close="handleDialogClose"
    >
      <el-form
        :model="formData"
        :rules="formRules"
        ref="formRef"
        label-width="120px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="参数编码" prop="parameterCode">
              <el-input v-model="formData.parameterCode" placeholder="请输入参数编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="参数名称" prop="parameterName">
              <el-input v-model="formData.parameterName" placeholder="请输入参数名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="参数类型" prop="parameterType">
              <el-select v-model="formData.parameterType" placeholder="请选择参数类型" style="width: 100%">
                <el-option label="系统参数" value="SYSTEM" />
                <el-option label="业务参数" value="BUSINESS" />
                <el-option label="流程参数" value="WORKFLOW" />
                <el-option label="界面参数" value="UI" />
                <el-option label="安全参数" value="SECURITY" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据类型" prop="dataType">
              <el-select v-model="formData.dataType" placeholder="请选择数据类型" style="width: 100%">
                <el-option label="字符串" value="STRING" />
                <el-option label="数字" value="NUMBER" />
                <el-option label="布尔值" value="BOOLEAN" />
                <el-option label="日期" value="DATE" />
                <el-option label="JSON" value="JSON" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="参数值" prop="parameterValue">
          <el-input v-model="formData.parameterValue" placeholder="请输入参数值" />
        </el-form-item>
        <el-form-item label="默认值" prop="defaultValue">
          <el-input v-model="formData.defaultValue" placeholder="请输入默认值" />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="是否必填">
              <el-switch v-model="formData.isRequired" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="是否可编辑">
              <el-switch v-model="formData.isEditable" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="是否启用">
              <el-switch v-model="formData.isEnabled" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="验证规则" prop="validationRule">
          <el-input v-model="formData.validationRule" placeholder="请输入验证规则（正则表达式）" />
        </el-form-item>
        <el-form-item label="参数描述" prop="description">
          <el-input
            v-model="formData.description"
            type="textarea"
            :rows="3"
            placeholder="请输入参数描述"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave" :loading="saveLoading">保存</el-button>
      </div>
    </el-dialog>

    <!-- 选项配置弹窗 -->
    <options-dialog
      :visible.sync="optionsDialogVisible"
      :parameter-id="currentParameterId"
      @close="handleOptionsDialogClose"
    />

    <!-- 权限配置弹窗 -->
    <permissions-dialog
      :visible.sync="permissionsDialogVisible"
      :parameter-id="currentParameterId"
      @close="handlePermissionsDialogClose"
    />
  </div>
</template>

<script>
import { expenseParameterApi } from '@/api/financialSharing/baseConfig'
import OptionsDialog from './OptionsDialog.vue'
import PermissionsDialog from './PermissionsDialog.vue'

export default {
  name: 'ExpenseParameter',
  components: {
    OptionsDialog,
    PermissionsDialog
  },
  data() {
    return {
      loading: false,
      saveLoading: false,
      tableData: [],
      multipleSelection: [],
      searchForm: {
        parameterName: '',
        parameterType: '',
        isEnabled: null
      },
      pagination: {
        currentPage: 1,
        pageSize: 15,
        total: 0
      },
      dialogVisible: false,
      dialogTitle: '新增报账参数',
      formData: {
        parameterId: null,
        parameterCode: '',
        parameterName: '',
        parameterType: '',
        dataType: '',
        parameterValue: '',
        defaultValue: '',
        isRequired: false,
        isEditable: true,
        isEnabled: true,
        validationRule: '',
        description: ''
      },
      optionsDialogVisible: false,
      permissionsDialogVisible: false,
      currentParameterId: '',
      formRules: {
        parameterCode: [
          { required: true, message: '请输入参数编码', trigger: 'blur' }
        ],
        parameterName: [
          { required: true, message: '请输入参数名称', trigger: 'blur' }
        ],
        parameterType: [
          { required: true, message: '请选择参数类型', trigger: 'change' }
        ],
        dataType: [
          { required: true, message: '请选择数据类型', trigger: 'change' }
        ]
      }
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    async loadData() {
      this.loading = true
      try {
        const params = {
          page: this.pagination.currentPage - 1,
          size: this.pagination.pageSize,
          ...this.searchForm
        }
        const response = await expenseParameterApi.getList(params)
        if (response.code === 1) {
          this.tableData = response.data.tlist || []
          this.pagination.total = response.data.totalRecord || 0
        } else {
          this.$message.error(response.msg || '查询失败')
        }
      } catch (error) {
        this.$message.error('查询失败：' + error.message)
      } finally {
        this.loading = false
      }
    },
    handleSearch() {
      this.pagination.currentPage = 1
      this.loadData()
    },
    handleReset() {
      this.$refs.searchForm.resetFields()
      this.handleSearch()
    },
    handleAdd() {
      this.dialogTitle = '新增报账参数'
      this.formData = {
        parameterId: null,
        parameterCode: '',
        parameterName: '',
        parameterType: '',
        dataType: '',
        parameterValue: '',
        defaultValue: '',
        isRequired: false,
        isEditable: true,
        isEnabled: true,
        validationRule: '',
        description: ''
      }
      this.dialogVisible = true
    },
    handleEdit(row) {
      this.dialogTitle = '编辑报账参数'
      this.formData = { ...row }
      this.dialogVisible = true
    },
    async handleSave() {
      try {
        await this.$refs.formRef.validate()
        this.saveLoading = true
        const response = await expenseParameterApi.save(this.formData)
        if (response.code === 1) {
          this.$message.success('保存成功')
          this.dialogVisible = false
          this.loadData()
        } else {
          this.$message.error(response.msg || '保存失败')
        }
      } catch (error) {
        this.$message.error('保存失败：' + error.message)
      } finally {
        this.saveLoading = false
      }
    },
    async handleDelete(row) {
      try {
        await this.$confirm('确定要删除该报账参数吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const response = await expenseParameterApi.delete(row.parameterId)
        if (response.code === 1) {
          this.$message.success('删除成功')
          this.loadData()
        } else {
          this.$message.error(response.msg || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败：' + error.message)
        }
      }
    },
    async handleBatchDelete() {
      try {
        await this.$confirm('确定要删除选中的报账参数吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        const parameterIds = this.multipleSelection.map(item => item.parameterId)
        const response = await expenseParameterApi.batchDelete(parameterIds)
        if (response.code === 1) {
          this.$message.success('批量删除成功')
          this.loadData()
        } else {
          this.$message.error(response.msg || '批量删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('批量删除失败：' + error.message)
        }
      }
    },
    async handleToggleStatus(row) {
      try {
        // 将布尔值转换为 0 或 1
        const isEnabled = !row.isEnabled ? 1 : 0
        const response = await expenseParameterApi.updateStatus(row.parameterId, isEnabled)
        if (response.code === 1) {
          this.$message.success('状态更新成功')
          this.loadData()
        } else {
          this.$message.error(response.msg || '状态更新失败')
        }
      } catch (error) {
        this.$message.error('状态更新失败：' + error.message)
      }
    },
    handleOptions(row) {
      console.log('点击了选项配置按钮', row.parameterId)
      this.currentParameterId = row.parameterId
      this.optionsDialogVisible = true
    },
    handlePermissions(row) {
      console.log('点击了权限配置按钮', row.parameterId)
      console.log('permissionsDialogVisible 当前值:', this.permissionsDialogVisible)

      // 强制重置，确保能触发 watch
      if (this.permissionsDialogVisible) {
        console.log('检测到 permissionsDialogVisible 已经是 true，先重置为 false')
        this.permissionsDialogVisible = false
        this.$nextTick(() => {
          console.log('nextTick 后重新设置为 true')
          this.currentParameterId = row.parameterId
          this.permissionsDialogVisible = true
          console.log('permissionsDialogVisible 设置后:', this.permissionsDialogVisible)
        })
      } else {
        this.currentParameterId = row.parameterId
        this.permissionsDialogVisible = true
        console.log('permissionsDialogVisible 设置后:', this.permissionsDialogVisible)
      }
    },
    handleOptionsDialogClose() {
      this.optionsDialogVisible = false
      this.currentParameterId = ''
    },
    handlePermissionsDialogClose() {
      console.log('handlePermissionsDialogClose 被调用')
      console.log('关闭前 permissionsDialogVisible:', this.permissionsDialogVisible)
      this.permissionsDialogVisible = false
      this.currentParameterId = ''
      console.log('关闭后 permissionsDialogVisible:', this.permissionsDialogVisible)
    },
    getDataTypeColor(dataType) {
      const colors = {
        'STRING': 'primary',
        'NUMBER': 'success',
        'BOOLEAN': 'warning',
        'DATE': 'info',
        'JSON': 'danger'
      }
      return colors[dataType] || 'default'
    },
    handleSelectionChange(selection) {
      this.multipleSelection = selection
    },
    handleSizeChange(size) {
      this.pagination.pageSize = size
      this.loadData()
    },
    handleCurrentChange(page) {
      this.pagination.currentPage = page
      this.loadData()
    },
    handleDialogClose() {
      this.$refs.formRef.resetFields()
    }
  }
}
</script>

<style scoped>
.expense-parameter-container {
  padding: 20px;
}

.search-container {
  background: #fff;
  padding: 20px;
  margin-bottom: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.toolbar {
  margin-bottom: 20px;
}

.table-container {
  background: #fff;
  padding: 20px;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}

.dialog-footer {
  text-align: right;
}
</style>
