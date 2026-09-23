<template>
  <div class="api-management-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="title-section">
          <h1 class="page-title">
            <i class="el-icon-connection"></i>
            API接口管理
          </h1>
          <p class="page-description">统一管理和监控系统中的所有API接口，提供完整的接口生命周期管理</p>
        </div>
        <div class="action-section">
          <el-button type="primary" icon="el-icon-plus" @click="showCreateDialog">
            创建API
          </el-button>
          <el-button icon="el-icon-refresh" @click="refreshData">
            刷新数据
          </el-button>
        </div>
      </div>
    </div>

    <!-- 统计概览 -->
    <div class="overview-section">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon api-total">
              <i class="el-icon-connection"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ overviewData.totalApis || 0 }}</div>
              <div class="stat-label">API总数</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon api-active">
              <i class="el-icon-check"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ overviewData.activeApis || 0 }}</div>
              <div class="stat-label">激活API</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon api-testing">
              <i class="el-icon-loading"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ overviewData.testingApis || 0 }}</div>
              <div class="stat-label">测试中API</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-icon api-deprecated">
              <i class="el-icon-warning"></i>
            </div>
            <div class="stat-content">
              <div class="stat-value">{{ overviewData.deprecatedApis || 0 }}</div>
              <div class="stat-label">已废弃API</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 功能导航 -->
    <div class="navigation-section">
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="nav-card" @click="navigateToList">
            <div class="nav-icon">
              <i class="el-icon-menu"></i>
            </div>
            <div class="nav-content">
              <h3>API列表管理</h3>
              <p>查看和管理所有API接口，支持批量操作和高级搜索</p>
            </div>
            <div class="nav-arrow">
              <i class="el-icon-arrow-right"></i>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="nav-card" @click="navigateToDashboard">
            <div class="nav-icon">
              <i class="el-icon-data-analysis"></i>
            </div>
            <div class="nav-content">
              <h3>数据分析仪表板</h3>
              <p>API使用统计、性能分析和趋势监控</p>
            </div>
            <div class="nav-arrow">
              <i class="el-icon-arrow-right"></i>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="nav-card" @click="navigateToTest">
            <div class="nav-icon">
              <i class="el-icon-cpu"></i>
            </div>
            <div class="nav-content">
              <h3>API测试工具</h3>
              <p>在线测试API功能，验证接口可用性和性能</p>
            </div>
            <div class="nav-arrow">
              <i class="el-icon-arrow-right"></i>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 最近活动 -->
    <div class="activity-section">
      <div class="section-header">
        <h2>最近活动</h2>
        <el-button type="text" @click="viewAllActivities">查看全部</el-button>
      </div>
      <div class="activity-list">
        <div v-for="activity in recentActivities" :key="activity.id" class="activity-item">
          <div class="activity-icon" :class="getActivityIconClass(activity.type)">
            <i :class="getActivityIcon(activity.type)"></i>
          </div>
          <div class="activity-content">
            <div class="activity-title">{{ activity.title }}</div>
            <div class="activity-description">{{ activity.description }}</div>
            <div class="activity-time">{{ formatTime(activity.time) }}</div>
          </div>
        </div>
      </div>
    </div>

    <!-- 快速访问 -->
    <div class="quick-access-section">
      <div class="section-header">
        <h2>快速访问</h2>
      </div>
      <el-row :gutter="15">
        <el-col :span="4">
          <div class="quick-item" @click="quickCreateRestApi">
            <i class="el-icon-plus"></i>
            <span>创建REST API</span>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="quick-item" @click="quickImportApi">
            <i class="el-icon-upload"></i>
            <span>导入API</span>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="quick-item" @click="quickExportApi">
            <i class="el-icon-download"></i>
            <span>导出API</span>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="quick-item" @click="quickTestApi">
            <i class="el-icon-cpu"></i>
            <span>测试API</span>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="quick-item" @click="quickViewDocs">
            <i class="el-icon-document"></i>
            <span>查看文档</span>
          </div>
        </el-col>
        <el-col :span="4">
          <div class="quick-item" @click="quickViewStats">
            <i class="el-icon-pie-chart"></i>
            <span>查看统计</span>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 创建API对话框 -->
    <el-dialog
      title="创建API"
      :visible.sync="createDialogVisible"
      width="600px"
      :before-close="handleCreateDialogClose"
    >
      <el-form ref="createForm" :model="createForm" :rules="createRules" label-width="120px">
        <el-form-item label="API名称" prop="apiName">
          <el-input v-model="createForm.apiName" placeholder="请输入API名称"></el-input>
        </el-form-item>
        <el-form-item label="API类型" prop="apiType">
          <el-select v-model="createForm.apiType" placeholder="请选择API类型" style="width: 100%">
            <el-option
              v-for="option in apiTypeOptions"
              :key="option.value"
              :label="option.label"
              :value="option.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="接口路径" prop="endpointPath">
          <el-input v-model="createForm.endpointPath" placeholder="请输入接口路径，如：/api/users"></el-input>
        </el-form-item>
        <el-form-item label="HTTP方法" prop="httpMethod">
          <el-select v-model="createForm.httpMethod" placeholder="请选择HTTP方法" style="width: 100%">
            <el-option
              v-for="option in httpMethodOptions"
              :key="option.value"
              :label="option.label"
              :value="option.value"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="API描述" prop="apiDescription">
          <el-input
            v-model="createForm.apiDescription"
            type="textarea"
            :rows="3"
            placeholder="请输入API描述"
          ></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="createDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleCreateApi" :loading="createLoading">创建</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  getSystemOverview,
  createApiManagement,
  getApiManagementOptions
} from '@/api/managementAccountant/intg/apiManagement'

export default {
  name: 'ApiManagementIndex',
  data() {
    return {
      // 概览数据
      overviewData: {},
      
      // 最近活动
      recentActivities: [
        {
          id: 1,
          type: 'create',
          title: '创建了新的API接口',
          description: 'REST API: /api/users/profile',
          time: new Date(Date.now() - 1000 * 60 * 30) // 30分钟前
        },
        {
          id: 2,
          type: 'update',
          title: '更新了API配置',
          description: 'GraphQL API: /api/graphql',
          time: new Date(Date.now() - 1000 * 60 * 60 * 2) // 2小时前
        },
        {
          id: 3,
          type: 'test',
          title: '执行了API测试',
          description: 'SOAP API: /api/soap/service',
          time: new Date(Date.now() - 1000 * 60 * 60 * 4) // 4小时前
        }
      ],
      
      // 创建对话框
      createDialogVisible: false,
      createLoading: false,
      createForm: {
        apiName: '',
        apiType: 'REST',
        endpointPath: '',
        httpMethod: 'GET',
        apiDescription: ''
      },
      createRules: {
        apiName: [
          { required: true, message: '请输入API名称', trigger: 'blur' }
        ],
        apiType: [
          { required: true, message: '请选择API类型', trigger: 'change' }
        ],
        endpointPath: [
          { required: true, message: '请输入接口路径', trigger: 'blur' }
        ],
        httpMethod: [
          { required: true, message: '请选择HTTP方法', trigger: 'change' }
        ]
      },
      
      // 选项数据
      apiTypeOptions: [],
      httpMethodOptions: []
    }
  },
  created() {
    this.loadData()
    this.loadOptions()
  },
  methods: {
    // 加载数据
    async loadData() {
      try {
        const response = await getSystemOverview()
        if (response.success) {
          this.overviewData = response.data
        }
      } catch (error) {
        console.error('加载概览数据失败:', error)
      }
    },
    
    // 加载选项数据
    loadOptions() {
      const options = getApiManagementOptions()
      this.apiTypeOptions = options.apiTypes
      this.httpMethodOptions = options.httpMethods
    },
    
    // 刷新数据
    refreshData() {
      this.loadData()
      this.$message.success('数据已刷新')
    },
    
    // 导航方法
    navigateToList() {
      this.$router.push('/management-accountant/intg/api-management/list')
    },
    
    navigateToDashboard() {
      this.$router.push('/management-accountant/intg/api-management/dashboard')
    },
    
    navigateToTest() {
      this.$router.push('/management-accountant/intg/api-management/test')
    },
    
    // 显示创建对话框
    showCreateDialog() {
      this.createDialogVisible = true
      this.resetCreateForm()
    },
    
    // 重置创建表单
    resetCreateForm() {
      this.createForm = {
        apiName: '',
        apiType: 'REST',
        endpointPath: '',
        httpMethod: 'GET',
        apiDescription: ''
      }
      if (this.$refs.createForm) {
        this.$refs.createForm.clearValidate()
      }
    },
    
    // 处理创建API
    async handleCreateApi() {
      try {
        await this.$refs.createForm.validate()
        this.createLoading = true
        
        const response = await createApiManagement(this.createForm)
        if (response.success) {
          this.$message.success('API创建成功')
          this.createDialogVisible = false
          this.loadData()
        } else {
          this.$message.error(response.message || 'API创建失败')
        }
      } catch (error) {
        console.error('创建API失败:', error)
        this.$message.error('创建失败')
      } finally {
        this.createLoading = false
      }
    },
    
    // 处理创建对话框关闭
    handleCreateDialogClose(done) {
      if (this.createLoading) {
        return
      }
      this.resetCreateForm()
      done()
    },
    
    // 快速操作
    quickCreateRestApi() {
      this.createForm.apiType = 'REST'
      this.showCreateDialog()
    },
    
    quickImportApi() {
      this.$message.info('导入API功能开发中...')
    },
    
    quickExportApi() {
      this.$message.info('导出API功能开发中...')
    },
    
    quickTestApi() {
      this.navigateToTest()
    },
    
    quickViewDocs() {
      this.$message.info('查看文档功能开发中...')
    },
    
    quickViewStats() {
      this.navigateToDashboard()
    },
    
    // 查看所有活动
    viewAllActivities() {
      this.$message.info('查看全部活动功能开发中...')
    },
    
    // 获取活动图标类名
    getActivityIconClass(type) {
      const classMap = {
        create: 'activity-create',
        update: 'activity-update',
        delete: 'activity-delete',
        test: 'activity-test'
      }
      return classMap[type] || 'activity-default'
    },
    
    // 获取活动图标
    getActivityIcon(type) {
      const iconMap = {
        create: 'el-icon-plus',
        update: 'el-icon-edit',
        delete: 'el-icon-delete',
        test: 'el-icon-cpu'
      }
      return iconMap[type] || 'el-icon-info'
    },
    
    // 格式化时间
    formatTime(time) {
      const now = new Date()
      const diff = now - time
      const minutes = Math.floor(diff / (1000 * 60))
      const hours = Math.floor(diff / (1000 * 60 * 60))
      const days = Math.floor(diff / (1000 * 60 * 60 * 24))
      
      if (minutes < 60) {
        return `${minutes}分钟前`
      } else if (hours < 24) {
        return `${hours}小时前`
      } else {
        return `${days}天前`
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.api-management-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: calc(100vh - 84px);
}

.page-header {
  background: white;
  border-radius: 8px;
  padding: 24px;
  margin-bottom: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

  .header-content {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .title-section {
    .page-title {
      margin: 0 0 8px 0;
      font-size: 24px;
      font-weight: 600;
      color: #303133;

      i {
        margin-right: 8px;
        color: #409eff;
      }
    }

    .page-description {
      margin: 0;
      color: #606266;
      font-size: 14px;
    }
  }
}

.overview-section {
  margin-bottom: 20px;

  .stat-card {
    background: white;
    border-radius: 8px;
    padding: 20px;
    display: flex;
    align-items: center;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    transition: transform 0.2s;

    &:hover {
      transform: translateY(-2px);
    }

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

      &.api-total {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }

      &.api-active {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      }

      &.api-testing {
        background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
      }

      &.api-deprecated {
        background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
      }
    }

    .stat-content {
      .stat-value {
        font-size: 28px;
        font-weight: 600;
        color: #303133;
        line-height: 1;
      }

      .stat-label {
        font-size: 14px;
        color: #909399;
        margin-top: 4px;
      }
    }
  }
}

.navigation-section {
  margin-bottom: 20px;

  .nav-card {
    background: white;
    border-radius: 8px;
    padding: 24px;
    display: flex;
    align-items: center;
    cursor: pointer;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
    transition: all 0.2s;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 20px 0 rgba(0, 0, 0, 0.15);
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
        margin: 0 0 8px 0;
        font-size: 16px;
        font-weight: 600;
        color: #303133;
      }

      p {
        margin: 0;
        font-size: 14px;
        color: #606266;
        line-height: 1.4;
      }
    }

    .nav-arrow {
      color: #c0c4cc;
      font-size: 18px;
    }
  }
}

.activity-section, .quick-access-section {
  background: white;
  border-radius: 8px;
  padding: 24px;
  margin-bottom: 20px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

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

.activity-list {
  .activity-item {
    display: flex;
    align-items: flex-start;
    padding: 16px 0;
    border-bottom: 1px solid #f0f0f0;

    &:last-child {
      border-bottom: none;
    }

    .activity-icon {
      width: 32px;
      height: 32px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 12px;
      flex-shrink: 0;

      i {
        font-size: 14px;
        color: white;
      }

      &.activity-create {
        background: #67c23a;
      }

      &.activity-update {
        background: #e6a23c;
      }

      &.activity-delete {
        background: #f56c6c;
      }

      &.activity-test {
        background: #409eff;
      }

      &.activity-default {
        background: #909399;
      }
    }

    .activity-content {
      flex: 1;

      .activity-title {
        font-size: 14px;
        font-weight: 500;
        color: #303133;
        margin-bottom: 4px;
      }

      .activity-description {
        font-size: 13px;
        color: #606266;
        margin-bottom: 4px;
      }

      .activity-time {
        font-size: 12px;
        color: #c0c4cc;
      }
    }
  }
}

.quick-access-section {
  .quick-item {
    background: #f8f9fa;
    border: 1px solid #e9ecef;
    border-radius: 8px;
    padding: 20px;
    text-align: center;
    cursor: pointer;
    transition: all 0.2s;

    &:hover {
      background: #e9ecef;
      border-color: #409eff;
      color: #409eff;
    }

    i {
      font-size: 24px;
      margin-bottom: 8px;
      display: block;
    }

    span {
      font-size: 14px;
      font-weight: 500;
    }
  }
}

.dialog-footer {
  text-align: right;
}
</style>
