
<template>
  <el-card>
    <div id="chats-3" style="width: 100%; height: 450px"></div>
  </el-card>
</template>

<script>
  import * as echarts from 'echarts'
  export default {
    mounted() {
      var chartDom = document.getElementById('chats-3')

      var myChart = echarts.init(chartDom)
      window.addEventListener('resize', () => {
        setTimeout(() => {
          myChart.resize()
        }, 100)
      })
      var option

      option = {
        title: {
          text: '风险来源分布',
          left: '2%',
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            // Use axis to trigger tooltip
            type: 'shadow', // 'shadow' as default; can also be 'line' or 'shadow'
          },
        },
        legend: {
          bottom: '20%',
          left: 'right',
          orient: 'vertical',
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true,
        },
        xAxis: {
          type: 'value',
        },
        yAxis: {
          type: 'category',
          data: [
            '仓储运输类',
            '仓储运输类',
            '仓储运输类',
            '仓储运输类',
            '仓储运输类',
          ],
        },
        series: [
          {
            name: '交易对象风险',
            type: 'bar',
            stack: 'total',
            label: {
              show: true,
            },
            emphasis: {
              focus: 'series',
            },
            data: [320, 302, 301, 334, 390],
          },
          {
            name: '合同结算风险',
            type: 'bar',
            stack: 'total',
            label: {
              show: true,
            },
            emphasis: {
              focus: 'series',
            },
            data: [120, 132, 101, 134, 90],
          },
          {
            name: '合同履约风险',
            type: 'bar',
            stack: 'total',
            label: {
              show: true,
            },
            emphasis: {
              focus: 'series',
            },
            data: [220, 182, 191, 234, 290],
          },
          {
            name: '合同文本风险',
            type: 'bar',
            stack: 'total',
            label: {
              show: true,
            },
            emphasis: {
              focus: 'series',
            },
            data: [150, 212, 201, 154, 190],
          },
          {
            name: '合同签订风险',
            type: 'bar',
            stack: 'total',
            label: {
              show: true,
            },
            emphasis: {
              focus: 'series',
            },
            data: [820, 832, 901, 934, 1290],
          },
        ],
      }
      option && myChart.setOption(option)
    },
  }
</script>

<style scoped>
  #chats-3 {
    border: 1px solid #d2d2d2;
  }
</style>