<template>
  <div class="organization-analysis">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>组织费用分析</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="exportData">导出</el-button>
      </div>
      
      <!-- 统计卡片 -->
      <el-row :gutter="20" class="stats-cards">
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-value">{{ organizationStats.totalAmount | currency }}</div>
            <div class="stat-label">总费用</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-value">{{ organizationStats.departmentCount }}</div>
            <div class="stat-label">部门数量</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-value">{{ organizationStats.avgAmount | currency }}</div>
            <div class="stat-label">平均费用</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-card">
            <div class="stat-value">{{ organizationStats.maxAmount | currency }}</div>
            <div class="stat-label">最高费用</div>
          </div>
        </el-col>
      </el-row>

      <!-- 图表区域 -->
      <el-row :gutter="20" class="chart-row">
        <el-col :span="12">
          <div class="chart-container">
            <h4>部门费用分布</h4>
            <div ref="departmentChart" class="chart"></div>
          </div>
        </el-col>
        <el-col :span="12">
          <div class="chart-container">
            <h4>费用类型分析</h4>
            <div ref="typeChart" class="chart"></div>
          </div>
        </el-col>
      </el-row>

      <!-- 详细数据表格 -->
      <div class="table-container">
        <h4>详细数据</h4>
        <el-table :data="organizationData" border style="width: 100%">
          <el-table-column prop="departmentName" label="部门名称" width="150"></el-table-column>
          <el-table-column prop="totalAmount" label="总费用" width="120">
            <template slot-scope="scope">
              {{ scope.row.totalAmount | currency }}
            </template>
          </el-table-column>
          <el-table-column prop="expenseCount" label="费用笔数" width="100"></el-table-column>
          <el-table-column prop="avgAmount" label="平均费用" width="120">
            <template slot-scope="scope">
              {{ scope.row.avgAmount | currency }}
            </template>
          </el-table-column>
          <el-table-column prop="budgetAmount" label="预算金额" width="120">
            <template slot-scope="scope">
              {{ scope.row.budgetAmount | currency }}
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
        </el-table>
      </div>
    </el-card>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { expenseAnalysisApi } from '@/api/financialSharing/advancedFeatures'

export default {
  name: 'OrganizationAnalysis',
  data() {
    return {
      organizationStats: {
        totalAmount: 0,
        departmentCount: 0,
        avgAmount: 0,
        maxAmount: 0
      },
      organizationData: [],
      departmentChart: null,
      typeChart: null
    }
  },
  mounted() {
    this.loadData()
    this.initCharts()
  },
  beforeDestroy() {
    if (this.departmentChart) {
      this.departmentChart.dispose()
    }
    if (this.typeChart) {
      this.typeChart.dispose()
    }
  },
  methods: {
    loadData() {
      expenseAnalysisApi.getOrganizationAnalysis().then(res => {
        if (res.code === 1 && res.data) {
          this.organizationStats = res.data.stats || {}
          this.organizationData = res.data.list || []
        }
      }).catch(error => {
        console.error('加载组织费用分析失败:', error)
      })
    },
    initCharts() {
      this.$nextTick(() => {
        this.initDepartmentChart()
        this.initTypeChart()
      })
    },
    initDepartmentChart() {
      this.departmentChart = echarts.init(this.$refs.departmentChart)
      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        xAxis: {
          type: 'category',
          data: this.organizationData.map(item => item.departmentName),
          axisLabel: {
            rotate: 45
          }
        },
        yAxis: {
          type: 'value',
          name: '费用金额(元)'
        },
        series: [{
          data: this.organizationData.map(item => item.totalAmount),
          type: 'bar',
          itemStyle: {
            color: '#409EFF'
          }
        }]
      }
      this.departmentChart.setOption(option)
    },
    initTypeChart() {
      this.typeChart = echarts.init(this.$refs.typeChart)
      const data = [
        { value: 450000, name: '差旅费' },
        { value: 320000, name: '办公费' },
        { value: 280000, name: '培训费' },
        { value: 200000, name: '招待费' }
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
      this.typeChart.setOption(option)
    },
    getProgressColor(percentage) {
      if (percentage < 70) return '#67C23A'
      if (percentage < 90) return '#E6A23C'
      return '#F56C6C'
    },
    getStatusType(status) {
      const statusMap = {
        '正常': 'success',
        '预警': 'warning',
        '超支': 'danger'
      }
      return statusMap[status] || 'info'
    },
        exportData() {
      expenseAnalysisApi.exportReport({ type: 'organization' }).then(res => {
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
.organization-analysis {
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
