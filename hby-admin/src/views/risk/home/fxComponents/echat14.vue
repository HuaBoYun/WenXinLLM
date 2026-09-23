<template>
  <div>
    <div
      ref="chart"
      style="width: 100%; height: 360px; padding: 10px; margin-bottom: 10px"
    ></div>
    <RiskDatabaseDialog ref="riskDialog" />
  </div>
</template>

<script>
  import * as echarts from 'echarts'
  import { getCountByOrg } from '@/api/risk/home.js'
  import RiskDatabaseDialog from './RiskDatabaseDialog.vue'

  export default {
    components: {
      RiskDatabaseDialog,
    },
    props: {
      year: {
        type: [String, Number],
        default: '',
      },
    },
    data() {
      return {
        riskData: [], // 初始化为空数组
      }
    },
    mounted() {
      this.fetchRiskData() // 在组件挂载后获取数据
    },
    watch: {
      year: {
        immediate: true, // 立即执行一次
        handler(newVal, oldVal) {
          if (newVal != oldVal) {
            this.fetchRiskData()
          }
        },
      },
      riskData: {
        handler() {
          this.initChart()
        },
        deep: true,
      },
    },
    methods: {
      async fetchRiskData() {
        try {
          const { data, code, msg } = await getCountByOrg({
            year: this.year,
            type: 1,
          })

          // 处理新的数据格式
          if (Array.isArray(data.list)) {
            this.riskData = data.list.map((item) => ({
              name: item.NAME,
              value: item.NUMBER,
            }))

            // 按数值从大到小排序
            this.riskData.sort((a, b) => b.value - a.value)
          } else {
            // 保留原有的处理逻辑，以防接口返回格式变化
            const xData = data.xAxis
            const yData = data.yAxis
            const seriesNames = Object.keys(yData)

            this.riskData = xData.map((company, index) => {
              const companyData = { name: company }
              seriesNames.forEach((name) => {
                companyData[name] = yData[name][index]
              })
              return companyData
            })
          }
        } catch (error) {
          console.error('获取数据失败:', error)
          this.riskData = []
        }
      },

      initChart() {
        const chartDom = this.$refs.chart
        if (!chartDom) return

        const myChart = echarts.init(chartDom)

        // 检查数据格式，确定使用哪种图表配置
        const isNewFormat =
          this.riskData.length > 0 && 'value' in this.riskData[0]

        let option

        if (isNewFormat) {
          // 新数据格式的图表配置
          option = {
            title: {
              text: '各公司风险数量统计',
              left: 'left',
              textStyle: { fontSize: 14, color: '#FF8C00' },
            },
            tooltip: {
              trigger: 'axis',
              axisPointer: {
                type: 'shadow',
              },
            },
            xAxis: {
              type: 'category',
              data: this.riskData.map((item) => item.name),
              axisLabel: {
                fontSize: 10,
                formatter: function (value) {
                  // 每六个字换一行
                  return value
                    .split(/(.{6})/)
                    .filter((s) => s)
                    .join('\n')
                },
              },
            },
            yAxis: {
              type: 'value',
            },
            series: [
              {
                type: 'bar',
                data: this.riskData.map((item) => item.value),
                label: {
                  show: true,
                  position: 'top',
                  formatter: '{c}',
                },
              },
            ],
          }
        } else {
          // 原有数据格式的图表配置
          const seriesNames = Object.keys(this.riskData[0] || {}).filter(
            (key) => key !== 'name'
          )

          option = {
            title: {
              text: '各公司风险数量统计',
              left: 'left',
              textStyle: { fontSize: 14, color: '#FF8C00' },
            },
            tooltip: {
              trigger: 'axis',
              axisPointer: {
                type: 'shadow',
              },
            },
            legend: {
              data: seriesNames,
              left: 'right',
            },
            xAxis: {
              type: 'category',
              data: this.riskData.map((item) => item.name),
              axisLabel: {
                fontSize: 10,
                formatter: function (value) {
                  return value
                    .split(/(.{6})/)
                    .filter((s) => s)
                    .join('\n')
                },
              },
            },
            yAxis: {
              type: 'value',
            },
            series: seriesNames.map((name) => ({
              name,
              type: 'bar',
              data: this.riskData.map((item) => item[name]),
              label: {
                show: true,
                position: 'top',
                formatter: '{c}',
              },
            })),
          }
        }

        // 添加点击事件监听
        myChart.on('click', (params) => {
          this.$refs.riskDialog.show({ unitname: params.name })
        })

        // 设置自适应
        window.addEventListener('resize', () => {
          myChart.resize()
        })

        myChart.setOption(option)
      },
    },
  }
</script>

<style scoped></style>
