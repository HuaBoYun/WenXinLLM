<template>
  <div class="performance-calibration-list">
    <!-- 搜索区域 -->
    <el-card class="search-card" shadow="never">
      <el-form :model="searchForm" :inline="true" size="small">
        <el-form-item label="校准编码">
          <el-input v-model="searchForm.calibrationCode" placeholder="请输入校准编码" clearable />
        </el-form-item>
        <el-form-item label="校准标题">
          <el-input v-model="searchForm.calibrationTitle" placeholder="请输入校准标题" clearable />
        </el-form-item>
        <el-form-item label="校准类型">
          <el-select v-model="searchForm.calibrationType" placeholder="请选择校准类型" clearable>
            <el-option label="年度校准" value="ANNUAL" />
            <el-option label="季度校准" value="QUARTERLY" />
            <el-option label="月度校准" value="MONTHLY" />
            <el-option label="项目校准" value="PROJECT" />
            <el-option label="专项校准" value="SPECIAL" />
          </el-select>
        </el-form-item>
        <el-form-item label="校准状态">
          <el-select v-model="searchForm.calibrationStatus" placeholder="请选择校准状态" clearable>
            <el-option label="计划中" value="PLANNED" />
            <el-option label="进行中" value="ONGOING" />
            <el-option label="已完成" value="COMPLETED" />
            <el-option label="已取消" value="CANCELLED" />
          </el-select>
        </el-form-item>
        <el-form-item label="校准年度">
          <el-date-picker
            v-model="searchForm.calibrationYear"
            type="year"
            placeholder="请选择校准年度"
            value-format="yyyy"
            clearable
          />
        </el-form-item>
        <el-form-item label="优先级">
          <el-select v-model="searchForm.priorityLevel" placeholder="请选择优先级" clearable>
            <el-option label="高" value="HIGH" />
            <el-option label="中" value="MEDIUM" />
            <el-option label="低" value="LOW" />
          </el-select>
        </el-form-item>
        <el-form-item label="时间范围">
          <el-date-picker
            v-model="searchForm.timeRange"
            type="datetimerange"
            range-separator="至"
            start-placeholder="开始时间"
            end-placeholder="结束时间"
            value-format="yyyy-MM-dd HH:mm:ss"
            clearable
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
        <el-button type="primary" icon="el-icon-plus" @click="handleCreate">新建校准</el-button>
        <el-button type="success" icon="el-icon-download" @click="handleExport">导出数据</el-button>
        <el-button type="warning" icon="el-icon-upload2" @click="handleImport">导入数据</el-button>
        <el-button 
          type="danger" 
          icon="el-icon-delete" 
          :disabled="selectedRows.length === 0"
          @click="handleBatchDelete"
        >
          批量删除
        </el-button>
        <el-button 
          type="info" 
          icon="el-icon-message" 
          :disabled="selectedRows.length === 0"
          @click="handleBatchNotification"
        >
          批量通知
        </el-button>
      </div>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="table-card" shadow="never">
      <el-table
        v-loading="loading"
        :data="tableData"
        stripe
        border
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column prop="calibrationCode" label="校准编码" width="150" />
        <el-table-column prop="calibrationTitle" label="校准标题" min-width="200" show-overflow-tooltip />
        <el-table-column prop="calibrationType" label="校准类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getTypeColor(scope.row.calibrationType)">
              {{ formatCalibrationType(scope.row.calibrationType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="calibrationStatus" label="校准状态" width="120">
          <template slot-scope="scope">
            <el-tag :type="getStatusColor(scope.row.calibrationStatus)">
              {{ formatCalibrationStatus(scope.row.calibrationStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="calibrationYear" label="校准年度" width="100" />
        <el-table-column prop="targetDeptName" label="目标部门" width="150" show-overflow-tooltip />
        <el-table-column prop="calibrationOwnerName" label="校准负责人" width="120" />
        <el-table-column prop="priorityLevel" label="优先级" width="100">
          <template slot-scope="scope">
            <el-tag :type="getPriorityColor(scope.row.priorityLevel)" size="mini">
              {{ formatPriorityLevel(scope.row.priorityLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="completionRate" label="完成率" width="100">
          <template slot-scope="scope">
            <el-progress 
              :percentage="scope.row.completionRate || 0" 
              :stroke-width="6"
              :show-text="false"
            />
            <span class="progress-text">{{ scope.row.completionRate || 0 }}%</span>
          </template>
        </el-table-column>
        <el-table-column prop="plannedStartTime" label="计划开始时间" width="160">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.plannedStartTime) }}
          </template>
        </el-table-column>
        <el-table-column prop="plannedEndTime" label="计划结束时间" width="160">
          <template slot-scope="scope">
            <span :class="{ 'overdue-text': isOverdue(scope.row) }">
              {{ formatDateTime(scope.row.plannedEndTime) }}
            </span>
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
                  :disabled="scope.row.calibrationStatus !== 'PLANNED'"
                >
                  开始校准
                </el-dropdown-item>
                <el-dropdown-item 
                  command="complete" 
                  :disabled="scope.row.calibrationStatus !== 'ONGOING'"
                >
                  完成校准
                </el-dropdown-item>
                <el-dropdown-item 
                  command="cancel" 
                  :disabled="scope.row.calibrationStatus === 'COMPLETED' || scope.row.calibrationStatus === 'CANCELLED'"
                >
                  取消校准
                </el-dropdown-item>
                <el-dropdown-item command="copy">复制校准</el-dropdown-item>
                <el-dropdown-item command="report">生成报告</el-dropdown-item>
                <el-dropdown-item command="notification">发送通知</el-dropdown-item>
                <el-dropdown-item 
                  command="delete" 
                  :disabled="scope.row.calibrationStatus !== 'PLANNED'"
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

    <!-- 创建/编辑对话框 -->
    <el-dialog
      :title="dialogTitle"
      :visible.sync="dialogVisible"
      width="800px"
      :close-on-click-modal="false"
    >
      <performance-calibration-form
        ref="calibrationForm"
        :form-data="currentRow"
        :is-edit="isEdit"
        @submit="handleFormSubmit"
        @cancel="handleFormCancel"
      />
    </el-dialog>

    <!-- 导入对话框 -->
    <el-dialog
      title="导入校准数据"
      :visible.sync="importDialogVisible"
      width="600px"
      :close-on-click-modal="false"
    >
      <el-upload
        ref="uploadRef"
        :action="uploadAction"
        :headers="uploadHeaders"
        :on-success="handleImportSuccess"
        :on-error="handleImportError"
        :before-upload="beforeImportUpload"
        :file-list="fileList"
        accept=".xlsx,.xls"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip" slot="tip">只能上传xlsx/xls文件，且不超过10MB</div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button @click="importDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleImportConfirm">确定导入</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  queryCalibrationPage,
  deleteCalibration,
  batchDeleteCalibrations,
  startCalibration,
  completeCalibration,
  cancelCalibration,
  copyCalibration,
  generateCalibrationReport,
  sendCalibrationNotification,
  batchSendNotifications,
  exportCalibrationData,
  importCalibrationData,
  formatCalibrationStatus,
  formatCalibrationType,
  formatPriorityLevel,
  getStatusColor,
  getPriorityColor,
  isOverdue
} from '@/api/managementAccountant/pm/performanceCalibration'
import PerformanceCalibrationForm from './components/PerformanceCalibrationForm'

export default {
  name: 'PerformanceCalibrationList',
  components: {
    PerformanceCalibrationForm
  },
  data() {
    return {
      loading: false,
      tableData: [],
      selectedRows: [],
      searchForm: {
        calibrationCode: '',
        calibrationTitle: '',
        calibrationType: '',
        calibrationStatus: '',
        calibrationYear: '',
        priorityLevel: '',
        timeRange: []
      },
      pagination: {
        current: 1,
        size: 10,
        total: 0
      },
      dialogVisible: false,
      dialogTitle: '',
      isEdit: false,
      currentRow: {},
      importDialogVisible: false,
      fileList: [],
      uploadAction: process.env.VUE_APP_BASE_API + '/accountant/pm/performance-calibration/import',
      uploadHeaders: {
        'Authorization': 'Bearer ' + this.$store.getters.token
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
        
        // 处理时间范围
        if (this.searchForm.timeRange && this.searchForm.timeRange.length === 2) {
          params.startTime = this.searchForm.timeRange[0]
          params.endTime = this.searchForm.timeRange[1]
        }
        
        const response = await queryCalibrationPage(params)
        if (response.success) {
          this.tableData = response.data.records
          this.pagination.total = response.data.total
        } else {
          this.$message.error(response.message || '查询失败')
        }
      } catch (error) {
        console.error('查询校准列表失败:', error)
        this.$message.error('查询失败')
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
        calibrationCode: '',
        calibrationTitle: '',
        calibrationType: '',
        calibrationStatus: '',
        calibrationYear: '',
        priorityLevel: '',
        timeRange: []
      }
      this.pagination.current = 1
      this.loadData()
    },

    // 分页大小改变
    handleSizeChange(size) {
      this.pagination.size = size
      this.pagination.current = 1
      this.loadData()
    },

    // 当前页改变
    handleCurrentChange(current) {
      this.pagination.current = current
      this.loadData()
    },

    // 选择改变
    handleSelectionChange(selection) {
      this.selectedRows = selection
    },

    // 新建
    handleCreate() {
      this.dialogTitle = '新建校准'
      this.isEdit = false
      this.currentRow = {}
      this.dialogVisible = true
    },

    // 查看
    handleView(row) {
      this.$router.push({
        name: 'PerformanceCalibrationDetail',
        params: { id: row.calibrationId }
      })
    },

    // 编辑
    handleEdit(row) {
      this.dialogTitle = '编辑校准'
      this.isEdit = true
      this.currentRow = { ...row }
      this.dialogVisible = true
    },

    // 下拉菜单命令
    async handleDropdownCommand(command, row) {
      switch (command) {
        case 'start':
          await this.handleStart(row)
          break
        case 'complete':
          await this.handleComplete(row)
          break
        case 'cancel':
          await this.handleCancel(row)
          break
        case 'copy':
          await this.handleCopy(row)
          break
        case 'report':
          await this.handleReport(row)
          break
        case 'notification':
          await this.handleNotification(row)
          break
        case 'delete':
          await this.handleDelete(row)
          break
      }
    },

    // 开始校准
    async handleStart(row) {
      try {
        const response = await startCalibration(row.calibrationId, {
          calibrationMethod: 'MEETING',
          calibrationLocation: '会议室'
        })
        if (response.success) {
          this.$message.success('校准开始成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '开始失败')
        }
      } catch (error) {
        console.error('开始校准失败:', error)
        this.$message.error('开始失败')
      }
    },

    // 完成校准
    async handleComplete(row) {
      this.$prompt('请输入校准总结', '完成校准', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputType: 'textarea',
        inputPlaceholder: '请输入校准总结'
      }).then(async ({ value }) => {
        try {
          const response = await completeCalibration(row.calibrationId, {
            calibrationSummary: value,
            effectivenessRating: 4,
            needFollowUp: false
          })
          if (response.success) {
            this.$message.success('校准完成成功')
            this.loadData()
          } else {
            this.$message.error(response.message || '完成失败')
          }
        } catch (error) {
          console.error('完成校准失败:', error)
          this.$message.error('完成失败')
        }
      })
    },

    // 取消校准
    async handleCancel(row) {
      this.$prompt('请输入取消原因', '取消校准', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputPlaceholder: '请输入取消原因'
      }).then(async ({ value }) => {
        try {
          const response = await cancelCalibration(row.calibrationId, {
            cancelReason: value
          })
          if (response.success) {
            this.$message.success('校准取消成功')
            this.loadData()
          } else {
            this.$message.error(response.message || '取消失败')
          }
        } catch (error) {
          console.error('取消校准失败:', error)
          this.$message.error('取消失败')
        }
      })
    },

    // 复制校准
    async handleCopy(row) {
      try {
        const response = await copyCalibration(row.calibrationId, {
          calibrationTitle: row.calibrationTitle + ' - 副本'
        })
        if (response.success) {
          this.$message.success('复制成功')
          this.loadData()
        } else {
          this.$message.error(response.message || '复制失败')
        }
      } catch (error) {
        console.error('复制校准失败:', error)
        this.$message.error('复制失败')
      }
    },

    // 生成报告
    async handleReport(row) {
      try {
        const response = await generateCalibrationReport(row.calibrationId, {})
        if (response.success) {
          this.$message.success('报告生成成功')
          // 这里可以添加下载报告的逻辑
        } else {
          this.$message.error(response.message || '生成报告失败')
        }
      } catch (error) {
        console.error('生成报告失败:', error)
        this.$message.error('生成报告失败')
      }
    },

    // 发送通知
    async handleNotification(row) {
      try {
        const response = await sendCalibrationNotification(row.calibrationId, {
          notificationType: 'EMAIL',
          notificationContent: '校准通知'
        })
        if (response.success) {
          this.$message.success('通知发送成功')
        } else {
          this.$message.error(response.message || '发送通知失败')
        }
      } catch (error) {
        console.error('发送通知失败:', error)
        this.$message.error('发送通知失败')
      }
    },

    // 删除
    async handleDelete(row) {
      this.$confirm('确定要删除这个校准吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const response = await deleteCalibration(row.calibrationId)
          if (response.success) {
            this.$message.success('删除成功')
            this.loadData()
          } else {
            this.$message.error(response.message || '删除失败')
          }
        } catch (error) {
          console.error('删除校准失败:', error)
          this.$message.error('删除失败')
        }
      })
    },

    // 批量删除
    async handleBatchDelete() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要删除的数据')
        return
      }

      this.$confirm(`确定要删除选中的 ${this.selectedRows.length} 个校准吗？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const ids = this.selectedRows.map(row => row.calibrationId)
          const response = await batchDeleteCalibrations(ids)
          if (response.success) {
            this.$message.success('批量删除成功')
            this.loadData()
          } else {
            this.$message.error(response.message || '批量删除失败')
          }
        } catch (error) {
          console.error('批量删除失败:', error)
          this.$message.error('批量删除失败')
        }
      })
    },

    // 批量通知
    async handleBatchNotification() {
      if (this.selectedRows.length === 0) {
        this.$message.warning('请选择要发送通知的数据')
        return
      }

      try {
        const ids = this.selectedRows.map(row => row.calibrationId)
        const response = await batchSendNotifications(ids, {
          notificationType: 'EMAIL',
          notificationContent: '批量校准通知'
        })
        if (response.success) {
          this.$message.success('批量通知发送成功')
        } else {
          this.$message.error(response.message || '批量通知发送失败')
        }
      } catch (error) {
        console.error('批量通知发送失败:', error)
        this.$message.error('批量通知发送失败')
      }
    },

    // 导出
    async handleExport() {
      try {
        const response = await exportCalibrationData(this.searchForm)
        if (response.success) {
          this.$message.success('导出成功')
          // 这里可以添加下载文件的逻辑
        } else {
          this.$message.error(response.message || '导出失败')
        }
      } catch (error) {
        console.error('导出失败:', error)
        this.$message.error('导出失败')
      }
    },

    // 导入
    handleImport() {
      this.importDialogVisible = true
      this.fileList = []
    },

    // 导入确认
    handleImportConfirm() {
      if (this.fileList.length === 0) {
        this.$message.warning('请选择要导入的文件')
        return
      }
      this.$refs.uploadRef.submit()
    },

    // 导入前验证
    beforeImportUpload(file) {
      const isExcel = file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' ||
                     file.type === 'application/vnd.ms-excel'
      const isLt10M = file.size / 1024 / 1024 < 10

      if (!isExcel) {
        this.$message.error('只能上传Excel文件!')
        return false
      }
      if (!isLt10M) {
        this.$message.error('文件大小不能超过10MB!')
        return false
      }
      return true
    },

    // 导入成功
    handleImportSuccess(response) {
      this.importDialogVisible = false
      if (response.success) {
        this.$message.success(`导入成功，成功${response.data.successCount}个，失败${response.data.failCount}个`)
        this.loadData()
      } else {
        this.$message.error(response.message || '导入失败')
      }
    },

    // 导入失败
    handleImportError(error) {
      console.error('导入失败:', error)
      this.$message.error('导入失败')
    },

    // 表单提交
    handleFormSubmit(formData) {
      this.dialogVisible = false
      this.loadData()
    },

    // 表单取消
    handleFormCancel() {
      this.dialogVisible = false
    },

    // 格式化日期时间
    formatDateTime(dateTime) {
      if (!dateTime) return '-'
      return this.$moment(dateTime).format('YYYY-MM-DD HH:mm')
    },

    // 格式化校准状态
    formatCalibrationStatus,
    
    // 格式化校准类型
    formatCalibrationType,
    
    // 格式化优先级
    formatPriorityLevel,
    
    // 获取状态颜色
    getStatusColor,
    
    // 获取优先级颜色
    getPriorityColor,

    // 获取类型颜色
    getTypeColor(type) {
      const colorMap = {
        'ANNUAL': 'primary',
        'QUARTERLY': 'success',
        'MONTHLY': 'info',
        'PROJECT': 'warning',
        'SPECIAL': 'danger'
      }
      return colorMap[type] || 'info'
    },
    
    // 判断是否逾期
    isOverdue
  }
}
</script>

<style lang="scss" scoped>
.performance-calibration-list {
  .search-card, .operation-card, .table-card {
    margin-bottom: 20px;
  }

  .operation-buttons {
    display: flex;
    gap: 10px;
  }

  .pagination-container {
    display: flex;
    justify-content: center;
    margin-top: 20px;
  }

  .progress-text {
    margin-left: 8px;
    font-size: 12px;
    color: #606266;
  }

  .overdue-text {
    color: #f56c6c;
    font-weight: bold;
  }

  .dialog-footer {
    text-align: right;
  }
}
</style>
