<template>
  <div class="archive-search-index">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h1 class="page-title">
            <i class="el-icon-search"></i>
            档案检索管理
          </h1>
          <p class="page-description">智能化档案检索系统，支持全文检索、语义检索、图像检索、语音检索等多种检索方式</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-plus" @click="handleCreate">
            创建检索配置
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
              <i class="el-icon-files"></i>
            </div>
            <div class="stats-content">
              <div class="stats-number">{{ overview.totalSearches || 0 }}</div>
              <div class="stats-label">总检索配置</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stats-card">
            <div class="stats-icon active">
              <i class="el-icon-success"></i>
            </div>
            <div class="stats-content">
              <div class="stats-number">{{ overview.activeSearches || 0 }}</div>
              <div class="stats-label">活跃配置</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stats-card">
            <div class="stats-icon indexing">
              <i class="el-icon-loading"></i>
            </div>
            <div class="stats-content">
              <div class="stats-number">{{ overview.indexingSearches || 0 }}</div>
              <div class="stats-label">索引中</div>
            </div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stats-card">
            <div class="stats-icon performance">
              <i class="el-icon-data-analysis"></i>
            </div>
            <div class="stats-content">
              <div class="stats-number">{{ overview.avgResponseTime || 0 }}ms</div>
              <div class="stats-label">平均响应时间</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 功能导航 -->
    <div class="function-nav">
      <div class="nav-title">功能导航</div>
      <el-row :gutter="20">
        <el-col :span="6" v-for="nav in functionNavs" :key="nav.key">
          <div class="nav-card" @click="handleNavClick(nav)">
            <div class="nav-icon" :class="nav.iconClass">
              <i :class="nav.icon"></i>
            </div>
            <div class="nav-content">
              <div class="nav-title">{{ nav.title }}</div>
              <div class="nav-description">{{ nav.description }}</div>
            </div>
            <div class="nav-arrow">
              <i class="el-icon-arrow-right"></i>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 快速访问 -->
    <div class="quick-access">
      <div class="section-title">快速访问</div>
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="quick-card">
            <div class="quick-header">
              <i class="el-icon-search"></i>
              <span>快速检索</span>
            </div>
            <div class="quick-content">
              <el-input
                v-model="quickSearchKeywords"
                placeholder="输入关键词进行快速检索"
                @keyup.enter="handleQuickSearch"
              >
                <el-button slot="append" icon="el-icon-search" @click="handleQuickSearch"></el-button>
              </el-input>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="quick-card">
            <div class="quick-header">
              <i class="el-icon-upload"></i>
              <span>文档上传</span>
            </div>
            <div class="quick-content">
              <el-upload
                class="upload-demo"
                drag
                action="#"
                :auto-upload="false"
                :on-change="handleFileUpload"
                multiple
              >
                <i class="el-icon-upload"></i>
                <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
              </el-upload>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="quick-card">
            <div class="quick-header">
              <i class="el-icon-star-on"></i>
              <span>智能推荐</span>
            </div>
            <div class="quick-content">
              <div class="recommendation-list">
                <div v-for="item in recommendations" :key="item.id" class="recommendation-item">
                  <span class="recommendation-title">{{ item.title }}</span>
                  <span class="recommendation-score">{{ item.score }}%</span>
                </div>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 最近活动 -->
    <div class="recent-activities">
      <div class="section-title">最近活动</div>
      <el-table :data="recentActivities" style="width: 100%">
        <el-table-column prop="searchName" label="检索配置" width="200"></el-table-column>
        <el-table-column prop="operation" label="操作类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getOperationTagType(scope.row.operation)">
              {{ formatOperation(scope.row.operation) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="keywords" label="检索关键词" width="200"></el-table-column>
        <el-table-column prop="resultCount" label="结果数量" width="100"></el-table-column>
        <el-table-column prop="responseTime" label="响应时间" width="120">
          <template slot-scope="scope">
            {{ scope.row.responseTime }}ms
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="时间" width="180">
          <template slot-scope="scope">
            {{ formatDateTime(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120">
          <template slot-scope="scope">
            <el-button type="text" size="small" @click="handleViewDetail(scope.row)">
              查看详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
import { quickGetSearchOverview } from '@/api/managementAccountant/as/archiveSearch'

export default {
  name: 'ArchiveSearchIndex',
  data() {
    return {
      overview: {},
      quickSearchKeywords: '',
      recommendations: [
        { id: 1, title: '财务报表检索', score: 95 },
        { id: 2, title: '合同文档检索', score: 88 },
        { id: 3, title: '发票档案检索', score: 82 }
      ],
      functionNavs: [
        {
          key: 'list',
          title: '检索配置',
          description: '管理所有检索配置',
          icon: 'el-icon-menu',
          iconClass: 'nav-list',
          route: '/managementAccountant/as/archiveSearch/list'
        },
        {
          key: 'dashboard',
          title: '数据仪表板',
          description: '查看检索统计分析',
          icon: 'el-icon-data-analysis',
          iconClass: 'nav-dashboard',
          route: '/managementAccountant/as/archiveSearch/dashboard'
        },
        {
          key: 'ocr',
          title: 'OCR处理',
          description: '文档OCR识别处理',
          icon: 'el-icon-camera',
          iconClass: 'nav-ocr',
          route: '/managementAccountant/as/archiveSearch/ocr'
        },
        {
          key: 'semantic',
          title: '语义检索',
          description: '智能语义检索配置',
          icon: 'el-icon-cpu',
          iconClass: 'nav-semantic',
          route: '/managementAccountant/as/archiveSearch/semantic'
        }
      ],
      recentActivities: []
    }
  },
  created() {
    this.loadOverview()
    this.loadRecentActivities()
  },
  methods: {
    async loadOverview() {
      try {
        const tenantId = this.$store.getters.tenantId
        const result = await quickGetSearchOverview(tenantId)
        this.overview = result.overview || {}
      } catch (error) {
        console.error('加载概览数据失败:', error)
      }
    },
    loadRecentActivities() {
      // 模拟最近活动数据
      this.recentActivities = [
        {
          id: 1,
          searchName: '财务档案检索',
          operation: 'SEARCH',
          keywords: '财务报表',
          resultCount: 156,
          responseTime: 245,
          createTime: new Date()
        },
        {
          id: 2,
          searchName: '合同档案检索',
          operation: 'INDEX',
          keywords: '-',
          resultCount: 0,
          responseTime: 0,
          createTime: new Date(Date.now() - 3600000)
        }
      ]
    },
    handleCreate() {
      this.$router.push('/managementAccountant/as/archiveSearch/create')
    },
    handleNavClick(nav) {
      this.$router.push(nav.route)
    },
    handleQuickSearch() {
      if (!this.quickSearchKeywords.trim()) {
        this.$message.warning('请输入检索关键词')
        return
      }
      // 执行快速检索逻辑
      this.$message.success('正在执行检索...')
    },
    handleFileUpload(file) {
      this.$message.success(`文件 ${file.name} 上传成功`)
    },
    handleViewDetail(row) {
      this.$router.push(`/managementAccountant/as/archiveSearch/detail/${row.id}`)
    },
    getOperationTagType(operation) {
      const typeMap = {
        'SEARCH': 'success',
        'INDEX': 'warning',
        'CREATE': 'primary',
        'UPDATE': 'info',
        'DELETE': 'danger'
      }
      return typeMap[operation] || 'info'
    },
    formatOperation(operation) {
      const operationMap = {
        'SEARCH': '检索',
        'INDEX': '索引',
        'CREATE': '创建',
        'UPDATE': '更新',
        'DELETE': '删除'
      }
      return operationMap[operation] || operation
    },
    formatDateTime(date) {
      if (!date) return '-'
      return new Date(date).toLocaleString()
    }
  }
}
</script>

<style lang="scss" scoped>
.archive-search-index {
  padding: 20px;
  
  .page-header {
    margin-bottom: 24px;
    
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
  
  .stats-cards {
    margin-bottom: 24px;
    
    .stats-card {
      display: flex;
      align-items: center;
      padding: 20px;
      background: white;
      border-radius: 8px;
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
      
      .stats-icon {
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
        
        &.indexing {
          background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
        }
        
        &.performance {
          background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
        }
      }
      
      .stats-content {
        .stats-number {
          font-size: 24px;
          font-weight: 600;
          color: #303133;
          margin-bottom: 4px;
        }
        
        .stats-label {
          font-size: 14px;
          color: #909399;
        }
      }
    }
  }
  
  .function-nav {
    margin-bottom: 24px;
    
    .nav-title {
      font-size: 18px;
      font-weight: 600;
      color: #303133;
      margin-bottom: 16px;
    }
    
    .nav-card {
      display: flex;
      align-items: center;
      padding: 20px;
      background: white;
      border-radius: 8px;
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
      cursor: pointer;
      transition: all 0.3s;
      
      &:hover {
        transform: translateY(-2px);
        box-shadow: 0 4px 20px 0 rgba(0, 0, 0, 0.15);
      }
      
      .nav-icon {
        width: 40px;
        height: 40px;
        border-radius: 8px;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 16px;
        
        i {
          font-size: 20px;
          color: white;
        }
        
        &.nav-list {
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
        }
        
        &.nav-dashboard {
          background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
        }
        
        &.nav-ocr {
          background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
        }
        
        &.nav-semantic {
          background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
        }
      }
      
      .nav-content {
        flex: 1;
        
        .nav-title {
          font-size: 16px;
          font-weight: 600;
          color: #303133;
          margin-bottom: 4px;
        }
        
        .nav-description {
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
  
  .quick-access, .recent-activities {
    margin-bottom: 24px;
    
    .section-title {
      font-size: 18px;
      font-weight: 600;
      color: #303133;
      margin-bottom: 16px;
    }
    
    .quick-card {
      background: white;
      border-radius: 8px;
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
      overflow: hidden;
      
      .quick-header {
        padding: 16px 20px;
        background: #f5f7fa;
        border-bottom: 1px solid #ebeef5;
        display: flex;
        align-items: center;
        
        i {
          margin-right: 8px;
          color: #409EFF;
        }
        
        span {
          font-weight: 600;
          color: #303133;
        }
      }
      
      .quick-content {
        padding: 20px;
        
        .recommendation-list {
          .recommendation-item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 8px 0;
            border-bottom: 1px solid #f0f0f0;
            
            &:last-child {
              border-bottom: none;
            }
            
            .recommendation-title {
              color: #303133;
            }
            
            .recommendation-score {
              color: #67C23A;
              font-weight: 600;
            }
          }
        }
      }
    }
  }
}
</style>
