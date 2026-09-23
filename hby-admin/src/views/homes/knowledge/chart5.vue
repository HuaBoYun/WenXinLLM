<template>
  <el-card>
    <div id="chats-5" style="width: 100%; height: 400px"></div>
  </el-card>
</template>

<script>
  import * as echarts from 'echarts'
  export default {
    mounted() {
      var chartDom = document.getElementById('chats-5')
      var myChart = echarts.init(chartDom)
      var option
      option = {
        title: {
          text: '合同状态统计',
          left: 'left',
          top: 0,
          textStyle: {
            color: '#000',
          },
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            // Use axis to trigger tooltip
            type: 'shadow', // 'shadow' as default; can also be 'line' or 'shadow'
          },
        },
        legend: {
          top: '0%',
          left: 'right',
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true,
        },
        yAxis: {
          type: 'value',
        },
        xAxis: {
          type: 'category',
          data: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'],
        },
        series: [
          {
            name: '待审核',
            type: 'bar',
            stack: 'total',
            label: {
              show: true,
            },
            emphasis: {
              focus: 'series',
            },
            data: [320, 302, 301, 334, 390, 330, 320, 320, 302, 301, 334, 390],
          },
          {
            name: '待签订',
            type: 'bar',
            stack: 'total',
            label: {
              show: true,
            },
            emphasis: {
              focus: 'series',
            },
            data: [120, 132, 101, 134, 90, 230, 210, 120, 132, 101, 134, 90],
          },
          {
            name: '履约中',
            type: 'bar',
            stack: 'total',
            label: {
              show: true,
            },
            emphasis: {
              focus: 'series',
            },
            data: [220, 182, 191, 234, 290, 330, 310, 220, 182, 191, 234, 290],
          },
          {
            name: '异常',
            type: 'bar',
            stack: 'total',
            label: {
              show: true,
            },
            emphasis: {
              focus: 'series',
            },
            data: [150, 212, 201, 154, 190, 330, 410, 150, 212, 201, 154, 190],
          },
          {
            name: '已结项',
            type: 'bar',
            stack: 'total',
            label: {
              show: true,
            },
            emphasis: {
              focus: 'series',
            },
            data: [
              820, 832, 901, 934, 1290, 1330, 1320, 820, 832, 901, 934, 1290,
            ],
          },
        ],
      }
      option && myChart.setOption(option)
    },
  }
</script>
