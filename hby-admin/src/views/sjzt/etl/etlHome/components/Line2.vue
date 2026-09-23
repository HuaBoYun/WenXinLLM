<template>
  <div class="branch">
    <el-card class="branch-card" shadow="hover">
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

  export default {
    components: {
      VabChart,
    },
    props: ['jobSuccess', 'jobFail'],
    data() {
      return {
        initOptions: {
          renderer: 'svg',
        },
        option: {
          title: {
            text: '作业运行情况',
            left: 'center',
          },
          legend: {
            data: ['成功', '失败'],
            left: 'left',
          },
          xAxis: {
            type: 'category',
            boundaryGap: false,
            data: [],
          },
          yAxis: {
            type: 'value',
          },
          series: [
            {
              name: '成功',
              type: 'line',
              data: [],
              // markPoint: {
              //   data: [
              //     { type: 'max', name: 'Max' },
              //     { type: 'min', name: 'Min' },
              //   ],
              // },
              // markLine: {
              //   data: [{ type: 'average', name: 'Avg' }]
              // }
            },
            {
              name: '失败',
              type: 'line',
              data: [],
            },
          ],
        },
      }
    },
    watch: {
      props: function () {
        this.getDateList()
      },
    },
    created() {
      this.getDateList()
    },
    methods: {
      async getDateList() {
        this.jobSuccess.forEach((key) => {
          this.option.xAxis.data.push(key.date)
          this.option.series[0].data.push(key.value)
        })
        this.jobFail.forEach((key) => {
          this.option.series[1].data.push(key.value)
        })
      },
    },
  }
</script>

<style scoped>
  .branch-echart1 {
    height: 400px !important;
  }
</style>
