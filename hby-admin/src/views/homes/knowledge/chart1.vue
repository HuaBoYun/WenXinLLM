<template>
  <el-card>
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
        title: {
          text: '合同类型分析',
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
              { value: 1048, name: '服务类采购' },
              { value: 735, name: '设备类采购' },
              { value: 580, name: '租赁合同' },
              { value: 484, name: '信息类采购' },
              { value: 300, name: '项目施工类' },
            ],
          },
        ],
      }
      option && myChart.setOption(option)
    },
  }
</script>
