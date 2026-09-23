<template>
  <div class="intelligent-classification-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h1 class="page-title">
            <i class="el-icon-cpu"></i>
            智能分类管理
          </h1>
          <p class="page-description">
            基于机器学习的智能档案分类系统，支持多种算法和自动化分类流程
          </p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            创建分类
          </el-button>
          <el-button type="success" icon="el-icon-data-analysis" @click="handleDashboard">
            仪表板
          </el-button>
        </div>
      </div>
    </div>

    <!-- 统计卡片 -->
    <div class="stats-cards">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stats-card">
            <div class="stats-icon total">
              <i class="el-icon-cpu"></i>
            </div>
            <div class="stats-content">
              <div class="stats-number">{{ overviewData.totalClassifications || 0 }}</div>
              <div class="stats-label">总分类数</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stats-card">
            <div class="stats-icon active">
              <i class="el-icon-check"></i>
            </div>
            <div class="stats-content">
              <div class="stats-number">{{ activeCount || 0 }}</div>
              <div class="stats-label">活跃分类</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stats-card">
            <div class="stats-icon training">
              <i class="el-icon-loading"></i>
            </div>
            <div class="stats-content">
              <div class="stats-number">{{ trainingCount || 0 }}</div>
              <div class="stats-label">训练中</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stats-card">
            <div class="stats-icon deployed">
              <i class="el-icon-upload"></i>
            </div>
            <div class="stats-content">
              <div class="stats-number">{{ deployedCount || 0 }}</div>
              <div class="stats-label">已部署</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 功能导航 -->
    <div class="function-nav">
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="nav-card" @click="handleList">
            <div class="nav-icon">
              <i class="el-icon-menu"></i>
            </div>
            <div class="nav-content">
              <h3>分类列表</h3>
              <p>查看和管理所有智能分类</p>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="nav-card" @click="handleBatchOperations">
            <div class="nav-icon">
              <i class="el-icon-operation"></i>
            </div>
            <div class="nav-content">
              <h3>批量操作</h3>
              <p>批量训练、部署和管理分类</p>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="nav-card" @click="handleDataManagement">
            <div class="nav-icon">
              <i class="el-icon-database"></i>
            </div>
            <div class="nav-content">
              <h3>数据管理</h3>
              <p>导入导出和数据维护</p>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 快速访问 -->
    <div class="quick-access">
      <h2 class="section-title">快速访问</h2>
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="quick-card" @click.native="handleReports">
            <div class="quick-content">
              <i class="el-icon-document-copy"></i>
              <span>分类报告</span>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="quick-card" @click.native="handleSystemMaintenance">
            <div class="quick-content">
              <i class="el-icon-tools"></i>
              <span>系统维护</span>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="quick-card" @click.native="handlePerformanceMonitor">
            <div class="quick-content">
              <i class="el-icon-monitor"></i>
              <span>性能监控</span>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="quick-card" @click.native="handleModelManagement">
            <div class="quick-content">
              <i class="el-icon-setting"></i>
              <span>模型管理</span>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 最近活动 -->
    <div class="recent-activity">
      <h2 class="section-title">最近活动</h2>
      <el-table :data="recentActivities" style="width: 100%">
        <el-table-column prop="classificationName" label="分类名称" width="200" />
        <el-table-column prop="action" label="操作" width="120" />
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusTagType(scope.row.status)">
              {{ getStatusLabel(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="accuracy" label="准确率" width="100">
          <template slot-scope="scope">
            {{ formatAccuracy(scope.row.accuracy) }}
          </template>
        </el-table-column>
        <el-table-column prop="updatedTime" label="更新时间" width="180" />
        <el-table-column label="操作" width="200">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="success" @click="handlePredict(scope.row)">预测</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
import { 
  quickGetClassificationOverview,
  getClassificationPage,
  getClassificationStatusLabel,
  getClassificationStatusTagType,
  formatAccuracy
} from '@/api/managementAccountant/as/intelligentClassification'

export default {
  name: 'IntelligentClassificationIndex',
  data() {
    return {
      overviewData: {},
      statusStats: [],
      recentActivities: [],
      loading: false
    }
  },
  computed: {
    activeCount() {
      const activeStat = this.statusStats.find(item => item.status === 'ACTIVE')
      return activeStat ? activeStat.count : 0
    },
    trainingCount() {
      const trainingStat = this.statusStats.find(item => item.status === 'TRAINING')
      return trainingStat ? trainingStat.count : 0
    },
    deployedCount() {
      const deployedStat = this.statusStats.find(item => item.status === 'DEPLOYED')
      return deployedStat ? deployedStat.count : 0
    }
  },
  created() {
    this.loadOverviewData()
    this.loadRecentActivities()
  },
  methods: {
    // 加载概览数据
    async loadOverviewData() {
      try {
        this.loading = true
        const tenantId = this.$store.getters.tenantId
        const result = await quickGetClassificationOverview(tenantId)
        
        this.overviewData = result.overview
        this.statusStats = result.statusStats
      } catch (error) {
        console.error('加载概览数据失败:', error)
        this.$message.error('加载概览数据失败')
      } finally {
        this.loading = false
      }
    },

    // 加载最近活动
    async loadRecentActivities() {
      try {
        const tenantId = this.$store.getters.tenantId
        const result = await getClassificationPage({
          current: 1,
          size: 10,
          tenantId
        })
        
        this.recentActivities = result.data.records || []
      } catch (error) {
        console.error('加载最近活动失败:', error)
      }
    },

    // 页面跳转方法
    handleCreate() {
      this.$router.push('/management-accountant/as/intelligent-classification/create')
    },

    handleDashboard() {
      this.$router.push('/management-accountant/as/intelligent-classification/dashboard')
    },

    handleList() {
      this.$router.push('/management-accountant/as/intelligent-classification/list')
    },

    handleBatchOperations() {
      this.$router.push('/management-accountant/as/intelligent-classification/batch-operations')
    },

    handleDataManagement() {
      this.$router.push('/management-accountant/as/intelligent-classification/data-management')
    },

    handleReports() {
      this.$router.push('/management-accountant/as/intelligent-classification/reports')
    },

    handleSystemMaintenance() {
      this.$router.push('/management-accountant/as/intelligent-classification/system-maintenance')
    },

    handlePerformanceMonitor() {
      this.$router.push('/management-accountant/as/intelligent-classification/performance/all')
    },

    handleModelManagement() {
      this.$router.push('/management-accountant/as/intelligent-classification/model-management/all')
    },

    handleView(row) {
      this.$router.push(`/management-accountant/as/intelligent-classification/detail/${row.classificationId}`)
    },

    handleEdit(row) {
      this.$router.push(`/management-accountant/as/intelligent-classification/edit/${row.classificationId}`)
    },

    handlePredict(row) {
      this.$router.push(`/management-accountant/as/intelligent-classification/prediction/${row.classificationId}`)
    },

    // 工具方法
    getStatusLabel(status) {
      return getClassificationStatusLabel(status)
    },

    getStatusTagType(status) {
      return getClassificationStatusTagType(status)
    },

    formatAccuracy(accuracy) {
      return formatAccuracy(accuracy)
    }
  }
}
</script>

<style lang="scss" scoped>
.intelligent-classification-container {
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

  .page-title {
    margin: 0 0 8px 0;
    font-size: 24px;
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

.stats-cards {
  margin-bottom: 20px;

  .stats-card {
    background: white;
    border-radius: 8px;
    padding: 20px;
    display: flex;
    align-items: center;
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);

    .stats-icon {
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

      &.total {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
      }

      &.active {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
      }

      &.training {
        background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
      }

      &.deployed {
        background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
      }
    }

    .stats-content {
      .stats-number {
        font-size: 28px;
        font-weight: bold;
        color: #303133;
        line-height: 1;
      }

      .stats-label {
        font-size: 14px;
        color: #909399;
        margin-top: 4px;
      }
    }
  }
}

.function-nav {
  margin-bottom: 20px;

  .nav-card {
    background: white;
    border-radius: 8px;
    padding: 24px;
    display: flex;
    align-items: center;
    cursor: pointer;
    transition: all 0.3s;
    box-shadow: 0 2px 4px rgba(0,0,0,0.1);

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 8px rgba(0,0,0,0.15);
    }

    .nav-icon {
      width: 50px;
      height: 50px;
      background: #409EFF;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-right: 16px;

      i {
        font-size: 20px;
        color: white;
      }
    }

    .nav-content {
      h3 {
        margin: 0 0 4px 0;
        font-size: 16px;
        color: #303133;
      }

      p {
        margin: 0;
        font-size: 12px;
        color: #909399;
      }
    }
  }
}

.quick-access, .recent-activity {
  background: white;
  border-radius: 8px;
  padding: 24px;
  margin-bottom: 20px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);

  .section-title {
    margin: 0 0 20px 0;
    font-size: 18px;
    color: #303133;
  }

  .quick-card {
    cursor: pointer;
    transition: all 0.3s;

    &:hover {
      transform: translateY(-2px);
      box-shadow: 0 4px 8px rgba(0,0,0,0.15);
    }

    .quick-content {
      text-align: center;
      padding: 20px;

      i {
        font-size: 32px;
        color: #409EFF;
        margin-bottom: 8px;
        display: block;
      }

      span {
        font-size: 14px;
        color: #303133;
      }
    }
  }
}
</style>
