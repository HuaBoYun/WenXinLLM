<template>
  <div class="assessment-plan-list">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-title">
        <h2>考核方案配置</h2>
        <p>管理和配置企业绩效考核方案</p>
      </div>
      <div class="header-actions">
        <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
          新建方案
        </el-button>
        <el-button icon="el-icon-download" @click="handleExport">
          导出方案
        </el-button>
        <el-button icon="el-icon-upload2" @click="handleImport">
          导入方案
        </el-button>
      </div>
    </div>

    <!-- 搜索筛选区域 -->
    <div class="search-section">
      <el-form :model="searchForm" inline class="search-form">
        <el-form-item label="方案名称">
          <el-input
            v-model="searchForm.keyword"
            placeholder="请输入方案名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="方案类型">
          <el-select v-model="searchForm.planType" placeholder="请选择" clearable style="width: 150px">
            <el-option
              v-for="item in planTypeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="考核模式">
          <el-select v-model="searchForm.assessmentMode" placeholder="请选择" clearable style="width: 150px">
            <el-option
              v-for="item in assessmentModeOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="方案状态">
          <el-select v-model="searchForm.planStatus" placeholder="请选择" clearable style="width: 150px">
            <el-option
              v-for="item in planStatusOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="考核年度">
          <el-date-picker
            v-model="searchForm.assessmentYear"
            type="year"
            placeholder="选择年度"
            style="width: 150px"
          />
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

    <!-- 数据表格 -->
    <div class="table-section">
      <el-table
        v-loading="loading"
        :data="tableData"
        stripe
        border
        style="width: 100%"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="planCode" label="方案编码" width="150" />
        <el-table-column prop="planName" label="方案名称" min-width="200" show-overflow-tooltip />
        <el-table-column prop="planType" label="方案类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getPlanTypeTagType(scope.row.planType)">
              {{ formatPlanType(scope.row.planType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="assessmentMode" label="考核模式" width="120">
          <template slot-scope="scope">
            {{ formatAssessmentMode(scope.row.assessmentMode) }}
          </template>
        </el-table-column>
        <el-table-column prop="assessmentCycle" label="考核周期" width="100">
          <template slot-scope="scope">
            {{ formatAssessmentCycle(scope.row.assessmentCycle) }}
          </template>
        </el-table-column>
        <el-table-column prop="assessmentYear" label="考核年度" width="100" />
        <el-table-column prop="planStatus" label="方案状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getPlanStatusTagType(scope.row.planStatus)">
              {{ formatPlanStatus(scope.row.planStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="approvalStatus" label="审批状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getApprovalStatusTagType(scope.row.approvalStatus)">
              {{ formatApprovalStatus(scope.row.approvalStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="startTime" label="开始时间" width="120">
          <template slot-scope="scope">
            {{ formatDate(scope.row.startTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="endTime" label="结束时间" width="120">
          <template slot-scope="scope">
            {{ formatDate(scope.row.endTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="createdBy" label="创建人" width="100" />
        <el-table-column prop="createdTime" label="创建时间" width="150">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.createdTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleView(scope.row)">
              查看
            </el-button>
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)">
              编辑
            </el-button>
            <el-dropdown @command="handleCommand($event, scope.row)">
              <el-button size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="copy">复制</el-dropdown-item>
                <el-dropdown-item command="activate" v-if="scope.row.planStatus === 'DRAFT'">激活</el-dropdown-item>
                <el-dropdown-item command="pause" v-if="scope.row.planStatus === 'ACTIVE'">暂停</el-dropdown-item>
                <el-dropdown-item command="complete" v-if="scope.row.planStatus === 'ACTIVE'">完成</el-dropdown-item>
                <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>

      <!-- 批量操作栏 -->
      <div v-if="selectedRows.length > 0" class="batch-actions">
        <span>已选择 {{ selectedRows.length }} 项</span>
        <el-button size="small" @click="handleBatchActivate">批量激活</el-button>
        <el-button size="small" @click="handleBatchPause">批量暂停</el-button>
        <el-button size="small" type="danger" @click="handleBatchDelete">批量删除</el-button>
      </div>

      <!-- 分页 -->
      <div class="pagination-section">
        <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="pagination.current"
          :page-sizes="[10, 20, 50, 100]"
          :page-size="pagination.size"
          layout="total, sizes, prev, pager, next, jumper"
          :total="pagination.total"
        />
      </div>
    </div>

    <!-- 导入对话框 -->
    <el-dialog title="导入考核方案" :visible.sync="importDialogVisible" width="600px">
      <el-upload
        class="upload-demo"
        drag
        action=""
        :before-upload="handleBeforeUpload"
        :on-success="handleUploadSuccess"
        :on-error="handleUploadError"
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">只能上传xlsx/xls文件，且不超过10MB</div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button @click="importDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="confirmImport">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  queryAssessmentPlanPage,
  deleteAssessmentPlan,
  activateAssessmentPlan,
  pauseAssessmentPlan,
  completeAssessmentPlan,
  copyAssessmentPlan,
  batchOperateAssessmentPlans,
  exportAssessmentPlans,
  importAssessmentPlans,
  assessmentPlanUtils,
  assessmentPlanConstants
} from '@/api/managementAccountant/pm/assessmentPlan'

export default {
  name: 'AssessmentPlanList',
  data() {
    return {
      loading: false,
      tableData: [],
      selectedRows: [],
      searchForm: {
        keyword: '',
        planType: '',
        assessmentMode: '',
        planStatus: '',
        assessmentYear: null
      },
      pagination: {
        current: 1,
        size: 20,
        total: 0
      },
      importDialogVisible: false,
      planTypeOptions: assessmentPlanConstants.PLAN_TYPES,
      assessmentModeOptions: assessmentPlanConstants.ASSESSMENT_MODES,
      planStatusOptions: assessmentPlanConstants.PLAN_STATUSES
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
          keyword: this.searchForm.keyword,
          planType: this.searchForm.planType,
          assessmentMode: this.searchForm.assessmentMode,
          planStatus: this.searchForm.planStatus,
          assessmentYear: this.searchForm.assessmentYear ? this.searchForm.assessmentYear.getFullYear() : null
        }
        
        const response = await queryAssessmentPlanPage(params)
        if (response.success) {
          this.tableData = response.data.records
          this.pagination.total = response.data.total
        } else {
          this.$message.error(response.message || '查询失败')
        }
      } catch (error) {
        this.$message.error('查询失败：' + error.message)
      } finally {
        this.loading = false
      }
    },

    // 搜索
    handleSearch() {
      this.pagination.current = 1
      this.loadData()
    },

    // 重置
    handleReset() {
      this.searchForm = {
        keyword: '',
        planType: '',
        assessmentMode: '',
        planStatus: '',
        assessmentYear: null
      }
      this.pagination.current = 1
      this.loadData()
    },

    // 新建
    handleCreate() {
      this.$router.push('/pm/assessment-plan/create')
    },

    // 查看
    handleView(row) {
      this.$router.push(`/pm/assessment-plan/detail/${row.planId}`)
    },

    // 编辑
    handleEdit(row) {
      this.$router.push(`/pm/assessment-plan/edit/${row.planId}`)
    },

    // 下拉菜单操作
    async handleCommand(command, row) {
      switch (command) {
        case 'copy':
          await this.handleCopy(row)
          break
        case 'activate':
          await this.handleActivate(row)
          break
        case 'pause':
          await this.handlePause(row)
          break
        case 'complete':
          await this.handleComplete(row)
          break
        case 'delete':
          await this.handleDelete(row)
          break
      }
    },

    // 复制
    async handleCopy(row) {
      try {
        const copyParams = {
          newPlanName: row.planName + '_副本',
          copyIndicators: true,
          copyProcess: true,
          copyWeights: true
        }
        
        const response = await copyAssessmentPlan(row.planId, copyParams)
        if (response.success) {
          this.$message.success('复制成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '复制失败')
        }
      } catch (error) {
        this.$message.error('复制失败：' + error.message)
      }
    },

    // 激活
    async handleActivate(row) {
      try {
        await this.$confirm('确认激活该考核方案？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const response = await activateAssessmentPlan(row.planId)
        if (response.success) {
          this.$message.success('激活成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '激活失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('激活失败：' + error.message)
        }
      }
    },

    // 暂停
    async handlePause(row) {
      try {
        await this.$confirm('确认暂停该考核方案？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const response = await pauseAssessmentPlan(row.planId)
        if (response.success) {
          this.$message.success('暂停成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '暂停失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('暂停失败：' + error.message)
        }
      }
    },

    // 完成
    async handleComplete(row) {
      try {
        await this.$confirm('确认完成该考核方案？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const response = await completeAssessmentPlan(row.planId)
        if (response.success) {
          this.$message.success('完成成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '完成失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('完成失败：' + error.message)
        }
      }
    },

    // 删除
    async handleDelete(row) {
      try {
        await this.$confirm('确认删除该考核方案？删除后不可恢复！', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const response = await deleteAssessmentPlan(row.planId)
        if (response.success) {
          this.$message.success('删除成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败：' + error.message)
        }
      }
    },

    // 表格选择变化
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },

    // 批量激活
    async handleBatchActivate() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要激活的方案')
        return
      }
      
      try {
        const planIds = this.selectedRows.map(row => row.planId)
        const response = await batchOperateAssessmentPlans({
          operation: 'ACTIVATE',
          planIds
        })
        
        if (response.success) {
          this.$message.success('批量激活成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '批量激活失败')
        }
      } catch (error) {
        this.$message.error('批量激活失败：' + error.message)
      }
    },

    // 批量暂停
    async handleBatchPause() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要暂停的方案')
        return
      }
      
      try {
        const planIds = this.selectedRows.map(row => row.planId)
        const response = await batchOperateAssessmentPlans({
          operation: 'PAUSE',
          planIds
        })
        
        if (response.success) {
          this.$message.success('批量暂停成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '批量暂停失败')
        }
      } catch (error) {
        this.$message.error('批量暂停失败：' + error.message)
      }
    },

    // 批量删除
    async handleBatchDelete() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要删除的方案')
        return
      }
      
      try {
        await this.$confirm(`确认删除选中的 ${this.selectedRows.length} 个方案？删除后不可恢复！`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        
        const planIds = this.selectedRows.map(row => row.planId)
        const response = await batchOperateAssessmentPlans({
          operation: 'DELETE',
          planIds
        })
        
        if (response.success) {
          this.$message.success('批量删除成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '批量删除失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('批量删除失败：' + error.message)
        }
      }
    },

    // 导出
    async handleExport() {
      try {
        const response = await exportAssessmentPlans({
          exportType: 'SELECTED',
          planIds: this.selectedRows.map(row => row.planId)
        })
        
        if (response.success) {
          this.$message.success('导出成功')
          // 这里应该处理文件下载
        } else {
          this.$message.error(response.message || '导出失败')
        }
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    },

    // 导入
    handleImport() {
      this.importDialogVisible = true
    },

    // 上传前检查
    handleBeforeUpload(file) {
      const isExcel = file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' ||
                     file.type === 'application/vnd.ms-excel'
      const isLt10M = file.size / 1024 / 1024 < 10

      if (!isExcel) {
        this.$message.error('只能上传Excel文件!')
        return false
      }
      if (!isLt10M) {
        this.$message.error('上传文件大小不能超过10MB!')
        return false
      }
      return true
    },

    // 上传成功
    handleUploadSuccess(response) {
      this.$message.success('上传成功')
      this.importDialogVisible = false
      this.loadData()
    },

    // 上传失败
    handleUploadError(error) {
      this.$message.error('上传失败：' + error.message)
    },

    // 确认导入
    confirmImport() {
      this.importDialogVisible = false
    },

    // 分页大小变化
    handleSizeChange(size) {
      this.pagination.size = size
      this.pagination.current = 1
      this.loadData()
    },

    // 当前页变化
    handleCurrentChange(current) {
      this.pagination.current = current
      this.loadData()
    },

    // 格式化方法
    formatPlanType(type) {
      return assessmentPlanUtils.formatPlanType(type)
    },

    formatAssessmentMode(mode) {
      return assessmentPlanUtils.formatAssessmentMode(mode)
    },

    formatAssessmentCycle(cycle) {
      return assessmentPlanUtils.formatAssessmentCycle(cycle)
    },

    formatPlanStatus(status) {
      return assessmentPlanUtils.formatPlanStatus(status).text
    },

    formatApprovalStatus(status) {
      return assessmentPlanUtils.formatApprovalStatus(status).text
    },

    formatDate(date) {
      if (!date) return ''
      return this.$moment(date).format('YYYY-MM-DD')
    },

    formatDateTime(datetime) {
      if (!datetime) return ''
      return this.$moment(datetime).format('YYYY-MM-DD HH:mm')
    },

    // 标签类型
    getPlanTypeTagType(type) {
      const typeMap = {
        'ANNUAL': 'primary',
        'QUARTERLY': 'success',
        'MONTHLY': 'warning',
        'PROJECT': 'info'
      }
      return typeMap[type] || ''
    },

    getPlanStatusTagType(status) {
      const statusMap = {
        'DRAFT': 'info',
        'ACTIVE': 'success',
        'PAUSED': 'warning',
        'COMPLETED': 'primary',
        'CANCELLED': 'danger'
      }
      return statusMap[status] || ''
    },

    getApprovalStatusTagType(status) {
      const statusMap = {
        'PENDING': 'warning',
        'APPROVED': 'success',
        'REJECTED': 'danger'
      }
      return statusMap[status] || ''
    }
  }
}
</script>

<style scoped>
.assessment-plan-list {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  padding-bottom: 20px;
  border-bottom: 1px solid #e4e7ed;
}

.header-title h2 {
  margin: 0 0 5px 0;
  color: #303133;
}

.header-title p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.search-section {
  background: #f5f7fa;
  padding: 20px;
  border-radius: 4px;
  margin-bottom: 20px;
}

.search-form {
  margin: 0;
}

.table-section {
  background: white;
  border-radius: 4px;
  overflow: hidden;
}

.batch-actions {
  padding: 10px 20px;
  background: #f5f7fa;
  border-top: 1px solid #e4e7ed;
  display: flex;
  align-items: center;
  gap: 10px;
}

.pagination-section {
  padding: 20px;
  text-align: right;
  border-top: 1px solid #e4e7ed;
}

.upload-demo {
  text-align: center;
}
</style>
