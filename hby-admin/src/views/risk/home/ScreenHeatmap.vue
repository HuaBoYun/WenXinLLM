<template>
  <div class="screen-heatmap-container">
    <div class="heatmap-chart">
      <!-- Y轴标签 (严重程度) -->
      <div class="y-axis">
        <div class="y-labels">
          <div class="y-label" v-for="i in 5" :key="i">{{ 6 - i }}</div>
        </div>
      </div>

      <!-- 主要表格区域 -->
      <div class="chart-main">
        <!-- 5x5 网格 -->
        <div class="grid-container">
          <div class="grid-row" v-for="row in 5" :key="row">
            <div
              class="grid-cell"
              v-for="col in 5"
              :key="col"
              :class="getCellClass(row, col)"
              :style="getCellStyle(row, col)"
              @mouseenter="handleMouseEnter($event, row, col)"
              @mouseleave="handleMouseLeave"
            >
              <!-- 立体数字显示 -->
              <div class="cell-content">
                <span class="cell-number">{{ getCellValue(row, col) || 0 }}</span>
              </div>
            </div>
          </div>
        </div>

        <!-- X轴标签 (频率) -->
        <div class="x-axis">
          <div class="x-labels">
            <div class="x-label" v-for="i in 5" :key="i">{{ i }}</div>
          </div>
        </div>
      </div>
    </div>

    <!-- 图例 -->
    <div class="legend">
      <div class="legend-item">
        <div class="legend-color low"></div>
        <span>低风险</span>
      </div>
      <div class="legend-item">
        <div class="legend-color medium"></div>
        <span>中风险</span>
      </div>
      <div class="legend-item">
        <div class="legend-color high"></div>
        <span>高风险</span>
      </div>
    </div>

    <!-- 悬浮提示 -->
    <div
      v-if="tooltipVisible"
      class="tooltip"
      :style="tooltipStyle"
    >
      数量: {{ tooltipContent }}
    </div>
  </div>
</template>

<script>
import { getPgrlt } from '@/api/risk/home.js'

export default {
  name: 'ScreenHeatmap',
  props: {
    orgid: {
      type: [String, Number],
      required: true
    }
  },
  data() {
    return {
      loading: false,
      tooltipVisible: false,
      tooltipContent: '',
      tooltipPosition: { x: 0, y: 0 },
      riskData: {}
    }
  },
  computed: {
    tooltipStyle() {
      return {
        left: this.tooltipPosition.x + 'px',
        top: this.tooltipPosition.y + 'px'
      }
    }
  },
  watch: {
    orgid: {
      handler() {
        this.fetchData()
      },
      immediate: true
    }
  },
  mounted() {
    this.fetchData()
  },
  methods: {
    async fetchData() {
      try {
        this.loading = true
        const response = await getPgrlt({ company: this.orgid })
        if (response.code === 1 && response.data) {
          // 注意：数据直接在 response.data 中，不是 response.data.data
          this.riskData = response.data || {}
        }
      } catch (error) {
        console.error('获取热力图数据失败:', error)
      } finally {
        this.loading = false
      }
    },

    // 获取单元格的值
    getCellValue(row, col) {
      const severity = 6 - row
      const frequency = col
      const key = `${frequency}_${severity}`
      const value = this.riskData[key] || 0
      return value
    },

    // 获取单元格的类名
    getCellClass(row, col) {
      const severity = 6 - row
      const frequency = col
      
      if (
        (frequency === 1 && (severity === 1 || severity === 2 || severity === 3)) ||
        (frequency === 2 && (severity === 1 || severity === 2))
      ) {
        return 'risk-low'
      } else if (
        (frequency === 1 && (severity === 4 || severity === 5)) ||
        (frequency === 2 && (severity === 3 || severity === 4 || severity === 5)) ||
        (frequency === 3 && (severity === 1 || severity === 2 || severity === 3 || severity === 4)) ||
        (frequency === 4 && (severity === 1 || severity === 2))
      ) {
        return 'risk-medium'
      } else {
        return 'risk-high'
      }
    },

    // 获取单元格的样式
    getCellStyle(row, col) {
      const severity = 6 - row
      const frequency = col
      let backgroundColor = '#4CAF50'

      if (
        (frequency === 1 && (severity === 1 || severity === 2 || severity === 3)) ||
        (frequency === 2 && (severity === 1 || severity === 2))
      ) {
        backgroundColor = '#4CAF50'
      } else if (
        (frequency === 1 && (severity === 4 || severity === 5)) ||
        (frequency === 2 && (severity === 3 || severity === 4 || severity === 5)) ||
        (frequency === 3 && (severity === 1 || severity === 2 || severity === 3 || severity === 4)) ||
        (frequency === 4 && (severity === 1 || severity === 2))
      ) {
        backgroundColor = '#FFEB3B'
      } else {
        backgroundColor = '#F44336'
      }

      return {
        backgroundColor,
        color: backgroundColor === '#FFEB3B' ? '#333' : '#fff',
        fontWeight: 'bold'
      }
    },

    // 鼠标进入
    handleMouseEnter(event, row, col) {
      const value = this.getCellValue(row, col)
      if (value > 0) {
        this.tooltipContent = value
        this.tooltipPosition.x = event.clientX + 15
        this.tooltipPosition.y = event.clientY + 10
        this.tooltipVisible = true
      }
    },

    // 鼠标离开
    handleMouseLeave() {
      this.tooltipVisible = false
    }
  }
}
</script>

<style scoped lang="scss">
.screen-heatmap-container {
  padding: 15px;
  height: 100%;
  display: flex;
  flex-direction: column;
  transform: translateZ(0);
  backface-visibility: hidden;
  overflow: hidden;

  .heatmap-chart {
    display: flex;
    align-items: flex-start;
    justify-content: center;
    margin-bottom: 15px;
    transform: translateZ(0);
    backface-visibility: hidden;
  }

  .y-axis {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-right: 10px;
  }

  .y-labels {
    display: flex;
    flex-direction: column;
    height: 250px;
    justify-content: space-between;
  }

  .y-label {
    width: 30px;
    height: 50px;
    display: flex;
    align-items: center;
    justify-content: center;
    font-weight: bold;
    color: rgba(255, 255, 255, 0.8);
    font-size: 14px;
  }

  .chart-main {
    display: flex;
    flex-direction: column;
    align-items: center;
  }

  .x-axis {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-top: 10px;
  }

  .x-labels {
    display: flex;
    width: 250px;
    justify-content: space-between;
  }

  .x-label {
    width: 50px;
    text-align: center;
    font-weight: bold;
    color: rgba(255, 255, 255, 0.8);
    font-size: 14px;
  }

  .grid-container {
    display: flex;
    flex-direction: column;
    gap: 3px;
    padding: 8px;
    background: rgba(0, 0, 0, 0.2);
    border-radius: 8px;
    transform: translateZ(0);
    backface-visibility: hidden;
    box-shadow: inset 0 2px 8px rgba(0, 0, 0, 0.3);
  }

  .grid-row {
    display: flex;
    gap: 3px;
  }

  .grid-cell {
    width: 50px;
    height: 50px;
    border: none;
    display: flex;
    align-items: center;
    justify-content: center;
    cursor: pointer;
    position: relative;
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
    will-change: transform;
    border-radius: 4px;
    box-shadow:
      0 2px 4px rgba(0, 0, 0, 0.2),
      inset 0 1px 0 rgba(255, 255, 255, 0.2),
      inset 0 -1px 0 rgba(0, 0, 0, 0.2);

    &:hover {
      transform: translateY(-3px) scale(1.05);
      box-shadow:
        0 6px 16px rgba(0, 0, 0, 0.4),
        0 0 20px rgba(64, 158, 255, 0.4),
        inset 0 1px 0 rgba(255, 255, 255, 0.3),
        inset 0 -1px 0 rgba(0, 0, 0, 0.3);
      z-index: 100;
    }

    &:active {
      transform: translateY(-1px) scale(1.02);
    }

    .cell-content {
      width: 100%;
      height: 100%;
      display: flex;
      align-items: center;
      justify-content: center;
      position: relative;
      z-index: 1;
    }

    .cell-number {
      font-size: 18px;
      font-weight: 700;
      pointer-events: none;
      text-shadow:
        0 1px 2px rgba(0, 0, 0, 0.3),
        0 2px 4px rgba(0, 0, 0, 0.2),
        1px 1px 0 rgba(255, 255, 255, 0.1);
      letter-spacing: 0.5px;
      filter: drop-shadow(0 0 8px rgba(255, 255, 255, 0.3));
    }
  }

  .risk-low {
    background: linear-gradient(135deg, #66bb6a 0%, #4caf50 50%, #43a047 100%) !important;

    .cell-number {
      color: #ffffff;
    }
  }

  .risk-medium {
    background: linear-gradient(135deg, #ffee58 0%, #ffeb3b 50%, #fdd835 100%) !important;

    .cell-number {
      color: #333333;
      text-shadow:
        0 1px 2px rgba(255, 255, 255, 0.5),
        0 2px 4px rgba(0, 0, 0, 0.1);
    }
  }

  .risk-high {
    background: linear-gradient(135deg, #ef5350 0%, #f44336 50%, #e53935 100%) !important;

    .cell-number {
      color: #ffffff;
    }
  }

  .legend {
    display: flex;
    justify-content: center;
    gap: 20px;
    margin-top: 10px;
  }

  .legend-item {
    display: flex;
    align-items: center;
    gap: 8px;

    span {
      color: rgba(255, 255, 255, 0.8);
      font-size: 13px;
    }
  }

  .legend-color {
    width: 20px;
    height: 20px;
    border-radius: 3px;

    &.low {
      background-color: #4caf50;
    }

    &.medium {
      background-color: #ffeb3b;
    }

    &.high {
      background-color: #f44336;
    }
  }

  .tooltip {
    position: fixed !important;
    background: rgba(0, 0, 0, 0.9) !important;
    border: 1px solid #409eff !important;
    color: #fff !important;
    padding: 8px 12px !important;
    border-radius: 4px !important;
    font-size: 14px !important;
    z-index: 99999 !important;
    pointer-events: none !important;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3) !important;
    white-space: nowrap !important;
    display: block !important;
  }
}
</style>

