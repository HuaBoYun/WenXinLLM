<template>
  <el-card>
    <div
      id="chats-7"
      style="width: 100%; height: 360px; padding: 10px; margin-bottom: 10px"
    ></div>
  </el-card>
</template>

<script>
  import * as echarts from 'echarts'
  import { numberEventsRisks } from '@/api/risk/home.js'

  export default {
    props: {
      orgid: {
        type: [String, Number],
        default: '1000',
      },
    },
    data() {
      return {
        yiBan: 0,
        zhongDa: 0,
      }
    },
    watch: {
      orgid: {
        immediate: true, // 立即执行一次
        handler(newVal, oldVal) {
          if (newVal != oldVal) {
            numberEventsRisks().then((res) => {
              if (res && res.code == 1) {
                this.yiBan = res.data.yiBan || 0
                this.zhongDa = res.data.zhongDa || 0
                this.initChart() // 在获取数据后初始化图表
              }
            })
          }
        },
      },
    },
    /**
     * @description:初始化，调用接口，获取数据
     * @param {*}
     * @return {*}
     */
    created() {
      numberEventsRisks().then((res) => {
        if (res && res.code == 1) {
          this.yiBan = res.data.yiBan || 0
          this.zhongDa = res.data.zhongDa || 0
          this.initChart() // 在获取数据后初始化图表
        }
      })
    },
    /**
     * @description:绘制图表
     * @param {*}
     * @return {*}
     */
    methods: {
      initChart() {
        var chartDom = document.getElementById('chats-7')
        var myChart = echarts.init(chartDom)

        window.addEventListener('resize', () => {
          setTimeout(() => {
            myChart.resize()
          }, 100)
        })

        var option = {
          title: {
            text: '风险事件数量分布',
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
            data: ['一般风险事件', '重大风险事件'],
          },
          series: [
            {
              name: '风险事件数量分布',
              type: 'pie',
              radius: '50%',
              center: ['50%', '60%'],
              data: [
                { value: this.yiBan, name: '一般风险事件' },
                { value: this.zhongDa, name: '重大风险事件' },
              ],
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

        myChart.setOption(option)
      },
    },
  }
</script>

<style scoped></style>
