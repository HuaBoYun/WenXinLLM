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
                <el-button type="warning" icon="el-icon-download" @click="handleExportPosition">导出</el-button>
              </el-form-item>
            </el-form>

            <!-- 持仓数据表格 -->
            <el-table
              ref="positionTable"
              v-loading="positionLoading"
              :data="positionList"
              stripe
              border
              :height="tableHeight"
              @selection-change="handlePositionSelectionChange"
            >
              <el-table-column type="selection" width="55" align="center" />
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
            <!-- 分页 -->
            <div class="position-pagination">
              <el-pagination
                :current-page="positionPageNum"
                :page-sizes="[10, 20, 50, 100]"
                :page-size="positionPageSize"
                :total="positionTotal"
                layout="total, sizes, prev, pager, next, jumper"
                background
                @size-change="handlePositionSizeChange"
                @current-change="handlePositionPageChange"
              />
            </div>
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
              <el-table-column label="数据类型" prop="dataType" min-width="100" align="center">
                <template slot-scope="scope">
                  <el-tag :type="getDataTypeTag(scope.row.dataType)" size="mini">{{ getDataTypeText(scope.row.dataType) }}</el-tag>
                </template>
              </el-table-column>
              <el-table-column label="品种/货币对" min-width="130" show-overflow-tooltip>
                <template slot-scope="scope">
                  {{ getDataTypeText(scope.row.productType) || scope.row.currencyPair || '-' }}
                </template>
              </el-table-column>
              <el-table-column label="最新价" prop="marketPrice" min-width="110" align="right">
                <template slot-scope="scope">
                  <span class="price-text">{{ formatPrice(scope.row.marketPrice) }}</span>
                </template>
              </el-table-column>
              <el-table-column label="涨跌幅" prop="changeRate" min-width="100" align="right">
                <template slot-scope="scope">
                  <span :class="getChangeClass(scope.row.changeRate)">
                    {{ formatPercent(scope.row.changeRate) }}
                  </span>
                </template>
              </el-table-column>
              <el-table-column label="开盘价" prop="openPrice" min-width="100" align="right">
                <template slot-scope="scope">
                  <span>{{ formatPrice(scope.row.openPrice) }}</span>
                </template>
              </el-table-column>
              <el-table-column label="最高价" prop="highPrice" min-width="100" align="right">
                <template slot-scope="scope">
                  <span style="color:#f56c6c">{{ formatPrice(scope.row.highPrice) }}</span>
                </template>
              </el-table-column>
              <el-table-column label="最低价" prop="lowPrice" min-width="100" align="right">
                <template slot-scope="scope">
                  <span style="color:#67c23a">{{ formatPrice(scope.row.lowPrice) }}</span>
                </template>
              </el-table-column>
              <el-table-column label="隐含波动率" prop="impliedVolatility" min-width="110" align="right">
                <template slot-scope="scope">
                  <span v-if="scope.row.dataType === 'OPTION' && scope.row.impliedVolatility != null" class="volatility-text">
                    {{ Number(scope.row.impliedVolatility).toFixed(2) }}%
                  </span>
                  <span v-else-if="scope.row.dataType === 'OPTION'" style="color:#909399">-</span>
                  <span v-else style="color:#c0c4cc">N/A</span>
                </template>
              </el-table-column>
              <el-table-column label="成交量" prop="volume" min-width="110" align="right">
                <template slot-scope="scope">
                  <span class="volume-text">{{ scope.row.volume != null ? formatNumber(scope.row.volume) : '-' }}</span>
                </template>
              </el-table-column>
              <el-table-column label="数据时间" prop="dataTime" min-width="150" align="center">
                <template slot-scope="scope">
                  <span>{{ parseTime(scope.row.dataTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
                </template>
              </el-table-column>
              <el-table-column label="数据源" prop="dataSource" min-width="160" show-overflow-tooltip align="center" />
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
              <el-button type="warning" icon="el-icon-download" size="mini" @click="handleExportAlert">导出</el-button>
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
            <!-- 预警分页 -->
            <div class="alert-pagination">
              <el-pagination
                background
                :current-page="alertPageNum"
                :page-sizes="[10, 20, 50]"
                :page-size="alertPageSize"
                layout="total, sizes, prev, pager, next"
                :total="alertTotal"
                @size-change="handleAlertSizeChange"
                @current-change="handleAlertPageChange"
              />
            </div>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 新增/修改预警对话框 -->
    <el-dialog :title="alertDialogTitle" :visible.sync="alertDialogOpen" width="600px" append-to-body @close="resetAlertForm">
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
  setMonitoringAlert,
  updateMonitoringAlert,
  deleteMonitoringAlert,
  processMonitoringAlert,
  ignoreMonitoringAlert,
  exportMonitoringPosition,
  exportMonitoringAlert
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
      positionTotal: 0,
      positionPageNum: 1,
      positionPageSize: 20,
      positionSelection: [],
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
      alertTotal: 0,
      alertPageNum: 1,
      alertPageSize: 20,
      alertIds: [],
      alertSingle: true,
      alertMultiple: true,
      alertDialogOpen: false,
      alertDialogTitle: '新增预警规则',
      alertDialogMode: 'add', // 'add' | 'edit'
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
        // 减去：顶部导航(50) + 页面padding(20) + page-header(55) + dashboard(110) + tabs-header(45) + search-form(55) + pagination(60) + 余量(20)
        this.tableHeight = windowHeight - 415
        // 计算图表高度
        this.chartHeight = Math.max(250, (windowHeight - 400) / 2)
      })
    },
    /** 加载仪表盘数据 */
    async loadDashboardData() {
      try {
        const response = await getDerivativesMonitoringDashboard({ orgId: this.$store.getters.orgId })
        if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
          const d = response.data || {}
          // 后端返回 totalNotional，前端展示字段为 totalPositionValue
          this.dashboardData = {
            totalPositionValue: d.totalNotional != null ? d.totalNotional : (d.totalPositionValue || 0),
            todayPnL: d.todayPnL || 0,
            var: d.var || 0,
            alertCount: d.alertCount || 0
          }
        } else {
          this.$message({ message: '仪表盘数据加载失败，请稍后重试', type: 'error', duration: 3000 })
        }
      } catch (error) {
        console.error('衍生品监控仪表盘API调用失败:', error)
        this.$message({ message: '仪表盘数据加载失败，请检查网络连接', type: 'error', duration: 3000 })
      }
    },
    /** 加载持仓数据 */
    async loadPositionData() {
      this.positionLoading = true
      try {
        // 过滤空字符串参数，只传有值的筛选条件
        const params = {
          pageNum: this.positionPageNum,
          pageSize: this.positionPageSize
        }
        if (this.positionSearchForm.productType) params.productType = this.positionSearchForm.productType
        if (this.positionSearchForm.currency) params.currency = this.positionSearchForm.currency

        const response = await getPositionMonitoring(params)
        if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
          this.positionList = response.data?.tlist || []
          this.positionTotal = response.data?.totalRecord || 0
        } else {
          throw new Error('API返回状态异常')
        }
      } catch (error) {
        console.warn('衍生品持仓监控API调用失败:', error)
        this.$message({ message: '持仓数据加载失败，请稍后重试', type: 'error', duration: 3000 })
      } finally {
        this.positionLoading = false
      }
    },
    /** 加载风险数据 */
    async loadRiskData() {
      try {
        const response = await getRiskMonitoring()
        if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
          this.riskData = response.data || {}
          this.$nextTick(() => {
            setTimeout(() => {
              this.renderRiskDistributionChart()
              this.renderPnlDistributionChart()
            }, 100)
          })
        } else {
          this.$message({ message: '风险数据加载失败', type: 'error', duration: 3000 })
        }
      } catch (error) {
        console.error('风险监控API调用失败:', error)
        this.$message({ message: '风险数据加载失败，请稍后重试', type: 'error', duration: 3000 })
      }
    },
    /** 加载市场数据 */
    async loadMarketData() {
      this.marketLoading = true
      try {
        const response = await getMarketData()
        if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
          this.marketDataList = response.data?.tlist || []
        } else {
          this.$message({ message: '市场数据加载失败', type: 'error', duration: 3000 })
        }
      } catch (error) {
        console.error('市场数据API调用失败:', error)
        this.$message({ message: '市场数据加载失败，请稍后重试', type: 'error', duration: 3000 })
      } finally {
        this.marketLoading = false
      }
    },
    /** 加载预警数据 */
    async loadAlertData() {
      this.alertLoading = true
      try {
        const response = await getMonitoringAlerts({
          pageNum: this.alertPageNum,
          pageSize: this.alertPageSize
        })
        if (response && [200, 0, '200', '0', '1', 1, 2].includes(response.code)) {
          this.alertList = response.data?.tlist || []
          this.alertTotal = response.data?.totalRecord || 0
        } else {
          this.$message({ message: '预警数据加载失败', type: 'error', duration: 3000 })
        }
      } catch (error) {
        console.error('预警数据API调用失败:', error)
        this.$message({ message: '预警数据加载失败，请稍后重试', type: 'error', duration: 3000 })
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
      this.positionPageNum = 1
      this.loadPositionData()
    },
    /** 持仓重置 */
    handlePositionReset() {
      this.positionSearchForm = { productType: '', currency: '' }
      this.positionPageNum = 1
      this.loadPositionData()
    },
    /** 持仓分页 */
    handlePositionPageChange(page) {
      this.positionPageNum = page
      this.loadPositionData()
    },
    handlePositionSizeChange(size) {
      this.positionPageSize = size
      this.positionPageNum = 1
      this.loadPositionData()
    },
    handlePositionSelectionChange(selection) {
      this.positionSelection = selection
    },
    async handleExportPosition() {
      try {
        const params = {
          ...this.positionSearchForm
        }
        if (this.positionSelection.length > 0) {
          params.ids = this.positionSelection.map(item => item.contractCode || item.id).join(',')
          params.pageSize = this.positionSelection.length
        } else {
          params.pageNum = this.positionPageNum
          params.pageSize = this.positionPageSize
        }
        const res = await exportMonitoringPosition(params)
        const blob = new Blob([res], { type: 'application/vnd.ms-excel' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '持仓监控数据_' + new Date().getTime() + '.csv'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        console.warn('导出持仓数据失败:', error)
        this.$message.error('导出失败，请稍后重试')
      }
    },
    async handleExportAlert() {
      try {
        const params = {}
        if (this.alertIds.length > 0) {
          params.ids = this.alertIds.join(',')
          params.pageSize = this.alertIds.length
        } else {
          params.pageNum = this.alertPageNum
          params.pageSize = this.alertPageSize
        }
        const res = await exportMonitoringAlert(params)
        const blob = new Blob([res], { type: 'application/vnd.ms-excel' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '预警管理数据_' + new Date().getTime() + '.csv'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        console.warn('导出预警数据失败:', error)
        this.$message.error('导出失败，请稍后重试')
      }
    },
    /** 构建预警表单提交数据 */
    buildAlertSubmitData() {
      return {
        ...this.alertForm,
        threshold: this.alertForm.threshold === '' || this.alertForm.threshold == null ? null : Number(this.alertForm.threshold),
        notificationMethod: Array.isArray(this.alertForm.notificationMethod)
          ? this.alertForm.notificationMethod.join(',')
          : (this.alertForm.notificationMethod || '')
      }
    },
    /** 重置预警表单 */
    resetAlertForm() {
      this.alertForm = {
        alertId: null,
        alertName: '',
        alertType: '',
        monitoringMetric: '',
        threshold: '',
        currentValue: '',
        alertLevel: '',
        status: 'ACTIVE',
        notificationMethod: [],
        description: '',
        orgId: this.$store.getters.orgId
      }
      this.$nextTick(() => {
        if (this.$refs.alertForm) {
          this.$refs.alertForm.resetFields()
        }
      })
    },
    /** 新增预警 */
    handleAddAlert() {
      this.alertDialogMode = 'add'
      this.alertDialogTitle = '新增预警规则'
      this.resetAlertForm()
      this.alertDialogOpen = true
    },
    /** 修改预警 */
    handleUpdateAlert() {
      if (this.alertSingle) {
        this.$message.warning('请选择一条预警记录进行修改')
        return
      }
      const row = this.alertList.find(item => this.alertIds.includes(item.alertId))
      if (!row) {
        this.$message.warning('未找到选中的预警记录')
        return
      }
      this.alertDialogMode = 'edit'
      this.alertDialogTitle = '修改预警规则'
      this.alertForm = {
        ...row,
        threshold: row.threshold != null ? String(row.threshold) : '',
        currentValue: row.currentValue != null ? String(row.currentValue) : '',
        notificationMethod: row.notificationMethod
          ? String(row.notificationMethod).split(',').filter(Boolean)
          : [],
        orgId: row.orgId || this.$store.getters.orgId
      }
      this.alertDialogOpen = true
    },
    /** 删除预警 */
    handleDeleteAlert() {
      const ids = this.alertIds && this.alertIds.length ? this.alertIds : []
      if (!ids.length) {
        this.$message.warning('请至少选择一条预警记录')
        return
      }
      this.$modal.confirm(`是否确认删除选中的 ${ids.length} 条预警记录？`).then(async () => {
        try {
          const response = await deleteMonitoringAlert(ids)
          if (response && [200, '200', 1, '1'].includes(response.code)) {
            this.$message.success('删除成功')
            this.alertPageNum = 1
            this.loadAlertData()
          } else {
            this.$message.error('删除失败，请稍后重试')
          }
        } catch (error) {
          console.error('删除预警失败:', error)
          this.$message.error('删除失败，请稍后重试')
        }
      }).catch(() => {})
    },
    /** 提交预警表单 */
    async submitAlertForm() {
      this.$refs['alertForm'].validate(async valid => {
        if (valid) {
          const submitData = this.buildAlertSubmitData()
          try {
            const requestApi = this.alertDialogMode === 'edit' ? updateMonitoringAlert : setMonitoringAlert
            const response = await requestApi(submitData)
            if (response && [200, '200', 1, '1', 2, '2'].includes(response.code)) {
              this.$message.success(this.alertDialogMode === 'edit' ? '修改成功' : '新增成功')
              this.alertDialogOpen = false
              // 新增时跳回第1页，修改时保持当前页
              if (this.alertDialogMode === 'add') this.alertPageNum = 1
              this.loadAlertData()
            } else {
              throw new Error('API返回状态异常')
            }
          } catch (error) {
            console.warn(this.alertDialogMode === 'edit' ? '修改预警规则失败:' : '新增预警规则失败:', error)
            this.$message.error(this.alertDialogMode === 'edit' ? '修改失败，请稍后重试' : '新增失败，请稍后重试')
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
    /** 预警分页 */
    handleAlertPageChange(page) {
      this.alertPageNum = page
      this.loadAlertData()
    },
    handleAlertSizeChange(size) {
      this.alertPageSize = size
      this.alertPageNum = 1
      this.loadAlertData()
    },
    /** 处理预警 */
    async handleProcessAlert(row) {
      try {
        const response = await processMonitoringAlert(row.alertId)
        if (response && [200, '200', 1, '1', 2, '2'].includes(response.code)) {
          this.$message.success('预警已处理')
          this.loadAlertData()
        } else {
          throw new Error('API返回状态异常')
        }
      } catch (error) {
        console.warn('处理预警失败:', error)
        this.$message.error('处理失败，请稍后重试')
      }
    },
    /** 忽略预警 */
    async handleIgnoreAlert(row) {
      try {
        const response = await ignoreMonitoringAlert(row.alertId)
        if (response && [200, '200', 1, '1', 2, '2'].includes(response.code)) {
          this.$message.success('预警已忽略')
          this.loadAlertData()
        } else {
          throw new Error('API返回状态异常')
        }
      } catch (error) {
        console.warn('忽略预警失败:', error)
        this.$message.error('忽略失败，请稍后重试')
      }
    },
    /** 风险分布饼图 */
    renderRiskDistributionChart() {
      const echarts = require('echarts')
      const el = document.getElementById('riskDistributionChart')
      if (!el) return
      const chart = echarts.getInstanceByDom(el) || echarts.init(el)
      const data = (this.riskData.riskDistribution || []).filter(d => Number(d.value) > 0)
      chart.setOption({
        tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
        legend: { bottom: 0, type: 'scroll' },
        series: [{
          type: 'pie',
          radius: ['40%', '65%'],
          center: ['50%', '45%'],
          data: data.length ? data : [{ name: '暂无数据', value: 1 }],
          label: { formatter: '{b}\n{d}%' },
          emphasis: { itemStyle: { shadowBlur: 10, shadowOffsetX: 0, shadowColor: 'rgba(0,0,0,0.5)' } }
        }]
      })
    },
    /** 损益分布柱状图 */
    renderPnlDistributionChart() {
      const echarts = require('echarts')
      const el = document.getElementById('pnlDistributionChart')
      if (!el) return
      const chart = echarts.getInstanceByDom(el) || echarts.init(el)
      const dist = this.riskData.pnlDistribution || []
      chart.setOption({
        tooltip: { trigger: 'axis' },
        grid: { left: '3%', right: '4%', bottom: '15%', containLabel: true },
        xAxis: {
          type: 'category',
          data: dist.map(d => d.range),
          axisLabel: { rotate: 30, fontSize: 10 }
        },
        yAxis: { type: 'value', name: '笔数' },
        series: [{
          type: 'bar',
          data: dist.map(d => d.count),
          itemStyle: { color: '#409eff' },
          label: { show: true, position: 'top' }
        }]
      })
    },
    /** 获取市场数据类型标签 */
    getDataTypeTag(type) {
      const tagMap = { 'FUTURES': 'warning', 'OPTION': 'success', 'FX': 'primary', 'INDEX': 'info', 'FORWARD': '' }
      return tagMap[type] || ''
    },
    /** 获取市场数据类型中文名 */
    getDataTypeText(type) {
      const textMap = { 'FUTURES': '期货', 'OPTION': '期权', 'FX': '外汇', 'INDEX': '指数', 'FORWARD': '远期' }
      return textMap[type] || type
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
}

/* 持仓分页居中 */
.position-pagination {
  padding: 14px 0 6px;
  display: flex;
  justify-content: center;
}

/* 风险监控布局优化 */
.risk-monitoring {
  height: 100%;
  overflow: auto;
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

/* 风险监控布局优化 */
.risk-monitoring {
  height: 100%;
  overflow: auto;
}
</style>
