<template>
  <div class="intelligent-audit-list">
    <!-- 搜索区域 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" :inline="true" size="small">
        <el-form-item label="审核标题">
          <el-input
            v-model="searchForm.auditTitle"
            placeholder="请输入审核标题"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="审核类型">
          <el-select
            v-model="searchForm.auditType"
            placeholder="请选择审核类型"
            clearable
            style="width: 150px"
          >
            <el-option label="财务审核" value="FINANCIAL" />
            <el-option label="合规审核" value="COMPLIANCE" />
            <el-option label="运营审核" value="OPERATIONAL" />
            <el-option label="安全审核" value="SECURITY" />
            <el-option label="质量审核" value="QUALITY" />
            <el-option label="绩效审核" value="PERFORMANCE" />
          </el-select>
        </el-form-item>
        <el-form-item label="审核状态">
          <el-select
            v-model="searchForm.auditStatus"
            placeholder="请选择审核状态"
            clearable
            style="width: 150px"
          >
            <el-option label="草稿" value="DRAFT" />
            <el-option label="进行中" value="IN_PROGRESS" />
            <el-option label="已暂停" value="PAUSED" />
            <el-option label="已完成" value="COMPLETED" />
            <el-option label="已取消" value="CANCELLED" />
            <el-option label="已复核" value="REVIEWED" />
          </el-select>
        </el-form-item>
        <el-form-item label="风险等级">
          <el-select
            v-model="searchForm.riskLevel"
            placeholder="请选择风险等级"
            clearable
            style="width: 150px"
          >
            <el-option label="低风险" value="LOW" />
            <el-option label="中风险" value="MEDIUM" />
            <el-option label="高风险" value="HIGH" />
            <el-option label="极高风险" value="CRITICAL" />
          </el-select>
        </el-form-item>
        <el-form-item label="时间范围">
          <el-date-picker
            v-model="searchForm.timeRange"
            type="datetimerange"
            range-separator="至"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            format="yyyy-MM-dd HH:mm:ss"
            value-format="yyyy-MM-dd HH:mm:ss"
            style="width: 350px"
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
          <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作区域 -->
    <el-card class="operation-card" shadow="never">
      <div class="operation-buttons">
        <el-button type="primary" icon="el-icon-plus" @click="handleAdd">新增审核</el-button>
        <el-button 
          type="success" 
          icon="el-icon-cpu" 
          :disabled="!hasSelection"
          @click="handleBatchIntelligentAudit"
        >
          批量智能审核
        </el-button>
        <el-button 
          type="warning" 
          icon="el-icon-user" 
          :disabled="!hasSelection"
          @click="handleBatchAssign"
        >
          批量分配
        </el-button>
        <el-button 
          type="danger" 
          icon="el-icon-delete" 
          :disabled="!hasSelection"
          @click="handleBatchDelete"
        >
          批量删除
        </el-button>
        <el-button type="info" icon="el-icon-download" @click="handleExport">导出数据</el-button>
      </div>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="table-card" shadow="never">
      <el-table
        v-loading="loading"
        :data="tableData"
        @selection-change="handleSelectionChange"
        stripe
        border
        style="width: 100%"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="auditCode" label="审核编码" width="150" />
        <el-table-column prop="auditTitle" label="审核标题" min-width="200" show-overflow-tooltip />
        <el-table-column prop="auditType" label="审核类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getAuditTypeTagType(scope.row.auditType)">
              {{ formatAuditType(scope.row.auditType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="auditStatus" label="审核状态" width="120">
          <template slot-scope="scope">
            <el-tag :type="getStatusTagType(scope.row.auditStatus)">
              {{ formatAuditStatus(scope.row.auditStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="riskLevel" label="风险等级" width="120">
          <template slot-scope="scope">
            <el-tag :type="getRiskLevelTagType(scope.row.riskLevel)">
              {{ formatRiskLevel(scope.row.riskLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="riskScore" label="风险评分" width="100">
          <template slot-scope="scope">
            <span v-if="scope.row.riskScore">{{ scope.row.riskScore }}%</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="auditorName" label="审核人员" width="120" show-overflow-tooltip />
        <el-table-column prop="auditDeptName" label="审核部门" width="150" show-overflow-tooltip />
        <el-table-column prop="auditStartTime" label="开始时间" width="160">
          <template slot-scope="scope">
            <span v-if="scope.row.auditStartTime">{{ formatDateTime(scope.row.auditStartTime) }}</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="auditEndTime" label="结束时间" width="160">
          <template slot-scope="scope">
            <span v-if="scope.row.auditEndTime">{{ formatDateTime(scope.row.auditEndTime) }}</span>
            <span v-else>-</span>
          </template>
        </el-table-column>
        <el-table-column prop="createdTime" label="创建时间" width="160">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.createdTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="300" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="text" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="text" @click="handleEdit(scope.row)">编辑</el-button>
            <el-dropdown @command="(command) => handleDropdownCommand(command, scope.row)">
              <el-button size="mini" type="text">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item 
                  command="start" 
                  :disabled="!canStart(scope.row)"
                >
                  启动审核
                </el-dropdown-item>
                <el-dropdown-item 
                  command="pause" 
                  :disabled="!canPause(scope.row)"
                >
                  暂停审核
                </el-dropdown-item>
                <el-dropdown-item 
                  command="resume" 
                  :disabled="!canResume(scope.row)"
                >
                  恢复审核
                </el-dropdown-item>
                <el-dropdown-item 
                  command="complete" 
                  :disabled="!canComplete(scope.row)"
                >
                  完成审核
                </el-dropdown-item>
                <el-dropdown-item 
                  command="review" 
                  :disabled="!canReview(scope.row)"
                >
                  复核审核
                </el-dropdown-item>
                <el-dropdown-item 
                  command="intelligentAudit"
                  :disabled="!canIntelligentAudit(scope.row)"
                >
                  智能审核
                </el-dropdown-item>
                <el-dropdown-item 
                  command="delete" 
                  :disabled="!canDelete(scope.row)"
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
    </el-card>

    <!-- 新增/编辑对话框 -->
    <IntelligentAuditForm
      :visible.sync="formVisible"
      :form-data="formData"
      :is-edit="isEdit"
      @success="handleFormSuccess"
    />

    <!-- 批量分配对话框 -->
    <BatchAssignDialog
      :visible.sync="batchAssignVisible"
      :audit-ids="selectedIds"
      @success="handleBatchAssignSuccess"
    />
  </div>
</template>

<script>
import { 
  getAuditPage, 
  deleteAudit, 
  batchDeleteAudits,
  startAudit,
  pauseAudit,
  resumeAudit,
  completeAudit,
  reviewAudit,
  executeIntelligentAudit,
  batchExecuteIntelligentAudit,
  exportAuditData,
  auditUtils
} from '@/api/managementAccountant/ss/intelligentAudit'
import IntelligentAuditForm from './components/IntelligentAuditForm'
import BatchAssignDialog from './components/BatchAssignDialog'

export default {
  name: 'IntelligentAuditList',
  components: {
    IntelligentAuditForm,
    BatchAssignDialog
  },
  data() {
    return {
      loading: false,
      tableData: [],
      selectedRows: [],
      searchForm: {
        auditTitle: '',
        auditType: '',
        auditStatus: '',
        riskLevel: '',
        timeRange: []
      },
      pagination: {
        current: 1,
        size: 20,
        total: 0
      },
      formVisible: false,
      formData: {},
      isEdit: false,
      batchAssignVisible: false
    }
  },
  computed: {
    hasSelection() {
      return this.selectedRows.length > 0
    },
    selectedIds() {
      return this.selectedRows.map(row => row.auditId)
    },
    tenantId() {
      return this.$store.getters.tenantId || 1
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
          tenantId: this.tenantId,
          ...this.searchForm
        }
        
        if (this.searchForm.timeRange && this.searchForm.timeRange.length === 2) {
          params.startTime = this.searchForm.timeRange[0]
          params.endTime = this.searchForm.timeRange[1]
        }
        
        const response = await getAuditPage(params)
        if (response.success) {
          this.tableData = response.data.records
          this.pagination.total = response.data.total
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

    // 重置
    handleReset() {
      this.searchForm = {
        auditTitle: '',
        auditType: '',
        auditStatus: '',
        riskLevel: '',
        timeRange: []
      }
      this.pagination.current = 1
      this.loadData()
    },

    // 分页
    handleSizeChange(size) {
      this.pagination.size = size
      this.loadData()
    },

    handleCurrentChange(current) {
      this.pagination.current = current
      this.loadData()
    },

    // 选择
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },

    // 新增
    handleAdd() {
      this.formData = {}
      this.isEdit = false
      this.formVisible = true
    },

    // 查看
    handleView(row) {
      this.$router.push({
        name: 'IntelligentAuditDetail',
        params: { id: row.auditId }
      })
    },

    // 编辑
    handleEdit(row) {
      this.formData = { ...row }
      this.isEdit = true
      this.formVisible = true
    },

    // 表单成功回调
    handleFormSuccess() {
      this.formVisible = false
      this.loadData()
    },

    // 格式化方法
    formatAuditStatus: auditUtils.formatAuditStatus,
    formatRiskLevel: auditUtils.formatRiskLevel,
    formatAuditType: auditUtils.formatAuditType,

    formatDateTime(dateTime) {
      if (!dateTime) return '-'
      return this.$moment(dateTime).format('YYYY-MM-DD HH:mm:ss')
    },

    // 获取标签类型
    getStatusTagType(status) {
      const typeMap = {
        'DRAFT': 'info',
        'IN_PROGRESS': 'primary',
        'PAUSED': 'warning',
        'COMPLETED': 'success',
        'CANCELLED': 'danger',
        'REVIEWED': 'success'
      }
      return typeMap[status] || 'info'
    },

    getRiskLevelTagType(level) {
      const typeMap = {
        'LOW': 'success',
        'MEDIUM': 'warning',
        'HIGH': 'danger',
        'CRITICAL': 'danger'
      }
      return typeMap[level] || 'info'
    },

    getAuditTypeTagType(type) {
      return 'primary'
    },

    // 权限判断
    canStart(row) {
      return ['DRAFT', 'PAUSED'].includes(row.auditStatus)
    },

    canPause(row) {
      return row.auditStatus === 'IN_PROGRESS'
    },

    canResume(row) {
      return row.auditStatus === 'PAUSED'
    },

    canComplete(row) {
      return row.auditStatus === 'IN_PROGRESS'
    },

    canReview(row) {
      return row.auditStatus === 'COMPLETED'
    },

    canIntelligentAudit(row) {
      return ['DRAFT', 'IN_PROGRESS'].includes(row.auditStatus)
    },

    canDelete(row) {
      return !['IN_PROGRESS', 'REVIEWING'].includes(row.auditStatus)
    },

    // 下拉菜单命令处理
    async handleDropdownCommand(command, row) {
      switch (command) {
        case 'start':
          await this.handleStartAudit(row)
          break
        case 'pause':
          await this.handlePauseAudit(row)
          break
        case 'resume':
          await this.handleResumeAudit(row)
          break
        case 'complete':
          await this.handleCompleteAudit(row)
          break
        case 'review':
          await this.handleReviewAudit(row)
          break
        case 'intelligentAudit':
          await this.handleIntelligentAudit(row)
          break
        case 'delete':
          await this.handleDelete(row)
          break
      }
    },

    // 启动审核
    async handleStartAudit(row) {
      try {
        await this.$prompt('请输入审核人员信息', '启动审核', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputPattern: /.+/,
          inputErrorMessage: '审核人员信息不能为空'
        })

        // 这里应该打开一个选择审核人员的对话框
        // 暂时使用当前用户信息
        const auditorInfo = {
          auditorId: this.$store.getters.userId,
          auditorName: this.$store.getters.userName,
          auditDeptId: this.$store.getters.deptId,
          auditDeptName: this.$store.getters.deptName,
          tenantId: this.tenantId
        }

        await startAudit(row.auditId, auditorInfo)
        this.$message.success('启动审核成功')
        this.loadData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('启动审核失败: ' + error.message)
        }
      }
    },

    // 暂停审核
    async handlePauseAudit(row) {
      try {
        const { value: reason } = await this.$prompt('请输入暂停原因', '暂停审核', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputPattern: /.+/,
          inputErrorMessage: '暂停原因不能为空'
        })

        await pauseAudit(row.auditId, reason, this.tenantId)
        this.$message.success('暂停审核成功')
        this.loadData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('暂停审核失败: ' + error.message)
        }
      }
    },

    // 恢复审核
    async handleResumeAudit(row) {
      try {
        await this.$confirm('确认恢复此审核吗？', '恢复审核', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        await resumeAudit(row.auditId, this.tenantId)
        this.$message.success('恢复审核成功')
        this.loadData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('恢复审核失败: ' + error.message)
        }
      }
    },

    // 完成审核
    async handleCompleteAudit(row) {
      try {
        const { value: result } = await this.$prompt('请输入审核结果', '完成审核', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputPattern: /.+/,
          inputErrorMessage: '审核结果不能为空'
        })

        const { value: conclusion } = await this.$prompt('请输入审核结论', '完成审核', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputPattern: /.+/,
          inputErrorMessage: '审核结论不能为空'
        })

        await completeAudit(row.auditId, {
          auditResult: result,
          auditConclusion: conclusion,
          tenantId: this.tenantId
        })
        this.$message.success('完成审核成功')
        this.loadData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('完成审核失败: ' + error.message)
        }
      }
    },

    // 复核审核
    async handleReviewAudit(row) {
      try {
        const { value: comments } = await this.$prompt('请输入复核意见', '复核审核', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputPattern: /.+/,
          inputErrorMessage: '复核意见不能为空'
        })

        // 这里应该打开一个复核对话框，包含复核结果选择
        const reviewInfo = {
          reviewerId: this.$store.getters.userId,
          reviewerName: this.$store.getters.userName,
          reviewComments: comments,
          reviewResult: 'APPROVED', // 默认通过
          tenantId: this.tenantId
        }

        await reviewAudit(row.auditId, reviewInfo)
        this.$message.success('复核审核成功')
        this.loadData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('复核审核失败: ' + error.message)
        }
      }
    },

    // 智能审核
    async handleIntelligentAudit(row) {
      try {
        await this.$confirm('确认执行智能审核吗？', '智能审核', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        this.loading = true
        await executeIntelligentAudit(row.auditId, this.tenantId)
        this.$message.success('智能审核执行成功')
        this.loadData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('智能审核执行失败: ' + error.message)
        }
      } finally {
        this.loading = false
      }
    },

    // 删除
    async handleDelete(row) {
      try {
        await this.$confirm('确认删除此审核吗？', '删除审核', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        await deleteAudit(row.auditId, this.tenantId)
        this.$message.success('删除成功')
        this.loadData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('删除失败: ' + error.message)
        }
      }
    },

    // 批量智能审核
    async handleBatchIntelligentAudit() {
      try {
        await this.$confirm(`确认对选中的 ${this.selectedRows.length} 条记录执行智能审核吗？`, '批量智能审核', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        this.loading = true
        await batchExecuteIntelligentAudit(this.selectedIds, this.tenantId)
        this.$message.success('批量智能审核执行成功')
        this.loadData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('批量智能审核执行失败: ' + error.message)
        }
      } finally {
        this.loading = false
      }
    },

    // 批量分配
    handleBatchAssign() {
      this.batchAssignVisible = true
    },

    // 批量分配成功回调
    handleBatchAssignSuccess() {
      this.batchAssignVisible = false
      this.loadData()
    },

    // 批量删除
    async handleBatchDelete() {
      try {
        await this.$confirm(`确认删除选中的 ${this.selectedRows.length} 条记录吗？`, '批量删除', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        await batchDeleteAudits(this.selectedIds, this.tenantId)
        this.$message.success('批量删除成功')
        this.loadData()
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('批量删除失败: ' + error.message)
        }
      }
    },

    // 导出数据
    async handleExport() {
      try {
        this.loading = true
        const params = {
          ...this.searchForm,
          tenantId: this.tenantId
        }

        if (this.searchForm.timeRange && this.searchForm.timeRange.length === 2) {
          params.startTime = this.searchForm.timeRange[0]
          params.endTime = this.searchForm.timeRange[1]
        }

        const response = await exportAuditData(params)
        if (response.success) {
          // 这里应该处理文件下载
          this.$message.success('导出成功')
        }
      } catch (error) {
        this.$message.error('导出失败: ' + error.message)
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
.intelligent-audit-list {
  padding: 20px;
}

.search-card,
.operation-card,
.table-card {
  margin-bottom: 20px;
}

.operation-buttons {
  margin-bottom: 20px;
}

.operation-buttons .el-button {
  margin-right: 10px;
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}
</style>
