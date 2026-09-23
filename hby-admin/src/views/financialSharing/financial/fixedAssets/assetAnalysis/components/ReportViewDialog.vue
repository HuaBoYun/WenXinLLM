<template>
  <el-dialog
    :title="reportData.title"
    :visible.sync="visible"
    width="1200px"
    :close-on-click-modal="false"
    @close="handleClose"
  >
    <div class="report-view-container" v-loading="loading">
      <!-- 报表头部信息 -->
      <div class="report-header">
        <el-descriptions :column="3" border size="small">
          <el-descriptions-item label="报表编号">{{ reportData.reportNumber }}</el-descriptions-item>
          <el-descriptions-item label="报表类型">{{ reportData.reportType }}</el-descriptions-item>
          <el-descriptions-item label="生成时间">{{ reportData.generateTime }}</el-descriptions-item>
          <el-descriptions-item label="统计期间">{{ reportData.period }}</el-descriptions-item>
          <el-descriptions-item label="生成人">{{ reportData.creator }}</el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="reportData.status === 'ready' ? 'success' : 'info'" size="small">
              {{ reportData.status === 'ready' ? '已生成' : '生成中' }}
            </el-tag>
          </el-descriptions-item>
        </el-descriptions>
      </div>

      <!-- 报表内容 -->
      <div class="report-content">
        <!-- 汇总数据 -->
        <div class="summary-section" v-if="summaryData">
          <h4>汇总数据</h4>
          <el-row :gutter="20">
            <el-col :span="6" v-for="(item, index) in summaryData" :key="index">
              <div class="summary-card">
                <div class="summary-label">{{ item.label }}</div>
                <div class="summary-value">{{ item.value }}</div>
              </div>
            </el-col>
          </el-row>
        </div>

        <!-- 详细数据表格 -->
        <div class="detail-section">
          <h4>详细数据</h4>
          <el-table
            :data="detailData"
            border
            stripe
            max-height="400"
            show-summary
            :summary-method="getSummaries"
          >
            <el-table-column type="index" label="序号" width="60" align="center" />
            <el-table-column
              v-for="column in tableColumns"
              :key="column.prop"
              :prop="column.prop"
              :label="column.label"
              :width="column.width"
              :min-width="column.minWidth"
              :align="column.align || 'left'"
              :show-overflow-tooltip="column.showOverflowTooltip"
            >
              <template slot-scope="scope">
                <span v-if="column.type === 'amount'" class="amount">
                  {{ formatAmount(scope.row[column.prop]) }}
                </span>
                <span v-else-if="column.type === 'percent'">
                  {{ scope.row[column.prop] }}%
                </span>
                <span v-else>{{ scope.row[column.prop] }}</span>
              </template>
            </el-table-column>
          </el-table>
        </div>

        <!-- 图表展示 -->
        <div class="chart-section" v-if="showChart">
          <h4>数据分析图表</h4>
          <div id="reportChart" style="width: 100%; height: 400px;"></div>
        </div>
      </div>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button @click="handleClose">关闭</el-button>
      <el-button type="primary" @click="handlePrint">打印</el-button>
      <el-button type="success" @click="handleExport">导出</el-button>
    </div>
  </el-dialog>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'ReportViewDialog',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    reportData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      loading: false,
      showChart: true,
      chart: null,
      summaryData: [],
      detailData: [],
      tableColumns: []
    }
  },
  watch: {
    visible(val) {
      if (val && this.reportData) {
        this.loadReportData()
      }
    }
  },
  beforeDestroy() {
    if (this.chart) {
      this.chart.dispose()
    }
  },
  methods: {
    async loadReportData() {
      this.loading = true
      try {
        // 暂未对接 API，先以空状态展示，待后端接口提供后接入
        this.summaryData = []
        this.tableColumns = [
          { prop: 'categoryName', label: '资产类别', width: '120' },
          { prop: 'assetCount', label: '资产数量', width: '100', align: 'center' },
          { prop: 'originalValue', label: '原值', width: '150', align: 'right', type: 'amount' },
          { prop: 'accumulatedDepreciation', label: '累计折旧', width: '150', align: 'right', type: 'amount' },
          { prop: 'netValue', label: '净值', width: '150', align: 'right', type: 'amount' },
          { prop: 'depreciationRate', label: '折旧率', width: '100', align: 'center', type: 'percent' }
        ]
        this.detailData = []

        this.$nextTick(() => {
          this.initChart()
        })
      } catch (error) {
        this.$message.error('加载报表数据失败')
      } finally {
        this.loading = false
      }
    },
    initChart() {
      const chartDom = document.getElementById('reportChart')
      if (!chartDom) return

      this.chart = echarts.init(chartDom)
      
      const option = {
        title: {
          text: '资产类别价值分布',
          left: 'center'
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: { type: 'shadow' }
        },
        legend: {
          data: ['原值', '累计折旧', '净值'],
          top: 30
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: this.detailData.map(item => item.categoryName)
        },
        yAxis: {
          type: 'value',
          axisLabel: {
            formatter: value => '¥' + (value / 10000).toFixed(0) + '万'
          }
        },
        series: [
          {
            name: '原值',
            type: 'bar',
            data: this.detailData.map(item => item.originalValue),
            itemStyle: { color: '#409EFF' }
          },
          {
            name: '累计折旧',
            type: 'bar',
            data: this.detailData.map(item => item.accumulatedDepreciation),
            itemStyle: { color: '#E6A23C' }
          },
          {
            name: '净值',
            type: 'bar',
            data: this.detailData.map(item => item.netValue),
            itemStyle: { color: '#67C23A' }
          }
        ]
      }
      
      this.chart.setOption(option)
    },
    getSummaries(param) {
      const { columns, data } = param
      const sums = []
      columns.forEach((column, index) => {
        if (index === 0) {
          sums[index] = '合计'
          return
        }
        const values = data.map(item => Number(item[column.property]))
        if (!values.every(value => isNaN(value))) {
          const sum = values.reduce((prev, curr) => {
            const value = Number(curr)
            if (!isNaN(value)) {
              return prev + curr
            } else {
              return prev
            }
          }, 0)
          
          if (column.property === 'assetCount') {
            sums[index] = sum
          } else if (['originalValue', 'accumulatedDepreciation', 'netValue'].includes(column.property)) {
            sums[index] = this.formatAmount(sum)
          } else if (column.property === 'depreciationRate') {
            sums[index] = (sum / data.length).toFixed(1) + '%'
          } else {
            sums[index] = ''
          }
        } else {
          sums[index] = ''
        }
      })
      return sums
    },
    handlePrint() {
      window.print()
    },
    handleExport() {
      try {
        const data = this.reportData || {}
        const jsonStr = JSON.stringify(data, null, 2)
        const blob = new Blob([jsonStr], { type: 'application/json' })
        const url = window.URL.createObjectURL(blob)
        const link = document.createElement('a')
        link.href = url
        link.download = '资产分析报表.json'
        link.click()
        window.URL.revokeObjectURL(url)
        this.$message.success('报表导出成功')
      } catch (error) {
        this.$message.error('导出失败')
      }
    },
    handleClose() {
      if (this.chart) {
        this.chart.dispose()
        this.chart = null
      }
      this.$emit('update:visible', false)
    },
    formatAmount(amount) {
      return amount ? '¥' + Number(amount).toLocaleString('zh-CN', { minimumFractionDigits: 2 }) : '¥0.00'
    }
  }
}
</script>

<style lang="scss" scoped>
.report-view-container {
  padding: 10px 0;
}

.report-header {
  margin-bottom: 20px;
}

.report-content {
  .summary-section {
    margin-bottom: 20px;

    h4 {
      margin: 0 0 15px 0;
      color: #303133;
      font-size: 16px;
    }

    .summary-card {
      background: #f5f7fa;
      padding: 15px;
      border-radius: 4px;
      text-align: center;

      .summary-label {
        font-size: 14px;
        color: #909399;
        margin-bottom: 8px;
      }

      .summary-value {
        font-size: 20px;
        font-weight: 600;
        color: #303133;
      }
    }
  }

  .detail-section,
  .chart-section {
    margin-bottom: 20px;

    h4 {
      margin: 0 0 15px 0;
      color: #303133;
      font-size: 16px;
    }
  }
}

.amount {
  font-weight: 600;
  color: #303133;
}
</style>

