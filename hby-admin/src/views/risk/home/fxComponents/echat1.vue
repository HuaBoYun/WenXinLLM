<template>
  <div
    ref="chart"
    style="width: 100%; height: 360px; padding: 10px; margin-bottom: 10px"
  ></div>
</template>

<script>
  import * as echarts from 'echarts'
  import { getRiskByCompanyAnalysis } from '@/api/risk/home.js'

  export default {
    props: {
      orgid: {
        type: [String, Number],
        default: '1000',
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
        const { data, code, msg } = await getRiskByCompanyAnalysis({
          company: this.orgid ? this.orgid : '1000',
        })
        let arr = []
        arr.push(data.xAxis)
        let levels = ['很低', '较低', '中等', '较高', '很高']
        let result = []
        arr.forEach((company, index) => {
          let companyData = { company: company }
          levels.forEach((level) => {
            companyData[level] = data.yAxis[level][index]
          })
          result.push(companyData)
        })
        this.riskData = result
      },
      initChart() {
        const chartDom = this.$refs.chart
        const myChart = echarts.init(chartDom)

        const option = {
          title: {
            text: '风险评估结果统计',
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
            data: ['很低', '较低', '中等', '较高', '很高'],
            left: 'center',
            bottom: 0,
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
              name: '很低',
              type: 'bar',
              data: this.riskData.map((item) => item.很低),
              label: {
                show: true,
                position: 'top',
                formatter: '{c}', // 显示具体的数值
              },
            },
            {
              name: '较低',
              type: 'bar',
              data: this.riskData.map((item) => item.较低),
              label: {
                show: true,
                position: 'top',
                formatter: '{c}', // 显示具体的数值
              },
            },
            {
              name: '中等',
              type: 'bar',
              data: this.riskData.map((item) => item.中等),
              label: {
                show: true,
                position: 'top',
                formatter: '{c}', // 显示具体的数值
              },
            },
            {
              name: '较高',
              type: 'bar',
              data: this.riskData.map((item) => item.较高),
              label: {
                show: true,
                position: 'top',
                formatter: '{c}', // 显示具体的数值
              },
            },
            {
              name: '很高',
              type: 'bar',
              data: this.riskData.map((item) => item.很高),
              label: {
                show: true,
                position: 'top',
                formatter: '{c}', // 显示具体的数值
              },
            },
          ],
        }

        myChart.setOption(option)
        myChart.on('click', (params) => {
          if (params.componentType === 'series') {
            // 获取点击的公司名称
            const companyName = params.name
            // 跳转到目标页面，假设目标页面是 `/company-detail/${companyName}`
            this.$router.push({
              path: `/assessment/result`,
            })
          }
        })
      },
    },
  }
</script>

<style scoped></style>
