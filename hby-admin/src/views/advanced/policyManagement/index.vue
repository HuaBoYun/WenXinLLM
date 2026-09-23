<template>
  <div class="advanced-policy-management">
    <!-- 统计概览 -->
    <el-row :gutter="16" class="overview-cards">
      <el-col :span="6">
        <el-card class="overview-card policy-gradient">
          <div class="card-content">
            <div class="card-icon">
              <i class="el-icon-document"></i>
            </div>
            <div class="card-info">
              <div class="card-title">政策文件</div>
              <div class="card-value">{{ overviewData.totalPolicies }}份</div>
              <div class="card-desc">有效政策 {{ overviewData.activePolicies }}份</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card policy-gradient">
          <div class="card-content">
            <div class="card-icon">
              <i class="el-icon-bell"></i>
            </div>
            <div class="card-info">
              <div class="card-title">政策更新</div>
              <div class="card-value">{{ overviewData.recentUpdates }}条</div>
              <div class="card-desc">本月新增 {{ overviewData.monthlyNew }}条</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card policy-gradient">
          <div class="card-content">
            <div class="card-icon">
              <i class="el-icon-check"></i>
            </div>
            <div class="card-info">
              <div class="card-title">执行率</div>
              <div class="card-value">{{ overviewData.executionRate }}%</div>
              <div class="card-desc">较上月提升 +2.1%</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="overview-card policy-gradient">
          <div class="card-content">
            <div class="card-icon">
              <i class="el-icon-warning"></i>
            </div>
            <div class="card-info">
              <div class="card-title">合规风险</div>
              <div class="card-value">{{ overviewData.complianceRisks }}项</div>
              <div class="card-desc">高风险 {{ overviewData.highRisks }}项</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 功能模块 -->
    <el-card class="function-card">
      <div slot="header" class="card-header">
        <span class="card-title">政策管理功能</span>
        <div class="card-actions">
          <el-button type="primary" icon="el-icon-plus" size="small">新增政策</el-button>
          <el-button type="success" icon="el-icon-refresh" size="small">同步更新</el-button>
        </div>
      </div>

      <el-tabs v-model="activeTab" type="border-card">
        <el-tab-pane label="政策文件管理" name="documents">
          <PolicyDocumentManagement />
        </el-tab-pane>
        <el-tab-pane label="政策解读分析" name="interpretation">
          <PolicyInterpretationAnalysis />
        </el-tab-pane>
        <el-tab-pane label="政策执行监控" name="execution">
          <PolicyExecutionMonitoring />
        </el-tab-pane>
        <el-tab-pane label="合规性检查" name="compliance">
          <ComplianceChecking />
        </el-tab-pane>
        <el-tab-pane label="政策影响评估" name="impact">
          <PolicyImpactAssessment />
        </el-tab-pane>
        <el-tab-pane label="政策预警提醒" name="alerts">
          <PolicyAlertsReminders />
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 政策分析图表 -->
    <el-row :gutter="16" class="chart-section">
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header" class="card-header">
            <span class="card-title">政策执行趋势</span>
          </div>
          <div id="policyExecutionChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card">
          <div slot="header" class="card-header">
            <span class="card-title">政策类型分布</span>
          </div>
          <div id="policyTypeChart" style="height: 300px;"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 政策清单 -->
    <el-card class="policies-card">
      <div slot="header" class="card-header">
        <span class="card-title">政策清单</span>
        <div class="card-actions">
          <el-input
            v-model="searchKeyword"
            placeholder="搜索政策"
            size="small"
            style="width: 200px; margin-right: 10px;"
            prefix-icon="el-icon-search"
            @input="handleSearch"
          />
          <el-button type="text" size="small" @click="refreshPolicies">刷新</el-button>
        </div>
      </div>
      
      <el-table :data="filteredPolicies" stripe border>
        <el-table-column prop="policyId" label="政策编号" width="120" />
        <el-table-column prop="policyName" label="政策名称" width="250" show-overflow-tooltip />
        <el-table-column prop="policyType" label="政策类型" width="120">
          <template slot-scope="scope">
            <el-tag :type="getPolicyTypeColor(scope.row.policyType)">{{ scope.row.policyType }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="level" label="政策级别" width="100">
          <template slot-scope="scope">
            <el-tag :type="getLevelType(scope.row.level)" size="mini">{{ scope.row.level }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="issueDate" label="发布日期" width="120" />
        <el-table-column prop="effectiveDate" label="生效日期" width="120" />
        <el-table-column prop="executionRate" label="执行率" width="100">
          <template slot-scope="scope">
            <el-progress :percentage="scope.row.executionRate" :color="getExecutionColor(scope.row.executionRate)" />
          </template>
        </el-table-column>
        <el-table-column prop="complianceStatus" label="合规状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getComplianceType(scope.row.complianceStatus)">{{ scope.row.complianceStatus }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="summary" label="政策摘要" show-overflow-tooltip />
        <el-table-column prop="status" label="状态" width="100">
          <template slot-scope="scope">
            <el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="success" @click="handleAnalyze(scope.row)">解读</el-button>
            <el-button size="mini" type="warning" @click="handleMonitor(scope.row)">监控</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>
  </div>
</template>

<script>
// import PolicyDocumentManagement from './components/PolicyDocumentManagement.vue'
// import PolicyInterpretationAnalysis from './components/PolicyInterpretationAnalysis.vue'
// import PolicyExecutionMonitoring from './components/PolicyExecutionMonitoring.vue'
// import ComplianceChecking from './components/ComplianceChecking.vue'
// import PolicyImpactAssessment from './components/PolicyImpactAssessment.vue'
// import PolicyAlertsReminders from './components/PolicyAlertsReminders.vue'

export default {
  name: 'AdvancedPolicyManagement',
  components: {
    // PolicyDocumentManagement,
    // PolicyInterpretationAnalysis,
    // PolicyExecutionMonitoring,
    // ComplianceChecking,
    // PolicyImpactAssessment,
    // PolicyAlertsReminders
  },
  data() {
    return {
      activeTab: 'documents',
      searchKeyword: '',
      overviewData: {
        totalPolicies: 856,
        activePolicies: 734,
        recentUpdates: 45,
        monthlyNew: 12,
        executionRate: 92.3,
        complianceRisks: 23,
        highRisks: 5
      },
      policies: [],
      filteredPolicies: []
    }
  },
  mounted() {
    this.loadOverviewData()
    this.loadPolicies()
    this.initCharts()
  },
  methods: {
    // 加载概览数据
    loadOverviewData() {
      // 模拟API调用
      console.log('加载政策管理概览数据')
    },

    // 加载政策数据
    loadPolicies() {
      // 生成模拟数据
      this.policies = this.generateMockPolicies()
      this.filteredPolicies = [...this.policies]
    },

    // 生成模拟政策数据
    generateMockPolicies() {
      const policyTypes = ['财政政策', '货币政策', '产业政策', '监管政策']
      const levels = ['国家级', '部委级', '省级', '市级']
      const complianceStatuses = ['合规', '部分合规', '不合规', '待评估']
      const statuses = ['有效', '即将到期', '已失效', '草案']
      const policyNames = [
        '国有企业改革深化实施方案',
        '国资监管体制改革指导意见',
        '央企重组整合管理办法',
        '国有资产保值增值考核办法',
        '企业投资项目核准管理规定',
        '国有企业负责人薪酬制度改革方案'
      ]
      
      const policies = []
      for (let i = 1; i <= 15; i++) {
        const executionRate = Math.floor(Math.random() * 30) + 70
        const issueDate = new Date(Date.now() - Math.random() * 365 * 24 * 60 * 60 * 1000)
        const effectiveDate = new Date(issueDate.getTime() + 30 * 24 * 60 * 60 * 1000)
        
        policies.push({
          policyId: `POL${new Date().getFullYear()}${String(i).padStart(4, '0')}`,
          policyName: policyNames[Math.floor(Math.random() * policyNames.length)],
          policyType: policyTypes[Math.floor(Math.random() * policyTypes.length)],
          level: levels[Math.floor(Math.random() * levels.length)],
          issueDate: issueDate.toLocaleDateString(),
          effectiveDate: effectiveDate.toLocaleDateString(),
          executionRate,
          complianceStatus: complianceStatuses[Math.floor(Math.random() * complianceStatuses.length)],
          summary: this.generatePolicySummary(),
          status: statuses[Math.floor(Math.random() * statuses.length)]
        })
      }
      
      return policies
    },

    // 生成政策摘要
    generatePolicySummary() {
      const summaries = [
        '深化国有企业改革，完善现代企业制度，提升企业竞争力',
        '加强国资监管，防范国有资产流失，提高监管效率',
        '推进央企重组整合，优化资源配置，实现协同发展',
        '建立科学的考核体系，促进国有资产保值增值',
        '规范投资项目管理，防控投资风险，提高投资效益',
        '完善薪酬激励机制，激发企业活力，留住优秀人才'
      ]
      return summaries[Math.floor(Math.random() * summaries.length)]
    },

    // 搜索政策
    handleSearch() {
      if (!this.searchKeyword.trim()) {
        this.filteredPolicies = [...this.policies]
      } else {
        this.filteredPolicies = this.policies.filter(policy =>
          policy.policyName.includes(this.searchKeyword) ||
          policy.policyType.includes(this.searchKeyword) ||
          policy.summary.includes(this.searchKeyword)
        )
      }
    },

    // 初始化图表
    initCharts() {
      // 这里应该使用ECharts初始化图表
      console.log('初始化政策分析图表')
    },

    // 刷新政策
    refreshPolicies() {
      this.loadPolicies()
      this.$message.success('政策数据已刷新')
    },

    // 查看政策详情
    handleView(row) {
      this.$message.info(`查看政策详情：${row.policyId}`)
    },

    // 政策解读
    handleAnalyze(row) {
      this.$message.info(`政策解读：${row.policyId}`)
    },

    // 监控政策执行
    handleMonitor(row) {
      this.$message.info(`监控政策执行：${row.policyId}`)
    },

    // 获取政策类型颜色
    getPolicyTypeColor(type) {
      const typeMap = {
        '财政政策': 'primary',
        '货币政策': 'success',
        '产业政策': 'warning',
        '监管政策': 'danger'
      }
      return typeMap[type] || 'info'
    },

    // 获取级别类型
    getLevelType(level) {
      const levelMap = {
        '国家级': 'danger',
        '部委级': 'warning',
        '省级': 'primary',
        '市级': 'info'
      }
      return levelMap[level] || 'info'
    },

    // 获取执行率颜色
    getExecutionColor(rate) {
      if (rate >= 90) return '#67C23A'
      if (rate >= 80) return '#409EFF'
      if (rate >= 70) return '#E6A23C'
      return '#F56C6C'
    },

    // 获取合规状态类型
    getComplianceType(status) {
      const statusMap = {
        '合规': 'success',
        '部分合规': 'warning',
        '不合规': 'danger',
        '待评估': 'info'
      }
      return statusMap[status] || 'info'
    },

    // 获取状态类型
    getStatusType(status) {
      const statusMap = {
        '有效': 'success',
        '即将到期': 'warning',
        '已失效': 'danger',
        '草案': 'info'
      }
      return statusMap[status] || 'info'
    }
  }
}
</script>

<style lang="scss" scoped>
.advanced-policy-management {
  padding: 20px;

  .overview-cards {
    margin-bottom: 20px;

    .overview-card {
      height: 120px;
      border: none;
      box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);

      &.policy-gradient {
        background: linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%);
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

  .function-card, .chart-section, .policies-card {
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

  ::v-deep .el-tabs__content {
    padding: 20px;
    min-height: 400px;
  }

  ::v-deep .el-card__body {
    padding: 20px;
  }
}
</style>
