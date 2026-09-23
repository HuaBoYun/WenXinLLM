<template>
  <div class="performance-interview-list">
    <!-- 搜索区域 -->
    <div class="search-section">
      <el-card>
        <el-form :model="searchForm" :inline="true" size="small">
          <el-form-item label="面谈编码">
            <el-input v-model="searchForm.interviewCode" placeholder="请输入面谈编码" clearable />
          </el-form-item>
          <el-form-item label="面谈标题">
            <el-input v-model="searchForm.interviewTitle" placeholder="请输入面谈标题" clearable />
          </el-form-item>
          <el-form-item label="面谈类型">
            <el-select v-model="searchForm.interviewType" placeholder="请选择面谈类型" clearable>
              <el-option label="年度面谈" value="ANNUAL" />
              <el-option label="季度面谈" value="QUARTERLY" />
              <el-option label="月度面谈" value="MONTHLY" />
              <el-option label="项目面谈" value="PROJECT" />
              <el-option label="专项面谈" value="SPECIAL" />
            </el-select>
          </el-form-item>
          <el-form-item label="面谈状态">
            <el-select v-model="searchForm.interviewStatus" placeholder="请选择面谈状态" clearable>
              <el-option label="已计划" value="PLANNED" />
              <el-option label="已安排" value="SCHEDULED" />
              <el-option label="进行中" value="ONGOING" />
              <el-option label="已完成" value="COMPLETED" />
              <el-option label="已取消" value="CANCELLED" />
              <el-option label="已延期" value="POSTPONED" />
            </el-select>
          </el-form-item>
          <el-form-item label="被面谈人">
            <el-input v-model="searchForm.intervieweeName" placeholder="请输入被面谈人姓名" clearable />
          </el-form-item>
          <el-form-item label="面谈官">
            <el-input v-model="searchForm.interviewerName" placeholder="请输入面谈官姓名" clearable />
          </el-form-item>
          <el-form-item label="面谈年度">
            <el-date-picker
              v-model="searchForm.interviewYear"
              type="year"
              placeholder="选择年度"
              value-format="yyyy"
            />
          </el-form-item>
          <el-form-item label="优先级">
            <el-select v-model="searchForm.priorityLevel" placeholder="请选择优先级" clearable>
              <el-option label="高" value="HIGH" />
              <el-option label="中" value="MEDIUM" />
              <el-option label="低" value="LOW" />
            </el-select>
          </el-form-item>
          <el-form-item label="跟进状态">
            <el-select v-model="searchForm.followUpStatus" placeholder="请选择跟进状态" clearable>
              <el-option label="待跟进" value="PENDING" />
              <el-option label="跟进中" value="IN_PROGRESS" />
              <el-option label="已完成" value="COMPLETED" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="handleSearch">搜索</el-button>
            <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
            <el-button type="success" icon="el-icon-download" @click="handleExport">导出</el-button>
          </el-form-item>
        </el-form>
      </el-card>
    </div>

    <!-- 操作区域 -->
    <div class="action-section">
      <el-card>
        <div class="action-buttons">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">新建面谈</el-button>
          <el-button type="success" icon="el-icon-upload2" @click="handleImport">导入面谈</el-button>
          <el-button 
            type="warning" 
            icon="el-icon-edit" 
            :disabled="selectedRows.length === 0"
            @click="handleBatchUpdate"
          >
            批量更新
          </el-button>
          <el-button 
            type="danger" 
            icon="el-icon-delete" 
            :disabled="selectedRows.length === 0"
            @click="handleBatchDelete"
          >
            批量删除
          </el-button>
          <el-button type="info" icon="el-icon-bell" @click="handleBatchNotify">批量通知</el-button>
        </div>
      </el-card>
    </div>

    <!-- 数据表格 -->
    <div class="table-section">
      <el-card>
        <el-table
          v-loading="loading"
          :data="tableData"
          stripe
          border
          @selection-change="handleSelectionChange"
          @sort-change="handleSortChange"
        >
          <el-table-column type="selection" width="55" />
          <el-table-column prop="interviewCode" label="面谈编码" width="150" sortable="custom" />
          <el-table-column prop="interviewTitle" label="面谈标题" min-width="200" show-overflow-tooltip />
          <el-table-column prop="interviewType" label="面谈类型" width="120">
            <template slot-scope="scope">
              <el-tag :type="getTypeTagType(scope.row.interviewType)" size="small">
                {{ formatInterviewType(scope.row.interviewType) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="interviewStatus" label="面谈状态" width="120">
            <template slot-scope="scope">
              <el-tag :type="getStatusTagType(scope.row.interviewStatus)" size="small">
                {{ formatInterviewStatus(scope.row.interviewStatus) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="intervieweeName" label="被面谈人" width="120" />
          <el-table-column prop="interviewerName" label="面谈官" width="120" />
          <el-table-column prop="plannedStartTime" label="计划时间" width="160" sortable="custom">
            <template slot-scope="scope">
              {{ formatDateTime(scope.row.plannedStartTime) }}
            </template>
          </el-table-column>
          <el-table-column prop="priorityLevel" label="优先级" width="100">
            <template slot-scope="scope">
              <el-tag :type="getPriorityTagType(scope.row.priorityLevel)" size="small">
                {{ formatPriorityLevel(scope.row.priorityLevel) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="needFollowUp" label="需要跟进" width="100">
            <template slot-scope="scope">
              <el-tag :type="scope.row.needFollowUp ? 'warning' : 'info'" size="small">
                {{ scope.row.needFollowUp ? '是' : '否' }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="satisfactionRating" label="满意度" width="100">
            <template slot-scope="scope">
              <el-rate
                v-if="scope.row.satisfactionRating"
                :value="scope.row.satisfactionRating"
                disabled
                show-score
                text-color="#ff9900"
                score-template="{value}"
              />
              <span v-else>--</span>
            </template>
          </el-table-column>
          <el-table-column prop="createdTime" label="创建时间" width="160" sortable="custom">
            <template slot-scope="scope">
              {{ formatDateTime(scope.row.createdTime) }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="280" fixed="right">
            <template slot-scope="scope">
              <el-button size="mini" type="text" @click="handleView(scope.row)">查看</el-button>
              <el-button 
                v-if="scope.row.interviewStatus === 'PLANNED'"
                size="mini" 
                type="text" 
                @click="handleEdit(scope.row)"
              >
                编辑
              </el-button>
              <el-button 
                v-if="scope.row.interviewStatus === 'PLANNED'"
                size="mini" 
                type="text" 
                @click="handleSchedule(scope.row)"
              >
                安排
              </el-button>
              <el-button 
                v-if="scope.row.interviewStatus === 'SCHEDULED'"
                size="mini" 
                type="text" 
                @click="handleStart(scope.row)"
              >
                开始
              </el-button>
              <el-button 
                v-if="scope.row.interviewStatus === 'ONGOING'"
                size="mini" 
                type="text" 
                @click="handleComplete(scope.row)"
              >
                完成
              </el-button>
              <el-dropdown @command="handleCommand" trigger="click">
                <el-button size="mini" type="text">
                  更多<i class="el-icon-arrow-down el-icon--right"></i>
                </el-button>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item :command="{action: 'copy', row: scope.row}">复制</el-dropdown-item>
                  <el-dropdown-item :command="{action: 'postpone', row: scope.row}">延期</el-dropdown-item>
                  <el-dropdown-item :command="{action: 'cancel', row: scope.row}">取消</el-dropdown-item>
                  <el-dropdown-item :command="{action: 'notify', row: scope.row}">通知</el-dropdown-item>
                  <el-dropdown-item :command="{action: 'report', row: scope.row}">报告</el-dropdown-item>
                  <el-dropdown-item 
                    v-if="scope.row.interviewStatus === 'PLANNED'"
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
      </el-card>
    </div>

    <!-- 安排面谈对话框 -->
    <el-dialog
      title="安排面谈"
      :visible.sync="scheduleDialogVisible"
      width="600px"
      @close="handleScheduleDialogClose"
    >
      <el-form ref="scheduleForm" :model="scheduleForm" :rules="scheduleRules" label-width="120px">
        <el-form-item label="计划开始时间" prop="plannedStartTime">
          <el-date-picker
            v-model="scheduleForm.plannedStartTime"
            type="datetime"
            placeholder="选择开始时间"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="计划结束时间" prop="plannedEndTime">
          <el-date-picker
            v-model="scheduleForm.plannedEndTime"
            type="datetime"
            placeholder="选择结束时间"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="面谈地点" prop="interviewLocation">
          <el-input v-model="scheduleForm.interviewLocation" placeholder="请输入面谈地点" />
        </el-form-item>
        <el-form-item label="面谈方式" prop="interviewMethod">
          <el-select v-model="scheduleForm.interviewMethod" placeholder="请选择面谈方式" style="width: 100%">
            <el-option label="面对面" value="FACE_TO_FACE" />
            <el-option label="视频" value="VIDEO" />
            <el-option label="电话" value="PHONE" />
            <el-option label="在线" value="ONLINE" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="scheduleDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleScheduleConfirm" :loading="scheduleLoading">确定</el-button>
      </div>
    </el-dialog>

    <!-- 批量更新对话框 -->
    <el-dialog
      title="批量更新"
      :visible.sync="batchUpdateDialogVisible"
      width="500px"
    >
      <el-form :model="batchUpdateForm" label-width="120px">
        <el-form-item label="更新状态">
          <el-select v-model="batchUpdateForm.status" placeholder="请选择状态" style="width: 100%">
            <el-option label="已计划" value="PLANNED" />
            <el-option label="已安排" value="SCHEDULED" />
            <el-option label="已取消" value="CANCELLED" />
            <el-option label="已延期" value="POSTPONED" />
          </el-select>
        </el-form-item>
        <el-form-item label="更新原因">
          <el-input
            v-model="batchUpdateForm.reason"
            type="textarea"
            :rows="3"
            placeholder="请输入更新原因"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="batchUpdateDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleBatchUpdateConfirm" :loading="batchUpdateLoading">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  queryInterviewPage,
  deleteInterview,
  batchDeleteInterviews,
  scheduleInterview,
  startInterview,
  completeInterview,
  cancelInterview,
  postponeInterview,
  copyInterview,
  batchUpdateStatus,
  sendInterviewNotification,
  batchSendNotifications,
  exportInterviewData,
  formatInterviewStatus,
  formatInterviewType,
  formatPriorityLevel,
  getStatusTagType,
  getPriorityTagType
} from '@/api/managementAccountant/pm/performanceInterview'

export default {
  name: 'PerformanceInterviewList',
  data() {
    return {
      loading: false,
      tableData: [],
      selectedRows: [],
      
      // 搜索表单
      searchForm: {
        interviewCode: '',
        interviewTitle: '',
        interviewType: '',
        interviewStatus: '',
        intervieweeName: '',
        interviewerName: '',
        interviewYear: '',
        priorityLevel: '',
        followUpStatus: ''
      },

      // 分页
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },

      // 排序
      sortField: '',
      sortOrder: '',

      // 安排面谈对话框
      scheduleDialogVisible: false,
      scheduleLoading: false,
      scheduleForm: {
        interviewId: null,
        plannedStartTime: null,
        plannedEndTime: null,
        interviewLocation: '',
        interviewMethod: 'FACE_TO_FACE'
      },
      scheduleRules: {
        plannedStartTime: [
          { required: true, message: '请选择开始时间', trigger: 'change' }
        ],
        plannedEndTime: [
          { required: true, message: '请选择结束时间', trigger: 'change' }
        ],
        interviewLocation: [
          { required: true, message: '请输入面谈地点', trigger: 'blur' }
        ],
        interviewMethod: [
          { required: true, message: '请选择面谈方式', trigger: 'change' }
        ]
      },

      // 批量更新对话框
      batchUpdateDialogVisible: false,
      batchUpdateLoading: false,
      batchUpdateForm: {
        status: '',
        reason: ''
      }
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

        if (this.sortField) {
          params.sortField = this.sortField
          params.sortOrder = this.sortOrder
        }

        const response = await queryInterviewPage(params)
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
        interviewCode: '',
        interviewTitle: '',
        interviewType: '',
        interviewStatus: '',
        intervieweeName: '',
        interviewerName: '',
        interviewYear: '',
        priorityLevel: '',
        followUpStatus: ''
      }
      this.pagination.current = 1
      this.loadData()
    },

    // 新建
    handleCreate() {
      this.$router.push('/pm/performance-interview/create')
    },

    // 查看
    handleView(row) {
      this.$router.push(`/pm/performance-interview/detail/${row.interviewId}`)
    },

    // 编辑
    handleEdit(row) {
      this.$router.push(`/pm/performance-interview/edit/${row.interviewId}`)
    },

    // 安排面谈
    handleSchedule(row) {
      this.scheduleForm.interviewId = row.interviewId
      this.scheduleDialogVisible = true
    },

    // 安排面谈确认
    async handleScheduleConfirm() {
      try {
        await this.$refs.scheduleForm.validate()
        
        this.scheduleLoading = true
        const response = await scheduleInterview(this.scheduleForm.interviewId, this.scheduleForm)
        
        if (response.success) {
          this.$message.success('安排成功')
          this.scheduleDialogVisible = false
          this.loadData()
        } else {
          this.$message.error(response.message || '安排失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('安排失败：' + error.message)
        }
      } finally {
        this.scheduleLoading = false
      }
    },

    // 安排对话框关闭
    handleScheduleDialogClose() {
      this.$refs.scheduleForm.resetFields()
      this.scheduleForm.interviewId = null
    },

    // 开始面谈
    async handleStart(row) {
      try {
        await this.$confirm('确认开始该面谈？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const response = await startInterview(row.interviewId)
        if (response.success) {
          this.$message.success('开始成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '开始失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('开始失败：' + error.message)
        }
      }
    },

    // 完成面谈
    handleComplete(row) {
      this.$router.push(`/pm/performance-interview/complete/${row.interviewId}`)
    },

    // 删除
    async handleDelete(row) {
      try {
        await this.$confirm('确认删除该面谈？删除后不可恢复。', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const response = await deleteInterview(row.interviewId)
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

    // 批量删除
    async handleBatchDelete() {
      try {
        await this.$confirm(`确认删除选中的${this.selectedRows.length}个面谈？删除后不可恢复。`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const ids = this.selectedRows.map(row => row.interviewId)
        const response = await batchDeleteInterviews(ids)
        
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

    // 批量更新
    handleBatchUpdate() {
      this.batchUpdateDialogVisible = true
    },

    // 批量更新确认
    async handleBatchUpdateConfirm() {
      if (!this.batchUpdateForm.status) {
        this.$message.warning('请选择要更新的状态')
        return
      }

      try {
        this.batchUpdateLoading = true
        const ids = this.selectedRows.map(row => row.interviewId)
        const response = await batchUpdateStatus(ids, this.batchUpdateForm.status, {
          reason: this.batchUpdateForm.reason
        })

        if (response.success) {
          this.$message.success('批量更新成功')
          this.batchUpdateDialogVisible = false
          this.loadData()
        } else {
          this.$message.error(response.message || '批量更新失败')
        }
      } catch (error) {
        this.$message.error('批量更新失败：' + error.message)
      } finally {
        this.batchUpdateLoading = false
      }
    },

    // 批量通知
    async handleBatchNotify() {
      try {
        const ids = this.selectedRows.map(row => row.interviewId)
        const response = await batchSendNotifications(ids, {
          notificationType: 'REMINDER'
        })

        if (response.success) {
          this.$message.success(`通知发送成功，成功${response.data.successCount}个，失败${response.data.failCount}个`)
        } else {
          this.$message.error(response.message || '通知发送失败')
        }
      } catch (error) {
        this.$message.error('通知发送失败：' + error.message)
      }
    },

    // 导出
    async handleExport() {
      try {
        const response = await exportInterviewData(this.searchForm)
        if (response.success) {
          // 这里可以处理导出逻辑，比如下载文件
          this.$message.success('导出成功')
        } else {
          this.$message.error(response.message || '导出失败')
        }
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      }
    },

    // 导入
    handleImport() {
      this.$message.info('导入功能开发中')
    },

    // 下拉菜单命令处理
    async handleCommand(command) {
      const { action, row } = command
      
      switch (action) {
        case 'copy':
          await this.handleCopy(row)
          break
        case 'postpone':
          await this.handlePostpone(row)
          break
        case 'cancel':
          await this.handleCancel(row)
          break
        case 'notify':
          await this.handleNotify(row)
          break
        case 'report':
          this.handleReport(row)
          break
        case 'delete':
          await this.handleDelete(row)
          break
      }
    },

    // 复制面谈
    async handleCopy(row) {
      try {
        const response = await copyInterview(row.interviewId)
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

    // 延期面谈
    async handlePostpone(row) {
      this.$message.info('延期功能开发中')
    },

    // 取消面谈
    async handleCancel(row) {
      try {
        await this.$confirm('确认取消该面谈？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })

        const response = await cancelInterview(row.interviewId, {
          cancelReason: '手动取消'
        })

        if (response.success) {
          this.$message.success('取消成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '取消失败')
        }
      } catch (error) {
        if (error !== 'cancel') {
          this.$message.error('取消失败：' + error.message)
        }
      }
    },

    // 发送通知
    async handleNotify(row) {
      try {
        const response = await sendInterviewNotification(row.interviewId, {
          notificationType: 'REMINDER'
        })

        if (response.success) {
          this.$message.success('通知发送成功')
        } else {
          this.$message.error(response.message || '通知发送失败')
        }
      } catch (error) {
        this.$message.error('通知发送失败：' + error.message)
      }
    },

    // 生成报告
    handleReport(row) {
      this.$router.push(`/pm/performance-interview/report/${row.interviewId}`)
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
    formatInterviewStatus,
    formatInterviewType,
    formatPriorityLevel,
    getStatusTagType,
    getPriorityTagType,

    getTypeTagType(type) {
      const tagMap = {
        'ANNUAL': 'danger',
        'QUARTERLY': 'primary',
        'MONTHLY': 'success',
        'PROJECT': 'warning',
        'SPECIAL': 'info'
      }
      return tagMap[type] || ''
    },

    formatDateTime(dateTime) {
      if (!dateTime) return '--'
      return this.$moment(dateTime).format('YYYY-MM-DD HH:mm')
    }
  }
}
</script>

<style scoped>
.performance-interview-list {
  padding: 20px;
}

.search-section,
.action-section,
.table-section {
  margin-bottom: 20px;
}

.action-buttons {
  display: flex;
  gap: 10px;
}

.pagination-section {
  margin-top: 20px;
  text-align: right;
}

.dialog-footer {
  text-align: right;
}
</style>
