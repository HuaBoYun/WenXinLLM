<template>
  <div class="screen-display" style="width: 100vw; height: 100vh; background: linear-gradient(180deg, #0a0e27 0%, #1a1f3a 100%); color: #fff; display: flex; flex-direction: column; position: relative; overflow: hidden;">
    <!-- 大屏头部 -->
    <div class="screen-header" style="height: 60px; display: flex; align-items: center; justify-content: space-between; padding: 0 30px; background: rgba(10, 14, 39, 0.8); border-bottom: 1px solid rgba(64, 158, 255, 0.2);">
      <div class="header-left" style="flex: 1;">
        <div class="current-time" style="font-size: 14px; color: #409eff; font-family: 'Courier New', monospace;">{{ currentTime }}</div>
      </div>
      <div class="header-title" style="flex: 2; text-align: center;">
        <div class="title-main" style="font-size: 24px; font-weight: bold; color: #409eff; letter-spacing: 4px; margin-bottom: 2px;">国资风险穿透</div>
        <div class="title-sub" style="font-size: 14px; color: #67c23a; letter-spacing: 2px;">十大风险动态监测</div>
      </div>
      <div class="header-right" style="flex: 1; display: flex; justify-content: flex-end;">
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
    <div class="screen-body" style="flex: 1; display: flex; padding: 20px; gap: 20px; position: relative;">
      <!-- 左侧区域 -->
      <div class="left-area" style="flex: 1; display: flex; flex-direction: column; gap: 15px;">
        <!-- 投资风险 -->
        <div class="panel-box" style="background: rgba(26, 31, 58, 0.6); border: 1px solid rgba(64, 158, 255, 0.3); border-radius: 8px; padding: 15px;">
          <div class="panel-title" style="font-size: 16px; font-weight: bold; color: #409eff; margin-bottom: 15px; padding-left: 10px; border-left: 3px solid #409eff;">投资风险</div>
          <div style="display: grid; grid-template-columns: repeat(3, 1fr); gap: 15px; margin-bottom: 15px;">
            <div style="text-align: center;">
              <div style="font-size: 24px; font-weight: bold; color: #409eff;">23<span style="font-size: 14px;">%</span></div>
              <div style="font-size: 12px; color: rgba(255,255,255,0.7); margin-top: 5px;">投资收益率</div>
            </div>
            <div style="text-align: center;">
              <div style="font-size: 24px; font-weight: bold; color: #409eff;">32<span style="font-size: 14px;">%</span></div>
              <div style="font-size: 12px; color: rgba(255,255,255,0.7); margin-top: 5px;">投资回报企业率</div>
            </div>
            <div style="text-align: center;">
              <div style="font-size: 24px; font-weight: bold; color: #409eff;">32<span style="font-size: 14px;">%</span></div>
              <div style="font-size: 12px; color: rgba(255,255,255,0.7); margin-top: 5px;">投资项目参股率</div>
            </div>
          </div>
          <div style="display: grid; grid-template-columns: repeat(2, 1fr); gap: 15px;">
            <div style="text-align: center;">
              <div style="font-size: 24px; font-weight: bold; color: #67c23a;">12<span style="font-size: 14px;">%</span></div>
              <div style="font-size: 12px; color: rgba(255,255,255,0.7); margin-top: 5px;">投资项目回收率</div>
            </div>
            <div style="text-align: center;">
              <div style="font-size: 24px; font-weight: bold; color: #67c23a;">75.8<span style="font-size: 14px;">%</span></div>
              <div style="font-size: 12px; color: rgba(255,255,255,0.7); margin-top: 5px;">投资资金到位及时率</div>
            </div>
          </div>
        </div>

        <!-- 经营风险 -->
        <div class="panel-box" style="background: rgba(26, 31, 58, 0.6); border: 1px solid rgba(64, 158, 255, 0.3); border-radius: 8px; padding: 15px; flex: 1;">
          <div class="panel-title" style="font-size: 16px; font-weight: bold; color: #409eff; margin-bottom: 15px; padding-left: 10px; border-left: 3px solid #409eff;">经营风险</div>
          <div ref="businessRiskChart" style="height: 200px;"></div>
        </div>

        <!-- 采购风险 -->
        <div class="panel-box" style="background: rgba(26, 31, 58, 0.6); border: 1px solid rgba(64, 158, 255, 0.3); border-radius: 8px; padding: 15px;">
          <div class="panel-title" style="font-size: 16px; font-weight: bold; color: #409eff; margin-bottom: 15px; padding-left: 10px; border-left: 3px solid #409eff;">采购风险</div>
          <div style="display: flex; flex-direction: column; gap: 10px;">
            <div v-for="item in purchaseRisks" :key="item.name" style="display: flex; align-items: center; gap: 10px;">
              <div style="width: 80px; font-size: 12px; color: rgba(255,255,255,0.8);">{{ item.name }}</div>
              <div style="flex: 1; height: 20px; background: rgba(0,0,0,0.3); border-radius: 10px; overflow: hidden;">
                <div :style="`width: ${item.value}%; height: 100%; background: ${item.color}; transition: width 0.3s;`"></div>
              </div>
              <div :style="`width: 60px; text-align: right; font-size: 12px; color: ${item.color};`">{{ item.level }}</div>
            </div>
          </div>
        </div>
      </div>

      <!-- 中间区域 -->
      <div class="center-area" style="flex: 1.2; display: flex; flex-direction: column; gap: 15px;">
        <!-- 集团管控风险 -->
        <div class="panel-box" style="background: rgba(26, 31, 58, 0.6); border: 1px solid rgba(64, 158, 255, 0.3); border-radius: 8px; padding: 15px;">
          <div class="panel-title" style="font-size: 16px; font-weight: bold; color: #409eff; margin-bottom: 10px; text-align: center;">集团管控风险</div>
          <div style="display: flex; justify-content: space-around; align-items: center;">
            <div style="text-align: center;">
              <div style="font-size: 14px; color: rgba(255,255,255,0.7); margin-bottom: 5px;">流入企业</div>
              <div style="font-size: 20px; font-weight: bold; color: #409eff;">基本监管</div>
            </div>
            <div style="width: 80px; height: 80px; border-radius: 50%; border: 3px solid #409eff; display: flex; align-items: center; justify-content: center; background: rgba(64, 158, 255, 0.1);">
              <div style="text-align: center;">
                <div style="font-size: 12px; color: #67c23a;">管控</div>
                <div style="font-size: 16px; font-weight: bold; color: #67c23a;">风险</div>
              </div>
            </div>
            <div style="text-align: center;">
              <div style="font-size: 14px; color: rgba(255,255,255,0.7); margin-bottom: 5px;">资产化债或信息支撑</div>
              <div style="font-size: 20px; font-weight: bold; color: #e6a23c;">国企改革</div>
            </div>
          </div>
        </div>

        <!-- 风险成熟度评价 -->
        <div class="panel-box" style="background: rgba(26, 31, 58, 0.6); border: 1px solid rgba(64, 158, 255, 0.3); border-radius: 8px; padding: 15px; flex: 1;">
          <div class="panel-title" style="font-size: 16px; font-weight: bold; color: #409eff; margin-bottom: 10px; text-align: center;">风险成熟度评价</div>
          <div style="display: flex; align-items: center; justify-content: center; gap: 30px;">
            <div ref="maturityChart" style="width: 200px; height: 200px;"></div>
            <div style="display: flex; flex-direction: column; gap: 15px;">
              <div style="text-align: center;">
                <div style="font-size: 14px; color: rgba(255,255,255,0.7);">风险成熟度：<span style="color: #e6a23c; font-weight: bold;">75%</span></div>
                <div style="font-size: 12px; color: #e6a23c; margin-top: 5px;">风险等级：较高风险</div>
              </div>
              <div style="display: flex; gap: 20px;">
                <div style="text-align: center;">
                  <div style="font-size: 20px; font-weight: bold; color: #67c23a;">62%</div>
                  <div style="font-size: 12px; color: rgba(255,255,255,0.7); margin-top: 3px;">业主司机比</div>
                </div>
                <div style="text-align: center;">
                  <div style="font-size: 20px; font-weight: bold; color: #409eff;">32%</div>
                  <div style="font-size: 12px; color: rgba(255,255,255,0.7); margin-top: 3px;">车辆复盘占比</div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 合规与内控风险 -->
        <div class="panel-box" style="background: rgba(26, 31, 58, 0.6); border: 1px solid rgba(64, 158, 255, 0.3); border-radius: 8px; padding: 15px;">
          <div class="panel-title" style="font-size: 16px; font-weight: bold; color: #409eff; margin-bottom: 10px; text-align: center;">合规与内控风险</div>
          <div style="display: flex; justify-content: space-around;">
            <div style="text-align: center;">
              <div style="font-size: 12px; color: rgba(255,255,255,0.7); margin-bottom: 5px;">行政机关处罚</div>
              <div style="font-size: 20px; font-weight: bold; color: #409eff;">5<span style="font-size: 12px;">件</span></div>
            </div>
            <div style="text-align: center;">
              <div style="font-size: 12px; color: rgba(255,255,255,0.7); margin-bottom: 5px;">劳动用工争议</div>
              <div style="font-size: 20px; font-weight: bold; color: #409eff;">9<span style="font-size: 12px;">件</span></div>
            </div>
          </div>
        </div>

        <!-- 审计问题整改 -->
        <div class="panel-box" style="background: rgba(26, 31, 58, 0.6); border: 1px solid rgba(64, 158, 255, 0.3); border-radius: 8px; padding: 15px;">
          <div class="panel-title" style="font-size: 16px; font-weight: bold; color: #409eff; margin-bottom: 10px; text-align: center;">审计问题整改</div>
          <div ref="auditChart" style="height: 150px;"></div>
        </div>
      </div>

      <!-- 右侧区域 -->
      <div class="right-area" style="flex: 1; display: flex; flex-direction: column; gap: 15px;">
        <!-- 信用风险 -->
        <div class="panel-box" style="background: rgba(26, 31, 58, 0.6); border: 1px solid rgba(64, 158, 255, 0.3); border-radius: 8px; padding: 15px;">
          <div class="panel-title" style="font-size: 16px; font-weight: bold; color: #409eff; margin-bottom: 15px; padding-left: 10px; border-left: 3px solid #409eff;">信用风险</div>
          <div style="display: flex; justify-content: space-around; margin-bottom: 15px;">
            <div style="text-align: center;">
              <div style="font-size: 12px; color: rgba(255,255,255,0.7); margin-bottom: 8px;">资质能力风险</div>
              <div style="width: 60px; height: 60px; margin: 0 auto; border-radius: 50%; border: 3px solid #409eff; display: flex; align-items: center; justify-content: center; background: rgba(64, 158, 255, 0.1);">
                <div style="font-size: 18px; font-weight: bold; color: #409eff;">AAA级</div>
              </div>
              <div style="font-size: 12px; color: #67c23a; margin-top: 5px;">3家</div>
            </div>
            <div style="text-align: center;">
              <div style="font-size: 12px; color: rgba(255,255,255,0.7); margin-bottom: 8px;">信用内控</div>
              <div style="width: 60px; height: 60px; margin: 0 auto; border-radius: 50%; border: 3px solid #67c23a; display: flex; align-items: center; justify-content: center; background: rgba(103, 194, 58, 0.1);">
                <div style="font-size: 18px; font-weight: bold; color: #67c23a;">AA级</div>
              </div>
              <div style="font-size: 12px; color: #67c23a; margin-top: 5px;">2家</div>
            </div>
          </div>
          <div ref="creditPieChart" style="height: 120px;"></div>
        </div>

        <!-- 贸易风险 -->
        <div class="panel-box" style="background: rgba(26, 31, 58, 0.6); border: 1px solid rgba(64, 158, 255, 0.3); border-radius: 8px; padding: 15px; flex: 1;">
          <div class="panel-title" style="font-size: 16px; font-weight: bold; color: #409eff; margin-bottom: 15px; padding-left: 10px; border-left: 3px solid #409eff;">贸易风险</div>
          <div ref="tradeRadarChart" style="height: 200px;"></div>
          <div style="display: flex; justify-content: space-around; margin-top: 10px;">
            <div style="text-align: center;">
              <div style="font-size: 12px; color: rgba(255,255,255,0.7);">市场与合规风险</div>
              <div style="font-size: 16px; font-weight: bold; color: #e6a23c; margin-top: 3px;">1.5分</div>
            </div>
            <div style="text-align: center;">
              <div style="font-size: 12px; color: rgba(255,255,255,0.7);">运营与物流风险</div>
              <div style="font-size: 16px; font-weight: bold; color: #67c23a; margin-top: 3px;">2.1分</div>
            </div>
          </div>
        </div>

        <!-- 法律风险 -->
        <div class="panel-box" style="background: rgba(26, 31, 58, 0.6); border: 1px solid rgba(64, 158, 255, 0.3); border-radius: 8px; padding: 15px;">
          <div class="panel-title" style="font-size: 16px; font-weight: bold; color: #409eff; margin-bottom: 15px; padding-left: 10px; border-left: 3px solid #409eff;">法律风险</div>
          <div style="display: grid; grid-template-columns: repeat(2, 1fr); gap: 15px;">
            <div style="text-align: center; padding: 15px; background: rgba(0,0,0,0.2); border-radius: 6px;">
              <div style="font-size: 12px; color: rgba(255,255,255,0.7); margin-bottom: 5px;">诉讼风险预警</div>
              <div style="font-size: 20px; font-weight: bold; color: #f56c6c;">761<span style="font-size: 12px;">件</span></div>
            </div>
            <div style="text-align: center; padding: 15px; background: rgba(0,0,0,0.2); border-radius: 6px;">
              <div style="font-size: 12px; color: rgba(255,255,255,0.7); margin-bottom: 5px;">税收优惠风险</div>
              <div style="font-size: 20px; font-weight: bold; color: #e6a23c;">2571<span style="font-size: 12px;">件</span></div>
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
    allRiskData: {
      type: Object,
      default: () => ({})
    }
  },
  data() {
    return {
      currentTime: '',
      timeTimer: null,
      maturityChart: null,
      businessRiskChart: null,
      auditChart: null,
      creditPieChart: null,
      tradeRadarChart: null,
      purchaseRisks: [
        { name: '采购流程风险', value: 85, level: '高风险', color: '#f56c6c' },
        { name: '供应商风险', value: 65, level: '较高风险', color: '#e6a23c' },
        { name: '质量风险', value: 45, level: '一般风险', color: '#409eff' },
        { name: '成本风险', value: 55, level: '一般风险', color: '#409eff' },
        { name: '合规风险', value: 35, level: '低风险', color: '#67c23a' },
        { name: '库存风险', value: 25, level: '低风险', color: '#67c23a' }
      ]
    }
  },
  computed: {
    safeData() {
      return {
        overview: this.allRiskData?.overview || {},
        investment: this.allRiskData?.investment || {},
        groupControl: this.allRiskData?.groupControl || {},
        financial: this.allRiskData?.financial || {},
        credit: this.allRiskData?.credit || {},
        business: this.allRiskData?.business || {},
        procurement: this.allRiskData?.procurement || {},
        legal: this.allRiskData?.legal || {},
        trade: this.allRiskData?.trade || {}
      }
    }
  },
  mounted() {
    console.log('ScreenDisplay mounted, allRiskData:', this.allRiskData)
    this.updateTime()
    this.timeTimer = setInterval(this.updateTime, 1000)
    this.$nextTick(() => {
      this.initAllCharts()
    })
  },
  watch: {
    allRiskData: {
      deep: true,
      handler(newVal) {
        console.log('allRiskData changed:', newVal)
      }
    }
  },
  beforeDestroy() {
    if (this.timeTimer) {
      clearInterval(this.timeTimer)
    }
    // 销毁所有图表
    const charts = [this.maturityChart, this.businessRiskChart, this.auditChart, this.creditPieChart, this.tradeRadarChart]
    charts.forEach(chart => {
      if (chart) {
        chart.dispose()
      }
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
    initAllCharts() {
      this.initMaturityChart()
      this.initBusinessRiskChart()
      this.initAuditChart()
      this.initCreditPieChart()
      this.initTradeRadarChart()
    },
    initMaturityChart() {
      if (!this.$refs.maturityChart) return
      this.maturityChart = echarts.init(this.$refs.maturityChart)

      const option = {
        series: [{
          type: 'gauge',
          startAngle: 180,
          endAngle: 0,
          min: 0,
          max: 100,
          splitNumber: 4,
          axisLine: {
            lineStyle: {
              width: 15,
              color: [
                [0.25, '#67c23a'],
                [0.5, '#409eff'],
                [0.75, '#e6a23c'],
                [1, '#f56c6c']
              ]
            }
          },
          pointer: {
            length: '60%',
            width: 6,
            itemStyle: { color: '#409eff' }
          },
          axisTick: {
            length: 6,
            lineStyle: { color: 'auto', width: 1 }
          },
          splitLine: {
            length: 10,
            lineStyle: { color: 'auto', width: 2 }
          },
          axisLabel: {
            color: '#fff',
            fontSize: 10,
            distance: -35,
            formatter: '{value}%'
          },
          title: {
            offsetCenter: [0, '20%'],
            fontSize: 12,
            color: '#409eff'
          },
          detail: {
            fontSize: 20,
            offsetCenter: [0, '0%'],
            valueAnimation: true,
            formatter: '{value}%',
            color: '#e6a23c'
          },
          data: [{ value: 75, name: '成熟度' }]
        }]
      }

      this.maturityChart.setOption(option)
    },
    initBusinessRiskChart() {
      if (!this.$refs.businessRiskChart) return
      this.businessRiskChart = echarts.init(this.$refs.businessRiskChart)

      const option = {
        tooltip: {
          trigger: 'item',
          backgroundColor: 'rgba(0,0,0,0.7)',
          borderColor: '#409eff',
          textStyle: { color: '#fff' }
        },
        legend: {
          bottom: '5%',
          left: 'center',
          textStyle: { color: '#fff', fontSize: 10 }
        },
        series: [{
          name: '经营风险',
          type: 'pie',
          radius: ['40%', '60%'],
          center: ['50%', '45%'],
          avoidLabelOverlap: false,
          itemStyle: {
            borderRadius: 5,
            borderColor: '#0a0e27',
            borderWidth: 2
          },
          label: { show: false },
          emphasis: {
            label: {
              show: true,
              fontSize: 12,
              fontWeight: 'bold',
              color: '#fff'
            }
          },
          labelLine: { show: false },
          data: [
            { value: 35, name: '业务增长风险', itemStyle: { color: '#67c23a' } },
            { value: 25, name: '组织效率风险', itemStyle: { color: '#409eff' } }
          ]
        }]
      }

      this.businessRiskChart.setOption(option)
    },
    initAuditChart() {
      if (!this.$refs.auditChart) return
      this.auditChart = echarts.init(this.$refs.auditChart)

      const option = {
        tooltip: {
          trigger: 'item',
          backgroundColor: 'rgba(0,0,0,0.7)',
          borderColor: '#409eff',
          textStyle: { color: '#fff' }
        },
        legend: {
          bottom: '0%',
          left: 'center',
          textStyle: { color: '#fff', fontSize: 10 }
        },
        series: [{
          name: '审计问题',
          type: 'pie',
          radius: ['35%', '55%'],
          center: ['50%', '40%'],
          avoidLabelOverlap: false,
          itemStyle: {
            borderRadius: 5,
            borderColor: '#0a0e27',
            borderWidth: 2
          },
          label: {
            show: true,
            position: 'outside',
            fontSize: 10,
            color: '#fff',
            formatter: '{d}%'
          },
          labelLine: {
            show: true,
            length: 10,
            length2: 5
          },
          data: [
            { value: 5, name: '本期行三重一大', itemStyle: { color: '#f56c6c' } },
            { value: 6, name: '合同问题', itemStyle: { color: '#e6a23c' } },
            { value: 7, name: '财务问题', itemStyle: { color: '#409eff' } }
          ]
        }]
      }

      this.auditChart.setOption(option)
    },
    initCreditPieChart() {
      if (!this.$refs.creditPieChart) return
      this.creditPieChart = echarts.init(this.$refs.creditPieChart)

      const option = {
        tooltip: {
          trigger: 'item',
          backgroundColor: 'rgba(0,0,0,0.7)',
          borderColor: '#409eff',
          textStyle: { color: '#fff' }
        },
        series: [{
          name: '信用等级',
          type: 'pie',
          radius: ['30%', '50%'],
          center: ['50%', '50%'],
          avoidLabelOverlap: false,
          itemStyle: {
            borderRadius: 5,
            borderColor: '#0a0e27',
            borderWidth: 2
          },
          label: {
            show: true,
            position: 'outside',
            fontSize: 10,
            color: '#fff',
            formatter: '{b}\n{d}%'
          },
          labelLine: {
            show: true,
            length: 8,
            length2: 5
          },
          data: [
            { value: 3, name: 'AAA级', itemStyle: { color: '#409eff' } },
            { value: 2, name: 'AA级', itemStyle: { color: '#67c23a' } },
            { value: 1, name: 'A级', itemStyle: { color: '#e6a23c' } }
          ]
        }]
      }

      this.creditPieChart.setOption(option)
    },
    initTradeRadarChart() {
      if (!this.$refs.tradeRadarChart) return
      this.tradeRadarChart = echarts.init(this.$refs.tradeRadarChart)

      const option = {
        tooltip: {
          backgroundColor: 'rgba(0,0,0,0.7)',
          borderColor: '#409eff',
          textStyle: { color: '#fff' }
        },
        radar: {
          indicator: [
            { name: '市场与合规风险', max: 5 },
            { name: '运营与物流风险', max: 5 },
            { name: '建设与物流风险', max: 5 },
            { name: '运输与物流风险', max: 5 },
            { name: '法律与物流风险', max: 5 }
          ],
          shape: 'polygon',
          splitNumber: 4,
          name: {
            textStyle: {
              color: '#fff',
              fontSize: 10
            }
          },
          splitLine: {
            lineStyle: {
              color: 'rgba(255, 255, 255, 0.2)'
            }
          },
          splitArea: {
            show: true,
            areaStyle: {
              color: ['rgba(64, 158, 255, 0.1)', 'rgba(64, 158, 255, 0.05)']
            }
          },
          axisLine: {
            lineStyle: {
              color: 'rgba(255, 255, 255, 0.3)'
            }
          }
        },
        series: [{
          name: '贸易风险',
          type: 'radar',
          data: [{
            value: [1.5, 2.1, 1.8, 1.9, 1.6],
            name: '风险评分',
            areaStyle: {
              color: 'rgba(64, 158, 255, 0.3)'
            },
            lineStyle: {
              color: '#409eff',
              width: 2
            },
            itemStyle: {
              color: '#409eff'
            }
          }]
        }]
      }

      this.tradeRadarChart.setOption(option)
    }
  }
}
</script>

<style lang="scss" scoped>
.screen-display {
  width: 100vw !important;
  height: 100vh !important;
  background: linear-gradient(180deg, #0a0e27 0%, #1a1f3a 100%) !important;
  position: relative !important;
  overflow: hidden !important;
  color: #fff !important;
  display: flex !important;
  flex-direction: column !important;

  // 大屏头部
  .screen-header {
    height: 60px !important;
    display: flex !important;
    align-items: center !important;
    justify-content: space-between !important;
    padding: 0 30px !important;
    background: rgba(10, 14, 39, 0.8) !important;
    border-bottom: 1px solid rgba(64, 158, 255, 0.2) !important;
    flex-shrink: 0 !important;

    .header-left {
      flex: 1 !important;

      .current-time {
        font-size: 14px !important;
        color: #409eff !important;
        font-family: 'Courier New', monospace !important;
      }
    }

    .header-title {
      flex: 2 !important;
      text-align: center !important;

      .title-main {
        font-size: 24px !important;
        font-weight: bold !important;
        color: #409eff !important;
        letter-spacing: 4px !important;
        margin-bottom: 2px !important;
      }

      .title-sub {
        font-size: 14px !important;
        color: #67c23a !important;
        letter-spacing: 2px !important;
      }
    }

    .header-right {
      flex: 1 !important;
      display: flex !important;
      justify-content: flex-end !important;

      .close-btn {
        background: rgba(245, 108, 108, 0.2) !important;
        border-color: #f56c6c !important;
        color: #f56c6c !important;
        transition: all 0.3s ease !important;

        &:hover {
          background: #f56c6c !important;
          color: white !important;
        }
      }
    }
  }

  // 大屏主体
  .screen-body {
    flex: 1 !important;
    display: flex !important;
    flex-direction: column !important;
    padding: 20px 160px !important;
    gap: 15px !important;
    position: relative !important;

    // 上半部分：风险预警统计
    .top-section {
      .stats-row {
        display: grid !important;
        grid-template-columns: repeat(4, 1fr) !important;
        gap: 15px !important;

        .stat-card {
          display: flex !important;
          align-items: center !important;
          gap: 15px !important;
          padding: 20px !important;
          border-radius: 8px !important;
          border: 2px solid !important;
          transition: all 0.3s ease !important;

          &.critical {
            background: rgba(245, 108, 108, 0.1);
            border-color: #f56c6c;

            .stat-icon {
              color: #f56c6c;
            }
          }

          &.high {
            background: rgba(230, 162, 60, 0.1);
            border-color: #e6a23c;

            .stat-icon {
              color: #e6a23c;
            }
          }

          &.medium {
            background: rgba(64, 158, 255, 0.1);
            border-color: #409eff;

            .stat-icon {
              color: #409eff;
            }
          }

          &.low {
            background: rgba(103, 194, 58, 0.1);
            border-color: #67c23a;

            .stat-icon {
              color: #67c23a;
            }
          }

          .stat-icon {
            font-size: 32px;
          }

          .stat-info {
            flex: 1;

            .stat-value {
              font-size: 32px;
              font-weight: bold;
              color: #fff;
              line-height: 1;
              margin-bottom: 5px;
            }

            .stat-label {
              font-size: 14px;
              color: rgba(255, 255, 255, 0.7);
            }
          }
        }
      }
    }

    // 中间部分：趋势图
    .middle-section {
      flex: 1;
      background: rgba(26, 31, 58, 0.6);
      border: 1px solid rgba(64, 158, 255, 0.3);
      border-radius: 8px;
      padding: 15px;
      display: flex;
      flex-direction: column;

      .section-title {
        font-size: 16px;
        font-weight: bold;
        color: #409eff;
        text-align: center;
        margin-bottom: 10px;
      }

      .trend-chart {
        flex: 1;
        min-height: 0;
      }
    }

    // 底部部分：风险卡片
    .bottom-section {
      display: grid;
      grid-template-columns: repeat(3, 1fr);
      gap: 15px;

      .risk-card {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;
        padding: 20px;
        background: rgba(26, 31, 58, 0.6);
        border: 2px solid rgba(64, 158, 255, 0.3);
        border-radius: 8px;
        transition: all 0.3s ease;

        &:hover {
          border-color: rgba(64, 158, 255, 0.6);
          transform: translateY(-3px);
        }

        .card-value {
          font-size: 36px;
          font-weight: bold;
          margin-bottom: 8px;
        }

        .card-label {
          font-size: 14px;
          color: rgba(255, 255, 255, 0.8);
        }
      }
    }
  }

  // 左侧边栏
  .left-sidebar {
    position: absolute;
    left: 20px;
    top: 80px;
    width: 120px;
    display: flex;
    flex-direction: column;
    gap: 10px;

    .sidebar-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 8px 12px;
      background: rgba(26, 31, 58, 0.8);
      border-left: 3px solid #409eff;
      border-radius: 4px;
      font-size: 12px;

      .item-label {
        color: rgba(255, 255, 255, 0.8);
      }

      .item-value {
        color: #409eff;
        font-weight: bold;
        font-size: 14px;
      }
    }
  }

  // 右侧边栏
  .right-sidebar {
    position: absolute;
    right: 20px;
    top: 80px;
    width: 120px;
    display: flex;
    flex-direction: column;
    gap: 10px;

    .sidebar-item {
      display: flex;
      justify-content: space-between;
      align-items: center;
      padding: 8px 12px;
      background: rgba(26, 31, 58, 0.8);
      border-right: 3px solid #67c23a;
      border-radius: 4px;
      font-size: 12px;

      .item-label {
        color: rgba(255, 255, 255, 0.8);
      }

      .item-value {
        color: #67c23a;
        font-weight: bold;
        font-size: 14px;
      }
    }
  }
}


