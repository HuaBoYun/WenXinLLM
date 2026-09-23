<template>
  <div class="screen-display">
    <!-- 大屏头部 -->
    <div class="screen-header">
      <div class="header-left">
        <div class="current-time">当前时间：{{ currentTime }}</div>
      </div>
      <div class="header-title">
        <div class="title-main">智能风控总览</div>
      </div>
      <div class="header-right">
        <div class="year-selector">
          <el-button size="mini" :type="selectedYear === '2018' ? 'primary' : ''" @click="selectedYear = '2018'">2018</el-button>
          <el-button size="mini" :type="selectedYear === '2017' ? 'primary' : ''" @click="selectedYear = '2017'">2017</el-button>
          <el-button size="mini" :type="selectedYear === '2016' ? 'primary' : ''" @click="selectedYear = '2016'">2016</el-button>
        </div>
        <el-button type="danger" icon="el-icon-close" circle size="small" @click="handleClose" class="close-btn"></el-button>
      </div>
    </div>

    <!-- 大屏主体内容 -->
    <div class="screen-body">
      <!-- 左侧区域 -->
      <div class="left-area">
        <!-- 成熟度评价等级 -->
        <div class="panel-box maturity-panel">
          <div class="panel-title">
            <span>成熟度评价等级</span>
            <span class="subtitle">企业数量评分统计-成熟度 2018-6-12 12:00:00</span>
          </div>
          <div ref="maturityChart" class="chart-container" style="height: 180px;"></div>
          <div class="legend-list">
            <div v-for="item in maturityList" :key="item.name" class="legend-item">
              <span class="legend-label">{{ item.name }}</span>
              <span class="legend-percent">{{ item.percent }}%</span>
              <span class="legend-value">{{ item.label }}</span>
            </div>
          </div>
        </div>

        <!-- 重大经营风险事件总体情况 -->
        <div class="panel-box stats-panel">
          <div class="panel-title">重大经营风险事件总体情况</div>
          <div class="stats-grid">
            <div class="stats-item">
              <div class="stats-label">监测事件总数</div>
              <div class="stats-value">
                <span class="number">{{ majorRiskStats.totalEvents }}</span>
                <span class="unit">件</span>
              </div>
            </div>
            <div class="stats-item">
              <div class="stats-label">已处理单位数</div>
              <div class="stats-value">
                <span class="number">{{ majorRiskStats.warningEvents }}</span>
                <span class="unit">个</span>
              </div>
            </div>
            <div class="stats-item">
              <div class="stats-label">涉及风险金额</div>
              <div class="stats-value">
                <span class="number">{{ majorRiskStats.riskAmount }}</span>
                <span class="unit">万元</span>
              </div>
            </div>
          </div>
          <div class="update-time">企业数量评分统计-成熟度 {{ majorRiskStats.updateTime }}</div>
        </div>

        <!-- 知识图谱 -->
        <div class="panel-box knowledge-panel">
          <div class="panel-title">知识图谱</div>
          <div ref="knowledgeGraphChart" class="chart-container" style="height: 300px;"></div>
          <div class="graph-subtitle">法规数量评分统计-成熟度 2018-6-12 12:00:00</div>
        </div>
      </div>

      <!-- 中间区域 -->
      <div class="center-area">
        <!-- 3D地球可视化 -->
        <div class="panel-box globe-panel">
          <div class="panel-title">
            全国数据分布
            <span class="map-tip">（鼠标滚轮缩放，拖拽移动）</span>
          </div>
          <div ref="globeChart" class="globe-container" style="height: 450px;"></div>
          <div class="globe-legend">
            <div class="legend-item">
              <span class="legend-dot" style="background: #00d4ff;"></span>
              <span class="legend-text">台风</span>
            </div>
            <div class="legend-item">
              <span class="legend-dot" style="background: #ffd700;"></span>
              <span class="legend-text">内部因</span>
            </div>
            <div class="legend-item">
              <span class="legend-dot" style="background: #00ff88;"></span>
              <span class="legend-text">外部因</span>
            </div>
          </div>
        </div>

        <!-- 审计问题整改情况 -->
        <div class="panel-box emergency-panel">
          <div class="panel-title">审计问题整改情况</div>
          <div class="emergency-grid">
            <div v-for="item in auditRectificationList" :key="item.type" class="emergency-item">
              <div class="item-header">
                <span class="item-label">{{ item.type }}</span>
                <span class="item-count">{{ item.count }}+</span>
              </div>
              <div class="item-progress">
                <el-progress
                  :percentage="item.percent"
                  :color="getProgressColor(item.percent)"
                  :show-text="false"
                ></el-progress>
              </div>
            </div>
          </div>
          <div class="update-time">企业数量评分统计-成熟度 2018-6-12 12:00:00</div>
        </div>

        <!-- 法律案件 -->
        <div class="panel-box legal-panel">
          <div class="panel-title">法律案件</div>
          <div ref="legalCasesChart" class="chart-container" style="height: 150px;"></div>
          <div class="legal-stats">
            <div v-for="item in legalCasesList" :key="item.name" class="legal-item">
              <span class="item-label">{{ item.name }}</span>
              <span class="item-value">{{ item.value }}件</span>
            </div>
          </div>
          <div class="update-time">法规数量评分统计-成熟度 2018-6-12 12:00:00</div>
        </div>
      </div>

      <!-- 右侧区域 -->
      <div class="right-area">
        <!-- 各单位划分图 -->
        <div class="panel-box unit-panel">
          <div class="panel-title">
            <span>各单位划分图</span>
            <span class="subtitle">企业数量评分统计-成熟度 2018-6-12 12:00:00</span>
          </div>
          <div class="unit-list">
            <div v-for="item in unitDivisionList" :key="item.id" class="unit-item">
              <div class="item-dot"></div>
              <div class="item-text">{{ item.company }}</div>
            </div>
          </div>
        </div>

        <!-- 重大风险事件 -->
        <div class="panel-box risk-panel">
          <div class="panel-title">重大风险事件</div>
          <div class="risk-events-list">
            <div v-for="item in riskEventsList" :key="item.id" class="risk-event-item">
              <div class="event-text">{{ item.event }}</div>
              <div class="event-company">{{ item.company }}</div>
              <div class="event-time">{{ item.time }}</div>
            </div>
          </div>
          <div class="update-time">企业数量评分统计-成熟度 2018-6-12 12:00:00</div>
        </div>

        <!-- 信用等级占比 -->
        <div class="panel-box credit-panel">
          <div class="panel-title">
            <span>信用等级占比</span>
            <span class="subtitle">企业数量评分统计-成熟度 2018-6-12 12:00:00</span>
          </div>
          <div ref="creditLevelChart" class="chart-container" style="height: 180px;"></div>
          <div class="credit-stats">
            <div v-for="item in creditLevelList" :key="item.name" class="credit-item">
              <span class="item-label">{{ item.name }}</span>
              <span class="item-percent">{{ item.percent }}%</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import { getDataMonitorData } from '../api/dataMonitor'

export default {
  name: 'ScreenDisplay',
  props: {
    monitorData: {
      type: Object,
      default: null
    }
  },
  data() {
    return {
      currentTime: '',
      timer: null,
      selectedYear: '2018',
      maturityList: [],
      majorRiskStats: {
        totalEvents: 0,
        warningEvents: 0,
        riskAmount: 0,
        updateTime: ''
      },
      legalCasesList: [],
      unitDivisionList: [],
      riskEventsList: [],
      creditLevelList: [],
      auditRectificationList: [],
      charts: {}
    }
  },
  mounted() {
    this.updateTime()
    this.timer = setInterval(this.updateTime, 1000)
    this.loadData()
  },
  beforeDestroy() {
    if (this.timer) {
      clearInterval(this.timer)
    }
    Object.values(this.charts).forEach(chart => {
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
    async loadData() {
      try {
        const data = this.monitorData || await getDataMonitorData()

        // 更新数据
        this.maturityList = data.maturityLevels || []
        this.majorRiskStats = data.majorRiskStats || {}
        this.legalCasesList = data.legalCases || []
        this.unitDivisionList = data.unitDivision || []
        this.riskEventsList = data.majorRiskEvents || []
        this.creditLevelList = data.creditLevelRatio || []
        this.auditRectificationList = data.auditRectification || []

        this.$nextTick(() => {
          this.initCharts(data)
        })
      } catch (error) {
        console.error('加载数据失败:', error)
      }
    },
    initCharts(data) {
      this.initMaturityChart(data.maturityLevels)
      this.initKnowledgeGraphChart(data.knowledgeGraph)
      this.initLegalCasesChart(data.legalCases)
      this.initGlobeChart(data.chinaMapData, data.worldMapData)
      this.initCreditLevelChart(data.creditLevelRatio)
    },
    getProgressColor(percent) {
      if (percent >= 80) return '#00d4ff'
      if (percent >= 50) return '#00ff88'
      if (percent >= 30) return '#ffd700'
      return '#ff6b6b'
    },
    handleClose() {
      this.$emit('close')
    },
    // 初始化成熟度评价等级图表
    initMaturityChart(data) {
      if (!this.$refs.maturityChart || !data) return

      const chart = echarts.init(this.$refs.maturityChart)
      this.charts.maturity = chart

      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c} ({d}%)',
          backgroundColor: 'rgba(0, 0, 0, 0.8)',
          borderColor: '#00d4ff',
          textStyle: { color: '#fff' }
        },
        legend: {
          show: false
        },
        series: [{
          type: 'pie',
          radius: ['50%', '70%'],
          center: ['50%', '50%'],
          data: data,
          label: {
            show: false
          },
          labelLine: {
            show: false
          },
          itemStyle: {
            borderRadius: 5,
            borderColor: '#0a0e27',
            borderWidth: 2
          },
          color: ['#00d4ff', '#00ff88', '#ffd700', '#ff9800', '#a78bfa'],
          emphasis: {
            scale: true,
            scaleSize: 10
          }
        }, {
          type: 'pie',
          radius: ['0%', '40%'],
          center: ['50%', '50%'],
          label: {
            show: true,
            position: 'center',
            formatter: '20%',
            fontSize: 32,
            fontWeight: 'bold',
            color: '#00d4ff'
          },
          labelLine: {
            show: false
          },
          data: [{ value: 1, itemStyle: { color: 'transparent' } }],
          silent: true
        }]
      }

      chart.setOption(option)
      window.addEventListener('resize', () => chart.resize())
    },
    // 初始化知识图谱
    initKnowledgeGraphChart(data) {
      if (!this.$refs.knowledgeGraphChart || !data) return

      const chart = echarts.init(this.$refs.knowledgeGraphChart)
      this.charts.knowledgeGraph = chart

      const option = {
        tooltip: {
          formatter: '{b}',
          backgroundColor: 'rgba(0, 0, 0, 0.8)',
          borderColor: '#00d4ff',
          textStyle: { color: '#fff' }
        },
        legend: {
          data: data.categories.map(c => c.name),
          textStyle: {
            color: '#00d4ff'
          },
          top: 10
        },
        series: [{
          type: 'graph',
          layout: 'force',
          data: data.nodes,
          links: data.links,
          categories: data.categories,
          roam: true,
          label: {
            show: true,
            position: 'right',
            formatter: '{b}',
            fontSize: 10,
            color: '#fff'
          },
          labelLayout: {
            hideOverlap: true
          },
          lineStyle: {
            color: 'source',
            curveness: 0.3,
            width: 2
          },
          itemStyle: {
            borderColor: '#fff',
            borderWidth: 1
          },
          emphasis: {
            focus: 'adjacency',
            lineStyle: {
              width: 4
            }
          },
          force: {
            repulsion: 100,
            edgeLength: [50, 100]
          },
          color: ['#00d4ff', '#00ff88', '#ffd700', '#ff9800']
        }]
      }

      chart.setOption(option)
      window.addEventListener('resize', () => chart.resize())
    },
    // 初始化法律案件图表
    initLegalCasesChart(data) {
      if (!this.$refs.legalCasesChart || !data) return

      const chart = echarts.init(this.$refs.legalCasesChart)
      this.charts.legalCases = chart

      const option = {
        tooltip: {
          trigger: 'axis',
          axisPointer: { type: 'shadow' },
          backgroundColor: 'rgba(0, 0, 0, 0.8)',
          borderColor: '#00d4ff',
          textStyle: { color: '#fff' }
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
          data: data.map(item => item.name),
          axisLabel: {
            color: '#00d4ff'
          },
          axisLine: {
            lineStyle: { color: '#00d4ff' }
          }
        },
        yAxis: {
          type: 'value',
          axisLabel: {
            color: '#00d4ff'
          },
          splitLine: {
            lineStyle: {
              color: 'rgba(0, 212, 255, 0.2)'
            }
          }
        },
        series: [{
          type: 'bar',
          data: data.map((item, index) => ({
            value: item.value,
            itemStyle: {
              color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [
                { offset: 0, color: ['#00d4ff', '#00ff88', '#ffd700'][index] },
                { offset: 1, color: ['#00d4ff', '#00ff88', '#ffd700'][index] + '80' }
              ])
            }
          })),
          barWidth: '60%'
        }]
      }

      chart.setOption(option)
      window.addEventListener('resize', () => chart.resize())
    },
    // 初始化地球图表（使用中国地图）
    async initGlobeChart(chinaData, worldData) {
      if (!this.$refs.globeChart) return

      const chart = echarts.init(this.$refs.globeChart)
      this.charts.globe = chart

      // 加载中国地图JSON数据
      try {
        const chinaMapJson = await this.loadChinaMap()
        echarts.registerMap('china', chinaMapJson)
        this.initChinaMapChart(chart, chinaData)
      } catch (error) {
        console.error('加载中国地图失败:', error)
        // 如果加载失败，使用散点图作为备选方案
        this.initGlobeScatterChart(chart, chinaData, worldData)
      }

      window.addEventListener('resize', () => chart.resize())
    },
    // 加载中国地图JSON数据
    async loadChinaMap() {
      // 从CDN加载中国地图数据
      const response = await fetch('https://geo.datav.aliyun.com/areas_v3/bound/100000_full.json')
      if (!response.ok) {
        throw new Error('Failed to load China map')
      }
      return await response.json()
    },
    // 中国地图模式
    initChinaMapChart(chart, chinaData) {
      // 准备城市数据
      const cityData = [
        { name: '北京', value: [116.4, 39.9, 177] },
        { name: '上海', value: [121.5, 31.2, 123] },
        { name: '广州', value: [113.3, 23.1, 201] },
        { name: '深圳', value: [114.1, 22.5, 189] },
        { name: '成都', value: [104.1, 30.7, 83] },
        { name: '杭州', value: [120.2, 30.3, 115] },
        { name: '武汉', value: [114.3, 30.6, 90] },
        { name: '西安', value: [108.9, 34.3, 63] },
        { name: '重庆', value: [106.5, 29.5, 55] },
        { name: '南京', value: [118.8, 32.1, 137] },
        { name: '天津', value: [117.2, 39.1, 95] },
        { name: '郑州', value: [113.6, 34.7, 72] },
        { name: '长沙', value: [112.9, 28.2, 68] },
        { name: '沈阳', value: [123.4, 41.8, 58] },
        { name: '青岛', value: [120.4, 36.1, 102] },
        { name: '济南', value: [117.0, 36.7, 65] },
        { name: '哈尔滨', value: [126.6, 45.8, 48] },
        { name: '福州', value: [119.3, 26.1, 56] },
        { name: '昆明', value: [102.7, 25.0, 52] },
        { name: '兰州', value: [103.8, 36.1, 38] }
      ]

      const option = {
        backgroundColor: 'transparent',
        tooltip: {
          trigger: 'item',
          formatter: function(params) {
            if (params.seriesType === 'effectScatter') {
              return `${params.name}<br/>数据量: ${params.value[2]}`
            } else if (params.seriesType === 'map') {
              return params.name
            }
            return params.name
          },
          backgroundColor: 'rgba(0, 0, 0, 0.8)',
          borderColor: '#00d4ff',
          textStyle: { color: '#fff' }
        },
        geo: {
          map: 'china',
          roam: true, // 启用缩放和拖拽
          scaleLimit: {
            min: 1,    // 最小缩放比例
            max: 5     // 最大缩放比例（可以放大5倍）
          },
          zoom: 1.2,
          center: [105, 36],
          label: {
            show: false,
            color: '#fff',
            fontSize: 10
          },
          itemStyle: {
            areaColor: {
              type: 'linear',
              x: 0,
              y: 0,
              x2: 0,
              y2: 1,
              colorStops: [{
                offset: 0,
                color: 'rgba(0, 212, 255, 0.3)'
              }, {
                offset: 1,
                color: 'rgba(0, 212, 255, 0.1)'
              }]
            },
            borderColor: 'rgba(0, 212, 255, 0.6)',
            borderWidth: 1,
            shadowColor: 'rgba(0, 212, 255, 0.5)',
            shadowBlur: 10
          },
          emphasis: {
            label: {
              show: true,
              color: '#fff'
            },
            itemStyle: {
              areaColor: 'rgba(0, 212, 255, 0.5)',
              borderColor: '#00d4ff',
              borderWidth: 2
            }
          }
        },
        series: [
          // 地图底图
          {
            type: 'map',
            map: 'china',
            geoIndex: 0,
            aspectScale: 0.75,
            showLegendSymbol: false,
            roam: true, // 启用缩放和拖拽
            animation: false,
            data: []
          },
          // 散点效果
          {
            type: 'effectScatter',
            coordinateSystem: 'geo',
            data: cityData,
            symbolSize: function(val) {
              return Math.max(val[2] / 5, 8)
            },
            showEffectOn: 'render',
            rippleEffect: {
              brushType: 'stroke',
              scale: 3,
              period: 4
            },
            label: {
              show: true,
              formatter: function(params) {
                // 根据缩放级别动态显示标签
                return params.name
              },
              position: 'top',
              color: '#fff',
              fontSize: 10,
              distance: 5,
              backgroundColor: 'rgba(0, 0, 0, 0.5)',
              padding: [2, 4],
              borderRadius: 2
            },
            emphasis: {
              label: {
                show: true,
                formatter: function(params) {
                  return `${params.name}\n数据量: ${params.value[2]}`
                },
                fontSize: 12,
                backgroundColor: 'rgba(0, 0, 0, 0.8)',
                padding: [4, 8],
                borderColor: '#00d4ff',
                borderWidth: 1
              },
              itemStyle: {
                color: '#ffd700',
                shadowBlur: 20,
                shadowColor: '#ffd700'
              }
            },
            itemStyle: {
              color: '#00d4ff',
              shadowBlur: 10,
              shadowColor: '#00d4ff'
            },
            zlevel: 1
          }
        ]
      }

      chart.setOption(option)
    },
    // 散点图模式（备选方案）
    initGlobeScatterChart(chart, chinaData, worldData) {
      const option = {
        backgroundColor: 'transparent',
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c}',
          backgroundColor: 'rgba(0, 0, 0, 0.8)',
          borderColor: '#00d4ff',
          textStyle: { color: '#fff' }
        },
        grid: {
          left: '10%',
          right: '10%',
          top: '10%',
          bottom: '10%',
          containLabel: true
        },
        xAxis: {
          type: 'value',
          show: false,
          min: 70,
          max: 140
        },
        yAxis: {
          type: 'value',
          show: false,
          min: 15,
          max: 55
        },
        series: [{
          type: 'effectScatter',
          coordinateSystem: 'cartesian2d',
          data: [
            { name: '北京', value: [116.4, 39.9, 177] },
            { name: '上海', value: [121.5, 31.2, 123] },
            { name: '广州', value: [113.3, 23.1, 201] },
            { name: '深圳', value: [114.1, 22.5, 189] },
            { name: '成都', value: [104.1, 30.7, 83] },
            { name: '杭州', value: [120.2, 30.3, 115] },
            { name: '武汉', value: [114.3, 30.6, 90] },
            { name: '西安', value: [108.9, 34.3, 63] },
            { name: '重庆', value: [106.5, 29.5, 55] },
            { name: '南京', value: [118.8, 32.1, 137] }
          ],
          symbolSize: function(val) {
            return Math.max(val[2] / 5, 10)
          },
          showEffectOn: 'render',
          rippleEffect: {
            brushType: 'stroke',
            scale: 3,
            period: 4
          },
          label: {
            show: true,
            formatter: '{b}',
            position: 'top',
            color: '#fff',
            fontSize: 10
          },
          itemStyle: {
            color: '#00d4ff',
            shadowBlur: 10,
            shadowColor: '#00d4ff'
          }
        }]
      }

      chart.setOption(option)
    },
    // 初始化信用等级占比图表
    initCreditLevelChart(data) {
      if (!this.$refs.creditLevelChart || !data) return

      const chart = echarts.init(this.$refs.creditLevelChart)
      this.charts.creditLevel = chart

      const option = {
        tooltip: {
          trigger: 'item',
          formatter: '{b}: {c} ({d}%)',
          backgroundColor: 'rgba(0, 0, 0, 0.8)',
          borderColor: '#00d4ff',
          textStyle: { color: '#fff' }
        },
        legend: {
          show: false
        },
        series: [{
          type: 'pie',
          radius: ['50%', '70%'],
          center: ['50%', '50%'],
          data: data,
          label: {
            show: false
          },
          labelLine: {
            show: false
          },
          itemStyle: {
            borderRadius: 5,
            borderColor: '#0a0e27',
            borderWidth: 2
          },
          color: ['#00d4ff', '#ffd700', '#00ff88'],
          emphasis: {
            scale: true,
            scaleSize: 10
          }
        }, {
          type: 'pie',
          radius: ['0%', '40%'],
          center: ['50%', '50%'],
          label: {
            show: true,
            position: 'center',
            formatter: '40%',
            fontSize: 32,
            fontWeight: 'bold',
            color: '#00d4ff'
          },
          labelLine: {
            show: false
          },
          data: [{ value: 1, itemStyle: { color: 'transparent' } }],
          silent: true
        }]
      }

      chart.setOption(option)
      window.addEventListener('resize', () => chart.resize())
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
  overflow: hidden;

  .screen-header {
    padding: 10px 20px;
    display: flex;
    align-items: center;
    justify-content: space-between;

    .header-left {
      flex: 1;

      .current-time {
        font-size: 14px;
        color: #00d4ff;
      }
    }

    .header-title {
      flex: 2;
      text-align: center;

      .title-main {
        font-size: 36px;
        font-weight: bold;
        background: linear-gradient(90deg, #00d4ff 0%, #00ff88 50%, #ffd700 100%);
        -webkit-background-clip: text;
        -webkit-text-fill-color: transparent;
        text-shadow: 0 0 20px rgba(0, 212, 255, 0.5);
      }
    }

    .header-right {
      flex: 1;
      display: flex;
      align-items: center;
      justify-content: flex-end;
      gap: 15px;

      .year-selector {
        display: flex;
        gap: 5px;
      }

      .close-btn {
        background: #ff6b6b;
        border-color: #ff6b6b;

        &:hover {
          background: #ff5252;
          border-color: #ff5252;
        }
      }
    }
  }

  .screen-body {
    flex: 1;
    display: flex;
    gap: 15px;
    padding: 0 20px 20px;
    overflow: hidden;

    .left-area,
    .center-area,
    .right-area {
      display: flex;
      flex-direction: column;
      gap: 12px;
      overflow-y: auto;

      &::-webkit-scrollbar {
        width: 6px;
      }

      &::-webkit-scrollbar-thumb {
        background: rgba(0, 212, 255, 0.3);
        border-radius: 3px;

        &:hover {
          background: rgba(0, 212, 255, 0.5);
        }
      }
    }

    .left-area {
      flex: 0 0 28%;
    }

    .center-area {
      flex: 0 0 44%;
    }

    .right-area {
      flex: 0 0 28%;
    }

    .panel-box {
      background: rgba(10, 30, 60, 0.6);
      border: 1px solid rgba(0, 212, 255, 0.3);
      border-radius: 8px;
      padding: 12px;
      backdrop-filter: blur(10px);

      &.flex-1 {
        flex: 1;
        display: flex;
        flex-direction: column;
      }

      .panel-title {
        font-size: 14px;
        font-weight: bold;
        color: #00d4ff;
        margin-bottom: 10px;
        padding-bottom: 8px;
        border-bottom: 1px solid rgba(0, 212, 255, 0.2);
        display: flex;
        align-items: center;
        justify-content: space-between;

        .map-tip {
          font-size: 10px;
          font-weight: normal;
          color: rgba(255, 255, 255, 0.5);
          margin-left: 10px;
          font-style: italic;
        }

        .subtitle {
          font-size: 11px;
          font-weight: normal;
          color: #00ff88;
        }
      }

      .chart-container {
        width: 100%;
      }
    }

    // 成熟度面板
    .maturity-panel {
      flex-shrink: 0;
    }

    // 统计面板
    .stats-panel {
      flex-shrink: 0;
    }

    // 知识图谱面板
    .knowledge-panel {
      flex-shrink: 0;
    }

    // 法律案件面板
    .legal-panel {
      flex-shrink: 0;
    }

    // 地球面板
    .globe-panel {
      flex-shrink: 0;
    }

    // 应急面板
    .emergency-panel {
      flex-shrink: 0;
    }

    // 单位面板
    .unit-panel {
      flex-shrink: 0;
    }

    // 风险事件面板
    .risk-panel {
      flex: 1;
      min-height: 0;
      display: flex;
      flex-direction: column;
    }

    // 信用等级面板
    .credit-panel {
      flex-shrink: 0;
    }

    // 图例列表
    .legend-list {
      margin-top: 10px;

      .legend-item {
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 6px 0;
        font-size: 11px;
        color: #fff;
        border-bottom: 1px solid rgba(0, 212, 255, 0.1);

        &:last-child {
          border-bottom: none;
        }

        .legend-label {
          flex: 1;
          color: #00d4ff;
        }

        .legend-percent {
          flex: 0 0 50px;
          text-align: right;
          color: #ffd700;
        }

        .legend-value {
          flex: 0 0 50px;
          text-align: right;
          color: #00ff88;
        }
      }
    }

    // 统计面板
    .stats-panel {
      .stats-grid {
        display: grid;
        grid-template-columns: 1fr;
        gap: 10px;

        .stats-item {
          background: linear-gradient(135deg, rgba(0, 212, 255, 0.2) 0%, rgba(0, 212, 255, 0.05) 100%);
          border: 1px solid rgba(0, 212, 255, 0.3);
          border-radius: 6px;
          padding: 12px;
          text-align: center;

          .stats-label {
            font-size: 11px;
            color: #00ff88;
            margin-bottom: 10px;
          }

          .stats-value {
            .number {
              font-size: 24px;
              font-weight: bold;
              color: #00d4ff;
            }

            .unit {
              font-size: 12px;
              color: #fff;
              margin-left: 5px;
            }
          }
        }
      }

      .update-time {
        margin-top: 10px;
        font-size: 11px;
        color: #00ff88;
        text-align: center;
      }
    }

    // 地球面板
    .globe-panel {
      position: relative;

      .globe-container {
        width: 100%;
      }

      .globe-legend {
        position: absolute;
        bottom: 15px;
        left: 15px;
        display: flex;
        gap: 15px;

        .legend-item {
          display: flex;
          align-items: center;
          gap: 8px;

          .legend-dot {
            width: 12px;
            height: 12px;
            border-radius: 50%;
          }

          .legend-text {
            font-size: 12px;
            color: #fff;
          }
        }
      }
    }

    // 应急面板
    .emergency-panel {
      .emergency-grid {
        display: grid;
        grid-template-columns: repeat(2, 1fr);
        gap: 12px;

        .emergency-item {
          background: linear-gradient(135deg, rgba(0, 212, 255, 0.1) 0%, rgba(0, 212, 255, 0.05) 100%);
          border: 1px solid rgba(0, 212, 255, 0.2);
          border-radius: 6px;
          padding: 12px;

          .item-header {
            display: flex;
            align-items: center;
            justify-content: space-between;
            margin-bottom: 8px;

            .item-label {
              font-size: 11px;
              color: #00d4ff;
            }

            .item-count {
              font-size: 18px;
              font-weight: bold;
              color: #ffd700;
            }
          }

          .item-progress {
            :deep(.el-progress-bar__outer) {
              background-color: rgba(0, 212, 255, 0.2);
            }
          }
        }
      }

      .update-time {
        margin-top: 10px;
        font-size: 11px;
        color: #00ff88;
        text-align: center;
      }
    }

    // 各单位划分图列表
    .unit-list {
      max-height: 150px;
      overflow-y: auto;

      &::-webkit-scrollbar {
        width: 4px;
      }

      &::-webkit-scrollbar-thumb {
        background: rgba(0, 212, 255, 0.3);
        border-radius: 2px;
      }

      .unit-item {
        display: flex;
        align-items: center;
        padding: 8px 0;
        border-bottom: 1px solid rgba(0, 212, 255, 0.1);

        &:last-child {
          border-bottom: none;
        }

        .item-dot {
          width: 6px;
          height: 6px;
          border-radius: 50%;
          background: #00d4ff;
          margin-right: 8px;
          animation: pulse 2s infinite;
        }

        .item-text {
          flex: 1;
          font-size: 11px;
          color: #fff;
        }

        @keyframes pulse {
          0%, 100% {
            opacity: 1;
            transform: scale(1);
          }
          50% {
            opacity: 0.5;
            transform: scale(1.2);
          }
        }
      }
    }

    // 风险事件列表
    .risk-events-list {
      flex: 1;
      min-height: 0;
      overflow-y: auto;

      &::-webkit-scrollbar {
        width: 4px;
      }

      &::-webkit-scrollbar-thumb {
        background: rgba(0, 212, 255, 0.3);
        border-radius: 2px;
      }

      .risk-event-item {
        padding: 10px;
        margin-bottom: 8px;
        background: linear-gradient(135deg, rgba(0, 212, 255, 0.1) 0%, rgba(0, 212, 255, 0.05) 100%);
        border: 1px solid rgba(0, 212, 255, 0.2);
        border-radius: 6px;
        transition: all 0.3s;

        &:hover {
          background: linear-gradient(135deg, rgba(0, 212, 255, 0.2) 0%, rgba(0, 212, 255, 0.1) 100%);
          border-color: rgba(0, 212, 255, 0.5);
          transform: translateX(3px);
        }

        .event-text {
          font-size: 11px;
          color: #fff;
          margin-bottom: 4px;
        }

        .event-company {
          font-size: 10px;
          color: #00d4ff;
          margin-bottom: 4px;
        }

        .event-time {
          font-size: 10px;
          color: #00ff88;
        }
      }
    }

    // 法律案件统计
    .legal-stats {
      margin-top: 10px;

      .legal-item {
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 6px 0;
        font-size: 11px;
        border-bottom: 1px solid rgba(0, 212, 255, 0.1);

        &:last-child {
          border-bottom: none;
        }

        .item-label {
          flex: 1;
          color: #00d4ff;
        }

        .item-value {
          color: #ffd700;
        }
      }
    }

    // 信用等级统计
    .credit-stats {
      margin-top: 10px;

      .credit-item {
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 6px 0;
        font-size: 11px;
        border-bottom: 1px solid rgba(0, 212, 255, 0.1);

        &:last-child {
          border-bottom: none;
        }

        .item-label {
          flex: 1;
          color: #00d4ff;
        }

        .item-percent {
          color: #ffd700;
        }
      }
    }

    // 更新时间
    .update-time {
      margin-top: 10px;
      font-size: 11px;
      color: #00ff88;
      text-align: center;
    }

    // 图谱副标题
    .graph-subtitle {
      margin-top: 8px;
      font-size: 11px;
      color: #00ff88;
      text-align: center;
    }
  }
}
</style>
