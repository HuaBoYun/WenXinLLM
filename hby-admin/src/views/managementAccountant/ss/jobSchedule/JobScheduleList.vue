<template>
  <div class="job-schedule-list">
    <!-- 搜索区域 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" :inline="true" size="small">
        <el-form-item label="作业名称">
          <el-input
            v-model="searchForm.jobName"
            placeholder="请输入作业名称"
            clearable
            style="width: 200px"
          />
        </el-form-item>
        <el-form-item label="作业类型">
          <el-select v-model="searchForm.jobType" placeholder="请选择作业类型" clearable style="width: 150px">
            <el-option label="批处理" value="BATCH" />
            <el-option label="实时处理" value="REALTIME" />
            <el-option label="定时任务" value="SCHEDULED" />
            <el-option label="触发任务" value="TRIGGER" />
            <el-option label="工作流" value="WORKFLOW" />
            <el-option label="数据处理" value="ETL" />
            <el-option label="备份任务" value="BACKUP" />
            <el-option label="维护任务" value="MAINTENANCE" />
          </el-select>
        </el-form-item>
        <el-form-item label="作业状态">
          <el-select v-model="searchForm.jobStatus" placeholder="请选择作业状态" clearable style="width: 120px">
            <el-option label="草稿" value="DRAFT" />
            <el-option label="激活" value="ACTIVE" />
            <el-option label="未激活" value="INACTIVE" />
            <el-option label="暂停" value="SUSPENDED" />
            <el-option label="完成" value="COMPLETED" />
            <el-option label="取消" value="CANCELLED" />
          </el-select>
        </el-form-item>
        <el-form-item label="调度状态">
          <el-select v-model="searchForm.scheduleStatus" placeholder="请选择调度状态" clearable style="width: 120px">
            <el-option label="已停止" value="STOPPED" />
            <el-option label="运行中" value="RUNNING" />
            <el-option label="已暂停" value="PAUSED" />
            <el-option label="错误" value="ERROR" />
          </el-select>
        </el-form-item>
        <el-form-item label="执行状态">
          <el-select v-model="searchForm.executionStatus" placeholder="请选择执行状态" clearable style="width: 120px">
            <el-option label="等待中" value="PENDING" />
            <el-option label="执行中" value="RUNNING" />
            <el-option label="已完成" value="COMPLETED" />
            <el-option label="失败" value="FAILED" />
            <el-option label="超时" value="TIMEOUT" />
            <el-option label="已取消" value="CANCELLED" />
            <el-option label="已停止" value="STOPPED" />
          </el-select>
        </el-form-item>
        <el-form-item label="优先级">
          <el-select v-model="searchForm.priority" placeholder="请选择优先级" clearable style="width: 100px">
            <el-option label="高(1-2)" :value="1" />
            <el-option label="中(3-5)" :value="3" />
            <el-option label="低(6-8)" :value="6" />
            <el-option label="最低(9-10)" :value="9" />
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
        <el-button type="primary" icon="el-icon-plus" @click="handleCreate">新建作业</el-button>
        <el-button type="success" icon="el-icon-video-play" :disabled="!hasSelection" @click="handleBatchStart">批量启动</el-button>
        <el-button type="warning" icon="el-icon-video-pause" :disabled="!hasSelection" @click="handleBatchStop">批量停止</el-button>
        <el-button type="info" icon="el-icon-refresh" :disabled="!hasSelection" @click="handleBatchRestart">批量重启</el-button>
        <el-button type="danger" icon="el-icon-delete" :disabled="!hasSelection" @click="handleBatchDelete">批量删除</el-button>
        
        <el-dropdown @command="handleBatchCommand" style="margin-left: 10px">
          <el-button type="primary">
            批量操作<i class="el-icon-arrow-down el-icon--right"></i>
          </el-button>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="batchPause" :disabled="!hasSelection">批量暂停</el-dropdown-item>
            <el-dropdown-item command="batchResume" :disabled="!hasSelection">批量恢复</el-dropdown-item>
            <el-dropdown-item command="batchHighPriority" :disabled="!hasSelection">设置高优先级</el-dropdown-item>
            <el-dropdown-item command="batchLowPriority" :disabled="!hasSelection">设置低优先级</el-dropdown-item>
            <el-dropdown-item command="batchAssignScheduler" :disabled="!hasSelection">分配调度器</el-dropdown-item>
            <el-dropdown-item command="batchNotify" :disabled="!hasSelection">批量通知</el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>

        <div style="float: right">
          <el-button icon="el-icon-download" @click="handleExport">导出</el-button>
          <el-button icon="el-icon-upload2" @click="handleImport">导入</el-button>
          <el-button icon="el-icon-refresh" @click="handleRefresh">刷新</el-button>
        </div>
      </div>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="table-card" shadow="never">
      <el-table
        v-loading="loading"
        :data="tableData"
        @selection-change="handleSelectionChange"
        @sort-change="handleSortChange"
        stripe
        border
        style="width: 100%"
      >
        <el-table-column type="selection" width="55" />
        <el-table-column prop="jobCode" label="作业编码" width="150" sortable="custom" />
        <el-table-column prop="jobName" label="作业名称" width="200" show-overflow-tooltip />
        <el-table-column prop="jobType" label="作业类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getJobTypeTagType(scope.row.jobType)" size="small">
              {{ formatJobType(scope.row.jobType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="jobStatus" label="作业状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getJobStatusTagType(scope.row.jobStatus)" size="small">
              {{ formatJobStatus(scope.row.jobStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="scheduleStatus" label="调度状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getScheduleStatusTagType(scope.row.scheduleStatus)" size="small">
              {{ formatScheduleStatus(scope.row.scheduleStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="executionStatus" label="执行状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getExecutionStatusTagType(scope.row.executionStatus)" size="small">
              {{ formatExecutionStatus(scope.row.executionStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="priority" label="优先级" width="80" sortable="custom">
          <template slot-scope="scope">
            <el-tag :type="getPriorityTagType(scope.row.priority)" size="small">
              {{ formatPriority(scope.row.priority) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="scheduleExpression" label="调度表达式" width="150" show-overflow-tooltip />
        <el-table-column prop="executionCount" label="执行次数" width="100" sortable="custom" />
        <el-table-column prop="successRate" label="成功率" width="100" sortable="custom">
          <template slot-scope="scope">
            <span>{{ calculateSuccessRate(scope.row.successCount, scope.row.executionCount) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="avgDuration" label="平均耗时" width="100">
          <template slot-scope="scope">
            {{ formatDuration(scope.row.avgDuration) }}
          </template>
        </el-table-column>
        <el-table-column prop="lastExecutionTime" label="最后执行时间" width="160" sortable="custom">
          <template slot-scope="scope">
            {{ scope.row.lastExecutionTime | formatDateTime }}
          </template>
        </el-table-column>
        <el-table-column prop="nextExecutionTime" label="下次执行时间" width="160" sortable="custom">
          <template slot-scope="scope">
            {{ scope.row.nextExecutionTime | formatDateTime }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-dropdown @command="(command) => handleRowCommand(command, scope.row)" trigger="click">
              <el-button type="text" size="small">
                操作<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="view">查看详情</el-dropdown-item>
                <el-dropdown-item command="edit">编辑</el-dropdown-item>
                <el-dropdown-item command="start" :disabled="scope.row.scheduleStatus === 'RUNNING'">启动</el-dropdown-item>
                <el-dropdown-item command="stop" :disabled="scope.row.scheduleStatus === 'STOPPED'">停止</el-dropdown-item>
                <el-dropdown-item command="pause" :disabled="scope.row.scheduleStatus !== 'RUNNING'">暂停</el-dropdown-item>
                <el-dropdown-item command="resume" :disabled="scope.row.scheduleStatus !== 'PAUSED'">恢复</el-dropdown-item>
                <el-dropdown-item command="execute">立即执行</el-dropdown-item>
                <el-dropdown-item command="reschedule">重新调度</el-dropdown-item>
                <el-dropdown-item command="logs">查看日志</el-dropdown-item>
                <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
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

    <!-- 作业详情对话框 -->
    <JobScheduleDetail
      :visible.sync="detailVisible"
      :job-id="currentJobId"
      :readonly="detailReadonly"
      @refresh="handleRefresh"
    />

    <!-- 重新调度对话框 -->
    <el-dialog title="重新调度" :visible.sync="rescheduleVisible" width="500px">
      <el-form :model="rescheduleForm" label-width="120px">
        <el-form-item label="调度表达式" required>
          <el-input v-model="rescheduleForm.scheduleExpression" placeholder="请输入Cron表达式" />
          <div class="form-tip">例如: 0 0 12 * * ? (每天中午12点执行)</div>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="rescheduleVisible = false">取消</el-button>
        <el-button type="primary" @click="handleRescheduleConfirm">确定</el-button>
      </div>
    </el-dialog>

    <!-- 批量分配调度器对话框 -->
    <el-dialog title="批量分配调度器" :visible.sync="assignSchedulerVisible" width="500px">
      <el-form :model="assignSchedulerForm" label-width="120px">
        <el-form-item label="调度器" required>
          <el-select v-model="assignSchedulerForm.schedulerId" placeholder="请选择调度器" style="width: 100%">
            <el-option label="调度器1" :value="1" />
            <el-option label="调度器2" :value="2" />
            <el-option label="调度器3" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="调度器名称" required>
          <el-input v-model="assignSchedulerForm.schedulerName" placeholder="请输入调度器名称" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="assignSchedulerVisible = false">取消</el-button>
        <el-button type="primary" @click="handleAssignSchedulerConfirm">确定</el-button>
      </div>
    </el-dialog>

    <!-- 批量通知对话框 -->
    <el-dialog title="批量通知" :visible.sync="batchNotifyVisible" width="500px">
      <el-form :model="batchNotifyForm" label-width="120px">
        <el-form-item label="通知类型" required>
          <el-select v-model="batchNotifyForm.notificationType" placeholder="请选择通知类型" style="width: 100%">
            <el-option label="邮件通知" value="EMAIL" />
            <el-option label="短信通知" value="SMS" />
            <el-option label="系统通知" value="SYSTEM" />
            <el-option label="微信通知" value="WECHAT" />
          </el-select>
        </el-form-item>
        <el-form-item label="通知内容" required>
          <el-input
            v-model="batchNotifyForm.message"
            type="textarea"
            :rows="4"
            placeholder="请输入通知内容"
          />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="batchNotifyVisible = false">取消</el-button>
        <el-button type="primary" @click="handleBatchNotifyConfirm">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import jobScheduleApi from '@/api/managementAccountant/ss/jobSchedule'
import JobScheduleDetail from './JobScheduleDetail'

export default {
  name: 'JobScheduleList',
  components: {
    JobScheduleDetail
  },
  data() {
    return {
      loading: false,
      tableData: [],
      selectedRows: [],
      searchForm: {
        jobName: '',
        jobType: '',
        jobStatus: '',
        scheduleStatus: '',
        executionStatus: '',
        priority: null,
        timeRange: []
      },
      pagination: {
        current: 1,
        size: 20,
        total: 0
      },
      sortField: '',
      sortOrder: '',
      
      // 对话框控制
      detailVisible: false,
      detailReadonly: true,
      currentJobId: null,
      
      rescheduleVisible: false,
      rescheduleForm: {
        jobId: null,
        scheduleExpression: ''
      },
      
      assignSchedulerVisible: false,
      assignSchedulerForm: {
        schedulerId: null,
        schedulerName: ''
      },
      
      batchNotifyVisible: false,
      batchNotifyForm: {
        notificationType: '',
        message: ''
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
    /**
     * 加载数据
     */
    async loadData() {
      this.loading = true
      try {
        const params = {
          current: this.pagination.current,
          size: this.pagination.size,
          ...this.searchForm
        }
        
        // 处理时间范围
        if (this.searchForm.timeRange && this.searchForm.timeRange.length === 2) {
          params.startTime = this.searchForm.timeRange[0]
          params.endTime = this.searchForm.timeRange[1]
        }
        delete params.timeRange
        
        // 处理排序
        if (this.sortField) {
          params.sortField = this.sortField
          params.sortOrder = this.sortOrder
        }
        
        const response = await jobScheduleApi.getJobSchedulePage(params)
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

    /**
     * 搜索
     */
    handleSearch() {
      this.pagination.current = 1
      this.loadData()
    },

    /**
     * 重置搜索
     */
    handleReset() {
      this.searchForm = {
        jobName: '',
        jobType: '',
        jobStatus: '',
        scheduleStatus: '',
        executionStatus: '',
        priority: null,
        timeRange: []
      }
      this.pagination.current = 1
      this.loadData()
    },

    /**
     * 刷新
     */
    handleRefresh() {
      this.loadData()
    },

    /**
     * 新建作业
     */
    handleCreate() {
      this.currentJobId = null
      this.detailReadonly = false
      this.detailVisible = true
    },

    /**
     * 选择变化
     */
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },

    /**
     * 排序变化
     */
    handleSortChange({ column, prop, order }) {
      this.sortField = prop
      this.sortOrder = order === 'ascending' ? 'asc' : 'desc'
      this.loadData()
    },

    /**
     * 页大小变化
     */
    handleSizeChange(size) {
      this.pagination.size = size
      this.pagination.current = 1
      this.loadData()
    },

    /**
     * 当前页变化
     */
    handleCurrentChange(current) {
      this.pagination.current = current
      this.loadData()
    },

    /**
     * 批量启动
     */
    async handleBatchStart() {
      try {
        const jobIds = this.selectedRows.map(row => row.jobId)
        const response = await jobScheduleApi.batchStartJobSchedule(jobIds)
        if (response.success) {
          this.$message.success('批量启动成功')
          this.loadData()
        }
      } catch (error) {
        this.$message.error('批量启动失败: ' + error.message)
      }
    },

    /**
     * 批量停止
     */
    async handleBatchStop() {
      try {
        const jobIds = this.selectedRows.map(row => row.jobId)
        const response = await jobScheduleApi.batchStopJobSchedule(jobIds)
        if (response.success) {
          this.$message.success('批量停止成功')
          this.loadData()
        }
      } catch (error) {
        this.$message.error('批量停止失败: ' + error.message)
      }
    },

    /**
     * 批量重启
     */
    async handleBatchRestart() {
      try {
        const jobIds = this.selectedRows.map(row => row.jobId)
        const result = await jobScheduleApi.quickActions.quickRestart(jobIds)
        if (result.success) {
          this.$message.success('批量重启操作已执行')
          this.loadData()
        } else {
          this.$message.error(result.message)
        }
      } catch (error) {
        this.$message.error('批量重启失败: ' + error.message)
      }
    },

    /**
     * 批量删除
     */
    handleBatchDelete() {
      this.$confirm('确定要删除选中的作业吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const jobIds = this.selectedRows.map(row => row.jobId)
          const response = await jobScheduleApi.batchDeleteJobSchedule(jobIds)
          if (response.success) {
            this.$message.success('批量删除成功')
            this.loadData()
          }
        } catch (error) {
          this.$message.error('批量删除失败: ' + error.message)
        }
      })
    },

    /**
     * 批量操作命令处理
     */
    async handleBatchCommand(command) {
      const jobIds = this.selectedRows.map(row => row.jobId)

      try {
        switch (command) {
          case 'batchPause':
            await jobScheduleApi.batchPauseJobSchedule(jobIds)
            this.$message.success('批量暂停成功')
            break
          case 'batchResume':
            await jobScheduleApi.batchResumeJobSchedule(jobIds)
            this.$message.success('批量恢复成功')
            break
          case 'batchHighPriority':
            await jobScheduleApi.quickActions.setHighPriority(jobIds)
            this.$message.success('设置高优先级成功')
            break
          case 'batchLowPriority':
            await jobScheduleApi.quickActions.setLowPriority(jobIds)
            this.$message.success('设置低优先级成功')
            break
          case 'batchAssignScheduler':
            this.assignSchedulerVisible = true
            return
          case 'batchNotify':
            this.batchNotifyVisible = true
            return
        }
        this.loadData()
      } catch (error) {
        this.$message.error('操作失败: ' + error.message)
      }
    },

    /**
     * 行操作命令处理
     */
    async handleRowCommand(command, row) {
      try {
        switch (command) {
          case 'view':
            this.currentJobId = row.jobId
            this.detailReadonly = true
            this.detailVisible = true
            break
          case 'edit':
            this.currentJobId = row.jobId
            this.detailReadonly = false
            this.detailVisible = true
            break
          case 'start':
            await jobScheduleApi.startJobSchedule(row.jobId)
            this.$message.success('启动成功')
            this.loadData()
            break
          case 'stop':
            await jobScheduleApi.stopJobSchedule(row.jobId)
            this.$message.success('停止成功')
            this.loadData()
            break
          case 'pause':
            await jobScheduleApi.pauseJobSchedule(row.jobId)
            this.$message.success('暂停成功')
            this.loadData()
            break
          case 'resume':
            await jobScheduleApi.resumeJobSchedule(row.jobId)
            this.$message.success('恢复成功')
            this.loadData()
            break
          case 'execute':
            await jobScheduleApi.executeJobImmediately(row.jobId)
            this.$message.success('立即执行成功')
            this.loadData()
            break
          case 'reschedule':
            this.rescheduleForm.jobId = row.jobId
            this.rescheduleForm.scheduleExpression = row.scheduleExpression
            this.rescheduleVisible = true
            break
          case 'logs':
            // 跳转到日志页面
            this.$router.push(`/ss/job-schedule/logs/${row.jobId}`)
            break
          case 'delete':
            this.handleDelete(row)
            break
        }
      } catch (error) {
        this.$message.error('操作失败: ' + error.message)
      }
    },

    /**
     * 删除单个作业
     */
    handleDelete(row) {
      this.$confirm(`确定要删除作业"${row.jobName}"吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await jobScheduleApi.deleteJobSchedule(row.jobId)
          if (response.success) {
            this.$message.success('删除成功')
            this.loadData()
          }
        } catch (error) {
          this.$message.error('删除失败: ' + error.message)
        }
      })
    },

    /**
     * 重新调度确认
     */
    async handleRescheduleConfirm() {
      if (!this.rescheduleForm.scheduleExpression) {
        this.$message.warning('请输入调度表达式')
        return
      }

      if (!jobScheduleApi.utils.validateCronExpression(this.rescheduleForm.scheduleExpression)) {
        this.$message.warning('调度表达式格式不正确')
        return
      }

      try {
        const response = await jobScheduleApi.rescheduleJob(
          this.rescheduleForm.jobId,
          this.rescheduleForm.scheduleExpression
        )
        if (response.success) {
          this.$message.success('重新调度成功')
          this.rescheduleVisible = false
          this.loadData()
        }
      } catch (error) {
        this.$message.error('重新调度失败: ' + error.message)
      }
    },

    /**
     * 分配调度器确认
     */
    async handleAssignSchedulerConfirm() {
      if (!this.assignSchedulerForm.schedulerId || !this.assignSchedulerForm.schedulerName) {
        this.$message.warning('请完整填写调度器信息')
        return
      }

      try {
        const jobIds = this.selectedRows.map(row => row.jobId)
        const response = await jobScheduleApi.batchAssignScheduler(
          jobIds,
          this.assignSchedulerForm.schedulerId,
          this.assignSchedulerForm.schedulerName
        )
        if (response.success) {
          this.$message.success('批量分配调度器成功')
          this.assignSchedulerVisible = false
          this.loadData()
        }
      } catch (error) {
        this.$message.error('批量分配调度器失败: ' + error.message)
      }
    },

    /**
     * 批量通知确认
     */
    async handleBatchNotifyConfirm() {
      if (!this.batchNotifyForm.notificationType || !this.batchNotifyForm.message) {
        this.$message.warning('请完整填写通知信息')
        return
      }

      try {
        const jobIds = this.selectedRows.map(row => row.jobId)
        const response = await jobScheduleApi.batchSendNotification(
          jobIds,
          this.batchNotifyForm.notificationType,
          this.batchNotifyForm.message
        )
        if (response.success) {
          this.$message.success('批量通知发送成功')
          this.batchNotifyVisible = false
        }
      } catch (error) {
        this.$message.error('批量通知发送失败: ' + error.message)
      }
    },

    /**
     * 导出数据
     */
    async handleExport() {
      try {
        const queryParams = { ...this.searchForm }
        const response = await jobScheduleApi.exportJobScheduleData(queryParams)
        if (response.success) {
          // 这里应该处理文件下载
          this.$message.success('导出成功')
        }
      } catch (error) {
        this.$message.error('导出失败: ' + error.message)
      }
    },

    /**
     * 导入数据
     */
    handleImport() {
      // 这里应该打开文件选择对话框
      this.$message.info('导入功能开发中')
    },

    // 格式化方法
    formatJobType(type) {
      return jobScheduleApi.utils.formatJobType(type)
    },

    formatJobStatus(status) {
      return jobScheduleApi.utils.formatJobStatus(status)
    },

    formatScheduleStatus(status) {
      return jobScheduleApi.utils.formatScheduleStatus(status)
    },

    formatExecutionStatus(status) {
      return jobScheduleApi.utils.formatExecutionStatus(status)
    },

    formatPriority(priority) {
      return jobScheduleApi.utils.formatPriority(priority)
    },

    formatDuration(seconds) {
      return jobScheduleApi.utils.formatDuration(seconds)
    },

    calculateSuccessRate(successCount, totalCount) {
      return jobScheduleApi.utils.calculateSuccessRate(successCount, totalCount)
    },

    // 标签类型方法
    getJobTypeTagType(type) {
      const typeMap = {
        'BATCH': 'primary',
        'REALTIME': 'success',
        'SCHEDULED': 'info',
        'TRIGGER': 'warning',
        'WORKFLOW': 'danger',
        'ETL': 'primary',
        'BACKUP': 'info',
        'MAINTENANCE': 'warning'
      }
      return typeMap[type] || 'info'
    },

    getJobStatusTagType(status) {
      const statusMap = {
        'DRAFT': 'info',
        'ACTIVE': 'success',
        'INACTIVE': 'warning',
        'SUSPENDED': 'warning',
        'COMPLETED': 'success',
        'CANCELLED': 'danger'
      }
      return statusMap[status] || 'info'
    },

    getScheduleStatusTagType(status) {
      const statusMap = {
        'STOPPED': 'info',
        'RUNNING': 'success',
        'PAUSED': 'warning',
        'ERROR': 'danger'
      }
      return statusMap[status] || 'info'
    },

    getExecutionStatusTagType(status) {
      const statusMap = {
        'PENDING': 'info',
        'RUNNING': 'primary',
        'COMPLETED': 'success',
        'FAILED': 'danger',
        'TIMEOUT': 'warning',
        'CANCELLED': 'info',
        'STOPPED': 'info'
      }
      return statusMap[status] || 'info'
    },

    getPriorityTagType(priority) {
      if (priority <= 2) return 'danger'
      if (priority <= 5) return 'warning'
      if (priority <= 8) return 'info'
      return 'success'
    }
  }
}
</script>

<style scoped>
.job-schedule-list {
  padding: 20px;
}

.search-card,
.operation-card,
.table-card {
  margin-bottom: 20px;
}

.operation-buttons {
  display: flex;
  align-items: center;
}

.pagination-container {
  margin-top: 20px;
  text-align: right;
}

.form-tip {
  font-size: 12px;
  color: #999;
  margin-top: 5px;
}
</style>
</script>
