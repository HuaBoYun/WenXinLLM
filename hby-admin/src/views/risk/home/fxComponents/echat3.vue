<template>
  <div
    ref="chart"
    style="width: 100%; height: 360px; padding: 10px; margin-bottom: 10px"
  ></div>
</template>

<script>
  import * as echarts from 'echarts'
  import { getRiskeventCountByCompany } from '@/api/risk/home.js'
  export default {
    props: {
      orgid: {
        type: [String, Number],
        default: 1000,
      },
    },
    data() {
      return {
        riskData: [], // 初始化为空数组
      }
    },
    mounted() {
      this.fetchRiskData() // 在组件挂载后获取数据
      this.initChart()
    },
    watch: {
      riskData: {
        handler() {
          this.initChart()
        },
        deep: true,
      },
      orgid: {
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
        console.log('🚀 ~ fetchRiskData ~ this.orgid:', this.orgid)
        const { data, code, msg } = await getRiskeventCountByCompany({
          company: this.orgid ? this.orgid : '1000',
        })
        const xData = data.xData
        const yData = data.yData
        // 初始化 riskData 数组
        this.riskData = []
        // 遍历 xData 和 yData 并组装数据
        xData.forEach((company, index) => {
          const events = yData[index]
          this.riskData.push({ company, events })
        })
      },
      initChart() {
        const chartDom = this.$refs.chart
        const myChart = echarts.init(chartDom)

        const option = {
          title: {
            text: '风险事件数量统计',
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
              // rotate: 45,
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
              name: '风险事件数量',
              type: 'bar',
              data: this.riskData.map((item) => item.events),
              label: {
                show: true,
                position: 'top', // 标签显示在柱状图的顶部
                formatter: '{c}', // 显示具体的数值
              },
            },
          ],
        }

        myChart.setOption(option)
      },
    },
  }
</script>

<style scoped></style>
