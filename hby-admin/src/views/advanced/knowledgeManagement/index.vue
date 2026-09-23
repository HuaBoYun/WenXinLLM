<template>
  <div class="advanced-knowledge-management">
    <!-- 统计概览 -->
    <el-row :gutter="16" class="overview-cards">
      <el-col :span="6">
        <el-card class="overview-card knowledge-gradient">
          <div class="card-content">
            <div class="card-icon">
              <i class="el-icon-document-copy"></i>
            </div>
            <div class="card-info">
              <div class="card-title">知识文档</div>
              <div class="card-value">{{ overviewData.totalDocuments }}份</div>
              <div class="card-desc">本月新增 {{ overviewData.monthlyNew }}份</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card knowledge-gradient">
          <div class="card-content">
            <div class="card-icon">
              <i class="el-icon-collection-tag"></i>
            </div>
            <div class="card-info">
              <div class="card-title">知识分类</div>
              <div class="card-value">{{ overviewData.totalCategories }}个</div>
              <div class="card-desc">活跃分类 {{ overviewData.activeCategories }}个</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card knowledge-gradient">
          <div class="card-content">
            <div class="card-icon">
              <i class="el-icon-view"></i>
            </div>
            <div class="card-info">
              <div class="card-title">访问量</div>
              <div class="card-value">{{ overviewData.totalViews }}万次</div>
              <div class="card-desc">今日访问 {{ overviewData.todayViews }}次</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card knowledge-gradient">
          <div class="card-content">
            <div class="card-icon">
              <i class="el-icon-star-on"></i>
            </div>
            <div class="card-info">
              <div class="card-title">知识评分</div>
              <div class="card-value">{{ overviewData.averageRating }}</div>
              <div class="card-desc">满意度 {{ overviewData.satisfaction }}%</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 功能模块 -->
    <el-card class="function-card">
      <div slot="header" class="card-header">
        <span class="card-title">知识管理功能</span>
        <div class="card-actions">
          <el-button type="primary" icon="el-icon-plus" size="small">新增知识</el-button>
          <el-button type="success" icon="el-icon-refresh" size="small">同步更新</el-button>
        </div>
      </div>

      <el-tabs v-model="activeTab" type="border-card">
        <el-tab-pane label="知识库管理" name="repository">
          <KnowledgeRepositoryManagement />
        </el-tab-pane>
        <el-tab-pane label="知识分类体系" name="classification">
          <KnowledgeClassificationSystem />
        </el-tab-pane>
        <el-tab-pane label="知识搜索引擎" name="search">
          <KnowledgeSearchEngine />
        </el-tab-pane>
        <el-tab-pane label="专家知识库" name="expert">
          <ExpertKnowledgeBase />
        </el-tab-pane>
        <el-tab-pane label="知识共享平台" name="sharing">
          <KnowledgeSharingPlatform />
        </el-tab-pane>
        <el-tab-pane label="知识质量评估" name="quality">
          <KnowledgeQualityAssessment />
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 知识分析图表 -->
    <el-row :gutter="16" class="chart-section">
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header" class="card-header">
            <span class="card-title">知识访问趋势</span>
          </div>
          <div id="knowledgeAccessChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header" class="card-header">
            <span class="card-title">知识类型分布</span>
          </div>
          <div id="knowledgeTypeChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 知识清单 -->
    <el-card class="knowledge-card">
      <div slot="header" class="card-header">
        <span class="card-title">知识清单</span>
        <div class="card-actions">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索知识"
            size="small"
            style="width: 200px; margin-right: 10px;"
            prefix-icon="el-icon-search"
            @input="handleSearch"
          />
          <el-button type="text" size="small" @click="refreshKnowledge">刷新</el-button>
        </div>
      </div>
      
      <el-table :data="filteredKnowledge" stripe border>
        <el-table-column prop="knowledgeId" label="知识ID" width="120" />
        <el-table-column prop="title" label="知识标题" width="250" show-overflow-tooltip />
        <el-table-column prop="category" label="知识分类" width="120">
          <template slot-scope="scope">
            <el-tag :type="getCategoryColor(scope.row.category)">{{ scope.row.category }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="type" label="知识类型" width="100">
          <template slot-scope="scope">
            <el-tag :type="getTypeColor(scope.row.type)" size="mini">{{ scope.row.type }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="author" label="作者" width="120" />
        <el-table-column prop="views" label="访问量" width="100">
          <template slot-scope="scope">
            <span class="view-count">{{ scope.row.views }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="rating" label="评分" width="100">
          <template slot-scope="scope">
            <el-rate v-model="scope.row.rating" disabled show-score text-color="#ff9900" />
          </template>
        </el-table-column>
        <el-table-column prop="lastUpdated" label="更新时间" width="150" />
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="success" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="warning" @click="handleShare(scope.row)">分享</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
// import KnowledgeRepositoryManagement from './components/KnowledgeRepositoryManagement.vue'
// import KnowledgeClassificationSystem from './components/KnowledgeClassificationSystem.vue'
// import KnowledgeSearchEngine from './components/KnowledgeSearchEngine.vue'
// import ExpertKnowledgeBase from './components/ExpertKnowledgeBase.vue'
// import KnowledgeSharingPlatform from './components/KnowledgeSharingPlatform.vue'
// import KnowledgeQualityAssessment from './components/KnowledgeQualityAssessment.vue'

export default {
  name: 'AdvancedKnowledgeManagement',
  components: {
    // KnowledgeRepositoryManagement,
    // KnowledgeClassificationSystem,
    // KnowledgeSearchEngine,
    // ExpertKnowledgeBase,
    // KnowledgeSharingPlatform,
    // KnowledgeQualityAssessment
  },
  data() {
    return {
      activeTab: 'repository',
      searchKeyword: '',
      overviewData: {
        totalDocuments: 12856,
        monthlyNew: 234,
        totalCategories: 45,
        activeCategories: 38,
        totalViews: 156.8,
        todayViews: 1256,
        averageRating: 4.6,
        satisfaction: 92.3
      },
      knowledge: [],
      filteredKnowledge: []
    }
  },
  mounted() {
    this.loadOverviewData()
    this.loadKnowledge()
    this.initCharts()
  },
  methods: {
    // 加载概览数据
    loadOverviewData() {
      // 模拟API调用
      console.log('加载知识管理概览数据')
    },

    // 加载知识数据
    loadKnowledge() {
      // 生成模拟数据
      this.knowledge = this.generateMockKnowledge()
      this.filteredKnowledge = [...this.knowledge]
    },

    // 生成模拟知识数据
    generateMockKnowledge() {
      const categories = ['政策法规', '业务流程', '技术标准', '最佳实践', '案例分析']
      const types = ['文档', '视频', '图表', '模板']
      const statuses = ['已发布', '草稿', '审核中', '已归档']
      const titles = [
        '国有企业改革实施指南',
        '财务管理制度与流程',
        '风险管控技术标准',
        '投资决策最佳实践',
        '并购重组案例分析',
        '数字化转型经验总结'
      ]
      const authors = ['张三', '李四', '王五', '赵六', '钱七']
      
      const knowledgeList = []
      for (let i = 1; i <= 15; i++) {
        const views = Math.floor(Math.random() * 5000) + 100
        const rating = (Math.random() * 2 + 3).toFixed(1)
        
        knowledgeList.push({
          knowledgeId: `KB${new Date().getFullYear()}${String(i).padStart(4, '0')}`,
          title: titles[Math.floor(Math.random() * titles.length)],
          category: categories[Math.floor(Math.random() * categories.length)],
          type: types[Math.floor(Math.random() * types.length)],
          author: authors[Math.floor(Math.random() * authors.length)],
          views,
          rating: parseFloat(rating),
          lastUpdated: new Date(Date.now() - Math.random() * 30 * 24 * 60 * 60 * 1000).toLocaleDateString(),
          status: statuses[Math.floor(Math.random() * statuses.length)]
        })
      }
      
      return knowledgeList
    },

    // 搜索知识
    handleSearch() {
      if (!this.searchKeyword.trim()) {
        this.filteredKnowledge = [...this.knowledge]
      } else {
        this.filteredKnowledge = this.knowledge.filter(item =>
          item.title.includes(this.searchKeyword) ||
          item.category.includes(this.searchKeyword) ||
          item.author.includes(this.searchKeyword)
        )
      }
    },

    // 初始化图表
    initCharts() {
      // 这里应该使用ECharts初始化图表
      console.log('初始化知识管理图表')
    },

    // 刷新知识
    refreshKnowledge() {
      this.loadKnowledge()
      this.$message.success('知识数据已刷新')
    },

    // 查看知识详情
    handleView(row) {
      this.$message.info(`查看知识详情：${row.knowledgeId}`)
    },

    // 编辑知识
    handleEdit(row) {
      this.$message.info(`编辑知识：${row.knowledgeId}`)
    },

    // 分享知识
    handleShare(row) {
      this.$message.success(`分享知识：${row.knowledgeId}`)
    },

    // 获取分类颜色
    getCategoryColor(category) {
      const categoryMap = {
        '政策法规': 'primary',
        '业务流程': 'success',
        '技术标准': 'warning',
        '最佳实践': 'danger',
        '案例分析': 'info'
      }
      return categoryMap[category] || 'info'
    },

    // 获取类型颜色
    getTypeColor(type) {
      const typeMap = {
        '文档': 'primary',
        '视频': 'success',
        '图表': 'warning',
        '模板': 'info'
      }
      return typeMap[type] || 'info'
    },

    // 获取状态类型
    getStatusType(status) {
      const statusMap = {
        '已发布': 'success',
        '草稿': 'warning',
        '审核中': 'primary',
        '已归档': 'info'
      }
      return statusMap[status] || 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
.advanced-knowledge-management {
  padding: 20px;

  .overview-cards {
    margin-bottom: 20px;

    .overview-card {
      height: 120px;
      border: none;
      box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

      &.knowledge-gradient {
        background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
        color: white;

        .card-content {
          display: flex;
          align-items: center;
          height: 100%;

          .card-icon {
            font-size: 36px;
            margin-right: 16px;
            opacity: 0.8;
          }

          .card-info {
            flex: 1;

            .card-title {
              font-size: 14px;
              margin-bottom: 8px;
              opacity: 0.9;
            }

            .card-value {
              font-size: 24px;
              font-weight: bold;
              margin-bottom: 4px;
            }

            .card-desc {
              font-size: 12px;
              opacity: 0.8;
            }
          }
        }
      }
    }
  }

  .function-card, .chart-section, .knowledge-card {
    margin-bottom: 20px;
  }

  .chart-section {
    .chart-card {
      height: 380px;
    }
  }

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;

    .card-title {
      font-size: 16px;
      font-weight: 600;
      color: #303133;
    }

    .card-actions {
      display: flex;
      align-items: center;

      .el-button {
        margin-left: 8px;
      }
    }
  }

  .view-count {
    color: #409EFF;
    font-weight: 500;
  }

  ::v-deep .el-tabs__content {
    padding: 20px;
    min-height: 400px;
  }

  ::v-deep .el-card__body {
    padding: 20px;
  }
}
</style>
