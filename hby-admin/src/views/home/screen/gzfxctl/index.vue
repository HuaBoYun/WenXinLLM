<template>
  <div class="gzfxctl-container">
    <!-- 页面头部 -->
    <div class="page-header">
      <div class="header-left">
        <i class="el-icon-data-analysis"></i>
        <h2>国资财经运行总览</h2>
      </div>
      <div class="header-right">
        <el-button
          type="primary"
          icon="el-icon-full-screen"
          @click="openFullScreen"
          class="screen-btn"
        >
          财经大屏
        </el-button>
      </div>
    </div>

    <!-- 主要内容区域 -->
    <div class="main-content">
      <!-- 数据概览卡片 -->
      <div class="overview-section">
        <el-row :gutter="20">
          <el-col :span="6">
            <el-card class="overview-card">
              <div class="card-header">
                <i class="el-icon-coin"></i>
                <span>国资收入</span>
              </div>
              <div class="card-value">{{ overviewData.totalIncome || 0 }}<span class="unit">亿元</span></div>
              <div class="card-trend" :class="overviewData.incomeTrend > 0 ? 'up' : 'down'">
                <i :class="overviewData.incomeTrend > 0 ? 'el-icon-top' : 'el-icon-bottom'"></i>
                {{ Math.abs(overviewData.incomeTrend || 0) }}%
              </div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card class="overview-card">
              <div class="card-header">
                <i class="el-icon-wallet"></i>
                <span>应收总额</span>
              </div>
              <div class="card-value">{{ overviewData.receivableTotal || 0 }}<span class="unit">亿元</span></div>
              <div class="card-desc">年同期比：{{ overviewData.receivableRate || 0 }}%</div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card class="overview-card">
              <div class="card-header">
                <i class="el-icon-money"></i>
                <span>应付总额</span>
              </div>
              <div class="card-value">{{ overviewData.payableTotal || 0 }}<span class="unit">亿元</span></div>
              <div class="card-desc">年同期比：{{ overviewData.payableRate || 0 }}%</div>
            </el-card>
          </el-col>
          <el-col :span="6">
            <el-card class="overview-card">
              <div class="card-header">
                <i class="el-icon-s-finance"></i>
                <span>成本费用</span>
              </div>
              <div class="card-value">{{ overviewData.costTotal || 0 }}<span class="unit">亿元</span></div>
              <div class="card-trend" :class="overviewData.costTrend > 0 ? 'down' : 'up'">
                <i :class="overviewData.costTrend > 0 ? 'el-icon-top' : 'el-icon-bottom'"></i>
                {{ Math.abs(overviewData.costTrend || 0) }}%
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>

      <!-- 企业分布 -->
      <div class="enterprise-section">
        <el-card>
          <div slot="header" class="card-title">
            <i class="el-icon-office-building"></i>
            <span>重点企业分布</span>
          </div>
          <el-row :gutter="15">
            <el-col :span="4" v-for="company in keyCompanies" :key="company.id">
              <div class="company-item">
                <div class="company-name">{{ company.name }}</div>
                <div class="company-status" :class="company.status">{{ company.statusText }}</div>
              </div>
            </el-col>
          </el-row>
        </el-card>
      </div>

      <!-- 行业分析 -->
      <div class="industry-section">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-card>
              <div slot="header" class="card-title">
                <i class="el-icon-data-line"></i>
                <span>应收行业分析</span>
              </div>
              <div ref="receivableIndustryChart" style="height: 300px;"></div>
            </el-card>
          </el-col>
          <el-col :span="12">
            <el-card>
              <div slot="header" class="card-title">
                <i class="el-icon-data-line"></i>
                <span>应付行业分析</span>
              </div>
              <div ref="payableIndustryChart" style="height: 300px;"></div>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </div>

    <!-- 全屏大屏弹窗 -->
    <el-dialog
      :visible.sync="screenVisible"
      fullscreen
      :show-close="false"
      custom-class="screen-dialog"
      @close="handleScreenClose"
    >
      <screen-display
        v-if="screenVisible"
        :financial-data="financialData"
        @close="closeFullScreen"
      />
    </el-dialog>
  </div>
</template>

<script>
import ScreenDisplay from './components/ScreenDisplay.vue'
import { getFinancialData } from './api/financial'
import * as echarts from 'echarts'

export default {
  name: 'Gzfxctl',
  components: {
    ScreenDisplay
  },
  data() {
    return {
      loading: false,
      screenVisible: false,
      overviewData: {
        totalIncome: 789.24,
        incomeTrend: -17.23,
        receivableTotal: 2048,
        receivableRate: 1.23,
        payableTotal: 2048,
        payableRate: -1.23,
        costTotal: 789.24,
        costTrend: -17.23
      },
      keyCompanies: [
        { id: 1, name: '河钢集团', status: 'normal', statusText: '正常' },
        { id: 2, name: '开滦集团', status: 'normal', statusText: '正常' },
        { id: 3, name: '冀中能源', status: 'warning', statusText: '预警' },
        { id: 4, name: '河北交投', status: 'normal', statusText: '正常' },
        { id: 5, name: '河北国控', status: 'normal', statusText: '正常' },
        { id: 6, name: '河北建投', status: 'normal', statusText: '正常' },
        { id: 7, name: '河北港工', status: 'normal', statusText: '正常' },
        { id: 8, name: '唐山三友', status: 'normal', statusText: '正常' },
        { id: 9, name: '河北水发', status: 'normal', statusText: '正常' },
        { id: 10, name: '河北建投', status: 'normal', statusText: '正常' },
        { id: 11, name: '河北地电', status: 'normal', statusText: '正常' },
        { id: 12, name: '河北国资', status: 'normal', statusText: '正常' },
        { id: 13, name: '河北物资', status: 'normal', statusText: '正常' },
        { id: 14, name: '河北矿业', status: 'normal', statusText: '正常' },
        { id: 15, name: '河北租赁', status: 'normal', statusText: '正常' }
      ],
      financialData: {},
      receivableIndustryChart: null,
      payableIndustryChart: null
    }
  },
  created() {
    this.loadData()
  },
  mounted() {
    this.$nextTick(() => {
      this.initCharts()
    })
  },
  beforeDestroy() {
    if (this.receivableIndustryChart) {
      this.receivableIndustryChart.dispose()
    }
    if (this.payableIndustryChart) {
      this.payableIndustryChart.dispose()
    }
  },
  methods: {
    async loadData() {
      this.loading = true
      try {
        const res = await getFinancialData()
        if (res.code === 200) {
          this.overviewData = res.data.overview || this.overviewData
          this.financialData = res.data
        }
      } catch (error) {
        console.error('加载数据失败:', error)
      } finally {
        this.loading = false
      }
    },
    initCharts() {
      this.initReceivableIndustryChart()
      this.initPayableIndustryChart()
    },
    initReceivableIndustryChart() {
      if (!this.$refs.receivableIndustryChart) return
      
      if (this.receivableIndustryChart) {
        this.receivableIndustryChart.dispose()
      }
      
      this.receivableIndustryChart = echarts.init(this.$refs.receivableIndustryChart)
      
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
          data: ['钢铁', '能源', '交通', '建筑', '金融', '其他']
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '应收金额',
            type: 'bar',
            data: [320, 280, 250, 200, 180, 150],
            itemStyle: {
              color: '#409eff'
            }
          }
        ]
      }
      
      this.receivableIndustryChart.setOption(option)
    },
    initPayableIndustryChart() {
      if (!this.$refs.payableIndustryChart) return
      
      if (this.payableIndustryChart) {
        this.payableIndustryChart.dispose()
      }
      
      this.payableIndustryChart = echarts.init(this.$refs.payableIndustryChart)
      
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
          data: ['钢铁', '能源', '交通', '建筑', '金融', '其他']
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '应付金额',
            type: 'bar',
            data: [300, 260, 240, 190, 170, 140],
            itemStyle: {
              color: '#67c23a'
            }
          }
        ]
      }
      
      this.payableIndustryChart.setOption(option)
    },
    openFullScreen() {
      this.screenVisible = true
    },
    closeFullScreen() {
      this.screenVisible = false
    },
    handleScreenClose() {
      this.screenVisible = false
    }
  }
}
</script>

<style lang="scss" scoped>
.gzfxctl-container {
  padding: 20px;
  background: #f0f2f5;
  min-height: calc(100vh - 84px);

  .page-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;
    padding: 15px 20px;
    background: #fff;
    border-radius: 4px;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);

    .header-left {
      display: flex;
      align-items: center;
      gap: 10px;

      i {
        font-size: 24px;
        color: #409eff;
      }

      h2 {
        margin: 0;
        font-size: 20px;
        color: #303133;
      }
    }

    .screen-btn {
      font-size: 14px;
    }
  }

  .main-content {
    .overview-section {
      margin-bottom: 20px;

      .overview-card {
        text-align: center;

        .card-header {
          display: flex;
          align-items: center;
          justify-content: center;
          gap: 8px;
          font-size: 14px;
          color: #606266;
          margin-bottom: 15px;

          i {
            font-size: 18px;
          }
        }

        .card-value {
          font-size: 28px;
          font-weight: bold;
          color: #409eff;
          margin-bottom: 10px;

          .unit {
            font-size: 14px;
            margin-left: 4px;
          }
        }

        .card-trend {
          font-size: 14px;
          display: flex;
          align-items: center;
          justify-content: center;
          gap: 4px;

          &.up {
            color: #f56c6c;
          }

          &.down {
            color: #67c23a;
          }
        }

        .card-desc {
          font-size: 12px;
          color: #909399;
        }
      }
    }

    .enterprise-section {
      margin-bottom: 20px;

      .card-title {
        display: flex;
        align-items: center;
        gap: 8px;
        font-size: 16px;
        font-weight: bold;
        color: #303133;

        i {
          font-size: 18px;
          color: #409eff;
        }
      }

      .company-item {
        padding: 12px;
        text-align: center;
        background: #f5f7fa;
        border-radius: 4px;
        margin-bottom: 10px;
        border-left: 3px solid #409eff;
        transition: all 0.3s;

        &:hover {
          background: #ecf5ff;
          transform: translateY(-2px);
          box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }

        .company-name {
          font-size: 14px;
          color: #303133;
          margin-bottom: 6px;
          font-weight: bold;
        }

        .company-status {
          font-size: 12px;
          padding: 2px 8px;
          border-radius: 3px;
          display: inline-block;

          &.normal {
            background: #e1f3d8;
            color: #67c23a;
          }

          &.warning {
            background: #fdf6ec;
            color: #e6a23c;
          }

          &.danger {
            background: #fef0f0;
            color: #f56c6c;
          }
        }
      }
    }

    .industry-section {
      .card-title {
        display: flex;
        align-items: center;
        gap: 8px;
        font-size: 16px;
        font-weight: bold;
        color: #303133;

        i {
          font-size: 18px;
          color: #409eff;
        }
      }
    }
  }
}

::v-deep .screen-dialog {
  background: #0a0e27 !important;
  margin: 0 !important;
  padding: 0 !important;

  .el-dialog__header {
    display: none !important;
  }

  .el-dialog__body {
    padding: 0 !important;
    height: 100vh !important;
    overflow: hidden !important;
  }
}
</style>

