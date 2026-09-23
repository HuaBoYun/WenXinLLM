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
  import { getRiskLosseventcategory } from '@/api/risk/home.js'
  import EventListDialog from './EventListDialog.vue'

  export default {
    components: {
      EventListDialog,
    },
    props: {
      orgid: {
        type: [String, Number],
        default: '1000',
      },
      orgname: {
        type: String,
        default: '',
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
      orgid: {
        immediate: true, // 立即执行一次
        handler(newVal, oldVal) {
          if (newVal != oldVal) {
            this.fetchRiskData()
          }
        },
      },
      riskData: {
        handler() {
          this.initChart()
        },
        deep: true,
      },
    },
    methods: {
      async fetchRiskData() {
        const { data, code, msg } = await getRiskLosseventcategory({
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
            text: '风险事件类型分布',
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

        this.myChart.setOption(option)

        // 添加点击事件，弹窗显示事件列表
        this.myChart.on('click', (params) => {
          // 根据点击的名称判断类型：重大风险传2，一般风险传1
          let type = ''
          if (params.name.includes('重大')) {
            type = '2'
          } else if (params.name.includes('一般')) {
            type = '1'
          }
          this.$refs.eventDialog.show({
            losseventcategory: type,
            companyname: this.orgname,
          })
        })
      },
    },
  }
</script>

<style scoped></style>
