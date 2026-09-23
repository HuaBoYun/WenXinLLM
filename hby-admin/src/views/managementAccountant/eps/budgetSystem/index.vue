<template>
  <div class="app-container">
    <!-- 查询条件 -->
    <div class="filter-container">
      <el-form :inline="true" :model="queryParams" class="demo-form-inline">
        <el-form-item label="体系名称">
          <el-input
            v-model="queryParams.systemName"
            placeholder="请输入体系名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="体系类型">
          <el-select v-model="queryParams.systemType" placeholder="请选择体系类型" clearable>
            <el-option label="综合预算" value="COMPREHENSIVE" />
            <el-option label="资本预算" value="CAPITAL" />
            <el-option label="现金预算" value="CASH" />
            <el-option label="滚动预算" value="ROLLING" />
          </el-select>
        </el-form-item>
        <el-form-item label="预算年度">
          <el-date-picker
            v-model="queryParams.fiscalYear"
            type="year"
            placeholder="选择年度"
            value-format="yyyy"
            style="width: 120px"
          />
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
      <el-button type="info" icon="el-icon-download" @click="handleExport">导出</el-button>
    </div>

    <!-- 数据表格 -->
    <el-table
      v-loading="loading"
      :data="budgetSystemList"
      @selection-change="handleSelectionChange"
      style="width: 100%"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column prop="systemCode" label="体系编码" width="120" />
      <el-table-column prop="systemName" label="体系名称" width="200" show-overflow-tooltip />
      <el-table-column prop="systemType" label="体系类型" width="100">
        <template slot-scope="scope">
          <el-tag :type="getSystemTypeTagType(scope.row.systemType)">
            {{ getSystemTypeText(scope.row.systemType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="fiscalYear" label="预算年度" width="100" />
      <el-table-column prop="organizationName" label="组织名称" width="200" show-overflow-tooltip />
      <el-table-column prop="isDefault" label="默认体系" width="80">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.isDefault === 1" type="success" size="mini">是</el-tag>
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
      <budget-system-form
        ref="budgetSystemForm"
        :form-data="formData"
        :is-edit="isEdit"
        :is-view="dialogTitle.includes('查看')"
        @submit="handleSubmit"
        @cancel="handleCancel"
      />
    </el-dialog>

    <!-- 复制对话框 -->
    <el-dialog
      title="复制预算体系"
      :visible.sync="copyDialogVisible"
      width="500px"
      :close-on-click-modal="false"
    >
      <el-form ref="copyForm" :model="copyForm" :rules="copyRules" label-width="120px">
        <el-form-item label="目标体系编码" prop="targetSystemCode">
          <el-input v-model="copyForm.targetSystemCode" placeholder="请输入目标体系编码" />
        </el-form-item>
        <el-form-item label="目标体系名称" prop="targetSystemName">
          <el-input v-model="copyForm.targetSystemName" placeholder="请输入目标体系名称" />
        </el-form-item>
        <el-form-item label="目标预算年度" prop="targetFiscalYear">
          <el-date-picker
            v-model="copyForm.targetFiscalYear"
            type="year"
            placeholder="选择年度"
            value-format="yyyy"
            style="width: 100%"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="copyDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleCopySubmit">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getBudgetSystemPage, deleteBudgetSystem, batchDeleteBudgetSystem, activateBudgetSystem, deactivateBudgetSystem, archiveBudgetSystem, setDefaultBudgetSystem, copyBudgetSystem } from '@/api/managementAccountant/eps/budgetSystem'
import BudgetSystemForm from './components/BudgetSystemForm'
import Pagination from '@/components/Pagination'

export default {
  name: 'BudgetSystem',
  components: {
    BudgetSystemForm,
    Pagination
  },
  data() {
    return {
      loading: false,
      budgetSystemList: [],
      total: 0,
      multipleSelection: [],
      queryParams: {
        current: 1,
        size: 10,
        systemName: '',
        systemType: '',
        fiscalYear: '',
        organizationId: null,
        status: ''
      },
      dialogVisible: false,
      dialogTitle: '',
      isEdit: false,
      formData: {},
      copyDialogVisible: false,
      copyForm: {
        sourceSystemId: null,
        targetSystemCode: '',
        targetSystemName: '',
        targetFiscalYear: ''
      },
      copyRules: {
        targetSystemCode: [
          { required: true, message: '请输入目标体系编码', trigger: 'blur' }
        ],
        targetSystemName: [
          { required: true, message: '请输入目标体系名称', trigger: 'blur' }
        ],
        targetFiscalYear: [
          { required: true, message: '请选择目标预算年度', trigger: 'change' }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    // 获取列表数据
    getList() {
      this.loading = true
      getBudgetSystemPage(this.queryParams).then(response => {
        if (response.code === 1) {
          this.budgetSystemList = response.data.records
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
        systemName: '',
        systemType: '',
        fiscalYear: '',
        organizationId: null,
        status: ''
      }
      this.getList()
    },

    // 新增
    handleAdd() {
      this.dialogTitle = '新增预算体系'
      this.isEdit = false
      this.formData = {}
      this.dialogVisible = true
    },

    // 编辑
    handleEdit(row) {
      this.dialogTitle = '编辑预算体系'
      this.isEdit = true
      this.formData = { ...row }
      this.dialogVisible = true
    },

    // 查看
    handleView(row) {
      this.dialogTitle = '查看预算体系'
      this.isEdit = false
      this.formData = { ...row }
      this.dialogVisible = true
    },

    // 复制
    handleCopy(row) {
      this.copyForm.sourceSystemId = row.systemId
      this.copyForm.targetSystemCode = ''
      this.copyForm.targetSystemName = ''
      this.copyForm.targetFiscalYear = ''
      this.copyDialogVisible = true
    },

    // 复制提交
    handleCopySubmit() {
      this.$refs.copyForm.validate(valid => {
        if (valid) {
          copyBudgetSystem(this.copyForm).then(response => {
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
      this.$confirm('确认删除选中的预算体系吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const systemIds = this.multipleSelection.map(item => item.systemId)
        batchDeleteBudgetSystem(systemIds).then(response => {
          if (response.code === 1) {
            this.$message.success('删除成功')
            this.getList()
          } else {
            this.$message.error(response.msg || '删除失败')
          }
        })
      })
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
        case 'delete':
          this.handleDelete(row)
          break
      }
    },

    // 激活
    handleActivate(row) {
      this.$confirm('确认激活该预算体系吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        activateBudgetSystem(row.systemId).then(response => {
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
      this.$confirm('确认停用该预算体系吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deactivateBudgetSystem(row.systemId).then(response => {
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
      this.$confirm('确认归档该预算体系吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        archiveBudgetSystem(row.systemId).then(response => {
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
      this.$confirm('确认设置为默认预算体系吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        setDefaultBudgetSystem(row.systemId, row.organizationId).then(response => {
          if (response.code === 1) {
            this.$message.success('设置成功')
            this.getList()
          } else {
            this.$message.error(response.msg || '设置失败')
          }
        })
      })
    },

    // 删除
    handleDelete(row) {
      this.$confirm('确认删除该预算体系吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteBudgetSystem(row.systemId).then(response => {
          if (response.code === 1) {
            this.$message.success('删除成功')
            this.getList()
          } else {
            this.$message.error(response.msg || '删除失败')
          }
        })
      })
    },

    // 获取体系类型标签类型
    getSystemTypeTagType(type) {
      const typeMap = {
        'COMPREHENSIVE': 'primary',
        'CAPITAL': 'success',
        'CASH': 'warning',
        'ROLLING': 'info'
      }
      return typeMap[type] || 'info'
    },

    // 获取体系类型文本
    getSystemTypeText(type) {
      const typeMap = {
        'COMPREHENSIVE': '综合预算',
        'CAPITAL': '资本预算',
        'CASH': '现金预算',
        'ROLLING': '滚动预算'
      }
      return typeMap[type] || type
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
