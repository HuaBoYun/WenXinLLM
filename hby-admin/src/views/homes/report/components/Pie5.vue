<template>
  <div class="branch">
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
import { getPersonnel } from '@/api/fwgl/echarts'

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
      option: {
        color: ['#BFD8EF', '#2DC7C9', '#2F84C6'], //环形图两部分的颜色
        tooltip: {
          trigger: 'item',
        },
        legend: {
          left: 'center',
          top: 'bottom',
        },
        toolbox: {
          show: true,
          feature: {   
            restore: { show: true },
            // magicType: { show: true, type: ['line', 'bar'] },
            saveAsImage: { show: true },
            dataView: { show: true, readOnly: false },
          },
        },
        series: [
          {
            type: 'pie',
            radius: '50%',
            data: [
              { value: 0.33, name: '不可用' },
              { value: 0.61, name: '可用' },
              { value: 0.6, name: '专用' },
            ],
          },
        ],
      },
    }
  },
  mounted() {
    // getPersonnel({ TblFwglParam: 'TblFwglParam' }).then(res => {
    //   if (res.code === 200) {
    //     this.option = {
    //       title: {
    //         text: '法务人员持证上岗率',
    //         left: 'center',
    //       },
    //       tooltip: {
    //         trigger: 'item',
    //       },
    //       legend: {
    //         orient: 'vertical',
    //         left: 'left',
    //       },
    //       series: [
    //         {
    //           name: '法务人员持证上岗率',
    //           type: 'pie',
    //           radius: '50%',
    //           data: [
    //             { value: res.data.positive, name: '持证' },
    //             { value: res.data.reverse, name: '未持证' },
    //           ],
    //           emphasis: {
    //             itemStyle: {
    //               shadowBlur: 10,
    //               shadowOffsetX: 0,
    //               shadowColor: 'rgba(0, 0, 0, 0.5)',
    //             },
    //           },
    //         },
    //       ],
    //     }
    //   }
    // })
  },
}
</script>

<style scoped>
.branch-echart1 {
  width: 100% !important;
  height: 260px !important;
}
</style>
