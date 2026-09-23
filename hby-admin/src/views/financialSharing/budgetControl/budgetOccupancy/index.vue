<template>
  <div class="app-container">
    <!-- 统计卡片 -->
    <el-row :gutter="20" class="statistics-row">
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background-color: #409EFF;">
              <i class="el-icon-s-data" />
            </div>
            <div class="stat-info">
              <div class="stat-label">总预算</div>
              <div class="stat-value">{{ formatAmount(statistics.totalBudget) }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background-color: #E6A23C;">
              <i class="el-icon-s-finance" />
            </div>
            <div class="stat-info">
              <div class="stat-label">已占用</div>
              <div class="stat-value">{{ formatAmount(statistics.totalOccupied) }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background-color: #67C23A;">
              <i class="el-icon-s-opportunity" />
            </div>
            <div class="stat-info">
              <div class="stat-label">可用预算</div>
              <div class="stat-value">{{ formatAmount(statistics.totalAvailable) }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background-color: #F56C6C;">
              <i class="el-icon-s-marketing" />
            </div>
            <div class="stat-info">
              <div class="stat-label">平均占用率</div>
              <div class="stat-value">{{ statistics.avgOccupancyRate }}%</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 查询表单 -->
    <el-form :inline="true" :model="queryForm" class="query-form">
      <el-form-item label="组织编码">
        <el-input v-model="queryForm.orgCode" placeholder="请输入组织编码" clearable style="width: 180px" />
      </el-form-item>
      <el-form-item label="组织名称">
        <el-input v-model="queryForm.orgName" placeholder="请输入组织名称" clearable style="width: 180px" />
      </el-form-item>
      <el-form-item label="科目编码">
        <el-input v-model="queryForm.subjectCode" placeholder="请输入科目编码" clearable style="width: 180px" />
      </el-form-item>
      <el-form-item label="科目名称">
        <el-input v-model="queryForm.subjectName" placeholder="请输入科目名称" clearable style="width: 180px" />
      </el-form-item>
      <el-form-item label="期间">
        <el-input v-model="queryForm.period" placeholder="如202601" clearable style="width: 150px" />
      </el-form-item>
      <el-form-item label="预算年度">
        <el-input v-model="queryForm.budgetYear" placeholder="如2026" clearable style="width: 120px" />
      </el-form-item>
      <el-form-item label="状态">
        <el-select v-model="queryForm.status" placeholder="请选择状态" clearable style="width: 150px">
          <el-option label="正常" value="NORMAL" />
          <el-option label="预警" value="WARNING" />
          <el-option label="超支" value="EXCEEDED" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 数据表格 -->
    <el-table v-loading="loading" :data="dataList" border>
      <el-table-column label="组织编码" prop="orgCode" width="120" />
      <el-table-column label="组织名称" prop="orgName" width="180" />
      <el-table-column label="科目编码" prop="subjectCode" width="120" />
      <el-table-column label="科目名称" prop="subjectName" width="180" />
      <el-table-column label="期间" prop="period" width="100" />
      <el-table-column label="预算金额" prop="budgetAmount" width="130" align="right">
        <template slot-scope="scope">
          {{ formatAmount(scope.row.budgetAmount) }}
        </template>
      </el-table-column>
      <el-table-column label="调整金额" prop="adjustedAmount" width="130" align="right">
        <template slot-scope="scope">
          {{ formatAmount(scope.row.adjustedAmount) }}
        </template>
      </el-table-column>
      <el-table-column label="占用金额" prop="occupiedAmount" width="130" align="right">
        <template slot-scope="scope">
          <span style="color: #E6A23C; font-weight: bold;">
            {{ formatAmount(scope.row.occupiedAmount) }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="可用金额" prop="availableAmount" width="130" align="right">
        <template slot-scope="scope">
          <span :style="{ color: scope.row.availableAmount < 0 ? '#F56C6C' : '#67C23A', fontWeight: 'bold' }">
            {{ formatAmount(scope.row.availableAmount) }}
          </span>
        </template>
      </el-table-column>
      <el-table-column label="占用率" width="100" align="center">
        <template slot-scope="scope">
          <el-progress
            :percentage="calculateOccupancyRate(scope.row)"
            :color="getProgressColor(calculateOccupancyRate(scope.row))"
            :format="() => calculateOccupancyRate(scope.row) + '%'"
          />
        </template>
      </el-table-column>
      <el-table-column label="状态" prop="status" width="100" align="center">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 'NORMAL'" type="success">正常</el-tag>
          <el-tag v-else-if="scope.row.status === 'WARNING'" type="warning">预警</el-tag>
          <el-tag v-else-if="scope.row.status === 'EXCEEDED'" type="danger">超支</el-tag>
        </template>
      </el-table-column>
    </el-table>

    <!-- 分页 -->
    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryForm.pageNumber"
      :limit.sync="queryForm.pageSize"
      @pagination="getList"
    />

    <!-- 占用趋势图表 -->
    <el-card shadow="hover" style="margin-top: 20px;">
      <div slot="header">
        <span>预算占用趋势</span>
      </div>
      <div id="trendChart" style="width: 100%; height: 400px;" />
    </el-card>
  </div>
</template>

<script>
import { queryOccupancyPage, queryOccupancyStatistics, queryOccupancyTrend } from '@/api/financialSharing/budgetControl'
import Pagination from '@/components/Pagination'
import * as echarts from 'echarts'

export default {
  name: 'BudgetOccupancy',
  components: { Pagination },
  data() {
    return {
      loading: false,
      dataList: [],
      total: 0,
      queryForm: {
        orgCode: '',
        orgName: '',
        subjectCode: '',
        subjectName: '',
        period: '',
        budgetYear: '',
        status: '',
        pageNumber: 1,
        pageSize: 10
      },
      statistics: {
        totalBudget: 0,
        totalOccupied: 0,
        totalAvailable: 0,
        avgOccupancyRate: 0,
        normalCount: 0,
        warningCount: 0,
        exceededCount: 0
      },
      trendChart: null
    }
  },
  mounted() {
    this.getList()
    this.getStatistics()
    this.getTrend()
  },
  beforeDestroy() {
    if (this.trendChart) {
      this.trendChart.dispose()
    }
  },
  methods: {
    getList() {
      this.loading = true
      queryOccupancyPage(this.queryForm).then(response => {
        if (response.code === 1) {
          this.dataList = response.data.records
          this.total = response.data.total
        }
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    getStatistics() {
      queryOccupancyStatistics(this.queryForm).then(response => {
        if (response.code === 1) {
          this.statistics = response.data
        }
      })
    },
    getTrend() {
      queryOccupancyTrend(this.queryForm).then(response => {
        if (response.code === 1) {
          this.renderTrendChart(response.data)
        }
      })
    },
    handleQuery() {
      this.queryForm.pageNumber = 1
      this.getList()
      this.getStatistics()
      this.getTrend()
    },
    handleReset() {
      this.queryForm = {
        orgCode: '',
        orgName: '',
        subjectCode: '',
        subjectName: '',
        period: '',
        budgetYear: '',
        status: '',
        pageNumber: 1,
        pageSize: 10
      }
      this.getList()
      this.getStatistics()
      this.getTrend()
    },
    formatAmount(amount) {
      if (amount === null || amount === undefined) {
        return '0.00'
      }
      return Number(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    },
    calculateOccupancyRate(row) {
      const budget = row.budgetAmount || 0
      const occupied = row.occupiedAmount || 0
      if (budget === 0) {
        return 0
      }
      const rate = (occupied / budget * 100).toFixed(2)
      return parseFloat(rate)
    },
    getProgressColor(percentage) {
      if (percentage < 70) {
        return '#67C23A'
      } else if (percentage < 90) {
        return '#E6A23C'
      } else {
        return '#F56C6C'
      }
    },
    renderTrendChart(data) {
      if (!data || data.length === 0) {
        return
      }

      const chartDom = document.getElementById('trendChart')
      if (!chartDom) {
        return
      }

      if (this.trendChart) {
        this.trendChart.dispose()
      }

      this.trendChart = echarts.init(chartDom)

      const periods = data.map(item => item.period)
      const budgetAmounts = data.map(item => item.budgetAmount)
      const occupiedAmounts = data.map(item => item.occupiedAmount)
      const availableAmounts = data.map(item => item.availableAmount)
      const occupancyRates = data.map(item => item.occupancyRate)

      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          }
        },
        legend: {
          data: ['预算金额', '占用金额', '可用金额', '占用率']
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
          data: periods
        },
        yAxis: [
          {
            type: 'value',
            name: '金额',
            axisLabel: {
              formatter: '{value}'
            }
          },
          {
            type: 'value',
            name: '占用率(%)',
            axisLabel: {
              formatter: '{value}%'
            }
          }
        ],
        series: [
          {
            name: '预算金额',
            type: 'line',
            data: budgetAmounts,
            smooth: true,
            itemStyle: {
              color: '#409EFF'
            }
          },
          {
            name: '占用金额',
            type: 'line',
            data: occupiedAmounts,
            smooth: true,
            itemStyle: {
              color: '#E6A23C'
            }
          },
          {
            name: '可用金额',
            type: 'line',
            data: availableAmounts,
            smooth: true,
            itemStyle: {
              color: '#67C23A'
            }
          },
          {
            name: '占用率',
            type: 'line',
            yAxisIndex: 1,
            data: occupancyRates,
            smooth: true,
            itemStyle: {
              color: '#F56C6C'
            }
          }
        ]
      }

      this.trendChart.setOption(option)

      // 响应式调整
      window.addEventListener('resize', () => {
        if (this.trendChart) {
          this.trendChart.resize()
        }
      })
    }
  }
}
</script>

<style scoped>
.statistics-row {
  margin-bottom: 20px;
}

.stat-card {
  cursor: pointer;
  transition: all 0.3s;
}

.stat-card:hover {
  transform: translateY(-5px);
}

.stat-content {
  display: flex;
  align-items: center;
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  color: white;
  margin-right: 15px;
}

.stat-info {
  flex: 1;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 5px;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}

.query-form {
  background-color: #f5f7fa;
  padding: 15px;
  border-radius: 4px;
  margin-bottom: 20px;
}
</style>


