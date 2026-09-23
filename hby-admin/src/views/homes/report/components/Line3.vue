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
            text: '创建量最高的流程',
            left: 'left',
          },
          tooltip: {
            trigger: 'axis',
          },
          legend: {},
          grid: {
            left: '1%',
            right: '1%',
            bottom: '10%',
            containLabel: true,
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
          calculable: true,
          xAxis: {
            type: 'value',
          },
          yAxis: {
            type: 'category',
            data: [
              '事前-差旅资报销',
              '外部会议模板',
              '政府发文模板',
              '资产盘点任务模板',
              '政府发文稿等',
              '公司会议模板',
              '新人入职类',
              '销售类合同',
              '员工请假申请流程',
              '招聘需求申请表单',
            ],
            axisLabel: {
              width: 105,
              padding: [0, 0, 0, 15],
              overflow: 'truncate',
            },
          },
          series: [
            {
              name: '草稿',
              type: 'bar',
              stack: 'total',
              emphasis: {
                focus: 'series',
              },
              label: {
                show: true,
              },
              // barWidth: '20%',
              itemStyle: {
                normal: {
                  color: '#9FD6FF',
                },
              },
              data: [4, 5, 3, 0, 4, 2, 1, 0, 13, 18],
            },
            {
              name: '待审',
              type: 'bar',
              stack: 'total',
              emphasis: {
                focus: 'series',
              },
              label: {
                show: true,
              },
              // barWidth: '20%',
              itemStyle: {
                normal: {
                  color: '#4175FF',
                },
              },
              data: [5, 3, 1, 0, 2, 0, 3, 0, 0, 12],
            },
            {
              name: '废弃',
              type: 'bar',
              stack: 'total',
              emphasis: {
                focus: 'series',
              },
              label: {
                show: true,
              },
              // barWidth: '20%',
              itemStyle: {
                normal: {
                  color: '#C2C2C2',
                },
              },
              data: [10, 31, 17, 0, 29, 1, 24, 0, 41, 169],
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
              // barWidth: '20%',
              itemStyle: {
                normal: {
                  color: '#FFD37D',
                },
              },
              data: [35, 18, 39, 58, 28, 71, 54, 91, 21, 47],
            },
            {
              name: '错误',
              type: 'bar',
              stack: 'total',
              emphasis: {
                focus: 'series',
              },
              label: {
                show: true,
              },
              // barWidth: '20%',
              itemStyle: {
                normal: {
                  color: '#800000',
                },
              },
              data: [2, 0, 1, 0, 1, 0, 0, 0, 29, 127],
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
