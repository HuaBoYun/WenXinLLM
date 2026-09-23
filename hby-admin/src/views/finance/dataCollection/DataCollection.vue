<template>
  <div class="data-collection-container">
    <!-- 页面标题 -->
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span class="title-text">数据采集</span>
      </div>

      <!-- 查询表单 -->
      <el-form :model="queryForm" label-width="100px" class="query-form">
        <el-row :gutter="20">
          <el-col :lg="6" :md="12" :sm="24">
            <el-form-item label="任务名称">
              <el-input v-model="queryForm.taskName" placeholder="请输入任务名称" />
            </el-form-item>
          </el-col>
          <el-col :lg="6" :md="12" :sm="24">
            <el-form-item label="任务分类">
              <el-select v-model="queryForm.taskCategory" placeholder="请选择分类" clearable>
                <el-option label="全部" value="" />
                <el-option label="财务数据" value="FINANCE" />
                <el-option label="业务数据" value="BUSINESS" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :lg="6" :md="12" :sm="24">
            <el-form-item label="任务状态">
              <el-select v-model="queryForm.taskStatus" placeholder="请选择状态" clearable>
                <el-option label="全部" value="" />
                <el-option label="待执行" value="PENDING" />
                <el-option label="执行中" value="RUNNING" />
                <el-option label="已暂停" value="PAUSED" />
                <el-option label="已完成" value="SUCCESS" />
                <el-option label="失败" value="FAILED" />
                <el-option label="已取消" value="CANCELLED" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :lg="6" :md="24" :sm="24">
            <el-form-item>
              <el-button type="primary" @click="handleQuery">查询</el-button>
              <el-button @click="handleReset">重置</el-button>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="24">
            <el-form-item>
              <el-button type="success" @click="handleCreate">新建采集任务</el-button>
              <el-button type="primary" @click="handleDataSourceManage">数据源管理</el-button>
              <el-button type="warning" icon="el-icon-download" @click="handleDownloadOfflineTool">下载离线采集工具</el-button>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>

      <!-- 任务列表 -->
      <el-table
        :data="tableData"
        style="width: 100%"
        :loading="loading"
        stripe
        border
        :default-sort="{ prop: 'startTime', order: 'descending' }"
      >
        <el-table-column prop="taskId" label="任务ID" width="120" align="center" />

        <el-table-column prop="taskName" label="任务名称" min-width="150" show-overflow-tooltip />

        <el-table-column prop="taskCategory" label="任务分类" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="scope.row.taskCategory === 'FINANCE' ? 'success' : 'primary'" size="small">
              {{ scope.row.taskCategory === 'FINANCE' ? '财务数据' : '业务数据' }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="dataSourceName" label="数据源" width="130" align="center" show-overflow-tooltip />

        <el-table-column prop="status" label="状态" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)" effect="dark">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column prop="progress" label="进度" width="140" align="center">
          <template slot-scope="scope">
            <div style="display: flex; align-items: center; gap: 8px;">
              <el-progress
                :percentage="scope.row.progress"
                :color="getProgressColor(scope.row.progress)"
                style="flex: 1;"
              />
              <span style="min-width: 35px; font-weight: 500;">{{ scope.row.progress }}%</span>
            </div>
          </template>
        </el-table-column>

        <el-table-column prop="recordCount" label="采集记录数" width="120" align="right">
          <template slot-scope="scope">
            <span style="font-weight: 500; color: #409EFF;">{{ scope.row.recordCount | formatNumber }}</span>
          </template>
        </el-table-column>

        <el-table-column prop="startTime" label="开始时间" width="160" align="center" sortable />

        <el-table-column label="操作" width="380" fixed="right" align="center">
          <template slot-scope="scope">
            <div style="display: flex; gap: 4px; justify-content: center; flex-wrap: wrap;">
              <el-button
                size="mini"
                type="info"
                @click="handleDetail(scope.row)"
              >
                详情
              </el-button>
              <el-button
                size="mini"
                type="primary"
                @click="handleViewProgress(scope.row)"
              >
                进度
              </el-button>
              <el-button
                v-if="scope.row.status === 'RUNNING'"
                size="mini"
                type="warning"
                @click="handlePause(scope.row)"
              >
                暂停
              </el-button>
              <el-button
                v-if="scope.row.status === 'PAUSED'"
                size="mini"
                type="success"
                @click="handleResume(scope.row)"
              >
                恢复
              </el-button>
              <el-button
                v-if="scope.row.status === 'RUNNING' || scope.row.status === 'PAUSED'"
                size="mini"
                type="danger"
                plain
                @click="handleCancel(scope.row)"
              >
                取消
              </el-button>
              <el-button
                size="mini"
                type="success"
                @click="handleSchedule(scope.row)"
              >
                定时
              </el-button>
              <el-button
                v-if="scope.row.status === 'SUCCESS'"
                size="mini"
                type="warning"
                @click="handleValidate(scope.row)"
              >
                验证
              </el-button>
              <el-button
                v-if="scope.row.status !== 'RUNNING' && scope.row.status !== 'PAUSED'"
                size="mini"
                type="danger"
                plain
                @click="handleDelete(scope.row)"
              >
                删除
              </el-button>
            </div>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <el-pagination
        :current-page="pageNumber"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="pageSize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
        @size-change="handlePageSizeChange"
        @current-change="handlePageChange"
        style="margin-top: 20px; text-align: right"
      />
    </el-card>

    <!-- 新建采集任务对话框 - 使用分步骤组件 -->
    <CreateTaskSteps ref="createTaskSteps" @refresh="loadData" />

    <!-- 数据源管理对话框 -->
    <DataSourceManage ref="dataSourceManage" />

    <!-- 定时任务配置对话框 -->
    <ScheduleConfig ref="scheduleConfig" @refresh="loadData" />

    <!-- 采集进度详情对话框 -->
    <ProgressDetail ref="progressDetail" />

    <!-- 任务详情对话框 -->
    <el-dialog
      title="采集任务详情"
      :visible.sync="detailDialogVisible"
      width="700px"
    >
      <el-descriptions :column="2" border v-if="detailData">
        <el-descriptions-item label="任务ID">{{ detailData.taskId }}</el-descriptions-item>
        <el-descriptions-item label="任务名称">{{ detailData.taskName }}</el-descriptions-item>
        <el-descriptions-item label="采集方案">{{ detailData.planId }}</el-descriptions-item>
        <el-descriptions-item label="数据源">{{ detailData.dataSourceId }}</el-descriptions-item>
        <el-descriptions-item label="任务状态">
          <el-tag :type="getStatusType(detailData.status)">
            {{ getStatusText(detailData.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="进度">
          <el-progress :percentage="detailData.progress" />
        </el-descriptions-item>
        <el-descriptions-item label="采集记录数">{{ detailData.recordCount }}</el-descriptions-item>
        <el-descriptions-item label="开始时间">{{ detailData.startTime }}</el-descriptions-item>
        <el-descriptions-item label="结束时间">{{ detailData.endTime || '进行中' }}</el-descriptions-item>
        <el-descriptions-item label="错误信息" v-if="detailData.errorMessage">
          {{ detailData.errorMessage }}
        </el-descriptions-item>
      </el-descriptions>
      <span slot="footer" class="dialog-footer">
        <el-button @click="detailDialogVisible = false">关闭</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import { getCollectionTaskList, deleteCollectionTask, getCollectionTaskDetail, validateCollectionData, pauseCollection, resumeCollection, cancelCollection, downloadOfflineTool } from '@/api/finance/dataCollection'
import CreateTaskSteps from './components/CreateTaskSteps.vue'
import DataSourceManage from './components/DataSourceManage.vue'
import ScheduleConfig from './components/ScheduleConfig.vue'
import ProgressDetail from './components/ProgressDetail.vue'

export default {
  name: 'DataCollection',
  components: {
    CreateTaskSteps,
    DataSourceManage,
    ScheduleConfig,
    ProgressDetail
  },
  filters: {
    formatNumber(value) {
      if (!value) return '0'
      return value.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',')
    }
  },
  data() {
    return {
      queryForm: {
        taskName: '',
        taskCategory: '',
        taskStatus: ''
      },
      tableData: [],
      loading: false,
      pageNumber: 1,
      pageSize: 20,
      total: 0,
      detailDialogVisible: false,
      detailData: null
    }
  },
  created() {
    this.handleQuery()
  },
  methods: {
    handleQuery() {
      this.pageNumber = 1
      this.loadData()
    },
    handleReset() {
      this.queryForm = {
        taskName: '',
        taskCategory: '',
        taskStatus: ''
      }
      this.pageNumber = 1
      this.loadData()
    },
    loadData() {
      this.loading = true
      // 传递查询条件参数,包括新增的任务分类
      const params = {
        pageNumber: this.pageNumber,
        pageSize: this.pageSize,
        taskName: this.queryForm.taskName,
        taskCategory: this.queryForm.taskCategory,
        taskStatus: this.queryForm.taskStatus
      }
      getCollectionTaskList(params.pageNumber, params.pageSize, params.taskName, params.taskStatus, params.taskCategory).then(res => {
        if (res.code === 1) {
          this.tableData = res.data.list || []
          this.total = res.data.total || 0
        } else {
          this.$message.error(res.msg || '加载数据失败')
          // 接口失败时使用 mock 数据
          this.loadMockData()
        }
      }).catch(err => {
        this.$message.warning('接口暂不可用，已加载示例数据')
        console.error(err)
        // 接口异常时使用 mock 数据
        this.loadMockData()
      }).finally(() => {
        this.loading = false
      })
    },
    loadMockData() {
      // Mock 数据 - 当后端接口报404或异常时使用
      const mockDataList = [
        {
          taskId: 'TASK001',
          taskName: '财务数据采集任务',
          taskCategory: 'FINANCE',
          dataSourceId: 'source001',
          dataSourceName: 'EAS K3',
          status: 'RUNNING',
          progress: 65,
          recordCount: 15000,
          startTime: '2025-01-21 10:30:00',
          endTime: null,
          planId: 'plan001',
          collectionType: 'FULL',
          remark: '示例数据 - 财务共享数据采集'
        },
        {
          taskId: 'TASK002',
          taskName: '预算数据采集',
          taskCategory: 'FINANCE',
          dataSourceId: 'source002',
          dataSourceName: 'Oracle数据源',
          status: 'SUCCESS',
          progress: 100,
          recordCount: 8500,
          startTime: '2025-01-20 14:00:00',
          endTime: '2025-01-20 16:45:00',
          planId: 'plan002',
          collectionType: 'FULL',
          remark: '示例数据 - 预算管理数据采集'
        },
        {
          taskId: 'TASK003',
          taskName: '成本数据增量采集',
          taskCategory: 'FINANCE',
          dataSourceId: 'source001',
          dataSourceName: 'EAS K3',
          status: 'PENDING',
          progress: 0,
          recordCount: 0,
          startTime: '2025-01-21 09:15:00',
          endTime: null,
          planId: 'plan001',
          collectionType: 'INCREMENT',
          remark: '示例数据 - 成本管理增量采集'
        },
        {
          taskId: 'TASK004',
          taskName: '固定资产数据采集',
          taskCategory: 'FINANCE',
          dataSourceId: 'source003',
          dataSourceName: 'MySQL数据源',
          status: 'FAILED',
          progress: 30,
          recordCount: 3200,
          startTime: '2025-01-19 11:00:00',
          endTime: '2025-01-19 12:30:00',
          planId: 'plan003',
          collectionType: 'FULL',
          remark: '示例数据 - 固定资产采集失败',
          errorMessage: '数据源连接超时'
        },
        {
          taskId: 'TASK005',
          taskName: '人力资源数据采集',
          taskCategory: 'BUSINESS',
          dataSourceId: 'source002',
          dataSourceName: 'Oracle数据源',
          status: 'SUCCESS',
          progress: 100,
          recordCount: 12000,
          startTime: '2025-01-18 08:00:00',
          endTime: '2025-01-18 10:20:00',
          planId: 'plan002',
          collectionType: 'FULL',
          remark: '示例数据 - 人力资源数据采集'
        },
        {
          taskId: 'TASK006',
          taskName: '供应链采购数据采集',
          taskCategory: 'BUSINESS',
          dataSourceId: 'source004',
          dataSourceName: '达梦数据源',
          status: 'RUNNING',
          progress: 78,
          recordCount: 22000,
          startTime: '2025-01-21 08:30:00',
          endTime: null,
          planId: 'plan004',
          collectionType: 'INCREMENT',
          remark: '示例数据 - 供应链采购数据采集'
        },
        {
          taskId: 'TASK007',
          taskName: '库存数据采集',
          taskCategory: 'BUSINESS',
          dataSourceId: 'source003',
          dataSourceName: 'MySQL数据源',
          status: 'CANCELLED',
          progress: 25,
          recordCount: 4500,
          startTime: '2025-01-17 15:00:00',
          endTime: '2025-01-17 15:45:00',
          planId: 'plan003',
          collectionType: 'FULL',
          remark: '示例数据 - 库存数据采集已取消'
        },
        {
          taskId: 'TASK008',
          taskName: '应收应付数据采集',
          taskCategory: 'FINANCE',
          dataSourceId: 'source001',
          dataSourceName: 'EAS K3',
          status: 'SUCCESS',
          progress: 100,
          recordCount: 18500,
          startTime: '2025-01-16 10:00:00',
          endTime: '2025-01-16 13:15:00',
          planId: 'plan001',
          collectionType: 'FULL',
          remark: '示例数据 - 应收应付数据采集'
        }
      ]

      // 根据分页参数返回对应的数据
      const startIndex = (this.pageNumber - 1) * this.pageSize
      const endIndex = startIndex + this.pageSize
      this.tableData = mockDataList.slice(startIndex, endIndex)
      this.total = mockDataList.length
    },
    handlePageChange(pageNumber) {
      this.pageNumber = pageNumber
      this.loadData()
    },
    handlePageSizeChange(pageSize) {
      this.pageSize = pageSize
      this.pageNumber = 1
      this.loadData()
    },
    handleCreate() {
      this.$refs.createTaskSteps.show()
    },
    handleDataSourceManage() {
      this.$refs.dataSourceManage.show()
    },
    handleSchedule(row) {
      this.$refs.scheduleConfig.show(row)
    },
    handleViewProgress(row) {
      this.$refs.progressDetail.show(row)
    },
    handlePause(row) {
      this.$confirm('确定要暂停该采集任务吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        pauseCollection(row.taskId).then(res => {
          if (res.code === 1) {
            this.$message.success('暂停成功')
            this.loadData()
          } else {
            this.$message.error(res.msg || '暂停失败')
          }
        }).catch(err => {
          this.$message.error('暂停失败')
          console.error(err)
        })
      }).catch(() => {})
    },
    handleResume(row) {
      this.$confirm('确定要恢复该采集任务吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        resumeCollection(row.taskId).then(res => {
          if (res.code === 1) {
            this.$message.success('恢复成功')
            this.loadData()
          } else {
            this.$message.error(res.msg || '恢复失败')
          }
        }).catch(err => {
          this.$message.error('恢复失败')
          console.error(err)
        })
      }).catch(() => {})
    },
    handleCancel(row) {
      this.$confirm('确定要取消该采集任务吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        cancelCollection(row.taskId).then(res => {
          if (res.code === 1) {
            this.$message.success('取消成功')
            this.loadData()
          } else {
            this.$message.error(res.msg || '取消失败')
          }
        }).catch(err => {
          this.$message.error('取消失败')
          console.error(err)
        })
      }).catch(() => {})
    },
    handleValidate(row) {
      this.$confirm('确定要验证该采集任务的数据吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        validateCollectionData(row.taskId).then(res => {
          if (res.code === 1) {
            this.$message.success('数据验证成功')
            this.loadData()
          } else {
            this.$message.error(res.msg || '验证失败')
          }
        }).catch(err => {
          this.$message.error('验证失败')
          console.error(err)
        })
      }).catch(() => {})
    },

    handleDetail(row) {
      getCollectionTaskDetail(row.taskId).then(res => {
        if (res.code === 1 && res.data) {
          // 后端返回的是 { task: {...}, details: [...] }
          // 需要提取 task 对象,并添加 status 字段映射
          const taskData = res.data.task || res.data
          // 映射字段名: taskStatus -> status
          this.detailData = {
            ...taskData,
            status: taskData.taskStatus || taskData.status
          }
          this.detailDialogVisible = true
        } else {
          this.$message.warning('详情接口暂不可用，已加载示例数据')
          // 接口失败时使用当前行数据作为详情
          this.detailData = row
          this.detailDialogVisible = true
        }
      }).catch(err => {
        this.$message.warning('详情接口暂不可用，已加载示例数据')
        console.error(err)
        // 接口异常时使用当前行数据作为详情
        this.detailData = row
        this.detailDialogVisible = true
      })
    },
    handleDelete(row) {
      this.$confirm('确定要删除该采集任务吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteCollectionTask(row.taskId).then(res => {
          if (res.code === 1) {
            this.$message.success('删除成功')
            this.loadData()
          } else {
            this.$message.error(res.msg || '删除失败')
          }
        }).catch(err => {
          this.$message.error('删除失败')
          console.error(err)
        })
      }).catch(() => {})
    },
    getStatusType(status) {
      const statusMap = {
        'PENDING': 'info',
        'RUNNING': 'warning',
        'PAUSED': '',
        'SUCCESS': 'success',
        'FAILED': 'danger',
        'CANCELLED': 'info'
      }
      return statusMap[status] || 'info'
    },
    getStatusText(status) {
      const statusMap = {
        'PENDING': '待执行',
        'RUNNING': '执行中',
        'PAUSED': '已暂停',
        'SUCCESS': '已完成',
        'FAILED': '失败',
        'CANCELLED': '已取消'
      }
      return statusMap[status] || status
    },
    getProgressColor(percentage) {
      if (percentage >= 100) {
        return '#67C23A'
      } else if (percentage >= 75) {
        return '#409EFF'
      } else if (percentage >= 50) {
        return '#E6A23C'
      } else if (percentage >= 25) {
        return '#F56C6C'
      } else {
        return '#F56C6C'
      }
    },
    /**
     * 下载离线采集工具
     */
    async handleDownloadOfflineTool() {
      try {
        this.$message({
          message: '正在准备下载离线采集工具,请稍候...',
          type: 'info',
          duration: 2000
        })

        const response = await downloadOfflineTool()

        // 创建blob对象
        const blob = new Blob([response], { type: 'application/zip' })

        // 创建下载链接
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url

        // 生成文件名:示例云数据采集工具_YYYYMMDD_HHmmss.zip
        const now = new Date()
        const dateStr = now.getFullYear() +
                       String(now.getMonth() + 1).padStart(2, '0') +
                       String(now.getDate()).padStart(2, '0')
        const timeStr = String(now.getHours()).padStart(2, '0') +
                       String(now.getMinutes()).padStart(2, '0') +
                       String(now.getSeconds()).padStart(2, '0')
        link.download = `示例云数据采集工具_${dateStr}_${timeStr}.zip`

        // 触发下载
        document.body.appendChild(link)
        link.click()

        // 清理
        document.body.removeChild(link)
        window.URL.revokeObjectURL(url)

        this.$message.success('离线采集工具下载成功!')
      } catch (error) {
        console.error('下载离线采集工具失败:', error)
        this.$message.error('下载失败: ' + (error.message || '网络错误'))
      }
    }
  }
}
</script>

<style scoped>
.data-collection-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;
}

.box-card {
  margin-bottom: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  border-radius: 4px;
}

.title-text {
  font-size: 18px;
  font-weight: bold;
  color: #303133;
  letter-spacing: 0.5px;
}

.query-form {
  margin-bottom: 20px;
  padding: 10px 0;
}

.clearfix:after {
  content: "";
  display: table;
  clear: both;
}

/* 表格样式优化 */
/deep/ .el-table {
  background-color: #fff;
  border-radius: 4px;
  overflow: hidden;
}

/deep/ .el-table__header {
  background-color: #f5f7fa;
}

/deep/ .el-table__header th {
  background-color: #f5f7fa;
  color: #303133;
  font-weight: 600;
  border-bottom: 2px solid #dfe6e9;
}

/deep/ .el-table__body tr:hover > td {
  background-color: #f0f9ff !important;
}

/deep/ .el-table__body td {
  padding: 12px 0;
  border-bottom: 1px solid #ebeef5;
}

/deep/ .el-table__row {
  height: 50px;
}

/deep/ .el-progress {
  width: 100%;
}

/deep/ .el-button--mini {
  padding: 5px 10px;
  font-size: 12px;
}

/deep/ .el-tag {
  padding: 4px 12px;
  border-radius: 3px;
  font-weight: 500;
}

/* 分页样式 */
/deep/ .el-pagination {
  text-align: right;
  margin-top: 20px;
  padding-top: 20px;
  border-top: 1px solid #ebeef5;
}
</style>

