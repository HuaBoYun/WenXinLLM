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
        fit
        highlight-current-row
        style="width: 100%;"
        @sort-change="sortChange"
      >
        <el-table-column label="日志ID" prop="logId" sortable="custom" align="center" width="80">
          <template slot-scope="{row}">
            <span>{{ row.logId }}</span>
          </template>
        </el-table-column>
        <el-table-column label="请求ID" width="150px" align="center">
          <template slot-scope="{row}">
            <span class="link-type" @click="handleViewDetail(row)">{{ row.requestId }}</span>
          </template>
        </el-table-column>
        <el-table-column label="接口类型" width="120px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getInterfaceTypeTagType(row.interfaceType)" size="mini">
              {{ getInterfaceTypeText(row.interfaceType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="请求时间" width="160px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.requestTime }}</span>
          </template>
        </el-table-column>
        <el-table-column label="响应时间" width="160px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.responseTime }}</span>
          </template>
        </el-table-column>
        <el-table-column label="执行时间" width="100px" align="center">
          <template slot-scope="{row}">
            <span :class="getExecutionTimeClass(row.executionTime)">{{ row.executionTime }}ms</span>
          </template>
        </el-table-column>
        <el-table-column label="状态码" width="80px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getStatusCodeTagType(row.statusCode)" size="mini">
              {{ row.statusCode }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="执行状态" class-name="status-col" width="80">
          <template slot-scope="{row}">
            <el-tag :type="row.isSuccessful | statusFilter">
              {{ row.isSuccessful ? '成功' : '失败' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="状态信息" min-width="150px">
          <template slot-scope="{row}">
            <span :class="row.isSuccessful ? 'success-message' : 'error-message'">
              {{ row.statusMessage }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" width="150" class-name="small-padding fixed-width">
          <template slot-scope="{row}">
            <el-button type="primary" size="mini" @click="handleViewDetail(row)">
              查看详情
            </el-button>
            <el-button v-if="!row.isSuccessful" size="mini" type="warning" @click="handleRetry(row)">
              重试
            </el-button>
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
import { getBankConnectLogPage, retryBankConnectRequest, exportBankConnectLog, cleanHistoryLogs } from '@/api/globalTreasurer/treasuryCommon'
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
    getList() {
      this.listLoading = true
      // 使用模拟数据
      setTimeout(() => {
        this.list = [
          {
            logId: 1,
            configId: 1,
            requestId: 'REQ_20240925_001',
            interfaceType: 'BALANCE_QUERY',
            requestTime: '2024-09-25 09:30:15',
            responseTime: '2024-09-25 09:30:16',
            requestData: '{"accountNo":"1234567890","queryDate":"2024-09-25"}',
            responseData: '{"code":"0000","message":"查询成功","balance":"1000000.00"}',
            statusCode: '0000',
            statusMessage: '查询成功',
            executionTime: 1250,
            isSuccessful: 1,
            errorMessage: null,
            createTime: '2024-09-25 09:30:16'
          },
          {
            logId: 2,
            configId: 2,
            requestId: 'REQ_20240925_002',
            interfaceType: 'PAYMENT',
            requestTime: '2024-09-25 10:15:30',
            responseTime: '2024-09-25 10:15:35',
            requestData: '{"fromAccount":"1234567890","toAccount":"0987654321","amount":"50000.00"}',
            responseData: '{"code":"9999","message":"账户余额不足"}',
            statusCode: '9999',
            statusMessage: '账户余额不足',
            executionTime: 5200,
            isSuccessful: 0,
            errorMessage: '支付失败：账户余额不足，当前余额：30000.00，支付金额：50000.00',
            createTime: '2024-09-25 10:15:35'
          },
          {
            logId: 3,
            configId: 1,
            requestId: 'REQ_20240925_003',
            interfaceType: 'TRANSACTION_DETAIL',
            requestTime: '2024-09-25 11:20:45',
            responseTime: '2024-09-25 11:20:46',
            requestData: '{"accountNo":"1234567890","startDate":"2024-09-20","endDate":"2024-09-25"}',
            responseData: '{"code":"0000","message":"查询成功","transactions":[...]}',
            statusCode: '0000',
            statusMessage: '查询成功',
            executionTime: 890,
            isSuccessful: 1,
            errorMessage: null,
            createTime: '2024-09-25 11:20:46'
          }
        ]
        this.total = this.list.length
        this.listLoading = false
      }, 1000)
    },
    updateStatistics() {
      this.totalLogs = 1256
      this.successLogs = 1089
      this.errorLogs = 167
      this.successRate = Math.round((this.successLogs / this.totalLogs) * 100)
      this.errorRate = Math.round((this.errorLogs / this.totalLogs) * 100)
      this.avgResponseTime = 1350
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
      this.currentLog = row
      this.dialogDetailVisible = true
    },
    handleRetry(row) {
      this.$confirm('确认重试该请求?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message({
          type: 'success',
          message: '重试请求已提交'
        })
      })
    },
    handleRefresh() {
      this.getList()
      this.$message({
        type: 'success',
        message: '日志刷新成功'
      })
    },
    handleCleanLogs() {
      this.$confirm('确认清理历史日志? 此操作将删除30天前的日志记录', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message({
          type: 'success',
          message: '日志清理成功'
        })
      })
    },
    handleExport() {
      this.$message({
        type: 'success',
        message: '日志导出成功'
      })
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
