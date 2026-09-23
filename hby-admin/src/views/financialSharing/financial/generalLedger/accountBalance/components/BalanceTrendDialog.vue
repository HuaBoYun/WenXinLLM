<template>
  <el-dialog
    title="科目余额趋势"
    :visible.sync="dialogVisible"
    width="900px"
    :before-close="handleClose">
    <div class="trend-dialog-content">
      <!-- 科目信息 -->
      <div class="subject-info">
        <el-row :gutter="16">
          <el-col :span="8">
            <div class="info-item">
              <label>科目编码：</label>
              <span>{{ subjectData.subjectCode }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>科目名称：</label>
              <span>{{ subjectData.subjectName }}</span>
            </div>
          </el-col>
          <el-col :span="8">
            <div class="info-item">
              <label>当前余额：</label>
              <span :class="{ 'debit-amount': subjectData.endingBalance > 0, 'credit-amount': subjectData.endingBalance < 0 }">
                {{ formatAmount(Math.abs(subjectData.endingBalance)) }}
                {{ subjectData.endingBalance > 0 ? '(借)' : subjectData.endingBalance < 0 ? '(贷)' : '' }}
              </span>
            </div>
          </el-col>
        </el-row>
      </div>

      <!-- 查询条件 -->
      <div class="query-form">
        <el-form :model="queryForm" :inline="true" label-width="80px">
          <el-form-item label="查询期间">
            <el-date-picker
              v-model="queryForm.dateRange"
              type="monthrange"
              range-separator="至"
              start-placeholder="开始月份"
              end-placeholder="结束月份"
              format="yyyy-MM"
              value-format="yyyy-MM">
            </el-date-picker>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="loadTrendData">查询</el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 趋势图表 -->
      <div class="trend-chart">
        <div ref="balanceTrendChart" style="height: 400px;"></div>
      </div>

      <!-- 趋势数据表格 -->
      <div class="trend-table">
        <h4>余额明细数据</h4>
        <el-table :data="trendData" border size="small" max-height="300">
          <el-table-column prop="period" label="期间" width="100" align="center"></el-table-column>
          <el-table-column prop="openingBalance" label="期初余额" width="120" align="right">
            <template slot-scope="scope">
              <span :class="{ 'debit-amount': scope.row.openingBalance > 0, 'credit-amount': scope.row.openingBalance < 0 }">
                {{ formatAmount(Math.abs(scope.row.openingBalance)) }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="currentDebit" label="本期借方" width="120" align="right">
            <template slot-scope="scope">
              {{ formatAmount(scope.row.currentDebit) }}
            </template>
          </el-table-column>
          <el-table-column prop="currentCredit" label="本期贷方" width="120" align="right">
            <template slot-scope="scope">
              {{ formatAmount(scope.row.currentCredit) }}
            </template>
          </el-table-column>
          <el-table-column prop="endingBalance" label="期末余额" width="120" align="right">
            <template slot-scope="scope">
              <span :class="{ 'debit-amount': scope.row.endingBalance > 0, 'credit-amount': scope.row.endingBalance < 0 }">
                {{ formatAmount(Math.abs(scope.row.endingBalance)) }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="balanceChange" label="余额变动" width="120" align="right">
            <template slot-scope="scope">
              <span :class="{ 'increase': scope.row.balanceChange > 0, 'decrease': scope.row.balanceChange < 0 }">
                {{ scope.row.balanceChange > 0 ? '+' : '' }}{{ formatAmount(scope.row.balanceChange) }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="changeRate" label="变动率" width="100" align="right">
            <template slot-scope="scope">
              <span :class="{ 'increase': scope.row.changeRate > 0, 'decrease': scope.row.changeRate < 0 }">
                {{ scope.row.changeRate > 0 ? '+' : '' }}{{ scope.row.changeRate }}%
              </span>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 统计分析 -->
      <div class="trend-analysis">
        <h4>趋势分析</h4>
        <el-row :gutter="16">
          <el-col :span="6">
            <div class="analysis-item">
              <div class="analysis-label">最高余额</div>
              <div class="analysis-value positive">{{ formatAmount(analysis.maxBalance) }}</div>
              <div class="analysis-period">{{ analysis.maxBalancePeriod }}</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="analysis-item">
              <div class="analysis-label">最低余额</div>
              <div class="analysis-value negative">{{ formatAmount(analysis.minBalance) }}</div>
              <div class="analysis-period">{{ analysis.minBalancePeriod }}</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="analysis-item">
              <div class="analysis-label">平均余额</div>
              <div class="analysis-value">{{ formatAmount(analysis.avgBalance) }}</div>
              <div class="analysis-period">统计期间</div>
            </div>
          </el-col>
          <el-col :span="6">
            <div class="analysis-item">
              <div class="analysis-label">总体趋势</div>
              <div class="analysis-value" :class="{ 'positive': analysis.trend > 0, 'negative': analysis.trend < 0 }">
                {{ analysis.trend > 0 ? '上升' : analysis.trend < 0 ? '下降' : '平稳' }}
              </div>
              <div class="analysis-period">{{ Math.abs(analysis.trend) }}%</div>
            </div>
          </el-col>
        </el-row>
      </div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="exportTrendData">导出数据</el-button>
    </div>
  </el-dialog>
</template>

<script>
import * as echarts from 'echarts'
import { getSubjectBalanceTrend } from '@/api/financialSharing/generalLedger'

export default {
  name: 'BalanceTrendDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    subjectData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      dialogVisible: false,
      trendChart: null,
      queryForm: {
        dateRange: []
      },
      trendData: [],
      analysis: {
        maxBalance: 0,
        maxBalancePeriod: '',
        minBalance: 0,
        minBalancePeriod: '',
        avgBalance: 0,
        trend: 0
      }
    }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
      if (val) {
        this.initDefaultDateRange()
        this.$nextTick(() => {
          this.initChart()
          this.loadTrendData()
        })
      }
    },
    dialogVisible(val) {
      this.$emit('update:visible', val)
      if (!val && this.trendChart) {
        this.trendChart.dispose()
        this.trendChart = null
      }
    }
  },
  methods: {
    initDefaultDateRange() {
      const now = new Date()
      const endMonth = now.getFullYear() + '-' + String(now.getMonth() + 1).padStart(2, '0')
      const startMonth = now.getFullYear() + '-' + String(now.getMonth() - 5).padStart(2, '0')
      this.queryForm.dateRange = [startMonth, endMonth]
    },
    initChart() {
      if (this.$refs.balanceTrendChart) {
        this.trendChart = echarts.init(this.$refs.balanceTrendChart)
      }
    },
    async loadTrendData() {
      if (!this.queryForm.dateRange || this.queryForm.dateRange.length !== 2) {
        this.$message.warning('请选择查询期间')
        return
      }

      try {
        const response = await getSubjectBalanceTrend(
          this.subjectData.subjectCode,
          this.queryForm.dateRange[0],
          this.queryForm.dateRange[1]
        )
        
        if (response.code === 200) {
          this.trendData = this.generateMockTrendData()
          this.calculateAnalysis()
          this.updateChart()
        }
      } catch (error) {
        this.$message.error('加载趋势数据失败：' + error.message)
        // 数据加载失败时的空状态降级（不再使用模拟数据）
        this.trendData = []
        this.calculateAnalysis()
        this.updateChart()
      }
    },
    generateMockTrendData() {
      // 暂未对接 API，先以空状态展示
      return []
    },
    calculateAnalysis() {
      if (this.trendData.length === 0) return
      
      const balances = this.trendData.map(item => Math.abs(item.endingBalance))
      const maxBalance = Math.max(...balances)
      const minBalance = Math.min(...balances)
      const avgBalance = balances.reduce((sum, val) => sum + val, 0) / balances.length
      
      const maxIndex = balances.indexOf(maxBalance)
      const minIndex = balances.indexOf(minBalance)
      
      const firstBalance = this.trendData[0].endingBalance
      const lastBalance = this.trendData[this.trendData.length - 1].endingBalance
      const trend = firstBalance !== 0 ? ((lastBalance - firstBalance) / Math.abs(firstBalance) * 100).toFixed(2) : 0
      
      this.analysis = {
        maxBalance,
        maxBalancePeriod: this.trendData[maxIndex].period,
        minBalance,
        minBalancePeriod: this.trendData[minIndex].period,
        avgBalance,
        trend: parseFloat(trend)
      }
    },
    updateChart() {
      if (!this.trendChart) return
      
      const periods = this.trendData.map(item => item.period)
      const balances = this.trendData.map(item => (item.endingBalance / 10000).toFixed(2))
      const debits = this.trendData.map(item => (item.currentDebit / 10000).toFixed(2))
      const credits = this.trendData.map(item => (item.currentCredit / 10000).toFixed(2))
      
      const option = {
        title: {
          text: '科目余额趋势图',
          left: 'center'
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          }
        },
        legend: {
          data: ['期末余额', '本期借方', '本期贷方'],
          top: 30
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          top: '15%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: periods
        },
        yAxis: {
          type: 'value',
          name: '金额(万元)'
        },
        series: [
          {
            name: '期末余额',
            type: 'line',
            data: balances,
            smooth: true,
            itemStyle: { color: '#409eff' }
          },
          {
            name: '本期借方',
            type: 'bar',
            data: debits,
            itemStyle: { color: '#67c23a' }
          },
          {
            name: '本期贷方',
            type: 'bar',
            data: credits,
            itemStyle: { color: '#e6a23c' }
          }
        ]
      }
      
      this.trendChart.setOption(option)
    },
    exportTrendData() {
      try {
        const data = this.trendData || []
        if (data.length === 0) {
          this.$message.warning('暂无数据可导出')
          return
        }
        const jsonStr = JSON.stringify(data, null, 2)
        const blob = new Blob([jsonStr], { type: 'application/json' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '余额趋势数据导出.json'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('导出成功')
      } catch (error) {
        this.$message.error('导出失败')
      }
    },
    handleClose() {
      this.dialogVisible = false
    },
    formatAmount(amount) {
      if (amount >= 10000) {
        return (amount / 10000).toFixed(2) + '万'
      }
      return amount.toLocaleString()
    }
  }
}
</script>

<style lang="scss" scoped>
.trend-dialog-content {
  .subject-info {
    margin-bottom: 20px;
    padding: 16px;
    background: #f8f9fa;
    border-radius: 8px;

    .info-item {
      font-size: 14px;
      margin-bottom: 8px;

      label {
        color: #606266;
        margin-right: 8px;
      }

      .debit-amount {
        color: #409eff;
        font-weight: 600;
      }

      .credit-amount {
        color: #67c23a;
        font-weight: 600;
      }
    }
  }

  .query-form {
    margin-bottom: 20px;
    padding: 16px;
    background: #f8f9fa;
    border-radius: 8px;
  }

  .trend-chart {
    margin-bottom: 20px;
    padding: 16px;
    background: white;
    border: 1px solid #ebeef5;
    border-radius: 8px;
  }

  .trend-table {
    margin-bottom: 20px;

    h4 {
      margin: 0 0 16px 0;
      color: #303133;
      font-size: 14px;
      font-weight: 600;
    }

    .debit-amount {
      color: #409eff;
      font-weight: 600;
    }

    .credit-amount {
      color: #67c23a;
      font-weight: 600;
    }

    .increase {
      color: #67c23a;
      font-weight: 600;
    }

    .decrease {
      color: #f56c6c;
      font-weight: 600;
    }
  }

  .trend-analysis {
    h4 {
      margin: 0 0 16px 0;
      color: #303133;
      font-size: 14px;
      font-weight: 600;
    }

    .analysis-item {
      text-align: center;
      padding: 16px;
      background: #f8f9fa;
      border-radius: 8px;

      .analysis-label {
        font-size: 12px;
        color: #909399;
        margin-bottom: 8px;
      }

      .analysis-value {
        font-size: 18px;
        font-weight: 600;
        color: #303133;
        margin-bottom: 4px;

        &.positive {
          color: #67c23a;
        }

        &.negative {
          color: #f56c6c;
        }
      }

      .analysis-period {
        font-size: 12px;
        color: #909399;
      }
    }
  }
}

.dialog-footer {
  text-align: right;
}
</style>
