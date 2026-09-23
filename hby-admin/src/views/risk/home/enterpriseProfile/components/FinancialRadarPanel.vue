<template>
  <div class="financial-radar-panel">
    <!-- 雷达图区域 -->
    <div class="radar-section">
      <div class="section-title">
        <i class="el-icon-pie-chart"></i>
        财务能力雷达图
      </div>
      <div class="radar-chart-container">
        <div 
          ref="radarChart" 
          class="radar-chart"
          v-loading="loading"
        ></div>
        <div class="overall-score" v-if="radarData && radarData.radarData">
          <div class="score-value">{{ radarData.radarData.overallScore || 0 }}</div>
          <div class="score-label">综合得分</div>
          <div class="score-level">{{ radarData.radarData.overallLevel || '一般' }}</div>
        </div>
      </div>
    </div>

    <!-- 关键指标区域 -->
    <div class="indicators-section">
      <div class="section-title">
        <i class="el-icon-data-line"></i>
        关键财务指标
      </div>
      <div class="indicators-list" v-if="radarData && radarData.keyIndicators">
        <div 
          v-for="indicator in radarData.keyIndicators" 
          :key="indicator.indicatorCode"
          class="indicator-card"
          :class="indicator.color"
        >
          <div class="indicator-header">
            <span class="indicator-name">{{ indicator.indicatorName }}</span>
            <span class="indicator-level" :class="indicator.level">{{ indicator.level }}</span>
          </div>
          <div class="indicator-value">
            {{ formatValue(indicator.currentValue) }}
            <span class="unit">{{ indicator.unit }}</span>
          </div>
          <div class="indicator-change" v-if="indicator.changeRate !== null">
            <i 
              :class="getChangeIcon(indicator.changeDirection)"
              :style="{ color: getChangeColor(indicator.changeDirection) }"
            ></i>
            <span :style="{ color: getChangeColor(indicator.changeDirection) }">
              {{ Math.abs(indicator.changeRate || 0).toFixed(2) }}%
            </span>
          </div>
        </div>
      </div>
      
      <!-- 空状态 -->
      <div v-else-if="!loading" class="empty-indicators">
        <el-empty description="暂无指标数据" :image-size="80" />
      </div>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'FinancialRadarPanel',
  props: {
    enterpriseId: {
      type: String,
      required: true
    },
    radarData: {
      type: Object,
      default: null
    },
    loading: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      radarChart: null
    }
  },
  watch: {
    radarData: {
      handler() {
        this.$nextTick(() => {
          this.initRadarChart()
        })
      },
      deep: true
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.initRadarChart()
    })
    
    // 监听窗口大小变化
    window.addEventListener('resize', this.handleResize)
  },
  beforeDestroy() {
    if (this.radarChart) {
      this.radarChart.dispose()
    }
    window.removeEventListener('resize', this.handleResize)
  },
  methods: {
    /**
     * 初始化雷达图
     */
    initRadarChart() {
      if (!this.$refs.radarChart) return
      
      // 销毁已存在的图表
      if (this.radarChart) {
        this.radarChart.dispose()
      }
      
      // 创建新图表
      this.radarChart = echarts.init(this.$refs.radarChart)
      
      if (this.radarData && this.radarData.radarData) {
        this.updateRadarChart()
      } else {
        this.showEmptyChart()
      }
    },

    /**
     * 更新雷达图数据
     */
    updateRadarChart() {
      const { dimensions } = this.radarData.radarData
      
      if (!dimensions || dimensions.length === 0) {
        this.showEmptyChart()
        return
      }

      const indicator = dimensions.map(item => ({
        name: item.dimensionName,
        max: item.maxScore || 100
      }))

      const data = dimensions.map(item => item.score || 0)

      const option = {
        tooltip: {
          trigger: 'item',
          formatter: function(params) {
            return `${params.name}: ${params.value}`
          }
        },
        radar: {
          indicator: indicator,
          center: ['50%', '50%'],
          radius: '70%',
          axisName: {
            color: '#666',
            fontSize: 12
          },
          splitLine: {
            lineStyle: {
              color: 'rgba(102, 102, 102, 0.3)'
            }
          },
          axisLine: {
            lineStyle: {
              color: 'rgba(102, 102, 102, 0.3)'
            }
          }
        },
        series: [{
          type: 'radar',
          data: [{
            value: data,
            name: '财务能力',
            areaStyle: {
              color: 'rgba(103, 126, 234, 0.3)'
            },
            lineStyle: {
              color: '#677eea',
              width: 2
            },
            itemStyle: {
              color: '#677eea'
            }
          }]
        }]
      }

      this.radarChart.setOption(option)
    },

    /**
     * 显示空图表
     */
    showEmptyChart() {
      const option = {
        title: {
          text: '暂无数据',
          left: 'center',
          top: 'middle',
          textStyle: {
            color: '#999',
            fontSize: 14
          }
        }
      }
      this.radarChart.setOption(option)
    },

    /**
     * 处理窗口大小变化
     */
    handleResize() {
      if (this.radarChart) {
        this.radarChart.resize()
      }
    },

    /**
     * 格式化数值
     */
    formatValue(value) {
      if (value === null || value === undefined) return '0'
      return Number(value).toFixed(2)
    },

    /**
     * 获取变化图标
     */
    getChangeIcon(direction) {
      switch (direction) {
        case 'UP':
          return 'el-icon-top'
        case 'DOWN':
          return 'el-icon-bottom'
        default:
          return 'el-icon-minus'
      }
    },

    /**
     * 获取变化颜色
     */
    getChangeColor(direction) {
      switch (direction) {
        case 'UP':
          return '#67C23A'
        case 'DOWN':
          return '#F56C6C'
        default:
          return '#909399'
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.financial-radar-panel {
  background: rgba(255, 255, 255, 0.95);
  border-radius: 12px;
  padding: 15px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
  height: 100%;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  box-sizing: border-box;

  .section-title {
    display: flex;
    align-items: center;
    gap: 8px;
    font-size: 16px;
    font-weight: bold;
    color: #1e3c72;
    margin-bottom: 15px;

    i {
      font-size: 18px;
    }
  }

  .radar-section {
    flex-shrink: 0;
    margin-bottom: 20px;

    .radar-chart-container {
      position: relative;
      height: 280px;
      max-height: 40vh; // 限制最大高度为视口高度的40%

      .radar-chart {
        width: 100%;
        height: 100%;
      }

      .overall-score {
        position: absolute;
        top: 50%;
        left: 50%;
        transform: translate(-50%, -50%);
        text-align: center;
        pointer-events: none;

        .score-value {
          font-size: 24px;
          font-weight: bold;
          color: #1e3c72;
        }

        .score-label {
          font-size: 12px;
          color: #666;
          margin: 2px 0;
        }

        .score-level {
          font-size: 14px;
          font-weight: bold;
          color: #677eea;
        }
      }
    }
  }

  .indicators-section {
    flex: 1;
    min-height: 0;

    .indicators-list {
      display: flex;
      flex-direction: column;
      gap: 8px;
      max-height: calc(60vh - 100px); // 动态计算最大高度
      min-height: 200px;
      overflow-y: auto;
      padding-right: 5px; // 为滚动条留出空间

      // 自定义滚动条样式
      &::-webkit-scrollbar {
        width: 6px;
      }

      &::-webkit-scrollbar-track {
        background: #f1f1f1;
        border-radius: 3px;
      }

      &::-webkit-scrollbar-thumb {
        background: #c1c1c1;
        border-radius: 3px;

        &:hover {
          background: #a8a8a8;
        }
      }

      .indicator-card {
        background: #f8f9fa;
        border-radius: 8px;
        padding: 10px 12px;
        border-left: 4px solid #677eea;
        transition: all 0.3s ease;

        &:hover {
          background: #e9ecef;
          transform: translateX(2px);
          box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
        }

        &.green {
          border-left-color: #67C23A;
        }

        &.blue {
          border-left-color: #409EFF;
        }

        &.orange {
          border-left-color: #E6A23C;
        }

        &.red {
          border-left-color: #F56C6C;
        }

        .indicator-header {
          display: flex;
          justify-content: space-between;
          align-items: center;
          margin-bottom: 8px;

          .indicator-name {
            font-size: 14px;
            font-weight: bold;
            color: #333;
          }

          .indicator-level {
            font-size: 12px;
            padding: 2px 8px;
            border-radius: 12px;
            background: #e9ecef;
            color: #666;

            &.优秀 {
              background: #d4edda;
              color: #155724;
            }

            &.良好 {
              background: #d1ecf1;
              color: #0c5460;
            }

            &.一般 {
              background: #fff3cd;
              color: #856404;
            }

            &.较差 {
              background: #f8d7da;
              color: #721c24;
            }
          }
        }

        .indicator-value {
          font-size: 18px;
          font-weight: bold;
          color: #1e3c72;
          margin-bottom: 5px;

          .unit {
            font-size: 12px;
            font-weight: normal;
            color: #666;
            margin-left: 2px;
          }
        }

        .indicator-change {
          display: flex;
          align-items: center;
          gap: 4px;
          font-size: 12px;
        }
      }
    }

    .empty-indicators {
      height: 200px;
      display: flex;
      align-items: center;
      justify-content: center;
    }
  }

  // 响应式设计
  @media (max-height: 600px) {
    .radar-section .radar-chart-container {
      height: 200px;
    }

    .indicators-section .indicators-list {
      max-height: 150px;
      min-height: 120px;
    }
  }

  @media (min-height: 800px) {
    .radar-section .radar-chart-container {
      height: 320px;
    }

    .indicators-section .indicators-list {
      max-height: calc(60vh - 120px);
      min-height: 250px;
    }
  }

  @media (min-height: 1000px) {
    .radar-section .radar-chart-container {
      height: 350px;
    }

    .indicators-section .indicators-list {
      max-height: calc(60vh - 150px);
      min-height: 300px;
    }
  }
}
</style>
