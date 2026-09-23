<template>
  <div class="travel-analysis">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>差旅费用分析</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="exportData">导出</el-button>
      </div>
      
      <!-- 统计卡片 -->
      <el-row :gutter="20" class="stats-cards">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-value">{{ travelStats.totalAmount | currency }}</div>
            <div class="stat-label">总差旅费</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-value">{{ travelStats.tripCount }}</div>
            <div class="stat-label">出差次数</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-value">{{ travelStats.avgAmount | currency }}</div>
            <div class="stat-label">平均费用</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-value">{{ travelStats.avgDays }}</div>
            <div class="stat-label">平均天数</div>
          </div>
        </el-col>
      </el-row>

      <!-- 图表区域 -->
      <el-row :gutter="20" class="chart-row">
        <el-col :span="12">
          <div class="chart-container">
            <h4>差旅费用趋势</h4>
            <div ref="trendChart" class="chart"></div>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="chart-container">
            <h4>费用类型分布</h4>
            <div ref="typeChart" class="chart"></div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="20" class="chart-row">
        <el-col :span="12">
          <div class="chart-container">
            <h4>目的地分析</h4>
            <div ref="destinationChart" class="chart"></div>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="chart-container">
            <h4>交通工具分析</h4>
            <div ref="transportChart" class="chart"></div>
          </div>
        </el-col>
      </el-row>

      <!-- 详细数据表格 -->
      <div class="table-container">
        <h4>差旅明细</h4>
        <el-table :data="travelData" border style="width: 100%">
          <el-table-column prop="employeeName" label="员工姓名" width="100"></el-table-column>
          <el-table-column prop="destination" label="目的地" width="120"></el-table-column>
          <el-table-column prop="startDate" label="开始日期" width="120"></el-table-column>
          <el-table-column prop="endDate" label="结束日期" width="120"></el-table-column>
          <el-table-column prop="days" label="天数" width="80"></el-table-column>
          <el-table-column prop="transport" label="交通工具" width="100"></el-table-column>
          <el-table-column prop="totalAmount" label="总费用" width="120">
            <template slot-scope="scope">
              {{ scope.row.totalAmount | currency }}
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template slot-scope="scope">
              <el-tag :type="getStatusType(scope.row.status)">{{ scope.row.status }}</el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="120">
            <template slot-scope="scope">
              <el-button size="mini" @click="viewDetail(scope.row)">详情</el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-card>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { expenseAnalysisApi } from '@/api/financialSharing/advancedFeatures'

export default {
  name: 'TravelAnalysis',
  data() {
    return {
      travelStats: {
        totalAmount: 0,
        tripCount: 0,
        avgAmount: 0,
        avgDays: 0
      },
      travelData: [],
      trendChart: null,
      typeChart: null,
      destinationChart: null,
      transportChart: null
    }
  },
  mounted() {
    this.loadData()
    this.initCharts()
  },
  beforeDestroy() {
    if (this.trendChart) this.trendChart.dispose()
    if (this.typeChart) this.typeChart.dispose()
    if (this.destinationChart) this.destinationChart.dispose()
    if (this.transportChart) this.transportChart.dispose()
  },
  methods: {
    loadData() {
      expenseAnalysisApi.getTravelAnalysis().then(res => {
        if (res.code === 1 && res.data) {
          this.travelStats = res.data.stats || {}
          this.travelData = res.data.list || []
        }
      }).catch(error => {
        console.error('加载差旅费用分析失败:', error)
      })
    },
    initCharts() {
      this.$nextTick(() => {
        this.initTrendChart()
        this.initTypeChart()
        this.initDestinationChart()
        this.initTransportChart()
      })
    },
    initTrendChart() {
      this.trendChart = echarts.init(this.$refs.trendChart)
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        xAxis: {
          type: 'category',
          data: ['1月', '2月', '3月', '4月', '5月', '6月']
        },
        yAxis: {
          type: 'value',
          name: '费用金额(元)'
        },
        series: [{
          data: [85000, 92000, 78000, 88000, 95000, 102000],
          type: 'line',
          smooth: true,
          itemStyle: {
            color: '#409EFF'
          }
        }]
      }
      this.trendChart.setOption(option)
    },
    initTypeChart() {
      this.typeChart = echarts.init(this.$refs.typeChart)
      const data = [
        { value: 180000, name: '交通费' },
        { value: 150000, name: '住宿费' },
        { value: 80000, name: '餐费' },
        { value: 40000, name: '其他' }
      ]
      const option = {
        tooltip: {
          trigger: 'item'
        },
        legend: {
          orient: 'vertical',
          left: 'left'
        },
        series: [{
          name: '费用类型',
          type: 'pie',
          radius: '50%',
          data: data
        }]
      }
      this.typeChart.setOption(option)
    },
    initDestinationChart() {
      this.destinationChart = echarts.init(this.$refs.destinationChart)
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        xAxis: {
          type: 'category',
          data: ['北京', '上海', '广州', '深圳', '杭州', '南京']
        },
        yAxis: {
          type: 'value',
          name: '次数'
        },
        series: [{
          data: [25, 20, 15, 12, 8, 5],
          type: 'bar',
          itemStyle: {
            color: '#67C23A'
          }
        }]
      }
      this.destinationChart.setOption(option)
    },
    initTransportChart() {
      this.transportChart = echarts.init(this.$refs.transportChart)
      const data = [
        { value: 45, name: '飞机' },
        { value: 30, name: '高铁' },
        { value: 8, name: '汽车' },
        { value: 2, name: '其他' }
      ]
      const option = {
        tooltip: {
          trigger: 'item'
        },
        series: [{
          name: '交通工具',
          type: 'pie',
          radius: ['40%', '70%'],
          data: data
        }]
      }
      this.transportChart.setOption(option)
    },
    getStatusType(status) {
      const statusMap = {
        '已报销': 'success',
        '审批中': 'warning',
        '已拒绝': 'danger',
        '草稿': 'info'
      }
      return statusMap[status] || 'info'
    },
    viewDetail(row) {
      this.$message.info(`查看${row.employeeName}的差旅详情`)
    },
    exportData() {
      expenseAnalysisApi.exportReport({ type: 'travel' }).then(res => {
        if (res.code === 1) {
          this.$message.success('导出成功')
          if (res.data && res.data.url) {
            window.open(res.data.url)
          }
        } else {
          this.$message.error(res.msg || '导出失败')
        }
      }).catch(() => {
        this.$message.error('导出失败')
      })
    }
  },
  filters: {
    currency(value) {
      if (!value) return '¥0.00'
      return '¥' + value.toLocaleString('zh-CN', { minimumFractionDigits: 2 })
    }
  }
}
</script>

<style scoped>
.travel-analysis {
  padding: 20px;
}

.stats-cards {
  margin-bottom: 20px;
}

.stat-card {
  text-align: center;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 8px;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #409EFF;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 14px;
  color: #666;
}

.chart-row {
  margin-bottom: 20px;
}

.chart-container {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
}

.chart {
  width: 100%;
  height: 300px;
}

.table-container {
  margin-top: 20px;
}

.table-container h4 {
  margin-bottom: 15px;
  color: #333;
}
</style>
