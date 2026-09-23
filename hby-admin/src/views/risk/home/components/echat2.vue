<template>
  <el-card>
    <div id="chats-2" style="width: 100%; height: 450px"></div>
  </el-card>
</template>

<script>
  import * as echarts from 'echarts'
  import { numberEventsRisks } from '@/api/risk/home.js'
  export default {
    data() {
      return {
        yiBan: 0,
        zhongDa: 0
      }
    },
    created() {
      numberEventsRisks().then(res => {
        if (res && res.code == 1) {
          this.yiBan = res.data.yiBan || 0
          this.zhongDa = res.data.zhongDa || 0
        }
      })
    },
    mounted() {
      var chartDom = document.getElementById('chats-2')

      var myChart = echarts.init(chartDom)
      window.addEventListener('resize', () => {
        setTimeout(() => {
          myChart.resize()
        }, 100)
      })
      var option

      option = {
        title: {
          text: '风险事件数量分布',
          left: '2%',
        },
        tooltip: {
          trigger: 'item',
        },
        legend: {
          bottom: '0%',
          left: 'left',
        },
        series: [
          {
            name: '',
            type: 'pie',
            radius: ['30%', '60%'],
            avoidLabelOverlap: true,
            emphasis: {
              // label: {
              //   show: true,
              //   fontSize: 16,
              //   fontWeight: 'bold',
              // },
            },
            data: [
              { value: this.yiBan, name: '一般风险事件' },
              { value: this.zhongDa, name: '重大风险事件' },
            ],
          },
        ],
      }
      option && myChart.setOption(option)
    },
  }
</script>

<style scoped>
  #chats-2 {
    border: 1px solid #d2d2d2;
  }
</style>
