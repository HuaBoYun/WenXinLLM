<template>
  <div class="app-container">
    <!-- 查询表单 -->
    <el-form :inline="true" :model="queryForm" class="query-form">
      <el-form-item label="组织ID">
        <el-input v-model="queryForm.orgId" placeholder="请输入组织ID" clearable style="width: 150px" />
      </el-form-item>
      <el-form-item label="科目编码">
        <el-input v-model="queryForm.subjectCode" placeholder="请输入科目编码" clearable style="width: 150px" />
      </el-form-item>
      <el-form-item label="期间范围">
        <el-input v-model="queryForm.startPeriod" placeholder="开始期间" clearable style="width: 120px" />
        <span style="margin: 0 5px;">-</span>
        <el-input v-model="queryForm.endPeriod" placeholder="结束期间" clearable style="width: 120px" />
      </el-form-item>
      <el-form-item label="来源系统">
        <el-input v-model="queryForm.sourceSystem" placeholder="请输入来源系统" clearable style="width: 120px" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" @click="handleReset">重置</el-button>
      </el-form-item>
    </el-form>

    <!-- 控制效果分析 -->
    <el-card shadow="hover" class="chart-card">
      <div slot="header" class="card-header">
        <span><i class="el-icon-data-analysis" /> 控制效果分析</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="12">
          <div id="effectPieChart" style="width: 100%; height: 350px;" />
        </el-col>
        <el-col :span="12">
          <div class="effect-stats">
            <div class="stat-item">
              <div class="stat-label">总控制次数</div>
              <div class="stat-value">{{ effectData.totalCount }}</div>
            </div>
            <div class="stat-item">
              <div class="stat-label">通过次数</div>
              <div class="stat-value" style="color: #67C23A;">{{ effectData.passCount }}</div>
              <div class="stat-rate">通过率: {{ effectData.passRate }}%</div>
            </div>
            <div class="stat-item">
              <div class="stat-label">阻止次数</div>
              <div class="stat-value" style="color: #F56C6C;">{{ effectData.blockCount }}</div>
              <div class="stat-rate">阻止率: {{ effectData.blockRate }}%</div>
            </div>
            <div class="stat-item">
              <div class="stat-label">预警次数</div>
              <div class="stat-value" style="color: #E6A23C;">{{ effectData.warningCount }}</div>
              <div class="stat-rate">预警率: {{ effectData.warningRate }}%</div>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <!-- 预算使用趋势分析 -->
    <el-card shadow="hover" class="chart-card">
      <div slot="header" class="card-header">
        <span><i class="el-icon-s-data" /> 预算使用趋势分析</span>
      </div>
      <div id="trendLineChart" style="width: 100%; height: 400px;" />
    </el-card>

    <!-- 控制结果分布 -->
    <el-card shadow="hover" class="chart-card">
      <div slot="header" class="card-header">
        <span><i class="el-icon-pie-chart" /> 控制结果分布</span>
      </div>
      <div id="distributionBarChart" style="width: 100%; height: 350px;" />
    </el-card>

    <!-- 组织和科目控制排名 -->
    <el-row :gutter="20" class="ranking-row">
      <el-col :span="12">
        <el-card shadow="hover" class="chart-card">
          <div slot="header" class="card-header">
            <span><i class="el-icon-s-flag" /> 组织控制排名 TOP10</span>
          </div>
          <div id="orgRankingChart" style="width: 100%; height: 400px;" />
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover" class="chart-card">
          <div slot="header" class="card-header">
            <span><i class="el-icon-s-flag" /> 科目控制排名 TOP10</span>
          </div>
          <div id="subjectRankingChart" style="width: 100%; height: 400px;" />
        </el-card>
      </el-col>
    </el-row>

    <!-- 异常控制分析 -->
    <el-card shadow="hover" class="chart-card">
      <div slot="header" class="card-header">
        <span><i class="el-icon-warning" /> 异常控制分析</span>
      </div>
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="abnormal-stat">
            <div class="abnormal-label">总阻止次数</div>
            <div class="abnormal-value">{{ abnormalData.totalBlockCount }}</div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="abnormal-stat">
            <div class="abnormal-label">总阻止金额</div>
            <div class="abnormal-value">{{ formatAmount(abnormalData.totalBlockAmount) }}</div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="abnormal-stat">
            <div class="abnormal-label">平均阻止金额</div>
            <div class="abnormal-value">{{ formatAmount(avgBlockAmount) }}</div>
          </div>
        </el-col>
      </el-row>
      <el-divider />
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="top-list">
            <div class="top-list-title">阻止次数最多的组织 TOP5</div>
            <div v-for="(item, index) in abnormalData.topOrgBlocks.slice(0, 5)" :key="index" class="top-list-item">
              <span class="top-rank">{{ index + 1 }}</span>
              <span class="top-name">{{ item.orgId }}</span>
              <span class="top-count">{{ item.count }}次</span>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="top-list">
            <div class="top-list-title">阻止次数最多的科目 TOP5</div>
            <div v-for="(item, index) in abnormalData.topSubjectBlocks.slice(0, 5)" :key="index" class="top-list-item">
              <span class="top-rank">{{ index + 1 }}</span>
              <span class="top-name">{{ item.subjectCode }}</span>
              <span class="top-count">{{ item.count }}次</span>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="top-list">
            <div class="top-list-title">阻止次数最多的系统 TOP5</div>
            <div v-for="(item, index) in abnormalData.topSystemBlocks.slice(0, 5)" :key="index" class="top-list-item">
              <span class="top-rank">{{ index + 1 }}</span>
              <span class="top-name">{{ item.sourceSystem }}</span>
              <span class="top-count">{{ item.count }}次</span>
            </div>
          </div>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import {
  getControlEffectAnalysis,
  getBudgetUsageTrend,
  getAbnormalControlAnalysis,
  getControlResultDistribution,
  getOrgControlRanking,
  getSubjectControlRanking
} from '@/api/financialSharing/budgetControl'

export default {
  name: 'ControlAnalysis',
  data() {
    return {
      queryForm: {
        orgId: '',
        subjectCode: '',
        startPeriod: '',
        endPeriod: '',
        sourceSystem: ''
      },
      effectData: {
        totalCount: 0,
        passCount: 0,
        blockCount: 0,
        warningCount: 0,
        passRate: 0,
        blockRate: 0,
        warningRate: 0
      },
      trendData: [],
      distributionData: [],
      orgRankingData: [],
      subjectRankingData: [],
      abnormalData: {
        totalBlockCount: 0,
        totalBlockAmount: 0,
        topOrgBlocks: [],
        topSubjectBlocks: [],
        topSystemBlocks: []
      },
      charts: {}
    }
  },
  computed: {
    avgBlockAmount() {
      if (this.abnormalData.totalBlockCount === 0) return 0
      return this.abnormalData.totalBlockAmount / this.abnormalData.totalBlockCount
    }
  },
  mounted() {
    this.initCharts()
    this.loadAllData()
  },
  beforeDestroy() {
    // 销毁所有图表实例
    Object.values(this.charts).forEach(chart => {
      if (chart) {
        chart.dispose()
      }
    })
  },
  methods: {
    // 初始化所有图表
    initCharts() {
      this.charts.effectPie = echarts.init(document.getElementById('effectPieChart'))
      this.charts.trendLine = echarts.init(document.getElementById('trendLineChart'))
      this.charts.distributionBar = echarts.init(document.getElementById('distributionBarChart'))
      this.charts.orgRanking = echarts.init(document.getElementById('orgRankingChart'))
      this.charts.subjectRanking = echarts.init(document.getElementById('subjectRankingChart'))

      // 监听窗口大小变化
      window.addEventListener('resize', this.handleResize)
    },
    // 处理窗口大小变化
    handleResize() {
      Object.values(this.charts).forEach(chart => {
        if (chart) {
          chart.resize()
        }
      })
    },
    // 加载所有数据
    loadAllData() {
      this.getEffectAnalysis()
      this.getTrendAnalysis()
      this.getDistributionAnalysis()
      this.getOrgRanking()
      this.getSubjectRanking()
      this.getAbnormalAnalysis()
    },
    // 获取控制效果分析
    getEffectAnalysis() {
      getControlEffectAnalysis(this.queryForm).then(response => {
        if (response.code === 1 && response.data) {
          this.effectData = response.data
          this.renderEffectPieChart()
        }
      })
    },
    // 获取趋势分析
    getTrendAnalysis() {
      getBudgetUsageTrend(this.queryForm).then(response => {
        if (response.code === 1 && response.data) {
          this.trendData = response.data
          this.renderTrendLineChart()
        }
      })
    },
    // 获取分布分析
    getDistributionAnalysis() {
      getControlResultDistribution(this.queryForm).then(response => {
        if (response.code === 1 && response.data) {
          this.distributionData = response.data
          this.renderDistributionBarChart()
        }
      })
    },
    // 获取组织排名
    getOrgRanking() {
      getOrgControlRanking(this.queryForm).then(response => {
        if (response.code === 1 && response.data) {
          this.orgRankingData = response.data.slice(0, 10)
          this.renderOrgRankingChart()
        }
      })
    },
    // 获取科目排名
    getSubjectRanking() {
      getSubjectControlRanking(this.queryForm).then(response => {
        if (response.code === 1 && response.data) {
          this.subjectRankingData = response.data.slice(0, 10)
          this.renderSubjectRankingChart()
        }
      })
    },
    // 获取异常分析
    getAbnormalAnalysis() {
      getAbnormalControlAnalysis(this.queryForm).then(response => {
        if (response.code === 1 && response.data) {
          this.abnormalData = response.data
        }
      })
    },
    // 渲染控制效果饼图
    renderEffectPieChart() {
      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        legend: {
          orient: 'vertical',
          right: 10,
          top: 'center'
        },
        series: [
          {
            name: '控制结果',
            type: 'pie',
            radius: ['40%', '70%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 10,
              borderColor: '#fff',
              borderWidth: 2
            },
            label: {
              show: false,
              position: 'center'
            },
            emphasis: {
              label: {
                show: true,
                fontSize: '20',
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: false
            },
            data: [
              { value: this.effectData.passCount, name: '通过', itemStyle: { color: '#67C23A' }},
              { value: this.effectData.blockCount, name: '阻止', itemStyle: { color: '#F56C6C' }},
              { value: this.effectData.warningCount, name: '预警', itemStyle: { color: '#E6A23C' }}
            ]
          }
        ]
      }
      this.charts.effectPie.setOption(option)
    },
    // 渲染趋势折线图
    renderTrendLineChart() {
      const periods = this.trendData.map(item => item.period)
      const occupyData = this.trendData.map(item => item.occupyAmount)
      const releaseData = this.trendData.map(item => item.releaseAmount)
      const transferData = this.trendData.map(item => item.transferAmount)

      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross'
          }
        },
        legend: {
          data: ['占用金额', '释放金额', '转移金额'],
          top: 10
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
        yAxis: {
          type: 'value',
          name: '金额'
        },
        series: [
          {
            name: '占用金额',
            type: 'line',
            smooth: true,
            data: occupyData,
            itemStyle: { color: '#409EFF' }
          },
          {
            name: '释放金额',
            type: 'line',
            smooth: true,
            data: releaseData,
            itemStyle: { color: '#67C23A' }
          },
          {
            name: '转移金额',
            type: 'line',
            smooth: true,
            data: transferData,
            itemStyle: { color: '#E6A23C' }
          }
        ]
      }
      this.charts.trendLine.setOption(option)
    },
    /** 控制结果文本映射: 通用工具, 给柱状图/饼图等用 */
    controlResultText(v) {
      const map = {
        PASS: '通过',
        BLOCK: '阻止',
        WARN: '警告',
        WARNING: '警告',
        APPROVE: '待审批',
        RELEASE: '预算释放',
        TRANSFER: '预算调拨',
        OCCUPY: '预算占用'
      }
      return map[v] || v || '-'
    },
    // 渲染分布柱状图
    renderDistributionBarChart() {
      // X 轴标签转成中文, 不要再显示 PASS/BLOCK/WARN 这种英文枚举
      const results = this.distributionData.map(item => this.controlResultText(item.result))
      const counts = this.distributionData.map(item => item.count)

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
          data: results
        },
        yAxis: {
          type: 'value',
          name: '次数'
        },
        series: [
          {
            name: '控制次数',
            type: 'bar',
            data: counts,
            itemStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: '#83bff6' },
                { offset: 0.5, color: '#188df0' },
                { offset: 1, color: '#188df0' }
              ])
            },
            emphasis: {
              itemStyle: {
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                  { offset: 0, color: '#2378f7' },
                  { offset: 0.7, color: '#2378f7' },
                  { offset: 1, color: '#83bff6' }
                ])
              }
            }
          }
        ]
      }
      this.charts.distributionBar.setOption(option)
    },
    // 渲染组织排名图
    renderOrgRankingChart() {
      const orgIds = this.orgRankingData.map(item => item.orgId)
      const totalCounts = this.orgRankingData.map(item => item.totalCount)
      const blockCounts = this.orgRankingData.map(item => item.blockCount)

      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        legend: {
          data: ['总控制次数', '阻止次数'],
          top: 10
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'value'
        },
        yAxis: {
          type: 'category',
          data: orgIds,
          inverse: true
        },
        series: [
          {
            name: '总控制次数',
            type: 'bar',
            data: totalCounts,
            itemStyle: { color: '#409EFF' }
          },
          {
            name: '阻止次数',
            type: 'bar',
            data: blockCounts,
            itemStyle: { color: '#F56C6C' }
          }
        ]
      }
      this.charts.orgRanking.setOption(option)
    },
    // 渲染科目排名图
    renderSubjectRankingChart() {
      const subjectCodes = this.subjectRankingData.map(item => item.subjectCode)
      const totalCounts = this.subjectRankingData.map(item => item.totalCount)
      const blockCounts = this.subjectRankingData.map(item => item.blockCount)

      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          }
        },
        legend: {
          data: ['总控制次数', '阻止次数'],
          top: 10
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'value'
        },
        yAxis: {
          type: 'category',
          data: subjectCodes,
          inverse: true
        },
        series: [
          {
            name: '总控制次数',
            type: 'bar',
            data: totalCounts,
            itemStyle: { color: '#67C23A' }
          },
          {
            name: '阻止次数',
            type: 'bar',
            data: blockCounts,
            itemStyle: { color: '#F56C6C' }
          }
        ]
      }
      this.charts.subjectRanking.setOption(option)
    },
    // 查询
    handleQuery() {
      this.loadAllData()
    },
    // 重置
    handleReset() {
      this.queryForm = {
        orgId: '',
        subjectCode: '',
        startPeriod: '',
        endPeriod: '',
        sourceSystem: ''
      }
      this.loadAllData()
    },
    // 格式化金额
    formatAmount(amount) {
      if (amount == null) return '0.00'
      return parseFloat(amount).toLocaleString('zh-CN', {
        minimumFractionDigits: 2,
        maximumFractionDigits: 2
      })
    }
  }
}
</script>

<style scoped>
.query-form {
  background-color: #f5f7fa;
  padding: 20px;
  border-radius: 4px;
  margin-bottom: 20px;
}

.chart-card {
  margin-bottom: 20px;
}

.card-header {
  font-size: 16px;
  font-weight: bold;
  color: #303133;
}

.card-header i {
  margin-right: 5px;
  color: #409EFF;
}

.effect-stats {
  padding: 20px;
}

.stat-item {
  margin-bottom: 30px;
  padding: 15px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 10px;
}

.stat-value {
  font-size: 28px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 5px;
}

.stat-rate {
  font-size: 12px;
  color: #606266;
}

.ranking-row {
  margin-bottom: 20px;
}

.abnormal-stat {
  text-align: center;
  padding: 20px;
  background-color: #f5f7fa;
  border-radius: 4px;
}

.abnormal-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 10px;
}

.abnormal-value {
  font-size: 24px;
  font-weight: bold;
  color: #F56C6C;
}

.top-list {
  padding: 10px;
}

.top-list-title {
  font-size: 14px;
  font-weight: bold;
  color: #303133;
  margin-bottom: 15px;
  padding-bottom: 10px;
  border-bottom: 2px solid #409EFF;
}

.top-list-item {
  display: flex;
  align-items: center;
  padding: 10px;
  margin-bottom: 8px;
  background-color: #f5f7fa;
  border-radius: 4px;
  transition: all 0.3s;
}

.top-list-item:hover {
  background-color: #e8f4ff;
  transform: translateX(5px);
}

.top-rank {
  display: inline-block;
  width: 24px;
  height: 24px;
  line-height: 24px;
  text-align: center;
  background-color: #409EFF;
  color: white;
  border-radius: 50%;
  font-size: 12px;
  font-weight: bold;
  margin-right: 10px;
}

.top-name {
  flex: 1;
  font-size: 14px;
  color: #303133;
}

.top-count {
  font-size: 14px;
  font-weight: bold;
  color: #F56C6C;
}
</style>
