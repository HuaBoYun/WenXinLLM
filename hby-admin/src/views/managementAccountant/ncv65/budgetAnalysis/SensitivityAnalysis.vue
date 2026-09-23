<template>
  <div class="sensitivity-analysis">
    <!-- 页面标题 -->
    <div class="page-header">
      <h2>敏感性分析</h2>
      <p>分析关键变量对预算结果的影响程度，识别敏感因素和风险点</p>
    </div>

    <!-- 操作工具栏 -->
    <el-card class="toolbar-card" shadow="never">
      <el-row :gutter="16">
        <el-col :span="12">
          <el-button-group>
            <el-button type="primary" icon="el-icon-data-analysis" :loading="analysisLoading" @click="handleStartAnalysis">开始分析</el-button>
            <el-button type="success" icon="el-icon-refresh" @click="handleRefreshAll">刷新</el-button>
            <el-button type="warning" icon="el-icon-download" :loading="exportLoading" @click="handleExportAnalysis">导出分析</el-button>
          </el-button-group>
        </el-col>
        <el-col :span="12" class="text-right">
          <el-button-group>
            <el-button icon="el-icon-setting" @click="handleAnalysisSettings">分析设置</el-button>
            <el-button icon="el-icon-help" @click="helpDialogVisible = true">帮助</el-button>
          </el-button-group>
        </el-col>
      </el-row>
    </el-card>

    <!-- 敏感性分析概览 -->
    <el-row :gutter="20" class="stats-row">
      <el-col :span="6">
        <el-card class="stat-card variables-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ sensitivityStats.totalVariables }}</div>
            <div class="stat-label">分析变量</div>
            <div class="stat-description">参与敏感性分析的变量</div>
            <div class="stat-trend">
              <i class="el-icon-s-data"></i>
              <span>多维度分析</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-s-data"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card sensitive-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ sensitivityStats.sensitiveVariables }}</div>
            <div class="stat-label">敏感变量</div>
            <div class="stat-description">高敏感性变量数量</div>
            <div class="stat-trend">
              <i class="el-icon-warning"></i>
              <span>需重点关注</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-warning"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card correlation-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ sensitivityStats.maxCorrelation }}%</div>
            <div class="stat-label">最大相关性</div>
            <div class="stat-description">变量间最大相关系数</div>
            <div class="stat-trend">
              <i class="el-icon-connection"></i>
              <span>相关性分析</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-connection"></i>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card stability-card" shadow="hover">
          <div class="stat-content">
            <div class="stat-number">{{ sensitivityStats.stability }}%</div>
            <div class="stat-label">稳定性指数</div>
            <div class="stat-description">模型稳定性评分</div>
            <div class="stat-trend">
              <i class="el-icon-success"></i>
              <span>稳定性良好</span>
            </div>
          </div>
          <div class="stat-icon">
            <i class="el-icon-success"></i>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 敏感性分析配置 -->
    <el-card class="config-card" shadow="never">
      <div class="config-header">
        <span class="config-title">筛选查询</span>
        <el-button type="text" @click="handleResetQuery">重置</el-button>
      </div>
      <el-form :model="queryForm" :inline="true" size="small">
        <el-form-item label="变量名称">
          <el-input v-model="queryForm.variableName" placeholder="请输入变量名称" clearable style="width:150px" />
        </el-form-item>
        <el-form-item label="变量类型">
          <el-select v-model="queryForm.variableType" placeholder="全部" clearable style="width:130px">
            <el-option value="REVENUE" label="收入" />
            <el-option value="COST" label="成本" />
            <el-option value="MARKET_SHARE" label="市场份额" />
            <el-option value="INFLATION_RATE" label="通胀率" />
            <el-option value="EXCHANGE_RATE" label="汇率" />
            <el-option value="INTEREST_RATE" label="利率" />
            <el-option value="CUSTOM" label="自定义" />
          </el-select>
        </el-form-item>
        <el-form-item label="启用状态">
          <el-select v-model="queryForm.enabled" placeholder="全部" clearable style="width:100px">
            <el-option :value="1" label="启用" />
            <el-option :value="0" label="禁用" />
          </el-select>
        </el-form-item>
        <el-form-item label="分析方法">
          <el-select v-model="queryForm.analysisMethod" placeholder="请选择" style="width:130px">
            <el-option value="ONE_AT_TIME" label="单因子分析" />
            <el-option value="GLOBAL" label="全局敏感性" />
            <el-option value="SOBOL" label="Sobol分析" />
            <el-option value="MORRIS" label="Morris分析" />
            <el-option value="REGRESSION" label="回归分析" />
          </el-select>
        </el-form-item>
        <el-form-item label="目标变量">
          <el-select v-model="queryForm.targetVariable" placeholder="请选择" style="width:120px">
            <el-option value="PROFIT" label="利润" />
            <el-option value="REVENUE" label="收入" />
            <el-option value="COST" label="成本" />
            <el-option value="CASH_FLOW" label="现金流" />
            <el-option value="ROI" label="投资回报率" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" icon="el-icon-search" @click="handleSearch">查询</el-button>
          <el-button icon="el-icon-refresh-left" @click="handleResetQuery">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 敏感性变量配置 -->
    <el-card class="variables-config-card" shadow="never">
      <div slot="header" class="card-header">
        <span>敏感性变量配置</span>
        <div class="header-tools">
          <el-button icon="el-icon-plus" size="mini" @click="handleAddSensitivityVariable">添加变量</el-button>
          <el-button icon="el-icon-refresh" size="mini" @click="handleRefreshVariables">刷新</el-button>
        </div>
      </div>
      <el-table
        v-loading="variableLoading"
        :data="sensitivityVariables"
        border
        size="mini"
        style="width: 100%"
      >
        <el-table-column type="index" label="#" width="50" align="center" />
        <el-table-column prop="variableName" label="变量名称" min-width="130" />
        <el-table-column prop="variableType" label="变量类型" width="110">
          <template slot-scope="scope">
            <el-tag :type="getVariableTypeColor(scope.row.variableType)" size="mini">
              {{ getVariableTypeText(scope.row.variableType) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="baseValue" label="基准值" width="90" align="right" />
        <el-table-column prop="minValue" label="最小值" width="90" align="right" />
        <el-table-column prop="maxValue" label="最大值" width="90" align="right" />
        <el-table-column prop="stepSize" label="步长" width="80" align="right" />
        <el-table-column prop="priority" label="优先级" width="80" align="center" />
        <el-table-column prop="sensitivityLevel" label="敏感等级" width="90" align="center">
          <template slot-scope="scope">
            <el-tag :type="getSensitivityLevelColor(scope.row.sensitivityLevel)" size="mini">
              {{ getSensitivityLevelText(scope.row.sensitivityLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="enabled" label="启用" width="70" align="center">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.enabled"
              :active-value="1"
              :inactive-value="0"
              size="mini"
              @change="handleEnabledChange(scope.row)"
            />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="110" align="center">
          <template slot-scope="scope">
            <el-button type="text" size="mini" icon="el-icon-edit" @click="handleEditSensitivityVariable(scope.row)">编辑</el-button>
            <el-button type="text" size="mini" icon="el-icon-delete" style="color:#F56C6C" @click="handleDeleteSensitivityVariable(scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页 -->
      <div style="margin-top:12px;text-align:right">
        <el-pagination
          background
          layout="total, sizes, prev, pager, next"
          :total="variableTotal"
          :page-size="variablePage.pageSize"
          :current-page="variablePage.pageNum"
          :page-sizes="[10, 20, 50]"
          @current-change="handleVariablePageChange"
          @size-change="handleVariableSizeChange"
        />
      </div>
    </el-card>
    <!-- 变量新增/编辑弹窗 -->
    <el-dialog :title="variableDialogTitle" :visible.sync="variableDialogVisible" width="560px" @close="resetVariableForm">
      <el-form ref="variableForm" :model="variableForm" :rules="variableRules" label-width="100px" size="small">
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="变量名称" prop="variableName">
              <el-input v-model="variableForm.variableName" placeholder="请输入变量名称" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="变量类型" prop="variableType">
              <el-select v-model="variableForm.variableType" placeholder="请选择" style="width:100%">
                <el-option value="REVENUE" label="收入" />
                <el-option value="COST" label="成本" />
                <el-option value="MARKET_SHARE" label="市场份额" />
                <el-option value="INFLATION_RATE" label="通胀率" />
                <el-option value="EXCHANGE_RATE" label="汇率" />
                <el-option value="INTEREST_RATE" label="利率" />
                <el-option value="CUSTOM" label="自定义" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="基准值" prop="baseValue">
              <el-input-number v-model="variableForm.baseValue" :precision="2" controls-position="right" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="步长" prop="stepSize">
              <el-input-number v-model="variableForm.stepSize" :precision="2" :min="0.01" controls-position="right" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="最小值" prop="minValue">
              <el-input-number v-model="variableForm.minValue" :precision="2" controls-position="right" style="width:100%" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="最大值" prop="maxValue">
              <el-input-number v-model="variableForm.maxValue" :precision="2" controls-position="right" style="width:100%" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="16">
          <el-col :span="12">
            <el-form-item label="优先级">
              <el-rate v-model="variableForm.priority" :max="5" style="margin-top:6px" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="是否启用">
              <el-switch v-model="variableForm.enabled" :active-value="1" :inactive-value="0" style="margin-top:6px" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="描述">
          <el-input v-model="variableForm.description" type="textarea" :rows="2" placeholder="请输入描述" />
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="variableDialogVisible = false">取消</el-button>
        <el-button type="primary" :loading="variableSaving" @click="handleSaveVariable">保存</el-button>
      </div>
    </el-dialog>

    <!-- 敏感性分析图表 -->
    <el-row :gutter="20" class="chart-row">
      <el-col :span="12">
        <el-card class="chart-card" shadow="never">
          <div slot="header" class="card-header">
            <span>敏感性图表</span>
            <div class="header-tools">
              <el-radio-group v-model="sensitivityChartType" size="mini">
                <el-radio-button label="tornado">龙卷风图</el-radio-button>
                <el-radio-button label="spider">蜘蛛图</el-radio-button>
                <el-radio-button label="scatter">散点图</el-radio-button>
              </el-radio-group>
            </div>
          </div>
          <div id="sensitivityChart" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card" shadow="never">
          <div slot="header" class="card-header">
            <span>相关性分析</span>
            <div class="header-tools">
              <el-radio-group v-model="correlationChartType" size="mini">
                <el-radio-button label="heatmap">热力图</el-radio-button>
                <el-radio-button label="network">网络图</el-radio-button>
                <el-radio-button label="matrix">矩阵图</el-radio-button>
              </el-radio-group>
            </div>
          </div>
          <div id="correlationChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 敏感性指标 -->
    <el-row :gutter="20" class="metrics-row">
      <el-col :span="8">
        <el-card class="chart-card" shadow="never">
          <div slot="header" class="card-header">
            <span>敏感性指数</span>
            <el-button icon="el-icon-refresh" size="mini" @click="refreshSensitivityIndex" />
          </div>
          <div id="sensitivityIndexChart" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="chart-card" shadow="never">
          <div slot="header" class="card-header">
            <span>影响程度分布</span>
            <el-button icon="el-icon-refresh" size="mini" @click="refreshImpactDistribution" />
          </div>
          <div id="impactDistributionChart" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card class="ranking-card" shadow="never">
          <div slot="header" class="card-header">
            <span>敏感性排名</span>
            <el-button icon="el-icon-refresh" size="mini" @click="refreshSensitivityRanking" />
          </div>
          <div class="ranking-list">
            <div
              v-for="(item, index) in sensitivityRanking"
              :key="item.id"
              class="ranking-item"
              :class="getSensitivityRankingClass(index)"
            >
              <div class="ranking-number">{{ index + 1 }}</div>
              <div class="ranking-content">
                <div class="ranking-name">{{ item.variableName }}</div>
                <div class="ranking-sensitivity">敏感性: {{ item.sensitivity }}%</div>
              </div>
              <div class="ranking-badge">
                <el-tag :type="getSensitivityLevelColor(item.sensitivityLevel)" size="mini">
                  {{ getSensitivityLevelText(item.sensitivityLevel) }}
                </el-tag>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <!-- 设置对话框 -->
    <el-dialog title="敏感性分析设置" :visible.sync="settingsDialogVisible" width="500px">
      <el-form label-width="120px" size="small">
        <el-form-item label="每页显示条数">
          <el-select v-model="variablePage.pageSize" style="width: 100%" @change="loadVariables">
            <el-option label="10条" :value="10" />
            <el-option label="20条" :value="20" />
            <el-option label="50条" :value="50" />
          </el-select>
        </el-form-item>
      </el-form>
      <div slot="footer">
        <el-button @click="settingsDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="settingsDialogVisible = false; $message.success('设置已保存')">保存</el-button>
      </div>
    </el-dialog>

    <!-- 帮助对话框 -->
    <el-dialog title="敏感性分析帮助" :visible.sync="helpDialogVisible" width="600px">
      <div class="help-content">
        <h4>功能说明</h4>
        <p>敏感性分析用于评估关键因素变化对预算的影响程度。</p>
        <h4>操作指南</h4>
        <p>1. 使用顶部工具栏的按钮进行创建、刷新和导出操作。</p>
        <p>2. 使用筛选条件缩小分析范围。</p>
        <p>3. 点击表格行查看详细信息。</p>
      </div>
      <div slot="footer"><el-button type="primary" @click="helpDialogVisible = false">我知道了</el-button></div>
    </el-dialog>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { budgetAnalysisApi } from '@/api/managementAccountant/ncv65/budgetAnalysis'

export default {
  name: 'SensitivityAnalysis',
  data() {
    return {
      settingsDialogVisible: false,
      helpDialogVisible: false,
      // 筛选表单
      queryForm: {
        variableName: '',
        variableType: '',
        enabled: '',
        analysisMethod: 'ONE_AT_TIME',
        targetVariable: 'PROFIT'
      },
      // 变量列表分页
      variableLoading: false,
      sensitivityVariables: [],
      variableTotal: 0,
      variablePage: { pageNum: 1, pageSize: 20 },
      // 图表类型
      sensitivityChartType: 'tornado',
      correlationChartType: 'heatmap',
      // 缓存原始图表数据，供切换图表类型时复用
      _sensitivityChartData: null,
      _correlationChartData: null,
      // 统计数据
      sensitivityStats: {
        totalVariables: 0,
        sensitiveVariables: 0,
        maxCorrelation: 0,
        stability: 0
      },
      // 敏感性排名（来自图表接口）
      sensitivityRanking: [],
      // 分析/导出 loading
      analysisLoading: false,
      exportLoading: false,
      // 变量弹窗
      variableDialogVisible: false,
      variableDialogTitle: '添加变量',
      variableSaving: false,
      variableForm: {
        id: null,
        variableName: '',
        variableType: 'CUSTOM',
        baseValue: 0,
        minValue: 0,
        maxValue: 0,
        stepSize: 0.1,
        priority: 1,
        enabled: 1,
        description: ''
      },
      variableRules: {
        variableName: [{ required: true, message: '请输入变量名称', trigger: 'blur' }],
        variableType: [{ required: true, message: '请选择变量类型', trigger: 'change' }],
        baseValue: [{ required: true, message: '请输入基准值', trigger: 'blur' }]
      }
    }
  },

  mounted() {
    this.loadSensitivityStats()
    this.loadVariables()
    // 延迟初始化图表，确保 DOM 已完全渲染
    setTimeout(() => {
      this.loadSensitivityChartData()
    }, 300)
  },

  watch: {
    sensitivityChartType() {
      this._renderSensitivityChart(this._sensitivityChartData)
    },
    correlationChartType() {
      this._renderCorrelationChart(this._correlationChartData)
    }
  },

  created() {
    // created 中不调用图表初始化
  },

  methods: {
    // ===== 统计数据 =====
    async loadSensitivityStats() {
      try {
        const response = await budgetAnalysisApi.getSensitivityStats()
        if (response.code === 1 && response.data) {
          this.sensitivityStats = { ...this.sensitivityStats, ...response.data }
        }
      } catch (error) {
        console.error('加载统计数据失败：', error)
      }
    },

    // ===== 变量列表 =====
    async loadVariables() {
      this.variableLoading = true
      try {
        const params = {
          pageNum: this.variablePage.pageNum,
          pageSize: this.variablePage.pageSize,
          variableName: this.queryForm.variableName || undefined,
          variableType: this.queryForm.variableType || undefined,
          enabled: this.queryForm.enabled === '' ? undefined : this.queryForm.enabled
        }
        const response = await budgetAnalysisApi.getSensitivityVariablePage(params)
        if (response.code === 1 && response.data) {
          const d = response.data
          this.sensitivityVariables = d.list || d.records || []
          this.variableTotal = d.total || d.totalRecord || 0
        }
      } catch (error) {
        console.error('加载变量列表失败：', error)
      } finally {
        this.variableLoading = false
      }
    },

    handleSearch() {
      this.variablePage.pageNum = 1
      this.loadVariables()
    },

    handleResetQuery() {
      this.queryForm = { variableName: '', variableType: '', enabled: '', analysisMethod: 'ONE_AT_TIME', targetVariable: 'PROFIT' }
      this.variablePage.pageNum = 1
      this.loadVariables()
    },

    handleVariablePageChange(page) {
      this.variablePage.pageNum = page
      this.loadVariables()
    },

    handleVariableSizeChange(size) {
      this.variablePage.pageSize = size
      this.variablePage.pageNum = 1
      this.loadVariables()
    },

    // ===== 图表 =====
    async loadSensitivityChartData() {
      try {
        const response = await budgetAnalysisApi.getSensitivityChartData({})
        if (response.code === 1 && response.data) {
          const d = response.data
          if (d.ranking) {
            this.sensitivityRanking = d.ranking
          }
          // 缓存原始数据，供切换图表类型时复用
          this._sensitivityChartData = d.sensitivityChart
          this._correlationChartData = d.correlationChart
          this._renderSensitivityChart(d.sensitivityChart)
          this._renderCorrelationChart(d.correlationChart)
          this._renderIndexChart(d.indexChart)
          this._renderDistributionChart(d.distributionChart)
        }
      } catch (error) {
        console.error('加载图表数据失败：', error)
      }
    },

    _getOrInitChart(domId) {
      const dom = document.getElementById(domId)
      if (!dom) return null
      const existing = echarts.getInstanceByDom(dom)
      if (existing) existing.dispose()
      return echarts.init(dom)
    },

    // ===== 敏感性图表（龙卷风图 / 蜘蛛图 / 散点图）=====
    _renderSensitivityChart(data) {
      const chart = this._getOrInitChart('sensitivityChart')
      if (!chart) return
      const type = this.sensitivityChartType
      const yAxis = (data && data.yAxis) ? data.yAxis : []
      const series = (data && data.series) ? data.series : []
      const negData = series[0] ? series[0].data || [] : []
      const posData = series[1] ? series[1].data || [] : []

      if (type === 'tornado') {
        // 龙卷风图（水平双向柱状图）
        chart.setOption({
          tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
          legend: { data: ['负向影响', '正向影响'], top: 5 },
          grid: { left: '3%', right: '4%', bottom: '3%', top: '40px', containLabel: true },
          xAxis: { type: 'value', axisLabel: { formatter: '{value}%' } },
          yAxis: { type: 'category', data: yAxis },
          series: [
            { name: '负向影响', type: 'bar', stack: 'total', data: negData, itemStyle: { color: '#F56C6C' } },
            { name: '正向影响', type: 'bar', stack: 'total', data: posData, itemStyle: { color: '#67C23A' } }
          ]
        }, true)
      } else if (type === 'spider') {
        // 蜘蛛图（雷达图）
        const radarNames = yAxis.slice(0, 8)
        const absMax = Math.max(...posData.map(Math.abs), ...negData.map(Math.abs), 1)
        const indicator = radarNames.map(name => ({ name, max: absMax }))
        const radarValues = radarNames.map((_, i) => posData[i] !== undefined ? Math.abs(posData[i]) : 0)
        chart.setOption({
          tooltip: { trigger: 'item' },
          legend: { data: ['敏感性影响'], top: 5 },
          radar: { indicator, center: ['50%', '55%'], radius: '65%', splitNumber: 4,
            axisName: { color: '#606266', fontSize: 11 },
            splitArea: { areaStyle: { color: ['rgba(64,158,255,0.05)', 'rgba(64,158,255,0.1)'] } } },
          series: [{ name: '敏感性影响', type: 'radar',
            data: [{ value: radarValues, name: '敏感性影响',
              areaStyle: { color: 'rgba(64,158,255,0.2)' },
              lineStyle: { color: '#409EFF' },
              itemStyle: { color: '#409EFF' } }] }]
        }, true)
      } else if (type === 'scatter') {
        // 散点图（x=负向影响绝对值，y=正向影响，气泡大小=综合影响）
        const scatterData = yAxis.map((name, i) => {
          const neg = Math.abs(negData[i] || 0)
          const pos = posData[i] || 0
          return [neg, pos, neg + pos, name]
        })
        chart.setOption({
          tooltip: { formatter: p => `${p.data[3]}<br/>负向: ${p.data[0]}%<br/>正向: ${p.data[1]}%<br/>综合: ${p.data[2].toFixed(1)}%` },
          grid: { left: '3%', right: '4%', bottom: '10%', top: '10%', containLabel: true },
          xAxis: { name: '负向影响(%)', type: 'value', nameLocation: 'end' },
          yAxis: { name: '正向影响(%)', type: 'value', nameLocation: 'end' },
          series: [{ type: 'scatter',
            data: scatterData,
            symbolSize: d => Math.max(d[2] * 3, 8),
            itemStyle: { color: p => {
              const v = p.data[2]
              if (v > 150) return '#F56C6C'
              if (v > 100) return '#E6A23C'
              if (v > 50) return '#409EFF'
              return '#67C23A'
            }, opacity: 0.8 },
            label: { show: true, formatter: p => p.data[3], position: 'top', fontSize: 10 } }]
        }, true)
      }
    },

    // ===== 相关性图表（热力图 / 网络图 / 矩阵图）=====
    _renderCorrelationChart(data) {
      const chart = this._getOrInitChart('correlationChart')
      if (!chart) return
      const type = this.correlationChartType
      const xAxis = (data && data.xAxis) ? data.xAxis : []
      const yAxis = (data && data.yAxis) ? data.yAxis : []
      const heatData = (data && data.data) ? data.data : []

      if (type === 'heatmap') {
        // 热力图
        chart.setOption({
          tooltip: { position: 'top', formatter: p => `${xAxis[p.data[0]]} - ${yAxis[p.data[1]]}: ${p.data[2]}` },
          grid: { height: '60%', top: '10%', containLabel: true },
          xAxis: { type: 'category', data: xAxis, splitArea: { show: true }, axisLabel: { rotate: 30, fontSize: 11 } },
          yAxis: { type: 'category', data: yAxis, splitArea: { show: true }, axisLabel: { fontSize: 11 } },
          visualMap: { min: -1, max: 1, calculable: true, orient: 'horizontal', left: 'center', bottom: '5%',
            inRange: { color: ['#F56C6C', '#FFFFFF', '#409EFF'] } },
          series: [{ name: '相关性', type: 'heatmap', data: heatData,
            label: { show: xAxis.length <= 8, fontSize: 10 },
            emphasis: { itemStyle: { shadowBlur: 10, shadowColor: 'rgba(0,0,0,0.5)' } } }]
        }, true)
      } else if (type === 'network') {
        // 网络图（力导向图）
        const nodes = xAxis.map((name, i) => ({
          id: String(i), name, symbolSize: 30 + i * 3,
          itemStyle: { color: ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C', '#909399', '#9B59B6'][i % 6] },
          label: { show: true, fontSize: 11 }
        }))
        const edges = []
        heatData.forEach(([x, y, val]) => {
          if (x < y && Math.abs(val) > 0.3) {
            edges.push({ source: String(x), target: String(y),
              lineStyle: { width: Math.abs(val) * 4, color: val > 0 ? '#409EFF' : '#F56C6C', opacity: 0.7 },
              value: val })
          }
        })
        chart.setOption({
          tooltip: { formatter: p => p.dataType === 'edge' ? `相关性: ${p.data.value}` : p.data.name },
          series: [{ type: 'graph', layout: 'force',
            force: { repulsion: 120, edgeLength: [60, 120] },
            roam: true, draggable: true,
            nodes, edges,
            label: { show: true, position: 'bottom', fontSize: 11 } }]
        }, true)
      } else if (type === 'matrix') {
        // 矩阵图（气泡矩阵，气泡大小=相关性绝对值）
        const matrixData = heatData.map(([x, y, val]) => [x, y, Math.abs(val), val])
        chart.setOption({
          tooltip: { formatter: p => `${xAxis[p.data[0]]} - ${yAxis[p.data[1]]}<br/>相关性: ${p.data[3]}` },
          grid: { containLabel: true, left: '5%', right: '5%', top: '10%', bottom: '15%' },
          xAxis: { type: 'category', data: xAxis, axisLabel: { rotate: 30, fontSize: 11 }, splitLine: { show: true } },
          yAxis: { type: 'category', data: yAxis, axisLabel: { fontSize: 11 }, splitLine: { show: true } },
          series: [{ type: 'scatter',
            data: matrixData,
            symbolSize: d => Math.max(d[2] * 40, 6),
            itemStyle: { color: p => p.data[3] >= 0 ? '#409EFF' : '#F56C6C', opacity: 0.75 },
            label: { show: xAxis.length <= 6, formatter: p => p.data[3], fontSize: 10 } }]
        }, true)
      }
    },

    _renderIndexChart(data) {
      const chart = this._getOrInitChart('sensitivityIndexChart')
      if (!chart) return
      const pieData = (data && data.data) ? data.data : []
      chart.setOption({
        tooltip: { trigger: 'item', formatter: '{b}: {c} ({d}%)' },
        legend: { orient: 'vertical', right: '5%', top: 'center' },
        color: ['#F56C6C', '#E6A23C', '#409EFF', '#909399'],
        series: [{
          name: '敏感性指数', type: 'pie', radius: ['40%', '65%'],
          center: ['40%', '50%'],
          data: pieData,
          label: { formatter: '{b}\n{d}%' },
          emphasis: { itemStyle: { shadowBlur: 10, shadowOffsetX: 0, shadowColor: 'rgba(0,0,0,0.5)' } }
        }]
      }, true)
    },

    _renderDistributionChart(data) {
      const chart = this._getOrInitChart('impactDistributionChart')
      if (!chart) return
      const xData = (data && data.xAxis) ? data.xAxis : []
      const barData = (data && data.data) ? data.data : []
      chart.setOption({
        tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
        grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
        xAxis: { type: 'category', data: xData },
        yAxis: { type: 'value', name: '变量数量', minInterval: 1 },
        series: [{
          name: '变量数量', type: 'bar', data: barData, barMaxWidth: 50,
          itemStyle: { color: (p) => ['#F56C6C', '#E6A23C', '#409EFF', '#67C23A'][p.dataIndex] || '#909399' },
          label: { show: true, position: 'top' }
        }]
      }, true)
    },

    // ===== 工具栏操作 =====
    async handleStartAnalysis() {
      this.analysisLoading = true
      try {
        const params = { analysisMethod: this.queryForm.analysisMethod, targetVariable: this.queryForm.targetVariable }
        const response = await budgetAnalysisApi.analyzeSensitivity(params)
        if (response.code === 1) {
          this.$message.success('分析完成')
          this.loadSensitivityStats()
          this.loadSensitivityChartData()
        } else {
          this.$message.error(response.msg || '分析失败')
        }
      } catch (error) {
        this.$message.error('分析失败：' + error.message)
      } finally {
        this.analysisLoading = false
      }
    },

    handleRefreshAll() {
      this.loadSensitivityStats()
      this.loadVariables()
      this.loadSensitivityChartData()
      this.$message.success('已刷新')
    },

    async handleExportAnalysis() {
      this.exportLoading = true
      try {
        await budgetAnalysisApi.exportSensitivityAnalysis({})
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + error.message)
      } finally {
        this.exportLoading = false
      }
    },

    handleAnalysisSettings() {
      this.settingsDialogVisible = true
    },

    // ===== 变量 CRUD =====
    handleAddSensitivityVariable() {
      this.variableDialogTitle = '添加变量'
      this.variableForm = { id: null, variableName: '', variableType: 'CUSTOM', baseValue: 0, minValue: 0, maxValue: 0, stepSize: 0.1, priority: 1, enabled: 1, description: '' }
      this.variableDialogVisible = true
    },

    handleRefreshVariables() {
      this.loadVariables()
    },

    handleEditSensitivityVariable(row) {
      this.variableDialogTitle = '编辑变量'
      this.variableForm = { ...row }
      this.variableDialogVisible = true
    },

    handleDeleteSensitivityVariable(row) {
      this.$confirm('确定删除该变量吗？', '提示', { type: 'warning' }).then(async () => {
        try {
          const response = await budgetAnalysisApi.deleteSensitivityVariable(row.id)
          if (response.code === 1) {
            this.$message.success('删除成功')
            this.loadVariables()
          } else {
            this.$message.error(response.msg || '删除失败')
          }
        } catch (error) {
          this.$message.error('删除失败：' + error.message)
        }
      }).catch(() => {})
    },

    async handleEnabledChange(row) {
      try {
        const response = await budgetAnalysisApi.updateSensitivityVariableEnabled(row.id, row.enabled)
        if (response.code !== 1) {
          row.enabled = row.enabled === 1 ? 0 : 1
          this.$message.error(response.msg || '操作失败')
        }
      } catch (error) {
        row.enabled = row.enabled === 1 ? 0 : 1
        this.$message.error('操作失败：' + error.message)
      }
    },

    handleSaveVariable() {
      this.$refs.variableForm.validate(async (valid) => {
        if (!valid) return
        this.variableSaving = true
        try {
          const isEdit = !!this.variableForm.id
          const api = isEdit ? budgetAnalysisApi.updateSensitivityVariable : budgetAnalysisApi.createSensitivityVariable
          const response = await api(this.variableForm)
          if (response.code === 1) {
            this.$message.success(isEdit ? '更新成功' : '添加成功')
            this.variableDialogVisible = false
            this.loadVariables()
          } else {
            this.$message.error(response.msg || '保存失败')
          }
        } catch (error) {
          this.$message.error('保存失败：' + error.message)
        } finally {
          this.variableSaving = false
        }
      })
    },

    resetVariableForm() {
      this.$refs.variableForm && this.$refs.variableForm.resetFields()
    },

    // ===== 刷新子图表 =====
    refreshSensitivityIndex() { this.loadSensitivityChartData() },
    refreshImpactDistribution() { this.loadSensitivityChartData() },
    refreshSensitivityRanking() { this.loadSensitivityChartData() },

    // ===== 辅助方法 =====
    getVariableTypeColor(type) {
      const colorMap = {
        'REVENUE': 'success',
        'COST': 'warning',
        'MARKET_SHARE': 'primary',
        'INFLATION_RATE': 'info',
        'EXCHANGE_RATE': 'danger',
        'INTEREST_RATE': 'success'
      }
      return colorMap[type] || 'info'
    },
    
    // 获取变量类型文本
    getVariableTypeText(type) {
      const textMap = {
        'REVENUE': '收入',
        'COST': '成本',
        'MARKET_SHARE': '市场份额',
        'INFLATION_RATE': '通胀率',
        'EXCHANGE_RATE': '汇率',
        'INTEREST_RATE': '利率'
      }
      return textMap[type] || type
    },
    
    // 获取敏感性排名样式类
    getSensitivityRankingClass(index) {
      if (index === 0) return 'first-place'
      if (index === 1) return 'second-place'
      if (index === 2) return 'third-place'
      return 'other-place'
    },
    
    // 获取敏感性等级颜色
    getSensitivityLevelColor(level) {
      const colorMap = {
        'HIGH': 'danger',
        'MEDIUM': 'warning',
        'LOW': 'success',
        'NONE': 'info'
      }
      return colorMap[level] || 'info'
    },
    
    // 获取敏感性等级文本
    getSensitivityLevelText(level) {
      const textMap = {
        'HIGH': '高敏感',
        'MEDIUM': '中敏感',
        'LOW': '低敏感',
        'NONE': '不敏感'
      }
      return textMap[level] || level
    }
  }
}
</script>

<style lang="scss" scoped>
.sensitivity-analysis {
  padding: 20px;

  .page-header {
    margin-bottom: 20px;

    h2 {
      color: #303133;
      font-size: 24px;
      margin: 0 0 8px 0;
    }

    p {
      color: #606266;
      font-size: 14px;
      margin: 0;
    }
  }

  .toolbar-card,
  .config-card,
  .variables-config-card {
    margin-bottom: 20px;
  }

  .stats-row {
    margin-bottom: 20px;

    .stat-card {
      border: none;
      border-radius: 8px;
      position: relative;
      overflow: hidden;

      &.variables-card {
        background: linear-gradient(135deg, #409EFF, #66B1FF);
        color: white;
      }

      &.sensitive-card {
        background: linear-gradient(135deg, #F56C6C, #F78989);
        color: white;
      }

      &.correlation-card {
        background: linear-gradient(135deg, #E6A23C, #EEBE77);
        color: white;
      }

      &.stability-card {
        background: linear-gradient(135deg, #67C23A, #85CE61);
        color: white;
      }

      .stat-content {
        position: relative;
        z-index: 2;

        .stat-number {
          font-size: 24px;
          font-weight: 600;
          margin-bottom: 4px;
        }

        .stat-label {
          font-size: 14px;
          opacity: 0.9;
          margin-bottom: 2px;
        }

        .stat-description {
          font-size: 12px;
          opacity: 0.8;
          margin-bottom: 8px;
        }

        .stat-trend {
          font-size: 12px;
          opacity: 0.9;
        }
      }

      .stat-icon {
        position: absolute;
        top: 20px;
        right: 20px;
        font-size: 48px;
        opacity: 0.3;
      }
    }
  }

  .config-card,
  .variables-config-card {
    .config-header,
    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 16px;

      .config-title {
        font-size: 16px;
        font-weight: 500;
        color: #303133;
      }

      .header-tools {
        display: flex;
        align-items: center;
        gap: 10px;
      }
    }

    .form-unit {
      margin-left: 8px;
      color: #606266;
      font-size: 12px;
    }
  }

  .chart-row,
  .metrics-row {
    margin-bottom: 20px;

    .chart-card,
    .ranking-card {
      .card-header {
        display: flex;
        justify-content: space-between;
        align-items: center;

        .header-tools {
          display: flex;
          align-items: center;
          gap: 10px;
        }
      }

      .chart-container {
        height: 300px;
      }
    }

    .ranking-card {
      .ranking-list {
        max-height: 300px;
        overflow-y: auto;

        .ranking-item {
          display: flex;
          align-items: center;
          padding: 12px;
          margin-bottom: 8px;
          border-radius: 6px;
          background: #F5F7FA;
          transition: all 0.3s ease;

          &:hover {
            background: #E4E7ED;
            transform: translateX(4px);
          }

          &.first-place {
            background: linear-gradient(135deg, #FFD700, #FFA500);
            color: white;
            box-shadow: 0 4px 8px rgba(255, 215, 0, 0.3);
          }

          &.second-place {
            background: linear-gradient(135deg, #C0C0C0, #A9A9A9);
            color: white;
            box-shadow: 0 4px 8px rgba(192, 192, 192, 0.3);
          }

          &.third-place {
            background: linear-gradient(135deg, #CD7F32, #B8860B);
            color: white;
            box-shadow: 0 4px 8px rgba(205, 127, 50, 0.3);
          }

          .ranking-number {
            width: 32px;
            height: 32px;
            border-radius: 50%;
            background: rgba(255, 255, 255, 0.2);
            display: flex;
            align-items: center;
            justify-content: center;
            font-weight: 600;
            margin-right: 12px;
          }

          .ranking-content {
            flex: 1;

            .ranking-name {
              font-size: 14px;
              font-weight: 500;
              margin-bottom: 2px;
            }

            .ranking-sensitivity {
              font-size: 12px;
              opacity: 0.8;
            }
          }

          .ranking-badge {
            margin-left: 8px;
          }
        }
      }
    }
  }

  .text-right {
    text-align: right;
  }
}
</style>
