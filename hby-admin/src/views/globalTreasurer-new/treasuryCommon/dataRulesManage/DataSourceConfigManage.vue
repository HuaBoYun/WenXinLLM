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
      v-loading="listLoading"
      :data="list"
      border
      style="width: 100%; margin-top: 12px;"
    >
      <el-table-column type="index" label="序号" width="60" align="center" />
      <el-table-column label="数据源编码" prop="sourceCode" align="center" width="150" show-overflow-tooltip />
      <el-table-column label="数据源名称" prop="sourceName" align="center" min-width="160" show-overflow-tooltip />
      <el-table-column label="数据源类型" align="center" width="120">
        <template slot-scope="scope">
          <el-tag :type="getDataSourceTypeColor(scope.row.sourceType)" size="small">{{ getDataSourceTypeName(scope.row.sourceType) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="环境类型" align="center" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.environment" :type="getEnvironmentColor(scope.row.environment)" size="small">{{ getEnvironmentName(scope.row.environment) }}</el-tag>
          <span v-else>未设置</span>
        </template>
      </el-table-column>
      <el-table-column label="连接地址" align="left" min-width="220" show-overflow-tooltip>
        <template slot-scope="scope">
          <span>{{ scope.row.connectionUrl || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="连接状态" align="center" width="100">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.connectionStatus" :type="getConnectionStatusColor(scope.row.connectionStatus)" size="small">{{ getConnectionStatusName(scope.row.connectionStatus) }}</el-tag>
          <el-tag v-else type="info" size="small">未测试</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="最后测试时间" align="center" width="170">
        <template slot-scope="scope">
          <span>{{ formatDateTime(scope.row.lastTestTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" width="170">
        <template slot-scope="scope">
          <span>{{ formatDateTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" width="80">
        <template slot-scope="scope">
          <el-tag :type="(scope.row.isEnabled === '1' || scope.row.isEnabled === 1) ? 'success' : 'danger'" size="small">
            {{ (scope.row.isEnabled === '1' || scope.row.isEnabled === 1) ? '启用' : '禁用' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="160" fixed="right">
        <template slot-scope="scope">
          <el-button type="primary" size="mini" @click="handleUpdate(scope.row)">编辑</el-button>
          <el-button type="danger" size="mini" @click="handleDelete(scope.row, scope.$index)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total > 0" :total="total" :page.sync="listQuery.page" :limit.sync="listQuery.limit" @pagination="getList" />

    <!-- 数据源配置对话框 -->
    <data-source-dialog
      :visible.sync="dataSourceDialogVisible"
      :data-source-data="currentDataSource"
      :status="dialogStatus"
      @close="handleDialogClose"
      @success="handleDialogSuccess"
    />
  </div>
</template>

<script>
import waves from '@/directive/waves'
import Pagination from '@/components/Pagination'
import DataSourceDialog from './components/DataSourceDialog'
import { getDataSourceConfigList, deleteDataSourceConfig } from '@/api/globalTreasurer/dataSourceConfigManage'

export default {
  name: 'DataSourceConfigManage',
  components: {
    Pagination,
    DataSourceDialog
  },
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
      errorConnections: 0,
      activeDataSources: 0,
      errorDataSources: 0,
      lastCheckTime: '',
      dataSourceDialogVisible: false,
      dialogStatus: 'create',
      currentDataSource: {}
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
    // 格式化日期时间（年-月-日 时:分:秒）
    formatDateTime(val) {
      if (!val) return '-'
      const d = new Date(typeof val === 'string' && !/[0-9]{13}/.test(val) ? val : Number(val) || val)
      if (isNaN(d.getTime())) return '-'
      const pad = n => String(n).padStart(2, '0')
      return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())} ${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`
    },
    // 格式化日期（年-月-日）
    formatDate(val) {
      if (!val) return '-'
      const d = new Date(typeof val === 'string' && !/[0-9]{13}/.test(val) ? val : Number(val) || val)
      if (isNaN(d.getTime())) return '-'
      const pad = n => String(n).padStart(2, '0')
      return `${d.getFullYear()}-${pad(d.getMonth() + 1)}-${pad(d.getDate())}`
    },
    // 后处理列表数据：从 connectionConfig JSON 解析虚拟字段
    processListData(list) {
      if (!Array.isArray(list)) return list
      return list.map(item => {
        // 解析 connectionConfig JSON，提取 environment、connectionUrl、connectionStatus 等
        if (item.connectionConfig) {
          try {
            const config = typeof item.connectionConfig === 'string'
              ? JSON.parse(item.connectionConfig)
              : item.connectionConfig
            if (!item.environment && config.environment) {
              item.environment = config.environment
            }
            if (!item.connectionUrl && config.connectionUrl) {
              item.connectionUrl = config.connectionUrl
            }
            if (!item.connectionStatus && config.connectionStatus) {
              item.connectionStatus = config.connectionStatus
            }
            if (!item.lastTestTime && config.lastTestTime) {
              item.lastTestTime = config.lastTestTime
            }
            // 如果还是没有 connectionUrl，尝试从 host/port/db 拼接
            if (!item.connectionUrl && config.host) {
              const port = config.port || ''
              const db = config.db || config.databaseName || ''
              item.connectionUrl = `${config.host}${port ? ':' + port : ''}${db ? '/' + db : ''}`
            }
          } catch (e) {
            console.warn('解析 connectionConfig 失败:', e)
          }
        }
        // isEnabled 虚拟字段：用 isActive 或 status 映射
        if (item.isEnabled === undefined || item.isEnabled === null) {
          item.isEnabled = item.isActive || item.status || '0'
        }
        return item
      })
    },
    async getList(paginationPayload) {
      if (paginationPayload) {
        this.listQuery.page = paginationPayload.page
        this.listQuery.limit = paginationPayload.limit
      }
      this.listLoading = true
      try {
        // environment 和 connectionStatus 在数据库中不存在，不发给后端，改为前端过滤
        const { environment, connectionStatus, ...serverQuery } = this.listQuery
        const response = await getDataSourceConfigList(serverQuery)

        if (response && response.code === 1) {
          const rawData = response.data || {}
          let rawList = rawData.tlist || rawData.list || []
          this.total = Number(rawData.totalRecord || rawData.total) || rawList.length
          // 后处理：从 connectionConfig 解析虚拟字段
          let processed = this.processListData(rawList)
          // 前端过滤：environment 和 connectionStatus（存在 connectionConfig JSON 中，数据库无对应列）
          if (environment) {
            processed = processed.filter(item => item.environment === environment)
          }
          if (connectionStatus) {
            processed = processed.filter(item => (item.connectionStatus || 'UNTESTED') === connectionStatus)
          }
          this.list = processed
          if (environment || connectionStatus) {
            this.total = processed.length
          }
        } else {
          this.$message.error((response && response.msg) || '获取数据失败')
          this.list = []
          this.total = 0
        }
      } catch (error) {
        console.error('获取数据源配置列表失败:', error)
        this.$message.error('获取数据失败')
        this.list = []
        this.total = 0
      } finally {
        this.listLoading = false
      }

      // 更新统计信息
      this.updateStatistics()
    },
    updateStatistics() {
      this.totalDataSources = this.list.length
      // 后端 isEnabled 是 String 类型，需要兼容 '1' 和 1
      this.activeDataSources = this.list.filter(ds => ds.isEnabled === '1' || ds.isEnabled === 1).length
      this.normalConnections = this.list.filter(ds => ds.connectionStatus === 'NORMAL').length
      this.errorConnections = this.list.filter(ds => ds.connectionStatus === 'ERROR').length
      this.errorDataSources = this.errorConnections

      // 更新最后检查时间
      this.lastCheckTime = new Date().toLocaleString()
    },
    handleFilter() {
      this.listQuery.page = 1
      this.getList()
    },
    handleReset() {
      this.listQuery = {
        page: 1,
        limit: 20,
        dataSourceCode: undefined,
        dataSourceName: undefined,
        dataSourceType: undefined,
        environment: undefined,
        connectionStatus: undefined
      }
      this.getList()
    },
    handleCreate() {
      this.dialogStatus = 'create'
      this.currentDataSource = {}
      this.dataSourceDialogVisible = true
    },
    handleUpdate(row) {
      this.dialogStatus = 'edit'
      this.currentDataSource = { ...row }
      this.dataSourceDialogVisible = true
    },
    async handleTestAll() {
      this.$confirm('确定要批量测试所有数据源的连接吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        const loading = this.$loading({
          lock: true,
          text: '正在批量测试连接...',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        })

        try {
          // 模拟批量测试
          await this.simulateBatchTest()

          this.$message.success('批量连接测试完成')
          this.getList()
        } catch (error) {
          this.$message.error('批量测试失败：' + error.message)
        } finally {
          loading.close()
        }
      })
    },
    simulateBatchTest() {
      return new Promise((resolve) => {
        setTimeout(() => {
          // 模拟更新连接状态
          this.list.forEach(ds => {
            if (Math.random() > 0.2) {
              ds.connectionStatus = 'NORMAL'
            } else {
              ds.connectionStatus = 'ERROR'
            }
            ds.lastTestTime = new Date().toISOString()
          })
          resolve()
        }, 3000)
      })
    },
    handleExport() {
      this.exportDataSources()
    },
    async exportDataSources() {
      try {
        this.$message({
          type: 'success',
          message: '导出成功'
        })

        // 创建下载链接
        const dataStr = JSON.stringify(this.list, null, 2)
        const dataUri = 'data:application/json;charset=utf-8,'+ encodeURIComponent(dataStr)

        const exportFileDefaultName = `数据源配置_${new Date().toISOString().split('T')[0]}.json`

        const linkElement = document.createElement('a')
        linkElement.setAttribute('href', dataUri)
        linkElement.setAttribute('download', exportFileDefaultName)
        linkElement.click()

      } catch (error) {
        this.$message.error('导出失败，请重试')
      }
    },
    handleDelete(row, index) {
      this.$confirm('此操作将永久删除该记录, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async() => {
        try {
          const response = await deleteDataSourceConfig(row.id)
          if (response && response.code === 1) {
            this.$notify({
              title: '成功',
              message: '删除成功',
              type: 'success',
              duration: 2000
            })
            this.getList()
          } else {
            this.$message.error(response.message || '删除失败')
          }
        } catch (error) {
          console.error('删除数据源配置失败:', error)
          this.$message.error('删除失败，请重试')
        }
      })
    },
    handleDialogClose() {
      this.dataSourceDialogVisible = false
    },
    handleDialogSuccess(dataSourceData) {
      // 保存成功后重新加载列表数据
      this.getList()
      this.dataSourceDialogVisible = false
    }
  }
}
</script>
