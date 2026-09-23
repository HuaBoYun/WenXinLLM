<template>
  <div class="system-config-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h1 class="page-title">
            <i class="el-icon-connection"></i>
            ERP系统集成配置
          </h1>
          <p class="page-description">管理和配置各种ERP系统的集成连接，支持数据库、API、文件等多种连接方式</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="showCreateDialog">
            新建配置
          </el-button>
          <el-button type="success" icon="el-icon-refresh" @click="refreshData">
            刷新数据
          </el-button>
        </div>
      </div>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-cards">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon total">
              <i class="el-icon-setting"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ statistics.totalConfigs || 0 }}</div>
              <div class="stat-label">配置总数</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon active">
              <i class="el-icon-check"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ statistics.activeConfigs || 0 }}</div>
              <div class="stat-label">启用配置</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon connected">
              <i class="el-icon-link"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ statistics.connectedConfigs || 0 }}</div>
              <div class="stat-label">已连接</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon healthy">
              <i class="el-icon-success"></i>
            </div>
            <div class="stat-content">
              <div class="stat-number">{{ statistics.healthyConfigs || 0 }}</div>
              <div class="stat-label">健康状态</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 功能导航 -->
    <div class="function-nav">
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="nav-card" @click="$router.push('/management-accountant/intg/system-config/list')">
            <div class="nav-icon">
              <i class="el-icon-menu"></i>
            </div>
            <div class="nav-content">
              <h3>配置列表</h3>
              <p>查看和管理所有系统配置</p>
            </div>
            <div class="nav-arrow">
              <i class="el-icon-arrow-right"></i>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="nav-card" @click="$router.push('/management-accountant/intg/system-config/dashboard')">
            <div class="nav-icon">
              <i class="el-icon-data-analysis"></i>
            </div>
            <div class="nav-content">
              <h3>配置仪表板</h3>
              <p>查看配置统计和分析报告</p>
            </div>
            <div class="nav-arrow">
              <i class="el-icon-arrow-right"></i>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="nav-card" @click="$router.push('/management-accountant/intg/system-config/monitor')">
            <div class="nav-icon">
              <i class="el-icon-monitor"></i>
            </div>
            <div class="nav-content">
              <h3>配置监控</h3>
              <p>实时监控配置状态和健康度</p>
            </div>
            <div class="nav-arrow">
              <i class="el-icon-arrow-right"></i>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 最近活动 -->
    <div class="recent-activity">
      <div class="section-header">
        <h2>最近活动</h2>
        <el-button type="text" @click="$router.push('/management-accountant/intg/system-config/list')">
          查看全部
        </el-button>
      </div>
      <el-table :data="recentConfigs" style="width: 100%" v-loading="loading">
        <el-table-column prop="configName" label="配置名称" min-width="150">
          <template slot-scope="scope">
            <el-link type="primary" @click="viewDetail(scope.row.configId)">
              {{ scope.row.configName }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column prop="systemType" label="系统类型" width="120">
          <template slot-scope="scope">
            <el-tag size="small">{{ formatSystemTypeName(scope.row.systemType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="connectionType" label="连接类型" width="120">
          <template slot-scope="scope">
            <el-tag type="info" size="small">{{ formatConnectionTypeName(scope.row.connectionType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="configStatus" label="配置状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.configStatus)" size="small">
              {{ formatStatusName(scope.row.configStatus, 'config') }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="connectionStatus" label="连接状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getConnectionStatusType(scope.row.connectionStatus)" size="small">
              {{ formatStatusName(scope.row.connectionStatus, 'connection') }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="healthStatus" label="健康状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getHealthStatusType(scope.row.healthStatus)" size="small">
              {{ formatStatusName(scope.row.healthStatus, 'health') }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="updatedTime" label="更新时间" width="160">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.updatedTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="testConnection(scope.row)">
              测试连接
            </el-button>
            <el-button size="mini" type="success" @click="performHealthCheck(scope.row)">
              健康检查
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 创建配置对话框 -->
    <el-dialog
      title="创建系统配置"
      :visible.sync="createDialogVisible"
      width="600px"
      :close-on-click-modal="false"
    >
      <el-form :model="createForm" :rules="createRules" ref="createForm" label-width="120px">
        <el-form-item label="配置名称" prop="configName">
          <el-input v-model="createForm.configName" placeholder="请输入配置名称"></el-input>
        </el-form-item>
        <el-form-item label="系统类型" prop="systemType">
          <el-select v-model="createForm.systemType" placeholder="请选择系统类型" style="width: 100%">
            <el-option
              v-for="(label, value) in systemTypeOptions"
              :key="value"
              :label="label"
              :value="value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="连接类型" prop="connectionType">
          <el-select v-model="createForm.connectionType" placeholder="请选择连接类型" style="width: 100%">
            <el-option
              v-for="(label, value) in connectionTypeOptions"
              :key="value"
              :label="label"
              :value="value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="连接地址" prop="connectionUrl">
          <el-input v-model="createForm.connectionUrl" placeholder="请输入连接地址"></el-input>
        </el-form-item>
        <el-form-item label="端口" prop="connectionPort">
          <el-input-number v-model="createForm.connectionPort" :min="1" :max="65535" style="width: 100%"></el-input-number>
        </el-form-item>
        <el-form-item label="用户名" prop="username">
          <el-input v-model="createForm.username" placeholder="请输入用户名"></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="createForm.password" type="password" placeholder="请输入密码" show-password></el-input>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="createForm.configDescription" type="textarea" :rows="3" placeholder="请输入配置描述"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="createDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleCreate" :loading="createLoading">确定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getSystemConfigPage,
  createSystemConfig,
  testConnection,
  performHealthCheck,
  countSystemConfigs,
  countByConfigStatus,
  countByConnectionStatus,
  countByHealthStatus,
  formatSystemTypeName,
  formatConnectionTypeName,
  formatStatusName,
  SYSTEM_TYPES,
  CONNECTION_TYPES
} from '@/api/managementAccountant/intg/systemConfig'

export default {
  name: 'IntgSystemConfig',
  data() {
    return {
      loading: false,
      createLoading: false,
      createDialogVisible: false,
      statistics: {
        totalConfigs: 0,
        activeConfigs: 0,
        connectedConfigs: 0,
        healthyConfigs: 0
      },
      recentConfigs: [],
      createForm: {
        configName: '',
        systemType: '',
        connectionType: '',
        connectionUrl: '',
        connectionPort: null,
        username: '',
        password: '',
        configDescription: ''
      },
      createRules: {
        configName: [
          { required: true, message: '请输入配置名称', trigger: 'blur' }
        ],
        systemType: [
          { required: true, message: '请选择系统类型', trigger: 'change' }
        ],
        connectionType: [
          { required: true, message: '请选择连接类型', trigger: 'change' }
        ],
        connectionUrl: [
          { required: true, message: '请输入连接地址', trigger: 'blur' }
        ]
      },
      systemTypeOptions: {},
      connectionTypeOptions: {}
    }
  },
  created() {
    this.initOptions()
    this.loadData()
  },
  methods: {
    initOptions() {
      // 初始化系统类型选项
      Object.keys(SYSTEM_TYPES).forEach(key => {
        this.systemTypeOptions[SYSTEM_TYPES[key]] = formatSystemTypeName(SYSTEM_TYPES[key])
      })
      
      // 初始化连接类型选项
      Object.keys(CONNECTION_TYPES).forEach(key => {
        this.connectionTypeOptions[CONNECTION_TYPES[key]] = formatConnectionTypeName(CONNECTION_TYPES[key])
      })
    },
    
    async loadData() {
      this.loading = true
      try {
        await Promise.all([
          this.loadStatistics(),
          this.loadRecentConfigs()
        ])
      } catch (error) {
        console.error('加载数据失败:', error)
        this.$message.error('加载数据失败')
      } finally {
        this.loading = false
      }
    },
    
    async loadStatistics() {
      try {
        const [totalResult, statusResult, connectionResult, healthResult] = await Promise.all([
          countSystemConfigs(),
          countByConfigStatus(),
          countByConnectionStatus(),
          countByHealthStatus()
        ])
        
        this.statistics.totalConfigs = totalResult.data || 0
        
        // 处理配置状态统计
        const statusStats = statusResult.data || []
        this.statistics.activeConfigs = statusStats.find(item => item.configStatus === 'ACTIVE')?.count || 0
        
        // 处理连接状态统计
        const connectionStats = connectionResult.data || []
        this.statistics.connectedConfigs = connectionStats.find(item => item.connectionStatus === 'CONNECTED')?.count || 0
        
        // 处理健康状态统计
        const healthStats = healthResult.data || []
        this.statistics.healthyConfigs = healthStats.find(item => item.healthStatus === 'HEALTHY')?.count || 0
      } catch (error) {
        console.error('加载统计数据失败:', error)
      }
    },
    
    async loadRecentConfigs() {
      try {
        const response = await getSystemConfigPage({
          current: 1,
          size: 10,
          orderBy: 'updated_time',
          orderDirection: 'DESC'
        })
        this.recentConfigs = response.data?.records || []
      } catch (error) {
        console.error('加载最近配置失败:', error)
      }
    },
    
    refreshData() {
      this.loadData()
      this.$message.success('数据已刷新')
    },
    
    showCreateDialog() {
      this.createDialogVisible = true
      this.$nextTick(() => {
        this.$refs.createForm.resetFields()
      })
    },
    
    async handleCreate() {
      try {
        await this.$refs.createForm.validate()
        this.createLoading = true
        
        await createSystemConfig(this.createForm)
        
        this.$message.success('配置创建成功')
        this.createDialogVisible = false
        this.loadData()
      } catch (error) {
        console.error('创建配置失败:', error)
        this.$message.error('创建配置失败')
      } finally {
        this.createLoading = false
      }
    },
    
    viewDetail(configId) {
      this.$router.push(`/management-accountant/intg/system-config/detail/${configId}`)
    },
    
    async testConnection(config) {
      try {
        this.$message.info('正在测试连接...')
        const response = await testConnection(config.configId)
        
        if (response.success) {
          this.$message.success('连接测试成功')
        } else {
          this.$message.error('连接测试失败')
        }
        
        this.loadRecentConfigs()
      } catch (error) {
        console.error('连接测试失败:', error)
        this.$message.error('连接测试失败')
      }
    },
    
    async performHealthCheck(config) {
      try {
        this.$message.info('正在执行健康检查...')
        const response = await performHealthCheck(config.configId)
        
        if (response.success) {
          this.$message.success('健康检查完成')
        } else {
          this.$message.error('健康检查失败')
        }
        
        this.loadRecentConfigs()
      } catch (error) {
        console.error('健康检查失败:', error)
        this.$message.error('健康检查失败')
      }
    },
    
    formatSystemTypeName,
    formatConnectionTypeName,
    formatStatusName,
    
    getStatusType(status) {
      const typeMap = {
        'ACTIVE': 'success',
        'INACTIVE': 'info',
        'TESTING': 'warning',
        'ERROR': 'danger',
        'MAINTENANCE': 'warning'
      }
      return typeMap[status] || 'info'
    },
    
    getConnectionStatusType(status) {
      const typeMap = {
        'CONNECTED': 'success',
        'DISCONNECTED': 'info',
        'CONNECTING': 'warning',
        'ERROR': 'danger',
        'TIMEOUT': 'warning'
      }
      return typeMap[status] || 'info'
    },
    
    getHealthStatusType(status) {
      const typeMap = {
        'HEALTHY': 'success',
        'UNHEALTHY': 'danger',
        'WARNING': 'warning',
        'CRITICAL': 'danger',
        'UNKNOWN': 'info'
      }
      return typeMap[status] || 'info'
    },
    
    formatDateTime(dateTime) {
      if (!dateTime) return '-'
      return new Date(dateTime).toLocaleString('zh-CN')
    }
  }
}
</script>

<style lang="scss" scoped>
.system-config-container {
  padding: 20px;
  background-color: #f5f5f5;
  min-height: calc(100vh - 84px);
}

.page-header {
  background: white;
  border-radius: 8px;
  padding: 24px;
  margin-bottom: 20px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  
  .header-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }
  
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

.stats-cards {
  margin-bottom: 20px;
  
  .stat-card {
    background: white;
    border-radius: 8px;
    padding: 20px;
    display: flex;
    align-items: center;
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    
    .stat-icon {
      width: 48px;
      height: 48px;
      border-radius: 8px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 16px;
      
      i {
        font-size: 24px;
        color: white;
      }
      
      &.total {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }
      
      &.active {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      }
      
      &.connected {
        background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
      }
      
      &.healthy {
        background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
      }
    }
    
    .stat-content {
      .stat-number {
        font-size: 28px;
        font-weight: 600;
        color: #303133;
        line-height: 1;
        margin-bottom: 4px;
      }
      
      .stat-label {
        font-size: 14px;
        color: #909399;
      }
    }
  }
}

.function-nav {
  margin-bottom: 20px;
  
  .nav-card {
    background: white;
    border-radius: 8px;
    padding: 20px;
    display: flex;
    align-items: center;
    cursor: pointer;
    transition: all 0.3s;
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    
    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 12px rgba(0,0,0,0.15);
    }
    
    .nav-icon {
      width: 48px;
      height: 48px;
      background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      border-radius: 8px;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 16px;
      
      i {
        font-size: 24px;
        color: white;
      }
    }
    
    .nav-content {
      flex: 1;
      
      h3 {
        margin: 0 0 4px 0;
        font-size: 16px;
        font-weight: 600;
        color: #303133;
      }
      
      p {
        margin: 0;
        font-size: 14px;
        color: #909399;
      }
    }
    
    .nav-arrow {
      color: #C0C4CC;
      font-size: 16px;
    }
  }
}

.recent-activity {
  background: white;
  border-radius: 8px;
  padding: 24px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  
  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    
    h2 {
      margin: 0;
      font-size: 18px;
      font-weight: 600;
      color: #303133;
    }
  }
}

.dialog-footer {
  text-align: right;
}
</style>
