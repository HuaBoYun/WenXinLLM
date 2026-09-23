<template>
  <div class="archive-permission-list">
    <!-- 搜索区域 -->
    <div class="search-container">
      <el-form :model="searchForm" :inline="true" class="search-form">
        <el-form-item label="权限名称">
          <el-input
            v-model="searchForm.permissionName"
            placeholder="请输入权限名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="权限类型">
          <el-select
            v-model="searchForm.permissionType"
            placeholder="请选择权限类型"
            clearable
            style="width: 150px"
          >
            <el-option label="资源权限" value="RESOURCE" />
            <el-option label="操作权限" value="OPERATION" />
            <el-option label="数据权限" value="DATA" />
            <el-option label="功能权限" value="FUNCTION" />
          </el-select>
        </el-form-item>
        <el-form-item label="权限状态">
          <el-select
            v-model="searchForm.permissionStatus"
            placeholder="请选择权限状态"
            clearable
            style="width: 150px"
          >
            <el-option label="激活" value="ACTIVE" />
            <el-option label="停用" value="INACTIVE" />
            <el-option label="暂停" value="SUSPENDED" />
            <el-option label="过期" value="EXPIRED" />
            <el-option label="撤销" value="REVOKED" />
          </el-select>
        </el-form-item>
        <el-form-item label="主体类型">
          <el-select
            v-model="searchForm.subjectType"
            placeholder="请选择主体类型"
            clearable
            style="width: 150px"
          >
            <el-option label="用户" value="USER" />
            <el-option label="角色" value="ROLE" />
            <el-option label="用户组" value="GROUP" />
            <el-option label="部门" value="DEPARTMENT" />
            <el-option label="组织" value="ORGANIZATION" />
          </el-select>
        </el-form-item>
        <el-form-item label="访问级别">
          <el-select
            v-model="searchForm.accessLevel"
            placeholder="请选择访问级别"
            clearable
            style="width: 150px"
          >
            <el-option label="公开" value="PUBLIC" />
            <el-option label="内部" value="INTERNAL" />
            <el-option label="机密" value="CONFIDENTIAL" />
            <el-option label="秘密" value="SECRET" />
            <el-option label="绝密" value="TOP_SECRET" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">
            搜索
          </el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 操作栏 -->
    <div class="toolbar">
      <div class="toolbar-left">
        <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
          创建权限
        </el-button>
        <el-button
          type="success"
          icon="el-icon-check"
          :disabled="!hasSelection"
          @click="handleBatchApprove"
        >
          批量审批
        </el-button>
        <el-button
          type="warning"
          icon="el-icon-edit"
          :disabled="!hasSelection"
          @click="handleBatchUpdateStatus"
        >
          批量更新状态
        </el-button>
        <el-button
          type="danger"
          icon="el-icon-delete"
          :disabled="!hasSelection"
          @click="handleBatchDelete"
        >
          批量删除
        </el-button>
      </div>
      <div class="toolbar-right">
        <el-button icon="el-icon-download" @click="handleExport">
          导出
        </el-button>
        <el-button icon="el-icon-upload2" @click="handleImport">
          导入
        </el-button>
        <el-button icon="el-icon-refresh" @click="handleRefresh">
          刷新
        </el-button>
      </div>
    </div>

    <!-- 数据表格 -->
    <div class="table-container">
      <el-table
        ref="dataTable"
        v-loading="loading"
        :data="tableData"
        stripe
        border
        style="width: 100%"
        @selection-change="handleSelectionChange"
        @sort-change="handleSortChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column
          prop="permissionCode"
          label="权限编码"
          width="180"
          show-overflow-tooltip
        />
        <el-table-column
          prop="permissionName"
          label="权限名称"
          width="200"
          show-overflow-tooltip
        />
        <el-table-column
          prop="permissionType"
          label="权限类型"
          width="120"
          align="center"
        >
          <template slot-scope="scope">
            <el-tag :type="getPermissionTypeColor(scope.row.permissionType)">
              {{ formatPermissionType(scope.row.permissionType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="permissionLevel"
          label="权限级别"
          width="120"
          align="center"
        >
          <template slot-scope="scope">
            {{ formatPermissionLevel(scope.row.permissionLevel) }}
          </template>
        </el-table-column>
        <el-table-column
          prop="subjectType"
          label="主体类型"
          width="120"
          align="center"
        >
          <template slot-scope="scope">
            {{ formatSubjectType(scope.row.subjectType) }}
          </template>
        </el-table-column>
        <el-table-column
          prop="subjectName"
          label="主体名称"
          width="150"
          show-overflow-tooltip
        />
        <el-table-column
          prop="resourceType"
          label="资源类型"
          width="120"
          align="center"
        >
          <template slot-scope="scope">
            {{ formatResourceType(scope.row.resourceType) }}
          </template>
        </el-table-column>
        <el-table-column
          prop="accessLevel"
          label="访问级别"
          width="120"
          align="center"
        >
          <template slot-scope="scope">
            <el-tag :type="getAccessLevelColor(scope.row.accessLevel)">
              {{ formatAccessLevel(scope.row.accessLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="permissionStatus"
          label="权限状态"
          width="120"
          align="center"
        >
          <template slot-scope="scope">
            <el-tag :type="getPermissionStatusColor(scope.row.permissionStatus)">
              {{ formatPermissionStatus(scope.row.permissionStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="riskLevel"
          label="风险等级"
          width="120"
          align="center"
        >
          <template slot-scope="scope">
            <el-tag :type="getRiskLevelColor(scope.row.riskLevel)">
              {{ formatRiskLevel(scope.row.riskLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column
          prop="createdTime"
          label="创建时间"
          width="160"
          align="center"
          sortable="custom"
        >
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.createdTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" align="center" fixed="right">
          <template slot-scope="scope">
            <el-dropdown @command="(command) => handleCommand(command, scope.row)">
              <el-button type="primary" size="mini">
                操作<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="view" icon="el-icon-view">
                  查看详情
                </el-dropdown-item>
                <el-dropdown-item command="edit" icon="el-icon-edit">
                  编辑权限
                </el-dropdown-item>
                <el-dropdown-item
                  v-if="scope.row.permissionStatus === 'PENDING'"
                  command="approve"
                  icon="el-icon-check"
                >
                  审批权限
                </el-dropdown-item>
                <el-dropdown-item
                  v-if="scope.row.permissionStatus === 'ACTIVE'"
                  command="suspend"
                  icon="el-icon-warning"
                >
                  暂停权限
                </el-dropdown-item>
                <el-dropdown-item
                  v-if="scope.row.permissionStatus === 'SUSPENDED'"
                  command="activate"
                  icon="el-icon-check"
                >
                  激活权限
                </el-dropdown-item>
                <el-dropdown-item command="delegate" icon="el-icon-share">
                  委托权限
                </el-dropdown-item>
                <el-dropdown-item command="copy" icon="el-icon-document-copy">
                  复制权限
                </el-dropdown-item>
                <el-dropdown-item
                  command="delete"
                  icon="el-icon-delete"
                  divided
                  style="color: #f56c6c"
                >
                  删除权限
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页 -->
    <div class="pagination-container">
      <el-pagination
        :current-page="pagination.current"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pagination.size"
        :total="pagination.total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- 创建/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="800px"
      :before-close="handleDialogClose"
    >
      <el-form
        ref="permissionForm"
        :model="permissionForm"
        :rules="permissionRules"
        label-width="120px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="权限名称" prop="permissionName">
              <el-input v-model="permissionForm.permissionName" placeholder="请输入权限名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="权限类型" prop="permissionType">
              <el-select v-model="permissionForm.permissionType" placeholder="请选择权限类型">
                <el-option label="资源权限" value="RESOURCE" />
                <el-option label="操作权限" value="OPERATION" />
                <el-option label="数据权限" value="DATA" />
                <el-option label="功能权限" value="FUNCTION" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="权限级别" prop="permissionLevel">
              <el-select v-model="permissionForm.permissionLevel" placeholder="请选择权限级别">
                <el-option label="系统级" value="SYSTEM" />
                <el-option label="模块级" value="MODULE" />
                <el-option label="资源级" value="RESOURCE" />
                <el-option label="记录级" value="RECORD" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="主体类型" prop="subjectType">
              <el-select v-model="permissionForm.subjectType" placeholder="请选择主体类型">
                <el-option label="用户" value="USER" />
                <el-option label="角色" value="ROLE" />
                <el-option label="用户组" value="GROUP" />
                <el-option label="部门" value="DEPARTMENT" />
                <el-option label="组织" value="ORGANIZATION" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="主体ID" prop="subjectId">
              <el-input v-model="permissionForm.subjectId" placeholder="请输入主体ID" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="主体名称" prop="subjectName">
              <el-input v-model="permissionForm.subjectName" placeholder="请输入主体名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="资源类型" prop="resourceType">
              <el-select v-model="permissionForm.resourceType" placeholder="请选择资源类型">
                <el-option label="档案" value="ARCHIVE" />
                <el-option label="文档" value="DOCUMENT" />
                <el-option label="文件夹" value="FOLDER" />
                <el-option label="分类" value="CATEGORY" />
                <el-option label="系统" value="SYSTEM" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="资源ID" prop="resourceId">
              <el-input v-model="permissionForm.resourceId" placeholder="请输入资源ID" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="操作权限" prop="operationPermissions">
          <el-checkbox-group v-model="permissionForm.operationPermissions">
            <el-checkbox label="READ">读取</el-checkbox>
            <el-checkbox label="WRITE">写入</el-checkbox>
            <el-checkbox label="DELETE">删除</el-checkbox>
            <el-checkbox label="EXECUTE">执行</el-checkbox>
            <el-checkbox label="ADMIN">管理</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="访问级别" prop="accessLevel">
              <el-select v-model="permissionForm.accessLevel" placeholder="请选择访问级别">
                <el-option label="公开" value="PUBLIC" />
                <el-option label="内部" value="INTERNAL" />
                <el-option label="机密" value="CONFIDENTIAL" />
                <el-option label="秘密" value="SECRET" />
                <el-option label="绝密" value="TOP_SECRET" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="权限优先级" prop="permissionPriority">
              <el-select v-model="permissionForm.permissionPriority" placeholder="请选择优先级">
                <el-option label="最高" :value="1" />
                <el-option label="高" :value="2" />
                <el-option label="中" :value="3" />
                <el-option label="低" :value="4" />
                <el-option label="最低" :value="5" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="权限描述" prop="permissionDescription">
          <el-input
            v-model="permissionForm.permissionDescription"
            type="textarea"
            :rows="3"
            placeholder="请输入权限描述"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSave" :loading="saveLoading">
          确定
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getPermissionPage,
  createPermission,
  updatePermission,
  deletePermission,
  updatePermissionStatus,
  batchUpdatePermissionStatus,
  formatPermissionType,
  formatPermissionLevel,
  formatPermissionStatus,
  formatSubjectType,
  formatResourceType,
  formatAccessLevel,
  formatRiskLevel,
  getPermissionStatusColor,
  getAccessLevelColor,
  getRiskLevelColor
} from '@/api/managementAccountant/as/archivePermission'

export default {
  name: 'ArchivePermissionList',
  data() {
    return {
      // 搜索表单
      searchForm: {
        permissionName: '',
        permissionType: '',
        permissionStatus: '',
        subjectType: '',
        accessLevel: ''
      },
      // 表格数据
      tableData: [],
      loading: false,
      selectedRows: [],
      // 分页
      pagination: {
        current: 1,
        size: 20,
        total: 0
      },
      // 排序
      sortField: '',
      sortOrder: '',
      // 对话框
      dialogVisible: false,
      dialogMode: 'create', // create, edit
      saveLoading: false,
      permissionForm: {
        permissionId: null,
        permissionName: '',
        permissionType: '',
        permissionLevel: '',
        subjectType: '',
        subjectId: '',
        subjectName: '',
        resourceType: '',
        resourceId: '',
        operationPermissions: [],
        accessLevel: 'INTERNAL',
        permissionPriority: 3,
        permissionDescription: ''
      },
      permissionRules: {
        permissionName: [
          { required: true, message: '请输入权限名称', trigger: 'blur' }
        ],
        permissionType: [
          { required: true, message: '请选择权限类型', trigger: 'change' }
        ],
        permissionLevel: [
          { required: true, message: '请选择权限级别', trigger: 'change' }
        ],
        subjectType: [
          { required: true, message: '请选择主体类型', trigger: 'change' }
        ],
        subjectId: [
          { required: true, message: '请输入主体ID', trigger: 'blur' }
        ],
        resourceType: [
          { required: true, message: '请选择资源类型', trigger: 'change' }
        ],
        resourceId: [
          { required: true, message: '请输入资源ID', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    hasSelection() {
      return this.selectedRows.length > 0
    },
    dialogTitle() {
      return this.dialogMode === 'create' ? '创建权限' : '编辑权限'
    }
  },
  created() {
    this.loadData()
  },
  methods: {
    // 加载数据
    async loadData() {
      this.loading = true
      try {
        const params = {
          ...this.searchForm,
          sortField: this.sortField,
          sortOrder: this.sortOrder
        }
        const response = await getPermissionPage(this.pagination.current, this.pagination.size, params)
        if (response.success) {
          this.tableData = response.data.records || []
          this.pagination.total = response.data.total || 0
        }
      } catch (error) {
        console.error('加载数据失败:', error)
        this.$message.error('加载数据失败')
      } finally {
        this.loading = false
      }
    },
    // 搜索
    handleSearch() {
      this.pagination.current = 1
      this.loadData()
    },
    // 重置搜索
    handleReset() {
      this.searchForm = {
        permissionName: '',
        permissionType: '',
        permissionStatus: '',
        subjectType: '',
        accessLevel: ''
      }
      this.pagination.current = 1
      this.loadData()
    },
    // 刷新
    handleRefresh() {
      this.loadData()
    },
    // 创建
    handleCreate() {
      this.dialogMode = 'create'
      this.resetForm()
      this.dialogVisible = true
    },
    // 编辑
    handleEdit(row) {
      this.dialogMode = 'edit'
      this.permissionForm = {
        ...row,
        operationPermissions: row.operationPermissions ? row.operationPermissions.split(',') : []
      }
      this.dialogVisible = true
    },
    // 查看详情
    handleView(row) {
      this.$router.push(`/managementAccountant/as/archivePermission/detail/${row.permissionId}`)
    },
    // 删除
    async handleDelete(row) {
      try {
        await this.$confirm('确定要删除这个权限吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const response = await deletePermission(row.permissionId)
        if (response.success) {
          this.$message.success('删除成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          console.error('删除失败:', error)
          this.$message.error('删除失败')
        }
      }
    },
    // 更新状态
    async handleUpdateStatus(row, status) {
      try {
        const response = await updatePermissionStatus(row.permissionId, status)
        if (response.success) {
          this.$message.success('状态更新成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '状态更新失败')
        }
      } catch (error) {
        console.error('状态更新失败:', error)
        this.$message.error('状态更新失败')
      }
    },
    // 命令处理
    handleCommand(command, row) {
      switch (command) {
        case 'view':
          this.handleView(row)
          break
        case 'edit':
          this.handleEdit(row)
          break
        case 'approve':
          this.handleUpdateStatus(row, 'APPROVED')
          break
        case 'suspend':
          this.handleUpdateStatus(row, 'SUSPENDED')
          break
        case 'activate':
          this.handleUpdateStatus(row, 'ACTIVE')
          break
        case 'delegate':
          this.handleDelegate(row)
          break
        case 'copy':
          this.handleCopy(row)
          break
        case 'delete':
          this.handleDelete(row)
          break
      }
    },
    // 委托权限
    handleDelegate(row) {
      this.$message.info('委托功能开发中...')
    },
    // 复制权限
    handleCopy(row) {
      this.dialogMode = 'create'
      this.permissionForm = {
        ...row,
        permissionId: null,
        permissionCode: '',
        permissionName: row.permissionName + '_副本',
        operationPermissions: row.operationPermissions ? row.operationPermissions.split(',') : []
      }
      this.dialogVisible = true
    },
    // 批量操作
    handleBatchApprove() {
      this.$message.info('批量审批功能开发中...')
    },
    handleBatchUpdateStatus() {
      this.$message.info('批量更新状态功能开发中...')
    },
    handleBatchDelete() {
      this.$message.info('批量删除功能开发中...')
    },
    // 导入导出
    handleExport() {
      this.$message.info('导出功能开发中...')
    },
    handleImport() {
      this.$message.info('导入功能开发中...')
    },
    // 表格事件
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },
    handleSortChange({ column, prop, order }) {
      this.sortField = prop
      this.sortOrder = order === 'ascending' ? 'asc' : 'desc'
      this.loadData()
    },
    // 分页事件
    handleSizeChange(size) {
      this.pagination.size = size
      this.pagination.current = 1
      this.loadData()
    },
    handleCurrentChange(current) {
      this.pagination.current = current
      this.loadData()
    },
    // 表单操作
    resetForm() {
      this.permissionForm = {
        permissionId: null,
        permissionName: '',
        permissionType: '',
        permissionLevel: '',
        subjectType: '',
        subjectId: '',
        subjectName: '',
        resourceType: '',
        resourceId: '',
        operationPermissions: [],
        accessLevel: 'INTERNAL',
        permissionPriority: 3,
        permissionDescription: ''
      }
      if (this.$refs.permissionForm) {
        this.$refs.permissionForm.clearValidate()
      }
    },
    // 保存
    handleSave() {
      this.$refs.permissionForm.validate(async (valid) => {
        if (valid) {
          this.saveLoading = true
          try {
            const formData = {
              ...this.permissionForm,
              operationPermissions: this.permissionForm.operationPermissions.join(',')
            }
            
            let response
            if (this.dialogMode === 'create') {
              response = await createPermission(formData)
            } else {
              response = await updatePermission(formData)
            }
            
            if (response.success) {
              this.$message.success(this.dialogMode === 'create' ? '创建成功' : '更新成功')
              this.dialogVisible = false
              this.loadData()
            } else {
              this.$message.error(response.message || '操作失败')
            }
          } catch (error) {
            console.error('保存失败:', error)
            this.$message.error('保存失败')
          } finally {
            this.saveLoading = false
          }
        }
      })
    },
    // 对话框关闭
    handleDialogClose(done) {
      this.resetForm()
      done()
    },
    // 格式化方法
    formatPermissionType,
    formatPermissionLevel,
    formatPermissionStatus,
    formatSubjectType,
    formatResourceType,
    formatAccessLevel,
    formatRiskLevel,
    getPermissionStatusColor,
    getAccessLevelColor,
    getRiskLevelColor,
    getPermissionTypeColor(type) {
      const colorMap = {
        RESOURCE: 'primary',
        OPERATION: 'success',
        DATA: 'warning',
        FUNCTION: 'info'
      }
      return colorMap[type] || 'info'
    },
    formatDateTime(dateTime) {
      if (!dateTime) return ''
      return new Date(dateTime).toLocaleString()
    }
  }
}
</script>

<style lang="scss" scoped>
.archive-permission-list {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 84px);

  .search-container {
    background: white;
    border-radius: 8px;
    padding: 20px;
    margin-bottom: 20px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

    .search-form {
      .el-form-item {
        margin-bottom: 16px;
      }
    }
  }

  .toolbar {
    background: white;
    border-radius: 8px;
    padding: 16px 20px;
    margin-bottom: 20px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    display: flex;
    justify-content: space-between;
    align-items: center;

    .toolbar-left,
    .toolbar-right {
      display: flex;
      gap: 8px;
    }
  }

  .table-container {
    background: white;
    border-radius: 8px;
    padding: 20px;
    margin-bottom: 20px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

    .el-table {
      .el-table__header {
        th {
          background-color: #f5f7fa;
          color: #606266;
          font-weight: 600;
        }
      }

      .el-table__row {
        &:hover {
          background-color: #f5f7fa;
        }
      }
    }
  }

  .pagination-container {
    background: white;
    border-radius: 8px;
    padding: 16px 20px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    display: flex;
    justify-content: center;
  }
}

// 对话框样式
::v-deep .el-dialog {
  .el-dialog__header {
    background: #f5f7fa;
    padding: 20px 24px;
    border-bottom: 1px solid #ebeef5;

    .el-dialog__title {
      font-size: 16px;
      font-weight: 600;
      color: #303133;
    }
  }

  .el-dialog__body {
    padding: 24px;
    max-height: 60vh;
    overflow-y: auto;
  }

  .el-dialog__footer {
    padding: 16px 24px;
    border-top: 1px solid #ebeef5;
    background: #f5f7fa;
  }
}

// 下拉菜单样式
::v-deep .el-dropdown-menu {
  .el-dropdown-menu__item {
    padding: 8px 16px;

    &:hover {
      background-color: #f5f7fa;
    }

    i {
      margin-right: 8px;
    }
  }
}

// 标签样式
.el-tag {
  font-size: 12px;
  padding: 0 8px;
  height: 24px;
  line-height: 22px;
  border-radius: 4px;
}

// 响应式设计
@media (max-width: 768px) {
  .archive-permission-list {
    padding: 12px;

    .search-container {
      padding: 16px;

      .search-form {
        .el-form-item {
          display: block;
          margin-bottom: 12px;

          .el-form-item__content {
            margin-left: 0 !important;
          }
        }
      }
    }

    .toolbar {
      flex-direction: column;
      gap: 12px;
      align-items: stretch;

      .toolbar-left,
      .toolbar-right {
        justify-content: center;
        flex-wrap: wrap;
      }
    }

    .table-container {
      padding: 12px;
      overflow-x: auto;
    }

    .pagination-container {
      padding: 12px;
    }
  }
}

// 表格滚动条样式
.table-container {
  ::-webkit-scrollbar {
    width: 6px;
    height: 6px;
  }

  ::-webkit-scrollbar-track {
    background: #f1f1f1;
    border-radius: 3px;
  }

  ::-webkit-scrollbar-thumb {
    background: #c1c1c1;
    border-radius: 3px;

    &:hover {
      background: #a8a8a8;
    }
  }
}
</style>
