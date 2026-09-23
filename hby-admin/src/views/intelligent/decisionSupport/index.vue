<template>
  <div class="intelligent-decision-support">
    <!-- 统计概览 -->
    <el-row :gutter="16" class="overview-cards">
      <el-col :span="6">
        <el-card class="overview-card decision-gradient">
          <div class="card-content">
            <div class="card-icon">
              <i class="el-icon-lightbulb"></i>
            </div>
            <div class="card-info">
              <div class="card-title">决策建议</div>
              <div class="card-value">{{ overviewData.totalSuggestions }}条</div>
              <div class="card-desc">采纳率 {{ overviewData.adoptionRate }}%</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card decision-gradient">
          <div class="card-content">
            <div class="card-icon">
              <i class="el-icon-data-line"></i>
            </div>
            <div class="card-info">
              <div class="card-title">决策模型</div>
              <div class="card-value">{{ overviewData.totalModels }}个</div>
              <div class="card-desc">运行中 {{ overviewData.runningModels }}个</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card decision-gradient">
          <div class="card-content">
            <div class="card-icon">
              <i class="el-icon-trend-charts"></i>
            </div>
            <div class="card-info">
              <div class="card-title">预测准确率</div>
              <div class="card-value">{{ overviewData.accuracy }}%</div>
              <div class="card-desc">较上月提升 +2.3%</div>
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
              <div class="card-title">决策效果</div>
              <div class="card-value">{{ overviewData.effectiveness }}%</div>
              <div class="card-desc">优秀等级</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 功能模块 -->
    <el-card class="function-card">
      <div slot="header" class="card-header">
        <span class="card-title">智能决策支持功能</span>
        <div class="card-actions">
          <el-button type="primary" icon="el-icon-plus" size="small">新增决策</el-button>
          <el-button type="success" icon="el-icon-refresh" size="small">刷新分析</el-button>
        </div>
      </div>

      <el-tabs v-model="activeTab" type="border-card">
        <el-tab-pane label="投资决策支持" name="investment">
          <InvestmentDecisionSupport />
        </el-tab-pane>
        <el-tab-pane label="经营决策支持" name="operational">
          <OperationalDecisionSupport />
        </el-tab-pane>
        <el-tab-pane label="战略决策支持" name="strategic">
          <StrategicDecisionSupport />
        </el-tab-pane>
        <el-tab-pane label="风险决策支持" name="risk">
          <RiskDecisionSupport />
        </el-tab-pane>
        <el-tab-pane label="决策模型管理" name="modelManagement">
          <DecisionModelManagement />
        </el-tab-pane>
        <el-tab-pane label="决策效果评估" name="effectiveness">
          <DecisionEffectivenessEvaluation />
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 决策分析图表 -->
    <el-row :gutter="16" class="chart-section">
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header" class="card-header">
            <span class="card-title">决策建议趋势</span>
          </div>
          <div id="decisionTrendChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header" class="card-header">
            <span class="card-title">决策类型分布</span>
          </div>
          <div id="decisionTypeChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 智能决策建议 -->
    <el-card class="suggestions-card">
      <div slot="header" class="card-header">
        <span class="card-title">智能决策建议</span>
        <div class="card-actions">
          <el-tag type="primary" size="small">AI推荐</el-tag>
          <el-button type="text" size="small" @click="refreshSuggestions">刷新</el-button>
        </div>
      </div>
      
      <el-table :data="decisionSuggestions" stripe border>
        <el-table-column prop="suggestionId" label="建议ID" width="120" />
        <el-table-column prop="companyName" label="企业名称" width="200" show-overflow-tooltip />
        <el-table-column prop="decisionType" label="决策类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getDecisionTypeColor(scope.row.decisionType)">{{ scope.row.decisionType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="priority" label="优先级" width="100">
          <template slot-scope="scope">
            <el-tag :type="getPriorityType(scope.row.priority)" size="mini">{{ scope.row.priority }}</el-tag>
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
        <el-table-column prop="suggestion" label="决策建议" show-overflow-tooltip />
        <el-table-column prop="expectedBenefit" label="预期收益" width="120">
          <template slot-scope="scope">
            <span class="benefit-amount">{{ scope.row.expectedBenefit }}万元</span>
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
            <el-button size="mini" type="success" @click="handleAdopt(scope.row)">采纳</el-button>
            <el-button size="mini" type="warning" @click="handleAnalyze(scope.row)">分析</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
// import InvestmentDecisionSupport from './components/InvestmentDecisionSupport.vue'
// import OperationalDecisionSupport from './components/OperationalDecisionSupport.vue'
// import StrategicDecisionSupport from './components/StrategicDecisionSupport.vue'
// import RiskDecisionSupport from './components/RiskDecisionSupport.vue'
// import DecisionModelManagement from './components/DecisionModelManagement.vue'
// import DecisionEffectivenessEvaluation from './components/DecisionEffectivenessEvaluation.vue'

export default {
  name: 'IntelligentDecisionSupport',
  components: {
    // InvestmentDecisionSupport,
    // OperationalDecisionSupport,
    // StrategicDecisionSupport,
    // RiskDecisionSupport,
    // DecisionModelManagement,
    // DecisionEffectivenessEvaluation
  },
  data() {
    return {
      activeTab: 'investment',
      overviewData: {
        totalSuggestions: 2856,
        adoptionRate: 78.5,
        totalModels: 32,
        runningModels: 24,
        accuracy: 91.2,
        effectiveness: 87.8
      },
      decisionSuggestions: []
    }
  },
  mounted() {
    this.loadOverviewData()
    this.loadDecisionSuggestions()
    this.initCharts()
  },
  methods: {
    // 加载概览数据
    loadOverviewData() {
      // 模拟API调用
      console.log('加载智能决策支持概览数据')
    },

    // 加载决策建议数据
    loadDecisionSuggestions() {
      // 生成模拟数据
      this.decisionSuggestions = this.generateMockSuggestions()
    },

    // 生成模拟决策建议数据
    generateMockSuggestions() {
      const decisionTypes = ['投资决策', '经营决策', '战略决策', '风险决策']
      const priorities = ['低', '中', '高', '紧急']
      const statuses = ['待审核', '已采纳', '已拒绝', '执行中']
      const companies = [
        '示例云科技有限公司',
        '国资投资控股集团',
        '中央企业发展公司',
        '国有资产管理公司',
        '央企科技创新公司'
      ]
      
      const suggestions = []
      for (let i = 1; i <= 15; i++) {
        const aiScore = Math.floor(Math.random() * 40) + 60
        const confidence = Math.floor(Math.random() * 30) + 70
        const expectedBenefit = (Math.random() * 5000 + 1000).toFixed(1)
        
        suggestions.push({
          suggestionId: `DS${new Date().getFullYear()}${String(i).padStart(4, '0')}`,
          companyName: companies[Math.floor(Math.random() * companies.length)],
          decisionType: decisionTypes[Math.floor(Math.random() * decisionTypes.length)],
          priority: priorities[Math.floor(Math.random() * priorities.length)],
          aiScore,
          confidence,
          suggestion: this.generateSuggestionText(),
          expectedBenefit,
          generatedTime: new Date(Date.now() - Math.random() * 7 * 24 * 60 * 60 * 1000).toLocaleString(),
          status: statuses[Math.floor(Math.random() * statuses.length)]
        })
      }
      
      return suggestions
    },

    // 生成建议文本
    generateSuggestionText() {
      const suggestions = [
        '建议增加对新兴技术领域的投资，预期可提升企业竞争力',
        '建议优化供应链管理，降低运营成本约15%',
        '建议调整产品结构，重点发展高附加值产品线',
        '建议加强风险管控，建立完善的风险预警机制',
        '建议扩大市场份额，通过并购整合实现规模效应',
        '建议数字化转型，提升运营效率和客户体验'
      ]
      return suggestions[Math.floor(Math.random() * suggestions.length)]
    },

    // 初始化图表
    initCharts() {
      // 这里应该使用ECharts初始化图表
      console.log('初始化决策分析图表')
    },

    // 刷新建议
    refreshSuggestions() {
      this.loadDecisionSuggestions()
      this.$message.success('决策建议已刷新')
    },

    // 查看建议详情
    handleView(row) {
      this.$message.info(`查看决策建议详情：${row.suggestionId}`)
    },

    // 采纳建议
    handleAdopt(row) {
      this.$message.success(`已采纳决策建议：${row.suggestionId}`)
    },

    // 分析建议
    handleAnalyze(row) {
      this.$message.info(`深度分析决策建议：${row.suggestionId}`)
    },

    // 获取决策类型颜色
    getDecisionTypeColor(type) {
      const typeMap = {
        '投资决策': 'primary',
        '经营决策': 'success',
        '战略决策': 'warning',
        '风险决策': 'danger'
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
        '待审核': 'warning',
        '已采纳': 'success',
        '已拒绝': 'danger',
        '执行中': 'primary'
      }
      return statusMap[status] || 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
.intelligent-decision-support {
  padding: 20px;

  .overview-cards {
    margin-bottom: 20px;

    .overview-card {
      height: 120px;
      border: none;
      box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

      &.decision-gradient {
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
