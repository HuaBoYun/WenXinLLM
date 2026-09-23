<template>
  <div class="document-version-list">
    <!-- 搜索区域 -->
    <div class="search-area">
      <el-form :model="searchForm" :inline="true" class="search-form">
        <el-form-item label="版本名称">
          <el-input
            v-model="searchForm.versionName"
            placeholder="请输入版本名称"
            clearable
            style="width: 200px"
          ></el-input>
        </el-form-item>
        <el-form-item label="文档ID">
          <el-input
            v-model="searchForm.documentId"
            placeholder="请输入文档ID"
            clearable
            style="width: 200px"
          ></el-input>
        </el-form-item>
        <el-form-item label="版本状态">
          <el-select v-model="searchForm.versionStatus" placeholder="请选择状态" clearable style="width: 150px">
            <el-option label="草稿" value="DRAFT"></el-option>
            <el-option label="审核中" value="UNDER_REVIEW"></el-option>
            <el-option label="已审批" value="APPROVED"></el-option>
            <el-option label="已发布" value="PUBLISHED"></el-option>
            <el-option label="已归档" value="ARCHIVED"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="版本类型">
          <el-select v-model="searchForm.versionType" placeholder="请选择类型" clearable style="width: 150px">
            <el-option label="主版本" value="MAJOR"></el-option>
            <el-option label="次版本" value="MINOR"></el-option>
            <el-option label="补丁版本" value="PATCH"></el-option>
            <el-option label="草稿版本" value="DRAFT"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="创建时间">
          <el-date-picker
            v-model="searchForm.createTimeRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            style="width: 240px"
          ></el-date-picker>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 操作区域 -->
    <div class="operation-area">
      <div class="operation-left">
        <el-button type="primary" icon="el-icon-plus" @click="handleCreate">创建版本</el-button>
        <el-button type="success" icon="el-icon-upload" @click="handleBatchPublish" :disabled="!hasSelection">
          批量发布
        </el-button>
        <el-button type="warning" icon="el-icon-lock" @click="handleBatchLock" :disabled="!hasSelection">
          批量锁定
        </el-button>
        <el-button type="info" icon="el-icon-folder" @click="handleBatchArchive" :disabled="!hasSelection">
          批量归档
        </el-button>
        <el-button type="danger" icon="el-icon-delete" @click="handleBatchDelete" :disabled="!hasSelection">
          批量删除
        </el-button>
      </div>
      <div class="operation-right">
        <el-button icon="el-icon-download" @click="handleExport">导出</el-button>
        <el-button icon="el-icon-upload2" @click="handleImport">导入</el-button>
        <el-button icon="el-icon-refresh" @click="loadData">刷新</el-button>
      </div>
    </div>

    <!-- 表格区域 -->
    <div class="table-area">
      <el-table
        ref="table"
        v-loading="loading"
        :data="tableData"
        @selection-change="handleSelectionChange"
        @sort-change="handleSortChange"
        stripe
        border
        style="width: 100%"
      >
        <el-table-column type="selection" width="55" align="center"></el-table-column>
        <el-table-column prop="versionCode" label="版本编号" width="150" sortable="custom">
          <template slot-scope="scope">
            <el-link type="primary" @click="handleView(scope.row)">
              {{ scope.row.versionCode }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column prop="versionName" label="版本名称" min-width="200" sortable="custom">
          <template slot-scope="scope">
            <div class="version-info">
              <div class="version-name">{{ scope.row.versionName }}</div>
              <div class="version-number">{{ scope.row.versionNumber }}</div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="documentName" label="文档名称" min-width="180" sortable="custom"></el-table-column>
        <el-table-column prop="versionType" label="版本类型" width="120" sortable="custom">
          <template slot-scope="scope">
            <el-tag :type="getVersionTypeColor(scope.row.versionType)" size="mini">
              {{ formatVersionType(scope.row.versionType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="versionStatus" label="版本状态" width="120" sortable="custom">
          <template slot-scope="scope">
            <el-tag :type="getVersionStatusColor(scope.row.versionStatus)" size="mini">
              {{ formatVersionStatus(scope.row.versionStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="fileSize" label="文件大小" width="120" sortable="custom">
          <template slot-scope="scope">
            {{ formatFileSize(scope.row.fileSize) }}
          </template>
        </el-table-column>
        <el-table-column prop="isCurrent" label="当前版本" width="100" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.isCurrent" type="success" size="mini">是</el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="isLocked" label="锁定状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.isLocked" type="warning" size="mini">已锁定</el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="createdBy" label="创建人" width="120"></el-table-column>
        <el-table-column prop="createdTime" label="创建时间" width="160" sortable="custom">
          <template slot-scope="scope">
            {{ formatTime(scope.row.createdTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-dropdown @command="handleCommand" trigger="click">
              <el-button type="text" size="small">
                操作<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item :command="{action: 'view', row: scope.row}">
                  <i class="el-icon-view"></i> 查看详情
                </el-dropdown-item>
                <el-dropdown-item :command="{action: 'edit', row: scope.row}" :disabled="scope.row.isLocked">
                  <i class="el-icon-edit"></i> 编辑
                </el-dropdown-item>
                <el-dropdown-item :command="{action: 'copy', row: scope.row}">
                  <i class="el-icon-copy-document"></i> 复制版本
                </el-dropdown-item>
                <el-dropdown-item :command="{action: 'branch', row: scope.row}">
                  <i class="el-icon-share"></i> 创建分支
                </el-dropdown-item>
                <el-dropdown-item :command="{action: 'tag', row: scope.row}">
                  <i class="el-icon-price-tag"></i> 创建标签
                </el-dropdown-item>
                <el-dropdown-item divided :command="{action: 'publish', row: scope.row}" 
                  :disabled="scope.row.versionStatus !== 'APPROVED'">
                  <i class="el-icon-upload"></i> 发布
                </el-dropdown-item>
                <el-dropdown-item :command="{action: 'lock', row: scope.row}" 
                  :disabled="scope.row.isLocked">
                  <i class="el-icon-lock"></i> 锁定
                </el-dropdown-item>
                <el-dropdown-item :command="{action: 'unlock', row: scope.row}" 
                  :disabled="!scope.row.isLocked">
                  <i class="el-icon-unlock"></i> 解锁
                </el-dropdown-item>
                <el-dropdown-item :command="{action: 'archive', row: scope.row}">
                  <i class="el-icon-folder"></i> 归档
                </el-dropdown-item>
                <el-dropdown-item :command="{action: 'rollback', row: scope.row}">
                  <i class="el-icon-refresh-left"></i> 回滚
                </el-dropdown-item>
                <el-dropdown-item divided :command="{action: 'delete', row: scope.row}" 
                  :disabled="scope.row.isCurrent || scope.row.isDefault">
                  <i class="el-icon-delete"></i> 删除
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页区域 -->
    <div class="pagination-area">
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="pagination.current"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pagination.size"
        layout="total, sizes, prev, pager, next, jumper"
        :total="pagination.total"
      ></el-pagination>
    </div>

    <!-- 创建版本对话框 -->
    <el-dialog
      title="创建文档版本"
      :visible.sync="createDialog"
      width="600px"
      @close="resetCreateForm"
    >
      <el-form ref="createForm" :model="createForm" :rules="createRules" label-width="100px">
        <el-form-item label="文档ID" prop="documentId">
          <el-input v-model="createForm.documentId" placeholder="请输入文档ID"></el-input>
        </el-form-item>
        <el-form-item label="版本名称" prop="versionName">
          <el-input v-model="createForm.versionName" placeholder="请输入版本名称"></el-input>
        </el-form-item>
        <el-form-item label="版本类型" prop="versionType">
          <el-select v-model="createForm.versionType" placeholder="请选择版本类型">
            <el-option label="主版本" value="MAJOR"></el-option>
            <el-option label="次版本" value="MINOR"></el-option>
            <el-option label="补丁版本" value="PATCH"></el-option>
            <el-option label="草稿版本" value="DRAFT"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="版本描述" prop="versionDescription">
          <el-input
            v-model="createForm.versionDescription"
            type="textarea"
            :rows="3"
            placeholder="请输入版本描述"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="createDialog = false">取消</el-button>
        <el-button type="primary" @click="confirmCreate" :loading="createLoading">确定</el-button>
      </div>
    </el-dialog>

    <!-- 复制版本对话框 -->
    <el-dialog
      title="复制版本"
      :visible.sync="copyDialog"
      width="500px"
      @close="resetCopyForm"
    >
      <el-form ref="copyForm" :model="copyForm" :rules="copyRules" label-width="100px">
        <el-form-item label="新版本名称" prop="newVersionName">
          <el-input v-model="copyForm.newVersionName" placeholder="请输入新版本名称"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="copyDialog = false">取消</el-button>
        <el-button type="primary" @click="confirmCopy" :loading="copyLoading">确定</el-button>
      </div>
    </el-dialog>

    <!-- 创建分支对话框 -->
    <el-dialog
      title="创建分支"
      :visible.sync="branchDialog"
      width="500px"
      @close="resetBranchForm"
    >
      <el-form ref="branchForm" :model="branchForm" :rules="branchRules" label-width="100px">
        <el-form-item label="分支名称" prop="branchName">
          <el-input v-model="branchForm.branchName" placeholder="请输入分支名称"></el-input>
        </el-form-item>
        <el-form-item label="分支描述" prop="description">
          <el-input
            v-model="branchForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入分支描述"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="branchDialog = false">取消</el-button>
        <el-button type="primary" @click="confirmCreateBranch" :loading="branchLoading">确定</el-button>
      </div>
    </el-dialog>

    <!-- 创建标签对话框 -->
    <el-dialog
      title="创建标签"
      :visible.sync="tagDialog"
      width="500px"
      @close="resetTagForm"
    >
      <el-form ref="tagForm" :model="tagForm" :rules="tagRules" label-width="100px">
        <el-form-item label="标签名称" prop="tagName">
          <el-input v-model="tagForm.tagName" placeholder="请输入标签名称"></el-input>
        </el-form-item>
        <el-form-item label="标签描述" prop="description">
          <el-input
            v-model="tagForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入标签描述"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="tagDialog = false">取消</el-button>
        <el-button type="primary" @click="confirmCreateTag" :loading="tagLoading">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getVersionPage,
  createVersion,
  deleteVersion,
  copyVersion,
  createBranch,
  createTag,
  publishVersion,
  lockVersion,
  unlockVersion,
  archiveVersion,
  rollbackToVersion,
  batchPublishVersions,
  batchLockVersions,
  batchArchiveVersions,
  batchDeleteVersions,
  formatVersionStatus,
  formatVersionType,
  formatFileSize,
  getVersionStatusColor,
  getVersionTypeIcon
} from '@/api/managementAccountant/as/documentVersion'

export default {
  name: 'DocumentVersionList',
  data() {
    return {
      // 搜索表单
      searchForm: {
        versionName: '',
        documentId: '',
        versionStatus: '',
        versionType: '',
        createTimeRange: []
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
      // 创建版本
      createDialog: false,
      createLoading: false,
      createForm: {
        documentId: '',
        versionName: '',
        versionType: '',
        versionDescription: ''
      },
      createRules: {
        documentId: [{ required: true, message: '请输入文档ID', trigger: 'blur' }],
        versionName: [{ required: true, message: '请输入版本名称', trigger: 'blur' }],
        versionType: [{ required: true, message: '请选择版本类型', trigger: 'change' }],
        versionDescription: [{ required: true, message: '请输入版本描述', trigger: 'blur' }]
      },
      // 复制版本
      copyDialog: false,
      copyLoading: false,
      copyForm: {
        sourceVersionId: '',
        newVersionName: ''
      },
      copyRules: {
        newVersionName: [{ required: true, message: '请输入新版本名称', trigger: 'blur' }]
      },
      // 创建分支
      branchDialog: false,
      branchLoading: false,
      branchForm: {
        sourceVersionId: '',
        branchName: '',
        description: ''
      },
      branchRules: {
        branchName: [{ required: true, message: '请输入分支名称', trigger: 'blur' }],
        description: [{ required: true, message: '请输入分支描述', trigger: 'blur' }]
      },
      // 创建标签
      tagDialog: false,
      tagLoading: false,
      tagForm: {
        versionId: '',
        tagName: '',
        description: ''
      },
      tagRules: {
        tagName: [{ required: true, message: '请输入标签名称', trigger: 'blur' }],
        description: [{ required: true, message: '请输入标签描述', trigger: 'blur' }]
      }
    }
  },
  computed: {
    hasSelection() {
      return this.selectedRows.length > 0
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
          current: this.pagination.current,
          size: this.pagination.size,
          ...this.searchForm
        }
        
        // 处理时间范围
        if (this.searchForm.createTimeRange && this.searchForm.createTimeRange.length === 2) {
          params.startTime = this.searchForm.createTimeRange[0]
          params.endTime = this.searchForm.createTimeRange[1]
        }
        
        // 处理排序
        if (this.sortField) {
          params.sortField = this.sortField
          params.sortOrder = this.sortOrder
        }
        
        const response = await getVersionPage(params)
        if (response.data) {
          this.tableData = response.data.records || []
          this.pagination.total = response.data.total || 0
        }
      } catch (error) {
        this.$message.error('加载数据失败: ' + error.message)
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
        versionName: '',
        documentId: '',
        versionStatus: '',
        versionType: '',
        createTimeRange: []
      }
      this.pagination.current = 1
      this.loadData()
    },

    // 选择变化
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },

    // 排序变化
    handleSortChange({ column, prop, order }) {
      this.sortField = prop
      this.sortOrder = order === 'ascending' ? 'asc' : 'desc'
      this.loadData()
    },

    // 分页变化
    handleSizeChange(val) {
      this.pagination.size = val
      this.pagination.current = 1
      this.loadData()
    },

    handleCurrentChange(val) {
      this.pagination.current = val
      this.loadData()
    },

    // 操作处理
    handleCommand({ action, row }) {
      switch (action) {
        case 'view':
          this.handleView(row)
          break
        case 'edit':
          this.handleEdit(row)
          break
        case 'copy':
          this.handleCopy(row)
          break
        case 'branch':
          this.handleCreateBranch(row)
          break
        case 'tag':
          this.handleCreateTag(row)
          break
        case 'publish':
          this.handlePublish(row)
          break
        case 'lock':
          this.handleLock(row)
          break
        case 'unlock':
          this.handleUnlock(row)
          break
        case 'archive':
          this.handleArchive(row)
          break
        case 'rollback':
          this.handleRollback(row)
          break
        case 'delete':
          this.handleDelete(row)
          break
      }
    },

    // 查看详情
    handleView(row) {
      this.$router.push(`/managementAccountant/as/documentVersion/detail/${row.versionId}`)
    },

    // 编辑
    handleEdit(row) {
      this.$router.push(`/managementAccountant/as/documentVersion/edit/${row.versionId}`)
    },

    // 创建版本
    handleCreate() {
      this.createDialog = true
    },

    // 确认创建
    confirmCreate() {
      this.$refs.createForm.validate(async (valid) => {
        if (valid) {
          this.createLoading = true
          try {
            await createVersion(this.createForm)
            this.$message.success('创建版本成功')
            this.createDialog = false
            this.loadData()
          } catch (error) {
            this.$message.error('创建版本失败: ' + error.message)
          } finally {
            this.createLoading = false
          }
        }
      })
    },

    // 重置创建表单
    resetCreateForm() {
      this.$refs.createForm?.resetFields()
    },

    // 复制版本
    handleCopy(row) {
      this.copyForm.sourceVersionId = row.versionId
      this.copyForm.newVersionName = row.versionName + '_copy'
      this.copyDialog = true
    },

    // 确认复制
    confirmCopy() {
      this.$refs.copyForm.validate(async (valid) => {
        if (valid) {
          this.copyLoading = true
          try {
            await copyVersion(this.copyForm.sourceVersionId, this.copyForm.newVersionName)
            this.$message.success('复制版本成功')
            this.copyDialog = false
            this.loadData()
          } catch (error) {
            this.$message.error('复制版本失败: ' + error.message)
          } finally {
            this.copyLoading = false
          }
        }
      })
    },

    // 重置复制表单
    resetCopyForm() {
      this.$refs.copyForm?.resetFields()
    },

    // 创建分支
    handleCreateBranch(row) {
      this.branchForm.sourceVersionId = row.versionId
      this.branchDialog = true
    },

    // 确认创建分支
    confirmCreateBranch() {
      this.$refs.branchForm.validate(async (valid) => {
        if (valid) {
          this.branchLoading = true
          try {
            await createBranch(this.branchForm.sourceVersionId, this.branchForm.branchName, this.branchForm.description)
            this.$message.success('创建分支成功')
            this.branchDialog = false
            this.loadData()
          } catch (error) {
            this.$message.error('创建分支失败: ' + error.message)
          } finally {
            this.branchLoading = false
          }
        }
      })
    },

    // 重置分支表单
    resetBranchForm() {
      this.$refs.branchForm?.resetFields()
    },

    // 创建标签
    handleCreateTag(row) {
      this.tagForm.versionId = row.versionId
      this.tagDialog = true
    },

    // 确认创建标签
    confirmCreateTag() {
      this.$refs.tagForm.validate(async (valid) => {
        if (valid) {
          this.tagLoading = true
          try {
            await createTag(this.tagForm.versionId, this.tagForm.tagName, this.tagForm.description)
            this.$message.success('创建标签成功')
            this.tagDialog = false
            this.loadData()
          } catch (error) {
            this.$message.error('创建标签失败: ' + error.message)
          } finally {
            this.tagLoading = false
          }
        }
      })
    },

    // 重置标签表单
    resetTagForm() {
      this.$refs.tagForm?.resetFields()
    },

    // 发布版本
    async handlePublish(row) {
      try {
        await this.$confirm('确认发布此版本吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        await publishVersion(row.versionId)
        this.$message.success('发布版本成功')
        this.loadData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('发布版本失败: ' + error.message)
        }
      }
    },

    // 锁定版本
    async handleLock(row) {
      try {
        const { value } = await this.$prompt('请输入锁定原因', '锁定版本', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputPattern: /.+/,
          inputErrorMessage: '锁定原因不能为空'
        })
        
        await lockVersion(row.versionId, value)
        this.$message.success('锁定版本成功')
        this.loadData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('锁定版本失败: ' + error.message)
        }
      }
    },

    // 解锁版本
    async handleUnlock(row) {
      try {
        await this.$confirm('确认解锁此版本吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        await unlockVersion(row.versionId)
        this.$message.success('解锁版本成功')
        this.loadData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('解锁版本失败: ' + error.message)
        }
      }
    },

    // 归档版本
    async handleArchive(row) {
      try {
        const { value } = await this.$prompt('请输入归档原因', '归档版本', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputPattern: /.+/,
          inputErrorMessage: '归档原因不能为空'
        })
        
        await archiveVersion(row.versionId, value)
        this.$message.success('归档版本成功')
        this.loadData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('归档版本失败: ' + error.message)
        }
      }
    },

    // 回滚版本
    async handleRollback(row) {
      try {
        const { value } = await this.$prompt('请输入回滚原因', '回滚版本', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputPattern: /.+/,
          inputErrorMessage: '回滚原因不能为空'
        })
        
        await rollbackToVersion(row.documentId, row.versionId, value)
        this.$message.success('回滚版本成功')
        this.loadData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('回滚版本失败: ' + error.message)
        }
      }
    },

    // 删除版本
    async handleDelete(row) {
      try {
        await this.$confirm('确认删除此版本吗？删除后不可恢复！', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        await deleteVersion(row.versionId)
        this.$message.success('删除版本成功')
        this.loadData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除版本失败: ' + error.message)
        }
      }
    },

    // 批量操作
    async handleBatchPublish() {
      try {
        await this.$confirm(`确认批量发布选中的 ${this.selectedRows.length} 个版本吗？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const versionIds = this.selectedRows.map(row => row.versionId)
        await batchPublishVersions(versionIds)
        this.$message.success('批量发布成功')
        this.loadData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('批量发布失败: ' + error.message)
        }
      }
    },

    async handleBatchLock() {
      try {
        const { value } = await this.$prompt('请输入锁定原因', '批量锁定版本', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputPattern: /.+/,
          inputErrorMessage: '锁定原因不能为空'
        })
        
        const versionIds = this.selectedRows.map(row => row.versionId)
        await batchLockVersions(versionIds, value)
        this.$message.success('批量锁定成功')
        this.loadData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('批量锁定失败: ' + error.message)
        }
      }
    },

    async handleBatchArchive() {
      try {
        await this.$confirm(`确认批量归档选中的 ${this.selectedRows.length} 个版本吗？`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const versionIds = this.selectedRows.map(row => row.versionId)
        await batchArchiveVersions(versionIds)
        this.$message.success('批量归档成功')
        this.loadData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('批量归档失败: ' + error.message)
        }
      }
    },

    async handleBatchDelete() {
      try {
        await this.$confirm(`确认批量删除选中的 ${this.selectedRows.length} 个版本吗？删除后不可恢复！`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const versionIds = this.selectedRows.map(row => row.versionId)
        await batchDeleteVersions(versionIds)
        this.$message.success('批量删除成功')
        this.loadData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('批量删除失败: ' + error.message)
        }
      }
    },

    // 导入导出
    handleImport() {
      this.$message.info('导入功能开发中')
    },

    handleExport() {
      this.$message.info('导出功能开发中')
    },

    // 工具方法
    formatTime(time) {
      if (!time) return '-'
      return this.$moment(time).format('YYYY-MM-DD HH:mm:ss')
    },

    formatVersionStatus,
    formatVersionType,
    formatFileSize,

    getVersionStatusColor(status) {
      const colorMap = {
        'DRAFT': 'info',
        'UNDER_REVIEW': 'warning',
        'APPROVED': 'success',
        'PUBLISHED': 'primary',
        'ARCHIVED': 'info',
        'DEPRECATED': 'danger',
        'DELETED': 'danger'
      }
      return colorMap[status] || 'info'
    },

    getVersionTypeColor(type) {
      const colorMap = {
        'MAJOR': 'danger',
        'MINOR': 'warning',
        'PATCH': 'success',
        'DRAFT': 'info'
      }
      return colorMap[type] || 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
.document-version-list {
  padding: 20px;

  .search-area {
    background: white;
    padding: 20px;
    border-radius: 8px;
    margin-bottom: 20px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

    .search-form {
      .el-form-item {
        margin-bottom: 16px;
      }
    }
  }

  .operation-area {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;

    .operation-left {
      .el-button {
        margin-right: 8px;
      }
    }

    .operation-right {
      .el-button {
        margin-left: 8px;
      }
    }
  }

  .table-area {
    background: white;
    border-radius: 8px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

    .version-info {
      .version-name {
        font-weight: 500;
        color: #303133;
      }

      .version-number {
        font-size: 12px;
        color: #909399;
        margin-top: 2px;
      }
    }
  }

  .pagination-area {
    margin-top: 20px;
    text-align: right;
  }
}
</style>
