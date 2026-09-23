<template>
  <div class="data-quality-list-container">
    <!-- 搜索区域 -->
    <div class="search-section">
      <el-form :model="searchForm" :inline="true" size="small">
        <el-form-item label="质量名称">
          <el-input
            v-model="searchForm.qualityName"
            placeholder="请输入质量名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="质量类型">
          <el-select
            v-model="searchForm.qualityType"
            placeholder="请选择质量类型"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="option in qualityTypeOptions"
              :key="option.value"
              :label="option.label"
              :value="option.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="质量分类">
          <el-select
            v-model="searchForm.qualityCategory"
            placeholder="请选择质量分类"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="option in qualityCategoryOptions"
              :key="option.value"
              :label="option.label"
              :value="option.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="检查状态">
          <el-select
            v-model="searchForm.checkStatus"
            placeholder="请选择检查状态"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="option in checkStatusOptions"
              :key="option.value"
              :label="option.label"
              :value="option.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="检查结果">
          <el-select
            v-model="searchForm.checkResult"
            placeholder="请选择检查结果"
            clearable
            style="width: 150px"
          >
            <el-option
              v-for="option in checkResultOptions"
              :key="option.value"
              :label="option.label"
              :value="option.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="resetSearch">重置</el-button>
        </el-form-item>
      </el-form>
    </div>

    <!-- 操作工具栏 -->
    <div class="toolbar-section">
      <div class="toolbar-left">
        <el-button type="primary" icon="el-icon-plus" @click="showCreateDialog">新增</el-button>
        <el-button
          type="success"
          icon="el-icon-refresh"
          :disabled="selectedRows.length === 0"
          @click="batchExecuteCheck"
        >
          批量检查
        </el-button>
        <el-button
          type="warning"
          icon="el-icon-setting"
          :disabled="selectedRows.length === 0"
          @click="batchFix"
        >
          批量修复
        </el-button>
        <el-button
          type="danger"
          icon="el-icon-delete"
          :disabled="selectedRows.length === 0"
          @click="batchDelete"
        >
          批量删除
        </el-button>
      </div>
      <div class="toolbar-right">
        <el-tooltip content="刷新" placement="top">
          <el-button icon="el-icon-refresh" circle @click="loadTableData" />
        </el-tooltip>
      </div>
    </div>

    <!-- 数据表格 -->
    <div class="table-section">
      <el-table
        v-loading="tableLoading"
        :data="tableData"
        border
        stripe
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column prop="qualityCode" label="质量编码" width="150" />
        <el-table-column prop="qualityName" label="质量名称" min-width="200" />
        <el-table-column prop="qualityType" label="质量类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getQualityTypeTagType(scope.row.qualityType)" size="small">
              {{ formatQualityType(scope.row.qualityType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="qualityCategory" label="质量分类" width="120">
          <template slot-scope="scope">
            <el-tag :type="getQualityCategoryTagType(scope.row.qualityCategory)" size="small">
              {{ formatQualityCategory(scope.row.qualityCategory) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="checkStatus" label="检查状态" width="120">
          <template slot-scope="scope">
            <el-tag :type="getCheckStatusTagType(scope.row.checkStatus)" size="small">
              {{ formatCheckStatus(scope.row.checkStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="checkResult" label="检查结果" width="120">
          <template slot-scope="scope">
            <el-tag
              v-if="scope.row.checkResult"
              :type="getCheckResultTagType(scope.row.checkResult)"
              size="small"
            >
              {{ formatCheckResult(scope.row.checkResult) }}
            </el-tag>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="qualityScore" label="质量得分" width="120">
          <template slot-scope="scope">
            <span v-if="scope.row.qualityScore">{{ scope.row.qualityScore }}分</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="issueCount" label="问题数量" width="100">
          <template slot-scope="scope">
            <span v-if="scope.row.issueCount > 0" style="color: #f56c6c">
              {{ scope.row.issueCount }}
            </span>
            <span v-else>0</span>
          </template>
        </el-table-column>
        <el-table-column prop="checkTime" label="检查时间" width="160">
          <template slot-scope="scope">
            <span v-if="scope.row.checkTime">{{ scope.row.checkTime }}</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="createdTime" label="创建时间" width="160" />
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-dropdown @command="handleCommand">
              <el-button type="text" size="small">
                操作<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item :command="{action: 'detail', row: scope.row}">
                  <i class="el-icon-view"></i> 详情
                </el-dropdown-item>
                <el-dropdown-item :command="{action: 'edit', row: scope.row}">
                  <i class="el-icon-edit"></i> 编辑
                </el-dropdown-item>
                <el-dropdown-item :command="{action: 'check', row: scope.row}">
                  <i class="el-icon-refresh"></i> 执行检查
                </el-dropdown-item>
                <el-dropdown-item
                  v-if="scope.row.checkResult === 'FAIL' || scope.row.checkResult === 'WARNING'"
                  :command="{action: 'fix', row: scope.row}"
                >
                  <i class="el-icon-setting"></i> 修复问题
                </el-dropdown-item>
                <el-dropdown-item :command="{action: 'test', row: scope.row}">
                  <i class="el-icon-connection"></i> 测试连接
                </el-dropdown-item>
                <el-dropdown-item
                  :command="{action: scope.row.status === 'ACTIVE' ? 'disable' : 'enable', row: scope.row}"
                >
                  <i :class="scope.row.status === 'ACTIVE' ? 'el-icon-close' : 'el-icon-check'"></i>
                  {{ scope.row.status === 'ACTIVE' ? '禁用' : '启用' }}
                </el-dropdown-item>
                <el-dropdown-item :command="{action: 'delete', row: scope.row}" divided>
                  <i class="el-icon-delete"></i> 删除
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 分页 -->
    <div class="pagination-section">
      <el-pagination
        :current-page="pagination.pageNum"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pagination.pageSize"
        :total="pagination.total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- 编辑对话框 -->
    <el-dialog
      :title="editForm.qualityId ? '编辑数据质量' : '新增数据质量'"
      :visible.sync="editDialogVisible"
      width="800px"
      @close="resetEditForm"
    >
      <el-form
        ref="editForm"
        :model="editForm"
        :rules="editRules"
        label-width="120px"
      >
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="质量编码" prop="qualityCode">
              <el-input v-model="editForm.qualityCode" placeholder="请输入质量编码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="质量名称" prop="qualityName">
              <el-input v-model="editForm.qualityName" placeholder="请输入质量名称" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="质量类型" prop="qualityType">
              <el-select v-model="editForm.qualityType" placeholder="请选择质量类型" style="width: 100%">
                <el-option
                  v-for="option in qualityTypeOptions"
                  :key="option.value"
                  :label="option.label"
                  :value="option.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="质量分类" prop="qualityCategory">
              <el-select v-model="editForm.qualityCategory" placeholder="请选择质量分类" style="width: 100%">
                <el-option
                  v-for="option in qualityCategoryOptions"
                  :key="option.value"
                  :label="option.label"
                  :value="option.value"
                />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="质量模块" prop="qualityModule">
              <el-select v-model="editForm.qualityModule" placeholder="请选择质量模块" style="width: 100%">
                <el-option label="预算模块" value="BUDGET" />
                <el-option label="报表模块" value="REPORT" />
                <el-option label="分析模块" value="ANALYSIS" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="数据源ID" prop="dataSourceId">
              <el-input v-model="editForm.dataSourceId" placeholder="请输入数据源ID" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="自动修复">
              <el-switch v-model="editForm.autoFix" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="发送通知">
              <el-switch v-model="editForm.sendNotification" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="质量描述">
          <el-input
            v-model="editForm.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入质量描述"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="editDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getDataQualityPage,
  createDataQuality,
  updateDataQuality,
  deleteDataQuality,
  executeDataQualityCheck,
  batchExecuteDataQualityCheck,
  fixDataQualityIssue,
  batchFixDataQualityIssue,
  batchEnableDataQuality,
  batchDisableDataQuality,
  batchDeleteDataQuality,
  testDataQualityConnection,
  getDataQualityOptions,
  formatDataQualityType,
  formatDataQualityCategory,
  formatCheckStatus,
  formatCheckResult,
  getCheckStatusTagType,
  getCheckResultTagType
} from '@/api/managementAccountant/eps/dataQuality'

export default {
  name: 'DataQualityList',
  data() {
    return {
      // 搜索表单
      searchForm: {
        qualityName: '',
        qualityType: '',
        qualityCategory: '',
        checkStatus: '',
        checkResult: ''
      },
      // 表格数据
      tableData: [],
      tableLoading: false,
      selectedRows: [],
      // 分页
      pagination: {
        pageNum: 1,
        pageSize: 10,
        total: 0
      },
      // 编辑对话框
      editDialogVisible: false,
      editForm: {
        qualityId: '',
        qualityCode: '',
        qualityName: '',
        qualityType: '',
        qualityCategory: '',
        qualityModule: '',
        dataSourceId: '',
        autoFix: false,
        sendNotification: true,
        remark: ''
      },
      editRules: {
        qualityCode: [
          { required: true, message: '请输入质量编码', trigger: 'blur' },
          { pattern: /^[A-Z0-9_]{3,50}$/, message: '编码格式不正确', trigger: 'blur' }
        ],
        qualityName: [
          { required: true, message: '请输入质量名称', trigger: 'blur' }
        ],
        qualityType: [
          { required: true, message: '请选择质量类型', trigger: 'change' }
        ],
        qualityCategory: [
          { required: true, message: '请选择质量分类', trigger: 'change' }
        ],
        qualityModule: [
          { required: true, message: '请选择质量模块', trigger: 'change' }
        ]
      },
      // 选项数据
      qualityTypeOptions: [],
      qualityCategoryOptions: [],
      checkStatusOptions: [],
      checkResultOptions: []
    }
  },
  created() {
    this.loadOptions()
    this.loadTableData()
  },
  methods: {
    // 加载选项数据
    loadOptions() {
      const options = getDataQualityOptions()
      this.qualityTypeOptions = options.qualityTypes
      this.qualityCategoryOptions = options.qualityCategories
      this.checkStatusOptions = options.checkStatuses
      this.checkResultOptions = options.checkResults
    },
    // 加载表格数据
    async loadTableData() {
      this.tableLoading = true
      try {
        const params = {
          pageNum: this.pagination.pageNum,
          pageSize: this.pagination.pageSize,
          ...this.searchForm
        }
        const response = await getDataQualityPage(params)
        if (response.success) {
          this.tableData = response.data.records || []
          this.pagination.total = response.data.total || 0
        }
      } catch (error) {
        this.$message.error('加载数据失败')
        console.error('加载表格数据失败:', error)
      } finally {
        this.tableLoading = false
      }
    },
    // 搜索
    handleSearch() {
      this.pagination.pageNum = 1
      this.loadTableData()
    },
    // 重置搜索
    resetSearch() {
      this.searchForm = {
        qualityName: '',
        qualityType: '',
        qualityCategory: '',
        checkStatus: '',
        checkResult: ''
      }
      this.pagination.pageNum = 1
      this.loadTableData()
    },
    // 选择变化
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },
    // 分页大小变化
    handleSizeChange(size) {
      this.pagination.pageSize = size
      this.loadTableData()
    },
    // 当前页变化
    handleCurrentChange(page) {
      this.pagination.pageNum = page
      this.loadTableData()
    },
    // 显示创建对话框
    showCreateDialog() {
      this.editDialogVisible = true
    },
    // 格式化方法
    formatDataQualityType,
    formatDataQualityCategory,
    formatCheckStatus,
    formatCheckResult,
    getCheckStatusTagType,
    getCheckResultTagType,
    // 获取质量类型标签类型
    getQualityTypeTagType(type) {
      const typeMap = {
        COMPLETENESS: 'primary',
        ACCURACY: 'success',
        CONSISTENCY: 'warning',
        VALIDITY: 'info',
        UNIQUENESS: 'danger',
        TIMELINESS: ''
      }
      return typeMap[type] || 'info'
    },
    // 获取质量分类标签类型
    getQualityCategoryTagType(category) {
      const categoryMap = {
        DATA: 'primary',
        PROCESS: 'success',
        SYSTEM: 'warning'
      }
      return categoryMap[category] || 'info'
    },
    // 处理命令
    async handleCommand(command) {
      const { action, row } = command
      switch (action) {
        case 'detail':
          this.viewDetail(row)
          break
        case 'edit':
          this.editRow(row)
          break
        case 'check':
          this.executeCheck(row)
          break
        case 'fix':
          this.fixIssue(row)
          break
        case 'test':
          this.testConnection(row)
          break
        case 'enable':
          this.enableRow(row)
          break
        case 'disable':
          this.disableRow(row)
          break
        case 'delete':
          this.deleteRow(row)
          break
      }
    },
    // 查看详情
    viewDetail(row) {
      this.$router.push(`/eps/data-quality/detail/${row.qualityId}`)
    },
    // 编辑行
    editRow(row) {
      this.editForm = { ...row }
      this.editDialogVisible = true
    },
    // 执行检查
    async executeCheck(row) {
      try {
        this.$loading({
          lock: true,
          text: '正在执行质量检查...',
          spinner: 'el-icon-loading'
        })

        const response = await executeDataQualityCheck(row.qualityId)
        this.$loading().close()

        if (response.success) {
          this.$message.success('质量检查执行成功')
          this.loadTableData()
        } else {
          this.$message.error(response.message || '质量检查执行失败')
        }
      } catch (error) {
        this.$loading().close()
        this.$message.error('质量检查执行异常')
        console.error('执行质量检查失败:', error)
      }
    },
    // 修复问题
    async fixIssue(row) {
      try {
        const response = await fixDataQualityIssue(row.qualityId)
        if (response.success) {
          this.$message.success('问题修复成功')
          this.loadTableData()
        } else {
          this.$message.error(response.message || '问题修复失败')
        }
      } catch (error) {
        this.$message.error('问题修复异常')
        console.error('修复问题失败:', error)
      }
    },
    // 测试连接
    async testConnection(row) {
      try {
        const response = await testDataQualityConnection(row.qualityId)
        if (response.success) {
          if (response.data) {
            this.$message.success('连接测试成功')
          } else {
            this.$message.error('连接测试失败')
          }
        } else {
          this.$message.error(response.message || '连接测试失败')
        }
      } catch (error) {
        this.$message.error('连接测试异常')
        console.error('测试连接失败:', error)
      }
    },
    // 启用行
    async enableRow(row) {
      try {
        const response = await batchEnableDataQuality([row.qualityId])
        if (response.success) {
          this.$message.success('启用成功')
          this.loadTableData()
        } else {
          this.$message.error(response.message || '启用失败')
        }
      } catch (error) {
        this.$message.error('启用异常')
        console.error('启用失败:', error)
      }
    },
    // 禁用行
    async disableRow(row) {
      try {
        const response = await batchDisableDataQuality([row.qualityId])
        if (response.success) {
          this.$message.success('禁用成功')
          this.loadTableData()
        } else {
          this.$message.error(response.message || '禁用失败')
        }
      } catch (error) {
        this.$message.error('禁用异常')
        console.error('禁用失败:', error)
      }
    },
    // 删除行
    deleteRow(row) {
      this.$confirm('确定要删除这条数据质量记录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await deleteDataQuality(row.qualityId)
          if (response.success) {
            this.$message.success('删除成功')
            this.loadTableData()
          } else {
            this.$message.error(response.message || '删除失败')
          }
        } catch (error) {
          this.$message.error('删除异常')
          console.error('删除失败:', error)
        }
      })
    },
    // 批量执行检查
    async batchExecuteCheck() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要检查的数据')
        return
      }

      try {
        this.$loading({
          lock: true,
          text: '正在批量执行质量检查...',
          spinner: 'el-icon-loading'
        })

        const qualityIds = this.selectedRows.map(row => row.qualityId)
        const response = await batchExecuteDataQualityCheck(qualityIds)
        this.$loading().close()

        if (response.success) {
          this.$message.success('批量质量检查执行成功')
          this.loadTableData()
        } else {
          this.$message.error(response.message || '批量质量检查执行失败')
        }
      } catch (error) {
        this.$loading().close()
        this.$message.error('批量质量检查执行异常')
        console.error('批量执行检查失败:', error)
      }
    },
    // 批量修复
    async batchFix() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要修复的数据')
        return
      }

      try {
        const qualityIds = this.selectedRows.map(row => row.qualityId)
        const response = await batchFixDataQualityIssue(qualityIds)
        if (response.success) {
          this.$message.success('批量修复成功')
          this.loadTableData()
        } else {
          this.$message.error(response.message || '批量修复失败')
        }
      } catch (error) {
        this.$message.error('批量修复异常')
        console.error('批量修复失败:', error)
      }
    },
    // 批量删除
    batchDelete() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要删除的数据')
        return
      }

      this.$confirm(`确定要删除选中的 ${this.selectedRows.length} 条数据质量记录吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const qualityIds = this.selectedRows.map(row => row.qualityId)
          const response = await batchDeleteDataQuality(qualityIds)
          if (response.success) {
            this.$message.success('批量删除成功')
            this.loadTableData()
          } else {
            this.$message.error(response.message || '批量删除失败')
          }
        } catch (error) {
          this.$message.error('批量删除异常')
          console.error('批量删除失败:', error)
        }
      })
    },
    // 处理提交
    handleSubmit() {
      this.$refs.editForm.validate(async (valid) => {
        if (valid) {
          try {
            let response
            if (this.editForm.qualityId) {
              response = await updateDataQuality(this.editForm)
            } else {
              response = await createDataQuality(this.editForm)
            }

            if (response.success) {
              this.$message.success(this.editForm.qualityId ? '更新成功' : '创建成功')
              this.editDialogVisible = false
              this.loadTableData()
            } else {
              this.$message.error(response.message || (this.editForm.qualityId ? '更新失败' : '创建失败'))
            }
          } catch (error) {
            this.$message.error(this.editForm.qualityId ? '更新异常' : '创建异常')
            console.error('提交失败:', error)
          }
        }
      })
    },
    // 重置编辑表单
    resetEditForm() {
      this.$refs.editForm && this.$refs.editForm.resetFields()
      this.editForm = {
        qualityId: '',
        qualityCode: '',
        qualityName: '',
        qualityType: '',
        qualityCategory: '',
        qualityModule: '',
        dataSourceId: '',
        autoFix: false,
        sendNotification: true,
        remark: ''
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.data-quality-list-container {
  padding: 20px;
  
  .search-section {
    background: #fff;
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    margin-bottom: 20px;
  }
  
  .toolbar-section {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    
    .toolbar-left {
      display: flex;
      gap: 10px;
    }
  }
  
  .table-section {
    background: #fff;
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  }
  
  .pagination-section {
    display: flex;
    justify-content: center;
    margin-top: 20px;
  }
}
</style>
