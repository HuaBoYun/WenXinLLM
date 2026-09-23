<template>
  <div class="hologram-radar-card">
    <div class="card-header">
      <div class="header-left">
        <i class="el-icon-star-on header-icon"></i>
        <span class="header-title">公司穿透360度雷达图全息画像</span>
      </div>
      <div class="header-right">
        <el-tag v-if="enterpriseTags.length > 0" v-for="tag in enterpriseTags.slice(0,3)" :key="tag.tagId"
          size="mini" type="success" effect="plain" style="margin-left:6px;">
          {{ tag.tagName }}
        </el-tag>
        <el-tag size="mini" :type="riskLevelType" effect="dark" style="margin-left:10px;">
          {{ riskLevelText }}
        </el-tag>
      </div>
    </div>

    <div class="card-body" v-loading="loading">
      <!-- 上：雷达图 -->
      <div class="radar-chart-wrap">
        <div ref="radarChart" class="radar-chart"></div>
      </div>

      <!-- 下：左右布局 -->
      <div class="radar-bottom">
        <!-- 左：综合评分 + 关键指标 -->
        <div class="bottom-left">
          <div class="overall-score">
            <div class="score-ring" :style="scoreRingStyle">
              <div class="score-inner">
                <div class="score-number">{{ overallScore }}</div>
                <div class="score-label">综合得分</div>
              </div>
            </div>
            <div class="score-grade" :class="scoreGradeClass">{{ scoreGrade }}</div>
          </div>

          <div class="key-indicators">
            <div class="ki-title">关键指标</div>
            <div class="ki-item" v-for="ki in keyIndicatorList" :key="ki.code">
              <span class="ki-label">{{ ki.name }}</span>
              <span class="ki-value" :class="ki.cls">{{ ki.value }}</span>
            </div>
          </div>
        </div>

        <!-- 右：各领域得分 -->
        <div class="bottom-right">
          <div class="domain-scores">
            <div class="ds-title">各领域得分</div>
            <div class="ds-item" v-for="d in topDomains" :key="d.key">
              <span class="ds-name">{{ d.name }}</span>
              <el-progress
                :percentage="d.score"
                :color="d.color"
                :stroke-width="6"
                :show-text="false"
                style="flex:1;margin:0 8px;"
              />
              <span class="ds-score" :style="{color: d.color}">{{ d.score }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'

const DOMAIN_COLORS = {
  invest:      '#1890FF',
  financial:   '#13C2C2',
  procurement: '#722ED1',
  military:    '#F5222D',
  overseas:    '#FA8C16',
  industry:    '#52C41A',
  contract:    '#EB2F96',
  accounting:  '#2F54EB',
  finance:     '#AD6800',
  fund:        '#006D75',
  salary:      '#531DAB',
  property:    '#237804'
}

const DOMAIN_NAMES = {
  invest: '投资', financial: '金融', procurement: '采购',
  military: '军品', overseas: '境外', industry: '行业',
  contract: '合同', accounting: '会计', finance: '财务',
  fund: '资金', salary: '薪酬', property: '产权'
}

export default {
  name: 'HologramRadarCard',
  props: {
    hologramData: { type: Object, default: null },
    radarData:    { type: Object, default: null },
    moduleStatsData: { type: Object, default: () => ({}) },
    loading: { type: Boolean, default: false }
  },
  data() {
    return { chart: null }
  },
  computed: {
    enterpriseTags() {
      return this.hologramData?.enterpriseTags || []
    },
    riskLevel() {
      return this.hologramData?.riskLevel || '1'
    },
    riskLevelType() {
      return { '1': 'success', '2': 'warning', '3': 'danger' }[this.riskLevel] || 'info'
    },
    riskLevelText() {
      return { '1': '低风险', '2': '中风险', '3': '高风险' }[this.riskLevel] || '待评估'
    },
    // 财务雷达维度数据（来自 financialRadar 接口）
    financialDimensions() {
      return this.radarData?.radarData?.dimensions || this.radarData?.dimensions || []
    },
    // 从 moduleStatsData 推导每个领域的合规得分 (0-100)
    domainScores() {
      const domains = Object.keys(DOMAIN_NAMES)
      // 财务雷达维度映射到12领域（如果有的话）
      const finDimMap = {}
      this.financialDimensions.forEach(d => {
        // 将财务维度映射到对应领域
        const mapping = {
          'PROFITABILITY': 'finance',    // 盈利能力 → 财务
          'ASSET_QUALITY': 'invest',     // 资产质量 → 投资
          'SOLVENCY': 'fund',            // 偿债能力 → 资金
          'GROWTH': 'industry',          // 经营增长 → 行业
          'SUPPLEMENT': 'accounting'     // 补充指标 → 会计
        }
        const key = mapping[d.dimension]
        if (key) finDimMap[key] = d.score
      })

      return domains.map(key => {
        // 优先使用财务雷达接口返回的得分
        if (finDimMap[key] !== undefined) {
          return { key, name: DOMAIN_NAMES[key], score: Math.round(finDimMap[key]), color: DOMAIN_COLORS[key] }
        }
        const stats = this.moduleStatsData[key] || []
        let score = 85 // 默认基础分
        // 如果有告警数据，降分
        const alertStat = stats.find(s => s.cls === 'danger')
        if (alertStat && alertStat.value !== '--') {
          const cnt = parseInt(alertStat.value) || 0
          score = Math.max(40, 95 - cnt * 5)
        }
        // 如果有合规率
        const rateStat = stats.find(s => s.cls === 'success')
        if (rateStat && rateStat.value !== '--') {
          const rate = parseFloat(rateStat.value)
          if (!isNaN(rate)) score = Math.min(100, Math.round(rate))
        }
        return { key, name: DOMAIN_NAMES[key], score, color: DOMAIN_COLORS[key] }
      })
    },
    overallScore() {
      // 优先使用 financialRadar 接口的综合得分
      const finOverall = this.radarData?.radarData?.overallScore || this.radarData?.overallScore
      if (finOverall) return Math.round(finOverall)
      const scores = this.domainScores.map(d => d.score)
      if (!scores.length) return '--'
      return Math.round(scores.reduce((a, b) => a + b, 0) / scores.length)
    },
    scoreGrade() {
      const s = this.overallScore
      if (s === '--') return '待评'
      if (s >= 90) return 'A级'
      if (s >= 80) return 'B级'
      if (s >= 70) return 'C级'
      if (s >= 60) return 'D级'
      return 'E级'
    },
    scoreGradeClass() {
      const s = this.overallScore
      if (s === '--') return ''
      if (s >= 90) return 'grade-a'
      if (s >= 80) return 'grade-b'
      if (s >= 70) return 'grade-c'
      return 'grade-d'
    },
    scoreRingStyle() {
      const s = Number(this.overallScore) || 0
      const color = s >= 90 ? '#52C41A' : s >= 75 ? '#1890FF' : s >= 60 ? '#FA8C16' : '#F5222D'
      return {
        background: `conic-gradient(${color} ${s * 3.6}deg, #eee ${s * 3.6}deg 360deg)`
      }
    },
    topDomains() {
      return [...this.domainScores].sort((a, b) => b.score - a.score)
    },
    keyIndicatorList() {
      const ki = this.hologramData?.keyIndicators || this.radarData?.keyIndicators || []
      if (ki.length) {
        return ki.slice(0, 4).map(k => ({
          code: k.code, name: k.name, value: k.value, cls: ''
        }))
      }
      return [
        { code: 'score',   name: '综合合规分', value: this.overallScore + '分', cls: 'highlight' },
        { code: 'domains', name: '监管领域数', value: '12个', cls: '' },
        { code: 'risk',    name: '活跃预警', value: this.totalAlerts + '条', cls: 'danger' },
        { code: 'rate',    name: '平均合规率', value: this.avgComplianceRate + '%', cls: 'success' }
      ]
    },
    totalAlerts() {
      return this.domainScores.reduce((sum, d) => {
        const stats = this.moduleStatsData[d.key] || []
        const s = stats.find(x => x.cls === 'danger')
        return sum + (s && s.value !== '--' ? (parseInt(s.value) || 0) : 0)
      }, 0)
    },
    avgComplianceRate() {
      const scores = this.domainScores.map(d => d.score)
      if (!scores.length) return '--'
      return (scores.reduce((a, b) => a + b, 0) / scores.length).toFixed(1)
    }
  },
  watch: {
    moduleStatsData: { deep: true, handler() { this.$nextTick(() => this.renderChart()) } },
    radarData: { deep: true, handler() { this.$nextTick(() => this.renderChart()) } },
    hologramData() { this.$nextTick(() => this.renderChart()) }
  },
  mounted() {
    this.$nextTick(() => this.renderChart())
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.handleResize)
    if (this.chart) { this.chart.dispose(); this.chart = null }
  },
  methods: {
    renderChart() {
      const el = this.$refs.radarChart
      if (!el) return
      if (!this.chart) this.chart = echarts.init(el)

      const domains = this.domainScores
      const indicators = domains.map(d => ({ name: d.name, max: 100 }))
      const values    = domains.map(d => d.score)
      // 财务雷达数据（如果有）
      const finDims   = this.radarData?.dimensions || []
      const finVals   = finDims.length ? finDims.map(d => d.score || 0) : null

      const option = {
        backgroundColor: 'transparent',
        tooltip: {
          trigger: 'item',
          backgroundColor: 'rgba(15,28,63,0.9)',
          borderColor: 'rgba(24,144,255,0.6)',
          textStyle: { color: '#fff', fontSize: 12 },
          formatter: (params) => {
            return `<b>${params.seriesName}</b><br>` + domains.map((d, i) =>
              `<span style="color:${d.color}">●</span> ${d.name}: <b>${params.value[i]}</b>`).join('<br>')
          }
        },
        legend: { show: false },
        radar: {
          indicator: indicators,
          shape: 'polygon',
          center: ['50%', '55%'],
          radius: '78%',
          startAngle: 90,
          name: {
            textStyle: { color: '#1a3a6b', fontSize: 11, fontWeight: '600' }
          },
          splitNumber: 4,
          splitArea: {
            areaStyle: {
              color: ['rgba(24,144,255,0.02)', 'rgba(24,144,255,0.05)',
                      'rgba(24,144,255,0.08)', 'rgba(24,144,255,0.12)']
            }
          },
          splitLine: {
            lineStyle: { color: 'rgba(24,144,255,0.15)', width: 1 }
          },
          axisLine: {
            lineStyle: { color: 'rgba(24,144,255,0.25)' }
          }
        },
        series: [
          {
            name: '穿透合规得分',
            type: 'radar',
            data: [{
              value: values,
              name: '穿透合规得分',
              symbol: 'circle',
              symbolSize: 5,
              areaStyle: {
                color: {
                  type: 'radial', x: 0.5, y: 0.5, r: 0.5,
                  colorStops: [
                    { offset: 0, color: 'rgba(24,144,255,0.6)' },
                    { offset: 1, color: 'rgba(24,144,255,0.1)' }
                  ]
                }
              },
              lineStyle: {
                color: '#1890FF', width: 2,
                shadowColor: 'rgba(24,144,255,0.5)', shadowBlur: 8
              },
              itemStyle: {
                color: '#fff', borderColor: '#1890FF', borderWidth: 2,
                shadowColor: 'rgba(24,144,255,0.8)', shadowBlur: 6
              }
            }],
            emphasis: {
              lineStyle: { width: 3, shadowBlur: 12 }
            }
          }
        ],
        animationDuration: 1200,
        animationEasing: 'cubicOut'
      }
      this.chart.setOption(option)
    },
    handleResize() {
      this.chart && this.chart.resize()
    }
  }
}
</script>

<style lang="scss" scoped>
.hologram-radar-card {
  background: linear-gradient(180deg, #f0f5ff 0%, #ffffff 100%);
  border-radius: 8px;
  overflow: hidden;
  height: 100%;
  display: flex;
  flex-direction: column;
  box-shadow: inset 0 0 20px rgba(24,144,255,0.03);

  .card-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 12px 16px;
    background: linear-gradient(135deg, #0d2137 0%, #1a4a8a 50%, #2a5298 100%);
    flex-shrink: 0;
    position: relative;
    overflow: hidden;

    &::after {
      content: '';
      position: absolute;
      top: -50%;
      right: -20%;
      width: 200px;
      height: 200px;
      background: radial-gradient(circle, rgba(250,173,20,0.15) 0%, transparent 70%);
      border-radius: 50%;
    }

    .header-left {
      display: flex;
      align-items: center;
      gap: 8px;
      z-index: 1;

      .header-icon {
        font-size: 20px;
        color: #FAAD14;
        animation: pulse 2s ease-in-out infinite;
      }

      .header-title {
        font-size: 15px;
        font-weight: bold;
        color: #fff;
        letter-spacing: 1px;
        text-shadow: 0 1px 3px rgba(0,0,0,0.3);
      }
    }

    .header-right {
      display: flex;
      align-items: center;
      flex-wrap: wrap;
      gap: 4px;
      z-index: 1;
    }
  }

  .card-body {
    flex: 1;
    padding: 6px 12px 10px;
    overflow: hidden;
    display: flex;
    flex-direction: column;
    gap: 6px;

    .radar-chart-wrap {
      flex: 2.5;
      min-height: 0;
      position: relative;
      padding-top: 10px;

      &::before {
        content: '';
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        width: 180px;
        height: 180px;
        background: radial-gradient(circle, rgba(24,144,255,0.06) 0%, transparent 70%);
        border-radius: 50%;
        pointer-events: none;
      }

      .radar-chart {
        width: 100%;
        height: 100%;
        min-height: 240px;
      }
    }

    .radar-bottom {
      flex: 0.8;
      min-height: 0;
      display: flex;
      gap: 8px;

      .bottom-left {
        flex: 1;
        display: flex;
        flex-direction: column;
        gap: 6px;
        overflow-y: auto;

        .overall-score {
          display: flex;
          align-items: center;
          gap: 12px;
          padding: 10px 12px;
          background: linear-gradient(135deg, #f0f7ff 0%, #e6f0ff 100%);
          border-radius: 10px;
          border: 1px solid rgba(24,144,255,0.12);
          flex-shrink: 0;

          .score-ring {
            width: 60px;
            height: 60px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            flex-shrink: 0;
            box-shadow: 0 2px 12px rgba(24,144,255,0.25);
            animation: ringGlow 3s ease-in-out infinite;

            .score-inner {
              width: 48px;
              height: 48px;
              border-radius: 50%;
              background: #fff;
              display: flex;
              flex-direction: column;
              align-items: center;
              justify-content: center;
              box-shadow: inset 0 1px 3px rgba(0,0,0,0.05);

              .score-number {
                font-size: 18px;
                font-weight: 800;
                color: #1a3a6b;
                line-height: 1;
              }

              .score-label {
                font-size: 9px;
                color: #7986cb;
                margin-top: 2px;
              }
            }
          }

          .score-grade {
            font-size: 24px;
            font-weight: 800;
            letter-spacing: 1px;

            &.grade-a { color: #52C41A; text-shadow: 0 0 8px rgba(82,196,26,0.3); }
            &.grade-b { color: #1890FF; text-shadow: 0 0 8px rgba(24,144,255,0.3); }
            &.grade-c { color: #FA8C16; text-shadow: 0 0 8px rgba(250,140,22,0.3); }
            &.grade-d { color: #F5222D; text-shadow: 0 0 8px rgba(245,34,45,0.3); }
          }
        }

        .key-indicators {
          padding: 8px 10px;
          background: linear-gradient(135deg, #fafbff 0%, #f5f7ff 100%);
          border-radius: 8px;
          border: 1px solid rgba(24,144,255,0.08);
          flex: 1;

          .ki-title {
            font-size: 11px;
            font-weight: 700;
            color: #1a3a6b;
            margin-bottom: 6px;
            padding-bottom: 4px;
            border-bottom: 1px solid rgba(24,144,255,0.1);
          }

          .ki-item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 3px 0;

            .ki-label { font-size: 11px; color: #606266; }
            .ki-value {
              font-size: 12px; font-weight: 700; color: #1a3a6b;
              &.highlight { color: #1890FF; }
              &.danger    { color: #F56C6C; }
              &.success   { color: #52C41A; }
            }
          }
        }
      }

      .bottom-right {
        flex: 1;
        overflow-y: auto;

        .domain-scores {
          padding: 8px 10px;
          background: linear-gradient(135deg, #fafbff 0%, #f5f7ff 100%);
          border-radius: 8px;
          border: 1px solid rgba(24,144,255,0.08);
          height: 100%;

          .ds-title {
            font-size: 11px;
            font-weight: 700;
            color: #1a3a6b;
            margin-bottom: 6px;
            padding-bottom: 4px;
            border-bottom: 1px solid rgba(24,144,255,0.1);
          }

          .ds-item {
            display: flex;
            align-items: center;
            margin-bottom: 3px;

            .ds-name {
              font-size: 10px;
              color: #606266;
              width: 28px;
              flex-shrink: 0;
            }

            .ds-score {
              font-size: 11px;
              font-weight: 700;
              width: 26px;
              text-align: right;
              flex-shrink: 0;
            }
          }
        }
      }
    }
  }
}

@keyframes pulse {
  0%, 100% { transform: scale(1); opacity: 1; }
  50% { transform: scale(1.1); opacity: 0.8; }
}

@keyframes ringGlow {
  0%, 100% { box-shadow: 0 2px 12px rgba(24,144,255,0.25); }
  50% { box-shadow: 0 2px 20px rgba(24,144,255,0.45); }
}
</style>
