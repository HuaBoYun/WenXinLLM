<template>
  <div class="branch">
    <el-card class="branch-card" shadow="hover">
      <template #header>
        <span>
          <vab-icon icon="donut-chart-fill" />
          专项法务服务次数及额度情况统计图表
        </span>
      </template>
      <vab-chart
        class="branch-echart1"
        :init-options="initOptions"
        :option="option"
        theme="vab-echarts-theme"
      />
    </el-card>
  </div>
</template>

<script>
  import VabChart from '@/extra/VabChart'
  const option = {
    color: ['rgba(50, 229, 255, 1)', 'rgba(250, 207, 18, 1)'],
    tooltip: {
      trigger: 'axis',
      axisPointer: {
        type: 'cross',
        crossStyle: {
          color: '#999',
        },
      },
    },
    grid: {
      // top: '30%',
      // left: '10%',
      // right: '10%',
      // bottom: '20%',
      containLabel: true,
    },
    xAxis: [
      {
        type: 'category',
        data: [
          '国资公司',
          '发展公司',
          '富浙租赁',
          '富客户C本',
          '富客户C产',
          '盐业公司',
          '环科公司',
        ],
        axisLabel: {
          rotate: 30, // 倾斜30度
        },
        axisLine: {
          show: true,
          lineStyle: {
            color: '#111',
          },
        },
        splitLine: {
          show: false,
          lineStyle: {
            color: 'blue',
            width: 1,
            type: 'solid',
          },
        },
        axisPointer: {
          type: 'shadow',
        },
      },
    ],
    yAxis: [
      {
        type: 'value',
        axisLabel: {
          color: '#FF6347',
        },
        axisLine: {
          show: true,
          lineStyle: {
            color: '#111',
          },
        },
        splitLine: {
          show: false,
          lineStyle: {
            color: 'blue',
            width: 1,
            type: 'solid',
          },
        },
        nameTextStyle: {
          rich: {
            legend: {
              width: 12,
              height: 4,
              backgroundColor: '#EE82EE',
            },
            value: {
              color: '#FF8C00',
            },
          },
        },
      },
      {
        type: 'value',
        axisLabel: {
          color: '#111',
        },
        axisLine: {
          show: true,
          lineStyle: {
            color: '#111',
          },
        },
        splitLine: {
          lineStyle: {
            color: '#FFDEAD',
            width: 1,
            type: 'solid',
          },
        },
        nameTextStyle: {
          rich: {
            legend: {
              width: 12,
              height: 4,
              backgroundColor: '#FFDEAD',
            },
            value: {
              color: '#FFDEAD',
            },
          },
        },
      },
    ],
    series: [
      {
        name: '柱状图',
        type: 'bar',
        data: [80, 120, 110, 130, 120, 110, 170],
        barWidth: 14, // 柱状图的宽度
        itemStyle: {
          normal: {
            //这里是颜色
            color: function (params) {
              //注意，如果颜色太少的话，后面颜色不会自动循环，最好多定义几个颜色
              var colorList = [
                '#FFA100',
                '#FFA100',
                '#FFA100',
                '#FFA100',
                '#FFA100',
                '#FFA100',
                '#FFA100',
                '#FFA100',
                '#FFA100',
              ]
              return colorList[params.dataIndex]
            },
          },
        },
      },
      {
        name: '柱状图',
        type: 'bar',
        data: [10, 20, 10, 30, 20, 10, 60],
        barWidth: 14, // 柱状图的宽度
        itemStyle: {
          normal: {
            //这里是颜色
            color: function (params) {
              //注意，如果颜色太少的话，后面颜色不会自动循环，最好多定义几个颜色
              var colorList = [
                '#7FFFD4',
                '#7FFFD4',
                '#7FFFD4',
                '#7FFFD4',
                '#7FFFD4',
                '#7FFFD4',
                '#7FFFD4',
                '#7FFFD4',
                '#7FFFD4',
              ]
              return colorList[params.dataIndex]
            },
          },
        },
      },
      {
        name: '折线图',
        type: 'line',
        yAxisIndex: 1,
        data: [80, 120, 110, 130, 120, 110, 160],
        itemStyle: {
          normal: {
            color: '#111',
            lineStyle: {
              color: '#0000FF',
            },
          },
        },
        showSymbol: false,
      },
    ],
  }

  export default {
    components: {
      VabChart,
    },
    data() {
      return {
        initOptions: {
          renderer: 'svg',
        },
        option: option,
      }
    },
  }
</script>

<style scoped>
  .branch-echart1 {
    height: 400px !important;
  }
</style>
