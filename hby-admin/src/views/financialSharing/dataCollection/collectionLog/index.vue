<template>
  <div class="app-container">
    <!-- 查询表单 -->
    <el-form :model="queryForm" :inline="true" label-width="100px" class="query-form">
      <el-form-item label="任务编码">
        <el-input v-model="queryForm.taskCode" placeholder="请输入任务编码" clearable style="width: 200px" />
      </el-form-item>
      <el-form-item label="任务名称">
        <el-input v-model="queryForm.taskName" placeholder="请输入任务名称" clearable style="width: 200px" />
      </el-form-item>
      <el-form-item label="执行类型">
        <el-select v-model="queryForm.executeType" placeholder="请选择执行类型" clearable style="width: 150px">
          <el-option label="手动执行" value="MANUAL" />
          <el-option label="定时执行" value="SCHEDULED" />
        </el-select>
      </el-form-item>
      <el-form-item label="执行状态">
        <el-select v-model="queryForm.executeStatus" placeholder="请选择执行状态" clearable style="width: 150px">
          <el-option label="成功" value="SUCCESS" />
          <el-option label="失败" value="FAILED" />
          <el-option label="已停止" value="STOPPED" />
        </el-select>
      </el-form-item>
      <el-form-item label="开始时间">
        <el-date-picker
          v-model="queryForm.startTimeRange"
          type="datetimerange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          value-format="yyyy-MM-dd HH:mm:ss"
          style="width: 380px"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 操作按钮 -->
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="danger"
          icon="el-icon-delete"
          :disabled="selectedIds.length === 0"
          @click="handleBatchDelete"
        >批量删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          icon="el-icon-delete"
          @click="handleCleanHistory"
        >清理历史</el-button>
      </el-col>
    </el-row>

    <!-- 数据表格 -->
    <el-table
      v-loading="loading"
      :data="logList"
      border
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="序号" type="index" width="60" align="center" />
      <el-table-column label="任务编码" prop="taskCode" width="150" show-overflow-tooltip />
      <el-table-column label="任务名称" prop="taskName" width="200" show-overflow-tooltip />
      <el-table-column label="执行类型" prop="executeType" width="100" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.executeType === 'MANUAL'" type="primary">手动执行</el-tag>
          <el-tag v-else-if="scope.row.executeType === 'SCHEDULED'" type="success">定时执行</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="执行状态" prop="executeStatus" width="100" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.executeStatus === 'SUCCESS'" type="success">成功</el-tag>
          <el-tag v-else-if="scope.row.executeStatus === 'FAILED'" type="danger">失败</el-tag>
          <el-tag v-else-if="scope.row.executeStatus === 'STOPPED'" type="warning">已停止</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="开始时间" prop="startTime" width="160" align="center" />
      <el-table-column label="结束时间" prop="endTime" width="160" align="center" />
      <el-table-column label="执行时长" prop="executeDuration" width="100" align="center">
        <template slot-scope="scope">
          {{ formatDuration(scope.row.executeDuration) }}
        </template>
      </el-table-column>
      <el-table-column label="总记录数" prop="totalCount" width="100" align="center" />
      <el-table-column label="成功数" prop="successCount" width="90" align="center">
        <template slot-scope="scope">
          <span style="color: #67C23A">{{ scope.row.successCount || 0 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="失败数" prop="failedCount" width="90" align="center">
        <template slot-scope="scope">
          <span style="color: #F56C6C">{{ scope.row.failedCount || 0 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="跳过数" prop="skipCount" width="90" align="center">
        <template slot-scope="scope">
          <span style="color: #E6A23C">{{ scope.row.skipCount || 0 }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" fixed="right" width="150" align="center">
        <template slot-scope="scope">
          <el-button type="text" icon="el-icon-view" @click="handleView(scope.row)">查看</el-button>
          <el-button type="text" icon="el-icon-delete" style="color: #F56C6C" @click="handleDelete(scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页组件 -->
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryForm.pageNumber"
      :limit.sync="queryForm.pageSize"
      @pagination="getList"
    />

    <!-- 日志详情对话框 -->
    <el-dialog title="日志详情" :visible.sync="detailDialogVisible" width="800px" append-to-body>
      <el-descriptions :column="2" border>
        <el-descriptions-item label="任务编码">{{ currentLog.taskCode }}</el-descriptions-item>
        <el-descriptions-item label="任务名称">{{ currentLog.taskName }}</el-descriptions-item>
        <el-descriptions-item label="执行类型">
          <el-tag v-if="currentLog.executeType === 'MANUAL'" type="primary">手动执行</el-tag>
          <el-tag v-else-if="currentLog.executeType === 'SCHEDULED'" type="success">定时执行</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="执行状态">
          <el-tag v-if="currentLog.executeStatus === 'SUCCESS'" type="success">成功</el-tag>
          <el-tag v-else-if="currentLog.executeStatus === 'FAILED'" type="danger">失败</el-tag>
          <el-tag v-else-if="currentLog.executeStatus === 'STOPPED'" type="warning">已停止</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="开始时间">{{ currentLog.startTime }}</el-descriptions-item>
        <el-descriptions-item label="结束时间">{{ currentLog.endTime }}</el-descriptions-item>
        <el-descriptions-item label="执行时长">{{ formatDuration(currentLog.executeDuration) }}</el-descriptions-item>
        <el-descriptions-item label="总记录数">{{ currentLog.totalCount || 0 }}</el-descriptions-item>
        <el-descriptions-item label="成功数">
          <span style="color: #67C23A">{{ currentLog.successCount || 0 }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="失败数">
          <span style="color: #F56C6C">{{ currentLog.failedCount || 0 }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="跳过数">
          <span style="color: #E6A23C">{{ currentLog.skipCount || 0 }}</span>
        </el-descriptions-item>
        <el-descriptions-item label="错误信息" :span="2" v-if="currentLog.errorMessage">
          <el-input type="textarea" :value="currentLog.errorMessage" :rows="4" readonly />
        </el-descriptions-item>
      </el-descriptions>
      <div slot="footer" class="dialog-footer">
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </div>
    </el-dialog>

    <!-- 清理历史对话框 -->
    <el-dialog title="清理历史日志" :visible.sync="cleanDialogVisible" width="400px" append-to-body>
      <el-form :model="cleanForm" label-width="120px">
        <el-form-item label="保留天数">
          <el-input-number v-model="cleanForm.days" :min="1" :max="365" placeholder="请输入保留天数" />
          <div style="color: #909399; font-size: 12px; margin-top: 5px">
            将删除 {{ cleanForm.days }} 天前的所有日志
          </div>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="cleanDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmCleanHistory">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  queryCollectionLogPage,
  queryCollectionLogById,
  deleteCollectionLog,
  batchDeleteCollectionLog,
  cleanHistoryLogs
} from '@/api/financialSharing/dataCollection'
import Pagination from '@/components/Pagination'

export default {
  name: 'CollectionLog',
  components: { Pagination },
  data() {
    return {
      // 查询参数
      queryForm: {
        taskCode: '',
        taskName: '',
        executeType: '',
        executeStatus: '',
        startTimeRange: [],
        pageNumber: 1,
        pageSize: 15
      },
      // 表格数据
      logList: [],
      total: 0,
      loading: false,
      // 选中的ID列表
      selectedIds: [],
      // 日志详情对话框
      detailDialogVisible: false,
      currentLog: {},
      // 清理历史对话框
      cleanDialogVisible: false,
      cleanForm: {
        days: 30
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    // 查询列表
    getList() {
      this.loading = true
      const params = {
        ...this.queryForm
      }
      // 处理时间范围
      if (params.startTimeRange && params.startTimeRange.length === 2) {
        params.startTimeBegin = params.startTimeRange[0]
        params.startTimeEnd = params.startTimeRange[1]
      }
      delete params.startTimeRange

      queryCollectionLogPage(params).then(response => {
        if (response.code === 1) {
          this.logList = response.data.records || []
          this.total = response.data.total || 0
        }
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    // 查询
    handleQuery() {
      this.queryForm.pageNumber = 1
      this.getList()
    },
    // 重置
    handleReset() {
      this.queryForm = {
        taskCode: '',
        taskName: '',
        executeType: '',
        executeStatus: '',
        startTimeRange: [],
        pageNumber: 1,
        pageSize: 15
      }
      this.getList()
    },
    // 选择变化
    handleSelectionChange(selection) {
      this.selectedIds = selection.map(item => item.logId)
    },
    // 查看详情
    handleView(row) {
      queryCollectionLogById({ logId: row.logId }).then(response => {
        if (response.code === 1) {
          this.currentLog = response.data
          this.detailDialogVisible = true
        }
      })
    },
    // 删除
    handleDelete(row) {
      this.$confirm('确认删除该日志吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteCollectionLog({ logId: row.logId }).then(response => {
          if (response.code === 1) {
            this.$message.success('删除成功')
            this.getList()
          }
        })
      })
    },
    // 批量删除
    handleBatchDelete() {
      this.$confirm('确认删除选中的日志吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        batchDeleteCollectionLog(this.selectedIds).then(response => {
          if (response.code === 1) {
            this.$message.success(response.msg || '删除成功')
            this.getList()
          }
        })
      })
    },
    // 清理历史
    handleCleanHistory() {
      this.cleanDialogVisible = true
    },
    // 确认清理历史
    confirmCleanHistory() {
      cleanHistoryLogs({ days: this.cleanForm.days }).then(response => {
        if (response.code === 1) {
          this.$message.success(response.msg || '清理成功')
          this.cleanDialogVisible = false
          this.getList()
        }
      })
    },
    // 格式化执行时长
    formatDuration(duration) {
      if (!duration) return '-'
      if (duration < 1000) {
        return duration + 'ms'
      } else if (duration < 60000) {
        return (duration / 1000).toFixed(2) + 's'
      } else {
        const minutes = Math.floor(duration / 60000)
        const seconds = ((duration % 60000) / 1000).toFixed(0)
        return minutes + 'm' + seconds + 's'
      }
    }
  }
}
</script>

<style scoped>
.query-form {
  background: #fff;
  padding: 20px;
  margin-bottom: 10px;
}
.mb8 {
  margin-bottom: 8px;
}
</style>

