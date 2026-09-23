<template>
  <el-dialog
    :title="chartTitle"
    :visible.sync="dialogVisible"
    width="800px"
    :before-close="handleClose"
  >
    <div ref="chart" style="width: 100%; height: 400px;" />
  </el-dialog>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'DataChart',
  props: {
    visible: {
      type: Boolean,
      default: false
    },
    chartData: {
      type: Array,
      default: () => []
    },
    chartTitle: {
      type: String,
      default: '数据图表'
    }
  },
  data() {
    return {
      dialogVisible: false,
      chart: null
    }
  },
  watch: {
    visible(val) {
      this.dialogVisible = val
      if (val) {
        this.$nextTick(() => {
          this.initChart()
        })
      }
    },
    dialogVisible(val) {
      this.$emit('update:visible', val)
    },
    chartData: {
      handler() {
        if (this.dialogVisible) {
          this.$nextTick(() => {
            this.initChart()
          })
        }
      },
      deep: true
    }
  },
  beforeDestroy() {
    if (this.chart) {
      this.chart.dispose()
      this.chart = null
    }
  },
  methods: {
    /** 初始化图表 */
    initChart() {
      if (!this.$refs.chart) {
        return
      }

      // 销毁旧图表
      if (this.chart) {
        this.chart.dispose()
      }

      // 创建新图表
      this.chart = echarts.init(this.$refs.chart)

      // 配置图表选项
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
          data: this.chartData.map(item => item.name),
          axisLabel: {
            rotate: 45
          }
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '数据值',
            type: 'line',
            data: this.chartData.map(item => item.value),
            smooth: true,
            itemStyle: {
              color: '#409EFF'
            },
            areaStyle: {
              color: {
                type: 'linear',
                x: 0,
                y: 0,
                x2: 0,
                y2: 1,
                colorStops: [
                  {
                    offset: 0,
                    color: 'rgba(64, 158, 255, 0.5)'
                  },
                  {
                    offset: 1,
                    color: 'rgba(64, 158, 255, 0.1)'
                  }
                ]
              }
            }
          }
        ]
      }

      this.chart.setOption(option)

      // 监听窗口大小变化
      window.addEventListener('resize', this.handleResize)
    },
    /** 处理窗口大小变化 */
    handleResize() {
      if (this.chart) {
        this.chart.resize()
      }
    },
    /** 关闭对话框 */
    handleClose() {
      if (this.chart) {
        window.removeEventListener('resize', this.handleResize)
      }
      this.dialogVisible = false
    }
  }
}
</script>

<style scoped>
</style>

