<template>
  <div class="enterprise-financial" :style="themeVars">
    <!-- 企业选择 -->
    <el-card class="mb-20">
      <el-row :gutter="20" type="flex" justify="space-between" align="middle">
        <el-col :span="12">
          <div class="enterprise-selector">
            <el-input
              :value="selectedEnterpriseName"
              placeholder="请选择企业"
              readonly
              style="width: 300px;"
              @click.native="openCompanyTree"
            >
              <el-button slot="append" icon="el-icon-search" @click="openCompanyTree"></el-button>
            </el-input>
            <CompanyTreeModal
              ref="companyTreeModal"
              @selected="handleCompanySelected"
            />
          </div>
        </el-col>
        <el-col :span="12" style="text-align: right;">
          <div class="financial-controls">
            <span class="enterprise-label" v-if="selectedEnterpriseName">当前企业：{{ selectedEnterpriseName }}</span>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <!-- 财务概览统计 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon revenue">
              <i class="el-icon-money"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">{{ formatAmount(statistics.totalRevenue) }}</div>
              <div class="statistics-label">营业收入(万元)</div>
              <div class="statistics-trend" :class="getTrendClass(statistics.revenueGrowth)">
                <i :class="getTrendIcon(statistics.revenueGrowth)"></i>
                {{ statistics.revenueGrowth }}%
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon profit">
              <i class="el-icon-s-finance"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">{{ formatAmount(statistics.netProfit) }}</div>
              <div class="statistics-label">净利润(万元)</div>
              <div class="statistics-trend" :class="getTrendClass(statistics.profitGrowth)">
                <i :class="getTrendIcon(statistics.profitGrowth)"></i>
                {{ statistics.profitGrowth }}%
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon assets">
              <i class="el-icon-office-building"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">{{ formatAmount(statistics.totalAssets) }}</div>
              <div class="statistics-label">总资产(万元)</div>
              <div class="statistics-trend">
                <span>负债率: {{ statistics.liabilityRatio }}%</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon roe">
              <i class="el-icon-data-line"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">{{ statistics.roe }}%</div>
              <div class="statistics-label">净资产收益率</div>
              <div class="statistics-trend">
                <span>ROA: {{ statistics.roa }}%</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 财务预警信息 -->
    <el-card class="mb-20" v-if="warnings.length > 0">
      <div slot="header" class="card-header">
        <span class="card-title">财务预警</span>
        <el-tag type="danger">{{ warnings.length }}个预警</el-tag>
      </div>
      <div class="warning-list">
        <el-alert
          v-for="warning in warnings"
          :key="warning.warningId"
          :title="warning.warningTitle"
          :description="warning.warningDesc"
          type="warning"
          :closable="false"
          show-icon
          class="mb-10"
        >
          <template slot="title">
            <span>{{ warning.warningTitle }}</span>
            <el-tag size="mini" :type="getWarningLevelTag(warning.warningLevel)" style="margin-left: 10px;">
              {{ warning.warningLevel }}
            </el-tag>
          </template>
        </el-alert>
      </div>
    </el-card>

    <!-- 功能模块标签页 -->
    <el-card>
      <el-tabs v-model="activeTab" @tab-click="handleTabClick">
        <!-- 财务报表 -->
        <el-tab-pane label="财务报表" name="statement">
          <FinancialStatementTab
            v-if="loadedTabs.includes('statement')"
            :enterprise-id="selectedEnterpriseId"
            :enterprise-name="selectedEnterpriseName"
            @refresh="loadStatistics"
          />
        </el-tab-pane>

        <!-- 财务报表编制流程 -->
        <el-tab-pane label="报表编制流程" name="statementProcess">
          <FinancialStatementProcessTab
            v-if="loadedTabs.includes('statementProcess')"
            :enterprise-id="selectedEnterpriseId"
            :enterprise-name="selectedEnterpriseName"
            @refresh="loadStatistics"
          />
        </el-tab-pane>

        <!-- 预算管理 -->
        <el-tab-pane label="预算管理" name="budget">
          <BudgetManagementTab
            v-if="loadedTabs.includes('budget')"
            :enterprise-id="selectedEnterpriseId"
            :enterprise-name="selectedEnterpriseName"
            @refresh="loadStatistics"
          />
        </el-tab-pane>

        <!-- 成本管理 -->
        <el-tab-pane label="成本管理" name="cost">
          <CostManagementTab
            v-if="loadedTabs.includes('cost')"
            :enterprise-id="selectedEnterpriseId"
            :enterprise-name="selectedEnterpriseName"
            @refresh="loadStatistics"
          />
        </el-tab-pane>

        <!-- 财务分析 -->
        <el-tab-pane label="财务分析" name="analysis">
          <FinancialAnalysisTab
            v-if="loadedTabs.includes('analysis')"
            :enterprise-id="selectedEnterpriseId"
            :enterprise-name="selectedEnterpriseName"
          />
        </el-tab-pane>

        <!-- 财务预警 -->
        <el-tab-pane label="财务预警" name="warning">
          <FinancialWarningTab
            v-if="loadedTabs.includes('warning')"
            :enterprise-id="selectedEnterpriseId"
            :enterprise-name="selectedEnterpriseName"
            @refresh="loadWarnings"
          />
        </el-tab-pane>

        <!-- 财务报告 -->
        <el-tab-pane label="财务报告" name="report">
          <FinancialReportTab
            v-if="loadedTabs.includes('report')"
            :enterprise-id="selectedEnterpriseId"
            :enterprise-name="selectedEnterpriseName"
          />
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script>
import {
  getFinancialStatistics,
  getFinancialWarningList
} from '@/api/enterprise/financial'
import { findOrganization } from '@/api/setting/org'
import CompanyTreeModal from '@/components/CompanyTreeModal'
import FinancialStatementTab from './components/FinancialStatementTab'
import FinancialStatementProcessTab from './components/FinancialStatementProcessTab'
import BudgetManagementTab from './components/BudgetManagementTab'
import CostManagementTab from './components/CostManagementTab'
import FinancialAnalysisTab from './components/FinancialAnalysisTab'
import FinancialWarningTab from './components/FinancialWarningTab'
import FinancialReportTab from './components/FinancialReportTab'
import { investThemeMixin } from '../../stateAssets/themeMixin'

export default {
  name: 'EnterpriseFinancial',
  mixins: [investThemeMixin],
  components: {
    CompanyTreeModal,
    FinancialStatementTab,
    FinancialStatementProcessTab,
    BudgetManagementTab,
    CostManagementTab,
    FinancialAnalysisTab,
    FinancialWarningTab,
    FinancialReportTab
  },
  data() {
    return {
      selectedEnterpriseId: '',
      selectedEnterpriseName: '',
      activeTab: 'statement',
      loadedTabs: ['statement'], // 首次只加载默认tab
      statistics: {},
      warnings: []
    }
  },
  mounted() {
    // 从路由参数获取企业ID
    if (this.$route.query.enterpriseId) {
      this.selectedEnterpriseId = this.$route.query.enterpriseId
      this.selectedEnterpriseName = this.$route.query.enterpriseName || ''
      this.loadStatistics()
      this.loadWarnings()
    } else {
      // 默认加载第一个企业
      this.loadDefaultEnterprise()
    }
  },
  methods: {
    // 默认加载第一个企业
    async loadDefaultEnterprise() {
      try {
        const res = await findOrganization({})
        if (res && res.data && res.data.length > 0) {
          const first = res.data[0]
          this.selectedEnterpriseId = String(first.id)
          this.selectedEnterpriseName = first.name || first.label || ''
          this.handleEnterpriseChange()
        } else {
          // 没有企业数据时，使用占位ID加载全部数据
          console.warn('未找到企业数据，将加载全部财务数据')
          this.selectedEnterpriseId = 'ALL'
          this.selectedEnterpriseName = '全部企业'
          this.handleEnterpriseChange()
        }
      } catch (e) {
        console.warn('自动加载企业失败，将加载全部财务数据:', e)
        // 加载失败时也要触发子组件加载数据
        this.selectedEnterpriseId = 'ALL'
        this.selectedEnterpriseName = '全部企业'
        this.handleEnterpriseChange()
      }
    },

    // 打开企业选择对话框
    openCompanyTree() {
      this.$refs.companyTreeModal.show()
    },

    // 企业选择确认回调
    handleCompanySelected(node) {
      if (node) {
        this.selectedEnterpriseId = String(node.id)
        this.selectedEnterpriseName = node.label || node.name || ''
        this.handleEnterpriseChange()
      }
    },

    // 企业选择变化
    handleEnterpriseChange() {
      if (this.selectedEnterpriseId) {
        // 切换企业时，重置已加载tab列表，只保留当前激活的tab
        // 其他tab在用户点击时会用新企业ID重新加载
        this.loadedTabs = [this.activeTab]
        this.loadStatistics()
        this.loadWarnings()
      }
    },

    // 加载统计数据
    async loadStatistics() {
      if (!this.selectedEnterpriseId) return

      try {
        const response = await getFinancialStatistics({
          enterpriseId: this.selectedEnterpriseId
        })
        const data = response.data || {}
        // 后端返回的金额单位是元，页面显示为万元，除以10000
        this.statistics = {
          totalRevenue: data.totalRevenue || 0,
          netProfit: data.netProfit || 0,
          totalAssets: data.totalAssets || 0,
          totalLiabilities: data.totalLiabilities || 0,
          netAssets: data.netAssets || 0,
          liabilityRatio: data.liabilityRatio || 0,
          roe: data.roe || 0,
          roa: data.roa || 0,
          revenueGrowth: data.revenueGrowth || 0,
          profitGrowth: data.profitGrowth || 0
        }
      } catch (error) {
        console.error('加载财务统计数据失败:', error)
        this.statistics = {}
      }
    },

    // 加载预警信息
    async loadWarnings() {
      if (!this.selectedEnterpriseId) return

      try {
        const response = await getFinancialWarningList({
          enterpriseId: this.selectedEnterpriseId,
          status: 'pending',
          pageNumber: 1,
          pageSize: 10
        })
        const data = response.data || {}
        const list = data.tlist || data.records || []
        // 适配字段映射：后端warningName→前端warningTitle, riskLevel→warningLevel
        this.warnings = list.map(w => ({
          warningId: w.warningId,
          warningTitle: w.warningName,
          warningDesc: w.warningType + ' - 偏离度: ' + (w.deviation || 0) + '%',
          warningLevel: w.riskLevel
        }))
      } catch (error) {
        console.error('加载财务预警数据失败:', error)
      }
    },

    // 标签页切换
    handleTabClick(tab) {
      // 将新tab加入已加载列表（首次点击时触发组件渲染和数据请求）
      if (!this.loadedTabs.includes(tab.name)) {
        this.loadedTabs.push(tab.name)
      }
    },

    // 格式化金额（后端返回的数据单位是元，转换为万元显示）
    formatAmount(amount) {
      if (!amount && amount !== 0) return '0'
      if (amount === 0) return '0'
      const num = typeof amount === 'string' ? parseFloat(amount) : amount
      if (isNaN(num)) return '0'
      return (num / 10000).toFixed(2)
    },

    // 获取趋势样式类
    getTrendClass(rate) {
      if (rate > 0) return 'trend-up'
      if (rate < 0) return 'trend-down'
      return 'trend-flat'
    },

    // 获取趋势图标
    getTrendIcon(rate) {
      if (rate > 0) return 'el-icon-top'
      if (rate < 0) return 'el-icon-bottom'
      return 'el-icon-minus'
    },

    // 获取预警等级标签
    getWarningLevelTag(level) {
      const tagMap = {
        'high': 'danger',
        'HIGH': 'danger',
        'medium': 'warning',
        'MEDIUM': 'warning',
        'low': 'info',
        'LOW': 'info'
      }
      return tagMap[level] || 'info'
    }
  }
}
</script>

<style scoped>
.enterprise-financial {
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

.financial-controls {
  display: flex;
  align-items: center;
  gap: 10px;
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

.statistics-icon.revenue {
  background: linear-gradient(135deg, var(--ip-primary, #667eea) 0%, var(--ip-secondary, #764ba2) 100%);
}

.statistics-icon.profit {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.statistics-icon.assets {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.statistics-icon.roe {
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

.trend-up {
  color: #67C23A;
}

.trend-down {
  color: #F56C6C;
}

.trend-flat {
  color: #909399;
}

.warning-list {
  max-height: 200px;
  overflow-y: auto;
}
</style>
