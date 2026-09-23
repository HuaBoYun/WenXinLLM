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
        title: {},
        tooltip: {
          trigger: 'item',
        },
        color: ['#D48265', '#46B97C','#AAAAAA'], //环形图两部分的颜色
        graphic: [
          // {
          //   //环形图中间添加文字
          //   type: 'text', //通过不同top值可以设置上下显示
          //   left: 'center',
          //   top: '30%',
          //   style: {
          //     text: '80%',
          //     textAlign: 'center',
          //     fill: '#000', //文字的颜色
          //     width: 30,
          //     height: 30,
          //     fontSize: 18,
          //     color: '#4d4f5c',
          //     fontFamily: 'Microsoft YaHei',
          //   },
          // },
          {
            type: 'text',
            left: 'center',
            top: '42%',
            style: {
              text: '近一周流程',
              textAlign: 'center',
              fill: '#000',
              width: 30,
              height: 30,
              fontSize: 14,
            },
          },
          {
            type: 'text',
            left: 'center',
            top: '50%',
            style: {
              text: '（0）',
              textAlign: 'center',
              fill: '#000',
              width: 30,
              height: 30,
              fontSize: 14,
            },
          },
        ],
        series: [
          {
            // name: '法务人员持证上岗率',
            type: 'pie',
            radius: ['40%', '50%'],
            avoidLabelOverlap: false,
            label: {
              normal: {
                show: false,
                position: 'center',
              },
              emphasis: {
                show: false,
                textStyle: {
                  fontSize: '30',
                  fontWeight: 'bold',
                },
              },
            },
            labelLine: {
              normal: {
                show: false,
              },
            },
            labelLine: {
              show: false,
            },
            data: [
              { value: 0, name: '' },
              { value: 0, name: '未结束流程' },
              { value: 0, name: '已结束流程' },
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
  height: 300px !important;
}
</style>
