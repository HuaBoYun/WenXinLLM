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
  import { getRiskByDepartmentAnalysis } from '@/api/risk/home.js'
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
      isFromZhjd: {
        type: Boolean,
        default: false,
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
        const { data, code, msg } = await getRiskByDepartmentAnalysis({
          company: this.orgid,
        })
        let levels = ['很低', '较低', '中等', '较高', '很高']
        let result = []
        data.xAxis.forEach((company, index) => {
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
        if (this.myChart) {
          this.myChart.dispose()
        }
        this.myChart = echarts.init(chartDom)

        const option = {
          title: {
            text: '按部门统计风险评估结果',
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
              itemStyle: { color: '#52ffb7' },
              label: {
                show: true,
                position: 'top',
                formatter: (params) => (params.value === 0 ? '' : params.value),
              },
            },
            {
              name: '较低',
              type: 'bar',
              data: this.riskData.map((item) => item.较低),
              itemStyle: { color: '#33d73b' },
              label: {
                show: true,
                position: 'top',
                formatter: (params) => (params.value === 0 ? '' : params.value),
              },
            },
            {
              name: '中等',
              type: 'bar',
              data: this.riskData.map((item) => item.中等),
              itemStyle: { color: '#ffb500' },
              label: {
                show: true,
                position: 'top',
                formatter: (params) => (params.value === 0 ? '' : params.value),
              },
            },
            {
              name: '较高',
              type: 'bar',
              data: this.riskData.map((item) => item.较高),
              itemStyle: { color: '#ff7f00' },
              label: {
                show: true,
                position: 'top',
                formatter: (params) => (params.value === 0 ? '' : params.value),
              },
            },
            {
              name: '很高',
              type: 'bar',
              data: this.riskData.map((item) => item.很高),
              itemStyle: { color: '#e92129' },
              label: {
                show: true,
                position: 'top',
                formatter: (params) => (params.value === 0 ? '' : params.value),
              },
            },
          ],
        }

        this.myChart.setOption(option)

        // 添加点击事件，弹窗显示列表
        this.myChart.on('click', (params) => {
          // 风险等级映射：很低-1, 较低-2, 中等-3, 较高-4, 很高-5
          const levelMap = {
            很低: '1',
            较低: '2',
            中等: '3',
            较高: '4',
            很高: '5',
          }
          this.$refs.riskDialog.show(
            {
              orgid: this.orgid,
              linkDeptName: params.name,
              cxlevel: levelMap[params.seriesName] || '',
              pgStatus: 'ypg',
            },
            `按部门统计风险评估结果 - ${params.name} - ${params.seriesName}`
          )
        })
      },
    },
  }
</script>

<style scoped></style>
