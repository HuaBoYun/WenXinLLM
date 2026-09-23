<template>
  <div>
    <div
      ref="chart"
      style="width: 100%; height: 259px; margin-bottom: 10px"
    ></div>
    <RiskPointTaskDialog ref="riskDialog" :orgid="orgid" />
  </div>
</template>

<script>
  import * as echarts from 'echarts'
  import { getRiskPointTask } from '@/api/risk/home.js'
  import RiskPointTaskDialog from './RiskPointTaskDialog.vue'

  export default {
    components: {
      RiskPointTaskDialog,
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
        const { data, code, msg } = await getRiskPointTask({
          company: this.orgid,
        })
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
            text: '风险点评估任务完成情况',
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
              name: '评估任务完成情况',
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
                position: 'inside',
                formatter: '{b}: {c} ({d}%)', // 显示风险点名称、数值和百分比
              },
            },
          ],
        }

        this.myChart.setOption(option)

        // 添加点击事件，弹窗显示列表
        this.myChart.on('click', (params) => {
          // 风险状态映射：ypg-已评估, wpg-未评估, pgz-评估中, wxf-未下发
          const statusMap = {
            已评估: 'ypg',
            未评估: 'wpg',
            评估中: 'pgz',
            // 未下发: 'wxf',
          }
          this.$refs.riskDialog.show(
            {
              orgid: this.orgid,
              pgStatus: statusMap[params.name] || params.name,
            },
            `风险点评估任务完成情况 - ${params.name}`
          )
        })
      },
    },
  }
</script>

<style scoped></style>
