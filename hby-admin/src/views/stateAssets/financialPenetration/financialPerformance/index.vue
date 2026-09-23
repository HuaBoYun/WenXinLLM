<template>
  <div class="financial-performance-container financial-ext-page">
    <div class="page-header" :style="{ background: 'linear-gradient(135deg, ' + themeColor + 'cc 0%, ' + themeColor + ' 100%)' }">
      <div class="page-header-left"><i class="el-icon-trophy"></i><span>财务绩效</span></div>
      <div class="page-header-desc">评估企业财务绩效指标、KPI达成率与同比分析</div>
    </div>
    <!-- 统计概览 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="6">
        <el-card shadow="hover" class="stats-card">
          <div class="stats-content">
            <div class="stats-icon">
              <i class="el-icon-trophy" style="color: #eb2f96;"></i>
            </div>
            <div class="stats-info">
              <div class="stats-value">{{ statistics.totalEvaluations }}</div>
              <div class="stats-label">绩效评价总数</div>
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
              <div class="stats-value">{{ statistics.excellentPerformance }}</div>
              <div class="stats-label">优秀绩效企业</div>
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
              <div class="stats-value">{{ statistics.poorPerformance }}</div>
              <div class="stats-label">待改进企业</div>
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
              <div class="stats-value">{{ statistics.avgPerformanceScore }}分</div>
              <div class="stats-label">平均绩效评分</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 绩效分析图表 -->
    <el-row :gutter="20" class="mb-20">
      <el-col :span="12">
        <el-card shadow="never">
          <div slot="header">
            <span>绩效等级分布</span>
            <el-button
              style="float: right; padding: 3px 0"
              type="text"
              @click="refreshPerformanceChart"
            >
              刷新
            </el-button>
          </div>
          <div ref="performanceDistributionChart" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="never">
          <div slot="header">
            <span>绩效趋势分析</span>
            <el-button
              style="float: right; padding: 3px 0"
              type="text"
              @click="refreshTrendChart"
            >
              刷新
            </el-button>
          </div>
          <div ref="performanceTrendChart" class="chart-container"></div>
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
        
        <el-form-item label="绩效等级">
          <el-select
            v-model="searchForm.performanceLevel"
            placeholder="请选择绩效等级"
            clearable
            style="width: 120px;"
          >
            <el-option label="优秀" value="EXCELLENT"></el-option>
            <el-option label="良好" value="GOOD"></el-option>
            <el-option label="一般" value="AVERAGE"></el-option>
            <el-option label="较差" value="POOR"></el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="评价状态">
          <el-select
            v-model="searchForm.evaluationStatus"
            placeholder="请选择评价状态"
            clearable
            style="width: 120px;"
          >
            <el-option label="待评价" value="PENDING"></el-option>
            <el-option label="草稿" value="DRAFT"></el-option>
            <el-option label="已提交" value="SUBMITTED"></el-option>
            <el-option label="已审核" value="APPROVED"></el-option>
            <el-option label="已发布" value="PUBLISHED"></el-option>
            <el-option label="已评价" value="EVALUATED"></el-option>
          </el-select>
        </el-form-item>
        
        <el-form-item label="绩效评分">
          <el-input
            v-model="searchForm.minPerformanceScore"
            placeholder="最小评分"
            style="width: 100px;"
          />
          <span style="margin: 0 10px;">-</span>
          <el-input
            v-model="searchForm.maxPerformanceScore"
            placeholder="最大评分"
            style="width: 100px;"
          />
        </el-form-item>
        
        <el-form-item label="评价时间">
          <el-date-picker
            v-model="searchForm.evaluationDateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
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
        <i class="el-icon-plus"></i> 新增评价
      </el-button>
      <el-button
        type="success"
        :disabled="selectedPerformances.length === 0"
        @click="handleBatchEvaluate"
      >
        <i class="el-icon-check"></i> 批量评价
      </el-button>
      <el-button type="info" @click="handleExport">
        <i class="el-icon-download"></i> 导出数据
      </el-button>
      <el-button type="danger" @click="handleGenerateReport">
        <i class="el-icon-document"></i> 绩效报告
      </el-button>
    </el-card>

    <!-- 数据表格 -->
    <el-card shadow="never">
      <div slot="header">
        <span>财务绩效评价列表</span>
        <span class="table-count">（共 {{ total }} 条）</span>
      </div>
      
      <el-table
        v-loading="loading"
        :data="performancesList"
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

        <el-table-column label="报告期间" prop="period" width="100" align="center">
          <template slot-scope="scope">
            <el-tag type="success" size="mini">{{ scope.row.period || '-' }}</el-tag>
          </template>
        </el-table-column>

        <el-table-column label="绩效等级" prop="performanceLevel" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getPerformanceLevelTag(scope.row.performanceLevel)" size="mini">
              {{ getPerformanceLevelText(scope.row.performanceLevel) }}
            </el-tag>
          </template>
        </el-table-column>

        <el-table-column label="绩效评分" prop="performanceScore" width="100" align="center">
          <template slot-scope="scope">
            <span :style="{ color: getScoreColor(scope.row.performanceScore), fontWeight: 'bold' }">
              {{ scope.row.performanceScore || '-' }}
            </span>
          </template>
        </el-table-column>

        <el-table-column label="营业收入" prop="revenue" width="130" align="center">
          <template slot-scope="scope">
            {{ formatNumber(scope.row.revenue) }}万元
          </template>
        </el-table-column>

        <el-table-column label="净利润" prop="netProfit" width="130" align="center">
          <template slot-scope="scope">
            <span :style="{ color: scope.row.netProfit >= 0 ? '#67C23A' : '#F56C6C' }">
              {{ formatNumber(scope.row.netProfit) }}万元
            </span>
          </template>
        </el-table-column>

        <el-table-column label="总资产" prop="totalAssets" width="130" align="center">
          <template slot-scope="scope">
            {{ formatNumber(scope.row.totalAssets) }}万元
          </template>
        </el-table-column>

        <el-table-column label="ROE(%)" prop="roe" width="90" align="center">
          <template slot-scope="scope">
            {{ scope.row.roe !== null && scope.row.roe !== undefined ? scope.row.roe : '-' }}
          </template>
        </el-table-column>

        <el-table-column label="ROA(%)" prop="roa" width="90" align="center">
          <template slot-scope="scope">
            {{ scope.row.roa !== null && scope.row.roa !== undefined ? scope.row.roa : '-' }}
          </template>
        </el-table-column>

        <el-table-column label="评价状态" prop="evaluationStatus" width="100" align="center">
          <template slot-scope="scope">
            <el-tag :type="getEvaluationStatusTag(scope.row.evaluationStatus)" size="mini">
              {{ getEvaluationStatusText(scope.row.evaluationStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column label="操作" width="240" fixed="right">
          <template slot-scope="scope">
            <el-button size="mini" @click="handleView(scope.row)">查看</el-button>
            <el-button size="mini" type="primary" @click="handleEdit(scope.row)">编辑</el-button>
            <el-button size="mini" type="success" @click="handleEvaluate(scope.row)">评价</el-button>
            <el-button size="mini" type="warning" @click="handleBenchmark(scope.row)">对标</el-button>
            <el-button size="mini" type="danger" @click="handleDelete(scope.row)">删除</el-button>
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
          @pagination="getPerformancesList"
        />
      </div>
    </el-card>

    <!-- 对话框组件 -->
    <FinancialPerformanceDialog
      :visible.sync="performanceDialogVisible"
      :performance-data="currentPerformance"
      :dialog-type="dialogType"
      @refresh="getPerformancesList"
    />

    <PerformanceEvaluationDialog
      :visible.sync="evaluationDialogVisible"
      :evaluation-data="currentPerformance"
      @refresh="getPerformancesList"
    />

    <PerformanceBenchmarkDialog
      :visible.sync="benchmarkDialogVisible"
      :benchmark-info="currentPerformance"
    />
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { mapGetters } from 'vuex'
import Pagination from '@/components/Pagination'
import FinancialPerformanceDialog from './components/FinancialPerformanceDialog'
import PerformanceEvaluationDialog from './components/PerformanceEvaluationDialog'
import PerformanceBenchmarkDialog from './components/PerformanceBenchmarkDialog'
import {
  getFinancialPerformanceList,
  getPerformanceStatistics,
  getPerformanceDistribution,
  getPerformanceTrend,
  deleteFinancialPerformance,
  exportPerformanceData,
  generatePerformanceReport,
  batchEvaluate
} from '@/api/stateAssets/financialPerformance'

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
  name: 'FinancialPerformance',
  components: {
    Pagination,
    FinancialPerformanceDialog,
    PerformanceEvaluationDialog,
    PerformanceBenchmarkDialog
  },
  data() {
    return {
      loading: false,
      performancesList: [],
      selectedPerformances: [],
      total: 0,
      statistics: {
        totalEvaluations: 0,
        excellentPerformance: 0,
        poorPerformance: 0,
        avgPerformanceScore: 0
      },
      searchForm: {
        enterpriseName: '',
        performanceLevel: '',
        evaluationStatus: '',
        minPerformanceScore: '',
        maxPerformanceScore: '',
        evaluationDateRange: [],
        pageNum: 1,
        pageSize: 10
      },
      // 图表实例
      performanceDistributionChart: null,
      performanceTrendChart: null,
      // 对话框相关
      performanceDialogVisible: false,
      evaluationDialogVisible: false,
      benchmarkDialogVisible: false,
      currentPerformance: {},
      dialogType: 'add'
    }
  },
  created() {
    this.getStatistics()
    this.getPerformancesList()
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
    getStatistics() {
      getPerformanceStatistics().then(response => {
        if (response && response.data) {
          this.statistics = {
            totalEvaluations: response.data.totalEvaluations || 0,
            excellentPerformance: response.data.excellentPerformance || 0,
            poorPerformance: response.data.poorPerformance || 0,
            avgPerformanceScore: response.data.avgPerformanceScore || 0
          }
        }
      }).catch(err => {
        console.error('获取统计数据失败:', err)
      })
    },

    // 获取绩效列表
    getPerformancesList() {
      this.loading = true
      const params = { ...this.searchForm }
      // 处理日期范围
      if (params.evaluationDateRange && params.evaluationDateRange.length === 2) {
        params.startDate = params.evaluationDateRange[0] ? new Date(params.evaluationDateRange[0]).toISOString().split('T')[0] : ''
        params.endDate = params.evaluationDateRange[1] ? new Date(params.evaluationDateRange[1]).toISOString().split('T')[0] : ''
      }
      delete params.evaluationDateRange
      getFinancialPerformanceList(params).then(response => {
        if (response && response.data) {
          this.performancesList = response.data.tlist || response.data.list || []
          this.total = response.data.total || 0
        }
        this.loading = false
      }).catch(err => {
        console.error('获取绩效列表失败:', err)
        this.performancesList = []
        this.total = 0
        this.loading = false
      })
    },

    // 初始化图表
    initCharts() {
      this.performanceDistributionChart = echarts.init(this.$refs.performanceDistributionChart)
      this.performanceTrendChart = echarts.init(this.$refs.performanceTrendChart)
      
      // 监听窗口大小变化
      window.addEventListener('resize', this.handleResize)
    },

    // 加载图表数据
    loadChartData() {
      getPerformanceDistribution().then(response => {
        if (response && response.data) {
          this.updatePerformanceDistributionChart(response.data)
        }
      }).catch(err => {
        console.error('获取绩效分布数据失败:', err)
      })

      getPerformanceTrend().then(response => {
        if (response && response.data) {
          this.updatePerformanceTrendChart(response.data)
        }
      }).catch(err => {
        console.error('获取绩效趋势数据失败:', err)
      })
    },

    // 更新绩效分布图表
    updatePerformanceDistributionChart(data) {
      const option = {
        title: {
          text: '绩效等级分布',
          left: 'center',
          textStyle: { fontSize: 14 }
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        series: [{
          name: '绩效等级',
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
      this.performanceDistributionChart.setOption(option)
    },

    // 更新绩效趋势图表
    updatePerformanceTrendChart(data) {
      const option = {
        title: {
          text: '绩效趋势分析',
          left: 'center',
          textStyle: { fontSize: 14 }
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['优秀', '良好', '一般', '较差'],
          bottom: 10
        },
        xAxis: {
          type: 'category',
          data: data.map(item => item.date)
        },
        yAxis: {
          type: 'value',
          name: '企业数量'
        },
        series: [
          {
            name: '优秀',
            type: 'line',
            data: data.map(item => item.excellent),
            smooth: true,
            itemStyle: { color: '#67C23A' }
          },
          {
            name: '良好',
            type: 'line',
            data: data.map(item => item.good),
            smooth: true,
            itemStyle: { color: '#409EFF' }
          },
          {
            name: '一般',
            type: 'line',
            data: data.map(item => item.average),
            smooth: true,
            itemStyle: { color: '#E6A23C' }
          },
          {
            name: '较差',
            type: 'line',
            data: data.map(item => item.poor),
            smooth: true,
            itemStyle: { color: '#F56C6C' }
          }
        ]
      }
      this.performanceTrendChart.setOption(option)
    },

    // 搜索
    handleSearch() {
      this.searchForm.pageNum = 1
      this.getPerformancesList()
    },

    // 重置搜索表单
    resetSearchForm() {
      this.$refs.searchForm.resetFields()
      this.searchForm = {
        enterpriseName: '',
        performanceLevel: '',
        evaluationStatus: '',
        minPerformanceScore: '',
        maxPerformanceScore: '',
        evaluationDateRange: [],
        pageNum: 1,
        pageSize: 10
      }
      this.getPerformancesList()
    },

    // 表格选择变化
    handleSelectionChange(selection) {
      this.selectedPerformances = selection
    },

    // 新增评价
    handleAdd() {
      this.dialogType = 'add'
      this.currentPerformance = {}
      this.performanceDialogVisible = true
    },

    // 查看绩效
    handleView(row) {
      this.dialogType = 'view'
      this.currentPerformance = { ...row }
      this.performanceDialogVisible = true
    },

    // 编辑绩效
    handleEdit(row) {
      this.dialogType = 'edit'
      this.currentPerformance = { ...row }
      this.performanceDialogVisible = true
    },

    // 评价绩效
    handleEvaluate(row) {
      this.currentPerformance = {
        ...row,
        evaluationPeriod: row.evaluationPeriod || row.period,
        evaluationType: row.evaluationType || 'ANNUAL'
      }
      this.evaluationDialogVisible = true
    },

    // 对标分析
    handleBenchmark(row) {
      this.currentPerformance = {
        ...row,
        evaluationPeriod: row.evaluationPeriod || row.period,
        evaluationType: row.evaluationType || 'ANNUAL'
      }
      this.benchmarkDialogVisible = true
    },

    // 对比绩效
    handleCompare(row) {
      this.currentPerformance = { ...row }
      this.benchmarkDialogVisible = true
    },

    // 批量评价
    handleBatchEvaluate() {
      if (this.selectedPerformances.length === 0) {
        this.$message.warning('请选择要评价的绩效')
        return
      }
      this.$confirm(`确认对选中的 ${this.selectedPerformances.length} 条记录执行批量评价？`, '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(async() => {
        const loading = this.$loading({ lock: true, text: '正在执行批量评价...', background: 'rgba(0, 0, 0, 0.7)' })
        try {
          const ids = this.selectedPerformances.map(item => item.id || item.performanceId)
          const res = await batchEvaluate({ ids })
          if (res && res.data) {
            this.$message.success(`批量评价完成：成功 ${res.data.successCount} 条，失败 ${res.data.failCount} 条`)
            this.getPerformancesList()
            this.getStatistics()
          } else {
            this.$message.error(res.msg || '批量评价失败')
          }
        } catch (error) {
          this.$message.error('批量评价失败：' + (error.message || '未知错误'))
        } finally {
          loading.close()
        }
      }).catch(() => {})
    },

    // 导出数据
    handleExport() {
      this.$confirm('确认导出当前筛选条件下的绩效评价数据？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(async() => {
        const loading = this.$loading({ lock: true, text: '正在导出数据...', background: 'rgba(0, 0, 0, 0.7)' })
        try {
          const params = { ...this.searchForm }
          if (params.evaluationDateRange && params.evaluationDateRange.length === 2) {
            params.startDate = params.evaluationDateRange[0] ? new Date(params.evaluationDateRange[0]).toISOString().split('T')[0] : ''
            params.endDate = params.evaluationDateRange[1] ? new Date(params.evaluationDateRange[1]).toISOString().split('T')[0] : ''
          }
          delete params.evaluationDateRange
          const res = await exportPerformanceData(params)
          const blobData = res.data || res
          const blob = new Blob([blobData], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
          const url = window.URL.createObjectURL(blob)
          const link = document.createElement('a')
          link.href = url
          link.download = `财务绩效评价数据_${new Date().getTime()}.xlsx`
          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link)
          window.URL.revokeObjectURL(url)
          this.$message.success('导出成功')
        } catch (error) {
          this.$message.error('导出失败：' + (error.message || '未知错误'))
        } finally {
          loading.close()
        }
      }).catch(() => {})
    },

    // 生成报告
    handleGenerateReport() {
      this.$confirm('确认生成本期财务绩效评价报告？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(async() => {
        const loading = this.$loading({ lock: true, text: '正在生成绩效报告...', background: 'rgba(0, 0, 0, 0.7)' })
        try {
          const params = {
            evaluationPeriod: this.searchForm.evaluationPeriod || '',
            enterpriseName: this.searchForm.enterpriseName || '',
            performanceLevel: this.searchForm.performanceLevel || ''
          }
          const res = await generatePerformanceReport(params)
          if (res && (res.result === 200 || res.code === 1 || res.data)) {
            this.$message.success('绩效报告生成成功，请在报告列表中查看')
          } else {
            this.$message.error(res.msg || '报告生成失败')
          }
        } catch (error) {
          this.$message.error('报告生成失败：' + (error.message || '未知错误'))
        } finally {
          loading.close()
        }
      }).catch(() => {})
    },

    // 删除绩效
    handleDelete(row) {
      this.$confirm('确认删除该财务绩效评价记录？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteFinancialPerformance(row.id || row.performanceId).then(() => {
          this.$message.success('删除成功')
          this.getPerformancesList()
          this.getStatistics()
        }).catch(() => {
          this.$message.error('删除失败')
        })
      }).catch(() => {})
    },

    // 刷新图表
    refreshPerformanceChart() {
      this.loadChartData()
    },

    refreshTrendChart() {
      this.loadChartData()
    },

    // 窗口大小变化
    handleResize() {
      if (this.performanceDistributionChart) this.performanceDistributionChart.resize()
      if (this.performanceTrendChart) this.performanceTrendChart.resize()
    },

    // 销毁图表
    destroyCharts() {
      if (this.performanceDistributionChart) {
        this.performanceDistributionChart.dispose()
        this.performanceDistributionChart = null
      }
      if (this.performanceTrendChart) {
        this.performanceTrendChart.dispose()
        this.performanceTrendChart = null
      }
      window.removeEventListener('resize', this.handleResize)
    },

    // 获取绩效等级标签
    getPerformanceLevelTag(level) {
      const tagMap = {
        'EXCELLENT': 'success',
        'GOOD': 'primary',
        'AVERAGE': 'warning',
        'POOR': 'danger'
      }
      return tagMap[level] || 'info'
    },

    // 获取绩效等级文本
    getPerformanceLevelText(level) {
      const textMap = {
        'EXCELLENT': '优秀',
        'GOOD': '良好',
        'AVERAGE': '一般',
        'POOR': '较差'
      }
      return textMap[level] || level
    },

    // 获取评价状态标签
    getEvaluationStatusTag(status) {
      const tagMap = {
        'PENDING': 'info',
        'EVALUATING': 'warning',
        'EVALUATED': 'success',
        'FAILED': 'danger',
        'DRAFT': 'info',
        'SUBMITTED': 'warning',
        'APPROVED': 'primary',
        'PUBLISHED': 'success'
      }
      return tagMap[status] || 'info'
    },

    // 获取评价状态文本
    getEvaluationStatusText(status) {
      const textMap = {
        'PENDING': '待评价',
        'EVALUATING': '评价中',
        'EVALUATED': '已评价',
        'FAILED': '评价失败',
        'DRAFT': '草稿',
        'SUBMITTED': '已提交',
        'APPROVED': '已审核',
        'PUBLISHED': '已发布'
      }
      return textMap[status] || status || '-'
    },

    // 获取绩效评分颜色
    getScoreColor(score) {
      if (score >= 4.5) return '#67C23A'
      if (score >= 3.5) return '#409EFF'
      if (score >= 2.5) return '#E6A23C'
      return '#F56C6C'
    },

    // 格式化数字
    formatNumber(num) {
      if (!num && num !== 0) return '0'
      if (num >= 100000000) return (num / 100000000).toFixed(2) + '亿'
      if (num >= 10000) return (num / 10000).toFixed(1) + '万'
      return num.toLocaleString()
    }
  }
}
</script>

<style lang="scss" scoped>
.financial-ext-page { padding: 16px; background: #f0f2f5; min-height: calc(100vh - 84px); }
.page-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 14px; padding: 14px 20px; border-radius: 6px; color: #fff; }
.page-header-left { display: flex; align-items: center; font-size: 16px; font-weight: 600; i { font-size: 22px; margin-right: 10px; } }
.page-header-desc { font-size: 13px; opacity: 0.85; }
::v-deep .el-table th { background: #fff0f6 !important; }
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
