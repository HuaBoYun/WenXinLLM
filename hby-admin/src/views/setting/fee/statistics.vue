<template>
  <div class="fee-statistics-container">
    <!-- 维度切换 -->
    <el-tabs v-model="dimension" @tab-click="handleSearch">
      <el-tab-pane label="个人维度" name="personal" />
      <el-tab-pane label="公司维度" name="company" />
      <el-tab-pane label="集团维度" name="group" />
    </el-tabs>

    <!-- 筛选条件 -->
    <div class="filter-bar">
      <el-date-picker v-model="dateRange" type="daterange" range-separator="至" start-placeholder="开始日期" end-placeholder="结束日期" value-format="yyyy-MM-dd" style="margin-right: 10px;" />
      <el-button type="primary" icon="el-icon-search" @click="handleSearch">查询</el-button>
    </div>

    <!-- 汇总 -->
    <el-row :gutter="20" style="margin: 15px 0;">
      <el-col :span="12">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-label">总费用</div>
            <div class="stat-value">¥ {{ statistics.totalFee || '0.00' }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
          <div class="stat-card">
            <div class="stat-label">总调用次数</div>
            <div class="stat-value">{{ statistics.totalCallCount || 0 }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 统计图 -->
    <div style="margin: 15px 0;">
      <el-radio-group v-model="chartType" size="small">
        <el-radio-button label="pie">扇形</el-radio-button>
        <el-radio-button label="line">折线</el-radio-button>
        <el-radio-button label="bar">柱形</el-radio-button>
      </el-radio-group>
      <div ref="chart" style="width: 100%; height: 350px;"></div>
    </div>

    <!-- 模块统计表格 -->
    <el-table v-loading="loading" :data="moduleStats" border style="width: 100%;" @row-click="handleDrilldown">
      <el-table-column prop="moduleName" label="模块名称" min-width="200" />
      <el-table-column prop="totalFee" label="费用合计(元)" width="150" align="right" />
      <el-table-column prop="callCount" label="调用次数" width="120" align="center" />
      <el-table-column label="操作" width="100" align="center">
        <template slot-scope="{}">
          <el-button type="text" size="small">查看明细</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 穿透查询弹窗 -->
    <el-dialog :title="'模块明细 - ' + drilldownTitle" :visible.sync="drilldownVisible" width="700px">
      <el-table v-loading="drilldownLoading" :data="drilldownList" border>
        <el-table-column prop="RIGHT_NAME" label="小模块名称" min-width="200" />
        <el-table-column prop="TOTAL_FEE" label="费用合计(元)" width="150" align="right" />
        <el-table-column prop="CALL_COUNT" label="调用次数" width="120" align="center" />
      </el-table>
    </el-dialog>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { queryFeeStatistics, drilldownFeeStatistics } from '@/api/setting/fee'

export default {
  name: 'FeeStatistics',
  data() {
    return {
      loading: false,
      dimension: 'personal',
      dateRange: [],
      statistics: { totalFee: '0.00', totalCallCount: 0 },
      moduleStats: [],
      chartType: 'pie',
      chartInstance: null,
      drilldownVisible: false,
      drilldownLoading: false,
      drilldownTitle: '',
      drilldownList: [],
    }
  },
  watch: {
    chartType() {
      this.renderChart()
    },
    moduleStats() {
      this.renderChart()
    },
  },
  created() {
    this.handleSearch()
  },
  beforeDestroy() {
    if (this.chartInstance) {
      this.chartInstance.dispose()
      this.chartInstance = null
    }
  },
  methods: {
    async handleSearch() {
      this.loading = true
      const params = { dimension: this.dimension, startTime: null, endTime: null }
      if (this.dateRange && this.dateRange.length === 2) {
        params.startTime = this.dateRange[0] + ' 00:00:00'
        params.endTime = this.dateRange[1] + ' 23:59:59'
      }
      try {
        const res = await queryFeeStatistics(params)
        if (res.code === 1 || res.code === 200) {
          const data = (res.data && res.data.statistics) || {}
          this.statistics = {
            totalFee: data.totalFee != null ? Number(data.totalFee).toFixed(2) : '0.00',
            totalCallCount: data.totalCallCount || 0,
          }
          this.moduleStats = data.moduleStats || []
        }
      } catch (e) {
        console.error(e)
      }
      this.loading = false
      this.$nextTick(() => { this.renderChart() })
    },
    renderChart() {
      if (!this.$refs.chart) return
      if (!this.chartInstance) {
        this.chartInstance = echarts.init(this.$refs.chart)
      }
      const names = this.moduleStats.map(i => i.moduleName)
      const values = this.moduleStats.map(i => Number(i.totalFee))
      let option = {}
      if (this.chartType === 'pie') {
        option = {
          tooltip: { trigger: 'item', formatter: '{b}: ¥{c} ({d}%)' },
          legend: { bottom: 0, data: names },
          series: [{
            type: 'pie',
            radius: '55%',
            center: ['50%', '45%'],
            data: names.map((n, i) => ({ name: n, value: values[i] })),
            label: { formatter: '{b}: ¥{c}' },
          }],
        }
      } else {
        option = {
          tooltip: { trigger: 'axis' },
          xAxis: { type: 'category', data: names, axisLabel: { rotate: 30 } },
          yAxis: { type: 'value', name: '费用(元)' },
          series: [{ type: this.chartType, data: values, smooth: true }],
        }
      }
      this.chartInstance.setOption(option, true)
    },
    async handleDrilldown(row) {
      this.drilldownTitle = row.moduleName || ''
      this.drilldownVisible = true
      this.drilldownLoading = true
      const params = {
        moduleType: row.moduleType,
        dimension: this.dimension,
        startTime: null,
        endTime: null,
      }
      if (this.dateRange && this.dateRange.length === 2) {
        params.startTime = this.dateRange[0] + ' 00:00:00'
        params.endTime = this.dateRange[1] + ' 23:59:59'
      }
      try {
        const res = await drilldownFeeStatistics(params)
        if (res.code === 1 || res.code === 200) {
          this.drilldownList = (res.data && res.data.list) || []
        }
      } catch (e) {
        console.error(e)
      }
      this.drilldownLoading = false
    },
  },
}
</script>

<style lang="scss" scoped>
.fee-statistics-container {
  padding: 20px;
}
.filter-bar {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}
.stat-card {
  text-align: center;
}
.stat-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 8px;
}
.stat-value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}
</style>
