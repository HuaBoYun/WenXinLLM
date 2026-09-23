<template>
  <div class="enterprise-risk" :style="themeVars">
    <!-- 企业选择 -->
    <el-card class="mb-20">
      <el-row :gutter="20" type="flex" justify="space-between" align="middle">
        <el-col :span="12">
          <div class="enterprise-selector">
            <el-select
              v-model="selectedEnterpriseId"
              placeholder="请选择企业"
              style="width: 300px;"
              @change="handleEnterpriseChange"
              filterable
            >
              <el-option
                v-for="item in enterpriseList"
                :key="item.enterpriseId"
                :label="item.enterpriseName"
                :value="item.enterpriseId"
              />
            </el-select>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <!-- 风险概览统计 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon total-risks">
              <i class="el-icon-warning-outline"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">{{ statistics.totalRisks || 0 }}</div>
              <div class="statistics-label">识别风险总数</div>
              <div class="statistics-trend">
                <span>本月新增: {{ statistics.newRisks || 0 }}个</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon high-risks">
              <i class="el-icon-error"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">{{ statistics.highRisks || 0 }}</div>
              <div class="statistics-label">高风险事项</div>
              <div class="statistics-trend">
                <span>占比: {{ statistics.highRiskRatio || 0 }}%</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon control-measures">
              <i class="el-icon-shield"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">{{ statistics.controlMeasures || 0 }}</div>
              <div class="statistics-label">控制措施数</div>
              <div class="statistics-trend">
                <span>有效率: {{ statistics.effectiveRate || 0 }}%</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon risk-score">
              <i class="el-icon-data-line"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">{{ statistics.riskScore || 0 }}</div>
              <div class="statistics-label">综合风险评分</div>
              <div class="statistics-trend" :class="getRiskScoreClass(statistics.riskScore)">
                <span>{{ getRiskScoreText(statistics.riskScore) }}</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 风险预警信息 -->
    <el-card class="mb-20" v-if="warnings.length > 0">
      <div slot="header" class="card-header">
        <span class="card-title">风险预警</span>
        <el-tag type="danger">{{ warnings.length }}个预警</el-tag>
      </div>
      <div class="warning-list">
        <el-alert
          v-for="warning in warnings"
          :key="warning.warningId"
          :title="warning.warningTitle"
          :description="warning.warningDesc"
          :type="getWarningType(warning.warningLevel)"
          :closable="false"
          show-icon
          class="mb-10"
        >
          <template slot="title">
            <span>{{ warning.warningTitle }}</span>
            <el-tag size="mini" :type="getWarningLevelTag(warning.warningLevel)" style="margin-left: 10px;">
              {{ getWarningLevelText(warning.warningLevel) }}
            </el-tag>
            <span class="warning-time">{{ warning.warningTime }}</span>
          </template>
        </el-alert>
      </div>
    </el-card>

    <!-- 风险矩阵图 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="12">
        <el-card>
          <div slot="header" class="card-header">
            <span class="card-title">风险矩阵图</span>
            <el-button size="mini" @click="refreshRiskMatrix">刷新</el-button>
          </div>
          <div class="risk-matrix-container">
            <div ref="riskMatrixChart" class="risk-matrix-chart"></div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card>
          <div slot="header" class="card-header">
            <span class="card-title">风险分布统计</span>
            <el-button size="mini" @click="refreshRiskDistribution">刷新</el-button>
          </div>
          <div class="risk-distribution-container">
            <div ref="riskDistributionChart" class="risk-distribution-chart"></div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 功能模块标签页 -->
    <el-card>
      <el-tabs v-model="activeTab" @tab-click="handleTabClick">
        <!-- 风险评估 -->
        <el-tab-pane label="风险评估" name="assessment">
          <RiskAssessmentTab
            :enterprise-id="selectedEnterpriseId"
            :enterprise-name="selectedEnterpriseName"
            @refresh="loadStatistics"
          />
        </el-tab-pane>

        <!-- 风险识别 -->
        <el-tab-pane label="风险识别" name="identification">
          <RiskIdentificationTab
            :enterprise-id="selectedEnterpriseId"
            :enterprise-name="selectedEnterpriseName"
            @refresh="loadStatistics"
          />
        </el-tab-pane>

        <!-- 控制措施 -->
        <el-tab-pane label="控制措施" name="control">
          <RiskControlTab
            :enterprise-id="selectedEnterpriseId"
            :enterprise-name="selectedEnterpriseName"
            @refresh="loadStatistics"
          />
        </el-tab-pane>

        <!-- 风险监控 -->
        <el-tab-pane label="风险监控" name="monitoring">
          <RiskMonitoringTab
            :enterprise-id="selectedEnterpriseId"
            :enterprise-name="selectedEnterpriseName"
            @refresh="loadStatistics"
          />
        </el-tab-pane>

        <!-- 风险事件 -->
        <el-tab-pane label="风险事件" name="incident">
          <RiskIncidentTab
            :enterprise-id="selectedEnterpriseId"
            :enterprise-name="selectedEnterpriseName"
            @refresh="loadStatistics"
          />
        </el-tab-pane>

        <!-- 风险报告 -->
        <el-tab-pane label="风险报告" name="report">
          <RiskReportTab
            :enterprise-id="selectedEnterpriseId"
            :enterprise-name="selectedEnterpriseName"
            @refresh="loadStatistics"
          />
        </el-tab-pane>

        <!-- 风险知识库 -->
        <el-tab-pane label="风险知识库" name="knowledge">
          <RiskKnowledgeTab
            :enterprise-id="selectedEnterpriseId"
            :enterprise-name="selectedEnterpriseName"
            @refresh="loadStatistics"
          />
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script>
import {
  getRiskStatistics,
  getRiskWarningList,
  getRiskMatrix,
  getRiskDistributionAnalysis,
  extractData
} from '@/api/enterprise/risk'
import { getEnterpriseList } from '@/api/stateAssets/enterprise'
import RiskAssessmentTab from './components/RiskAssessmentTab'
import RiskIdentificationTab from './components/RiskIdentificationTab'
import RiskControlTab from './components/RiskControlTab'
import RiskMonitoringTab from './components/RiskMonitoringTab'
import RiskIncidentTab from './components/RiskIncidentTab'
import RiskReportTab from './components/RiskReportTab'
import RiskKnowledgeTab from './components/RiskKnowledgeTab'
import { investThemeMixin } from '../../stateAssets/themeMixin'

export default {
  name: 'EnterpriseRisk',
  mixins: [investThemeMixin],
  components: {
    RiskAssessmentTab,
    RiskIdentificationTab,
    RiskControlTab,
    RiskMonitoringTab,
    RiskIncidentTab,
    RiskReportTab,
    RiskKnowledgeTab
  },
  data() {
    return {
      selectedEnterpriseId: '',
      selectedEnterpriseName: '',
      enterpriseList: [],
      activeTab: 'assessment',
      statistics: {},
      warnings: [],
      riskMatrixChart: null,
      riskDistributionChart: null
    }
  },
  mounted() {
    // 加载企业列表
    this.loadEnterpriseList()
    // 从路由参数获取默认标签页
    if (this.$route.query.tab) {
      this.activeTab = this.$route.query.tab
    }
  },
  methods: {
    // 加载企业列表
    async loadEnterpriseList() {
      try {
        const res = await getEnterpriseList({ pageNumber: 1, pageSize: 100 })
        const data = extractData(res)
        this.enterpriseList = (data && data.tlist) || []
        // 从路由参数获取企业ID，否则默认选第一个
        if (this.$route.query.enterpriseId) {
          this.selectedEnterpriseId = this.$route.query.enterpriseId
          this.selectedEnterpriseName = this.$route.query.enterpriseName || ''
        } else if (this.enterpriseList.length > 0) {
          this.selectedEnterpriseId = this.enterpriseList[0].enterpriseId
          this.selectedEnterpriseName = this.enterpriseList[0].enterpriseName
        }
        if (this.selectedEnterpriseId) {
          this.loadStatistics()
          this.loadWarnings()
          this.$nextTick(() => { this.initCharts() })
        }
      } catch (e) {
        console.error('加载企业列表失败:', e)
      }
    },

    // 企业选择变化
    handleEnterpriseChange(val) {
      const ent = this.enterpriseList.find(e => e.enterpriseId === val)
      if (ent) this.selectedEnterpriseName = ent.enterpriseName
      if (this.selectedEnterpriseId) {
        this.loadStatistics()
        this.loadWarnings()
        this.refreshCharts()
      }
    },

    // 加载统计数据
    async loadStatistics() {
      if (!this.selectedEnterpriseId) return
      try {
        const response = await getRiskStatistics({ enterpriseId: this.selectedEnterpriseId })
        this.statistics = extractData(response) || {}
      } catch (error) {
        console.error('加载风险统计数据失败:', error)
      }
    },

    // 加载预警信息
    async loadWarnings() {
      if (!this.selectedEnterpriseId) return
      try {
        const response = await getRiskWarningList({
          enterpriseId: this.selectedEnterpriseId,
          warningStatus: 'ACTIVE',
          pageNumber: 1,
          pageSize: 10
        })
        const data = extractData(response)
        this.warnings = (data && (data.tlist || data.records)) || []
      } catch (error) {
        console.error('加载风险预警数据失败:', error)
      }
    },

    // 初始化图表
    initCharts() {
      this.initRiskMatrixChart()
      this.initRiskDistributionChart()
    },

    // 初始化风险矩阵图
    async initRiskMatrixChart() {
      if (!this.selectedEnterpriseId) return

      try {
        const echarts = await import('echarts')
        this.riskMatrixChart = echarts.init(this.$refs.riskMatrixChart)
        this.loadRiskMatrixChart()
      } catch (error) {
        console.error('初始化风险矩阵图失败:', error)
      }
    },

    // 初始化风险分布图
    async initRiskDistributionChart() {
      if (!this.selectedEnterpriseId) return

      try {
        const echarts = await import('echarts')
        this.riskDistributionChart = echarts.init(this.$refs.riskDistributionChart)
        this.loadRiskDistributionChart()
      } catch (error) {
        console.error('初始化风险分布图失败:', error)
      }
    },

    // 加载风险矩阵图数据
    async loadRiskMatrixChart() {
      if (!this.riskMatrixChart || !this.selectedEnterpriseId) return
      try {
        const response = await getRiskMatrix({ enterpriseId: this.selectedEnterpriseId })
        const data = extractData(response)
        const matrixData = (data && data.matrixData) || []
        // 转换为 echarts 格式
        const scatterData = matrixData.map(item => {
          const arr = Array.isArray(item) ? item : [item[0], item[1], item[2], item[3]]
          return arr
        })
        const option = {
          title: { text: '风险矩阵图', left: 'center', textStyle: { fontSize: 14 } },
          tooltip: {
            trigger: 'item',
            formatter: function(p) {
              const probLabels = ['低', '中', '高']
              const impactLabels = ['低', '中', '高']
              return '可能性: ' + probLabels[p.data[0]] + '<br/>影响程度: ' + impactLabels[p.data[1]] + '<br/>数量: ' + p.data[2]
            }
          },
          xAxis: { type: 'category', data: ['低', '中', '高'], name: '可能性', nameLocation: 'middle', nameGap: 25 },
          yAxis: { type: 'category', data: ['低', '中', '高'], name: '影响程度', nameLocation: 'middle', nameGap: 35 },
          series: [{
            type: 'scatter',
            data: scatterData,
            symbolSize: function(val) { return val[2] * 30 + 20 },
            itemStyle: {
              color: function(params) {
                const colors = ['#67C23A', '#E6A23C', '#F56C6C']
                return colors[params.data[3]] || '#909399'
              }
            },
            label: { show: true, formatter: function(p) { return p.data[2] }, position: 'inside' }
          }]
        }
        this.riskMatrixChart.setOption(option)
      } catch (error) {
        console.error('加载风险矩阵图数据失败:', error)
      }
    },

    // 加载风险分布图数据
    async loadRiskDistributionChart() {
      if (!this.riskDistributionChart || !this.selectedEnterpriseId) return
      try {
        const response = await getRiskDistributionAnalysis({ enterpriseId: this.selectedEnterpriseId })
        const data = extractData(response)
        const distributionData = (data && data.distributionData) || []
        const colorMap = { '财务风险': '#F56C6C', '运营风险': '#E6A23C', '市场风险': '#409EFF', '技术风险': '#909399', '合规风险': '#67C23A' }
        const option = {
          title: { text: '风险类型分布', left: 'center', textStyle: { fontSize: 14 } },
          tooltip: { trigger: 'item', formatter: '{a} <br/>{b}: {c} ({d}%)' },
          legend: { orient: 'vertical', left: 'left', top: 'middle' },
          series: [{
            name: '风险类型',
            type: 'pie',
            radius: ['40%', '65%'],
            center: ['55%', '55%'],
            data: distributionData,
            emphasis: { itemStyle: { shadowBlur: 10, shadowOffsetX: 0, shadowColor: 'rgba(0, 0, 0, 0.5)' } },
            itemStyle: {
              color: function(params) { return colorMap[params.name] || '#409EFF' }
            },
            label: { formatter: '{b}: {c}' }
          }]
        }
        this.riskDistributionChart.setOption(option)
      } catch (error) {
        console.error('加载风险分布图数据失败:', error)
      }
    },

    // 刷新图表
    refreshCharts() {
      this.loadRiskMatrixChart()
      this.loadRiskDistributionChart()
    },

    // 刷新风险矩阵
    refreshRiskMatrix() {
      this.loadRiskMatrixChart()
    },

    // 刷新风险分布
    refreshRiskDistribution() {
      this.loadRiskDistributionChart()
    },

    // 标签页切换
    handleTabClick(tab) {
      // 更新路由参数
      this.$router.replace({
        query: {
          ...this.$route.query,
          tab: tab.name
        }
      })
    },

    // 获取风险评分样式类
    getRiskScoreClass(score) {
      if (score >= 80) return 'risk-high'
      if (score >= 60) return 'risk-medium'
      return 'risk-low'
    },

    // 获取风险评分文本
    getRiskScoreText(score) {
      if (score >= 80) return '高风险'
      if (score >= 60) return '中风险'
      return '低风险'
    },

    // 获取预警类型
    getWarningType(level) {
      const typeMap = {
        'HIGH': 'error',
        'MEDIUM': 'warning',
        'LOW': 'info'
      }
      return typeMap[level] || 'info'
    },

    // 获取预警等级标签
    getWarningLevelTag(level) {
      const tagMap = {
        'HIGH': 'danger',
        'MEDIUM': 'warning',
        'LOW': 'info'
      }
      return tagMap[level] || 'info'
    },

    // 获取预警等级文本
    getWarningLevelText(level) {
      const textMap = {
        'HIGH': '高',
        'MEDIUM': '中',
        'LOW': '低'
      }
      return textMap[level] || level
    }
  },
  beforeDestroy() {
    if (this.riskMatrixChart) {
      this.riskMatrixChart.dispose()
    }
    if (this.riskDistributionChart) {
      this.riskDistributionChart.dispose()
    }
  }
}
</script>

<style scoped>
.enterprise-risk {
  padding: 20px;
}

.mb-20 {
  margin-bottom: 20px;
}

.mb-10 {
  margin-bottom: 10px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-title {
  font-size: 16px;
  font-weight: bold;
  color: #303133;
}

.enterprise-selector {
  display: flex;
  align-items: center;
}

.statistics-card {
  height: 120px;
}

.statistics-content {
  display: flex;
  align-items: center;
  height: 100%;
}

.statistics-icon {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  color: white;
  margin-right: 15px;
}

.statistics-icon.total-risks {
  background: linear-gradient(135deg, var(--ip-primary, #667eea) 0%, var(--ip-secondary, #764ba2) 100%);
}

.statistics-icon.high-risks {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.statistics-icon.control-measures {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.statistics-icon.risk-score {
  background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
}

.statistics-info {
  flex: 1;
}

.statistics-number {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
  line-height: 1;
  margin-bottom: 5px;
}

.statistics-label {
  font-size: 12px;
  color: #909399;
  margin-bottom: 5px;
}

.statistics-trend {
  font-size: 12px;
  display: flex;
  align-items: center;
  gap: 2px;
}

.risk-high {
  color: #F56C6C;
}

.risk-medium {
  color: #E6A23C;
}

.risk-low {
  color: #67C23A;
}

.warning-list {
  max-height: 200px;
  overflow-y: auto;
}

.warning-time {
  float: right;
  font-size: 12px;
  color: #909399;
}

.risk-matrix-container,
.risk-distribution-container {
  height: 300px;
}

.risk-matrix-chart,
.risk-distribution-chart {
  width: 100%;
  height: 100%;
}
</style>
