<template>
  <div>
    <div
      ref="chart"
      style="width: 100%; height: 360px; padding: 10px; margin-bottom: 10px"
    ></div>
    <EventListDialog ref="eventDialog" />
  </div>
</template>

<script>
  import * as echarts from 'echarts'
  import { getCompanyRiskEventList } from '@/api/risk/home.js'
  import EventListDialog from './EventListDialog.vue'

  export default {
    components: {
      EventListDialog,
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
        seriesNames: [], // 动态存储 series 的名称
      }
    },
    mounted() {
      this.fetchRiskData() // 在组件挂载后获取数据
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
        console.log('🚀 ~ fetchRiskData ~ this.year:', this.year)
        const { data, code, msg } = await getCompanyRiskEventList({
          year: this.year,
        })

        const xData = data.xAxis
        const yData = data.yAxis

        // 动态提取 yAxis 的键作为 series 的名称
        this.seriesNames = Object.keys(yData) // ['一般', '重大']

        // 初始化 riskData 数组
        this.riskData = xData.map((company, index) => {
          const companyData = { company }
          this.seriesNames.forEach((name) => {
            companyData[name] = yData[name][index] // 动态添加属性
          })
          return companyData
        })
      },
      initChart() {
        const chartDom = this.$refs.chart
        const myChart = echarts.init(chartDom)

        const option = {
          title: {
            text: '按公司风险事件统计图',
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
            data: this.seriesNames, // 动态生成图例
            left: 'right',
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
          series: this.seriesNames.map((name) => ({
            name, // 动态生成 series 的 name
            type: 'bar',
            data: this.riskData.map((item) => item[name]), // 动态获取数据
            label: {
              show: true,
              position: 'top', // 标签显示在柱状图的顶部
              formatter: '{c}', // 显示具体的数值
            },
          })),
        }
        myChart.on('click', (params) => {
          const type = params.seriesName == '一般' ? '1' : '2'
          this.$refs.eventDialog.show({
            losseventcategory: type,
            companyname: params.name,
          })
        })
        myChart.setOption(option)
      },
    },
  }
</script>

<style scoped></style>
