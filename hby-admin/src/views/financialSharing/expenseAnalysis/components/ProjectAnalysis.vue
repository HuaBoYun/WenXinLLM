<template>
  <div class="project-analysis">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>项目费用分析</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="exportData">导出</el-button>
      </div>
      
      <!-- 统计卡片 -->
      <el-row :gutter="20" class="stats-cards">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-value">{{ projectStats.totalAmount | currency }}</div>
            <div class="stat-label">总项目费用</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-value">{{ projectStats.projectCount }}</div>
            <div class="stat-label">项目数量</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-value">{{ projectStats.avgAmount | currency }}</div>
            <div class="stat-label">平均费用</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-value">{{ projectStats.completionRate }}%</div>
            <div class="stat-label">完成率</div>
          </div>
        </el-col>
      </el-row>

      <!-- 图表区域 -->
      <el-row :gutter="20" class="chart-row">
        <el-col :span="12">
          <div class="chart-container">
            <h4>项目费用分布</h4>
            <div ref="projectChart" class="chart"></div>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="chart-container">
            <h4>项目状态分析</h4>
            <div ref="statusChart" class="chart"></div>
          </div>
        </el-col>
      </el-row>

      <el-row :gutter="20" class="chart-row">
        <el-col :span="24">
          <div class="chart-container">
            <h4>项目预算执行情况</h4>
            <div ref="budgetChart" class="chart"></div>
          </div>
        </el-col>
      </el-row>

      <!-- 详细数据表格 -->
      <div class="table-container">
        <h4>项目明细</h4>
        <el-table :data="projectData" border style="width: 100%">
          <el-table-column prop="projectName" label="项目名称" width="150"></el-table-column>
          <el-table-column prop="projectManager" label="项目经理" width="100"></el-table-column>
          <el-table-column prop="startDate" label="开始日期" width="120"></el-table-column>
          <el-table-column prop="endDate" label="结束日期" width="120"></el-table-column>
          <el-table-column prop="budgetAmount" label="预算金额" width="120">
            <template slot-scope="scope">
              {{ scope.row.budgetAmount | currency }}
            </template>
          </el-table-column>
          <el-table-column prop="actualAmount" label="实际费用" width="120">
            <template slot-scope="scope">
              {{ scope.row.actualAmount | currency }}
            </template>
          </el-table-column>
          <el-table-column prop="budgetUsage" label="预算使用率" width="120">
            <template slot-scope="scope">
              <el-progress :percentage="scope.row.budgetUsage" :color="getProgressColor(scope.row.budgetUsage)"></el-progress>
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
  name: 'ProjectAnalysis',
  data() {
    return {
      projectStats: {
        totalAmount: 0,
        projectCount: 0,
        avgAmount: 0,
        completionRate: 0
      },
      projectData: [],
      projectChart: null,
      statusChart: null,
      budgetChart: null
    }
  },
  mounted() {
    this.loadData()
    this.initCharts()
  },
  beforeDestroy() {
    if (this.projectChart) this.projectChart.dispose()
    if (this.statusChart) this.statusChart.dispose()
    if (this.budgetChart) this.budgetChart.dispose()
  },
  methods: {
    loadData() {
      expenseAnalysisApi.getProjectAnalysis().then(res => {
        if (res.code === 1 && res.data) {
          this.projectStats = res.data.stats || {}
          this.projectData = res.data.list || []
        }
      }).catch(error => {
        console.error('加载项目费用分析失败:', error)
      })
    },
    initCharts() {
      this.$nextTick(() => {
        this.initProjectChart()
        this.initStatusChart()
        this.initBudgetChart()
      })
    },
    initProjectChart() {
      this.projectChart = echarts.init(this.$refs.projectChart)
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        xAxis: {
          type: 'category',
          data: this.projectData.map(item => item.projectName),
          axisLabel: {
            rotate: 45,
            interval: 0
          }
        },
        yAxis: {
          type: 'value',
          name: '费用金额(元)'
        },
        series: [{
          name: '实际费用',
          data: this.projectData.map(item => item.actualAmount),
          type: 'bar',
          itemStyle: {
            color: '#409EFF'
          }
        }]
      }
      this.projectChart.setOption(option)
    },
    initStatusChart() {
      this.statusChart = echarts.init(this.$refs.statusChart)
      const data = [
        { value: 4, name: '进行中' },
        { value: 2, name: '已完成' },
        { value: 1, name: '已暂停' },
        { value: 1, name: '已取消' }
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
          name: '项目状态',
          type: 'pie',
          radius: '50%',
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
      this.statusChart.setOption(option)
    },
    initBudgetChart() {
      this.budgetChart = echarts.init(this.$refs.budgetChart)
      const option = {
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['预算金额', '实际费用']
        },
        xAxis: {
          type: 'category',
          data: this.projectData.map(item => item.projectName),
          axisLabel: {
            rotate: 45,
            interval: 0
          }
        },
        yAxis: {
          type: 'value',
          name: '金额(元)'
        },
        series: [
          {
            name: '预算金额',
            type: 'bar',
            data: this.projectData.map(item => item.budgetAmount),
            itemStyle: {
              color: '#E6A23C'
            }
          },
          {
            name: '实际费用',
            type: 'bar',
            data: this.projectData.map(item => item.actualAmount),
            itemStyle: {
              color: '#409EFF'
            }
          }
        ]
      }
      this.budgetChart.setOption(option)
    },
    getProgressColor(percentage) {
      if (percentage < 70) return '#67C23A'
      if (percentage < 90) return '#E6A23C'
      return '#F56C6C'
    },
    getStatusType(status) {
      const statusMap = {
        '进行中': 'primary',
        '已完成': 'success',
        '已暂停': 'warning',
        '已取消': 'danger'
      }
      return statusMap[status] || 'info'
    },
    viewDetail(row) {
      this.$message.info(`查看项目${row.projectName}的详情`)
    },
    exportData() {
      expenseAnalysisApi.exportReport({ type: 'project' }).then(res => {
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
.project-analysis {
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
