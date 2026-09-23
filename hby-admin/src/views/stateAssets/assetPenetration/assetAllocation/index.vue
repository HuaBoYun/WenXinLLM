<template>
  <div class="asset-allocation-container asset-page">
    <div class="page-header" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="page-header-left"><i class="el-icon-coin"></i><span>资产配置</span></div>
      <div class="page-header-desc">分析企业资产配置结构、行业分布与优化建议</div>
    </div>
    <!-- 统计概览 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="6">
        <el-card shadow="hover" class="stats-card">
          <div class="stats-content">
            <div class="stats-icon">
              <i class="el-icon-coin" style="color: #1890ff;"></i>
            </div>
            <div class="stats-info">
              <div class="stats-value">{{ statistics.totalAssets }}</div>
              <div class="stats-label">资产总额(万元)</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stats-card">
          <div class="stats-content">
            <div class="stats-icon">
              <i class="el-icon-pie-chart" style="color: #67C23A;"></i>
            </div>
            <div class="stats-info">
              <div class="stats-value">{{ statistics.assetCategories }}</div>
              <div class="stats-label">资产类别</div>
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
              <i class="el-icon-data-analysis" style="color: #F56C6C;"></i>
            </div>
            <div class="stats-info">
              <div class="stats-value">{{ statistics.allocationScore }}%</div>
              <div class="stats-label">配置效率</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 资产配置分析图表 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="12">
        <el-card shadow="never">
          <div slot="header">
            <span>资产类型分布</span>
            <el-button
              style="float: right; padding: 3px 0"
              type="text"
              @click="refreshTypeChart"
            >
              刷新
            </el-button>
          </div>
          <div ref="assetTypeChart" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="never">
          <div slot="header">
            <span>地区分布</span>
            <el-button
              style="float: right; padding: 3px 0"
              type="text"
              @click="refreshRegionChart"
            >
              刷新
            </el-button>
          </div>
          <div ref="assetRegionChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="mb-20">
      <el-col :span="12">
        <el-card shadow="never">
          <div slot="header">
            <span>行业分布</span>
            <el-button
              style="float: right; padding: 3px 0"
              type="text"
              @click="refreshIndustryChart"
            >
              刷新
            </el-button>
          </div>
          <div ref="assetIndustryChart" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="never">
          <div slot="header">
            <span>配置效率趋势</span>
            <el-button
              style="float: right; padding: 3px 0"
              type="text"
              @click="refreshEfficiencyChart"
            >
              刷新
            </el-button>
          </div>
          <div ref="allocationEfficiencyChart" class="chart-container"></div>
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
        
        <el-form-item label="所属地区">
          <el-select
            v-model="searchForm.region"
            placeholder="请选择地区"
            clearable
            style="width: 150px;"
          >
            <el-option label="华北地区" value="NORTH_CHINA"></el-option>
            <el-option label="华东地区" value="EAST_CHINA"></el-option>
            <el-option label="华南地区" value="SOUTH_CHINA"></el-option>
            <el-option label="华中地区" value="CENTRAL_CHINA"></el-option>
            <el-option label="西北地区" value="NORTHWEST_CHINA"></el-option>
            <el-option label="西南地区" value="SOUTHWEST_CHINA"></el-option>
            <el-option label="东北地区" value="NORTHEAST_CHINA"></el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="所属行业">
          <el-select
            v-model="searchForm.industry"
            placeholder="请选择行业"
            clearable
            style="width: 150px;"
          >
            <el-option label="制造业" value="MANUFACTURING"></el-option>
            <el-option label="金融业" value="FINANCE"></el-option>
            <el-option label="房地产业" value="REAL_ESTATE"></el-option>
            <el-option label="建筑业" value="CONSTRUCTION"></el-option>
            <el-option label="交通运输业" value="TRANSPORTATION"></el-option>
            <el-option label="信息技术业" value="IT"></el-option>
            <el-option label="能源业" value="ENERGY"></el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="风险等级">
          <el-select
            v-model="searchForm.riskLevel"
            placeholder="请选择风险等级"
            clearable
            style="width: 120px;"
          >
            <el-option label="低风险" value="LOW"></el-option>
            <el-option label="中风险" value="MEDIUM"></el-option>
            <el-option label="高风险" value="HIGH"></el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="资产规模">
          <el-input
            v-model="searchForm.minAssetValue"
            placeholder="最小值"
            style="width: 100px;"
          />
          <span style="margin: 0 10px;">-</span>
          <el-input
            v-model="searchForm.maxAssetValue"
            placeholder="最大值"
            style="width: 100px;"
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
        <i class="el-icon-plus"></i> 新增配置
      </el-button>
      <el-button
        type="success"
        :disabled="selectedAllocations.length === 0"
        @click="handleBatchOptimize"
      >
        <i class="el-icon-data-analysis"></i> 批量优化
      </el-button>
      <el-button
        type="warning"
        :disabled="selectedAllocations.length === 0"
        @click="handleBatchAssess"
      >
        <i class="el-icon-warning"></i> 批量评估
      </el-button>
      <el-button type="info" @click="handleExport">
        <i class="el-icon-download"></i> 导出数据
      </el-button>
      <el-button type="danger" @click="handleGenerateReport">
        <i class="el-icon-document"></i> 生成报告
      </el-button>
    </el-card>

    <!-- 数据表格 -->
    <el-card shadow="never">
      <div slot="header">
        <span>资产配置列表</span>
        <span class="table-count">（共 {{ total }} 条）</span>
      </div>
      
      <el-table
        v-loading="loading"
        :data="allocationsList"
        @selection-change="handleSelectionChange"
        stripe
        border
      >
        <el-table-column type="selection" width="55" />
        
        <el-table-column label="企业名称" prop="enterpriseName" min-width="200">
          <template slot-scope="scope">
            <el-link type="primary" @click="handleView(scope.row)">
              {{ scope.row.enterpriseName }}
            </el-link>
          </template>
        </el-table-column>
        
        <el-table-column label="资产类型" prop="assetType" width="120" align="center">
          <template slot-scope="scope">
            <el-tag :type="getAssetTypeTag(scope.row.assetType)" size="mini">
              {{ getAssetTypeText(scope.row.assetType) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column label="资产规模" prop="assetValue" width="120" align="center">
          <template slot-scope="scope">
            {{ formatNumber(scope.row.assetValue) }}万元
          </template>
        </el-table-column>
        
        <el-table-column label="配置比例" prop="allocationRatio" width="100" align="center">
          <template slot-scope="scope">
            {{ scope.row.allocationRatio }}%
          </template>
        </el-table-column>
        
        <el-table-column label="所属地区" prop="region" width="100" align="center">
          <template slot-scope="scope">
            {{ getRegionText(scope.row.region) }}
          </template>
        </el-table-column>
        
        <el-table-column label="所属行业" prop="industry" width="100" align="center">
          <template slot-scope="scope">
            {{ getIndustryText(scope.row.industry) }}
          </template>
        </el-table-column>
        
        <el-table-column label="风险等级" prop="riskLevel" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getRiskLevelTag(scope.row.riskLevel)" size="mini">
              {{ getRiskLevelText(scope.row.riskLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column label="配置效率" prop="allocationEfficiency" width="100" align="center">
          <template slot-scope="scope">
            <el-progress
              :percentage="scope.row.allocationEfficiency"
              :color="getEfficiencyColor(scope.row.allocationEfficiency)"
              :show-text="false"
              :stroke-width="8"
            />
            <div style="margin-top: 5px; font-size: 12px;">
              {{ scope.row.allocationEfficiency }}%
            </div>
          </template>
        </el-table-column>
        
        <el-table-column label="更新时间" prop="updateTime" width="150" />
        
        <el-table-column label="操作" width="250" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="success" @click="handleOptimize(scope.row)">优化</el-button>
            <el-dropdown @command="handleCommand($event, scope.row)">
              <el-button size="mini">
                更多<i class="el-icon-arrow-down el-icon--right"></i>
              </el-button>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item command="assess">评估分析</el-dropdown-item>
                <el-dropdown-item command="trend">趋势分析</el-dropdown-item>
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
          :page.sync="searchForm.pageNumber"
          :limit.sync="searchForm.pageSize"
          @pagination="getAllocationsList"
        />
      </div>
    </el-card>

    <!-- 对话框组件 -->
    <AssetAllocationDialog
      :visible.sync="allocationDialogVisible"
      :allocation-data="currentAllocation"
      :dialog-type="dialogType"
      @refresh="getAllocationsList"
    />

    <AssetOptimizationDialog
      :visible.sync="optimizationDialogVisible"
      :allocation-data="currentAllocation"
    />

    <AssetAssessmentDialog
      :visible.sync="assessmentDialogVisible"
      :allocation-data="currentAllocation"
    />

    <TrendAnalysisDialog
      :visible.sync="trendDialogVisible"
      :allocation-data="currentAllocation"
    />

    <ComparisonAnalysisDialog
      :visible.sync="comparisonDialogVisible"
      :allocation-data="currentAllocation"
    />
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { mapGetters } from 'vuex'
import Pagination from '@/components/Pagination'
import AssetAllocationDialog from './components/AssetAllocationDialog'
import AssetOptimizationDialog from './components/AssetOptimizationDialog'
import AssetAssessmentDialog from './components/AssetAssessmentDialog'
import TrendAnalysisDialog from './components/TrendAnalysisDialog'
import ComparisonAnalysisDialog from './components/ComparisonAnalysisDialog'
import {
  getAssetAllocationsList,
  getAssetAllocationStatistics,
  getAssetAllocationCharts,
  deleteAssetAllocation,
  exportAssetAllocationData,
  batchOptimizeAssetAllocations,
  batchAssessAssetAllocations,
  generateAssetAllocationReport
} from '@/api/stateAssets/assetAllocation'

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
  name: 'AssetAllocation',
  components: {
    Pagination,
    AssetAllocationDialog,
    AssetOptimizationDialog,
    AssetAssessmentDialog,
    TrendAnalysisDialog,
    ComparisonAnalysisDialog
  },
  data() {
    return {
      loading: false,
      allocationsList: [],
      selectedAllocations: [],
      total: 0,
      statistics: {
        totalAssets: 0,
        assetCategories: 0,
        riskAssets: 0,
        allocationScore: 0
      },
      searchForm: {
        enterpriseName: '',
        assetType: '',
        region: '',
        industry: '',
        riskLevel: '',
        minAssetValue: '',
        maxAssetValue: '',
        pageNumber: 1,
        pageSize: 10
      },
      allocationDialogVisible: false,
      optimizationDialogVisible: false,
      assessmentDialogVisible: false,
      trendDialogVisible: false,
      comparisonDialogVisible: false,
      currentAllocation: {},
      dialogType: 'add',
      // 图表实例
      assetTypeChart: null,
      assetRegionChart: null,
      assetIndustryChart: null,
      allocationEfficiencyChart: null
    }
  },
  created() {
    this.getStatistics()
    this.getAllocationsList()
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
        const response = await getAssetAllocationStatistics()
        if (response && response.result === 200 && response.data) {
          const data = response.data
          this.statistics = {
            totalAssets: data.totalAssets || 0,
            assetCategories: data.assetCategories || 0,
            riskAssets: data.riskAssets || 0,
            allocationScore: data.allocationScore || 0
          }
        }
      } catch (error) {
        console.error('获取统计数据失败:', error)
      }
    },

    // 获取配置列表
    async getAllocationsList() {
      this.loading = true
      try {
        const response = await getAssetAllocationsList(this.searchForm)
        if (response && response.result === 200 && response.data) {
          this.allocationsList = response.data.tlist || response.data.list || []
          this.total = response.data.totalRecord || response.data.total || 0
        } else {
          this.allocationsList = []
          this.total = 0
        }
      } catch (error) {
        console.error('获取配置列表异常:', error)
        this.allocationsList = []
        this.total = 0
      } finally {
        this.loading = false
      }
    },

    // 初始化图表
    initCharts() {
      this.assetTypeChart = echarts.init(this.$refs.assetTypeChart)
      this.assetRegionChart = echarts.init(this.$refs.assetRegionChart)
      this.assetIndustryChart = echarts.init(this.$refs.assetIndustryChart)
      this.allocationEfficiencyChart = echarts.init(this.$refs.allocationEfficiencyChart)
      
      // 监听窗口大小变化
      window.addEventListener('resize', this.handleResize)
    },

    // 加载图表数据
    async loadChartData() {
      try {
        const response = await getAssetAllocationCharts()
        if (response && response.result === 200 && response.data) {
          const chartData = response.data
          this.updateAssetTypeChart(chartData.assetTypeData || [])
          this.updateAssetRegionChart(chartData.assetRegionData || [])
          this.updateAssetIndustryChart(chartData.assetIndustryData || [])
          this.updateAllocationEfficiencyChart(chartData.efficiencyTrendData || [])
        } else {
          this.updateAssetTypeChart([])
          this.updateAssetRegionChart([])
          this.updateAssetIndustryChart([])
          this.updateAllocationEfficiencyChart([])
        }
      } catch (error) {
        console.error('加载图表数据失败:', error)
        this.updateAssetTypeChart([])
        this.updateAssetRegionChart([])
        this.updateAssetIndustryChart([])
        this.updateAllocationEfficiencyChart([])
      }
    },

    // 更新资产类型图表
    updateAssetTypeChart(data) {
      const option = {
        title: {
          text: '资产类型分布',
          left: 'center',
          textStyle: { fontSize: 14 }
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c}万元 ({d}%)'
        },
        series: [{
          name: '资产类型',
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
      this.assetTypeChart.setOption(option)
    },

    // 更新地区分布图表
    updateAssetRegionChart(data) {
      const option = {
        title: {
          text: '地区分布',
          left: 'center',
          textStyle: { fontSize: 14 }
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c}万元 ({d}%)'
        },
        series: [{
          name: '地区分布',
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
      this.assetRegionChart.setOption(option)
    },

    // 更新行业分布图表
    updateAssetIndustryChart(data) {
      const option = {
        title: {
          text: '行业分布',
          left: 'center',
          textStyle: { fontSize: 14 }
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: { type: 'shadow' }
        },
        xAxis: {
          type: 'category',
          data: data.map(item => item.name),
          axisLabel: { rotate: 45 }
        },
        yAxis: {
          type: 'value',
          name: '资产规模(万元)'
        },
        series: [{
          name: '资产规模',
          type: 'bar',
          data: data.map(item => item.value),
          itemStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: '#83bff6' },
              { offset: 0.5, color: '#188df0' },
              { offset: 1, color: '#188df0' }
            ])
          }
        }]
      }
      this.assetIndustryChart.setOption(option)
    },

    // 更新配置效率趋势图表
    updateAllocationEfficiencyChart(data) {
      const option = {
        title: {
          text: '配置效率趋势',
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
          name: '效率(%)',
          min: 0,
          max: 100
        },
        series: [{
          name: '配置效率',
          type: 'line',
          data: data.map(item => item.efficiency),
          smooth: true,
          itemStyle: { color: '#67C23A' },
          areaStyle: {
            color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
              { offset: 0, color: 'rgba(103, 194, 58, 0.3)' },
              { offset: 1, color: 'rgba(103, 194, 58, 0.1)' }
            ])
          }
        }]
      }
      this.allocationEfficiencyChart.setOption(option)
    },

    // 搜索
    handleSearch() {
      this.searchForm.pageNumber = 1
      this.getAllocationsList()
    },

    // 重置搜索表单
    resetSearchForm() {
      this.$refs.searchForm.resetFields()
      this.searchForm = {
        enterpriseName: '',
        assetType: '',
        region: '',
        industry: '',
        riskLevel: '',
        minAssetValue: '',
        maxAssetValue: '',
        pageNumber: 1,
        pageSize: 10
      }
      this.getAllocationsList()
    },

    // 表格选择变化
    handleSelectionChange(selection) {
      this.selectedAllocations = selection
    },

    // 新增配置
    handleAdd() {
      this.currentAllocation = {}
      this.dialogType = 'add'
      this.allocationDialogVisible = true
    },

    // 查看配置
    handleView(row) {
      this.currentAllocation = { ...row }
      this.dialogType = 'view'
      this.allocationDialogVisible = true
    },

    // 编辑配置
    handleEdit(row) {
      this.currentAllocation = { ...row }
      this.dialogType = 'edit'
      this.allocationDialogVisible = true
    },

    // 优化配置
    handleOptimize(row) {
      this.currentAllocation = { ...row }
      this.optimizationDialogVisible = true
    },

    // 批量优化
    handleBatchOptimize() {
      if (this.selectedAllocations.length === 0) {
        this.$message.warning('请选择要优化的配置')
        return
      }
      this.$confirm(`确认对选中的 ${this.selectedAllocations.length} 条配置进行批量优化？`, '批量优化', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(async () => {
        try {
          const ids = this.selectedAllocations.map(item => item.allocationId)
          const response = await batchOptimizeAssetAllocations({ allocationIds: ids })
          if (response && response.result === 200) {
            this.$message.success(`成功优化 ${response.data.optimizedCount} 条配置`)
            this.getAllocationsList()
            this.getStatistics()
          } else {
            this.$message.error(response.msg || '批量优化失败')
          }
        } catch (error) {
          this.$message.error('批量优化失败')
        }
      })
    },

    // 批量评估
    handleBatchAssess() {
      if (this.selectedAllocations.length === 0) {
        this.$message.warning('请选择要评估的配置')
        return
      }
      this.$confirm(`确认对选中的 ${this.selectedAllocations.length} 条配置进行批量评估？`, '批量评估', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(async () => {
        try {
          const ids = this.selectedAllocations.map(item => item.allocationId)
          const response = await batchAssessAssetAllocations({ allocationIds: ids })
          if (response && response.result === 200) {
            const data = response.data
            this.$message.success(`评估完成，平均得分：${data.averageScore}`)
          } else {
            this.$message.error(response.msg || '批量评估失败')
          }
        } catch (error) {
          this.$message.error('批量评估失败')
        }
      })
    },

    // 导出数据
    async handleExport() {
      try {
        const response = await exportAssetAllocationData(this.searchForm)
        const blobData = response.data || response
        const blob = new Blob([blobData], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
        const link = document.createElement('a')
        link.href = URL.createObjectURL(blob)
        link.download = '资产配置数据.xlsx'
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        URL.revokeObjectURL(link.href)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败')
      }
    },

    // 生成报告
    async handleGenerateReport() {
      try {
        const response = await generateAssetAllocationReport(this.searchForm)
        if (response && response.result === 200 && response.data) {
          const report = response.data
          this.$alert(
            `<div style="line-height: 2;">
              <p><strong>报告标题：</strong>${report.reportTitle}</p>
              <p><strong>生成时间：</strong>${report.generateTime}</p>
              <p><strong>总记录数：</strong>${report.totalRecords}</p>
              <p><strong>资产总额：</strong>${report.totalAmount} 万元</p>
              <p><strong>平均收益率：</strong>${report.averageYieldRate}%</p>
              <p><strong>配置建议：</strong></p>
              <ul>${(report.suggestions || []).map(s => '<li>' + s + '</li>').join('')}</ul>
            </div>`,
            '资产配置分析报告',
            { dangerouslyUseHTMLString: true, confirmButtonText: '关闭' }
          )
        } else {
          this.$message.error(response.msg || '生成报告失败')
        }
      } catch (error) {
        this.$message.error('生成报告失败')
      }
    },

    // 下拉菜单命令处理
    handleCommand(command, row) {
      switch (command) {
        case 'assess':
          this.currentAllocation = { ...row }
          this.assessmentDialogVisible = true
          break
        case 'trend':
          this.currentAllocation = { ...row }
          this.trendDialogVisible = true
          break
        case 'compare':
          this.currentAllocation = { ...row }
          this.comparisonDialogVisible = true
          break
        case 'delete':
          this.handleDelete(row)
          break
      }
    },

    // 删除配置
    handleDelete(row) {
      this.$confirm('确认删除该资产配置？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await deleteAssetAllocation({ allocationId: row.allocationId })
          this.$message.success('删除成功')
          this.getAllocationsList()
          this.getStatistics()
        } catch (error) {
          this.$message.error('删除失败')
        }
      })
    },

    // 刷新图表
    refreshTypeChart() {
      this.loadChartData()
    },

    refreshRegionChart() {
      this.loadChartData()
    },

    refreshIndustryChart() {
      this.loadChartData()
    },

    refreshEfficiencyChart() {
      this.loadChartData()
    },

    // 窗口大小变化
    handleResize() {
      if (this.assetTypeChart) this.assetTypeChart.resize()
      if (this.assetRegionChart) this.assetRegionChart.resize()
      if (this.assetIndustryChart) this.assetIndustryChart.resize()
      if (this.allocationEfficiencyChart) this.allocationEfficiencyChart.resize()
    },

    // 销毁图表
    destroyCharts() {
      if (this.assetTypeChart) {
        this.assetTypeChart.dispose()
        this.assetTypeChart = null
      }
      if (this.assetRegionChart) {
        this.assetRegionChart.dispose()
        this.assetRegionChart = null
      }
      if (this.assetIndustryChart) {
        this.assetIndustryChart.dispose()
        this.assetIndustryChart = null
      }
      if (this.allocationEfficiencyChart) {
        this.allocationEfficiencyChart.dispose()
        this.allocationEfficiencyChart = null
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
      return textMap[type] || type || '-'
    },

    // 获取地区文本
    getRegionText(region) {
      const textMap = {
        'NORTH_CHINA': '华北',
        'EAST_CHINA': '华东',
        'SOUTH_CHINA': '华南',
        'CENTRAL_CHINA': '华中',
        'NORTHWEST_CHINA': '西北',
        'SOUTHWEST_CHINA': '西南',
        'NORTHEAST_CHINA': '东北'
      }
      return textMap[region] || region || '-'
    },

    // 获取行业文本
    getIndustryText(industry) {
      const textMap = {
        'MANUFACTURING': '制造业',
        'FINANCE': '金融业',
        'REAL_ESTATE': '房地产',
        'CONSTRUCTION': '建筑业',
        'TRANSPORTATION': '交通运输',
        'IT': '信息技术',
        'ENERGY': '能源业'
      }
      return textMap[industry] || industry || '-'
    },

    // 获取风险等级标签
    getRiskLevelTag(level) {
      const tagMap = {
        'LOW': 'success',
        'MEDIUM': 'warning',
        'HIGH': 'danger'
      }
      return tagMap[level] || 'info'
    },

    // 获取风险等级文本
    getRiskLevelText(level) {
      const textMap = {
        'LOW': '低风险',
        'MEDIUM': '中风险',
        'HIGH': '高风险'
      }
      return textMap[level] || level || '-'
    },

    // 获取效率颜色
    getEfficiencyColor(efficiency) {
      if (efficiency >= 80) return '#67C23A'
      if (efficiency >= 60) return '#E6A23C'
      return '#F56C6C'
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
</style>
