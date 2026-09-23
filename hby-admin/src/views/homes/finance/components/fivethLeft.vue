<template>
  <el-card>
    <div class="title">付款情况</div>
    <div id="chats-1" style="width: 100%; height: 300px"></div>
  </el-card>
</template>

<script>
  import * as echarts from 'echarts'
  export default {
    mounted() {
      var chartDom = document.getElementById('chats-1')
      var myChart = echarts.init(chartDom)
      var option
      option = {
        tooltip: {
          trigger: 'item',
        },
        legend: {
          top: '0%',
          left: 'center',
        },

        xAxis: {
          type: 'category',
          data: [
            '一部',
            '二部',
            '三部',
            '四部',
            '五部',
            '六部',
            '七部',
            '八部',
            '九部',
          ],
        },
        yAxis: {
          type: 'value',
        },
        series: [
          {
            name: '付款金额（万元）',
            data: [120, 200, 150, 80, 70, 110, 130, 100, 90],
            type: 'bar',
          },
        ],
      }
      option && myChart.setOption(option)
    },
  }
</script>

<style scoped lang="scss">
  .title {
    font-size: 20px;
    font-weight: 700;
    color: #333;
    padding: 10px 0;
  }
</style>
