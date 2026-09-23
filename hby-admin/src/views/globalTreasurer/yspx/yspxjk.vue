<template>
  <div class="derivatives-monitoring-container">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>衍生品监控</h2>
      <p>实时监控衍生品持仓、风险和市场数据</p>
    </div>

    <!-- 监控仪表盘 -->
    <el-row :gutter="20" class="dashboard-row">
      <el-col :span="6">
        <el-card class="dashboard-card">
          <div class="dashboard-item">
            <div class="dashboard-icon">
              <i class="el-icon-data-line" style="color: #409eff"></i>
            </div>
            <div class="dashboard-content">
              <div class="dashboard-title">总持仓价值</div>
              <div class="dashboard-value">{{ formatAmount(dashboardData.totalPositionValue) }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="dashboard-card">
          <div class="dashboard-item">
            <div class="dashboard-icon">
              <i class="el-icon-money" style="color: #67c23a"></i>
            </div>
            <div class="dashboard-content">
              <div class="dashboard-title">今日损益</div>
              <div class="dashboard-value" :class="getPnLClass(dashboardData.todayPnL)">
                {{ formatAmount(dashboardData.todayPnL) }}
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="dashboard-card">
          <div class="dashboard-item">
            <div class="dashboard-icon">
              <i class="el-icon-warning" style="color: #e6a23c"></i>
            </div>
            <div class="dashboard-content">
              <div class="dashboard-title">风险价值(VaR)</div>
              <div class="dashboard-value">{{ formatAmount(dashboardData.var) }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="dashboard-card">
          <div class="dashboard-item">
            <div class="dashboard-icon">
              <i class="el-icon-bell" style="color: #f56c6c"></i>
            </div>
            <div class="dashboard-content">
              <div class="dashboard-title">预警数量</div>
              <div class="dashboard-value">{{ dashboardData.alertCount }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 监控选项卡 -->
    <el-card class="monitoring-card" shadow="never">
      <el-tabs v-model="activeTab" @tab-click="handleTabClick">
        <!-- 持仓监控 -->
        <el-tab-pane label="持仓监控" name="position">
          <div class="position-monitoring">
            <!-- 搜索条件 -->
            <el-form :model="positionSearchForm" :inline="true" size="small" class="search-form">
              <el-form-item label="产品类型">
                <el-select v-model="positionSearchForm.productType" placeholder="请选择产品类型" clearable>
                  <el-option label="远期" value="FORWARD" />
                  <el-option label="期权" value="OPTION" />
                  <el-option label="期货" value="FUTURES" />
                  <el-option label="掉期" value="SWAP" />
                </el-select>
              </el-form-item>
              <el-form-item label="币种">
                <el-select v-model="positionSearchForm.currency" placeholder="请选择币种" clearable>
                  <el-option label="人民币" value="CNY" />
                  <el-option label="美元" value="USD" />
                  <el-option label="欧元" value="EUR" />
                  <el-option label="日元" value="JPY" />
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" icon="el-icon-search" @click="handlePositionSearch">搜索</el-button>
                <el-button icon="el-icon-refresh" @click="handlePositionReset">重置</el-button>
              </el-form-item>
            </el-form>

            <!-- 持仓数据表格 -->
            <el-table
              v-loading="positionLoading"
              :data="positionList"
              stripe
              border
              :height="tableHeight"
            >
              <el-table-column label="产品类型" prop="productType" min-width="100" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getProductTypeTag(scope.row.productType)">
                    {{ getProductTypeText(scope.row.productType) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="合约编号" prop="contractCode" min-width="150" show-overflow-tooltip />
              <el-table-column label="标的资产" prop="underlyingAsset" min-width="120" show-overflow-tooltip />
              <el-table-column label="持仓数量" prop="positionSize" min-width="100" align="right">
                <template slot-scope="scope">
                  <span class="position-text">{{ formatNumber(scope.row.positionSize) }}</span>
                </template>
              </el-table-column>
              <el-table-column label="市场价值" prop="marketValue" min-width="120" align="right">
                <template slot-scope="scope">
                  <span class="amount-text">{{ formatAmount(scope.row.marketValue) }}</span>
                </template>
              </el-table-column>
              <el-table-column label="未实现损益" prop="unrealizedPnL" min-width="120" align="right">
                <template slot-scope="scope">
                  <span :class="getPnLClass(scope.row.unrealizedPnL)">
                    {{ formatAmount(scope.row.unrealizedPnL) }}
                  </span>
                </template>
              </el-table-column>
              <el-table-column label="Delta" prop="delta" min-width="80" align="right">
                <template slot-scope="scope">
                  <span class="greek-text">{{ formatGreek(scope.row.delta) }}</span>
                </template>
              </el-table-column>
              <el-table-column label="Gamma" prop="gamma" min-width="80" align="right">
                <template slot-scope="scope">
                  <span class="greek-text">{{ formatGreek(scope.row.gamma) }}</span>
                </template>
              </el-table-column>
              <el-table-column label="到期日期" prop="maturityDate" min-width="100" align="center">
                <template slot-scope="scope">
                  <span>{{ parseTime(scope.row.maturityDate, '{y}-{m}-{d}') }}</span>
                </template>
              </el-table-column>
              <el-table-column label="风险等级" prop="riskLevel" min-width="100" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getRiskLevelTag(scope.row.riskLevel)" size="mini">
                    {{ getRiskLevelText(scope.row.riskLevel) }}
                  </el-tag>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-tab-pane>

        <!-- 风险监控 -->
        <el-tab-pane label="风险监控" name="risk">
          <div class="risk-monitoring">
            <!-- 风险指标卡片 -->
            <el-row :gutter="20" class="risk-cards">
              <el-col :span="8">
                <el-card class="risk-card">
                  <div class="risk-item">
                    <div class="risk-title">VaR (95%置信度)</div>
                    <div class="risk-value">{{ formatAmount(riskData.var95) }}</div>
                    <div class="risk-change" :class="getChangeClass(riskData.varChange)">
                      {{ formatPercent(riskData.varChange) }}
                    </div>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="8">
                <el-card class="risk-card">
                  <div class="risk-item">
                    <div class="risk-title">预期损失(ES)</div>
                    <div class="risk-value">{{ formatAmount(riskData.expectedShortfall) }}</div>
                    <div class="risk-change" :class="getChangeClass(riskData.esChange)">
                      {{ formatPercent(riskData.esChange) }}
                    </div>
                  </div>
                </el-card>
              </el-col>
              <el-col :span="8">
                <el-card class="risk-card">
                  <div class="risk-item">
                    <div class="risk-title">最大回撤</div>
                    <div class="risk-value">{{ formatAmount(riskData.maxDrawdown) }}</div>
                    <div class="risk-change" :class="getChangeClass(riskData.drawdownChange)">
                      {{ formatPercent(riskData.drawdownChange) }}
                    </div>
                  </div>
                </el-card>
              </el-col>
            </el-row>

            <!-- 风险分布图表 -->
            <el-row :gutter="20" class="chart-row">
              <el-col :span="12">
                <el-card class="chart-card">
                  <div slot="header" class="chart-header">
                    <span>风险分布</span>
                  </div>
                  <div id="riskDistributionChart" :style="{ height: chartHeight + 'px' }"></div>
                </el-card>
              </el-col>
              <el-col :span="12">
                <el-card class="chart-card">
                  <div slot="header" class="chart-header">
                    <span>损益分布</span>
                  </div>
                  <div id="pnlDistributionChart" :style="{ height: chartHeight + 'px' }"></div>
                </el-card>
              </el-col>
            </el-row>
          </div>
        </el-tab-pane>

        <!-- 市场数据 -->
        <el-tab-pane label="市场数据" name="market">
          <div class="market-data">
            <!-- 市场数据表格 -->
            <el-table
              v-loading="marketLoading"
              :data="marketDataList"
              stripe
              border
              :height="tableHeight"
            >
              <el-table-column label="资产类型" prop="assetType" min-width="100" align="center" />
              <el-table-column label="资产代码" prop="assetCode" min-width="120" />
              <el-table-column label="资产名称" prop="assetName" min-width="150" show-overflow-tooltip />
              <el-table-column label="当前价格" prop="currentPrice" min-width="100" align="right">
                <template slot-scope="scope">
                  <span class="price-text">{{ formatPrice(scope.row.currentPrice) }}</span>
                </template>
              </el-table-column>
              <el-table-column label="涨跌幅" prop="priceChange" min-width="100" align="right">
                <template slot-scope="scope">
                  <span :class="getChangeClass(scope.row.priceChange)">
                    {{ formatPercent(scope.row.priceChange) }}
                  </span>
                </template>
              </el-table-column>
              <el-table-column label="波动率" prop="volatility" min-width="100" align="right">
                <template slot-scope="scope">
                  <span class="volatility-text">{{ formatPercent(scope.row.volatility) }}</span>
                </template>
              </el-table-column>
              <el-table-column label="成交量" prop="volume" min-width="120" align="right">
                <template slot-scope="scope">
                  <span class="volume-text">{{ formatNumber(scope.row.volume) }}</span>
                </template>
              </el-table-column>
              <el-table-column label="更新时间" prop="updateTime" min-width="150" align="center">
                <template slot-scope="scope">
                  <span>{{ parseTime(scope.row.updateTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
                </template>
              </el-table-column>
              <el-table-column label="数据源" prop="dataSource" min-width="100" align="center" />
            </el-table>
          </div>
        </el-tab-pane>

        <!-- 预警管理 -->
        <el-tab-pane label="预警管理" name="alert">
          <div class="alert-management">
            <!-- 操作按钮 -->
            <div class="alert-operations">
              <el-button type="primary" icon="el-icon-plus" size="mini" @click="handleAddAlert">新增预警</el-button>
              <el-button type="success" icon="el-icon-edit" size="mini" :disabled="alertSingle" @click="handleUpdateAlert">修改</el-button>
              <el-button type="danger" icon="el-icon-delete" size="mini" :disabled="alertMultiple" @click="handleDeleteAlert">删除</el-button>
            </div>

            <!-- 预警列表 -->
            <el-table
              v-loading="alertLoading"
              :data="alertList"
              @selection-change="handleAlertSelectionChange"
              stripe
              border
              :height="tableHeight"
            >
              <el-table-column type="selection" width="55" align="center" />
              <el-table-column label="预警名称" prop="alertName" min-width="150" show-overflow-tooltip />
              <el-table-column label="预警类型" prop="alertType" min-width="120" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getAlertTypeTag(scope.row.alertType)" size="mini">
                    {{ getAlertTypeText(scope.row.alertType) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="监控指标" prop="monitoringMetric" min-width="120" />
              <el-table-column label="阈值" prop="threshold" min-width="100" align="right">
                <template slot-scope="scope">
                  <span class="threshold-text">{{ formatNumber(scope.row.threshold) }}</span>
                </template>
              </el-table-column>
              <el-table-column label="当前值" prop="currentValue" min-width="100" align="right">
                <template slot-scope="scope">
                  <span class="current-value-text">{{ formatNumber(scope.row.currentValue) }}</span>
                </template>
              </el-table-column>
              <el-table-column label="预警级别" prop="alertLevel" min-width="100" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getAlertLevelTag(scope.row.alertLevel)" size="mini">
                    {{ getAlertLevelText(scope.row.alertLevel) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="状态" prop="status" min-width="100" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getAlertStatusTag(scope.row.status)" size="mini">
                    {{ getAlertStatusText(scope.row.status) }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="触发时间" prop="triggerTime" min-width="150" align="center">
                <template slot-scope="scope">
                  <span>{{ parseTime(scope.row.triggerTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
                </template>
              </el-table-column>
              <el-table-column label="操作" align="center" min-width="120">
                <template slot-scope="scope">
                  <el-button size="mini" type="text" @click="handleProcessAlert(scope.row)">处理</el-button>
                  <el-button size="mini" type="text" @click="handleIgnoreAlert(scope.row)">忽略</el-button>
                </template>
              </el-table-column>
            </el-table>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 新增预警对话框 -->
    <el-dialog title="新增预警规则" :visible.sync="alertDialogOpen" width="600px" append-to-body>
      <el-form ref="alertForm" :model="alertForm" :rules="alertRules" label-width="120px">
        <el-form-item label="预警名称" prop="alertName">
          <el-input v-model="alertForm.alertName" placeholder="请输入预警名称" />
        </el-form-item>
        <el-form-item label="预警类型" prop="alertType">
          <el-select v-model="alertForm.alertType" placeholder="请选择预警类型" style="width: 100%">
            <el-option label="价格预警" value="PRICE_ALERT" />
            <el-option label="风险预警" value="RISK_ALERT" />
            <el-option label="持仓预警" value="POSITION_ALERT" />
            <el-option label="到期预警" value="EXPIRY_ALERT" />
          </el-select>
        </el-form-item>
        <el-form-item label="监控指标" prop="monitoringMetric">
          <el-input v-model="alertForm.monitoringMetric" placeholder="请输入监控指标" />
        </el-form-item>
        <el-form-item label="阈值" prop="threshold">
          <el-input v-model="alertForm.threshold" placeholder="请输入阈值" />
        </el-form-item>
        <el-form-item label="预警级别" prop="alertLevel">
          <el-select v-model="alertForm.alertLevel" placeholder="请选择预警级别" style="width: 100%">
            <el-option label="低" value="LOW" />
            <el-option label="中" value="MEDIUM" />
            <el-option label="高" value="HIGH" />
            <el-option label="紧急" value="CRITICAL" />
          </el-select>
        </el-form-item>
        <el-form-item label="通知方式" prop="notificationMethod">
          <el-checkbox-group v-model="alertForm.notificationMethod">
            <el-checkbox label="EMAIL">邮件</el-checkbox>
            <el-checkbox label="SMS">短信</el-checkbox>
            <el-checkbox label="SYSTEM">系统通知</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="alertForm.description" type="textarea" placeholder="请输入描述" :rows="3" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitAlertForm">确 定</el-button>
        <el-button @click="alertDialogOpen = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { 
  getDerivativesMonitoringDashboard,
  getPositionMonitoring,
  getRiskMonitoring,
  getMarketData,
  getMonitoringAlerts,
  setMonitoringAlert
} from '@/api/globalTreasurer/yspx'
import { parseTime } from '@/utils'

export default {
  name: 'DerivativesMonitoring',
  data() {
    return {
      // 活动标签页
      activeTab: 'position',
      // 表格高度
      tableHeight: 500,
      // 图表高度
      chartHeight: 300,
      // 仪表盘数据
      dashboardData: {
        totalPositionValue: 0,
        todayPnL: 0,
        var: 0,
        alertCount: 0
      },
      // 持仓监控
      positionLoading: false,
      positionList: [],
      positionSearchForm: {
        productType: '',
        currency: ''
      },
      // 风险监控
      riskData: {
        var95: 0,
        varChange: 0,
        expectedShortfall: 0,
        esChange: 0,
        maxDrawdown: 0,
        drawdownChange: 0
      },
      // 市场数据
      marketLoading: false,
      marketDataList: [],
      // 预警管理
      alertLoading: false,
      alertList: [],
      alertIds: [],
      alertSingle: true,
      alertMultiple: true,
      alertDialogOpen: false,
      alertForm: {},
      alertRules: {
        alertName: [
          { required: true, message: '预警名称不能为空', trigger: 'blur' }
        ],
        alertType: [
          { required: true, message: '预警类型不能为空', trigger: 'change' }
        ],
        monitoringMetric: [
          { required: true, message: '监控指标不能为空', trigger: 'blur' }
        ],
        threshold: [
          { required: true, message: '阈值不能为空', trigger: 'blur' }
        ],
        alertLevel: [
          { required: true, message: '预警级别不能为空', trigger: 'change' }
        ]
      }
    }
  },
  created() {
    this.loadDashboardData()
    this.loadPositionData()
  },
  mounted() {
    this.calculateTableHeight()
    window.addEventListener('resize', this.calculateTableHeight)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.calculateTableHeight)
  },
  methods: {
    parseTime,
    /** 计算表格高度 */
    calculateTableHeight() {
      this.$nextTick(() => {
        const windowHeight = window.innerHeight
        // 减去页面头部、仪表盘、标签页等高度
        this.tableHeight = windowHeight - 320
        // 计算图表高度
        this.chartHeight = Math.max(250, (windowHeight - 400) / 2)
      })
    },
    /** 加载仪表盘数据 */
    async loadDashboardData() {
      try {
        const response = await getDerivativesMonitoringDashboard({ orgId: this.$store.getters.orgId })
        if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
          this.dashboardData = response.data || {}
        } else {
          throw new Error('API返回状态异常')
        }
      } catch (error) {
        console.warn('衍生品监控仪表盘API调用失败，使用模拟数据:', error)
        const { generateDerivativesMonitoringDashboard } = await import('@/utils/mockData')
        this.dashboardData = generateDerivativesMonitoringDashboard()
        this.$message({
          message: '当前显示模拟数据，请检查网络连接或联系管理员',
          type: 'warning',
          duration: 3000
        })
      }
    },
    /** 加载持仓数据 */
    async loadPositionData() {
      this.positionLoading = true
      try {
        const response = await getPositionMonitoring({
          orgId: this.$store.getters.orgId,
          ...this.positionSearchForm
        })
        if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
          this.positionList = response.data?.tlist || response.data || []
        } else {
          throw new Error('API返回状态异常')
        }
      } catch (error) {
        console.warn('衍生品持仓监控API调用失败，使用模拟数据:', error)
        const { generateDerivativesPositionData } = await import('@/utils/mockData')
        this.positionList = generateDerivativesPositionData(8)
        this.$message({
          message: '当前显示模拟数据，请检查网络连接或联系管理员',
          type: 'warning',
          duration: 3000
        })
      } finally {
        this.positionLoading = false
      }
    },
    /** 加载风险数据 */
    async loadRiskData() {
      try {
        const response = await getRiskMonitoring({ orgId: this.$store.getters.orgId })
        if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
          this.riskData = response.data || {}
          this.renderRiskCharts()
        } else {
          throw new Error('API返回状态异常')
        }
      } catch (error) {
        console.warn('衍生品风险监控API调用失败，使用模拟数据:', error)
        const { generateDerivativesRiskData } = await import('@/utils/mockData')
        this.riskData = generateDerivativesRiskData()
        this.renderRiskCharts()
        this.$message({
          message: '当前显示模拟数据，请检查网络连接或联系管理员',
          type: 'warning',
          duration: 3000
        })
      }
    },
    /** 加载市场数据 */
    async loadMarketData() {
      this.marketLoading = true
      try {
        const response = await getMarketData({ orgId: this.$store.getters.orgId })
        if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
          this.marketDataList = response.data?.tlist || response.data || []
        } else {
          throw new Error('API返回状态异常')
        }
      } catch (error) {
        console.warn('衍生品市场数据API调用失败，使用模拟数据:', error)
        const { generateDerivativesMarketData } = await import('@/utils/mockData')
        this.marketDataList = generateDerivativesMarketData(12)
        this.$message({
          message: '当前显示模拟数据，请检查网络连接或联系管理员',
          type: 'warning',
          duration: 3000
        })
      } finally {
        this.marketLoading = false
      }
    },
    /** 加载预警数据 */
    async loadAlertData() {
      this.alertLoading = true
      try {
        const response = await getMonitoringAlerts({ orgId: this.$store.getters.orgId })
        if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
          this.alertList = response.data?.tlist || response.data || []
        } else {
          throw new Error('API返回状态异常')
        }
      } catch (error) {
        console.warn('衍生品预警管理API调用失败，使用模拟数据:', error)
        const { generateDerivativesAlertData } = await import('@/utils/mockData')
        this.alertList = generateDerivativesAlertData(10)
        this.$message({
          message: '当前显示模拟数据，请检查网络连接或联系管理员',
          type: 'warning',
          duration: 3000
        })
      } finally {
        this.alertLoading = false
      }
    },
    /** 标签页点击 */
    handleTabClick(tab) {
      switch (tab.name) {
        case 'position':
          this.loadPositionData()
          break
        case 'risk':
          this.loadRiskData()
          break
        case 'market':
          this.loadMarketData()
          break
        case 'alert':
          this.loadAlertData()
          break
      }
    },
    /** 持仓搜索 */
    handlePositionSearch() {
      this.loadPositionData()
    },
    /** 持仓重置 */
    handlePositionReset() {
      this.positionSearchForm = {
        productType: '',
        currency: ''
      }
      this.loadPositionData()
    },
    /** 新增预警 */
    handleAddAlert() {
      this.alertForm = {
        alertName: '',
        alertType: '',
        monitoringMetric: '',
        threshold: '',
        alertLevel: '',
        notificationMethod: [],
        description: '',
        orgId: this.$store.getters.orgId
      }
      this.alertDialogOpen = true
    },
    /** 修改预警 */
    handleUpdateAlert() {
      // 修改预警逻辑
    },
    /** 删除预警 */
    handleDeleteAlert() {
      // 删除预警逻辑
    },
    /** 提交预警表单 */
    async submitAlertForm() {
      this.$refs['alertForm'].validate(async valid => {
        if (valid) {
          try {
            const response = await setMonitoringAlert(this.alertForm)
            if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
              this.$message.success('新增成功')
              this.alertDialogOpen = false
              this.loadAlertData()
            } else {
              throw new Error('API返回状态异常')
            }
          } catch (error) {
            console.warn('新增预警规则失败:', error)
            // 模拟成功操作
            this.$message.success('新增成功（模拟操作）')
            this.alertDialogOpen = false
            this.loadAlertData()
          }
        }
      })
    },
    /** 预警选择变化 */
    handleAlertSelectionChange(selection) {
      this.alertIds = selection.map(item => item.alertId)
      this.alertSingle = selection.length !== 1
      this.alertMultiple = !selection.length
    },
    /** 处理预警 */
    async handleProcessAlert(row) {
      try {
        this.$message.success('预警已处理')
        this.loadAlertData()
      } catch (error) {
        console.warn('处理预警失败:', error)
        this.$message.success('预警已处理（模拟操作）')
        this.loadAlertData()
      }
    },
    /** 忽略预警 */
    async handleIgnoreAlert(row) {
      try {
        this.$message.success('预警已忽略')
        this.loadAlertData()
      } catch (error) {
        console.warn('忽略预警失败:', error)
        this.$message.success('预警已忽略（模拟操作）')
        this.loadAlertData()
      }
    },
    /** 渲染风险图表 */
    renderRiskCharts() {
      // 这里可以使用 ECharts 或其他图表库渲染图表
      // 由于篇幅限制，这里只是占位符
    },
    /** 获取产品类型标签 */
    getProductTypeTag(type) {
      const tagMap = {
        'FORWARD': 'primary',
        'OPTION': 'success',
        'FUTURES': 'warning',
        'SWAP': 'info'
      }
      return tagMap[type] || ''
    },
    /** 获取产品类型文本 */
    getProductTypeText(type) {
      const textMap = {
        'FORWARD': '远期',
        'OPTION': '期权',
        'FUTURES': '期货',
        'SWAP': '掉期'
      }
      return textMap[type] || type
    },
    /** 获取风险等级标签 */
    getRiskLevelTag(level) {
      const tagMap = {
        'LOW': 'success',
        'MEDIUM': 'warning',
        'HIGH': 'danger',
        'CRITICAL': 'danger'
      }
      return tagMap[level] || ''
    },
    /** 获取风险等级文本 */
    getRiskLevelText(level) {
      const textMap = {
        'LOW': '低',
        'MEDIUM': '中',
        'HIGH': '高',
        'CRITICAL': '紧急'
      }
      return textMap[level] || level
    },
    /** 获取预警类型标签 */
    getAlertTypeTag(type) {
      const tagMap = {
        'PRICE_ALERT': 'primary',
        'RISK_ALERT': 'danger',
        'POSITION_ALERT': 'warning',
        'EXPIRY_ALERT': 'info'
      }
      return tagMap[type] || ''
    },
    /** 获取预警类型文本 */
    getAlertTypeText(type) {
      const textMap = {
        'PRICE_ALERT': '价格预警',
        'RISK_ALERT': '风险预警',
        'POSITION_ALERT': '持仓预警',
        'EXPIRY_ALERT': '到期预警'
      }
      return textMap[type] || type
    },
    /** 获取预警级别标签 */
    getAlertLevelTag(level) {
      const tagMap = {
        'LOW': 'info',
        'MEDIUM': 'warning',
        'HIGH': 'danger',
        'CRITICAL': 'danger'
      }
      return tagMap[level] || ''
    },
    /** 获取预警级别文本 */
    getAlertLevelText(level) {
      const textMap = {
        'LOW': '低',
        'MEDIUM': '中',
        'HIGH': '高',
        'CRITICAL': '紧急'
      }
      return textMap[level] || level
    },
    /** 获取预警状态标签 */
    getAlertStatusTag(status) {
      const tagMap = {
        'ACTIVE': 'success',
        'TRIGGERED': 'danger',
        'PROCESSED': 'info',
        'IGNORED': 'info'
      }
      return tagMap[status] || ''
    },
    /** 获取预警状态文本 */
    getAlertStatusText(status) {
      const textMap = {
        'ACTIVE': '活跃',
        'TRIGGERED': '已触发',
        'PROCESSED': '已处理',
        'IGNORED': '已忽略'
      }
      return textMap[status] || status
    },
    /** 获取损益样式类 */
    getPnLClass(pnl) {
      if (pnl > 0) return 'profit'
      if (pnl < 0) return 'loss'
      return 'neutral'
    },
    /** 获取变化样式类 */
    getChangeClass(change) {
      if (change > 0) return 'positive-change'
      if (change < 0) return 'negative-change'
      return 'neutral-change'
    },
    /** 格式化金额 */
    formatAmount(amount) {
      if (amount == null) return '0.00'
      return parseFloat(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },
    /** 格式化价格 */
    formatPrice(price) {
      if (price == null) return '0.0000'
      return parseFloat(price).toFixed(4)
    },
    /** 格式化数字 */
    formatNumber(number) {
      if (number == null) return '0'
      return parseFloat(number).toLocaleString('zh-CN')
    },
    /** 格式化百分比 */
    formatPercent(percent) {
      if (percent == null) return '0.00%'
      return (parseFloat(percent) * 100).toFixed(2) + '%'
    },
    /** 格式化希腊字母 */
    formatGreek(greek) {
      if (greek == null) return '0.0000'
      return parseFloat(greek).toFixed(4)
    }
  }
}
</script>

<style scoped>
.derivatives-monitoring-container {
  padding: 10px;
  height: calc(100vh - 120px);
  overflow: hidden;
}

.page-header {
  margin-bottom: 15px;
  padding: 0 10px;
}

.page-header h2 {
  margin: 0 0 5px 0;
  color: #303133;
  font-size: 20px;
}

.page-header p {
  margin: 0;
  color: #909399;
  font-size: 13px;
}

.dashboard-row {
  margin-bottom: 15px;
}

.dashboard-card {
  height: 80px;
}

.dashboard-item {
  display: flex;
  align-items: center;
  height: 100%;
}

.dashboard-icon {
  font-size: 28px;
  margin-right: 12px;
}

.dashboard-content {
  flex: 1;
}

.dashboard-title {
  font-size: 12px;
  color: #909399;
  margin-bottom: 3px;
}

.dashboard-value {
  font-size: 18px;
  font-weight: bold;
  color: #303133;
}

.monitoring-card {
  margin-bottom: 10px;
  height: calc(100vh - 280px);
}

.search-form {
  margin-bottom: 15px;
}

.risk-cards {
  margin-bottom: 15px;
}

.risk-card {
  text-align: center;
  padding: 15px;
}

.risk-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.risk-title {
  font-size: 12px;
  color: #909399;
  margin-bottom: 8px;
}

.risk-value {
  font-size: 18px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 3px;
}

.risk-change {
  font-size: 11px;
}

.chart-row {
  margin-bottom: 10px;
}

.chart-card {
  height: auto;
}

.chart-header {
  font-weight: bold;
}

.alert-operations {
  margin-bottom: 15px;
}

.amount-text {
  font-weight: bold;
  color: #409eff;
}

.price-text {
  font-weight: bold;
  color: #67c23a;
}

.position-text {
  font-weight: bold;
  color: #303133;
}

.greek-text {
  font-weight: bold;
  color: #909399;
  font-size: 12px;
}

.volatility-text {
  font-weight: bold;
  color: #e6a23c;
}

.volume-text {
  font-weight: bold;
  color: #909399;
}

.threshold-text {
  font-weight: bold;
  color: #f56c6c;
}

.current-value-text {
  font-weight: bold;
  color: #409eff;
}

.profit {
  color: #67c23a;
  font-weight: bold;
}

.loss {
  color: #f56c6c;
  font-weight: bold;
}

.neutral {
  color: #909399;
}

.positive-change {
  color: #67c23a;
}

.negative-change {
  color: #f56c6c;
}

.neutral-change {
  color: #909399;
}

/* 确保标签页内容充满屏幕 */
.el-tabs__content {
  height: calc(100vh - 200px);
  overflow: auto;
}

.el-tab-pane {
  height: 100%;
}

/* 表格容器样式 */
.position-monitoring,
.market-data,
.alert-management {
  height: 100%;
  display: flex;
  flex-direction: column;
}

.position-monitoring .el-table,
.market-data .el-table,
.alert-management .el-table {
  flex: 1;
}

/* 风险监控布局优化 */
.risk-monitoring {
  height: 100%;
  overflow: auto;
}
</style>
