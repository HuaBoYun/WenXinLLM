<template>
  <div>
    <div
      ref="chart"
      style="width: 100%; height: 360px; padding: 10px; margin-bottom: 10px"
    ></div>
    <ReportListDialog ref="reportDialog" />
  </div>
</template>

<script>
  import * as echarts from 'echarts'
  import { reportByOrg } from '@/api/risk/home.js'
  import ReportListDialog from './ReportListDialog.vue'

  export default {
    components: {
      ReportListDialog,
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
      // this.fetchRiskData() // 在组件挂载后获取数据
    },

    watch: {
      riskData: {
        handler() {
          this.initChart()
        },
        deep: true,
      },
      year: {
        immediate: true, // 立即执行一次
        handler(newVal, oldVal) {
          if (newVal != oldVal) {
            this.fetchRiskData()
          }
        },
      },
    },

    methods: {
      async fetchRiskData() {
        try {
          const { data, code, msg } = await reportByOrg({
            company: this.year,
          })

          // 处理新的数据格式
          if (data.list && Array.isArray(data.list)) {
            // 新数据格式
            this.riskData = data.list.map((item) => ({
              company: item.NAME,
              value: item.NUMBER,
            }))

            // 按数值从大到小排序
            this.riskData.sort((a, b) => b.value - a.value)
          } else if (data.x && data.y) {
            // 原有数据格式
            this.riskData = data.x.map((company, index) => {
              return {
                company,
                value: data.y[index], // 风险值
              }
            })
          } else {
            this.riskData = []
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

        const option = {
          title: {
            text: '各公司风险报告统计图',
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
            data: this.riskData.map((item) => item.company),
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
              name: '风险值',
              type: 'bar',
              data: this.riskData.map((item) => item.value),
              label: {
                show: true,
                position: 'top', // 标签显示在柱状图的顶部
                formatter: '{c}', // 显示具体的数值
              },
            },
          ],
        }

        // 添加点击事件监听
        myChart.on('click', (params) => {
          this.$refs.reportDialog.show({ companyname: params.name })
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
