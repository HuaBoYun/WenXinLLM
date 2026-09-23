<template>
  <div>
    <!-- <template #header>
        <span>
          <vab-icon icon="donut-chart-fill" />
          专项法务服务费统计
        </span>
      </template> -->
    <vab-chart
      class="branch-echart1"
      :init-options="initOptions"
      :option="option"
      theme="vab-echarts-theme"
      style="width: 100%; height: 460px; padding: 10px; margin-bottom: 10px"
    />
  </div>
</template>

<script>
  import VabChart from '@/extra/VabChart'
  import { getEcharts8 } from '@/api/fwgl/echarts'
  export default {
    components: {
      VabChart,
    },
    data() {
      return {
        initOptions: {
          renderer: 'svg',
        },
        option: {
          title: {
            text: '全体系经营事项及制度审核数量',
            left: 'left',
            textStyle: { fontSize: 14, color: '#FF8C00' },
          },
          tooltip: {
            trigger: 'axis',
          },
          legend: {
            data: ['经营事项数量', '制度审核数量'],
            bottom: 0,
          },
          toolbox: {
            show: true,
            feature: {
              dataView: { show: true, readOnly: false },
              magicType: { show: true, type: ['line', 'bar'] },
              restore: { show: true },
              saveAsImage: { show: true },
            },
          },
          calculable: true,
          xAxis: {
            type: 'category',
            data: [],
          },
          yAxis: [
            {
              type: 'value',
            },
          ],
          series: [
            {
              name: '经营事项数量',
              type: 'bar',
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
              name: '制度审核数量',
              type: 'bar',
              data: [],
              // markPoint: {
              //   data: [
              //     { name: 'Max', value: 182.2, xAxis: 7, yAxis: 183 },
              //     { name: 'Min', value: 2.3, xAxis: 11, yAxis: 3 },
              //   ],
              // },
              // markLine: {
              //   data: [{ type: 'average', name: 'Avg' }]
              // }
            },
          ],
        },
      }
    },
    created() {
      this.fetchData()
    },
    methods: {
      async fetchData() {
        // this.date.year = new Date().getFullYear()
        const res = await getEcharts8({})
        const names = [],
          val1 = [],
          val2 = []
        res.data.forEach((item, index) => {
          names.push(item.belongGroupName)
          val1.push(item.institutionCount)
          val2.push(item.manageCount)
        })
        this.$set(this.option.xAxis, 'data', names)
        this.$set(this.option.series[0], 'data', val1)
        this.$set(this.option.series[1], 'data', val2)
      },
    },
  }
</script>

<style scoped>
  .branch-echart1 {
    height: 400px !important;
  }
</style>
