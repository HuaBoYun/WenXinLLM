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
  import { getLegal } from '@/api/fwgl/echarts'

  export default {
    components: {
      VabChart,
    },
    data() {
      return {
        initOptions: {
          renderer: 'svg',
        },
        option: {},
      }
    },
    mounted() {
      getLegal({ TblFwglParam: 'TblFwglParam' }).then((res) => {
        if (res.code === 200) {
          this.option = {
            // title: {
            //   text: '本年重大决策法律合规审查数量',
            //   left: 'center',
            // },
            // xAxis: {
            //   type: 'category',
            //   boundaryGap: false,
            //   data: res.data.map(v => { return v.workUnitName }),
            // },
            // yAxis: {
            //   type: 'value',
            // },
            // series: [
            //   {
            //     data: res.data.map(v => { return v.reviewNumber }),
            //     type: 'line',
            //     areaStyle: {},
            //   },
            // ],
            title: {
              text: '本年制度法律合规审查数量',
              left: 'center',
            },
            xAxis: {
              type: 'category',
              boundaryGap: false,
              data: res.data.map((v) => {
                return v.workUnitName
              }),
            },
            yAxis: {
              type: 'value',
            },
            series: [
              {
                data: res.data.map((v) => {
                  return v.reviewNumber
                }),
                type: 'line',
                areaStyle: {},
              },
            ],
          }
        }
      })
    },
  }
</script>

<style scoped>
  .branch-echart1 {
    height: 400px !important;
  }
</style>
