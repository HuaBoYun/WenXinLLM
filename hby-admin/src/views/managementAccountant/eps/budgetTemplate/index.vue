<template>
  <div class="app-container">
    <!-- 查询条件 -->
    <div class="filter-container">
      <el-form :inline="true" :model="queryParams" class="demo-form-inline">
        <el-form-item label="模板名称">
          <el-input
            v-model="queryParams.templateName"
            placeholder="请输入模板名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="模板类型">
          <el-select v-model="queryParams.templateType" placeholder="请选择模板类型" clearable>
            <el-option label="标准模板" value="STANDARD" />
            <el-option label="自定义模板" value="CUSTOM" />
            <el-option label="行业模板" value="INDUSTRY" />
          </el-select>
        </el-form-item>
        <el-form-item label="模板分类">
          <el-select v-model="queryParams.templateCategory" placeholder="请选择模板分类" clearable>
            <el-option label="收入预算" value="REVENUE" />
            <el-option label="成本预算" value="COST" />
            <el-option label="费用预算" value="EXPENSE" />
            <el-option label="资本预算" value="CAPEX" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
            <el-option label="草稿" value="DRAFT" />
            <el-option label="激活" value="ACTIVE" />
            <el-option label="停用" value="INACTIVE" />
            <el-option label="归档" value="ARCHIVED" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
          <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 操作按钮 -->
    <div class="operation-container">
      <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增</el-button>
      <el-button
        type="danger"
        icon="el-icon-delete"
        :disabled="multipleSelection.length === 0"
        @click="handleBatchDelete"
      >
        批量删除
      </el-button>
      <el-button type="success" icon="el-icon-upload2" @click="handleImport">导入</el-button>
      <el-button type="info" icon="el-icon-download" @click="handleExport">导出</el-button>
    </div>

    <!-- 数据表格 -->
    <el-table
      v-loading="loading"
      :data="budgetTemplateList"
      @selection-change="handleSelectionChange"
      style="width: 100%"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column prop="templateCode" label="模板编码" width="120" />
      <el-table-column prop="templateName" label="模板名称" width="200" show-overflow-tooltip />
      <el-table-column prop="templateType" label="模板类型" width="100">
        <template slot-scope="scope">
          <el-tag :type="getTemplateTypeTagType(scope.row.templateType)">
            {{ getTemplateTypeText(scope.row.templateType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="templateCategory" label="模板分类" width="100">
        <template slot-scope="scope">
          <el-tag :type="getTemplateCategoryTagType(scope.row.templateCategory)">
            {{ getTemplateCategoryText(scope.row.templateCategory) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="usageCount" label="使用次数" width="80" />
      <el-table-column prop="isDefault" label="默认模板" width="80">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.isDefault === 1" type="success" size="mini">是</el-tag>
          <el-tag v-else type="info" size="mini">否</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="isShared" label="共享" width="60">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.isShared === 1" type="success" size="mini">是</el-tag>
          <el-tag v-else type="info" size="mini">否</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="80">
        <template slot-scope="scope">
          <el-tag :type="getStatusTagType(scope.row.status)" size="mini">
            {{ getStatusText(scope.row.status) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createdTime" label="创建时间" width="160" />
      <el-table-column label="操作" width="300" fixed="right">
        <template slot-scope="scope">
          <el-button size="mini" type="text" @click="handleView(scope.row)">查看</el-button>
          <el-button size="mini" type="text" @click="handleEdit(scope.row)">编辑</el-button>
          <el-button size="mini" type="text" @click="handleCopy(scope.row)">复制</el-button>
          <el-button size="mini" type="text" @click="handleDesign(scope.row)">设计</el-button>
          <el-dropdown @command="handleCommand" style="margin-left: 10px">
            <el-button size="mini" type="text">
              更多<i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item
                v-if="scope.row.status === 'DRAFT'"
                :command="{action: 'activate', row: scope.row}"
              >
                激活
              </el-dropdown-item>
              <el-dropdown-item
                v-if="scope.row.status === 'ACTIVE'"
                :command="{action: 'deactivate', row: scope.row}"
              >
                停用
              </el-dropdown-item>
              <el-dropdown-item
                v-if="scope.row.status !== 'ARCHIVED'"
                :command="{action: 'archive', row: scope.row}"
              >
                归档
              </el-dropdown-item>
              <el-dropdown-item
                v-if="scope.row.isDefault !== 1"
                :command="{action: 'setDefault', row: scope.row}"
              >
                设为默认
              </el-dropdown-item>
              <el-dropdown-item
                :command="{action: 'export', row: scope.row}"
              >
                导出模板
              </el-dropdown-item>
              <el-dropdown-item
                :command="{action: 'delete', row: scope.row}"
                divided
              >
                删除
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.current"
      :limit.sync="queryParams.size"
      @pagination="getList"
    />

    <!-- 新增/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="800px"
      :close-on-click-modal="false"
    >
      <budget-template-form
        ref="budgetTemplateForm"
        :form-data="formData"
        :is-edit="isEdit"
        :is-view="dialogTitle.includes('查看')"
        @submit="handleSubmit"
        @cancel="handleCancel"
      />
    </el-dialog>

    <!-- 复制对话框 -->
    <el-dialog
      title="复制预算模板"
      :visible.sync="copyDialogVisible"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form ref="copyForm" :model="copyForm" :rules="copyRules" label-width="120px">
        <el-form-item label="目标模板编码" prop="targetTemplateCode">
          <el-input v-model="copyForm.targetTemplateCode" placeholder="请输入目标模板编码" />
        </el-form-item>
        <el-form-item label="目标模板名称" prop="targetTemplateName">
          <el-input v-model="copyForm.targetTemplateName" placeholder="请输入目标模板名称" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="copyDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleCopySubmit">确定</el-button>
      </div>
    </el-dialog>

    <!-- 模板设计器对话框 -->
    <el-dialog
      title="模板设计器"
      :visible.sync="designDialogVisible"
      width="90%"
      :close-on-click-modal="false"
      fullscreen
    >
      <template-designer
        ref="templateDesigner"
        :template-data="designTemplateData"
        @save="handleDesignSave"
        @cancel="handleDesignCancel"
      />
    </el-dialog>
  </div>
</template>

<script>
import { getBudgetTemplatePage, deleteBudgetTemplate, batchDeleteBudgetTemplate, activateBudgetTemplate, deactivateBudgetTemplate, archiveBudgetTemplate, setDefaultBudgetTemplate, copyBudgetTemplate, exportTemplate } from '@/api/managementAccountant/eps/budgetTemplate'
import BudgetTemplateForm from './components/BudgetTemplateForm'
import TemplateDesigner from './components/TemplateDesigner'
import Pagination from '@/components/Pagination'

export default {
  name: 'BudgetTemplate',
  components: {
    BudgetTemplateForm,
    TemplateDesigner,
    Pagination
  },
  data() {
    return {
      loading: false,
      budgetTemplateList: [],
      total: 0,
      multipleSelection: [],
      queryParams: {
        current: 1,
        size: 10,
        templateName: '',
        templateType: '',
        templateCategory: '',
        systemId: null,
        status: ''
      },
      dialogVisible: false,
      dialogTitle: '',
      isEdit: false,
      formData: {},
      copyDialogVisible: false,
      copyForm: {
        sourceTemplateId: null,
        targetTemplateCode: '',
        targetTemplateName: ''
      },
      copyRules: {
        targetTemplateCode: [
          { required: true, message: '请输入目标模板编码', trigger: 'blur' }
        ],
        targetTemplateName: [
          { required: true, message: '请输入目标模板名称', trigger: 'blur' }
        ]
      },
      designDialogVisible: false,
      designTemplateData: {}
    }
  },
  created() {
    this.getList()
  },
  methods: {
    // 获取列表数据
    getList() {
      this.loading = true
      getBudgetTemplatePage(this.queryParams).then(response => {
        if (response.code === 1) {
          this.budgetTemplateList = response.data.records
          this.total = response.data.total
        } else {
          this.$message.error(response.msg || '查询失败')
        }
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },

    // 查询
    handleQuery() {
      this.queryParams.current = 1
      this.getList()
    },

    // 重置查询
    resetQuery() {
      this.queryParams = {
        current: 1,
        size: 10,
        templateName: '',
        templateType: '',
        templateCategory: '',
        systemId: null,
        status: ''
      }
      this.getList()
    },

    // 新增
    handleAdd() {
      this.dialogTitle = '新增预算模板'
      this.isEdit = false
      this.formData = {}
      this.dialogVisible = true
    },

    // 编辑
    handleEdit(row) {
      this.dialogTitle = '编辑预算模板'
      this.isEdit = true
      this.formData = { ...row }
      this.dialogVisible = true
    },

    // 查看
    handleView(row) {
      this.dialogTitle = '查看预算模板'
      this.isEdit = false
      this.formData = { ...row }
      this.dialogVisible = true
    },

    // 复制
    handleCopy(row) {
      this.copyForm.sourceTemplateId = row.templateId
      this.copyForm.targetTemplateCode = ''
      this.copyForm.targetTemplateName = ''
      this.copyDialogVisible = true
    },

    // 设计
    handleDesign(row) {
      this.designTemplateData = { ...row }
      this.designDialogVisible = true
    },

    // 复制提交
    handleCopySubmit() {
      this.$refs.copyForm.validate(valid => {
        if (valid) {
          copyBudgetTemplate(this.copyForm).then(response => {
            if (response.code === 1) {
              this.$message.success('复制成功')
              this.copyDialogVisible = false
              this.getList()
            } else {
              this.$message.error(response.msg || '复制失败')
            }
          })
        }
      })
    },

    // 表单提交
    handleSubmit() {
      this.dialogVisible = false
      this.getList()
    },

    // 取消
    handleCancel() {
      this.dialogVisible = false
    },

    // 设计保存
    handleDesignSave() {
      this.designDialogVisible = false
      this.getList()
    },

    // 设计取消
    handleDesignCancel() {
      this.designDialogVisible = false
    },

    // 多选
    handleSelectionChange(selection) {
      this.multipleSelection = selection
    },

    // 批量删除
    handleBatchDelete() {
      if (this.multipleSelection.length === 0) {
        this.$message.warning('请选择要删除的数据')
        return
      }
      this.$confirm('确认删除选中的预算模板吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const templateIds = this.multipleSelection.map(item => item.templateId)
        batchDeleteBudgetTemplate(templateIds).then(response => {
          if (response.code === 1) {
            this.$message.success('删除成功')
            this.getList()
          } else {
            this.$message.error(response.msg || '删除失败')
          }
        })
      })
    },

    // 导入
    handleImport() {
      this.$message.info('导入功能开发中...')
    },

    // 导出
    handleExport() {
      this.$message.info('导出功能开发中...')
    },

    // 下拉菜单命令处理
    handleCommand(command) {
      const { action, row } = command
      switch (action) {
        case 'activate':
          this.handleActivate(row)
          break
        case 'deactivate':
          this.handleDeactivate(row)
          break
        case 'archive':
          this.handleArchive(row)
          break
        case 'setDefault':
          this.handleSetDefault(row)
          break
        case 'export':
          this.handleExportTemplate(row)
          break
        case 'delete':
          this.handleDelete(row)
          break
      }
    },

    // 激活
    handleActivate(row) {
      this.$confirm('确认激活该预算模板吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        activateBudgetTemplate(row.templateId).then(response => {
          if (response.code === 1) {
            this.$message.success('激活成功')
            this.getList()
          } else {
            this.$message.error(response.msg || '激活失败')
          }
        })
      })
    },

    // 停用
    handleDeactivate(row) {
      this.$confirm('确认停用该预算模板吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deactivateBudgetTemplate(row.templateId).then(response => {
          if (response.code === 1) {
            this.$message.success('停用成功')
            this.getList()
          } else {
            this.$message.error(response.msg || '停用失败')
          }
        })
      })
    },

    // 归档
    handleArchive(row) {
      this.$confirm('确认归档该预算模板吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        archiveBudgetTemplate(row.templateId).then(response => {
          if (response.code === 1) {
            this.$message.success('归档成功')
            this.getList()
          } else {
            this.$message.error(response.msg || '归档失败')
          }
        })
      })
    },

    // 设为默认
    handleSetDefault(row) {
      this.$confirm('确认设置为默认预算模板吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        setDefaultBudgetTemplate(row.templateId, row.systemId, row.templateCategory).then(response => {
          if (response.code === 1) {
            this.$message.success('设置成功')
            this.getList()
          } else {
            this.$message.error(response.msg || '设置失败')
          }
        })
      })
    },

    // 导出模板
    handleExportTemplate(row) {
      exportTemplate(row.templateId).then(response => {
        if (response.code === 1) {
          // TODO: 处理导出数据
          this.$message.success('导出成功')
        } else {
          this.$message.error(response.msg || '导出失败')
        }
      })
    },

    // 删除
    handleDelete(row) {
      this.$confirm('确认删除该预算模板吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteBudgetTemplate(row.templateId).then(response => {
          if (response.code === 1) {
            this.$message.success('删除成功')
            this.getList()
          } else {
            this.$message.error(response.msg || '删除失败')
          }
        })
      })
    },

    // 获取模板类型标签类型
    getTemplateTypeTagType(type) {
      const typeMap = {
        'STANDARD': 'primary',
        'CUSTOM': 'success',
        'INDUSTRY': 'warning'
      }
      return typeMap[type] || 'info'
    },

    // 获取模板类型文本
    getTemplateTypeText(type) {
      const typeMap = {
        'STANDARD': '标准模板',
        'CUSTOM': '自定义模板',
        'INDUSTRY': '行业模板'
      }
      return typeMap[type] || type
    },

    // 获取模板分类标签类型
    getTemplateCategoryTagType(category) {
      const categoryMap = {
        'REVENUE': 'success',
        'COST': 'warning',
        'EXPENSE': 'danger',
        'CAPEX': 'primary'
      }
      return categoryMap[category] || 'info'
    },

    // 获取模板分类文本
    getTemplateCategoryText(category) {
      const categoryMap = {
        'REVENUE': '收入预算',
        'COST': '成本预算',
        'EXPENSE': '费用预算',
        'CAPEX': '资本预算'
      }
      return categoryMap[category] || category
    },

    // 获取状态标签类型
    getStatusTagType(status) {
      const statusMap = {
        'DRAFT': 'info',
        'ACTIVE': 'success',
        'INACTIVE': 'warning',
        'ARCHIVED': 'danger'
      }
      return statusMap[status] || 'info'
    },

    // 获取状态文本
    getStatusText(status) {
      const statusMap = {
        'DRAFT': '草稿',
        'ACTIVE': '激活',
        'INACTIVE': '停用',
        'ARCHIVED': '归档'
      }
      return statusMap[status] || status
    }
  }
}
</script>

<style scoped>
.app-container {
  padding: 20px;
}

.filter-container {
  margin-bottom: 20px;
}

.operation-container {
  margin-bottom: 20px;
}
</style>
