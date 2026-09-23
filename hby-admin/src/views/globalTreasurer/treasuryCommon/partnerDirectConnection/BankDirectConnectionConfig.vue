<template>
  <div class="bank-direct-connection-config">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-link"></i>
            银行直连配置
          </h2>
          <p class="page-description">管理银行直连接口配置，包括连接参数、认证信息和通信协议设置</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增配置
          </el-button>
          <el-button type="success" icon="el-icon-connection" @click="handleTestAll">
            批量测试
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出配置
          </el-button>
        </div>
      </div>
    </div>

    <!-- 连接状态概览 -->
    <div class="connection-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-link"></i>
              </div>
              <div class="card-info">
                <div class="card-title">总连接数</div>
                <div class="card-value">{{ totalConnections }}</div>
                <div class="card-change">已配置连接</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon active-icon">
                <i class="el-icon-success"></i>
              </div>
              <div class="card-info">
                <div class="card-title">正常连接</div>
                <div class="card-value">{{ activeConnections }}</div>
                <div class="card-change positive">运行正常</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon error-icon">
                <i class="el-icon-warning"></i>
              </div>
              <div class="card-info">
                <div class="card-title">异常连接</div>
                <div class="card-value">{{ errorConnections }}</div>
                <div class="card-change negative">需要处理</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon check-icon">
                <i class="el-icon-time"></i>
              </div>
              <div class="card-info">
                <div class="card-title">最后检查</div>
                <div class="card-value">{{ lastCheckTime }}</div>
                <div class="card-change">连接状态</div>
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
          <el-form-item label="银行名称">
            <el-input
              v-model="listQuery.bankName"
              placeholder="请输入银行名称"
              style="width: 200px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="连接类型">
            <el-select
              v-model="listQuery.connectionType"
              placeholder="请选择连接类型"
              clearable
              style="width: 150px;"
            >
              <el-option label="HTTP接口" value="HTTP" />
              <el-option label="WebService" value="WEBSERVICE" />
              <el-option label="FTP传输" value="FTP" />
              <el-option label="专线连接" value="DEDICATED" />
            </el-select>
          </el-form-item>
          <el-form-item label="连接状态">
            <el-select
              v-model="listQuery.connectionStatus"
              placeholder="请选择连接状态"
              clearable
              style="width: 120px;"
            >
              <el-option label="正常" value="NORMAL" />
              <el-option label="异常" value="ERROR" />
              <el-option label="维护中" value="MAINTENANCE" />
            </el-select>
          </el-form-item>
          <el-form-item label="状态">
            <el-select
              v-model="listQuery.status"
              placeholder="请选择状态"
              clearable
              style="width: 100px;"
            >
              <el-option label="启用" value="1" />
              <el-option label="禁用" value="0" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="handleFilter">
              搜索
            </el-button>
            <el-button icon="el-icon-refresh" @click="handleReset">
              重置
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>

    <!-- 数据表格 -->
    <el-card class="table-card" shadow="never">
      <div class="table-header">
        <div class="table-title">
          <span class="title-text">银行直连配置列表</span>
          <span class="title-count">共 {{ total }} 条记录</span>
        </div>
        <div class="table-actions">
          <el-button-group>
            <el-button size="small" icon="el-icon-refresh" @click="fetchData">刷新</el-button>
            <el-button size="small" icon="el-icon-setting" @click="handleTableSetting">设置</el-button>
          </el-button-group>
        </div>
      </div>

      <el-table
        :key="tableKey"
        v-loading="listLoading"
        :data="list"
        border
        fit
        highlight-current-row
        style="width: 100%;"
        :row-class-name="tableRowClassName"
        @selection-change="handleSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="序号" type="index" width="60" align="center" />
        
        <el-table-column label="银行信息" prop="bankName" align="center" width="200" show-overflow-tooltip>
          <template slot-scope="{row}">
            <div class="bank-info">
              <div class="bank-logo">
                <i class="el-icon-office-building"></i>
              </div>
              <div class="bank-details">
                <div class="bank-name">{{ row.bankName }}</div>
                <div class="bank-code">{{ row.bankCode }}</div>
              </div>
            </div>
          </template>
        </el-table-column>
        
        <el-table-column label="连接类型" prop="connectionType" align="center" width="120">
          <template slot-scope="{row}">
            <el-tag :type="getConnectionTypeColor(row.connectionType)" size="small">
              <i :class="getConnectionTypeIcon(row.connectionType)"></i>
              {{ getConnectionTypeText(row.connectionType) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column label="连接地址" prop="connectionUrl" min-width="250" show-overflow-tooltip>
          <template slot-scope="{row}">
            <div class="connection-url">
              <i class="el-icon-link"></i>
              <span>{{ row.connectionUrl || '未配置' }}</span>
            </div>
          </template>
        </el-table-column>
        
        <el-table-column label="连接状态" prop="connectionStatus" align="center" width="120">
          <template slot-scope="{row}">
            <div class="connection-status">
              <el-badge :status="getConnectionStatusType(row.connectionStatus)" />
              <span :class="'status-text ' + row.connectionStatus.toLowerCase()">
                {{ getConnectionStatusText(row.connectionStatus) }}
              </span>
            </div>
          </template>
        </el-table-column>
        
        <el-table-column label="最后测试" prop="lastTestTime" align="center" width="160">
          <template slot-scope="{row}">
            <span class="test-time">
              <i class="el-icon-time"></i>
              {{ formatTime(row.lastTestTime) }}
            </span>
          </template>
        </el-table-column>
        
        <el-table-column label="状态" class-name="status-col" width="80" align="center">
          <template slot-scope="{row}">
            <el-switch
              v-model="row.status"
              :active-value="1"
              :inactive-value="0"
              active-color="#13ce66"
              inactive-color="#ff4949"
              @change="handleStatusChange(row)"
            />
          </template>
        </el-table-column>
        
        <el-table-column label="操作" align="center" width="240" class-name="small-padding fixed-width">
          <template slot-scope="{row,$index}">
            <el-button-group>
              <el-button type="primary" size="mini" icon="el-icon-edit" @click="handleUpdate(row)">
                编辑
              </el-button>
              <el-button type="success" size="mini" icon="el-icon-connection" @click="handleTest(row)">
                测试
              </el-button>
              <el-button type="info" size="mini" icon="el-icon-view" @click="handleView(row)">
                详情
              </el-button>
              <el-button 
                v-if="row.status !== 'deleted'" 
                size="mini" 
                type="danger" 
                icon="el-icon-delete"
                @click="handleDelete(row,$index)"
              >
                删除
              </el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页 -->
      <div class="pagination-wrapper">
        <el-pagination
          background
          :current-page="listQuery.pageNum"
          :layout="layout"
          :page-size="listQuery.pageSize"
          :total="total"
          :page-sizes="[10, 20, 50, 100]"
          @current-change="handleCurrentChange"
          @size-change="handleSizeChange"
        />
      </div>
    </el-card>
  </div>
</template>

<script>
import waves from '@/directive/waves'

export default {
  name: 'BankDirectConnectionConfig',
  directives: { waves },
  filters: {
    statusFilter(status) {
      const statusMap = {
        1: 'success',
        0: 'info'
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
      layout: 'total, sizes, prev, pager, next, jumper',
      listQuery: {
        pageNum: 1,
        pageSize: 20,
        bankName: undefined,
        connectionType: undefined,
        connectionStatus: undefined,
        status: undefined
      },
      multipleSelection: [],
      totalConnections: 12,
      activeConnections: 10,
      errorConnections: 2,
      lastCheckTime: new Date().toLocaleString()
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    async fetchData() {
      this.listLoading = true
      setTimeout(() => {
        this.list = [
          {
            id: 1,
            bankName: '中国工商银行',
            bankCode: 'ICBC',
            connectionType: 'HTTP',
            connectionUrl: 'https://api.icbc.com.cn/gateway',
            connectionStatus: 'NORMAL',
            lastTestTime: new Date(),
            status: 1
          },
          {
            id: 2,
            bankName: '中国建设银行',
            bankCode: 'CCB',
            connectionType: 'WEBSERVICE',
            connectionUrl: 'https://webservice.ccb.com/api',
            connectionStatus: 'ERROR',
            lastTestTime: new Date(),
            status: 1
          }
        ]
        this.total = 2
        this.listLoading = false
      }, 1000)
    },
    handleFilter() {
      this.listQuery.pageNum = 1
      this.fetchData()
    },
    handleReset() {
      this.listQuery = {
        pageNum: 1,
        pageSize: 20,
        bankName: undefined,
        connectionType: undefined,
        connectionStatus: undefined,
        status: undefined
      }
      this.fetchData()
    },
    handleTestAll() {
      this.$message.success('批量连接测试完成')
      this.fetchData()
    },
    handleExport() {
      this.$message.info('导出功能开发中...')
    },
    handleTableSetting() {
      this.$message.info('表格设置功能开发中...')
    },
    tableRowClassName({ row, rowIndex }) {
      if (row.status === 0) {
        return 'disabled-row'
      }
      if (row.connectionStatus === 'ERROR') {
        return 'error-row'
      }
      return ''
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    getConnectionTypeColor(type) {
      const colorMap = {
        'HTTP': 'success',
        'WEBSERVICE': 'primary',
        'FTP': 'warning',
        'DEDICATED': 'danger'
      }
      return colorMap[type] || 'info'
    },
    getConnectionTypeIcon(type) {
      const iconMap = {
        'HTTP': 'el-icon-link',
        'WEBSERVICE': 'el-icon-service',
        'FTP': 'el-icon-upload',
        'DEDICATED': 'el-icon-connection'
      }
      return iconMap[type] || 'el-icon-link'
    },
    getConnectionTypeText(type) {
      const textMap = {
        'HTTP': 'HTTP接口',
        'WEBSERVICE': 'WebService',
        'FTP': 'FTP传输',
        'DEDICATED': '专线连接'
      }
      return textMap[type] || '未知类型'
    },
    getConnectionStatusType(status) {
      const statusMap = {
        'NORMAL': 'success',
        'ERROR': 'danger',
        'MAINTENANCE': 'warning'
      }
      return statusMap[status] || 'info'
    },
    getConnectionStatusText(status) {
      const textMap = {
        'NORMAL': '正常',
        'ERROR': '异常',
        'MAINTENANCE': '维护中'
      }
      return textMap[status] || '未知状态'
    },
    formatTime(time) {
      if (!time) return '-'
      return new Date(time).toLocaleString()
    },
    handleStatusChange(row) {
      this.$message.success('状态更新成功')
    },
    handleView(row) {
      this.$message.info('查看功能开发中...')
    },
    handleTest(row) {
      this.$message.success('连接测试成功')
    },
    handleCreate() {
      this.$message.info('新增功能开发中...')
    },
    handleUpdate(row) {
      this.$message.info('编辑功能开发中...')
    },
    handleDelete(row, index) {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.$message.success('删除成功')
        this.fetchData()
      })
    },
    handleCurrentChange(val) {
      this.listQuery.pageNum = val
      this.fetchData()
    },
    handleSizeChange(val) {
      this.listQuery.pageSize = val
      this.fetchData()
    }
  }
}
</script>

<style lang="scss" scoped>
.bank-direct-connection-config {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 84px);

  .page-header {
    margin-bottom: 20px;
    
    .header-content {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 20px;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      border-radius: 8px;
      color: white;
      
      .header-left {
        .page-title {
          margin: 0 0 8px 0;
          font-size: 24px;
          font-weight: 600;
          display: flex;
          align-items: center;
          
          i {
            margin-right: 12px;
            font-size: 28px;
          }
        }
        
        .page-description {
          margin: 0;
          opacity: 0.9;
          font-size: 14px;
        }
      }
      
      .header-right {
        .el-button {
          margin-left: 12px;
        }
      }
    }
  }

  .connection-overview {
    margin-bottom: 20px;
    
    .overview-card {
      border-radius: 8px;
      transition: all 0.3s ease;
      
      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
      }
      
      .card-content {
        display: flex;
        align-items: center;
        padding: 10px;
        
        .card-icon {
          width: 60px;
          height: 60px;
          border-radius: 50%;
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
          
          &.active-icon {
            background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
          }
          
          &.error-icon {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
          }
          
          &.check-icon {
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
          }
        }
        
        .card-info {
          flex: 1;
          
          .card-title {
            font-size: 14px;
            color: #909399;
            margin-bottom: 4px;
          }
          
          .card-value {
            font-size: 20px;
            font-weight: 600;
            color: #303133;
            margin-bottom: 4px;
          }
          
          .card-change {
            font-size: 12px;
            
            &.positive {
              color: #67c23a;
            }
            
            &.negative {
              color: #f56c6c;
            }
          }
        }
      }
    }
  }

  .search-card {
    margin-bottom: 20px;
    border-radius: 8px;
  }

  .table-card {
    border-radius: 8px;
    
    .table-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 16px;
      
      .table-title {
        .title-text {
          font-size: 16px;
          font-weight: 600;
          color: #303133;
        }
        
        .title-count {
          margin-left: 12px;
          color: #909399;
          font-size: 14px;
        }
      }
    }
    
    .bank-info {
      display: flex;
      align-items: center;
      
      .bank-logo {
        width: 40px;
        height: 40px;
        border-radius: 8px;
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 12px;
        
        i {
          color: white;
          font-size: 18px;
        }
      }
      
      .bank-details {
        .bank-name {
          font-weight: 600;
          color: #303133;
        }
        
        .bank-code {
          font-size: 12px;
          color: #909399;
          margin-top: 2px;
        }
      }
    }
    
    .connection-url {
      display: flex;
      align-items: center;
      
      i {
        margin-right: 8px;
        color: #409eff;
      }
      
      span {
        color: #606266;
        font-size: 13px;
      }
    }
    
    .connection-status {
      display: flex;
      align-items: center;
      
      .status-text {
        margin-left: 8px;
        font-size: 12px;
        
        &.normal {
          color: #67c23a;
        }
        
        &.error {
          color: #f56c6c;
        }
        
        &.maintenance {
          color: #e6a23c;
        }
      }
    }
    
    .test-time {
      display: flex;
      align-items: center;
      color: #909399;
      font-size: 12px;
      
      i {
        margin-right: 4px;
      }
    }
    
    .pagination-wrapper {
      margin-top: 20px;
      text-align: right;
    }
  }
}

// 全局样式
::v-deep .el-table {
  .disabled-row {
    background-color: #f5f7fa;
    color: #c0c4cc;
  }
  
  .error-row {
    background-color: #fef0f0;
  }
  
  .el-table__row:hover {
    background-color: #f5f7fa;
  }
}

::v-deep .el-card__body {
  padding: 20px;
}

::v-deep .el-form--inline .el-form-item {
  margin-right: 20px;
  margin-bottom: 0;
}

::v-deep .el-button-group .el-button {
  margin-left: 0;
}
</style>
