<template>
  <div class="intelligent-data-analysis">
    <!-- 统计概览 -->
    <el-row :gutter="16" class="overview-cards">
      <el-col :span="6">
        <el-card class="overview-card analysis-gradient">
          <div class="card-content">
            <div class="card-icon">
              <i class="el-icon-data-analysis"></i>
            </div>
            <div class="card-info">
              <div class="card-title">数据分析任务</div>
              <div class="card-value">{{ overviewData.totalTasks }}个</div>
              <div class="card-desc">运行中 {{ overviewData.runningTasks }}个</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card analysis-gradient">
          <div class="card-content">
            <div class="card-icon">
              <i class="el-icon-pie-chart"></i>
            </div>
            <div class="card-info">
              <div class="card-title">数据处理量</div>
              <div class="card-value">{{ overviewData.dataVolume }}TB</div>
              <div class="card-desc">今日处理 {{ overviewData.todayVolume }}GB</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card analysis-gradient">
          <div class="card-content">
            <div class="card-icon">
              <i class="el-icon-monitor"></i>
            </div>
            <div class="card-info">
              <div class="card-title">分析准确率</div>
              <div class="card-value">{{ overviewData.accuracy }}%</div>
              <div class="card-desc">较上月提升 +1.8%</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card analysis-gradient">
          <div class="card-content">
            <div class="card-icon">
              <i class="el-icon-star-on"></i>
            </div>
            <div class="card-info">
              <div class="card-title">洞察发现</div>
              <div class="card-value">{{ overviewData.insights }}条</div>
              <div class="card-desc">高价值洞察 {{ overviewData.highValueInsights }}条</div>
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
            <el-option label="财务分析" value="财务分析" />
            <el-option label="经营分析" value="经营分析" />
            <el-option label="市场分析" value="市场分析" />
            <el-option label="风险分析" value="风险分析" />
          </el-select>
        </el-form-item>
        <el-form-item label="重要性">
          <el-select v-model="queryForm.importance" placeholder="请选择重要性" clearable>
            <el-option label="低" value="低" />
            <el-option label="中" value="中" />
            <el-option label="高" value="高" />
            <el-option label="极高" value="极高" />
          </el-select>
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="queryForm.status" placeholder="请选择状态" clearable>
            <el-option label="已完成" value="已完成" />
            <el-option label="分析中" value="分析中" />
            <el-option label="待处理" value="待处理" />
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
        <span class="card-title">智能数据分析功能</span>
        <div class="card-actions">
          <el-button type="primary" @click="handleAdd" icon="el-icon-plus" size="small">新增分析</el-button>
          <el-button type="success" @click="loadOverviewData" icon="el-icon-refresh" size="small">刷新数据</el-button>
        </div>
      </div>

      <el-tabs v-model="activeTab" type="border-card">
        <el-tab-pane label="财务数据分析" name="financial">
          <FinancialDataAnalysis />
        </el-tab-pane>
        <el-tab-pane label="经营数据分析" name="operational">
          <OperationalDataAnalysis />
        </el-tab-pane>
        <el-tab-pane label="市场数据分析" name="market">
          <MarketDataAnalysis />
        </el-tab-pane>
        <el-tab-pane label="风险数据分析" name="risk">
          <RiskDataAnalysis />
        </el-tab-pane>
        <el-tab-pane label="智能报表生成" name="reporting">
          <IntelligentReporting />
        </el-tab-pane>
        <el-tab-pane label="数据质量检测" name="quality">
          <DataQualityDetection />
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 数据分析图表 -->
    <el-row :gutter="16" class="chart-section">
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header" class="card-header">
            <span class="card-title">数据处理趋势</span>
          </div>
          <div id="dataProcessingChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header" class="card-header">
            <span class="card-title">分析类型分布</span>
          </div>
          <div id="analysisTypeChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 智能洞察发现 -->
    <el-card class="insights-card">
      <div slot="header" class="card-header">
        <span class="card-title">智能洞察发现</span>
        <div class="card-actions">
          <el-tag type="success" size="small">AI分析中</el-tag>
          <el-button type="text" size="small" @click="refreshInsights">刷新</el-button>
        </div>
      </div>
      
      <el-table :data="dataInsights" stripe border>
        <el-table-column prop="insightId" label="洞察ID" width="120" />
        <el-table-column prop="dataSource" label="数据源" width="150" show-overflow-tooltip />
        <el-table-column prop="analysisType" label="分析类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getAnalysisTypeColor(scope.row.analysisType)">{{ scope.row.analysisType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="importance" label="重要性" width="100">
          <template slot-scope="scope">
            <el-tag :type="getImportanceType(scope.row.importance)" size="mini">{{ scope.row.importance }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="confidence" label="置信度" width="100">
          <template slot-scope="scope">
            <el-progress :percentage="scope.row.confidence" :color="getConfidenceColor(scope.row.confidence)" />
          </template>
        </el-table-column>
        <el-table-column prop="insight" label="洞察内容" show-overflow-tooltip />
        <el-table-column prop="impact" label="影响程度" width="100">
          <template slot-scope="scope">
            <span :class="getImpactClass(scope.row.impact)">{{ scope.row.impact }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="discoveredTime" label="发现时间" width="150" />
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="success" @click="handleExport(scope.row)">导出</el-button>
            <el-button size="mini" type="warning" @click="handleDeepAnalysis(scope.row)">深度分析</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
// import FinancialDataAnalysis from './components/FinancialDataAnalysis.vue'
// import OperationalDataAnalysis from './components/OperationalDataAnalysis.vue'
// import MarketDataAnalysis from './components/MarketDataAnalysis.vue'
// import RiskDataAnalysis from './components/RiskDataAnalysis.vue'
// import IntelligentReporting from './components/IntelligentReporting.vue'
// import DataQualityDetection from './components/DataQualityDetection.vue'

export default {
  name: 'IntelligentDataAnalysis',
  components: {
    // FinancialDataAnalysis,
    // OperationalDataAnalysis,
    // MarketDataAnalysis,
    // RiskDataAnalysis,
    // IntelligentReporting,
    // DataQualityDetection
  },
  data() {
    return {
      activeTab: 'financial',
      loading: false,
      total: 0,
      queryForm: {
        analysisType: '',
        importance: '',
        status: '',
        pageNum: 1,
        pageSize: 20
      },
      overviewData: {
        totalTasks: 456,
        runningTasks: 23,
        dataVolume: 125.8,
        todayVolume: 8.9,
        accuracy: 94.2,
        insights: 1856,
        highValueInsights: 234
      },
      dataInsights: []
    }
  },
  mounted() {
    this.loadOverviewData()
    this.loadDataInsights()
    this.initCharts()
  },
  methods: {
    // 加载概览数据
    loadOverviewData() {
      // 模拟API调用
      console.log('加载智能数据分析概览数据')
    },

    // 加载数据洞察
    async loadDataInsights() {
      this.loading = true
      try {
        const response = await this.$http.post('/api/intelligent-ai/analysis/list', this.queryForm)
        if (response.code === 1) {
          this.dataInsights = response.data.list || []
          this.total = response.data.total || 0
        } else {
          this.$message.error(response.msg || '获取智能分析列表失败')
        }
      } catch (error) {
        console.error('获取智能分析列表异常:', error)
        this.$message.error('获取数据失败')
        // 降级到模拟数据
        this.dataInsights = this.generateMockInsights()
        this.total = this.dataInsights.length
      } finally {
        this.loading = false
      }
    },

    // 查询
    handleQuery() {
      this.queryForm.pageNum = 1
      this.loadDataInsights()
    },

    // 重置
    handleReset() {
      this.$refs.queryForm.resetFields()
      this.queryForm = {
        analysisType: '',
        importance: '',
        status: '',
        pageNum: 1,
        pageSize: 20
      }
      this.loadDataInsights()
    },

    // 新增分析
    handleAdd() {
      this.$message.info('新增智能分析任务功能开发中...')
    },

    // 生成模拟洞察数据
    generateMockInsights() {
      const analysisTypes = ['财务分析', '经营分析', '市场分析', '风险分析']
      const importance = ['低', '中', '高', '极高']
      const statuses = ['新发现', '已确认', '处理中', '已处理']
      const dataSources = [
        '财务系统',
        '经营系统',
        '市场数据',
        '风险系统',
        '外部数据'
      ]
      
      const insights = []
      for (let i = 1; i <= 12; i++) {
        const confidence = Math.floor(Math.random() * 30) + 70
        const impact = ['低', '中', '高'][Math.floor(Math.random() * 3)]
        
        insights.push({
          insightId: `INS${new Date().getFullYear()}${String(i).padStart(4, '0')}`,
          dataSource: dataSources[Math.floor(Math.random() * dataSources.length)],
          analysisType: analysisTypes[Math.floor(Math.random() * analysisTypes.length)],
          importance: importance[Math.floor(Math.random() * importance.length)],
          confidence,
          insight: this.generateInsightText(),
          impact,
          discoveredTime: new Date(Date.now() - Math.random() * 3 * 24 * 60 * 60 * 1000).toLocaleString(),
          status: statuses[Math.floor(Math.random() * statuses.length)]
        })
      }
      
      return insights
    },

    // 生成洞察文本
    generateInsightText() {
      const insights = [
        '发现企业现金流存在季节性波动规律，建议优化资金配置',
        '检测到供应链成本上升趋势，建议寻找替代供应商',
        '市场数据显示新兴市场增长潜力巨大，建议加大投入',
        '风险模型识别出潜在信用风险，建议加强风控措施',
        '客户行为分析显示用户偏好变化，建议调整产品策略',
        '竞争对手分析发现市场空白点，建议快速布局'
      ]
      return insights[Math.floor(Math.random() * insights.length)]
    },

    // 初始化图表
    initCharts() {
      // 这里应该使用ECharts初始化图表
      console.log('初始化数据分析图表')
    },

    // 刷新洞察
    refreshInsights() {
      this.loadDataInsights()
      this.$message.success('洞察数据已刷新')
    },

    // 查看洞察详情
    handleView(row) {
      this.$message.info(`查看洞察详情：${row.insightId}`)
    },

    // 导出洞察
    handleExport(row) {
      this.$message.success(`导出洞察报告：${row.insightId}`)
    },

    // 深度分析
    handleDeepAnalysis(row) {
      this.$message.info(`启动深度分析：${row.insightId}`)
    },

    // 获取分析类型颜色
    getAnalysisTypeColor(type) {
      const typeMap = {
        '财务分析': 'primary',
        '经营分析': 'success',
        '市场分析': 'warning',
        '风险分析': 'danger'
      }
      return typeMap[type] || 'info'
    },

    // 获取重要性类型
    getImportanceType(importance) {
      const importanceMap = {
        '低': 'info',
        '中': 'warning',
        '高': 'danger',
        '极高': 'danger'
      }
      return importanceMap[importance] || 'info'
    },

    // 获取影响程度样式
    getImpactClass(impact) {
      const impactMap = {
        '低': 'impact-low',
        '中': 'impact-medium',
        '高': 'impact-high'
      }
      return impactMap[impact] || 'impact-low'
    },

    // 获取置信度颜色
    getConfidenceColor(confidence) {
      if (confidence >= 90) return '#67C23A'
      if (confidence >= 80) return '#409EFF'
      if (confidence >= 70) return '#E6A23C'
      return '#F56C6C'
    },

    // 获取状态类型
    getStatusType(status) {
      const statusMap = {
        '新发现': 'warning',
        '已确认': 'primary',
        '处理中': 'info',
        '已处理': 'success'
      }
      return statusMap[status] || 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
.intelligent-data-analysis {
  padding: 20px;

  .overview-cards {
    margin-bottom: 20px;

    .overview-card {
      height: 120px;
      border: none;
      box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

      &.analysis-gradient {
        background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
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

  .function-card, .chart-section, .insights-card {
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

  .impact-low {
    color: #909399;
  }

  .impact-medium {
    color: #E6A23C;
    font-weight: 500;
  }

  .impact-high {
    color: #F56C6C;
    font-weight: bold;
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
