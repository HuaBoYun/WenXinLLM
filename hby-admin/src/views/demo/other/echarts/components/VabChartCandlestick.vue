<template>
  <el-col :lg="8" :md="12" :sm="24" :xl="6" :xs="24">
    <el-card shadow="hover">
      <template #header>
        <span>{{ title }}</span>
      </template>
      <vab-chart
        :init-options="initOptions"
        :option="option"
        theme="vab-echarts-theme"
      />
    </el-card>
  </el-col>
</template>

<script>
  import VabChart from '@/extra/VabChart'

  export default {
    name: 'VabChartCandlestick',
    components: {
      VabChart,
    },
    props: {
      title: {
        type: String,
        default: '',
      },
    },
    data() {
      return {
        initOptions: {
          renderer: 'svg',
        },
        option: {
          grid: {
            top: 20,
            right: 20,
            bottom: 40,
            left: 40,
          },
          xAxis: {
            data: ['2017-10-24', '2017-10-25', '2017-10-26', '2017-10-27'],
          },
          yAxis: {},
          series: [
            {
              type: 'k',
              data: [
                [20, 34, 10, 38],
                [40, 35, 30, 50],
                [31, 38, 33, 44],
                [38, 15, 5, 42],
              ],
            },
          ],
        },
      }
    },
  }
</script>
