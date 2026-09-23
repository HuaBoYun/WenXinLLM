<template>
  <div class="branch">
    <el-card class="branch-card" shadow="hover">
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
            text: '平均审批耗时最长的流程',
            left: 'left',
          },
          tooltip: {
            trigger: 'axis',
          },
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
              '集团会议',
              '市长办公会',
              '质量手册',
              '蓝悦集团签呈单',
              '培训学习',
              '收文',
              '员工退休审批流程',
              '住宿申请流程',
              '产品退货信息反馈申请流程',
              '公司收文单',
            ],
            axisLabel: {
              width: 105,
              padding: [0, 0, 0, 15],
              overflow: 'truncate',
            },
          },
          series: [
            {
              name: '平均审批耗时最长的流程时长',
              type: 'bar',
              data: [542, 557, 569, 681, 1194, 1512, 1758, 1787, 2544, 7146],
              itemStyle: {
                normal: {
                  color: '#3398DB',
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
        // // this.date.year = new Date().getFullYear()
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
