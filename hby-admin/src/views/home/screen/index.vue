<template>
  <div class="risk-screen-container">
    <!-- 顶部标题栏 -->
    <div class="screen-header">
      <div class="header-decoration-left"></div>
      <div class="header-title">
        <div class="title-text">河北国资风险穿透</div>
        <div class="title-line"></div>
      </div>
      <div class="header-controls">
        <el-select v-model="selectedRegion" size="small" class="region-select">
          <el-option label="河北国资委" value="hebei"></el-option>
        </el-select>
        <el-button size="small" class="control-btn">查询</el-button>
        <el-button size="small" class="control-btn">全屏</el-button>
      </div>
    </div>

    <!-- 副标题 -->
    <div class="screen-subtitle">十大风险动态监测</div>

    <!-- 主内容区域 -->
    <div class="screen-content">
      <!-- 左侧区域 -->
      <div class="left-section">
        <!-- 投资风险 -->
        <div class="risk-card">
          <div class="card-title">
            <span class="title-icon"></span>
            <span>投资风险</span>
          </div>
          <div class="card-content">
            <div class="investment-tabs">
              <div class="tab active">全年投资风险率</div>
              <div class="tab">资产减值风险率</div>
            </div>
            <div class="investment-metrics">
              <div class="metric-row">
                <div class="metric-item">
                  <div class="metric-value">23<span class="unit">%</span></div>
                  <div class="metric-label">投资收益率</div>
                </div>
                <div class="metric-item">
                  <div class="metric-value">32<span class="unit">%</span></div>
                  <div class="metric-label">投资项目逾期率</div>
                </div>
                <div class="metric-item">
                  <div class="metric-value">32<span class="unit">%</span></div>
                  <div class="metric-label">投资项目超概算率</div>
                </div>
              </div>
              <div class="metric-row">
                <div class="metric-item">
                  <div class="metric-value">12<span class="unit">%</span></div>
                  <div class="metric-label">未按规划出资项目还原率</div>
                </div>
                <div class="metric-item">
                  <div class="metric-value">75.8<span class="unit">%</span></div>
                  <div class="metric-label">投资项目逾期及时率</div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 经营风险 -->
        <div class="risk-card">
          <div class="card-title">
            <span class="title-icon"></span>
            <span>经营风险</span>
          </div>
          <div class="card-content">
            <div id="operatingRiskChart" class="chart-container"></div>
            <div class="chart-legend">
              <div class="legend-item">
                <span class="legend-dot" style="background: #00D9FF"></span>
                <span>指标风险</span>
                <span class="legend-value">2家</span>
              </div>
              <div class="legend-item">
                <span class="legend-dot" style="background: #FFD700"></span>
                <span>业务模式风险</span>
                <span class="legend-value">3家</span>
              </div>
              <div class="legend-item">
                <span class="legend-dot" style="background: #00FF88"></span>
                <span>运营效率风险</span>
                <span class="legend-value">2家</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 采购风险 -->
        <div class="risk-card">
          <div class="card-title">
            <span class="title-icon"></span>
            <span>采购风险</span>
          </div>
          <div class="card-content">
            <div class="procurement-list">
              <div class="procurement-item" v-for="(item, index) in procurementRisks" :key="index">
                <span class="item-icon">▶</span>
                <span class="item-label">{{ item.name }}</span>
                <div class="item-bar">
                  <div class="bar-fill" :style="{ width: item.value + '%', background: item.color }"></div>
                </div>
                <span class="item-level" :style="{ color: item.color }">{{ item.level }}</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 中间区域 -->
      <div class="middle-section">
        <!-- 集团管控风险 -->
        <div class="risk-card">
          <div class="card-title">
            <span class="title-icon"></span>
            <span>集团管控风险</span>
          </div>
          <div class="card-content">
            <div class="control-grid">
              <div class="control-item blue">
                <div class="item-text">法人治理</div>
                <div class="item-sub">管控风险</div>
              </div>
              <div class="control-item red">
                <div class="item-text">资本运作</div>
                <div class="item-sub">风险管控</div>
              </div>
              <div class="control-item blue">
                <div class="item-text">国企改革</div>
                <div class="item-sub">改革风险</div>
              </div>
              <div class="control-item red">
                <div class="item-text">其他化债风险</div>
                <div class="item-sub">信息安全管理</div>
              </div>
            </div>
          </div>
        </div>

        <!-- 财务风险 -->
        <div class="risk-card">
          <div class="card-title">
            <span class="title-icon"></span>
            <span>财务风险</span>
          </div>
          <div class="card-content">
            <div class="finance-items">
              <div class="finance-item">
                <span>资金能力风险</span>
                <span class="badge">风险等级一般风险</span>
              </div>
              <div class="finance-item">
                <span>现金流风险</span>
                <span class="badge">风险等级一般风险</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 风险成熟度评价 -->
        <div class="risk-card">
          <div class="card-title">
            <span class="title-icon"></span>
            <span>风险成熟度评价</span>
          </div>
          <div class="card-content">
            <div class="maturity-main">
              <div class="gauge-wrapper">
                <div class="gauge-value">75%</div>
                <div class="gauge-label">风险成熟度：75%<br/>风险等级：<span class="risk-high">较高风险</span></div>
              </div>
              <div class="maturity-stats">
                <div class="stat-item">
                  <div class="stat-value green">62%</div>
                  <div class="stat-label">发生可能性出现</div>
                </div>
                <div class="stat-item">
                  <div class="stat-value blue">32%</div>
                  <div class="stat-label">影响程度出现</div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 合规与内控风险 -->
        <div class="risk-card">
          <div class="card-title">
            <span class="title-icon"></span>
            <span>合规与内控风险</span>
          </div>
          <div class="card-content">
            <div class="compliance-bars">
              <div class="bar-item">
                <span class="bar-label">行政机关关注</span>
                <div class="bar-progress">
                  <div class="bar-fill" style="width: 50%"></div>
                </div>
                <span class="bar-value">5家</span>
              </div>
              <div class="bar-item">
                <span class="bar-label">劳动用工争议</span>
                <div class="bar-progress">
                  <div class="bar-fill" style="width: 90%"></div>
                </div>
                <span class="bar-value">9家</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 审计问题整改 -->
        <div class="risk-card">
          <div class="card-title">
            <span class="title-icon"></span>
            <span>审计问题整改</span>
          </div>
          <div class="card-content">
            <div id="auditChart" class="chart-container"></div>
          </div>
        </div>
      </div>

      <!-- 右侧区域 -->
      <div class="right-section">
        <!-- 信用风险 -->
        <div class="risk-card">
          <div class="card-title">
            <span class="title-icon"></span>
            <span>信用风险</span>
          </div>
          <div class="card-content">
            <div id="creditRiskChart" class="chart-container"></div>
          </div>
        </div>

        <!-- 贸易风险 -->
        <div class="risk-card">
          <div class="card-title">
            <span class="title-icon"></span>
            <span>贸易风险</span>
          </div>
          <div class="card-content">
            <div class="trade-header">
              <span>市场与价格风险</span>
              <span class="trade-score">1.5分</span>
            </div>
            <div id="tradeRiskChart" class="chart-container"></div>
          </div>
        </div>

        <!-- 法律风险 -->
        <div class="risk-card">
          <div class="card-title">
            <span class="title-icon"></span>
            <span>法律风险</span>
          </div>
          <div class="card-content">
            <div class="legal-tabs">
              <div class="tab active">资金监管风险率</div>
              <div class="tab">知识产权风险</div>
            </div>
            <div class="legal-stats">
              <div class="stat-box">
                <div class="stat-value">761<span class="unit">件</span></div>
                <div class="stat-label">和解案件数量</div>
              </div>
              <div class="stat-box">
                <div class="stat-value">2571<span class="unit">件</span></div>
                <div class="stat-label">新增诉讼案件</div>
              </div>
              <div class="stat-box">
                <div class="stat-value">567<span class="unit">次</span></div>
                <div class="stat-label">新增被执行人</div>
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
  name: 'RiskScreen',
  data() {
    return {
      selectedRegion: 'hebei',
      procurementRisks: [
        { name: '采购合同风险', value: 85, color: '#FF4444', level: '高风险' },
        { name: '供应商风险', value: 70, color: '#FF8800', level: '较高风险' },
        { name: '质量风险', value: 55, color: '#FFD700', level: '一般风险' },
        { name: '严重风险', value: 40, color: '#00FF88', level: '低风险' },
        { name: '合规风险', value: 60, color: '#00D9FF', level: '一般风险' },
        { name: '价格风险', value: 50, color: '#00FF88', level: '低风险' }
      ],
      charts: {
        operatingRisk: null,
        creditRisk: null,
        tradeRisk: null,
        audit: null
      }
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.initCharts()
    })
  },
  beforeDestroy() {
    // 销毁图表实例
    Object.values(this.charts).forEach(chart => {
      if (chart) {
        chart.dispose()
      }
    })
  },
  methods: {
    initCharts() {
      this.initOperatingRiskChart()
      this.initCreditRiskChart()
      this.initTradeRiskChart()
      this.initAuditChart()
    },

    // 经营风险环形图
    initOperatingRiskChart() {
      const dom = document.getElementById('operatingRiskChart')
      if (!dom) return

      this.charts.operatingRisk = echarts.init(dom)
      const option = {
        backgroundColor: 'transparent',
        tooltip: {
          trigger: 'item',
          backgroundColor: 'rgba(0, 0, 0, 0.8)',
          borderColor: '#00D9FF',
          textStyle: { color: '#fff' }
        },
        legend: {
          show: false
        },
        series: [{
          type: 'pie',
          radius: ['50%', '70%'],
          center: ['50%', '50%'],
          avoidLabelOverlap: false,
          label: {
            show: false
          },
          labelLine: {
            show: false
          },
          data: [
            { value: 2, name: '指标风险', itemStyle: { color: '#00D9FF' } },
            { value: 3, name: '业务模式风险', itemStyle: { color: '#FFD700' } },
            { value: 2, name: '运营效率风险', itemStyle: { color: '#00FF88' } }
          ],
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 217, 255, 0.5)'
            }
          }
        }]
      }
      this.charts.operatingRisk.setOption(option)
    },

    // 信用风险饼图
    initCreditRiskChart() {
      const dom = document.getElementById('creditRiskChart')
      if (!dom) return

      this.charts.creditRisk = echarts.init(dom)
      const option = {
        backgroundColor: 'transparent',
        tooltip: {
          trigger: 'item',
          backgroundColor: 'rgba(0, 0, 0, 0.8)',
          borderColor: '#00D9FF',
          textStyle: { color: '#fff' }
        },
        legend: {
          orient: 'vertical',
          right: '10%',
          top: 'center',
          textStyle: {
            color: '#fff',
            fontSize: 12
          }
        },
        series: [{
          type: 'pie',
          radius: '60%',
          center: ['40%', '50%'],
          data: [
            { value: 35, name: 'AAA', itemStyle: { color: '#00FF88' } },
            { value: 25, name: 'AA', itemStyle: { color: '#00D9FF' } },
            { value: 20, name: 'A', itemStyle: { color: '#FFD700' } },
            { value: 15, name: 'BBB', itemStyle: { color: '#FF8800' } },
            { value: 5, name: 'D', itemStyle: { color: '#FF4444' } }
          ],
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 217, 255, 0.5)'
            }
          },
          label: {
            color: '#fff',
            fontSize: 12
          }
        }]
      }
      this.charts.creditRisk.setOption(option)
    },

    // 贸易风险雷达图
    initTradeRiskChart() {
      const dom = document.getElementById('tradeRiskChart')
      if (!dom) return

      this.charts.tradeRisk = echarts.init(dom)
      const option = {
        backgroundColor: 'transparent',
        tooltip: {
          backgroundColor: 'rgba(0, 0, 0, 0.8)',
          borderColor: '#00D9FF',
          textStyle: { color: '#fff' }
        },
        radar: {
          indicator: [
            { name: '价格波动', max: 100 },
            { name: '供需关系', max: 100 },
            { name: '政策风险', max: 100 },
            { name: '汇率风险', max: 100 },
            { name: '信用风险', max: 100 }
          ],
          splitArea: {
            areaStyle: {
              color: ['rgba(0, 217, 255, 0.05)', 'rgba(0, 217, 255, 0.1)']
            }
          },
          axisLine: {
            lineStyle: {
              color: 'rgba(0, 217, 255, 0.3)'
            }
          },
          splitLine: {
            lineStyle: {
              color: 'rgba(0, 217, 255, 0.3)'
            }
          },
          name: {
            textStyle: {
              color: '#fff',
              fontSize: 12
            }
          }
        },
        series: [{
          type: 'radar',
          data: [{
            value: [70, 85, 60, 75, 80],
            name: '市场风险',
            areaStyle: {
              color: 'rgba(0, 217, 255, 0.3)'
            },
            lineStyle: {
              color: '#00D9FF',
              width: 2
            },
            itemStyle: {
              color: '#00D9FF'
            }
          }]
        }]
      }
      this.charts.tradeRisk.setOption(option)
    },

    // 审计问题整改环形图
    initAuditChart() {
      const dom = document.getElementById('auditChart')
      if (!dom) return

      this.charts.audit = echarts.init(dom)
      const option = {
        backgroundColor: 'transparent',
        tooltip: {
          trigger: 'item',
          backgroundColor: 'rgba(0, 0, 0, 0.8)',
          borderColor: '#00D9FF',
          textStyle: { color: '#fff' }
        },
        legend: {
          bottom: '5%',
          left: 'center',
          textStyle: {
            color: '#fff',
            fontSize: 12
          }
        },
        series: [{
          type: 'pie',
          radius: ['40%', '60%'],
          center: ['50%', '45%'],
          avoidLabelOverlap: false,
          label: {
            show: true,
            position: 'outside',
            color: '#fff',
            fontSize: 12,
            formatter: '{b}: {c}'
          },
          labelLine: {
            show: true,
            lineStyle: {
              color: 'rgba(255, 255, 255, 0.3)'
            }
          },
          data: [
            { value: 45, name: '已整改', itemStyle: { color: '#00FF88' } },
            { value: 30, name: '整改中', itemStyle: { color: '#FFD700' } },
            { value: 25, name: '未整改', itemStyle: { color: '#FF4444' } }
          ],
          emphasis: {
            itemStyle: {
              shadowBlur: 10,
              shadowOffsetX: 0,
              shadowColor: 'rgba(0, 217, 255, 0.5)'
            }
          }
        }]
      }
      this.charts.audit.setOption(option)
    }
  }
}
</script>

<style scoped lang="scss">
.risk-screen-container {
  width: 100%;
  min-height: 100vh;
  background: #0a1e3f;
  background: linear-gradient(180deg, #0a1e3f 0%, #051429 50%, #0a1e3f 100%);
  color: #fff;
  padding: 20px;
  box-sizing: border-box;
  position: relative;
  overflow: auto;
  font-family: 'Microsoft YaHei', Arial, sans-serif;

  // 顶部标题栏
  .screen-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 15px 30px;
    background: linear-gradient(90deg, rgba(0, 217, 255, 0.1) 0%, transparent 50%, rgba(0, 217, 255, 0.1) 100%);
    border: 1px solid rgba(0, 217, 255, 0.3);
    border-radius: 4px;
    margin-bottom: 20px;
    position: relative;

    &::before,
    &::after {
      content: '';
      position: absolute;
      width: 100px;
      height: 100%;
      top: 0;
      background: linear-gradient(90deg, rgba(0, 217, 255, 0.3), transparent);
      pointer-events: none;
    }

    &::before {
      left: 0;
    }

    &::after {
      right: 0;
      transform: scaleX(-1);
    }

    .header-decoration-left {
      width: 50px;
      height: 30px;
      border-left: 3px solid #00D9FF;
      border-top: 3px solid #00D9FF;
      box-shadow: 0 0 10px rgba(0, 217, 255, 0.5);
    }

    .header-title {
      flex: 1;
      text-align: center;

      .title-text {
        font-size: 32px;
        font-weight: bold;
        background: linear-gradient(90deg, #00D9FF 0%, #00FF88 100%);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        text-shadow: 0 0 20px rgba(0, 217, 255, 0.5);
        letter-spacing: 4px;
      }

      .title-line {
        width: 200px;
        height: 2px;
        background: linear-gradient(90deg, transparent, #00D9FF, transparent);
        margin: 10px auto 0;
      }
    }

    .header-controls {
      display: flex;
      gap: 10px;
      align-items: center;

      .region-select {
        width: 150px;

        ::v-deep .el-input__inner {
          background: rgba(0, 217, 255, 0.1);
          border: 1px solid rgba(0, 217, 255, 0.5);
          color: #00D9FF;

          &:hover,
          &:focus {
            border-color: #00D9FF;
          }
        }
      }

      .control-btn {
        background: linear-gradient(135deg, rgba(0, 217, 255, 0.2), rgba(0, 217, 255, 0.1));
        border: 1px solid #00D9FF;
        color: #00D9FF;
        padding: 8px 20px;
        transition: all 0.3s;

        &:hover {
          background: linear-gradient(135deg, rgba(0, 217, 255, 0.3), rgba(0, 217, 255, 0.2));
          box-shadow: 0 0 15px rgba(0, 217, 255, 0.5);
          transform: translateY(-2px);
        }
      }
    }
  }

  // 副标题
  .screen-subtitle {
    text-align: center;
    font-size: 24px;
    font-weight: bold;
    color: #FFD700;
    margin-bottom: 30px;
    text-shadow: 0 0 10px rgba(255, 215, 0, 0.5);
    letter-spacing: 3px;
    position: relative;

    &::before,
    &::after {
      content: '◆';
      color: #FFD700;
      margin: 0 20px;
      font-size: 16px;
    }
  }

  // 主内容区域
  .screen-content {
    display: flex;
    gap: 20px;
    justify-content: space-between;
  }

  // 左中右三列布局
  .left-section,
  .middle-section,
  .right-section {
    flex: 1;
    display: flex;
    flex-direction: column;
    gap: 20px;
  }

  // 风险卡片通用样式
  .risk-card {
    background: linear-gradient(135deg, rgba(0, 50, 100, 0.4), rgba(0, 30, 60, 0.4));
    border: 1px solid rgba(0, 217, 255, 0.3);
    border-radius: 8px;
    padding: 15px;
    backdrop-filter: blur(10px);
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.5), inset 0 1px 0 rgba(255, 255, 255, 0.1);
    position: relative;
    overflow: hidden;

    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      height: 1px;
      background: linear-gradient(90deg, transparent, rgba(0, 217, 255, 0.5), transparent);
    }

    &::after {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      width: 3px;
      height: 100%;
      background: linear-gradient(180deg, #00D9FF, transparent);
      box-shadow: 0 0 10px rgba(0, 217, 255, 0.8);
    }

    .card-title {
      font-size: 16px;
      font-weight: bold;
      color: #00D9FF;
      margin-bottom: 15px;
      display: flex;
      align-items: center;
      padding-bottom: 10px;
      border-bottom: 1px solid rgba(0, 217, 255, 0.2);

      .title-icon {
        width: 8px;
        height: 8px;
        background: #00D9FF;
        border-radius: 50%;
        margin-right: 10px;
        box-shadow: 0 0 10px rgba(0, 217, 255, 0.8);
        animation: pulse 2s infinite;
      }
    }

    .card-content {
      color: #fff;
    }
  }

  @keyframes pulse {
    0%, 100% {
      box-shadow: 0 0 10px rgba(0, 217, 255, 0.8);
    }
    50% {
      box-shadow: 0 0 20px rgba(0, 217, 255, 1);
    }
  }

  // 投资风险样式
  .investment-tabs {
    display: flex;
    gap: 10px;
    margin-bottom: 20px;

    .tab {
      flex: 1;
      padding: 8px 15px;
      background: rgba(0, 217, 255, 0.1);
      border: 1px solid rgba(0, 217, 255, 0.3);
      border-radius: 4px;
      font-size: 12px;
      text-align: center;
      cursor: pointer;
      transition: all 0.3s;

      &.active {
        background: linear-gradient(135deg, rgba(0, 217, 255, 0.3), rgba(0, 217, 255, 0.2));
        border-color: #00D9FF;
        color: #00D9FF;
        box-shadow: 0 0 10px rgba(0, 217, 255, 0.5);
      }

      &:hover {
        background: rgba(0, 217, 255, 0.2);
        border-color: #00D9FF;
      }
    }
  }

  .investment-metrics {
    .metric-row {
      display: flex;
      justify-content: space-around;
      margin-bottom: 20px;

      &:last-child {
        margin-bottom: 0;
      }
    }

    .metric-item {
      text-align: center;
      flex: 1;

      .metric-value {
        font-size: 36px;
        font-weight: bold;
        color: #00D9FF;
        text-shadow: 0 0 10px rgba(0, 217, 255, 0.8);
        margin-bottom: 8px;

        .unit {
          font-size: 20px;
          margin-left: 2px;
        }
      }

      .metric-label {
        font-size: 12px;
        color: rgba(255, 255, 255, 0.7);
      }
    }
  }

  // 图表容器
  .chart-container {
    width: 100%;
    height: 200px;
    margin-bottom: 10px;
  }

  .chart-legend {
    .legend-item {
      display: flex;
      align-items: center;
      gap: 10px;
      margin-bottom: 8px;
      font-size: 12px;

      .legend-dot {
        width: 12px;
        height: 12px;
        border-radius: 50%;
        box-shadow: 0 0 8px currentColor;
      }

      .legend-value {
        margin-left: auto;
        color: #00D9FF;
        font-weight: bold;
      }
    }
  }

  // 采购风险列表
  .procurement-list {
    .procurement-item {
      display: flex;
      align-items: center;
      gap: 10px;
      margin-bottom: 15px;
      padding: 8px;
      background: rgba(0, 217, 255, 0.05);
      border-radius: 4px;
      transition: all 0.3s;

      &:hover {
        background: rgba(0, 217, 255, 0.1);
        transform: translateX(5px);
      }

      .item-icon {
        color: #00D9FF;
        font-size: 10px;
      }

      .item-label {
        width: 100px;
        font-size: 12px;
        color: rgba(255, 255, 255, 0.8);
      }

      .item-bar {
        flex: 1;
        height: 10px;
        background: rgba(255, 255, 255, 0.1);
        border-radius: 5px;
        overflow: hidden;
        position: relative;

        .bar-fill {
          height: 100%;
          border-radius: 5px;
          transition: width 0.5s;
          box-shadow: 0 0 10px currentColor;
        }
      }

      .item-level {
        width: 70px;
        text-align: right;
        font-size: 12px;
        font-weight: bold;
      }
    }
  }

  // 集团管控风险
  .control-grid {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 15px;

    .control-item {
      padding: 15px;
      border-radius: 4px;
      text-align: center;
      transition: all 0.3s;
      cursor: pointer;

      &.blue {
        background: linear-gradient(135deg, rgba(0, 217, 255, 0.2), rgba(0, 217, 255, 0.1));
        border: 1px solid rgba(0, 217, 255, 0.5);
        color: #00D9FF;

        &:hover {
          box-shadow: 0 0 20px rgba(0, 217, 255, 0.5);
          transform: translateY(-3px);
        }
      }

      &.red {
        background: linear-gradient(135deg, rgba(255, 68, 68, 0.2), rgba(255, 68, 68, 0.1));
        border: 1px solid rgba(255, 68, 68, 0.5);
        color: #FF4444;

        &:hover {
          box-shadow: 0 0 20px rgba(255, 68, 68, 0.5);
          transform: translateY(-3px);
        }
      }

      .item-text {
        font-size: 14px;
        font-weight: bold;
        margin-bottom: 5px;
      }

      .item-sub {
        font-size: 12px;
        opacity: 0.8;
      }
    }
  }

  // 财务风险
  .finance-items {
    display: flex;
    flex-direction: column;
    gap: 15px;

    .finance-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 12px;
      background: rgba(0, 217, 255, 0.05);
      border: 1px solid rgba(0, 217, 255, 0.2);
      border-radius: 4px;
      transition: all 0.3s;

      &:hover {
        background: rgba(0, 217, 255, 0.1);
        border-color: #00D9FF;
        box-shadow: 0 0 15px rgba(0, 217, 255, 0.3);
      }

      .badge {
        padding: 5px 12px;
        background: linear-gradient(135deg, rgba(0, 217, 255, 0.3), rgba(0, 217, 255, 0.2));
        border: 1px solid #00D9FF;
        border-radius: 4px;
        font-size: 12px;
        color: #00D9FF;
      }
    }
  }

  // 风险成熟度评价
  .maturity-main {
    display: flex;
    justify-content: space-around;
    align-items: center;
    margin-bottom: 20px;

    .gauge-wrapper {
      text-align: center;

      .gauge-value {
        font-size: 48px;
        font-weight: bold;
        color: #FFD700;
        text-shadow: 0 0 20px rgba(255, 215, 0, 0.8);
        margin-bottom: 10px;
      }

      .gauge-label {
        font-size: 12px;
        color: rgba(255, 255, 255, 0.8);
        line-height: 1.6;

        .risk-high {
          color: #FF4444;
          font-weight: bold;
        }
      }
    }

    .maturity-stats {
      display: flex;
      flex-direction: column;
      gap: 20px;

      .stat-item {
        text-align: center;

        .stat-value {
          font-size: 32px;
          font-weight: bold;
          margin-bottom: 5px;

          &.green {
            color: #00FF88;
            text-shadow: 0 0 10px rgba(0, 255, 136, 0.8);
          }

          &.blue {
            color: #00D9FF;
            text-shadow: 0 0 10px rgba(0, 217, 255, 0.8);
          }
        }

        .stat-label {
          font-size: 12px;
          color: rgba(255, 255, 255, 0.7);
        }
      }
    }
  }

  // 合规与内控风险
  .compliance-bars {
    .bar-item {
      display: flex;
      align-items: center;
      gap: 15px;
      margin-bottom: 20px;

      .bar-label {
        width: 100px;
        font-size: 12px;
        color: rgba(255, 255, 255, 0.8);
      }

      .bar-progress {
        flex: 1;
        height: 12px;
        background: rgba(255, 255, 255, 0.1);
        border-radius: 6px;
        overflow: hidden;
        position: relative;

        .bar-fill {
          height: 100%;
          background: linear-gradient(90deg, #00D9FF, #00FF88);
          border-radius: 6px;
          box-shadow: 0 0 10px rgba(0, 217, 255, 0.8);
          transition: width 0.5s;
        }
      }

      .bar-value {
        width: 50px;
        text-align: right;
        font-size: 14px;
        color: #00D9FF;
        font-weight: bold;
      }
    }
  }

  // 贸易风险
  .trade-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 15px;
    padding-bottom: 10px;
    border-bottom: 1px solid rgba(0, 217, 255, 0.2);

    .trade-score {
      font-size: 16px;
      font-weight: bold;
      color: #00D9FF;
    }
  }

  // 法律风险
  .legal-tabs {
    display: flex;
    gap: 10px;
    margin-bottom: 20px;

    .tab {
      flex: 1;
      padding: 8px 12px;
      background: rgba(0, 217, 255, 0.1);
      border: 1px solid rgba(0, 217, 255, 0.3);
      border-radius: 4px;
      font-size: 12px;
      text-align: center;
      cursor: pointer;
      transition: all 0.3s;

      &.active {
        background: linear-gradient(135deg, rgba(255, 215, 0, 0.3), rgba(255, 215, 0, 0.2));
        border-color: #FFD700;
        color: #FFD700;
        box-shadow: 0 0 10px rgba(255, 215, 0, 0.5);
      }

      &:hover {
        background: rgba(0, 217, 255, 0.2);
        border-color: #00D9FF;
      }
    }
  }

  .legal-stats {
    display: flex;
    justify-content: space-around;
    gap: 10px;

    .stat-box {
      flex: 1;
      text-align: center;
      padding: 20px 10px;
      background: linear-gradient(135deg, rgba(0, 217, 255, 0.1), rgba(0, 217, 255, 0.05));
      border: 1px solid rgba(0, 217, 255, 0.3);
      border-radius: 4px;
      transition: all 0.3s;

      &:hover {
        background: linear-gradient(135deg, rgba(0, 217, 255, 0.2), rgba(0, 217, 255, 0.1));
        border-color: #00D9FF;
        box-shadow: 0 0 15px rgba(0, 217, 255, 0.5);
        transform: translateY(-5px);
      }

      .stat-value {
        font-size: 32px;
        font-weight: bold;
        color: #00D9FF;
        text-shadow: 0 0 10px rgba(0, 217, 255, 0.8);
        margin-bottom: 10px;

        .unit {
          font-size: 16px;
          margin-left: 2px;
        }
      }

      .stat-label {
        font-size: 12px;
        color: rgba(255, 255, 255, 0.7);
      }
    }
  }
}
</style>
