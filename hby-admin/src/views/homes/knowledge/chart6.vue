<template>
  <el-card>
    <div id="chats-6" style="width: 100%; height: 400px"></div>
  </el-card>
</template>

<script>
  import * as echarts from 'echarts'
  export default {
    mounted() {
      var chartDom = document.getElementById('chats-6')
      var myChart = echarts.init(chartDom)
      var option
      option = {
        title: {
          text: '合同金额月度统计',
          left: 'left',
          top: 0,
          textStyle: {
            color: '#000',
          },
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow',
          },
        },
        legend: {
          top: '5%',
          left: 'right',
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true,
        },
        xAxis: [
          {
            type: 'category',
            data: [
              '1',
              '2',
              '3',
              '4',
              '5',
              '6',
              '7',
              '8',
              '9',
              '10',
              '11',
              '12',
            ],
          },
        ],
        yAxis: [
          {
            type: 'value',
          },
        ],
        series: [
          {
            name: '集团总部',
            type: 'bar',

            data: [320, 332, 301, 334, 390, 330, 320, 320, 332, 301, 334, 390],
          },
          {
            name: '股份公司',
            type: 'bar',

            data: [120, 132, 101, 134, 90, 230, 210, 120, 132, 101, 134, 90],
          },
          {
            name: '高技术',
            type: 'bar',

            data: [220, 182, 191, 234, 290, 330, 310, 220, 182, 191, 234, 290],
          },
          {
            name: '深爱',
            type: 'bar',

            data: [150, 232, 201, 154, 190, 330, 410, 150, 232, 201, 154, 190],
          },
          {
            name: '小贷',
            type: 'bar',
            data: [
              862, 1018, 964, 1026, 1679, 1600, 1570, 862, 1018, 964, 1026,
              1679,
            ],
          },
        ],
      }
      option && myChart.setOption(option)
    },
  }
</script>
