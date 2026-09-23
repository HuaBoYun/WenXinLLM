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
            text: '超时处理最多流程',
            left: 'left',
          },
          tooltip: {
            trigger: 'axis',
          },
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
          legend: {},
          xAxis: {
            type: 'value',
          },
          yAxis: {
            type: 'category',
            data: [
              '在职证明（收入）确认申请流程',
              '外派项目人员申请流程',
              '员工转正申请流程',
            ],
            axisLabel: {
              width: 95,
              padding: [0, 0, 0, 15],
              overflow: 'truncate',
            },
          },
          series: [
            {
              name: '运行中',
              type: 'bar',
              stack: 'total',
              emphasis: {
                focus: 'series',
              },
              label: {
                show: true,
              },
              barWidth: '20%',
              itemStyle: {
                normal: {
                  color: '#4175FF',
                },
              },
              data: [0, 0, 1],
            },
            {
              name: '已结束',
              type: 'bar',
              stack: 'total',
              emphasis: {
                focus: 'series',
              },
              label: {
                show: true,
              },
              barWidth: '20%',
              itemStyle: {
                normal: {
                  color: '#C2C2C2',
                },
              },
              data: [2, 4, 11],
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
    width: 100% !important;
    height: 400px !important;
  }
</style>
