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
  import { getPersonnel, getEcharts5 } from '@/api/fwgl/echarts'

  export default {
    components: {
      VabChart,
    },
    data() {
      return {
        queryForm: {
          compere: '',
          conferenceBeginDate: '',
          conferenceEndDate: '',
          conferenceName: '',
          pageNumber: 1,
          pageSize: 20,
        },
        initOptions: {
          renderer: 'svg',
        },
        option: {},
      }
    },
    mounted() {
      getEcharts5({ TblFwglParam: 'TblFwglParam' }).then((res) => {
        if (res.code === 200) {
          this.option = {
            title: {
              text: '法务人员数量',
              left: 'left',
              textStyle: { fontSize: 14, color: '#FF8C00' },
            },
            tooltip: {
              trigger: 'item',
            },
            legend: {
              bottom: 0,
            },
            series: [
              {
                name: '法务人员数量',
                type: 'pie',
                radius: '50%',
                data: [
                  { value: res.data.count, name: '兼职法务人员' },
                  { value: res.data.fullimeCount, name: '专职法务人员' },
                ],
                emphasis: {
                  itemStyle: {
                    shadowBlur: 10,
                    shadowOffsetX: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)',
                  },
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
    height: 300px !important;
  }
</style>
