<template>
  <div class="app-container">
    <el-card class="box-card">
      <!-- 查询表单 -->
      <el-form :inline="true" :model="queryForm" class="demo-form-inline">
        <el-form-item label="报表任务">
          <el-select v-model="queryForm.taskId" placeholder="请选择报表任务" clearable>
            <el-option
              v-for="item in taskOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="指标">
          <el-select v-model="queryForm.indicatorId" placeholder="请选择指标" clearable>
            <el-option
              v-for="item in indicatorOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="组织ID">
          <el-input v-model="queryForm.orgId" placeholder="请输入组织ID" clearable />
        </el-form-item>
        <el-form-item label="期间">
          <el-input v-model="queryForm.period" placeholder="如:202401" clearable />
        </el-form-item>
      </el-form>

      <!-- 查询类型选择 -->
      <el-tabs v-model="activeTab" @tab-click="handleTabClick">
        <el-tab-pane label="按指标汇总" name="indicator">
          <el-button type="primary" icon="el-icon-search" @click="handleAggregateByIndicator">查询</el-button>
          <el-table v-loading="loading" :data="indicatorData" border style="margin-top: 20px;">
            <el-table-column prop="indicatorName" label="指标名称" width="200" />
            <el-table-column prop="dataCount" label="数据数量" width="120" />
            <el-table-column prop="dataSum" label="总和" width="150">
              <template slot-scope="scope">
                <span>{{ formatNumber(scope.row.dataSum) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="dataAvg" label="平均值" width="150">
              <template slot-scope="scope">
                <span>{{ formatNumber(scope.row.dataAvg) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="dataMax" label="最大值" width="150">
              <template slot-scope="scope">
                <span>{{ formatNumber(scope.row.dataMax) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="dataMin" label="最小值" width="150">
              <template slot-scope="scope">
                <span>{{ formatNumber(scope.row.dataMin) }}</span>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="按组织汇总" name="org">
          <el-button type="primary" icon="el-icon-search" @click="handleAggregateByOrg">查询</el-button>
          <el-table v-loading="loading" :data="orgData" border style="margin-top: 20px;">
            <el-table-column prop="orgName" label="组织名称" width="200" />
            <el-table-column prop="dataCount" label="数据数量" width="120" />
            <el-table-column prop="dataSum" label="总和" width="150">
              <template slot-scope="scope">
                <span>{{ formatNumber(scope.row.dataSum) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="dataAvg" label="平均值" width="150">
              <template slot-scope="scope">
                <span>{{ formatNumber(scope.row.dataAvg) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="dataMax" label="最大值" width="150">
              <template slot-scope="scope">
                <span>{{ formatNumber(scope.row.dataMax) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="dataMin" label="最小值" width="150">
              <template slot-scope="scope">
                <span>{{ formatNumber(scope.row.dataMin) }}</span>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="按期间汇总" name="period">
          <el-button type="primary" icon="el-icon-search" @click="handleAggregateByPeriod">查询</el-button>
          <el-button type="success" icon="el-icon-data-line" @click="showTrendChart">趋势图</el-button>
          <el-table v-loading="loading" :data="periodData" border style="margin-top: 20px;">
            <el-table-column prop="period" label="期间" width="150" />
            <el-table-column prop="dataCount" label="数据数量" width="120" />
            <el-table-column prop="dataSum" label="总和" width="150">
              <template slot-scope="scope">
                <span>{{ formatNumber(scope.row.dataSum) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="dataAvg" label="平均值" width="150">
              <template slot-scope="scope">
                <span>{{ formatNumber(scope.row.dataAvg) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="dataMax" label="最大值" width="150">
              <template slot-scope="scope">
                <span>{{ formatNumber(scope.row.dataMax) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="dataMin" label="最小值" width="150">
              <template slot-scope="scope">
                <span>{{ formatNumber(scope.row.dataMin) }}</span>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>

        <el-tab-pane label="期间对比" name="compare">
          <el-form :inline="true" :model="compareForm" style="margin-bottom: 20px;">
            <el-form-item label="当前期间">
              <el-input v-model="compareForm.currentPeriod" placeholder="如:202401" />
            </el-form-item>
            <el-form-item label="对比期间">
              <el-input v-model="compareForm.comparePeriod" placeholder="如:202312" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" icon="el-icon-search" @click="handleComparePeriod">对比</el-button>
            </el-form-item>
          </el-form>
          <el-table v-loading="loading" :data="compareData" border>
            <el-table-column prop="indicatorName" label="指标名称" width="200" />
            <el-table-column prop="period" label="当前期间" width="120" />
            <el-table-column prop="dataValue" label="当前值" width="150">
              <template slot-scope="scope">
                <span>{{ formatNumber(scope.row.dataValue) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="compareValue" label="对比值" width="150">
              <template slot-scope="scope">
                <span>{{ formatNumber(scope.row.compareValue) }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="growthAmount" label="增长额" width="150">
              <template slot-scope="scope">
                <span :class="scope.row.growthAmount >= 0 ? 'text-success' : 'text-danger'">
                  {{ formatNumber(scope.row.growthAmount) }}
                </span>
              </template>
            </el-table-column>
            <el-table-column prop="growthRate" label="增长率(%)" width="150">
              <template slot-scope="scope">
                <span :class="scope.row.growthRate >= 0 ? 'text-success' : 'text-danger'">
                  {{ formatNumber(scope.row.growthRate) }}%
                </span>
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 趋势图对话框 -->
    <data-chart
      ref="dataChart"
      :visible.sync="chartVisible"
      :chart-data="chartData"
      :chart-title="chartTitle"
    />
  </div>
</template>

<script>
import {
  aggregateByIndicator,
  aggregateByOrg,
  aggregateByPeriod,
  comparePeriod
} from '@/api/financialSharing/enterpriseReport/reportDataAnalysis'
import { getReportTaskList } from '@/api/financialSharing/enterpriseReport/reportTask'
import { getIndicatorInfoList } from '@/api/financialSharing/enterpriseReport/indicatorInfo'
import DataChart from './components/DataChart'

export default {
  name: 'ReportDataQuery',
  components: {
    DataChart
  },
  data() {
    return {
      // 遮罩层
      loading: false,
      // 当前激活的标签页
      activeTab: 'indicator',
      // 查询参数
      queryForm: {
        taskId: '',
        indicatorId: '',
        orgId: '',
        period: ''
      },
      // 对比查询参数
      compareForm: {
        currentPeriod: '',
        comparePeriod: ''
      },
      // 任务选项
      taskOptions: [],
      // 指标选项
      indicatorOptions: [],
      // 按指标汇总数据
      indicatorData: [],
      // 按组织汇总数据
      orgData: [],
      // 按期间汇总数据
      periodData: [],
      // 期间对比数据
      compareData: [],
      // 图表显示
      chartVisible: false,
      // 图表数据
      chartData: [],
      // 图表标题
      chartTitle: ''
    }
  },
  created() {
    this.getTaskOptions()
    this.getIndicatorOptions()
  },
  methods: {
    /** 获取任务选项 */
    getTaskOptions() {
      getReportTaskList({ status: 'PUBLISHED' }).then(response => {
        if (response.code === 200) {
          this.taskOptions = (response.data || []).map(item => ({
            value: item.taskId,
            label: item.taskName
          }))
        }
      })
    },
    /** 获取指标选项 */
    getIndicatorOptions() {
      getIndicatorInfoList({ status: 'ACTIVE' }).then(response => {
        if (response.code === 200) {
          this.indicatorOptions = (response.data || []).map(item => ({
            value: item.indicatorId,
            label: item.indicatorName
          }))
        }
      })
    },
    /** 标签页切换 */
    handleTabClick(tab) {
      // 清空数据
      this.indicatorData = []
      this.orgData = []
      this.periodData = []
      this.compareData = []
    },
    /** 按指标汇总 */
    handleAggregateByIndicator() {
      this.loading = true
      // 启用真实 API 调用，失败时显示空状态
      aggregateByIndicator({
        taskId: this.queryForm.taskId,
        period: this.queryForm.period
      }).then(response => {
        this.loading = false
        if (response.code === 1 || response.code === 200) {
          this.indicatorData = response.data || []
        } else {
          this.$message.error(response.msg || '查询失败')
          this.indicatorData = []
        }
      }).catch(err => {
        this.loading = false
        console.error('查询失败:', err)
        this.indicatorData = []
      })
    },
    /** 按组织汇总 */
    handleAggregateByOrg() {
      this.loading = true
      // 启用真实 API 调用，失败时显示空状态
      aggregateByOrg({
        taskId: this.queryForm.taskId,
        period: this.queryForm.period
      }).then(response => {
        this.loading = false
        if (response.code === 1 || response.code === 200) {
          this.orgData = response.data || []
        } else {
          this.$message.error(response.msg || '查询失败')
          this.orgData = []
        }
      }).catch(err => {
        this.loading = false
        console.error('查询失败:', err)
        this.orgData = []
      })
    },
    /** 按期间汇总 */
    handleAggregateByPeriod() {
      this.loading = true
      aggregateByPeriod({
        taskId: this.queryForm.taskId,
        indicatorId: this.queryForm.indicatorId,
        orgId: this.queryForm.orgId
      }).then(response => {
        if (response.code === 200) {
          this.periodData = response.data || []
        } else {
          this.$message.error(response.msg || '查询失败')
        }
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    /** 期间对比 */
    handleComparePeriod() {
      if (!this.compareForm.currentPeriod) {
        this.$message.warning('请输入当前期间')
        return
      }
      if (!this.compareForm.comparePeriod) {
        this.$message.warning('请输入对比期间')
        return
      }

      this.loading = true
      comparePeriod({
        taskId: this.queryForm.taskId,
        indicatorId: this.queryForm.indicatorId,
        orgId: this.queryForm.orgId,
        currentPeriod: this.compareForm.currentPeriod,
        comparePeriod: this.compareForm.comparePeriod
      }).then(response => {
        if (response.code === 200) {
          this.compareData = response.data ? [response.data] : []
        } else {
          this.$message.error(response.msg || '查询失败')
        }
        this.loading = false
      }).catch(() => {
        this.loading = false
      })
    },
    /** 显示趋势图 */
    showTrendChart() {
      if (this.periodData.length === 0) {
        this.$message.warning('请先查询数据')
        return
      }

      this.chartData = this.periodData.map(item => ({
        name: item.period,
        value: item.dataSum
      }))
      this.chartTitle = '数据趋势图'
      this.chartVisible = true
    },
    /** 格式化数字 */
    formatNumber(value) {
      if (value === null || value === undefined) {
        return '-'
      }
      return Number(value).toFixed(2)
    }
  }
}
</script>

<style scoped>
.text-success {
  color: #67c23a;
}
.text-danger {
  color: #f56c6c;
}
</style>


