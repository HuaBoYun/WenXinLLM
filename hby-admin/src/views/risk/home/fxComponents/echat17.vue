<template>
  <div>
    <div
      ref="chart"
      style="width: 100%; height: 259px; margin-bottom: 10px"
    ></div>
    <EventListDialog ref="eventDialog" />
  </div>
</template>

<script>
  import * as echarts from 'echarts'
  import EventListDialog from './EventListDialog.vue'

  export default {
    components: {
      EventListDialog,
    },
    props: {
      yiBan: {
        type: [String, Number],
        default: 0, // 默认值改为 0，避免空值问题
      },
      zhongDa: {
        type: [String, Number],
        default: 0, // 默认值改为 0，避免空值问题
      },
    },
    data() {
      return {
        riskData: [], // 初始化为空数组
      }
    },
    watch: {
      yiBan: {
        handler() {
          this.updateRiskData()
        },
        immediate: true, // 立即触发一次，确保初始化时更新数据
      },
      zhongDa: {
        handler() {
          this.updateRiskData()
        },
        immediate: true, // 立即触发一次，确保初始化时更新数据
      },
    },
    mounted() {
      this.initChart()
    },
    methods: {
      updateRiskData() {
        // 根据 props 更新 riskData
        this.riskData = [
          { value: this.yiBan, name: '一般风险事件' },
          { value: this.zhongDa, name: '重大风险事件' },
        ]
        this.initChart()
      },
      initChart() {
        const chartDom = this.$refs.chart
        const myChart = echarts.init(chartDom)

        const option = {
          title: {
            text: '全集团风险事件比例',
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
              name: '风险事件类型分布',
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
        // 添加点击事件监听
        myChart.on('click', (params) => {
          const type = params.name == '一般风险事件' ? '1' : '2'
          this.$refs.eventDialog.show({ losseventcategory: type })
        })
        myChart.setOption(option)
      },
    },
  }
</script>

<style scoped></style>
