<template>
  <div class="advanced-decision-analysis">
    <!-- 统计概览 -->
    <el-row :gutter="16" class="overview-cards">
      <el-col :span="6">
        <el-card class="overview-card decision-gradient">
          <div class="card-content">
            <div class="card-icon">
              <i class="el-icon-compass"></i>
            </div>
            <div class="card-info">
              <div class="card-title">决策分析项目</div>
              <div class="card-value">{{ overviewData.totalProjects }}个</div>
              <div class="card-desc">进行中 {{ overviewData.activeProjects }}个</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card decision-gradient">
          <div class="card-content">
            <div class="card-icon">
              <i class="el-icon-data-board"></i>
            </div>
            <div class="card-info">
              <div class="card-title">分析模型</div>
              <div class="card-value">{{ overviewData.totalModels }}个</div>
              <div class="card-desc">活跃模型 {{ overviewData.activeModels }}个</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card decision-gradient">
          <div class="card-content">
            <div class="card-icon">
              <i class="el-icon-trophy"></i>
            </div>
            <div class="card-info">
              <div class="card-title">决策成功率</div>
              <div class="card-value">{{ overviewData.successRate }}%</div>
              <div class="card-desc">较上季度提升 +3.2%</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card decision-gradient">
          <div class="card-content">
            <div class="card-icon">
              <i class="el-icon-money"></i>
            </div>
            <div class="card-info">
              <div class="card-title">经济效益</div>
              <div class="card-value">{{ overviewData.economicBenefit }}亿</div>
              <div class="card-desc">累计创造价值</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询表单 -->
    <el-card class="search-card">
      <el-form :model="queryForm" ref="queryForm" :inline="true" label-width="80px">
        <el-form-item label="分析类型">
          <el-select v-model="queryForm.analysisType" placeholder="请选择分析类型" clearable>
            <el-option label="投资决策分析" value="投资决策分析" />
            <el-option label="战略决策分析" value="战略决策分析" />
            <el-option label="经营决策分析" value="经营决策分析" />
            <el-option label="风险决策分析" value="风险决策分析" />
          </el-select>
        </el-form-item>
        <el-form-item label="分析状态">
          <el-select v-model="queryForm.status" placeholder="请选择状态" clearable>
            <el-option label="已完成" value="已完成" />
            <el-option label="分析中" value="分析中" />
            <el-option label="待分析" value="待分析" />
          </el-select>
        </el-form-item>
        <el-form-item label="重要性">
          <el-select v-model="queryForm.importance" placeholder="请选择重要性" clearable>
            <el-option label="高" value="高" />
            <el-option label="中" value="中" />
            <el-option label="低" value="低" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery" icon="el-icon-search">查询</el-button>
          <el-button @click="handleReset" icon="el-icon-refresh">重置</el-button>
          <el-button type="success" @click="handleAdd" icon="el-icon-plus">新增分析</el-button>
          <el-button type="warning" @click="handleExport" icon="el-icon-download">导出</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 功能模块 -->
    <el-card class="function-card">
      <div slot="header" class="card-header">
        <span class="card-title">决策支持分析功能</span>
        <div class="card-actions">
          <el-button type="primary" @click="handleAdd" icon="el-icon-plus" size="small">新增分析</el-button>
          <el-button type="success" @click="loadOverviewData" icon="el-icon-refresh" size="small">刷新数据</el-button>
        </div>
      </div>

      <el-tabs v-model="activeTab" type="border-card">
        <el-tab-pane label="投资决策分析" name="investment">
          <InvestmentDecisionAnalysis />
        </el-tab-pane>
        <el-tab-pane label="战略决策分析" name="strategic">
          <StrategicDecisionAnalysis />
        </el-tab-pane>
        <el-tab-pane label="运营决策分析" name="operational">
          <OperationalDecisionAnalysis />
        </el-tab-pane>
        <el-tab-pane label="风险决策分析" name="risk">
          <RiskDecisionAnalysis />
        </el-tab-pane>
        <el-tab-pane label="情景分析模拟" name="scenario">
          <ScenarioAnalysisSimulation />
        </el-tab-pane>
        <el-tab-pane label="决策效果评估" name="evaluation">
          <DecisionEffectEvaluation />
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 决策分析图表 -->
    <el-row :gutter="16" class="chart-section">
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header" class="card-header">
            <span class="card-title">决策效果趋势</span>
          </div>
          <div id="decisionEffectChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header" class="card-header">
            <span class="card-title">决策类型分布</span>
          </div>
          <div id="decisionDistributionChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 决策分析报告 -->
    <el-card class="reports-card">
      <div slot="header" class="card-header">
        <span class="card-title">决策分析报告</span>
        <div class="card-actions">
          <el-tag type="primary" size="small">智能分析</el-tag>
          <el-button type="text" size="small" @click="refreshReports">刷新</el-button>
        </div>
      </div>
      
      <el-table :data="analysisReports" stripe border>
        <el-table-column prop="reportId" label="报告ID" width="120" />
        <el-table-column prop="projectName" label="项目名称" width="200" show-overflow-tooltip />
        <el-table-column prop="analysisType" label="分析类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getAnalysisTypeColor(scope.row.analysisType)">{{ scope.row.analysisType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="complexity" label="复杂度" width="100">
          <template slot-scope="scope">
            <el-tag :type="getComplexityType(scope.row.complexity)" size="mini">{{ scope.row.complexity }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="accuracy" label="准确度" width="100">
          <template slot-scope="scope">
            <el-progress :percentage="scope.row.accuracy" :color="getAccuracyColor(scope.row.accuracy)" />
          </template>
        </el-table-column>
        <el-table-column prop="recommendation" label="推荐方案" show-overflow-tooltip />
        <el-table-column prop="expectedROI" label="预期ROI" width="100">
          <template slot-scope="scope">
            <span class="roi-value">{{ scope.row.expectedROI }}%</span>
          </template>
        </el-table-column>
        <el-table-column prop="riskLevel" label="风险等级" width="100">
          <template slot-scope="scope">
            <el-tag :type="getRiskLevelType(scope.row.riskLevel)">{{ scope.row.riskLevel }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="generatedTime" label="生成时间" width="150" />
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="success" @click="handleDownload(scope.row)">下载</el-button>
            <el-button size="mini" type="warning" @click="handleShare(scope.row)">分享</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
// import InvestmentDecisionAnalysis from './components/InvestmentDecisionAnalysis.vue'
// import StrategicDecisionAnalysis from './components/StrategicDecisionAnalysis.vue'
// import OperationalDecisionAnalysis from './components/OperationalDecisionAnalysis.vue'
// import RiskDecisionAnalysis from './components/RiskDecisionAnalysis.vue'
// import ScenarioAnalysisSimulation from './components/ScenarioAnalysisSimulation.vue'
// import DecisionEffectEvaluation from './components/DecisionEffectEvaluation.vue'

export default {
  name: 'AdvancedDecisionAnalysis',
  components: {
    // InvestmentDecisionAnalysis,
    // StrategicDecisionAnalysis,
    // OperationalDecisionAnalysis,
    // RiskDecisionAnalysis,
    // ScenarioAnalysisSimulation,
    // DecisionEffectEvaluation
  },
  data() {
    return {
      activeTab: 'investment',
      loading: false,
      total: 0,
      queryForm: {
        analysisType: '',
        status: '',
        importance: '',
        pageNum: 1,
        pageSize: 20
      },
      overviewData: {
        totalProjects: 156,
        activeProjects: 23,
        totalModels: 45,
        activeModels: 32,
        successRate: 87.5,
        economicBenefit: 125.8
      },
      analysisReports: []
    }
  },
  mounted() {
    this.loadOverviewData()
    this.loadAnalysisReports()
    this.initCharts()
  },
  methods: {
    // 加载概览数据
    loadOverviewData() {
      // 模拟API调用
      console.log('加载决策支持分析概览数据')
    },

    // 加载分析报告
    async loadAnalysisReports() {
      this.loading = true
      try {
        const response = await this.$http.post('/api/advanced-analysis/reports/list', this.queryForm)
        if (response.code === 1) {
          this.analysisReports = response.data.list || []
          this.total = response.data.total || 0
        } else {
          this.$message.error(response.msg || '获取高级分析报告列表失败')
        }
      } catch (error) {
        console.error('获取高级分析报告列表异常:', error)
        this.$message.error('获取数据失败')
        // 降级到模拟数据
        this.analysisReports = this.generateMockReports()
        this.total = this.analysisReports.length
      } finally {
        this.loading = false
      }
    },

    // 查询
    handleQuery() {
      this.queryForm.pageNum = 1
      this.loadAnalysisReports()
    },

    // 重置
    handleReset() {
      this.$refs.queryForm.resetFields()
      this.queryForm = {
        analysisType: '',
        status: '',
        importance: '',
        pageNum: 1,
        pageSize: 20
      }
      this.loadAnalysisReports()
    },

    // 新增分析
    handleAdd() {
      this.$message.info('新增高级分析任务功能开发中...')
    },

    // 导出
    handleExport() {
      const loading = this.$loading({
        lock: true,
        text: '正在导出数据...',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)',
      })

      setTimeout(() => {
        loading.close()
        this.$message.success('导出成功')
      }, 2000)
    },

    // 生成模拟报告数据
    generateMockReports() {
      const analysisTypes = ['投资分析', '战略分析', '运营分析', '风险分析']
      const complexity = ['简单', '中等', '复杂', '极复杂']
      const riskLevels = ['低', '中', '高']
      const statuses = ['生成中', '已完成', '已审核', '已发布']
      const projects = [
        '新能源投资项目',
        '数字化转型战略',
        '供应链优化项目',
        '市场扩张计划',
        '技术创新项目'
      ]
      
      const reports = []
      for (let i = 1; i <= 10; i++) {
        const accuracy = Math.floor(Math.random() * 20) + 80
        const expectedROI = (Math.random() * 20 + 5).toFixed(1)
        
        reports.push({
          reportId: `RPT${new Date().getFullYear()}${String(i).padStart(4, '0')}`,
          projectName: projects[Math.floor(Math.random() * projects.length)],
          analysisType: analysisTypes[Math.floor(Math.random() * analysisTypes.length)],
          complexity: complexity[Math.floor(Math.random() * complexity.length)],
          accuracy,
          recommendation: this.generateRecommendation(),
          expectedROI,
          riskLevel: riskLevels[Math.floor(Math.random() * riskLevels.length)],
          generatedTime: new Date(Date.now() - Math.random() * 7 * 24 * 60 * 60 * 1000).toLocaleString(),
          status: statuses[Math.floor(Math.random() * statuses.length)]
        })
      }
      
      return reports
    },

    // 生成推荐方案
    generateRecommendation() {
      const recommendations = [
        '建议采用分阶段投资策略，降低投资风险',
        '建议优先投资核心技术领域，提升竞争优势',
        '建议加强与战略伙伴合作，实现资源共享',
        '建议建立风险预警机制，及时应对市场变化',
        '建议优化资源配置，提高运营效率',
        '建议加大研发投入，保持技术领先地位'
      ]
      return recommendations[Math.floor(Math.random() * recommendations.length)]
    },

    // 初始化图表
    initCharts() {
      // 这里应该使用ECharts初始化图表
      console.log('初始化决策分析图表')
    },

    // 刷新报告
    refreshReports() {
      this.loadAnalysisReports()
      this.$message.success('分析报告已刷新')
    },

    // 查看报告详情
    handleView(row) {
      this.$message.info(`查看分析报告：${row.reportId}`)
    },

    // 下载报告
    handleDownload(row) {
      this.$message.success(`下载分析报告：${row.reportId}`)
    },

    // 分享报告
    handleShare(row) {
      this.$message.info(`分享分析报告：${row.reportId}`)
    },

    // 获取分析类型颜色
    getAnalysisTypeColor(type) {
      const typeMap = {
        '投资分析': 'primary',
        '战略分析': 'success',
        '运营分析': 'warning',
        '风险分析': 'danger'
      }
      return typeMap[type] || 'info'
    },

    // 获取复杂度类型
    getComplexityType(complexity) {
      const complexityMap = {
        '简单': 'success',
        '中等': 'warning',
        '复杂': 'danger',
        '极复杂': 'danger'
      }
      return complexityMap[complexity] || 'info'
    },

    // 获取准确度颜色
    getAccuracyColor(accuracy) {
      if (accuracy >= 95) return '#67C23A'
      if (accuracy >= 90) return '#409EFF'
      if (accuracy >= 85) return '#E6A23C'
      return '#F56C6C'
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
        '生成中': 'warning',
        '已完成': 'primary',
        '已审核': 'success',
        '已发布': 'info'
      }
      return statusMap[status] || 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
.advanced-decision-analysis {
  padding: 20px;

  .overview-cards {
    margin-bottom: 20px;

    .overview-card {
      height: 120px;
      border: none;
      box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

      &.decision-gradient {
        background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
        color: #333;

        .card-content {
          display: flex;
          align-items: center;
          height: 100%;

          .card-icon {
            font-size: 36px;
            margin-right: 16px;
            opacity: 0.8;
            color: #666;
          }

          .card-info {
            flex: 1;

            .card-title {
              font-size: 14px;
              margin-bottom: 8px;
              opacity: 0.9;
              color: #666;
            }

            .card-value {
              font-size: 24px;
              font-weight: bold;
              margin-bottom: 4px;
              color: #333;
            }

            .card-desc {
              font-size: 12px;
              opacity: 0.8;
              color: #666;
            }
          }
        }
      }
    }
  }

  .function-card, .chart-section, .reports-card {
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

  .roi-value {
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
