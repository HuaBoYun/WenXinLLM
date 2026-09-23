<template>
  <div class="data-source-config-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-coin"></i>
            数据源配置管理
          </h2>
          <p class="page-description">管理系统数据源配置，包括数据库连接、API接口、文件数据源等配置信息</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            新增数据源
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

    <!-- 数据源统计卡片 -->
    <div class="datasource-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-coin"></i>
              </div>
              <div class="card-info">
                <div class="card-title">总数据源</div>
                <div class="card-value">{{ totalDataSources }}</div>
                <div class="card-change">已配置数据源</div>
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
                <div class="card-value">{{ activeDataSources }}</div>
                <div class="card-change positive">连接正常</div>
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
                <div class="card-title">连接异常</div>
                <div class="card-value">{{ errorDataSources }}</div>
                <div class="card-change negative">需要处理</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon test-icon">
                <i class="el-icon-time"></i>
              </div>
              <div class="card-info">
                <div class="card-title">最后检查</div>
                <div class="card-value">{{ lastCheckTime }}</div>
                <div class="card-change">连接检查</div>
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
          <el-form-item label="数据源编码">
            <el-input
              v-model="listQuery.dataSourceCode"
              placeholder="请输入数据源编码"
              style="width: 150px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="数据源名称">
            <el-input
              v-model="listQuery.dataSourceName"
              placeholder="请输入数据源名称"
              style="width: 200px;"
              clearable
            />
          </el-form-item>
          <el-form-item label="数据源类型">
            <el-select
              v-model="listQuery.dataSourceType"
              placeholder="请选择数据源类型"
              clearable
              style="width: 150px;"
            >
              <el-option label="MySQL数据库" value="MYSQL" />
              <el-option label="Oracle数据库" value="ORACLE" />
              <el-option label="达梦数据库" value="DAMENG" />
              <el-option label="PostgreSQL" value="POSTGRESQL" />
              <el-option label="API接口" value="API" />
              <el-option label="文件数据源" value="FILE" />
              <el-option label="Redis缓存" value="REDIS" />
            </el-select>
          </el-form-item>
          <el-form-item label="环境类型">
            <el-select
              v-model="listQuery.environment"
              placeholder="请选择环境类型"
              clearable
              style="width: 120px;"
            >
              <el-option label="开发环境" value="DEV" />
              <el-option label="测试环境" value="TEST" />
              <el-option label="生产环境" value="PROD" />
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
              <el-option label="未测试" value="UNTESTED" />
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

    <el-table
      :key="tableKey"
      v-loading="listLoading"
      :data="list"
      border
      fit
      highlight-current-row
      style="width: 100%;"
    >
      <el-table-column label="数据源编码" prop="dataSourceCode" sortable="custom" align="center" width="150">
        <template slot-scope="{row}">
          <span>{{ row.dataSourceCode }}</span>
        </template>
      </el-table-column>
      <el-table-column label="数据源名称" width="200px" align="center" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.dataSourceName }}</span>
        </template>
      </el-table-column>
      <el-table-column label="数据源类型" width="120px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getDataSourceTypeColor(row.dataSourceType)" size="small">
            {{ getDataSourceTypeName(row.dataSourceType) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="环境类型" width="100px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getEnvironmentColor(row.environment)" size="small">
            {{ getEnvironmentName(row.environment) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="连接地址" min-width="250px" show-overflow-tooltip>
        <template slot-scope="{row}">
          <span>{{ row.connectionUrl }}</span>
        </template>
      </el-table-column>
      <el-table-column label="连接状态" width="100px" align="center">
        <template slot-scope="{row}">
          <el-tag :type="getConnectionStatusColor(row.connectionStatus)">
            {{ getConnectionStatusName(row.connectionStatus) }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="最后测试时间" width="180px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.lastTestTime | parseTime('{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" width="180px" align="center">
        <template slot-scope="{row}">
          <span>{{ row.createTime | parseTime('{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" class-name="status-col" width="100">
        <template slot-scope="{row}">
          <el-tag :type="row.isEnabled === 1 ? 'success' : 'danger'">
            {{ row.isEnabled === 1 ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="230" class-name="small-padding fixed-width">
        <template slot-scope="{row,$index}">
          <el-button type="primary" size="mini" @click="handleUpdate(row)">
            编辑
          </el-button>
          <el-button v-if="row.status!='deleted'" size="mini" type="danger" @click="handleDelete(row,$index)">
            删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />
  </div>
</template>

<script>
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'

export default {
  name: 'DataSourceConfigManage',
  components: { Pagination },
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
      listQuery: {
        page: 1,
        limit: 20,
        dataSourceCode: undefined,
        dataSourceName: undefined,
        dataSourceType: undefined,
        environment: undefined,
        connectionStatus: undefined
      },
      totalDataSources: 0,
      enabledDataSources: 0,
      normalConnections: 0,
      errorConnections: 0
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getDataSourceTypeName(type) {
      const typeMap = {
        'MYSQL': 'MySQL',
        'ORACLE': 'Oracle',
        'DAMENG': '达梦',
        'POSTGRESQL': 'PostgreSQL',
        'API': 'API接口',
        'FILE': '文件数据源',
        'REDIS': 'Redis缓存'
      }
      return typeMap[type] || type
    },
    getDataSourceTypeColor(type) {
      const colorMap = {
        'MYSQL': 'primary',
        'ORACLE': 'warning',
        'DAMENG': 'success',
        'POSTGRESQL': 'info',
        'API': 'danger',
        'FILE': 'default',
        'REDIS': 'primary'
      }
      return colorMap[type] || 'default'
    },
    getEnvironmentName(env) {
      const envMap = {
        'DEV': '开发环境',
        'TEST': '测试环境',
        'PROD': '生产环境'
      }
      return envMap[env] || env
    },
    getEnvironmentColor(env) {
      const colorMap = {
        'DEV': 'info',
        'TEST': 'warning',
        'PROD': 'danger'
      }
      return colorMap[env] || 'default'
    },
    getConnectionStatusName(status) {
      const statusMap = {
        'NORMAL': '正常',
        'ERROR': '异常',
        'UNTESTED': '未测试'
      }
      return statusMap[status] || status
    },
    getConnectionStatusColor(status) {
      const colorMap = {
        'NORMAL': 'success',
        'ERROR': 'danger',
        'UNTESTED': 'info'
      }
      return colorMap[status] || 'default'
    },
    getList() {
      this.listLoading = true
      setTimeout(() => {
        this.list = [
          {
            dataSourceId: 1,
            dataSourceCode: 'DS_MYSQL_MAIN',
            dataSourceName: '主数据库MySQL',
            dataSourceType: 'MYSQL',
            environment: 'PROD',
            connectionUrl: 'jdbc:mysql://192.0.2.200:3306/hbyun_treasury',
            username: 'treasury_user',
            connectionStatus: 'NORMAL',
            isEnabled: 1,
            lastTestTime: '2024-09-25 15:30:00',
            createTime: '2024-01-01 10:00:00',
            description: '财资系统主数据库连接'
          },
          {
            dataSourceId: 2,
            dataSourceCode: 'DS_DAMENG_BACKUP',
            dataSourceName: '备份数据库达梦',
            dataSourceType: 'DAMENG',
            environment: 'PROD',
            connectionUrl: 'jdbc:dm://192.0.2.200:5236/HBYUN_BACKUP',
            username: 'backup_user',
            connectionStatus: 'NORMAL',
            isEnabled: 1,
            lastTestTime: '2024-09-25 14:00:00',
            createTime: '2024-01-15 14:30:00',
            description: '财资系统备份数据库连接'
          },
          {
            dataSourceId: 3,
            dataSourceCode: 'DS_API_BANK',
            dataSourceName: '银行接口API',
            dataSourceType: 'API',
            environment: 'PROD',
            connectionUrl: 'https://api.bank.com/treasury/v1',
            username: 'api_user',
            connectionStatus: 'ERROR',
            isEnabled: 0,
            lastTestTime: '2024-09-25 12:00:00',
            createTime: '2024-02-01 09:00:00',
            description: '银行系统API接口连接'
          }
        ]
        this.total = 3
        this.listLoading = false
      }, 1000)
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleCreate() {
      this.$message.info('创建功能待实现')
    },
    handleUpdate(row) {
      this.$message.info('编辑功能待实现')
    },
    handleDelete(row, index) {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.list.splice(index, 1)
        this.$notify({
          title: '成功',
          message: '删除成功',
          type: 'success',
          duration: 2000
        })
      })
    }
  }
}
</script>
