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
            text: '超时转办次数最多的员工排名',
            // subtext: '说明：最近10天进入队列的工作项数量',
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
              // restore: { show: true },
              // saveAsImage: { show: true },
            },
          },
          calculable: true,
          xAxis: {
            type: 'category',
            axisLabel: {
              rotate: 38, // 倾斜30度
            },
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
          },
          yAxis: [
            {
              type: 'value',
            },
          ],
          series: [
            {
              name: '系统负载',
              type: 'bar',
              data: [4, 1, 0, 8, 240, 186, 147, 158, 33, 24],
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
    width: 100% !important;
    height: 400px !important;
  }
</style>
