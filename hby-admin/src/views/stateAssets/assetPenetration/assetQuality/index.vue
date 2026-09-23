<template>
  <div class="asset-quality-container asset-page">
    <div class="page-header" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="page-header-left"><i class="el-icon-medal"></i><span>资产质量</span></div>
      <div class="page-header-desc">评估企业资产质量等级、不良资产率与减值分析</div>
    </div>
    <!-- 统计概览 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="6">
        <el-card shadow="hover" class="stats-card">
          <div class="stats-content">
            <div class="stats-icon">
              <i class="el-icon-medal" style="color: #1890ff;"></i>
            </div>
            <div class="stats-info">
              <div class="stats-value">{{ statistics.totalAssets }}</div>
              <div class="stats-label">评估资产数</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stats-card">
          <div class="stats-content">
            <div class="stats-icon">
              <i class="el-icon-star-on" style="color: #67C23A;"></i>
            </div>
            <div class="stats-info">
              <div class="stats-value">{{ statistics.highQualityAssets }}</div>
              <div class="stats-label">优质资产</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stats-card">
          <div class="stats-content">
            <div class="stats-icon">
              <i class="el-icon-warning" style="color: #E6A23C;"></i>
            </div>
            <div class="stats-info">
              <div class="stats-value">{{ statistics.riskAssets }}</div>
              <div class="stats-label">风险资产</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stats-card">
          <div class="stats-content">
            <div class="stats-icon">
              <i class="el-icon-data-line" style="color: #F56C6C;"></i>
            </div>
            <div class="stats-info">
              <div class="stats-value">{{ statistics.avgQualityScore }}%</div>
              <div class="stats-label">平均质量评分</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 质量分析图表 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="12">
        <el-card shadow="never">
          <div slot="header">
            <span>资产质量分布</span>
            <el-button
              style="float: right; padding: 3px 0"
              type="text"
              @click="refreshQualityChart"
            >
              刷新
            </el-button>
          </div>
          <div ref="qualityDistributionChart" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="never">
          <div slot="header">
            <span>质量趋势分析</span>
            <el-button
              style="float: right; padding: 3px 0"
              type="text"
              @click="refreshTrendChart"
            >
              刷新
            </el-button>
          </div>
          <div ref="qualityTrendChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="mb-20">
      <el-col :span="12">
        <el-card shadow="never">
          <div slot="header">
            <span>收益率分析</span>
            <el-button
              style="float: right; padding: 3px 0"
              type="text"
              @click="refreshReturnChart"
            >
              刷新
            </el-button>
          </div>
          <div ref="returnAnalysisChart" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="never">
          <div slot="header">
            <span>减值风险预警</span>
            <el-button
              style="float: right; padding: 3px 0"
              type="text"
              @click="refreshRiskChart"
            >
              刷新
            </el-button>
          </div>
          <div ref="impairmentRiskChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询条件 -->
    <el-card shadow="never" class="mb-20">
      <div slot="header">
        <span>查询条件</span>
        <el-button
          style="float: right; padding: 3px 0"
          type="text"
          @click="resetSearchForm"
        >
          重置
        </el-button>
      </div>
      
      <el-form
        ref="searchForm"
        :model="searchForm"
        :inline="true"
        label-width="100px"
      >
        <el-form-item label="企业名称">
          <el-input
            v-model="searchForm.enterpriseName"
            placeholder="请输入企业名称"
            clearable
            style="width: 200px;"
          />
        </el-form-item>
        
        <el-form-item label="资产名称">
          <el-input
            v-model="searchForm.assetName"
            placeholder="请输入资产名称"
            clearable
            style="width: 200px;"
          />
        </el-form-item>
        
        <el-form-item label="资产类型">
          <el-select
            v-model="searchForm.assetType"
            placeholder="请选择资产类型"
            clearable
            style="width: 150px;"
          >
            <el-option label="固定资产" value="FIXED_ASSETS"></el-option>
            <el-option label="流动资产" value="CURRENT_ASSETS"></el-option>
            <el-option label="无形资产" value="INTANGIBLE_ASSETS"></el-option>
            <el-option label="投资性资产" value="INVESTMENT_ASSETS"></el-option>
            <el-option label="金融资产" value="FINANCIAL_ASSETS"></el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="质量等级">
          <el-select
            v-model="searchForm.qualityLevel"
            placeholder="请选择质量等级"
            clearable
            style="width: 120px;"
          >
            <el-option label="优质" value="EXCELLENT"></el-option>
            <el-option label="良好" value="GOOD"></el-option>
            <el-option label="一般" value="AVERAGE"></el-option>
            <el-option label="较差" value="POOR"></el-option>
            <el-option label="风险" value="RISK"></el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="评估状态">
          <el-select
            v-model="searchForm.assessmentStatus"
            placeholder="请选择评估状态"
            clearable
            style="width: 120px;"
          >
            <el-option label="待评估" value="PENDING"></el-option>
            <el-option label="评估中" value="ASSESSING"></el-option>
            <el-option label="已完成" value="COMPLETED"></el-option>
            <el-option label="评估失败" value="FAILED"></el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="评估时间">
          <el-date-picker
            v-model="searchForm.assessmentDateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="yyyy-MM-dd"
            style="width: 240px;"
          />
        </el-form-item>
        
        <el-form-item>
          <el-button type="primary" @click="handleSearch">
            <i class="el-icon-search"></i> 查询
          </el-button>
          <el-button @click="resetSearchForm">
            <i class="el-icon-refresh"></i> 重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- 操作按钮 -->
    <el-card shadow="never" class="mb-20">
      <el-button type="primary" @click="handleAdd">
        <i class="el-icon-plus"></i> 新增评估
      </el-button>
      <el-button
        type="success"
        :disabled="selectedAssets.length === 0"
        @click="handleBatchAssess"
      >
        <i class="el-icon-data-analysis"></i> 批量评估
      </el-button>
      <el-button
        type="warning"
        :disabled="selectedAssets.length === 0"
        @click="handleBatchMonitor"
      >
        <i class="el-icon-view"></i> 批量监控
      </el-button>
      <el-button type="info" @click="handleExport">
        <i class="el-icon-download"></i> 导出数据
      </el-button>
      <el-button type="danger" @click="handleGenerateReport">
        <i class="el-icon-document"></i> 质量报告
      </el-button>
    </el-card>

    <!-- 数据表格 -->
    <el-card shadow="never">
      <div slot="header">
        <span>资产质量评估列表</span>
        <span class="table-count">（共 {{ total }} 条）</span>
      </div>
      
      <el-table
        v-loading="loading"
        :data="assetsList"
        @selection-change="handleSelectionChange"
        stripe
        border
      >
        <el-table-column type="selection" width="55" />
        
        <el-table-column label="企业名称" prop="enterpriseName" min-width="180">
          <template slot-scope="scope">
            <el-link type="primary" @click="handleView(scope.row)">
              {{ scope.row.enterpriseName }}
            </el-link>
          </template>
        </el-table-column>
        
        <el-table-column label="资产名称" prop="assetName" min-width="200" />
        
        <el-table-column label="资产类型" prop="assetCategory" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getAssetTypeTag(scope.row.assetCategory)" size="mini">
              {{ getAssetTypeText(scope.row.assetCategory) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column label="资产价值" prop="assetValue" width="120" align="center">
          <template slot-scope="scope">
            {{ formatNumber(scope.row.assetValue) }}万元
          </template>
        </el-table-column>
        
        <el-table-column label="质量等级" prop="qualityLevel" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getQualityLevelTag(scope.row.qualityLevel)" size="mini">
              {{ getQualityLevelText(scope.row.qualityLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column label="质量评分" prop="qualityScore" width="120" align="center">
          <template slot-scope="scope">
            <el-progress
              :percentage="scope.row.qualityScore"
              :color="getQualityScoreColor(scope.row.qualityScore)"
              :show-text="false"
              :stroke-width="8"
            />
            <div style="margin-top: 5px; font-size: 12px;">
              {{ scope.row.qualityScore }}分
            </div>
          </template>
        </el-table-column>
        
        <el-table-column label="收益率" prop="returnRate" width="100" align="center">
          <template slot-scope="scope">
            <span :class="getReturnRateClass(scope.row.returnRate)">
              {{ scope.row.returnRate }}%
            </span>
          </template>
        </el-table-column>
        
        <el-table-column label="减值金额" prop="impairmentAmount" width="100" align="center">
          <template slot-scope="scope">
            {{ scope.row.impairmentAmount ? scope.row.impairmentAmount + '万' : '-' }}
          </template>
        </el-table-column>

        <el-table-column label="风险等级" prop="riskLevel" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getImpairmentRiskTag(scope.row.riskLevel)" size="mini">
              {{ getImpairmentRiskText(scope.row.riskLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column label="评估状态" prop="assessmentStatus" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getAssessmentStatusTag(scope.row.assessmentStatus)" size="mini">
              {{ getAssessmentStatusText(scope.row.assessmentStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column label="评估时间" prop="assessmentDate" width="150" />
        
        <el-table-column label="操作" width="300" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="warning" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="primary" @click="handleAssess(scope.row)">评估</el-button>
            <el-button size="mini" type="success" @click="handleMonitor(scope.row)">监控</el-button>
            <el-dropdown @command="handleCommand($event, scope.row)">
              <el-button size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="trend">趋势分析</el-dropdown-item>
                <el-dropdown-item command="predict">风险预警</el-dropdown-item>
                <el-dropdown-item command="compare">对比分析</el-dropdown-item>
                <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页 -->
      <div class="pagination-container">
        <Pagination
          v-show="total > 0"
          :total="total"
          :page.sync="searchForm.pageNum"
          :limit.sync="searchForm.pageSize"
          @pagination="getAssetsList"
        />
      </div>
    </el-card>

    <!-- 对话框组件 -->
    <AssetQualityDialog
      :visible.sync="qualityDialogVisible"
      :asset-data="currentAsset"
      :dialog-type="dialogType"
      @refresh="getAssetsList"
    />
    
    <AssetQualityAssessmentDialog
      :visible.sync="assessmentDialogVisible"
      :asset-data="currentAsset"
    />
    
    <AssetQualityMonitorDialog
      :visible.sync="monitorDialogVisible"
      :asset-data="currentAsset"
    />
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { mapGetters } from 'vuex'
import Pagination from '@/components/Pagination'
import AssetQualityDialog from './components/AssetQualityDialog'
import AssetQualityAssessmentDialog from './components/AssetQualityAssessmentDialog'
import AssetQualityMonitorDialog from './components/AssetQualityMonitorDialog'
import {
  getAssetQualityList,
  getAssetQualityStatistics,
  getAssetQualityCharts,
  deleteAssetQuality,
  exportAssetQualityData,
  batchAssessAssetQuality,
  batchMonitorAssetQuality,
  generateAssetQualityReport,
  getAssetQualityTrendAnalysis,
  getAssetQualityComparisonAnalysis,
  getAssetQualityAlerts
} from '@/api/stateAssets/assetQuality'

export default {
  computed: {
    ...mapGetters({ theme: 'settings/theme' }),
    themeColor() {
      const map = { red: '#e50113', green: '#41b584', ocean: '#1890ff', white: '#1890ff', default: '#1890ff' }
      return map[(this.theme && this.theme.themeName) || 'default'] || '#1890ff'
    },
    themeColorLight() {
      const map = { red: '#fff1f0', green: '#f6ffed', ocean: '#e6f7ff', white: '#e6f7ff', default: '#e6f7ff' }
      return map[(this.theme && this.theme.themeName) || 'default'] || '#e6f7ff'
    },
  },
  name: 'AssetQuality',
  components: {
    Pagination,
    AssetQualityDialog,
    AssetQualityAssessmentDialog,
    AssetQualityMonitorDialog
  },
  data() {
    return {
      loading: false,
      assetsList: [],
      selectedAssets: [],
      total: 0,
      statistics: {
        totalAssets: 0,
        highQualityAssets: 0,
        riskAssets: 0,
        avgQualityScore: 0
      },
      searchForm: {
        enterpriseName: '',
        assetName: '',
        assetType: '',
        qualityLevel: '',
        assessmentStatus: '',
        assessmentDateRange: [],
        pageNum: 1,
        pageSize: 10
      },
      qualityDialogVisible: false,
      assessmentDialogVisible: false,
      monitorDialogVisible: false,
      currentAsset: {},
      dialogType: 'add',
      // 图表实例
      qualityDistributionChart: null,
      qualityTrendChart: null,
      returnAnalysisChart: null,
      impairmentRiskChart: null
    }
  },
  created() {
    this.getStatistics()
    this.getAssetsList()
  },
  mounted() {
    this.initCharts()
    this.loadChartData()
  },
  beforeDestroy() {
    this.destroyCharts()
  },
  methods: {
    // 获取统计数据
    async getStatistics() {
      try {
        const response = await getAssetQualityStatistics()
        const data = response.data
        if (data) {
          this.statistics = {
            totalAssets: data.totalAssets || data.totalProperties || 0,
            highQualityAssets: data.highQualityAssets || data.highQuality || 0,
            riskAssets: data.riskAssets || data.riskCount || 0,
            avgQualityScore: data.averageScore || data.avgQualityScore || data.qualityScore || 0
          }
        }
      } catch (error) {
        console.error('获取统计数据失败:', error)
      }
    },

    // 获取资产列表
    async getAssetsList() {
      this.loading = true
      try {
        const params = { ...this.searchForm }
        // 处理日期范围
        if (params.assessmentDateRange && params.assessmentDateRange.length === 2) {
          params.assessmentStartDate = params.assessmentDateRange[0]
          params.assessmentEndDate = params.assessmentDateRange[1]
        }
        delete params.assessmentDateRange
        const response = await getAssetQualityList(params)
        const data = response.data
        if (data) {
          this.assetsList = data.tlist || data.list || []
          this.total = data.totalRecord || data.total || 0
        } else {
          this.assetsList = []
          this.total = 0
        }
      } catch (error) {
        console.error('获取资产列表失败:', error)
        this.assetsList = []
        this.total = 0
      } finally {
        this.loading = false
      }
    },

    // 初始化图表
    initCharts() {
      this.qualityDistributionChart = echarts.init(this.$refs.qualityDistributionChart)
      this.qualityTrendChart = echarts.init(this.$refs.qualityTrendChart)
      this.returnAnalysisChart = echarts.init(this.$refs.returnAnalysisChart)
      this.impairmentRiskChart = echarts.init(this.$refs.impairmentRiskChart)
      
      // 监听窗口大小变化
      window.addEventListener('resize', this.handleResize)
    },

    // 加载图表数据
    async loadChartData() {
      try {
        const response = await getAssetQualityCharts()
        const chartData = response.data || {}

        const qualityDistribution = chartData.qualityDistribution || []
        const qualityTrend = chartData.qualityTrend || []

        this.updateQualityDistributionChart(qualityDistribution)
        this.updateQualityTrendChart(qualityTrend)
        this.updateReturnAnalysisChart(chartData.returnAnalysis || [])
        this.updateImpairmentRiskChart(chartData.impairmentRisk || [])
      } catch (error) {
        console.error('加载图表数据失败:', error)
        this.updateQualityDistributionChart([])
        this.updateQualityTrendChart([])
        this.updateReturnAnalysisChart([])
        this.updateImpairmentRiskChart([])
      }
    },

    // 更新质量分布图表
    updateQualityDistributionChart(data) {
      const option = {
        title: {
          text: '资产质量分布',
          left: 'center',
          textStyle: { fontSize: 14 }
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        series: [{
          name: '质量等级',
          type: 'pie',
          radius: '60%',
          data: data,
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }]
      }
      this.qualityDistributionChart.setOption(option)
    },

    // 更新质量趋势图表
    updateQualityTrendChart(data) {
      const option = {
        title: {
          text: '质量趋势分析',
          left: 'center',
          textStyle: { fontSize: 14 }
        },
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          data: data.map(item => item.date)
        },
        yAxis: {
          type: 'value',
          name: '质量评分',
          min: 0,
          max: 100
        },
        series: [{
          name: '平均质量评分',
          type: 'line',
          data: data.map(item => item.score),
          smooth: true,
          itemStyle: { color: '#409EFF' },
          areaStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: 'rgba(64, 158, 255, 0.3)' },
              { offset: 1, color: 'rgba(64, 158, 255, 0.1)' }
            ])
          }
        }]
      }
      this.qualityTrendChart.setOption(option)
    },

    // 更新收益率分析图表
    updateReturnAnalysisChart(data) {
      const option = {
        title: {
          text: '收益率分析',
          left: 'center',
          textStyle: { fontSize: 14 }
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: { type: 'shadow' }
        },
        xAxis: {
          type: 'category',
          data: data.map(item => item.name)
        },
        yAxis: {
          type: 'value',
          name: '收益率(%)'
        },
        series: [{
          name: '收益率',
          type: 'bar',
          data: data.map(item => item.value),
          itemStyle: {
            color: (params) => {
              const colors = ['#67C23A', '#E6A23C', '#F56C6C']
              return colors[params.dataIndex % colors.length]
            }
          }
        }]
      }
      this.returnAnalysisChart.setOption(option)
    },

    // 更新减值风险图表
    updateImpairmentRiskChart(data) {
      const option = {
        title: {
          text: '减值风险预警',
          left: 'center',
          textStyle: { fontSize: 14 }
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        series: [{
          name: '风险等级',
          type: 'pie',
          radius: ['40%', '70%'],
          data: data,
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 0, 0, 0.5)'
            }
          }
        }]
      }
      this.impairmentRiskChart.setOption(option)
    },

    // 搜索
    handleSearch() {
      this.searchForm.pageNum = 1
      this.getAssetsList()
    },

    // 重置搜索表单
    resetSearchForm() {
      this.$refs.searchForm.resetFields()
      this.searchForm = {
        enterpriseName: '',
        assetName: '',
        assetType: '',
        qualityLevel: '',
        assessmentStatus: '',
        assessmentDateRange: [],
        pageNum: 1,
        pageSize: 10
      }
      this.getAssetsList()
    },

    // 表格选择变化
    handleSelectionChange(selection) {
      this.selectedAssets = selection
    },

    // 新增评估
    handleAdd() {
      this.currentAsset = {}
      this.dialogType = 'add'
      this.qualityDialogVisible = true
    },

    // 编辑资产
    handleEdit(row) {
      this.currentAsset = { ...row }
      this.dialogType = 'edit'
      this.qualityDialogVisible = true
    },

    // 查看资产
    handleView(row) {
      this.currentAsset = { ...row }
      this.dialogType = 'view'
      this.qualityDialogVisible = true
    },

    // 评估资产
    handleAssess(row) {
      this.currentAsset = { ...row }
      this.assessmentDialogVisible = true
    },

    // 监控资产
    handleMonitor(row) {
      this.currentAsset = { ...row }
      this.monitorDialogVisible = true
    },

    // 批量评估
    handleBatchAssess() {
      if (this.selectedAssets.length === 0) {
        this.$message.warning('请选择要评估的资产')
        return
      }
      this.$confirm(`确认对选中的 ${this.selectedAssets.length} 项资产进行批量评估？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(async () => {
        try {
          const ids = this.selectedAssets.map(item => item.assetQualityId)
          await batchAssessAssetQuality({ ids })
          this.$message.success('批量评估成功')
          this.getAssetsList()
          this.getStatistics()
        } catch (error) {
          this.$message.error('批量评估失败')
        }
      })
    },

    // 批量监控
    handleBatchMonitor() {
      if (this.selectedAssets.length === 0) {
        this.$message.warning('请选择要监控的资产')
        return
      }
      this.$confirm(`确认对选中的 ${this.selectedAssets.length} 项资产设置监控？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(async () => {
        try {
          const ids = this.selectedAssets.map(item => item.assetQualityId)
          await batchMonitorAssetQuality({ ids })
          this.$message.success('批量监控设置成功')
          this.getAssetsList()
        } catch (error) {
          this.$message.error('批量监控设置失败')
        }
      })
    },

    // 导出数据
    async handleExport() {
      try {
        this.$message.info('正在导出数据...')
        const response = await exportAssetQualityData(this.searchForm)
        const blob = new Blob([response], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const link = document.createElement('a')
        link.href = URL.createObjectURL(blob)
        link.download = '资产质量评估数据.xlsx'
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        URL.revokeObjectURL(link.href)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败：' + (error.message || '网络错误'))
      }
    },

    // 生成报告
    async handleGenerateReport() {
      try {
        this.$message.info('正在生成质量报告...')
        const response = await generateAssetQualityReport(this.searchForm)
        if (response.code === 1) {
          this.$message.success('质量报告生成成功')
          // 可以在这里展示报告数据或下载
          console.log('报告数据:', response.data)
        } else {
          this.$message.warning(response.msg || '报告生成失败')
        }
      } catch (error) {
        this.$message.error('报告生成失败：' + (error.message || '网络错误'))
      }
    },

    // 下拉菜单命令处理
    handleCommand(command, row) {
      switch (command) {
        case 'trend':
          this.handleTrendAnalysis(row)
          break
        case 'predict':
          this.handleRiskWarning(row)
          break
        case 'compare':
          this.handleCompareAnalysis(row)
          break
        case 'delete':
          this.handleDelete(row)
          break
      }
    },

    // 趋势分析
    async handleTrendAnalysis(row) {
      try {
        const response = await getAssetQualityTrendAnalysis({ assetQualityId: row.assetQualityId })
        const data = response.data || []
        if (data.length === 0) {
          this.$message.info('暂无趋势数据')
          return
        }
        this.$alert(
          `<div style="padding:10px;">
            <p><strong>${row.enterpriseName}</strong> 资产质量趋势：</p>
            <table border="1" cellpadding="8" cellspacing="0" style="width:100%;border-collapse:collapse;margin-top:10px;">
              <tr style="background:#f5f7fa;"><th>年份</th><th>质量评分</th></tr>
              ${data.map(item => `<tr><td>${item.year}</td><td>${item.score}分</td></tr>`).join('')}
            </table>
          </div>`,
          '趋势分析',
          { dangerouslyUseHTMLString: true, customClass: 'trend-dialog' }
        )
      } catch (error) {
        this.$message.error('趋势分析失败：' + (error.message || '网络错误'))
      }
    },

    // 风险预警
    async handleRiskWarning(row) {
      try {
        const response = await getAssetQualityAlerts({ assetQualityId: row.assetQualityId })
        const data = response.data || []
        if (data.length === 0) {
          this.$message.success(row.enterpriseName + ' 暂无风险预警')
          return
        }
        this.$alert(
          `<div style="padding:10px;">
            <p><strong>${row.enterpriseName}</strong> 风险预警信息：</p>
            <table border="1" cellpadding="8" cellspacing="0" style="width:100%;border-collapse:collapse;margin-top:10px;">
              <tr style="background:#f5f7fa;"><th>预警级别</th><th>预警内容</th><th>时间</th></tr>
              ${data.slice(0, 5).map(item => `<tr><td><span style="color:${item.alertLevel === '高' ? '#F56C6C' : '#E6A23C'}">${item.alertLevel}</span></td><td>${item.alertContent}</td><td>${item.alertTime || '-'}</td></tr>`).join('')}
            </table>
          </div>`,
          '风险预警',
          { dangerouslyUseHTMLString: true, customClass: 'alert-dialog' }
        )
      } catch (error) {
        this.$message.error('风险预警查询失败：' + (error.message || '网络错误'))
      }
    },

    // 对比分析
    async handleCompareAnalysis(row) {
      try {
        const response = await getAssetQualityComparisonAnalysis({ assetQualityId: row.assetQualityId })
        const data = response.data || {}
        this.$alert(
          `<div style="padding:10px;">
            <p><strong>${row.enterpriseName}</strong> 对比分析结果：</p>
            <table border="1" cellpadding="8" cellspacing="0" style="width:100%;border-collapse:collapse;margin-top:10px;">
              <tr><td>当前评分</td><td><strong>${data.currentScore || row.qualityScore}分</strong></td></tr>
              <tr><td>行业平均</td><td>${data.industryAvgScore || '-'}分</td></tr>
              <tr><td>排名</td><td>第${data.rank || '-'}名 / 共${data.totalCount || '-'}家</td></tr>
              <tr><td>百分位</td><td>超过${data.percentile || '-'}%的企业</td></tr>
              <tr><td>偏差</td><td><span style="color:${(data.deviation || 0) >= 0 ? '#67C23A' : '#F56C6C'}">${(data.deviation || 0) >= 0 ? '+' : ''}${data.deviation || 0}分</span></td></tr>
            </table>
            ${data.top5 ? '<p style="margin-top:10px;"><strong>Top5企业：</strong></p><ol>' + data.top5.map(t => `<li>${t.name}: ${t.score}分</li>`).join('') + '</ol>' : ''}
          </div>`,
          '对比分析',
          { dangerouslyUseHTMLString: true, customClass: 'compare-dialog' }
        )
      } catch (error) {
        this.$message.error('对比分析失败：' + (error.message || '网络错误'))
      }
    },

    // 删除资产
    handleDelete(row) {
      this.$confirm('确认删除该资产质量评估？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await deleteAssetQuality({ assetQualityId: row.assetQualityId })
          this.$message.success('删除成功')
          this.getAssetsList()
          this.getStatistics()
        } catch (error) {
          this.$message.error('删除失败')
        }
      })
    },

    // 刷新图表
    refreshQualityChart() {
      this.loadChartData()
    },

    refreshTrendChart() {
      this.loadChartData()
    },

    refreshReturnChart() {
      this.loadChartData()
    },

    refreshRiskChart() {
      this.loadChartData()
    },

    // 窗口大小变化
    handleResize() {
      if (this.qualityDistributionChart) this.qualityDistributionChart.resize()
      if (this.qualityTrendChart) this.qualityTrendChart.resize()
      if (this.returnAnalysisChart) this.returnAnalysisChart.resize()
      if (this.impairmentRiskChart) this.impairmentRiskChart.resize()
    },

    // 销毁图表
    destroyCharts() {
      if (this.qualityDistributionChart) {
        this.qualityDistributionChart.dispose()
        this.qualityDistributionChart = null
      }
      if (this.qualityTrendChart) {
        this.qualityTrendChart.dispose()
        this.qualityTrendChart = null
      }
      if (this.returnAnalysisChart) {
        this.returnAnalysisChart.dispose()
        this.returnAnalysisChart = null
      }
      if (this.impairmentRiskChart) {
        this.impairmentRiskChart.dispose()
        this.impairmentRiskChart = null
      }
      window.removeEventListener('resize', this.handleResize)
    },

    // 格式化数字
    formatNumber(num) {
      if (num >= 10000) {
        return (num / 10000).toFixed(1) + '万'
      }
      return num.toLocaleString()
    },

    // 获取资产类型标签
    getAssetTypeTag(type) {
      const tagMap = {
        'FIXED_ASSETS': 'primary',
        'CURRENT_ASSETS': 'success',
        'INTANGIBLE_ASSETS': 'warning',
        'INVESTMENT_ASSETS': 'info',
        'FINANCIAL_ASSETS': 'danger'
      }
      return tagMap[type] || 'info'
    },

    // 获取资产类型文本
    getAssetTypeText(type) {
      const textMap = {
        'FIXED_ASSETS': '固定资产',
        'CURRENT_ASSETS': '流动资产',
        'INTANGIBLE_ASSETS': '无形资产',
        'INVESTMENT_ASSETS': '投资性资产',
        'FINANCIAL_ASSETS': '金融资产'
      }
      return textMap[type] || type
    },

    // 获取质量等级标签
    getQualityLevelTag(level) {
      const tagMap = {
        'EXCELLENT': 'success',
        'GOOD': 'primary',
        'AVERAGE': 'info',
        'POOR': 'warning',
        'RISK': 'danger'
      }
      return tagMap[level] || 'info'
    },

    // 获取质量等级文本
    getQualityLevelText(level) {
      const textMap = {
        'EXCELLENT': '优质',
        'GOOD': '良好',
        'AVERAGE': '一般',
        'POOR': '较差',
        'RISK': '风险'
      }
      return textMap[level] || level
    },

    // 获取质量评分颜色
    getQualityScoreColor(score) {
      if (score >= 90) return '#67C23A'
      if (score >= 80) return '#409EFF'
      if (score >= 70) return '#E6A23C'
      return '#F56C6C'
    },

    // 获取收益率样式类
    getReturnRateClass(rate) {
      if (rate > 0) return 'positive-return'
      if (rate < 0) return 'negative-return'
      return 'zero-return'
    },

    // 获取减值风险标签
    getImpairmentRiskTag(risk) {
      const tagMap = {
        'LOW': 'success',
        'MEDIUM': 'warning',
        'HIGH': 'danger',
        'CRITICAL': 'danger'
      }
      return tagMap[risk] || 'info'
    },

    // 获取减值风险文本
    getImpairmentRiskText(risk) {
      const textMap = {
        'LOW': '低风险',
        'MEDIUM': '中风险',
        'HIGH': '高风险',
        'CRITICAL': '极高风险'
      }
      return textMap[risk] || risk || '-'
    },

    // 获取评估状态标签
    getAssessmentStatusTag(status) {
      const tagMap = {
        'PENDING': 'info',
        'ASSESSING': 'warning',
        'COMPLETED': 'success',
        'FAILED': 'danger'
      }
      return tagMap[status] || 'info'
    },

    // 获取评估状态文本
    getAssessmentStatusText(status) {
      const textMap = {
        'PENDING': '待评估',
        'ASSESSING': '评估中',
        'COMPLETED': '已完成',
        'FAILED': '评估失败'
      }
      return textMap[status] || status
    }
  }
}
</script>

<style lang="scss" scoped>
.asset-page { padding: 16px; background: #f0f2f5; min-height: calc(100vh - 84px); }
.page-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 14px; padding: 14px 20px; border-radius: 6px; color: #fff; }
.page-header-left { display: flex; align-items: center; font-size: 16px; font-weight: 600; i { font-size: 22px; margin-right: 10px; } }
.page-header-desc { font-size: 13px; opacity: 0.85; }
::v-deep .el-table th { background: #e6f7ff !important; }
::v-deep .el-card { border-radius: 6px; }
.mb-20 { margin-bottom: 20px; }
.stats-card {
  .stats-content { display: flex; align-items: center;
    .stats-icon { font-size: 40px; margin-right: 20px; }
    .stats-info {
      .stats-value { font-size: 28px; font-weight: bold; color: #303133; line-height: 1; }
      .stats-label { font-size: 14px; color: #909399; margin-top: 5px; }
    }
  }
}
.chart-container { height: 300px; width: 100%; }
.table-count { color: #909399; font-size: 14px; }
.pagination-container { margin-top: 20px; text-align: right; }
.positive-return { color: #67C23A; font-weight: bold; }
.negative-return { color: #F56C6C; font-weight: bold; }
.zero-return { color: #909399; }
</style>
