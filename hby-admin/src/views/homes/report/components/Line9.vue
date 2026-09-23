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
  import { getContractLegal } from '@/api/fwgl/echarts'

  export default {
    components: {
      VabChart,
    },
    data() {
      return {
        date: {},
        initOptions: {
          renderer: 'svg',
        },
        option: {
          title: {
            text: '人员平均耗时蓝榜',
            left: 'left',
          },
          tooltip: {
            trigger: 'axis',
          },
          // legend: {
          //   data: ['数量', '金额'],
          //   left: 'left',
          // },
          toolbox: {
            show: true,
            feature: {
              dataView: { show: true, readOnly: false },
              // magicType: { show: true, type: ['line', 'bar'] },
              restore: { show: true },
              saveAsImage: { show: true },
            },
          },
          grid: {
            left: '1%',
            right: '1%',
            bottom: '10%',
            containLabel: true,
          },
          calculable: true,
          xAxis: {
            type: 'value',
            boundaryGap: [0, 0.01],
          },
          yAxis: {
            type: 'category',
            data: [
              '张孝昆',
              '张孝昆',
              '张孝昆',
              '张孝昆',
              '张孝昆',
              '张孝昆',
              '张孝昆',
              '张孝昆',
              '张孝昆',
              '张孝昆',
            ],
            axisLabel: {
              width: 95,
              padding: [0, 0, 0, 15],
              overflow: 'truncate',
            },
          },
          series: [
            {
              name: '数量',
              type: 'bar',
              data: [1, 1, 1, 1, 1, 1, 1, 2, 2, 6],
              itemStyle: {
                normal: {
                  color: '#ffd37d',
                },
              },
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
        // const res = await getContractLegal(this.date)
        // const names = [],
        //   val1 = [],
        //   val2 = []
        // res.statData.forEach((item, index) => {
        //   //
        //   names.push(item.NAME)
        //   val1.push(item.CNT)
        //   val2.push(item.VALUE)
        // })
        // this.$set(this.option.xAxis, 'data', names)
        // this.$set(this.option.series[0], 'data', val1)
        // this.$set(this.option.series[1], 'data', val2)
      },
    },
  }
</script>

<style scoped>
  .branch-echart1 {
    height: 400px !important;
    width: 100%;
  }
</style>
