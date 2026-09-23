<template>
  <div>
    <div
      ref="chart"
      style="width: 100%; height: 259px; margin-bottom: 10px"
    ></div>
    <RiskDatabaseDialog ref="riskDialog" />
  </div>
</template>

<script>
  import * as echarts from 'echarts'
  import RiskDatabaseDialog from './RiskDatabaseDialog.vue'

  export default {
    components: {
      RiskDatabaseDialog,
    },
    props: {
      risks: {
        type: Array,
        default: () => [], // 修改为函数返回空数组
      },
    },
    data() {
      return {
        riskData: [], // 初始化为空数组
      }
    },
    watch: {
      risks: {
        handler(newVal) {
          if (newVal && newVal.length > 0) {
            this.processRiskData(newVal)
          }
        },
        immediate: true, // 立即触发一次，确保初始化时更新数据
        deep: true,
      },
    },
    mounted() {
      if (this.risks && this.risks.length > 0) {
        this.processRiskData(this.risks)
      } else {
        this.initChart()
      }
    },
    methods: {
      processRiskData(data) {
        // 处理传入的风险数据
        this.riskData = data.map((item) => ({
          value: item.NUMBER,
          name: item.NAME || '',
        }))

        // 按数值从大到小排序，便于展示
        this.riskData.sort((a, b) => b.value - a.value)

        this.initChart()
      },
      initChart() {
        const chartDom = this.$refs.chart
        if (!chartDom) return

        const myChart = echarts.init(chartDom)

        const option = {
          title: {
            text: '全集团风险领域统计',
            left: 'left',
            textStyle: { fontSize: 14, color: '#FF8C00' },
          },
          tooltip: {
            trigger: 'item',
            formatter: '{a} <br/>{b}: {c} ({d}%)',
          },
          legend: {
            type: 'scroll',
            orient: 'horizontal',
            bottom: 0,
            left: 'center',
            data: this.riskData.map((item) => item.name),
          },
          series: [
            {
              name: '风险统计',
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
                position: 'outside',
                formatter: '{b}: {c} ({d}%)', // 显示风险点名称、数值和百分比
                alignTo: 'edge',
                edgeDistance: '10%',
              },
              labelLine: {
                show: true,
              },
            },
          ],
        }
        // 添加点击事件监听
        myChart.on('click', (params) => {
          this.$refs.riskDialog.show({ riskcatname: params.name })
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
