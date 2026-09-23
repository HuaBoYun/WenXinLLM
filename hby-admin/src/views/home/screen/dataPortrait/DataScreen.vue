<template>
  <div class="data-screen">
    <!-- 大屏头部 -->
    <div class="screen-header">
      <div class="header-left">
        <div class="current-time">{{ currentTime }}</div>
      </div>
      <div class="header-title">
        <div class="title-main">企业数据画像大屏</div>
        <div class="title-sub">Enterprise Data Portrait Dashboard</div>
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
      <!-- 左侧：社会侧数据 -->
      <div class="left-panel">
        <div class="panel-title">
          <i class="el-icon-share"></i>
          <span>社会侧数据</span>
        </div>
        <div class="panel-content">
          <!-- 司法数据卡片 -->
          <div class="data-module judicial-module" @click="navigateTo('/ruleMonitor/Telescope')">
            <div class="module-header">
              <div class="module-icon">
                <i class="el-icon-document-checked"></i>
              </div>
              <div class="module-title">司法数据</div>
            </div>
            <div class="module-body">
              <div class="module-chart" ref="judicialChart"></div>
            </div>
            <div class="module-footer">
              <div class="module-stats">
                <div class="stat-item">
                  <span class="stat-label">裁判文书</span>
                  <span class="stat-value">{{ judicialData.judgment }}</span>
                </div>
                <div class="stat-item">
                  <span class="stat-label">法院公告</span>
                  <span class="stat-value">{{ judicialData.announcement }}</span>
                </div>
                <div class="stat-item">
                  <span class="stat-label">被执行人</span>
                  <span class="stat-value">{{ judicialData.executor }}</span>
                </div>
              </div>
            </div>
            <div class="module-action">
              <span>点击查看详情</span>
              <i class="el-icon-arrow-right"></i>
            </div>
          </div>

          <!-- 数据关联说明 -->
          <div class="relation-box">
            <div class="relation-title">
              <i class="el-icon-connection"></i>
              <span>数据关联</span>
            </div>
            <div class="relation-items">
              <div class="relation-item">
                <i class="el-icon-check"></i>
                <span>实时监控司法风险</span>
              </div>
              <div class="relation-item">
                <i class="el-icon-check"></i>
                <span>关联企业信用评级</span>
              </div>
              <div class="relation-item">
                <i class="el-icon-check"></i>
                <span>预警风险事件</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 中间：核心展示区 -->
      <div class="center-panel">
        <!-- 核心理念 -->
        <div class="core-concept">
          <div class="concept-title">数据驱动 · 智慧决策</div>
          <div class="concept-subtitle">Data-Driven Decision Making</div>
        </div>

        <!-- 数据流转图 -->
        <div class="data-flow">
          <div class="flow-node social-node">
            <div class="node-icon">
              <i class="el-icon-share"></i>
            </div>
            <div class="node-text">社会侧数据</div>
          </div>
          <div class="flow-arrow">
            <div class="arrow-line"></div>
            <i class="el-icon-d-arrow-right"></i>
          </div>
          <div class="flow-node center-node">
            <div class="node-icon">
              <i class="el-icon-data-analysis"></i>
            </div>
            <div class="node-text">数据融合</div>
            <div class="node-desc">实时分析</div>
          </div>
          <div class="flow-arrow">
            <div class="arrow-line"></div>
            <i class="el-icon-d-arrow-right"></i>
          </div>
          <div class="flow-node internal-node">
            <div class="node-icon">
              <i class="el-icon-office-building"></i>
            </div>
            <div class="node-text">内部数据</div>
          </div>
        </div>

        <!-- 数据统计图表 -->
        <div class="data-statistics">
          <div class="statistics-chart" ref="mainChart"></div>
        </div>

        <!-- 三大目标 -->
        <div class="three-goals">
          <div class="goal-item">
            <div class="goal-icon">
              <i class="el-icon-warning-outline"></i>
            </div>
            <div class="goal-text">
              <span class="goal-title">事前预防</span>
              <span class="goal-desc">风险预警与监控</span>
            </div>
          </div>
          <div class="goal-item">
            <div class="goal-icon">
              <i class="el-icon-data-analysis"></i>
            </div>
            <div class="goal-text">
              <span class="goal-title">事中监督</span>
              <span class="goal-desc">实时数据分析</span>
            </div>
          </div>
          <div class="goal-item">
            <div class="goal-icon">
              <i class="el-icon-document-checked"></i>
            </div>
            <div class="goal-text">
              <span class="goal-title">事后监督</span>
              <span class="goal-desc">全面数据审计</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧：内部数据 -->
      <div class="right-panel">
        <div class="panel-title">
          <i class="el-icon-office-building"></i>
          <span>内部数据</span>
        </div>
        <div class="panel-content">
          <!-- 内部数据模块 -->
          <div
            class="data-module internal-module"
            v-for="(item, index) in internalDataModules"
            :key="index"
            @click="navigateTo(item.path)"
          >
            <div class="module-mini-header">
              <div class="module-mini-icon">
                <i :class="item.icon"></i>
              </div>
              <div class="module-mini-title">{{ item.name }}</div>
              <div class="module-mini-arrow">
                <i class="el-icon-arrow-right"></i>
              </div>
            </div>
            <div class="module-mini-body">
              <div class="mini-stats">
                <div class="mini-stat-item">
                  <span class="mini-stat-label">{{ item.statLabel }}</span>
                  <span class="mini-stat-value">{{ item.statValue }}</span>
                </div>
              </div>
              <div class="mini-chart" :ref="`chart${index}`"></div>
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
  name: 'DataScreen',
  data() {
    return {
      currentTime: '',
      timeTimer: null,
      judicialData: {
        judgment: 156,
        announcement: 89,
        executor: 23
      },
      internalDataModules: [
        {
          name: '司库数据',
          icon: 'el-icon-coin',
          path: '/ruleMonitor/TableQuerySK',
          statLabel: '资金总额',
          statValue: '¥1.2亿'
        },
        {
          name: '合同数据',
          icon: 'el-icon-document',
          path: '/ruleMonitor/TableQueryHT',
          statLabel: '合同数量',
          statValue: '2,345'
        },
        {
          name: '财务数据',
          icon: 'el-icon-money',
          path: '/ruleMonitor/TableQueryCW',
          statLabel: '报表数量',
          statValue: '856'
        },
        {
          name: '发票数据',
          icon: 'el-icon-tickets',
          path: '/ruleMonitor/TableQueryFP',
          statLabel: '发票数量',
          statValue: '12,456'
        },
        {
          name: '人员数据',
          icon: 'el-icon-user',
          path: '/ruleMonitor/TableQueryRY',
          statLabel: '员工人数',
          statValue: '1,234'
        }
      ],
      charts: []
    }
  },
  mounted() {
    this.updateTime()
    this.timeTimer = setInterval(this.updateTime, 1000)
    this.$nextTick(() => {
      this.initJudicialChart()
      this.initMainChart()
      this.initMiniCharts()
    })
  },
  beforeDestroy() {
    if (this.timeTimer) {
      clearInterval(this.timeTimer)
    }
    this.charts.forEach(chart => {
      if (chart) chart.dispose()
    })
  },
  methods: {
    updateTime() {
      const now = new Date()
      const year = now.getFullYear()
      const month = String(now.getMonth() + 1).padStart(2, '0')
      const day = String(now.getDate()).padStart(2, '0')
      const hours = String(now.getHours()).padStart(2, '0')
      const minutes = String(now.getMinutes()).padStart(2, '0')
      const seconds = String(now.getSeconds()).padStart(2, '0')
      this.currentTime = `${year}-${month}-${day} ${hours}:${minutes}:${seconds}`
    },
    handleClose() {
      this.$emit('close')
    },
    navigateTo(path, query = {}) {
      this.$emit('close')
      // 添加来源参数，让目标页面知道是从数据画像大屏跳转过来的
      const finalQuery = {
        ...query,
        from: 'dataPortraitScreen'
      }
      this.$router.push({ path, query: finalQuery })
    },
    initJudicialChart() {
      if (!this.$refs.judicialChart) return

      const chart = echarts.init(this.$refs.judicialChart)
      this.charts.push(chart)

      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c} ({d}%)'
        },
        series: [
          {
            type: 'pie',
            radius: ['50%', '70%'],
            center: ['50%', '50%'],
            data: [
              { value: this.judicialData.judgment, name: '裁判文书' },
              { value: this.judicialData.announcement, name: '法院公告' },
              { value: this.judicialData.executor, name: '被执行人' }
            ],
            label: {
              show: false
            },
            itemStyle: {
              borderRadius: 8,
              borderColor: '#0a0e27',
              borderWidth: 2
            }
          }
        ],
        color: ['#667eea', '#764ba2', '#f093fb']
      }

      chart.setOption(option)
    },
    initMainChart() {
      if (!this.$refs.mainChart) return

      const chart = echarts.init(this.$refs.mainChart)
      this.charts.push(chart)

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
          top: '10%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          data: ['司库', '合同', '财务', '发票', '人员', '司法'],
          axisLine: {
            lineStyle: {
              color: 'rgba(255, 255, 255, 0.3)'
            }
          },
          axisLabel: {
            color: '#fff'
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
            color: '#fff'
          },
          splitLine: {
            lineStyle: {
              color: 'rgba(255, 255, 255, 0.1)'
            }
          }
        },
        series: [
          {
            name: '数据量',
            type: 'bar',
            data: [1200, 2345, 856, 12456, 1234, 268],
            itemStyle: {
              borderRadius: [8, 8, 0, 0],
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: '#667eea' },
                { offset: 1, color: '#764ba2' }
              ])
            },
            barWidth: '40%'
          }
        ]
      }

      chart.setOption(option)
    },
    initMiniCharts() {
      this.internalDataModules.forEach((item, index) => {
        const chartRef = this.$refs[`chart${index}`]
        if (!chartRef || !chartRef[0]) return

        const chart = echarts.init(chartRef[0])
        this.charts.push(chart)

        const option = {
          xAxis: {
            type: 'category',
            show: false,
            data: ['1', '2', '3', '4', '5', '6', '7']
          },
          yAxis: {
            type: 'value',
            show: false
          },
          grid: {
            left: 0,
            right: 0,
            top: 0,
            bottom: 0
          },
          series: [
            {
              data: [120, 200, 150, 80, 70, 110, 130],
              type: 'line',
              smooth: true,
              symbol: 'none',
              lineStyle: {
                color: '#f5576c',
                width: 2
              },
              areaStyle: {
                color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                  { offset: 0, color: 'rgba(245, 87, 108, 0.3)' },
                  { offset: 1, color: 'rgba(245, 87, 108, 0.05)' }
                ])
              }
            }
          ]
        }

        chart.setOption(option)
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.data-screen {
  width: 100vw;
  height: 100vh;
  background: linear-gradient(180deg, #0a0e27 0%, #1a1f3a 100%);
  color: #fff;
  display: flex;
  flex-direction: column;
  overflow: hidden;

  .screen-header {
    height: 80px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 0 40px;
    background: rgba(10, 14, 39, 0.8);
    border-bottom: 2px solid rgba(102, 126, 234, 0.3);
    position: relative;

    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      height: 2px;
      background: linear-gradient(90deg, transparent, #667eea, transparent);
    }

    .header-left {
      flex: 1;

      .current-time {
        font-size: 16px;
        color: #667eea;
        font-family: 'Courier New', monospace;
        letter-spacing: 2px;
      }
    }

    .header-title {
      flex: 2;
      text-align: center;

      .title-main {
        font-size: 36px;
        font-weight: bold;
        background: linear-gradient(90deg, #667eea, #764ba2);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        letter-spacing: 8px;
        margin-bottom: 5px;
      }

      .title-sub {
        font-size: 14px;
        color: rgba(255, 255, 255, 0.6);
        letter-spacing: 2px;
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

  .screen-body {
    flex: 1;
    display: flex;
    padding: 20px;
    gap: 20px;
    overflow: hidden;

    .panel-title {
      display: flex;
      align-items: center;
      gap: 10px;
      font-size: 18px;
      font-weight: bold;
      padding: 15px;
      background: rgba(26, 31, 58, 0.6);
      border-radius: 8px;
      margin-bottom: 15px;

      i {
        font-size: 20px;
      }
    }

    .left-panel {
      width: 280px;
      display: flex;
      flex-direction: column;

      .panel-title {
        color: #667eea;
        border-left: 4px solid #667eea;

        i {
          color: #667eea;
        }
      }

      .panel-content {
        flex: 1;
        display: flex;
        flex-direction: column;
        gap: 15px;
        overflow-y: auto;

        .judicial-module {
          background: rgba(26, 31, 58, 0.6);
          border: 2px solid rgba(102, 126, 234, 0.3);
          border-radius: 12px;
          padding: 15px;
          cursor: pointer;
          transition: all 0.3s;

          &:hover {
            border-color: #667eea;
            transform: translateY(-3px);
            box-shadow: 0 8px 20px rgba(102, 126, 234, 0.3);
          }

          .module-header {
            display: flex;
            align-items: center;
            gap: 10px;
            margin-bottom: 15px;

            .module-icon {
              width: 40px;
              height: 40px;
              display: flex;
              align-items: center;
              justify-content: center;
              background: linear-gradient(135deg, #667eea, #764ba2);
              border-radius: 8px;

              i {
                font-size: 20px;
                color: #fff;
              }
            }

            .module-title {
              font-size: 16px;
              font-weight: bold;
              color: #fff;
            }
          }

          .module-body {
            .module-chart {
              height: 150px;
            }
          }

          .module-footer {
            margin-top: 15px;

            .module-stats {
              display: flex;
              flex-direction: column;
              gap: 8px;

              .stat-item {
                display: flex;
                justify-content: space-between;
                align-items: center;
                padding: 8px 12px;
                background: rgba(102, 126, 234, 0.1);
                border-radius: 6px;

                .stat-label {
                  font-size: 13px;
                  color: rgba(255, 255, 255, 0.7);
                }

                .stat-value {
                  font-size: 16px;
                  font-weight: bold;
                  color: #667eea;
                }
              }
            }
          }

          .module-action {
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 5px;
            margin-top: 15px;
            padding: 10px;
            background: rgba(102, 126, 234, 0.2);
            border-radius: 6px;
            font-size: 13px;
            color: #667eea;
            transition: all 0.3s;

            &:hover {
              background: rgba(102, 126, 234, 0.3);
            }
          }
        }

        .relation-box {
          background: rgba(26, 31, 58, 0.6);
          border: 1px solid rgba(102, 126, 234, 0.2);
          border-radius: 12px;
          padding: 15px;

          .relation-title {
            display: flex;
            align-items: center;
            gap: 8px;
            font-size: 14px;
            font-weight: bold;
            color: #667eea;
            margin-bottom: 12px;

            i {
              font-size: 16px;
            }
          }

          .relation-items {
            display: flex;
            flex-direction: column;
            gap: 8px;

            .relation-item {
              display: flex;
              align-items: center;
              gap: 8px;
              font-size: 12px;
              color: rgba(255, 255, 255, 0.7);

              i {
                color: #67c23a;
                font-size: 14px;
              }
            }
          }
        }
      }
    }

    .center-panel {
      flex: 1;
      display: flex;
      flex-direction: column;
      gap: 15px;
      overflow-y: auto;

      .core-concept {
        text-align: center;
        padding: 20px;
        background: rgba(26, 31, 58, 0.6);
        border-radius: 12px;
        border: 2px solid rgba(102, 126, 234, 0.3);

        .concept-title {
          font-size: 24px;
          font-weight: bold;
          background: linear-gradient(90deg, #667eea, #764ba2);
          -webkit-background-clip: text;
          -webkit-text-fill-color: transparent;
          margin-bottom: 5px;
        }

        .concept-subtitle {
          font-size: 12px;
          color: rgba(255, 255, 255, 0.5);
          letter-spacing: 2px;
        }
      }

      .data-flow {
        display: flex;
        align-items: center;
        justify-content: center;
        gap: 20px;
        padding: 20px;
        background: rgba(26, 31, 58, 0.6);
        border-radius: 12px;

        .flow-node {
          display: flex;
          flex-direction: column;
          align-items: center;
          gap: 10px;
          padding: 20px;
          border-radius: 12px;
          min-width: 120px;

          &.social-node {
            background: linear-gradient(135deg, rgba(102, 126, 234, 0.2), rgba(118, 75, 162, 0.2));
            border: 2px solid #667eea;
          }

          &.center-node {
            background: linear-gradient(135deg, rgba(103, 194, 58, 0.2), rgba(230, 162, 60, 0.2));
            border: 2px solid #67c23a;
          }

          &.internal-node {
            background: linear-gradient(135deg, rgba(245, 87, 108, 0.2), rgba(240, 147, 251, 0.2));
            border: 2px solid #f5576c;
          }

          .node-icon {
            width: 50px;
            height: 50px;
            display: flex;
            align-items: center;
            justify-content: center;
            background: rgba(255, 255, 255, 0.1);
            border-radius: 50%;

            i {
              font-size: 24px;
            }
          }

          .node-text {
            font-size: 14px;
            font-weight: bold;
            color: #fff;
          }

          .node-desc {
            font-size: 11px;
            color: rgba(255, 255, 255, 0.6);
          }
        }

        .flow-arrow {
          display: flex;
          align-items: center;
          gap: 5px;

          .arrow-line {
            width: 40px;
            height: 2px;
            background: linear-gradient(90deg, #667eea, #764ba2);
          }

          i {
            font-size: 20px;
            color: #667eea;
          }
        }
      }

      .data-statistics {
        flex: 1;
        background: rgba(26, 31, 58, 0.6);
        border-radius: 12px;
        padding: 15px;
        border: 1px solid rgba(102, 126, 234, 0.2);

        .statistics-chart {
          height: 100%;
          min-height: 200px;
        }
      }

      .three-goals {
        display: grid;
        grid-template-columns: repeat(3, 1fr);
        gap: 15px;

        .goal-item {
          display: flex;
          align-items: center;
          gap: 12px;
          padding: 15px;
          background: rgba(26, 31, 58, 0.6);
          border: 1px solid rgba(102, 126, 234, 0.2);
          border-radius: 10px;
          transition: all 0.3s;

          &:hover {
            border-color: #667eea;
            transform: translateY(-3px);
            box-shadow: 0 6px 15px rgba(102, 126, 234, 0.3);
          }

          .goal-icon {
            width: 45px;
            height: 45px;
            display: flex;
            align-items: center;
            justify-content: center;
            background: linear-gradient(135deg, #667eea, #764ba2);
            border-radius: 50%;
            flex-shrink: 0;

            i {
              font-size: 22px;
              color: #fff;
            }
          }

          .goal-text {
            display: flex;
            flex-direction: column;
            gap: 4px;

            .goal-title {
              font-size: 14px;
              font-weight: bold;
              color: #fff;
            }

            .goal-desc {
              font-size: 11px;
              color: rgba(255, 255, 255, 0.6);
            }
          }
        }
      }
    }

    .right-panel {
      width: 280px;
      display: flex;
      flex-direction: column;

      .panel-title {
        color: #f5576c;
        border-right: 4px solid #f5576c;

        i {
          color: #f5576c;
        }
      }

      .panel-content {
        flex: 1;
        display: flex;
        flex-direction: column;
        gap: 12px;
        overflow-y: auto;

        .internal-module {
          background: rgba(26, 31, 58, 0.6);
          border: 2px solid rgba(245, 87, 108, 0.2);
          border-radius: 10px;
          padding: 12px;
          cursor: pointer;
          transition: all 0.3s;

          &:hover {
            border-color: #f5576c;
            transform: translateX(-3px);
            box-shadow: 0 6px 15px rgba(245, 87, 108, 0.3);
          }

          .module-mini-header {
            display: flex;
            align-items: center;
            gap: 10px;
            margin-bottom: 10px;

            .module-mini-icon {
              width: 35px;
              height: 35px;
              display: flex;
              align-items: center;
              justify-content: center;
              background: linear-gradient(135deg, #f093fb, #f5576c);
              border-radius: 8px;
              flex-shrink: 0;

              i {
                font-size: 18px;
                color: #fff;
              }
            }

            .module-mini-title {
              flex: 1;
              font-size: 14px;
              font-weight: bold;
              color: #fff;
            }

            .module-mini-arrow {
              font-size: 16px;
              color: #f5576c;
              transition: transform 0.3s;
            }
          }

          &:hover .module-mini-arrow {
            transform: translateX(3px);
          }

          .module-mini-body {
            .mini-stats {
              margin-bottom: 8px;

              .mini-stat-item {
                display: flex;
                justify-content: space-between;
                align-items: center;
                padding: 6px 10px;
                background: rgba(245, 87, 108, 0.1);
                border-radius: 6px;

                .mini-stat-label {
                  font-size: 11px;
                  color: rgba(255, 255, 255, 0.6);
                }

                .mini-stat-value {
                  font-size: 13px;
                  font-weight: bold;
                  color: #f5576c;
                }
              }
            }

            .mini-chart {
              height: 60px;
            }
          }
        }
      }
    }
  }
}

// 滚动条样式
::-webkit-scrollbar {
  width: 6px;
  height: 6px;
}

::-webkit-scrollbar-track {
  background: rgba(26, 31, 58, 0.3);
  border-radius: 3px;
}

::-webkit-scrollbar-thumb {
  background: rgba(102, 126, 234, 0.5);
  border-radius: 3px;

  &:hover {
    background: rgba(102, 126, 234, 0.7);
  }
}
</style>




