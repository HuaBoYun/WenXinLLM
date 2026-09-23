<template>
  <div class="advanced-capital-layout">
    <!-- 统计概览 -->
    <el-row :gutter="16" class="overview-cards">
      <el-col :span="6">
        <el-card class="overview-card capital-gradient">
          <div class="card-content">
            <div class="card-icon">
              <i class="el-icon-money"></i>
            </div>
            <div class="card-info">
              <div class="card-title">国有资本总额</div>
              <div class="card-value">{{ overviewData.totalCapital }}万亿</div>
              <div class="card-desc">较去年增长 +{{ overviewData.growthRate }}%</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card capital-gradient">
          <div class="card-content">
            <div class="card-icon">
              <i class="el-icon-pie-chart"></i>
            </div>
            <div class="card-info">
              <div class="card-title">布局企业</div>
              <div class="card-value">{{ overviewData.totalCompanies }}家</div>
              <div class="card-desc">央企 {{ overviewData.centralCompanies }}家</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card capital-gradient">
          <div class="card-content">
            <div class="card-icon">
              <i class="el-icon-trophy"></i>
            </div>
            <div class="card-info">
              <div class="card-title">投资回报率</div>
              <div class="card-value">{{ overviewData.roi }}%</div>
              <div class="card-desc">优秀等级</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card capital-gradient">
          <div class="card-content">
            <div class="card-icon">
              <i class="el-icon-star-on"></i>
            </div>
            <div class="card-info">
              <div class="card-title">优化建议</div>
              <div class="card-value">{{ overviewData.suggestions }}条</div>
              <div class="card-desc">待实施 {{ overviewData.pendingSuggestions }}条</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 功能模块 -->
    <el-card class="function-card">
      <div slot="header" class="card-header">
        <span class="card-title">国有资本布局优化功能</span>
        <div class="card-actions">
          <el-button type="primary" icon="el-icon-plus" size="small">新增布局</el-button>
          <el-button type="success" icon="el-icon-refresh" size="small">刷新分析</el-button>
        </div>
      </div>

      <el-tabs v-model="activeTab" type="border-card">
        <el-tab-pane label="资本布局分析" name="analysis">
          <CapitalLayoutAnalysis />
        </el-tab-pane>
        <el-tab-pane label="行业配置优化" name="industry">
          <IndustryAllocationOptimization />
        </el-tab-pane>
        <el-tab-pane label="区域布局优化" name="regional">
          <RegionalLayoutOptimization />
        </el-tab-pane>
        <el-tab-pane label="投资组合管理" name="portfolio">
          <InvestmentPortfolioManagement />
        </el-tab-pane>
        <el-tab-pane label="风险收益评估" name="riskReturn">
          <RiskReturnAssessment />
        </el-tab-pane>
        <el-tab-pane label="布局效果监控" name="monitoring">
          <LayoutEffectMonitoring />
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 资本布局图表 -->
    <el-row :gutter="16" class="chart-section">
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header" class="card-header">
            <span class="card-title">行业资本分布</span>
          </div>
          <div id="industryDistributionChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header" class="card-header">
            <span class="card-title">区域资本分布</span>
          </div>
          <div id="regionalDistributionChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 优化建议 -->
    <el-card class="suggestions-card">
      <div slot="header" class="card-header">
        <span class="card-title">资本布局优化建议</span>
        <div class="card-actions">
          <el-tag type="primary" size="small">AI推荐</el-tag>
          <el-button type="text" size="small" @click="refreshSuggestions">刷新</el-button>
        </div>
      </div>
      
      <el-table :data="optimizationSuggestions" stripe border>
        <el-table-column prop="suggestionId" label="建议ID" width="120" />
        <el-table-column prop="targetArea" label="目标领域" width="150" show-overflow-tooltip />
        <el-table-column prop="optimizationType" label="优化类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getOptimizationTypeColor(scope.row.optimizationType)">{{ scope.row.optimizationType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="priority" label="优先级" width="100">
          <template slot-scope="scope">
            <el-tag :type="getPriorityType(scope.row.priority)" size="mini">{{ scope.row.priority }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="expectedBenefit" label="预期收益" width="120">
          <template slot-scope="scope">
            <span class="benefit-amount">{{ scope.row.expectedBenefit }}亿元</span>
          </template>
        </el-table-column>
        <el-table-column prop="riskLevel" label="风险等级" width="100">
          <template slot-scope="scope">
            <el-tag :type="getRiskLevelType(scope.row.riskLevel)">{{ scope.row.riskLevel }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="suggestion" label="优化建议" show-overflow-tooltip />
        <el-table-column prop="implementationPeriod" label="实施周期" width="100" />
        <el-table-column prop="generatedTime" label="生成时间" width="150" />
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="success" @click="handleImplement(scope.row)">实施</el-button>
            <el-button size="mini" type="warning" @click="handleEvaluate(scope.row)">评估</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
// import CapitalLayoutAnalysis from './components/CapitalLayoutAnalysis.vue'
// import IndustryAllocationOptimization from './components/IndustryAllocationOptimization.vue'
// import RegionalLayoutOptimization from './components/RegionalLayoutOptimization.vue'
// import InvestmentPortfolioManagement from './components/InvestmentPortfolioManagement.vue'
// import RiskReturnAssessment from './components/RiskReturnAssessment.vue'
// import LayoutEffectMonitoring from './components/LayoutEffectMonitoring.vue'

export default {
  name: 'AdvancedCapitalLayout',
  components: {
    // CapitalLayoutAnalysis,
    // IndustryAllocationOptimization,
    // RegionalLayoutOptimization,
    // InvestmentPortfolioManagement,
    // RiskReturnAssessment,
    // LayoutEffectMonitoring
  },
  data() {
    return {
      activeTab: 'analysis',
      overviewData: {
        totalCapital: 78.5,
        growthRate: 8.2,
        totalCompanies: 1256,
        centralCompanies: 97,
        roi: 12.8,
        suggestions: 156,
        pendingSuggestions: 23
      },
      optimizationSuggestions: []
    }
  },
  mounted() {
    this.loadOverviewData()
    this.loadOptimizationSuggestions()
    this.initCharts()
  },
  methods: {
    // 加载概览数据
    loadOverviewData() {
      // 模拟API调用
      console.log('加载国有资本布局优化概览数据')
    },

    // 加载优化建议
    loadOptimizationSuggestions() {
      // 生成模拟数据
      this.optimizationSuggestions = this.generateMockSuggestions()
    },

    // 生成模拟优化建议数据
    generateMockSuggestions() {
      const optimizationTypes = ['行业调整', '区域优化', '资产重组', '投资退出']
      const priorities = ['低', '中', '高', '紧急']
      const riskLevels = ['低', '中', '高']
      const statuses = ['待审核', '已批准', '实施中', '已完成']
      const targetAreas = [
        '新能源产业',
        '数字经济',
        '高端制造',
        '现代服务业',
        '基础设施',
        '金融服务'
      ]
      
      const suggestions = []
      for (let i = 1; i <= 12; i++) {
        const expectedBenefit = (Math.random() * 100 + 20).toFixed(1)
        const implementationPeriod = ['3个月', '6个月', '1年', '2年'][Math.floor(Math.random() * 4)]
        
        suggestions.push({
          suggestionId: `OPT${new Date().getFullYear()}${String(i).padStart(4, '0')}`,
          targetArea: targetAreas[Math.floor(Math.random() * targetAreas.length)],
          optimizationType: optimizationTypes[Math.floor(Math.random() * optimizationTypes.length)],
          priority: priorities[Math.floor(Math.random() * priorities.length)],
          expectedBenefit,
          riskLevel: riskLevels[Math.floor(Math.random() * riskLevels.length)],
          suggestion: this.generateOptimizationSuggestion(),
          implementationPeriod,
          generatedTime: new Date(Date.now() - Math.random() * 7 * 24 * 60 * 60 * 1000).toLocaleString(),
          status: statuses[Math.floor(Math.random() * statuses.length)]
        })
      }
      
      return suggestions
    },

    // 生成优化建议文本
    generateOptimizationSuggestion() {
      const suggestions = [
        '建议增加对新兴产业的投资比重，优化产业结构',
        '建议退出传统低效产业，集中资源发展核心业务',
        '建议加强区域协调发展，平衡东西部投资布局',
        '建议通过并购重组，提升产业集中度和竞争力',
        '建议优化投资组合，降低整体投资风险',
        '建议加大科技创新投入，培育新的增长点'
      ]
      return suggestions[Math.floor(Math.random() * suggestions.length)]
    },

    // 初始化图表
    initCharts() {
      // 这里应该使用ECharts初始化图表
      console.log('初始化资本布局图表')
    },

    // 刷新建议
    refreshSuggestions() {
      this.loadOptimizationSuggestions()
      this.$message.success('优化建议已刷新')
    },

    // 查看建议详情
    handleView(row) {
      this.$message.info(`查看优化建议详情：${row.suggestionId}`)
    },

    // 实施建议
    handleImplement(row) {
      this.$message.success(`开始实施优化建议：${row.suggestionId}`)
    },

    // 评估建议
    handleEvaluate(row) {
      this.$message.info(`评估优化建议：${row.suggestionId}`)
    },

    // 获取优化类型颜色
    getOptimizationTypeColor(type) {
      const typeMap = {
        '行业调整': 'primary',
        '区域优化': 'success',
        '资产重组': 'warning',
        '投资退出': 'danger'
      }
      return typeMap[type] || 'info'
    },

    // 获取优先级类型
    getPriorityType(priority) {
      const priorityMap = {
        '低': 'info',
        '中': 'warning',
        '高': 'danger',
        '紧急': 'danger'
      }
      return priorityMap[priority] || 'info'
    },

    // 获取风险等级类型
    getRiskLevelType(level) {
      const levelMap = {
        '低': 'success',
        '中': 'warning',
        '高': 'danger'
      }
      return levelMap[level] || 'info'
    },

    // 获取状态类型
    getStatusType(status) {
      const statusMap = {
        '待审核': 'warning',
        '已批准': 'primary',
        '实施中': 'info',
        '已完成': 'success'
      }
      return statusMap[status] || 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
.advanced-capital-layout {
  padding: 20px;

  .overview-cards {
    margin-bottom: 20px;

    .overview-card {
      height: 120px;
      border: none;
      box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

      &.capital-gradient {
        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
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

  .function-card, .chart-section, .suggestions-card {
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
      .el-button {
        margin-left: 8px;
      }
    }
  }

  .benefit-amount {
    color: #67C23A;
    font-weight: 500;
    font-size: 14px;
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
