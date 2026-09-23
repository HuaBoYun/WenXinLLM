<template>
  <div class="branch">
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
  </div>
</template>

<script>
  import VabChart from '@/extra/VabChart'
  import { getLegal, getEcharts6 } from '@/api/fwgl/echarts'

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
      getEcharts6({ TblFwglParam: 'TblFwglParam' }).then((res) => {
        if (res.code === 200) {
          this.option = {
            title: {
              text: '全体系本年经营事项合规审核数量',
              left: 'left',
              textStyle: { fontSize: 14, color: '#FF8C00' },
            },
            tooltip: {
              trigger: 'axis',
            },
            legend: {
              left: 'left',
            },
            xAxis: {
              type: 'category',
              data: res.data.map((item) => {
                return item.belongGroupName
              }),
            },
            yAxis: {
              type: 'value',
            },
            series: [
              {
                data: res.data.map((item) => {
                  return item.institutionCount
                }),
                type: 'bar',
                showBackground: true,
                backgroundStyle: {
                  color: 'rgba(180, 180, 180, 0.2)',
                },
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
