<template>
  <el-card>
    <div id="chats-4" style="width: 100%; height: 300px"></div>
  </el-card>
</template>

<script>
  import * as echarts from 'echarts'
  export default {
    mounted() {
      var chartDom = document.getElementById('chats-4')
      var myChart = echarts.init(chartDom)
      var option
      option = {
        title: {
          text: '合同年度统计',
        },
        tooltip: {
          trigger: 'axis',
        },
        legend: {
          data: ['采购类', '销售类', '工程项目类', '咨询服务类', '战略合作类'],
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true,
        },
        toolbox: {
          feature: {
            saveAsImage: {},
          },
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10', '11', '12'],
        },
        yAxis: {
          type: 'value',
        },
        series: [
          {
            name: '采购类',
            type: 'line',
            stack: 'Total',
            data: [120, 132, 101, 134, 90, 230, 210, 120, 132, 101, 134, 90],
          },
          {
            name: '销售类',
            type: 'line',
            stack: 'Total',
            data: [220, 182, 191, 234, 290, 330, 310, 220, 182, 191, 234, 290],
          },
          {
            name: '工程项目类',
            type: 'line',
            stack: 'Total',
            data: [150, 232, 201, 154, 190, 330, 410, 150, 232, 201, 154, 190],
          },
          {
            name: '咨询服务类',
            type: 'line',
            stack: 'Total',
            data: [320, 332, 301, 334, 390, 330, 320, 320, 332, 301, 334, 390],
          },
          {
            name: '战略合作类',
            type: 'line',
            stack: 'Total',
            data: [
              820, 932, 901, 934, 1290, 1330, 1320, 820, 932, 901, 934, 1290,
            ],
          },
        ],
      }
      option && myChart.setOption(option)
    },
  }
</script>
