<template>
  <div class="screen-display">
    <!-- 大屏头部 -->
    <div class="screen-header">
      <div class="header-left">
        <div class="current-date">{{ currentDate }}</div>
      </div>
      <div class="header-title">
        <div class="title-main">国资财经总览</div>
      </div>
      <div class="header-right">
        <el-button
          type="danger"
          icon="el-icon-close"
          circle
          size="small"
          @click="handleClose"
          class="close-btn"
        ></el-button>
      </div>
    </div>

    <!-- 大屏主体内容 -->
    <div class="screen-body">
      <!-- 左侧：应收情况 -->
      <div class="left-panel">
        <!-- 应收情况 -->
        <div class="panel-box receivable-overview">
          <div class="panel-title">
            <i class="el-icon-wallet"></i>
            <span>应收情况</span>
          </div>
          <div class="overview-content">
            <div class="overview-item">
              <div class="item-icon">
                <i class="el-icon-coin"></i>
              </div>
              <div class="item-info">
                <div class="item-label">应收总额</div>
                <div class="item-value">{{ receivableData.total }}<span class="unit">亿元</span></div>
              </div>
            </div>
            <div class="overview-item">
              <div class="item-icon warning">
                <i class="el-icon-warning"></i>
              </div>
              <div class="item-info">
                <div class="item-label">年同期比</div>
                <div class="item-value warning">{{ receivableData.rate }}<span class="unit">%</span></div>
              </div>
            </div>
          </div>
        </div>

        <!-- 应收趋势分析 -->
        <div class="panel-box trend-analysis">
          <div class="panel-title">
            <i class="el-icon-data-line"></i>
            <span>应收趋势分析</span>
          </div>
          <div class="chart-container">
            <div ref="receivableTrendChart" class="chart"></div>
          </div>
        </div>

        <!-- 行业分析 -->
        <div class="panel-box industry-analysis">
          <div class="panel-title">
            <i class="el-icon-s-data"></i>
            <span>行业分析</span>
          </div>
          <div class="industry-list">
            <div class="industry-item" v-for="(item, index) in receivableIndustries" :key="index">
              <div class="industry-rank">{{ index + 1 }}</div>
              <div class="industry-name">{{ item.name }}</div>
              <div class="industry-value">{{ item.value }}</div>
            </div>
          </div>
        </div>
      </div>

      <!-- 中间：地球仪和企业分布 -->
      <div class="center-panel">
        <!-- 国资收入 -->
        <div class="income-display">
          <div class="income-label">国资收入</div>
          <div class="income-value">
            <span class="value">{{ incomeData.total }}</span>
            <span class="unit">亿元</span>
          </div>
          <div class="income-trend" :class="incomeData.trend > 0 ? 'up' : 'down'">
            年同期比：
            <i :class="incomeData.trend > 0 ? 'el-icon-top' : 'el-icon-bottom'"></i>
            {{ Math.abs(incomeData.trend) }}%
          </div>
        </div>

        <!-- 企业集团标签 -->
        <div class="enterprise-tags">
          <div class="tag-group top-group">
            <div class="enterprise-tag" v-for="(company, index) in topCompanies" :key="'top-' + index">
              {{ company }}
            </div>
          </div>
          
          <!-- 地球仪中心 -->
          <div class="globe-container">
            <div class="globe-core">
              <div class="core-ring"></div>
              <div class="core-content">
                <div class="core-label">资金金额</div>
                <div class="core-value">{{ globeData.amount }}<span class="unit">亿元</span></div>
                <div class="core-desc">年同期比：{{ globeData.rate }}%</div>
              </div>
            </div>
          </div>

          <div class="tag-group bottom-group">
            <div class="enterprise-tag" v-for="(company, index) in bottomCompanies" :key="'bottom-' + index">
              {{ company }}
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧：应付情况 -->
      <div class="right-panel">
        <!-- 应付情况 -->
        <div class="panel-box payable-overview">
          <div class="panel-title">
            <i class="el-icon-money"></i>
            <span>应付情况</span>
          </div>
          <div class="overview-content">
            <div class="overview-item">
              <div class="item-icon">
                <i class="el-icon-coin"></i>
              </div>
              <div class="item-info">
                <div class="item-label">应付总额</div>
                <div class="item-value">{{ payableData.total }}<span class="unit">亿元</span></div>
              </div>
            </div>
            <div class="overview-item">
              <div class="item-icon success">
                <i class="el-icon-success"></i>
              </div>
              <div class="item-info">
                <div class="item-label">年同期比</div>
                <div class="item-value success">{{ payableData.rate }}<span class="unit">%</span></div>
              </div>
            </div>
          </div>
        </div>

        <!-- 应付趋势分析 -->
        <div class="panel-box trend-analysis">
          <div class="panel-title">
            <i class="el-icon-data-line"></i>
            <span>应付趋势分析</span>
          </div>
          <div class="chart-container">
            <div ref="payableTrendChart" class="chart"></div>
          </div>
        </div>

        <!-- 行业分析 -->
        <div class="panel-box industry-analysis">
          <div class="panel-title">
            <i class="el-icon-s-data"></i>
            <span>行业分析</span>
          </div>
          <div class="industry-list">
            <div class="industry-item" v-for="(item, index) in payableIndustries" :key="index">
              <div class="industry-rank">{{ index + 1 }}</div>
              <div class="industry-name">{{ item.name }}</div>
              <div class="industry-value">{{ item.value }}</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 底部：成本费用 -->
    <div class="screen-footer">
      <div class="cost-section">
        <div class="cost-header">
          <div class="cost-title">
            <i class="el-icon-s-finance"></i>
            <span>成本费用</span>
          </div>
          <div class="cost-summary">
            <span class="summary-label">成本费用总额：</span>
            <span class="summary-value">{{ costData.total }}</span>
            <span class="summary-unit">亿元</span>
            <span class="summary-trend" :class="costData.trend > 0 ? 'up' : 'down'">
              年同期比：
              <i :class="costData.trend > 0 ? 'el-icon-top' : 'el-icon-bottom'"></i>
              {{ Math.abs(costData.trend) }}%
            </span>
          </div>
        </div>
        <div class="cost-content">
          <div class="cost-chart-container">
            <div ref="costTrendChart" class="cost-chart"></div>
          </div>
          <div class="cost-list">
            <div class="cost-table">
              <div class="table-header">
                <div class="col-rank">序号</div>
                <div class="col-name">行业名称</div>
                <div class="col-value">成本费用（万元）</div>
              </div>
              <div class="table-body">
                <div class="table-row" v-for="(item, index) in costIndustries" :key="index">
                  <div class="col-rank">{{ index + 1 }}</div>
                  <div class="col-name">{{ item.name }}</div>
                  <div class="col-value">{{ item.value }}</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'ScreenDisplay',
  props: {
    financialData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      currentDate: '',
      timeTimer: null,
      receivableTrendChart: null,
      payableTrendChart: null,
      costTrendChart: null,
      // 应收数据
      receivableData: {
        total: 2048,
        rate: 1.23
      },
      receivableIndustries: [
        { name: '钢铁', value: 123654 },
        { name: '能源', value: 123525 },
        { name: '交通', value: 42523 },
        { name: '建筑', value: 145624 },
        { name: '金融', value: 123525 },
        { name: '其他', value: 123525 }
      ],
      // 应付数据
      payableData: {
        total: 2048,
        rate: -1.23
      },
      payableIndustries: [
        { name: '钢铁', value: 123654 },
        { name: '能源', value: 123525 },
        { name: '交通', value: 42523 },
        { name: '建筑', value: 145624 },
        { name: '金融', value: 123525 },
        { name: '其他', value: 123525 }
      ],
      // 收入数据
      incomeData: {
        total: 789.24,
        trend: -17.23
      },
      // 地球仪数据
      globeData: {
        amount: 1617.46,
        rate: -3.2
      },
      // 企业列表
      topCompanies: ['河钢集团', '开滦集团', '冀中能源', '河北交投', '河北国控'],
      bottomCompanies: ['河北建投', '河北港工', '唐山三友', '河北水发', '河北建投'],
      // 成本费用数据
      costData: {
        total: 789.24,
        trend: -17.23
      },
      costIndustries: [
        { name: '钢铁', value: 123654 },
        { name: '能源', value: 123525 },
        { name: '交通', value: 42523 },
        { name: '建筑', value: 145624 },
        { name: '金融', value: 123525 },
        { name: '其他', value: 123525 }
      ]
    }
  },
  mounted() {
    this.updateTime()
    this.timeTimer = setInterval(this.updateTime, 1000)
    this.$nextTick(() => {
      this.initCharts()
    })
  },
  beforeDestroy() {
    if (this.timeTimer) {
      clearInterval(this.timeTimer)
    }
    if (this.receivableTrendChart) {
      this.receivableTrendChart.dispose()
    }
    if (this.payableTrendChart) {
      this.payableTrendChart.dispose()
    }
    if (this.costTrendChart) {
      this.costTrendChart.dispose()
    }
  },
  methods: {
    updateTime() {
      const now = new Date()
      const year = now.getFullYear()
      const month = String(now.getMonth() + 1).padStart(2, '0')
      this.currentDate = `${year}年${month}月`
    },
    handleClose() {
      this.$emit('close')
    },
    initCharts() {
      this.initReceivableTrendChart()
      this.initPayableTrendChart()
      this.initCostTrendChart()
    },
    initReceivableTrendChart() {
      if (!this.$refs.receivableTrendChart) return

      if (this.receivableTrendChart) {
        this.receivableTrendChart.dispose()
      }

      this.receivableTrendChart = echarts.init(this.$refs.receivableTrendChart)

      const option = {
        tooltip: {
          trigger: 'axis'
        },
        grid: {
          left: '5%',
          right: '5%',
          bottom: '10%',
          top: '10%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月'],
          axisLine: {
            lineStyle: {
              color: 'rgba(255, 255, 255, 0.3)'
            }
          },
          axisLabel: {
            color: '#fff',
            fontSize: 10
          }
        },
        yAxis: {
          type: 'value',
          axisLine: {
            lineStyle: {
              color: 'rgba(255, 255, 255, 0.3)'
            }
          },
          axisLabel: {
            color: '#fff',
            fontSize: 10
          },
          splitLine: {
            lineStyle: {
              color: 'rgba(255, 255, 255, 0.1)'
            }
          }
        },
        series: [
          {
            name: '应收金额',
            type: 'line',
            smooth: true,
            data: [20, 50, 75, 100, 75, 150, 100, 50, 25, 0],
            lineStyle: {
              color: '#e6a23c',
              width: 2
            },
            areaStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: 'rgba(230, 162, 60, 0.3)' },
                { offset: 1, color: 'rgba(230, 162, 60, 0.05)' }
              ])
            },
            itemStyle: {
              color: '#e6a23c'
            }
          }
        ]
      }

      this.receivableTrendChart.setOption(option)
    },
    initPayableTrendChart() {
      if (!this.$refs.payableTrendChart) return

      if (this.payableTrendChart) {
        this.payableTrendChart.dispose()
      }

      this.payableTrendChart = echarts.init(this.$refs.payableTrendChart)

      const option = {
        tooltip: {
          trigger: 'axis'
        },
        grid: {
          left: '5%',
          right: '5%',
          bottom: '10%',
          top: '10%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月'],
          axisLine: {
            lineStyle: {
              color: 'rgba(255, 255, 255, 0.3)'
            }
          },
          axisLabel: {
            color: '#fff',
            fontSize: 10
          }
        },
        yAxis: {
          type: 'value',
          axisLine: {
            lineStyle: {
              color: 'rgba(255, 255, 255, 0.3)'
            }
          },
          axisLabel: {
            color: '#fff',
            fontSize: 10
          },
          splitLine: {
            lineStyle: {
              color: 'rgba(255, 255, 255, 0.1)'
            }
          }
        },
        series: [
          {
            name: '应付金额',
            type: 'line',
            smooth: true,
            data: [0, 25, 50, 100, 75, 150, 100, 75, 50, 25],
            lineStyle: {
              color: '#67c23a',
              width: 2
            },
            areaStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: 'rgba(103, 194, 58, 0.3)' },
                { offset: 1, color: 'rgba(103, 194, 58, 0.05)' }
              ])
            },
            itemStyle: {
              color: '#67c23a'
            }
          }
        ]
      }

      this.payableTrendChart.setOption(option)
    },
    initCostTrendChart() {
      if (!this.$refs.costTrendChart) return

      if (this.costTrendChart) {
        this.costTrendChart.dispose()
      }

      this.costTrendChart = echarts.init(this.$refs.costTrendChart)

      const option = {
        tooltip: {
          trigger: 'axis'
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '5%',
          top: '5%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月'],
          axisLine: {
            lineStyle: {
              color: 'rgba(255, 255, 255, 0.3)'
            }
          },
          axisLabel: {
            color: '#fff',
            fontSize: 11
          }
        },
        yAxis: {
          type: 'value',
          axisLine: {
            lineStyle: {
              color: 'rgba(255, 255, 255, 0.3)'
            }
          },
          axisLabel: {
            color: '#fff',
            fontSize: 11
          },
          splitLine: {
            lineStyle: {
              color: 'rgba(255, 255, 255, 0.1)'
            }
          }
        },
        series: [
          {
            name: '成本',
            type: 'line',
            smooth: true,
            data: [100, 75, 105, 100, 75, 100, 75, 50, 25, 0],
            lineStyle: {
              color: '#409eff',
              width: 2
            },
            areaStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: 'rgba(64, 158, 255, 0.3)' },
                { offset: 1, color: 'rgba(64, 158, 255, 0.05)' }
              ])
            },
            itemStyle: {
              color: '#409eff'
            }
          },
          {
            name: '费用',
            type: 'line',
            smooth: true,
            data: [75, 50, 75, 100, 50, 75, 50, 25, 0, 25],
            lineStyle: {
              color: '#e6a23c',
              width: 2
            },
            areaStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: 'rgba(230, 162, 60, 0.3)' },
                { offset: 1, color: 'rgba(230, 162, 60, 0.05)' }
              ])
            },
            itemStyle: {
              color: '#e6a23c'
            }
          }
        ]
      }

      this.costTrendChart.setOption(option)
    }
  }
}
</script>

<style lang="scss" scoped>
.screen-display {
  width: 100vw;
  height: 100vh;
  background: linear-gradient(180deg, #0a0e27 0%, #1a1f3a 100%);
  color: #fff;
  display: flex;
  flex-direction: column;
  position: relative;
  overflow: hidden;

  // 大屏头部
  .screen-header {
    height: 70px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 40px;
    background: rgba(10, 14, 39, 0.8);
    border-bottom: 2px solid rgba(64, 158, 255, 0.3);
    position: relative;

    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      height: 2px;
      background: linear-gradient(90deg, transparent, #409eff, transparent);
    }

    .header-left {
      flex: 1;

      .current-date {
        font-size: 14px;
        color: #409eff;
        font-family: 'Courier New', monospace;
        letter-spacing: 2px;
        background: rgba(64, 158, 255, 0.1);
        padding: 6px 15px;
        border-radius: 4px;
        border: 1px solid rgba(64, 158, 255, 0.3);
        display: inline-block;
      }
    }

    .header-title {
      flex: 2;
      text-align: center;

      .title-main {
        font-size: 32px;
        font-weight: bold;
        background: linear-gradient(90deg, #409eff, #67c23a);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        letter-spacing: 6px;
        text-shadow: 0 0 20px rgba(64, 158, 255, 0.5);
      }
    }

    .header-right {
      flex: 1;
      display: flex;
      justify-content: flex-end;

      .close-btn {
        background: rgba(245, 108, 108, 0.2);
        border-color: #f56c6c;
        color: #f56c6c;

        &:hover {
          background: rgba(245, 108, 108, 0.4);
        }
      }
    }
  }

  // 大屏主体
  .screen-body {
    flex: 1;
    display: flex;
    padding: 15px;
    gap: 15px;
    position: relative;
    overflow: hidden;

    // 左侧面板
    .left-panel {
      width: 280px;
      display: flex;
      flex-direction: column;
      gap: 12px;
    }

    // 右侧面板
    .right-panel {
      width: 280px;
      display: flex;
      flex-direction: column;
      gap: 12px;
    }

    // 中间面板
    .center-panel {
      flex: 1;
      display: flex;
      flex-direction: column;
      align-items: center;
      justify-content: center;
      position: relative;
    }

    // 通用面板盒子
    .panel-box {
      background: rgba(26, 31, 58, 0.6);
      border: 1px solid rgba(64, 158, 255, 0.3);
      border-radius: 8px;
      padding: 12px;
      backdrop-filter: blur(10px);

      .panel-title {
        display: flex;
        align-items: center;
        gap: 8px;
        font-size: 14px;
        font-weight: bold;
        color: #409eff;
        margin-bottom: 12px;
        padding-left: 8px;
        border-left: 3px solid #409eff;

        i {
          font-size: 16px;
        }
      }
    }

    // 应收/应付概览
    .receivable-overview,
    .payable-overview {
      .overview-content {
        display: flex;
        flex-direction: column;
        gap: 12px;

        .overview-item {
          display: flex;
          align-items: center;
          gap: 12px;
          padding: 10px;
          background: rgba(10, 14, 39, 0.4);
          border-radius: 6px;

          .item-icon {
            width: 45px;
            height: 45px;
            display: flex;
            align-items: center;
            justify-content: center;
            background: rgba(64, 158, 255, 0.2);
            border-radius: 50%;

            i {
              font-size: 22px;
              color: #409eff;
            }

            &.warning {
              background: rgba(230, 162, 60, 0.2);

              i {
                color: #e6a23c;
              }
            }

            &.success {
              background: rgba(103, 194, 58, 0.2);

              i {
                color: #67c23a;
              }
            }
          }

          .item-info {
            flex: 1;

            .item-label {
              font-size: 12px;
              color: rgba(255, 255, 255, 0.6);
              margin-bottom: 4px;
            }

            .item-value {
              font-size: 22px;
              font-weight: bold;
              color: #409eff;

              .unit {
                font-size: 12px;
                margin-left: 4px;
              }

              &.warning {
                color: #e6a23c;
              }

              &.success {
                color: #67c23a;
              }
            }
          }
        }
      }
    }

    // 趋势分析
    .trend-analysis {
      flex: 1;

      .chart-container {
        .chart {
          height: 180px;
        }
      }
    }

    // 行业分析
    .industry-analysis {
      .industry-list {
        display: flex;
        flex-direction: column;
        gap: 8px;

        .industry-item {
          display: flex;
          align-items: center;
          gap: 10px;
          padding: 8px;
          background: rgba(10, 14, 39, 0.4);
          border-radius: 4px;
          transition: all 0.3s;

          &:hover {
            background: rgba(64, 158, 255, 0.1);
            transform: translateX(3px);
          }

          .industry-rank {
            width: 24px;
            height: 24px;
            display: flex;
            align-items: center;
            justify-content: center;
            background: rgba(64, 158, 255, 0.2);
            border-radius: 4px;
            font-size: 12px;
            font-weight: bold;
            color: #409eff;
          }

          .industry-name {
            flex: 1;
            font-size: 13px;
            color: rgba(255, 255, 255, 0.9);
          }

          .industry-value {
            font-size: 13px;
            font-weight: bold;
            color: #67c23a;
          }
        }
      }
    }

    // 中间面板样式
    .income-display {
      text-align: center;
      margin-bottom: 20px;

      .income-label {
        font-size: 14px;
        color: rgba(255, 255, 255, 0.7);
        margin-bottom: 8px;
      }

      .income-value {
        margin-bottom: 8px;

        .value {
          font-size: 36px;
          font-weight: bold;
          color: #409eff;
        }

        .unit {
          font-size: 14px;
          color: rgba(255, 255, 255, 0.7);
          margin-left: 6px;
        }
      }

      .income-trend {
        font-size: 13px;
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

        i {
          font-size: 14px;
        }
      }
    }

    .enterprise-tags {
      position: relative;
      width: 100%;
      max-width: 700px;

      .tag-group {
        display: flex;
        justify-content: center;
        gap: 12px;
        flex-wrap: wrap;
        margin-bottom: 15px;

        &.bottom-group {
          margin-top: 15px;
          margin-bottom: 0;
        }

        .enterprise-tag {
          padding: 8px 16px;
          background: rgba(64, 158, 255, 0.15);
          border: 1px solid rgba(64, 158, 255, 0.4);
          border-radius: 20px;
          font-size: 12px;
          color: #409eff;
          cursor: pointer;
          transition: all 0.3s;
          white-space: nowrap;

          &:hover {
            background: rgba(64, 158, 255, 0.3);
            border-color: #409eff;
            transform: scale(1.05);
            box-shadow: 0 4px 12px rgba(64, 158, 255, 0.4);
          }
        }
      }

      .globe-container {
        display: flex;
        justify-content: center;
        align-items: center;
        padding: 20px 0;

        .globe-core {
          width: 280px;
          height: 280px;
          border-radius: 50%;
          background: radial-gradient(circle, rgba(64, 158, 255, 0.2), rgba(10, 14, 39, 0.8));
          border: 3px solid #409eff;
          display: flex;
          align-items: center;
          justify-content: center;
          position: relative;
          box-shadow: 0 0 40px rgba(64, 158, 255, 0.5), inset 0 0 40px rgba(64, 158, 255, 0.2);

          &::before {
            content: '';
            position: absolute;
            width: 320px;
            height: 320px;
            border-radius: 50%;
            border: 1px solid rgba(64, 158, 255, 0.3);
            animation: pulse 3s infinite;
          }

          &::after {
            content: '';
            position: absolute;
            width: 360px;
            height: 360px;
            border-radius: 50%;
            border: 1px solid rgba(64, 158, 255, 0.2);
            animation: pulse 3s infinite 1.5s;
          }

          .core-ring {
            position: absolute;
            width: 240px;
            height: 240px;
            border-radius: 50%;
            border: 2px dashed rgba(103, 194, 58, 0.4);
            animation: rotate 20s linear infinite;
          }

          .core-content {
            text-align: center;
            z-index: 1;

            .core-label {
              font-size: 13px;
              color: rgba(255, 255, 255, 0.7);
              margin-bottom: 8px;
            }

            .core-value {
              font-size: 32px;
              font-weight: bold;
              color: #67c23a;
              margin-bottom: 8px;
              text-shadow: 0 0 10px rgba(103, 194, 58, 0.8);

              .unit {
                font-size: 14px;
                margin-left: 4px;
              }
            }

            .core-desc {
              font-size: 12px;
              color: rgba(255, 255, 255, 0.6);
            }
          }
        }
      }
    }
  }

  // 底部：成本费用
  .screen-footer {
    height: 200px;
    padding: 12px 15px;
    background: rgba(10, 14, 39, 0.8);
    border-top: 2px solid rgba(64, 158, 255, 0.3);
    position: relative;

    &::before {
      content: '';
      position: absolute;
      bottom: 0;
      left: 0;
      right: 0;
      height: 2px;
      background: linear-gradient(90deg, transparent, #409eff, transparent);
    }

    .cost-section {
      height: 100%;
      display: flex;
      flex-direction: column;

      .cost-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        margin-bottom: 10px;

        .cost-title {
          display: flex;
          align-items: center;
          gap: 8px;
          font-size: 16px;
          font-weight: bold;
          color: #409eff;

          i {
            font-size: 18px;
          }
        }

        .cost-summary {
          display: flex;
          align-items: center;
          gap: 8px;
          font-size: 13px;

          .summary-label {
            color: rgba(255, 255, 255, 0.7);
          }

          .summary-value {
            font-size: 20px;
            font-weight: bold;
            color: #409eff;
          }

          .summary-unit {
            color: rgba(255, 255, 255, 0.7);
          }

          .summary-trend {
            display: flex;
            align-items: center;
            gap: 4px;

            &.up {
              color: #f56c6c;
            }

            &.down {
              color: #67c23a;
            }
          }
        }
      }

      .cost-content {
        flex: 1;
        display: flex;
        gap: 15px;

        .cost-chart-container {
          flex: 2;
          background: rgba(26, 31, 58, 0.6);
          border: 1px solid rgba(64, 158, 255, 0.3);
          border-radius: 8px;
          padding: 10px;

          .cost-chart {
            height: 100%;
          }
        }

        .cost-list {
          flex: 1;
          background: rgba(26, 31, 58, 0.6);
          border: 1px solid rgba(64, 158, 255, 0.3);
          border-radius: 8px;
          padding: 10px;

          .cost-table {
            height: 100%;
            display: flex;
            flex-direction: column;

            .table-header {
              display: flex;
              padding: 8px 0;
              border-bottom: 1px solid rgba(64, 158, 255, 0.3);
              font-size: 12px;
              font-weight: bold;
              color: #409eff;

              .col-rank {
                width: 50px;
                text-align: center;
              }

              .col-name {
                flex: 1;
                text-align: left;
              }

              .col-value {
                width: 120px;
                text-align: right;
              }
            }

            .table-body {
              flex: 1;
              overflow-y: auto;

              .table-row {
                display: flex;
                padding: 6px 0;
                font-size: 11px;
                color: rgba(255, 255, 255, 0.8);
                transition: all 0.3s;

                &:hover {
                  background: rgba(64, 158, 255, 0.1);
                }

                .col-rank {
                  width: 50px;
                  text-align: center;
                  color: #67c23a;
                }

                .col-name {
                  flex: 1;
                  text-align: left;
                }

                .col-value {
                  width: 120px;
                  text-align: right;
                  color: #409eff;
                  font-weight: bold;
                }
              }
            }
          }
        }
      }
    }
  }
}

@keyframes pulse {
  0%, 100% {
    opacity: 0.3;
    transform: scale(1);
  }
  50% {
    opacity: 0.6;
    transform: scale(1.05);
  }
}

@keyframes rotate {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}
</style>

