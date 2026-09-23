<template>
  <div>
    <div
      ref="chart"
      style="width: 100%; height: 360px; padding: 10px; margin-bottom: 10px"
    ></div>
    <ReportIndexDialog ref="reportIndexDialog" />
  </div>
</template>

<script>
  import * as echarts from 'echarts'
  import { getRiskReportTypeCountByCompany } from '@/api/risk/home.js'
  import ReportIndexDialog from './ReportIndexDialog.vue'

  export default {
    components: {
      ReportIndexDialog,
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
        const { data, code, msg } = await getRiskReportTypeCountByCompany({
          company: this.orgid,
        })
        // this.riskData = data.data
        this.riskData = data.data.map((item) => ({
          name: item.name || '未知',
          value: item.value,
        }))
      },
      initChart() {
        const chartDom = this.$refs.chart
        if (this.myChart) {
          this.myChart.dispose()
        }
        this.myChart = echarts.init(chartDom)

        const option = {
          title: {
            text: '风险报告类型统计',
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
            data: this.riskData.map((item) => item.name),
          },
          series: [
            {
              name: '风险报告类型统计',
              type: 'pie',
              radius: '50%',
              center: ['50%', '43%'],
              data: this.riskData,
              emphasis: {
                itemStyle: {
                  shadowBlur: 10,
                  shadowOffsetX: 0,
                  shadowColor: 'rgba(0, 0, 0, 0.5)',
                },
              },
              label: {
                show: true,
                formatter: '{b}: {c} ({d}%)', // 显示风险点名称、数值和百分比
              },
            },
          ],
        }

        this.myChart.setOption(option)

        // 添加点击事件，弹窗显示报告列表
        this.myChart.on('click', (params) => {
          this.$refs.reportIndexDialog.show({
            type: 'fx_zdy',
            title: params.name,
          })
        })
      },
    },
  }
</script>

<style scoped></style>
