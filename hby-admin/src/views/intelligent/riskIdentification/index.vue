<template>
  <div class="intelligent-risk-identification">
    <!-- 统计概览 -->
    <el-row :gutter="16" class="overview-cards">
      <el-col :span="6">
        <el-card class="overview-card ai-gradient">
          <div class="card-content">
            <div class="card-icon">
              <i class="el-icon-warning"></i>
            </div>
            <div class="card-info">
              <div class="card-title">AI风险识别</div>
              <div class="card-value">{{ overviewData.totalRisks }}项</div>
              <div class="card-desc">识别准确率 {{ overviewData.accuracy }}%</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card ai-gradient">
          <div class="card-content">
            <div class="card-icon">
              <i class="el-icon-cpu"></i>
            </div>
            <div class="card-info">
              <div class="card-title">AI模型数量</div>
              <div class="card-value">{{ overviewData.totalModels }}个</div>
              <div class="card-desc">活跃模型 {{ overviewData.activeModels }}个</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card ai-gradient">
          <div class="card-content">
            <div class="card-icon">
              <i class="el-icon-data-analysis"></i>
            </div>
            <div class="card-info">
              <div class="card-title">数据处理量</div>
              <div class="card-value">{{ overviewData.dataVolume }}万条</div>
              <div class="card-desc">今日新增 {{ overviewData.todayIncrease }}万条</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card ai-gradient">
          <div class="card-content">
            <div class="card-icon">
              <i class="el-icon-medal"></i>
            </div>
            <div class="card-info">
              <div class="card-title">预警成功率</div>
              <div class="card-value">{{ overviewData.successRate }}%</div>
              <div class="card-desc">优秀等级</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 功能模块 -->
    <el-card class="function-card">
      <div slot="header" class="card-header">
        <span class="card-title">智能风险识别功能</span>
        <div class="card-actions">
          <el-button type="primary" icon="el-icon-plus" size="small">新增模型</el-button>
          <el-button type="success" icon="el-icon-refresh" size="small">刷新分析</el-button>
        </div>
      </div>

      <el-tabs v-model="activeTab" type="border-card">
        <el-tab-pane label="财务风险识别" name="financial">
          <FinancialRiskIdentification />
        </el-tab-pane>
        <el-tab-pane label="经营风险识别" name="operational">
          <OperationalRiskIdentification />
        </el-tab-pane>
        <el-tab-pane label="合规风险识别" name="compliance">
          <ComplianceRiskIdentification />
        </el-tab-pane>
        <el-tab-pane label="市场风险识别" name="market">
          <MarketRiskIdentification />
        </el-tab-pane>
        <el-tab-pane label="AI模型管理" name="modelManagement">
          <AIModelManagement />
        </el-tab-pane>
        <el-tab-pane label="风险预警配置" name="alertConfig">
          <RiskAlertConfiguration />
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- AI风险分析图表 -->
    <el-row :gutter="16" class="chart-section">
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header" class="card-header">
            <span class="card-title">风险趋势分析</span>
          </div>
          <div id="riskTrendChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header" class="card-header">
            <span class="card-title">风险类型分布</span>
          </div>
          <div id="riskTypeChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 实时风险监控 -->
    <el-card class="monitor-card">
      <div slot="header" class="card-header">
        <span class="card-title">实时风险监控</span>
        <div class="card-actions">
          <el-tag type="success" size="small">AI监控中</el-tag>
          <el-button type="text" size="small" @click="refreshMonitor">刷新</el-button>
        </div>
      </div>
      
      <el-table :data="realtimeRisks" stripe border>
        <el-table-column prop="riskId" label="风险ID" width="120" />
        <el-table-column prop="companyName" label="企业名称" width="200" show-overflow-tooltip />
        <el-table-column prop="riskType" label="风险类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getRiskTypeColor(scope.row.riskType)">{{ scope.row.riskType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="riskLevel" label="风险等级" width="100">
          <template slot-scope="scope">
            <el-tag :type="getRiskLevelType(scope.row.riskLevel)" size="mini">{{ scope.row.riskLevel }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="aiScore" label="AI评分" width="100">
          <template slot-scope="scope">
            <span :class="getScoreClass(scope.row.aiScore)">{{ scope.row.aiScore }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="confidence" label="置信度" width="100">
          <template slot-scope="scope">
            <el-progress :percentage="scope.row.confidence" :color="getConfidenceColor(scope.row.confidence)" />
          </template>
        </el-table-column>
        <el-table-column prop="riskDescription" label="风险描述" show-overflow-tooltip />
        <el-table-column prop="detectedTime" label="识别时间" width="150" />
        <el-table-column prop="status" label="处理状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="warning" @click="handleProcess(scope.row)">处理</el-button>
            <el-button size="mini" type="success" @click="handleAnalyze(scope.row)">分析</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
// import FinancialRiskIdentification from './components/FinancialRiskIdentification.vue'
// import OperationalRiskIdentification from './components/OperationalRiskIdentification.vue'
// import ComplianceRiskIdentification from './components/ComplianceRiskIdentification.vue'
// import MarketRiskIdentification from './components/MarketRiskIdentification.vue'
// import AIModelManagement from './components/AIModelManagement.vue'
// import RiskAlertConfiguration from './components/RiskAlertConfiguration.vue'

export default {
  name: 'IntelligentRiskIdentification',
  components: {
    // FinancialRiskIdentification,
    // OperationalRiskIdentification,
    // ComplianceRiskIdentification,
    // MarketRiskIdentification,
    // AIModelManagement,
    // RiskAlertConfiguration
  },
  data() {
    return {
      activeTab: 'financial',
      overviewData: {
        totalRisks: 1256,
        accuracy: 92.8,
        totalModels: 24,
        activeModels: 18,
        dataVolume: 8945.2,
        todayIncrease: 156.7,
        successRate: 89.5
      },
      realtimeRisks: []
    }
  },
  mounted() {
    this.loadOverviewData()
    this.loadRealtimeRisks()
    this.initCharts()
  },
  methods: {
    // 加载概览数据
    loadOverviewData() {
      // 模拟API调用
      console.log('加载智能风险识别概览数据')
    },

    // 加载实时风险数据
    loadRealtimeRisks() {
      // 生成模拟数据
      this.realtimeRisks = this.generateMockRisks()
    },

    // 生成模拟风险数据
    generateMockRisks() {
      const riskTypes = ['财务风险', '经营风险', '合规风险', '市场风险']
      const riskLevels = ['低', '中', '高', '极高']
      const statuses = ['待处理', '处理中', '已处理', '已忽略']
      const companies = [
        '示例云科技有限公司',
        '国资投资控股集团',
        '中央企业发展公司',
        '国有资产管理公司',
        '央企科技创新公司'
      ]
      
      const risks = []
      for (let i = 1; i <= 20; i++) {
        const aiScore = Math.floor(Math.random() * 40) + 60
        const confidence = Math.floor(Math.random() * 30) + 70
        
        risks.push({
          riskId: `RISK${new Date().getFullYear()}${String(i).padStart(4, '0')}`,
          companyName: companies[Math.floor(Math.random() * companies.length)],
          riskType: riskTypes[Math.floor(Math.random() * riskTypes.length)],
          riskLevel: riskLevels[Math.floor(Math.random() * riskLevels.length)],
          aiScore,
          confidence,
          riskDescription: this.generateRiskDescription(),
          detectedTime: new Date(Date.now() - Math.random() * 24 * 60 * 60 * 1000).toLocaleString(),
          status: statuses[Math.floor(Math.random() * statuses.length)]
        })
      }
      
      return risks
    },

    // 生成风险描述
    generateRiskDescription() {
      const descriptions = [
        'AI检测到异常财务指标波动，建议关注现金流状况',
        '经营数据显示业务增长放缓，存在市场竞争风险',
        '合规检查发现潜在违规操作，需要进一步核实',
        '市场分析显示行业下行趋势，影响企业盈利能力',
        '财务杠杆率超出安全阈值，存在债务风险',
        '关联交易频繁且金额较大，存在利益输送风险'
      ]
      return descriptions[Math.floor(Math.random() * descriptions.length)]
    },

    // 初始化图表
    initCharts() {
      // 这里应该使用ECharts初始化图表
      console.log('初始化AI风险分析图表')
    },

    // 刷新监控
    refreshMonitor() {
      this.loadRealtimeRisks()
      this.$message.success('监控数据已刷新')
    },

    // 查看风险详情
    handleView(row) {
      this.$message.info(`查看风险详情：${row.riskId}`)
    },

    // 处理风险
    handleProcess(row) {
      this.$message.info(`处理风险：${row.riskId}`)
    },

    // 分析风险
    handleAnalyze(row) {
      this.$message.info(`深度分析风险：${row.riskId}`)
    },

    // 获取风险类型颜色
    getRiskTypeColor(type) {
      const typeMap = {
        '财务风险': 'danger',
        '经营风险': 'warning',
        '合规风险': 'primary',
        '市场风险': 'info'
      }
      return typeMap[type] || 'info'
    },

    // 获取风险等级类型
    getRiskLevelType(level) {
      const levelMap = {
        '低': 'success',
        '中': 'warning',
        '高': 'danger',
        '极高': 'danger'
      }
      return levelMap[level] || 'info'
    },

    // 获取评分样式
    getScoreClass(score) {
      if (score >= 90) return 'score-excellent'
      if (score >= 80) return 'score-good'
      if (score >= 70) return 'score-normal'
      return 'score-poor'
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
        '待处理': 'warning',
        '处理中': 'primary',
        '已处理': 'success',
        '已忽略': 'info'
      }
      return statusMap[status] || 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
.intelligent-risk-identification {
  padding: 20px;

  .overview-cards {
    margin-bottom: 20px;

    .overview-card {
      height: 120px;
      border: none;
      box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

      &.ai-gradient {
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

  .function-card, .chart-section, .monitor-card {
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

  .score-excellent {
    color: #67C23A;
    font-weight: bold;
    font-size: 16px;
  }

  .score-good {
    color: #409EFF;
    font-weight: 500;
  }

  .score-normal {
    color: #E6A23C;
  }

  .score-poor {
    color: #F56C6C;
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
