<template>
  <div>
    <div
      ref="chart"
      style="width: 100%; height: 360px; padding: 10px; margin-bottom: 10px"
    ></div>
    <RiskAssessmentDialog ref="riskDialog" :orgid="orgid" />
  </div>
</template>

<script>
  import * as echarts from 'echarts'
  import { getRiskAnalysis } from '@/api/risk/home.js'
  import RiskAssessmentDialog from './RiskAssessmentDialog.vue'

  export default {
    components: {
      RiskAssessmentDialog,
    },
    props: {
      orgid: {
        type: [String, Number],
        default: '1000',
      },
    },
    data() {
      return {
        riskData: [], // 初始化为空数组
        myChart: null,
      }
    },
    mounted() {
      // this.fetchRiskData() // 在组件挂载后获取数据
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
        const { data, code, msg } = await getRiskAnalysis({
          company: this.orgid,
        })
        const xData = data.nameList
        const yData = data.valueList
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
        if (this.myChart) {
          this.myChart.dispose()
        }
        this.myChart = echarts.init(chartDom)

        const option = {
          title: {
            text: '风险点统计风险评估结果',
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
              rotate: 45,
              interval: 0,
              formatter: function (value) {
                // 每六个字换一行
                return value
                  .split(/(.{5})/)
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
              name: '风险点统计风险评估结果',
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

        this.myChart.setOption(option)

        // 添加点击事件，弹窗显示列表
        this.myChart.on('click', (params) => {
          this.$refs.riskDialog.show(
            {
              orgid: this.orgid,
              riskname: params.name,
              pgStatus: 'ypg',
            },
            `风险点统计风险评估结果 - ${params.name}`
          )
        })
      },
    },
  }
</script>

<style scoped></style>
