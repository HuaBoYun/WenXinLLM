<template>
  <div class="cash-flow-forecast">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-content">
        <div class="header-left">
          <h2 class="page-title">
            <i class="el-icon-data-line"></i>
            现金流预测
          </h2>
          <p class="page-description">基于历史数据和业务计划，预测企业未来现金流入流出情况</p>
        </div>
        <div class="header-right">
          <el-button type="primary" icon="el-icon-refresh" @click="handleRefreshForecast">
            刷新预测
          </el-button>
          <el-button type="success" icon="el-icon-setting" @click="handleForecastSettings">
            预测设置
          </el-button>
          <el-button type="info" icon="el-icon-download" @click="handleExport">
            导出报告
          </el-button>
        </div>
      </div>
    </div>

    <!-- 预测概览卡片 -->
    <div class="forecast-overview">
      <el-row :gutter="20">
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon inflow-icon">
                <i class="el-icon-top"></i>
              </div>
              <div class="card-info">
                <div class="card-title">预测流入</div>
                <div class="card-value">{{ totalInflow }}</div>
                <div class="card-change positive">万元</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon outflow-icon">
                <i class="el-icon-bottom"></i>
              </div>
              <div class="card-info">
                <div class="card-title">预测流出</div>
                <div class="card-value">{{ totalOutflow }}</div>
                <div class="card-change negative">万元</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon net-icon">
                <i class="el-icon-minus"></i>
              </div>
              <div class="card-info">
                <div class="card-title">净现金流</div>
                <div class="card-value">{{ netCashFlow }}</div>
                <div class="card-change" :class="netCashFlow >= 0 ? 'positive' : 'negative'">万元</div>
              </div>
            </div>
          </el-card>
        </el-col>
        <el-col :span="6">
          <el-card class="overview-card">
            <div class="card-content">
              <div class="card-icon accuracy-icon">
                <i class="el-icon-pie-chart"></i>
              </div>
              <div class="card-info">
                <div class="card-title">预测准确率</div>
                <div class="card-value">{{ forecastAccuracy }}%</div>
                <div class="card-change">历史平均</div>
              </div>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <!-- 预测参数设置 -->
    <el-card class="settings-card" shadow="never">
      <div class="settings-form">
        <el-form :inline="true" :model="forecastParams" class="demo-form-inline">
          <el-form-item label="预测周期">
            <el-select v-model="forecastParams.period" placeholder="请选择预测周期" style="width: 120px;" @change="handlePeriodChange">
              <el-option label="7天" value="7D" />
              <el-option label="30天" value="30D" />
              <el-option label="90天" value="90D" />
              <el-option label="180天" value="180D" />
              <el-option label="365天" value="365D" />
            </el-select>
          </el-form-item>
          <el-form-item label="预测模型">
            <el-select v-model="forecastParams.model" placeholder="请选择预测模型" style="width: 150px;" @change="handleModelChange">
              <el-option label="线性回归" value="LINEAR" />
              <el-option label="移动平均" value="MOVING_AVERAGE" />
              <el-option label="指数平滑" value="EXPONENTIAL" />
              <el-option label="ARIMA" value="ARIMA" />
              <el-option label="神经网络" value="NEURAL" />
            </el-select>
          </el-form-item>
          <el-form-item label="币种">
            <el-select v-model="forecastParams.currency" placeholder="请选择币种" style="width: 100px;" @change="handleCurrencyChange">
              <el-option label="人民币" value="CNY" />
              <el-option label="美元" value="USD" />
              <el-option label="欧元" value="EUR" />
              <el-option label="全部" value="ALL" />
            </el-select>
          </el-form-item>
          <el-form-item label="置信度">
            <el-slider
              v-model="forecastParams.confidence"
              :min="80"
              :max="99"
              :step="1"
              show-input
              style="width: 200px;"
              @change="handleConfidenceChange"
            />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" icon="el-icon-refresh" @click="handleRefreshForecast">
              重新预测
            </el-button>
          </el-form-item>
        </el-form>
      </div>
    </el-card>

    <!-- 现金流趋势图表 -->
    <el-card class="chart-card" shadow="never">
      <div class="chart-header">
        <h3>现金流趋势预测</h3>
        <div class="chart-legend">
          <span class="legend-item">
            <i class="legend-dot inflow-dot"></i>
            现金流入
          </span>
          <span class="legend-item">
            <i class="legend-dot outflow-dot"></i>
            现金流出
          </span>
          <span class="legend-item">
            <i class="legend-dot net-dot"></i>
            净现金流
          </span>
        </div>
      </div>
      <div id="cashFlowChart" class="chart-container"></div>
    </el-card>

    <!-- 预测详情表格 -->
    <el-card class="table-card" shadow="never">
      <div class="table-header">
        <h3>预测详情</h3>
        <div class="table-actions">
          <el-button size="small" @click="handleViewByDay">按日查看</el-button>
          <el-button size="small" @click="handleViewByWeek">按周查看</el-button>
          <el-button size="small" @click="handleViewByMonth">按月查看</el-button>
        </div>
      </div>
      <el-table
        :data="forecastData"
        border
        fit
        highlight-current-row
        style="width: 100%;"
        v-loading="chartLoading"
      >
        <el-table-column label="日期" width="120px" align="center">
          <template slot-scope="{row}">
            <span>{{ row.date }}</span>
          </template>
        </el-table-column>
        <el-table-column label="期初余额" width="120px" align="center">
          <template slot-scope="{row}">
            <span class="balance-amount">{{ formatCurrency(row.openingBalance) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="预测流入" width="120px" align="center">
          <template slot-scope="{row}">
            <span class="inflow-amount">{{ formatCurrency(row.inflowAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="预测流出" width="120px" align="center">
          <template slot-scope="{row}">
            <span class="outflow-amount">{{ formatCurrency(row.outflowAmount) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="净现金流" width="120px" align="center">
          <template slot-scope="{row}">
            <span class="net-amount" :class="row.netCashFlow >= 0 ? 'positive' : 'negative'">
              {{ formatCurrency(row.netCashFlow) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="期末余额" width="120px" align="center">
          <template slot-scope="{row}">
            <span class="balance-amount">{{ formatCurrency(row.closingBalance) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="置信区间" width="150px" align="center">
          <template slot-scope="{row}">
            <span class="confidence-range">
              {{ formatCurrency(row.confidenceLower) }} ~ {{ formatCurrency(row.confidenceUpper) }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="风险等级" width="100px" align="center">
          <template slot-scope="{row}">
            <el-tag :type="getRiskTagType(row.riskLevel)" size="mini">
              {{ getRiskText(row.riskLevel) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="主要影响因素" min-width="200px">
          <template slot-scope="{row}">
            <span class="factors-text">{{ row.mainFactors }}</span>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 预测设置对话框 -->
    <el-dialog title="预测设置" :visible.sync="dialogSettingsVisible" width="600px">
      <el-form ref="settingsForm" :model="settingsForm" label-width="120px">
        <el-form-item label="历史数据期间">
          <el-select v-model="settingsForm.historicalPeriod" placeholder="请选择历史数据期间" style="width: 100%;">
            <el-option label="最近3个月" value="3M" />
            <el-option label="最近6个月" value="6M" />
            <el-option label="最近1年" value="1Y" />
            <el-option label="最近2年" value="2Y" />
            <el-option label="最近3年" value="3Y" />
          </el-select>
        </el-form-item>
        <el-form-item label="季节性调整">
          <el-switch v-model="settingsForm.seasonalAdjustment" />
        </el-form-item>
        <el-form-item label="异常值处理">
          <el-switch v-model="settingsForm.outlierHandling" />
        </el-form-item>
        <el-form-item label="业务计划权重">
          <el-slider
            v-model="settingsForm.businessPlanWeight"
            :min="0"
            :max="100"
            :step="5"
            show-input
            style="width: 100%;"
          />
        </el-form-item>
        <el-form-item label="自动更新频率">
          <el-select v-model="settingsForm.updateFrequency" placeholder="请选择更新频率" style="width: 100%;">
            <el-option label="每日" value="DAILY" />
            <el-option label="每周" value="WEEKLY" />
            <el-option label="每月" value="MONTHLY" />
            <el-option label="手动" value="MANUAL" />
          </el-select>
        </el-form-item>
        <el-form-item label="预警阈值(%)">
          <el-input-number
            v-model="settingsForm.alertThreshold"
            :min="1"
            :max="50"
            :step="1"
            style="width: 100%;"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogSettingsVisible = false">取消</el-button>
        <el-button type="primary" @click="saveSettings">保存设置</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getCashFlowForecast, updateForecastSettings } from '@/api/globalTreasurer/xjgl'

export default {
  name: 'CashFlowForecast',
  data() {
    return {
      chartLoading: false,
      totalInflow: 2580.5,
      totalOutflow: 2234.8,
      netCashFlow: 345.7,
      forecastAccuracy: 87.5,
      forecastParams: {
        period: '30D',
        model: 'LINEAR',
        currency: 'CNY',
        confidence: 95
      },
      forecastData: [],
      dialogSettingsVisible: false,
      settingsForm: {
        historicalPeriod: '1Y',
        seasonalAdjustment: true,
        outlierHandling: true,
        businessPlanWeight: 30,
        updateFrequency: 'DAILY',
        alertThreshold: 10
      },
      chart: null
    }
  },
  mounted() {
    this.initChart()
    this.loadForecastData()
  },
  beforeDestroy() {
    if (this.chart) {
      this.chart.dispose()
    }
  },
  methods: {
    initChart() {
      // 初始化图表
      const echarts = require('echarts')
      this.chart = echarts.init(document.getElementById('cashFlowChart'))
      
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
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
          data: this.generateDateRange(30)
        },
        yAxis: {
          type: 'value',
          axisLabel: {
            formatter: '{value}万'
          }
        },
        series: [
          {
            name: '现金流入',
            type: 'line',
            data: this.generateMockData(30, 50, 200),
            itemStyle: { color: '#67C23A' },
            areaStyle: { opacity: 0.3 }
          },
          {
            name: '现金流出',
            type: 'line',
            data: this.generateMockData(30, 40, 180),
            itemStyle: { color: '#F56C6C' },
            areaStyle: { opacity: 0.3 }
          },
          {
            name: '净现金流',
            type: 'line',
            data: this.generateNetCashFlow(30),
            itemStyle: { color: '#409EFF' },
            lineStyle: { width: 3 }
          }
        ]
      }
      
      this.chart.setOption(option)
    },
    loadForecastData() {
      this.chartLoading = true
      // 模拟加载预测数据
      setTimeout(() => {
        this.forecastData = this.generateForecastData(30)
        this.chartLoading = false
      }, 1000)
    },
    generateDateRange(days) {
      const dates = []
      const today = new Date()
      for (let i = 0; i < days; i++) {
        const date = new Date(today)
        date.setDate(today.getDate() + i)
        dates.push(date.toISOString().slice(5, 10))
      }
      return dates
    },
    generateMockData(days, min, max) {
      const data = []
      for (let i = 0; i < days; i++) {
        data.push(Math.floor(Math.random() * (max - min) + min))
      }
      return data
    },
    generateNetCashFlow(days) {
      const data = []
      for (let i = 0; i < days; i++) {
        data.push(Math.floor(Math.random() * 100 - 20))
      }
      return data
    },
    generateForecastData(days) {
      const data = []
      let balance = 5000000
      
      for (let i = 0; i < days; i++) {
        const date = new Date()
        date.setDate(date.getDate() + i)
        
        const inflowAmount = Math.floor(Math.random() * 1000000 + 500000)
        const outflowAmount = Math.floor(Math.random() * 800000 + 400000)
        const netCashFlow = inflowAmount - outflowAmount
        const closingBalance = balance + netCashFlow
        
        data.push({
          date: date.toISOString().slice(0, 10),
          openingBalance: balance,
          inflowAmount: inflowAmount,
          outflowAmount: outflowAmount,
          netCashFlow: netCashFlow,
          closingBalance: closingBalance,
          confidenceLower: netCashFlow * 0.9,
          confidenceUpper: netCashFlow * 1.1,
          riskLevel: this.calculateRiskLevel(closingBalance),
          mainFactors: this.getMainFactors(i)
        })
        
        balance = closingBalance
      }
      
      return data
    },
    calculateRiskLevel(balance) {
      if (balance < 1000000) return 'HIGH'
      if (balance < 3000000) return 'MEDIUM'
      return 'LOW'
    },
    getMainFactors(index) {
      const factors = [
        '销售回款、投资收益',
        '运营支出、税费缴纳',
        '项目投资、设备采购',
        '贷款还款、利息支付',
        '季节性波动、市场变化'
      ]
      return factors[index % factors.length]
    },
    handlePeriodChange() {
      this.handleRefreshForecast()
    },
    handleModelChange() {
      this.handleRefreshForecast()
    },
    handleCurrencyChange() {
      this.handleRefreshForecast()
    },
    handleConfidenceChange() {
      this.handleRefreshForecast()
    },
    handleRefreshForecast() {
      this.$message({
        type: 'success',
        message: '预测数据已刷新'
      })
      this.loadForecastData()
    },
    handleForecastSettings() {
      this.dialogSettingsVisible = true
    },
    handleExport() {
      this.$message({
        type: 'success',
        message: '预测报告导出成功'
      })
    },
    handleViewByDay() {
      this.loadForecastData()
    },
    handleViewByWeek() {
      this.$message({
        type: 'info',
        message: '切换到按周查看'
      })
    },
    handleViewByMonth() {
      this.$message({
        type: 'info',
        message: '切换到按月查看'
      })
    },
    saveSettings() {
      this.dialogSettingsVisible = false
      this.$message({
        type: 'success',
        message: '预测设置已保存'
      })
    },
    getRiskTagType(riskLevel) {
      const typeMap = {
        'LOW': 'success',
        'MEDIUM': 'warning',
        'HIGH': 'danger'
      }
      return typeMap[riskLevel] || 'info'
    },
    getRiskText(riskLevel) {
      const textMap = {
        'LOW': '低风险',
        'MEDIUM': '中风险',
        'HIGH': '高风险'
      }
      return textMap[riskLevel] || riskLevel
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
.cash-flow-forecast {
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

  .forecast-overview {
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
          &.inflow-icon {
            background: linear-gradient(135deg, #a8edea 0%, #fed6e3 100%);
          }
          &.outflow-icon {
            background: linear-gradient(135deg, #ff9a9e 0%, #fecfef 100%);
          }
          &.net-icon {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          }
          &.accuracy-icon {
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
            &.negative {
              color: #F56C6C;
            }
          }
        }
      }
    }
  }

  .settings-card, .chart-card, .table-card {
    margin-bottom: 20px;
  }

  .chart-header, .table-header {
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

  .chart-legend {
    display: flex;
    gap: 20px;
    
    .legend-item {
      display: flex;
      align-items: center;
      font-size: 12px;
      color: #606266;
      
      .legend-dot {
        width: 8px;
        height: 8px;
        border-radius: 50%;
        margin-right: 6px;
        
        &.inflow-dot {
          background-color: #67C23A;
        }
        &.outflow-dot {
          background-color: #F56C6C;
        }
        &.net-dot {
          background-color: #409EFF;
        }
      }
    }
  }

  .chart-container {
    height: 400px;
    width: 100%;
  }

  .balance-amount {
    color: #303133;
    font-weight: 600;
  }

  .inflow-amount {
    color: #67C23A;
    font-weight: 600;
  }

  .outflow-amount {
    color: #F56C6C;
    font-weight: 600;
  }

  .net-amount {
    font-weight: 600;
    &.positive {
      color: #67C23A;
    }
    &.negative {
      color: #F56C6C;
    }
  }

  .confidence-range {
    font-size: 12px;
    color: #909399;
  }

  .factors-text {
    font-size: 12px;
    color: #606266;
    line-height: 1.4;
  }
}
</style>
