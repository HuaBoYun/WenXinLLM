<template>
  <el-card>
    <div class="chart-wrapper">
      <div class="total-display">
        <div class="total-number">{{ chartData.total }}</div>
        <div class="total-label">总数</div>
      </div>
      <div
        ref="chartContainer"
        style="width: 100%; height: 360px; margin-bottom: 10px"
      ></div>
    </div>
  </el-card>
</template>

<script>
  import * as echarts from 'echarts'
  import { getYjsyBtData } from '@/oapi/risk/index'

  export default {
    name: 'YjsyBt',
    data() {
      return {
        chartInstance: null,
        chartData: {
          red: 0,
          yellow: 0,
          green: 0,
          total: 0,
        },
      }
    },
    mounted() {
      this.initChart()
      this.fetchData()
    },
    beforeUnmount() {
      if (this.chartInstance) {
        this.chartInstance.dispose()
      }
    },
    methods: {
      async fetchData() {
        try {
          const response = await getYjsyBtData()
          console.log('接口返回数据:', response)
          if (response.code == 1) {
            this.chartData = response.data
            console.log('更新后的chartData:', this.chartData)
            this.updateChart()
          }
        } catch (error) {
          console.error('获取应急事业部数据失败:', error)
        }
      },
      initChart() {
        this.$nextTick(() => {
          if (this.$refs.chartContainer) {
            this.chartInstance = echarts.init(this.$refs.chartContainer)
            console.log('ECharts实例初始化成功:', this.chartInstance)

            // 监听窗口大小变化
            window.addEventListener('resize', this.handleResize)
          } else {
            console.error('图表容器未找到')
          }
        })
      },
      updateChart() {
        if (!this.chartInstance) {
          console.error('图表实例未初始化')
          return
        }

        const option = {
          title: {
            text: '预警指标处理饼状图',
            left: 'left',
            textStyle: { fontSize: 14, color: '#FF8C00' },
          },
          tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b}: {c} ({d}%)',
          },
          legend: {
            orient: 'horizontal',
            bottom: 0,
            left: 'center',
            data: ['高风险', '中风险', '低风险'],
          },
          series: [
            {
              name: '预警指标',
              type: 'pie',
              radius: '50%',
              center: ['50%', '43%'],
              data: [
                {
                  value: this.chartData.red,
                  name: '高风险',
                  itemStyle: {
                    color: '#ff4757',
                  },
                },
                {
                  value: this.chartData.yellow,
                  name: '中风险',
                  itemStyle: {
                    color: '#ffa502',
                  },
                },
                {
                  value: this.chartData.green,
                  name: '低风险',
                  itemStyle: {
                    color: '#2ed573',
                  },
                },
              ],
              emphasis: {
                itemStyle: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)',
                },
              },
              label: {
                show: true,
                position: 'outside',
                formatter: '{b}: {c} ({d}%)',
              },
            },
          ],
        }

        console.log('设置图表配置:', option)
        this.chartInstance.setOption(option)
      },
      handleResize() {
        if (this.chartInstance) {
          this.chartInstance.resize()
        }
      },
    },
  }
</script>

<style scoped>
  .chart-wrapper {
    position: relative;
  }
  .total-display {
    position: absolute;
    top: 20px;
    right: 20px;
    text-align: center;
    z-index: 10;
    background: rgba(255, 255, 255, 0.9);
    border-radius: 8px;
    padding: 10px 15px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  }

  .total-number {
    font-size: 24px;
    font-weight: bold;
    color: #ff8c00;
    line-height: 1;
  }

  .total-label {
    font-size: 12px;
    color: #666;
    margin-top: 4px;
  }
</style>
