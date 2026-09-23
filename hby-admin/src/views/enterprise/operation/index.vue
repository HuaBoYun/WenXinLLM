<template>
  <div class="enterprise-operation" :style="themeVars">
    <!-- 企业选择 -->
    <el-card class="mb-20">
      <el-row :gutter="20" type="flex" justify="space-between" align="middle">
        <el-col :span="12">
          <div class="enterprise-selector">
            <span style="margin-right: 10px; font-weight: bold;">选择企业：</span>
            <el-select
              v-model="selectedEnterpriseId"
              placeholder="请选择企业"
              filterable
              style="width: 350px;"
              @change="handleEnterpriseChange"
            >
              <el-option
                v-for="item in enterpriseList"
                :key="item.enterpriseId"
                :label="item.enterpriseName"
                :value="item.enterpriseId"
              ></el-option>
            </el-select>
          </div>
        </el-col>
        <el-col :span="12" style="text-align: right;">
          <div class="operation-controls">
            <el-button type="info" @click="handleReport" icon="el-icon-document">经营报告</el-button>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <!-- 经营概览统计 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon production">
              <i class="el-icon-goods"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">{{ formatAmount(statistics.productionOutput) }}</div>
              <div class="statistics-label">生产产量(万元)</div>
              <div class="statistics-trend" :class="getTrendClass(statistics.productionGrowth)">
                <i :class="getTrendIcon(statistics.productionGrowth)"></i>
                {{ statistics.productionGrowth || 0 }}%
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon sales">
              <i class="el-icon-shopping-cart-2"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">{{ formatAmount(statistics.salesVolume) }}</div>
              <div class="statistics-label">销售额(万元)</div>
              <div class="statistics-trend" :class="getTrendClass(statistics.salesGrowth)">
                <i :class="getTrendIcon(statistics.salesGrowth)"></i>
                {{ statistics.salesGrowth || 0 }}%
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon efficiency">
              <i class="el-icon-cpu"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">{{ statistics.operationEfficiency || 0 }}%</div>
              <div class="statistics-label">运营效率</div>
              <div class="statistics-trend">
                <span>目标: {{ statistics.efficiencyTarget || 0 }}%</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="statistics-card">
          <div class="statistics-content">
            <div class="statistics-icon projects">
              <i class="el-icon-folder-opened"></i>
            </div>
            <div class="statistics-info">
              <div class="statistics-number">{{ statistics.activeProjects || 0 }}</div>
              <div class="statistics-label">在建项目</div>
              <div class="statistics-trend">
                <span>完成: {{ statistics.completedProjects || 0 }}个</span>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 经营状态监控 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="8">
        <el-card>
          <div slot="header" class="card-header">
            <span class="card-title">生产状态监控</span>
            <el-tag :type="getProductionStatusTag(productionStatus.status)">
              {{ productionStatus.statusText || '加载中' }}
            </el-tag>
          </div>
          <div class="production-monitor">
            <div class="monitor-item">
              <span class="monitor-label">产能利用率:</span>
              <el-progress
                :percentage="productionStatus.capacityUtilization || 0"
                :color="getProgressColor(productionStatus.capacityUtilization || 0)"
                :show-text="false"
                style="width: 120px;"
              ></el-progress>
              <span class="monitor-value">{{ productionStatus.capacityUtilization || 0 }}%</span>
            </div>
            <div class="monitor-item">
              <span class="monitor-label">设备运行率:</span>
              <el-progress
                :percentage="productionStatus.equipmentRunning || 0"
                :color="getProgressColor(productionStatus.equipmentRunning || 0)"
                :show-text="false"
                style="width: 120px;"
              ></el-progress>
              <span class="monitor-value">{{ productionStatus.equipmentRunning || 0 }}%</span>
            </div>
            <div class="monitor-item">
              <span class="monitor-label">质量合格率:</span>
              <el-progress
                :percentage="productionStatus.qualityRate || 0"
                :color="getProgressColor(productionStatus.qualityRate || 0)"
                :show-text="false"
                style="width: 120px;"
              ></el-progress>
              <span class="monitor-value">{{ productionStatus.qualityRate || 0 }}%</span>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <div slot="header" class="card-header">
            <span class="card-title">销售状态监控</span>
            <el-tag :type="getSalesStatusTag(salesStatus.status)">
              {{ salesStatus.statusText || '加载中' }}
            </el-tag>
          </div>
          <div class="sales-monitor">
            <div class="monitor-item">
              <span class="monitor-label">销售完成率:</span>
              <el-progress
                :percentage="salesStatus.salesCompletion || 0"
                :color="getProgressColor(salesStatus.salesCompletion || 0)"
                :show-text="false"
                style="width: 120px;"
              ></el-progress>
              <span class="monitor-value">{{ salesStatus.salesCompletion || 0 }}%</span>
            </div>
            <div class="monitor-item">
              <span class="monitor-label">客户满意度:</span>
              <el-progress
                :percentage="salesStatus.customerSatisfaction || 0"
                :color="getProgressColor(salesStatus.customerSatisfaction || 0)"
                :show-text="false"
                style="width: 120px;"
              ></el-progress>
              <span class="monitor-value">{{ salesStatus.customerSatisfaction || 0 }}%</span>
            </div>
            <div class="monitor-item">
              <span class="monitor-label">市场占有率:</span>
              <el-progress
                :percentage="salesStatus.marketShare || 0"
                :color="getProgressColor(salesStatus.marketShare || 0)"
                :show-text="false"
                style="width: 120px;"
              ></el-progress>
              <span class="monitor-value">{{ salesStatus.marketShare || 0 }}%</span>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card>
          <div slot="header" class="card-header">
            <span class="card-title">项目进度监控</span>
            <el-tag :type="getProjectStatusTag(projectStatus.status)">
              {{ projectStatus.statusText || '加载中' }}
            </el-tag>
          </div>
          <div class="project-monitor">
            <div class="monitor-item">
              <span class="monitor-label">项目完成率:</span>
              <el-progress
                :percentage="projectStatus.projectCompletion || 0"
                :color="getProgressColor(projectStatus.projectCompletion || 0)"
                :show-text="false"
                style="width: 120px;"
              ></el-progress>
              <span class="monitor-value">{{ projectStatus.projectCompletion || 0 }}%</span>
            </div>
            <div class="monitor-item">
              <span class="monitor-label">预算执行率:</span>
              <el-progress
                :percentage="projectStatus.budgetExecution || 0"
                :color="getProgressColor(projectStatus.budgetExecution || 0)"
                :show-text="false"
                style="width: 120px;"
              ></el-progress>
              <span class="monitor-value">{{ projectStatus.budgetExecution || 0 }}%</span>
            </div>
            <div class="monitor-item">
              <span class="monitor-label">风险控制率:</span>
              <el-progress
                :percentage="projectStatus.riskControl || 0"
                :color="getProgressColor(projectStatus.riskControl || 0)"
                :show-text="false"
                style="width: 120px;"
              ></el-progress>
              <span class="monitor-value">{{ projectStatus.riskControl || 0 }}%</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 功能模块标签页 -->
    <el-card>
      <el-tabs v-model="activeTab" @tab-click="handleTabClick">
        <!-- 经营计划管理 -->
        <el-tab-pane label="经营计划管理" name="operationPlan">
          <OperationPlanTab
            :enterprise-id="selectedEnterpriseId"
            :enterprise-name="selectedEnterpriseName"
            @refresh="loadStatistics"
          />
        </el-tab-pane>

        <!-- 生产管理 -->
        <el-tab-pane label="生产管理" name="production">
          <ProductionManagementTab
            :enterprise-id="selectedEnterpriseId"
            :enterprise-name="selectedEnterpriseName"
            @refresh="loadStatistics"
          />
        </el-tab-pane>

        <!-- 销售管理 -->
        <el-tab-pane label="销售管理" name="sales">
          <SalesManagementTab
            :enterprise-id="selectedEnterpriseId"
            :enterprise-name="selectedEnterpriseName"
            @refresh="loadStatistics"
          />
        </el-tab-pane>

        <!-- 供应链管理 -->
        <el-tab-pane label="供应链管理" name="supply">
          <SupplyChainTab
            :enterprise-id="selectedEnterpriseId"
            :enterprise-name="selectedEnterpriseName"
            @refresh="loadStatistics"
          />
        </el-tab-pane>

        <!-- 业务流程 -->
        <el-tab-pane label="业务流程" name="process">
          <BusinessProcessTab
            :enterprise-id="selectedEnterpriseId"
            :enterprise-name="selectedEnterpriseName"
            @refresh="loadStatistics"
          />
        </el-tab-pane>

        <!-- 绩效管理 -->
        <el-tab-pane label="绩效管理" name="performance">
          <PerformanceManagementTab
            :enterprise-id="selectedEnterpriseId"
            :enterprise-name="selectedEnterpriseName"
            @refresh="loadStatistics"
          />
        </el-tab-pane>

        <!-- 市场分析 -->
        <el-tab-pane label="市场分析" name="market">
          <MarketAnalysisTab
            :enterprise-id="selectedEnterpriseId"
            :enterprise-name="selectedEnterpriseName"
          />
        </el-tab-pane>

        <!-- 项目管理 -->
        <el-tab-pane label="项目管理" name="project">
          <ProjectManagementTab
            :enterprise-id="selectedEnterpriseId"
            :enterprise-name="selectedEnterpriseName"
            @refresh="loadStatistics"
          />
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 经营报告对话框 -->
    <OperationReportDialog
      :visible.sync="reportDialogVisible"
      :enterprise-id="selectedEnterpriseId"
    />
  </div>
</template>

<script>
import {
  getOperationStatistics,
  getProductionStatistics,
  getSalesStatistics,
  getProjectProgress
} from '@/api/enterprise/operation'
import request from '@/utils/request'
import { transData } from '@/utils/requestData'
import OperationPlanTab from './components/OperationPlanTab'
import ProductionManagementTab from './components/ProductionManagementTab'
import SalesManagementTab from './components/SalesManagementTab'
import SupplyChainTab from './components/SupplyChainTab'
import BusinessProcessTab from './components/BusinessProcessTab'
import PerformanceManagementTab from './components/PerformanceManagementTab'
import MarketAnalysisTab from './components/MarketAnalysisTab'
import ProjectManagementTab from './components/ProjectManagementTab'
import OperationReportDialog from './components/OperationReportDialog'
import { investThemeMixin } from '../../stateAssets/themeMixin'

export default {
  name: 'EnterpriseOperation',
  mixins: [investThemeMixin],
  components: {
    OperationPlanTab,
    ProductionManagementTab,
    SalesManagementTab,
    SupplyChainTab,
    BusinessProcessTab,
    PerformanceManagementTab,
    MarketAnalysisTab,
    ProjectManagementTab,
    OperationReportDialog
  },
  data() {
    return {
      selectedEnterpriseId: '',
      selectedEnterpriseName: '',
      enterpriseList: [],
      activeTab: 'operationPlan',
      statistics: {},
      productionStatus: {},
      salesStatus: {},
      projectStatus: {},

      // 对话框状态
      reportDialogVisible: false
    }
  },
  mounted() {
    this.loadEnterpriseList()
    if (this.$route.query.tab) {
      this.activeTab = this.$route.query.tab
    }
  },
  methods: {
    // 从API响应中提取数据（兼容 R 类的 result/code 字段和 pageInfo 包装）
    extractData(response) {
      if (!response) return null
      // handleData 返回 R 对象 {result, msg, data} 或直接返回 data
      // 兼容两种格式: response.data.tlist 和 response.data.pageInfo.tlist
      const resData = response.data || response
      if (resData && resData.pageInfo) {
        return resData.pageInfo
      }
      return resData
    },

    // 加载企业列表并默认选中第一个
    async loadEnterpriseList() {
      try {
        const response = await request({
          url: '/monitor/v1/enterprise/info/list',
          method: 'post',
          data: transData({ pageNumber: 1, pageSize: 100 }),
          headers: { 'Content-Type': 'application/json;charset=UTF-8' }
        })
        const pageData = this.extractData(response)
        const records = pageData && pageData.tlist ? pageData.tlist : []
        this.enterpriseList = records.map(item => ({
          enterpriseId: item.enterpriseId,
          enterpriseName: item.enterpriseName
        }))
        // 默认选中第一个企业
        if (this.enterpriseList.length > 0) {
          // 优先从路由参数获取
          if (this.$route.query.enterpriseId) {
            this.selectedEnterpriseId = this.$route.query.enterpriseId
            const found = this.enterpriseList.find(e => e.enterpriseId === this.selectedEnterpriseId)
            this.selectedEnterpriseName = found ? found.enterpriseName : ''
          } else {
            this.selectedEnterpriseId = this.enterpriseList[0].enterpriseId
            this.selectedEnterpriseName = this.enterpriseList[0].enterpriseName
          }
          this.loadStatistics()
          this.loadMonitoringData()
        }
      } catch (error) {
        console.error('加载企业列表失败:', error)
      }
    },

    // 企业选择变化
    handleEnterpriseChange(val) {
      const found = this.enterpriseList.find(e => e.enterpriseId === val)
      this.selectedEnterpriseName = found ? found.enterpriseName : ''
      if (this.selectedEnterpriseId) {
        this.loadStatistics()
        this.loadMonitoringData()
      }
    },

    // 加载统计数据
    async loadStatistics() {
      if (!this.selectedEnterpriseId) return

      try {
        const response = await getOperationStatistics({
          enterpriseId: this.selectedEnterpriseId
        })
        this.statistics = this.extractData(response) || {}
      } catch (error) {
        console.error('加载经营统计数据失败:', error)
      }
    },

    // 加载监控数据
    async loadMonitoringData() {
      if (!this.selectedEnterpriseId) return

      try {
        const [productionRes, salesRes, projectRes] = await Promise.all([
          getProductionStatistics({ enterpriseId: this.selectedEnterpriseId }),
          getSalesStatistics({ enterpriseId: this.selectedEnterpriseId }),
          getProjectProgress({ enterpriseId: this.selectedEnterpriseId })
        ])

        this.productionStatus = this.extractData(productionRes) || {}
        this.salesStatus = this.extractData(salesRes) || {}
        this.projectStatus = this.extractData(projectRes) || {}
      } catch (error) {
        console.error('加载监控数据失败:', error)
      }
    },

    // 格式化金额
    formatAmount(value) {
      if (!value) return '0'
      const num = typeof value === 'string' ? parseFloat(value) : value
      if (num >= 10000) {
        return (num / 10000).toFixed(2) + '万'
      }
      return num.toFixed(2)
    },

    // 标签页切换
    handleTabClick(tab) {
      this.$router.replace({
        query: {
          ...this.$route.query,
          tab: tab.name
        }
      })
    },

    // 经营报告
    handleReport() {
      if (!this.selectedEnterpriseId) {
        this.$message.warning('请先选择企业')
        return
      }
      this.reportDialogVisible = true
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

    // 获取进度条颜色
    getProgressColor(percentage) {
      if (percentage >= 90) return '#67C23A'
      if (percentage >= 70) return '#E6A23C'
      return '#F56C6C'
    },

    // 获取生产状态标签
    getProductionStatusTag(status) {
      const tagMap = {
        'NORMAL': 'success',
        'WARNING': 'warning',
        'CRITICAL': 'danger'
      }
      return tagMap[status] || 'info'
    },

    // 获取销售状态标签
    getSalesStatusTag(status) {
      const tagMap = {
        'NORMAL': 'success',
        'WARNING': 'warning',
        'CRITICAL': 'danger'
      }
      return tagMap[status] || 'info'
    },

    // 获取项目状态标签
    getProjectStatusTag(status) {
      const tagMap = {
        'NORMAL': 'success',
        'WARNING': 'warning',
        'CRITICAL': 'danger'
      }
      return tagMap[status] || 'info'
    }
  }
}
</script>

<style scoped>
.enterprise-operation {
  padding: 20px;
}

.mb-20 {
  margin-bottom: 20px;
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

.operation-controls {
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

.statistics-icon.production {
  background: linear-gradient(135deg, var(--ip-primary, #667eea) 0%, var(--ip-secondary, #764ba2) 100%);
}

.statistics-icon.sales {
  background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
}

.statistics-icon.efficiency {
  background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
}

.statistics-icon.projects {
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

.production-monitor,
.sales-monitor,
.project-monitor {
  padding: 10px 0;
}

.monitor-item {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}

.monitor-label {
  width: 80px;
  font-size: 12px;
  color: #606266;
  margin-right: 10px;
}

.monitor-value {
  margin-left: 10px;
  font-size: 12px;
  color: #303133;
  font-weight: bold;
}
</style>
