<template>
  <div class="visualization-container" :class="{ fullscreen: isFullscreen }">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <h1><i class="el-icon-data-analysis"></i>成本可视化分析</h1>
        <p>多维度数据可视化展示</p>
      </div>
      <div class="header-right">
        <el-date-picker
          v-model="dateRange"
          type="monthrange"
          format="yyyy年MM月"
          value-format="yyyy-MM"
          range-separator="至"
          start-placeholder="开始月份"
          end-placeholder="结束月份"
          @change="handleDateRangeChange"
          style="width: 240px"
        />
        <el-button type="primary" @click="handleExport" :loading="exportLoading">
          <i class="el-icon-download"></i>导出报告
        </el-button>
        <el-button @click="handleFullscreen">
          <i :class="isFullscreen ? 'el-icon-aim' : 'el-icon-full-screen'"></i>
          {{ isFullscreen ? '退出全屏' : '全屏' }}
        </el-button>
      </div>
    </div>

    <!-- 可视化图表网格 -->
    <div class="charts-grid" v-loading="loading">
      <!-- 第一行: 成本构成饼图 + 成本趋势折线图 -->
      <el-row :gutter="24">
        <el-col :span="8">
          <div class="chart-card">
            <div class="card-header">
              <h4><i class="el-icon-pie-chart"></i>成本构成分析</h4>
            </div>
            <div id="costPieChart" style="height: 350px;"></div>
          </div>
        </el-col>
        <el-col :span="16">
          <div class="chart-card">
            <div class="card-header">
              <h4><i class="el-icon-line-chart"></i>成本趋势分析</h4>
              <el-radio-group v-model="trendDimension" size="small" @change="loadChartData">
                <el-radio-button label="month">月度</el-radio-button>
                <el-radio-button label="quarter">季度</el-radio-button>
              </el-radio-group>
            </div>
            <div id="costTrendChart" style="height: 350px;"></div>
          </div>
        </el-col>
      </el-row>

      <!-- 第二行: 部门成本对比 + 产品成本分析 -->
      <el-row :gutter="24" style="margin-top: 24px;">
        <el-col :span="12">
          <div class="chart-card">
            <div class="card-header">
              <h4><i class="el-icon-data-bar"></i>部门成本对比</h4>
              <el-select v-model="compareType" size="small" @change="loadChartData" style="width: 150px">
                <el-option label="实际vs预算" value="actual_budget"></el-option>
                <el-option label="同比" value="yoy"></el-option>
                <el-option label="环比" value="mom"></el-option>
              </el-select>
            </div>
            <div id="deptCompareChart" style="height: 400px;"></div>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="chart-card">
            <div class="card-header">
              <h4><i class="el-icon-goods"></i>产品成本分析</h4>
              <el-select v-model="productMetric" size="small" @change="loadChartData" style="width: 150px">
                <el-option label="单位成本" value="unit_cost"></el-option>
                <el-option label="总成本" value="total_cost"></el-option>
                <el-option label="成本占比" value="cost_ratio"></el-option>
              </el-select>
            </div>
            <div id="productCostChart" style="height: 400px;"></div>
          </div>
        </el-col>
      </el-row>

      <!-- 第三行: 成本中心雷达图 -->
      <el-row :gutter="24" style="margin-top: 24px;">
        <el-col :span="24">
          <div class="chart-card">
            <div class="card-header">
              <h4><i class="el-icon-s-data"></i>成本中心综合评估雷达图</h4>
              <el-select
                v-model="radarCenters"
                size="small"
                multiple
                placeholder="选择成本中心(最多5个)"
                @change="handleRadarCenterChange"
                style="width: 300px"
              >
                <el-option
                  v-for="center in centerOptions"
                  :key="center.value"
                  :label="center.label"
                  :value="center.value">
                </el-option>
              </el-select>
            </div>
            <div id="radarChart" style="height: 450px;"></div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- 导出对话框 -->
    <el-dialog title="导出可视化报告" :visible.sync="exportDialogVisible" width="600px">
      <el-form :model="exportForm" label-width="100px">
        <el-form-item label="报告标题">
          <el-input v-model="exportForm.reportTitle" placeholder="请输入报告标题" />
        </el-form-item>
        <el-form-item label="导出格式">
          <el-radio-group v-model="exportForm.format">
            <el-radio label="pdf">PDF</el-radio>
            <el-radio label="xlsx">Excel</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="包含图表">
          <el-checkbox-group v-model="exportForm.includeCharts">
            <el-checkbox label="costComposition">成本构成</el-checkbox>
            <el-checkbox label="costTrend">成本趋势</el-checkbox>
            <el-checkbox label="deptComparison">部门对比</el-checkbox>
            <el-checkbox label="productCost">产品成本</el-checkbox>
            <el-checkbox label="centerRadar">雷达图</el-checkbox>
          </el-checkbox-group>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="exportDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmExport" :loading="exportLoading">确定导出</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import * as VisualizationAPI from '@/api/financialSharing/reportsVisualization'

export default {
  name: 'CostVisualizationAnalysis',
  data() {
    return {
      // 查询参数
      dateRange: [],

      // 图表维度选择
      trendDimension: 'month',
      compareType: 'actual_budget',
      productMetric: 'unit_cost',
      radarCenters: [],

      // 成本中心选项
      centerOptions: [],

      // ECharts实例
      charts: {
        costPie: null,
        costTrend: null,
        deptCompare: null,
        productCost: null,
        radar: null
      },

      // 导出
      exportDialogVisible: false,
      exportLoading: false,
      exportForm: {
        reportTitle: '成本可视化分析报告',
        format: 'pdf',
        includeCharts: ['costComposition', 'costTrend', 'deptComparison', 'productCost', 'centerRadar']
      },

      // 数据加载状态
      loading: false,

      // 全屏状态
      isFullscreen: false
    }
  },

  mounted() {
    this.initPage()
  },

  beforeDestroy() {
    // 销毁所有ECharts实例
    Object.values(this.charts).forEach(chart => {
      if (chart) {
        chart.dispose()
      }
    })
  },

  methods: {
    // 初始化页面
    async initPage() {
      // 设置默认时间范围为今年
      const now = new Date()
      const year = now.getFullYear()
      this.dateRange = [`${year}-01`, `${year}-12`]

      // 加载成本中心选项
      await this.loadCenterOptions()

      // 默认选择前3个成本中心
      if (this.centerOptions.length > 0) {
        this.radarCenters = this.centerOptions.slice(0, Math.min(3, this.centerOptions.length)).map(c => c.value)
      }

      // 加载图表数据
      await this.loadChartData()
    },

    // 加载成本中心选项
    async loadCenterOptions() {
      try {
        const response = await VisualizationAPI.getVisualizationCenterOptions({
          bookId: 1,
          tenantId: 1000
        })

        if (response.data && response.data.code === 1) {
          this.centerOptions = response.data.data || []
        }
      } catch (error) {
        console.error('加载成本中心选项失败:', error)
        this.$message.error('加载成本中心选项失败')
      }
    },

    // 加载图表数据
    async loadChartData() {
      this.loading = true
      try {
        const response = await VisualizationAPI.getVisualizationData({
          bookId: 1,
          tenantId: 1000,
          startDate: this.dateRange[0],
          endDate: this.dateRange[1],
          dimension: this.trendDimension,
          includeCharts: ['costComposition', 'costTrend', 'deptComparison', 'productCost', 'centerRadar']
        })

        if (response.data && response.data.code === 1) {
          const data = response.data.data

          // 渲染所有图表
          this.renderCostPieChart(data.costComposition)
          this.renderCostTrendChart(data.costTrend)
          this.renderDeptCompareChart(data.deptComparison)
          this.renderProductCostChart(data.productCost)
          this.renderRadarChart(data.centerRadar)
        } else {
          this.$message.error('加载图表数据失败')
        }
      } catch (error) {
        console.error('加载图表数据失败:', error)
        this.$message.error('加载图表数据失败,请稍后重试')
      } finally {
        this.loading = false
      }
    },

    // 渲染成本构成饼图
    renderCostPieChart(chartData) {
      if (!chartData) return

      if (!this.charts.costPie) {
        this.charts.costPie = this.$echarts.init(document.getElementById('costPieChart'))
      }

      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          left: 'left',
          data: chartData.legend
        },
        series: [
          {
            name: '成本构成',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2
            },
            label: {
              show: true,
              formatter: '{b}: {d}%'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: 16,
                fontWeight: 'bold'
              }
            },
            data: chartData.series[0].data
          }
        ]
      }

      this.charts.costPie.setOption(option)
    },

    // 渲染成本趋势折线图
    renderCostTrendChart(chartData) {
      if (!chartData) return

      if (!this.charts.costTrend) {
        this.charts.costTrend = this.$echarts.init(document.getElementById('costTrendChart'))
      }

      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          }
        },
        legend: {
          data: chartData.series.map(s => s.name)
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: chartData.categories
        },
        yAxis: {
          type: 'value'
        },
        series: chartData.series.map(s => ({
          name: s.name,
          type: 'line',
          smooth: true,
          data: s.data,
          areaStyle: {
            opacity: 0.3
          },
          lineStyle: {
            width: 2
          }
        }))
      }

      this.charts.costTrend.setOption(option)
    },

    // 渲染部门成本对比柱状图
    renderDeptCompareChart(chartData) {
      if (!chartData) return

      if (!this.charts.deptCompare) {
        this.charts.deptCompare = this.$echarts.init(document.getElementById('deptCompareChart'))
      }

      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        legend: {
          data: chartData.series.map(s => s.name)
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: chartData.categories
        },
        yAxis: {
          type: 'value'
        },
        series: chartData.series.map(s => ({
          name: s.name,
          type: 'bar',
          barWidth: '40%',
          data: s.data
        }))
      }

      this.charts.deptCompare.setOption(option)
    },

    // 渲染产品成本分析图
    renderProductCostChart(chartData) {
      if (!chartData) return

      if (!this.charts.productCost) {
        this.charts.productCost = this.$echarts.init(document.getElementById('productCostChart'))
      }

      const option = {
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
          data: chartData.categories
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '单位成本',
            type: 'bar',
            data: chartData.series[0].data,
            itemStyle: {
              color: new this.$echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: '#83bff6' },
                { offset: 0.5, color: '#188df0' },
                { offset: 1, color: '#188df0' }
              ])
            },
            emphasis: {
              itemStyle: {
                color: new this.$echarts.graphic.LinearGradient(0, 0, 0, 1, [
                  { offset: 0, color: '#2378f7' },
                  { offset: 0.7, color: '#2378f7' },
                  { offset: 1, color: '#83bff6' }
                ])
              }
            }
          }
        ]
      }

      this.charts.productCost.setOption(option)
    },

    // 渲染雷达图
    renderRadarChart(chartData) {
      if (!chartData) return

      if (!this.charts.radar) {
        this.charts.radar = this.$echarts.init(document.getElementById('radarChart'))
      }

      const option = {
        tooltip: {
          trigger: 'item'
        },
        legend: {
          data: chartData.series.map(s => s.name)
        },
        radar: {
          indicator: chartData.indicators,
          radius: 65
        },
        series: [
          {
            name: '成本中心评估',
            type: 'radar',
            data: chartData.series.map(s => ({
              value: s.value,
              name: s.name,
              areaStyle: {
                opacity: 0.3
              }
            }))
          }
        ]
      }

      this.charts.radar.setOption(option)
    },

    // 切换时间范围
    handleDateRangeChange() {
      this.loadChartData()
    },

    // 雷达图中心选择变更
    handleRadarCenterChange(value) {
      if (value.length > 5) {
        this.$message.warning('最多只能选择5个成本中心')
        this.radarCenters = value.slice(0, 5)
      }
      this.loadChartData()
    },

    // 导出报告
    handleExport() {
      this.exportDialogVisible = true
    },

    // 确认导出
    async confirmExport() {
      this.exportLoading = true
      try {
        const response = await VisualizationAPI.exportVisualizationReport({
          bookId: 1,
          tenantId: 1000,
          startDate: this.dateRange[0],
          endDate: this.dateRange[1],
          format: this.exportForm.format,
          includeCharts: this.exportForm.includeCharts,
          reportTitle: this.exportForm.reportTitle
        })

        // 创建下载链接
        const blob = new Blob([response.data], {
          type: this.exportForm.format === 'pdf' ? 'application/pdf' : 'application/vnd.ms-excel'
        })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = `${this.exportForm.reportTitle}_${this.dateRange[1]}.${this.exportForm.format}`
        link.click()
        window.URL.revokeObjectURL(url)

        this.$message.success('导出成功')
        this.exportDialogVisible = false
      } catch (error) {
        console.error('导出失败:', error)
        this.$message.error('导出失败,请稍后重试')
      } finally {
        this.exportLoading = false
      }
    },

    // 全屏切换
    handleFullscreen() {
      this.isFullscreen = !this.isFullscreen

      if (this.isFullscreen) {
        if (document.documentElement.requestFullscreen) {
          document.documentElement.requestFullscreen()
        }
      } else {
        if (document.exitFullscreen) {
          document.exitFullscreen()
        }
      }

      // 重新渲染图表以适应新尺寸
      setTimeout(() => {
        Object.values(this.charts).forEach(chart => {
          if (chart) {
            chart.resize()
          }
        })
      }, 300)
    }
  }
}
</script>

<style lang="scss" scoped>
.visualization-container {
  padding: 20px;
  background-color: #f5f7fa;
  min-height: 100vh;

  // 页面头部
  .page-header {
    background: white;
    border-radius: 8px;
    padding: 24px;
    margin-bottom: 24px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    display: flex;
    justify-content: space-between;
    align-items: center;

    .header-left {
      h1 {
        font-size: 24px;
        font-weight: 600;
        color: #303133;
        margin: 0 0 8px 0;

        i {
          color: #409eff;
          margin-right: 8px;
        }
      }

      p {
        font-size: 14px;
        color: #909399;
        margin: 0;
      }
    }

    .header-right {
      display: flex;
      gap: 12px;
      align-items: center;
    }
  }

  // 图表网格
  .charts-grid {
    .chart-card {
      background: white;
      border-radius: 8px;
      padding: 20px;
      box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);

      .card-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 16px;

        h4 {
          font-size: 16px;
          font-weight: 600;
          color: #303133;
          margin: 0;

          i {
            margin-right: 8px;
            color: #409eff;
          }
        }
      }
    }
  }

  // 全屏样式
  &.fullscreen {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    z-index: 9999;
    background: #1a1a1a;
    padding: 40px;
    overflow-y: auto;

    .page-header {
      background: rgba(255, 255, 255, 0.05);
      color: white;

      .header-left h1,
      .header-left p {
        color: white;
      }
    }

    .chart-card {
      background: rgba(255, 255, 255, 0.05);
      color: white;

      .card-header h4 {
        color: white;
      }
    }
  }
}
</style>
