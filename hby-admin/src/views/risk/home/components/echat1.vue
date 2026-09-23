<template>
  <el-card>
    <div id="chats-1" style="width: 100%; height: 450px"></div>
  </el-card>
</template>

<script>
  import * as echarts from 'echarts'
  import { numberRisks } from '@/api/risk/home.js'
  export default {
    data() {
      return {
        risksYiBan: 0,
        riskszhongDa: 0
      }
    },
    created() {
      numberRisks().then(res => {
        if (res && res.code == 1) {
          this.risksYiBan = res.data.risksYiBan || 0
          this.riskszhongDa = res.data.riskszhongDa || 0
        }
      })
    },
    mounted() {
      var chartDom = document.getElementById('chats-1')

      var myChart = echarts.init(chartDom)
      window.addEventListener('resize', () => {
        setTimeout(() => {
          myChart.resize()
        }, 100)
      })
      var option

      option = {
        title: {
          text: '风险数量分布',
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
              //   fontSize: 20,
              //   fontWeight: 'bold',
              // },
            },
            data: [
              { value: this.risksYiBan, name: '一般风险' },
              { value: this.riskszhongDa, name: '重大风险' },
            ],
          },
        ],
      }
      option && myChart.setOption(option)
    },
  }
</script>

<style scoped>
  #chats-1 {
    border: 1px solid #d2d2d2;
  }
</style>
