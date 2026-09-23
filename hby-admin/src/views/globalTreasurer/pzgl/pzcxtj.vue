<template>
  <div class="bill-query-statistics">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-data-analysis"></i>
            票据查询统计
          </h2>
          <p class="page-description">票据数据的多维度查询、统计分析和报表展示</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-refresh" @click="handleRefresh">
            刷新数据
          </el-button>
          <el-button type="success" icon="el-icon-download" @click="handleExportReport">
            导出报表
          </el-button>
          <el-button type="info" icon="el-icon-printer" @click="handlePrintReport">
            打印报表
          </el-button>
        </div>
      </div>
    </div>

    <!-- 统计概览卡片 -->
    <div class="statistics-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon total-icon">
                <i class="el-icon-document"></i>
              </div>
              <div class="card-info">
                <div class="card-title">票据总数</div>
                <div class="card-value">{{ statisticsData.totalCount }}</div>
                <div class="card-change">张</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon amount-icon">
                <i class="el-icon-money"></i>
              </div>
              <div class="card-info">
                <div class="card-title">票据总额</div>
                <div class="card-value">{{ statisticsData.totalAmount }}</div>
                <div class="card-change">万元</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon active-icon">
                <i class="el-icon-success"></i>
              </div>
              <div class="card-info">
                <div class="card-title">有效票据</div>
                <div class="card-value">{{ statisticsData.activeCount }}</div>
                <div class="card-change positive">{{ statisticsData.activeRate }}%</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon growth-icon">
                <i class="el-icon-trend-charts"></i>
              </div>
              <div class="card-info">
                <div class="card-title">月增长率</div>
                <div class="card-value">{{ statisticsData.growthRate }}</div>
                <div class="card-change positive">%</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 查询条件区域 -->
    <el-card class="query-card" shadow="never">
      <div class="query-form">
        <el-form :inline="true" :model="queryForm" class="demo-form-inline">
          <el-form-item label="统计维度">
            <el-select v-model="queryForm.dimension" placeholder="请选择统计维度" @change="handleDimensionChange">
              <el-option label="按票据类型" value="billType" />
              <el-option label="按票据状态" value="billStatus" />
              <el-option label="按承兑银行" value="acceptingBank" />
              <el-option label="按时间分布" value="timeDistribution" />
              <el-option label="按金额区间" value="amountRange" />
            </el-select>
          </el-form-item>
          <el-form-item label="时间范围">
            <el-date-picker
              v-model="queryForm.dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              style="width: 240px;"
              @change="handleDateRangeChange"
            />
          </el-form-item>
          <el-form-item label="票据类型">
            <el-select v-model="queryForm.billType" placeholder="请选择票据类型" clearable multiple>
              <el-option label="银行承兑汇票" value="BANK_ACCEPTANCE" />
              <el-option label="商业承兑汇票" value="COMMERCIAL_ACCEPTANCE" />
              <el-option label="支票" value="CHECK" />
              <el-option label="本票" value="PROMISSORY_NOTE" />
              <el-option label="电子票据" value="ELECTRONIC_BILL" />
            </el-select>
          </el-form-item>
          <el-form-item label="票据状态">
            <el-select v-model="queryForm.billStatus" placeholder="请选择票据状态" clearable multiple>
              <el-option label="持有" value="HOLDING" />
              <el-option label="已背书" value="ENDORSED" />
              <el-option label="已贴现" value="DISCOUNTED" />
              <el-option label="已到期" value="MATURED" />
              <el-option label="已作废" value="CANCELLED" />
            </el-select>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-search" @click="handleQuery">
              查询统计
            </el-button>
            <el-button type="default" icon="el-icon-refresh" @click="resetQuery">
              重置条件
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>

    <!-- 统计图表区域 -->
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card class="chart-card" shadow="never">
          <div class="chart-header">
            <h3>{{ getChartTitle() }}</h3>
            <div class="chart-controls">
              <el-radio-group v-model="chartType" size="small" @change="handleChartTypeChange">
                <el-radio-button label="pie">饼图</el-radio-button>
                <el-radio-button label="bar">柱状图</el-radio-button>
                <el-radio-button label="line">折线图</el-radio-button>
              </el-radio-group>
            </div>
          </div>
          <div id="mainChart" class="chart-container"></div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="chart-card" shadow="never">
          <div class="chart-header">
            <h3>趋势分析</h3>
            <div class="chart-controls">
              <el-radio-group v-model="trendPeriod" size="small" @change="handleTrendPeriodChange">
                <el-radio-button label="7D">7天</el-radio-button>
                <el-radio-button label="30D">30天</el-radio-button>
                <el-radio-button label="90D">90天</el-radio-button>
                <el-radio-button label="1Y">1年</el-radio-button>
              </el-radio-group>
            </div>
          </div>
          <div id="trendChart" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 统计数据表格 -->
    <el-card class="table-card" shadow="never">
      <div class="table-header">
        <h3>统计数据详情</h3>
        <div class="table-controls">
          <el-button size="small" @click="handleToggleView">
            {{ showPercentage ? '显示数值' : '显示百分比' }}
          </el-button>
        </div>
      </div>
      <el-table
        :data="statisticsTableData"
        border
        fit
        highlight-current-row
        style="width: 100%;"
        v-loading="tableLoading"
        :summary-method="getSummaries"
        show-summary
      >
        <el-table-column label="序号" type="index" width="60" align="center" />
        <el-table-column label="分类" prop="category" width="150" align="center" />
        <el-table-column label="票据数量" width="120" align="center">
          <template slot-scope="{row}">
            <span class="count-value">{{ row.count }}张</span>
          </template>
        </el-table-column>
        <el-table-column label="票据金额" width="150" align="center">
          <template slot-scope="{row}">
            <span class="amount-value">{{ formatCurrency(row.amount) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="占比" width="100" align="center">
          <template slot-scope="{row}">
            <span class="percentage-value">{{ row.percentage }}%</span>
          </template>
        </el-table-column>
        <el-table-column label="平均金额" width="150" align="center">
          <template slot-scope="{row}">
            <span class="avg-amount">{{ formatCurrency(row.avgAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="环比增长" width="120" align="center">
          <template slot-scope="{row}">
            <span :class="getGrowthClass(row.growth)">
              <i :class="getGrowthIcon(row.growth)"></i>
              {{ Math.abs(row.growth) }}%
            </span>
          </template>
        </el-table-column>
        <el-table-column label="备注" prop="remark" min-width="200" />
      </el-table>
    </el-card>

    <!-- 详细报表区域 -->
    <el-card class="report-card" shadow="never">
      <div class="report-header">
        <h3>详细报表</h3>
        <div class="report-controls">
          <el-button-group>
            <el-button :type="reportType === 'summary' ? 'primary' : 'default'" size="small" @click="reportType = 'summary'">
              汇总报表
            </el-button>
            <el-button :type="reportType === 'detail' ? 'primary' : 'default'" size="small" @click="reportType = 'detail'">
              明细报表
            </el-button>
            <el-button :type="reportType === 'analysis' ? 'primary' : 'default'" size="small" @click="reportType = 'analysis'">
              分析报表
            </el-button>
          </el-button-group>
        </div>
      </div>
      
      <!-- 汇总报表 -->
      <div v-if="reportType === 'summary'" class="summary-report">
        <el-descriptions :column="3" border>
          <el-descriptions-item label="统计期间">{{ getStatisticsPeriod() }}</el-descriptions-item>
          <el-descriptions-item label="票据总数">{{ statisticsData.totalCount }}张</el-descriptions-item>
          <el-descriptions-item label="票据总额">{{ formatCurrency(statisticsData.totalAmount * 10000) }}</el-descriptions-item>
          <el-descriptions-item label="平均金额">{{ formatCurrency(statisticsData.avgAmount) }}</el-descriptions-item>
          <el-descriptions-item label="最大金额">{{ formatCurrency(statisticsData.maxAmount) }}</el-descriptions-item>
          <el-descriptions-item label="最小金额">{{ formatCurrency(statisticsData.minAmount) }}</el-descriptions-item>
          <el-descriptions-item label="有效票据">{{ statisticsData.activeCount }}张</el-descriptions-item>
          <el-descriptions-item label="有效率">{{ statisticsData.activeRate }}%</el-descriptions-item>
          <el-descriptions-item label="月增长率">{{ statisticsData.growthRate }}%</el-descriptions-item>
        </el-descriptions>
      </div>
      
      <!-- 明细报表 -->
      <div v-if="reportType === 'detail'" class="detail-report">
        <el-table :data="detailReportData" border size="small" max-height="400">
          <el-table-column label="票据号码" prop="billNumber" width="150" />
          <el-table-column label="票据类型" prop="billType" width="120" />
          <el-table-column label="票据金额" prop="billAmount" width="120" align="right" />
          <el-table-column label="出票日期" prop="issueDate" width="120" />
          <el-table-column label="到期日期" prop="maturityDate" width="120" />
          <el-table-column label="票据状态" prop="billStatus" width="100" />
          <el-table-column label="承兑银行" prop="acceptingBank" min-width="150" />
        </el-table>
      </div>
      
      <!-- 分析报表 -->
      <div v-if="reportType === 'analysis'" class="analysis-report">
        <el-row :gutter="20">
          <el-col :span="12">
            <div class="analysis-item">
              <h4>票据类型分析</h4>
              <div class="analysis-content">
                <p>银行承兑汇票占比最高，达到65%，显示企业对银行信用的依赖度较高。</p>
                <p>商业承兑汇票占比25%，反映了企业间的商业信用关系。</p>
                <p>电子票据占比逐步提升，数字化转型趋势明显。</p>
              </div>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="analysis-item">
              <h4>金额分布分析</h4>
              <div class="analysis-content">
                <p>大额票据（>100万）占总数的15%，但占总金额的60%。</p>
                <p>中等金额票据（10-100万）是主要构成部分。</p>
                <p>小额票据（<10万）数量较多但金额占比较小。</p>
              </div>
            </div>
          </el-col>
        </el-row>
        <el-row :gutter="20" style="margin-top: 20px;">
          <el-col :span="12">
            <div class="analysis-item">
              <h4>时间趋势分析</h4>
              <div class="analysis-content">
                <p>近3个月票据业务呈上升趋势，月均增长率12%。</p>
                <p>季末票据到期集中，需要提前做好资金安排。</p>
                <p>电子票据使用率持续提升，传统纸质票据逐步减少。</p>
              </div>
            </div>
          </el-col>
          <el-col :span="12">
            <div class="analysis-item">
              <h4>风险提示</h4>
              <div class="analysis-content">
                <p class="risk-warning">即将到期票据18张，总金额2.3亿元，需及时处理。</p>
                <p class="risk-warning">商业承兑汇票风险相对较高，建议加强跟踪管理。</p>
                <p class="risk-info">建议优化票据结构，提高银行承兑汇票比例。</p>
              </div>
            </div>
          </el-col>
        </el-row>
      </div>
    </el-card>
  </div>
</template>

<script>
import { getBillStatisticsOverview, getBillTypeStatistics, getBillTrendAnalysis } from '@/api/globalTreasurer/pzgl'

export default {
  name: 'BillQueryStatistics',
  data() {
    return {
      queryForm: {
        dimension: 'billType',
        dateRange: null,
        billType: [],
        billStatus: []
      },
      statisticsData: {
        totalCount: 234,
        totalAmount: 18560.8,
        activeCount: 198,
        activeRate: 84.6,
        growthRate: 12.5,
        avgAmount: 793450,
        maxAmount: 5000000,
        minAmount: 50000
      },
      chartType: 'pie',
      trendPeriod: '30D',
      reportType: 'summary',
      showPercentage: false,
      tableLoading: false,
      statisticsTableData: [],
      detailReportData: [],
      mainChart: null,
      trendChart: null
    }
  },
  mounted() {
    this.initCharts()
    this.loadStatisticsData()
    this.loadDetailReportData()
  },
  beforeDestroy() {
    if (this.mainChart) {
      this.mainChart.dispose()
    }
    if (this.trendChart) {
      this.trendChart.dispose()
    }
  },
  methods: {
    initCharts() {
      const echarts = require('echarts')
      
      // 初始化主图表
      this.mainChart = echarts.init(document.getElementById('mainChart'))
      this.updateMainChart()
      
      // 初始化趋势图表
      this.trendChart = echarts.init(document.getElementById('trendChart'))
      this.updateTrendChart()
    },
    updateMainChart() {
      const data = [
        { name: '银行承兑汇票', value: 152, amount: 12065.2 },
        { name: '商业承兑汇票', value: 58, amount: 4628.6 },
        { name: '支票', value: 15, amount: 1205.8 },
        { name: '本票', value: 6, value: 482.3 },
        { name: '电子票据', value: 3, amount: 178.9 }
      ]
      
      let option = {}
      
      if (this.chartType === 'pie') {
        option = {
          tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b}: {c}张 ({d}%)'
          },
          legend: {
            orient: 'vertical',
            left: 10,
            data: data.map(item => item.name)
          },
          series: [
            {
              name: '票据类型',
              type: 'pie',
              radius: ['50%', '70%'],
              center: ['60%', '50%'],
              avoidLabelOverlap: false,
              label: {
                show: false,
                position: 'center'
              },
              emphasis: {
                label: {
                  show: true,
                  fontSize: '18',
                  fontWeight: 'bold'
                }
              },
              labelLine: {
                show: false
              },
              data: data
            }
          ]
        }
      } else if (this.chartType === 'bar') {
        option = {
          tooltip: {
            trigger: 'axis',
            axisPointer: {
              type: 'shadow'
            }
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
          },
          xAxis: {
            type: 'category',
            data: data.map(item => item.name),
            axisLabel: {
              rotate: 45
            }
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              name: '票据数量',
              type: 'bar',
              data: data.map(item => item.value),
              itemStyle: {
                color: '#409EFF'
              }
            }
          ]
        }
      } else if (this.chartType === 'line') {
        option = {
          tooltip: {
            trigger: 'axis'
          },
          grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
          },
          xAxis: {
            type: 'category',
            data: data.map(item => item.name)
          },
          yAxis: {
            type: 'value'
          },
          series: [
            {
              name: '票据数量',
              type: 'line',
              data: data.map(item => item.value),
              itemStyle: {
                color: '#409EFF'
              }
            }
          ]
        }
      }
      
      this.mainChart.setOption(option)
    },
    updateTrendChart() {
      const dates = this.generateDateLabels(30)
      const countData = this.generateMockData(30, 5, 15)
      const amountData = this.generateMockData(30, 500, 2000)
      
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          }
        },
        legend: {
          data: ['票据数量', '票据金额(万元)']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: dates
        },
        yAxis: [
          {
            type: 'value',
            name: '数量(张)',
            position: 'left'
          },
          {
            type: 'value',
            name: '金额(万元)',
            position: 'right'
          }
        ],
        series: [
          {
            name: '票据数量',
            type: 'line',
            data: countData,
            itemStyle: { color: '#409EFF' }
          },
          {
            name: '票据金额(万元)',
            type: 'line',
            yAxisIndex: 1,
            data: amountData,
            itemStyle: { color: '#E6A23C' }
          }
        ]
      }
      
      this.trendChart.setOption(option)
    },
    loadStatisticsData() {
      this.tableLoading = true
      // 模拟统计数据
      setTimeout(() => {
        this.statisticsTableData = [
          {
            category: '银行承兑汇票',
            count: 152,
            amount: 120652000,
            percentage: 65.0,
            avgAmount: 793763,
            growth: 12.5,
            remark: '主要票据类型，信用等级高'
          },
          {
            category: '商业承兑汇票',
            count: 58,
            amount: 46286000,
            percentage: 24.8,
            avgAmount: 798379,
            growth: -3.2,
            remark: '企业间商业信用，需关注风险'
          },
          {
            category: '支票',
            count: 15,
            amount: 12058000,
            percentage: 6.4,
            avgAmount: 803867,
            growth: 8.7,
            remark: '小额支付工具'
          },
          {
            category: '本票',
            count: 6,
            amount: 4823000,
            percentage: 2.6,
            avgAmount: 803833,
            growth: 15.3,
            remark: '银行本票，安全性高'
          },
          {
            category: '电子票据',
            count: 3,
            amount: 1789000,
            percentage: 1.2,
            avgAmount: 596333,
            growth: 45.8,
            remark: '数字化票据，发展迅速'
          }
        ]
        this.tableLoading = false
      }, 1000)
    },
    loadDetailReportData() {
      // 模拟明细数据
      this.detailReportData = [
        {
          billNumber: 'BA20240925001',
          billType: '银行承兑汇票',
          billAmount: '¥1,000,000.00',
          issueDate: '2024-09-25',
          maturityDate: '2024-12-25',
          billStatus: '持有',
          acceptingBank: '中国工商银行'
        },
        {
          billNumber: 'CA20240920002',
          billType: '商业承兑汇票',
          billAmount: '¥500,000.00',
          issueDate: '2024-09-20',
          maturityDate: '2024-10-20',
          billStatus: '持有',
          acceptingBank: '客户B公司'
        }
      ]
    },
    generateDateLabels(days) {
      const labels = []
      for (let i = days - 1; i >= 0; i--) {
        const date = new Date()
        date.setDate(date.getDate() - i)
        labels.push((date.getMonth() + 1) + '/' + date.getDate())
      }
      return labels
    },
    generateMockData(count, min, max) {
      const data = []
      for (let i = 0; i < count; i++) {
        data.push(Math.floor(Math.random() * (max - min) + min))
      }
      return data
    },
    handleDimensionChange() {
      this.handleQuery()
    },
    handleDateRangeChange() {
      this.handleQuery()
    },
    handleChartTypeChange() {
      this.updateMainChart()
    },
    handleTrendPeriodChange() {
      this.updateTrendChart()
    },
    handleQuery() {
      this.loadStatisticsData()
      this.updateMainChart()
      this.updateTrendChart()
    },
    resetQuery() {
      this.queryForm = {
        dimension: 'billType',
        dateRange: null,
        billType: [],
        billStatus: []
      }
      this.handleQuery()
    },
    handleRefresh() {
      this.handleQuery()
      this.$message({
        type: 'success',
        message: '数据刷新成功'
      })
    },
    handleExportReport() {
      this.$message({
        type: 'success',
        message: '报表导出成功'
      })
    },
    handlePrintReport() {
      window.print()
    },
    handleToggleView() {
      this.showPercentage = !this.showPercentage
    },
    getChartTitle() {
      const titleMap = {
        'billType': '票据类型分布',
        'billStatus': '票据状态分布',
        'acceptingBank': '承兑银行分布',
        'timeDistribution': '时间分布统计',
        'amountRange': '金额区间分布'
      }
      return titleMap[this.queryForm.dimension] || '统计分析'
    },
    getStatisticsPeriod() {
      if (this.queryForm.dateRange && this.queryForm.dateRange.length === 2) {
        return `${this.queryForm.dateRange[0]} 至 ${this.queryForm.dateRange[1]}`
      }
      return '全部时间'
    },
    getSummaries(param) {
      const { columns, data } = param
      const sums = []
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = '合计'
          return
        }
        if (index === 1) {
          sums[index] = '-'
          return
        }
        if (index === 2) {
          const values = data.map(item => Number(item.count))
          sums[index] = values.reduce((prev, curr) => prev + curr, 0) + '张'
          return
        }
        if (index === 3) {
          const values = data.map(item => Number(item.amount))
          const sum = values.reduce((prev, curr) => prev + curr, 0)
          sums[index] = this.formatCurrency(sum)
          return
        }
        sums[index] = '-'
      })
      return sums
    },
    getGrowthClass(growth) {
      if (growth > 0) return 'growth-positive'
      if (growth < 0) return 'growth-negative'
      return 'growth-neutral'
    },
    getGrowthIcon(growth) {
      if (growth > 0) return 'el-icon-top'
      if (growth < 0) return 'el-icon-bottom'
      return 'el-icon-minus'
    },
    formatCurrency(amount) {
      if (amount === undefined || amount === null) return '¥0.00'
      const formatter = new Intl.NumberFormat('zh-CN', {
        style: 'currency',
        currency: 'CNY',
        minimumFractionDigits: 2
      })
      return formatter.format(amount)
    }
  }
}
</script>

<style lang="scss" scoped>
.bill-query-statistics {
  padding: 20px;

  .page-header {
    margin-bottom: 20px;
    .header-content {
      display: flex;
      justify-content: space-between;
      align-items: center;
      .header-left {
        .page-title {
          margin: 0 0 8px 0;
          font-size: 24px;
          font-weight: 600;
          color: #303133;
          i {
            margin-right: 8px;
            color: #409EFF;
          }
        }
        .page-description {
          margin: 0;
          color: #606266;
          font-size: 14px;
        }
      }
    }
  }

  .statistics-overview {
    margin-bottom: 20px;
    .overview-card {
      .card-content {
        display: flex;
        align-items: center;
        .card-icon {
          width: 60px;
          height: 60px;
          border-radius: 8px;
          display: flex;
          align-items: center;
          justify-content: center;
          margin-right: 16px;
          i {
            font-size: 24px;
            color: white;
          }
          &.total-icon {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          }
          &.amount-icon {
            background: linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%);
          }
          &.active-icon {
            background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
          }
          &.growth-icon {
            background: linear-gradient(135deg, #ffecd2 0%, #fcb69f 100%);
          }
        }
        .card-info {
          flex: 1;
          .card-title {
            font-size: 14px;
            color: #909399;
            margin-bottom: 8px;
          }
          .card-value {
            font-size: 24px;
            font-weight: 600;
            color: #303133;
            margin-bottom: 4px;
          }
          .card-change {
            font-size: 12px;
            color: #909399;
            &.positive {
              color: #67C23A;
            }
          }
        }
      }
    }
  }

  .query-card, .chart-card, .table-card, .report-card {
    margin-bottom: 20px;
  }

  .chart-header, .table-header, .report-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    
    h3 {
      margin: 0;
      color: #303133;
      font-size: 16px;
      font-weight: 600;
    }
  }

  .chart-container {
    height: 300px;
    width: 100%;
  }

  .count-value, .amount-value {
    font-weight: 600;
    color: #409EFF;
  }

  .percentage-value {
    font-weight: 600;
    color: #E6A23C;
  }

  .avg-amount {
    color: #606266;
  }

  .growth-positive {
    color: #67C23A;
    font-weight: 600;
  }

  .growth-negative {
    color: #F56C6C;
    font-weight: 600;
  }

  .growth-neutral {
    color: #909399;
  }

  .summary-report {
    .el-descriptions {
      margin-bottom: 20px;
    }
  }

  .analysis-report {
    .analysis-item {
      h4 {
        margin: 0 0 12px 0;
        color: #303133;
        font-size: 14px;
        font-weight: 600;
        border-left: 3px solid #409EFF;
        padding-left: 8px;
      }
      
      .analysis-content {
        p {
          margin: 8px 0;
          color: #606266;
          line-height: 1.6;
          
          &.risk-warning {
            color: #F56C6C;
            font-weight: 600;
          }
          
          &.risk-info {
            color: #E6A23C;
          }
        }
      }
    }
  }
}
</style>
