<template>
  <div class="bank-connect-log-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-document"></i>
            银企直连日志管理
          </h2>
          <p class="page-description">管理银企直连接口调用日志，包括请求记录、响应数据、执行状态和异常信息</p>
        </div>
        <div class="header-right">
          <el-button type="success" icon="el-icon-refresh" @click="handleRefresh">
            刷新日志
          </el-button>
          <el-button type="warning" icon="el-icon-delete" @click="handleCleanLogs">
            清理日志
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出日志
          </el-button>
        </div>
      </div>
    </div>

    <!-- 日志统计卡片 -->
    <div class="log-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-document"></i>
              </div>
              <div class="card-info">
                <div class="card-title">总日志数</div>
                <div class="card-value">{{ totalLogs }}</div>
                <div class="card-change">条记录</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon success-icon">
                <i class="el-icon-success"></i>
              </div>
              <div class="card-info">
                <div class="card-title">成功调用</div>
                <div class="card-value">{{ successLogs }}</div>
                <div class="card-change positive">{{ successRate }}%</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon error-icon">
                <i class="el-icon-error"></i>
              </div>
              <div class="card-info">
                <div class="card-title">失败调用</div>
                <div class="card-value">{{ errorLogs }}</div>
                <div class="card-change negative">{{ errorRate }}%</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon time-icon">
                <i class="el-icon-time"></i>
              </div>
              <div class="card-info">
                <div class="card-title">平均响应时间</div>
                <div class="card-value">{{ avgResponseTime }}</div>
                <div class="card-change">毫秒</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 搜索区域 -->
    <el-card class="search-card" shadow="never">
      <div class="search-form">
        <el-form :inline="true" :model="listQuery" class="demo-form-inline">
          <el-form-item label="请求ID">
            <el-input
              v-model="listQuery.requestId"
              placeholder="请输入请求ID"
              style="width: 150px;"
              class="filter-item"
              @keyup.enter.native="handleFilter"
            />
          </el-form-item>
          <el-form-item label="接口类型">
            <el-select
              v-model="listQuery.interfaceType"
              placeholder="请选择接口类型"
              clearable
              style="width: 150px;"
            >
              <el-option label="余额查询" value="BALANCE_QUERY" />
              <el-option label="交易明细" value="TRANSACTION_DETAIL" />
              <el-option label="支付" value="PAYMENT" />
              <el-option label="转账" value="TRANSFER" />
              <el-option label="对账" value="RECONCILIATION" />
            </el-select>
          </el-form-item>
          <el-form-item label="执行状态">
            <el-select
              v-model="listQuery.isSuccessful"
              placeholder="请选择状态"
              clearable
              style="width: 100px;"
            >
              <el-option label="成功" :value="1" />
              <el-option label="失败" :value="0" />
            </el-select>
          </el-form-item>
          <el-form-item label="请求时间">
            <el-date-picker
              v-model="listQuery.requestTimeRange"
              type="datetimerange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              style="width: 300px;"
            />
          </el-form-item>
          <el-form-item>
            <el-button v-waves class="filter-item" type="primary" icon="el-icon-search" @click="handleFilter">
              搜索
            </el-button>
            <el-button v-waves class="filter-item" type="default" icon="el-icon-refresh" @click="resetQuery">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>

    <!-- 日志表格 -->
    <el-card class="table-card" shadow="never">
      <el-table
        :key="tableKey"
        v-loading="listLoading"
        :data="list"
        border
        style="width: 100%;"
        row-key="logId"
        @sort-change="sortChange"
      >
        <el-table-column label="日志ID" prop="logId" width="80" align="center" />
        <el-table-column label="请求ID" prop="requestId" width="150" align="center" />
        <el-table-column label="接口类型" prop="interfaceType" width="120" align="center" />
        <el-table-column label="银行名称" prop="bankName" width="120" align="center" />
        <el-table-column label="请求时间" prop="requestTime" width="160" align="center" />
        <el-table-column label="响应时间" prop="responseTime" width="160" align="center" />
        <el-table-column label="执行时间" width="100" align="center">
          <template slot-scope="{row}">
            <span>{{ row.executionTime || row.elapsedTime }}ms</span>
          </template>
        </el-table-column>
        <el-table-column label="状态码" width="80" align="center">
          <template slot-scope="{row}">
            <span>{{ row.statusCode || row.responseStatus }}</span>
          </template>
        </el-table-column>
        <el-table-column label="执行状态" width="80" align="center">
          <template slot-scope="{row}">
            <el-tag :type="row.isSuccessful ? 'success' : 'danger'" size="mini">
              {{ row.isSuccessful ? '成功' : '失败' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="状态信息" min-width="150">
          <template slot-scope="{row}">
            <span>{{ row.statusMessage || (row.isSuccessful ? '请求成功' : (row.errorMessage || '请求失败')) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="150">
          <template slot-scope="{row}">
            <el-button type="primary" size="mini" @click="handleViewDetail(row)">查看详情</el-button>
            <el-button v-if="!row.isSuccessful" size="mini" type="warning" @click="handleRetry(row)">重试</el-button>
          </template>
        </el-table-column>
      </el-table>

      <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
    </el-card>

    <!-- 日志详情对话框 -->
    <el-dialog title="日志详情" :visible.sync="dialogDetailVisible" width="1000px">
      <div v-if="currentLog" class="log-detail">
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="detail-section">
              <h4>基本信息</h4>
              <el-descriptions :column="1" border>
                <el-descriptions-item label="日志ID">{{ currentLog.logId }}</el-descriptions-item>
                <el-descriptions-item label="请求ID">{{ currentLog.requestId }}</el-descriptions-item>
                <el-descriptions-item label="接口类型">{{ getInterfaceTypeText(currentLog.interfaceType) }}</el-descriptions-item>
                <el-descriptions-item label="请求时间">{{ currentLog.requestTime }}</el-descriptions-item>
                <el-descriptions-item label="响应时间">{{ currentLog.responseTime }}</el-descriptions-item>
                <el-descriptions-item label="执行时间">{{ currentLog.executionTime }}ms</el-descriptions-item>
                <el-descriptions-item label="状态码">{{ currentLog.statusCode }}</el-descriptions-item>
                <el-descriptions-item label="执行状态">
                  <el-tag :type="currentLog.isSuccessful | statusFilter">
                    {{ currentLog.isSuccessful ? '成功' : '失败' }}
                  </el-tag>
                </el-descriptions-item>
              </el-descriptions>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="detail-section">
              <h4>状态信息</h4>
              <div class="status-info">
                <p><strong>状态消息：</strong>{{ currentLog.statusMessage }}</p>
                <p v-if="currentLog.errorMessage"><strong>错误信息：</strong></p>
                <pre v-if="currentLog.errorMessage" class="error-message">{{ currentLog.errorMessage }}</pre>
              </div>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20" style="margin-top: 20px;">
          <el-col :span="12">
            <div class="detail-section">
              <h4>请求数据</h4>
              <pre class="json-data">{{ formatJsonData(currentLog.requestData) }}</pre>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="detail-section">
              <h4>响应数据</h4>
              <pre class="json-data">{{ formatJsonData(currentLog.responseData) }}</pre>
            </div>
          </el-col>
        </el-row>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogDetailVisible = false">关闭</el-button>
        <el-button v-if="currentLog && !currentLog.isSuccessful" type="warning" @click="handleRetry(currentLog)">
          重试请求
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getBankConnectLogPage,
  getBankConnectLogDetail,
  getBankConnectLogStatistics,
  retryBankConnectLog,
  exportBankConnectLog as exportBankConnectLogAPI,
  cleanBankConnectLog
} from '@/api/globalTreasurer-new/partnerDirectConnection/bank'
import waves from '@/directive/waves' // waves directive
import Pagination from '@/components/Pagination' // secondary package based on el-pagination

export default {
  name: 'BankConnectLogManage',
  components: { Pagination },
  directives: { waves },
  filters: {
    statusFilter(status) {
      const statusMap = {
        1: 'success',
        0: 'danger'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      tableKey: 0,
      list: [],
      total: 0,
      listLoading: true,
      listQuery: {
        page: 1,
        limit: 20,
        requestId: undefined,
        interfaceType: undefined,
        isSuccessful: undefined,
        requestTimeRange: undefined
      },
      totalLogs: 0,
      successLogs: 0,
      errorLogs: 0,
      successRate: 0,
      errorRate: 0,
      avgResponseTime: 0,
      dialogDetailVisible: false,
      currentLog: null
    }
  },
  created() {
    this.getList()
    this.updateStatistics()
  },
  methods: {
    async getList(paginationParams) {
      this.listLoading = true
      try {
        // 如果有分页参数（从 @pagination 事件传递），则更新查询参数
        if (paginationParams) {
          this.listQuery.page = paginationParams.page
          this.listQuery.limit = paginationParams.limit
        }

        // 请求后端接口
        const params = {
          pageNum: this.listQuery.page,
          pageSize: this.listQuery.limit,
          requestId: this.listQuery.requestId,
          interfaceType: this.listQuery.interfaceType,
          isSuccessful: this.listQuery.isSuccessful,
          requestTimeRange: this.listQuery.requestTimeRange
        }
        const response = await getBankConnectLogPage(params)

        // 检查响应数据
        if (response && response.data) {
          // 先设置总数，无论数据是否为空
          this.total = parseInt(response.data.totalRecord || response.data.total || 0)

          // 处理列表数据
          const rawData = response.data.tlist || response.data.list || []

          if (rawData.length > 0) {
            // 处理数据,将后端字段映射到前端需要的字段
            const processedList = rawData.map(item => {
              try {
                return {
                  ...item,
                  executionTime: item.elapsedTime, // 映射执行时间
                  statusCode: item.responseStatus, // 映射状态码
                  statusMessage: item.isSuccessful ? '请求成功' : (item.errorMessage || '请求失败'), // 映射状态信息
                  requestData: item.requestBody, // 映射请求数据
                  responseData: item.responseBody, // 映射响应数据
                  requestTime: this.formatTimestamp(item.requestTime), // 格式化请求时间
                  responseTime: this.formatTimestamp(item.responseTime) // 格式化响应时间
                }
              } catch (err) {
                // 如果格式化失败,返回原始数据
                return {
                  ...item,
                  executionTime: item.elapsedTime,
                  statusCode: item.responseStatus,
                  statusMessage: item.isSuccessful ? '请求成功' : (item.errorMessage || '请求失败'),
                  requestData: item.requestBody,
                  responseData: item.responseBody,
                  requestTime: item.requestTime,
                  responseTime: item.responseTime
                }
              }
            })

            this.list = processedList
          } else {
            // 数据为空时清空列表
            this.list = []

            // 如果当前页没有数据且不是第一页，则调整到上一页
            if (this.listQuery.page > 1) {
              this.listQuery.page = this.listQuery.page - 1
              // 重新获取数据
              this.getList()
              return
            }
          }

          // 计算总页数并限制当前页码
          const totalPages = Math.ceil(this.total / this.listQuery.limit) || 1
          if (this.listQuery.page > totalPages) {
            this.listQuery.page = totalPages
            // 重新获取数据
            this.getList()
            return
          }

          // 强制触发表格更新 - 修改key值
          this.tableKey = this.tableKey + 1
        } else {
          this.$message.error('获取日志列表失败:响应格式错误')
          this.list = []
          this.total = 0
        }
      } catch (error) {
        console.error('获取日志列表失败:', error)
        this.$message.error('获取日志列表失败')
        this.list = []
        this.total = 0
      } finally {
        this.listLoading = false
      }
    },
    async updateStatistics() {
      try {
        const response = await getBankConnectLogStatistics()
        if (response && response.code === 1) {
          const data = response.data || {}
          // 修正字段名，与后端返回的JSON字段保持一致
          this.totalLogs = data.total || 0
          this.successLogs = data.successCount || 0
          this.errorLogs = data.failCount || 0
          // successRate 后端返回的是字符串 "90.00%"，需要转换为数字用于显示
          const rateStr = data.successRate || '0%'
          this.successRate = parseFloat(rateStr.replace('%', ''))
          this.errorRate = this.totalLogs > 0 ? ((this.errorLogs / this.totalLogs) * 100).toFixed(2) : 0
          this.avgResponseTime = data.avgResponseTime || 0
        }
      } catch (error) {
        console.error('获取统计数据失败:', error)
      }
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    resetQuery() {
      this.listQuery = {
        page: 1,
        limit: 20,
        requestId: undefined,
        interfaceType: undefined,
        isSuccessful: undefined,
        requestTimeRange: undefined
      }
      this.getList()
    },
    handleViewDetail(row) {
      // 应用字段映射到详情数据
      this.currentLog = {
        ...row,
        executionTime: row.elapsedTime || row.executionTime,
        statusCode: row.responseStatus || row.statusCode,
        statusMessage: row.statusMessage || (row.isSuccessful ? '请求成功' : (row.errorMessage || '请求失败')),
        requestData: row.requestBody || row.requestData,
        responseData: row.responseBody || row.responseData,
        requestTime: this.formatTimestamp(row.requestTime),
        responseTime: this.formatTimestamp(row.responseTime)
      }
      this.dialogDetailVisible = true
    },
    async handleRetry(row) {
      try {
        this.$confirm('确认重试该请求?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(async () => {
          // 调用重试API
          const response = await retryBankConnectLog(row.logId)
          if (response && response.code === 1) {
            this.$message({
              type: 'success',
              message: '重试请求已提交，请等待处理结果'
            })
            this.getList() // 刷新列表
          } else {
            this.$message({
              type: 'error',
              message: response?.message || '重试请求提交失败'
            })
          }
        })
      } catch (error) {
        console.error('重试请求失败:', error)
        this.$message({
          type: 'error',
          message: '重试请求提交失败，请稍后重试'
        })
      }
    },
    handleRefresh() {
      this.getList()
      this.$message({
        type: 'success',
        message: '日志刷新成功'
      })
    },
    async handleCleanLogs() {
      try {
        const { value: days } = await this.$prompt('请输入要保留的天数（30天前的日志将被清理）:', '清理历史日志', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          inputValue: '30',
          inputPattern: /^[1-9]\d*$/,
          inputErrorMessage: '请输入有效的天数'
        })

        this.$confirm(`确认清理 ${days} 天前的历史日志? 此操作不可恢复`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(async () => {
          const response = await cleanBankConnectLog(days)
          if (response && response.code === 1) {
            this.$message({
              type: 'success',
              message: `成功清理 ${response.data?.deletedCount || 0} 条历史日志`
            })
            this.getList() // 刷新列表
            this.updateStatistics() // 更新统计
          } else {
            this.$message({
              type: 'error',
              message: response?.message || '日志清理失败'
            })
          }
        })
      } catch (error) {
        if (error !== 'cancel') {
          console.error('清理日志失败:', error)
          this.$message({
            type: 'error',
            message: '日志清理失败，请稍后重试'
          })
        }
      }
    },
    async handleExport() {
      try {
        this.$message({
          type: 'info',
          message: '正在导出日志数据，请稍候...'
        })

        const response = await exportBankConnectLogAPI(this.listQuery)
        if (response && response.code === 1) {
          // 创建下载链接
          const blob = new Blob([response.data], {
            type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
          })
          const url = window.URL.createObjectURL(blob)
          const link = document.createElement('a')
          link.href = url
          link.download = `银行连接日志_${new Date().toISOString().slice(0, 10)}.xlsx`
          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link)
          window.URL.revokeObjectURL(url)

          this.$message({
            type: 'success',
            message: '日志导出成功'
          })
        } else {
          this.$message({
            type: 'error',
            message: response?.message || '日志导出失败'
          })
        }
      } catch (error) {
        console.error('导出日志失败:', error)
        this.$message({
          type: 'error',
          message: '日志导出失败，请稍后重试'
        })
      }
    },
    sortChange(data) {
      const { prop, order } = data
      if (prop === 'logId') {
        this.sortByID(order)
      }
    },
    sortByID(order) {
      if (order === 'ascending') {
        this.list.sort((a, b) => a.logId - b.logId)
      } else {
        this.list.sort((a, b) => b.logId - a.logId)
      }
    },
    getInterfaceTypeTagType(interfaceType) {
      const typeMap = {
        'BALANCE_QUERY': 'primary',
        'TRANSACTION_DETAIL': 'success',
        'PAYMENT': 'warning',
        'TRANSFER': 'danger',
        'RECONCILIATION': 'info'
      }
      return typeMap[interfaceType] || 'info'
    },
    getInterfaceTypeText(interfaceType) {
      const textMap = {
        'BALANCE_QUERY': '余额查询',
        'TRANSACTION_DETAIL': '交易明细',
        'PAYMENT': '支付',
        'TRANSFER': '转账',
        'RECONCILIATION': '对账'
      }
      return textMap[interfaceType] || interfaceType
    },
    getStatusCodeTagType(statusCode) {
      if (statusCode === '0000') {
        return 'success'
      } else if (statusCode.startsWith('9')) {
        return 'danger'
      } else {
        return 'warning'
      }
    },
    getExecutionTimeClass(executionTime) {
      if (executionTime < 1000) {
        return 'fast-response'
      } else if (executionTime < 3000) {
        return 'normal-response'
      } else {
        return 'slow-response'
      }
    },
    formatJsonData(jsonString) {
      if (!jsonString) return ''
      try {
        const obj = JSON.parse(jsonString)
        return JSON.stringify(obj, null, 2)
      } catch (e) {
        return jsonString
      }
    },
    formatTimestamp(timestamp) {
      if (!timestamp) return ''
      const date = new Date(timestamp)
      const year = date.getFullYear()
      const month = String(date.getMonth() + 1).padStart(2, '0')
      const day = String(date.getDate()).padStart(2, '0')
      const hours = String(date.getHours()).padStart(2, '0')
      const minutes = String(date.getMinutes()).padStart(2, '0')
      const seconds = String(date.getSeconds()).padStart(2, '0')
      return `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
    }
  }
}
</script>

<style lang="scss" scoped>
.bank-connect-log-manage {
  padding: 20px;

  .page-header {
    margin-bottom: 20px;
    .header-content {
      display: flex;
      justify-content: space-between;
      align-items: center;
      .header-left {
        .page-title {
          margin: 0 0 8px 0;
          font-size: 24px;
          font-weight: 600;
          color: #303133;
          i {
            margin-right: 8px;
            color: #409EFF;
          }
        }
        .page-description {
          margin: 0;
          color: #606266;
          font-size: 14px;
        }
      }
    }
  }

  .log-overview {
    margin-bottom: 20px;
    .overview-card {
      .card-content {
        display: flex;
        align-items: center;
        .card-icon {
          width: 60px;
          height: 60px;
          border-radius: 8px;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 16px;
          i {
            font-size: 24px;
            color: white;
          }
          &.total-icon {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          }
          &.success-icon {
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
          }
          &.error-icon {
            background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
          }
          &.time-icon {
            background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
          }
        }
        .card-info {
          flex: 1;
          .card-title {
            font-size: 14px;
            color: #909399;
            margin-bottom: 8px;
          }
          .card-value {
            font-size: 24px;
            font-weight: 600;
            color: #303133;
            margin-bottom: 4px;
          }
          .card-change {
            font-size: 12px;
            color: #909399;
            &.positive {
              color: #67C23A;
            }
            &.negative {
              color: #F56C6C;
            }
          }
        }
      }
    }
  }

  .search-card, .table-card {
    margin-bottom: 20px;
  }

  .link-type {
    color: #409EFF;
    cursor: pointer;
    &:hover {
      color: #66b1ff;
    }
  }

  .fast-response {
    color: #67C23A;
    font-weight: 600;
  }

  .normal-response {
    color: #E6A23C;
    font-weight: 600;
  }

  .slow-response {
    color: #F56C6C;
    font-weight: 600;
  }

  .success-message {
    color: #67C23A;
  }

  .error-message {
    color: #F56C6C;
  }

  .log-detail {
    .detail-section {
      margin-bottom: 20px;
      h4 {
        margin-bottom: 10px;
        color: #303133;
        font-size: 16px;
        font-weight: 600;
      }
      .status-info {
        p {
          margin: 8px 0;
          line-height: 1.5;
        }
      }
      .json-data, .error-message {
        background: #f5f7fa;
        border: 1px solid #e4e7ed;
        border-radius: 4px;
        padding: 12px;
        font-family: 'Courier New', monospace;
        font-size: 12px;
        line-height: 1.4;
        max-height: 200px;
        overflow-y: auto;
        white-space: pre-wrap;
        word-break: break-all;
      }
      .error-message {
        background: #fef0f0;
        border-color: #fbc4c4;
        color: #f56c6c;
      }
    }
  }
}
</style>
