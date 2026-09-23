<template>
  <el-card>
    <div id="chats-3" style="width: 100%; height: 300px"></div>
  </el-card>
</template>

<script>
  import * as echarts from 'echarts'
  export default {
    mounted() {
      var chartDom = document.getElementById('chats-3')
      var myChart = echarts.init(chartDom)
      var option
      option = {
        title: {
          text: '印章类型分布',
          left: 'left',
          top: 0,
          textStyle: {
            color: '#000',
          },
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
            radius: ['20%', '50%'],
            avoidLabelOverlap: false,
            label: {
              show: true,
            },
            emphasis: {
              label: {
                show: true,
                fontSize: 20,
              },
            },
            labelLine: {
              show: true,
            },
            data: [
              { value: 1048, name: '部门章' },
              { value: 735, name: '财务章' },
              { value: 580, name: '合同章' },
              { value: 484, name: '项目章' },
              { value: 300, name: '业务专用章' },
            ],
          },
        ],
      }
      option && myChart.setOption(option)
    },
  }
</script>
